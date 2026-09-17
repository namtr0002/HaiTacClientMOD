package itemz.rebuilds;

import zabstracts.AbsUpgrade;
import model.Player;
import template.Item_wear;
import network.Message;
import model.YesNoDialog;
import java.io.IOException;

public class NangCapCongCheTac extends AbsUpgrade {

    private static NangCapCongCheTac instance;

    public NangCapCongCheTac() {
        instance = this;
        AbsUpgrade.register(this);
    }

    public static NangCapCongCheTac getInstance() {
        if (instance == null) {
            instance = new NangCapCongCheTac();
        }
        return instance;
    }

    public static NangCapCongCheTac gI() {
        return getInstance();
    }

    @Override
    public byte getType() {
        return 12;
    }

    @Override
    public void showTable(Player p) throws IOException {
        p.setUpgrade(this);
        Message m = new Message(-67);
        m.writer().writeByte(0);
        m.writer().writeByte(12);
        p.addmsg(m);
        m.cleanup();
        p.item_to_kham_ngoc = null;
        p.item_to_kham_ngoc_id_ngoc = -1;
        p.data_yesno = null;
    }

    public static boolean canCongCheTac(Item_wear it) {
        if (it == null || it.template == null) return false;
        if (it.isThanTrang()) {
            return false;
        }
        return (it.template.typeEquip < 6 || it.template.typeEquip == 7);
    }

    @Override
    public void process(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException {
        if (type == 12 && action == 1 && cat == 3 && num == 1) { // bo item cong che tac
            Item_wear it_select = p.item.bag3[idItem];
            if (it_select != null) {
                if (it_select.isThanTrang()) {
                    p.getService().send_box_ThongBao_OK("Trang bị Thần Trang (Type 8-15) không thể phục hồi điểm chế tác!");
                    return;
                }
                if (canCongCheTac(it_select)) {
                    p.getService().sendRebuildPutItem(idItem, (byte) 3, (short) 1);
                    p.item_to_kham_ngoc = it_select;
                }
            }
        } else if (type == 12 && (action == 24 || action == 20) && cat == 0) { // bat dau cong che tac
            if (p.item_to_kham_ngoc != null && p.item_to_kham_ngoc.isThanTrang()) {
                p.getService().send_box_ThongBao_OK("Trang bị Thần Trang (Type 8-15) không thể phục hồi điểm chế tác!");
                return;
            }
            if (p.item_to_kham_ngoc != null) {
                Item_wear it = p.item_to_kham_ngoc;
                if (it.valueChetac >= 100) {
                    p.getService().send_box_ThongBao_OK("Trang bị này đã đạt tối đa 100 điểm chế tác!");
                    return;
                }
                int neededPoints = 100 - it.valueChetac;
                int fullRubyCost = ((neededPoints + 9) / 10) * 100;

                YesNoDialog ynd = new YesNoDialog(p, 31, "Phục Hồi Chế Tác",
                        "Điểm chế tác hiện tại: " + it.valueChetac + "/100 (Cần " + neededPoints + "đ để max)\n"
                        + "Chọn phương thức phục hồi:",
                        new String[]{"Hồi Full 100đ (" + fullRubyCost + " Ruby)", "100 Ruby (+10đ)", "5 Ruby (+1đ)", "Đóng"},
                        new byte[]{7, 7, 7, 1}, value -> {
                            if (value == 0) { // Hồi full 100đ
                                if (p.item_to_kham_ngoc != null && canCongCheTac(p.item_to_kham_ngoc)) {
                                    int need = 100 - p.item_to_kham_ngoc.valueChetac;
                                    if (need <= 0) {
                                        p.getService().send_box_ThongBao_OK("Trang bị đã max 100 điểm chế tác!");
                                        return;
                                    }
                                    int rubyNeed = ((need + 9) / 10) * 100;
                                    if (p.get_ngoc() < rubyNeed) {
                                        // Hồi tối đa theo số ruby hiện có
                                        int maxTenBlocks = p.get_ngoc() / 100;
                                        if (maxTenBlocks > 0) {
                                            int actualRuby = maxTenBlocks * 100;
                                            p.update_ngoc(-actualRuby);
                                            p.updateMoney();
                                            p.item_to_kham_ngoc.valueChetac += (maxTenBlocks * 10);
                                            if (p.item_to_kham_ngoc.valueChetac > 100) p.item_to_kham_ngoc.valueChetac = 100;
                                            p.getService().sendRebuildActionMsg((byte) 25, "Bạn phục hồi " + (maxTenBlocks * 10) + " điểm chế tác thành công");
                                            p.item.updateInventory(false);
                                        } else {
                                            p.getService().send_box_ThongBao_OK("Không đủ " + rubyNeed + " ruby để hồi full!");
                                        }
                                        return;
                                    }
                                    p.update_ngoc(-rubyNeed);
                                    p.updateMoney();
                                    p.item_to_kham_ngoc.valueChetac = 100;
                                    p.item.updateInventory(false);
                                    p.getService().send_box_ThongBao_OK("Phục hồi thành công 100/100 điểm chế tác! (Tiêu hao " + rubyNeed + " Ruby)");
                                }
                            } else if (value == 1) { // 100 ruby -> 10 points
                                if (p.item_to_kham_ngoc != null && canCongCheTac(p.item_to_kham_ngoc)) {
                                    if (p.get_ngoc() < 100) {
                                        p.getService().send_box_ThongBao_OK("Không đủ 100 ruby");
                                        return;
                                    }
                                    p.update_ngoc(-100);
                                    p.updateMoney();
                                    p.item_to_kham_ngoc.valueChetac += 10;
                                    if (p.item_to_kham_ngoc.valueChetac >= 100) {
                                        p.item_to_kham_ngoc.valueChetac = 100;
                                    }
                                    
                                    p.getService().sendRebuildActionMsg((byte) 25, "Bạn phục hồi 10 điểm chế tác thành công (Hiện tại: " + p.item_to_kham_ngoc.valueChetac + "/100)");
                                    p.item.updateInventory(false);
                                }
                            } else if (value == 2) { // 5 ruby -> 1 point
                                if (p.item_to_kham_ngoc != null && canCongCheTac(p.item_to_kham_ngoc)) {
                                    if (p.get_ngoc() < 5) {
                                        p.getService().send_box_ThongBao_OK("Không đủ 5 ruby");
                                        return;
                                    }
                                    p.update_ngoc(-5);
                                    p.updateMoney();
                                    p.item_to_kham_ngoc.valueChetac++;
                                    if (p.item_to_kham_ngoc.valueChetac >= 100) {
                                        p.item_to_kham_ngoc.valueChetac = 100;
                                    }
                                    
                                    p.getService().sendRebuildActionMsg((byte) 25, "Bạn phục hồi 1 điểm chế tác thành công (Hiện tại: " + p.item_to_kham_ngoc.valueChetac + "/100)");
                                    p.item.updateInventory(false);
                                }
                            }
                        });
                ynd.startYesNo();
            }
        }
    }
}
