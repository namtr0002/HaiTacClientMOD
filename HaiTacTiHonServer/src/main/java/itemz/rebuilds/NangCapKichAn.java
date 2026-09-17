package itemz.rebuilds;

import zabstracts.AbsUpgrade;
import model.Player;
import template.Item_wear;
import itemz.Rebuild_Item;
import network.Message;
import model.YesNoDialog;
import template.ItemTemplate4;
import core.ZUtil;
import java.io.IOException;

public class NangCapKichAn extends AbsUpgrade {

    public static final String[] KICH_AN_NAMES = {
        "Bất tử",                  // 0 (Ẩn 1)
        "Lời cảm ơn",              // 1 (Ẩn 2)
        "Lá chắn",                 // 2 (Ẩn 3)
        "Khóa năng lượng",          // 3 (Ẩn 4)
        "Bộc phá",                 // 4 (Ẩn 5)
        "Tập trung cao độ",        // 5 (Ẩn 6)
        "Ma cà rồng",              // 6 (Ẩn 7)
        "Đánh là choáng",          // 7 (Ẩn 8)
        "Thanh lọc",               // 8 (Ẩn 9)
        "Nén đau",                 // 9 (Ẩn 10)
        "Giải phóng năng lượng",   // 10 (Ẩn 11)
        "Người bất tử",            // 11 (Ẩn 12)
        "Mở khóa"                  // 12 (Ẩn 13)
    };

    public static String getKichAnName(int id) {
        if (id >= 0 && id < KICH_AN_NAMES.length) {
            return KICH_AN_NAMES[id];
        }
        return "Không rõ";
    }

    private static NangCapKichAn instance;

    public NangCapKichAn() {
        instance = this;
        AbsUpgrade.register(this);
    }

    public static NangCapKichAn getInstance() {
        if (instance == null) {
            instance = new NangCapKichAn();
        }
        return instance;
    }

    public static NangCapKichAn gI() {
        return getInstance();
    }

    @Override
    public byte getType() {
        return 11;
    }

    @Override
    public void showTable(Player p) throws IOException {
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        p.setUpgrade(this);
        Message m = new Message(-67);
        m.writer().writeByte(0);
        m.writer().writeByte(11);
        p.addmsg(m);
        m.cleanup();
        p.item_to_kham_ngoc = null;
        p.item_to_kham_ngoc_id_ngoc = -1;
        p.data_yesno = null;
    }

    @Override
    public void process(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException {
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        if (type == 11 && action == 1 && cat == 3 && num == 1) { // bo item kich an
            if (idItem < 0 || idItem >= p.item.bag3.length) {
                return;
            }
            Item_wear it_select = p.item.bag3[idItem];
            if (it_select != null) {
                if (it_select.isThanTrang()) {
                    p.getService().send_box_ThongBao_OK("Trang bị Thần Trang (Type 8-15) không thể Kích Ẩn!");
                    return;
                }
                if (it_select.template.typeEquip < 6) {
                    if ((it_select.getColor() >= 2) && it_select.valueChetac >= 50) {
                        p.getService().sendRebuildPutItem(idItem, (byte) 3, (short) 1);
                        p.item_to_kham_ngoc = it_select;
                    } else {
                        p.getService().send_box_ThongBao_OK("Trang bị đem đi kích ẩn phải là trang bị tím hoặc cam và có điểm chế tác >= 50");
                    }
                }
            }
        } else if (type == 11 && action == 1 && cat == 4 && idItem >= 221 && idItem <= 226 && num == 1) { // bo material kich an
            if (p.item.total_item_bag_by_id(4, idItem) > 0) {
                p.getService().sendRebuildPutItem(idItem, (byte) 4, num);
                p.item_to_kham_ngoc_id_ngoc = idItem;
            } else {
                p.getService().send_box_ThongBao_OK("Không đủ " + ItemTemplate4.get_item_name(idItem));
            }
        } else if (type == 11 && action == 22 && cat == 0 && idItem == 0 && num == 0) { // bat dau kich an
            if (p.item_to_kham_ngoc != null && p.item_to_kham_ngoc_id_ngoc >= 221
                    && p.item_to_kham_ngoc_id_ngoc <= 226) {
                Item_wear it_select = p.item_to_kham_ngoc;
                if (it_select.valueChetac < 50) {
                    p.getService().send_box_ThongBao_OK("Trang bị đem đi kích ẩn phải có điểm chế tác >= 50!");
                    return;
                }

                boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
                if (!isTest) {
                    // Khi OPEN: Không cho auto gì hết, chỉ kích ẩn 1 lần thủ công
                    YesNoDialog yndConfirm = new YesNoDialog(p, 11, "Kích Ẩn Trang Bị",
                        "Bạn có muốn kích ẩn trang bị " + it_select.template.name + " không?\n(Chi phí: 1x " + ItemTemplate4.get_item_name(p.item_to_kham_ngoc_id_ngoc) + " + 5 Ruby)",
                        new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}, val -> {
                            if (val == 0) {
                                executeAutoKichAn(p, -1, 1);
                            }
                        });
                    yndConfirm.startYesNo();
                    return;
                }

                String[] typeOptions = new String[]{
                    "Bất kỳ (Ra ẩn là dừng)",
                    "Ẩn 1: Bất tử",
                    "Ẩn 2: Lời cảm ơn",
                    "Ẩn 3: Lá chắn",
                    "Ẩn 4: Khóa năng lượng",
                    "Ẩn 5: Bộc phá",
                    "Ẩn 6: Tập trung cao độ",
                    "Ẩn 7: Ma cà rồng",
                    "Ẩn 8: Đánh là choáng",
                    "Ẩn 9: Thanh lọc",
                    "Ẩn 10: Nén đau",
                    "Ẩn 11: Giải phóng NL",
                    "Ẩn 12: Người bất tử",
                    "Ẩn 13: Mở khóa",
                    "Đóng"
                };
                byte[] typeIcons = new byte[typeOptions.length];
                for (int i = 0; i < typeIcons.length; i++) {
                    typeIcons[i] = (byte) (i == typeIcons.length - 1 ? 1 : -1);
                }

                YesNoDialog yndType = new YesNoDialog(p, 32, "Chọn Loại Kích Ẩn",
                    "Chọn loại kích ẩn mục tiêu bạn muốn Auto (Tự dừng khi ra đúng loại đã chọn hoặc ra bất kỳ loại nào nếu chọn Bất kỳ):",
                    typeOptions, typeIcons, typeValue -> {
                        if (typeValue == 14 || typeValue < 0 || typeValue >= typeOptions.length) {
                            return; // Đóng / Hủy
                        }
                        int targetKichAn = typeValue == 0 ? -1 : (typeValue - 1);
                        showSelectCountMenu(p, targetKichAn);
                    });
                yndType.startYesNo();
            } else {
                p.getService().send_box_ThongBao_OK("Vui lòng đặt trang bị và ngọc hải thạch vào bàn ghép trước!");
            }
        }
    }

