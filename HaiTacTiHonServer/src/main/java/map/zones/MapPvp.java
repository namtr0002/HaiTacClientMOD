package map.zones;

import model.Player;
import model.Quest;
import event.EventData;
import template.Level;
import core.ZUtil;
import core.Manager;
import map.Zone;
import network.Message;
import network.Service;
import database.DbManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import template.GiftBox;

public class MapPvp extends zabstracts.AbsEventDungeon {
    public int idP1;
    public int idP2;
    public int time_pvp;
    public int status_pvp;
    public int num_win_p1;
    public int num_win_p2;
    public byte type_map;
    
    public Player player1;
    public Player player2;
    public int mapId = 58;
    public long lastSecondTick = 0;

    public MapPvp() {
        this.maps = new CopyOnWriteArrayList<>();
    }

    public MapPvp(Player p1, Player p2, byte typeMap) {
        this.player1 = p1;
        this.player2 = p2;
        this.idP1 = (p1 != null) ? p1.IDPlayer : 0;
        this.idP2 = (p2 != null) ? p2.IDPlayer : 0;
        this.type_map = typeMap;
        this.time_pvp = 3;
        this.status_pvp = 0;
        this.num_win_p1 = 0;
        this.num_win_p2 = 0;
        this.maps = new CopyOnWriteArrayList<>();
        this.lastSecondTick = System.currentTimeMillis();
    }

    public MapPvp(Player p1, Player p2, byte typeMap, int mapId) {
        this(p1, p2, typeMap);
        this.mapId = mapId;
    }

    public static MapPvp createDungeon(Player p1, Player p2, byte typeMap) {
        int targetMapId = 58;
        if (typeMap == 1 || typeMap == 2 || typeMap == 3) {
            short[] mapID = new short[]{120, 122, 123};
            targetMapId = mapID[core.ZUtil.random(mapID.length)];
        }
        MapPvp pvp;
        if (typeMap == 0) {
            pvp = new SieuHangFight(p1, p2, targetMapId);
        } else if (typeMap == 1) {
            pvp = new FriendlyFight(p1, p2, targetMapId);
        } else if (typeMap == 2) {
            pvp = new WantedFight(p1, p2, targetMapId);
        } else {
            long bet = 0;
            if (p1 != null && p1.betAmountFight > 0) {
                bet = p1.betAmountFight;
            } else if (p2 != null && p2.betAmountFight > 0) {
                bet = p2.betAmountFight;
            }
            pvp = new BetFight(p1, p2, targetMapId, bet);
        }
        pvp.create();
        return pvp;
    }

    @Override
    public void create() {
        Zone[] templateZones = Zone.getMapByID(this.mapId);
        Zone maptemp = (templateZones != null && templateZones.length > 0) ? templateZones[0] : null;
        Zone map_create = new Zone();
        if (maptemp != null) {
            map_create.template = maptemp.template;
        }
        map_create.zone_id = (byte) 0;
        map_create.list_mob = new int[0];
        
        map_create.map_vp = this;
        map_create.map_dungeon = this;
        
        map_create.start_map();
        Zone.add_map_plus(map_create);
        this.maps.add(map_create);
    }

