package zinterfaces.menus.npcs;

import model.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.Npc;
import zinterfaces.iNpc;
import zinterfaces.iMenuDymanic;
import achievement.ArchiDaily;
import activities.Market;
import activities.DanhHieu;
import zabstracts.AbsListTichNap;
import zabstracts.AbsTichTieuRuby;
import zabstracts.AbsTichLuyCongDon;
import zabstracts.AbsShop;
import event.Event;
import event.EventManager;
import model.TichLuyCongDon;
import core.MenuController;
import model.Menu;
import model.DauGia;
import model.InputDialog;
import model.VongQuayWC;

/**
 * NpcNami — NPC Nami (-72) + sub-menu động:
 *   993 = Đổi Ruby/Extol/Code
 *   981 = Đấu giá
 *   994 = Khóa bảo vệ
 * Tự động kết hợp các Dynamic Sub-menu (như DynamicNamiPass - 9956) qua iMenuDymanic.
 */
public class NpcNami implements iNpc {

    @FunctionalInterface
    private interface MenuAction {
        void run() throws IOException;
    }

    private static class MenuOption {
        String name;
        short icon;
        MenuAction action;

        MenuOption(String name, short icon, MenuAction action) {
            this.name = name;
            this.icon = icon;
            this.action = action;
        }
    }

    @Override
    public short[] getId() {
        return new short[]{-72, 993, 981, 9933, 9934};
    }

    @Override
    public String getChatText() {
        return "Chào bạn, mình là Nami! Bạn cần hỗ trợ gì?";
    }

    @Override
    public String[] getChatTexts() {
        return new String[]{
            "Chào bạn, mình là Nami! Bạn cần hỗ trợ gì?",
            "Shop Tích Luỹ có rất nhiều thời trang đẹp nha!",
            "Đổi Ruby sang Extol rất dễ dàng!",
            "Bản đồ kho báu ở đây có giá trị cực lớn!"
        };
    }

    private List<MenuOption> getOptions(Player p) {
        List<MenuOption> options = new ArrayList<>();

        // 1. Đổi Tiền
        options.add(new MenuOption("Đổi Tiền", (short) 132, () -> {
            p.getService().openDynamicMenu(993, "Nami",
                    new String[]{"Đổi Coin ra Extol", "Đổi Extol ra Ruby", "Mã quà tặng"},
                    new short[]{128, 132, 110});
        }));

        // 2. Thành tích hằng ngày
        options.add(new MenuOption("Thành tích hằng ngày", (short) 134, () -> {
            ArchiDaily.show_table(p);
        }));

        // 3. Nami Pass
        options.add(new MenuOption("Nami Pass", (short) 134, () -> {
            zinterfaces.menus.dymanics.DynamicNamiPass.showNamiPassMenu(p);
        }));

        // 4. Tích nạp & Tích tiêu
        if (!EventManager.hasActiveEvent()) {
            options.add(new MenuOption("Tích nạp", (short) 110, () -> {
                p.getService().openDynamicMenu(9933, "Tích nạp",
                        new String[]{"Tích nạp ngày", "Tích nạp tuần", "Tích nạp tổng", "Lịch sử nạp"},
                        new short[]{110, 110, 110, 132});
            }));

            options.add(new MenuOption("Tích tiêu", (short) 110, () -> {
                p.getService().openDynamicMenu(9934, "Tích tiêu",
                        new String[]{"Tích tiêu ngày", "Tích tiêu tuần", "Tích tiêu tổng"},
                        new short[]{110, 110, 110});
            }));
        }

        // 5. Đấu giá
        options.add(new MenuOption("Đấu giá", (short) 169, () -> {
            p.getService().openDynamicMenu(981, "Đấu giá", new String[]{"Đấu giá", "Đổi extol sang búa",
                "Đổi búa sang extol", "Nhận búa đấu giá về"}, null);
        }));

        // 6. Chợ mua bán
        options.add(new MenuOption("Chợ mua bán", (short) 152, () -> {
            Market.show_table(p);
        }));

        // 6. Map 17 specific items: Khóa bảo vệ, Nạp tiền
        if (p.map.template.id == 17) {
            options.add(new MenuOption("Khóa bảo vệ", (short) 152, () -> {
                if (p.timeHuyMbv == -1) {
                    p.getService().openDynamicMenu(994, "Khóa Bảo Vệ",
                            new String[]{"Đăng ký khóa bảo vệ", "Hướng dẫn", "Đăng ký hủy mã khóa"},
                            new short[]{118, 148, 118});
                } else {
                    p.getService().openDynamicMenu(994, "Khóa Bảo Vệ",
                            new String[]{"Đăng ký khóa bảo vệ", "Hướng dẫn", "Đăng ký hủy mã khóa", "Hủy đăng ký xóa mã rương"},
                            new short[]{118, 148, 118, -1});
                }
            }));
            options.add(new MenuOption("Nạp tiền", (short) 133, () -> {
                p.getService().send_box_ThongBao_OK("Vui lòng liên hệ Admin hoặc truy cập website để thực hiện nạp tiền!");
            }));
        }

        // 7. Shop Tích Luỹ
        options.add(new MenuOption("Shop Tích Luỹ", (short) 170, () -> {
            AbsShop shop = AbsShop.get(118);
            if (shop != null) {
                shop.openUI(p);
            }
        }));

        // 8. Danh Hiệu
        if (p.map.template.id != 17) {
            options.add(new MenuOption("Danh Hiệu", (short) 133, () -> {
                if (DanhHieu.ENY.isEmpty()) {
                    p.getService().send_box_ThongBao_OK("Danh sách Danh Hiệu trống.");
                } else {
                    String[] titles = new String[DanhHieu.ENY.size()];
                    for (int i = 0; i < titles.length; i++) {
                        DanhHieu dh = DanhHieu.ENY.get(i);
                        int[] owned = p.getDanhHieuById(dh.id);
                        if (owned != null && owned.length >= 2 && owned[1] == 1) {
                            titles[i] = dh.Name + " [Đang Dùng]";
                        } else if (owned != null) {
                            titles[i] = dh.Name + " [Đã Có]";
                        } else {
                            titles[i] = dh.Name;
                        }
                    }
                    p.getService().openDynamicMenu(999, "Danh Hiệu Nhân Vật", titles, null);
                }
            }));
        }

        return options;
    }

