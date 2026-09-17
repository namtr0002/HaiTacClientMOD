package zinterfaces.menus.npcs;

import activities.ChiemDaoMenu;
import activities.DungeonGiftMenu;
import activities.TimedDungeonManager;
import activities.TimedDungeonManager.ScheduleConfig;
import clan.Clan;
import map.Npc;
import map.Zone;
import map.zones.ChiemDao;
import model.Menu;
import model.Player;
import zinterfaces.iNpc;

import java.io.IOException;

/**
 * NpcSoHuuDao — Handler cho NPC Bia Sở Hữu Đảo (idmenu -137).
 *
 * Xuất hiện tại các đảo lớn (Map 25, 33, 49, 69, 83) và Đấu Trường Chiếm Đảo (Map 261..265).
 * Chức năng:
 *   1. Vào Chiếm Đảo (Cần Bang Hội, mở theo lịch)
 *   2. Nhận thưởng Chiếm Đảo
 *   3. Xem thông tin đảo này (Bang đang giữ, cấp độ, trạng thái, lịch mở)
 *   4. Tất cả các đảo (Danh sách 5 đảo và bang chiếm giữ trên toàn server)
 *   5. Đóng
 */
public class NpcSoHuuDao implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-137};
    }

    @Override
    public String getChatText() {
        return "Bia Sở Hữu Đảo — Nơi ghi danh các Bang Hội hùng mạnh chiếm giữ lãnh địa!";
    }

    @Override
    public String[] getChatTexts() {
        return new String[]{
            "Bia Sở Hữu Đảo — Nơi ghi danh các Bang Hội hùng mạnh!",
            "Chiếm đảo để khẳng định vị thế Bang Hội trên Đại Hải Trình!",
            "Chiến thắng Chiếm Đảo nhận 500,000 Beri + 20 Đá Hải Thạch!",
            "Tham gia Chiếm Đảo vào 19h00 các ngày Thứ 2, Thứ 4, Thứ 6!"
        };
    }

    @Override
    public void onInitNpcForMap(Zone zone) {
        if (zone == null || zone.template == null) return;
        int mapId = zone.template.id;
        // Tự động đảm bảo NPC -137 có mặt trên các map đảo nếu chưa có
        if (mapId == 25 || mapId == 33 || mapId == 49 || mapId == 69 || mapId == 83
                || (mapId >= 261 && mapId <= 265)) {
            if (!hasNpc(zone, (short) -137)) {
                Npc soHuuDao = iNpc.createNpc(
                    (short) -137,
                    "Sở hữu đảo",
                    "Sở hữu đảo",
                    "Bia ghi danh Bang Hội chiếm giữ đảo",
                    (short) (zone.template.maxW / 2),
                    (short) 170,
                    (byte) 98, (byte) -1, (byte) 20, (byte) 20,
                    (short) 0, (short) 0, new short[]{-1, -1, -1, -1}
                );
                zone.template.npcs.add(soHuuDao);
            }
        }
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        if (p == null || p.isdie) return;
        short type = (npc != null) ? npc.idmenu : -137;

        int islandId = ChiemDaoMenu.getIslandIdFromMap(p.map != null && p.map.template != null ? p.map.template.id : 25);
        Clan occupying = ChiemDao.getClanTop(islandId);
        String islandName = ChiemDaoMenu.getIslandName(islandId);
        String location = ChiemDaoMenu.getIslandGateLocation(islandId);
        String occupyStr = (occupying != null) ? "Bang đang giữ: [" + occupying.name + "]" : "Đảo chưa có bang chiếm giữ";
        String title = islandName + " (" + location + ") - " + occupyStr;

        ScheduleConfig cfg = TimedDungeonManager.gI().getConfigs().get("CHIEM_DAO");
        String timeInfo = (cfg != null) ? cfg.getTimeRangeString() : "19h00 - 20h00 (T2, T4, T6)";

        p.menus.clear();

        // 1. Vào Chiếm Đảo
        p.menus.add(new Menu("Vào Chiếm Đảo", (short) -1, () -> {
            try {
                boolean isOpen = ChiemDao.isOpen() || p.admin > 0;
                if (!isOpen) {
                    p.getService().send_box_ThongBao_OK(
                        "Chiếm Đảo Bang Hội chưa đến giờ mở cửa!\n" +
                        "- " + islandName + " (" + location + ")\n" +
                        "- " + occupyStr + "\n" +
                        "- Lịch mở: " + timeInfo + "\n" +
                        "Hãy quay lại khi đến giờ mở cửa!"
                    );
                    return;
                }

                if (p.clan == null) {
                    p.getService().send_box_ThongBao_OK("Bạn cần gia nhập Bang Hội để tham gia Chiếm Đảo!");
                    return;
                }

                int targetMapId = ChiemDaoMenu.getGateTargetMapId(islandId);
                Zone[] targetZones = Zone.getMapByID(targetMapId);
                if (targetZones == null || targetZones.length == 0 || targetZones[0] == null) {
                    p.getService().send_box_ThongBao_OK("Bản đồ Chiếm Đảo hiện không khả dụng!");
                    return;
                }

                map.Vgo vgo = new map.Vgo();
                vgo.map_go = new Zone[]{targetZones[0]};
                vgo.xnew = 349;
                vgo.ynew = 345;

                p.save_previous_map();
                p.isPassGateCheck = true;
                try {
                    p.goto_map(vgo);
                    p.getService().send_box_ThongBao_OK("Bạn đã tiến vào Cổng " + islandName + "!\nHãy tiến qua cửa để vào Đấu Trường cùng Bang Hội chiến đấu!");
                } catch (Exception e) {
                    p.getService().send_box_ThongBao_OK("Lỗi khi vào phó bản Chiếm Đảo: " + e.getMessage());
                } finally {
                    p.isPassGateCheck = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // 2. Nhận thưởng Chiếm Đảo
        p.menus.add(new Menu("Nhận thưởng Chiếm Đảo", (short) -1, () -> {
            try {
                DungeonGiftMenu.openMenuByType(p, "CHIEM_DAO");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // 3. Xem thông tin đảo này
        p.menus.add(new Menu("Xem thông tin đảo", (short) -1, () -> {
            try {
                Clan curOccupy = ChiemDao.getClanTop(islandId);
                String curOccupyStr = (curOccupy != null)
                    ? "Bang đang giữ: [" + curOccupy.name + "]\nCấp Bang: " + curOccupy.level + " | XP: " + curOccupy.xp
                    : "Đảo chưa có bang chiếm giữ";

                p.getService().send_box_ThongBao_OK(
                    "ĐẢO: " + islandName.toUpperCase() + " (" + location + ")\n" +
                    "-----------------\n" +
                    curOccupyStr + "\n" +
                    "Trạng thái: " + (ChiemDao.isOpen() ? "ĐANG MỞ CỬA" : "CHƯA MỞ") + "\n" +
                    "Lịch mở: " + timeInfo + "\n" +
                    "Phần thưởng: 500,000 Beri + 20 Đá Hải Thạch cho bang chiến thắng"
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // 4. Danh sách tất cả các đảo
        p.menus.add(new Menu("Tất cả các đảo", (short) -1, () -> {
            try {
                ChiemDaoMenu.showAllIslandsInfo(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // 5. Đóng
        p.menus.add(new Menu("Đóng", (short) -1, () -> {}));

        // Mở dynamic menu
        p.getService().openDynamicMenu(type, title, p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        if (p.menus != null && index >= 0 && index < p.menus.size()) {
            p.menus.get(index).execute(p, index);
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
