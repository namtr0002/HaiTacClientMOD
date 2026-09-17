package event;

import model.Player;
import core.Manager;
import core.ZUtil;
import itemz.MainItem;
import mob.Mob;
import map.Zone;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuKienTrungThu extends Event {

    public static final int ID = Event.ID_SUKIEN_TRUNGTHU_2025;

    // Materials
    public static final int ITEM_NANG_TRE = 109;
    public static final int ITEM_DAY_KEM = 409;
    public static final int ITEM_GIAY_MAU = 819;

    // Event items
    public static final int ITEM_LONG_DEN = 111;
    public static final int ITEM_DEN_KEO_QUAN = 410;
    public static final int ITEM_BANH_TRUNG_THU = 207;
    public static final int ITEM_HOP_BANH = 211;

    private static SuKienTrungThu instance;

    public SuKienTrungThu() {
        super(ID, "Sự Kiện Trung Thu 2025");
        shopName = "Shop Cửa Hàng Trung Thu";
        costItemId = ITEM_LONG_DEN;
        this.bxhSubTypes = new int[]{801, 802};
        this.bxhNames = new String[]{"Top Lồng Đèn", "Top Đèn Kéo Quân"};
        time = "0:00:00 15/6/2026 > 23:59:59 15/10/2026";
        timex2pay = "0:00:00 15/6/2026 > 23:59:59 15/10/2026";
        timechangeitem = "0:00:00 15/6/2026 > 23:59:59 15/10/2026";
        timedropitem = "0:00:00 15/6/2026 > 23:59:59 15/10/2026";
        timeremoveitem = "0:00:00 15/6/2026 > 23:59:59 15/10/2026";
        // [NPC_MIGRATED] npcConfigs = new Object[][]{
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 1,   -877, (short) 430, (short) 170, 5073},
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 9,   -877, (short) 445, (short) 174, 5073},
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 17,  -877, (short) 390, (short) 170, 5073},
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 25,  -877, (short) 655, (short) 171, 5073},
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 33,  -877, (short) 415, (short) 200, 5073},
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 41,  -877, (short) 340, (short) 176, 5073},
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 49,  -877, (short) 355, (short) 174, 5073},
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 69,  -877, (short) 360, (short) 160, 5073},
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 83,  -877, (short) 375, (short) 170, 5073},
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 93,  -877, (short) 390, (short) 152, 5073},
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 113, -877, (short) 340, (short) 168, 5073},
        // [NPC_MIGRATED]             {"Chị Hằng", "Chúc các bạn có một mùa sự kiện với vẻ và may mắn", 191, -877, (short) 340, (short) 147, 5073}
        // [NPC_MIGRATED]         };
        initShop();
    }

    public static SuKienTrungThu gI() {
        if (instance == null) {
            instance = new SuKienTrungThu();
        }
        return instance;
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            107, 108, 109, 110, 111, 199, 200, 201, 202, 203, 204, 205, 206,
            207, 208, 209, 210, 211, 328, 329, 330, 331, 332, 407, 408, 409,
            410, 411, 412, 471, 472, 473, 474, 576, 819, 891, 892, 893
        };
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienTrungThu(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();

        // [NPC_MIGRATED] // [NPC_MIGRATED] initNpc(); // NPC đã chuyển sang class iNpc riêng // NPC đã chuyển sang class iNpc riêng trong package zinterfaces.menus.npcs.event
        System.out.println("[SuKienTrungThu] Init completed.");
    }

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -877 || npcId == -1018 || npcId == -100 || npcId == ID) {
            sendMenu(p);
            return true;
        }
        return false;
    }

    public void sendMenu(Player p) {
        if (!event.EventManager.isActive(ID)) return;
        p.menus.clear();
        p.menus.add(new model.Menu("Làm Lồng Đèn Thường", (short) 165, () -> {
            try {
                new itemz.rebuilds.GhepLongDenThuong().show_table(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new model.Menu("Làm Lồng Đèn VIP", (short) 165, () -> {
            try {
                new itemz.rebuilds.GhepDenKeoQuan().show_table(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new model.Menu("BXH Lồng Đèn", (short) 141, () -> {
            p.typeBXH = 801;
            showEventRank(p);
        }));
        p.menus.add(new model.Menu("BXH Đèn Kéo Quân", (short) 141, () -> {
            p.typeBXH = 802;
            showEventRank(p);
        }));
        p.menus.add(new model.Menu("Cửa Hàng Trung Thu", (short) 104, () -> {
            openShop(p);
        }));
        p.menus.add(new model.Menu("Hướng dẫn", (short) 123, () -> {
            try {
                String txt = "SỰ KIỆN TẾT TRUNG THU\b"
                        + "1. THU THẬP NGUYÊN LIỆU:\n"
                        + "- Đánh quái thường để nhặt: Nang Tre, Dây Kẽm.\n"
                        + "- Đánh quái biển để nhặt: Giấy Màu.\n"
                        + "2. LÀM LỒNG ĐÈN:\n"
                        + "- Lồng Đèn Thường: 5 Nang Tre + 5 Dây Kẽm + 5 Giấy Màu + 10.000 Beri.\n"
                        + "- Lồng Đèn VIP (Đèn Kéo Quân): 5 Nang Tre + 5 Dây Kẽm + 5 Giấy Màu + 5 Ruby.\n"
                        + "3. PHẦN THƯỞNG:\n"
                        + "- Sử dụng Lồng Đèn từ hành trang nhận EXP, Beri, Bột Cường Hóa, Bánh Trung Thu, Hộp Bánh...\n"
                        + "- Sử dụng Đèn Kéo Quân nhận Ruby, Rương Hệ, Thẻ TT Trung Thu...\n"
                        + "- Tích nạp và Tích tiêu sự kiện nhận thêm nhiều phần quà giá trị!";
                p.getService().Help_From_Server(-877, txt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new model.Menu("Tích nạp sự kiện", (short) 110, () -> {
            try {
                showTichNap(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new model.Menu("Tích tiêu sự kiện", (short) 110, () -> {
            try {
                showTichTieu(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.getService().openDynamicMenu(-877, "Chị Hằng", p.menus);
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == ID || npcId == -877 || npcId == -1018) {
            if (p.menus != null && index >= 0 && index < p.menus.size()) {
                p.menus.get(index).execute(p, index);
                return true;
            }
            switch (index) {
                case 0: // Làm Lồng Đèn Thường
                    new itemz.rebuilds.GhepLongDenThuong().show_table(p);
                    break;
                case 1: // Làm Lồng Đèn VIP
                    new itemz.rebuilds.GhepDenKeoQuan().show_table(p);
                    break;
                case 2: // BXH Lồng Đèn
                    p.typeBXH = 1;
                    showEventRank(p);
                    break;
                case 3: // BXH Đèn Kéo Quân
                    p.typeBXH = 2;
                    showEventRank(p);
                    break;
                case 4: // Shop Trung Thu
                    openShop(p);
                    break;
                case 5: { // Hướng dẫn
                    String txt = "SỰ KIỆN TẾT TRUNG THU\b"
                            + "1. THU THẬP NGUYÊN LIỆU:\n"
                            + "- Đánh quái thường để nhặt: Nang Tre, Dây Kẽm.\n"
                            + "- Đánh quái biển để nhặt: Giấy Màu.\n"
                            + "2. LÀM LỒNG ĐÈN:\n"
                            + "- Lồng Đèn Thường: 5 Nang Tre + 5 Dây Kẽm + 5 Giấy Màu + 10.000 Beri.\n"
                            + "- Lồng Đèn VIP (Đèn Kéo Quân): 5 Nang Tre + 5 Dây Kẽm + 5 Giấy Màu + 5 Ruby.\n"
                            + "3. PHẦN THƯỞNG:\n"
                            + "- Sử dụng Lồng Đèn từ hành trang nhận EXP, Beri, Bột Cường Hóa, Bánh Trung Thu, Hộp Bánh...\n"
                            + "- Sử dụng Đèn Kéo Quân nhận Ruby, Rương Hệ, Thẻ TT Trung Thu...\n"
                            + "- Tích nạp và Tích tiêu sự kiện nhận thêm nhiều phần quà giá trị!";
                    p.getService().Help_From_Server(npcId, txt);
                    return true;
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

    @Override
    public boolean onUseItem(Player p, int id) {
        if (!isEventActive()) {
            if (id == ITEM_LONG_DEN || id == ITEM_DEN_KEO_QUAN || id == ITEM_BANH_TRUNG_THU || id == ITEM_HOP_BANH || id == 891 || id == 892 || id == 893) {
                p.getService().send_box_ThongBao_OK("Sự kiện Tết Trung Thu đã kết thúc!");
                return false;
            }
            return false;
        }
        try {
            if (id == ITEM_LONG_DEN) {
                if (p.item.total_item_bag_by_id(4, ITEM_LONG_DEN) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Lồng Đèn!");
                    return false;
                }
                
                p.item.remove_item47(4, ITEM_LONG_DEN, 1);
                p.item.updateInventory(false);
                p.update_pointEvent1(1);

                // Rewards
                int expEarn = p.level * 2000;
                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 333, 50)); // 50 Exp Skill

                int rand = ZUtil.random(100);
                if (rand < 55) {
                    int gold = ZUtil.random(200000, 500000);
                    listGift.add(new template.GiftBox(4, 0, gold));
                } else if (rand < 75) {
                    int ruby = ZUtil.random(5, 10);
                    listGift.add(new template.GiftBox(4, 1, ruby));
                } else if (rand < 90) {
                    listGift.add(new template.GiftBox(7, 1, 1)); // Bột cường hóa
                } else if (rand < 95) {
                    listGift.add(new template.GiftBox(4, ITEM_BANH_TRUNG_THU, 1));
                } else {
                    listGift.add(new template.GiftBox(4, ITEM_HOP_BANH, 1));
                }

                core.RewardService.sendGiftOrMail(p, 1, "Thả Lồng Đèn", "Phần thưởng thả Lồng Đèn", listGift, true);
                return true;
            } else if (id == ITEM_DEN_KEO_QUAN) {
                if (p.item.total_item_bag_by_id(4, ITEM_DEN_KEO_QUAN) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Đèn Kéo Quân!");
                    return false;
                }
                
                p.item.remove_item47(4, ITEM_DEN_KEO_QUAN, 1);
                p.item.updateInventory(false);
                p.update_pointEvent2(1);

                // Premium Rewards
                int expEarn = p.level * 5000;
                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 333, 200)); // 200 Exp Skill

                int rand = ZUtil.random(100);
                if (rand < 40) {
                    int ruby = ZUtil.random(20, 50);
                    listGift.add(new template.GiftBox(4, 1, ruby));
                } else if (rand < 65) {
                    int gold = ZUtil.random(1000000, 2000000);
                    listGift.add(new template.GiftBox(4, 0, gold));
                } else if (rand < 80) {
                    listGift.add(new template.GiftBox(4, 122, 1)); // Rương Cam Cùng Hệ Lv10
                } else if (rand < 90) {
                    listGift.add(new template.GiftBox(4, ITEM_HOP_BANH, 1));
                } else {
                    listGift.add(new template.GiftBox(4, 475, 1)); // Thẻ TT Trung Thu
                }

                core.RewardService.sendGiftOrMail(p, 1, "Thả Đèn Kéo Quân (VIP)", "Phần thưởng thả Đèn Kéo Quân VIP", listGift, true);
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

        int rand = ZUtil.random(100);
        if (Zone.is_map_sea(mob.map.template.id)) {
            // Sea Map: drop Giấy màu (ID 819)
            if (rand < 12) {
                p.item.add_item_bag47(4, ITEM_GIAY_MAU, 1);
                p.item.updateInventory(false);
            }
        } else {
            // Normal Map: drop Nang Tre (ID 109) & Dây Kẽm (ID 409)
            if (rand < 6) {
                p.item.add_item_bag47(4, ITEM_DAY_KEM, 1);
                p.item.updateInventory(false);
            } else if (rand < 12) {
                p.item.add_item_bag47(4, ITEM_NANG_TRE, 1);
                p.item.updateInventory(false);
            }
        }
    }
}
