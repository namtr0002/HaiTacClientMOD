package model;

import core.Manager;
import network.Service;
import database.DbManager;
import network.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.GiftBox;
import template.ItemDauGia;
import template.ItemTemplate4;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hệ thống Đấu Giá (DauGia) — hoàn toàn không dùng bảng daugia.
 *
 * Cấu trúc JSON trong bảng settings (key_name = 'game_daugia'):
 * {
 *   "session": "MUA_1",
 *   "description": "Dữ liệu đấu giá",
 *   "items": {
 *     "0": {
 *       "id": 0,
 *       "itemID": 325,
 *       "priceStart": 2000,
 *       "priceNow": 34000,
 *       "minPricePlus": 2000,
 *       "minPriceDutDiem": 50000,
 *       "maxPriceDutDiem": 200000,
 *       "priceDutDiem": 60000,
 *       "name_own": "Luffy",
 *       "id_own": 12345,
 *       "time": 1742737047836,
 *       "session": "MUA_1",
 *       "isClose": false,
 *       "isClaimed": false,
 *       "listPrice": {
 *         "12345": 34000,
 *         "67890": 20000
 *       }
 *     }
 *   }
 * }
 *
 * Lịch sử đấu giá của người chơi được lưu vào bảng historys:
 *   type = "DAUGIA_<session>" (ví dụ: "DAUGIA_MUA_1")
 *   data = JSONArray các hành động: BID, BUYOUT, CLAIM, REFUND
 */
public class DauGia {
    /** Toàn bộ danh sách vật phẩm đấu giá */
    public static List<ItemDauGia> ENTRY = new ArrayList<>();
    /** Phiên đấu giá hiện tại (ví dụ: "MUA_1") */
    public static String SESSION = "";
    /** Danh sách người chơi đang mở giao diện đấu giá */
    public static List<Player> listP = new ArrayList<>();
    /** Cooldown tránh spam đấu giá */
    public static HashMap<String, Long> TimeJoin = new HashMap<>();

    // =====================================================================
    // LỌC VẬT PHẨM THEO PHIÊN (ẨN ITEM PHIÊN CŨ)
    // =====================================================================

    /**
     * Lấy danh sách vật phẩm của phiên hiện tại để hiển thị cho client.
     * Các item thuộc phiên cũ sẽ được ẩn đi nhưng vẫn bảo lưu trong cơ sở dữ liệu.
     */
    public static synchronized List<ItemDauGia> getActiveItems() {
        List<ItemDauGia> activeList = new ArrayList<>();
        for (ItemDauGia it : ENTRY) {
            if (it == null) continue;
            if (SESSION == null || SESSION.isEmpty() || it.session == null || it.session.isEmpty()
                    || it.session.equalsIgnoreCase(SESSION)) {
                activeList.add(it);
            }
        }
        return activeList;
    }

    /**
     * Lấy vật phẩm theo index hiển thị trên client.
     */
    public static synchronized ItemDauGia getItemByClientIndex(int clientIndex) {
        List<ItemDauGia> activeList = getActiveItems();
        if (clientIndex >= 0 && clientIndex < activeList.size()) {
            return activeList.get(clientIndex);
        }
        return null;
    }

    // =====================================================================
    // LOAD / SAVE TỪ SETTINGS (KEY: game_daugia)
    // =====================================================================

