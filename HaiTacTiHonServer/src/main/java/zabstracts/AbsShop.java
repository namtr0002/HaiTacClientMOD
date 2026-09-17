package zabstracts;

import model.Player;
import store.StoreItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Base abstract class for all shops (AbsShop).
 */
public abstract class AbsShop {
    public static final List<StoreItem> storeItems = new CopyOnWriteArrayList<>();
    private static final java.util.Map<Integer, AbsShop> shops = new java.util.HashMap<>();

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        shops.clear();

        try {
            // DaThanThoaiShop (type 116)
            register(new store.DaThanThoaiShop());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // ClanShop (type 110)
            register(new store.ClanShop());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Register parameterized limited shops
        register(new store.LimitShop(8, "Cửa Hàng Coin", 8, "shoptichluy", "Coin"));
        store.LimitShop tichLuyShop = new store.LimitShop(118, "Shop Tích Lũy", 12, "shoptichluy", "Điểm Sự Kiện");
        register(tichLuyShop);
        shops.put(12, tichLuyShop);
    }

    public static void register(AbsShop shop) {
        shops.put(shop.getType(), shop);
    }

    public List<AbsShop> getInstances() {
        return Collections.singletonList(this);
    }

    public static List<StoreItem> getItemsForStore(int storeId) {
        List<StoreItem> result = new ArrayList<>();
        for (StoreItem item : storeItems) {
            if (item.storeId == storeId) {
                result.add(item);
            }
        }
        return result;
    }

    public static AbsShop get(int type) {
        return shops.get(type);
    }

    protected final int type;
    protected final String name;
    protected String key;

    public String getSeasonKey() {
        return key != null ? key : "Shop_" + type;
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    protected AbsShop(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static int getPurchasedCount(int playerId, String tableName, byte cat, short id) {
        return historys.ShopLimitHistory.getPurchasedCount(playerId, tableName, cat, id);
    }

    public static void logPurchase(int accountId, int playerId, String tableName, byte cat, short id) {
        historys.ShopLimitHistory.logPurchase(accountId, playerId, tableName, cat, id);
    }

    public static void logPurchaseBatch(int accountId, int playerId, String tableName, byte cat, short id, int quantity) {
        historys.ShopLimitHistory.logPurchaseBatch(accountId, playerId, tableName, cat, id, quantity);
    }

    public abstract void openUI(Player p) throws IOException;
    public abstract void buy(Player p, byte cat, short id, int num) throws IOException;
}
