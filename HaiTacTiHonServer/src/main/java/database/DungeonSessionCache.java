package database;

import model.Player;
import zabstracts.AbsDungeon;
import map.Zone;
import mob.Mob;
import map.zones.Map_Mr3;
import map.zones.PhoBanThuThachVeThan;
import map.zones.Map_Lien_Tang;
import map.zones.Map_Sieu_Lien_Tang;
import map.zones.Map_Hang_Dong;
import map.zones.MapDaoKhoBau;
import map.zones.MapAiDon;
import map.zones.VuonCam;
import map.zones.BaoVePhaoDai;
import map.zones.ThuLinhBienKhoi;
import map.zones.MapTranChienKhongLo;
import map.zones.ChiemDao;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DungeonSessionCache — Hệ thống Quản Lý & Lưu Cache Chuẩn Toàn Bộ Phó Bản (RAM + SSD Disk).
 *
 * ------------------------------------------------------
 *  Chức năng:
 *    1. Lưu trữ và đồng bộ hóa mọi Instance Phó Bản người chơi tự mở / đi theo nhóm:
 *       - Map_Mr3 (Bộ Đôi Mr Candle)
 *       - PhoBanThuThachVeThan (Thử Thách Vệ Thần)
 *       - Map_Lien_Tang (Phó Bản Liên Tầng)
 *       - Map_Sieu_Lien_Tang (Phó Bản Siêu Liên Tầng)
 *       - Map_Hang_Dong (Phó Bản Hang Động Bang Hội)
 *       - MapDaoKhoBau (Đảo Kho Báu)
 *       - MapAiDon (Ải Đơn)
 *    2. Khi người chơi hoặc nhóm mất kết nối / bảo trì server:
 *       - Dữ liệu phó bản (mốc kết thúc chuẩn xác, danh sách thành viên trong nhóm, máu quái, stage, level...)
 *         được lưu an toàn vào .cache/dungeon_instances/<instanceId>.json.
 *       - Zone phó bản không bị huỷ ngang nếu thời gian vẫn còn hiệu lực.
 *    3. Khi BẤT KỲ người chơi nào trong nhóm đăng nhập vào game:
 *       - Tự động nhận diện phó bản của nhóm, khôi phục instance/zone (nếu vừa restart server).
 *       - Tự động đưa người chơi vào thẳng phó bản và gửi đếm ngược thời gian còn lại chuẩn xác.
 *       - Tự động liên kết lại nhóm (Party) với các thành viên khác khi họ lần lượt đăng nhập.
 *    4. Khi phó bản hết giờ hoặc hoàn thành:
 *       - Tự động dọn dẹp Zone, xóa instance và xóa file cache SSD.
 * ------------------------------------------------------
 */
public class DungeonSessionCache {

    private static final DungeonSessionCache INSTANCE = new DungeonSessionCache();

    public static DungeonSessionCache gI() {
        return INSTANCE;
    }

    /** Thời gian cache session sống tối đa nếu không có instance (10 phút) */
    private static final long EXPIRE_MS = 10 * 60 * 1000L;

    /** Thư mục lưu SSD cache session */
    private static final String SSD_CACHE_DIR = "./.cache/dungeon";
    /** Thư mục lưu SSD cache instances phó bản */
    private static final String SSD_INSTANCE_DIR = "./.cache/dungeon_instances";

    // ----------------------------------------------------
    //  Data Structures
    // ----------------------------------------------------

    public static class MobSaveData {
        public int mobTemplateId;
        public int index;
        public short x;
        public short y;
        public int hp;
        public int hpMax;
        public int level;
        public boolean isDie;

        public JSONObject toJsonObject() {
            JSONObject obj = new JSONObject();
            obj.put("mobTemplateId", mobTemplateId);
            obj.put("index", index);
            obj.put("x", (int) x);
            obj.put("y", (int) y);
            obj.put("hp", hp);
            obj.put("hpMax", hpMax);
            obj.put("level", level);
            obj.put("isDie", isDie);
            return obj;
        }

