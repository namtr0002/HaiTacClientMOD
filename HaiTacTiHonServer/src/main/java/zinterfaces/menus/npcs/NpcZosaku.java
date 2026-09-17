package zinterfaces.menus.npcs;

import model.Menu;
import model.Player;
import core.ZUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.Npc;
import map.Vgo;
import map.Zone;
import map.zones.ZSanTrum;
import model.Quest;
import zinterfaces.menus.SubMenuWorldWar;
import zinterfaces.iNpc;
import zinterfaces.menus.SubMenuSanTrum;

/**
 * NPC Zosaku — cổng vào các phó bản, săn trùm, PvP, vượt ải đơn.
 * Tự quản lý toàn bộ menu + handle, không phụ thuộc MenuController.
 */
public class NpcZosaku implements iNpc {

    // ID NPC Zosaku xuất hiện trên các map + ID sub-menu động
    @Override
    public short[] getId() {
        return new short[]{-106, -91, -71, -48, 988, 9888};
    }

    @Override
    public String getChatText() {
        return "Nếu ngươi muốn khẳng định sức mạnh của mình thì hãy đến tìm ta. Ta sẽ luôn ở đây.";
    }

    @Override
    public String[] getChatTexts() {
        return new String[]{
            "Nếu ngươi muốn khẳng định sức mạnh của mình thì hãy đến tìm ta. Ta sẽ luôn ở đây.",
            "Phó bản săn trùm sẽ đem lại nhiều phần thưởng quý giá!",
            "Hãy lập nhóm cùng đồng đội để chinh phục các tầng phó bản!"
        };
    }

    @Override
    public void onInitNpcForMap(Zone zone) {
        if (zone == null || zone.template == null) return;
        
        // 1. Tự động thêm NPC Quản Lý Trận Chiến vào các map Trận Chiến Lớn (272..275)
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

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        if (menuId == 988) {
            handleAiDonLevelSelect(p, index);
            return;
        }
        if (menuId == 9888) {
            if (p.menus != null && index >= 0 && index < p.menus.size()) {
                p.menus.get(index).execute(p, index);
            }
            return;
        }
        handleMenu(p, index);
    }

