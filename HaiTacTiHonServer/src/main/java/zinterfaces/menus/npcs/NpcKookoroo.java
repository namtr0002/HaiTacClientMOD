package zinterfaces.menus.npcs;

import model.Player;
import model.Menu;
import core.MenuController;
import zinterfaces.iNpc;
import map.Zone;
import map.Npc;
import map.Vgo;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import template.ItemFashionP;

/**
 * NpcKookoroo — Quán ăn / Vận chuyển hàng (-147..-2) + sub-menu động:
 *   986 = Vận chuyển hàng
 */
public class NpcKookoroo implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{
            -147, -120, -116, -102, -89, -76, -68, -46, -39, -29, -22, -14, -2
        };
    }

    private String getNpcName(short type) {
        switch (type) {
            case -147: return "kookoroo";
            case -120: return "Conic";
            case -102: return "Yoshi moto";
            case -89: return "Dr Kure";
            case -76: return "Mr Acrobatic";
            case -68: return "Sapie";
            case -46: return "Noziko";
            case -39: return "Cami";
            case -29: return "Kaiya";
            case -22: return "Cho Cho";
            case -14: return "Rita";
            case -2: return "Machiko";
            default: return "Machiko";
        }
    }

    private void buildMenuOptions(Player p, short npcId) {
        p.menus.clear();

        // 0. Quán ăn
        p.menus.add(new Menu("Quán ăn", (short) 104, () -> {
            try {
                p.getService().Send_UI_Shop(20);
            } catch (IOException ex) {
                Logger.getLogger(NpcKookoroo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // 1. Vận Chuyển Hàng
        p.menus.add(new Menu("Vận Chuyển Hàng", (short) 107, () -> {
            String[] name = new String[]{"Lấy Hàng", "Trả hàng", "Đăng ký bảo vệ hàng",
                "Thuê bảo vệ hàng", "Đăng ký chức năng", "Hủy vận buôn", "Gọi Lạc đà trở về",
                "Xem vị trí lạc đà", "Xem số lần vận buôn", "Hướng dẫn"};
            short[] icon = new short[]{107, 109, 110, 111, 110, 111, 151, -1, 114, 114};
            byte[] b7 = new byte[]{3, 3, 3, 3, 7, 3, 3, 7, 7, 7};
            p.getService().send_dynamic_menu_type6(986, 0, "Vận chuyển hàng", name, icon, b7);
        }));

        // 2. Tiệm tóc
        p.menus.add(new Menu("Tiệm tóc", (short) 106, () -> {
            try {
                ItemFashionP.show_table(p, 103);
            } catch (IOException ex) {
                Logger.getLogger(NpcKookoroo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // 3. Đóng thuyền
        p.menus.add(new Menu("Đóng thuyền", (short) 105, () -> {
            try {
                ItemFashionP.show_table(p, 102);
            } catch (IOException ex) {
                Logger.getLogger(NpcKookoroo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // 4. Thời trang
        p.menus.add(new Menu("Thời trang", (short) 108, () -> {
            try {
                ItemFashionP.show_table(p, 105);
            } catch (IOException ex) {
                Logger.getLogger(NpcKookoroo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // 5. Thẩm mỹ viện
        p.menus.add(new Menu("Thẩm mỹ viện", (short) 158, () -> {
            try {
                ItemFashionP.show_table(p, 108);
            } catch (IOException ex) {
                Logger.getLogger(NpcKookoroo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -2;
        buildMenuOptions(p, type);
        p.getService().openDynamicMenu(type, getNpcName(type), p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        short currentId = (short) p.currentNpcId;
        if (currentId == 986) {
            zinterfaces.menus.MenuVanChuyenHang.gI().handleMenu(p, index);
            return;
        }

        if (p.menus != null && index >= 0 && index < p.menus.size()) {
            p.menus.get(index).execute(p, index);
            return;
        }

        switch (index) {
            case 0:
                p.getService().Send_UI_Shop(20);
                break;
            case 1: {
                String[] name = new String[]{"Lấy Hàng", "Trả hàng", "Đăng ký bảo vệ hàng",
                    "Thuê bảo vệ hàng", "Đăng ký chức năng", "Hủy vận buôn", "Gọi Lạc đà trở về",
                    "Xem vị trí lạc đà", "Xem số lần vận buôn", "Hướng dẫn"};
                short[] icon = new short[]{107, 109, 110, 111, 110, 111, 151, -1, 114, 114};
                byte[] b7 = new byte[]{3, 3, 3, 3, 7, 3, 3, 7, 7, 7};
                p.getService().send_dynamic_menu_type6(986, 0, "Vận chuyển hàng", name, icon, b7);
                break;
            }
            case 2:
                ItemFashionP.show_table(p, 103);
                break;
            case 3:
                ItemFashionP.show_table(p, 102);
                break;
            case 4:
                ItemFashionP.show_table(p, 105);
                break;
            case 5:
                ItemFashionP.show_table(p, 108);
                break;
        }
    }
}
