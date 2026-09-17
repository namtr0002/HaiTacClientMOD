package network;

import java.util.HashMap;
import java.util.LinkedList;
import model.Player;
import java.util.List;
import historys.zLog;
import core.Manager;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SessionManager {
    
    // [FIX BUG-019/RACE] Dùng CopyOnWriteArrayList thay LinkedList để thread-safe khi iterate
    public static final List<Session> CLIENT_ENTRYS = new CopyOnWriteArrayList<>();
    // [FIX BUG-RACE] Dùng ConcurrentHashMap thay HashMap để thread-safe
    public static final ConcurrentHashMap<String, Long> time_login = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Long> CLIENT_LOGIN_TIME = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Long> CHECK_BUG = new ConcurrentHashMap<>();
    public final static long TIME_LOGIN_AGAIN = 0L;
    public static final ConcurrentHashMap<Integer, Player> PLAYERS_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Player> PLAYERS_BY_NAME = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, Player> PLAYERS_BY_INDEX = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Session> CLIENTS_MAP = new ConcurrentHashMap<>();
    
    public static void client_connect(Session ss) {
        ss.init();
        SessionManager.CLIENT_ENTRYS.add(ss);
    }
    
    public static void client_disconnect(Session ss) {
        if (SessionManager.CLIENT_ENTRYS.contains(ss)) {
            ss.connected = false;
            try {
                if (ss.p != null) {
                    // Reset PvP target state and notify opponent if in PvP search/match
                    if (ss.p.pvp_target != null) {
                        Player targetPvp = ss.p.pvp_target;
                        ss.p.pvp_target = null;
                        if (targetPvp != null && !targetPvp.equals(ss.p)) {
                            targetPvp.pvp_target = null;
                            targetPvp.pvp_accept = false;
                            if (targetPvp.conn != null) {
                                try {
                                    activities.Pvp.show_table(targetPvp);
                                    targetPvp.getService().send_box_ThongBao_OK("Đối thủ đã ngắt kết nối!");
                                } catch (Exception ignore) {}
                            }
                        }
                    }

                    // 1. Flush player data to SQL DB FIRST before tearing down session/maps
                    try {
                        if (ss.p != null) {
                            Player activeP = ss.p;
                            if (activeP instanceof model.DeTu) {
                                model.DeTu dt = (model.DeTu) activeP;
                                if (dt.master != null) {
                                    activeP = dt.master;
                                }
                            }
                            activeP.flush(activeP, true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    // 2. Remove player from map & party
                    if (ss.p.map != null) {
                        try {
                            if (ss.p instanceof model.DeTu) {
                                model.DeTu dt = (model.DeTu) ss.p;
                                if (dt.master != null) {
                                    Player master = dt.master;
                                    if (master.map != null) {
                                        try {
                                            master.map.leave_map(master, 0);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    PLAYERS_MAP.remove(master.IDPlayer, master);
                                    PLAYERS_BY_NAME.remove(master.name, master);
                                    master.isClosed = true;
                                }
                            }
                            if (ss.p.map != null) {
                                ss.p.map.leave_map(ss.p, 0);
                            }
                            if (ss.p.ship_pet != null && ss.p.ship_pet.map == null) {
                                ss.p.ship_pet.map = ss.p.map;
                            }
                            if (ss.p.party != null) {
                                ss.p.party.remove_mem(ss.p);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // 3. Mark player closed & cleanup mercenary session contracts
                    try {
                        bot.mercenary.MercenaryManager.gI().onPlayerLogout(ss.p);
                    } catch (Exception ignored) {}
                    ss.p.isClosed = true;

                    try {
                        if (ss.user != null) {
                            Player activeP = ss.p;
                            if (activeP instanceof model.DeTu) {
                                model.DeTu dt = (model.DeTu) activeP;
                                if (dt.master != null) {
                                    activeP = dt.master;
                                }
                            }
                            CHECK_BUG.put(ss.user + "_" + activeP.name, activeP.get_vnd());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (ss.p != null) {
                    PLAYERS_MAP.remove(ss.p.IDPlayer);
                    PLAYERS_BY_NAME.remove(ss.p.name);
                    PLAYERS_BY_INDEX.remove((int) ss.p.index_map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try { ss.clear_network(ss); } catch (Exception e) { e.printStackTrace(); }
                try { ss.update_onl(0);    } catch (Exception e) { e.printStackTrace(); }
                
                SessionManager.CLIENT_ENTRYS.remove(ss);
                if (ss.user != null) {
                    SessionManager.CLIENTS_MAP.remove(ss.user);
                    SessionManager.time_login.remove(ss.user);
                    SessionManager.CLIENT_LOGIN_TIME.remove(ss.user);
                }
            }
        }
    }

    /* public static List<Session> getClient(Session o) {
        synchronized (CLIENT_ENTRYS) {
            List<Session> result = new ArrayList<>();
            for (int i = 0; i < CLIENT_ENTRYS.size(); i++) {
                Session ss = CLIENT_ENTRYS.get(i);
                if (!ss.equals(o) && ss.user != null && ss.user.equals(o.user)) {
                    result.add(ss);
                    //
                    if (ss.p != null && ss.p.map != null) {
                        ss.p.map.players.remove(ss.p);
                    }
                }
            }
            return result;
        }
    }*/
    public static void update() {
//        Set<String> hs = new HashSet<>();
//        List<Player> listExist = new ArrayList<>();
//        for (map.Map mapall : map.MapManager.getInstance().getMaps()) {
//            for (Zone map : mapall.zones) {
//                for (int i = 0; i < map.players.size(); i++) {
//                    Player p0 = map.players.get(i);
//                    if (!p0.isBot && !p0.isClone && p0.canSave) {
//                        if (!hs.contains(p0.name)) {
//                            hs.add(p0.name);
//                        } else {
//                            listExist.add(p0);
//                        }
//                    }
//                }
//            }
//        }
//        List<Zone> mapplus = Zone.get_map_plus();
//        for (int i = 0; i < mapplus.size(); i++) {
//            for (int i12 = 0; i12 < mapplus.get(i).players.size(); i12++) {
//                Player p0 = mapplus.get(i).players.get(i12);
//                if (!p0.isBot && !p0.isClone && p0.canSave) {
//                    if (!hs.contains(p0.name)) {
//                        hs.add(p0.name);
//                    } else {
//                        listExist.add(p0);
//                    }
//                }
//            }
//        }
//        listExist.forEach(l -> l.canSave = false);
//        System.out.println("checkkkkk " + listExist.size());
    }
}
