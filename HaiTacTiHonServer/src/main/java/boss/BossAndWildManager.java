package boss;

import core.Manager;
import core.ZUtil;
import map.Map;
import map.MapManager;
import map.MapTemplate;
import map.Zone;
import mob.Mob;
import model.DeTu;
import model.Player;
import template.MobTemplate;
import zabstracts.AbsBoss;
import zabstracts.AbsEventBoss;
import zabstracts.AbsWorldBoss;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * BossAndWildManager — Quản lý theo dõi vị trí và Spawn linh hoạt
 * cho Boss, Siêu Trùm và Đệ Tử Hoang trên Game Server HTTH.
 *
 * Cung cấp API chuẩn xác cho:
 * 1. Tra cứu chi tiết vị trí (Tên Map, ID Map, ID Khu, Tọa độ X/Y, Máu, Trạng thái).
 * 2. Spawn Random số lượng phân bổ đều, đảm bảo KHÔNG TRÙNG MAP / KHU.
 * 3. Spawn Tùy Chọn: Nhập Map ID (bỏ trống = Random), Nhập Zone ID (bỏ trống = Random).
 * 4. Dịch chuyển nhanh Admin đến tận nơi, tiêu diệt/despawn và dọn dẹp hàng loạt.
 */
public class BossAndWildManager {

    private static volatile BossAndWildManager instance;

    // Danh sách Map phiêu lưu phổ biến thích hợp cho việc spawn ngẫu nhiên
    public static final int[] DEFAULT_ADVENTURE_MAPS = new int[]{
        1, 2, 9, 10, 17, 18, 19, 25, 26, 27, 33, 34, 35, 41, 42, 43, 49, 50, 51,
        66, 67, 68, 69, 70, 71, 83, 84, 85, 93, 94, 95, 107, 108, 109, 110, 163, 193
    };

    private BossAndWildManager() {
    }

    public static BossAndWildManager gI() {
        if (instance == null) {
            synchronized (BossAndWildManager.class) {
                if (instance == null) {
                    instance = new BossAndWildManager();
                }
            }
        }
        return instance;
    }

    // =========================================================================
    // DTO: TRACKED ENTITY
    // =========================================================================

    public static class TrackedEntityDTO {
        public String category;     // SIÊU TRÙM, BOSS THƯỜNG, BOSS THẾ GIỚI, EVENT BOSS, ĐỆ HOANG
        public int id;              // mob_id hoặc index_map
        public String name;         // Tên hiển thị
        public int mapId = -1;
        public String mapName = "Chưa xác định";
        public int zoneId = -1;     // 0-indexed (Khu = zoneId + 1)
        public short x = -1;
        public short y = -1;
        public long hp = 0;
        public long maxHp = 0;
        public int level = 1;
        public boolean isAlive = false;
        public String statusText = "Chưa xuất hiện";
        public String extraInfo = "";
        public Object rawEntity;    // AbsBoss, DeTu, Mob

        public String getFullLocation() {
            if (mapId < 0) return "Chưa xuất hiện";
            return mapName + " (ID: " + mapId + ") - Khu " + (zoneId + 1) + " [" + x + ", " + y + "]";
        }

        public String getHpDisplay() {
            if (!isAlive) return "0 / " + ZUtil.number_format(maxHp);
            return ZUtil.number_format(hp) + " / " + ZUtil.number_format(maxHp);
        }
    }

    // =========================================================================
    // 1. LIVE TRACKING & QUERY APIS
    // =========================================================================

    /**
     * Lấy toàn bộ danh sách thực thể theo dõi (Siêu Trùm, Boss, Đệ Hoang).
     */
    public List<TrackedEntityDTO> getAllTrackedEntities() {
        List<TrackedEntityDTO> list = new ArrayList<>();
        list.addAll(getSuperBossEntities());
        list.addAll(getNormalAndWorldBossEntities());
        list.addAll(getWildDeTuEntities());
        return list;
    }

    /**
     * Lấy danh sách Siêu Trùm
     */
    public List<TrackedEntityDTO> getSuperBossEntities() {
        List<TrackedEntityDTO> list = new ArrayList<>();
        SuperBossManager.init();

        Set<Integer> trackedMobIds = new HashSet<>();

        // 1. Quét các AbsBoss đang tồn tại trong SuperBossManager.ENTRYS
        if (SuperBossManager.ENTRYS != null) {
            for (AbsBoss b : SuperBossManager.ENTRYS) {
                if (b == null) continue;
                TrackedEntityDTO dto = new TrackedEntityDTO();
                dto.category = "SIÊU TRÙM";
                dto.id = b.id;
                dto.name = (b.mtemplate != null && b.mtemplate.name != null && !b.mtemplate.name.isBlank())
                        ? b.mtemplate.name : ("Siêu Trùm " + b.id);
                dto.level = b.level;
                dto.maxHp = b.hp_max > 0 ? b.hp_max : b.getHpMax();
                dto.hp = b.hp;
                dto.isAlive = !b.isdie && b.hp > 0 && b.map != null;
                dto.rawEntity = b;

                if (b.map != null && b.map.template != null) {
                    dto.mapId = b.map.template.id;
                    dto.mapName = b.map.template.name;
                    dto.zoneId = b.map.zone_id;
                    dto.x = (short) b.x;
                    dto.y = (short) b.y;
                } else {
                    short[] loc = b.resolveSpawnLocation();
                    if (loc != null && loc.length >= 3) {
                        dto.mapId = loc[AbsBoss.LOC_MAP_ID];
                        dto.mapName = getMapNameById(dto.mapId);
                        dto.x = loc[AbsBoss.LOC_X];
                        dto.y = loc[AbsBoss.LOC_Y];
                    }
                }

                if (dto.isAlive) {
                    dto.statusText = "ĐANG SỐNG (" + ZUtil.number_format(dto.hp) + " HP)";
                } else {
                    dto.statusText = "ĐÃ CHẾT / CHƯA SPAWN";
                }
                dto.extraInfo = "Cấp " + b.levelBoss;
                list.add(dto);
                trackedMobIds.add(b.id);
            }
        }

        // 2. Kiểm tra các Siêu Trùm trong ID_BOSS chưa có trong ENTRYS
        for (int i = 0; i < SuperBossManager.ID_BOSS.length; i++) {
            short mobId = SuperBossManager.ID_BOSS[i];
            if (!trackedMobIds.contains((int) mobId)) {
                TrackedEntityDTO dto = new TrackedEntityDTO();
                dto.category = "SIÊU TRÙM";
                dto.id = mobId;
                dto.name = SuperBossManager.getBossName(i);
                AbsBoss b = SuperBossManager.getBossByMobId(mobId);
                MobTemplate mt = MobTemplate.get_mob_template(mobId);
                dto.maxHp = b != null ? b.getHpMax() : (mt != null ? mt.hp_max : 1_000_000);
                dto.hp = 0;
                dto.isAlive = false;
                dto.statusText = "CHƯA XUẤT HIỆN";
                list.add(dto);
            }
        }

        return list;
    }

