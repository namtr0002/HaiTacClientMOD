package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import network.Service;
import network.Message;
import template.Option;

public class Party {
    public List<Player> list;
    public List<Option> list_op;
    public int activeKeyIndex = 0;

    public Party(Player p) {
        list_op = new ArrayList<>();
        list = new ArrayList<>();
        if (p != null) {
            this.list.add(p);
            p.party = this;
            add_buff_party(p.clazz);
        }
    }

    private synchronized void add_buff_party(byte clazz) {
        Option op_select = null;
        switch (clazz) {
            case 1: {
                for (int i = 0; i < this.list_op.size(); i++) {
                    if (this.list_op.get(i).id == 17) {
                        op_select = this.list_op.get(i);
                        break;
                    }
                }
                if (op_select == null) {
                    op_select = new Option(17, 250);
                    this.list_op.add(op_select);
                }
                break;
            }
            case 2: {
                for (int i = 0; i < this.list_op.size(); i++) {
                    if (this.list_op.get(i).id == 1) {
                        op_select = this.list_op.get(i);
                        break;
                    }
                }
                if (op_select == null) {
                    op_select = new Option(1, 150);
                    this.list_op.add(op_select);
                }
                break;
            }
            case 3: {
                for (int i = 0; i < this.list_op.size(); i++) {
                    if (this.list_op.get(i).id == 4) {
                        op_select = this.list_op.get(i);
                        break;
                    }
                }
                if (op_select == null) {
                    op_select = new Option(4, 250);
                    this.list_op.add(op_select);
                }
                break;
            }
            case 4: {
                for (int i = 0; i < this.list_op.size(); i++) {
                    if (this.list_op.get(i).id == 23) {
                        op_select = this.list_op.get(i);
                        break;
                    }
                }
                if (op_select == null) {
                    op_select = new Option(23, 250);
                    this.list_op.add(op_select);
                }
                break;
            }
            case 5: {
                for (int i = 0; i < this.list_op.size(); i++) {
                    if (this.list_op.get(i).id == 25) {
                        op_select = this.list_op.get(i);
                        break;
                    }
                }
                if (op_select == null) {
                    op_select = new Option(25, 100);
                    this.list_op.add(op_select);
                }
                break;
            }
        }
    }

