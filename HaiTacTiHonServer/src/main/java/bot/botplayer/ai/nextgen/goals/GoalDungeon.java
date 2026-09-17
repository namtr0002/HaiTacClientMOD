package bot.botplayer.ai.nextgen.goals;

import bot.botplayer.ai.nextgen.*;
import mob.Mob;

public class GoalDungeon implements IGoal {

    @Override
    public String getName() { return "DUNGEON"; }

    @Override
    public double evaluateUtility(GoalContext ctx, BotPersonality personality) {
        if (ctx.canDoDungeon) {
            return 70.0 * personality.getGoalWeightMultiplier(getName());
        }
        return 0.0;
    }

    @Override
    public ActionPlan planActions(GoalContext ctx) {
        ActionPlan plan = new ActionPlan(getName());
        plan.addAction(new BotAction("ExecuteDungeonAction", 60000) {
            @Override
            public boolean execute(bot.botplayer.BotPlayerReal bot) {
                if (bot.map == null || bot.map.template == null) return true;
                
                if (!map.Zone.is_map_dungeon(bot.map.template.id)) {
                    if (bot.dungeon == null && bot.aidonMax > 0) {
                        byte mode = (byte) Math.min((bot.level - 30) / 10, 11);
                        if (mode < 0) mode = 0;
                        map.zones.MapAiDon.createDungeon(bot, mode);
                    }
                    if (bot.dungeon != null) {
                        try {
                            bot.dungeon.join(bot);
                        } catch (Exception e) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                    return false;
                } else {
                    Mob dungMob = bot.getCombatController().findBestMob();
                    if (dungMob != null) {
                        double dist = Math.hypot(dungMob.x - bot.x, dungMob.y - bot.y);
                        if (dist > 80) {
                            bot.getMovementController().moveTowards(dungMob.x, dungMob.y);
                        } else {
                            bot.getCombatController().attackTarget(dungMob);
                        }
                        return false;
                    } else {
                        int nextDungeonMapId = bot.map.template.id + 1;
                        if (nextDungeonMapId > 175) {
                            return true; // Finished dungeon
                        } else {
                            bot.targetMapId = nextDungeonMapId;
                            bot.getMovementController().traversePath();
                            return false;
                        }
                    }
                }
            }
        });
        return plan;
    }

    @Override
    public long getCooldownMs() { return 600000; }

    @Override
    public double getRisk() { return 3.0; }

    @Override
    public double getCost() { return 2.0; }
}
