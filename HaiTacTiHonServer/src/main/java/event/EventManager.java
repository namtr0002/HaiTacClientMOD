package event;

import network.Service;

import model.Player;
import mob.Mob;
import map.Zone;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * EventManager — Dispatcher duy nhất cho mọi tương tác sự kiện.
 *
 * CODE BÊN NGOÀI CHỈ GỌI QUA EventManager.dispatch*() — không import bất kỳ subclass nào.
 *
 * API DISPATCH:
 *   dispatchShop(p, -1)          -> mở shop
 *   dispatchShop(p, index)       -> mua slot index
 *   dispatchHandleMenu(p, id, i) -> xử lý menu NPC
 *   dispatchUseItem(p, itemId)   -> dùng item sự kiện (spawn/bắt mob, dùng vé...)
 *   dispatchOnMobKilled(p, mob)  -> quái chết
 *   dispatchSendMobsToPlayer(p, zone)
 *   dispatchGetMobInMap(zone)
 *   dispatchCanSellItem(p, id)
 *   dispatchGetSellItemList()
 *   dispatchInitPlayerData(data, eventId)
 *   dispatchResetDailyData(data, eventId)
 *   dispatchUpdate(h, m, s)
 *   isActive(eventId)
 *   activeId()
 */
public class EventManager {

    // ======================== SINGLETON ========================

    // [FIX BUG-003] volatile + double-checked locking để đảm bảo thread-safe singleton
    private static volatile EventManager instance;
    private final java.util.concurrent.ConcurrentHashMap<Integer, Event> events = new java.util.concurrent.ConcurrentHashMap<>();
    private final Map<Integer, Class<? extends Event>> eventRegistry = new java.util.concurrent.ConcurrentHashMap<>();
    public volatile Event active;
    private final Map<Integer, Event> activeEvents = new java.util.concurrent.ConcurrentHashMap<>();

    private EventManager() {
        registerEvents();
    }

    // [FIX BUG-003] Double-checked locking với volatile
    public static EventManager gI() {
        if (instance == null) {
            synchronized (EventManager.class) {
                if (instance == null) instance = new EventManager();
            }
        }
        return instance;
    }

    // ======================== REGISTRATION ========================

    private void registerEvents() {
        eventRegistry.clear();
        List<Class<?>> classes = core.ZUtil.getClasses("event");
        for (Class<?> clazz : classes) {
            if (Event.class.isAssignableFrom(clazz) && !clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                @SuppressWarnings("unchecked")
                Class<? extends Event> eventClazz = (Class<? extends Event>) clazz;
                register(eventClazz);
            }
        }
    }

