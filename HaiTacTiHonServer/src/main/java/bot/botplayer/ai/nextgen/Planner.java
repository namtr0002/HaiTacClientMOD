package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerReal;

/**
 * Planner — Chuyển đổi Goal đã chọn thành ActionPlan cụ thể.
 */
public class Planner {

    public static ActionPlan createPlan(BotPlayerReal bot, IGoal goal) {
        if (goal == null) return null;
        GoalContext ctx = new GoalContext(bot);
        return goal.planActions(ctx);
    }
}
