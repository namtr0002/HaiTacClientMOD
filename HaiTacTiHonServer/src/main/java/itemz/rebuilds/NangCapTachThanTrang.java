package itemz.rebuilds;

import zabstracts.AbsUpgrade;
import model.Player;
import template.Item_wear;
import template.ItemTemplate7;
import network.Message;
import model.YesNoDialog;
import core.ZUtil;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * NangCapTachThanTrang - Bàn Tách Cường Hóa Thần Trang chuẩn trực quan
 * Tách Thần Trang (+1..+20) về +0:
 * - Đồ giữ lại KHÔNG XÓA
 * - Cấp giảm về +0
 * - Chỉ số chuẩn giảm về +0 (không bug chỉ số)
 * - Chỉ thu về nguyên liệu (Bột Cường Hóa, Bột Tím, Bột Vàng, Bột Siêu Cấp)
 * - Bỏ Beri và Ruby hoàn trả (không hoàn trả Beri/Ruby)
 */
public class NangCapTachThanTrang extends AbsUpgrade {

    public static final byte TYPE_UPGRADE = 17;
    private static NangCapTachThanTrang instance;

    public NangCapTachThanTrang() {
        instance = this;
        AbsUpgrade.register(this);
    }

    public static NangCapTachThanTrang getInstance() {
        if (instance == null) {
            instance = new NangCapTachThanTrang();
        }
        return instance;
    }

    public static NangCapTachThanTrang gI() {
        return getInstance();
    }

    @Override
    public byte getType() {
        return TYPE_UPGRADE;
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
        m.writer().writeByte(12); // Bàn Rebuild chuẩn trực quan Client
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
        if (action == 1 && cat == 3) { // Đặt Thần Trang vào bàn tách cường hóa
            Item_wear it_select = (idItem >= 0 && idItem < p.item.bag3.length) ? p.item.bag3[idItem] : null;
            if (it_select != null) {
                if (!it_select.isThanTrang()) {
                    p.getService().send_box_ThongBao_OK("Bàn này chỉ dành cho trang bị Thần Trang (Type 8-15)!");
                    return;
                }
                if (it_select.levelUp <= 0) {
                    p.getService().send_box_ThongBao_OK("Trang bị này đang ở cấp +0, không có cấp cường hóa để tách!");
                    return;
                }
                p.getService().sendRebuildPutItem(idItem, (byte) 3, (short) 1);
                p.item_to_kham_ngoc = it_select;
            }
        } else if ((action == 20 || action == 24 || action == 2 || action == 6 || action == 15) && (cat == 0 || cat == 3)) { // Bắt đầu tách cường hóa
            if (p.item_to_kham_ngoc == null) {
                p.getService().send_box_ThongBao_OK("Vui lòng chọn 1 trang bị Thần Trang (+1 trở lên) đặt vào bàn trước!");
                return;
            }
            Item_wear it = p.item_to_kham_ngoc;
            if (!it.isThanTrang()) {
                p.getService().send_box_ThongBao_OK("Chức năng chỉ dành cho trang bị Thần Trang (Type 8-15)!");
                return;
            }
            if (it.levelUp <= 0) {
                p.getService().send_box_ThongBao_OK("Trang bị này đang ở cấp +0, không có cấp cường hóa để tách!");
                return;
            }
            confirmTachCuongHoa(p, it);
        }
    }

