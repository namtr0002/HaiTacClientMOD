package itemz.rebuilds;

import zabstracts.AbsUpgrade;
import model.Player;
import template.Item_wear;
import template.ItemTemplate7;
import template.ItemOptionTemplate;
import template.Option;
import template.ThanTrangConfig;
import network.Message;
import model.YesNoDialog;
import model.InputDialog;
import itemz.UpgradeSuperItem;
import core.Manager;
import core.ZUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * NangCapThanTrang - Hệ thống Thần Trang (TypeEquip 8..15, Set 16 -> Set 30)
 * Gồm 3 tính năng độc quyền:
 * 1. Cường Hóa Thần Trang (+0 -> +20)
 * 2. Tách Cường Hóa Thần Trang (+1..+20 -> +0, nhận lại 50% nguyên liệu)
 * 3. Tinh Luyện Thần Trang (Re-roll chỉ số theo min-max, có tỉ lệ MAX ALL chỉ số)
 * Tuyệt đối chặn trang bị cũ (Type 0-7) và có thông báo rõ ràng.
 */
public class NangCapThanTrang extends AbsUpgrade {

    public static final byte TYPE_UPGRADE = 12;
    private static NangCapThanTrang instance;

    public static final int TINH_LUYEN_BERI_COST = 300_000;
    public static final int TINH_LUYEN_RUBY_COST = 10;

    public NangCapThanTrang() {
        instance = this;
        AbsUpgrade.register(this);
    }

    public static NangCapThanTrang getInstance() {
        if (instance == null) {
            instance = new NangCapThanTrang();
        }
        return instance;
    }

    public static NangCapThanTrang gI() {
        return getInstance();
    }

    @Override
    public byte getType() {
        return TYPE_UPGRADE;
    }

    public static boolean isThanTrang(Item_wear it) {
        if (it == null) return false;
        return it.isThanTrang();
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
        m.writer().writeByte(12); // Giao diện Bàn Tinh Luyện Rebuild chuẩn Client
        p.addmsg(m);
        m.cleanup();
        p.item_to_kham_ngoc = null;
        p.item_to_kham_ngoc_id_ngoc = -1;
        p.data_yesno = null;
    }

    @Override
    public void process(Player p, byte type, byte action, short buttonId, byte cat, short num) throws IOException {
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        if (action == 1 && cat == 3) { // Đặt Thần Trang vào bàn tinh luyện
            Item_wear it_select = (buttonId >= 0 && buttonId < p.item.bag3.length) ? p.item.bag3[buttonId] : null;
            if (it_select != null) {
                if (!isThanTrang(it_select)) {
                    p.getService().send_box_ThongBao_OK("Bàn Tinh Luyện chỉ dành cho trang bị Thần Trang (Type 8-15)!");
                    return;
                }
                if (it_select.levelUp > 0) {
                    p.getService().send_box_ThongBao_OK("Chỉ có thể Tinh Luyện trang bị Thần Trang ở cấp +0!\nTrang bị này đang ở cấp +" + it_select.levelUp + ", vui lòng Tách Cường Hóa về +0 trước.");
                    return;
                }
                if (isMaxAllOptions(it_select)) {
                    p.getService().send_box_ThongBao_OK("Trang bị " + it_select.template.name + " đã đạt tất cả chỉ số TỐI ĐA (MAX ALL)!\nKhông thể tinh luyện thêm.");
                    return;
                }
                p.getService().sendRebuildPutItem(buttonId, (byte) 3, (short) 1);
                p.item_to_kham_ngoc = it_select;
            }
        } else if ((action == 20 || action == 24 || action == 2) && cat == 0) { // Bắt đầu tinh luyện
            if (p.item_to_kham_ngoc == null) {
                p.getService().send_box_ThongBao_OK("Vui lòng chọn 1 trang bị Thần Trang (+0) trong hành trang đặt vào bàn trước!");
                return;
            }
            Item_wear it = p.item_to_kham_ngoc;
            if (!isThanTrang(it)) {
                p.getService().send_box_ThongBao_OK("Chức năng này chỉ dành cho trang bị Thần Trang (Type 8-15)!");
                return;
            }
            if (it.levelUp > 0) {
                p.getService().send_box_ThongBao_OK("Chỉ có thể Tinh Luyện trang bị Thần Trang ở cấp +0!\nVui lòng Tách Cường Hóa về +0 trước khi Tinh Luyện.");
                return;
            }
            if (isMaxAllOptions(it)) {
                p.getService().send_box_ThongBao_OK("Trang bị " + it.template.name + " đã đạt tất cả chỉ số TỐI ĐA (MAX ALL)!\nKhông thể tinh luyện thêm.");
                return;
            }
            openTinhLuyenDialog(p, it);
        }
    }

