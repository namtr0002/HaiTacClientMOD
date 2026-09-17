package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotPlayerReal;
import core.ZUtil;

/**
 * TickScheduler — Quản lý lịch tick thông minh cho bot (LOD AI & Sleep State).
 * Giúp tối ưu CPU khi có hàng ngàn bot chạy cùng lúc.
 */
public class TickScheduler {

    public static long calculateNextTickDelay(BotPlayerReal bot) {
        boolean hasRealPlayer = bot.map != null && BotPlayerManager.hasRealPlayer(bot.map);
        long now = System.currentTimeMillis();

        if (!hasRealPlayer) {
            // SLEEP STATE: Không có người chơi thật trong zone -> tick rất chậm (2.0s - 3.5s)
            return 2000 + ZUtil.random(1500);
        }

        // ACTIVE STATE: Có người chơi thật đang nhìn -> tick nhanh theo chế độ bot
        if (bot.botMode == BotPlayerReal.BotMode.GRIND) {
            return 200 + ZUtil.random(250); // 200 - 450ms
        } else {
            return 400 + ZUtil.random(400); // 400 - 800ms
        }
    }
}
