package core;

import activities.TimedDungeonManager;
import activities.TimedDungeonManager.ScheduleConfig;
import database.DbManager;
import event.Event;
import event.EventManager;
import historys.zLog;
import map.MapManager;
import map.Zone;
import model.MyPet;
import model.Pet;
import model.Player;
import network.Session;
import network.SessionManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.concurrent.*;

/**
 * PanelManager - Quản lý Admin Panel Toàn Diện cho Game Server HTTH.
 *
 * Cung cấp bộ API quản lý chuyên sâu:
 * 1. Quản lý Bảo trì & Điều khiển Server (Countdown, Kick all, Save DB, Reload config, Test mode).
 * 2. Lệnh GM Console & Loa KTG / Popup Broadcast toàn server.
 * 3. Quản lý & Soi chi tiết Người chơi (Online / Offline, Túi đồ, Rương đồ, Trang bị mặc, Pet, Thuyền, Buffs, Mute, Ban, Đổi Pass, Cứu kẹt).
 * 4. Mở / Đóng & Điều khiển 10 Phó bản Timed Dungeons & 17 Sự kiện mùa.
 * 5. Quản lý Giftcode (CRUD mã quà tặng, chọn item đính kèm, reset lượt nhập).
 * 6. Gửi thư & Quà Mail (Cá nhân, Toàn bộ Online, Toàn bộ Server DB).
 * 7. Chỉnh sửa chỉ số Item 3 & Pet Template realtime (Lưu DB & RAM), Tạo đồ custom max chỉ số.
 * 8. Tra cứu nhật ký lịch sử hệ thống SQL `historys`.
 */
public class PanelManager {

    private static volatile PanelManager instance;
    private AdminPanelFrame frame;
    private ScheduledExecutorService maintenanceScheduler;
    private ScheduledFuture<?> maintenanceTask;
    private int maintenanceRemainingSeconds = 0;
    private String maintenanceReasonText = "";

    private PanelManager() {
    }

    public static PanelManager gI() {
        if (instance == null) {
            synchronized (PanelManager.class) {
                if (instance == null) {
                    instance = new PanelManager();
                }
            }
        }
        return instance;
    }

