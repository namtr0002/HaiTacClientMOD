package event;

import model.Player;
import core.Manager;
import core.ZUtil;
import itemz.MainItem;
import itemz.rebuilds.GhepVeTuoiTho;
import mob.Mob;
import template.MobTemplate;
import map.Zone;
import network.Message;
import template.DataTemplate;
import template.GiftBox;
import template.ItemTemplate4;
import model.YesNoDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SuKienHe — SỰ KIỆN HÈ 2026 (Săn Bắt Pokemon & Ghép Vé Về Tuổi Thơ)
 *
 * NPC Sự Kiện: Chị Hằng (tại các Làng khởi đầu)
 *
 * 1. Bắt Pokemon:
 *   - Dùng Bóng Mồi (#191) hoặc Pokemon xuất hiện tự nhiên trên các bản đồ.
 *   - Các loại Pokemon: Lửa (115), Nước (116), Đá (117), Điện (118), Cỏ (119).
 *   - Bóng Bắt Pokemon:
 *     + Quả cầu pokemon thường (#180) - Tỷ lệ 5/120 (~4.2%)
 *     + Quả cầu pokemon hiếm (#181) - Tỷ lệ 10/120 (~8.3%)
 *     + Quả cầu pokemon đặc biệt (#182) - Tỷ lệ 25/120 (~20.8%)
 *   - Thu phục nhận Huy hiệu tương ứng:
 *     + Pokemon Lửa (115) -> Huy hiệu Lửa (#184)
 *     + Pokemon Nước (116) -> Huy hiệu Nước (#185)
 *     + Pokemon Đá (117) -> Huy hiệu Đá (#186)
 *     + Pokemon Điện (118) -> Huy hiệu Điện (#187)
 *     + Pokemon Cỏ (119) -> Huy hiệu Cỏ (#183)
 *     + +1 Điểm sự kiện 1
 *
 * 2. Ghép Vé Về Tuổi Thơ:
 *   - 5 Huy hiệu khác nhau (Cỏ, Lửa, Nước, Đá, Điện) + 35.000 Beri + 25 Ruby + 20.000 Extol -> 1 Vé Về Tuổi Thơ (#192)
 *   - +1 Điểm sự kiện 2
 *
 * 3. Shop Đổi Quà Sự Kiện Hè:
 *   - Dùng Vé Về Tuổi Thơ (#192) để đổi:
 *     + Rương đại ác quỷ (#158)
 *     + Thời trang hè 1-4 (#755 - #758, yêu cầu Level >= 30)
 *     + Tinh thể đá (#135)
 *     + Rương Cam Cùng Hệ Lv10 (#122)
 *     + Rương Dial (#455)
 *     + Hộp trang phục Raid Suit (#589)
 *
 * 4. Dùng Item Sự Kiện:
 *   - Radar tìm pokemon (#189): Xem vị trí các Pokemon đang xuất hiện trên map.
 *   - Bộ vật phẩm pokemon (#190): Mở nhận Radar (#189), 5 Bóng đặc biệt (#182), 1 Huy hiệu đặc biệt (#188).
 *   - Vé triệu hồi Pokemon (#191): Triệu hồi 1 Pokemon ngay tại map hiện tại.
 *   - Vé về tuổi thơ (#192): Mở quà ngẫu nhiên nhận EXP, Beri, Rương đại ác quỷ, Rương trang bị cam...
 *   - Huy hiệu đặc biệt (#188): Mở quà nhận Ruby, EXP, Rương ác quỷ.
 */
public class SuKienHe extends Event {

    public static final int ID = Event.ID_SUKIEN_HE_2026;

    // Items Bán Trong Shop Sự Kiện
    private static final short[] SELL_ITEMS = {190, 191, 182, 359};

    // Template ID của Pokemon (115-119)
    public static final int[] POKEMON_MOB_IDS = {115, 116, 117, 118, 119};

    // [mob_id, reward_item4_id] khi bắt thành công
    private static final int[][] CATCH_REWARD = {
        {115, 184}, // Lửa -> Huy hiệu Lửa
        {116, 185}, // Nước -> Huy hiệu Nước
        {117, 186}, // Đá -> Huy hiệu Đá
        {118, 187}, // Điện -> Huy hiệu Điện
        {119, 183}  // Cỏ -> Huy hiệu Cỏ
    };

    // [ballItemId, tỷ lệ % trên 120]
    private static final int[][] POKEBALL = {
        {180,  5}, // Bóng thường
        {181, 10}, // Bóng hiếm
        {182, 25}  // Bóng đặc biệt
    };

    private static final int POKEMON_HP    = 1000;
    private static final int POKEMON_LEVEL = 50;
    private static final int POKEMON_MAX   = 25;

    // Tên hiển thị của từng loại (index = mob_id - 115)
    public static final String[] POKEMON_NAMES = {
        "Pokemon lửa", "Pokemon nước", "Pokemon đá", "Pokemon điện", "Pokemon cỏ"
    };

    // Danh sách Pokemon đang xuất hiện trên map
    public static final List<Mob> ENTRY = Collections.synchronizedList(new ArrayList<>());

    // Indexes EventData
    public static final int IDX_DAILY_DROP_BALL = 10;
    public static final int IDX_DAILY_VAN_BUON  = 11;
    public static final int IDX_DAILY_NV_LAP   = 12;
    public static final int IDX_DAILY_TRUY_NA   = 13;
    public static final int IDX_DAILY_VUON_CAM  = 14;
    public static final int IDX_DAILY_LIEN_TANG = 15;

    private static SuKienHe instance;

    public static SuKienHe gI() {
        if (instance == null) {
            instance = new SuKienHe();
        }
        return instance;
    }

    public SuKienHe() {
        super(ID, "Sự Kiện Hè");
        this.shopName     = "Shop Sự Kiện Hè";
        this.costItemId   = 192;   // Vé Về Tuổi Thơ
        this.costItemType = 4;
        this.sellableItems = SELL_ITEMS;

        this.time = "0:00:00 10/7/2026 > 23:59:59 20/7/2026";
        this.timex2pay = "0:00:00 10/7/2026 > 23:59:59 24/7/2026";
        this.timechangeitem = "0:00:00 10/7/2026 > 23:59:59 22/7/2026";
        this.timedropitem = "0:00:00 10/7/2026 > 23:59:59 21/7/2026";
        this.timeremoveitem = "0:00:00 10/7/2026 > 23:59:59 23/7/2026";

        this.bxhSubTypes = new int[]{101, 102};
        this.bxhNames = new String[]{"Top Bắt Pokemon", "Top Vé Tuổi Thơ"};

        initShop();
        instance = this;
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienHe(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 196, 198,
            527, 528, 529, 530, 531, 532, 533, 534, 535, 867, 868
        };
    }

    // ======================== LIFECYCLE ========================

    @Override
    public void init() throws Exception {
        initShop();
        ENTRY.clear();
        System.out.println("[SuKienHe] Init completed. Max Pokemon: " + POKEMON_MAX);
    }

    @Override
    public void update(int hour, int min, int sec) throws Exception {
        if (sec == 0) {
            pokemonSpawnAuto(); // Mỗi phút kiểm tra sinh thêm Pokemon
        }
    }

    // ======================== SHOP HOOKS ========================

    @Override
    protected boolean canBuyExtra(Player p, MainItem item) throws IOException {
        // Trang phục hè (index 1-4) yêu cầu level >= 30
        if (item.indexShop >= 1 && item.indexShop <= 4 && p.level < 30) {
            p.getService().send_box_ThongBao_OK("Cần level 30 trở lên để mua trang phục sự kiện!");
            return false;
        }
        return true;
    }

    // ======================== MENU NPC ========================

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!EventManager.isActive(ID)) return false;
        if (npcId == -1020 || npcId == ID) {
            p.getService().openDynamicMenu(npcId, "Sự Kiện Hè", new String[]{
                "Sự Kiện Hè",
                "Báo Danh",
                "Vòng Quay",
                "Cửa Hàng Coin",
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
        if (!EventManager.isActive(ID)) return false;
        switch (menuId) {
            case -1020:
            case ID: {
                switch (index) {
                    case 0: sendMainMenu(p); break;
                    case 1: sendDiemDanhMenu(p); break;
                    case 2: openVongQuay(p); break;
                    case 3: sendCoinShopMenu(p); break;
                    case 4: {
                        String txt = "SỰ KIỆN HÈ 2026 \n\n"
                                + "- Dùng Bóng Mồi (#191) hoặc tìm Pokemon xuất hiện tự nhiên trên các bản đồ.\n"
                                + "- Dùng Bóng Bắt Pokemon để thu phục nhận các loại Huy hiệu (Cỏ, Lửa, Nước, Đá, Điện).\n"
                                + "- Ghép 5 Huy hiệu khác nhau thành Vé Về Tuổi Thơ (#192).\n"
                                + "- Dùng Vé Tuổi Thơ đổi Trang Phục Hè và các vật phẩm quý hiếm tại Shop Đổi Quà!";
                        p.getService().Help_From_Server(menuId, txt);
                        break;
                    }
                    case 5: showTichNap(p); break;
                    case 6: showTichTieu(p); break;
                }
                return true;
            }
            case 975: handleMenu975(p, index); return true;
            case 974: handleMenu974(p, index); return true;
            case 973: handleMenu973(p, index); return true;
            case -2: sendDiemDanhMenu(p); return true;
            case -3: openVongQuay(p); return true;
            case -4: sendCoinShopMenu(p); return true;
        }
        return false;
    }

    private void sendMainMenu(Player p) throws IOException {
        p.getService().openDynamicMenu(975, "Sự Kiện Hè", new String[]{
            "Shop Đổi Quà", "Ghép Huy Hiệu", "BXH Bắt Thú", "BXH Ghép Vé", "Hướng dẫn"
        }, null);
    }

    private void handleMenu975(Player p, int index) throws IOException {
        switch (index) {
            case 0:
                openShop(p);
                break;
            case 1:
                new GhepVeTuoiTho().show_table(p);
                break;
            case 2:
                p.typeBXH = 101;
                showEventRank(p);
                break;
            case 3:
                p.typeBXH = 102;
                showEventRank(p);
                break;
            case 4: {
                String txt = "SỰ KIỆN HÈ 2026 \n\n"
                        + "- Bắt Pokemon: Dùng Bóng Mồi gọi Pokemon hoặc tìm chúng trên các bản đồ.\n"
                        + "- Thu phục: Dùng Bóng Bắt Pokemon nhận Huy hiệu Cỏ, Lửa, Nước, Đá, Điện.\n"
                        + "- Ghép Vé: Ghép 5 Huy hiệu khác nhau thành Vé Về Tuổi Thơ.\n"
                        + "- Đổi Quà: Dùng Vé Về Tuổi Thơ đổi Cải Trang Hè và vật phẩm quý hiếm!";
                p.getService().Help_From_Server(975, txt);
                break;
            }
        }
    }

    public void handleMenu974(Player p, int index) throws IOException {
        if (index < 0 || index >= POKEMON_NAMES.length) return;
        int targetId = POKEMON_MOB_IDS[index];
        List<Mob> matchingMobs = new ArrayList<>();
        List<String> locs = new ArrayList<>();
        synchronized (ENTRY) {
            for (Mob mob : ENTRY) {
                if (mob != null && mob.mtemplate != null && mob.mtemplate.mob_id == targetId && !mob.isdie && mob.map != null && mob.map.template != null) {
                    matchingMobs.add(mob);
                    locs.add(mob.map.template.name + " (Khu " + (mob.map.zone_id + 1) + ")");
                }
            }
        }
        if (locs.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Hiện chưa có " + POKEMON_NAMES[index] + " nào xuất hiện.");
            return;
        }
        p.tempPokemonTargets = matchingMobs;
        p.getService().openDynamicMenu(973, POKEMON_NAMES[index], locs.toArray(new String[0]), null);
    }

    public void handleMenu973(Player p, int index) throws IOException {
        List<Mob> matchingMobs = p.tempPokemonTargets;
        if (matchingMobs == null || index < 0 || index >= matchingMobs.size()) {
            p.getService().send_box_ThongBao_OK("Vị trí Pokemon không tồn tại hoặc đã kết thúc!");
            return;
        }
        Mob targetMob = matchingMobs.get(index);
        if (targetMob == null || targetMob.isdie || targetMob.map == null || targetMob.map.template == null) {
            p.getService().send_box_ThongBao_OK("Pokemon này đã bị thu phục hoặc biến mất!");
            return;
        }
        String mapName = targetMob.map.template.name;
        int zoneNum = targetMob.map.zone_id + 1;
        String mobName = (targetMob.mtemplate != null) ? targetMob.mtemplate.name : "Pokemon";

        p.setyesNoDialog(new YesNoDialog(p, 973, "Dịch chuyển",
                "Bạn có muốn dịch chuyển đến " + mapName + " (Khu " + zoneNum + ") để săn " + mobName + " với chi phí 5 Ruby không?",
                new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1},
                (val) -> {
                    if (val == 0) { // Đồng ý
                        if (p.get_ngoc() < 5) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ 5 Ruby để dịch chuyển!");
                            return;
                        }
                        if (targetMob.isdie || targetMob.map == null) {
                            p.getService().send_box_ThongBao_OK("Pokemon này đã bị thu phục hoặc biến mất!");
                            return;
                        }
                        p.update_ngoc(-5);
                        p.updateMoney();

                        Message m = new Message(30);
                        p.addmsg(m);
                        m.cleanup();
                        if (p.map != null) {
                            p.map.leave_map(p, 2);
                        }
                        p.map = targetMob.map;
                        p.x = targetMob.x;
                        p.y = targetMob.y;
                        p.xold = p.x;
                        p.yold = p.y;
                        p.lastValidX = p.x;
                        p.lastValidY = p.y;
                        p.map.enter_map(p);
                        p.map.enter_zone(p);
                        p.getService().send_box_ThongBao_OK("Đã dịch chuyển đến vị trí của " + mobName + "!");
                    }
                }));
        p.getService().startYesNo();
    }

    private void sendDiemDanhMenu(Player p) throws IOException {
        model.DiemDanhEvent.gI().sendDiemDanhMenu(p);
    }

    private void openVongQuay(Player p) throws IOException {
        p.typeVongQuay = 5;
        p.currentVongQuay = model.VongQuayHLW.gI();
        p.currentVongQuay.showTable(p);
    }

    private void sendCoinShopMenu(Player p) throws IOException {
        p.getService().openDynamicMenu(-969, "Cửa Hàng Coin", new String[]{"Vào cửa hàng"}, null);
    }

    // ======================== USE ITEM ========================

    @Override
    public boolean onUseItem(Player p, int itemId) throws IOException {
        if (!isEventActive()) {
            if (itemId >= 180 && itemId <= 192) {
                p.getService().send_box_ThongBao_OK("Sự kiện Hè đã kết thúc!");
                return false;
            }
            return false;
        }

        // 1. Radar Tìm Pokemon (#189)
        if (itemId == 189) {
            p.getService().openDynamicMenu(974, "Radar Tìm Pokemon", new String[]{
                "Pokemon lửa", "Pokemon nước", "Pokemon đá", "Pokemon điện", "Pokemon cỏ"
            }, null);
            return true;
        }

        // 2. Bộ vật phẩm Pokemon (#190)
        if (itemId == 190) {
            if (p.item.total_item_bag_by_id(4, itemId) < 1) return false;
            p.item.remove_item47(4, itemId, 1);
            p.item.updateInventory(false);

            List<MainItem> giftList = new ArrayList<>();
            giftList.add(new MainItem(189, 4, 1));
            giftList.add(new MainItem(182, 4, 5));
            giftList.add(new MainItem(188, 4, 1));
            MainItem.showGiftBox(p, "Bộ Vật Phẩm Pokemon", "Bạn đã mở Bộ Vật Phẩm Pokemon thành công!", giftList, true, true);
            return true;
        }

        // 3. Vé triệu hồi Pokemon (#191)
        if (itemId == 191) {
            return spawnPokemon(p);
        }

        // 4. Vé về tuổi thơ (#192)
        if (itemId == 192) {
            if (p.item.total_item_bag_by_id(4, itemId) < 1) return false;
            p.item.remove_item47(4, itemId, 1);

            long expEarn = (long) p.level * 5000L;
            int beriEarn = ZUtil.random(50000, 300000);

            List<template.GiftBox> giftList = new ArrayList<>();
            giftList.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, expEarn)));
            giftList.add(new template.GiftBox(4, 0, beriEarn));

            int roll = ZUtil.random(100);
            if (roll < 5) { // 5% Rương đại ác quỷ
                giftList.add(new template.GiftBox(4, 158, 1));
                Manager.gI().chatKTG(0, "Chúc mừng người chơi [" + p.name + "] đã mở Vé Về Tuổi Thơ nhận được [Rương Đại Ác Quỷ]!", 0);
            } else if (roll < 25) { // 20% Rương đồ cam cùng hệ theo level
                int chestId = (p.level < 20) ? 122 : Math.min(131, 122 + (p.level / 10 - 1));
                giftList.add(new template.GiftBox(4, chestId, 1));
            } else if (roll < 55) { // 30% Bột cường hóa (1-5)
                int qty = ZUtil.random(1, 5);
                giftList.add(new template.GiftBox(7, 1, qty));
            } else if (roll < 80) { // 25% Túi Beri / Đá mài
                giftList.add(new template.GiftBox(4, 135, 1));
            } else { // 20% Đá khảm ngẫu nhiên
                int[] daIds = {44, 50, 56, 62, 68, 74};
                int daId = daIds[ZUtil.random(daIds.length)];
                giftList.add(new template.GiftBox(4, daId, 1));
            }

            p.item.updateInventory(false);
            core.RewardService.sendGiftOrMail(p, 1, "Mở Vé Về Tuổi Thơ", "Phần thưởng", giftList, true);
            return true;
        }

        // 5. Huy hiệu đặc biệt (#188)
        if (itemId == 188) {
            if (p.item.total_item_bag_by_id(4, itemId) < 1) return false;
            p.item.remove_item47(4, itemId, 1);

            int ruby = ZUtil.random(5, 20);
            long exp = (long) p.level * 8000L;

            List<template.GiftBox> giftList = new ArrayList<>();
            giftList.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, exp)));
            giftList.add(new template.GiftBox(4, 1, ruby));

            if (ZUtil.random(100) < 15) { // 15% Rương Ác Quỷ
                giftList.add(new template.GiftBox(4, 29, 1));
            }

            p.item.updateInventory(false);
            core.RewardService.sendGiftOrMail(p, 1, "Huy Hiệu Đặc Biệt", "Phần thưởng", giftList, true);
            return true;
        }

        // 6. Huy hiệu các hệ (#183 - #187) -> Mở nhanh bảng Ghép Vé Về Tuổi Thơ
        if (itemId >= 183 && itemId <= 187) {
            new GhepVeTuoiTho().show_table(p);
            return true;
        }

        // 7. Bóng bắt Pokemon (#180, #181, #182)
        if (itemId == 180 || itemId == 181 || itemId == 182) {
            return true;
        }

        return false;
    }

    // ======================== BẮT POKEMON ========================

    public void catchPokemon(Player p, short ballItemId, short mobIndex) throws IOException {
        if (!EventManager.isActive(ID)) return;
        if (p == null || p.item == null || p.map == null) return;

        if (p.item.total_item_bag_by_id(4, ballItemId) <= 0) {
            p.getService().send_box_ThongBao_OK("Không đủ bóng Pokemon trong hành trang!");
            return;
        }

        Mob target = findEntry(mobIndex);
        if (target == null || target.isdie || target.map == null || !target.map.equals(p.map)) {
            p.getService().send_box_ThongBao_OK("Pokemon không tồn tại hoặc đã biến mất!");
            return;
        }

        int percent = getCatchChance(ballItemId);
        boolean suc = ZUtil.random(120) < percent;

        // Gửi animation ném bóng
        Message m = new Message(-15);
        m.writer().writeByte(suc ? 12 : 13);
        m.writer().writeShort(p.index_map);
        m.writer().writeByte(0);
        m.writer().writeShort(1000);
        m.writer().writeShort(mobIndex);
        m.writer().writeByte(1);
        m.writer().writeShort(ballItemId);
        p.map.send_msg_all_p(m, p, true);
        m.cleanup();

        if (suc) {
            target.isdie = true;
            int reward = getCatchReward(target.mtemplate.mob_id);
            p.update_pointEvent1(1);
            p.map.remove_obj(mobIndex, 1);
            ENTRY.remove(target);

            if (reward > 0) {
                List<template.GiftBox> listGift = new ArrayList<>();
                listGift.add(new template.GiftBox(4, reward, 1));
                core.RewardService.sendGiftOrMail(p, 1, "Thu Phục Pokemon", "Chúc mừng bạn đã thu phục thành công " + target.mtemplate.name + "!", listGift, true);
            }
        }

        // Cập nhật bóng
        if (p.item.total_item_bag_by_id(4, ballItemId) == 1) {
            updateBallIcon(p, ballItemId);
        }
        p.item.remove_item47(4, ballItemId, 1);
        p.item.updateInventory(false);
    }

    // ======================== HOOK: MOB KILLED ========================

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!EventManager.isActive(ID) || mob == null || mob.is_boss) return;
        if (!isDropItemActive()) return;

        p = p.getOwnerPlayer();
        if (p.isBot || p.item == null) return;

        if (Math.abs(p.level - mob.level) <= 10) {
            EventData evData = getOrCreateEventData(p);
            if (evData != null && evData.data[IDX_DAILY_DROP_BALL] < 100) {
                if (ZUtil.random(100) < 15) { // 15% rơi bóng
                    evData.data[IDX_DAILY_DROP_BALL]++;
                    int ballId = (ZUtil.random(100) < 25) ? 181 : 180; // 25% bóng hiếm, 75% bóng thường
                    p.item.add_item_bag47(4, ballId, 1);
                    p.item.updateInventory(false);
                }
            }
        }
    }

    @Override
    public void onVanChuyen(Player p) {
        if (!EventManager.isActive(ID) || !isDropItemActive()) return;
        giveActivityBall(p, 181, 3, IDX_DAILY_VAN_BUON, 5, "Vận Buôn", "Quả Cầu Pokemon Hiếm");
    }

    @Override
    public void onNhiemVuLap(Player p) {
        if (!EventManager.isActive(ID) || !isDropItemActive()) return;
        giveActivityBall(p, 181, 3, IDX_DAILY_NV_LAP, 5, "Nhiệm Vụ Lặp", "Quả Cầu Pokemon Hiếm");
    }

    @Override
    public void onWanted(Player p) {
        if (!EventManager.isActive(ID) || !isDropItemActive()) return;
        giveActivityBall(p, 182, 1, IDX_DAILY_TRUY_NA, 5, "Truy Nã", "Quả Cầu Pokemon Đặc Biệt");
    }

    @Override
    public void onVuonCam(Player p, int round) {
        if (!EventManager.isActive(ID) || !isDropItemActive()) return;
        if (round >= 10) {
            giveActivityBall(p, 191, 1, IDX_DAILY_VUON_CAM, 3, "PB Vườn Cam", "Vé Triệu Hồi Pokemon");
        }
    }

    @Override
    public void onLienTang(Player p, int floor) {
        if (!EventManager.isActive(ID) || !isDropItemActive()) return;
        if (floor >= 5) {
            giveActivityBall(p, 191, 1, IDX_DAILY_LIEN_TANG, 3, "PB Liên Tầng", "Vé Triệu Hồi Pokemon");
        }
    }

    private void giveActivityBall(Player p, int itemId, int amount, int dataIndex, int maxTurns, String source, String itemName) {
        if (p == null || p.isBot || p.item == null) return;
        if (p.item.able_bag() < 1) return;

        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[dataIndex] < maxTurns) {
            evData.data[dataIndex]++;
            p.item.add_item_bag47(4, itemId, amount);
            p.item.updateInventory(false);
        }
    }

    // ======================== POKEMON SPAWN ========================

    private boolean spawnPokemon(Player p) throws IOException {
        if (p == null || p.map == null) return false;
        boolean isSea = isSea(p.map.template.id);
        if (p.map.list_mob == null || p.map.list_mob.length < 3 || isSea || p.map.template.id >= 111) {
            p.getService().send_box_ThongBao_OK("Khu vực này không thích hợp để triệu hồi Pokemon!");
            return false;
        }

        if (p.item.total_item_bag_by_id(4, 191) < 1) {
            p.getService().send_box_ThongBao_OK("Bạn không có Vé Triệu Hồi Pokemon!");
            return false;
        }

        p.item.remove_item47(4, 191, 1);
        p.item.updateInventory(false);

        addPokemonToMap(p.x, p.y, p.map);
        p.getService().send_box_ThongBao_OK("Triệu hồi Pokemon thành công! Hãy dùng bóng để bắt ngay!");
        return true;
    }

    private void pokemonSpawnAuto() throws IOException {
        if (!EventManager.isActive(ID) || ENTRY.size() >= POKEMON_MAX) return;
        Zone[] zones = pickValidZones();
        if (zones == null || zones.length == 0 || zones[0].list_mob == null || zones[0].list_mob.length == 0) return;

        Zone targetZone = zones[ZUtil.random(zones.length)];
        if (targetZone != null && targetZone.list_mob != null && targetZone.list_mob.length > 0) {
            Mob ref = targetZone.getMob(targetZone.list_mob[ZUtil.random(targetZone.list_mob.length)]);
            if (ref != null) {
                addPokemonToMap(ref.x, ref.y, targetZone);
            }
        }
    }

    private void addPokemonToMap(short x, short y, Zone zone) throws IOException {
        if (zone == null) return;
        int mobId = POKEMON_MOB_IDS[ZUtil.random(POKEMON_MOB_IDS.length)];
        MobTemplate mt = MobTemplate.get_mob_template(mobId);

        Mob mob = new Mob();
        mob.mtemplate = mt;
        mob.x = x;
        mob.y = y;
        mob.hp_max = POKEMON_HP;
        mob.hp = POKEMON_HP;
        mob.level = POKEMON_LEVEL;
        mob.isdie = false;
        mob.id_target = -1;
        mob.index = ENTRY.isEmpty() ? -10 : (ENTRY.get(ENTRY.size() - 1).index - 1);
        mob.map = zone;
        mob.boss_inf = null;

        ENTRY.add(mob);

        Message m = new Message(1);
        m.writer().writeByte(1);
        m.writer().writeShort(mob.index);
        m.writer().writeShort(mob.x);
        m.writer().writeShort(mob.y);
        zone.send_msg_all_p(m, null, true);
        m.cleanup();

        if (zone.getService() != null) {
            zone.getService().move(mob.index, mob.x, mob.y);
        }
    }

    private Zone[] pickValidZones() {
        for (int i = 0; i < 30; i++) {
            int mapCount = map.MapManager.getInstance().getMaps().size();
            if (mapCount <= 0) continue;
            map.Map m = map.MapManager.getInstance().getMap(ZUtil.random(mapCount));
            if (m != null && m.zones != null && m.zones.length >= 3) {
                Zone z0 = m.zones[0];
                if (z0 != null && z0.list_mob != null && z0.list_mob.length >= 3 && z0.template != null
                        && z0.template.id < 111 && !isSea(z0.template.id)) {
                    return m.zones;
                }
            }
        }
        return null;
    }

    private boolean isSea(int templateId) {
        if (DataTemplate.mSea != null) {
            for (int[] s : DataTemplate.mSea) {
                if (s != null && s.length > 1 && s[1] == templateId) return true;
            }
        }
        return false;
    }

    // ======================== MAP SYNC ========================

    @Override
    public void sendMobsToPlayer(Player p, Zone zone) throws IOException {
        if (!EventManager.isActive(ID) || p == null || zone == null) return;
        synchronized (ENTRY) {
            for (Mob mob : ENTRY) {
                if (mob == null || mob.isdie || mob.map == null || !mob.map.equals(zone)) continue;
                Message m = new Message(1);
                m.writer().writeByte(1);
                m.writer().writeShort(mob.index);
                m.writer().writeShort(mob.x);
                m.writer().writeShort(mob.y);
                p.addmsg(m);
                m.cleanup();
            }
        }
    }

    @Override
    public Mob getMobInMap(Zone zone) {
        if (!EventManager.isActive(ID) || zone == null) return null;
        synchronized (ENTRY) {
            for (Mob mob : ENTRY) {
                if (mob != null && !mob.isdie && mob.map != null && mob.map.equals(zone)) {
                    return mob;
                }
            }
        }
        return null;
    }

    // ======================== HELPERS ========================

    private Mob findEntry(short index) {
        synchronized (ENTRY) {
            for (Mob m : ENTRY) {
                if (m != null && m.index == index) return m;
            }
        }
        return null;
    }

    private int getCatchChance(int itemId) {
        for (int[] b : POKEBALL) {
            if (b[0] == itemId) return b[1];
        }
        return 0;
    }

    private int getCatchReward(int mobId) {
        for (int[] r : CATCH_REWARD) {
            if (r[0] == mobId) return r[1];
        }
        return -1;
    }

    private void updateBallIcon(Player p, int itemId) throws IOException {
        Message m = new Message(-15);
        m.writer().writeByte(15);
        m.writer().writeByte(4);
        m.writer().writeShort(itemId);
        m.writer().writeByte(0);
        p.addmsg(m);
        m.cleanup();
    }
}
