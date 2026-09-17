package rank;

import zabstracts.AbsRanked;
import model.Player;
import network.Message;

import java.io.IOException;

/**
 * Ranked — Entry point cho hệ thống BXH.
 *
 * DISPATCH:
 *   Ranked.send(p, type, page)  -> delegate cho AbsRanked.get(type).show(p, page)
 *   Ranked.update()             -> update định kỳ tất cả top
 */
public class Ranked {

    /**
     * Gửi BXH cho player theo type packet.
     * Nếu không có dữ liệu hoặc type chưa đăng ký, vẫn gửi packet rỗng (count=0)
     * để client mở cửa sổ hiển thị rỗng an toàn, tuyệt đối không lỗi.
     */
    public static void send(Player p, int type, int page) {
        if (page < 0) page = 0;
        try {
            if (type == 7) {
                AbsRanked rankObj = AbsRanked.getBySubType(p.typeBXH);
                if (rankObj != null) {
                    rankObj.show(p, page);
                } else {
                    sendEmptyRank(p, 7, "Sự Kiện", page);
                }
                return;
            }
            AbsRanked rankObj = AbsRanked.get(type);
            if (rankObj != null) {
                rankObj.show(p, page);
            } else {
                sendEmptyRank(p, type, "Bảng Xếp Hạng", page);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gửi packet BXH rỗng (count=0) cho client hiển thị khung rỗng an toàn khi không có dữ liệu.
     */
    public static void sendEmptyRank(Player p, int type, String title, int page) {
        try {
            Message m = new Message(-30);
            m.writer().writeByte(type);
            m.writer().writeUTF(title);
            m.writer().writeByte(page < 0 ? 0 : page);
            m.writer().writeByte(0); // count = 0
            p.addmsg(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void process(Player p, network.Message m2) throws IOException {
        byte type   = m2.reader().readByte();
        byte idlist = m2.reader().readByte();
        byte page   = m2.reader().readByte();
        switch (type) {
            case 2: {
                if (idlist == 2 && page == 0) {
                    Message m = new Message(-30);
                    m.writer().writeByte(2);
                    m.writer().writeUTF("Kẻ Thù");
                    m.writer().writeByte(0);
                    m.writer().writeByte(p.enemy_list.size());
                    for (int i = 0; i < p.enemy_list.size(); i++) {
                        activities.Friend.ReadInfoMemList(m.writer(), p.enemy_list.get(i));
                    }
                    p.addmsg(m);
                    m.cleanup();
                }
                break;
            }
            case 3: {
                Ranked.send(p, idlist, page);
                break;
            }
        }
    }

    private static final java.util.concurrent.ExecutorService RANK_POOL = java.util.concurrent.Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "HTTH-Ranked-Updater");
        t.setDaemon(true);
        return t;
    });
    private static final java.util.concurrent.atomic.AtomicBoolean IS_UPDATING = new java.util.concurrent.atomic.AtomicBoolean(false);

    public static void updateAsync() {
        if (IS_UPDATING.compareAndSet(false, true)) {
            RANK_POOL.submit(() -> {
                try {
                    update();
                } finally {
                    IS_UPDATING.set(false);
                }
            });
        }
    }

    public static void shutdownPool() {
        try {
            if (RANK_POOL != null && !RANK_POOL.isShutdown()) {
                RANK_POOL.shutdown();
                if (!RANK_POOL.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS)) {
                    RANK_POOL.shutdownNow();
                }
            }
        } catch (Exception e) {
            RANK_POOL.shutdownNow();
        }
    }

    public static void update() {
        for (AbsRanked r : AbsRanked.all()) {
            try {
                r.update();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (AbsRanked r : AbsRanked.allEventSubTypes()) {
            try {
                if (r.isActive()) r.update();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int get_Thanh_tich_level(Player p) {
        if (p != null && (p.getIdEff() > 0 || p.id_danh_hieu_su_dung > 0)) {
            return -1;
        }
        return TopCaoThu.gI().getRankTier(p, 10);
    }

    public static int get_Thanh_tich_level2(Player p) {
        if (p != null && (p.getIdEff() > 0 || p.id_danh_hieu_su_dung > 0)) {
            return -1;
        }
        return TopCaoThu.gI().getRank(p, 10);
    }

    public static int get_Thanh_tich_pvp(Player p) {
        if (p != null && (p.getIdEff() > 0 || p.id_danh_hieu_su_dung > 0)) {
            return -1;
        }
        return TopPVP.gI().getRankTier(p, 10);
    }

    public static int get_Thanh_tich_pvp2(Player p) {
        if (p != null && (p.getIdEff() > 0 || p.id_danh_hieu_su_dung > 0)) {
            return -1;
        }
        return TopPVP.gI().getRank(p, 10);
    }

    public static int get_rank_wanted(String name) {
        return TopWanted.gI().getRank(name);
    }

    public static int get_rank_wanted2(Player p) {
        return TopWanted.gI().getRank(p);
    }

    public static int get_rank_hang_dong(Player p) {
        return TopHang.gI().getRankTier(p, 10);
    }
}