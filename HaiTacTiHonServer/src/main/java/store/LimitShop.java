package store;

import model.Player;
import core.Manager;
import historys.zLog;
import network.Message;
import template.ItemFashion;
import template.ItemFashionP2;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.ShopLimitItemTemplate;

import java.io.IOException;
import java.util.List;

/**
 * Concrete shop class handling shoptichluy limited/accumulated shops.
 */
public class LimitShop extends zabstracts.AbsShop implements zinterfaces.IShopInit {
    private final String tableName;
    private final String pointsName;
    private final int typeShopVal;

    public LimitShop() {
        super(-1, "");
        this.typeShopVal = -1;
        this.tableName = "";
        this.pointsName = "";
    }

    public LimitShop(int type, String name, int typeShopVal, String tableName, String pointsName) {
        super(type, name);
        this.typeShopVal = typeShopVal;
        this.tableName = tableName;
        this.pointsName = pointsName;
        this.key = "LIMIT_SHOP_" + tableName + "_" + type;
    }

    @Override
    public List<ShopLimitItemTemplate> initItems() {
        List<ShopLimitItemTemplate> list = new java.util.ArrayList<>();
        list.add(createItem(10, 7, 2000, "Khiên", 100));
        list.add(createItem(158, 4, 1000, "Rương Đại Ác Quỷ", 9999));
        list.add(createItem(324, 4, 500000, "Đá khảm vô cực", 3));
        list.add(createItem(910, 4, 400000, "Đá khảm siêu cấp", 3));
        list.add(createItem(349, 4, 5000, "Túi beri nhận ngẫu nhiên 30-50m", 9999));
        list.add(createItem(754, 4, 1000000, "Haki bá vương", 1));
        list.add(createItem(866, 4, 10000, "Vé Vòng Quay Sự Kiện", 9999));
        return list;
    }

    private static ShopLimitItemTemplate createItem(int id, int type, int point, String info, int limit) {
        ShopLimitItemTemplate temp = new ShopLimitItemTemplate();
        temp.id = (short) id;
        temp.type = (byte) type;
        temp.point = point;
        temp.info = info;
        temp.limit = limit;
        temp.limit_data = new java.util.HashMap<>();
        return temp;
    }

    public List<ShopLimitItemTemplate> getItems() {
        return Manager.shoptichluy_entry;
    }

    public int getPlayerPoints(Player p) throws IOException {
        if (typeShopVal == 8) {
            return p.get_gcoin();
        } else {
            return p.pointEvent1;
        }
    }

    public void updatePlayerPoints(Player p, int amount) throws IOException {
        if (typeShopVal == 8) {
            p.update_gcoin(amount);
        } else if (typeShopVal == 12) {
            p.update_pointEvent1(amount);
        } else {
            p.update_point_event(amount);
        }
    }

    public String getPointsName() {
        return pointsName;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public void openUI(Player p) throws IOException {
        p.isShopSk = false;
        p.typeShop = typeShopVal;

        List<ShopLimitItemTemplate> items = getItems();

        Message m = new Message(-19);
        m.writer().writeByte(type);
        m.writer().writeUTF(name);
        m.writer().writeByte(11);
        m.writer().writeShort(items.size());

        for (ShopLimitItemTemplate item : items) {
            m.writer().writeShort(item.id);
            m.writer().writeByte(item.type);

            String name = "";
            short icon = 0;
            String info = "";
            switch (item.type) {
                case 4: {
                    ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(item.id);
                    if (it4 != null) {
                        name = it4.name;
                        icon = it4.icon;
                        template.ItemTemplate4_Info infoTemp = template.ItemTemplate4_Info.get_by_id(it4.indexInfoPotion);
                        if (infoTemp != null) {
                            info = infoTemp.info;
                        }
                    }
                    break;
                }
                case 7: {
                    ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(item.id);
                    if (it7 != null) {
                        name = it7.name;
                        icon = it7.icon;
                        info = "Nguyên liệu nâng cấp";
                    }
                    break;
                }
                case 105: {
                    ItemFashion itf = ItemFashion.get_item(item.id);
                    if (itf != null) {
                        name = itf.name;
                        icon = itf.idIcon;
                        info = "Thời Trang";
                    }
                    break;
                }
            }
            if (item.info != null && !item.info.trim().isEmpty()) {
                info = item.info.trim();
            }
            info = sanitizeInfo(info);

            if (name.isEmpty()) {
                name = "Vật phẩm " + item.id;
            }
            m.writer().writeUTF(name);
            m.writer().writeShort(icon);

            StringBuilder fullInfo = new StringBuilder(info);
            if (fullInfo.length() > 0) fullInfo.append("\n\n");
            fullInfo.append("Giá: ").append(core.ZUtil.number_format(item.point)).append(" ").append(getPointsName());
            if (item.limit > 0) {
                int purchased = getPurchasedCount(p.IDPlayer, getTableName(), item.type, item.id);
                fullInfo.append("\nĐổi tối đa: ").append(item.limit)
                        .append(" lần.\nHiện tại đã đổi: ").append(purchased)
                        .append("/").append(item.limit).append(".");
            }
            m.writer().writeUTF(fullInfo.toString());
        }
        p.addmsg(m);
        m.cleanup();
    }

    private static String sanitizeInfo(String s) {
        if (s == null) return "";
        int tagIdx = s.indexOf("</USER_REQUEST>");
        if (tagIdx != -1) {
            s = s.substring(0, tagIdx);
        }
        tagIdx = s.indexOf("<ADDITIONAL_METADATA>");
        if (tagIdx != -1) {
            s = s.substring(0, tagIdx);
        }
        tagIdx = s.indexOf("<USER_REQUEST>");
        if (tagIdx != -1) {
            s = s.substring(0, tagIdx);
        }
        return s.trim();
    }

    @Override
    public void buy(Player p, byte cat, short id, int num) throws IOException {
        List<ShopLimitItemTemplate> items = getItems();
        ShopLimitItemTemplate target = null;
        for (ShopLimitItemTemplate item : items) {
            if ((cat == -1 || item.type == cat) && item.id == id) {
                target = item;
                break;
            }
        }

        if (target == null) {
            p.getService().send_box_ThongBao_OK("Vật phẩm không tồn tại trong cửa hàng!");
            return;
        }

        String itemName = "";
        switch (target.type) {
            case 4 -> {
                ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(target.id);
                itemName = (it4 != null) ? it4.name : ("Vật phẩm " + target.id);
            }
            case 7 -> {
                ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(target.id);
                itemName = (it7 != null) ? it7.name : ("Nguyên liệu " + target.id);
            }
            case 105 -> {
                ItemFashion itf = ItemFashion.get_item(target.id);
                itemName = (itf != null) ? itf.name : ("Thời trang " + target.id);
            }
            default -> itemName = "Vật phẩm " + target.id;
        }

        if (target.type == 105) {
            buyWithQuantity(p, target, 1);
            return;
        }

        if (num == 20) {
            final ShopLimitItemTemplate fTarget = target;
            new model.InputDialog(p, 999, "Nhập số lượng", new String[]{"Số lượng muốn đổi (1 - 9999):"}, inputs -> {
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
                    buyWithQuantity(p, fTarget, qty);
                } catch (NumberFormatException e) {
                    p.getService().end_Dialog();
                    p.getService().send_box_ThongBao_OK("Vui lòng nhập số hợp lệ!");
                } catch (Exception e) {
                    p.getService().end_Dialog();
                    e.printStackTrace();
                }
            }).startInput();
            return;
        }

        buyWithQuantity(p, target, num > 0 ? num : 1);
    }

