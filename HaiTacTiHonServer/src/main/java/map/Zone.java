package map;

import model.MyPet;
import model.Player;
import model.ActiveBuff;
import zabstracts.AbsMap;
import mob.Mob;
import map.zones.BaoVePhaoDai;
import map.zones.DauTruongTuDo;
import skill.Skill_info;
import network.Service;
import template.ItemTemplate4;
import template.ItemTemplate7;

import clan.ClanMember;
import clan.ClanChat;
import clan.Clan;
import boss.SuperBossManager;
import rank.Ranked;
import map.zones.*;
import bot.BotKhoBau;
import boss.BossPica;
import boss.BossTheGioi;
import event.SuKienTrongCay;
import network.MessageHandler;
import event.eboss.BiNgoMa;
import event.SuKienHalloween;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import event.SuKienNoel;
import event.eboss.SantaNoel;
import event.eboss.QuaiVatTuyetNoel;
import event.eboss.LanSuTu;
import activities.*;
import achievement.ArchiDaily;
import core.*;
import database.IDManager;
import database.DbManager;
import event.EventManager;
import event.SuKienGioTo;
import event.SuKienKinhKibi;
import template.ItemTemplate4;
import template.ItemTemplate7;
import network.Message;
import network.Session;
import network.SessionManager;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map.Entry;
import template.*;
import map.zones.*;

public class Zone extends AbsMap {

    public static boolean isWaitingOrUnstartedMatch(Zone zone) {
        if (zone == null || zone.template == null) return true;
        int id = zone.template.id;
        if (id == 1000 || id == 119 || id == 260 || isMapLang(id)) return true;
        if (zone.map_vp instanceof MapPvp) {
            MapPvp pvp = (MapPvp) zone.map_vp;
            if (pvp.status_pvp != 3) {
                return true;
            }
        }
        if (id >= 272 && id <= 275) {
            if (!WorldWar.runnning || WorldWar.status != WorldWar.STATUS_WAR) {
                return true;
            }
        }
        if (zone.pvpBang != null && zone.pvpBang.state != PvpBang.FIGHTING) {
            return true;
        }
        if (zone.pvpBangMapFight != null && zone.pvpBangMapFight.status_pvp != 3) {
            return true;
        }
        return false;
    }

    private static List<Zone> MAP_PLUS = new ArrayList<>();



    public static byte weather = -1;
    public static byte weather_level = 1;

    public static Player get_player_by_Index_allmap(int index) {
        return network.SessionManager.PLAYERS_BY_INDEX.get(index);
    }
    public static Player get_player_by_id_allmap(int id) {
        return network.SessionManager.PLAYERS_MAP.get(id);
    }
    public MapTemplate template;
    public PhoBanThuThachVeThan map_ThuThachVeThan;
    public Map_Lien_Tang map_LienTang;
    public Map_Sieu_Lien_Tang map_SieuLienTang;
    public Map_Mr3 map_Mr3;
    public ZSanTrum mapSanTrum;
    private boolean running;
    public Thread mythread;
    public List<Player> players = new java.util.concurrent.CopyOnWriteArrayList<>();
    public List<Player> onlyPlayers = new java.util.concurrent.CopyOnWriteArrayList<>();
    public final HashMap<Integer, Mob> mobs = new HashMap<>();

    // [FIX MEMORY] Reusable lists để tránh tạo new ArrayList() mỗi 125ms trong game loop
    // update_player() có synchronized nên dùng field an toàn
    private final List<Player> _tmp_remove     = new ArrayList<>();
    private final List<Player> _tmp_remove2    = new ArrayList<>();
    private final List<Player> _tmp_remove3    = new ArrayList<>();
    private final List<Mob>    _tmp_mobs       = new ArrayList<>();
    private final List<Mob>    _tmp_dungeon_rm = new ArrayList<>();
    private final List<Mob>    _tmp_dungeon_gt = new ArrayList<>();
    private final java.util.concurrent.atomic.AtomicBoolean isTicking = new java.util.concurrent.atomic.AtomicBoolean(false);

    public boolean tryBeginTick() {
        return isTicking.compareAndSet(false, true);
    }

    public void endTick() {
        isTicking.set(false);
    }

    public Mob getMob(int id) {
        Mob m = this.mobs.get(id);
        if (m == null) {
            m = Mob.ENTRYS.get(id);
        }
        return m;
    }
    public int[] list_mob;
    public byte zone_id;
    public MapPvp map_vp;
    public zabstracts.AbsDungeon map_dungeon;
    public VuonCam vuonCam;
    public LanhDiaClan clan_resource;
    public MapTranChienKhongLo map_little_garden;
    public ItemMap[] list_it_map = new ItemMap[1_000];
    public boolean can_PK = true;
    public ThuLinhBienKhoi map_thuLinhBienKhoi;
    public MapDaoKhoBau map_DaoKhoBau;
    public PvpBang pvpBang;
    public Map_Hang_Dong map_Hang;
    public PvpBangMapFight pvpBangMapFight;
    public BaoVePhaoDai baoVePhaoDai;
    public boolean IsMapBaoVePhaoDai() {
        return (this.template != null && this.template.id >= 267 && this.template.id <= 271);
    }
    public static Clan getChiemDaoTopClan(int mapId) {
        return map.zones.ChiemDao.getClanTop(mapId);
    }
    public static int getChiemDaoAliveSubTurrets(Zone z) {
        return map.zones.ChiemDao.getAliveSubTurretCount(z);
    }
    private map.MapService service;
    private long lastUpdateWwScore = 0;

    public Zone() {
        this.running = true;
        this.service = new map.MapService(this);
    }

    public map.MapService getService() {
        return this.service;
    }

    /**
     * Gửi vị trí mob/boss tới player (type=1: mob, type=0: player).
     * Giảm lặp lại tạo Message(1) khắp nơi.
     */
    public static void sendMobPos(Player p, Mob mob) throws IOException {
        if (p != null && p.map != null && p.map.getService() != null && mob != null) {
            p.map.getService().move(p, (byte) 1, mob.index, mob.x, mob.y);
        }
    }

    /** Gửi vị trí mob/boss tới toàn bộ zone (broadcast). */
    public void broadcastMobPos(Mob mob) throws IOException {
        if (mob != null && this.service != null) {
            this.service.move(mob.index, mob.x, mob.y);
        }
    }

    public static boolean is_map_boss(int id) {
        return id == 5 || id == 13 || id == 21 || id == 29 || id == 37 || id == 45 || id == 37
                || id == 45 || id == 53 || id == 73 || id == 87 || id == 102 || id == 127
                || id == 198;
    }





    public static boolean is_map_dungeon(int id) {
        return (id >= 167 && id <= 176) || id == 80 || id == 81 || id == 120 || id == 301 || id == 984 || (id >= 913 && id <= 917) || id == 62 || id == 266 || (id >= 254 && id <= 258) || (id >= 261 && id <= 271) || (id >= 9990 && id <= 10009) || (id >= 199 && id <= 218) || id == 59;
    }

    public boolean isDungeon() {
        return this.map_dungeon != null
            || this.map_Hang != null
            || this.map_LienTang != null
            || this.map_SieuLienTang != null
            || this.map_Mr3 != null
            || this.map_ThuThachVeThan != null
            || this.vuonCam != null
            || this.mapSanTrum != null
            || this.map_DaoKhoBau != null
            || this.baoVePhaoDai != null
            || (this.template != null && (Zone.is_map_dungeon(this.template.id)
                || (this.template.id >= 9990 && this.template.id <= 10009)
                || (this.template.id >= 199 && this.template.id <= 218)));
    }

    public Class<?> getDungeonClass() {
        if (this.map_dungeon != null) return this.map_dungeon.getClass();
        if (this.map_Hang != null) return map.zones.Map_Hang_Dong.class;
        if (this.map_LienTang != null) return map.zones.Map_Lien_Tang.class;
        if (this.map_SieuLienTang != null) return map.zones.Map_Sieu_Lien_Tang.class;
        if (this.map_Mr3 != null) return map.zones.Map_Mr3.class;
        if (this.map_ThuThachVeThan != null) return map.zones.PhoBanThuThachVeThan.class;
        if (this.vuonCam != null) return map.zones.VuonCam.class;
        if (this.mapSanTrum != null) return map.zones.ZSanTrum.class;
        if (this.map_DaoKhoBau != null) return map.zones.MapDaoKhoBau.class;
        if (this.baoVePhaoDai != null) return map.zones.BaoVePhaoDai.class;
        if (this.template != null) {
            int mapId = this.template.id;
            if (mapId == 81) return map.zones.MapTranChienKhongLo.class;
            if (mapId == 301) return map.zones.Map_Hang_Dong.class;
            if (mapId == 80 || mapId == 120) return map.zones.Map_Mr3.class;
            if (mapId == 266) return map.zones.VuonCam.class;
            if (mapId == 62) return map.zones.MapDaoKhoBau.class;
            if (mapId == 984 || (mapId >= 913 && mapId <= 917)) return map.zones.PhoBanThuThachVeThan.class;
            if (mapId >= 9990 && mapId <= 10009) return map.zones.Map_Lien_Tang.class;
            if (mapId >= 199 && mapId <= 218) return map.zones.Map_Sieu_Lien_Tang.class;
            if (mapId >= 167 && mapId <= 176) return map.zones.MapAiDon.class;
            if ((mapId >= 261 && mapId <= 265) || mapId == 59) return map.zones.ZSanTrum.class;
            if (mapId >= 267 && mapId <= 271) return map.zones.BaoVePhaoDai.class;
        }
        return null;
    }

    public static boolean is_map_luyentap(int id) {
        return id >= 220 && id <= 229;
    }

    public static boolean isMapNoQuestLimit(int id) {
        return id > 900 
            || id == 62 
            || id == 81 
            || (id >= 167 && id <= 176) 
            || id == 199 
            || id == 80 
            || (id >= 119 && id <= 123) 
            || (id >= 913 && id <= 917) 
            || (id >= 58 && id <= 59) 
            || (id >= 178 && id <= 184) 
            || id >= 200 
            || id == 984 
            || id == 1000 
            || id == 127 
            || id == 54;
    }

    public static void add_map_plus(Zone map_boss) {
        if (map_boss == null) return;
        map_boss.setRunning(true);
        map_boss.start_map();
        synchronized (Zone.MAP_PLUS) {
            if (!Zone.MAP_PLUS.contains(map_boss)) {
                Zone.MAP_PLUS.add(map_boss);
            }
        }
        MapManager.getInstance().startMapPlus(map_boss);
    }

    public static void remove_map_plus(Zone map_boss) {
        if (map_boss == null) return;
        synchronized (Zone.MAP_PLUS) {
            Zone.MAP_PLUS.remove(map_boss);
        }
    }

    public static List<Zone> get_map_plus() {
        return Zone.MAP_PLUS;
    }

