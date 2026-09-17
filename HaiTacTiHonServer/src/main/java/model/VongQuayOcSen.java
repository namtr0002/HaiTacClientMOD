package model;

import zabstracts.AbsVongQuay;
import java.io.IOException;
import network.Service;
import core.ZUtil;
import database.DbManager;
import network.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.ItemFashion;
import template.ItemFashionP2;
import template.ItemTemplate4;
import template.ItemTemplate7;
import historys.zLog;

public class VongQuayOcSen extends AbsVongQuay {

    private static VongQuayOcSen instance;

    public VongQuayOcSen() {
        this.id = 2;
        this.name = "Vòng Quay Ốc Sên";
    }

    public static VongQuayOcSen gI() {
        if (instance == null) {
            instance = new VongQuayOcSen();
        }
        return instance;
    }
    
    public static HashMap<String, byte[]> ENTRY;
    public static short[] ID = new short[] {80, 48, 221, 7, 159, 10, 5, 232, 6, 416, 40, 6, 158, 0,
        362, 85, 225, 338, 339, 131, 322, 83};
    public static byte[] CAT =
        new byte[] {4, 4, 4, 7, 4, 7, 7, 4, 7, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 105};

    static {
        ENTRY = new HashMap<>();
        historys.VongQuayOcSenHistory.load(ENTRY);
    }

    public static void send_table(Player p) throws IOException {
        Message m = new Message(77);
        m.writer().writeByte(0);
        m.writer().writeUTF("Vòng Quay Ốc Sên");
        p.addmsg(m);
        m.cleanup();
        if (p.itemVongQuayOcSen == null) {
            p.itemVongQuayOcSen = ENTRY.get(p.name);
            if (p.itemVongQuayOcSen == null) {
                p.itemVongQuayOcSen = new byte[ID.length];
                ENTRY.put(p.name, p.itemVongQuayOcSen);
            }
        }
    }

    @Override
    public void showTable(Player p) throws IOException {
        send_table(p);
    }

