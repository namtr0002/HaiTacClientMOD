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
 * NpcOngGiaNoel — Handler độc lập cho NPC Ông Già Noel (IDMenu: -882) thuộc Sự Kiện 12.
 * Trực tiếp implements iNpc, tự quản lý vòng đời spawn tại 11 Map Làng và xử lý Menu tương tác.
 */
public class NpcOngGiaNoel implements iNpc {

    public static final short NPC_ID = (short) -882;
    public static final int EVENT_ID = 12;
    public static final String NPC_NAME = "Ông Già Noel";
    public static final String NPC_TITLE = "Sự kiện";
    public static final String NPC_CHAT = "Merry Christmas! Hãy cùng trang trí cây thông và đón Santa nhé!";
    public static final short HEAD = (short) 37;
    public static final short HAIR = (short) 2;
    public static final byte[] DATA_FRAME = new byte[]{37, 2};

    /** Tọa độ xuất hiện cố định tại 11 bản đồ Làng & Thị Trấn */
    public static final int[][] VILLAGE_SPAWN_POS = new int[][]{
    {1, 550, 173}, {9, 670, 174}, {17, 560, 176}, {25, 445, 171}, {33, 480, 200}, {41, 430, 176},
    {49, 445, 174}, {69, 445, 160}, {83, 450, 170}, {93, 495, 152}, {113, 590, 168}, {191, 580, 147}
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
        p.menus.add(new Menu("Đổi Hộp Quà Noel", (short) 165, () -> {
            try {
                new itemz.rebuilds.GhepHopQuaNoel().show_table(p);
            } catch (Exception e) { e.printStackTrace(); }
        }));
        p.menus.add(new Menu("Trang Trí Cây Thông Thường", (short) 165, () -> {
            try {
                new itemz.rebuilds.TrangTriCayThongThuong().show_table(p);
            } catch (Exception e) { e.printStackTrace(); }
        }));
        p.menus.add(new Menu("Trang Trí Cây Thông VIP", (short) 165, () -> {
            try {
                new itemz.rebuilds.TrangTriCayThongVip().show_table(p);
            } catch (Exception e) { e.printStackTrace(); }
        }));
        p.menus.add(new Menu("Chúc Mừng Giáng Sinh", (short) 141, () -> {
            try {
                if (ev != null) ev.handleMenu(p, NPC_ID, 4);
            } catch (Exception e) { e.printStackTrace(); }
        }));

        // 5. Bảng Xếp Hạng
        p.menus.add(new Menu("Bảng Xếp Hạng", (short) 124, () -> {
            if (ev != null) ev.showEventRank(p);
        }));

        // 6. Hướng Dẫn Chi Tiết
        p.menus.add(new Menu("Hướng Dẫn", (short) 123, () -> {
            try {
                if (ev != null) ev.sendHelp(p);
                else p.getService().Help_From_Server(NPC_ID, NPC_CHAT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 7. Tích Nạp Sự Kiện
        p.menus.add(new Menu("Tích Nạp Sự Kiện", (short) 134, () -> {
            try {
                if (ev != null) ev.showTichNap(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 8. Tích Tiêu Ruby
        p.menus.add(new Menu("Tích Tiêu Ruby", (short) 135, () -> {
            try {
                if (ev != null) ev.showTichTieu(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        p.getService().openDynamicMenu(NPC_ID, NPC_NAME, p.menus);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        if (p.menus != null && index >= 0 && index < p.menus.size()) {
            p.menus.get(index).execute(p, index);
            return;
        }
        Event ev = EventManager.gI().getEvent(EVENT_ID);
        if (ev instanceof event.SuKienNoel) {
            ((event.SuKienNoel) ev).processMenu(p, index);
        }
    }
}