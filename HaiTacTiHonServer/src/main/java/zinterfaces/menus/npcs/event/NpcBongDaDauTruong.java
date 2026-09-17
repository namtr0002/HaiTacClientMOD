package zinterfaces.menus.npcs.event;

import model.Player;
import map.Npc;
import map.Zone;
import event.EventManager;
import event.SuKienDauTruongRucLua2026;
import zinterfaces.iNpc;

import java.io.IOException;

/**
 * NpcBongDaDauTruong — Handler cho NPC NPC Bóng Đá (IDMenu: -883) thuộc Sự Kiện 13.
 * Trực tiếp implements iNpc, tự quản lý vòng đời spawn tại 11 Map Làng và mở Menu sự kiện chuẩn.
 */
public class NpcBongDaDauTruong implements iNpc {

    public static final short NPC_ID = (short) -883;
    public static final int EVENT_ID = 13;
    public static final String NPC_NAME = "NPC Bóng Đá";
    public static final String NPC_TITLE = "Sự kiện";
    public static final String NPC_CHAT = "Sôi động cùng Đấu Trường Rực Lửa và Dự Đoán Bot PvP!";
    public static final short HEAD = (short) 27;
    public static final short HAIR = (short) 2;
    public static final byte[] DATA_FRAME = new byte[]{27, 2};

    /** Tọa độ xuất hiện cố định tại 11 bản đồ Làng & Thị Trấn */
    public static final int[][] VILLAGE_SPAWN_POS = new int[][]{
        {1, 740, 173}, {9, 895, 174}, {17, 755, 176}, {25, 580, 171}, {33, 730, 200}, {41, 660, 176},
        {49, 610, 174}, {69, 715, 160}, {83, 685, 170}, {93, 690, 152}, {113, 850, 168}, {191, 835, 147}
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
        SuKienDauTruongRucLua2026.gI().openEventMenu(p);
    }
}
