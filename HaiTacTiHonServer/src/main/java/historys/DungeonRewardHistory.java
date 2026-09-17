package historys;

import database.DbManager;
import template.GiftBox;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;
import core.ZUtil;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Lưu trữ và quản lý quà phần thưởng top phó bản & săn Boss vào DB.
 * Key: (player_id, dungeonId, roundToken/md5) - tránh nhận trùng (Idempotent).
 * Dữ liệu item/quà được mã hóa gọn gàng (compact JSON array) để tối ưu dung lượng DB.
 */
public class DungeonRewardHistory {

    public static final String TYPE = "DUNGEON_TOP_REWARD";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getDungeonDisplayName(String dungeonType) {
        if (dungeonType == null) return "Phó Bản";
        return PendingReward.dungeonDisplayName(dungeonType);
    }

    /**
     * Lưu quà top của một phó bản hoặc săn Boss vào DB cho một player.
     * Nếu đã có bản ghi (roundToken trùng) thì bỏ qua (idempotent).
     */
    public static void saveReward(model.Player p, String dungeonId, String roundToken,
                                   int rank, long damage, List<GiftBox> gifts) {
        if (p == null) return;
        int accId = (p.conn != null) ? p.conn.idUser : HistoryManager.getAccountIdByPlayerId(p.IDPlayer);
        saveReward(p.IDPlayer, accId, dungeonId, roundToken, rank, damage, gifts);
    }

    public static void saveReward(int playerId, int accountId,
                                   String dungeonId, String roundToken,
                                   int rank, List<GiftBox> gifts) {
        saveReward(playerId, accountId, dungeonId, roundToken, rank, 0L, gifts);
    }

