package itemz;

import model.Player;
import network.Service;
import network.Message;
import template.ItemTemplate7;
import template.Item_wear;
import java.io.IOException;

public class Split_Item {
    public static void process(Player p, Message m2) throws IOException {
        if (p == null || p.item == null || p.item.bag3 == null) return;
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        byte act = m2.reader().readByte();
        short id = m2.reader().readShort();
        byte cat = m2.reader().readByte();
        short num = m2.reader().readShort();
        if (id < 0 || id >= p.item.bag3.length) return;
        if (act == 0 && cat == 3 && num == 1) { // add item
            Item_wear it_select = p.item.bag3[id];
            if (it_select != null) {
                Message m = new Message(-50);
                m.writer().writeByte(0);
                m.writer().writeByte(0);
                m.writer().writeShort(id);
                m.writer().writeByte(3);
                m.writer().writeShort(1);
                p.addmsg(m);
                m.cleanup();
            }
        } else if (act == 1 && cat == 3 && num == 1) { // process
            Item_wear it_select = p.item.bag3[id];
            if (it_select != null && p.data_yesno == null && !p.isTachTB) {
                p.data_yesno = new int[] {id};
                byte id_7 = getDustId(it_select);
                p.isTachTB = true;
                p.setyesNoDialog(new model.YesNoDialog(p, 11, "Thông báo",
                        "Bạn có thật sự muốn phá hủy vật phẩm " + it_select.template.name
                                + " và nhận 1 " + ItemTemplate7.get_it_by_id(id_7).name + " không?",
                        new String[] {"Đồng ý", "Hủy"}, new byte[] {2, 1}));
                p.getService().startYesNo();
            }
        }
    }

    public static byte getDustId(Item_wear it) {
        if (it == null) return 2;
        if (it.isThanTrang() || it.getColor() >= 4) {
            if (it.template != null && it.template.level >= 150) {
                return 18; // Bột Siêu Cấp cho Thần Trang cấp cao
            }
            return 4; // Bột Vàng cho Thần Trang
        }
        if (it.getColor() == 3) {
            return 4; // Bột Vàng
        }
        if (it.getColor() == 2) {
            return 3; // Bột Tím
        }
        return 2; // Bột Than
    }

    public static void show_table(Player p) throws IOException {
        Message m = new Message(-50);
        m.writer().writeByte(0);
        m.writer().writeByte(3);
        p.addmsg(m);
        m.cleanup();
    }
}
