package zinterfaces.menus.npcs;

import model.Player;
import zinterfaces.iNpc;
import zinterfaces.iMenuDymanic;
import java.io.IOException;
import map.Npc;

public class NpcCroket implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-73};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -73;
        p.menus.clear();
        p.menus.add(new model.Menu("Hướng dẫn", (short) -1, () -> { try { handleMenu(p, 0); } catch (IOException e) {} }));
        p.getService().openDynamicMenu(type, "Croket", p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        if (index == 0) {
            String txt = "HƯỚNG DẪN\nBổ sung thông tin sau.";
            p.getService().Help_From_Server(-73, txt);
        }
    }
}
