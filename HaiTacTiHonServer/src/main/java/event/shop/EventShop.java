package event.shop;

import event.Event;
import itemz.MainItem;
import itemz.MainItemShop;
import model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp trừu tượng cơ sở cho toàn bộ các Shop Sự Kiện riêng biệt.
 * Mỗi sự kiện kế thừa lớp này để định nghĩa danh sách vật phẩm, giá cả và điều kiện đổi quà.
 */
public abstract class EventShop {
    protected final Event event;
    protected final List<MainItemShop> shopItems = new ArrayList<>();

    public EventShop(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public List<MainItemShop> getShopItems() {
        return shopItems;
    }

    /**
     * Khởi tạo danh sách vật phẩm mặc định của Shop sự kiện riêng.
     */
    public abstract void initShop();

    public MainItemShop findItem(int index, int cat) {
        if (shopItems == null) return null;
        for (MainItemShop item : shopItems) {
            if (item.indexShop == index) return item;
        }
        for (MainItemShop item : shopItems) {
            if (item.id == index && item.cat == cat) return item;
        }
        for (MainItemShop item : shopItems) {
            if (item.id == index) return item;
        }
        return null;
    }

    public void openShop(Player p) {
        if (event != null) {
            event.openShop(p);
        }
    }

    public void buyShop(Player p, int index, int cat) throws IOException {
        if (event != null) {
            event.buyShop(p, index, cat);
        }
    }

    public void buyShopWithQuantity(Player p, MainItem item, int quantity) throws IOException {
        if (event != null) {
            event.buyShopWithQuantity(p, item, quantity);
        }
    }
}

