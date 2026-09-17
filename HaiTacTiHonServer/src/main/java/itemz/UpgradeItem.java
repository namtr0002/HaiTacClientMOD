package itemz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Player;
import network.Service;
import core.ZUtil;
import core.Manager;
import network.Message;
import template.*;

public class UpgradeItem {
    public static List<DataUpgrade> DATA = new ArrayList<>();
    static {
        // Level 1 (+0 -> +1)
        addUpgrade((byte)1, (short)950, (byte)0, 3000, 1500, (short)2, (short)10, "8:4:3,8:1:3,3:4:1,2:3:1,-1:1:1,1:2:1,0:2:1");
        // Level 2 (+1 -> +2)
        addUpgrade((byte)2, (short)850, (byte)0, 3000, 1500, (short)2, (short)20, "8:4:3,8:1:3,3:4:1,2:3:1,-1:1:1,1:2:1,0:2:1");
        // Level 3 (+2 -> +3)
        addUpgrade((byte)3, (short)750, (byte)0, 4000, 2000, (short)2, (short)30, "8:4:3,8:1:3,3:4:1,2:3:1,-1:1:1,1:2:1,0:2:1");
        // Level 4 (+3 -> +4)
        addUpgrade((byte)4, (short)650, (byte)0, 4000, 2000, (short)2, (short)45, "8:4:3,8:1:3,3:4:1,2:3:1,-1:1:1,1:2:1,0:2:1");
        // Level 5 (+4 -> +5)
        addUpgrade((byte)5, (short)550, (byte)0, 4000, 2000, (short)2, (short)60, "8:4:3,8:1:3,3:4:1,2:3:1,-1:1:1,1:2:1,0:2:1");
        // Level 6 (+5 -> +6)
        addUpgrade((byte)6, (short)450, (byte)5, 6000, 3000, (short)3, (short)75, "8:4:6,8:1:3,3:4:2,2:3:2,-1:1:1,1:2:2,0:2:2");
        // Level 7 (+6 -> +7)
        addUpgrade((byte)7, (short)400, (byte)5, 6000, 3000, (short)3, (short)90, "8:4:6,8:1:3,3:4:2,2:3:2,-1:1:1,1:2:2,0:2:2");
        // Level 8 (+7 -> +8)
        addUpgrade((byte)8, (short)350, (byte)5, 6000, 3000, (short)3, (short)110, "8:4:9,8:1:3,3:4:3,2:3:3,-1:1:1,1:2:3,0:2:3");
        // Level 9 (+8 -> +9)
        addUpgrade((byte)9, (short)300, (byte)5, 8000, 4000, (short)4, (short)130, "8:4:9,8:1:3,3:4:3,2:3:3,-1:1:1,1:2:3,0:2:3");
        // Level 10 (+9 -> +10)
        addUpgrade((byte)10, (short)250, (byte)5, 10000, 5000, (short)5, (short)150, "8:4:15,8:1:6,3:4:5,2:3:5,-1:1:2,1:2:5,0:2:5");
        // Level 11 (+10 -> +11)
        addUpgrade((byte)11, (short)200, (byte)10, 15000, 7500, (short)8, (short)175, "8:4:18,8:1:6,3:4:6,2:3:6,-1:1:2,1:2:6,0:2:6");
        // Level 12 (+11 -> +12)
        addUpgrade((byte)12, (short)180, (byte)10, 20000, 10000, (short)10, (short)200, "8:4:21,8:1:6,3:4:7,2:3:7,-1:1:2,1:2:7,0:2:7");
        // Level 13 (+12 -> +13)
        addUpgrade((byte)13, (short)160, (byte)10, 25000, 12500, (short)15, (short)230, "8:4:24,8:1:9,3:4:8,2:3:8,-1:1:3,1:2:8,0:2:8");
        // Level 14 (+13 -> +14)
        addUpgrade((byte)14, (short)140, (byte)10, 30000, 15000, (short)20, (short)265, "8:4:27,8:1:9,3:4:9,2:3:9,-1:1:3,1:2:9,0:2:9");
        // Level 15 (+14 -> +15)
        addUpgrade((byte)15, (short)120, (byte)10, 40000, 20000, (short)30, (short)300, "8:4:30,8:1:12,3:4:10,2:3:10,-1:1:4,1:2:10,0:2:10");
        // Level 16 (+15 -> +16)
        addUpgrade((byte)16, (short)100, (byte)15, 60000, 30000, (short)50, (short)340, "8:18:18,8:1:12,3:18:6,2:4:12,-1:1:4,1:3:12,0:3:12");
        // Level 17 (+16 -> +17)
        addUpgrade((byte)17, (short)80, (byte)15, 80000, 40000, (short)70, (short)385, "8:18:24,8:1:15,3:18:8,2:4:14,-1:1:5,1:3:14,0:3:14");
        // Level 18 (+17 -> +18)
        addUpgrade((byte)18, (short)60, (byte)15, 100000, 50000, (short)90, (short)435, "8:18:30,8:1:15,3:18:10,2:4:16,-1:1:5,1:3:16,0:3:16");
        // Level 19 (+18 -> +19)
        addUpgrade((byte)19, (short)45, (byte)15, 120000, 60000, (short)120, (short)490, "8:18:36,8:1:18,3:18:12,2:4:18,-1:1:6,1:3:18,0:3:18");
        // Level 20 (+19 -> +20)
        addUpgrade((byte)20, (short)30, (byte)15, 150000, 75000, (short)150, (short)550, "8:18:42,8:1:21,3:18:14,2:4:20,-1:1:7,1:3:20,0:3:20");
    }

