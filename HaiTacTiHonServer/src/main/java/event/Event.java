package event;

import model.Player;
import core.Manager;
import core.ZUtil;
import itemz.MainItem;
import mob.Mob;
import map.Npc;
import map.MapTemplate;
import map.Zone;
import network.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import template.TichLuyEntry;

/**
 * Event — Abstract base class cho mọi sự kiện.
 *
 * ==============================================================
 *  CÁCH TẠO EVENT MỚI:
 * ==============================================================
 *
 *  public class SuKienMoi extends Event {
 *      public SuKienMoi() {
 *          super(ID, "Sự Kiện Mới");
 *          shopName     = "Cửa Hàng Sự Kiện Mới";
 *          pointName    = "Điểm Sự Kiện";
 *          costItemId   = 999;   // ID item dùng để đổi quà
 *          costItemType = 4;
 *      }
 *
 *      Override
 *      public void init() throws Exception {
 *          int idx = 0;
 *          shopItems = new ArrayList<>();
 *          shopItems.add(new MainItem(639, 4, 1, 200,  1, idx++));  // limit 1 lần
 *          shopItems.add(new MainItem(640, 4, 1, 500,  3, idx++));  // limit 3 lần
 *          shopItems.add(new MainItem( 10, 7, 1, 100, -1, idx++));  // không giới hạn
 *          // [NPC_MIGRATED] initNpc(); // NPC đã chuyển sang class iNpc riêng trong package zinterfaces.menus.npcs.event
 *      }
 *
 *      // Tùy chọn: điều kiện thêm khi mua
 *      Override
 *      protected boolean canBuyExtra(Player p, MainItem item) throws IOException {
 *          if (item.indexShop == 0 && p.level < 50) {
 *              p.getService().send_box_ThongBao_OK("Cần level 50!");
 *              return false;
 *          }
 *          return true;
 *      }
 *
 *      // Tùy chọn: override hoàn toàn 1 item (trả true = đã xử lý)
 *      Override
 *      protected boolean onBuyItem(Player p, MainItem item, EventData data) throws IOException {
 *          if (item.id == 999) { ... return true; }
 *          return false;
 *      }
 *
 *      // Tùy chọn: sau khi mua thành công
 *      Override
 *      protected void onAfterBuy(Player p, MainItem item) throws IOException {
 *          updateQuestProgress(p);
 *      }
 *  }
 *
 * ==============================================================
 *  CODE NGOÀI CHỈ GỌI QUA EventManager.dispatch*() — không import subclass.
 * ==============================================================
 */
public abstract class Event {

    // ======================== EVENT ID CONSTANTS ========================

    public static final int ID_SUKIEN_TETAMLICH_2026          = 2;
    public static final int ID_SUKIEN_HE_2026                 = 1;
    public static final int ID_SUKIEN_GIOTOHUNGVUONG_2026     = 3;
    public static final int ID_SUKIEN_TETDUONGLICH_2026       = 4;
    public static final int ID_SUKIEN_TETTHIEUNHI_2026        = 5;
    public static final int ID_SUKIEN_30THANG41THANG5_2026    = 6;
    public static final int ID_SUKIEN_HALLOWEEN_2025          = 7;
    public static final int ID_SUKIEN_TRUNGTHU_2025           = 8;
    public static final int ID_SUKIEN_TRONGCAY                 = 9;
    public static final int ID_SUKIEN_8THANG3_2026            = 10;
    public static final int ID_SUKIEN_20THANG10_2025          = 11;
    public static final int ID_SUKIEN_NOEL_2025               = 12;
    public static final int ID_SUKIEN_DAU_TRUONG_RUC_LUA_2026 = 13;
    public static final int ID_SUKIEN_20THANG11_REAL          = 14;
    public static final int ID_SUKIEN_VALENTINE               = 15;
    public static final int ID_SUKIEN_BIGMOM                  = 16;
    public static final int ID_SUKIEN_WANO_2026               = 17;
    public static final int ID_SUKIEN_VULAN_2026              = 18;
    public static final int ID_SUKIEN_THATTICH_2026           = 19;
    public static final int ID_SUKIEN_THATTICH                = 19;

    public static final int INDEX_HQMQ                        = 29;
    public static final int INDEX_VQT                         = 28;
    public static final int INDEX_VQV                         = 27;

    // ======================== FIELDS ========================

    public int    id;
    public String name;

    public String time = "";
    public String timeend = "";
    public String timex2pay = "";
    public String timechangeitem = "";
    public String timedropitem = "";
    public String timeremoveitem = "";
    public String season = "";

    /** Tên hiển thị trên UI shop */
    public String shopName    = "Cửa Hàng Sự Kiện";

    /** ID item dùng để đổi quà (currency). -1 = không dùng */
    public int    costItemId   = -1;

    /** Type của cost item (4=item, 7=nguyên liệu) */
    public int    costItemType = 4;

    /** SubType BXH của sự kiện (-1 = không có) */
    public int    bxhSubType = -1;

    /** Danh sách nhiều BXH con của sự kiện (nếu có) */
    public int[] bxhSubTypes = new int[0];
    public String[] bxhNames = new String[0];

    /**
     * Danh sách item trong shop — init trong init() của subclass.
     * Dùng MainItem(id, cat, num, price, limitNumBuy, indexShop).
     * indexShop = vị trí trong eventData.data[] để track limit.
     * limitNumBuy = -1 nghĩa là không giới hạn.
     */
    protected List<MainItem> shopItems = new ArrayList<>();
    /** Dedicated shop instance for this event */
    protected event.shop.EventShop eventShop;

    public event.shop.EventShop getEventShop() {
        return eventShop;
    }

    public void setEventShop(event.shop.EventShop eventShop) {
        this.eventShop = eventShop;
    }

    /**
     * Khởi tạo shop sự kiện.
     */
    public void initShop() {}

    /** Item xuất hiện trong shop bán thường khi event chạy */
    protected short[] sellableItems = new short[0];

    /**
     * Cấu hình NPC tự động spawn. FORMAT: {name, chat, mapId, npcId, x, y [,iconId]}
     */
    protected Object[][] npcConfigs = new Object[0][0]; // [DEPRECATED] Đã chuyển sang từng class iNpc riêng trong package zinterfaces.menus.npcs.event

    // ======================== ACTIVITY REWARDS ========================

    public static class ActivityReward {
        public String key;
        public String pointKey;
        public byte limitIndex;
        public int maxLimit;
        public List<MainItem> itemGifts = new ArrayList<>();
        public int pointIndex = -1;
        public int pointAmount = 0;

        public ActivityReward(String key, int maxLimit) {
            this.key = key;
            this.limitIndex = -1;
            this.maxLimit = maxLimit;
        }

        public ActivityReward(byte limitIndex, int maxLimit) {
            this.limitIndex = limitIndex;
            this.maxLimit = maxLimit;
        }

        public ActivityReward(int limitIndex, int maxLimit) {
            this.limitIndex = (byte) limitIndex;
            this.maxLimit = maxLimit;
        }

        public ActivityReward(String key, int limitIndex, int maxLimit) {
            this.key = key;
            this.limitIndex = (byte) limitIndex;
            this.maxLimit = maxLimit;
        }

        public void addGiftItem(int type, int id, int amount) {
            itemGifts.add(new MainItem(id, type, amount));
        }

        public void addItemGift(int id, byte type, int amount) {
            itemGifts.add(new MainItem(id, type, amount));
        }

        public void addItemGift(int id, int type, int amount) {
            itemGifts.add(new MainItem(id, type, amount));
        }

        public void setGiftPoint(int pointIndex, int pointAmount) {
            this.pointIndex = pointIndex;
            this.pointAmount = pointAmount;
        }

        public void setGiftPoint(String pointKey, int pointIndex, int pointAmount) {
            this.pointKey = pointKey;
            this.pointIndex = pointIndex;
            this.pointAmount = pointAmount;
        }
    }

    public static class EventMapDrop {
        public Class<?> mapClass;
        public int floorIndex;
        public int rate;
        
        public int itemType;
        public int itemId;
        public int itemNum;
        public List<template.Option> options;

        public EventMapDrop(Class<?> mapClass, int floorIndex, int rate, int itemType, int itemId, int itemNum) {
            this.mapClass = mapClass;
            this.floorIndex = floorIndex;
            this.rate = rate;
            this.itemType = itemType;
            this.itemId = itemId;
            this.itemNum = itemNum;
        }

        public EventMapDrop(Class<?> mapClass, int floorIndex, int rate, int itemType, int itemId, int itemNum, List<template.Option> options) {
            this(mapClass, floorIndex, rate, itemType, itemId, itemNum);
            this.options = options;
        }
    }

    public static class EventBossDrop {
        public int bossMobId; // -1 cho tất cả Boss Ngoài
        public int rate;
        public int itemType;
        public int itemId;
        public int itemNum;
        public List<template.Option> options;

        public EventBossDrop(int bossMobId, int rate, int itemType, int itemId, int itemNum) {
            this.bossMobId = bossMobId;
            this.rate = rate;
            this.itemType = itemType;
            this.itemId = itemId;
            this.itemNum = itemNum;
        }

        public EventBossDrop(int bossMobId, int rate, int itemType, int itemId, int itemNum, List<template.Option> options) {
            this(bossMobId, rate, itemType, itemId, itemNum);
            this.options = options;
        }
    }

    protected List<EventMapDrop> mapDrops = new ArrayList<>();
    protected List<EventBossDrop> bossDrops = new ArrayList<>();

    public List<EventMapDrop> getMapDrops(Class<?> mapClass, int floorIndex) {
        List<EventMapDrop> result = new ArrayList<>();
        for (EventMapDrop drop : mapDrops) {
            if (drop == null) continue;
            boolean classMatch = (drop.mapClass == null && mapClass == null)
                    || (drop.mapClass != null && mapClass != null && (drop.mapClass == mapClass || drop.mapClass.isAssignableFrom(mapClass)));
            if (classMatch && (drop.floorIndex == -1 || drop.floorIndex == floorIndex)) {
                result.add(drop);
            }
        }
        return result;
    }