    public static void openSelectTachThanTrangDialog(Player p) throws IOException {
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
                if (it != null && it.isThanTrang() && it.levelUp > 0) {
                    thanTrangList.add(it);
                    names.add("[Đang Mặc] " + it.template.name + " (+" + it.levelUp + ")");
                }
            }
        }

        if (p.item.bag3 != null) {
            for (int i = 0; i < p.item.bag3.length; i++) {
                Item_wear it = p.item.bag3[i];
                if (it != null && it.isThanTrang() && it.levelUp > 0) {
                    thanTrangList.add(it);
                    names.add(it.template.name + " (+" + it.levelUp + ")");
                }
            }
        }

        if (thanTrangList.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Bạn không có trang bị Thần Trang đã cường hóa (+1 trở lên) nào để tách!");
            return;
        }

        names.add("Đóng");
        byte[] types = new byte[names.size()];
        for (int i = 0; i < types.length - 1; i++) types[i] = -1;
        types[types.length - 1] = 1;

        YesNoDialog ynd = new YesNoDialog(p, 69, "TÁCH CƯỜNG HÓA THẦN TRANG",
                "Chọn Thần Trang (+1..+20) bạn muốn Tách Cường Hóa về +0 (Nhận lại 50% bột):",
                names.toArray(new String[0]), types, (byte value) -> {
            if (p.trade_target != null) {
                try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
                return;
            }
            if (value >= 0 && value < thanTrangList.size()) {
                Item_wear selected = thanTrangList.get(value);
                if (selected != null) {
                    if (!selected.isThanTrang()) {
                        try {
                            p.getService().send_box_ThongBao_OK("Chức năng chỉ dành cho trang bị Thần Trang (Type 8-15)!");
                        } catch (Exception ignored) {}
                        return;
                    }
                    if (selected.levelUp <= 0) {
                        try {
                            p.getService().send_box_ThongBao_OK("Trang bị này đang ở cấp +0, không có cấp cường hóa để tách!");
                        } catch (Exception ignored) {}
                        return;
                    }
                    confirmTachCuongHoa(p, selected);
                }
            }
        });
        ynd.startYesNo();
    }

    public static void confirmTachCuongHoa(Player p, Item_wear it) {
        if (p == null || it == null || !it.isThanTrang() || it.levelUp <= 0) return;
        if (p.trade_target != null) {
            try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
            return;
        }

        int totalBotCH = 0;
        int totalBotTim = 0;
        int totalBotVang = 0;
        int totalBotSieuCap = 0;

        for (int lvl = 0; lvl < it.levelUp; lvl++) {
            int stoneId = NangCapThanTrang.getRequiredStoneId(lvl);
            int stoneNum = NangCapThanTrang.getRequiredStoneNum(lvl);
            if (stoneId == 1) {
                totalBotCH += stoneNum;
            } else if (stoneId == 3) {
                totalBotTim += stoneNum;
            } else if (stoneId == 4) {
                totalBotVang += stoneNum;
            } else if (stoneId == 18) {
                totalBotSieuCap += stoneNum;
            }
        }

        int refundCH = (totalBotCH > 0) ? Math.max(1, (totalBotCH * 50) / 100) : 0;
        int refundTim = (totalBotTim > 0) ? Math.max(1, (totalBotTim * 50) / 100) : 0;
        int refundVang = (totalBotVang > 0) ? Math.max(1, (totalBotVang * 50) / 100) : 0;
        int refundSieuCap = (totalBotSieuCap > 0) ? Math.max(1, (totalBotSieuCap * 50) / 100) : 0;

        StringBuilder matList = new StringBuilder();
        int bagSlotsNeeded = 0;
        if (refundCH > 0) {
            matList.append("- ").append(refundCH).append("x Bột Cường Hóa\n");
            bagSlotsNeeded++;
        }
        if (refundTim > 0) {
            matList.append("- ").append(refundTim).append("x Bột Tím\n");
            bagSlotsNeeded++;
        }
        if (refundVang > 0) {
            matList.append("- ").append(refundVang).append("x Bột Vàng\n");
            bagSlotsNeeded++;
        }
        if (refundSieuCap > 0) {
            matList.append("- ").append(refundSieuCap).append("x Bột Siêu Cấp\n");
            bagSlotsNeeded++;
        }

        String notice = "XÁC NHẬN TÁCH CƯỜNG HÓA THẦN TRANG\n"
                + "Trang bị: " + it.template.name + " (+" + it.levelUp + " -> +0)\n"
                + "Thu về nguyên liệu (50%):\n"
                + matList.toString()
                + "Trang bị sẽ được giữ nguyên (không xóa) và giảm cấp về +0 để tinh luyện lại!";

        final int finalBagNeeded = bagSlotsNeeded;
        final int finalRefundCH = refundCH;
        final int finalRefundTim = refundTim;
        final int finalRefundVang = refundVang;
        final int finalRefundSieuCap = refundSieuCap;

        YesNoDialog ynd = new YesNoDialog(p, 69, "Tách Cường Hóa Thần Trang", notice,
                new String[]{"Đồng ý Tách", "Hủy"}, new byte[]{-1, 1}, (byte value) -> {
            if (value == 0) {
                if (p.trade_target != null) {
                    try { p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!"); } catch (Exception ignored) {}
                    return;
                }
                if (it == null || it.levelUp <= 0) return;

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
                    try { p.getService().send_box_ThongBao_OK("Vật phẩm không còn trong hành trang hoặc trên người!"); } catch (Exception ignored) {}
                    return;
                }

                synchronized (p.item) {
                    if (it.levelUp <= 0) return;
                    if (p.item.able_bag() < finalBagNeeded) {
                        try {
                            p.getService().send_box_ThongBao_OK("Hành trang cần ít nhất " + finalBagNeeded + " ô trống để nhận lại nguyên liệu!");
                        } catch (Exception ignored) {}
                        return;
                    }

                    if (finalRefundCH > 0) p.item.add_item_bag47(7, (short) 1, finalRefundCH);
                    if (finalRefundTim > 0) p.item.add_item_bag47(7, (short) 3, finalRefundTim);
                    if (finalRefundVang > 0) p.item.add_item_bag47(7, (short) 4, finalRefundVang);
                    if (finalRefundSieuCap > 0) p.item.add_item_bag47(7, (short) 18, finalRefundSieuCap);

                    int oldLv = it.levelUp;
                    it.levelUp = 0; // Giảm về +0, không xóa đồ

                    p.item.updateInventory(false);
                    p.setAbility(); // Giảm chỉ số chuẩn theo cấp +0, không bug chỉ số
                    p.update_info_to_all();
                    String itName = it.template != null ? it.template.name : "";
                    try {
                        p.getService().sendRebuildActionMsg((byte) 6, "Tách cường hóa " + itName + " (+" + oldLv + " -> +0) thành công!");
                    } catch (Exception ignored) {}
                    p.item_to_kham_ngoc = null;
                    p.data_yesno = null;
                }
            }
        });
        ynd.startYesNo();
    }
}
