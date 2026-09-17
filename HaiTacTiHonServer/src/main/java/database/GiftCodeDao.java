package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import template.GiftTemplate;

public class GiftCodeDao {
    public synchronized static void updateUsed(GiftTemplate temp, String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DbManager.gI().getConnect();
            temp.used = (temp.used == null ? "" : temp.used) + name + ",";
            temp.luotnhap++;
            ps = conn.prepareStatement("UPDATE `giftcode` SET `used` = ?, `luotnhap` = ? WHERE `giftname` = ? LIMIT 1;");
            ps.setString(1, temp.used);
            ps.setInt(2, temp.luotnhap);
            ps.setString(3, temp.giftname);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