    public List<EventBossDrop> getBossDrops(int bossMobId) {
        List<EventBossDrop> result = new ArrayList<>();
        for (EventBossDrop drop : bossDrops) {
            if (drop == null) continue;
            if (drop.bossMobId == -1 || drop.bossMobId == bossMobId) {
                result.add(drop);
            }
        }
        return result;
    }

    protected ActivityReward lienTangReward = null;
    protected ActivityReward sieuLienTangReward = null;
    protected ActivityReward pvpReward = null;
    protected ActivityReward wantedReward = null;
    protected ActivityReward trainReward = null;

    protected void giveActivityReward(Player p, ActivityReward reward) {
        if (reward == null || p == null || p.isBot) return;
        if (!isEventActive()) return;
        if (p.level < 10) return;
        event.EventData eventData = getOrCreateEventData(p);
        if (eventData == null) return;

        int curCount = 0;
        if (reward.key != null && !reward.key.isEmpty()) {
            curCount = eventData.getPoint(reward.key);
        } else if (reward.limitIndex >= 0 && reward.limitIndex < eventData.data.length) {
            curCount = eventData.data[reward.limitIndex];
        }

        if (curCount < reward.maxLimit) {
            if (!reward.itemGifts.isEmpty()) {
                List<template.GiftBox> checkGifts = new ArrayList<>();
                for (MainItem gift : reward.itemGifts) {
                    template.GiftBox gb = new template.GiftBox();
                    gb.id = (short) gift.id;
                    gb.type = (byte) gift.cat;
                    gb.num = gift.num;
                    checkGifts.add(gb);
                }
                boolean canAddDirectly = core.RewardService.hasEnoughBagSpace(p, checkGifts);

                if (canAddDirectly) {
                    for (MainItem gift : reward.itemGifts) {
                        p.item.add_item_bag47(gift.cat, gift.id, gift.num);
                    }
                    p.item.updateInventory(false);
                } else {
                    // Fallback to Mail safely
                    List<template.GiftBox> mailGifts = new ArrayList<>();
                    for (MainItem gift : reward.itemGifts) {
                        template.GiftBox gb = new template.GiftBox();
                        gb.id = (short) gift.id;
                        gb.type = (byte) gift.cat;
                        gb.num = gift.num;
                        mailGifts.add(gb);
                    }
                    core.MailService.sendMail(p, "Sự Kiện", "Quà Hoạt Động " + this.name,
                            "Hành trang của bạn bị đầy khi nhận thưởng hoạt động sự kiện, phần thưởng đã được chuyển an toàn vào Hộp Thư!",
                            core.MailService.MAIL_TYPE_GIFT, false, mailGifts, 0L);
                }
            }
            if (reward.pointKey != null && !reward.pointKey.isEmpty()) {
                p.addEventPoint(this.id, reward.pointKey, reward.pointIndex, reward.pointAmount);
            } else if (reward.pointIndex != -1 && reward.pointIndex < eventData.data.length) {
                eventData.data[reward.pointIndex] += reward.pointAmount;
            }

            if (reward.key != null && !reward.key.isEmpty()) {
                eventData.addPoint(reward.key, 1);
            }
            if (reward.limitIndex >= 0 && reward.limitIndex < eventData.data.length) {
                eventData.data[reward.limitIndex]++;
            }
        }
    }

    protected List<TichLuyEntry> tichNapEntries = new ArrayList<>();
    protected List<TichLuyEntry> tichTieuEntries = new ArrayList<>();

    public List<TichLuyEntry> getTichNapEntries() {
        return tichNapEntries;
    }

    public List<TichLuyEntry> getTichTieuEntries() {
        return tichTieuEntries;
    }

    protected void initTichLuy() {
        tichNapEntries.clear();
        tichTieuEntries.clear();

        // Default Tích Nạp
        tichNapEntries.add(new TichLuyEntry(50_000, new byte[]{4, 4, 4}, new short[]{159, 327, 135}, new short[]{5, 3, 5}));
        tichNapEntries.add(new TichLuyEntry(100_000, new byte[]{4, 4, 4, 4}, new short[]{159, 327, 135, 339}, new short[]{10, 5, 10, 5}));
        tichNapEntries.add(new TichLuyEntry(200_000, new byte[]{4, 4, 4, 4}, new short[]{29, 327, 135, 122}, new short[]{2, 10, 20, 3}));
        tichNapEntries.add(new TichLuyEntry(500_000, new byte[]{4, 4, 4, 4, 4}, new short[]{29, 691, 134, 122, 119}, new short[]{5, 1, 20, 5, 1}));
        tichNapEntries.add(new TichLuyEntry(1_000_000, new byte[]{4, 4, 4, 4, 4, 4}, new short[]{158, 732, 807, 134, 829, 688}, new short[]{2, 1, 5, 50, 1, 1}));
        tichNapEntries.add(new TichLuyEntry(2_000_000, new byte[]{4, 4, 4, 4, 4, 4, 4}, new short[]{158, 732, 807, 134, 643, 829, 689}, new short[]{5, 2, 10, 100, 1, 1, 1}));

        // Default Tích Tiêu
        tichTieuEntries.add(new TichLuyEntry(5_000, new byte[]{4, 4}, new short[]{159, 135}, new short[]{5, 3}));
        tichTieuEntries.add(new TichLuyEntry(10_000, new byte[]{4, 4, 4}, new short[]{159, 135, 339}, new short[]{10, 5, 5}));
        tichTieuEntries.add(new TichLuyEntry(20_000, new byte[]{4, 4, 4, 4}, new short[]{159, 135, 29, 339}, new short[]{20, 10, 2, 10}));
        tichTieuEntries.add(new TichLuyEntry(50_000, new byte[]{4, 4, 4, 4}, new short[]{29, 158, 122, 119}, new short[]{5, 2, 5, 1}));
        tichTieuEntries.add(new TichLuyEntry(100_000, new byte[]{4, 4, 4, 4}, new short[]{29, 158, 122, 829}, new short[]{10, 5, 10, 1}));
        tichTieuEntries.add(new TichLuyEntry(200_000, new byte[]{4, 4, 4, 4, 4}, new short[]{158, 122, 688, 829, 106}, new short[]{5, 20, 1, 1, 2}));
    }

    // ======================== CONSTRUCTOR ========================

    public Event(int id, String name) {
        this.id   = id;
        this.name = name;
        initTichLuy();
    }

    // ======================== GETTERS ========================

    public int              getId()        { return id; }
    public String           getName()      { return name; }
    public List<MainItem>   getShopItems() { return shopItems; }

    // ======================== LIFECYCLE ========================

    /** Khởi tạo event: init shopItems, NPC, mob, clear list... */
    public void init() throws Exception { /* // [NPC_MIGRATED] initNpc(); // NPC đã chuyển sang class iNpc riêng trong package zinterfaces.menus.npcs.event */ }

    /** Tick định kỳ từ ServerEventManager */
    public void update(int hour, int min, int sec) throws Exception {}

    /** Gửi danh sách mob sự kiện khi player vào map */
    public void sendMobsToPlayer(Player p, Zone zone) throws IOException {}

    // ======================== ACTIVITY CALLBACKS ========================

    public void onLienTang(Player p) {
        giveActivityReward(p, lienTangReward);
    }

    public void onLienTang(Player p, int floor) {
        onLienTang(p);
    }
    
    public void onSieuLienTang(Player p) {
        giveActivityReward(p, sieuLienTangReward);
    }

    public void onPvP(Player p) {
        giveActivityReward(p, pvpReward);
    }
    
    public void onWanted(Player p) {
        giveActivityReward(p, wantedReward);
    }
    
    public void onTrainMob(Player p) {
        giveActivityReward(p, trainReward);
    }

    public void onVanChuyen(Player p) {}

    public void onNhiemVuLap(Player p) {}

    public void onVuonCam(Player p, int round) {}

    // ======================== SHOP / NPC / LOGIC ========================

    /** Xử lý menu NPC sự kiện */
    public boolean handleMenu(Player p, int menuId, int index) throws IOException { return false; }

    /** Xử lý packet CMD_EVENT (cmd 47) từ client */
    public boolean handleCmdEvent(Player p, byte action) throws IOException { return false; }

    /** Gửi menu NPC sự kiện (khi click vào NPC) */
    public boolean sendMenu(Player p, int npcId) throws IOException { return false; }

    /** Dùng item sự kiện — trả true nếu event đã xử lý */
    public boolean onUseItem(Player p, int itemId) throws IOException { return false; }

    /** Quái/Boss chết — drop, thưởng */
    public void onMobKilled(Player p, Mob mob) {}


    /** Lấy mob sự kiện trong map */
    public Mob getMobInMap(Zone zone) { return null; }

    /** Item có được bán trong shop thường khi event chạy không? */
    public boolean canSellItem(Player p, int id) {
        for (short sid : sellableItems) { if (sid == id) return true; }
        return false;
    }

    /** Danh sách item thêm vào shop thường */
    public short[] getSellItemList() { return sellableItems; }

    // ----------------------------------------------------------
    //  SHOP HOOKS — override để tuỳ chỉnh hành vi mua
    // ----------------------------------------------------------

    /**
     * Điều kiện mua thêm (theo item.indexShop hoặc item.id).
     * @return true = được phép (default), false = chặn (tự gửi thông báo)
     */
    protected boolean canBuyExtra(Player p, MainItem item) throws IOException {
        return true;
    }

    /**
     * Override hoàn toàn logic mua 1 item.
     * @return true = subclass xử lý xong, false = dùng default (trừ cost + gửi quà)
     */
    protected boolean onBuyItem(Player p, MainItem item, EventData data) throws IOException {
        return false;
    }

    /** Hook sau khi mua thành công (đã trừ cost, đã gửi quà) */
    protected void onAfterBuy(Player p, MainItem item) throws IOException {}

