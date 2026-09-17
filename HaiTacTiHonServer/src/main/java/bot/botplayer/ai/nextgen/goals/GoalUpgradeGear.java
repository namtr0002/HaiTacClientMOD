package bot.botplayer.ai.nextgen.goals;

import bot.botplayer.ai.nextgen.*;

public class GoalUpgradeGear implements IGoal {

    @Override
    public String getName() { return "UPGRADE"; }

    @Override
    public double evaluateUtility(GoalContext ctx, BotPersonality personality) {
        if (ctx.bot.getEquipmentController().needsUpgrade()) {
            return 60.0;
        }
        return 0.0;
    }

    @Override
    public ActionPlan planActions(GoalContext ctx) {
        ActionPlan plan = new ActionPlan(getName());
        plan.addAction(new BotAction("UpgradeGearAction", 15000) {
            @Override
            public boolean execute(bot.botplayer.BotPlayerReal bot) {
                bot.getEquipmentController().autoSocketGems();
                bot.getEquipmentController().autoProcessDevilFruitsAndChests();
                bot.getEquipmentController().autoHoanMyGear();
                bot.getEquipmentController().autoKichAnGear();
                return true;
            }
        });
        return plan;
    }

    @Override
    public long getCooldownMs() { return 120000; }

    @Override
    public double getRisk() { return 0.5; }

    @Override
    public double getCost() { return 2.0; }
}