    private static void addUpgrade(byte level, short per, byte prelevel, int beri, int beri_white, short ruby, short att, String materialsStr) {
        DataUpgrade temp = new DataUpgrade();
        temp.level = level;
        temp.per = per;
        temp.prelevel = prelevel;
        temp.beri = beri;
        temp.beri_white = beri_white;
        temp.ruby = ruby;
        temp.att = att;
        
        String[] parts = materialsStr.split(",");
        temp.material = new UpgradeMaterialTemplate[parts.length];
        for (int j = 0; j < parts.length; j++) {
            String[] matInfo = parts[j].split(":");
            temp.material[j] = new UpgradeMaterialTemplate(
                    Byte.parseByte(matInfo[0].trim()),
                    Byte.parseByte(matInfo[1].trim()),
                    Short.parseShort(matInfo[2].trim())
            );
        }
        DATA.add(temp);
    }

    public static void show_table_upgrade(Player p) throws IOException {
        Message m = new Message(-48);
        m.writer().writeByte(7);
        p.addmsg(m);
        m.cleanup();
        p.item_upgrade_index = -1;
        p.tool_upgrade = new int[] {-1, -1};
    }

    public static void process(Player p, Message m2) throws IOException {
        if (p == null || p.item == null) return;
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể nâng cấp khi đang giao dịch!");
            return;
        }
        synchronized (p.item) {
            byte type = m2.reader().readByte();
            short id = m2.reader().readShort();
            byte bery_gem = m2.reader().readByte();
        if (type == 8 && id == 0 && bery_gem == 0) {
            p.getService().Send_UI_Shop(6);
        } else if (type == 7 && id == 0 && bery_gem == 0) {
            UpgradeItem.show_table_upgrade(p);
        } else if (type == 13 && id == 0 && bery_gem == 0) {
            Rebuild_Item.show_table(p, 4);
        } else if (type == 10 && id == 0 && bery_gem == 0) {
            Rebuild_Item.show_table(p, 2);
        } else if (type == 12 && id == 0 && bery_gem == 0) {
            Rebuild_Item.show_table(p, 1);
        } else if (type == 9 && id == 0 && bery_gem == 0) {
            Rebuild_Item.show_table(p, 3);
        } else if (type == 11 && id == 0 && bery_gem == 0) {
            p.getService().Send_UI_Shop(111);
        } else if (type == 4 && bery_gem == 0) { // bo item3 vao
            Item_wear it = (id >= 0 && id < p.item.bag3.length) ? p.item.bag3[id] : null;
            if (it != null && it.template != null) {
                boolean isTT = it.isThanTrang();
                if (!isTT && it.template.typeEquip > 5) {
                    p.getService().send_box_ThongBao_OK("Vật phẩm không hợp lệ!");
                    return;
                }
                int maxLevel = isTT ? 20 : 10;
                if (it.levelUp < maxLevel) {
                    p.item_upgrade_index = id;
                    p.tool_upgrade = new int[] {-1, -1};
                    Message m = new Message(-48);
                    m.writer().writeByte(4);
                    m.writer().writeShort(id);
                    p.addmsg(m);
                    m.cleanup();

                    if (isTT) {
                        Message m5 = new Message(-48);
                        m5.writer().writeByte(5);
                        m5.writer().writeByte(0);
                        m5.writer().writeShort(0);
                        p.addmsg(m5);
                        m5.cleanup();

                        Message m6 = new Message(-48);
                        m6.writer().writeByte(6);
                        m6.writer().writeByte(0);
                        m6.writer().writeShort(0);
                        p.addmsg(m6);
                        m6.cleanup();
                    }
                } else {
                    notice_upgrade(p, 0, "Trang bị đã cường hóa cấp tối đa (+" + maxLevel + ")!");
                }
            }
        } else if (type == 1 && bery_gem == 0) { // ask upgrade
            Item_wear it = (id >= 0 && id < p.item.bag3.length) ? p.item.bag3[id] : null;
            if (it != null && it.template != null) {
                boolean isTT = it.isThanTrang();
                if (!isTT && it.template.typeEquip > 5) {
                    notice_upgrade(p, 0, "Trang bị này không thể nâng cấp!");
                    return;
                }
                int maxLevel = isTT ? 20 : 10;
                if (it.levelUp >= maxLevel) {
                    notice_upgrade(p, 0, "Vật phẩm đã nâng cấp tối đa (+" + maxLevel + ")!");
                    return;
                }
                int currentLv = it.levelUp;
                if (currentLv >= UpgradeItem.DATA.size()) {
                    notice_upgrade(p, 0, "Đã đạt cấp tối đa!");
                    return;
                }
                DataUpgrade du = UpgradeItem.DATA.get(currentLv);
                int[] matReq = get_material(currentLv, it.getColor());
                String matInfo = "";
                String mat0Name = (matReq[0] > 0 && ItemTemplate7.get_it_by_id(matReq[0]) != null) ? ItemTemplate7.get_it_by_id(matReq[0]).name : "Bột cường hóa";
                String mat1Name = (matReq[2] > 0 && ItemTemplate7.get_it_by_id(matReq[2]) != null) ? ItemTemplate7.get_it_by_id(matReq[2]).name : "Nguyên liệu";
                if (matReq[0] > 0 && matReq[2] > 0) {
                    matInfo = " (Cần " + matReq[1] + "x " + mat0Name + ", " + matReq[3] + "x " + mat1Name + ")";
                } else if (matReq[2] > 0) {
                    matInfo = " (Cần " + matReq[3] + "x " + mat1Name + ")";
                }

                Message m = new Message(-48);
                m.writer().writeByte(1);
                m.writer().writeUTF("Nâng cấp " + it.template.name + " lên +" + (it.levelUp + 1) + matInfo);
                m.writer().writeInt(du.beri);
                m.writer().writeShort(du.ruby);
                m.writer().writeShort(id);
                p.addmsg(m);
                m.cleanup();
            }
        } else if (type == 2) { // start upgrade (beri gem 1 : beri, 2: ruby)
            Item_wear it = (id >= 0 && id < p.item.bag3.length) ? p.item.bag3[id] : null;
            if (it != null && it.template != null) {
                boolean isTT = it.isThanTrang();
                if (!isTT && it.template.typeEquip > 5) {
                    p.getService().send_box_ThongBao_OK("Trang bị này không thể nâng cấp!");
                    return;
                }
                int maxLevel = isTT ? 20 : 10;
                if (it.levelUp >= maxLevel) {
                    notice_upgrade(p, 0, "Vật phẩm đã nâng cấp tối đa (+" + maxLevel + ")!");
                    return;
                }
                int currentLv = it.levelUp;
                if (currentLv >= UpgradeItem.DATA.size()) {
                    notice_upgrade(p, 0, "Đã đạt cấp tối đa!");
                    return;
                }

                if (isTT) {
                    p.tool_upgrade = new int[] {-1, -1};
                }

                DataUpgrade dataUpgrade = UpgradeItem.DATA.get(currentLv);
                int[] material_req = get_material(currentLv, it.getColor());

                int req0 = material_req[0];
                int quant0 = material_req[1];
                int req1 = material_req[2];
                int quant1 = material_req[3];

                int totalNeedReq0 = (req0 > 0 && quant0 > 0) ? quant0 : 0;
                int totalNeedReq1 = (req1 > 0 && quant1 > 0) ? quant1 : 0;

                if (req0 > 0 && req0 == req1) {
                    int totalBoth = totalNeedReq0 + totalNeedReq1;
                    if (p.item.total_item_bag_by_id(7, req0) < totalBoth) {
                        String matName = ItemTemplate7.get_it_by_id(req0) != null ? ItemTemplate7.get_it_by_id(req0).name : "Nguyên liệu";
                        notice_upgrade(p, 0, "Không đủ " + totalBoth + " " + matName + "!");
                        return;
                    }
                } else {
                    if (req0 > 0 && totalNeedReq0 > 0) {
                        if (p.item.total_item_bag_by_id(7, req0) < totalNeedReq0) {
                            String matName = ItemTemplate7.get_it_by_id(req0) != null ? ItemTemplate7.get_it_by_id(req0).name : "Bột cường hóa";
                            notice_upgrade(p, 0, "Không đủ " + totalNeedReq0 + " " + matName + "!");
                            return;
                        }
                    }
                    if (req1 > 0 && totalNeedReq1 > 0) {
                        if (p.item.total_item_bag_by_id(7, req1) < totalNeedReq1) {
                            String matName = ItemTemplate7.get_it_by_id(req1) != null ? ItemTemplate7.get_it_by_id(req1).name : "Đá/Bột nâng cấp";
                            notice_upgrade(p, 0, "Không đủ " + totalNeedReq1 + " " + matName + "!");
                            return;
                        }
                    }
                }

                int beriCost = dataUpgrade.beri;
                int rubyCost = dataUpgrade.ruby;

                if (bery_gem == 1) {
                    if (p.get_vang() < beriCost) {
                        notice_upgrade(p, 0, "Không đủ " + ZUtil.number_format(beriCost) + " Beri!");
                        return;
                    }
                    p.update_vang(-beriCost);
                } else {
                    if (p.get_ngoc() < rubyCost) {
                        notice_upgrade(p, 0, "Không đủ " + rubyCost + " Ruby!");
                        return;
                    }
                    p.update_ngoc(-rubyCost);
                }

                // Tiêu hao nguyên liệu
                if (req0 > 0 && req0 == req1) {
                    p.item.remove_item47(7, req0, totalNeedReq0 + totalNeedReq1);
                } else {
                    if (req0 > 0 && totalNeedReq0 > 0) {
                        p.item.remove_item47(7, req0, totalNeedReq0);
                    }
                    if (req1 > 0 && totalNeedReq1 > 0) {
                        p.item.remove_item47(7, req1, totalNeedReq1);
                    }
                }

                if (!isTT) {
                    // Kiểm tra tools
                    for (int i = 0; i < 2; i++) {
                        if (p.tool_upgrade[i] != -1 && p.item.total_item_bag_by_id(7, p.tool_upgrade[i]) < 1) {
                            p.tool_upgrade[i] = -1;
                        }
                    }
                }

                int percent = dataUpgrade.per;
                if (isTT) {
                    percent = Math.max(1, percent / 5);
                } else {
                    if (p.tool_upgrade[1] != -1) {
                        if (p.tool_upgrade[1] == 5) { // Ngôi sao may mắn
                            percent = (percent * 15) / 10;
                        } else if (p.tool_upgrade[1] == 11) { // Thiên thạch may mắn
                            percent = (percent * 2);
                        }
                    }
                }

                boolean suc = (percent >= 1000) || (percent > ZUtil.random(1000));
                if (suc) {
                    it.levelUp++;
                    p.updateArchiDaily(6);
                    if (it.levelUp >= 15) {
                        Manager.gI().chatKTG(0, "Chúc mừng người chơi [" + p.name + "] đã cường hóa thành công " + (it.isThanTrang() ? "Thần Trang " : "") + it.template.name + " lên +" + it.levelUp + "!", 5);
                    }
                    notice_upgrade(p, 2, "Cường hóa thành công lên +" + it.levelUp);
                } else {
                    if (isTT) {
                        // Thần Trang tuyệt đối không có bảo hiểm, chỉ các mốc an toàn mới không rớt cấp
                        if (!isSafeLevel(it, it.levelUp)) {
                            it.levelUp = (byte) Math.max(0, it.levelUp - 1);
                        }
                    } else {
                        if (p.tool_upgrade[0] == -1) { // Không có bảo hiểm
                            if (!isSafeLevel(it, it.levelUp)) {
                                it.levelUp = (byte) Math.max(0, it.levelUp - 1);
                            }
                        }
                    }
                    notice_upgrade(p, 3, "Cường hóa thất bại!");
                }

                if (!isTT) {
                    // Tiêu hao tool
                    if (p.tool_upgrade[0] != -1) {
                        p.item.remove_item47(7, p.tool_upgrade[0], 1);
                        if (p.item.total_item_bag_by_id(7, p.tool_upgrade[0]) < 1) p.tool_upgrade[0] = -1;
                    }
                    if (p.tool_upgrade[1] != -1) {
                        p.item.remove_item47(7, p.tool_upgrade[1], 1);
                        if (p.item.total_item_bag_by_id(7, p.tool_upgrade[1]) < 1) p.tool_upgrade[1] = -1;
                    }
                }

                p.item.updateInventory(false);
                p.setAbility();
                p.update_info_to_all();
                p.updateMoney();
            }
        } else if (type == 6 && (bery_gem == 1 || bery_gem == 0)) { // mai rua
            if (bery_gem == 1) {
                if (p.item_upgrade_index >= 0 && p.item_upgrade_index < p.item.bag3.length) {
                    Item_wear currentEquip = p.item.bag3[p.item_upgrade_index];
                    if (currentEquip != null && currentEquip.isThanTrang()) {
                        p.getService().send_box_ThongBao_OK("Thần Trang không thể sử dụng vật phẩm hỗ trợ hay bảo hộ!");
                        return;
                    }
                }
            }
            if (bery_gem == 0 || p.item.total_item_bag_by_id(7, id) > 0) {
                Message m5 = new Message(-48);
                m5.writer().writeByte(6);
                m5.writer().writeByte(bery_gem); // is use
                m5.writer().writeShort(id);
                p.addmsg(m5);
                m5.cleanup();
                p.tool_upgrade[0] = (bery_gem == 0) ? -1 : id;
            }
        } else if (type == 5 && (bery_gem == 1 || bery_gem == 0)) { // ngoi sao may man
            if (bery_gem == 1) {
                if (p.item_upgrade_index >= 0 && p.item_upgrade_index < p.item.bag3.length) {
                    Item_wear currentEquip = p.item.bag3[p.item_upgrade_index];
                    if (currentEquip != null && currentEquip.isThanTrang()) {
                        p.getService().send_box_ThongBao_OK("Thần Trang không thể sử dụng vật phẩm hỗ trợ hay bảo hộ!");
                        return;
                    }
                }
            }
            if (bery_gem == 0 || p.item.total_item_bag_by_id(7, id) > 0) {
                Message m5 = new Message(-48);
                m5.writer().writeByte(5);
                m5.writer().writeByte(bery_gem); // is use
                m5.writer().writeShort(id);
                p.addmsg(m5);
                m5.cleanup();
                p.tool_upgrade[1] = (bery_gem == 0) ? -1 : id;
            }
        } else if (type == 15 && id == -1 && bery_gem == 0) { // heart upgrade
            if (p.item.it_heart != null && p.item.it_heart.levelUp < 110) {
                model.YesNoDialog ynd = new model.YesNoDialog(p, 28, "Auto Phẫu Thuật Tim",
                    "Chọn số lần Auto Phẫu Thuật Tim (Cấp hiện tại: " + p.item.it_heart.levelUp + "/110, Chế tác: " + p.item.it_heart.valueChetac + "/100):",
                    new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"},
                    new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, 1}, value -> {
                        if (value == 7) {
                            new model.InputDialog(p, 28, "Nhập số lần", new String[]{"Số lần muốn phẫu thuật:"}, inputs -> {
                                if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                try {
                                    int count = Integer.parseInt(inputs[0].trim());
                                    if (count <= 0) {
                                        p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                        return;
                                    }
                                    executeAutoHeartSurgery(p, false, count);
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
                            executeAutoHeartSurgery(p, false, count);
                        }
                    });
                ynd.startYesNo();
            } else {
                p.getService().send_box_ThongBao_OK(p.item.it_heart == null ? "Bạn chưa có tim!" : "Tim đã đạt cấp tối đa (+110)!");
            }
        }
        }
    }

    public static boolean isSafeLevel(Item_wear it, int level) {
        if (it != null && it.isThanTrang()) {
            return level <= 1 || level == 4 || level == 8 || level == 10 || level == 14 || level == 16 || level >= 20;
        }
        return level <= 1 || level == 4 || level == 8 || level >= 10;
    }

    public static boolean isSafeLevel(int level) {
        return level <= 1 || level == 4 || level == 8 || level >= 10;
    }

    public static void openSelectUpgradeItemDialog(Player p) throws IOException {
        if (p == null || p.item == null) return;
        List<Item_wear> equipList = new ArrayList<>();
        List<Short> bagIndices = new ArrayList<>();
        List<String> names = new ArrayList<>();

        if (p.item.it_body != null) {
            for (int i = 0; i < p.item.it_body.length; i++) {
                Item_wear it = p.item.it_body[i];
                if (it != null && it.template != null && !it.isThanTrang() && it.template.typeEquip <= 5 && it.levelUp < 10) {
                    equipList.add(it);
                    bagIndices.add((short) -1);
                    names.add("[Đang Mặc] " + it.template.name + " (+" + it.levelUp + ")");
                }
            }
        }

        if (p.item.bag3 != null) {
            for (int i = 0; i < p.item.bag3.length; i++) {
                Item_wear it = p.item.bag3[i];
                if (it != null && it.template != null && !it.isThanTrang() && it.template.typeEquip <= 5 && it.levelUp < 10) {
                    equipList.add(it);
                    bagIndices.add((short) i);
                    names.add(it.template.name + " (+" + it.levelUp + ")");
                }
            }
        }

        if (equipList.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Bạn không có trang bị thường nào chưa đạt cấp tối đa (+10) để cường hóa!");
            return;
        }

        names.add("Đóng");
        byte[] types = new byte[names.size()];
        for (int i = 0; i < types.length - 1; i++) types[i] = -1;
        types[types.length - 1] = 1;

        model.YesNoDialog ynd = new model.YesNoDialog(p, 68, "CƯỜNG HÓA ĐỒ THƯỜNG",
                "Chọn trang bị thường bạn muốn Cường Hóa (+0 -> +10):",
                names.toArray(new String[0]), types, (byte value) -> {
            if (value >= 0 && value < equipList.size()) {
                Item_wear selected = equipList.get(value);
                short bagIndex = bagIndices.get(value);
                if (selected != null) {
                    openAutoUpgradeItemDialog(p, selected, bagIndex);
                }
            }
        });
        ynd.startYesNo();
    }

    public static void openAutoUpgradeItemDialog(Player p, Item_wear it, short bagIndex) {
        if (p == null || it == null || it.template == null) return;
        if (it.isThanTrang() || it.template.typeEquip > 5) {
            try { p.getService().send_box_ThongBao_OK("Trang bị này không thể cường hóa tại đây!"); } catch (Exception ignored) {}
            return;
        }
        if (it.levelUp >= 10) {
            try { p.getService().send_box_ThongBao_OK("Trang bị " + it.template.name + " đã đạt cấp tối đa (+10)!"); } catch (Exception ignored) {}
            return;
        }

        int currentLv = it.levelUp;
        if (currentLv >= UpgradeItem.DATA.size()) {
            try { p.getService().send_box_ThongBao_OK("Đã đạt cấp tối đa!"); } catch (Exception ignored) {}
            return;
        }

        DataUpgrade du = UpgradeItem.DATA.get(currentLv);
        int[] matReq = get_material(currentLv, it.getColor());
        String mat1Name = (matReq[0] > 0 && ItemTemplate7.get_it_by_id(matReq[0]) != null) ? ItemTemplate7.get_it_by_id(matReq[0]).name : "Bột Cường Hóa";
        int ownedMat1 = matReq[0] > 0 ? p.item.total_item_bag_by_id(7, matReq[0]) : 0;

        String mat2Name = (matReq[2] > 0 && ItemTemplate7.get_it_by_id(matReq[2]) != null) ? ItemTemplate7.get_it_by_id(matReq[2]).name : "Đá Nâng Cấp";
        int ownedMat2 = matReq[2] > 0 ? p.item.total_item_bag_by_id(7, matReq[2]) : 0;

        int ownedMaiRua = p.item.total_item_bag_by_id(7, 6);
        int ownedStar = p.item.total_item_bag_by_id(7, 5);
        int ownedThienThach = p.item.total_item_bag_by_id(7, 11);

        String info = "CƯỜNG HÓA TRANG BỊ THƯỜNG\n"
                + "Trang bị: " + it.template.name + " (+" + it.levelUp + " -> +" + (it.levelUp + 1) + ")\n"
                + "Yêu cầu mỗi lần:\n"
                + "• Tiền: " + ZUtil.number_format(du.beri) + " Beri (hoặc " + du.ruby + " Ruby)\n"
                + "• " + matReq[1] + "x " + mat1Name + " (Hiện có: " + ownedMat1 + ")\n"
                + (matReq[2] > 0 ? ("• " + matReq[3] + "x " + mat2Name + " (Hiện có: " + ownedMat2 + ")\n") : "")
                + "• Bùa: Mai Rùa (" + ownedMaiRua + ") | Ngôi Sao (" + ownedStar + ") | Thiên Thạch (" + ownedThienThach + ")\n"
                + "Chọn chế độ Auto:";

        boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
        String[] options = !isTest
                ? new String[]{
                    "Cường hóa Beri (1 lần)",
                    "Cường hóa Ruby (1 lần)",
                    "Auto Beri (5 lần)",
                    "Auto Beri (10 lần)",
                    "Auto Ruby (5 lần)",
                    "Auto Ruby (10 lần)",
                    "Hủy"
                }
                : new String[]{
                    "Cường hóa Beri (1 lần)",
                    "Cường hóa Ruby (1 lần)",
                    "Auto Beri (10 lần)",
                    "Auto Beri (50 lần)",
                    "Auto Beri lên +10 (Max)",
                    "Auto Ruby lên +10 (Max)",
                    "Nhập số lần",
                    "Hủy"
                };
        byte[] types = new byte[options.length];
        for (int i = 0; i < types.length - 1; i++) types[i] = -1;
        types[types.length - 1] = 1;

        model.YesNoDialog ynd = new model.YesNoDialog(p, 69, "Auto Cường Hóa Đồ Thường", info, options, types, value -> {
            if (!isTest) {
                int count = 1;
                byte bery_gem = 1;
                switch (value) {
                    case 0: count = 1; bery_gem = 1; break;
                    case 1: count = 1; bery_gem = 2; break;
                    case 2: count = 5; bery_gem = 1; break;
                    case 3: count = 10; bery_gem = 1; break;
                    case 4: count = 5; bery_gem = 2; break;
                    case 5: count = 10; bery_gem = 2; break;
                    default: return;
                }
                executeAutoUpgradeDirectItem(p, it, bery_gem, count, 10);
                return;
            }

            if (value == 6) {
                new model.InputDialog(p, 69, "Nhập số lần", new String[]{"Số lần muốn Auto Cường Hóa (1-500):", "Dùng tiền (1=Beri, 2=Ruby):"}, inputs -> {
                    if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                    try {
                        int count = Integer.parseInt(inputs[0].trim());
                        byte bery_gem = (byte) ((inputs.length > 1 && inputs[1].trim().equals("2")) ? 2 : 1);
                        if (count <= 0 || count > 500) {
                            p.getService().send_box_ThongBao_OK("Số lần không hợp lệ (1 - 500)!");
                            return;
                        }
                        executeAutoUpgradeDirectItem(p, it, bery_gem, count, 10);
                    } catch (Exception e) {
                        p.getService().send_box_ThongBao_OK("Vui lòng nhập đúng định dạng số!");
                    }
                }).startInput();
                return;
            }

            int count = 1;
            byte bery_gem = 1;
            int targetCap = 10;
            switch (value) {
                case 0: count = 1; bery_gem = 1; break;
                case 1: count = 1; bery_gem = 2; break;
                case 2: count = 10; bery_gem = 1; break;
                case 3: count = 50; bery_gem = 1; break;
                case 4: count = 500; bery_gem = 1; targetCap = 10; break;
                case 5: count = 500; bery_gem = 2; targetCap = 10; break;
                default: return;
            }
            executeAutoUpgradeDirectItem(p, it, bery_gem, count, targetCap);
        });
        ynd.startYesNo();
    }

    public static void executeAutoUpgradeItem(Player p, short id, byte bery_gem, int loopCount) {
        if (p == null || p.item == null || id < 0 || id >= p.item.bag3.length) return;
        Item_wear it = p.item.bag3[id];
        executeAutoUpgradeDirectItem(p, it, bery_gem, loopCount, 10);
    }

    public static void executeAutoUpgradeDirectItem(Player p, Item_wear it, byte bery_gem, int loopCount, int targetCap) {
        if (p == null || p.item == null || it == null || it.template == null) return;
        if (it.isThanTrang() || (it.template != null && it.template.typeEquip >= 8)) {
            try { p.getService().send_box_ThongBao_OK("Vui lòng vào Menu [Thần Trang] -> [Cường Hóa Thần Trang]!"); } catch (Exception ignored) {}
            return;
        }
        if (it.template.typeEquip > 5) {
            try { p.getService().send_box_ThongBao_OK("Trang bị này không thể cường hóa!"); } catch (Exception ignored) {}
            return;
        }
        int maxLevel = Math.min(10, targetCap);
        if (it.levelUp >= maxLevel) {
            try { p.getService().send_box_ThongBao_OK("Trang bị đã đạt cấp tối đa (+" + maxLevel + ")!"); } catch (Exception ignored) {}
            return;
        }

        int startLevel = it.levelUp;
        int successCount = 0;
        int failCount = 0;
        long totalBeriSpent = 0;
        int totalRubySpent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần cường hóa";

        for (int step = 0; step < loopCount; step++) {
            if (it.levelUp >= maxLevel) {
                stopReason = "Đạt cấp tối đa (+" + maxLevel + ")!";
                break;
            }

            int[] material_req = get_material(it.levelUp, it.getColor());
            if (material_req[0] <= -1) {
                stopReason = "Lỗi dữ liệu nguyên liệu!";
                break;
            }

            if (material_req[0] > 0 && material_req[1] > 0) {
                if (p.item.total_item_bag_by_id(7, material_req[0]) < material_req[1]) {
                    String matName = ItemTemplate7.get_it_by_id(material_req[0]) != null ? ItemTemplate7.get_it_by_id(material_req[0]).name : "Bột cường hóa";
                    stopReason = "Hết nguyên liệu " + matName + "!";
                    break;
                }
            }
            if (material_req[2] > 0 && material_req[3] > 0) {
                if (p.item.total_item_bag_by_id(7, material_req[2]) < material_req[3]) {
                    String matName = ItemTemplate7.get_it_by_id(material_req[2]) != null ? ItemTemplate7.get_it_by_id(material_req[2]).name : "Đá/Bột nâng cấp";
                    stopReason = "Hết nguyên liệu " + matName + "!";
                    break;
                }
            }

            DataUpgrade dataUpgrade = UpgradeItem.DATA.get(it.levelUp);
            if (bery_gem == 1) {
                int beriCost = dataUpgrade.beri;
                if (p.get_vang() < beriCost) {
                    stopReason = "Không đủ " + ZUtil.number_format(beriCost) + " beri!";
                    break;
                }
                p.update_vang(-beriCost);
                totalBeriSpent += beriCost;
            } else {
                int rubyCost = dataUpgrade.ruby;
                if (p.get_ngoc() < rubyCost) {
                    stopReason = "Không đủ " + rubyCost + " ruby!";
                    break;
                }
                p.update_ngoc(-rubyCost);
                totalRubySpent += rubyCost;
            }

            if (material_req[0] > 0 && material_req[1] > 0) {
                p.item.remove_item47(7, material_req[0], material_req[1]);
            }
            if (material_req[2] > 0 && material_req[3] > 0) {
                p.item.remove_item47(7, material_req[2], material_req[3]);
            }

            // Check tools: Mai rùa (ID 6), Ngôi sao (ID 5), Thiên thạch (ID 11)
            boolean hasMaiRua = p.item.total_item_bag_by_id(7, 6) >= 1;
            boolean hasThienThach = p.item.total_item_bag_by_id(7, 11) >= 1;
            boolean hasNgoiSao = !hasThienThach && (p.item.total_item_bag_by_id(7, 5) >= 1);

            int percent = dataUpgrade.per;
            if (hasThienThach) {
                percent = percent * 2;
                p.item.remove_item47(7, 11, 1);
            } else if (hasNgoiSao) {
                percent = (percent * 15) / 10;
                p.item.remove_item47(7, 5, 1);
            }

            boolean suc = (percent >= 1000) || (percent > ZUtil.random(1000));
            if (suc) {
                it.levelUp++;
                successCount++;
                p.updateArchiDaily(6);
            } else {
                failCount++;
                if (isSafeLevel(it.levelUp)) {
                    // Mốc an toàn (4, 8, 10): không rớt cấp
                } else {
                    if (hasMaiRua) {
                        p.item.remove_item47(7, 6, 1); // Bảo hiểm mai rùa không giảm cấp
                    } else {
                        it.levelUp = (byte) Math.max(0, it.levelUp - 1);
                    }
                }
            }
        }

        try {
            p.updateMoney();
            p.item.updateInventory(false);
            p.getService().charWearing(p, false);
            p.setAbility();
            p.update_info_to_all();

            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO CƯỜNG HÓA:\n" +
                "• Trang bị: " + it.template.name + "\n" +
                "• Cấp độ: +" + startLevel + " -> +" + it.levelUp + "\n" +
                "• Thành công: " + successCount + " lần | Thất bại: " + failCount + " lần\n" +
                "• Tổng tiêu hao: " + (totalBeriSpent > 0 ? (ZUtil.number_format(totalBeriSpent) + " Beri ") : "") + (totalRubySpent > 0 ? (totalRubySpent + " Ruby") : "") + "\n" +
                "• Trạng thái: " + stopReason
            );
        } catch (Exception ignored) {}
    }

