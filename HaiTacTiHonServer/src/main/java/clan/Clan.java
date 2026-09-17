package clan;

import event.EventManager;

import model.Buff;
import model.Clazz;
import model.Player;
import event.SuKienTrongCay;
import map.zones.ChiemDao;
import map.zones.ThuLinhBienKhoi;
import map.zones.PvpBang;
import core.Manager;
import network.Service;
import core.ZUtil;
import database.DbManager;
import network.Message;
import map.Zone;
import map.Vgo;
import map.zones.BaoVePhaoDai;
import org.json.simple.JSONArray;
import template.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import mob.Mob;
import org.joda.time.LocalTime;

public class Clan {

    public static HashMap<String, Long> HM_TIME_GIFT = new HashMap<>();

    public BaoVePhaoDai baoVePhaoDai;
    public boolean isGhepBaoVePhaoDai;
    public long timeGhepBaoVePhaoDai;
    public List<Player> pWait = new ArrayList<>();
    public HashMap<String, List<GiftBox>> listGift = new HashMap<>();

    public static void findBaoVePhaoDai() {
        List<Clan> listClan = new ArrayList<>();
        for (int i = 0; i < ENTRY.size(); i++) {
            Clan get = ENTRY.get(i);
            if (get.isGhepBaoVePhaoDai) {
                listClan.add(get);
            }
        }
        // 1. Ưu tiên ghép 2 clan thật ngay lập tức
        while (listClan.size() >= 2) {
            Clan clan1 = listClan.remove(ZUtil.random(listClan.size()));
            Clan clan2 = listClan.remove(ZUtil.random(listClan.size()));
            BaoVePhaoDai tt = new BaoVePhaoDai();
            List<Player> listP = new ArrayList<>();
            listP.addAll(clan1.pWait);
            listP.addAll(clan2.pWait);
            clan1.isGhepBaoVePhaoDai = false;
            clan2.isGhepBaoVePhaoDai = false;
            clan1.baoVePhaoDai = tt;
            clan2.baoVePhaoDai = tt;
            tt.Init(clan1, clan2, listP);
        }
        // 2. Nếu chỉ có 1 clan: chỉ ghép bot khi hết thời gian chờ ngẫu nhiên
        if (listClan.size() == 1) {
            Clan realClan = listClan.get(0);
            if (realClan.timeGhepBaoVePhaoDai < System.currentTimeMillis()) {
                listClan.remove(0);
                Clan botClan = new Clan();
                botClan.id = (short) (-900 - ZUtil.random(100));
                botClan.name = bot.botplayer.BotNameGenerator.generateRandomClanName();
                botClan.icon = (short) ZUtil.random(1, 10);
                botClan.level = (short) Math.max(1, realClan.level > 0 ? realClan.level : (short) ZUtil.random(5, 12));
                botClan.trungsinh = realClan.trungsinh > 0 ? realClan.trungsinh : (byte) ZUtil.random(0, 2);
                botClan.xp = ZUtil.random(1000, 8000);
                botClan.members = new ArrayList<>();
                ClanMember leader = new ClanMember();
                leader.id = botClan.id;
                leader.name = "Thuyền Trưởng " + botClan.name;
                leader.level = (short) Math.max(30, (botClan.level * 6 + ZUtil.random(10, 20)));
                leader.levelInclan = 0;
                botClan.members.add(leader);
                BaoVePhaoDai tt = new BaoVePhaoDai();
                List<Player> listP = new ArrayList<>(realClan.pWait);
                realClan.isGhepBaoVePhaoDai = false;
                realClan.baoVePhaoDai = tt;
                botClan.baoVePhaoDai = tt;
                tt.Init(realClan, botClan, listP);
            }
        }
    }

