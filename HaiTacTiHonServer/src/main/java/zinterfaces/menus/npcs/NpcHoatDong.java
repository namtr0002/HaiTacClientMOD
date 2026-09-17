package zinterfaces.menus.npcs;

import model.Player;
import zinterfaces.iNpc;
import java.io.IOException;
import map.Npc;

public class NpcHoatDong implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-78};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -78;
        p.menus.clear();
        p.menus.add(new model.Menu("Hang Động", (short) -1, () -> { try { handleMenu(p, 0); } catch (IOException e) {} }));
        p.getService().openDynamicMenu(type, "Hoạt Động", p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        if (index == 0) { // Hang động
            p.getService().openDynamicMenu(-990, "Hoạt Động",
                    new String[]{"Tham Gia", "Đổi Thẻ Hang Động", "BXH Hang Động"}, null);
        }
    }
}