    private void register(Class<? extends Event> clazz) {
        try {
            java.lang.reflect.Constructor<? extends Event> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Event instance = constructor.newInstance();
            eventRegistry.put(instance.getId(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerEvent(Event event) {
        events.put(event.getId(), event);
    }

    public void init(int activeEventId) {
        reloadConfigs();
    }

    public void setActive(int eventId) {
        Event ev = getEvent(eventId);
        if (ev != null) {
            try { 
                ev.init(); 
                activeEvents.put(eventId, ev);
                active = ev;
                core.Manager.gI().ZEVENT_ID = eventId;
            }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public static void setEventActive(int eventId, boolean active) {
        if (active) {
            gI().setActive(eventId);
        } else {
            Event ev = gI().activeEvents.remove(eventId);
            if (ev != null) {
                try {
                    ev.clearNpc();
                    if (ev.shouldCleanupEventItems()) {
                        ev.checkAndTriggerOfflineItemCleanup();
                        for (network.Session ss : network.SessionManager.CLIENT_ENTRYS) {
                            if (ss != null && ss.p != null) {
                                ev.checkAndRemoveEventItems(ss.p);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (gI().active != null && gI().active.getId() == eventId) {
                gI().active = gI().activeEvents.isEmpty() ? null : gI().activeEvents.values().iterator().next();
            }
        }
    }

    public Event getActiveEvent()        { return active; }

    public java.util.Collection<Event> getActiveEventsList() {
        return activeEvents.values();
    }

    public Event getEvent(int id) {
        Event event = events.get(id);
        if (event == null) {
            Class<? extends Event> clazz = eventRegistry.get(id);
            if (clazz != null) {
                try {
                    java.lang.reflect.Constructor<? extends Event> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    event = constructor.newInstance();
                    events.put(id, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return event;
    }

    public static Event get(int id) {
        return gI().getEvent(id);
    }

    // ======================== STATIC HELPERS ========================

    /** Event đang active có ID này không? */
    public static boolean isActive(int eventId) {
        return gI().activeEvents.containsKey(eventId);
    }

    public static boolean isEventActive(int eventId) {
        return isActive(eventId);
    }

    /** ID của event đang active, -1 nếu không có */
    public static int activeId() {
        return (gI().active != null) ? gI().active.getId() : -1;
    }

    /** Kiểm tra xem hiện tại có bất kỳ sự kiện nào đang hoạt động không */
    public static boolean hasActiveEvent() {
        return (gI().active != null && gI().active.getId() > 0) || !gI().activeEvents.isEmpty();
    }

    public static class EventConfigEntry {
        public int eventId;
        public int isActive;
        public String time = "";
        public String timeend = "";
        public String timex2pay = "";
        public String timechangeitem = "";
        public String timedropitem = "";
        public String timeremoveitem = "";
        public String season = "";
    }

    public static List<EventConfigEntry> parseEventConfig(String rawConfig) {
        List<EventConfigEntry> list = new ArrayList<>();
        if (rawConfig == null || rawConfig.trim().isEmpty()) {
            return list;
        }

        String trimmed = rawConfig.trim();
        // JSON fallback if rawConfig starts with [ or {
        if (trimmed.startsWith("[") || trimmed.startsWith("{")) {
            try {
                Object parsed = JSONValue.parse(trimmed);
                if (parsed instanceof JSONArray) {
                    JSONArray arr = (JSONArray) parsed;
                    for (Object item : arr) {
                        if (!(item instanceof JSONObject)) continue;
                        JSONObject obj = (JSONObject) item;
                        Object eventIdObj = obj.get("event_id");
                        if (eventIdObj == null) continue;
                        EventConfigEntry entry = new EventConfigEntry();
                        entry.eventId = Integer.parseInt(eventIdObj.toString().trim());

                        int isActive = 0;
                        Object actObj = obj.get("is_active");
                        if (actObj != null) {
                            if (actObj instanceof Boolean) {
                                isActive = ((Boolean) actObj) ? 1 : 0;
                            } else {
                                try {
                                    isActive = Integer.parseInt(actObj.toString().trim());
                                } catch (Exception ex) {
                                    if ("true".equalsIgnoreCase(actObj.toString().trim())) isActive = 1;
                                }
                            }
                        }
                        entry.isActive = isActive;
                        entry.time = obj.containsKey("time") && obj.get("time") != null ? obj.get("time").toString().trim() : "";
                        entry.timeend = obj.containsKey("timeend") && obj.get("timeend") != null ? obj.get("timeend").toString().trim() : "";
                        entry.timex2pay = obj.containsKey("timex2pay") && obj.get("timex2pay") != null ? obj.get("timex2pay").toString().trim() : "";
                        entry.timechangeitem = obj.containsKey("timechangeitem") && obj.get("timechangeitem") != null ? obj.get("timechangeitem").toString().trim() : "";
                        entry.timedropitem = obj.containsKey("timedropitem") && obj.get("timedropitem") != null ? obj.get("timedropitem").toString().trim() : "";
                        entry.timeremoveitem = obj.containsKey("timeremoveitem") && obj.get("timeremoveitem") != null ? obj.get("timeremoveitem").toString().trim() : "";
                        if (obj.containsKey("key") && obj.get("key") != null) {
                            entry.season = obj.get("key").toString().trim();
                        } else if (obj.containsKey("season") && obj.get("season") != null) {
                            entry.season = obj.get("season").toString().trim();
                        } else {
                            entry.season = "";
                        }
                        list.add(entry);
                    }
                    return list;
                }
            } catch (Exception e) {
                System.err.println("[EventManager] JSON fallback parsing error: " + e.getMessage());
            }
        }

        // Parse line-by-line args format: event_id,is_active,time,timedropitem,timechangeitem,timeremoveitem,timex2pay,key
        String[] lines = trimmed.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) {
                continue;
            }
            int hashIdx = line.indexOf('#');
            if (hashIdx >= 0) {
                line = line.substring(0, hashIdx).trim();
            }
            if (line.isEmpty()) continue;

            String[] parts = line.split(",", -1);
            if (parts.length < 2 && line.contains("|")) {
                parts = line.split("\\|", -1);
            }
            if (parts.length >= 2) {
                try {
                    EventConfigEntry entry = new EventConfigEntry();
                    entry.eventId = Integer.parseInt(parts[0].trim());
                    String actStr = parts[1].trim();
                    entry.isActive = ("1".equals(actStr) || "true".equalsIgnoreCase(actStr)) ? 1 : 0;
                    entry.time = parts.length > 2 ? parts[2].trim() : "";
                    entry.timedropitem = parts.length > 3 ? parts[3].trim() : "";
                    entry.timechangeitem = parts.length > 4 ? parts[4].trim() : "";
                    entry.timeremoveitem = parts.length > 5 ? parts[5].trim() : "";
                    entry.timex2pay = parts.length > 6 ? parts[6].trim() : "";
                    if (parts.length > 7) entry.season = parts[7].trim();
                    if (parts.length > 8) entry.timeend = parts[8].trim();
                    list.add(entry);
                } catch (Exception ex) {
                    System.err.println("[EventManager] Error parsing event_config line: '" + line + "' -> " + ex.getMessage());
                }
            }
        }
        return list;
    }

    public void reloadConfigs() {
        try (java.sql.Connection conn = database.DbManager.gI().getConnect()) {
            if (conn == null) {
                System.err.println("[EventManager] Failed to get connection to load settings/event_config.");
                return;
            }

            String eventConfigRaw = null;

            // Load general configs first (like exp_rate) & get event_config args/json
            try (java.sql.PreparedStatement psConf = conn.prepareStatement("SELECT `id`, `key_name`, `value` FROM `settings`")) {
                try (java.sql.ResultSet rsConf = psConf.executeQuery()) {
                    while (rsConf.next()) {
                        int rowId = rsConf.getInt("id");
                        String key = rsConf.getString("key_name");
                        String val = rsConf.getString("value");
                        if ("exp_rate".equalsIgnoreCase(key)) {
                            try {
                                core.Manager.gI().exp = Integer.parseInt(val);
                            } catch (Exception ignored) {}
                        }
                        if ("event_config".equalsIgnoreCase(key) || rowId == 22) {
                            if (val != null && !val.trim().isEmpty()) {
                                eventConfigRaw = val;
                            }
                        }
                    }
                }
            }

            java.util.Map<Integer, Event> newActiveEvents = new java.util.HashMap<>();

            // Parse event_config from settings table (id = 22 / key_name = 'event_config')
            if (eventConfigRaw != null && !eventConfigRaw.trim().isEmpty()) {
                try {
                    List<EventConfigEntry> entries = parseEventConfig(eventConfigRaw);

                    // Group all entries by eventId to support duplicate event IDs (multiple seasons/schedules)
                    java.util.Map<Integer, List<EventConfigEntry>> grouped = new java.util.LinkedHashMap<>();
                    for (EventConfigEntry entry : entries) {
                        grouped.computeIfAbsent(entry.eventId, k -> new ArrayList<>()).add(entry);
                    }

                    for (java.util.Map.Entry<Integer, List<EventConfigEntry>> mapEntry : grouped.entrySet()) {
                        int eventId = mapEntry.getKey();
                        List<EventConfigEntry> eventEntries = mapEntry.getValue();
                        Event ev = getEvent(eventId);
                        if (ev == null) {
                            core.Log.warn("EventManager", "Event ID " + eventId + " is configured in settings but no matching Event class registered. Skipping.");
                            continue;
                        }

                        EventConfigEntry chosenEntry = null;
                        boolean isChosenActive = false;

                        // 1. Prioritize an entry that is active and currently within its time window
                        for (EventConfigEntry entry : eventEntries) {
                            if (entry.isActive == 1) {
                                boolean hasAnyTimeConfig = !entry.time.isEmpty() || !entry.timechangeitem.isEmpty() || !entry.timedropitem.isEmpty() || !entry.timex2pay.isEmpty() || !entry.timeremoveitem.isEmpty();
                                boolean isTimeActive = !hasAnyTimeConfig
                                        || ev.checkTimeActive(entry.time, entry.timeend)
                                        || ev.checkTimeActive(entry.timechangeitem, entry.timeend)
                                        || ev.checkTimeActive(entry.timedropitem, entry.timeend)
                                        || ev.checkTimeActive(entry.timex2pay, entry.timeend)
                                        || ev.checkTimeActive(entry.timeremoveitem, entry.timeend);

                                if (isTimeActive) {
                                    chosenEntry = entry;
                                    isChosenActive = true;
                                    break;
                                }
                            }
                        }

                        // 2. If none is currently active, pick the first configured entry to load config metadata
                        if (chosenEntry == null && !eventEntries.isEmpty()) {
                            chosenEntry = eventEntries.get(0);
                        }

                        if (chosenEntry != null) {
                            String oldSeasonKey = ev.getSeasonKey();
                            ev.loadConfig(chosenEntry.time, chosenEntry.timeend, chosenEntry.timex2pay, chosenEntry.timechangeitem, chosenEntry.timedropitem, chosenEntry.timeremoveitem, chosenEntry.season);

                            // Validate event configuration before adding to newActiveEvents
                            if (isChosenActive) {
                                boolean isValid = validateEvent(ev);
                                if (isValid) {
                                    if (activeEvents.containsKey(eventId) && oldSeasonKey != null && !oldSeasonKey.equals(ev.getSeasonKey())) {
                                        core.Log.info("EventManager", "Season transition for active event " + ev.getName() + " (ID " + eventId + ") from [" + oldSeasonKey + "] to [" + ev.getSeasonKey() + "]. Re-initializing.");
                                        try {
                                            ev.clearNpc();
                                            ev.init();
                                        } catch (Exception ex) {
                                            core.Log.error("EventManager", "Error re-initializing event " + ev.getName() + " (ID " + eventId + "): " + ex.getMessage());
                                            ex.printStackTrace();
                                        }
                                    }
                                    newActiveEvents.put(eventId, ev);
                                } else {
                                    core.Log.error("EventManager", "Validation failed for event " + ev.getName() + " (ID " + eventId + "). Event will remain deactivated without fallback dummy data.");
                                }
                            }
                            ev.checkAndTriggerOfflineItemCleanup();
                        }
                    }
                } catch (Exception e) {
                    core.Log.error("EventManager", "Error parsing event_config from settings: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                core.Log.warn("EventManager", "Warning: event_config in settings table is empty.");
            }

            // Deactivate events that are no longer active: clear NPCs safely
            for (Integer oldId : activeEvents.keySet()) {
                if (!newActiveEvents.containsKey(oldId)) {
                    Event ev = activeEvents.get(oldId);
                    if (ev != null) {
                        try {
                            ev.clearNpc();
                            core.Log.info("EventManager", "Event deactivated: " + ev.getName() + " (ID: " + oldId + ") - NPCs cleared");
                        } catch (Exception ex) {
                            core.Log.error("EventManager", "Error clearing NPCs for deactivated event " + ev.getName() + " (ID: " + oldId + "): " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }
            }

            // Activate new events: call init() to setup NPC, shop items, etc.
            for (Integer newId : newActiveEvents.keySet()) {
                if (!activeEvents.containsKey(newId)) {
                    Event ev = newActiveEvents.get(newId);
                    if (ev != null) {
                        try {
                            ev.init();
                            core.Log.info("EventManager", "Event activated & initialized: " + ev.getName() + " (ID: " + newId + ") [Season: " + ev.getSeasonKey() + "]");
                        } catch (Exception ex) {
                            core.Log.error("EventManager", "Error initializing event " + ev.getName() + " (ID " + newId + "): " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }
            }

            activeEvents.clear();
            activeEvents.putAll(newActiveEvents);

            // Tự động xóa vật phẩm của sự kiện khi và chỉ khi đã qua thời gian xóa (shouldCleanupEventItems == true)
            for (Integer regId : eventRegistry.keySet()) {
                Event ev = getEvent(regId);
                if (ev != null && ev.shouldCleanupEventItems()) {
                    String cleanupKey = "OFFLINE_CLEANUP_EVENT_" + ev.id + "_" + ev.getSeasonKey();
                    if (!historys.HistoryManager.hasSystemLog(cleanupKey)) {
                        ev.checkAndTriggerOfflineItemCleanup();
                        for (network.Session ss : network.SessionManager.CLIENT_ENTRYS) {
                            if (ss != null && ss.p != null) {
                                ev.checkAndRemoveEventItems(ss.p);
                            }
                        }
                    }
                }
            }

            // Backward compatibility fields
            if (!activeEvents.isEmpty()) {
                active = activeEvents.values().iterator().next();
                core.Manager.gI().ZEVENT_ID = active.getId();
            } else {
                active = null;
                core.Manager.gI().ZEVENT_ID = -1;
            }
        } catch (Exception e) {
            core.Log.error("EventManager", "Error loading configurations from DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Pre-runtime validation for Event components:
     * Validates shop items, cost tokens, currency, and parameters before committing to runtime.
     */
    public static boolean validateEvent(Event ev) {
        if (ev == null) return false;
        try {
            if (ev.getId() <= 0 || ev.getName() == null || ev.getName().trim().isEmpty()) {
                core.Log.error("EventManager", "Invalid event ID or Name: " + ev.getName() + " (ID: " + ev.getId() + ")");
                return false;
            }
            // Check cost item if configured
            if (ev.costItemId >= 0) {
                if (ev.costItemType == 4) {
                    if (template.ItemTemplate4.get_it_by_id(ev.costItemId) == null && ev.costItemId > 2) {
                        core.Log.warn("EventManager", "Event " + ev.getName() + " cost item 4 ID " + ev.costItemId + " not found in ItemTemplate4.");
                    }
                } else if (ev.costItemType == 7) {
                    if (template.ItemTemplate7.get_it_by_id(ev.costItemId) == null) {
                        core.Log.warn("EventManager", "Event " + ev.getName() + " cost item 7 ID " + ev.costItemId + " not found in ItemTemplate7.");
                    }
                }
            }
            return true;
        } catch (Exception e) {
            core.Log.error("EventManager", "Exception validating event " + ev.getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ======================== DISPATCH METHODS ========================

    /**
     * Mở UI shop của event đang active hoặc event theo id.
     * Menu NPC gọi: EventManager.dispatchOpenShop(p) hoặc EventManager.dispatchOpenShop(p, eventId)
     */
    public static void dispatchOpenShop(Player p) throws IOException {
        Event ev = (p != null && p.typeShop > 0) ? gI().getEvent(p.typeShop) : null;
        if (ev != null && gI().isEventActive(ev.getId())) {
            ev.openShop(p);
        } else if (gI().active != null) {
            gI().active.openShop(p);
        }
    }

    public static void dispatchOpenShop(Player p, int eventId) throws IOException {
        Event ev = gI().getEvent(eventId);
        if (ev != null) {
            ev.openShop(p);
        } else if (gI().active != null) {
            gI().active.openShop(p);
        }
    }

    /**
     * Xử lý mua item từ shop event.
     * Service.buy_item gọi khi TypeShop==118 và p.typeShop > 0.
     * @param index  indexShop (client gửi và trước đó số id gửi lên là index)
     * @param cat    loại item (4, 7, 105, 110...)
     */
    public static void dispatchBuyShop(Player p, int index, int cat) throws IOException {
        Event ev = gI().getEvent(p.typeShop);
        if (ev != null) ev.buyShop(p, index, cat);
    }
    /**
     * Xử lý menu NPC sự kiện.
     * Thay thế toàn bộ if/else if (event==1) trong MenuController.
     */
    public static boolean dispatchHandleMenu(Player p, int menuId, int index) throws IOException {
        for (Event ev : gI().getActiveEventsList()) {
            if (ev.handleMenu(p, menuId, index)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Xử lý packet CMD_EVENT (cmd 47) từ client.
     */
    public static boolean dispatchCmdEvent(Player p, byte action) throws IOException {
        for (Event ev : gI().getActiveEventsList()) {
            if (ev.handleCmdEvent(p, action)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gửi menu NPC sự kiện.
     * Gọi khi người chơi click vào NPC sự kiện.
     * @return true nếu event đã xử lý (MenuController không cần xử lý tiếp)
     */
    public static boolean dispatchSendMenu(Player p, int npcId) throws IOException {
        for (Event ev : gI().getActiveEventsList()) {
            if (ev.sendMenu(p, npcId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dùng item sự kiện — spawn mob, bắt mob, dùng vé...
     * Thay thế: dispatchSpawnMobAt + dispatchCatchMob
     * @return true nếu event đã xử lý (UseItem không cần xử lý tiếp)
     */
    public static boolean dispatchUseItem(Player p, int itemId) throws IOException {
        for (Event ev : gI().getActiveEventsList()) {
            if (ev.onUseItem(p, itemId)) {
                return true;
            }
        }
        return false;
    }

    // ======================== ACTIVITY DISPATCHERS ========================

    public static void dispatchOnLienTang(Player p) {
        for (Event ev : gI().getActiveEventsList()) {
            ev.onLienTang(p);
        }
    }

    public static void dispatchOnLienTang(Player p, int floor) {
        for (Event ev : gI().getActiveEventsList()) {
            ev.onLienTang(p, floor);
        }
    }

    public static void dispatchOnVanChuyen(Player p) {
        for (Event ev : gI().getActiveEventsList()) {
            ev.onVanChuyen(p);
        }
    }

    public static void dispatchOnNhiemVuLap(Player p) {
        for (Event ev : gI().getActiveEventsList()) {
            ev.onNhiemVuLap(p);
        }
    }

    public static void dispatchOnVuonCam(Player p, int round) {
        for (Event ev : gI().getActiveEventsList()) {
            ev.onVuonCam(p, round);
        }
    }

    public static void dispatchOnSieuLienTang(Player p) {
        for (Event ev : gI().getActiveEventsList()) {
            ev.onSieuLienTang(p);
        }
    }

    public static void dispatchOnPvP(Player p) {
        for (Event ev : gI().getActiveEventsList()) {
            ev.onPvP(p);
        }
    }

    public static void dispatchOnWanted(Player p) {
        for (Event ev : gI().getActiveEventsList()) {
            ev.onWanted(p);
        }
    }

    public static void dispatchOnTrainMob(Player p) {
        for (Event ev : gI().getActiveEventsList()) {
            ev.onTrainMob(p);
        }
    }

    /**
     * Quái/Boss chết — drop, thưởng sự kiện.
     * Thay thế: inline code trong Zone.java / Mob.java
     */
    public static void dispatchOnMobKilled(Player p, Mob mob) {
        if (p != null) {
            p = p.getOwnerPlayer();
        }
        for (Event ev : gI().getActiveEventsList()) {
            ev.onMobKilled(p, mob);
        }
    }

    /**
     * Gửi danh sách mob sự kiện tới player khi vào map.
     * Zone.java gọi khi player enter zone.
     */
    public static void dispatchSendMobsToPlayer(Player p, Zone zone) throws IOException {
        for (Event ev : gI().getActiveEventsList()) {
            ev.sendMobsToPlayer(p, zone);
        }
    }

    /**
     * Lấy mob sự kiện trong map (scan boss gần nhất).
     * Service.java gọi khi cần tìm boss gần.
     */
    public static Mob dispatchGetMobInMap(Zone zone) {
        for (Event ev : gI().getActiveEventsList()) {
            Mob mob = ev.getMobInMap(zone);
            if (mob != null) return mob;
        }
        return null;
    }

    /**
     * Item có được bán trong shop thường khi event chạy không?
     * ItemSell.java gọi trước khi cho phép bán.
     */
    public static boolean dispatchCanSellItem(Player p, int id) {
        for (Event ev : gI().getActiveEventsList()) {
            if (ev.canSellItem(p, id)) return true;
        }
        return false;
    }

    /**
     * Danh sách item thêm vào shop thường khi event chạy.
     * ItemSell.java dùng để build danh sách bán.
     */
    public static short[] dispatchGetSellItemList() {
        java.util.List<Short> list = new ArrayList<>();
        for (Event ev : gI().getActiveEventsList()) {
            short[] items = ev.getSellItemList();
            if (items != null) {
                for (short item : items) {
                    list.add(item);
                }
            }
        }
        short[] res = new short[list.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    /**
     * Khởi tạo EventData khi player login với event đang chạy.
     * Player.java gọi khi load player data.
     * @param eventId  typeEvent của data (để tìm đúng event handler)
     */
    public static void dispatchInitPlayerData(EventData data, int eventId) {
        Event ev = gI().events.get(eventId);
        if (ev != null) ev.initPlayerData(data);
    }

    /**
     * Reset EventData hàng ngày.
     * Player.java gọi khi detect ngày mới.
     */
    public static void dispatchResetDailyData(EventData data, int eventId) {
        Event ev = gI().events.get(eventId);
        if (ev != null) ev.resetDailyData(data);
    }

    /**
     * Kiểm tra và xóa vật phẩm sự kiện hết hạn cho người chơi khi đăng nhập.
     */
    public static void dispatchCheckAndRemoveEventItems(Player p) {
        for (Integer id : gI().eventRegistry.keySet()) {
            Event ev = gI().getEvent(id);
            if (ev != null) {
                ev.checkAndRemoveEventItems(p);
            }
        }
    }

    /**
     * Tick định kỳ event từ ServerEventManager.
     * Thay thế: SuKienHe.gI().pokemonInit() mỗi giây/phút.
     */
    public static void dispatchUpdate(int hour, int min, int sec) {
        for (Event ev : gI().getActiveEventsList()) {
            try { ev.update(hour, min, sec); }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * Dispatch general map/dungeon drops dynamically to all active events.
     * Tự động phân giải chính xác Class phó bản từ Zone hoặc Dungeon Object.
     */
    public static void dispatchMapDrop(Object dungeonOrZone, java.util.List<template.GiftBox> listGift, int floorIndex) {
        if (dungeonOrZone == null || listGift == null) return;
        Class<?> mapClass = null;
        if (dungeonOrZone instanceof Zone) {
            Zone z = (Zone) dungeonOrZone;
            mapClass = z.getDungeonClass();
            if (mapClass == null) {
                mapClass = z.getClass();
            }
        } else if (dungeonOrZone instanceof Class<?>) {
            mapClass = (Class<?>) dungeonOrZone;
        } else {
            mapClass = dungeonOrZone.getClass();
        }

        for (Event ev : gI().getActiveEventsList()) {
            if (ev == null || !ev.isDropItemActive()) continue;
            java.util.List<Event.EventMapDrop> mapDrops = ev.getMapDrops(mapClass, floorIndex);
            for (Event.EventMapDrop drop : mapDrops) {
                if (drop == null) continue;
                if (core.ZUtil.random(100) < drop.rate) {
                    if (drop.itemType == 3) {
                        template.ItemTemplate3 it3 = template.ItemTemplate3.get_it_by_id(drop.itemId);
                        if (it3 != null) {
                            listGift.add(new template.GiftBox(it3, drop.itemNum, drop.options));
                        }
                    } else {
                        template.GiftBox.addGift(listGift, drop.itemType, drop.itemId, drop.itemNum);
                    }
                }
            }
        }
    }

    public static void dispatchMapDrop(Zone zone, java.util.List<template.GiftBox> listGift, int floorIndex) {
        dispatchMapDrop((Object) zone, listGift, floorIndex);
    }

    /**
     * Dispatch boss drops dynamically cho Boss Ngoài (World Boss, Super Boss).
     * Append trực tiếp phần thưởng sự kiện vào list_gift của Boss.
     */
    public static void dispatchBossDrop(zabstracts.AbsBoss boss, Player pKill, java.util.List<template.GiftBox> listGift) {
        if (boss == null || listGift == null) return;
        for (Event ev : gI().getActiveEventsList()) {
            if (ev == null || !ev.isDropItemActive()) continue;
            java.util.List<Event.EventBossDrop> bossDrops = ev.getBossDrops(boss.id);
            for (Event.EventBossDrop drop : bossDrops) {
                if (drop == null) continue;
                if (core.ZUtil.random(100) < drop.rate) {
                    if (drop.itemType == 3) {
                        template.ItemTemplate3 it3 = template.ItemTemplate3.get_it_by_id(drop.itemId);
                        if (it3 != null) {
                            listGift.add(new template.GiftBox(it3, drop.itemNum, drop.options));
                        }
                    } else {
                        template.GiftBox.addGift(listGift, drop.itemType, drop.itemId, drop.itemNum);
                    }
                }
            }
        }
    }
}
