package map.zones;

import model.Player;
import itemz.Item;
import core.Manager;
import network.Service;
import core.ZUtil;
import network.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.*;
import map.Zone;
import zabstracts.AbsDungeon;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Free Arena PvP Dungeon.
 * Refactored to map.zones and extends AbsDungeon.
 */
public class DauTruongTuDo extends AbsDungeon {

    private static java.util.concurrent.ConcurrentHashMap<String, DauTruongTuDoInfo> ENTRY = new java.util.concurrent.ConcurrentHashMap<>();
    public static List<InfoMemList> bxh = new ArrayList<>();
    private static boolean isOpen = false;
    public static long time;
    public static String currentSessionToken = "";

    @Override
    public void create() {
        // Handled via static Open() trigger
    }

    @Override
    public void update(Zone zone) throws IOException {
        // Event runs globally in public maps, does not require private zone ticks
    }

    public static boolean IsOpen() {
        return isOpen || activities.TimedDungeonManager.gI().isDungeonOpen("DAU_TRUONG_TU_DO");
    }

    public static void Open() throws IOException {
        Open(ZUtil.generateMD5Token("DauTruongTuDo_" + System.currentTimeMillis()));
    }

    public static void Open(String sessionToken) throws IOException {
        isOpen = true;
        currentSessionToken = (sessionToken != null && !sessionToken.isEmpty()) ? sessionToken : ZUtil.generateMD5Token("DauTruongTuDo_" + System.currentTimeMillis());
        activities.TimedDungeonManager.ScheduleConfig cfg = activities.TimedDungeonManager.gI().getConfigs().get("DAU_TRUONG_TU_DO");
        int durationMins = (cfg != null && cfg.runDurationMinutes > 0) ? cfg.runDurationMinutes : 15;
        time = System.currentTimeMillis() + 60_000L * durationMins;
        Manager.gI().chatKTG(0, "Thời gian Đấu Trường Tự Do đã chính thức bắt đầu! Hãy tham gia so tài PK!", 5);

        // Tự động điều phối bot vào các map đấu trường 70, 71, 72, 74
        bot.BotDauTruongTuDo.dispatchBots();
    }