    /**
     * Khởi tạo EventData cho player.
     * data.data[i] = limit của shopItems.get(i), slot còn lại là extra.
     */
    public void initPlayerData(EventData data) {
        if (data == null) return;
        int size = Math.max(35, (shopItems != null ? shopItems.size() : 0) + 20);
        if (data.data == null || data.data.length < size) {
            int[] newData = new int[size];
            if (data.data != null) {
                System.arraycopy(data.data, 0, newData, 0, Math.min(data.data.length, size));
            }
            data.data = newData;
        }

        // Kiểm tra nếu player chưa từng được khởi tạo limit mua shop lần đầu
        if (data.getLimit("init_shop_done") <= 0) {
            if (shopItems != null) {
                for (int i = 0; i < shopItems.size() && i < data.data.length; i++) {
                    MainItem item = shopItems.get(i);
                    if (item != null && item.limitNumBuy >= 0) {
                        data.data[i] = item.limitNumBuy;
                    }
                }
            }
            data.setLimit("init_shop_done", 1);
        }

        if (shopItems != null) {
            for (int i = 0; i < shopItems.size(); i++) {
                MainItem mi = shopItems.get(i);
                if (mi != null) {
                    data.setLimit("shop_limit_" + mi.id, mi.limitNumBuy);
                }
            }
        }
    }

    /** Reset EventData hàng ngày (reset limit về ban đầu & reset daily counters) */
    public void resetDailyData(EventData data) {
        if (data == null) return;
        data.resetDaily();
        if (data.data != null && shopItems != null) {
            for (int i = 0; i < shopItems.size() && i < data.data.length; i++) {
                MainItem mi = shopItems.get(i);
                if (mi != null && mi.limitNumBuy >= 0) {
                    data.data[i] = mi.limitNumBuy;
                }
            }
        }
    }

    private static String[] splitTimeRange(String str) {
        if (str == null || str.trim().isEmpty()) return new String[0];
        str = str.trim();
        if (str.contains(">")) {
            return str.split(">");
        } else if (str.contains("->")) {
            return str.split("->");
        } else if (str.contains("~")) {
            return str.split("~");
        }
        return new String[]{str};
    }

