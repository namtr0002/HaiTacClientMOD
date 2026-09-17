package itemz;

import model.Player;
import historys.zLog;
import network.Service;
import core.ZUtil;
import network.Message;
import template.Item_wear;
import template.Option;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class UpgradeDial {

    public static void show_table(Player p) throws IOException {
        Message m = new Message(-94);
        m.writer().writeByte(7);
        p.addmsg(m);
        m.cleanup();
        p.tool_dial = new byte[]{0, 0, 0};
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
        if (type == 4 && beri_gem == 0 && num == 0) { // select item
            if (id < 0 || id >= p.item.bag3.length) return;
            Item_wear it_select = p.item.bag3[id];
            if (it_select != null) {
                if (it_select.template.typeEquip == 7) {
                    Message m = new Message(-94);
                    m.writer().writeByte(4);
                    m.writer().writeShort(id);
                    m.writer().writeByte(3);
                    m.writer().writeShort(4);
                    m.writer().writeShort(get_botvang(it_select.levelUp));
                    m.writer().writeShort(1);
                    m.writer().writeShort(get_botCH(it_select.levelUp));
                    m.writer().writeShort(13);
                    m.writer().writeShort(get_longvu(it_select.levelUp));
                    m.writer().writeInt(get_beri_up(it_select.levelUp));
                    m.writer().writeInt(get_ruby_up(it_select.levelUp));
                    m.writer().writeInt(get_extol_up(it_select.levelUp));
                    m.writer()
                            .writeByte((it_select.levelUp == 0 || it_select.levelUp == 3) ? 0 : 85);
                    p.addmsg(m);
                    m.cleanup();
                } else {
                    p.getService().send_box_ThongBao_OK("Chỉ có thể bỏ dial vào để nâng cấp");
                }
            }
            p.tool_dial = new byte[]{0, 0, 0};
        } else if (type == 5 && id == 11 && (beri_gem == 0 || beri_gem == 1)) { // bo tool
            if (num >= 0 && num <= 3) {
                Message m = new Message(-94);
                m.writer().writeByte(5);
                m.writer().writeByte(beri_gem);
                m.writer().writeShort(id);
                m.writer().writeByte(num);
                m.writer().writeByte(10);
                p.addmsg(m);
                m.cleanup();
                p.tool_dial[0] = num;
            } else {
                p.getService().send_box_ThongBao_OK("Chỉ bỏ tối đa 3 món");
            }
        } else if (type == 6 && id == 6 && (beri_gem == 0 || beri_gem == 1)) { // bo tool
            if (num >= 0 && num <= 3) {
                Message m = new Message(-94);
                m.writer().writeByte(6);
                m.writer().writeByte(beri_gem);
                m.writer().writeShort(id);
                m.writer().writeByte(num);
                m.writer().writeByte((beri_gem == 0) ? 0 : (5 * num));
                p.addmsg(m);
                m.cleanup();
                p.tool_dial[1] = num;
            } else {
                p.getService().send_box_ThongBao_OK("Chỉ bỏ tối đa 3 món");
            }
        } else if (type == 14 && id == 10 && (beri_gem == 0 || beri_gem == 1) && num >= 0
                && num <= 1) { // bo tool
            Message m = new Message(-94);
            m.writer().writeByte(14);
            m.writer().writeByte(beri_gem);
            m.writer().writeShort(id);
            p.addmsg(m);
            m.cleanup();
            p.tool_dial[2] = num;
        } else if (type == 1 && beri_gem == 0 && num == 0) { // start
            if (id < 0 || id >= p.item.bag3.length) return;
            Item_wear it_select = p.item.bag3[id];
            if (it_select != null) {
                if (it_select.template.typeEquip == 7) {
                    if (it_select.levelUp < 5) {
                        boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
                        String[] options = !isTest
                            ? new String[]{"1 lần", "5 lần", "10 lần", "Đóng"}
                            : new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"};
                        byte[] icons = new byte[options.length];
                        for (int i = 0; i < icons.length - 1; i++) icons[i] = -1;
                        icons[icons.length - 1] = 1;

                        model.YesNoDialog ynd = new model.YesNoDialog(p, 94, "Auto Nâng Cấp Dial",
                            "Chọn số lần Auto Nâng Cấp " + it_select.template.name + " (Cấp hiện tại: +" + it_select.levelUp + "/5):",
                            options, icons, value -> {
                                if (!isTest) {
                                    int count = switch (value) {
                                        case 0 -> 1;
                                        case 1 -> 5;
                                        case 2 -> 10;
                                        default -> 0;
                                    };
                                    if (count > 0) {
                                        executeAutoUpgradeDial(p, id, count);
                                    }
                                    return;
                                }
                                if (value == 7) {
                                    new model.InputDialog(p, 94, "Nhập số lần", new String[]{"Số lần muốn nâng cấp:"}, inputs -> {
                                        if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                        try {
                                            int count = Integer.parseInt(inputs[0].trim());
                                            if (count <= 0) {
                                                p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                                return;
                                            }
                                            executeAutoUpgradeDial(p, id, count);
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
                                    executeAutoUpgradeDial(p, id, count);
                                }
                            });
                        ynd.startYesNo();
                    } else {
                        p.getService().send_box_ThongBao_OK("Hiện tại trang bị đã nâng cấp tối đa");
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Chỉ có thể bỏ dial vào để nâng cấp");
                }
            }
        }
    }

    public static void executeAutoUpgradeDial(Player p, short id, int loopCount) {
        executeAutoUpgradeDial(p, id, loopCount, 5);
    }

    public static void executeAutoUpgradeDial(Player p, short id, int loopCount, int targetLevel) {
        if (p == null || p.item == null || id < 0 || id >= p.item.bag3.length) return;
        if (p.trade_target != null) {
            try { p.getService().send_box_ThongBao_OK("Không thể nâng cấp khi đang giao dịch!"); } catch (Exception ignored) {}
            return;
        }
        Item_wear it_select = p.item.bag3[id];
        if (it_select == null || it_select.template == null || it_select.template.typeEquip != 7) {
            try { p.getService().send_box_ThongBao_OK("Vật phẩm không hợp lệ!"); } catch (Exception ignored) {}
            return;
        }
        if (targetLevel > 5) targetLevel = 5;
        if (it_select.levelUp >= targetLevel) {
            try { p.getService().send_box_ThongBao_OK("Hiện tại trang bị đã đạt hoặc vượt mốc +" + targetLevel + " rồi!"); } catch (Exception ignored) {}
            return;
        }

        int startLevel = it_select.levelUp;
        int successCount = 0;
        int failCount = 0;
        long totalBeriSpent = 0;
        long totalExtolSpent = 0;
        int totalBotCHSpent = 0;
        int totalBotVangSpent = 0;
        int totalLongVuSpent = 0;
        int totalThienThachSpent = 0;
        int totalMaiRuaSpent = 0;
        int totalKhienSpent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần nâng cấp";

        byte[] tools = p.tool_dial != null ? p.tool_dial.clone() : new byte[]{0, 0, 0};

        int step = 0;
        synchronized (p.item) {
        for (; step < loopCount; step++) {
            if (it_select.levelUp >= targetLevel) {
                stopReason = "Nâng cấp đạt mốc mong muốn (+" + targetLevel + ")!";
                break;
            }

            int beri_req = get_beri_up(it_select.levelUp);
            int extol_req = get_extol_up(it_select.levelUp);
            if (extol_req < 0) {
                zLog.gI().add_log("BUG", "Bug ne 1: " + extol_req);
                break;
            }
            int botCH_req = get_botCH(it_select.levelUp);
            int botvang_req = get_botvang(it_select.levelUp);
            int longvu_req = get_longvu(it_select.levelUp);

            if (p.get_vang() < beri_req) {
                stopReason = "Không đủ " + beri_req + " Beri";
                break;
            }
            if (p.get_vnd() < extol_req) {
                stopReason = "Không đủ " + extol_req + " Extol";
                break;
            }
            if (p.item.total_item_bag_by_id(7, 1) < botCH_req) {
                stopReason = "Không đủ " + botCH_req + " Bột cường hóa";
                break;
            }
            if (p.item.total_item_bag_by_id(7, 4) < botvang_req) {
                stopReason = "Không đủ " + botvang_req + " Bột vàng";
                break;
            }
            if (p.item.total_item_bag_by_id(7, 13) < longvu_req) {
                stopReason = "Không đủ " + longvu_req + " Lông vũ";
                break;
            }

            // Check tools
            if (tools[0] > 0 && p.item.total_item_bag_by_id(7, 11) < tools[0]) {
                stopReason = "Không đủ " + tools[0] + " Thiên thạch may mắn";
                break;
            }
            if (tools[1] > 0 && p.item.total_item_bag_by_id(7, 6) < tools[1]) {
                stopReason = "Không đủ " + tools[1] + " Mai rùa";
                break;
            }
            if (tools[2] > 0 && p.item.total_item_bag_by_id(7, 10) < tools[2]) {
                stopReason = "Không đủ " + tools[2] + " Khiên";
                break;
            }

            // Deduct
            p.update_vang(-beri_req);
            p.updateVnd(-extol_req);
            totalBeriSpent += beri_req;
            totalExtolSpent += extol_req;

            p.item.remove_item47(7, 1, botCH_req);
            totalBotCHSpent += botCH_req;

            p.item.remove_item47(7, 4, botvang_req);
            totalBotVangSpent += botvang_req;

            p.item.remove_item47(7, 13, longvu_req);
            totalLongVuSpent += longvu_req;

            if (tools[0] > 0) {
                p.item.remove_item47(7, 11, tools[0]);
                totalThienThachSpent += tools[0];
            }
            if (tools[1] > 0) {
                p.item.remove_item47(7, 6, tools[1]);
                totalMaiRuaSpent += tools[1];
            }
            if (tools[2] > 0) {
                p.item.remove_item47(7, 10, tools[2]);
                totalKhienSpent += tools[2];
            }

            // Roll success: cấp cao vẫn có base rate tối thiểu 10/200 = 5%
            int baseChance = Math.max(10, 80 - it_select.levelUp * 15);
            boolean suc = (baseChance + tools[0] * 15) > ZUtil.random(200);
            if (suc) {
                it_select.levelUp++;
                successCount++;
                addBonusOptions(it_select);
                if (it_select.levelUp >= targetLevel) {
                    stopReason = "Nâng cấp đạt mốc mong muốn (+" + targetLevel + ")!";
                    step++;
                    break;
                }
            } else {
                failCount++;
                int percent_rotcap = 85 - tools[1] * 5 - tools[2] * 20;
                if (percent_rotcap > ZUtil.random(100)) {
                    if (it_select.levelUp == 2) {
                        it_select.levelUp = 1;
                    } else if (it_select.levelUp == 4) {
                        it_select.levelUp = 3;
                    }
                }
            }
        }
        }

        try {
            p.updateMoney();
            p.item.updateInventory(false);
            p.setAbility();
            p.update_info_to_all();
            p.tool_dial = new byte[]{0, 0, 0};

            if (loopCount == 1) {
                Message m = new Message(-94);
                if (successCount > 0) {
                    m.writer().writeByte(2);
                    m.writer().writeUTF("Nâng cấp thành công vật phẩm lên +" + it_select.levelUp);
                } else {
                    m.writer().writeByte(3);
                    m.writer().writeUTF("Rất tiếc nâng cấp thất bại, vật phẩm rớt cấp về +" + it_select.levelUp);
                }
                p.addmsg(m);
                m.cleanup();
            } else {
                StringBuilder summary = new StringBuilder();
                summary.append("KẾT QUẢ AUTO NÂNG CẤP DIAL:\n");
                summary.append("- Vật phẩm: ").append(it_select.template.name).append("\n");
                summary.append("- Cấp độ: +").append(startLevel).append(" -> +").append(it_select.levelUp).append("\n");
                summary.append("- Số lần thực hiện: ").append(step).append("/").append(loopCount).append("\n");
                summary.append("- Thành công: ").append(successCount).append(" lần | Thất bại: ").append(failCount).append(" lần\n");
                summary.append("- Tiêu hao: ").append(ZUtil.number_format(totalBeriSpent)).append(" Beri, ")
                        .append(ZUtil.number_format(totalExtolSpent)).append(" Extol\n");
                if (totalBotCHSpent > 0) summary.append("- Bột cường hóa: ").append(totalBotCHSpent).append(" | Bột vàng: ").append(totalBotVangSpent).append(" | Lông vũ: ").append(totalLongVuSpent).append("\n");
                if (totalThienThachSpent > 0 || totalMaiRuaSpent > 0 || totalKhienSpent > 0) {
                    summary.append("- Công cụ phụ trợ: ");
                    if (totalThienThachSpent > 0) summary.append(totalThienThachSpent).append(" Thiên thạch ");
                    if (totalMaiRuaSpent > 0) summary.append(totalMaiRuaSpent).append(" Mai rùa ");
                    if (totalKhienSpent > 0) summary.append(totalKhienSpent).append(" Khiên");
                    summary.append("\n");
                }
                summary.append("- Trạng thái: ").append(stopReason);
                p.getService().send_box_ThongBao_OK(summary.toString());
            }
        } catch (Exception ignored) {}
    }

    public static int getMaxOptionsAllowed(Item_wear dial) {
        if (dial == null || dial.template == null) return 2;
        int id = dial.template.id;
        int level = dial.levelUp;
        if (id == 12017) { // Dial Cực Phẩm (Đỏ) - tối đa 8 dòng
            return 8;
        }
        String name = dial.template.name != null ? dial.template.name.toLowerCase().trim() : "";
        if (name.contains("truyền thuyết") || dial.getColor() >= 4) {
            return Math.min(6, 4 + (level >= 3 ? 2 : (level >= 1 ? 1 : 0)));
        } else if (name.contains("thần thoại") || dial.getColor() >= 3) {
            return Math.min(5, 3 + (level >= 3 ? 2 : (level >= 1 ? 1 : 0)));
        } else if (name.contains("sử thi") || dial.getColor() >= 2) {
            return Math.min(4, 3 + (level >= 2 ? 1 : 0));
        } else { // Siêu Năng
            return Math.min(3, 2 + (level >= 3 ? 1 : 0));
        }
    }

    public static void sanitizeDialOptions(Item_wear dial) {
        if (dial == null || dial.template == null || dial.template.typeEquip != 7) return;
        if (dial.option_item == null) dial.option_item = new ArrayList<>();
        
        List<Option> cleaned = new ArrayList<>();
        java.util.Set<Integer> seen = new java.util.HashSet<>();

        for (Option op : dial.option_item) {
            if (op == null) continue;
            int optId = op.id;
            // Dial không bao giờ có Option 56 (Máu cuối) -> Tự động chuyển thành Option 17 (Tăng HP %)
            if (optId == 56) {
                optId = 17;
                op.id = 17;
            }
            // Chuẩn hóa giới hạn chỉ số chống cao ảo / bug
            int param = op.getParam();
            if (optId == 1 && param > 500) {
                // Tấn công % cũ bị ảo hàng nghìn -> chuẩn hóa lại theo template chuẩn
                param = getStandardOptionParam(dial.template.id, 1, 350);
                op.setParam(param);
            } else if (optId == 53 && param > 35) { // Miễn thương không vượt quá 25-30 ở base
                op.setParam(25);
            } else if ((optId == 49 || optId == 50 || optId == 51 || optId == 52 || optId == 63) && param > 40) {
                op.setParam(25);
            } else if (optId == 17 && param > 60) {
                op.setParam(40);
            } else if (optId == 4 && param > 50) {
                op.setParam(35);
            } else if ((optId == 10 || optId == 12 || optId == 13 || optId == 14) && param > 50) {
                op.setParam(25);
            } else if (optId >= 5 && optId <= 9 && param > 15) {
                op.setParam(5);
            }

            if (!seen.contains(optId)) {
                seen.add(optId);
                cleaned.add(op);
            }
        }

        // TỰ ĐỘNG PHỤC HỒI: Nếu Dial bị thiếu dòng (ví dụ chỉ có 1 dòng do bug cũ), bổ sung các dòng chuẩn
        List<Option> defaultOps = template.ItemTemplate3.getDefaultDialOptions(dial.template.id);
        if (defaultOps != null && !defaultOps.isEmpty()) {
            for (Option defOp : defaultOps) {
                if (defOp != null && !seen.contains((int) defOp.id)) {
                    cleaned.add(new Option(defOp.id, defOp.getParam()));
                    seen.add((int) defOp.id);
                }
            }
        }

        dial.option_item.clear();
        dial.option_item.addAll(cleaned);
    }

    private static int getStandardOptionParam(int templateId, int optionId, int fallback) {
        List<Option> defaultOps = template.ItemTemplate3.getDefaultDialOptions(templateId);
        if (defaultOps != null) {
            for (Option op : defaultOps) {
                if (op != null && op.id == optionId) {
                    return op.getParam();
                }
            }
        }
        return fallback;
    }

    public static void addBonusOptions(Item_wear it_select) {
        if (it_select == null || it_select.template == null) return;
        if (it_select.option_item == null) it_select.option_item = new ArrayList<>();

        // Tự động làm sạch và phục hồi chuẩn
        sanitizeDialOptions(it_select);

        int maxAllowed = getMaxOptionsAllowed(it_select);
        if (it_select.option_item.size() >= maxAllowed) {
            return;
        }

        String name = it_select.template.name != null ? it_select.template.name.toLowerCase().trim() : "";
        List<Option> candidatePool = new ArrayList<>();

        // Lấy danh sách các option ID đã tồn tại trên Dial để chống trùng lặp
        java.util.Set<Integer> existingIds = new java.util.HashSet<>();
        for (Option op : it_select.option_item) {
            if (op != null) existingIds.add((int) op.id);
        }

        // Pool 1: Tiềm năng (5..9) - vừa tầm 3..5 điểm
        for (int id = 5; id <= 9; id++) {
            if (!existingIds.contains(id)) {
                candidatePool.add(new Option(id, ZUtil.random(3, 6)));
            }
        }

        // Pool 2: Tự hồi HP/MP (19, 20) - vừa tầm 15..25
        if (!existingIds.contains(19)) candidatePool.add(new Option(19, ZUtil.random(15, 26)));
        if (!existingIds.contains(20)) candidatePool.add(new Option(20, ZUtil.random(15, 26)));

        // Pool 3: Chỉ số phụ (10: Chí mạng, 12: Né tránh, 13: Xuyên giáp, 14: Phản đòn, 15: HP +) - vừa tầm 15..25
        if (!existingIds.contains(10)) candidatePool.add(new Option(10, ZUtil.random(15, 26)));
        if (!existingIds.contains(12)) candidatePool.add(new Option(12, ZUtil.random(15, 26)));
        if (!existingIds.contains(13)) candidatePool.add(new Option(13, ZUtil.random(15, 26)));
        if (!existingIds.contains(14)) candidatePool.add(new Option(14, ZUtil.random(15, 26)));
        if (!existingIds.contains(15)) candidatePool.add(new Option(15, ZUtil.random(100, 201)));

        // Pool 4: Tăng HP % (Option 17) & Tăng P.Thủ % (Option 4) - vừa tầm 20..30
        if (!existingIds.contains(17)) candidatePool.add(new Option(17, ZUtil.random(20, 31)));
        if (!existingIds.contains(4))  candidatePool.add(new Option(4, ZUtil.random(20, 31)));

        // Pool 5: Cấp cao (Thần thoại, Truyền thuyết, Cực phẩm)
        if (name.contains("thần thoại") || name.contains("truyền thuyết") || name.contains("cực phẩm") || it_select.getColor() >= 3) {
            if (!existingIds.contains(53)) candidatePool.add(new Option(53, ZUtil.random(15, 26))); // Miễn thương (+1.5%..2.5%)
            if (!existingIds.contains(51)) candidatePool.add(new Option(51, ZUtil.random(15, 26))); // Giảm né đ/t
        }

        // Pool 6: Dành riêng cho Truyền thuyết / Cực phẩm
        if (name.contains("truyền thuyết") || name.contains("cực phẩm") || it_select.getColor() >= 4) {
            if (!existingIds.contains(50)) candidatePool.add(new Option(50, ZUtil.random(15, 26))); // Giảm xuyên giáp đ/t
            if (!existingIds.contains(52)) candidatePool.add(new Option(52, ZUtil.random(15, 26))); // Giảm phản đòn đ/t
            if (!existingIds.contains(63)) candidatePool.add(new Option(63, ZUtil.random(15, 26))); // Giảm miễn thương
            if (!existingIds.contains(49)) candidatePool.add(new Option(49, ZUtil.random(15, 26))); // Giảm chí mạng đ/t
        }

        while (it_select.option_item.size() < maxAllowed && !candidatePool.isEmpty()) {
            int randIdx = ZUtil.random(candidatePool.size());
            Option picked = candidatePool.remove(randIdx);
            it_select.option_item.add(picked);
            existingIds.add((int) picked.id);
        }
    }

    public static void initDialOptions(Item_wear dial) {
        if (dial == null || dial.template == null) return;
        if (dial.option_item == null) dial.option_item = new ArrayList<>();
        sanitizeDialOptions(dial);
        if (dial.levelUp > 0) {
            addBonusOptions(dial);
        }
    }

    private static int get_longvu(byte levelUp) {
        return (levelUp + 1) * 4;
    }

    private static int get_botCH(byte levelUp) {
        return (levelUp + 1) * 100;
    }

    private static int get_botvang(byte levelUp) {
        return (levelUp + 1) * 70;
    }

    private static int get_beri_up(byte levelUp) {
        return (levelUp + 1) * 500_000;
    }

    private static int get_ruby_up(byte levelUp) {
        return 0;
    }

    private static int get_extol_up(byte levelUp) {
        return (levelUp + 1) * 2_000;
    }
}
