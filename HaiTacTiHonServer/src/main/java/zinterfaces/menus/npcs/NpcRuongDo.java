package zinterfaces.menus.npcs;

import map.Npc;
import map.Vgo;
import map.Zone;
import model.Menu;
import model.Player;
import zinterfaces.iNpc;

import java.io.IOException;

/**
 * NpcRuongDo — Handler cho NPC Rương Đồ (idmenu -6).
 *
 * <p>SQL data:
 * [-6, "Rương đồ", "Rương đồ", "", 580, 170, 3, -1, 0, 0, 0, [6, 1], 0, 0, []]
 *
 * <p>Chức năng:
 * <ul>
 *   <li>Mở rương đồ (UI Box)</li>
 *   <li>Rời khỏi đây (Teleport về làng / map 33)</li>
 * </ul>
 */
public class NpcRuongDo implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-6};
    }

    @Override
    public void onInitNpcForMap(Zone zone) {
        if (zone == null || zone.template == null) return;

        // Map 260 (Phòng chờ PvP Băng)
        if (zone.template.id == 260) {
            if (!hasNpc(zone, (short) -6)) {
                Npc ruongDo = new Npc();
                ruongDo.idmenu    = (short) -6;
                ruongDo.name      = "Rương đồ";
                ruongDo.namegt    = "Rương đồ";
                ruongDo.chat      = "";
                ruongDo.x         = 580;
                ruongDo.y         = 170;
                ruongDo.isPerson  = 3;
                ruongDo.typeIcon  = (byte) -1;
                ruongDo.wBlock    = 0;
                ruongDo.hBlock    = 0;
                ruongDo.b3        = 0;
                ruongDo.dataFrame = new byte[]{6, 1};
                ruongDo.head      = 0;
                ruongDo.hair      = 0;
                ruongDo.wearing   = new short[]{};
                zone.template.npcs.add(ruongDo);
            }
        }
        // Map 272..275 (Trận Chiến Lớn / World War)
        else if (zone.template.id >= 272 && zone.template.id <= 275) {
            if (!hasNpc(zone, (short) -6)) {
                Npc ruongDo = iNpc.createNpc(
                    (short) -6,
                    "Rương đồ",
                    "Rương đồ",
                    "",
                    (short) 200, (short) 170,
                    (byte) 3, (byte) -1, (byte) 0, (byte) 0,
                    (short) 6, (short) 1, new short[]{-1, -1, -1, -1}
                );
                zone.template.npcs.add(ruongDo);
            }
        }
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -6;
        p.menus.clear();

        int mapId = (p.map != null && p.map.template != null) ? p.map.template.id : -1;
        boolean isPvpMap = (mapId == 260 || (mapId >= 272 && mapId <= 275));

        if (isPvpMap) {
            // Map PvP / World War: hiện đủ chức năng rương đồ + rời khỏi
            p.menus.add(new Menu("Mở rương đồ", (short) 138, () -> {
                try {
                    p.getService().openUIBox();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            p.menus.add(new Menu("Rời khỏi đây", (short) 157, () -> {
                leaveMap(p);
            }));

            p.getService().openDynamicMenu(type, "Rương đồ", p.menus);
        } else {
            // Map làng hoặc map khác: NPC -6 là shop Buggi  mở shop bình thường
            p.getService().Send_UI_Shop(99);
        }
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        if (p.menus != null && index >= 0 && index < p.menus.size()) {
            p.menus.get(index).execute(p, index);
        }
    }

    private void leaveMap(Player p) {
        try {
            Vgo vgo = new Vgo();
            try {
                vgo.map_go = Zone.getMapByID(p.id_map_save);
                if (vgo.map_go == null || vgo.map_go.length == 0 || vgo.map_go[0] == null) {
                    vgo.map_go = Zone.getMapByID(33);
                }
            } catch (Exception e) {
                vgo.map_go = Zone.getMapByID(1);
            }
            if (vgo.map_go != null && vgo.map_go.length > 0 && vgo.map_go[0] != null) {
                vgo.xnew = (short) (vgo.map_go[0].template.maxW / 2);
                vgo.ynew = (short) (vgo.map_go[0].template.maxH / 2);
            } else {
                vgo.xnew = 440;
                vgo.ynew = 320;
            }
            p.goto_map(vgo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasNpc(Zone zone, short idmenu) {
        if (zone.template.npcs == null) return false;
        for (Npc n : zone.template.npcs) {
            if (n != null && n.idmenu == idmenu) return true;
        }
        return false;
    }
}
