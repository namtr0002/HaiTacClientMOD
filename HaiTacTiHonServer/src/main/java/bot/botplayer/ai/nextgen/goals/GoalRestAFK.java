package bot.botplayer.ai.nextgen.goals;

import bot.botplayer.ai.nextgen.*;

public class GoalRestAFK implements IGoal {

    @Override
    public String getName() { return "REST"; }

    @Override
    public double evaluateUtility(GoalContext ctx, BotPersonality personality) {
        if (core.ZUtil.random(1000) < 5) {
            return 40.0 * personality.getGoalWeightMultiplier(getName());
        }
        return 0.0;
    }

    @Override
    public ActionPlan planActions(GoalContext ctx) {
        ActionPlan plan = new ActionPlan(getName());
        plan.addAction(new BotAction("AFKRestAction", 15000) {
            @Override
            public boolean execute(bot.botplayer.BotPlayerReal bot) {
                // Simulates human standing still / AFK moment
                return true;
            }
        });
        return plan;
    }

    @Override
    public long getCooldownMs() { return 120000; }

    @Override
    public double getRisk() { return 0.0; }

    @Override
    public double getCost() { return 0.0; }
}
