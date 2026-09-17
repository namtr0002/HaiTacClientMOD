package template;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemTemplate3 {
    public static List<ItemTemplate3> ENTRYS = new ArrayList<>();
    public short id;
    public String name = "";
    public byte clazz;
    public byte typeEquip;
    public short icon;
    public short level;
    public byte color;
    public byte typelock;
    public byte numHoleDaDuc;
    public short valueChetac;
    public byte isHoanMy;
    public byte valueKichAn = -1;
    public List<Option> option_item;
    public List<Option> option_item_2;
    public byte numLoKham;
    public short[] mdakham;
    public short part;
    public int beri;
    public int ruby;

    public static ItemTemplate3 get_it_by_id(int id) {
        for (int i = 0; i < ItemTemplate3.ENTRYS.size(); i++) {
            if (ItemTemplate3.ENTRYS.get(i).id == id) {
                return ItemTemplate3.ENTRYS.get(i);
            }
        }
        return null;
    }

    public boolean isThanTrang() {
        return ThanTrangConfig.isThanTrang(this.id)
                || (this.typeEquip >= 8 && this.typeEquip <= 15)
                || (this.id >= 2604 && this.id <= 2693);
    }

    public static boolean isThanTrang(int templateId, int typeEquip) {
        if (ThanTrangConfig.isThanTrang(templateId)) return true;
        if (typeEquip >= 8 && typeEquip <= 15) return true;
        if (templateId >= 2604 && templateId <= 2693) return true;
        ItemTemplate3 temp = get_it_by_id(templateId);
        if (temp != null) {
            return (temp.typeEquip >= 8 && temp.typeEquip <= 15) || (temp.id >= 2604 && temp.id <= 2693) || ThanTrangConfig.isThanTrang(temp.id);
        }
        return false;
    }

    public static void readUpdateItem(DataOutputStream dos, ItemTemplate3 it) throws IOException {
        dos.writeShort(it.id);
        dos.writeUTF(it.name != null ? it.name : "");
        dos.writeByte(it.clazz);
        dos.writeByte(it.typeEquip);
        dos.writeShort(it.icon);
        dos.writeShort(it.level);
        dos.writeByte(0); // level up
        dos.writeByte(it.color);
        dos.writeByte(0); // is trade
        dos.writeByte(it.typelock);
        dos.writeByte(it.numHoleDaDuc);
        dos.writeInt(0); // time use
        dos.writeShort(it.valueChetac);
        dos.writeByte(it.isHoanMy);
        dos.writeByte(-1);
        dos.writeByte(it.option_item.size());
        for (int i = 0; i < it.option_item.size(); i++) {
            Option op = it.option_item.get(i);
            if (op != null) {
                dos.writeByte(op.id);
                int val = op.getParam();
                if (val < 0) val = 0;
                if (val > 0xFFFF) val = 0xFFFF;
                dos.writeShort(val);
            } else {
                dos.writeByte(0);
                dos.writeShort(0);
            }
        }
        dos.writeByte(it.option_item_2.size());
        for (int i = 0; i < it.option_item_2.size(); i++) {
            Option op2 = it.option_item_2.get(i);
            if (op2 != null) {
                dos.writeByte(op2.id);
                int val2 = op2.getParam();
                if (val2 < 0) val2 = 0;
                if (val2 > 0xFFFF) val2 = 0xFFFF;
                dos.writeShort(val2);
            } else {
                dos.writeByte(0);
                dos.writeShort(0);
            }
        }
        dos.writeByte(it.numLoKham);
        dos.writeByte(it.mdakham.length);
        for (int i = 0; i < it.mdakham.length; i++) {
                dos.writeShort(it.mdakham[i]);
        }
    }

    public static ItemTemplate3 get_item_random(byte typeEquip, byte clazz, int bound1, int bound2) {
        for (int i = bound1; i < bound2; i++) {
            if (ItemTemplate3.ENTRYS.get(i).typeEquip == typeEquip && ItemTemplate3.ENTRYS.get(i).clazz == clazz) {
                return ItemTemplate3.ENTRYS.get(i);
            }
        }
        return null;
    }

    public void initDefaultOptionsFromCode() {
        if (this.option_item == null) {
            this.option_item = new java.util.ArrayList<>();
        }
        if (this.option_item_2 == null) {
            this.option_item_2 = new java.util.ArrayList<>();
        }

        // Tự động gán cấu hình chuẩn cho 16 Bộ Thần Trang (Set 1..16, ID 2598..2693)
        boolean isTT = this.isThanTrang();
        if (isTT) {
            this.color = 8;
            this.isHoanMy = 0;
            this.valueKichAn = -1;
            this.numLoKham = 0;
            this.numHoleDaDuc = 0;
            this.valueChetac = 0;
            this.typeEquip = ThanTrangConfig.getTypeEquip(this.id);
            this.option_item.clear();
            this.option_item_2.clear();

            List<Option> defaultOps = ThanTrangConfig.getThanTrangOptions(this.id);
            if (!defaultOps.isEmpty()) {
                for (Option op : defaultOps) {
                    addOptionItem(op.id, op.getParam());
                }
            }
            return;
        }

        switch (this.id) {
            case 2548:
                addOptionItem(8, 4);
                addOptionItem(13, 39);
                addOptionItem(15, 28);
                addOptionItem(22, 3);
                break;
            case 2549:
                addOptionItem(6, 4);
                addOptionItem(14, 39);
                addOptionItem(15, 36);
                addOptionItem(17, 109);
                break;
            case 2550:
                addOptionItem(1, 737);
                addOptionItem(2, 505);
                addOptionItem(4, 66);
                addOptionItem(10, 49);
                addOptionItem(19, 14);
                break;
            case 2551:
                addOptionItem(1, 923);
                addOptionItem(2, 350);
                addOptionItem(14, 49);
                addOptionItem(20, 5);
                break;
            case 2552:
                addOptionItem(1, 923);
                addOptionItem(2, 362);
                addOptionItem(13, 49);
                addOptionItem(20, 5);
                break;
            case 2553:
                addOptionItem(1, 758);
                addOptionItem(2, 503);
                addOptionItem(10, 49);
                addOptionItem(11, 239);
                addOptionItem(19, 13);
                break;
            case 2554:
                addOptionItem(1, 738);
                addOptionItem(2, 490);
                addOptionItem(12, 49);
                addOptionItem(15, 55);
                addOptionItem(19, 14);
                break;
            case 2555:
                addOptionItem(1, 924);
                addOptionItem(2, 363);
                addOptionItem(13, 49);
                addOptionItem(20, 5);
                break;
            case 2556:
                addOptionItem(1, 751);
                addOptionItem(2, 495);
                addOptionItem(13, 49);
                addOptionItem(21, 14);
                addOptionItem(23, 198);
                break;
            case 2557:
                addOptionItem(1, 848);
                addOptionItem(2, 412);
                addOptionItem(10, 49);
                addOptionItem(16, 29);
                addOptionItem(22, 4);
                break;
            case 2558:
                addOptionItem(1, 832);
                addOptionItem(2, 414);
                addOptionItem(14, 49);
                addOptionItem(16, 20);
                addOptionItem(22, 4);
                break;
            case 2559:
                addOptionItem(1, 753);
                addOptionItem(2, 506);
                addOptionItem(10, 49);
                addOptionItem(11, 239);
                addOptionItem(24, 172);
                break;
            case 2560:
                addOptionItem(3, 47);
                addOptionItem(4, 119);
                addOptionItem(14, 49);
                addOptionItem(23, 190);
                addOptionItem(27, 45);
                break;
            case 2561:
                addOptionItem(3, 58);
                addOptionItem(4, 117);
                addOptionItem(15, 40);
                addOptionItem(20, 5);
                addOptionItem(26, 40);
                break;
            case 2562:
                addOptionItem(3, 47);
                addOptionItem(4, 122);
                addOptionItem(11, 169);
                addOptionItem(16, 29);
                addOptionItem(27, 47);
                break;
            case 2563:
                addOptionItem(3, 39);
                addOptionItem(4, 150);
                addOptionItem(15, 77);
                addOptionItem(19, 14);
                addOptionItem(26, 40);
                break;
            case 2564:
                addOptionItem(3, 49);
                addOptionItem(4, 123);
                addOptionItem(15, 56);
                addOptionItem(20, 4);
                addOptionItem(26, 40);
                break;
            case 2565:
                addOptionItem(3, 59);
                addOptionItem(4, 117);
                addOptionItem(12, 49);
                addOptionItem(16, 18);
                addOptionItem(27, 47);
                break;
            case 2566:
                addOptionItem(3, 37);
                addOptionItem(4, 150);
                addOptionItem(15, 75);
                addOptionItem(19, 13);
                addOptionItem(26, 40);
                break;
            case 2567:
                addOptionItem(3, 48);
                addOptionItem(4, 114);
                addOptionItem(13, 49);
                addOptionItem(24, 185);
                addOptionItem(27, 48);
                break;
            case 2568:
                addOptionItem(3, 38);
                addOptionItem(4, 154);
                addOptionItem(14, 49);
                addOptionItem(23, 191);
                addOptionItem(27, 41);
                break;
            case 2569:
                addOptionItem(3, 48);
                addOptionItem(4, 120);
                addOptionItem(15, 56);
                addOptionItem(20, 4);
                addOptionItem(26, 40);
                break;
            case 2570:
                addOptionItem(3, 48);
                addOptionItem(12, 49);
                addOptionItem(15, 75);
                addOptionItem(17, 129);
                addOptionItem(23, 215);
                break;
            case 2571:
                addOptionItem(3, 36);
                addOptionItem(16, 19);
                addOptionItem(20, 5);
                addOptionItem(24, 171);
                addOptionItem(26, 40);
                break;
            case 2572:
                addOptionItem(3, 50);
                addOptionItem(11, 239);
                addOptionItem(15, 38);
                addOptionItem(16, 19);
                break;
            case 2573:
                addOptionItem(3, 26);
                addOptionItem(12, 49);
                addOptionItem(15, 112);
                addOptionItem(17, 129);
                addOptionItem(23, 211);
                break;
            case 2574:
                addOptionItem(3, 49);
                addOptionItem(12, 49);
                addOptionItem(15, 95);
                addOptionItem(17, 129);
                addOptionItem(23, 214);
                break;
            case 2575:
                addOptionItem(3, 36);
                addOptionItem(11, 239);
                addOptionItem(13, 49);
                addOptionItem(19, 13);
                addOptionItem(24, 187);
                break;
            case 2576:
                addOptionItem(3, 38);
                addOptionItem(16, 28);
                addOptionItem(23, 187);
                addOptionItem(24, 216);
                addOptionItem(26, 40);
                break;
            case 2577:
                addOptionItem(3, 26);
                addOptionItem(14, 49);
                addOptionItem(15, 112);
                addOptionItem(18, 113);
                addOptionItem(23, 218);
                break;
            case 2578:
                addOptionItem(3, 26);
                addOptionItem(10, 49);
                addOptionItem(11, 309);
                addOptionItem(24, 171);
                addOptionItem(26, 40);
                break;
            case 2579:
                addOptionItem(3, 37);
                addOptionItem(14, 49);
                addOptionItem(15, 94);
                addOptionItem(18, 106);
                addOptionItem(23, 213);
                break;
            case 2580:
                addOptionItem(3, 48);
                addOptionItem(6, 5);
                addOptionItem(11, 239);
                addOptionItem(22, 5);
                addOptionItem(27, 53);
                break;
            case 2581:
                addOptionItem(3, 49);
                addOptionItem(7, 5);
                addOptionItem(16, 20);
                addOptionItem(18, 156);
                addOptionItem(26, 50);
                break;
            case 2582:
                addOptionItem(3, 28);
                addOptionItem(5, 5);
                addOptionItem(16, 37);
                addOptionItem(17, 179);
                addOptionItem(26, 50);
                break;
            case 2583:
                addOptionItem(3, 27);
                addOptionItem(9, 5);
                addOptionItem(11, 369);
                addOptionItem(21, 14);
                addOptionItem(27, 51);
                break;
            case 2584:
                addOptionItem(3, 48);
                addOptionItem(6, 5);
                addOptionItem(16, 18);
                addOptionItem(18, 171);
                addOptionItem(26, 50);
                break;
            case 2585:
                addOptionItem(3, 50);
                addOptionItem(5, 5);
                addOptionItem(11, 239);
                addOptionItem(22, 4);
                addOptionItem(27, 51);
                break;
            case 2586:
                addOptionItem(3, 28);
                addOptionItem(7, 5);
                addOptionItem(11, 369);
                addOptionItem(21, 14);
                addOptionItem(27, 51);
                break;
            case 2587:
                addOptionItem(3, 26);
                addOptionItem(8, 5);
                addOptionItem(16, 38);
                addOptionItem(17, 179);
                addOptionItem(26, 50);
                break;
            case 2588:
                addOptionItem(3, 38);
                addOptionItem(9, 5);
                addOptionItem(16, 29);
                addOptionItem(17, 179);
                addOptionItem(26, 50);
                break;
            case 2589:
                addOptionItem(3, 37);
                addOptionItem(5, 5);
                addOptionItem(11, 309);
                addOptionItem(22, 5);
                addOptionItem(27, 55);
                break;
            case 2590:
                addOptionItem(6, 5);
                addOptionItem(16, 18);
                addOptionItem(17, 139);
                addOptionItem(19, 15);
                break;
            case 2591:
                addOptionItem(4, 129);
                addOptionItem(16, 36);
                addOptionItem(26, 40);
                addOptionItem(27, 45);
                break;
            case 2592:
                addOptionItem(1, 200);
                addOptionItem(10, 49);
                addOptionItem(11, 139);
                addOptionItem(16, 27);
                break;
            case 2593:
                addOptionItem(2, 553);
                addOptionItem(9, 5);
                addOptionItem(16, 37);
                addOptionItem(18, 131);
                break;
            case 2594:
                addOptionItem(7, 5);
                addOptionItem(10, 49);
                addOptionItem(15, 56);
                addOptionItem(21, 14);
                break;
            case 2595:
                addOptionItem(5, 5);
                addOptionItem(13, 49);
                addOptionItem(15, 39);
                addOptionItem(18, 179);
                break;
            case 2596:
                addOptionItem(8, 5);
                addOptionItem(13, 49);
                addOptionItem(15, 55);
                addOptionItem(22, 5);
                break;
            case 2597:
                addOptionItem(6, 5);
                addOptionItem(14, 15);
                addOptionItem(15, 76);
                addOptionItem(17, 50);
                break;
            case 12001:
            case 12002:
            case 12003:
            case 12004:
            case 12005:
            case 12006:
            case 12007:
            case 12008:
            case 12009:
            case 12010:
            case 12011:
            case 12012:
            case 12013:
            case 12014:
            case 12015:
            case 12016:
            case 12017: {
                List<Option> dialOps = getDefaultDialOptions(this.id);
                for (Option op : dialOps) {
                    addOptionItem(op.id, op.getParam());
                }
                break;
            }
        }
    }

    public static List<Option> getDefaultDialOptions(int templateId) {
        List<Option> list = new ArrayList<>();
        switch (templateId) {
            // === Nhóm Trắng (color 0) ===
            case 12013: // Dial Siêu Năng (Trắng) - 2 dòng
                list.add(new Option(1, 90));   // Tăng tấn công (+9.0%)
                list.add(new Option(17, 15));  // Tăng HP % (+1.5%)
                break;
            case 12014: // Dial Sử Thi (Trắng) - 3 dòng
                list.add(new Option(1, 100));  // Tăng tấn công (+10.0%)
                list.add(new Option(4, 15));   // Tăng P.Thủ % (+1.5%)
                list.add(new Option(10, 12));  // Chí mạng % (+1.2%)
                break;
            case 12015: // Dial Thần Thoại (Trắng) - 4 dòng
                list.add(new Option(1, 110));  // Tăng tấn công (+11.0%)
                list.add(new Option(17, 15));  // Tăng HP % (+1.5%)
                list.add(new Option(4, 15));   // Tăng P.Thủ % (+1.5%)
                list.add(new Option(19, 10));  // Tự hồi HP (+10)
                break;
            case 12016: // Dial Truyền thuyết (Trắng) - 5 dòng
                list.add(new Option(1, 120));  // Tăng tấn công (+12.0%)
                list.add(new Option(17, 18));  // Tăng HP % (+1.8%)
                list.add(new Option(4, 18));   // Tăng P.Thủ % (+1.8%)
                list.add(new Option(10, 15));  // Chí mạng % (+1.5%)
                list.add(new Option(13, 15));  // Xuyên giáp % (+1.5%)
                break;

            // === Nhóm Xanh Lam (color 1) ===
            case 12009: // Dial Siêu Năng (Xanh) - 2 dòng
                list.add(new Option(1, 140));  // Tăng tấn công (+14.0%)
                list.add(new Option(17, 20));  // Tăng HP % (+2.0%)
                break;
            case 12010: // Dial Sử Thi (Xanh) - 3 dòng
                list.add(new Option(1, 160));  // Tăng tấn công (+16.0%)
                list.add(new Option(4, 20));   // Tăng P.Thủ % (+2.0%)
                list.add(new Option(10, 15));  // Chí mạng % (+1.5%)
                break;
            case 12011: // Dial Thần Thoại (Xanh) - 4 dòng
                list.add(new Option(1, 180));  // Tăng tấn công (+18.0%)
                list.add(new Option(17, 22));  // Tăng HP % (+2.2%)
                list.add(new Option(4, 20));   // Tăng P.Thủ % (+2.0%)
                list.add(new Option(51, 15));  // Giảm né đ/t % (+1.5%)
                break;
            case 12012: // Dial Truyền thuyết (Xanh) - 5 dòng
                list.add(new Option(1, 200));  // Tăng tấn công (+20.0%)
                list.add(new Option(17, 25));  // Tăng HP % (+2.5%)
                list.add(new Option(4, 22));   // Tăng P.Thủ % (+2.2%)
                list.add(new Option(51, 16));  // Giảm né đ/t % (+1.6%)
                list.add(new Option(50, 16));  // Giảm xuyên giáp đ/t % (+1.6%)
                break;

            // === Nhóm Tím (color 2) ===
            case 12005: // Dial Siêu Năng (Tím) - 3 dòng
                list.add(new Option(1, 190));  // Tăng tấn công (+19.0%)
                list.add(new Option(17, 25));  // Tăng HP % (+2.5%)
                list.add(new Option(7, 3));    // T/n thể lực (+3)
                break;
            case 12006: // Dial Sử Thi (Tím) - 4 dòng
                list.add(new Option(1, 210));  // Tăng tấn công (+21.0%)
                list.add(new Option(4, 25));   // Tăng P.Thủ % (+2.5%)
                list.add(new Option(10, 20));  // Chí mạng % (+2.0%)
                list.add(new Option(13, 20));  // Xuyên giáp % (+2.0%)
                break;
            case 12007: // Dial Thần Thoại (Tím) - 4 dòng
                list.add(new Option(1, 230));  // Tăng tấn công (+23.0%)
                list.add(new Option(17, 28));  // Tăng HP % (+2.8%)
                list.add(new Option(53, 15));  // Miễn thương % (+1.5%)
                list.add(new Option(51, 18));  // Giảm né đ/t % (+1.8%)
                break;
            case 12008: // Dial Truyền thuyết (Tím) - 5 dòng
                list.add(new Option(1, 250));  // Tăng tấn công (+25.0%)
                list.add(new Option(17, 30));  // Tăng HP % (+3.0%)
                list.add(new Option(53, 16));  // Miễn thương % (+1.6%)
                list.add(new Option(50, 18));  // Giảm xuyên giáp đ/t % (+1.8%)
                list.add(new Option(52, 18));  // Giảm phản đòn đ/t % (+1.8%)
                break;

            // === Nhóm Cam (color 3) ===
            case 12001: // Dial Siêu Năng (Cam) - 3 dòng
                list.add(new Option(1, 250));  // Tăng tấn công (+25.0%)
                list.add(new Option(17, 32));  // Tăng HP % (+3.2%)
                list.add(new Option(7, 4));    // T/n thể lực (+4)
                break;
            case 12002: // Dial Sử Thi (Cam) - 4 dòng
                list.add(new Option(1, 270));  // Tăng tấn công (+27.0%)
                list.add(new Option(4, 30));   // Tăng P.Thủ % (+3.0%)
                list.add(new Option(10, 25));  // Chí mạng % (+2.5%)
                list.add(new Option(13, 25));  // Xuyên giáp % (+2.5%)
                break;
            case 12003: // Dial Thần Thoại (Cam) - 5 dòng
                list.add(new Option(1, 290));  // Tăng tấn công (+29.0%)
                list.add(new Option(17, 35));  // Tăng HP % (+3.5%)
                list.add(new Option(53, 20));  // Miễn thương % (+2.0%)
                list.add(new Option(51, 22));  // Giảm né đ/t % (+2.2%)
                list.add(new Option(10, 22));  // Chí mạng % (+2.2%)
                break;
            case 12004: // Dial Truyền thuyết (Cam) - 6 dòng
                list.add(new Option(1, 310));  // Tăng tấn công (+31.0%)
                list.add(new Option(17, 38));  // Tăng HP % (+3.8%)
                list.add(new Option(53, 22));  // Miễn thương % (+2.2%)
                list.add(new Option(63, 22));  // Giảm miễn thương % (+2.2%)
                list.add(new Option(50, 22));  // Giảm xuyên giáp đ/t % (+2.2%)
                list.add(new Option(52, 22));  // Giảm phản đòn đ/t % (+2.2%)
                break;

            // === Nhóm Đỏ / Cực Phẩm (color 8) ===
            case 12017: // Dial Cực Phẩm (Đỏ) - 8 dòng tối thượng
                list.add(new Option(1, 350));  // Tăng tấn công (+35.0%)
                list.add(new Option(17, 40));  // Tăng HP % (+4.0%)
                list.add(new Option(4, 35));   // Tăng P.Thủ % (+3.5%)
                list.add(new Option(53, 25));  // Miễn thương % (+2.5%)
                list.add(new Option(63, 25));  // Giảm miễn thương % (+2.5%)
                list.add(new Option(49, 25));  // Giảm chí mạng đ/t % (+2.5%)
                list.add(new Option(50, 25));  // Giảm xuyên giáp đ/t % (+2.5%)
                list.add(new Option(52, 25));  // Giảm phản đòn đ/t % (+2.5%)
                break;
        }
        return list;
    }

    private void addOptionItem(int id, int param) {
        if (this.option_item == null) {
            this.option_item = new java.util.ArrayList<>();
        }
        for (Option op : this.option_item) {
            if (op.id == id) {
                op.setParam(param);
                return;
            }
        }
        this.option_item.add(new Option((byte) id, (short) param));
    }
}
