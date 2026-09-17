package activities;

import model.Player;
import historys.zLog;
import network.Service;
import core.ZUtil;
import database.DbManager;
import network.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import template.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.YesNoDialog;

public class Market {

    private static short INDEX = 0;
    public static List<Market> ENTRY = new ArrayList<>();
    public byte type;
    public List<ItemMarket> item3;
    public List<PotionMarket> item47;

    public static void show_table(Player p) throws IOException {
        if (p.isDe || p instanceof model.DeTu) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng Chợ/Ký gửi!");
            return;
        }
        if (core.Manager.gI().isTopCaoThuOpenWeek()) {
            p.getService().send_box_ThongBao_OK("Máy chủ đang trong tuần đầu Open Server Đua Top Cao Thủ!\nTính năng Mua/Bán Chợ tạm thời khóa để đảm bảo tính công bằng cho toàn bộ Thuyền Trưởng.");
            return;
        }
        Message m = new Message(44);
        m.writer().writeByte(2);
        p.addmsg(m);
        m.cleanup();
        // send old item sell
        Market.update_at_market_index(p, 3);
    }

    public static void update_at_market_index(Player p, int type) throws IOException {
        if (type != 0 && type != 1 && type != 2 && type != 5 && type != 6 && type != 3) { // chua co
            // type
            System.err.println("market type not exist!!!!!");
            return;
        }
        
        Market get_market = null;
        if (type != 3) {
            Market template = Market.get_list_by_type(type);
            if (template != null) {
                get_market = new Market();
                get_market.type = (byte) type;
                get_market.item3 = new ArrayList<>();
                get_market.item47 = new ArrayList<>();
                long now = System.currentTimeMillis();
                synchronized (template.item3) {
                    for (int j = 0; j < template.item3.size(); j++) {
                        ItemMarket im = template.item3.get(j);
                        if (im.time_market > now && im.type_market != 2) {
                            get_market.item3.add(im);
                        } else if (im.type_market == 1) {
                            im.type_market = 3;
                            updateItemStatus(im);
                        }
                    }
                }
                synchronized (template.item47) {
                    for (int j = 0; j < template.item47.size(); j++) {
                        PotionMarket pm = template.item47.get(j);
                        if (pm.time_market > now && pm.type_market != 2) {
                            get_market.item47.add(pm);
                        } else if (pm.type_market == 1) {
                            pm.type_market = 3;
                            updatePotionStatus(pm);
                        }
                    }
                }
            }
            get_market.sort();
        } else {
            get_market = Market.get_list_my_item(p);
            get_market.sort();
        }
        if (get_market != null) {
            Message m = new Message(44);
            m.writer().writeByte(9);
            m.writer().writeByte(type);
            m.writer().writeShort(get_market.item3.size() + get_market.item47.size());
            for (int i = 0; i < get_market.item3.size(); i++) {
                m.writer().writeByte(3);
                Market.readUpdateItem(m.writer(), get_market.item3.get(i));
                m.writer().writeInt(get_market.item3.get(i).price_market); // price extol
                int time_remain
                        = (int) ((get_market.item3.get(i).time_market - System.currentTimeMillis())
                        / 1000);
                m.writer().writeInt(time_remain > 0 ? time_remain : 0); // time by second
                m.writer().writeByte(get_market.item3.get(i).type_market); // type market : 3: het
                // time
            }
            for (int i = 0; i < get_market.item47.size(); i++) {
                PotionMarket temp = get_market.item47.get(i);
                m.writer().writeByte(temp.category);
                m.writer().writeShort(temp.index);
                m.writer().writeShort(temp.id);
                m.writer().writeShort(temp.quant);
                m.writer().writeInt(get_market.item47.get(i).price_market); // price extol
                int time_remain
                        = (int) ((get_market.item47.get(i).time_market - System.currentTimeMillis())
                        / 1000);
                m.writer().writeInt(time_remain > 0 ? time_remain : 0); // time by second
                m.writer().writeByte(get_market.item47.get(i).type_market); // type market : 3: het
                // time
            }
            p.addmsg(m);
            m.cleanup();
        }
    }

    private void sort() {
        if (this.item3.size() > 1) {
            List<ItemMarket> item_3 = new ArrayList<>();
            while (this.item3.size() > 0) {
                ItemMarket temp_add = null;
                for (int i = 0; i < this.item3.size(); i++) {
                    if (temp_add == null) {
                        temp_add = this.item3.get(i);
                    } else {
                        if (temp_add.time_market < this.item3.get(i).time_market) {
                            temp_add = this.item3.get(i);
                        }
                    }
                }
                item_3.add(temp_add);
                this.item3.remove(temp_add);
            }
            this.item3.addAll(item_3);
            item_3.clear();
        }
        if (this.item47.size() > 1) {
            List<PotionMarket> item_47 = new ArrayList<>();
            while (this.item47.size() > 0) {
                PotionMarket temp_add = null;
                for (int i = 0; i < this.item47.size(); i++) {
                    if (temp_add == null) {
                        temp_add = this.item47.get(i);
                    } else {
                        if (temp_add.time_market < this.item47.get(i).time_market) {
                            temp_add = this.item47.get(i);
                        }
                    }
                }
                item_47.add(temp_add);
                this.item47.remove(temp_add);
            }
            this.item47.addAll(item_47);
            item_47.clear();
        }
    }

    public static Market get_list_my_item(Player p) {
        Market result = new Market();
        result.type = 3;
        result.item3 = new ArrayList<>();
        result.item47 = new ArrayList<>();
        for (int i = 0; i < Market.ENTRY.size(); i++) {
            Market temp = Market.ENTRY.get(i);
            if (temp == null) continue;
            synchronized (temp.item3) {
                for (int j = 0; j < temp.item3.size(); j++) {
                    ItemMarket im = temp.item3.get(j);
                    if (im != null && im.seller_id == p.IDPlayer) {
                        result.item3.add(im);
                    }
                }
            }
            synchronized (temp.item47) {
                for (int j = 0; j < temp.item47.size(); j++) {
                    PotionMarket pm = temp.item47.get(j);
                    if (pm != null && pm.seller_id == p.IDPlayer) {
                        result.item47.add(pm);
                    }
                }
            }
        }
        return result;
    }

    private static void readUpdateItem(DataOutputStream dos, ItemMarket it) throws IOException {
        dos.writeShort(it.index);
        dos.writeUTF(it.template.name);
        dos.writeByte(it.template.clazz);
        dos.writeByte(it.template.typeEquip);
        dos.writeShort(it.template.icon);
        dos.writeShort(it.template.level);
        dos.writeByte(it.levelUp);
        dos.writeByte(it.getColor());
        dos.writeByte(0); // is trade
        dos.writeByte(it.typelock);
        dos.writeByte(it.numHoleDaDuc);
        dos.writeInt(it.timeUse);
        dos.writeShort(it.valueChetac);
        dos.writeByte(it.isHoanMy);
        dos.writeByte(it.getSanitizedValueKichAn());
        dos.writeByte(it.option_item.size());
        for (int i = 0; i < it.option_item.size(); i++) {
            template.Option op = it.option_item.get(i);
            if (op != null) {
                dos.writeByte(op.id);
                int val = op.getParam(it.template != null ? it.template.typeEquip : 0, it.levelUp, it.isHoanMy);
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
            template.Option op2 = it.option_item_2.get(i);
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

    public static Market get_list_by_type(int type) {
        for (int i = 0; i < Market.ENTRY.size(); i++) {
            if (Market.ENTRY.get(i).type == type) {
                return Market.ENTRY.get(i);
            }
        }
        return null;
    }

    public static void process(Player p, Message m2) throws IOException {
        if (p == null || p.isClosed) return;
        if (p.trade_target != null) {
            p.getService().send_box_ThongBao_OK("Không thể thực hiện khi đang giao dịch!");
            return;
        }
        if (p.isDe || p instanceof model.DeTu) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng Chợ/Ký gửi!");
            return;
        }
        if (core.Manager.gI().isTopCaoThuOpenWeek()) {
            p.getService().send_box_ThongBao_OK("Máy chủ đang trong tuần đầu Open Server Đua Top Cao Thủ!\nTính năng Mua/Bán Chợ tạm thời khóa để đảm bảo tính công bằng cho toàn bộ Thuyền Trưởng.");
            return;
        }
        byte type = m2.reader().readByte();
        byte index_market = m2.reader().readByte();
        short id = m2.reader().readShort();
        byte cat = m2.reader().readByte();
        short value = m2.reader().readShort();

         if (p.getConnStatus() != 1){
         p.getService().send_box_ThongBao_OK("Chưa kích hoạt thành viên không thể kí vật phẩm");
           return;
        }
        if (!p.checkPassRuong()) {
            return;
        }
       
        // System.out.printf("type %s index %s id %s cat %s value %s\n", type,
        // index_market, id, cat, value);
//        Log.gI().add_log(p, "Market " + type + " " + index_market + " " + id + " " + cat + " " + value);
        if (type == 9 && id == 0 && cat == 0 && value == 1) { // update item
            // index 0 vu khi + non, 1 trang phuc, 2 trang suc, 3 ruong, 5 nguyen lieu, 6
            // vat pham
            update_at_market_index(p, index_market);
        } else if (type == 10 && index_market == -1 && cat == 3 && value == 1) { // sell item3
            if (id < 0 || id >= p.item.bag3.length) return;
            Item_wear it_select = p.item.bag3[id];
            if (it_select != null) {
                if (p.level < 1) {
                    p.getService().send_box_ThongBao_OK("Chưa đủ level 1 không thể đăng bán vật phẩm");
                    return;
                }
                if (it_select.levelUp > 10 && it_select.levelUp < 14) {
                    p.getService().send_box_ThongBao_OK("Không thể bán trang bị +11, +12, +13 ở chợ. Hãy bán qua chức năng giao dịch");
                    return;
                }
                
                if (p.get_vnd() < 2_000) {
                    p.getService().send_box_ThongBao_OK("Bạn không đủ 2000 extol để đăng bán vật phẩm");
                    return;
                }
                if (it_select.template.typeEquip != 7 && it_select.template.typeEquip > 5 && !it_select.isThanTrang()) {
                    p.getService().send_box_ThongBao_OK("Chỉ có thể đăng bán Nón, Vũ khí, Áo, Quần, Dây chuyền, Nhẫn và Thần Trang");
                    return;
                }
                if (it_select.typelock == 1) {
                    p.getService().send_box_ThongBao_OK("Các trang bị sau khi mặc lên người sẽ bị Khoá, và bạn không thể đăng bán trang bị khoá");
                    return;
                }
                if (it_select.mdakham.length > 0) {
                    p.getService().send_box_ThongBao_OK("Các trang bị đã được Khảm đá thì không thể đăng bán");
                    return;
                }
                Market get_market = Market.get_list_my_item(p);
                if (get_market == null
                        || ((get_market.item3.size() + get_market.item47.size()) >= 5)) {
                    p.getService().send_box_ThongBao_OK("Bạn không đủ chỗ trống trên chợ mua bán, tối đa bán 5 item");
                    return;
                }
                p.data_yesno = new int[]{id};
                new model.InputDialog(p, 5, "Đăng bán", new String[]{"Giá bán"}).startInput();
            }
        } 
        else if (type == 10 && index_market == -1 && id == 0 && cat == 4 && value > 0 && value < 32000) { // sell
            // berri
            long beri_add = 1_000_000L * value;
            if (p.level < 1) {
                p.getService().send_box_ThongBao_OK("Chưa đủ level 1 không thể đăng bán vật phẩm");
                return;
            }
            
            if (p.get_vnd() < 2_000) {
                p.getService().send_box_ThongBao_OK("Bạn không đủ 2000 extol để đăng bán");
                return;
            }
            if (p.get_vang() < beri_add) {
                p.getService().send_box_ThongBao_OK("Bạn không đủ " + ZUtil.number_format(beri_add) + " beri để đăng bán");
                return;
            }
            Market get_market = Market.get_list_my_item(p);
            if (get_market == null || ((get_market.item3.size() + get_market.item47.size()) >= 5)) {
                p.getService().send_box_ThongBao_OK("Bạn không đủ chỗ trống trên chợ mua bán, tối đa bán 5 item");
                return;
            }
            p.data_yesno = new int[]{value};
            new model.InputDialog(p, 6, "Đăng bán", new String[]{"Giá bán"}).startInput();
        } 
        else if (type == 10 && index_market == -1 && (cat == 4 || cat == 7) && value > 0 && value < 32000) { // sell
            // item47
            if (p.level < 1) {
                p.getService().send_box_ThongBao_OK("Chưa đủ level 1 không thể đăng bán vật phẩm");
                return;
            }
            if (p.get_vnd() < 2_000) {
                p.getService().send_box_ThongBao_OK("Bạn không đủ 2000 extol để đăng bán");
                return;
            }
            if (cat == 4) {
                if (p.item.total_item_bag_by_id(4, id) < value) {
                    p.getService().send_box_ThongBao_OK("Bạn không đủ " + value + " "
                            + ItemTemplate4.get_item_name(id) + " để đăng bán");
                    return;
                }
                if (check_it_47_cant_sell(4, id)) {
                    p.getService().send_box_ThongBao_OK("Không thể đăng bán vật phẩm này");
                    return;
                }
                if (id == 519) {
                    p.getService().send_box_ThongBao_OK("Không thể đăng bán Rương hành trình");
                    return;
                }
                if (id == 455) {
                    p.getService().send_box_ThongBao_OK("Không thể đăng bán Rương Dial");
                    return;
                }
            } else {
                if (p.item.total_item_bag_by_id(7, id) < value) {
                    p.getService().send_box_ThongBao_OK("Bạn không đủ " + value + " "
                            + ItemTemplate7.get_item_name(id) + " để đăng bán");
                    return;
                }
                if (check_it_47_cant_sell(7, id)) {
                    p.getService().send_box_ThongBao_OK("Không thể đăng bán vật phẩm này");
                    return;
                }
            }
            Market get_market = Market.get_list_my_item(p);
            if (get_market == null || ((get_market.item3.size() + get_market.item47.size()) >= 5)) {
                p.getService().send_box_ThongBao_OK("Bạn không đủ chỗ trống trên chợ mua bán, tối đa bán 5 item");
                return;
            }
            p.data_yesno = new int[]{cat, id, value};
            new model.InputDialog(p, 7, "Đăng bán", new String[]{"Giá bán"}).startInput();
        } 
        else if (type == 5 && index_market == 3 && cat == 3 && value == 1) { // receive item3
            Market index_sell = null;
            ItemMarket it_receive = null;
            for (int i = 0; i < Market.ENTRY.size(); i++) {
                Market market = Market.ENTRY.get(i);
                for (int j = 0; j < market.item3.size(); j++) {
                    if (market.item3.get(j).index == id) {
                        it_receive = market.item3.get(j);
                        index_sell = market;
                        break;
                    }
                }
                if (it_receive != null && index_sell != null) {
                    break;
                }
            }
            if (it_receive != null && it_receive.seller_id == p.IDPlayer
                    && it_receive.time_market < System.currentTimeMillis()
                    && index_sell != null) {
                p.data_yesno = new int[]{index_sell.type, it_receive.index};
                if (it_receive.type_market == 2) {
                    int price_receive = (int) (((long) it_receive.price_market * 90L) / 100L);
                    p.setyesNoDialog(new YesNoDialog(p, 19, "Thông báo",
                            ("Bạn có muốn nhận về tiền bán vật phẩm là " + price_receive
                            + " extol không?"),
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                    p.getService().startYesNo();
                } else {
                    p.setyesNoDialog(new YesNoDialog(p, 19, "Thông báo",
                            ("Bạn có muốn nhận về hành trang trang bị " + it_receive.template.name
                            + " không"),
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                    p.getService().startYesNo();
                }
            }
        } 
        else if (type == 11 && index_market == 3 && cat == 3 && value == 1) { // resell item3
            Market index_sell = null;
            ItemMarket it_receive = null;
            for (int i = 0; i < Market.ENTRY.size(); i++) {
                Market market = Market.ENTRY.get(i);
                for (int j = 0; j < market.item3.size(); j++) {
                    if (market.item3.get(j).index == id) {
                        it_receive = market.item3.get(j);
                        index_sell = market;
                        break;
                    }
                }
                if (it_receive != null && index_sell != null) {
                    break;
                }
            }
            if (it_receive != null && it_receive.seller_id == p.IDPlayer) {
                if (it_receive.levelUp > 10 && it_receive.levelUp < 14) {
                    p.getService().send_box_ThongBao_OK("Không thể bán trang bị +11, +12, +13 ở chợ. Hãy bán qua chức năng giao dịch");
                    return;
                }
                if (it_receive.type_market == 3 && it_receive.time_market < System.currentTimeMillis() && index_sell != null) {
                    p.data_yesno = new int[]{index_sell.type, it_receive.index};
                    p.setyesNoDialog(new YesNoDialog(p, 23, "Thông báo",
                            ("Bạn có muốn gia hạn thêm thời gian bán với giá 1.500 extol không?"),
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                    p.getService().startYesNo();
                }
            }
        } 
        else if (type == 5 && index_market == 3 && (cat == 4 || cat == 7) && value == 1) { // receive item47
            Market index_sell = null;
            PotionMarket it_receive = null;
            for (int i = 0; i < Market.ENTRY.size(); i++) {
                Market market = Market.ENTRY.get(i);
                for (int j = 0; j < market.item47.size(); j++) {
                    if (market.item47.get(j).index == id) {
                        it_receive = market.item47.get(j);
                        index_sell = market;
                        break;
                    }
                }
                if (it_receive != null && index_sell != null) {
                    break;
                }
            }
            if (it_receive != null && it_receive.seller_id == p.IDPlayer
                    && it_receive.time_market < System.currentTimeMillis()
                    && index_sell != null) {
                p.data_yesno = new int[]{index_sell.type, it_receive.index};
                if (it_receive.type_market == 2) {
                    int price_receive = (int) (((long) it_receive.price_market * 90L) / 100L);
                    p.setyesNoDialog(new YesNoDialog(p, 21, "Thông báo",
                            ("Bạn có muốn nhận về tiền bán vật phẩm là " + price_receive
                            + " extol không?"),
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                    p.getService().startYesNo();
                } else {
                    if (it_receive.category == 4) {
                        if (it_receive.id == 0) {
                            p.setyesNoDialog(new YesNoDialog(p, 21, "Thông báo",
                                    ("Bạn có muốn nhận " + it_receive.quant + " triệu beri về hành trang không?"),
                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                            p.getService().startYesNo();
                        } else {
                            p.setyesNoDialog(new YesNoDialog(p, 21, "Thông báo",
                                    ("Bạn có muốn nhận " + it_receive.quant + " "
                                    + ItemTemplate4.get_item_name(it_receive.id)
                                    + " về hành trang không"),
                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                            p.getService().startYesNo();
                        }
                    } else if (it_receive.category == 7) {
                        p.setyesNoDialog(new YesNoDialog(p, 21, "Thông báo",
                                ("Bạn có muốn nhận " + it_receive.quant + " "
                                + ItemTemplate7.get_item_name(it_receive.id)
                                + " về hành trang không"),
                                new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();
                    }
                }
            }
        } 
        else if (type == 6 && index_market == 3 && cat == 3 && value == 1) { // stop selling item 3
            Market index_sell = null;
            ItemMarket it_receive = null;
            for (int i = 0; i < Market.ENTRY.size(); i++) {
                Market market = Market.ENTRY.get(i);
                for (int j = 0; j < market.item3.size(); j++) {
                    if (market.item3.get(j).index == id) {
                        it_receive = market.item3.get(j);
                        index_sell = market;
                        break;
                    }
                }
                if (it_receive != null && index_sell != null) {
                    break;
                }
            }
            if (it_receive != null && it_receive.seller_id == p.IDPlayer
                    && it_receive.time_market > System.currentTimeMillis()
                    && index_sell != null) {
                p.data_yesno = new int[]{index_sell.type, it_receive.index};
                p.setyesNoDialog(new YesNoDialog(p, 20, "Thông báo",
                        ("Bạn có muốn dừng bán " + it_receive.template.name + " không?"),
                        new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                p.getService().startYesNo();
            }
        } else if (type == 6 && index_market == 3 && (cat == 4 || cat == 7) && value == 1) { // stop selling item47
            Market index_sell = null;
            PotionMarket it_receive = null;
            for (int i = 0; i < Market.ENTRY.size(); i++) {
                Market market = Market.ENTRY.get(i);
                for (int j = 0; j < market.item47.size(); j++) {
                    if (market.item47.get(j).index == id) {
                        it_receive = market.item47.get(j);
                        index_sell = market;
                        break;
                    }
                }
                if (it_receive != null && index_sell != null) {
                    break;
                }
            }
            if (it_receive != null && it_receive.seller_id == p.IDPlayer
                    && it_receive.time_market > System.currentTimeMillis()
                    && index_sell != null) {
                p.data_yesno = new int[]{index_sell.type, it_receive.index};
                if (it_receive.category == 4) {
                    if (it_receive.id == 0) {
                        p.setyesNoDialog(new YesNoDialog(p, 24, "Thông báo",
                                ("Bạn có muốn dừng bán " + it_receive.quant + " triệu beri không?"),
                                new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();
                    } else {
                        p.setyesNoDialog(new YesNoDialog(p, 24, "Thông báo",
                                ("Bạn có muốn dừng bán " + it_receive.quant + " "
                                + ItemTemplate4.get_item_name(it_receive.id) + " không?"),
                                new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();
                    }
                } else {
                    p.setyesNoDialog(new YesNoDialog(p, 24, "Thông báo",
                            ("Bạn có muốn dừng bán " + it_receive.quant + " "
                            + ItemTemplate7.get_item_name(it_receive.id) + " không?"),
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                    p.getService().startYesNo();
                }
            }
        } 
        else if (type == 0 && cat == 3 && value == 1) { // buy item3
            Market index_sell = null;
            ItemMarket it_receive = null;
            for (int i = 0; i < Market.ENTRY.size(); i++) {
                Market market = Market.ENTRY.get(i);
                for (int j = 0; j < market.item3.size(); j++) {
                    if (market.item3.get(j).index == id) {
                        it_receive = market.item3.get(j);
                        index_sell = market;
                        break;
                    }
                }
                if (it_receive != null && index_sell != null) {
                    break;
                }
            }
            if (it_receive != null && it_receive.seller_id != p.IDPlayer && index_sell != null) {
                if (p.level < 1) {
                    p.getService().send_box_ThongBao_OK("Chưa đủ level 1 không thể mua vật phẩm");
                    return;
                }
                if (it_receive.levelUp > 10 && it_receive.levelUp < 14) {
                    p.getService().send_box_ThongBao_OK("Không thể mua trang bị +11, +12, +13 ở chợ. Hãy mua qua chức năng giao dịch");
                    return;
                }
                if (it_receive.time_market > System.currentTimeMillis()) {
                    p.data_yesno = new int[]{index_sell.type, it_receive.index};
                    p.setyesNoDialog(new YesNoDialog(p, 25, "Thông báo",
                            ("Bạn có muốn mua " + it_receive.template.name + " với giá "
                            + it_receive.price_market + " extol không?"),
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                    p.getService().startYesNo();
                } else {
                    p.getService().send_box_ThongBao_OK("Vật phẩm đã hết thời gian rao bán");
                }
            }
        } 
        else if (type == 0 && ((cat == 4 && index_market == 6) || (cat == 7 && index_market == 5))
                && value == 1) { // buy
            // item47
            Market index_sell = null;
            PotionMarket it_receive = null;
            for (int i = 0; i < Market.ENTRY.size(); i++) {
                Market market = Market.ENTRY.get(i);
                for (int j = 0; j < market.item47.size(); j++) {
                    if (market.item47.get(j).index == id) {
                        it_receive = market.item47.get(j);
                        index_sell = market;
                        break;
                    }
                }
                if (it_receive != null && index_sell != null) {
                    break;
                }
            }
            if (it_receive != null && it_receive.seller_id != p.IDPlayer && index_sell != null) {
                if (it_receive.time_market > System.currentTimeMillis()) {
                    if (p.level < 1) {
                        p.getService().send_box_ThongBao_OK("Chưa đủ level 1 không thể mua vật phẩm");
                        return;
                    }
                    p.data_yesno = new int[]{index_sell.type, it_receive.index};
                    if (cat == 4) {
                        if (it_receive.id == 0) {
                            p.setyesNoDialog(new YesNoDialog(p, 26, "Thông báo",
                                    ("Bạn có muốn mua " + it_receive.quant + " triệu beri với giá "
                                    + it_receive.price_market + " extol không?"),
                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                            p.getService().startYesNo();
                        } else {
                            p.setyesNoDialog(new YesNoDialog(p, 26, "Thông báo",
                                    ("Bạn có muốn mua " + it_receive.quant + " "
                                    + ItemTemplate4.get_item_name(it_receive.id)
                                    + " với giá " + it_receive.price_market
                                    + " extol không?"),
                                    new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                            p.getService().startYesNo();
                        }
                    } else {
                        p.setyesNoDialog(new YesNoDialog(p, 26, "Thông báo",
                                ("Bạn có muốn mua " + it_receive.quant + " "
                                + ItemTemplate7.get_item_name(it_receive.id) + " với giá "
                                + it_receive.price_market + " extol không?"),
                                new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
                        p.getService().startYesNo();
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Vật phẩm đã hết thời gian rao bán");
                }
            }
        } else if (type == 8 && id == 0 && cat == 0 && value == 1) { // page
            Message m = new Message(44);
            m.writer().writeByte(8);
            m.writer().writeByte(1);
            p.addmsg(m);
            m.cleanup();
        } else if (type == 1 && id == 0 && cat == 0 && value == 1) { // update item by page
            update_at_market_index(p, index_market);
        }
    }

    public static boolean check_it_47_cant_sell(int i, int id) {
        if (i == 4) {
            ItemTemplate4 temp4 = ItemTemplate4.get_it_by_id(id);
            if (temp4 != null) {
                if (id == 519 ||id == 211 || id == 207 || id == 427 || id == 324 || id == 325 || id == 326 || id == 457 || id == 910 ||( id >= 367 && id <= 373)
                    ||( id >= 241 && id <= 270)||( id >= 647 && id <= 682)|| id == 49|| id == 55|| id == 61
                                || id == 67|| id == 73|| id == 79) {
                    return true;
                }
                if (temp4.type == 74 || temp4.id == 339 || temp4.id == 519 || temp4.id == 455) {
                    return false;
                }
                return temp4.istrade == 1;
            }
        } else { // =7
            ItemTemplate7 temp7 = ItemTemplate7.get_it_by_id(id);
            if (temp7 != null) {
                if (temp7.id == 4 || temp7.id == 10) {
                    return false;
                }
                return temp7.istrade == 1;
            }
        }
        return true;
    }

    public static void init() {
        ENTRY.clear();
        for (int t : new int[]{0, 1, 2, 5, 6}) {
            Market m = new Market();
            m.type = (byte) t;
            m.item3 = new ArrayList<>();
            m.item47 = new ArrayList<>();
            ENTRY.add(m);
        }
    }

    public static void load(Connection conn) {
        init();
        int totalLoaded = 0;
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM `market` WHERE `type_market` IN (1, 2, 3);");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                byte cat = rs.getByte("category");
                byte marketType = rs.getByte("market_type");
                Market market = get_list_by_type(marketType);
                if (market == null) {
                    continue;
                }
                if (cat == 3) {
                    ItemMarket im = new ItemMarket();
                    if (im.fromResultSet(rs)) {
                        market.item3.add(im);
                        totalLoaded++;
                    }
                } else if (cat == 4 || cat == 7) {
                    PotionMarket pm = new PotionMarket();
                    if (pm.fromResultSet(rs)) {
                        market.item47.add(pm);
                        totalLoaded++;
                    }
                }
            }
            core.Log.step("Market", totalLoaded + " items loaded across " + ENTRY.size() + " categories");
        } catch (SQLException e) {
            core.Log.error("Market", "Error loading market: " + e.getMessage());
        }
    }

    public static boolean insertItem(ItemMarket it) {
        String sql = "INSERT INTO `market` (`seller_id`, `buyer_id`, `market_type`, `category`, `item_id`, `quantity`, "
                + "`level_up`, `type_lock`, `num_hole_da_duc`, `time_use`, `value_chetac`, `is_hoan_my`, `value_kich_an`, "
                + "`option_item`, `option_item_2`, `num_lo_kham`, `mdakham`, `time_market`, `price_market`, `type_market`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, it.seller_id);
            ps.setInt(2, it.buyer_id);
            ps.setByte(3, it.market_type);
            ps.setByte(4, (byte) 3);
            ps.setInt(5, it.template != null ? it.template.id : 0);
            ps.setInt(6, 1);
            ps.setByte(7, it.levelUp);
            ps.setByte(8, it.typelock);
            ps.setByte(9, it.numHoleDaDuc);
            ps.setInt(10, it.timeUse);
            ps.setShort(11, it.valueChetac);
            ps.setByte(12, it.isHoanMy);
            ps.setByte(13, it.valueKichAn);
            ps.setString(14, it.getOptionItemJson());
            ps.setString(15, it.getOptionItem2Json());
            ps.setByte(16, it.numLoKham);
            ps.setString(17, it.getMdakhamJson());
            ps.setLong(18, it.time_market);
            ps.setInt(19, it.price_market);
            ps.setByte(20, it.type_market);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    it.id_db = rs.getInt(1);
                }
            }
            return true;
        } catch (Exception e) {
            core.Log.error("Market", "Error insertItem: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertPotion(PotionMarket it) {
        String sql = "INSERT INTO `market` (`seller_id`, `buyer_id`, `market_type`, `category`, `item_id`, `quantity`, "
                + "`level_up`, `type_lock`, `num_hole_da_duc`, `time_use`, `value_chetac`, `is_hoan_my`, `value_kich_an`, "
                + "`option_item`, `option_item_2`, `num_lo_kham`, `mdakham`, `time_market`, `price_market`, `type_market`) "
                + "VALUES (?, ?, ?, ?, ?, ?, 0, 0, 0, 0, 0, 0, -1, '[]', '[]', 0, '[]', ?, ?, ?)";
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, it.seller_id);
            ps.setInt(2, it.buyer_id);
            ps.setByte(3, it.market_type);
            ps.setByte(4, it.category);
            ps.setInt(5, it.id);
            ps.setInt(6, it.quant);
            ps.setLong(7, it.time_market);
            ps.setInt(8, it.price_market);
            ps.setByte(9, it.type_market);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    it.id_db = rs.getInt(1);
                }
            }
            return true;
        } catch (Exception e) {
            core.Log.error("Market", "Error insertPotion: " + e.getMessage());
            return false;
        }
    }

    public static void updateItemStatus(ItemMarket it) {
        if (it == null || it.id_db <= 0) return;
        String sql = "UPDATE `market` SET `type_market` = ?, `time_market` = ?, `buyer_id` = ? WHERE `id` = ?";
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setByte(1, it.type_market);
            ps.setLong(2, it.time_market);
            ps.setInt(3, it.buyer_id);
            ps.setInt(4, it.id_db);
            ps.executeUpdate();
        } catch (Exception e) {
            core.Log.error("Market", "Error updateItemStatus: " + e.getMessage());
        }
    }

    public static void updatePotionStatus(PotionMarket it) {
        if (it == null || it.id_db <= 0) return;
        String sql = "UPDATE `market` SET `type_market` = ?, `time_market` = ?, `buyer_id` = ? WHERE `id` = ?";
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setByte(1, it.type_market);
            ps.setLong(2, it.time_market);
            ps.setInt(3, it.buyer_id);
            ps.setInt(4, it.id_db);
            ps.executeUpdate();
        } catch (Exception e) {
            core.Log.error("Market", "Error updatePotionStatus: " + e.getMessage());
        }
    }

    public static void deleteItem(int id_db) {
        if (id_db <= 0) return;
        String sql = "DELETE FROM `market` WHERE `id` = ?";
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id_db);
            ps.executeUpdate();
        } catch (Exception e) {
            core.Log.error("Market", "Error deleteItem: " + e.getMessage());
        }
    }

    public static void update() {
        long now = System.currentTimeMillis();
        for (Market market : Market.ENTRY) {
            if (market == null) continue;
            if (market.item3 != null) {
                synchronized (market.item3) {
                    for (int i = 0; i < market.item3.size(); i++) {
                        ItemMarket im = market.item3.get(i);
                        if (im != null && im.type_market == 1 && im.time_market <= now) {
                            im.type_market = 3;
                            updateItemStatus(im);
                        }
                    }
                }
            }
            if (market.item47 != null) {
                synchronized (market.item47) {
                    for (int i = 0; i < market.item47.size(); i++) {
                        PotionMarket pm = market.item47.get(i);
                        if (pm != null && pm.type_market == 1 && pm.time_market <= now) {
                            pm.type_market = 3;
                            updatePotionStatus(pm);
                        }
                    }
                }
            }
        }
    }

    public synchronized static short get_index() {
        if (INDEX >= Short.MAX_VALUE - 100 || INDEX < 0) {
            INDEX = 1;
        }
        return INDEX++;
    }
}
