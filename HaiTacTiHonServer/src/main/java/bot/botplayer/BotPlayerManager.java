package bot.botplayer;

import core.ZUtil;
import core.Log;
import map.Zone;
import network.SessionManager;
import bot.botplayer.BotPlayerReal.BotType;
import bot.BotDauTruongTuDo;
import bot.botplayer.ai.BotPvpCacheManager;
import bot.botplayer.ai.Pathfinder;
import bot.mercenary.MercenaryManager;
import bot.BotDauTruongTuDo;
import map.MapCanGoTo;
import model.Player;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BotPlayerManager — Quản lý vòng đời của tất cả BotPlayerReal.
 *
 * Nguyên tắc:
 *  - KHÔNG tạo Thread mới — init() và update() chạy từ map thread hoặc server startup
 *  - update() được gọi từ game loop (Zone/Map update) để login/logout bot theo chu kỳ
 *  - Quan sát người chơi thật và delegate sang GameAnalyzer để học hành vi
 */
public class BotPlayerManager {

    public static final Map<String, BotPlayerReal> activeBots = new ConcurrentHashMap<>();
    private static final List<String> botNamesPool = new ArrayList<>();

    /** Track thời gian hoạt động cuối cùng của mỗi bot (phục vụ auto-logout idle) */
    private static final ConcurrentHashMap<String, Long> botLastActivity = new ConcurrentHashMap<>();

    /** Thời gian idle tối đa trước khi logout: 2 giờ */
    private static final long MAX_IDLE_MS = 2L * 60 * 60 * 1000;

    /** Password chung cho tất cả bot — ASCII, không dấu, không cách */
    private static final String BOT_PASSWORD = "bot123456";

    /** Giới hạn tổng số tài khoản bot cố định trong DB (không spam tạo mới) */
    public static final int MAX_TOTAL_BOTS_IN_DB = 100;
    /** Giới hạn số bot online đồng thời trên server */
    public static final int MAX_ACTIVE_BOTS = 100;
    private static long lastCycleTick   = 0;
    private static long lastObserveTick = 0;

    public static Collection<BotPlayerReal> getActiveBots() {
        return activeBots.values();
    }

    // ========================= INIT =========================

    /**
     * Khởi tạo BotPlayerManager — KHÔNG dùng Thread riêng.
     * Gọi từ server startup sau khi map đã khởi tạo xong.
     */
    public static void init() {
        // Khởi tạo bảng players_bot trong DB
        BotDb.initBotTable();

        // Xóa cache cũ và lấy danh sách bot đã có trong DB
        clearRegisteredBotNamesCache();
        List<String> registeredInDb = getRegisteredBotNamesFromDb();

        // Tạo pool tên cố định đúng bằng MAX_TOTAL_BOTS_IN_DB
        botNamesPool.clear();
        botNamesPool.addAll(BotNameGenerator.generateUniqueNames(MAX_TOTAL_BOTS_IN_DB));

        // Kiểm tra xem đã có bao nhiêu bot trong DB, chỉ đăng ký bổ sung nếu chưa đủ
        if (registeredInDb.size() < MAX_TOTAL_BOTS_IN_DB) {
            int needed = MAX_TOTAL_BOTS_IN_DB - registeredInDb.size();
            int added = 0;
            for (String candidateName : botNamesPool) {
                if (!registeredInDb.contains(candidateName)) {
                    int clazz = ZUtil.random(1, 5);
                    BotDb.registerBot(candidateName, BOT_PASSWORD, clazz);
                    added++;
                    if (added >= needed) break;
                }
            }
            clearRegisteredBotNamesCache();
        }

        Log.info("BotManager", "Initializing Bot Player Manager (Roster: " + Math.min(MAX_TOTAL_BOTS_IN_DB, getRegisteredBotNamesFromDb().size()) + " persistent bots)...");

        // Khởi tạo bộ nhớ Cache PvP Bot trong RAM (Tránh truy vấn SQL ORDER BY RAND nhiều lần)
        BotPvpCacheManager.initCache();

        // Khởi tạo hệ thống clan bot
        BotBangHaiTac.initBotClans();

        // Khởi chạy luồng riêng cho Bot Player Manager (Tách khỏi luồng save_data)
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1500L); // Chạy cập nhật mỗi 1.5s
                    update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Bot-Manager-Thread").start();

