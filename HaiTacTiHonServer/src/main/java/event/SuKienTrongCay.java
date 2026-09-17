package event;

import model.Tree;
import map.zones.ThuLinhBienKhoi;
import model.Player;
import map.Zone;
import network.Message;
import network.Service;
import clan.ClanChat;
import template.GiftBox;
import template.ItemTemplate4;
import template.ItemTemplate4_Info;
import template.ItemTemplate7;
import mob.Mob;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * SuKienTrongCay — Sự kiện Trồng Cây (event id = 9).
 *
 * Gộp toàn bộ logic từ core.TrongCay + event.EventTrongCay vào đây.
 * Các hằng số index EventData:
 *   data[0..SHOP_SIZE-1] = lượt đổi shop
 *   data[SHOP_SIZE + 2]  = giỏ trái cây tích lũy (nhiệm vụ)
 *   data[SHOP_SIZE + 3]  = hạt giống nhặt từ quái (max 10)
 *   data[SHOP_SIZE + 4]  = cây giống mua (max 1000)
 *   data[SHOP_SIZE + 5]  = nước tưới bang (max 5)
 *   data[SHOP_SIZE + 6]  = giỏ trái cây qua đánh quái (max 20)
 *   data[SHOP_SIZE + 8]  = vườn cam Nami (max 8)
 *   data[SHOP_SIZE + 9]  = liên tầng tầng 7 (max 8)
 *   data[SHOP_SIZE + 12] = PVP thắng (max 6)
 *   data[SHOP_SIZE + 13] = truy nã thắng (max 6)
 */
public class SuKienTrongCay extends Event {

    private static SuKienTrongCay instance;

    public static SuKienTrongCay gI() {
        if (instance == null) {
            instance = new SuKienTrongCay();
        }
        return instance;
    }

    // ======================== SHOP CONFIG ========================
    public static final int     SHOP_SIZE  = 8;

    // Offset trong data[] của EventData
    public static final int IDX_GIO_TRAI_CAY   = SHOP_SIZE + 2;
    public static final int IDX_HAT_GIONG      = SHOP_SIZE + 3;
    public static final int IDX_CAY_GIONG_MUA  = SHOP_SIZE + 4;
    public static final int IDX_NUOC_TUOI_BANG = SHOP_SIZE + 5;
    public static final int IDX_GIO_QUA_DANH   = SHOP_SIZE + 6;
    public static final int IDX_VUON_CAM       = SHOP_SIZE + 8;
    public static final int IDX_LIEN_TANG      = SHOP_SIZE + 9;
    public static final int IDX_PVP            = SHOP_SIZE + 12;
    public static final int IDX_TRUY_NA        = SHOP_SIZE + 13;

    // Item tưới nước = id 396, hạt giống lớn = 387, phân = 389, nước = 390
    public static final int ITEM_GIO_TRAI_CAY = 396;
    public static final int ITEM_HAT_GIONG    = 387;
    public static final int ITEM_PHAN         = 389;
    public static final int ITEM_NUOC         = 390;
    public static final int ITEM_PHAN_THUONG_THUONG = 388;


    // ======================== ENTRY MAP ========================
    public static final HashMap<String, Tree> ENTRY = new HashMap<>();

    public static Tree getByName(String name) {
        synchronized (ENTRY) { return ENTRY.get(name); }
    }

    public static Tree setByName(String name, Tree t) {
        synchronized (ENTRY) { return ENTRY.put(name, t); }
    }

    public static Tree getByIdx(short index) {
        synchronized (ENTRY) {
            for (Tree t : ENTRY.values()) {
                if (t.index == index) return t;
            }
        }
        return null;
    }

    public static void remove(Tree t) {
        synchronized (ENTRY) { ENTRY.remove(t.name); }
    }

    public static short getIndex(int id) {
        return (short) (id + 15_000);
    }

