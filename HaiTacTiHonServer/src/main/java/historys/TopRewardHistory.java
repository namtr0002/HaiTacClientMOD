package historys;

import model.Player;
import database.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class TopRewardHistory {

    public static boolean hasReceivedReward(Player p, String topType) {
        if (p == null) return false;
        String weekIdentifier = getWeekIdentifier(LocalDate.now());
        String match = "topType:" + topType + " | week:" + weekIdentifier;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `data`, `create_at` FROM `historys` WHERE `player_id` = ? AND `type` = 'TOP_REWARD' AND `data` LIKE ?"
             )) {
            ps.setInt(1, p.IDPlayer);
            ps.setString(2, "%" + match + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int rowId = rs.getInt("id");
                    String data = rs.getString("data");
                    Timestamp cAt = rs.getTimestamp("create_at");

                    if (!HistoryManager.validatePlayerRecord(p, data, cAt)) {
                        try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM `historys` WHERE `id` = ?")) {
                            psDel.setInt(1, rowId);
                            psDel.executeUpdate();
                        } catch (Exception ignored) {}
                        continue;
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasReceivedReward(String playerName, LocalDateTime date, String topType) {
        String weekIdentifier = getWeekIdentifier(date.toLocalDate());
        String match = "topType:" + topType + " | week:" + weekIdentifier;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT h.`id`, h.`data`, h.`create_at`, p.`date` FROM `historys` h "
                 + "JOIN `players` p ON h.`player_id` = p.`id` "
                 + "WHERE p.`name` = ? AND h.`type` = 'TOP_REWARD' AND h.`data` LIKE ?"
             )) {
            ps.setString(1, playerName);
            ps.setString(2, "%" + match + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int rowId = rs.getInt("id");
                    Timestamp cAt = rs.getTimestamp("create_at");
                    String pDateStr = rs.getString("date");
                    if (pDateStr != null && !pDateStr.isEmpty() && cAt != null) {
                        try {
                            org.joda.time.DateTime pdt = org.joda.time.DateTime.parse(pDateStr);
                            if (cAt.getTime() < (pdt.getMillis() - 5000L)) {
                                try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM `historys` WHERE `id` = ?")) {
                                    psDel.setInt(1, rowId);
                                    psDel.executeUpdate();
                                } catch (Exception ignored) {}
                                continue;
                            }
                        } catch (Exception ignored) {}
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasReceivedRewardManual(String playerName, LocalDateTime date, String topType) {
        String match = "topType:" + topType;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT h.`id`, h.`data`, h.`create_at`, p.`date` FROM `historys` h "
                 + "JOIN `players` p ON h.`player_id` = p.`id` "
                 + "WHERE p.`name` = ? AND h.`type` = 'TOP_REWARD' AND h.`data` LIKE ?"
             )) {
            ps.setString(1, playerName);
            ps.setString(2, "%" + match + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int rowId = rs.getInt("id");
                    Timestamp cAt = rs.getTimestamp("create_at");
                    String pDateStr = rs.getString("date");
                    if (pDateStr != null && !pDateStr.isEmpty() && cAt != null) {
                        try {
                            org.joda.time.DateTime pdt = org.joda.time.DateTime.parse(pDateStr);
                            if (cAt.getTime() < (pdt.getMillis() - 5000L)) {
                                try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM `historys` WHERE `id` = ?")) {
                                    psDel.setInt(1, rowId);
                                    psDel.executeUpdate();
                                } catch (Exception ignored) {}
                                continue;
                            }
                        } catch (Exception ignored) {}
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void logRewardClaim(Player p, String topType) {
        if (p == null) return;
        String weekIdentifier = getWeekIdentifier(LocalDate.now());
        String md5 = HistoryManager.getPlayerMd5(p);
        String logEntry = String.format("topType:%s | week:%s | md5:%s", topType, weekIdentifier, md5);
        zLog.gI().add_log(p, "TOP_REWARD", logEntry);
    }

    public static void logManualReward(Player p, String topType) {
        if (p == null) return;
        String md5 = HistoryManager.getPlayerMd5(p);
        String logEntry = String.format("topType:%s | md5:%s", topType, md5);
        zLog.gI().add_log(p, "TOP_REWARD", logEntry);
    }

    private static String getWeekIdentifier(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        return "Week-" + weekNumber + "-Year-" + date.getYear();
    }
}
