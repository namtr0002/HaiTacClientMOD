package rank;

import model.Player;
import zabstracts.AbsRanked;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.InfoMemList;
import template.GiftBox;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * TopPVP — Bảng xếp hạng Thách Đấu / Top PVP (top pvppoint).
 * TYPE = 8.
 */
public class TopPVP extends AbsRanked {

    private static TopPVP instance;

    public static TopPVP gI() {
        if (instance == null) instance = new TopPVP();
        return instance;
    }

    public static final List<InfoMemList> CACHE = new ArrayList<>();
    public static final int TYPE = 8;
    public static final int MAX_ITEMS = 50;

    private TopPVP() {
        super("Top Thách Đấu", TYPE, MAX_ITEMS);
        this.key = "TOP_PVP";
    }

    @Override
    protected boolean shouldSort() {
        return true;
    }

    @Override
    public String getSql() {
        return "SELECT `id`, `name`, `body_parts`, `it_body`, `fashion`, `inventory` " +
               "FROM `players`";
    }

    @Override
    public InfoMemList buildEntry(ResultSet rs) throws Exception {
        String invStr = rs.getString("inventory");
        if (invStr == null || invStr.isEmpty()) return null;
        long score = 0;
        try {
            JSONObject inv = (JSONObject) JSONValue.parse(invStr);
            if (inv != null) {
                if (inv.containsKey("pvppoint")) {
                    score = Long.parseLong(inv.get("pvppoint").toString());
                }
                if (score <= 0 && inv.containsKey("point_inven")) {
                    Object pi = inv.get("point_inven");
                    if (pi instanceof JSONObject) {
                        JSONObject piObj = (JSONObject) pi;
                        if (piObj.containsKey("pvpW")) {
                            score = Long.parseLong(piObj.get("pvpW").toString());
                        }
                    } else if (pi instanceof JSONArray) {
                        JSONArray a = (JSONArray) pi;
                        if (a.size() > 5) {
                            score = Long.parseLong(a.get(5).toString());
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = "Điểm PVP: " + core.ZUtil.number_format(e.thongthao);
        return e;
    }

    @Override
    public List<InfoMemList> getCache() { return CACHE; }

    // ======================================================
    //  REWARD — phần thưởng theo rank
    // ======================================================

    /**
     * Phần thưởng PVP:
     *   Top 1  : 2000 Ruby + 5M Beri + 5 Rương theo level
     *   Top 2-9: 500 Ruby  + 1 Rương theo level
     *   Ngoài top 10: không có quà.
     */
    @Override
    public List<GiftBox> getRewards(Player p, int rank) {
        return getRewards(rank);
    }

    public List<GiftBox> getRewards(int rank) {
        List<GiftBox> listGift = new ArrayList<>();
        if (rank < 0 || rank >= 10) return listGift; // Chỉ Top 10 mới có quà

        if (rank == 0) { // Top 1 PVP
            listGift.add(createGift(0, 4, 300_000_000)); // 300M Beri
            listGift.add(createGift(1, 4, 30_000)); // 30.000 Ruby
            listGift.add(createGift(810, 4, 1)); // 1 Rương Thú Cưng / Pet Hiếm
            listGift.add(createGift(647, 4, 1)); // Đá Thần Thoại 1
            listGift.add(createGift(648, 4, 1)); // Đá Thần Thoại 2
            listGift.add(createGift(866, 4, 50)); // 50 Vé VIP
        } else if (rank == 1 || rank == 2) { // Top 2 - 3 PVP
            listGift.add(createGift(0, 4, 150_000_000)); // 150M Beri
            listGift.add(createGift(1, 4, 15_000)); // 15.000 Ruby
            listGift.add(createGift(810, 4, 1)); // 1 Rương Thú Cưng / Pet Hiếm
            listGift.add(createGift(649, 4, 1)); // Đá Thần Thoại
            listGift.add(createGift(866, 4, 30)); // 30 Vé VIP
        } else if (rank < 10) { // Top 4 - 10 PVP (như nhau)
            listGift.add(createGift(0, 4, 50_000_000)); // 50M Beri
            listGift.add(createGift(1, 4, 5_000)); // 5.000 Ruby
            listGift.add(createGift(650 + (rank % 28), 4, 1)); // Đá Thần Thoại
            listGift.add(createGift(866, 4, 10)); // 10 Vé VIP
        }
        return listGift;
    }

    public synchronized boolean distributeAutoRewardsToMail() {
        String seasonKey = core.Manager.gI().getTopPvpSeasonKey();
        String logType = "TOP_DISTRIBUTED_TOP_PVP_" + seasonKey;

        if (historys.HistoryManager.hasSystemLog(logType)) {
            return false;
        }

        update();
        List<InfoMemList> list = getCache();
        if (list == null || list.isEmpty()) {
            return false;
        }

        int count = 0;
        int max = Math.min(list.size(), 10);
        for (int rank = 0; rank < max; rank++) {
            InfoMemList entry = list.get(rank);
            if (entry == null || entry.name == null || entry.name.isEmpty()) continue;
            List<template.GiftBox> gifts = getRewards(rank);
            if (gifts == null || gifts.isEmpty()) continue;

            int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(entry.name);
            if (ids[0] > 0) {
                String title = "Quà Đua Top PvP Mùa " + seasonKey + " (Hạng " + (rank + 1) + ")";
                String content = "Chúc mừng bạn đã xuất sắc đạt Hạng " + (rank + 1) + " trong Sự kiện Đua Top PvP Thách Đấu Mùa " + seasonKey + "! Hệ thống đã tự động gửi toàn bộ phần thưởng vào Hộp Thư của bạn.";
                core.MailService.sendMailOffline(ids[0], ids[1], entry.name, "Hệ Thống", title, content, core.MailService.MAIL_TYPE_GIFT, false, gifts, 0L);
                count++;
            }
        }

        JSONObject logData = new JSONObject();
        logData.put("season", seasonKey);
        logData.put("rewarded_count", count);
        logData.put("time", System.currentTimeMillis());
        logData.put("date", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        historys.HistoryManager.saveSystemLog(logType, logData.toJSONString());

        core.Manager.gI().chatKTG(0, "Sự kiện Đua Top PvP Thách Đấu Mùa " + seasonKey + " đã chính thức kết thúc! Phần thưởng đã được hệ thống tự động gửi vào Hộp Thư của TOP 10!", 5);
        return true;
    }

    public static String getGuideInfo() {
        core.Manager mgr = core.Manager.gI();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        String startStr = sdf.format(new java.util.Date(mgr.getTopPvpStartTime()));
        String endStr = sdf.format(new java.util.Date(mgr.getTopPvpEndTime()));
        boolean ended = mgr.hasTopPvpEnded();

        StringBuilder sb = new StringBuilder();
        sb.append("--- THỂ LỆ & THỜI GIAN ĐUA TOP THÁCH ĐẤU PVP (MÙA ").append(mgr.getTopPvpSeasonKey()).append(") ---\n\n");
        sb.append("• Thời gian bắt đầu: ").append(startStr).append("\n");
        sb.append("• Thời gian kết thúc: ").append(endStr).append("\n");
        sb.append("• Trạng thái: ").append(ended ? "ĐÃ KẾT THÚC (Đã phát quà)" : "ĐANG DIỄN RA").append("\n");
        sb.append("• Tiêu chí xếp hạng: Dựa trên Điểm Thách Đấu PvP tích lũy khi tham gia khiêu chiến người chơi khác.\b");
        sb.append("--- CƠ CẤU PHẦN THƯỞNG TOP 10 THÁCH ĐẤU PVP ---\n\n");
        sb.append("TOP 1: 300M Beri, 30.000 Ruby, 1 Rương Thú Cưng Pet Hiếm, 2 Đá Thần Thoại, 50 Vé VIP\n\n");
        sb.append("TOP 2 - 3: 150M Beri, 15.000 Ruby, 1 Rương Thú Cưng Pet Hiếm, 1 Đá Thần Thoại, 30 Vé VIP\n\n");
        sb.append("TOP 4 - 10: 50M Beri, 5.000 Ruby, 1 Đá Thần Thoại, 10 Vé VIP\n");
        sb.append("Hạng 11 trở đi không có quà.\b");
        sb.append("--- HÌNH THỨC NHẬN THƯỞNG ---\n\n");
        sb.append("• Khi sự kiện kết thúc, hệ thống sẽ TỰ ĐỘNG GỬI QUÀ VÀO HỘP THƯ của nhân vật đạt giải Top 10.\n");
        sb.append("• Người chơi không cần thao tác nhận thủ công tại NPC.");
        return sb.toString();
    }

    private static short jShort(JSONObject o, String key, short def) {
        try { Object v = o.get(key); return v == null ? def : Short.parseShort(v.toString()); } catch (Exception e) { return def; }
    }
}
