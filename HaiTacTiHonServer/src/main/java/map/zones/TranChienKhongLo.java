package map.zones;

import clan.Clan;
import model.Player;
import map.Zone;
import network.Message;
import core.Manager;
import core.ZUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * TranChienKhongLo — Quản lý ghép trận và logic tĩnh của Phó Bản Khổng Lồ (Dorry vs Brogy).
 *
 * <p>Quản lý hàng đợi ghép trận (hỗ trợ tối thiểu 1 người cùng clan cùng map, không giới hạn tối đa).
 * Khi có 2 clan thì ghép với nhau, khi chỉ có 1 clan thì tự động ghép với Đội Bot Khổng Lồ.
 */
public class TranChienKhongLo {

    // ======================================================
    //  QUEUE ENTRY
    // ======================================================

    public static class ClanQueueEntry {
        public Clan clan;
        public List<Player> readyPlayers;
        public long registerTime;
        public long maxWaitDuration;

        public ClanQueueEntry(Clan clan, List<Player> readyPlayers) {
            this.clan = clan;
            this.readyPlayers = (readyPlayers != null) ? readyPlayers : new ArrayList<>();
            this.registerTime = System.currentTimeMillis();
            this.maxWaitDuration = ZUtil.random(18_000, 35_000);
        }
    }

    /** Danh sách hàng đợi đăng ký ghép trận */
    public static final List<ClanQueueEntry> WAITING_QUEUE = new CopyOnWriteArrayList<>();

    /** Tương thích cũ */
    public static final List<Clan> LIST = new ArrayList<>();

    /** Flag mở ép buộc bởi Admin */
    public static boolean isForceOpen = false;

    /** Trạng thái mở phiên hoạt động theo Engine */
    public static boolean isOpenSession = false;
    public static String currentSessionToken = "";

    /**
     * Bắt đầu một phiên hoạt động mới cho Phó Bản Khổng Lồ.
     */
    public static void startSession(String sessionToken, int durationMinutes) {
        currentSessionToken = (sessionToken != null && !sessionToken.isEmpty()) ? sessionToken : ZUtil.generateMD5Token("TranChienKhongLo_" + System.currentTimeMillis());
        isOpenSession = true;

        // Tự động phân phối bot ra các map ngoài và Little Garden
        bot.BotKhongLoMapNgoai.dispatchBots();

        try {
            Manager.gI().chatKTG(0, "Phó Bản Trận Chiến Khổng Lồ (Little Garden) đã chính thức mở! Hãy đến NPC Phó Bản lập đội hoặc ra các map lân cận để săn Boss & Bot!", 5);
        } catch (Exception ignored) {}
    }

    /**
     * Kết thúc phiên hoạt động.
     */
    public static void endSession() {
        isOpenSession = false;
        bot.BotKhongLoMapNgoai.clearBots();
        try {
            Manager.gI().chatKTG(0, "Phó Bản Trận Chiến Khổng Lồ đã kết thúc đợt hoạt động này!", 5);
        } catch (Exception ignored) {}
    }

    /**
     * Kiểm tra phó bản có đang mở không.
     */
    public static boolean isOpen() {
        if (isForceOpen) return true;
        return activities.TimedDungeonManager.gI().isDungeonOpen("TRAN_CHIEN_KHONG_LO") || isOpenSession;
    }

