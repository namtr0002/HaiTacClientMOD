package bot.botplayer.ai.nextgen.goals;

import bot.botplayer.ai.nextgen.*;
import model.Player;

public class GoalPvP implements IGoal {

    @Override
    public String getName() { return "PVP"; }

    @Override
    public double evaluateUtility(GoalContext ctx, BotPersonality personality) {
        if (!ctx.canDoPvp) return 0.0;
        
        // Ưu tiên cao khi đang trong bối cảnh map PvP thực sự hoặc là Bot ghép đấu PVP_QUEUE
        boolean isPvpMap = ctx.bot.map != null && (ctx.bot.map.map_vp != null 
                || map.zones.WorldWar.mapTranChienLon(ctx.bot.map.template.id) 
                || ctx.bot.map.map_little_garden != null 
                || ctx.bot.map.pvpBang != null);
        
        if (isPvpMap || ctx.bot.botType == bot.botplayer.BotPlayerReal.BotType.PVP_QUEUE) {
            return 60.0 * personality.getGoalWeightMultiplier(getName());
        }

        // Ở map train quái bình thường: thỉnh thoảng đăng ký hàng chờ PvP/Truy nã
        if (personality.pkInclination > 0.6 && core.ZUtil.random(100) < 25) {
            return 20.0 * personality.getGoalWeightMultiplier(getName());
        }
        return 0.0;
    }

    @Override
    public ActionPlan planActions(GoalContext ctx) {
        ActionPlan plan = new ActionPlan(getName());
        plan.addAction(new BotAction("PvPCombatAction", 30000) {
            @Override
            public boolean execute(bot.botplayer.BotPlayerReal bot) {
                bot.updateBotTypePk();
                boolean isPvpMap = bot.map != null && (bot.map.map_vp != null 
                        || map.zones.WorldWar.mapTranChienLon(bot.map.template.id) 
                        || bot.map.map_little_garden != null 
                        || bot.map.pvpBang != null);

                // Chỉ tìm đối thủ tấn công nếu đang trong map PvP hoặc chế độ PVP_QUEUE
                Player pTarget = isPvpMap ? bot.getCombatController().findNearestPlayerToPVP() : null;
                if (pTarget != null) {
                    int hpMax = bot.ability.get_hp_max(true);
                    if (bot.hp < hpMax * 2 / 10 && (bot.map == null || bot.map.map_vp == null)) {
                        bot.type_pk = -1;
                        return true;
                    } else if (bot.hp < hpMax * 4 / 10) {
                        bot.getMovementController().kiteTarget(pTarget.x, pTarget.y);
                    } else {
                        double dist = Math.hypot(pTarget.x - bot.x, pTarget.y - bot.y);
                        if (dist > 80) {
                            bot.getMovementController().moveTowards(pTarget.x, pTarget.y);
                        } else {
                            bot.getCombatController().attackPlayer(pTarget);
                        }
                    }
                    return false;
                } else {
                    // Nếu không ở map PvP -> tham gia hàng chờ ghép đấu PvP hoặc Truy Nã
                    if (bot.level >= 30) {
                        if (core.ZUtil.random(100) < 50) {
                            activities.Wanted.add_player_wait(bot);
                        } else {
                            try {
                                activities.Pvp.start_find(bot);
                            } catch (Exception ignored) {}
                        }
                    }
                    bot.type_pk = -1;
                    return true;
                }
            }
        });
        return plan;
    }

    @Override
    public long getCooldownMs() { return 600000; }

    @Override
    public double getRisk() { return 5.0; }

    @Override
    public double getCost() { return 3.0; }
}