    public void buyWithQuantity(Player p, ShopLimitItemTemplate target, int quantity) throws IOException {
        if (p == null || target == null || quantity < 1 || quantity > 9999) {
            if (p != null) p.getService().end_Dialog();
            return;
        }

        byte cat = target.type;
        short id = target.id;

        // Limit check
        int purchased = 0;
        if (target.limit > 0) {
            purchased = getPurchasedCount(p.IDPlayer, getTableName(), cat, id);
            if (purchased >= target.limit) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Bạn đã hết lượt đổi vật phẩm này");
                return;
            }
            if (purchased + quantity > target.limit) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Bạn chỉ còn " + (target.limit - purchased) + " lượt đổi vật phẩm này!");
                return;
            }
        }

        // Inventory check
        int totalItemAmount = (cat == 4 && id == 221) ? 10 * quantity : quantity;
        if (cat == 105) {
            if (p.item.able_bag() < 1) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                return;
            }
        } else {
            if (!p.item.can_add_item_bag47(cat, id, totalItemAmount)) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                return;
            }
        }

        // Currency check
        long totalPrice = (long) target.point * quantity;
        if (totalPrice > Integer.MAX_VALUE || getPlayerPoints(p) < totalPrice) {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Không đủ " + core.ZUtil.number_format(totalPrice) + " " + getPointsName());
            return;
        }

        // Specific fashion ownership check
        if (cat == 105) {
            ItemFashion itf = ItemFashion.get_item(id);
            if (itf != null && p.check_fashion(itf.ID) != null) {
                p.getService().end_Dialog();
                p.getService().send_box_ThongBao_OK("Đã sở hữu thời trang này rồi!");
                return;
            }
        }

        // Deduct points
        updatePlayerPoints(p, (int) -totalPrice);

        // Add item
        boolean success = false;
        switch (cat) {
            case 4: {
                if (ItemTemplate4.get_it_by_id(id) != null) {
                    p.item.add_item_bag47(4, id, totalItemAmount);
                    p.item.updateInventory(false);
                    success = true;
                }
                break;
            }
            case 7: {
                if (ItemTemplate7.get_it_by_id(id) != null) {
                    p.item.add_item_bag47(7, id, quantity);
                    p.item.updateInventory(false);
                    success = true;
                }
                break;
            }
            case 105: {
                ItemFashion itf = ItemFashion.get_item(id);
                if (itf != null) {
                    ItemFashionP2 temp2 = new ItemFashionP2();
                    temp2.id = itf.ID;
                    p.fashion.add(temp2);
                    p.update_fashionP2(temp2);

                    if (p.map != null && p.map.players != null) {
                        for (int i = 0; i < p.map.players.size(); i++) {
                            Player p0 = p.map.players.get(i);
                            if (p0 != null && p0.getService() != null) {
                                p0.getService().charWearing(p, false);
                            }
                        }
                    }
                    p.getService().UpdateInfoMaincharInfo();
                    p.item.updateInventory(false);
                    success = true;
                }
                break;
            }
        }

        if (success) {
            // Log purchase batch efficiently
            logPurchaseBatch(p.conn != null ? p.conn.idUser : 0, p.IDPlayer, getTableName(), cat, id, quantity);

            p.updateMoney();
            Message m22 = new Message(-64);
            m22.writer().writeUTF("Đổi thành công x" + quantity);
            p.addmsg(m22);
            m22.cleanup();
            zLog.gI().add_log(p, "SHOP_LIMIT_BUY", "buyItem:" + id + " | cat:" + cat + " | qty:" + quantity);
            p.getService().end_Dialog();
        } else {
            p.getService().end_Dialog();
            p.getService().send_box_ThongBao_OK("Chưa mở quy đổi vật phẩm này");
        }
    }
}
