package event;

import event.eboss.LanSuTu;
import event.eboss.LucciGioTo;
import itemz.MainItem;
import itemz.MainItemShop;
import model.Player;
import core.Manager;
import core.ZUtil;
import core.ZCollection;
import database.RandomCollection;
import model.YesNoDialog;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import mob.Mob;

/**
 * SuKienGioTo — SỰ KIỆN GIỖ TỔ HÙNG VƯƠNG 2026
 *
 * NPC Sự Kiện: Lucci Giỗ Tổ / Vua Hùng (-975) tại các Làng khởi đầu
 *
 * 1. Thu Thập Nguyên Liệu & Chế Tạo:
 *   - Đánh quái rơi Mâm Sính Lễ (#833), Cựa Gà (#834), Hồng Mao (#835), Ngà Voi (#836).
 *   - Làm Mâm Bạc (#837): 5 Cựa Gà + 5 Hồng Mao + 5 Ngà Voi + 30.000 Beri + 10 Ruby.
 *   - Làm Mâm Vàng (#838): 5 Cựa Gà + 5 Hồng Mao + 5 Ngà Voi + 25 Ruby + 25.000 Extol.
 *
 * 2. Triệu Hồi & Săn Boss:
 *   - Dùng Vé Triệu Hồi Lân (#839) để gọi Boss Lân Sư Tử (tối đa 1 boss/khu).
 *   - Boss Thế Giới Lucci Giỗ Tổ xuất hiện ngẫu nhiên trên các bản đồ, hồi sinh sau 10 phút.
 *
 * 3. Cửa Hàng Giỗ Tổ:
 *   - Dùng Mâm Sính Lễ (#833) đổi:
 *     + Mảnh Sách Haki Quan Sát 1-4 (#639-#642)
 *     + Hộp trang phục cao (#469)
 *     + Tinh thể đá (#10 cat 7)
 *     + Rương Trái Ác Quỷ Tự Chọn (#690)
 *     + Vé Triệu Hồi Lân (#839)
 *     + Rương Dial Truyền Thuyết (#823)
 *     + Hộp Trang Phục Raid Suit (#589)
 *     + Bảo Hiểm Chuyển Hóa Siêu (#794)
 *
 * 4. Dùng Item Sự Kiện:
 *   - Mâm Bạc (#837): Dâng lễ nhận EXP, Beri, Rương Vua Hùng (#172), Đá khảm, Bột cường hóa...
 *   - Mâm Vàng (#838): Dâng lễ nhận EXP khủng, Beri, Rương Đại Ác Quỷ (#158), Rương Cam, Rương Dial... và phát quà cho toàn map.
 *   - Rương Vua Hùng (#172): Mở quà ngẫu nhiên nhận Mâm Sính Lễ (#833), Beri, Ruby, Rương Ác Quỷ...
 *   - Vé Triệu Hồi Lân (#839): Gọi Boss Lân Sư Tử.
 */
public class SuKienGioTo extends Event {

    public static final int ID = Event.ID_SUKIEN_GIOTOHUNGVUONG_2026;

    public static SuKienGioTo instance;
    public RandomCollection<MainItem> giftKillBossALL = new RandomCollection<>();
    public List<LanSuTu> listBoss = new java.util.concurrent.CopyOnWriteArrayList<>();

    // Indexes EventData
    public static final int IDX_DAILY_DROP_SINH_LE = 10;
    public static final int IDX_DAILY_VAN_BUON     = 11;
    public static final int IDX_DAILY_NV_LAP       = 12;
    public static final int IDX_DAILY_TRUY_NA      = 13;
    public static final int IDX_DAILY_VUON_CAM     = 14;
    public static final int IDX_DAILY_LIEN_TANG    = 15;

