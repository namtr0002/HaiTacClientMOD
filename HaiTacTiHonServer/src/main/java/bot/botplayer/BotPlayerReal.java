package bot.botplayer;

import bot.botplayer.ai.BotBrain;
import bot.botplayer.ai.BotCombatController;
import bot.botplayer.ai.BotMovementController;
import bot.botplayer.ai.BotInventoryController;
import bot.botplayer.ai.BotEquipmentController;
import bot.botplayer.ai.BotQuestController;
import bot.botplayer.ai.BotSocialController;
import bot.botplayer.ai.BotPersistenceController;
import bot.botplayer.ai.BotDailyController;
import bot.botplayer.ai.BotClanMissionController;
import bot.botplayer.ai.BotPvpQueueController;

import model.Player;
import map.MapTemplate;
import map.Zone;
import map.MapCanGoTo;
import network.Message;
import core.ZUtil;
import java.util.*;

/**
 * BotPlayerReal — Phiên bản Bot modular hóa.
 * Đóng vai trò là Shell (Composition base) kế thừa Player để tương thích với Type Hierarchy của Game Server.
 * Mọi logic hành vi được ủy quyền (delegate) cho các controller phụ trách riêng biệt.
 */
public class BotPlayerReal extends bot.Bot {

    // ——— Identity ———
    public String username;
    public String password;
    public int accountId;

    // ——— Bot Mode ———
    public enum BotMode { GRIND, BALANCED }
    public BotMode botMode = BotMode.BALANCED;

    // ——— Bot Type (loại bot) ———
    /**
     * FULL_AI    : Bot đầy đủ — cày, quest, đập đồ, phó bản, boss, clan, pvp (60%)
     * LOBBY_IDLE : Bot treo online — di chuyển hạn chế, chat, tạo cảm giác đông (25%)
     * PVP_QUEUE  : Bot ghép đấu — đứng map 119/arena, tăng tỉ lệ ghép PVP (15%)
     */
    public enum BotType { FULL_AI, LOBBY_IDLE, PVP_QUEUE }
    public BotType botType = BotType.FULL_AI;

    // ——— Navigation / State ———
    public int targetMapId = -1;
    public List<Integer> currentPath = null;
    public int pathIndex = -1;
    public String state = "FARM";
    public long nextActionTime = 0;
    public int lastMobId = -1;
    public long lastQuestProgressTime = 0;

    // ——— Delayed Tasks ———
    public static class DelayedTask {
        public long executeTime;
        public Runnable action;
        public DelayedTask(long executeTime, Runnable action) {
            this.executeTime = executeTime;
            this.action = action;
        }
    }
    public final List<DelayedTask> delayedTasks = new ArrayList<>();

    // ——— PVP Fields ———
    public int pvppoint = 0;
    public int wanted_point = 0;

    // ——— Modularity (Composition) ———
    private final BotBrain brain;
    private final BotCombatController combatController;
    private final BotMovementController movementController;
    private final BotInventoryController inventoryController;
    private final BotEquipmentController equipmentController;
    private final BotQuestController questController;
    private final BotSocialController socialController;
    private final BotPersistenceController persistenceController;
    private final BotDailyController dailyController;
    private final BotClanMissionController clanMissionController;
    private final BotPvpQueueController pvpQueueController;

    // ========================= CONSTRUCTOR =========================

    public BotPlayerReal(int id, String name) throws Exception {
        this(id, name, name, "bot123456", id);
    }

    public BotPlayerReal(int id, String name, String username, String password, int accountId) throws Exception {
        super(id, name);
        this.username = username;
        this.password = password;
        this.accountId = accountId;
        this.isBot = true;
        this.disableBaseChat = true;

        // Khởi tạo các module hành vi
        this.brain = new BotBrain(this);
        this.combatController = new BotCombatController(this);
        this.movementController = new BotMovementController(this);
        this.inventoryController = new BotInventoryController(this);
        this.equipmentController = new BotEquipmentController(this);
        this.questController = new BotQuestController(this);
        this.socialController = new BotSocialController(this);
        this.persistenceController = new BotPersistenceController(this);
        this.dailyController = new BotDailyController(this);
        this.clanMissionController = new BotClanMissionController(this);
        this.pvpQueueController = new BotPvpQueueController(this);
        updateBotTypePk();
    }

    // ========================= GETTERS =========================

    public BotBrain getBrain() { return brain; }
    public BotCombatController getCombatController() { return combatController; }
    public BotMovementController getMovementController() { return movementController; }
    public BotInventoryController getInventoryController() { return inventoryController; }
    public BotEquipmentController getEquipmentController() { return equipmentController; }
    public BotQuestController getQuestController() { return questController; }
    public BotSocialController getSocialController() { return socialController; }
    public BotPersistenceController getPersistenceController() { return persistenceController; }
    public BotDailyController getDailyController() { return dailyController; }
    public BotClanMissionController getClanMissionController() { return clanMissionController; }
    public BotPvpQueueController getPvpQueueController() { return pvpQueueController; }
    public bot.botplayer.ai.nextgen.ActionQueue getActionQueue() { return brain != null ? brain.getActionQueue() : null; }

