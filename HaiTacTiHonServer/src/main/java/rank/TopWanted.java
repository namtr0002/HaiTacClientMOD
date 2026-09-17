package rank;

import model.Player;
import zabstracts.AbsRanked;
import map.Zone;
import network.Message;
import template.InfoMemList;
import template.GiftBox;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * TopWanted — Bảng xếp hạng Truy nã (top wanted_point).
 *
 * TYPE = 9.
 * Format packet type-9 đặc biệt:
 *   head, hair, hat, body/leg/weapon parts, rank index, wanted_point.
 */
public class TopWanted extends AbsRanked {

    private static TopWanted instance;

    public static TopWanted gI() {
        if (instance == null) instance = new TopWanted();
        return instance;
    }

    public static final List<InfoMemList> CACHE = new ArrayList<>();
    public static final int TYPE      = 9;
    public static final int MAX_ITEMS = 50;

    private TopWanted() {
        super("Truy nã", TYPE, MAX_ITEMS);
        this.key = "TOP_WANTED";
    }

    /**
     * Lấy top 50 player sort theo wanted_point giảm dần.
     * Hỗ trợ COALESCE cả key mới 'wanted_point' lẫn key cũ 'wanted' trong inventory JSON.
     */
    @Override
    protected boolean shouldSort() {
        return true;
    }

