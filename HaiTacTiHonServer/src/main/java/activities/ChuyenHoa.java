package activities;

import java.io.IOException;
import model.Player;
import network.Service;
import network.Message;
import template.Item_wear;

public class ChuyenHoa {
    public static boolean isItemBlockedChuyenHoa(Item_wear it) {
        if (it == null || it.template == null) {
            return true;
        }
        if (it.isThanTrang() || it.template.isThanTrang()) {
            return true;
        }
        if (it.getColor() == 8) {
            return true;
        }
        if (it.template.typeEquip >= 8 && it.template.typeEquip <= 15) {
            return true;
        }
        if (template.ThanTrangConfig.isThanTrang(it.template.id)) {
            return true;
        }
        if (it.template.id >= 2598 && it.template.id <= 2694) {
            return true;
        }
        if (it.template.typeEquip == 6 || it.template.id == 11000) {
            return true;
        }
        if (it.template.typeEquip == 7) {
            return true;
        }
        return false;
    }

    public static void show_table(Player p) throws IOException {
        Message m = new Message(-77);
        m.writer().writeByte(0);
        p.addmsg(m);
        m.cleanup();
        p.item_chuyenhoa_save_0 = null;
        p.item_chuyenhoa_save_1 = null;
    }

    public static void process(Player p, Message m2) throws IOException {
        if (p == null || p.item == null || p.isClosed) return;
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        byte type = m2.reader().readByte();
        short idLeft = m2.reader().readShort();
        short idRight = -1;
        if (type == 2 || type == 3) {
            idRight = m2.reader().readShort();
        }
        switch (type) {
            case 1: {
                if (idRight == -1 && idLeft >= 0 && idLeft < p.item.bag3.length) {
                    Item_wear it_select = p.item.bag3[idLeft];
                    if (it_select != null) {
                        if (isItemBlockedChuyenHoa(it_select)) {
                            p.getService().send_box_ThongBao_OK("Trang bị Thần Trang không thể chuyển hóa!");
                            return;
                        }
                        if (it_select.levelUp > 15) {
                            p.getService().send_box_ThongBao_OK("Không thể chuyển hóa trang bị trên cấp +15!");
                            return;
                        }
                        Message m = new Message(-77);
                        m.writer().writeByte(1);
                        if (it_select.levelUp <= 5) {
                            m.writer().writeByte(1);
                            p.item_chuyenhoa_save_1 = it_select;
                        } else {
                            m.writer().writeByte(0);
                            p.item_chuyenhoa_save_0 = it_select;
                        }
                        m.writer().writeShort(idLeft);
                        p.addmsg(m);
                        m.cleanup();
                    }
                }
                break;
            }
            case 2: {
                if (p.item_chuyenhoa_save_0 != null && p.item_chuyenhoa_save_1 != null
                        && p.item_chuyenhoa_save_0.levelUp > p.item_chuyenhoa_save_1.levelUp) {
                    if (isItemBlockedChuyenHoa(p.item_chuyenhoa_save_0) || isItemBlockedChuyenHoa(p.item_chuyenhoa_save_1)) {
                        p.getService().send_box_ThongBao_OK("Trang bị Thần Trang không thể chuyển hóa!");
                        p.item_chuyenhoa_save_0 = null;
                        p.item_chuyenhoa_save_1 = null;
                        return;
                    }
                    if (p.item_chuyenhoa_save_0.levelUp > 15 || p.item_chuyenhoa_save_1.levelUp > 15) {
                        p.getService().send_box_ThongBao_OK("Không thể chuyển hóa trang bị trên cấp +15!");
                        return;
                    }
                    if (p.item_chuyenhoa_save_1.template.level > 50
                            && p.item_chuyenhoa_save_1.template.level > (p.item_chuyenhoa_save_0.template.level
                                    + 10)) {
                        p.getService().send_box_ThongBao_OK("Trang bị 5x trở lên, khi chuyển hóa chỉ được chuyển hóa cho trang bị cao hơn 1 cấp trang bị!");
                        return;
                    }
                    if (p.item_chuyenhoa_save_0.template.typeEquip != p.item_chuyenhoa_save_1.template.typeEquip) {
                        p.getService().send_box_ThongBao_OK("Chỉ có thể chuyển hóa giữa 2 trang bị cùng loại!");
                        return;
                    }
                    if (p.item_chuyenhoa_save_0.template.typeEquip == 7
                            || p.item_chuyenhoa_save_1.template.typeEquip == 7) {
                        p.getService().send_box_ThongBao_OK("Không thể thực hiện chuyển hóa đối với dial!");
                        return;
                    }
                    p.setyesNoDialog(new model.YesNoDialog(p, 6, "Thông báo",
                            ("Bạn muốn thực hiện chuyển số cường hóa 2 món đồ với mức phí là 250 Ruby?"),
                            new String[] {"250", "Đóng"}, new byte[] {7, -1}));
                    p.getService().startYesNo();
                }
                break;
            }
        }
    }

    public static void show_result(Player p, String s, int lv) throws IOException {
        Message m = new Message(-77);
        m.writer().writeByte(3);
        m.writer().writeUTF(s);
        m.writer().writeByte(lv);
        p.addmsg(m);
        m.cleanup();
    }
}
