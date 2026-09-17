package bot.botplayer.ai.nextgen.goals;

import bot.botplayer.ai.nextgen.*;
import bot.botplayer.TrainBoss;

public class GoalWorldBoss implements IGoal {

    @Override
    public String getName() { return "WORLD_BOSS"; }

    @Override
    public double evaluateUtility(GoalContext ctx, BotPersonality personality) {
        if (ctx.canDoBoss && TrainBoss.canTrainBoss(ctx.bot)) {
            return 65.0 * personality.getGoalWeightMultiplier(getName());
        }
        return 0.0;
    }

    @Override
    public ActionPlan planActions(GoalContext ctx) {
        ActionPlan plan = new ActionPlan(getName());
        plan.addAction(new BotAction("FightWorldBossAction", 45000) {
            @Override
            public boolean execute(bot.botplayer.BotPlayerReal bot) {
                if (TrainBoss.canTrainBoss(bot)) {
                    TrainBoss.tick(bot);
                    if (bot.map != null && map.Zone.is_map_boss(bot.map.template.id)
                            && bot.getCombatController().isBossDeadInCurrentMap()) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
        });
        return plan;
    }

    @Override
    public long getCooldownMs() { return 300000; }

    @Override
    public double getRisk() { return 4.0; }

    @Override
    public double getCost() { return 2.5; }
}
