package historys;

import core.ZUtil;
import database.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Player;
import network.Session;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * HistoryManager — Quản lý toàn bộ dữ liệu lưu trữ trong bảng `historys`.
 * 
 * Tính năng chính:
 * 1. Chuẩn hóa MD5 Signature cho từng vòng đời của Tài khoản (account) và Nhân vật (player)
 *    dựa trên: Ngày tạo acc, Username, Account ID, Ngày tạo player, Tên nhân vật, Player ID.
 * 2. Tự động gắn mã băm MD5 và metadata ngày tạo vào dữ liệu khi lưu (`saveData`).
 * 3. Tự động kiểm tra MD5 signature / ngày tạo khi nạp dữ liệu (`loadData`). Nếu phát hiện dữ liệu
 *    cũ từ nhân vật/tài khoản đời trước (do admin wipe/reset acc hoặc tạo lại char mà quên truncate historys),
 *    hệ thống sẽ tự động dọn dẹp sạch bản ghi cũ đó khỏi DB và trả về trạng thái mới 100%, không bị bug!
 * 4. Hỗ trợ dọn dẹp data mồ côi (`cleanStaleHistory`) khi khởi động server và định kỳ trong `cleanOldLogs`.
 */
public class HistoryManager {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Tự động chuẩn hóa Key dạng VARCHAR (ví dụ: "MUA_1", "MUA_2", "MUA_1_SO_SU_KIEN", "SO_SU_KIEN_MUA_1")
     */
    public static String formatKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            return "GENERAL";
        }
        return key.trim();
    }

    /**
     * Tự động ghép prefix với mùa/sự kiện (ví dụ: prefix="EVENT_DATA", season="MUA_1" -> "EVENT_DATA_MUA_1")
     */
    public static String formatKey(String prefix, String season) {
        if (season == null || season.trim().isEmpty()) {
            return formatKey(prefix);
        }
        String cleanSeason = season.trim();
        if (prefix == null || prefix.trim().isEmpty()) {
            return cleanSeason;
        }
        String cleanPrefix = prefix.trim();
        if (cleanSeason.toUpperCase().startsWith(cleanPrefix.toUpperCase())) {
            return cleanSeason;
        }
        return cleanPrefix + "_" + cleanSeason;
    }

    // =========================================================================
    // MD5 SIGNATURE GENERATION (TÀI KHOẢN & NHÂN VẬT)
    // =========================================================================

    /**
     * Sinh mã MD5 định danh duy nhất cho một tài khoản (Account).
     * Dựa trên: Ngày tạo tài khoản, Username, Account ID.
     */
    public static String getAccountMd5(String accCreatedAt, String username, int accountId) {
        String cleanDate = (accCreatedAt != null) ? accCreatedAt.trim() : "";
        String cleanUser = (username != null) ? username.trim().toLowerCase() : "";
        String raw = "ACC:" + cleanDate + "|USER:" + cleanUser + "|AID:" + accountId;
        return ZUtil.generateMD5Token(raw);
    }

    /**
     * Sinh mã MD5 từ Session của tài khoản.
     */
    public static String getAccountMd5(Session session) {
        if (session == null) return "";
        String accDate = (session.accCreatedAt != null && !session.accCreatedAt.isEmpty()) 
                ? session.accCreatedAt : getAccountCreatedAt(session.idUser);
        return getAccountMd5(accDate, session.user, session.idUser);
    }

    /**
     * Sinh mã MD5 định danh duy nhất cho một nhân vật (Player).
     * Dựa trên: Ngày tạo tài khoản, Username, Account ID, Ngày tạo nhân vật, Tên nhân vật, Player ID.
     */
    public static String getPlayerMd5(String accCreatedAt, String username, int accountId,
                                      String playerCreatedAt, String playerName, int playerId) {
        String cleanAccDate = (accCreatedAt != null) ? accCreatedAt.trim() : "";
        String cleanUser = (username != null) ? username.trim().toLowerCase() : "";
        String cleanPDate = (playerCreatedAt != null) ? playerCreatedAt.trim() : "";
        String cleanName = (playerName != null) ? playerName.trim().toLowerCase() : "";

        String raw = "ACC:" + cleanAccDate 
                   + "|USER:" + cleanUser 
                   + "|AID:" + accountId 
                   + "|PDATE:" + cleanPDate 
                   + "|NAME:" + cleanName 
                   + "|PID:" + playerId;
        return ZUtil.generateMD5Token(raw);
    }

    /**
     * Sinh mã MD5 trực tiếp từ đối tượng Player.
     */
    public static String getPlayerMd5(Player p) {
        if (p == null) return "";
        String accCreatedAt = (p.conn != null && p.conn.accCreatedAt != null) ? p.conn.accCreatedAt : "";
        String username = (p.conn != null && p.conn.user != null) ? p.conn.user : "";
        int accId = (p.conn != null) ? p.conn.idUser : 0;
        String playerCreatedAt = (p.date != null) ? p.date.toString() : "";

        // Fallback nếu conn là null (ví dụ xử lý offline / background task)
        if (accId <= 0 || username.isEmpty() || accCreatedAt.isEmpty()) {
            int foundAccId = getAccountIdByPlayerId(p.IDPlayer);
            if (foundAccId > 0) accId = foundAccId;
            if (username.isEmpty()) username = getUsernameByPlayerId(p.IDPlayer);
            if (accCreatedAt.isEmpty()) accCreatedAt = getAccountCreatedAt(accId);
        }
        if (playerCreatedAt.isEmpty()) {
            playerCreatedAt = getPlayerCreatedAt(p.IDPlayer);
        }

        return getPlayerMd5(accCreatedAt, username, accId, playerCreatedAt, p.name, p.IDPlayer);
    }

    /**
     * Sinh mã MD5 cho Player theo playerId (tự động tra cứu nếu player offline).
     */
    public static String getPlayerMd5ByPlayerId(int playerId) {
        if (playerId <= 0) return "";
        Player onlineP = map.Zone.get_player_by_id_allmap(playerId);
        if (onlineP != null) {
            return getPlayerMd5(onlineP);
        }
        int accId = getAccountIdByPlayerId(playerId);
        String username = getUsernameByPlayerId(playerId);
        String accCreatedAt = getAccountCreatedAt(accId);
        String playerCreatedAt = getPlayerCreatedAt(playerId);
        String playerName = getPlayerNameById(playerId);
        return getPlayerMd5(accCreatedAt, username, accId, playerCreatedAt, playerName, playerId);
    }

    // =========================================================================
    // DATA ENVELOPE (WRAPPING & UNWRAPPING)
    // =========================================================================

    /**
     * Đóng gói dữ liệu JSON kèm metadata MD5 signature và ngày tạo nhân vật/tài khoản.
     */
    @SuppressWarnings("unchecked")
    public static String wrapPlayerData(Player p, String rawJson) {
        if (rawJson == null || rawJson.trim().isEmpty()) {
            return "{}";
        }
        String cleanJson = rawJson.trim();
        String md5 = getPlayerMd5(p);
        String pDate = (p != null && p.date != null) ? p.date.toString() : "";
        String aDate = (p != null && p.conn != null && p.conn.accCreatedAt != null) ? p.conn.accCreatedAt : "";
        String user = (p != null && p.conn != null && p.conn.user != null) ? p.conn.user : "";
        String pName = (p != null && p.name != null) ? p.name : "";

        try {
            Object parsed = JSONValue.parse(cleanJson);
            if (parsed instanceof JSONObject) {
                JSONObject obj = (JSONObject) parsed;
                obj.put("_md5", md5);
                obj.put("_pdate", pDate);
                obj.put("_adate", aDate);
                obj.put("_user", user);
                obj.put("_pname", pName);
                return obj.toJSONString();
            } else if (parsed instanceof JSONArray) {
                JSONObject wrapper = new JSONObject();
                wrapper.put("_md5", md5);
                wrapper.put("_pdate", pDate);
                wrapper.put("_adate", aDate);
                wrapper.put("_user", user);
                wrapper.put("_pname", pName);
                wrapper.put("_data", parsed);
                return wrapper.toJSONString();
            }
        } catch (Exception ignored) {}

        JSONObject fallback = new JSONObject();
        fallback.put("_md5", md5);
        fallback.put("_pdate", pDate);
        fallback.put("_adate", aDate);
        fallback.put("_user", user);
        fallback.put("_pname", pName);
        fallback.put("_raw", cleanJson);
        return fallback.toJSONString();
    }

    /**
     * Đóng gói dữ liệu cho Account kèm metadata MD5 signature.
     */
    @SuppressWarnings("unchecked")
    public static String wrapAccountData(Session s, String rawJson) {
        if (rawJson == null || rawJson.trim().isEmpty()) {
            return "{}";
        }
        String cleanJson = rawJson.trim();
        String md5 = getAccountMd5(s);
        String aDate = (s != null && s.accCreatedAt != null) ? s.accCreatedAt : "";
        String user = (s != null && s.user != null) ? s.user : "";

        try {
            Object parsed = JSONValue.parse(cleanJson);
            if (parsed instanceof JSONObject) {
                JSONObject obj = (JSONObject) parsed;
                obj.put("_md5", md5);
                obj.put("_adate", aDate);
                obj.put("_user", user);
                return obj.toJSONString();
            } else if (parsed instanceof JSONArray) {
                JSONObject wrapper = new JSONObject();
                wrapper.put("_md5", md5);
                wrapper.put("_adate", aDate);
                wrapper.put("_user", user);
                wrapper.put("_data", parsed);
                return wrapper.toJSONString();
            }
        } catch (Exception ignored) {}

        JSONObject fallback = new JSONObject();
        fallback.put("_md5", md5);
        fallback.put("_adate", aDate);
        fallback.put("_user", user);
        fallback.put("_raw", cleanJson);
        return fallback.toJSONString();
    }

    /**
     * Mở gói (unwrap) dữ liệu để lấy lại payload gốc (JSONArray / JSONObject / String).
     */
    public static String unwrapData(String storedJson) {
        if (storedJson == null || storedJson.trim().isEmpty()) {
            return null;
        }
        String clean = storedJson.trim();
        try {
            Object parsed = JSONValue.parse(clean);
            if (parsed instanceof JSONObject) {
                JSONObject obj = (JSONObject) parsed;
                if (obj.containsKey("_data")) {
                    Object dataObj = obj.get("_data");
                    if (dataObj instanceof JSONArray) {
                        return ((JSONArray) dataObj).toJSONString();
                    } else if (dataObj instanceof JSONObject) {
                        return ((JSONObject) dataObj).toJSONString();
                    } else if (dataObj != null) {
                        return dataObj.toString();
                    }
                } else if (obj.containsKey("_raw")) {
                    return obj.get("_raw").toString();
                }
                return obj.toJSONString();
            } else if (parsed instanceof JSONArray) {
                return ((JSONArray) parsed).toJSONString();
            }
        } catch (Exception ignored) {}
        return clean;
    }

    // =========================================================================
    // VALIDATION & STALE RECORD DETECTION
    // =========================================================================

    /**
     * Kiểm tra bản ghi historys có hợp lệ với vòng đời hiện tại của Player hay không.
     * Trả về true nếu hợp lệ, false nếu là dữ liệu cũ của nhân vật đời trước (cần xóa).
     */
    public static boolean validatePlayerRecord(Player p, String storedJson, Timestamp createAt) {
        if (p == null) return true;
        if (storedJson == null || storedJson.trim().isEmpty()) return true;

        try {
            Object parsed = JSONValue.parse(storedJson.trim());
            if (parsed instanceof JSONObject) {
                JSONObject obj = (JSONObject) parsed;
                if (obj.containsKey("_md5")) {
                    String storedMd5 = obj.get("_md5").toString();
                    String expectedMd5 = getPlayerMd5(p);
                    if (!expectedMd5.isEmpty() && expectedMd5.equalsIgnoreCase(storedMd5)) {
                        return true;
                    }
                    // Check if MD5 matches with stored old _pname or if _pdate matches current player
                    if (obj.containsKey("_pname")) {
                        String storedPName = obj.get("_pname").toString();
                        String accCreatedAt = (p.conn != null && p.conn.accCreatedAt != null) ? p.conn.accCreatedAt : "";
                        String username = (p.conn != null && p.conn.user != null) ? p.conn.user : "";
                        int accId = (p.conn != null) ? p.conn.idUser : 0;
                        String playerCreatedAt = (p.date != null) ? p.date.toString() : "";
                        String oldMd5 = getPlayerMd5(accCreatedAt, username, accId, playerCreatedAt, storedPName, p.IDPlayer);
                        if (!oldMd5.isEmpty() && oldMd5.equalsIgnoreCase(storedMd5)) {
                            return true;
                        }
                    }
                    if (obj.containsKey("_pdate")) {
                        String storedPDate = obj.get("_pdate").toString();
                        String curPDate = (p.date != null) ? p.date.toString() : "";
                        if (!curPDate.isEmpty() && curPDate.equalsIgnoreCase(storedPDate)) {
                            return true;
                        }
                    }
                    return false;
                }

                if (obj.containsKey("_pdate")) {
                    String storedPDate = obj.get("_pdate").toString();
                    String curPDate = (p.date != null) ? p.date.toString() : "";
                    if (!curPDate.isEmpty() && !curPDate.equalsIgnoreCase(storedPDate)) {
                        return false;
                    }
                }
            }

            if (createAt != null && p.date != null) {
                long playerCreateMillis = p.date.getMillis();
                long recordCreateMillis = createAt.getTime();
                if (recordCreateMillis < (playerCreateMillis - 5000L)) {
                    return false;
                }
            }
        } catch (Exception ignored) {}
        return true;
    }

    /**
     * Kiểm tra bản ghi historys có hợp lệ với Account hiện tại hay không.
     */
    public static boolean validateAccountRecord(Session s, String storedJson, Timestamp createAt) {
        if (s == null) return true;
        if (storedJson == null || storedJson.trim().isEmpty()) return true;

        try {
            Object parsed = JSONValue.parse(storedJson.trim());
            if (parsed instanceof JSONObject) {
                JSONObject obj = (JSONObject) parsed;
                if (obj.containsKey("_md5")) {
                    String storedMd5 = obj.get("_md5").toString();
                    String expectedMd5 = getAccountMd5(s);
                    if (!expectedMd5.isEmpty() && !expectedMd5.equalsIgnoreCase(storedMd5)) {
                        return false;
                    }
                    return true;
                }
            }

            if (createAt != null && s.accCreatedAt != null && !s.accCreatedAt.isEmpty()) {
                try {
                    java.util.Date accDate = DATE_FMT.parse(s.accCreatedAt);
                    if (createAt.getTime() < (accDate.getTime() - 5000L)) {
                        return false;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return true;
    }

    // =========================================================================
    // LOAD DATA (TỰ ĐỘNG CHECK MD5 & CLEAN DATA CŨ)
    // =========================================================================

    /**
     * Tải dữ liệu từ bảng historys cho Player.
     * Tự động kiểm tra MD5 signature / ngày tạo. Nếu phát hiện dữ liệu cũ từ đời trước,
     * tự động xóa bản ghi đó và trả về null để nhân vật mới nhận dữ liệu sạch 100%.
     */
    public static String loadData(Player p, String key) {
        if (p == null) return null;
        String typeKey = formatKey(key);
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return null;
            try (PreparedStatement ps = conn.prepareStatement(
                     "SELECT `id`, `data`, `create_at` FROM `historys` WHERE `player_id` = ? AND `type` = ? LIMIT 1"
                 )) {
                ps.setInt(1, p.IDPlayer);
                ps.setString(2, typeKey);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int rowId = rs.getInt("id");
                        String data = rs.getString("data");
                        Timestamp createAt = rs.getTimestamp("create_at");

                        if (!validatePlayerRecord(p, data, createAt)) {
                            deleteRowById(conn, rowId);
                            System.out.println("[HistoryManager] Auto-cleaned stale record id=" + rowId 
                                    + " (key=" + typeKey + ") for player " + p.name + " (MD5/date mismatch).");
                            return null;
                        }
                        return unwrapData(data);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[HistoryManager] Error loading data for player " + p.name + ", key [" + typeKey + "]: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tải dữ liệu theo playerId và key (hỗ trợ tự động tra cứu Player để kiểm tra MD5).
     */
    public static String loadData(int playerId, String key) {
        if (playerId <= 0) return null;
        Player onlineP = map.Zone.get_player_by_id_allmap(playerId);
        if (onlineP != null) {
            return loadData(onlineP, key);
        }

        String typeKey = formatKey(key);
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return null;
            try (PreparedStatement ps = conn.prepareStatement(
                     "SELECT `id`, `data`, `create_at` FROM `historys` WHERE `player_id` = ? AND `type` = ? LIMIT 1"
                 )) {
                ps.setInt(1, playerId);
                ps.setString(2, typeKey);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int rowId = rs.getInt("id");
                        String data = rs.getString("data");
                        Timestamp createAt = rs.getTimestamp("create_at");

                        String playerDateStr = getPlayerCreatedAt(playerId);
                        if (playerDateStr != null && !playerDateStr.isEmpty() && createAt != null) {
                            try {
                                DateTime pdt = DateTime.parse(playerDateStr);
                                if (createAt.getTime() < (pdt.getMillis() - 5000L)) {
                                    deleteRowById(conn, rowId);
                                    System.out.println("[HistoryManager] Auto-cleaned stale record id=" + rowId 
                                            + " (key=" + typeKey + ") for offline player ID " + playerId);
                                    return null;
                                }
                            } catch (Exception ignored) {}
                        }
                        return unwrapData(data);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[HistoryManager] Error loading data for player ID " + playerId + ", key [" + typeKey + "]: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tải dữ liệu theo Session của tài khoản (Account).
     */
    public static String loadDataByAccount(Session session, String key) {
        if (session == null) return null;
        String typeKey = formatKey(key);
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return null;
            try (PreparedStatement ps = conn.prepareStatement(
                     "SELECT `id`, `data`, `create_at` FROM `historys` WHERE `account_id` = ? AND `type` = ? LIMIT 1"
                 )) {
                ps.setInt(1, session.idUser);
                ps.setString(2, typeKey);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int rowId = rs.getInt("id");
                        String data = rs.getString("data");
                        Timestamp createAt = rs.getTimestamp("create_at");

                        if (!validateAccountRecord(session, data, createAt)) {
                            deleteRowById(conn, rowId);
                            System.out.println("[HistoryManager] Auto-cleaned stale account record id=" + rowId 
                                    + " (key=" + typeKey + ") for account " + session.user);
                            return null;
                        }
                        return unwrapData(data);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[HistoryManager] Error loading data for account " + session.user + ", key [" + typeKey + "]: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tải dữ liệu theo accountId và key.
     */
    public static String loadDataByAccount(int accountId, String key) {
        if (accountId <= 0) return null;
        String typeKey = formatKey(key);
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return null;
            try (PreparedStatement ps = conn.prepareStatement(
                     "SELECT `data` FROM `historys` WHERE `account_id` = ? AND `type` = ? LIMIT 1"
                 )) {
                ps.setInt(1, accountId);
                ps.setString(2, typeKey);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return unwrapData(rs.getString("data"));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[HistoryManager] Error loading data for account ID " + accountId + ", key [" + typeKey + "]: " + e.getMessage());
        }
        return null;
    }

    // =========================================================================
    // SAVE DATA (TỰ ĐỘNG GẮN MD5 SIGNATURE & TỐI ƯU KẾT NỐI)
    // =========================================================================

    /**
     * Lưu dữ liệu chuẩn cho Player với Connection tái sử dụng.
     */
    public static void saveData(Connection conn, Player p, String key, String jsonData) {
        if (p == null) return;
        int accId = (p.conn != null) ? p.conn.idUser : getAccountIdByPlayerId(p.IDPlayer);
        String wrappedData = wrapPlayerData(p, jsonData);
        saveDataInternal(conn, accId, p.IDPlayer, key, wrappedData);
    }

    /**
     * Lưu dữ liệu cho Player.
     */
    public static void saveData(Player p, String key, String jsonData) {
        if (p == null) return;
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return;
            saveData(conn, p, key, jsonData);
        } catch (SQLException e) {
            System.err.println("[HistoryManager] Error obtaining connection to save player " + p.name + ", key [" + key + "]: " + e.getMessage());
        }
    }

    /**
     * Lưu dữ liệu cho Player theo accountId và playerId.
     */
    public static void saveData(Connection conn, int accountId, int playerId, String key, String jsonData) {
        Player onlineP = map.Zone.get_player_by_id_allmap(playerId);
        if (onlineP != null) {
            saveData(conn, onlineP, key, jsonData);
            return;
        }
        String wrappedData = jsonData;
        if (playerId > 0) {
            String pDate = getPlayerCreatedAt(playerId);
            String aDate = getAccountCreatedAt(accountId);
            String user = getUsernameByPlayerId(playerId);
            String pName = getPlayerNameById(playerId);
            String md5 = getPlayerMd5(aDate, user, accountId, pDate, pName, playerId);
            wrappedData = wrapDataWithCustomMd5(jsonData, md5, pDate, aDate, user, pName);
        }
        saveDataInternal(conn, accountId, playerId, key, wrappedData);
    }

    /**
     * Lưu dữ liệu theo accountId và playerId.
     */
    public static void saveData(int accountId, int playerId, String key, String jsonData) {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return;
            saveData(conn, accountId, playerId, key, jsonData);
        } catch (SQLException e) {
            System.err.println("[HistoryManager] Error saving data for playerId " + playerId + ", key [" + key + "]: " + e.getMessage());
        }
    }

    public static void saveData(int playerId, String key, String jsonData) {
        saveData(0, playerId, key, jsonData);
    }

    /**
     * Lưu dữ liệu theo Session của tài khoản.
     */
    public static void saveDataByAccount(Connection conn, Session session, String key, String jsonData) {
        if (session == null) return;
        String wrappedData = wrapAccountData(session, jsonData);
        saveAccountDataInternal(conn, session.idUser, key, wrappedData);
    }

    public static void saveDataByAccount(Session session, String key, String jsonData) {
        if (session == null) return;
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return;
            saveDataByAccount(conn, session, key, jsonData);
        } catch (SQLException e) {
            System.err.println("[HistoryManager] Error saving data for account " + session.user + ", key [" + key + "]: " + e.getMessage());
        }
    }

    public static void saveDataByAccount(int accountId, String key, String jsonData) {
        String aDate = getAccountCreatedAt(accountId);
        String user = getUsernameByAccountId(accountId);
        String md5 = getAccountMd5(aDate, user, accountId);
        String wrappedData = wrapDataWithCustomMd5(jsonData, md5, "", aDate, user, "");

        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return;
            saveAccountDataInternal(conn, accountId, key, wrappedData);
        } catch (SQLException e) {
            System.err.println("[HistoryManager] Error saving data for account ID " + accountId + ", key [" + key + "]: " + e.getMessage());
        }
    }

    private static void saveDataInternal(Connection conn, int accountId, int playerId, String key, String finalData) {
        if (conn == null) {
            try (Connection newConn = DbManager.gI().getConnect()) {
                if (newConn != null) saveDataInternal(newConn, accountId, playerId, key, finalData);
            } catch (SQLException ignored) {}
            return;
        }

        String typeKey = formatKey(key);
        boolean autoCommit = true;
        try {
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            // 1. Xóa bản ghi cũ cùng player_id và typeKey
            try (PreparedStatement psDel = conn.prepareStatement(
                "DELETE FROM `historys` WHERE `player_id` = ? AND `type` = ?"
            )) {
                psDel.setInt(1, playerId);
                psDel.setString(2, typeKey);
                psDel.executeUpdate();
            }

            // 2. Chèn dữ liệu mới nếu không rỗng
            if (finalData != null && !finalData.trim().isEmpty() && !finalData.trim().equals("[]") && !finalData.trim().equals("{}")) {
                try (PreparedStatement psIns = conn.prepareStatement(
                    "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, ?, ?, ?)"
                )) {
                    psIns.setInt(1, accountId);
                    psIns.setInt(2, playerId);
                    psIns.setString(3, typeKey);
                    psIns.setString(4, finalData);
                    psIns.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (Exception ignored) {}
            System.err.println("[HistoryManager] Error saving data for player " + playerId + ", key [" + typeKey + "]: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(autoCommit); } catch (Exception ignored) {}
        }
    }

    private static void saveAccountDataInternal(Connection conn, int accountId, String key, String finalData) {
        if (conn == null) return;
        String typeKey = formatKey(key);
        boolean autoCommit = true;
        try {
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            try (PreparedStatement psDel = conn.prepareStatement(
                "DELETE FROM `historys` WHERE `account_id` = ? AND `type` = ?"
            )) {
                psDel.setInt(1, accountId);
                psDel.setString(2, typeKey);
                psDel.executeUpdate();
            }

            if (finalData != null && !finalData.trim().isEmpty() && !finalData.trim().equals("[]") && !finalData.trim().equals("{}")) {
                try (PreparedStatement psIns = conn.prepareStatement(
                    "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, 0, ?, ?)"
                )) {
                    psIns.setInt(1, accountId);
                    psIns.setString(2, typeKey);
                    psIns.setString(3, finalData);
                    psIns.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (Exception ignored) {}
            System.err.println("[HistoryManager] Error saving data for account " + accountId + ", key [" + typeKey + "]: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(autoCommit); } catch (Exception ignored) {}
        }
    }

    @SuppressWarnings("unchecked")
    private static String wrapDataWithCustomMd5(String rawJson, String md5, String pDate, String aDate, String user, String pName) {
        if (rawJson == null || rawJson.trim().isEmpty()) return "{}";
        try {
            Object parsed = JSONValue.parse(rawJson.trim());
            if (parsed instanceof JSONObject) {
                JSONObject obj = (JSONObject) parsed;
                obj.put("_md5", md5);
                if (pDate != null && !pDate.isEmpty()) obj.put("_pdate", pDate);
                if (aDate != null && !aDate.isEmpty()) obj.put("_adate", aDate);
                if (user != null && !user.isEmpty()) obj.put("_user", user);
                if (pName != null && !pName.isEmpty()) obj.put("_pname", pName);
                return obj.toJSONString();
            } else if (parsed instanceof JSONArray) {
                JSONObject wrapper = new JSONObject();
                wrapper.put("_md5", md5);
                if (pDate != null && !pDate.isEmpty()) wrapper.put("_pdate", pDate);
                if (aDate != null && !aDate.isEmpty()) wrapper.put("_adate", aDate);
                if (user != null && !user.isEmpty()) wrapper.put("_user", user);
                if (pName != null && !pName.isEmpty()) wrapper.put("_pname", pName);
                wrapper.put("_data", parsed);
                return wrapper.toJSONString();
            }
        } catch (Exception ignored) {}
        return rawJson;
    }

    // =========================================================================
    // UTILITY & STALE CLEANUP METHODS
    // =========================================================================

    private static void deleteRowById(Connection conn, int rowId) {
        if (conn == null || rowId <= 0) return;
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM `historys` WHERE `id` = ?")) {
            ps.setInt(1, rowId);
            ps.executeUpdate();
        } catch (SQLException ignored) {}
    }

    /**
     * Dọn dẹp sạch sẽ toàn bộ các bản ghi mồ côi / data cũ của các nhân vật và tài khoản đã bị xóa/reset.
     */
    public static int cleanStaleHistory() {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return 0;
            return cleanStaleHistory(conn);
        } catch (SQLException e) {
            System.err.println("[HistoryManager] Error cleaning stale history: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Dọn dẹp data rác/cũ trên kết nối đang mở.
     */
    public static int cleanStaleHistory(Connection conn) {
        if (conn == null) return 0;
        int totalDeleted = 0;

        // 1. Xóa bản ghi player_id > 0 nhưng player không còn tồn tại trong bảng players
        try (PreparedStatement ps = conn.prepareStatement(
            "DELETE FROM `historys` WHERE `player_id` > 0 AND `player_id` NOT IN (SELECT `id` FROM `players`)"
        )) {
            int d = ps.executeUpdate();
            if (d > 0) {
                totalDeleted += d;
                System.out.println("[HistoryManager] Cleaned " + d + " orphaned player records.");
            }
        } catch (Exception e) {
            System.err.println("[HistoryManager] Error cleaning orphaned player records: " + e.getMessage());
        }

        // 2. Xóa bản ghi account_id > 0 nhưng account không còn tồn tại trong bảng account
        try (PreparedStatement ps = conn.prepareStatement(
            "DELETE FROM `historys` WHERE `account_id` > 0 AND `account_id` NOT IN (SELECT `id` FROM `account`)"
        )) {
            int d = ps.executeUpdate();
            if (d > 0) {
                totalDeleted += d;
                System.out.println("[HistoryManager] Cleaned " + d + " orphaned account records.");
            }
        } catch (Exception e) {
            System.err.println("[HistoryManager] Error cleaning orphaned account records: " + e.getMessage());
        }

        // 3. Xóa bản ghi được tạo trước ngày tạo của nhân vật hiện tại (trường hợp tạo lại nhân vật cùng ID/tên)
        // Lưu ý: Không bao giờ xóa lịch sử nạp tiền (NAP_THE / RECHARGE_HISTORY)
        try (PreparedStatement ps = conn.prepareStatement(
            "DELETE h FROM `historys` h "
            + "INNER JOIN `players` p ON h.`player_id` = p.`id` "
            + "WHERE h.`player_id` > 0 "
            + "AND h.`type` NOT IN ('NAP_THE', 'RECHARGE_HISTORY') "
            + "AND p.`date` IS NOT NULL "
            + "AND p.`date` != '' "
            + "AND h.`create_at` < STR_TO_DATE(SUBSTRING(p.`date`, 1, 19), '%Y-%m-%dT%H:%i:%s')"
        )) {
            int d = ps.executeUpdate();
            if (d > 0) {
                totalDeleted += d;
                System.out.println("[HistoryManager] Cleaned " + d + " stale records created before current player creation date.");
            }
        } catch (Exception e) {
            try (PreparedStatement psFallback = conn.prepareStatement(
                "DELETE h FROM `historys` h "
                + "INNER JOIN `players` p ON h.`player_id` = p.`id` "
                + "WHERE h.`player_id` > 0 "
                + "AND h.`type` NOT IN ('NAP_THE', 'RECHARGE_HISTORY') "
                + "AND p.`date` IS NOT NULL "
                + "AND p.`date` != '' "
                + "AND h.`create_at` < STR_TO_DATE(p.`date`, '%Y-%m-%d %H:%i:%s')"
            )) {
                int d = psFallback.executeUpdate();
                if (d > 0) {
                    totalDeleted += d;
                    System.out.println("[HistoryManager] Cleaned " + d + " stale records (fallback).");
                }
            } catch (Exception ignored) {}
        }

        // 4. Xóa bản ghi được tạo trước ngày tạo của tài khoản hiện tại (ngoại trừ nạp thẻ)
        try (PreparedStatement ps = conn.prepareStatement(
            "DELETE h FROM `historys` h "
            + "INNER JOIN `account` a ON h.`account_id` = a.`id` "
            + "WHERE h.`account_id` > 0 "
            + "AND h.`type` NOT IN ('NAP_THE', 'RECHARGE_HISTORY') "
            + "AND a.`create_at` IS NOT NULL "
            + "AND h.`create_at` < a.`create_at`"
        )) {
            int d = ps.executeUpdate();
            if (d > 0) {
                totalDeleted += d;
                System.out.println("[HistoryManager] Cleaned " + d + " stale account records.");
            }
        } catch (Exception ignored) {}

        return totalDeleted;
    }

    public static boolean hasData(int playerId, String key) {
        String typeKey = formatKey(key);
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return false;
            try (PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM `historys` WHERE `player_id` = ? AND `type` = ?"
                 )) {
                ps.setInt(1, playerId);
                ps.setString(2, typeKey);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("[HistoryManager] Error checking hasData for player " + playerId + ": " + e.getMessage());
        }
        return false;
    }

    public static void deleteData(int playerId, String key) {
        String typeKey = formatKey(key);
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM `historys` WHERE `player_id` = ? AND `type` = ?"
                 )) {
                ps.setInt(1, playerId);
                ps.setString(2, typeKey);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println("[HistoryManager] Error deleting data for player " + playerId + ": " + e.getMessage());
        }
    }

    public static Map<Integer, String> loadAllByKey(String key) {
        String typeKey = formatKey(key);
        Map<Integer, String> resultMap = new HashMap<>();
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return resultMap;
            try (PreparedStatement ps = conn.prepareStatement(
                     "SELECT `player_id`, `data` FROM `historys` WHERE `type` = ?"
                 )) {
                ps.setString(1, typeKey);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        resultMap.put(rs.getInt("player_id"), unwrapData(rs.getString("data")));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[HistoryManager] Error loading all data for key [" + typeKey + "]: " + e.getMessage());
        }
        return resultMap;
    }

    public static String findRecordByMd5(String type, String md5Token) {
        if (md5Token == null || md5Token.trim().isEmpty()) return null;
        String sql = (type != null && !type.trim().isEmpty())
            ? "SELECT `data` FROM `historys` WHERE `type` = ? AND `data` LIKE ? LIMIT 1"
            : "SELECT `data` FROM `historys` WHERE `data` LIKE ? LIMIT 1";

        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (type != null && !type.trim().isEmpty()) {
                    ps.setString(1, type.trim());
                    ps.setString(2, "%" + md5Token.trim() + "%");
                } else {
                    ps.setString(1, "%" + md5Token.trim() + "%");
                }
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return unwrapData(rs.getString("data"));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[HistoryManager] Error finding record by MD5 " + md5Token + ": " + e.getMessage());
        }
        return null;
    }

    public static List<String> loadHistoryList(int playerId, String type, int limit) {
        List<String> list = new ArrayList<>();
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return list;
            try (PreparedStatement ps = conn.prepareStatement(
                     "SELECT `data` FROM `historys` WHERE `player_id` = ? AND `type` = ? ORDER BY `id` DESC LIMIT ?"
                 )) {
                ps.setInt(1, playerId);
                ps.setString(2, formatKey(type));
                ps.setInt(3, Math.max(1, limit));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(unwrapData(rs.getString("data")));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[HistoryManager] Error loading history list for player " + playerId + ": " + e.getMessage());
        }
        return list;
    }

    // =========================================================================
    // DB METADATA LOOKUP HELPERS
    // =========================================================================

    public static int getAccountIdByPlayerId(int playerId) {
        if (playerId <= 0) return 0;
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return 0;
            try (PreparedStatement ps = conn.prepareStatement("SELECT `account_id` FROM `players` WHERE `id` = ? LIMIT 1")) {
                ps.setInt(1, playerId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("account_id");
                }
            }
        } catch (Exception ignored) {}
        return 0;
    }

    public static String getPlayerNameById(int playerId) {
        if (playerId <= 0) return "";
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return "";
            try (PreparedStatement ps = conn.prepareStatement("SELECT `name` FROM `players` WHERE `id` = ? LIMIT 1")) {
                ps.setInt(1, playerId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getString("name");
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    public static String getPlayerCreatedAt(int playerId) {
        if (playerId <= 0) return "";
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return "";
            try (PreparedStatement ps = conn.prepareStatement("SELECT `date` FROM `players` WHERE `id` = ? LIMIT 1")) {
                ps.setInt(1, playerId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String d = rs.getString("date");
                        return (d != null) ? d : "";
                    }
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    public static String getUsernameByPlayerId(int playerId) {
        int accId = getAccountIdByPlayerId(playerId);
        if (accId > 0) return getUsernameByAccountId(accId);
        return "";
    }

    public static String getUsernameByAccountId(int accountId) {
        if (accountId <= 0) return "";
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return "";
            try (PreparedStatement ps = conn.prepareStatement("SELECT `username` FROM `account` WHERE `id` = ? LIMIT 1")) {
                ps.setInt(1, accountId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getString("username");
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    public static String getAccountCreatedAt(int accountId) {
        if (accountId <= 0) return "";
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return "";
            try (PreparedStatement ps = conn.prepareStatement("SELECT `create_at`, `date` FROM `account` WHERE `id` = ? LIMIT 1")) {
                ps.setInt(1, accountId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String cAt = rs.getString("create_at");
                        if (cAt != null && !cAt.isEmpty()) return cAt;
                        String d = rs.getString("date");
                        return (d != null) ? d : "";
                    }
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    public static boolean hasSystemLog(String type) {
        if (type == null || type.trim().isEmpty()) return false;
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return false;
            try (PreparedStatement ps = conn.prepareStatement("SELECT `id` FROM `historys` WHERE `type` = ? LIMIT 1")) {
                ps.setString(1, type.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    public static boolean saveSystemLog(String type, String data) {
        if (type == null || type.trim().isEmpty()) return false;
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return false;
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (0, 0, ?, ?)")) {
                ps.setString(1, type.trim());
                ps.setString(2, (data != null) ? data : "{}");
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
