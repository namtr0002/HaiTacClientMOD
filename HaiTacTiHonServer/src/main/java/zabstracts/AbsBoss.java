package zabstracts;

import zinterfaces.iBoss;
import model.Player;
import network.Service;
import network.Message;
import core.Manager;
import core.ZUtil;
import mob.Mob;
import map.Zone;
import template.MobTemplate;
import template.Option;
import template.GiftBox;
import template.Top_Dame;
import template.Item_wear;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbsBoss extends Mob implements iBoss {
    public static final int LOC_MAP_ID = 0;
    public static final int LOC_X = 1;
    public static final int LOC_Y = 2;

    public int id; // mob_id
    public byte levelBoss;
    public short[] skill;
    public List<Option> buff;
    public long[] time_atk;
    public List<Top_Dame> TopDame;
    public int index_mob_save;
    public HashMap<String, Long> time_def_special = new HashMap<>();
    public long timeDeath;

    // --- Configurable Boss Attributes ---
    public boolean isSieuTrum() {
        return true;
    }

    public boolean isLevelUpOnDeath() {
        return isSieuTrum();
    }

    public byte[] getSpawnHours() {
        return null; // null = mở full 24/24
    }

    public int getRespawnTimeSeconds() {
        return isSieuTrum() ? -1 : 1800; // -1: daily spawn, >0: respawn interval in seconds
    }

    public AbsBoss(int mob_id) {
        super();
        this.id = mob_id;
        this.levelBoss = 1;
        this.TopDame = new ArrayList<>();
        this.buff = new ArrayList<>();
        this.mtemplate = MobTemplate.ENTRYS.get(mob_id);
        this.isSieuTrum = isSieuTrum();
        this.isdie = true;
        this.hp = 0;
        this.id_target = -1;
        init();
    }

    public abstract void init();

    // -------------------------------------------------------------
    // SPAWN LOCATION & ZONE CONFIGURATION
    // -------------------------------------------------------------

    /**
     * Cấu hình toạ độ và map xuất hiện của Boss dưới dạng mảng 1 chiều short[].
     * Format chuẩn: short[] { mapId, x, y }
     */
    public abstract short[] getSpawnLocation();

    /**
     * Pool danh sách nhiều toạ độ/map xuất hiện khả thi (short[][]).
     */
    public short[][] getSpawnLocationPool() {
        return null;
    }

    /**
     * Giải quyết toạ độ spawn thực tế:
     * Nếu có Pool thì random 1 điểm trong Pool, ngược lại lấy getSpawnLocation().
     */
    public short[] resolveSpawnLocation() {
        short[][] pool = getSpawnLocationPool();
        if (pool != null && pool.length > 0) {
            return pool[ZUtil.random(pool.length)];
        }
        return getSpawnLocation();
    }

    public short getMapId() {
        short[] loc = resolveSpawnLocation();
        return (loc != null && loc.length > LOC_MAP_ID) ? loc[LOC_MAP_ID] : -1;
    }

    public short getSpawnX() {
        short[] loc = resolveSpawnLocation();
        return (loc != null && loc.length > LOC_X) ? loc[LOC_X] : -1;
    }

    public short getSpawnY() {
        short[] loc = resolveSpawnLocation();
        return (loc != null && loc.length > LOC_Y) ? loc[LOC_Y] : -1;
    }

    /**
     * Cấu hình cho phép có nhiều hơn 1 Boss cùng sống trong 1 Khu (Zone) hay không.
     */
    public boolean isAllowMultipleInSameZone() {
        return false;
    }

    /**
     * Kiểm tra xem khu vực (Zone) này đã có Boss đang sống hay chưa.
     */
    public boolean isZoneOccupied(Zone targetZone) {
        if (targetZone == null) return true;
        if (isAllowMultipleInSameZone()) return false;
        return targetZone.hasActiveBoss();
    }

    /**
     * Tìm 1 Zone còn trống (chưa có Boss nào đang sống) trong map.
     */
    public Zone findAvailableZone(int mapId, Zone excludeZone) {
        Zone[] zones = Zone.getMapByID(mapId);
        if (zones == null || zones.length == 0) return null;
        
        List<Zone> available = new ArrayList<>();
        for (int i = 0; i < zones.length; i++) {
            Zone z = zones[i];
            if (z == null) continue;
            if (excludeZone != null && z.equals(excludeZone) && zones.length > 1) continue;
            if (!isZoneOccupied(z)) {
                available.add(z);
            }
        }
        
        if (!available.isEmpty()) {
            return available.get(ZUtil.random(available.size()));
        }
        return isAllowMultipleInSameZone() ? zones[ZUtil.random(zones.length)] : null;
    }

    /**
     * Tính toán toạ độ x, y thực tế cho Boss khi vào Zone.
     */
    public short[] calculateCoordinatesInZone(Zone targetZone, short specifiedX, short specifiedY) {
        short finalX = specifiedX;
        short finalY = specifiedY;
        
        if (finalX <= 0 || finalY <= 0) {
            if (targetZone != null) {
                if (targetZone.list_mob != null && targetZone.list_mob.length > 0) {
                    int randomMobIdx = targetZone.list_mob[ZUtil.random(targetZone.list_mob.length)];
                    Mob mobInMap = targetZone.getMob(randomMobIdx);
                    if (mobInMap != null && mobInMap.x > 0 && mobInMap.y > 0) {
                        return new short[]{(short) mobInMap.x, (short) mobInMap.y};
                    }
                }
                int mapW = (targetZone.template != null && targetZone.template.maxW > 0) ? targetZone.template.maxW : 1000;
                int mapH = (targetZone.template != null && targetZone.template.maxH > 0) ? targetZone.template.maxH : 600;
                finalX = (short) ZUtil.random(100, Math.max(101, mapW - 100));
                finalY = (short) ZUtil.random(80, Math.max(81, mapH - 80));
            } else {
                finalX = 500;
                finalY = 260;
            }
        }
        return new short[]{finalX, finalY};
    }

    public void spawn(Zone zone, short x, short y, int index_mob) {
        if (zone == null) return;
        // Chặn trùng con thứ 2 cùng 1 khu
        if (isZoneOccupied(zone)) {
            Zone freeZone = findAvailableZone(zone.template.id, zone);
            if (freeZone == null) {
                return;
            }
            zone = freeZone;
        }

        short[] coords = calculateCoordinatesInZone(zone, x, y);
        this.x = coords[0];
        this.y = coords[1];
        this.hp_max = getHpMax();
        this.hp = this.hp_max;
        this.level = getLevel();
        this.isdie = false;
        this.id_target = -1;
        this.index = index_mob;
        this.index_mob_save = index_mob;
        this.map = zone;
        
        // Setup skills
        this.skill = getSkills();
        this.time_atk = new long[this.skill.length];
        
        // Register in Zone & global Mob list
        zone.list_mob_custom_add(this);
    }

    public void onDamage(Player p, long damage) {
        if (damage <= 0) return;
        Player pS = p.isDe ? p.map.players.stream().filter(pl -> pl.detu != null && pl.detu.equals(p)).findFirst().orElse(p) : p;
        if (pS != null) {
            Top_Dame topdame = null;
            for (int j = 0; j < this.TopDame.size(); j++) {
                if (this.TopDame.get(j).name.equals(pS.name)) {
                    topdame = this.TopDame.get(j);
                    break;
                }
            }
            if (topdame != null) {
                topdame.dame += damage;
            } else {
                topdame = new Top_Dame();
                topdame.name = pS.name;
                topdame.dame = damage;
                this.TopDame.add(topdame);
            }
        }
    }

    // -------------------------------------------------------------
    // INCOMING DAMAGE & HP MILESTONE HOOKS
    // -------------------------------------------------------------

    /**
     * Giới hạn sát thương tối đa nhận vào trên mỗi đòn đánh.
     */
    public long getMaxDamageReceived() {
        if (isSieuTrum() && this.hp_max > 0) {
            return (long) this.hp_max * 10L / 100L;
        }
        return Long.MAX_VALUE;
    }

    /**
     * Can thiệp và tính toán sát thương thực tế Boss sẽ nhận từ Player.
     */
    public long modifyIncomingDamage(Player p, long rawDamage) {
        long cap = getMaxDamageReceived();
        if (isSieuTrum() && this.hp_max > 0) {
            long maxHitCap = (long) this.hp_max * 10L / 100L;
            if (maxHitCap > 0 && maxHitCap < cap) {
                cap = maxHitCap;
            }
        }
        if (rawDamage > cap) {
            return cap;
        }
        return rawDamage;
    }

    /**
     * Hook xử lý quà khi Boss tụt qua từng mốc 10% HP.
     */
    public void onHpMilestone10Percent(Zone map, Player p, int stepIndex) {
        if (!isSieuTrum()) return;
        try {
            List<GiftBox> list_gift = new ArrayList<>();
            int beri_receiv = (1 + this.level / 10) * 1000;
            beri_receiv = (beri_receiv / 100) * (100 + this.levelBoss * 10);
            GiftBox gb_beri = new GiftBox();
            template.ItemTemplate4 it_temp4 = template.ItemTemplate4.get_it_by_id(0);
            if (it_temp4 != null) {
                gb_beri.id = it_temp4.id;
                gb_beri.type = 4;
                gb_beri.name = it_temp4.name;
                gb_beri.icon = it_temp4.icon;
                gb_beri.num = beri_receiv;
                gb_beri.color = 0;
                list_gift.add(gb_beri);
            }

            if (15 > ZUtil.random(120)) {
                GiftBox gb_rcam = new GiftBox();
                template.ItemTemplate4 it_temp4_in = template.ItemTemplate4
                        .get_it_by_id((((p.level < 11 ? 11 : p.level) / 10) + 111));
                if (it_temp4_in != null) {
                    gb_rcam.id = it_temp4_in.id;
                    gb_rcam.type = 4;
                    gb_rcam.name = it_temp4_in.name;
                    gb_rcam.icon = it_temp4_in.icon;
                    gb_rcam.num = 1;
                    gb_rcam.color = 0;
                    list_gift.add(gb_rcam);
                }
            }

            if (this.id == 137 || this.id == 138) {
                if (15 > ZUtil.random(120)) {
                    int id_add = (70 > ZUtil.random(120)) ? 310 : (70 > ZUtil.random(120)) ? 311 : 312;
                    GiftBox gb_manh = new GiftBox();
                    template.ItemTemplate4 it_temp4_in = template.ItemTemplate4.get_it_by_id(id_add);
                    if (it_temp4_in != null) {
                        gb_manh.id = it_temp4_in.id;
                        gb_manh.type = 4;
                        gb_manh.name = it_temp4_in.name;
                        gb_manh.icon = it_temp4_in.icon;
                        gb_manh.num = 1;
                        gb_manh.color = 0;
                        list_gift.add(gb_manh);
                    }
                }
            } else if (this.id == 139 || this.id == 140) {
                if (15 > ZUtil.random(120)) {
                    int id_add = (70 > ZUtil.random(120)) ? 310 : (70 > ZUtil.random(120)) ? 311 : 312;
                    GiftBox gb_manh = new GiftBox();
                    template.ItemTemplate4 it_temp4_in = template.ItemTemplate4.get_it_by_id(id_add);
                    if (it_temp4_in != null) {
                        gb_manh.id = it_temp4_in.id;
                        gb_manh.type = 4;
                        gb_manh.name = it_temp4_in.name;
                        gb_manh.icon = it_temp4_in.icon;
                        gb_manh.num = 1;
                        gb_manh.color = 0;
                        list_gift.add(gb_manh);
                    }
                }
                if (10 > ZUtil.random(120)) {
                    int id_add = (70 > ZUtil.random(120)) ? 313 : (70 > ZUtil.random(120)) ? 314 : 315;
                    GiftBox gb_manh = new GiftBox();
                    template.ItemTemplate4 it_temp4_in = template.ItemTemplate4.get_it_by_id(id_add);
                    if (it_temp4_in != null) {
                        gb_manh.id = it_temp4_in.id;
                        gb_manh.type = 4;
                        gb_manh.name = it_temp4_in.name;
                        gb_manh.icon = it_temp4_in.icon;
                        gb_manh.num = 1;
                        gb_manh.color = 0;
                        list_gift.add(gb_manh);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hook xử lý quà khi Boss tụt xuống dưới 50% HP.
     * Mặc định xử lý quà 50% cho Siêu Trùm. Subclass có thể override để tùy biến riêng.
     */
    public void onHpMilestone50Percent(Zone map, Player p) {
        if (!isSieuTrum()) return;
        try {
            List<GiftBox> list_gift = new ArrayList<>();
            if (20 > ZUtil.random(120)) {
                GiftBox gb_rcam = new GiftBox();
                template.ItemTemplate4 it_temp4_in = template.ItemTemplate4
                        .get_it_by_id((((p.level < 11 ? 11 : p.level) / 10) + 111));
                if (it_temp4_in != null) {
                    gb_rcam.id = it_temp4_in.id;
                    gb_rcam.type = 4;
                    gb_rcam.name = it_temp4_in.name;
                    gb_rcam.icon = it_temp4_in.icon;
                    gb_rcam.num = 1;
                    gb_rcam.color = 0;
                    list_gift.add(gb_rcam);
                }
            }
            if (20 > ZUtil.random(120)) {
                GiftBox gb_rcam = new GiftBox();
                template.ItemTemplate4 it_temp4_in = template.ItemTemplate4.get_it_by_id(339);
                if (it_temp4_in != null) {
                    gb_rcam.id = it_temp4_in.id;
                    gb_rcam.type = 4;
                    gb_rcam.name = it_temp4_in.name;
                    gb_rcam.icon = it_temp4_in.icon;
                    gb_rcam.num = 1;
                    gb_rcam.color = 0;
                    list_gift.add(gb_rcam);
                }
            }
            int beri_receiv = 0;
            switch (this.id) {
                case 135:
                case 136:
                case 137:
                    beri_receiv = 100_000;
                    break;
                case 138:
                    beri_receiv = 150_000;
                    break;
                case 139:
                    beri_receiv = 200_000;
                    break;
                case 140:
                    beri_receiv = 250_000;
                    break;
            }
            if (beri_receiv > 0) {
                beri_receiv = (beri_receiv / 100) * (100 + this.levelBoss * 10);
                GiftBox gb_beri = new GiftBox();
                template.ItemTemplate4 it_temp4 = template.ItemTemplate4.get_it_by_id(0);
                if (it_temp4 != null) {
                    gb_beri.id = it_temp4.id;
                    gb_beri.type = 4;
                    gb_beri.name = it_temp4.name;
                    gb_beri.icon = it_temp4.icon;
                    gb_beri.num = beri_receiv;
                    gb_beri.color = 0;
                    list_gift.add(gb_beri);
                }
            }
            if (this.id >= 137 && this.id <= 140) {
                if (15 > ZUtil.random(120)) {
                    int id_add = (70 > ZUtil.random(120)) ? 310 : (70 > ZUtil.random(120)) ? 311 : 312;
                    GiftBox gb_manh = new GiftBox();
                    template.ItemTemplate4 it_temp4_in = template.ItemTemplate4.get_it_by_id(id_add);
                    if (it_temp4_in != null) {
                        gb_manh.id = it_temp4_in.id;
                        gb_manh.type = 4;
                        gb_manh.name = it_temp4_in.name;
                        gb_manh.icon = it_temp4_in.icon;
                        gb_manh.num = 1;
                        gb_manh.color = 0;
                        list_gift.add(gb_manh);
                    }
                }
                if (10 > ZUtil.random(120)) {
                    int id_add = (70 > ZUtil.random(120)) ? 313 : (70 > ZUtil.random(120)) ? 314 : 315;
                    GiftBox gb_manh = new GiftBox();
                    template.ItemTemplate4 it_temp4_in = template.ItemTemplate4.get_it_by_id(id_add);
                    if (it_temp4_in != null) {
                        gb_manh.id = it_temp4_in.id;
                        gb_manh.type = 4;
                        gb_manh.name = it_temp4_in.name;
                        gb_manh.icon = it_temp4_in.icon;
                        gb_manh.num = 1;
                        gb_manh.color = 0;
                        list_gift.add(gb_manh);
                    }
                }
            }
            if (!list_gift.isEmpty() && p != null && !p.isBot && p.conn != null) {
                core.RewardService.sendGiftOrMail(p, 1, "Săn Trùm", "Mốc 50% HP", list_gift, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tự động kiểm tra và kích hoạt các hook mốc máu (10% HP, 50% HP) khi Boss bị trừ HP.
     */
    public void checkHpMilestoneRewards(Zone map, Player p, int oldHp, int newHp, long damage) {
        int maxHp = this.hp_max;
        if (maxHp <= 0) return;

        int percent10 = maxHp / 10;
        if (percent10 > 0) {
            int oldStep = (oldHp - 1) / percent10;
            int newStep = (newHp - 1) / percent10;

            for (int step = oldStep - 1; step >= newStep; step--) {
                onHpMilestone10Percent(map, p, step);
            }
        }

        if (oldHp > maxHp / 2 && newHp <= maxHp / 2) {
            onHpMilestone50Percent(map, p);
        }
    }

    public abstract int getHpMax();
    public abstract int getLevel();
    public abstract short[] getSkills();
    public abstract void update();

    public void dropThanTrangReward(Player pKill, List<GiftBox> list_gift) {
        if (!isSieuTrum() || pKill == null || list_gift == null) return;
        // Tỉ lệ rớt Thần Trang khi hạ gục Siêu Trùm (35%)
        int dropChance = 35;
        if (ZUtil.random(100) < dropChance) {
            Item_wear ttItem = template.ThanTrangConfig.createRandomSuperBossDrop();
            if (ttItem != null && ttItem.template != null) {
                GiftBox gb = new GiftBox();
                gb.id = (short) ttItem.template.id;
                gb.type = 3;
                gb.name = ttItem.template.name;
                gb.icon = ttItem.template.icon;
                gb.num = 1;
                gb.color = ttItem.color;
                gb.options = new ArrayList<>(ttItem.option_item);
                list_gift.add(gb);

                String colorName = model.PlayerAutoSettings.getColorFullName(ttItem.color);
                Manager.gI().chatKTG(0, pKill.name + " đã tiêu diệt Siêu Trùm " + (this.mtemplate != null ? this.mtemplate.name : "Trùm") 
                    + " và đoạt được Thần Trang " + ttItem.template.name + " (" + colorName + ", Không Khóa)!", 5);
            }
        }
    }

    public void onDeath(Player pKill) {
        try {
            this.isdie = true;
            this.timeDeath = System.currentTimeMillis();
            pKill.map.remove_obj(this.index, 1);

            List<GiftBox> list_gift = new ArrayList<>();
            dropReward(pKill, list_gift);
            if (isSieuTrum()) {
                dropThanTrangReward(pKill, list_gift);
            }
            // Append quà sự kiện Boss Ngoài (nếu có sự kiện đang chạy)
            event.EventManager.dispatchBossDrop(this, pKill, list_gift);

            pKill.updateMoney();
            if (!list_gift.isEmpty() && pKill != null && !pKill.isBot && pKill.conn != null) {
                core.RewardService.sendGiftOrMail(pKill, 1, "Săn Trùm",
                        "Tiêu diệt Boss " + (this.mtemplate != null ? this.mtemplate.name : "Trùm"), list_gift, true);
            }

            // -- Siêu Trùm --
            if (isSieuTrum()) {
                final int MAX_LEVEL_BOSS = 10;
                boolean isTest = core.Manager.gI() != null && core.Manager.gI().isTestMode();
                if (isLevelUpOnDeath()) {
                    // Lên cấp sau khi chết
                    if (this.levelBoss < MAX_LEVEL_BOSS) {
                        byte nextLevel = (byte) (this.levelBoss + 1);
                        Manager.gI().chatKTG(0,
                                pKill.name + " đã hạ gục " + this.mtemplate.name
                                + " bậc " + this.levelBoss + "! Trùm lên bậc " + nextLevel + " và hồi sinh!", 5);
                        // Giữ nguyên zone cũ, random vị trí trong zone đó
                        respawnInZone(this.map, nextLevel);
                    } else {
                        // Đã max cấp (Bậc 10) -> Tổng kết quà top chu kỳ
                        boss.SuperBossManager.processBossCycleComplete(this);
                        
                        if (isTest) {
                            // Test: chết hồi sinh luôn 0s
                            Manager.gI().chatKTG(0,
                                    pKill.name + " đã hạ gục " + this.mtemplate.name
                                    + " BẬC TỐI THƯỢNG (Bậc " + this.levelBoss + ")! Hoàn thành chu kỳ! Trùm đã tái sinh lại từ đầu tại khu vực mới!", 5);
                            Zone targetZone = findAvailableZone(this.map.template.id, this.map);
                            if (targetZone == null) {
                                targetZone = this.map;
                            }
                            respawnInZone(targetZone, (byte) 1);
                        } else {
                            // Open: Chết hẳn -> 30p hồi sinh 1 lần tính thời điểm phút thứ 0 và 30
                            this.isdie = true;
                            this.timeDeath = System.currentTimeMillis();
                            this.levelBoss = 1;
                            this.hp = 0;
                            
                            for (int idx = 0; idx < boss.SuperBossManager.ID_BOSS.length; idx++) {
                                if (boss.SuperBossManager.ID_BOSS[idx] == this.id) {
                                    boss.SuperBossManager.BOSS_LIVE[idx] = 0;
                                    boss.SuperBossManager.BOSS_AREA[idx] = -1;
                                    break;
                                }
                            }
                            Manager.gI().chatKTG(0,
                                    pKill.name + " đã hạ gục " + this.mtemplate.name
                                    + " BẬC TỐI THƯỢNG (Bậc 10)! Siêu Trùm đã bị tiêu diệt hoàn toàn và sẽ tái sinh vào mốc phút thứ :00 hoặc :30 tiếp theo!", 5);
                        }
                    }
                } else {
                    if (isTest) {
                        Manager.gI().chatKTG(0,
                                pKill.name + " đã hạ gục " + this.mtemplate.name
                                + "! Trùm đã hồi sinh ở khu vực khác!", 5);
                        Zone targetZone = findAvailableZone(this.map.template.id, this.map);
                        if (targetZone == null) {
                            targetZone = this.map;
                        }
                        respawnInZone(targetZone, (byte) 1);
                    } else {
                        this.isdie = true;
                        this.timeDeath = System.currentTimeMillis();
                        this.levelBoss = 1;
                        this.hp = 0;
                        for (int idx = 0; idx < boss.SuperBossManager.ID_BOSS.length; idx++) {
                            if (boss.SuperBossManager.ID_BOSS[idx] == this.id) {
                                boss.SuperBossManager.BOSS_LIVE[idx] = 0;
                                boss.SuperBossManager.BOSS_AREA[idx] = -1;
                                break;
                            }
                        }
                        Manager.gI().chatKTG(0,
                                pKill.name + " đã hạ gục " + this.mtemplate.name
                                + "! Siêu Trùm sẽ tái sinh vào mốc phút thứ :00 hoặc :30 tiếp theo!", 5);
                    }
                }
            } else {
                // Boss thường: log death
                Manager.gI().chatKTG(0,
                        pKill.name + " đã hạ gục " + this.mtemplate.name + " bậc " + this.levelBoss,
                        5);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hồi sinh boss trong zone chỉ định, random toạ độ an toàn trong zone đó.
     */
    private void respawnInZone(Zone targetZone, byte newLevelBoss) {
        try {
            if (targetZone == null) return;
            if (isZoneOccupied(targetZone)) {
                Zone freeZone = findAvailableZone(targetZone.template.id, targetZone);
                if (freeZone != null) {
                    targetZone = freeZone;
                } else if (!isAllowMultipleInSameZone()) {
                    // Không có khu trống và chặn trùng con thứ 2
                    return;
                }
            }

            this.isdie = false;
            this.hp = this.hp_max;
            this.id_target = -1;
            this.levelBoss = newLevelBoss;
            this.timeDeath = 0;
            this.TopDame.clear();
            this.map = targetZone;
            this.index = this.index_mob_save;

            short[] coords = calculateCoordinatesInZone(targetZone, (short) -1, (short) -1);
            this.x = coords[0];
            this.y = coords[1];

            targetZone.list_mob_custom_add(this);

            Message m = new Message(1);
            m.writer().writeByte(1);
            m.writer().writeShort(this.index);
            m.writer().writeShort(this.x);
            m.writer().writeShort(this.y);
            targetZone.send_msg_all_p(m, null, true);
            m.cleanup();

            // Cập nhật BOSS_AREA
            for (int i = 0; i < boss.SuperBossManager.ID_BOSS.length; i++) {
                if (boss.SuperBossManager.ID_BOSS[i] == this.id) {
                    boss.SuperBossManager.BOSS_AREA[i] = targetZone.zone_id;
                    break;
                }
            }

            if (isSieuTrum()) {
                boss.SuperBossManager.kickInvalidPlayersInZone(targetZone, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkRespawn() {
        if (!this.isdie) return;
        // Siêu trùm không tự động hồi sinh mỗi tick của Zone, mà hồi sinh theo lịch :00 và :30 qua SuperBossManager.createSuperBoss
        if (isSieuTrum()) {
            return;
        }
        if (getRespawnTimeSeconds() > 0 && this.timeDeath > 0) {
            if (System.currentTimeMillis() - this.timeDeath >= getRespawnTimeSeconds() * 1000L) {
                respawn();
            }
        }
    }

    public void respawn() {
        if (this.map == null) return;
        // Mỗi map id chỉ được 1 siêu trùm ở khu bất kì. Nếu 1 trong all khu có siêu trùm thì không respawn nữa
        if (isSieuTrum() && Zone.hasActiveSuperBossInMap(this.map.template.id)) {
            return;
        }
        if (isZoneOccupied(this.map)) {
            Zone freeZone = findAvailableZone(this.map.template.id, this.map);
            if (freeZone != null) {
                this.map = freeZone;
            } else if (!isAllowMultipleInSameZone()) {
                // Không có khu trống và chặn trùng con thứ 2
                return;
            }
        }
        this.isdie = false;
        this.hp = this.hp_max;
        this.id_target = -1;
        this.levelBoss = 1;
        this.index = this.index_mob_save;
        this.timeDeath = 0;
        this.TopDame.clear();
        
        try {
            Message m_local = new Message(1);
            m_local.writer().writeByte(1);
            m_local.writer().writeShort(this.index);
            m_local.writer().writeShort(this.x);
            m_local.writer().writeShort(this.y);
            if (this.map != null) {
                this.map.send_msg_all_p(m_local, null, true);
            }
            m_local.cleanup();
            Manager.gI().chatKTG(0, this.mtemplate.name + " đã hồi sinh!", 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void dropReward(Player pKill, List<GiftBox> list_gift);
}