        public static MobSaveData fromJsonObject(JSONObject obj) {
            if (obj == null) return null;
            try {
                MobSaveData md = new MobSaveData();
                md.mobTemplateId = obj.containsKey("mobTemplateId") ? Integer.parseInt(obj.get("mobTemplateId").toString()) : 0;
                md.index = obj.containsKey("index") ? Integer.parseInt(obj.get("index").toString()) : -1;
                md.x = obj.containsKey("x") ? Short.parseShort(obj.get("x").toString()) : 0;
                md.y = obj.containsKey("y") ? Short.parseShort(obj.get("y").toString()) : 0;
                md.hp = obj.containsKey("hp") ? Integer.parseInt(obj.get("hp").toString()) : 0;
                md.hpMax = obj.containsKey("hpMax") ? Integer.parseInt(obj.get("hpMax").toString()) : md.hp;
                md.level = obj.containsKey("level") ? Integer.parseInt(obj.get("level").toString()) : 1;
                md.isDie = obj.containsKey("isDie") && Boolean.parseBoolean(obj.get("isDie").toString());
                return md;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class DungeonInstanceData {
        public String instanceId;
        public String dungeonClassName;
        public String creatorName;
        public List<String> participantNames = new ArrayList<>();
        public long createTime;
        public long time; // Mốc deadline kết thúc phó bản (System.currentTimeMillis())
        public int mapId;
        public byte mode;
        public int levelMob;
        public int hpScale;
        public int stageOrType;
        public boolean gifted;
        public boolean isClosed;
        public List<MobSaveData> mobsData = new ArrayList<>();
        public transient AbsDungeon liveDungeon;

        public boolean isValid() {
            return !isClosed && time > System.currentTimeMillis();
        }

        public long getRemainingSeconds() {
            return Math.max(0L, (time - System.currentTimeMillis()) / 1000L);
        }

        @SuppressWarnings("unchecked")
        public JSONObject toJsonObject() {
            JSONObject obj = new JSONObject();
            obj.put("instanceId", instanceId);
            obj.put("dungeonClassName", dungeonClassName);
            obj.put("creatorName", creatorName);
            obj.put("createTime", createTime);
            obj.put("time", time);
            obj.put("mapId", mapId);
            obj.put("mode", (int) mode);
            obj.put("levelMob", levelMob);
            obj.put("hpScale", hpScale);
            obj.put("stageOrType", stageOrType);
            obj.put("gifted", gifted);
            obj.put("isClosed", isClosed);

            JSONArray arrP = new JSONArray();
            if (participantNames != null) arrP.addAll(participantNames);
            obj.put("participantNames", arrP);

            JSONArray arrM = new JSONArray();
            if (mobsData != null) {
                for (MobSaveData md : mobsData) {
                    if (md != null) arrM.add(md.toJsonObject());
                }
            }
            obj.put("mobsData", arrM);

            return obj;
        }

        public static DungeonInstanceData fromJsonObject(JSONObject obj) {
            if (obj == null) return null;
            try {
                DungeonInstanceData d = new DungeonInstanceData();
                d.instanceId = (String) obj.get("instanceId");
                d.dungeonClassName = (String) obj.get("dungeonClassName");
                d.creatorName = (String) obj.get("creatorName");
                d.createTime = obj.containsKey("createTime") ? Long.parseLong(obj.get("createTime").toString()) : System.currentTimeMillis();
                d.time = obj.containsKey("time") ? Long.parseLong(obj.get("time").toString()) : 0L;
                d.mapId = obj.containsKey("mapId") ? Integer.parseInt(obj.get("mapId").toString()) : 0;
                d.mode = obj.containsKey("mode") ? Byte.parseByte(obj.get("mode").toString()) : 0;
                d.levelMob = obj.containsKey("levelMob") ? Integer.parseInt(obj.get("levelMob").toString()) : -1;
                d.hpScale = obj.containsKey("hpScale") ? Integer.parseInt(obj.get("hpScale").toString()) : -1;
                d.stageOrType = obj.containsKey("stageOrType") ? Integer.parseInt(obj.get("stageOrType").toString()) : 0;
                d.gifted = obj.containsKey("gifted") && Boolean.parseBoolean(obj.get("gifted").toString());
                d.isClosed = obj.containsKey("isClosed") && Boolean.parseBoolean(obj.get("isClosed").toString());

                JSONArray arrP = (JSONArray) obj.get("participantNames");
                if (arrP != null) {
                    for (Object o : arrP) {
                        if (o != null) d.participantNames.add(o.toString());
                    }
                }

                JSONArray arrM = (JSONArray) obj.get("mobsData");
                if (arrM != null) {
                    for (Object o : arrM) {
                        if (o instanceof JSONObject) {
                            MobSaveData md = MobSaveData.fromJsonObject((JSONObject) o);
                            if (md != null) d.mobsData.add(md);
                        }
                    }
                }
                return d;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class DungeonResumeData {
        public final String playerName;
        public final List<Integer> dungeonIds;
        public final List<String> dungeonClassNames;
        public final List<String> partyMemberNames;
        public final long disconnectedAt;

        public DungeonResumeData(String playerName,
                                  List<Integer> dungeonIds,
                                  List<String> dungeonClassNames,
                                  List<String> partyMemberNames) {
            this(playerName, dungeonIds, dungeonClassNames, partyMemberNames, System.currentTimeMillis());
        }

        public DungeonResumeData(String playerName,
                                  List<Integer> dungeonIds,
                                  List<String> dungeonClassNames,
                                  List<String> partyMemberNames,
                                  long disconnectedAt) {
            this.playerName        = playerName;
            this.dungeonIds        = dungeonIds;
            this.dungeonClassNames = dungeonClassNames;
            this.partyMemberNames  = partyMemberNames;
            this.disconnectedAt    = disconnectedAt;
        }

        public boolean isValid() {
            return (System.currentTimeMillis() - disconnectedAt) < EXPIRE_MS;
        }

        public long minutesLeft() {
            long left = EXPIRE_MS - (System.currentTimeMillis() - disconnectedAt);
            return Math.max(0L, left / 60_000L);
        }

        @SuppressWarnings("unchecked")
        public JSONObject toJsonObject() {
            JSONObject obj = new JSONObject();
            obj.put("playerName", playerName);
            obj.put("disconnectedAt", disconnectedAt);

            JSONArray arrIds = new JSONArray();
            if (dungeonIds != null) arrIds.addAll(dungeonIds);
            obj.put("dungeonIds", arrIds);

            JSONArray arrNames = new JSONArray();
            if (dungeonClassNames != null) arrNames.addAll(dungeonClassNames);
            obj.put("dungeonClassNames", arrNames);

            JSONArray arrParty = new JSONArray();
            if (partyMemberNames != null) arrParty.addAll(partyMemberNames);
            obj.put("partyMemberNames", arrParty);

            return obj;
        }

        public static DungeonResumeData fromJsonObject(JSONObject obj) {
            if (obj == null) return null;
            try {
                String pName = (String) obj.get("playerName");
                long discAt = obj.containsKey("disconnectedAt") ? Long.parseLong(obj.get("disconnectedAt").toString()) : System.currentTimeMillis();

                List<Integer> ids = new ArrayList<>();
                JSONArray arrIds = (JSONArray) obj.get("dungeonIds");
                if (arrIds != null) {
                    for (Object item : arrIds) {
                        ids.add(Integer.parseInt(item.toString()));
                    }
                }

                List<String> names = new ArrayList<>();
                JSONArray arrNames = (JSONArray) obj.get("dungeonClassNames");
                if (arrNames != null) {
                    for (Object item : arrNames) {
                        names.add(item.toString());
                    }
                }

                List<String> party = new ArrayList<>();
                JSONArray arrParty = (JSONArray) obj.get("partyMemberNames");
                if (arrParty != null) {
                    for (Object item : arrParty) {
                        party.add(item.toString());
                    }
                }

                return new DungeonResumeData(pName, ids, names, party, discAt);
            } catch (Exception e) {
                return null;
            }
        }
    }

    // ----------------------------------------------------
    //  In-Memory Storage
    // ----------------------------------------------------

    /** Key = instanceId */
    private final ConcurrentHashMap<String, DungeonInstanceData> activeInstances = new ConcurrentHashMap<>();
    /** Key = playerName (lower-case), Value = instanceId */
    private final ConcurrentHashMap<String, String> playerToInstanceMap = new ConcurrentHashMap<>();
    /** Key = playerName (lower-case), Value = DungeonResumeData */
    private final ConcurrentHashMap<String, DungeonResumeData> legacySessionCache = new ConcurrentHashMap<>();

    public DungeonSessionCache() {
        ensureDirs();
    }

    private void ensureDirs() {
        try {
            File dir = new File(SSD_CACHE_DIR);
            if (!dir.exists()) dir.mkdirs();
            File instDir = new File(SSD_INSTANCE_DIR);
            if (!instDir.exists()) instDir.mkdirs();
        } catch (Exception ignored) {}
    }

    // ----------------------------------------------------
    //  SSD Disk Helpers for Instances
    // ----------------------------------------------------

    private File getInstanceFile(String instanceId) {
        if (instanceId == null) return null;
        String safeName = instanceId.replaceAll("[^a-zA-Z0-9_-]", "_");
        return new File(SSD_INSTANCE_DIR, safeName + ".json");
    }

    public synchronized void writeInstanceToDisk(DungeonInstanceData data) {
        if (data == null || data.instanceId == null) return;
        ensureDirs();
        File file = getInstanceFile(data.instanceId);
        if (file != null) {
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                writer.write(data.toJsonObject().toJSONString());
            } catch (Exception e) {
                core.Log.error("DungeonSessionCache", "Failed to write instance disk cache for " + data.instanceId, e);
            }
        }
    }

    public synchronized void deleteInstanceFromDisk(String instanceId) {
        if (instanceId == null) return;
        try {
            File file = getInstanceFile(instanceId);
            if (file != null && file.exists()) {
                file.delete();
            }
        } catch (Exception ignored) {}
    }

    private DungeonInstanceData readInstanceFromDisk(File file) {
        if (file == null || !file.exists()) return null;
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Object obj = JSONValue.parse(reader);
            if (obj instanceof JSONObject) {
                return DungeonInstanceData.fromJsonObject((JSONObject) obj);
            }
        } catch (Exception e) {
            core.Log.error("DungeonSessionCache", "Failed to read instance file: " + file.getName(), e);
        }
        return null;
    }

    // ----------------------------------------------------
    //  DISPLAY NAME HELPER
    // ----------------------------------------------------

    public String getDungeonDisplayName(DungeonInstanceData data) {
        if (data == null || data.dungeonClassName == null) return "Phó bản";
        switch (data.dungeonClassName) {
            case "Map_Mr3":
                return "Bộ Đôi Mr Candle";
            case "PhoBanThuThachVeThan":
                return data.stageOrType == 0 ? "Thử Thách Vệ Thần (Phòng chờ)" : "Thử Thách Vệ Thần (Tầng " + Math.max(1, data.mapId - 912) + ")";
            case "Map_Lien_Tang":
                return "Phó Bản Liên Tầng (Tầng " + Math.max(1, data.mapId - 9989) + ")";
            case "Map_Sieu_Lien_Tang":
                return "Phó Bản Siêu Liên Tầng (Tầng " + Math.max(1, data.mapId - 198) + ")";
            case "Map_Hang_Dong":
                return "Phó Bản Hang Động (Ải " + Math.max(1, data.stageOrType) + ")";
            case "MapDaoKhoBau":
                return "Đảo Kho Báu" + (data.stageOrType == 385 ? " (Vé Thường)" : " (Vé Vip)");
            case "MapAiDon":
                return "Ải Đơn (Ải " + Math.max(1, data.mapId - 166) + ", Cấp " + (data.mode + 1) + ")";
            case "VuonCam":
                return "Vườn Cam (Đợt " + Math.max(1, data.stageOrType) + ")";
            case "BaoVePhaoDai":
                return "Bảo Vệ Pháo Đài";
            case "ThuLinhBienKhoi":
                return "Thủ Lĩnh Biển Khơi";
            case "TranChienKhongLo":
            case "MapTranChienKhongLo":
                return "Trận Chiến Khổng Lồ";
            case "ChiemDao":
                return "Chiếm Đảo Bang Hội";
            case "DauTruongTuDo":
                return "Đấu Trường Tự Do";
            default:
                return "Phó bản " + data.dungeonClassName;
        }
    }

    // ----------------------------------------------------
    //  INSTANCE REGISTRATION & MANAGEMENT API
    // ----------------------------------------------------

    /**
     * Đăng ký phó bản người chơi tự mở hoặc đi theo nhóm vào hệ thống Cache.
     */
    public synchronized String registerInstance(AbsDungeon dungeon, String creatorName, List<Player> participants,
                                                int mapId, long deadlineTime, int levelMob, int hpScale,
                                                byte mode, int stageOrType) {
        if (dungeon == null) return null;

        String instId = dungeon.instanceId;
        if (instId == null || !activeInstances.containsKey(instId)) {
            instId = "DNG_" + dungeon.getClass().getSimpleName() + "_" + System.currentTimeMillis() + "_"
                    + (creatorName != null ? creatorName.replaceAll("[^a-zA-Z0-9_-]", "") : "solo");
            dungeon.instanceId = instId;
        }
        dungeon.creatorName = creatorName;

        List<String> pNames = new ArrayList<>();
        // 1. Giữ lại danh sách thành viên cũ của dungeon nếu có (để không mất người bị dis mạng qua tầng)
        if (dungeon.participantNames != null) {
            for (String oldName : dungeon.participantNames) {
                if (oldName != null && !pNames.contains(oldName)) {
                    pNames.add(oldName);
                }
            }
        }
        DungeonInstanceData existingData = activeInstances.get(instId);
        if (existingData != null && existingData.participantNames != null) {
            for (String oldName : existingData.participantNames) {
                if (oldName != null && !pNames.contains(oldName)) {
                    pNames.add(oldName);
                }
            }
        }

        // 2. Bổ sung các player truyền vào
        if (participants != null) {
            for (Player p : participants) {
                if (p != null && p.name != null && !p.isBot
                        && !(p instanceof model.DeTu)
                        && !(p instanceof bot.mercenary.MercenaryBot)) {
                    if (!pNames.contains(p.name)) pNames.add(p.name);
                }
            }
        }
        if (creatorName != null && !pNames.contains(creatorName)) {
            pNames.add(creatorName);
        }
        dungeon.participantNames = pNames;

        DungeonInstanceData data = existingData;
        if (data == null) {
            data = new DungeonInstanceData();
            data.instanceId = instId;
            data.createTime = System.currentTimeMillis();
        }
        data.dungeonClassName = dungeon.getClass().getSimpleName();
        data.creatorName = creatorName;
        data.participantNames = pNames;
        data.time = deadlineTime > 0 ? deadlineTime : (dungeon.time > 0 ? dungeon.time : System.currentTimeMillis() + 120_000L);
        data.mapId = mapId;
        data.mode = mode;
        data.levelMob = levelMob;
        data.hpScale = hpScale;
        data.stageOrType = stageOrType;
        data.liveDungeon = dungeon;

        // Snapshot mobs nếu đã tạo
        data.mobsData.clear();
        if (dungeon.mobs != null) {
            for (Mob m : dungeon.mobs) {
                if (m != null && m.mtemplate != null) {
                    MobSaveData md = new MobSaveData();
                    md.mobTemplateId = m.mtemplate.mob_id;
                    md.index = m.index;
                    md.x = (short) m.x;
                    md.y = (short) m.y;
                    md.hp = m.hp;
                    md.hpMax = m.hp_max;
                    md.level = m.level;
                    md.isDie = m.isdie;
                    data.mobsData.add(md);
                }
            }
        }

        activeInstances.put(instId, data);
        for (String pName : pNames) {
            playerToInstanceMap.put(pName.toLowerCase(), instId);
        }

        writeInstanceToDisk(data);
        core.Log.info("DungeonSessionCache", "Registered active dungeon instance [" + instId + "] for players: "
                + pNames + " | Ends at: " + new Date(data.time) + " | Map: " + mapId + " | Stage/Turn: " + stageOrType);
        return instId;
    }

    /**
     * Cập nhật trạng thái quái, tầng/đợt và thời gian của instance phó bản đang chạy.
     */
    public synchronized void updateInstanceState(AbsDungeon dungeon) {
        if (dungeon == null || dungeon.instanceId == null) return;
        DungeonInstanceData data = activeInstances.get(dungeon.instanceId);
        if (data == null) return;

        data.liveDungeon = dungeon;
        if (dungeon.time > 0) {
            data.time = dungeon.time;
        }

        // Tự động nhận diện và cập nhật Turn / Tầng / Map / Time tương ứng
        if (dungeon instanceof Map_Hang_Dong) {
            Map_Hang_Dong hd = (Map_Hang_Dong) dungeon;
            data.stageOrType = hd.level;
            data.gifted = hd.gifted;
            if (hd.time > 0) data.time = hd.time;
        } else if (dungeon instanceof PhoBanThuThachVeThan) {
            PhoBanThuThachVeThan pb = (PhoBanThuThachVeThan) dungeon;
            data.stageOrType = pb.type;
            data.mapId = pb.mapId;
            data.gifted = pb.gifted;
            if (pb.time_state > 0) data.time = pb.time_state;
        } else if (dungeon instanceof Map_Lien_Tang) {
            Map_Lien_Tang lt = (Map_Lien_Tang) dungeon;
            data.mapId = lt.mapId;
            data.stageOrType = lt.mapId - 9989;
            data.gifted = lt.gifted;
            if (lt.time > 0) data.time = lt.time;
        } else if (dungeon instanceof Map_Sieu_Lien_Tang) {
            Map_Sieu_Lien_Tang slt = (Map_Sieu_Lien_Tang) dungeon;
            data.mapId = slt.mapId;
            data.stageOrType = slt.mapId - 198;
            data.gifted = slt.gifted;
            if (slt.time > 0) data.time = slt.time;
        } else if (dungeon instanceof VuonCam) {
            VuonCam vc = (VuonCam) dungeon;
            data.stageOrType = vc.level;
            if (vc.time_state > 0) data.time = vc.time_state;
        } else if (dungeon instanceof MapDaoKhoBau) {
            MapDaoKhoBau dkb = (MapDaoKhoBau) dungeon;
            data.stageOrType = dkb.ticketId;
            data.gifted = dkb.gifted;
            if (dkb.time > 0) data.time = dkb.time;
        } else if (dungeon instanceof Map_Mr3) {
            Map_Mr3 mr3 = (Map_Mr3) dungeon;
            data.gifted = mr3.gifted;
            if (mr3.time > 0) data.time = mr3.time;
        } else if (dungeon instanceof MapAiDon) {
            MapAiDon ad = (MapAiDon) dungeon;
            if (ad.maps != null) {
                for (Zone z : ad.maps) {
                    if (z != null && z.players != null && !z.players.isEmpty() && z.template != null) {
                        data.mapId = z.template.id;
                        data.stageOrType = z.template.id - 166;
                        break;
                    }
                }
            }
            if (ad.time > 0) data.time = ad.time;
        }

        if (!(dungeon instanceof MapAiDon) && dungeon.maps != null && !dungeon.maps.isEmpty() && dungeon.maps.get(0) != null && dungeon.maps.get(0).template != null) {
            data.mapId = dungeon.maps.get(0).template.id;
        }

        if (dungeon.participantNames != null) {
            for (String pn : dungeon.participantNames) {
                if (pn != null) {
                    if (!data.participantNames.contains(pn)) {
                        data.participantNames.add(pn);
                    }
                    playerToInstanceMap.put(pn.toLowerCase(), data.instanceId);
                }
            }
        }

        data.mobsData.clear();
        if (dungeon.mobs != null) {
            for (Mob m : dungeon.mobs) {
                if (m != null && m.mtemplate != null && !m.isdie && m.hp > 0) {
                    MobSaveData md = new MobSaveData();
                    md.mobTemplateId = m.mtemplate.mob_id;
                    md.index = m.index;
                    md.x = (short) m.x;
                    md.y = (short) m.y;
                    md.hp = m.hp;
                    md.hpMax = m.hp_max;
                    md.level = m.level;
                    md.isDie = false;
                    data.mobsData.add(md);
                }
            }
        }
        writeInstanceToDisk(data);
    }

    /**
     * Dọn dẹp phó bản khi hoàn thành hoặc hết thời gian.
     */
    public synchronized void unregisterInstance(AbsDungeon dungeon) {
        if (dungeon == null) return;
        String instId = dungeon.instanceId;
        if (instId == null) {
            for (Map.Entry<String, DungeonInstanceData> e : activeInstances.entrySet()) {
                if (e.getValue().liveDungeon == dungeon) {
                    instId = e.getKey();
                    break;
                }
            }
        }
        if (instId != null) {
            DungeonInstanceData data = activeInstances.remove(instId);
            if (data != null) {
                for (String pName : data.participantNames) {
                    playerToInstanceMap.remove(pName.toLowerCase());
                    clear(pName);
                }
            }
            deleteInstanceFromDisk(instId);
            core.Log.info("DungeonSessionCache", "Unregistered and cleaned up dungeon instance [" + instId + "]");
        }
    }

    public synchronized void removeParticipant(String instanceId, String playerName) {
        if (instanceId == null || playerName == null) return;
        playerToInstanceMap.remove(playerName.toLowerCase());
        clear(playerName);
        DungeonInstanceData data = activeInstances.get(instanceId);
        if (data != null) {
            data.participantNames.remove(playerName);
            if (data.participantNames.isEmpty()) {
                unregisterInstance(data.liveDungeon);
            } else {
                writeInstanceToDisk(data);
            }
        }
    }

    /**
     * Tìm instance phó bản còn hiệu lực cho player từ disk.
     */
    private DungeonInstanceData findDiskInstanceForPlayer(String playerName) {
        if (playerName == null) return null;
        ensureDirs();
        File dir = new File(SSD_INSTANCE_DIR);
        if (!dir.exists() || !dir.isDirectory()) return null;

        File[] files = dir.listFiles();
        if (files == null) return null;

        long now = System.currentTimeMillis();
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".json")) {
                DungeonInstanceData data = readInstanceFromDisk(f);
                if (data != null) {
                    if (data.time <= now) {
                        f.delete();
                        continue;
                    }
                    if (data.participantNames != null) {
                        for (String pName : data.participantNames) {
                            if (pName.equalsIgnoreCase(playerName)) {
                                return data;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Kiểm tra phó bản đang diễn ra và gửi câu hỏi Yes/No cho người chơi khi đăng nhập.
     *
     * @param p Player vừa đăng nhập
     * @return true nếu đã phát hiện và gửi câu hỏi Yes/No tiếp tục phó bản
     */
    public synchronized boolean checkAndPromptResumeDungeon(Player p) {
        if (p == null || p.name == null || p.isBot || p instanceof model.DeTu || p instanceof bot.mercenary.MercenaryBot) return false;
        String pNameLower = p.name.toLowerCase();

        String instanceId = playerToInstanceMap.get(pNameLower);
        DungeonInstanceData data = null;
        if (instanceId != null) {
            data = activeInstances.get(instanceId);
        }
        if (data == null) {
            data = findDiskInstanceForPlayer(p.name);
        }

        if (data == null || !data.isValid()) {
            if (data != null) {
                unregisterInstance(data.liveDungeon);
            }
            return false;
        }

        long remainSec = data.getRemainingSeconds();
        if (remainSec <= 0) {
            unregisterInstance(data.liveDungeon);
            return false;
        }

        final String targetInstId = data.instanceId;
        final String displayName = getDungeonDisplayName(data);
        long mins = remainSec / 60;
        long secs = remainSec % 60;

        String notice = "Bạn đang có phó bản [" + displayName + "] chưa hoàn thành!\n"
                + "Thời gian còn lại: " + mins + " phút " + secs + " giây.\n\n"
                + "Bạn có muốn tiếp tục tham gia không?";

        model.YesNoDialog dialog = new model.YesNoDialog(p, 9999, "Tiếp Tục Phó Bản", notice,
                new String[]{"Tiếp tục", "Hủy bỏ"}, new byte[]{2, 1}, (value) -> {
            if (value == 0) { // Tiếp tục / Đồng ý
                boolean ok = resumeDungeon(p, targetInstId);
                if (!ok) {
                    p.getService().send_box_ThongBao_OK("Phó bản đã kết thúc hoặc không còn khả dụng!");
                    p.return_to_previous_map();
                }
            } else { // Hủy bỏ / Từ chối / Về map trước
                removeParticipant(targetInstId, p.name);
                clear(p.name);
                p.dungeon = null;
                p.clearAllDungeons();
                p.return_to_previous_map();
                p.getService().send_box_ThongBao_OK("Đã hủy tham gia và trở về bản đồ trước khi vào phó bản.");
            }
        });
        dialog.startYesNo();
        return true;
    }

    /**
     * Đưa người chơi vào lại phó bản đang chạy.
     *
     * @param p Player
     * @param instanceId ID instance phó bản
     * @return true nếu thành công
     */
    public synchronized boolean resumeDungeon(Player p, String instanceId) {
        if (p == null || p.name == null || instanceId == null) return false;
        DungeonInstanceData data = activeInstances.get(instanceId);
        if (data == null) {
            data = findDiskInstanceForPlayer(p.name);
        }

        if (data == null || !data.isValid()) {
            if (data != null) {
                unregisterInstance(data.liveDungeon);
            }
            return false;
        }

        try {
            AbsDungeon dungeon = data.liveDungeon;
            Zone targetZone = null;

            if (dungeon != null && dungeon.maps != null && !dungeon.maps.isEmpty()) {
                for (Zone z : dungeon.maps) {
                    if (z != null && z.template != null && z.template.id == data.mapId) {
                        targetZone = z;
                        break;
                    }
                }
                if (targetZone == null) {
                    targetZone = dungeon.maps.get(0);
                }
            }

            // Nếu zone chưa chạy hoặc vừa restart server -> khôi phục lại zone và mob
            if (targetZone == null || !targetZone.isRun() || !Zone.get_map_plus().contains(targetZone)) {
                dungeon = restoreDungeonFromData(data);
                if (dungeon != null && dungeon.maps != null && !dungeon.maps.isEmpty()) {
                    for (Zone z : dungeon.maps) {
                        if (z != null && z.template != null && z.template.id == data.mapId) {
                            targetZone = z;
                            break;
                        }
                    }
                    if (targetZone == null) {
                        targetZone = dungeon.maps.get(0);
                    }
                    data.liveDungeon = dungeon;
                    activeInstances.put(data.instanceId, data);
                    for (String pn : data.participantNames) {
                        playerToInstanceMap.put(pn.toLowerCase(), data.instanceId);
                    }
                }
            }

            if (dungeon != null && targetZone != null) {
                if (!targetZone.isRun()) {
                    targetZone.setRunning(true);
                    targetZone.start_map();
                }
                if (!Zone.get_map_plus().contains(targetZone)) {
                    Zone.add_map_plus(targetZone);
                }

                // Gắn dungeon vào player
                p.dungeon = dungeon;
                p.addDungeon(dungeon);

                // Rời map hiện tại nếu đang ở map khác
                if (p.map != null && !p.map.equals(targetZone)) {
                    try {
                        p.map.leave_map(p, 0);
                    } catch (Exception ignored) {}
                }

                // Thiết lập toạ độ vào map phó bản
                p.map = targetZone;
                p.x = dungeon.getJoinX() > 0 ? dungeon.getJoinX() : 350;
                p.y = dungeon.getJoinY() > 0 ? dungeon.getJoinY() : 260;
                p.xold = p.x;
                p.yold = p.y;
                p.lastValidX = p.x;
                p.lastValidY = p.y;

                // Tự động ghép nối lại nhóm (Party) nếu nhóm còn người online
                relinkPartyForDungeon(p, data);

                // Vào map
                targetZone.goto_map(p);

                try {
                    p.getService().update_PK(p, true);
                    p.getService().pet(p, true);
                    model.Quest.update_map_have_side_quest(p, true);
                } catch (Exception ignored) {}

                // Gửi đếm ngược thời gian còn lại chuẩn xác theo realtime
                long remainSec = data.getRemainingSeconds();
                String displayName = getDungeonDisplayName(data);
                p.getService().send_time_cool_down(data.time, displayName, 0);
                p.getService().send_box_ThongBao_OK("Bạn đã trở lại phó bản [" + displayName + "] đang diễn ra!\n(Thời gian còn lại: "
                        + (remainSec / 60) + " phút " + (remainSec % 60) + " giây)");

                core.Log.info("DungeonSessionCache", "Successfully resumed player [" + p.name + "] into dungeon ["
                        + data.instanceId + "] map=" + targetZone.template.id + " | stage=" + data.stageOrType);
                return true;
            }
        } catch (Exception e) {
            core.Log.error("DungeonSessionCache", "Error resuming dungeon for " + p.name, e);
        }
        return false;
    }

    /**
     * Tự động ghép lại nhóm Party cho phó bản khi các thành viên lần lượt đăng nhập vào.
     */
    private void relinkPartyForDungeon(Player p, DungeonInstanceData data) {
        if (p == null || data == null || data.participantNames == null || data.participantNames.size() <= 1) return;

        // Tìm xem có thành viên nào khác trong nhóm đang online không
        for (String memberName : data.participantNames) {
            if (memberName.equalsIgnoreCase(p.name)) continue;
            Player otherP = Zone.get_player_by_name_allmap(memberName);
            if (otherP != null && otherP.party != null && otherP.party.list != null) {
                if (!otherP.party.list.contains(p)) {
                    otherP.party.list.add(p);
                    p.party = otherP.party;
                    try {
                        p.party.send_info();
                    } catch (Exception ignore) {}
                }
                return;
            }
        }

        // Nếu chưa có ai online trước, tạo party mới cho p để các thành viên sau vào sẽ ghép chung
        if (p.party == null && data.participantNames.size() > 1) {
            p.party = new model.Party(p);
        }
    }

    /**
     * Khôi phục AbsDungeon instance từ dữ liệu đã lưu khi server khởi động lại.
     */
    private AbsDungeon restoreDungeonFromData(DungeonInstanceData data) {
        if (data == null || data.dungeonClassName == null) return null;
        try {
            AbsDungeon dungeon = null;
            switch (data.dungeonClassName) {
                case "Map_Mr3": {
                    Map_Mr3 mr3 = new Map_Mr3(data.levelMob, data.hpScale);
                    mr3.instanceId = data.instanceId;
                    mr3.creatorName = data.creatorName;
                    mr3.participantNames = new ArrayList<>(data.participantNames);
                    mr3.create();
                    mr3.time = data.time;
                    mr3.gifted = data.gifted;
                    restoreMobStates(mr3.mobs, data.mobsData);
                    dungeon = mr3;
                    break;
                }
                case "PhoBanThuThachVeThan": {
                    PhoBanThuThachVeThan ttvt = new PhoBanThuThachVeThan(data.levelMob, data.hpScale, data.stageOrType,
                            data.mapId > 0 ? data.mapId : 984, Math.max(1000L, data.time - System.currentTimeMillis()));
                    ttvt.instanceId = data.instanceId;
                    ttvt.creatorName = data.creatorName;
                    ttvt.participantNames = new ArrayList<>(data.participantNames);
                    ttvt.create();
                    ttvt.time = data.time;
                    ttvt.time_state = data.time;
                    ttvt.gifted = data.gifted;
                    restoreMobStates(ttvt.mobs, data.mobsData);
                    dungeon = ttvt;
                    break;
                }
                case "Map_Lien_Tang": {
                    Map_Lien_Tang lt = new Map_Lien_Tang(data.levelMob, data.hpScale, data.mapId > 0 ? data.mapId : 9990);
                    lt.instanceId = data.instanceId;
                    lt.creatorName = data.creatorName;
                    lt.participantNames = new ArrayList<>(data.participantNames);
                    lt.create();
                    lt.time = data.time;
                    lt.gifted = data.gifted;
                    restoreMobStates(lt.mobs, data.mobsData);
                    dungeon = lt;
                    break;
                }
                case "Map_Sieu_Lien_Tang": {
                    Map_Sieu_Lien_Tang slt = new Map_Sieu_Lien_Tang(data.levelMob, data.hpScale, data.mapId > 0 ? data.mapId : 199);
                    slt.instanceId = data.instanceId;
                    slt.creatorName = data.creatorName;
                    slt.participantNames = new ArrayList<>(data.participantNames);
                    slt.create();
                    slt.time = data.time;
                    slt.gifted = data.gifted;
                    restoreMobStates(slt.mobs, data.mobsData);
                    dungeon = slt;
                    break;
                }
                case "Map_Hang_Dong": {
                    Map_Hang_Dong hd = new Map_Hang_Dong(data.levelMob, data.hpScale, data.stageOrType > 0 ? data.stageOrType : 1);
                    hd.instanceId = data.instanceId;
                    hd.creatorName = data.creatorName;
                    hd.participantNames = new ArrayList<>(data.participantNames);
                    hd.create();
                    hd.time = data.time;
                    hd.gifted = data.gifted;
                    restoreMobStates(hd.mobs, data.mobsData);
                    dungeon = hd;
                    break;
                }
                case "MapDaoKhoBau": {
                    MapDaoKhoBau dkb = new MapDaoKhoBau(data.levelMob, data.stageOrType > 0 ? data.stageOrType : 385);
                    dkb.instanceId = data.instanceId;
                    dkb.creatorName = data.creatorName;
                    dkb.participantNames = new ArrayList<>(data.participantNames);
                    dkb.create();
                    dkb.time = data.time;
                    dkb.gifted = data.gifted;
                    restoreMobStates(dkb.mobs, data.mobsData);
                    dungeon = dkb;
                    break;
                }
                case "MapAiDon": {
                    MapAiDon ad = new MapAiDon();
                    ad.instanceId = data.instanceId;
                    ad.creatorName = data.creatorName;
                    ad.participantNames = new ArrayList<>(data.participantNames);
                    ad.mode = data.mode;
                    ad.hpScale = data.hpScale > 0 ? data.hpScale : 1000;
                    ad.playerLevel = data.levelMob > 0 ? data.levelMob : 35;
                    ad.create();
                    ad.time = data.time;
                    if (data.stageOrType > 0) {
                        for (int m = 167; m < data.mapId; m++) {
                            ad.getCheckG().add(m);
                        }
                    }
                    restoreMobStates(ad.mobs, data.mobsData);
                    dungeon = ad;
                    break;
                }
                case "VuonCam": {
                    VuonCam vc = new VuonCam(data.hpScale, data.levelMob);
                    vc.instanceId = data.instanceId;
                    vc.creatorName = data.creatorName;
                    vc.participantNames = new ArrayList<>(data.participantNames);
                    vc.level = data.stageOrType > 0 ? data.stageOrType : 1;
                    vc.create();
                    vc.time = data.time;
                    vc.time_state = data.time;
                    restoreMobStates(vc.mobs, data.mobsData);
                    dungeon = vc;
                    break;
                }
                case "ThuLinhBienKhoi": {
                    ThuLinhBienKhoi tlbk = new ThuLinhBienKhoi(data.stageOrType > 0 ? data.stageOrType : 4);
                    tlbk.instanceId = data.instanceId;
                    tlbk.creatorName = data.creatorName;
                    tlbk.participantNames = new ArrayList<>(data.participantNames);
                    tlbk.time = data.time;
                    dungeon = tlbk;
                    break;
                }
            }
            return dungeon;
        } catch (Exception e) {
            core.Log.error("DungeonSessionCache", "Failed to restore dungeon from data: " + data.instanceId, e);
            return null;
        }
    }

    private void restoreMobStates(List<Mob> liveMobs, List<MobSaveData> savedMobs) {
        if (liveMobs == null) return;
        if (savedMobs == null || savedMobs.isEmpty()) {
            for (Mob liveMob : new ArrayList<>(liveMobs)) {
                if (liveMob != null) {
                    liveMob.hp = 0;
                    liveMob.isdie = true;
                    if (liveMob.map != null && liveMob.map.mobs != null) {
                        liveMob.map.mobs.remove(liveMob.index);
                    }
                }
            }
            liveMobs.clear();
            return;
        }

        List<Mob> deadMobs = new ArrayList<>();
        Set<Integer> usedSavedIndexes = new HashSet<>();
        Set<Mob> matchedLiveMobs = new HashSet<>();

        // 1. So khớp chính xác theo mob index trước
        for (Mob liveMob : liveMobs) {
            for (int i = 0; i < savedMobs.size(); i++) {
                if (usedSavedIndexes.contains(i)) continue;
                MobSaveData sm = savedMobs.get(i);
                if (liveMob.index == sm.index) {
                    usedSavedIndexes.add(i);
                    matchedLiveMobs.add(liveMob);
                    applyMobState(liveMob, sm, deadMobs);
                    break;
                }
            }
        }

        // 2. Với các liveMob chưa khớp index, so khớp theo mobTemplateId (mỗi savedMob chỉ dùng 1 lần)
        for (Mob liveMob : liveMobs) {
            if (matchedLiveMobs.contains(liveMob)) continue;
            for (int i = 0; i < savedMobs.size(); i++) {
                if (usedSavedIndexes.contains(i)) continue;
                MobSaveData sm = savedMobs.get(i);
                if (liveMob.mtemplate != null && liveMob.mtemplate.mob_id == sm.mobTemplateId) {
                    usedSavedIndexes.add(i);
                    matchedLiveMobs.add(liveMob);
                    applyMobState(liveMob, sm, deadMobs);
                    break;
                }
            }
        }

        // 3. Bất kỳ liveMob nào KHÔNG nằm trong matchedLiveMobs nghĩa là đã bị tiêu diệt trước khi lưu -> Đánh dấu chết
        for (Mob liveMob : liveMobs) {
            if (!matchedLiveMobs.contains(liveMob)) {
                liveMob.hp = 0;
                liveMob.isdie = true;
                deadMobs.add(liveMob);
            }
        }

        for (Mob dm : deadMobs) {
            liveMobs.remove(dm);
            if (dm.map != null && dm.map.mobs != null) {
                dm.map.mobs.remove(dm.index);
            }
        }
    }

    private void applyMobState(Mob liveMob, MobSaveData sm, List<Mob> deadMobs) {
        if (liveMob == null || sm == null) return;
        if (sm.isDie || sm.hp <= 0) {
            liveMob.hp = 0;
            liveMob.isdie = true;
            deadMobs.add(liveMob);
        } else {
            liveMob.hp = sm.hp;
            liveMob.hp_max = sm.hpMax > 0 ? sm.hpMax : liveMob.hp_max;
            liveMob.isdie = false;
        }
    }

    /**
     * Nạp toàn bộ dữ liệu dungeon instances khi khởi động server.
     */
    public synchronized void loadAllDungeonsFromCache() {
        ensureDirs();
        File dir = new File(SSD_INSTANCE_DIR);
        if (!dir.exists() || !dir.isDirectory()) return;

        File[] files = dir.listFiles();
        if (files == null) return;

        long now = System.currentTimeMillis();
        int loaded = 0;
        int expired = 0;

        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".json")) {
                DungeonInstanceData data = readInstanceFromDisk(f);
                if (data != null) {
                    if (data.time <= now) {
                        f.delete();
                        expired++;
                    } else {
                        activeInstances.put(data.instanceId, data);
                        for (String pName : data.participantNames) {
                            playerToInstanceMap.put(pName.toLowerCase(), data.instanceId);
                        }
                        loaded++;
                    }
                }
            }
        }
//        core.Log.info("DungeonSessionCache", "Loaded " + loaded + " active dungeon instances from SSD cache (" + expired + " expired cleaned).");
    }

    /**
     * Lưu toàn bộ dữ liệu dungeon instances khi tắt server (bảo trì).
     */
    public synchronized void saveAllDungeonsToCache() {
        for (DungeonInstanceData data : activeInstances.values()) {
            if (data != null && data.isValid()) {
                if (data.liveDungeon != null) {
                    updateInstanceState(data.liveDungeon);
                } else {
                    writeInstanceToDisk(data);
                }
            }
        }
        core.Log.info("DungeonSessionCache", "Saved " + activeInstances.size() + " active dungeon instances to SSD cache.");
    }

    // ----------------------------------------------------
    //  LEGACY SESSION CACHE HELPERS (Tương thích ngược)
    // ----------------------------------------------------

    private File getSsdFile(String playerName) {
        if (playerName == null) return null;
        String safeName = playerName.toLowerCase().replaceAll("[^a-z0-9_-]", "_");
        return new File(SSD_CACHE_DIR, safeName + ".json");
    }

    private void writeSsdCache(DungeonResumeData data) {
        if (data == null || data.playerName == null) return;
        try {
            File file = getSsdFile(data.playerName);
            if (file != null) {
                try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                    writer.write(data.toJsonObject().toJSONString());
                }
            }
        } catch (Exception e) {
            core.Log.error("DungeonSessionCache", "Failed to write SSD cache for " + data.playerName, e);
        }
    }

    private DungeonResumeData readSsdCache(String playerName) {
        File file = getSsdFile(playerName);
        if (file == null || !file.exists()) return null;

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            Object obj = JSONValue.parse(reader);
            if (obj instanceof JSONObject) {
                return DungeonResumeData.fromJsonObject((JSONObject) obj);
            }
        } catch (Exception e) {
            core.Log.error("DungeonSessionCache", "Failed to read SSD cache for " + playerName, e);
        }
        return null;
    }

    private void deleteSsdCache(String playerName) {
        try {
            File file = getSsdFile(playerName);
            if (file != null && file.exists()) {
                file.delete();
            }
        } catch (Exception ignored) {}
    }

    public void save(Player p) {
        if (p == null || p.name == null) return;
        
        List<AbsDungeon> targetDungeons = new ArrayList<>();
        if (p.dungeons != null && !p.dungeons.isEmpty()) {
            targetDungeons.addAll(p.dungeons.values());
        }
        if (p.dungeon != null && !targetDungeons.contains(p.dungeon)) {
            targetDungeons.add(p.dungeon);
        }
        if (p.map != null) {
            if (p.map.map_dungeon != null && !targetDungeons.contains(p.map.map_dungeon)) {
                targetDungeons.add(p.map.map_dungeon);
            }
            if (p.map.map_LienTang != null && !targetDungeons.contains(p.map.map_LienTang)) {
                targetDungeons.add(p.map.map_LienTang);
            }
            if (p.map.map_SieuLienTang != null && !targetDungeons.contains(p.map.map_SieuLienTang)) {
                targetDungeons.add(p.map.map_SieuLienTang);
            }
            if (p.map.map_ThuThachVeThan != null && !targetDungeons.contains(p.map.map_ThuThachVeThan)) {
                targetDungeons.add(p.map.map_ThuThachVeThan);
            }
            if (p.map.map_Hang != null && !targetDungeons.contains(p.map.map_Hang)) {
                targetDungeons.add(p.map.map_Hang);
            }
            if (p.map.map_Mr3 != null && !targetDungeons.contains(p.map.map_Mr3)) {
                targetDungeons.add(p.map.map_Mr3);
            }
            if (p.map.map_DaoKhoBau != null && !targetDungeons.contains(p.map.map_DaoKhoBau)) {
                targetDungeons.add(p.map.map_DaoKhoBau);
            }
        }

        if (targetDungeons.isEmpty()) return;

        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (AbsDungeon d : targetDungeons) {
            ids.add(d.getDungeonId());
            names.add(d.getClass().getSimpleName());
            updateInstanceState(d);
        }

        List<String> partyNames = new ArrayList<>();
        if (p.party != null && p.party.list != null) {
            for (Player pm : p.party.list) {
                if (pm != null && pm.name != null && !pm.name.equals(p.name)) {
                    partyNames.add(pm.name);
                }
            }
        }

        DungeonResumeData data = new DungeonResumeData(p.name, ids, names, partyNames);
        legacySessionCache.put(p.name.toLowerCase(), data);
        writeSsdCache(data);

        core.Log.info("DungeonSessionCache", "Saved session cache for [" + p.name + "] dungeons=" + names);
    }

    public DungeonResumeData restore(Player p) {
        if (p == null || p.name == null) return null;
        String key = p.name.toLowerCase();

        DungeonResumeData data = legacySessionCache.remove(key);
        if (data == null) {
            data = readSsdCache(p.name);
        }
        deleteSsdCache(p.name);

        if (data == null) return null;
        if (!data.isValid()) return null;

        return data;
    }

    public void clear(String playerName) {
        if (playerName == null) return;
        legacySessionCache.remove(playerName.toLowerCase());
        deleteSsdCache(playerName);
    }

    public boolean has(String playerName) {
        if (playerName == null) return false;
        if (playerToInstanceMap.containsKey(playerName.toLowerCase())) return true;
        DungeonResumeData d = legacySessionCache.get(playerName.toLowerCase());
        if (d == null) {
            d = readSsdCache(playerName);
        }
        return d != null && d.isValid();
    }

    public void evictExpired() {
        int removed = 0;
        long now = System.currentTimeMillis();

        // Evict expired instances
        for (Iterator<Map.Entry<String, DungeonInstanceData>> it = activeInstances.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, DungeonInstanceData> entry = it.next();
            if (!entry.getValue().isValid()) {
                for (String pName : entry.getValue().participantNames) {
                    playerToInstanceMap.remove(pName.toLowerCase());
                }
                deleteInstanceFromDisk(entry.getKey());
                it.remove();
                removed++;
            }
        }

        // Evict expired legacy session cache
        for (Iterator<Map.Entry<String, DungeonResumeData>> it = legacySessionCache.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, DungeonResumeData> entry = it.next();
            if (!entry.getValue().isValid()) {
                deleteSsdCache(entry.getValue().playerName);
                it.remove();
                removed++;
            }
        }

        // Quét thêm ổ SSD
        try {
            File dir = new File(SSD_CACHE_DIR);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.isFile() && f.getName().endsWith(".json")) {
                            if ((now - f.lastModified()) > EXPIRE_MS) {
                                f.delete();
                                removed++;
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        if (removed > 0) {
            core.Log.info("DungeonSessionCache", "Evicted " + removed + " expired SSD cache entries.");
        }
    }

    public int size() {
        return activeInstances.size() + legacySessionCache.size();
    }
}

