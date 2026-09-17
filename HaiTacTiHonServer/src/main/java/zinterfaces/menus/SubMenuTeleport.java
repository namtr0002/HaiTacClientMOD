package zinterfaces.menus;

import model.Player;
import core.MenuController;
import zinterfaces.iMenu;
import java.io.IOException;
import map.Npc;
import map.Vgo;
import map.Zone;

public class SubMenuTeleport implements iMenu {

    @Override
    public short[] getId() {
        return new short[]{995, 996};
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (idNPC == 995) {
            MenuController.Select_Map_Tele_world(p, (byte) index);
        } else if (idNPC == 996) {
            MenuController.Select_Map_Tele(p, (byte) index);
        }
    }
}
