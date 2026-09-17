package rank;

import zabstracts.AbsRanked;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * TopCaoThu — Bảng xếp hạng Cao Thủ (top level / exp / thông thảo).
 */
public class TopCaoThu extends AbsRanked {

    private static TopCaoThu instance;

    public static TopCaoThu gI() {
        if (instance == null) instance = new TopCaoThu();
        return instance;
    }

    public static final List<InfoMemList> CACHE = new ArrayList<>();
    public static final int TYPE      = 4;
    public static final int MAX_ITEMS = 50;

    private TopCaoThu() {
        super("Cao Thủ", TYPE, MAX_ITEMS);
        this.key = "TOP_CAO_THU";
    }

    /**
     * Lấy top 50 player sort theo Thông Thảo -> Level -> Exp giảm dần.
     * Hỗ trợ chuẩn xác cả dữ liệu lưu trong JSON level {"lv", "exp", "tt"} lẫn cột exp gốc.
     */
    @Override
    protected boolean shouldSort() {
        return true;
    }

    @Override
    public String getSql() {
        return "SELECT `id`, `name`, `level`, `exp`, `body_parts`, `it_body`, `fashion`, `inventory` " +
               "FROM `players`";
    }

    @Override
    public InfoMemList buildEntry(ResultSet rs) throws Exception {
        String rawLevel = rs.getString("level");
        int lv = 1;
        long exp = 0;
        short tt = 0;
        if (rawLevel != null && !rawLevel.isEmpty()) {
            try {
                JSONObject o = (JSONObject) JSONValue.parse(rawLevel);
                if (o != null) {
                    lv = jShort(o, "lv", (short) 1);
                    exp = jLong(o, "exp", 0L);
                    tt = jShort(o, "tt", (short) 0);
                } else {
                    JSONArray a = (JSONArray) JSONValue.parse(rawLevel);
                    if (a != null) {
                        lv = a.size() > 0 ? Integer.parseInt(a.get(0).toString()) : 1;
                        exp = a.size() > 1 ? Long.parseLong(a.get(1).toString()) : 0L;
                        tt = a.size() > 2 ? Short.parseShort(a.get(2).toString()) : 0;
                    } else {
                        lv = Integer.parseInt(rawLevel);
                    }
                }
            } catch (Exception ignored) {
                try { lv = Integer.parseInt(rawLevel); } catch (Exception ignored2) {}
            }
        }
        try {
            long colExp = rs.getLong("exp");
            if (colExp > exp) exp = colExp;
        } catch (Exception ignored) {}

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = (long) lv * 1_000_000_000_000L + (long) tt * 1_000_000_000L + exp;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = "Cấp: " + lv + (tt > 0 ? " (TT " + tt + ")" : "");
        return e;
    }

    @Override
    public List<InfoMemList> getCache() { return CACHE; }

    public int getRankTierTop10(String playerName) {
        return getRankTier(playerName, 10);
    }