    public static void showSelectCountMenu(Player p, int targetKichAn) {
        if (p == null || p.item_to_kham_ngoc == null || p.item_to_kham_ngoc_id_ngoc < 221 || p.item_to_kham_ngoc_id_ngoc > 226) {
            try { p.getService().send_box_ThongBao_OK("Vui lòng đặt trang bị và ngọc hải thạch vào bàn ghép trước!"); } catch (Exception ignored) {}
            return;
        }
        if (p.trade_target != null) {
            try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            return;
        }
        String targetTitle = targetKichAn == -1 ? "Mục tiêu: Bất kỳ loại ẩn" : ("Mục tiêu: Ẩn " + (targetKichAn + 1) + " - " + getKichAnName(targetKichAn));
        YesNoDialog yndCount = new YesNoDialog(p, 33, targetTitle,
            "Chọn số lần Auto Kích Ẩn (Tự dừng khi ra đúng loại hoặc hết ruby/ngọc/chế tác < 50):",
            new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lượng", "Đóng"},
            new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, 1}, value -> {
                if (value == 7) {
                    new model.InputDialog(p, 32, "Nhập số lần", new String[]{"Số lần muốn kích ẩn (1-1000):"}, inputs -> {
                        if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                        try {
                            int count = Integer.parseInt(inputs[0].trim());
                            if (count <= 0) {
                                p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                return;
                            }
                            if (count > 1000) {
                                count = 1000;
                            }
                            executeAutoKichAn(p, targetKichAn, count);
                        } catch (NumberFormatException e) {
                            p.getService().send_box_ThongBao_OK("Vui lòng nhập số hợp lệ!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).startInput();
                    return;
                }
                int count = switch (value) {
                    case 0 -> 1;
                    case 1 -> 10;
                    case 2 -> 20;
                    case 3 -> 50;
                    case 4 -> 100;
                    case 5 -> 200;
                    case 6 -> 500;
                    default -> 0;
                };
                if (count > 0) {
                    executeAutoKichAn(p, targetKichAn, count);
                }
            });
        yndCount.startYesNo();
    }

    public static void executeAutoKichAn(Player p, int targetKichAn, int loopCount) {
        if (p == null || p.item_to_kham_ngoc == null || p.item_to_kham_ngoc_id_ngoc < 221 || p.item_to_kham_ngoc_id_ngoc > 226) {
            try { p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, vui lòng đặt lại trang bị và ngọc!"); } catch (Exception ignored) {}
            return;
        }
        if (p.trade_target != null) {
            try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            return;
        }

        Item_wear it_select = p.item_to_kham_ngoc;
        boolean found = false;
        if (p.item.bag3 != null) {
            for (Item_wear it : p.item.bag3) {
                if (it == it_select) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            p.item_to_kham_ngoc = null;
            p.item_to_kham_ngoc_id_ngoc = -1;
            try { p.getService().send_box_ThongBao_OK("Vật phẩm không còn trong hành trang!"); } catch (Exception ignored) {}
            return;
        }

        int startChetac = it_select.valueChetac;
        int successCount = 0;
        int failCount = 0;
        int totalRubySpent = 0;
        int totalGemsSpent = 0;
        int totalRuns = 0;
        String targetName = targetKichAn == -1 ? "Bất kỳ" : ("Ẩn " + (targetKichAn + 1) + " (" + getKichAnName(targetKichAn) + ")");
        String stopReason = "Đã hoàn thành " + loopCount + " lần kích ẩn";

        synchronized (p.item) {
            for (int step = 0; step < loopCount; step++) {
                if (it_select.valueChetac < 50) {
                    stopReason = "Điểm chế tác giảm dưới 50!";
                    break;
                }
                if (p.item.total_item_bag_by_id(4, p.item_to_kham_ngoc_id_ngoc) < 1) {
                    stopReason = "Hết " + ItemTemplate4.get_item_name(p.item_to_kham_ngoc_id_ngoc) + "!";
                    break;
                }
                if (p.get_ngoc() < 5) {
                    stopReason = "Không đủ 5 Ruby!";
                    break;
                }

                p.item.remove_item47(4, p.item_to_kham_ngoc_id_ngoc, 1);
                p.update_ngoc(-5);
                totalRubySpent += 5;
                totalGemsSpent += 1;
                totalRuns++;

                // Tỷ lệ kích ẩn theo loại Ngọc Hải Thạch (random range 120):
                // id=221: 4/120 ≈ 3.3%  | id=222: 8/120 ≈ 6.7%  | id=223: 13/120 ≈ 10.8%
                // id=224: 18/120 = 15%   | id=225: 22/120 ≈ 18.3% | id=226: 30/120 = 25%
                int kichAnChance = switch (p.item_to_kham_ngoc_id_ngoc) {
                    case 221 -> 4;
                    case 222 -> 8;
                    case 223 -> 13;
                    case 224 -> 18;
                    case 225 -> 22;
                    case 226 -> 30;
                    default  -> 0;
                };
                boolean suc = kichAnChance > ZUtil.random(120);
                if (suc) {
                    byte rolled = ZUtil.rollKichAn();
                    it_select.valueKichAn = rolled;
                    if (it_select.valueKichAn == 12) {
                        it_select.typelock = -1;
                    }
                    successCount++;

                    if (targetKichAn == -1) {
                        stopReason = "Kích ẩn thành công Ẩn " + (rolled + 1) + " (" + getKichAnName(rolled) + ")!";
                        break;
                    } else if (rolled == targetKichAn) {
                        stopReason = "Chúc mừng bạn đã kích ẩn trúng đúng mục tiêu Ẩn " + (rolled + 1) + " (" + getKichAnName(rolled) + ")!";
                        break;
                    }
                    // Nếu chưa trúng loại mục tiêu, tiếp tục vòng lặp
                } else {
                    failCount++;
                    int reduce_chetac = switch (p.item_to_kham_ngoc_id_ngoc) {
                        case 226 -> ZUtil.random(1, 6);
                        case 225 -> ZUtil.random(14, 23);
                        case 224 -> ZUtil.random(16, 25);
                        case 223 -> ZUtil.random(18, 27);
                        case 222 -> ZUtil.random(20, 29);
                        default  -> ZUtil.random(22, 31);
                    };
                    it_select.valueChetac -= reduce_chetac;
                    if (it_select.valueChetac <= 0) {
                        it_select.valueChetac = 0;
                        stopReason = "Điểm chế tác về 0, không thể tiếp tục!";
                        break;
                    }
                }
            }

            try {
                p.updateMoney();
                p.item.updateInventory(false);
                p.setAbility();
                p.update_info_to_all();
            } catch (Exception ignored) {}
        }

        p.item_to_kham_ngoc_id_ngoc = -1;
        p.data_yesno = null;

        try {
            String currentKichAnStr = (it_select.valueKichAn >= 0 && it_select.valueKichAn <= 12)
                ? ("Ẩn " + (it_select.valueKichAn + 1) + " (" + getKichAnName(it_select.valueKichAn) + ")")
                : "Chưa kích ẩn";

            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO KÍCH ẨN\n" +
                "• Trang bị: " + it_select.template.name + "\n" +
                "• Mục tiêu: " + targetName + "\n" +
                "• Ẩn hiện tại: " + currentKichAnStr + "\n" +
                "• Điểm chế tác: " + startChetac + " -> " + it_select.valueChetac + "\n" +
                "• Đã chạy: " + totalRuns + " lần (Thành công: " + successCount + " | Thất bại: " + failCount + ")\n" +
                "• Tiêu hao: " + totalGemsSpent + " " + ItemTemplate4.get_item_name(p.item_to_kham_ngoc_id_ngoc) + " | " + totalRubySpent + " Ruby\n" +
                "• Trạng thái: " + stopReason
            );
        } catch (Exception ignored) {}
    }
}
