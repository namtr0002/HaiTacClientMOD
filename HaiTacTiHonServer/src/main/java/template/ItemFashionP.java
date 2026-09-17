package template;


import java.io.IOException;

import model.Player;
import core.ZUtil;
import network.Message;

public class ItemFashionP {

    public byte category;
    public short id;
    public short icon;
    public boolean is_use;
    public ItemHair template;
    
    public ItemFashionP(int id, int icon, int cat, boolean isUse) {
        this.id = (short) id;
        this.icon = (short) icon;
        this.category = (byte) cat;
        this.is_use = isUse;
        if (cat == 103 || cat == 108) {
            this.template = ItemHair.get_item(id, cat);
        }
    }

    public static void show_table(Player p, int type) throws IOException {
        switch (type) {
            case 103: {
                Message m = new Message(-19);
                m.writer().writeByte(103);
                m.writer().writeUTF("Tiệm tóc");
                m.writer().writeByte(103);
                m.writer().writeShort(ItemHair.get_size_type(103));
                for (int i = 0; i < ItemHair.ENTRYS.size(); i++) {
                    ItemHair temp = ItemHair.ENTRYS.get(i);
                    if (temp != null && temp.type == 103) {
                        if (p.getConnVersionInt() >= 129) {
                            m.writer().writeShort(temp.ID);
                        } else {
                            m.writer().writeByte(temp.ID);
                        }
                        m.writer().writeUTF(temp.name != null ? temp.name : "");
                        m.writer().writeByte(0);
                        m.writer().writeShort(temp.idIcon);
                        m.writer().writeShort(0);
                        
                        if (p.check_itfashionP(temp.ID, 103) != null) {
                            m.writer().writeInt(0);
                            m.writer().writeShort(0);
                        } else {
                            m.writer().writeInt(temp.beri);
                            m.writer().writeShort(temp.ruby);
                        }
                    }
                }
                p.addmsg(m);
                m.cleanup();
                break;
            }
            case 102: {
                Message m = new Message(-19);
                m.writer().writeByte(102);
                m.writer().writeUTF("Đóng thuyền");
                m.writer().writeByte(102);
                m.writer().writeShort(ItemBoat.ENTRYS.size());
                for (int i = 0; i < ItemBoat.ENTRYS.size(); i++) {
                    m.writer().writeByte(ItemBoat.ENTRYS.get(i).id);
                    m.writer().writeUTF(ItemBoat.ENTRYS.get(i).name);
                    m.writer().writeByte(ItemBoat.ENTRYS.get(i).type);
                    m.writer().writeShort(ItemBoat.ENTRYS.get(i).idimg);
                    m.writer().writeShort(ItemBoat.ENTRYS.get(i).icon);
                    //
                    m.writer().writeInt(0);
                    ItemBoatP my_boat = p.check_itboat(ItemBoat.ENTRYS.get(i).id);
                    m.writer().writeShort(my_boat == null ? 5 : 0);
                }
                p.addmsg(m);
                m.cleanup();
                break;
            }
            case 105: {
                int ver_ = p.getConnVersionInt();
                Message m = new Message(-19);
                m.writer().writeByte(105);
                m.writer().writeUTF("Thời trang");
                m.writer().writeByte(105);
                m.writer().writeShort(ItemFashion.ENTRYS.size());
                for (int i = 0; i < ItemFashion.ENTRYS.size(); i++) {
                    if(p.getConnVersionInt() >= 129) {
                        m.writer().writeShort(ItemFashion.ENTRYS.get(i).ID);
                    } else {
                        m.writer().writeByte(ItemFashion.ENTRYS.get(i).ID);
                    }
                    m.writer().writeUTF(ItemFashion.ENTRYS.get(i).name);
                    ItemFashionP2 myFashion = p.check_fashion(ItemFashion.ENTRYS.get(i).ID);
                    String info = ItemFashion.ENTRYS.get(i).info;
                    if (myFashion != null) {
                        if (myFashion.level > 0) {
                        
                        info += "\n (+" + myFashion.level + ") Toàn bộ chỉ số được + "
                                + Upgrade_Skin_Info.get_op_level(myFashion.level) + "%";
                        }
                        long time = myFashion.expires;
                        if (time != -1) {
                            
                                info += ("\nHạn sử dụng: còn " + ZUtil.get_time_str_by_sec2(time - System.currentTimeMillis()));
                            
                        } else {
                             info += "\nHạn sử dụng vĩnh viễn";
                        }
                    } else {
                        if (ItemFashion.ENTRYS.get(i).hsd > -1) {
                              info += "\nHạn sử dụng : còn " + ItemFashion.ENTRYS.get(i).hsd+ " day";
                        } else {
                            info += "\nHạn sử dụng vĩnh viễn";
                        }
                    }
                    m.writer().writeUTF(info);
                    m.writer().writeShort(ItemFashion.ENTRYS.get(i).idIcon);
                    m.writer().writeByte(ItemFashion.ENTRYS.get(i).mWearing.length);
                    for (int j = 0; j < ItemFashion.ENTRYS.get(i).mWearing.length; j++) {
                        m.writer().writeShort(ItemFashion.ENTRYS.get(i).mWearing[j]);
                    }
                    if (myFashion != null) {
                        m.writer().writeInt(0);
                        m.writer().writeShort(0);
                    } else {
                        if (core.Manager.gI().isTestMode()) {
                            m.writer().writeInt(0);
                            short displayPrice = (short) (ItemFashion.ENTRYS.get(i).price > 0 ? ItemFashion.ENTRYS.get(i).price : 500);
                            m.writer().writeShort(displayPrice);
                        } else {
                            if (ItemFashion.ENTRYS.get(i).price <= 0) {
                                m.writer().writeInt(-1);
                                m.writer().writeShort(-1);
                            } else {
                                m.writer().writeInt(0);
                                m.writer().writeShort((short) ItemFashion.ENTRYS.get(i).price);
                            }
                        }
                    }
                    if (ver_ >= 115) {
                        if (myFashion != null) {
                            m.writer().writeByte(myFashion.level);
                        } else {
                            m.writer().writeByte(0);
                        }
                    }
                }
                p.addmsg(m);
                m.cleanup();
                break;
            }
            case 108: {
                Message m = new Message(-19);
                m.writer().writeByte(112);
                m.writer().writeUTF("Thẩm mỹ viện");
                m.writer().writeByte(108);
                m.writer().writeShort(ItemHair.get_size_type(108));
                for (int i = 0; i < ItemHair.ENTRYS.size(); i++) {
                    ItemHair temp = ItemHair.ENTRYS.get(i);
                    if (temp.type == 108) {
                        if(p.getConnVersionInt() >= 129) {
                            m.writer().writeShort(temp.ID);
                        } else {
                            m.writer().writeByte(temp.ID);
                        }
                        m.writer().writeUTF(temp.name);
                        m.writer().writeByte(0);
                        m.writer().writeShort(temp.idIcon);
                        m.writer().writeShort(0);
                        if (p.check_itfashionP(temp.ID, 108) != null) {
                            m.writer().writeInt(0);
                            m.writer().writeShort(0);
                        } else {
                            m.writer().writeInt(temp.beri);
                            m.writer().writeShort(temp.ruby);
                        }
                    }
                }
                p.addmsg(m);
                m.cleanup();
                break;
            }
        }
    }
}
