package historys;

import model.Player;
import database.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DiemDanhHistory {

    public static boolean hasReceivedReward(Player p, int dayIndex) {
        if (p == null) return false;
        String match = "day:" + (dayIndex + 1);
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `data`, `create_at` FROM `historys` WHERE `player_id` = ? AND `type` = 'DIEM_DANH' AND `data` LIKE ?"
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

    public static boolean hasReceivedReward(String player, int dayIndex) {
        String match = "day:" + (dayIndex + 1);
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT h.`id`, h.`data`, h.`create_at`, p.`date` FROM `historys` h "
                 + "JOIN `players` p ON h.`player_id` = p.`id` "
                 + "WHERE p.`name` = ? AND h.`type` = 'DIEM_DANH' AND h.`data` LIKE ?"
             )) {
            ps.setString(1, player);
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

    public static void logDetail(Player p, int day, short itemId, short itemNum) {
        if (p == null) return;
        String md5 = HistoryManager.getPlayerMd5(p);
        String logLine = String.format("day:%d | item:%d | num:%d | md5:%s", day, itemId, itemNum, md5);
        zLog.gI().add_log(p, "DIEM_DANH", logLine);
    }
}
