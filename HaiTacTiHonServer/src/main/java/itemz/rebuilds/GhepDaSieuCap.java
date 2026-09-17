package itemz.rebuilds;

import zabstracts.AbsCombie;
import model.Player;
import itemz.Rebuild_Item;
import network.Message;
import model.YesNoDialog;
import model.InputDialog;
import template.ItemTemplate4;
import core.ZUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GhepDaSieuCap extends AbsCombie {
    @Override
    public byte getType() {
        return 13;
    }

    @Override
    public void process(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException {
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        if (cat == 4 && num == 0 && type == 13 && (action == 28 || action == 29)) { // bo item da sieu cap
            if (idItem >= 221 && idItem <= 226) {
                Rebuild_Item.show_table(p, 5);
                p.getService().send_box_ThongBao_OK("Đá Hải Thạch không thể dùng để ghép đá siêu cấp!");
                p.data_yesno = null;
                return;
            }
            if (!Rebuild_Item.isDaCap6(idItem)) {
                Rebuild_Item.show_table(p, 5);
                p.getService().send_box_ThongBao_OK("Vật phẩm không hợp lệ! Phải là Đá Cấp 6.");
                p.data_yesno = null;
                return;
            }
            if (idItem == 367 && action == 29) {
                Rebuild_Item.show_table(p, 5);
                p.getService().send_box_ThongBao_OK("Không thể sử dụng hổ phách để làm đá nguyên liệu!");
                p.data_yesno = null;
                return;
            }
            int requiredCount = (action == 28 ? 1 : 2);
            if (p.item.total_item_bag_by_id(4, idItem) < requiredCount) {
                Rebuild_Item.show_table(p, 5);
                p.getService().send_box_ThongBao_OK("Không đủ " + requiredCount + " "
                        + ItemTemplate4.get_item_name(idItem));
                p.data_yesno = null;
                return;
            }
            if (p.data_yesno == null || p.data_yesno.length != 2) {
                p.data_yesno = new int[]{-1, -1};
            }
            if (action == 28) {
                p.data_yesno[0] = idItem;
            } else if (action == 29) {
                p.data_yesno[1] = idItem;
            }
            p.getService().sendRebuildCombineDaSieuCapPutMaterial(action, idItem, (byte) 4, (short) requiredCount, p.percent_da_sieu_cap);
        } else if (idItem == 0 && cat == 0 && num == 0 && type == 13 && action == 26
                && p.data_yesno != null && p.data_yesno.length == 2) { // bat dau tao thanh da sieu cap
            int id1 = p.data_yesno[0];
            int id2 = p.data_yesno[1];
            if (id1 <= 0 || id2 <= 0) {
                p.getService().send_box_ThongBao_OK("Vui lòng đặt đủ đá chính và đá nguyên liệu!");
                return;
            }
            if (id1 == id2) {
                p.getService().send_box_ThongBao_OK("Đá siêu cấp và đá nguyên liệu phải khác nhau!");
                return;
            }
            if (p.item.able_bag() < 1) {
                p.getService().send_box_ThongBao_OK("Hành trang đầy (cần ít nhất 1 ô trống)!");
                return;
            }
            ItemTemplate4 it_temp1 = ItemTemplate4.get_it_by_id(id1);
            ItemTemplate4 it_temp2 = ItemTemplate4.get_it_by_id(id2);
            if (it_temp1 == null || it_temp2 == null) {
                p.getService().send_box_ThongBao_OK("Vật phẩm không hợp lệ!");
                return;
            }

            synchronized (p.item) {
                if (p.item.total_item_bag_by_id(4, id1) < 1) {
                    p.getService().send_box_ThongBao_OK("Không đủ 1 " + it_temp1.name);
                    return;
                }
                if (p.item.total_item_bag_by_id(4, id2) < 2) {
                    p.getService().send_box_ThongBao_OK("Không đủ 2 " + it_temp2.name);
                    return;
                }

                boolean suc = (p.percent_da_sieu_cap >= 100 ? 120 : p.percent_da_sieu_cap) > ZUtil.random(120);
                p.item.remove_item47(4, id2, suc ? 2 : 1);
                if (suc) {
                    p.item.remove_item47(4, id1, 1);
                    short resultGemId = Rebuild_Item.get_id_ngoc_sieu_cap(id1, id2);
                    p.item.add_item_bag47(4, resultGemId, 1);
                    p.percent_da_sieu_cap = 35;

                    Message m = new Message(-67);
                    m.writer().writeByte(27);
                    m.writer().writeUTF("Chúc mừng bạn nâng cấp thành công " + ItemTemplate4.get_item_name(resultGemId));
                    p.addmsg(m);
                    m.cleanup();
                } else {
                    p.percent_da_sieu_cap = (byte) Math.min(100, p.percent_da_sieu_cap + 5);

                    Message m = new Message(-67);
                    m.writer().writeByte(30);
                    m.writer().writeUTF("Quá trình nâng cấp thất bại! (Tỉ lệ tăng lên " + p.percent_da_sieu_cap + "%)");
                    p.addmsg(m);
                    m.cleanup();
                }
                p.item.updateInventory(false);
            }
        }
    }

    public static void executeAutoGhepDaSieuCap(Player p, int id1, int id2, int loopCount) {
        if (p == null) return;
        if (p.trade_target != null) {
            try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            return;
        }
        ItemTemplate4 it_temp1 = ItemTemplate4.get_it_by_id(id1);
        ItemTemplate4 it_temp2 = ItemTemplate4.get_it_by_id(id2);
        if (it_temp1 == null || it_temp2 == null || id1 == id2) {
            try { p.getService().send_box_ThongBao_OK("Đá chính và đá nguyên liệu không hợp lệ!"); } catch (Exception ignored) {}
            return;
        }

        int successCount = 0;
        int failCount = 0;
        int totalMat1Spent = 0;
        int totalMat2Spent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần ghép";

        synchronized (p.item) {
            for (int step = 0; step < loopCount; step++) {
                if (p.item.able_bag() < 1) {
                    stopReason = "Hành trang đầy (cần ít nhất 1 ô trống)!";
                    break;
                }
                if (p.item.total_item_bag_by_id(4, id1) < 1) {
                    stopReason = "Hết " + it_temp1.name + "!";
                    break;
                }
                if (p.item.total_item_bag_by_id(4, id2) < 2) {
                    stopReason = "Hết " + it_temp2.name + " (Cần 2 viên)!";
                    break;
                }

                boolean suc = (p.percent_da_sieu_cap >= 100 ? 120 : p.percent_da_sieu_cap) > ZUtil.random(120);
                p.item.remove_item47(4, id2, suc ? 2 : 1);
                totalMat2Spent += (suc ? 2 : 1);

                if (suc) {
                    p.item.remove_item47(4, id1, 1);
                    totalMat1Spent += 1;
                    short resultId = Rebuild_Item.get_id_ngoc_sieu_cap(id1, id2);
                    p.item.add_item_bag47(4, resultId, 1);
                    p.percent_da_sieu_cap = 35;
                    successCount++;
                } else {
                    p.percent_da_sieu_cap = (byte) Math.min(100, p.percent_da_sieu_cap + 5);
                    failCount++;
                }
            }

            p.item.updateInventory(false);
        }

        try {
            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO GHÉP ĐÁ SIÊU CẤP:\n" +
                "- Thành công: " + successCount + " lần | Thất bại: " + failCount + " lần\n" +
                "- Tiêu hao: " + totalMat1Spent + " " + it_temp1.name + " | " + totalMat2Spent + " " + it_temp2.name + "\n" +
                "- Tỉ lệ hiện tại: " + p.percent_da_sieu_cap + "%\n" +
                "- Trạng thái: " + stopReason
            );
        } catch (Exception ignored) {}
    }

    public static void openAutoGhepDaSieuCapMenu(Player p) {
        if (p == null) return;
        if (p.trade_target != null) {
            try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            return;
        }
        short[] tier6Gems = new short[]{49, 55, 61, 67, 73, 79, 367};
        List<Short> availableMainGems = new ArrayList<>();
        for (short gid : tier6Gems) {
            if (p.item.total_item_bag_by_id(4, gid) >= 1) {
                availableMainGems.add(gid);
            }
        }
        if (availableMainGems.isEmpty()) {
            try {
                p.getService().send_box_ThongBao_OK("Bạn không có Đá Cấp 6 nào trong hành trang!");
            } catch (Exception ignored) {}
            return;
        }

        String[] mainGemNames = new String[availableMainGems.size()];
        for (int i = 0; i < availableMainGems.size(); i++) {
            short gid = availableMainGems.get(i);
            mainGemNames[i] = ItemTemplate4.get_item_name(gid) + " (" + p.item.total_item_bag_by_id(4, gid) + ")";
        }

        try {
            YesNoDialog mainDialog = new YesNoDialog(p, 13, "Chọn Đá Chính (Slot 1)",
                "Chọn loại Đá Cấp 6 làm đá chính (Tỉ lệ hiện tại: " + p.percent_da_sieu_cap + "%):",
                mainGemNames, new byte[mainGemNames.length], selectedMainIdx -> {
                    if (selectedMainIdx < 0 || selectedMainIdx >= availableMainGems.size()) return;
                    short chosenMainId = availableMainGems.get(selectedMainIdx);

                    List<Short> availableMatGems = new ArrayList<>();
                    for (short gid : new short[]{49, 55, 61, 67, 73, 79}) {
                        if (gid != chosenMainId && p.item.total_item_bag_by_id(4, gid) >= 2) {
                            availableMatGems.add(gid);
                        }
                    }
                    if (availableMatGems.isEmpty()) {
                        try {
                            p.getService().send_box_ThongBao_OK("Không có Đá Cấp 6 phụ nào đủ từ 2 viên trở lên để làm nguyên liệu!");
                        } catch (Exception ignored) {}
                        return;
                    }

                    String[] matGemNames = new String[availableMatGems.size()];
                    for (int j = 0; j < availableMatGems.size(); j++) {
                        short gid = availableMatGems.get(j);
                        matGemNames[j] = ItemTemplate4.get_item_name(gid) + " (" + p.item.total_item_bag_by_id(4, gid) + ")";
                    }

                    try {
                        YesNoDialog matDialog = new YesNoDialog(p, 13, "Chọn Đá Nguyên Liệu (Slot 2)",
                            "Chọn loại Đá Cấp 6 làm nguyên liệu (Cần 2 viên mỗi lần):",
                            matGemNames, new byte[matGemNames.length], selectedMatIdx -> {
                                if (selectedMatIdx < 0 || selectedMatIdx >= availableMatGems.size()) return;
                                short chosenMatId = availableMatGems.get(selectedMatIdx);

                                try {
                                    boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
                                    String[] countOptions = isTest
                                            ? new String[]{"1 lần", "5 lần", "10 lần", "20 lần", "50 lần", "100 lần", "Nhập số", "Hủy"}
                                            : new String[]{"1 lần", "5 lần", "10 lần", "Hủy"};
                                    byte[] countTypes = isTest
                                            ? new byte[]{-1, -1, -1, -1, -1, -1, -1, 1}
                                            : new byte[]{-1, -1, -1, 1};

                                    YesNoDialog countDialog = new YesNoDialog(p, 13, "Số lần Auto Ghép",
                                        "Ghép " + ItemTemplate4.get_item_name(chosenMainId) + " + " + ItemTemplate4.get_item_name(chosenMatId)
                                        + "\n-> " + ItemTemplate4.get_item_name(Rebuild_Item.get_id_ngoc_sieu_cap(chosenMainId, chosenMatId))
                                        + "\n(Tỉ lệ hiện tại: " + p.percent_da_sieu_cap + "%):",
                                        countOptions, countTypes, countIdx -> {
                                            if (isTest && countIdx == 6) { // Nhập số
                                                try {
                                                    new InputDialog(p, 13, "Nhập số lần ghép", new String[]{"Số lần muốn ghép:"}, inputs -> {
                                                        if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                                        try {
                                                            int count = Integer.parseInt(inputs[0].trim());
                                                            if (count <= 0) {
                                                                p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                                                return;
                                                            }
                                                            executeAutoGhepDaSieuCap(p, chosenMainId, chosenMatId, count);
                                                        } catch (NumberFormatException e) {
                                                            try { p.getService().send_box_ThongBao_OK("Vui lòng nhập số hợp lệ!"); } catch (Exception ignored) {}
                                                        }
                                                    }).startInput();
                                                } catch (Exception ignored) {}
                                                return;
                                            }
                                            int count = 0;
                                            if (!isTest) {
                                                count = switch (countIdx) {
                                                    case 0 -> 1;
                                                    case 1 -> 5;
                                                    case 2 -> 10;
                                                    default -> 0;
                                                };
                                            } else {
                                                count = switch (countIdx) {
                                                    case 0 -> 1;
                                                    case 1 -> 5;
                                                    case 2 -> 10;
                                                    case 3 -> 20;
                                                    case 4 -> 50;
                                                    case 5 -> 100;
                                                    default -> 0;
                                                };
                                            }
                                            if (count > 0) {
                                                executeAutoGhepDaSieuCap(p, chosenMainId, chosenMatId, count);
                                            }
                                        });
                                    countDialog.startYesNo();
                                } catch (Exception ignored) {}
                            });
                        matDialog.startYesNo();
                    } catch (Exception ignored) {}
                });
            mainDialog.startYesNo();
        } catch (Exception ignored) {}
    }
}
