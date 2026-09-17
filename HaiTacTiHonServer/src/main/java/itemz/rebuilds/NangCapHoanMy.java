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

public class NangCapHoanMy extends AbsUpgrade {

    private static NangCapHoanMy instance;

    public NangCapHoanMy() {
        instance = this;
        AbsUpgrade.register(this);
    }

    public static NangCapHoanMy getInstance() {
        if (instance == null) {
            instance = new NangCapHoanMy();
        }
        return instance;
    }

    public static NangCapHoanMy gI() {
        return getInstance();
    }

    @Override
    public byte getType() {
        return 10;
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
        m.writer().writeByte(10);
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
        if (type == 10 && action == 1 && cat == 3 && num == 1) { // bo item vao de hoan my
            if (idItem < 0 || idItem >= p.item.bag3.length) {
                return;
            }
            Item_wear it_select = p.item.bag3[idItem];
            if (it_select != null) {
                if (it_select.isThanTrang()) {
                    p.getService().send_box_ThongBao_OK("Trang bị Thần Trang (Type 8-15) không thể Hoàn Mỹ!");
                    return;
                }
                if (it_select.template.typeEquip < 6) {
                    if ((it_select.getColor() >= 2) && it_select.valueChetac >= 50) {
                        p.getService().sendRebuildPutItem(idItem, (byte) 3, (short) 1);
                        p.item_to_kham_ngoc = it_select;
                    } else {
                        p.getService().send_box_ThongBao_OK("Trang bị đem đi hoàn mỹ phải là trang bị tím hoặc cam và có điểm chế tác >= 50");
                    }
                }
            }
        } else if (type == 10 && action == 1 && cat == 4 && idItem >= 221 && idItem <= 226 && num == 1) { // bo material hoan my
            if (p.item.total_item_bag_by_id(4, idItem) > 0) {
                p.getService().sendRebuildPutItem(idItem, (byte) 4, num);
                p.item_to_kham_ngoc_id_ngoc = idItem;
            } else {
                p.getService().send_box_ThongBao_OK("Không đủ " + ItemTemplate4.get_item_name(idItem));
            }
        } else if (type == 10 && action == 20 && cat == 0 && idItem == 0 && num == 0) { // bat dau hoan my
            if (p.item_to_kham_ngoc != null && p.item_to_kham_ngoc_id_ngoc >= 221
                    && p.item_to_kham_ngoc_id_ngoc <= 226) {
                Item_wear it_select = p.item_to_kham_ngoc;
                if (it_select.isHoanMy == 1) {
                    p.getService().send_box_ThongBao_OK("Trang bị này đã được hoàn mỹ rồi!");
                    return;
                }
                if (it_select.valueChetac < 50) {
                    p.getService().send_box_ThongBao_OK("Trang bị đem đi hoàn mỹ phải có điểm chế tác >= 50!");
                    return;
                }
                boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
                String[] options = !isTest
                    ? new String[]{"1 lần", "5 lần", "10 lần", "Hủy"}
                    : new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Hủy"};
                byte[] icons = new byte[options.length];
                for (int i = 0; i < icons.length - 1; i++) icons[i] = -1;
                icons[icons.length - 1] = 1;

                YesNoDialog ynd = new YesNoDialog(p, 30, "Auto Hoàn Mỹ Trang Bị",
                    "Chọn số lần Auto Hoàn Mỹ (Dừng khi thành công hoặc hết ruby/ngọc/chế tác < 50):",
                    options, icons, value -> {
                        if (!isTest) {
                            int count = switch (value) {
                                case 0 -> 1;
                                case 1 -> 5;
                                case 2 -> 10;
                                default -> 0;
                            };
                            if (count > 0) {
                                executeAutoHoanMy(p, count);
                            }
                            return;
                        }
                        if (value == 7) {
                            new model.InputDialog(p, 30, "Nhập số lần", new String[]{"Số lần muốn hoàn mỹ:"}, inputs -> {
                                if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                try {
                                    int count = Integer.parseInt(inputs[0].trim());
                                    if (count <= 0) {
                                        p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                        return;
                                    }
                                    executeAutoHoanMy(p, count);
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
                            executeAutoHoanMy(p, count);
                        }
                    });
                ynd.startYesNo();
            } else {
                p.getService().send_box_ThongBao_OK("Vui lòng đặt trang bị và ngọc hải thạch vào bàn ghép trước!");
            }
        }
    }

    public static void executeAutoHoanMy(Player p, int loopCount) {
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

        if (it_select.isHoanMy == 1) {
            try { p.getService().send_box_ThongBao_OK("Trang bị này đã được hoàn mỹ rồi!"); } catch (Exception ignored) {}
            return;
        }

        int startChetac = it_select.valueChetac;
        int successCount = 0;
        int failCount = 0;
        int totalRubySpent = 0;
        int totalGemsSpent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần hoàn mỹ";

        synchronized (p.item) {
            for (int step = 0; step < loopCount; step++) {
                if (it_select.isHoanMy == 1) {
                    stopReason = "Chúc mừng bạn đã hoàn mỹ thành công!";
                    break;
                }
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

                int hoanMyChance = switch (p.item_to_kham_ngoc_id_ngoc) {
                    case 221 -> 3;
                    case 222 -> 6;
                    case 223 -> 10;
                    case 224 -> 15;
                    case 225 -> 20;
                    case 226 -> 25;
                    default  -> 0;
                };
                boolean suc = hoanMyChance > ZUtil.random(150);
                if (suc) {
                    it_select.isHoanMy = 1;
                    successCount++;
                    stopReason = "Chúc mừng bạn đã hoàn mỹ thành công " + it_select.template.name + "!";
                    break;
                } else {
                    failCount++;
                    int reduce_chetac;
                    switch (p.item_to_kham_ngoc_id_ngoc) {
                        case 226 -> reduce_chetac = ZUtil.random(1, 6);
                        case 225 -> reduce_chetac = ZUtil.random(14, 23);
                        case 224 -> reduce_chetac = ZUtil.random(16, 25);
                        case 223 -> reduce_chetac = ZUtil.random(18, 27);
                        case 222 -> reduce_chetac = ZUtil.random(20, 29);
                        default -> reduce_chetac = ZUtil.random(22, 31);
                    }
                    it_select.valueChetac -= reduce_chetac;
                    if (it_select.valueChetac <= 0) {
                        it_select.valueChetac = 0;
                        stopReason = "Điểm chế tác về 0, không thể tiếp tục hoàn mỹ!";
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

        if (it_select.isHoanMy == 1) {
            p.item_to_kham_ngoc = null;
        }
        p.item_to_kham_ngoc_id_ngoc = -1;
        p.data_yesno = null;

        try {
            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO HOÀN MỸ:\n" +
                "- Trang bị: " + it_select.template.name + "\n" +
                "- Trạng thái: " + (it_select.isHoanMy == 1 ? "HOÀN MỸ THÀNH CÔNG" : "Chưa hoàn mỹ") + "\n" +
                "- Điểm chế tác: " + startChetac + " -> " + it_select.valueChetac + "\n" +
                "- Thành công: " + successCount + " lần | Thất bại: " + failCount + " lần\n" +
                "- Tiêu hao: " + totalGemsSpent + " Ngọc Hải Thạch | " + totalRubySpent + " Ruby\n" +
                "- Lý do dừng: " + stopReason
            );
        } catch (Exception ignored) {}
    }
}
