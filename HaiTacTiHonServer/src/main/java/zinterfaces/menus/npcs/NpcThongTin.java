package zinterfaces.menus.npcs;

import functions.PvpBangTick;
import clan.Clan;
import map.Npc;
import map.Vgo;
import map.Zone;
import map.zones.PvpBang;
import map.zones.WorldWar;
import model.Menu;
import model.Player;
import zinterfaces.iNpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * NpcThongTin — Handler cho NPC Thông Tin / Bản Đồ (idmenu -98).
 *
 * <p>SQL data map 260:
 * [-98, "Thông Tin", "Bản đồ", "", 650, 170, 0, -1, 24, 24, 0, [63, 1], 0, 0, []]
 *
 * <p>Chức năng map 260 (Phòng chờ PvP Băng):
 * <ul>
 *   <li>Thông tin trận (Xem danh sách thành viên trong phòng chờ, lượt tham gia còn lại)</li>
 *   <li>Ghép trận nhanh (CHỈ Thuyền Trưởng - levelInclan == 0 mới thấy và bấm được)</li>
 *   <li>Rời khỏi đây (Teleport về làng / map 33)</li>
 * </ul>
 */
public class NpcThongTin implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-98};
    }

    @Override
    public void onInitNpcForMap(Zone zone) {
        if (zone == null || zone.template == null) return;

        // Map 260 (Phòng chờ PvP Băng)
        if (zone.template.id == 260) {
            if (!hasNpc(zone, (short) -98)) {
                Npc thongTin = new Npc();
                thongTin.idmenu    = (short) -98;
                thongTin.name      = "Thông Tin";
                thongTin.namegt    = "Bản đồ";
                thongTin.chat      = "";
                thongTin.x         = 650;
                thongTin.y         = 170;
                thongTin.isPerson  = 0;
                thongTin.typeIcon  = (byte) -1;
                thongTin.wBlock    = 24;
                thongTin.hBlock    = 24;
                thongTin.b3        = 0;
                thongTin.dataFrame = new byte[]{63, 1};
                thongTin.head      = 0;
                thongTin.hair      = 0;
                thongTin.wearing   = new short[]{};
                zone.template.npcs.add(thongTin);
            }
        }
        // Map 272..275 (Trận Chiến Lớn / World War)
        else if (zone.template.id >= 272 && zone.template.id <= 275) {
            if (!hasNpcAny(zone, new short[]{-98, -991, -106, -78})) {
                Npc warNpc = new Npc();
                warNpc.idmenu    = (short) -98;
                warNpc.name      = "Thông Tin";
                warNpc.namegt    = "Bản đồ";
                warNpc.chat      = "";
                warNpc.x         = (short) (zone.template.maxW / 2);
                warNpc.y         = (short) (zone.template.maxH / 2);
                warNpc.isPerson  = 0;
                warNpc.typeIcon  = 1;
                warNpc.wBlock    = 24;
                warNpc.hBlock    = 24;
                warNpc.b3        = 0;
                warNpc.dataFrame = new byte[]{63, 1};
                warNpc.head      = 0;
                warNpc.hair      = 1;
                warNpc.wearing   = new short[]{-1, -1, -1, -1};
                zone.template.npcs.add(warNpc);
            }
        }
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -98;
        p.menus.clear();

        // -- 1. Map 260: Phòng chờ PvP Băng ------------------------------------
        if (p.map != null && p.map.template != null && p.map.template.id == 260) {
            // Xem thông tin trận
            p.menus.add(new Menu("Thông tin trận", (short) 138, () -> {
                try {
                    showMatchInfoPvpBang(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            // Nút "Ghép trận ngay"
            p.menus.add(new Menu("Ghép trận ngay", (short) 171, () -> {
                try {
                    if (p.clan == null || p.clan.getTypeMem(p) > 1) {
                        p.getService().send_box_ThongBao_OK("Chỉ Thuyền Trưởng hoặc Thuyền Phó mới có thể yêu cầu ghép trận!");
                        return;
                    }
                    boolean success = PvpBang.triggerQuickMatch(p);
                    if (success) {
                        p.getService().send_box_ThongBao_OK("ĐÃ GHÉP TRẬN THÀNH CÔNG!\nTrận đấu sẽ bắt đầu sau 15 giây!");
                    } else {
                        p.getService().send_box_ThongBao_OK("Phòng chờ đang chuẩn bị hoặc trận đấu đang diễn ra!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            // Rời khỏi đây
            p.menus.add(new Menu("Rời khỏi đây", (short) 157, () -> {
                p.tableTickOption = null;
                try {
                    p.getService().send_time_cool_down(System.currentTimeMillis(), "", 0);
                } catch (Exception ignored) {}
                leaveMap(p);
            }));

            p.getService().openDynamicMenu(type, "Thông Tin Trận", p.menus);
            return;
        }

        // -- 1.1 Map 120: Đấu Trường PvP Băng -----------------------------------
        if (p.map != null && p.map.template != null && (p.map.template.id == 120 || p.map.pvpBangMapFight != null)) {
            p.menus.add(new Menu("Thông tin trận đấu", (short) 138, () -> {
                try {
                    showArenaInfoPvpBang(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            p.menus.add(new Menu("Rời khỏi đấu trường", (short) 157, () -> {
                p.tableTickOption = null;
                try {
                    p.getService().send_time_cool_down(System.currentTimeMillis(), "", 0);
                } catch (Exception ignored) {}
                leaveMap(p);
            }));

            p.getService().openDynamicMenu(type, "Đấu Trường PvP Băng", p.menus);
            return;
        }

        // -- 2. Map 272..275: Trận Chiến Lớn -----------------------------------
        boolean inWorldWar = (p.map != null && p.map.template != null
                && p.map.template.id >= 272 && p.map.template.id <= 275);
        if (inWorldWar) {
            p.menus.add(new Menu("Thông tin Trận Chiến Lớn", (short) 138, () -> {
                try {
                    String matchStatusStr = (WorldWar.status == WorldWar.STATUS_WAR)
                            ? "Đang Giao Tranh"
                            : ((WorldWar.status == WorldWar.STATUS_REGISTER) ? "Đang Trong Phòng Chờ" : "Chưa Mở");
                    String info = "THONG TIN TRAN CHIEN LON\n"
                            + "- Trang thai: " + matchStatusStr + "\n"
                            + "- Phe cua ban: " + (p.huongnghiep == 1 ? "Hai Quan" : (p.huongnghiep == 2 ? "Hai Tac" : (p.huongnghiep == 3 ? "Quan Cach Mang" : "Chua chon"))) + "\n\n"
                            + WorldWar.findTop();
                    p.getService().send_box_ThongBao_OK(info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            p.menus.add(new Menu("Chọn Phe", (short) 171, () -> {
                try {
                    zinterfaces.menus.SubMenuWorldWar.openSelectFactionMenu(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            p.menus.add(new Menu("Nhận thưởng TOP 3", (short) 138, () -> {
                try {
                    zinterfaces.menus.SubMenuWorldWar.handleNhanThuong(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            if (!p.isSpectator) {
                p.menus.add(new Menu("Xem trận đấu", (short) 117, () -> {
                    try {
                        if (p.map != null) {
                            p.enterSpectatorMode(p.map, (short) (p.map.template.maxW / 2), (short) (p.map.template.maxH / 2));
                            p.getService().send_box_ThongBao_OK("BAN DA VAO XEM TRAN DAU TRAN CHIEN LON!\n- Ban dang trong trang thai quan sat khong bi nhan sat thuong.\n- Chon menu NPC de Thoat xem.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
            }

            if (p.isSpectator) {
                p.menus.add(new Menu("Thoát xem", (short) 113, () -> {
                    try {
                        p.exitSpectatorMode();
                        p.getService().send_box_ThongBao_OK("Đã thoát Chế Độ Xem và trở về vị trí cũ!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
            }

            p.menus.add(new Menu("Rời sảnh chiến", (short) 113, () -> {
                try {
                    p.type_pk = -1;
                    p.return_to_previous_map();
                    p.getService().send_box_ThongBao_OK("Đã rời sảnh chiến và trở về vị trí trước đó!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
            p.getService().openDynamicMenu(type, "Bản đồ thông tin", p.menus);
            return;
        }

        // -- 3. Các map mặc định khác ------------------------------------------
        p.menus.add(new Menu("Thông tin", (short) 138, () -> {
            try {
                if (p.map != null && p.map.template != null && p.map.template.id == 994) {
                    p.getService().send_box_ThongBao_OK("Bảng thông tin Trận Chiến Bang Hội");
                } else {
                    p.getService().send_box_ThongBao_OK("Bảng thông tin phòng chờ PvP Bang");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        if (p.isSpectator) {
            p.menus.add(new Menu("Thoát chế độ xem", (short) 113, () -> {
                try {
                    p.exitSpectatorMode();
                    p.getService().send_box_ThongBao_OK("Đã thoát Chế Độ Xem và trở về vị trí cũ!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
        p.getService().openDynamicMenu(type, "Bản đồ thông tin", p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        if (p.menus != null && index >= 0 && index < p.menus.size()) {
            p.menus.get(index).execute(p, index);
        }
    }

    // --------------------------------------------------------------------------
    // Logic Ghép trận nhanh cho Map 260
    // --------------------------------------------------------------------------

    private void startPvpBangQueue(Player leader) throws IOException {
        // 1. Phải có băng
        if (leader.clan == null) {
            leader.getService().send_box_ThongBao_OK("Bạn chưa vào băng hội, không thể ghép trận!");
            return;
        }

        // 2. Phải là người có quyền cao nhất trong băng (Thuyền Trưởng)
        if (!isClanLeader(leader)) {
            leader.getService().send_box_ThongBao_OK("Chỉ Thuyền Trưởng mới có thể bắt đầu ghép trận PvP Băng!");
            return;
        }

        // 3. Phải đang ở map 260
        if (leader.map == null || leader.map.template == null || leader.map.template.id != 260) {
            leader.getService().send_box_ThongBao_OK("Bạn phải ở trong phòng chờ PvP Băng (map 260) để ghép trận!");
            return;
        }

        // 4. Băng chưa có dungeon đang xếp hàng
        if (leader.clan.map_create != null) {
            leader.getService().send_box_ThongBao_OK("Băng của bạn đã có phòng chờ đang hoạt động! Hãy vào phòng chờ đó.");
            return;
        }

        // 5. Kiểm tra TableTickOption của leader chưa mở
        if (leader.tableTickOption != null) {
            leader.getService().send_box_ThongBao_OK("Đang có bảng sẵn sàng mở, hãy hoàn tất hoặc huỷ trước.");
            return;
        }

        // 6. Kiểm tra lượt PvP Băng
        int ticket = PvpBang.getTicket(leader.clan);
        if (ticket >= 5) {
            leader.getService().send_box_ThongBao_OK("Băng hội đã hết lượt tham gia PvP Băng hôm nay! Quay lại vào ngày mai.");
            return;
        }

        // 7. Thu thập tất cả thành viên băng ĐANG ĐỨNG trong cùng zone 260 này
        Zone currentZone = leader.map;
        List<Player> clanMembersInMap = new ArrayList<>();

        // Thêm leader trước tiên (index 0 = trưởng nhóm trong TableTickOption)
        clanMembersInMap.add(leader);

        for (int i = 0; i < currentZone.players.size(); i++) {
            Player member = currentZone.players.get(i);
            if (member == null || member.name.equals(leader.name)) continue;
            if (member.isBot) continue; // Chỉ add người thật trước, bot lính đánh thuê được show() tự động add
            // Kiểm tra cùng băng
            if (member.clan != null && member.clan.equals(leader.clan)) {
                clanMembersInMap.add(member);
            }
        }

        // 8. Tạo PvpBangTick và show bảng sẵn sàng
        PvpBangTick tick = new PvpBangTick(leader);
        tick.listP = clanMembersInMap;
        tick.list_check = new byte[clanMembersInMap.size()];
        // Leader (index 0) tự động tick sẵn sàng
        tick.list_check[0] = 1;

        // Gán tableTickOption cho leader để block re-entry
        leader.tableTickOption = tick;

        // show() sẽ tự động:
        //   - Thu thêm bot mercenary của từng người thật vào listP
        //   - Tự tick sẵn sàng (list_check[i]=1) cho mọi bot (isBot == true)
        //   - Gửi packet -74 cho tất cả người thật
        //   - Gửi packet tick xanh cho từng bot để client hiển thị
        tick.show("PvP Băng — " + leader.clan.name + " (" + clanMembersInMap.size() + " thành viên sẵn sàng)");
    }

    private void showMatchInfoPvpBang(Player p) throws IOException {
        if (p.clan == null) {
            p.getService().send_box_ThongBao_OK("Bạn chưa vào băng hội.");
            return;
        }

        Clan clan = p.clan;
        int ticket = PvpBang.getTicket(clan);
        int luotConLai = Math.max(0, 5 - ticket);

        // Đếm thành viên băng đang ở map 260
        int soNguoiTrongMap = 0;
        if (p.map != null) {
            for (int i = 0; i < p.map.players.size(); i++) {
                Player pm = p.map.players.get(i);
                if (pm != null && !pm.isBot && pm.clan != null && pm.clan.equals(clan)) {
                    soNguoiTrongMap++;
                }
            }
        }

        // Xây dựng danh sách đầy đủ thành viên trong map
        StringBuilder danhSach = new StringBuilder();
        danhSach.append("DANH SÁCH THÀNH VIÊN BĂNG TRONG MAP\n");
        danhSach.append("----------------------------\n");
        if (p.map != null) {
            for (int i = 0; i < p.map.players.size(); i++) {
                Player pm = p.map.players.get(i);
                if (pm == null || pm.isBot) continue;
                if (pm.clan == null || !pm.clan.equals(clan)) continue;
                String chucVu = getChucVuText(clan, pm);
                danhSach.append("- ").append(pm.name)
                        .append(" [Lv.").append(pm.level).append("] ")
                        .append(chucVu).append("\n");
            }
        }

        String info = "THONG TIN PVP BANG\n"
                + "----------------\n"
                + "- Bang: " + clan.name + "\n"
                + "- Luot con lai hom nay: " + luotConLai + "/5\n"
                + "- Thanh vien trong phong cho: " + soNguoiTrongMap + " nguoi\n\n"
                + danhSach
                + "\n[Thuyen Truong an Ghep Tran Nhanh de bat dau]";

        p.getService().send_box_ThongBao_OK(info);
    }

    private void showArenaInfoPvpBang(Player p) throws IOException {
        if (p.map == null || p.map.pvpBangMapFight == null) {
            p.getService().send_box_ThongBao_OK("Không tìm thấy thông tin trận đấu.");
            return;
        }
        map.zones.PvpBangMapFight fight = p.map.pvpBangMapFight;
        String name1 = fight.clan1 != null ? fight.clan1.name : "Phe 1";
        String name2 = fight.clan2 != null ? fight.clan2.name : "Phe 2";
        int alive1 = 0, alive2 = 0;
        for (Player pj : p.map.players) {
            if (pj != null && !pj.isdie && !pj.isSpectator) {
                if (pj.clan != null && pj.clan.equals(fight.clan1)) alive1++;
                else if (pj.clan != null && pj.clan.equals(fight.clan2)) alive2++;
            }
        }
        String statusStr = fight.status_pvp == 3 ? "Đang Giao Tranh" : (fight.status_pvp < 3 ? "Chuẩn Bị" : "Đã Kết Thúc");
        String info = "THÔNG TIN ĐẤU TRƯỜNG PVP BĂNG\n"
                + "----------------------------\n"
                + "- Trạng thái: " + statusStr + "\n"
                + "- " + name1 + " (Cờ Đỏ): " + alive1 + " thành viên còn sống\n"
                + "- " + name2 + " (Cờ Xanh): " + alive2 + " thành viên còn sống\n"
                + "- Thời gian còn lại: " + fight.time_pvp + " giây\n\n"
                + "Đội nào hạ gục toàn bộ đối thủ trước hoặc có tổng HP cao hơn khi hết giờ sẽ giành Chiến Thắng!";
        p.getService().send_box_ThongBao_OK(info);
    }

    private void leaveMap(Player p) {
        try {
            p.dungeon = null;
            if (p.map != null) {
                p.map.change_flag(p, -1);
            }
            p.return_to_previous_map();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasNpc(Zone zone, short idmenu) {
        if (zone.template.npcs == null) return false;
        for (Npc n : zone.template.npcs) {
            if (n != null && n.idmenu == idmenu) return true;
        }
        return false;
    }

    private boolean hasNpcAny(Zone zone, short[] ids) {
        if (zone.template.npcs == null) return false;
        for (Npc n : zone.template.npcs) {
            if (n == null) continue;
            for (short id : ids) {
                if (n.idmenu == id) return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra player có phải Thuyền Trưởng (levelInclan == 0) của băng không.
     * Clan.getTypeMem() trả về 0 = Thuyền Trưởng, 1 = Thuyền Phó, 2 = Hoa Tiêu, 10 = Thành Viên.
     */
    private boolean isClanLeader(Player p) {
        if (p.clan == null) return false;
        return p.clan.getTypeMem(p) == 0;
    }

    private String getChucVuText(Clan clan, Player p) {
        int lv = clan.getTypeMem(p);
        switch (lv) {
            case 0:  return "Thuyền Trưởng";
            case 1:  return "Thuyền Phó";
            case 2:  return "Hoa Tiêu";
            default: return "Thành Viên";
        }
    }
}