    // ========================= LIFECYCLE DELEGATES =========================

    public static BotPlayerReal loadBot(String name, String password) {
        int[] ids = BotDb.getIds(name);
        if (ids == null) {
            int clazz = ZUtil.random(1, 5);
            BotDb.registerBot(name, password, clazz);
            ids = BotDb.getIds(name);
        }
        if (ids == null) {
            System.err.println("[BotPlayerReal] loadBot: cannot find/create bot '" + name + "'");
            return null;
        }

        int pId = ids[0];
        try {
            BotPlayerReal bot = new BotPlayerReal(pId, name, name, password, 0);
            if (bot.getPersistenceController().setupFromBotTable()) {
                bot.setin4();
                bot.init();
                network.SessionManager.PLAYERS_MAP.put(bot.IDPlayer, bot);
                network.SessionManager.PLAYERS_BY_NAME.put(bot.name, bot);
                return bot;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BotPlayerReal create(int id, String name, String username, String password, int accountId) {
        try {
            BotPlayerReal bot = new BotPlayerReal(id, name, username, password, accountId);
            boolean ok = false;
            try {
                ok = bot.setup();
            } catch (Exception ignored) {}
            if (!ok) {
                // Fallback RAM Init: Cân bằng tự động trong RAM không phụ thuộc SQL
                bot.isBot = true;
                bot.clazz = (byte) core.ZUtil.random(1, 5);
                bot.level = (short) core.ZUtil.random(50, 85);
                bot.setupBotBalancedAgainst(null);
            }
            bot.setin4();
            bot.init();
            network.SessionManager.PLAYERS_MAP.put(bot.IDPlayer, bot);
            network.SessionManager.PLAYERS_BY_NAME.put(bot.name, bot);
            return bot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update() {
        super.update();
        updateBotTypePk();
        // Ủy quyền hoàn toàn cho AI State Machine
        brain.tick();
    }

    @Override
    public synchronized int flush(Player p, boolean print) {
        if (!(p instanceof BotPlayerReal)) return 1;
        BotPlayerReal bot = (BotPlayerReal) p;
        int res = super.flush(bot, print, "players_bot");
        if (print) {
            // System.out.printf("[BotPlayerReal] Saved bot '%s' L%d to players_bot: %s%n",
            //     bot.name, bot.level, res == 0 ? "OK" : "FAIL");
        }
        return res;
    }

    @Override
    public void addmsg(Message m) {
        try {
            if (m == null) return;
            int cmd = m.cmd;
            
            if (cmd == -11) { // Lời mời Yes/No (Party, Trade, Lôi Đài...)
                m.reader().mark(50);
                short yesNoType = m.reader().readShort();
                long execTime = System.currentTimeMillis() + 500 + ZUtil.random(500);
                
                // Ủy quyền cho BotSocialController quyết định có Accept hay không
                if (socialController.shouldAcceptInvite(yesNoType)) {
                    synchronized (delayedTasks) {
                        delayedTasks.add(new DelayedTask(execTime,
                            () -> this.getService().sendConfirmYesNo(yesNoType)));
                    }
                } else {
                    if (this.map != null) {
                        this.map.send_chat_popup(0, this.index_map, "Đang bận farm nha AE!");
                    }
                }
                m.reader().reset();
            }
            m.cleanup();
        } catch (Exception ignored) {}
    }

    // ========================= TRAINING MAP DETERMINATION =========================

    public int getTrainingMapId() {
        int maxUnlocked = getMaxUnlockedMapId();
        // Giới hạn thêm bởi quest hiện tại (checkQuest)
        int questLimit = checkQuest(); // ID map tối đa chưa mở
        if (questLimit < 9999) {
            // maxUnlocked không được vượt đầu ra quest gate (giới hạn - 1)
            int questMax = questLimit - 1;
            if (questMax < maxUnlocked) maxUnlocked = questMax;
            if (maxUnlocked < 2) maxUnlocked = 2; // Tối thiểu là Map 2 (Rừng làng)
        }
        if (maxUnlocked < 2) maxUnlocked = 2;

        // Bảng danh sách các map cày cấp đa dạng trải đều từ map đầu đến map cuối
        int[][] TRAIN_MAP_GROUPS = {
            {1,   9,   2, 3, 4, 6, 220},
            {10,  17,  10, 11, 12, 14, 221},
            {18,  24,  18, 19, 20, 22, 222},
            {25,  31,  26, 27, 28, 30, 223},
            {32,  39,  34, 35, 36, 38, 224},
            {40,  49,  42, 43, 44, 46, 225},
            {50,  59,  50, 51, 52, 54, 226},
            {60,  69,  58, 59, 60, 62, 227},
            {70,  79,  66, 67, 68, 228},
            {80,  89,  74, 75, 76, 77, 78, 229},
            {90,  99,  84, 85, 86, 88, 94, 95},
            {100, 109, 96, 97, 98, 99, 100, 101, 103},
            {110, 119, 112, 115, 116, 117, 118, 124, 125, 126},
            {120, 200, 180, 184, 192, 193, 194, 195, 196, 197}
        };

        int selectedMapId = 2;
        for (int[] group : TRAIN_MAP_GROUPS) {
            if (this.level >= group[0] && this.level <= group[1]) {
                // Chọn một map ngẫu nhiên trong nhóm dựa trên ID hoặc random để phân tán đều
                int mapCount = group.length - 2;
                if (mapCount > 0) {
                    int offset = (Math.abs(this.IDPlayer) + ZUtil.random(mapCount)) % mapCount;
                    selectedMapId = group[2 + offset];
                } else {
                    selectedMapId = group[2];
                }
                break;
            }
        }

        if (selectedMapId > maxUnlocked) {
            selectedMapId = maxUnlocked;
        }
        if (selectedMapId <= 1 || MapCanGoTo.isVillageMap(selectedMapId)) {
            selectedMapId = 2;
        }

        if (MapTemplate.ENTRYS != null) {
            java.util.List<MapTemplate> suitableMaps = new java.util.ArrayList<>();
            for (MapTemplate t : MapTemplate.ENTRYS) {
                if (t.specMap != 0) continue;
                if (Zone.is_map_boss(t.id)) continue;
                if (t.id > maxUnlocked) continue;
                if (t.id <= 1 || MapCanGoTo.isVillageMap(t.id)) continue; // Không chọn map làng hoặc map 0/1 làm map cày cấp

                int mapLv = t.level & 0xFF;
                if (mapLv > 0 && Math.abs(mapLv - this.level) <= 6) {
                    suitableMaps.add(t);
                }
            }
            if (!suitableMaps.isEmpty()) {
                // 30% ưu tiên map có người chơi thật để tạo không khí đông vui
                if (ZUtil.random(100) < 30) {
                    for (MapTemplate t : suitableMaps) {
                        Zone[] zones = Zone.getMapByID(t.id);
                        if (zones != null) {
                            for (Zone z : zones) {
                                if (BotPlayerManager.hasRealPlayer(z)) {
                                    return t.id;
                                }
                            }
                        }
                    }
                }
                // Còn lại phân tán đều các map phù hợp
                int randIdx = (Math.abs(this.IDPlayer) + ZUtil.random(suitableMaps.size())) % suitableMaps.size();
                return suitableMaps.get(randIdx).id;
            }
        }
        return selectedMapId;
    }

    /**
     * Trả về map ID tối đa bot được phép đến dựa theo quest hiện tại.
     * (Sử dụng bởi BotBrain và BotQuestController)
     */
    public int getQuestMapLimit() {
        return checkQuest();
    }

    // ========================= DELEGATED SHORTCUTS =========================

    public void moveTowardsPublic(short tx, short ty) {
        movementController.moveTowards(tx, ty);
    }

    public void attackBossMob(mob.Mob boss) {
        combatController.attackBossMob(boss);
    }

    /** Expose parent protected field mapping */
    public void setupBotFieldsPublic(long vang, int kimcuong, int ruby,
                                      int point_tich_tieu, int level_so_tay, int exp_so_tay,
                                      int point_hang_dong, int hd_max, int so_tay_vip, int active_so_tay,
                                      int diem_danh_event) {
        setupBotFields(vang, kimcuong, ruby, point_tich_tieu, level_so_tay, exp_so_tay,
            point_hang_dong, hd_max, so_tay_vip, active_so_tay, diem_danh_event);
    }

    // ========================= PRIVATE FIELD ACCESSORS =========================

    public long get_vang() { return super.get_vang(); }
    public int get_ngoc_val() { return super.get_ngoc(); }
    public int get_coin_val() { return super.get_coin(); }
    public int get_ruby0_val() { return super.ruby(); }
    public int get_point_tich_tieu_val() { return super.get_point_tich_tieu(); }
    public int get_level_so_tay_val() { return super.so_tay(); }
    public int get_exp_so_tay_val() { return super.exp_so_tay(); }
    public int get_point_hang_dong_val() { return super.get_point_hang_dong(); }
    public int get_hd_max_val() { return super.get_hd_max(); }
    public int get_diem_danh_event_val() { return super.get_diem_danh_ev(); }
}