    /**
     * Lấy danh sách Boss Thường, Boss Thế Giới, Boss Pica và Event Boss
     */
    public List<TrackedEntityDTO> getNormalAndWorldBossEntities() {
        List<TrackedEntityDTO> list = new ArrayList<>();

        // 1. Boss Thế Giới (ID 172)
        try {
            BossTheGioi btg = BossTheGioi.gI();
            TrackedEntityDTO dto = new TrackedEntityDTO();
            dto.category = "BOSS THẾ GIỚI";
            dto.id = 172;
            dto.name = "Boss Thế Giới";
            dto.maxHp = btg.getHpMax();
            dto.hp = btg.hp;
            dto.level = btg.getLevel();
            dto.isAlive = !btg.isdie && btg.hp > 0 && btg.map != null;
            dto.rawEntity = btg;
            if (btg.map != null && btg.map.template != null) {
                dto.mapId = btg.map.template.id;
                dto.mapName = btg.map.template.name;
                dto.zoneId = btg.map.zone_id;
                dto.x = (short) btg.x;
                dto.y = (short) btg.y;
            }
            dto.statusText = dto.isAlive ? "ĐANG SỐNG" : "CHƯA XUẤT HIỆN";
            list.add(dto);
        } catch (Exception ignored) {}

        // 2. Boss Pica (ID 173)
        try {
            BossPica bp = BossPica.gI();
            TrackedEntityDTO dto = new TrackedEntityDTO();
            dto.category = "BOSS THẾ GIỚI";
            dto.id = 173;
            dto.name = "Boss Pica";
            dto.maxHp = bp.getHpMax();
            dto.hp = bp.hp;
            dto.level = bp.getLevel();
            dto.isAlive = !bp.isdie && bp.hp > 0 && bp.map != null;
            dto.rawEntity = bp;
            if (bp.map != null && bp.map.template != null) {
                dto.mapId = bp.map.template.id;
                dto.mapName = bp.map.template.name;
                dto.zoneId = bp.map.zone_id;
                dto.x = (short) bp.x;
                dto.y = (short) bp.y;
            }
            dto.statusText = dto.isAlive ? "ĐANG SỐNG" : "CHƯA XUẤT HIỆN";
            list.add(dto);
        } catch (Exception ignored) {}

        // 3. Quét các Boss Thường trong BossManager (không phải siêu trùm)
        try {
            for (AbsBoss b : BossManager.gI().getBosses()) {
                if (b == null || b.isSieuTrum()) continue;
                TrackedEntityDTO dto = new TrackedEntityDTO();
                dto.category = "BOSS THƯỜNG";
                dto.id = b.id;
                dto.name = (b.mtemplate != null && b.mtemplate.name != null) ? b.mtemplate.name : ("Boss " + b.id);
                dto.maxHp = b.getHpMax();
                dto.hp = b.hp;
                dto.level = b.getLevel();
                dto.isAlive = !b.isdie && b.hp > 0 && b.map != null;
                dto.rawEntity = b;
                if (b.map != null && b.map.template != null) {
                    dto.mapId = b.map.template.id;
                    dto.mapName = b.map.template.name;
                    dto.zoneId = b.map.zone_id;
                    dto.x = (short) b.x;
                    dto.y = (short) b.y;
                }
                dto.statusText = dto.isAlive ? "ĐANG SỐNG" : "CHƯA XUẤT HIỆN";
                list.add(dto);
            }
        } catch (Exception ignored) {}

        return list;
    }

    /**
     * Lấy danh sách toàn bộ Đệ Tử Hoang đang có trên server
     */
    public List<TrackedEntityDTO> getWildDeTuEntities() {
        List<TrackedEntityDTO> list = new ArrayList<>();
        if (DeTu.WILD_DETU_MAP == null) return list;

        for (DeTu dt : DeTu.WILD_DETU_MAP.values()) {
            if (dt == null) continue;
            TrackedEntityDTO dto = new TrackedEntityDTO();
            dto.category = "ĐỆ HOANG";
            dto.id = dt.index_map;
            String clazzName = (dt.clazz >= 1 && dt.clazz <= model.Clazz.NAME.length)
                    ? model.Clazz.NAME[dt.clazz - 1] : "Vô Phái";
            dto.name = "Đệ Tử Hoang (" + clazzName + ")";
            dto.level = dt.level;
            dto.maxHp = (dt.ability != null) ? dt.ability.get_hp_max(true) : 1000;
            dto.hp = dt.hp > 0 ? dt.hp : dto.maxHp;
            dto.isAlive = !dt.isdie && dt.map != null && dt.master == null;
            dto.rawEntity = dt;

            if (dt.map != null && dt.map.template != null) {
                dto.mapId = dt.map.template.id;
                dto.mapName = dt.map.template.name;
                dto.zoneId = dt.map.zone_id;
                dto.x = (short) dt.x;
                dto.y = (short) dt.y;
            }

            dto.statusText = dto.isAlive ? "ĐANG TỒN TẠI (Chờ Sư Phụ)" : "KHÔNG KHẢ DỤNG";
            dto.extraInfo = "Phái: " + clazzName + " | Cấp " + dt.level;
            list.add(dto);
        }

        return list;
    }

