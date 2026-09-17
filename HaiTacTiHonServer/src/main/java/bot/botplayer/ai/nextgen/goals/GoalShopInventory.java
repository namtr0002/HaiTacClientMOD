package bot.botplayer.ai.nextgen.goals;

import bot.botplayer.ai.nextgen.*;
import bot.botplayer.ai.Pathfinder;
import bot.botplayer.BotMarket;

import map.Zone;
import map.MapCanGoTo;

public class GoalShopInventory implements IGoal {

    @Override
    public String getName() { return "SHOP"; }

    @Override
    public double evaluateUtility(GoalContext ctx, BotPersonality personality) {
        if (ctx.needsPotionOrShop) {
            return 90.0;
        }
        return 0.0;
    }

    @Override
    public ActionPlan planActions(GoalContext ctx) {
        ActionPlan plan = new ActionPlan(getName());
        plan.addAction(new BotAction("TravelToTownAndShop", 30000) {
            @Override
            public boolean execute(bot.botplayer.BotPlayerReal bot) {
                if (bot.map == null || bot.map.template == null) return true;
                int curMapId = bot.map.template.id;
                int townMapId = Zone.getVillageMapId(curMapId);
                if (townMapId <= 0 || !MapCanGoTo.isVillageMap(townMapId)) {
                    townMapId = (bot.id_map_save > 0 && MapCanGoTo.isVillageMap(bot.id_map_save)) ? bot.id_map_save : 1;
                }

                if (curMapId != townMapId) {
                    if (bot.currentPath == null || bot.targetMapId != townMapId) {
                        bot.targetMapId = townMapId;
                        bot.currentPath = Pathfinder.findPath(curMapId, townMapId);
                        bot.pathIndex = 0;
                    }
                    bot.getMovementController().traversePath();
                    return false;
                } else {
                    BotMarket.sellTrashAtNpc(bot);
                    bot.getInventoryController().buyPotions();
                    bot.getInventoryController().autoExpandInventorySlots();
                    bot.getInventoryController().buyUpgradeMaterials();
                    
                    int trainMap = bot.getTrainingMapId();
                    bot.targetMapId = trainMap;
                    bot.currentPath = Pathfinder.findPath(curMapId, trainMap);
                    bot.pathIndex = 0;
                    bot.state = "TRAIN_LEVEL";
                    return true;
                }
            }
        });
        return plan;
    }

    @Override
    public long getCooldownMs() { return 5000; }

    @Override
    public double getRisk() { return 0.1; }

    @Override
    public double getCost() { return 0.2; }
}
