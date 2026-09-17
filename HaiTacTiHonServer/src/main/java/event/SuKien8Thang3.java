package event;

import model.Player;
import core.ZUtil;
import itemz.MainItem;
import mob.Mob;
import map.Zone;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuKien8Thang3 extends Event {

    public static final int ID = Event.ID_SUKIEN_8THANG3_2026;

    // Materials
    public static final int ITEM_RUY_BANG = 811;
    public static final int ITEM_HOA_HONG_DO = 812;
    public static final int ITEM_HOA_HONG_VANG = 813;
    public static final int ITEM_HOA_HONG_XANH = 814;
    public static final int ITEM_GIAY_MAU = 819;

    // Crafted & Event items
    public static final int ITEM_BO_HOA_DO = 815;
    public static final int ITEM_BO_HOA_XANH = 816;
    public static final int ITEM_BO_HOA_VANG = 817;
    public static final int ITEM_LO_NUOC_HOA = 818;
    public static final int ITEM_GIO_HOA = 820;
    public static final int ITEM_HOP_BOA = 821;
    public static final int ITEM_HOP_NAMI = 822;
    public static final int ITEM_DANH_HIEU_XINH = 824;

    private static SuKien8Thang3 instance;

    public SuKien8Thang3() {
        super(ID, "Sự Kiện 8 Tháng 3");
        shopName = "Cửa Hàng Hoa Hồng";
        costItemId = ITEM_BO_HOA_DO;
        costItemType = 4;
        sellableItems = new short[]{ITEM_LO_NUOC_HOA, ITEM_GIAY_MAU};

        this.bxhSubTypes = new int[]{11, 12};
        this.bxhNames = new String[]{"Top Giỏ Hoa", "Top Bó Hoa"};

        initShop();
    }

    public static SuKien8Thang3 gI() {
        if (instance == null) {
            instance = new SuKien8Thang3();
        }
        return instance;
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            171, 526, 811, 812, 813, 814, 815, 816, 817, 818, 819, 820
        };
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKien8Thang3(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();

        // Activity rewards
        lienTangReward = new ActivityReward(2, 5); // 5 lần/ngày
        lienTangReward.addItemGift(ITEM_GIO_HOA, (byte) 4, 1);

        wantedReward = new ActivityReward(3, 10); // 10 lần/ngày
        wantedReward.addItemGift(ITEM_HOA_HONG_XANH, (byte) 4, 2);

        trainReward = new ActivityReward(4, 5); // 5 lần/ngày
        trainReward.addItemGift(ITEM_RUY_BANG, (byte) 4, 2);

        System.out.println("[SuKien8Thang3] Init completed with full features.");
    }

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -935 || npcId == -100 || npcId == ID) { // Boa Hancock NPC / Rubin
            p.getService().openDynamicMenu(npcId, "Boa Hancock", new String[]{
                "Gói Bó Hoa Hồng Đỏ", "Gói Bó Hoa Hồng Xanh", "Gói Bó Hoa Hồng Vàng", "Làm Giỏ Hoa",
                "Cửa Hàng Hoa Hồng", "BXH Giỏ Hoa", "BXH Bó Hoa", "Hướng dẫn",
                "Tích nạp sự kiện", "Tích tiêu sự kiện"
            }, null);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -935 || npcId == -100 || npcId == ID) {
            switch (index) {
                case 0:
                    new itemz.rebuilds.GhepBoHoaDo().show_table(p);
                    return true;
                case 1:
                    new itemz.rebuilds.GhepBoHoaXanh().show_table(p);
                    return true;
                case 2:
                    new itemz.rebuilds.GhepBoHoaVang().show_table(p);
                    return true;
                case 3:
                    new itemz.rebuilds.GhepGioHoa().show_table(p);
                    return true;
                case 4:
                    openShop(p);
                    return true;
                case 5:
                    p.typeBXH = 11;
                    showEventRank(p);
                    return true;
                case 6:
                    p.typeBXH = 12;
                    showEventRank(p);
                    return true;
                case 7: {
                    String txt = "Sự Kiện Quốc Tế Phụ Nữ 8 Tháng 3\n"
                            + "- Đánh quái +-10 level rơi Hoa Hồng Đỏ, Vàng, Ruy Băng, Giấy Màu (tối đa 150/ngày).\n"
                            + "- Đánh quái biển rơi Hoa Hồng Xanh (tối đa 100/ngày).\n"
                            + "- Tham gia Hoạt động để nhận thêm Giỏ Hoa và Lọ Nước Hoa.\n"
                            + "- Gói hoa tặng Boa Hancock để nhận điểm sự kiện, EXP khủng và mở hộp trang phục Boa / Nami!";
                    p.getService().Help_From_Server(npcId, txt);
                    return true;
                }
                case 8:
                    showTichNap(p);
                    return true;
                case 9:
                    showTichTieu(p);
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean onUseItem(Player p, int id) {
        if (!isEventActive()) {
            if (id == ITEM_BO_HOA_DO || id == ITEM_BO_HOA_XANH || id == ITEM_BO_HOA_VANG || id == ITEM_GIO_HOA || id == ITEM_LO_NUOC_HOA) {
                p.getService().send_box_ThongBao_OK("Sự kiện 8 Tháng 3 đã kết thúc!");
                return false;
            }
            return false;
        }
        try {
            if (id == ITEM_BO_HOA_DO || id == ITEM_BO_HOA_XANH || id == ITEM_BO_HOA_VANG) {
                if (p.item.total_item_bag_by_id(4, id) < 1) return false;
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 3500;

                java.util.List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));

                int rand = ZUtil.random(100);
                if (rand < 50) {
                    listGift.add(new template.GiftBox(4, 0, 150_000));
                } else if (rand < 80) {
                    listGift.add(new template.GiftBox(7, 1, 1)); // Bột cường hóa
                } else if (rand < 95) {
                    listGift.add(new template.GiftBox(4, 135, 1)); // Tinh thể đá
                } else {
                    listGift.add(new template.GiftBox(4, 122, 1)); // Rương Cam Cùng Hệ
                }
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, template.ItemTemplate4.get_item_name(id), "Phần thưởng", listGift, true);
                return true;
            } else if (id == ITEM_GIO_HOA) {
                if (p.item.total_item_bag_by_id(4, id) < 1) return false;
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 10000;

                java.util.List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));

                int rand = ZUtil.random(100);
                if (rand < 40) {
                    listGift.add(new template.GiftBox(4, 1, ZUtil.random(10, 25)));
                } else if (rand < 70) {
                    listGift.add(new template.GiftBox(4, 122, 1)); // Rương Cam Cùng Hệ
                } else if (rand < 90) {
                    listGift.add(new template.GiftBox(4, 821, 1)); // Hộp trang phục Boa
                } else {
                    listGift.add(new template.GiftBox(4, 822, 1)); // Hộp trang phục Nami
                }
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Giỏ Hoa Đặc Biệt", "Phần thưởng", listGift, true);
                return true;
            } else if (id == ITEM_LO_NUOC_HOA) {
                if (p.item.total_item_bag_by_id(4, id) < 1) return false;
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 8000;

                java.util.List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 0, 300_000));

                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Lọ Nước Hoa Quyến Rũ", "Phần thưởng", listGift, true);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!event.EventManager.isActive(id) || mob.is_boss) return;
        if (!isDropItemActive()) return;

        if (Zone.is_map_sea(mob.map.template.id)) {
            dropEventItem(p, mob, ITEM_HOA_HONG_XANH, 15, 100, 0);
        } else {
            int[] items = new int[]{ITEM_HOA_HONG_DO, ITEM_HOA_HONG_VANG, ITEM_RUY_BANG, ITEM_GIAY_MAU};
            dropEventItemRandom(p, mob, items, 18, 150, 0);
        }
    }

    @Override
    public void onVanChuyen(Player p) {
        if (!event.EventManager.isActive(id)) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[5] < 5) {
            evData.data[5]++;
            p.item.add_item_bag47(4, ITEM_LO_NUOC_HOA, 1);
            p.item.updateInventory(false);
        }
    }

    @Override
    public void onVuonCam(Player p, int round) {
        if (!event.EventManager.isActive(id)) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[6] < 3) {
            evData.data[6]++;
            p.item.add_item_bag47(4, ITEM_GIO_HOA, 1);
            p.item.updateInventory(false);
        }
    }
}