    /**
     * Tạo báo cáo chuỗi tổng hợp trực quan để hiển thị hộp thoại in-game
     */
    public String getLiveStatusSummary(String filterCategory) {
        StringBuilder sb = new StringBuilder();
        List<TrackedEntityDTO> all = getAllTrackedEntities();

        if (filterCategory != null && !filterCategory.equalsIgnoreCase("ALL")) {
            all = all.stream().filter(e -> e.category.equalsIgnoreCase(filterCategory)).collect(Collectors.toList());
        }

        long aliveCount = all.stream().filter(e -> e.isAlive).count();
        sb.append("=== DANH SÁCH & VỊ TRÍ THỰC THỂ ===\n");
        sb.append("• Đang hoạt động: ").append(aliveCount).append(" / ").append(all.size()).append("\n\n");

        if (all.isEmpty()) {
            sb.append("Hiện không có thực thể nào phù hợp.");
            return sb.toString();
        }

        int idx = 1;
        for (TrackedEntityDTO e : all) {
            String mark = e.isAlive ? "" : "";
            sb.append(idx++).append(". ").append(mark).append(" [").append(e.category).append("] ")
              .append(e.name).append("\n");
            if (e.isAlive) {
                sb.append("   - Vị trí: ").append(e.mapName).append(" (Map ").append(e.mapId).append(") - Khu ").append(e.zoneId + 1)
                  .append(" [").append(e.x).append(", ").append(e.y).append("]\n");
                sb.append("   - HP: ").append(ZUtil.number_format(e.hp)).append(" / ").append(ZUtil.number_format(e.maxHp)).append("\n");
            } else {
                sb.append("   - Trạng thái: ").append(e.statusText).append("\n");
            }
        }
        return sb.toString();
    }

    // =========================================================================
    // 2. SUPREME SPAWNING APIS
    // =========================================================================

    /**
     * Spawn Đệ Tử Hoang linh hoạt:
     * - targetMapId: ID Map cụ thể (null hoặc <= 0 => Tự động Random)
     * - targetZoneId: ID Khu cụ thể (null hoặc < 0 => Tự động Random)
     * - clazz: Hệ phái 1-5 (null hoặc <= 0 => Random)
     * - count: Số lượng cần spawn
     * - noDuplicateMap: Đảm bảo mỗi con ở 1 Map khác nhau
     * - noDuplicateZone: Đảm bảo các con cùng Map ở các Khu khác nhau
     * - notifyKtg: Bật thông báo KTG toàn server
     */
    public synchronized List<DeTu> spawnWildDeTu(Integer targetMapId, Integer targetZoneId, Integer clazz,
                                                 int count, boolean noDuplicateMap, boolean noDuplicateZone,
                                                 boolean notifyKtg) {
        boolean allZones = (targetZoneId != null && targetZoneId == -1);
        return spawnWildDeTu(targetMapId, targetZoneId, clazz, count, allZones, noDuplicateMap, noDuplicateZone, notifyKtg);
    }

