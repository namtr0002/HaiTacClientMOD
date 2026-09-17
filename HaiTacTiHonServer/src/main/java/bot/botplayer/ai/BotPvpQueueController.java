package bot.botplayer.ai;

import activities.Wanted;
import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotPlayerReal;
import model.Player;
import core.ZUtil;

/**
 * BotPvpQueueController — Bot chuyên ghép trận PVP / Truy Nã.
 *
 * Nhiệm vụ:
 *  - Bot loại PVP_QUEUE treo tại map 119 (Truy Nã) hoặc arena PVP
 *  - Tự động tham gia hàng chờ ghép trận để tăng tỉ lệ ghép thành công cho người thật
 *  - Chat ngẫu nhiên như người đang chờ đấu
 *  - Khi ghép được: tham chiến bình thường (delegate sang BotCombatController)
 *  - Sau khi trận kết thúc: quay lại treo chờ ghép tiếp
 */
public class BotPvpQueueController {

    private final BotPlayerReal bot;

    private static final int MAP_TRUY_NA = 119;

    // Chat pool khi đứng chờ ghép
    private static final String[] LOBBY_CHATS = {
        "Ai solo không nào?",
        "Ghép trận đi ae ơi!",
        "Chờ mãi chưa có đối thủ...",
        "Truy nã hôm nay đông chưa?",
        "Ai dám 1v1 không? Trao điểm nhé!",
        "Ghép với tao đi, không troll đâu!",
        "Đang chờ ghép, ae vào nhanh lên!",
        "Solo ai nào dám!",
        "Vào đây ghép luôn ae ơi",
        "Chờ người ghép mà lâu quá..."
    };

    // Trạng thái
    private long lastQueueJoinTime = 0;
    private long lastChatTime = 0;
    private long lastIdleMoveTime = 0;
    private boolean inQueue = false;

    private static final long QUEUE_JOIN_INTERVAL = 5_000L;   // thử vào queue mỗi 5s
    private static final long CHAT_INTERVAL       = 30_000L;  // chat mỗi 30-60s
    private static final long IDLE_MOVE_INTERVAL  = 8_000L;   // di chuyển idle mỗi 8s

    public BotPvpQueueController(BotPlayerReal bot) {
        this.bot = bot;
    }

    // ========================= MAIN TICK =========================

    /**
     * Tick chính — gọi từ BotBrain trong state PVP_QUEUE.
     */
    public void tick() {
        long now = System.currentTimeMillis();

        // 1. Đảm bảo bot đang ở map Truy Nã
        if (bot.map == null || bot.map.template == null) return;
        if (bot.map.template.id != MAP_TRUY_NA) {
            // Di chuyển đến map 119 nếu chưa đến
            travelToWantedMap();
            return;
        }

        // 2. Tham gia hàng chờ ghép trận
        if (now - lastQueueJoinTime > QUEUE_JOIN_INTERVAL) {
            lastQueueJoinTime = now;
            joinQueue();
        }

        // 3. Tắt chat bot ở phòng chờ pvp truy nã

        // 4. Di chuyển nhẹ nhàng tại chỗ (tránh đứng như cọc)
        if (now - lastIdleMoveTime > IDLE_MOVE_INTERVAL + ZUtil.random(4000)) {
            lastIdleMoveTime = now;
            doIdleMove();
        }
    }

    // ========================= QUEUE =========================

    /**
     * Tham gia hàng chờ ghép trận qua API Wanted.
     * Nếu đủ người thì trận sẽ được tạo tự động bởi Wanted system.
     */
    private void joinQueue() {
        try {
            if (!inQueue) {
                // Thêm bot vào hàng chờ Wanted
                Wanted.add_player_wait(bot);
                inQueue = true;
            }
        } catch (Exception ignored) {}
    }

    /**
     * Bot rời khỏi queue khi kết thúc trạng thái PVP_QUEUE.
     */
    public void leaveQueue() {
        try {
            if (inQueue) {
                Wanted.remove_player_wait(bot);
                inQueue = false;
            }
        } catch (Exception ignored) {}
    }

    // ========================= NAVIGATION =========================

    private void travelToWantedMap() {
        if (bot.currentPath == null || bot.targetMapId != MAP_TRUY_NA) {
            bot.targetMapId = MAP_TRUY_NA;
            bot.currentPath = Pathfinder.findPath(
                bot.map != null && bot.map.template != null ? bot.map.template.id : 0,
                MAP_TRUY_NA
            );
            bot.pathIndex = 0;
        }
        bot.getMovementController().traversePath();
    }

    // ========================= IDLE BEHAVIOR =========================

    /**
     * Chat ngẫu nhiên như người đang chờ ghép.
     */
    private void doLobbyChat() {
        if (bot.map == null) return;
        if (bot.map.template != null && bot.map.template.id == MAP_TRUY_NA) return;
        // Chỉ chat khi có người thật trong map để tránh spam vô nghĩa
        if (!BotPlayerManager.hasRealPlayer(bot.map) && ZUtil.random(100) > 20) return;

        try {
            String chat = LOBBY_CHATS[ZUtil.random(LOBBY_CHATS.length)];
            bot.map.send_chat_popup(0, bot.index_map, chat);
        } catch (Exception ignored) {}
    }

    /**
     * Di chuyển nhẹ nhàng quanh vị trí hiện tại (không đứng như cọc).
     */
    private void doIdleMove() {
        // Di chuyển rất nhỏ (±20px) để trông tự nhiên
        short tx = (short) (bot.x + ZUtil.random(-20, 20));
        short ty = (short) (bot.y + ZUtil.random(-10, 10));
        try {
            bot.getMovementController().moveTowards(tx, ty);
        } catch (Exception ignored) {}
    }

    // ========================= INFO =========================

    public boolean isInQueue() {
        return inQueue;
    }

    /**
     * Kiểm tra bot có đang ở map pvp/arena không.
     */
    public boolean isInPvpMap() {
        return bot.map != null && bot.map.map_vp != null;
    }
}
