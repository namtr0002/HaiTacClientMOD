package model;


import zabstracts.AbsVongQuay;
import java.io.IOException;
import core.Manager;
import network.Service;
import core.ZUtil;
import network.Message;
import template.ItemBag47;
import template.ItemTemplate4;
import template.ItemTemplate7;

public class VongQuay extends AbsVongQuay {

    private static VongQuay instance;

    public VongQuay() {
        this.id = 1;
        this.name = "Vòng Quay Thường";
    }

    public static VongQuay gI() {
        if (instance == null) {
            instance = new VongQuay();
        }
        return instance;
    }

    public static short[] ID_ITEM = new short[]{174, 173, 221, 222, 223, 7, 159, 133, //
        112, 224, 29, 225, 48, 158};

    private static ItemBag47 get_random(Player p) {
        ItemBag47 result = null;
        if (70 > ZUtil.random(120)) {
            return result;
        }
        int random = ZUtil.random(10_000);
        if (random < 3000 && random > 2200 && p.level > 1) {
            if (70 > ZUtil.random(120)) { // RUONG AC QUY
                result = new ItemBag47();
                result.id = 29;
                result.category = 4;
                result.quant = 1;
            } else {
                result = new ItemBag47();
                result.id = 158;
                result.category = 4;
                result.quant = 1;
            }
        } else if (random < 1600 && random > 1300 && p.level > 1) { // Nguyen lieu upgrade skinn
            result = new ItemBag47();
            result.id = (short) (5 > ZUtil.random(120) ? -3 : -2);
            result.category = 7;
            result.quant = 1;
        } else if (random < 1300 && random > 1000 && p.level > 1) { // da than thoai 1, 6
            result = new ItemBag47();
            result.id = (short) (10 > ZUtil.random(120) ? 226 : 221);
            result.category = 4;
            result.quant = 1;
        } else if (random < 1000 && random > 800 && p.level > 1) { // bua ma thuat,
            result = new ItemBag47();
            result.id = (short) (50 > ZUtil.random(120) ? 222 : 223);
            result.category = 4;
            result.quant = 1;
        } else if (random < 800 && random > 500 && p.level > 1) { // sach ky nang , ve cuong hoa
            result = new ItemBag47();
            result.id = (short) (10 > ZUtil.random(120) ? 48 : 224);
            result.category = 4;
            result.quant = 1;
        } else if (random < 500 && random > 300 && p.level > 1) { // sach ky nang dac biet, ve x2 x3
            if (10 > ZUtil.random(120)) {
                result = new ItemBag47();
                result.id = (short) 225;
                result.category = 4;
                result.quant = (short) 1;
            } else {
                result = new ItemBag47();
                result.id = (short) (50 > ZUtil.random(120) ? 159 : 133);
                result.category = 4;
                result.quant = (short) 1;
            }
        } else {
            if (50 > ZUtil.random(120)) { // HP MP
                result = new ItemBag47();
                result.id = 173;
                result.category = 4;
                result.quant = (short) ZUtil.random(2, 6);
            } else {
                result = new ItemBag47();
                result.id = 174;
                result.category = 4;
                result.quant = (short) ZUtil.random(2, 6);
            }
        }
        return result;
    }