    private void handleDoiRuby(Player p, int index) throws IOException {
        if ((index == 0 || index == 1) && core.Manager.gI().lock_coin_exchange_racing && core.Manager.gI().isTopRacingRunning()) {
            p.getService().send_box_ThongBao_OK("Chức năng đổi Xu / Extol sang Ruby đang tạm khóa trong thời gian Đua Top Cao Thủ để đảm bảo tính công bằng cho toàn bộ người chơi!");
            return;
        }
        switch (index) {
            case 0: // Đổi Coin ra Extol (1 Coin = 1 Extol = 1 VND)
                p.sendInput("Đổi Coin ra Extol", new String[]{"Nhập số lượng Coin muốn đổi:"}, textInputs -> {
                    try {
                        long coinAmt = Long.parseLong(textInputs[0].trim());
                        if (coinAmt <= 0) {
                            p.getService().send_box_ThongBao_OK("Số lượng Coin nhập phải lớn hơn 0!");
                            return;
                        }
                        if (p.get_coin() < coinAmt) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ " + core.ZUtil.number_format(coinAmt) + " Coin (Hiện có: " + core.ZUtil.number_format(p.get_coin()) + ")!");
                            return;
                        }
                        long pointBonus = coinAmt / 2;
                        p.update_coin(-coinAmt);
                        p.updateVnd(coinAmt);
                        if (pointBonus > 0) {
                            p.update_pointEvent1((int) Math.min(Integer.MAX_VALUE, pointBonus));
                        }

                        // Đồng bộ tăng tích nạp tổng, tích nạp ngày, cập nhật điểm VIP và cấp VIP 20 cấp chuẩn theo website
                        p.addRecharge(coinAmt);
                        p.updateMoney();

                        int curVip = p.getVip();
                        String vipTitle = p.getVipTitle();
                        String notifyMsg = "Đổi thành công " + core.ZUtil.number_format(coinAmt) + " Coin sang " + core.ZUtil.number_format(coinAmt) + " Extol (1 Coin = 1 Extol)!\n"
                                + "+ Tích lũy Sự Kiện: +" + core.ZUtil.number_format(pointBonus) + " Điểm.\n"
                                + "+ Tích nạp: +" + core.ZUtil.number_format(coinAmt) + " VNĐ (Tổng: " + core.ZUtil.number_format(p.getTongnap()) + " VNĐ).\n"
                                + "+ Cấp VIP: VIP " + curVip + " (" + vipTitle + ").";

                        p.getService().send_box_ThongBao_OK(notifyMsg);
                        historys.zLog.gI().add_log(p, "Đổi Coin sang Extol", "Đổi " + coinAmt + " Coin -> " + coinAmt + " Extol (+ " + pointBonus + " Điểm Sự Kiện, +VIP " + curVip + ")");
                    } catch (Exception e) {
                        p.getService().send_box_ThongBao_OK("Số nhập vào không hợp lệ!");
                    }
                });
                break;
            case 1: // Đổi Extol ra Ruby (1000 Extol = 1 Ruby)
                p.sendInput("Đổi Extol ra Ruby", new String[]{"Nhập số lượng Extol (tối thiểu 1.000):"}, textInputs -> {
                    try {
                        long extolAmt = Long.parseLong(textInputs[0].trim());
                        if (extolAmt < 1000) {
                            p.getService().send_box_ThongBao_OK("Số lượng Extol nhập phải từ 1.000 Extol trở lên (1.000 Extol = 1 Ruby)!");
                            return;
                        }
                        long rubyGain = extolAmt / 1000L;
                        long extolDeduct = rubyGain * 1000L;
                        if (p.get_vnd() < extolDeduct) {
                            p.getService().send_box_ThongBao_OK("Bạn không đủ " + core.ZUtil.number_format(extolDeduct) + " Extol!");
                            return;
                        }
                        p.updateVnd(-extolDeduct);
                        p.update_ngoc_ex((int) rubyGain);
                        p.updateMoney();
                        p.getService().send_box_ThongBao_OK("Đổi thành công " + core.ZUtil.number_format(extolDeduct) + " Extol sang " + core.ZUtil.number_format(rubyGain) + " Ruby (Tỉ giá: 1.000 Extol = 1 Ruby)!");
                    } catch (Exception e) {
                        p.getService().send_box_ThongBao_OK("Số nhập vào không hợp lệ!");
                    }
                });
                break;
            case 2: // Mã quà tặng
                p.sendInput("Nhập Mã Quà Tặng", new String[]{"Nhập Code:"}, textInputs -> {
                    if (textInputs != null && textInputs.length > 0 && !textInputs[0].trim().isEmpty()) {
                        template.GiftTemplate.execute(p, new String[]{textInputs[0].trim()});
                    } else {
                        p.getService().send_box_ThongBao_OK("Mã quà tặng không được để trống!");
                    }
                });
                break;
        }
    }

    private void handleDauGia(Player p, int index) throws IOException {
        switch (index) {
            case 0:
                DauGia.show_table(p);
                break;
            case 1:
                p.setinputDialog(new InputDialog(15, "Đổi búa đấu giá", new String[]{"Nhập số lượng Extol"}));
                p.getService().startInput();
                break;
            case 2:
                new InputDialog(p, 14, "Đổi extol từ búa", new String[]{"Nhập số lượng Búa"}).startInput();
                break;
            case 3:
                DauGia.getBuaBack(p);
                break;
        }
    }

    private void handleKhoaBaoVe(Player p, int index) throws IOException {
        if (index == 0) {
            if (p.mbv.isBlank()) {
                p.sendInput("Nhập mã bảo vệ:", new String[]{""}, inputs -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].trim().isEmpty()) {
                        p.mbv = inputs[0].trim();
                        p.getService().send_box_ThongBao_OK("Đăng ký mã bảo vệ thành công!");
                    }
                });
            } else {
                p.getService().send_box_ThongBao_OK("Hiện tại đang dùng mã khoá bảo vệ khác!");
            }
        } else if (index == 1) {
            String txt = "HƯỚNG DẪN KHÓA BẢO VỆ\n\n"
                    + "• Ai cũng có những món đồ mình rất quý trọng và không muốn mất nó.\n"
                    + "• Chức năng Khóa Bảo Vệ sẽ giúp bạn bảo vệ tài sản tài khoản an toàn tuyệt đối.\b"
                    + "CÁCH THỨC HOẠT ĐỘNG:\n"
                    + "- Sau khi đăng ký mã khóa bảo vệ, các thao tác có ảnh hưởng đến tài sản của bạn sẽ yêu cầu nhập đúng mã để xác nhận.\n"
                    + "- Khi đang khóa bảo vệ, các thao tác: Giao dịch, Bán trang bị, Vứt bỏ item, Tháo ngọc... sẽ bị chặn để chống trộm cắp.\b"
                    + "QUY ĐỊNH HỦY MÃ KHÓA:\n"
                    + "- Nếu muốn hủy mã, bạn có thể chọn 'Đăng ký hủy mã khóa'.\n"
                    + "- Thời gian chờ hủy an toàn là 24 giờ (1 ngày) để đảm bảo an toàn tuyệt đối cho tài khoản.";
            p.getService().Help_From_Server(-72, txt);
        } else if (index == 2) {
            if (p.mbv.isBlank()) {
                p.getService().send_box_ThongBao_OK("Hiện tại chưa đăng ký mã khoá bảo vệ!");
            } else {
                if (p.timeHuyMbv != -1) {
                    long t = p.timeHuyMbv - System.currentTimeMillis();
                    t /= 1000;
                    if (t < 0) t = 0;
                    p.getService().send_box_ThongBao_OK("Hiện tại đã đăng ký hủy mã. Thời gian còn lại: " + t + "s");
                } else {
                    p.data_yesno = new int[]{90};
                    p.setyesNoDialog(new model.YesNoDialog(p, 90, "Thông báo", "Bạn muốn đăng ký hủy mã khóa? Thời gian hủy 1 ngày.",
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                    p.getService().startYesNo();
                }
            }
        } else if (index == 3) {
            if (!p.mbv.isBlank() && p.timeHuyMbv != -1) {
                p.timeHuyMbv = -1;
                p.mbv = "";
                p.passBagOK = true;
                p.getService().send_box_ThongBao_OK("Hủy đăng ký thành công");
            }
        }
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
        int type = npc != null ? npc.idmenu : -72;
        if (p.map.template.id == 62) {
            p.getService().openDynamicMenu(type, "Nami", new ArrayList<>());
            return;
        }

        List<MenuOption> options = getOptions(p);
        p.menus.clear();
        for (MenuOption opt : options) {
            p.menus.add(new Menu(opt.name, opt.icon, () -> {
                try {
                    opt.action.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }

        p.getService().openDynamicMenu(type, "Nami", p.menus);
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        if (p.map.template.id == 62) return;

        short currentId = (short) p.currentNpcId;
        switch (currentId) {
            case 993:
                handleDoiRuby(p, index);
                break;
            case 981:
                handleDauGia(p, index);
                break;
            case 994:
                handleKhoaBaoVe(p, index);
                break;
            case 9933:
                handleTichNapSubMenu(p, index);
                break;
            case 9934:
                handleTichTieuSubMenu(p, index);
                break;
            default:
                List<MenuOption> options = getOptions(p);
                if (index >= 0 && index < options.size()) {
                    options.get(index).action.run();
                }
                break;
        }
    }

    private void handleTichNapSubMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: // Tích nạp ngày
                p.typeShopTichNap = 2;
                p.typeShopTichNapSuKien = 0;
                p.refreshRecharge();
                p.ensureTichHangNgayCheck();
                AbsListTichNap napNgay = AbsListTichNap.get(2);
                if (napNgay != null) {
                    napNgay.showTable(p);
                } else {
                    p.getService().send_box_ThongBao_OK("Chức năng Tích nạp ngày đang bảo trì!");
                }
                break;
            case 1: // Tích nạp tuần
                p.typeShopTichNap = 5;
                p.typeShopTichNapSuKien = 0;
                p.refreshRecharge();
                p.ensureTichNapTuanCheck();
                AbsListTichNap napTuan = AbsListTichNap.get(5);
                if (napTuan != null) {
                    napTuan.showTable(p);
                } else {
                    p.getService().send_box_ThongBao_OK("Chức năng Tích nạp tuần đang bảo trì!");
                }
                break;
            case 2: // Tích nạp tổng
                p.typeShopTichNap = 0;
                p.typeShopTichNapSuKien = 0;
                p.refreshRecharge();
                p.ensureTichNapCheck();
                AbsListTichNap tichNap = AbsListTichNap.get(0);
                if (tichNap != null) {
                    tichNap.showTable(p);
                } else {
                    p.getService().send_box_ThongBao_OK("Chức năng Tích nạp tổng đang bảo trì!");
                }
                break;
            case 3: // Lịch sử nạp
                historys.RechargeHistory.showRechargeHistoryDialog(p);
                break;
        }
    }

    private void handleTichTieuSubMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: // Tích tiêu ngày
                p.typeShopTichTieu = 1;
                p.typeShopTichTieuSuKien = 0;
                p.ensureTieuRubyCheck();
                AbsTichTieuRuby tichTieuNgay = AbsTichTieuRuby.get(1);
                if (tichTieuNgay != null) {
                    tichTieuNgay.showTable(p);
                } else {
                    p.getService().send_box_ThongBao_OK("Chức năng Tích tiêu ngày đang bảo trì!");
                }
                break;
            case 1: // Tích tiêu tuần
                p.typeShopTichTieu = 5;
                p.typeShopTichTieuSuKien = 0;
                p.ensureTieuTuanCheck();
                AbsTichTieuRuby tichTieuTuan = AbsTichTieuRuby.get(5);
                if (tichTieuTuan != null) {
                    tichTieuTuan.showTable(p);
                } else {
                    p.getService().send_box_ThongBao_OK("Chức năng Tích tiêu tuần đang bảo trì!");
                }
                break;
            case 2: // Tích tiêu tổng
                p.typeShopTichTieu = 6;
                p.typeShopTichTieuSuKien = 0;
                p.ensureTieuTongCheck();
                AbsTichTieuRuby tichTieuTong = AbsTichTieuRuby.get(6);
                if (tichTieuTong != null) {
                    tichTieuTong.showTable(p);
                } else {
                    p.getService().send_box_ThongBao_OK("Chức năng Tích tiêu tổng đang bảo trì!");
                }
                break;
        }
    }
}

