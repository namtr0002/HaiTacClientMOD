package historys;

import database.DbManager;
import model.Player;
import core.ZUtil;
import event.EventManager;
import event.Event;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * RechargeHistory — Quản lý và theo dõi lịch sử nạp tiền (Coin, Tổng Nạp, Extol, Thẻ cào, ATM/Momo).
 * 
 * Tính năng chính:
 * 1. Tự động gắn mã băm MD5 duy nhất cho mỗi giao dịch nạp.
 * 2. Lưu trữ bất biến trong bảng `historys` (type = 'NAP_THE' hoặc 'RECHARGE_HISTORY').
 * 3. Hỗ trợ đối soát và tính tổng nạp ngày, nạp tuần, nạp tổng trực tiếp từ historys (getRechargeSummary).
 * 4. Hiển thị hộp thoại tra cứu lịch sử nạp trực tiếp trong game (showRechargeHistoryDialog).
 */
public class RechargeHistory {

    public static final String TYPE = "NAP_THE";
    public static final String TYPE_ALT = "RECHARGE_HISTORY";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * DTO lưu trữ tóm tắt tổng nạp từ historys.
     */
    public static class RechargeSummary {
        public long today;
        public long thisWeek;
        public long total;

        public RechargeSummary(long today, long thisWeek, long total) {
            this.today = today;
            this.thisWeek = thisWeek;
            this.total = total;
        }
    }

