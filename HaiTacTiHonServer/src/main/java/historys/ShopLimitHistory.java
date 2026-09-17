package historys;

import database.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import model.Player;

public class ShopLimitHistory {

    public static java.util.Date getThresholdDate() {
        // Check active event
        event.Event activeEvent = event.EventManager.gI().getActiveEvent();
        if (activeEvent != null) {
            java.util.Date eventStart = event.Event.getEventStartDate(activeEvent.time);
            if (eventStart != null) {
                return eventStart;
            }
        }
        // Fallback to start of current week (Monday 00:00:00 GMT+7)
        java.util.Calendar cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+7"));
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        int daysToSubtract = (dayOfWeek == java.util.Calendar.SUNDAY) ? 6 : (dayOfWeek - java.util.Calendar.MONDAY);
        cal.add(java.util.Calendar.DAY_OF_MONTH, -daysToSubtract);
        return cal.getTime();
    }

    public static int getPurchasedCount(Player p, String tableName, byte cat, short id) {
        if (p == null) return 0;
        java.util.Date threshold = getThresholdDate();
        if (p.date != null && p.date.getMillis() > threshold.getTime()) {
            threshold = new java.util.Date(p.date.getMillis());
        }
        return getPurchasedCountInternal(p.IDPlayer, tableName, cat, id, threshold);
    }

    public static int getPurchasedCount(int playerId, String tableName, byte cat, short id) {
        java.util.Date threshold = getThresholdDate();
        String pDateStr = HistoryManager.getPlayerCreatedAt(playerId);
        if (pDateStr != null && !pDateStr.isEmpty()) {
            try {
                org.joda.time.DateTime pdt = org.joda.time.DateTime.parse(pDateStr);
                if (pdt.getMillis() > threshold.getTime()) {
                    threshold = new java.util.Date(pdt.getMillis());
                }
            } catch (Exception ignored) {}
        }
        return getPurchasedCountInternal(playerId, tableName, cat, id, threshold);
    }

    private static int getPurchasedCountInternal(int playerId, String tableName, byte cat, short id, java.util.Date threshold) {
        try (Connection connection = DbManager.gI().getConnect();
             PreparedStatement ps = connection.prepareStatement(
                 "SELECT COUNT(*) FROM `historys` WHERE `player_id` = ? AND `type` = 'SHOP_LIMIT_BUY' AND `data` = ? AND `create_at` >= ?;"
             )) {
            ps.setInt(1, playerId);
            ps.setString(2, tableName + "_" + cat + "_" + id);
            ps.setTimestamp(3, new Timestamp(threshold.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void logPurchase(Player p, String tableName, byte cat, short id) {
        if (p == null) return;
        int accId = (p.conn != null) ? p.conn.idUser : HistoryManager.getAccountIdByPlayerId(p.IDPlayer);
        logPurchase(accId, p.IDPlayer, tableName, cat, id);
    }

    public static void logPurchase(int accountId, int playerId, String tableName, byte cat, short id) {
        logPurchaseBatch(accountId, playerId, tableName, cat, id, 1);
    }

    public static void logPurchaseBatch(int accountId, int playerId, String tableName, byte cat, short id, int quantity) {
        if (quantity <= 0) return;
        try (Connection connection = DbManager.gI().getConnect()) {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, ?, 'SHOP_LIMIT_BUY', ?);"
                 )) {
                for (int i = 0; i < quantity; i++) {
                    ps.setInt(1, accountId);
                    ps.setInt(2, playerId);
                    ps.setString(3, tableName + "_" + cat + "_" + id);
                    ps.addBatch();
                    if (i % 500 == 0 || i == quantity - 1) {
                        ps.executeBatch();
                    }
                }
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
