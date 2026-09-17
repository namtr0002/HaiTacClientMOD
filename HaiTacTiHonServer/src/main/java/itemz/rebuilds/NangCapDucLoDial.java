package itemz.rebuilds;

import zabstracts.AbsUpgrade;
import model.Player;
import template.Item_wear;
import itemz.Rebuild_Item;
import network.Message;
import model.YesNoDialog;
import java.io.IOException;

public class NangCapDucLoDial extends AbsUpgrade {

    private static NangCapDucLoDial instance;

    public NangCapDucLoDial() {
        instance = this;
        AbsUpgrade.register(this);
    }

    public static NangCapDucLoDial getInstance() {
        if (instance == null) {
            instance = new NangCapDucLoDial();
        }
        return instance;
    }

    public static NangCapDucLoDial gI() {
        return getInstance();
    }

    @Override
    public byte getType() {
        return 19;
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
        m.writer().writeByte(19);
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
        if (cat == 3 && num == 1 && type == 19 && action == 1) { // bo dial vao de duc lo
            if (idItem < 0 || idItem >= p.item.bag3.length) {
                return;
            }
            Item_wear it_select = p.item.bag3[idItem];
            if (it_select != null && it_select.template != null && it_select.template.typeEquip == 7 && it_select.numLoKham < 5) {
                p.item_to_kham_ngoc = it_select;
                p.data_yesno = new int[]{idItem};
                p.getService().sendRebuildPutItem(idItem, (byte) 3, (short) 1);
            }
        } else if (cat == 4 && num == 1 && type == 19 && action == 1) { // bo bua duc dial vao
            if (idItem == 457) {
                p.getService().sendRebuildPutItem(idItem, (byte) 4, (short) 1);
            }
        } else if (type == 19 && (action == 7 || action == 20 || action == 24 || action == 15)) { // bat dau duc lo dial
            Item_wear it_select = (idItem >= 0 && idItem < p.item.bag3.length && p.item.bag3[idItem] != null) 
                    ? p.item.bag3[idItem] : p.item_to_kham_ngoc;
            if (it_select == null && p.data_yesno != null && p.data_yesno.length > 0 && p.data_yesno[0] >= 0 && p.data_yesno[0] < p.item.bag3.length) {
                it_select = p.item.bag3[p.data_yesno[0]];
            }
            if (it_select != null && it_select.template != null && it_select.template.typeEquip == 7) {
                short targetIdItem = (idItem >= 0 && idItem < p.item.bag3.length && p.item.bag3[idItem] != null) ? idItem : (p.data_yesno != null && p.data_yesno.length > 0 ? (short) p.data_yesno[0] : (short) -1);
                if (targetIdItem < 0) {
                    for (short i = 0; i < p.item.bag3.length; i++) {
                        if (p.item.bag3[i] == it_select) {
                            targetIdItem = i;
                            break;
                        }
                    }
                }
                if (it_select.numLoKham >= 5) {
                    Rebuild_Item.show_table(p, 10);
                    p.getService().send_box_ThongBao_OK("Dial này đã đục tối đa 5 lỗ khảm!");
                    return;
                }
                if (it_select.valueChetac < 50) {
                    Rebuild_Item.show_table(p, 10);
                    p.getService().send_box_ThongBao_OK("Vật phẩm không đủ điểm chế tác để thực hiện, tối thiểu 50!");
                    return;
                }
                if (p.item.total_item_bag_by_id(4, 457) < 1) {
                    Rebuild_Item.show_table(p, 10);
                    p.getService().send_box_ThongBao_OK("Không đủ 1 búa đục dial (ID 457)");
                    return;
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

                YesNoDialog ynd = new YesNoDialog(p, 57, "Auto Đục Lỗ Dial",
                        "Chọn số lần Auto Đục Lỗ cho " + it_select.template.name + " (Số lỗ hiện tại: " + it_select.numLoKham + "/5, Điểm chế tác: " + it_select.valueChetac + "/100):",
                        countOptions, countTypes, value -> {
                            if (isTest && value == 7) {
                                new model.InputDialog(p, 57, "Nhập số lần", new String[]{"Số lần muốn đục lỗ:"}, inputs -> {
                                    if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                    try {
                                        int count = Integer.parseInt(inputs[0].trim());
                                        if (count <= 0) {
                                            p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                            return;
                                        }
                                        executeAutoDucLoDial(p, finalTargetId, count);
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
                                executeAutoDucLoDial(p, finalTargetId, count);
                            }
                        });
                ynd.startYesNo();
            } else {
                Rebuild_Item.show_table(p, 10);
                p.getService().send_box_ThongBao_OK("Hãy chọn Dial (Trang bị Type 7) để đục lỗ!");
            }
        }
    }

    public static void executeAutoDucLoDial(Player p, short idItem, int loopCount) {
        if (p == null || idItem < 0 || idItem >= p.item.bag3.length) return;
        if (p.trade_target != null) {
            try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            return;
        }
        Item_wear it_select = p.item.bag3[idItem];
        if (it_select == null || it_select.template == null || it_select.template.typeEquip != 7) return;

        int startHoles = it_select.numLoKham;
        int successCount = 0;
        int totalHammersSpent = 0;
        int totalRubySpent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần đục lỗ";

        synchronized (p.item) {
            for (int step = 0; step < loopCount; step++) {
                if (it_select.numLoKham >= 5) {
                    stopReason = "Dial đã đạt tối đa 5 lỗ khảm!";
                    break;
                }
                if (it_select.valueChetac < 50) {
                    stopReason = "Điểm chế tác giảm dưới 50!";
                    break;
                }
                if (p.item.total_item_bag_by_id(4, 457) < 1) {
                    stopReason = "Hết búa đục dial!";
                    break;
                }
                int ruby_req = 50 * (it_select.numLoKham + 1);
                if (p.get_ngoc() < ruby_req) {
                    stopReason = "Không đủ " + ruby_req + " Ruby!";
                    break;
                }

                p.update_ngoc(-ruby_req);
                totalRubySpent += ruby_req;
                p.item.remove_item47(4, 457, 1);
                totalHammersSpent++;

                if (it_select.numHoleDaDuc < 0) {
                    it_select.numHoleDaDuc = 0;
                }
                it_select.numLoKham++;
                it_select.numHoleDaDuc++;
                successCount++;
            }

            try {
                p.updateMoney();
                p.item.updateInventory(false);
                p.setAbility();
                p.update_info_to_all();
            } catch (Exception ignored) {}
        }

        try {
            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO ĐỤC LỖ DIAL:\n" +
                "- Vật phẩm: " + it_select.template.name + "\n" +
                "- Số lỗ khảm: " + startHoles + " -> " + it_select.numLoKham + " lỗ\n" +
                "- Đục thành công: " + successCount + " lỗ\n" +
                "- Tiêu hao: " + totalHammersSpent + " Búa đục dial, " + totalRubySpent + " Ruby\n" +
                "- Trạng thái: " + stopReason
            );
            try {
                itemz.Rebuild_Item.show_table(p, 10);
            } catch (Exception ignored) {}
        } catch (Exception ignored) {}
    }
}