    public static void endBaoVePhaoDai() {
        List<BaoVePhaoDai> listClan = new ArrayList<>();
        for (int i = 0; i < ENTRY.size(); i++) {
            Clan get = ENTRY.get(i);
            if (get.baoVePhaoDai != null && get.baoVePhaoDai.time < System.currentTimeMillis() && !listClan.contains(get.baoVePhaoDai)) {
                listClan.add(get.baoVePhaoDai);
            }
        }
        for (int i = 0; i < listClan.size(); i++) {
            BaoVePhaoDai get = listClan.get(i);
            new Thread(() -> {
                try {
                    List<Player> listP = new ArrayList<>();
                    for (int j = 0; j < get.maps.size(); j++) {
                        Zone map = get.maps.get(j);
                        listP.addAll(map.players);
                        map.stop_map();
                    }
                    final Clan[] clanWin = new Clan[1];
                    boolean[] isHoa = new boolean[]{false};
                    if (get.truChinhA == null) {
                        clanWin[0] = get.clanB;
                    } else if (get.truChinhB == null) {
                        clanWin[0] = get.clanA;
                    } else if (get.pointA < get.pointB) {
                        clanWin[0] = get.clanB;
                    } else if (get.pointA > get.pointB) {
                        clanWin[0] = get.clanA;
                    } else {
                        isHoa[0] = true;
                    }
                    List<GiftBox> listGiftWin = new ArrayList<>();
                    GiftBox.addGift(listGiftWin, (byte) 4, (short) 1, ZUtil.random(50, 100));
                    GiftBox.addGift(listGiftWin, (byte) 4, (short) -10, 2000);
                    List<GiftBox> listGiftLose = new ArrayList<>();
                    GiftBox.addGift(listGiftLose, (byte) 4, (short) -10, 1000);
                    listP.forEach(p0 -> {
                        try {
                            p0.getService().send_time_cool_down(System.currentTimeMillis() + 10_000, "Thời gian", 2);
                            if (isHoa[0]) {
                                Service.send_gift(p0, 1, "Phần thưởng Bảo vệ pháo đài", "Phần thưởng Hòa", listGiftLose, true);
                            } else {
                                if (p0.clan != null && clanWin[0] != null && p0.clan.equals(clanWin[0])) {
                                    Service.send_gift(p0, 1, "Phần thưởng Bảo vệ pháo đài", "Phần thưởng Thắng cuộc", listGiftWin, true);
                                } else {
                                    Service.send_gift(p0, 1, "Phần thưởng Bảo vệ pháo đài", "Phần thưởng Thua cuộc", listGiftLose, true);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    Thread.sleep(10_000);
                    listP.forEach(p0 -> {
                        try {
                            Vgo vgo = new Vgo();
                            vgo.map_go = Zone.getMapByID(p0.id_map_save > 0 ? p0.id_map_save : 1);
                            vgo.xnew = (short) (vgo.map_go[0].template.maxW / 2);
                            vgo.ynew = (short) (vgo.map_go[0].template.maxH / 2);
                            p0.goto_map(vgo);
                            if (p0.map != null) {
                                p0.map.change_flag(p0, -1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception ex) {
                }
            }).start();
            if (get.clanA != null) get.clanA.baoVePhaoDai = null;
            if (get.clanB != null) get.clanB.baoVePhaoDai = null;
            get.isClose = true;
        }
    }

    private static long parseTimeFromData(String data) {
        try {
            int idx = data.indexOf("time:");
            if (idx != -1) {
                String sub = data.substring(idx + 5).trim();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < sub.length(); i++) {
                    char c = sub.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    } else {
                        break;
                    }
                }
                return Long.parseLong(sb.toString());
            }
        } catch (Exception e) {
            // Ignore
        }
        return 0L;
    }

    static {
        try {
            historys.ClanHistory.loadClanTimeGifts(HM_TIME_GIFT);
        } catch (Exception ignored) {}
    }

    // type == 0 T.thuyentruong;
    // type == 1 T.thuyenpho;
    // type == 2 T.hoatieu;
    // else 10 T.thanhvien;
    public static List<Clan> ENTRY;
    public static List<String> BXH;

    private static void send_cdGift(Player p) throws IOException {
        if (p == null || p.clan == null || p.clan.members == null) return;
        Message m = new Message(-52);
        m.writer().writeByte(13);
        if (HM_TIME_GIFT.containsKey(p.name)) {
            long time = HM_TIME_GIFT.get(p.name);
            time -= System.currentTimeMillis();
            time /= 1_000;
            m.writer().writeInt((int) time); // tinh = sec
        } else {
            m.writer().writeInt(0); // tinh = sec
        }
        for (int i = 0; i < p.clan.members.size(); i++) {
            if (p.clan.members.get(i) != null && p.clan.members.get(i).name.equals(p.name)) {
                p.clan.members.get(i).donate++;
                m.writer().writeInt(p.clan.members.get(i).donate);
                break;
            }
        }
        p.addmsg(m);
        m.cleanup();
    }

    public List<ClanMember> members = new ArrayList<>();
    public short icon;
    public String name;
    public short id;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Clan)) return false;
        Clan other = (Clan) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }
    public short level = 1;
    public int xp = 0;
    public short maxAttri = 20;
    public short pointAttri = 0;
    public short[] opAttri = new short[]{0, 0, 0, 0, 0};
    public String thongbao = "";
    public byte trungsinh = 0;
    public int countAction = 0;
    public int ruby = 0;
    public int beri = 0;
    public List<ClanChat> chat = new ArrayList<>();
    public byte allowRequest = 1;
    public List<ClanMember> mem_request = new ArrayList<>();
    public List<ItemBag47> list_it = new ArrayList<>();
    public List<EffTemplate> buff = new CopyOnWriteArrayList<>();
    public Zone map_create;
    public Mob[] mob1 = new Mob[10];
    public int numPvp = 5;

    public int getTypeMem(Player p) {
        // type == 0 T.thuyentruong;
        // type == 1 T.thuyenpho;
        // type == 2 T.hoatieu;
        // else 10 T.thanhvien;
        if (p == null) return 10;
        Player target = p;
        if (p instanceof bot.mercenary.MercenaryBot) {
            Player owner = ((bot.mercenary.MercenaryBot) p).getOwner();
            if (owner != null) target = owner;
        } else if (p instanceof model.DeTu) {
            Player master = ((model.DeTu) p).master;
            if (master != null) target = master;
        }
        if (this.members != null) {
            for (int i = 0; i < this.members.size(); i++) {
                ClanMember cm = this.members.get(i);
                if (cm != null && cm.name != null && cm.name.equals(target.name)) {
                    return cm.levelInclan;
                }
            }
        }
        return 10;
    }
    
     public static boolean renameClan(Clan clan, String newName) {
        if (newName == null || newName.isEmpty()) {
            return false; 
        }

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = DbManager.gI().getConnect();
            ps = connection.prepareStatement("SELECT `id` FROM `clan` WHERE `name` = ?");
            ps.setString(1, newName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return false; 
            }

            ps.close();
            ps = connection.prepareStatement("UPDATE `clan` SET `name` = ? WHERE `id` = ?");
            ps.setString(1, newName);
            ps.setInt(2, clan.id);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                clan.name = newName; 
                return true; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
    
    @SuppressWarnings("unchecked")
    public static boolean delete_clan_from_db(Clan clan) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DbManager.gI().getConnect();
            ps = connection.prepareStatement("DELETE FROM `clan` WHERE `id` = ?");
            ps.setInt(1, clan.id);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        delete_clan(clan);

        return true;
    }

    public ClanMember getLeader() {
        if (this.members != null && !this.members.isEmpty()) {
            for (ClanMember cm : this.members) {
                if (cm != null && cm.levelInclan == 0) return cm;
            }
            for (ClanMember cm : this.members) {
                if (cm != null && cm.levelInclan == 1) return cm;
            }
            return this.members.get(0);
        }
        return null;
    }

    public String getLeaderName() {
        ClanMember leader = getLeader();
        if (leader != null && leader.name != null && !leader.name.isEmpty()) {
            return leader.name;
        }
        return "";
    }

    public ClanMember getMember(String name) {
        if (name == null || this.members == null) return null;
        String clean = name.trim();
        for (ClanMember cm : this.members) {
            if (cm != null && cm.name != null && (cm.name.equalsIgnoreCase(clean) || cm.name.trim().equalsIgnoreCase(clean))) {
                return cm;
            }
        }
        return null;
    }

    public static int get_icon_clan(String name) {
        if (name == null || ENTRY == null) return -1;
        String clean = name.trim();
        for (int i = 0; i < ENTRY.size(); i++) {
            Clan c = ENTRY.get(i);
            if (c != null && c.members != null) {
                for (int j = 0; j < c.members.size(); j++) {
                    ClanMember cm = c.members.get(j);
                    if (cm != null && cm.name != null && (cm.name.equalsIgnoreCase(clean) || cm.name.trim().equalsIgnoreCase(clean))) {
                        return c.icon;
                    }
                }
            }
        }
        return -1;
    }

    public static Clan get_my_clan(int playerId, String name) {
        if (ENTRY == null) return null;
        String clean = (name != null) ? name.trim() : "";
        for (int i = 0; i < ENTRY.size(); i++) {
            Clan c = ENTRY.get(i);
            if (c != null && c.members != null) {
                for (int j = 0; j < c.members.size(); j++) {
                    ClanMember cm = c.members.get(j);
                    if (cm != null) {
                        if (playerId > 0 && cm.playerId == playerId) {
                            if (!clean.isEmpty() && !cm.name.equalsIgnoreCase(clean)) {
                                cm.name = clean;
                            }
                            return c;
                        }
                        if (!clean.isEmpty() && cm.name != null && (cm.name.equalsIgnoreCase(clean) || cm.name.trim().equalsIgnoreCase(clean))) {
                            if (playerId > 0 && cm.playerId <= 0) {
                                cm.playerId = playerId;
                            }
                            return c;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Clan get_my_clan(String name) {
        return get_my_clan(-1, name);
    }

    public static Clan get_clan_by_id(int id) {
        if (ENTRY != null) {
            for (int i = 0; i < ENTRY.size(); i++) {
                Clan c = ENTRY.get(i);
                if (c != null && c.id == id) {
                    return c;
                }
            }
        }
        for (bot.BotLobbyManager.BotLobbyBot lobbyBot : bot.BotLobbyManager.LOBBY_BOTS) {
            if (lobbyBot != null && lobbyBot.clan != null && lobbyBot.clan.id == id) {
                return lobbyBot.clan;
            }
        }
        return null;
    }

    public static Clan get_clan_by_name(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        String clean = name.trim();
        if (ENTRY != null) {
            for (int i = 0; i < ENTRY.size(); i++) {
                Clan c = ENTRY.get(i);
                if (c != null && c.name != null && (c.name.equalsIgnoreCase(clean) || c.name.trim().equalsIgnoreCase(clean))) {
                    return c;
                }
            }
        }
        for (bot.BotLobbyManager.BotLobbyBot lobbyBot : bot.BotLobbyManager.LOBBY_BOTS) {
            if (lobbyBot != null && lobbyBot.clan != null && lobbyBot.clan.name != null
                    && (lobbyBot.clan.name.equalsIgnoreCase(clean) || lobbyBot.clan.name.trim().equalsIgnoreCase(clean))) {
                return lobbyBot.clan;
            }
        }
        return null;
    }

    public static void broadcastSyncStats(Clan clan) {
        if (clan == null || clan.members == null) return;
        for (int i = 0; i < clan.members.size(); i++) {
            ClanMember mem = clan.members.get(i);
            if (mem != null) {
                Player pt = Zone.get_player_by_name_allmap(mem.name);
                if (pt != null) {
                    pt.syncFullPlayerStats();
                }
            }
        }
    }

    public static void send_info(Player p, boolean b) throws IOException {
        if (p.isDe || p instanceof model.DeTu) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể mở thông tin Băng hải tặc!");
            return;
        }
        if (p.clan != null) {
            Message m = new Message(-52);
            m.writer().writeByte(0);
            m.writer().writeShort(p.clan.id);
            m.writer().writeUTF(p.clan.name);
            if (b) {
                p.msgs.add(m);
            } else {
                p.addmsg(m);
            }
            m.cleanup();
            //
            set_data(p, b);
            send_Attri(p, b);
            update_list_member(p, b);
            send_me_to_other(p, p, b);
            send_notice(p, false);
            p.clan.send_inventory(p, b);
            //
            send_xp(p, b);
            send_money(p, b);
            //
        }
    }

    public static void send_money(Player p, boolean b) throws IOException {
        if (p.clan != null) {
            Message m = new Message(-52);
            m.writer().writeByte(17);
            m.writer().writeInt(p.clan.get_ngoc());
            m.writer().writeInt(p.clan.get_vang());
            if (b) {
                p.msgs.add(m);
            } else {
                p.addmsg(m);
            }
            m.cleanup();
        }
    }

    public static void send_me_to_other(Player p0, Player p, boolean b) throws IOException {
        if (p0 != null && p0.clan != null && p != null) {
            Message m = new Message(-52);
            m.writer().writeByte(5);
            m.writer().writeShort(p0.index_map);
            m.writer().writeShort(p0.clan.id);
            m.writer().writeShort(p0.clan.icon);
            byte levelInClan = (byte) p0.clan.getTypeMem(p0);
            m.writer().writeByte(levelInClan);
            if ((!Clan.BXH.isEmpty() && Clan.BXH.get(0).equals(p0.clan.name))
                    || p0.clan.equals(ChiemDao.clanTop1)
                    || p0.clan.equals(ChiemDao.clanTop2)
                    || p0.clan.equals(ChiemDao.clanTop3)
                    || p0.clan.equals(ChiemDao.clanTop4)
                    || p0.clan.equals(ChiemDao.clanTop5)) {
                m.writer().writeByte(1);
            } else {
                m.writer().writeByte(0);
            }
            if (b) {
                p.msgs.add(m);
            } else {
                p.addmsg(m);
            }
            m.cleanup();
        }
    }

   public synchronized static void reset_day() {
        for (int i = 0; i < Clan.ENTRY.size(); i++) {
            Clan.ENTRY.get(i).chat.clear();
            Clan.ENTRY.get(i).numPvp = 5;
            for (int j = 0; j < Clan.ENTRY.get(i).members.size(); j++) {
                ClanMember mem = Clan.ENTRY.get(i).members.get(j);
                mem.numquest = 0;
                // mem.gopRuby = 0;
            }
        }
        PvpBang.typeFight = ZUtil.random(4);
        PvpBang.typeFight = 3;
        PvpBang.mapClanTicket.clear();

    }

    public synchronized static List<Clan> get_list_now_clan() {
        List<Clan> result = new ArrayList<>();
        for (int i = 0; i < Clan.ENTRY.size(); i++) {
            result.add(Clan.ENTRY.get(i));
        }
        return result;
    }
    public List<Integer> hanhtrinh = new ArrayList<>();
    public ClanHanhTrinhIcon hanhtrinhIcon;
    public long cdItem;

    public int get_vang() {
        return beri;
    }

    public int get_ngoc() {
        return ruby;
    }

    private static void send_xp(Player p, boolean b) throws IOException {
        if (p == null || p.clan == null) return;
        Message m = new Message(-52);
        m.writer().writeByte(16);
        m.writer()
                .writeByte(p.clan.xp >= Clan.get_xp_max(p.clan.level, p.clan.trungsinh)
                        ? ((p.clan.level == 15) ? 2 : 1)
                        : 0);
        m.writer().writeInt(p.clan.xp);
        if (b) {
            p.msgs.add(m);
        } else {
            p.addmsg(m);
        }
        m.cleanup();
    }

    public static void process(Player p, Message m2, int type) throws IOException {
        if (p == null) return;
        if (p.isDe || p instanceof model.DeTu) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể thực hiện chức năng Băng hải tặc!");
            return;
        }
        // System.out.println(type);
        switch (type) {
            case 2: { // send gift 2other
                if (p.clan == null || p.clan.members == null) break;
                String nameTarget = m2.reader().readUTF();
                m2.reader().readByte(); // levelInClan
                if (!nameTarget.equals(p.name)) {
                    if (HM_TIME_GIFT.containsKey(p.name)) {
                        long time = HM_TIME_GIFT.get(p.name);
                        if (time > System.currentTimeMillis()) {
                            p.getService().send_box_ThongBao_OK("Chỉ có thể tặng quà sau "
                                    + ZUtil.get_time_str_by_sec2(time - System.currentTimeMillis()));
                            return;
                        }
                    }
                    //
                    ClanMember memSelect = null;
                    ClanMember me = null;
                    for (int i = 0; i < p.clan.members.size(); i++) {
                        if (p.clan.members.get(i).name.equals(nameTarget)) {
                            memSelect = p.clan.members.get(i);
                        } else if (p.clan.members.get(i).name.equals(p.name)) {
                            me = p.clan.members.get(i);
                        }
                    }
                    if (memSelect != null && me != null) {
                        Player pSelect = Zone.get_player_by_name_allmap(memSelect.name);
                        if (pSelect != null) {
                            long cooldownTime = System.currentTimeMillis() + (60_000l * 60 * 8);
                            HM_TIME_GIFT.put(p.name, cooldownTime);
                            historys.zLog.gI().add_log(p, "CLAN_TIME_GIFT", "time:" + cooldownTime);
                            int rubyAdd = ZUtil.random(1, 6);
                            pSelect.update_ngoc(rubyAdd);
                            pSelect.updateMoney();
                            pSelect.getService().send_box_ThongBao_OK(p.name + " trong băng của bạn tặng bạn " + rubyAdd + " ruby");
                            p.getService().send_box_ThongBao_OK("Tặng " + rubyAdd + " ruby cho " + pSelect.name + " thành công");
                            //
                            Clan.send_cdGift(p);
                            for (int i = 0; i < p.clan.members.size(); i++) {
                                Player pSelect2 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                                if (pSelect2 != null) {
                                    Clan.update_list_member(pSelect2, false);
                                }
                            }
                            ClanChat chat = new ClanChat();
                            chat.idMem = me.id;
                            chat.name = me.name;
                            chat.str = "Tặng " + memSelect.name + " " + rubyAdd + " ruby";
                            chat.time = System.currentTimeMillis();
                            chat.typeChat = -4;
                            p.clan.add_chat(chat);
                            //
                            p.clan.send_chat(chat, null);
                        } else {
                            p.getService().send_box_ThongBao_OK("Đối phương offline");
                        }
                    }
                }
                break;
            }
            case 13: { // up clan
                if (p.clan == null || p.clan.members == null || p.clan.members.isEmpty()) break;
                if (p.clan.members.get(0).name.equals(p.name)) {
                    if (p.clan.xp >= Clan.get_xp_max(p.clan.level, p.clan.trungsinh)) {
                        if (p.clan.get_ngoc() < Clan.get_ngoc_upgrade(p.clan.level,
                                p.clan.trungsinh)) {
                            p.getService().send_box_ThongBao_OK("Cần " + ZUtil.number_format(
                                            Clan.get_ngoc_upgrade(p.clan.level, p.clan.trungsinh))
                                    + " ruby băng để thực hiện nâng cấp");
                            return;
                        }
                        if (p.clan.get_vang() < Clan.get_vang_upgrade(p.clan.level)) {
                            p.getService().send_box_ThongBao_OK("Cần " + ZUtil.number_format(Clan.get_vang_upgrade(p.clan.level))
                                    + " beri băng để thực hiện nâng cấp");
                            return;
                        }
                        if (p.clan.level >= 14 && p.clan.trungsinh >= 6) {
                            p.getService().send_box_ThongBao_OK("Trùng sinh đạt tối đa không thể nâng thêm!");
                            return;
                        }
                        p.clan.update_ruby(-Clan.get_ngoc_upgrade(p.clan.level, p.clan.trungsinh));
                        p.clan.update_beri(-Clan.get_vang_upgrade(p.clan.level));
                        //
                        p.clan.xp -= Clan.get_xp_max(p.clan.level, p.clan.trungsinh);
                        p.clan.level++;
                        p.clan.pointAttri += 2;
                        if (p.clan.level >= 16) {
                            p.clan.level = 1;
                            p.clan.opAttri = new short[]{0, 0, 0, 0, 0};
                            p.clan.pointAttri = 2;
                            p.clan.maxAttri = 20;
                            p.clan.trungsinh++;
                            switch (p.clan.trungsinh) {
                                case 1: {
                                    p.clan.maxAttri = 25;
                                    break;
                                }
                                case 2: {
                                    p.clan.maxAttri = 30;
                                    break;
                                }
                                case 3: {
                                    p.clan.maxAttri = 35;
                                    break;
                                }
                                case 4: {
                                    p.clan.maxAttri = 40;
                                    break;
                                }
                                case 5: {
                                    p.clan.maxAttri = 45;
                                    break;
                                }
                                case 6: {
                                    p.clan.maxAttri = 50;
                                    break;
                                }
                            }
                        }
                        //
                        for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i1).name);
                            if (p0 != null) {
                                Clan.send_info(p0, false);
                                p0.syncFullPlayerStats();
                            }
                        }
                        //
                        if (p.clan.level == 1) {
                            p.getService().send_box_ThongBao_OK("Trùng sinh băng thành công lên " + p.clan.trungsinh);
                        } else {
                            p.getService().send_box_ThongBao_OK("Nâng cấp bang thành công lên cấp " + p.clan.level);
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Chưa đủ điều kiện nâng cấp bang");
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng");
                }
                break;
            }
            case 14: { // use item
                if (p.clan == null || p.clan.members == null || p.clan.members.isEmpty()) break;
                short id = m2.reader().readShort();
                // byte chucVu =
                m2.reader().readByte();
                if (p.clan.members.get(0).name.equals(p.name)) {
                    ItemBag47 it_select = null;
                    for (int i = 0; i < p.clan.list_it.size(); i++) {
                        if (p.clan.list_it.get(i).id == id) {
                            if (p.clan.list_it.get(i).quant > 0) {
                                it_select = p.clan.list_it.get(i);
                            }
                            break;
                        }
                    }
                    if (it_select != null) {
                        // System.out.println(id);
                        switch (id) {
                            case 0: {
                                EffTemplate eff = null;
                                if (p.clan.buff != null) {
                                    for (EffTemplate b : p.clan.buff) {
                                        if (b != null && b.id == 0) {
                                            eff = b;
                                            break;
                                        }
                                    }
                                }
                                if (eff != null) {
                                    if (eff.time > System.currentTimeMillis()) {
                                        eff.time += (60_000L * 60);
                                    } else {
                                        eff.time = System.currentTimeMillis() + (60_000L * 60);
                                    }
                                } else {
                                    eff = new EffTemplate(0, 100,
                                            System.currentTimeMillis() + (60_000L * 60));
                                    p.clan.buff.add(eff);
                                }
                                ClanChat chat = new ClanChat();
                                chat.idMem = p.clan.members.get(0).id;
                                chat.name = p.clan.members.get(0).name;
                                chat.str = "sử dụng bùa kinh nghiệm lv1 tác dụng còn "
                                        + ((eff.time - System.currentTimeMillis()) / 1000) + "s";
                                chat.time = System.currentTimeMillis();
                                chat.typeChat = -3;
                                p.clan.add_chat(chat);
                                //
                                p.clan.send_chat(chat, null);
                                //
                                it_select.quant--;
                                if (it_select.quant <= 0) {
                                    p.clan.list_it.remove(it_select);
                                }
                                for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                    Player p0 = Zone
                                            .get_player_by_name_allmap(p.clan.members.get(i1).name);
                                    if (p0 != null) {
                                        Clan.send_info(p0, false);
                                    }
                                }
                                break;
                            }
                            case 1: {
                                EffTemplate eff = null;
                                if (p.clan.buff != null) {
                                    for (EffTemplate b : p.clan.buff) {
                                        if (b != null && b.id == 1) {
                                            eff = b;
                                            break;
                                        }
                                    }
                                }
                                if (eff != null) {
                                    if (eff.time > System.currentTimeMillis()) {
                                        eff.time += (60_000L * 60);
                                    } else {
                                        eff.time = System.currentTimeMillis() + (60_000L * 60);
                                    }
                                } else {
                                    eff = new EffTemplate(1, 100,
                                            System.currentTimeMillis() + (60_000L * 60));
                                    p.clan.buff.add(eff);
                                }
                                ClanChat chat = new ClanChat();
                                chat.idMem = p.clan.members.get(0).id;
                                chat.name = p.clan.members.get(0).name;
                                chat.str = "sử dụng bùa kinh nghiệm lv2 tác dụng còn "
                                        + ((eff.time - System.currentTimeMillis()) / 1000) + "s";
                                chat.time = System.currentTimeMillis();
                                chat.typeChat = -3;
                                p.clan.add_chat(chat);
                                //
                                p.clan.send_chat(chat, null);
                                //
                                it_select.quant--;
                                if (it_select.quant <= 0) {
                                    p.clan.list_it.remove(it_select);
                                }
                                for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                    Player p0 = Zone
                                            .get_player_by_name_allmap(p.clan.members.get(i1).name);
                                    if (p0 != null) {
                                        Clan.send_info(p0, false);
                                    }
                                }
                                break;
                            }case 8: {
                                if (p.clan.numPvp > 0) {
                                    p.clan.numPvp--;
                                    it_select.quant--;
                                    if (it_select.quant <= 0) {
                                        p.clan.list_it.remove(it_select);
                                    }
                                    for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                        Player p0 = Zone
                                                .get_player_by_name_allmap(p.clan.members.get(i1).name);
                                        if (p0 != null) {
                                            Clan.send_info(p0, false);
                                        }
                                    }
                                    PvpBang.buyTicket(p.clan);
                                    p.getService().send_box_ThongBao_OK("Số lượt pvp băng hiện tại: " + (5 - PvpBang.getTicket(p.clan)));
                                } else {
                                    p.getService().send_box_ThongBao_OK("Hôm nay đã hết lượt sử dụng vé pvp băng");
                                }

                                break;
                            }
                            case 2: {
                                EffTemplate eff = null;
                                if (p.clan.buff != null) {
                                    for (EffTemplate b : p.clan.buff) {
                                        if (b != null && b.id == 2) {
                                            eff = b;
                                            break;
                                        }
                                    }
                                }
                                if (eff != null) {
                                    if (eff.time > System.currentTimeMillis()) {
                                        eff.time += (60_000L * 60);
                                    } else {
                                        eff.time = System.currentTimeMillis() + (60_000L * 60);
                                    }
                                } else {
                                    eff = new EffTemplate(2, 25,
                                            System.currentTimeMillis() + (60_000L * 60));
                                    p.clan.buff.add(eff);
                                }
                                ClanChat chat = new ClanChat();
                                chat.idMem = p.clan.members.get(0).id;
                                chat.name = p.clan.members.get(0).name;
                                chat.str = "sử dụng bùa hp tác dụng còn "
                                        + ((eff.time - System.currentTimeMillis()) / 1000) + "s";
                                chat.time = System.currentTimeMillis();
                                chat.typeChat = -3;
                                p.clan.add_chat(chat);
                                //
                                p.clan.send_chat(chat, null);
                                //
                                it_select.quant--;
                                if (it_select.quant <= 0) {
                                    p.clan.list_it.remove(it_select);
                                }
                                for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                    Player p0 = Zone
                                            .get_player_by_name_allmap(p.clan.members.get(i1).name);
                                    if (p0 != null) {
                                        Clan.send_info(p0, false);
                                    }
                                }
                                break;
                            }
                            case 3: {
                                EffTemplate eff = null;
                                if (p.clan.buff != null) {
                                    for (EffTemplate b : p.clan.buff) {
                                        if (b != null && b.id == 3) {
                                            eff = b;
                                            break;
                                        }
                                    }
                                }
                                if (eff != null) {
                                    if (eff.time > System.currentTimeMillis()) {
                                        eff.time += (60_000L * 60);
                                    } else {
                                        eff.time = System.currentTimeMillis() + (60_000L * 60);
                                    }
                                } else {
                                    eff = new EffTemplate(3, 25,
                                            System.currentTimeMillis() + (60_000L * 60));
                                    p.clan.buff.add(eff);
                                }
                                ClanChat chat = new ClanChat();
                                chat.idMem = p.clan.members.get(0).id;
                                chat.name = p.clan.members.get(0).name;
                                chat.str = "sử dụng bùa mp tác dụng còn "
                                        + ((eff.time - System.currentTimeMillis()) / 1000) + "s";
                                chat.time = System.currentTimeMillis();
                                chat.typeChat = -3;
                                p.clan.add_chat(chat);
                                //
                                p.clan.send_chat(chat, null);
                                //
                                it_select.quant--;
                                if (it_select.quant <= 0) {
                                    p.clan.list_it.remove(it_select);
                                }
                                for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                    Player p0 = Zone
                                            .get_player_by_name_allmap(p.clan.members.get(i1).name);
                                    if (p0 != null) {
                                        Clan.send_info(p0, false);
                                    }
                                }
                                break;
                            }
                            case 4: {
                                EffTemplate eff = null;
                                if (p.clan.buff != null) {
                                    for (EffTemplate b : p.clan.buff) {
                                        if (b != null && b.id == 4) {
                                            eff = b;
                                            break;
                                        }
                                    }
                                }
                                if (eff != null) {
                                    if (eff.time > System.currentTimeMillis()) {
                                        eff.time += (60_000L * 60);
                                    } else {
                                        eff.time = System.currentTimeMillis() + (60_000L * 60);
                                    }
                                } else {
                                    eff = new EffTemplate(4, 25,
                                            System.currentTimeMillis() + (60_000L * 60));
                                    p.clan.buff.add(eff);
                                }
                                ClanChat chat = new ClanChat();
                                chat.idMem = p.clan.members.get(0).id;
                                chat.name = p.name;
                                chat.str = "sử dụng bùa tổng hợp tác dụng còn "
                                        + ((eff.time - System.currentTimeMillis()) / 1000) + "s";
                                chat.time = System.currentTimeMillis();
                                chat.typeChat = -3;
                                p.clan.add_chat(chat);
                                //
                                p.clan.send_chat(chat, null);
                                //
                                it_select.quant--;
                                if (it_select.quant <= 0) {
                                    p.clan.list_it.remove(it_select);
                                }
                                for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                    Player p0 = Zone
                                            .get_player_by_name_allmap(p.clan.members.get(i1).name);
                                    if (p0 != null) {
                                        Clan.send_info(p0, false);
                                    }
                                }
                                break;
                            }
                            case 6: {
                                it_select.quant--;
                                if (it_select.quant <= 0) {
                                    p.clan.list_it.remove(it_select);
                                }
                                p.clan.update_beri(10_000);
                                for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                    Player p0 = Zone
                                            .get_player_by_name_allmap(p.clan.members.get(i1).name);
                                    if (p0 != null) {
                                        Clan.send_info(p0, false);
                                    }
                                }
                                break;
                            }
                            case 7: {
                                it_select.quant--;
                                if (it_select.quant <= 0) {
                                    p.clan.list_it.remove(it_select);
                                }
                                p.clan.pointAttri = 0;
                                for (int i = 1; i <= p.clan.level; i++) {
                                    p.clan.pointAttri += 2;
                                }
                                p.clan.opAttri = new short[]{0, 0, 0, 0, 0};
                                for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                    Player p0 = Zone
                                            .get_player_by_name_allmap(p.clan.members.get(i1).name);
                                    if (p0 != null) {
                                        Clan.send_info(p0, false);
                                    }
                                }
                                p.getService().send_box_ThongBao_OK("Tẩy tiềm năng băng thành công");
                                break;
                            }
                            case 17: {
                                if (p.clan.cdItem < System.currentTimeMillis()) {
                                    p.clan.cdItem = System.currentTimeMillis() + 60_000;
                                    if (p.clan.equals(ChiemDao.getClanTop(p.map.template.id))) {
                                        it_select.quant--;
                                        if (it_select.quant <= 0) {
                                            p.clan.list_it.remove(it_select);
                                        }
                                        for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i1).name);
                                            if (p0 != null) {
                                                Clan.send_info(p0, false);
                                            }
                                        }
                                        if (p.map.template.id >= 261 && p.map.template.id <= 265) {
                                            for (int i = 0; i < p.map.players.size(); i++) {
                                                Player p0 = p.map.players.get(i);
                                                if (p0 != null && !p0.equals(p) && !p0.isdie && (p.canAttackTargetPlayer(p0) || (p0.clan != null && !p.clan.equals(p0.clan)))) {
                                                    p.map.getService().send_choang(p, p0, 10_000);
                                                    p0.add_new_eff(201, 1, 10_000);
                                                }
                                            }
                                        }
                                        p.getService().send_box_ThongBao_OK("Sử dụng thành công");
                                    } else {
                                        p.getService().send_box_ThongBao_OK("Băng chưa chiếm được đảo");
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK("Sử dụng sau "
                                            + ((p.clan.cdItem - System.currentTimeMillis()) / 1_000) + "s nữa");
                                }
                            }
                            break;
                            case 11: {
                                if (event.EventManager.isActive(3)) {
                                    if (p.map.template.id > 48 || p.map.zone_id > 4 || p.map.list_mob.length < 5 || p.map.isMapSea()) {
                                        p.getService().send_box_ThongBao_OK("Vị trí không phù hợp. Bạn chỉ có thể gọi bí ngô (Từ Làng Cối Xoay Gió đến Thị Trấn Khởi Đầu) ở khu từ 1-5");
                                        return;
                                    }
                                    int idx = -1;
                                    for (int i = 0; i < p.clan.mob1.length; i++) {
                                      if (p.clan.mob1[i]==null) {
                                          idx=i;
                                          break;
                                      }
                                    }
                                    if (idx !=-1) {
                                        it_select.quant--;
                                        if (it_select.quant <= 0) {
                                            p.clan.list_it.remove(it_select);
                                        }
                                        for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i1).name);
                                            if (p0 != null) {
                                                Clan.send_info(p0, false);
                                            }
                                        }
                                        MobTemplate mobTemplate = MobTemplate.ENTRYS.get(171);
                                        p.clan.mob1[idx] = new Mob();
                                        p.clan.mob1[idx].mtemplate = mobTemplate;
                                        p.clan.mob1[idx].x = p.x;
                                        p.clan.mob1[idx].y = p.y;
                                        p.clan.mob1[idx].hp_max = 50;
                                        p.clan.mob1[idx].hp = p.clan.mob1[idx].hp_max;
                                        p.clan.mob1[idx].level = 55;
                                        p.clan.mob1[idx].isdie = false;
                                        p.clan.mob1[idx].id_target = -1;
                                        p.clan.mob1[idx].index = -2-idx;
                                        p.clan.mob1[idx].map = p.map;
                                        p.clan.mob1[idx].boss_inf = null;
                                        p.map.getService().move(p.clan.mob1[idx].index, p.clan.mob1[idx].x, p.clan.mob1[idx].y);
                                    } else {
//                                        p.getService().send_box_ThongBao_OK("Đã triệu hồi bí ngô vị trí " + p.clan.mob1[idx].map.template.name + " khu " + (p.clan.mob1[idx].map.zone_id + 1));
                                            p.getService().send_box_ThongBao_OK("Đã triệu hồi tôi đa 10 bí ngô");
                                    }
                                    return;
                                }
                            }
                            break;
                            case 21: {
                                if (event.EventManager.isActive(9)) {
                                    if (p.map.template.id > 48 || p.map.zone_id > 4 || p.map.list_mob.length < 5 || p.map.isMapSea()) {
                                        p.getService().send_box_ThongBao_OK("Vị trí không phù hợp. Bạn chỉ có thể gieo hạt giống bên ngoài các Làng để trồng Cây thần kỳ (Từ Làng Cối Xoay Gió đến Thị Trấn Khởi Đầu) ở khu từ 1-5");
                                        return;
                                    }
                                    model.Tree t = SuKienTrongCay.getByName(p.name);
                                    if (t != null) {
                                        p.getService().send_box_ThongBao_OK("Hiện tại bạn đang trồng 1 cây rồi. Vị trí\n" + t.map.template.name + " khu " + (t.map.zone_id + 1));
                                        return;
                                    } else {
                                        it_select.quant--;
                                        if (it_select.quant <= 0) {
                                            p.clan.list_it.remove(it_select);
                                        }
                                        for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i1).name);
                                            if (p0 != null) {
                                                Clan.send_info(p0, false);
                                            }
                                        }
                                        //
                                        model.Tree myCay = new model.Tree();
                                        myCay.index = SuKienTrongCay.getIndex(p.IDPlayer);
                                        if (myCay.index != -1) {
                                            myCay.setup(p);
                                            SuKienTrongCay.setByName(p.name, myCay);
                                            //
                                            Message m_local = new Message(1);
                                            m_local.writer().writeByte(2);
                                            m_local.writer().writeShort(myCay.index);
                                            m_local.writer().writeShort(myCay.x);
                                            m_local.writer().writeShort(myCay.y);
                                            p.map.send_msg_all_p(m_local, null, true);
                                            m_local.cleanup();
                                            p.getService().send_box_ThongBao_OK("Gieo hạt thành công. Sau 10 phút có thể thu hoạch, nhớ chăm sóc đều đặn để tránh chết cây. Cây sẽ nảy mầm sau 5 - 10 giây");
                                            myCay.type = 2;
                                            Manager.gI().chatKTG(1, "Băng " + p.clan.name + " đã đặt nhẹ Hạt giống băng xuống đất và hét lớn: Có ngon thì đến cướp cây của bố", 0);
                                            for (int i = 0; i < p.clan.members.size(); i++) {
                                                Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                                                if (p0 != null) {
                                                    p0.map.change_flag(p0, -1);
                                                }
                                            }
                                            ClanChat chat = new ClanChat();
                                            chat.idMem = p.clan.members.get(0).id;
                                            chat.name = p.name;
                                            chat.str = "Hạt giống bang hiện tại ở " + myCay.map.template.name + " khu " + (myCay.map.zone_id + 1);
                                            chat.time = System.currentTimeMillis();
                                            chat.typeChat = -3;
                                            p.clan.add_chat(chat);
                                            //
                                            p.clan.send_chat(chat, null);
                                            //
                                        }
                                    }
                                    return;
                                }
                            }
                            case 18: {
                                if (p.clan.cdItem < System.currentTimeMillis()) {
                                    p.clan.cdItem = System.currentTimeMillis() + 60_000;
                                    if (p.clan.equals(ChiemDao.getClanTop(p.map.template.id))) {
                                        it_select.quant--;
                                        if (it_select.quant <= 0) {
                                            p.clan.list_it.remove(it_select);
                                        }
                                        for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i1).name);
                                            if (p0 != null) {
                                                Clan.send_info(p0, false);
                                            }
                                        }
                                        if (p.map.template.id >= 261 && p.map.template.id <= 265) {
                                            for (int i = 0; i < p.map.players.size(); i++) {
                                                Player p0 = p.map.players.get(i);
                                                if (!p.clan.equals(p0.clan)) {
                                                    p0.add_new_eff(23, 50, 15_000);
                                                }
                                            }
                                        }
                                        p.getService().send_box_ThongBao_OK("Sử dụng thành công");
                                    } else {
                                        p.getService().send_box_ThongBao_OK("Băng chưa chiếm được đảo");
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK("Sử dụng sau "
                                            + ((p.clan.cdItem - System.currentTimeMillis()) / 1_000) + "s nữa");
                                }
                            }
                            break;
                            case 19: {
                                if (p.clan.cdItem < System.currentTimeMillis()) {
                                    p.clan.cdItem = System.currentTimeMillis() + 60_000;
                                    if (p.clan.equals(ChiemDao.getClanTop(p.map.template.id))) {
                                        it_select.quant--;
                                        if (it_select.quant <= 0) {
                                            p.clan.list_it.remove(it_select);
                                        }
                                        for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i1).name);
                                            if (p0 != null) {
                                                Clan.send_info(p0, false);
                                            }
                                        }
                                        if (p.map.template.id >= 261 && p.map.template.id <= 265) {
                                            for (int i = 0; i < p.map.players.size(); i++) {
                                                Player p0 = p.map.players.get(i);
                                                if (p.clan.equals(p0.clan)) {
                                                    p0.add_new_eff(24, 1, 15_000);
                                                }
                                            }
                                        }
                                        p.getService().send_box_ThongBao_OK("Sử dụng thành công");
                                    } else {
                                        p.getService().send_box_ThongBao_OK("Băng chưa chiếm được đảo");
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK("Sử dụng sau "
                                            + ((p.clan.cdItem - System.currentTimeMillis()) / 1_000) + "s nữa");
                                }
                            }
                            break;
                            case 20: {
                                if (p.clan.cdItem < System.currentTimeMillis()) {
                                    p.clan.cdItem = System.currentTimeMillis() + 60_000;
                                    if (p.clan.equals(ChiemDao.getClanTop(p.map.template.id))) {
                                        it_select.quant--;
                                        if (it_select.quant <= 0) {
                                            p.clan.list_it.remove(it_select);
                                        }
                                        for (int i1 = 0; i1 < p.clan.members.size(); i1++) {
                                            Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i1).name);
                                            if (p0 != null) {
                                                Clan.send_info(p0, false);
                                            }
                                        }
                                        if (p.map.template.id >= 261 && p.map.template.id <= 265) {
                                            for (int i = 0; i < p.map.players.size(); i++) {
                                                Player p0 = p.map.players.get(i);
                                                if (!p.clan.equals(p0.clan)) {
                                                    p0.add_new_eff(25, 1, 15_000);
                                                }
                                            }
                                        }
                                        p.getService().send_box_ThongBao_OK("Sử dụng thành công");
                                    } else {
                                        p.getService().send_box_ThongBao_OK("Băng chưa chiếm được đảo");
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK("Sử dụng sau "
                                            + ((p.clan.cdItem - System.currentTimeMillis()) / 1_000) + "s nữa");
                                }
                            }
                            break;
                            default: {
                                p.getService().send_box_ThongBao_OK("Hiện tại "
                                        + ItemTemplate8.getItemName(id)
                                        + " chưa thể sử dụng, đợi mình 1 thời gian nữa sẽ cập nhật sớm nhất nha");
                                break;
                            }
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Không đủ 1 "
                                + ItemTemplate8.getItemName(id) + " trong hành trang băng");
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng");
                }
                break;
            }
            // case 2:
            case 15: {
                if (p.clan != null) {
                    new model.InputDialog(p, 11, "Đóng góp băng", new String[]{"Nhập số ruby muốn góp"}).startInput();
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn chưa tham gia băng hải tặc");
                }
                break;
            }
            case 3: { // phong chuc
                if (p.clan == null || p.clan.members == null) break;
                String strChat = m2.reader().readUTF();
                byte chucVu = m2.reader().readByte();
                // System.out.println(strChat);
                // System.out.println(chucVu);
                ClanMember getMem = null;
                for (int i = 0; i < p.clan.members.size(); i++) {
                    if (p.clan.members.get(i).name.equals(p.name)
                            && (p.clan.members.get(i).levelInclan == 0
                            || p.clan.members.get(i).levelInclan == 1)) {
                        getMem = p.clan.members.get(i);
                        break;
                    }
                }
                if (chucVu != 0 && getMem != null) {
                    ClanMember mem = null;
                    for (int i = 0; i < p.clan.members.size(); i++) {
                        if (p.clan.members.get(i).name.equals(strChat)) {
                            mem = p.clan.members.get(i);
                            break;
                        }
                    }
                    if (mem != null) {
                        if (mem.levelInclan == 0) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện chức năng này!");
                            return;
                        }
                        if (mem.levelInclan == chucVu) {
                            p.getService().send_box_ThongBao_OK("Đối phương đã có chức vụ này");
                            return;
                        }
                        int numThuyenPho = 0;
                        int numHoaTieu = 0;
                        for (int i = 0; i < p.clan.members.size(); i++) {
                            if (p.clan.members.get(i).levelInclan == 1) {
                                numThuyenPho++;
                            }
                            if (p.clan.members.get(i).levelInclan == 2) {
                                numHoaTieu++;
                            }
                        }
                        if (chucVu == 1 && numThuyenPho >= 2) {
                            p.getService().send_box_ThongBao_OK("Trong băng chỉ có thể tối đa 2 thuyền phó!");
                            return;
                        }
                        if (chucVu == 2 && numHoaTieu >= 1) {
                            p.getService().send_box_ThongBao_OK("Trong băng chỉ có thể tối đa 1 hoa tiêu!");
                            return;
                        }
                        //
                        mem.levelInclan = chucVu;
                        Player p0 = Zone.get_player_by_name_allmap(mem.name);
                        if (p0 != null) {
                            Clan.send_info(p0, false);
                            for (int i = 0; i < p.map.players.size(); i++) {
                                if (!p.map.players.get(i).equals(p0)) {
                                    Clan.send_me_to_other(p0, p.map.players.get(i), false);
                                }
                            }
                            p0.getService().send_box_ThongBao_OK("Bạn được phong thành " + (mem.levelInclan == 1 ? "Thuyền phó"
                                            : (mem.levelInclan == 2 ? "Hoa tiêu" : "thuyền viên")));
                        }
                        for (int i = 0; i < p.clan.members.size(); i++) {
                            Player p0ther
                                    = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                            if (p0ther != null) {
                                update_list_member(p0ther, false);
                                Clan.send_info(p0ther, false);
                            }
                        }
                        //
                        ClanChat chat = new ClanChat();
                        chat.idMem = p.clan.members.get(0).id;
                        chat.name = p.name;
                        chat.str = "phong chức " + mem.name + " thành "
                                + (mem.levelInclan == 1 ? "Thuyền phó"
                                        : (mem.levelInclan == 2 ? "Hoa tiêu" : "thuyền viên"));
                        chat.time = System.currentTimeMillis();
                        chat.typeChat = -3;
                        p.clan.add_chat(chat);
                        //
                        p.clan.send_chat(chat, null);
                        p.getService().send_box_ThongBao_OK("Phong chức " + mem.name + " thành "
                                + (mem.levelInclan == 1 ? "Thuyền phó"
                                        : (mem.levelInclan == 2 ? "Hoa tiêu" : "thuyền viên"))
                                + " thành công");
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng");
                }
                break;
            }
            case 1: { // truc xuat
                if (p.clan == null || p.clan.members == null) break;
                String strChat = m2.reader().readUTF();
                byte chucVu = m2.reader().readByte();
                ClanMember getMem = null;
                for (int i = 0; i < p.clan.members.size(); i++) {
                    if (p.clan.members.get(i).name.equals(p.name)
                            && (p.clan.members.get(i).levelInclan == 0
                            || p.clan.members.get(i).levelInclan == 1)) {
                        getMem = p.clan.members.get(i);
                        break;
                    }
                }
                if (chucVu == 0 && getMem != null) {
                    ClanMember mem = null;
                    for (int i = 0; i < p.clan.members.size(); i++) {
                        if (p.clan.members.get(i).name.equals(strChat)) {
                            mem = p.clan.members.get(i);
                            break;
                        }
                    }
                    if (mem != null) {
                        if (mem.levelInclan == 0) {
                            p.getService().send_box_ThongBao_OK("Không thể thực hiện chức năng này!");
                            return;
                        }
                        p.clan.members.remove(mem);
                        //
                        Player p0 = Zone.get_player_by_name_allmap(mem.name);
                        if (p0 != null) {
                            Clan.send_info(p0, false);
                            for (int i = 0; i < p.map.players.size(); i++) {
                                if (!p.map.players.get(i).equals(p0)) {
                                    Clan.send_me_to_other(p0, p.map.players.get(i), false);
                                }
                            }
                            p0.getService().send_box_ThongBao_OK("Bạn bị trục xuất khỏi băng " + p.clan.name);
                            Message m = new Message(-52);
                            m.writer().writeByte(10);
                            m.writer().writeShort(p0.index_map);
                            p.map.send_msg_all_p(m, p0, true);
                            m.cleanup();
                            p0.clan = null;
                            p0.syncFullPlayerStats();
                        }
                        for (int i = 0; i < p.clan.members.size(); i++) {
                            Player p0ther
                                    = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                            if (p0ther != null) {
                                update_list_member(p0ther, false);
                                Clan.send_info(p0ther, false);
                                //
                                Message m_out = new Message(-52);
                                m_out.writer().writeByte(12);
                                m_out.writer().writeByte(1);
                                m_out.writer().writeUTF(mem.name);
                                p0ther.addmsg(m_out);
                                m_out.cleanup();
                            }
                        }
                        //
                        ClanChat chat = new ClanChat();
                        chat.idMem = p.clan.members.get(0).id;
                        chat.name = p.name;
                        chat.str = mem.name + " bị trục xuất khỏi băng";
                        chat.time = System.currentTimeMillis();
                        chat.typeChat = -2;
                        p.clan.add_chat(chat);
                        //
                        p.clan.send_chat(chat, null);
                        p.getService().send_box_ThongBao_OK(mem.name + " bị trục xuất khỏi băng thành công");
                    }
                }
                break;
            }
            case 4: {
                // roi bang
                int time_h = LocalTime.now().getHourOfDay();
                if ((ZUtil.is_DayofWeek(2) || ZUtil.is_DayofWeek(4) || ZUtil.is_DayofWeek(6))
                 && time_h == 21) {
                p.getService().send_box_ThongBao_OK("Đang trong giờ phó bản khổng lồ không thể rời băng vào lúc 19-20h");
                break;
                }
            
                if (p.clan != null) {
                    Clan oldClan = p.clan;
                    for (int i = 0; i < oldClan.members.size(); i++) {
                        if (oldClan.members.get(i).name.equals(p.name)) {
                            oldClan.members.remove(i);
                            break;
                        }
                    }
                    //
                    Clan.send_info(p, false);
                    for (int i = 0; i < p.map.players.size(); i++) {
                        if (!p.map.players.get(i).equals(p)) {
                            Clan.send_me_to_other(p, p.map.players.get(i), false);
                        }
                    }
                    for (int i = 0; i < oldClan.members.size(); i++) {
                        if (!oldClan.members.get(i).name.equals(p.name)) {
                            Player p0ther
                                    = Zone.get_player_by_name_allmap(oldClan.members.get(i).name);
                            if (p0ther != null) {
                                update_list_member(p0ther, false);
                                Clan.send_info(p0ther, false);
                                //
                                Message m_out = new Message(-52);
                                m_out.writer().writeByte(12);
                                m_out.writer().writeByte(1);
                                m_out.writer().writeUTF(p.name);
                                p0ther.addmsg(m_out);
                                m_out.cleanup();
                            }
                        }
                    }
                    //
                    Message m = new Message(-52);
                    m.writer().writeByte(10);
                    m.writer().writeShort(p.index_map);
                    p.map.send_msg_all_p(m, p, true);
                    m.cleanup();
                    p.getService().send_box_ThongBao_OK("Rời băng " + oldClan.name + " thành công");
                    //
                    if (!oldClan.members.isEmpty()) {
                        ClanChat chat = new ClanChat();
                        chat.idMem = oldClan.members.get(0).id;
                        chat.name = oldClan.members.get(0).name;
                        chat.str = p.name + " rời băng";
                        chat.time = System.currentTimeMillis();
                        chat.typeChat = -2;
                        oldClan.add_chat(chat);
                        //
                        oldClan.send_chat(chat, null);
                    }
                    //
                    p.clan = null;
                    p.syncFullPlayerStats();
                }
                break;
            }
            case 17: { // update list mem
                if (p.clan != null) {
                    update_list_member(p, false);
                }
                break;
            }
            case 0: {
                if (p.clan == null || p.clan.members == null) break;
                String strChat = m2.reader().readUTF();
                // byte chucVu =
                m2.reader().readByte();
                ClanMember mem = null;
                for (int i = 0; i < p.clan.members.size(); i++) {
                    if (p.clan.members.get(i).name.equals(p.name)) {
                        mem = p.clan.members.get(i);
                        break;
                    }
                }
                if (mem != null) {
                    ClanChat chat = new ClanChat();
                    chat.idMem = mem.id;
                    chat.name = p.name;
                    chat.str = strChat;
                    chat.time = System.currentTimeMillis();
                    chat.typeChat = (byte) (mem.levelInclan == 0 ? -1 : -4);
                    p.clan.add_chat(chat);
                    //
                    p.clan.send_chat(chat, null);
                }
                break;
            }
            case 5: {
                if (p.clan == null || p.clan.members == null || p.clan.members.isEmpty()) break;
                String strChat = m2.reader().readUTF();
                // byte chucVu =
                m2.reader().readByte();
                //
                if (p.clan.members.get(0).name.equals(p.name)) {
                    p.clan.thongbao = strChat;
                    for (int i = 0; i < p.clan.members.size(); i++) {
                        Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                        if (p0 != null) {
                            Clan.send_notice(p0, false);
                        }
                    }
                    //
                    ClanChat chat = new ClanChat();
                    chat.idMem = p.clan.members.get(0).id;
                    chat.name = p.name;
                    chat.str = strChat;
                    chat.time = System.currentTimeMillis();
                    chat.typeChat = -3;
                    p.clan.add_chat(chat);
                    //
                    p.clan.send_chat(chat, null);
                }
                break;
            }
            case 6: {
                if (p.clan == null || p.clan.members == null || p.clan.members.isEmpty()) break;
                byte chucVu = m2.reader().readByte();
                // byte id =
                m2.reader().readByte();
                if (p.clan.members.get(0).name.equals(p.name)) {
                    if (p.clan.pointAttri > 0) {
                        if ((p.clan.opAttri[chucVu]
                                + Clan.get_point_trungsinh_plus(p.clan)) >= p.clan.maxAttri) {
                            p.getService().send_box_ThongBao_OK("Hiện tại tiềm năng tối đa là " + p.clan.maxAttri);
                        } else {
                            p.clan.pointAttri--;
                            p.clan.opAttri[chucVu]++;
                            for (int i = 0; i < p.clan.members.size(); i++) {
                                Player p0
                                        = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                                if (p0 != null) {
                                    Clan.send_Attri(p0, false);
                                    p0.update_info_to_all();
                                }
                            }
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Không đủ 1 điểm tiềm năng");
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng");
                }
                break;
            }
            case 7: {
                if (p.clan == null || p.clan.members == null) break;
                ClanMember clan_mem = null;
                for (int i = 0; i < p.clan.members.size(); i++) {
                    if (p.clan.members.get(i).name.equals(p.name)) {
                        clan_mem = p.clan.members.get(i);
                        break;
                    }
                }
                if (clan_mem != null && clan_mem.levelInclan >= 0 && clan_mem.levelInclan <= 2) {
                    // String strChat =
                    m2.reader().readUTF();
                    int id = m2.reader().readInt();
                    ClanChat clan_chat = null;
                    if (p.clan.chat != null) {
                        for (int i = 0; i < p.clan.chat.size(); i++) {
                            if (p.clan.chat.get(i).idChat == id) {
                                clan_chat = p.clan.chat.get(i);
                                break;
                            }
                        }
                    }
                    if (clan_chat != null) {
                        ClanMember mem
                                = p.clan.get_mem_request(clan_chat.str.replace(" xin vào băng", ""));
                        if (mem != null) {
                            // clear chat
                            List<ClanChat> list_remove = new ArrayList<>();
                            if (p.clan.chat != null) {
                                for (int i = 0; i < p.clan.chat.size(); i++) {
                                    if (p.clan.chat.get(i).str.equals(clan_chat.str)) {
                                        list_remove.add(p.clan.chat.get(i));
                                    }
                                }
                                for (int i = 0; i < list_remove.size(); i++) {
                                    for (int j = 0; j < p.clan.members.size(); j++) {
                                        Player p0 = Zone
                                                .get_player_by_name_allmap(p.clan.members.get(j).name);
                                        if (p0 != null) {
                                            p.clan.remove_chat(p0, list_remove.get(i).idChat);
                                        }
                                    }
                                }
                                p.clan.chat.removeAll(list_remove);
                            }
                            //
                            int num_clazz = 0;
                            for (int j = 0; j < p.clan.members.size(); j++) {
                                if (p.clan.members.get(j).clazz == mem.clazz) {
                                    num_clazz++;
                                }
                            }
                            if (num_clazz >= 4) {
                                p.getService().send_box_ThongBao_OK("Băng đã đầy đủ 4/4 " + Clazz.NAME[mem.clazz - 1]);
                                return;
                            }
                            if (p.clan.members.size() >= Clan.get_mem_max(p.clan.level,
                                    p.clan.trungsinh)) {
                                p.getService().send_box_ThongBao_OK("Băng đầy đủ người rồi");
                            } else {
                                //
                                boolean checkCoMat = false;
                                for (int i = 0; i < p.clan.members.size(); i++) {
                                    if (p.clan.members.get(i).name.equals(mem.name)) {
                                        checkCoMat = true;
                                        break;
                                    }
                                }
                                if (!checkCoMat) {
                                    Player p0 = Zone.get_player_by_name_allmap(mem.name);
                                    if (p0 != null && p0.clan != null) {
                                        p0.getService().send_box_ThongBao_OK("Đối phuong đã ở trong băng khác");
                                        return;
                                    }
                                    if (p0 != null) {
                                        p0.clan = p.clan;
                                        mem.id = (short) ClanMember.get_id(p0.clan.members);
                                        p.clan.members.add(mem);
                                         long cooldownTime = System.currentTimeMillis() + (60_000l * 60 * 8);
                                         Clan.HM_TIME_GIFT.put(mem.name, cooldownTime);
                                         historys.zLog.gI().add_log_by_name(mem.name, "CLAN_TIME_GIFT", "time:" + cooldownTime);
                                        //
                                        Clan.send_info(p0, false);
                                        for (int i = 0; i < p.map.players.size(); i++) {
                                            if (!p.map.players.get(i).equals(p0)) {
                                                Clan.send_me_to_other(p0, p.map.players.get(i),
                                                        false);
                                            }
                                        }
                                        p0.getService().send_box_ThongBao_OK("Tham gia băng hải tặc "
                                                + p.clan.name + " thành công");
                                    }
                                    for (int i = 0; i < p.clan.members.size(); i++) {
                                        Player p0ther = Zone.get_player_by_name_allmap(
                                                p.clan.members.get(i).name);
                                        if (p0ther != null) {
                                            update_list_member(p0ther, false);
                                            Clan.send_info(p0ther, false);
                                        }
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK("Đã có mặt trong băng");
                                }
                            }
                            p.clan.mem_request.remove(mem);
                        }
                    }
                }
                break;
            }
            case 16: {
                if (p.clan == null || p.clan.members == null) break;
                ClanMember clan_mem = null;
                for (int i = 0; i < p.clan.members.size(); i++) {
                    if (p.clan.members.get(i).name.equals(p.name)) {
                        clan_mem = p.clan.members.get(i);
                        break;
                    }
                }
                if (clan_mem != null && clan_mem.levelInclan >= 0 && clan_mem.levelInclan <= 2) {
                    // String strChat =
                    m2.reader().readUTF();
                    int id = m2.reader().readInt();
                    ClanChat clan_chat = null;
                    if (p.clan.chat != null) {
                        for (int i = 0; i < p.clan.chat.size(); i++) {
                            if (p.clan.chat.get(i).idChat == id) {
                                clan_chat = p.clan.chat.get(i);
                                break;
                            }
                        }
                    }
                    if (clan_chat != null) {
                        ClanMember mem
                                = p.clan.get_mem_request(clan_chat.str.replace(" xin vào băng", ""));
                        if (mem != null) {
                            // clear chat
                            List<ClanChat> list_remove = new ArrayList<>();
                            if (p.clan.chat != null) {
                                for (int i = 0; i < p.clan.chat.size(); i++) {
                                    if (p.clan.chat.get(i).str.equals(clan_chat.str)) {
                                        list_remove.add(p.clan.chat.get(i));
                                    }
                                }
                                for (int i = 0; i < list_remove.size(); i++) {
                                    for (int j = 0; j < p.clan.members.size(); j++) {
                                        Player p0 = Zone
                                                .get_player_by_name_allmap(p.clan.members.get(j).name);
                                        if (p0 != null) {
                                            p.clan.remove_chat(p0, list_remove.get(i).idChat);
                                        }
                                    }
                                }
                                p.clan.chat.removeAll(list_remove);
                            }
                            p.clan.mem_request.remove(mem);
                        }
                    }
                }
                break;
            }
            case 9: { // update
                if (p.clan != null) {
                    set_data(p, false);
                    send_Attri(p, false);
                    update_list_member(p, false);
                    if (p.clan.chat != null) {
                        for (int i = 0; i < p.clan.chat.size(); i++) {
                            p.clan.send_chat(p.clan.chat.get(i), p);
                        }
                    }
                }
                break;
            }
            case 11: {
                int id = m2.reader().readInt();
                Clan clan = Clan.get_clan_by_id(id);
                if (clan != null) {
                    if (clan.allowRequest == 0) {
                        p.getService().send_box_ThongBao_OK("Băng này đã đầy đủ người");
                    } else {
                        boolean check = true;
                        String request_chat = p.name + " xin vào băng";
                        long time = 0;
                        if (clan.chat != null) {
                            for (int i = 0; i < clan.chat.size(); i++) {
                                if (clan.chat.get(i).typeChat == 1
                                        && clan.chat.get(i).str.equals(request_chat)
                                        && (System.currentTimeMillis()
                                        - clan.chat.get(i).time) < 600_000L - 600_000) {
                                    time = 600_000
                                            - (System.currentTimeMillis() - clan.chat.get(i).time);
                                    check = false;
                                    break;
                                }
                            }
                        }
                        if (check) {
                            ClanMember mem = null;
                            if (clan.mem_request != null) {
                                for (int i = 0; i < clan.mem_request.size(); i++) {
                                    if (clan.mem_request.get(i).name.equals(p.name)) {
                                        mem = clan.mem_request.get(i);
                                        break;
                                    }
                                }
                            }
                            if (mem == null) {
                                mem = new ClanMember();
                                mem.playerId = p.IDPlayer;
                                mem.name = p.name;
                                mem.conghien = 0;
                                mem.donate = 0;
                                mem.gopRuby = 0;
                                mem.numquest = 3;
                                mem.id = 0;
                                mem.hair = (short) p.get_hair();
                                mem.head = (short) p.get_head();
                                mem.hat = p.get_hat();
                                mem.level = p.level;
                                mem.levelInclan = 10;
                                mem.clazz = p.clazz;
                                mem.timeJoinClan = System.currentTimeMillis();
                                long cooldownTime = System.currentTimeMillis() + (60_000l * 60 * 8);
                                Clan.HM_TIME_GIFT.put(mem.name, cooldownTime);
                                historys.zLog.gI().add_log_by_name(mem.name, "CLAN_TIME_GIFT", "time:" + cooldownTime);
                                if (clan.mem_request != null) {
                                    clan.mem_request.add(mem);
                                }
                            }
                            //
                            if (clan.members != null && !clan.members.isEmpty()) {
                                ClanChat chat = new ClanChat();
                                chat.idMem = clan.members.get(0).id;
                                chat.name = clan.members.get(0).name;
                                chat.str = request_chat;
                                chat.time = System.currentTimeMillis();
                                chat.typeChat = 1;
                                clan.add_chat(chat);
                                //
                                clan.send_chat(chat, null);
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Xin vào sau " + (time / 1000) + "s nữa");
                        }
                    }
                }
                break;
            }
            case 10: {
                int id = m2.reader().readInt();
                Player p0 = p.map.get_player_by_id_inmap(id);
                if (p0 != null) {
                    Message m = new Message(-52);
                    m.writer().writeByte(7);
                    m.writer().writeInt(p.index_map);
                    m.writer().writeUTF(p.name);
                    p0.addmsg(m);
                    m.cleanup();
                } else {
                    p.getService().send_box_ThongBao_OK("Nhân vật hiện đang offline hoặc không ở trong map");
                }
                break;
            }
            case 12: {
                if (p.clan != null) {
                    p.getService().send_box_ThongBao_OK("Bạn đang ở trong 1 băng hải tặc khác");
                } else {
                    int id = m2.reader().readInt();
                    Player p0 = p.map.get_player_by_id_inmap(id);
                    if (p0 != null && p0.clan != null) {
                        if (p0.clan.members.size() >= Clan.get_mem_max(p0.clan.level,
                                p0.clan.trungsinh)) {
                            p.getService().send_box_ThongBao_OK("Băng hải tặc đã này đủ người");
                        } else {
                            ClanMember clan_mem = null;
                            for (int i = 0; i < p0.clan.members.size(); i++) {
                                if (p0.clan.members.get(i).name.equals(p0.name)) {
                                    clan_mem = p0.clan.members.get(i);
                                    break;
                                }
                            }
                            if (clan_mem != null && clan_mem.levelInclan >= 0
                                    && clan_mem.levelInclan <= 2) {
                                int num_clazz = 0;
                                for (int j = 0; j < p0.clan.members.size(); j++) {
                                    if (p0.clan.members.get(j).clazz == p.clazz) {
                                        num_clazz++;
                                    }
                                }
                                if (num_clazz >= 4) {
                                    p.getService().send_box_ThongBao_OK("Băng đã đầy đủ 4/4 " + Clazz.NAME[p.clazz - 1]);
                                    return;
                                }
                                //
                                boolean checkCoMat = false;
                                for (int i = 0; i < p0.clan.members.size(); i++) {
                                    if (p0.clan.members.get(i).name.equals(p.name)) {
                                        checkCoMat = true;
                                        break;
                                    }
                                }
                                if (!checkCoMat) {
                                    if (p.clan != null) {
//                                         p.getService().send_box_ThongBao_OK("Đối phuong đã ở trong băng khác");
                                        return;
                                    }
                                    p.clan = p0.clan;
                                    ClanMember mem = new ClanMember();
                                    mem.playerId = p.IDPlayer;
                                    mem.name = p.name;
                                    mem.conghien = 0;
                                    mem.donate = 0;
                                    mem.gopRuby = 32_000;
                                    mem.numquest = 3;
                                    mem.id = (short) ClanMember.get_id(p0.clan.members);
                                    mem.hair = (short) p.get_hair();
                                    mem.head = (short) p.get_head();
                                    mem.hat = p.get_hat();
                                    mem.level = p.level;
                                    mem.levelInclan = 10;
                                    mem.clazz = p.clazz;
                                    mem.timeJoinClan = System.currentTimeMillis();
                                    p0.clan.members.add(mem);
                                    long cooldownTime = System.currentTimeMillis() + (60_000l * 60 * 8);
                                    Clan.HM_TIME_GIFT.put(mem.name, cooldownTime);
                                    historys.zLog.gI().add_log_by_name(mem.name, "CLAN_TIME_GIFT", "time:" + cooldownTime);
                                    //
                                    Clan.send_info(p, false);
                                    for (int i = 0; i < p.map.players.size(); i++) {
                                        if (!p.map.players.get(i).equals(p)) {
                                            Clan.send_me_to_other(p, p.map.players.get(i), false);
                                        }
                                    }
                                    for (int i = 0; i < p0.clan.members.size(); i++) {
                                        if (!p0.clan.members.get(i).name.equals(p.name)) {
                                            Player p0ther = Zone.get_player_by_name_allmap(
                                                    p0.clan.members.get(i).name);
                                            if (p0ther != null) {
                                                update_list_member(p0ther, false);
                                                Clan.send_info(p0ther, false);
                                            }
                                        }
                                    }
                                    p.getService().send_box_ThongBao_OK("Tham gia băng hải tặc "
                                            + p0.clan.name + " thành công");
                                } else {
                                    p.getService().send_box_ThongBao_OK("Đã có mặt trong băng");
                                }
                            } else {
                                p.getService().send_box_ThongBao_OK("Đối phương không phải là đội trưởng");
                            }
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Nhân vật hiện đang offline hoặc không ở trong map");
                    }
                }
                break;
            }
        }
    }

    private void remove_chat(Player p, int idChat) throws IOException {
        Message m = new Message(-52);
        m.writer().writeByte(11);
        m.writer().writeShort(idChat);
        p.addmsg(m);
        m.cleanup();
    }

    public ClanMember get_mem_request(int playerId, String str) {
        if (this.mem_request == null) return null;
        for (int i = 0; i < this.mem_request.size(); i++) {
            ClanMember cm = this.mem_request.get(i);
            if (cm != null) {
                if (playerId > 0 && cm.playerId == playerId) return cm;
                if (str != null && cm.name.equalsIgnoreCase(str)) return cm;
            }
        }
        return null;
    }

    public ClanMember get_mem_request(String str) {
        return get_mem_request(-1, str);
    }

    static void send_notice(Player p, boolean addcache) throws IOException {
        if (p != null && p.clan != null && p.conn != null) {
            Message m = new Message(-52);
            m.writer().writeByte(14);
            m.writer().writeUTF(p.clan.thongbao != null ? p.clan.thongbao : "");
            if (addcache) {
                p.msgs.add(m);
            } else {
                p.addmsg(m);
            }
            m.cleanup();
        }
    }

    public void send_chat(ClanChat chat, Player p) throws IOException {
        if (chat == null || this.members == null) return;
        for (int i = 0; i < this.members.size(); i++) {
            ClanMember cm = this.members.get(i);
            if (cm == null) continue;
            Player p0 = Zone.get_player_by_name_allmap(cm.name);
            if (p0 != null && (p == null || p0.equals(p))) {
                Message m = new Message(-52);
                m.writer().writeByte(8);
                m.writer().writeByte(chat.typeChat); // -1, -4 khac color, 1: xin vao
                m.writer().writeShort(chat.idChat);
                m.writer().writeShort(chat.idMem);
                m.writer().writeUTF(chat.name != null ? chat.name : "");
                m.writer().writeUTF(chat.str != null ? chat.str : "");
                m.writer().writeLong(chat.time);
                p0.addmsg(m);
                m.cleanup();
            }
        }
    }

    public synchronized void add_chat(ClanChat chat) {
        if (chat == null) return;
        if (this.chat == null) {
            this.chat = new ArrayList<>();
        }
        int id_get = 0;
        for (int i = 0; i < this.chat.size(); i++) {
            ClanChat cc = this.chat.get(i);
            if (cc != null) {
                id_get = Math.max(id_get, cc.idChat);
            }
        }
        chat.idChat = id_get + 1;
        this.chat.add(chat);
    }

    public static void set_data(Player p, boolean b) throws IOException {
        if (p.clan != null) {
            short clanLevel = (short) Math.max(1, p.clan.level);
            p.clan.level = clanLevel;
            String captainName = p.clan.getLeaderName();
            if (captainName == null || captainName.isEmpty()) captainName = p.name;
            int memCount = (p.clan.members != null) ? p.clan.members.size() : 1;
            int maxMem = Clan.get_mem_max(clanLevel, p.clan.trungsinh);

            Message m = new Message(-52);
            m.writer().writeByte(2);
            m.writer().writeShort(p.clan.icon >= 0 ? p.clan.icon : 1);
            m.writer().writeUTF(captainName); // captain name
            m.writer().writeShort(clanLevel);
            m.writer().writeInt(p.clan.xp);
            m.writer().writeInt(Clan.get_xp_max(clanLevel, p.clan.trungsinh));
            m.writer().writeByte(memCount);
            m.writer().writeByte(maxMem);
            m.writer().writeInt(Clan.get_rank(p.clan)); // rank
            m.writer().writeUTF(p.clan.thongbao != null ? p.clan.thongbao : "");
            m.writer().writeByte(p.clan.trungsinh); // trung sinh
            m.writer().writeInt(p.clan.countAction); // count Action
            if (b) {
                p.msgs.add(m);
            } else {
                p.addmsg(m);
            }
            m.cleanup();
        }
    }

    public static int get_mem_max(short level, int trungsinh) {
        if (level <= 0) level = 1;
        int max = 10;
        if (trungsinh == 0) {
            if (level == 1) {
                max = 5;
            } else if (level == 2) {
                max = 6;
            } else if (level == 3) {
                max = 7;
            } else if (level == 4) {
                max = 8;
            } else if (level == 5) {
                max = 9;
            }
        } else {
            max += trungsinh;
        }
        if (max > 13) {
            max = 13;
        }
        return max;
    }

    public static void update_list_member(Player p, boolean b) throws IOException {
        if (p.clan != null) {
            int ver_ = p.getConnVersionInt();
            Message m = new Message(-52);
            m.writer().writeByte(3);
            int memSize = (p.clan.members != null) ? p.clan.members.size() : 0;
            m.writer().writeByte(memSize);
            for (int i = 0; i < memSize; i++) {
                ClanMember mem = p.clan.members.get(i);
                if (mem == null) continue;
                Player p0 = Zone.get_player_by_name_allmap(mem.name);
                if (p0 != null) {
                    mem.head = (short) p0.get_head();
                    mem.hair = (short) p0.get_hair();
                    mem.hat = p0.get_hat();
                    mem.name = p0.name;
                    mem.level = (short) Math.max(1, p0.level);
                } else if (mem.level <= 0) {
                    mem.level = 1;
                }
                m.writer().writeShort(mem.id);
                m.writer().writeUTF(mem.name != null ? mem.name : "");
                m.writer().writeShort(Math.max((short) 1, mem.level));
                m.writer().writeByte(mem.levelInclan);
                m.writer().writeShort(mem.donate);
                m.writer().writeShort(mem.gopRuby);
                m.writer().writeShort(mem.numquest);
                if (ver_ >= 111) {
                    m.writer().writeInt(mem.conghien);
                }
                //
                m.writer().writeShort(mem.head);
                m.writer().writeShort(mem.hair);
                m.writer().writeShort(mem.hat);
                m.writer().writeByte(p0 != null ? 1 : 0);
            }
            if (b) {
                p.msgs.add(m);
            } else {
                p.addmsg(m);
            }
            m.cleanup();
        }
    }

    static void send_Attri(Player p, boolean b) throws IOException {
        if (p == null || p.clan == null) return;
        Message m = new Message(-52);
        m.writer().writeByte(4);
        m.writer().writeShort(p.clan.maxAttri);
        m.writer().writeShort(p.clan.pointAttri);
        for (int i = 0; i < 5; i++) {
            short val = (p.clan.opAttri != null && i < p.clan.opAttri.length) ? p.clan.opAttri[i] : 0;
            m.writer().writeShort(val + Clan.get_point_trungsinh_plus(p.clan));
        }
        if (b) {
            p.msgs.add(m);
        } else {
            p.addmsg(m);
        }
        m.cleanup();
    }

    public static int get_point_trungsinh_plus(Clan clan) {
        if (clan == null) return 0;
        return clan.trungsinh * 4;
    }

    public static int get_rank(Clan clan) {
        for (int i = 0; i < Clan.BXH.size(); i++) {
            if (Clan.BXH.get(i).equals(clan.name)) {
                return (i + 1);
            }
        }
        return 9999;
    }

    public synchronized static int get_clan_id() {
        int result = -1;
        for (int i = 0; i < ENTRY.size(); i++) {
            result = Math.max(result, ENTRY.get(i).id);
        }
        result++;
        return result;
    }

    public void maxOutForTest() {
        this.trungsinh = 6;
        this.level = 15;
        this.maxAttri = 50;
        this.pointAttri = 0;
        this.opAttri = new short[]{26, 26, 26, 26, 26}; // 26 + (6 * 4) = 50 max stat
        this.xp = Clan.get_xp_max(this.level, this.trungsinh);
        this.beri = 2_000_000_000;
        this.ruby = 2_000_000_000;
        this.countAction = 999_999;
        this.numPvp = 5;

        // Kho huy hiệu và xu hành trình không bị ghi đè để bảo toàn dữ liệu sở hữu của clan
        if (this.hanhtrinh == null) {
            this.hanhtrinh = new ArrayList<>();
            this.hanhtrinh.add(0);
        } else if (this.hanhtrinh.isEmpty()) {
            this.hanhtrinh.add(0);
        }

        // Full Clan Buffs (Bùa EXP 100%, Bùa HP 25%, Bùa MP 25%, Bùa Tổng Hợp 25%)
        if (this.buff == null) {
            this.buff = new CopyOnWriteArrayList<>();
        }
        this.buff.clear();
        this.buff.add(new EffTemplate((byte) 0, 100, System.currentTimeMillis() + 86400000L * 7));
        this.buff.add(new EffTemplate((byte) 1, 100, System.currentTimeMillis() + 86400000L * 7));
        this.buff.add(new EffTemplate((byte) 2, 25,  System.currentTimeMillis() + 86400000L * 7));
        this.buff.add(new EffTemplate((byte) 3, 25,  System.currentTimeMillis() + 86400000L * 7));
        this.buff.add(new EffTemplate((byte) 4, 25,  System.currentTimeMillis() + 86400000L * 7));

        // Full kho đồ / Vật phẩm bang hội
        if (this.list_it == null) {
            this.list_it = new ArrayList<>();
        }
        if (template.ItemTemplate8.ENTRYS != null) {
            for (template.ItemTemplate8 it8 : template.ItemTemplate8.ENTRYS) {
                if (it8 != null) {
                    ItemBag47 existing = null;
                    for (ItemBag47 bag : this.list_it) {
                        if (bag != null && bag.id == it8.id) {
                            existing = bag;
                            break;
                        }
                    }
                    if (existing != null) {
                        existing.quant = 9999;
                    } else {
                        ItemBag47 bag = new ItemBag47();
                        bag.category = 4;
                        bag.id = (short) it8.id;
                        bag.quant = 9999;
                        this.list_it.add(bag);
                    }
                }
            }
        }

        // Max cống hiến thành viên
        if (this.members != null) {
            for (ClanMember mem : this.members) {
                if (mem != null) {
                    mem.conghien = 2_000_000_000;
                    mem.gopRuby = 32_000;
                    mem.numquest = 3;
                }
            }
        }
    }

    public static void applyMaxTestModeAllClans() {
        // Không tự động gán max chỉ số/huy hiệu để bảo toàn tiến trình của clan
    }

    @SuppressWarnings("unchecked")
    public static boolean create_new_clan(Clan clan) {
        if (clan != null) {
            if (clan.hanhtrinh == null) {
                clan.hanhtrinh = new ArrayList<>();
            }
            if (clan.hanhtrinh.isEmpty()) {
                clan.hanhtrinh.add(0);
            }
        }
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DbManager.gI().getConnect();
            ps = connection.prepareStatement(
                    "INSERT INTO `clan` (`id`, `name`, `info`, `notice`, `member`, `item`, `xp`, `buff`,`hanhtrinh`) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.clearParameters();
            ps.setInt(1, clan.id);
            ps.setNString(2, clan.name);
            JSONArray js = new JSONArray();
            js.add(clan.icon);
            js.add(clan.level);
            js.add(clan.xp);
            js.add(clan.maxAttri);
            js.add(clan.pointAttri);
            js.add(clan.trungsinh);
            js.add(clan.countAction);
            js.add(clan.ruby);
            js.add(clan.beri);
            js.add(clan.allowRequest);
            JSONArray js2 = new JSONArray();
            for (int i = 0; i < clan.opAttri.length; i++) {
                js2.add(clan.opAttri[i]);
            }
            js.add(js2);
            ps.setNString(3, js.toJSONString());
            js.clear();
            js2.clear();
            ps.setNString(4, clan.thongbao);
            for (int i = 0; i < clan.members.size(); i++) {
                JSONArray js_in = new JSONArray();
                ClanMember mem = clan.members.get(i);
                js_in.add(mem.name);
                js_in.add(mem.level);
                js_in.add(mem.levelInclan);
                js_in.add(mem.donate);
                js_in.add(mem.gopRuby);
                js_in.add(mem.numquest);
                js_in.add(mem.conghien);
                js_in.add(mem.head);
                js_in.add(mem.hair);
                js_in.add(mem.hat);
                js_in.add(mem.clazz);
                js.add(js_in);
            }
            ps.setNString(5, js.toJSONString());
            JSONArray js_it = new JSONArray();
            for (int j = 0; j < clan.list_it.size(); j++) {
                JSONArray js_in = new JSONArray();
                js_in.add(clan.list_it.get(j).id);
                js_in.add(clan.list_it.get(j).quant);
                js_it.add(js_in);
            }
            ps.setNString(6, js_it.toJSONString());
            long xp_total = clan.xp;
            for (int j = 1; j < clan.level; j++) {
                xp_total += Clan.get_xp_max(j, clan.trungsinh);
            }
            xp_total += (2_400_000L * clan.trungsinh);
            ps.setLong(7, xp_total);
            JSONArray js_buff = new JSONArray();
            if (clan.buff != null) {
                for (EffTemplate b : clan.buff) {
                    if (b != null) {
                        JSONArray js_in = new JSONArray();
                        js_in.add(b.id);
                        js_in.add(b.param);
                        js_in.add(b.time);
                        js_buff.add(js_in);
                    }
                }
            }
            ps.setNString(8, js_buff.toJSONString());
            JSONArray js_ht = new JSONArray();
            for (int j = 0; j < clan.hanhtrinh.size(); j++) {
                js_ht.add(clan.hanhtrinh.get(j));
            }
            ps.setNString(9, js_ht.toJSONString());
            js.clear();
            ps.executeUpdate();
        } catch (SQLException e) {
//             e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Clan.add_new_clan(clan);
        return true;
    }

    public synchronized static void add_new_clan(Clan clan) {
        ENTRY.add(clan);
    }

    public synchronized static void delete_clan(Clan clan) {
        ENTRY.remove(clan);
    }

    public static void delete_clan_db(int clanId) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DbManager.gI().getConnect();
            if (connection == null) return;
            ps = connection.prepareStatement("DELETE FROM `clan` WHERE `id` = ?");
            ps.setInt(1, clanId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static int get_xp_max(int level, int ts) {
        if (level <= 0) level = 1;
        return 10000 * level;
    }

    static int get_ngoc_upgrade(short level, int trungsinh) {
        if (level <= 0) level = 1;
        return 100 * level;
    }

    static int get_vang_upgrade(short level) {
        if (level <= 0) level = 1;
        return level * 5_000;
    }

    public synchronized static void flush(Clan clan) {
        if (clan == null) return;
        Clan.update();
    }

    @SuppressWarnings("unchecked")
    public synchronized static void update() {
        if (Clan.ENTRY == null || Clan.ENTRY.isEmpty()) {
            return;
        }
        Connection connection = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psRank = null;
        ResultSet rs = null;
        try {
            connection = DbManager.gI().getConnect();
            if (connection == null) return;
            connection.setAutoCommit(false);

            psUpdate = connection.prepareStatement(
                    "UPDATE `clan` SET `info` = ?, `member` = ?, `item` = ?, `xp` = ?,"
                    + " `buff` = ?, `hanhtrinh` = ?, `notice` = ? WHERE `id` = ?");

            for (int i = 0; i < Clan.ENTRY.size(); i++) {
                Clan clan = Clan.ENTRY.get(i);
                if (clan == null) continue;

                JSONArray js = new JSONArray();
                js.add(clan.icon);
                js.add(clan.level);
                js.add(clan.xp);
                js.add(clan.maxAttri);
                js.add(clan.pointAttri);
                js.add(clan.trungsinh);
                js.add(clan.countAction);
                js.add(clan.ruby);
                js.add(clan.beri);
                js.add(clan.allowRequest);
                JSONArray js2 = new JSONArray();
                for (int i2 = 0; i2 < clan.opAttri.length; i2++) {
                    js2.add(clan.opAttri[i2]);
                }
                js.add(js2);
                psUpdate.setNString(1, js.toJSONString());
                js.clear();

                for (int i2 = 0; i2 < clan.members.size(); i2++) {
                    JSONArray js_in = new JSONArray();
                    ClanMember mem = clan.members.get(i2);
                    if (mem != null) {
                        js_in.add(mem.name);
                        js_in.add(mem.level);
                        js_in.add(mem.levelInclan);
                        js_in.add(mem.donate);
                        js_in.add(mem.gopRuby);
                        js_in.add(mem.numquest);
                        js_in.add(mem.conghien);
                        js_in.add(mem.head);
                        js_in.add(mem.hair);
                        js_in.add(mem.hat);
                        js_in.add(mem.clazz);
                        js_in.add(mem.timeJoinClan);
                        js_in.add(mem.playerId);
                        js.add(js_in);
                    }
                }
                psUpdate.setNString(2, js.toJSONString());
                js.clear();

                for (int j = 0; j < clan.list_it.size(); j++) {
                    JSONArray js_in = new JSONArray();
                    js_in.add(clan.list_it.get(j).id);
                    js_in.add(clan.list_it.get(j).quant);
                    js.add(js_in);
                }
                psUpdate.setNString(3, js.toJSONString());
                js.clear();

                long xp_total = clan.xp;
                if (xp_total > Clan.get_xp_max(clan.level, clan.trungsinh)) {
                    xp_total = Clan.get_xp_max(clan.level, clan.trungsinh);
                }
                for (int j = 1; j < clan.level; j++) {
                    xp_total += Clan.get_xp_max(j, clan.trungsinh);
                }
                xp_total += (2_400_000 * clan.trungsinh);
                psUpdate.setLong(4, xp_total);

                if (clan.buff != null) {
                    for (EffTemplate b : clan.buff) {
                        if (b != null) {
                            JSONArray js_in = new JSONArray();
                            js_in.add(b.id);
                            js_in.add(b.param);
                            js_in.add(b.time);
                            js.add(js_in);
                        }
                    }
                }
                psUpdate.setNString(5, js.toJSONString());
                js.clear();

                js = new JSONArray();
                for (int j = 0; j < clan.hanhtrinh.size(); j++) {
                    js.add(clan.hanhtrinh.get(j));
                }
                psUpdate.setString(6, js.toJSONString());
                psUpdate.setNString(7, clan.thongbao != null ? clan.thongbao : "");
                psUpdate.setInt(8, clan.id);
                psUpdate.addBatch();
            }
            psUpdate.executeBatch();
            connection.commit();

            // Refresh Clan Ranking
            List<String> newBxh = new ArrayList<>();
            psRank = connection.prepareStatement("SELECT `name` FROM `clan` ORDER BY `xp` DESC");
            rs = psRank.executeQuery();
            while (rs.next()) {
                newBxh.add(rs.getString("name"));
            }
            Clan.BXH.clear();
            Clan.BXH.addAll(newBxh);
        } catch (SQLException e) {
            if (connection != null) {
                try { connection.rollback(); } catch (SQLException ignored) {}
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (psRank != null) psRank.close();
                if (psUpdate != null) psUpdate.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Saved dynamically to user_logs under type 'CLAN_TIME_GIFT'
    }

    public static int get_ngoc_icon(short id) {
        int result = 50;
        if (id >= 293 && id < 370) {
            result = 200;
        }
        if (id >= 500 && id <= 522) {
            result = 10_000;
        }
        return result;
    }

    public synchronized void update_xp(int num) {
        // if (Manager.gI().exp > 0) {
        this.xp += num;
        // }
    }

    public synchronized void update_beri(long num) {
        if (Manager.gI().isTestMode()) {
            this.beri = 2_000_000_000;
            return;
        }
        if ((num + this.beri) <= 2_000_000_000L) {
            this.beri += num;
        }
    }

    public synchronized void update_ruby(long num) {
        if (Manager.gI().isTestMode()) {
            this.ruby = 2_000_000_000;
            return;
        }
        if ((num + this.ruby) <= 2_000_000_000L) {
            this.ruby += num;
        }
    }

    public void send_inventory(Player p, boolean b) throws IOException {
        if (p == null || p.clan == null) return;
        if (p.clan.list_it == null) {
            p.clan.list_it = new ArrayList<>();
        }
        // Đồng bộ template ItemTemplate8 trước để client chắc chắn có dữ liệu Potion Clan
        store.ClanShop.sendSyncClanPotionTemplate(p, b);

        List<ItemBag47> snapshot;
        synchronized (p.clan.list_it) {
            p.clan.list_it.removeIf(it -> it == null || it.quant <= 0);
            snapshot = new ArrayList<>(p.clan.list_it);
        }

        Message m = new Message(-52);
        m.writer().writeByte(19);
        m.writer().writeByte(0);
        m.writer().writeByte(8);
        m.writer().writeShort(snapshot.size()); // Sửa chuẩn writeShort (2 bytes) theo đúng Client
        for (ItemBag47 it : snapshot) {
            m.writer().writeShort(it.id);
            m.writer().writeShort(it.quant);
        }
        if (b) {
            p.msgs.add(m);
        } else {
            p.addmsg(m);
        }
        m.cleanup();
    }

    public boolean check_buff(int i) {
        if (this.buff != null) {
            for (EffTemplate b : this.buff) {
                if (b != null && b.id == i && b.time > System.currentTimeMillis()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void chat_on_board(short id, String name, String s, int typeChat) throws IOException {
        ClanChat chat = new ClanChat();
        chat.idMem = id;
        chat.name = name;
        chat.str = s;
        chat.time = System.currentTimeMillis();
        chat.typeChat = (byte) typeChat;
        this.add_chat(chat);
        //
        this.send_chat(chat, null);
    }



    public void saveGift(String name, int type, int id, int num) {
        synchronized (this.listGift) {
            List<GiftBox> listSelect = new ArrayList<>();
            if (this.listGift.containsKey(name)) {
                listSelect.addAll(this.listGift.get(name));
            }
            GiftBox.addGift(listSelect, type, id, num);
            this.listGift.put(name, listSelect);
        }
    }
    
    public void saveGift2(String name, int type, int id, int num) {
        synchronized (this.listGift) {
            List<GiftBox> listSelect = new ArrayList<>();
            if (this.listGift.containsKey(name)) {
                listSelect.addAll(this.listGift.get(name));
            }
            GiftBox.addGift(listSelect, type, id, num);
            this.listGift.put(name, listSelect);
        }
    }

    public void sendGift(Player p) throws IOException {
        if (p.isDe || p instanceof model.DeTu) return;
        synchronized (this.listGift) {
            List<GiftBox> listSelect = new ArrayList<>();
            if (this.listGift.containsKey(p.name)) {
                listSelect.addAll(this.listGift.get(p.name));
            }
            if (!listSelect.isEmpty()) {
                core.RewardService.sendGiftOrMail(p, 1, "Phần thưởng chiếm đảo", "Nhận được", listSelect, true);
                this.listGift.remove(p.name);
            }
        }
    }
}
