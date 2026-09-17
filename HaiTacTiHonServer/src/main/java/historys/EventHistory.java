package historys;

import database.DbManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class EventHistory {

    public static String loadEventData(model.Player p) {
        return loadEventData(p, null);
    }

    public static String loadEventData(model.Player p, String seasonKey) {
        if (p == null) return null;
        String typeKey = HistoryManager.formatKey("EVENT_DATA", seasonKey);
        String data = HistoryManager.loadData(p, typeKey);
        if (data != null) {
            return data;
        }
        if (seasonKey != null && !seasonKey.trim().isEmpty()) {
            data = HistoryManager.loadData(p, seasonKey.trim());
            if (data != null) return data;
        }
        if (!"EVENT_DATA".equals(typeKey)) {
            return HistoryManager.loadData(p, "EVENT_DATA");
        }
        return null;
    }

    public static String loadEventData(int playerId) {
        return loadEventData(playerId, null);
    }

    public static String loadEventData(int playerId, String seasonKey) {
        String typeKey = HistoryManager.formatKey("EVENT_DATA", seasonKey);
        String data = HistoryManager.loadData(playerId, typeKey);
        if (data != null) {
            return data;
        }
        // Fallback: Thử truy vấn trực tiếp theo seasonKey dạng VARCHAR (ví dụ: "MUA_1", "MUA_1_SO_SU_KIEN", "SO_SU_KIEN_MUA_1")
        if (seasonKey != null && !seasonKey.trim().isEmpty()) {
            data = HistoryManager.loadData(playerId, seasonKey.trim());
            if (data != null) return data;
        }
        // Fallback: Thử tải theo type mặc định "EVENT_DATA"
        if (!"EVENT_DATA".equals(typeKey)) {
            return HistoryManager.loadData(playerId, "EVENT_DATA");
        }
        return null;
    }

    public static void saveEventData(model.Player p, String jsonData) {
        saveEventData(p, null, jsonData);
    }

    public static void saveEventData(model.Player p, String seasonKey, String jsonData) {
        if (p == null) return;
        String typeKey = HistoryManager.formatKey("EVENT_DATA", seasonKey);
        HistoryManager.saveData(p, typeKey, jsonData);
    }

    public static void saveEventData(int accountId, int playerId, String jsonData) {
        saveEventData(accountId, playerId, null, jsonData);
    }

    public static void saveEventData(int accountId, int playerId, String seasonKey, String jsonData) {
        String typeKey = HistoryManager.formatKey("EVENT_DATA", seasonKey);
        HistoryManager.saveData(accountId, playerId, typeKey, jsonData);
    }

    public static void updateOfflineEventData(String playerName, int activeEventId, int value) {
        updateOfflineEventData(playerName, activeEventId, value, null);
    }

    public static void updateOfflineEventData(String playerName, int activeEventId, int value, String seasonKey) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement psSel = conn.prepareStatement("SELECT `account_id`, `id` FROM `players` WHERE `name` = ? LIMIT 1")) {
            psSel.setString(1, playerName);
            try (ResultSet rs = psSel.executeQuery()) {
                if (rs.next()) {
                    int accId = rs.getInt("account_id");
                    int pId = rs.getInt("id");
                    
                    String eventJson = loadEventData(pId, seasonKey);
                    if (eventJson != null && !eventJson.isEmpty() && !eventJson.equals("[]")) {
                        JSONArray js12 = (JSONArray) JSONValue.parse(eventJson);
                        if (js12 != null) {
                            boolean updated = false;
                            for (int i = 0; i < js12.size(); i++) {
                                JSONObject eo = (JSONObject) js12.get(i);
                                if (eo.get("ev") != null && Integer.parseInt(eo.get("ev").toString()) == activeEventId) {
                                    JSONArray da = (JSONArray) eo.get("d");
                                    if (da != null) {
                                        while (da.size() < 30) da.add(0);
                                        long currentVal = Long.parseLong(da.get(20).toString());
                                        da.set(20, Math.max(0, currentVal - value));
                                        updated = true;
                                    }
                                    if (eo.containsKey("pt")) {
                                        Object ptObj = eo.get("pt");
                                        if (ptObj instanceof JSONObject) {
                                            JSONObject pt = (JSONObject) ptObj;
                                            if (pt.containsKey("point_event1")) {
                                                long curPt = Long.parseLong(pt.get("point_event1").toString());
                                                pt.put("point_event1", Math.max(0, curPt - value));
                                                updated = true;
                                            }
                                            if (pt.containsKey("top_goi_hop_qua")) {
                                                long curPt = Long.parseLong(pt.get("top_goi_hop_qua").toString());
                                                pt.put("top_goi_hop_qua", Math.max(0, curPt - value));
                                                updated = true;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                            if (updated) {
                                saveEventData(accId, pId, seasonKey, js12.toJSONString());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