    @Override
    public java.util.List<template.GiftBox> getRewards(int rank) {
        List<template.GiftBox> gifts = new java.util.ArrayList<>();
        if (rank < 0 || rank >= 10) return gifts; // CHỈ TOP 10 MỚI CÓ QUÀ! TOP 11++ KHÔNG CÓ QUÀ

        if (rank == 0) { // TOP 1
            gifts.add(createGift(0, 4, 1_000_000_000)); // 1 Tỷ
            gifts.add(createGift(0, 4, 1_000_000_000)); // 2 Tỷ
            gifts.add(createGift(0, 4, 1_000_000_000)); // 3 Tỷ Beri
            gifts.add(createGift(1, 4, 500_000)); // 500.000 Ruby
            gifts.add(createGift(894, 4, 1)); // 1 Rương Trái Ác Quỷ Tự Chọn Cao Cấp (có Nika 873)
            gifts.add(createGift(469, 4, 1)); // 1 Rương Thời Trang Cao Cấp (Râu Đen, Sabo, Roger)
            gifts.add(createGift(647, 4, 1)); // Đá Thần Thoại 1
            gifts.add(createGift(648, 4, 1)); // Đá Thần Thoại 2
            gifts.add(createGift(649, 4, 1)); // Đá Thần Thoại 3
            gifts.add(createGift(866, 4, 100)); // 100 Vé VIP
        } else if (rank == 1) { // TOP 2
            gifts.add(createGift(0, 4, 1_000_000_000)); // 1 Tỷ
            gifts.add(createGift(0, 4, 500_000_000)); // 1.5 Tỷ Beri
            gifts.add(createGift(1, 4, 250_000)); // 250.000 Ruby
            gifts.add(createGift(911, 4, 1)); // 1 Rương Siêu Đại Ác Quỷ (random ra Trái Cao Cấp, có cơ hội ra Nika/Ánh Sáng)
            gifts.add(createGift(650, 4, 1)); // Đá Thần Thoại 1
            gifts.add(createGift(651, 4, 1)); // Đá Thần Thoại 2
            gifts.add(createGift(866, 4, 50)); // 50 Vé VIP
        } else if (rank == 2) { // TOP 3
            gifts.add(createGift(0, 4, 1_000_000_000)); // 1 Tỷ Beri
            gifts.add(createGift(1, 4, 150_000)); // 150.000 Ruby
            gifts.add(createGift(911, 4, 1)); // 1 Rương Siêu Đại Ác Quỷ
            gifts.add(createGift(652, 4, 1)); // Đá Thần Thoại 1
            gifts.add(createGift(653, 4, 1)); // Đá Thần Thoại 2
            gifts.add(createGift(866, 4, 30)); // 30 Vé VIP
        } else if (rank < 10) { // TOP 4 - 10 (Như nhau)
            gifts.add(createGift(0, 4, 300_000_000)); // 300M Beri
            gifts.add(createGift(1, 4, 50_000)); // 50.000 Ruby
            gifts.add(createGift(158, 4, 1)); // 1 Rương Đại Ác Quỷ
            gifts.add(createGift(654 + (rank % 28), 4, 1)); // 1 Đá Thần Thoại
            gifts.add(createGift(866, 4, 20)); // 20 Vé VIP
        }

        return gifts;
    }

