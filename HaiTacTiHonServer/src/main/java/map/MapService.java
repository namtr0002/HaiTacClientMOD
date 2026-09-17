package map;

import network.Message;
import model.Player;
import java.io.IOException;
import java.util.List;
import mob.Mob;

public class MapService {

    private Zone zone;

    public MapService(Zone zone) {
        this.zone = zone;
    }

    public void sendMyMessage(Player p, Message m) {
        if (p != null && p.getService() != null) {
            p.getService().sendMessage(m);
        }
    }

    public void sendMyMessage(Player p, Message m, boolean cleanup) {
        if (p != null && p.getService() != null) {
            p.getService().sendMessage(m);
        }
        if (cleanup && m != null) {
            try {
                try { m.cleanup(); } catch (Exception e) {}
            } catch (Exception e) {}
        }
    }

    public void broadcast(Message m) {
        try {
            List<Player> players = zone.players;
            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                if (p != null && p.getService() != null) {
                    p.getService().sendMessage(m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                try { m.cleanup(); } catch (Exception e) {}
            }
        }
    }

    public void broadcast(Message m, Player excludePlayer) {
        try {
            List<Player> players = zone.players;
            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                if (p != null && p != excludePlayer && p.getService() != null) {
                    p.getService().sendMessage(m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                try { m.cleanup(); } catch (Exception e) {}
            }
        }
    }
    
    public void move(int id, int x, int y) {
        move((byte) 1, id, x, y);
    }

    public void move(byte type, int id, int x, int y) {
        try {
            Message m = new Message(1);
            m.writer().writeByte(type);
            m.writer().writeShort(id);
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            zone.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move(Player p, byte type, int id, int x, int y) {
        if (p == null) return;
        try {
            Message m = new Message(1);
            m.writer().writeByte(type);
            m.writer().writeShort(id);
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            p.addmsg(m);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move(Player p, byte type, int id, int x, int y, boolean includeSelf) {
        try {
            Message m = new Message(1);
            m.writer().writeByte(type);
            m.writer().writeShort(id);
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            zone.send_msg_all_p(m, p, includeSelf);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_buff(Player p, short idSkill, short idIcon, short idEffSkill, int time_buff, short[] extraEffs) {
        send_buff(p, idSkill, idIcon, idEffSkill, time_buff, null, null, extraEffs);
    }

    public void send_buff(Player p, short idSkill, short idIcon, short idEffSkill, int time_buff, List<Short> list_id, List<Integer> list_par, short[] extraEffs) {
        if (p == null) return;
        try {
            Message m12 = new Message(20);
            m12.writer().writeByte(1);
            m12.writer().writeShort(idSkill);
            m12.writer().writeShort(p.index_map);
            m12.writer().writeByte(0);
            m12.writer().writeShort(idIcon);
            m12.writer().writeShort(idEffSkill);
            m12.writer().writeInt(time_buff);
            m12.writer().writeByte(0);
            m12.writer().writeByte(1);
            m12.writer().writeShort(p.index_map);
            if (list_id != null && list_par != null) {
                m12.writer().writeByte(list_id.size());
                for (int i = 0; i < list_id.size(); i++) {
                    m12.writer().writeByte(list_id.get(i));
                    m12.writer().writeShort(list_par.get(i));
                }
            } else {
                m12.writer().writeByte(0);
            }
            if (extraEffs != null && extraEffs.length > 0) {
                m12.writer().writeByte(extraEffs.length);
                for (int i = 0; i < extraEffs.length; i++) {
                    m12.writer().writeShort(extraEffs[i]);
                }
            } else {
                m12.writer().writeByte(0);
            }
            zone.send_msg_all_p(m12, null, true);
            m12.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_eff_15_4(Player p, short targetIndex, byte isPlayer, short timeStr) {
        try {
            Message m = new Message(-15);
            m.writer().writeByte(4);
            m.writer().writeShort(p.index_map);
            m.writer().writeByte(0);
            m.writer().writeShort(timeStr);
            m.writer().writeShort(targetIndex);
            m.writer().writeByte(isPlayer);
            zone.send_msg_all_p(m, p, true);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_choang(Player p, Player p2, int time) {
        try {
            Message m = new Message(28);
            m.writer().writeShort(p2.index_map);
            m.writer().writeByte(0);
            m.writer().writeInt(p2.hp);
            m.writer().writeInt(p2.ability.get_hp_max(true));
            m.writer().writeShort(1);
            m.writer().writeShort(time / 100);
            zone.send_msg_all_p(m, p, true);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_choang_mob(Player p, Mob mob, int time) {
        try {
            Message m22 = new Message(28);
            m22.writer().writeShort(mob.index);
            m22.writer().writeByte(1);
            m22.writer().writeInt(mob.hp);
            m22.writer().writeInt(mob.hp_max);
            m22.writer().writeShort(1);
            m22.writer().writeShort(time / 100);
            zone.send_msg_all_p(m22, p, true);
            m22.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_attack_mob(Player p, Mob mob, int dame) {
        try {
            Message m22 = new Message(100);
            m22.writer().writeShort(p.index_map);
            m22.writer().writeByte(0);
            m22.writer().writeInt(p.hp);
            m22.writer().writeInt(p.mp);
            m22.writer().writeShort(-1);
            m22.writer().writeByte(1);
            m22.writer().writeShort(mob.index);
            m22.writer().writeByte(1);
            m22.writer().writeInt(dame);
            m22.writer().writeInt(0);
            m22.writer().writeInt(mob.hp);
            m22.writer().writeByte(0);
            zone.send_msg_all_p(m22, p, true);
            m22.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_kich_an(Player p0, Player p, int time_buff, int type, int type_eff, int par) {
        if (p == null || p0 == null) return;
        try {
            Message m = new Message(57);
            m.writer().writeByte(type);
            m.writer().writeShort(time_buff);
            m.writer().writeShort(p.index_map);
            m.writer().writeByte(0);
            m.writer().writeByte(0);
            m.writer().writeInt(time_buff * 10);
            //
            m.writer().writeShort(p0.index_map);
            m.writer().writeByte(0);
            m.writer().writeByte(type_eff);
            m.writer().writeInt(par);
            //
            zone.send_msg_all_p(m, p, true);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_vong_sinh_tu(short x, short y, short w, short h, int color) {
        try {
            Message m = new Message(73);
            m.writer().writeByte(0);
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            m.writer().writeShort(w);
            m.writer().writeShort(h);
            m.writer().writeInt(color);
            zone.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_update_point_ww(Player target) {
        if (target == null) return;
        try {
            Message m = new Message(53);
            m.writer().writeShort(target.index_map);
            m.writer().writeByte(target.point_ww);
            m.writer().writeShort(target.killWW);
            m.writer().writeShort(target.deadWW);
            zone.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addEffect(short indexMap, short idEff, int time, byte typeMove, byte loop) {
        try {
            Message m = new Message(74);
            m.writer().writeByte(1);
            m.writer().writeShort(indexMap);
            m.writer().writeShort(idEff);
            m.writer().writeInt(time);
            m.writer().writeByte(typeMove);
            m.writer().writeByte(loop);
            zone.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeEffect(short indexMap, short idEff) {
        try {
            Message m = new Message(74);
            m.writer().writeByte(2);
            m.writer().writeShort(indexMap);
            m.writer().writeShort(idEff);
            zone.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processFight(Player p, Message m) throws IOException {
        map.zones.Fight.handle(p, m);
    }
}
