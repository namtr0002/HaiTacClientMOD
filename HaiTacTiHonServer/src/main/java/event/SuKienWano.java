package event;

import model.Player;
import core.Manager;
import core.ZUtil;
import itemz.MainItem;
import mob.Mob;
import map.Zone;
import network.Message;
import template.GiftBox;
import template.ItemBag47;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.Item_wear;
import event.eboss.KingHoaTai;
import event.eboss.KaidoRong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SuKienWano — Sự Kiện "ĐẠI CHIẾN TỨ HOÀNG WANO & RỒNG SHINRYU" (Event ID 17)
 */
public class SuKienWano extends Event {

    public static final int ID = 17;
    private static SuKienWano instance;

    public static SuKienWano gI() {
        if (instance == null) {
            instance = new SuKienWano();
        }
        return instance;
    }

    public static final int EVENT_ITEM_HOADANG = 797; // Hoa Đăng Wano
    public static final int EVENT_ITEM_THAODUOC = 10; // Thảo Dược Wano (item7)
    public static final int EVENT_ITEM_QUANGTHIET = 11; // Quặng Thiết Tháp (item7)
    public static final int EVENT_ITEM_BIKIP = 610; // Cuộn Bí Kíp Samurai (item4)
    public static final int EVENT_ITEM_RUONGWANO = 609; // Rương Thần Thoại Wano (item4)

    public static long serverHoaDangCount = 0;
    public static long serverBuffTimeout = 0;

    private static final short[] SELL_ITEMS = {797, 609, 610, 600};

    public SuKienWano() {
        super(ID, "Đại Chiến Tứ Hoàng Wano 2026");
        shopName = "Cửa Hàng Wano Quốc";
        costItemId = EVENT_ITEM_HOADANG;
        costItemType = 4;
        sellableItems = new short[0];

        this.bxhSubTypes = new int[]{1701, 1702};
        this.bxhNames = new String[]{"Top Thả Hoa Đăng", "Top Tiêu Diệt Kaido"};

        time = "0:00:00 01/01/2026 > 23:59:59 31/12/2026";
        timex2pay = "0:00:00 01/01/2026 > 23:59:59 31/12/2026";
        timechangeitem = "0:00:00 01/01/2026 > 23:59:59 31/12/2026";
        timedropitem = "0:00:00 01/01/2026 > 23:59:59 31/12/2026";
        timeremoveitem = "0:00:00 01/01/2026 > 23:59:59 31/12/2026";

        npcConfigs = new Object[][] {
            {"O-Tsuru Wano", "Cửa hàng sự kiện Wano Quốc & Cầu nguyện Long Thần đây!", 0, -888, (short) 360, (short) 335, 5071},
            {"O-Tsuru Wano", "Cửa hàng sự kiện Wano Quốc & Cầu nguyện Long Thần đây!", 1, -889, (short) 560, (short) 168, 5071}
        };

        initShop();
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{EVENT_ITEM_HOADANG};
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienWano(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();
        System.out.println("[SuKienWano] Init completed. Shop items and NPCs registered.");
    }

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!EventManager.isActive(this.id) || !isDropItemActive()) return;
        if (p == null || mob == null || mob.mtemplate == null || mob.is_boss) return;
        p = p.getOwnerPlayer();
        if (p.isBot || p.item == null) return;