    // =========================================================================
    // 1. CƯỜNG HÓA THẦN TRANG (+0 -> +20)
    // =========================================================================

    public static void openSelectThanTrangDialog(Player p) throws IOException {
        if (p == null || p.item == null) return;
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        List<Item_wear> thanTrangList = new ArrayList<>();
        List<String> names = new ArrayList<>();

        if (p.item.it_body != null) {
            for (int i = 0; i < p.item.it_body.length; i++) {
                Item_wear it = p.item.it_body[i];
                if (it != null && isThanTrang(it)) {
                    thanTrangList.add(it);
                    String maxTag = (it.levelUp >= 20) ? " [MAX +20]" : "";
                    names.add("[Đang Mặc] " + it.template.name + " (+" + it.levelUp + ")" + maxTag);
                }
            }
        }

        if (p.item.bag3 != null) {
            for (int i = 0; i < p.item.bag3.length; i++) {
                Item_wear it = p.item.bag3[i];
                if (it != null && isThanTrang(it)) {
                    thanTrangList.add(it);
                    String maxTag = (it.levelUp >= 20) ? " [MAX +20]" : "";
                    names.add(it.template.name + " (+" + it.levelUp + ")" + maxTag);
                }
            }
        }

        if (thanTrangList.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Bạn không có trang bị Thần Trang nào trong hành trang hoặc đang mặc để cường hóa!");
            return;
        }

        int visualTableIndex = names.size();
        names.add("Mở Bàn Cường Hóa Trực Quan");
        names.add("Đóng");
        byte[] types = new byte[names.size()];
        for (int i = 0; i < types.length - 1; i++) types[i] = -1;
        types[types.length - 1] = 1;

        YesNoDialog ynd = new YesNoDialog(p, 68, "CƯỜNG HÓA THẦN TRANG",
                "Chọn Thần Trang bạn muốn cường hóa (+0 -> +20):",
                names.toArray(new String[0]), types, (byte value) -> {
            if (p.trade_target != null) {
                try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
                return;
            }
            if (value >= 0 && value < thanTrangList.size()) {
                Item_wear selected = thanTrangList.get(value);
                if (selected != null) {
                    if (!isThanTrang(selected)) {
                        try {
                            p.getService().send_box_ThongBao_OK("Chức năng này chỉ dành cho trang bị Thần Trang (Type 8-15)!");
                        } catch (Exception ignored) {}
                        return;
                    }
                    if (selected.levelUp >= 20) {
                        try {
                            p.getService().send_box_ThongBao_OK("Trang bị " + selected.template.name + " đã đạt cấp tối đa +20!\nKhông thể cường hóa thêm.");
                        } catch (Exception ignored) {}
                        return;
                    }
                    p.item_to_kham_ngoc = selected;
                    openAutoUpgradeDialog(p, selected);
                }
            } else if (value == visualTableIndex) {
                try {
                    itemz.UpgradeItem.show_table_upgrade(p);
                } catch (Exception ignored) {}
            }
        });
        ynd.startYesNo();
    }

