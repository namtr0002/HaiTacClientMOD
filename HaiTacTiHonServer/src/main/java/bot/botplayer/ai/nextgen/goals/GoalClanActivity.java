package bot.botplayer.ai.nextgen.goals;

import bot.botplayer.ai.nextgen.*;
import bot.botplayer.BotBangHaiTac;

public class GoalClanActivity implements IGoal {

    @Override
    public String getName() { return "CLAN"; }

    @Override
    public double evaluateUtility(GoalContext ctx, BotPersonality personality) {
        if (ctx.bot.clan != null) {
            activities.TimedDungeonManager.ScheduleConfig pvpBang = activities.TimedDungeonManager.gI().getConfigs().get("PVP_BANG");
            activities.TimedDungeonManager.ScheduleConfig tckl = activities.TimedDungeonManager.gI().getConfigs().get("TRAN_CHIEN_KHONG_LO");
            activities.TimedDungeonManager.ScheduleConfig tlbk = activities.TimedDungeonManager.gI().getConfigs().get("THU_LINH_BIEN_KHOI");
            boolean isDungeonRunning = (pvpBang != null && pvpBang.state == activities.TimedDungeonManager.ScheduleConfig.STATE_RUNNING)
                    || (tckl != null && tckl.state == activities.TimedDungeonManager.ScheduleConfig.STATE_RUNNING)
                    || (tlbk != null && tlbk.state == activities.TimedDungeonManager.ScheduleConfig.STATE_RUNNING)
                    || ctx.bot.clan.map_create != null;
            if (isDungeonRunning) {
                return 95.0 * personality.getGoalWeightMultiplier(getName());
            }
        }
        // Khi phó bản chưa mở, không chiếm quyền để GoalTrainLevel / GoalQuestMain điều khiển bot đi cày map ngang level
        return 0.0;
    }

    @Override
    public ActionPlan planActions(GoalContext ctx) {
        ActionPlan plan = new ActionPlan(getName());
        plan.addAction(new BotAction("ClanMissionAndDungeonAction", 30000) {
            @Override
            public boolean execute(bot.botplayer.BotPlayerReal bot) {
                if (bot.map == null) return true;

                // Nếu bot đang trong map phó bản băng (Trận Chiến Khổng Lồ hoặc PvP Băng) -> chiến đấu
                if (bot.map.map_little_garden != null || bot.map.pvpBang != null || bot.map.pvpBangMapFight != null) {
                    // Tìm mục tiêu kẻ địch hoặc quái trong phó bản
                    model.Player enemy = bot.getCombatController().findNearestPlayerToPVP();
                    if (enemy != null) {
                        double dist = Math.hypot(enemy.x - bot.x, enemy.y - bot.y);
                        if (dist > 80) {
                            bot.getMovementController().moveTowards(enemy.x, enemy.y);
                        } else {
                            bot.getCombatController().attackPlayer(enemy);
                        }
                        return false;
                    }

                    mob.Mob mobTarget = bot.getCombatController().findBestMob();
                    if (mobTarget != null) {
                        double dist = Math.hypot(mobTarget.x - bot.x, mobTarget.y - bot.y);
                        if (dist > 80) {
                            bot.getMovementController().moveTowards(mobTarget.x, mobTarget.y);
                        } else {
                            bot.getCombatController().attackTarget(mobTarget);
                        }
                        return false;
                    }
                    bot.getMovementController().moveRandom();
                    return false;
                }

                // Nếu không trong phó bản -> thực hiện hoạt động bang hội (nhiệm vụ, cống hiến, ghép trận)
                bot.getSocialController().tickClanActivity();
                BotBangHaiTac.tickClanActivity(bot);
                return true;
            }
        });
        return plan;
    }

    @Override
    public long getCooldownMs() { return 60000; }

    @Override
    public double getRisk() { return 0.5; }

    @Override
    public double getCost() { return 1.0; }
}
