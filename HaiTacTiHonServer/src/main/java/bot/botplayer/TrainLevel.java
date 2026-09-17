package bot.botplayer;

/**
 * TrainLevel — Khởi tạo hệ thống bot farming.
 *
 * Lưu ý: KHÔNG tạo Thread mới ở đây.
 * Bot chạy trong map thread sẵn có (Zone update loop).
 * Delegate hoàn toàn sang BotPlayerManager.
 */
public class TrainLevel {

    /**
     * Khởi động bot farming system.
     * Gọi sau khi map đã khởi tạo xong (server startup).
     * KHÔNG dùng Thread — BotPlayerManager.init() chạy đồng bộ.
     */
    public static void init() {
        // TrainLevel: Starting Bot Farming System
        BotPlayerManager.init();
    }
}
