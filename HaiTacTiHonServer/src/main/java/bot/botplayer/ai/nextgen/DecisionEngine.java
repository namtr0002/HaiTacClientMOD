package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerReal;
import java.util.*;

/**
 * DecisionEngine — Bộ máy chấm điểm Utility & chọn lựa Goal tối ưu nhất dựa trên toán học Utility AI.
 */
public class DecisionEngine {

    public static IGoal selectBestGoal(BotPlayerReal bot, List<IGoal> registeredGoals, GoalContext ctx, BotPersonality personality) {
        if (registeredGoals == null || registeredGoals.isEmpty()) {
            return null;
        }

        IGoal bestGoal = null;
        double maxUtility = -1.0;

        for (IGoal goal : registeredGoals) {
            double baseUtility = goal.evaluateUtility(ctx, personality);
            if (baseUtility <= 0) continue;

            double risk = goal.getRisk();
            double cost = goal.getCost();
            double personalityWeight = personality.getGoalWeightMultiplier(goal.getName());

            // Formula: Score = (Utility * Weight) / (Risk + Cost + 1.0)
            double finalScore = (baseUtility * personalityWeight) / (risk * (1.0 - personality.riskTolerance) + cost + 1.0);

            if (finalScore > maxUtility) {
                maxUtility = finalScore;
                bestGoal = goal;
            }
        }

        return bestGoal;
    }
}
