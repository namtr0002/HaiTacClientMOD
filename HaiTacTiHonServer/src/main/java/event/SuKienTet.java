package event;

import network.Service;

import model.Player;
import core.Manager;
import core.ZUtil;
import template.DataTemplate;
import template.ItemTemplate4;
import map.Npc;
import map.MapTemplate;
import map.Zone;
import mob.Mob;
import map.MapBossInfo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SuKienTet extends Event {
    public static final int ID_EVENT_TET = 2;
    private static SuKienTet instance;

    /*
    350	Bánh chưng
    351	Lá dong
    352	Đậu xanh
    353	Gạo nếp
    354	Thịt heo
    355	Rương nguyên liệu tết
    356	Hộp trang phục sơ
    357	Bao lì xì Canh Tý
    358	Thời trang tết 30 ngày
    359	Pháo hoa
    */
    // --- CẤU HÌNH VẬT PHẨM SỰ KIỆN ---
    public static final int ITEM_BANH_CHUNG = 350;
    public static final int ITEM_LA_DONG = 351;
    public static final int ITEM_DAU_XANH = 352;
    public static final int ITEM_GAO_NEP = 353;
    public static final int ITEM_THIT_HEO = 354;
    public static final int ITEM_RUONG_NL = 355;
    public static final int ITEM_HOP_TRANG_PHUC_SO = 356;
    public static final int ITEM_BAO_LIXI = 357;
    public static final int ITEM_THOI_TRANG_30N = 358;
    public static final int ITEM_PHAO_HOA = 359;
    
    // Spawning timeframes
    private static final int[] SPAWN_HOURS = {12, 19, 21};
    private static final int[] CLEAR_HOURS = {13, 20, 22};
    private static final long RESPAWN_DELAY = 300_000L; // 5 minutes respawn delay
    private long bossDeathTime = 0;
    private boolean spawnedThisTimeframe = false;

    public SuKienTet() {
        super(ID_EVENT_TET, "Sự Kiện Tết");
        shopName     = "Shop Sự Kiện Tết";
        costItemId   = ITEM_BAO_LIXI;
        costItemType = 4;

        this.bxhSubTypes = new int[]{201, 202};
        this.bxhNames = new String[]{"Top Đốt Pháo", "Top Làm Bánh Chưng"};
        sellableItems = new short[]{};

        initShop();
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            162, 163, 164, 165, 166, 167, 169, 170, 236, 237, 238, 239,
            350, 351, 352, 353, 354, 355, 357, 429, 430, 431, 432, 433,
            523, 524, 525, 629, 630, 631, 632, 633, 634, 635, 636
        };
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienTet(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();
    }

    public static SuKienTet gI() {
        if (instance == null) {
            instance = new SuKienTet();
        }
        return instance;
    }

    @Override
    public void update(int hour, int min, int sec) throws Exception {
        if (!event.EventManager.isActive(ID_EVENT_TET)) return;

        // Check if current hour is within any active timeframe
        boolean inTimeframe = false;
        for (int i = 0; i < SPAWN_HOURS.length; i++) {
            if (hour >= SPAWN_HOURS[i] && hour < CLEAR_HOURS[i]) {
                inTimeframe = true;
                break;
            }
        }

        zabstracts.AbsBoss bossNgua = boss.BossManager.gI().getBossById(174);
        if (bossNgua == null) return;

        if (inTimeframe) {
            if (!spawnedThisTimeframe) {
                // Spawn boss immediately when entering timeframe
                Zone targetZone = Zone.getMapByID(2)[0]; // Zone 0 of Map 2
                bossNgua.spawn(targetZone, (short) 300, (short) 300, Manager.gI().index_mob.getAndIncrement());
                spawnedThisTimeframe = true;
                bossDeathTime = 0;
                Manager.gI().chatKTG(0, "Boss Ngựa 9 Hồng Mao đã xuất hiện tại " + targetZone.template.name + "!", 5);
            } else if (bossNgua.isdie) {
                if (bossDeathTime == 0) {
                    bossDeathTime = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - bossDeathTime >= RESPAWN_DELAY) {
                    // Respawn boss after delay
                    Zone targetZone = Zone.getMapByID(2)[0]; // Zone 0 of Map 2
                    bossNgua.spawn(targetZone, (short) 300, (short) 300, Manager.gI().index_mob.getAndIncrement());
                    bossDeathTime = 0;
                    Manager.gI().chatKTG(0, "Boss Ngựa 9 Hồng Mao đã hồi sinh tại " + targetZone.template.name + "!", 5);
                }
            } else {
                bossDeathTime = 0;
            }
        } else {
            // Outside timeframe: auto-clear if still alive
            if (spawnedThisTimeframe) {
                spawnedThisTimeframe = false;
            }
            if (!bossNgua.isdie) {
                bossNgua.isdie = true;
                removeCustomMob(bossNgua.map, bossNgua);
                Manager.gI().chatKTG(0, "Boss Ngựa 9 Hồng Mao đã rút lui!", 5);
            }
            bossDeathTime = 0;
        }
    }

    public static void removeCustomMob(Zone zone, Mob mob) {
        if (zone == null || mob == null) return;
        try {
            zone.remove_obj(mob.index, 1);
        } catch (Exception e) {}
        Mob.ENTRYS.remove(mob.index);
        zone.mobs.remove(mob.index);
        
        if (zone.list_mob != null) {
            int[] newList = new int[zone.list_mob.length - 1];
            int idx = 0;
            for (int id : zone.list_mob) {
                if (id != mob.index) {
                    if (idx < newList.length) {
                        newList[idx++] = id;
                    }
                }
            }
            zone.list_mob = newList;
        }
    }

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!event.EventManager.isActive(this.id) || mob.is_boss) return;
        if (!isDropItemActive()) return;
        
        // 1. Drop standard Tết ingredients: 351 (Lá dong) to 354 (Thịt heo)
        int[] ingredients = new int[]{351, 352, 353, 354};
        dropEventItemRandom(p, mob, ingredients, 10, 150, 5); // 10% cơ bản, giới hạn 150/ngày tại index 5
        
        // 2. Drop special Tết gift boxes: 355 (Rương nguyên liệu tết) and 357 (Bao lì xì Canh Tý)
        // Rate: 1 in 500 (0.2%)
        if (core.ZUtil.random(500) == 0) {
            EventData evData = getOrCreateEventData(p);
            if (evData != null && evData.data[5] < 200) { // giới hạn số lượng rơi
                evData.data[5]++;
                p.item.add_item_bag47(4, 355, 1);
                p.item.add_item_bag47(4, 357, 1);
                p.item.updateInventory(false);
            }
        }
    }

    @Override
    public boolean onUseItem(Player p, int id) {
        if (!isEventActive()) {
            if (id == ITEM_PHAO_HOA || id == ITEM_BAO_LIXI || id == ITEM_RUONG_NL || id == ITEM_BANH_CHUNG) {
                p.getService().send_box_ThongBao_OK("Sự kiện Tết đã kết thúc!");
                return false;
            }
            return false;
        }

        try {
            if (id == ITEM_PHAO_HOA) {
                if (p.item.total_item_bag_by_id(4, id) < 1) return false;
                p.item.remove_item47(4, id, 1);
                p.update_pointEvent1(1);
                p.item.updateInventory(false);
                return true;
            } else if (id == ITEM_BAO_LIXI) {
                if (p.item.total_item_bag_by_id(4, ITEM_BAO_LIXI) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Bao Lì Xì!");
                    return false;
                }
                
                p.item.remove_item47(4, ITEM_BAO_LIXI, 1);
                p.item.updateInventory(false);

                List<template.GiftBox> listGift = new ArrayList<>();
                int rand = ZUtil.random(100);
                if (rand < 50) {
                    listGift.add(new template.GiftBox(4, 0, 500000));
                } else if (rand < 80) {
                    listGift.add(new template.GiftBox(4, 1, 100));
                } else {
                    listGift.add(new template.GiftBox(4, 0, 100000));
                }
                core.RewardService.sendGiftOrMail(p, 1, "Bao Lì Xì Canh Tý", "Chúc mừng năm mới an khang thịnh vượng!", listGift, true);
                return true;
            } else if (id == ITEM_RUONG_NL) {
                if (p.item.total_item_bag_by_id(4, ITEM_RUONG_NL) < 1) {
                    p.getService().send_box_ThongBao_OK("Bạn không có Rương Nguyên Liệu!");
                    return false;
                }
                
                p.item.remove_item47(4, ITEM_RUONG_NL, 1);
                p.item.updateInventory(false);

                int[] materials = {ITEM_LA_DONG, ITEM_DAU_XANH, ITEM_GAO_NEP, ITEM_THIT_HEO};
                int matId = materials[ZUtil.random(materials.length)];

                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(4, matId, 1));
                core.RewardService.sendGiftOrMail(p, 1, "Rương Nguyên Liệu Tết", "Nhận được nguyên liệu làm bánh!", listGift, true);
                return true;
            } else if (id == ITEM_BANH_CHUNG) {
                if (p.item.total_item_bag_by_id(4, id) < 1) return false;
                
                p.item.remove_item47(4, id, 1);
                p.item.updateInventory(false);

                long expEarn = (long) p.level * 3000L;
                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, expEarn)));
                listGift.add(new template.GiftBox(4, 0, 100_000));

                core.RewardService.sendGiftOrMail(p, 1, "Bánh Chưng", "Thưởng thức bánh tết thơm ngon!", listGift, true);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(ID_EVENT_TET)) return false;
        switch (npcId) {
            case -1011:
                p.getService().openDynamicMenu(npcId, core.MenuController.get_name_npc(npcId), new String[]{"Làm Bánh Chưng", "Hướng dẫn"}, null);
                return true;
            case -100:
            case ID_EVENT_TET:
            case -1012:
                p.getService().openDynamicMenu(npcId, core.MenuController.get_name_npc(npcId), new String[]{"Cửa Hàng Tết", "Đổi quà Bánh Chưng", "Phó bản Ngựa Sát Thủ", "BXH Đốt Pháo", "BXH Bánh Chưng", "Hướng dẫn", "Tích nạp sự kiện", "Tích tiêu sự kiện"}, null);
                return true;
            case -1013:
                p.getService().openDynamicMenu(npcId, core.MenuController.get_name_npc(npcId), new String[]{"Hái lộc", "Hướng dẫn"}, null);
                return true;
        }
        return false;
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!event.EventManager.isActive(ID_EVENT_TET)) return false;

        switch (npcId) {
            case -1011: // Nồi Bánh Chưng
                if (index == 0) { // Làm Bánh Chưng
                    new itemz.rebuilds.GhepBanhChung350().show_table(p);
                    return true;
                } else if (index == 1) { // Hướng dẫn
                    String txt = "Sự Kiện Tết - Nồi Bánh Chưng\n"
                            + "- Làm Bánh Chưng cần: 5 Gạo nếp + 5 Đậu xanh + 5 Lá dong + 5 Thịt heo + 50 Ruby.\n"
                            + "- Các nguyên liệu rơi ngẫu nhiên khi đánh quái ngoài map.";
                    p.getService().Help_From_Server(npcId, txt);
                    return true;
                }
                break;

            case -100:
            case ID_EVENT_TET:
            case -1012: // Boa Hancock
                if (index == 0) { // Cửa hàng Tết
                    openShop(p);
                    return true;
                } else if (index == 1) { // Đổi Quà Bánh Chưng
                    if (p.item.total_item_bag_by_id(4, ITEM_BANH_CHUNG) >= 1) {
                        p.item.remove_item47(4, ITEM_BANH_CHUNG, 1);
                        if (p.item.able_bag() == 0) {
                            p.getService().send_box_ThongBao_OK("Hành trang đã đầy!");
                            return true;
                        }
                        List<template.GiftBox> listGift = new ArrayList<>();
                        int rand = ZUtil.random(100);
                        if (rand < 30) {
                            listGift.add(new template.GiftBox(4, ITEM_BAO_LIXI, 1));
                        } else if (rand < 60) {
                            listGift.add(new template.GiftBox(4, ITEM_RUONG_NL, 1));
                        } else {
                            listGift.add(new template.GiftBox(4, 0, 50000));
                        }
                        p.item.updateInventory(false);
                        core.RewardService.sendGiftOrMail(p, 1, "Đổi Bánh Chưng", "Quà đổi từ Bánh Chưng", listGift, true);
                    } else {
                        p.getService().send_box_ThongBao_OK("Bạn không có đủ Bánh Chưng!");
                    }
                    return true;
                } else if (index == 2) { // Mở Phó Bản Ngựa Sát Thủ
                    return spawnInstanceBoss(p, 124, new int[]{175, 5000000, 100});
                } else if (index == 3) { // BXH Đốt Pháo
                    p.typeBXH = 201;
                    showEventRank(p);
                    return true;
                } else if (index == 4) { // BXH Bánh Chưng
                    p.typeBXH = 202;
                    showEventRank(p);
                    return true;
                } else if (index == 5) { // Hướng dẫn
                    String txt = "Sự Kiện Tết - Boa Hancock\n"
                            + "- Cửa Hàng Tết: Đổi Bao Lì Xì lấy các vật phẩm trang phục, pháo hoa và rương nguyên liệu.\n"
                            + "- Đổi quà Bánh Chưng: Mang Bánh Chưng tới đây đổi các bao lì xì và nguyên liệu hiếm.\n"
                            + "- Phó bản Ngựa Sát Thủ: Tổ đội khiêu chiến boss Ngựa Sát Thủ nhận Rương Nguyên Liệu.";
                    p.getService().Help_From_Server(npcId, txt);
                    return true;
                } else if (index == 6) {
                    showTichNap(p);
                    return true;
                } else if (index == 7) {
                    showTichTieu(p);
                    return true;
                }
                break;

            case -1013: // Cây Lì Xì
                if (index == 0) { // Hái Lộc
                    return haiLoc(p);
                } else if (index == 1) { // Hướng dẫn
                    String txt = "Sự Kiện Tết - Cây Lì Xì\n"
                            + "- Hái lộc miễn phí theo 4 khung giờ trong ngày: 0h-6h, 6h-12h, 12h-18h, 18h-24h.\n"
                            + "- Mỗi khung giờ hái 1 lần nhận 1 Bao Lì Xì Canh Tý.";
                    p.getService().Help_From_Server(npcId, txt);
                    return true;
                }
                break;
        }
        return false;
    }

    // --- HÀM HỖ TRỢ XỬ LÝ SỰ KIỆN ---

    private boolean haiLoc(Player p) {
        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
        int hour = now.getHour();
        int currentPeriod = -1;
        
        if (hour >= 0 && hour < 6) currentPeriod = 0;
        else if (hour >= 6 && hour < 12) currentPeriod = 1;
        else if (hour >= 12 && hour < 18) currentPeriod = 2;
        else if (hour >= 18 && hour < 24) currentPeriod = 3;

        if (p.pointEvent2 == currentPeriod) {
            p.getService().send_box_ThongBao_OK("Bạn đã hái lộc trong khung giờ này rồi. Hãy quay lại vào khung giờ tiếp theo!");
            return true;
        }

        if (p.item.able_bag() == 0) {
            p.getService().send_box_ThongBao_OK("Hành trang đã đầy!");
            return true;
        }

        p.pointEvent2 = currentPeriod;
        List<template.GiftBox> listGift = new ArrayList<>();
        listGift.add(new template.GiftBox(4, ITEM_BAO_LIXI, 1));
        core.RewardService.sendGiftOrMail(p, 1, "Hái Lộc Đầu Năm", "Hái lộc thành công!", listGift, true);
        return true;
    }

    private boolean spawnInstanceBoss(Player p, int mapTemplateId, int[] cfg) throws IOException {
        if (p.party == null) {
            p.getService().send_box_ThongBao_OK("Bạn phải có tổ đội để tham gia phó bản!");
            return true;
        }
        if (!p.party.list.get(0).equals(p)) {
            p.getService().send_box_ThongBao_OK("Chỉ trưởng nhóm mới có thể mở phó bản!");
            return true;
        }
        
        Zone map_boss = new Zone();
        map_boss.template = Zone.getMapByID(mapTemplateId)[0].template;
        map_boss.zone_id = (byte) 0;
        map_boss.list_mob = new int[0];
        
        MapBossInfo info = new MapBossInfo();
        info.map = map_boss;
        info.mob = new ArrayList<>();
        
        Mob boss = new Mob();
        boss.is_boss = true;
        boss.x = 200;
        boss.y = 200;
        boss.hp_max = cfg[1];
        boss.hp = boss.hp_max;
        boss.level = cfg[2];
        boss.mtemplate = template.MobTemplate.get_mob_template(cfg[0]);
        boss.isdie = false;
        boss.id_target = -1;
        boss.index = Manager.gI().index_mob.getAndIncrement();
        boss.map = map_boss;
        boss.isSieuTrum = false;
        
        // Dynamically integrate reward to the spawned boss instance
        boss.iMob = new zinterfaces.iMob() {
            @Override
            public void onDeath(Player pKill, Mob mobTarget) {
                try {
                    mobTarget.map.remove_obj(mobTarget.index, 1);
                    
                    ArrayList<template.GiftBox> list_gift = new ArrayList<>();
                    template.ItemTemplate4 it_temp4 = template.ItemTemplate4.get_it_by_id(ITEM_RUONG_NL);
                    if (it_temp4 != null) {
                        template.GiftBox gb = new template.GiftBox();
                        gb.id = it_temp4.id;
                        gb.type = 4;
                        gb.name = it_temp4.name;
                        gb.icon = it_temp4.icon;
                        gb.num = 5;
                        gb.color = 0;
                        list_gift.add(gb);
                    }
                    
                    pKill.updateMoney();
                    if (!list_gift.isEmpty()) {
                        core.RewardService.sendGiftOrMail(pKill, 1, "Ngựa Sát Thủ", "Phần thưởng hạ gục", list_gift, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Mob.ENTRYS.put(boss.index, boss);
        if (boss.map != null) {
            boss.map.mobs.put(boss.index, boss);
        }
        info.mob.add(boss);
        
        MapBossInfo.add(info);
        
        for (int i = 0; i < p.party.list.size(); i++) {
            Player mem = p.party.list.get(i);
            mem.map_boss_info = info;
            mem.map.leave_map(mem, 2);
            mem.x = 100;
            mem.y = 200;
            mem.map = map_boss;
            mem.goto_map(new map.Vgo());
        }
        return true;
    }
    
    @Override
    public void initPlayerData(EventData ev) {
        super.initPlayerData(ev);
    }
}
