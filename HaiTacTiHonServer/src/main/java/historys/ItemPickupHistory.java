package historys;

import database.DbManager;
import model.Player;
import map.Zone;
import core.ZUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ItemPickupHistory — Quản lý lịch sử nhặt vật phẩm tối ưu hóa.
 * 
 * Mỗi row trong bảng `historys` lưu đúng 100 vật phẩm (hoặc đệm đến khi đủ 100/flush khi logout).
 * Cấu trúc dữ liệu JSON chuẩn hóa kèm mã băm MD5 định danh cho từng lô (Batch).
 */
public class ItemPickupHistory {

    public static final String TYPE = "ITEM_PICKUP";
    public static final int BATCH_SIZE = 100;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static class ItemPickupEntry {
        public String time;
        public int cat;
        public int itemId;
        public String itemName;
        public int quantity;
        public int mapId;
        public String mapName;

        public ItemPickupEntry(int cat, int itemId, String itemName, int quantity, int mapId, String mapName) {
            this.time = DATE_FORMAT.format(new Date());
            this.cat = cat;
            this.itemId = itemId;
            this.itemName = (itemName != null && !itemName.isEmpty()) ? itemName : ("Item #" + itemId);
            this.quantity = Math.max(1, quantity);
            this.mapId = mapId;
            this.mapName = (mapName != null && !mapName.isEmpty()) ? mapName : ("Map " + mapId);
        }

        public ItemPickupEntry(String text) {
            this.time = DATE_FORMAT.format(new Date());
            this.cat = 0;
            this.itemId = 0;
            this.itemName = (text != null) ? text : "Vật phẩm";
            this.quantity = 1;
            this.mapId = -1;
            this.mapName = "";
        }

        @SuppressWarnings("unchecked")
        public JSONObject toJsonObject() {
            JSONObject obj = new JSONObject();
            obj.put("time", time);
            obj.put("cat", cat);
            obj.put("id", itemId);
            obj.put("name", itemName);
            obj.put("quant", quantity);
            if (mapId >= 0) {
                obj.put("map_id", mapId);
                obj.put("map_name", mapName);
            }
            return obj;
        }

        public static ItemPickupEntry fromJsonObject(JSONObject obj) {
            if (obj == null) return null;
            int cat = obj.containsKey("cat") ? ((Number) obj.get("cat")).intValue() : 0;
            int id = obj.containsKey("id") ? ((Number) obj.get("id")).intValue() : 0;
            String name = obj.containsKey("name") ? obj.get("name").toString() : "";
            int quant = obj.containsKey("quant") ? ((Number) obj.get("quant")).intValue() : 1;
            int mapId = obj.containsKey("map_id") ? ((Number) obj.get("map_id")).intValue() : -1;
            String mapName = obj.containsKey("map_name") ? obj.get("map_name").toString() : "";
            
            ItemPickupEntry entry = new ItemPickupEntry(cat, id, name, quant, mapId, mapName);
            if (obj.containsKey("time")) {
                entry.time = obj.get("time").toString();
            }
            return entry;
        }
    }

    private static final Map<Integer, ConcurrentLinkedQueue<ItemPickupEntry>> PLAYER_BUFFERS = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> PLAYER_ACCOUNTS = new ConcurrentHashMap<>();

    /**
     * Ghi nhận 1 lượt nhặt đồ vào bộ đệm của Player.
     * Khi đạt đủ BATCH_SIZE (100 cái) sẽ tự động ghi 1 dòng vào bảng historys.
     */
    public static void logPickup(Player p, int cat, int itemId, String itemName, int quantity, Zone map) {
        if (p == null || p.isBot) return;
        int playerId = p.IDPlayer;
        int accountId = (p.conn != null) ? p.conn.idUser : 0;
        PLAYER_ACCOUNTS.put(playerId, accountId);

        int mapId = (map != null && map.template != null) ? map.template.id : -1;
        String mapName = (map != null && map.template != null) ? map.template.name : "";

        ItemPickupEntry entry = new ItemPickupEntry(cat, itemId, itemName, quantity, mapId, mapName);
        addEntryAndCheckFlush(accountId, playerId, entry);
    }