    public static void show_table(Player p) {
        try {
            p.currentVongQuay = VongQuay.gI();
            Message m = new Message(54);
            m.writer().writeByte(0);
            p.addmsg(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }

    @Override
    public void showTable(Player p) throws IOException {
        show_table(p);
    }

    @Override
    public void process(Player p, byte action, Message m2) throws IOException {

        switch (action) {
            case 3: {
                Message m = new Message(54);
                m.writer().writeByte(3);
                m.writer().writeByte(VongQuay.ID_ITEM.length);
                for (int i = 0; i < VongQuay.ID_ITEM.length; i++) {
                    m.writer().writeByte(4);
                    m.writer().writeShort(ItemTemplate4.get_it_by_id(VongQuay.ID_ITEM[i]).icon);
                }
                p.addmsg(m);
                m.cleanup();
                break;
            }
            case 4: {
                p.setyesNoDialog(new model.YesNoDialog(p, 36, "Thông báo",
                        "Bạn cần mua bao nhiêu thẻ quay? Giá mỗi thẻ quay là 15 ruby",
                        new String[]{"1 thẻ", "3 thẻ", "Nhập số lượng", "Hủy"},
                        new byte[]{-1, -1, -1, 1},
                        value -> {
                            if (value == 0) {
                                buyTicket(p, 1);
                            } else if (value == 1) {
                                buyTicket(p, 3);
                            } else if (value == 2) {
                                new model.InputDialog(p, 36, "Nhập số lượng", new String[]{"Số thẻ muốn mua:"},
                                        inputs -> {
                                            if (inputs == null || inputs.length == 0 || inputs[0].trim().isEmpty()) return;
                                            try {
                                                int count = Integer.parseInt(inputs[0].trim());
                                                if (count <= 0) {
                                                    p.getService().send_box_ThongBao_OK("Số lượng không hợp lệ!");
                                                    return;
                                                }
                                                buyTicket(p, count);
                                            } catch (NumberFormatException e) {
                                                p.getService().send_box_ThongBao_OK("Vui lòng nhập số hợp lệ!");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }).startInput();
                            }
                        }));
                p.getService().startYesNo();
                break;
            }
            case 2:
            case 1: {
                int quant_reward = 0;
                if (action == 1) {
                    if (p.item.total_item_bag_by_id(4, 232) < 1) {
                        p.getService().send_box_ThongBao_OK("Không đủ vé!");
                        return;
                    }
                    p.item.remove_item47(4, 232, 1);
                    quant_reward = 3;
                } else {
                    if (p.item.total_item_bag_by_id(4, 232) < 3) {
                        p.getService().send_box_ThongBao_OK("Không đủ vé!");
                        return;
                    }
                    p.item.remove_item47(4, 232, 3);
                    quant_reward = 9;
                }
                ItemBag47[] list_reward = new ItemBag47[quant_reward];
                for (int i = 0; i < quant_reward; i++) {
                    list_reward[i] = VongQuay.get_random(p);
                }
                if (list_reward[0] == null && list_reward[1] == null && list_reward[2] == null) {
                    ItemBag47 it = new ItemBag47();
                    it.id = (short) ZUtil.random(2, 6);
                    it.category = 4;
                    it.quant = (short) ZUtil.random(1, 6);
                    list_reward[ZUtil.random(3)] = it;
                }
                boolean add_vang = false;
                Message m = new Message(54);
                m.writer().writeByte(action);
                m.writer().writeByte(list_reward.length);
                for (int i = 0; i < list_reward.length; i++) {
                    if (list_reward[i] == null) { // lose
                        if (!add_vang && 15 > ZUtil.random(120)) {
                            int vang_receiv = (10 > ZUtil.random(120)) ? ZUtil.random(10_000, 20_000)
                                    : ZUtil.random(1_000, 2_000);
                            m.writer().writeByte(4);
                            m.writer().writeUTF("Beri");
                            m.writer().writeShort(0);
                            m.writer().writeInt(vang_receiv);
                            m.writer().writeByte(0);
                            p.update_vang(vang_receiv);
                            p.updateMoney();
                            add_vang = true;
                        } else {
                            m.writer().writeByte(0);
                            m.writer().writeUTF("");
                            m.writer().writeShort(-1);
                            m.writer().writeInt(0);
                            m.writer().writeByte(0);
                        }
                    } else {
                        if (list_reward[i].id == -2) {
                            ItemTemplate7 template7 = ItemTemplate7.get_it_by_id(16);
                            m.writer().writeByte(7); // type
                            m.writer().writeUTF(template7.name);
                            m.writer().writeShort(template7.icon);
                            m.writer().writeInt(1); // quant
                            m.writer().writeByte(0); // color
                            //
                            if (!p.item.add_item_bag47(7, template7.id, 1)) {
                                // p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống");
                            }
                        } else if (list_reward[i].id == -3) {
                            ItemTemplate7 template7 = ItemTemplate7.get_it_by_id(17);
                            m.writer().writeByte(7); // type
                            m.writer().writeUTF(template7.name);
                            m.writer().writeShort(template7.icon);
                            m.writer().writeInt(1); // quant
                            m.writer().writeByte(0); // color
                            //
                            if (!p.item.add_item_bag47(7, template7.id, 1)) {
                                // p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống");
                            }
                        } else {
                            ItemTemplate4 template4 = ItemTemplate4.get_it_by_id(list_reward[i].id);
                            m.writer().writeByte(4); // type
                            m.writer().writeUTF(template4.name);
                            m.writer().writeShort(template4.icon);
                            m.writer().writeInt(1); // quant
                            m.writer().writeByte(0); // color
                            //
                            if (!p.item.add_item_bag47(4, template4.id, 1)) {
                            }
                        }
                    }
                }
                p.addmsg(m);
                m.cleanup();
                p.item.updateInventory(false);
                break;
            }
        }
    }

    public static void buyTicket(Player p, int count) {
        if (p == null || count <= 0) return;
        int totalRuby = count * 15;
        if (p.get_ngoc() < totalRuby) {
            try { p.getService().send_box_ThongBao_OK("Không đủ " + totalRuby + " ruby!"); } catch (Exception ignored) {}
            return;
        }
        if (!p.item.can_add_item_bag47(4, 232, count)) {
            try { p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!"); } catch (Exception ignored) {}
            return;
        }
        p.update_ngoc(-totalRuby);
        try {
            p.updateMoney();
        } catch (Exception ignored) {}
        p.item.add_item_bag47(4, 232, count);
        try {
            p.item.updateInventory(false);
            Message m22 = new Message(-64);
            m22.writer().writeUTF("Mua " + count);
            p.addmsg(m22);
            m22.cleanup();
            p.getService().send_box_ThongBao_OK("Mua " + count + " Thẻ quay vòng xoay thành công!");
        } catch (Exception ignored) {}
    }
}
