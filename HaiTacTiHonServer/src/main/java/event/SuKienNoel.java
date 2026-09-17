package event;

import event.eboss.QuaiVatTuyetNoel;
import event.eboss.SantaNoel;
import itemz.MainItem;
import model.Player;
import zabstracts.AbsRanked;
import core.Manager;
import core.MenuController;
import network.Service;
import core.ZUtil;
import core.ZCollection;
import database.RandomCollection;
import network.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import map.Zone;
import mob.Mob;
import template.MobTemplate;
import template.ItemTemplate4;
import database.IDManager;
import java.util.HashSet;
import java.util.Set;

public class SuKienNoel extends Event {
    /**
     * ID sự kiện Noel. Được dùng làm event id (super) và sub-menu id (sendDymanicMenu).
     * Manager.ZEVENT_ID == ID_EVENT khi Noel active.
     */
    public static final short ID_EVENT = 12;
    /** Alias của ID_EVENT — dùng khi cần phân biệt ngữ nghĩa sub-menu. */
    public static final short MENU_ID  = ID_EVENT;
    public static final byte INDEX_NVL = 0;
    public static final byte INDEX_LT = 1;
    public static final byte INDEX_SLT = 2;
    public static final byte INDEX_PVP = 3;
    public static final byte INDEX_WANTED = 4;
    public static final byte INDEX_DECOR = 5;
    public static final byte INDEX_GIFT_SANTA = 6;
    public static final byte INDEX_MERRY = 7;
    public static final byte INDEX_TRAIN = 8;
    
    public static final short ITEM_VE_TRIEU_HOI = 168;
    public static final short ITEM_VE_NOEL = 230;
    public static final short ITEM_KEO_GIANG_SINH = 489;
    
    public static final String[] mChatSanta = new String[] {
        "Chúc Mừng Giáng Sinh An lành!",
        "Các cháu! Hãy ngoan ngoãn, bạn sẽ nhận được những gì mình mong muốn!",
        "Phép màu của Giáng Sinh không bao giờ kết thúc.",
        "Chúc các cháu một Giáng Sinh vui vẻ!",
        "Hãy tin vào phép màu của Giáng Sinh!",
        "Chúc bạn một mùa lễ hội đầy niềm vui và tiếng cười!",
        "Giữ gìn tinh thần Giáng Sinh!",
        "Ông già Noel đang đến thị trấn!",
        "Chúc bạn yêu thương, bình an và hạnh phúc trong mùa Giáng Sinh này!",
        "Hô hô hô, Chúc Mừng Giáng Sinh đến tất cả mọi người, và chúc mọi người một đêm tốt lành!"
    };
        
    public static short[] idMapSanta = new short[] {1, 9, 17, 25, 33};
    public short[] mMapSanta = idMapSanta;
    
    public static SuKienNoel instance;
    
    public List<QuaiVatTuyetNoel> listBoss = new java.util.concurrent.CopyOnWriteArrayList<>();
    public Map<Short, SantaNoel> listSanta = new java.util.concurrent.ConcurrentHashMap<>();
    
    public RandomCollection<MainItem> giftNoelBox = new RandomCollection<>();
    public RandomCollection<MainItem> giftKillBossALL = new RandomCollection<>();
    public RandomCollection<MainItem> giftKillBoss = new RandomCollection<>();
    public RandomCollection<MainItem> giftSanta = new RandomCollection<>();
    
    public SuKienNoel() {
        super(ID_EVENT, "Sự Kiện Noel");
        this.bxhSubType = 10;
        this.costItemId = ITEM_VE_NOEL;
        this.costItemType = 4;
        initShop();
        instance = this;
    }
    
    public static SuKienNoel gI() {
        if (instance == null) {
            instance = new SuKienNoel();
        }
        return instance;
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            168, 227, 229, 230, 340, 341, 342, 343, 344, 345, 346, 347, 348,
            485, 486, 487, 488, 489, 490, 491, 492, 611, 612, 613, 614, 615,
            616, 617, 618, 619, 620, 895, 896, 897
        };
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienNoel(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }
    