    // ======================== CONSTRUCTOR ========================
    public SuKienTrongCay() {
        super(9, "Sự Kiện Trồng Cây");
        this.costItemId = ITEM_GIO_TRAI_CAY;
        this.costItemType = 4;
        this.bxhSubTypes = new int[]{901, 902};
        this.bxhNames = new String[]{"Top Trồng Cây", "Top Gom Trái Cây"};

        initShop();
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            378, 379, 380, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396
        };
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienTrongCay(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        super.init();
        initShop();
        
        lienTangReward = new ActivityReward((byte) IDX_LIEN_TANG, 8);
        lienTangReward.setGiftPoint(IDX_GIO_TRAI_CAY, 1);

        pvpReward = new ActivityReward((byte) IDX_PVP, 6);
        pvpReward.setGiftPoint(IDX_GIO_TRAI_CAY, 1);

        wantedReward = new ActivityReward((byte) IDX_TRUY_NA, 6);
        wantedReward.setGiftPoint(IDX_GIO_TRAI_CAY, 1);

        // Note: SuKienTrongCay also has a random drop from mobs for point IDX_GIO_QUA_DANH, 
        // but since trainReward limit index is different (IDX_GIO_QUA_DANH max 20), we can configure it:
        trainReward = new ActivityReward((byte) IDX_GIO_QUA_DANH, 20);
        trainReward.setGiftPoint(IDX_GIO_TRAI_CAY, 1);
        
        // Cấu hình test rơi vật phẩm ở map Liên Tầng (tầng 1)
        // Rớt item type 4 (Vé Kibi hoặc Hạt Giống)
        mapDrops.add(new EventMapDrop(map.zones.Map_Lien_Tang.class, 1, 100, 4, 158, 5));
        
        // Rớt item type 3 (kèm Option tùy chỉnh)
        List<template.Option> opts = new ArrayList<>();
        opts.add(new template.Option(0, 500)); // hp = 500
        opts.add(new template.Option(4, 150)); // dame = 150
        mapDrops.add(new EventMapDrop(map.zones.Map_Lien_Tang.class, 1, 100, 3, 45, 1, opts));

        // // [NPC_MIGRATED] initNpc(); // NPC đã chuyển sang class iNpc riêng
    }

    // ======================== LIFECYCLE ========================
    @Override
    public void initPlayerData(EventData data) {
        int size = Math.max(30, shopItems.size() + 14);
        if (data.data == null || data.data.length < size) {
            int[] oldData = data.data;
            data.data = new int[size];
            if (oldData != null) {
                System.arraycopy(oldData, 0, data.data, 0, Math.min(oldData.length, size));
            }
        }
        super.initPlayerData(data);
    }

    @Override
    public void resetDailyData(EventData data) {
        if (data == null || data.data == null) return;
        super.resetDailyData(data);
        for (int j = shopItems.size() + 3; j < data.data.length; j++) {
            if (j == 23 || j == 25 || j == 26 || j == 27 || j == 28) {
                continue;
            }
            data.data[j] = 0;
        }
        if (shopItems.size() + 1 < data.data.length) {
            data.data[SHOP_SIZE + 1]++;
        }
    }

