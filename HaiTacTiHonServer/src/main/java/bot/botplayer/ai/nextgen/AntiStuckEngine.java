package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotPlayerReal;
import bot.botplayer.ai.Pathfinder;
import map.Zone;
import map.MapCanGoTo;

/**
 * AntiStuckEngine — Hệ thống phát hiện và tự động giải phóng kẹt theo State Machine đa tầng:
 *  - Cấp 1 (1.5 - 3s): SLOW_PROGRESS (Trượt tường, vi chỉnh tọa độ)
 *  - Cấp 2 (3 - 6s):   STUCK_SUSPECTED (Bước ngẫu nhiên moveRandom)
 *  - Cấp 3 (6 - 12s):  REPATH (Tìm lại đường BFS từ map hiện tại)
 *  - Cấp 4 (> 15s):    ABORT_ACTION (Hủy action hiện tại, trả về ActionQueue để GoalManager chọn Goal mới)
 */
public class AntiStuckEngine {

    public enum StuckSeverity {
        NORMAL,
        SLOW_PROGRESS,
        STUCK_SUSPECTED,
        REPATH,
        ABORT_ACTION
    }

    private short lastX = -1;
    private short lastY = -1;
    private long samePosStartTime = 0;
    private String lastState = "";
    private long stateStartTime = 0;
    private StuckSeverity currentSeverity = StuckSeverity.NORMAL;

    public void checkAndRecover(BotPlayerReal bot) {
        if (bot == null) return;
        long now = System.currentTimeMillis();

        // 1. Kiểm tra Null Map Stuck
        if (bot.map == null) {
            int fallbackMapId = bot.getTrainingMapId();
            if (fallbackMapId <= 1 || MapCanGoTo.isVillageMap(fallbackMapId)) fallbackMapId = 2;
            AIDebugLogger.logStuckRecovery(bot, "NULL_MAP_STUCK", "Joining fallback map " + fallbackMapId);
            Zone defaultZone = BotPlayerManager.findBestZoneForBot(bot, fallbackMapId);
            if (defaultZone != null) {
                try {
                    bot.join(defaultZone, (short) 300, (short) 300);
                } catch (Exception ignored) {}
            }
            return;
        }

        // 2. Vị trí không đổi (Movement / Terrain Stuck)
        // Chỉ kiểm tra kẹt di chuyển khi bot ĐANG có mục tiêu di chuyển hoặc chuyển map/portal
        if (bot.isdie) {
            samePosStartTime = 0;
            currentSeverity = StuckSeverity.NORMAL;
            return;
        }

        boolean isActivelyMoving = (bot.currentPath != null && !bot.currentPath.isEmpty()) 
                || bot.targetMapId > 0 
                || "NAVIGATE".equals(bot.state) 
                || "PORTAL".equals(bot.state) 
                || bot.ischangemap;

        if (isActivelyMoving && bot.x == lastX && bot.y == lastY) {
            if (samePosStartTime == 0) {
                samePosStartTime = now;
                currentSeverity = StuckSeverity.NORMAL;
            } else {
                long duration = now - samePosStartTime;
                if (duration > 15000L) {
                    if (currentSeverity != StuckSeverity.ABORT_ACTION) {
                        currentSeverity = StuckSeverity.ABORT_ACTION;
                        AIDebugLogger.logStuckRecovery(bot, "ABORT_ACTION_STUCK", "Cancelling current action plan");
                        if (bot.getActionQueue() != null) {
                            bot.getActionQueue().clear();
                        }
                        int curMapId = (bot.map != null && bot.map.template != null) ? bot.map.template.id : 1;
                        if (curMapId <= 1 || MapCanGoTo.isVillageMap(curMapId)) {
                            int trainMap = bot.getTrainingMapId();
                            if (trainMap <= 1 || MapCanGoTo.isVillageMap(trainMap)) trainMap = 2;
                            bot.targetMapId = trainMap;
                            bot.currentPath = Pathfinder.findPath(curMapId, trainMap);
                            bot.pathIndex = 0;
                            bot.state = "TRAIN_LEVEL";
                        } else {
                            bot.currentPath = null;
                            bot.targetMapId = -1;
                            bot.state = "FARM";
                        }
                        bot.ischangemap = false;
                    }
                } else if (duration > 7000L) {
                    if (currentSeverity != StuckSeverity.REPATH) {
                        currentSeverity = StuckSeverity.REPATH;
                        AIDebugLogger.logStuckRecovery(bot, "REPATH_STUCK", "Recalculating path to targetMapId " + bot.targetMapId);
                        if (bot.targetMapId > 0 && bot.map != null && bot.map.template != null) {
                            bot.currentPath = Pathfinder.findPath(bot.map.template.id, bot.targetMapId);
                            bot.pathIndex = 0;
                        }
                        bot.getMovementController().moveRandom();
                    }
                } else if (duration > 3500L) {
                    if (currentSeverity != StuckSeverity.STUCK_SUSPECTED) {
                        currentSeverity = StuckSeverity.STUCK_SUSPECTED;
                        AIDebugLogger.logStuckRecovery(bot, "TERRAIN_STUCK", "Randomizing movement");
                        bot.getMovementController().moveRandom();
                    }
                } else if (duration > 1500L) {
                    currentSeverity = StuckSeverity.SLOW_PROGRESS;
                }
            }
        } else {
            lastX = bot.x;
            lastY = bot.y;
            samePosStartTime = 0;
            currentSeverity = StuckSeverity.NORMAL;
        }

        // 3. State Watchdog Stuck Check (> 3 phút không đổi state khi PORTAL / DUNGEON)
        if (!bot.state.equals(lastState)) {
            lastState = bot.state;
            stateStartTime = now;
        } else if (now - stateStartTime > 180_000L) { // 3 mins
            if ("PORTAL".equals(bot.state) || "DUNGEON".equals(bot.state) || "FOLLOW_PARTY".equals(bot.state)) {
                AIDebugLogger.logStuckRecovery(bot, "STATE_WATCHDOG_STUCK", "Resetting action queue and state to FARM");
                if (bot.getActionQueue() != null) {
                    bot.getActionQueue().clear();
                }
                int curMapId = (bot.map != null && bot.map.template != null) ? bot.map.template.id : 1;
                if (curMapId <= 1 || MapCanGoTo.isVillageMap(curMapId)) {
                    int trainMap = bot.getTrainingMapId();
                    if (trainMap <= 1 || MapCanGoTo.isVillageMap(trainMap)) trainMap = 2;
                    bot.targetMapId = trainMap;
                    bot.currentPath = Pathfinder.findPath(curMapId, trainMap);
                    bot.pathIndex = 0;
                    bot.state = "TRAIN_LEVEL";
                } else {
                    bot.state = "FARM";
                    bot.currentPath = null;
                    bot.targetMapId = -1;
                }
                bot.ischangemap = false;
                stateStartTime = now;
            }
        }
    }
}
