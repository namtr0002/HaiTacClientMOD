package zinterfaces.menus.npcs.event;

import model.Player;
import model.Menu;
import map.Npc;
import map.Zone;
import event.Event;
import event.EventManager;
import zinterfaces.iNpc;

import java.io.IOException;

/**
 * NpcRobin20Thang10 — Handler độc lập cho NPC Chị Robin (IDMenu: -881) thuộc Sự Kiện 11.
 * Trực tiếp implements iNpc, tự quản lý vòng đời spawn tại 11 Map Làng và xử lý Menu tương tác.
 */
public class NpcRobin20Thang10 implements iNpc {

    public static final short NPC_ID = (short) -881;
    public static final int EVENT_ID = 11;
    public static final String NPC_NAME = "Chị Robin";
    public static final String NPC_TITLE = "Sự kiện";
    public static final String NPC_CHAT = "Chào mừng ngày Phụ Nữ Việt Nam 20/10 cùng muôn sắc hoa tươi!";
    public static final short HEAD = (short) 42;
    public static final short HAIR = (short) 2;
    public static final byte[] DATA_FRAME = new byte[]{42, 2};

    /** Tọa độ xuất hiện cố định tại 11 bản đồ Làng & Thị Trấn */
    public static final int[][] VILLAGE_SPAWN_POS = new int[][]{
    {1, 365, 315}, {9, 405, 302}, {17, 405, 300}, {25, 365, 278}, {33, 385, 240}, {41, 365, 312},
    {49, 365, 312}, {69, 385, 330}, {83, 385, 320}, {93, 385, 310}, {113, 385, 300}, {191, 385, 395}
    };

    @Override
    public short[] getId() {
        return new short[]{NPC_ID};
    }

    @Override
    public String getChatText() {
        return NPC_CHAT;
    }

    @Override
    public String[] getChatTexts() {
        return new String[]{NPC_CHAT};
    }

    @Override
    public void onInitNpcForMap(Zone zone) {
        if (zone == null || zone.template == null || zone.template.npcs == null) return;

        int mapId = zone.template.id;
        int targetX = -1;
        int targetY = -1;

        for (int[] pos : VILLAGE_SPAWN_POS) {
            if (pos[0] == mapId) {
                targetX = pos[1];
                targetY = pos[2];
                break;
            }
        }

        if (targetX < 0 || targetY < 0) return;

        boolean active = EventManager.isActive(EVENT_ID);
        if (active) {
            boolean exists = false;
            for (Npc n : zone.template.npcs) {
                if (n != null && n.idmenu == NPC_ID) {
                    exists = true;
                    n.x = (short) targetX;
                    n.y = (short) targetY;
                    n.dataFrame = DATA_FRAME;
                    n.head = HEAD;
                    n.hair = HAIR;
                    n.b3 = 0;
                    n.typeIcon = -1;
                    break;
                }
            }
            if (!exists) {
                Npc npc = iNpc.createNpc(
                        NPC_ID,
                        NPC_NAME,
                        NPC_TITLE,
                        NPC_CHAT,
                        (short) targetX,
                        (short) targetY,
                        (byte) 1,
                        (byte) -1,
                        (byte) 20,
                        (byte) 20,
                        HEAD,
                        HAIR,
                        new short[]{-1, -1, -1, -1}
                );
                npc.dataFrame = DATA_FRAME;
                npc.head = HEAD;
                npc.hair = HAIR;
                npc.b3 = 0;
                npc.typeIcon = -1;
                zone.template.npcs.add(npc);
            }
        } else {
            zone.template.npcs.removeIf(n -> n != null && n.idmenu == NPC_ID);
        }
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        event.SuKien20Thang10.gI().sendMenu(p, (int) NPC_ID);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        event.SuKien20Thang10.gI().handleMenu(p, menuId, index);
    }
}