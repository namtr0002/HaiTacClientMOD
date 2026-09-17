package zinterfaces.menus.npcs;

import model.Player;
import model.Menu;
import model.YesNoDialog;
import model.InputDialog;
import clan.Clan;
import clan.ClanMember;
import clan.ClanHanhTrinhIcon;
import template.ClanIcon;
import template.QuestP;
import template.ItemTemplate4;
import template.ItemTemplate8;
import zinterfaces.iNpc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.Npc;
import map.Zone;
import map.Vgo;
import map.zones.PvpBang;
import map.zones.TranChienKhongLo;
import map.zones.ThuLinhBienKhoi;
import functions.PvpBangTick;
import functions.PhoBanKhongLoTick;
import network.Message;

public class NpcMihaw implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-84, 953, 952, 984, 979, 954};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        if (p.isDe || p instanceof model.DeTu) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể thực hiện chức năng Băng hải tặc!");
            return;
        }
        short type = npc != null ? npc.idmenu : -84;
        p.menus.clear();
        if (p.clan != null) {
            boolean isLeader = p.clan.members != null && !p.clan.members.isEmpty() && p.clan.members.get(0).name.equals(p.name);
            p.menus.add(new Menu("Nhiệm vụ băng", (short) 141, () -> { try { handleNhiemVuBang(p); } catch (IOException ex) { ex.printStackTrace(); } }));
            p.menus.add(new Menu("Huy hiệu hành trình", (short) 171, () -> { try { openHuyHieuHanhTrinhMenu(p); } catch (IOException ex) { ex.printStackTrace(); } }));
            p.menus.add(new Menu("Phó bản băng", (short) 146, () -> { try { openPhoBanBangMenu(p); } catch (IOException ex) { ex.printStackTrace(); } }));
            if (isLeader) {
                p.menus.add(new Menu("Cửa hàng biểu tượng", (short) 143, () -> { try { openCuaHangBieuTuongMenu(p); } catch (IOException ex) { ex.printStackTrace(); } }));
                p.menus.add(new Menu("Cửa hàng vật phẩm", (short) 144, () -> { try { handleCuaHangVatPham(p); } catch (IOException ex) { ex.printStackTrace(); } }));
                p.menus.add(new Menu(p.clan.allowRequest == 1 ? "Khóa xin vào băng" : "Mở xin vào băng", (short) 118, () -> { handleToggleAllowRequest(p); }));
                p.menus.add(new Menu("Đổi tên băng", (short) 110, () -> { handleDoiTenBang(p); }));
                p.menus.add(new Menu("Xóa băng", (short) 113, () -> { handleXoaBang(p); }));
                p.menus.add(new Menu("Nhường thuyền trưởng", (short) 132, () -> { handleNhuongThuyenTruong(p); }));
            }
        } else {
            p.menus.add(new Menu("Đăng ký băng hải tặc", (short) -1, () -> { handleDangKyBang(p); }));
            p.menus.add(new Menu("Hướng dẫn", (short) -1, () -> {
                try {
                    handleHuongDan(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }
        p.getService().openDynamicMenu(type, "Băng hải tặc", p.menus);
    }

    public static void handleNhiemVuBang(Player p) throws IOException {
        if (p.clan == null) return;
        ClanMember clan_mem = null;
        for (int i = 0; i < p.clan.members.size(); i++) {
            if (p.clan.members.get(i).name.equals(p.name)) {
                clan_mem = p.clan.members.get(i);
                break;
            }
        }
        if (clan_mem != null) {
            if (!clan_mem.isJoin24H()) {
                p.getService().send_box_ThongBao_OK("Tham gia băng trên 24h mới có thể làm nhiệm vụ băng");
                return;
            }
            if (clan_mem.numquest >= 3) {
                p.getService().send_box_ThongBao_OK("Hôm nay đã hết nhiệm vụ, hãy quay lại vào ngày mai");
                return;
            }
            QuestP questP = null;
            for (int i = 0; i < p.list_quest.size(); i++) {
                if (p.list_quest.get(i).template.id < -2000) {
                    questP = p.list_quest.get(i);
                    break;
                }
            }
            if (questP == null) {
                p.setyesNoDialog(new YesNoDialog(p, 42, "Thông báo",
                        ("Bạn muốn nhận nhiệm vụ Băng hải tặc cấp " + (clan_mem.numquest + 1)),
                        new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                p.getService().startYesNo();
            } else {
                p.getService().send_box_ThongBao_OK("Nhiệm vụ hiện tại chưa hoàn thành");
            }
        }
    }

    public static void openPhoBanBangMenu(Player p) throws IOException {
        if (p.clan == null) return;
        p.menus.clear();
        p.menus.add(new Menu("Phó bản PVP", (short) 146, () -> {
            try {
                handlePhoBanPVP(p);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        p.menus.add(new Menu("Phó bản khổng lồ", (short) 142, () -> {
            try {
                handlePhoBanKhongLo(p);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        p.menus.add(new Menu("Thủ lĩnh biển khơi", (short) 138, () -> {
            try {
                openThuLinhBienKhoiMenu(p);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        p.menus.add(new Menu("Chiếm Đảo", (short) 136, () -> {
            try {
                openChiemDaoClanMenu(p);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        p.menus.add(new Menu("Bảo Vệ Pháo Đài", (short) 135, () -> {
            try {
                handleBaoVePhaoDai(p);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        p.menus.add(new Menu("Hòm Thư Quà Phó Bản", (short) 110, () -> {
            try {
                activities.DungeonGiftMenu.openMenu(p);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        p.getService().openDynamicMenu(984, "Phó bản băng", p.menus);
    }

    public static void handlePhoBanPVP(Player p) throws IOException {
        if (p.clan == null) {
            p.getService().send_box_ThongBao_OK("Bạn chưa gia nhập Băng hải tặc!");
            return;
        }
        int ticket = PvpBang.getTicket(p.clan);
        if (ticket >= 5 && (p.clan.map_create == null || p.clan.map_create.pvpBang == null)) {
            p.getService().send_box_ThongBao_OK("Băng của bạn hôm nay đã hoàn thành tối đa 5/5 lượt tham gia PvP Băng!\nHãy quay lại vào ngày mai.");
            return;
        }
        if (p.clan.map_create != null && p.clan.map_create.pvpBang != null) {
            PvpBang pvp = p.clan.map_create.pvpBang;
            if (pvp.mapFight != null && pvp.state == PvpBang.FIGHTING) {
                boolean isClan1 = (pvp.mapFight.pvpBangMapFight != null && p.clan.equals(pvp.mapFight.pvpBangMapFight.clan1));
                p.type_pk = (byte) (isClan1 ? 4 : 5);
                short mapW = (pvp.mapFight.template != null && pvp.mapFight.template.maxW > 0)
                        ? pvp.mapFight.template.maxW : 1000;
                Vgo vgo = new Vgo();
                vgo.map_go = new Zone[]{pvp.mapFight};
                vgo.xnew = (short) (isClan1 ? 250 : Math.max(500, mapW - 250));
                vgo.ynew = 250;
                p.goto_map(vgo);
                p.getService().send_box_ThongBao_OK("Bạn đã tiến vào trận chiến PVP Băng đang diễn ra!");
                return;
            }
            Vgo vgo = new Vgo();
            vgo.map_go = new Zone[]{p.clan.map_create};
            vgo.xnew = 530;
            vgo.ynew = 260;
            p.goto_map(vgo);
            p.getService().send_time_cool_down(pvp.timeState, "Chờ ghép trận", 0);
            return;
        }

        // Tạo phòng chờ PvP Băng map 260 và đưa người chơi vào kèm Đệ tử
        PvpBang pvp = PvpBang.createDungeon(p.clan);
        List<Player> team = new ArrayList<>();
        team.add(p);
        if (p.detu != null && !p.detu.isdie && p.detu.detuStatus != model.DeTu.STATUS_HOME && p.detu.detuStatus != model.DeTu.STATUS_FUSION) {
            team.add(p.detu);
        }
        if (p.clan.members != null) {
            for (int i = 0; i < p.clan.members.size(); i++) {
                Player p0 = Zone.get_player_by_name_allmap(p.clan.members.get(i).name);
                if (p0 != null && p0.index_map != p.index_map && p0.map != null && p0.map.equals(p.map)) {
                    team.add(p0);
                    if (p0.detu != null && !p0.detu.isdie && p0.detu.detuStatus != model.DeTu.STATUS_HOME && p0.detu.detuStatus != model.DeTu.STATUS_FUSION) {
                        team.add(p0.detu);
                    }
                }
            }
        }
        pvp.join(team);
        p.tableTickOption = null;
    }

    public static void openChiemDaoClanMenu(Player p) throws IOException {
        if (p.clan == null) {
            p.getService().send_box_ThongBao_OK("Bạn chưa gia nhập Băng hải tặc!");
            return;
        }
        p.menus.clear();
        int[] islands = {25, 33, 49, 69, 83};
        for (int i = 0; i < islands.length; i++) {
            final int islandId = islands[i];
            final String islandName = activities.ChiemDaoMenu.getIslandName(islandId);
            Clan occupying = map.zones.ChiemDao.getClanTop(islandId);
            String clanTag = (occupying != null) ? " [" + occupying.name + "]" : "";
            String menuText = islandName + clanTag;
            p.menus.add(new Menu(menuText, (short) -1, () -> {
                try {
                    activities.ChiemDaoMenu.openMenu(p, islandId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }
        p.getService().openDynamicMenu(984, "Chiếm Đảo Bang Hội", p.menus);
    }

    public static void handleBaoVePhaoDai(Player p) throws IOException {
        if (p.clan == null) {
            p.getService().send_box_ThongBao_OK("Bạn chưa gia nhập Băng hải tặc!");
            return;
        }
        boolean isOpenBv = activities.TimedDungeonManager.gI().isDungeonOpen("BAO_VE_PHAO_DAI") || (p.clan.baoVePhaoDai != null && !p.clan.baoVePhaoDai.isClose);
        if (!isOpenBv && p.admin == 0) {
            p.getService().send_box_ThongBao_OK("Bảo Vệ Pháo Đài hiện chưa mở cửa!\n"
                    + activities.TimedDungeonManager.gI().getDungeonStatusMessage("BAO_VE_PHAO_DAI"));
            return;
        }
        if (p.clan.baoVePhaoDai != null && !p.clan.baoVePhaoDai.isClose) {
            boolean isTeamA = p.clan.equals(p.clan.baoVePhaoDai.clanA);
            Zone targetZone = isTeamA ? p.clan.baoVePhaoDai.mapClanA : p.clan.baoVePhaoDai.mapClanB;
            if (targetZone != null) {
                p.type_pk = (byte) (isTeamA ? 4 : 5);
                Vgo vgo = new Vgo();
                vgo.map_go = new Zone[]{targetZone};
                vgo.xnew = (short) (isTeamA ? 200 : 1400);
                vgo.ynew = 260;
                p.goto_map(vgo);
                p.getService().send_box_ThongBao_OK("Bạn đã tiến vào trận chiến Bảo Vệ Pháo Đài đang diễn ra!");
                return;
            }
        }
        if (p.clan.getTypeMem(p) == 0 || p.clan.getTypeMem(p) == 1) {
            p.tableTickOption = new functions.BaoVePhaoDaiTick(p);
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
            p.tableTickOption.show("Bảo Vệ Pháo Đài");
        } else {
            p.getService().send_box_ThongBao_OK("Chỉ có Thuyền trưởng hoặc Thuyền phó mới có thể mở đăng ký Bảo Vệ Pháo Đài!");
        }
    }

    public static void handlePhoBanKhongLo(Player p) throws IOException {
        if (p.clan == null) {
            p.getService().send_box_ThongBao_OK("Bạn chưa gia nhập Băng hải tặc!");
            return;
        }
        if (!TranChienKhongLo.isOpen() && p.admin == 0) {
            p.getService().send_box_ThongBao_OK("Phó bản Trận Chiến Khổng Lồ hiện chưa mở cửa!\n"
                    + activities.TimedDungeonManager.gI().getDungeonStatusMessage("TRAN_CHIEN_KHONG_LO"));
            return;
        }
        if (p.clan.map_create != null && p.clan.map_create.map_little_garden != null && !p.clan.map_create.map_little_garden.is_finish) {
            byte typePk = (byte) ((p.clan.map_create.map_little_garden.clan1 != null
                    && p.clan.map_create.map_little_garden.clan1.equals(p.clan)) ? 4 : 5);
            p.type_pk = typePk;
            Vgo vgo = new Vgo();
            vgo.map_go = new Zone[]{p.clan.map_create};
            vgo.xnew = (short) (typePk == 4 ? 350 : 1400);
            vgo.ynew = 260;
            p.goto_map(vgo);
            return;
        }
        if (TranChienKhongLo.isClanInQueue(p.clan)) {
            p.getService().send_box_ThongBao_OK("Băng của bạn đang trong hàng chờ ghép trận, vui lòng đợi trong giây lát...");
            return;
        }
        if (p.clan.getTypeMem(p) == 0 || p.clan.getTypeMem(p) == 1) {
            p.tableTickOption = new PhoBanKhongLoTick(p);
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
        } else {
            p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng hoặc thuyền phó");
        }
    }

    public static void openThuLinhBienKhoiMenu(Player p) throws IOException {
        if (p.clan == null) {
            p.getService().send_box_ThongBao_OK("Bạn chưa gia nhập Băng hải tặc!");
            return;
        }
        if (p.clan.map_create != null && p.clan.map_create.map_thuLinhBienKhoi != null) {
            if (p.get_key_boss() < 2) {
                p.getService().send_box_ThongBao_OK("Bạn cần 2 Chìa khoá phó bản để vào phó bản cùng Băng!");
                return;
            }
            p.update_key_boss(-2);
            p.updateMoney();
            int seaFlag = p.clan.map_create.map_thuLinhBienKhoi.flag;
            p.type_pk = (byte) seaFlag;
            Vgo vgo = new Vgo();
            vgo.map_go = new Zone[]{p.clan.map_create};
            vgo.xnew = 100;
            vgo.ynew = 230;
            p.goto_map(vgo);
            if (p.getService() != null) {
                p.getService().update_PK(p, false);
            }
            return;
        }
        if (!ThuLinhBienKhoi.isOpen()) {
            p.getService().send_box_ThongBao_OK("Hoạt động Thủ Lĩnh Biển Khơi chưa đến giờ mở cửa (19:00 Thứ 2, Thứ 4, Thứ 6)!");
            return;
        }
        if (p.clan.getTypeMem(p) == 0 || p.clan.getTypeMem(p) == 1) {
            p.menus.clear();
            p.menus.add(new Menu("Biển Đông", (short) 138, () -> { handleSelectSea(p, 0); }));
            p.menus.add(new Menu("Biển Tây", (short) 138, () -> { handleSelectSea(p, 1); }));
            p.menus.add(new Menu("Biển Nam", (short) 138, () -> { handleSelectSea(p, 2); }));
            p.menus.add(new Menu("Biển Bắc", (short) 138, () -> { handleSelectSea(p, 3); }));
            p.getService().openDynamicMenu(954, "Thủ lĩnh biển khơi", p.menus);
        } else {
            p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng hoặc thuyền phó");
        }
    }

    public static void handleSelectSea(Player p, int seaIndex) {
        if (p.clan == null || (p.clan.getTypeMem(p) != 0 && p.clan.getTypeMem(p) != 1)) {
            p.getService().send_box_ThongBao_OK("Bạn không phải thuyền trưởng hoặc thuyền phó");
            return;
        }
        String[] seaNames = new String[]{"Đông", "Tây", "Nam", "Bắc"};
        if (seaIndex < 0 || seaIndex >= seaNames.length) return;
        int flag = seaIndex == 0 ? 4 : (seaIndex == 1 ? 5 : (seaIndex == 2 ? 6 : 7));
        p.data_yesno = new int[]{82, flag};
        p.setyesNoDialog(new YesNoDialog(p, 82, "Thông báo",
                "Xác nhận chọn phe Biển " + seaNames[seaIndex] + " để tham gia phó bản thủ lĩnh biển khơi?",
                new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
        p.getService().startYesNo();
    }

    public static void openHuyHieuHanhTrinhMenu(Player p) throws IOException {
        if (p.clan == null) return;
        p.menus.clear();
        p.menus.add(new Menu("Ghép xu hành trình", (short) 171, () -> {
            try {
                handleGhepXuHanhTrinh(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new Menu("Cống hiến xu hành trình", (short) 141, () -> { handleCongHienXuHanhTrinh(p); }));
        p.menus.add(new Menu("Huy hiệu hành trình", (short) 138, () -> { try { openDanhSachHuyHieuMenu(p); } catch (IOException e) { e.printStackTrace(); } }));
        p.getService().openDynamicMenu(953, "Huy hiệu hành trình", p.menus);
    }

    public static void handleGhepXuHanhTrinh(Player p) throws IOException {
        p.getService().sendUpgradeDevilCraftPanel(
            "Ghép vật phẩm",
            (byte) 1,
            new short[]{579},
            new short[]{100},
            new byte[]{4},
            new short[]{551},
            50_000,
            (short) 10,
            10_000,
            (short) 580,
            (short) 1,
            (byte) 4,
            (short) 552,
            (byte) 10
        );
    }

    public static void handleCongHienXuHanhTrinh(Player p) {
        new InputDialog(p, 27, "Cống hiến xu hành trình", new String[]{"Nhập xu hành trình"}).startInput();
    }

    public static void openDanhSachHuyHieuMenu(Player p) throws IOException {
        if (p.clan == null) return;
        p.menus.clear();
        p.menus.add(new Menu("Danh sách huy hiệu hành trình", (short) 138, () -> { try { showAllHuyHieuList(p); } catch (IOException e) { e.printStackTrace(); } }));
        p.menus.add(new Menu("Danh sách sở hữu", (short) 171, () -> { try { showOwnedHuyHieuList(p); } catch (IOException e) { e.printStackTrace(); } }));
        p.menus.add(new Menu("Mở xu hành trình", (short) 144, () -> { try { showOpenXuHanhTrinh(p); } catch (IOException e) { e.printStackTrace(); } }));
        p.getService().openDynamicMenu(952, "Huy hiệu hành trình", p.menus);
    }

    public static void showAllHuyHieuList(Player p) throws IOException {
        Message m = new Message(-95);
        m.writer().writeByte(0);
        m.writer().writeUTF("Danh sách huy hiệu hành trình");
        m.writer().writeShort(ClanHanhTrinhIcon.ENTRY.size());
        for (int i = 0; i < ClanHanhTrinhIcon.ENTRY.size(); i++) {
            ClanHanhTrinhIcon icon = ClanHanhTrinhIcon.ENTRY.get(i);
            m.writer().writeShort(icon.id);
            m.writer().writeUTF(icon.name != null ? icon.name : "");
            m.writer().writeUTF(icon.info != null ? icon.info : "");
            m.writer().writeShort(icon.icon);
            m.writer().writeUTF("");
            if (icon.op != null && !icon.op.isEmpty()) {
                m.writer().writeByte(icon.op.size());
                for (template.Option op : icon.op) {
                    m.writer().writeByte(op.id);
                    m.writer().writeShort(op.getParam());
                }
            } else {
                m.writer().writeByte(0);
            }
        }
        p.addmsg(m);
        m.cleanup();
    }

    public static void showOwnedHuyHieuList(Player p) throws IOException {
        if (p.clan == null) return;
        Message m = new Message(-95);
        m.writer().writeByte(0);
        m.writer().writeUTF("Danh sách sở hữu");
        List<ClanHanhTrinhIcon> owned = new ArrayList<>();
        if (p.clan.hanhtrinh != null && p.clan.hanhtrinh.size() > 1) {
            for (int i = 1; i < p.clan.hanhtrinh.size(); i++) {
                ClanHanhTrinhIcon temp = ClanHanhTrinhIcon.getById(p.clan.hanhtrinh.get(i));
                if (temp != null) {
                    owned.add(temp);
                }
            }
        }
        m.writer().writeShort(owned.size());
        for (ClanHanhTrinhIcon temp : owned) {
            m.writer().writeShort(temp.id);
            m.writer().writeUTF(temp.name != null ? temp.name : "");
            m.writer().writeUTF(temp.info != null ? temp.info : "");
            m.writer().writeShort(temp.icon);
            m.writer().writeUTF("");
            if (temp.op != null && !temp.op.isEmpty()) {
                m.writer().writeByte(temp.op.size());
                for (template.Option op : temp.op) {
                    m.writer().writeByte(op.id);
                    m.writer().writeShort(op.getParam());
                }
            } else {
                m.writer().writeByte(0);
            }
        }
        p.addmsg(m);
        m.cleanup();
    }

    public static void showOpenXuHanhTrinh(Player p) throws IOException {
        if (p.clan == null) return;
        if (p.clan.hanhtrinh == null || p.clan.hanhtrinh.isEmpty()) {
            p.clan.hanhtrinh = new ArrayList<>();
            p.clan.hanhtrinh.add(0);
        }
        Message m = new Message(-95);
        m.writer().writeByte(3);
        m.writer().writeByte(0);
        m.writer().writeShort(580);
        m.writer().writeShort(p.clan.hanhtrinh.get(0));
        m.writer().writeByte(4);
        m.writer().writeShort(ItemTemplate4.get_it_by_id(580).icon);
        p.addmsg(m);
        m.cleanup();
    }

    public static void openCuaHangBieuTuongMenu(Player p) throws IOException {
        if (p.clan == null) return;
        p.menus.clear();
        p.menus.add(new Menu("Cửa hàng thường", (short) 143, () -> {
            try {
                sendShopBieuTuongThuong(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.menus.add(new Menu("Cửa hàng cao cấp", (short) 144, () -> {
            try {
                sendShopBieuTuongVip(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        p.getService().openDynamicMenu(979, "Icon băng", p.menus);
    }

    public static void sendShopBieuTuongThuong(Player p) throws IOException {
        Message m = new Message(-19);
        m.writer().writeByte(97);
        m.writer().writeUTF("Cửa hàng biểu tượng thường");
        m.writer().writeByte(107);
        List<ClanIcon> list = ClanIcon.getIconThuong();
        m.writer().writeShort(list.size());
        for (int i = 0; i < list.size(); i++) {
            ClanIcon icon = list.get(i);
            m.writer().writeShort(icon.id);
            m.writer().writeShort(icon.id);
            m.writer().writeUTF(icon.name != null ? icon.name : "");
            m.writer().writeUTF(icon.info != null ? icon.info : "");
            m.writer().writeShort(icon.price);
        }
        p.addmsg(m);
        m.cleanup();
    }

    public static void sendShopBieuTuongVip(Player p) throws IOException {
        Message m = new Message(-19);
        m.writer().writeByte(97);
        m.writer().writeUTF("Cửa hàng biểu tượng cao cấp");
        m.writer().writeByte(107);
        List<ClanIcon> list = ClanIcon.getIconVip();
        m.writer().writeShort(list.size());
        for (int i = 0; i < list.size(); i++) {
            ClanIcon icon = list.get(i);
            m.writer().writeShort(icon.id);
            m.writer().writeShort(icon.id);
            m.writer().writeUTF(icon.name != null ? icon.name : "");
            m.writer().writeUTF(icon.info != null ? icon.info : "");
            m.writer().writeShort(icon.price);
        }
        p.addmsg(m);
        m.cleanup();
    }

    public static void handleCuaHangVatPham(Player p) throws IOException {
        if (p == null) return;
        zabstracts.AbsShop shop = zabstracts.AbsShop.get(110);
        if (shop != null) {
            shop.openUI(p);
        } else {
            new store.ClanShop().openUI(p);
        }
    }

    public static void handleToggleAllowRequest(Player p) {
        if (p.clan == null) return;
        if (p.clan.allowRequest == 1) {
            p.clan.allowRequest = 0;
            p.getService().send_box_ThongBao_OK("Khóa mọi người xin vào băng thành công");
        } else {
            p.clan.allowRequest = 1;
            p.getService().send_box_ThongBao_OK("Cho phép mọi người xin vào băng thành công");
        }
    }

    public static void handleDoiTenBang(Player p) {
        if (p.clan == null) return;
        if (p.clan.members != null && !p.clan.members.isEmpty() && p.clan.members.get(0).name.equals(p.name)) {
            new InputDialog(p, -21, "Đổi tên băng", new String[]{"Nhập tên băng muốn đổi"}).startInput();
        } else {
            p.getService().send_box_ThongBao_OK("Thuyền trưởng mới có thể đổi tên");
        }
    }

    public static void handleXoaBang(Player p) {
        if (p.clan == null) return;
        if (p.clan.members != null && !p.clan.members.isEmpty() && p.clan.members.get(0).name.equals(p.name)) {
            new InputDialog(p, -13, "Xóa Băng", new String[]{"Nhập xoabang để xác nhận xóa băng"}).startInput();
        } else {
            p.getService().send_box_ThongBao_OK("Chỉ thuyền trưởng mới có thể xóa");
        }
    }

    public static void handleNhuongThuyenTruong(Player p) {
        if (p.clan == null) return;
        ClanMember mem = null;
        if (p.clan.members != null) {
            for (int i = 0; i < p.clan.members.size(); i++) {
                if (p.clan.members.get(i).levelInclan == 1) {
                    mem = p.clan.members.get(i);
                    break;
                }
            }
        }
        if (mem != null) {
            p.data_yesno = new int[]{65};
            p.setyesNoDialog(new YesNoDialog(p, 65, "Thông báo",
                    "Xác nhận nhường thuyền trưởng cho " + mem.name + "?",
                    new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
            p.getService().startYesNo();
        } else {
            p.getService().send_box_ThongBao_OK("Không tìm thấy thuyền phó");
        }
    }

    public static void handleDangKyBang(Player p) {
        new InputDialog(p, 10, "Đăng ký băng", new String[]{"Tên băng"}).startInput();
    }

    public static void handleHuongDan(Player p) throws IOException {
        String txt = "HƯỚNG DẪN BĂNG HẢI TẶC\n\n"
                + "• Giờ đây bạn có thể tập hợp các chiến hữu cùng chung chí hướng để thành lập Băng Hải Tặc hùng mạnh!\n"
                + "• Điều kiện lập băng: Thuyền trưởng cần đạt cấp độ quy định và có đủ Beri/Ruby để đăng ký.\b"
                + "QUYỀN LỢI & CỬA HÀNG BĂNG:\n"
                + "- Thành viên tham gia cống hiến Beri/Ruby để gia tăng quỹ và điểm kinh nghiệm của Băng.\n"
                + "- Tích lũy điểm cống hiến để mua sắm các vật phẩm đặc biệt tại Cửa Hàng Băng.\n"
                + "- Thăng cấp Băng giúp mở rộng số lượng thành viên tối đa và mở khóa thêm nhiều đặc quyền mới.\b"
                + "HOẠT ĐỘNG BĂNG HẢI TẶC:\n"
                + "- Phó Bản Bang: Tham gia khiêu chiến Phó Bản PvP, Phó Bản Khổng Lồ và săn Boss Thủ Lĩnh Biển Khơi cùng đồng đội.\n"
                + "- Chiếm Đảo Bang Hội: Đại chiến tranh đoạt lãnh thổ và tài nguyên giữa các Băng trên toàn server!";
        p.getService().Help_From_Server(-84, txt);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        if (p.isDe || p instanceof model.DeTu) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể thực hiện chức năng Băng hải tặc!");
            return;
        }

        switch (menuId) {
            case 984: { // Phó bản băng
                if (index == 0) handlePhoBanPVP(p);
                else if (index == 1) handlePhoBanKhongLo(p);
                else if (index == 2) openThuLinhBienKhoiMenu(p);
                return;
            }
            case 954: { // Thủ lĩnh biển khơi
                handleSelectSea(p, index);
                return;
            }
            case 953: { // Huy hiệu hành trình
                if (index == 0) handleGhepXuHanhTrinh(p);
                else if (index == 1) handleCongHienXuHanhTrinh(p);
                else if (index == 2) openDanhSachHuyHieuMenu(p);
                return;
            }
            case 952: { // Danh sách huy hiệu
                if (index == 0) showAllHuyHieuList(p);
                else if (index == 1) showOwnedHuyHieuList(p);
                else if (index == 2) showOpenXuHanhTrinh(p);
                return;
            }
            case 979: { // Icon băng
                if (index == 0) sendShopBieuTuongThuong(p);
                else if (index == 1) sendShopBieuTuongVip(p);
                return;
            }
            default: { // -84 hoặc main menu
                handleMenu(p, index);
                return;
            }
        }
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        if (p.isDe || p instanceof model.DeTu) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể thực hiện chức năng Băng hải tặc!");
            return;
        }
        short currentId = (short) p.currentNpcId;
        if (currentId == 984 || currentId == 954 || currentId == 953 || currentId == 952 || currentId == 979) {
            handleMenu(p, currentId, index);
            return;
        }

        if (p.clan != null) {
            boolean isLeader = p.clan.members != null && !p.clan.members.isEmpty() && p.clan.members.get(0).name.equals(p.name);
            if (index > 2 && !isLeader) {
                return;
            }
            switch (index) {
                case 0: handleNhiemVuBang(p); break;
                case 1: openHuyHieuHanhTrinhMenu(p); break;
                case 2: openPhoBanBangMenu(p); break;
                case 3: openCuaHangBieuTuongMenu(p); break;
                case 4: handleCuaHangVatPham(p); break;
                case 5: handleToggleAllowRequest(p); break;
                case 6: handleDoiTenBang(p); break;
                case 7: handleXoaBang(p); break;
                case 8: handleNhuongThuyenTruong(p); break;
            }
        } else {
            if (index == 0) handleDangKyBang(p);
            else if (index == 1) handleHuongDan(p);
        }
    }
}

