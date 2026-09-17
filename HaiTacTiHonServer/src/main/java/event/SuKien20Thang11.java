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

public class SuKien20Thang11 extends Event {

    public static final int ID = Event.ID_SUKIEN_20THANG11_REAL;

    // Items Sự Kiện 20/11
    public static final int ITEM_HOA_HONG_TRANG = 888;
    public static final int ITEM_HOA_HONG_DO = 889;
    public static final int ITEM_BO_HOA_RUC_RO = 890;
    public static final int ITEM_PHAO_HOA = 359;

    private static SuKien20Thang11 instance;

    public SuKien20Thang11() {
        super(ID, "Sự Kiện 20.11");
        shopName = "Cửa Hàng Nhà Giáo";
        costItemId = ITEM_BO_HOA_RUC_RO;
        costItemType = 4;
        time = "0:00:00 15/6/2026 > 23:59:59 15/12/2026";
        timex2pay = "0:00:00 15/6/2026 > 23:59:59 15/12/2026";
        timechangeitem = "0:00:00 15/6/2026 > 23:59:59 15/12/2026";
        timedropitem = "0:00:00 15/6/2026 > 23:59:59 15/12/2026";
        timeremoveitem = "0:00:00 15/6/2026 > 23:59:59 15/12/2026";

        this.bxhSubTypes = new int[]{1401};
        this.bxhNames = new String[]{"Top Dâng Hoa Tri Ân"};

        initShop();
    }

    public static SuKien20Thang11 gI() {
        if (instance == null) {
            instance = new SuKien20Thang11();
        }
        return instance;
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            ITEM_HOA_HONG_TRANG, ITEM_HOA_HONG_DO, ITEM_BO_HOA_RUC_RO
        };
    }

    @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKien20Thang11(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();
        System.out.println("[SuKien20Thang11] Init completed.");
    }

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -1051 || npcId == -100 || npcId == ID) { // Boa Hancock / Menu Sự Kiện
            p.getService().openDynamicMenu(npcId, "Boa Hancock", new String[]{
                "Tạo Bó Hoa Rực Rỡ", "BXH Dâng Hoa Tri Ân", "Shop 20.11", "Hướng dẫn",
                "Tích nạp sự kiện", "Tích tiêu sự kiện"
            }, null);
            return true;
        } else if (npcId == -1052) { // Rayleigh
            p.getService().openDynamicMenu(npcId, "Thầy Rayleigh", new String[]{
                "Dâng Bó Hoa Rực Rỡ", "BXH Dâng Hoa Tri Ân", "Hướng dẫn"
            }, null);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!event.EventManager.isActive(ID)) return false;
        if (npcId == -1051 || npcId == -100 || npcId == ID) { // Boa Hancock / Menu Sự Kiện
            switch (index) {
                case 0:
                    new itemz.rebuilds.GhepBoHoaRucRo().show_table(p);
                    break;
                case 1:
                    p.typeBXH = 1401;
                    showEventRank(p);
                    break;
                case 2:
                    openShop(p);
                    break;
                case 3: {
                    String txt = "Sự Kiện Ngày Nhà Giáo 20.11\n"
                            + "- Luffy muốn dâng hoa tri ân thầy Rayleigh.\n"
                            + "- Đánh quái rơi: Hoa hồng trắng (Đất liền), Hoa hồng đỏ (Biển).\n"
                            + "- Ghép Bó Hoa Rực Rỡ: 2 Hoa hồng trắng + 1 Hoa hồng đỏ + 5 Ruby.\n"
                            + "- Mang Bó Hoa Rực Rỡ dâng lên thầy Rayleigh để nhận quà tri ân và đua TOP BXH!";
                    p.getService().Help_From_Server(npcId, txt);
                    break;
                }
                case 4:
                    showTichNap(p);
                    break;
                case 5:
                    showTichTieu(p);
                    break;
            }
            return true;
        } else if (npcId == -1052) { // Rayleigh
            switch (index) {
                case 0:
                    offerRayleigh(p);
                    break;
                case 1:
                    p.typeBXH = 1401;
                    showEventRank(p);
                    break;
                case 2: {
                    String txt = "Tri Ân Thầy Rayleigh 20.11\n"
                            + "- Dâng Bó Hoa Rực Rỡ: nhận Ruby, Exp, Exp Skill, Bột Vàng, Rương Ác Quỷ và Rương Đại Ác Quỷ.";
                    p.getService().Help_From_Server(npcId, txt);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public void offerRayleigh(Player p) throws IOException {
        if (p.item.total_item_bag_by_id(4, ITEM_BO_HOA_RUC_RO) < 1) {
            p.getService().send_box_ThongBao_OK("Bạn không có Bó hoa rực rỡ nào để dâng lên thầy!");
            return;
        }

        p.item.remove_item47(4, ITEM_BO_HOA_RUC_RO, 1);
        p.item.updateInventory(false);

        p.update_pointEvent1(1);
        int expEarn = p.level * 8000;
        List<template.GiftBox> listGift = new ArrayList<>();
        listGift.add(new template.GiftBox(99, 0, expEarn));
        listGift.add(new template.GiftBox(4, 333, 300)); // 300 Exp Skill

        int rand = ZUtil.random(100);
        if (rand < 40) {
            int ruby = ZUtil.random(10, 20);
            listGift.add(new template.GiftBox(4, 1, ruby));
        } else if (rand < 70) {
            int gold = ZUtil.random(500000, 1000000);
            listGift.add(new template.GiftBox(4, 0, gold));
        } else if (rand < 85) {
            listGift.add(new template.GiftBox(7, 4, 2)); // 2 Bột Vàng
        } else if (rand < 95) {
            listGift.add(new template.GiftBox(4, 106, 1)); // Rương Ác Quỷ
        } else {
            listGift.add(new template.GiftBox(4, 158, 1)); // Rương Đại Ác Quỷ
        }
        core.RewardService.sendGiftOrMail(p, 1, "Tri Ân Thầy Rayleigh", "Rayleigh mỉm cười và tặng bạn phần thưởng", listGift, true);
    }

    @Override
    public boolean onUseItem(Player p, int id) {
        if (!isEventActive()) {
            if (id == ITEM_PHAO_HOA || id == ITEM_BO_HOA_RUC_RO) {
                p.getService().send_box_ThongBao_OK("Sự kiện 20 Tháng 11 đã kết thúc!");
                return false;
            }
            return false;
        }
        try {
            if (id == ITEM_PHAO_HOA) {
                if (p.item.total_item_bag_by_id(4, id) < 1) return false;
                
                p.item.remove_item47(4, id, 1);
                p.update_pointEvent1(1); // Tăng điểm pháo hoa

                // Reward all players in the zone
                for (Player pl : p.map.players) {
                    if (pl != null) {
                        List<template.GiftBox> listGift = new ArrayList<>();
                        listGift.add(new template.GiftBox(99, 0, 50000));
                        core.RewardService.sendGiftOrMail(pl, 1, "Pháo Hoa 20.11", p.name + " đã đốt Pháo hoa!", listGift, true);
                    }
                }
                p.item.updateInventory(false);
                return true;
            } else if (id == ITEM_BO_HOA_RUC_RO) {
                offerRayleigh(p);
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
            dropEventItem(p, mob, ITEM_HOA_HONG_DO, 12, 150, 0); // 12% cơ bản, giới hạn 150/ngày tại index 0
        } else {
            dropEventItem(p, mob, ITEM_HOA_HONG_TRANG, 12, 150, 0); // 12% cơ bản, giới hạn 150/ngày tại index 0
        }
    }
}