        Log.success("BotManager", "Bot Player System Ready (" + activeBots.size() + " bots active)");
    }

    // ========================= LOGIN / LOGOUT =========================

    public static synchronized void loginBot(String targetName) {
        try {
            if (activeBots.containsKey(targetName)) return;

            BotPlayerReal bot = BotPlayerReal.loadBot(targetName, BOT_PASSWORD);
            if (bot == null) return;

            activeBots.put(targetName, bot);
            botLastActivity.put(targetName, System.currentTimeMillis()); // Ghi nhận hoạt động

            int maxUnlocked = bot.getMaxUnlockedMapId();
            int targetMap = bot.getTrainingMapId();
            if (targetMap > maxUnlocked) {
                targetMap = maxUnlocked;
            }
            if (targetMap <= 1 || MapCanGoTo.isVillageMap(targetMap)) {
                targetMap = 2;
            }
            bot.targetMapId = targetMap;
            bot.state = "TRAIN_LEVEL";
            Zone selectedZone = findBestZoneForBot(bot, targetMap);
            if (selectedZone != null) {
                short spawnX = (short) Math.max(100, Math.min(selectedZone.template.maxW - 100, 250 + ZUtil.random(150)));
                short spawnY = (short) Math.max(150, Math.min(selectedZone.template.maxH - 100, 200 + ZUtil.random(100)));
                bot.join(selectedZone, spawnX, spawnY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final List<String> cachedRegisteredBotNames = new ArrayList<>();

    /**
     * Xóa cache danh sách tên bot trong RAM để nạp lại từ DB khi có thay đổi.
     */
    public static void clearRegisteredBotNamesCache() {
        synchronized (cachedRegisteredBotNames) {
            cachedRegisteredBotNames.clear();
        }
    }

    /**
     * Lấy danh sách tất cả tên bot đã được đăng ký trong bảng players_bot (có cache RAM).
     */
    public static List<String> getRegisteredBotNamesFromDb() {
        synchronized (cachedRegisteredBotNames) {
            if (!cachedRegisteredBotNames.isEmpty()) {
                return new ArrayList<>(cachedRegisteredBotNames);
            }
        }
        List<String> list = new ArrayList<>();
        java.sql.Connection conn = null;
        java.sql.Statement st = null;
        java.sql.ResultSet rs = null;
        try {
            conn = database.DbManager.gI().getConnect();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT `name` FROM `players_bot` ORDER BY `id` ASC");
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
            synchronized (cachedRegisteredBotNames) {
                cachedRegisteredBotNames.clear();
                cachedRegisteredBotNames.addAll(list);
            }
        } catch (Exception e) {
            System.err.println("[BotPlayerManager] getRegisteredBotNamesFromDb error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (Exception ignored) {}
        }
        return list;
    }

    public static synchronized void loginRandomBot() {
        try {
            List<String> registeredInDb = getRegisteredBotNamesFromDb();
            List<String> offlineRegistered = new ArrayList<>();
            for (String name : registeredInDb) {
                if (!activeBots.containsKey(name)) {
                    offlineRegistered.add(name);
                }
            }

            if (offlineRegistered.isEmpty()) {
                return; // Đã online hết hoặc không có bot offline
            }

            // Tái sử dụng 100% bot có sẵn trong DB
            String targetName = offlineRegistered.get(ZUtil.random(offlineRegistered.size()));

            // Phân loại bot theo xác suất: 85% FULL_AI (cày cấp, làm nhiệm vụ, đi phó bản) / 15% PVP_QUEUE
            int roll = ZUtil.random(100);
            BotType assignedType;
            if (roll < 85) {
                assignedType = BotType.FULL_AI;
            } else {
                assignedType = BotType.PVP_QUEUE;
            }

            loginBotOfType(targetName, assignedType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Login bot với loại (BotType) chỉ định.
     * - FULL_AI: đi đến map training cày cấp, làm nv, đi phó bản
     * - PVP_QUEUE: spawn tại map 119 (Truy Nã) hoặc arena
     */
    public static synchronized void loginBotOfType(String targetName, BotType botType) {
        try {
            if (activeBots.containsKey(targetName)) return;

            BotPlayerReal bot = BotPlayerReal.loadBot(targetName, BOT_PASSWORD);
            if (bot == null) return;

            bot.botType = botType;

            activeBots.put(targetName, bot);
            botLastActivity.put(targetName, System.currentTimeMillis());

            int targetMap;
            int maxUnlocked = bot.getMaxUnlockedMapId();
            switch (botType) {
                case LOBBY_IDLE -> {
                    targetMap = bot.getTrainingMapId();
                    if (targetMap > maxUnlocked) targetMap = maxUnlocked;
                    if (targetMap <= 1 || MapCanGoTo.isVillageMap(targetMap)) targetMap = 2;
                    bot.state = "TRAIN_LEVEL";
                    bot.targetMapId = targetMap;
                }
                case PVP_QUEUE -> {
                    // Spawn tại map Truy Nã (119) hoặc tham gia luyện tập/hàng chờ pvp
                    targetMap = (ZUtil.random(100) < 50) ? 119 : bot.getTrainingMapId();
                    bot.state = "PVP_QUEUE";
                }
                default -> {
                    // FULL_AI: Cày cấp trải đều từ map đầu đến cuối theo cấp độ
                    targetMap = bot.getTrainingMapId();
                    if (bot.level == 1) {
                        targetMap = 2; // Khởi đầu cày quái lv 1 ở map 2 thay vì kẹt ở bãi biển hoang sơ
                    }
                    if (targetMap > maxUnlocked) targetMap = maxUnlocked;
                    if (targetMap <= 1 || MapCanGoTo.isVillageMap(targetMap)) targetMap = 2;
                    bot.targetMapId = targetMap;
                    bot.state = "TRAIN_LEVEL";
                }
            }

            Zone selectedZone = findBestZoneForBot(bot, targetMap);
            if (selectedZone != null) {
                short spawnX = (short) Math.max(100, Math.min(selectedZone.template.maxW - 100, 250 + ZUtil.random(150)));
                short spawnY = (short) Math.max(150, Math.min(selectedZone.template.maxH - 100, 200 + ZUtil.random(100)));
                bot.join(selectedZone, spawnX, spawnY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void logoutRandomBot() {
        try {
            if (activeBots.isEmpty()) return;
            List<String> names = new ArrayList<>(activeBots.keySet());
            Collections.shuffle(names);
            for (String targetName : names) {
                BotPlayerReal bot = activeBots.get(targetName);
                if (bot == null) continue;
                // Tuyệt đối không xóa/logout bot trước mặt người chơi thật
                boolean hasRealPlayer = false;
                if (bot.map != null && bot.map.players != null) {
                    for (int i = 0; i < bot.map.players.size(); i++) {
                        Player pl = bot.map.players.get(i);
                        if (pl != null && !pl.isBot && !pl.isdie) {
                            hasRealPlayer = true;
                            break;
                        }
                    }
                }
                if (hasRealPlayer) {
                    continue;
                }
                activeBots.remove(targetName);
                bot.leave();
                try { bot.flush(bot, false, "players_bot"); } catch (Exception ignored) {}
                SessionManager.PLAYERS_MAP.remove(bot.IDPlayer);
                SessionManager.PLAYERS_BY_NAME.remove(bot.name);
                SessionManager.PLAYERS_BY_INDEX.remove((int) bot.index_map);
                botLastActivity.remove(targetName);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========================= SHUTDOWN =========================

    /**
     * Lưu tất cả bot và logout khi server tắt.
     * Gọi từ server shutdown hook.
     */
    public static synchronized void shutdown() {
//        System.out.println("[BotPlayerManager] Saving all bots before shutdown...");
        for (Map.Entry<String, BotPlayerReal> entry : activeBots.entrySet()) {
            BotPlayerReal bot = entry.getValue();
            if (bot == null) continue;
            try {
                bot.flush(bot, false, "players_bot");
            } catch (Exception e) {
//                System.err.println("[BotPlayerManager] Error saving bot " + bot.name + ": " + e.getMessage());
            }
        }
        activeBots.clear();
//        System.out.println("[BotPlayerManager] All bots saved.");
    }

    // ========================= UPDATE (gọi từ map/game loop) =========================

    /**
     * Update cycle — được gọi từ map thread hoặc game timer.
     * KHÔNG tạo Thread mới ở đây.
     */
    public static void update() {
        long now = System.currentTimeMillis();

        // Tick Mercenary Manager (Mercenary Bot contracts and movement/combat AI)
        MercenaryManager.gI().tick();
        dispatchBotsToWorldWar();

        // Spawn bot từ từ đến MAX_ACTIVE_BOTS
        if (now - lastCycleTick > 1500) {
            lastCycleTick = now;

            int realPlayersCount = 0;
            for (Player p : SessionManager.PLAYERS_MAP.values()) {
                if (p != null && !p.isBot && p.conn != null) {
                    realPlayersCount++;
                }
            }
            int minBots = 20; // Luôn duy trì tối thiểu 20 bot cho thế giới sống động
            int maxBotsAllowed = (realPlayersCount > 0) ? Math.min(MAX_ACTIVE_BOTS, Math.max(minBots, realPlayersCount * 3)) : minBots;
            if (activeBots.size() < maxBotsAllowed) {
                int spawnBatch = Math.min(2, maxBotsAllowed - activeBots.size());
                for (int sb = 0; sb < spawnBatch; sb++) {
                    loginRandomBot();
                }
            } else if (activeBots.size() > maxBotsAllowed) {
                logoutRandomBot();
            }

            // Logout bot idle quá 2 giờ (giải phóng memory và thay bằng bot mới)
            try {
                List<String> idleBots = new ArrayList<>();
                for (Map.Entry<String, Long> entry : botLastActivity.entrySet()) {
                    if (now - entry.getValue() > MAX_IDLE_MS) {
                        idleBots.add(entry.getKey());
                    }
                }
                for (String idleName : idleBots) {
                    BotPlayerReal idleBot = activeBots.get(idleName);
                    if (idleBot != null) {
                        // Cập nhật activity nếu bot vẫn đang hoạt động trên map
                        if (idleBot.map != null && idleBot.map.players != null
                                && idleBot.map.players.contains(idleBot)) {
                            // Bot vẫn trên map, gia hạn
                            botLastActivity.put(idleName, now);
                        } else {
//                            System.out.printf("[BotPlayerManager] Idle logout: %s (idle > 2h)%n", idleName);
                            logoutBot(idleName);
                        }
                    }
                }
            } catch (Exception ignored) {}
        }

        // Quan sát real player mỗi 10 giây — học hành vi
        if (now - lastObserveTick > 10000) {
            lastObserveTick = now;
            observeAllRealPlayers();
        }

        // Tự động kiểm tra và giải phóng bot bị kẹt/lang thang ở các map làng (đặc biệt Map 1)
        checkAndRedirectVillageStuckBots(now);
    }

    private static long lastVillageStuckCheck = 0;

    /**
     * Tự động điều hướng bot đang ở map làng ra các map cày cấp nếu không có nhiệm vụ đối thoại đang làm.
     */
    private static void checkAndRedirectVillageStuckBots(long now) {
        if (now - lastVillageStuckCheck < 4000L) return;
        lastVillageStuckCheck = now;

        for (BotPlayerReal bot : activeBots.values()) {
            if (bot == null || bot.map == null || bot.map.template == null || bot.isdie) continue;
            int curMapId = bot.map.template.id;
            if (curMapId <= 1 || map.MapCanGoTo.isVillageMap(curMapId)) {
                // Kiểm tra xem bot có đang làm nhiệm vụ đối thoại tại làng này không
                boolean isDoingVillageQuest = false;
                template.QuestP q = bot.getMainQuest();
                if (q != null && q.template != null) {
                    int status = q.template.statusQuest;
                    if (status == 0 || status == 2) {
                        int npcId = (status == 2 && q.template.idNpcSub != 0 && q.template.idNpcSub != -1) ? q.template.idNpcSub : q.template.idNpc;
                        if (npcId != 0 && bot.map.template.npcs != null) {
                            for (map.Npc npc : bot.map.template.npcs) {
                                if (npc != null && (npc.idmenu == npcId || npc.idmenu == -Math.abs(npcId) || npc.idmenu == Math.abs(npcId))) {
                                    isDoingVillageQuest = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                // Nếu không có quest đối thoại ở làng và bot đang ở trạng thái FARM / TRAIN_LEVEL hoặc bị kẹt
                if (!isDoingVillageQuest && ("FARM".equals(bot.state) || "TRAIN_LEVEL".equals(bot.state) || bot.targetMapId <= 1 || bot.currentPath == null || bot.currentPath.isEmpty())) {
                    int trainMap = bot.getTrainingMapId();
                    if (trainMap <= 1 || MapCanGoTo.isVillageMap(trainMap)) trainMap = 2;
                    bot.targetMapId = trainMap;
                    bot.currentPath = Pathfinder.findPath(curMapId, trainMap);
                    bot.pathIndex = 0;
                    bot.state = "TRAIN_LEVEL";
                    bot.ischangemap = false;
                }
            }
        }
    }

    /**
     * Quan sát tất cả người chơi thật và học vào GameAnalyzer.
     */
    private static void observeAllRealPlayers() {
        if (activeBots.isEmpty()) return;
        try {
            for (Player p : SessionManager.PLAYERS_MAP.values()) {
                if (p == null || p.isBot || p.conn == null) continue;
                GameAnalyzer.observeRealPlayer(p);
            }
        } catch (Exception ignored) {}
    }

    // ========================= HELPERS =========================

    /** Logout cụ thể một bot theo tên */
    public static synchronized void logoutBot(String name) {
        try {
            BotPlayerReal bot = activeBots.remove(name);
            if (bot == null) return;
            bot.leave();
            try { bot.flush(bot, false); } catch (Exception ignored) {}
            SessionManager.PLAYERS_MAP.remove(bot.IDPlayer);
            SessionManager.PLAYERS_BY_NAME.remove(bot.name);
            SessionManager.PLAYERS_BY_INDEX.remove((int) bot.index_map);
            botLastActivity.remove(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Kiểm tra có real player trong zone không (chạy vòng lặp for-index an toàn hơn) */
    public static boolean hasRealPlayer(Zone zone) {
        if (zone == null || zone.players == null) return false;
        for (int i = 0; i < zone.players.size(); i++) {
            try {
                Player p = zone.players.get(i);
                if (p != null && !p.isBot && p.conn != null) return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    /** Đếm số bot hiện có trong khu */
    public static int getBotCountInZone(Zone z) {
        int count = 0;
        if (z != null && z.players != null) {
            for (int i = 0; i < z.players.size(); i++) {
                try {
                    Player p = z.players.get(i);
                    if (p != null && p.isBot) count++;
                } catch (Exception ignored) {}
            }
        }
        return count;
    }

    /** Auto dispatch bots into WorldWar maps (272..275) to fill lobby/factions (Code-Init Bots & Real-Player First Matchmaking) */
    public static void dispatchBotsToWorldWar() {
        boolean shouldDispatch = (map.zones.WorldWar.status != map.zones.WorldWar.STATUS_CLOSE);
        if (!shouldDispatch) {
            for (int mapId = 272; mapId <= 275; mapId++) {
                Zone[] zones = Zone.getMapByID(mapId);
                if (zones != null && zones.length > 0) {
                    for (Zone z : zones) {
                        if (z != null && hasRealPlayer(z)) {
                            shouldDispatch = true;
                            if (map.zones.WorldWar.status == map.zones.WorldWar.STATUS_CLOSE) {
                                map.zones.WorldWar.startRegister();
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (!shouldDispatch) return;

        // Ưu tiên sắp xếp ghép Người Chơi Thật (Real Players First) vào phòng thi đấu trước
        for (int targetMapId = 272; targetMapId <= 275; targetMapId++) {
            Zone[] zones = Zone.getMapByID(targetMapId);
            if (zones == null || zones.length == 0 || zones[0] == null) continue;
            Zone primaryZone = zones[0];

            int botCount = 0;
            if (primaryZone.players != null) {
                for (int i = 0; i < primaryZone.players.size(); i++) {
                    try {
                        Player p = primaryZone.players.get(i);
                        if (p != null && p.isBot) botCount++;
                    } catch (Exception ignored) {}
                }
            }

            // Đảm bảo có tối thiểu 9 Bot Động (3 Hải Quân, 3 Hải Tặc, 3 Quân Cách Mạng) Code-Init
            int targetTotalBots = 9;
            if (botCount < targetTotalBots) {
                int needed = targetTotalBots - botCount;
                for (int k = 0; k < needed; k++) {
                    try {
                        int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                        byte faction = (byte) ((k % 3) + 1); // Chia đều 3 phe: 1 HQ, 2 HT, 3 QCM
                        int levelRange = (targetMapId == 272) ? 40 : ((targetMapId == 273) ? 60 : ((targetMapId == 274) ? 80 : 95));
                        int vipTier = (k % 3); // Xen kẽ chỉ số: 0=Cùi, 1=Vừa Phải, 2=VIP (chuẩn 100%, không "ảo")
                        
                        bot.BotPVP codeInitBot = bot.BotPVP.createDynamicWorldWarBot(botId, faction, levelRange, vipTier);
                        if (codeInitBot != null) {
                            short spawnX = (short) ZUtil.random(180, 480);
                            short spawnY = (short) ZUtil.random(200, 320);
                            codeInitBot.join(primaryZone, spawnX, spawnY);
                            map.zones.WorldWar.setType(codeInitBot);
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    /** Kiểm tra xem bot có thể gia nhập khu hay không (tránh dồn cục, tránh full khu) */
    public static boolean isValidZoneForBot(Player bot, Zone z) {
        if (z == null || z.template == null) return false;

        int botCount = 0;
        int realCount = 0;
        int totalPlayers = 0;
        if (z.players != null) {
            totalPlayers = z.players.size();
            for (int i = 0; i < totalPlayers; i++) {
                try {
                    model.Player p = z.players.get(i);
                    if (p != null) {
                        if (p.isDe || p instanceof model.DeTu || p instanceof bot.mercenary.MercenaryBot) {
                            continue;
                        }
                        if (p.isBot) botCount++;
                        else if (p.conn != null) realCount++;
                    }
                } catch (Exception ignored) {}
            }
        }

        int maxPlayerCap = z.template.max_player & 0xFF;
        if (maxPlayerCap <= 0) maxPlayerCap = 20;

        // Tránh làm khu bị "FULL" -> Luôn chừa ít nhất 3 slot trống cho người chơi thật
        if (totalPlayers >= maxPlayerCap - 3) {
            return false;
        }

        // Nếu là map Trận Chiến Lớn (WorldWar 272..275), PvP, hoặc Phó bản -> Cho phép bot vào theo dung lượng
        boolean isSpecialBattleMap = (z.map_vp != null) 
                || (map.zones.WorldWar.mapTranChienLon(z.template.id)) 
                || (z.map_dungeon != null)
                || (z.template.id == 119 || z.template.id == 1000);

        if (isSpecialBattleMap) {
            return totalPlayers < maxPlayerCap - 2;
        }

        // Map thường / quái cày cấp / làng:
        // 1. Nếu có người chơi thật trong khu -> Giới hạn tối đa 1 bot (tránh tranh bãi / gây lag cho người chơi)
        if (realCount > 0) {
            return botCount < 1;
        }

        // 2. Khu vắng không có người thật:
        // Cân bằng phân bổ đều, tối đa 2 bot trên map làng / đảo và tối đa 3 bot trên map quái
        boolean isVillage = (z.template.id <= 1 || map.MapCanGoTo.isVillageMap(z.template.id));
        int maxBotsAllowed = isVillage ? 2 : 3;
        return botCount < maxBotsAllowed;
    }

    /** Tìm zone thích hợp nhất cho bot trên map cho trước dựa vào các quy tắc phân bổ đều */
    public static Zone findBestZoneForBot(BotPlayerReal bot, int mapId) {
        Zone[] zones = Zone.getMapByID(mapId);
        if (zones == null || zones.length == 0) return null;

        List<Zone> allowedZones = new ArrayList<>();
        for (Zone z : zones) {
            if (z != null && isValidZoneForBot(bot, z)) {
                allowedZones.add(z);
            }
        }

        if (!allowedZones.isEmpty()) {
            // Chọn zone có ít bot nhất trong số các zone được phép để rải đều
            Zone bestZone = null;
            int minBotCount = Integer.MAX_VALUE;
            for (Zone z : allowedZones) {
                int botCount = getBotCountInZone(z);
                if (botCount < minBotCount) {
                    minBotCount = botCount;
                    bestZone = z;
                }
            }
            return bestZone;
        }

        // Nếu tất cả các zone trên mapId này đã đạt giới hạn bot (mỗi khu 2-3 bot),
        // tìm zone ở map cày cấp thay thế phù hợp với level của bot để tránh tập trung ở 1 map
        if (bot != null) {
            int altMap = bot.getTrainingMapId();
            if (altMap != mapId) {
                Zone[] altZones = Zone.getMapByID(altMap);
                if (altZones != null && altZones.length > 0) {
                    for (Zone az : altZones) {
                        if (az != null && isValidZoneForBot(bot, az)) {
                            return az;
                        }
                    }
                }
            }
        }

        // Hồi phòng (fallback): Chọn zone có ít bot nhất và chưa bị đầy
        Zone bestFallback = null;
        int minBotCount = Integer.MAX_VALUE;
        for (Zone z : zones) {
            if (z == null) continue;
            int botCount = getBotCountInZone(z);
            int maxPlayerCap = (z.template != null) ? (z.template.max_player & 0xFF) : 20;
            int total = (z.players != null) ? z.players.size() : 0;
            if (total >= maxPlayerCap - 1) continue;
            if (botCount < minBotCount) {
                minBotCount = botCount;
                bestFallback = z;
            }
        }
        return bestFallback != null ? bestFallback : zones[0];
    }

    /**
     * Học chat từ người chơi thật — delegate sang GameAnalyzer.
     */
    public static void learnChat(String text) {
        GameAnalyzer.learnChat(text);
    }

    public static String getLearnedChat() {
        return GameAnalyzer.getRandomLearnedChat();
    }

    /**
     * Học giá thị trường — delegate sang GameAnalyzer.
     */
    public static void learnMarketPrice(short templateId, int price) {
        GameAnalyzer.learnMarketPrice(templateId, price);
    }

    public static int getLearnedPrice(short templateId, int defaultPrice) {
        return GameAnalyzer.getLearnedPrice(templateId, defaultPrice);
    }

    // ========================= BOSS ASSIST SYSTEM =========================
    
    private static int activeAssistMapId = -1;
    private static long assistTimeout = 0;

    public static void broadcastBossAssist(int mapId) {
        activeAssistMapId = mapId;
        assistTimeout = System.currentTimeMillis() + 60000; // Gọi hội trong 60s
    }

    public static int getActiveAssistMap() {
        if (System.currentTimeMillis() > assistTimeout) {
            activeAssistMapId = -1;
        }
        return activeAssistMapId;
    }

    // ========================= COMPAT METHODS (backward compat) =========================

    public static int getLearnedChatsCount() {
        return GameAnalyzer.getLearnedChatsCount();
    }

    public static int getLearnedPricesCount() {
        return GameAnalyzer.getLearnedPricesCount();
    }
}
