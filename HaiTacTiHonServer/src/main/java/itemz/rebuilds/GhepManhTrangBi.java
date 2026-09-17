package itemz.rebuilds;

import zabstracts.AbsCombie;
import model.Player;
import template.Item_wear;
import network.Message;
import model.YesNoDialog;
import template.ItemTemplate4;
import template.ItemTemplate3;
import core.ZUtil;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class GhepManhTrangBi extends AbsCombie {
    @Override
    public byte getType() {
        return 14;
    }

    @Override
    public void process(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException {
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        if (type == 14 && action == 1 && cat == 4 && idItem >= 0 && num == 1) { // bo manh trang bi vao
            if (p.item.total_item_bag_by_id(cat, idItem) < num) {
                p.getService().send_box_ThongBao_OK("Không đủ trong hành trang");
                return;
            }
            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(idItem);
            if (itemTemplate4 != null) {
                if (itemTemplate4.type == 74) {
                    p.getService().sendRebuildPutItem(idItem, (byte) 4, (short) 1);
                    p.item_to_kham_ngoc_id_ngoc = idItem;
                } else {
                    p.getService().send_box_ThongBao_OK("Chỉ có thể bỏ mảnh trang bị vào");
                }
            }
        } else if (type == 14 && action == 31) { // bat dau ghep trang bi
            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(p.item_to_kham_ngoc_id_ngoc);
            if (itemTemplate4 != null) {
                if (itemTemplate4.type == 74) {
                    p.data_yesno = new int[]{itemTemplate4.id};
                    
                    int reqCount = 10;
                    String noticeText = "";
                    switch (itemTemplate4.id) {
                        case 304:
                        case 305:
                        case 306: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị trắng 9x?";
                            break;
                        }
                        case 307:
                        case 308:
                        case 309: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị xanh 9x?";
                            break;
                        }
                        case 310:
                        case 311:
                        case 312: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị tím 9x?";
                            break;
                        }
                        case 313:
                        case 314:
                        case 315: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị cam 9x?";
                            break;
                        }
                        case 536:
                        case 537:
                        case 538: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị trắng 10x?";
                            break;
                        }
                        case 539:
                        case 540:
                        case 541: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị xanh 10x?";
                            break;
                        }
                        case 542:
                        case 543:
                        case 544: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị tím 10x?";
                            break;
                        }
                        case 545:
                        case 546:
                        case 547: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị cam 10x?";
                            break;
                        }
                        case 692:
                        case 693:
                        case 694: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị 11x?";
                            break;
                        }
                        case 723:
                        case 724:
                        case 725: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị 12x?";
                            break;
                        }
                        case 720:
                        case 721:
                        case 722: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị 13x?";
                            break;
                        }
                        case 717:
                        case 718:
                        case 719: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị 14x?";
                            break;
                        }
                        case 714:
                        case 715:
                        case 716: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị 15x?";
                            break;
                        }
                        case 711:
                        case 712:
                        case 713: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị 16x?";
                            break;
                        }
                        case 708:
                        case 709:
                        case 710: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị 17x?";
                            break;
                        }
                        case 705:
                        case 706:
                        case 707: {
                            noticeText = "Bạn có muốn ghép " + reqCount + " " + itemTemplate4.name + " thành 1 trang bị 18x?";
                            break;
                        }
                        default: {
                            p.data_yesno = null;
                            p.getService().send_box_ThongBao_OK("Mảnh trang bị loại này hiện tại chưa ghép được");
                            return;
                        }
                    }
                    
                    boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
                    String[] countOptions = isTest
                            ? new String[]{"1 lần", "10 lần", "20 lần", "50 lần", "100 lần", "200 lần", "500 lần", "Nhập số lần", "Đóng"}
                            : new String[]{"1 lần", "5 lần", "10 lần", "Đóng"};
                    byte[] countTypes = isTest
                            ? new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, 1}
                            : new byte[]{-1, -1, -1, 1};

                    YesNoDialog ynd = new YesNoDialog(p, 40, "Auto Ghép Mảnh Trang Bị",
                            noticeText + "\nChọn số lần ghép:",
                            countOptions, countTypes, value -> {
                                if (isTest && value == 7) {
                                    new model.InputDialog(p, 40, "Nhập số lần", new String[]{"Số lần muốn ghép:"}, inputs -> {
                                        if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                        try {
                                            int count = Integer.parseInt(inputs[0].trim());
                                            if (count <= 0) {
                                                p.getService().send_box_ThongBao_OK("Số lần không hợp lệ!");
                                                return;
                                            }
                                            executeAutoGhepManh(p, itemTemplate4, count);
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
                                    executeAutoGhepManh(p, itemTemplate4, count);
                                }
                            });
                    ynd.startYesNo();
                } else {
                    p.getService().send_box_ThongBao_OK("Chỉ có thể bỏ mảnh trang bị vào");
                }
            }
        }
    }

    public static void executeAutoGhepManh(Player p, ItemTemplate4 itemTemplate4, int loopCount) {
        if (p == null || itemTemplate4 == null) return;
        if (p.trade_target != null) {
            try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            return;
        }
        int reqCount = 10;
        int successCount = 0;
        int totalShardsSpent = 0;
        String stopReason = "Đã hoàn thành " + loopCount + " lần ghép";
        Item_wear lastItem = null;

        synchronized (p.item) {
            for (int step = 0; step < loopCount; step++) {
                if (p.item.total_item_bag_by_id(4, itemTemplate4.id) < reqCount) {
                    stopReason = "Không đủ " + reqCount + " " + itemTemplate4.name + "!";
                    break;
                }
                if (p.item.able_bag() < 1) {
                    stopReason = "Hành trang đầy (cần ít nhất 1 ô trống)!";
                    break;
                }

                p.item.remove_item47(4, itemTemplate4.id, reqCount);
                totalShardsSpent += reqCount;

                Item_wear it_add = craftOneItem(p, itemTemplate4);
                if (it_add != null && it_add.template != null) {
                    p.item.add_item_bag3(it_add);
                    lastItem = it_add;
                    successCount++;
                }
            }

            p.item.updateInventory(false);
        }

        try {
            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO GHÉP MẢNH TRANG BỊ:\n" +
                "- Ghép thành công: " + successCount + " trang bị\n" +
                "- Tiêu hao: " + totalShardsSpent + " " + itemTemplate4.name + "\n" +
                "- Trạng thái: " + stopReason
            );
        } catch (Exception ignored) {}
    }

    public static Item_wear craftOneItem(Player p, ItemTemplate4 itemTemplate4) {
        int id_b1 = 0;
        int id_b2 = 0;
        int color = 0;
        int clazz = p.clazz;
        short[] type_equip = new short[]{0};

        switch (itemTemplate4.id) {
            case 304 -> { id_b1 = 1728; id_b2 = 1919; color = 0; type_equip = new short[]{1, 3, 5}; }
            case 305 -> { id_b1 = 1728; id_b2 = 1919; color = 0; type_equip = new short[]{2, 4}; clazz = 0; }
            case 306 -> { id_b1 = 1728; id_b2 = 1919; color = 0; type_equip = new short[]{0}; }
            case 307 -> { id_b1 = 1728; id_b2 = 1919; color = 1; type_equip = new short[]{1, 3, 5}; }
            case 308 -> { id_b1 = 1728; id_b2 = 1919; color = 1; type_equip = new short[]{2, 4}; clazz = 0; }
            case 309 -> { id_b1 = 1728; id_b2 = 1919; color = 1; type_equip = new short[]{0}; }
            case 310 -> { id_b1 = 1728; id_b2 = 1919; color = 2; type_equip = new short[]{1, 3, 5}; }
            case 311 -> { id_b1 = 1728; id_b2 = 1919; color = 2; type_equip = new short[]{2, 4}; clazz = 0; }
            case 312 -> { id_b1 = 1728; id_b2 = 1919; color = 2; type_equip = new short[]{0}; }
            case 313 -> { id_b1 = 1728; id_b2 = 1919; color = 3; type_equip = new short[]{1, 3, 5}; }
            case 314 -> { id_b1 = 1728; id_b2 = 1919; color = 3; type_equip = new short[]{2, 4}; clazz = 0; }
            case 315 -> { id_b1 = 1728; id_b2 = 1919; color = 3; type_equip = new short[]{0}; }
            case 536 -> { id_b1 = 1920; id_b2 = 2111; color = 0; type_equip = new short[]{1, 3, 5}; }
            case 537 -> { id_b1 = 1920; id_b2 = 2111; color = 0; type_equip = new short[]{2, 4}; clazz = 0; }
            case 538 -> { id_b1 = 1920; id_b2 = 2111; color = 0; type_equip = new short[]{0}; }
            case 539 -> { id_b1 = 1920; id_b2 = 2111; color = 1; type_equip = new short[]{1, 3, 5}; }
            case 540 -> { id_b1 = 1920; id_b2 = 2111; color = 1; type_equip = new short[]{2, 4}; clazz = 0; }
            case 541 -> { id_b1 = 1920; id_b2 = 2111; color = 1; type_equip = new short[]{0}; }
            case 542 -> { id_b1 = 1920; id_b2 = 2111; color = 2; type_equip = new short[]{1, 3, 5}; }
            case 543 -> { id_b1 = 1920; id_b2 = 2111; color = 2; type_equip = new short[]{2, 4}; clazz = 0; }
            case 544 -> { id_b1 = 1920; id_b2 = 2111; color = 2; type_equip = new short[]{0}; }
            case 545 -> { id_b1 = 1920; id_b2 = 2111; color = 3; type_equip = new short[]{1, 3, 5}; }
            case 546 -> { id_b1 = 1920; id_b2 = 2111; color = 3; type_equip = new short[]{2, 4}; clazz = 0; }
            case 547 -> { id_b1 = 1920; id_b2 = 2111; color = 3; type_equip = new short[]{0}; }
            case 692 -> { id_b1 = 2112; id_b2 = 2171; color = 3; type_equip = new short[]{1, 3, 5}; }
            case 693 -> { id_b1 = 2112; id_b2 = 2171; color = 3; type_equip = new short[]{2, 4}; clazz = 0; }
            case 694 -> { id_b1 = 2112; id_b2 = 2171; color = 3; type_equip = new short[]{0}; }
            case 723 -> { id_b1 = 2172; id_b2 = 2231; color = 3; type_equip = new short[]{1, 3, 5}; }
            case 724 -> { id_b1 = 2172; id_b2 = 2231; color = 3; type_equip = new short[]{2, 4}; clazz = 0; }
            case 725 -> { id_b1 = 2172; id_b2 = 2231; color = 3; type_equip = new short[]{0}; }
            case 720 -> { id_b1 = 2232; id_b2 = 2291; color = 3; type_equip = new short[]{1, 3, 5}; }
            case 721 -> { id_b1 = 2232; id_b2 = 2291; color = 3; type_equip = new short[]{2, 4}; clazz = 0; }
            case 722 -> { id_b1 = 2232; id_b2 = 2291; color = 3; type_equip = new short[]{0}; }
            case 717 -> { id_b1 = 2292; id_b2 = 2351; color = 3; type_equip = new short[]{1, 3, 5}; }
            case 718 -> { id_b1 = 2292; id_b2 = 2351; color = 3; type_equip = new short[]{2, 4}; clazz = 0; }
            case 719 -> { id_b1 = 2292; id_b2 = 2351; color = 3; type_equip = new short[]{0}; }
            case 714 -> { id_b1 = 2352; id_b2 = 2411; color = 3; type_equip = new short[]{1, 3, 5}; }
            case 715 -> { id_b1 = 2352; id_b2 = 2411; color = 3; type_equip = new short[]{2, 4}; clazz = 0; }
            case 716 -> { id_b1 = 2352; id_b2 = 2411; color = 3; type_equip = new short[]{0}; }
            case 711 -> { id_b1 = 2412; id_b2 = 2471; color = 3; type_equip = new short[]{1, 3, 5}; }
            case 712 -> { id_b1 = 2412; id_b2 = 2471; color = 3; type_equip = new short[]{2, 4}; clazz = 0; }
            case 713 -> { id_b1 = 2412; id_b2 = 2471; color = 3; type_equip = new short[]{0}; }
            case 708 -> { id_b1 = 2472; id_b2 = 2531; color = 3; type_equip = new short[]{1, 3, 5}; }
            case 709 -> { id_b1 = 2472; id_b2 = 2531; color = 3; type_equip = new short[]{2, 4}; clazz = 0; }
            case 710 -> { id_b1 = 2472; id_b2 = 2531; color = 3; type_equip = new short[]{0}; }
            case 705 -> { id_b1 = 2532; id_b2 = 2591; color = 3; type_equip = new short[]{1, 3, 5}; }
            case 706 -> { id_b1 = 2532; id_b2 = 2591; color = 3; type_equip = new short[]{2, 4}; clazz = 0; }
            case 707 -> { id_b1 = 2532; id_b2 = 2591; color = 3; type_equip = new short[]{0}; }
            default -> { return null; }
        }

        List<ItemTemplate3> list_random = new ArrayList<>();
        for (int i = 0; i < ItemTemplate3.ENTRYS.size(); i++) {
            ItemTemplate3 it_temp = ItemTemplate3.ENTRYS.get(i);
            if (it_temp.id >= id_b1 && it_temp.id <= id_b2
                    && it_temp.color == color && (clazz == 0 || it_temp.clazz == clazz)) {
                for (int j = 0; j < type_equip.length; j++) {
                    if (type_equip[j] == it_temp.typeEquip) {
                        list_random.add(it_temp);
                        break;
                    }
                }
            }
        }
        if (list_random.size() > 0) {
            Item_wear it_add = new Item_wear();
            it_add.setup_template_by_id(list_random.get(ZUtil.random(list_random.size())));
            if (it_add != null && it_add.template != null) {
                int numLoKham = (50 > ZUtil.random(120)) ? 0 : ((70 > ZUtil.random(120)) ? 1 : 2);
                it_add.numLoKham = (byte) numLoKham;
                return it_add;
            }
        }
        return null;
    }
}
