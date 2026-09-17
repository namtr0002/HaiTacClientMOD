package store;

import model.Player;
import network.Message;
import template.DataTemplate;
import template.ItemBag47;
import template.ItemTemplate4;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shop Đá — store_id = 7, item category = 4.
 *
 * Đặc điểm: Một số item trong shop là "gói đá" — khi mua item A thì thực ra
 * player nhận được item B với số lượng × 100 (ví dụ: mua id=272 -> nhận id=46, q=100).
 * Mapping này được định nghĩa tĩnh trong STONE_PACK_MAP bên dưới.
 *
 * Các item không có trong STONE_PACK_MAP được mua bình thường (1:1).
 */
public class StoneShop extends zabstracts.AbsShop {

    /**
     * Map: itemId trong store_data -> realItemId thực nhận được.
     * Khi mua 1 gói, player nhận realItemId với quant = 100 * num.
     *
     * Thêm/sửa ở đây khi cần cập nhật gói đá mới.
     */
    private static final Map<Integer, Integer> STONE_PACK_MAP;
    static {
        Map<Integer, Integer> m = new HashMap<>();
        m.put(272, 46);
        m.put(273, 52);
        m.put(274, 58);
        m.put(275, 64);
        m.put(276, 70);
        m.put(277, 76);
        STONE_PACK_MAP = Collections.unmodifiableMap(m);
    }

    private volatile List<StoreItem> cachedItems;

    public StoneShop() {
        super(111, "Shop Đá");
    }

    private List<StoreItem> getStoreItems() {
        List<StoreItem> items = new ArrayList<>(zabstracts.AbsShop.getItemsForStore(7));
        java.util.Set<Integer> existingIds = new java.util.HashSet<>();
        for (StoreItem si : items) {
            existingIds.add(si.itemId);
        }
        for (short sid : itemz.Rebuild_Item.ID_SELL) {
            if (!existingIds.contains((int) sid)) {
                ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(sid);
                int coin = it4 != null ? it4.beri : 0;
                int ruby = it4 != null ? it4.ruby : 0;
                items.add(new StoreItem(sid, 7, sid, 4, coin, ruby, -1, 0, -1));
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
            if (ItemTemplate4.get_it_by_id(displayId) != null) {
                validItems.add(item);
            }
        }
        Message m = new Message(-19);
        m.writer().writeByte(type);
        m.writer().writeUTF(name != null ? name : "Shop Đá");
        m.writer().writeByte(4); // category item4
        m.writer().writeShort(validItems.size());
        for (StoreItem item : validItems) {
            int displayId = (item.type2 > -1) ? item.type2 : item.itemId;
            m.writer().writeShort((short) displayId);
            m.writer().writeShort(1);
        }
        p.addmsg(m);
        m.cleanup();
    }

    @Override
    public void buy(Player p, byte cat, short id, int num) throws IOException {
        StoreItem found = null;
        for (StoreItem si : getStoreItems()) {
            int displayId = (si.type2 > -1) ? si.type2 : si.itemId;
            if (displayId == id || si.itemId == id) {
                found = si;
                break;
            }
        }

        if (found == null) {
            ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(id);
            if (it4 != null) {
                found = new StoreItem(id, 7, id, 4, it4.beri, it4.ruby, -1, 0, -1);
            }
        }

        if (found == null) {
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
        // Xác nhận item có trong shop
        StoreItem found = null;
        for (StoreItem si : getStoreItems()) {
            int displayId = (si.type2 > -1) ? si.type2 : si.itemId;
            if (displayId == id || si.itemId == id) {
                found = si;
                break;
            }
        }

        if (found == null) {
            ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(id);
            if (it4 != null) {
                found = new StoreItem(id, 7, id, 4, it4.beri, it4.ruby, -1, 0, -1);
            }
        }

        if (found == null) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Vật phẩm không tồn tại trong cửa hàng!");
            return;
        }

        int targetItemId = found.itemId;

        // Lấy template để đọc giá
        ItemTemplate4 itTemplate = ItemTemplate4.get_it_by_id(targetItemId);
        if (itTemplate == null) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy báo cho admin!");
            return;
        }

        // Xác định item thực sự sẽ nhận (gói đá hay đá thường)
        int realItemId;
        int realQuant;
        if (STONE_PACK_MAP.containsKey(targetItemId)) {
            realItemId = STONE_PACK_MAP.get(targetItemId);
            long totalQuant = 100L * num;
            if (totalQuant > Integer.MAX_VALUE) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Số lượng quá lớn!");
                return;
            }
            realQuant = (int) totalQuant;
        } else {
            realItemId = targetItemId;
            realQuant  = num;
        }

        // 1. Kiểm tra túi cho item thực sự
        if (!p.item.can_add_item_bag47(4, realItemId, realQuant)) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
            return;
        }

        // 2. Tính tiền cần trả tránh tràn số
        long totalCost;
        boolean useRuby = found.priceRuby > 0 || (found.priceCoin <= 0 && itTemplate.ruby > 0);
        int unitPrice = useRuby ? (found.priceRuby > 0 ? found.priceRuby : itTemplate.ruby)
                                : (found.priceCoin > 0 ? found.priceCoin : itTemplate.beri);
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
                p.update_TieuRuby(cost);
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

        // 3. Thêm item vào túi
        if (p.item.add_item_bag47(4, realItemId, realQuant)) {
            Message m22 = new Message(-64);
            m22.writer().writeUTF("Mua " + num);
            p.addmsg(m22);
            m22.cleanup();
        } else {
            // Hoàn tiền nếu thêm thất bại
            if (useRuby) {
                p.update_ngoc(cost);
                p.update_TieuRuby(-cost);
            } else {
                p.update_vang(cost);
            }
            p.updateMoney();
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Không thể mua với số lượng này");
            return;
        }

        p.item.updateInventory(false);
        p.updateMoney();
    }
}