    public static boolean isMapLang(int id) {
        for (int i = 0; i < MenuController.ID_MAP_LANG.length; i++) {
            if (id == MenuController.ID_MAP_LANG[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lấy ID bản đồ cổng đảo/làng xuất phát tương ứng với map Chiến trường / Cổng Chiếm Đảo.
     */
    public static int getIslandGateMap(int mapId) {
        switch (mapId) {
            case 254:
            case 261:
                return 25; // Đảo Cối Xay Gió (Làng Sirup / Cổng Đảo 1)
            case 255:
            case 262:
                return 33; // Đảo Thị Trấn Vỏ Sò (Baratie / Cổng Đảo 2)
            case 256:
            case 263:
                return 49; // Đảo Làng Khởi Đầu (Loguetown / Cổng Đảo 3)
            case 257:
            case 264:
                return 69; // Đảo Đấu Trường (Whiskey Peak / Cổng Đảo 4)
            case 258:
            case 265:
                return 83; // Đảo Mỏm Sinh Đôi (Đảo Drum / Cổng Đảo 5)
            default:
                return 1;
        }
    }

    /**
     * Lấy ID bản đồ Cổng Đảo / Khu vực đảo vừa vào (254..258) tương ứng với Đấu Trường (261..265) hoặc Cổng Đảo.
     */
    public static int getIslandEntranceMap(int mapId) {
        switch (mapId) {
            case 25:
            case 254:
            case 261:
                return 254; // Cổng Đảo Tiền bạc sơ cấp (Sirup)
            case 33:
            case 255:
            case 262:
                return 255; // Cổng Đảo Châu báu sơ cấp (Baratie)
            case 49:
            case 256:
            case 263:
                return 256; // Cổng Đảo Danh vọng (Loguetown)
            case 69:
            case 257:
            case 264:
                return 257; // Cổng Đảo Tiền bạc trung cấp (Whiskey Peak)
            case 83:
            case 258:
            case 265:
                return 258; // Cổng Đảo Châu báu trung cấp (Drum)
            default:
                return 254;
        }
    }

    /**
     * Lấy ID bản đồ Đấu Trường Chiếm Đảo (261..265) tương ứng với cổng hoặc đấu trường.
     */
    public static int getIslandArenaMap(int mapId) {
        switch (mapId) {
            case 25:
            case 254:
            case 261:
                return 261; // Đấu Trường Đảo Tiền bạc sơ cấp (Sirup)
            case 33:
            case 255:
            case 262:
                return 262; // Đấu Trường Đảo Châu báu sơ cấp (Baratie)
            case 49:
            case 256:
            case 263:
                return 263; // Đấu Trường Đảo Danh vọng (Loguetown)
            case 69:
            case 257:
            case 264:
                return 264; // Đấu Trường Đảo Tiền bạc trung cấp (Whiskey Peak)
            case 83:
            case 258:
            case 265:
                return 265; // Đấu Trường Đảo Châu báu trung cấp (Drum)
            default:
                return 261;
        }
    }

    /**
     * Ánh xạ mọi ID bản đồ trong game về đúng Map Làng của đảo/khu vực tương ứng.
     */
    public static int getVillageMapId(int mapId) {
        if (isMapLang(mapId)) {
            return mapId;
        }
        if (mapId >= 0 && mapId <= 7) return 1;    // Đảo Làng Cối Xay Gió (Foosha)
        if (mapId >= 8 && mapId <= 15) return 9;   // Đảo Thị Trấn Vỏ Sò (Shells Town)
        if (mapId >= 16 && mapId <= 23) return 17; // Đảo Thị Trấn Orange
        if (mapId >= 24 && mapId <= 31) return 25; // Đảo Làng Sirup
        if (mapId >= 32 && mapId <= 39) return 33; // Nhà Hàng Baratie
        if ((mapId >= 40 && mapId <= 47) || mapId == 62) return 41; // Đảo Làng Hạt Dẻ (Cocoyasi)
        if (mapId >= 48 && mapId <= 54) return 49; // Thị Trấn Khởi Đầu (Loguetown)
        if (mapId >= 63 && mapId <= 67) return 66; // Mỏm Sinh Đôi (Twin Cape)
        if (mapId >= 68 && mapId <= 74) return 69; // Thị Trấn Whiskay (Whiskey Peak)
        if (mapId >= 78 && mapId <= 81) return 79; // Đảo Little Garden
        if (mapId >= 82 && mapId <= 88) return 83; // Thị Trấn Horn (Đảo Drum)
        if (mapId >= 91 && mapId <= 103) return 93; // Thị Trấn Nanohano (Alabasta)
        if (mapId >= 106 && mapId <= 108) return 107; // Đảo Jaza (Jaya)
        if (mapId >= 109 && mapId <= 127) return 113; // Thị Trấn Thiên Sứ (Skypiea)
        if (mapId >= 189 && mapId <= 198) return 191; // Kinh Đô Nước (Water 7)
        if (mapId == 254 || mapId == 261) return 25;  // Đảo Chiếm Đảo 1 -> Đảo Cối Xay Gió / Sirup
        if (mapId == 255 || mapId == 262) return 33;  // Đảo Chiếm Đảo 2 -> Nhà Hàng Baratie
        if (mapId == 256 || mapId == 263) return 49;  // Đảo Chiếm Đảo 3 -> Thị Trấn Khởi Đầu
        if (mapId == 257 || mapId == 264) return 69;  // Đảo Chiếm Đảo 4 -> Thị Trấn Whiskay
        if (mapId == 258 || mapId == 265) return 83;  // Đảo Chiếm Đảo 5 -> Thị Trấn Horn
        if (mapId == 267) return 267; // Làng đỏ (Bảo Vệ Pháo Đài)
        if (mapId == 271) return 271; // Làng xanh (Bảo Vệ Pháo Đài)
        return 1; // Fallback mặc định
    }

    public void start_map() {
        this.running = true;
        map.MapManager.getInstance().registerActiveZone(this);
    }

    public void stop_map() {
        this.running = false;
        map.MapManager.getInstance().unregisterActiveZone(this);
        // [FIX] Dọn sạch mapSanTrum khi map bị dừng để tránh memory leak
        if (this.mapSanTrum != null) {
            if (this.mapSanTrum.mob != null && this.mobs != null) {
                this.mobs.remove(this.mapSanTrum.mob.index);
            }
            this.mapSanTrum.mob = null;
            this.mapSanTrum = null;
        }
    }

    public static boolean isRealPlayer(Player p) {
        return p != null && !p.isBot && !p.isDe && !(p instanceof model.DeTu)
                && !(p instanceof bot.mercenary.MercenaryBot) && p.conn != null && !p.isClosed;
    }

    public boolean hasRealPlayer() {
        for (int i = 0; i < this.players.size(); i++) {
            Player p = this.players.get(i);
            if (isRealPlayer(p)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSpecialActive() {
        if (this.map_dungeon != null) return true;
        if (this.mapSanTrum != null) return true;
        if (this.map_vp != null) return true;
        if (this.pvpBang != null) return true;
        if (this.pvpBangMapFight != null) return true;
        if (this.baoVePhaoDai != null) return true;
        if (this.template != null && (this.template.id == 119 || this.template.id == 1000)) return true;
        return false;
    }

    private long lastUpdate1s = System.currentTimeMillis();

    public void update() {
        if (!this.running) {
            return;
        }
        if (!tryBeginTick()) {
            return;
        }
        try {
            boolean hasRealP = this.hasRealPlayer();
            boolean isSpecial = this.isSpecialActive();

            if (hasRealP || isSpecial) {
                update_mob();
                update_player();
                update_item_map();

                if (this.template != null && this.template.id == 119) {
                    WantedDungeon.updateLobby(this);
                } else if (this.template != null && this.template.id == 1000) {
                    MapPvp.updateLobby(this);
                }
                if (this.map_dungeon != null) {
                    this.map_dungeon.update(this);
                } else if (this.map_vp != null) {
                    this.map_vp.update(this);
                }
                if (hasRealP && System.currentTimeMillis() - lastUpdate1s >= 1000) {
                    lastUpdate1s = System.currentTimeMillis();
                    update_npc_chat();
                }
            } else {
                update_mob_idle();
                update_item_map_idle();
                if (this.template != null && this.template.id == 119) {
                    WantedDungeon.updateLobby(this);
                } else if (this.template != null && this.template.id == 1000) {
                    MapPvp.updateLobby(this);
                }
                if (this.map_dungeon != null) {
                    this.map_dungeon.update(this);
                } else if (this.map_vp != null) {
                    this.map_vp.update(this);
                }
            }

            // [FIX] Auto stop map SanTrum khi timer hết và map trống hoàn toàn
            if (this.mapSanTrum != null && this.mapSanTrum.time < System.currentTimeMillis()
                    && this.players.isEmpty()) {
                this.stop_map();
                Zone.remove_map_plus(this);
            }
            if (this.map_dungeon != null && this.map_dungeon instanceof zabstracts.AbsDungeon) {
                zabstracts.AbsDungeon abs = (zabstracts.AbsDungeon) this.map_dungeon;
                if (abs.isFinished) {
                    this.map_dungeon = null;
                    this.stop_map();
                    Zone.remove_map_plus(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endTick();
        }
    }

    public void update_idle() {
        if (!this.running) {
            return;
        }
        if (!tryBeginTick()) {
            return;
        }
        try {
            update_mob_idle();
            update_item_map_idle();

            if (this.template != null && this.template.id == 119) {
                WantedDungeon.updateLobby(this);
            } else if (this.template != null && this.template.id == 1000) {
                MapPvp.updateLobby(this);
            }
            if (this.map_dungeon != null) {
                this.map_dungeon.update(this);
            } else if (this.map_vp != null) {
                this.map_vp.update(this);
            }
            if (this.mapSanTrum != null && this.mapSanTrum.time < System.currentTimeMillis()
                    && this.players.isEmpty()) {
                this.stop_map();
                Zone.remove_map_plus(this);
            }
            if (this.map_dungeon != null && this.map_dungeon instanceof zabstracts.AbsDungeon) {
                zabstracts.AbsDungeon abs = (zabstracts.AbsDungeon) this.map_dungeon;
                if (abs.isFinished) {
                    this.map_dungeon = null;
                    this.stop_map();
                    Zone.remove_map_plus(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endTick();
        }
    }

    private void update_mob_idle() {
        // 1. Quét quái trong list_mob (giữ nguyên status HP nếu sống, đếm hồi sinh nếu chết)
        if (this.list_mob != null) {
            for (int i = 0; i < list_mob.length; i++) {
                Mob mob = this.getMob(list_mob[i]);
                if (mob != null) {
                    if (mob.isdie || mob.hp <= 0) {
                        boolean isNonRespawn = (mob.mtemplate != null && (mob.mtemplate.mob_id == 132 || mob.mtemplate.mob_id == 133))
                                || (this.template != null && this.template.id == 179);
                        if (!isNonRespawn) {
                            if (mob.time_refresh <= 0) {
                                mob.time_refresh = System.currentTimeMillis() + Mob.TIME_RESPAWN * 1000L;
                            } else if (mob.time_refresh <= System.currentTimeMillis()) {
                                mob.isdie = false;
                                mob.hp = mob.hp_max;
                                mob.id_target = -1;
                                mob.time_refresh = 0;
                            }
                        }
                    } else {
                        // Quái sống: GIỮ NGUYÊN STATUS HP, reset target ảo
                        mob.id_target = -1;
                    }
                }
            }
        }

        // 2. Quét quái dynamic trong this.mobs (giữ nguyên status HP nếu sống, đếm hồi sinh nếu chết)
        if (this.mobs != null && !this.mobs.isEmpty()) {
            for (Mob mob : this.mobs.values()) {
                if (mob != null) {
                    if (mob.isdie || mob.hp <= 0) {
                        if (mob.time_refresh > 0 && mob.time_refresh <= System.currentTimeMillis()) {
                            mob.isdie = false;
                            mob.hp = mob.hp_max;
                            mob.id_target = -1;
                            mob.time_refresh = 0;
                        }
                    } else {
                        mob.id_target = -1;
                    }
                }
            }
        }

        // 3. SuperBoss checkRespawn & giữ nguyên HP khi sống
        if (SuperBossManager.ENTRYS != null) {
            for (int i = 0; i < SuperBossManager.ENTRYS.size(); i++) {
                zabstracts.AbsBoss bossTemp = SuperBossManager.ENTRYS.get(i);
                if (bossTemp != null && bossTemp.map != null && bossTemp.map.equals(this)) {
                    if (bossTemp.isdie) {
                        if (!bossTemp.isSieuTrum()) {
                            bossTemp.checkRespawn();
                        }
                    } else {
                        bossTemp.id_target = -1;
                    }
                }
            }
        }
    }

    private void update_item_map_idle() {
        if (this.list_it_map != null) {
            long now = System.currentTimeMillis();
            for (int i = 0; i < this.list_it_map.length; i++) {
                ItemMap it = this.list_it_map[i];
                if (it != null && it.time_exist < now) {
                    this.list_it_map[i] = null;
                }
            }
        }
    }

    private void update_npc_chat() {
        if (this.players.isEmpty() || this.template == null || this.template.npcs == null || this.template.npcs.isEmpty()) return;
        long now = System.currentTimeMillis();
        for (Npc npc : this.template.npcs) {
            if (npc == null) continue;
            String[] chats = npc.getChatTexts();
            if (chats == null || chats.length == 0 || (chats.length == 1 && chats[0].isEmpty())) continue;
            
            if (now - npc.lastChatTime >= 15000 + core.ZUtil.random(5000)) {
                npc.lastChatTime = now;
                String selectedChat = chats[core.ZUtil.random(chats.length)];
                try {
                    Message m = new Message(17);
                    m.writer().writeShort(npc.idmenu);
                    m.writer().writeByte(2);
                    m.writer().writeUTF(selectedChat);
                    send_msg_all_p(m, null, true);
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
//    private void update_map_little_garden() {
//        if (this.map_little_garden != null) {
//            for (int i = 0; i < this.map_little_garden.mobs.size(); i++) {
//                Mob mob = this.map_little_garden.mobs.get(i);
//                if (mob != null) {
//                    if (mob.isdie) {
//                        if (mob.time_refresh < System.currentTimeMillis()) {
//                            mob.isdie = false;
//                            mob.hp = mob.hp_max;
//                            mob.id_target = -1;
//                            //
//                            try {
//                                Message m_local = new Message(1);
//                                m_local.writer().writeByte(1);
//                                m_local.writer().writeShort(mob.index);
//                                m_local.writer().writeShort(mob.x);
//                                m_local.writer().writeShort(mob.y);
//                                this.send_msg_all_p(m_local, null, true);
//                                m_local.cleanup();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//            //
//            if (this.map_little_garden.is_finish
//                    || this.map_little_garden.time < System.currentTimeMillis()) {
//                // tong ket
//                int xp_receiv1 = 400;
//                int xp_receiv2 = 400;
//                int rb_receiv1 = 150;
//                int rb_receiv2 = 150;
//                if (this.map_little_garden.hp_1 <= 0) {
//                    xp_receiv2 = 1000;
//                    xp_receiv1 = 300;
//                    rb_receiv2 = 200;
//                    rb_receiv1 = 100;
//                } else if (this.map_little_garden.hp_2 <= 0) {
//                    xp_receiv1 = 1000;
//                    xp_receiv2 = 300;
//                    rb_receiv1 = 200;
//                    rb_receiv2 = 100;
//                }
//                //
//                this.map_little_garden.clan1.update_xp(xp_receiv1);
//                this.map_little_garden.clan1.update_ruby(rb_receiv1);
//                for (int i1 = 0; i1 < this.map_little_garden.clan1.members.size(); i1++) {
//                    Player p0 = Zone.get_player_by_name_allmap(
//                            this.map_little_garden.clan1.members.get(i1).name);
//                    if (p0 != null) {
//                        try {
//                            Clan.set_data(p0, false);
//                            Clan.send_money(p0, false);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                try {
//                    this.map_little_garden.clan1.chat_on_board(
//                            this.map_little_garden.clan1.members.get(0).id,
//                            this.map_little_garden.clan1.members.get(0).name,
//                            ("Phó bản khổng lồ với: " + this.map_little_garden.clan2.name
//                            + ": nhận được " + xp_receiv1 + " xp băng và " + rb_receiv1
//                            + " ruby băng"),
//                            -3);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //
//                this.map_little_garden.clan2.update_xp(xp_receiv2);
//                this.map_little_garden.clan2.update_ruby(rb_receiv2);
//                for (int i1 = 0; i1 < this.map_little_garden.clan2.members.size(); i1++) {
//                    Player p0 = Zone.get_player_by_name_allmap(
//                            this.map_little_garden.clan2.members.get(i1).name);
//                    if (p0 != null) {
//                        try {
//                            Clan.set_data(p0, false);
//                            Clan.send_money(p0, false);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                try {
//                    this.map_little_garden.clan2.chat_on_board(
//                            this.map_little_garden.clan2.members.get(0).id,
//                            this.map_little_garden.clan2.members.get(0).name,
//                            ("Phó bản khổng lồ với: " + this.map_little_garden.clan1.name
//                            + ": nhận được " + xp_receiv2 + " xp băng và " + rb_receiv2
//                            + " ruby băng"),
//                            -3);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                this.map_little_garden.clan1.map_create = null;
//                this.map_little_garden.clan2.map_create = null;
//                //
//                // try {
//                Vgo vgo = new Vgo();
//                vgo.map_go = Zone.get_map_by_id(33);
//                vgo.xnew = 710;
//                vgo.ynew = 320;
//                List<Player> playerList = new ArrayList<>();
//                for (int i = 0; i < players.size(); i++) {
//                    playerList.add(players.get(i));
//                }
//                playerList.forEach(l -> {
//                    try {
//                        if (this.map_little_garden.hp_1 <= 0) {
//                            if (l.clan.equals(this.map_little_garden.clan1)) {
//                                l.item.add_item_bag47(4, 579, 1);
//                            } else {
//                                l.item.add_item_bag47(4, 579, 2);
//                            }
//                        } else if (this.map_little_garden.hp_2 <= 0) {
//                            if (l.clan.equals(this.map_little_garden.clan1)) {
//                                l.item.add_item_bag47(4, 579, 2);
//                            } else {
//                                l.item.add_item_bag47(4, 579, 1);
//                            }
//                        }
//                        l.item.update_Inventory(-1, false);
//                        l.goto_map(vgo);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//                // } catch (IOException e) {
//                // e.printStackTrace();
//                // }
//                this.running = false;
//            }
//        }
//    }



    private void update_item_map() throws IOException {
        for (int i = 0; i < this.list_it_map.length; i++) {
            ItemMap it = this.list_it_map[i];
            // if (it != null) {
            // System.out.println((it.time_exist - System.currentTimeMillis()) / 1000);
            // }
            if (it != null && it.time_exist < System.currentTimeMillis()) {
                this.remove_obj(it.index, it.category);
                this.list_it_map[i] = null;
            }
            if (it != null && (it.time_exist - 10_000L) < System.currentTimeMillis()) {
                it.id_master = -1;
            }
        }
    }

    public void remove_obj(int index, int category) {
        try {
            Message m = new Message(13);
            m.writer().writeShort(index);
            m.writer().writeByte(category);
            send_msg_all_p(m, null, true);
            m.cleanup();
        }
        catch(IOException e) {
            
        }
    }

    private synchronized void update_player() throws IOException {
        // [FIX MEMORY] Dùng _tmp_ field tái sử dụng thay vì new ArrayList() mỗi 125ms
        _tmp_remove.clear();
        _tmp_remove2.clear();
        _tmp_remove3.clear();
        final List<Player> list_remove  = _tmp_remove;
        final List<Player> list_remove2 = _tmp_remove2;
        final List<Player> list_remove3 = _tmp_remove3;
        for (int i = 0; i < players.size(); i++) {
            Player p0 = players.get(i);
            if (p0.conn == null && !p0.isBot && !p0.isDe && !(p0 instanceof model.DeTu) && !(p0.detu != null && p0.detu.conn != null && p0.detu.conn.p == p0.detu)) {
                list_remove.add(p0);
                continue;
            }
            if (p0 instanceof model.DeTu) {
                model.DeTu dt = (model.DeTu) p0;
                if (dt.master == null || dt.master.conn == null || dt.master.isClosed || (dt.master.map != this && !dt.isdie) || dt.detuStatus == model.DeTu.STATUS_HOME || dt.detuStatus == model.DeTu.STATUS_FUSION || dt.master.isSpectator) {
                    list_remove.add(p0);
                    continue;
                }
            }
            if (p0 instanceof bot.mercenary.MercenaryBot) {
                bot.mercenary.MercenaryBot merc = (bot.mercenary.MercenaryBot) p0;
                Player owner = merc.getOwner();
                if (owner == null || owner.conn == null || owner.isClosed || (owner.map != this && !merc.isdie) || owner.isSpectator || merc.mercStatus == bot.mercenary.MercenaryBot.STATUS_HOME || bot.mercenary.MercenaryBot.isRestrictedPvpMap(this)) {
                    list_remove.add(p0);
                    continue;
                }
            }
            try {
                if (!(p0 instanceof model.DeTu && ((model.DeTu) p0).master != null)) {
                    p0.update();
                }
                if (p0.map_remove_type == 2) {
                    list_remove2.add(p0);
                } else if (p0.map_remove_type == 3) {
                    list_remove3.add(p0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        players.removeAll(list_remove);
        for (int i = 0; i < list_remove.size(); i++) {
            Player p0 = list_remove.get(i);
            if (p0 instanceof model.DeTu) {
                ((model.DeTu) p0).map = null;
            } else if (p0 instanceof bot.mercenary.MercenaryBot) {
                ((bot.mercenary.MercenaryBot) p0).map = null;
            }
            Message m = new Message(3);
            m.writer().writeShort(p0.index_map);
            m.writer().writeByte(0);
            this.send_msg_all_p(m, null, true);
            m.cleanup();
        }
        list_remove.clear();
        if (list_remove2.size() > 0) {
            Vgo vgo = new Vgo();
            list_remove2.forEach(l -> {
                try {
                    l.map_remove_type = 0;
                    l.isdie = false;
                    if (l.ability != null) {
                        l.hp = l.ability.get_hp_max(true);
                        l.mp = l.ability.get_mp_max(true);
                    }
                    if (l.pre_map_id > 0 && !Zone.map_cant_save_site(l.pre_map_id)) {
                        l.return_to_previous_map();
                    } else {
                        int safeMap = (l.id_map_save > 0) ? l.id_map_save : 1;
                        vgo.map_go = Zone.getMapByID(safeMap);
                        if (vgo.map_go != null && vgo.map_go.length > 0 && vgo.map_go[0] != null && vgo.map_go[0].template != null) {
                            for (int idxNpc = 0; idxNpc < vgo.map_go[0].template.npcs.size(); idxNpc++) {
                                Npc npc_temp = vgo.map_go[0].template.npcs.get(idxNpc);
                                if (npc_temp.namegt.equals("Bản đồ") || npc_temp.namegt.equals("Zosaku") || (npc_temp.name != null && npc_temp.name.contains("Zosaku"))) {
                                    vgo.xnew = npc_temp.x;
                                    if (npc_temp.y < 250) {
                                        vgo.ynew = (short) (npc_temp.y + 20);
                                    } else {
                                        vgo.ynew = (short) (npc_temp.y - 40);
                                    }
                                    break;
                                }
                            }
                            if (vgo.xnew == 0 || vgo.ynew == 0) {
                                vgo.xnew = (short) (vgo.map_go[0].template.maxW / 2);
                                vgo.ynew = (short) (vgo.map_go[0].template.maxH / 2);
                            }
                            l.goto_map(vgo);
                        }
                    }
                    l.timeEnterMap = System.currentTimeMillis() + 20_000;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        if (list_remove3.size() > 0) {
            Vgo vgo = new Vgo();
            list_remove3.forEach(l -> {
                try {
                    l.map_remove_type = 0;
                    if (l.clan != null && l.clan.map_create != null) {
                        vgo.map_go = new Zone[]{l.clan.map_create};
                        vgo.xnew = (short) (vgo.map_go[0].template.maxW / 2);
                        vgo.ynew = (short) (vgo.map_go[0].template.maxH / 2);
                        l.isdie = false;
                        l.hp = l.ability.get_hp_max(true);
                        l.mp = l.ability.get_mp_max(true);
                        l.type_pk = -1; // Tắt PK khi về map biển riêng của clan
                        l.goto_map(vgo);
                        //
                        l.isdie = false;
                        if (l.getService() != null) {
                            l.getService().update_PK(l, false);
                            l.getService().use_potion(0, l.ability.get_hp_max(true));
                            l.getService().use_potion(1, l.ability.get_mp_max(true));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void update_mob() {
        if (this.map_DaoKhoBau != null) {
            try {
                if (this.map_DaoKhoBau.mobs != null && !this.map_DaoKhoBau.mobs.isEmpty()) {
                    Mob mob = this.map_DaoKhoBau.mobs.get(0);
                    if (mob != null && mob.isdie) {
                        this.remove_obj(mob.index, 1);
                    }
                }
            } catch (Exception e) {
            }
        }
        if (BossPica.mob != null && !BossPica.mob.isdie) {
            if (BossPica.mob.id_target != -1) {
                try {
                    mob_fire(BossPica.mob, BossPica.mob.id_target);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        _tmp_mobs.clear();
        List<Mob> zListm = _tmp_mobs;
        if (this.list_mob != null) {
            for (int i = 0; i < list_mob.length; i++) {
                Mob mob = this.getMob(list_mob[i]);
                if (mob != null && !zListm.contains(mob)) {
                    zListm.add(mob);
                }
            }
        }
        if (this.mobs != null && !this.mobs.isEmpty()) {
            for (Mob mob : this.mobs.values()) {
                if (mob != null && !zListm.contains(mob)) {
                    zListm.add(mob);
                }
            }
        }
        if (this.mapSanTrum != null && this.mapSanTrum.mob != null && !zListm.contains(this.mapSanTrum.mob)) {
            zListm.add(this.mapSanTrum.mob);
        }
        if (this.can_PK && this.IsMapBaoVePhaoDai() && this.baoVePhaoDai != null) {
            if (this.baoVePhaoDai.truThuongADuoi != null && this.baoVePhaoDai.truThuongADuoi.map != null && this.baoVePhaoDai.truThuongADuoi.map.equals(this) && !zListm.contains(this.baoVePhaoDai.truThuongADuoi)) {
                zListm.add(this.baoVePhaoDai.truThuongADuoi);
            }
            if (this.baoVePhaoDai.truThuongATren != null && this.baoVePhaoDai.truThuongATren.map != null && this.baoVePhaoDai.truThuongATren.map.equals(this) && !zListm.contains(this.baoVePhaoDai.truThuongATren)) {
                zListm.add(this.baoVePhaoDai.truThuongATren);
            }
            if (this.baoVePhaoDai.truThuongBDuoi != null && this.baoVePhaoDai.truThuongBDuoi.map != null && this.baoVePhaoDai.truThuongBDuoi.map.equals(this) && !zListm.contains(this.baoVePhaoDai.truThuongBDuoi)) {
                zListm.add(this.baoVePhaoDai.truThuongBDuoi);
            }
            if (this.baoVePhaoDai.truThuongBTren != null && this.baoVePhaoDai.truThuongBTren.map != null && this.baoVePhaoDai.truThuongBTren.map.equals(this) && !zListm.contains(this.baoVePhaoDai.truThuongBTren)) {
                zListm.add(this.baoVePhaoDai.truThuongBTren);
            }
            if (this.baoVePhaoDai.truChinhA != null && this.baoVePhaoDai.truChinhA.map != null && this.baoVePhaoDai.truChinhA.map.equals(this) && !zListm.contains(this.baoVePhaoDai.truChinhA)) {
                zListm.add(this.baoVePhaoDai.truChinhA);
            }
            if (this.baoVePhaoDai.truChinhB != null && this.baoVePhaoDai.truChinhB.map != null && this.baoVePhaoDai.truChinhB.map.equals(this) && !zListm.contains(this.baoVePhaoDai.truChinhB)) {
                zListm.add(this.baoVePhaoDai.truChinhB);
            }
            if (this.baoVePhaoDai.bossDuongDuoi != null && this.baoVePhaoDai.bossDuongDuoi.map != null && this.baoVePhaoDai.bossDuongDuoi.map.equals(this) && !zListm.contains(this.baoVePhaoDai.bossDuongDuoi)) {
                zListm.add(this.baoVePhaoDai.bossDuongDuoi);
            }
            if (this.baoVePhaoDai.bossDuongGiua != null && this.baoVePhaoDai.bossDuongGiua.map != null && this.baoVePhaoDai.bossDuongGiua.map.equals(this) && !zListm.contains(this.baoVePhaoDai.bossDuongGiua)) {
                zListm.add(this.baoVePhaoDai.bossDuongGiua);
            }
            if (this.baoVePhaoDai.bossDuongTren != null && this.baoVePhaoDai.bossDuongTren.map != null && this.baoVePhaoDai.bossDuongTren.map.equals(this) && !zListm.contains(this.baoVePhaoDai.bossDuongTren)) {
                zListm.add(this.baoVePhaoDai.bossDuongTren);
            }
        }
        for (int i = 0; i < zListm.size(); i++) {
            Mob mob = zListm.get(i);
            if (mob != null) {
                try {
                    mob.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!mob.isdie && mob.hp > 0) {
                    if (!this.players.isEmpty()) {
                        // 1. Quét tìm mục tiêu tự động nếu quái đang rảnh (Aggro AI)
                        if (mob.id_target == -1) {
                            Player nearestPlayer = null;
                            double minDist = 140.0;
                            for (int j = 0; j < players.size(); j++) {
                                Player pCandidate = players.get(j);
                                if (pCandidate != null && !pCandidate.isdie && !pCandidate.isSpectator && !pCandidate.wait_change_map) {
                                    double dist = Math.hypot(mob.x - pCandidate.x, mob.y - pCandidate.y);
                                    if (dist <= minDist) {
                                        minDist = dist;
                                        nearestPlayer = pCandidate;
                                    }
                                }
                            }
                            if (nearestPlayer != null) {
                                mob.id_target = nearestPlayer.index_map;
                            }
                        }

                        // 2. Nếu đã có mục tiêu, xác thực và tấn công
                        if (mob.id_target != -1) {
                            Player targetP = this.get_player_by_id_inmap(mob.id_target);
                            if (targetP == null || targetP.isdie || targetP.isSpectator || targetP.wait_change_map || targetP.map != this || Math.hypot(mob.x - targetP.x, mob.y - targetP.y) > 220) {
                                mob.id_target = -1;
                                try {
                                    mob_non_focus(mob);
                                } catch (Exception ignored) {}
                            } else {
                                try {
                                    mob_fire(mob, mob.id_target);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        mob.id_target = -1;
                    }
                } else {
                    // 3. Xử lý quái chết & tự động hồi sinh theo chu kỳ
                    boolean isNonRespawn = (mob.mtemplate != null && (mob.mtemplate.mob_id == 132 || mob.mtemplate.mob_id == 133))
                            || (this.template != null && this.template.id == 179);

                    if (!isNonRespawn) {
                        if (mob.time_refresh <= 0) {
                            mob.time_refresh = System.currentTimeMillis() + Mob.TIME_RESPAWN * 1000L;
                        } else if (mob.time_refresh <= System.currentTimeMillis()) {
                            mob.isdie = false;
                            mob.hp = mob.hp_max;
                            mob.id_target = -1;
                            mob.time_refresh = 0;
                            if (!this.players.isEmpty()) {
                                try {
                                    Message m_local = new Message(1);
                                    m_local.writer().writeByte(1);
                                    m_local.writer().writeShort(mob.index);
                                    m_local.writer().writeShort(mob.x);
                                    m_local.writer().writeShort(mob.y);
                                    this.send_msg_all_p(m_local, null, true);
                                    m_local.cleanup();
                                    this.mob_non_focus(mob);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < SuperBossManager.ENTRYS.size(); i++) {
            zabstracts.AbsBoss bossTemp = SuperBossManager.ENTRYS.get(i);
            if (bossTemp != null && bossTemp.map != null && bossTemp.map.equals(this)) {
                if (!bossTemp.isdie) {
                    if (bossTemp.id_target != -1) {
                        try {
                            mob_fire(bossTemp, bossTemp.id_target);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (!bossTemp.isSieuTrum()) {
                        bossTemp.checkRespawn();
                    }
                }
            }
        }
        if (Zone.is_map_boss(this.template.id)) {
            List<Mob> get_list_Mob = MapBossInfo.get_list_mob(this);
            if (get_list_Mob != null) {
                for (int i = 0; i < get_list_Mob.size(); i++) {
                    Mob mob = get_list_Mob.get(i);
                    if (mob != null && !mob.isdie) {
                        if (mob.id_target != -1) {
                            try {
                                mob_fire(mob, mob.id_target);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        if (this.map_dungeon != null && this.map_dungeon.mobs != null) {
            boolean isKhongLo = (this.map_little_garden != null || this.map_dungeon instanceof MapTranChienKhongLo);
            _tmp_dungeon_rm.clear();
            _tmp_dungeon_gt.clear();
            List<Mob> list_remove = _tmp_dungeon_rm;
            List<Mob> get_list_Mob = _tmp_dungeon_gt;
            for (int i = 0; i < this.map_dungeon.mobs.size(); i++) {
                Mob mob = this.map_dungeon.mobs.get(i);
                if (mob != null && mob.map != null && mob.map.equals(this)) {
                    get_list_Mob.add(mob);
                }
            }
            for (int i = 0; i < get_list_Mob.size(); i++) {
                Mob mob = get_list_Mob.get(i);
                if (mob != null) {
                    if (mob.iMob != null) {
                        try {
                            mob.iMob.update(mob);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (!isKhongLo && mob.isdie && ((mob.time_refresh - (Mob.TIME_RESPAWN * 1000) / 2) < System.currentTimeMillis())) {
                        if (mob.mtemplate == null || (mob.mtemplate.mob_id != 132 && mob.mtemplate.mob_id != 133)) {
                            list_remove.add(mob);
                        }
                    }
                }
            }
            if (!list_remove.isEmpty()) {
                for (int i = 0; i < list_remove.size(); i++) {
                    Mob mobRm = list_remove.get(i);
                    if (mobRm != null) {
                        this.remove_obj(mobRm.index, 1);
                        this.mobs.remove(mobRm.index);
                    }
                }
                try {
                    get_list_Mob.removeAll(list_remove);
                } catch (Exception ignored) {}
                try {
                    if (this.map_dungeon.mobs != null) {
                        this.map_dungeon.mobs.removeAll(list_remove);
                    }
                } catch (Exception ignored) {}
            }
            for (int i = 0; i < get_list_Mob.size(); i++) {
                Mob mob = get_list_Mob.get(i);
                if (mob != null && !mob.isdie) {
                    if (mob.id_target != -1) {
                        try {
                            mob_fire(mob, mob.id_target);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public synchronized void mob_fire(Mob mob, int id_target) throws IOException {
        Player p0 = this.get_player_by_id_inmap(id_target);
        if (p0 == null) {
            for (int i = 0; i < players.size(); i++) {
                Player get = players.get(i);
                if (get.isDeOnl && get.detu != null && get.detu.index_map == id_target) {
                    p0 = get.detu;
                    p0.wait_change_map = false;
                    break;
                }
            }
            if (p0 == null || p0.isdie || p0.isSpectator) {
                mob.id_target = -1;
                return;
            }
        }
        if (p0.isSpectator) {
            mob.id_target = -1;
            return;
        }
        mob.attack(p0);
    }

    public void mob_non_focus(Mob mob) throws IOException {
        Message m2 = new Message(5);
        m2.writer().writeShort(mob.index);
        send_msg_all_p(m2, null, true);
        m2.cleanup();
    }

    public void die_player(Player p0, Player p) throws IOException {
        if (p0 == null || p0.isSpectator) return;
        model.ThanTrangConfig.onPlayerUnequipOrCancel(p0);
        p0.isdie = true;
        p0.update_die();
        //
        if (this.IsMapBaoVePhaoDai() || (p0.map != null && p0.map.IsMapBaoVePhaoDai())) {
            p0.time_hs_little_garden = System.currentTimeMillis() + 5_000L;
            if (p0.getService() != null) {
                p0.getService().send_time_cool_down(p0.time_hs_little_garden, "Hồi sinh", 3);
            }
            BaoVePhaoDai bvd = (this.baoVePhaoDai != null) ? this.baoVePhaoDai
                    : (p0.map != null ? p0.map.baoVePhaoDai
                    : (p0.clan != null ? p0.clan.baoVePhaoDai
                    : (p0.dungeon instanceof BaoVePhaoDai ? (BaoVePhaoDai) p0.dungeon : null)));
            if (bvd != null) {
                if (p != null && p.clan != null) {
                    bvd.UpdateDie(p.clan);
                } else if (p != null) {
                    boolean killerIsA = (p.clan != null && p.clan.equals(bvd.clanA)) || p.type_pk == 4;
                    bvd.UpdateDie(killerIsA ? bvd.clanA : bvd.clanB);
                } else {
                    boolean victimIsA = (p0.clan != null && p0.clan.equals(bvd.clanA)) || p0.type_pk == 4;
                    bvd.UpdateDie(victimIsA ? bvd.clanB : bvd.clanA);
                }
            }
        }
        // Phó bản Khổng Lồ (Little Garden / Map 81)
        if (p0.map != null && (p0.map.map_little_garden != null || (p0.map.template != null && p0.map.template.id == 81))) {
            p0.time_hs_little_garden = System.currentTimeMillis() + 10_000L;
            if (p0.getService() != null) {
                p0.getService().send_time_cool_down(p0.time_hs_little_garden, "Hồi sinh", 3);
            }
        }
        // Chiếm Đảo Bang Hội (Cổng Đảo 254..258 hoặc Chiến Trường 261..265)
        if (p0.map != null && p0.map.template != null && ((p0.map.template.id >= 254 && p0.map.template.id <= 258) || (p0.map.template.id >= 261 && p0.map.template.id <= 265))) {
            p0.time_hs_little_garden = System.currentTimeMillis() + 10_000L;
            if (p0.getService() != null) {
                p0.getService().send_time_cool_down(p0.time_hs_little_garden, "Hồi sinh", 3);
            }
        }
        // Trận Chiến Lớn / Đại Chiến Thế Giới (Map 272..275)
        if (p0.map != null && p0.map.template != null && map.zones.TranChienLon.isMapTranChienLon(p0.map.template.id)) {
            p0.time_hs_little_garden = System.currentTimeMillis() + 8_000L;
            if (p0.getService() != null) {
                p0.getService().send_time_cool_down(p0.time_hs_little_garden, "Hồi sinh", 3);
            }
        }
        // Đấu Trường Tự Do (Map 70, 71, 72, 74)
        if (p0.map != null && p0.map.template != null && (p0.map.template.id == 70 || p0.map.template.id == 71 || p0.map.template.id == 72 || p0.map.template.id == 74)) {
            p0.time_hs_little_garden = System.currentTimeMillis() + 5_000L;
            if (p0.getService() != null) {
                p0.getService().send_time_cool_down(p0.time_hs_little_garden, "Về Whiskey", 3);
            }
        }
        //
        Message m = new Message(7);
        m.writer().writeShort(p != null ? p.index_map : p0.index_map);
        m.writer().writeByte(0);
        m.writer().writeShort(p0.index_map);
        m.writer().writeByte(0);
        m.writer().writeShort(p != null ? p.pointPk : 0); // point pk
        send_msg_all_p(m, p0, true);
        m.cleanup();
        //
        p0.resetCountKichAn();
        if (p0.is_combo != null) {
            p0.is_combo = null;
            if (p0.getService() != null) {
                p0.getService().start_combo(0);
            }
        }
        //
        if (this.map_vp != null && this.map_vp.status_pvp == 3) {
            if (p0 != null && !p0.isDe && !(p0 instanceof model.DeTu) && !(p0 instanceof bot.mercenary.MercenaryBot)) {
                boolean isP1Die = (this.map_vp.player1 != null && (p0.equals(this.map_vp.player1) || p0.IDPlayer == this.map_vp.idP1));
                boolean isP2Die = (this.map_vp.player2 != null && (p0.equals(this.map_vp.player2) || p0.IDPlayer == this.map_vp.idP2));
                if (isP1Die || isP2Die) {
                this.map_vp.status_pvp = 91;
                this.map_vp.time_pvp = 3;
                Player pWin = null;
                Player pLose = null;
                if (isP1Die) {
                    this.map_vp.num_win_p2++;
                    pWin = this.map_vp.player2;
                    pLose = this.map_vp.player1;
                } else {
                    this.map_vp.num_win_p1++;
                    pWin = this.map_vp.player1;
                    pLose = this.map_vp.player2;
                }
                if (pWin != null && !pWin.isBot) {
                    Pvp.pvp_notice(pWin, 3); // Thắng
                }
                if (pLose != null && !pLose.isBot) {
                    Pvp.pvp_notice(pLose, 4); // Thua
                }
                if (this.map_vp.player1 != null && !this.map_vp.player1.isBot) {
                    Pvp.show_info(this.map_vp.player1, 3, this.map_vp.num_win_p1, this.map_vp.num_win_p2, 3);
                }
                if (this.map_vp.player2 != null && !this.map_vp.player2.isBot) {
                    Pvp.show_info(this.map_vp.player2, 3, this.map_vp.num_win_p2, this.map_vp.num_win_p1, 3);
                }
            }
        }
    }
}

    public static boolean isCountForZoneSlot(Player p) {
        if (p == null) return false;
        if (p.isDe || p instanceof model.DeTu) return false;
        if (p instanceof bot.mercenary.MercenaryBot) return false;
        return true;
    }

    public int getNumPlayerSlot() {
        int count = 0;
        for (Player p : this.players) {
            if (isCountForZoneSlot(p)) {
                count++;
            }
        }
        return count;
    }

    public void enter_map(Player p) {
        this.setRunning(true);
        synchronized (this) {
            players.add(p);
            if (isCountForZoneSlot(p)) {
                onlyPlayers.add(p);
            }
        }
        if (isRealPlayer(p)) {
            map.MapManager.getInstance().registerActiveZone(this);
        }
        if (p.name != null) {
            network.SessionManager.PLAYERS_BY_NAME.put(p.name, p);
        }
        if (p.IDPlayer != 0) {
            network.SessionManager.PLAYERS_MAP.put(p.IDPlayer, p);
        }
        network.SessionManager.PLAYERS_BY_INDEX.put((int) p.index_map, p);
        p.lastValidX = p.x;  // dùng tọa độ map mới, không reset về -1
        p.lastValidY = p.y;
        if (p != null && !p.isBot && !p.isDe && !(p instanceof bot.mercenary.MercenaryBot)) {
            if (p.ability != null) {
                p.ability.recalculatePlayerStats(p);
            }
            bot.mercenary.MercenaryManager.gI().onPlayerChangeMap(p, this);
            zinterfaces.iNpc.dispatchInitNpcsForMap(this);
            if (this.template != null && this.template.id >= 272 && this.template.id <= 275) {
                bot.botplayer.BotPlayerManager.dispatchBotsToWorldWar();
            }
            if (this.template != null && (this.template.id == 70 || this.template.id == 71 || this.template.id == 72 || this.template.id == 74)) {
                bot.BotDauTruongTuDo.adjustBotsInZone(this);
            }
        }
    }


    public void leave_map(Player p, int type) {
        if (p.dungeon != null && (p.dungeon.maps == null || !p.dungeon.maps.contains(this))) {
            try {
                p.dungeon.leave(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Tự động tháo cờ và xóa cooldown khi rời phó bản / hoạt động chiến đấu đặc biệt
        boolean isLeavingSpecialActivity = (this.map_vp != null
                || (this.template != null && (this.template.id == 1000 || this.template.id == 119))
                || (this.template != null && ((this.template.id >= 254 && this.template.id <= 258)
                || (this.template.id >= 261 && this.template.id <= 265)
                || (this.template.id >= 70 && this.template.id <= 74)
                || this.template.id == 81 || this.map_little_garden != null
                || this.template.id == 120 || this.template.id == 260 || this.template.id == 259
                || (this.template.id >= 178 && this.template.id <= 184)
                || (this.template.id >= 267 && this.template.id <= 271) || this.baoVePhaoDai != null || this.IsMapBaoVePhaoDai()
                || (this.template.id >= 272 && this.template.id <= 275)
                || this.mapSanTrum != null)));
        if (isLeavingSpecialActivity) {
            p.type_pk = -1;
            p.isBackTypePk = false;
            p.pvp_target = null;
            p.targetFight = null;
            try {
                p.getService().send_time_cool_down(System.currentTimeMillis(), "", 0);
            } catch (Exception ignored) {}
            List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(p);
            if (activeMercs != null) {
                for (bot.mercenary.MercenaryBot merc : activeMercs) {
                    if (merc != null) {
                        merc.type_pk = -1;
                    }
                }
            }
        }

        synchronized (this) {
            players.remove(p);
            if (isCountForZoneSlot(p)) {
                onlyPlayers.remove(p);
            }
            if (!this.hasRealPlayer() && !this.isSpecialActive()) {
                map.MapManager.getInstance().unregisterActiveZone(this);
            }
        }
        if (p.name != null) {
            network.SessionManager.PLAYERS_BY_NAME.remove(p.name, p);
        }
        network.SessionManager.PLAYERS_BY_INDEX.remove((int) p.index_map, p);
        p.is_combo = null;
        p.time_combo = 0;
        p.id_meet_in_map.clear();
        p.id_meet_in_map.add("" + p.index_map);
        //
        if (p.map_boss_info != null && Zone.is_map_boss(this.template.id)) {
            MapBossInfo.remove(p.map_boss_info);
            p.map_boss_info = null;
        }
        try {
            Message m = new Message(3);
            m.writer().writeShort(p.index_map);
            // 2: next map, 1: tele, 0: exit game
            m.writer().writeByte(type);
            for (int i = 0; i < players.size(); i++) {
                Player p0 = players.get(i);
                p0.addmsg(m);
                p0.id_meet_in_map.remove("" + p.index_map);
            }
            m.cleanup();
            //
            if (p.ship_pet != null && p.ship_pet.map == null) {
                m = new Message(3);
                m.writer().writeShort(p.ship_pet.index_map);
                m.writer().writeByte(type);
                for (int i = 0; i < players.size(); i++) {
                    Player p0 = players.get(i);
                    p0.addmsg(m);
                }
                m.cleanup();
            }
            if (p.botTruyNa != null && !p.isSpectator) {
                m = new Message(3);
                m.writer().writeShort(p.botTruyNa.p.index_map);
                m.writer().writeByte(type);
                for (int i = 0; i < players.size(); i++) {
                    Player p0 = players.get(i);
                    p0.addmsg(m);
                }
                m.cleanup();
            }
            if (p.detu != null) {
                m = new Message(3);
                m.writer().writeShort(p.detu.index_map);
                m.writer().writeByte(type);
                for (int i = 0; i < players.size(); i++) {
                    Player p0 = players.get(i);
                    p0.addmsg(m);
                }
                m.cleanup();
                players.remove(p.detu);
                if (p.detu.map != null && p.detu.map.equals(this)) {
                    p.detu.map = null;
                }
            }
            List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(p);
            if (activeMercs != null) {
                for (bot.mercenary.MercenaryBot merc : activeMercs) {
                    if (merc != null) {
                        Message m2 = new Message(2);
                        m2.writer().writeShort(merc.index_map);
                        m2.writer().writeByte(0);
                        p.addmsg(m2);
                        m2.cleanup();

                        Message m3 = new Message(3);
                        m3.writer().writeShort(merc.index_map);
                        m3.writer().writeByte(type);
                        for (int i = 0; i < players.size(); i++) {
                            Player p0 = players.get(i);
                            p0.addmsg(m3);
                        }
                        m3.cleanup();

                        if (p.id_meet_in_map != null) {
                            p.id_meet_in_map.remove("" + merc.index_map);
                        }
                        players.remove(merc);
                        if (merc.map != null && merc.map.equals(this)) {
                            merc.map = null;
                        }
                    }
                }
            }
            //
            if (p.trade_target != null) {
                Trade.end_trade_by_disconnect(p.trade_target, p, 0, "");
                p.fee_trade = 0;
                p.money_trade = 0;
                p.is_lock_trade = false;
                p.is_accept_trade = false;
                p.list_item_trade3 = null;
                p.list_item_trade47 = null;
                p.trade_target = null;
            }
            MyPet pet_select = p.get_pet();
            if (pet_select != null) {
                Message m22 = new Message(-80);
                m22.writer().writeByte(1);
                m22.writer().writeShort(-1);
                m22.writer().writeShort(p.index_map);
                send_msg_all_p(m22, null, true);
                m22.cleanup();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("err leave map " + this.template.id);
        }
        if (p != null && !p.isBot && !p.isDe && !(p instanceof bot.mercenary.MercenaryBot)) {
            if (this.template != null && (this.template.id == 70 || this.template.id == 71 || this.template.id == 72 || this.template.id == 74)) {
                bot.BotDauTruongTuDo.adjustBotsInZone(this);
            }
        }
    }

    public static Zone[] getMapByID(int id) {
        map.Map m = map.MapManager.getInstance().getMapByTemplateId(id);
        if (m != null) {
            return m.zones;
        }
        return null;
    }

    public static Player get_player_by_name_allmap(String name) {
        if (name == null) return null;
        return network.SessionManager.PLAYERS_BY_NAME.get(name);
    }

    public static List<Player> get_player_by_name_allmap2(String name) {
        List<Player> result = new ArrayList<>();
        Player p = get_player_by_name_allmap(name);
        if (p != null) {
            result.add(p);
        }
        return result;
    }

    public void send_move(Player p, Message m) throws IOException {
        if (!p.isdie) {
            short newX = m.reader().readShort();
            short newY = m.reader().readShort();
            if (p.isCantMove()) {
                if (p.lastValidX >= 0 && p.lastValidY >= 0) {
                    p.x = p.lastValidX;
                    p.y = p.lastValidY;
                }
                this.service.move(p, (byte) 0, p.index_map, p.x, p.y, true);
                return;
            }
            if (p.lastValidX < 0 || p.lastValidY < 0) {
                p.lastValidX = p.x;
                p.lastValidY = p.y;
            }
            int minX = -100;
            int maxX = p.map.template.maxW > 0 ? p.map.template.maxW : 3000;
            int minY = -100;
            int maxY = p.map.template.maxH > 0 ? p.map.template.maxH : 1500;
            if (p.map.template.data != null && p.map.template.data.length > 0
                    && p.map.template.data[0] != null && p.map.template.data[0].length >= 2) {
                int tileW = p.map.template.data[0][0] & 0xFF;
                int tileH = p.map.template.data[0][1] & 0xFF;
                if (tileW > 0) maxX = Math.max(maxX, tileW * 24);
                if (tileH > 0) maxY = Math.max(maxY, tileH * 24);
            }
            // Thêm vùng đệm linh hoạt cho mọi client zoomLevel (Java x1, Android x2, PC x4) và kích thước icon/character lớn
            maxX += 150;
            maxY += 150;
            if (p.map.map_vp != null) {
                minX = 0;
                maxX = 1150;
                minY = 150;
                maxY = 360;
            }
            if (newX < minX || newX > maxX || newY < minY || newY > maxY) {
                p.x = (short) Math.max(minX, Math.min(maxX, (int) (p.lastValidX >= 0 ? p.lastValidX : p.x)));
                p.y = (short) Math.max(minY, Math.min(maxY, (int) (p.lastValidY >= 0 ? p.lastValidY : p.y)));
                p.lastValidX = p.x;
                p.lastValidY = p.y;
                p.map.service.move(p, (byte) 0, p.index_map, p.x, p.y);
                p.map.service.move(p, (byte) 0, p.index_map, p.x, p.y, false);
                return;
            }
            long now = System.currentTimeMillis();
            long timeDelta = now - p.lastMovePacketTime;
            if (timeDelta <= 0) timeDelta = 1;
            int playerSpeed = 150;
            double maxDistAllowed = Math.max(350.0, ((double) (playerSpeed + 150) / 1000.0) * timeDelta * 2.5);
            if (p.lastMovePacketTime > 0 && p.lastValidX >= 0 && p.lastValidY >= 0 && (now - p.lastMovePacketTime < 5000)) {
                double dist = Math.hypot(newX - p.lastValidX, newY - p.lastValidY);
                if (dist > maxDistAllowed) {
                    this.service.move(p, (byte) 0, p.index_map, p.lastValidX, p.lastValidY, true);
                    return;
                }
            }
            p.lastMovePacketTime = now;
            p.x = newX;
            p.y = newY;
            p.lastValidX = newX;
            p.lastValidY = newY;

            // 1. Kiem tra xem nguoi choi da di chuyen xa khoi vi tri xuat hien (spawn) va Vgo xuat hien chua
            if (!p.hasMovedFromSpawn && p.spawnX != -1 && p.spawnY != -1) {
                if (Math.abs(p.x - p.spawnX) >= 20 || Math.abs(p.y - p.spawnY) >= 20) {
                    p.hasMovedFromSpawn = true;
                }
            }

            // 2. Kiem tra chuyen map truoc khi broadcast move toi map cu de tranh tao bong ma (ghost player) o portal
            boolean canChangeMap = (p.time_change_map <= System.currentTimeMillis());
            boolean isInPvPMatch = (this.map_vp != null || (this.map_dungeon != null && this.map_dungeon instanceof MapPvp) || (p.dungeon != null && p.dungeon instanceof MapPvp));
            if (canChangeMap && !isInPvPMatch && this.template != null && this.template.vgos != null) {
                if ((Zone.is_map_dungeon(this.template.id) || this.map_dungeon != null) && p.dungeon != null && !(p.dungeon instanceof MapPvp)) {
                    int num_mob = 0;
                    if (p.dungeon.mobs != null) {
                        for (int i = 0; i < p.dungeon.mobs.size(); i++) {
                            Mob mob_dungeon = p.dungeon.mobs.get(i);
                            if (mob_dungeon != null && mob_dungeon.map != null && mob_dungeon.map.equals(this) && !mob_dungeon.isdie) {
                                num_mob++;
                                Message mmove = new Message(1);
                                mmove.writer().writeByte(1);
                                mmove.writer().writeShort(mob_dungeon.index);
                                mmove.writer().writeShort(mob_dungeon.x);
                                mmove.writer().writeShort(mob_dungeon.y);
                                send_msg_all_p(mmove, p, true);
                                mmove.cleanup();
                            }
                        }
                    }
                    if (num_mob > 0) {
                        return;
                    }
                }
                for (Vgo vgo : this.template.vgos) {
                    if (vgo != null && Math.abs(vgo.xold - p.x) < 70 && Math.abs(vgo.yold - p.y) < 70) {
                        // Neu Vgo o ngay canh diem spawn, nguoi choi phai di chuyen ra khoi diem spawn truoc khi co the quay lai trigger
                        boolean isSpawnVgo = (p.spawnX != -1 && p.spawnY != -1
                                && Math.abs(vgo.xold - p.spawnX) < 40 && Math.abs(vgo.yold - p.spawnY) < 40);
                        if (isSpawnVgo && !p.hasMovedFromSpawn) {
                            continue;
                        }
                        if (vgo.map_go == null || vgo.map_go.length == 0 || vgo.map_go[0] == null) {
                            vgo.map_go = Zone.getMapByID(vgo.id_map_go);
                        }
                        if (vgo.map_go != null && vgo.map_go.length > 0 && vgo.map_go[0] != null && vgo.map_go[0].template != null) {
                            if (vgo.map_go[0].template.id == 179 && p.time_hs_little_garden > System.currentTimeMillis()) {
                                long waitSec = (p.time_hs_little_garden - System.currentTimeMillis()) / 1000 + 1;
                                p.getService().send_box_ThongBao_OK("Đang trong thời gian hồi sinh (" + waitSec + "s), chưa thể vào Sảnh chiến!");
                                p.time_change_map = System.currentTimeMillis() + 2000L;
                                break;
                            }
                        }
                        p.ischangemap = false;
                        p.time_change_map = System.currentTimeMillis() + 1500L;
                        p.goto_map(vgo);
                        return;
                    }
                }
            }

            if (p instanceof model.DeTu) {
                model.DeTu dt = (model.DeTu) p;
                if (dt.master != null && dt.master.map == this && dt.master.isDe) {
                    if (Math.hypot(dt.master.x - dt.x, dt.master.y - dt.y) > 100) {
                        dt.master.x = dt.x;
                        dt.master.y = dt.y;
                        this.service.move((byte) 0, dt.master.index_map, dt.master.x, dt.master.y);
                    }
                }
            }
            //
            if (!Zone.is_map_dont_show_other_info(this.template.id)) {
                this.service.move(p, (byte) 0, p.index_map, p.x, p.y, false);
                //
                if (p.ship_pet != null && p.ship_pet.map != null && p.ship_pet.map.equals(p.map)) {
                    if (p.ship_pet.time < System.currentTimeMillis()) {
                        p.ship_pet.time = System.currentTimeMillis() + 1800L;
                        p.ship_pet.x = p.x;
                        p.ship_pet.y = p.y;
                    }
                    //
                    this.service.move(p, (byte) 0, p.ship_pet.index_map, p.ship_pet.x, p.ship_pet.y);
                }
            }
            // mob
            if (this.list_mob != null) {
                for (int i = 0; i < list_mob.length; i++) {
                    Mob mob = this.getMob(Integer.valueOf(list_mob[i]));
                    if (mob != null && !mob.isdie && Math.abs(mob.x - p.x) < 140
                            && Math.abs(mob.y - p.y) < 140 && mob.id_target == -1) {
                        mob.id_target = p.index_map;
                    }
                }
            }
            if (this.map_dungeon != null && this.map_dungeon.mobs != null) {
                for (int i = 0; i < this.map_dungeon.mobs.size(); i++) {
                    Mob mob = this.map_dungeon.mobs.get(i);
                    if (mob != null && !mob.isdie && mob.map != null && mob.map.equals(this)
                            && Math.abs(mob.x - p.x) < 140 && Math.abs(mob.y - p.y) < 140
                            && mob.id_target == -1) {
                        mob.id_target = p.index_map;
                    }
                }
            }

            // boss
            for (int i = 0; i < SuperBossManager.ENTRYS.size(); i++) {
                zabstracts.AbsBoss temp = SuperBossManager.ENTRYS.get(i);
                if (temp != null && !temp.isdie && temp.map != null && temp.map.equals(this)
                        && Math.abs(temp.x - p.x) < 140 && Math.abs(temp.y - p.y) < 140
                        && temp.id_target == -1) {
                    temp.id_target = p.index_map;
                }
            }
            if (p.botTruyNa != null && !p.isSpectator) {
                p.botTruyNa.p.x = p.x;
                p.botTruyNa.p.y = p.y;
                this.service.move((byte) 0, p.botTruyNa.p.index_map, p.botTruyNa.p.x + ZUtil.random(-80, 80), p.botTruyNa.p.y + ZUtil.random(-40, 40));
            }
            if (p.isDeOnl && p.detu != null && !p.isSpectator) {
                this.service.move((byte) 0, p.detu.index_map, p.x + ZUtil.random(-80, 80), p.y + ZUtil.random(-40, 40));
            }
        }
    }

    private static boolean is_map_dont_show_other_info(int id) {
        return id == 64;
    }

    public void update_num_player_in_map(Player p) throws IOException {
        Message m = new Message(-70);
        m.writer().writeByte((byte) p.map.players.size());
        m.writer().writeByte(15);
        p.addmsg(m);
        m.cleanup();
    }

    public synchronized void use_skill(Player p, Message m2) throws IOException {
        if (p == null || p.isCantSkill()) {
            if (p != null && p.getService() != null) {
                //p.getService().send_box_ThongBao_OK("Bạn đang bị choáng hoặc không thể sử dụng kỹ năng!");
            }
            return;
        }
        if (Zone.isWaitingOrUnstartedMatch(this)) {
            p.getService().send_box_ThongBao_OK("Không thể sử dụng kỹ năng tại phòng chờ!");
            return;
        }
        short idSkill = m2.reader().readShort();
        byte CatBeFire = m2.reader().readByte();
        byte size_target = m2.reader().readByte();
        // System.out.println(idSkill);
        // System.out.println(CatBeFire);
        // System.out.println(size_target);
        if (!p.isdie && size_target > 0) {
            Skill_info sk_temp = p.get_skill_temp(idSkill);
            if (sk_temp == null || sk_temp.temp == null || (sk_temp.temp.typeSkill != 1 && sk_temp.temp.typeSkill != 4)) {
                return;
            }
            if (!p.isSkillReady(sk_temp.temp.ID)) {
                return;
            }
            if ((p.mp - sk_temp.temp.manaLost) < 0) {
                return;
            }
            p.limitTime = System.currentTimeMillis() + p.calculateSkillCooldown(sk_temp.temp);
            p.applySkillCooldown(sk_temp.temp.ID, sk_temp.temp);

            int hp_ = p.ability.get_hp_atk_absorb(true);
            int mp_ = p.ability.get_mp_atk_absorb(true);

            if (hp_ > 0) {
                p.getService().use_potion(0, hp_);
            }
            if (mp_ > 0) {
                p.getService().use_potion(1, mp_);
            }

            p.mp -= sk_temp.temp.manaLost;
            Sudo mySudo = Sudo.getSuDoByName(p.name);
            if (mySudo != null) {
                mySudo.exp += ZUtil.random(0, 3);
                if (mySudo.exp >= Level.ENTRYS[mySudo.lvExp - 1].exp) {
                    mySudo.exp -= Level.ENTRYS[mySudo.lvExp - 1].exp;
                    mySudo.lvExp++;
                }
                if (mySudo.lvExp > 20) {
                    mySudo.lvExp = 20;
                }
            }
            long dame = p.ability.get_dame(true);
            if (dame <= 0) {
                dame = Math.max(100L, (long) p.level * 50L);
            }
            EffTemplate eff = p.get_eff(5); // combo
            if (eff != null) {
                dame *= 2;
            }
            eff = p.get_eff(18); // skill boc pha
            if (eff != null) {
                dame = (dame * eff.param) / 100;
            }
            if (dame > 2 && p.get_eff(21) != null) { // zoombie
                dame /= 2;
            }
            if (sk_temp.temp.ID == 2057 || sk_temp.temp.ID == 2058) { // buff trai bong toi
                dame = (dame * 12) / 10;
            }
            if ((p.map.template.specMap == 4 && sk_temp.temp.ID != 3)
                    || (p.map.template.specMap != 4 && sk_temp.temp.ID == 3)) {// skill bien chi
                // dung tren bien
                dame = 0;
            }
            if (p.clan != null && p.clan.map_create != null && p.clan.map_create.map_thuLinhBienKhoi != null) {

                if (p.clan.map_create.map_thuLinhBienKhoi.time < System.currentTimeMillis()) {
                    p.clan.map_create.map_thuLinhBienKhoi.buff = -1;
                }
                if (p.clan.map_create.map_thuLinhBienKhoi.buff == 1) {
                    dame = (dame * 15) / 10;
                }
            }
            eff = p.get_eff(23);
            if (eff != null) {
                dame /= 2;
            }
            // Kích ẩn tích lũy khi tấn công (Đánh là choáng 16 hits / Thanh lọc 8 hits)
            p.accumulateOffensiveKichAn(getService());
            //
            if (sk_temp.temp.nTarget > 0 && sk_temp.temp.nTarget < size_target) {
                size_target = sk_temp.temp.nTarget;
            }

            // skill kaido thoi trang
            for (int i12 = 0; i12 < p.fashion.size(); i12++) {
                if (p.fashion.get(i12).id == 122 && p.fashion.get(i12).is_use) {
                    eff = p.get_eff(23); // skill kaido thoi trang
                    if (eff == null && 8 > ZUtil.random(120)) {
                        p.add_new_eff(23, 0, 8000);
                        //
                        //p.getService().addEffect(p.index_map, (short) 103, 8000, (byte) 0, (byte) 20);
                    }
                    break;
                }
            }

            Player[] p_target = new Player[size_target];
            Mob[] mob_target = new Mob[size_target];
            Ship_pet spet = null;
            for (int i = 0; i < size_target; i++) {
                int id_target = m2.reader().readShort();
                switch (CatBeFire) {
                    case 0: {
                        p_target[i] = this.get_player_by_id_inmap(id_target);
                        if (i == 0 && p_target[i] == null) {
                            spet = Ship_pet.get_pet(id_target);
                        }
                        break;
                    }
                    case 1: {
                        mob_target[i] = this.getMob(id_target);
                        if (mob_target[i] == null && Zone.is_map_boss(this.template.id)
                                && p.map_boss_info != null) {
                            mob_target[i] = MapBossInfo.get_mob(p, id_target);
                        }
                        if (mob_target[i] == null && this.map_dungeon != null) {
                            mob_target[i] = this.map_dungeon.get_mob(p, id_target);
                        }
                        if (mob_target[i] == null && this.template.id == 81
                                && this.map_little_garden != null) {
                            mob_target[i] = this.get_mobs(id_target, 0);
                        }
                        if (mob_target[i] == null && this.map_dungeon != null) {
                            remove_obj(id_target, 1);
                        }
                        if (mob_target[i] == null && this.mapSanTrum != null) {
                            if (p.map != null && p.map.mapSanTrum != null && p.map.mapSanTrum.mob != null && p.map.mapSanTrum.mob.index == id_target) {
                                mob_target[i] = p.map.mapSanTrum.mob;
                            }
                            if (mob_target[i] == null) {
                                remove_obj(id_target, 1);
                            }
                        }
                        if (mob_target[i] == null && p.clan != null && p.clan.mob1 != null) {
                            for (int i2 = 0; i2 < p.clan.mob1.length; i2++) {
                                if (p.clan.mob1[i2] != null && p.clan.mob1[i2].index == id_target) {
                                    mob_target[i] = p.clan.mob1[i2];
                                    break;
                                }
                            }
                        }

                        if (mob_target[i] == null && id_target < 0) {
                            remove_obj(id_target, 1);
                        }
                        if (mob_target[i] == null && BossTheGioi.mob != null && BossTheGioi.mob.map != null && BossTheGioi.mob.map.equals(p.map) && !BossTheGioi.mob.isdie) {
                            mob_target[i] = BossTheGioi.mob;
                        }
                        if (mob_target[i] == null && event.EventManager.isActive(9) && this.map_DaoKhoBau != null && this.map_DaoKhoBau.mobs != null && !this.map_DaoKhoBau.mobs.isEmpty()) {
                            mob_target[i] = this.map_DaoKhoBau.mobs.get(0);
                        }
                        if (mob_target[i] == null && event.EventManager.isActive(3) && mob_target[i] == null && event.eboss.LucciGioTo.mob != null && event.eboss.LucciGioTo.mob.map != null && event.eboss.LucciGioTo.mob.map.equals(p.map) && !event.eboss.LucciGioTo.mob.isdie) {
                            mob_target[i] = event.eboss.LucciGioTo.mob;
                        }
                        if (mob_target[i] == null && !BossPica.mob.isdie && id_target == BossPica.mob.index) {
                            mob_target[i] = BossPica.mob;
                        }
                        break;
                    }
                }
            }
            int maxSkillRange = (sk_temp.temp != null && sk_temp.temp.range > 0) ? Math.max((int) sk_temp.temp.range + 800, 1500) : 1500;
            for (int idx = 0; idx < size_target; idx++) {
                if (p_target[idx] != null && Math.hypot(p.x - p_target[idx].x, p.y - p_target[idx].y) > maxSkillRange) {
                    p_target[idx] = null;
                }
                if (mob_target[idx] != null && Math.hypot(p.x - mob_target[idx].x, p.y - mob_target[idx].y) > maxSkillRange) {
                    mob_target[idx] = null;
                }
            }
            long[] exp_up = null;
            switch (CatBeFire) {
                case 0: {
                    eff = p.get_eff(12);
                    if (eff != null && p_target[0] != null) { // skill buff zoro
                        p.getService().send_eff_sword_splash(p_target[0].index_map);
                    }
                    if (p_target.length > 0 && p_target[0] == null && spet != null) {
                        atk_ship_pet(spet, p, idSkill);
                    } else {
                        Fire_Player(p_target, p, idSkill, dame);
                    }
                    break;
                }
                case 1: {
                    if (mob_target[0] != null) {
                        eff = p.get_eff(12);
                        if (eff != null) { // skill buff zoro
                            p.getService().send_eff_sword_splash(mob_target[0].index);
                        }
                    }

                    exp_up = Fire_Monster(mob_target, p, idSkill, dame);
                    break;
                }
            }
            if (exp_up != null) { // update exp
//                System.out.println(exp_up[0]);
                if (exp_up[0] > 0) {
                    exp_up[0] = (exp_up[0] * (1000 + p.ability.get_xp_more()
                            + ((p.clan != null && p.clan.check_buff(1)) ? 500 : 0))) / 1000;

                    p.update_exp(exp_up[0], true);
                    if (Zone.is_map_luyentap(this.template.id)) {
                        p.updateHk(exp_up[0]);
                    }
                    if (p instanceof model.DeTu) {
                        model.DeTu dt = (model.DeTu) p;
                        if (dt.master != null) {
                            long masterExp = exp_up[0] / 2;
                            if (masterExp > 0) {
                                dt.master.update_exp(masterExp, true);
                            }
                        }
                    }
                }
                if (exp_up[1] > 0) {
                    exp_up[1] = (exp_up[1] * (1000l + p.ability.get_xp_skill_more())) / 1000l;
                    p.updateExpSkill(idSkill, exp_up[1]);
                    if ((this.template != null && this.template.id >= 272 && this.template.id <= 275) || is_map_luyentap(this.template.id)) {
                        p.updateExpFactionSkill(exp_up[1]);
                    }
                }
            }
        }
    }

    private void atk_ship_pet(Ship_pet spet, Player p, short idSkill) throws IOException {
        if (Zone.isMapLang(this.template.id) || spet.main_ship.index_map == p.index_map
                || !(p.typePirate == 0 || p.typePirate == 2)
                || (p.typePirate == 0 && spet.main_ship.typePirate == 0)) {
            return;
        }
        Skill_info sk_temp = p.get_skill_temp(idSkill);
        if (sk_temp != null) {
            Message m = new Message(100);
            m.writer().writeShort(p.index_map);
            m.writer().writeByte(0);
            m.writer().writeInt(p.hp);
            m.writer().writeInt(p.mp);
            m.writer().writeShort(sk_temp.get_eff_skills()[0]);
            m.writer().writeByte(1);
            //
            m.writer().writeShort(spet.index_map);
            m.writer().writeByte(0);
            int dame_ship_pet = 50;
            if (spet.main_ship.typePirate == 2) {
                dame_ship_pet = 100;
            }
            m.writer().writeInt(dame_ship_pet);
            //
            spet.hp -= dame_ship_pet;
            if (spet.hp <= 0) {
                spet.hp = 0;
                spet.main_ship.ship_pet = null;
                Ship_pet.remove(spet);
                try {
                    remove_obj(spet.index_map, 0);
                } catch (Exception e) {
                }
                //
                p.ship_pet = new Ship_pet();
                short index_map_new = IDManager.takeID(IDManager.SHIP_PET);
                if(index_map_new != -1) {
                    p.ship_pet.index_map = index_map_new;
                    p.id_ship_packet = spet.main_ship.id_ship_packet;
                    p.ship_pet.main_ship = p;
                    p.ship_pet.map = p.map;
                    p.ship_pet.name = "HÌÊng " + p.name;
                    p.ship_pet.x = spet.x;
                    p.ship_pet.y = spet.y;
                    p.ship_pet.hp_max = 2000;
                    p.ship_pet.hp = p.ship_pet.hp_max;
                    p.ship_pet.time_start = spet.time_start;
                    p.ship_pet.mainBaoVe = "";
                    Ship_pet.add(p.ship_pet);
                    //
                    Message m_local = new Message(1);
                    m_local.writer().writeByte(0);
                    m_local.writer().writeShort(p.ship_pet.index_map);
                    m_local.writer().writeShort(p.ship_pet.x);
                    m_local.writer().writeShort(p.ship_pet.y);
                    for (int j = 0; j < p.map.players.size(); j++) {
                        Player p0 = p.map.players.get(j);
                        p0.addmsg(m_local);
                    }
                    m_local.cleanup();
                }
            }
            //
            m.writer().writeInt(0); // dame plus
            m.writer().writeInt(spet.hp);
            //
            m.writer().writeByte(0);
            send_msg_all_p(m, p, true);
            m.cleanup();
        }
    }

    public void Fire_Player(Player[] list_target, Player p, int idSkill, long dame) throws IOException {
        if (p != null) {
            p.Fire_Player(list_target, p, idSkill, dame);
        }
    }

    public void update_hp_mp_eff(Player p, Mob mob, int type, int dame) throws IOException {
        Message m = new Message(55);
        if (mob != null) {
            m.writer().writeShort(mob.index);
            m.writer().writeByte(1);
            m.writer().writeByte(type);
            m.writer().writeInt(mob.hp_max);
            m.writer().writeInt(mob.hp);
            m.writer().writeInt(dame);
            m.writer().writeInt(mob.hp_max);
            m.writer().writeInt(mob.hp);
            m.writer().writeInt(0);
        } else if (p != null) {
            m.writer().writeShort(p.index_map);
            m.writer().writeByte(0);
            m.writer().writeByte(1);
            m.writer().writeInt(p.ability.get_hp_max(true));
            m.writer().writeInt(p.hp);
            m.writer().writeInt(dame);
            m.writer().writeInt(p.ability.get_mp_max(true));
            m.writer().writeInt(p.mp);
            m.writer().writeInt(0);
        }
        send_msg_all_p(m, p, true);
        m.cleanup();
    }

    public long[] Fire_Monster(Mob[] list_target, Player p, int idSkill, long dame)
            throws IOException {
        if (p != null) {
            return p.Fire_Monster(list_target, p, idSkill, dame);
        }
        return new long[]{0, 0};
    }

    public void die_mob(Mob targetM) throws IOException {
        Message m = new Message(7);
        m.writer().writeShort(targetM.index);
        m.writer().writeByte(1);
        m.writer().writeShort(targetM.index);
        m.writer().writeByte(1);
        m.writer().writeShort(0); // point pk
        send_msg_all_p(m, null, true);
        m.cleanup();

        if (targetM.isSieuTrum || targetM.boss_inf != null || targetM.is_boss) {
            remove_obj(targetM.index, 1);
        }
    }

    public void send_dame_msg(Player p, short typeEffSkill, List<Dame_Msg> list)
            throws IOException {
        Message m = new Message(100);
        m.writer().writeShort(p.index_map);
        m.writer().writeByte(0);
        m.writer().writeInt(p.hp);
        m.writer().writeInt(p.mp);
        // System.out.println(Zone.id_eff);
        // typeEffSkill = (short) Zone.id_eff;
        m.writer().writeShort(typeEffSkill);
        m.writer().writeByte(list.size());
        for (int j = 0; j < list.size(); j++) {
            Dame_Msg temp = list.get(j);
            if (temp.targetM != null) {
                m.writer().writeShort(temp.targetM.index);
                m.writer().writeByte(1);
                m.writer().writeInt((int) temp.dameP);
                m.writer().writeInt((int) temp.dameM); // dame plus
                m.writer().writeInt(temp.targetM.hp);
                m.writer().writeByte(temp.data.size());
                for (int i = 0; i < temp.data.size(); i++) {
                    m.writer().writeShort(temp.data.get(i).type);
                    m.writer().writeShort(temp.data.get(i).hp);
                    m.writer().writeShort(temp.data.get(i).time);
                }
            } else {
                m.writer().writeShort(temp.targetP.index_map);
                m.writer().writeByte(0);
                m.writer().writeInt((int) temp.dameP);
                m.writer().writeInt((int) temp.dameM); // dame plus
                m.writer().writeInt(temp.targetP.hp);
                //
                m.writer().writeByte(temp.data.size());
                for (int i = 0; i < temp.data.size(); i++) {
                    m.writer().writeShort(temp.data.get(i).type);
                    m.writer().writeShort(temp.data.get(i).hp);
                    m.writer().writeShort(temp.data.get(i).time);
                }
            }
        }
        send_msg_all_p(m, p, true);
        m.cleanup();
    }
    public void list_mob_custom_add(Mob mob) {
        if (this.list_mob == null) {
            this.list_mob = new int[0];
        }
        int[] newList = new int[this.list_mob.length + 1];
        System.arraycopy(this.list_mob, 0, newList, 0, this.list_mob.length);
        newList[this.list_mob.length] = mob.index;
        this.list_mob = newList;
        Mob.ENTRYS.put(mob.index, mob);
        this.mobs.put(mob.index, mob);
    }

    /**
     * Kiểm tra xem khu vực này hiện có bất kỳ Boss nào đang sống (!isdie && hp > 0) hay không.
     */
    public boolean hasActiveBoss() {
        if (this.mobs != null) {
            for (Mob m : this.mobs.values()) {
                if (m instanceof zabstracts.AbsBoss) {
                    zabstracts.AbsBoss b = (zabstracts.AbsBoss) m;
                    if (!b.isdie && b.hp > 0) {
                        return true;
                    }
                }
            }
        }
        if (BossTheGioi.mob != null && !BossTheGioi.mob.isdie && BossTheGioi.mob.hp > 0 && this.equals(BossTheGioi.mob.map)) {
            return true;
        }
        if (BossPica.mob != null && !BossPica.mob.isdie && BossPica.mob.hp > 0 && this.equals(BossPica.mob.map)) {
            return true;
        }
        if (event.eboss.KaidoRong.mob != null && !event.eboss.KaidoRong.mob.isdie && event.eboss.KaidoRong.mob.hp > 0 && this.equals(event.eboss.KaidoRong.mob.map)) {
            return true;
        }
        if (event.eboss.KingHoaTai.mob != null && !event.eboss.KingHoaTai.mob.isdie && event.eboss.KingHoaTai.mob.hp > 0 && this.equals(event.eboss.KingHoaTai.mob.map)) {
            return true;
        }
        if (event.eboss.LucciGioTo.mob != null && !event.eboss.LucciGioTo.mob.isdie && event.eboss.LucciGioTo.mob.hp > 0 && this.equals(event.eboss.LucciGioTo.mob.map)) {
            return true;
        }
        return false;
    }

    /**
     * Kiểm tra xem khu vực này hiện có Boss với MobTemplate ID chỉ định đang sống hay không.
     */
    public boolean hasActiveBoss(int mobTemplateId) {
        if (this.mobs != null) {
            for (Mob m : this.mobs.values()) {
                if (m instanceof zabstracts.AbsBoss && m.mtemplate != null && m.mtemplate.mob_id == mobTemplateId) {
                    zabstracts.AbsBoss b = (zabstracts.AbsBoss) m;
                    if (!b.isdie && b.hp > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem trên toàn bộ các khu của mapId này đã có Siêu Trùm nào đang sống hay chưa.
     * Quy tắc: Mỗi map id chỉ được tối đa 1 siêu trùm ở 1 khu bất kỳ.
     */
    public static boolean hasActiveSuperBossInMap(int mapId) {
        if (mapId <= 0) return false;
        // 1. Kiểm tra SuperBossManager.ENTRYS
        if (boss.SuperBossManager.ENTRYS != null) {
            for (zabstracts.AbsBoss b : boss.SuperBossManager.ENTRYS) {
                if (b != null && b.isSieuTrum() && !b.isdie && b.hp > 0 && b.map != null && b.map.template != null && b.map.template.id == mapId) {
                    return true;
                }
            }
        }
        // 2. Kiểm tra trực tiếp các Zone của mapId
        Zone[] zones = Zone.getMapByID(mapId);
        if (zones != null) {
            for (Zone z : zones) {
                if (z != null && z.mobs != null) {
                    for (Mob m : z.mobs.values()) {
                        if (m instanceof zabstracts.AbsBoss) {
                            zabstracts.AbsBoss b = (zabstracts.AbsBoss) m;
                            if (b.isSieuTrum() && !b.isdie && b.hp > 0) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem trên toàn bộ các khu của mapId này đã có Đệ Tử Hoang nào đang sống hay chưa.
     * Quy tắc: Mỗi map id chỉ được tối đa 1 đệ tử hoang ở 1 khu bất kỳ.
     */
    public static boolean hasActiveWildDeTuInMap(int mapId) {
        if (mapId <= 0) return false;
        // 1. Kiểm tra WILD_DETU_MAP
        if (model.DeTu.WILD_DETU_MAP != null) {
            for (model.DeTu dt : model.DeTu.WILD_DETU_MAP.values()) {
                if (dt != null && !dt.isdie && dt.master == null && dt.map != null && dt.map.template != null && dt.map.template.id == mapId) {
                    return true;
                }
            }
        }
        // 2. Kiểm tra trực tiếp các Zone của mapId
        Zone[] zones = Zone.getMapByID(mapId);
        if (zones != null) {
            for (Zone z : zones) {
                if (z != null && z.players != null) {
                    synchronized (z.players) {
                        for (Player p : z.players) {
                            if (p instanceof model.DeTu) {
                                model.DeTu dt = (model.DeTu) p;
                                if (!dt.isdie && dt.master == null) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void send_msg_all_p(Message m, Player p, boolean all) throws IOException {
        for (int i = 0; i < players.size(); i++) {
            Player p0 = players.get(i);
            if (p0.conn == null && !p0.isBot && !p0.isDe && !(p0 instanceof model.DeTu)) {
                synchronized (this) {
                    players.remove(p0);
                }
                // remove_obj(p0.index_map, 0);
            } else {
                if (all || (p != null && p0.index_map != p.index_map)) {
                    p0.addmsg(m);
                }
            }
        }
    }

    public void send_chat(Player p, Message m2) throws IOException {
        String s = m2.reader().readUTF();
        if (p != null && !p.isBot && p.conn != null) {
            bot.botplayer.BotPlayerManager.learnChat(s);
        }
        if (s != null) {
            String worldText = activities.Chat.extractWorldChatPrefix(s);
            if (worldText != null && !worldText.isEmpty()) {
                activities.Chat.requestWorldChat(p, worldText);
                return;
            }
        }
        if (p.getUsername().equals("admin") && s.equals("admin")) {

            p.getService().openDynamicMenu(9999, "Menu Admin",
                    new String[]{
                            "Bảo trì",
                            "Buff Beri - Ruby",
                            "Buff Extol",
                            "Uplevel",
                            "setXP",
                            "get item",
                            "DauGiaPanel",
                            "Buff coin",
                            "Buff Mốc",
                            "Buff danh hiệu"},
                    null);

        }
        
        if (p.getUsername().equals("admin") && s.equals("vptk")) {
            SuKienHalloween.gI().createBoss();
        } else if(p.getUsername().equals("admin") && s.equals("santacall")) {// vuong event noel
            SuKienNoel.gI().callSanta();
        } else if (p.getUsername().equals("admin") && s.equals("vol")) {
            p.getService().send_box_ThongBao_OK("Online: " + SessionManager.CLIENT_ENTRYS.size());
        } else if (s.equalsIgnoreCase("auto") || s.equalsIgnoreCase("!auto") || s.equalsIgnoreCase("bando") || s.equalsIgnoreCase("!bando") || s.equalsIgnoreCase("autoban")) {
            zinterfaces.menus.AutoSystemMenu.openAutoMainMenu(p);
        } else {
            this.send_chat_popup(0, p.index_map, s);
        }
    }

    public void send_chat_popup(int type, int id_p, String s) throws IOException {
        if (this.template != null && this.template.id == 119) {
            Player p0 = this.get_player_by_id_inmap(id_p);
            if (p0 != null && p0.isBot) {
                return; // Chặn toàn bộ chat của bot tại phòng chờ pvp truy nã (map 119)
            }
        }
        Message m = new Message(17);
        switch (type) {
            case 0: {
                m.writer().writeShort(id_p);
                m.writer().writeByte(0);
                m.writer().writeUTF(s);
                Player p0 = this.get_player_by_id_inmap(id_p);
                this.send_msg_all_p(m, p0, false);
                break;
            }
        }
        m.cleanup();
    }

    public void send_in4_obj_inmap(Player p) throws IOException {
        // send npc
        boolean haveBoss = false;
        if(event.EventManager.isActive(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025)) {
            for (BiNgoMa biNgoMa : SuKienHalloween.gI().listBiNgoMa) {
                if (!biNgoMa.mob.isdie && biNgoMa.mob.map != null && biNgoMa.mob.map.equals(p.map)) {
                    p.map.getService().move(p, (byte) 1, biNgoMa.mob.index, biNgoMa.mob.x, biNgoMa.mob.y);
                    haveBoss = true;
                }
            }
        }
        if(SuKienNoel.gI().isNoel()) {// vuong event noel
            for (SantaNoel santa : SuKienNoel.gI().listSanta.values()) {
                if (!santa.isOff && santa.map != null && santa.map.equals(p.map)) {
                    p.map.getService().move(p, (byte) 0, santa.index_map, santa.x, santa.y);
                }
            }
            for (QuaiVatTuyetNoel quaiVatTuyet : SuKienNoel.gI().listBoss) {
                if (!quaiVatTuyet.mob.isdie && quaiVatTuyet.mob.map != null && quaiVatTuyet.mob.map.equals(p.map)) {
                    p.map.getService().move(p, (byte) 1, quaiVatTuyet.mob.index, quaiVatTuyet.mob.x, quaiVatTuyet.mob.y);
                    haveBoss = true;
                }
            }
        }
        if (event.EventManager.isActive(event.Event.ID_SUKIEN_GIOTOHUNGVUONG_2026)) {
            for (LanSuTu Zombie : SuKienGioTo.gI().listBoss) {
                if (!Zombie.mob.isdie && Zombie.mob.map != null && Zombie.mob.map.equals(p.map)) {
                    p.map.getService().move(p, (byte) 1, Zombie.mob.index, Zombie.mob.x, Zombie.mob.y);
                    haveBoss = true;
                }
            }
        }
        //
        if (BossPica.mob != null && !BossPica.mob.isdie && BossPica.mob.map != null && BossPica.mob.map.equals(p.map)) {
            Message m_local = new Message(1);
            m_local.writer().writeByte(1);
            m_local.writer().writeShort(BossPica.mob.index);
            m_local.writer().writeShort(BossPica.mob.x);
            m_local.writer().writeShort(BossPica.mob.y);
            p.addmsg(m_local);
            m_local.cleanup();
        }
        try {
            zinterfaces.iNpc.dispatchInitNpcsForMap(this);
        } catch (Exception e) {
            System.err.println("[Zone] Warning: dispatchInitNpcsForMap error: " + e.getMessage());
        }

        try {
            List<Npc> validNpcs = new ArrayList<>();
            if (this.template.npcs != null && !this.template.npcs.isEmpty()) {
                for (int i = 0; i < this.template.npcs.size(); i++) {
                    Npc npc = this.template.npcs.get(i);
                    if (npc != null) {
                        validNpcs.add(npc);
                    }
                }
            }

            Message mnpc = new Message(16);
            mnpc.writer().writeByte(validNpcs.size());
            for (Npc npc : validNpcs) {
                try {
                    mnpc.writer().writeShort(npc.idmenu);
                    String npcName = (npc.name != null) ? npc.name : "";
                    String npcNameGt = (npc.namegt != null) ? npc.namegt : "";
                    String npcChat = (npc.getChat() != null) ? npc.getChat() : "";
                    mnpc.writer().writeUTF(npcName);
                    mnpc.writer().writeUTF(npcNameGt);
                    mnpc.writer().writeUTF(npcChat);
                    mnpc.writer().writeShort(npc.x);
                    mnpc.writer().writeShort(npc.y);
                    mnpc.writer().writeByte(npc.isPerson);
                    mnpc.writer().writeByte(npc.typeIcon);
                    mnpc.writer().writeByte(npc.wBlock > 0 ? npc.wBlock : 20);
                    mnpc.writer().writeByte(npc.hBlock > 0 ? npc.hBlock : 20);
                    mnpc.writer().writeByte(npc.b3);
                    if (npc.b3 == 0) {
                        byte icon = (npc.dataFrame != null && npc.dataFrame.length > 0) ? npc.dataFrame[0] : 1;
                        byte frames = (npc.dataFrame != null && npc.dataFrame.length > 1) ? npc.dataFrame[1] : 2;
                        mnpc.writer().writeByte(icon);
                        mnpc.writer().writeByte(frames);
                    } else {
                        mnpc.writer().writeShort(npc.head);
                        mnpc.writer().writeShort(npc.hair);
                        short[] wearing = (npc.wearing != null) ? npc.wearing : new short[]{-1, -1, -1, -1};
                        mnpc.writer().writeByte(wearing.length);
                        for (int j = 0; j < wearing.length; j++) {
                            if (wearing[j] == -1) {
                                mnpc.writer().writeByte(-1);
                            } else {
                                mnpc.writer().writeByte(1);
                                mnpc.writer().writeShort(wearing[j]);
                            }
                        }
                    }
                } catch (Exception exNpc) {
                    System.err.println("[Zone] Error serializing NPC " + (npc != null ? npc.idmenu : "null") + ": " + exNpc.getMessage());
                }
            }
            p.addmsg(mnpc);
            mnpc.cleanup();
        } catch (Exception e) {
            System.err.println("[Zone] Error sending NPC list to player " + p.name + ": " + e.getMessage());
        }
        // map boss
        if (Zone.is_map_boss(this.template.id)) {
            if (p.map_boss_info != null && p.map_boss_info.mob != null && !p.map_boss_info.mob.isEmpty()) {
                for (int i = 0; i < p.map_boss_info.mob.size(); i++) {
                    Mob mob = p.map_boss_info.mob.get(i);
                    if (mob != null && !mob.isdie) {
                        Zone.sendMobPos(p, mob);
                        if (p.getService() != null) {
                            p.getService().send_mob_info(mob);
                        }
                    }
                }
            } else if (!this.mobs.isEmpty()) {
                for (Mob mob : this.mobs.values()) {
                    if (mob != null && !mob.isdie) {
                        Zone.sendMobPos(p, mob);
                        if (p.getService() != null) {
                            p.getService().send_mob_info(mob);
                        }
                    }
                }
            }
        }
        if (event.EventManager.isActive(3) && p.clan != null && p.clan.mob1 != null) {
            for (Mob cm : p.clan.mob1) {
                if (cm != null) Zone.sendMobPos(p, cm);
            }
        }
        if (this.map_dungeon != null && this.map_dungeon.mobs != null) {
            for (Mob mob : this.map_dungeon.mobs) {
                if (mob != null && !mob.isdie && (mob.map == null || mob.map.equals(this))) {
                    Zone.sendMobPos(p, mob);
                    if (p.getService() != null) {
                        p.getService().send_mob_info(mob);
                    }
                }
            }
        }

        if (this.map_Hang != null && this.map_Hang.mobs != null && (this.map_dungeon == null || !this.map_dungeon.equals(this.map_Hang))) {
            for (Mob mob : this.map_Hang.mobs) {
                if (mob != null && !mob.isdie && (mob.map == null || mob.map.equals(this))) {
                    Zone.sendMobPos(p, mob);
                    if (p.getService() != null) {
                        p.getService().send_mob_info(mob);
                    }
                }
            }
        }
        if (this.map_LienTang != null && this.map_LienTang.mobs != null && (this.map_dungeon == null || !this.map_dungeon.equals(this.map_LienTang))) {
            for (Mob mob : this.map_LienTang.mobs) {
                if (mob != null && !mob.isdie && (mob.map == null || mob.map.equals(this))) {
                    Zone.sendMobPos(p, mob);
                    if (p.getService() != null) {
                        p.getService().send_mob_info(mob);
                    }
                }
            }
        }
        if (this.map_SieuLienTang != null && this.map_SieuLienTang.mobs != null && (this.map_dungeon == null || !this.map_dungeon.equals(this.map_SieuLienTang))) {
            for (Mob mob : this.map_SieuLienTang.mobs) {
                if (mob != null && !mob.isdie && (mob.map == null || mob.map.equals(this))) {
                    Zone.sendMobPos(p, mob);
                    if (p.getService() != null) {
                        p.getService().send_mob_info(mob);
                    }
                }
            }
        }
        if (this.map_Mr3 != null && this.map_Mr3.mobs != null && (this.map_dungeon == null || !this.map_dungeon.equals(this.map_Mr3))) {
            for (Mob mob : this.map_Mr3.mobs) {
                if (mob != null && !mob.isdie && (mob.map == null || mob.map.equals(this))) {
                    Zone.sendMobPos(p, mob);
                    if (p.getService() != null) {
                        p.getService().send_mob_info(mob);
                    }
                }
            }
        }
        if (this.map_ThuThachVeThan != null && this.map_ThuThachVeThan.mobs != null && (this.map_dungeon == null || !this.map_dungeon.equals(this.map_ThuThachVeThan))) {
            for (Mob mob : this.map_ThuThachVeThan.mobs) {
                if (mob != null && !mob.isdie && (mob.map == null || mob.map.equals(this))) {
                    Zone.sendMobPos(p, mob);
                    if (p.getService() != null) {
                        p.getService().send_mob_info(mob);
                    }
                }
            }
        }
        if (this.map_DaoKhoBau != null && this.map_DaoKhoBau.mobs != null && (this.map_dungeon == null || !this.map_dungeon.equals(this.map_DaoKhoBau))) {
            for (Mob mob : this.map_DaoKhoBau.mobs) {
                if (mob != null && !mob.isdie && (mob.map == null || mob.map.equals(this))) {
                    Zone.sendMobPos(p, mob);
                    if (p.getService() != null) {
                        p.getService().send_mob_info(mob);
                    }
                }
            }
        }
        if (this.vuonCam != null && this.vuonCam.mobs != null) {
            for (Mob mob : this.vuonCam.mobs) {
                if (mob != null && !mob.isdie && (mob.map == null || mob.map.equals(this))) {
                    Zone.sendMobPos(p, mob);
                    if (p.getService() != null) {
                        p.getService().send_mob_info(mob);
                    }
                }
            }
        }

        if (this.template.id == 81 && this.map_little_garden != null && this.map_little_garden.mobs != null) {
            for (Mob mob : this.map_little_garden.mobs) {
                if (mob != null && !mob.isdie && mob.hp > 0 && (mob.map == null || mob.map.equals(this))) {
                    Zone.sendMobPos(p, mob);
                    if (p.getService() != null) {
                        p.getService().send_mob_info(mob);
                    }
                }
            }
        }
        // Sự kiện Hè - gửi danh sách Pokemon đến người chơi
        EventManager.dispatchSendMobsToPlayer(p, this);

        // boss siêu trùm
        if (SuperBossManager.ENTRYS != null) {
            for (zabstracts.AbsBoss bossTemp : SuperBossManager.ENTRYS) {
                if (bossTemp != null && !bossTemp.isdie && bossTemp.map != null && bossTemp.map.equals(p.map)) {
                    Zone.sendMobPos(p, bossTemp);
                    if (p.getService() != null) {
                        p.getService().send_mob_info(bossTemp);
                    }
                    break;
                }
            }
        }
        // mob thường trong map (luôn gửi để hiển thị đầy đủ quái khi vào map, đồng bộ HP nếu bị mất máu hoặc là trụ/boss/quái phó bản)
        if (this.list_mob != null) {
            for (int idx : this.list_mob) {
                Mob mob = this.getMob(idx);
                if (mob != null && !mob.isdie && mob.hp > 0) {
                    Zone.sendMobPos(p, mob);
                    if (this.isDungeon() || mob.index < 0 || mob.is_boss || mob.hp < mob.hp_max || (mob.mtemplate != null && (mob.mtemplate.mob_id == 132 || mob.mtemplate.mob_id == 133))) {
                        if (p.getService() != null) {
                            p.getService().send_mob_info(mob);
                        }
                    }
                }
            }
        }
        // Đảm bảo boss Săn Trùm có trong mobs map để được gửi duy nhất 1 lần cùng quái dynamic
        if (this.mapSanTrum != null && this.mapSanTrum.mob != null && !this.mapSanTrum.mob.isdie) {
            if (this.mobs != null && !this.mobs.containsKey(this.mapSanTrum.mob.index)) {
                this.mobs.put(this.mapSanTrum.mob.index, this.mapSanTrum.mob);
            }
        }
        // gửi thêm quái custom/dynamically added trong zone nếu chưa có trong list_mob
        if (this.mobs != null && !this.mobs.isEmpty()) {
            for (Mob mob : this.mobs.values()) {
                if (mob != null && !mob.isdie && mob.hp > 0) {
                    boolean alreadySent = false;
                    if (this.list_mob != null) {
                        for (int idx : this.list_mob) {
                            if (idx == mob.index) {
                                alreadySent = true;
                                break;
                            }
                        }
                    }
                    if (!alreadySent) {
                        Zone.sendMobPos(p, mob);
                        if (this.isDungeon() || mob.index < 0 || mob.is_boss || mob.hp < mob.hp_max || (mob.mtemplate != null && (mob.mtemplate.mob_id == 132 || mob.mtemplate.mob_id == 133))) {
                            if (p.getService() != null) {
                                p.getService().send_mob_info(mob);
                            }
                        }
                    }
                }
            }
        }
        // WorldBoss
        if (BossTheGioi.mob != null && BossTheGioi.mob.map != null && BossTheGioi.mob.map.equals(p.map) && !BossTheGioi.mob.isdie) {
            Zone.sendMobPos(p, BossTheGioi.mob);
            if (p.getService() != null) {
                p.getService().send_mob_info(BossTheGioi.mob);
            }
        }
        if (event.eboss.LucciGioTo.mob != null && event.eboss.LucciGioTo.mob.map != null
                && event.eboss.LucciGioTo.mob.map.equals(p.map) && !event.eboss.LucciGioTo.mob.isdie) {
            Zone.sendMobPos(p, event.eboss.LucciGioTo.mob);
            if (p.getService() != null) {
                p.getService().send_mob_info(event.eboss.LucciGioTo.mob);
            }
        }
        //
        if (!Zone.is_map_dont_show_other_info(this.template.id)) {
            // send player
            for (int i = 0; i < players.size(); i++) {
                Player p0 = players.get(i);
                if (p0 != null && p.index_map != p0.index_map) {
                    // Send existing player p0 to newcomer p (move + full character info, clothes, skins, buffs)
                    p.map.getService().move(p, (byte) 0, p0.index_map, p0.x, p0.y);
                    try {
                        this.send_char_in4_inmap(p, p0.index_map);
                    } catch (Exception ignored) {}
                    p.id_meet_in_map.add("" + p0.index_map);

                    // Send newcomer p to existing player p0 immediately (visual, info, wearing, buffs)
                    if (p0.conn != null && p0.map != null && p0.map.getService() != null) {
                        p0.map.getService().move(p0, (byte) 0, p.index_map, p.x, p.y);
                        try {
                            this.send_char_in4_inmap(p0, p.index_map);
                        } catch (Exception ignored) {}
                        p0.id_meet_in_map.add("" + p.index_map);
                        if (p.ship_pet != null && p.ship_pet.map != null
                                && p.ship_pet.map.equals(this) && !p.isSpectator) {
                            p0.map.getService().move(p0, (byte) 0, p.ship_pet.index_map, p.ship_pet.x, p.ship_pet.y);
                        }
                    }

                    if (p0.ship_pet != null && p0.ship_pet.map != null
                            && p0.ship_pet.map.equals(p.map) && !p0.isSpectator) {
                        p.map.getService().move(p, (byte) 0, p0.ship_pet.index_map, p0.ship_pet.x, p0.ship_pet.y);
                    }
                }
            }
            if (!WorldWar.mapTranChienLon(this.template.id)) {
                if (this.getService() != null) {
                    this.getService().send_update_point_ww(p);
                }
                for (int i = 0; i < players.size(); i++) {
                    Player p0 = players.get(i);
                    if (p0 != null && p0.index_map != p.index_map && p.getService() != null) {
                        p.getService().send_update_point_ww(p0);
                    }
                }
            }
            //
            boolean check = true;
            for (int i = 0; i < DataTemplate.mSea.length; i++) {
                if (DataTemplate.mSea[i][0] == this.template.id) {
                    check = false;
                    break;
                }
            }
            //
            p.map.getService().move(p, (byte) 0, p.index_map, p.x, p.y, check);
        }
        if (p.party != null) {
            try {
                p.party.send_info();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        p.change_new_date();
        this.send_active_buffs(p, p);
        p.update_info_to_all();
    }

    public void send_active_buffs(Player receiver, Player target) {
        if (receiver == null || receiver.conn == null || target == null || target.active_buffs == null || target.active_buffs.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        for (ActiveBuff b : target.active_buffs) {
            if (b != null && b.expireTime > now) {
                int remaining = (int) (b.expireTime - now);
                try {
                    Message m12 = new Message(20);
                    m12.writer().writeByte(1);
                    m12.writer().writeShort(b.canonicalBuffId);
                    m12.writer().writeShort(target.index_map);
                    m12.writer().writeByte(0);
                    m12.writer().writeShort(b.icon);
                    m12.writer().writeShort(b.effSkill);
                    m12.writer().writeInt(remaining);
                    m12.writer().writeByte(0);
                    m12.writer().writeByte(1);
                    m12.writer().writeShort(target.index_map);
                    if (b.list_id != null && b.list_par != null) {
                        m12.writer().writeByte(b.list_id.size());
                        for (int i = 0; i < b.list_id.size(); i++) {
                            m12.writer().writeByte(b.list_id.get(i));
                            m12.writer().writeShort(b.list_par.get(i));
                        }
                    } else {
                        m12.writer().writeByte(0);
                    }
                    if (b.extraEffs != null && b.extraEffs.length > 0) {
                        m12.writer().writeByte(b.extraEffs.length);
                        for (int i = 0; i < b.extraEffs.length; i++) {
                            m12.writer().writeShort(b.extraEffs[i]);
                        }
                    } else {
                        m12.writer().writeByte(0);
                    }
                    receiver.addmsg(m12);
                    m12.cleanup();

                    if (b.idDataEff > 0) {
                        effect.DataEffect.sendData(receiver.conn, b.idDataEff);
                        Message mEff = new Message(74);
                        mEff.writer().writeByte(1);
                        mEff.writer().writeShort(target.index_map);
                        mEff.writer().writeShort(b.idDataEff);
                        mEff.writer().writeInt(remaining);
                        mEff.writer().writeByte(0);
                        mEff.writer().writeByte(0);
                        receiver.addmsg(mEff);
                        mEff.cleanup();
                    }
                } catch (Exception e) {
                    System.err.println("[Zone.send_active_buffs] Error sending buff " + b.canonicalBuffId + " to " + receiver.name + ": " + e.getMessage());
                }
            }
        }
    }

    public void send_char_in4_inmap(Player p, short id) throws IOException {
        if(SuKienNoel.gI().isNoel()) {// vuong event noel
            SantaNoel santa = SuKienNoel.gI().getSanta(id);
            if(santa != null) {
                SuKienNoel.gI().sendSanta(p, santa);
                return;
            }
        }
        Player p0 = get_player_by_id_inmap(id);
        if (p0 == null) {
            for (int i = 0; i < p.map.players.size(); i++) {
                if (p.map.players.get(i).detu != null && p.map.players.get(i).detu.index_map == id) {
                    p0 = p.map.players.get(i).detu;
                    break;
                }
            }
        }
        if (p0 != null && p0.map != null) {
            if (!p0.map.equals(p.map)) {
                return;
            }
            boolean new_enter = false;
            if (!p.id_meet_in_map.contains("" + p0.index_map)) {
                p.id_meet_in_map.add("" + p0.index_map);
                new_enter = true;
            }
            int dir_ = 1;
            for (int i12 = 0; i12 < p0.fashion.size(); i12++) {
                if ((p0.fashion.get(i12).id == 55) && p0.fashion.get(i12).is_use) {
                    if (p.idWeather == -1) {
                        Message m3 = new Message(-47);
                        m3.writer().writeByte(8);
                        m3.writer().writeByte(4);
                        p.addmsg(m3);
                        m3.cleanup();
                        p.idWeather = 8;
                    }
                    break;
                }
            }
            Message m = new Message(-5);
            m.writer().writeShort(p0.index_map);
            m.writer().writeByte(0);
            m.writer().writeByte(0); // typePlayer
            m.writer().writeByte(p0.typePirate); // typePirate
            m.writer().writeByte(p0.type_pk); // typePk
            m.writer().writeByte(new_enter ? dir_ : 0); // eff dir new
            m.writer().writeByte(-1); // index team
            m.writer().writeUTF(p0.name);
            m.writer().writeShort(p0.level);
            m.writer().writeInt(p0.ability.get_hp_max(true));
            m.writer().writeInt(p0.hp);
            m.writer().writeShort(p0.thongthao);
            m.writer().writeInt(Ranked.get_rank_wanted(p0.name));
            m.writer().writeByte(p0.ability.get_level_perfect());
            m.writer().writeByte(p0.clazz);
            m.writer().writeByte(-1); // dir new
            m.writer().writeByte(p0.item.it_heart != null ? 
                    (p0.item.it_heart.levelUp > 99 ? 99 : p0.item.it_heart.levelUp)
                    : 0); // levelheart
            //
            m.writer().writeShort(-1); // body bay
            m.writer().writeShort(-1); // leg bay
            m.writer().writeShort(-1); // weapon bay
            //
            p.addmsg(m);
            m.cleanup();
            //
            if (p.getService() != null) {
                p.getService().pet(p0, false);
                p.getService().update_PK(p0, false);
            }
            if (p0.getService() != null) {
                p0.getService().pet(p, false);
                p0.getService().update_PK(p, false);
            }
            if (p.getService() != null) {
                p.getService().Weapon_fashion(p0, false);
                p.getService().getThanhTich(p0);
                p.getService().charWearing(p0, false);
                if (p0.id_danh_hieu_su_dung > 0) {
                    activities.DanhHieu dh0 = activities.DanhHieu.get_Id(p0.id_danh_hieu_su_dung);
                    if (dh0 != null && dh0.idEff > 0) {
                        try {
                            Message mDh = new Message(74);
                            mDh.writer().writeByte(1);
                            mDh.writer().writeShort(p0.index_map);
                            mDh.writer().writeShort(dh0.idEff);
                            mDh.writer().writeInt(-1);
                            mDh.writer().writeByte(0);
                            mDh.writer().writeByte(0);
                            p.addmsg(mDh);
                            mDh.cleanup();
                        } catch (Exception ignored) {}
                    }
                }
            }
            if (p0.getService() != null && p.id_danh_hieu_su_dung > 0) {
                activities.DanhHieu dhSelf = activities.DanhHieu.get_Id(p.id_danh_hieu_su_dung);
                if (dhSelf != null && dhSelf.idEff > 0) {
                    try {
                        Message mDh = new Message(74);
                        mDh.writer().writeByte(1);
                        mDh.writer().writeShort(p.index_map);
                        mDh.writer().writeShort(dhSelf.idEff);
                        mDh.writer().writeInt(-1);
                        mDh.writer().writeByte(0);
                        mDh.writer().writeByte(0);
                        p0.addmsg(mDh);
                        mDh.cleanup();
                    } catch (Exception ignored) {}
                }
            }
            // Sync active buffs between p and p0
            this.send_active_buffs(p, p0);
            this.send_active_buffs(p0, p);
            //
            if (p0 != null && p != null) {
                this.update_boat(p0, p, false);
                this.update_boat(p, p0, false);
            }
            //
            EffTemplate eff = p0.get_eff(7);
            if (eff != null) {
                Message m2 = new Message(-71);
                m2.writer().writeByte(1);
                m2.writer().writeShort(p0.index_map);
                m2.writer().writeByte(0);
                m2.writer().writeInt((int) ((eff.time - System.currentTimeMillis()) / 1000));
                p.addmsg(m2);
                m2.cleanup();
            }
            // clan
            if (p0.clan != null) {
                Clan.send_me_to_other(p0, p, false);
            }
            model.Tree t = SuKienTrongCay.getByName(p0.name);
            if (t != null && t.map.equals(p.map)) {
                Message m_local = new Message(1);
                m_local.writer().writeByte(2);
                m_local.writer().writeShort(t.index);
                m_local.writer().writeShort(t.x);
                m_local.writer().writeShort(t.y);
                p.addmsg(m_local);
                m_local.cleanup();
            }
            DauTruongTuDo.SendInfoOther(p0, p);
            if ((p.map.map_vp != null || MessageHandler.isMapDetu(p0.map.template.id)) && !p0.isSpectator && !p.isSpectator) {
                Player follower0 = null;
                if (p0 instanceof model.DeTu) {
                    follower0 = ((model.DeTu) p0).master;
                } else if (p0.isDeOnl && p0.detu != null) {
                    follower0 = p0.detu;
                }
                if (follower0 != null) {
                    Message m_local = new Message(1);
                    m_local.writer().writeByte(0);
                    m_local.writer().writeShort(follower0.index_map);
                    m_local.writer().writeShort(p0.x);
                    m_local.writer().writeShort(p0.y);
                    p.addmsg(m_local);
                    m_local.cleanup();
                }
            }
        } else {
            Ship_pet spet = Ship_pet.get_pet(id);
            if (spet == null) {
                spet = p.ship_pet;
            }
            if (spet != null && spet.map != null && spet.map.equals(p.map)) {
                Message m = new Message(-5);
                m.writer().writeShort(spet.index_map);
                m.writer().writeByte(0);
                m.writer().writeByte(2); // typePlayer
                m.writer().writeByte(spet.main_ship.typePirate); // typePirate
                m.writer().writeByte(-1); // typePk
                m.writer().writeByte(1);
                m.writer().writeByte(-1); // index team
                m.writer().writeUTF(spet.name);
                m.writer().writeShort(1); // level
                m.writer().writeInt(spet.hp_max);
                m.writer().writeInt(spet.hp);
                m.writer().writeShort(0);
                m.writer().writeInt(-1);
                m.writer().writeByte(0);
                //
                m.writer().writeShort(999);
                m.writer().writeByte(1);
                m.writer().writeShort(spet.main_ship.index_map);
                m.writer().writeByte(spet.main_ship.typePirate);
                //
                m.writer().writeShort(-1); // body bay
                m.writer().writeShort(-1); // leg bay
                m.writer().writeShort(-1); // weapon bay
                //
                p.addmsg(m);
                m.cleanup();
                //
            }
            model.Tree t = SuKienTrongCay.getByIdx(id);
            if (t != null) {
                Message m = new Message(-5);
                m.writer().writeShort(t.index);
                m.writer().writeByte(2);
                m.writer().writeByte(3); // typePlayer
                m.writer().writeByte(-1); // typePirate
                m.writer().writeByte(-1); // typePk
                m.writer().writeByte(1);
                m.writer().writeByte(-1); // index team
                m.writer().writeUTF("Cây " + t.name);
                m.writer().writeShort(99); // level
                m.writer().writeInt(100);
                m.writer().writeInt(t.hp);
                m.writer().writeShort(0);
                m.writer().writeInt(-1);
                m.writer().writeByte(0);
                //
                short treeTemplateId = 62;
                if (t.type == 1 || t.type == 2) {
                    treeTemplateId = 63;
                }
                m.writer().writeShort(treeTemplateId);
                m.writer().writeByte(1);
                m.writer().writeShort(t.index);
                m.writer().writeByte(-1);
                //
                m.writer().writeShort(-1); // body bay
                m.writer().writeShort(-1); // leg bay
                m.writer().writeShort(-1); // weapon bay
                //
                p.addmsg(m);
                m.cleanup();
                //
                m = new Message(31);
                m.writer().writeShort(t.index);
                m.writer().writeUTF("Giao tiếp");
                //
                p.addmsg(m);
                m.cleanup();
                // [FIX BUG-9] Gửi packet HP (-83) để client hiển thị đúng HP cây
                m = new Message(-83);
                m.writer().writeShort(t.index);
                m.writer().writeByte(2);
                m.writer().writeInt(100);
                m.writer().writeInt(t.hp);
                m.writer().writeInt(0);
                m.writer().writeInt(100);
                m.writer().writeInt(t.hp);
                m.writer().writeInt(0);
                p.addmsg(m);
                m.cleanup();
                // Gửi packet state (71)
                m = new Message(71);
                m.writer().writeShort(t.index);
                m.writer().writeByte(2);
                int timeRemain = (int) ((t.timeThuHoach - System.currentTimeMillis()) / 1_000L);
                m.writer().writeShort(timeRemain);
                m.writer().writeByte(t.stateOld);
                p.addmsg(m);
                m.cleanup();
            }
        }
    }

    public Player get_player_by_id_inmap(int id) {
        Player p0 = null;
        for (int i = 0; i < players.size(); i++) {
            Player p01 = players.get(i);
            if (p01 != null && p01.index_map == id) {
                p0 = p01;
                break;
            }
        }
        return p0;
    }

    public static boolean map_cant_save_site(int id) {
        boolean check = false;
        for (int i = 0; i < DataTemplate.mSea.length; i++) {
            if (DataTemplate.mSea[i][1] == id) {
                check = true;
                break;
            }
        }
        return check || id == 64 || id == 984 || id == 1000 || id == 9998 || id == 9999 || id == 115
                || id == 81 || id == 120 || id == 122 || id == 123 || id == 119 || id == 58 || id == 59 || id == 62 || id == 80 || (id >= 913 && id <= 917) || (id >= 9990 && id <= 9996) || (id >= 199 && id <= 211)
                || (id >= 254 && id <= 258) || (id >= 261 && id <= 265) || (id >= 178 && id <= 184) || id == 266 || id == 301 || (id >= 267 && id <= 275)
                || id == 260 || id == 259
                || (id >= 70 && id <= 74 && id != 73)
                || Zone.is_map_boss(id) || Zone.is_map_dungeon(id) || Zone.is_map_luyentap(id);
    }

    public static boolean is_map_sea(int id) {
        return id == 7;
    }

    public void change_flag(Player p, int type) throws IOException {
        if (p == null) return;
        if (this.map_vp != null) {
            p.type_pk = (byte) type;
            for (int i = 0; i < this.players.size(); i++) {
                Player p0 = this.players.get(i);
                if (p0 != null && p0.conn != null && p0.getService() != null) {
                    p0.getService().update_PK(p, false);
                }
            }
            return;
        }
        // Chỉ ép cờ khi đang ở trong các Instance Phó bản/Chiến trường đặc biệt:
        if (this.map_little_garden != null) {
            if (p.clan != null && p.clan.equals(this.map_little_garden.clan1)) {
                type = 4;
            } else if (p.clan != null && p.clan.equals(this.map_little_garden.clan2)) {
                type = 5;
            }
        } else if (this.map_dungeon instanceof ChiemDao && ChiemDao.isOpen()) {
            ChiemDao cd = (ChiemDao) this.map_dungeon;
            if (cd.occupyingClan != null && p.clan != null && cd.occupyingClan.id == p.clan.id) {
                type = 5;
            } else if (p.clan != null) {
                type = 4;
            }
        } else if (this.baoVePhaoDai != null) {
            if (p.clan != null && p.clan.equals(this.baoVePhaoDai.clanA)) {
                type = 4;
            } else if (p.clan != null && p.clan.equals(this.baoVePhaoDai.clanB)) {
                type = 5;
            }
        } else if (this.pvpBangMapFight != null) {
            if (p.clan != null && p.clan.equals(this.pvpBangMapFight.clan1)) {
                type = 4;
            } else if (p.clan != null && p.clan.equals(this.pvpBangMapFight.clan2)) {
                type = 5;
            }
        } else if (DauTruongTuDo.IsCantChangeFlag(p)) {
            DauTruongTuDoInfo info = DauTruongTuDo.GetInfo(p);
            if (info != null && (info.typePk == 4 || info.typePk == 5)) {
                type = info.typePk;
                p.isBackTypePk = true;
            }
        }

        p.type_pk = (byte) type;
        for (int i = 0; i < this.players.size(); i++) {
            Player p0 = this.players.get(i);
            if (p0 != null) {
                network.Service svc = p0.getService();
                if (svc != null) {
                    svc.update_PK(p, false);
                }
            }
        }
        if (p.detu != null) {
            p.detu.type_pk = p.type_pk;
            p.detu.typePirate = p.typePirate;
            p.detu.clan = p.clan;
            for (int i = 0; i < this.players.size(); i++) {
                Player p0 = this.players.get(i);
                if (p0 != null) {
                    network.Service svc = p0.getService();
                    if (svc != null) {
                        svc.update_PK(p.detu, false);
                    }
                }
            }
        }
        try {
            java.util.List<bot.mercenary.MercenaryBot> activeBots = bot.mercenary.MercenaryManager.gI().getActiveBots(p);
            if (activeBots != null) {
                for (bot.mercenary.MercenaryBot merc : activeBots) {
                    if (merc != null && merc.map != null && merc.map.equals(this)) {
                        merc.type_pk = p.type_pk;
                        merc.typePirate = p.typePirate;
                        merc.clan = p.clan;
                        for (int i = 0; i < this.players.size(); i++) {
                            Player p0 = this.players.get(i);
                            if (p0 != null) {
                                network.Service svc = p0.getService();
                                if (svc != null) {
                                    svc.update_PK(merc, false);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable ignored) {}
    }

    public synchronized void pick_item(Player p, short id, byte cat) throws IOException {
        Message m2 = new Message(-18);
        m2.writer().writeShort(id);
        m2.writer().writeByte(cat);
        pick_item(p, m2);
        m2.cleanup();
    }

    public synchronized void pick_item(Player p, Message m2) throws IOException {
        if (p == null || p.isdie || p.hp <= 0 || (p.rms.length > 2 && p.rms[2].length > 0 && p.rms[2][0] == 0)) {
            return;
        }
        short id = m2.reader().readShort();
        byte cat = m2.reader().readByte();

        ItemMap itCheck = null;
        for (int i = 0; i < list_it_map.length; i++) {
            if (list_it_map[i] != null && list_it_map[i].category == cat && list_it_map[i].index == id) {
                itCheck = list_it_map[i];
                break;
            }
        }
        if (itCheck == null) return;

        byte code_response = -1;
        //
        switch (cat) {
            case 3: {
                for (int i = 0; i < list_it_map.length; i++) {
                    if (list_it_map[i] != null && list_it_map[i].category == cat
                            && list_it_map[i].index == id) {
                        if (list_it_map[i].id_master == -1 || (list_it_map[i].id_master != -1
                                && list_it_map[i].id_master == p.index_map)) {
                            ItemTemplate3 temp3 = ItemTemplate3.get_it_by_id(list_it_map[i].id);
                            if (temp3 != null && p.rms.length > 2 && p.rms[2].length > 3) {
                                // System.out.println(p.rms[2][1] + " " + temp3.color);
                                if (p.rms[2][1] == 1 && temp3.color < 2) {
                                    // return;
                                }
                                if (p.rms[2][1] == 2 && temp3.color < 3) {
                                    // return;
                                }
                            }
                            //
                            if (temp3 != null) {
                                Item_wear it_add = new Item_wear();
                                it_add.setup_template_by_id(temp3);
                                if (it_add.template != null) {
                                    if (!p.item.add_item_bag3(it_add)) {
                                        if (p.getService() != null) {
                                            p.getService().send_box_ThongBao_OK("Hành trang của bạn đã đầy!");
                                        }
                                        return;
                                    }
                                    p.item.updateInventory(false);
                                    historys.ItemPickupHistory.logPickup(p, 3, temp3.id, temp3.name, 1, this);
                                }
                            }
                            list_it_map[i] = null;
                            code_response = 0;
                        } else {
                            code_response = 1;
                        }
                        break;
                    }
                }
                break;
            }
            case 5: { // quest
                for (int i = 0; i < list_it_map.length; i++) {
                    if (list_it_map[i] != null && list_it_map[i].category == cat
                            && list_it_map[i].index == id) {
                        if (list_it_map[i].id < DataTemplate.NamePotionquest.length) {
                            if (list_it_map[i].id_master == -1 || (list_it_map[i].id_master != -1
                                    && list_it_map[i].id_master == p.index_map)) {
                                if (!p.item.add_item_bag47(5, list_it_map[i].id,
                                        list_it_map[i].quant)) {
                                    if (p.getService() != null) {
                                        p.getService().send_box_ThongBao_OK("Hành trang của bạn đã đầy!");
                                    }
                                    return;
                                }
                                p.item.updateInventory(false);
                                p.update_num_item_quest(2, list_it_map[i].id, list_it_map[i].quant);
                                String qName = (list_it_map[i].id < DataTemplate.NamePotionquest.length) ? DataTemplate.NamePotionquest[list_it_map[i].id] : "Item Quest";
                                historys.ItemPickupHistory.logPickup(p, 5, list_it_map[i].id, qName, list_it_map[i].quant, this);
                                list_it_map[i] = null;
                                code_response = 0;
                            } else {
                                code_response = 1;
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case 4: {
                for (int i = 0; i < list_it_map.length; i++) {
                    if (list_it_map[i] != null && list_it_map[i].category == cat
                            && list_it_map[i].index == id) {
                        if (this.template.id == 81 && this.map_little_garden != null) {
                            if (list_it_map[i].id_master == -1 || (list_it_map[i].id_master != -1
                                    && list_it_map[i].id_master == p.index_map)) {
                                //
                                switch (list_it_map[i].id) {
                                    case 94: {
                                        if (p.type_pk == 4) {
                                            for (int j = 0; j < this.players.size(); j++) {
                                                Player p0 = this.players.get(j);
                                                if (p0 != null && p0.conn != null && p0.type_pk == 5
                                                        && !p0.isdie) {
                                                    die_player(p0, p);
                                                    p0.time_hs_little_garden
                                                            = System.currentTimeMillis() + 10_000L;
                                                    p0.getService().send_time_cool_down(p0.time_hs_little_garden, "Hồi sinh", 3);
                                                }
                                            }
                                        } else {
                                            for (int j = 0; j < this.players.size(); j++) {
                                                Player p0 = this.players.get(j);
                                                if (p0 != null && p0.conn != null && p0.type_pk == 4
                                                        && !p0.isdie) {
                                                    die_player(p0, p);
                                                    p0.time_hs_little_garden
                                                            = System.currentTimeMillis() + 10_000L;
                                                    p0.getService().send_time_cool_down(p0.time_hs_little_garden, "Hồi sinh", 3);
                                                }
                                            }
                                        }
                                        break;
                                    }
                                    case 95: {
                                        for (int j = 0; j < this.players.size(); j++) {
                                            Player p0 = this.players.get(j);
                                            if (p0 != null && p0.conn != null
                                                    && p0.type_pk == p.type_pk && p0.isdie) {
                                                p0.time_hs_little_garden = 0;
                                            }
                                        }
                                        break;
                                    }
                                    case 96: {
                                        TranChienKhongLo.update_mp(this, p.type_pk, 0);
                                        break;
                                    }
                                    case 97: {
                                        TranChienKhongLo.update_mp(this, p.type_pk, 2);
                                        break;
                                    }
                                    case 98: {
                                        TranChienKhongLo.update_hp(this, p.type_pk, 2);
                                        break;
                                    }
                                    case 99: {
                                        TranChienKhongLo.update_mp(this, p.type_pk, 1);
                                        break;
                                    }
                                    case 100: {
                                        TranChienKhongLo.update_hp(this, p.type_pk, 1);
                                        break;
                                    }
                                }
                                //
                                list_it_map[i] = null;
                                code_response = 2;
                            } else {
                                code_response = 1;
                            }
                        } else {
                            for (int i2 = 0; i2 < LeaveItemMap.ITEM_POTION.length; i2++) {
                                if (LeaveItemMap.ITEM_POTION[i2] == list_it_map[i].id
                                        || (list_it_map[i].id >= 7 && list_it_map[i].id <= 17)) {
                                    if (list_it_map[i].id_master == -1
                                            || (list_it_map[i].id_master != -1
                                            && list_it_map[i].id_master == p.index_map)) {
                                        if (list_it_map[i].id == 0) { // beri
                                            if (p.rms.length > 2 && p.rms[2].length > 3
                                                    && p.rms[2][3] == 1) {
                                                return;
                                            }
                                            p.update_vang(list_it_map[i].quant);
                                            p.updateMoney();
                                            historys.ItemPickupHistory.logPickup(p, 4, 0, "Beri", list_it_map[i].quant, this);
                                        } else if (list_it_map[i].id == 1) { // ruby
                                            if (p.rms.length > 2 && p.rms[2].length > 3
                                                    && p.rms[2][3] == 1) {
                                                return;
                                            }
                                            // p.update_ngoc(list_it_map[i].quant);
                                            // p.update_money();
                                            historys.ItemPickupHistory.logPickup(p, 4, 1, "Ruby", list_it_map[i].quant, this);
                                        } else {
                                            ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(list_it_map[i].id);
                                            if (p.rms.length > 2 && p.rms[2].length > 3) {
                                                if (it4 != null) {
                                                    if (p.rms[2][2] == 1 && it4.type != 1) {
                                                        return;
                                                    }
                                                    if (p.rms[2][2] == 2 && it4.type != 2) {
                                                        return;
                                                    }
                                                }
                                            }
                                            if (!p.item.add_item_bag47(4, list_it_map[i].id,
                                                    list_it_map[i].quant)) {
                                                if (p.getService() != null) {
                                                    p.getService().send_box_ThongBao_OK("Hành trang của bạn đã đầy!");
                                                }
                                                return;
                                            }
                                            p.item.updateInventory(false);
                                            historys.ItemPickupHistory.logPickup(p, 4, list_it_map[i].id, it4 != null ? it4.name : "Item", list_it_map[i].quant, this);
                                        }
                                        list_it_map[i] = null;
                                        code_response = 0;
                                    } else {
                                        code_response = 1;
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case 7: {
                for (int i = 0; i < list_it_map.length; i++) {
                    if (list_it_map[i] != null && list_it_map[i].category == cat
                            && list_it_map[i].index == id) {
                        if (list_it_map[i].id_master == -1 || (list_it_map[i].id_master != -1
                                && list_it_map[i].id_master == p.index_map)) {
                            if (!p.item.add_item_bag47(7, list_it_map[i].id,
                                    list_it_map[i].quant)) {
                                if (p.getService() != null) {
                                    p.getService().send_box_ThongBao_OK("Hành trang của bạn đã đầy!");
                                }
                                return;
                            }
                            p.item.updateInventory(false);
                            ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(list_it_map[i].id);
                            historys.ItemPickupHistory.logPickup(p, 7, list_it_map[i].id, it7 != null ? it7.name : "Đá khảm", list_it_map[i].quant, this);
                            list_it_map[i] = null;
                            code_response = 0;
                        } else {
                            code_response = 1;
                        }
                        break;
                    }
                }
                break;
            }
        }
        switch (code_response) {
            case -1: {
                remove_obj(id, cat);
                break;
            }
            case 0: { // ok
                Message m = new Message(12);
                m.writer().writeShort(id);
                m.writer().writeByte(cat);
                m.writer().writeShort(p.index_map);
                p.addmsg(m);
                m.cleanup();
                remove_obj(id, cat);
                break;
            }
            case 1: {
                if (p.time_pick_item_other < System.currentTimeMillis()) {
                    p.time_pick_item_other = System.currentTimeMillis() + 7_000L;
                    Message mnext = new Message(-31);
                    mnext.writer().writeByte(0);
                    mnext.writer().writeUTF("Vật phẩm của người khác");
                    mnext.writer().writeByte(0);
                    mnext.writer().writeShort(-1);
                    p.addmsg(mnext);
                    mnext.cleanup();
                }
                break;
            }
            case 2: { // little garden
                if (this.map_little_garden != null && !this.map_little_garden.is_finish
                        && (p.type_pk == 4 || p.type_pk == 5)) {
                    //
                    Message m = new Message(33);
                    m.writer().writeShort(id);
                    m.writer().writeByte(cat);
                    m.writer().writeByte(p.type_pk == 4 ? 0 : 1);
                    p.addmsg(m);
                    m.cleanup();
                    // remove_obj(id, cat);
                }
                break;
            }
        }
    }

    public void send_data(Player p) throws IOException {
        Message m = new Message(0);
        m.writer().writeShort(this.template.id);
        m.writer().writeByte(this.zone_id);
        m.writer().writeByte(p.isSpectator ? 1 : this.template.type_view_p);
        m.writer().writeShort(p.x);
        m.writer().writeShort(p.y);
        m.writer().writeInt(p.ability.get_hp_max(true));
        m.writer().writeInt(p.hp);
        m.writer().writeInt(p.ability.get_mp_max(true));
        m.writer().writeInt(p.mp);
        byte sendB = (this.template.b == 1 && this.template.data != null && this.template.data.length >= 2 && this.template.data[0] != null && this.template.data[0].length >= 3) ? (byte) 1 : (byte) 0;
        m.writer().writeByte(sendB);
        m.writer().writeByte(this.template.specMap);
        if (sendB == 1) {
            m.writer().writeInt(this.template.data[0].length);
            m.writer().write(this.template.data[0]);
            m.writer().writeInt(this.template.data[1] != null ? this.template.data[1].length : 0);
            if (this.template.data[1] != null && this.template.data[1].length > 0) {
                m.writer().write(this.template.data[1]);
            }
            int vgoSize = (this.template.vgos != null) ? this.template.vgos.size() : 0;
            m.writer().writeByte(vgoSize);
            for (int i = 0; i < vgoSize; i++) {
                String vgoName = (this.template.vgos.get(i).map_go != null && this.template.vgos.get(i).map_go.length > 0 && this.template.vgos.get(i).map_go[0] != null && this.template.vgos.get(i).map_go[0].template != null)
                        ? this.template.vgos.get(i).map_go[0].template.name : "";
                m.writer().writeUTF(vgoName);
                m.writer().writeShort(this.template.vgos.get(i).xold);
                m.writer().writeShort(this.template.vgos.get(i).yold);
            }
        }
        m.writer().writeByte(this.template.IDBack);
        m.writer().writeShort(this.template.HBack);
        m.writer().writeByte(this.template.id_eff_map);
        m.writer().writeByte(this.template.level);
        m.writer().writeByte(this.template.typeChangeMap == 2 ? 0 : this.template.typeChangeMap);
        if (this.template.specMap == 3) {
            m.writer().writeByte(this.template.mPosMapTrain.length);
            for (int i = 0; i < this.template.mPosMapTrain.length; i++) {
                for (int j = 0; j < this.template.mPosMapTrain[i].length; j++) {
                    m.writer().writeByte(this.template.mPosMapTrain[i][j]);
                }
            }
            m.writer().writeUTF(this.template.strTimeChange);
        }
        m.writer().writeUTF(this.template.name);
        p.addmsg(m);
        m.cleanup();
    }

    public void goto_map(Player p) throws IOException {
        if (p.conn != null || p.isBot || p.isDe || p instanceof model.DeTu) {
            p.ischangemap = false;
            p.spawnX = p.x;
            p.spawnY = p.y;
            p.hasMovedFromSpawn = false;
            p.time_change_map = System.currentTimeMillis() + 1500L;
            this.enter_map(p);
            this.send_data(p);
            //
            if (p.conn != null) {
                boolean send_move = true;
                for (int i = 0; i < DataTemplate.mSea.length; i++) {
                    if (DataTemplate.mSea[i][0] == this.template.id) {
                        send_move = false;
                        break;
                    }
                }
                if (send_move) {
                    p.map.getService().move(p, (byte) 0, p.index_map, p.x, p.y);
                }
                if (p.id_danh_hieu_su_dung > 0) {
                    activities.DanhHieu dhSelf = activities.DanhHieu.get_Id(p.id_danh_hieu_su_dung);
                    if (dhSelf != null && dhSelf.idEff > 0) {
                        try {
                            Message mDh = new Message(74);
                            mDh.writer().writeByte(1);
                            mDh.writer().writeShort(p.index_map);
                            mDh.writer().writeShort(dhSelf.idEff);
                            mDh.writer().writeInt(-1);
                            mDh.writer().writeByte(0);
                            mDh.writer().writeByte(0);
                            p.addmsg(mDh);
                            mDh.cleanup();
                        } catch (Exception ignored) {}
                    }
                }
                this.send_active_buffs(p, p);
            }
            // Cập nhật làng hồi sinh tương ứng với map hiện tại (không áp dụng cho map phó bản/đặc biệt)
            if (!Zone.map_cant_save_site(this.template.id) && !Zone.is_map_dungeon(this.template.id) && !this.isDungeon() && !(this.template.id >= 178 && this.template.id <= 184)) {
                int villageSave = Zone.getVillageMapId(this.template.id);
                if (villageSave > 0) {
                    p.id_map_save = villageSave;
                }
            }
            if (p.time_can_hs < 1) {
                p.time_can_hs = 7;
            }
        }
    }

    public static boolean is_map_save_revival(int id) {
        return isMapLang(id);
    }

    public void send_boat(Player p, boolean is_have_my_boat) throws IOException {
        for (int i = 0; i < DataTemplate.mSea.length; i++) {
            if (DataTemplate.mSea[i][0] == this.template.id) {
                Message m = new Message(-56);
                int size = is_have_my_boat ? this.template.list_boat.size()
                        : (this.template.list_boat.size() - 1);
                m.writer().writeByte(size);
                if (is_have_my_boat) {
                    m.writer().writeShort(p.index_map);
                    m.writer().writeShort(this.template.list_boat.get(0).x);
                    m.writer().writeShort(this.template.list_boat.get(0).y);
                    m.writer().writeByte(4);
                    m.writer().writeShort(0);
                    m.writer().writeShort(1);
                    m.writer().writeShort(2);
                    m.writer().writeShort(3);
                }
                for (int j = 1; j < this.template.list_boat.size(); j++) {
                    m.writer().writeShort(-1);
                    m.writer().writeShort(this.template.list_boat.get(j).x);
                    m.writer().writeShort(this.template.list_boat.get(j).y);
                    m.writer().writeByte(0);
                }
                p.msgs.add(m);
                m.cleanup();
                break;
            }
        }
    }

    public void enter_zone(Player p) throws IOException {
        p.ischangemap = false;
        p.spawnX = p.x;
        p.spawnY = p.y;
        p.hasMovedFromSpawn = false;
        p.time_change_map = System.currentTimeMillis() + 1500L;
        p.xold = p.x;
        p.yold = p.y;
        Message m = new Message(21);
        m.writer().writeByte(this.zone_id);
        m.writer().writeByte(0);
        m.writer().writeShort(p.x);
        m.writer().writeShort(p.y);
        m.writer().writeInt(p.ability.get_hp_max(true));
        m.writer().writeInt(p.hp);
        m.writer().writeInt(p.ability.get_mp_max(true));
        m.writer().writeInt(p.mp);
        m.writer().writeByte(p.map.template.IDBack);
        m.writer().writeShort(p.map.template.HBack);
        p.addmsg(m);
        m.cleanup();
        //
        try {
            this.change_flag(p, p.type_pk);
        } catch (Exception ignored) {}
        p.getService().update_PK(p, true);
        p.getService().pet(p, true);
        // Service.send_Quest(p,true);
        this.send_boat(p, true);
        this.update_boat(p, p, true);
        if (p.id_danh_hieu_su_dung > 0) {
            activities.DanhHieu dhSelf = activities.DanhHieu.get_Id(p.id_danh_hieu_su_dung);
            if (dhSelf != null && dhSelf.idEff > 0) {
                try {
                    Message mDh = new Message(74);
                    mDh.writer().writeByte(1);
                    mDh.writer().writeShort(p.index_map);
                    mDh.writer().writeShort(dhSelf.idEff);
                    mDh.writer().writeInt(-1);
                    mDh.writer().writeByte(0);
                    mDh.writer().writeByte(0);
                    p.addmsg(mDh);
                    mDh.cleanup();
                } catch (Exception ignored) {}
            }
        }
        this.send_active_buffs(p, p);

        // Flush cached messages (boat, pet, PK)
        if (p.conn != null) {
            try {
                while (!p.msgs.isEmpty()) {
                    Message m_send = p.msgs.poll();
                    if (m_send != null) {
                        try {
                            p.conn.addmsg(m_send);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Send all objects (NPCs, mobs, players, bosses) in the new zone
        try {
            this.send_in4_obj_inmap(p);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send existing items on ground in the new zone if any exist
        if (this.list_it_map != null) {
            List<ItemMap> existingItems = new ArrayList<>();
            for (int i = 0; i < this.list_it_map.length; i++) {
                ItemMap it = this.list_it_map[i];
                if (it != null && it.time_exist > System.currentTimeMillis()) {
                    existingItems.add(it);
                }
            }
            if (!existingItems.isEmpty()) {
                try {
                    Message mItem = new Message(11);
                    mItem.writer().writeByte(existingItems.size());
                    for (int i = 0; i < existingItems.size(); i++) {
                        ItemMap itm = existingItems.get(i);
                        mItem.writer().writeShort(itm.index);
                        mItem.writer().writeByte(itm.category);
                        mItem.writer().writeShort(itm.icon);
                        mItem.writer().writeByte(itm.color);
                        mItem.writer().writeUTF(itm.name);
                        mItem.writer().writeShort(-1);
                        mItem.writer().writeByte(1);
                        Player ownerP = this.get_player_by_id_inmap(itm.id_master);
                        if (ownerP != null) {
                            ownerP = ownerP.getOwnerPlayer();
                        }
                        mItem.writer().writeShort(ownerP != null ? ownerP.index_map : -1);
                    }
                    p.addmsg(mItem);
                    mItem.cleanup();
                } catch (Exception ignored) {}
            }
        }

        p.wait_change_map = false;
        p.ischangemap = false;
        p.time_change_map = System.currentTimeMillis() + 1500L;
    }

    public void update_boat(Player p0, Player p, boolean cache) throws IOException {
        if (p0 == null || p == null) return;
        boolean check = false;
        if (this.template != null) {
            for (int i = 0; i < DataTemplate.mSea.length; i++) {
                if (DataTemplate.mSea[i][1] == this.template.id) {
                    check = true;
                    break;
                }
            }
        }
        int mapId = -1;
        if (p0.map != null && p0.map.template != null) {
            mapId = p0.map.template.id;
        } else if (this.template != null) {
            mapId = this.template.id;
        }
        if (check || mapId == 984) {
            Message m = new Message(-62);
            m.writer().writeShort(p0.index_map);
            m.writer().writeByte(0);
            m.writer().writeByte(4);
            short[] part_boat = p0.get_part_boat();
            m.writer().writeShort(part_boat[0]);
            m.writer().writeShort(part_boat[1]);
            m.writer().writeShort(part_boat[2]);
            m.writer().writeShort(part_boat[3]);
            if (cache) {
                p.msgs.add(m);
            } else {
                p.addmsg(m);
            }
            m.cleanup();
        } else {
            Message m = new Message(-33);
            m.writer().writeByte(0);
            if (p.rms != null && p.rms.length > 0 && p.rms[0] != null) {
                m.writer().writeShort(p.rms[0].length);
                if (p.rms[0].length > 0) {
                    m.writer().write(p.rms[0]);
                }
            } else {
                m.writer().writeShort(0);
            }
            if (cache) {
                p.msgs.add(m);
            } else {
                p.addmsg(m);
            }
            m.cleanup();
        }
    }

    public synchronized int get_index_item_map() {
        for (int i = 0; i < this.list_it_map.length; i++) {
            if (this.list_it_map[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public synchronized boolean addItemMap(ItemMap itm) {
        if (itm == null) return false;
        int idx = get_index_item_map();
        if (idx > -1 && idx < this.list_it_map.length) {
            itm.index = (short) idx;
            this.list_it_map[idx] = itm;
            return true;
        }
        return false;
    }

    public void send_weather(Player p) throws IOException {
        // 3 rain, 0 leaf wind, 1 snow, 2
        if (p.map.template.id_eff_map == -1) {
            Message m = new Message(-47);
            m.writer().writeByte(Zone.weather);
            m.writer().writeByte(Zone.weather_level);
            p.addmsg(m);
            m.cleanup();
        }
    }

    public Mob get_mobs(int id, int type) {
        switch (type) {
            case 0: {
                if (this.map_little_garden != null) {
                    for (int i = 0; i < this.map_little_garden.mobs.size(); i++) {
                        if (this.map_little_garden.mobs.get(i).index == id) {
                            return this.map_little_garden.mobs.get(i);
                        }
                    }
                }
                break;
            }
        }
        return null;
    }

    public boolean isMapSea() {
        for (int i = 0; i < DataTemplate.mSea.length; i++) {
            if (DataTemplate.mSea[i][1] == this.template.id) {
                return true;
            }
        }
        return false;
    }

    public boolean isRun() {
        return running;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