    /**
     * Ghi nhận theo chuỗi text (ví dụ từ zLog: "nhặt phân bón và nước lúc đánh quái").
     */
    public static void logPickupText(Player p, String rawText) {
        if (p == null || p.isBot) return;
        int playerId = p.IDPlayer;
        int accountId = (p.conn != null) ? p.conn.idUser : 0;
        PLAYER_ACCOUNTS.put(playerId, accountId);

        ItemPickupEntry entry = new ItemPickupEntry(rawText);
        addEntryAndCheckFlush(accountId, playerId, entry);
    }

    private static void addEntryAndCheckFlush(int accountId, int playerId, ItemPickupEntry entry) {
        ConcurrentLinkedQueue<ItemPickupEntry> queue = PLAYER_BUFFERS.computeIfAbsent(playerId, k -> new ConcurrentLinkedQueue<>());
        queue.add(entry);

        if (queue.size() >= BATCH_SIZE) {
            flushBatch(accountId, playerId, BATCH_SIZE);
        }
    }

    /**
     * Rút đúng batchSize phần tử và lưu vào 1 row trong historys.
     */
    private static synchronized void flushBatch(int accountId, int playerId, int batchSize) {
        ConcurrentLinkedQueue<ItemPickupEntry> queue = PLAYER_BUFFERS.get(playerId);
        if (queue == null || queue.isEmpty()) return;

        List<ItemPickupEntry> batch = new ArrayList<>();
        while (!queue.isEmpty() && batch.size() < batchSize) {
            ItemPickupEntry entry = queue.poll();
            if (entry != null) {
                batch.add(entry);
            }
        }

        if (!batch.isEmpty()) {
            saveBatchToDb(accountId, playerId, batch);
        }
    }

    /**
     * Flush toàn bộ vật phẩm còn đọng lại trong buffer của player (gọi khi player logout hoặc chuyển zone).
     */
    public static void flushPlayer(Player p) {
        flushPlayer((Connection) null, p);
    }

    public static void flushPlayer(Connection conn, Player p) {
        if (p == null) return;
        int playerId = p.IDPlayer;
        int accountId = (p.conn != null) ? p.conn.idUser : 0;
        flushPlayer(conn, accountId, playerId);
    }

    public static void flushPlayer(int accountId, int playerId) {
        flushPlayer(null, accountId, playerId);
    }

    public static void flushPlayer(Connection conn, int accountId, int playerId) {
        ConcurrentLinkedQueue<ItemPickupEntry> queue = PLAYER_BUFFERS.get(playerId);
        if (queue == null || queue.isEmpty()) {
            PLAYER_BUFFERS.remove(playerId);
            PLAYER_ACCOUNTS.remove(playerId);
            return;
        }

        List<ItemPickupEntry> batch = new ArrayList<>();
        while (!queue.isEmpty()) {
            ItemPickupEntry entry = queue.poll();
            if (entry != null) {
                batch.add(entry);
            }
        }

        PLAYER_BUFFERS.remove(playerId);
        PLAYER_ACCOUNTS.remove(playerId);

        if (!batch.isEmpty()) {
            saveBatchToDb(conn, accountId, playerId, batch);
        }
    }

    /**
     * Flush toàn bộ buffer của tất cả người chơi (gọi khi Server shutdown hoặc định kỳ).
     */
    public static void flushAll() {
        for (Map.Entry<Integer, ConcurrentLinkedQueue<ItemPickupEntry>> entry : PLAYER_BUFFERS.entrySet()) {
            int playerId = entry.getKey();
            int accountId = PLAYER_ACCOUNTS.getOrDefault(playerId, 0);
            flushPlayer(null, accountId, playerId);
        }
    }

    /**
     * Ghi 1 mảng các vật phẩm vào đúng 1 bản ghi duy nhất trong bảng historys kèm mã MD5 Batch Token.
     */
    @SuppressWarnings("unchecked")
    private static void saveBatchToDb(int accountId, int playerId, List<ItemPickupEntry> batch) {
        saveBatchToDb(null, accountId, playerId, batch);
    }

