package bot.botplayer.ai.nextgen.goals;

import bot.botplayer.ai.nextgen.*;
import bot.botplayer.ai.Pathfinder;
import mob.Mob;

public class GoalTrainLevel implements IGoal {

    @Override
    public String getName() { return "TRAIN_LEVEL"; }

    @Override
    public double evaluateUtility(GoalContext ctx, BotPersonality personality) {
        double score = 70.0;
        if (!ctx.hasDoableQuest) {
            score += 20.0;
        }
        return score * personality.getGoalWeightMultiplier(getName());
    }

    @Override
    public ActionPlan planActions(GoalContext ctx) {
        ActionPlan plan = new ActionPlan(getName());
        plan.addAction(new BotAction("TrainMobAction", 15000) {
            @Override
            public boolean execute(bot.botplayer.BotPlayerReal bot) {
                if (bot.map == null) return true;

                int trainMapId = bot.getTrainingMapId();
                if (bot.map.template != null && bot.map.template.id != trainMapId) {
                    if (bot.currentPath == null || bot.targetMapId != trainMapId) {
                        bot.targetMapId = trainMapId;
                        bot.currentPath = Pathfinder.findPath(bot.map.template.id, trainMapId);
                        bot.pathIndex = 0;
                    }
                    bot.getMovementController().traversePath();
                    return false;
                }

                Mob target = bot.getCombatController().findBestMob();
                if (target != null) {
                    double dist = Math.hypot(target.x - bot.x, target.y - bot.y);
                    if (dist > 80) {
                        bot.getMovementController().moveTowards(target.x, target.y);
                    } else {
                        bot.getCombatController().attackTarget(target);
                    }
                } else {
                    bot.getMovementController().moveRandom();
                }
                return true;
            }
        });
        return plan;
    }

    @Override
    public long getCooldownMs() { return 1000; }

    @Override
    public double getRisk() { return 1.0; }

    @Override
    public double getCost() { return 1.0; }
}
