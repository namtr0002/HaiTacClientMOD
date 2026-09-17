package event;

import model.Player;
import core.Manager;
import core.ZUtil;
import itemz.MainItem;
import mob.Mob;
import map.Zone;
import template.GiftBox;
import template.ItemTemplate4;
import network.Service;
import network.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuKien20Thang10 extends Event {

    public static final int ID = Event.ID_SUKIEN_20THANG10_2025;

    // Materials
    public static final int ITEM_GAU_BONG = 590;
    public static final int ITEM_CANH_HOA = 591;
    public static final int ITEM_GIAY_GOI = 575;
    public static final int ITEM_RUY_BANG_TIM = 576;
    public static final int ITEM_RUONG_DAU_LAU = 578;
    public static final int ITEM_PHAO_HOA = 359;

    // Crafted boxes
    public static final int ITEM_HOP_QUA_1 = 592;
    public static final int ITEM_HOP_QUA_2 = 593;
    public static final int ITEM_HOP_QUA_3 = 594;
    public static final int ITEM_HOP_QUA_4 = 595;
    public static final int ITEM_HOP_QUA_DB = 596;

    // String Keys cho các hoạt động sự kiện 20/10
    public static final String KEY_DAILY_DROP_MOB       = "drop_mob_daily";
    public static final String KEY_DAILY_DROP_SEA       = "drop_sea_daily";
    public static final String KEY_DAILY_LIEN_TANG      = "lien_tang_daily";
    public static final String KEY_DAILY_SIEU_LIEN_TANG = "sieu_lien_tang_daily";
    public static final String KEY_DAILY_WANTED         = "wanted_daily";
    public static final String KEY_DAILY_VAN_CHUYEN     = "van_chuyen_daily";
    public static final String KEY_DAILY_VUON_CAM       = "vuon_cam_daily";
    public static final String KEY_DAILY_NV_LAP         = "nv_lap_daily";
    public static final String KEY_DAILY_PVP            = "pvp_daily";
    public static final String KEY_DAILY_TRAIN          = "train_daily";

    public static final String KEY_TOP_GOI_HOP_QUA      = "top_goi_hop_qua";
    public static final String KEY_TOP_HOP_QUA_DB       = "top_hop_qua_db";
    public static final String KEY_POINT_EVENT1         = "point_event1";
    public static final String KEY_POINT_EVENT2         = "point_event2";

    // EventData Daily Limits Indexes (fallback tương thích ngược)
    public static final int IDX_DAILY_DROP_MOB       = 22; // Rơi quái thường (tối đa 150/ngày)
    public static final int IDX_DAILY_DROP_SEA       = 23; // Rơi quái biển (tối đa 100/ngày)
    public static final int IDX_DAILY_LIEN_TANG      = 24; // Liên Tầng (tối đa 5 lần/ngày)
    public static final int IDX_DAILY_SIEU_LIEN_TANG = 25; // Siêu Liên Tầng (tối đa 3 lần/ngày)
    public static final int IDX_DAILY_WANTED         = 26; // Truy Nã (tối đa 10 lần/ngày)
    public static final int IDX_DAILY_VAN_CHUYEN     = 27; // Vận Chuyển (tối đa 5 lần/ngày)
    public static final int IDX_DAILY_VUON_CAM       = 28; // Vườn Cam (tối đa 3 lần/ngày)
    public static final int IDX_DAILY_NV_LAP         = 29; // Nhiệm Vụ Lặp (tối đa 5 lần/ngày)
    public static final int IDX_DAILY_PVP            = 30; // PvP Thắng (tối đa 5 lần/ngày)
    public static final int IDX_DAILY_TRAIN          = 31; // Train Quái (tối đa 5 lần/ngày)

    private static SuKien20Thang10 instance;

    public SuKien20Thang10() {
        super(ID, "Sự Kiện 20 Tháng 10");
        shopName = "Cửa Hàng Phụ Nữ VN";
        costItemId = ITEM_CANH_HOA;
        costItemType = 4;
        sellableItems = new short[]{ITEM_GIAY_GOI, ITEM_RUY_BANG_TIM};

        this.bxhSubTypes = new int[]{1101, 1102};
        this.bxhNames = new String[]{"Top Gói Hộp Quà", "Top Hộp Quà Đặc Biệt"};

        initShop();
    }

    public static SuKien20Thang10 gI() {
        if (instance == null) {
            instance = new SuKien20Thang10();
        }
        return instance;
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            575, 576, 578, 582, 583, 584, 585, 586, 587, 590, 591, 592, 593, 594, 595, 596, 880
        };
    }

    @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKien20Thang10(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();

        // 1. Cấu hình Activity Rewards (Dùng String Key chuẩn kết hợp index dự phòng)
        lienTangReward = new ActivityReward(KEY_DAILY_LIEN_TANG, IDX_DAILY_LIEN_TANG, 5); // 5 lần/ngày
        lienTangReward.addItemGift(ITEM_HOP_QUA_DB, (byte) 4, 1);
        lienTangReward.addItemGift(ITEM_CANH_HOA, (byte) 4, 3);

        sieuLienTangReward = new ActivityReward(KEY_DAILY_SIEU_LIEN_TANG, IDX_DAILY_SIEU_LIEN_TANG, 3); // 3 lần/ngày
        sieuLienTangReward.addItemGift(ITEM_HOP_QUA_DB, (byte) 4, 2);
        sieuLienTangReward.addItemGift(ITEM_CANH_HOA, (byte) 4, 5);

        wantedReward = new ActivityReward(KEY_DAILY_WANTED, IDX_DAILY_WANTED, 10); // 10 lần/ngày
        wantedReward.addItemGift(ITEM_GIAY_GOI, (byte) 4, 2);
        wantedReward.addItemGift(ITEM_CANH_HOA, (byte) 4, 3);

        trainReward = new ActivityReward(KEY_DAILY_TRAIN, IDX_DAILY_TRAIN, 5); // 5 lần/ngày
        trainReward.addItemGift(ITEM_RUY_BANG_TIM, (byte) 4, 2);
        trainReward.addItemGift(ITEM_CANH_HOA, (byte) 4, 2);

        pvpReward = new ActivityReward(KEY_DAILY_PVP, IDX_DAILY_PVP, 5); // 5 lần/ngày
        pvpReward.addItemGift(ITEM_CANH_HOA, (byte) 4, 2);
        pvpReward.addItemGift(ITEM_GIAY_GOI, (byte) 4, 1);

        // 2. Cấu hình Rơi Đồ Khi Hoàn Thành Các Phó Bản (dispatchMapDrop)
        mapDrops.clear();
        // Hang Động (map.zones.Map_Hang_Dong)
        mapDrops.add(new EventMapDrop(map.zones.Map_Hang_Dong.class, -1, 80, 4, ITEM_CANH_HOA, 3));
        mapDrops.add(new EventMapDrop(map.zones.Map_Hang_Dong.class, -1, 50, 4, ITEM_GIAY_GOI, 1));

        // Phó Bản Mr3 (map.zones.Map_Mr3)
        mapDrops.add(new EventMapDrop(map.zones.Map_Mr3.class, -1, 80, 4, ITEM_CANH_HOA, 5));
        mapDrops.add(new EventMapDrop(map.zones.Map_Mr3.class, -1, 60, 4, ITEM_RUY_BANG_TIM, 1));

        // Thử Thách Vệ Thần (map.zones.PhoBanThuThachVeThan)
        mapDrops.add(new EventMapDrop(map.zones.PhoBanThuThachVeThan.class, -1, 80, 4, ITEM_CANH_HOA, 5));
        mapDrops.add(new EventMapDrop(map.zones.PhoBanThuThachVeThan.class, -1, 40, 4, ITEM_RUONG_DAU_LAU, 1));

        // Liên Tầng (map.zones.Map_Lien_Tang)
        mapDrops.add(new EventMapDrop(map.zones.Map_Lien_Tang.class, -1, 100, 4, ITEM_CANH_HOA, 2));

        // Siêu Liên Tầng (map.zones.Map_Sieu_Lien_Tang)
        mapDrops.add(new EventMapDrop(map.zones.Map_Sieu_Lien_Tang.class, -1, 100, 4, ITEM_CANH_HOA, 5));
        mapDrops.add(new EventMapDrop(map.zones.Map_Sieu_Lien_Tang.class, -1, 40, 4, ITEM_HOP_QUA_DB, 1));

        // Vườn Cam (map.zones.VuonCam)
        mapDrops.add(new EventMapDrop(map.zones.VuonCam.class, -1, 70, 4, ITEM_GIAY_GOI, 2));
        mapDrops.add(new EventMapDrop(map.zones.VuonCam.class, -1, 50, 4, ITEM_RUY_BANG_TIM, 1));

        // 3. Cấu hình Rơi Đồ Boss Ngoài Bản Đồ (dispatchBossDrop)
        bossDrops.clear();
        bossDrops.add(new EventBossDrop(-1, 100, 4, ITEM_CANH_HOA, 4));
        bossDrops.add(new EventBossDrop(-1, 100, 4, ITEM_GIAY_GOI, 2));
        bossDrops.add(new EventBossDrop(-1, 100, 4, ITEM_RUY_BANG_TIM, 2));
        bossDrops.add(new EventBossDrop(-1, 35, 4, ITEM_RUONG_DAU_LAU, 1));
        bossDrops.add(new EventBossDrop(-1, 15, 4, ITEM_HOP_QUA_DB, 1));

        System.out.println("[SuKien20Thang10] Init completed with full mob drops, boss drops & dungeons support.");
    }

    @Override
    public void initPlayerData(EventData data) {
        int size = Math.max(35, shopItems.size() + 20);
        if (data.data == null || data.data.length < size) {
            int[] newData = new int[size];
            if (data.data != null) {
                System.arraycopy(data.data, 0, newData, 0, Math.min(data.data.length, size));
            } else {
                for (int i = 0; i < shopItems.size() && i < size; i++) {
                    newData[i] = shopItems.get(i).limitNumBuy;
                }
            }
            data.data = newData;
        }
        super.initPlayerData(data);
    }

    @Override
    public void resetDailyData(EventData data) {
        if (data == null || data.data == null) return;
        super.resetDailyData(data);
        // Reset toàn bộ số lượt hoạt động hàng ngày
        for (int j = 22; j < data.data.length; j++) {
            data.data[j] = 0;
        }
    }

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -1041 || npcId == -100 || npcId == ID || npcId == -881) { // Robin NPC / Rubin / NpcRobin20Thang10
            if (p.level < 10) {
                p.getService().send_box_ThongBao_OK("Bạn cần đạt cấp độ 10 trở lên để tham gia Sự kiện 20 Tháng 10!");
                return true;
            }
            p.menus.clear();
            p.menus.add(new model.Menu("Gói Hộp Quà", (short) 165, () -> {
                try {
                    showGoiHopQuaMenu(p);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("Gói Hộp Quà Đặc Biệt VIP", (short) 165, () -> {
                try {
                    new itemz.rebuilds.GhepHopQuaDacBiet().show_table(p);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("Tặng Gấu Bông ", (short) 168, () -> {
                try {
                    tangGauBong(p);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("Tặng Cành Hoa ", (short) 174, () -> {
                try {
                    tangCanhHoa(p);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("Vòng Quay May Mắn", (short) 141, () -> {
                try {
                    openLuckyWheel(p);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("BXH Gói Hộp Quà", (short) 124, () -> {
                try {
                    showEventRankDirect(p, 1101);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("BXH Hộp Quà Đặc Biệt", (short) 124, () -> {
                try {
                    showEventRankDirect(p, 1102);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("Cửa Hàng Sự Kiện", (short) 104, () -> {
                try {
                    openShop(p);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("Tích Nạp Sự Kiện", (short) 134, () -> {
                try {
                    showTichNap(p);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("Tích Tiêu Ruby", (short) 135, () -> {
                try {
                    showTichTieu(p);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("Hướng Dẫn", (short) 123, () -> {
                try {
                    String txt = "Sự Kiện Ngày Phụ Nữ VN 20.10\n"
                            + "- Cấp độ tham gia: Level 10 trở lên.\n"
                            + "- Đánh quái +-10 level rơi Cành Hoa 20.10, Giấy Gói Quà, Ruy Băng Tím, Gấu Bông (tối đa 150/ngày).\n"
                            + "- Đánh quái biển rơi Cành Hoa 20.10, Ruy Băng Tím, Rương Đầu Lâu (tối đa 100/ngày).\n"
                            + "- Tiêu diệt Boss bản đồ nhận lượng lớn nguyên liệu và cơ hội nhận Rương Đầu Lâu, Hộp Quà ĐB!\n"
                            + "- Gói Hộp Quà 1-4 & Hộp Quà Đặc Biệt tại Chị Robin để mở nhận 100% quà cực khủng theo mô tả (Beri, Ruby, Đá Hải Thạch c4/c5/c6, Khảm Lv6 tự chọn, Vé x3 skill, Rương đại ác quỷ, Vé khóa EXP, Xu hành trình, Sao 8 cánh).\n"
                            + "- Tặng Gấu Bông & Cành Hoa cho Chị Robin để nhận Beri, Ruby, EXP và Điểm Sự Kiện đua TOP!";
                    p.getService().Help_From_Server(npcId, txt);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.getService().openDynamicMenu(npcId, "Chị Robin", p.menus);
            return true;
        }
        return false;
    }

    public void tangGauBong(Player p) throws IOException {
        if (p == null) return;
        int total = p.item.total_item_bag_by_id(4, ITEM_GAU_BONG);
        if (total < 1) {
            p.getService().send_box_ThongBao_OK("Bạn không có Gấu Bông trong hành trang!");
            return;
        }
        p.item.remove_item47(4, ITEM_GAU_BONG, 1);
        p.addEventPoint(ID, KEY_TOP_GOI_HOP_QUA, 20, 5);
        p.addEventPoint(ID, KEY_POINT_EVENT1, 20, 5);
        long exp = (long) p.level * 5000L;
        p.update_exp(exp, true);
        p.update_vang(200_000);
        p.update_ruby(2);
        p.item.updateInventory(false);

        List<template.GiftBox> listGift = new ArrayList<>();
        listGift.add(new template.GiftBox(4, 0, 200_000));
        listGift.add(new template.GiftBox(4, 1, 2));
        listGift.add(new template.GiftBox(99, 0, (int) exp));
        core.RewardService.sendGiftOrMail(p, 1, "Tặng Quà Robin", "Cảm ơn bạn đã tặng Gấu Bông xinh xắn!", listGift, true);
    }

    public void tangCanhHoa(Player p) throws IOException {
        if (p == null) return;
        int total = p.item.total_item_bag_by_id(4, ITEM_CANH_HOA);
        if (total < 1) {
            p.getService().send_box_ThongBao_OK("Bạn không có Cành Hoa 20.10 trong hành trang!");
            return;
        }
        p.item.remove_item47(4, ITEM_CANH_HOA, 1);
        p.addEventPoint(ID, KEY_TOP_GOI_HOP_QUA, 20, 1);
        p.addEventPoint(ID, KEY_POINT_EVENT1, 20, 1);
        long exp = (long) p.level * 1000L;
        p.update_exp(exp, true);
        p.update_vang(50_000);
        p.item.updateInventory(false);

        List<template.GiftBox> listGift = new ArrayList<>();
        listGift.add(new template.GiftBox(4, 0, 50_000));
        listGift.add(new template.GiftBox(99, 0, (int) exp));
        core.RewardService.sendGiftOrMail(p, 1, "Tặng Quà Robin", "Cảm ơn bạn đã tặng Cành Hoa 20.10!", listGift, true);
    }

    public void showGoiHopQuaMenu(Player p) throws IOException {
        p.menus.clear();
        p.menus.add(new model.Menu("Gói Hộp Quà 1", (short) 165, () -> {
            try { new itemz.rebuilds.GhepHopQuaThuong1().show_table(p); } catch (Exception e) { e.printStackTrace(); }
        }));
        p.menus.add(new model.Menu("Gói Hộp Quà 2", (short) 165, () -> {
            try { new itemz.rebuilds.GhepHopQuaThuong2().show_table(p); } catch (Exception e) { e.printStackTrace(); }
        }));
        p.menus.add(new model.Menu("Gói Hộp Quà 3", (short) 165, () -> {
            try { new itemz.rebuilds.GhepHopQuaThuong3().show_table(p); } catch (Exception e) { e.printStackTrace(); }
        }));
        p.menus.add(new model.Menu("Gói Hộp Quà 4", (short) 165, () -> {
            try { new itemz.rebuilds.GhepHopQuaThuong4().show_table(p); } catch (Exception e) { e.printStackTrace(); }
        }));
        p.getService().openDynamicMenu(9460, "Chọn Hộp Quà Gói", p.menus);
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (p.level < 10) {
            p.getService().send_box_ThongBao_OK("Bạn cần đạt cấp độ 10 trở lên để tham gia Sự kiện 20 Tháng 10!");
            return true;
        }
        if (p.menus != null && index >= 0 && index < p.menus.size()) {
            p.menus.get(index).execute(p, index);
            return true;
        }
        // Fallback if p.menus was cleared or dispatched via legacy NPC handler
        switch (index) {
            case 0:
                showGoiHopQuaMenu(p);
                return true;
            case 1:
                try { new itemz.rebuilds.GhepHopQuaDacBiet().show_table(p); } catch (Exception e) { e.printStackTrace(); }
                return true;
            case 2:
                tangGauBong(p);
                return true;
            case 3:
                tangCanhHoa(p);
                return true;
            case 4:
                try { openLuckyWheel(p); } catch (Exception e) { e.printStackTrace(); }
                return true;
            case 5:
                try { showEventRankDirect(p, 1101); } catch (Exception e) { e.printStackTrace(); }
                return true;
            case 6:
                try { showEventRankDirect(p, 1102); } catch (Exception e) { e.printStackTrace(); }
                return true;
            case 7:
                try { openShop(p); } catch (Exception e) { e.printStackTrace(); }
                return true;
            case 8:
                try { showTichNap(p); } catch (Exception e) { e.printStackTrace(); }
                return true;
            case 9:
                try { showTichTieu(p); } catch (Exception e) { e.printStackTrace(); }
                return true;
            case 10:
                try {
                    String txt = "Sự Kiện Ngày Phụ Nữ VN 20.10\n"
                            + "- Cấp độ tham gia: Level 10 trở lên.\n"
                            + "- Đánh quái +-10 level rơi Cành Hoa 20.10, Giấy Gói Quà, Ruy Băng Tím, Gấu Bông (tối đa 150/ngày).\n"
                            + "- Đánh quái biển rơi Cành Hoa 20.10, Ruy Băng Tím, Rương Đầu Lâu (tối đa 100/ngày).\n"
                            + "- Tiêu diệt Boss bản đồ nhận lượng lớn nguyên liệu và cơ hội nhận Rương Đầu Lâu, Hộp Quà ĐB!\n"
                            + "- Gói Hộp Quà 1-4 & Hộp Quà Đặc Biệt tại Chị Robin để mở nhận 100% quà cực khủng theo mô tả.\n"
                            + "- Tặng Gấu Bông & Cành Hoa cho Chị Robin để nhận Beri, Ruby, EXP và Điểm Sự Kiện đua TOP!";
                    p.getService().Help_From_Server(npcId, txt);
                } catch (Exception e) { e.printStackTrace(); }
                return true;
        }
        return false;
    }

    @Override
    public boolean onUseItem(Player p, int id) {
        if (p.level < 10) {
            try {
                p.getService().send_box_ThongBao_OK("Bạn cần đạt cấp độ 10 trở lên để sử dụng vật phẩm này!");
            } catch (Exception ignored) {}
            return false;
        }
        try {
            // 1. Sử dụng Pháo Hoa
            if (id == ITEM_PHAO_HOA) {
                if (p.item.total_item_bag_by_id(4, id) < 1) return false;
                p.item.remove_item47(4, id, 1);
                p.addEventPoint(ID, "point_event1", 20, 1);
                p.addEventPoint(ID, "top_goi_hop_qua", 20, 1);

                if (p.map != null && p.map.players != null) {
                    for (Player pl : p.map.players) {
                        if (pl != null && pl.conn != null && pl.conn.status == 1) {
                            List<GiftBox> listGift = new ArrayList<>();
                            listGift.add(new GiftBox(99, 0, (int) Math.min(2_000_000_000L, (long) pl.level * 2000L)));
                            listGift.add(new GiftBox(4, 0, 50000));
                            core.RewardService.sendGiftOrMail(pl, 1, "Pháo Hoa 20/10", p.name + " vừa đốt Pháo hoa mừng ngày 20.10!", listGift, true);
                        }
                    }
                }
                p.item.updateInventory(false);
                return true;
            }

            // 2. Sử dụng Hộp Quà 1-4 và Hộp Quà Đặc Biệt -> Cấp đúng 100% quà như mô tả
            if (id >= ITEM_HOP_QUA_1 && id <= ITEM_HOP_QUA_DB) {
                return model.UseItem.open_hop_qua_20_10(p, id);
            }

            // 3. Sử dụng Rương Đầu Lâu
            if (id == ITEM_RUONG_DAU_LAU) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Rương Đầu Lâu!");
                    return false;
                }
                
                long expEarn = (long) p.level * 25000L;
                int rubyEarn = 25 + ZUtil.random(50);
                long beriEarn = 500000L;

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, expEarn)));
                listGift.add(new template.GiftBox(4, 1, rubyEarn));
                listGift.add(new template.GiftBox(4, 0, (int) beriEarn));

                int rand = ZUtil.random(100);
                if (rand < 35) {
                    listGift.add(new template.GiftBox(4, 158, 1)); // Rương đại ác quỷ
                    Manager.gI().chatKTG(0, "Chúc mừng [" + p.name + "] mở Rương Đầu Lâu 20/10 nhận được [Rương Đại Ác Quỷ]!", 0);
                } else if (rand < 65) {
                    int chestId = (p.level < 20) ? 122 : Math.min(131, 122 + (p.level / 10 - 1));
                    listGift.add(new template.GiftBox(4, chestId, 2)); // Rương Cam Cùng Hệ x2
                } else if (rand < 85) {
                    listGift.add(new template.GiftBox(7, 2, 2)); // Bột vàng x2
                } else if (rand < 95) {
                    listGift.add(new template.GiftBox(4, 823, 1)); // Rương Dial Truyền Thuyết
                } else {
                    listGift.add(new template.GiftBox(4, 753, 1)); // Sách Haki Vũ Trang
                    Manager.gI().chatKTG(0, "Chúc mừng [" + p.name + "] mở Rương Đầu Lâu 20/10 nhận được [Sách Haki Vũ Trang]!", 0);
                }
                p.item.remove_item47(4, id, 1);
                Message m2 = new Message(-13);
                m2.writer().writeShort(id);
                m2.writer().writeShort(p.item.total_item_bag_by_id(4, id));
                p.conn.addmsg(m2);
                m2.cleanup();
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Rương Đầu Lâu 20.10", "Phần thưởng mở Rương Đầu Lâu", listGift, true);
                return true;
            }

            // 4. Bấm vào nguyên liệu sự kiện (Gấu bông, Cành hoa, Giấy gói, Ruy băng) -> Mở menu NPC Robin
            if (id == ITEM_GAU_BONG || id == ITEM_CANH_HOA || id == ITEM_GIAY_GOI || id == ITEM_RUY_BANG_TIM) {
                sendMenu(p, -881);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!event.EventManager.isActive(id) || mob == null || mob.is_boss) return;
        if (!isDropItemActive()) return;
        if (p == null || p.level < 10) return;
        p = p.getOwnerPlayer();
        if (p.isBot || p.item == null) return;

        // Tiêu diệt Quái thường
        if (mob.map != null && mob.map.template != null) {
            if (Zone.is_map_sea(mob.map.template.id)) {
                // Quái biển: Rơi Cành Hoa, Ruy Băng Tím, Rương Đầu Lâu (tối đa 100/ngày)
                int[] seaItems = new int[]{ITEM_CANH_HOA, ITEM_RUY_BANG_TIM, ITEM_RUONG_DAU_LAU};
                dropEventItemRandom(p, mob, seaItems, 15, 100, KEY_DAILY_DROP_SEA, IDX_DAILY_DROP_SEA);
            } else {
                // Quái đất liền: Rơi Cành Hoa, Giấy Gói Quà, Ruy Băng Tím (tối đa 150/ngày)
                int[] landItems = new int[]{ITEM_CANH_HOA, ITEM_GIAY_GOI, ITEM_RUY_BANG_TIM};
                dropEventItemRandom(p, mob, landItems, 18, 150, KEY_DAILY_DROP_MOB, IDX_DAILY_DROP_MOB);
            }
        }
    }

    @Override
    public void onVanChuyen(Player p) {
        if (!event.EventManager.isActive(id) || !isDropItemActive()) return;
        if (p == null || p.level < 10 || p.isBot || p.item == null) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.getLimit(KEY_DAILY_VAN_CHUYEN) < 5) {
            evData.addLimit(KEY_DAILY_VAN_CHUYEN, 1);
            if (evData.data != null && evData.data.length > IDX_DAILY_VAN_CHUYEN) evData.data[IDX_DAILY_VAN_CHUYEN]++;
            p.item.add_item_bag47(4, ITEM_RUY_BANG_TIM, 2);
            p.item.add_item_bag47(4, ITEM_CANH_HOA, 3);
            p.item.updateInventory(false);
        }
    }

    @Override
    public void onVuonCam(Player p, int round) {
        if (!event.EventManager.isActive(id) || !isDropItemActive()) return;
        if (p == null || p.level < 10 || p.isBot || p.item == null) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.getLimit(KEY_DAILY_VUON_CAM) < 3) {
            evData.addLimit(KEY_DAILY_VUON_CAM, 1);
            if (evData.data != null && evData.data.length > IDX_DAILY_VUON_CAM) evData.data[IDX_DAILY_VUON_CAM]++;
            p.item.add_item_bag47(4, ITEM_GIAY_GOI, 2);
            p.item.add_item_bag47(4, ITEM_RUY_BANG_TIM, 1);
            p.item.add_item_bag47(4, ITEM_CANH_HOA, 3);
            p.item.updateInventory(false);
        }
    }

    @Override
    public void onNhiemVuLap(Player p) {
        if (!event.EventManager.isActive(id) || !isDropItemActive()) return;
        if (p == null || p.level < 10 || p.isBot || p.item == null) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.getLimit(KEY_DAILY_NV_LAP) < 5) {
            evData.addLimit(KEY_DAILY_NV_LAP, 1);
            if (evData.data != null && evData.data.length > IDX_DAILY_NV_LAP) evData.data[IDX_DAILY_NV_LAP]++;
            p.item.add_item_bag47(4, ITEM_CANH_HOA, 2);
            p.item.add_item_bag47(4, ITEM_GIAY_GOI, 1);
            p.item.updateInventory(false);
        }
    }

    @Override
    public void onPvP(Player p) {
        super.onPvP(p);
    }

    @Override
    public void onLienTang(Player p, int floor) {
        super.onLienTang(p, floor);
    }

    @Override
    public void onSieuLienTang(Player p) {
        super.onSieuLienTang(p);
    }

    @Override
    public void onWanted(Player p) {
        super.onWanted(p);
    }

    @Override
    public void onTrainMob(Player p) {
        super.onTrainMob(p);
    }
}