    public synchronized List<DeTu> spawnWildDeTu(Integer targetMapId, Integer targetZoneId, Integer clazz,
                                                 int count, boolean allZones, boolean noDuplicateMap, boolean noDuplicateZone,
                                                 boolean notifyKtg) {
        List<DeTu> spawned = new ArrayList<>();
        if (count <= 0) count = 1;

        // =========================================================================
        // TRƯỜNG HỢP 1: CHỈ ĐỊNH MAP CỤ THỂ
        // =========================================================================
        if (targetMapId != null && targetMapId > 0) {
            Zone[] zones = Zone.getMapByID(targetMapId);
            if (zones == null || zones.length == 0) return spawned;

            String mapName = getMapNameById(targetMapId);

            // 1.1: SPAWN TẤT CẢ CÁC KHU CỦA MAP (Mỗi khu count đệ tử, mặc định count = 1 => mỗi khu 1 đệ)
            if (allZones || (targetZoneId != null && targetZoneId == -1)) {
                for (Zone z : zones) {
                    if (z == null || z.template == null) continue;
                    for (int i = 0; i < count; i++) {
                        short spawnX = (short) ZUtil.random(150, Math.max(200, z.template.maxW - 150));
                        short spawnY = 260;
                        if (z.template.vgos != null && !z.template.vgos.isEmpty()) {
                            spawnY = z.template.vgos.get(0).ynew;
                        }
                        int selectedClazz = (clazz != null && clazz >= 1 && clazz <= 5) ? clazz : ZUtil.random(1, 5);
                        try {
                            DeTu wild = DeTu.spawnWildDeTuAt(z, spawnX, spawnY, selectedClazz);
                            if (wild != null) {
                                spawned.add(wild);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (notifyKtg && !spawned.isEmpty()) {
                    String desc = (clazz != null && clazz >= 1 && clazz <= model.Clazz.NAME.length)
                            ? ("Đệ Tử Hoang (" + model.Clazz.NAME[clazz - 1] + ")")
                            : "Đệ Tử Hoang";
                    Manager.gI().chatKTG(0, desc + " đã xuất hiện đồng loạt tại TẤT CẢ các khu của "
                            + mapName + " (Tổng " + spawned.size() + " đệ tử)! Hãy mau đến thu phục!", 5);
                }
                return spawned;
            }

            // 1.2: SPAWN TẠI 1 KHU CỤ THỂ (Có thể spawn nhiều đệ tử trong cùng 1 khu này)
            if (targetZoneId != null && targetZoneId >= 0) {
                int zIdx = Math.min(targetZoneId, zones.length - 1);
                Zone z = zones[zIdx];
                if (z != null && z.template != null) {
                    for (int i = 0; i < count; i++) {
                        short spawnX = (short) ZUtil.random(150, Math.max(200, z.template.maxW - 150));
                        short spawnY = 260;
                        if (z.template.vgos != null && !z.template.vgos.isEmpty()) {
                            spawnY = z.template.vgos.get(0).ynew;
                        }
                        int selectedClazz = (clazz != null && clazz >= 1 && clazz <= 5) ? clazz : ZUtil.random(1, 5);
                        try {
                            DeTu wild = DeTu.spawnWildDeTuAt(z, spawnX, spawnY, selectedClazz);
                            if (wild != null) {
                                spawned.add(wild);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (notifyKtg && !spawned.isEmpty()) {
                        String desc = (clazz != null && clazz >= 1 && clazz <= model.Clazz.NAME.length)
                                ? ("Đệ Tử Hoang (" + model.Clazz.NAME[clazz - 1] + ")")
                                : "Đệ Tử Hoang";
                        if (spawned.size() == 1) {
                            Manager.gI().chatKTG(0, desc + " vừa xuất hiện tại "
                                    + mapName + " khu " + (z.zone_id + 1) + "! Hãy mau đến thu phục!", 5);
                        } else {
                            Manager.gI().chatKTG(0, "Đã xuất hiện " + spawned.size() + " " + desc + " tại "
                                    + mapName + " khu " + (z.zone_id + 1) + "! Hãy mau đến thu phục!", 5);
                        }
                    }
                }
                return spawned;
            }

            // 1.3: MAP CỤ THỂ NHƯNG KHÔNG CHỌN KHU (Phân bổ số lượng đệ tử vào các khu)
            // Nếu count >= số khu, mỗi khu sẽ nhận ít nhất 1 đệ tử
            // Nếu count < số khu, chọn các khu khác nhau (nếu noDuplicateZone = true)
            List<Zone> validZones = new ArrayList<>();
            for (Zone z : zones) {
                if (z != null && z.template != null) validZones.add(z);
            }
            if (validZones.isEmpty()) return spawned;

            Collections.shuffle(validZones);
            for (int i = 0; i < count; i++) {
                Zone z = validZones.get(i % validZones.size());
                short spawnX = (short) ZUtil.random(150, Math.max(200, z.template.maxW - 150));
                short spawnY = 260;
                if (z.template.vgos != null && !z.template.vgos.isEmpty()) {
                    spawnY = z.template.vgos.get(0).ynew;
                }
                int selectedClazz = (clazz != null && clazz >= 1 && clazz <= 5) ? clazz : ZUtil.random(1, 5);
                try {
                    DeTu wild = DeTu.spawnWildDeTuAt(z, spawnX, spawnY, selectedClazz);
                    if (wild != null) {
                        spawned.add(wild);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (notifyKtg && !spawned.isEmpty()) {
                String desc = (clazz != null && clazz >= 1 && clazz <= model.Clazz.NAME.length)
                        ? ("Đệ Tử Hoang (" + model.Clazz.NAME[clazz - 1] + ")")
                        : "Đệ Tử Hoang";
                if (spawned.size() == 1) {
                    Zone z = spawned.get(0).map;
                    int zId = z != null ? (z.zone_id + 1) : 1;
                    Manager.gI().chatKTG(0, desc + " vừa xuất hiện tại "
                            + mapName + " khu " + zId + "! Hãy mau đến thu phục!", 5);
                } else {
                    Manager.gI().chatKTG(0, "Đã xuất hiện " + spawned.size() + " " + desc + " tại "
                            + mapName + "! Hãy mau đến thu phục!", 5);
                }
            }
            return spawned;
        }

        // =========================================================================
        // TRƯỜNG HỢP 2: RANDOM MAP TOÀN SERVER (targetMapId == null hoặc <= 0)
        // =========================================================================
        List<Integer> candidateMaps = getCandidateMapsForWild(null, count);
        if (candidateMaps.isEmpty()) return spawned;

        Set<Integer> usedMapIds = new HashSet<>();
        Set<String> usedMapZones = new HashSet<>();

        List<Integer> mapPoolToUse = new ArrayList<>();
        if (noDuplicateMap) {
            for (int mId : candidateMaps) {
                if (!usedMapIds.contains(mId)) {
                    mapPoolToUse.add(mId);
                    usedMapIds.add(mId);
                    if (mapPoolToUse.size() >= count) break;
                }
            }
        } else {
            for (int i = 0; i < count; i++) {
                mapPoolToUse.add(candidateMaps.get(i % candidateMaps.size()));
            }
        }

        for (int mapId : mapPoolToUse) {
            if (spawned.size() >= count) break;

            Zone[] zones = Zone.getMapByID(mapId);
            if (zones == null || zones.length == 0) continue;

            Zone targetZone = selectAppropriateZone(zones, targetZoneId, noDuplicateZone, usedMapZones, mapId);
            if (targetZone == null || targetZone.template == null) continue;

            usedMapZones.add(mapId + "_" + targetZone.zone_id);

            short spawnX = (short) ZUtil.random(150, Math.max(200, targetZone.template.maxW - 150));
            short spawnY = 260;
            if (targetZone.template.vgos != null && !targetZone.template.vgos.isEmpty()) {
                spawnY = targetZone.template.vgos.get(0).ynew;
            }

            int selectedClazz = (clazz != null && clazz >= 1 && clazz <= 5) ? clazz : ZUtil.random(1, 5);

            try {
                DeTu wild = DeTu.spawnWildDeTuAt(targetZone, spawnX, spawnY, selectedClazz);
                if (wild != null) {
                    spawned.add(wild);
                    if (notifyKtg) {
                        String className = (selectedClazz >= 1 && selectedClazz <= model.Clazz.NAME.length)
                                ? model.Clazz.NAME[selectedClazz - 1] : "Hải Tặc";
                        Manager.gI().chatKTG(0, "Đệ Tử Hoang (" + className + ") vừa xuất hiện tại "
                                + targetZone.template.name + " khu " + (targetZone.zone_id + 1) + "! Hãy mau đến thu phục!", 5);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return spawned;
    }

    /**
     * Spawn Siêu Trùm linh hoạt:
     * - mobId: ID mob của Siêu Trùm (null hoặc <= 0 => Random trong danh sách Siêu Trùm)
     * - targetMapId: ID Map cụ thể (null hoặc <= 0 => Dùng map mặc định của Boss hoặc Random)
     * - targetZoneId: ID Khu cụ thể (null hoặc < 0 => Tự động Random)
     * - count: Số lượng cần spawn
     * - noDuplicateMap: Đảm bảo mỗi con ở 1 Map khác nhau
     * - noDuplicateZone: Đảm bảo các con cùng Map ở các Khu khác nhau
     * - notifyKtg: Bật thông báo KTG toàn server
     */
    public synchronized List<AbsBoss> spawnSuperBoss(Integer mobId, Integer targetMapId, Integer targetZoneId,
                                                     int count, boolean noDuplicateMap, boolean noDuplicateZone,
                                                     boolean notifyKtg) {
        List<AbsBoss> spawned = new ArrayList<>();
        SuperBossManager.init();
        if (count <= 0) count = 1;

        List<Short> bossPool = new ArrayList<>();
        if (mobId != null && mobId > 0) {
            bossPool.add(mobId.shortValue());
        } else {
            // Lấy ngẫu nhiên từ ID_BOSS
            List<Short> available = new ArrayList<>();
            for (short id : SuperBossManager.ID_BOSS) {
                available.add(id);
            }
            Collections.shuffle(available);
            bossPool.addAll(available);
        }

        List<Integer> candidateMaps = getCandidateMapsForBoss(targetMapId, count);
        if (candidateMaps.isEmpty()) return spawned;

        Set<Integer> usedMapIds = new HashSet<>();
        Set<String> usedMapZones = new HashSet<>();

        int bossIdx = 0;
        for (int selectedMapId : candidateMaps) {
            if (spawned.size() >= count) break;
            if (usedMapIds.contains(selectedMapId)) continue;
            // Mỗi map id chỉ được 1 siêu trùm ở khu bất kì. Nếu 1 trong all khu có siêu trùm thì không spawn nữa!
            if (Zone.hasActiveSuperBossInMap(selectedMapId)) continue;

            Zone[] zones = Zone.getMapByID(selectedMapId);
            if (zones == null || zones.length == 0) continue;

            Zone targetZone = selectAppropriateZone(zones, targetZoneId, noDuplicateZone, usedMapZones, selectedMapId);
            if (targetZone == null || targetZone.template == null) continue;

            short bMobId = bossPool.get(bossIdx % bossPool.size());
            bossIdx++;

            usedMapIds.add(selectedMapId);
            usedMapZones.add(selectedMapId + "_" + targetZone.zone_id);

            // Tìm AbsBoss trong SuperBossManager.ENTRYS hoặc khởi tạo mới
            AbsBoss boss = null;
            for (AbsBoss b : SuperBossManager.ENTRYS) {
                if (b != null && b.id == bMobId) {
                    boss = b;
                    break;
                }
            }

            if (boss == null) {
                // Tạo mới hoặc kích hoạt AbsBoss tại targetZone
                boss = BossManager.gI().createBoss(bMobId);
                if (boss == null) continue;

                int bossIndex = Manager.gI().index_mob.getAndAdd(10);
                boss.index = bossIndex;
                boss.index_mob_save = bossIndex;
                boss.boss_inf = boss;
                boss.isSieuTrum = true;
                boss.skill = boss.getSkills();
                boss.time_atk = new long[boss.skill.length];
                boss.TopDame = new ArrayList<>();
                boss.buff = new ArrayList<>();
                SuperBossManager.ENTRYS.add(boss);
            } else if (boss.map != null && !boss.map.equals(targetZone)) {
                // Chuyển sang targetZone mới
                try {
                    boss.map.remove_obj(boss.index_mob_save, 1);
                    boss.map.mobs.remove(boss.index);
                    for (int j = 0; j < 10; j++) {
                        boss.map.mobs.remove(boss.index + j);
                    }
                } catch (Exception ignored) {}
            }

            boss.map = targetZone;
            short[] coords = boss.calculateCoordinatesInZone(targetZone, (short) -1, (short) -1);
            boss.x = coords[0];
            boss.y = coords[1];
            boss.hp_max = boss.getHpMax();
            boss.hp = boss.hp_max;
            boss.level = boss.getLevel();
            boss.isdie = false;
            boss.id_target = -1;
            boss.levelBoss = 1;
            boss.index = boss.index_mob_save;

            Mob.ENTRYS.put(boss.index, boss);
            targetZone.mobs.put(boss.index, boss);
            for (int j = 0; j < 10; j++) {
                Mob.ENTRYS.put((boss.index + j), boss);
                targetZone.mobs.put((boss.index + j), boss);
            }

            // Đánh dấu mảng trạng thái SuperBoss
            for (int idx = 0; idx < SuperBossManager.ID_BOSS.length; idx++) {
                if (SuperBossManager.ID_BOSS[idx] == bMobId) {
                    SuperBossManager.BOSS_LIVE[idx] = 1;
                    SuperBossManager.BOSS_AREA[idx] = (byte) targetZone.zone_id;
                    break;
                }
            }

            targetZone.can_PK = false;
            targetZone.list_mob_custom_add(boss);
            boss.sendMove();
            SuperBossManager.kickInvalidPlayersInZone(targetZone, boss);
            spawned.add(boss);

            if (notifyKtg) {
                String bName = (boss.mtemplate != null && boss.mtemplate.name != null)
                        ? boss.mtemplate.name : ("Siêu Trùm " + bMobId);
                Manager.gI().chatKTG(0, "Siêu trùm " + bName + " đã xuất hiện tại "
                        + targetZone.template.name + " khu " + (targetZone.zone_id + 1) + "! Các hải tặc hãy mau đến săn lùng!", 5);
            }
        }

        return spawned;
    }

    /**
     * Spawn Boss Thường / Boss Thế Giới / Boss Pica linh hoạt
     */
    public synchronized List<AbsBoss> spawnNormalBoss(Integer mobId, Integer targetMapId, Integer targetZoneId,
                                                      int count, boolean noDuplicateMap, boolean noDuplicateZone,
                                                      boolean notifyKtg) {
        List<AbsBoss> spawned = new ArrayList<>();
        if (count <= 0) count = 1;

        if (mobId != null && mobId == 172) {
            // Boss Thế Giới
            BossTheGioi.spawn_boss();
            if (BossTheGioi.gI().map != null) {
                spawned.add(BossTheGioi.gI());
            }
            return spawned;
        }

        if (mobId != null && mobId == 173) {
            // Boss Pica
            try {
                BossPica.gI().spawn();
                if (BossPica.gI().map != null) {
                    spawned.add(BossPica.gI());
                }
            } catch (Exception ignored) {}
            return spawned;
        }

        int resolvedMobId = (mobId != null && mobId > 0) ? mobId : 16;
        return spawnSuperBoss(resolvedMobId, targetMapId, targetZoneId, count, noDuplicateMap, noDuplicateZone, notifyKtg);
    }

    public synchronized List<DeTu> spawnWildDeTuAtCoords(Zone targetZone, short spawnX, short spawnY, Integer clazz, int count, boolean notifyKtg) {
        List<DeTu> result = new ArrayList<>();
        if (targetZone == null || targetZone.template == null) return result;
        if (count <= 0) count = 1;
        for (int i = 0; i < count; i++) {
            int selectedClazz = (clazz != null && clazz >= 1 && clazz <= 5) ? clazz : (ZUtil.random(1, 6));
            DeTu wild = DeTu.spawnWildDeTuAt(targetZone, spawnX, spawnY, selectedClazz);
            if (wild != null) {
                result.add(wild);
            }
        }
        if (notifyKtg && !result.isEmpty()) {
            Manager.gI().chatKTG(0, "Admin đã triệu hồi " + result.size() + " Đệ Tử Hoang tại " + targetZone.template.name + " Khu " + (targetZone.zone_id + 1), 5);
        }
        return result;
    }

    public synchronized AbsBoss spawnSuperBossAtCoords(short bMobId, Zone targetZone, short spawnX, short spawnY, boolean notifyKtg) {
        if (targetZone == null || targetZone.template == null) return null;
        SuperBossManager.init();

        AbsBoss boss = null;
        for (AbsBoss b : SuperBossManager.ENTRYS) {
            if (b != null && b.id == bMobId) {
                boss = b;
                break;
            }
        }

        if (boss == null) {
            boss = BossManager.gI().createBoss(bMobId);
            if (boss == null) return null;

            int bossIndex = Manager.gI().index_mob.getAndAdd(10);
            boss.index = bossIndex;
            boss.index_mob_save = bossIndex;
            boss.boss_inf = boss;
            boss.isSieuTrum = true;
            boss.skill = boss.getSkills();
            boss.time_atk = new long[boss.skill.length];
            boss.TopDame = new ArrayList<>();
            boss.buff = new ArrayList<>();
            SuperBossManager.ENTRYS.add(boss);
        } else if (boss.map != null && !boss.map.equals(targetZone)) {
            try {
                boss.map.remove_obj(boss.index_mob_save, 1);
                boss.map.mobs.remove(boss.index);
                for (int j = 0; j < 10; j++) {
                    boss.map.mobs.remove(boss.index + j);
                }
            } catch (Exception ignored) {}
        }

        boss.map = targetZone;
        boss.x = spawnX > 0 ? spawnX : (short) 500;
        boss.y = spawnY > 0 ? spawnY : (short) 260;
        boss.hp_max = boss.getHpMax();
        boss.hp = boss.hp_max;
        boss.level = boss.getLevel();
        boss.isdie = false;
        boss.id_target = -1;
        boss.levelBoss = 1;
        boss.index = boss.index_mob_save;

        Mob.ENTRYS.put(boss.index, boss);
        targetZone.mobs.put(boss.index, boss);
        for (int j = 0; j < 10; j++) {
            Mob.ENTRYS.put((boss.index + j), boss);
            targetZone.mobs.put((boss.index + j), boss);
        }

        for (int idx = 0; idx < SuperBossManager.ID_BOSS.length; idx++) {
            if (SuperBossManager.ID_BOSS[idx] == bMobId) {
                SuperBossManager.BOSS_LIVE[idx] = 1;
                SuperBossManager.BOSS_AREA[idx] = (byte) targetZone.zone_id;
                break;
            }
        }

        targetZone.can_PK = false;
        targetZone.list_mob_custom_add(boss);
        boss.sendMove();

        SuperBossManager.kickInvalidPlayersInZone(targetZone, boss);

        if (notifyKtg) {
            String bName = (boss.mtemplate != null && boss.mtemplate.name != null) ? boss.mtemplate.name : ("Siêu Trùm Mob " + bMobId);
            Manager.gI().chatKTG(0, "Admin đã triệu hồi Siêu Trùm " + bName + " tại " + targetZone.template.name + " Khu " + (targetZone.zone_id + 1), 5);
        }

        return boss;
    }

    public synchronized List<AbsBoss> spawnAll10SuperBossesAtZone(Zone targetZone, short centerX, short centerY, boolean notifyKtg) {
        List<AbsBoss> list = new ArrayList<>();
        if (targetZone == null || targetZone.template == null) return list;
        SuperBossManager.init();

        int mapW = targetZone.template.maxW > 0 ? targetZone.template.maxW : 1200;
        int total = SuperBossManager.ID_BOSS.length;
        int startX = Math.max(150, centerX - 300);
        int endX = Math.min(mapW - 150, centerX + 300);
        int spacing = Math.max(50, (endX - startX) / Math.max(1, total - 1));

        for (int i = 0; i < total; i++) {
            short mobId = SuperBossManager.ID_BOSS[i];
            short posX = (short) (startX + i * spacing);
            short posY = centerY;
            AbsBoss b = spawnSuperBossAtCoords(mobId, targetZone, posX, posY, false);
            if (b != null) {
                list.add(b);
            }
        }

        if (notifyKtg && !list.isEmpty()) {
            Manager.gI().chatKTG(0, "Admin đã triệu hồi TOÀN BỘ 10 SIÊU TRÙM tại " + targetZone.template.name + " Khu " + (targetZone.zone_id + 1), 5);
        }
        return list;
    }

    public synchronized AbsBoss spawnNormalBossAtCoords(int mobId, Zone targetZone, short spawnX, short spawnY, boolean notifyKtg) {
        if (targetZone == null || targetZone.template == null) return null;
        AbsBoss boss = BossManager.gI().createBoss(mobId);
        if (boss == null) return null;

        int bossIndex = Manager.gI().index_mob.getAndAdd(10);
        boss.index = bossIndex;
        boss.index_mob_save = bossIndex;
        boss.boss_inf = boss;
        boss.skill = boss.getSkills();
        boss.time_atk = new long[boss.skill.length];
        boss.TopDame = new ArrayList<>();
        boss.buff = new ArrayList<>();

        boss.map = targetZone;
        boss.x = spawnX > 0 ? spawnX : (short) 500;
        boss.y = spawnY > 0 ? spawnY : (short) 260;
        boss.hp_max = boss.getHpMax();
        boss.hp = boss.hp_max;
        boss.level = boss.getLevel();
        boss.isdie = false;
        boss.id_target = -1;
        boss.levelBoss = 1;

        Mob.ENTRYS.put(boss.index, boss);
        targetZone.mobs.put(boss.index, boss);
        for (int j = 0; j < 10; j++) {
            Mob.ENTRYS.put((boss.index + j), boss);
            targetZone.mobs.put((boss.index + j), boss);
        }

        targetZone.list_mob_custom_add(boss);
        boss.sendMove();

        if (notifyKtg) {
            String bName = (boss.mtemplate != null && boss.mtemplate.name != null) ? boss.mtemplate.name : ("Boss Mob " + mobId);
            Manager.gI().chatKTG(0, "Admin đã triệu hồi " + bName + " tại " + targetZone.template.name + " Khu " + (targetZone.zone_id + 1), 5);
        }

        return boss;
    }

    // =========================================================================
    // 3. ADMIN ACTIONS (TELEPORT, CLEAR, KILL)
    // =========================================================================

    /**
     * Dịch chuyển tức thời Admin đến vị trí thực thể
     */
    public boolean teleportAdminToEntity(Player admin, Object rawEntity) {
        if (admin == null || rawEntity == null) return false;
        try {
            Zone targetZone = null;
            short targetX = 200;
            short targetY = 200;
            String entityName = "Thực thể";

            if (rawEntity instanceof AbsBoss) {
                AbsBoss b = (AbsBoss) rawEntity;
                targetZone = b.map;
                targetX = (short) b.x;
                targetY = (short) b.y;
                entityName = (b.mtemplate != null) ? b.mtemplate.name : "Boss";
            } else if (rawEntity instanceof DeTu) {
                DeTu dt = (DeTu) rawEntity;
                targetZone = dt.map;
                targetX = (short) dt.x;
                targetY = (short) dt.y;
                entityName = dt.name;
            } else if (rawEntity instanceof Mob) {
                Mob m = (Mob) rawEntity;
                targetZone = m.map;
                targetX = (short) m.x;
                targetY = (short) m.y;
                entityName = (m.mtemplate != null) ? m.mtemplate.name : "Quái vật";
            }

            if (targetZone == null) {
                admin.getService().send_box_ThongBao_OK("Thực thể hiện chưa xuất hiện trên bản đồ!");
                return false;
            }

            if (admin.map != null) {
                admin.map.leave_map(admin, 2);
            }
            admin.map = targetZone;
            admin.x = targetX;
            admin.y = targetY;
            admin.xold = targetX;
            admin.yold = targetY;
            admin.lastValidX = targetX;
            admin.lastValidY = targetY;
            admin.map.goto_map(admin);
            admin.getService().update_PK(admin, true);
            admin.getService().pet(admin, true);
            admin.getService().send_box_ThongBao_OK("Đã dịch chuyển đến cạnh " + entityName + " tại "
                    + targetZone.template.name + " (Khu " + (targetZone.zone_id + 1) + ")!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Thu hồi / dọn dẹp toàn bộ đệ tử hoang trên server
     */
    public int clearAllWildDeTu() {
        int count = 0;
        if (DeTu.WILD_DETU_MAP != null) {
            for (DeTu dt : DeTu.WILD_DETU_MAP.values()) {
                if (dt != null) {
                    try {
                        if (dt.map != null) {
                            dt.map.leave_map(dt, 2);
                        }
                        if (dt.index_map < 0) {
                            database.IDManager.putID(dt.index_map, database.IDManager.FAKE_BOT);
                        }
                        count++;
                    } catch (Exception ignored) {}
                }
            }
            DeTu.WILD_DETU_MAP.clear();
        }
        return count;
    }

    /**
     * Tiêu diệt / Despawn toàn bộ Boss đang sống
     */
    public int killAllBosses() {
        int count = 0;
        if (SuperBossManager.ENTRYS != null) {
            for (AbsBoss b : SuperBossManager.ENTRYS) {
                if (b != null && !b.isdie) {
                    b.isdie = true;
                    b.hp = 0;
                    if (b.map != null) {
                        try {
                            b.map.remove_obj(b.index_mob_save, 1);
                        } catch (Exception ignored) {}
                    }
                    count++;
                }
            }
            Arrays.fill(SuperBossManager.BOSS_LIVE, (byte) 0);
            Arrays.fill(SuperBossManager.BOSS_AREA, (byte) -1);
        }

        try {
            if (BossTheGioi.gI() != null && !BossTheGioi.gI().isdie) {
                BossTheGioi.gI().isdie = true;
                count++;
            }
            if (BossPica.gI() != null && !BossPica.gI().isdie) {
                BossPica.gI().isdie = true;
                count++;
            }
        } catch (Exception ignored) {}

        return count;
    }

    /**
     * Tiêu diệt hoặc xóa 1 thực thể cụ thể
     */
    public boolean killEntity(Object rawEntity) {
        if (rawEntity == null) return false;
        try {
            if (rawEntity instanceof AbsBoss) {
                AbsBoss b = (AbsBoss) rawEntity;
                b.isdie = true;
                b.hp = 0;
                if (b.map != null) {
                    b.map.remove_obj(b.index_mob_save, 1);
                }
                return true;
            } else if (rawEntity instanceof DeTu) {
                DeTu dt = (DeTu) rawEntity;
                DeTu.WILD_DETU_MAP.remove((int) dt.index_map);
                if (dt.map != null) {
                    dt.map.leave_map(dt, 2);
                }
                if (dt.index_map < 0) {
                    database.IDManager.putID(dt.index_map, database.IDManager.FAKE_BOT);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    public List<Integer> getCandidateMapsForBoss(Integer targetMapId, int count) {
        List<Integer> result = new ArrayList<>();
        if (targetMapId != null && targetMapId > 0) {
            if (!Zone.hasActiveSuperBossInMap(targetMapId)) {
                result.add(targetMapId);
            }
            return result;
        }

        List<Integer> pool = new ArrayList<>();
        for (int mId : DEFAULT_ADVENTURE_MAPS) {
            if (Zone.getMapByID(mId) != null && !Zone.hasActiveSuperBossInMap(mId)) {
                pool.add(mId);
            }
        }
        Collections.shuffle(pool);
        for (int i = 0; i < Math.min(count, pool.size()); i++) {
            result.add(pool.get(i));
        }
        return result;
    }

    public List<Integer> getCandidateMapsForWild(Integer targetMapId, int count) {
        List<Integer> result = new ArrayList<>();
        if (targetMapId != null && targetMapId > 0) {
            result.add(targetMapId);
            return result;
        }

        List<Integer> poolWithoutWild = new ArrayList<>();
        List<Integer> allValidPool = new ArrayList<>();

        for (int mId : model.DeTu.WILD_DETU_MAP_IDS) {
            if (Zone.getMapByID(mId) != null) {
                allValidPool.add(mId);
                if (!Zone.hasActiveWildDeTuInMap(mId)) {
                    poolWithoutWild.add(mId);
                }
            }
        }
        if (allValidPool.isEmpty()) {
            for (int mId : DEFAULT_ADVENTURE_MAPS) {
                if (Zone.getMapByID(mId) != null) {
                    allValidPool.add(mId);
                    if (!Zone.hasActiveWildDeTuInMap(mId)) {
                        poolWithoutWild.add(mId);
                    }
                }
            }
        }

        List<Integer> pool = !poolWithoutWild.isEmpty() ? poolWithoutWild : allValidPool;
        if (pool.isEmpty()) return result;

        Collections.shuffle(pool);
        for (int i = 0; i < Math.min(count, pool.size()); i++) {
            result.add(pool.get(i));
        }
        while (result.size() < count && !allValidPool.isEmpty()) {
            result.add(allValidPool.get(result.size() % allValidPool.size()));
        }
        return result;
    }

    private List<Integer> getCandidateMaps(Integer targetMapId, int count, boolean noDuplicateMap) {
        List<Integer> result = new ArrayList<>();
        if (targetMapId != null && targetMapId > 0) {
            result.add(targetMapId);
            return result;
        }

        List<Integer> pool = new ArrayList<>();
        for (int mId : DEFAULT_ADVENTURE_MAPS) {
            if (Zone.getMapByID(mId) != null) {
                pool.add(mId);
            }
        }
        if (pool.isEmpty()) {
            pool.add(1); // Fallback làng Foosha
        }

        if (noDuplicateMap) {
            Collections.shuffle(pool);
            for (int i = 0; i < Math.min(count, pool.size()); i++) {
                result.add(pool.get(i));
            }
            // Nếu số lượng yêu cầu lớn hơn số map trong pool, lặp lại
            while (result.size() < count) {
                result.add(pool.get(result.size() % pool.size()));
            }
        } else {
            result.addAll(pool);
        }
        return result;
    }

    private Zone selectAppropriateZone(Zone[] zones, Integer targetZoneId, boolean noDuplicateZone,
                                       Set<String> usedMapZones, int mapId) {
        if (zones == null || zones.length == 0) return null;

        if (targetZoneId != null && targetZoneId >= 0 && targetZoneId < zones.length) {
            return zones[targetZoneId];
        }

        if (noDuplicateZone) {
            List<Zone> available = new ArrayList<>();
            for (Zone z : zones) {
                if (z != null && !usedMapZones.contains(mapId + "_" + z.zone_id)) {
                    available.add(z);
                }
            }
            if (!available.isEmpty()) {
                return available.get(ZUtil.random(available.size()));
            }
        }
        return zones[ZUtil.random(zones.length)];
    }

    public static String getMapNameById(int mapId) {
        try {
            MapTemplate mt = MapTemplate.ENTRYS.stream().filter(m -> m.id == mapId).findFirst().orElse(null);
            if (mt != null && mt.name != null && !mt.name.isBlank()) {
                return mt.name;
            }
            Zone[] z = Zone.getMapByID(mapId);
            if (z != null && z.length > 0 && z[0] != null && z[0].template != null) {
                return z[0].template.name;
            }
        } catch (Exception ignored) {}
        return "Bản đồ " + mapId;
    }
}
