package bot.botplayer.ai.nextgen.goals;

import bot.botplayer.BotPlayerReal;
import bot.botplayer.ai.Pathfinder;
import bot.botplayer.ai.nextgen.*;

import java.util.List;

/**
 * GoalTransportCargo — Goal AI Next-Gen cho Bot Vận Chuyển Hàng (Ship buôn hàng).
 * 
 * Lộ trình an toàn:
 *  - Nhận hàng tại Làng Cối Xay Gió (Map 1) -> Tải lạc đà Ship_pet (Đặt cọc 10.000 Beri/Vàng).
 *  - Tự động tìm đường BFS bỏ qua 15 map boss nguy hiểm.
 *  - Di chuyển lần lượt qua các làng, giao hàng tại NPC lái buôn và nhận 2.000.000 - 7.000.000 Vàng.
 */
public class GoalTransportCargo implements IGoal {

    private static final int[] CARGO_VILLAGES = {1, 9, 17, 25, 33, 41, 49};
    private boolean hasCargo = false;
    private int currentTargetMap = -1;

    @Override
    public String getName() {
        return "TRANSPORT_CARGO";
    }

    @Override
    public double evaluateUtility(GoalContext ctx, BotPersonality personality) {
        if (ctx == null || ctx.bot == null) return 0.0;
        if (hasCargo) return 85.0 * personality.getGoalWeightMultiplier(getName());
        if (ctx.canDoTransport && ctx.bot.vang >= 10000 && !ctx.hasDoableQuest) {
            return 35.0 * personality.getGoalWeightMultiplier(getName());
        }
        return 0.0;
    }

    @Override
    public ActionPlan planActions(GoalContext ctx) {
        ActionPlan plan = new ActionPlan(getName());
        plan.addAction(new BotAction("ExecuteTransportCargoAction", 30000) {
            @Override
            public boolean execute(BotPlayerReal bot) {
                if (bot == null || bot.map == null || bot.map.template == null || bot.isdie) {
                    return true;
                }

                int currentMap = bot.map.template.id;

                // 1. Nếu chưa có hàng
                if (!hasCargo) {
                    // Nếu đang ở một map làng -> Nhận hàng tại làng đó
                    if (map.MapCanGoTo.isVillageMap(currentMap)) {
                        if (bot.vang >= 10000) {
                            bot.vang -= 10000;
                            hasCargo = true;
                            // Chọn làng đích tiếp theo
                            int nextVillage = 9;
                            for (int i = 0; i < CARGO_VILLAGES.length; i++) {
                                if (CARGO_VILLAGES[i] == currentMap) {
                                    nextVillage = CARGO_VILLAGES[(i + 1) % CARGO_VILLAGES.length];
                                    break;
                                }
                            }
                            currentTargetMap = nextVillage;
                            bot.targetMapId = currentTargetMap;
                            bot.currentPath = Pathfinder.findTransportPath(currentMap, currentTargetMap);
                            bot.pathIndex = 0;
                        } else {
                            return true;
                        }
                    } else {
                        // Chưa ở làng -> Đi đến làng gần nhất để nhận hàng
                        int startVillage = map.Zone.getVillageMapId(currentMap);
                        if (startVillage <= 0) startVillage = 1;
                        if (bot.currentPath == null || bot.targetMapId != startVillage) {
                            bot.targetMapId = startVillage;
                            bot.currentPath = Pathfinder.findTransportPath(currentMap, startVillage);
                            bot.pathIndex = 0;
                        }
                        bot.getMovementController().traversePath();
                        return false;
                    }
                }

                // 2. Đang chở hàng -> Di chuyển đến làng đích
                if (hasCargo) {
                    if (currentMap == currentTargetMap) {
                        // Đã tới làng đích -> Giao hàng & nhận thưởng
                        long rewardBeri = 2000000L + (long) (Math.random() * 5000000L);
                        bot.vang += rewardBeri;
                        hasCargo = false;
                        currentTargetMap = -1;

                        int trainMap = bot.getTrainingMapId();
                        bot.targetMapId = trainMap;
                        bot.currentPath = Pathfinder.findPath(currentMap, trainMap);
                        bot.pathIndex = 0;
                        bot.state = "TRAIN_LEVEL";
                        return true;
                    }

                    // Chưa tới làng đích -> Tiếp tục chạy lộ trình an toàn
                    if (bot.currentPath == null || bot.targetMapId != currentTargetMap) {
                        bot.targetMapId = currentTargetMap;
                        bot.currentPath = Pathfinder.findTransportPath(currentMap, currentTargetMap);
                        bot.pathIndex = 0;
                    }
                    bot.getMovementController().traversePath();
                }

                return false;
            }
        });
        return plan;
    }

    @Override
    public long getCooldownMs() {
        return 180000; // 3 phút cooldown giữa các chuyến buôn
    }

    @Override
    public double getRisk() {
        return 1.5;
    }

    @Override
    public double getCost() {
        return 1.0;
    }
}