    public SuKienGioTo() {
        super(ID, "Sự kiện Giỗ Tổ");
        this.shopName     = "Cửa Hàng Sự Kiện Giỗ Tổ";
        this.costItemId   = 833;   // Mâm sính lễ
        this.costItemType = 4;
        this.sellableItems = new short[]{};

        this.time = "0:00:00 15/4/2026 > 23:59:59 25/4/2026";
        this.timex2pay = "0:00:00 15/4/2026 > 23:59:59 29/4/2026";
        this.timechangeitem = "0:00:00 15/4/2026 > 23:59:59 27/4/2026";
        this.timedropitem = "0:00:00 15/4/2026 > 23:59:59 26/4/2026";
        this.timeremoveitem = "0:00:00 15/4/2026 > 23:59:59 30/4/2026";

        this.bxhSubTypes = new int[]{3, 14};
        this.bxhNames = new String[]{"Top Dâng Mâm Lễ", "Top Diệt Boss Lân"};

        initShop();
        instance = this;
    }

    public static SuKienGioTo gI() {
        if (instance == null) {
            instance = new SuKienGioTo();
        }
        return instance;
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            172, 374, 375, 376, 377, 458, 459, 833, 834, 835, 836, 837, 838, 839, 881, 882, 883, 884
        };
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienGioTo(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() {
        initShop();

        // Khởi tạo Boss Lân Sư Tử
        listBoss = new java.util.concurrent.CopyOnWriteArrayList<>();
        for (int i = 0; i < 100; i++) {
            listBoss.add(new LanSuTu(i));
        }

        initGift();
        System.out.println("[SuKienGioTo] Init completed with " + shopItems.size() + " shop items.");
    }

    @Override
    public void update(int hour, int min, int sec) {
        if (listBoss != null) {
            for (LanSuTu boss : listBoss) {
                if (boss != null && boss.mob != null) {
                    boss.update(boss.mob);
                }
            }
        }
        LucciGioTo.update();
    }

    public synchronized boolean callBoss(Player p) {
        if (listBoss == null || p == null || p.map == null) {
            p.getService().send_box_ThongBao_OK("Danh sách Boss chưa được khởi tạo.");
            return false;
        }

        LanSuTu boss = null;
        boolean hasBossInZone = false;
        for (LanSuTu lan : listBoss) {
            if (boss == null && lan.mob.hp <= 0 && lan.mob.isdie) {
                boss = lan;
            }
            if (lan.mob.map != null && lan.mob.map.equals(p.map) && !lan.mob.isdie && lan.mob.hp > 0) {
                hasBossInZone = true;
                break;
            }
        }

        if (hasBossInZone) {
            p.getService().send_box_ThongBao_OK("Tối đa 1 Boss Lân Sư Tử trong một khu vực!");
            return false;
        }

        if (boss == null) {
            p.getService().send_box_ThongBao_OK("Boss Lân Sư Tử đã xuất hiện quá nhiều, vui lòng chờ!");
            return false;
        }

        boss.timeLive = System.currentTimeMillis() + (1000 * 60 * 60); // 1 giờ
        boss.mob.map = p.map;
        boss.mob.hp = boss.mob.hp_max;
        boss.mob.isdie = false;
        boss.mob.x = p.x;
        boss.mob.y = p.y;
        boss.mob.sendMove();

        Manager.gI().chatKTG(0, "Người chơi [" + p.name + "] đã triệu hồi Boss Lân Sư Tử tại " + p.map.template.name + " (Khu " + (p.map.zone_id + 1) + ")!", 0);
        return true;
    }

    public void initGift() {
        giftKillBossALL.clear();
        giftKillBossALL.add(5, new MainItem(158, 4, 1)); // Rương Đại Ác Quỷ
        giftKillBossALL.add(10, RandomCollection.listDa2.nextMainItem()); // Đá Cấp 2
        giftKillBossALL.add(10, RandomCollection.listDa3.nextMainItem()); // Đá Cấp 3
        giftKillBossALL.add(5, RandomCollection.listManhDo.nextMainItem()); // Mảnh Đồ
        giftKillBossALL.add(2, RandomCollection.getTraiAcQuyTrungCap()); // Trái Ác Quỷ Trung Cấp
    }

    // ======================== MENU NPC ========================

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!EventManager.isActive(this.id)) return false;
        if (npcId == -975 || npcId == -100 || npcId == this.id) { // Lucci Giỗ Tổ / Menu Sự Kiện
            p.getService().openDynamicMenu(npcId, "Sự Kiện Giỗ Tổ", new String[]{
                "Cửa hàng Giỗ Tổ",
                "Làm Mâm Bạc",
                "Làm Mâm Vàng",
                "BXH điểm nạp",
                "BXH diệt boss",
                "Hướng dẫn",
                "Tích nạp sự kiện",
                "Tích tiêu sự kiện"
            }, null);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!EventManager.isActive(this.id)) return false;
        if (npcId == -873 || npcId == -975 || npcId == -100 || npcId == this.id) {
            switch (index) {
                case 0:
                    openShop(p);
                    break;
                case 1:
                    new itemz.rebuilds.GhepMamBac().show_table(p);
                    break;
                case 2:
                    new itemz.rebuilds.GhepMamVang().show_table(p);
                    break;
                case 3:
                    p.typeBXH = 3;
                    showEventRank(p);
                    break;
                case 4:
                    p.typeBXH = 14;
                    showEventRank(p);
                    break;
                case 5: {
                    String txt = "SỰ KIỆN GIỖ TỔ HÙNG VƯƠNG \n\n"
                            + "- Đánh quái rơi Mâm Sính Lễ (#833), Cựa Gà (#834), Hồng Mao (#835), Ngà Voi (#836).\n"
                            + "- Làm Mâm Bạc và Mâm Vàng dâng lên Vua Hùng nhận Exp và quà cực khủng.\n"
                            + "- Dùng Vé Triệu Hồi Lân (#839) để săn Boss Lân Sư Tử.\n"
                            + "- Khiêu chiến Boss Thế Giới Lucci Giỗ Tổ để săn Rương Đại Ác Quỷ, Mâm Vàng và Đá Hải Thạch C6.\n"
                            + "- Dùng Mâm Sính Lễ đổi vật phẩm quý tại Cửa Hàng Giỗ Tổ.";
                    p.getService().Help_From_Server(npcId, txt);
                    break;
                }
                case 6:
                    showTichNap(p);
                    break;
                case 7:
                    showTichTieu(p);
                    break;
            }
            return true;
        }
        return false;
    }

    // ======================== DÙNG ITEM ========================

    @Override
    public boolean onUseItem(Player p, int id) throws IOException {
        if (!isEventActive()) {
            if (id == 839 || id == 837 || id == 838 || id == 172) {
                p.getService().send_box_ThongBao_OK("Sự kiện Giỗ Tổ đã kết thúc!");
                return false;
            }
            return false;
        }

        // 1. Vé triệu hồi Lân (#839)
        if (id == 839) {
            if (p.conn == null || p.conn.status != 1) {
                p.getService().send_box_ThongBao_OK("Tài khoản chưa kích hoạt, không thể triệu hồi Lân Sư Tử!");
                return false;
            }
            if (p.item.total_item_bag_by_id(4, id) < 1) return false;
            if (callBoss(p)) {
                p.item.remove_item47(4, id, 1);
                p.item.updateInventory(false);
                return true;
            }
            return false;
        }

        // 2. Mâm Bạc (#837)
        if (id == 837) {
            if (p.item.total_item_bag_by_id(4, id) < 1) return false;
            p.item.remove_item47(4, id, 1);
            p.item.updateInventory(false);

            long expEarn = (long) p.level * 6000L;
            int beri = ZUtil.random(50000, 200000);

            List<template.GiftBox> giftList = new ArrayList<>();
            giftList.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, expEarn)));
            giftList.add(new template.GiftBox(4, 0, beri));

            int roll = ZUtil.random(100);
            if (roll < 10) { // 10% Rương Vua Hùng
                giftList.add(new template.GiftBox(4, 172, 1));
            } else if (roll < 35) { // 25% Rương Ác Quỷ
                giftList.add(new template.GiftBox(4, 29, 1));
            } else if (roll < 65) { // 30% Bột cường hóa (1-5)
                int qty = ZUtil.random(1, 5);
                giftList.add(new template.GiftBox(7, 1, qty));
            } else { // 35% Đá khảm C1-C3
                int tier = ZUtil.random(1, 3);
                MainItem da = RandomCollection.getStone(tier);
                if (da != null) {
                    giftList.add(new template.GiftBox(4, da.id, 1));
                }
            }

            core.RewardService.sendGiftOrMail(p, 1, "Dâng Mâm Bạc", "Phần thưởng dâng Mâm Bạc", giftList, true);
            return true;
        }

        // 3. Mâm Vàng (#838)
        if (id == 838) {
            if (p.item.total_item_bag_by_id(4, id) < 1) return false;
            p.item.remove_item47(4, id, 1);
            p.item.updateInventory(false);

            long expEarn = (long) p.level * 15000L;
            int beri = ZUtil.random(200000, 800000);
            int ruby = ZUtil.random(2, 5);

            List<template.GiftBox> giftList = new ArrayList<>();
            giftList.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, expEarn)));
            giftList.add(new template.GiftBox(4, 0, beri));
            giftList.add(new template.GiftBox(4, 1, ruby));

            int roll = ZUtil.random(1000);

            if (roll < 20) { // 2% Rương Dial
                giftList.add(new template.GiftBox(4, 455, 1));
                Manager.gI().chatKTG(0, "Chúc mừng người chơi [" + p.name + "] đã mở Mâm Vàng nhận được [Rương Dial] siêu phẩm!", 0);
            } else if (roll < 60) { // 4% Rương Đại Ác Quỷ
                giftList.add(new template.GiftBox(4, 158, 1));
                Manager.gI().chatKTG(0, "Chúc mừng người chơi [" + p.name + "] đã mở Mâm Vàng nhận được [Rương Đại Ác Quỷ]!", 0);
            } else if (roll < 200) { // 14% Rương cam cùng hệ theo level
                int chestId = (p.level < 20) ? 122 : Math.min(131, 122 + (p.level / 10 - 1));
                giftList.add(new template.GiftBox(4, chestId, 1));
            } else if (roll < 450) { // 25% Rương Ác Quỷ
                giftList.add(new template.GiftBox(4, 29, 1));
            } else if (roll < 750) { // 30% Đá Hải Thạch C1-C3
                int[] daHt = {221, 222, 223};
                int daId = daHt[ZUtil.random(daHt.length)];
                giftList.add(new template.GiftBox(4, daId, 1));
            } else { // 25% Bột cường hóa (5-10)
                int qty = ZUtil.random(5, 10);
                giftList.add(new template.GiftBox(7, 1, qty));
            }

            core.RewardService.sendGiftOrMail(p, 1, "Dâng Mâm Vàng", "Phần thưởng dâng Mâm Vàng", giftList, true);

            // Gửi quà chia vui cho người chơi trong map
            if (p.map != null && p.map.players != null) {
                for (Player other : p.map.players) {
                    if (other != null && !other.equals(p) && other.conn != null && other.conn.status == 1) {
                        try {
                            List<template.GiftBox> shareGifts = new ArrayList<>();
                            shareGifts.add(new template.GiftBox(99, 0, (int) ((long) other.level * 2000L)));
                            shareGifts.add(new template.GiftBox(4, 0, 30000));
                            core.RewardService.sendGiftOrMail(other, 1, "Lộc Chia Vui Giỗ Tổ", "[" + p.name + "] vừa dâng Mâm Vàng! Bạn nhận được lộc chia vui.", shareGifts, true);
                        } catch (Exception ignored) {}
                    }
                }
            }
            return true;
        }

        // 4. Rương Vua Hùng (#172)
        if (id == 172) {
            if (p.item.total_item_bag_by_id(4, id) < 1) return false;
            p.item.remove_item47(4, id, 1);
            p.item.updateInventory(false);

            int beri = ZUtil.random(10000, 100000);

            List<template.GiftBox> giftList = new ArrayList<>();
            giftList.add(new template.GiftBox(4, 0, beri));

            int roll = ZUtil.random(100);
            if (roll < 25) { // 25% Mâm sính lễ
                giftList.add(new template.GiftBox(4, 833, 1));
            } else if (roll < 45) { // 20% Ruby
                int r = ZUtil.random(5, 15);
                giftList.add(new template.GiftBox(4, 1, r));
            } else if (roll < 70) { // 25% Rương Ác Quỷ
                giftList.add(new template.GiftBox(4, 29, 1));
            } else { // 30% Bột Cường Hóa
                int qty = ZUtil.random(1, 3);
                giftList.add(new template.GiftBox(7, 1, qty));
            }

            core.RewardService.sendGiftOrMail(p, 1, "Mở Rương Vua Hùng", "Phần thưởng mở Rương Vua Hùng", giftList, true);
            return true;
        }

        // 5. Mâm Sính Lễ (#833) -> Mở Cửa Hàng Giỗ Tổ
        if (id == 833) {
            openShop(p);
            return true;
        }

        // 6. Cựa Gà (#834), Hồng Mao (#835), Ngà Voi (#836)
        if (id >= 834 && id <= 836) {
            sendMenu(p, -975);
            return true;
        }

        return false;
    }

    // ======================== HOẠT ĐỘNG ========================

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!EventManager.isActive(this.id) || mob == null || mob.is_boss) return;
        if (!isDropItemActive()) return;

        p = p.getOwnerPlayer();
        if (p.isBot || p.item == null) return;

        if (Math.abs(p.level - mob.level) <= 10) {
            EventData evData = getOrCreateEventData(p);
            if (evData != null && evData.data[IDX_DAILY_DROP_SINH_LE] < 150) {
                if (ZUtil.random(100) < 15) { // 15% rơi vật phẩm
                    evData.data[IDX_DAILY_DROP_SINH_LE]++;
                    int roll = ZUtil.random(100);
                    int dropId;
                    if (roll < 40) dropId = 833;      // 40% Mâm sính lễ
                    else if (roll < 60) dropId = 834; // 20% Cựa gà
                    else if (roll < 80) dropId = 835; // 20% Hồng mao
                    else dropId = 836;                // 20% Ngà voi

                    p.item.add_item_bag47(4, dropId, 1);
                    p.item.updateInventory(false);
                }
            }
        }
    }

    @Override
    public void onVanChuyen(Player p) {
        if (!EventManager.isActive(this.id) || !isDropItemActive()) return;
        giveActivityReward(p, 833, 5, IDX_DAILY_VAN_BUON, 5, "Vận Buôn", "Mâm Sính Lễ");
    }

    @Override
    public void onNhiemVuLap(Player p) {
        if (!EventManager.isActive(this.id) || !isDropItemActive()) return;
        giveActivityReward(p, 833, 5, IDX_DAILY_NV_LAP, 5, "Nhiệm Vụ Lặp", "Mâm Sính Lễ");
    }

    @Override
    public void onWanted(Player p) {
        if (!EventManager.isActive(this.id) || !isDropItemActive()) return;
        giveActivityReward(p, 833, 3, IDX_DAILY_TRUY_NA, 5, "Thắng Truy Nã", "Mâm Sính Lễ");
    }

    @Override
    public void onVuonCam(Player p, int round) {
        if (!EventManager.isActive(this.id) || !isDropItemActive()) return;
        if (round >= 10) {
            giveActivityReward(p, 839, 1, IDX_DAILY_VUON_CAM, 3, "PB Vườn Cam", "Vé Triệu Hồi Lân");
        }
    }

    @Override
    public void onLienTang(Player p, int floor) {
        if (!EventManager.isActive(this.id) || !isDropItemActive()) return;
        if (floor >= 5) {
            giveActivityReward(p, 839, 1, IDX_DAILY_LIEN_TANG, 3, "PB Liên Tầng", "Vé Triệu Hồi Lân");
        }
    }

    private void giveActivityReward(Player p, int itemId, int amount, int dataIndex, int maxTurns, String source, String itemName) {
        if (p == null || p.isBot || p.item == null) return;
        if (p.item.able_bag() < 1) return;

        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[dataIndex] < maxTurns) {
            evData.data[dataIndex]++;
            p.item.add_item_bag47(4, itemId, amount);
            p.item.updateInventory(false);
        }
    }

    public boolean isGioTo() {
        return EventManager.isActive(this.id);
    }
}