    /** Tick mỗi giây — update tất cả cây đang sống */
    @Override
    public void update(int hour, int min, int sec) throws Exception {
        List<Tree> listRemove = new ArrayList<>();
        synchronized (ENTRY) {
            for (Tree t : ENTRY.values()) {
                if (t.timeThuHoach < System.currentTimeMillis()) {
                    if (!t.canThuHoach) {
                        Player p0 = Zone.get_player_by_name_allmap(t.name);
                        if (p0 != null) {
                            if (t.type == 2) {
                                ClanChat chat = new ClanChat();
                                chat.idMem = p0.clan.members.get(0).id;
                                chat.name  = p0.clan.members.get(0).name;
                                chat.str   = "Trồng cây đã hoàn thành, có thể thu hoạch";
                                chat.time  = System.currentTimeMillis();
                                chat.typeChat = -3;
                                p0.clan.add_chat(chat);
                                p0.clan.send_chat(chat, null);
                            } else {
                                p0.getService().send_box_ThongBao_OK("Cây của bạn đã có thể thu hoạch");
                            }
                        }
                        t.canThuHoach = true;
                    }
                } else {
                    // Tính state
                    if (t.timeBonPhan < System.currentTimeMillis() && t.timeTuoiNuoc < System.currentTimeMillis()) {
                        t.stateNew = 3;
                    } else if (t.timeTuoiNuoc < System.currentTimeMillis()) {
                        t.stateNew = 1;
                    } else if (t.timeBonPhan < System.currentTimeMillis()) {
                        t.stateNew = 2;
                    } else {
                        t.stateNew = 0;
                    }

                    if (t.timeBonPhan < System.currentTimeMillis() || t.timeTuoiNuoc < System.currentTimeMillis()) {
                        // Cây đang khô/thiếu nước -> giảm HP
                        if (t.timeHp < System.currentTimeMillis()) {
                            t.hp -= 1;
                            if (t.hp <= 0) {
                                listRemove.add(t);
                            } else {
                                Service.sendTreeHp(t.map, t.index, t.hp);
                            }
                            t.timeHp = System.currentTimeMillis() + 100L;
                        }
                    } else {
                        // Cây đủ nước -> tăng HP theo số người trong map
                        int numP = t.map.players.size() - 1;
                        if (numP > 0) {
                            t.timeTuoiNuoc -= 400 * numP;
                            t.timeBonPhan  -= 400 * numP;
                        }
                        if (t.timeHp < System.currentTimeMillis()) {
                            t.hp = Math.min(100, t.hp + 5);
                            Service.sendTreeHp(t.map, t.index, t.hp);
                            t.timeHp = System.currentTimeMillis() + 2_000L;
                        }
                    }
                }

                // Gửi state thay đổi
                if (t.stateOld != t.stateNew) {
                    t.stateOld = t.stateNew;
                    Service.sendTreeState(t.map, t.index, t.stateOld, t.timeThuHoach);
                }
            }
        }

        // Xử lý cây chết
        for (Tree t : listRemove) {
            try {
                t.map.remove_obj(t.index, 2);
                remove(t);
                Player p0 = Zone.get_player_by_name_allmap(t.name);
                if (p0 != null) {
                    p0.getService().send_box_ThongBao_OK("Cây của bạn đã chết");
                }
                if (!t.nameKill.isBlank()) {
                    p0 = Zone.get_player_by_name_allmap(t.nameKill);
                    if (p0 != null) {
                        List<GiftBox> listGift = new ArrayList<>();
                        if (t.type == 0) {
                            GiftBox.addGift(listGift, 4, 387, 1);
                        } else if (t.type == 1) {
                            GiftBox.addGift(listGift, 4, 388, 1);
                        }
                        if (!listGift.isEmpty()) {
                            core.RewardService.sendGiftOrMail(p0, 1, "Trồng Cây", "Cướp được", listGift, true);
                        }
                    }
                }
            } catch (Exception e) { /* ignore */ }
        }
    }

