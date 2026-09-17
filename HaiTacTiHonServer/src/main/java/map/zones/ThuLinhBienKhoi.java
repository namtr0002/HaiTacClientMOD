package map.zones;

import clan.Clan;
import clan.ClanChat;
import clan.ClanMember;
import model.Player;
import core.Manager;
import core.ZUtil;
import map.Zone;
import mob.Mob;
import network.Message;
import network.Service;
import template.GiftBox;
import zabstracts.AbsClanDungeon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ThuLinhBienKhoi — Phó bản Bang hội & Sự kiện Thủ Lĩnh Biển Khơi.
 *
 * <p>Mỗi instance đại diện cho <b>1 Bang hội</b> đang tham gia phó bản.
 * Lưu trong {@link Zone#map_thuLinhBienKhoi} của zone riêng của clan đó.
 *
 * <pre>
 *  PER-CLAN (instance fields):
 *    flag  — vùng biển đang chiến đấu (4=Đông, 5=Tây, 6=Nam, 7=Bắc)
 *    point — điểm tích luỹ của clan này
 *    buff  — loại bùa lợi sau khi hạ boss (0=tăng tấn công, 1=giảm CD)
 *    time  — thời điểm hết hiệu lực buff
 *
 *  GLOBAL (static fields):
 *    point1-4, flag1-4 — điểm & số clan theo từng vùng toàn server
 *    mob               — boss tại map 179
 *    TIME              — deadline phiên hiện tại
 * </pre>
 */
public class ThuLinhBienKhoi extends AbsClanDungeon {

    // ======================================================
    //  INSTANCE FIELDS — Per-clan state (1 object = 1 clan)
    // ======================================================

    /** Vùng biển đang chiến đấu: 4=Đông, 5=Tây, 6=Nam, 7=Bắc */
    public int flag;

    /** Tổng điểm tích luỹ của clan này trong phiên hiện tại */
    public int point;

    /** Loại buff sau khi hạ boss: 0=tăng tấn công, 1=giảm hồi sinh */
    public int buff;

    /** Thời điểm hết buff (epoch millis) */
    public long buffExpire;

    // ======================================================
    //  GLOBAL STATIC STATE — Event-wide, shared toàn server
    // ======================================================

    private static long TIME = System.currentTimeMillis() + 60_000L * 60;

    public static int point1 = 0;
    public static int point2 = 0;
    public static int point3 = 0;
    public static int point4 = 0;

    public static int flag1 = 0;
    public static int flag2 = 0;
    public static int flag3 = 0;
    public static int flag4 = 0;

    /** Boss tại map 179 — gán từ Manager.java lúc khởi động server */
    public static Mob mob;

    // ======================================================
    //  CONSTRUCTOR
    // ======================================================

    public ThuLinhBienKhoi(int flag) {
        this.flag = flag;
        this.point = 0;
        this.buff = 0;
        this.buffExpire = 0;
        this.maps = new CopyOnWriteArrayList<>();
        this.mobs = new CopyOnWriteArrayList<>();
        this.startTime = System.currentTimeMillis();
        this.time = System.currentTimeMillis() + 60_000L * 60;
    }

    // ======================================================
    //  ABSDUNGEON OVERRIDES
    // ======================================================

    @Override
    public void create() {
        // khởi tạo map riêng cho clan nếu cần
    }

    @Override
    public void update(Zone zone) throws IOException {
        if (isTimedOut()) {
            onTimeout();
        }
    }

    @Override
    public void onTimeout() throws IOException {
        for (Zone z : maps) {
            if (z == null || z.players == null) continue;
            for (Player p : new ArrayList<>(z.players)) {
                leave(p);
            }
        }
    }

    @Override
    public String canJoin(Player p) {
        if (!isOpen()) return "Hoạt động Thủ Lĩnh Biển Khơi chưa đến giờ mở cửa (19h T2, T4, T6)!";
        if (p.clan == null) return "Bạn cần gia nhập Bang Hội để tham gia!";
        if (p.get_key_boss() < 2) return "Bạn không đủ 2 Chìa khóa phó bản để tham gia!";
        return null;
    }

    @Override public JoinMode getJoinMode()  { return JoinMode.YES_NO; }
    @Override public String   getJoinTitle() { return "Thủ Lĩnh Biển Khơi"; }
    @Override public String   getJoinDesc()  { return "Để tham gia phó bản Thủ Lĩnh Biển Khơi mỗi thành viên cần 2 Chìa khoá phó bản. Bạn có muốn tham gia không?"; }
    @Override public int      getDungeonId() { return -70; }
    @Override public int      getKeyCost()   { return 2; }
    @Override public int      getKeyItemId() { return -1; } // Tiêu hao key_boss trực tiếp

    // ======================================================
    //  SCHEDULE CHECK
    // ======================================================

    public static boolean isForceOpen = false;

    public static boolean isOpen() {
        if (isForceOpen) return true;
        return activities.TimedDungeonManager.gI().isDungeonOpen("THU_LINH_BIEN_KHOI");
    }

    // ======================================================
    //  GLOBAL TICK — gọi từ scheduler mỗi giây
    // ======================================================

    public static synchronized void update(int min, int sec) throws IOException {
        if (sec % 10 == 0) {
            bot.BotThuLinhBienKhoi.checkAndMaintainBots();
        }

        if (min == 0 && sec == 0) {
            Manager.gI().chatKTG(0, "Hoạt động Thủ Lĩnh Biển Khơi (4 Vùng Biển) đã chính thức bắt đầu! Hãy đến NPC Mihawk để tham gia!", 5);
            TIME   = System.currentTimeMillis() + 60_000L * 60;
            point1 = 0; point2 = 0; point3 = 0; point4 = 0;
            flag1  = 0; flag2  = 0; flag3  = 0; flag4  = 0;
            if (mob != null) mob.isdie = true;

            // Phân phối bot ra 4 vùng biển và map Boss/Quái
            bot.BotThuLinhBienKhoi.dispatchBots();

        } else if (min == 59 && sec == 59) {
            Manager.gI().chatKTG(0, "Thời gian Phó bản Thủ lĩnh biển khơi đã kết thúc!", 5);
            bot.BotThuLinhBienKhoi.clearBots();
            new Thread(() -> {
                try { Thread.sleep(5_000); } catch (InterruptedException ignored) {}
                sendGiftTLBK();
            }, "TLBK-EndReward").start();

        } else if (min % 5 == 0 && sec == 0) {
            spawnBoss();
        }
    }

    private static void spawnBoss() throws IOException {
        if (mob != null) {
            mob.isdie = false;
            mob.hp = mob.hp_max;
            mob.id_target = -1;
            if (mob.map != null) {
                try {
                    Message m_add = new Message(4);
                    m_add.writer().writeShort(mob.index);
                    m_add.writer().writeShort(mob.mtemplate.mob_id);
                    m_add.writer().writeShort(mob.x);
                    m_add.writer().writeShort(mob.y);
                    m_add.writer().writeShort(mob.level);
                    m_add.writer().writeInt(mob.hp);
                    m_add.writer().writeInt(mob.hp_max);
                    short skill0 = (mob.mtemplate != null && mob.mtemplate.skill != null && mob.mtemplate.skill.length > 0) ? mob.mtemplate.skill[0] : 0;
                    m_add.writer().writeShort(skill0);
                    m_add.writer().writeShort((short) Mob.TIME_RESPAWN);
                    m_add.writer().writeByte(mob.mtemplate != null ? mob.mtemplate.typemonster : 0);
                    m_add.writer().writeByte(mob.boss_inf != null ? mob.boss_inf.levelBoss : 0);
                    mob.map.send_msg_all_p(m_add, null, true);
                    m_add.cleanup();

                    mob.map.getService().move(mob.index, mob.x, mob.y);
                    Manager.gI().chatKTG(0, "Boss Quái Thú Biển Khơi đã xuất hiện tại Vùng Biển Trung Tâm (Map 179)! Các Băng hãy mau đến tiêu diệt!", 5);
                } catch (Exception ignored) {}
            }
        }
    }

    // ======================================================
    //  ON MOB DIE — gọi từ Zone.java
    // ======================================================

    /**
     * Xử lý toàn bộ logic khi quái/boss bị hạ trong map 179, 180, 184.
     * Zone.java chỉ cần 1 lời gọi:
     * <pre>ThuLinhBienKhoi.onMobDie(p, mob_target, this);</pre>
     */
    public static void onMobDie(Player p, Mob mob_target, Zone zone) throws IOException {
        if (p == null || zone == null) return;
        int flag = (p.type_pk >= 4 && p.type_pk <= 7) ? p.type_pk : 4;
        if (p.clan != null && p.clan.map_create != null && p.clan.map_create.map_thuLinhBienKhoi != null) {
            flag = p.clan.map_create.map_thuLinhBienKhoi.flag;
        }

        int mapId = zone.template.id;

        if (mapId == 180 || mapId == 184) {
            // Mob thường -> +1 điểm
            updatePoint(p.clan, flag, 1);

        } else if (mapId == 179) {
            // Boss -> +500 điểm + buff lợi 60s + chat clan
            updatePoint(p.clan, flag, 500);
            zone.remove_obj(mob_target.index, 1);

            if (p.clan != null && p.clan.map_create != null && p.clan.map_create.map_thuLinhBienKhoi != null) {
                ThuLinhBienKhoi tlbk = p.clan.map_create.map_thuLinhBienKhoi;
                tlbk.buff       = ZUtil.random(2);
                tlbk.buffExpire = System.currentTimeMillis() + 60_000L;

                ClanMember me = findMember(p.clan, p.name);
                if (me != null) {
                    String desc = tlbk.buff == 0 ? "tăng tấn công" : "giảm thời gian hồi sinh";
                    ClanChat chat = new ClanChat();
                    chat.idMem    = me.id;
                    chat.name     = me.name;
                    chat.str      = "Tiêu diệt quái thú nhận 500đ và bùa lợi " + desc + " trong 60s";
                    chat.time     = System.currentTimeMillis();
                    chat.typeChat = -4;
                    p.clan.add_chat(chat);
                    p.clan.send_chat(chat, null);
                }
            }
        }
    }

    // ======================================================
    //  SEND INFO — gửi UI bảng điểm cho player
    // ======================================================

    public static void sendInfo(Player p) throws IOException {
        if (p == null || p.map == null || !isOpen()) return;
        if (p.clan == null || p.clan.map_create == null
                || p.clan.map_create.map_thuLinhBienKhoi == null) return;

        int mapId = p.map.template.id;
        if (mapId >= 178 && mapId <= 184) {
            p.addmsg(buildScoreMessage());
            if (TIME <= System.currentTimeMillis()) {
                TIME = System.currentTimeMillis() + 60_000L * 60;
            }
            p.getService().send_time_cool_down(TIME, "Thủ Lĩnh Biển Khơi", 0);
        }
    }

    private static Message buildScoreMessage() throws IOException {
        Message m = new Message(62);
        m.writer().writeByte(4);
        m.writer().writeUTF("Biển Đông");  m.writer().writeShort(-1); m.writer().writeInt(point1);
        m.writer().writeUTF("Biển Tây");   m.writer().writeShort(-1); m.writer().writeInt(point2);
        m.writer().writeUTF("Biển Nam");   m.writer().writeShort(-1); m.writer().writeInt(point3);
        m.writer().writeUTF("Biển Bắc");   m.writer().writeShort(-1); m.writer().writeInt(point4);
        return m;
    }

    private static long getTimeRemain() {
        return Math.max(0, TIME - System.currentTimeMillis());
    }

    // ======================================================
    //  UPDATE POINT — cộng điểm + broadcast
    // ======================================================

    public static synchronized void updatePoint(Clan clan, int flag, int num) throws IOException {
        if (clan == null || clan.map_create == null
                || clan.map_create.map_thuLinhBienKhoi == null) return;

        clan.map_create.map_thuLinhBienKhoi.point += num;
        switch (flag) {
            case 4: point1 += num; break;
            case 5: point2 += num; break;
            case 6: point3 += num; break;
            default: point4 += num; break;
        }

        Message m = buildScoreMessage();
        Zone[] z179 = Zone.getMapByID(179);
        if (z179 != null && z179.length > 0) z179[0].send_msg_all_p(m, null, true);
        Zone[] z180 = Zone.getMapByID(180);
        if (z180 != null && z180.length > 0) z180[0].send_msg_all_p(m, null, true);
        m.cleanup();
    }

    // ======================================================
    //  UPDATE FLAG — phân phối cân bằng vùng biển
    // ======================================================

    /** Gán clan vào vùng biển, đảm bảo chênh lệch số clan giữa các vùng không quá lớn */
    public static synchronized boolean updateFlag(int flag) {
        int f1 = 0, f2 = 0, f3 = 0, f4 = 0;
        for (int i = 0; i < Clan.ENTRY.size(); i++) {
            Clan c = Clan.ENTRY.get(i);
            if (c != null && c.map_create != null && c.map_create.map_thuLinhBienKhoi != null) {
                int fl = c.map_create.map_thuLinhBienKhoi.flag;
                if (fl == 4) f1++;
                else if (fl == 5) f2++;
                else if (fl == 6) f3++;
                else if (fl == 7) f4++;
            }
        }
        int min = Math.min(f1, Math.min(f2, Math.min(f3, f4)));
        int max = Math.max(f1, Math.max(f2, Math.max(f3, f4)));
        if (max - min >= 2) {
            if (flag == 4 && f1 == max) return false;
            if (flag == 5 && f2 == max) return false;
            if (flag == 6 && f3 == max) return false;
            if (flag == 7 && f4 == max) return false;
        }
        return true;
    }

    // ======================================================
    //  END REWARDS & CLEANUP
    // ======================================================

    private static void sendGiftTLBK() {
        sendGiftByFlag(4);
        sendGiftByFlag(5);
        sendGiftByFlag(6);
        sendGiftByFlag(7);
        cleanupAllMaps();
    }

    private static void sendGiftByFlag(int flag) {
        List<Clan> list = listTop(flag);
        for (int i = 0; i < list.size() && i < 4; i++) {
            Clan clan = list.get(i);
            if (clan == null || clan.members == null) continue;
            List<GiftBox> gifts = buildRankReward(i);
            for (ClanMember mem : clan.members) {
                if (mem == null || mem.name == null || mem.name.isEmpty()) continue;
                Player p0 = Zone.get_player_by_name_allmap(mem.name);
                if (p0 != null && (p0.isBot || p0.conn == null)) continue;
                int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(mem.name);
                if (ids[0] > 0) {
                    core.MailService.sendMailOffline(ids[0], ids[1], mem.name, "Hệ Thống",
                            "Thủ Lĩnh Biển Khơi (Top " + (i + 1) + ")",
                            "Chúc mừng Clan " + clan.name + " đạt Top " + (i + 1) + " Thủ Lĩnh Biển Khơi! Phần thưởng đính kèm bên dưới.",
                            core.MailService.MAIL_TYPE_GIFT, false, gifts, 14L * 24 * 3600 * 1000);
                }
            }
        }
    }

    private static void cleanupAllMaps() {
        for (int i = 0; i < Clan.ENTRY.size(); i++) {
            try {
                Clan c = Clan.ENTRY.get(i);
                if (c != null && c.map_create != null && c.map_create.map_thuLinhBienKhoi != null) {
                    for (Player p : new ArrayList<>(c.map_create.players)) {
                        if (p != null && !p.isBot) {
                            p.type_pk = -1;
                            p.return_to_previous_map();
                            if (p.getService() != null) p.getService().update_PK(p, false);
                        }
                    }
                    c.map_create.stop_map();
                    c.map_create.map_thuLinhBienKhoi = null;
                }
            } catch (Exception ignored) {}
        }
        int[] globalMaps = {179, 180, 184};
        for (int gId : globalMaps) {
            Zone[] gZones = Zone.getMapByID(gId);
            if (gZones != null && gZones.length > 0 && gZones[0] != null) {
                for (Player p : new ArrayList<>(gZones[0].players)) {
                    if (p != null && !p.isBot) {
                        try {
                            p.type_pk = -1;
                            p.return_to_previous_map();
                            if (p.getService() != null) p.getService().update_PK(p, false);
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
    }

    public static List<Clan> listTop(int flag) {
        List<Clan> list = new ArrayList<>();
        for (int i = 0; i < Clan.ENTRY.size(); i++) {
            try {
                Clan c = Clan.ENTRY.get(i);
                if (c == null || c.map_create == null || c.map_create.map_thuLinhBienKhoi == null) continue;
                if (flag == -1 || c.map_create.map_thuLinhBienKhoi.flag == flag) list.add(c);
            } catch (Exception ignored) {}
        }
        list.sort((a, b) -> {
            int pa = a.map_create != null && a.map_create.map_thuLinhBienKhoi != null ? a.map_create.map_thuLinhBienKhoi.point : 0;
            int pb = b.map_create != null && b.map_create.map_thuLinhBienKhoi != null ? b.map_create.map_thuLinhBienKhoi.point : 0;
            return Integer.compare(pb, pa);
        });
        return list;
    }

    private static List<GiftBox> buildRankReward(int rank) {
        List<GiftBox> g = new ArrayList<>();
        switch (rank) {
            case 0: GiftBox.addGift(g,4,0,2_000_000); GiftBox.addGift(g,4,1,2_000); GiftBox.addGift(g,4,226,5); break;
            case 1: GiftBox.addGift(g,4,0,1_000_000); GiftBox.addGift(g,4,1,1_500); GiftBox.addGift(g,4,226,3); break;
            case 2: GiftBox.addGift(g,4,0,500_000);   GiftBox.addGift(g,4,1,1_000); GiftBox.addGift(g,4,226,2); break;
            case 3: GiftBox.addGift(g,4,0,400_000);   GiftBox.addGift(g,4,1,700);   GiftBox.addGift(g,4,226,1); break;
        }
        return g;
    }

    // ======================================================
    //  HELPER
    // ======================================================

    private static ClanMember findMember(Clan clan, String name) {
        if (clan == null || clan.members == null || name == null) return null;
        for (int i = 0; i < clan.members.size(); i++) {
            ClanMember m = clan.members.get(i);
            if (m != null && m.name != null && m.name.equals(name)) return m;
        }
        return null;
    }
}