    @Override
    public String getSql() {
        return "SELECT `id`, `name`, `clazz`, `body_parts`, `it_body`, `fashion`, `inventory` " +
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
                if (inv.containsKey("wanted_point")) {
                    score = Long.parseLong(inv.get("wanted_point").toString());
                }
                if (score <= 0 && inv.containsKey("point_inven")) {
                    Object pi = inv.get("point_inven");
                    if (pi instanceof JSONObject) {
                        JSONObject piObj = (JSONObject) pi;
                        if (piObj.containsKey("wanted")) {
                            score = Long.parseLong(piObj.get("wanted").toString());
                        }
                    } else if (pi instanceof JSONArray) {
                        JSONArray a = (JSONArray) pi;
                        if (a.size() > 11) {
                            score = Long.parseLong(a.get(11).toString());
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
        short[] app      = readFullAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.body = app[3]; e.leg = app[4]; e.weapon = app[5];
        e.info           = "Tiền truy nã: " + core.ZUtil.number_format(e.thongthao);
        return e;
    }

    @Override
    public List<InfoMemList> getCache() { return CACHE; }

    @Override
    public void show(Player p, int page) throws IOException {
        if (p == null) return;
        List<InfoMemList> cache = getCache();
        if (cache.isEmpty()) {
            Ranked.sendEmptyRank(p, TYPE, title, page);
            return;
        }

        if (page < 0) page = 0;

        int bound1 = 0, bound2 = 0;
        if (cache.size() > 10) {
            if ((page + 1) * 10 > cache.size()) {
                bound1 = 10 * page;
                bound2 = cache.size();
                while (bound1 >= bound2 && page > 0) {
                    bound1 -= 10;
                    page--;
                }
            } else {
                bound1 = 10 * page;
                bound2 = bound1 + 10;
            }
        } else {
            bound1 = 0;
            bound2 = cache.size();
            page = 0;
        }

        Message m = new Message(-30);
        m.writer().writeByte(TYPE);
        m.writer().writeUTF(title);
        m.writer().writeByte(page);
        m.writer().writeByte(bound2 - bound1);

        for (int i = bound1; i < bound2; i++) {
            InfoMemList temp = cache.get(i);

            Player p0 = Zone.get_player_by_name_allmap(temp.name);
            short head = temp.head;
            short hair = temp.hair;
            short hat  = temp.hat;
            short body = temp.body;
            short leg  = temp.leg;
            short weapon = temp.weapon;

            if (p0 != null) {
                head = (short) p0.get_head();
                hair = (short) p0.get_hair();
                hat  = p0.get_wearing_part(1);
                body = p0.get_wearing_part(3);
                if (body < 0) body = p0.part_body;
                leg  = p0.get_wearing_part(5);
                if (leg < 0) leg = p0.part_leg;
                weapon = p0.get_wearing_part(0);
                if (weapon < 0) weapon = p0.part_weapon;
            }

            m.writer().writeInt(temp.id);
            m.writer().writeUTF(temp.name);
            m.writer().writeShort(head);
            m.writer().writeShort(hair);
            m.writer().writeShort(hat);
            m.writer().writeShort(body);
            m.writer().writeShort(leg);
            m.writer().writeShort(weapon);
            m.writer().writeInt(i);                     // rank index
            m.writer().writeInt((int) temp.thongthao);  // wanted_point
        }

        p.addmsg(m);
        m.cleanup();
    }

    @Override
    public java.util.List<template.GiftBox> getRewards(Player p, int rank) {
        return getRewards(rank);
    }

    public java.util.List<template.GiftBox> getRewards(int rank) {
        java.util.List<template.GiftBox> listGift = new ArrayList<>();
        if (rank < 0 || rank >= 10) return listGift; // Chỉ Top 10 mới có quà

        if (rank == 0) { // Top 1 Wanted
            listGift.add(createGift(0, 4, 300_000_000)); // 300M Beri
            listGift.add(createGift(1, 4, 30_000)); // 30.000 Ruby
            listGift.add(createGift(829, 4, 5)); // 5 Rương Cam +15 Cùng Hệ
            listGift.add(createGift(647, 4, 1)); // Đá Thần Thoại 1
            listGift.add(createGift(648, 4, 1)); // Đá Thần Thoại 2
            listGift.add(createGift(866, 4, 50)); // 50 Vé VIP
        } else if (rank == 1 || rank == 2) { // Top 2 - 3 Wanted
            listGift.add(createGift(0, 4, 150_000_000)); // 150M Beri
            listGift.add(createGift(1, 4, 15_000)); // 15.000 Ruby
            listGift.add(createGift(829, 4, 3)); // 3 Rương Cam +15 Cùng Hệ
            listGift.add(createGift(649, 4, 1)); // Đá Thần Thoại
            listGift.add(createGift(866, 4, 30)); // 30 Vé VIP
        } else if (rank < 10) { // Top 4 - 10 Wanted (như nhau)
            listGift.add(createGift(0, 4, 50_000_000)); // 50M Beri
            listGift.add(createGift(1, 4, 5_000)); // 5.000 Ruby
            listGift.add(createGift(106, 4, 5)); // 5 Rương Vàng
            listGift.add(createGift(650 + (rank % 28), 4, 1)); // Đá Thần Thoại
            listGift.add(createGift(866, 4, 10)); // 10 Vé VIP
        }
        return listGift;
    }

    public synchronized boolean distributeAutoRewardsToMail() {
        String seasonKey = core.Manager.gI().getTopWantedSeasonKey();
        String logType = "TOP_DISTRIBUTED_TOP_WANTED_" + seasonKey;

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
                String title = "Quà Đua Top Truy Nã Mùa " + seasonKey + " (Hạng " + (rank + 1) + ")";
                String content = "Chúc mừng bạn đã xuất sắc đạt Hạng " + (rank + 1) + " trong Sự kiện Đua Top Lệnh Truy Nã Mùa " + seasonKey + "! Hệ thống đã tự động gửi toàn bộ phần thưởng vào Hộp Thư của bạn.";
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

        core.Manager.gI().chatKTG(0, "Sự kiện Đua Top Lệnh Truy Nã Mùa " + seasonKey + " đã chính thức kết thúc! Phần thưởng đã được hệ thống tự động gửi vào Hộp Thư của TOP 10!", 5);
        return true;
    }

    public static String getGuideInfo() {
        core.Manager mgr = core.Manager.gI();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        String startStr = sdf.format(new java.util.Date(mgr.getTopWantedStartTime()));
        String endStr = sdf.format(new java.util.Date(mgr.getTopWantedEndTime()));
        boolean ended = mgr.hasTopWantedEnded();

        StringBuilder sb = new StringBuilder();
        sb.append("--- THỂ LỆ & THỜI GIAN ĐUA TOP LỆNH TRUY NÃ (MÙA ").append(mgr.getTopWantedSeasonKey()).append(") ---\n\n");
        sb.append("• Thời gian bắt đầu: ").append(startStr).append("\n");
        sb.append("• Thời gian kết thúc: ").append(endStr).append("\n");
        sb.append("• Trạng thái: ").append(ended ? "ĐÃ KẾT THÚC (Đã phát quà)" : "ĐANG DIỄN RA").append("\n");
        sb.append("• Tiêu chí xếp hạng: Dựa trên tổng Điểm Truy Nã (Wanted Point) tích lũy được.\b");
        sb.append("--- CƠ CẤU PHẦN THƯỞNG TOP 10 LỆNH TRUY NÃ ---\n\n");
        sb.append("TOP 1: 300M Beri, 30.000 Ruby, 10 Rương Báu Đỉnh Phong, 2 Đá Thần Thoại, 50 Vé VIP\n\n");
        sb.append("TOP 2 - 3: 150M Beri, 15.000 Ruby, 5 Rương Báu Đỉnh Phong, 1 Đá Thần Thoại, 30 Vé VIP\n\n");
        sb.append("TOP 4 - 10: 50M Beri, 5.000 Ruby, 5 Rương Vàng, 1 Đá Thần Thoại, 10 Vé VIP\n");
        sb.append("Hạng 11 trở đi không có quà.\b");
        sb.append("--- HÌNH THỨC NHẬN THƯỞNG ---\n\n");
        sb.append("• Khi sự kiện kết thúc, hệ thống sẽ TỰ ĐỘNG GỬI QUÀ VÀO HỘP THƯ của nhân vật đạt giải Top 10.\n");
        sb.append("• Người chơi không cần thao tác nhận thủ công tại NPC.");
        return sb.toString();
    }
}
