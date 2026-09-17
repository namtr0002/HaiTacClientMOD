package zinterfaces.menus;

import model.Player;
import zinterfaces.iNpc;
import map.Npc;
import itemz.rebuilds.NangCapHoanMy;
import itemz.rebuilds.NangCapKichAn;
import itemz.rebuilds.NangCapCongCheTac;
import java.io.IOException;

/**
 * MenuHoanMyKichAn — Xử lý menu Hoàn Mỹ - Kích Ẩn - Phục Hồi Chế Tác (Dynamic Menu ID 991)
 */
public class MenuHoanMyKichAn implements iNpc {

    private static final MenuHoanMyKichAn instance = new MenuHoanMyKichAn();

    public static MenuHoanMyKichAn gI() {
        return instance;
    }

    @Override
    public short[] getId() {
        return new short[]{991};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        String[] options = new String[]{
            "Hoàn Mỹ trang bị",
            "Kích ẩn trang bị",
            "Phục hồi điểm chế tác",
            "Hướng dẫn"
        };
        short[] icons = new short[]{126, 128, 130, 148};
        p.getService().openDynamicMenu(991, "Hoàn Mỹ - Kích Ẩn", options, icons);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        handleMenu(p, 991, index);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        switch (index) {
            case 0: { // Hoàn Mỹ trang bị
                NangCapHoanMy.getInstance().showTable(p);
                break;
            }
            case 1: { // Kích ẩn trang bị
                NangCapKichAn.getInstance().showTable(p);
                break;
            }
            case 2: { // Phục hồi điểm chế tác
                NangCapCongCheTac.getInstance().showTable(p);
                break;
            }
            case 3: { // Hướng dẫn
                String helpTxt = "HƯỚNG DẪN HOÀN MỸ - KÍCH ẨN - PHỤC HỒI:\n\n"
                        + "1. Hoàn mỹ trang bị:\n"
                        + "- Yêu cầu: Trang bị Tím hoặc Cam có điểm chế tác >= 50, Ngọc hải thạch (221-226) và 5 Ruby.\n"
                        + "- Tác dụng: Biến trang bị thành trang bị Hoàn Mỹ gia tăng thêm thuộc tính cao cấp.\n\n"
                        + "2. Kích ẩn trang bị:\n"
                        + "- Yêu cầu: Trang bị Tím hoặc Cam có điểm chế tác >= 50, Ngọc hải thạch (221-226) và 5 Ruby.\n"
                        + "- Tác dụng: Kích hoạt chỉ số ẩn ngẫu nhiên cho trang bị.\n\n"
                        + "3. Phục hồi điểm chế tác:\n"
                        + "- Yêu cầu: 5 Ruby (hồi 1 điểm) hoặc 100 Ruby (hồi 10 điểm).\n"
                        + "- Tác dụng: Hồi phục lại điểm chế tác cho trang bị bị tổn hại khi nâng cấp.";
                p.getService().Help_From_Server(-6, helpTxt);
                break;
            }
        }
    }
}
