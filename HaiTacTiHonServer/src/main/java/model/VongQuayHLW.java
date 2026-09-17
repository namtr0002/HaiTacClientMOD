package model;

import zabstracts.AbsVongQuay;
import itemz.MainItem;
import itemz.RandomMainItem;
import event.EventData;
import core.Manager;
import network.Service;
import core.ZUtil;
import network.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VongQuayHLW extends AbsVongQuay {
    private static VongQuayHLW instance;

    public VongQuayHLW() {
        this.id = 3;
        this.name = "Vòng Quay Halloween";
    }

    public static VongQuayHLW gI() {
        if (instance == null) {
            instance = new VongQuayHLW();
        }
        return instance;
    }

    @Override
    public void showTable(Player p) throws IOException {
        p.currentVongQuay = this;
        try {
            Message m = new Message(54);
            m.writer().writeByte(0);
            p.addmsg(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }

    @Override
    public void process(Player p, byte action, Message m) throws IOException {
        process_instance(p, action, m);
    }

    public void process_instance(Player p, byte action, Message m) {
        int type = action;
        if(p.typeVongQuay == 1) {
            normal(p, type);
        } 
        else if(p.typeVongQuay == 2) {
            VIP(p, type);
        }

        if(p.typeVongQuay == 3) {
            normal(p, type);
        }
        else if(p.typeVongQuay == 4) {
            VIP(p, type);
        }
         else if(p.typeVongQuay == 5) {
            VIP(p, type);
        }
    }

    public static List<RandomMainItem> listNormal;
    public static List<RandomMainItem> listVIP;
    
    static {
        init();
    }
    
    public static void init() {
        listNormal = new ArrayList<>();
        listVIP = new ArrayList<>();
        
        listNormal.add(new RandomMainItem(new MainItem(29, 4, 1), 3)); // raq
        listNormal.add(new RandomMainItem(new MainItem(221, 4, 1), 3)); // dht1
        
        listNormal.add(new RandomMainItem(new MainItem(80, 4, 1), 3)); // x2 Exp sp
        listNormal.add(new RandomMainItem(new MainItem(133, 4, 1), 3)); // x3 Exp sp
        listNormal.add(new RandomMainItem(new MainItem(159, 4, 1), 3)); // x2 Skill sp
        
        listNormal.add(new RandomMainItem(new MainItem(9, 7, 1), 3)); // Da ac quy
        
        listNormal.add(new RandomMainItem(new MainItem(704, 110, 1), 1)); // Aokiji
        listNormal.add(new RandomMainItem(new MainItem(705, 110, 1), 1)); // Crocodile
        listNormal.add(new RandomMainItem(new MainItem(706, 110, 1), 1)); // Fan Cung
        listNormal.add(new RandomMainItem(new MainItem(707, 110, 1), 1));
        listNormal.add(new RandomMainItem(new MainItem(708, 110, 1), 1));
        
        listNormal.add(new RandomMainItem(new MainItem(226, 4, 1), 2)); // dht6
        listNormal.add(new RandomMainItem(new MainItem(339, 4, 1), 2)); // bua so cap
        listNormal.add(new RandomMainItem(new MainItem(158, 4, 1), 2)); // rdaq
        
        listNormal.add(new RandomMainItem(new MainItem(737, 4, 1), 3)); // x2 Exp dt
        listNormal.add(new RandomMainItem(new MainItem(738, 4, 1), 3)); // x3 Exp dt
        listNormal.add(new RandomMainItem(new MainItem(733, 4, 1), 3)); // x2 Skill dt
        
        ////////////////////////////////////////////////////////////////////////
        
        listVIP = new ArrayList<>();
        
        listVIP.add(new RandomMainItem(new MainItem(46, 4, 1), 3)); // Da c3
        
        listVIP.add(new RandomMainItem(new MainItem(29, 4, 1), 3)); // raq
        listVIP.add(new RandomMainItem(new MainItem(221, 4, 1), 3)); // dht1
        
        listVIP.add(new RandomMainItem(new MainItem(80, 4, 1), 3)); // x2 Exp sp
        listVIP.add(new RandomMainItem(new MainItem(133, 4, 1), 3)); // x3 Exp sp
        listVIP.add(new RandomMainItem(new MainItem(159, 4, 1), 3)); // x2 Skill sp
        
        listVIP.add(new RandomMainItem(new MainItem(9, 7, 1), 3)); // Da ac quy
        // thời trang
        listVIP.add(new RandomMainItem(new MainItem(20, 105, 1), 1)); // Akainu
        listVIP.add(new RandomMainItem(new MainItem(32, 105, 1), 1)); // Fujitora
 
        // Pet
        listVIP.add(new RandomMainItem(new MainItem(773, 110, 1), 1));
        listVIP.add(new RandomMainItem(new MainItem(775, 110, 1), 1));
        
        listVIP.add(new RandomMainItem(new MainItem(226, 4, 1), 3)); // dht6
        listVIP.add(new RandomMainItem(new MainItem(339, 4, 1), 3)); // bua so cap
        listVIP.add(new RandomMainItem(new MainItem(158, 4, 1), 3)); // rdaq
    }
    
    public static void normal(Player p, int action) {
        try {
            switch (action) {
                case 3: {
                    Message m = new Message(54);
                    m.writer().writeByte(3);
                    m.writer().writeByte(Math.min(14, listNormal.size()));
                    for (RandomMainItem randomMainItem : listNormal) {
                        m.writer().writeByte(randomMainItem.item.cat);
                        m.writer().writeShort(randomMainItem.item.idIcon);
                    }
                    p.addmsg(m);
                    m.cleanup();
                    break;
                }
                case 1:
                case 2: {
                    int valueRoll = action == 1 ? 1 : 3;
                    
                    if(p.item.total_item_bag_by_id(4, 801) < valueRoll) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ " + valueRoll + " Vé vòng quay thường");
                        return;
                    }
                    
                    p.item.remove_item47(4, 801, valueRoll);
                    
                    List<MainItem> list = new ArrayList<>();
                    
                    for(int i = 0; i < valueRoll * 3; i++) {
                        if(ZUtil.random(1000) < 666) {
                            list.add(null);
                            continue;
                        }
                        
                        MainItem itemRandom = RandomMainItem.random(listNormal);
                        MainItem itemAdd = new MainItem(itemRandom.id, itemRandom.cat, 1);
                        
                        //random thời trang vv or 1 ngày
                        if(itemAdd.cat == 110) {
                            itemAdd.time = 60_000L * 60 * 24 * 1;
                            itemAdd.limitDay = 7;
                            if((itemAdd.id == 704 || itemAdd.id == 705 || itemAdd.id == 706 || itemAdd.id == 707) && ZUtil.random(500) < 1) {
                                itemAdd.time = -1;
                                Manager.gI().chatKTG(0, p.name + " nhận được " + itemAdd.name + " vĩnh viễn khi quay vòng quay thường, thật đáng ngưỡng mộ", 5);
                            }
                            if(itemAdd.id == 708 && ZUtil.random(1000) < 2) {
                                itemAdd.time = -1;
                                Manager.gI().chatKTG(0, p.name + " nhận được " + itemAdd.name + " vĩnh viễn khi quay vòng quay thường, thật đáng ngưỡng mộ", 5);
                            }
                        }
                        
                        //random Đá hải thạch 1 - 5
                        if(itemAdd.cat == 4) {
                            if(itemAdd.id == 221) {
                                itemAdd = new MainItem(itemAdd.id += ZUtil.random(5), 4, 1);
                            }
                        }
                        
                        list.add(itemAdd);
                    }
                    List<MainItem> listAdd = new ArrayList<>();
                    
                    Message m = new Message(54);
                    m.writer().writeByte(action);
                    m.writer().writeByte(list.size());
                    for (MainItem mainItem : list) {
                        if(mainItem == null) {
                            m.writer().writeByte(-1);
                            m.writer().writeUTF("");
                            m.writer().writeShort(-1);
                            m.writer().writeInt(-1);
                            m.writer().writeByte(-1);
                        } else {
                            m.writer().writeByte(mainItem.cat);
                            m.writer().writeUTF(mainItem.name);
                            m.writer().writeShort(mainItem.idIcon);
                            m.writer().writeInt(1);
                            m.writer().writeByte(0);
                            
                            listAdd.add(mainItem);
                        }
                    }
                    
                    MainItem.showGiftBox(p, "", "", listAdd, true, false);
                    p.addmsg(m);
                    m.cleanup();
                    p.item.updateInventory(false);
                    
                    list.clear();
                    listAdd.clear();
                    break;
                }   
            } 
        } 
        catch (IOException e) {
            
        }
    }
    
    public static void VIP(Player p, int action) {
        try {
            switch (action) {
                case 3: {
                    Message m = new Message(54);
                    m.writer().writeByte(3);
                    m.writer().writeByte(Math.min(14, listVIP.size()));
                    for (RandomMainItem randomMainItem : listVIP) {
                        m.writer().writeByte(randomMainItem.item.cat);
                        m.writer().writeShort(randomMainItem.item.idIcon);
                    }
                    p.addmsg(m);
                    m.cleanup();
                    break;
                }
                case 1:
                case 2: {
                    int valueRoll = action == 1 ? 1 : 3;
                    
                    if(p.item.total_item_bag_by_id(4, 866) < valueRoll) {
                        p.getService().send_box_ThongBao_OK("Bạn không đủ " + valueRoll + " Vé vòng quay sự kiện");
                        return;
                    }
                    
                    p.item.remove_item47(4, 866, valueRoll);
                    
                    List<MainItem> list = new ArrayList<>();
                    
                    for(int i = 0; i < valueRoll * 3; i++) {
                        if(ZUtil.random(1000) < 555) {
                            list.add(null);
                            continue;
                        }
                        
                        MainItem itemRandom = RandomMainItem.random(listVIP);
                        MainItem itemAdd = new MainItem(itemRandom.id, itemRandom.cat, 1);
                        
                        //random thời trang vv or 1 ngày
                        if(itemAdd.cat == 105) {
                            itemAdd.time = 60_000L * 60 * 24 * 1;
                            itemAdd.limitDay = 7;
                            if((itemAdd.id == 20 || itemAdd.id == 32) && ZUtil.random(500) < 1) {
                                itemAdd.time = -1;
                                Manager.gI().chatKTG(0, p.name + " nhận được " + itemAdd.name + " vĩnh viễn khi quay vòng quay sự kiện, thật đáng ngưỡng mộ", 5);
                            }
                        }
                        if(itemAdd.cat == 110) {
                            itemAdd.time = 60_000L * 60 * 24 * 1;
                            itemAdd.limitDay = 7;
                            if((itemAdd.id == 773|| itemAdd.id == 775) && ZUtil.random(1500) < 1) {
                                itemAdd.time = -1;
                                Manager.gI().chatKTG(0, p.name + " nhận được " + itemAdd.name + " vĩnh viễn khi quay vòng quay sự kiện, thật đáng ngưỡng mộ", 5);
                            }
                        }
                        
                        if(itemAdd.cat == 4) {
                            //random Đá hải thạch 1 - 5
                            if(itemAdd.id == 221) {
                                itemAdd = new MainItem(itemAdd.id += ZUtil.random(5), 4, 1);
                            }
                            
                            //random Đá c3 trừ né
                            if(itemAdd.id == 46) {
                                int[] mDac3 = new int[] {46, 52, 58, 64, 70, 76};
                                itemAdd = new MainItem(mDac3[ZUtil.random(mDac3.length)], 4, 1);
                            }
                        }
                        
                        list.add(itemAdd);
                    }
                    List<MainItem> listAdd = new ArrayList<>();
                    
                    Message m = new Message(54);
                    m.writer().writeByte(action);
                    m.writer().writeByte(list.size());
                    for (MainItem mainItem : list) {
                        if(mainItem == null) {
                            m.writer().writeByte(-1);
                            m.writer().writeUTF("");
                            m.writer().writeShort(-1);
                            m.writer().writeInt(-1);
                            m.writer().writeByte(-1);
                        } else {
                            m.writer().writeByte(mainItem.cat);
                            m.writer().writeUTF(mainItem.name);
                            m.writer().writeShort(mainItem.idIcon);
                            m.writer().writeInt(1);
                            m.writer().writeByte(0);
                            
                            listAdd.add(mainItem);
                        }
                    }
                    MainItem.showGiftBox(p, "", "", listAdd, true, false);
                    p.addmsg(m);
                    m.cleanup();
                    p.item.updateInventory(false);
                    
                    list.clear();
                    listAdd.clear();
                    break;
                }   
            } 
        } 
        catch (IOException e) {
            
        }
    }
}
