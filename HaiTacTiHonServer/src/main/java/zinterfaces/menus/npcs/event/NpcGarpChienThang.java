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
 * NpcGarpChienThang — Handler độc lập cho NPC Phó Đô Đốc Garp (IDMenu: -876) thuộc Sự Kiện 6.
 * Trực tiếp implements iNpc, tự quản lý vòng đời spawn tại 11 Map Làng và xử lý Menu tương tác.
 */
public class NpcGarpChienThang implements iNpc {

    public static final short NPC_ID = (short) -876;
    public static final int EVENT_ID = 6;
    public static final String NPC_NAME = "Phó Đô Đốc Garp";
    public static final String NPC_TITLE = "Sự kiện";
    public static final String NPC_CHAT = "Hahaha! Chào mừng ngày Đại Lễ Chiến Thắng non sông thống nhất!";
    public static final short HEAD = (short) 4;
    public static final short HAIR = (short) 2;
    public static final byte[] DATA_FRAME = new byte[]{4, 2};

    /** Tọa độ xuất hiện cố định tại 11 bản đồ Làng & Thị Trấn */
    public static final int[][] VILLAGE_SPAWN_POS = new int[][]{
    {1, 385, 173}, {9, 445, 174}, {17, 310, 176}, {25, 265, 171}, {33, 320, 200}, {41, 235, 176},
    {49, 255, 174}, {69, 240, 160}, {83, 235, 170}, {93, 235, 152}, {113, 360, 168}, {191, 340, 147}
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
        p.menus.clear();
        Event ev = EventManager.gI().getEvent(EVENT_ID);

        // 1. Cửa Hàng Sự Kiện
        p.menus.add(new Menu("Cửa Hàng Sự Kiện", (short) 104, () -> {
            try {
                if (ev != null) ev.openShop(p);
                else EventManager.dispatchOpenShop(p, EVENT_ID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 2. Ghép Đồ / Đổi Quà riêng của sự kiện
        p.menus.add(new Menu("Ghép Hộp Kim Cương Đỏ", (short) 165, () -> {
            try { new itemz.rebuilds.GhepHopKimCuongDo().show_table(p); } catch (Exception e) { e.printStackTrace(); }
        }));
        p.menus.add(new Menu("Ghép Hộp Kim Cương Tím", (short) 165, () -> {
            try { new itemz.rebuilds.GhepHopKimCuongTim().show_table(p); } catch (Exception e) { e.printStackTrace(); }
        }));
        p.menus.add(new Menu("Ghép Rương Chiến Thắng", (short) 165, () -> {
            try { new itemz.rebuilds.GhepRuongChienThang807().show_table(p); } catch (Exception e) { e.printStackTrace(); }
        }));

        // 3. Vòng Quay May Mắn
        p.menus.add(new Menu("Vòng Quay May Mắn", (short) 141, () -> {
            try {
                if (ev != null) ev.openLuckyWheel(p);
                else p.getService().send_box_ThongBao_OK("Vòng quay sự kiện chưa mở!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 4. Bảng Xếp Hạng
        p.menus.add(new Menu("Bảng Xếp Hạng", (short) 124, () -> {
            if (ev != null) ev.showEventRank(p);
        }));

        // 5. Tích Nạp Sự Kiện
        p.menus.add(new Menu("Tích Nạp Sự Kiện", (short) 134, () -> {
            try {
                if (ev != null) ev.showTichNap(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 6. Tích Tiêu Ruby
        p.menus.add(new Menu("Tích Tiêu Ruby", (short) 135, () -> {
            try {
                if (ev != null) ev.showTichTieu(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 7. Hướng Dẫn Chi Tiết
        p.menus.add(new Menu("Hướng Dẫn", (short) 123, () -> {
            try {
                if (ev != null) ev.sendHelp(p);
                else p.getService().Help_From_Server(NPC_ID, NPC_CHAT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        p.getService().openDynamicMenu(NPC_ID, NPC_NAME, p.menus);
    }
}