    public static void openAutoUpgradeDialog(Player p, Item_wear it) {
        if (p == null || it == null) return;
        if (p.trade_target != null) {
            try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            return;
        }
        if (!isThanTrang(it)) {
            try {
                p.getService().send_box_ThongBao_OK("Chức năng này chỉ dành cho trang bị Thần Trang (Type 8-15)!");
            } catch (Exception ignored) {}
            return;
        }
        if (it.levelUp >= 20) {
            try {
                p.getService().send_box_ThongBao_OK("Trang bị " + it.template.name + " đã đạt cấp tối đa +20!\nKhông thể cường hóa thêm.");
            } catch (Exception ignored) {}
            return;
        }

        int targetLevel = it.levelUp + 1;
        int beriCost = getBeriCost(it.levelUp);
        int rubyCost = getRubyCost(it.levelUp);
        int stoneId = getRequiredStoneId(it.levelUp);
        int stoneNum = getRequiredStoneNum(it.levelUp);
        String stoneName = ItemTemplate7.get_it_by_id(stoneId) != null ? ItemTemplate7.get_it_by_id(stoneId).name : "Đá Cường Hóa";
        int ownedStone = p.item.total_item_bag_by_id(7, stoneId);

        int ownedShield = p.item.total_item_bag_by_id(7, 10);
        int ownedMaiRua = p.item.total_item_bag_by_id(7, 6);
        int ownedThienThach = p.item.total_item_bag_by_id(7, 11);

        String info = "CƯỜNG HÓA THẦN TRANG\n"
                + "Trang bị: " + it.template.name + " (+" + it.levelUp + " -> +" + targetLevel + ")\n"
                + "Yêu cầu mỗi lần:\n"
                + "• Tiền: " + ZUtil.number_format(beriCost) + " Beri (hoặc " + rubyCost + " Ruby)\n"
                + "• Nguyên liệu: " + stoneNum + "x " + stoneName + " (Hiện có: " + ownedStone + ")\n"
                + "• Bùa bảo vệ: Khiên (Có " + ownedShield + ") | Mai Rùa (Có " + ownedMaiRua + ") | Thiên Thạch (Có " + ownedThienThach + ")\n"
                + "• Quy tắc rớt cấp: Mốc 1, 4, 8, 10, 14, 16, 20 an toàn (không rớt) | Các cấp khác có thể rớt 1 cấp (bảo vệ bằng Khiên / Mai Rùa)\n"
                + "Chọn chế độ cường hóa:";

        String[] options = new String[]{
            "Cường hóa Beri (1 lần)",
            "Cường hóa Ruby (1 lần)",
            "Auto Beri (10 lần)",
            "Auto Beri (50 lần)",
            "Auto Beri lên mốc (+5/+10/+15/+20)",
            "Auto Ruby lên mốc (+5/+10/+15/+20)",
            "Auto Beri lên +20 (Max)",
            "Auto Ruby lên +20 (Max)",
            "Nhập số lần",
            "Hủy"
        };
        byte[] types = new byte[options.length];
        for (int i = 0; i < types.length - 1; i++) types[i] = -1;
        types[types.length - 1] = 1;

        YesNoDialog ynd = new YesNoDialog(p, 69, "Auto Cường Hóa Thần Trang", info, options, types, value -> {
            int nextCap = 20;
            if (it.levelUp < 5) nextCap = 5;
            else if (it.levelUp < 10) nextCap = 10;
            else if (it.levelUp < 15) nextCap = 15;
            else nextCap = 20;

            switch (value) {
                case 0: executeAutoUpgrade(p, it, 1, 1, 20); break;
                case 1: executeAutoUpgrade(p, it, 2, 1, 20); break;
                case 2: executeAutoUpgrade(p, it, 1, 10, 20); break;
                case 3: executeAutoUpgrade(p, it, 1, 50, 20); break;
                case 4: executeAutoUpgrade(p, it, 1, 500, nextCap); break;
                case 5: executeAutoUpgrade(p, it, 2, 500, nextCap); break;
                case 6: executeAutoUpgrade(p, it, 1, 500, 20); break;
                case 7: executeAutoUpgrade(p, it, 2, 500, 20); break;
                case 8:
                    new InputDialog(p, 69, "Nhập số lần", new String[]{"Số lần muốn Auto Cường Hóa (1-500):", "Dùng tiền (1=Beri, 2=Ruby):"}, inputs -> {
                        if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                        try {
                            int count = Integer.parseInt(inputs[0].trim());
                            int moneyType = (inputs.length > 1 && inputs[1].trim().equals("2")) ? 2 : 1;
                            if (count <= 0 || count > 500) {
                                p.getService().send_box_ThongBao_OK("Số lần không hợp lệ (1 - 500)!");
                                return;
                            }
                            executeAutoUpgrade(p, it, moneyType, count, 20);
                        } catch (Exception e) {
                            p.getService().send_box_ThongBao_OK("Vui lòng nhập đúng định dạng số!");
                        }
                    }).startInput();
                    break;
                default:
                    break;
            }
        });
        ynd.startYesNo();
    }

    public static boolean isSafeLevel(int level) {
        return level <= 1 || level == 4 || level == 8 || level == 10 || level == 14 || level == 16 || level >= 20;
    }