    @Override
    public void init() {
        initShop();
        listBoss = new java.util.concurrent.CopyOnWriteArrayList<>();
        for(int i = 0; i < 100; i++) {
            QuaiVatTuyetNoel boss = new QuaiVatTuyetNoel(i);
            listBoss.add(boss);
        }
        
        listSanta = new java.util.concurrent.ConcurrentHashMap<>();
        for(int i = 0; i < 3; i++) {
            try {
                SantaNoel boss = new SantaNoel("Santa #" + (i + 1), mMapSanta[i]);
                listSanta.put(boss.index_map, boss);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        lienTangReward = new ActivityReward(INDEX_LT, 5);
        lienTangReward.addGiftItem(4, ITEM_VE_NOEL, 5);

        sieuLienTangReward = new ActivityReward(INDEX_SLT, 5);
        sieuLienTangReward.addGiftItem(4, ITEM_VE_NOEL, 5);

        pvpReward = new ActivityReward(INDEX_PVP, 5);
        pvpReward.addGiftItem(4, ITEM_VE_NOEL, 2);

        wantedReward = new ActivityReward(INDEX_WANTED, 5);
        wantedReward.addGiftItem(4, ITEM_VE_NOEL, 2);

        trainReward = new ActivityReward(INDEX_TRAIN, 200);
        trainReward.addGiftItem(4, ITEM_KEO_GIANG_SINH, 1);

        this.bxhSubType = 10;
        this.shopName = "Cửa Hàng Noel";
        this.costItemId = ITEM_VE_NOEL;
        this.costItemType = 4;

        initGift();
    }
    
    public synchronized boolean callBoss(Player p) {
        QuaiVatTuyetNoel boss = null;
        boolean check = false;
        for(QuaiVatTuyetNoel quaiVatTuyet : listBoss) {
            if(boss == null && quaiVatTuyet.mob.hp <= 0 && quaiVatTuyet.mob.isdie) {
                boss = quaiVatTuyet;
            }
            if(quaiVatTuyet.mob.map != null && quaiVatTuyet.mob.map.equals(p.map)) {
                check = true;
                break;
            }
        }
        if(boss == null) {
            p.getService().send_box_ThongBao_OK("Quái vật tuyết đã xuất hiện quá nhiều, vui lòng chờ");
            return false;
        }
        if(check) {
            p.getService().send_box_ThongBao_OK("Tối đa 1 Quái Vật Tuyết trong khu vực");
            return false;
        }
        boss.timeLive = System.currentTimeMillis() + (1000 * 60 * 60);
        boss.mob.map = p.map;
        boss.mob.hp = boss.mob.hp_max;
        boss.mob.isdie = false;
        boss.mob.sendMove();
        Manager.gI().chatKTG(0, p.name + " đã triệu hồi Quái vật tuyết " + (boss.slotId + 1) + " tại " + p.map.template.name + ", khu vực " + (p.map.zone_id + 1), 0);
        
        return true;
    }
    
    public synchronized void callSanta() {
        for(SantaNoel santa : listSanta.values()) {
            santa.reset();
            santa.isOff = false;
            Manager.gI().chatKTG(0, "Ông già Noel đã xuất hiện tại " + santa.map.template.name + ", khu vực " + (santa.map.zone_id + 1), 0);
        }
    }
    
    public synchronized void endSanta() {
        for(SantaNoel santa : listSanta.values()) {
            santa.reset();
            santa.isOff = true;
        }
    }
    
    public boolean isNoel() {
        return event.EventManager.isActive(ID_EVENT);
    }
    
    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!isNoel()) return false;
        if (npcId == -882 || npcId == -1019 || npcId == -100 || npcId == ID_EVENT) {
            sendMenu(p);
            return true;
        }
        return false;
    }

