package zinterfaces.menus.npcs;

import model.Player;
import model.DeTu;
import model.Clazz;
import model.Menu;
import core.MenuController;
import zinterfaces.iNpc;
import zinterfaces.iMenuDymanic;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.Npc;
import model.InputDialog;
import model.YesNoDialog;

/**
 * NpcPaule — Quản lý Menu các NPC Kỹ năng / Guru / Shop Trang Bị:
 *   -3 (Guru - Làng Cối Xay Gió)
 *   -15 (Mẹ Rita - Thị Trấn Vỏ Sò)
 *   -23 (Poroy - Thị Trấn Orange)
 *   -30 (Merri - Làng Syrup)
 *   -38 (Partty - Nhà Hàng Baratie)
 *   -69 (Masu - Thị Trấn Khởi Đầu Loguetown)
 *   -75 (Ms Vivi - Thị Trấn Whiskey)
 *   -88 (Stook - Mỏm Sinh Đôi / Little Garden)
 *   -101 (Kohzak - Đảo Drum)
 *   -117 (Spect - Đảo Jaya)
 *   -121 (Pagada - Đảo Sky Island)
 *   -146 (Paule - Water 7)
 * Sub-menus:
 *   32003 = Hệ khác
 *   943   = Đệ tử
 */
public class NpcPaule implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{
            -146, -121, -117, -101, -88, -75, -69, -38, -30, -23, -15, -3,
            32003, 943
        };
    }

    private String getNpcName(short type) {
        switch (type) {
            case -3: return "Guru";
            case -15: return "Mẹ Rita";
            case -23: return "Poroy";
            case -30: return "Merri";
            case -38: return "Partty";
            case -69: return "Masu";
            case -75: return "Ms Vivi";
            case -88: return "Stook";
            case -101: return "Kohzak";
            case -117: return "Spect";
            case -121: return "Pagada";
            case -146: return "Paule";
            default: return "Guru";
        }
    }

    private void buildMenuOptions(Player p) {
        p.menus.clear();

        if (p instanceof DeTu) {
            DeTu dt = (DeTu) p;
            p.menus.add(new Menu("Trở về Sư phụ", (short) 125, () -> {
                try {
                    dt.switchCharacter(dt.master);
                } catch (IOException ex) {
                    Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }));
            p.menus.add(new Menu(!p.is_show_hat ? "Bật hiển thị nón" : "Tắt hiển thị nón", (short) 117, () -> {
                try {
                    p.is_show_hat = !p.is_show_hat;
                    p.update_wearing_to_all();
                    p.getService().send_box_ThongBao_OK(p.is_show_hat ? "Đã bật hiển thị nón" : "Đã tắt hiển thị nón");
                } catch (IOException ex) {
                    Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }));
            p.menus.add(new Menu("Cường hóa Kỹ năng ác quỷ", (short) 154, () -> {
                try {
                    activities.UpgradeDevil.show_table(p, 1);
                } catch (IOException ex) {
                    Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }));
            p.menus.add(new Menu("Thùng rác", (short) 113, () -> {
                try {
                    p.getService().Send_UI_Shop(119);
                } catch (IOException ex) {
                    Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }));
            p.menus.add(new Menu(p.is_hide_fashion_hair ? "Hiện Tóc Thời Trang" : "Che Tóc Thời Trang", (short) 103, () -> {
                try {
                    p.is_hide_fashion_hair = !p.is_hide_fashion_hair;
                    p.update_wearing_to_all();
                    p.getService().send_box_ThongBao_OK(p.is_hide_fashion_hair ? "Đã che tóc thời trang" : "Đã hiện tóc thời trang");
                } catch (IOException ex) {
                    Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }));
            p.menus.add(new Menu(p.is_hide_fashion_head ? "Hiện Mặt Thời Trang" : "Che Mặt Thời Trang", (short) 108, () -> {
                try {
                    p.is_hide_fashion_head = !p.is_hide_fashion_head;
                    p.update_wearing_to_all();
                    p.getService().send_box_ThongBao_OK(p.is_hide_fashion_head ? "Đã che mặt thời trang" : "Đã hiện mặt thời trang");
                } catch (IOException ex) {
                    Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }));
            return;
        }

        // 0. Phái của bản thân (Mở shop trang bị hệ hiện tại)
        p.menus.add(new Menu(Clazz.NAME[p.clazz - 1], Clazz.ICON[p.clazz - 1], () -> {
            try {
                p.getService().Send_UI_Shop((p.clazz - 1));
            } catch (IOException ex) {
                Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // 1. Hệ khác
        p.menus.add(new Menu("Hệ khác", (short) 116, () -> {
            String[] other_clazz_name = new String[4];
            short[] other_clazz_icon = new short[4];
            int pos = 0;
            for (int i = 0; i < 5; i++) {
                if ((i + 1) == p.clazz) {
                    continue;
                }
                other_clazz_name[pos] = Clazz.NAME[i];
                other_clazz_icon[pos++] = Clazz.ICON[i];
            }
            p.getService().openDynamicMenu(32003, "Hệ khác", other_clazz_name, other_clazz_icon);
        }));

        // 2. Hiển thị nón
        p.menus.add(new Menu(!p.is_show_hat ? "Bật hiển thị nón" : "Tắt hiển thị nón", (short) 117, () -> {
            try {
                p.is_show_hat = !p.is_show_hat;
                p.update_wearing_to_all();
                p.getService().send_box_ThongBao_OK(p.is_show_hat ? "Đã bật hiển thị nón" : "Đã tắt hiển thị nón");
            } catch (IOException ex) {
                Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // 3. Khóa bảo vệ (Menu 994)
        p.menus.add(new Menu("Khóa bảo vệ", (short) 118, () -> {
            try {
                if (p.timeHuyMbv == -1) {
                    iMenuDymanic.buildAndSend(p, 994, "Khóa Bảo Vệ",
                            new String[]{"Đăng ký khóa bảo vệ", "Hướng dẫn", "Đăng ký hủy mã khóa"},
                            new short[]{118, 148, 118});
                } else {
                    iMenuDymanic.buildAndSend(p, 994, "Khóa Bảo Vệ",
                            new String[]{"Đăng ký khóa bảo vệ", "Hướng dẫn", "Đăng ký hủy mã khóa", "Hủy đăng ký xóa mã rương"},
                            new short[]{118, 148, 118, -1});
                }
            } catch (IOException ex) {
                Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // 4. Thùng rác
        p.menus.add(new Menu("Thùng rác", (short) 113, () -> {
            try {
                p.getService().Send_UI_Shop(119);
            } catch (IOException ex) {
                Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // 5. Che / Hiện Tóc Thời Trang
        p.menus.add(new Menu(p.is_hide_fashion_hair ? "Hiện Tóc Thời Trang" : "Che Tóc Thời Trang", (short) 103, () -> {
            try {
                p.is_hide_fashion_hair = !p.is_hide_fashion_hair;
                p.update_wearing_to_all();
                p.getService().send_box_ThongBao_OK(p.is_hide_fashion_hair ? "Đã che tóc thời trang" : "Đã hiện tóc thời trang");
            } catch (IOException ex) {
                Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // 6. Che / Hiện Mặt Thời Trang
        p.menus.add(new Menu(p.is_hide_fashion_head ? "Hiện Mặt Thời Trang" : "Che Mặt Thời Trang", (short) 108, () -> {
            try {
                p.is_hide_fashion_head = !p.is_hide_fashion_head;
                p.update_wearing_to_all();
                p.getService().send_box_ThongBao_OK(p.is_hide_fashion_head ? "Đã che mặt thời trang" : "Đã hiện mặt thời trang");
            } catch (IOException ex) {
                Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // 7. Đệ tử
        if (p.detu != null) {
            p.menus.add(new Menu("Đệ tử", (short) 125, () -> {
                try {
                    String statusNow = DeTu.STATUS_NAMES[Math.min(p.detu.detuStatus, DeTu.STATUS_NAMES.length - 1)];
                    iMenuDymanic.buildAndSend(p, 943, "Đệ tử",
                            new String[]{"Thông tin", "Nâng tiềm năng", "Kỹ năng", "Tim", "Sử dụng Pet", "Nâng thông thạo",
                                    "Chuyển sang đệ tử", "Đổi trạng thái [Đang: " + statusNow + "]", "Xóa đệ tử"},
                            null);
                } catch (IOException ex) {
                    Logger.getLogger(NpcPaule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }));
        }
    }

    private void handleHeKhac(Player p, int index) throws IOException {
        int[] shop_pos = new int[4];
        int pos = 0;
        for (int i = 0; i < 5; i++) {
            if ((i + 1) == p.clazz) {
                continue;
            }
            shop_pos[pos++] = i;
        }
        if (index >= 0 && index < shop_pos.length) {
            p.getService().Send_UI_Shop(shop_pos[index]);
        }
    }

    private void handleDeTu(Player p, int index) throws IOException {
        if (p.detu == null) {
            p.getService().send_box_ThongBao_OK("Bạn chưa có đệ tử!");
            return;
        }
        DeTu dt = p.detu;
        switch (index) {
            case 0: // Thông tin
                DeTu.showDeTuInfo(p);
                break;
            case 1: // Nâng tiềm năng
                iMenuDymanic.buildAndSend(p, 942, "Đệ tử",
                        new String[]{"Thông tin", "Sức mạnh", "Phòng thủ", "Thể lực", "Tinh Thần", "Nhanh nhẹn"},
                        null);
                break;
            case 2: // Kỹ năng
                DeTu.showDeTuSkillMenu(p);
                break;
            case 3: // Tim
                iMenuDymanic.buildAndSend(p, 938, "Tim",
                        new String[]{"Tách", "Nâng cấp"},
                        null);
                break;
            case 4: { // Sử dụng Pet
                java.util.List<model.MyPet> pet_select = new java.util.ArrayList<>();
                for (int i = 0; i < p.my_pet.size(); i++) {
                    if (!p.my_pet.get(i).isUse) {
                        pet_select.add(p.my_pet.get(i));
                    }
                }
                if (!pet_select.isEmpty()) {
                    String[] menu = new String[pet_select.size()];
                    for (int i = 0; i < menu.length; i++) {
                        menu[i] = pet_select.get(i).template.name;
                    }
                    iMenuDymanic.buildAndSend(p, 936, "Sử dụng Pet", menu, null);
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn không có thú cưng nào chưa sử dụng!");
                }
                break;
            }
            case 5: { // Nâng thông thạo
                if (dt.level >= 100) {
                    String[] name = new String[]{"Kháng phép", "MP+", "Kháng vật lý", "Tăng phòng thủ", "HP+", "Tăng tấn công"};
                    String notice = "Điểm tiềm năng: " + dt.pointAttributeThongThao;
                    short[] id_op = new short[]{27, 16, 26, 4, 15, 1};
                    for (int i = 0; i < name.length; i++) {
                        int value = 0;
                        for (int j = 0; j < dt.list_op_thongthao.size(); j++) {
                            if (dt.list_op_thongthao.get(j).id == id_op[i]) {
                                value += dt.list_op_thongthao.get(j).getParam();
                            }
                        }
                        notice += ("\n" + name[i] + ": " + value + " điểm");
                    }
                    p.data_yesno = new int[]{96};
                    p.setyesNoDialog(new YesNoDialog(p, 96, "Thông báo", notice, new String[]{
                        "+Kháng phép", "+MP", "+Kháng vật lý", "+Tăng phòng thủ", "+HP",
                        "+Tăng tấn công", "Hủy"}, new byte[]{-1, -1, -1, -1, -1, -1, -1}));
                    p.getService().startYesNo();
                } else {
                    p.getService().send_box_ThongBao_OK("Đệ tử chưa đạt level 100!");
                }
                break;
            }
            case 6: { // Chuyển sang đệ tử
                if (dt != null) {
                    p.switchCharacter(dt);
                }
                break;
            }
            case 7: { // Đổi trạng thái đệ tử
                String curStatus = DeTu.STATUS_NAMES[Math.min(dt.detuStatus, DeTu.STATUS_NAMES.length - 1)];
                YesNoDialog dlg = new YesNoDialog(p, 87101,
                        "Trạng Thái Đệ Tử [Đang: " + curStatus + "]",
                        "Chọn trạng thái:",
                        new String[]{"Đi theo", "Bảo vệ", "Tấn công", "Hợp thể", "Về nhà", "Hủy"},
                        new byte[]{-1, -1, -1, -1, -1, -1});
                dlg.setHandler(val -> {
                    try {
                        model.DeTu dt2 = p.detu;
                        if (val == 5 || dt2 == null) return;
                        switch (val) {
                            case 0: dt2.setStatus(DeTu.STATUS_FOLLOW); break;
                            case 1: dt2.setStatus(DeTu.STATUS_PROTECT); break;
                            case 2: dt2.setStatus(DeTu.STATUS_ATTACK); break;
                            case 3: dt2.setStatus(DeTu.STATUS_FUSION); break;
                            case 4: dt2.setStatus(DeTu.STATUS_HOME); break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                p.setyesNoDialog(dlg);
                p.getService().startYesNo();
                break;
            }
            case 8: { // Xóa đệ tử
                DeTu.confirmDeleteDeTu(p);
                break;
            }
        }
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -3;
        buildMenuOptions(p);
        p.getService().openDynamicMenu(type, getNpcName(type), p.menus);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        if (menuId == 32003) {
            handleHeKhac(p, index);
            return;
        } else if (menuId == 943) {
            handleDeTu(p, index);
            return;
        }
        handleMenu(p, index);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        short currentId = (short) p.currentNpcId;
        if (currentId == 32003) {
            handleHeKhac(p, index);
            return;
        } else if (currentId == 943) {
            handleDeTu(p, index);
            return;
        }

        if (p.menus != null && index >= 0 && index < p.menus.size()) {
            p.menus.get(index).execute(p, index);
            return;
        }

        if (p instanceof DeTu) {
            switch (index) {
                case 0: {
                    DeTu dt = (DeTu) p;
                    dt.switchCharacter(dt.master);
                    break;
                }
                case 1: {
                    p.is_show_hat = !p.is_show_hat;
                    p.update_wearing_to_all();
                    p.getService().send_box_ThongBao_OK(p.is_show_hat ? "Đã bật hiển thị nón" : "Đã tắt hiển thị nón");
                    break;
                }
                case 2: {
                    p.getService().Send_UI_Shop(119);
                    break;
                }
                case 3: {
                    p.is_hide_fashion_hair = !p.is_hide_fashion_hair;
                    p.update_wearing_to_all();
                    p.getService().send_box_ThongBao_OK(p.is_hide_fashion_hair ? "Đã che tóc thời trang" : "Đã hiện tóc thời trang");
                    break;
                }
                case 4: {
                    p.is_hide_fashion_head = !p.is_hide_fashion_head;
                    p.update_wearing_to_all();
                    p.getService().send_box_ThongBao_OK(p.is_hide_fashion_head ? "Đã che mặt thời trang" : "Đã hiện mặt thời trang");
                    break;
                }
            }
            return;
        }

        switch (index) {
            case 0: // Phái của bản thân
                p.getService().Send_UI_Shop(p.clazz - 1);
                break;
            case 1: { // Hệ khác
                String[] other_clazz_name = new String[4];
                short[] other_clazz_icon = new short[4];
                int pos = 0;
                for (int i = 0; i < 5; i++) {
                    if ((i + 1) == p.clazz) continue;
                    other_clazz_name[pos] = Clazz.NAME[i];
                    other_clazz_icon[pos] = Clazz.ICON[i];
                    pos++;
                }
                p.getService().openDynamicMenu(32003, "Hệ Khác", other_clazz_name, other_clazz_icon);
                break;
            }
            case 2: // Bật/Tắt hiển thị nón
                p.is_show_hat = !p.is_show_hat;
                p.update_wearing_to_all();
                p.getService().send_box_ThongBao_OK(p.is_show_hat ? "Đã bật hiển thị nón" : "Đã tắt hiển thị nón");
                break;
            case 3: // Khóa bảo vệ
                if (p.timeHuyMbv == -1) {
                    iMenuDymanic.buildAndSend(p, 994, "Khóa Bảo Vệ",
                            new String[]{"Đăng ký khóa bảo vệ", "Hướng dẫn", "Đăng ký hủy mã khóa"},
                            new short[]{118, 148, 118});
                } else {
                    iMenuDymanic.buildAndSend(p, 994, "Khóa Bảo Vệ",
                            new String[]{"Đăng ký khóa bảo vệ", "Hướng dẫn", "Đăng ký hủy mã khóa", "Hủy đăng ký xóa mã rương"},
                            new short[]{118, 148, 118, -1});
                }
                break;
            case 4: // Thùng rác
                p.getService().Send_UI_Shop(119);
                break;
            case 5: // Che / Hiện Tóc Thời Trang
                p.is_hide_fashion_hair = !p.is_hide_fashion_hair;
                p.update_wearing_to_all();
                p.getService().send_box_ThongBao_OK(p.is_hide_fashion_hair ? "Đã che tóc thời trang" : "Đã hiện tóc thời trang");
                break;
            case 6: // Che / Hiện Mặt Thời Trang
                p.is_hide_fashion_head = !p.is_hide_fashion_head;
                p.update_wearing_to_all();
                p.getService().send_box_ThongBao_OK(p.is_hide_fashion_head ? "Đã che mặt thời trang" : "Đã hiện mặt thời trang");
                break;
            case 7: // Đệ tử
                if (p.detu != null) {
                    String statusNow = DeTu.STATUS_NAMES[Math.min(p.detu.detuStatus, DeTu.STATUS_NAMES.length - 1)];
                    iMenuDymanic.buildAndSend(p, 943, "Đệ tử",
                            new String[]{"Thông tin", "Nâng tiềm năng", "Kỹ năng", "Tim", "Sử dụng Pet", "Nâng thông thạo",
                                    "Chuyển sang đệ tử", "Đổi trạng thái [Đang: " + statusNow + "]"}, null);
                }
                break;
        }
    }
}