    /**
     * Mở giao diện đồ họa Admin Panel (Java Swing GUI).
     */
    public void openUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (frame == null || !frame.isDisplayable()) {
                frame = new AdminPanelFrame();
            }
            frame.setVisible(true);
            frame.toFront();
            frame.requestFocus();
        });
    }

    public AdminPanelFrame getFrame() {
        return frame;
    }

    // =========================================================================
    // DATA TRANSFER OBJECTS (DTOs)
    // =========================================================================

    public static class EventInfoDTO {
        public int id;
        public int eventId;
        public String name;
        public boolean isActive;
        public String time = "";
        public String timeend = "";
        public String timex2pay = "";
        public String timechangeitem = "";
        public String timedropitem = "";
        public String timeremoveitem = "";
        public String season = "";
        public String seasonName = "";
    }

    public static class DungeonStatusDTO {
        public String id;
        public String name;
        public boolean isEnabled;
        public int timingMode;
        public int state; // 0=IDLE, 1=RUNNING
        public String stateName;
        public long remainingSeconds;
        public int roundIndex;
        public int runDurationMinutes;
        public int cooldownMinutes;
        public String timeRangeInfo;
        public String rewardInfo;
    }

    public static class PlayerInfoDTO {
        public int playerId;
        public int accountId;
        public String name;
        public int level;
        public long exp;
        public int mapId;
        public String mapName = "";
        public int zoneId;
        public String ip;
        public long extol; // VND
        public long beri;  // Vang
        public int ruby;
        public int hp;
        public int maxHp;
        public int mp;
        public int maxMp;
        public int spPoint;
        public int haki;
        public int ticket;
        public int bossKey;
        public String clanName;
        public boolean isOnline;
        public boolean isBot;
        public boolean isClosed;
        public boolean isBanned;
        public boolean isMuted;
    }

    public static class OptionDTO {
        public int id;
        public String name = "";
        public int param;

        public OptionDTO(int id, String name, int param) {
            this.id = id;
            this.name = name;
            this.param = param;
        }
    }

    public static class ItemWearDTO {
        public int id;
        public String name = "";
        public int icon;
        public int typeEquip;
        public int clazz;
        public int color;
        public int level;
        public int levelUp;
        public int isHoanMy;
        public int valueKichAn;
        public int numLoKham;
        public short[] mdakham = new short[0];
        public List<OptionDTO> options = new ArrayList<>();
        public int index;
        public String location = ""; // Body, Bag, Box
    }

    public static class SimpleItemDTO {
        public int category; // 3, 4, 7, 105, 110
        public int id;
        public String name = "";
        public int icon;
        public int quantity;
        public String info = "";
        public String location = ""; // Bag, Box
    }

    public static class PetDetailDTO {
        public int id;
        public String name = "";
        public int icon;
        public int frame;
        public boolean isUsing;
        public long expireTime;
        public List<OptionDTO> options = new ArrayList<>();
    }

    public static class BoatDetailDTO {
        public int id;
        public String name = "";
        public int icon;
        public boolean isUsing;
    }

    public static class PlayerDetailDTO {
        public PlayerInfoDTO basicInfo;
        public List<ItemWearDTO> equippedItems = new ArrayList<>();
        public List<ItemWearDTO> bagItem3 = new ArrayList<>();
        public List<ItemWearDTO> boxItem3 = new ArrayList<>();
        public List<SimpleItemDTO> bagItems4 = new ArrayList<>();
        public List<SimpleItemDTO> boxItems4 = new ArrayList<>();
        public List<SimpleItemDTO> bagItems7 = new ArrayList<>();
        public List<SimpleItemDTO> boxItems7 = new ArrayList<>();
        public List<PetDetailDTO> pets = new ArrayList<>();
        public List<BoatDetailDTO> boats = new ArrayList<>();
    }

    public static class BotInfoDTO {
        public int botId;
        public String name;
        public int level;
        public int mapId;
        public int zoneId;
        public String botType;
    }

    public static class HistoryLogDTO {
        public int id;
        public int accountId;
        public int playerId;
        public String type;
        public String data;
        public String createdAt;
    }

    public static class GiftCodeDTO {
        public String giftname;
        public int luotnhap;
        public int gioihan;
        public String notice = "";
        public int beri;
        public int ruby;
        public String itemJson = "[]";
        public String used = "";
        public String special = "";
        public byte mtv;
    }

    public static class Item3TemplateDTO {
        public int id;
        public String name = "";
        public int clazz;
        public int typeEquip;
        public int icon;
        public int level;
        public int color;
        public int typelock;
        public int numHoleDaDuc;
        public int chetac;
        public int ishoanmy;
        public int beri;
        public int part;
        public int numlokham;
        public List<OptionDTO> op1 = new ArrayList<>();
        public List<OptionDTO> op2 = new ArrayList<>();
        public List<Short> mdakham = new ArrayList<>();
    }

    public static class PetTemplateDTO {
        public int id;
        public String name = "";
        public int type;
        public int icon;
        public int frame;
        public List<OptionDTO> options = new ArrayList<>();
    }

    // =========================================================================
    // 1. PHÂN HỆ QUẢN TRỊ MÁY CHỦ, BẢO TRÌ & RELOAD
    // =========================================================================

    public Map<String, Object> getServerStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        int realPlayers = getOnlineRealPlayerCount();
        int onlineBots = getOnlineBotCount();
        int totalSessions = SessionManager.CLIENT_ENTRYS != null ? SessionManager.CLIENT_ENTRYS.size() : 0;

        Runtime rt = Runtime.getRuntime();
        long totalMem = rt.totalMemory() / (1024 * 1024);
        long freeMem = rt.freeMemory() / (1024 * 1024);
        long usedMem = totalMem - freeMem;
        long maxMem = rt.maxMemory() / (1024 * 1024);

        int activeDungeons = 0;
        for (ScheduleConfig cfg : TimedDungeonManager.gI().getConfigs().values()) {
            if (cfg != null && cfg.isEnabled && cfg.state == ScheduleConfig.STATE_RUNNING) {
                activeDungeons++;
            }
        }

        int activeEvents = EventManager.gI() != null && EventManager.gI().getActiveEventsList() != null
                ? EventManager.gI().getActiveEventsList().size() : 0;

        stats.put("real_players_online", realPlayers);
        stats.put("bots_online", onlineBots);
        stats.put("total_connected_sessions", totalSessions);
        stats.put("ram_used_mb", usedMem);
        stats.put("ram_total_mb", totalMem);
        stats.put("ram_max_mb", maxMem);
        stats.put("active_dungeons_count", activeDungeons);
        stats.put("active_events_count", activeEvents);
        stats.put("is_baotri", ServerManager.gI().isBaoTri);
        stats.put("is_test_mode", Manager.gI().isTestMode());
        stats.put("server_admin_mode", Manager.gI().server_admin);
        stats.put("maintenance_countdown", maintenanceRemainingSeconds);
        stats.put("maintenance_reason", maintenanceReasonText);
        stats.put("threads_count", Thread.activeCount());

        return stats;
    }

    /**
     * Lên lịch đếm ngược bảo trì và tự động lưu dữ liệu + ngắt server an toàn.
     */
    public synchronized boolean scheduleMaintenance(int countdownSeconds, String reason) {
        if (ServerManager.gI().isBaoTri) {
            return false;
        }

        if (countdownSeconds <= 0) {
            countdownSeconds = 10;
        }
        if (reason == null || reason.trim().isEmpty()) {
            reason = "Bảo trì định kỳ máy chủ";
        }

        this.maintenanceRemainingSeconds = countdownSeconds;
        this.maintenanceReasonText = reason;

        if (maintenanceScheduler == null || maintenanceScheduler.isShutdown()) {
            maintenanceScheduler = Executors.newSingleThreadScheduledExecutor();
        }

        if (maintenanceTask != null && !maintenanceTask.isDone()) {
            maintenanceTask.cancel(true);
        }

        // Broadcast alert ngay lập tức
        broadcastWorldChat("HỆ THỐNG: Máy chủ sẽ BẢO TRÌ sau " + countdownSeconds + " giây! Lý do: " + reason, 1);
        zLog.gI().add_log("ADMIN_PANEL", "MAINTENANCE_SCHEDULED",
                "Scheduled maintenance in " + countdownSeconds + "s. Reason: " + reason);

        maintenanceTask = maintenanceScheduler.scheduleAtFixedRate(() -> {
            try {
                maintenanceRemainingSeconds--;
                if (maintenanceRemainingSeconds == 30 || maintenanceRemainingSeconds == 20
                        || maintenanceRemainingSeconds == 10 || (maintenanceRemainingSeconds <= 5 && maintenanceRemainingSeconds > 0)) {
                    broadcastWorldChat("CẢNH BÁO: Máy chủ bảo trì sau " + maintenanceRemainingSeconds + " giây!", 1);
                }

                if (maintenanceRemainingSeconds <= 0) {
                    if (maintenanceTask != null) {
                        maintenanceTask.cancel(false);
                    }
                    new Thread(() -> {
                        Log.warn("PanelManager", "Executing scheduled maintenance shutdown...");
                        SaveData.BaoTri();
                    }, "Maintenance-Exec-Thread").start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);

        return true;
    }

    /**
     * Hủy bỏ đếm ngược bảo trì máy chủ nếu chưa đóng server.
     */
    public synchronized boolean cancelMaintenance() {
        if (ServerManager.gI().isBaoTri) {
            return false;
        }
        if (maintenanceTask != null && !maintenanceTask.isDone()) {
            maintenanceTask.cancel(true);
            maintenanceRemainingSeconds = 0;
            maintenanceReasonText = "";
            broadcastWorldChat("THÔNG BÁO: Lệnh bảo trì máy chủ đã được HỦY BỎ bởi Admin. Chúc các bạn chơi game vui vẻ!", 2);
            zLog.gI().add_log("ADMIN_PANEL", "MAINTENANCE_CANCELLED", "Admin cancelled maintenance countdown.");
            return true;
        }
        return false;
    }

    /**
     * Kick toàn bộ người chơi đang kết nối (sau khi lưu sạch cache).
     */
    public boolean kickAllPlayers(String reason) {
        if (reason == null || reason.isEmpty()) reason = "Admin ngắt kết nối toàn máy chủ.";
        try {
            SaveData.process();
            database.CacheManager.gI().flushAllDirtyToDb();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int count = 0;
        if (SessionManager.CLIENT_ENTRYS != null) {
            synchronized (SessionManager.CLIENT_ENTRYS) {
                for (int i = SessionManager.CLIENT_ENTRYS.size() - 1; i >= 0; i--) {
                    try {
                        Session ss = SessionManager.CLIENT_ENTRYS.get(i);
                        if (ss != null) {
                            if (ss.p != null && ss.p.getService() != null) {
                                ss.p.getService().send_box_ThongBao_OK("Bạn bị ngắt kết nối: " + reason);
                            }
                            ss.disconnect();
                            ss.disconnectSC();
                            count++;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
        zLog.gI().add_log("ADMIN_PANEL", "KICK_ALL", "Kicked all " + count + " connected clients. Reason: " + reason);
        return true;
    }

    /**
     * Kick một người chơi cụ thể.
     */
    public boolean kickPlayer(String playerName, String reason) {
        if (reason == null || reason.isEmpty()) reason = "Admin kick khỏi máy chủ.";
        try {
            Player p = getOnlinePlayer(playerName);
            if (p != null) {
                try {
                    if (p.getService() != null) {
                        p.getService().send_box_ThongBao_OK("Bạn bị kick khỏi máy chủ: " + reason);
                    }
                } catch (Exception ignored) {}
                try {
                    p.flush(p, true);
                } catch (Exception ignored) {}
                if (p.conn != null) {
                    p.conn.disconnect();
                    p.conn.disconnectSC();
                }
                zLog.gI().add_log_by_name(playerName, "ADMIN_KICK", "Kicked player: " + playerName + " Reason: " + reason);
                return true;
            }
        } catch (Exception e) {
            Log.error("PanelManager", "kickPlayer error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lưu toàn bộ dữ liệu máy chủ bất đồng bộ vào Database (Players, Clans, Market, Dungeons...).
     */
    public CompletableFuture<Boolean> saveAllDataAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SaveData.process();
                database.CacheManager.gI().flushAllDirtyToDb();
                zLog.gI().add_log("ADMIN_PANEL", "SAVE_ALL_DATA", "Manual full data flush triggered via Admin Panel.");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Reload toàn bộ cấu hình, templates, shop, events, dungeons.
     */
    public CompletableFuture<Boolean> reloadAllConfigs() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 1. Reload Events
                EventManager.gI().reloadConfigs();
                // 2. Reload Timed Dungeons Cache
                TimedDungeonManager.gI().loadCache();
                // 3. Reload Shop & Stores
                try (Connection conn = DbManager.gI().getConnect();
                     PreparedStatement ps = conn.prepareStatement("SELECT * FROM `store_data`")) {
                    zabstracts.AbsShop.storeItems.clear();
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            int storeId = rs.getInt("store_id");
                            int itemId = rs.getInt("item_id");
                            int itemCategory = rs.getInt("item_category");
                            int priceCoin = 0;
                            try { priceCoin = rs.getInt("price_coin"); } catch (Exception ignore) {}
                            int priceRuby = 0;
                            try { priceRuby = rs.getInt("price_ruby"); } catch (Exception ignore) {}
                            int reqLevel = 0;
                            try { reqLevel = rs.getInt("req_level"); } catch (Exception ignore) {}
                            int eventId = 0;
                            try { eventId = rs.getInt("event_id"); } catch (Exception ignore) {}
                            int type2 = -1;
                            try { type2 = rs.getInt("type2"); } catch (Exception ignore) {}
                            zabstracts.AbsShop.storeItems.add(new store.StoreItem(id, storeId, itemId, itemCategory, priceCoin, priceRuby, reqLevel, eventId, type2));
                        }
                    }
                }
                // 4. Reload Pet templates
                try (Connection conn = DbManager.gI().getConnect();
                     PreparedStatement ps = conn.prepareStatement("SELECT * FROM `pet_template`")) {
                    List<Pet> listPet = new ArrayList<>();
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Pet tempPet = new Pet();
                            tempPet.id = rs.getShort("id");
                            tempPet.name = rs.getString("name");
                            if (tempPet.name == null) tempPet.name = "";
                            tempPet.type = rs.getByte("type");
                            tempPet.icon = rs.getShort("icon");
                            tempPet.frame = rs.getShort("frame");
                            tempPet.op = new ArrayList<>();
                            JSONArray js = (JSONArray) JSONValue.parse(rs.getString("op"));
                            if (js != null) {
                                for (int i = 0; i < js.size(); i++) {
                                    JSONArray js_in = (JSONArray) js.get(i);
                                    if (js_in != null && js_in.size() >= 2) {
                                        tempPet.op.add(new Option(Byte.parseByte(js_in.get(0).toString()), Integer.parseInt(js_in.get(1).toString())));
                                    }
                                }
                            }
                            listPet.add(tempPet);
                        }
                    }
                    Pet.ENTRY = listPet;
                }
                zLog.gI().add_log("ADMIN_PANEL", "RELOAD_ALL_CONFIGS", "Successfully reloaded all server configs.");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public void setAdminTestMode(boolean enable) {
        Manager.gI().server_admin = enable;
        zLog.gI().add_log("ADMIN_PANEL", "TEST_MODE_TOGGLE", "Set server_admin test mode to: " + enable);
    }

    // =========================================================================
    // 2. PHÂN HỆ LỆNH GM CONSOLE & BROADCAST
    // =========================================================================

    /**
     * Phát Loa Thế Giới (Kênh Thế Giới) tới toàn bộ người chơi.
     */
    public boolean broadcastWorldChat(String message, int color) {
        if (message == null || message.trim().isEmpty()) return false;
        try {
            Manager.gI().chatKTG(1, message.trim(), color);
            zLog.gI().add_log("ADMIN_PANEL", "BROADCAST_KTG", "Broadcasted KTG: " + message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gửi hộp thoại thông báo Popup OK tới toàn bộ người chơi đang online.
     */
    public boolean broadcastPopupNotice(String message) {
        if (message == null || message.trim().isEmpty()) return false;
        int count = 0;
        for (Session ss : SessionManager.CLIENT_ENTRYS) {
            if (ss != null && ss.connected && ss.p != null && ss.p.getService() != null) {
                try {
                    ss.p.getService().send_box_ThongBao_OK(message);
                    count++;
                } catch (Exception ignored) {}
            }
        }
        zLog.gI().add_log("ADMIN_PANEL", "BROADCAST_POPUP", "Sent popup notice to " + count + " players: " + message);
        return true;
    }

    /**
     * Thực thi lệnh GM Console với bộ lệnh phong phú.
     */
    public String executeAdminCommand(String line) {
        if (line == null || line.trim().isEmpty()) return "Vui lòng nhập lệnh!";
        String trimmed = line.trim();
        if (trimmed.startsWith("/")) {
            trimmed = trimmed.substring(1).trim();
        }

        String[] parts = trimmed.split("\\s+");
        if (parts.length == 0) return "Lệnh trống!";
        String cmd = parts[0].toLowerCase();

        try {
            switch (cmd) {
                case "help":
                case "?":
                    return "=== DANH SÁCH LỆNH GM CONSOLE ===\n"
                            + "1. /broadcast <nội dung> | /ktg <nội dung> : Phát loa thế giới\n"
                            + "2. /popup [player] <nội dung> : Gửi thông báo popup\n"
                            + "3. /baotri <giây> [lý do] : Hẹn giờ đếm ngược bảo trì\n"
                            + "4. /cancelbaotri : Hủy đếm ngược bảo trì\n"
                            + "5. /kick <tên player> [lý do] : Kick người chơi\n"
                            + "6. /kickall [lý do] : Kick toàn bộ người chơi\n"
                            + "7. /ban <tên player> [lý do] : Khóa tài khoản vĩnh viễn\n"
                            + "8. /unban <username|player> : Mở khóa tài khoản\n"
                            + "9. /mute <tên player> <phút> [lý do] : Cấm chat người chơi\n"
                            + "10. /unmute <tên player> : Gỡ cấm chat\n"
                            + "11. /buff <tên player> <loại> <số lượng> : Buff tài nguyên (beri, ruby, vnd, exp, level, sp, haki, ticket, key, hpmp, full)\n"
                            + "12. /money <tên player> <beri> <ruby> <vnd> : Đặt trực tiếp số dư\n"
                            + "13. /setlevel <tên player> <level> : Đặt level chính xác\n"
                            + "14. /unstuck <tên player> : Cứu kẹt map (tele về Map 1)\n"
                            + "15. /tele <tên player> <mapId> [x] [y] : Dịch chuyển người chơi\n"
                            + "16. /pass <username|player> <mật khẩu mới> : Đổi mật khẩu tài khoản\n"
                            + "17. /item3 <tên player> <templateId> [color 0-8] [levelUp 0-16] : Cho Item 3\n"
                            + "18. /item4 <tên player> <templateId> <số lượng> : Cho Item 4\n"
                            + "19. /item7 <tên player> <templateId> <số lượng> : Cho Item 7\n"
                            + "20. /pet <tên player> <petTemplateId> : Cho Thú cưng / Pet\n"
                            + "21. /dungeon <id> <start|end|on|off> : Điều khiển phó bản\n"
                            + "22. /event <id> <on|off> : Bật/tắt sự kiện\n"
                            + "23. /save : Lưu toàn bộ dữ liệu DB\n"
                            + "24. /reload : Tải lại toàn bộ configs & templates\n"
                            + "25. /stats : Xem thông số realtime server";

                case "broadcast":
                case "ktg":
                case "loa":
                case "chat":
                    if (parts.length < 2) return "Cú pháp: /broadcast <nội dung>";
                    String bcMsg = trimmed.substring(parts[0].length()).trim();
                    broadcastWorldChat(bcMsg, 1);
                    return "[SUCCESS] Đã gửi thông báo Loa Thế Giới: " + bcMsg;

                case "popup":
                case "tb":
                case "thongbao":
                    if (parts.length < 2) return "Cú pháp: /popup [tên player] <nội dung>";
                    if (parts.length >= 3 && getOnlinePlayer(parts[1]) != null) {
                        Player target = getOnlinePlayer(parts[1]);
                        String msg = trimmed.substring(parts[0].length() + parts[1].length() + 1).trim();
                        target.getService().send_box_ThongBao_OK(msg);
                        return "[SUCCESS] Đã gửi popup tới " + target.name + ": " + msg;
                    } else {
                        String msg = trimmed.substring(parts[0].length()).trim();
                        broadcastPopupNotice(msg);
                        return "[SUCCESS] Đã gửi popup toàn server: " + msg;
                    }

                case "baotri":
                    int secs = parts.length > 1 ? Integer.parseInt(parts[1]) : 10;
                    String reason = parts.length > 2 ? trimmed.substring(parts[0].length() + parts[1].length() + 1).trim() : "Bảo trì định kỳ";
                    scheduleMaintenance(secs, reason);
                    return "[SUCCESS] Đã lên lịch bảo trì sau " + secs + "s. Lý do: " + reason;

                case "cancelbaotri":
                case "huybaotri":
                    if (cancelMaintenance()) {
                        return "[SUCCESS] Đã hủy đếm ngược bảo trì!";
                    } else {
                        return "[ERROR] Không có lịch bảo trì nào đang chạy hoặc server đã đóng.";
                    }

                case "kick":
                    if (parts.length < 2) return "Cú pháp: /kick <tên player> [lý do]";
                    String kReason = parts.length > 2 ? trimmed.substring(parts[0].length() + parts[1].length() + 1).trim() : "Kick bởi Admin";
                    if (kickPlayer(parts[1], kReason)) {
                        return "[SUCCESS] Đã kick người chơi: " + parts[1];
                    }
                    return "[ERROR] Không tìm thấy player online: " + parts[1];

                case "kickall":
                    String kaReason = parts.length > 1 ? trimmed.substring(parts[0].length()).trim() : "Admin kick all";
                    kickAllPlayers(kaReason);
                    return "[SUCCESS] Đã kick toàn bộ người chơi đang kết nối!";

                case "ban":
                    if (parts.length < 2) return "Cú pháp: /ban <tên player> [lý do]";
                    String bReason = parts.length > 2 ? trimmed.substring(parts[0].length() + parts[1].length() + 1).trim() : "Admin Ban";
                    if (banPlayer(parts[1], bReason)) {
                        return "[SUCCESS] Đã khóa tài khoản của player: " + parts[1];
                    }
                    return "[ERROR] Khóa tài khoản thất bại (không tìm thấy player: " + parts[1] + ")";

                case "unban":
                    if (parts.length < 2) return "Cú pháp: /unban <username|player>";
                    if (unbanPlayer(parts[1])) {
                        return "[SUCCESS] Đã mở khóa tài khoản: " + parts[1];
                    }
                    return "[ERROR] Không tìm thấy tài khoản để mở khóa: " + parts[1];

                case "mute":
                    if (parts.length < 3) return "Cú pháp: /mute <tên player> <số phút> [lý do]";
                    int mMinutes = Integer.parseInt(parts[2]);
                    String mReason = parts.length > 3 ? trimmed.substring(parts[0].length() + parts[1].length() + parts[2].length() + 2).trim() : "Admin Mute";
                    if (mutePlayer(parts[1], mMinutes, mReason)) {
                        return "[SUCCESS] Đã cấm chat " + parts[1] + " trong " + mMinutes + " phút.";
                    }
                    return "[ERROR] Không tìm thấy player online: " + parts[1];

                case "unmute":
                    if (parts.length < 2) return "Cú pháp: /unmute <tên player>";
                    if (unmutePlayer(parts[1])) {
                        return "[SUCCESS] Đã gỡ cấm chat cho " + parts[1];
                    }
                    return "[ERROR] Không tìm thấy player online: " + parts[1];

                case "buff":
                    if (parts.length < 4) return "Cú pháp: /buff <tên player> <loại: beri|ruby|vnd|exp|level|sp|haki|ticket|key|hpmp|full> <số lượng>";
                    String pName = parts[1];
                    String bType = parts[2].toLowerCase();
                    long bVal = Long.parseLong(parts[3]);
                    if (buffPlayer(pName, bType, bVal)) {
                        return "[SUCCESS] Đã buff " + bType + " = " + bVal + " cho " + pName;
                    }
                    return "[ERROR] Buff thất bại cho " + pName;

                case "money":
                    if (parts.length < 5) return "Cú pháp: /money <tên player> <beri> <ruby> <vnd>";
                    if (setPlayerMoney(parts[1], Long.parseLong(parts[2]), Integer.parseInt(parts[3]), Long.parseLong(parts[4]))) {
                        return "[SUCCESS] Đã đặt tiền cho " + parts[1] + ": Beri=" + parts[2] + ", Ruby=" + parts[3] + ", VND=" + parts[4];
                    }
                    return "[ERROR] Đặt tiền thất bại cho " + parts[1];

                case "setlevel":
                    if (parts.length < 3) return "Cú pháp: /setlevel <tên player> <level>";
                    if (setPlayerLevel(parts[1], Integer.parseInt(parts[2]))) {
                        return "[SUCCESS] Đã đặt Level cho " + parts[1] + " = " + parts[2];
                    }
                    return "[ERROR] Đặt Level thất bại cho " + parts[1];

                case "unstuck":
                case "cuuket":
                    if (parts.length < 2) return "Cú pháp: /unstuck <tên player>";
                    if (resetPlayerLocation(parts[1])) {
                        return "[SUCCESS] Đã cứu kẹt, đưa " + parts[1] + " về Map 1 (Làng Cối Xay Gió)!";
                    }
                    return "[ERROR] Cứu kẹt thất bại cho " + parts[1];

                case "tele":
                    if (parts.length < 3) return "Cú pháp: /tele <tên player> <mapId> [x] [y]";
                    int tMapId = Integer.parseInt(parts[2]);
                    short tX = parts.length > 3 ? Short.parseShort(parts[3]) : 300;
                    short tY = parts.length > 4 ? Short.parseShort(parts[4]) : 300;
                    if (teleportPlayer(parts[1], tMapId, tX, tY)) {
                        return "[SUCCESS] Đã dịch chuyển " + parts[1] + " sang Map " + tMapId + " (" + tX + ", " + tY + ")";
                    }
                    return "[ERROR] Dịch chuyển thất bại cho " + parts[1];

                case "pass":
                case "doipass":
                    if (parts.length < 3) return "Cú pháp: /pass <username|player> <mật khẩu mới>";
                    if (changeAccountPassword(parts[1], parts[2])) {
                        return "[SUCCESS] Đã đổi mật khẩu cho " + parts[1] + " thành công!";
                    }
                    return "[ERROR] Đổi mật khẩu thất bại cho " + parts[1];

                case "sync_extol":
                case "syncextol":
                    if (parts.length < 3) return "[ERROR] Cú pháp: /sync_extol <tên player> <số lượng>";
                    try {
                        String sTarget = parts[1];
                        long sAmount = Long.parseLong(parts[2]);
                        Player sOnline = getOnlinePlayer(sTarget);
                        if (sOnline != null) {
                            sOnline.updateVnd(sAmount);
                            sOnline.updateMoney();
                            if (sOnline.getService() != null) {
                                sOnline.getService().send_box_ThongBao_OK("Bạn vừa nhận được +" + sAmount + " Extol từ Web!");
                            }
                            zLog.gI().add_log_by_name(sOnline.name, "WEB_EXCHANGE_SYNC", "Synced Extol +" + sAmount);
                            return "[SUCCESS] Đã đồng bộ Extol +" + sAmount + " cho " + sOnline.name + " (đang online)";
                        } else {
                            return "[SUCCESS] Player " + sTarget + " đang offline, số dư Extol trong DB đã được cập nhật trước đó.";
                        }
                    } catch (Exception exSync) {
                        return "[ERROR] Lỗi sync_extol: " + exSync.getMessage();
                    }

                case "save":
                    saveAllDataAsync();
                    return "[SUCCESS] Đang tiến hành lưu toàn bộ dữ liệu vào Database...";

                case "reload":
                    reloadAllConfigs();
                    return "[SUCCESS] Đang tải lại toàn bộ cấu hình, events, phó bản, shop...";

                case "cleanlog":
                case "dondep":
                    try {
                        historys.zLog.gI().cleanOldLogs();
                        core.MailService.cleanExpiredMails();
                        return "[SUCCESS] Đã dọn dẹp sạch sẽ logs hệ thống và các hòm thư hết hạn trong Database.";
                    } catch (Exception ex) {
                        return "[ERROR] Lỗi khi dọn dẹp log: " + ex.getMessage();
                    }

                case "baotriclean":
                    try {
                        historys.zLog.gI().cleanOldLogs();
                        core.MailService.cleanExpiredMails();
                        int delaySecs = parts.length > 1 ? Integer.parseInt(parts[1]) : 5;
                        scheduleMaintenance(delaySecs, "Bảo trì & Dọn Dẹp Hệ Thống");
                        return "[SUCCESS] Đã dọn log thành công và lên lịch bảo trì tự động sau " + delaySecs + " giây.";
                    } catch (Exception ex) {
                        return "[ERROR] Lỗi thực hiện bảo trì clean: " + ex.getMessage();
                    }

                case "reset":
                    String rTarget = (parts.length > 1 ? parts[1].toUpperCase() : "OPEN");
                    return executeReset(rTarget);

                case "resetbetatoopen":
                    return executeReset("OPEN");

                case "resetbeta":
                    return executeReset("BETA");

                case "setstatus":
                case "status":
                    if (parts.length < 2) return "Cú pháp: /status <TEST|BETA|OPEN>";
                    String stTarget = parts[1].toUpperCase();
                    if (!stTarget.equals("TEST") && !stTarget.equals("BETA") && !stTarget.equals("OPEN")) {
                        return "[ERROR] Trạng thái không hợp lệ. Chỉ chấp nhận: TEST, BETA, OPEN";
                    }
                    if (stTarget.equals("BETA") || stTarget.equals("OPEN")) {
                        return executeReset(stTarget);
                    } else {
                        return setServerStatusTest();
                    }

                case "dungeon":
                    if (parts.length < 3) return "Cú pháp: /dungeon <dungeonId> <start|end|on|off>";
                    String dId = parts[1].toUpperCase();
                    String dAction = parts[2].toLowerCase();
                    if (dAction.equals("start")) {
                        TimedDungeonManager.gI().forceStartDungeon(dId);
                        return "[SUCCESS] Đã ép BẮT ĐẦU đợt phó bản: " + dId;
                    } else if (dAction.equals("end")) {
                        TimedDungeonManager.gI().forceEndDungeon(dId);
                        return "[SUCCESS] Đã ép KẾT THÚC đợt phó bản: " + dId;
                    } else if (dAction.equals("on") || dAction.equals("true")) {
                        TimedDungeonManager.gI().toggleDungeon(dId, true);
                        return "[SUCCESS] Đã BẬT phó bản: " + dId;
                    } else if (dAction.equals("off") || dAction.equals("false")) {
                        TimedDungeonManager.gI().toggleDungeon(dId, false);
                        return "[SUCCESS] Đã TẮT phó bản: " + dId;
                    }
                    return "[ERROR] Hành động phó bản không hợp lệ: " + dAction;

                case "event":
                    if (parts.length < 3) return "Cú pháp: /event <eventId> <on|off>";
                    int evId = Integer.parseInt(parts[1]);
                    boolean evActive = parts[2].equalsIgnoreCase("on") || parts[2].equalsIgnoreCase("true");
                    if (toggleEvent(evId, evActive)) {
                        return "[SUCCESS] Đã chuyển trạng thái Event ID " + evId + " = " + evActive;
                    }
                    return "[ERROR] Chuyển trạng thái Event ID " + evId + " thất bại (xung đột mùa hoặc trùng lịch)!";

                case "stats":
                    Map<String, Object> st = getServerStats();
                    return "=== SERVER REALTIME STATS ===\n"
                            + "Players Online: " + st.get("real_players_online") + "\n"
                            + "Bots Online: " + st.get("bots_online") + "\n"
                            + "Sessions: " + st.get("total_connected_sessions") + "\n"
                            + "RAM: " + st.get("ram_used_mb") + " MB / " + st.get("ram_total_mb") + " MB (Max: " + st.get("ram_max_mb") + " MB)\n"
                            + "Active Dungeons: " + st.get("active_dungeons_count") + "\n"
                            + "Active Events: " + st.get("active_events_count") + "\n"
                            + "Threads: " + st.get("threads_count");

                default:
                    return "[ERROR] Lệnh không nhận diện: '" + cmd + "'. Gõ /help để xem danh sách lệnh!";
            }
        } catch (Exception e) {
            return "[EXCEPTION] Lỗi thực thi lệnh: " + e.getMessage();
        }
    }

    // =========================================================================
    // 3. PHÂN HỆ QUẢN LÝ NGƯỜI CHƠI, SOI DỮ LIỆU & BUFF
    // =========================================================================

    public Player getOnlinePlayer(String nameOrId) {
        if (nameOrId == null || nameOrId.trim().isEmpty()) return null;
        String trimmed = nameOrId.trim();
        Player p = SessionManager.PLAYERS_BY_NAME.get(trimmed);
        if (p == null) {
            p = Zone.get_player_by_name_allmap(trimmed);
        }
        if (p == null) {
            try {
                int pid = Integer.parseInt(trimmed);
                p = Zone.get_player_by_id_allmap(pid);
            } catch (NumberFormatException ignored) {}
        }
        return p;
    }

    public int getOnlineRealPlayerCount() {
        int count = 0;
        Set<Integer> tracked = new HashSet<>();
        if (SessionManager.CLIENT_ENTRYS != null) {
            for (Session ss : SessionManager.CLIENT_ENTRYS) {
                if (ss != null && ss.connected && ss.p != null) {
                    Player p = ss.p;
                    if (!p.isBot && !p.isDe && !p.isClosed && !tracked.contains(p.IDPlayer)) {
                        tracked.add(p.IDPlayer);
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public int getOnlineBotCount() {
        int count = 0;
        Set<Integer> tracked = new HashSet<>();
        for (map.Map m : MapManager.getInstance().getMaps()) {
            if (m != null && m.zones != null) {
                for (Zone z : m.zones) {
                    if (z != null && z.players != null) {
                        for (Player p : z.players) {
                            if (p != null && p.isBot && !p.isClosed && !tracked.contains(p.IDPlayer)) {
                                tracked.add(p.IDPlayer);
                                count++;
                            }
                        }
                    }
                }
            }
        }
        List<Zone> plus = Zone.get_map_plus();
        if (plus != null) {
            for (Zone z : plus) {
                if (z != null && z.players != null) {
                    for (Player p : z.players) {
                        if (p != null && p.isBot && !p.isClosed && !tracked.contains(p.IDPlayer)) {
                            tracked.add(p.IDPlayer);
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public List<PlayerInfoDTO> getOnlinePlayerList(String filterName, int limit, int offset) {
        List<PlayerInfoDTO> result = new ArrayList<>();
        Set<Integer> tracked = new HashSet<>();
        int skipped = 0;

        String f = filterName != null ? filterName.trim().toLowerCase() : "";

        if (SessionManager.CLIENT_ENTRYS != null) {
            for (Session ss : SessionManager.CLIENT_ENTRYS) {
                if (ss != null && ss.connected && ss.p != null) {
                    Player p = ss.p;
                    if (!p.isBot && !p.isDe && !p.isClosed && !tracked.contains(p.IDPlayer)) {
                        tracked.add(p.IDPlayer);
                        if (!f.isEmpty() && !p.name.toLowerCase().contains(f) && !String.valueOf(p.IDPlayer).contains(f)) {
                            continue;
                        }
                        if (skipped < offset) {
                            skipped++;
                            continue;
                        }
                        if (result.size() >= limit) break;

                        PlayerInfoDTO dto = new PlayerInfoDTO();
                        dto.playerId = p.IDPlayer;
                        dto.accountId = ss.idUser;
                        dto.name = p.name;
                        dto.level = p.level;
                        dto.exp = p.exp;
                        dto.mapId = (p.map != null && p.map.template != null) ? p.map.template.id : -1;
                        dto.mapName = (p.map != null && p.map.template != null && p.map.template.name != null) ? p.map.template.name : "Map " + dto.mapId;
                        dto.zoneId = (p.map != null) ? p.map.zone_id : -1;
                        dto.ip = ss.ip != null ? ss.ip : "N/A";
                        dto.extol = p.vnd;
                        dto.beri = p.vang;
                        dto.ruby = p.ruby;
                        dto.hp = p.hp;
                        dto.maxHp = p.hpMax;
                        dto.mp = p.mp;
                        dto.maxMp = p.mpMax;
                        dto.spPoint = p.pointSkill;
                        dto.haki = (int) p.pointAttribute;
                        dto.ticket = p.ticket;
                        dto.bossKey = p.key_boss;
                        dto.clanName = (p.clan != null) ? p.clan.name : "Không";
                        dto.isOnline = true;
                        dto.isBot = false;
                        dto.isClosed = p.isClosed;
                        dto.isMuted = p.isMute;
                        result.add(dto);
                    }
                }
            }
        }
        return result;
    }

    public List<BotInfoDTO> getOnlineBotList(int limit, int offset) {
        List<BotInfoDTO> result = new ArrayList<>();
        Set<Integer> tracked = new HashSet<>();
        int skipped = 0;

        for (map.Map mapAll : MapManager.getInstance().getMaps()) {
            if (mapAll == null || mapAll.zones == null) continue;
            for (Zone zone : mapAll.zones) {
                if (zone == null || zone.players == null) continue;
                for (Player p : zone.players) {
                    if (p != null && p.isBot && !p.isClosed && !tracked.contains(p.IDPlayer)) {
                        tracked.add(p.IDPlayer);
                        if (skipped < offset) {
                            skipped++;
                            continue;
                        }
                        if (result.size() >= limit) break;

                        BotInfoDTO dto = new BotInfoDTO();
                        dto.botId = p.IDPlayer;
                        dto.name = p.name;
                        dto.level = p.level;
                        dto.mapId = (p.map != null && p.map.template != null) ? p.map.template.id : -1;
                        dto.zoneId = (p.map != null) ? p.map.zone_id : -1;
                        dto.botType = p.getClass().getSimpleName();
                        result.add(dto);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Soi toàn diện thông tin nhân vật (Online hoặc Offline từ Database).
     */
    public PlayerDetailDTO getPlayerDetail(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) return null;
        String idStr = identifier.trim();

        Player onlineP = getOnlinePlayer(idStr);
        if (onlineP != null) {
            return extractPlayerDetailFromLivePlayer(onlineP);
        }

        // Nếu offline, query DB `players` và `account`
        return extractPlayerDetailFromDB(idStr);
    }

    private PlayerDetailDTO extractPlayerDetailFromLivePlayer(Player p) {
        PlayerDetailDTO detail = new PlayerDetailDTO();
        PlayerInfoDTO dto = new PlayerInfoDTO();
        dto.playerId = p.IDPlayer;
        dto.accountId = (p.conn != null) ? p.conn.idUser : 0;
        dto.name = p.name;
        dto.level = p.level;
        dto.exp = p.exp;
        dto.mapId = (p.map != null && p.map.template != null) ? p.map.template.id : -1;
        dto.mapName = (p.map != null && p.map.template != null && p.map.template.name != null) ? p.map.template.name : "Map " + dto.mapId;
        dto.zoneId = (p.map != null) ? p.map.zone_id : -1;
        dto.ip = (p.conn != null && p.conn.ip != null) ? p.conn.ip : "N/A";
        dto.extol = p.vnd;
        dto.beri = p.vang;
        dto.ruby = p.ruby;
        dto.hp = p.hp;
        dto.maxHp = p.hpMax;
        dto.mp = p.mp;
        dto.maxMp = p.mpMax;
        dto.spPoint = p.pointSkill;
        dto.haki = (int) p.pointAttribute;
        dto.ticket = p.ticket;
        dto.bossKey = p.key_boss;
        dto.clanName = (p.clan != null) ? p.clan.name : "Không";
        dto.isOnline = true;
        dto.isBot = p.isBot;
        dto.isClosed = p.isClosed;
        dto.isMuted = p.isMute;
        detail.basicInfo = dto;

        // 1. Equipped Items (it_body)
        if (p.item != null && p.item.it_body != null) {
            for (int i = 0; i < p.item.it_body.length; i++) {
                Item_wear it = p.item.it_body[i];
                if (it != null && it.template != null && it.template.id > 0) {
                    ItemWearDTO wDto = convertItemWearToDTO(it, i, "Đang mặc (Slot " + i + ")");
                    detail.equippedItems.add(wDto);
                }
            }
        }

        // 2. Bag Item 3
        if (p.item != null && p.item.bag3 != null) {
            for (int i = 0; i < p.item.bag3.length; i++) {
                Item_wear it = p.item.bag3[i];
                if (it != null && it.template != null && it.template.id > 0) {
                    ItemWearDTO wDto = convertItemWearToDTO(it, i, "Hành trang");
                    detail.bagItem3.add(wDto);
                }
            }
        }

        // 3. Box Item 3
        if (p.item != null && p.item.box3 != null) {
            for (int i = 0; i < p.item.box3.length; i++) {
                Item_wear it = p.item.box3[i];
                if (it != null && it.template != null && it.template.id > 0) {
                    ItemWearDTO wDto = convertItemWearToDTO(it, i, "Rương đồ");
                    detail.boxItem3.add(wDto);
                }
            }
        }

        // 4. Bag Items 4 & 7
        if (p.item != null && p.item.bag47 != null) {
            for (ItemBag47 ib : p.item.bag47) {
                if (ib == null || ib.quant <= 0) continue;
                SimpleItemDTO sDto = new SimpleItemDTO();
                sDto.category = ib.category;
                sDto.id = ib.id;
                sDto.quantity = ib.quant;
                sDto.location = "Hành trang";
                if (ib.category == 4) {
                    ItemTemplate4 t4 = ItemTemplate4.get_it_by_id(ib.id);
                    if (t4 != null) {
                        sDto.name = t4.name;
                        sDto.icon = t4.icon;
                    } else {
                        sDto.name = "Item4 ID " + ib.id;
                    }
                    detail.bagItems4.add(sDto);
                } else if (ib.category == 7) {
                    ItemTemplate7 t7 = ItemTemplate7.get_it_by_id(ib.id);
                    if (t7 != null) {
                        sDto.name = t7.name;
                        sDto.icon = t7.icon;
                    } else {
                        sDto.name = "Item7 ID " + ib.id;
                    }
                    detail.bagItems7.add(sDto);
                }
            }
        }

        // 5. Box Items 4 & 7
        if (p.item != null && p.item.box47 != null) {
            for (ItemBag47 ib : p.item.box47) {
                if (ib == null || ib.quant <= 0) continue;
                SimpleItemDTO sDto = new SimpleItemDTO();
                sDto.category = ib.category;
                sDto.id = ib.id;
                sDto.quantity = ib.quant;
                sDto.location = "Rương đồ";
                if (ib.category == 4) {
                    ItemTemplate4 t4 = ItemTemplate4.get_it_by_id(ib.id);
                    if (t4 != null) {
                        sDto.name = t4.name;
                        sDto.icon = t4.icon;
                    } else {
                        sDto.name = "Item4 ID " + ib.id;
                    }
                    detail.boxItems4.add(sDto);
                } else if (ib.category == 7) {
                    ItemTemplate7 t7 = ItemTemplate7.get_it_by_id(ib.id);
                    if (t7 != null) {
                        sDto.name = t7.name;
                        sDto.icon = t7.icon;
                    } else {
                        sDto.name = "Item7 ID " + ib.id;
                    }
                    detail.boxItems7.add(sDto);
                }
            }
        }

        // 6. Pets
        if (p.my_pet != null) {
            for (MyPet mp : p.my_pet) {
                if (mp == null) continue;
                PetDetailDTO petDto = new PetDetailDTO();
                petDto.id = mp.id;
                petDto.isUsing = mp.isUse;
                petDto.expireTime = mp.time;
                Pet tpl = mp.template != null ? mp.template : Pet.getTemplate(mp.id);
                if (tpl != null) {
                    petDto.name = tpl.name;
                    petDto.icon = tpl.icon;
                    petDto.frame = tpl.frame;
                    if (tpl.op != null) {
                        for (Option op : tpl.op) {
                            if (op != null) {
                                String opName = getOptionNameById(op.id);
                                petDto.options.add(new OptionDTO(op.id, opName, op.getParam()));
                            }
                        }
                    }
                }
                detail.pets.add(petDto);
            }
        }

        // 7. Boats
        if (p.itemboat != null) {
            for (ItemBoatP ib : p.itemboat) {
                if (ib == null) continue;
                BoatDetailDTO bDto = new BoatDetailDTO();
                bDto.id = ib.id;
                bDto.isUsing = ib.is_use;
                ItemBoat boatTpl = ItemBoat.get_item(ib.id);
                if (boatTpl != null) {
                    bDto.name = boatTpl.name;
                    bDto.icon = boatTpl.icon;
                } else {
                    bDto.name = "Thuyền ID " + ib.id;
                }
                detail.boats.add(bDto);
            }
        }

        return detail;
    }

    private PlayerDetailDTO extractPlayerDetailFromDB(String identifier) {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return null;
            String sql = "SELECT p.*, a.username, a.`lock` as is_lock FROM `players` p "
                    + "LEFT JOIN `account` a ON p.account_id = a.id "
                    + "WHERE p.name = ? OR p.id = ? LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, identifier);
                int pId = -1;
                try { pId = Integer.parseInt(identifier); } catch (Exception ignore) {}
                ps.setInt(2, pId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        PlayerDetailDTO detail = new PlayerDetailDTO();
                        PlayerInfoDTO dto = new PlayerInfoDTO();
                        dto.playerId = rs.getInt("id");
                        dto.accountId = rs.getInt("account_id");
                        dto.name = rs.getString("name");
                        dto.level = rs.getInt("level");
                        dto.exp = rs.getLong("exp");
                        dto.beri = rs.getLong("vang");
                        dto.ruby = rs.getInt("ruby");
                        dto.extol = rs.getLong("vnd");
                        dto.ticket = rs.getInt("ticket");
                        dto.bossKey = rs.getInt("key_boss");
                        dto.isOnline = false;
                        dto.isBanned = rs.getInt("is_lock") == 1;

                        try {
                            String siteStr = rs.getString("site");
                            if (siteStr != null) {
                                JSONArray siteJs = (JSONArray) JSONValue.parse(siteStr);
                                if (siteJs != null && siteJs.size() >= 3) {
                                    dto.mapId = Integer.parseInt(siteJs.get(0).toString());
                                    dto.zoneId = Integer.parseInt(siteJs.get(1).toString());
                                }
                            }
                        } catch (Exception ignore) {}

                        dto.mapName = "Map " + dto.mapId;
                        detail.basicInfo = dto;

                        // Parse Item JSON if available
                        try {
                            String itemJson = rs.getString("item");
                            if (itemJson != null && !itemJson.trim().isEmpty()) {
                                JSONObject itemObj = (JSONObject) JSONValue.parse(itemJson);
                                if (itemObj != null) {
                                    // Parse bag47
                                    if (itemObj.containsKey("bag47")) {
                                        JSONArray b47 = (JSONArray) itemObj.get("bag47");
                                        for (Object o : b47) {
                                            JSONArray arr = (JSONArray) o;
                                            if (arr != null && arr.size() >= 3) {
                                                SimpleItemDTO s = new SimpleItemDTO();
                                                s.category = Integer.parseInt(arr.get(0).toString());
                                                s.id = Integer.parseInt(arr.get(1).toString());
                                                s.quantity = Integer.parseInt(arr.get(2).toString());
                                                s.location = "Hành trang";
                                                if (s.category == 4) detail.bagItems4.add(s);
                                                else if (s.category == 7) detail.bagItems7.add(s);
                                            }
                                        }
                                    }
                                    // Parse box47
                                    if (itemObj.containsKey("box47")) {
                                        JSONArray b47 = (JSONArray) itemObj.get("box47");
                                        for (Object o : b47) {
                                            JSONArray arr = (JSONArray) o;
                                            if (arr != null && arr.size() >= 3) {
                                                SimpleItemDTO s = new SimpleItemDTO();
                                                s.category = Integer.parseInt(arr.get(0).toString());
                                                s.id = Integer.parseInt(arr.get(1).toString());
                                                s.quantity = Integer.parseInt(arr.get(2).toString());
                                                s.location = "Rương đồ";
                                                if (s.category == 4) detail.boxItems4.add(s);
                                                else if (s.category == 7) detail.boxItems7.add(s);
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception ignore) {}

                        return detail;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ItemWearDTO convertItemWearToDTO(Item_wear it, int index, String location) {
        ItemWearDTO dto = new ItemWearDTO();
        dto.index = index;
        dto.location = location;
        if (it != null && it.template != null) {
            dto.id = it.template.id;
            dto.name = it.template.name != null ? it.template.name : "Trang bị ID " + it.template.id;
            dto.icon = it.template.icon;
            dto.typeEquip = it.template.typeEquip;
            dto.clazz = it.template.clazz;
            dto.color = it.getColor();
            dto.level = it.template.level;
            dto.levelUp = it.levelUp;
            dto.isHoanMy = it.isHoanMy;
            dto.valueKichAn = it.valueKichAn;
            dto.numLoKham = it.numLoKham;
            dto.mdakham = it.mdakham != null ? it.mdakham : new short[0];

            if (it.option_item != null) {
                for (Option op : it.option_item) {
                    if (op != null) {
                        dto.options.add(new OptionDTO(op.id, getOptionNameById(op.id), op.getParam()));
                    }
                }
            }
            if (it.option_item_2 != null) {
                for (Option op : it.option_item_2) {
                    if (op != null) {
                        dto.options.add(new OptionDTO(op.id, getOptionNameById(op.id) + " (Kích ẩn)", op.getParam()));
                    }
                }
            }
        }
        return dto;
    }

    public static String getOptionNameById(int optionId) {
        ItemOptionTemplate tpl = ItemOptionTemplate.get(optionId);
        if (tpl != null && tpl.name != null && !tpl.name.isEmpty()) {
            return tpl.name;
        }
        return "Chỉ số #" + optionId;
    }

    private static JSONObject parseJsonObj(String str) {
        if (str == null || str.trim().isEmpty()) return new JSONObject();
        try {
            Object obj = JSONValue.parse(str.trim());
            if (obj instanceof JSONObject) return (JSONObject) obj;
        } catch (Exception ignored) {}
        return new JSONObject();
    }

    private static long getJsonLong(JSONObject obj, String key, long def) {
        if (obj == null) return def;
        Object val = obj.get(key);
        if (val instanceof Number) return ((Number) val).longValue();
        if (val instanceof String) {
            try { return Long.parseLong((String) val); } catch (Exception ignored) {}
        }
        return def;
    }

    private static int getJsonInt(JSONObject obj, String key, int def) {
        return (int) getJsonLong(obj, key, def);
    }

    /**
     * Buff tài nguyên, tiền tệ, chỉ số cho người chơi.
     */
    public boolean buffPlayer(String playerName, String type, long amount) {
        Player onlineP = getOnlinePlayer(playerName);
        if (onlineP != null) {
            try {
                switch (type.toLowerCase()) {
                    case "beri":
                    case "vang":
                        onlineP.update_vang(amount);
                        onlineP.updateMoney();
                        break;
                    case "ruby":
                    case "ngoc":
                        onlineP.update_ngoc((int) amount);
                        onlineP.updateMoney();
                        break;
                    case "vnd":
                    case "extol":
                        onlineP.updateVnd(amount);
                        onlineP.updateMoney();
                        break;
                    case "exp":
                        onlineP.update_exp(amount, false);
                        break;
                    case "level":
                    case "lvl":
                        onlineP.level = (short) Math.max(1, amount);
                        onlineP.update_info_to_all();
                        break;
                    case "sp":
                    case "point":
                    case "tiemnang":
                        onlineP.pointSkill += (int) amount;
                        break;
                    case "haki":
                        onlineP.pointAttribute += (long) amount;
                        break;
                    case "ticket":
                    case "ve":
                        onlineP.ticket = (short) Math.max(0, onlineP.ticket + amount);
                        break;
                    case "key":
                    case "key_boss":
                        onlineP.key_boss = (short) Math.max(0, onlineP.key_boss + amount);
                        break;
                    case "hpmp":
                    case "heal":
                        onlineP.hp = onlineP.hpMax;
                        onlineP.mp = onlineP.mpMax;
                        break;
                    case "full":
                        onlineP.update_vang(500_000_000);
                        onlineP.update_ngoc(500_000);
                        onlineP.updateVnd(500_000_000);
                        onlineP.pointSkill += 5000;
                        onlineP.pointAttribute += 1000;
                        onlineP.ticket = (short) 99;
                        onlineP.key_boss = (short) 99;
                        onlineP.hp = onlineP.hpMax;
                        onlineP.mp = onlineP.mpMax;
                        onlineP.updateMoney();
                        break;
                    default:
                        return false;
                }
                if (onlineP.getService() != null) {
                    onlineP.getService().send_box_ThongBao_OK("Admin đã buff [" + type + "] số lượng: " + amount);
                }
                zLog.gI().add_log_by_name(onlineP.name, "ADMIN_BUFF", "Buffed " + type + " amount=" + amount);
                return true;
            } catch (Exception e) {
                Log.error("PanelManager", "buffPlayer online error: " + e.getMessage());
            }
        }

        // Nếu offline, cập nhật an toàn vào các cột JSON: inventory, level, potential, exp
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return false;
            int playerId = -1;
            String invStr = null;
            String lvlStr = null;
            long currentExp = 0;
            String potStr = null;

            try (PreparedStatement psSel = conn.prepareStatement(
                    "SELECT `id`, `inventory`, `level`, `exp`, `potential` FROM `players` WHERE `name` = ? LIMIT 1")) {
                psSel.setString(1, playerName);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        playerId = rs.getInt("id");
                        invStr = rs.getString("inventory");
                        lvlStr = rs.getString("level");
                        currentExp = rs.getLong("exp");
                        potStr = rs.getString("potential");
                    }
                }
            }
            if (playerId <= 0) return false;

            JSONObject inv = parseJsonObj(invStr);
            JSONObject pointInven = inv.get("point_inven") instanceof JSONObject ? (JSONObject) inv.get("point_inven") : new JSONObject();
            inv.put("point_inven", pointInven);
            JSONObject site = inv.get("site") instanceof JSONObject ? (JSONObject) inv.get("site") : new JSONObject();
            inv.put("site", site);

            boolean updateInv = false;
            boolean updateLvlExp = false;
            boolean updatePot = false;
            long newExp = currentExp;
            JSONObject lvlObj = parseJsonObj(lvlStr);
            JSONObject potObj = parseJsonObj(potStr);

            switch (type.toLowerCase()) {
                case "beri":
                case "vang":
                    long curGold = getJsonLong(pointInven, "gold", 0L);
                    pointInven.put("gold", Math.max(0L, curGold + amount));
                    updateInv = true;
                    break;
                case "ruby":
                case "ngoc":
                    int curRuby = getJsonInt(inv, "ruby", 0);
                    inv.put("ruby", Math.max(0, curRuby + (int) amount));
                    updateInv = true;
                    break;
                case "vnd":
                case "extol":
                    long curVnd = getJsonLong(pointInven, "vnd", 0L);
                    pointInven.put("vnd", Math.max(0L, curVnd + amount));
                    updateInv = true;
                    break;
                case "ticket":
                case "ve":
                    short curTk = (short) getJsonInt(site, "tk", 0);
                    site.put("tk", (short) Math.max(0, curTk + amount));
                    updateInv = true;
                    break;
                case "key":
                case "key_boss":
                    short curKey = (short) getJsonInt(site, "boss", 0);
                    site.put("boss", (short) Math.max(0, curKey + amount));
                    updateInv = true;
                    break;
                case "exp":
                    newExp = Math.max(0L, currentExp + amount);
                    lvlObj.put("exp", newExp);
                    updateLvlExp = true;
                    break;
                case "level":
                case "lvl":
                    int targetLv = (int) Math.max(1, amount);
                    lvlObj.put("lv", targetLv);
                    updateLvlExp = true;
                    break;
                case "sp":
                case "point":
                case "tiemnang":
                    int skPts = getJsonInt(potObj, "skPts", 0) + (int) amount;
                    potObj.put("skPts", Math.max(0, skPts));
                    updatePot = true;
                    break;
                case "haki":
                    long pts = getJsonLong(potObj, "pts", 0L) + amount;
                    potObj.put("pts", Math.max(0L, pts));
                    updatePot = true;
                    break;
                case "full":
                    pointInven.put("gold", 500_000_000L);
                    inv.put("ruby", 500_000);
                    pointInven.put("vnd", 500_000_000L);
                    site.put("tk", (short) 99);
                    site.put("boss", (short) 99);
                    site.put("hp", 999999);
                    site.put("mp", 999999);
                    potObj.put("skPts", 5000);
                    potObj.put("pts", 1000L);
                    updateInv = true;
                    updatePot = true;
                    break;
                default:
                    return false;
            }

            if (updateInv) {
                try (PreparedStatement psInv = conn.prepareStatement("UPDATE `players` SET `inventory` = ? WHERE `id` = ?")) {
                    psInv.setString(1, inv.toJSONString());
                    psInv.setInt(2, playerId);
                    psInv.executeUpdate();
                }
            }
            if (updateLvlExp) {
                try (PreparedStatement psLvl = conn.prepareStatement("UPDATE `players` SET `level` = ?, `exp` = ? WHERE `id` = ?")) {
                    psLvl.setString(1, lvlObj.toJSONString());
                    psLvl.setLong(2, newExp);
                    psLvl.setInt(3, playerId);
                    psLvl.executeUpdate();
                }
            }
            if (updatePot) {
                try (PreparedStatement psPot = conn.prepareStatement("UPDATE `players` SET `potential` = ? WHERE `id` = ?")) {
                    psPot.setString(1, potObj.toJSONString());
                    psPot.setInt(2, playerId);
                    psPot.executeUpdate();
                }
            }

            database.CacheManager.gI().remove("players_" + playerName);
            zLog.gI().add_log_by_name(playerName, "ADMIN_BUFF_OFFLINE", "Buffed offline " + type + " amount=" + amount);
            return true;
        } catch (Exception e) {
            Log.error("PanelManager", "buffPlayer offline error: " + e.getMessage());
        }
        return false;
    }

    public boolean setPlayerMoney(String playerName, long beri, int ruby, long vnd) {
        Player onlineP = getOnlinePlayer(playerName);
        if (onlineP != null) {
            try {
                onlineP.vang = Math.max(0, beri);
                onlineP.ruby = Math.max(0, ruby);
                onlineP.vnd = Math.max(0, vnd);
                onlineP.updateMoney();
                if (onlineP.getService() != null) {
                    onlineP.getService().send_box_ThongBao_OK("Tài sản nhân vật đã được Admin cập nhật!");
                }
                zLog.gI().add_log_by_name(onlineP.name, "SET_MONEY", "Beri=" + beri + ", Ruby=" + ruby + ", VND=" + vnd);
                return true;
            } catch (Exception e) {
                Log.error("PanelManager", "setPlayerMoney error: " + e.getMessage());
            }
        }

        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return false;
            int playerId = -1;
            String invStr = null;
            try (PreparedStatement psSel = conn.prepareStatement("SELECT `id`, `inventory` FROM `players` WHERE `name` = ? LIMIT 1")) {
                psSel.setString(1, playerName);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        playerId = rs.getInt("id");
                        invStr = rs.getString("inventory");
                    }
                }
            }
            if (playerId <= 0) return false;

            JSONObject inv = parseJsonObj(invStr);
            JSONObject pointInven = inv.get("point_inven") instanceof JSONObject ? (JSONObject) inv.get("point_inven") : new JSONObject();
            pointInven.put("gold", Math.max(0L, beri));
            pointInven.put("vnd", Math.max(0L, vnd));
            inv.put("point_inven", pointInven);
            inv.put("ruby", Math.max(0, ruby));

            try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players` SET `inventory` = ? WHERE `id` = ?")) {
                psUp.setString(1, inv.toJSONString());
                psUp.setInt(2, playerId);
                int updated = psUp.executeUpdate();
                if (updated > 0) {
                    database.CacheManager.gI().remove("players_" + playerName);
                    zLog.gI().add_log_by_name(playerName, "SET_MONEY_OFFLINE", "Beri=" + beri + ", Ruby=" + ruby + ", VND=" + vnd);
                    return true;
                }
            }
        } catch (Exception e) {
            Log.error("PanelManager", "setPlayerMoney offline error: " + e.getMessage());
        }
        return false;
    }

    public boolean setPlayerLevel(String playerName, int targetLevel) {
        if (targetLevel < 1) targetLevel = 1;
        Player onlineP = getOnlinePlayer(playerName);
        if (onlineP != null) {
            try {
                onlineP.level = (short) targetLevel;
                onlineP.update_info_to_all();
                if (onlineP.getService() != null) {
                    onlineP.getService().send_box_ThongBao_OK("Cấp độ nhân vật đã được Admin thiết lập: " + targetLevel);
                }
                zLog.gI().add_log_by_name(onlineP.name, "SET_LEVEL", "Set level to " + targetLevel);
                return true;
            } catch (Exception e) {
                Log.error("PanelManager", "setPlayerLevel error: " + e.getMessage());
            }
        }

        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return false;
            int playerId = -1;
            String lvlStr = null;
            try (PreparedStatement psSel = conn.prepareStatement("SELECT `id`, `level` FROM `players` WHERE `name` = ? LIMIT 1")) {
                psSel.setString(1, playerName);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        playerId = rs.getInt("id");
                        lvlStr = rs.getString("level");
                    }
                }
            }
            if (playerId <= 0) return false;

            JSONObject lvlObj = parseJsonObj(lvlStr);
            lvlObj.put("lv", Math.max(1, targetLevel));

            try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players` SET `level` = ? WHERE `id` = ?")) {
                psUp.setString(1, lvlObj.toJSONString());
                psUp.setInt(2, playerId);
                int updated = psUp.executeUpdate();
                if (updated > 0) {
                    database.CacheManager.gI().remove("players_" + playerName);
                    zLog.gI().add_log_by_name(playerName, "SET_LEVEL_OFFLINE", "Set level to " + targetLevel);
                    return true;
                }
            }
        } catch (Exception e) {
            Log.error("PanelManager", "setPlayerLevel offline error: " + e.getMessage());
        }
        return false;
    }

    public boolean resetPlayerLocation(String playerName) {
        Player onlineP = getOnlinePlayer(playerName);
        if (onlineP != null) {
            try {
                TimedDungeonManager.teleportPlayerToMap(onlineP, 1);
                if (onlineP.getService() != null) {
                    onlineP.getService().send_box_ThongBao_OK("Bạn đã được Admin cứu kẹt về Làng Cối Xay Gió (Map 1)!");
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return false;
            int playerId = -1;
            String invStr = null;
            try (PreparedStatement psSel = conn.prepareStatement("SELECT `id`, `inventory` FROM `players` WHERE `name` = ? LIMIT 1")) {
                psSel.setString(1, playerName);
                try (ResultSet rs = psSel.executeQuery()) {
                    if (rs.next()) {
                        playerId = rs.getInt("id");
                        invStr = rs.getString("inventory");
                    }
                }
            }
            if (playerId <= 0) return false;

            JSONObject inv = parseJsonObj(invStr);
            JSONObject site = inv.get("site") instanceof JSONObject ? (JSONObject) inv.get("site") : new JSONObject();
            site.put("map", 1);
            site.put("zone", 0);
            site.put("x", 830);
            site.put("y", 203);
            site.put("hp", 1000);
            site.put("mp", 1000);
            inv.put("site", site);

            try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players` SET `inventory` = ? WHERE `id` = ?")) {
                psUp.setString(1, inv.toJSONString());
                psUp.setInt(2, playerId);
                int updated = psUp.executeUpdate();
                if (updated > 0) {
                    database.CacheManager.gI().remove("players_" + playerName);
                    zLog.gI().add_log_by_name(playerName, "UNSTUCK_OFFLINE", "Reset location to Map 1");
                    return true;
                }
            }
        } catch (Exception e) {
            Log.error("PanelManager", "resetPlayerLocation offline error: " + e.getMessage());
        }
        return false;
    }

    public boolean teleportPlayer(String playerName, int mapId, short x, short y) {
        Player onlineP = getOnlinePlayer(playerName);
        if (onlineP != null) {
            try {
                TimedDungeonManager.teleportPlayerToMap(onlineP, mapId);
                onlineP.x = x;
                onlineP.y = y;
                onlineP.xold = x;
                onlineP.yold = y;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean mutePlayer(String playerName, int durationMinutes, String reason) {
        Player onlineP = getOnlinePlayer(playerName);
        if (onlineP != null) {
            onlineP.isMute = true;
            if (onlineP.getService() != null) {
                onlineP.getService().send_box_ThongBao_OK("Bạn bị cấm chat trong " + durationMinutes + " phút. Lý do: " + reason);
            }
            zLog.gI().add_log_by_name(onlineP.name, "MUTE_PLAYER", "Muted for " + durationMinutes + " mins. Reason: " + reason);
            return true;
        }
        return false;
    }

    public boolean unmutePlayer(String playerName) {
        Player onlineP = getOnlinePlayer(playerName);
        if (onlineP != null) {
            onlineP.isMute = false;
            if (onlineP.getService() != null) {
                onlineP.getService().send_box_ThongBao_OK("Bạn đã được Admin mở cấm chat!");
            }
            zLog.gI().add_log_by_name(onlineP.name, "UNMUTE_PLAYER", "Unmuted.");
            return true;
        }
        return false;
    }

    public boolean banPlayer(String playerName, String reason) {
        int accountId = -1;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("SELECT account_id FROM players WHERE name = ?")) {
            ps.setString(1, playerName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    accountId = rs.getInt("account_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (accountId <= 0) return false;

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("UPDATE `account` SET `lock` = 1 WHERE `id` = ?")) {
            ps.setInt(1, accountId);
            ps.executeUpdate();
            kickPlayer(playerName, "Tài khoản của bạn đã bị khóa bởi Admin. Lý do: " + reason);
            zLog.gI().add_log_by_name(playerName, "BAN_PLAYER", "Banned account ID " + accountId + ". Reason: " + reason);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unbanPlayer(String identifier) {
        int accountId = -1;
        try (Connection conn = DbManager.gI().getConnect()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM `account` WHERE username = ?")) {
                ps.setString(1, identifier);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) accountId = rs.getInt("id");
                }
            }
            if (accountId <= 0) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT account_id FROM `players` WHERE name = ?")) {
                    ps.setString(1, identifier);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) accountId = rs.getInt("account_id");
                    }
                }
            }
            if (accountId <= 0) return false;

            try (PreparedStatement ps = conn.prepareStatement("UPDATE `account` SET `lock` = 0 WHERE `id` = ?")) {
                ps.setInt(1, accountId);
                ps.executeUpdate();
                zLog.gI().add_log("ADMIN_PANEL", "UNBAN_ACCOUNT", "Unbanned account ID " + accountId + " (" + identifier + ")");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changeAccountPassword(String identifier, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) return false;
        try (Connection conn = DbManager.gI().getConnect()) {
            int accountId = -1;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM `account` WHERE username = ?")) {
                ps.setString(1, identifier);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) accountId = rs.getInt("id");
                }
            }
            if (accountId <= 0) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT account_id FROM `players` WHERE name = ?")) {
                    ps.setString(1, identifier);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) accountId = rs.getInt("account_id");
                    }
                }
            }
            if (accountId <= 0) return false;

            try (PreparedStatement ps = conn.prepareStatement("UPDATE `account` SET `password` = ? WHERE `id` = ?")) {
                ps.setString(1, newPassword.trim());
                ps.setInt(2, accountId);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    zLog.gI().add_log("ADMIN_PANEL", "CHANGE_PASSWORD", "Changed password for account ID " + accountId);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================================================================
    // 4. PHÂN HỆ QUẢN LÝ PHÓ BẢN (TIMED DUNGEONS) & SỰ KIỆN (EVENTS)
    // =========================================================================

    public List<DungeonStatusDTO> getAllDungeonsStatus() {
        List<DungeonStatusDTO> list = new ArrayList<>();
        Map<String, ScheduleConfig> configs = TimedDungeonManager.gI().getConfigs();

        for (Map.Entry<String, ScheduleConfig> entry : configs.entrySet()) {
            ScheduleConfig cfg = entry.getValue();
            if (cfg == null) continue;
            DungeonStatusDTO dto = new DungeonStatusDTO();
            dto.id = cfg.id;
            dto.name = cfg.name;
            dto.isEnabled = cfg.isEnabled;
            dto.timingMode = cfg.timingMode;
            dto.state = cfg.state;
            dto.stateName = (cfg.state == ScheduleConfig.STATE_RUNNING) ? "Đang chạy" : "Đang chờ";
            dto.remainingSeconds = Math.max(0, (cfg.nextStateChangeTime - System.currentTimeMillis()) / 1000);
            dto.roundIndex = cfg.roundIndex;
            dto.runDurationMinutes = cfg.runDurationMinutes;
            dto.cooldownMinutes = cfg.cooldownMinutes;
            dto.timeRangeInfo = cfg.getTimeRangeString();
            dto.rewardInfo = cfg.rewardInfo != null ? cfg.rewardInfo : "";
            list.add(dto);
        }
        return list;
    }

    public boolean toggleDungeon(String dungeonId, boolean enable) {
        TimedDungeonManager.gI().toggleDungeon(dungeonId, enable);
        zLog.gI().add_log("ADMIN_PANEL", "DUNGEON_TOGGLE", "Toggled dungeon " + dungeonId + " active=" + enable);
        return true;
    }

    public boolean forceStartDungeon(String dungeonId) {
        TimedDungeonManager.gI().forceStartDungeon(dungeonId);
        zLog.gI().add_log("ADMIN_PANEL", "DUNGEON_FORCE_START", "Force started dungeon: " + dungeonId);
        return true;
    }

    public boolean forceEndDungeon(String dungeonId) {
        TimedDungeonManager.gI().forceEndDungeon(dungeonId);
        zLog.gI().add_log("ADMIN_PANEL", "DUNGEON_FORCE_END", "Force ended dungeon: " + dungeonId);
        return true;
    }

    public boolean setDungeonCycle(String dungeonId, int runMin, int cdMin) {
        TimedDungeonManager.gI().setCycleDuration(dungeonId, runMin, cdMin);
        zLog.gI().add_log("ADMIN_PANEL", "DUNGEON_SET_CYCLE", "Set dungeon " + dungeonId + " runMin=" + runMin + " cdMin=" + cdMin);
        return true;
    }

    public boolean setMasterDungeonSwitch(boolean enable) {
        TimedDungeonManager.gI().setMasterSwitch(enable);
        zLog.gI().add_log("ADMIN_PANEL", "DUNGEON_MASTER_SWITCH", "Master switch=" + enable);
        return true;
    }

    public List<EventInfoDTO> getAllEventsConfig() {
        List<EventInfoDTO> list = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();

        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT `value` FROM `settings` WHERE `key_name` = 'event_config' OR `id` = 22 ORDER BY (`key_name` = 'event_config') DESC LIMIT 1");
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String rawConfig = rs.getString("value");
                        if (rawConfig != null && !rawConfig.trim().isEmpty()) {
                            List<EventManager.EventConfigEntry> entries = EventManager.parseEventConfig(rawConfig);
                            int autoId = 1;
                            for (EventManager.EventConfigEntry entry : entries) {
                                EventInfoDTO dto = new EventInfoDTO();
                                dto.id = autoId++;
                                dto.eventId = entry.eventId;
                                dto.time = entry.time != null ? entry.time : "";
                                dto.timeend = entry.timeend != null ? entry.timeend : "";
                                dto.timex2pay = entry.timex2pay != null ? entry.timex2pay : "";
                                dto.timechangeitem = entry.timechangeitem != null ? entry.timechangeitem : "";
                                dto.timedropitem = entry.timedropitem != null ? entry.timedropitem : "";
                                dto.timeremoveitem = entry.timeremoveitem != null ? entry.timeremoveitem : "";
                                dto.season = entry.season != null ? entry.season : "";
                                dto.isActive = entry.isActive == 1;

                                Event ev = EventManager.gI().getEvent(dto.eventId);
                                dto.name = ev != null ? ev.getName() : "Sự kiện ID " + dto.eventId;
                                dto.seasonName = (entry.season != null && !entry.season.trim().isEmpty())
                                        ? entry.season.trim()
                                        : getEventSeasonName(dto.eventId);
                                seen.add(dto.eventId);
                                list.add(dto);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= 17; i++) {
            Event ev = EventManager.gI().getEvent(i);
            if (ev != null && !seen.contains(i)) {
                EventInfoDTO dto = new EventInfoDTO();
                dto.id = list.size() + 1;
                dto.eventId = i;
                dto.name = ev.getName();
                dto.isActive = false;
                dto.seasonName = getEventSeasonName(i);
                list.add(dto);
                seen.add(i);
            }
        }

        return list;
    }

    public String getEventSeasonName(int eventId) {
        switch (eventId) {
            case 1: return "Trung Thu / Mùa Thu";
            case 2: return "Halloween (Tháng 10)";
            case 3: return "Giáng Sinh / Noel (Tháng 12)";
            case 4: return "Tết Nguyên Đán (Tháng 1 - 2)";
            case 5: return "Giỗ Tổ Hùng Vương (Tháng 3 - 4)";
            case 6: return "Sự Kiện Hè (Tháng 6 - 8)";
            default: return "Quanh Năm / Mùa Thường";
        }
    }

    public synchronized boolean toggleEvent(int eventId, boolean active) {
        List<EventInfoDTO> list = getAllEventsConfig();
        boolean found = false;
        for (EventInfoDTO dto : list) {
            if (dto.eventId == eventId) {
                dto.isActive = active;
                found = true;
                break;
            }
        }
        if (!found && active) {
            EventInfoDTO newDto = new EventInfoDTO();
            newDto.eventId = eventId;
            newDto.isActive = true;
            list.add(newDto);
        }
        boolean saved = saveEventConfigToDB(list);
        if (saved) {
            EventManager.gI().reloadConfigs();
            zLog.gI().add_log("ADMIN_PANEL", "EVENT_TOGGLE", "Toggled Event " + eventId + " active=" + active);
            return true;
        }
        return false;
    }

    public synchronized boolean addOrUpdateEventSchedule(int scheduleId, int eventId, String time, String timex2pay,
            String timechangeitem, String timedropitem, String timeremoveitem, boolean isActive) {
        List<EventInfoDTO> list = getAllEventsConfig();
        boolean found = false;
        if (scheduleId > 0) {
            for (EventInfoDTO dto : list) {
                if (dto.id == scheduleId) {
                    dto.eventId = eventId;
                    dto.time = time != null ? time : "";
                    dto.timex2pay = timex2pay != null ? timex2pay : "";
                    dto.timechangeitem = timechangeitem != null ? timechangeitem : "";
                    dto.timedropitem = timedropitem != null ? timedropitem : "";
                    dto.timeremoveitem = timeremoveitem != null ? timeremoveitem : "";
                    dto.isActive = isActive;
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            EventInfoDTO newDto = new EventInfoDTO();
            newDto.id = scheduleId > 0 ? scheduleId : list.size() + 1;
            newDto.eventId = eventId;
            newDto.time = time != null ? time : "";
            newDto.timex2pay = timex2pay != null ? timex2pay : "";
            newDto.timechangeitem = timechangeitem != null ? timechangeitem : "";
            newDto.timedropitem = timedropitem != null ? timedropitem : "";
            newDto.timeremoveitem = timeremoveitem != null ? timeremoveitem : "";
            newDto.isActive = isActive;
            list.add(newDto);
        }
        boolean saved = saveEventConfigToDB(list);
        if (saved) {
            EventManager.gI().reloadConfigs();
            zLog.gI().add_log("ADMIN_PANEL", "EVENT_UPDATE", "Updated Event " + eventId + " schedule");
            return true;
        }
        return false;
    }

    private boolean saveEventConfigToDB(List<EventInfoDTO> list) {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return false;
            StringBuilder sb = new StringBuilder();
            sb.append("#event_id,is_active,time,timedropitem,timechangeitem,timeremoveitem,timex2pay,key\n");
            for (EventInfoDTO dto : list) {
                sb.append(dto.eventId).append(",")
                  .append(dto.isActive ? "1" : "0").append(",")
                  .append(dto.time != null ? dto.time : "").append(",")
                  .append(dto.timedropitem != null ? dto.timedropitem : "").append(",")
                  .append(dto.timechangeitem != null ? dto.timechangeitem : "").append(",")
                  .append(dto.timeremoveitem != null ? dto.timeremoveitem : "").append(",")
                  .append(dto.timex2pay != null ? dto.timex2pay : "").append(",")
                  .append(dto.season != null ? dto.season : "").append("\n");
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO `settings` (`id`, `key_name`, `value`, `description`) "
                    + "VALUES (22, 'event_config', ?, 'Cấu hình thời gian các sự kiện') "
                    + "ON DUPLICATE KEY UPDATE `value` = VALUES(`value`), `description` = VALUES(`description`)")) {
                ps.setString(1, sb.toString().trim());
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // 5. PHÂN HỆ QUẢN LÝ GIFTCODE (GIFTCODE CRUD)
    // =========================================================================

    public List<GiftCodeDTO> getAllGiftCodes(String searchKey) {
        List<GiftCodeDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM `giftcode` WHERE 1=1");
        if (searchKey != null && !searchKey.trim().isEmpty()) {
            sql.append(" AND (`giftname` LIKE ? OR `thongbao` LIKE ?)");
        }
        sql.append(" ORDER BY `luotnhap` DESC, `giftname` ASC");

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (searchKey != null && !searchKey.trim().isEmpty()) {
                String kw = "%" + searchKey.trim() + "%";
                ps.setString(1, kw);
                ps.setString(2, kw);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GiftCodeDTO dto = new GiftCodeDTO();
                    dto.giftname = rs.getString("giftname");
                    dto.luotnhap = rs.getInt("luotnhap");
                    dto.gioihan = rs.getInt("gioihan");
                    dto.notice = rs.getString("thongbao");
                    dto.beri = rs.getInt("beri");
                    dto.ruby = rs.getInt("ruby");
                    dto.itemJson = rs.getString("item");
                    dto.used = rs.getString("used");
                    dto.special = rs.getString("special");
                    dto.mtv = rs.getByte("mtv");
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean saveOrUpdateGiftCode(GiftCodeDTO dto) {
        if (dto == null || dto.giftname == null || dto.giftname.trim().isEmpty()) return false;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO `giftcode` (`giftname`, `luotnhap`, `gioihan`, `thongbao`, `beri`, `ruby`, `item`, `used`, `special`, `mtv`) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                     + "ON DUPLICATE KEY UPDATE `gioihan` = VALUES(`gioihan`), `thongbao` = VALUES(`thongbao`), `beri` = VALUES(`beri`), "
                     + "`ruby` = VALUES(`ruby`), `item` = VALUES(`item`), `special` = VALUES(`special`), `mtv` = VALUES(`mtv`)")) {
            ps.setString(1, dto.giftname.trim());
            ps.setInt(2, dto.luotnhap);
            ps.setInt(3, dto.gioihan);
            ps.setString(4, dto.notice != null ? dto.notice : "");
            ps.setInt(5, dto.beri);
            ps.setInt(6, dto.ruby);
            ps.setString(7, dto.itemJson != null ? dto.itemJson : "[]");
            ps.setString(8, dto.used != null ? dto.used : "");
            ps.setString(9, dto.special != null ? dto.special : "");
            ps.setByte(10, dto.mtv);
            ps.executeUpdate();
            zLog.gI().add_log("ADMIN_PANEL", "GIFTCODE_SAVE", "Saved giftcode: " + dto.giftname);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGiftCode(String code) {
        if (code == null || code.trim().isEmpty()) return false;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM `giftcode` WHERE `giftname` = ?")) {
            ps.setString(1, code.trim());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                zLog.gI().add_log("ADMIN_PANEL", "GIFTCODE_DELETE", "Deleted giftcode: " + code);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean resetGiftCodeUsed(String code) {
        if (code == null || code.trim().isEmpty()) return false;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("UPDATE `giftcode` SET `used` = '', `luotnhap` = 0 WHERE `giftname` = ?")) {
            ps.setString(1, code.trim());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                zLog.gI().add_log("ADMIN_PANEL", "GIFTCODE_RESET", "Reset used list for giftcode: " + code);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================================================================
    // 6. PHÂN HỆ GỬI THƯ & QUÀ MAIL (MAIL SENDER)
    // =========================================================================

    public boolean sendMailToPlayer(String targetName, String sender, String title, String content,
                                    List<GiftBox> gifts, long durationMs) {
        if (targetName == null || targetName.trim().isEmpty()) return false;
        String tName = targetName.trim();

        Player onlineP = getOnlinePlayer(tName);
        if (onlineP != null) {
            MailService.sendMail(onlineP, sender, title, content, MailService.MAIL_TYPE_GIFT, false, gifts, durationMs);
            zLog.gI().add_log_by_name(onlineP.name, "MAIL_SEND", "Sent mail '" + title + "' to " + onlineP.name);
            return true;
        }

        // Nếu offline, query id & account_id từ DB
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("SELECT id, account_id, name FROM players WHERE name = ? OR id = ? LIMIT 1")) {
            ps.setString(1, tName);
            int pId = -1;
            try { pId = Integer.parseInt(tName); } catch (Exception ignore) {}
            ps.setInt(2, pId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int playerId = rs.getInt("id");
                    int accountId = rs.getInt("account_id");
                    String realName = rs.getString("name");
                    MailService.sendMailOffline(playerId, accountId, realName, sender, title, content, MailService.MAIL_TYPE_GIFT, false, gifts, durationMs);
                    zLog.gI().add_log_by_name(realName, "MAIL_SEND_OFFLINE", "Sent offline mail '" + title + "' to " + realName);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int sendMailToAllOnline(String sender, String title, String content, List<GiftBox> gifts, long durationMs) {
        int count = 0;
        Set<Integer> sent = new HashSet<>();
        if (SessionManager.CLIENT_ENTRYS != null) {
            for (Session ss : SessionManager.CLIENT_ENTRYS) {
                if (ss != null && ss.connected && ss.p != null) {
                    Player p = ss.p;
                    if (!p.isBot && !p.isDe && !p.isClosed && !sent.contains(p.IDPlayer)) {
                        sent.add(p.IDPlayer);
                        MailService.sendMail(p, sender, title, content, MailService.MAIL_TYPE_GIFT, false, gifts, durationMs);
                        count++;
                    }
                }
            }
        }
        zLog.gI().add_log("ADMIN_PANEL", "MAIL_ALL_ONLINE", "Sent mail '" + title + "' to " + count + " online players.");
        return count;
    }

    public CompletableFuture<Integer> sendMailToServerAll(String sender, String title, String content, List<GiftBox> gifts, long durationMs) {
        return CompletableFuture.supplyAsync(() -> {
            int count = 0;
            try (Connection conn = DbManager.gI().getConnect();
                 PreparedStatement ps = conn.prepareStatement("SELECT id, account_id, name FROM players WHERE is_bot = 0");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int pId = rs.getInt("id");
                    int accId = rs.getInt("account_id");
                    String pName = rs.getString("name");
                    MailService.sendMailOffline(pId, accId, pName, sender, title, content, MailService.MAIL_TYPE_GIFT, false, gifts, durationMs);
                    count++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            zLog.gI().add_log("ADMIN_PANEL", "MAIL_SERVER_ALL", "Sent mail '" + title + "' to all " + count + " players in DB.");
            return count;
        });
    }

    // =========================================================================
    // 7. PHÂN HỆ CHỈNH SỬA TEMPLATE ITEM 3 & PET & TẠO ĐỒ CUSTOM
    // =========================================================================

    public List<Item3TemplateDTO> searchItem3Templates(String query, int limit) {
        List<Item3TemplateDTO> list = new ArrayList<>();
        if (ItemTemplate3.ENTRYS == null) return list;

        String q = query != null ? query.trim().toLowerCase() : "";
        for (ItemTemplate3 tpl : ItemTemplate3.ENTRYS) {
            if (tpl == null) continue;
            if (!q.isEmpty()) {
                boolean match = String.valueOf(tpl.id).equals(q)
                        || (tpl.name != null && tpl.name.toLowerCase().contains(q));
                if (!match) continue;
            }
            Item3TemplateDTO dto = new Item3TemplateDTO();
            dto.id = tpl.id;
            dto.name = tpl.name;
            dto.clazz = tpl.clazz;
            dto.typeEquip = tpl.typeEquip;
            dto.icon = tpl.icon;
            dto.level = tpl.level;
            dto.color = tpl.color;
            dto.typelock = tpl.typelock;
            dto.numHoleDaDuc = tpl.numHoleDaDuc;
            dto.chetac = tpl.valueChetac;
            dto.ishoanmy = tpl.isHoanMy;
            dto.beri = tpl.beri;
            dto.part = tpl.part;
            dto.numlokham = tpl.numLoKham;

            if (tpl.option_item != null) {
                for (Option op : tpl.option_item) {
                    if (op != null) dto.op1.add(new OptionDTO(op.id, getOptionNameById(op.id), op.getParam()));
                }
            }
            if (tpl.option_item_2 != null) {
                for (Option op : tpl.option_item_2) {
                    if (op != null) dto.op2.add(new OptionDTO(op.id, getOptionNameById(op.id), op.getParam()));
                }
            }
            if (tpl.mdakham != null) {
                for (short dk : tpl.mdakham) dto.mdakham.add(dk);
            }
            list.add(dto);
            if (list.size() >= limit) break;
        }
        return list;
    }

    public boolean saveItem3Template(Item3TemplateDTO dto) {
        if (dto == null || dto.id <= 0) return false;

        // 1. Build JSON Arrays for op_1, op_2, mdakham
        JSONArray jsOp1 = new JSONArray();
        for (OptionDTO op : dto.op1) {
            JSONArray pair = new JSONArray();
            pair.add(op.id);
            pair.add(op.param);
            jsOp1.add(pair);
        }

        JSONArray jsOp2 = new JSONArray();
        for (OptionDTO op : dto.op2) {
            JSONArray pair = new JSONArray();
            pair.add(op.id);
            pair.add(op.param);
            jsOp2.add(pair);
        }

        JSONArray jsMdakham = new JSONArray();
        for (Short dk : dto.mdakham) {
            jsMdakham.add(dk);
        }

        // 2. Update Database `item3`
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE `item3` SET `name` = ?, `clazz` = ?, `typeequip` = ?, `icon` = ?, `level` = ?, `color` = ?, "
                     + "`typelock` = ?, `numHoleDaDuc` = ?, `chetac` = ?, `ishoanmy` = ?, `beri` = ?, `op_1` = ?, `op_2` = ?, "
                     + "`numlokham` = ?, `mdakham` = ?, `part` = ? WHERE `id` = ?")) {
            ps.setString(1, dto.name);
            ps.setInt(2, dto.clazz);
            ps.setInt(3, dto.typeEquip);
            ps.setInt(4, dto.icon);
            ps.setInt(5, dto.level);
            ps.setInt(6, dto.color);
            ps.setInt(7, dto.typelock);
            ps.setInt(8, dto.numHoleDaDuc);
            ps.setInt(9, dto.chetac);
            ps.setInt(10, dto.ishoanmy);
            ps.setInt(11, dto.beri);
            ps.setString(12, jsOp1.toJSONString());
            ps.setString(13, jsOp2.toJSONString());
            ps.setInt(14, dto.numlokham);
            ps.setString(15, jsMdakham.toJSONString());
            ps.setInt(16, dto.part);
            ps.setInt(17, dto.id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // 3. Update In-Memory RAM `ItemTemplate3.ENTRYS`
        ItemTemplate3 tpl = ItemTemplate3.get_it_by_id(dto.id);
        if (tpl != null) {
            tpl.name = dto.name;
            tpl.clazz = (byte) dto.clazz;
            tpl.typeEquip = (byte) dto.typeEquip;
            tpl.icon = (short) dto.icon;
            tpl.level = (short) dto.level;
            tpl.color = (byte) dto.color;
            tpl.typelock = (byte) dto.typelock;
            tpl.numHoleDaDuc = (byte) dto.numHoleDaDuc;
            tpl.valueChetac = (short) dto.chetac;
            tpl.isHoanMy = (byte) dto.ishoanmy;
            tpl.beri = dto.beri;
            tpl.part = (short) dto.part;
            tpl.numLoKham = (byte) dto.numlokham;

            tpl.option_item = new ArrayList<>();
            for (OptionDTO op : dto.op1) {
                tpl.option_item.add(new Option(op.id, op.param));
            }
            tpl.option_item_2 = new ArrayList<>();
            for (OptionDTO op : dto.op2) {
                tpl.option_item_2.add(new Option(op.id, op.param));
            }
            tpl.mdakham = new short[dto.mdakham.size()];
            for (int i = 0; i < dto.mdakham.size(); i++) {
                tpl.mdakham[i] = dto.mdakham.get(i);
            }
        }

        zLog.gI().add_log("ADMIN_PANEL", "ITEM3_TEMPLATE_UPDATE", "Updated Item3 Template ID " + dto.id + " (" + dto.name + ")");
        return true;
    }

    public List<PetTemplateDTO> searchPetTemplates(String query) {
        List<PetTemplateDTO> list = new ArrayList<>();
        if (Pet.ENTRY == null) return list;

        String q = query != null ? query.trim().toLowerCase() : "";
        for (Pet pet : Pet.ENTRY) {
            if (pet == null) continue;
            if (!q.isEmpty()) {
                boolean match = String.valueOf(pet.id).equals(q)
                        || (pet.name != null && pet.name.toLowerCase().contains(q));
                if (!match) continue;
            }
            PetTemplateDTO dto = new PetTemplateDTO();
            dto.id = pet.id;
            dto.name = pet.name;
            dto.type = pet.type;
            dto.icon = pet.icon;
            dto.frame = pet.frame;
            if (pet.op != null) {
                for (Option op : pet.op) {
                    if (op != null) {
                        dto.options.add(new OptionDTO(op.id, getOptionNameById(op.id), op.getParam()));
                    }
                }
            }
            list.add(dto);
        }
        return list;
    }

    public boolean savePetTemplate(PetTemplateDTO dto) {
        if (dto == null || dto.id <= 0) return false;

        JSONArray jsOp = new JSONArray();
        for (OptionDTO op : dto.options) {
            JSONArray pair = new JSONArray();
            pair.add(op.id);
            pair.add(op.param);
            jsOp.add(pair);
        }

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE `pet_template` SET `name` = ?, `type` = ?, `icon` = ?, `frame` = ?, `op` = ? WHERE `id` = ?")) {
            ps.setString(1, dto.name);
            ps.setInt(2, dto.type);
            ps.setInt(3, dto.icon);
            ps.setInt(4, dto.frame);
            ps.setString(5, jsOp.toJSONString());
            ps.setInt(6, dto.id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        Pet tpl = Pet.getTemplate(dto.id);
        if (tpl != null) {
            tpl.name = dto.name;
            tpl.type = (byte) dto.type;
            tpl.icon = (short) dto.icon;
            tpl.frame = (short) dto.frame;
            tpl.op = new ArrayList<>();
            for (OptionDTO op : dto.options) {
                tpl.op.add(new Option(op.id, op.param));
            }
        }

        zLog.gI().add_log("ADMIN_PANEL", "PET_TEMPLATE_UPDATE", "Updated Pet Template ID " + dto.id + " (" + dto.name + ")");
        return true;
    }

    public List<ItemOptionTemplate> getAllOptionTemplates() {
        if (ItemOptionTemplate.ENTRYS != null) {
            return ItemOptionTemplate.ENTRYS;
        }
        return new ArrayList<>();
    }

    /**
     * Tạo một trang bị Item 3 với chỉ số custom và gửi trực tiếp vào túi người chơi hoặc qua thư.
     */
    public boolean giveCustomItem3(String playerName, int templateId, int color, int levelUp,
                                   int isHoanMy, int kichAn, List<Option> customOp1, List<Option> customOp2, short[] daKham) {
        Player onlineP = getOnlinePlayer(playerName);
        ItemTemplate3 tpl = ItemTemplate3.get_it_by_id(templateId);
        if (tpl == null) return false;

        Item_wear it = new Item_wear(tpl);
        it.color = (byte) color;
        it.levelUp = (byte) levelUp;
        it.isHoanMy = (byte) isHoanMy;
        it.valueKichAn = (byte) kichAn;
        if (daKham != null) {
            it.mdakham = daKham;
            it.numLoKham = (byte) daKham.length;
        }
        if (customOp1 != null && !customOp1.isEmpty()) {
            it.option_item = new ArrayList<>(customOp1);
        }
        if (customOp2 != null && !customOp2.isEmpty()) {
            it.option_item_2 = new ArrayList<>(customOp2);
        }

        if (onlineP != null && onlineP.item != null) {
            boolean added = onlineP.item.add_item_bag3(it);
            if (added) {
                if (onlineP.getService() != null) {
                    onlineP.getService().send_box_ThongBao_OK("Bạn nhận được trang bị đặc biệt từ Admin: " + it.template.name);
                }
                zLog.gI().add_log_by_name(onlineP.name, "CUSTOM_ITEM3_GIVEN", "Gave custom item3 " + tpl.name + " to " + onlineP.name);
                return true;
            }
        }

        // Nếu túi đầy hoặc player offline -> gửi qua MailService
        List<GiftBox> list = new ArrayList<>();
        GiftBox gb = new GiftBox(tpl, 1);
        gb.color = (byte) color;
        gb.options = new ArrayList<>();
        if (it.option_item != null) {
            for (Option op : it.option_item) {
                gb.options.add(new Option(op.id, op.getParam()));
            }
        }
        list.add(gb);

        return sendMailToPlayer(playerName, "BQT Admin", "Trang Bị Đặc Biệt", "Admin gửi tặng bạn trang bị đặc biệt.", list, 0);
    }

    // =========================================================================
    // 8. PHÂN HỆ TRA CỨU LỊCH SỬ SQL HISTORYS
    // =========================================================================

    public List<HistoryLogDTO> getHistoryLogs(Integer accountId, Integer playerId, String typeKey,
            String keywordSearch, int limit, int offset) {
        List<HistoryLogDTO> logs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT `id`, `account_id`, `player_id`, `type`, `data`, `create_at` FROM `historys` WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (accountId != null && accountId > 0) {
            sql.append(" AND `account_id` = ?");
            params.add(accountId);
        }
        if (playerId != null && playerId > 0) {
            sql.append(" AND `player_id` = ?");
            params.add(playerId);
        }
        if (typeKey != null && !typeKey.trim().isEmpty()) {
            if (typeKey.equalsIgnoreCase("NAP_THE") || typeKey.equalsIgnoreCase("RECHARGE_HISTORY")) {
                sql.append(" AND (`type` = 'NAP_THE' OR `type` = 'RECHARGE_HISTORY')");
            } else {
                sql.append(" AND `type` = ?");
                params.add(typeKey.trim());
            }
        }
        if (keywordSearch != null && !keywordSearch.trim().isEmpty()) {
            sql.append(" AND `data` LIKE ?");
            params.add("%" + keywordSearch.trim() + "%");
        }

        sql.append(" ORDER BY `id` DESC LIMIT ? OFFSET ?");
        params.add(limit > 0 ? limit : 50);
        params.add(offset >= 0 ? offset : 0);

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HistoryLogDTO dto = new HistoryLogDTO();
                    dto.id = rs.getInt("id");
                    dto.accountId = rs.getInt("account_id");
                    dto.playerId = rs.getInt("player_id");
                    dto.type = rs.getString("type");
                    dto.data = rs.getString("data");
                    dto.createdAt = rs.getString("create_at");
                    logs.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    // =========================================================================
    // 9. BOSS, SIÊU TRÙM & ĐỆ HOANG PANEL APIS
    // =========================================================================

    public CompletableFuture<List<boss.BossAndWildManager.TrackedEntityDTO>> getAllBossAndWildEntitiesAsync() {
        return CompletableFuture.supplyAsync(() -> boss.BossAndWildManager.gI().getAllTrackedEntities());
    }

    public CompletableFuture<List<model.DeTu>> spawnWildDeTuAsync(Integer targetMapId, Integer targetZoneId, Integer clazz,
                                                                 int count, boolean noDuplicateMap, boolean noDuplicateZone,
                                                                 boolean notifyKtg) {
        return CompletableFuture.supplyAsync(() ->
            boss.BossAndWildManager.gI().spawnWildDeTu(targetMapId, targetZoneId, clazz, count, noDuplicateMap, noDuplicateZone, notifyKtg)
        );
    }

    public CompletableFuture<List<model.DeTu>> spawnWildDeTuAsync(Integer targetMapId, Integer targetZoneId, Integer clazz,
                                                                 int count, boolean allZones, boolean noDuplicateMap, boolean noDuplicateZone,
                                                                 boolean notifyKtg) {
        return CompletableFuture.supplyAsync(() ->
            boss.BossAndWildManager.gI().spawnWildDeTu(targetMapId, targetZoneId, clazz, count, allZones, noDuplicateMap, noDuplicateZone, notifyKtg)
        );
    }

    public CompletableFuture<List<zabstracts.AbsBoss>> spawnSuperBossAsync(Integer mobId, Integer targetMapId, Integer targetZoneId,
                                                                          int count, boolean noDuplicateMap, boolean noDuplicateZone,
                                                                          boolean notifyKtg) {
        return CompletableFuture.supplyAsync(() ->
            boss.BossAndWildManager.gI().spawnSuperBoss(mobId, targetMapId, targetZoneId, count, noDuplicateMap, noDuplicateZone, notifyKtg)
        );
    }

    public CompletableFuture<List<zabstracts.AbsBoss>> spawnNormalBossAsync(Integer mobId, Integer targetMapId, Integer targetZoneId,
                                                                           int count, boolean noDuplicateMap, boolean noDuplicateZone,
                                                                           boolean notifyKtg) {
        return CompletableFuture.supplyAsync(() ->
            boss.BossAndWildManager.gI().spawnNormalBoss(mobId, targetMapId, targetZoneId, count, noDuplicateMap, noDuplicateZone, notifyKtg)
        );
    }

    public CompletableFuture<Integer> clearAllWildDeTuAsync() {
        return CompletableFuture.supplyAsync(() -> boss.BossAndWildManager.gI().clearAllWildDeTu());
    }

    public CompletableFuture<Integer> killAllBossesAsync() {
        return CompletableFuture.supplyAsync(() -> boss.BossAndWildManager.gI().killAllBosses());
    }

    public CompletableFuture<Boolean> killEntityAsync(Object rawEntity) {
        return CompletableFuture.supplyAsync(() -> boss.BossAndWildManager.gI().killEntity(rawEntity));
    }

    public CompletableFuture<Boolean> teleportAdminToEntityAsync(Player admin, Object rawEntity) {
        return CompletableFuture.supplyAsync(() -> boss.BossAndWildManager.gI().teleportAdminToEntity(admin, rawEntity));
    }

    public String setServerStatusTest() {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return "[ERROR] Không thể kết nối Database!";
            try (PreparedStatement ps = conn.prepareStatement("UPDATE `settings` SET `value` = 'TEST' WHERE `key_name` = 'server_status'")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("UPDATE `settings` SET `value` = '0' WHERE `key_name` = 'server_open_status'")) {
                ps.executeUpdate();
            }
            try (PreparedStatement psPh = conn.prepareStatement("SELECT `value` FROM `settings` WHERE `key_name` = 'server_phase_config'")) {
                ResultSet rsPh = psPh.executeQuery();
                if (rsPh.next()) {
                    String phVal = rsPh.getString("value");
                    Object phObj = org.json.simple.JSONValue.parse(phVal);
                    if (phObj instanceof org.json.simple.JSONObject) {
                        org.json.simple.JSONObject phJson = (org.json.simple.JSONObject) phObj;
                        phJson.put("status", "TEST");
                        phJson.put("server_status", "TEST");
                        phJson.put("server_open_status", 0);
                        try (PreparedStatement psUp = conn.prepareStatement("UPDATE `settings` SET `value` = ? WHERE `key_name` = 'server_phase_config'")) {
                            psUp.setString(1, phJson.toJSONString());
                            psUp.executeUpdate();
                        }
                    }
                }
            } catch (Exception ignored) {}
            Manager.gI().server_status = "TEST";
            Manager.gI().server_open_status = 0;
            TimedDungeonManager.gI().applyStandardSchedules();
            return "[SUCCESS] Đã chuyển máy chủ sang chế độ TEST!";
        } catch (Exception ex) {
            return "[ERROR] Lỗi khi đổi trạng thái TEST: " + ex.getMessage();
        }
    }

    public String executeResetBetaToOpen() {
        return executeReset("OPEN");
    }

    public String executeReset(String targetStatus) {
        if (targetStatus == null || targetStatus.trim().isEmpty()) {
            targetStatus = "OPEN";
        }
        targetStatus = targetStatus.trim().toUpperCase();
        if (!"BETA".equals(targetStatus) && !"OPEN".equals(targetStatus)) {
            targetStatus = "OPEN";
        }
        Log.warn("ServerReset", "Bắt đầu tiến trình RESET máy chủ sang " + targetStatus + " (Bảo toàn 100% Admin)...");
        try {
            // 1. Kick all players online
            kickAllPlayers("Reset máy chủ sang " + targetStatus + " (Bảo toàn 100% Admin)!");
            try { Thread.sleep(1500L); } catch (Exception ignored) {}

            int deletedMarket = 0;
            int deletedHistory = 0;
            int deletedDetu = 0;
            int deletedPlayers = 0;
            int deletedBots = 0;
            int resetAccounts = 0;

            try (Connection conn = DbManager.gI().getConnect()) {
                if (conn == null) return "[ERROR] Không thể kết nối Database!";

                // Subqueries identifying admin
                String adminAccSubquery = "SELECT `id` FROM `account` WHERE `role` = 1 OR `id` = 1 OR `is_admin` = 1 OR BINARY `username` = 'admin'";
                String adminPlayerSubquery = "SELECT `id` FROM `players` WHERE `account_id` IN (" + adminAccSubquery + ")";

                try (Statement st = conn.createStatement()) {
                    // 2. Reset Market (ALL)
                    try {
                        deletedMarket = st.executeUpdate("DELETE FROM `market`");
                    } catch (Exception ignored) {}

                    // 3. Reset Historys (All except admin)
                    try {
                        deletedHistory = st.executeUpdate("DELETE FROM `historys` WHERE (`player_id` IS NOT NULL AND `player_id` > 0 AND `player_id` NOT IN (" + adminPlayerSubquery + ")) OR (`account_id` IS NOT NULL AND `account_id` > 0 AND `account_id` NOT IN (" + adminAccSubquery + "))");
                    } catch (Exception ignored) {}

                    // 4. Delete Detu of non-admin players
                    try {
                        deletedDetu += st.executeUpdate("DELETE FROM `players_detu` WHERE `owner_id` NOT IN (" + adminPlayerSubquery + ")");
                    } catch (Exception ignored) {}
                    try {
                        deletedDetu += st.executeUpdate("DELETE FROM `players_detu` WHERE `player_id` NOT IN (" + adminPlayerSubquery + ")");
                    } catch (Exception ignored) {}
                    try {
                        deletedDetu += st.executeUpdate("DELETE FROM `detu` WHERE `player_id` NOT IN (" + adminPlayerSubquery + ")");
                    } catch (Exception ignored) {}

                    // 5. Delete Bots (ALL)
                    try {
                        deletedBots = st.executeUpdate("DELETE FROM `players_bot`");
                    } catch (Exception ignored) {}

                    // 6. Delete Non-Admin Players
                    try {
                        deletedPlayers = st.executeUpdate("DELETE FROM `players` WHERE `account_id` NOT IN (" + adminAccSubquery + ")");
                    } catch (Exception ignored) {}

                    // 7. Reset character list in account table for non-admin to '[]'
                    try {
                        resetAccounts = st.executeUpdate("UPDATE `account` SET `char` = '[]' WHERE `id` NOT IN (" + adminAccSubquery + ")");
                    } catch (Exception ignored) {}

                    // 8. Update server_status in settings
                    st.executeUpdate("UPDATE `settings` SET `value` = '" + targetStatus + "' WHERE `key_name` = 'server_status'");

                    // 10. Update server_open_status in settings (1 for OPEN, 0 for BETA)
                    String openStatusVal = "OPEN".equals(targetStatus) ? "1" : "0";
                    st.executeUpdate("UPDATE `settings` SET `value` = '" + openStatusVal + "' WHERE `key_name` = 'server_open_status'");

                    // 11. Update server_phase_config
                    try (PreparedStatement psPh = conn.prepareStatement("SELECT `value` FROM `settings` WHERE `key_name` = 'server_phase_config'")) {
                        ResultSet rsPh = psPh.executeQuery();
                        if (rsPh.next()) {
                            String phVal = rsPh.getString("value");
                            Object phObj = org.json.simple.JSONValue.parse(phVal);
                            if (phObj instanceof org.json.simple.JSONObject) {
                                org.json.simple.JSONObject phJson = (org.json.simple.JSONObject) phObj;
                                phJson.put("status", targetStatus);
                                phJson.put("server_status", targetStatus);
                                phJson.put("server_open_status", Integer.parseInt(openStatusVal));
                                try (PreparedStatement psUp = conn.prepareStatement("UPDATE `settings` SET `value` = ? WHERE `key_name` = 'server_phase_config'")) {
                                    psUp.setString(1, phJson.toJSONString());
                                    psUp.executeUpdate();
                                }
                            }
                        }
                    } catch (Exception ignored) {}
                }

                // 12. Record operation in `historys` table
                String logContent = "{\"action\":\"RESET_SERVER_TO_" + targetStatus + "\",\"target_status\":\"" + targetStatus + "\",\"deleted_market\":" + deletedMarket + ",\"deleted_history\":" + deletedHistory + ",\"deleted_players\":" + deletedPlayers + ",\"deleted_bots\":" + deletedBots + ",\"deleted_detu\":" + deletedDetu + ",\"reset_accounts\":" + resetAccounts + ",\"timestamp\":" + System.currentTimeMillis() + ",\"date\":\"" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + "\"}";
                historys.HistoryManager.saveSystemLog("RESET_SERVER_TO_" + targetStatus, logContent);

                Manager.gI().server_status = targetStatus;
                Manager.gI().server_open_status = "OPEN".equals(targetStatus) ? 1 : 0;
                TimedDungeonManager.gI().applyStandardSchedules();

                Log.success("ServerReset", "RESET to " + targetStatus + " completed: " + deletedPlayers + " players, " + deletedBots + " bots, " + deletedDetu + " detu, " + deletedMarket + " market, " + deletedHistory + " historys. Admin accounts & characters preserved 100%.");

                return "[SUCCESS] Đã Reset máy chủ sang " + targetStatus + " thành công! Đã xóa " + deletedPlayers + " nhân vật thường, " + deletedDetu + " đệ tử, " + deletedBots + " bots, " + deletedMarket + " vật phẩm chợ, " + deletedHistory + " lịch sử thường, đặt lại " + resetAccounts + " tài khoản thường. Toàn bộ Admin (role 1, id 1, admin) được giữ nguyên 100%.";
            }
        } catch (Exception ex) {
            Log.error("ServerReset", "Error during reset: " + ex.getMessage(), ex);
            return "[ERROR] Lỗi trong quá trình Reset máy chủ: " + ex.getMessage();
        }
    }
}
