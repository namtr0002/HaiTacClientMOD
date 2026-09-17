package historys;

import database.DbManager;
import map.zones.WorldWar;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WorldWarHistory {

    public static void loadData(java.util.concurrent.ConcurrentHashMap<String, Integer> mem_haiquan,
                                java.util.concurrent.ConcurrentHashMap<String, Integer> mem_haitac,
                                java.util.concurrent.ConcurrentHashMap<String, Integer> mem_quancachmang,
                                java.util.HashMap<String, Integer> wins,
                                java.util.HashMap<String, Integer> top3) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("SELECT `data` FROM `historys` WHERE `type` = 'WORLDWAR'");
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String jsonData = rs.getString("data");
                if (jsonData != null && !jsonData.isEmpty()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    WorldWar.DataContainer dataContainer = objectMapper.readValue(jsonData, WorldWar.DataContainer.class);
                    if (dataContainer.getMem_haiquan() != null) {
                        mem_haiquan.putAll(dataContainer.getMem_haiquan());
                    }
                    if (dataContainer.getMem_haitac() != null) {
                        mem_haitac.putAll(dataContainer.getMem_haitac());
                    }
                    if (dataContainer.getMem_quancachmang() != null) {
                        mem_quancachmang.putAll(dataContainer.getMem_quancachmang());
                    }
                    if (dataContainer.getWins() != null) {
                        wins.putAll(dataContainer.getWins());
                    }
                    if (dataContainer.getTop3() != null) {
                        top3.putAll(dataContainer.getTop3());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading WorldWar from DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void saveData(java.util.concurrent.ConcurrentHashMap<String, Integer> mem_haiquan,
                                java.util.concurrent.ConcurrentHashMap<String, Integer> mem_haitac,
                                java.util.concurrent.ConcurrentHashMap<String, Integer> mem_quancachmang,
                                java.util.HashMap<String, Integer> wins,
                                java.util.HashMap<String, Integer> top3) {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return;
            try {
                conn.setAutoCommit(false);
                
                WorldWar.DataContainer dataContainer = new WorldWar.DataContainer(mem_haiquan, mem_haitac, mem_quancachmang, wins, top3);
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonStr = objectMapper.writeValueAsString(dataContainer);

                try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM `historys` WHERE `type` = 'WORLDWAR'")) {
                    psDel.executeUpdate();
                }
                try (PreparedStatement psIns = conn.prepareStatement("INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (0, 0, 'WORLDWAR', ?)")) {
                    psIns.setString(1, jsonStr);
                    psIns.executeUpdate();
                }
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
        } catch (Exception e) {
            System.err.println("Error saving WorldWar to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
