package itemz.rebuilds;

import java.io.IOException;
import model.Player;
import network.Message;
import template.ItemTemplate7;
import skill.Skill_info;
import zabstracts.AbsUpgradeDevil;
import model.YesNoDialog;
import core.ZUtil;

public class NangCapKyNangAcQuy extends AbsUpgradeDevil {

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public byte getClientType() {
        return 8;
    }

    @Override
    public void show_table(Player p) throws IOException {
        p.getService().sendUpgradeDevilShowTable(getClientType());
    }

    @Override
    public boolean checkProcess(Player p, byte action, short id, byte cat, short num) throws IOException {
        return cat == 104 && (num == 0 || num == 1) && (action == 9 || action == 12);
    }

    @Override
    public void process(Player p, byte action, short id, byte cat, short num) throws IOException {
        if (action == 9) { // bo skill vao (num =0) va sau khi xong bo vao lai (num = 1)
            if (model.ThanTrangConfig.isThanTrangSkill(id)) {
                p.getService().send_box_ThongBao_OK("Không thể cường hóa kỹ năng Thần Trang!");
                return;
            }
            Skill_info sk_temp = p.get_skill_temp(id);
            if (sk_temp != null) {
                if (sk_temp.temp != null && model.ThanTrangConfig.isThanTrangSkill(sk_temp.temp.ID)) {
                    p.getService().send_box_ThongBao_OK("Không thể cường hóa kỹ năng Thần Trang!");
                    return;
                }
                if (sk_temp.lvdevil > 4) {
                    p.getService().send_box_ThongBao_OK(sk_temp.temp.name + " đã được cường hóa tối đa!");
                    return;
                }
                if (p.item.total_item_bag_by_id(7, 9) > 9) {
                    p.getService().sendUpgradeDevilPutMaterial((byte) 9, (byte) 0, id, (byte) 104, (short) 1);
                    p.getService().sendUpgradeDevilPutMaterial((byte) 9, (byte) 1, (short) 9, (byte) 7, (short) 10);
                } else {
                    p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ItemTemplate7.get_it_by_id(9).name);
                }
            }
        } else if (action == 12) { // bat dau cuong hoa skill
            if (model.ThanTrangConfig.isThanTrangSkill(id)) {
                p.getService().send_box_ThongBao_OK("Không thể cường hóa kỹ năng Thần Trang!");
                return;
            }
            final Skill_info sk_temp = p.get_skill_temp(id);
            if (sk_temp != null) {
                if (sk_temp.temp != null && model.ThanTrangConfig.isThanTrangSkill(sk_temp.temp.ID)) {
                    p.getService().send_box_ThongBao_OK("Không thể cường hóa kỹ năng Thần Trang!");
                    return;
                }
                if (sk_temp.lvdevil > 4) {
                    p.getService().send_box_ThongBao_OK(sk_temp.temp.name + " đã được cường hóa tối đa!");
                    return;
                }
                final int percent = (sk_temp.lvdevil == 0) ? 10
                        : ((sk_temp.lvdevil == 1) ? 8
                                : ((sk_temp.lvdevil == 2) ? 6
                                        : ((sk_temp.lvdevil == 3) ? 5 : 4)));
                
                p.data_yesno = new int[]{id};
                p.setyesNoDialog(new YesNoDialog(p, 33, "Thông báo",
                        ("Bạn có thật sự muốn cường hóa " + sk_temp.temp.name
                        + " không? Thành công sẽ tăng thêm " + percent
                        + "% vào cấp ác quỷ"),
                        new String[]{"50.000 Beri", "5 Ruby", "Đóng"}, new byte[]{-1, -1, -1},
                        value -> {
                            if (value == 0 || value == 1) {
                                if (model.ThanTrangConfig.isThanTrangSkill(id)) {
                                    try { p.getService().send_box_ThongBao_OK("Không thể cường hóa kỹ năng Thần Trang!"); } catch (Exception ignored) {}
                                    return;
                                }
                                byte payType = (byte) value; // 0 = Beri, 1 = Ruby
                                boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
                                String[] countOptions = isTest
                                        ? new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"}
                                        : new String[]{"1 lần", "5 lần", "10 lần", "Đóng"};
                                byte[] countTypes = isTest
                                        ? new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, 1}
                                        : new byte[]{-1, -1, -1, 1};

                                YesNoDialog ynd = new YesNoDialog(p, 33, "Auto Cường Hóa Kỹ Năng",
                                    "Chọn số lần Auto Cường Hóa bằng " + (payType == 0 ? "Beri" : "Ruby") + " (Dừng khi max cấp / hết tài nguyên):",
                                    countOptions, countTypes, valCount -> {
                                        if (isTest && valCount == 7) {
                                            new model.InputDialog(p, 33, "Nhập số lần", new String[]{"Số lần muốn cường hóa:"}, inputs -> {
                                                if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                                try {
                                                    int count = Integer.parseInt(inputs[0].trim());
                                                    if (count <= 0) {
                                                        p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                                        return;
                                                    }
                                                    executeAutoUpgradeDevilSkill(p, id, payType, count);
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
                                            executeAutoUpgradeDevilSkill(p, id, payType, count);
                                        }
                                    });
                                ynd.startYesNo();
                            }
                        }));
                p.getService().startYesNo();
            }
        }
    }

    public static void executeAutoUpgradeDevilSkill(Player p, short skillId, byte payType, int loopCount) {
        if (p == null) return;
        if (model.ThanTrangConfig.isThanTrangSkill(skillId)) {
            try { p.getService().send_box_ThongBao_OK("Không thể cường hóa kỹ năng Thần Trang!"); } catch (Exception ignored) {}
            return;
        }
        Skill_info sk_temp = p.get_skill_temp(skillId);
        if (sk_temp == null || sk_temp.temp == null) {
            try { p.getService().send_box_ThongBao_OK("Kỹ năng không tồn tại!"); } catch (Exception ignored) {}
            return;
        }
        if (model.ThanTrangConfig.isThanTrangSkill(sk_temp.temp.ID)) {
            try { p.getService().send_box_ThongBao_OK("Không thể cường hóa kỹ năng Thần Trang!"); } catch (Exception ignored) {}
            return;
        }
        if (sk_temp.lvdevil > 4) {
            try { p.getService().send_box_ThongBao_OK(sk_temp.temp.name + " đã được cường hóa tối đa!"); } catch (Exception ignored) {}
            return;
        }

        int startLv = sk_temp.lvdevil;
        int startPercent = sk_temp.devilpercent;
        int successCount = 0;
        int failCount = 0;
        long totalBeriSpent = 0;
        int totalRubySpent = 0;
        int totalMatSpent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần cường hóa";

        for (int step = 0; step < loopCount; step++) {
            if (sk_temp.lvdevil > 4) {
                stopReason = "Đạt cấp kỹ năng ác quỷ tối đa (+5)!";
                break;
            }
            if (p.item.total_item_bag_by_id(7, 9) < 10) {
                stopReason = "Không đủ 10 " + ItemTemplate7.get_it_by_id(9).name + "!";
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
                suc = 50 > ZUtil.random(120);
            } else { // Ruby
                if (p.get_ngoc() < 5) {
                    stopReason = "Không đủ 5 Ruby!";
                    break;
                }
                p.update_ngoc(-5);
                totalRubySpent += 5;
                suc = 75 > ZUtil.random(120);
            }

            p.item.remove_item47(7, 9, 10);
            totalMatSpent += 10;

            int per = (sk_temp.lvdevil == 0) ? 10
                    : ((sk_temp.lvdevil == 1) ? 8
                            : ((sk_temp.lvdevil == 2) ? 6
                                    : ((sk_temp.lvdevil == 3) ? 5 : 4)));

            if (suc) {
                successCount++;
                sk_temp.devilpercent += per;
                if (sk_temp.devilpercent >= 100) {
                    sk_temp.devilpercent = 0;
                    sk_temp.lvdevil++;
                }
            } else {
                failCount++;
            }
        }

        try {
            p.updateMoney();
            p.item.updateInventory(false);
            p.send_skill();
            p.setAbility();
            p.update_info_to_all();
            if (p instanceof model.DeTu) {
                ((model.DeTu) p).saveDeTu(false);
                if (((model.DeTu) p).master != null) {
                    ((model.DeTu) p).master.flush(((model.DeTu) p).master, false);
                }
            } else {
                p.flush(p, false);
            }

            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO CƯỜNG HÓA KỸ NĂNG ÁC QUỶ:\n" +
                "- Kỹ năng: " + sk_temp.temp.name + "\n" +
                "- Cấp độ: Cấp " + startLv + " (" + startPercent + "%) -> Cấp " + sk_temp.lvdevil + " (" + sk_temp.devilpercent + "%)\n" +
                "- Thành công: " + successCount + " lần | Thất bại: " + failCount + " lần\n" +
                "- Tiêu hao: " + totalMatSpent + " " + ItemTemplate7.get_it_by_id(9).name + " | " + (totalBeriSpent > 0 ? (ZUtil.number_format(totalBeriSpent) + " Beri ") : "") + (totalRubySpent > 0 ? (totalRubySpent + " Ruby") : "") + "\n" +
                "- Trạng thái: " + stopReason
            );
        } catch (Exception ignored) {}
    }
}