    @Override
    public void process(Player p, byte act, Message m2) throws IOException {
        if (p.getConnStatus() != 1){
            p.getService().send_box_ThongBao_OK("Chưa kích hoạt thành viên không thể tham gia");
            return;
        }
        if (act == 1) {
            Message m = new Message(77);
            m.writer().writeByte(1);
            m.writer().writeByte(ID.length);
            for (int i = 0; i < ID.length; i++) {
                m.writer().writeByte(i);
                m.writer().writeByte(CAT[i]);
                if (CAT[i] == 7) {
                    m.writer().writeShort(ItemTemplate7.get_it_by_id(ID[i]).icon);
                    m.writer().writeInt(1);
                } else if (CAT[i] == 105) {
                    m.writer().writeShort(ItemFashion.get_item(ID[i]).idIcon);
                    m.writer().writeInt(1);
                } else {
                    m.writer().writeShort(ItemTemplate4.get_it_by_id(ID[i]).icon);
                    m.writer().writeInt(1);
                }
                m.writer().writeByte(p.itemVongQuayOcSen[i]);
            }
            p.addmsg(m);
            m.cleanup();
        } else if (act == 3) {
            if (checkItemAvailable(p)) {
                p.data_yesno = new int[] {74};
                p.setyesNoDialog(new model.YesNoDialog(p, 74, "Thông báo",
                    ("Bạn sẽ mất " + ZUtil.number_format(getRubyQuay(p))
                        + " Ruby để quay vòng quay ốc sên lượt này?"),
                    new String[] {"Đồng ý", "Hủy"}, new byte[] {2, 1}));
                p.getService().startYesNo();
            } else {
                VongQuayOcSen.reset(p);
            }
        } else if (act == 4) {
            if (p.item.total_item_bag_by_id(4, 441) > 0) {
                p.item.remove_item47(4, 441, 1);
                int index = ZUtil.random(p.itemVongQuayOcSen.length);
                int time = 0;
                while (p.itemVongQuayOcSen[index] == 1) {
                    index = ZUtil.random(p.itemVongQuayOcSen.length);
                    if (time > 1_000) {
                        p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại sau!");
                        return;
                    }
                    time++;
                }
                if (95 > ZUtil.random(100)) {
                    if (VongQuayOcSen.CAT[index] == 105) {
                        while (p.itemVongQuayOcSen[index] == 1) {
                            index = ZUtil.random(p.itemVongQuayOcSen.length);
                            if (time > 1_000) {
                                p.getService().send_box_ThongBao_OK("Có lỗi xảy ra, hãy thử lại sau!");
                                return;
                            }
                            time++;
                        }
                    }
                }
                p.itemVongQuayOcSen[index] = 1;
                Message m = new Message(77);
                m.writer().writeByte(2);
                m.writer().writeByte(index);
                p.addmsg(m);
                m.cleanup();
                String rewardName = "";
                if (VongQuayOcSen.CAT[index] == 7) {
                    ItemTemplate7 it_temp7 = ItemTemplate7.get_it_by_id(VongQuayOcSen.ID[index]);
                    if (it_temp7 != null) {
                        p.item.add_item_bag47(7, it_temp7.id, 1);
                        rewardName = it_temp7.name;
                    }
                } else if (VongQuayOcSen.CAT[index] == 105) {
                    ItemFashion itf = ItemFashion.get_item(VongQuayOcSen.ID[index]);
                    if (itf != null) {
                        rewardName = itf.name;
                        if (p.check_fashion(itf.ID) == null) {
                            ItemFashionP2 temp2 = new ItemFashionP2();
                            temp2.id = itf.ID;
                            p.fashion.add(temp2);
                            p.update_fashionP2(temp2);
                            if (p.map != null && p.map.players != null) {
                                for (int i = 0; i < p.map.players.size(); i++) {
                                    Player p0 = p.map.players.get(i);
                                    if (p0 != null && p0.getService() != null) {
                                        p0.getService().charWearing(p, false);
                                    }
                                }
                            }
                            p.getService().UpdateInfoMaincharInfo();
                        }
                    }
                } else {
                    ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(VongQuayOcSen.ID[index]);
                    if (it_temp4 != null) {
                        rewardName = it_temp4.name;
                        if (it_temp4.id == 0) {
                            p.update_vang(20_000);
                            p.updateMoney();
                            rewardName = "20,000 Beri";
                        } else if (it_temp4.id == 6) {
                            p.update_ticket(1);
                            p.updateMoney();
                            rewardName = "1 Vé";
                        } else {
                            p.item.add_item_bag47(4, it_temp4.id, 1);
                        }
                    }
                }
                p.item.updateInventory(false);
                // Log
                String logData = String.format("{\"action\":\"VONG_QUAY_OC_SEN_ITEM\", \"item_spent\":1, \"index\":%d, \"cat\":%d, \"item_id\":%d, \"reward\":\"%s\"}",
                        index, VongQuayOcSen.CAT[index], VongQuayOcSen.ID[index], rewardName);
                zLog.gI().add_log(p, "VONG_QUAY_OC_SEN", logData);
            } else {
                p.getService().send_box_ThongBao_OK("Bạn không đủ ốc sên");
            }
        }
    }

    private static void reset(Player p) throws IOException {
        p.itemVongQuayOcSen = new byte[ID.length];
        ENTRY.put(p.name, p.itemVongQuayOcSen);
        VongQuayOcSen.send_table(p);
    }

    public static int getRubyQuay(Player p) {
        int result = 100;
        for (int i = 0; i < p.itemVongQuayOcSen.length; i++) {
            if (p.itemVongQuayOcSen[i] == 1) {
                result += 50;
            }
        }
        return result;
    }

    private static boolean checkItemAvailable(Player p) {
        for (int i = 0; i < p.itemVongQuayOcSen.length; i++) {
            if (p.itemVongQuayOcSen[i] == 0) {
                return true;
            }
        }
        return false;
    }

    public static void updateDB() {
        historys.VongQuayOcSenHistory.updateDB(ENTRY);
    }
}
