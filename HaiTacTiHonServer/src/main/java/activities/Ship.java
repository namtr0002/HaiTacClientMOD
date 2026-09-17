package activities;

import model.Player;
import network.Service;
import core.ZUtil;
import network.Message;
import template.ItemTemplate4;
import java.io.IOException;
import model.YesNoDialog;

public class Ship {

    public static void show_table(Player p) throws IOException {
        Message m = new Message(-19);
        m.writer().writeByte(101);
        m.writer().writeUTF("Lái buôn");
        m.writer().writeByte(4);
        m.writer().writeShort(4);
        // Gói hàng 39, 38, 37, 36
        m.writer().writeShort(39);
        m.writer().writeShort(1);
        m.writer().writeShort(38);
        m.writer().writeShort(1);
        m.writer().writeShort(37);
        m.writer().writeShort(1);
        m.writer().writeShort(36);
        m.writer().writeShort(1);
        p.addmsg(m);
        m.cleanup();
        notice_ship_packet(p, 36);
    }

    public static void notice_ship_packet(Player p, int type) throws IOException {
        Message m = new Message(-53);
        m.writer().writeByte(0);
        m.writer().writeUTF("Gói hàng hiện tại của bạn là " + ItemTemplate4.get_item_name(type));
        m.writer().writeShort(type);
        p.addmsg(m);
        m.cleanup();
        p.id_ship_packet = (short) type;
    }

    public static void process(Player p, Message m2) throws IOException {
        if (p.map == null || p.map.template == null || p.map.template.id != 1 || p.typePirate != 0) {
            p.getService().send_box_ThongBao_OK("Chỉ có thể lấy hàng tại Làng Cối Xay Gió khi là Lái Buôn!");
            return;
        }
        byte act = m2.reader().readByte();
        switch (act) {
            case 0: {
                if (p.item.total_item_bag_by_id(4, 361) > 0) {
                    if (80 > ZUtil.random(120)) {
                        Ship.notice_ship_packet(p, 36);
                    } else if (90 > ZUtil.random(120)) {
                        Ship.notice_ship_packet(p, 37);
                    } else if (95 > ZUtil.random(120)) {
                        Ship.notice_ship_packet(p, 38);
                    } else {
                        Ship.notice_ship_packet(p, 39);
                    }
                    p.item.remove_item47(4, 361, 1);
                    p.item.updateInventory(false);
                } else {
                    p.getService().send_box_ThongBao_OK("Không đủ 1 " + ItemTemplate4.get_item_name(361));
                }
                break;
            }
            case 1: {
                if (p.id_ship_packet != -1) {
                    if (p.time_ship >= 5) {
                        p.getService().send_box_ThongBao_OK("Hôm nay đã vận chuyến tối đa (5/5 chuyến)!");
                        return;
                    }
                    if (p.ship_pet != null) {
                        p.getService().send_box_ThongBao_OK("Bạn đang có gói hàng!");
                        return;
                    }
                    p.setyesNoDialog(new YesNoDialog(p, 50, "Thông báo",
                            "Để tham gia lái buôn, bạn phải mất 10.000 beri, bạn có muốn tham gia?",
                            new String[]{"10.000", "Hủy"}, new byte[]{6, -1}, value -> {
                                if (value == 0) { // Chọn 10.000 Beri
                                    try {
                                        if (p.get_vang() < 10000) {
                                            p.getService().send_box_ThongBao_OK("Bạn không đủ 10.000 Beri!");
                                            return;
                                        }
                                        p.update_vang(-10000);
                                        p.updateMoney();
                                        Ship.notice_start_shipping(p);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }));
                    p.getService().startYesNo();
                }
                break;
            }
        }
    }

    public static void notice_start_shipping(Player p) throws IOException {
        if (p.map == null || p.map.template == null || p.map.template.id != 1 || p.typePirate != 0 || p.id_ship_packet == -1) {
            return;
        }
        p.id_map_ship_start = (short) p.map.template.id;

        Message m = new Message(-53);
        m.writer().writeByte(1);
        m.writer().writeUTF("Gói hàng " + ItemTemplate4.get_item_name(p.id_ship_packet)
                + " của bạn đang được chuyển đến, lên đường may mắn");
        p.addmsg(m);
        m.cleanup();

        // Tạo và spawn lạc đà Ship_pet
        p.ship_pet = new template.Ship_pet();
        p.ship_pet.index_map = database.IDManager.takeID(database.IDManager.SHIP_PET);
        if (p.ship_pet.index_map != -1) {
            p.ship_pet.main_ship = p;
            p.ship_pet.map = p.map;
            p.ship_pet.name = "Hàng " + p.name;
            p.ship_pet.x = 315;
            p.ship_pet.y = 210;
            p.ship_pet.hp_max = 2000;
            p.ship_pet.hp = p.ship_pet.hp_max;
            p.ship_pet.time_start = System.currentTimeMillis();
            p.ship_pet.id_map_save = p.map.template.id;
            template.Ship_pet.add(p.ship_pet);

            Message m_local = new Message(1);
            m_local.writer().writeByte(0);
            m_local.writer().writeShort(p.ship_pet.index_map);
            m_local.writer().writeShort(p.ship_pet.x);
            m_local.writer().writeShort(p.ship_pet.y);
            if (p.map != null) {
                p.map.send_msg_all_p(m_local, null, true);
            }
            m_local.cleanup();
        } else {
            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra khi tạo lạc đà vận chuyển, hãy thử lại!");
        }
    }
}