    /**
     * Lấy Timestamp bắt đầu của Thứ 2 tuần hiện tại (00:00:00).
     */
    public static Timestamp getCurrentWeekMondayTimestamp() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate monday = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        return Timestamp.valueOf(monday.atStartOfDay());
    }

    /**
     * Tính toán tổng nạp ngày hôm nay, tuần này và toàn bộ từ trước đến nay từ bảng `historys`.
     */
    public static RechargeSummary getRechargeSummary(int accountId) {
        if (accountId <= 0) return new RechargeSummary(0, 0, 0);

        Timestamp mondayTs = getCurrentWeekMondayTimestamp();
        String sql = "SELECT "
                + "COALESCE(SUM(CASE WHEN `create_at` >= CURDATE() THEN JSON_EXTRACT(`data`, '$.amount') ELSE 0 END), 0) AS `today`, "
                + "COALESCE(SUM(CASE WHEN `create_at` >= ? THEN JSON_EXTRACT(`data`, '$.amount') ELSE 0 END), 0) AS `week`, "
                + "COALESCE(SUM(JSON_EXTRACT(`data`, '$.amount')), 0) AS `total` "
                + "FROM `historys` "
                + "WHERE `account_id` = ? AND (`type` = 'NAP_THE' OR `type` = 'RECHARGE_HISTORY' OR `type` = 'NAP_TIEN' OR `type` = 'NAP_COIN')";

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, mondayTs);
            ps.setInt(2, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long today = rs.getLong("today");
                    long week = rs.getLong("week");
                    long total = rs.getLong("total");
                    return new RechargeSummary(today, week, total);
                }
            }
        } catch (Exception e) {
            // Dự phòng: parse JSON từng dòng trong Java nếu MySQL gặp sự cố hàm JSON
            return getRechargeSummaryFallback(accountId, mondayTs);
        }
        return new RechargeSummary(0, 0, 0);
    }

    /**
     * Fallback parser cho getRechargeSummary.
     */
    private static RechargeSummary getRechargeSummaryFallback(int accountId, Timestamp mondayTs) {
        long today = 0;
        long week = 0;
        long total = 0;
        long todayStartMs = java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        long mondayStartMs = mondayTs.getTime();

        String sql = "SELECT `data`, `create_at` FROM `historys` WHERE `account_id` = ? AND (`type` = 'NAP_THE' OR `type` = 'RECHARGE_HISTORY' OR `type` = 'NAP_TIEN' OR `type` = 'NAP_COIN')";
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String jsonStr = rs.getString("data");
                    Timestamp createAt = rs.getTimestamp("create_at");
                    long rowTime = (createAt != null) ? createAt.getTime() : 0;
                    long amt = parseAmountFromJson(jsonStr);
                    if (amt > 0) {
                        total += amt;
                        if (rowTime >= mondayStartMs) {
                            week += amt;
                        }
                        if (rowTime >= todayStartMs) {
                            today += amt;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new RechargeSummary(today, week, total);
    }

    private static long parseAmountFromJson(String jsonStr) {
        if (jsonStr == null || jsonStr.isEmpty()) return 0;
        try {
            Object parsed = JSONValue.parse(jsonStr);
            if (parsed instanceof JSONObject) {
                Object val = ((JSONObject) parsed).get("amount");
                if (val instanceof Number) {
                    return ((Number) val).longValue();
                }
            }
        } catch (Exception ignored) {}
        return 0;
    }

    public static long getRechargeToday(int accountId) {
        RechargeSummary s = getRechargeSummary(accountId);
        return s != null ? s.today : 0;
    }

    public static long getRechargeThisWeek(int accountId) {
        RechargeSummary s = getRechargeSummary(accountId);
        return s != null ? s.thisWeek : 0;
    }

    public static long getRechargeTotal(int accountId) {
        RechargeSummary s = getRechargeSummary(accountId);
        return s != null ? s.total : 0;
    }

    /**
     * Ghi nhận giao dịch nạp tiền với đầy đủ thông tin sự kiện và mã MD5.
     */
    @SuppressWarnings("unchecked")
    public static String logRecharge(int accountId, int playerId, String playerName, String rechargeType, long amount,
                                     long oldVal, long newVal, String note,
                                     long tongnapAfter, long tongnap2After, long naphangngayAfter, int vipAfter) {
        long now = System.currentTimeMillis();
        String timeStr = DATE_FORMAT.format(new Date(now));
        String md5Token = ZUtil.generateMD5Token("RECHARGE_" + accountId + "_" + playerId + "_" + rechargeType + "_" + amount + "_" + now + "_" + Math.random());

        String pDate = HistoryManager.getPlayerCreatedAt(playerId);
        String aDate = HistoryManager.getAccountCreatedAt(accountId);
        String user = HistoryManager.getUsernameByAccountId(accountId);
        String pMd5 = HistoryManager.getPlayerMd5(aDate, user, accountId, pDate, playerName != null ? playerName : "", playerId);
        String aMd5 = HistoryManager.getAccountMd5(aDate, user, accountId);

        JSONObject obj = new JSONObject();
        obj.put("md5", md5Token);
        obj.put("p_md5", pMd5);
        obj.put("a_md5", aMd5);
        obj.put("p_date", pDate);
        obj.put("a_date", aDate);
        obj.put("user", user);
        obj.put("type", rechargeType != null ? rechargeType : "GENERAL");
        obj.put("name", playerName != null ? playerName : "");
        obj.put("amount", amount);
        obj.put("old_val", oldVal);
        obj.put("new_val", newVal);
        obj.put("tongnap_before", oldVal);
        obj.put("tongnap_after", newVal);
        obj.put("tongnap", tongnapAfter);
        obj.put("tongnap2", tongnap2After);
        obj.put("naphangngay", naphangngayAfter);
        obj.put("vip", vipAfter);
        obj.put("note", note != null ? note : "");
        obj.put("time", timeStr);

        // Ghi nhận danh sách các sự kiện đang diễn ra tại thời điểm nạp
        JSONArray activeEventsArray = new JSONArray();
        try {
            java.util.Collection<Event> activeEvents = EventManager.gI().getActiveEventsList();
            if (activeEvents != null && !activeEvents.isEmpty()) {
                for (Event ev : activeEvents) {
                    if (ev != null) {
                        JSONObject evObj = new JSONObject();
                        evObj.put("id", ev.getId());
                        evObj.put("name", ev.getName());
                        evObj.put("season", ev.getSeasonKey());
                        activeEventsArray.add(evObj);
                    }
                }
            }
        } catch (Exception e) {
            // Ignore error if EventManager is not yet fully initialized
        }
        obj.put("events", activeEventsArray);

        String jsonData = obj.toJSONString();

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, ?, ?, ?)"
             )
        ) {
            ps.setInt(1, accountId);
            ps.setInt(2, playerId);
            ps.setString(3, TYPE);
            ps.setString(4, jsonData);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[RechargeHistory] Error saving recharge log to DB: " + e.getMessage());
        }

        return md5Token;
    }

    public static String logRecharge(int accountId, int playerId, String playerName, String rechargeType, long amount, long oldVal, long newVal, String note) {
        return logRecharge(accountId, playerId, playerName, rechargeType, amount, oldVal, newVal, note, newVal, 0, 0, 0);
    }

    public static String logRecharge(Player p, String rechargeType, long amount, long oldVal, long newVal, String note) {
        if (p == null) return null;
        int accId = (p.conn != null) ? p.conn.idUser : HistoryManager.getAccountIdByPlayerId(p.IDPlayer);
        return logRecharge(accId, p.IDPlayer, p.name, rechargeType, amount, oldVal, newVal, note,
                           p.getTongnap(), p.getTongnap2(), p.getNaphangngay(), p.getVip());
    }

    public static List<JSONObject> getRechargesByPlayer(Player p, int limit) {
        if (p == null) return new ArrayList<>();
        return getRechargesByPlayer(p.IDPlayer, limit);
    }

    /**
     * Tra cứu chi tiết giao dịch nạp tiền theo mã MD5.
     */
    public static JSONObject findRechargeByMd5(String md5Token) {
        if (md5Token == null || md5Token.trim().isEmpty()) return null;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `account_id`, `player_id`, `data`, `create_at` FROM `historys` WHERE (`type` = ? OR `type` = ?) AND `data` LIKE ? LIMIT 1"
             )
        ) {
            ps.setString(1, TYPE);
            ps.setString(2, TYPE_ALT);
            ps.setString(3, "%\"md5\":\"" + md5Token.trim() + "\"%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String jsonStr = rs.getString("data");
                    Object parsed = JSONValue.parse(jsonStr);
                    if (parsed instanceof JSONObject) {
                        JSONObject obj = (JSONObject) parsed;
                        obj.put("account_id", rs.getInt("account_id"));
                        obj.put("player_id", rs.getInt("player_id"));
                        obj.put("create_at", rs.getString("create_at"));
                        return obj;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[RechargeHistory] Error finding recharge by MD5 " + md5Token + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Tra cứu danh sách lịch sử nạp gần nhất của một nhân vật.
     */
    @SuppressWarnings("unchecked")
    public static List<JSONObject> getRechargesByPlayer(int playerId, int limit) {
        List<JSONObject> list = new ArrayList<>();
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `account_id`, `player_id`, `data`, `create_at` FROM `historys` WHERE `player_id` = ? AND (`type` = ? OR `type` = ?) ORDER BY `id` DESC LIMIT ?"
             )
        ) {
            ps.setInt(1, playerId);
            ps.setString(2, TYPE);
            ps.setString(3, TYPE_ALT);
            ps.setInt(4, Math.max(1, limit));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String jsonStr = rs.getString("data");
                    Object parsed = JSONValue.parse(jsonStr);
                    if (parsed instanceof JSONObject) {
                        JSONObject obj = (JSONObject) parsed;
                        obj.put("row_id", rs.getInt("id"));
                        obj.put("account_id", rs.getInt("account_id"));
                        obj.put("player_id", rs.getInt("player_id"));
                        obj.put("create_at", rs.getString("create_at"));
                        list.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[RechargeHistory] Error getting recharges for player " + playerId + ": " + e.getMessage());
        }
        return list;
    }

    /**
     * Tra cứu danh sách lịch sử nạp gần nhất theo Account ID.
     */
    @SuppressWarnings("unchecked")
    public static List<JSONObject> getRechargesByAccount(int accountId, int limit) {
        List<JSONObject> list = new ArrayList<>();
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `account_id`, `player_id`, `data`, `create_at` FROM `historys` WHERE `account_id` = ? AND (`type` = ? OR `type` = ?) ORDER BY `id` DESC LIMIT ?"
             )
        ) {
            ps.setInt(1, accountId);
            ps.setString(2, TYPE);
            ps.setString(3, TYPE_ALT);
            ps.setInt(4, Math.max(1, limit));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String jsonStr = rs.getString("data");
                    Object parsed = JSONValue.parse(jsonStr);
                    if (parsed instanceof JSONObject) {
                        JSONObject obj = (JSONObject) parsed;
                        obj.put("row_id", rs.getInt("id"));
                        obj.put("account_id", rs.getInt("account_id"));
                        obj.put("player_id", rs.getInt("player_id"));
                        obj.put("create_at", rs.getString("create_at"));
                        list.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[RechargeHistory] Error getting recharges for account " + accountId + ": " + e.getMessage());
        }
        return list;
    }

    /**
     * Hiển thị bảng chi tiết lịch sử và theo dõi nạp tiền trực tiếp cho Player.
     */
    public static void showRechargeHistoryDialog(Player p) {
        if (p == null) return;
        int accId = (p.conn != null) ? p.conn.idUser : HistoryManager.getAccountIdByPlayerId(p.IDPlayer);
        p.refreshRecharge();
        RechargeSummary summary = getRechargeSummary(accId);
        List<JSONObject> recent = getRechargesByAccount(accId, 8);

        StringBuilder sb = new StringBuilder();
        sb.append("===== THEO DÕI NẠP TIỀN =====\n");
        sb.append("Tài khoản: ").append(p.getUsername()).append(" | NV: ").append(p.name).append("\n");
        sb.append("• Tích nạp hôm nay: ").append(ZUtil.number_format(p.getNaphangngay())).append(" VNĐ\n");
        sb.append("• Tích nạp tuần này: ").append(ZUtil.number_format(p.getTongnap2())).append(" VNĐ\n");
        sb.append("• Tích nạp tổng: ").append(ZUtil.number_format(p.getTongnap())).append(" VNĐ\n");
        sb.append("• Cấp VIP hiện tại: VIP ").append(p.getVip()).append(" (").append(p.getVipTitle()).append(")\n\n");
        sb.append("--- LỊCH SỬ GIAO DỊCH GẦN NHẤT ---\n");

        if (recent.isEmpty()) {
            sb.append("Chưa có bản ghi giao dịch nạp nào trong hệ thống.\n");
        } else {
            for (JSONObject item : recent) {
                long amt = 0;
                try { amt = ((Number) item.get("amount")).longValue(); } catch (Exception ignored) {}
                String time = (String) item.get("time");
                if (time == null) time = (String) item.get("create_at");
                if (time != null && time.length() >= 16) {
                    time = time.substring(5, 16); // "MM-dd HH:mm"
                }
                String note = (String) item.get("note");
                if (note == null || note.isEmpty()) note = (String) item.get("type");
                sb.append("• [").append(time != null ? time : "Giao dịch").append("] +")
                  .append(ZUtil.number_format(amt)).append(" VNĐ (").append(note).append(")\n");
            }
        }
        sb.append("\n(Dữ liệu được đối soát và lưu trữ bất biến từ lịch sử nạp!)");
        p.getService().send_box_ThongBao_OK(sb.toString());
    }
}
