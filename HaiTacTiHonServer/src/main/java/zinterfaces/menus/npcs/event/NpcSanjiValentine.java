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
 * NpcSanjiValentine — Handler độc lập cho NPC Sanji Tình Yêu (IDMenu: -885) thuộc Sự Kiện 15.
 * Trực tiếp implements iNpc, tự quản lý vòng đời spawn tại 11 Map Làng và xử lý Menu tương tác.
 */
public class NpcSanjiValentine implements iNpc {

    public static final short NPC_ID = (short) -885;
    public static final int EVENT_ID = 15;
    public static final String NPC_NAME = "Sanji Tình Yêu";
    public static final String NPC_TITLE = "Sự kiện";
    public static final String NPC_CHAT = "Tình yêu ngọt ngào như những viên Socola Valentine tuyệt hảo!";
    public static final short HEAD = (short) 19;
    public static final short HAIR = (short) 2;
    public static final byte[] DATA_FRAME = new byte[]{19, 2};

    /** Tọa độ xuất hiện cố định tại 11 bản đồ Làng & Thị Trấn */
    public static final int[][] VILLAGE_SPAWN_POS = new int[][]{
    {1, 85, 173}, {9, 140, 174}, {17, 80, 176}, {25, 85, 171}, {33, 160, 200}, {41, 85, 176},
    {49, 115, 174}, {69, 80, 160}, {83, 80, 170}, {93, 80, 152}, {113, 100, 168}, {191, 80, 147}
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
        p.menus.add(new Menu("Làm Hộp Socola Tình Yêu", (short) 165, () -> {
            try { new itemz.rebuilds.GhepSocola().show_table(p); } catch (Exception e) { e.printStackTrace(); }
        }));
        p.menus.add(new Menu("Làm Bánh Kem Socola", (short) 165, () -> {
            try { new itemz.rebuilds.GhepBanhKemSocola().show_table(p); } catch (Exception e) { e.printStackTrace(); }
        }));
        p.menus.add(new Menu("Gói Bó Hoa Tình Yêu", (short) 165, () -> {
            try { new itemz.rebuilds.GhepBoHoaTinhYeu().show_table(p); } catch (Exception e) { e.printStackTrace(); }
        }));
        p.menus.add(new Menu("Ghép Rương Tình Yêu", (short) 165, () -> {
            try { new itemz.rebuilds.GhepRuongTinhYeu().show_table(p); } catch (Exception e) { e.printStackTrace(); }
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