    /**
     * Đăng ký clan vào hàng đợi ghép trận.
     * Tối thiểu 3 người chơi/bot sẵn sàng, không giới hạn tối đa.
     */
    public static synchronized void registerClanQueue(Clan clan, List<Player> readyPlayers) {
        if (clan == null) return;

        // Nếu trận của clan này đang diễn ra -> cho vào thẳng trận
        if (clan.map_create != null && clan.map_create.map_little_garden != null && !clan.map_create.map_little_garden.is_finish) {
            if (readyPlayers != null) {
                int team = (clan.equals(clan.map_create.map_little_garden.clan1) ? 4 : 5);
                for (Player p : readyPlayers) {
                    if (p != null && (p.isBot || p.isDe || p instanceof model.DeTu || p instanceof bot.mercenary.MercenaryBot || p.conn != null)) {
                        p.type_pk = (byte) team;
                        map.Vgo vgo = new map.Vgo();
                        vgo.map_go = new Zone[]{clan.map_create};
                        vgo.xnew = (short) (team == 4 ? 350 : 1400);
                        vgo.ynew = 260;
                        try {
                            p.goto_map(vgo);
                            if (p.getService() != null) {
                                p.getService().send_box_ThongBao_OK("Bạn đã tiến vào trận chiến khổng lồ đang diễn ra của Băng!");
                            }
                        } catch (Exception ignored) {}
                    }
                }
            }
            return;
        }

        // Lọc thành viên hợp lệ
        List<Player> validPlayers = new ArrayList<>();
        if (readyPlayers != null) {
            for (Player p : readyPlayers) {
                if (p != null && (p.isBot || p.isDe || p instanceof model.DeTu || p instanceof bot.mercenary.MercenaryBot || p.conn != null)) {
                    validPlayers.add(p);
                }
            }
        }

        if (validPlayers.isEmpty()) return;

        // Xoá entry cũ nếu có
        removeClanFromQueue(clan);

        ClanQueueEntry entry = new ClanQueueEntry(clan, validPlayers);
        WAITING_QUEUE.add(entry);

        long expireTime = entry.registerTime + entry.maxWaitDuration;
        // Gửi cooldown đếm ngược cho cả đội
        for (Player p : validPlayers) {
            try {
                if (p.getService() != null) {
                    p.getService().send_time_cool_down(expireTime, "Ghép đội Khổng Lồ", 0);
                    p.getService().send_box_ThongBao_OK("Đăng ký Phó Bản Khổng Lồ thành công!\nĐang tìm kiếm đối thủ...");
                }
            } catch (Exception ignored) {}
        }

        // Kích hoạt ghép trận ngay nếu có đối thủ thật sẵn
        processQueue();
    }

    /**
     * Xử lý ghép trận từ hàng đợi.
     * Có 2 clan -> Ghép 2 clan đối đầu ngay lập tức (ưu tiên người thật).
     * Có 1 clan sau thời gian chờ ngẫu nhiên -> Ghép với Đội Bot Khổng Lồ cân bằng.
     */
    public static synchronized void processQueue() {
        // Dọn dẹp các clan không còn ai online
        WAITING_QUEUE.removeIf(entry -> {
            if (entry == null || entry.clan == null || entry.readyPlayers == null) return true;
            entry.readyPlayers.removeIf(p -> p == null || (!p.isBot && !p.isDe && !(p instanceof model.DeTu) && !(p instanceof bot.mercenary.MercenaryBot) && p.conn == null));
            return entry.readyPlayers.isEmpty();
        });

        if (WAITING_QUEUE.isEmpty()) return;

        // Ưu tiên cao nhất: Ghép cặp giữa 2 clan thật nếu có từ 2 clan trở lên
        while (WAITING_QUEUE.size() >= 2) {
            ClanQueueEntry entry1 = WAITING_QUEUE.remove(0);
            ClanQueueEntry entry2 = WAITING_QUEUE.remove(0);

            if (entry1 != null && entry2 != null && entry1.clan != null && entry2.clan != null) {
                // Clan 1 = Dorry (type 4)
                for (Player p : entry1.readyPlayers) {
                    try { p.getService().send_time_cool_down(System.currentTimeMillis(), "", 0); } catch (Exception ignored) {}
                    p.type_pk = 4;
                    p.tableTickOption = null;
                }
                // Clan 2 = Brogy (type 5)
                for (Player p : entry2.readyPlayers) {
                    try { p.getService().send_time_cool_down(System.currentTimeMillis(), "", 0); } catch (Exception ignored) {}
                    p.type_pk = 5;
                    p.tableTickOption = null;
                }

                MapTranChienKhongLo dungeon = MapTranChienKhongLo.createDungeon(entry1.clan, entry2.clan);
                dungeon.join(entry1.readyPlayers);
                dungeon.join(entry2.readyPlayers);

                String msg1 = "Đã ghép trận thành công với [" + entry2.clan.name + "]!\nTiến vào chiến trường!";
                String msg2 = "Đã ghép trận thành công với [" + entry1.clan.name + "]!\nTiến vào chiến trường!";
                for (Player p : entry1.readyPlayers) {
                    try { p.getService().send_box_ThongBao_OK(msg1); } catch (Exception ignored) {}
                }
                for (Player p : entry2.readyPlayers) {
                    try { p.getService().send_box_ThongBao_OK(msg2); } catch (Exception ignored) {}
                }
            }
        }

        // Nếu chỉ còn clan đơn lẻ trong hàng đợi: chỉ ghép bot khi hết thời gian chờ ngẫu nhiên
        long now = System.currentTimeMillis();
        for (int i = 0; i < WAITING_QUEUE.size(); i++) {
            ClanQueueEntry entry1 = WAITING_QUEUE.get(i);
            if (entry1 != null && entry1.clan != null && !entry1.readyPlayers.isEmpty()) {
                if (now - entry1.registerTime >= entry1.maxWaitDuration) {
                    WAITING_QUEUE.remove(i);
                    i--;
                    // Clan thật = Dorry (type 4)
                    for (Player p : entry1.readyPlayers) {
                        try { p.getService().send_time_cool_down(System.currentTimeMillis(), "", 0); } catch (Exception ignored) {}
                        p.type_pk = 4;
                        p.tableTickOption = null;
                    }

                    MapTranChienKhongLo dungeon = MapTranChienKhongLo.createDungeonSolo(entry1.clan);
                    dungeon.join(entry1.readyPlayers);

                    String msg = "Không tìm thấy băng đối thủ!\nHệ thống đã ghép Băng của bạn vào trận chiến với Đội Bot Khổng Lồ!";
                    for (Player p : entry1.readyPlayers) {
                        try { p.getService().send_box_ThongBao_OK(msg); } catch (Exception ignored) {}
                    }
                }
            }
        }
    }