    /**
     * Tự động trao quà Đua Top Cao Thủ vào Hộp Thư (Mail in-game) cho toàn bộ Top 50 khi hết thời gian đua top.
     * Đảm bảo tính Idempotent: Chỉ trao 1 lần duy nhất cho mỗi Season Key.
     */
    public synchronized boolean distributeAutoRewardsToMail() {
        String seasonKey = core.Manager.gI().getTopCaoThuSeasonKey();
        if (seasonKey == null || seasonKey.trim().isEmpty()) {
            seasonKey = "OPEN_MUA_1";
        }
        String logType = "TOP_DISTRIBUTED_TOP_CAO_THU_" + seasonKey;

        if (historys.HistoryManager.hasSystemLog(logType)) {
            return false;
        }

        update();
        List<InfoMemList> list = getCache();
        if (list == null || list.isEmpty()) {
            return false;
        }

        int count = 0;
        int max = Math.min(list.size(), MAX_ITEMS);
        for (int rank = 0; rank < max; rank++) {
            InfoMemList entry = list.get(rank);
            if (entry == null || entry.name == null || entry.name.isEmpty()) continue;
            List<template.GiftBox> gifts = getRewards(rank);
            if (gifts == null || gifts.isEmpty()) continue;

            int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(entry.name);
            if (ids[0] > 0) {
                String title = "Quà Đua Top Cao Thủ Mùa " + seasonKey + " (Hạng " + (rank + 1) + ")";
                String content = "Chúc mừng bạn đã xuất sắc đạt Hạng " + (rank + 1) + " trong Sự kiện Đua Top Cao Thủ Mùa " + seasonKey + "! Hệ thống đã tự động gửi toàn bộ phần thưởng vào Hộp Thư của bạn.";
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

        core.Manager.gI().chatKTG(0, "Sự kiện Đua Top Cao Thủ Mùa " + seasonKey + " đã chính thức kết thúc! Phần thưởng đã được hệ thống tự động gửi vào Hộp Thư của TOP 50!", 5);
        return true;
    }

    public static String getGuideInfo() {
        core.Manager mgr = core.Manager.gI();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        String startStr = sdf.format(new java.util.Date(mgr.getTopCaoThuStartTime()));
        String endStr = sdf.format(new java.util.Date(mgr.getTopCaoThuEndTime()));
        boolean ended = mgr.hasTopCaoThuEnded();

        StringBuilder sb = new StringBuilder();
        sb.append("--- THỂ LỆ & THỜI GIAN ĐUA TOP CAO THỦ (MÙA ").append(mgr.getTopCaoThuSeasonKey()).append(") ---\n\n");
        sb.append("• Thời gian bắt đầu: ").append(startStr).append("\n");
        sb.append("• Thời gian kết thúc: ").append(endStr).append("\n");
        sb.append("• Trạng thái: ").append(ended ? "ĐÃ KẾT THÚC (Đã phát quà)" : "ĐANG DIỄN RA").append("\n");
        sb.append("• Tiêu chí xếp hạng: Sắp xếp theo Thông Thạo -> Cấp Độ (Level) -> Điểm Kinh Nghiệm (EXP) giảm dần.\b");
        sb.append("--- CƠ CẤU PHẦN THƯỞNG TOP CAO THỦ ---\n\n");
        sb.append("TOP 1: 3 Tỷ Beri, 500.000 Ruby, 1 Rương Ác Quỷ Tự Chọn Cao Cấp (có Nika), 1 Rương Thời Trang Cao Cấp, 3 Đá Thần Thoại, 100 Vé VIP\n\n");
        sb.append("TOP 2: 1.5 Tỷ Beri, 250.000 Ruby, 1 Rương Siêu Đại Ác Quỷ, 2 Đá Thần Thoại, 50 Vé VIP\n\n");
        sb.append("TOP 3: 1 Tỷ Beri, 150.000 Ruby, 1 Rương Siêu Đại Ác Quỷ, 2 Đá Thần Thoại, 30 Vé VIP\n\n");
        sb.append("TOP 4 - 10: 300M Beri, 50.000 Ruby, 1 Rương Đại Ác Quỷ, 1 Đá Thần Thoại, 20 Vé VIP\n");
        sb.append("Hạng 11 trở đi không có quà.\b");
        sb.append("--- HÌNH THỨC NHẬN THƯỞNG & LƯU Ý ---\n\n");
        sb.append("• Lưu ý công bằng: Trong 7 ngày đầu mở server, Chợ Mua/Bán và Giao Dịch trực tiếp tạm khóa để tránh gian lận.\n");
        sb.append("• Hình thức nhận quà: Khi sự kiện kết thúc, hệ thống sẽ TỰ ĐỘNG GỬI QUÀ VÀO HỘP THƯ của nhân vật đạt giải, không cần nhận thủ công tại NPC.\n");
        sb.append("• Vui lòng dọn dẹp hòm thư trước khi sự kiện kết thúc để đảm bảo nhận trọn vẹn phần thưởng!");
        return sb.toString();
    }

    private static short jShort(JSONObject o, String key, short def) {
        try { Object v = o.get(key); return v == null ? def : Short.parseShort(v.toString()); } catch (Exception e) { return def; }
    }
    private static long jLong(JSONObject o, String key, long def) {
        try { Object v = o.get(key); return v == null ? def : Long.parseLong(v.toString()); } catch (Exception e) { return def; }
    }
}
