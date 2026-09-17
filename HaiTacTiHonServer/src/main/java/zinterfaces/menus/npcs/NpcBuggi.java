package zinterfaces.menus.npcs;

import model.Player;
import model.Menu;
import model.VongQuay;
import model.VongQuayOcSen;
import zinterfaces.iNpc;
import zinterfaces.menus.MenuHoanMyKichAn;
import java.io.IOException;
import map.Npc;
import core.MenuController;

/**
 * NpcBuggi — Thuyền trưởng Buggi / Kho Báu / Vòng Quay (-133, -967, -6):
 *   -6   = Cửa hàng Buggi
 *   -133 = Kho Báu / Thuyền trưởng Buggi (Vòng quay kho báu, Hoàn mỹ - Kích ẩn, Vòng quay ốc sên)
 *   -967 = Vòng Quay Sự Kiện (Vòng Quay Thường, Vòng Quay Vip)
 */
public class NpcBuggi implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-133, -967, -6};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short npcId = npc != null ? npc.idmenu : -133;

        if (npcId == -6) {
            p.getService().Send_UI_Shop(99);
            return;
        }

        String title = (npc != null && npc.name != null && !npc.name.isEmpty()) 
                ? npc.name 
                : ((npcId == -967) ? MenuController.get_name_npc(npcId) : "Kho Báu");

        p.menus.clear();

        if (npcId == -967) {
            p.menus.add(new Menu("Vòng Quay Thường", (short) -1, () -> {
                try {
                    p.typeVongQuay = 3;
                    VongQuay.show_table(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
            p.menus.add(new Menu("Vòng Quay Vip", (short) -1, () -> {
                try {
                    p.typeVongQuay = 4;
                    VongQuay.show_table(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        } else {
            p.menus.add(new Menu("Vòng quay kho báu", (short) 129, () -> {
                try {
                    VongQuay.show_table(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            p.menus.add(new Menu("Hoàn Mỹ - Kích Ẩn", (short) 126, () -> {
                try {
                    MenuHoanMyKichAn.gI().sendMenu(p, npc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            p.menus.add(new Menu("Vòng quay ốc sên", (short) 130, () -> {
                try {
                    VongQuayOcSen.send_table(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        p.getService().openDynamicMenu(npcId, title, p.menus);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        handleMenu(p, index);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        if (p.menus != null && index >= 0 && index < p.menus.size()) {
            p.menus.get(index).execute(p, index);
        }
    }
}
