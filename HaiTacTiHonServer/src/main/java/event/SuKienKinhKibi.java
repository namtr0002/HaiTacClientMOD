package event;

import model.Player;
import network.Message;
import template.ItemFashion;
import template.ItemTemplate4;
import template.ItemTemplate7;
import mob.Mob;
import map.Zone;
import core.ZUtil;
import itemz.MainItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuKienKinhKibi extends Event {

    public static final int ID = Event.ID_SUKIEN_TETDUONGLICH_2026; // 4

    // Materials & Items
    public static final int ITEM_RUONG_BI_AN = 459;
    public static final int ITEM_CHIA_KHOA_BI_AN = 460;
    public static final int ITEM_RUONG_KI_BI = 462;
    public static final int ITEM_KINH_KI_BI = 463;
    public static final int ITEM_RUONG_KI_BI_S = 470;
    public static final int ITEM_HUY_HIEU_BI_AN = 480;
    public static final int ITEM_MANH_BAN_DO_CO = 481;

    private static SuKienKinhKibi instance;

    public static SuKienKinhKibi gI() {
        if (instance == null) {
            instance = new SuKienKinhKibi();
        }
        return instance;
    }

    public SuKienKinhKibi() {
        super(ID, "Sự Kiện Kinh Kì Bí");
        this.shopName = "Shop Kinh Kì Bí";
        this.costItemId = ITEM_KINH_KI_BI;
        this.costItemType = 4;

        this.bxhSubTypes = new int[]{401, 402};
        this.bxhNames = new String[]{"Top Mở Rương Kỳ Bí", "Top Ghép Kính"};

        initShop();
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            460, 461, 462, 463, 464, 465, 466, 470, 826
        };
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienKinhKibi(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();

        // Activity rewards
        lienTangReward = new ActivityReward(2, 5); // 5 lần/ngày
        lienTangReward.addItemGift(ITEM_RUONG_KI_BI_S, (byte) 4, 1);

        wantedReward = new ActivityReward(3, 10); // 10 lần/ngày
        wantedReward.addItemGift(ITEM_CHIA_KHOA_BI_AN, (byte) 4, 2);

        trainReward = new ActivityReward(4, 5); // 5 lần/ngày
        trainReward.addItemGift(ITEM_MANH_BAN_DO_CO, (byte) 4, 2);

        System.out.println("[SuKienKinhKibi] Init completed with full features.");
    }

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -874 || npcId == -1022 || npcId == -100 || npcId == ID) { // NPC Cổ Thư / Rubin
            p.getService().openDynamicMenu(npcId, "Cổ Thư Kỳ Bí", new String[]{
                "Cửa Hàng Kinh Kỳ Bí",
                "Ghép Cổ Thư Kibi",
                "BXH Mở Rương Kỳ Bí",
                "BXH Ghép Kính Kỳ Bí",
                "Hướng dẫn",
                "Tích nạp sự kiện",
                "Tích tiêu sự kiện"
            }, null);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMenu(Player p, int menuId, int index) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (menuId == -874 || menuId == -1022 || menuId == -100 || menuId == ID) {
            switch (index) {
                case 0:
                    openShop(p);
                    break;
                case 1:
                    new itemz.rebuilds.GhepCoThuKibi().show_table(p);
                    break;
                case 2:
                    p.typeBXH = 401;
                    showEventRank(p);
                    break;
                case 3:
                    p.typeBXH = 402;
                    showEventRank(p);
                    break;
                case 4:
                    p.getService().Help_From_Server(menuId, "Sự Kiện Săn Kinh Kỳ Bí & Cải Trang Tây Du Ký\n"
                            + "- Đánh quái +-10 level rơi Rương Bí Ẩn, Chìa Khóa Bí Ẩn và Rương Kì Bí.\n"
                            + "- Mở Rương Kì Bí để nhận Kinh Kì Bí, Rương Kì Bí Cấp S, Mảnh Bản Đồ Cổ và Huy Hiệu Bí Ẩn.\n"
                            + "- Dùng Kinh Kì Bí đổi trọn bộ Cải Trang Tây Du Ký (Ngộ Không, Đường Tăng, Bát Giới, Sa Tăng), Trái Sét, Trái Nham Thạch và Rương TAQ Tự Chọn!");
                    break;
                case 5:
                    showTichNap(p);
                    break;
                case 6:
                    showTichTieu(p);
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onUseItem(Player p, int itemId) throws IOException {
        if (!isEventActive()) {
            if (itemId == ITEM_RUONG_BI_AN || itemId == ITEM_CHIA_KHOA_BI_AN || itemId == ITEM_RUONG_KI_BI || itemId == ITEM_KINH_KI_BI || itemId == ITEM_RUONG_KI_BI_S || itemId == ITEM_HUY_HIEU_BI_AN || itemId == ITEM_MANH_BAN_DO_CO) {
                p.getService().send_box_ThongBao_OK("Sự kiện Kinh Kì Bí đã kết thúc!");
                return false;
            }
            return false;
        }
        try {
            if (itemId == ITEM_RUONG_BI_AN) {
                if (p.item.total_item_bag_by_id(4, itemId) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Rương Bí Ẩn!");
                    return false;
                }
                
                int expEarn = p.level * 3000;

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));

                int rand = ZUtil.random(100);
                if (rand < 50) {
                    listGift.add(new template.GiftBox(4, 0, 100_000));
                } else if (rand < 80) {
                    listGift.add(new template.GiftBox(4, ITEM_CHIA_KHOA_BI_AN, 1));
                } else {
                    listGift.add(new template.GiftBox(4, 135, 1)); // Tinh thể đá
                }
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Rương Bí Ẩn", "Phần thưởng", listGift, true);
                return true;
            } else if (itemId == ITEM_RUONG_KI_BI) {
                if (p.item.total_item_bag_by_id(4, itemId) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Rương Kì Bí!");
                    return false;
                }
                
                int expEarn = p.level * 6000;

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));

                int rand = ZUtil.random(100);
                if (rand < 30) {
                    listGift.add(new template.GiftBox(4, ITEM_KINH_KI_BI, 1)); // Kinh Kì Bí
                } else if (rand < 60) {
                    listGift.add(new template.GiftBox(4, ITEM_HUY_HIEU_BI_AN, 1)); // Huy Hiệu
                } else if (rand < 85) {
                    listGift.add(new template.GiftBox(4, ITEM_MANH_BAN_DO_CO, 1)); // Mảnh bản đồ cổ
                } else {
                    listGift.add(new template.GiftBox(4, 122, 1)); // Rương Cam Cùng Hệ Lv10
                }
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Rương Kì Bí", "Phần thưởng", listGift, true);
                return true;
            } else if (itemId == ITEM_RUONG_KI_BI_S) {
                if (p.item.total_item_bag_by_id(4, itemId) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Rương Kì Bí Cấp S!");
                    return false;
                }
                
                int expEarn = p.level * 20000;
                int rubyEarn = 10 + ZUtil.random(30);

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 1, rubyEarn));

                int rand = ZUtil.random(100);
                if (rand < 35) {
                    listGift.add(new template.GiftBox(4, 158, 1)); // Rương đại ác quỷ
                } else if (rand < 70) {
                    listGift.add(new template.GiftBox(4, ITEM_KINH_KI_BI, 2)); // 2 Kinh Kì Bí
                } else {
                    listGift.add(new template.GiftBox(4, 122, 2)); // Rương Cam Cùng Hệ Lv10 x2
                }
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Rương Kì Bí Cấp S", "Phần thưởng", listGift, true);
                return true;
            } else if (itemId == ITEM_MANH_BAN_DO_CO) {
                if (p.item.total_item_bag_by_id(4, itemId) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Mảnh Bản Đồ Cổ!");
                    return false;
                }
                
                int expEarn = p.level * 8000;

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 0, 300_000));

                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Mảnh Bản Đồ Cổ", "Phần thưởng", listGift, true);
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
            int[] items = new int[]{ITEM_RUONG_BI_AN, ITEM_CHIA_KHOA_BI_AN, ITEM_RUONG_KI_BI};
            dropEventItemRandom(p, mob, items, 15, 150, 0); // 15% cơ bản, giới hạn 150/ngày
        }
    }

    @Override
    public void onVanChuyen(Player p) {
        if (!event.EventManager.isActive(ID)) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[5] < 5) {
            evData.data[5]++;
            p.item.add_item_bag47(4, ITEM_MANH_BAN_DO_CO, 2);
            p.item.updateInventory(false);
        }
    }

    @Override
    public void onVuonCam(Player p, int round) {
        if (!event.EventManager.isActive(ID)) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[6] < 3) {
            evData.data[6]++;
            p.item.add_item_bag47(4, ITEM_RUONG_KI_BI_S, 1);
            p.item.updateInventory(false);
        }
    }
}