    public static void executeAutoUpgrade(Player p, Item_wear it, int moneyType, int maxLoops, int targetCap) {
        if (p == null || it == null || !isThanTrang(it)) return;
        if (p.trade_target != null) {
            try {
                p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            } catch (Exception ignored) {}
            return;
        }

        boolean owned = false;
        if (p.item.it_body != null) {
            for (Item_wear bodyIt : p.item.it_body) {
                if (bodyIt == it) { owned = true; break; }
            }
        }
        if (!owned && p.item.bag3 != null) {
            for (Item_wear bagIt : p.item.bag3) {
                if (bagIt == it) { owned = true; break; }
            }
        }
        if (!owned) {
            try {
                p.getService().send_box_ThongBao_OK("Vật phẩm không còn trong hành trang hoặc trên người!");
            } catch (Exception ignored) {}
            return;
        }

        if (targetCap <= 0 || targetCap > 20) targetCap = 20;
        if (it.levelUp >= targetCap || it.levelUp >= 20) {
            try {
                p.getService().send_box_ThongBao_OK("Trang bị " + it.template.name + " đã đạt cấp tối đa (+20)!");
            } catch (Exception ignored) {}
            return;
        }

        int startLevel = it.levelUp;
        int successCount = 0;
        int failCount = 0;
        long totalBeriSpent = 0;
        int totalRubySpent = 0;
        String stopReason = "Hoàn thành " + maxLoops + " lượt";

        synchronized (p.item) {
            for (int step = 0; step < maxLoops; step++) {
                if (it.levelUp >= targetCap || it.levelUp >= 20) {
                    stopReason = "Đã đạt cấp mong muốn (+" + it.levelUp + ")!";
                    break;
                }

                int beriCost = getBeriCost(it.levelUp);
                int rubyCost = getRubyCost(it.levelUp);
                int stoneId = getRequiredStoneId(it.levelUp);
                int stoneNum = getRequiredStoneNum(it.levelUp);

                if (p.item.total_item_bag_by_id(7, stoneId) < stoneNum) {
                    String matName = ItemTemplate7.get_it_by_id(stoneId) != null ? ItemTemplate7.get_it_by_id(stoneId).name : "Đá Cường Hóa";
                    stopReason = "Hết nguyên liệu " + matName + " (cần " + stoneNum + ")!";
                    break;
                }

                if (moneyType == 1) {
                    if (p.get_vang() < beriCost) {
                        stopReason = "Không đủ " + ZUtil.number_format(beriCost) + " Beri!";
                        break;
                    }
                    p.update_vang(-beriCost);
                    totalBeriSpent += beriCost;
                } else {
                    if (p.get_ngoc() < rubyCost) {
                        stopReason = "Không đủ " + rubyCost + " Ruby!";
                        break;
                    }
                    p.update_ngoc(-rubyCost);
                    totalRubySpent += rubyCost;
                }

                p.item.remove_item47(7, stoneId, stoneNum);

                int baseRate = getSuccessRate(it.levelUp);

                // Thiên thạch may mắn (Item 7 ID 11) tăng 50% tỉ lệ thành công
                if (p.item.total_item_bag_by_id(7, 11) >= 1) {
                    baseRate = (baseRate * 15) / 10;
                    p.item.remove_item47(7, 11, 1);
                }

                boolean isSuccess = ZUtil.random(1000) < baseRate;

                if (isSuccess) {
                    it.levelUp++;
                    successCount++;
                    if (it.levelUp >= 15) {
                        Manager.gI().chatKTG(0, "Chúc mừng người chơi [" + p.name + "] đã cường hóa thành công Thần Trang " + it.template.name + " lên +" + it.levelUp + "!", 5);
                    }
                } else {
                    failCount++;
                    // Quy tắc rớt cấp: mốc 4, 8, 10, 14, 16, 20 là an toàn (không rớt), các cấp còn lại có thể rớt
                    if (isSafeLevel(it.levelUp)) {
                        // Mốc an toàn: không rớt cấp
                    } else {
                        boolean hasShield = p.item.total_item_bag_by_id(7, 10) >= 1;
                        if (hasShield) {
                            p.item.remove_item47(7, 10, 1); // Dùng 1 Khiên, không rớt cấp
                        } else if (p.item.total_item_bag_by_id(7, 6) >= 1) {
                            p.item.remove_item47(7, 6, 1); // Dùng 1 Mai rùa, 30% rớt 1 cấp
                            if (ZUtil.random(100) < 30) {
                                it.levelUp = (byte) Math.max(0, it.levelUp - 1);
                            }
                        } else {
                            // Không có bảo hiểm: 60% rớt 1 cấp
                            if (ZUtil.random(100) < 60) {
                                it.levelUp = (byte) Math.max(0, it.levelUp - 1);
                            }
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
            } catch (Exception ignored) {}
        }

        try {
            String resultNotice = "KẾT QUẢ CƯỜNG HÓA THẦN TRANG:\n"
                    + "• Trang bị: " + it.template.name + "\n"
                    + "• Cấp độ: +" + startLevel + " -> +" + it.levelUp + "\n"
                    + "• Thành công: " + successCount + " lần | Thất bại: " + failCount + " lần\n"
                    + "• Tiêu hao: " + (totalBeriSpent > 0 ? (ZUtil.number_format(totalBeriSpent) + " Beri ") : "")
                    + (totalRubySpent > 0 ? (totalRubySpent + " Ruby") : "") + "\n"
                    + "• Trạng thái: " + stopReason;

            p.getService().send_box_ThongBao_OK(resultNotice);
        } catch (Exception ignored) {}
    }

    public static void executeAutoUpgrade(Player p, Item_wear it, int moneyType, int maxLoops) {
        executeAutoUpgrade(p, it, moneyType, maxLoops, 20);
    }

    public static double getMaterialMultiplier(int currentLevel) {
        if (currentLevel < 0) currentLevel = 0;
        if (currentLevel > 19) currentLevel = 19;
        return 1.25 + (1.75 * currentLevel / 19.0);
    }

    public static int getBeriCost(int currentLevel) {
        int baseBeri;
        if (currentLevel < 5) {
            baseBeri = 40_000 * (currentLevel + 1);
        } else if (currentLevel < 10) {
            baseBeri = 250_000 * (currentLevel - 4);
        } else if (currentLevel < 15) {
            baseBeri = 1_500_000 + (currentLevel - 10) * 500_000;
        } else {
            baseBeri = 4_500_000 + (currentLevel - 15) * 1_200_000;
        }
        return (int) Math.round(baseBeri * getMaterialMultiplier(currentLevel));
    }

    public static int getRubyCost(int currentLevel) {
        int baseRuby;
        if (currentLevel < 5) {
            baseRuby = 15 + currentLevel * 5;
        } else if (currentLevel < 10) {
            baseRuby = 40 + (currentLevel - 5) * 10;
        } else if (currentLevel < 15) {
            baseRuby = 100 + (currentLevel - 10) * 20;
        } else {
            baseRuby = 220 + (currentLevel - 15) * 50;
        }
        return (int) Math.round(baseRuby * getMaterialMultiplier(currentLevel));
    }

    public static int getRequiredStoneId(int currentLevel) {
        if (currentLevel < 5) return 1;  // Bột cường hóa (Item 7 ID 1)
        if (currentLevel < 10) return 3; // Bột tím (Item 7 ID 3)
        if (currentLevel < 15) return 4; // Bột vàng (Item 7 ID 4)
        return 18;                       // Bột siêu cấp (Item 7 ID 18)
    }

    public static int getRequiredStoneNum(int currentLevel) {
        int baseStone;
        if (currentLevel < 5) {
            baseStone = 1 + (currentLevel / 2); // 1..3
        } else if (currentLevel < 10) {
            baseStone = 2 + (currentLevel - 5); // 2..6
        } else if (currentLevel < 15) {
            baseStone = 3 + (currentLevel - 10); // 3..7
        } else {
            baseStone = 6 + (currentLevel - 15) * 2; // 6..14
        }
        return (int) Math.ceil(baseStone * getMaterialMultiplier(currentLevel));
    }

    public static int getSuccessRate(int currentLevel) {
        switch (currentLevel) {
            case 0: return 900;
            case 1: return 800;
            case 2: return 700;
            case 3: return 600;
            case 4: return 500;
            case 5: return 400;
            case 6: return 350;
            case 7: return 300;
            case 8: return 250;
            case 9: return 200;
            case 10: return 180;
            case 11: return 160;
            case 12: return 140;
            case 13: return 120;
            case 14: return 100;
            case 15: return 80;
            case 16: return 60;
            case 17: return 50;
            case 18: return 40;
            case 19: return 30;
            default: return 20;
        }
    }

    // =========================================================================
    // 2. TÁCH CƯỜNG HÓA THẦN TRANG (+1..+20 -> +0)
    // =========================================================================

    public static void openTachCuongHoaThanTrang(Player p) throws IOException {
        if (p == null) return;
        NangCapTachThanTrang.getInstance().showTable(p);
    }

    public static void confirmTachCuongHoaThanTrang(Player p, Item_wear it) {
        NangCapTachThanTrang.confirmTachCuongHoa(p, it);
    }

    // =========================================================================
    // 3. TINH LUYỆN THẦN TRANG (Re-roll chỉ số theo min-max, Tỉ lệ MAX ALL)
    // =========================================================================

    public static class ThanTrangOptionRange {
        public int optionId;
        public int minParam;
        public int maxParam;

        public ThanTrangOptionRange(int optionId, int minParam, int maxParam) {
            this.optionId = optionId;
            this.minParam = minParam;
            this.maxParam = maxParam;
        }
    }

    public static List<ThanTrangOptionRange> getThanTrangOptionRanges(Item_wear it) {
        if (it == null || it.template == null) return Collections.emptyList();
        List<ThanTrangConfig.ThanTrangOptionRange> cfgRanges = ThanTrangConfig.getThanTrangOptionRanges(it.template.id);
        List<ThanTrangOptionRange> ranges = new ArrayList<>();
        if (cfgRanges != null && !cfgRanges.isEmpty()) {
            for (ThanTrangConfig.ThanTrangOptionRange r : cfgRanges) {
                ranges.add(new ThanTrangOptionRange(r.optionId, r.minParam, r.maxParam));
            }
        }
        return ranges;
    }

    public static class TinhLuyenResult {
        public List<Option> newOptions = new ArrayList<>();
    }

    public static boolean isMaxAllOptions(Item_wear it) {
        if (it == null || it.template == null || it.option_item == null || it.option_item.isEmpty()) return false;
        List<ThanTrangOptionRange> ranges = getThanTrangOptionRanges(it);
        if (ranges.isEmpty()) return false;
        if (it.option_item.size() < ranges.size()) return false;

        for (ThanTrangOptionRange r : ranges) {
            Option matchOp = null;
            for (Option op : it.option_item) {
                if (op != null && op.id == r.optionId) {
                    matchOp = op;
                    break;
                }
            }
            if (matchOp == null) return false;
            int max = r.maxParam;
            if (ThanTrangConfig.isPercentOption(r.optionId)) {
                if (max > 50) max = 50;
            }
            if (matchOp.param < max) {
                return false;
            }
        }
        return true;
    }

    public static int rollSingleParam(int optionId, int min, int max) {
        if (min >= max) return max;
        // Tỉ lệ ra max cho từng dòng đơn là 10%
        if (ZUtil.random(100) < 10) {
            return max;
        } else {
            return ZUtil.random(min, max); // random trong khoảng [min, max - 1]
        }
    }

    public static TinhLuyenResult rollTinhLuyen(Item_wear it) {
        List<ThanTrangOptionRange> ranges = getThanTrangOptionRanges(it);
        TinhLuyenResult result = new TinhLuyenResult();
        // 5% tỉ lệ bạo phát may mắn đạt MAX ALL toàn bộ chỉ số
        boolean luckyMaxAll = ZUtil.random(100) < 5;

        for (ThanTrangOptionRange r : ranges) {
            int min = r.minParam;
            int max = r.maxParam;
            if (ThanTrangConfig.isPercentOption(r.optionId)) {
                if (max > 50) max = 50;
                if (min > max) min = max;
            }
            int val;
            if (luckyMaxAll) {
                val = max;
            } else {
                val = rollSingleParam(r.optionId, min, max);
            }
            result.newOptions.add(new Option(r.optionId, val));
        }
        return result;
    }

    public static void openTinhLuyenThanTrang(Player p) throws IOException {
        if (p == null || p.item == null) return;
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        List<Item_wear> thanTrangList = new ArrayList<>();
        List<String> names = new ArrayList<>();

        if (p.item.it_body != null) {
            for (int i = 0; i < p.item.it_body.length; i++) {
                Item_wear it = p.item.it_body[i];
                if (it != null && isThanTrang(it) && it.levelUp == 0) {
                    thanTrangList.add(it);
                    String maxTag = isMaxAllOptions(it) ? " [MAX ALL]" : "";
                    names.add("[Đang Mặc] " + it.template.name + " (+0)" + maxTag);
                }
            }
        }

        if (p.item.bag3 != null) {
            for (int i = 0; i < p.item.bag3.length; i++) {
                Item_wear it = p.item.bag3[i];
                if (it != null && isThanTrang(it) && it.levelUp == 0) {
                    thanTrangList.add(it);
                    String maxTag = isMaxAllOptions(it) ? " [MAX ALL]" : "";
                    names.add(it.template.name + " (+0)" + maxTag);
                }
            }
        }

        if (thanTrangList.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Bạn không có trang bị Thần Trang cấp +0 nào để Tinh Luyện!\n(Trang bị đã cường hóa vui lòng Tách Cường Hóa về +0 trước).");
            return;
        }

        names.add("Đóng");
        byte[] types = new byte[names.size()];
        for (int i = 0; i < types.length - 1; i++) types[i] = -1;
        types[types.length - 1] = 1;

        YesNoDialog ynd = new YesNoDialog(p, 68, "TINH LUYỆN THẦN TRANG (+0)",
                "Chọn Thần Trang (+0) bạn muốn Tinh Luyện (Random ngẫu nhiên chỉ số, có tỉ lệ MAX ALL):",
                names.toArray(new String[0]), types, (byte value) -> {
            if (p.trade_target != null) {
                try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
                return;
            }
            if (value >= 0 && value < thanTrangList.size()) {
                Item_wear selected = thanTrangList.get(value);
                if (selected != null) {
                    if (!isThanTrang(selected)) {
                        try {
                            p.getService().send_box_ThongBao_OK("Chức năng này chỉ dành cho trang bị Thần Trang (Type 8-15)!");
                        } catch (Exception ignored) {}
                        return;
                    }
                    if (selected.levelUp > 0) {
                        try {
                            p.getService().send_box_ThongBao_OK("Chỉ có thể Tinh Luyện trang bị Thần Trang ở cấp +0!\nTrang bị này đang ở cấp +" + selected.levelUp + ", vui lòng Tách Cường Hóa về +0 trước.");
                        } catch (Exception ignored) {}
                        return;
                    }
                    if (isMaxAllOptions(selected)) {
                        try {
                            p.getService().send_box_ThongBao_OK("Trang bị " + selected.template.name + " đã đạt tất cả chỉ số TỐI ĐA (MAX ALL)!\nKhông thể tinh luyện thêm để tránh mất chỉ số hoàn hảo.");
                        } catch (Exception ignored) {}
                        return;
                    }
                    openTinhLuyenDialog(p, selected);
                }
            }
        });
        ynd.startYesNo();
    }

    public static void openTinhLuyenDialog(Player p, Item_wear it) {
        if (p == null || it == null || !isThanTrang(it)) return;
        if (p.trade_target != null) {
            try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            return;
        }
        if (it.levelUp > 0) {
            try {
                p.getService().send_box_ThongBao_OK("Chỉ có thể Tinh Luyện trang bị Thần Trang ở cấp +0!\nVui lòng sử dụng tính năng Tách Cường Hóa về +0 trước khi Tinh Luyện.");
            } catch (Exception ignored) {}
            return;
        }
        if (isMaxAllOptions(it)) {
            try {
                p.getService().send_box_ThongBao_OK("Trang bị " + it.template.name + " đã đạt tất cả chỉ số TỐI ĐA (MAX ALL)!\nKhông thể tinh luyện thêm để tránh mất chỉ số hoàn hảo.");
            } catch (Exception ignored) {}
            return;
        }

        String info = "TINH LUYỆN THẦN TRANG (+0)\n"
                + "Trang bị: " + it.template.name + "\n"
                + "Chi phí: " + ZUtil.number_format(TINH_LUYEN_BERI_COST) + " Beri + " + TINH_LUYEN_RUBY_COST + " Ruby/lần\n"
                + "Random ngẫu nhiên chỉ số (có tỉ lệ bạo phát MAX ALL toàn bộ dòng).\n"
                + "Chọn số lần thực hiện:";

        String[] tlOptions = new String[]{
            "Tinh Luyện (1 lần)",
            "Auto (10 lần)",
            "Auto (30 lần)",
            "Auto đến khi MAX ALL (Tối đa 100 lần)",
            "Auto đến khi MAX ALL (Tối đa 300 lần)",
            "Nhập số lần",
            "Hủy"
        };
        byte[] tlTypes = new byte[tlOptions.length];
        for (int i = 0; i < tlTypes.length - 1; i++) tlTypes[i] = -1;
        tlTypes[tlTypes.length - 1] = 1;

        YesNoDialog ynd = new YesNoDialog(p, 69, "Tinh Luyện Thần Trang", info, tlOptions, tlTypes, (byte value) -> {
            switch (value) {
                case 0: executeAutoTinhLuyen(p, it, 1); break;
                case 1: executeAutoTinhLuyen(p, it, 10); break;
                case 2: executeAutoTinhLuyen(p, it, 30); break;
                case 3: executeAutoTinhLuyen(p, it, 100); break;
                case 4: executeAutoTinhLuyen(p, it, 300); break;
                case 5:
                    new InputDialog(p, 69, "Nhập số lần", new String[]{"Số lần muốn Auto Tinh Luyện (1-500):"}, inputs -> {
                        if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                        try {
                            int count = Integer.parseInt(inputs[0].trim());
                            if (count <= 0 || count > 500) {
                                p.getService().send_box_ThongBao_OK("Số lần không hợp lệ (1 - 500)!");
                                return;
                            }
                            executeAutoTinhLuyen(p, it, count);
                        } catch (Exception e) {
                            p.getService().send_box_ThongBao_OK("Vui lòng nhập đúng định dạng số!");
                        }
                    }).startInput();
                    break;
                default:
                    break;
            }
        });
        ynd.startYesNo();
    }

    public static void executeAutoTinhLuyen(Player p, Item_wear it, int maxLoops) {
        if (p == null || it == null || !isThanTrang(it)) return;
        if (p.trade_target != null) {
            try {
                p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            } catch (Exception ignored) {}
            return;
        }

        boolean owned = false;
        if (p.item.it_body != null) {
            for (Item_wear bodyIt : p.item.it_body) {
                if (bodyIt == it) { owned = true; break; }
            }
        }
        if (!owned && p.item.bag3 != null) {
            for (Item_wear bagIt : p.item.bag3) {
                if (bagIt == it) { owned = true; break; }
            }
        }
        if (!owned) {
            try {
                p.getService().send_box_ThongBao_OK("Vật phẩm không còn trong hành trang hoặc trên người!");
            } catch (Exception ignored) {}
            return;
        }

        if (it.levelUp > 0) {
            try {
                p.getService().send_box_ThongBao_OK("Chỉ có thể Tinh Luyện trang bị Thần Trang ở cấp +0!\nVui lòng Tách Cường Hóa về +0 trước khi Tinh Luyện.");
            } catch (Exception ignored) {}
            return;
        }

        if (isMaxAllOptions(it)) {
            try {
                p.getService().send_box_ThongBao_OK("Trang bị " + it.template.name + " đã đạt tất cả chỉ số TỐI ĐA (MAX ALL)!\nKhông cần tinh luyện thêm để tránh lãng phí.");
            } catch (Exception ignored) {}
            return;
        }

        int loopsDone = 0;
        long totalBeriSpent = 0;
        int totalRubySpent = 0;
        String stopReason = "Hoàn thành " + maxLoops + " lượt tinh luyện";
        boolean reachedMaxAll = false;

        synchronized (p.item) {
            for (int step = 0; step < maxLoops; step++) {
                if (p.get_vang() < TINH_LUYEN_BERI_COST) {
                    stopReason = "Không đủ Beri!";
                    break;
                }
                if (p.get_ngoc() < TINH_LUYEN_RUBY_COST) {
                    stopReason = "Không đủ Ruby!";
                    break;
                }

                p.update_vang(-TINH_LUYEN_BERI_COST);
                p.update_ngoc(-TINH_LUYEN_RUBY_COST);
                totalBeriSpent += TINH_LUYEN_BERI_COST;
                totalRubySpent += TINH_LUYEN_RUBY_COST;
                loopsDone++;

                TinhLuyenResult lastResult = rollTinhLuyen(it);
                it.option_item.clear();
                it.option_item.addAll(lastResult.newOptions);

                if (isMaxAllOptions(it)) {
                    reachedMaxAll = true;
                    stopReason = "ĐÃ ĐẠT TẤT CẢ CHỈ SỐ TỐI ĐA (MAX ALL)!";
                    Manager.gI().chatKTG(0, "Chúc mừng người chơi [" + p.name + "] đã tinh luyện thành công Thần Trang " + it.template.name + " đạt chỉ số HOÀN HẢO (MAX ALL)!", 5);
                    break;
                }
            }

            if (loopsDone > 0) {
                try {
                    p.updateMoney();
                    p.item.updateInventory(false);
                    p.getService().charWearing(p, false);
                    p.setAbility();
                    p.update_info_to_all();
                } catch (Exception ignored) {}
            }
        }

        if (loopsDone > 0) {
            try {
                StringBuilder sb = new StringBuilder();
                if (reachedMaxAll) {
                    sb.append("CHÚC MỪNG: TINH LUYỆN ĐẠT MAX ALL! \n");
                } else {
                    sb.append("TINH LUYỆN THÀNH CÔNG!\n");
                }
                sb.append("Trang bị: ").append(it.template.name).append("\n");
                sb.append("Số lần: ").append(loopsDone).append(" | Tốn: ").append(ZUtil.number_format(totalBeriSpent)).append(" Beri, ").append(totalRubySpent).append(" Ruby\n");
                if (stopReason != null) {
                    sb.append("Trạng thái: ").append(stopReason).append("\n");
                }
                sb.append("Chỉ số mới:\n");
                for (Option op : it.option_item) {
                    String optName = (ItemOptionTemplate.ENTRYS != null && op.id < ItemOptionTemplate.ENTRYS.size() && ItemOptionTemplate.ENTRYS.get(op.id) != null)
                            ? ItemOptionTemplate.ENTRYS.get(op.id).name : ("#" + op.id);
                    boolean isPercent = (ItemOptionTemplate.ENTRYS != null && op.id < ItemOptionTemplate.ENTRYS.size() && ItemOptionTemplate.ENTRYS.get(op.id) != null && ItemOptionTemplate.ENTRYS.get(op.id).percent == 1)
                            || ThanTrangConfig.isPercentOption(op.id);
                    if (isPercent) {
                        sb.append("• ").append(optName).append(": ").append(String.format("%.1f%%", op.param / 10.0)).append("\n");
                    } else {
                        sb.append("• ").append(optName).append(": ").append(op.param).append("\n");
                    }
                }

                p.getService().send_box_ThongBao_OK(sb.toString().trim());
            } catch (Exception ignored) {}
        } else {
            try {
                p.getService().send_box_ThongBao_OK("Tinh luyện thất bại: " + (stopReason != null ? stopReason : "Lỗi không xác định"));
            } catch (Exception ignored) {}
        }
    }
}
