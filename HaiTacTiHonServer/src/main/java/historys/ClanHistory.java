package historys;

import database.DbManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class ClanHistory {

    public static void loadClanTimeGifts(Map<String, Long> hmTimeGift) {
        try (Connection connection = DbManager.gI().getConnect();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT p.name, u.data FROM historys u " +
                 "JOIN players p ON u.player_id = p.id " +
                 "WHERE u.type = 'CLAN_TIME_GIFT' " +
                 "AND u.id IN (SELECT MAX(id) FROM historys WHERE type = 'CLAN_TIME_GIFT' GROUP BY player_id)"
             )
        ) {
            while (rs.next()) {
                String name = rs.getString("name");
                String data = rs.getString("data");
                long time = parseTimeFromData(data);
                hmTimeGift.put(name, time);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static long parseTimeFromData(String data) {
        try {
            if (data != null && data.startsWith("time:")) {
                String val = data.substring(5).trim();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < val.length(); i++) {
                    char c = val.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    } else {
                        break;
                    }
                }
                return Long.parseLong(sb.toString());
            }
        } catch (Exception e) {
            // Ignore
        }
        return 0L;
    }
}
