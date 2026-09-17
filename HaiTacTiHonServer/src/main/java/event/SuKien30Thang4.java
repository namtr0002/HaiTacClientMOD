package event;

import model.Player;
import core.ZUtil;
import itemz.MainItem;
import mob.Mob;
import map.Zone;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuKien30Thang4 extends Event {

    public static final int ID = Event.ID_SUKIEN_30THANG41THANG5_2026;

    // Materials
    public static final int ITEM_BONG_BAC = 797;
    public static final int ITEM_BONG_DONG = 798;
    public static final int ITEM_BONG_VANG = 799;
    public static final int ITEM_RUY_BANG_DO = 800;

    // Crafted items
    public static final int ITEM_HOP_DO = 803;
    public static final int ITEM_HOP_TIM = 804;
    public static final int ITEM_CUP_VANG = 800;
    public static final int ITEM_CUP_BAC = 798;
    public static final int ITEM_RUONG_CHIEN_THANG = 887;

    private static SuKien30Thang4 instance;

    public SuKien30Thang4() {
        super(ID, "Sự Kiện 30 Tháng 4");
        shopName = "Shop Chiến Thắng";
        costItemId = ITEM_HOP_DO;
        costItemType = 4;

        this.bxhSubTypes = new int[]{601, 602};
        this.bxhNames = new String[]{"Top Hộp Kim Cương Đỏ", "Top Hộp Kim Cương Tím"};

        initShop();
    }

    public static SuKien30Thang4 gI() {
        if (instance == null) {
            instance = new SuKien30Thang4();
        }
        return instance;
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            797, 798, 799, 800, 803, 804
        };
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKien30Thang4(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();

        // Activity rewards
        lienTangReward = new ActivityReward(2, 5); // 5 lần/ngày
        lienTangReward.addItemGift(ITEM_HOP_TIM, (byte) 4, 1);

        wantedReward = new ActivityReward(3, 10); // 10 lần/ngày
        wantedReward.addItemGift(ITEM_BONG_VANG, (byte) 4, 2);

        trainReward = new ActivityReward(4, 5); // 5 lần/ngày
        trainReward.addItemGift(ITEM_RUY_BANG_DO, (byte) 4, 2);

        System.out.println("[SuKien30Thang4] Init completed with full features.");
    }

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -1007 || npcId == -100 || npcId == ID) { // Garp NPC / Rubin
            p.getService().openDynamicMenu(npcId, "Phó Đô Đốc Garp", new String[]{
                "Gói Hộp Kim Cương Đỏ (Thường)",
                "Làm Hộp Kim Cương Tím (VIP)",
                "Chế Tạo Rương Chiến Thắng 30/4",
                "BXH Hộp Kim Cương Đỏ",
                "BXH Hộp Kim Cương Tím",
                "Shop Chiến Thắng",
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
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -1007 || npcId == -100 || npcId == ID) {
            switch (index) {
                case 0:
                    new itemz.rebuilds.GhepKimCuongDo803().show_table(p);
                    return true;
                case 1:
                    new itemz.rebuilds.GhepKimCuongTim804().show_table(p);
                    return true;
                case 2:
                    new itemz.rebuilds.GhepRuongChienThang807().show_table(p);
                    return true;
                case 3:
                    p.typeBXH = 601;
                    showEventRank(p);
                    return true;
                case 4:
                    p.typeBXH = 602;
                    showEventRank(p);
                    return true;
                case 5:
                    openShop(p);
                    return true;
                case 6: {
                    String txt = "Sự Kiện Giải Phóng 30/4 & Quốc Tế Lao Động 1/5\n"
                            + "- Đánh quái +-10 level rơi Bóng Bạc, Bóng Đồng, Bóng Vàng, Ruy Băng Đỏ (tối đa 150/ngày).\n"
                            + "- Tham gia Phó bản, Liên tầng, Vận buôn để nhận thêm Bóng Vàng và Hộp Kim Cương Tím.\n"
                            + "- Công thức ghép tại Phó Đô Đốc Garp:\n"
                            + "  + Hộp Kim Cương Đỏ: 5 Bóng Bạc + 5 Bóng Đồng + 1 Ruy Băng Đỏ + 10k Beri\n"
                            + "  + Hộp Kim Cương Tím: 5 Bóng Vàng + 5 Bóng Bạc + 1 Ruy Băng Đỏ + 5 Ruby\n"
                            + "  + Rương Chiến Thắng 30/4: 3 Hộp Kim Cương Đỏ + 3 Hộp Kim Cương Tím + 10 Ruby\n"
                            + "- Mở hộp và rương để nhận EXP khủng, Ruby, Rương Đại Ác Quỷ, Cúp Vàng và Thời Trang Franky/Euro!";
                    p.getService().Help_From_Server(npcId, txt);
                    return true;
                }
                case 7:
                    showTichNap(p);
                    return true;
                case 8:
                    showTichTieu(p);
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean onUseItem(Player p, int id) {
        if (!isEventActive()) {
            if (id == ITEM_HOP_DO || id == ITEM_HOP_TIM || id == ITEM_RUONG_CHIEN_THANG || id == ITEM_CUP_VANG || id == 887) {
                p.getService().send_box_ThongBao_OK("Sự kiện 30 Tháng 4 đã kết thúc!");
                return false;
            }
            return false;
        }
        try {
            if (id == ITEM_HOP_DO) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Hộp Kim Cương Đỏ!");
                    return false;
                }
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 4000;

                java.util.List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));

                int rand = ZUtil.random(100);
                if (rand < 50) {
                    listGift.add(new template.GiftBox(4, 0, 150_000));
                } else if (rand < 80) {
                    listGift.add(new template.GiftBox(7, 1, 1)); // Bột cường hóa
                } else {
                    listGift.add(new template.GiftBox(4, 135, 1)); // Tinh thể đá
                }
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Hộp Kim Cương Đỏ", "Phần thưởng", listGift, true);
                return true;
            } else if (id == ITEM_HOP_TIM) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Hộp Kim Cương Tím!");
                    return false;
                }
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 10000;
                int rubyEarn = 5 + ZUtil.random(15);

                java.util.List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 1, rubyEarn));

                int rand = ZUtil.random(100);
                if (rand < 40) {
                    listGift.add(new template.GiftBox(7, 2, 1)); // Bột vàng
                } else if (rand < 75) {
                    listGift.add(new template.GiftBox(4, 122, 1)); // Rương Cam Cùng Hệ
                } else {
                    listGift.add(new template.GiftBox(4, 806, 1)); // Cờ chiến thắng
                }
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Hộp Kim Cương Tím", "Phần thưởng", listGift, true);
                return true;
            } else if (id == ITEM_RUONG_CHIEN_THANG) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Rương Chiến Thắng!");
                    return false;
                }
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 25000;
                int rubyEarn = 20 + ZUtil.random(50);

                java.util.List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 1, rubyEarn));

                int rand = ZUtil.random(100);
                if (rand < 35) {
                    listGift.add(new template.GiftBox(4, 158, 1)); // Rương đại ác quỷ
                } else if (rand < 70) {
                    listGift.add(new template.GiftBox(4, 805, 1)); // Cúp vàng chiến thắng
                } else {
                    listGift.add(new template.GiftBox(7, 2, 3)); // Bột vàng x3
                }
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Rương Chiến Thắng 30/4", "Phần thưởng", listGift, true);
                return true;
            } else if (id == ITEM_CUP_VANG) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Cúp Vàng Chiến Thắng!");
                    return false;
                }
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 15000;

                java.util.List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 1, 20));
                listGift.add(new template.GiftBox(4, 0, 500_000));

                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Cúp Vàng Chiến Thắng", "Phần thưởng", listGift, true);
                return true;
            } else if (id == ITEM_CUP_BAC) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Cúp Bạc Chiến Thắng!");
                    return false;
                }
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 5000;

                java.util.List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 0, 200_000));

                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Cúp Bạc Chiến Thắng", "Phần thưởng", listGift, true);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!event.EventManager.isActive(ID) || mob.is_boss) return;
        if (!isDropItemActive()) return;

        if (!Zone.is_map_sea(mob.map.template.id)) {
            int[] items = new int[]{ITEM_BONG_BAC, ITEM_BONG_DONG, ITEM_BONG_VANG, ITEM_RUY_BANG_DO};
            dropEventItemRandom(p, mob, items, 18, 150, 0); // 18% cơ bản, giới hạn 150/ngày
        }
    }

    @Override
    public void onVanChuyen(Player p) {
        if (!event.EventManager.isActive(ID)) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[5] < 5) {
            evData.data[5]++;
            p.item.add_item_bag47(4, ITEM_RUY_BANG_DO, 2);
            p.item.updateInventory(false);
        }
    }

    @Override
    public void onVuonCam(Player p, int round) {
        if (!event.EventManager.isActive(ID)) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[6] < 3) {
            evData.data[6]++;
            p.item.add_item_bag47(4, ITEM_RUONG_CHIEN_THANG, 1);
            p.item.updateInventory(false);
        }
    }
}
