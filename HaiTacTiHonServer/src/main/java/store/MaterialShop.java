package store;

import model.Player;
import network.Message;
import template.DataTemplate;
import template.ItemTemplate7;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shop Nguyên liệu — store_id = 5, item category = 7 (ItemTemplate7).
 *
 * Protocol client readUpdateMaterial():
 *   readByte()  -> itemId  (byte, tối đa 127 — đúng với id7 hiện tại)
 *   readShort() -> quantity
 *
 * Giá: it7.priceruby > 0 -> trừ ruby; còn lại trừ beri.
 */
public class MaterialShop extends zabstracts.AbsShop {

    private volatile List<StoreItem> cachedItems;

    public MaterialShop() {
        super(6, "Shop Nguyên liệu");
    }

    private List<StoreItem> getStoreItems() {
        List<StoreItem> items = zabstracts.AbsShop.getItemsForStore(5);
        if (items == null || items.isEmpty()) {
            items = new ArrayList<>();
            byte[] defaultSell = template.ItemSell.get_it_sell_material();
            if (defaultSell != null) {
                for (byte id : defaultSell) {
                    ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(id);
                    int coin = it7 != null ? it7.price : 0;
                    int ruby = it7 != null ? it7.priceruby : 0;
                    items.add(new StoreItem(id, 5, id, 7, coin, ruby, -1, 0, -1));
                }
            }
        }
        return items;
    }

    @Override
    public void openUI(Player p) throws IOException {
        p.isShopSk = false;
        p.typeShop = type;
        List<StoreItem> items = getStoreItems();
        List<StoreItem> validItems = new ArrayList<>();
        for (StoreItem item : items) {
            int displayId = (item.type2 > -1) ? item.type2 : item.itemId;
            if (ItemTemplate7.get_it_by_id(displayId) != null) {
                validItems.add(item);
            }
        }
        Message m = new Message(-19);
        m.writer().writeByte(type);
        m.writer().writeUTF(name != null ? name : "Shop Nguyên liệu");
        m.writer().writeByte(7);           // category item7
        m.writer().writeShort(validItems.size());
        for (StoreItem item : validItems) {
            int displayId = (item.type2 > -1) ? item.type2 : item.itemId;
            m.writer().writeByte((byte) displayId);  // client đọc readByte()
            m.writer().writeShort(1);                 // quantity hiển thị
        }
        p.addmsg(m);
        m.cleanup();
    }

    @Override
    public void buy(Player p, byte cat, short id, int num) throws IOException {
        StoreItem found = null;
        for (StoreItem item : getStoreItems()) {
            int displayId = (item.type2 > -1) ? item.type2 : item.itemId;
            if (displayId == id || item.itemId == id) {
                found = item;
                break;
            }
        }

        if (found == null) {
            ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(id);
            if (it7 != null) {
                found = new StoreItem(id, 5, id, 7, it7.price, it7.priceruby, -1, 0, -1);
            }
        }

        if (found == null) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Vật phẩm không tồn tại trong cửa hàng!");
            return;
        }

        buyWithQuantity(p, (short) id, num > 0 ? num : 1);
    }

    public void buyWithQuantity(Player p, short id, int num) throws IOException {
        if (p == null || num < 1 || num > 9999) {
            if (p != null) p.getService().end_Dialog();
            return;
        }
        StoreItem found = null;
        for (StoreItem item : getStoreItems()) {
            int displayId = (item.type2 > -1) ? item.type2 : item.itemId;
            if (displayId == id || item.itemId == id) {
                found = item;
                break;
            }
        }

        if (found == null) {
            ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(id);
            if (it7 != null) {
                found = new StoreItem(id, 5, id, 7, it7.price, it7.priceruby, -1, 0, -1);
            }
        }

        if (found == null) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Vật phẩm không tồn tại trong cửa hàng!");
            return;
        }

        int realItemId = found.itemId;

        // Kiểm tra túi
        if (!p.item.can_add_item_bag47(7, realItemId, num)) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
            return;
        }

        ItemTemplate7 it = ItemTemplate7.get_it_by_id(realItemId);
        if (it == null) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy báo cho admin!");
            return;
        }

        // Tính tiền tránh tràn số
        long totalCost;
        boolean useRuby = found.priceRuby > 0 || (found.priceCoin <= 0 && it.priceruby > 0);
        int unitPrice = useRuby ? (found.priceRuby > 0 ? found.priceRuby : it.priceruby)
                                : (found.priceCoin > 0 ? found.priceCoin : it.price);
        totalCost = (long) unitPrice * num;
        if (totalCost < 0 || totalCost > Integer.MAX_VALUE) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Số lượng quá lớn, không thể thực hiện giao dịch!");
            return;
        }
        int cost = (int) totalCost;

        if (useRuby) {
            if (cost > 0 && p.get_ngoc() < cost) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Không đủ " + cost + " ruby");
                return;
            }
            if (cost > 0) {
                p.update_ngoc(-cost);
            }
        } else {
            if (cost > 0 && p.get_vang() < cost) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Không đủ " + cost + " beri");
                return;
            }
            if (cost > 0) {
                p.update_vang(-cost);
            }
        }

        p.item.add_item_bag47(7, realItemId, num);
        p.item.updateInventory(false);
        p.updateMoney();

        Message m22 = new Message(-64);
        m22.writer().writeUTF("Mua " + num);
        p.addmsg(m22);
        m22.cleanup();
    }
}
