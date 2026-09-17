package zinterfaces.menus.npcs;

import map.Npc;
import map.Vgo;
import map.Zone;
import model.Menu;
import model.Player;
import zinterfaces.iNpc;

import java.io.IOException;

/**
 * NpcMsFinger — Handler cho NPC Ms Finger (idmenu -99).
 *
 * <p>SQL data:
 * [-99, "Ms Finger", "Shop\nthức ăn",
 *  "Trận chiến trong đấu trường sẽ rất khó khăn, thức ăn và nước uống sẽ rất quan trọng trong việc giành chiến thắng.",
 *  530, 170, 1, 1, 0, 0, 0, [41, 2], 0, 0, []]
 *
 * <p>Chức năng:
 * <ul>
 *   <li>Quán ăn (Shop thức ăn, UI shop 20)</li>
 *   <li>Rời khỏi đây (Teleport về làng / map 33)</li>
 * </ul>
 */
public class NpcMsFinger implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-99};
    }

    @Override
    public void onInitNpcForMap(Zone zone) {
        if (zone == null || zone.template == null) return;

        // Map 260 (Phòng chờ PvP Băng)
        if (zone.template.id == 260) {
            if (!hasNpc(zone, (short) -99)) {
                Npc msFinger = new Npc();
                msFinger.idmenu    = (short) -99;
                msFinger.name      = "Ms Finger";
                msFinger.namegt    = "Shop\nthức ăn";
                msFinger.chat      = "Trận chiến trong đấu trường sẽ rất khó khăn, thức ăn và nước uống sẽ rất quan trọng trong việc giành chiến thắng.";
                msFinger.x         = 530;
                msFinger.y         = 170;
                msFinger.isPerson  = 1;
                msFinger.typeIcon  = 1;
                msFinger.wBlock    = 0;
                msFinger.hBlock    = 0;
                msFinger.b3        = 0;
                msFinger.dataFrame = new byte[]{41, 2};
                msFinger.head      = 0;
                msFinger.hair      = 0;
                msFinger.wearing   = new short[]{};
                zone.template.npcs.add(msFinger);
            }
        }
        // Map 272..275 (Trận Chiến Lớn / World War)
        else if (zone.template.id >= 272 && zone.template.id <= 275) {
            if (!hasNpc(zone, (short) -99)) {
                Npc msFinger = iNpc.createNpc(
                    (short) -99,
                    "Ms Finger",
                    "Shop\nthức ăn",
                    "Trận chiến trong đấu trường sẽ rất khó khăn, thức ăn và nước uống sẽ rất quan trọng trong việc giành chiến thắng.",
                    (short) 150, (short) 170,
                    (byte) 1, (byte) 1, (byte) 0, (byte) 0,
                    (short) 41, (short) 2, new short[]{-1, -1, -1, -1}
                );
                zone.template.npcs.add(msFinger);
            }
        }
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -99;
        p.menus.clear();

        p.menus.add(new Menu("Quán ăn", (short) 104, () -> {
            try {
                p.getService().Send_UI_Shop(20);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        p.menus.add(new Menu("Rời khỏi đây", (short) 157, () -> {
            leaveMap(p);
        }));

        p.getService().openDynamicMenu(type, "Ms Finger", p.menus);
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
