package bot.botplayer;

import model.Player;
import network.Message;
import network.SessionManager;
import core.ZUtil;

import java.util.concurrent.ConcurrentHashMap;

/**
 * BotWorldAnnounce — Phát thông báo kênh thế giới từ bot.
 *
 * Nguyên tắc:
 *  - Chỉ broadcast tới tất cả người chơi thật đang online (PLAYERS_MAP)
 *  - Mỗi bot có cooldown riêng để tránh spam (tối thiểu 5 phút / bot)
 *  - Các sự kiện: đập đồ lên +5/+8/+10, lên level mốc, tạo clan, chiêu mộ clan
 *  - Format nhìn như hệ thống thông báo thật
 */
public class BotWorldAnnounce {

    /** Cooldown tối thiểu giữa 2 thông báo của cùng 1 bot: 5 phút */
    private static final long BOT_ANNOUNCE_COOLDOWN_MS = 5 * 60 * 1000L;

    /** Cooldown toàn cục giữa 2 thông báo bất kỳ: 30 giây (tránh spam kênh thế giới) */
    private static final long GLOBAL_COOLDOWN_MS = 30_000L;

    private static final ConcurrentHashMap<String, Long> botLastAnnounceTime = new ConcurrentHashMap<>();
    private static volatile long lastGlobalAnnounceTime = 0;

    // ========================= PUBLIC API =========================

    /**
     * Thông báo đập đồ thành công.
     * @param botName   Tên bot
     * @param itemName  Tên trang bị
     * @param levelUp   Cấp tăng cường (+5/+8/+10)
     */
    public static void announceGearUpgrade(String botName, String itemName, int levelUp) {
        if (levelUp < 5) return; // Chỉ thông báo từ +5 trở lên
        if (!canAnnounce(botName)) return;

        String[] templates;
        if (levelUp >= 10) {
            templates = new String[]{
                "[Hệ thống] " + botName + " vừa đập thành công " + itemName + " lên +" + levelUp + "! Tuyệt đỉnh!!",
                "[Thế giới] " + botName + " đã tạo ra " + itemName + " +" + levelUp + " huyền thoại!",
                "" + botName + " đập " + itemName + " thành công +" + levelUp + "! Kỹ năng đỉnh cao!"
            };
        } else if (levelUp >= 8) {
            templates = new String[]{
                "[Hệ thống] " + botName + " đập thành công " + itemName + " +" + levelUp + "! Xịn sò!",
                "" + botName + " cường hóa " + itemName + " +" + levelUp + " thành công!",
                "[Thế giới] " + botName + " vừa đập " + itemName + " lên +" + levelUp + "!"
            };
        } else {
            templates = new String[]{
                "" + botName + " đập " + itemName + " +" + levelUp + " thành công!",
                "[Hệ thống] " + botName + " cường hóa " + itemName + " +" + levelUp + " thành công!",
                botName + " đập " + itemName + " +" + levelUp + " thành công, hên thật!"
            };
        }

        String msg = templates[ZUtil.random(templates.length)];
        sendWorldMessage(msg);
        markAnnounced(botName);
    }

    /**
     * Thông báo lên level mốc (10, 20, 30, 50, 70, 100...).
     */
    public static void announceLevelUp(String botName, int level) {
        if (!isMilestoneLevel(level)) return;
        if (!canAnnounce(botName)) return;

        String[] templates = {
            "[Hệ thống] " + botName + " vừa đạt level " + level + "! Chúc mừng!",
            "" + botName + " lên cấp " + level + "! Thăng tiến vượt bậc!",
            "[Thế giới] " + botName + " đã lên level " + level + "! Cày không biết mệt!"
        };

        sendWorldMessage(templates[ZUtil.random(templates.length)]);
        markAnnounced(botName);
    }

    /**
     * Thông báo tạo clan mới.
     */
    public static void announceClanCreated(String botName, String clanName) {
        if (!canAnnounce(botName)) return;

        String[] templates = {
            "[Hệ thống] Băng hải tặc mới [" + clanName + "] do " + botName + " lập ra! Chiêu mộ thành viên!",
            "" + botName + " vừa thành lập băng [" + clanName + "]! AE join nhé!",
            "[Thế giới] Bang mới [" + clanName + "] ra đời! Trưởng bang: " + botName
        };

        sendWorldMessage(templates[ZUtil.random(templates.length)]);
        markAnnounced(botName);
    }

    /**
     * Thông báo chiêu mộ thành viên clan.
     * Xác suất thấp hơn để tránh spam.
     */
    public static void announceRecruitment(String botName, String clanName) {
        if (!canAnnounce(botName)) return;
        if (ZUtil.random(100) >= 30) return; // Chỉ 30% xác suất

        String[] templates = {
            "[" + clanName + "] đang tuyển thành viên! Lv 20+ inbox " + botName + " nhé!",
            "Bang [" + clanName + "] cần thêm người! Chiến phó bản / cày boss cùng nhau!",
            "[Thế giới] Tuyển mem bang [" + clanName + "]! Đặc quyền: phó bản, buff bang, đua top!"
        };

        sendWorldMessage(templates[ZUtil.random(templates.length)]);
        markAnnounced(botName);
    }

    /**
     * Thông báo tùy chỉnh — dùng cho các sự kiện đặc biệt khác.
     */
    public static void announceCustom(String botName, String message) {
        if (!canAnnounce(botName)) return;
        sendWorldMessage(message);
        markAnnounced(botName);
    }

    // ========================= CORE SEND =========================

    /**
     * Gửi tin nhắn kênh thế giới đến TẤT CẢ người chơi đang online.
     * Dùng SessionManager.PLAYERS_MAP để iterate qua toàn bộ người chơi.
     */
    public static void sendWorldMessage(String text) {
        if (text == null || text.isEmpty()) return;
        try {
            // PLAYERS_MAP là ConcurrentHashMap<Integer, Player> (key = IDPlayer)
            for (Player p : SessionManager.PLAYERS_MAP.values()) {
                if (p == null || p.isBot) continue;
                if (p.conn == null) continue;
                try {
                    // Gửi chat type=0 với sender id=-1 (system message không có nguồn)
                    Message m = new Message(17);
                    m.writer().writeShort(-1); // -1 = system
                    m.writer().writeByte(0);
                    m.writer().writeUTF(text);
                    p.addmsg(m);
                    m.cleanup();
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
    }

    // ========================= COOLDOWN =========================

    private static boolean canAnnounce(String botName) {
        long now = System.currentTimeMillis();
        // Kiểm tra global cooldown
        if (now - lastGlobalAnnounceTime < GLOBAL_COOLDOWN_MS) return false;
        // Kiểm tra per-bot cooldown
        Long lastTime = botLastAnnounceTime.get(botName);
        return lastTime == null || (now - lastTime) >= BOT_ANNOUNCE_COOLDOWN_MS;
    }

    private static void markAnnounced(String botName) {
        long now = System.currentTimeMillis();
        botLastAnnounceTime.put(botName, now);
        lastGlobalAnnounceTime = now;
    }

    private static boolean isMilestoneLevel(int level) {
        return level == 10 || level == 20 || level == 30 || level == 40
            || level == 50 || level == 60 || level == 70 || level == 80
            || level == 90 || level == 100;
    }
}