        int mobLv = mob.level & 0xFF;
        if (Math.abs(mobLv - p.level) <= 5) {
            boolean hasAdded = false;
            // Drop Hoa Đăng Wano (tỷ lệ 15%)
            if (ZUtil.random(100) < 15) {
                p.item.add_item_bag47(4, EVENT_ITEM_HOADANG, 1);
                hasAdded = true;
            }
            // Drop Thảo Dược Wano (tỷ lệ 10%)
            if (ZUtil.random(100) < 10) {
                p.item.add_item_bag47(7, EVENT_ITEM_THAODUOC, 1);
                hasAdded = true;
            }
            // Drop Quặng Thiết Tháp (tỷ lệ 8%)
            if (ZUtil.random(100) < 8) {
                p.item.add_item_bag47(7, EVENT_ITEM_QUANGTHIET, 1);
                hasAdded = true;
            }
            if (hasAdded) {
                p.item.updateInventory(false);
            }
        }
    }

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!EventManager.isActive(this.id)) return false;
        if (npcId == -887 || npcId == -888 || npcId == -889 || npcId == -100 || npcId == ID) {
            String statusBuff = System.currentTimeMillis() < serverBuffTimeout 
                ? " [ĐANG KÍCH HOẠT BUFF LONG THẦN X2 EXP]" 
                : "";
            String title = "Sự Kiện Đại Chiến Tứ Hoàng Wano\n"
                + "Tiến độ Cầu Nguyện Server: " + serverHoaDangCount + "/10.000 Hoa Đăng" + statusBuff;
            
            p.getService().openDynamicMenu(npcId, "O-Tsuru Wano", new String[]{
                "Cửa Hàng Wano",
                "Chế Rượu Sake Bách Thú",
                "Đúc Ấn Tứ Hoàng Kaido",
                "Ghép Rương Cải Trang Wano",
                "Cầu Nguyện Long Thần (Góp Hoa Đăng)",
                "Triệu Hồi Boss Kaido Rồng",
                "BXH Thả Hoa Đăng",
                "BXH Tiêu Diệt Kaido",
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
        if (!EventManager.isActive(this.id)) return false;
        if (menuId == -887 || menuId == -888 || menuId == -889 || menuId == -100 || menuId == ID) {
            switch (index) {
                case 0:
                    openShop(p);
                    break;
                case 1:
                    // Chế Rượu Sake Bách Thú: 100 Hoa Đăng + 50 Thảo Dược
                    if (p.item.total_item_bag_by_id(4, EVENT_ITEM_HOADANG) >= 100 
                             && p.item.total_item_bag_by_id(7, EVENT_ITEM_THAODUOC) >= 50) {
                        p.item.remove_item47(4, EVENT_ITEM_HOADANG, 100);
                        p.item.remove_item47(7, EVENT_ITEM_THAODUOC, 50);
                        List<template.GiftBox> listGift = new ArrayList<>();
                        listGift.add(new template.GiftBox(4, 600, 1));
                        core.RewardService.sendGiftOrMail(p, 1, "Chế Tạo Rượu Sake", "Chế tạo thành công 1 Rượu Sake Bách Thú!", listGift, true);
                    } else {
                        p.getService().send_box_ThongBao_OK("Cần 100 Hoa Đăng Wano và 50 Thảo Dược Wano để chế Rượu Sake Bách Thú!");
                    }
                    break;
                case 2:
                    // Đúc Ấn Tứ Hoàng Kaido: 200 Quặng Thiết Tháp + 10 Cuộn Bí Kíp
                    if (p.item.total_item_bag_by_id(7, EVENT_ITEM_QUANGTHIET) >= 200 
                             && p.item.total_item_bag_by_id(4, EVENT_ITEM_BIKIP) >= 10) {
                        p.item.remove_item47(7, EVENT_ITEM_QUANGTHIET, 200);
                        p.item.remove_item47(4, EVENT_ITEM_BIKIP, 10);
                        List<template.GiftBox> listGift = new ArrayList<>();
                        listGift.add(new template.GiftBox(4, 610, 1));
                        core.RewardService.sendGiftOrMail(p, 1, "Đúc Ấn Tứ Hoàng", "Đúc thành công Ấn Tứ Hoàng Kaido!", listGift, true);
                    } else {
                        p.getService().send_box_ThongBao_OK("Cần 200 Quặng Thiết Tháp và 10 Cuộn Bí Kíp Samurai!");
                    }
                    break;
                case 3:
                    // Ghép Rương Cải Trang Wano: 500 Hoa Đăng + 50 Cuộn Bí Kíp + 10,000 Ruby
                    if (p.item.total_item_bag_by_id(4, EVENT_ITEM_HOADANG) >= 500 
                             && p.item.total_item_bag_by_id(4, EVENT_ITEM_BIKIP) >= 50
                             && p.get_ngoc() >= 10000) {
                        p.item.remove_item47(4, EVENT_ITEM_HOADANG, 500);
                        p.item.remove_item47(4, EVENT_ITEM_BIKIP, 50);
                        p.update_ngoc(-10000);
                        p.updateMoney();
                        
                        List<template.GiftBox> listGift = new ArrayList<>();
                        listGift.add(new template.GiftBox(4, 609, 1));
                        core.RewardService.sendGiftOrMail(p, 1, "Ghép Rương Cải Trang", "Ghép thành công Rương Cải Trang Wano!", listGift, true);
                    } else {
                        p.getService().send_box_ThongBao_OK("Cần 500 Hoa Đăng Wano, 50 Cuộn Bí Kíp và 10.000 Ruby!");
                    }
                    break;
                case 4:
                    // Cầu Nguyện Long Thần (Đóng góp 100 Hoa Đăng)
                    if (p.item.total_item_bag_by_id(4, EVENT_ITEM_HOADANG) >= 100) {
                        p.item.remove_item47(4, EVENT_ITEM_HOADANG, 100);
                        p.update_pointEvent1(100);
                        serverHoaDangCount += 100;
                        if (serverHoaDangCount >= 10000) {
                            serverHoaDangCount = 0;
                            serverBuffTimeout = System.currentTimeMillis() + (4 * 3600 * 1000L); // 4 tiếng
                            Manager.gI().chatKTG(0, "[LONG THẦN GIÁO LÂM] Toàn Server vừa đạt mốc 10.000 Hoa Đăng Wano! Kích hoạt BUFF X2 EXP & +15% Tỷ lệ Rớt Đồ trong 4 Giờ!", 5);
                        } else {
                            p.getService().send_box_ThongBao_OK("Bạn vừa đóng góp 100 Hoa Đăng Wano! (+100 Điểm BXH)\nTiến độ Server: " + serverHoaDangCount + "/10.000.");
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Bạn cần ít nhất 100 Hoa Đăng Wano để đóng góp Cầu Nguyện!");
                    }
                    break;
                case 5:
                    // Triệu Hồi Boss Kaido Rồng bằng Ấn Tứ Hoàng (Item 610)
                    if (p.item.total_item_bag_by_id(4, EVENT_ITEM_BIKIP) >= 1) {
                        p.item.remove_item47(4, EVENT_ITEM_BIKIP, 1);
                        KaidoRong.init();
                        p.getService().send_box_ThongBao_OK("Triệu hồi thành công Boss Thần Thoại Kaido Rồng!");
                    } else {
                        p.getService().send_box_ThongBao_OK("Bạn cần 1 Ấn Tứ Hoàng Kaido để triệu hồi!");
                    }
                    break;
                case 6:
                    p.typeBXH = 1701;
                    showEventRank(p);
                    break;
                case 7:
                    p.typeBXH = 1702;
                    showEventRank(p);
                    break;
                case 8: {
                    String txt = "Sự Kiện Đại Chiến Tứ Hoàng Wano\n"
                            + "- Đánh quái rơi Hoa Đăng Wano, Thảo Dược Wano và Quặng Thiết Tháp.\n"
                            + "- Chế Rượu Sake Bách Thú và Đúc Ấn Tứ Hoàng Kaido để triệu hồi và khiêu chiến Boss Kaido Rồng.\n"
                            + "- Đóng góp Hoa Đăng Cầu Nguyện Long Thần để kích hoạt Buff X2 Exp toàn Server.\n"
                            + "- Dùng Hoa Đăng Wano đổi vật phẩm tại Cửa Hàng Wano.";
                    p.getService().Help_From_Server(menuId, txt);
                    break;
                }
                case 9:
                    showTichNap(p);
                    break;
                case 10:
                    showTichTieu(p);
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void update(int hour, int min, int sec) throws Exception {
        KingHoaTai.update();
        KaidoRong.update();

        // Tự động xuất hiện Boss King Hỏa Tai lúc 12h30 và 19h30
        if ((hour == 12 || hour == 19) && min == 30 && sec == 0) {
            KingHoaTai.init();
        }
        // Tự động xuất hiện Boss Kaido Rồng lúc 21h00
        if (hour == 21 && min == 0 && sec == 0) {
            KaidoRong.init();
        }
    }

    @Override
    public boolean onUseItem(Player p, int id) throws IOException {
        if (!isEventActive()) {
            if (id == EVENT_ITEM_RUONGWANO || id == 600 || id == EVENT_ITEM_HOADANG) {
                p.getService().send_box_ThongBao_OK("Sự kiện Wano đã kết thúc!");
                return false;
            }
            return false;
        }

        // 1. Rương Thần Thoại Wano (#609)
        if (id == EVENT_ITEM_RUONGWANO) {
            if (p.item.total_item_bag_by_id(4, id) < 1) return false;
            
            p.item.updateInventory(false);

            long exp = (long) p.level * 8000L;
            int beri = ZUtil.random(100000, 500000);

            List<template.GiftBox> giftList = new ArrayList<>();
            giftList.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, exp)));
            giftList.add(new template.GiftBox(4, 0, beri));

            int roll = ZUtil.random(100);
            if (roll < 5) { // 5% Rương Đại Ác Quỷ
                giftList.add(new template.GiftBox(4, 158, 1));
                Manager.gI().chatKTG(0, "Chúc mừng [" + p.name + "] mở Rương Thần Thoại Wano nhận được [Rương Đại Ác Quỷ]!", 0);
            } else if (roll < 25) { // 20% Rương Cam
                int chestId = (p.level < 20) ? 122 : Math.min(131, 122 + (p.level / 10 - 1));
                giftList.add(new template.GiftBox(4, chestId, 1));
            } else if (roll < 55) { // 30% Bột Cường Hóa (2-6)
                int qty = ZUtil.random(2, 6);
                giftList.add(new template.GiftBox(7, 1, qty));
            } else { // 45% Đá khảm C2-C4
                int[] daIds = {45, 46, 47, 51, 52, 53, 57, 58, 59, 63, 64, 65, 69, 70, 71};
                int daId = daIds[ZUtil.random(daIds.length)];
                giftList.add(new template.GiftBox(4, daId, 1));
            }

            core.RewardService.sendGiftOrMail(p, 1, "Rương Thần Thoại Wano", "Phần thưởng mở Rương Thần Thoại Wano", giftList, true);
            return true;
        }

        // 2. Rượu Sake Bách Thú (#600)
        if (id == 600) {
            sendMenu(p, -888);
            return false;
        }

        // 3. Hoa Đăng Wano (#797)
        if (id == EVENT_ITEM_HOADANG) {
            sendMenu(p, -888);
            return false;
        }

        return false;
    }

}