    public void sendMenu(Player p) {
        // Sub-menu riêng của event — gửi với ID_EVENT/MENU_ID để client biết đây là menu Noel
        p.getService().openDynamicMenu(ID_EVENT, "Sự kiện Noel",
        new String[]{
            "Cửa Hàng Noel",
            "Đổi hộp quà Noel",
            "Trang trí cây thông",
            "Trang trí cây thông VIP",
            "Chúc mừng giáng sinh",
            "BXH Trang trí",
            "Hướng dẫn",
            "Tích nạp sự kiện",
            "Tích tiêu sự kiện"},
        null);
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!isNoel()) return false;
        if (npcId == -882 || npcId == -1019 || npcId == -100 || npcId == ID_EVENT) {
            processMenu(p, index);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleCmdEvent(Player p, byte action) throws IOException {
        if (!isNoel()) return false;
        if (action == 0) {
            new itemz.rebuilds.TrangTriCayThongThuong().show_table(p);
            return true;
        } else if (action == 1) {
            openShop(p);
            return true;
        }
        return false;
    }
    
    public void processMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0:
                openShop(p);
                break;
            case 1:
                new itemz.rebuilds.GhepHopQuaNoel().show_table(p);
                break;
            case 2:
                new itemz.rebuilds.TrangTriCayThongThuong().show_table(p);
                break;
            case 3:
                new itemz.rebuilds.TrangTriCayThongVip().show_table(p);
                break;
            case 4:
                EventData eventData = p.getDataEvent(ID_EVENT);
                if(eventData == null) {
                    return;
                }
                if(eventData.data[INDEX_MERRY] > 0) {
                    p.getService().send_box_ThongBao_OK("Hôm nay bạn đã chúc rồi");
                    return;
                }
                eventData.data[INDEX_MERRY]++;
                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(4, ITEM_KEO_GIANG_SINH, 10));
                core.RewardService.sendGiftOrMail(p, 1, "Quà Giáng Sinh", "Kho Báu Hải Tặc chúc bạn giáng sinh an lành!", listGift, true);
                break;
            case 5:
                p.typeBXH = 10;
                showEventRank(p);
                break;
            case 6: {
                String txt = "Sự Kiện Giáng Sinh Noel\n"
                        + "- Đánh quái rơi Kẹo Giáng Sinh và Vé Noel.\n"
                        + "- Dùng Vé Noel đổi Hộp Quà Noel để mở nhận nhiều phần quà quý giá.\n"
                        + "- Thu thập nguyên liệu trang trí Cây Thông Noel để nhận Vé Triệu Hồi Quái Vật Tuyết.\n"
                        + "- Ông Già Noel xuất hiện tặng quà lúc 1h, 5h, 9h, 16h, 20h hàng ngày.";
                p.getService().Help_From_Server(-1019, txt);
                break;
            }
            case 7:
                showTichNap(p);
                break;
            case 8:
                showTichTieu(p);
                break;
        }
    }
    
    public SantaNoel getSanta(short id) {
        return listSanta.get(id);
    }
    
    public SantaNoel getSantaInMap(Zone map) {
        for(SantaNoel santa : listSanta.values()) {
            if(santa != null && santa.map != null && santa.map.equals(map)) {
                return santa;
            }
        }
        return null;
    }
    
    @Override
    public void update(int hour, int min, int sec) {
        for(SantaNoel santa : listSanta.values()) {
            santa.update();
        }
        for(QuaiVatTuyetNoel quaiVatTuyet : listBoss) {
            if(!quaiVatTuyet.mob.isdie && quaiVatTuyet.timeLive < System.currentTimeMillis()) {
                quaiVatTuyet.mob.isdie = true;
                quaiVatTuyet.mob.hp = 0;
                quaiVatTuyet.mob.map.remove_obj(quaiVatTuyet.mob.index, 1);
            }
        }
        if(hour == 1 || hour == 5 || hour == 9 || hour == 16 || hour == 20) {
            if(min == 0 && sec == 0) {
                callSanta();
            } else if(min == 59 && sec == 59) {
                endSanta();
            }
        }
    }
    
    public void initGift() {
        double a = (double)9999.0 / (double)9.0;
        giftSanta.add(1, new MainItem(790, 4, 1));
        giftSanta.add(a, new MainItem(158, 4, 1));
        giftSanta.add(a, new MainItem(45, 4, 1));
        giftSanta.add(a, new MainItem(112, 4, 1));
        giftSanta.add(a, new MainItem(122, 4, 1));
        giftSanta.add(a, new MainItem(3, 7, 1));
        giftSanta.add(a, new MainItem(4, 7, 1));
        giftSanta.add(a, new MainItem(1, 7, 1));
        giftSanta.add(a, new MainItem(5, 7, 1));
        giftSanta.add(a, new MainItem(6, 7, 1));
        //
        a = (double)985.0 / (double)4.0;
        giftKillBossALL.add(a, new MainItem(158, 4, 1));
        giftKillBossALL.add(a, new MainItem(122, 4, 1));
        giftKillBossALL.add(a, new MainItem(45, 4, 1));
        giftKillBossALL.add(10, new MainItem(46, 4, 1));
        giftKillBossALL.add(5, new MainItem(692, 4, 1));
        giftKillBossALL.add(a, new MainItem(3, 7, 1));
        //
        a = (double)9995.0 / (double)4.0;
        giftNoelBox.add(a, new MainItem(1, 7, 1));
        giftNoelBox.add(a / 2.0, new MainItem(112, 4, 1));
        giftNoelBox.add(a / 2.0, new MainItem(122, 4, 1));
        
        giftNoelBox.add(5, new MainItem(692, 4, 2));
        
        giftNoelBox.add(a / 7.0, new MainItem(80, 4, 1)); // x2 lv sp
        giftNoelBox.add(a / 7.0, new MainItem(133, 4, 1)); // x3 lv sp
        giftNoelBox.add(a / 7.0, new MainItem(159, 4, 1)); // x2 skill sp
        
        giftNoelBox.add(a / 7.0, new MainItem(733, 4, 1)); // x2 skill dt
        giftNoelBox.add(a / 7.0, new MainItem(734, 4, 1)); // x3 skill dt
        giftNoelBox.add(a / 7.0, new MainItem(737, 4, 1)); // x2 lv dt
        giftNoelBox.add(a / 7.0, new MainItem(738, 4, 1)); // x skill dt
        
        giftNoelBox.add(a / 2.0, new MainItem(45, 4, 1));
        giftNoelBox.add(a / 2.0, new MainItem(46, 4, 1));
    }
    
    public void accectGiftSanta(Player p, EventData eventData) {
        if(p.item.able_bag() < 2) {
            p.getService().send_box_ThongBao_OK("Hành trang phải dư ít nhất 2 chỗ trống");
            return;
        }
        byte random = (byte) ZUtil.random(1, 3);
        ArrayList<MainItem> listGift = new ArrayList<>();
        for(int i = 0; i < random; i++) {
            MainItem mainItem = giftSanta.next();
            short id = mainItem.id;
            byte cat = mainItem.cat;
            int num = mainItem.num;
            
            if(id == 112) {
                id = (short) RandomCollection.listRuongCam.nextId();
            } else if(id == 122) {
                id = (short) RandomCollection.listRuongCamHe.nextId();
            } else if(id == 45) {
                id = (short) RandomCollection.listDa2.nextId();
            } else if(cat == 7) {
                num = ZUtil.random(1, 11);
            }
            listGift.add(new MainItem(id, cat, num));
        }
        eventData.data[INDEX_GIFT_SANTA]++;
        p.item.remove_item47(4, ITEM_KEO_GIANG_SINH, 1);
        MainItem.showGiftBox(p, "Quà Noel", "Santa Claus", listGift, true, true);
    }
    
    public void giftKillBoss(Player p) {
        ArrayList<MainItem> listGift = new ArrayList<>();
        listGift.add(RandomCollection.listDa4.nextMainItem());
        listGift.add(RandomCollection.listRuongCamHe.nextMainItem());
        listGift.add(new MainItem(158, 4, 2));
        listGift.add(RandomCollection.listManhDo.nextMainItem());
        MainItem.showGiftBox(p, "Quà Noel", "Hạ gục Boss", listGift, true, true);
    }
    
    public void giftKillBossALL(Player p) {
        ArrayList<MainItem> listGift = new ArrayList<>();
        byte random = (byte) ZUtil.random(1, 3);
        for(int i = 0; i < random; i++) {
            MainItem mainItem = giftKillBossALL.next();
            short id = mainItem.id;
            byte cat = mainItem.cat;
            int num = mainItem.num;
            
            if(id == 45) {
                id = (short) RandomCollection.listDa2.nextId();
            } else if(id == 122) {
                id = (short) RandomCollection.listRuongCamHe.nextId();
            } else if(id == 46) {
                id = (short) RandomCollection.listDa3.nextId();
            } else if(id == 692) {
                id = (short) RandomCollection.listManhDo.nextId();
            } else if(cat == 7) {
                num = ZUtil.random(1, 5);
            }
            listGift.add(new MainItem(id, cat, num));
        }
        MainItem.showGiftBox(p, "Quà Noel", "Hạ gục Boss chung", listGift, true, true);
    }
    
    public void giftOpenBox(Player p) {
        ArrayList<MainItem> listGift = new ArrayList<>();
        byte random = (byte) ZUtil.random(1, 3);
        for(int i = 0; i < random; i++) {
            MainItem mainItem = giftNoelBox.next();
            short id = mainItem.id;
            byte cat = mainItem.cat;
            int num = mainItem.num;
            
            if(id == 45) {
                id = (short) RandomCollection.listDa2.nextId();
            } else if(id == 112) {
                id = (short) RandomCollection.listRuongCam.nextId();
            } else if(id == 122) {
                id = (short) RandomCollection.listRuongCamHe.nextId();
            } else if(id == 46) {
                id = (short) RandomCollection.listDa3.nextId();
            } else if(id == 692) {
                id = (short) RandomCollection.listManhDo.nextId();
            } else if(cat == 7) {
                num = ZUtil.random(1, 3);
            }
            listGift.add(new MainItem(id, cat, num));
        }
        MainItem.showGiftBox(p, "Quà Noel", "Hộp quà Noel", listGift, true, true);
    }
    
    public void sendSanta(Player p, SantaNoel santa) {
        try {
            Message m = new Message(-5);
            m.writer().writeShort(santa.index_map);
            m.writer().writeByte(0);
            m.writer().writeByte(3);
            m.writer().writeByte(-1); // typePirate
            m.writer().writeByte(-1); // typePk
            m.writer().writeByte(1);
            m.writer().writeByte(-1); // index team
            m.writer().writeUTF("Ông già Noel");
            m.writer().writeShort(1); // level
            m.writer().writeInt(1000);
            m.writer().writeInt(1000);
            m.writer().writeShort(0);
            m.writer().writeInt(-1);
            m.writer().writeByte(0);
            //
            m.writer().writeShort(983);
            m.writer().writeByte(2);
            m.writer().writeShort(santa.index_map);
            m.writer().writeByte(-1);
            //
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
        } catch (java.io.IOException e) {
        }
    }



    @Override
    public boolean onUseItem(model.Player p, int itemId) throws java.io.IOException {
        if (!isEventActive()) {
            if (itemId == 489 || itemId == 227 || itemId == 168 || itemId == 789) {
                p.getService().send_box_ThongBao_OK("Sự kiện Noel đã kết thúc!");
                return false;
            }
            return false;
        }
        
        if (itemId == 168) { // Vé triệu hồi quái tuyết
            if (p.item.total_item_bag_by_id(4, 168) < 1) return false;
            if (callBoss(p)) {
                p.item.remove_item47(4, 168, 1);
                p.item.updateInventory(false);
                return true;
            } else {
                p.item.remove_item47(4, 168, 1);
                p.item.updateInventory(false);
                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, 50_000));
                core.RewardService.sendGiftOrMail(p, 1, "Vé Triệu Hồi Quái Tuyết", "Nhận được EXP", listGift, true);
                return true;
            }
        }
        else if (itemId == 489) { // Kẹo giáng sinh
            if (p.item.total_item_bag_by_id(4, 489) < 1) return false;
            p.item.remove_item47(4, 489, 1);
            p.item.updateInventory(false);

            List<template.GiftBox> listGift = new ArrayList<>();
            listGift.add(new template.GiftBox(99, 0, 500000));
            listGift.add(new template.GiftBox(4, 0, 50000));
            core.RewardService.sendGiftOrMail(p, 1, "Kẹo Giáng Sinh", "Thưởng thức kẹo giáng sinh ngọt ngào!", listGift, true);
            return true;
        } 
        else if (itemId == 227) { // Hộp quà Noel
            if (p.item.total_item_bag_by_id(4, 227) < 1) return false;
            if (p.item.able_bag() < 3) {
                p.getService().send_box_ThongBao_OK("Hành trang cần tối thiểu 3 ô trống!");
                return false;
            }
            p.item.remove_item47(4, 227, 1);
            giftOpenBox(p);
            p.item.updateInventory(false);
            return true;
        }
        return false;
    }



    @Override
    public void onMobKilled(model.Player p, mob.Mob mob) {
        if (!event.EventManager.isActive(ID_EVENT) || mob.is_boss) return;
        if (!isDropItemActive()) return;
        dropEventItem(p, mob, ITEM_KEO_GIANG_SINH, 10, 150, 0);
        dropEventItem(p, mob, ITEM_VE_NOEL, 5, 200, INDEX_TRAIN);
    }
}
