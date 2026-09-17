package activities;

import model.Player;
import network.Service;
import network.Message;
import java.io.IOException;
import model.YesNoDialog;

public class Max_Level {
    public static void process(Player player, Message m) throws IOException {
        byte act = m.reader().readByte();
        short id = m.reader().readShort();
        if (act == 0 && player.level >= 100) {
            if (player.pointAttributeThongThao < 1) {
                player.getService().send_box_ThongBao_OK("Không đủ 1 điểm thông thạo");
                return;
            }
            String[] name = new String[] {"Kháng phép", "MP+", "Kháng vật lý", "Tăng phòng thủ",
                    "HP+", "Tăng tấn công"};
            short[] id_op = new short[]{27, 16, 26, 4, 15, 1};
            for (int i = 0; i < id_op.length; i++) {
                if (id_op[i] == id) {
                    player.data_yesno = new int[] {i};
                    player.setyesNoDialog(new YesNoDialog(player, 41, "Thông báo",
                            ("Bạn có muốn cộng 1 điểm vào " + name[i] + "?"),
                            new String[] {"Đồng ý", "Hủy"}, new byte[] {2, 1}));
                    player.getService().startYesNo();
                    break;
                }
            }
        }
    }

    public static void show_table(Player p) throws IOException {
        Max_Level.set_pointMaxLevelAttri(p);
        Message m = new Message(49);
        m.writer().writeByte(2);
        m.writer().writeShort((int) p.pointAttributeThongThao);
        p.addmsg(m);
        m.cleanup();
    }

    private static void set_pointMaxLevelAttri(Player p) throws IOException {
        Message m = new Message(49);
        m.writer().writeByte(0);
        m.writer().writeShort(p.thongthao);
        String[] name = new String[] {"Kháng phép", "MP+", "Kháng vật lý", "Tăng phòng thủ", "HP+",
                "Tăng tấn công"};
        short[] id_op = new short[]{27, 16, 26, 4, 15, 1};
        m.writer().writeByte(name.length);
        for (int i = 0; i < name.length; i++) {
            m.writer().writeShort(id_op[i]);
            m.writer().writeUTF(name[i]);
            int value = 0;
            for (int j = 0; j < p.list_op_thongthao.size(); j++) {
                if (p.list_op_thongthao.get(j).id == id_op[i]) {
                    value += p.list_op_thongthao.get(j).getParam();
                }
            }
            m.writer().writeShort(value);
            m.writer().writeShort(80);
        }
        p.addmsg(m);
        m.cleanup();
    }
}
