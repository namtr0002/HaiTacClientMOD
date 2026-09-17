package itemz.rebuilds;

import java.io.IOException;
import model.Player;
import network.Message;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.DataTemplate;
import zabstracts.AbsUpgradeDevil;
import model.YesNoDialog;
import core.ZUtil;

public class NangCapRuongAcQuy extends AbsUpgradeDevil {

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public byte getClientType() {
        return 13;
    }

    @Override
    public void show_table(Player p) throws IOException {
        p.getService().sendUpgradeDevilShowTable(getClientType());
    }

    @Override
    public boolean checkProcess(Player p, byte action, short id, byte cat, short num) throws IOException {
        return id == 29 && cat == 4 && num == 1 && (action == 14 || action == 17);
    }

    @Override
    public void process(Player p, byte action, short id, byte cat, short num) throws IOException {
        if (action == 14) { // bo ruong ac quy vao
            if (p.item.total_item_bag_by_id(7, 9) < 10) {
                p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ItemTemplate7.get_it_by_id(9).name);
                return;
            }
            if (p.item.total_item_bag_by_id(4, 29) < 1) {
                p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ItemTemplate4.get_it_by_id(29).name);
                return;
            }
            p.getService().sendUpgradeDevilPutMaterial((byte) 14, (byte) 0, (short) 29, (byte) 4, (short) 1);
            p.getService().sendUpgradeDevilPutMaterial((byte) 14, (byte) 1, (short) 9, (byte) 7, (short) 10);
            p.getService().sendUpgradeDevilPercent((byte) 19, (byte) 5);
        } else if (action == 17) { // bat dau upgrade ruong ac quy
            if (p.item.total_item_bag_by_id(7, 9) < 10) {
                p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ItemTemplate7.get_it_by_id(9).name);
                return;
            }
            if (p.item.total_item_bag_by_id(4, 29) < 1) {
                p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ItemTemplate4.get_it_by_id(29).name);
                return;
            }
            
            p.setyesNoDialog(new YesNoDialog(p, 34, "Thông báo",
                    ("Bạn có thật sự muốn nâng cấp rương ác quỷ"
                    + "? Thất bại sẽ không mất rương ác quỷ"),
                    new String[]{"50.000 Beri", "5 Ruby", "Đóng"}, new byte[]{-1, -1, -1},
                    value -> {
                        if (value == 0 || value == 1) {
                            byte payType = (byte) value; // 0 = Beri, 1 = Ruby
                            boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
                            String[] countOptions = isTest
                                    ? new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"}
                                    : new String[]{"1 lần", "5 lần", "10 lần", "Đóng"};
                            byte[] countTypes = isTest
                                    ? new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, 1}
                                    : new byte[]{-1, -1, -1, 1};

                            YesNoDialog ynd = new YesNoDialog(p, 34, "Auto Nâng Cấp Rương Ác Quỷ",
                                "Chọn số lần Auto Nâng Cấp bằng " + (payType == 0 ? "Beri" : "Ruby") + ":",
                                countOptions, countTypes, valCount -> {
                                    if (isTest && valCount == 7) {
                                        new model.InputDialog(p, 34, "Nhập số lần", new String[]{"Số lần muốn nâng cấp:"}, inputs -> {
                                            if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                            try {
                                                int count = Integer.parseInt(inputs[0].trim());
                                                if (count <= 0) {
                                                    p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                                    return;
                                                }
                                                executeAutoUpgradeRuongAcQuy(p, payType, count);
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
                                        count = switch (valCount) {
                                            case 0 -> 1;
                                            case 1 -> 5;
                                            case 2 -> 10;
                                            default -> 0;
                                        };
                                    } else {
                                        count = switch (valCount) {
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
                                        executeAutoUpgradeRuongAcQuy(p, payType, count);
                                    }
                                });
                            ynd.startYesNo();
                        }
                    }));
            p.getService().startYesNo();
        }
    }

    public static void executeAutoUpgradeRuongAcQuy(Player p, byte payType, int loopCount) {
        if (p == null) return;
        int successCount = 0;
        int failCount = 0;
        long totalBeriSpent = 0;
        int totalRubySpent = 0;
        int totalMatSpent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần nâng cấp";

        for (int step = 0; step < loopCount; step++) {
            if (p.item.total_item_bag_by_id(4, 29) < 1) {
                stopReason = "Hết " + ItemTemplate4.get_it_by_id(29).name + "!";
                break;
            }
            if (p.item.total_item_bag_by_id(7, 9) < 10) {
                stopReason = "Không đủ 10 " + ItemTemplate7.get_it_by_id(9).name + "!";
                break;
            }
            if (!p.item.can_add_item_bag47(4, 158, 1)) {
                stopReason = "Hành trang đầy!";
                break;
            }

            boolean suc = false;
            if (payType == 0) { // Beri
                if (p.get_vang() < 50_000) {
                    stopReason = "Không đủ 50.000 Beri!";
                    break;
                }
                p.update_vang(-50_000);
                totalBeriSpent += 50_000;
            } else { // Ruby
                if (p.get_ngoc() < 5) {
                    stopReason = "Không đủ 5 Ruby!";
                    break;
                }
                p.update_ngoc(-5);
                totalRubySpent += 5;
            }

            p.item.remove_item47(7, 9, 10);
            totalMatSpent += 10;
            suc = (payType == 1 ? 15 : 5) > ZUtil.random(150);

            if (suc) {
                p.item.remove_item47(4, 29, 1);
                p.item.add_item_bag47(4, 158, 1);
                successCount++;
            } else {
                failCount++;
            }
        }

        try {
            p.updateMoney();
            p.item.updateInventory(false);

            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO NÂNG CẤP RƯƠNG ÁC QUỶ:\n" +
                "- Nhận được: " + successCount + " Rương Đại ác quỷ\n" +
                "- Thành công: " + successCount + " lần | Thất bại: " + failCount + " lần\n" +
                "- Tiêu hao: " + totalMatSpent + " " + ItemTemplate7.get_it_by_id(9).name + " | " + (totalBeriSpent > 0 ? (ZUtil.number_format(totalBeriSpent) + " Beri ") : "") + (totalRubySpent > 0 ? (totalRubySpent + " Ruby") : "") + "\n" +
                "- Trạng thái: " + stopReason
            );
        } catch (Exception ignored) {}
    }
}
