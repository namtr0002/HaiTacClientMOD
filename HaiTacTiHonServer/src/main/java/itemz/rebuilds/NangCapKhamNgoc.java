package itemz.rebuilds;

import zabstracts.AbsUpgrade;
import model.Player;
import template.Item_wear;
import itemz.Rebuild_Item;
import network.Message;
import template.ItemTemplate4;
import java.io.IOException;

public class NangCapKhamNgoc extends AbsUpgrade {

    private static NangCapKhamNgoc instance;

    public NangCapKhamNgoc() {
        instance = this;
        AbsUpgrade.register(this);
    }

    public static NangCapKhamNgoc getInstance() {
        if (instance == null) {
            instance = new NangCapKhamNgoc();
        }
        return instance;
    }

    public static NangCapKhamNgoc gI() {
        return getInstance();
    }

    @Override
    public byte getType() {
        return 1;
    }

    @Override
    public void showTable(Player p) throws IOException {
        p.setUpgrade(this);
        Message m = new Message(-67);
        m.writer().writeByte(0);
        m.writer().writeByte(1);
        p.addmsg(m);
        m.cleanup();
        p.item_to_kham_ngoc = null;
        p.item_to_kham_ngoc_id_ngoc = -1;
        p.data_yesno = null;
    }

    @Override
    public void process(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException {
        if (p == null || p.item == null || p.trade_target != null) return;
        if (cat == 3 && num == 1 && type == 1 && action == 1) { // bo item kham ngoc vao
            if (idItem < 0 || idItem >= p.item.bag3.length) return;
            Item_wear it_select = p.item.bag3[idItem];
            if (it_select != null) {
                if (it_select.isThanTrang()) {
                    p.getService().send_box_ThongBao_OK("Trang bị Thần Trang (Type 8-15) không thể khảm ngọc!");
                    return;
                }
                if (it_select.template != null && (it_select.template.typeEquip == 6 || it_select.template.id == 11000)) {
                    p.getService().send_box_ThongBao_OK("Vật phẩm Quả Tim (Type 6) không có lỗ và không thể khảm ngọc!");
                    return;
                }
                p.getService().sendRebuildPutItem(idItem, (byte) 3, (short) 1);
                p.item_to_kham_ngoc = it_select;
            }
        } else if (cat == 4 && num == 1 && type == 1 && action == 1) { // bo ngoc kham vao
            if (p.item.total_item_bag_by_id(4, idItem) < num) {
                p.getService().send_box_ThongBao_OK("Không đủ vật phẩm trong hành trang!");
                return;
            }
            p.getService().sendRebuildPutItem(idItem, (byte) 4, (short) 1);
            p.item_to_kham_ngoc_id_ngoc = idItem;
        } else if (cat == 0 && num == 0 && type == 1 && action == 4) { // bat dau kham ngoc len item
            if (p.trade_target != null) {
                p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                return;
            }
            Item_wear it_select = p.item_to_kham_ngoc;
            if (it_select != null && p.item_to_kham_ngoc_id_ngoc != -1) {
                boolean exists = false;
                for (Item_wear it : p.item.bag3) {
                    if (it != null && it == it_select) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    Rebuild_Item.show_table(p, 3);
                    p.getService().send_box_ThongBao_OK("Vật phẩm không còn tồn tại trong hành trang!");
                    p.item_to_kham_ngoc = null;
                    p.item_to_kham_ngoc_id_ngoc = -1;
                    return;
                }
                if (it_select.isThanTrang()) {
                    p.getService().send_box_ThongBao_OK("Trang bị Thần Trang (Type 8-15) không thể khảm ngọc!");
                    return;
                }
                if (it_select.template != null && (it_select.template.typeEquip == 6 || it_select.template.id == 11000)) {
                    Rebuild_Item.show_table(p, 3);
                    p.getService().send_box_ThongBao_OK("Vật phẩm Quả Tim (Type 6) không có lỗ và không thể khảm ngọc!");
                    return;
                }
                ItemTemplate4 temp4 = ItemTemplate4.get_it_by_id(p.item_to_kham_ngoc_id_ngoc);
                if (it_select.mdakham == null) {
                    it_select.mdakham = new short[0];
                }
                if (it_select.numLoKham <= it_select.mdakham.length && temp4 != null) {
                    Rebuild_Item.show_table(p, 3);
                    p.getService().send_box_ThongBao_OK("Vật phẩm này không còn lỗ trống để khảm!");
                    return;
                }
                if (p.item_to_kham_ngoc_id_ngoc >= 221 && p.item_to_kham_ngoc_id_ngoc <= 226) {
                    Rebuild_Item.show_table(p, 3);
                    p.getService().send_box_ThongBao_OK("Đá Hải Thạch không thể khảm lên trang bị!");
                    return;
                }
                if (it_select.getColor() < 2 && Rebuild_Item.isGemCaoCap(p.item_to_kham_ngoc_id_ngoc)) {
                    Rebuild_Item.show_table(p, 3);
                    p.getService().send_box_ThongBao_OK(it_select.template.name + " chỉ có thể khảm ngọc Cấp 5 trở xuống!");
                    return;
                }
                if (!Rebuild_Item.check_can_kham_len_item(it_select, p.item_to_kham_ngoc_id_ngoc)) {
                    Rebuild_Item.show_table(p, 3);
                    p.getService().send_box_ThongBao_OK("Không thể khảm " + (temp4 != null ? temp4.name : "ngọc") + " lên loại trang bị này!");
                    return;
                }
                synchronized (p.item) {
                    if (p.item.total_item_bag_by_id(4, p.item_to_kham_ngoc_id_ngoc) < 1) {
                        Rebuild_Item.show_table(p, 3);
                        p.getService().send_box_ThongBao_OK("Không đủ vật phẩm trong hành trang");
                        return;
                    }
                    short ngocId = p.item_to_kham_ngoc_id_ngoc;
                    p.item.remove_item47(4, ngocId, 1);
                    Rebuild_Item.add_op_ngoc_kham_new(it_select, ngocId);
                    p.item.updateInventory(false);
                    p.setAbility();
                    p.update_info_to_all();
                    p.item_to_kham_ngoc_id_ngoc = -1;
                    
                    p.getService().sendRebuildActionMsg((byte) 4, "Bạn khảm thành công "
                            + ItemTemplate4.get_it_by_id(ngocId).name
                            + " lên " + it_select.template.name);
                }
            } else {
                Rebuild_Item.show_table(p, 3);
                p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại!");
            }
        }
    }
}
