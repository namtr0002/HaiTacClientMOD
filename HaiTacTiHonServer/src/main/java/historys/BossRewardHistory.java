package historys;

import boss.BossReward;
import database.DbManager;
import template.GiftBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class BossRewardHistory {

    public static void load(ResultSet rs, List<BossReward> topBossReward) throws SQLException {
        BossReward t = new BossReward();
        t.playerId = rs.getInt("player_id");
        t.listGiftBox = new ArrayList<>();
        
        String dataStr = rs.getString("data");
        Object parsed = JSONValue.parse(dataStr);
        if (parsed instanceof JSONObject) {
            JSONObject js_obj = (JSONObject) parsed;
            if (js_obj.containsKey("player_id")) {
                try {
                    t.playerId = Integer.parseInt(js_obj.get("player_id").toString());
                } catch (Exception ignored) {}
            }
            t.name = js_obj.get("name").toString();
            if (t.playerId > 0) {
                String currentName = database.DbManager.getPlayerNameById(t.playerId);
                if (currentName != null && !currentName.isEmpty()) {
                    t.name = currentName;
                }
            } else {
                t.playerId = database.DbManager.getPlayerIdByName(t.name);
            }
            JSONArray js = (JSONArray) js_obj.get("reward");
            for (int i = 0; i < js.size(); i++) {
                JSONArray js_in = (JSONArray) js.get(i);
                GiftBox gift = new GiftBox();
                gift.id = Short.parseShort(js_in.get(0).toString());
                gift.type = Byte.parseByte(js_in.get(1).toString());
                gift.name = js_in.get(2).toString();
                gift.icon = Short.parseShort(js_in.get(3).toString());
                gift.num = Integer.parseInt(js_in.get(4).toString());
                gift.color = Byte.parseByte(js_in.get(5).toString());
                t.listGiftBox.add(gift);
            }
        }
        topBossReward.add(t);
    }

    public static void updateReward(List<BossReward> topBossReward) {
        if (topBossReward == null || topBossReward.isEmpty()) return;
        Connection conn = null;
        PreparedStatement psDelete = null;
        PreparedStatement psInsert = null;
        PreparedStatement psPlayer = null;
        try {
            conn = DbManager.gI().getConnect();
            if (conn == null) return;
            conn.setAutoCommit(false);
            
            psDelete = conn.prepareStatement("DELETE FROM `historys` WHERE `type` = 'BOSS_REWARD'");
            psDelete.executeUpdate();
            
            psInsert = conn.prepareStatement(
                "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, ?, 'BOSS_REWARD', ?)"
            );
            psPlayer = conn.prepareStatement("SELECT id, account_id FROM players WHERE name = ? LIMIT 1");
            
            for (int i = 0; i < topBossReward.size(); i++) {
                BossReward reward = topBossReward.get(i);
                if (reward == null) continue;
                
                int playerId = reward.playerId;
                int accountId = 0;
                if (playerId <= 0) {
                    model.Player onlineP = network.SessionManager.PLAYERS_BY_NAME.get(reward.name);
                    if (onlineP != null) {
                        playerId = onlineP.IDPlayer;
                        accountId = (onlineP.conn != null) ? onlineP.conn.idUser : 0;
                    } else {
                        psPlayer.setString(1, reward.name);
                        try (ResultSet rs = psPlayer.executeQuery()) {
                            if (rs.next()) {
                                playerId = rs.getInt("id");
                                accountId = rs.getInt("account_id");
                            }
                        }
                    }
                } else {
                    accountId = historys.HistoryManager.getAccountIdByPlayerId(playerId);
                }
                
                JSONObject js_obj = new JSONObject();
                js_obj.put("player_id", playerId);
                js_obj.put("name", reward.name);
                JSONArray js = new JSONArray();
                if (reward.listGiftBox != null) {
                    for (int j = 0; j < reward.listGiftBox.size(); j++) {
                        GiftBox gift = reward.listGiftBox.get(j);
                        if (gift != null) {
                            JSONArray js_in = new JSONArray();
                            js_in.add(gift.id);
                            js_in.add(gift.type);
                            js_in.add(gift.name);
                            js_in.add(gift.icon);
                            js_in.add(gift.num);
                            js_in.add(gift.color);
                            js.add(js_in);
                        }
                    }
                }
                js_obj.put("reward", js);
                
                psInsert.setInt(1, accountId);
                psInsert.setInt(2, playerId);
                psInsert.setString(3, js_obj.toJSONString());
                psInsert.addBatch();
            }
            psInsert.executeBatch();
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Error saving BossReward to DB: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (psPlayer != null) psPlayer.close();
                if (psDelete != null) psDelete.close();
                if (psInsert != null) psInsert.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
