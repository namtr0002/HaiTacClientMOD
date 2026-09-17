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
 * NpcSuGiaTet — Handler độc lập cho NPC Sứ Giả Tết Bính Ngọ (IDMenu: -872) thuộc Sự Kiện 2.
 * Trực tiếp implements iNpc, tự quản lý vòng đời spawn tại 11 Map Làng và xử lý Menu tương tác.
 */
public class NpcSuGiaTet implements iNpc {

    public static final short NPC_ID = (short) -872;
    public static final int EVENT_ID = 2;
    public static final String NPC_NAME = "Sứ Giả Tết Bính Ngọ";
    public static final String NPC_TITLE = "Sự kiện";
    public static final String NPC_CHAT = "Chúc mừng năm mới Bính Ngọ 2026 vạn sự như ý!";
    public static final short HEAD = (short) 1;
    public static final short HAIR = (short) 2;
    public static final byte[] DATA_FRAME = new byte[]{1, 2};

    /** Tọa độ xuất hiện cố định tại 11 bản đồ Làng & Thị Trấn */
    public static final int[][] VILLAGE_SPAWN_POS = new int[][]{
    {1, 175, 315}, {9, 215, 302}, {17, 215, 300}, {25, 175, 278}, {33, 195, 240}, {41, 175, 312},
    {49, 175, 312}, {69, 195, 330}, {83, 195, 320}, {93, 195, 310}, {113, 195, 300}, {191, 195, 395}
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
        p.menus.add(new Menu("Gói Bánh Chưng", (short) 165, () -> {
            try { new itemz.rebuilds.GhepBanhChung350().show_table(p); } catch (Exception e) { e.printStackTrace(); }
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