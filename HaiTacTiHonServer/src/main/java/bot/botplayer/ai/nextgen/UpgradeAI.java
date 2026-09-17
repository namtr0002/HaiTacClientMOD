package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerReal;

/**
 * UpgradeAI — Nâng cấp đồ, khảm đá, đập đồ, hoàn mỹ theo ngân sách và xác suất dừng an toàn.
 */
public class UpgradeAI {

    public static void processUpgrades(BotPlayerReal bot) {
        if (bot.getEquipmentController().needsUpgrade()) {
            bot.getEquipmentController().autoSocketGems();
            bot.getEquipmentController().autoProcessDevilFruitsAndChests();
            bot.getEquipmentController().autoHoanMyGear();
            bot.getEquipmentController().autoKichAnGear();
        }
    }
}