    @Override
    public void join(Player p) throws IOException {
        if (maps != null && !maps.isEmpty()) {
            p.save_previous_map();
            Zone map_create = maps.get(0);
            p.map = map_create;
            if (player1 != null && p.name.equals(player1.name)) {
                p.x = 320;
            } else {
                p.x = 380;
            }
            p.y = 240;
            p.xold = p.x;
            p.yold = p.y;
            if (p.isBot) {
                p.isdie = false;
                if (p.ability == null) {
                    p.ability = new ability.Ability(p);
                }
                long maxHp = Math.max(1000, p.ability.get_hp_max(true));
                long maxMp = Math.max(500, p.ability.get_mp_max(true));
                p.hp = (int) maxHp;
                p.mp = (int) maxMp;
                p.dungeon = this;
                if (p instanceof bot.Bot) {
                    ((bot.Bot) p).join(map_create, p.x, p.y);
                } else {
                    map_create.enter_map(p);
                }
            } else {
                p.map.goto_map(p);
                p.dungeon = this;
                p.getService().update_PK(p, true);
                p.getService().pet(p, true);
                Quest.update_map_have_side_quest(p, true);
                
                // Đồng bộ trạng thái PvP hiện tại cho player thật khi vừa vào map
                try {
                    int leftScore = p.equals(this.player1) ? this.num_win_p1 : this.num_win_p2;
                    int rightScore = p.equals(this.player1) ? this.num_win_p2 : this.num_win_p1;
                    int curTime = (this.status_pvp == 3) ? this.time_pvp : ((this.time_pvp > 0) ? this.time_pvp : 2);
                    activities.Pvp.show_info(p, curTime, leftScore, rightScore, 3);
                    if (this.status_pvp == 0 || this.status_pvp == 90) {
                        activities.Pvp.pvp_notice(p, 0);
                    } else if (this.status_pvp == 1) {
                        activities.Pvp.pvp_notice(p, 1);
                    } else if (this.status_pvp == 2 || this.status_pvp == 3) {
                        activities.Pvp.pvp_notice(p, 2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void join() {
        if (player1 != null && player2 != null) {
            if (!player1.isBot && player2.isBot) {
                player2.setupBotBalancedAgainst(player1);
            } else if (player1.isBot && !player2.isBot) {
                player1.setupBotBalancedAgainst(player2);
            }
        }
        if (player1 != null) {
            player1.save_previous_map();
            if (player1.map != null) {
                player1.map.leave_map(player1, 2);
            }
            try {
                this.join(player1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (player2 != null) {
            player2.save_previous_map();
            if (player2.map != null) {
                player2.map.leave_map(player2, 2);
            }
            try {
                this.join(player2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Zone zone) throws IOException {
        if (zone == null || zone.template == null) return;
        long now = System.currentTimeMillis();

        if (zone.template.id == 1000) { // Map 1000: Phòng chờ PvP
            updateLobby(zone);
            return;
        }

        // ====================== MAP THI ĐẤU PVP (58, 120, 122, 123) ======================
        // Cập nhật đếm ngược chính xác 1 giây 1 lần
        if (now - this.lastSecondTick >= 1000L) {
            this.lastSecondTick = now;
            this.time_pvp--;
        }

        // Kiểm tra an toàn: Nếu 1 người chơi đứt kết nối / rời game trong khi đang đấu -> xử thắng cho người còn lại
        if (this.status_pvp == 3 || this.status_pvp == 90 || this.status_pvp == 91) {
            boolean p1Offline = (player1 == null || (!player1.isBot && player1.conn == null));
            boolean p2Offline = (player2 == null || (!player2.isBot && player2.conn == null));
            if (p1Offline || p2Offline) {
                this.status_pvp = 4;
                this.time_pvp = 2;
                Player remain = p1Offline ? player2 : player1;
                Player leaver = p1Offline ? player1 : player2;
                if (remain != null && zone.onlyPlayers.contains(remain)) {
                    try {
                        onMatchEnd(remain, leaver);
                        activities.Pvp.pvp_notice(remain, 3);
                        activities.Pvp.show_info(remain, 2, 3, 0, 3);
                        zone.change_flag(remain, -1);
                        if (remain.getService() != null) {
                            remain.getService().send_box_ThongBao_OK("Đối thủ đã ngắt kết nối hoặc rời đi!");
                        }
                        if (this.type_map == 0) {
                            remain.update_pvpPoint(20);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }

        // Status 0: Chuẩn bị (Wait - 2 giây)
        if (this.status_pvp == 0 && this.time_pvp <= 0) {
            this.status_pvp = 1;
            this.time_pvp = 2;
            if (player1 != null) {
                player1.isdie = false;
                player1.resetAllSkillCooldowns();
                zone.change_flag(player1, (short) 4); // Cờ Đỏ
                syncTeamCompanions(zone, player1, (short) 4);
                int hp_max1 = player1.ability.get_hp_max(true);
                int mp_max1 = player1.ability.get_mp_max(true);
                player1.hp = hp_max1;
                player1.mp = mp_max1;
                if (!player1.isBot) {
                    activities.Pvp.pvp_notice(player1, 1); // Sẵn sàng
                    player1.getService().use_potion(0, hp_max1);
                    player1.getService().use_potion(1, mp_max1);
                    try { zone.getService().send_kich_an(player1, player1, 1, 8, 0, 0); } catch (Exception ignored) {}
                }
            }
            if (player2 != null) {
                player2.isdie = false;
                player2.resetAllSkillCooldowns();
                zone.change_flag(player2, (short) 5); // Cờ Xanh
                syncTeamCompanions(zone, player2, (short) 5);
                int hp_max2 = player2.ability.get_hp_max(true);
                int mp_max2 = player2.ability.get_mp_max(true);
                player2.hp = hp_max2;
                player2.mp = mp_max2;
                if (!player2.isBot) {
                    activities.Pvp.pvp_notice(player2, 1); // Sẵn sàng
                    player2.getService().use_potion(0, hp_max2);
                    player2.getService().use_potion(1, mp_max2);
                    try { zone.getService().send_kich_an(player2, player2, 1, 8, 0, 0); } catch (Exception ignored) {}
                }
            }
        } 
        // Status 1: Sẵn sàng (Ready - 2 giây) -> Chuyển sang Status 2 (Đếm ngược bắt đầu)
        else if (this.status_pvp == 1 && this.time_pvp <= 0) {
            this.status_pvp = 2;
            this.time_pvp = 1;
            if (player1 != null && !player1.isBot) {
                activities.Pvp.pvp_notice(player1, 2); // Bắt đầu
                activities.Pvp.show_info(player1, 180, num_win_p1, num_win_p2, 3);
            }
            if (player2 != null && !player2.isBot) {
                activities.Pvp.pvp_notice(player2, 2); // Bắt đầu
                activities.Pvp.show_info(player2, 180, num_win_p2, num_win_p1, 3);
            }
        } 
        // Status 2: Đếm ngược bắt đầu trận đấu (1 giây) -> Bắt đầu hiệp đấu ngay
        else if (this.status_pvp == 2 && this.time_pvp <= 0) {
            if (!checkEntryConditions()) {
                this.status_pvp = 4;
                this.time_pvp = 0;
                return;
            }
            onFightStart();
            if (player1 != null) {
                zone.change_flag(player1, (short) 4);
                player1.resetAllSkillCooldowns();
                if (!player1.isBot) {
                    activities.Pvp.pvp_notice(player1, 2);
                    activities.Pvp.show_info(player1, 180, num_win_p1, num_win_p2, 3);
                } else if (player1 instanceof bot.Bot) {
                    ((bot.Bot) player1).setAttack(new bot.AttackAround());
                    ((bot.Bot) player1).setMove(new bot.MoveToTarget(player2, 2000));
                }
            }
            if (player2 != null) {
                zone.change_flag(player2, (short) 5);
                player2.resetAllSkillCooldowns();
                if (!player2.isBot) {
                    activities.Pvp.pvp_notice(player2, 2);
                    activities.Pvp.show_info(player2, 180, num_win_p2, num_win_p1, 3);
                } else if (player2 instanceof bot.Bot) {
                    ((bot.Bot) player2).setAttack(new bot.AttackAround());
                    ((bot.Bot) player2).setMove(new bot.MoveToTarget(player1, 2000));
                }
            }
            this.time_pvp = 180;
            this.status_pvp = 3;
        }

        // Status 3: Giao chiến hiệp đấu (180 giây = 3 phút)
        if (this.status_pvp == 3) {
            // Đã đạt đủ điểm thắng (3 hiệp)
            if (this.num_win_p1 >= 3 || this.num_win_p2 >= 3) {
                this.status_pvp = 4;
                this.time_pvp = 3;
                Player p_win = (this.num_win_p1 >= 3) ? player1 : player2;
                Player p_lose = (this.num_win_p1 >= 3) ? player2 : player1;
                try {
                    onMatchEnd(p_win, p_lose);
                    if (p_win != null && !p_win.isBot) activities.Pvp.pvp_notice(p_win, 3);
                    if (p_lose != null && !p_lose.isBot) activities.Pvp.pvp_notice(p_lose, 4);
                    for (int i = 0; i < zone.onlyPlayers.size(); i++) {
                        Message m = new Message(-63);
                        m.writer().writeByte(5);
                        m.writer().writeUTF(p_win != null ? p_win.name : "");
                        zone.onlyPlayers.get(i).addmsg(m);
                        m.cleanup();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (this.time_pvp <= 0) { // Hết giờ hiệp đấu (180 giây)
                // So sánh % HP còn lại của hiệp hiện tại
                double p1HpPercent = (player1 != null && player1.ability != null && player1.ability.get_hp_max(true) > 0)
                        ? ((double) player1.hp / player1.ability.get_hp_max(true)) : 0;
                double p2HpPercent = (player2 != null && player2.ability != null && player2.ability.get_hp_max(true) > 0)
                        ? ((double) player2.hp / player2.ability.get_hp_max(true)) : 0;

                Player roundWin = null;
                Player roundLose = null;
                if (p1HpPercent > p2HpPercent + 0.001) {
                    this.num_win_p1++;
                    roundWin = player1;
                    roundLose = player2;
                } else if (p2HpPercent > p1HpPercent + 0.001) {
                    this.num_win_p2++;
                    roundWin = player2;
                    roundLose = player1;
                } else {
                    this.num_win_p1++;
                    this.num_win_p2++;
                }

                try {
                    if (roundWin != null && !roundWin.isBot) {
                        activities.Pvp.pvp_notice(roundWin, 3); // Thắng
                    }
                    if (roundLose != null && !roundLose.isBot) {
                        activities.Pvp.pvp_notice(roundLose, 4); // Thua
                    }
                    if (player1 != null && !player1.isBot) {
                        activities.Pvp.show_info(player1, 3, num_win_p1, num_win_p2, 3);
                    }
                    if (player2 != null && !player2.isBot) {
                        activities.Pvp.show_info(player2, 3, num_win_p2, num_win_p1, 3);
                    }
                } catch (Exception ignored) {}

                this.status_pvp = 91;
                this.time_pvp = 3;
            } else {
                // Kiểm tra xem có player nào vừa ngã xuống trong round không
                boolean p1Die = (player1 != null && player1.isdie);
                boolean p2Die = (player2 != null && player2.isdie);
                if (p1Die || p2Die) {
                    Player roundWin = null;
                    Player roundLose = null;
                    if (p1Die && !p2Die) {
                        this.num_win_p2++;
                        roundWin = player2;
                        roundLose = player1;
                    } else if (p2Die && !p1Die) {
                        this.num_win_p1++;
                        roundWin = player1;
                        roundLose = player2;
                    } else {
                        this.num_win_p1++;
                        this.num_win_p2++;
                    }
                    this.status_pvp = 91;
                    this.time_pvp = 3;

                    // Gửi thông báo kết quả hiệp và cập nhật tỉ số ngay lập tức
                    try {
                        if (roundWin != null && !roundWin.isBot) {
                            activities.Pvp.pvp_notice(roundWin, 3); // Thắng
                        }
                        if (roundLose != null && !roundLose.isBot) {
                            activities.Pvp.pvp_notice(roundLose, 4); // Thua
                        }
                        if (player1 != null && !player1.isBot) {
                            activities.Pvp.show_info(player1, 3, num_win_p1, num_win_p2, 3);
                        }
                        if (player2 != null && !player2.isBot) {
                            activities.Pvp.show_info(player2, 3, num_win_p2, num_win_p1, 3);
                        }
                    } catch (Exception ignored) {}
                }
            }
        } 
        // Status 91: Đánh giá sau mỗi round (3 giây hiển thị thông báo Thắng/Thua)
        else if (this.status_pvp == 91 && this.time_pvp <= 0) {
            if (this.num_win_p1 >= 3 || this.num_win_p2 >= 3) {
                this.status_pvp = 4;
                this.time_pvp = 3;
                Player p_win = (this.num_win_p1 >= 3) ? player1 : player2;
                Player p_lose = (this.num_win_p1 >= 3) ? player2 : player1;
                try {
                    onMatchEnd(p_win, p_lose);
                    if (p_win != null && !p_win.isBot) activities.Pvp.pvp_notice(p_win, 3);
                    if (p_lose != null && !p_lose.isBot) activities.Pvp.pvp_notice(p_lose, 4);
                    for (int i = 0; i < zone.onlyPlayers.size(); i++) {
                        Message m = new Message(-63);
                        m.writer().writeByte(5);
                        m.writer().writeUTF(p_win != null ? p_win.name : "");
                        zone.onlyPlayers.get(i).addmsg(m);
                        m.cleanup();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Hồi sinh cả 2 người chơi chuẩn bị cho round tiếp theo
                Player[] pList = new Player[]{player1, player2};
                for (int i = 0; i < pList.length; i++) {
                    Player p_revive = pList[i];
                    if (p_revive == null) continue;
                    
                    p_revive.isdie = false;
                    p_revive.list_eff.clear();
                    p_revive.is_combo = null;
                    p_revive.resetAllSkillCooldowns();
                    
                    int hp_max = p_revive.ability.get_hp_max(true);
                    int mp_max = p_revive.ability.get_mp_max(true);
                    p_revive.hp = hp_max;
                    p_revive.mp = mp_max;
                    
                    if (player1 != null && p_revive.name.equals(player1.name)) {
                        p_revive.x = 320;
                    } else {
                        p_revive.x = 380;
                    }
                    p_revive.y = 240;
                    p_revive.xold = p_revive.x;
                    p_revive.yold = p_revive.y;
                    p_revive.lastValidX = p_revive.x;
                    p_revive.lastValidY = p_revive.y;
                    
                    try {
                        // 1. Gửi gói tin cập nhật full HP/MP (cmd -83)
                        Message mHp = new Message(-83);
                        mHp.writer().writeShort(p_revive.index_map);
                        mHp.writer().writeByte(0);
                        mHp.writer().writeInt(hp_max);
                        mHp.writer().writeInt(p_revive.hp);
                        mHp.writer().writeInt(hp_max); // deltaHp > 0 gọi mainObject.Reveive() trên client
                        mHp.writer().writeInt(mp_max);
                        mHp.writer().writeInt(p_revive.mp);
                        mHp.writer().writeInt(0);
                        zone.send_msg_all_p(mHp, null, true);
                        mHp.cleanup();
                        
                        // 2. Gửi gói tin hồi sinh (cmd -71)
                        Message mRevive = new Message(-71);
                        mRevive.writer().writeByte(1);
                        mRevive.writer().writeShort(p_revive.index_map);
                        mRevive.writer().writeByte(0);
                        mRevive.writer().writeInt(1);
                        zone.send_msg_all_p(mRevive, null, true);
                        mRevive.cleanup();
                        
                        // 3. Đồng bộ tọa độ về vị trí xuất phát
                        zone.getService().move((byte) 0, p_revive.index_map, p_revive.x, p_revive.y);

                        // 4. Xóa cooldown kỹ năng trên client
                        if (!p_revive.isBot) {
                            zone.getService().send_kich_an(p_revive, p_revive, 1, 8, 0, 0);
                        }
                    } catch (Exception ignored) {}

                    if (p_revive.isBot && p_revive instanceof bot.Bot) {
                        Player opp = p_revive.equals(player1) ? player2 : player1;
                        ((bot.Bot) p_revive).setAttack(new bot.AttackAround());
                        ((bot.Bot) p_revive).setMove(new bot.MoveToTarget(opp, 2000));
                    }
                }
                
                // Tái thiết lập cờ PK 4 & 5
                if (player1 != null) {
                    try { zone.change_flag(player1, (short) 4); } catch (Exception ignored) {}
                    syncTeamCompanions(zone, player1, (short) 4);
                }
                if (player2 != null) {
                    try { zone.change_flag(player2, (short) 5); } catch (Exception ignored) {}
                    syncTeamCompanions(zone, player2, (short) 5);
                }

                this.status_pvp = 90;
                this.time_pvp = 2;
                if (player1 != null && !player1.isBot) {
                    try {
                        activities.Pvp.pvp_notice(player1, 0); // "Chuẩn bị"
                        activities.Pvp.show_info(player1, 180, num_win_p1, num_win_p2, 3);
                    } catch (Exception ignored) {}
                }
                if (player2 != null && !player2.isBot) {
                    try {
                        activities.Pvp.pvp_notice(player2, 0); // "Chuẩn bị"
                        activities.Pvp.show_info(player2, 180, num_win_p2, num_win_p1, 3);
                    } catch (Exception ignored) {}
                }
            }
        } 
        // Status 90: Đếm ngược chuẩn bị hiệp tiếp theo (2 giây) -> Chuyển sang Status 1 (Sẵn sàng)
        else if (this.status_pvp == 90 && this.time_pvp <= 0) {
            this.status_pvp = 1;
            this.time_pvp = 2;
            if (player1 != null && !player1.isBot) {
                try {
                    activities.Pvp.pvp_notice(player1, 1); // "Sẵn sàng"
                    activities.Pvp.show_info(player1, 180, num_win_p1, num_win_p2, 3);
                } catch (Exception ignored) {}
            }
            if (player2 != null && !player2.isBot) {
                try {
                    activities.Pvp.pvp_notice(player2, 1); // "Sẵn sàng"
                    activities.Pvp.show_info(player2, 180, num_win_p2, num_win_p1, 3);
                } catch (Exception ignored) {}
            }
        } 
        // Status 4: Trận đấu kết thúc -> Đưa về phòng chờ
        else if (this.status_pvp == 4 && this.time_pvp <= 0) {
            Player[] pList = new Player[]{player1, player2};
            for (int i = 0; i < pList.length; i++) {
                Player p = pList[i];
                if (p != null) {
                    p.isdie = false;
                    p.hp = p.ability.get_hp_max(true);
                    p.mp = p.ability.get_mp_max(true);
                    if (p.detu != null) {
                        p.detu.isdie = false;
                        p.detu.hp = p.detu.ability.get_hp_max(true);
                        p.detu.mp = p.detu.ability.get_mp_max(true);
                    }
                }
            }
            activities.Pvp.kick_out(zone);
            this.status_pvp = 99;
        } 
        // Status 99: Dọn dẹp map
        else if (this.status_pvp == 99) {
            zone.setRunning(false);
            zone.stop_map();
            zone.map_vp = null;
            zone.map_dungeon = null;
        }
    }

    protected boolean checkEntryConditions() {
        return player1 != null && player2 != null;
    }

    protected void onFightStart() throws IOException {
    }

    protected void onMatchEnd(Player p_win, Player p_lose) throws IOException {
    }

    protected void onMatchTie(Player p1, Player p2) throws IOException {
    }

    private void syncTeamCompanions(map.Zone zone, Player p, short flag) {
        if (zone == null || p == null) return;
        // 1. Đồng bộ Đệ Tử
        try {
            if (p.isDeOnl && p.detu != null && p.detu.detuStatus != model.DeTu.STATUS_HOME && p.detu.detuStatus != model.DeTu.STATUS_FUSION) {
                if (p.detu.map == null || !p.detu.map.equals(zone)) {
                    p.detu.map = zone;
                    p.detu.x = (short) (p.x + 25);
                    p.detu.y = (short) (p.y + 10);
                    p.detu.syncMasterIdentity();
                    if (!zone.players.contains(p.detu)) {
                        zone.enter_map(p.detu);
                    }
                    network.Message m_spawn = new network.Message(1);
                    m_spawn.writer().writeByte(0);
                    m_spawn.writer().writeShort(p.detu.index_map);
                    m_spawn.writer().writeShort(p.detu.x);
                    m_spawn.writer().writeShort(p.detu.y);
                    zone.send_msg_all_p(m_spawn, null, true);
                    m_spawn.cleanup();
                }
                p.detu.isdie = false;
                p.detu.list_eff.clear();
                p.detu.resetAllSkillCooldowns();
                int hpMax = p.detu.ability.get_hp_max(true);
                int mpMax = p.detu.ability.get_mp_max(true);
                p.detu.hp = hpMax;
                p.detu.mp = mpMax;
                p.detu.x = (short) (p.x + 25);
                p.detu.y = (short) (p.y + 10);
                p.detu.type_pk = (byte) flag;
                zone.change_flag(p.detu, flag);
                zone.getService().move((byte) 0, p.detu.index_map, p.detu.x, p.detu.y);
            }
        } catch (Throwable ignored) {}

        // 2. Chặn Lính Đánh Thuê tham gia map đấu trường PvP (Đơn, Truy Nã, Giao Hữu, Thách Đấu)
    }

    public static void updateLobby(Zone zone) throws IOException {
        if (zone == null || zone.template == null) return;
        long now = System.currentTimeMillis();

        if (zone.template.id == 1000) { // Map 1000: Phòng chờ PvP
            // 1. Duy trì số lượng Bot đứng chờ tại Map 1000 để tạo không khí đông đúc
            boolean hasRealPlayer = false;
            int botCount = 0;
            Player sampleRealPlayer = null;
            for (int i = 0; i < zone.onlyPlayers.size(); i++) {
                Player p0 = zone.onlyPlayers.get(i);
                if (p0 != null && !p0.isBot && p0.conn != null) {
                    hasRealPlayer = true;
                    if (sampleRealPlayer == null) sampleRealPlayer = p0;
                } else if (p0 != null && p0.isBot) {
                    botCount++;
                }
            }

            // Nếu có người chơi thật trong phòng chờ mà số bot < 3 -> Spawn bot đứng chờ
            if (hasRealPlayer && botCount < 3 && sampleRealPlayer != null) {
                try {
                    int fakeBotId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                    bot.BotPVP waitBot = bot.BotPVP.createAnalyzedBot(fakeBotId, sampleRealPlayer);
                    if (waitBot != null) {
                        waitBot.type_pk = -1;
                        waitBot.typePirate = -1;
                        waitBot.join(zone, (short) ZUtil.random(300, 650), (short) ZUtil.random(240, 260));
                    }
                } catch (Exception ignored) {}
            }

            // Bot trong phòng chờ chat ngẫu nhiên
            for (int i = 0; i < zone.onlyPlayers.size(); i++) {
                Player p0 = zone.onlyPlayers.get(i);
                if (p0 != null && p0.isBot && p0 instanceof bot.BotPVP) {
                    bot.BotPVP botP = (bot.BotPVP) p0;
                    if (now - botP.timeChatBot > 15_000L + ZUtil.random(10000)) {
                        botP.timeChatBot = now;
                        String[] chats = {"Ai solo 1v1 không?", "Ghép trận đấu trường đi ae!", "Chờ đối thủ lâu quá...", "Trao điểm PVP không gà ơi?", "Ai dám thách đấu không?"};
                        try {
                            zone.send_chat_popup(0, botP.index_map, chats[ZUtil.random(chats.length)]);
                        } catch (Exception ignored) {}
                    }
                }
            }

            // 2. Lấy danh sách người chơi thật đang trong hàng chờ tìm trận (pvp_target == p0)
            List<Player> realPlayersSearching = new ArrayList<>();
            for (int i = 0; i < zone.onlyPlayers.size(); i++) {
                Player p0 = zone.onlyPlayers.get(i);
                if (p0 != null && !p0.isBot && p0.conn != null) {
                    if (p0.pvp_target != null && p0.pvp_target.equals(p0)) {
                        realPlayersSearching.add(p0);
                    }
                }
            }

            // Giai đoạn 1: Ưu tiên ghép các người chơi thật với nhau nếu có từ 2 người thật tìm trận
            while (realPlayersSearching.size() >= 2) {
                Player p1 = realPlayersSearching.remove(0);
                Player p2 = null;
                int maxLevelDiff = Math.max(2, (int) (p1.level * 0.10)); // Tỉ lệ chênh lệch tối đa 10%
                
                // Tìm người chơi thật phù hợp level trước
                for (int i = 0; i < realPlayersSearching.size(); i++) {
                    Player candidate = realPlayersSearching.get(i);
                    if (Math.abs(p1.level - candidate.level) <= maxLevelDiff) {
                        p2 = realPlayersSearching.remove(i);
                        break;
                    }
                }
                // Nếu không có ai trong khoảng ±10% level nhưng còn người chờ khác -> ghép luôn
                if (p2 == null && !realPlayersSearching.isEmpty()) {
                    p2 = realPlayersSearching.remove(0);
                }

                if (p2 != null) {
                    p1.pvp_target = p2;
                    p2.pvp_target = p1;
                    p1.pvp_accept = false;
                    p2.pvp_accept = false;
                    activities.Pvp.find_out_other(p1, p2);
                    activities.Pvp.find_out_other(p2, p1);
                }
            }

            // Giai đoạn 2: Với người chơi thật còn lại đơn lẻ trong hàng chờ (Ghép với BOT sau 3-5s)
            for (int i = 0; i < realPlayersSearching.size(); i++) {
                Player p_select = realPlayersSearching.get(i);
                if (p_select.pvp_target == null || !p_select.pvp_target.equals(p_select)) {
                    continue;
                }

                long searchDuration = p_select.pvpSearchDuration > 0 ? p_select.pvpSearchDuration : 3000L;
                long elapsed = now - p_select.pvpSearchStartTime;

                // Nếu chưa hết thời gian chờ -> Tiếp tục chờ
                if (elapsed < searchDuration) {
                    continue;
                }

                // Đã hết thời gian chờ mà không có người chơi thật nào -> Ghép với BOT
                Player botP = null;
                int maxLevelDiff = Math.max(2, (int) (p_select.level * 0.10));

                // 1) Tìm Bot trong phòng chờ sát level
                for (int j = 0; j < zone.onlyPlayers.size(); j++) {
                    Player inMapBot = zone.onlyPlayers.get(j);
                    if (inMapBot != null && inMapBot.isBot && (inMapBot.pvp_target == null || inMapBot.pvp_target.equals(inMapBot))) {
                        if (Math.abs(p_select.level - inMapBot.level) <= maxLevelDiff) {
                            botP = inMapBot;
                            break;
                        }
                    }
                }

                // 2) Tìm bot offline SQL sát level
                if (botP == null) {
                    botP = bot.BotPVP.findOfflineSqlBot(p_select);
                }

                // 3) Fallback tạo Bot PVP phân tích chỉ số
                if (botP == null) {
                    botP = bot.BotPVP.createAnalyzedBot(database.IDManager.takeID(database.IDManager.FAKE_BOT), p_select);
                }

                if (botP != null) {
                    if (botP.clazz < 1 || botP.clazz > 5) {
                        botP.clazz = (byte) core.ZUtil.random(1, 5);
                    }
                    botP.map = zone;
                    botP.isBot = true;
                    botP.isdie = false;
                    botP.type_pk = -1;
                    botP.typePirate = -1;
                    p_select.pvp_target = botP;
                    botP.pvp_target = p_select;
                    p_select.pvp_accept = false;
                    botP.pvp_accept = true; // Bot tự động Sẵn sàng

                    activities.Pvp.find_out_other(p_select, botP);

                    // Gửi thông báo Bot đã Sẵn sàng ("OK" ở phía đối thủ và hiển thị "Đối thủ đã sẵn sàng")
                    Message m = new Message(-63);
                    m.writer().writeByte(4);
                    m.writer().writeByte(1);
                    p_select.addmsg(m);
                    m.cleanup();
                }
            }
        }
    }
}
