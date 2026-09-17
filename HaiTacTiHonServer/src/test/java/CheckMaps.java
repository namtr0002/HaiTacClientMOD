import java.sql.Connection;
import database.DbManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class CheckMaps {
    public static void main(String[] args) {
        try {
            core.Manager.gI().load_config();
        } catch (Exception e) {
            System.err.println("Failed to load config: " + e.getMessage());
            return;
        }
        try (Connection conn = DbManager.gI().getConnect();
             Statement st = conn.createStatement()) {

            System.out.println("=== MOBS >= 150 ===");
            try (ResultSet rs = st.executeQuery("SELECT id, name, level, hp FROM mobs WHERE id >= 150;")) {
                while (rs.next()) {
                    System.out.printf("Mob ID: %d, Name: %s, Level: %d, HP: %d\n",
                            rs.getInt("id"), rs.getString("name"), rs.getInt("level"), rs.getInt("hp"));
                }
            }

            System.out.println("\n=== ALL MOBS SEARCH BY NAME ===");
            try (ResultSet rs = st.executeQuery("SELECT id, name, level, hp FROM mobs WHERE name LIKE '%Big%' OR name LIKE '%Mom%' OR name LIKE '%Charlotte%';")) {
                while (rs.next()) {
                    System.out.printf("Mob ID: %d, Name: %s, Level: %d, HP: %d\n",
                            rs.getInt("id"), rs.getString("name"), rs.getInt("level"), rs.getInt("hp"));
                }
            }

            System.out.println("\n=== ITEMS 870..885 in item4 ===");
            try (ResultSet rs = st.executeQuery("SELECT * FROM item4 WHERE id BETWEEN 870 AND 885;")) {
                while (rs.next()) {
                    System.out.printf("Item4 ID: %d, Name: %s\n",
                            rs.getInt("id"), rs.getString("name"));
                }
            }

            System.out.println("\n=== MAPS 1 to 55 ===");
            try (ResultSet rs = st.executeQuery("SELECT id, name FROM maps WHERE id <= 55 ORDER BY id;")) {
                while (rs.next()) {
                    System.out.printf("Map ID: %d, Name: %s\n", rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
