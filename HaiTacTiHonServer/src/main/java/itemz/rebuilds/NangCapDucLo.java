package itemz.rebuilds;

import zabstracts.AbsUpgrade;
import model.Player;
import template.Item_wear;
import itemz.Rebuild_Item;
import network.Message;
import model.YesNoDialog;
import network.Service;
import core.ZUtil;
import java.io.IOException;

public class NangCapDucLo extends AbsUpgrade {

    private static NangCapDucLo instance;

    public NangCapDucLo() {
        instance = this;
        AbsUpgrade.register(this);
    }

    public static NangCapDucLo getInstance() {
        if (instance == null) {
            instance = new NangCapDucLo();
        }
        return instance;
    }

    public static NangCapDucLo gI() {
        return getInstance();
    }

    @Override
    public byte getType() {
        return 2;
    }

    @Override
    public void showTable(Player p) throws IOException {
        p.setUpgrade(this);
        Message m = new Message(-67);
        m.writer().writeByte(0);
        m.writer().writeByte(2);
        p.addmsg(m);
        m.cleanup();
        p.item_to_kham_ngoc = null;
        p.item_to_kham_ngoc_id_ngoc = -1;
        p.data_yesno = null;
    }

    @Override
    public void process(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException {
        if (cat == 3 && num == 1 && type == 2 && action == 1) { // bo item duc lo
            Item_wear it_select = p.item.bag3[idItem];
            if (it_select != null) {
                if (it_select.isThanTrang() || (it_select.template != null && it_select.template.isThanTrang())) {
                    p.getService().send_box_ThongBao_OK("Trang bị Thần Trang không có lỗ và không thể đục lỗ!");
                    return;
                }
                if (it_select.template != null && (it_select.template.typeEquip == 6 || it_select.template.id == 11000)) {
                    p.getService().send_box_ThongBao_OK("Vật phẩm Quả Tim (Type 6) không có lỗ và không thể đục lỗ!");
                    return;
                }
                if (it_select.template != null && it_select.template.typeEquip < 6) {
                    p.item_to_kham_ngoc = it_select;
                    p.data_yesno = new int[]{idItem};
                    p.getService().sendRebuildPutItem(idItem, (byte) 3, (short) 1);
                }
            }
        } else if (cat == 3 && num == 1 && type == 2 && action == 7) { // duc lo
            Item_wear it_select = (idItem >= 0 && idItem < p.item.bag3.length && p.item.bag3[idItem] != null) 
                    ? p.item.bag3[idItem] : p.item_to_kham_ngoc;
            if (it_select != null && (it_select.isThanTrang() || (it_select.template != null && it_select.template.isThanTrang()))) {
                Rebuild_Item.show_table(p, 2);
                p.getService().send_box_ThongBao_OK("Trang bị Thần Trang không có lỗ và không thể đục lỗ!");
                return;
            }
            if (it_select != null && it_select.template != null && (it_select.template.typeEquip == 6 || it_select.template.id == 11000)) {
                Rebuild_Item.show_table(p, 2);
                p.getService().send_box_ThongBao_OK("Vật phẩm Quả Tim (Type 6) không có lỗ và không thể đục lỗ!");
                return;
            }
            if (it_select != null && it_select.template != null && it_select.numLoKham < 4
                    && it_select.template.typeEquip < 6 && !it_select.isThanTrang()) {
                short targetIdItem = (idItem >= 0 && idItem < p.item.bag3.length && p.item.bag3[idItem] != null) ? idItem : (p.data_yesno != null && p.data_yesno.length > 0 ? (short) p.data_yesno[0] : (short) -1);
                if (targetIdItem < 0) {
                    for (short i = 0; i < p.item.bag3.length; i++) {
                        if (p.item.bag3[i] == it_select) {
                            targetIdItem = i;
                            break;
                        }
                    }
                }
                p.data_yesno = new int[]{targetIdItem};
                int ruby_req = (it_select.numHoleDaDuc >= 1 ? 200 : 50);
                final Item_wear finalSelect = it_select;
                YesNoDialog ynd = new YesNoDialog(p, 12, "Thông báo",
                        "Đục lỗ bạn phải mất " + ruby_req + " Ruby",
                        new String[]{"Đồng ý", "Hủy"}, new byte[]{7, -1}, value -> {
                            if (value == 0) {
                                if (p.trade_target != null) {
                                    p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                                    return;
                                }
                                synchronized (p.item) {
                                    boolean exists = false;
                                    for (Item_wear it : p.item.bag3) {
                                        if (it != null && it == finalSelect) {
                                            exists = true;
                                            break;
                                        }
                                    }
                                    if (!exists) {
                                        p.getService().send_box_ThongBao_OK("Vật phẩm không còn trong hành trang!");
                                        return;
                                    }
                                    int cost = (finalSelect.numHoleDaDuc >= 1 ? 200 : 50);
                                    if (p.get_ngoc() < cost) {
                                        p.getService().send_box_ThongBao_OK("Bạn không đủ " + cost + " Ruby");
                                        return;
                                    }
                                    p.update_ngoc(-cost);
                                    if (finalSelect.numHoleDaDuc < 0) {
                                        finalSelect.numHoleDaDuc = 0;
                                    }
                                    finalSelect.numHoleDaDuc++;
                                    finalSelect.numLoKham++;
                                    p.updateMoney();
                                    p.getService().sendRebuildActionMsg((byte) 7, "Đục lỗ thành công " + finalSelect.template.name);
                                    p.item.updateInventory(false);
                                }
                            }
                        });
                ynd.startYesNo();
            } else {
                Rebuild_Item.show_table(p, 2);
                p.getService().send_box_ThongBao_OK("Không thể đục thêm lỗ với vật phẩm này, hãy sử dụng búa đục lỗ để có thể tiếp tục");
            }
        } else if (cat == 3 && num == 1 && type == 2 && action == 34) { // duc lo = bua sieu cap
            Item_wear it_select = (idItem >= 0 && idItem < p.item.bag3.length && p.item.bag3[idItem] != null) 
                    ? p.item.bag3[idItem] : p.item_to_kham_ngoc;
            if (it_select != null && (it_select.isThanTrang() || (it_select.template != null && it_select.template.isThanTrang()))) {
                Rebuild_Item.show_table(p, 2);
                p.getService().send_box_ThongBao_OK("Trang bị Thần Trang không có lỗ và không thể đục lỗ!");
                return;
            }
            if (it_select != null && it_select.template != null && (it_select.template.typeEquip == 6 || it_select.template.id == 11000)) {
                Rebuild_Item.show_table(p, 2);
                p.getService().send_box_ThongBao_OK("Vật phẩm Quả Tim (Type 6) không có lỗ và không thể đục lỗ!");
                return;
            }
            if (it_select != null && it_select.template != null && it_select.template.typeEquip < 6 && !it_select.isThanTrang()) {
                if (it_select.numLoKham >= 6) {
                    Rebuild_Item.show_table(p, 2);
                    p.getService().send_box_ThongBao_OK("Trang bị đã đạt tối đa 6 lỗ khảm!");
                    return;
                }
                if (it_select.valueChetac < 50) {
                    Rebuild_Item.show_table(p, 2);
                    p.getService().send_box_ThongBao_OK("Vật phẩm không đủ điểm chế tác để thực hiện, tối thiểu 50!");
                    return;
                }
                if (p.item.total_item_bag_by_id(4, 323) < 1) {
                    Rebuild_Item.show_table(p, 2);
                    p.getService().send_box_ThongBao_OK("Không đủ 1 búa siêu cấp");
                    return;
                }
                short targetIdItem = (idItem >= 0 && idItem < p.item.bag3.length && p.item.bag3[idItem] != null) ? idItem : (p.data_yesno != null && p.data_yesno.length > 0 ? (short) p.data_yesno[0] : (short) -1);
                if (targetIdItem < 0) {
                    for (short i = 0; i < p.item.bag3.length; i++) {
                        if (p.item.bag3[i] == it_select) {
                            targetIdItem = i;
                            break;
                        }
                    }
                }
                p.data_yesno = new int[]{targetIdItem};
                final short finalTargetId = targetIdItem;
                boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
                String[] countOptions = isTest
                        ? new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"}
                        : new String[]{"1 lần", "5 lần", "10 lần", "Đóng"};
                byte[] countTypes = isTest
                        ? new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, 1}
                        : new byte[]{-1, -1, -1, 1};

                YesNoDialog ynd = new YesNoDialog(p, 46, "Auto Đục Lỗ Búa Siêu Cấp",
                        "Chọn số lần Auto Đục Lỗ " + it_select.template.name + " (Hiện tại: " + it_select.numLoKham + "/6 lỗ):",
                        countOptions, countTypes, value -> {
                            if (isTest && value == 7) {
                                new model.InputDialog(p, 46, "Nhập số lần", new String[]{"Số lần muốn đục lỗ:"}, inputs -> {
                                    if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                    try {
                                        int count = Integer.parseInt(inputs[0].trim());
                                        if (count <= 0) {
                                            p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                            return;
                                        }
                                        executeAutoDucLoBuaSieuCap(p, finalTargetId, count);
                                    } catch (NumberFormatException e) {
                                        p.getService().send_box_ThongBao_OK("Vui lòng nhập số hợp lệ!");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }).startInput();
                                return;
                            }
                            int count = 0;
                            if (!isTest) {
                                count = switch (value) {
                                    case 0 -> 1;
                                    case 1 -> 5;
                                    case 2 -> 10;
                                    default -> 0;
                                };
                            } else {
                                count = switch (value) {
                                    case 0 -> 1;
                                    case 1 -> 10;
                                    case 2 -> 20;
                                    case 3 -> 50;
                                    case 4 -> 100;
                                    case 5 -> 200;
                                    case 6 -> 500;
                                    default -> 0;
                                };
                            }
                            if (count > 0) {
                                executeAutoDucLoBuaSieuCap(p, finalTargetId, count);
                            }
                        });
                ynd.startYesNo();
            } else {
                Rebuild_Item.show_table(p, 2);
                p.getService().send_box_ThongBao_OK("Không thể dùng búa siêu cấp với vật phẩm này");
            }
        } else if (cat == 3 && num == 1 && type == 2 && action == 33) { // duc lo = bua so cap
            Item_wear it_select = (idItem >= 0 && idItem < p.item.bag3.length && p.item.bag3[idItem] != null) 
                    ? p.item.bag3[idItem] : p.item_to_kham_ngoc;
            if (it_select != null && (it_select.isThanTrang() || (it_select.template != null && it_select.template.isThanTrang()))) {
                Rebuild_Item.show_table(p, 2);
                p.getService().send_box_ThongBao_OK("Trang bị Thần Trang không có lỗ và không thể đục lỗ!");
                return;
            }
            if (it_select != null && it_select.template != null && (it_select.template.typeEquip == 6 || it_select.template.id == 11000)) {
                Rebuild_Item.show_table(p, 2);
                p.getService().send_box_ThongBao_OK("Vật phẩm Quả Tim (Type 6) không có lỗ và không thể đục lỗ!");
                return;
            }
            if (it_select != null && it_select.template != null && it_select.template.typeEquip < 6 && !it_select.isThanTrang()) {
                if (it_select.numLoKham >= 5) {
                    Rebuild_Item.show_table(p, 2);
                    p.getService().send_box_ThongBao_OK("Búa sơ cấp chỉ có thể đục lỗ vật phẩm tối đa 5 lỗ!");
                    return;
                }
                if (it_select.valueChetac < 50) {
                    Rebuild_Item.show_table(p, 2);
                    p.getService().send_box_ThongBao_OK("Vật phẩm không đủ điểm chế tác để thực hiện, tối thiểu 50!");
                    return;
                }
                if (p.item.total_item_bag_by_id(4, 339) < 1) {
                    Rebuild_Item.show_table(p, 2);
                    p.getService().send_box_ThongBao_OK("Không đủ 1 búa sơ cấp");
                    return;
                }
                short targetIdItem = (idItem >= 0 && idItem < p.item.bag3.length && p.item.bag3[idItem] != null) ? idItem : (p.data_yesno != null && p.data_yesno.length > 0 ? (short) p.data_yesno[0] : (short) -1);
                if (targetIdItem < 0) {
                    for (short i = 0; i < p.item.bag3.length; i++) {
                        if (p.item.bag3[i] == it_select) {
                            targetIdItem = i;
                            break;
                        }
                    }
                }
                p.data_yesno = new int[]{targetIdItem};
                final short finalTargetId = targetIdItem;
                boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
                String[] countOptions = isTest
                        ? new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"}
                        : new String[]{"1 lần", "5 lần", "10 lần", "Đóng"};
                byte[] countTypes = isTest
                        ? new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, 1}
                        : new byte[]{-1, -1, -1, 1};

                YesNoDialog ynd = new YesNoDialog(p, 45, "Auto Đục Lỗ Búa Sơ Cấp",
                        "Chọn số lần Auto Đục Lỗ " + it_select.template.name + " (Hiện tại: " + it_select.numLoKham + "/5 lỗ, Tỉ lệ 10/150, dừng khi lên 5 lỗ hoặc chế tác < 50):",
                        countOptions, countTypes, value -> {
                            if (isTest && value == 7) {
                                new model.InputDialog(p, 45, "Nhập số lần", new String[]{"Số lần muốn đục lỗ:"}, inputs -> {
                                    if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                    try {
                                        int count = Integer.parseInt(inputs[0].trim());
                                        if (count <= 0) {
                                            p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                            return;
                                        }
                                        executeAutoDucLoBuaSoCap(p, finalTargetId, count);
                                    } catch (NumberFormatException e) {
                                        p.getService().send_box_ThongBao_OK("Vui lòng nhập số hợp lệ!");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }).startInput();
                                return;
                            }
                            int count = 0;
                            if (!isTest) {
                                count = switch (value) {
                                    case 0 -> 1;
                                    case 1 -> 5;
                                    case 2 -> 10;
                                    default -> 0;
                                };
                            } else {
                                count = switch (value) {
                                    case 0 -> 1;
                                    case 1 -> 10;
                                    case 2 -> 20;
                                    case 3 -> 50;
                                    case 4 -> 100;
                                    case 5 -> 200;
                                    case 6 -> 500;
                                    default -> 0;
                                };
                            }
                            if (count > 0) {
                                executeAutoDucLoBuaSoCap(p, finalTargetId, count);
                            }
                        });
                ynd.startYesNo();
            } else {
                Rebuild_Item.show_table(p, 2);
                p.getService().send_box_ThongBao_OK("Không thể dùng búa sơ cấp với vật phẩm này");
            }
        }
    }

    public static void executeAutoDucLoBuaSieuCap(Player p, short idItem, int loopCount) {
        if (p == null || p.item == null || p.trade_target != null || idItem < 0 || idItem >= p.item.bag3.length) {
            if (p != null && p.trade_target != null) {
                try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            }
            return;
        }
        Item_wear it_select = p.item.bag3[idItem];
        if (it_select == null || it_select.template == null) return;
        if (it_select.isThanTrang() || it_select.template.isThanTrang() || it_select.template.typeEquip == 6 || it_select.template.id == 11000) {
            try { p.getService().send_box_ThongBao_OK("Vật phẩm Quả Tim và Thần Trang không thể đục lỗ!"); } catch (Exception ignored) {}
            return;
        }

        int startHoles = it_select.numLoKham;
        int successCount = 0;
        int totalHammersSpent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần đục lỗ";

        synchronized (p.item) {
            for (int step = 0; step < loopCount; step++) {
                if (it_select.numLoKham >= 6) {
                    stopReason = "Trang bị đã đạt tối đa 6 lỗ khảm!";
                    break;
                }
                if (it_select.valueChetac < 50) {
                    stopReason = "Điểm chế tác giảm dưới 50!";
                    break;
                }
                if (p.item.total_item_bag_by_id(4, 323) < 1) {
                    stopReason = "Hết búa siêu cấp!";
                    break;
                }

                p.item.remove_item47(4, 323, 1);
                totalHammersSpent++;

                if (it_select.numHoleDaDuc < 0) {
                    it_select.numHoleDaDuc = 0;
                }
                it_select.numLoKham++;
                it_select.numHoleDaDuc++;
                successCount++;
            }
        }

        try {
            p.item.updateInventory(false);
            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO ĐỤC LỖ BÚA SIÊU CẤP:\n" +
                "- Trang bị: " + it_select.template.name + "\n" +
                "- Số lỗ khảm: " + startHoles + " -> " + it_select.numLoKham + " lỗ\n" +
                "- Đục thành công: " + successCount + " lỗ\n" +
                "- Tiêu hao: " + totalHammersSpent + " Búa Siêu Cấp\n" +
                "- Trạng thái: " + stopReason
            );
        } catch (Exception ignored) {}
    }

