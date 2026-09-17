package itemz.rebuilds;

import zabstracts.AbsUpgrade;
import model.Player;
import template.Item_wear;
import itemz.Rebuild_Item;
import network.Message;
import model.YesNoDialog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NangCapThaoNgoc extends AbsUpgrade {

    private static NangCapThaoNgoc instance;

    public NangCapThaoNgoc() {
        instance = this;
        AbsUpgrade.register(this);
    }

    public static NangCapThaoNgoc getInstance() {
        if (instance == null) {
            instance = new NangCapThaoNgoc();
        }
        return instance;
    }

    public static NangCapThaoNgoc gI() {
        return getInstance();
    }

    @Override
    public byte getType() {
        return 3;
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
        m.writer().writeByte(3);
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
        if (cat == 3 && num == 1 && type == 3 && action == 1) { // bo item thao ngoc kham
            if (idItem < 0 || idItem >= p.item.bag3.length) {
                return;
            }
            Item_wear it_select = p.item.bag3[idItem];
            if (it_select != null) {
                if (it_select.isThanTrang() || (it_select.template != null && it_select.template.isThanTrang())) {
                    p.getService().send_box_ThongBao_OK("Trang bị Thần Trang không có đá khảm!");
                    return;
                }
                if (it_select.mdakham != null && it_select.mdakham.length > 0) {
                    p.getService().sendRebuildPutItem(idItem, (byte) 3, (short) 1);
                    p.item_to_kham_ngoc = it_select;
                } else {
                    p.getService().send_box_ThongBao_OK("Vật phẩm chưa có đá khảm!");
                }
            }
        } else if (cat == 3 && num == 1 && type == 3 && action == 6) { // bat dau thao ngoc kham
            if (p.item_to_kham_ngoc != null && p.item_to_kham_ngoc.mdakham != null && p.item_to_kham_ngoc.mdakham.length > 0) {
                int vang_req = 0;
                for (int i = 0; i < p.item_to_kham_ngoc.mdakham.length; i++) {
                    if (p.item_to_kham_ngoc.mdakham[i] >= 44
                            && p.item_to_kham_ngoc.mdakham[i] <= 79) {
                        vang_req += Rebuild_Item.PRICE_THAO_NGOC[Rebuild_Item
                                .get_percent_hop_ngoc(p.item_to_kham_ngoc.mdakham[i])];
                    } else if ((p.item_to_kham_ngoc.mdakham[i] >= 241
                            && p.item_to_kham_ngoc.mdakham[i] <= 270)
                            || (p.item_to_kham_ngoc.mdakham[i] >= 362
                            && p.item_to_kham_ngoc.mdakham[i] <= 373)) {
                        vang_req += 300;
                    } else {
                        vang_req += 350;
                    }
                }
                if (vang_req > 0) {
                    final int reqRuby = vang_req;
                    YesNoDialog ynd = new YesNoDialog(p, 1, "Thông báo",
                            "Xác nhận tháo tất cả ngọc khảm với giá " + reqRuby + " ruby?",
                            new String[]{"Có", "Không"}, new byte[]{-1, -1}, value -> {
                                if (value == 0) {
                                    if (p.trade_target != null) {
                                        p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
                                        return;
                                    }
                                    if (p.item_to_kham_ngoc != null) {
                                        boolean found = false;
                                        for (Item_wear it : p.item.bag3) {
                                            if (it == p.item_to_kham_ngoc) {
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (!found) {
                                            p.item_to_kham_ngoc = null;
                                            p.getService().send_box_ThongBao_OK("Vật phẩm không còn trong hành trang!");
                                            return;
                                        }

                                        synchronized (p.item) {
                                            if (p.get_ngoc() < reqRuby) {
                                                p.getService().send_box_ThongBao_OK("Không đủ " + reqRuby + " ruby");
                                                return;
                                            }
                                            if (p.item_to_kham_ngoc.mdakham.length > p.item.able_bag()) {
                                                p.getService().send_box_ThongBao_OK("Hãy chừa ít nhất " + p.item_to_kham_ngoc.mdakham.length
                                                        + " ô trống trong hành trang");
                                                return;
                                            }
                                            p.update_ngoc(-reqRuby);
                                            p.updateMoney();
                                            for (int i = 0; i < p.item_to_kham_ngoc.mdakham.length; i++) {
                                                p.item.add_item_bag47(4, p.item_to_kham_ngoc.mdakham[i], 1);
                                            }
                                            p.item_to_kham_ngoc.mdakham = new short[0];
                                            if (p.item_to_kham_ngoc.template != null && (p.item_to_kham_ngoc.template.typeEquip == 6 || p.item_to_kham_ngoc.template.id == 11000)) {
                                                p.item_to_kham_ngoc.numLoKham = 0;
                                                p.item_to_kham_ngoc.numHoleDaDuc = 0;
                                            }
                                            if (p.item_to_kham_ngoc.option_item_2 != null) {
                                                p.item_to_kham_ngoc.option_item_2.clear();
                                            } else {
                                                p.item_to_kham_ngoc.option_item_2 = new ArrayList<>();
                                            }
                                            p.item.updateInventory(false);
                                        }
                                        p.setAbility();
                                        p.update_info_to_all();
                                        
                                        String itemName = p.item_to_kham_ngoc.template != null ? p.item_to_kham_ngoc.template.name : "";
                                        p.getService().sendRebuildActionMsg((byte) 6, "Tháo ngọc khảm trang bị "
                                                + itemName + " thành công");
                                        p.item_to_kham_ngoc = null;
                                        p.data_yesno = null;
                                    } else {
                                        Rebuild_Item.show_table(p, 4);
                                        p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại");
                                    }
                                }
                            });
                    ynd.startYesNo();
                }
            }
        }
    }
}
