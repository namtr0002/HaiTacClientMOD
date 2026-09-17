package activities;

import activities.TimedDungeonManager.ScheduleConfig;
import clan.Clan;
import map.Zone;
import map.zones.ChiemDao;
import model.Menu;
import model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ChiemDaoMenu — Menu vào Phó Bản Chiếm Đảo Bang Hội.
 *
 * Player chạm Vgo cổng đảo (25, 33, 49, 69, 83) -> Mở giao diện cuộn giấy:
 *   1. Vào Chiếm Đảo (Cần Bang Hội)
 *   2. Xem thông tin đảo (Bang đang chiếm, cấp bang, lịch mở, phần thưởng)
 *   3. Đóng
 *
 * Sử dụng kiến trúc Lambda Dynamic Menu (model.Menu) chuẩn OOP.
 */
public class ChiemDaoMenu {

    /**
     * Mở menu Chiếm Đảo cho player tại cổng islandId.
     * islandId: 25, 33, 49, 69, 83 (map cổng ngõ đảo).
     */
    public static void openMenu(Player p, int islandId) throws IOException {
        if (p == null || p.isdie) return;

        Clan occupying = ChiemDao.getClanTop(islandId);
        String islandName = getIslandName(islandId);
        String occupyStr = (occupying != null) ? "Bang đang giữ: [" + occupying.name + "]" : "Đảo chưa có bang chiếm giữ";
        String title = islandName + " - " + occupyStr;

        ScheduleConfig cfg = TimedDungeonManager.gI().getConfigs().get("CHIEM_DAO");
        String timeInfo = (cfg != null) ? cfg.getTimeRangeString() : "19h00 - 20h00 (T2, T4, T6)";

        List<Menu> menus = new ArrayList<>();

        // 1. Vào Chiếm Đảo
        menus.add(new Menu("Vào Chiếm Đảo", (short) -1, () -> {
            try {
                boolean isOpen = ChiemDao.isOpen() || p.admin > 0;
                if (!isOpen) {
                    p.getService().send_box_ThongBao_OK(
                        "Chiếm Đảo Bang Hội chưa đến giờ mở cửa!\n" +
                        "- " + islandName + " (" + getIslandGateLocation(islandId) + ")\n" +
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

                int targetMapId = getGateTargetMapId(islandId);
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
        menus.add(new Menu("Nhận thưởng Chiếm Đảo", (short) -1, () -> {
            try {
                DungeonGiftMenu.openMenuByType(p, "CHIEM_DAO");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // 3. Xem thông tin đảo
        menus.add(new Menu("Xem thông tin đảo", (short) -1, () -> {
            try {
                Clan curOccupy = ChiemDao.getClanTop(islandId);
                String curOccupyStr = (curOccupy != null)
                    ? "Bang đang giữ: [" + curOccupy.name + "]\nCấp Bang: " + curOccupy.level + " | XP: " + curOccupy.xp
                    : "Đảo chưa có bang chiếm giữ";

                p.getService().send_box_ThongBao_OK(
                    "ĐẢO: " + islandName.toUpperCase() + " (" + getIslandGateLocation(islandId) + ")\n" +
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
        menus.add(new Menu("Tất cả các đảo", (short) -1, () -> {
            try {
                showAllIslandsInfo(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // 5. Đóng
        menus.add(new Menu("Đóng", (short) -1, () -> {}));

        // Mở dynamic menu với callback lambda (sử dụng islandId làm NPC ID)
        p.getService().openDynamicMenu((short) islandId, title, menus);
    }

    /**
     * Hiển thị bảng thông tin sở hữu toàn bộ 5 đảo trên server.
     */
    public static void showAllIslandsInfo(Player p) {
        StringBuilder sb = new StringBuilder();
        sb.append("DANH SÁCH CHIẾM ĐẢO BANG HỘI:\n");
        sb.append("-----------------------------\n");
        int[] islands = {25, 33, 49, 69, 83};
        for (int id : islands) {
            String name = getIslandName(id);
            String location = getIslandGateLocation(id);
            Clan c = ChiemDao.getClanTop(id);
            String cName = (c != null) ? "[" + c.name + "] (Cấp " + c.level + ")" : "Chưa có bang chiếm giữ";
            sb.append("• ").append(name).append(" (").append(location).append("): ").append(cName).append("\n");
        }
        sb.append("-----------------------------\n");
        ScheduleConfig cfg = TimedDungeonManager.gI().getConfigs().get("CHIEM_DAO");
        String timeInfo = (cfg != null) ? cfg.getTimeRangeString() : "19h00 - 20h00 (T2, T4, T6)";
        sb.append("Trạng thái: ").append(ChiemDao.isOpen() ? "ĐANG MỞ CỬA" : "CHƯA MỞ").append("\n");
        sb.append("Lịch mở: ").append(timeInfo);
        try {
            p.getService().send_box_ThongBao_OK(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy ID cổng đảo chính (25, 33, 49, 69, 83) từ bất kỳ map nào (làng, cổng, đấu trường).
     */
    public static int getIslandIdFromMap(int mapId) {
        switch (mapId) {
            case 25:
            case 254:
            case 261:
                return 25;
            case 33:
            case 255:
            case 262:
                return 33;
            case 49:
            case 256:
            case 263:
                return 49;
            case 69:
            case 257:
            case 264:
                return 69;
            case 83:
            case 258:
            case 265:
                return 83;
            default:
                return 25;
        }
    }

    /**
     * Fallback cho trường hợp ClientYesNo gọi lại (nếu có).
     */
    public static void handleMenu(Player p, int index) throws IOException {
        if (p.data_yesno == null || p.data_yesno.length < 2) return;
        int islandId = p.data_yesno[1];
        p.data_yesno = null;

        if (index == 0) {
            boolean isOpen = ChiemDao.isOpen() || p.admin > 0;
            if (!isOpen) {
                p.getService().send_box_ThongBao_OK("Chiếm Đảo Bang Hội chưa mở!");
                return;
            }
            if (p.clan == null) {
                p.getService().send_box_ThongBao_OK("Bạn cần gia nhập Bang Hội để tham gia Chiếm Đảo!");
                return;
            }
            int targetMapId = getGateTargetMapId(islandId);
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
            } finally {
                p.isPassGateCheck = false;
            }
        }
    }

    public static int getGateTargetMapId(int islandId) {
        switch (islandId) {
            case 25: return 254;
            case 33: return 255;
            case 49: return 256;
            case 69: return 257;
            case 83: return 258;
            default: return 254;
        }
    }

    /**
     * Tên chuẩn của 5 Đảo Chiếm Đảo trong game Hải Tặc Tí Hon.
     */
    public static String getIslandName(int islandId) {
        switch (islandId) {
            case 25:
            case 254:
            case 261:
                return "Đảo Tiền bạc sơ cấp";
            case 33:
            case 255:
            case 262:
                return "Đảo Châu báu sơ cấp";
            case 49:
            case 256:
            case 263:
                return "Đảo Danh vọng";
            case 69:
            case 257:
            case 264:
                return "Đảo Tiền bạc trung cấp";
            case 83:
            case 258:
            case 265:
                return "Đảo Châu báu trung cấp";
            default:
                return "Đảo ID-" + islandId;
        }
    }

    /**
     * Tên map làng/cửa ngõ đặt cổng vào của từng đảo.
     */
    public static String getIslandGateLocation(int islandId) {
        switch (islandId) {
            case 25:
            case 254:
            case 261:
                return "Làng Sirup";
            case 33:
            case 255:
            case 262:
                return "Nhà hàng Barati";
            case 49:
            case 256:
            case 263:
                return "Thị trấn khởi đầu";
            case 69:
            case 257:
            case 264:
                return "Thị Trấn Whiskay";
            case 83:
            case 258:
            case 265:
                return "Thị Trấn Horn";
            default:
                return "Bản đồ " + islandId;
        }
    }
}
