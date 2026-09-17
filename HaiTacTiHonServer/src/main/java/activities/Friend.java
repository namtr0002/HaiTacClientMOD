package activities;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Player;
import network.Service;
import network.Message;
import map.Zone;
import template.FriendTemp;

public class Friend {
    public static void process(Player p, Message m2) throws IOException {
        if (p.isDe || p instanceof model.DeTu) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể sử dụng tính năng Bạn bè!");
            return;
        }
        byte type = m2.reader().readByte();
        short id = (short) m2.reader().readInt();
        switch (type) {
            case 0: {
                Player p0 = p.map.get_player_by_id_inmap(id);
                if (p0 != null) {
                    if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                        p.getService().send_box_ThongBao_OK("Không thể kết bạn với lính đánh thuê hoặc đệ tử!");
                        return;
                    }
                    if (p.friend_list.size() < 100) {
                        for (int i = 0; i < p.friend_list.size(); i++) {
                            if (p.friend_list.get(i).playerId == p0.IDPlayer) {
                                p.getService().send_box_ThongBao_OK("Đối phương đã có trong danh sách bạn bè");
                                return;
                            }
                        }
                    } else {
                        p.getService().send_box_ThongBao_OK("Đầy danh sách");
                        return;
                    }
                }
                if (p0 != null && p0.index_map != p.index_map) {
                    Message m = new Message(-29);
                    m.writer().writeByte(0);
                    m.writer().writeShort(p.index_map);
                    m.writer().writeUTF(p.name);
                    p0.addmsg(m);
                    m.cleanup();
                } else {
                    p.getService().send_box_ThongBao_OK("Đối phương offline");
                }
                break;
            }
            case 3: {
                Player p0 = p.map.get_player_by_id_inmap(id);
                if (p0 == null) {
                    p.getService().send_box_ThongBao_OK("Đối phương không có mặt trong map");
                    return;
                }
                if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                    p.getService().send_box_ThongBao_OK("Không thể kết bạn với lính đánh thuê hoặc đệ tử!");
                    return;
                }
                if (p.friend_list.size() < 100) {
                    for (int i = 0; i < p.friend_list.size(); i++) {
                        if (p.friend_list.get(i).playerId == p0.IDPlayer) {
                            p.getService().send_box_ThongBao_OK("Đối phương đã có trong danh sách bạn bè");
                            return;
                        }
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Danh sách bạn bè đã đầy, không thể thêm nữa");
                    return;
                }
                if (p0.index_map != p.index_map) {
                    p.friend_list.add(new FriendTemp(p0));
                    p.friend_list.get(p.friend_list.size() - 1).id =
                            p.friend_list.indexOf(p.friend_list.get(p.friend_list.size() - 1));
                    p0.friend_list.add(new FriendTemp(p));
                    p0.friend_list.get(p0.friend_list.size() - 1).id =
                            p0.friend_list.indexOf(p0.friend_list.get(p0.friend_list.size() - 1));
                    //
                    Message m = new Message(-29);
                    m.writer().writeByte(3);
                    ReadInfoMemList(m.writer(), p.friend_list.get(p.friend_list.size() - 1));
                    p.addmsg(m);
                    m.cleanup();
                    m = new Message(-29);
                    m.writer().writeByte(3);
                    ReadInfoMemList(m.writer(), p0.friend_list.get(p0.friend_list.size() - 1));
                    p0.addmsg(m);
                    m.cleanup();
                    p.getService().send_box_ThongBao_OK("Bạn trở thành bạn bè với " + p0.name);
                    p0.getService().send_box_ThongBao_OK("Bạn trở thành bạn bè với " + p.name);
                }
                break;
            }
            case 2: {
                if (id == 0) {
                    update_list(p);
                }
                break;
            }
            case 1: {
                if (id < p.friend_list.size()) {
                    p.friend_list.remove(id);
                    update_list(p);
                }
                break;
            }
        }
    }

    private static void update_list(Player p) throws IOException {
        List<FriendTemp> list_remove = new ArrayList<>();
        for (int i = 0; i < p.friend_list.size(); i++) {
            Player p0 = Zone.get_player_by_id_allmap(p.friend_list.get(i).playerId);
            boolean can_remove = true;
            if (p0 != null) {
                for (int j = 0; j < p0.friend_list.size(); j++) {
                    if (p0.friend_list.get(j).playerId == p.IDPlayer) {
                        can_remove = false;
                        break;
                    }
                }
                if (can_remove) {
                    list_remove.add(p.friend_list.get(i));
                }
            }
        }
        p.friend_list.removeAll(list_remove);
        Message m = new Message(-29);
        m.writer().writeByte(2);
        m.writer().writeByte(p.friend_list.size());
        for (int i = 0; i < p.friend_list.size(); i++) {
            ReadInfoMemList(m.writer(), p.friend_list.get(i));
        }
        p.addmsg(m);
        m.cleanup();
    }

    public static void ReadInfoMemList(DataOutputStream dos, FriendTemp temp) throws IOException {
        Player p0 = Zone.get_player_by_id_allmap(temp.playerId);
        if (p0 != null) {
            temp.name = p0.name;
            temp.level = p0.level;
            temp.head = p0.head;
            temp.hair = p0.hair;
        } else if (temp.playerId > 0) {
            String dbName = database.DbManager.getPlayerNameById(temp.playerId);
            if (dbName != null && !dbName.isEmpty()) {
                temp.name = dbName;
            }
        }
        dos.writeInt(temp.id);
        dos.writeUTF(temp.name);
        dos.writeShort(temp.level);
        dos.writeShort(temp.head);
        dos.writeShort(temp.hair);
        dos.writeShort(temp.hat);
        dos.writeByte((p0 != null) ? 1 : 0);
        String info =
                (p0 == null) ? "Offline" : (p0.map.template.name + " khu " + (p0.map.zone_id + 1));
        dos.writeUTF(info);
        dos.writeShort(temp.rank);
    }
}