    public static boolean isClanInQueue(Clan clan) {
        if (clan == null) return false;
        for (ClanQueueEntry entry : WAITING_QUEUE) {
            if (entry.clan != null && entry.clan.equals(clan)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPlayerInQueue(Player p) {
        if (p == null) return false;
        for (ClanQueueEntry entry : WAITING_QUEUE) {
            if (entry != null && entry.readyPlayers != null) {
                for (Player pl : entry.readyPlayers) {
                    if (pl != null && pl.name != null && pl.name.equals(p.name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void removeClanFromQueue(Clan clan) {
        if (clan == null) return;
        for (ClanQueueEntry entry : WAITING_QUEUE) {
            if (entry.clan != null && entry.clan.equals(clan)) {
                for (Player p : entry.readyPlayers) {
                    try { p.getService().send_time_cool_down(System.currentTimeMillis(), "", 0); } catch (Exception ignored) {}
                }
            }
        }
        WAITING_QUEUE.removeIf(entry -> entry != null && entry.clan != null && entry.clan.equals(clan));
    }

    public static synchronized void add_clan_wait(Clan clan) {
        if (clan != null) {
            // Tương thích cũ
            List<Player> list = new ArrayList<>();
            for (int i = 0; i < clan.members.size(); i++) {
                Player p0 = Zone.get_player_by_name_allmap(clan.members.get(i).name);
                if (p0 != null) list.add(p0);
            }
            registerClanQueue(clan, list);
        }
    }

    public static synchronized void remove_clan_wait(Clan clan) {
        removeClanFromQueue(clan);
        LIST.remove(clan);
    }

    // ======================================================
    //  GAME LOGIC & PACKET DISPATCHING (Gửi packet -79 cho client)
    // ======================================================

    /**
     * Gửi khung HP/MP ban đầu của cả 2 khổng lồ khi player vào map.
     */
    public static void send_info(Player p) throws IOException {
        if (p == null || p.map == null || p.map.map_little_garden == null) return;
        // Gửi HP max Dorry
        Message m = new Message(-79);
        m.writer().writeByte(0);
        m.writer().writeByte(0);
        m.writer().writeInt(20);
        m.writer().writeInt(10);
        p.addmsg(m);
        m.cleanup();

        // Gửi HP max Brogy
        m = new Message(-79);
        m.writer().writeByte(0);
        m.writer().writeByte(1);
        m.writer().writeInt(20);
        m.writer().writeInt(10);
        p.addmsg(m);
        m.cleanup();

        update_info(p);
    }

    /** Gửi HP/MP hiện tại của cả 2 khổng lồ cho player */
    private static void update_info(Player p) throws IOException {
        if (p == null || p.map == null || p.map.map_little_garden == null) return;
        MapTranChienKhongLo g = p.map.map_little_garden;
        Message m = new Message(-79);
        m.writer().writeByte(1);
        m.writer().writeByte(0);
        m.writer().writeInt(g.hp_1);
        m.writer().writeInt(g.mp_1);
        p.addmsg(m);
        m.cleanup();

        m = new Message(-79);
        m.writer().writeByte(1);
        m.writer().writeByte(1);
        m.writer().writeInt(g.hp_2);
        m.writer().writeInt(g.mp_2);
        p.addmsg(m);
        m.cleanup();
    }

    /**
     * Tích luỹ MP cho đội [type] (4=Dorry, 5=Brogy).
     * Mỗi 10 MP -> kích hoạt đòn đánh lên đội còn lại (-2 HP).
     */
    public static void update_mp(Zone map, int type, int quant) throws IOException {
        if (map == null || map.map_little_garden == null) return;
        MapTranChienKhongLo g = map.map_little_garden;
        if (g.is_finish) return;

        if (type == 4) {
            g.mp_1 += quant;
            if (quant == 0) g.mp_2 = 0;
            broadcastStat(map, 0, g.hp_1, g.mp_1);
            if (g.mp_1 >= 10) {
                g.mp_1 = 0;
                update_dame(map, type);
            }
        } else if (type == 5) {
            g.mp_2 += quant;
            if (quant == 0) g.mp_1 = 0;
            broadcastStat(map, 1, g.hp_2, g.mp_2);
            if (g.mp_2 >= 10) {
                g.mp_2 = 0;
                update_dame(map, type);
            }
        }

        if (quant == 0) {
            if (type == 5) broadcastStat(map, 0, g.hp_1, g.mp_1);
            else if (type == 4) broadcastStat(map, 1, g.hp_2, g.mp_2);
        }
    }

    /**
     * Cộng HP cho đội [type] (4=Dorry, 5=Brogy), tối đa 20.
     */
    public static void update_hp(Zone map, int type, int quant) throws IOException {
        if (map == null || map.map_little_garden == null) return;
        MapTranChienKhongLo g = map.map_little_garden;
        if (g.is_finish) return;

        if (type == 4) {
            g.hp_1 = Math.min(20, g.hp_1 + quant);
            broadcastStat(map, 0, g.hp_1, g.mp_1);
        } else if (type == 5) {
            g.hp_2 = Math.min(20, g.hp_2 + quant);
            broadcastStat(map, 1, g.hp_2, g.mp_2);
        }
    }

    /** Gây 2 HP lên khổng lồ đối diện khi MP đầy, broadcast animation đòn đánh và kiểm tra thắng */
    private static void update_dame(Zone map, int type) throws IOException {
        if (map == null || map.map_little_garden == null) return;
        MapTranChienKhongLo g = map.map_little_garden;
        Message m;

        if (type == 4) {
            m = new Message(-79);
            m.writer().writeByte(2);
            m.writer().writeByte(0); // Dorry đánh
            m.writer().writeInt(1);
            g.hp_2 = Math.max(0, g.hp_2 - 2);
            map.send_msg_all_p(m, null, true);
            m.cleanup();
        } else if (type == 5) {
            m = new Message(-79);
            m.writer().writeByte(2);
            m.writer().writeByte(1); // Brogy đánh
            m.writer().writeInt(1);
            g.hp_1 = Math.max(0, g.hp_1 - 2);
            map.send_msg_all_p(m, null, true);
            m.cleanup();
        }

        // Broadcast HP/MP mới cả 2 đội
        broadcastStat(map, 0, g.hp_1, g.mp_1);
        broadcastStat(map, 1, g.hp_2, g.mp_2);

        // Kiểm tra kết thúc
        if (g.hp_1 <= 0) {
            sendFinish(map, 0);
            g.is_finish = true;
        } else if (g.hp_2 <= 0) {
            sendFinish(map, 1);
            g.is_finish = true;
        }
    }

    // ======================================================
    //  HELPERS
    // ======================================================

    private static void broadcastStat(Zone map, int who, int hp, int mp) throws IOException {
        Message m = new Message(-79);
        m.writer().writeByte(1);
        m.writer().writeByte(who);
        m.writer().writeInt(hp);
        m.writer().writeInt(mp);
        map.send_msg_all_p(m, null, true);
        m.cleanup();
    }

    private static void sendFinish(Zone map, int loser) throws IOException {
        Message m = new Message(-79);
        m.writer().writeByte(3);
        m.writer().writeByte(loser);
        map.send_msg_all_p(m, null, true);
        m.cleanup();
    }
}