    public boolean checkTimeActive(String startTimeStr, String endTimeStr) {
        if (startTimeStr == null) startTimeStr = "";
        if (endTimeStr == null) endTimeStr = "";

        String startStr = startTimeStr.trim();
        String endStr = endTimeStr.trim();

        if (startStr.isEmpty() && endStr.isEmpty()) {
            return true; // Mặc định bật nếu không cấu hình
        }

        String[] parts = splitTimeRange(startStr);
        if (parts.length >= 2) {
            startStr = parts[0].trim();
            endStr = parts[1].trim();
        } else if (parts.length == 1) {
            startStr = parts[0].trim();
        }

        if (startStr.isEmpty() && endStr.isEmpty()) {
            return true;
        }

        try {
            java.util.Date startDate = !startStr.isEmpty() ? parseEventDate(startStr, true) : null;
            java.util.Date endDate = !endStr.isEmpty() ? parseEventDate(endStr, false) : null;

            if (startDate == null && endDate == null) {
                return true;
            }

            java.util.Date now = new java.util.Date();
            if (startDate != null && now.before(startDate)) {
                return false;
            }
            if (endDate != null && now.after(endDate)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println("[Event.checkTimeActive] Parse error: " + e.getMessage());
            return false;
        }
    }

    public boolean checkTimeActive(String timeStr) {
        return checkTimeActive(timeStr, "");
    }

    public static java.util.Date parseEventDate(String str, boolean isStart) {
        if (str == null || str.trim().isEmpty()) return null;
        str = str.trim();

        String[] patterns = new String[] {
            "yyyy-M-d H:m:s",
            "yyyy-M-d HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss",
            "H:m:s yyyy-M-d",
            "HH:mm:ss yyyy-MM-dd",
            "yyyy-M-d",
            "yyyy-MM-dd",
            "yyyy/M/d H:m:s",
            "yyyy/M/d HH:mm:ss",
            "yyyy/M/d",
            "d/M/yyyy H:m:s",
            "d/M/yyyy HH:mm:ss",
            "H:m:s d/M/yyyy",
            "HH:mm:ss d/M/yyyy",
            "d/M/yyyy",
            "d-M-yyyy H:m:s",
            "d-M-yyyy HH:mm:ss",
            "d-M-yyyy"
        };

        for (String pattern : patterns) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
                sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
                sdf.setLenient(true);
                java.util.Date d = sdf.parse(str);
                if (d != null) {
                    if (!pattern.contains("H") && !pattern.contains("s")) {
                        java.util.Calendar cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+7"));
                        cal.setTime(d);
                        if (isStart) {
                            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
                            cal.set(java.util.Calendar.MINUTE, 0);
                            cal.set(java.util.Calendar.SECOND, 0);
                            cal.set(java.util.Calendar.MILLISECOND, 0);
                        } else {
                            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
                            cal.set(java.util.Calendar.MINUTE, 59);
                            cal.set(java.util.Calendar.SECOND, 59);
                            cal.set(java.util.Calendar.MILLISECOND, 999);
                        }
                        return cal.getTime();
                    }
                    return d;
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    public void loadConfig(String time, String timeend, String timex2pay, String timechangeitem, String timedropitem, String timeremoveitem, String season) {
        if (time != null) this.time = time;
        if (timeend != null) this.timeend = timeend;
        if (timex2pay != null) this.timex2pay = timex2pay;
        if (timechangeitem != null) this.timechangeitem = timechangeitem;
        if (timedropitem != null) this.timedropitem = timedropitem;
        if (timeremoveitem != null) this.timeremoveitem = timeremoveitem;
        if (season != null) this.season = season;
    }

    public void loadConfig(String time, String timeend, String timex2pay, String timechangeitem, String timedropitem, String timeremoveitem) {
        loadConfig(time, timeend, timex2pay, timechangeitem, timedropitem, timeremoveitem, "");
    }

    public String getSeasonKey() {
        if (season != null && !season.trim().isEmpty()) {
            return season.trim();
        }
        return getSeasonKeyMD5();
    }

    public String getSeasonKeyMD5() {
        String startStr = "";
        String endStr = "";
        if (time != null && !time.trim().isEmpty()) {
            String[] parts = splitTimeRange(time.trim());
            if (parts.length >= 2) {
                startStr = parts[0].trim();
                endStr = parts[1].trim();
            } else if (parts.length == 1) {
                startStr = parts[0].trim();
            }
        }
        if ((endStr == null || endStr.isEmpty()) && timeend != null && !timeend.trim().isEmpty()) {
            endStr = timeend.trim();
        }

        long startTs = 0L;
        long endTs = 0L;

        if (!startStr.isEmpty()) {
            java.util.Date d = parseEventDate(startStr, true);
            if (d != null) startTs = d.getTime();
        }
        if (!endStr.isEmpty()) {
            java.util.Date d = parseEventDate(endStr, false);
            if (d != null) endTs = d.getTime();
        }

        String rawSeason = (season != null && !season.trim().isEmpty()) ? season.trim() : "event_" + id;
        String rawKey = startTs + "-" + endTs + "-" + rawSeason;
        return core.ZUtil.generateMD5Token(rawKey);
    }

    public String getRawSeasonKey() {
        return (season != null && !season.trim().isEmpty()) ? season.trim() : (time != null ? time.trim() : "");
    }

    public String getKey() {
        return getSeasonKey();
    }

    public boolean isEventActive() {
        return EventManager.isActive(id) && checkTimeActive(time, timeend);
    }

    public boolean isX2PayActive() {
        if (timex2pay == null || timex2pay.trim().isEmpty()) {
            return isEventActive();
        }
        return EventManager.isActive(id) && checkTimeActive(timex2pay, timeend);
    }

    public boolean isChangeItemActive() {
        if (timechangeitem == null || timechangeitem.trim().isEmpty()) {
            return isEventActive();
        }
        return EventManager.isActive(id) && checkTimeActive(timechangeitem, timeend);
    }

    public boolean isDropItemActive() {
        if (timedropitem == null || timedropitem.trim().isEmpty()) {
            return isEventActive();
        }
        return EventManager.isActive(id) && checkTimeActive(timedropitem, timeend);
    }

    public boolean isRemoveItemActive() {
        if (timeremoveitem == null || timeremoveitem.trim().isEmpty()) {
            return isEventActive();
        }
        return EventManager.isActive(id) && checkTimeActive(timeremoveitem, timeend);
    }

    public static java.util.Date getEventStartDate(String startStr, String defaultEndStr) {
        String[] parts = splitTimeRange(startStr);
        if (parts.length >= 1) startStr = parts[0].trim();
        if ((startStr == null || startStr.trim().isEmpty()) && defaultEndStr != null && !defaultEndStr.trim().isEmpty()) {
            startStr = defaultEndStr.trim();
        }
        return parseEventDate(startStr, true);
    }

    public static java.util.Date getEventStartDate(String timeStr) {
        return getEventStartDate(timeStr, "");
    }

    public static java.util.Date getEventEndDate(String startStr, String defaultEndStr) {
        String[] parts = splitTimeRange(startStr);
        if (parts.length >= 2) {
            defaultEndStr = parts[1].trim();
        } else if ((defaultEndStr == null || defaultEndStr.trim().isEmpty()) && startStr != null && !startStr.trim().isEmpty()) {
            defaultEndStr = startStr.trim();
        }
        return parseEventDate(defaultEndStr, false);
    }

    public static java.util.Date getEventEndDate(String timeStr) {
        return getEventEndDate(timeStr, "");
    }

    public boolean isEventEnded() {
        if ((time == null || time.trim().isEmpty()) && (timeend == null || timeend.trim().isEmpty())) {
            return false;
        }
        try {
            java.util.Date endDate = getEventEndDate(this.time, this.timeend);
            if (endDate == null) return false;
            return new java.util.Date().after(endDate);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean shouldCleanupEventItems() {
        // 1. Nếu sự kiện đang chạy active hợp lệ -> Không xóa
        if (isEventActive()) {
            return false;
        }

        // 2. Nếu sự kiện còn trong thời gian ân hạn đổi quà / xóa vật phẩm (changeitem / removeitem) -> Chưa xóa
        if (EventManager.isActive(this.id)) {
            if (isChangeItemActive() || isRemoveItemActive()) {
                return false;
            }
        }

        // 3. Trong tất cả các trường hợp còn lại (sự kiện đã tắt, bị hủy kích hoạt, hoặc nằm ngoài khoảng thời gian sự kiện) -> Tự động xóa
        return true;
    }

    public static String getEventPointKey1(int eventId) {
        switch (eventId) {
            case 1: return "top_bat_pokemon";
            case 2: return "top_dot_phao";
            case 3: return "top_gio_to";
            case 4: return "top_mo_ruong_kibi";
            case 5: return "top_banh_keo";
            case 6: return "top_kc_do";
            case 7: return "top_hqmq";
            case 8: return "top_long_den";
            case 9: return "top_trong_cay";
            case 10: return "top_gio_hoa";
            case 11: return "top_goi_hop_qua";
            case 12: return "top_trang_tri";
            case 13: return "top_trieu_hoi";
            case 14: return "top_sen_trang";
            case 15: return "top_goi_socola";
            case 16: return "top_bigmom";
            case 17: return "top_hoa_dang";
            case 18: return "top_hoa_hong_trang";
            case 19: return "top_che_dau_do";
            default: return "point_event1";
        }
    }

    public static String getEventPointKey2(int eventId) {
        switch (eventId) {
            case 1: return "top_ve_tuoi_tho";
            case 2: return "top_banh_chung";
            case 3: return "top_gio_to_boss";
            case 4: return "top_ghep_kinh_kibi";
            case 5: return "top_bong_bong";
            case 6: return "top_kc_tim";
            case 7: return "top_nap";
            case 8: return "top_den_keo_quan";
            case 9: return "top_gom_trai_cay";
            case 10: return "top_bo_hoa";
            case 11: return "top_hop_qua_db";
            case 13: return "top_du_doan";
            case 14: return "top_sen_hong";
            case 15: return "top_an_socola";
            case 17: return "top_kaido";
            case 18: return "top_bo_hoa_vulan";
            case 19: return "top_cau_duyen";
            default: return "point_event2";
        }
    }

    public int[] getEventItemsToRemove() {
        return new int[0];
    }

    public event.EventData getOrCreateEventData(Player p) {
        if (p == null) return null;
        String sKey = getSeasonKey();
        event.EventData ev = p.getDataEvent(this.id, sKey);
        if (ev == null) {
            ev = new event.EventData();
            ev.eventID = this.id;
            ev.key = sKey;
            ev.data = new int[35];
            p.eventData.add(ev);
        }
        if (ev.key == null || (sKey != null && !ev.key.equals(sKey))) {
            ev.key = sKey;
        }
        if (ev.data == null || ev.data.length < 35) {
            int[] newData = new int[35];
            if (ev.data != null) {
                System.arraycopy(ev.data, 0, newData, 0, Math.min(ev.data.length, 35));
            }
            ev.data = newData;
        }
        return ev;
    }

    public void dropEventItem(Player p, Mob mob, int itemId, int chance, int limit, String key, int dataIndex) {
        if (!isDropItemActive()) return;
        if (mob == null || p == null) return;
        p = p.getOwnerPlayer();
        if (p.isBot || p.item == null || p.level < 10) return;
        
        int finalChance = chance;
        if (mob.map != null && mob.map.map_dungeon != null) {
            finalChance = chance * 2; // Gấp đôi tỷ lệ rơi trong phó bản
        }
        
        if (core.ZUtil.random(100) < finalChance) {
            if (p.level >= 10 && Math.abs(p.level - mob.level) <= 10) {
                event.EventData evData = getOrCreateEventData(p);
                if (evData != null) {
                    int cur = (key != null && !key.isEmpty()) ? evData.getPoint(key) : (dataIndex >= 0 && dataIndex < evData.data.length ? evData.data[dataIndex] : 0);
                    if (cur < limit) {
                        if (key != null && !key.isEmpty()) evData.addPoint(key, 1);
                        if (dataIndex >= 0 && dataIndex < evData.data.length) evData.data[dataIndex]++;
                        p.item.add_item_bag47(4, itemId, 1);
                        p.item.updateInventory(false);
                    }
                }
            }
        }
    }

    public void dropEventItem(Player p, Mob mob, int itemId, int chance, int limit, int dataIndex) {
        dropEventItem(p, mob, itemId, chance, limit, "daily_drop_limit_" + dataIndex, dataIndex);
    }

    public void dropEventItemRandom(Player p, Mob mob, int[] itemIds, int chance, int limit, String key, int dataIndex) {
        if (!isDropItemActive()) return;
        if (mob == null || p == null) return;
        p = p.getOwnerPlayer();
        if (p.isBot || p.item == null || itemIds == null || itemIds.length == 0 || p.level < 10) return;
        
        int finalChance = chance;
        if (mob.map != null && mob.map.map_dungeon != null) {
            finalChance = chance * 2; // Gấp đôi tỷ lệ rơi trong phó bản
        }
        
        if (core.ZUtil.random(100) < finalChance) {
            if (p.level >= 10 && Math.abs(p.level - mob.level) <= 10) {
                event.EventData evData = getOrCreateEventData(p);
                if (evData != null) {
                    int cur = (key != null && !key.isEmpty()) ? evData.getPoint(key) : (dataIndex >= 0 && dataIndex < evData.data.length ? evData.data[dataIndex] : 0);
                    if (cur < limit) {
                        if (key != null && !key.isEmpty()) evData.addPoint(key, 1);
                        if (dataIndex >= 0 && dataIndex < evData.data.length) evData.data[dataIndex]++;
                        int itemId = itemIds[core.ZUtil.random(itemIds.length)];
                        p.item.add_item_bag47(4, itemId, 1);
                        p.item.updateInventory(false);
                    }
                }
            }
        }
    }

    public void dropEventItemRandom(Player p, Mob mob, int[] itemIds, int chance, int limit, int dataIndex) {
        dropEventItemRandom(p, mob, itemIds, chance, limit, "daily_drop_limit_" + dataIndex, dataIndex);
    }

    /**
     * Danh sách các Item4 quan trọng / vĩnh viễn TUYỆT ĐỐI KHÔNG ĐƯỢC XÓA khi dọn dẹp sự kiện.
     * Bao gồm: Tiền tệ, Thức ăn/Potion, Rương trang bị, Trái ác quỷ, Đá khảm, Sách Haki,
     * Đơn dược (413-416), Danh hiệu, Pet/Trứng, Mảnh trang bị, Hộp thời trang, Bảo hiểm, Vé.
     */
    private static final java.util.Set<Integer> PROTECTED_ITEM4_IDS = new java.util.HashSet<>(java.util.Arrays.asList(
        // Tiền tệ / Cơ bản
        0, 1, 908,
        // Potion / Thức ăn
        2, 3, 4, 5, 6, 80, 81, 82, 83, 84, 85, 173, 174, 175, 176, 177, 178, 179,
        // Rương trang bị / Rương quý
        7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
        105, 106, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131,
        136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158,
        215, 216, 217, 218, 690, 691, 695, 696, 697, 698, 699, 700, 701, 702, 703, 704,
        732, 740, 741, 742, 743, 744, 745, 746, 747, 751, 752, 772, 773, 774, 775, 776, 777,
        823, 827, 828, 829, 830, 831, 845, 846, 847, 848, 849, 850, 851, 852, 853, 854, 855, 856, 857, 858, 859, 860, 861, 862, 863, 864, 894, 911,
        // Trái Ác Quỷ
        32, 33, 34, 86, 87, 88, 90, 91, 92, 93, 134, 160, 161, 219, 220, 240, 316, 317, 318, 322, 427, 869, 870, 873,
        // Đá khảm / Cường hóa
        44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79,
        135, 221, 222, 223, 224, 225, 226, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270,
        272, 273, 274, 275, 276, 277, 321, 324, 325, 326, 327, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373,
        493, 494, 495, 496, 497, 498, 499, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512, 513, 514, 515, 516, 517,
        588, 644, 645, 646, 647, 648, 649, 650, 651, 652, 653, 654, 655, 656, 657, 658, 659, 660, 661, 662, 663, 664, 665, 666, 667, 668, 669, 670, 671, 672, 673, 674, 675, 676, 677, 678, 679, 680, 681, 682, 683,
        // Sách Kỹ Năng / Sách Haki
        30, 31, 159, 333, 639, 640, 641, 642, 643, 684, 753, 754, 755, 756, 757, 758, 759, 760, 761, 762,
        // Đơn dược vĩnh viễn / Búa
        323, 339, 413, 414, 415, 416, 457,
        // Danh hiệu
        763, 764, 765, 766, 767, 768, 769, 770, 779, 780, 781, 782, 787, 788, 795, 796, 805, 810, 824, 832, 840, 842, 865,
        // Pet / Thú cưng / Trứng / Thức ăn Pet
        233, 234, 235, 441, 685, 686, 687, 688, 689, 727, 728, 729, 730, 731, 825, 903, 904, 905,
        // Mảnh trang bị
        304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315,
        536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547,
        692, 693, 694, 705, 706, 707, 708, 709, 710, 711, 712, 713, 714, 715, 716, 717, 718, 719, 720, 721, 722, 723, 724, 725,
        898, 899, 900, 901, 902,
        // Bảo hiểm / Khóa / Buff đệ tử
        548, 549, 550, 551, 733, 734, 735, 737, 738, 794,
        // Vé / Thẻ hệ thống
        35, 40, 41, 42, 43, 89, 101, 102, 103, 104, 132, 133, 214, 232, 271, 360, 361, 771, 801, 802, 866, 871, 872,
        // Hộp thời trang / Thẻ thời trang
        228, 287, 356, 358, 456, 467, 468, 469, 475, 482, 518, 520, 521, 522, 568, 581, 589, 600, 621, 622, 637, 638,
        726, 736, 739, 748, 749, 750, 778, 789, 790, 791, 792, 793, 821, 822, 843, 844, 906, 907,
        // Hành trình
        442, 443, 444, 445, 446, 447, 448, 449, 450, 451, 452, 453, 454, 455, 519, 579, 580,
        // Hệ thống Sư Đồ / May mặc vĩnh viễn (Cuộn chỉ 1-4, Chứng nhận sư phụ, Bùa xóa phản sư)
        623, 624, 625, 626, 627, 628
    ));

    public static boolean isProtectedItem4(int id) {
        return id <= 28 || PROTECTED_ITEM4_IDS.contains(id);
    }

    public static java.util.Set<Integer> getActiveEventItemIds() {
        java.util.Set<Integer> activeSet = new java.util.HashSet<>();
        try {
            for (Event ev : EventManager.gI().getActiveEventsList()) {
                if (ev == null) continue;
                if (ev.costItemId >= 0) {
                    activeSet.add((int) ev.costItemId);
                }
                if (ev.shopItems != null) {
                    for (MainItem item : ev.shopItems) {
                        if (item != null && item.cat == 4) {
                            activeSet.add((int) item.id);
                        }
                        if (item instanceof itemz.MainItemShop) {
                            itemz.MainItemShop mis = (itemz.MainItemShop) item;
                            if (mis.reqItems != null) {
                                for (itemz.MainItemShop.ReqItem req : mis.reqItems) {
                                    if (req.cat == 4) activeSet.add((int) req.id);
                                }
                            }
                        }
                    }
                }
                if (ev.sellableItems != null) {
                    for (short sid : ev.sellableItems) {
                        activeSet.add((int) sid);
                    }
                }
                if (ev.mapDrops != null) {
                    for (EventMapDrop md : ev.mapDrops) {
                        if (md != null && md.itemType == 4) {
                            activeSet.add((int) md.itemId);
                        }
                    }
                }
                if (ev.bossDrops != null) {
                    for (EventBossDrop bd : ev.bossDrops) {
                        if (bd != null && bd.itemType == 4) {
                            activeSet.add((int) bd.itemId);
                        }
                    }
                }
                int[] toRemove = ev.getEventItemsToRemove();
                if (toRemove != null) {
                    for (int id : toRemove) {
                        activeSet.add((int) id);
                    }
                }
            }
        } catch (Exception ignored) {}
        return activeSet;
    }

    public void checkAndRemoveEventItems(Player p) {
        if (p == null || p.item == null || p.isBot) return;
        if (!shouldCleanupEventItems()) return;
        try {
            int[] itemsToRemove = getEventItemsToRemove();
            if (itemsToRemove == null || itemsToRemove.length == 0) return;
            
            java.util.Set<Integer> activeItems = getActiveEventItemIds();
            java.util.Set<Integer> removeSet = new java.util.HashSet<>();
            for (int id : itemsToRemove) {
                if (!isProtectedItem4(id) && !activeItems.contains(id)) {
                    removeSet.add(id);
                }
            }

            if (removeSet.isEmpty()) return;

            boolean removedBag = false;
            boolean removedBox = false;
            boolean removedSave = false;

            // 1. Xóa khỏi Hành trang bag47 (CHỈ XÓA CATEGORY 4 - Vật phẩm sự kiện)
            if (p.item.bag47 != null) {
                for (int i = p.item.bag47.size() - 1; i >= 0; i--) {
                    template.ItemBag47 it = p.item.bag47.get(i);
                    if (it != null && it.category == 4 && removeSet.contains((int) it.id)) {
                        p.item.bag47.remove(i);
                        removedBag = true;
                    }
                }
            }

            // 2. Xóa khỏi Rương đồ box47 (CHỈ XÓA CATEGORY 4 - Vật phẩm sự kiện)
            if (p.item.box47 != null) {
                for (int i = p.item.box47.size() - 1; i >= 0; i--) {
                    template.ItemBag47 it = p.item.box47.get(i);
                    if (it != null && it.category == 4 && removeSet.contains((int) it.id)) {
                        p.item.box47.remove(i);
                        removedBox = true;
                    }
                }
            }

            // 3. Xóa khỏi Lưu trữ tạm thời save_item_47 (CHỈ XÓA CATEGORY 4 - Vật phẩm sự kiện)
            if (p.item.save_item_47 != null) {
                for (int i = p.item.save_item_47.size() - 1; i >= 0; i--) {
                    template.ItemBag47 itSave = p.item.save_item_47.get(i);
                    if (itSave != null && itSave.category == 4 && removeSet.contains((int) itSave.id)) {
                        p.item.save_item_47.remove(i);
                        removedSave = true;
                    }
                }
            }

            if (removedBag || removedBox || removedSave) {
                if (removedBag) p.item.updateInventory(false);
                if (removedBox) p.item.update_Inventory_box(-1, false);
                if (p.getService() != null) {
                    p.getService().send_box_ThongBao_OK("Vật phẩm sự kiện [" + name + "] đã hết hạn và được xóa khỏi hành trang / rương đồ.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkAndTriggerOfflineItemCleanup() {
        if (!shouldCleanupEventItems()) return;
        try {
            String cleanupKey = "OFFLINE_CLEANUP_EVENT_" + this.id + "_" + getSeasonKey();
            if (historys.HistoryManager.hasSystemLog(cleanupKey)) {
                return;
            }
            int[] itemsToRemove = getEventItemsToRemove();
            if (itemsToRemove != null && itemsToRemove.length > 0) {
                new Thread(() -> {
                    try {
                        removeOfflineEventItems(itemsToRemove);
                        historys.HistoryManager.saveSystemLog(cleanupKey, "{\"event_id\":" + this.id + ",\"time\":" + System.currentTimeMillis() + "}");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }, "Event-Cleanup-" + this.id).start();
            } else {
                historys.HistoryManager.saveSystemLog(cleanupKey, "{\"event_id\":" + this.id + ",\"empty\":true}");
            }
        } catch (Exception e) {
            // Ignore parse exception
        }
    }

    public static void removeOfflineEventItems(int[] itemsToRemove) {
        if (itemsToRemove == null || itemsToRemove.length == 0) return;
        java.util.Set<Integer> activeItems = getActiveEventItemIds();
        java.util.Set<Integer> removeSet = new java.util.HashSet<>();
        for (int id : itemsToRemove) {
            if (!isProtectedItem4(id) && !activeItems.contains(id)) {
                removeSet.add(id);
            }
        }

        if (removeSet.isEmpty()) return;

        // Lấy danh sách ID người chơi đang online để loại trừ (vì online được xử lý trực tiếp trong RAM)
        java.util.Set<Integer> onlineIds = new java.util.HashSet<>();
        for (network.Session ss : network.SessionManager.CLIENT_ENTRYS) {
            if (ss != null && ss.p != null) {
                onlineIds.add(ss.p.IDPlayer);
            }
        }

        try (java.sql.Connection conn = database.DbManager.gI().getConnect();
             java.sql.PreparedStatement psSelect = conn.prepareStatement("SELECT `id`, `bag47`, `box47`, `save_it47` FROM `players`")) {
            try (java.sql.ResultSet rs = psSelect.executeQuery()) {
                java.sql.PreparedStatement psUpdate = conn.prepareStatement("UPDATE `players` SET `bag47` = ?, `box47` = ?, `save_it47` = ? WHERE `id` = ?");
                int batchCount = 0;

                while (rs.next()) {
                    int pId = rs.getInt("id");
                    if (onlineIds.contains(pId)) {
                        continue; // Bỏ qua người chơi online
                    }

                    String bag47Json = rs.getString("bag47");
                    String box47Json = rs.getString("box47");
                    String save47Json = rs.getString("save_it47");

                    boolean bagModified = false;
                    boolean boxModified = false;
                    boolean saveModified = false;

                    String newBag47 = cleanJsonArray47(bag47Json, removeSet);
                    if (newBag47 != null) bagModified = true;
                    else newBag47 = bag47Json;

                    String newBox47 = cleanJsonArray47(box47Json, removeSet);
                    if (newBox47 != null) boxModified = true;
                    else newBox47 = box47Json;

                    String newSave47 = cleanJsonArray47(save47Json, removeSet);
                    if (newSave47 != null) saveModified = true;
                    else newSave47 = save47Json;

                    if (bagModified || boxModified || saveModified) {
                        psUpdate.setString(1, newBag47);
                        psUpdate.setString(2, newBox47);
                        psUpdate.setString(3, newSave47);
                        psUpdate.setInt(4, pId);
                        psUpdate.addBatch();
                        batchCount++;
                    }
                }

                if (batchCount > 0) {
                    psUpdate.executeBatch();
                    System.out.println("[Event] Cleaned offline event items in database (bag47/box47/save_it47) for " + batchCount + " players.");
                }
                psUpdate.close();
            }
        } catch (Exception e) {
            System.err.println("[Event] Error removing offline event items from DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String cleanJsonArray47(String jsonStr, java.util.Set<Integer> removeSet) {
        if (jsonStr == null || jsonStr.trim().isEmpty() || "[]".equals(jsonStr.trim())) return null;
        try {
            org.json.simple.JSONArray arr = (org.json.simple.JSONArray) org.json.simple.JSONValue.parse(jsonStr);
            if (arr == null || arr.isEmpty()) return null;

            org.json.simple.JSONArray newArr = new org.json.simple.JSONArray();
            boolean modified = false;

            for (Object item : arr) {
                if (item instanceof org.json.simple.JSONArray) {
                    org.json.simple.JSONArray itemArr = (org.json.simple.JSONArray) item;
                    if (itemArr.size() >= 2) {
                        int cat = Integer.parseInt(itemArr.get(0).toString());
                        int itemId = Integer.parseInt(itemArr.get(1).toString());
                        if (cat == 4 && removeSet.contains(itemId)) {
                            modified = true;
                            continue; // Skip this event item (Category 4 only)
                        }
                    }
                } else if (item instanceof org.json.simple.JSONObject) {
                    org.json.simple.JSONObject obj = (org.json.simple.JSONObject) item;
                    if (obj.containsKey("id")) {
                        int cat = obj.containsKey("cat") ? Integer.parseInt(obj.get("cat").toString()) : (obj.containsKey("category") ? Integer.parseInt(obj.get("category").toString()) : 4);
                        int itemId = Integer.parseInt(obj.get("id").toString());
                        if (cat == 4 && removeSet.contains(itemId)) {
                            modified = true;
                            continue;
                        }
                    }
                } else if (item instanceof String) {
                    Object parsedItem = org.json.simple.JSONValue.parse(item.toString());
                    if (parsedItem instanceof org.json.simple.JSONArray) {
                        org.json.simple.JSONArray subArr = (org.json.simple.JSONArray) parsedItem;
                        if (subArr.size() >= 2) {
                            int cat = Integer.parseInt(subArr.get(0).toString());
                            int itemId = Integer.parseInt(subArr.get(1).toString());
                            if (cat == 4 && removeSet.contains(itemId)) {
                                modified = true;
                                continue;
                            }
                        }
                    }
                }
                newArr.add(item);
            }

            return modified ? newArr.toJSONString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    // ======================== SHOP BASE ========================

    public void openShop(Player p) {
        try {
            if (shopItems == null || shopItems.isEmpty()) {
                if (eventShop != null && eventShop.getShopItems() != null && !eventShop.getShopItems().isEmpty()) {
                    shopItems = new ArrayList<>(eventShop.getShopItems());
                } else {
                    try { init(); } catch (Exception ignored) {}
                }
            }
            if (shopItems == null || shopItems.isEmpty()) return;
            EventData data = getOrCreateEventData(p);
            if (data == null) return;
            initPlayerData(data);

            p.isShopSk = true;
            p.typeShop = id; // client dùng typeShop để biết shop thuộc event nào

            Message m = new Message(-19);
            m.writer().writeByte(118);
            m.writer().writeUTF(shopName != null ? shopName : name);
            m.writer().writeByte(11); // Category 11 = Shop Limit / Event Shop
            m.writer().writeShort(shopItems.size());

            for (MainItem item : shopItems) {
                m.writer().writeShort(item.id);
                m.writer().writeByte(item.cat);

                String itemName = item.name;
                short itemIcon = item.idIcon;
                String itemInfo = "";

                if (item.cat == 4) {
                    template.ItemTemplate4 it4 = template.ItemTemplate4.get_it_by_id(item.id);
                    if (it4 != null) {
                        if (itemName == null || itemName.isEmpty()) itemName = it4.name;
                        itemIcon = it4.icon;
                        template.ItemTemplate4_Info infoTemp = template.ItemTemplate4_Info.get_by_id(it4.indexInfoPotion);
                        if (infoTemp != null) itemInfo = infoTemp.info;
                    }
                } else if (item.cat == 7) {
                    template.ItemTemplate7 it7 = template.ItemTemplate7.get_it_by_id(item.id);
                    if (it7 != null) {
                        if (itemName == null || itemName.isEmpty()) itemName = it7.name;
                        itemIcon = it7.icon;
                        itemInfo = "Nguyên liệu nâng cấp";
                    }
                } else if (item.cat == 3) {
                    template.ItemTemplate3 it3 = template.ItemTemplate3.get_it_by_id(item.id);
                    if (it3 != null) {
                        if (itemName == null || itemName.isEmpty()) itemName = it3.name;
                        itemIcon = it3.icon;
                    }
                } else if (item.cat == 105) {
                    template.ItemFashion itf = template.ItemFashion.get_item(item.id);
                    if (itf != null) {
                        if (itemName == null || itemName.isEmpty()) itemName = itf.name;
                        itemIcon = itf.idIcon;
                        itemInfo = "Thời Trang";
                    }
                }

                if (itemName == null || itemName.isEmpty()) {
                    itemName = "Vật phẩm " + item.id;
                }

                m.writer().writeUTF(itemName);
                m.writer().writeShort(itemIcon);
                m.writer().writeUTF(buildItemInfo(item, data, itemInfo));
            }

            p.addmsg(m);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Xử lý mua item trong shop sự kiện với hộp thoại chọn số lượng (20, 50, 100, Nhập số lượng, Đóng).
     * Trang bị (cat 3) và Thời trang (cat 105) mua lẻ 1 cái một như cũ.
     */
    public void buyShop(Player p, int index, int cat) throws IOException {
        MainItem item = findItem(index, cat);
        if (item == null) return;

        String itemName = (item.name != null && !item.name.isEmpty()) ? item.name : itemz.MainItemShop.getItemName(item.cat, item.id);

        // Trang bị & Thời trang mua lẻ 1 cái như cũ
        if (item.cat == 3 || item.cat == 105) {
            buyShopWithQuantity(p, item, 1);
            return;
        }

        // Hiện YesNoDialog chọn số lượng (20, 50, 100, Nhập số lượng, Đóng)
        p.setyesNoDialog(new model.YesNoDialog(p, 999, "Cửa Hàng", "Chọn số lượng muốn đổi [" + itemName + "]:",
                new String[]{"20", "50", "100", "Nhập số lượng", "Đóng"},
                new byte[]{-1, -1, -1, -1, 1},
                value -> {
                    if (value == 0) {
                        try { buyShopWithQuantity(p, item, 20); } catch (Exception e) { e.printStackTrace(); }
                    } else if (value == 1) {
                        try { buyShopWithQuantity(p, item, 50); } catch (Exception e) { e.printStackTrace(); }
                    } else if (value == 2) {
                        try { buyShopWithQuantity(p, item, 100); } catch (Exception e) { e.printStackTrace(); }
                    } else if (value == 3) {
                        new model.InputDialog(p, 999, "Nhập số lượng", new String[]{"Số lượng muốn đổi (1 - 9999):"},
                                inputs -> {
                                    if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) {
                                        p.getService().end_Dialog();
                                        return;
                                    }
                                    try {
                                        int qty = Integer.parseInt(inputs[0].trim());
                                        if (qty < 1 || qty > 9999) {
                                            p.getService().end_Dialog();
                                            p.getService().send_box_ThongBao_OK("Số lượng không hợp lệ! Vui lòng nhập từ 1 đến 9999.");
                                            return;
                                        }
                                        buyShopWithQuantity(p, item, qty);
                                    } catch (NumberFormatException e) {
                                        p.getService().end_Dialog();
                                        p.getService().send_box_ThongBao_OK("Vui lòng nhập số hợp lệ!");
                                    } catch (Exception e) {
                                        p.getService().end_Dialog();
                                        e.printStackTrace();
                                    }
                                }).startInput();
                    } else {
                        p.getService().end_Dialog();
                    }
                }));
        p.getService().startYesNo();
    }

    public void buyShopWithQuantity(Player p, MainItem item, int quantity) throws IOException {
        if (p == null || item == null || quantity < 1 || quantity > 9999) {
            if (p != null) p.getService().end_Dialog();
            return;
        }

        synchronized (p) {
            EventData data = getEventData(p);
            if (data == null) {
                p.getService().end_Dialog();
                return;
            }

            // 1. Kiểm tra limit
            if (item.limitNumBuy >= 0 && item.indexShop >= 0 && item.indexShop < data.data.length) {
                if (data.data[item.indexShop] <= 0) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Bạn đã hết lượt đổi vật phẩm này!");
                    return;
                }
                if (data.data[item.indexShop] < quantity) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Bạn chỉ còn " + data.data[item.indexShop] + " lượt đổi vật phẩm này!");
                    return;
                }
            }

            // 2. Túi đồ
            int totalItemAmount = (item.num > 0 ? item.num : 1) * quantity;
            if (item.cat == 3) {
                if (p.item.able_bag() < quantity) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                    return;
                }
            } else if (item.cat == 105) {
                if (p.item.able_bag() < 1) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                    return;
                }
            } else {
                if (!p.item.can_add_item_bag47(item.cat, item.id, totalItemAmount)) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                    return;
                }
            }

            // 3. Thời gian đổi quà sự kiện
            if (!isChangeItemActive()) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Thời gian đổi quà sự kiện đã kết thúc!");
                return;
            }
            // 3.1. Điều kiện thêm (subclass)
            if (!canBuyExtra(p, item)) {
                p.getService().end_Dialog();
                return;
            }

            // 3.5. Kiểm tra chi phí MainItemShop (Beri, Ruby, Extol, ReqItems)
            if (item instanceof itemz.MainItemShop) {
                itemz.MainItemShop mis = (itemz.MainItemShop) item;
                long totalBeri = (long) mis.priceBeri * quantity;
                long totalRuby = (long) mis.priceRuby * quantity;
                long totalExtol = (long) mis.priceExtol * quantity;

                if (totalBeri > Integer.MAX_VALUE || totalRuby > Integer.MAX_VALUE || totalExtol > Integer.MAX_VALUE) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Số lượng quá lớn, không thể thực hiện giao dịch!");
                    return;
                }

                if (mis.priceBeri > 0 && p.get_vang() < totalBeri) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Không đủ Beri! Cần " + core.ZUtil.number_format(totalBeri) + " (đang có " + core.ZUtil.number_format(p.get_vang()) + ")");
                    return;
                }
                if (mis.priceRuby > 0 && p.get_ngoc() < totalRuby) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Không đủ Ruby! Cần " + core.ZUtil.number_format(totalRuby) + " (đang có " + core.ZUtil.number_format(p.get_ngoc()) + ")");
                    return;
                }
                if (mis.priceExtol > 0 && p.get_vnd() < totalExtol) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Không đủ Extol! Cần " + core.ZUtil.number_format(totalExtol) + " (đang có " + core.ZUtil.number_format(p.get_vnd()) + ")");
                    return;
                }
                if (mis.reqItems != null && !mis.reqItems.isEmpty()) {
                    for (itemz.MainItemShop.ReqItem req : mis.reqItems) {
                        long totalReqLong = (long) req.num * quantity;
                        if (totalReqLong > Integer.MAX_VALUE) {
                            p.getService().end_Dialog();
                            p.getService().send_box_ThongBao_OK("Số lượng quá lớn!");
                            return;
                        }
                        int totalReq = (int) totalReqLong;
                        int have = p.item.total_item_bag_by_id(req.cat, req.id);
                        if (have < totalReq) {
                            String reqName = req.getName();
                            p.getService().end_Dialog();
                            p.getService().send_box_ThongBao_OK("Không đủ " + reqName + "! Cần " + totalReq + " (đang có " + have + ")");
                            return;
                        }
                    }
                }
            } else {
                // MainItem thông thường: nếu có price mà không có costItemId thì check Beri
                long totalBeri = (long) item.price * quantity;
                if (totalBeri > Integer.MAX_VALUE) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Số lượng quá lớn!");
                    return;
                }
                if (costItemId < 0 && item.price > 0 && p.get_vang() < totalBeri) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Không đủ Beri! Cần " + core.ZUtil.number_format(totalBeri) + " (đang có " + core.ZUtil.number_format(p.get_vang()) + ")");
                    return;
                }
            }

            // 4. Kiểm tra đủ cost item nếu có
            boolean usesDefaultCost = !(item instanceof itemz.MainItemShop && (((itemz.MainItemShop) item).priceBeri > 0 || ((itemz.MainItemShop) item).priceRuby > 0 || ((itemz.MainItemShop) item).priceExtol > 0 || !((itemz.MainItemShop) item).reqItems.isEmpty()));
            if (usesDefaultCost && costItemId >= 0 && item.price > 0) {
                long totalCostLong = (long) item.price * quantity;
                if (totalCostLong > Integer.MAX_VALUE) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Số lượng quá lớn!");
                    return;
                }
                int totalCost = (int) totalCostLong;
                int have = p.item.total_item_bag_by_id(costItemType, costItemId);
                if (have < totalCost) {
                    String costName = itemz.MainItemShop.getItemName(costItemType, costItemId);
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK(
                            "Không đủ " + costName + "! Cần " + totalCost + " (đang có " + have + ")");
                    return;
                }
            }

            // 5. Subclass override hoàn toàn?
            if (onBuyItem(p, item, data)) {
                openShop(p);
                onAfterBuy(p, item);
                p.getService().end_Dialog();
                return;
            }

            // 6. Trừ chi phí
            if (item instanceof itemz.MainItemShop) {
                itemz.MainItemShop mis = (itemz.MainItemShop) item;
                if (mis.priceBeri > 0) {
                    p.update_vang(-(long) mis.priceBeri * quantity);
                }
                if (mis.priceRuby > 0) {
                    p.update_ngoc(-mis.priceRuby * quantity);
                }
                if (mis.priceExtol > 0) {
                    p.updateVnd(-(long) mis.priceExtol * quantity);
                }
                if (mis.reqItems != null && !mis.reqItems.isEmpty()) {
                    for (itemz.MainItemShop.ReqItem req : mis.reqItems) {
                        p.item.remove_item47(req.cat, req.id, req.num * quantity);
                    }
                }
                p.updateMoney();
            } else {
                if (usesDefaultCost && costItemId >= 0 && item.price > 0) {
                    p.item.remove_item47(costItemType, costItemId, item.price * quantity);
                } else if (item.price > 0) {
                    p.update_vang(-(long) item.price * quantity);
                    p.updateMoney();
                }
            }

            // 6.1. Trừ limit nếu có
            if (item.limitNumBuy >= 0 && item.indexShop >= 0 && item.indexShop < data.data.length) {
                data.data[item.indexShop] -= quantity;
            }

            // 7. Thêm vật phẩm vào hành trang
            if (item.cat == 105) {
                template.ItemFashion itf = template.ItemFashion.get_item(item.id);
                if (itf != null) {
                    template.ItemFashionP2 skin = p.check_fashion(itf.ID);
                    boolean isPermanent = (item.time == -1 || (item.limitDay <= 0 && item.time <= 0));
                    if (skin == null) {
                        template.ItemFashionP2 temp2 = new template.ItemFashionP2();
                        temp2.id = itf.ID;
                        if (isPermanent) {
                            temp2.expires = -1;
                        } else if (item.limitDay > 0) {
                            temp2.expires = System.currentTimeMillis() + (long) item.limitDay * 86400000L;
                        } else {
                            temp2.expires = System.currentTimeMillis() + item.time;
                        }
                        p.fashion.add(temp2);
                        p.update_fashionP2(temp2);
                    } else {
                        if (isPermanent || skin.expires == -1) {
                            skin.expires = -1;
                        } else {
                            long addTime = item.limitDay > 0 ? (long) item.limitDay * 86400000L : (item.time > 0 ? item.time : 86400000L);
                            long baseTime = Math.max(System.currentTimeMillis(), skin.expires);
                            skin.expires = baseTime + addTime;
                        }
                        p.update_fashionP2(skin);
                    }
                    if (p.map != null && p.map.players != null) {
                        for (int i = 0; i < p.map.players.size(); i++) {
                            Player p0 = p.map.players.get(i);
                            if (p0 != null && p0.getService() != null) {
                                p0.getService().charWearing(p, false);
                            }
                        }
                    }
                    p.getService().UpdateInfoMaincharInfo();
                }
            } else if (item.cat == 3) {
                template.ItemTemplate3 template3 = template.ItemTemplate3.get_it_by_id(item.id);
                if (template3 != null) {
                    for (int q = 0; q < quantity; q++) {
                        template.Item_wear it_add = new template.Item_wear();
                        it_add.setup_template_by_id(template3);
                        if (item.option1 != null && !item.option1.isEmpty()) {
                            it_add.option_item.clear();
                            for (template.Option opt : item.option1) {
                                it_add.option_item.add(new template.Option(opt.id, opt.getParam()));
                            }
                        }
                        p.item.add_item_bag3(it_add);
                    }
                }
            } else if (item.cat == 4 || item.cat == 7) {
                int addNum = (item.num > 0 ? item.num : 1) * quantity;
                p.item.add_item_bag47(item.cat, item.id, addNum);
            }

            p.item.updateInventory(false);
            p.updateMoney();
            Message m22 = new Message(-64);
            m22.writer().writeUTF("Đổi thành công x" + quantity);
            p.addmsg(m22);
            m22.cleanup();

            // 8. Refresh shop UI
            openShop(p);

            // 9. Post-buy hook
            onAfterBuy(p, item);
            p.getService().end_Dialog();
        }
    }

    // ======================== NPC INIT ========================

    /**
     * Tạo NPC vào map theo npcConfigs.
     * FORMAT: {name, chat, mapId, npcId, x, y [,iconId]}
     */
    protected void initNpc() {
        for (Object[] cfg : npcConfigs) {
            if (cfg.length < 6) continue;
            String name  = cfg[0] != null ? cfg[0].toString() : "";
            String chat  = cfg[1] != null ? cfg[1].toString() : "";
            int    mapId = ((Number) cfg[2]).intValue();
            short  npcId = ((Number) cfg[3]).shortValue();
            short  x     = ((Number) cfg[4]).shortValue();
            short  y     = ((Number) cfg[5]).shortValue();

            int iconId = (cfg.length > 6 && cfg[6] != null) ? ((Number) cfg[6]).intValue() : 1;
            int offset = (iconId >= 5000) ? (iconId - 5000) : iconId;
            int nFrame = 1;
            if (offset == 0) {
                nFrame = 2; // Garp / Boa Hancock / Sanji event placeholder image 5000 has 2 frames
            } else if (offset == 71) {
                nFrame = 2; // NPC Bóng Đá icon 5071 has 2 frames
            } else if (offset == 73) {
                nFrame = 2; // Chị Hằng icon 5073 has 2 frames
            } else if (map.MapTemplate.ENTRYS != null) {
                for (map.MapTemplate mt : map.MapTemplate.ENTRYS) {
                    if (mt.npcs != null) {
                        for (map.Npc n : mt.npcs) {
                            if (n != null && n.b3 == 0 && n.dataFrame != null && n.dataFrame.length >= 2) {
                                if ((n.dataFrame[0] & 0xFF) == offset) {
                                    nFrame = n.dataFrame[1] & 0xFF;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            byte[] dFrame = {(byte) offset, (byte) nFrame};

            MapTemplate tpl = null;
            if (MapTemplate.ENTRYS != null) {
                for (MapTemplate t : MapTemplate.ENTRYS) {
                    if (t != null && t.id == mapId) {
                        tpl = t;
                        break;
                    }
                }
            }
            if (tpl == null) continue;

            boolean exist = false;
            for (Npc n : tpl.npcs) {
                if (n.idmenu == npcId) {
                    exist = true;
                    n.name = name;
                    n.chat = chat;
                    n.x = x;
                    n.y = y;
                    n.wBlock = 0;
                    n.hBlock = 0;
                    n.dataFrame = dFrame;
                    break;
                }
            }
            if (!exist) {
                Npc npc       = new Npc();
                npc.idmenu    = npcId;
                npc.name      = name;
                npc.namegt    = "Sự kiện";
                npc.chat      = chat;
                npc.x         = x;
                npc.y         = y;
                npc.isPerson  = 1;
                npc.typeIcon  = -1;
                npc.wBlock    = 0;
                npc.hBlock    = 0;
                npc.b3        = 0;
                npc.dataFrame = dFrame;
                npc.wearing   = new short[0];
                tpl.npcs.add(npc);
            }
        }
    }

    public void clearNpc() {
        if (npcConfigs == null) return;
        for (Object[] cfg : npcConfigs) {
            if (cfg.length < 4) continue;
            int mapId = ((Number) cfg[2]).intValue();
            short npcId = ((Number) cfg[3]).shortValue();
            if (MapTemplate.ENTRYS != null) {
                for (MapTemplate t : MapTemplate.ENTRYS) {
                    if (t != null && t.id == mapId) {
                        t.npcs.removeIf(n -> n.idmenu == npcId);
                        break;
                    }
                }
            }
        }
    }

    // ======================== UTILITY ========================

    /** Tìm item trong shopItems theo id và cat (hoặc indexShop) */
    public MainItem findItem(int id, int cat) {
        if (shopItems == null || shopItems.isEmpty()) {
            if (eventShop != null && eventShop.getShopItems() != null && !eventShop.getShopItems().isEmpty()) {
                shopItems = new ArrayList<>(eventShop.getShopItems());
            }
        }
        if (shopItems == null) return null;
        for (MainItem item : shopItems) {
            if (item.indexShop == id && (cat == -1 || item.cat == cat)) return item;
        }
        for (MainItem item : shopItems) {
            if (item.indexShop == id) return item;
        }
        for (MainItem item : shopItems) {
            if (item.id == id && (cat == -1 || item.cat == cat)) return item;
        }
        for (MainItem item : shopItems) {
            if (item.id == id) return item;
        }
        return null;
    }

    /** Build chuỗi mô tả item hiển thị trong shop */
    private String buildItemInfo(MainItem item, EventData data, String defaultInfo) {
        StringBuilder sb = new StringBuilder();
        if (item.info != null && !item.info.isEmpty()) {
            sb.append(item.info);
        } else if (defaultInfo != null && !defaultInfo.isEmpty()) {
            sb.append(defaultInfo);
        }
        
        // 1. Hiển thị chỉ số (options) nếu có
        boolean hasOption = false;
        if (item.option1 != null && !item.option1.isEmpty()) {
            sb.append("\n\nChỉ số:");
            hasOption = true;
            for (template.Option op : item.option1) {
                String opName = "Thuộc tính " + op.id;
                if (template.ItemOptionTemplate.ENTRYS != null) {
                    for (template.ItemOptionTemplate t : template.ItemOptionTemplate.ENTRYS) {
                        if (t != null && t.id == op.id) {
                            opName = t.name.replace("#", String.valueOf(op.getParam()));
                            break;
                        }
                    }
                }
                sb.append("\n- ").append(opName);
            }
        }
        if (item.option2 != null && !item.option2.isEmpty()) {
            if (!hasOption) sb.append("\n\nChỉ số:");
            for (template.Option op : item.option2) {
                String opName = "Thuộc tính " + op.id;
                if (template.ItemOptionTemplate.ENTRYS != null) {
                    for (template.ItemOptionTemplate t : template.ItemOptionTemplate.ENTRYS) {
                        if (t != null && t.id == op.id) {
                            opName = t.name.replace("#", String.valueOf(op.getParam()));
                            break;
                        }
                    }
                }
                sb.append("\n- ").append(opName);
            }
        }

        // 2. Hiển thị số lượng nhận được
        if (item.num > 1) {
            sb.append("\n\nSố lượng nhận: x").append(item.num);
        }

        // 3. Hiển thị hạn sử dụng
        if (item.limitDay > 0) {
            sb.append("\n\nHạn sử dụng: ").append(item.limitDay).append(" ngày");
        } else if (item.time == -1 && (item.cat == 105 || item.cat == 110)) {
            sb.append("\n\nHạn sử dụng: Vĩnh viễn");
        }
        
        // 4. Hiển thị điều kiện mua / giá bán
        sb.append("\n\nĐiều kiện mua:");
        boolean hasReq = false;
        if (item instanceof itemz.MainItemShop) {
            itemz.MainItemShop mis = (itemz.MainItemShop) item;
            if (mis.priceBeri > 0) {
                sb.append("\n- ").append(core.ZUtil.number_format(mis.priceBeri)).append(" Beri");
                hasReq = true;
            }
            if (mis.priceRuby > 0) {
                sb.append("\n- ").append(core.ZUtil.number_format(mis.priceRuby)).append(" Ruby");
                hasReq = true;
            }
            if (mis.priceExtol > 0) {
                sb.append("\n- ").append(core.ZUtil.number_format(mis.priceExtol)).append(" Extol");
                hasReq = true;
            }
            if (mis.reqItems != null && !mis.reqItems.isEmpty()) {
                for (itemz.MainItemShop.ReqItem req : mis.reqItems) {
                    sb.append("\n- ").append(req.getName()).append(" x").append(req.num);
                    hasReq = true;
                }
            }
        }
        
        if (!hasReq) {
            if (costItemId >= 0 && item.price > 0) {
                String costName = itemz.MainItemShop.getItemName(costItemType, costItemId);
                sb.append("\n- x").append(item.price).append(" ").append(costName);
            } else if (item.price > 0) {
                sb.append("\n- ").append(core.ZUtil.number_format(item.price)).append(" Beri");
            } else {
                sb.append("\n- Miễn phí");
            }
        }
        
        // 5. Hiển thị lượt đổi còn lại
        if (item.limitNumBuy < 0) {
            sb.append("\n\nLượt đổi: Không giới hạn");
        } else if (item.indexShop >= 0 && item.indexShop < data.data.length) {
            sb.append("\n\nSố lượt đổi còn lại: ").append(data.data[item.indexShop]).append(" lần.");
        }
        return sb.toString();
    }

    protected EventData getEventData(Player p) {
        for (int i = 0; i < p.eventData.size(); i++) {
            if (p.eventData.get(i).eventID == id) return p.eventData.get(i);
        }
        return null;
    }

    public void showEventRankDirect(Player p, int subType) {
        try {
            p.typeBXH = subType;
            p.viewingEventId = this.id;
            zabstracts.AbsRanked bxh = zabstracts.AbsRanked.getBySubType(subType);
            if (bxh != null) {
                if (bxh.getCache().isEmpty()) bxh.update();
                bxh.show(p, 0);
            } else {
                rank.Ranked.sendEmptyRank(p, 7, "Sự Kiện " + name, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showEventRank(Player p) {
        try {
            if (p.typeBXH >= 0) {
                boolean match = (p.typeBXH == this.bxhSubType);
                if (!match && bxhSubTypes != null) {
                    for (int st : bxhSubTypes) {
                        if (st == p.typeBXH) {
                            match = true;
                            break;
                        }
                    }
                }
                if (match) {
                    showEventRankDirect(p, p.typeBXH);
                    return;
                }
            }

            if (bxhSubTypes != null && bxhSubTypes.length > 1) {
                p.menus.clear();
                for (int i = 0; i < bxhSubTypes.length; i++) {
                    final int st = bxhSubTypes[i];
                    final String bName = (bxhNames != null && i < bxhNames.length) ? bxhNames[i] : ("Bảng Xếp Hạng " + (i + 1));
                    p.menus.add(new model.Menu(bName, (short) 124, () -> {
                        showEventRankDirect(p, st);
                    }));
                }
                p.getService().openDynamicMenu(this.id, "Bảng Xếp Hạng - " + this.name, p.menus);
                return;
            }

            int subTypeToShow = this.bxhSubType;
            if (subTypeToShow < 0 && bxhSubTypes != null && bxhSubTypes.length > 0) {
                subTypeToShow = bxhSubTypes[0];
            }
            if (subTypeToShow >= 0) {
                showEventRankDirect(p, subTypeToShow);
            } else {
                rank.Ranked.sendEmptyRank(p, 7, "Sự Kiện " + name, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTichNap(Player p) throws IOException {
        p.typeShopTichNap = 3;
        p.typeShopTichNapSuKien = 3;
        p.refreshRecharge();
        zabstracts.AbsListTichNap tichNapEvent = zabstracts.AbsListTichNap.get(p.typeShopTichNapSuKien);
        if (tichNapEvent != null) {
            tichNapEvent.showTable(p);
        } else {
            p.getService().send_box_ThongBao_OK("Không tìm thấy sự kiện tích nạp!");
        }
    }

    public void showTichTieu(Player p) throws IOException {
        p.typeShopTichTieu = 4;
        p.typeShopTichTieuSuKien = 4;
        zabstracts.AbsTichTieuRuby tichTieuEvent = zabstracts.AbsTichTieuRuby.get(p.typeShopTichTieuSuKien);
        if (tichTieuEvent != null) {
            tichTieuEvent.showTable(p);
        } else {
            p.getService().send_box_ThongBao_OK("Không tìm thấy sự kiện tích tiêu!");
        }
    }

    public void sendMenu(Player p) {
        try {
            sendMenu(p, -100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHelpText() {
        return "SỰ KIỆN: " + (name != null ? name.toUpperCase() : "HẢI TẶC TÍ HON") + "\n\n"
                + "• Thời gian diễn ra sự kiện: " + time + "\n"
                + "• Chúc các thuyền trưởng có một mùa sự kiện thật nhiều niềm vui và phần thưởng giá trị!\b"
                + "THU THẬP NGUYÊN LIỆU:\n"
                + "- Đánh quái tại các bản đồ dã ngoại (+-10 cấp độ) để nhặt nguyên liệu sự kiện rơi ra.\n"
                + "- Tham gia các phó bản hàng ngày và tiêu diệt boss để nhận thêm nhiều nguyên liệu hiếm.\b"
                + "GHÉP VẬT PHẨM & TÍCH NẠP / TÍCH TIÊU:\n"
                + "- Dùng nguyên liệu thu thập được đến NPC Sự Kiện để chế tạo các món quà và trang phục giới hạn.\n"
                + "- Tích nạp và tích tiêu Ruby đạt các mốc để nhận thêm quà tặng độc quyền vô cùng giá trị!";
    }

    public void sendHelp(Player p) throws IOException {
        short npc = (short) (p.currentNpcId != 0 ? p.currentNpcId : -100);
        p.getService().Help_From_Server(npc, getHelpText());
    }

    // ======================== EVENT LUCKY WHEEL (MESSAGE 54) & TOP REWARDS ========================

    protected zabstracts.AbsVongQuay luckyWheel;

    public zabstracts.AbsVongQuay getLuckyWheel() {
        if (luckyWheel == null) {
            luckyWheel = new model.EventLuckyWheel(this.id, this.name, getSeasonKey());
        }
        return luckyWheel;
    }

    public void setLuckyWheel(zabstracts.AbsVongQuay wheel) {
        this.luckyWheel = wheel;
    }

    public void openLuckyWheel(Player p) throws IOException {
        zabstracts.AbsVongQuay vq = getLuckyWheel();
        if (vq != null) {
            vq.showTable(p);
        }
    }

    public void openLuckyWheel(Player p, int costRuby, int ticketItemId) throws IOException {
        zabstracts.AbsVongQuay vq = getLuckyWheel();
        if (vq instanceof model.EventLuckyWheel) {
            ((model.EventLuckyWheel) vq).setCostRuby(costRuby);
            ((model.EventLuckyWheel) vq).setTicketItemId(ticketItemId);
        }
        if (vq != null) {
            vq.showTable(p);
        }
    }

    public static void checkAndSendTopRankRewards(int bxhSubType) {
        try {
            zabstracts.AbsRanked bxh = zabstracts.AbsRanked.getBySubType(bxhSubType);
            if (bxh != null) {
                bxh.sendRewardToAllTop();
                System.out.println("[Event] Auto-sent TOP ranking rewards for BXH subType: " + bxhSubType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
