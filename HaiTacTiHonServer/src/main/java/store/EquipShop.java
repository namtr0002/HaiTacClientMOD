package store;

import model.Player;
import core.Manager;
import network.Message;
import template.ItemTemplate3;
import template.ItemSell;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Shop trang bị (item category 3), store_id = 0..4 tương ứng với từng nghề.
 * Danh sách item được lọc theo level/10 group của player, tự động fallback sang ItemSell nếu store_data rỗng.
 */
public class EquipShop extends zabstracts.AbsShop {

    /** Constructor mặc định — dùng bởi reflection, không đăng ký shop nào */
    public EquipShop() {
        super(-1, "");
    }

    private EquipShop(int type, String name) {
        super(type, name);
    }

    @Override
    public List<zabstracts.AbsShop> getInstances() {
        return List.of(
            new EquipShop(0, "Shop Kiếm Sĩ"),
            new EquipShop(1, "Shop Hoa Tiêu"),
            new EquipShop(2, "Shop Xạ Thủ"),
            new EquipShop(3, "Shop Đầu Bếp"),
            new EquipShop(4, "Shop Chiến Sĩ")
        );
    }

    // ------------------------------------------------------------------ helpers

    /** Lấy toàn bộ item của shop này từ store_data */
    private List<StoreItem> getAllItems() {
        List<StoreItem> items = zabstracts.AbsShop.getItemsForStore(type);
        return items != null ? items : new ArrayList<>();
    }

    /**
     * Lọc item theo level/10 group của player.
     * - level group < 2  -> hiển thị group 0-2
     * - level group > 7  -> hiển thị group 7-9
     * - còn lại          -> hiển thị group [pLvGrp-1, pLvGrp+1]
     */
    private List<StoreItem> getFilteredItems(Player p) {
        int pLvGrp = (p != null) ? p.level / 10 : 0;
        int minGrp, maxGrp;
        if (pLvGrp < 2) {
            minGrp = 0; maxGrp = 2;
        } else if (pLvGrp > 7) {
            minGrp = 7; maxGrp = 9;
        } else {
            minGrp = pLvGrp - 1; maxGrp = pLvGrp + 1;
        }

        List<StoreItem> result = new ArrayList<>();
        for (StoreItem item : getAllItems()) {
            int itemGrp = item.reqLevel;
            if (itemGrp < 0) {
                ItemTemplate3 it3 = ItemTemplate3.get_it_by_id(item.itemId);
                if (it3 != null) itemGrp = it3.level / 10;
            } else if (itemGrp >= 10) {
                itemGrp = itemGrp / 10;
            }
            if (itemGrp >= minGrp && itemGrp <= maxGrp) {
                result.add(item);
            }
        }

        // Nếu store_data không có item nào phù hợp -> Fallback sang danh sách chuẩn ItemSell
        if (result.isEmpty() && p != null) {
            List<ItemSell> list_id_sell = ItemSell.get_it_sell(p.level, type);
            if (list_id_sell != null) {
                for (ItemSell is : list_id_sell) {
                    ItemTemplate3 it = ItemTemplate3.get_it_by_id(is.id);
                    if (it != null) {
                        result.add(new StoreItem(is.id, type, is.id, 3, is.price > 0 ? is.price : it.beri, it.ruby, -1, 0, -1));
                    }
                }
            }
        }
        return result;
    }

    // ------------------------------------------------------------------ Shop interface

    @Override
    public void openUI(Player p) throws IOException {
        p.isShopSk = false;
        p.typeShop = type;
        List<StoreItem> listSell = getFilteredItems(p);
        List<StoreItem> validItems = new ArrayList<>();
        for (StoreItem si : listSell) {
            int displayId = (si.type2 > -1) ? si.type2 : si.itemId;
            if (ItemTemplate3.get_it_by_id(displayId) != null) {
                validItems.add(si);
            }
        }
        Message m = new Message(-19);
        m.writer().writeByte(type);
        m.writer().writeUTF(Manager.NAME_ITEM_SELL_TEMP[type]);
        m.writer().writeByte(3); // category item3
        m.writer().writeShort(validItems.size());
        for (StoreItem si : validItems) {
            int displayId = (si.type2 > -1) ? si.type2 : si.itemId;
            ItemTemplate3 it = ItemTemplate3.get_it_by_id(displayId);
            ItemTemplate3.readUpdateItem(m.writer(), it);
            if (si.priceRuby > 0) {
                m.writer().writeByte(1); // 1 = ruby
                m.writer().writeInt(si.priceRuby);
            } else {
                m.writer().writeByte(0); // 0 = beri
                m.writer().writeInt(si.priceCoin);
            }
        }
        p.addmsg(m);
        m.cleanup();
    }

    @Override
    public void buy(Player p, byte cat, short id, int num) throws IOException {
        List<StoreItem> listSell = getAllItems();
        StoreItem found = null;
        for (StoreItem si : listSell) {
            int displayId = (si.type2 > -1) ? si.type2 : si.itemId;
            if (displayId == id || si.itemId == id) {
                found = si;
                break;
            }
        }

        if (found == null) {
            List<ItemSell> list_id_sell = ItemSell.get_it_sell(p.level, type);
            if (list_id_sell != null) {
                for (ItemSell is : list_id_sell) {
                    if (is.id == id) {
                        ItemTemplate3 it = ItemTemplate3.get_it_by_id(is.id);
                        if (it != null) {
                            found = new StoreItem(is.id, type, is.id, 3, is.price > 0 ? is.price : it.beri, it.ruby, -1, 0, -1);
                            break;
                        }
                    }
                }
            }
        }

        if (found == null) {
            ItemTemplate3 it = ItemTemplate3.get_it_by_id(id);
            if (it != null) {
                found = new StoreItem(id, type, id, 3, it.beri, it.ruby, -1, 0, -1);
            }
        }

        if (found == null) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Mua thất bại, hãy thử lại!");
            return;
        }

        if (p.item.able_bag() <= 0) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
            return;
        }

        boolean useRuby = found.priceRuby > 0;
        int cost = useRuby ? found.priceRuby : found.priceCoin;
        if (cost <= 0) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Vật phẩm chưa bán!");
            return;
        }

        if (useRuby) {
            if (p.get_ngoc() < cost) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Bạn không đủ " + cost + " ruby!");
                return;
            }
            p.update_ngoc(-cost);
        } else {
            if (p.get_vang() < cost) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Bạn không đủ " + cost + " beri!");
                return;
            }
            p.update_vang(-cost);
        }
        p.updateMoney();

        int realItemId = found.itemId;
        template.Item_wear itAdd = new template.Item_wear();
        itAdd.setup_template_by_id(realItemId);
        if (itAdd.template == null) {
            // Hoàn tiền nếu item không tồn tại
            if (useRuby) {
                p.update_ngoc(cost);
            } else {
                p.update_vang(cost);
            }
            p.updateMoney();
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy báo cho admin!");
            return;
        }

        p.item.add_item_bag3(itAdd);
        p.item.updateInventory(false);

        p.getService().end_Dialog();

        ItemTemplate3 realIt = ItemTemplate3.get_it_by_id(realItemId);
        String name = (realIt != null) ? realIt.name : ("" + realItemId);

        Message m22 = new Message(-64);
        m22.writer().writeUTF("Mua 1");
        p.addmsg(m22);
        m22.cleanup();
    }
}
