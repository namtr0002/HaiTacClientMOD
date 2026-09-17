package core;

import database.DbManager;
import model.Player;
import network.Service;
import template.GiftBox;
import template.InfoMemList;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopGift — Quản lý phần thưởng đua Top Hàng Tuần (Wanted & PVP) và Top Phó Bản.
 * Hoàn toàn lưu trữ qua bảng `historys` (TYPE = 'TOP_REWARD'), có cờ `claimed` đánh dấu.
 */
public class TopGift {

    public static final String TYPE_TOP_REWARD = "TOP_REWARD";

    public static class TopGiftEntry {
        public int id;
        public String player_name;
        public int gift_type; // 1 = Wanted (Tuần), 2 = PVP (Tuần)
        public int rank;      // 1-based rank (1 đến 10)
        public int cycle;     // 1 = Weekly
        public long created_at;
        public long expire_at;
        public boolean claimed;
    }

    public static void initGift() {
        calculateWeeklyGifts();
    }

    public static void getGift(Player p) throws IOException {
        openReceiveMenu(p, (short) 0);
    }

    // ======================================================
    //  QUERIES & CHECKS (Từ bảng historys)
    // ======================================================

    /**
     * Lấy danh sách phần thưởng Top tuần chưa nhận và chưa hết hạn của Player từ bảng `historys`.
     */
    public static List<TopGiftEntry> getActiveGifts(String playerName) {
        List<TopGiftEntry> list = new ArrayList<>();
        if (playerName == null || playerName.isBlank()) return list;
        long now = System.currentTimeMillis();
        List<Integer> staleIds = new ArrayList<>();
        model.Player onlineP = map.Zone.get_player_by_name_allmap(playerName);
        String pDateStr = (onlineP != null && onlineP.date != null) ? onlineP.date.toString() : "";
        if (pDateStr.isEmpty()) {
            int[] pInfo = historys.DungeonRewardHistory.getPlayerAndAccountId(playerName);
            if (pInfo[0] > 0) pDateStr = historys.HistoryManager.getPlayerCreatedAt(pInfo[0]);
        }

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `data`, `create_at` FROM `historys` WHERE `type` = ? ORDER BY `id` DESC LIMIT 200")) {
            ps.setString(1, TYPE_TOP_REWARD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int rowId = rs.getInt("id");
                    String dataStr = rs.getString("data");
                    java.sql.Timestamp cAt = rs.getTimestamp("create_at");
                    if (dataStr == null || dataStr.isEmpty()) continue;

                    Object parsed = JSONValue.parse(dataStr);
                    if (parsed instanceof JSONObject) {
                        JSONObject js = (JSONObject) parsed;
                        String pName = (String) js.get("player_name");
                        if (pName != null && pName.equalsIgnoreCase(playerName)) {
                            if (onlineP != null) {
                                if (!historys.HistoryManager.validatePlayerRecord(onlineP, dataStr, cAt)) {
                                    staleIds.add(rowId);
                                    continue;
                                }
                            } else if (pDateStr != null && !pDateStr.isEmpty() && cAt != null) {
                                try {
                                    org.joda.time.DateTime pdt = org.joda.time.DateTime.parse(pDateStr);
                                    if (cAt.getTime() < (pdt.getMillis() - 5000L)) {
                                        staleIds.add(rowId);
                                        continue;
                                    }
                                } catch (Exception ignored) {}
                            }

                            boolean claimed = Boolean.TRUE.equals(js.get("claimed"));
                            long expireAt = js.containsKey("expire_at") ? ((Number) js.get("expire_at")).longValue() : 0L;
                            if (!claimed && (expireAt == 0 || expireAt > now)) {
                                TopGiftEntry entry = new TopGiftEntry();
                                entry.id = rowId;
                                entry.player_name = pName;
                                entry.gift_type = js.containsKey("gift_type") ? ((Number) js.get("gift_type")).intValue() : 1;
                                entry.rank = js.containsKey("rank") ? ((Number) js.get("rank")).intValue() : 1;
                                entry.cycle = js.containsKey("cycle") ? ((Number) js.get("cycle")).intValue() : 1;
                                entry.created_at = js.containsKey("created_at") ? ((Number) js.get("created_at")).longValue() : 0L;
                                entry.expire_at = expireAt;
                                entry.claimed = claimed;
                                list.add(entry);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!staleIds.isEmpty()) {
            try (Connection conn = DbManager.gI().getConnect();
                 PreparedStatement psDel = conn.prepareStatement("DELETE FROM `historys` WHERE `id` = ?")) {
                for (int id : staleIds) {
                    psDel.setInt(1, id);
                    psDel.addBatch();
                }
                psDel.executeBatch();
            } catch (Exception ignored) {}
        }

        return list;
    }

    /**
     * Kiểm tra xem Player có phần thưởng Top (tuần hoặc phó bản) nào chưa nhận hay không.
     */
    public static boolean hasActiveGifts(String playerName) {
        List<TopGiftEntry> activeGifts = getActiveGifts(playerName);
        if (!activeGifts.isEmpty()) return true;

        int[] pInfo = historys.DungeonRewardHistory.getPlayerAndAccountId(playerName);
        if (pInfo[0] > 0) {
            List<historys.DungeonRewardHistory.PendingReward> dungeonGifts =
                historys.DungeonRewardHistory.getPendingRewards(pInfo[0]);
            return dungeonGifts != null && !dungeonGifts.isEmpty();
        }
        return false;
    }

    /**
     * Cập nhật claimed = true trong bảng `historys` cho entry rowId.
     */
    public static boolean claimGift(int rowId) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("SELECT `data` FROM `historys` WHERE `id` = ? AND `type` = ?")) {
            ps.setInt(1, rowId);
            ps.setString(2, TYPE_TOP_REWARD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dataStr = rs.getString("data");
                    Object parsed = JSONValue.parse(dataStr);
                    if (parsed instanceof JSONObject) {
                        JSONObject js = (JSONObject) parsed;
                        boolean claimed = Boolean.TRUE.equals(js.get("claimed"));
                        if (claimed) return false; // Đã nhận rồi
                        js.put("claimed", true);

                        try (PreparedStatement psUp = conn.prepareStatement("UPDATE `historys` SET `data` = ? WHERE `id` = ?")) {
                            psUp.setString(1, js.toJSONString());
                            psUp.setInt(2, rowId);
                            return psUp.executeUpdate() > 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================================================
    //  SCHEDULERS (Tính toán và lưu phần thưởng vào historys)
    // ======================================================

    /**
     * Tính toán Top Hàng Tuần (Wanted & PVP): Chạy mỗi thứ 2 lúc 00:00:00.
     * Lưu trữ toàn bộ vào bảng `historys` với type = 'TOP_REWARD'.
     */
    public static void calculateWeeklyGifts() {
        long now = System.currentTimeMillis();
        // Thời gian hết hạn: Thứ 2 tuần sau 00:00
        org.joda.time.DateTime nextMonday = org.joda.time.DateTime.now().plusWeeks(1).withDayOfWeek(1).withTimeAtStartOfDay();
        long expireAt = nextMonday.getMillis();

        // 1. Lưu top 10 Truy Nã (Wanted Point)
        int rank1 = 1;
        for (InfoMemList mem : rank.TopWanted.gI().getCache()) {
            addTopGiftEntry(mem.name, 1, rank1++, 1, now, expireAt);
            if (rank1 > 10) break;
        }

        // 2. Lưu top 10 PvP (PVP Point)
        int rank2 = 1;
        for (InfoMemList mem : rank.TopPVP.gI().getCache()) {
            addTopGiftEntry(mem.name, 2, rank2++, 1, now, expireAt);
            if (rank2 > 10) break;
        }

        // 3. Reset wanted_point & pvppoint = 0 in inventory
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("SELECT `id`, `inventory` FROM `players`");
             ResultSet rs = ps.executeQuery()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psUp = conn.prepareStatement("UPDATE `players` SET `inventory` = ? WHERE `id` = ?")) {
                int count = 0;
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String invStr = rs.getString("inventory");
                    if (invStr != null && !invStr.isEmpty()) {
                        try {
                            JSONObject inv = (JSONObject) JSONValue.parse(invStr);
                            if (inv != null) {
                                boolean changed = false;
                                if (inv.containsKey("wanted_point") && Integer.parseInt(inv.get("wanted_point").toString()) != 0) {
                                    inv.put("wanted_point", 0);
                                    changed = true;
                                }
                                if (inv.containsKey("pvppoint") && Integer.parseInt(inv.get("pvppoint").toString()) != 0) {
                                    inv.put("pvppoint", 0);
                                    changed = true;
                                }
                                if (changed) {
                                    psUp.setString(1, inv.toJSONString());
                                    psUp.setInt(2, id);
                                    psUp.addBatch();
                                    if (++count % 500 == 0) psUp.executeBatch();
                                }
                            }
                        } catch (Exception ignored) {}
                    }
                }
                psUp.executeBatch();
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addTopGiftEntry(String name, int giftType, int rank, int cycle, long createdAt, long expireAt) {
        int[] pInfo = historys.DungeonRewardHistory.getPlayerAndAccountId(name);
        int playerId = pInfo[0];
        int accountId = pInfo[1];

        String pDate = historys.HistoryManager.getPlayerCreatedAt(playerId);
        String aDate = historys.HistoryManager.getAccountCreatedAt(accountId);
        String user = historys.HistoryManager.getUsernameByAccountId(accountId);
        String pMd5 = historys.HistoryManager.getPlayerMd5(aDate, user, accountId, pDate, name != null ? name : "", playerId);

        JSONObject js = new JSONObject();
        js.put("player_name", name);
        js.put("p_md5", pMd5);
        js.put("p_date", pDate);
        js.put("a_date", aDate);
        js.put("user", user);
        js.put("gift_type", giftType);
        js.put("rank", rank);
        js.put("cycle", cycle);
        js.put("created_at", createdAt);
        js.put("expire_at", expireAt);
        js.put("claimed", true);

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, accountId);
            ps.setInt(2, playerId);
            ps.setString(3, TYPE_TOP_REWARD);
            ps.setString(4, js.toJSONString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tự động chuyển quà Top vào Hộp Thư người chơi
        List<GiftBox> rewards = getGiftList(null, giftType, rank);
        String typeStr = (giftType == 1) ? "Truy nã" : "PVP";
        String title = "Quà Top " + rank + " " + typeStr + " (Tuần)";
        String content = "Chúc mừng bạn đã đạt Top " + rank + " Bảng Xếp Hạng " + typeStr + " Tuần! Hãy nhận quà đính kèm bên dưới.";
        MailService.sendMailOffline(playerId, accountId, name, "Đua Top Tuần", title, content, MailService.MAIL_TYPE_GIFT, false, rewards, 0L);
    }

    // ======================================================
    //  REWARDS INTERACTION (NPC & Menus — dùng getService())
    // ======================================================

    /**
     * Mở menu hiển thị danh sách quà Top và quà Phó bản của player tại NPC / Hệ thống.
     */
    public static void openReceiveMenu(Player p, short npcId) throws IOException {
        List<TopGiftEntry> activeGifts = getActiveGifts(p.name);
        List<historys.DungeonRewardHistory.PendingReward> dungeonGifts =
            historys.DungeonRewardHistory.getPendingRewards(p.IDPlayer);

        int totalCount = activeGifts.size() + dungeonGifts.size();
        if (totalCount == 0) {
            p.getService().send_box_ThongBao_OK("Bạn không có phần thưởng Top hoặc Phó bản nào chưa nhận.");
            return;
        }

        String[] menuNames = new String[totalCount + 1];
        short[] menuIcons = new short[totalCount + 1];

        int idx = 0;
        for (int i = 0; i < activeGifts.size(); i++) {
            TopGiftEntry entry = activeGifts.get(i);
            String cycleStr = "Tuần";
            String typeStr = entry.gift_type == 1 ? "Truy nã" : "PVP";
            menuNames[idx] = "Top " + entry.rank + " " + typeStr + " (Hàng " + cycleStr + ")";
            menuIcons[idx] = 101;
            idx++;
        }

        for (int i = 0; i < dungeonGifts.size(); i++) {
            historys.DungeonRewardHistory.PendingReward pr = dungeonGifts.get(i);
            menuNames[idx] = "" + pr.getDisplayName() + " (" + pr.getGiftSummary() + ")";
            menuIcons[idx] = 110;
            idx++;
        }

        menuNames[totalCount] = "Đóng";
        menuIcons[totalCount] = 134;

        // Gửi Menu động với ID 9899 qua getService()
        p.getService().openDynamicMenu(9899, "Nhận Quà Top & Phó Bản (" + totalCount + " phần quà)", menuNames, menuIcons);
    }

    /**
     * Xử lý khi player chọn nhận quà từ danh sách.
     */
    public static void handleReceiveMenu(Player p, int index) throws IOException {
        List<TopGiftEntry> activeGifts = getActiveGifts(p.name);
        List<historys.DungeonRewardHistory.PendingReward> dungeonGifts =
            historys.DungeonRewardHistory.getPendingRewards(p.IDPlayer);

        int totalCount = activeGifts.size() + dungeonGifts.size();
        if (index < 0 || index >= totalCount) {
            // Nút đóng hoặc out of bounds
            return;
        }

        if (index < activeGifts.size()) {
            TopGiftEntry entry = activeGifts.get(index);
            if (claimGift(entry.id)) {
                List<GiftBox> listGift = getGiftList(p, entry.gift_type, entry.rank);
                if (!listGift.isEmpty()) {
                    Service.send_gift(p, 0, "Phần thưởng Top", "Phần thưởng", listGift, true);
                    p.getService().send_box_ThongBao_OK("Nhận thành công! Phần thưởng đã được chuyển vào hành trang/hộp thư.");
                } else {
                    p.getService().send_box_ThongBao_OK("Nhận thành công phần thưởng Top!");
                }
            } else {
                p.getService().send_box_ThongBao_OK("Phần thưởng này đã được nhận từ trước hoặc đã hết hạn.");
            }
        } else {
            int dIndex = index - activeGifts.size();
            historys.DungeonRewardHistory.PendingReward pr = dungeonGifts.get(dIndex);
            historys.DungeonRewardHistory.markClaimed(pr.rowId);
            if (pr.gifts != null && !pr.gifts.isEmpty()) {
                Service.send_gift(p, 1, "Quà " + pr.getDisplayName(), "Phần thưởng phó bản", pr.gifts, true);
                p.updateMoney();
                p.getService().send_box_ThongBao_OK("Nhận thành công " + pr.getDisplayName() + "!\n" + pr.getGiftSummary());
            } else {
                p.getService().send_box_ThongBao_OK("Quà đã nhận hoặc không có quà.");
            }
        }
    }

    public static List<GiftBox> getGiftList(Player p, int giftType, int rankVal) {
        zabstracts.AbsRanked rankObj = null;
        if (giftType == 1) {
            rankObj = rank.TopWanted.gI();
        } else if (giftType == 2) {
            rankObj = rank.TopPVP.gI();
        }

        if (rankObj != null) {
            return rankObj.getRewards(p, rankVal - 1);
        }
        return Collections.emptyList();
    }
}
