package zinterfaces.menus.npcs;

import activities.Max_Level;
import activities.Sudo;
import model.Player;
import model.Menu;
import core.MenuController;
import skill.Skill_info;
import skill.Skill_Template;
import zinterfaces.iNpc;
import zinterfaces.iMenuDymanic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.Npc;

/**
 * NpcGarp — xử lý NPC Gap (-37, -4) + sub-menu động:
 *   978 = Đá hành trình, 997 = Xóa nội tại, 998 = Học skill,
 *   950 = Chức năng Sư Đồ, 499 = Hướng nghiệp
 */
public class NpcGarp implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-37, -4, 978, 997, 998, 950, 499};
    }

    private void buildMenuOptions(Player p, short npcId) {
        p.menus.clear();

        if (p instanceof model.DeTu) {
            p.menus.add(new Menu("Học Skill", (short) 123, () -> handleHocSkill(p)));
            p.menus.add(new Menu("Cường hóa Kỹ năng ác quỷ", (short) 154, () -> {
                try {
                    activities.UpgradeDevil.show_table(p, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
            p.menus.add(new Menu("Chuyển sang sư phụ", (short) 145, () -> {
                model.DeTu dt = (model.DeTu) p;
                if (dt.master != null) {
                    try {
                        p.switchCharacter(dt.master);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Không tìm thấy sư phụ");
                }
            }));
            return;
        }

        // 1. Học Skill (icon 123)
        p.menus.add(new Menu("Học Skill", (short) 123, () -> handleHocSkill(p)));

        // 2. Tẩy tiềm năng (icon 124)
        p.menus.add(new Menu("Tẩy tiềm năng", (short) 124, () -> {
            if (p.get_ngoc() < 5) {
                p.getService().send_box_ThongBao_OK("Bạn không đủ tiền, hồi điểm tiềm năng phải cần 5 Ruby.");
                return;
            }
            if (p.detu != null) {
                p.setyesNoDialog(new model.YesNoDialog(p, 2, "",
                        "Hồi điểm tiềm năng bạn mất 5 Ruby, đệ tử mất 5 Ruby. Bạn có đồng ý không?",
                        new String[]{"5 (Bản thân)", "5 (Đệ tử)", "Hủy"}, new byte[]{7, 7, -1}));
                p.getService().startYesNo();
            } else {
                p.setyesNoDialog(new model.YesNoDialog(p, 2, "",
                        "Hồi điểm tiềm năng bạn mất 5 Ruby. Bạn có đồng ý không?",
                        new String[]{"5 Ruby", "Hủy"}, new byte[]{7, -1}));
                p.getService().startYesNo();
            }
        }));

        // 3. Xóa nội tại (icon 125)
        p.menus.add(new Menu("Xóa nội tại", (short) 125, () -> handleXoaNoiTai(p)));

        // 4. Người giới thiệu (icon 138)
        p.menus.add(new Menu("Người giới thiệu", (short) 138, () -> {
            // Nguoi gioi thieu
        }));

        // Chuyển sang đệ tử (nếu có đệ tử)
        if (p.detu != null) {
            p.menus.add(new Menu("Chuyển sang đệ tử", (short) 145, () -> {
                try {
                    p.switchCharacter(p.detu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        if (npcId == -37) { // Bếp trưởng
            // 5. Đá hành trình (icon 127)
            p.menus.add(new Menu("Đá hành trình", (short) 127, () -> {
                p.getService().openDynamicMenu(978, "Đá hành trình",
                        new String[]{"Kho hành trình", "Bản đồ hành trình", "Hướng dẫn"}, null);
            }));
        } else { // Gap (-4)
            // 5. Thông thạo (icon 158)
            p.menus.add(new Menu("Thông thạo", (short) 158, () -> {
                try {
                    Max_Level.show_table(p);
                } catch (IOException ex) {
                    Logger.getLogger(NpcGarp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }));

            // 6. Chọn Phe (icon 145) - Nếu chưa hướng nghiệp
            if (p.huongnghiep <= 0) {
                p.menus.add(new Menu("Chọn Phe", (short) 145, () -> {
                    try {
                        sendHuongNghiep(p);
                    } catch (IOException ex) {
                        Logger.getLogger(NpcGarp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }));
            }

            // 7. Sư Đồ (icon 145)
            p.menus.add(new Menu("Sư Đồ", (short) 145, () -> {
                p.getService().openDynamicMenu(950, "Chức Năng Sư Đồ",
                        new String[]{"Mở chức năng Sư Phụ", "Ghép vật phẩm sư đồ"},
                        new short[]{158, 127});
            }));
        }
    }

    // -- Gửi sub-menu Hướng Nghiệp khi NPC Gap bấm vào (499) -----------------
    private void sendHuongNghiep(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, 499, "Chọn Phe / Hướng Nghiệp",
                new String[]{"Phe Hải Quân", "Phe Hải Tặc", "Phe Quân Cách Mạng"},
                new short[]{171, 117, 125});
    }

    // -- Xử lý Hướng Nghiệp --------------------------------------------------
    private void handleHuongNghiep(Player p, int index) throws IOException {
        byte selected = (byte) (index + 1);
        String[] factionNames = {"HẢI QUÂN", "HẢI TẶC", "QUÂN CÁCH MẠNG"};
        if (selected >= 1 && selected <= 3) {
            p.huongnghiep = selected;
            p.ensureFactionSkill();
            if (p.map != null && !map.zones.WorldWar.mapTranChienLon(p.map.template.id)) {
                map.zones.WorldWar.setType(p);
            }
            p.send_skill();
            p.update_info_to_all();
            historys.zLog.gI().add_log(p, "HUONG_NGHIEP", "Gia nhập phe " + factionNames[selected - 1]);
            p.getService().send_box_ThongBao_OK("BẠN ĐÃ ĐĂNG KÝ GIA NHẬP PHE: " + factionNames[selected - 1] + "!\nHãy tiến vào Trận Chiến Lớn để chiến đấu!");
        }
    }

    // -- Xử lý Đá Hành Trình -------------------------------------------------
    private void handleDaHanhTrinh(Player p, int index) throws IOException {
        switch (index) {
            case 0: // Kho hành trình
                activities.HanhTrinh.show_table(p, 1);
                break;
            case 1: // Bản đồ hành trình
                activities.HanhTrinh.show_table(p, 0);
                break;
            case 2: { // Hướng dẫn
                String txt = "HƯỚNG DẪN ĐÁ HÀNH TRÌNH\n\n"
                        + "- Đá Hành Trình là những viên đá cổ xưa ẩn chứa sức mạnh to lớn trên khắp các vùng biển.\b"
                        + "CÁCH THU THẬP:\n"
                        + "- Mỗi khi tiêu diệt Boss cuối tại mỗi bản đồ làng, bạn sẽ có 10% cơ hội nhận được Đá Hành Trình tương ứng.\n"
                        + "- Ngoài ra có thể tìm thấy tại các phó bản và rương kho báu đặc biệt.\b"
                        + "TÁC DỤNG & SỬ DỤNG:\n"
                        + "- Đem Đá Hành Trình kích hoạt tại Bản Đồ Hành Trình để tăng vĩnh viễn các chỉ số tiềm năng cho nhân vật.\n"
                        + "- Bạn có thể kiểm tra các viên đá đã thu thập được tại Kho Hành Trình.";
                p.getService().Help_From_Server((short) -4, txt);
                break;
            }
        }
    }

    // -- Xử lý Chức Năng Sư Đồ -----------------------------------------------
    private void handleChucNangSuDo(Player p, int index) throws IOException {
        switch (index) {
            case 0: { // Mở chức năng Sư Phụ
                Sudo mySudo = Sudo.getSuDoByName(p.name);
                if (mySudo == null) {
                    if (p.item.total_item_bag_by_id(4, 627) > 0) {
                        p.item.remove_item47(4, 627, 1);
                        p.item.update_Inventory_bag(-1, false);
                        Sudo sudoAdd = new Sudo();
                        sudoAdd.id = p.IDPlayer;
                        sudoAdd.accountId = p.conn != null ? p.conn.idUser : 0;
                        sudoAdd.name = p.name;
                        sudoAdd.head = p.head;
                        sudoAdd.hair = p.hair;
                        sudoAdd.hat = p.get_hat();
                        sudoAdd.chucInSudo = 1;
                        sudoAdd.lvExp = 1;
                        sudoAdd.exp = 0;
                        sudoAdd.point = 1;
                        sudoAdd.nameRelative = new ArrayList<>();
                        sudoAdd.nameRelative.add(p.name);
                        Sudo.addSuDoByName(p.name, sudoAdd);
                        Sudo.updateDb();
                        p.getService().sendSudoInfo(false);
                        p.getService().send_box_ThongBao_OK("Chúc mừng bạn đã mở thành công chức năng Sư Phụ! Bạn có thể nhận người chơi khác làm đệ tử cũng như thu nhận đệ tử hoang.");
                    } else {
                        p.getService().send_box_ThongBao_OK("Bạn cần có 1 [Chứng nhận sư phụ] để mở chức năng Sư Phụ! (Vật phẩm rơi từ Phó bản với tỉ lệ may mắn 1%).");
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn hiện tại đã có sư đồ.");
                }
                break;
            }
            case 1: { // Ghép vật phẩm sư đồ
                itemz.rebuilds.GhepChungNhanSuDo.showTable(p);
                break;
            }
        }
    }

    private void handleHocSkill(Player p) {
        List<String> str_ = new ArrayList<>();
        List<Integer> icon_ = new ArrayList<>();
        for (int i = 0; i < p.skill_point.size(); i++) {
            Skill_info temp = p.skill_point.get(i);
            if ((temp.temp.ID < 4 && temp.temp.Lv_RQ == -1) || (temp.temp.ID > 3 && temp.temp.ID < 2000 && temp.temp.Lv_RQ < 5)) {
                if (temp.temp.ID > 3 && temp.temp.ID < 2000 && temp.temp.Lv_RQ > -1) {
                    Skill_Template sk_temp = Skill_Template.get_temp((temp.temp.indexSkillInServer + 1), 0);
                    if (sk_temp != null) {
                        str_.add(sk_temp.name);
                        icon_.add((int) (sk_temp.idIcon));
                    }
                } else {
                    str_.add(temp.temp.name);
                    icon_.add((int) (temp.temp.idIcon));
                }
            }
        }
        if (str_.isEmpty()) {
            str_.add("Hiện tại không có kỹ năng thích hợp để học");
            icon_ = null;
        }
        p.getService().send_dynamic_menu_type4(998, 0, "Học kỹ năng - Gap", str_, icon_);
    }

    // -- Xử lý chọn Học Skill (998) ------------------------------------------
    private void handleHocSkillSelect(Player p, int index) throws IOException {
        MenuController.Menu_Learn_Skill(p, (byte) index);
    }

    private void handleXoaNoiTai(Player p) {
        try {
            List<String> str_ = new ArrayList<>();
            List<Integer> icon_ = new ArrayList<>();
            for (int i = 0; i < p.skill_point.size(); i++) {
                Skill_info temp = p.skill_point.get(i);
                if (temp.temp.ID >= 1000 && temp.temp.ID < 2000 && temp.temp.Lv_RQ > 0) {
                    str_.add(temp.temp.name);
                    icon_.add((int) (temp.temp.idIcon));
                }
            }
            if (str_.isEmpty()) {
                iMenuDymanic.buildAndSend(p, 997, "Xóa kỹ năng - Gap",
                        new String[]{"Hiện tại chưa học nội tại gì"}, null);
            } else {
                p.getService().send_dynamic_menu_type4(997, 0, "Xóa kỹ năng - Gap", str_, icon_);
            }
        } catch (IOException ex) {
            Logger.getLogger(NpcGarp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // -- Xử lý chọn Xóa Nội Tại (997) ---------------------------------------
    private void handleXoaNoiTaiSelect(Player p, int index) throws IOException {
        MenuController.Menu_Remove_Skill(p, (byte) index);
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -4;
        String title = (type == -37) ? "Bếp trưởng" : "Gap";
        buildMenuOptions(p, type);
        p.getService().openDynamicMenu(type, title, p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        short currentId = (short) p.currentNpcId;
        switch (currentId) {
            case 978: // sub-menu Đá hành trình
                handleDaHanhTrinh(p, index);
                break;
            case 997: // sub-menu Xóa nội tại
                handleXoaNoiTaiSelect(p, index);
                break;
            case 998: // sub-menu Học skill
                handleHocSkillSelect(p, index);
                break;
            case 950: // sub-menu Chức năng Sư Đồ
                handleChucNangSuDo(p, index);
                break;
            case 499: // sub-menu Hướng nghiệp
                handleHuongNghiep(p, index);
                break;
            case -37: // Bếp trưởng
                switch (index) {
                    case 0: // Học Skill
                        handleHocSkill(p);
                        break;
                    case 1: // Tẩy tiềm năng
                        if (p.get_ngoc() < 5) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ tiền, hồi điểm tiềm năng phải cần 5 Ruby.");
                            return;
                        }
                        if (p.detu != null) {
                            p.setyesNoDialog(new model.YesNoDialog(p, 2, "",
                                    "Hồi điểm tiềm năng bạn mất 5 Ruby, đệ tử mất 5 Ruby. Bạn có đồng ý không?",
                                    new String[]{"5 (Bản thân)", "5 (Đệ tử)", "Hủy"}, new byte[]{7, 7, -1}));
                            p.getService().startYesNo();
                        } else {
                            p.setyesNoDialog(new model.YesNoDialog(p, 2, "",
                                    "Hồi điểm tiềm năng bạn mất 5 Ruby. Bạn có đồng ý không?",
                                    new String[]{"5 Ruby", "Hủy"}, new byte[]{7, -1}));
                            p.getService().startYesNo();
                        }
                        break;
                    case 2: // Xóa nội tại
                        handleXoaNoiTai(p);
                        break;
                    case 3: // Người giới thiệu
                        break;
                    case 4: // Đá hành trình
                        p.getService().openDynamicMenu(978, "Đá hành trình",
                                new String[]{"Kho hành trình", "Bản đồ hành trình", "Hướng dẫn"}, null);
                        break;
                }
                break;
            case -4: // Gap
                if (p.huongnghiep <= 0) {
                    switch (index) {
                        case 0: handleHocSkill(p); break;
                        case 1:
                            if (p.get_ngoc() < 5) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ tiền, hồi điểm tiềm năng phải cần 5 Ruby.");
                                return;
                            }
                            if (p.detu != null) {
                                p.setyesNoDialog(new model.YesNoDialog(p, 2, "",
                                        "Hồi điểm tiềm năng bạn mất 5 Ruby, đệ tử mất 5 Ruby. Bạn có đồng ý không?",
                                        new String[]{"5 (Bản thân)", "5 (Đệ tử)", "Hủy"}, new byte[]{7, 7, -1}));
                                p.getService().startYesNo();
                            } else {
                                p.setyesNoDialog(new model.YesNoDialog(p, 2, "",
                                        "Hồi điểm tiềm năng bạn mất 5 Ruby. Bạn có đồng ý không?",
                                        new String[]{"5 Ruby", "Hủy"}, new byte[]{7, -1}));
                                p.getService().startYesNo();
                            }
                            break;
                        case 2: handleXoaNoiTai(p); break;
                        case 3: break; // Người giới thiệu
                        case 4: Max_Level.show_table(p); break; // Thông thạo
                        case 5: // Chọn Phe
                            p.getService().openDynamicMenu(499, "Hướng nghiệp",
                                    new String[]{"Hải Tặc", "Hải Quân"}, null);
                            break;
                        case 6: // Sư Đồ
                            p.getService().openDynamicMenu(950, "Chức Năng Sư Đồ",
                                    new String[]{"Mở chức năng Sư Phụ", "Ghép vật phẩm sư đồ"},
                                    new short[]{158, 127});
                            break;
                    }
                } else {
                    switch (index) {
                        case 0: handleHocSkill(p); break;
                        case 1:
                            if (p.get_ngoc() < 5) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ tiền, hồi điểm tiềm năng phải cần 5 Ruby.");
                                return;
                            }
                            if (p.detu != null) {
                                p.setyesNoDialog(new model.YesNoDialog(p, 2, "",
                                        "Hồi điểm tiềm năng bạn mất 5 Ruby, đệ tử mất 5 Ruby. Bạn có đồng ý không?",
                                        new String[]{"5 (Bản thân)", "5 (Đệ tử)", "Hủy"}, new byte[]{7, 7, -1}));
                                p.getService().startYesNo();
                            } else {
                                p.setyesNoDialog(new model.YesNoDialog(p, 2, "",
                                        "Hồi điểm tiềm năng bạn mất 5 Ruby. Bạn có đồng ý không?",
                                        new String[]{"5 Ruby", "Hủy"}, new byte[]{7, -1}));
                                p.getService().startYesNo();
                            }
                            break;
                        case 2: handleXoaNoiTai(p); break;
                        case 3: break; // Người giới thiệu
                        case 4: Max_Level.show_table(p); break; // Thông thạo
                        case 5: // Sư Đồ
                            iMenuDymanic.buildAndSend(p, 950, "Chức Năng Sư Đồ",
                                    new String[]{"Mở chức năng Sư Phụ", "Ghép vật phẩm sư đồ"},
                                    new short[]{158, 127});
                            break;
                    }
                }
                break;
        }
    }
}
