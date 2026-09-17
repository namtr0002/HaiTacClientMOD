package model;

import event.EventData;
import event.EventManager;

import clan.ClanMember;
import event.SuKienNoel;
import event.eboss.SantaNoel;
import event.eboss.QuaiVatTuyetNoel;
import core.Manager;
import network.Service;
import core.ZUtil;
import event.SuKienTrongCay;
import network.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import template.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Quest {

    public static List<Quest> ENTRY = new ArrayList<>();
    public static Quest QUEST_FINISH;

    private static int numNVL(Player p) {
        int res = 0;
        for (int i = 0; i < p.list_quest.size(); i++) {
            if (p.list_quest.get(i).template.id < 0 && p.list_quest.get(i).template.id >= -1000
                    && p.list_quest.get(i).template.statusQuest != 0) {
                res++;
            }

        }
        return res;
    }
    public short id;
    public short index;
    public byte statusQuest;
    public byte typeMainSub;
    public byte typeActionQuest;
    public String name;
    public short idNpc;
    public String talk;
    public byte typeQ;
    public String strNpcMap;
    public short lvRequest;
    public String strNhacNho;
    public String showDialog;
    public short[][] data_quest;
    public short idMapHelp;
    public short idNpcSub;
    public List<GiftBox> gift;
    public String infoFinish;

    public static void add(database.DbResultRow rs) throws Exception {
        Quest t = new Quest();
        t.index = rs.getShort("index_server");
        t.statusQuest = rs.getByte("statusQuest");
        t.typeMainSub = rs.getByte("typeMainSub");
        t.typeActionQuest = rs.getByte("typeActionQuest");
        t.name = rs.getString("name");
        t.idNpc = rs.getShort("idNpc");
        t.talk = rs.getString("talk");
        t.typeQ = rs.getByte("typeQ");
        t.strNpcMap = rs.getString("strNpcMap");
        t.lvRequest = rs.getShort("lvRequest");
        t.strNhacNho = rs.getString("strNhacNho");
        t.showDialog = rs.getString("showDialog");
        JSONArray js = (JSONArray) JSONValue.parse(rs.getString("data_quest"));
        t.data_quest = new short[js.size()][];
        for (int i = 0; i < t.data_quest.length; i++) {
            JSONArray js2 = (JSONArray) js.get(i);
            t.data_quest[i] = new short[js2.size()];
            for (int j = 0; j < t.data_quest[i].length; j++) {
                t.data_quest[i][j] = Short.parseShort(js2.get(j).toString());
            }
        }
        js.clear();
        t.idMapHelp = rs.getShort("idMapHelp");
        t.idNpcSub = rs.getShort("idNpcSub");
        t.id = rs.getShort("id");
        t.gift = new ArrayList<>();
        js = (JSONArray) JSONValue.parse(rs.getString("gift"));
        for (int i = 0; i < js.size(); i++) {
            JSONArray js2 = (JSONArray) js.get(i);
            GiftBox temp = new GiftBox();
            temp.type = Byte.parseByte(js2.get(0).toString());
            temp.name = js2.get(1).toString();
            temp.icon = Short.parseShort(js2.get(2).toString());
            temp.num = Integer.parseInt(js2.get(3).toString());
            temp.color = Byte.parseByte(js2.get(4).toString());
            if (temp.type == 4) {
                if (temp.name.equals("XP Clan")) {
                    temp.id = -10;
                } else if (temp.name.equals("Bery Clan")) {
                    temp.id = -11;
                } else if (temp.name.equals("Ruby Clan")) {
                    temp.id = -12;
                } else {
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_name(temp.name);
                    if (itemTemplate4 != null && itemTemplate4.icon == temp.icon) {
                        temp.id = itemTemplate4.id;
                    } else {
                        System.exit(0);
                    }
                }
            } else if (temp.type == 7) {
                ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_name(temp.name);
                if (itemTemplate7 != null && itemTemplate7.icon == temp.icon) {
                    temp.id = itemTemplate7.id;
                }
            }
            t.gift.add(temp);
        }
        js.clear();
        t.infoFinish = rs.getString("infoFinish");
        ENTRY.add(t);
    }

    public static void add(ResultSet rs) throws SQLException {
        Quest t = new Quest();
        t.index = rs.getShort("index_server");
        t.statusQuest = rs.getByte("statusQuest");
        t.typeMainSub = rs.getByte("typeMainSub");
        t.typeActionQuest = rs.getByte("typeActionQuest");
        t.name = rs.getString("name");
        t.idNpc = rs.getShort("idNpc");
        t.talk = rs.getString("talk");
        t.typeQ = rs.getByte("typeQ");
        t.strNpcMap = rs.getString("strNpcMap");
        t.lvRequest = rs.getShort("lvRequest");
        t.strNhacNho = rs.getString("strNhacNho");
        t.showDialog = rs.getString("showDialog");
        JSONArray js = (JSONArray) JSONValue.parse(rs.getString("data_quest"));
        t.data_quest = new short[js.size()][];
        for (int i = 0; i < t.data_quest.length; i++) {
            JSONArray js2 = (JSONArray) js.get(i);
            t.data_quest[i] = new short[js2.size()];
            for (int j = 0; j < t.data_quest[i].length; j++) {
                t.data_quest[i][j] = Short.parseShort(js2.get(j).toString());
            }
        }
        js.clear();
        t.idMapHelp = rs.getShort("idMapHelp");
        t.idNpcSub = rs.getShort("idNpcSub");
        t.id = rs.getShort("id");
        t.gift = new ArrayList<>();
        js = (JSONArray) JSONValue.parse(rs.getString("gift"));
        for (int i = 0; i < js.size(); i++) {
            JSONArray js2 = (JSONArray) js.get(i);
            GiftBox temp = new GiftBox();
            temp.type = Byte.parseByte(js2.get(0).toString());
            temp.name = js2.get(1).toString();
            temp.icon = Short.parseShort(js2.get(2).toString());
            temp.num = Integer.parseInt(js2.get(3).toString());
            temp.color = Byte.parseByte(js2.get(4).toString());
            if (temp.type == 4) {
                if (temp.name.equals("XP Clan")) {
                    temp.id = -10;
                } else if (temp.name.equals("Bery Clan")) {
                    temp.id = -11;
                } else if (temp.name.equals("Ruby Clan")) {
                    temp.id = -12;
                } else {
                    ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_name(temp.name);
                    if (itemTemplate4 != null && itemTemplate4.icon == temp.icon) {
                        temp.id = itemTemplate4.id;
                    } else {
//                        System.out.println("load gift quest err " + temp.name + " id " + t.id);
                        System.exit(0);
                    }
                }
            } else if (temp.type == 7) {
                ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_name(temp.name);
                if (itemTemplate7 != null && itemTemplate7.icon == temp.icon) {
                    temp.id = itemTemplate7.id;
                } else {
//                    System.out.println("load gift quest err " + temp.name + " id " + t.id);
                    System.exit(0);
                }
            } else if (temp.type == 99) {
                temp.id = -1;
            } else {
//                System.out.println("load gift quest err " + temp.name + " id " + t.id);
                System.exit(0);
            }
            t.gift.add(temp);
        }
        t.infoFinish = rs.getString("infoFinish");
        Quest.ENTRY.add(t);
    }

    public static Quest get_quest(int id_quest) {
        for (int i = 0; i < Quest.ENTRY.size(); i++) {
            if (Quest.ENTRY.get(i).id == id_quest) {
                return Quest.ENTRY.get(i);
            }
        }
        return Quest.QUEST_FINISH;
    }

    public static void write_Quest(DataOutputStream dos, QuestP q) throws IOException {
        if (q == null || q.template == null) return;
        switch (q.template.statusQuest) {
            case 0: {
                dos.writeShort(q.template.index);
                dos.writeByte(q.template.typeMainSub);
                dos.writeByte(q.template.typeActionQuest);
                dos.writeUTF(q.template.name != null ? q.template.name : "");
                dos.writeShort(q.template.idNpc);
                dos.writeUTF(q.template.talk != null ? q.template.talk : "");
                dos.writeByte(q.template.typeQ);
                dos.writeUTF(q.template.showDialog != null ? q.template.showDialog : "");
                dos.writeUTF(q.template.strNpcMap != null ? q.template.strNpcMap : "");
                dos.writeShort(q.template.lvRequest);
                break;
            }
            case 1: {
                dos.writeShort(q.template.index);
                dos.writeByte(q.template.typeMainSub);
                dos.writeByte(q.template.typeActionQuest);
                dos.writeUTF(q.template.name != null ? q.template.name : "");
                dos.writeShort(q.template.idNpc);
                dos.writeUTF(q.template.strNhacNho != null ? q.template.strNhacNho : "");
                dos.writeUTF(q.template.showDialog != null ? q.template.showDialog : "");
                if (q.data == null) {
                    q.data = new short[q.template.data_quest != null ? q.template.data_quest.length : 0][];
                    for (int i = 0; i < q.data.length; i++) {
                        q.data[i] = new short[q.template.data_quest[i].length];
                        System.arraycopy(q.template.data_quest[i], 0, q.data[i], 0, q.template.data_quest[i].length);
                    }
                }
                dos.writeByte(q.data.length);
                for (int i = 0; i < q.data.length; i++) {
                    dos.writeByte(q.data[i][0]);
                    if (q.data[i][0] == 1 || q.data[i][0] == 2) {
                        dos.writeShort(q.data[i][1]);
                        dos.writeShort(q.data[i][2]);
                        dos.writeShort(q.data[i][3]);
                    }
                }
                dos.writeUTF(q.template.strNpcMap != null ? q.template.strNpcMap : "");
                dos.writeShort(q.template.idMapHelp);
                break;
            }
            case 2: {
                dos.writeShort(q.template.index);
                dos.writeByte(q.template.typeMainSub);
                dos.writeByte(q.template.typeActionQuest);
                dos.writeUTF(q.template.name != null ? q.template.name : "");
                dos.writeShort(q.template.idNpc);
                dos.writeShort(q.template.idNpcSub);
                dos.writeUTF(q.template.talk != null ? q.template.talk : "");
                dos.writeByte(q.template.typeQ);
                dos.writeUTF(q.template.showDialog != null ? q.template.showDialog : "");
                dos.writeUTF(q.template.strNpcMap != null ? q.template.strNpcMap : "");
                dos.writeUTF(q.template.strNhacNho != null ? q.template.strNhacNho : "");
                break;
            }
        }
    }

    public static void process(Player p, Message m2) throws IOException {
        byte act = m2.reader().readByte();
        short id = m2.reader().readShort();
        // if (p.islock_inven) {
        // new model.InputDialog(p, 13, "Nhập mã bảo vệ", new String[] { "Còn " +
        // p.time_pass_inven + " lượt thử" }).startInput();
        // return;
        // }
//         System.out.println("act " + act + " id " + id);
        QuestP temp = p.get_quest(id);
        switch (act) {
            case 1: { // do quest
                if (temp != null && temp.template.equals(Quest.QUEST_FINISH)) {
                    p.getService().send_box_ThongBao_OK("Bạn đã hoàn thành hết nhiệm vụ hiện tại");
                    return;
                }
                if (temp != null && temp.template.statusQuest == 0) {
                    // check side quest
                    if (temp.template.id < 0) {
                        QuestP mainQ = p.getMainQuest();
                        if (mainQ != null && mainQ.template != null && mainQ.template.id < 11) {
                            p.getService().send_box_ThongBao_OK("Bạn phải hoàn thành nhiệm vụ Bài tập đầu tiên mới có "
                                    + "thể nhận nhiệm vụ lặp");
                        } else if (p.nvlMax <= 0) {
                            p.getService().send_box_ThongBao_OK("Hôm nay đã làm 50/50 nhiệm vụ");
                        } else {
                            if (numNVL(p) > 0) {
                                p.getService().send_box_ThongBao_OK("Hiện tại đang thực hiện nhiệm vụ lặp khác");
                            } else {
                                p.data_yesno = new int[]{id};
                                p.setyesNoDialog(new model.YesNoDialog(p, 15, "Thông báo",
                                        ("Nhận nhiệm vụ lặp mất 3 bánh mì, bạn có muốn"
                                        + " nhận không?"),
                                        new String[]{"3", "Hủy"}, new byte[]{8, -1}));
                                p.getService().startYesNo();
                            }

                        }
                    } else {
                        // remove quest now
                        Quest.remove_old_and_send_next(p, temp);
                        // send dialog quest new
                        Message m = new Message(-23);
                        m.writer().writeByte(5);
                        m.writer().writeShort(temp.template.index);
                        p.addmsg(m);
                        m.cleanup();
                    }
                }
                break;
            }
            case 4: { // finish quest
                if (temp != null && temp.template.equals(Quest.QUEST_FINISH)) {
                    p.getService().send_box_ThongBao_OK("Bạn đã hoàn thành hết nhiệm vụ hiện tại");
                    return;
                }
                if (temp != null && temp.template.statusQuest == 2) {
                    Quest.remove_old_and_send_next(p, temp);
                    // remove item quest
                    List<ItemBag47> list_remove = new ArrayList<>();
                    for (int i = 0; i < p.item.bag47.size(); i++) {
                        if (p.item.bag47.get(i).category == 5) {
                            list_remove.add(p.item.bag47.get(i));
                        }
                    }
                    p.item.bag47.removeAll(list_remove);
                    p.item.updateInventory(false);
                }
                break;
            }
            case 2: {
                break;
            }
        }
    }

    public static void remove_old_and_send_next(Player p, QuestP temp) throws IOException {
        Message m = new Message(-23);
        m.writer().writeByte(2);
        m.writer().writeShort(temp.template.index);
        p.addmsg(m);
        m.cleanup();
        //
        if (temp.template.id < 0 && temp.template.statusQuest == 2) {
            Quest.remove_side_quest(p, temp);
        } else {
            Quest.next_quest(p, temp);
            m = new Message(-23);
            m.writer().writeByte(1);
            m.writer().writeByte(temp.template.statusQuest);
            Quest.write_Quest(m.writer(), temp);
            p.addmsg(m);
            m.cleanup();
        }
        if (p.pointPk > 0) {
            p.update_point_pk(-25);
        }
    }

    private static void remove_side_quest(Player p, QuestP questP) throws IOException {
        if (questP.template.gift.size() > 0) {
            Quest.send_gift(p, questP);
        }
        p.list_quest.remove(questP);
        Quest.update_map_have_side_quest(p, false);
    }

    public static void next_quest(Player p, QuestP questP) throws IOException {
        if (questP.template.gift.size() > 0) {
            Quest.send_gift(p, questP);
        }
        questP.template = Quest.get_quest(questP.template.id + 1);
        questP.data = new short[questP.template.data_quest.length][];
        for (int i = 0; i < questP.data.length; i++) {
            questP.data[i] = new short[questP.template.data_quest[i].length];
            for (int j = 0; j < questP.data[i].length; j++) {
                questP.data[i][j] = questP.template.data_quest[i][j];
            }
        }
    }

    private static void send_gift(Player p, QuestP questP) throws IOException {
        List<GiftBox> result = new ArrayList<>();
        for (int i = 0; i < questP.template.gift.size(); i++) {
            GiftBox temp = new GiftBox();
            temp.type = questP.template.gift.get(i).type;
            temp.name = questP.template.gift.get(i).name;
            temp.icon = questP.template.gift.get(i).icon;
            temp.num = questP.template.gift.get(i).num;
            temp.color = questP.template.gift.get(i).color;
            temp.id = questP.template.gift.get(i).id;
            result.add(temp);
        }
        if (questP.template.id >= -1000 && questP.template.id < 0) {
            p.time_nvl++;
            p.nvlMax--;
            if (p.completedRepeatQuestMaps == null) {
                p.completedRepeatQuestMaps = new java.util.HashSet<>();
            }
            if (p.map != null && p.map.template != null) {
                p.completedRepeatQuestMaps.add((int) p.map.template.id);
            }
            if (questP.template.idMapHelp > 0) {
                p.completedRepeatQuestMaps.add((int) questP.template.idMapHelp);
            }
            event.EventManager.dispatchOnNhiemVuLap(p);
            if (p.time_nvl % 10 == 0) {
                {
                    GiftBox temp = new GiftBox();
                    temp.type = 4;
                    temp.name = "ruby";
                    temp.icon = 1;
                    temp.num = 3 * (50 - p.nvlMax);
                    temp.color = 5;
                    temp.id = 1;
                    result.add(temp);
                }
                if (event.EventManager.isActive(1)) {
                    if (p.time_nvl % 20 == 0) {
                        GiftBox temp = new GiftBox();
                        temp.type = 4;
                        temp.name = "Huy hiệu đặc biệt";
                        temp.icon = 1;
                        temp.num = 85;
                        temp.color = 5;
                        temp.id = 188;
                        result.add(temp);
                    } else {
                        GiftBox temp = new GiftBox();
                        temp.type = 4;
                        temp.name = "Quả cầu pokemon đặc biệt";
                        temp.icon = 1;
                        temp.num = 85;
                        temp.color = 5;
                        temp.id = 182;
                        result.add(temp);
                    }
                } else if (event.EventManager.isActive(3)) {
                    if (p.time_nvl % 10 == 0) {
                        GiftBox temp = new GiftBox();
                        temp.type = 4;
                        temp.name = "Hộp Bánh Trung Thu";
                        temp.icon = 165;
                        temp.num = 3;
                        temp.color = 5;
                        temp.id = 211;
                        result.add(temp);
                    }
                } else if (event.EventManager.isActive(9)) {
                    EventData temp_select = null;
                    for (int i2 = 0; i2 < p.eventData.size(); i2++) {
                        if (p.eventData.get(i2).eventID == 9) {
                            temp_select = p.eventData.get(i2);
                            break;
                        }
                    }
                    if (temp_select != null && temp_select.data[SuKienTrongCay.IDX_LIEN_TANG - 1] < 6) {
                        temp_select.data[SuKienTrongCay.IDX_GIO_TRAI_CAY]++;
                    }
                }
                else if(SuKienNoel.gI().isNoel() && p.time_nvl % 10 == 0) {// vuong event noel
                    EventData eventData = p.getDataEvent(SuKienNoel.ID_EVENT);
                    if(eventData != null && eventData.data[SuKienNoel.INDEX_NVL] < 5) {
                        eventData.data[SuKienNoel.INDEX_NVL]++;
                        GiftBox temp = new GiftBox();
                        temp.type = 4;
                        temp.name = "Vé Noel";
                        temp.icon = 293;
                        temp.num = 10;
                        temp.color = 0;
                        temp.id = SuKienNoel.ITEM_VE_NOEL;
                        result.add(temp);
                    }
                }
            }
            p.updateArchiDaily(5);
            p.updateArchiDaily(8);
        } 
        
        else if (questP.template.id >= -3000 && questP.template.id < -2500) {
            if(p.clan == null) {
                return;
            }
            ClanMember clan_mem = null;
            for (int i = 0; i < p.clan.members.size(); i++) {
                if (p.clan.members.get(i).name.equals(p.name)) {
                    clan_mem = p.clan.members.get(i);
                    break;
                }
            }
            if (clan_mem != null) {
                if(!clan_mem.isJoin24H()) {
                    p.getService().send_box_ThongBao_OK("Tham gia băng trên 24h mới có thể làm nhiệm vụ băng");
                    return;
                }
                GiftBox temp = new GiftBox();
                temp.type = 4;
                temp.name = "Mảnh xu hành trình";
                temp.icon = 551;
                temp.num = ZUtil.random(1, 5);
                temp.color = 5;
                temp.id = 579;
                result.add(temp);
            }
        }
        Service.send_gift(p, 0, "Nhiệm vụ hoàn thành!", questP.template.infoFinish, result, true);
    }

    public static void send_List_Quest(Player p, boolean save_cache) {
        try {
            List<QuestP> q0 = new ArrayList<>();
            List<QuestP> q1 = new ArrayList<>();
            List<QuestP> q2 = new ArrayList<>();
            for (int i = 0; i < p.list_quest.size(); i++) {
                QuestP temp = p.list_quest.get(i);
                switch (temp.template.statusQuest) {
                    case 0: {
                        q0.add(temp);
                        break;
                    }
                    case 1: {
                        q1.add(temp);
                        break;
                    }
                    case 2: {
                        q2.add(temp);
                        break;
                    }
                }
            }
            Message m = new Message(-23);
            m.writer().writeByte(0);
            m.writer().writeByte(q0.size());
            for (int i = 0; i < q0.size(); i++) {
                Quest.write_Quest(m.writer(), q0.get(i));
            }
            m.writer().writeByte(q1.size());
            for (int i = 0; i < q1.size(); i++) {
                Quest.write_Quest(m.writer(), q1.get(i));
            }
            m.writer().writeByte(q2.size());
            for (int i = 0; i < q2.size(); i++) {
                Quest.write_Quest(m.writer(), q2.get(i));
            }
            if (save_cache) {
                p.msgs.add(m);
            } else {
                p.addmsg(m);
            }
            m.cleanup();
        } 
        catch (IOException e) {
            
        }
    }

    public static void update_map_have_side_quest(Player p, boolean save_cache) throws IOException {
        // remove job repeat status 0
        List<QuestP> list_remove = new ArrayList<>();
        for (int i = 0; i < p.list_quest.size(); i++) {
            if (p.list_quest.get(i).template.id < 0
                    && p.list_quest.get(i).template.statusQuest == 0) {
                list_remove.add(p.list_quest.get(i));
            }
        }
        if (list_remove.size() > 0) {
            for (int i = 0; i < list_remove.size(); i++) {
                Message m = new Message(-23);
                m.writer().writeByte(2);
                m.writer().writeShort(list_remove.get(i).template.index);
                if (save_cache) {
                    p.msgs.add(m);
                } else {
                    p.addmsg(m);
                }
                m.cleanup();
                p.list_quest.remove(list_remove.get(i));
            }
            list_remove.clear();
        }
        // add quest repeat new
        Quest side_q = null;
        for (int i = 0; i < Quest.ENTRY.size(); i++) {
            if (Quest.ENTRY.get(i).id < 0 && Quest.ENTRY.get(i).id >= -1000
                    && Quest.ENTRY.get(i).statusQuest == 0) {
                for (int j = 0; j < p.map.template.npcs.size(); j++) {
                    // System.out.println(p.map.template.npcs.get(j).iditem + " : " +
                    // Quest.ENTRY.get(i).idNpc);
                    if (p.map.template.npcs.get(j).idmenu == Quest.ENTRY.get(i).idNpc) {
                        side_q = Quest.ENTRY.get(i);
                        break;
                    }
                }
            }
            if (side_q != null) {
                break;
            }
        }
        if (side_q != null) {
            QuestP questP = p.get_quest(side_q.index);
            if (questP == null) {
                QuestP quest_add = new QuestP();
                quest_add.template = side_q;
                quest_add.data = new short[quest_add.template.data_quest.length][];
                for (int i = 0; i < quest_add.data.length; i++) {
                    quest_add.data[i] = new short[quest_add.template.data_quest[i].length];
                    for (int j = 0; j < quest_add.data[i].length; j++) {
                        quest_add.data[i][j] = quest_add.template.data_quest[i][j];
                    }
                }
                p.list_quest.add(quest_add);
                //
                // questP = p.get_quest(side_q.index);
                Message m = new Message(-23);
                m.writer().writeByte(1);
                m.writer().writeByte(quest_add.template.statusQuest);
                Quest.write_Quest(m.writer(), quest_add);
                if (save_cache) {
                    p.msgs.add(m);
                } else {
                    p.addmsg(m);
                }
                m.cleanup();
            }
        }
    }

    public static void add_finish_quest() {
        if (Quest.ENTRY.size() > 0) {
            short maxId = 0;
            short maxIndex = 0;
            for (Quest q : Quest.ENTRY) {
                if (q.typeMainSub == 0 && q.id >= 0) {
                    if (q.id > maxId) {
                        maxId = q.id;
                        maxIndex = q.index;
                    }
                }
            }
            QUEST_FINISH = new Quest();
            QUEST_FINISH.index = (short) (maxIndex + 1);
            QUEST_FINISH.statusQuest = 2;
            QUEST_FINISH.typeMainSub = 0;
            QUEST_FINISH.typeActionQuest = 0;
            QUEST_FINISH.name = "Làng Cối Xay Gió";
            QUEST_FINISH.idNpc = -1;
            QUEST_FINISH.talk
                    = "1 Chào mừng bạn đến server Hành Trình Hải Tặc, chúc các bạn có một trải nghiệm tuyệt vời, nhiệm vụ hiện tại đã hết";
            QUEST_FINISH.typeQ = 0;
            QUEST_FINISH.strNpcMap = "Trưởng làng - Làng Cối Xay Gió";
            QUEST_FINISH.lvRequest = 1;
            QUEST_FINISH.strNhacNho = "nhacnho";
            QUEST_FINISH.showDialog = "Hãy tiếp tục nói chuyện với trưởng làng.";
            QUEST_FINISH.data_quest = new short[0][];
            QUEST_FINISH.idMapHelp = -1;
            QUEST_FINISH.idNpcSub = -1;
            QUEST_FINISH.id = (short) (maxId + 1);
            QUEST_FINISH.gift = new ArrayList<>();
            QUEST_FINISH.infoFinish = "info finish";
        }
    }
}
