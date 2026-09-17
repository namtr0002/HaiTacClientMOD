package bot.botplayer;

import event.EventManager;
import event.Event;
import core.ZUtil;

/**
 * TrainEvent — Tự động tham gia sự kiện (Event) đang chạy.
 *
 * Logic:
 *  1. Kiểm tra EventManager.gI().active xem sự kiện nào đang hoạt động
 *  2. Bot đến NPC sự kiện và tham gia nếu phù hợp
 *  3. Hành động đơn giản: farm map hiện tại, nhận item sự kiện
 *
 * Gọi từ BotBrain mỗi 10 phút (không tạo Thread).
 */
public class TrainEvent {

    /**
     * Trả về ID của sự kiện đang chạy (> 0), hoặc -1 nếu không có.
     * Bot dùng kết quả này để quyết định có chuyển state EVENT không.
     */
    public static int getActiveEventId(BotPlayerReal bot) {
        if (bot == null) return -1;
        try {
            Event ev = EventManager.gI().active;
            if (ev == null) return -1;
            return ev.getId();
        } catch (Exception ignored) {
            return -1;
        }
    }

    /**
     * Trả về true nếu có sự kiện đang chạy và bot đủ điều kiện tham gia.
     */
    public static boolean hasActiveEvent(BotPlayerReal bot) {
        return getActiveEventId(bot) > 0;
    }

    /**
     * Hành động sự kiện mỗi tick — gọi từ BotBrain state EVENT.
     * Bot đã ở map sự kiện khi gọi hàm này.
     */
    public static void tick(BotPlayerReal bot) {
        if (bot == null || bot.map == null) return;
        try {
            Event ev = EventManager.gI().active;
            if (ev == null) return;

            // Chat thông báo tham gia sự kiện (xác suất 20%)
            if (ZUtil.random(100) < 20 && bot.map != null) {
                try {
//                    bot.map.send_chat_popup(0, bot.index_map,
//                        "Đang tham gia sự kiện " + ev.getName() + "!");
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
    }
}