    public static void process(Player p, Message m2) throws IOException {
        if (p == null || p.map == null) {
            return;
        }
        if (p.isDe || p instanceof DeTu) {
            p.getService().send_box_ThongBao_OK("Đệ tử không thể thực hiện chức năng nhóm!");
            return;
        }
        byte type = m2.reader().readByte();
        short id = -1;
        if (type == 0 || type == 2 || type == 4 || type == 6) {
            id = m2.reader().readShort();
        }
        // System.out.println("type " + type);
        // System.out.println("id " + id);
        switch (type) {
            case 0: { // request
                Player p0 = p.map.get_player_by_id_inmap(id);
                if (p0 != null) {
                    if (p0.isDe || p0 instanceof DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                        p.getService().send_box_ThongBao_OK("Không thể mời lính đánh thuê hoặc đệ tử vào nhóm!");
                    } else if (p0.party != null) {
                        p.getService().send_box_ThongBao_OK("Đối phương đang ở trong nhóm khác!");
                    } else {
                        if (p0.isBot) {
                            if (p0 instanceof bot.botplayer.BotPlayerReal) {
                                bot.botplayer.BotPlayerReal botP = (bot.botplayer.BotPlayerReal) p0;
                                botP.getSocialController().tryAcceptPartyFromReal(p);
                            } else {
                                p.getService().send_box_ThongBao_OK("Không thể mời đối tượng này vào nhóm!");
                            }
                        } else {
                            Message m = new Message(-25);
                            m.writer().writeByte(0);
                            m.writer().writeShort(p.index_map);
                            m.writer().writeUTF(p.name);
                            p0.addmsg(m);
                            m.cleanup();
                            //
                            if (p.party == null) {
                                p.party = new Party(p);
                                p.party.send_info();
                            }
                        }
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Đối phương offline");
                }
                break;
            }
            case 4: { // accept
                Player p0 = p.map.get_player_by_id_inmap(id);
                if (p0 != null) {
                    if (p0.isDe || p0 instanceof DeTu) {
                        p.getService().send_box_ThongBao_OK("Không thể tham gia nhóm với đệ tử!");
                    } else if (p0.party != null) {
                        p0.party.add_new_mem(p);
                    } else {
                        p.getService().send_box_ThongBao_OK("Đối phương đã hủy nhóm");
                    }
                } else {
                    p.getService().send_box_ThongBao_OK("Đối phương offline");
                }
                break;
            }
            case 3: { // delete
                if (p.party != null && !p.party.list.isEmpty() && p.party.list.get(0).equals(p)) {
                    p.party.delete();
                }
                break;
            }
            case 2: { // leave
                if (p.party != null) {
                    Player p0 = p.map != null ? p.map.get_player_by_id_inmap(id) : null;
                    if (p0 == null) {
                        for (Player mem : p.party.list) {
                            if (mem != null && mem.index_map == id) {
                                p0 = mem;
                                break;
                            }
                        }
                    }
                    if (p0 != null) {
                        p.party.remove_mem(p0);
                        if (p0.index_map == p.index_map) {
                            p0.getService().send_box_ThongBao_OK("Bạn rời khỏi nhóm");
                        } else {
                            p0.getService().send_box_ThongBao_OK("Bạn bị đuổi khỏi nhóm");
                        }
                    }
                }
                break;
            }
        }
    }

    public synchronized void remove_mem(Player p) throws IOException {
        if (p == null) return;
        for (int i = 0; i < list.size(); i++) {
            Player p0 = list.get(i);
            if (p0 != null && p0.equals(p)) {
                Message m = new Message(-25);
                m.writer().writeByte(3);
                if (p0.conn != null) {
                    p0.addmsg(m);
                }
                m.cleanup();
                p0.party = null;
                list.remove(i);
                refresh_buff_party();
                if (!list.isEmpty()) {
                    this.send_info();
                }
                break;
            }
        }
    }

    public synchronized void refresh_buff_party() {
        this.list_op.clear();
        for (int i = 0; i < list.size(); i++) {
            Player p0 = list.get(i);
            if (p0 != null) {
                add_buff_party(p0.clazz);
            }
        }
    }

    private synchronized void delete() throws IOException {
        Message m = new Message(-25);
        m.writer().writeByte(3);
        for (int i = 0; i < list.size(); i++) {
            Player p0 = list.get(i);
            if (p0 != null) {
                p0.party = null;
                if (p0.conn != null) {
                    p0.addmsg(m);
                    p0.getService().send_box_ThongBao_OK("Nhóm đã giải tán");
                }
            }
        }
        m.cleanup();
        this.list.clear();
    }

    public synchronized void add_new_mem(Player p) throws IOException {
        if (p == null) return;
        if (this.list.size() < 5) {
            list.add(p);
            p.party = this;
            add_buff_party(p.clazz);
            this.send_info();
            p.getService().send_box_ThongBao_OK("Vào nhóm thành công!");
            p.updateArchiDaily(11);
            if (this.list.size() == 5) {
                for (int i = 0; i < this.list.size(); i++) {
                    Player mem = this.list.get(i);
                    if (mem != null) {
                        mem.updateArchiDaily(11);
                    }
                }
            }
        } else {
            if (!list.isEmpty() && list.get(0) != null) {
                list.get(0).getService().send_box_ThongBao_OK("Nhóm đầy!");
            }
        }
    }

    public synchronized void send_info() throws IOException {
        for (int i = 0; i < list.size(); i++) {
            Player p0 = list.get(i);
            if (p0 == null || p0.isClosed || (p0.conn == null && !p0.isBot)) {
                if (p0 != null) {
                    p0.party = null;
                }
                list.remove(i);
                i--;
            }
        }
        if (list.isEmpty()) {
            return;
        }
        Message m = new Message(-25);
        m.writer().writeByte(5);
        m.writer().writeByte(list.size());
        for (int i = 0; i < list.size(); i++) {
            Player p0 = list.get(i);
            m.writer().writeShort(p0.index_map);
            m.writer().writeUTF(p0.name != null ? p0.name : "");
            short mapTemplateId = (p0.map != null && p0.map.template != null) ? (short) p0.map.template.id : (short) -1;
            m.writer().writeShort(mapTemplateId);
            m.writer().writeByte(i == 0 ? 1 : 0);
            byte zoneId = (p0.map != null) ? (byte) p0.map.zone_id : (byte) 0;
            m.writer().writeByte(zoneId);
        }
        for (int i = 0; i < list.size(); i++) {
            Player p0 = list.get(i);
            if (p0 != null && p0.conn != null) {
                p0.addmsg(m);
            }
        }
        m.cleanup();

        // party buff
        for (int i = 0; i < list.size(); i++) {
            Player p0 = list.get(i);
            if (p0 == null) continue;
            List<Option> op_select = get_list_buff_now(p0);
            Message mBuff = new Message(32);
            if (op_select != null && !op_select.isEmpty()) {
                mBuff.writer().writeByte(op_select.size());
                for (int i4 = 0; i4 < op_select.size(); i4++) {
                    Option op = op_select.get(i4);
                    if (op != null) {
                        mBuff.writer().writeByte(op.id);
                        mBuff.writer().writeShort(op.getParam());
                    }
                }
            } else {
                mBuff.writer().writeByte(1);
                mBuff.writer().writeByte(1);
                mBuff.writer().writeShort(0);
            }
            if (p0.conn != null) {
                p0.addmsg(mBuff);
            }
            mBuff.cleanup();
            if (p0.map != null) {
                try {
                    p0.update_info_to_all();
                } catch (Exception ignored) {}
            }
        }
    }

    public synchronized List<Option> get_list_buff_now(Player p0) {
        List<Option> result = new ArrayList<>();
        if (p0 == null || p0.map == null) {
            return result;
        }
        int num_P = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            Player p0_2 = list.get(i2);
            if (p0_2 == null || p0_2.map == null) {
                continue;
            }
            Option op_select = null;
            if (p0.map.equals(p0_2.map)) {
                switch (p0_2.clazz) {
                    case 1: {
                        for (int i3 = 0; i3 < this.list_op.size(); i3++) {
                            Option o = this.list_op.get(i3);
                            if (o != null && o.id == 17) {
                                op_select = o;
                                break;
                            }
                        }
                        break;
                    }
                    case 2: {
                        for (int i3 = 0; i3 < this.list_op.size(); i3++) {
                            Option o = this.list_op.get(i3);
                            if (o != null && o.id == 1) {
                                op_select = o;
                                break;
                            }
                        }
                        break;
                    }
                    case 3: {
                        for (int i3 = 0; i3 < this.list_op.size(); i3++) {
                            Option o = this.list_op.get(i3);
                            if (o != null && o.id == 4) {
                                op_select = o;
                                break;
                            }
                        }
                        break;
                    }
                    case 4: {
                        for (int i3 = 0; i3 < this.list_op.size(); i3++) {
                            Option o = this.list_op.get(i3);
                            if (o != null && o.id == 23) {
                                op_select = o;
                                break;
                            }
                        }
                        break;
                    }
                    case 5: {
                        for (int i3 = 0; i3 < this.list_op.size(); i3++) {
                            Option o = this.list_op.get(i3);
                            if (o != null && o.id == 25) {
                                op_select = o;
                                break;
                            }
                        }
                        break;
                    }
                }
                num_P++;
            }
            if (op_select != null) {
                Option op_can_add = null;
                for (int i = 0; i < result.size(); i++) {
                    Option r = result.get(i);
                    if (r != null && op_select.id == r.id) {
                        op_can_add = r;
                        break;
                    }
                }
                if (op_can_add == null) {
                    result.add(op_select);
                }
            }
        }
        if (num_P < 2 && result.size() < 2) {
            result.clear();
        }
        return result;
    }
}