    public static void executeAutoDucLoBuaSoCap(Player p, short idItem, int loopCount) {
        if (p == null || p.item == null || p.trade_target != null || idItem < 0 || idItem >= p.item.bag3.length) {
            if (p != null && p.trade_target != null) {
                try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            }
            return;
        }
        Item_wear it_select = p.item.bag3[idItem];
        if (it_select == null || it_select.template == null) return;
        if (it_select.isThanTrang() || it_select.template.isThanTrang() || it_select.template.typeEquip == 6 || it_select.template.id == 11000) {
            try { p.getService().send_box_ThongBao_OK("Vật phẩm Quả Tim và Thần Trang không thể đục lỗ!"); } catch (Exception ignored) {}
            return;
        }

        int startChetac = it_select.valueChetac;
        int successCount = 0;
        int failCount = 0;
        int totalHammersSpent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần đục lỗ";

        synchronized (p.item) {
            for (int step = 0; step < loopCount; step++) {
                if (it_select.numLoKham >= 5) {
                    stopReason = "Đục lỗ thành công lên 5 lỗ!";
                    break;
                }
                if (it_select.valueChetac < 50) {
                    stopReason = "Điểm chế tác giảm dưới 50!";
                    break;
                }
                if (p.item.total_item_bag_by_id(4, 339) < 1) {
                    stopReason = "Hết búa sơ cấp!";
                    break;
                }

                p.item.remove_item47(4, 339, 1);
                totalHammersSpent++;

                boolean suc = 10 > ZUtil.random(150);
                if (suc) {
                    if (it_select.numHoleDaDuc < 0) {
                        it_select.numHoleDaDuc = 0;
                    }
                    it_select.numLoKham++;
                    it_select.numHoleDaDuc++;
                    successCount++;
                    stopReason = "Đục lỗ thành công lên 5 lỗ!";
                    break;
                } else {
                    failCount++;
                    it_select.valueChetac -= ZUtil.random(10, 20);
                    if (it_select.valueChetac < 0) {
                        it_select.valueChetac = 0;
                    }
                }
            }
        }

        try {
            p.item.updateInventory(false);
            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO ĐỤC LỖ BÚA SƠ CẤP:\n" +
                "- Trang bị: " + it_select.template.name + " (" + it_select.numLoKham + " lỗ)\n" +
                "- Điểm chế tác: " + startChetac + " -> " + it_select.valueChetac + "\n" +
                "- Thành công: " + successCount + " lần | Thất bại: " + failCount + " lần\n" +
                "- Tiêu hao: " + totalHammersSpent + " Búa Sơ Cấp\n" +
                "- Trạng thái: " + stopReason
            );
        } catch (Exception ignored) {}
    }
}
