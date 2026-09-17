package itemz;

import model.Player;
import core.Manager;
import network.Service;
import core.ZUtil;
import network.Message;
import template.ItemTemplate7;
import template.Item_wear;
import java.io.IOException;

public class UpgradeSuperItem {
    public static int[][] MATERIAL = new int[][] { //
            new int[] {100, 15, 2_000_000, 30}, // 3
            new int[] {100, 20, 2_500_000, 30}, // 4
            new int[] {100, 25, 3_000_000, 50}, // 5
            new int[] {100, 30, 3_200_000, 50}, // 6
            new int[] {100, 35, 3_500_000, 50}, // 7
            new int[] {100, 40, 4_000_000, 70}, // 8
            new int[] {100, 45, 4_500_000, 70}, // 9
            new int[] {100, 50, 4_500_000, 70} // 10
    };

    public static void show_table(Player p) throws IOException {
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        Message m = new Message(66);
        m.writer().writeByte(7);
        p.addmsg(m);
        m.cleanup();
        p.data_super_upgrade = new int[] {-1, 0, 0, 0};
    }

    public static void process(Player p, Message m2) throws IOException {
        if (p == null || p.item == null || p.item.bag3 == null) return;
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        byte type = m2.reader().readByte();
        short id = m2.reader().readShort();
        byte beri_gem = m2.reader().readByte();
        byte num = m2.reader().readByte();
        if (type == 4 && beri_gem == 0 && num == 0) { // add item3
            if (id < 0 || id >= p.item.bag3.length) return;
            Item_wear it_select = p.item.bag3[id];
            if (it_select != null) {
                if (!UpgradeSuperItem.check_it_can_upgrade_super(p, it_select)) {
                    return;
                }
                Message m = new Message(66);
                m.writer().writeByte(4);
                m.writer().writeShort(id);
                //
                m.writer().writeShort(it_select.getColor() >= 3 ? 4 : 3);
                m.writer().writeShort(UpgradeSuperItem.get_material(1, it_select));
                m.writer().writeShort(1);
                m.writer().writeShort(UpgradeSuperItem.get_material(0, it_select));
                p.addmsg(m);
                m.cleanup();
                p.data_super_upgrade[0] = id;
            }
        } else if (type == 6 && id == 6 && (beri_gem == 1 || beri_gem == 0)) { // add or remove
                                                                               // item7 mai rua
            if (num > 0 && p.item.total_item_bag_by_id(7, 6) < num || num > 5 || num < 0) {
                return;
            }
            Message m = new Message(66);
            m.writer().writeByte(6);
            m.writer().writeByte(num);
            m.writer().writeShort(id);
            m.writer().writeByte(num);
            p.addmsg(m);
            m.cleanup();
            p.data_super_upgrade[1] = num;
        } else if (type == 14 && id == 10 && (beri_gem == 1 || beri_gem == 0) && num == 1) { // add
                                                                                             // or
                                                                                             // remove
                                                                                             // item7
                                                                                             // khien
            if (beri_gem > 0 && p.item.total_item_bag_by_id(7, 10) < beri_gem) {
                return;
            }
            Message m = new Message(66);
            m.writer().writeByte(14);
            m.writer().writeByte(beri_gem);
            m.writer().writeShort(id);
            p.addmsg(m);
            m.cleanup();
            p.data_super_upgrade[2] = beri_gem;
        } else if (type == 5 && id == 11 && (beri_gem == 1 || beri_gem == 0) && num == 1) { // add
                                                                                            // or
                                                                                            // remove
                                                                                            // item7
                                                                                            // thien
                                                                                            // thach
                                                                                            // may
                                                                                            // man
            if (beri_gem > 0 && p.item.total_item_bag_by_id(7, 11) < beri_gem) {
                return;
            }
            Message m = new Message(66);
            m.writer().writeByte(5);
            m.writer().writeByte(beri_gem);
            m.writer().writeShort(id);
            p.addmsg(m);
            m.cleanup();
            p.data_super_upgrade[3] = beri_gem;
        } else if (type == 1 && beri_gem == 0 && num == 0) { // request use beri or ruby
            if (id < 0 || id >= p.item.bag3.length) return;
            Item_wear it_select = p.item.bag3[id];
            if (it_select != null) {
                if (!UpgradeSuperItem.check_it_can_upgrade_super(p, it_select)) {
                    return;
                }
                Message m = new Message(66);
                m.writer().writeByte(1);
                m.writer().writeUTF("Bạn có muốn cường hóa vật phẩm " + it_select.template.name
                        + " lên cấp " + (it_select.levelUp + 1));
                m.writer().writeInt(UpgradeSuperItem.get_material(2, it_select));
                m.writer().writeShort(UpgradeSuperItem.get_material(3, it_select));
                m.writer().writeShort(id);
                p.addmsg(m);
                m.cleanup();
            }
        } else if (type == 7 && id == 0 && beri_gem == 0 && num == 0) { // open upgrade from shop
                                                                        // material
            UpgradeSuperItem.show_table(p);
        } else if (type == 2 && (beri_gem == 1 || beri_gem == 2) && num == 0) { // start upgrade
            if (id < 0 || id >= p.item.bag3.length) return;
            Item_wear it_select = p.item.bag3[id];
            if (it_select != null) {
                if (!UpgradeSuperItem.check_it_can_upgrade_super(p, it_select)) {
                    return;
                }
                if (it_select.levelUp >= 15) {
                    p.getService().send_box_ThongBao_OK("Trang bị đã Cường hóa cấp tối đa (+15)!");
                    return;
                }
                if (it_select.isThanTrang()) {
                    p.getService().send_box_ThongBao_OK("Trang bị Thần Trang (Type 8-15) vui lòng sử dụng tính năng Cường Hóa Thần Trang!");
                    return;
                }
                if (it_select.template.typeEquip > 5) {
                    p.getService().send_box_ThongBao_OK("Trang bị này không thể nâng cấp!");
                    return;
                }
                boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
                String[] options = !isTest
                    ? new String[]{"1 lần", "5 lần", "10 lần", "Đóng"}
                    : new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"};
                byte[] icons = new byte[options.length];
                for (int i = 0; i < icons.length - 1; i++) icons[i] = -1;
                icons[icons.length - 1] = 1;

                model.YesNoDialog ynd = new model.YesNoDialog(p, 66, "Auto Cường Hóa Cao Cấp",
                    "Chọn số lần Auto Cường Hóa bằng " + (beri_gem == 1 ? "Beri" : "Ruby") + " (Dừng khi đạt cấp tối đa +15 / hết nguyên liệu):",
                    options, icons, value -> {
                        if (!isTest) {
                            int count = switch (value) {
                                case 0 -> 1;
                                case 1 -> 5;
                                case 2 -> 10;
                                default -> 0;
                            };
                            if (count > 0) {
                                executeAutoSuperUpgrade(p, id, beri_gem, count);
                            }
                            return;
                        }
                        if (value == 7) {
                            new model.InputDialog(p, 66, "Nhập số lần", new String[]{"Số lần muốn cường hóa:"}, inputs -> {
                                if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                try {
                                    int count = Integer.parseInt(inputs[0].trim());
                                    if (count <= 0) {
                                        p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                        return;
                                    }
                                    executeAutoSuperUpgrade(p, id, beri_gem, count);
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
                            executeAutoSuperUpgrade(p, id, beri_gem, count);
                        }
                    });
                ynd.startYesNo();
            }
        }
    }

    public static void executeAutoSuperUpgrade(Player p, short id, byte beri_gem, int loopCount) {
        if (p == null || p.item == null || id < 0 || id >= p.item.bag3.length) return;
        if (p.trade_target != null) {
            try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            return;
        }
        Item_wear it_select = p.item.bag3[id];
        if (it_select == null || it_select.template == null) {
            try { p.getService().send_box_ThongBao_OK("Vật phẩm không tồn tại!"); } catch (Exception ignored) {}
            return;
        }

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
            try { p.getService().send_box_ThongBao_OK("Vật phẩm không còn trong hành trang!"); } catch (Exception ignored) {}
            return;
        }

        if (it_select.levelUp >= 15) {
            try { p.getService().send_box_ThongBao_OK("Trang bị đã đạt cấp tối đa (+15)!"); } catch (Exception ignored) {}
            return;
        }

        int startLevel = it_select.levelUp;
        int successCount = 0;
        int failCount = 0;
        long totalBeriSpent = 0;
        int totalRubySpent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần cường hóa";

        int initialMaiRua = (p.data_super_upgrade != null && p.data_super_upgrade.length > 1) ? p.data_super_upgrade[1] : 0;
        int initialKhien = (p.data_super_upgrade != null && p.data_super_upgrade.length > 2) ? p.data_super_upgrade[2] : 0;
        int initialMayMan = (p.data_super_upgrade != null && p.data_super_upgrade.length > 3) ? p.data_super_upgrade[3] : 0;

        synchronized (p.item) {
            for (int step = 0; step < loopCount; step++) {
                if (it_select.levelUp >= 15) {
                    stopReason = "Đạt cấp tối đa (+15)!";
                    break;
                }

                int material_0 = UpgradeSuperItem.get_material(0, it_select);
                int material_1 = UpgradeSuperItem.get_material(1, it_select);
                int id_matrial_1 = it_select.getColor() >= 3 ? 4 : 3;

                if (p.item.total_item_bag_by_id(7, 1) < material_0) {
                    stopReason = "Không đủ " + material_0 + " " + ItemTemplate7.get_it_by_id(1).name;
                    break;
                }
                if (p.item.total_item_bag_by_id(7, id_matrial_1) < material_1) {
                    stopReason = "Không đủ " + material_1 + " " + ItemTemplate7.get_it_by_id(id_matrial_1).name;
                    break;
                }

                if (beri_gem == 1) {
                    int vang_req = UpgradeSuperItem.get_material(2, it_select);
                    if (p.get_vang() < vang_req) {
                        stopReason = "Không đủ " + ZUtil.number_format(vang_req) + " beri";
                        break;
                    }
                    p.update_vang(-vang_req);
                    totalBeriSpent += vang_req;
                } else {
                    int ruby_req = UpgradeSuperItem.get_material(3, it_select);
                    if (p.get_ngoc() < ruby_req) {
                        stopReason = "Không đủ " + ruby_req + " ruby";
                        break;
                    }
                    p.update_ngoc(-ruby_req);
                    totalRubySpent += ruby_req;
                }

                p.item.remove_item47(7, 1, material_0);
                p.item.remove_item47(7, id_matrial_1, material_1);

                int percent_decrease_level = (it_select.levelUp == 13 || it_select.levelUp == 14) ? 120 : 80;
                int percent_suc = 1;
                switch(it_select.levelUp) {
                    case 0: case 1: case 2: case 3: case 4:
                        percent_suc = 350;
                        percent_decrease_level = 0;
                        break;
                    case 5: case 6: case 7:
                        percent_suc = 200;
                        percent_decrease_level = 0;
                        break;
                    case 8: case 9:
                        percent_suc = 100;
                        percent_decrease_level = 0;
                        break;
                    case 10: case 11: case 12: case 13:
                        percent_suc = 15;
                        break;
                    case 14:
                        percent_suc = 7;
                        break;
                }

                // Apply mai rùa protection if available
                if (initialMaiRua >= 1 && initialMaiRua <= 5 && p.item.total_item_bag_by_id(7, 6) >= initialMaiRua) {
                    percent_decrease_level -= initialMaiRua * 15;
                    p.item.remove_item47(7, 6, initialMaiRua);
                }
                // Apply khiên
                if (initialKhien == 1 && p.item.total_item_bag_by_id(7, 10) >= 1) {
                    percent_decrease_level -= 20;
                    p.item.remove_item47(7, 10, 1);
                }
                if (percent_decrease_level < 0) {
                    percent_decrease_level = 0;
                }
                // Apply thiên thạch may mắn
                if (initialMayMan == 1 && p.item.total_item_bag_by_id(7, 11) >= 1) {
                    percent_suc *= 2;
                    p.item.remove_item47(7, 11, 1);
                }

                boolean suc = percent_suc > ZUtil.random(1000 + it_select.levelUp * 2);
                if (suc) {
                    it_select.levelUp++;
                    successCount++;
                    p.updateArchiDaily(6);
                    if (it_select.levelUp >= 15) {
                        Manager.gI().chatKTG(0, (p.name + " cường hóa thành công " + it_select.template.name + " lên +" + it_select.levelUp + ", thật là may mắn!"), 5);
                    }
                } else {
                    failCount++;
                    if (it_select.levelUp > 10 && percent_decrease_level > ZUtil.random(120)) {
                        it_select.levelUp -= ZUtil.random(1, 4);
                        if (it_select.levelUp < 10) {
                            it_select.levelUp = 10;
                        }
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

        try {
            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO CƯỜNG HÓA CAO CẤP:\n" +
                "- Trang bị: " + it_select.template.name + "\n" +
                "- Cấp độ: +" + startLevel + " -> +" + it_select.levelUp + "\n" +
                "- Thành công: " + successCount + " lần | Thất bại: " + failCount + " lần\n" +
                "- Tổng tiêu hao: " + (totalBeriSpent > 0 ? (ZUtil.number_format(totalBeriSpent) + " Beri ") : "") + (totalRubySpent > 0 ? (totalRubySpent + " Ruby") : "") + "\n" +
                "- Trạng thái: " + stopReason
            );
        } catch (Exception ignored) {}
    }

    public static int get_material(int type, Item_wear it_select) {
        if (it_select == null || it_select.template == null) return 1;
        boolean isThanTrang = it_select.isThanTrang();

        int result = 0;
        int tier = Math.max(0, it_select.levelUp - 10);
        int delta = 0;
        int index = (it_select.template.level / 10) - 3;
        if (index < 0) {
            index = 0;
        } else if (index >= UpgradeSuperItem.MATERIAL.length) {
            index = UpgradeSuperItem.MATERIAL.length - 1;
        }
        result = UpgradeSuperItem.MATERIAL[index][type];
        if (it_select.template.level == 30) {
            delta = (result * 3) / 10;
        } else if (it_select.template.level == 40) {
            delta = (result * 2) / 10;
        } else {
            delta = result / 10;
        }
        while (tier > 0) {
            result += delta;
            tier--;
        }
        if (isThanTrang) {
            double mult = 1.15;
            result = (int) Math.ceil(result * mult);
        }
        return Math.max(1, result);
    }

    private static boolean check_it_can_upgrade_super(Player p, Item_wear it_select)
            throws IOException {
        if (it_select == null || it_select.template == null) return false;

        if (it_select.isThanTrang()) {
            p.getService().send_box_ThongBao_OK("Trang bị Thần Trang (Type 8-15) vui lòng sử dụng tính năng Cường Hóa Thần Trang!");
            return false;
        }

        if (it_select.getColor() < 2) {
            p.getService().send_box_ThongBao_OK("Phẩm chất trang bị Tím, Cam hoặc Đỏ mới có thể tiến hành Siêu Cường Hóa!");
            return false;
        }
        if (it_select.levelUp < 10) {
            p.getService().send_box_ThongBao_OK("Trang bị đã Cường hóa +10 mới có thể tiến hành Siêu Cường Hóa!");
            return false;
        }
        if (it_select.levelUp >= 15) {
            p.getService().send_box_ThongBao_OK("Trang bị đã Cường hóa cấp tối đa (+15)!");
            return false;
        }
        if (it_select.template.level < 30) {
            p.getService().send_box_ThongBao_OK("Cấp trang bị phải từ Level 30 trở lên mới có thể tiến hành Siêu Cường Hóa!");
            return false;
        }
        return true;
    }
}
