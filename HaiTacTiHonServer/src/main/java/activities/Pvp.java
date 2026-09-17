package activities;

import model.Player;
import model.Quest;
import network.Service;
import network.Message;
import map.Zone;
import map.Npc;
import map.Vgo;
import template.EffTemplate;
import map.zones.MapPvp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pvp {
    public static void show_table(Player p) throws IOException {
        if (p == null || p.isBot || p.conn == null) return;
        if (p.isDe || p instanceof model.DeTu || p instanceof bot.mercenary.MercenaryBot) {
            p.getService().send_box_ThongBao_OK("Đệ tử và lính đánh thuê không thể tham gia PvP!");
            return;
        }
        if (p.getService() != null) {
            p.getService().UpdatePvpPoint();
        }
        //
        Message m = new Message(-63);
        m.writer().writeByte(0);
        m.writer().writeShort(p.map != null && p.map.players != null ? p.map.players.size() : 0);
        p.addmsg(m);
        m.cleanup();
        //
        p.pvp_target = null;
        p.pvp_accept = false;
    }

    public static void pvp_notice(Player p, int type) throws IOException {
        Message m = new Message(36);
        m.writer().writeByte(type);
        p.addmsg(m);
        m.cleanup();
    }

    public static void show_info(Player p, int countDown, int left, int right, int maxWin)
            throws IOException {
        Message m = new Message(-73);
        m.writer().writeByte(5);
        m.writer().writeShort(countDown);
        m.writer().writeByte(left);
        m.writer().writeByte(right);
        m.writer().writeByte(maxWin);
        p.addmsg(m);
        m.cleanup();
    }

    public static void find_out_other(Player p, Player p0) throws IOException {
        if (p == null || p.isBot || p.conn == null) return;
        byte oppClazz = 1;
        if (p0 != null) {
            if (p0.clazz >= 1 && p0.clazz <= 5) {
                oppClazz = (byte) p0.clazz;
            } else {
                oppClazz = (byte) core.ZUtil.random(1, 5);
            }
        } else {
            oppClazz = (byte) core.ZUtil.random(1, 5);
        }
        Message m = new Message(-63);
        m.writer().writeByte(3);
        m.writer().writeUTF("Ẩn danh");
        m.writer().writeByte(oppClazz); // clazz (1: Kiếm sĩ, 2: Xạ thủ, 3: Đầu bếp, 4: Hoa tiêu, 5: Bác sĩ)
        p.addmsg(m);
        m.cleanup();
        //
        p.pvp_accept = false;
        EffTemplate ef = p.get_eff(19);
        if (ef != null) {
            ef.time = System.currentTimeMillis() + 30_000;
        } else {
            p.add_new_eff(19, 1, 30_000);
        }
    }

    public synchronized static void process(Player p, Message m2) throws IOException {
        if (p == null) return;
        byte act = m2.reader().readByte();
        switch (act) {
            case 2: {
                stop_find(p);
                break;
            }
            case 1: {
                start_find(p);
                break;
            }
            case 4: {
                Player target = p.pvp_target;
                if (target != null && !target.equals(p) && (target.isBot || target.conn != null) && (target.isBot || (p.map != null && p.map.equals(target.map))) && p.map != null && p.map.template.id == 1000) {
                    Message m = new Message(-63);
                    m.writer().writeByte(4);
                    m.writer().writeByte(0);
                    p.addmsg(m);
                    m.cleanup();
                    //
                    if (target.conn != null) {
                        m = new Message(-63);
                        m.writer().writeByte(4);
                        m.writer().writeByte(1);
                        target.addmsg(m);
                        m.cleanup();
                    }
                    //
                    p.pvp_accept = true;
                    if (p.pvp_accept && target.pvp_accept) {
                        if (!p.isBot && p.get_pvp_ticket() < 1) {
                            if (!target.isBot && target.get_pvp_ticket() < 1) {
                                show_table(target);
                                stop_find(target);
                                if (target.getService() != null) {
                                    target.getService().send_box_ThongBao_OK("Bạn không đủ vé pvp!");
                                }
                            } else {
                                show_table(target);
                                if (!target.isBot) {
                                    start_find(target);
                                }
                                if (target.getService() != null) {
                                    target.getService().send_box_ThongBao_OK("Đang tìm đối thủ khác!");
                                }
                            }
                            show_table(p);
                            stop_find(p);
                            if (p.getService() != null) {
                                p.getService().send_box_ThongBao_OK("Bạn không đủ vé pvp!");
                            }
                            return;
                        } else if (!target.isBot && target.get_pvp_ticket() < 1) {
                            show_table(target);
                            stop_find(target);
                            if (target.getService() != null) {
                                target.getService().send_box_ThongBao_OK("Bạn không đủ vé pvp!");
                            }
                            //
                            show_table(p);
                            start_find(p);
                            if (p.getService() != null) {
                                p.getService().send_box_ThongBao_OK("Đối thủ không đủ vé pvp!");
                            }
                            return;
                        }
                        if (!p.isBot) {
                            p.update_pvp_ticket(-1);
                        }
                        if (!target.isBot) {
                            target.update_pvp_ticket(-1);
                        }
                        p.type_pk = -1;
                        target.type_pk = -1;
                        //
                        MapPvp pvp = MapPvp.createDungeon(p, target, (byte) 0);
                        pvp.join();
                        //
                        try {
                            p.updateArchiDaily(10);
                        } catch (Exception ignored) {}
                        if (target != null && !target.isBot) {
                            try {
                                target.updateArchiDaily(10);
                            } catch (Exception ignored) {}
                        }
                    }
                } else {
                    show_table(p);
                    stop_find(p);
                    if (p.getService() != null) {
                        p.getService().send_box_ThongBao_OK("Đối thủ đã ngắt kết nối hoặc rời đi!");
                    }
                }
                break;
            }
            case 5: {
                Player target = p.pvp_target;
                if (target != null && !target.equals(p) && target.conn != null) {
                    show_table(target);
                    start_find(target);
                    if (target.getService() != null) {
                        target.getService().send_box_ThongBao_OK("Đối thủ rời đi, bạn quay lại hàng chờ");
                    }
                }
                p.pvp_target = null;
                p.return_to_previous_map();
                break;
            }
        }
    }

    private static void stop_find(Player p) throws IOException {
        if (p == null) return;
        if (!p.isBot && p.conn != null) {
            Message m = new Message(-63);
            m.writer().writeByte(2);
            p.addmsg(m);
            m.cleanup();
        }
        //
        p.pvp_target = null;
    }

    public static void start_find(Player p) throws IOException {
        if (p == null || p.isBot || p.conn == null) return;
        Message m = new Message(-63);
        m.writer().writeByte(1);
        p.addmsg(m);
        m.cleanup();
        //
        p.pvp_target = p;
        p.pvp_accept = false;
        p.pvpSearchStartTime = System.currentTimeMillis();
        p.pvpSearchDuration = 3000L + core.ZUtil.random(2001); // 3 - 5 giây
    }

    public static void kick_out(Zone zone) {
        try {
            byte type_map = -1;
            if (zone.map_vp instanceof MapPvp) {
                type_map = ((MapPvp) zone.map_vp).type_map;
            }
            List<Player> players = new ArrayList<>(zone.players);
            for (Player p : players) {
                if (p != null) {
                    if (p.isSpectator) {
                        p.exitSpectatorMode();
                        continue;
                    }
                    if (p.isBot && p instanceof bot.Bot) {
                        ((bot.Bot) p).leave();
                    } else {
                        p.dungeon = null;
                        p.type_pk = -1;
                        p.pvp_target = null;
                        p.targetFight = null;
                        p.betAmountFight = 0;
                        p.isdie = false;
                        p.ischangemap = false;
                        p.time_change_map = System.currentTimeMillis() + 2000L;
                        if (p.ability != null) {
                            p.hp = p.ability.get_hp_max(true);
                            p.mp = p.ability.get_mp_max(true);
                        }
                        if (p.getService() != null) {
                            p.getService().update_PK(p, false);
                        }
                        if (type_map == 0) { // Standard PvP -> Quay lại phòng chờ 1000
                            Vgo vgo = new Vgo();
                            vgo.map_go = Zone.getMapByID(1000);
                            if (vgo.map_go != null && vgo.map_go.length > 0 && vgo.map_go[0] != null) {
                                vgo.xnew = (short) core.ZUtil.random(150, 300);
                                vgo.ynew = (short) core.ZUtil.random(200, 300);
                                p.goto_map(vgo);
                            } else {
                                p.return_to_previous_map();
                            }
                        } else if (type_map == 2) { // Wanted PvP -> Quay lại phòng chờ 119
                            Vgo vgo = new Vgo();
                            vgo.map_go = Zone.getMapByID(119);
                            if (vgo.map_go != null && vgo.map_go.length > 0 && vgo.map_go[0] != null) {
                                vgo.xnew = (short) core.ZUtil.random(120, 380);
                                vgo.ynew = (short) core.ZUtil.random(230, 330);
                                p.goto_map(vgo);
                            } else {
                                p.return_to_previous_map();
                            }
                        } else {
                            // type_map == 1 (Giao hữu), 3 (Thách đấu cược), hoặc các chế độ solo khác
                            p.return_to_previous_map();
                        }
                    }
                }
            }
            synchronized (Zone.get_map_plus()) {
                Zone.get_map_plus().remove(zone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
