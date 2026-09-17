package zinterfaces.menus;

import model.Player;
import model.Menu;
import zinterfaces.iNpc;
import zinterfaces.iMenu;
import map.Npc;
import map.Zone;
import map.zones.WorldWar;
import map.zones.TranChienLon;
import template.ItemTemplate4;
import template.GiftBox;
import network.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubMenuWorldWar implements iNpc, iMenu {

    private static final SubMenuWorldWar instance = new SubMenuWorldWar();

    public static SubMenuWorldWar gI() {
        return instance;
    }

    @Override
    public short[] getId() {
        return new short[]{-991, -995};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        openWorldWarMenu(p);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        handleMenu(p, (short) -991, index);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        handleMenu(p, (short) menuId, index);
    }

    @Override
    public void onInitNpcForMap(Zone zone) {
        if (zone == null || zone.template == null) return;
        if (zone.template.id >= 272 && zone.template.id <= 275) {
            boolean hasNpc = false;
            for (Npc n : zone.template.npcs) {
                if (n != null && (n.idmenu == -991 || n.idmenu == -106)) {
                    hasNpc = true;
                    break;
                }
            }
            if (!hasNpc) {
                short centerX = (short) (zone.template.maxW / 2);
                short centerY = (short) (zone.template.maxH / 2);
                Npc warNpc = iNpc.createNpc(
                        (short) -991,
                        "Quản Lý Trận Chiến",
                        "Quản Lý Lễ Hội",
                        "Chào mừng đến Trận Chiến Lớn! Hãy chiến đấu hết mình vì phe phái của bạn!",
                        centerX, centerY,
                        (byte) 1, (byte) -1, (byte) 20, (byte) 20,
                        (short) 24, (short) 2, new short[]{-1, -1, -1, -1}
                );
                warNpc.dataFrame = new byte[]{24, 2};
                zone.template.npcs.add(warNpc);
            }
        }
    }

    public static void openWorldWarMenu(Player p) throws IOException {
        p.menus.clear();
        p.menus.add(new Menu("Thông Tin Trận Đấu", (short) 138, () -> {
            try {
                handleThongTin(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new Menu("Chọn Phe", (short) 171, () -> {
            try {
                openSelectFactionMenu(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new Menu("Nhận thưởng TOP 3", (short) 138, () -> {
            try {
                handleNhanThuong(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        boolean inWorldWar = (p.map != null && p.map.template != null
                && p.map.template.id >= 272 && p.map.template.id <= 275);
        if (inWorldWar) {
            if (p.isSpectator) {
                p.menus.add(new Menu("Thoát xem", (short) 113, () -> {
                    try {
                        p.exitSpectatorMode();
                        p.getService().send_box_ThongBao_OK("Đã thoát Chế Độ Xem và trở về vị trí cũ!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
            } else {
                p.menus.add(new Menu("Xem trận đấu", (short) 117, () -> {
                    try {
                        handleXemTranDau(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
            }
            p.menus.add(new Menu("Rời sảnh chiến", (short) 113, () -> {
                try {
                    handleRoiPhongCho(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        } else {
            p.menus.add(new Menu("Tham gia chiến trường", (short) 171, () -> {
                try {
                    handleThamGia(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            p.menus.add(new Menu("Xem trận đấu", (short) 117, () -> {
                try {
                    handleXemTranDau(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }

        p.getService().openDynamicMenu(-991, "Quản Lý Trận Chiến", p.menus);
    }

    public static void openSelectFactionMenu(Player p) throws IOException {
        p.menus.clear();
        p.menus.add(new Menu("Phe Hải Quân", (short) 171, () -> {
            try {
                selectFaction(p, (byte) 1, "HẢI QUÂN");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new Menu("Phe Hải Tặc", (short) 117, () -> {
            try {
                selectFaction(p, (byte) 2, "HẢI TẶC");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new Menu("Phe Quân Cách Mạng", (short) 125, () -> {
            try {
                selectFaction(p, (byte) 3, "QUÂN CÁCH MẠNG");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        p.getService().openDynamicMenu(-995, "Chọn Phe", p.menus);
    }

    public static void selectFaction(Player p, byte selectedFaction, String factionName) throws IOException {
        if (selectedFaction > 0) {
            p.huongnghiep = selectedFaction;
            p.ensureFactionSkill();
            boolean inWorldWar = (p.map != null && p.map.template != null
                    && p.map.template.id >= 272 && p.map.template.id <= 275);
            if (inWorldWar) {
                WorldWar.setType(p);
                p.getService().send_box_ThongBao_OK("BẠN ĐÃ CHỌN GIA NHẬP PHE: " + factionName + "!");
            } else {
                p.getService().send_box_ThongBao_OK("BẠN ĐÃ CHỌN GIA NHẬP PHE: " + factionName + "!\nHãy chọn 'Tham gia chiến trường' hoặc 'Xem trận đấu' để tiến vào.");
            }
            p.send_skill();
            p.update_info_to_all();
            historys.zLog.gI().add_log(p, "HUONG_NGHIEP", "Gia nhập phe " + factionName);
        }
    }

    public static void handleThamGia(Player p) throws IOException {
        if (p.huongnghiep == 0) {
            openSelectFactionMenu(p);
            p.getService().send_box_ThongBao_OK("Hãy chọn 1 trong 3 phe (Hải Quân, Hải Tặc, Quân Cách Mạng) trước khi vào sảnh chiến!");
            return;
        }
        boolean isOpen = activities.TimedDungeonManager.gI().isDungeonOpen("WORLD_WAR") || WorldWar.status != WorldWar.STATUS_CLOSE;
        if (!isOpen && p.admin == 0) {
            p.getService().send_box_ThongBao_OK("Đại Chiến Thế Giới hiện chưa mở cửa!\n"
                    + activities.TimedDungeonManager.gI().getDungeonStatusMessage("WORLD_WAR"));
            return;
        }
        if (WorldWar.status == WorldWar.STATUS_CLOSE && (p.admin > 0 || activities.TimedDungeonManager.gI().isDungeonOpen("WORLD_WAR"))) {
            WorldWar.startRegister();
        }

        // Nếu người chơi đã ở trong map phó bản 272..275
        if (p.map != null && p.map.template != null && p.map.template.id >= 272 && p.map.template.id <= 275) {
            WorldWar.setType(p);
            p.ensureFactionSkill();
            p.send_skill();
            bot.botplayer.BotPlayerManager.dispatchBotsToWorldWar();
            p.getService().send_box_ThongBao_OK("Bạn đang ở trong sảnh chiến Trận Chiến Lớn!");
            return;
        }

        // Lưu lại vị trí map trước khi tiến vào Trận Chiến Lớn
        p.save_previous_map();

        int targetMapId;
        if (p.level < 50) {
            targetMapId = 272;
        } else if (p.level < 70) {
            targetMapId = 273;
        } else if (p.level < 90) {
            targetMapId = 274;
        } else {
            targetMapId = 275;
        }
        map.Zone[] zones = map.Zone.getMapByID(targetMapId);
        if (zones != null && zones.length > 0 && zones[0] != null) {
            map.Vgo vgo = new map.Vgo();
            vgo.map_go = new map.Zone[]{zones[0]};
            vgo.xnew = (short) (vgo.map_go[0].template.maxW / 2);
            vgo.ynew = (short) (vgo.map_go[0].template.maxH / 2);
            p.goto_map(vgo);
            WorldWar.setType(p);
            p.ensureFactionSkill();
            p.send_skill();
            bot.botplayer.BotPlayerManager.dispatchBotsToWorldWar();
            if (WorldWar.status == WorldWar.STATUS_REGISTER) {
                p.getService().send_time_cool_down(TranChienLon.registerStartTime + 30_000L, "Chờ bắt đầu", 0);
            } else if (WorldWar.status == WorldWar.STATUS_WAR) {
                p.getService().send_time_cool_down(TranChienLon.warStartTime + 600_000L, "Thời gian", 0);
            }
            p.getService().send_box_ThongBao_OK("Bạn đã tiến vào sảnh chiến Trận Chiến Lớn!");
        } else {
            map.Zone[] fallbackZones = map.Zone.getMapByID(1000);
            if (fallbackZones != null && fallbackZones.length > 0 && fallbackZones[0] != null) {
                map.Vgo vgo = new map.Vgo();
                vgo.map_go = new map.Zone[]{fallbackZones[0]};
                vgo.xnew = (short) core.ZUtil.random(150, 350);
                vgo.ynew = (short) core.ZUtil.random(200, 300);
                p.goto_map(vgo);
                WorldWar.setType(p);
                p.ensureFactionSkill();
                p.send_skill();
                bot.botplayer.BotPlayerManager.dispatchBotsToWorldWar();
                p.getService().send_box_ThongBao_OK("Bạn đã tiến vào sảnh chiến Trận Chiến Lớn!");
            } else {
                p.getService().send_box_ThongBao_OK("Không tìm thấy bản đồ Trận Chiến Lớn!");
            }
        }
    }

    public static void handleThongTin(Player p) throws IOException {
        String statusStr;
        if (WorldWar.status == WorldWar.STATUS_WAR) statusStr = "Đang Giao Tranh";
        else if (WorldWar.status == WorldWar.STATUS_REGISTER) statusStr = "Trong Phòng Chờ Đăng Ký";
        else statusStr = "Đang Trao Thưởng / Đóng";

        String factionStr = (p.huongnghiep == 1) ? "Hải Quân" : ((p.huongnghiep == 2) ? "Hải Tặc" : ((p.huongnghiep == 3) ? "Quân Cách Mạng" : "Chưa chọn"));

        String txt = "THONG TIN TRAN CHIEN LON\n"
                + "- Trang thai: " + statusStr + "\n"
                + "- Phe cua ban: " + factionStr + "\n"
                + "-----------------------------\n"
                + WorldWar.findTop();
        p.getService().send_box_ThongBao_OK(txt);
    }

    public static void handleNhanThuong(Player p) throws IOException {
        p.getService().send_box_ThongBao_OK("Phần thưởng Trận Chiến Lớn đã được hệ thống tự động gửi vào Hộp Thư của bạn!\nVui lòng mở Hộp Thư để nhận.");
    }

    public static void handleXemTranDau(Player p) throws IOException {
        int targetMapId = 272;
        if (p.level >= 90) targetMapId = 275;
        else if (p.level >= 70) targetMapId = 274;
        else if (p.level >= 50) targetMapId = 273;

        map.Zone[] zones = map.Zone.getMapByID(targetMapId);
        if (zones != null && zones.length > 0 && zones[0] != null) {
            p.enterSpectatorMode(zones[0], (short) (zones[0].template.maxW / 2), (short) (zones[0].template.maxH / 2));
            bot.botplayer.BotPlayerManager.dispatchBotsToWorldWar();
            p.getService().send_box_ThongBao_OK("BAN DA VAO CHE DO XEM TRAN DAU!\n- Ban dang trong trang thai quan sat khong bi nhan sat thuong.\n- Chon NPC hoac mo Menu de Thoat che do xem tro ve vi tri cu.");
        } else {
            p.getService().send_box_ThongBao_OK("Không tìm thấy map trận đấu để xem!");
        }
    }

    public static void handleRoiPhongCho(Player p) throws IOException {
        if (p.isSpectator) {
            p.exitSpectatorMode();
            p.getService().send_box_ThongBao_OK("Đã thoát Chế Độ Xem và trở về vị trí cũ!");
        } else {
            p.type_pk = -1;
            p.return_to_previous_map();
            p.getService().send_box_ThongBao_OK("Đã rời sảnh chiến và trở về vị trí trước đó!");
            p.send_skill();
        }
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (idNPC == -995 || idNPC == 499) {
            byte selectedFaction = 0;
            String factionName = "";
            if (index == 0) {
                selectedFaction = 1;
                factionName = "HẢI QUÂN";
            } else if (index == 1) {
                selectedFaction = 2;
                factionName = "HẢI TẶC";
            } else if (index == 2) {
                selectedFaction = 3;
                factionName = "QUÂN CÁCH MẠNG";
            }
            selectFaction(p, selectedFaction, factionName);
            return;
        }

        boolean inWorldWar = (p.map != null && p.map.template != null
                && p.map.template.id >= 272 && p.map.template.id <= 275);

        if (inWorldWar) {
            switch (index) {
                case 0:
                    handleThongTin(p);
                    break;
                case 1:
                    openSelectFactionMenu(p);
                    break;
                case 2:
                    handleNhanThuong(p);
                    break;
                case 3:
                    if (p.isSpectator) {
                        p.exitSpectatorMode();
                        p.getService().send_box_ThongBao_OK("Đã thoát Chế Độ Xem và trở về vị trí cũ!");
                    } else {
                        handleXemTranDau(p);
                    }
                    break;
                case 4:
                    handleRoiPhongCho(p);
                    break;
                default:
                    break;
            }
        } else {
            switch (index) {
                case 0:
                    handleThongTin(p);
                    break;
                case 1:
                    openSelectFactionMenu(p);
                    break;
                case 2:
                    handleNhanThuong(p);
                    break;
                case 3:
                    handleThamGia(p);
                    break;
                case 4:
                    handleXemTranDau(p);
                    break;
                default:
                    break;
            }
        }
    }
}
