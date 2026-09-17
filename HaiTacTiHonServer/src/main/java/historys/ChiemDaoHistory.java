package historys;

import clan.Clan;
import database.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChiemDaoHistory {

    public static void updateDb(Clan clanTop1, Clan clanTop2, Clan clanTop3, Clan clanTop4, Clan clanTop5) {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return;
            conn.setAutoCommit(false);
            try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM `historys` WHERE `type` = 'CHIEMDAO'")) {
                psDel.executeUpdate();
            }
            try (PreparedStatement psIns = conn.prepareStatement("INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (0, 0, 'CHIEMDAO', ?)")) {
                Clan[] tops = new Clan[]{clanTop1, clanTop2, clanTop3, clanTop4, clanTop5};
                for (int i = 0; i < tops.length; i++) {
                    if (tops[i] != null) {
                        org.json.simple.JSONObject obj = new org.json.simple.JSONObject();
                        obj.put("id", i + 1);
                        obj.put("clan_id", tops[i].id);
                        psIns.setString(1, obj.toString());
                        psIns.addBatch();
                    }
                }
                psIns.executeBatch();
            }
            conn.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Clan[] loadDb() {
        Clan[] tops = new Clan[5];
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("SELECT `data` FROM `historys` WHERE `type` = 'CHIEMDAO'")) {
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String json = rs.getString("data");
                    if (json != null && !json.isEmpty()) {
                        org.json.simple.JSONObject obj = (org.json.simple.JSONObject) org.json.simple.JSONValue.parse(json);
                        if (obj != null) {
                            int rank = ((Number) obj.get("id")).intValue() - 1;
                            int clanId = ((Number) obj.get("clan_id")).intValue();
                            if (rank >= 0 && rank < 5) {
                                tops[rank] = Clan.get_clan_by_id(clanId);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tops;
    }
}