    /**
     * Tải dữ liệu đấu giá từ bảng settings.
     */
    @SuppressWarnings("unchecked")
    public static synchronized void loadFromSettings() {
        ENTRY = new ArrayList<>();
        SESSION = "";
        listP = new ArrayList<>();
        TimeJoin = new HashMap<>();
        String raw = readSettingsValue("game_daugia");
        if (raw == null || raw.trim().isEmpty() || raw.trim().equals("[]") || raw.trim().equals("{}")) {
            return;
        }
        try {
            Object parsed = JSONValue.parse(raw);
            if (parsed instanceof JSONObject) {
                JSONObject root = (JSONObject) parsed;
                if (root.containsKey("session")) {
                    SESSION = root.get("session").toString();
                }
                Object itemsObj = root.get("items");
                if (itemsObj instanceof JSONObject) {
                    JSONObject itemsJsonObj = (JSONObject) itemsObj;
                    for (Object k : itemsJsonObj.keySet()) {
                        Object v = itemsJsonObj.get(k);
                        if (v instanceof JSONObject) {
                            ItemDauGia it = parseItemFromJson((JSONObject) v);
                            if (it != null) ENTRY.add(it);
                        }
                    }
                } else if (itemsObj instanceof JSONArray) {
                    JSONArray itemsArr = (JSONArray) itemsObj;
                    for (Object obj : itemsArr) {
                        if (obj instanceof JSONObject) {
                            ItemDauGia it = parseItemFromJson((JSONObject) obj);
                            if (it != null) ENTRY.add(it);
                        }
                    }
                }
            } else if (parsed instanceof JSONArray) {
                // Fallback nếu lưu dưới dạng mảng
                JSONArray arr = (JSONArray) parsed;
                for (Object obj : arr) {
                    if (obj instanceof JSONObject) {
                        ItemDauGia it = parseItemFromJson((JSONObject) obj);
                        if (it != null) ENTRY.add(it);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[DauGia] loadFromSettings error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static ItemDauGia parseItemFromJson(JSONObject jo) {
        try {
            ItemDauGia it = new ItemDauGia();
            it.id              = jo.containsKey("id")              ? Integer.parseInt(jo.get("id").toString()) : 0;
            it.itemID          = Short.parseShort(jo.get("itemID").toString());
            it.priceStart      = jo.containsKey("priceStart")      ? Integer.parseInt(jo.get("priceStart").toString()) : 0;
            it.priceNow        = jo.containsKey("priceNow")        ? Integer.parseInt(jo.get("priceNow").toString()) : it.priceStart;
            it.minPricePlus    = jo.containsKey("minPricePlus")    ? Integer.parseInt(jo.get("minPricePlus").toString()) : 0;
            it.minPriceDutDiem = jo.containsKey("minPriceDutDiem") ? Integer.parseInt(jo.get("minPriceDutDiem").toString()) : 0;
            it.maxPriceDutDiem = jo.containsKey("maxPriceDutDiem") ? Integer.parseInt(jo.get("maxPriceDutDiem").toString()) : 0;
            it.priceDutDiem    = jo.containsKey("priceDutDiem")    ? Integer.parseInt(jo.get("priceDutDiem").toString()) : it.minPriceDutDiem;
            it.name_own        = jo.containsKey("name_own")        ? jo.get("name_own").toString() : "";
            it.id_own          = jo.containsKey("id_own")          ? Integer.parseInt(jo.get("id_own").toString()) : -1;
            it.time            = jo.containsKey("time")            ? Long.parseLong(jo.get("time").toString()) : 0L;
            it.session         = jo.containsKey("session")         ? jo.get("session").toString() : SESSION;
            it.isClose         = jo.containsKey("isClose") && Boolean.parseBoolean(jo.get("isClose").toString());
            it.isClaimed       = jo.containsKey("isClaimed") && Boolean.parseBoolean(jo.get("isClaimed").toString());
            it.listPrice       = new HashMap<>();

            if (jo.containsKey("listPrice")) {
                Object lpObj = jo.get("listPrice");
                if (lpObj instanceof JSONObject) {
                    JSONObject lpJson = (JSONObject) lpObj;
                    for (Object pk : lpJson.keySet()) {
                        try {
                            int pId = Integer.parseInt(pk.toString());
                            int pVal = Integer.parseInt(lpJson.get(pk).toString());
                            it.listPrice.put(pId, pVal);
                        } catch (Exception ignored) {}
                    }
                } else if (lpObj instanceof JSONArray) {
                    JSONArray lpArr = (JSONArray) lpObj;
                    for (Object pairObj : lpArr) {
                        if (pairObj instanceof JSONArray) {
                            JSONArray pair = (JSONArray) pairObj;
                            it.listPrice.put(Integer.parseInt(pair.get(0).toString()),
                                             Integer.parseInt(pair.get(1).toString()));
                        }
                    }
                }
            }
            return it;
        } catch (Exception e) {
            System.err.println("[DauGia] parseItemFromJson error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lưu toàn bộ trạng thái đấu giá vào bảng settings chuẩn JSONObjectKey.
     */
    @SuppressWarnings("unchecked")
    public static synchronized void saveSettings() {
        try {
            JSONObject root = new JSONObject();
            root.put("session", SESSION != null ? SESSION : "");
            root.put("description", "Dữ liệu đấu giá");

            JSONObject itemsObj = new JSONObject();
            for (int i = 0; i < ENTRY.size(); i++) {
                ItemDauGia it = ENTRY.get(i);
                if (it == null) continue;
                it.id = i;
                JSONObject jo = new JSONObject();
                jo.put("id",              it.id);
                jo.put("itemID",          (int) it.itemID);
                jo.put("priceStart",      it.priceStart);
                jo.put("priceNow",        it.priceNow);
                jo.put("minPricePlus",    it.minPricePlus);
                jo.put("minPriceDutDiem", it.minPriceDutDiem);
                jo.put("maxPriceDutDiem", it.maxPriceDutDiem);
                jo.put("priceDutDiem",    it.priceDutDiem);
                jo.put("name_own",        it.name_own != null ? it.name_own : "");
                jo.put("id_own",          it.id_own);
                jo.put("time",            it.time);
                jo.put("session",         it.session != null ? it.session : "");
                jo.put("isClose",         it.isClose);
                jo.put("isClaimed",       it.isClaimed);

                JSONObject lpObj = new JSONObject();
                for (Map.Entry<Integer, Integer> en : it.listPrice.entrySet()) {
                    lpObj.put(String.valueOf(en.getKey()), en.getValue());
                }
                jo.put("listPrice", lpObj);

                itemsObj.put(String.valueOf(i), jo);
            }
            root.put("items", itemsObj);

            writeSettingsValue("game_daugia", root.toJSONString());
        } catch (Exception e) {
            System.err.println("[DauGia] saveSettings error: " + e.getMessage());
        }
    }

    // =====================================================================
    // GIAO TIẾP VỚI CLIENT (PACKET -91)
    // =====================================================================

    /**
     * Gửi danh sách đấu giá phiên hiện tại cho người chơi.
     */
    public static void show_table(Player p) throws IOException {
        player_join(p);
        List<ItemDauGia> activeList = getActiveItems();

        Message m = new Message(-91);
        m.writer().writeByte(0);
        m.writer().writeByte(activeList.size());
        for (int i = 0; i < activeList.size(); i++) {
            ItemDauGia it = activeList.get(i);
            m.writer().writeByte(i);
            m.writer().writeInt(it.priceNow);
            int time_remain = (int) ((it.time - System.currentTimeMillis()) / 1000);
            if (time_remain < 0 || it.isClose) {
                time_remain = 0;
            }
            m.writer().writeInt(time_remain);
            // Giá chốt dứt điểm hiển thị tại cột "Giá chốt" và kích hoạt nút "Mua"
            m.writer().writeInt(it.priceDutDiem > 0 ? it.priceDutDiem : -1);

            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(it.itemID);
            m.writer().writeByte(4);
            m.writer().writeUTF(itemTemplate4 != null ? itemTemplate4.name : "Vật phẩm");
            m.writer().writeShort(itemTemplate4 != null ? itemTemplate4.icon : 0);
            m.writer().writeShort(1);
            m.writer().writeByte(0);
            // 1 nếu player đang là người giữ giá cao nhất (hiển thị nút đỏ)
            boolean isOwn = (p.name != null && p.name.equals(it.name_own)) || (p.IDPlayer == it.id_own);
            m.writer().writeByte(isOwn ? 1 : 0);
        }
        p.addmsg(m);
        m.cleanup();
        p.updateMoney();
    }

    /**
     * Xử lý gói tin từ client gửi lên (Packet -91).
     */
    public static synchronized void process(Player p, Message m) throws IOException {
        byte type = m.reader().readByte();
        byte clientIndex = -1;
        try {
            clientIndex = m.reader().readByte();
        } catch (IOException ignored) {}

        switch (type) {
            case 0: { // Yêu cầu mở bảng đấu giá
                if (clientIndex == -1) {
                    show_table(p);
                }
                break;
            }
            case 1: { // Người chơi bấm nút "Đấu Giá"
                if (p.getConnStatus() != 1) {
                    p.data_yesno = null;
                    p.map_tele = null;
                    p.getService().send_box_ThongBao_OK("Tài khoản chưa kích hoạt không thể tham gia đấu giá");
                    return;
                }
                ItemDauGia itSelect = getItemByClientIndex(clientIndex);
                if (itSelect == null) {
                    p.getService().send_box_ThongBao_OK("Không tìm thấy vật phẩm đấu giá!");
                    return;
                }
                if (itSelect.isClose || itSelect.time <= System.currentTimeMillis()) {
                    p.getService().send_box_ThongBao_OK("Đấu giá vật phẩm này đã kết thúc!");
                    return;
                }
                p.data_yesno = new int[]{17, clientIndex};
                int minBid = itSelect.priceNow + itSelect.minPricePlus;
                String title = "Đấu giá " + ItemTemplate4.get_item_name(itSelect.itemID);
                new model.InputDialog(p, 17, title, new String[]{
                    "Giá tối thiểu: " + minBid + " búa"
                }).startInput();
                break;
            }
            case 2: { // Người chơi bấm nút "Nhận" (nhận vật phẩm thắng cuộc)
                ItemDauGia itSelect = getItemByClientIndex(clientIndex);
                if (itSelect == null) {
                    p.getService().send_box_ThongBao_OK("Không tìm thấy vật phẩm đấu giá!");
                    return;
                }
                boolean isWinner = (p.name != null && p.name.equals(itSelect.name_own)) || (p.IDPlayer == itSelect.id_own);
                boolean isFinished = itSelect.isClose || itSelect.time <= System.currentTimeMillis();

                if (isFinished && isWinner && !itSelect.isClaimed) {
                    itSelect.isClaimed = true;
                    itSelect.listPrice.remove(p.IDPlayer);

                    List<GiftBox> listGift = new ArrayList<>();
                    GiftBox gb = new GiftBox();
                    ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(itSelect.itemID);
                    if (it4 != null) {
                        gb.id = it4.id;
                        gb.type = 4;
                        gb.name = it4.name;
                        gb.icon = it4.icon;
                        gb.num = 1;
                        gb.color = 0;
                        listGift.add(gb);
                    }
                    Service.send_gift(p, 0, "Phần thưởng Đấu giá", "Phần thưởng", listGift, true);
                    savePlayerHistory(p, "CLAIM", itSelect, itSelect.priceNow);
                    saveSettings();
                    p.getService().send_box_ThongBao_OK("Nhận phần thưởng đấu giá thành công!");
                } else if (itSelect.isClaimed) {
                    p.getService().send_box_ThongBao_OK("Vật phẩm đấu giá đã được nhận!");
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn chưa thể nhận vật phẩm này!");
                }
                break;
            }
            case 3: { // Người chơi bấm nút "Mua" (Chốt giá dứt điểm)
                if (p.getConnStatus() != 1) {
                    p.getService().send_box_ThongBao_OK("Tài khoản chưa kích hoạt không thể chốt giá");
                    return;
                }
                ItemDauGia itSelect = getItemByClientIndex(clientIndex);
                if (itSelect == null) {
                    p.getService().send_box_ThongBao_OK("Không tìm thấy vật phẩm đấu giá!");
                    return;
                }
                if (itSelect.isClose || itSelect.time <= System.currentTimeMillis()) {
                    p.getService().send_box_ThongBao_OK("Đấu giá vật phẩm này đã kết thúc!");
                    return;
                }
                if (itSelect.priceDutDiem <= 0) {
                    p.getService().send_box_ThongBao_OK("Vật phẩm này không hỗ trợ chốt giá dứt điểm!");
                    return;
                }
                buy_out(itSelect, p, clientIndex);
                break;
            }
            case 4: { // Đóng bảng đấu giá
                player_leave(p);
                break;
            }
        }
    }

    // =====================================================================
    // NGHIỆP VỤ ĐẶT GIÁ & CHỐT DỨT ĐIỂM
    // =====================================================================

    public synchronized static int get_price_now(ItemDauGia item) {
        return item != null ? item.priceNow : 0;
    }

    public synchronized static long get_time_join(String name) {
        if (name == null) return 0;
        if (TimeJoin.containsKey(name)) {
            return TimeJoin.get(name);
        }
        update_time_join(name);
        return 0;
    }

    public synchronized static void update_time_join(String name) {
        if (name != null && !Manager.gI().server_admin) {
            TimeJoin.put(name, System.currentTimeMillis() + 32_000L);
        }
    }

    /**
     * Đặt giá đấu mới.
     * - Nếu giá đặt >= giá dứt điểm (hoặc giá dứt điểm tối đa): tự động chốt dứt điểm ngay.
     * - Tự động tăng giá dứt điểm hiện tại theo mức cấu hình.
     */
    public synchronized static void set_new_value(ItemDauGia item, Player p, int value, int clientIndex)
            throws IOException {
        if (item == null || p == null) return;

        // Nếu giá đặt đạt hoặc vượt giá dứt điểm hiện tại -> kích hoạt chốt dứt điểm
        if (item.priceDutDiem > 0 && value >= item.priceDutDiem) {
            buy_out(item, p, clientIndex);
            return;
        }

        // Tính chênh lệch búa cần nộp thêm
        int alreadyBid = item.listPrice.getOrDefault(p.IDPlayer, 0);
        int needPay = value - alreadyBid;
        if (needPay < 0) needPay = 0;

        if (p.get_bua() < needPay) {
            p.getService().send_box_ThongBao_OK("Không đủ " + needPay + " búa trong hành trang để đặt giá!");
            return;
        }

        // Trừ búa và ghi nhận
        p.update_bua(-needPay);
        item.listPrice.put(p.IDPlayer, value);
        item.priceNow = value;
        item.name_own = p.name;
        item.id_own = p.IDPlayer;

        // Gia hạn thời gian nếu còn dưới 60 giây
        set_time(item);

        // Tăng giá dứt điểm nếu cấu hình maxPriceDutDiem > 0
        if (item.maxPriceDutDiem > 0) {
            long step = item.minPricePlus > 0 ? (long) item.minPricePlus : 1000L;
            long nextDutDiem = Math.max((long) item.priceDutDiem + step, (long) value + step);
            if (item.minPriceDutDiem > 0 && nextDutDiem < item.minPriceDutDiem) {
                nextDutDiem = item.minPriceDutDiem;
            }
            if (nextDutDiem > item.maxPriceDutDiem) {
                nextDutDiem = item.maxPriceDutDiem;
            }
            item.priceDutDiem = (int) Math.min(nextDutDiem, 2_000_000_000L);
        }

        // Lưu lịch sử đặt giá
        savePlayerHistory(p, "BID", item, value);

        // Broadcast cập nhật tới tất cả người chơi đang mở bảng
        broadcastUpdate(clientIndex, p.IDPlayer, item.priceNow, item.time);

        // Lưu vào bảng settings
        saveSettings();
    }

    /**
     * Mua / Chốt giá dứt điểm ngay lập tức.
     */
    public synchronized static void buy_out(ItemDauGia item, Player p, int clientIndex) throws IOException {
        if (item == null || p == null) return;

        int buyoutPrice = item.priceDutDiem > 0 ? item.priceDutDiem : item.priceNow;
        int alreadyBid = item.listPrice.getOrDefault(p.IDPlayer, 0);
        int needPay = buyoutPrice - alreadyBid;
        if (needPay < 0) needPay = 0;

        if (p.get_bua() < needPay) {
            p.getService().send_box_ThongBao_OK("Không đủ " + needPay + " búa để chốt giá dứt điểm!");
            return;
        }

        // Trừ búa
        p.update_bua(-needPay);
        item.listPrice.put(p.IDPlayer, buyoutPrice);
        item.priceNow = buyoutPrice;
        item.name_own = p.name;
        item.id_own = p.IDPlayer;
        item.isClose = true;
        item.time = System.currentTimeMillis();

        // Ghi nhận lịch sử
        savePlayerHistory(p, "BUYOUT", item, buyoutPrice);

        // Thông báo toàn server
        String itemName = ItemTemplate4.get_item_name(item.itemID);
        String notice = "Đấu giá " + itemName + " đã chốt dứt điểm! Người chiến thắng: "
                + p.name + " với giá " + buyoutPrice + " búa!";
        Manager.gI().chatKTG(0, notice, 5);

        // Broadcast bảng đấu giá (time = 0 thông báo hoàn tất)
        broadcastUpdate(clientIndex, p.IDPlayer, item.priceNow, item.time);

        // Lưu dữ liệu vào settings
        saveSettings();

        p.updateMoney();
        p.getService().send_box_ThongBao_OK("Chốt giá dứt điểm thành công " + itemName + "!");
    }

    public synchronized static void set_time(ItemDauGia item) {
        if (item == null) return;
        long time_remain = item.time - System.currentTimeMillis();
        if (time_remain < 60_000) {
            item.time = System.currentTimeMillis() + 60_000;
        }
    }

    private static void broadcastUpdate(int clientIndex, int playerId, int priceNow, long endTime) {
        Message m = new Message(-91);
        try {
            m.writer().writeByte(1);
            m.writer().writeByte(clientIndex);
            m.writer().writeShort((short) playerId);
            m.writer().writeInt(priceNow);
            int time_remain = (int) ((endTime - System.currentTimeMillis()) / 1000);
            if (time_remain < 0) time_remain = 0;
            m.writer().writeInt(time_remain);

            for (int i = 0; i < listP.size(); i++) {
                try {
                    Player p0 = listP.get(i);
                    if (p0 != null && p0.conn != null) {
                        p0.addmsg(m);
                    }
                } catch (Exception ignored) {}
            }
        } catch (IOException ignored) {
        } finally {
            try {
                m.cleanup();
            } catch (Exception ignored) {}
        }
    }

    // =====================================================================
    // VÒNG LẶP UPDATE (GỌI ĐỊNH KỲ MỖI GIÂY)
    // =====================================================================

    /**
     * Kiểm tra và đóng các phiên đấu giá hết thời gian, trao thưởng cho người ra giá cao nhất.
     */
    public synchronized static void update() throws IOException {
        boolean hasChange = false;
        long now = System.currentTimeMillis();
        for (ItemDauGia it : ENTRY) {
            if (it == null) continue;
            if (!it.isClose && it.time <= now) {
                it.isClose = true;
                hasChange = true;
                if (it.name_own != null && !it.name_own.isEmpty()) {
                    String notice = "Đấu giá " + ItemTemplate4.get_item_name(it.itemID)
                            + ". Người chiến thắng: " + it.name_own + " với số búa " + it.priceNow;
                    Manager.gI().chatKTG(0, notice, 5);
                }
            }
        }
        if (hasChange) {
            saveSettings();
        }
    }

    // =====================================================================
    // QUẢN LÝ NGƯỜI XEM BẢNG
    // =====================================================================

    private synchronized static void player_join(Player p) {
        if (p != null && !listP.contains(p)) {
            listP.add(p);
        }
    }

    public synchronized static void player_leave(Player p) {
        if (p != null) {
            listP.remove(p);
        }
    }

    // =====================================================================
    // HOÀN BÚA CHO NGƯỜI THUA
    // =====================================================================

    /**
     * Nhận lại búa đã đặt từ các vật phẩm đã kết thúc mà người chơi không thắng.
     */
    public synchronized static void getBuaBack(Player p) throws IOException {
        if (p == null) return;
        int total = 0;
        long now = System.currentTimeMillis();

        for (ItemDauGia it : ENTRY) {
            if (it == null) continue;
            boolean isEnded = it.isClose || it.time <= (now - 1_000);
            boolean isWinner = (p.name != null && p.name.equals(it.name_own)) || (p.IDPlayer == it.id_own);

            if (isEnded && !isWinner && it.listPrice.containsKey(p.IDPlayer)) {
                total += it.listPrice.remove(p.IDPlayer);
            }
        }

        if (total > 0) {
            p.update_bua(total);
            p.updateMoney();
            savePlayerHistory(p, "REFUND", null, total);
            saveSettings();
            p.getService().send_box_ThongBao_OK("Nhận về thành công " + total + " búa đấu giá!");
        } else {
            p.getService().send_box_ThongBao_OK("Bạn không có búa nào để nhận về!");
        }
    }

    // =====================================================================
    // LỊCH SỬ ĐẤU GIÁ CỦA NGƯỜI CHƠI (BẢNG historys)
    // =====================================================================

    @SuppressWarnings("unchecked")
    private static void savePlayerHistory(Player p, String action, ItemDauGia item, int amount) {
        if (p == null) return;
        try {
            String sessionKey = (item != null && item.session != null && !item.session.isEmpty())
                    ? item.session : (SESSION != null && !SESSION.isEmpty() ? SESSION : "GENERAL");
            String historyKey = "DAUGIA_" + sessionKey;

            String existingRaw = historys.HistoryManager.loadData(p.IDPlayer, historyKey);
            JSONArray arr;
            if (existingRaw != null && !existingRaw.trim().isEmpty()) {
                Object parsed = JSONValue.parse(existingRaw);
                arr = (parsed instanceof JSONArray) ? (JSONArray) parsed : new JSONArray();
            } else {
                arr = new JSONArray();
            }

            JSONObject entry = new JSONObject();
            entry.put("action",   action);
            entry.put("time",     System.currentTimeMillis());
            entry.put("session",  sessionKey);
            if (item != null) {
                entry.put("itemID",   (int) item.itemID);
                entry.put("itemName", ItemTemplate4.get_item_name(item.itemID));
            }
            entry.put("amount", amount);

            arr.add(entry);
            while (arr.size() > 50) {
                arr.remove(0);
            }

            int accId = (p.conn != null) ? p.conn.idUser : p.IDPlayer;
            historys.HistoryManager.saveData(accId, p.IDPlayer, historyKey, arr.toJSONString());
        } catch (Exception e) {
            System.err.println("[DauGia] savePlayerHistory error: " + e.getMessage());
        }
    }

    // =====================================================================
    // THAO TÁC CƠ SỞ DỮ LIỆU VỚI BẢNG settings
    // =====================================================================

    private static String readSettingsValue(String keyName) {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return null;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT `value` FROM `settings` WHERE `key_name` = ? LIMIT 1")) {
                ps.setString(1, keyName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("value");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[DauGia] readSettings error: " + e.getMessage());
        }
        return null;
    }

    private static void writeSettingsValue(String keyName, String value) {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return;
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE `settings` SET `value` = ? WHERE `key_name` = ?")) {
                ps.setString(1, value);
                ps.setString(2, keyName);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println("[DauGia] writeSettings error: " + e.getMessage());
        }
    }
}