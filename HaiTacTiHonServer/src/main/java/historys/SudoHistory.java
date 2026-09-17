package historys;

import activities.Sudo;
import database.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class SudoHistory {
    
    public static void load(ResultSet rs) throws SQLException {
        Sudo sudoAdd = new Sudo();
        sudoAdd.id = rs.getInt("player_id");
        sudoAdd.accountId = rs.getInt("account_id");
        
        String dataStr = rs.getString("data");
        Object parsed = JSONValue.parse(dataStr);
        if (parsed instanceof JSONObject) {
            JSONObject js_in0 = (JSONObject) parsed;
            sudoAdd.name = js_in0.get("name").toString();
            sudoAdd.head = Short.parseShort(js_in0.get("head").toString());
            sudoAdd.hair = Short.parseShort(js_in0.get("hair").toString());
            sudoAdd.hat = Short.parseShort(js_in0.get("hat").toString());
            sudoAdd.chucInSudo = Byte.parseByte(js_in0.get("chucInSudo").toString());
            sudoAdd.lvExp = Short.parseShort(js_in0.get("lvExp").toString());
            sudoAdd.exp = Long.parseLong(js_in0.get("exp").toString());
            sudoAdd.point = Short.parseShort(js_in0.get("point").toString());
            sudoAdd.nameRelative = new ArrayList<>();
            JSONArray js_in2 = (JSONArray) js_in0.get("nameRelative");
            if (js_in2 != null) {
                for (int j = 0; j < js_in2.size(); j++) {
                    sudoAdd.nameRelative.add(js_in2.get(j).toString());
                }
            }
        } else if (parsed instanceof JSONArray) {
            JSONArray js_in0 = (JSONArray) parsed;
            sudoAdd.name = js_in0.get(1).toString();
            sudoAdd.head = Short.parseShort(js_in0.get(2).toString());
            sudoAdd.hair = Short.parseShort(js_in0.get(3).toString());
            sudoAdd.hat = Short.parseShort(js_in0.get(4).toString());
            sudoAdd.chucInSudo = Byte.parseByte(js_in0.get(5).toString());
            sudoAdd.lvExp = Short.parseShort(js_in0.get(6).toString());
            sudoAdd.exp = Long.parseLong(js_in0.get(7).toString());
            sudoAdd.point = Short.parseShort(js_in0.get(8).toString());
            sudoAdd.nameRelative = new ArrayList<>();
            JSONArray js_in2 = (JSONArray) js_in0.get(9);
            if (js_in2 != null) {
                for (int j = 0; j < js_in2.size(); j++) {
                    sudoAdd.nameRelative.add(js_in2.get(j).toString());
                }
            }
        }
        if (sudoAdd.id > 0) {
            String currentName = database.DbManager.getPlayerNameById(sudoAdd.id);
            if (currentName != null && !currentName.isEmpty()) {
                sudoAdd.name = currentName;
            }
        } else {
            sudoAdd.id = database.DbManager.getPlayerIdByName(sudoAdd.name);
        }
        Sudo.addSuDoByName(sudoAdd.name, sudoAdd);
    }

    public static void updateDb(java.util.Map<String, Sudo> entries) {
        Connection conn = null;
        PreparedStatement psDelete = null;
        PreparedStatement psInsert = null;
        try {
            conn = DbManager.gI().getConnect();
            if (conn == null) return;
            conn.setAutoCommit(false);
            
            psDelete = conn.prepareStatement("DELETE FROM `historys` WHERE `type` = 'SUDO'");
            psDelete.executeUpdate();
            
            psInsert = conn.prepareStatement("INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, ?, 'SUDO', ?)");
            int i = 0;
            for (java.util.Map.Entry<String, Sudo> en : entries.entrySet()) {
                Sudo temp = en.getValue();
                
                JSONObject js = new JSONObject();
                js.put("name", temp.name);
                js.put("head", temp.head);
                js.put("hair", temp.hair);
                js.put("hat", temp.hat);
                js.put("chucInSudo", temp.chucInSudo);
                js.put("lvExp", temp.lvExp);
                js.put("exp", temp.exp);
                js.put("point", temp.point);
                
                JSONArray js_in3 = new JSONArray();
                if (temp.nameRelative != null) {
                    for (int j = 0; j < temp.nameRelative.size(); j++) {
                        js_in3.add(temp.nameRelative.get(j));
                    }
                }
                js.put("nameRelative", js_in3);
                
                int targetPlayerId = temp.id;
                if (temp.chucInSudo == 2 && temp.nameRelative != null && !temp.nameRelative.isEmpty()) {
                    Sudo masterSudo = Sudo.getSuDoByName(temp.nameRelative.get(0));
                    if (masterSudo != null) {
                        targetPlayerId = masterSudo.id;
                    }
                }
                
                psInsert.setInt(1, temp.accountId);
                psInsert.setInt(2, targetPlayerId);
                psInsert.setString(3, js.toJSONString());
                psInsert.addBatch();
                
                if (i % 1000 == 0) {
                    psInsert.executeBatch();
                }
                i++;
            }
            psInsert.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (psDelete != null) psDelete.close();
                if (psInsert != null) psInsert.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
