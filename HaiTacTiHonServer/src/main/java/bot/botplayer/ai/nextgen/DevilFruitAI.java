package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerReal;

/**
 * DevilFruitAI — Đổi trái, nâng cấp trái ác quỷ, ưu tiên trái theo class/build.
 */
public class DevilFruitAI {

    public static void evaluateAndSwapDevilFruit(BotPlayerReal bot) {
        // Evaluate devil fruit inventory vs current equipped fruit
        bot.getEquipmentController().autoProcessDevilFruitsAndChests();
    }
}