    @SuppressWarnings("unchecked")
    private static void saveBatchToDb(Connection conn, int accountId, int playerId, List<ItemPickupEntry> batch) {
        if (batch == null || batch.isEmpty()) return;

        long now = System.currentTimeMillis();
        String md5Token = ZUtil.generateMD5Token("PICKUP_" + playerId + "_" + now + "_" + batch.size() + "_" + Math.random());
        String startTime = batch.get(0).time;
        String endTime = batch.get(batch.size() - 1).time;

        String pDate = HistoryManager.getPlayerCreatedAt(playerId);
        String aDate = HistoryManager.getAccountCreatedAt(accountId);
        String user = HistoryManager.getUsernameByPlayerId(playerId);
        String pName = HistoryManager.getPlayerNameById(playerId);
        String pMd5 = HistoryManager.getPlayerMd5(aDate, user, accountId, pDate, pName, playerId);

        JSONObject root = new JSONObject();
        root.put("batch_md5", md5Token);
        root.put("p_md5", pMd5);
        root.put("p_date", pDate);
        root.put("count", batch.size());
        root.put("start_time", startTime);
        root.put("end_time", endTime);

        JSONArray itemArray = new JSONArray();
        for (ItemPickupEntry e : batch) {
            itemArray.add(e.toJsonObject());
        }
        root.put("items", itemArray);

        String jsonData = root.toJSONString();

        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, ?, ?, ?)"
                 )) {
                ps.setInt(1, accountId);
                ps.setInt(2, playerId);
                ps.setString(3, TYPE);
                ps.setString(4, jsonData);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("[ItemPickupHistory] Error saving batch to DB for player " + playerId + ": " + e.getMessage());
            }
        } else {
            try (Connection newConn = DbManager.gI().getConnect();
                 PreparedStatement ps = newConn.prepareStatement(
                     "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, ?, ?, ?)"
                 )) {
                ps.setInt(1, accountId);
                ps.setInt(2, playerId);
                ps.setString(3, TYPE);
                ps.setString(4, jsonData);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("[ItemPickupHistory] Error saving batch to DB for player " + playerId + ": " + e.getMessage());
            }
        }
    }

    /**
     * Tra cứu lịch sử nhặt theo mã batch MD5.
     */
    public static JSONObject findBatchByMd5(String md5Token) {
        if (md5Token == null || md5Token.trim().isEmpty()) return null;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `data` FROM `historys` WHERE `type` = ? AND `data` LIKE ? LIMIT 1"
             )
        ) {
            ps.setString(1, TYPE);
            ps.setString(2, "%\"batch_md5\":\"" + md5Token.trim() + "\"%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String jsonStr = rs.getString("data");
                    Object parsed = JSONValue.parse(jsonStr);
                    if (parsed instanceof JSONObject) {
                        return (JSONObject) parsed;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[ItemPickupHistory] Error finding batch by MD5 " + md5Token + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Tra cứu danh sách các batch nhặt đồ gần nhất của Player.
     */
    @SuppressWarnings("unchecked")
    public static List<JSONObject> getPickupBatches(int playerId, int limit) {
        List<JSONObject> list = new ArrayList<>();
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `data`, `create_at` FROM `historys` WHERE `player_id` = ? AND `type` = ? ORDER BY `id` DESC LIMIT ?"
             )
        ) {
            ps.setInt(1, playerId);
            ps.setString(2, TYPE);
            ps.setInt(3, Math.max(1, limit));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String jsonStr = rs.getString("data");
                    Object parsed = JSONValue.parse(jsonStr);
                    if (parsed instanceof JSONObject) {
                        JSONObject obj = (JSONObject) parsed;
                        obj.put("row_id", rs.getInt("id"));
                        obj.put("create_at", rs.getString("create_at"));
                        list.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[ItemPickupHistory] Error getting batches for player " + playerId + ": " + e.getMessage());
        }
        return list;
    }
}
