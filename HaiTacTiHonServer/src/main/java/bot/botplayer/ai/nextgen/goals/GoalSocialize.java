package bot.botplayer.ai.nextgen.goals;

import bot.botplayer.ai.nextgen.*;

public class GoalSocialize implements IGoal {

    @Override
    public String getName() { return "SOCIAL"; }

    @Override
    public double evaluateUtility(GoalContext ctx, BotPersonality personality) {
        if (ctx.hasRealPlayersInZone) {
            return 30.0 * personality.getGoalWeightMultiplier(getName());
        }
        return 0.0;
    }

    @Override
    public ActionPlan planActions(GoalContext ctx) {
        ActionPlan plan = new ActionPlan(getName());
        plan.addAction(new BotAction("SocialChatAction", 10000) {
            @Override
            public boolean execute(bot.botplayer.BotPlayerReal bot) {
                bot.getSocialController().tryChat();
                return true;
            }
        });
        return plan;
    }

    @Override
    public long getCooldownMs() { return 45000; }

    @Override
    public double getRisk() { return 0.0; }

    @Override
    public double getCost() { return 0.1; }
}