    private void handleAiDonLevelSelect(Player p, int index) throws IOException {
        if (p.aiDonLevel < index) {
            p.getService().send_box_ThongBao_OK("Hãy hoàn thành cấp độ " + (p.aiDonLevel + 3) + " trước");
            return;
        }
        if (index >= 0 && index <= 12 && p.dungeon == null) {
            int save = index;
            p.data_yesno = new int[]{save};
            if (save < 7) {
                p.setyesNoDialog(new model.YesNoDialog(p, 52, "Thông báo",
                        ("Vào phó bản đơn cấp độ " + (index + 3) + " cần 1 chìa khóa phó bản"),
                        new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                p.getService().startYesNo();
            } else {
                p.setyesNoDialog(new model.YesNoDialog(p, 52, "Thông báo",
                        ("Vào phó bản đơn cấp độ " + (index + 3) + " cần 2 chìa khóa phó bản"),
                        new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                p.getService().startYesNo();
            }
        } else if (p.dungeon != null) {
            p.dungeon.mobs.clear();
            for (int i = 0; i < p.dungeon.maps.size(); i++) {
                p.dungeon.maps.get(i).stop_map();
            }
            p.dungeon = null;
            p.getService().send_box_ThongBao_OK("Có lỗi xảy ra");
        }
    }



    // -------------------------------------------------------------------------
    // Gửi menu theo map player đang đứng
    // -------------------------------------------------------------------------
    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        int npcId = npc != null ? npc.idmenu : -106;
        switch (p.map.template.id) {
            case 9:
            case 25:
                sendDynMenu(p, npcId, "Zosaku",
                    new String[]{"Săn trùm", "Thách đấu", "Vượt liên ải", "Trận chiến lớn", "Vượt ải đơn", "Đảo Kho Báu"},
                    new short[]{136, 137, 138, 146, 111, 139});
                break;
            case 41:
                sendDynMenu(p, npcId, "Zosaku",
                    new String[]{"Săn trùm", "Thách đấu", "Vượt liên ải", "Trận chiến lớn", "Bảo vệ kho báu Namie", "Đảo Kho Báu"},
                    new short[]{136, 137, 138, 146, 139, 139});
                break;
            case 49:
                sendDynMenu(p, npcId, "Zosaku",
                    new String[]{"Săn trùm", "Thách đấu", "Vượt liên ải", "Trận chiến lớn", "Lệnh truy nã", "Đảo Kho Báu"},
                    new short[]{136, 137, 138, 146, 160, 139});
                break;
            case 33:
            case 79:
                sendDynMenu(p, npcId, "Zosaku",
                    new String[]{"Săn trùm", "Thách đấu", "Phó bản khổng lồ", "Vượt liên ải", "Trận chiến lớn", "Đảo Kho Báu"},
                    new short[]{136, 137, 142, 138, 146, 139});
                break;
            case 83:
            case 189:
            case 191:
                sendDynMenu(p, npcId, "Zosaku",
                    new String[]{"Săn trùm", "Thách đấu", "Vượt siêu liên ải", "Trận chiến lớn", "Đảo Kho Báu"},
                    new short[]{136, 137, 159, 146, 139});
                break;
            case 17:
            case 69:
            case 93:
            case 113:
            default:
                String lienAiText = (p.map.template.id >= 83 || p.level >= 80) ? "Vượt siêu liên ải" : "Vượt liên ải";
                short lienAiIcon = (p.map.template.id >= 83 || p.level >= 80) ? (short) 159 : (short) 138;
                sendDynMenu(p, npcId, "Zosaku",
                    new String[]{"Săn trùm", "Thách đấu", lienAiText, "Trận chiến lớn", "Đảo Kho Báu"},
                    new short[]{136, 137, lienAiIcon, 146, 139});
                break;
        }
    }

    // -------------------------------------------------------------------------
    // Xử lý lựa chọn menu từ client
    // -------------------------------------------------------------------------
    @Override
    public void handleMenu(Player p, int index) throws IOException {
        switch (index) {

            case 0: { // Săn trùm
                if (p.party != null && p.party.list.size() > 1) {
                    if (!isLeader(p)) {
                        p.getService().send_box_ThongBao_OK("Bạn không phải trưởng nhóm");
                        return;
                    }
                    List<String> missingKeys = new ArrayList<>();
                    for (Player member : p.party.list) {
                        if (member != null && !member.isBot && !member.isDe && !(member instanceof model.DeTu) && !(member instanceof bot.mercenary.MercenaryBot)) {
                            if (member.get_key_boss() < 1) {
                                missingKeys.add(member.name);
                            }
                        }
                    }
                    if (!missingKeys.isEmpty()) {
                        p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 1 chìa khóa phó bản");
                        return;
                    }
                } else {
                    if (p.get_key_boss() < 1) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ 1 chìa khóa phó bản");
                        return;
                    }
                }
                p.sanTrumId = ZUtil.random(ZSanTrum.BOSS.length);
                p.menus.clear();
                for (int i = 0; i < 11; i++) {
                    final int levelIndex = i;
                    final int lv = (i + 1) * 10;
                    p.menus.add(new Menu(String.format("Boss %s Cấp: %s", ZSanTrum.NAME[p.sanTrumId], lv), () -> {
                        try {
                            SubMenuSanTrum menuHandler = new SubMenuSanTrum();
                            menuHandler.handleMenu(p, (short) 701, levelIndex);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }));
                }
                p.getService().openDynamicMenu(701, "Lựa chọn cấp độ boss " + ZSanTrum.NAME[p.sanTrumId], p.menus);
                break;
            }

            case 1: { // Thách đấu PvP
                if (p.isDe || p instanceof model.DeTu) {
                    p.getService().send_box_ThongBao_OK("Đệ tử không thể tham gia PvP!");
                    return;
                }
                p.save_previous_map();
                Vgo vgo = new Vgo();
                vgo.map_go = Zone.getMapByID(1000);
                if (vgo.map_go != null) {
                    vgo.xnew = (short) ZUtil.random(150, 300);
                    vgo.ynew = (short) ZUtil.random(200, 300);
                    p.goto_map(vgo);
                } else {
                    p.getService().send_box_ThongBao_OK("Bản đồ quá tải!");
                }
                break;
            }

            case 2: { // Vượt liên ải / siêu liên ải HOẶC Phó bản khổng lồ (Map 33 & 79)
                if (p.map.template.id == 33 || p.map.template.id == 79) {
                    openPhoBanKhongLo(p);
                    break;
                }
                switch (p.map.template.id) {
                    case 83:
                    case 189:
                    case 191:
                        startTableTickOption(p, 68, 5, "Phó bản Siêu Liên tầng");
                        break;
                    case 9:
                    case 17:
                    case 25:
                    case 41:
                    case 49:
                    case 69:
                    case 93:
                    case 113:
                        startTableTickOption(p, 62, 1, "Phó bản Liên tầng");
                        break;
                    default:
                        if (p.map.template.id >= 83 || p.level >= 80) {
                            startTableTickOption(p, 68, 5, "Phó bản Siêu Liên tầng");
                        } else {
                            startTableTickOption(p, 62, 1, "Phó bản Liên tầng");
                        }
                        break;
                }
                break;
            }

            case 3: { // Trận chiến lớn — WorldWar HOẶC Vượt liên ải (Map 33 & 79)
                if (p.map.template.id == 33 || p.map.template.id == 79) {
                    startTableTickOption(p, 62, 1, "Phó bản Liên tầng");
                    break;
                }
                SubMenuWorldWar.openWorldWarMenu(p);
                break;
            }

            case 4: { // Chức năng đặc biệt theo map HOẶC Trận chiến lớn (Map 33 & 79) HOẶC Đảo Kho Báu (Map 5 lựa chọn)
                if (p.map.template.id == 33 || p.map.template.id == 79) {
                    SubMenuWorldWar.openWorldWarMenu(p);
                    break;
                }
                switch (p.map.template.id) {
                    case 9:
                    case 25: { // Vượt ải đơn
                        String[] name = new String[12];
                        short[] icon = new short[12];
                        for (int i = 0; i < 12; i++) {
                            name[i] = "Cấp độ " + (i + 3);
                            if (p.aiDonLevel < i)      icon[i] = 168;
                            else if (i < 2)            icon[i] = 164;
                            else if (i < 5)            icon[i] = 165;
                            else if (i < 7)            icon[i] = 166;
                            else                       icon[i] = 167;
                        }
                        sendDynMenu(p, 988, "Vượt ải đơn", name, icon);
                        break;
                    }
                    case 49: { // Lệnh truy nã
                        if (p.isDe || p instanceof model.DeTu) {
                            p.getService().send_box_ThongBao_OK("Đệ tử không thể tham gia Lệnh truy nã!");
                            return;
                        }
                        p.save_previous_map();
                        Vgo vgo = new Vgo();
                        vgo.map_go = Zone.getMapByID(119);
                        if (vgo.map_go != null) {
                            vgo.xnew = (short) ZUtil.random(120, 380);
                            vgo.ynew = (short) ZUtil.random(230, 330);
                            p.goto_map(vgo);
                        } else {
                            p.getService().send_box_ThongBao_OK("Zone đầy hãy quay lại sau");
                        }
                        break;
                    }
                    case 41: { // Bảo vệ kho báu Namie
                        startTableTickOption(p, 63, 2, "Bảo vệ kho báu Namie");
                        break;
                    }
                    default: {
                        handleDaoKhoBau(p);
                        break;
                    }
                }
                break;
            }

            case 5: { // Đảo Kho Báu (cho các map có chức năng đặc biệt ở slot 4 như 9, 25, 41, 49, 33, 79)
                handleDaoKhoBau(p);
                break;
            }
        }
    }

    private void openPhoBanKhongLo(Player p) throws IOException {
        if (p.clan == null) {
            p.getService().send_box_ThongBao_OK("Bạn chưa có Băng Hải Tặc! Hãy tạo hoặc gia nhập Băng trước khi tham gia Phó Bản Khổng Lồ.");
            return;
        }
        if (p.clan.map_create != null && p.clan.map_create.map_little_garden != null) {
            int team = (p.clan.map_create.map_little_garden.clan1 != null && p.clan.map_create.map_little_garden.clan1.equals(p.clan)) ? 4 : 5;
            p.type_pk = (byte) team;
            Vgo vgo = new Vgo();
            vgo.map_go = new Zone[]{p.clan.map_create};
            vgo.xnew = (short) (team == 4 ? 350 : 1400);
            vgo.ynew = 260;
            p.goto_map(vgo);
            p.getService().send_box_ThongBao_OK("Bạn đã tiến vào trận chiến khổng lồ cùng Băng của mình!");
            return;
        }
        if (map.zones.TranChienKhongLo.isClanInQueue(p.clan)) {
            p.getService().send_box_ThongBao_OK("Băng của bạn đang trong hàng chờ ghép trận, vui lòng đợi trong giây lát...");
            return;
        }
        if (!map.zones.TranChienKhongLo.isOpen()) {
            p.getService().send_box_ThongBao_OK("Trận Chiến Khổng Lồ hiện chưa mở cửa!\n" + activities.TimedDungeonManager.gI().getDungeonStatusMessage("TRAN_CHIEN_KHONG_LO"));
            return;
        }
        p.tableTickOption = new functions.PhoBanKhongLoTick(p);
        p.tableTickOption.listP = new ArrayList<>();
        p.tableTickOption.listP.add(p);
        if (p.clan.members != null) {
            for (int i = 0; i < p.clan.members.size(); i++) {
                Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {
                    p.tableTickOption.listP.add(p0);
                }
            }
        }
        p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];
        p.tableTickOption.list_check[0] = 1;
        for (int i = 1; i < p.tableTickOption.list_check.length; i++) {
            p.tableTickOption.list_check[i] = 0;
        }
        p.tableTickOption.show("Phó bản khổng lồ");
    }

    private void handleDaoKhoBau(Player p) throws IOException {
        if (p.isDe || p instanceof model.DeTu) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể tham gia Đảo Kho Báu!");
            return;
        }
        if (p.party != null && p.party.list.size() > 1) {
            if (!isLeader(p)) {
                p.getService().send_box_ThongBao_OK("Bạn không phải trưởng nhóm");
                return;
            }
            List<String> missingKeys = new ArrayList<>();
            for (Player member : p.party.list) {
                if (member != null && !member.isBot && !member.isDe && !(member instanceof model.DeTu) && !(member instanceof bot.mercenary.MercenaryBot)) {
                    if (member.get_key_boss() < 1) {
                        missingKeys.add(member.name);
                    }
                }
            }
            if (!missingKeys.isEmpty()) {
                p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 1 chìa khóa phó bản");
                return;
            }
        } else {
            if (p.get_key_boss() < 1) {
                p.getService().send_box_ThongBao_OK("Bạn không đủ 1 chìa khóa phó bản");
                return;
            }
        }
        p.save_previous_map();
        if (event.EventManager.isActive(9)) {
            p.menus.clear();
            p.menus.add(new Menu("Vé săn thường", () -> {
                p.data_yesno = new int[]{84, 384};
                p.setyesNoDialog(new model.YesNoDialog(p, 84, "Thông báo",
                        "Sử dụng 1 Vé săn thường để vào Đảo Kho Báu?",
                        new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                p.getService().startYesNo();
            }));
            p.menus.add(new Menu("Vé săn đặc biệt", () -> {
                p.data_yesno = new int[]{84, 385};
                p.setyesNoDialog(new model.YesNoDialog(p, 84, "Thông báo",
                        "Sử dụng 1 Vé săn đặc biệt để vào Đảo Kho Báu?",
                        new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
                p.getService().startYesNo();
            }));
            p.getService().openDynamicMenu(9888, "Đảo Kho Báu - Sự Kiện", p.menus);
        } else {
            p.data_yesno = new int[]{84, 0};
            p.setyesNoDialog(new model.YesNoDialog(p, 84, "Thông báo",
                    "Bạn có muốn sử dụng 1 Chìa khóa phó bản để vào Đảo Kho Báu?",
                    new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
            p.getService().startYesNo();
        }
    }

    // -------------------------------------------------------------------------
    // Helper: gửi packet menu động Message(-20) — không cần MenuController
    // -------------------------------------------------------------------------

    /**
     * Gửi menu động xuống client.
     * icons == null -> type byte = 0 (không icon), != null -> type byte = 5 (có icon).
     */
    private void sendDynMenu(Player p, int npcId, String title, String[] options, short[] icons) throws IOException {
        p.menus.clear();
        for (int i = 0; i < options.length; i++) {
            final int idx = i;
            short icon = (icons != null && i < icons.length) ? icons[i] : -1;
            p.menus.add(new model.Menu(options[i], icon, () -> { try { handleMenu(p, npcId, idx); } catch (IOException e) {} }));
        }
        p.getService().openDynamicMenu(npcId, title, p.menus);
    }

    // -------------------------------------------------------------------------
    // Helper: khởi tạo phòng chờ phó bản nhóm (TableTickOption)
    // -------------------------------------------------------------------------

    private void startTableTickOption(Player p, int yesnoData, int dialogId, String title) throws IOException {
        if (p.party != null && p.party.list.size() > 1) {
            if (!isLeader(p)) {
                p.getService().send_box_ThongBao_OK("Chỉ có trưởng nhóm mới có thể bắt đầu phó bản");
                return;
            }
        }
        if (p.tableTickOption != null) {
            zabstracts.AbsTableTickOption.unregister(p.tableTickOption);
            p.tableTickOption = null;
        }
        p.data_yesno = new int[]{yesnoData};
        zabstracts.AbsTableTickOption option = null;
        if (yesnoData == 62) {
            option = new functions.LienTangTick(p);
        } else if (yesnoData == 68) {
            option = new functions.SieuLienTangTick(p);
        } else if (yesnoData == 63) {
            option = new functions.VuonCamTick(p);
        }
        
        if (option != null) {
            p.tableTickOption = option;
            p.tableTickOption.listP = new ArrayList<>();
            p.tableTickOption.listP.add(p);
            if (p.party != null && p.party.list.size() > 1) {
                for (Player member : p.party.list) {
                    Player p0 = Zone.get_player_by_name_allmap(member.name);
                    if (p0 != null && p0.index_map != p.index_map && p0.map != null && p.map != null && p0.map.equals(p.map)) {
                        p.tableTickOption.listP.add(p0);
                    }
                }
            }
            p.tableTickOption.list_check = new byte[p.tableTickOption.listP.size()];
            p.tableTickOption.list_check[0] = 1; // trưởng nhóm tự động tick
            p.tableTickOption.show(title);
        }
    }

    /** Kiểm tra p có phải trưởng nhóm không. */
    private boolean isLeader(Player p) {
        return p.party != null && !p.party.list.isEmpty() && p.party.list.get(0).equals(p);
    }
}
