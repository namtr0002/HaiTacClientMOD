package bot.botplayer.ai;

import model.Player;
import ability.Ability;
import map.Zone;
import database.DbManager;
import core.ZUtil;
import core.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BotPvpCacheManager — Quản lý bộ nhớ Cache PvP Bot.
 *
 * Tải sẵn và lưu trữ danh sách Profile người chơi / bot offline từ bảng `players` và `players_bot` vào RAM.
 * Giúp ghép trận PvP nhanh tức thì, tránh hoàn toàn việc query SQL `ORDER BY RAND()` mỗi lần ghép trận.
 */
public class BotPvpCacheManager {

    public static class BotProfile {
        public String name;
        public int level;
        public byte clazz;
        public long exp;
        public int wanted_point;
        public String tableName; // "players" hoặc "players_bot"

        public BotProfile(String name, int level, byte clazz, long exp, int wanted_point, String tableName) {
            this.name = name;
            this.level = level;
            this.clazz = clazz;
            this.exp = exp;
            this.wanted_point = wanted_point;
            this.tableName = tableName;
        }
    }

    private static final List<BotProfile> cachedProfiles = new CopyOnWriteArrayList<>();
    private static long lastRefreshTime = 0;
    private static final long REFRESH_INTERVAL_MS = 15 * 60 * 1000L; // 15 phút refresh cache 1 lần

    /**
     * Khởi tạo và nạp Cache PvP Bot từ DB (Gọi lúc Server startup).
     */
    public static synchronized void initCache() {
        refreshCache();
    }

    /**
     * Làm mới danh sách Profile Bot từ DB (Chạy ngầm).
     */
    public static synchronized void refreshCache() {
        long now = System.currentTimeMillis();
        List<BotProfile> newList = new ArrayList<>();

        // 1. Load từ bảng `players` (người chơi offline)
        loadFromTable(newList, "players", 500);

        // 2. Load từ bảng `players_bot` (bot hệ thống)
        loadFromTable(newList, "players_bot", 500);

        if (!newList.isEmpty()) {
            cachedProfiles.clear();
            cachedProfiles.addAll(newList);
            lastRefreshTime = now;
            Log.success("BotPvpCacheManager", "Pre-loaded " + cachedProfiles.size() + " bot profiles into RAM cache for PvP matchmaking.");
        }
    }

    private static void loadFromTable(List<BotProfile> list, String tableName, int limit) {
        String query;
        if ("players".equalsIgnoreCase(tableName)) {
            query = "SELECT `name`, `level`, `clazz`, `exp`, "
                  + "COALESCE(CAST(JSON_UNQUOTE(JSON_EXTRACT(`inventory`, '$.wanted_point')) AS SIGNED), "
                  + "         CAST(JSON_UNQUOTE(JSON_EXTRACT(`inventory`, '$.wanted')) AS SIGNED), 0) AS `wanted_point` "
                  + "FROM `players` ORDER BY `id` DESC LIMIT ?;";
        } else {
            query = "SELECT `name`, `level`, `clazz`, `exp`, 0 AS `wanted_point` FROM `" + tableName + "` ORDER BY `id` DESC LIMIT ?;";
        }
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    String rawLevel = rs.getString("level");
                    int level = parseLevel(rawLevel);
                    byte clazz = rs.getByte("clazz");
                    long exp = parseExp(rawLevel, rs.getLong("exp"));
                    int wantedPoint = rs.getInt("wanted_point");
                    if (name != null && !name.trim().isEmpty()) {
                        list.add(new BotProfile(name, Math.max(1, level), clazz, exp, wantedPoint, tableName));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[BotPvpCacheManager] Error loading from " + tableName + ": " + e.getMessage());
        }
    }

    private static int parseLevel(String rawLevel) {
        if (rawLevel == null || rawLevel.trim().isEmpty()) return 1;
        try {
            Object parsed = JSONValue.parse(rawLevel);
            if (parsed instanceof JSONObject) {
                JSONObject o = (JSONObject) parsed;
                Object lvObj = o.get("lv");
                if (lvObj != null) return Integer.parseInt(lvObj.toString());
            } else if (parsed instanceof JSONArray) {
                JSONArray a = (JSONArray) parsed;
                if (a.size() > 0) return Integer.parseInt(a.get(0).toString());
            } else if (parsed instanceof Number) {
                return ((Number) parsed).intValue();
            } else {
                return Integer.parseInt(rawLevel.trim());
            }
        } catch (Exception ignored) {}
        return 1;
    }

    private static long parseExp(String rawLevel, long rawExp) {
        if (rawExp > 0) return rawExp;
        if (rawLevel == null || rawLevel.trim().isEmpty()) return rawExp;
        try {
            Object parsed = JSONValue.parse(rawLevel);
            if (parsed instanceof JSONObject) {
                JSONObject o = (JSONObject) parsed;
                Object expObj = o.get("exp");
                if (expObj != null) return Long.parseLong(expObj.toString());
            } else if (parsed instanceof JSONArray) {
                JSONArray a = (JSONArray) parsed;
                if (a.size() > 1) return Long.parseLong(a.get(1).toString());
            }
        } catch (Exception ignored) {}
        return rawExp;
    }

    /**
     * Tìm kiếm Candidate Bot từ Cache trong RAM phù hợp với level người chơi `p`.
     */
    public static BotProfile findCachedProfile(Player p) {
        if (p == null) return null;
        long now = System.currentTimeMillis();
        if (now - lastRefreshTime > REFRESH_INTERVAL_MS || cachedProfiles.isEmpty()) {
            refreshCache();
        }

        int targetLevel = p.level;
        int maxLevelDiff = Math.max(2, (int) (targetLevel * 0.10)); // Tỉ lệ chênh lệch tối đa 10%
        List<BotProfile> candidates = new ArrayList<>();

        // Tìm bot trong khoảng level chênh lệch <= 10% để đảm bảo cân bằng
        for (BotProfile prof : cachedProfiles) {
            if (prof.name != null && !prof.name.equalsIgnoreCase(p.name)) {
                if (Zone.get_player_by_name_allmap(prof.name) == null) { // Không online
                    if (Math.abs(prof.level - targetLevel) <= maxLevelDiff) {
                        candidates.add(prof);
                    }
                }
            }
        }

        if (!candidates.isEmpty()) {
            return candidates.get(ZUtil.random(candidates.size()));
        }
        return null;
    }

    /**
     * Tìm kiếm BotProfile trong Cache theo tên nhân vật (không phân biệt hoa thường).
     */
    public static BotProfile findCachedProfileByName(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        String clean = name.trim();
        if (cachedProfiles.isEmpty()) {
            refreshCache();
        }
        for (BotProfile prof : cachedProfiles) {
            if (prof != null && prof.name != null && prof.name.equalsIgnoreCase(clean)) {
                return prof;
            }
        }
        return null;
    }
}
