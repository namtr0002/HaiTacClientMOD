package bot.botplayer.ai.nextgen.goals;

import bot.botplayer.ai.nextgen.*;

public class GoalQuestMain implements IGoal {

    @Override
    public String getName() { return "QUEST"; }

    @Override
    public double evaluateUtility(GoalContext ctx, BotPersonality personality) {
        if (ctx.hasDoableQuest) {
            double base = 85.0;
            if (personality.archetype == BotPersonality.Archetype.HARDCORE_FARMER) {
                base = 90.0;
            } else if (personality.archetype == BotPersonality.Archetype.CASUAL_PLAYER) {
                base = 80.0;
            } else {
                base = 75.0;
            }
            return base * personality.getGoalWeightMultiplier(getName());
        }
        return 0.0;
    }

    @Override
    public ActionPlan planActions(GoalContext ctx) {
        ActionPlan plan = new ActionPlan(getName());
        plan.addAction(new BotAction("AdvanceQuestAction", 20000) {
            @Override
            public boolean execute(bot.botplayer.BotPlayerReal bot) {
                return !bot.getQuestController().tryAdvanceQuest();
            }
        });
        return plan;
    }

    @Override
    public long getCooldownMs() { return 1000L; } // 1 second cooldown

    @Override
    public double getRisk() { return 0.5; }

    @Override
    public double getCost() { return 1.0; }
}