    // ======================== SHOP ========================



    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!event.EventManager.isActive(9)) return false;
        if (npcId == -1023 || npcId == -100 || npcId == 9) {
            p.getService().openDynamicMenu(npcId, "NPH Trồng Cây", new String[]{"Cửa hàng trồng cây", "BXH Trồng Cây", "BXH Gom Trái Cây", "Hướng dẫn", "Tích nạp sự kiện", "Tích tiêu sự kiện"}, null);
            return true;
        }
        Tree t = getByName(p.name);
        if (t != null && (short) t.index == (short) npcId) { // [FIX BUG-6] cast explicit short
            p.getService().openDynamicMenu(947, "Trồng cây", new String[]{"Tưới cây", "Bón phân", "Thu hoạch", "Trạng thái cây", "BXH Trồng Cây", "BXH Gom Trái Cây", "Tích nạp sự kiện", "Tích tiêu sự kiện"}, null);
            return true;
        }
        // [FIX BUG-1] Kiểm tra p.clan != null trước khi truy cập
        if (p.clan != null && p.clan.members != null && !p.clan.members.isEmpty()) {
            t = getByName(p.clan.members.get(0).name);
            if (t != null && (short) t.index == (short) npcId) {
                p.getService().openDynamicMenu(947, "Trồng cây", new String[]{"Tưới cây", "Bón phân", "Thu hoạch", "Trạng thái cây", "BXH Trồng Cây", "BXH Gom Trái Cây", "Tích nạp sự kiện", "Tích tiêu sự kiện"}, null);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleMenu(Player p, int menuId, int index) throws IOException {
        if (!event.EventManager.isActive(9)) return false;
        if (menuId == -1023 || menuId == -100 || menuId == 9) {
            switch (index) {
                case 0:
                    openShop(p);
                    break;
                case 1:
                    p.typeBXH = 1;
                    showEventRank(p);
                    break;
                case 2:
                    p.typeBXH = 2;
                    showEventRank(p);
                    break;
                case 3: {
                    String txt = "Sự Kiện Trồng Cây\n"
                            + "- Tìm hạt giống, phân bón và nước tưới khi đánh quái.\n"
                            + "- Trồng và chăm sóc cây đúng cách để thu hoạch Giỏ Trái Cây đổi nhiều phần thưởng hiếm.";
                    p.getService().Help_From_Server(menuId, txt);
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
        }
        if (menuId == 947) {
            if (index == 4) {
                p.typeBXH = 901;
                showEventRank(p);
                return true;
            } else if (index == 5) {
                p.typeBXH = 902;
                showEventRank(p);
                return true;
            } else if (index == 6) {
                showTichNap(p);
                return true;
            } else if (index == 7) {
                showTichTieu(p);
                return true;
            }

            // [FIX BUG-1,4] Tìm cây của bản thân hoặc trưởng băng an toàn
            Tree t = getByName(p.name);
            if (t == null) {
                // [FIX BUG-1] Kiểm tra p.clan != null và members không rỗng
                if (p.clan != null && p.clan.members != null && !p.clan.members.isEmpty()) {
                    t = getByName(p.clan.members.get(0).name);
                }
            }

            if (t == null) {
                p.getService().send_box_ThongBao_OK("Bạn chưa trồng cây nào!");
                return true;
            }

            if (index == 0) { // Tưới nước
                if (t.timeTuoiNuoc > System.currentTimeMillis()) {
                    p.getService().send_box_ThongBao_OK("Cây đã đủ nước");
                } else {
                    if (p.item.total_item_bag_by_id(4, 389) > 0) {
                        t.timeTuoiNuoc = System.currentTimeMillis() + 300_000L;
                        t.timeThuHoach -= 300_000L;
                        p.item.remove_item47(4, 389, 1);
                        p.item.updateInventory(false);
                        p.getService().send_box_ThongBao_OK("Tưới nước thành công, thời gian thu hoạch giảm 5 phút");
                    } else {
                        p.getService().send_box_ThongBao_OK("Không đủ 1 bình nước");
                    }
                }
            } else if (index == 1) { // Bón phân
                if (t.timeBonPhan > System.currentTimeMillis()) {
                    p.getService().send_box_ThongBao_OK("Cây đã đủ phân bón");
                } else {
                    if (p.item.total_item_bag_by_id(4, 390) > 0) {
                        t.timeBonPhan = System.currentTimeMillis() + 300_000L;
                        t.timeThuHoach -= 300_000L;
                        p.item.remove_item47(4, 390, 1);
                        p.item.updateInventory(false);
                        p.getService().send_box_ThongBao_OK("Bón phân thành công, thời gian thu hoạch giảm 5 phút");
                    } else {
                        p.getService().send_box_ThongBao_OK("Không đủ 1 phân bón");
                    }
                }
            } else if (index == 2) { // Thu hoạch
                if (t.timeThuHoach < System.currentTimeMillis()) {
                    t.map.remove_obj(t.index, 2);
                    remove(t);
                    List<GiftBox> listGift = new ArrayList<>();
                    if (t.type == 1) {
                        for (int i = 0; i < 5; i++) {
                            GiftBox.addGift(listGift, 4, core.ZUtil.random(391, 396), 1);
                        }
                        EventData temp_select = null;
                        for (int i2 = 0; i2 < p.eventData.size(); i2++) {
                            if (p.eventData.get(i2).eventID == 9) {
                                temp_select = p.eventData.get(i2);
                                break;
                            }
                        }
                        if (temp_select != null) {
                            temp_select.data[SHOP_SIZE]++;
                        }
                        p.update_pointEvent1(1);
                    } else if (t.type == 0) {
                        for (int i = 0; i < 2; i++) {
                            GiftBox.addGift(listGift, 4, core.ZUtil.random(391, 396), 1);
                        }
                        p.update_pointEvent1(1);
                    } else if (t.type == 2) {
                        GiftBox.addGift(listGift, 4, 396, 1);
                        p.update_pointEvent1(5);
                    }
                    if (!listGift.isEmpty()) {
                        core.RewardService.sendGiftOrMail(p, 1, "Trồng Cây", "Thu hoạch thành công!", listGift, true);
                        // [FIX BUG-3] Kiểm tra p.clan != null trước khi thu hoạch cây bang
                        if (t.type == 2 && p.clan != null && p.clan.members != null) {
                            List<Player> listPRd = new ArrayList<>();
                            listPRd.add(p);
                            for (int i = 0; i < p.clan.members.size(); i++) {
                                Player p0 = map.Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                                if (p0 != null && !p0.equals(p)) {
                                    listPRd.add(p0);
                                    core.RewardService.sendGiftOrMail(p0, 1, "Trồng Cây Bang", "Nhận được từ thu hoạch cây bang!", listGift, true);
                                }
                            }
                            if (!listPRd.isEmpty()) {
                                Player pRd = listPRd.get(core.ZUtil.random(listPRd.size()));
                                List<GiftBox> luckyGifts = new ArrayList<>();
                                GiftBox.addGift(luckyGifts, 4, 396, 4);
                                core.RewardService.sendGiftOrMail(pRd, 1, "May Mắn Trồng Cây", "May mắn nhận thêm 4 giỏ trái cây!", luckyGifts, true);

                                clan.ClanChat chat = new clan.ClanChat();
                                chat.idMem = p.clan.members.get(0).id;
                                chat.name = p.name;
                                chat.str = "Trồng cây: " + pRd.name + " may mắn nhận đc 5 giỏ trái cây";
                                chat.time = System.currentTimeMillis();
                                chat.typeChat = -3;
                                p.clan.add_chat(chat);
                                p.clan.send_chat(chat, null);
                            }
                        }
                    }
                } else {
                    network.Message m = new network.Message(-69);
                    String state = (t.timeBonPhan < System.currentTimeMillis()) ? "thiếu phân bón"
                            : (t.timeTuoiNuoc < System.currentTimeMillis()) ? "thiếu nước" : "ổn định";
                    m.writer().writeUTF(String.format("Cây của bạn đang trong trạng thái %s.\nHiện thời gian thu hoạch còn lại:", state));
                    m.writer().writeShort((int) ((t.timeThuHoach - System.currentTimeMillis()) / 1_000));
                    p.addmsg(m);
                    m.cleanup();
                }
            } else if (index == 3) { // Trạng thái cây
                if (t.timeThuHoach > System.currentTimeMillis()) {
                    network.Message m = new network.Message(-69);
                    String state = (t.timeBonPhan < System.currentTimeMillis()) ? "thiếu phân bón"
                            : (t.timeTuoiNuoc < System.currentTimeMillis()) ? "thiếu nước" : "ổn định";
                    m.writer().writeUTF(String.format("Cây của bạn đang trong trạng thái %s.\nHiện thời gian thu hoạch còn lại:", state));
                    m.writer().writeShort((int) ((t.timeThuHoach - System.currentTimeMillis()) / 1_000));
                    p.addmsg(m);
                    m.cleanup();
                } else {
                    p.getService().send_box_ThongBao_OK("Cây của bạn đã đến thời gian thu hoạch!");
                }
            }
            return true;
        }
        return false;
    }

    // ======================== HELPER: addGioTraiCay ========================

    /**
     * Tăng chỉ số giỏ trái cây trong EventData của player (dùng ở Zone.java, v.v.)
     * @param p    player
     * @param idx  IDX_GIO_TRAI_CAY hoặc các IDX_* khác
     * @param max  giới hạn tối đa
     */
    public static void addEventPoint(Player p, int idx, int max) {
        EventData data = p.getDataEvent(9);
        if (data != null && data.data[idx] < max) {
            data.data[idx]++;
        }
    }

    @Override
    public void onMobKilled(Player p, mob.Mob mob) {
        if (!event.EventManager.isActive(this.id) || mob.is_boss) return;
        if (!isDropItemActive()) return;
        
        // 1. Drop Seed (387) — giới hạn bởi IDX_HAT_GIONG (max 10)
        int seedChance = (mob.map != null && mob.map.map_dungeon != null) ? 4 : 2;
        if (core.ZUtil.random(150) < seedChance) {
            EventData temp_select = getOrCreateEventData(p);
            if (temp_select != null && temp_select.data[IDX_HAT_GIONG] < 10) {
                temp_select.data[IDX_HAT_GIONG]++;
                p.item.add_item_bag47(4, ITEM_HAT_GIONG, 1);
                p.item.updateInventory(false);
                historys.zLog.gI().add_log(p, "nhặt 1 hạt giống lúc đánh quái");
            }
            // Cũng cộng vào điểm giỏ trái cây nếu chưa đầy hạn mức IDX_GIO_QUA_DANH
            if (temp_select != null && temp_select.data[IDX_GIO_QUA_DANH] < 20) {
                temp_select.data[IDX_GIO_QUA_DANH]++;
                temp_select.data[IDX_GIO_TRAI_CAY]++;
            }
        }
        
        // 2. Drop Fertilizer & Water (389, 390)
        int fertilChance = (mob.map != null && mob.map.map_dungeon != null) ? 40 : 20;
        if (core.ZUtil.random(150) < fertilChance) {
            EventData temp_select = getOrCreateEventData(p);
            if (!p.isBot && !p.isDe && !p.isdie && temp_select != null && temp_select.data[IDX_GIO_QUA_DANH] < 20) {
                p.item.add_item_bag47(4, ITEM_PHAN, 1);
                p.item.add_item_bag47(4, ITEM_NUOC, 1);
                p.item.updateInventory(false);
                historys.zLog.gI().add_log(p, "nhặt phân bón và nước lúc đánh quái");
                temp_select.data[IDX_GIO_QUA_DANH]++;
                temp_select.data[IDX_GIO_TRAI_CAY]++;
            }
        }
    }

    @Override
    protected boolean onBuyItem(model.Player p, itemz.MainItem item, event.EventData data) throws java.io.IOException {
        if (item.id == -10 || item.id == -11) {
            p.item.remove_item47(4, ITEM_GIO_TRAI_CAY, item.price);
            data.data[item.indexShop]--;
            java.util.List<template.GiftBox> listGift = new java.util.ArrayList<>();
            int actualId = 0;
            if (item.id == -10) {
                actualId = ((p.level < 10 ? 10 : p.level) / 10) + 111;
            } else if (item.id == -11) {
                actualId = (p.level / 10) + 136;
            }
            template.GiftBox.addGift(listGift, 4, actualId, 1);
            
            if (!listGift.isEmpty()) {
                network.Service.send_gift(p, 1, "Trồng cây", "Nhận được", listGift, true);
            }
            openShop(p);
            return true;
        }
        return false;
    }

    @Override
    public boolean onUseItem(Player p, int id) throws IOException {
        if (!isEventActive()) {
            if (id == ITEM_HAT_GIONG || id == 388 || id == ITEM_PHAN || id == ITEM_NUOC || (id >= 391 && id <= 396)) {
                p.getService().send_box_ThongBao_OK("Sự kiện Trồng Cây đã kết thúc!");
                return false;
            }
            return false;
        }

        if (id == ITEM_HAT_GIONG || id == 388) {
            if (p.map.template.id > 48 || p.map.zone_id > 4 || p.map.list_mob.length < 5 || p.map.isMapSea()) {
                p.getService().send_box_ThongBao_OK("Vị trí không phù hợp. Bạn chỉ có thể gieo hạt giống bên ngoài các Làng để trồng Cây kỳ bí (Từ Làng Cối Xoay Gió đến Thị Trấn Khởi Đầu) ở khu từ 1-5");
                return false;
            }
            if (p.item.total_item_bag_by_id(4, id) < 1) {
                p.getService().send_box_ThongBao_OK("Bạn không có hạt giống!");
                return false;
            }
            Tree t = getByName(p.name);
            if (t != null) {
                p.getService().send_box_ThongBao_OK("Hiện tại bạn đang trồng 1 cây rồi. Vị trí\n" + t.map.template.name + " khu " + (t.map.zone_id + 1));
                return true;
            }
            Tree myCay = new Tree();
            myCay.index = getIndex(p.IDPlayer);
            if (myCay.index != -1) {
                p.item.remove_item47(4, id, 1);
                p.item.updateInventory(false);
                myCay.setup(p);
                setByName(p.name, myCay);

                Message m_local = new Message(1);
                m_local.writer().writeByte(2);
                m_local.writer().writeShort(myCay.index);
                m_local.writer().writeShort(myCay.x);
                m_local.writer().writeShort(myCay.y);
                p.map.send_msg_all_p(m_local, null, true);
                m_local.cleanup();
                p.getService().send_box_ThongBao_OK("Gieo hạt thành công. Sau 10 phút có thể thu hoạch, nhớ chăm sóc đều đặn để tránh chết cây.");
                if (id == 388) {
                    myCay.type = 1;
                    core.Manager.gI().chatKTG(1, p.name + " đã đặt nhẹ Hạt giống may mắn xuống đất tại " + myCay.map.template.name + " khu " + (myCay.map.zone_id + 1), 0);
                } else {
                    myCay.type = 0;
                    core.Manager.gI().chatKTG(1, p.name + " đã đặt nhẹ Hạt giống thường xuống đất tại " + myCay.map.template.name + " khu " + (myCay.map.zone_id + 1), 0);
                }
                p.map.change_flag(p, -1);
                return true;
            }
            return true;
        } else if (id >= 391 && id <= 396) {
            if (p.item.total_item_bag_by_id(4, id) < 1) return false;
            
            long expEarn = (long) p.level * 3000L;
            p.update_exp(expEarn, false);
            p.update_vang(100_000);
            p.item.updateInventory(false);
            p.updateMoney();
            List<template.GiftBox> listGift = new ArrayList<>();
            listGift.add(new template.GiftBox(4, 0, 100_000));
            listGift.add(new template.GiftBox(99, 0, (int) expEarn));
            core.RewardService.sendGiftOrMail(p, 1, "Thưởng thức Trái Cây", "Trái Cây thơm ngon", listGift, true);
            return false;
        }
        return false;
    }

    // [FIX BUG-14] Override clearNpc() để xóa toàn bộ cây trong ENTRY khi event deactivate
    @Override
    public void clearNpc() {
        synchronized (ENTRY) {
            java.util.List<Tree> trees = new java.util.ArrayList<>(ENTRY.values());
            for (Tree t : trees) {
                try {
                    if (t.map != null) {
                        t.map.remove_obj(t.index, 2);
                    }
                } catch (Exception e) { /* ignore */ }
            }
            ENTRY.clear();
        }
        System.out.println("[SuKienTrongCay] clearNpc: đã xóa toàn bộ cây sự kiện.");
    }
}