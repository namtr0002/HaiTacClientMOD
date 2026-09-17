package bot.botplayer.ai;

import bot.botplayer.BotPlayerReal;
import model.Player;

/**
 * BotPersistenceController — Quản lý việc lưu (save/flush) và nạp (load) dữ liệu bot từ DB players_bot.
 */
public class BotPersistenceController {

    private final BotPlayerReal bot;

    public BotPersistenceController(BotPlayerReal bot) {
        this.bot = bot;
    }

    /**
     * Nạp toàn bộ dữ liệu của bot từ bảng players_bot.
     */
    public boolean setupFromBotTable() {
        return bot.setup("players_bot");
    }

    /**
     * Lưu thông tin bot vào bảng players_bot (bất đồng bộ để không chặn game loop).
     */
    public boolean saveBot() {
        network.GlobalThreadManager.getPlayerExecutor().execute(() -> {
            try {
                bot.flush(bot, false, "players_bot");
            } catch (Exception ignored) {}
        });
        return true;
    }
}
