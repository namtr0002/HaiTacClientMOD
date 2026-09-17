package event;

import event.eboss.BiNgoMa;
import itemz.MainItem;
import model.VongQuay;
import model.Player;
import zabstracts.AbsRanked;
import core.Manager;
import core.MenuController;
import network.Service;
import core.ZUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.Zone;
import mob.Mob;
import template.MobTemplate;
import network.Message;

public class SuKienHalloween extends Event {
    
    public static final int INDEX_HQMQ = 29;
    public static final int INDEX_VQT = 28;
    public static final int INDEX_VQV = 27;
    public static final int INDEX_HH_DRACULA = 26;
    public static final int INDEX_HH_SOI = 25;
    public static final int MAX_CRE_HH = 200;

    public static SuKienHalloween instance;
    
    public int[] timeBoss = new int[]{9, 12, 15, 18, 21, 22};
    public boolean isUnlimitMapBoss;
    
    public List<BiNgoMa>  listBiNgoMa = new java.util.concurrent.CopyOnWriteArrayList<>();
    public List<MainItem> listManhDo  = new ArrayList<>();
    public List<MainItem> listDa23    = new ArrayList<>();
    
    public SuKienHalloween() {
        super(Event.ID_SUKIEN_HALLOWEEN_2025, "Sự Kiện Halloween");
        shopName     = "Shop Bí Ngô Quỷ";
        costItemId   = 426;   // Bí Ngô quỷ
        costItemType = 4;

        this.bxhSubTypes = new int[]{4, 5, 6, 7};
        this.bxhNames = new String[]{"Top Hộp Quà Ma Quái", "Top Vòng Quay Thường", "Top Vòng Quay VIP", "Top Nạp Halloween"};
        
        // [NPC_MIGRATED] npcConfigs = new Object[][]{
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 1,   -1021, (short) 380, (short) 170, 5000},
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 9,   -1021, (short) 445, (short) 174, 5000},
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 17,  -1021, (short) 390, (short) 170, 5000},
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 25,  -1021, (short) 655, (short) 171, 5000},
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 33,  -1021, (short) 415, (short) 200, 5000},
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 41,  -1021, (short) 340, (short) 176, 5000},
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 49,  -1021, (short) 355, (short) 174, 5000},
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 69,  -1021, (short) 360, (short) 160, 5000},
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 83,  -1021, (short) 375, (short) 170, 5000},
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 93,  -1021, (short) 390, (short) 152, 5000},
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 113, -1021, (short) 340, (short) 168, 5000},
        // [NPC_MIGRATED]             {"Bí Ngô Ma", "Bí ngô ma quái Halloween đây!", 191, -1021, (short) 340, (short) 147, 5000}
        // [NPC_MIGRATED]         };
        initShop();
        instance = this;
    }
    
    public static SuKienHalloween gI() {
        if (instance == null) instance = new SuKienHalloween();
        return instance;
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienHalloween(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }
    
    @Override
    public void init() {
        initShop();

        model.VongQuayHLW.init();
        
        listBiNgoMa = new ArrayList<>();
        for (int i = 0; i < 100; i++) listBiNgoMa.add(new BiNgoMa(i));
        
        listManhDo = new ArrayList<>();
        for (int i = 692; i <= 725; i++) {
            if (i >= 695 && i <= 704) continue;
            listManhDo.add(new MainItem(i, 4, 1));
        }
        
        listDa23 = new ArrayList<>();
        listDa23.add(new MainItem(45, 4, 1));
        listDa23.add(new MainItem(46, 4, 1));
        listDa23.add(new MainItem(51, 4, 1));
        listDa23.add(new MainItem(52, 4, 1));
        listDa23.add(new MainItem(57, 4, 1));
        listDa23.add(new MainItem(58, 4, 1));
        listDa23.add(new MainItem(63, 4, 1));
        listDa23.add(new MainItem(64, 4, 1));
        listDa23.add(new MainItem(69, 4, 1));
        listDa23.add(new MainItem(70, 4, 1));
        listDa23.add(new MainItem(75, 4, 1));
        listDa23.add(new MainItem(76, 4, 1));

        // // [NPC_MIGRATED] initNpc(); // NPC đã chuyển sang class iNpc riêng
    }
    
    public void GiftBossAll(Player pKill, Zone map) {
        List<MainItem> list = new ArrayList<>();
        
        list.add(new MainItem(ZUtil.random(417, 419), 4, ZUtil.random(1, 3)));
        
        if(ZUtil.random(1000) < 150) {
            list.add(new MainItem(ZUtil.random(112, 122), 4, ZUtil.random(1, 3)));
        }
        if(ZUtil.random(1000) < 150) {
            list.add(new MainItem(158, 4, ZUtil.random(1, 3)));
        }
        if(ZUtil.random(1000) < 20) {
            list.add(new MainItem(ZUtil.random(2) == 0 ? 80 : 737, 4, ZUtil.random(1, 3)));
        }
        if(ZUtil.random(1000) < 200) {
            list.add(new MainItem(11, 7, ZUtil.random(1, 3)));
        }
        if(ZUtil.random(1000) < 150) {
            list.add(new MainItem(6, 7, ZUtil.random(1, 3)));
        }
        
        while (list.size() > 2) {            
            list.remove(ZUtil.random(list.size() - 1));
        }
        
        if(list.isEmpty()) {
            list.add(new MainItem(ZUtil.random(112, 122), 4, ZUtil.random(1, 3)));
        }
        
        for(Player p : map.players) {
            if(pKill.IDPlayer == p.IDPlayer) continue;
            MainItem.showGiftBox(p, "Phần thưởng chung Boss", "Bí Ngô Ma Quái", list, true, true);
        }
    }
    
    public void GiftBossKiller(Player p) {
        List<MainItem> list = new ArrayList<>();
        list.add(new MainItem(426, 4, 1));
        list.add(new MainItem(listDa23.get(ZUtil.random(listDa23.size() - 1)).id, 4, 1));
        list.add(new MainItem(listManhDo.get(ZUtil.random(listManhDo.size() - 1)).id, 4, 1));
        list.add(new MainItem(121 + Math.min(Math.max(p.level / 10, 1), 10), 4, ZUtil.random(1, 6)));
        if(ZUtil.random(1000) < 5) {
            //list.add(new MainItem(727, 4, 1));
        }
        MainItem.showGiftBox(p, "Phần thưởng hạ Boss", "Bí Ngô Ma Quái", list, true, true);
    }
    
    public void OpenHQMQ(Player p) {
        if(p.getConnStatus() != 1) {
            return;
        }
        
        EventData eventData = p.getDataEvent(ID_SUKIEN_HALLOWEEN_2025);
        if(eventData == null) return;
        
        for(Player pl : p.map.players) {
            List<MainItem> list = new ArrayList<>();

            if(ZUtil.random(1000) < 10) {
                list.add(new MainItem(listManhDo.get(ZUtil.random(listManhDo.size() - 1)).id, 4, ZUtil.random(1, 3)));
            }
            if(ZUtil.random(1000) < 200) {
                list.add(new MainItem(29, 4, 1));
            }

            if(ZUtil.random(1000) < 200) {
                list.add(new MainItem(158, 4, 1));
            }

            if(ZUtil.random(1000) < 200) {
                list.add(new MainItem(1, 7, ZUtil.random(1, 4)));
            }

            if(ZUtil.random(1000) < 200) {
                list.add(new MainItem(6, 7, ZUtil.random(1, 3)));
            }

            if(ZUtil.random(1000) < 200) {
                list.add(new MainItem(ZUtil.random(112, 122), 4, 1));
            }

            if(ZUtil.random(1000) < 200) {
                list.add(new MainItem(111 + Math.min(Math.max(p.level / 10, 1), 10), 4, 1));
            }

            if(ZUtil.random(1000) < 200) {
                list.add(new MainItem(ZUtil.random(2) == 0 ? 80 : 737, 4, ZUtil.random(1, 3)));
            }

            while (list.size() > 2) {            
                list.remove(ZUtil.random(list.size() - 1));
            }

            if(list.isEmpty()) {
                list.add(new MainItem(1, 7, ZUtil.random(1, 4)));
            }

            MainItem.showGiftBox(pl, "Hộp Quà Ma Quái", p.name + " đã sử dụng", list, true, true);
            
            list.clear();
        }
        
        p.addEventPoint(ID_SUKIEN_HALLOWEEN_2025, "top_hqmq", INDEX_HQMQ, 1);
    }
    
    public void recoveryBoss(int id) {
        if(id >= listBiNgoMa.size()) {
            closeBoss();
        }
        
        if(id < 0 || id >= listBiNgoMa.size()) return;
        
        BiNgoMa biNgoMa = listBiNgoMa.get(id);
        biNgoMa.mob.hp = biNgoMa.mob.hp_max;
        biNgoMa.mob.isdie = false;
        biNgoMa.mob.sendMove();
        Manager.gI().chatKTG(0, "Boss Bí Ngô Ma Quái " + (biNgoMa.slotId + 1) + " đã xuất hiện tại 1-1 Rừng làng khu vực 5, các hải tặc hãy nhanh chóng tiêu diệt để nhận những phần quà hấp dẫn", 0);
    }
    
    public void createBoss() {
        Manager.gI().chatKTG(0, "Hoạt động săn Boss Bí Ngô Ma Quái bắt đầu, các hải tặc hãy nhanh chóng đến 1-1 Rừng làng khu vực 5 tham gia tiêu diệt Boss", 0);
        // System.out.println("Hoạt động săn Boss Bí Ngô Ma Quái bắt đầu, các hải tặc hãy nhanh chóng đến 1-1 Rừng làng khu vực 5 tham gia tiêu diệt Boss");
        for(BiNgoMa biNgoMa : listBiNgoMa) {
            biNgoMa.mob.hp = 0;
            biNgoMa.mob.isdie = true;
            biNgoMa.mob.map.remove_obj(biNgoMa.mob.index, 1);
        }
        recoveryBoss(0);
        isUnlimitMapBoss = true;
    }
    
    public void closeBoss() {
        for(BiNgoMa biNgoMa : listBiNgoMa) {
            biNgoMa.mob.hp = 0;
            biNgoMa.mob.isdie = true;
            biNgoMa.mob.map.remove_obj(biNgoMa.mob.index, 1);
        }
        if(isUnlimitMapBoss) {
            Manager.gI().chatKTG(0, "Hoạt động săn Boss Bí Ngô Ma Quái kết thúc, các hải tặc hãy nhanh chóng đổi quà tại NPC Robin tại Làng Cối Xoay Gió", 0);
            // System.out.println("Hoạt động săn Boss Bí Ngô Ma Quái kết thúc, các hải tặc hãy nhanh chóng đổi quà tại NPC Robin tại Làng Cối Xoay Gió");
            isUnlimitMapBoss = false;
        }
    }
    
    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(this.id)) return false;
        if (npcId == -1021) {
            sendMenu(p);
            return true;
        }
        return false;
    }

    public void sendMenu(Player p) {
        p.getService().openDynamicMenu(ID_SUKIEN_HALLOWEEN_2025, "Sự kiện Halloween",
        new String[]{
            "Shop Halloween",
            "Vòng quay thường",
            "Vòng quay VIP",
            "BXH Nạp sự kiện",
            "BXH Dùng HQMQ",
            "BXH Quay thường",
            "BXH Quay VIP",
            "Ghép huy hiệu Sói Ma",
            "Ghép huy hiệu Dracula Quỷ",
            "Hướng dẫn",
            "Tích nạp sự kiện",
            "Tích tiêu sự kiện"},
        null);
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!event.EventManager.isActive(this.id)) return false;
        if (npcId == -877 || npcId == -1021 || npcId == -100 || npcId == this.id) {
            processMenu(p, index);
            return true;
        }
        return false;
    }

    public void processMenu(Player p, int index) {
        try {
            switch (index) {
                case 0:
                    openShop(p);
                    break;
                case 1:
                    p.typeVongQuay = 1;
                    model.VongQuayHLW.gI().showTable(p);
                    break;
                case 2:
                    p.typeVongQuay = 2;
                    model.VongQuayHLW.gI().showTable(p);
                    break;
                case 3:
                    p.typeBXH = 7;
                    showEventRank(p);
                    break;
                case 4:
                    p.typeBXH = 4;
                    showEventRank(p);
                    break;
                case 5:
                    p.typeBXH = 5;
                    showEventRank(p);
                    break;
                case 6:
                    p.typeBXH = 6;
                    showEventRank(p);
                    break;
                case 7:
                    new itemz.rebuilds.GhepHuyHieuSoiMa().show_table(p);
                    break;
                case 8:
                    new itemz.rebuilds.GhepHuyHieuDracula().show_table(p);
                    break;
                case 9: {
                    String txt = "Sự Kiện Lễ Hội Halloween\n"
                            + "- Đánh quái rơi Kẹo Ma Quái, Răng Quỷ, Móng Sói và Hộp Quà Ma Quái.\n"
                            + "- Dùng Kẹo Ma Quái quay Vòng Quay Thường hoặc Vé Quay Vàng quay Vòng Quay VIP nhận vật phẩm khủng.\n"
                            + "- Ghép Huy Hiệu Sói Ma và Huy Hiệu Dracula Quỷ để sở hữu các bộ cải trang độc quyền.\n"
                            + "- Mở Hộp Quà Ma Quái nhận lượng lớn Exp, Beri và bảo vật ma quái.";
                    p.getService().Help_From_Server(-1021, txt);
                    break;
                }
                case 10:
                    showTichNap(p);
                    break;
                case 11:
                    showTichTieu(p);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUseItem(Player p, int itemId) throws IOException {
        if (!isEventActive()) {
            if (itemId == 213 || itemId == 801 || itemId == 802 || itemId == 894) {
                p.getService().send_box_ThongBao_OK("Sự kiện Halloween đã kết thúc!");
                return false;
            }
            return false;
        }
        if (itemId == 213) {
            if (p.item.total_item_bag_by_id(4, 213) < 1) return false;
            p.item.remove_item47(4, 213, 1);
            OpenHQMQ(p);
            p.item.updateInventory(false);
            return true;
        }
        return false;
    }

    public void processGhep(Player p, int id, String[] name) throws IOException {
        if(!event.EventManager.isActive(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025)) {
            return;
        }
        
        if(name.length != 1) {
            return;
        }
        
        if(!core.ZUtil.isnumber(name[0])) {
            p.getService().send_box_ThongBao_OK("Giá trị nhập vào không hợp lệ");
            return;
        }
        
        int value = Integer.parseInt(name[0]);
        if(value <= 0 || value > 100) {
            p.getService().send_box_ThongBao_OK("Số lượng phải từ 1 -> 100");
            return;
        }
        
        event.EventData eventData = p.getDataEvent(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025);
        if(eventData == null) return;
        
        int rubiNeed = id == 6900 ? 10 : 5;
        int idNeed = 417 + (id - 6900);
        int idRecive = 424 + (id - 6900);
        int indexData = id == 6900 ? SuKienHalloween.INDEX_HH_DRACULA : SuKienHalloween.INDEX_HH_SOI;
        
        int numCanCreate = SuKienHalloween.MAX_CRE_HH - (eventData.data[indexData] + value);
        if(numCanCreate < 0) {
            p.getService().send_box_ThongBao_OK("Chỉ có thể ghép tối đa " + 
                SuKienHalloween.MAX_CRE_HH + 
                " mỗi loại Huy Hiệu/ngày" + 
                ".(" + "Bạn đã ghép " + 
                eventData.data[indexData] + 
                "/" + SuKienHalloween.MAX_CRE_HH + ")");
            return;
        }
        
        boolean check = true;
        for(int i = 419; i <= 423; i++) {
            if(p.item.total_item_bag_by_id(4, i) < value) {
                check = false;
                break;
            }
        }
        
        if(p.item.total_item_bag_by_id(4, idNeed) < value) {
            check = false;
        }
        
        if(p.get_ngoc() < rubiNeed * value) {
            check = false;
        }
        
        if(!check) {
            p.getService().send_box_ThongBao_OK("Ghép 1 Huy hiệu cần:\n 5 loại Bí ngô(cam)\nHuy Hiệu(vàng) cùng loại\n" + rubiNeed + " Rubi");
            return;
        }
        
        for(int i = 419; i <= 423; i++) {
            p.item.remove_item47(4, i, value);
        }
        p.item.remove_item47(4, idNeed, value);
        p.update_ngoc(-rubiNeed*value);
        
        p.item.add_item_bag47(4, idRecive, value);
        p.item.updateInventory(false);
        p.updateMoney();
        
        p.getService().send_box_ThongBao_OK("Chúc mừng!\nBạn đã ghép Huy hiệu thành công");
        
        eventData.data[indexData]+= value;
    }

    public boolean isTimeBoss(int hour) {
        for(int time : timeBoss) {
            if(time == hour) return true;
        }
        return false;   
    }



    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            212, 213, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426,
            476, 477, 478, 479, 480, 481, 483, 484, 577
        };
    }

    @Override
    public void onMobKilled(Player p, mob.Mob mob) {
        if (!event.EventManager.isActive(this.id) || mob.is_boss) return;
        if (!isDropItemActive()) return;
        dropEventItem(p, mob, 426, 10, 150, 0); // Bí Ngô quỷ (426), 10% cơ bản, giới hạn 150/ngày tại index 0

        // kẹo halloween ngẫu nhiên (419-423)
        if (core.ZUtil.random(1000) < 75) {
            int idItem = core.ZUtil.random(419, 424);
            if (p.item.can_add_item_bag47(4, idItem, 1)) {
                p.item.add_item_bag47(4, idItem, 1);
                p.item.updateInventory(false);
            }
        }
    }
}