    public static void executeAutoHeartSurgery(Player p, boolean isDeTu, int loopCount) {
        if (p == null) return;
        Item_wear heart = isDeTu ? (p.detu != null ? p.detu.item.it_heart : null) : (p.item != null ? p.item.it_heart : null);
        if (heart == null) {
            try { p.getService().send_box_ThongBao_OK(isDeTu ? "Đệ tử chưa có tim!" : "Bạn chưa có tim!"); } catch (Exception ignored) {}
            return;
        }
        int maxLevel = isDeTu ? 99 : 110;
        if (heart.levelUp >= maxLevel) {
            try { p.getService().send_box_ThongBao_OK("Tim đã đạt cấp tối đa (+" + maxLevel + ")!"); } catch (Exception ignored) {}
            return;
        }

        int startLevel = heart.levelUp;
        int successCount = 0;
        int failCount = 0;
        long totalBeriSpent = 0;
        int totalRubySpent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần phẫu thuật";

        for (int step = 0; step < loopCount; step++) {
            if (heart.levelUp >= maxLevel) {
                stopReason = "Đã đạt cấp tối đa (+" + maxLevel + ")!";
                break;
            }

            long vang_req = 1_200_000L + heart.levelUp * 200_000L;
            int ruby_req = 0;
            if ((heart.levelUp % 5) == 4) {
                ruby_req = ((heart.levelUp / 5) + 1) * 10;
                if (!isDeTu) {
                    if (heart.levelUp == 104) {
                        ruby_req = 400;
                    } else if (heart.levelUp == 109) {
                        ruby_req = 600;
                    }
                }
            }

            if (p.get_vang() < vang_req) {
                stopReason = "Không đủ " + ZUtil.number_format(vang_req) + " beri!";
                break;
            }
            if (p.get_ngoc() < ruby_req) {
                stopReason = "Không đủ " + ruby_req + " ruby!";
                break;
            }

            p.update_vang(-vang_req);
            p.update_ngoc(-ruby_req);
            totalBeriSpent += vang_req;
            totalRubySpent += ruby_req;

            boolean suc = (530 - (heart.levelUp * 5)) > ZUtil.random(1200) || heart.valueChetac >= 100;
            if (suc) {
                heart.levelUp++;
                heart.valueChetac = 0;
                if (!isDeTu && heart.levelUp == 100) {
                    boolean has79 = false;
                    for (Option op : heart.option_item) {
                        if (op != null && op.id == 79) {
                            has79 = true;
                            break;
                        }
                    }
                    if (!has79) {
                        heart.option_item.add(0, new Option(79, 2));
                    }
                }
                successCount++;
            } else {
                failCount++;
                if (!isDeTu && heart.levelUp >= 100) {
                    heart.valueChetac += 5;
                } else {
                    heart.valueChetac += 10;
                }
                if (heart.valueChetac > 100) {
                    heart.valueChetac = 100;
                }
            }
        }

        try {
            p.updateMoney();
            if (isDeTu && p.detu != null) {
                UpgradeItem.send_heart_info(p.detu, false);
                p.detu.update_info_to_all();
            } else {
                UpgradeItem.send_heart_info(p, false);
                p.update_info_to_all();
            }

            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO PHẪU THUẬT TIM " + (isDeTu ? "(ĐỆ TỬ)" : "") + ":\n" +
                "- Cấp độ: +" + startLevel + " -> +" + heart.levelUp + "\n" +
                "- Điểm chế tác: " + heart.valueChetac + "/100\n" +
                "- Thành công: " + successCount + " lần | Thất bại: " + failCount + " lần\n" +
                "- Tổng tiêu hao: " + ZUtil.number_format(totalBeriSpent) + " Beri | " + ZUtil.number_format(totalRubySpent) + " Ruby\n" +
                "- Trạng thái: " + stopReason
            );
        } catch (Exception ignored) {}
    }

    public static int[] get_material(int level, int color) {
        int[] result = new int[] {-1, -1, -1, -1};
        if (level < 0 || level >= UpgradeItem.DATA.size()) {
            return result;
        }
        DataUpgrade temp = UpgradeItem.DATA.get(level);
        if (color == 8) {
            int count8 = 0;
            for (int i = 0; i < temp.material.length; i++) {
                if (temp.material[i].type == 8) {
                    if (count8 == 0) {
                        result[2] = temp.material[i].id;
                        result[3] = temp.material[i].quant;
                        count8++;
                    } else if (count8 == 1) {
                        result[0] = temp.material[i].id;
                        result[1] = temp.material[i].quant;
                        count8++;
                    }
                }
            }
            return result;
        }
        int effectiveColor = (color >= 3 && color < 8) ? 3 : color;
        for (int i = 0; i < temp.material.length; i++) {
            if (temp.material[i].type == -1) {
                result[0] = temp.material[i].id;
                result[1] = temp.material[i].quant;
            }
            if (temp.material[i].type == effectiveColor) {
                result[2] = temp.material[i].id;
                result[3] = temp.material[i].quant;
            }
        }
        return result;
    }

    private static void notice_upgrade(Player p, int type, String s) throws IOException {
        Message m = new Message(-48);
        m.writer().writeByte(type);
        m.writer().writeUTF(s);
        p.addmsg(m);
        m.cleanup();
    }

    public static void show_table_upgrade_heart(Player p) throws IOException {
        UpgradeItem.send_heart_info(p, false);
        //
        Message m = new Message(-48);
        m.writer().writeByte(15);
        p.addmsg(m);
        m.cleanup();
    }

    public static void send_heart_info(Player p, boolean b) throws IOException {
        if (p != null && p.getService() != null) {
            p.getService().charWearing(p, false);
        }
    }

    public static void show_eff_get_heart(Player p) throws IOException {
        Message m = new Message(-15);
        m.writer().writeByte(24);
        m.writer().writeShort(p.index_map);
        m.writer().writeByte(0);
        m.writer().writeShort(-1);
        p.addmsg(m);
        m.cleanup();
    }
}
