package zinterfaces.menus.npcs;

import model.Player;
import core.MenuController;
import zinterfaces.iNpc;
import java.io.IOException;
import map.Npc;

/**
 * NpcTeleport — Quản lý Menu NPC Dịch Chuyển.
 *
 * Menu 1: Mở Menu NPC động type 2 với 2 tùy chọn {"Trong làng", "Thế giới"}.
 * Menu 2: Mở danh sách Map dịch chuyển dạng Dynamic Map (Quest style - type 1)
 *         dùng ID 995 cho Thế Giới và ID 996 cho Trong Làng.
 */
public class NpcTeleport implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{
            -144, -153, -152, -151, -150, -149, -148,
            -124, -131, -130, -129, -128, -127, -126, -125, -123,
            -107, -115, -114, -113, -112, -111, -110, -109, -108,
            -85, -96, -94, -93, -92,
            -97, -82,
            -60, -59, -63, -62, -61,
            -44, -58, -51, -50, -49,
            -36, -57, -42, -41, -40,
            -28, -56, -34, -33, -32,
            -20, -55, -26, -25, -24,
            -12, -54, -18, -17, -16,
            -5, -53, -10, -9, -8,
            -132, 0, -83, -81, -80, -79
        };
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short id = npc != null ? npc.idmenu : (short) p.currentNpcId;
        p.currentNpcId = id;
        p.currentMenuNpcId = id;
        if (isSubTeleNpc(id)) {
            MenuController.Show_List_Map_Tele(p, 0, id);
        } else {
            // Chuẩn packet -20 type 2: Menu NGANG dưới đáy màn hình {"Trong làng", "Thế giới"}
            p.getService().sendDymanicMenuMap(id, "", new String[]{"Trong làng", "Thế giới"});
        }
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        if (index < 0 || index > 1) return;
        MenuController.Show_List_Map_Tele(p, index, menuId);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        handleMenu(p, (int) p.currentNpcId, index);
    }

    private boolean isSubTeleNpc(short current) {
        switch (current) {
            case -153: case -152: case -151: case -150: case -149: case -148:
            case -131: case -130: case -129: case -128: case -127: case -126: case -125: case -123:
            case -115: case -114: case -113: case -112: case -111: case -110: case -109: case -108:
            case -96: case -94: case -93: case -92:
            case -83: case -81: case -80: case -79:
            case -59: case -63: case -62: case -61:
            case -58: case -51: case -50: case -49:
            case -57: case -42: case -41: case -40:
            case -56: case -34: case -33: case -32:
            case -55: case -26: case -25: case -24:
            case -54: case -18: case -17: case -16:
            case -53: case -10: case -9: case -8:
                return true;
            default:
                return false;
        }
    }
}