    /**
     * Lưu quà top của phó bản/Boss kèm thông tin lượng sát thương gây ra.
     * Dữ liệu gifts được mã hóa dạng compact array [id, type, num, icon, color, name] siêu gọn.
     */
    @SuppressWarnings("unchecked")
    public static void saveReward(int playerId, int accountId,
                                   String dungeonId, String roundToken,
                                   int rank, long damage, List<GiftBox> gifts) {
        if (gifts == null || gifts.isEmpty()) return;
        String key = dungeonId + "|" + roundToken;
        if (hasReward(playerId, key)) return; // đã có, bỏ qua

        long now = System.currentTimeMillis();
        String timeStr = DATE_FORMAT.format(new java.util.Date(now));

        String pDate = HistoryManager.getPlayerCreatedAt(playerId);
        String aDate = HistoryManager.getAccountCreatedAt(accountId);
        String user = HistoryManager.getUsernameByPlayerId(playerId);
        String pName = HistoryManager.getPlayerNameById(playerId);
        String pMd5 = HistoryManager.getPlayerMd5(aDate, user, accountId, pDate, pName, playerId);

        JSONObject js = new JSONObject();
        js.put("md5", roundToken);
        js.put("p_md5", pMd5);
        js.put("p_date", pDate);
        js.put("a_date", aDate);
        js.put("dungeon", dungeonId);
        js.put("round", roundToken);
        js.put("rank", rank);
        if (damage > 0) {
            js.put("dame", damage);
        }
        js.put("claimed", true);
        js.put("time", timeStr);

        // Compact array format cho quà: [id, type, num, icon, color, name]
        JSONArray arr = new JSONArray();
        for (GiftBox g : gifts) {
            if (g == null) continue;
            JSONArray item = new JSONArray();
            item.add(g.id);
            item.add(g.type);
            item.add(g.num);
            item.add(g.icon);
            item.add(g.color);
            item.add(g.name != null ? g.name : "");
            arr.add(item);
        }
        js.put("gifts", arr);

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, ?, ?, ?)"
             )) {
            ps.setInt(1, accountId);
            ps.setInt(2, playerId);
            ps.setString(3, TYPE);
            ps.setString(4, js.toJSONString());
            ps.executeUpdate();
            
            // Gửi quà phó bản trực tiếp vào Hộp Thư
            String dName = getDungeonDisplayName(dungeonId);
            String title = "Thưởng " + dName + (rank > 0 ? " (Hạng " + rank + ")" : "");
            String content = "Chúc mừng bạn đã hoàn thành " + dName + (rank > 0 ? " đạt hạng " + rank : "") + "! Nhận phần thưởng đính kèm bên dưới.";
            core.MailService.sendMailOffline(playerId, accountId, pName, "Phó Bản", title, content, core.MailService.MAIL_TYPE_GIFT, false, gifts, 0L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<PendingReward> getPendingRewards(model.Player p) {
        return getPendingRewardsByType(p, null);
    }

    public static List<PendingReward> getPendingRewardsByType(model.Player p, String dungeonType) {
        if (p == null) return new ArrayList<>();
        return getPendingRewardsByType(p.IDPlayer, dungeonType);
    }

    /**
     * Lấy danh sách quà chưa nhận của player (claimed=false).
     * Trả về list các entry chưa claim để hiển thị menu.
     */
    public static List<PendingReward> getPendingRewards(int playerId) {
        return getPendingRewardsByType(playerId, null);
    }

    /**
     * Lấy danh sách quà chưa nhận của player lọc theo loại phó bản (ví dụ: "SUPER_BOSS", "DAU_TRUONG_TU_DO"...).
     * Hỗ trợ alias matching để đảm bảo không bị sót quà.
     * Tự động lọc và xóa các bản ghi cũ của nhân vật đời trước nếu server chưa reset historys.
     */
    public static List<PendingReward> getPendingRewardsByType(int playerId, String dungeonType) {
        List<PendingReward> result = new ArrayList<>();
        List<Integer> staleIdsToDelete = new ArrayList<>();
        String playerDateStr = HistoryManager.getPlayerCreatedAt(playerId);
        model.Player onlineP = map.Zone.get_player_by_id_allmap(playerId);

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `data`, `create_at` FROM `historys` WHERE `player_id` = ? AND `type` = ? ORDER BY `id` ASC"
             )) {
            ps.setInt(1, playerId);
            ps.setString(2, TYPE);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int rowId = rs.getInt("id");
                    String data = rs.getString("data");
                    Timestamp cAt = rs.getTimestamp("create_at");

                    if (onlineP != null) {
                        if (!HistoryManager.validatePlayerRecord(onlineP, data, cAt)) {
                            staleIdsToDelete.add(rowId);
                            continue;
                        }
                    } else if (playerDateStr != null && !playerDateStr.isEmpty() && cAt != null) {
                        try {
                            org.joda.time.DateTime pdt = org.joda.time.DateTime.parse(playerDateStr);
                            if (cAt.getTime() < (pdt.getMillis() - 5000L)) {
                                staleIdsToDelete.add(rowId);
                                continue;
                            }
                        } catch (Exception ignored) {}
                    }

                    Object parsed = JSONValue.parse(data);
                    if (parsed instanceof JSONObject) {
                        JSONObject js = (JSONObject) parsed;
                        boolean claimed = Boolean.TRUE.equals(js.get("claimed"));
                        if (!claimed) {
                            String dId = (String) js.get("dungeon");
                            if (dungeonType != null && !dungeonType.trim().isEmpty()) {
                                if (!isDungeonMatch(dId, dungeonType)) {
                                    continue;
                                }
                            }
                            PendingReward pr = parsePendingReward(rowId, js, cAt != null ? cAt.toString() : "");
                            if (pr != null) {
                                result.add(pr);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tự động xóa các bản ghi quà mồ côi / cũ của nhân vật đời trước
        if (!staleIdsToDelete.isEmpty()) {
            try (Connection conn = DbManager.gI().getConnect();
                 PreparedStatement psDel = conn.prepareStatement("DELETE FROM `historys` WHERE `id` = ?")) {
                for (int id : staleIdsToDelete) {
                    psDel.setInt(1, id);
                    psDel.addBatch();
                }
                psDel.executeBatch();
            } catch (Exception ignored) {}
        }

        return result;
    }

    /**
     * Kiểm tra khớp loại phó bản hỗ trợ alias
     */
    public static boolean isDungeonMatch(String targetDungeon, String queryType) {
        if (targetDungeon == null || queryType == null) return false;
        String t = targetDungeon.trim().toUpperCase();
        String q = queryType.trim().toUpperCase();
        if (t.equals(q)) return true;

        if (q.contains("DAU_TRUONG") && t.contains("DAU_TRUONG")) return true;
        if ((q.contains("WORLD_WAR") || q.contains("TRAN_CHIEN_LON")) && (t.contains("WORLD_WAR") || t.contains("TRAN_CHIEN_LON"))) return true;
        if (q.contains("CHIEM_DAO") && t.contains("CHIEM_DAO")) return true;
        if ((q.contains("SUPER_BOSS") || q.contains("SIEU_TRUM")) && (t.contains("SUPER_BOSS") || t.contains("SIEU_TRUM"))) return true;
        if ((q.contains("KHONG_LO") || q.contains("LITTLE_GARDEN")) && (t.contains("KHONG_LO") || t.contains("LITTLE_GARDEN"))) return true;
        if (q.contains("PHAO_DAI") && t.contains("PHAO_DAI")) return true;
        if (q.contains("THU_LINH") && t.contains("THU_LINH")) return true;
        if (q.contains("PVP_BANG") && t.contains("PVP_BANG")) return true;
        return false;
    }

    /**
     * Parse đối tượng PendingReward từ JSON với khả năng tương thích ngược toàn diện
     */
    private static PendingReward parsePendingReward(int rowId, JSONObject js, String createAt) {
        try {
            PendingReward pr = new PendingReward();
            pr.rowId = rowId;
            pr.dungeonId = (String) js.get("dungeon");
            pr.roundToken = js.containsKey("md5") ? js.get("md5").toString() : (String) js.get("round");
            pr.rank = js.containsKey("rank") ? ((Number) js.get("rank")).intValue() : 0;
            pr.damage = js.containsKey("dame") ? ((Number) js.get("dame")).longValue() : 0L;
            pr.time = js.containsKey("time") ? js.get("time").toString() : (createAt != null ? createAt : "");
            pr.gifts = new ArrayList<>();

            JSONArray arr = (JSONArray) js.get("gifts");
            if (arr != null) {
                for (Object o : arr) {
                    if (o instanceof JSONArray) {
                        JSONArray item = (JSONArray) o;
                        GiftBox g = parseGiftBoxFromArray(item);
                        if (g != null) pr.gifts.add(g);
                    } else if (o instanceof JSONObject) {
                        JSONObject obj = (JSONObject) o;
                        GiftBox g = parseGiftBoxFromObject(obj);
                        if (g != null) pr.gifts.add(g);
                    }
                }
            }
            return pr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse GiftBox từ JSONArray (hỗ trợ cả định dạng mới [id, type, num, icon, color, name] và định dạng cũ [id, type, name, icon, num, color])
     */
    private static GiftBox parseGiftBoxFromArray(JSONArray item) {
        if (item == null || item.isEmpty()) return null;
        try {
            GiftBox g = new GiftBox();
            int sz = item.size();
            if (sz >= 3) {
                g.id = ((Number) item.get(0)).shortValue();
                g.type = ((Number) item.get(1)).byteValue();
                
                // Kiểm tra xem vị trí thứ 2 là String (định dạng cũ: [id, type, name, icon, num, color])
                // hay là Number (định dạng mới: [id, type, num, icon, color, name])
                if (sz >= 6 && item.get(2) instanceof String) {
                    // Cũ: [id, type, name, icon, num, color]
                    g.name = item.get(2).toString();
                    g.icon = ((Number) item.get(3)).shortValue();
                    g.num = ((Number) item.get(4)).intValue();
                    g.color = ((Number) item.get(5)).byteValue();
                } else {
                    // Mới: [id, type, num, icon, color, name]
                    g.num = ((Number) item.get(2)).intValue();
                    g.icon = (sz > 3 && item.get(3) instanceof Number) ? ((Number) item.get(3)).shortValue() : 0;
                    g.color = (sz > 4 && item.get(4) instanceof Number) ? ((Number) item.get(4)).byteValue() : 0;
                    g.name = (sz > 5 && item.get(5) != null) ? item.get(5).toString() : null;
                }

                // Tự động bổ sung tên / icon từ Template nếu còn trống
                populateGiftMetadata(g);
                return g;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static GiftBox parseGiftBoxFromObject(JSONObject obj) {
        if (obj == null) return null;
        try {
            GiftBox g = new GiftBox();
            g.id = obj.containsKey("id") ? ((Number) obj.get("id")).shortValue() : 0;
            g.type = obj.containsKey("type") ? ((Number) obj.get("type")).byteValue() : 4;
            g.num = obj.containsKey("num") ? ((Number) obj.get("num")).intValue() : 1;
            g.icon = obj.containsKey("icon") ? ((Number) obj.get("icon")).shortValue() : 0;
            g.color = obj.containsKey("color") ? ((Number) obj.get("color")).byteValue() : 0;
            g.name = obj.containsKey("name") ? obj.get("name").toString() : null;
            populateGiftMetadata(g);
            return g;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tự động điền name, icon, color từ ItemTemplate nếu thiếu
     */
    private static void populateGiftMetadata(GiftBox g) {
        if (g == null) return;
        if (g.name == null || g.name.trim().isEmpty()) {
            switch (g.type) {
                case 3: {
                    ItemTemplate3 it3 = ItemTemplate3.get_it_by_id(g.id);
                    if (it3 != null) {
                        g.name = it3.name;
                        g.icon = it3.icon;
                        g.color = it3.color;
                    } else {
                        g.name = "Trang bị " + g.id;
                    }
                    break;
                }
                case 4: {
                    ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(g.id);
                    if (it4 != null) {
                        g.name = it4.name;
                        g.icon = it4.icon;
                    } else {
                        g.name = "Vật phẩm " + g.id;
                    }
                    break;
                }
                case 7: {
                    ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(g.id);
                    if (it7 != null) {
                        g.name = it7.name;
                        g.icon = it7.icon;
                    } else {
                        g.name = "Đá khảm " + g.id;
                    }
                    break;
                }
                default: {
                    ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(g.id);
                    if (it4 != null) {
                        g.name = it4.name;
                        g.icon = it4.icon;
                    } else {
                        g.name = "Phần thưởng " + g.id;
                    }
                    break;
                }
            }
        }
    }

    /**
     * Đánh dấu đã nhận quà (claimed=true) theo rowId.
     */
    @SuppressWarnings("unchecked")
    public static void markClaimed(int rowId) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `data` FROM `historys` WHERE `id` = ?"
             )) {
            ps.setInt(1, rowId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String data = rs.getString("data");
                    Object parsed = JSONValue.parse(data);
                    if (parsed instanceof JSONObject) {
                        JSONObject js = (JSONObject) parsed;
                        js.put("claimed", true);
                        js.put("claimed_at", DATE_FORMAT.format(new java.util.Date()));
                        try (PreparedStatement ps2 = conn.prepareStatement(
                            "UPDATE `historys` SET `data` = ? WHERE `id` = ?"
                        )) {
                            ps2.setString(1, js.toJSONString());
                            ps2.setInt(2, rowId);
                            ps2.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tra cứu quà theo mã MD5 token.
     */
    public static PendingReward findRewardByMd5(String md5Token) {
        if (md5Token == null || md5Token.trim().isEmpty()) return null;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `data`, `create_at` FROM `historys` WHERE `type` = ? AND `data` LIKE ? LIMIT 1"
             )) {
            ps.setString(1, TYPE);
            ps.setString(2, "%" + md5Token.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int rowId = rs.getInt("id");
                    String data = rs.getString("data");
                    String createAt = rs.getString("create_at");
                    Object parsed = JSONValue.parse(data);
                    if (parsed instanceof JSONObject) {
                        return parsePendingReward(rowId, (JSONObject) parsed, createAt);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Kiểm tra xem player đã có bản ghi quà cho key (dungeonId|roundToken) chưa.
     */
    private static boolean hasReward(int playerId, String key) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT COUNT(*) FROM `historys` WHERE `player_id` = ? AND `type` = ? AND `data` LIKE ?"
             )) {
            ps.setInt(1, playerId);
            ps.setString(2, TYPE);
            String[] parts = key.split("\\|", 2);
            String likeStr = "%" + (parts.length > 0 ? parts[0] : key) + "%";
            if (parts.length > 1) likeStr = "%" + parts[1] + "%";
            ps.setString(3, likeStr);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy playerId từ tên player trong DB.
     */
    public static int[] getPlayerAndAccountId(String playerName) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT id, account_id FROM players WHERE name = ?"
             )) {
            ps.setString(1, playerName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new int[]{rs.getInt("id"), rs.getInt("account_id")};
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{0, 0};
    }

    /** DTO chứa thông tin quà pending */
    public static class PendingReward {
        public int rowId;
        public String dungeonId;
        public String roundToken;
        public int rank;
        public long damage;
        public String time;
        public List<GiftBox> gifts;

        public String getDisplayName() {
            String name = dungeonDisplayName(dungeonId) + " - Top " + (rank + 1);
            if (damage > 0) {
                name += " (" + ZUtil.number_format(damage) + " st)";
            }
            return name;
        }

        public String getGiftSummary() {
            StringBuilder sb = new StringBuilder();
            if (gifts != null) {
                for (GiftBox g : gifts) {
                    sb.append(g.name).append(" x").append(ZUtil.number_format(g.num)).append(", ");
                }
            }
            if (sb.length() > 2) sb.setLength(sb.length() - 2);
            return sb.toString();
        }

        public String getShortMd5() {
            if (roundToken != null && roundToken.length() >= 8) {
                return roundToken.substring(0, 8);
            }
            return roundToken != null ? roundToken : "";
        }

        public static String dungeonDisplayName(String id) {
            if (id == null) return "Phó Bản";
            String up = id.toUpperCase();
            if (up.contains("DAU_TRUONG")) return "Đấu Trường Tự Do";
            if (up.contains("TRAN_CHIEN_KHONG_LO") || up.contains("KHONG_LO")) return "Trận Chiến Khổng Lồ";
            if (up.contains("WORLD_WAR") || up.contains("TRAN_CHIEN_LON")) return "Đại Chiến Thế Giới";
            if (up.contains("BOSS_THE_GIOI")) return "Boss Thế Giới";
            if (up.contains("BAO_VE_PHAO_DAI") || up.contains("PHAO_DAI")) return "Bảo Vệ Pháo Đài";
            if (up.contains("CHIEM_DAO")) return "Chiếm Đảo";
            if (up.contains("PVP_BANG")) return "PVP Băng";
            if (up.contains("SUPER_BOSS") || up.contains("SIEU_TRUM")) return "Siêu Trùm";
            if (up.contains("THU_LINH_BIEN_KHOI") || up.contains("THU_LINH")) return "Thủ Lĩnh Biển Khơi";
            return id;
        }
    }
}
