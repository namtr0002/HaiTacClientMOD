package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotPlayerReal;

/**
 * GoalContext — Chứa thông tin trạng thái để phục vụ tính điểm Utility của Goal.
 */
public class GoalContext {
    public final BotPlayerReal bot;
    public final long currentTime;
    public final boolean hasRealPlayersInZone;
    public final boolean hasDoableQuest;
    public final boolean needsPotionOrShop;
    public final boolean canDoDungeon;
    public final boolean canDoBoss;
    public final boolean canDoPvp;
    public final boolean canDoTransport;
    public boolean hasCargo;
    public int cargoTripsCount;

    public GoalContext(BotPlayerReal bot) {
        this.bot = bot;
        this.currentTime = System.currentTimeMillis();
        this.hasRealPlayersInZone = bot.map != null && BotPlayerManager.hasRealPlayer(bot.map);
        this.hasDoableQuest = bot.getQuestController().hasDoableQuest();
        this.needsPotionOrShop = bot.getInventoryController().shouldSellTrash() || bot.getInventoryController().needsPotions();
        this.canDoDungeon = bot.level >= 30 && bot.aidonMax > 0;
        this.canDoBoss = bot.level >= 20;
        this.canDoPvp = bot.level >= 30;
        this.canDoTransport = bot.level >= 10;
        this.hasCargo = false;
        this.cargoTripsCount = 0;
    }
}
