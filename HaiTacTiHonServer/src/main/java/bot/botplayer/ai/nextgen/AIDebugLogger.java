package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerReal;
import template.QuestP;

/**
 * AIDebugLogger — Hệ thống chẩn đoán và ghi log chi tiết các quyết định, lỗi và phục hồi của Bot AI.
 * Phân loại lỗi theo chuẩn:
 *   INIT_ERROR, SPAWN_ERROR, MAP_ERROR, ZONE_ERROR, PATH_ERROR, QUEST_ERROR,
 *   COMBAT_ERROR, INVENTORY_ERROR, EQUIPMENT_ERROR, PERSISTENCE_ERROR, ACTIVITY_ERROR, STATE_ERROR.
 */
public class AIDebugLogger {

    public static boolean ENABLED = false;
    public static boolean VERBOSE = false;

    public enum Category {
        INIT_ERROR,
        SPAWN_ERROR,
        MAP_ERROR,
        ZONE_ERROR,
        PATH_ERROR,
        QUEST_ERROR,
        COMBAT_ERROR,
        INVENTORY_ERROR,
        EQUIPMENT_ERROR,
        PERSISTENCE_ERROR,
        ACTIVITY_ERROR,
        STATE_ERROR
    }

    public static void logDecision(BotPlayerReal bot, String goalName, String reason, double reward, double risk, double cost, long execTimeMs) {
        if (!ENABLED || !VERBOSE) return;
        if (bot == null) return;
        System.out.printf("[AI-GOAL] Bot '%s' (ID %d, Lv %d) -> Goal: %s | Reason: %s | Score(Rew=%.1f, Risk=%.1f, Cost=%.1f) | %dms%n",
                bot.name, bot.IDPlayer, bot.level, goalName, reason, reward, risk, cost, execTimeMs);
    }

    public static void logStateTransition(BotPlayerReal bot, String oldState, String newState, String reason) {
        if (!ENABLED || !VERBOSE) return;
        if (bot == null) return;
        System.out.printf("[AI-STATE] Bot '%s' State: %s -> %s (Reason: %s)%n",
                bot.name, oldState, newState, reason);
    }

    public static void logStuckRecovery(BotPlayerReal bot, String stuckType, String actionTaken) {
        if (!ENABLED) return;
        if (bot == null) return;
        System.out.printf("[AI-STUCK] Bot '%s' (ID %d, Lv %d) Map %s Pos(%d,%d) -> StuckType: %s | Recovery: %s%n",
                bot.name, bot.IDPlayer, bot.level,
                bot.map != null && bot.map.template != null ? bot.map.template.id : "null",
                bot.x, bot.y, stuckType, actionTaken);
    }

    public static void logError(Category category, BotPlayerReal bot, String action, String reason, String recovery) {
        if (!ENABLED) return;
        if (bot == null) {
            System.err.printf("[AI-ERROR][%s] Bot: null | Action: %s | Reason: %s | Recovery: %s%n",
                    category, action, reason, recovery);
            return;
        }

        QuestP qp = bot.getMainQuest();
        String questInfo = (qp != null && qp.template != null)
                ? String.format("Quest %d (status=%d, lvReq=%d)", qp.template.id, qp.template.statusQuest, qp.template.lvRequest)
                : "None";

        String pathInfo = (bot.currentPath != null) ? bot.currentPath.toString() : "null";
        int currentMapId = (bot.map != null && bot.map.template != null) ? bot.map.template.id : -1;
        int zoneId = (bot.map != null) ? bot.map.zone_id : -1;

        StringBuilder sb = new StringBuilder();
        sb.append("\n==================== [AI-DIAGNOSTIC TRACE] ====================\n");
        sb.append(String.format("CATEGORY    : %s%n", category));
        sb.append(String.format("BOT         : %s (ID: %d, Lv: %d, Class: %d)%n", bot.name, bot.IDPlayer, bot.level, bot.clazz));
        sb.append(String.format("STATE       : %s%n", bot.state));
        sb.append(String.format("QUEST       : %s%n", questInfo));
        sb.append(String.format("CURRENT MAP : %d (Zone: %d, Pos: %d, %d)%n", currentMapId, zoneId, bot.x, bot.y));
        sb.append(String.format("TARGET MAP  : %d%n", bot.targetMapId));
        sb.append(String.format("PATH        : %s (Index: %d)%n", pathInfo, bot.pathIndex));
        sb.append(String.format("ACTION      : %s%n", action));
        sb.append(String.format("RESULT      : FAIL%n"));
        sb.append(String.format("REASON      : %s%n", reason));
        sb.append(String.format("RECOVERY    : %s%n", recovery));
        sb.append("===============================================================\n");

        System.err.print(sb.toString());
    }
}
