package event;

import model.Player;
import core.ZUtil;
import itemz.MainItem;
import mob.Mob;
import map.Zone;
import template.GiftBox;
import template.ItemTemplate4;
import network.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuKienValentine extends Event {

    public static final int ID = Event.ID_SUKIEN_VALENTINE;

    // Materials
    public static final int ITEM_SOCOLA_DO = 434;
    public static final int ITEM_SOCOLA_TIM = 435;
    public static final int ITEM_SOCOLA_SUA = 436;
    public static final int ITEM_SOCOLA_TRANG = 437;

    // Crafted items
    public static final int ITEM_BANH_KEM_SOCOLA = 438;
    public static final int ITEM_HOP_SOCOLA = 439;
    public static final int ITEM_RUONG_TINH_YEU = 440;
    public static final int ITEM_BO_HOA_TINH_YEU = 441;

    private static SuKienValentine instance;

    public SuKienValentine() {
        super(ID, "Sự Kiện Valentine");
        shopName = "Shop Tình Yêu";
        costItemId = ITEM_HOP_SOCOLA;
        costItemType = 4;

        this.bxhSubTypes = new int[]{1501, 1502};
        this.bxhNames = new String[]{"Top Gói Socola", "Top Ăn Socola"};

        initShop();
    }

    public static SuKienValentine gI() {
        if (instance == null) {
            instance = new SuKienValentine();
        }
        return instance;
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            434, 435, 436, 437, 438, 439, 440, 806, 807, 808, 809
        };
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienValentine(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();

        // Activity rewards
        lienTangReward = new ActivityReward(2, 5); // 5 lần/ngày
        lienTangReward.addItemGift(ITEM_BANH_KEM_SOCOLA, (byte) 4, 1);

        wantedReward = new ActivityReward(3, 10); // 10 lần/ngày
        wantedReward.addItemGift(ITEM_SOCOLA_TRANG, (byte) 4, 2);

        trainReward = new ActivityReward(4, 5); // 5 lần/ngày
        trainReward.addItemGift(ITEM_SOCOLA_SUA, (byte) 4, 3);
    }

    // ======================== MENU NPC SANJI TÌNH YÊU (LAMBDA MENU) ========================

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -1008 || npcId == -100 || npcId == ID) {
            List<model.Menu> menus = new ArrayList<>();
            menus.add(new model.Menu("Shop Tình Yêu", () -> {
                try { openShop(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Gói Hộp Socola Tình Yêu", () -> {
                try { new itemz.rebuilds.GhepSocola().show_table(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Làm Bánh Kem Socola", () -> {
                try { new itemz.rebuilds.GhepBanhKemSocola().show_table(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Chế Tạo Rương Tình Yêu", () -> {
                try { new itemz.rebuilds.GhepRuongTinhYeu().show_table(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Gói Bó Hoa Hồng Tình Yêu", () -> {
                try { new itemz.rebuilds.GhepBoHoaTinhYeu().show_table(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Vòng Quay Tình Yêu Lãng Mạn", () -> {
                try { openLuckyWheel(p, 10, -1); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("BXH Gói Socola", () -> {
                try {
                    p.typeBXH = 1501;
                    showEventRank(p);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("BXH Ăn Socola", () -> {
                try {
                    p.typeBXH = 1502;
                    showEventRank(p);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Hướng dẫn sự kiện", () -> {
                try { sendHelp(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Tích nạp sự kiện", () -> {
                try { showTichNap(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Tích tiêu sự kiện", () -> {
                try { showTichTieu(p); } catch (Exception e) { e.printStackTrace(); }
            }));

            p.getService().openMenu(-1008, "Sanji Tình Yêu", menus);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        return false;
    }

    @Override
    public boolean onUseItem(Player p, int id) {
        if (!isEventActive()) {
            if (id == ITEM_HOP_SOCOLA || id == ITEM_BANH_KEM_SOCOLA || id == ITEM_RUONG_TINH_YEU || id == ITEM_BO_HOA_TINH_YEU) {
                p.getService().send_box_ThongBao_OK("Sự kiện Valentine đã kết thúc!");
                return false;
            }
            return false;
        }
        try {
            if (id == ITEM_HOP_SOCOLA) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Hộp Socola!");
                    return false;
                }
                
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 5000;

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));

                int rand = ZUtil.random(100);
                if (rand < 40) {
                    listGift.add(new template.GiftBox(4, 0, 200_000));
                } else if (rand < 70) {
                    listGift.add(new template.GiftBox(7, 1, 2)); // Bột cường hóa x2
                } else if (rand < 90) {
                    listGift.add(new template.GiftBox(4, 135, 1)); // Tinh thể đá
                } else {
                    listGift.add(new template.GiftBox(4, 122, 1)); // Rương Cam Cùng Hệ Lv10
                }
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Hộp Socola", "Phần thưởng", listGift, true);
                return true;
            } else if (id == ITEM_BANH_KEM_SOCOLA) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Bánh Kem Socola!");
                    return false;
                }
                
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 10000;
                int rubyEarn = 5 + ZUtil.random(15);

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 1, rubyEarn));

                int rand = ZUtil.random(100);
                if (rand < 50) {
                    listGift.add(new template.GiftBox(7, 2, 1)); // Bột vàng
                } else if (rand < 80) {
                    listGift.add(new template.GiftBox(4, 29, 1)); // Rương ác quỷ
                } else {
                    listGift.add(new template.GiftBox(4, 122, 1)); // Rương Cam Cùng Hệ Lv10
                }
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Bánh Kem Socola", "Phần thưởng", listGift, true);
                return true;
            } else if (id == ITEM_RUONG_TINH_YEU) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Rương Tình Yêu!");
                    return false;
                }
                
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 20000;
                int rubyEarn = 20 + ZUtil.random(50);

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 1, rubyEarn));

                int rand = ZUtil.random(100);
                if (rand < 40) {
                    listGift.add(new template.GiftBox(4, 158, 1)); // Rương đại ác quỷ
                } else if (rand < 75) {
                    listGift.add(new template.GiftBox(4, 122, 2)); // Rương Cam Cùng Hệ Lv10 x2
                } else {
                    listGift.add(new template.GiftBox(7, 2, 3)); // Bột vàng x3
                }
                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Rương Tình Yêu", "Phần thưởng", listGift, true);
                return true;
            } else if (id == ITEM_BO_HOA_TINH_YEU) {
                if (p.item.total_item_bag_by_id(4, id) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Bó Hoa Tình Yêu!");
                    return false;
                }
                
                p.item.remove_item47(4, id, 1);
                int expEarn = p.level * 8000;

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, expEarn));
                listGift.add(new template.GiftBox(4, 0, 500_000));

                p.item.updateInventory(false);
                core.RewardService.sendGiftOrMail(p, 1, "Bó Hoa Tình Yêu", "Phần thưởng", listGift, true);
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

        if (!Zone.is_map_sea(mob.map.template.id)) {
            int[] items = new int[]{ITEM_SOCOLA_DO, ITEM_SOCOLA_TIM, ITEM_SOCOLA_SUA, ITEM_SOCOLA_TRANG};
            dropEventItemRandom(p, mob, items, 20, 150, 0); // 20% rơi, tối đa 150/ngày
        }
    }

    @Override
    public void onVanChuyen(Player p) {
        if (!event.EventManager.isActive(id)) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[5] < 5) {
            evData.data[5]++;
            p.item.add_item_bag47(4, ITEM_SOCOLA_TRANG, 2);
            p.item.updateInventory(false);
        }
    }

    @Override
    public void onVuonCam(Player p, int round) {
        if (!event.EventManager.isActive(id)) return;
        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[6] < 3) {
            evData.data[6]++;
            p.item.add_item_bag47(4, ITEM_HOP_SOCOLA, 1);
            p.item.updateInventory(false);
        }
    }
}
