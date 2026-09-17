package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerReal;
import bot.botplayer.ai.nextgen.goals.*;
import java.util.*;

/**
 * GoalManager — Quản lý tập các Goal và điều phối việc chấm điểm Utility cho bot.
 */
public class GoalManager {

    private final List<IGoal> registeredGoals = new ArrayList<>();
    private final Map<String, Long> goalCooldowns = new HashMap<>();

    public GoalManager() {
        // Register all available Next-Gen Goals
        registeredGoals.add(new GoalShopInventory());
        registeredGoals.add(new GoalQuestMain());
        registeredGoals.add(new GoalDungeon());
        registeredGoals.add(new GoalWorldBoss());
        registeredGoals.add(new GoalPvP());
        registeredGoals.add(new GoalTransportCargo());
        registeredGoals.add(new GoalUpgradeGear());
        registeredGoals.add(new GoalClanActivity());
        registeredGoals.add(new GoalSocialize());
        registeredGoals.add(new GoalRestAFK());
        registeredGoals.add(new GoalTrainLevel()); // Default fallback goal
    }

    public IGoal evaluateAndSelectGoal(BotPlayerReal bot, BotPersonality personality) {
        GoalContext ctx = new GoalContext(bot);
        long now = System.currentTimeMillis();

        List<IGoal> availableGoals = new ArrayList<>();
        for (IGoal goal : registeredGoals) {
            Long lastTime = goalCooldowns.get(goal.getName());
            if (lastTime == null || (now - lastTime >= goal.getCooldownMs())) {
                availableGoals.add(goal);
            }
        }

        IGoal selectedGoal = DecisionEngine.selectBestGoal(bot, availableGoals, ctx, personality);
        if (selectedGoal != null) {
            goalCooldowns.put(selectedGoal.getName(), now);
        }
        return selectedGoal;
    }
}