    public static void Close() throws IOException {
        if (isOpen) {
            // Dọn sạch bot đấu trường
            bot.BotDauTruongTuDo.clearBots();

            CreateBxh();

            Manager.gI().chatKTG(0, "Thời gian đấu trường tự do kết thúc", 5);
            Message mT = new Message(-73);
            mT.writer().writeByte(0); // time type
            mT.writer().writeShort(-1);
            mT.writer().writeUTF("Thời gian");
            for (int i = 70; i <= 74; i++) {
                if (i != 73) {
                    Zone[] maps = Zone.getMapByID(i);
                    if (maps != null) {
                        for (int j = 0; j < maps.length; j++) {
                            Zone map = maps[j];
                            if (j <= 1) {
                                for (int k = 0; k < map.players.size(); k++) {
                                    Player get = map.players.get(k);
                                    if (get != null) {
                                        get.isBackTypePk = false;
                                        get.type_pk = -1;
                                        map.change_flag(get, -1);
                                        if (get.conn != null) {
                                            get.conn.addmsg(mT);
                                        }
                                        if (get.getService() != null) {
                                            get.getService().update_PK(get, false);
                                            get.getService().send_time_cool_down(0, "", 0);
                                        }
                                        //
                                        List<GiftBox> listGift = new ArrayList<>();
                                        int getTop = getTop(get);
                                        DauTruongTuDoInfo info = GetInfo(get);
                                        if (get.item.total_item_bag_by_id(4, 102) > 0) {
                                            get.item.remove_item47(4, 102, 1);
                                            GiftBox.addGift(listGift, (byte) 4, (short) 30, 5000);
                                        } else {
                                            GiftBox.addGift(listGift, (byte) 4, (short) 30, 1000);
                                        }
                                        if (getTop == 1) {
                                            GiftBox.addGift(listGift, (byte) 4, (short) 1, 200);
                                            GiftBox.addGift(listGift, (byte) 4, (short) 0, 100_000);
                                        } else if (getTop == 2) {
                                            GiftBox.addGift(listGift, (byte) 4, (short) 1, 100);
                                            GiftBox.addGift(listGift, (byte) 4, (short) 0, 70_000);
                                        } else if (getTop == 3) {
                                            GiftBox.addGift(listGift, (byte) 4, (short) 1, 70);
                                            GiftBox.addGift(listGift, (byte) 4, (short) 0, 50_000);
                                        } else if (getTop != -1) {
                                            GiftBox.addGift(listGift, (byte) 4, (short) 1, 50);
                                        }
                                        if (!listGift.isEmpty() && get != null && !get.isBot && get.conn != null) {
                                            String titleNotice = (getTop != -1) ? ("Chúc mừng bạn đạt Top " + getTop + " Đấu Trường Tự Do!") : "Phần thưởng tham gia Đấu Trường Tự Do!";
                                            core.MailService.sendMail(get, "Hệ Thống", "Đấu Trường Tự Do",
                                                    titleNotice + " Phần thưởng đính kèm bên dưới.",
                                                    core.MailService.MAIL_TYPE_GIFT, false, listGift, 14L * 24 * 3600 * 1000);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            mT.cleanup();
            isOpen = false;
            ENTRY.clear();
        }
    }

    public static void SendBxh(Player p) throws IOException {
        rank.TopDauTruongTuDo.gI().show(p, 0);
    }

    public static void CreateBxh() {
        if (isOpen) {
            List<InfoMemList> listAll = new ArrayList<>();
            for (java.util.Map.Entry<String, DauTruongTuDoInfo> en : ENTRY.entrySet()) {
                InfoMemList add = new InfoMemList();
                add.id = Integer.parseInt(en.getKey());
                add.head = (short) en.getValue().win;
                add.hair = (short) en.getValue().lose;
                add.thongthao = en.getValue().point;
                listAll.add(add);
            }
            List<InfoMemList> listMax20 = new ArrayList<>();
            while (!listAll.isEmpty() && listMax20.size() < 20) {
                InfoMemList get = null;
                for (int i = 0; i < listAll.size(); i++) {
                    if (get == null || get.thongthao < listAll.get(i).thongthao) {
                        get = listAll.get(i);
                    }
                }
                listAll.remove(get);
                listMax20.add(get);
            }
            for (int i = 0; i < listMax20.size(); i++) {
                try (Connection connection = database.DbManager.gI().getConnect();
                     PreparedStatement ps = connection.prepareStatement("SELECT `id`, `name`, `clazz`, `level`, `body_parts`, `it_body`, `fashion`, `inventory` FROM `players` where `id` = " + listMax20.get(i).id);
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        JSONObject levelObj = (JSONObject) JSONValue.parse(rs.getString("level"));
                        if (levelObj != null) {
                            listMax20.get(i).level = (short) ((Number) levelObj.get("lv")).intValue();
                        }
                        listMax20.get(i).info = String.format("Lv: %s Giết: %s - Thua: %s", listMax20.get(i).level, listMax20.get(i).head, listMax20.get(i).hair);
                        listMax20.get(i).name = rs.getString("name");
                        short[] app = zabstracts.AbsRanked.readAppearance(rs);
                        listMax20.get(i).head = app[0];
                        listMax20.get(i).hair = app[1];
                        listMax20.get(i).hat = app[2];
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!listMax20.isEmpty()) {
                bxh.clear();
                bxh.addAll(listMax20);
            }
        }
    }

    public static byte getBalancedFlag(Zone zone, Player p) {
        if (zone == null || p == null) return 4;

        // 1. Cùng Clan trong khu -> Cùng cờ (4 hoặc 5)
        if (p.clan != null) {
            for (int i = 0; i < zone.players.size(); i++) {
                Player other = zone.players.get(i);
                if (other != null && other.IDPlayer != p.IDPlayer && p.clan.equals(other.clan)) {
                    if (other.type_pk == 4 || other.type_pk == 5) {
                        return other.type_pk;
                    }
                }
            }
        }

        // 2. Cùng Party trong khu -> Cùng cờ (4 hoặc 5)
        if (p.party != null && p.party.list != null) {
            for (int i = 0; i < zone.players.size(); i++) {
                Player other = zone.players.get(i);
                if (other != null && other.IDPlayer != p.IDPlayer && p.party.list.contains(other)) {
                    if (other.type_pk == 4 || other.type_pk == 5) {
                        return other.type_pk;
                    }
                }
            }
        }

        // 3. Nếu solo hoặc người đầu tiên: Đếm quân số Phe 4 vs Phe 5 trong khu
        int count4 = 0;
        int count5 = 0;
        for (int i = 0; i < zone.players.size(); i++) {
            Player other = zone.players.get(i);
            if (other != null && other.IDPlayer != p.IDPlayer) {
                if (other.type_pk == 4) count4++;
                else if (other.type_pk == 5) count5++;
            }
        }
        return (byte) (count4 <= count5 ? 4 : 5);
    }

    public static DauTruongTuDoInfo GetInfo(Player p) {
        if (isOpen) {
            if (!ENTRY.containsKey("" + p.IDPlayer)) {
                DauTruongTuDoInfo info = new DauTruongTuDoInfo();
                info.point = 100_000;
                info.color = 0;
                info.typePk = 4;
                info.win = 0;
                info.lose = 0;
                ENTRY.put("" + p.IDPlayer, info);
            }
            return ENTRY.get("" + p.IDPlayer);
        }
        return null;
    }

    public static void UpdateInfo(Player p, Player pDie) throws IOException {
        if (IsCantChangeFlag(pDie)) {
            int numXp = ZUtil.random(5_000, 25_000);
            DauTruongTuDoInfo info = ENTRY.get("" + p.IDPlayer);
            DauTruongTuDoInfo infoDie = ENTRY.get("" + pDie.IDPlayer);
            if (info == null || infoDie == null) return;
            if (infoDie.point == 0) {
                return;
            }
            info.point += numXp;
            infoDie.point -= numXp;
            info.win++;
            infoDie.lose++;
            if (infoDie.point < 0) {
                infoDie.point = 0;
            }
            if (info.point >= 1_000_000) {
                info.color = 3;
            } else if (info.point >= 500_000) {
                info.color = 2;
            } else if (info.point >= 250_000) {
                info.color = 1;
            }
            if (infoDie.point >= 1_000_000) {
                infoDie.color = 3;
            } else if (infoDie.point >= 500_000) {
                infoDie.color = 2;
            } else if (infoDie.point >= 250_000) {
                infoDie.color = 1;
            }
            {
                Message m = new Message(39);
                m.writer().writeShort(p.index_map);
                m.writer().writeInt(info.point);
                m.writer().writeByte(info.color == 0 ? 11 : (info.color == 1 ? 4 : (info.color == 2 ? 3 : 1)));
                p.map.send_msg_all_p(m, null, true);
            }
            {
                Message m = new Message(39);
                m.writer().writeShort(pDie.index_map);
                m.writer().writeInt(infoDie.point);
                m.writer().writeByte(infoDie.color == 0 ? 11 : (infoDie.color == 1 ? 4 : (infoDie.color == 2 ? 3 : 1)));
                p.map.send_msg_all_p(m, null, true);
            }
        }
    }

    public static int getMapIdByLevel(short level) {
        if (level < 65) return 70;       // Đấu Trường Sơ cấp (Cấp 50 - 64, Map 70)
        if (level < 80) return 71;       // Đấu Trường Trung cấp (Cấp 65 - 79, Map 71)
        if (level < 95) return 72;       // Đấu Trường Cao cấp (Cấp 80 - 94, Map 72)
        return 74;                       // Đấu Trường Siêu cấp (Cấp 95+, Map 74)
    }

    public static void SendInfoGotoMap(Player p) throws IOException {
        if (IsCantChangeFlag(p)) {
            DauTruongTuDoInfo info = GetInfo(p);
            if (info == null) return;

            byte assignedFlag = getBalancedFlag(p.map, p);
            info.typePk = assignedFlag;
            p.type_pk = info.typePk;
            p.isBackTypePk = true;

            // Broadcast cờ cho toàn bộ người chơi trong map
            if (p.map != null) {
                p.map.change_flag(p, info.typePk);
            }
            if (p.getService() != null) {
                p.getService().update_PK(p, false);
            }

            // Đồng bộ cờ cho toàn bộ Lính Đánh Thuê theo chủ nhân
            List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(p);
            if (activeMercs != null) {
                for (bot.mercenary.MercenaryBot merc : activeMercs) {
                    if (merc != null && merc.map != null && merc.map.equals(p.map)) {
                        merc.type_pk = info.typePk;
                        if (p.map != null) {
                            p.map.change_flag(merc, info.typePk);
                        }
                    }
                }
            }

            // Gói tin UI điểm & màu cờ
            Message m = new Message(-7);
            m.writer().writeByte(22);
            m.writer().writeByte(1);
            p.addmsg(m);
            m.cleanup();

            m = new Message(39);
            m.writer().writeShort(p.index_map);
            m.writer().writeInt(info.point);
            m.writer().writeByte(info.color == 0 ? 11 : (info.color == 1 ? 4 : (info.color == 2 ? 3 : 1)));
            p.addmsg(m);
            m.cleanup();

            // Đồng bộ điểm & cờ của tất cả đối thủ khác trong map cho p và ngược lại
            if (p.map != null) {
                for (int i = 0; i < p.map.players.size(); i++) {
                    Player other = p.map.players.get(i);
                    if (other != null && other.IDPlayer != p.IDPlayer) {
                        SendInfoOther(other, p);
                        SendInfoOther(p, other);
                    }
                }
            }

            // Gửi bộ đếm thời gian HUD chính xác
            if (p.getService() != null) {
                p.getService().send_time_cool_down(time, "Đấu Trường Tự Do", 0);
            }
        } else {
            if (p.isBackTypePk) {
                p.isBackTypePk = false;
                p.type_pk = -1;
                if (p.map != null) {
                    p.map.change_flag(p, -1);
                }
                if (p.getService() != null) {
                    p.getService().update_PK(p, false);
                    p.getService().send_time_cool_down(0, "", 0);
                }

                // Tháo cờ toàn bộ Lính Đánh Thuê khi rời Đấu Trường
                List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(p);
                if (activeMercs != null) {
                    for (bot.mercenary.MercenaryBot merc : activeMercs) {
                        if (merc != null) {
                            merc.type_pk = -1;
                        }
                    }
                }
            }
        }
    }

    public static boolean IsCantChangeFlag(Player p) {
        return (isOpen && p != null && p.map != null && p.map.template != null && (p.map.template.id == 70 || p.map.template.id == 71 || p.map.template.id == 72 || p.map.template.id == 74) && (p.map.zone_id == 0 || p.map.zone_id == 1));
    }

    public static boolean CheckFlag(Player p) throws IOException {
        if (IsCantChangeFlag(p)) {
            DauTruongTuDoInfo info = GetInfo(p);
            if (info.point == 0 && (p.type_pk == 4 || p.type_pk == 5)) {
                return true;
            }
        }
        return false;
    }

    public static void SendInfoOther(Player p, Player pReceiv) throws IOException {
        if (p == null || pReceiv == null || pReceiv.conn == null) return;
        if (IsCantChangeFlag(p)) {
            DauTruongTuDoInfo info = GetInfo(p);
            if (info == null) return;
            Message m = new Message(39);
            m.writer().writeShort(p.index_map);
            m.writer().writeInt(info.point);
            m.writer().writeByte(info.color == 0 ? 11 : (info.color == 1 ? 4 : (info.color == 2 ? 3 : 1)));
            if (pReceiv.conn != null) pReceiv.conn.addmsg(m); // double-check sau send_msg_all_p race
        }
    }

    public static boolean UseItem(Player p) throws IOException {
        DauTruongTuDoInfo info = GetInfo(p);
        if (info != null) {
            if (info.point == 0) {
                info.point += 50_000;
                Message m = new Message(39);
                m.writer().writeShort(p.index_map);
                m.writer().writeInt(info.point);
                m.writer().writeByte(info.color == 0 ? 11 : (info.color == 1 ? 4 : (info.color == 2 ? 3 : 1)));
                p.map.send_msg_all_p(m, null, true);
                return true;
            }
            p.getService().send_box_ThongBao_OK("Chỉ dùng khi đang 0xp");
            return false;
        }
        p.getService().send_box_ThongBao_OK("Hiện tại không thể sử dụng");
        return false;
    }

    private static int getTop(Player get) {
        for (int i = 0; i < bxh.size(); i++) {
            InfoMemList get1 = bxh.get(i);
            if (get1.name.equals(get.name)) {
                return (i + 1);
            }
        }
        return -1;
    }
}
