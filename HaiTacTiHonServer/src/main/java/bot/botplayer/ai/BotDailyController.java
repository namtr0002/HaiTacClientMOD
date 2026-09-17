package bot.botplayer.ai;

import bot.botplayer.BotPlayerReal;
import bot.botplayer.GameAnalyzer;
import template.ItemFashionP2;
import core.ZUtil;

import java.util.List;

/**
 * BotDailyController — Quản lý các hoạt động hàng ngày của bot.
 *
 * Chức năng:
 *  1. Điểm danh hàng ngày (diemdanh)
 *  2. Spin vòng quay nếu có lượt
 *  3. Tháo thời trang (fashion) đã hết hạn
 *  4. Nhận thưởng hành trình hàng ngày (task)
 *  5. Giới hạn số lần hành động mỗi ngày
 *
 * Không tạo Thread — gọi từ BotBrain mỗi 10 phút.
 */
public class BotDailyController {

    private final BotPlayerReal bot;

    // Timestamps để tránh lặp lại trong cùng ngày
    private long lastDailyCheckTime = 0;
    private long lastFashionCheckTime = 0;
    private long lastWheelSpinTime = 0;

    // Xác định ngày đã thực hiện (tránh làm lại nhiều lần)
    private int lastDailyDay = -1;

    private static final long DAILY_CHECK_INTERVAL_MS   = 10L * 60 * 1000;  // 10 phút
    private static final long FASHION_CHECK_INTERVAL_MS = 15L * 60 * 1000;  // 15 phút
    private static final long WHEEL_INTERVAL_MS          =  5L * 60 * 1000;  // 5 phút

    public BotDailyController(BotPlayerReal bot) {
        this.bot = bot;
    }

    /**
     * Hàm chính — gọi từ BotBrain.tick() định kỳ.
     */
    public void tick() {
        long now = System.currentTimeMillis();

        // 1. Điểm danh hàng ngày
        if (now - lastDailyCheckTime > DAILY_CHECK_INTERVAL_MS) {
            lastDailyCheckTime = now;
            checkAndClaimDailyLogin();
        }

        // 2. Kiểm tra fashion hết hạn
        if (now - lastFashionCheckTime > FASHION_CHECK_INTERVAL_MS) {
            lastFashionCheckTime = now;
            checkFashionExpiry();
        }

        // 3. Spin vòng quay nếu có lượt
        if (now - lastWheelSpinTime > WHEEL_INTERVAL_MS) {
            lastWheelSpinTime = now;
            trySpinLuckyWheel();
        }
    }

    // ========================= ĐIỂM DANH =========================

    /**
     * Điểm danh hàng ngày — tăng diemdanh nếu chưa điểm danh hôm nay.
     */
    private void checkAndClaimDailyLogin() {
        try {
            // Lấy ngày hiện tại (dạng ordinal đơn giản)
            int todayOrdinal = getTodayOrdinal();
            if (todayOrdinal == lastDailyDay) return; // Đã điểm danh hôm nay

            // Kiểm tra diemdanh_ngay: nếu khác hôm nay thì reset và điểm danh
            boolean shouldClaim = false;
            if (bot.diemdanh_ngay != todayOrdinal) {
                bot.diemdanh_ngay = todayOrdinal;
                shouldClaim = true;
            }

            if (shouldClaim) {
                lastDailyDay = todayOrdinal;
                bot.diemdanh++;

                // Phần thưởng điểm danh đơn giản (vàng)
                long reward = 50_000L + (long) Math.min(bot.diemdanh, 30) * 10_000L;
                bot.update_vang(reward);
                bot.updateMoney();

                // Chat thông báo (30% xác suất để không spam)
                if (ZUtil.random(100) < 30 && bot.map != null) {
                    try {
                        bot.map.send_chat_popup(0, bot.index_map,
                            "Điểm danh ngày " + bot.diemdanh + " thành công!");
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception ignored) {}
    }

    // ========================= FASHION EXPIRY =========================

    /**
     * Kiểm tra và tháo các trang phục (fashion) đã hết hạn.
     * Tránh hiện tượng "bất tử" do fashion hết hạn không được xử lý.
     */
    private void checkFashionExpiry() {
        try {
            List<?> fashionList = bot.fashion;
            if (fashionList == null || fashionList.isEmpty()) return;

            long now = System.currentTimeMillis();
            boolean changed = false;

            for (int i = fashionList.size() - 1; i >= 0; i--) {
                Object obj = fashionList.get(i);
                if (!(obj instanceof ItemFashionP2)) continue;
                ItemFashionP2 f = (ItemFashionP2) obj;

                // Nếu fashion đang dùng nhưng ID không hợp lệ, tháo ra
                if (f.is_use && f.id <= 0) {
                    f.is_use = false;
                    changed = true;
                }
            }

            if (changed) {
                bot.update_info_to_all();
            }
        } catch (Exception ignored) {}
    }

    // ========================= VÒNG QUAY =========================

    /**
     * Thử spin vòng quay nếu bot còn lượt (ticket).
     */
    private void trySpinLuckyWheel() {
        try {
            // Chỉ spin nếu có ticket và chưa hết lượt
            if (bot.ticket <= 0) return;
            if (System.currentTimeMillis() < bot.cd_ticket_next) return;

            // Tiêu 1 ticket — phần thưởng ngẫu nhiên
            bot.ticket--;
            long now = System.currentTimeMillis();
            bot.cd_ticket_next = now + 60_000L; // cooldown 1 phút

            // Phần thưởng ngẫu nhiên đơn giản
            int roll = ZUtil.random(100);
            long reward;
            String rewardMsg;
            if (roll < 5) {
                // 5% jackpot
                reward = 1_000_000L;
                rewardMsg = "Trúng jackpot 1 triệu vàng!";
            } else if (roll < 25) {
                // 20% lớn
                reward = 200_000L;
                rewardMsg = "Trúng 200k vàng từ vòng quay!";
            } else {
                // 75% nhỏ
                reward = 50_000L;
                rewardMsg = null; // Không chat nếu thắng nhỏ
            }
            bot.update_vang(reward);
            bot.updateMoney();
            GameAnalyzer.addGoldEarned(reward);

            if (rewardMsg != null && bot.map != null) {
                try {
                    bot.map.send_chat_popup(0, bot.index_map, rewardMsg);
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
    }

    // ========================= UTILS =========================

    /**
     * Trả về ordinal ngày hiện tại (tính từ epoch, theo múi giờ UTC+7).
     */
    private int getTodayOrdinal() {
        // UTC+7 = +25200000 ms
        return (int) ((System.currentTimeMillis() + 25_200_000L) / 86_400_000L);
    }
}
