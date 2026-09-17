package historys;

import database.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class VongQuayOcSenHistory {

    public static void load(Map<String, byte[]> entryMap) {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) {
                System.err.println("[VongQuayOcSenHistory] Warning: Cannot get DB connection to load.");
                return;
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT `data` FROM `historys` WHERE `type` = 'VONGQUAYOCSEN'"); 
                 ResultSet rs = ps.executeQuery() 
            ) {
                while (rs.next()) {
                    JSONObject obj = (JSONObject) JSONValue.parse(rs.getString("data"));
                    String name = obj.get("name").toString();
                    JSONArray js = (JSONArray) obj.get("data");
                    byte[] data = new byte[js.size()];
                    for (int i = 0; i < data.length; i++) {
                        data[i] = Byte.parseByte(js.get(i).toString());
                    }
                    entryMap.put(name, data);
                }
            }
        } catch (Throwable e) {
            System.err.println("[VongQuayOcSenHistory] Warning: Failed to load from DB: " + e.getMessage());
        }
    }

    public static void updateDB(Map<String, byte[]> entryMap) {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) {
                System.err.println("[VongQuayOcSenHistory] Warning: Cannot get DB connection to update.");
                return;
            }
            try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM `historys` WHERE `type` = 'VONGQUAYOCSEN'");
                 PreparedStatement psIns = conn.prepareStatement("INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (0, 0, 'VONGQUAYOCSEN', ?)")
            ) {
                conn.setAutoCommit(false);
                psDel.executeUpdate();
                for (Map.Entry<String, byte[]> en : entryMap.entrySet()) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", en.getKey());
                    JSONArray js = new JSONArray();
                    for (byte b : en.getValue()) {
                        js.add(b);
                    }
                    obj.put("data", js);

                    psIns.setString(1, obj.toString());
                    psIns.addBatch();
                }
                psIns.executeBatch();
                conn.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
