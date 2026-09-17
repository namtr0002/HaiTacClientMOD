package zinterfaces.menus.npcs;

import model.Player;
import map.Npc;
import template.EffTemplate;
import zinterfaces.iNpc;
import zabstracts.AbsListTichNap;
import zabstracts.AbsTichTieuRuby;
import zabstracts.AbsTichLuyCongDon;
import event.Event;
import event.EventManager;
import model.Menu;
import skill.Skill_info;

import java.io.IOException;
import java.util.Collection;

/**
 * NpcRubin — Handler cho NPC Rubin / Robin (idmenu = -100).
 * Quản lý toàn bộ:
 *   - Các sự kiện đang hoạt động (Sự kiện, Tích nạp sự kiện, Tích tiêu sự kiện, Hướng dẫn sự kiện)
 *   - Hệ thống Tích lũy nạp thẻ, Tích tiêu Ruby, Nạp hằng ngày, Tích lũy cộng dồn
 *   - Base features: x2 EXP, khóa EXP, Tài xỉu, Xem Skill
 */
public class NpcRubin implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{-100, 989};
    }

    @Override
    public String getChatText() {
        return "Chào bạn! Tôi là Robin. Bạn muốn tham gia sự kiện hay nhận ưu đãi tích nạp?";
    }

    @Override
    public String[] getChatTexts() {
        return new String[]{
            "Chào bạn! Tôi là Robin. Bạn cần hỗ trợ gì?",
            "Sự kiện đang diễn ra rất sôi động với nhiều phần quà hấp dẫn!",
            "Đừng quên kiểm tra các mốc Tích nạp và Nạp hằng ngày nhé!",
            "Hãy đọc kỹ hướng dẫn sự kiện để săn nhiều nguyên liệu quý!"
        };
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        short type = npc != null ? npc.idmenu : -100;
        String title = (npc != null && npc.name != null) ? npc.name : "Chị Robin";
        p.menus.clear();

        // HỆ THỐNG TÍCH NẠP / TÍCH TIÊU
        if (EventManager.hasActiveEvent()) {
            Event activeEv = EventManager.gI().getActiveEvent();
            p.menus.add(new Menu("Tích nạp sự kiện", (short) 110, () -> {
                try {
                    if (activeEv != null) {
                        activeEv.showTichNap(p);
                    } else {
                        p.getService().send_box_ThongBao_OK("Không tìm thấy sự kiện tích nạp!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

            p.menus.add(new Menu("Tích tiêu sự kiện", (short) 110, () -> {
                try {
                    if (activeEv != null) {
                        activeEv.showTichTieu(p);
                    } else {
                        p.getService().send_box_ThongBao_OK("Không tìm thấy sự kiện tích tiêu!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        } else {
            p.menus.add(new Menu("Tích nạp", (short) 110, () -> {
                try {
                    p.getService().openDynamicMenu(9933, "Tích nạp",
                            new String[]{"Tích nạp ngày", "Tích nạp tổng"},
                            new short[]{110, 110});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            p.menus.add(new Menu("Tích tiêu", (short) 110, () -> {
                try {
                    p.typeShopTichTieu = 1;
                    p.typeShopTichTieuSuKien = 0;
                    AbsTichTieuRuby tichTieu = AbsTichTieuRuby.get(1);
                    if (tichTieu != null) {
                        tichTieu.showTable(p);
                    } else {
                        p.getService().send_box_ThongBao_OK("Chức năng Tích tiêu đang bảo trì!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }

        p.menus.add(new Menu("Tích lũy cộng dồn", (short) 134, () -> {
            try {
                p.typeTichLuyCongDon = 4;
                p.refreshRecharge();
                AbsTichLuyCongDon tichLuy = AbsTichLuyCongDon.get(4);
                if (tichLuy != null) {
                    tichLuy.showTable(p);
                } else {
                    p.getService().send_box_ThongBao_OK("Chức năng Tích lũy cộng dồn đang bảo trì!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        // 3. MENU CƠ BẢN (BASE MENU)
        p.menus.add(new Menu("T/g x2 kỹ năng EXP", (short) -1, () -> {
            try {
                handleBaseMenu(p, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        p.menus.add(new Menu("T/g khóa EXP", (short) -1, () -> {
            try {
                handleBaseMenu(p, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        p.menus.add(new Menu("Hủy t/g khóa EXP", (short) -1, () -> {
            try {
                handleBaseMenu(p, 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        p.menus.add(new Menu("Tài xỉu", (short) -1, () -> {
            try {
                handleBaseMenu(p, 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        p.menus.add(new Menu("Xem Skill", (short) -1, () -> {
            try {
                handleBaseMenu(p, 4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        p.getService().openDynamicMenu(type, title, p.menus);
    }

    private void handleBaseMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                EffTemplate eff = p.get_eff(3);
                if (eff != null) {
                    p.getService().send_box_ThongBao_OK(
                        "Thời gian x2 kỹ năng EXP còn lại "
                        + core.ZUtil.get_time_str_by_sec2(eff.time - System.currentTimeMillis())
                        + "\nLưu ý cộng dồn tối đa 7 ngày");
                } else {
                    p.getService().send_box_ThongBao_OK("Thời gian x2 kỹ năng EXP còn lại 0 s.");
                }
                break;
            }
            case 1: {
                EffTemplate eff = p.get_eff(8);
                if (eff != null) {
                    p.getService().send_box_ThongBao_OK(
                        "Thời gian khóa EXP còn lại "
                        + core.ZUtil.get_time_str_by_sec2(eff.time - System.currentTimeMillis())
                        + "\nLưu ý cộng dồn tối đa 30 ngày");
                } else {
                    p.getService().send_box_ThongBao_OK("Thời gian khóa EXP còn lại 0 s.");
                }
                break;
            }
            case 2: {
                if (p.detu != null) {
                    p.getService().openDynamicMenu(933, "Hủy khóa exp",
                        new String[]{"Bản thân", "Đệ tử"}, null);
                } else {
                    EffTemplate eff = p.get_eff(8);
                    if (eff != null) {
                        eff.time = 0;
                        p.getService().send_box_ThongBao_OK("Hủy thành công");
                    } else {
                        p.getService().send_box_ThongBao_OK("Thời gian khóa EXP còn lại 0 s.");
                    }
                }
                break;
            }
            case 3: {
                if (p.getConnStatus() != 1) {
                    p.getService().send_box_ThongBao_OK("Chưa kích hoạt thành viên không thể tham gia");
                    return;
                }
                p.getService().openDynamicMenu(989, "Tài xỉu",
                    new String[]{"Tham gia", "Nhận thưởng"}, null);
                break;
            }
            case 4: {
                StringBuilder info = new StringBuilder("Danh sách kỹ năng:");
                for (int i = 0; i < p.skill_point.size(); i++) {
                    Skill_info s = p.skill_point.get(i);
                    if (s.temp.typeSkill == 1 && s.temp.Lv_RQ > 0) {
                        info.append("\n ").append(s.temp.name).append(" Lv").append(s.temp.Lv_RQ);
                        if (s.temp.ID < 4) {
                            info.append(" ").append(s.get_percent() / 10).append("%");
                        }
                    }
                }
                p.getService().send_box_ThongBao_OK(info.toString());
                break;
            }
        }
    }

    private void handleTaiXiuSubMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: { // Tham gia đặt cược
                p.sendInput("Đặt Cược Tài Xỉu", new String[]{"Lựa chọn (0: Xỉu, 1: Tài):", "Số tiền cược (Vàng):"}, inputs -> {
                    try {
                        byte typeTX = Byte.parseByte(inputs[0].trim());
                        int money = Integer.parseInt(inputs[1].trim());
                        if (typeTX != 0 && typeTX != 1) {
                            p.getService().send_box_ThongBao_OK("Vui lòng nhập 0 (Xỉu) hoặc 1 (Tài)!");
                            return;
                        }
                        if (money < 100_000 || money > 500_000_000) {
                            p.getService().send_box_ThongBao_OK("Số tiền cược từ 100.000 đến 500.000.000 Vàng!");
                            return;
                        }
                        if (p.get_vang() < money) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ Vàng!");
                            return;
                        }
                        core.Manager.gI().TaiXiu().register(p, money, typeTX);
                    } catch (Exception e) {
                        p.getService().send_box_ThongBao_OK("Dữ liệu nhập vào không hợp lệ!");
                    }
                });
                break;
            }
            case 1: { // Nhận thưởng
                template.TaiXiuInfo info = core.Manager.gI().TaiXiu().get_my_result(p);
                if (info != null && info.money > 0) {
                    long winMoney = info.money;
                    p.update_vang(winMoney);
                    p.updateMoney();
                    core.Manager.gI().TaiXiu().remove_result(p);
                    p.getService().send_box_ThongBao_OK("Chúc mừng! Bạn đã nhận thưởng thành công " + core.ZUtil.number_format(winMoney) + " Vàng từ Tài Xỉu!");
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn không có tiền thưởng Tài Xỉu để nhận!");
                }
                break;
            }
        }
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        short currentId = (short) p.currentNpcId;
        if (currentId == 989) {
            handleTaiXiuSubMenu(p, index);
            return;
        }

        if (index >= 0 && index < p.menus.size()) {
            Menu m = p.menus.get(index);
            if (m != null) {
                m.confirm(p);
                return;
            }
        }

        handleBaseMenu(p, index);
    }
}
