package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotPlayerReal;
import bot.botplayer.ai.Pathfinder;
import map.Zone;

/**
 * SmartLevelingEngine — Tự động tính toán chọn map, né khu đông, đổi khu khi bị KS.
 */
public class SmartLevelingEngine {

    public static void checkAndOptimizeLeveling(BotPlayerReal bot) {
        if (bot.map == null || bot.map.template == null) return;

        if (bot.getQuestController().hasUnfinishedQuest()) return;

        // 1. Kiểm tra mật độ người thật trong khu -> Nếu đông người thật, tự động nerf giảm bot
        int realPlayerCount = 0;
        int botCount = 0;
        if (bot.map.players != null) {
            for (int i = 0; i < bot.map.players.size(); i++) {
                model.Player p = bot.map.players.get(i);
                if (p != null) {
                    if (p.isDe || p instanceof model.DeTu || p instanceof bot.mercenary.MercenaryBot) continue;
                    if (p.isBot) botCount++;
                    else if (p.conn != null) realPlayerCount++;
                }
            }
        }

        // Nếu có người thật trong khu và số lượng bot > 2 (hoặc > 1 nếu có nhiều người thật) -> bot tự đổi khu/map nhường chỗ
        int maxAllowedBots = (realPlayerCount >= 2) ? 1 : 2;
        if (realPlayerCount > 0 && botCount > maxAllowedBots) {
            Zone alternativeZone = BotPlayerManager.findBestZoneForBot(bot, bot.map.template.id);
            if (alternativeZone != null && alternativeZone != bot.map) {
                try {
                    bot.leave();
                    bot.join(alternativeZone, bot.x, bot.y);
                    return;
                } catch (Exception ignored) {}
            }
        }

        // 2. Chuyển map tối ưu khi không làm nhiệm vụ
        int optimalMap = bot.getTrainingMapId();
        if (bot.map.template.id != optimalMap && "FARM".equals(bot.state)) {
            bot.targetMapId = optimalMap;
            bot.currentPath = Pathfinder.findPath(bot.map.template.id, optimalMap);
            bot.pathIndex = 0;
            bot.state = "PORTAL";
            return;
        }

        // 3. Đổi khu khi bị KS hoặc va chạm
        if (bot.getCombatController().ksCount >= 2) {
            bot.getCombatController().ksCount = 0;
            Zone alternativeZone = BotPlayerManager.findBestZoneForBot(bot, bot.map.template.id);
            if (alternativeZone != null && alternativeZone != bot.map) {
                try {
                    bot.leave();
                    bot.join(alternativeZone, bot.x, bot.y);
                } catch (Exception ignored) {}
            }
        }
    }
}
