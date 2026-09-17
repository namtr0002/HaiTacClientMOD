package map.zones;

import clan.Clan;
import model.Player;
import map.Zone;
import map.Vgo;
import network.Message;
import activities.Pvp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PvpBangMapFight extends zabstracts.AbsClanDungeon {
    public Clan clan1;
    public Clan clan2;
    public Zone mapWaitClan1;
    public Zone mapWaitClan2;
    public PvpBang pvpBang1;
    public PvpBang pvpBang2;
    public boolean isClan2Bot = false;

    public int time_pvp;
    public int status_pvp;
    public int num_win_p1;
    public int num_win_p2;

    public long nextStateTime = 0;
    public long fightStartTime = 0;

    public PvpBangMapFight() {
        this.maps = new ArrayList<>();
        this.status_pvp = -1; // Chờ người chơi và bot chuyển sang mapFight mới kích hoạt
        this.nextStateTime = 0;
    }

    public synchronized void startFightCountdown() {
        if (this.status_pvp == -1) {
            this.status_pvp = 0;
            this.nextStateTime = System.currentTimeMillis() + 3_000L;
        }
    }

    @Override
    public void create() {}

    @Override
    public void update(Zone zone) throws IOException {
        if (zone == null || this.status_pvp < 0 || this.status_pvp >= 99) return;
        long now = System.currentTimeMillis();

        // Trạng thái 0: Chuẩn bị (READY)
        if (this.status_pvp == 0 && now >= this.nextStateTime) {
            for (int i = 0; i < zone.players.size(); i++) {
                Player pi = zone.players.get(i);
                if (pi != null) Pvp.pvp_notice(pi, 0);
            }
            this.status_pvp = 1;
            this.nextStateTime = now + 4_000L;
        } 
        // Trạng thái 1: Bắt đầu sẵn sàng, hồi 100% HP/MP
        else if (this.status_pvp == 1 && now >= this.nextStateTime) {
            for (int i = 0; i < zone.players.size(); i++) {
                Player pi = zone.players.get(i);
                if (pi != null) {
                    Pvp.pvp_notice(pi, 1);
                    if (pi.ability != null) {
                        pi.hp = (int) Math.max(1000, pi.ability.get_hp_max(true));
                        pi.mp = (int) Math.max(500, pi.ability.get_mp_max(true));
                    }
                    if (pi.getService() != null && pi.ability != null) {
                        pi.getService().use_potion(0, pi.ability.get_hp_max(true));
                        pi.getService().use_potion(1, pi.ability.get_mp_max(true));
                    }
                    pi.isdie = false;
                }
            }
            this.status_pvp = 2;
            this.nextStateTime = now + 3_000L;
        } 
        // Trạng thái 2: Bật cờ PK và bắt đầu chiến đấu (GO!)
        else if (this.status_pvp == 2 && now >= this.nextStateTime) {
            this.status_pvp = 3;
            this.fightStartTime = now;
            this.nextStateTime = now + 180_000L;
            this.time_pvp = 180;

            for (int i = 0; i < zone.players.size(); i++) {
                Player pi = zone.players.get(i);
                if (pi != null) {
                    Pvp.pvp_notice(pi, 2);
                    if (pi.clan != null && pi.clan.equals(this.clan1)) {
                        zone.change_flag(pi, 4); // Team 1: Cờ Đỏ
                    } else {
                        zone.change_flag(pi, 5); // Team 2: Cờ Xanh
                    }
                    Pvp.show_info(pi, 180, 0, 0, 1);
                }
            }
        }

        // Trạng thái 3: Giao tranh
        if (this.status_pvp == 3) {
            if (now - this.fightStartTime >= 5_000L) {
                int numAliveClan1 = 0;
                int numAliveClan2 = 0;
                for (int j = 0; j < zone.players.size(); j++) {
                    Player pj = zone.players.get(j);
                    if (pj != null && !pj.isdie && !pj.isSpectator) {
                        if (pj.clan != null && pj.clan.equals(this.clan1)) {
                            numAliveClan1++;
                        } else if (pj.clan != null && pj.clan.equals(this.clan2)) {
                            numAliveClan2++;
                        }
                    }
                }

                // Nếu 1 trong 2 đội (hoặc cả 2) hết người sống -> Kết thúc trận đấu
                if (numAliveClan1 == 0 || numAliveClan2 == 0) {
                    this.status_pvp = 4;
                    this.nextStateTime = now + 5_000L;

                    // Tháo cờ PK
                    for (int i = 0; i < zone.players.size(); i++) {
                        Player pi = zone.players.get(i);
                        if (pi != null) {
                            try { zone.change_flag(pi, -1); } catch (Exception ignored) {}
                        }
                    }

                    String nameClan1 = (this.clan1 != null) ? this.clan1.name : "Băng 1";
                    String nameClan2 = (this.clan2 != null) ? this.clan2.name : "Băng 2";

                    if (numAliveClan1 == 0 && numAliveClan2 == 0) {
                        // Cả 2 cùng gục ngã -> Hòa
                        for (int j = 0; j < zone.players.size(); j++) {
                            Player pj = zone.players.get(j);
                            if (pj != null) {
                                Pvp.pvp_notice(pj, 5);
                                if (!pj.isBot) {
                                    pj.pvppoint += 5;
                                    if (pj.getService() != null) pj.getService().UpdatePvpPoint();
                                }
                            }
                        }
                        PvpBang.notice(zone, "Cả hai bên đều gục ngã - Kết quả hòa! Bạn sẽ được đưa về phòng chờ sau 5 giây.");
                        distributeRewards(clan1, clan2, false, true);
                        distributeRewards(clan2, clan1, false, true);
                        if (this.pvpBang1 != null) {
                            this.pvpBang1.noticeFinished = "Kết quả PVP với băng " + nameClan2 + " - Hòa.";
                        }
                        if (this.pvpBang2 != null) {
                            this.pvpBang2.noticeFinished = "Kết quả PVP với băng " + nameClan1 + " - Hòa.";
                        }
                    } else if (numAliveClan1 == 0) {
                        // Clan 2 Thắng, Clan 1 Thua
                        for (int j = 0; j < zone.players.size(); j++) {
                            Player pj = zone.players.get(j);
                            if (pj != null) {
                                if (pj.clan != null && pj.clan.equals(this.clan1)) {
                                    Pvp.pvp_notice(pj, 4); // Lose
                                    if (!pj.isBot) {
                                        pj.pvp_lose++;
                                        pj.pvppoint += 3;
                                        if (pj.getService() != null) pj.getService().UpdatePvpPoint();
                                    }
                                } else {
                                    Pvp.pvp_notice(pj, 3); // Win
                                    if (!pj.isBot) {
                                        pj.pvp_win++;
                                        pj.pvppoint += 10;
                                        if (pj.getService() != null) pj.getService().UpdatePvpPoint();
                                    }
                                }
                            }
                        }
                        PvpBang.notice(zone, "Kết thúc - Băng chiến thắng: " + nameClan2 + ". Bạn sẽ được đưa về phòng chờ sau 5 giây!");
                        distributeRewards(clan1, clan2, false, false);
                        distributeRewards(clan2, clan1, true, false);
                        if (this.pvpBang1 != null) {
                            this.pvpBang1.lose++;
                            this.pvpBang1.isWin = false;
                            this.pvpBang1.noticeFinished = "Kết quả PVP với băng " + nameClan2 + " - Thua.";
                        }
                        if (this.pvpBang2 != null) {
                            this.pvpBang2.win++;
                            this.pvpBang2.isWin = true;
                            this.pvpBang2.noticeFinished = "Kết quả PVP với băng " + nameClan1 + " - Thắng.";
                        }
                    } else {
                        // Clan 1 Thắng, Clan 2 Thua
                        for (int j = 0; j < zone.players.size(); j++) {
                            Player pj = zone.players.get(j);
                            if (pj != null) {
                                if (pj.clan != null && pj.clan.equals(this.clan1)) {
                                    Pvp.pvp_notice(pj, 3); // Win
                                    if (!pj.isBot) {
                                        pj.pvp_win++;
                                        pj.pvppoint += 10;
                                        if (pj.getService() != null) pj.getService().UpdatePvpPoint();
                                    }
                                } else {
                                    Pvp.pvp_notice(pj, 4); // Lose
                                    if (!pj.isBot) {
                                        pj.pvp_lose++;
                                        pj.pvppoint += 3;
                                        if (pj.getService() != null) pj.getService().UpdatePvpPoint();
                                    }
                                }
                            }
                        }
                        PvpBang.notice(zone, "Kết thúc - Băng chiến thắng: " + nameClan1 + ". Bạn sẽ được đưa về phòng chờ sau 5 giây!");
                        distributeRewards(clan1, clan2, true, false);
                        distributeRewards(clan2, clan1, false, false);
                        if (this.pvpBang1 != null) {
                            this.pvpBang1.win++;
                            this.pvpBang1.isWin = true;
                            this.pvpBang1.noticeFinished = "Kết quả PVP với băng " + nameClan2 + " - Thắng.";
                        }
                        if (this.pvpBang2 != null) {
                            this.pvpBang2.lose++;
                            this.pvpBang2.isWin = false;
                            this.pvpBang2.noticeFinished = "Kết quả PVP với băng " + nameClan1 + " - Thua.";
                        }
                    }
                }
            }

            // Hết giờ thi đấu 3 phút -> Tính điểm HP bên nào cao hơn bên đó thắng
            if (this.status_pvp == 3 && now >= this.nextStateTime) {
                this.status_pvp = 4;
                this.nextStateTime = now + 5_000L;

                for (int i = 0; i < zone.players.size(); i++) {
                    Player pi = zone.players.get(i);
                    if (pi != null) {
                        try { zone.change_flag(pi, -1); } catch (Exception ignored) {}
                    }
                }

                long totalHp1 = 0;
                long totalHp2 = 0;
                for (int j = 0; j < zone.players.size(); j++) {
                    Player pj = zone.players.get(j);
                    if (pj != null && !pj.isdie && !pj.isSpectator) {
                        if (pj.clan != null && pj.clan.equals(this.clan1)) {
                            totalHp1 += pj.hp;
                        } else if (pj.clan != null && pj.clan.equals(this.clan2)) {
                            totalHp2 += pj.hp;
                        }
                    }
                }

                String nameClan1 = (this.clan1 != null) ? this.clan1.name : "Băng 1";
                String nameClan2 = (this.clan2 != null) ? this.clan2.name : "Băng 2";

                if (totalHp1 > totalHp2) {
                    for (int j = 0; j < zone.players.size(); j++) {
                        Player pj = zone.players.get(j);
                        if (pj != null) {
                            if (pj.clan != null && pj.clan.equals(this.clan1)) {
                                Pvp.pvp_notice(pj, 3);
                                if (!pj.isBot) {
                                    pj.pvp_win++;
                                    pj.pvppoint += 10;
                                    if (pj.getService() != null) pj.getService().UpdatePvpPoint();
                                }
                            } else {
                                Pvp.pvp_notice(pj, 4);
                                if (!pj.isBot) {
                                    pj.pvp_lose++;
                                    pj.pvppoint += 3;
                                    if (pj.getService() != null) pj.getService().UpdatePvpPoint();
                                }
                            }
                        }
                    }
                    PvpBang.notice(zone, "Hết thời gian - Băng " + nameClan1 + " có tổng HP cao hơn và giành Chiến Thắng! Đang đưa về phòng chờ...");
                    distributeRewards(clan1, clan2, true, false);
                    distributeRewards(clan2, clan1, false, false);
                    if (this.pvpBang1 != null) {
                        this.pvpBang1.win++;
                        this.pvpBang1.isWin = true;
                        this.pvpBang1.noticeFinished = "Kết quả PVP với băng " + nameClan2 + " - Thắng.";
                    }
                    if (this.pvpBang2 != null) {
                        this.pvpBang2.lose++;
                        this.pvpBang2.isWin = false;
                        this.pvpBang2.noticeFinished = "Kết quả PVP với băng " + nameClan1 + " - Thua.";
                    }
                } else if (totalHp2 > totalHp1) {
                    for (int j = 0; j < zone.players.size(); j++) {
                        Player pj = zone.players.get(j);
                        if (pj != null) {
                            if (pj.clan != null && pj.clan.equals(this.clan2)) {
                                Pvp.pvp_notice(pj, 3);
                                if (!pj.isBot) {
                                    pj.pvp_win++;
                                    pj.pvppoint += 10;
                                    if (pj.getService() != null) pj.getService().UpdatePvpPoint();
                                }
                            } else {
                                Pvp.pvp_notice(pj, 4);
                                if (!pj.isBot) {
                                    pj.pvp_lose++;
                                    pj.pvppoint += 3;
                                    if (pj.getService() != null) pj.getService().UpdatePvpPoint();
                                }
                            }
                        }
                    }
                    PvpBang.notice(zone, "Hết thời gian - Băng " + nameClan2 + " có tổng HP cao hơn và giành Chiến Thắng! Đang đưa về phòng chờ...");
                    distributeRewards(clan1, clan2, false, false);
                    distributeRewards(clan2, clan1, true, false);
                    if (this.pvpBang1 != null) {
                        this.pvpBang1.lose++;
                        this.pvpBang1.isWin = false;
                        this.pvpBang1.noticeFinished = "Kết quả PVP với băng " + nameClan2 + " - Thua.";
                    }
                    if (this.pvpBang2 != null) {
                        this.pvpBang2.win++;
                        this.pvpBang2.isWin = true;
                        this.pvpBang2.noticeFinished = "Kết quả PVP với băng " + nameClan1 + " - Thắng.";
                    }
                } else {
                    for (int j = 0; j < zone.players.size(); j++) {
                        Player pj = zone.players.get(j);
                        if (pj != null) {
                            Pvp.pvp_notice(pj, 5);
                            if (!pj.isBot) {
                                pj.pvppoint += 5;
                                if (pj.getService() != null) pj.getService().UpdatePvpPoint();
                            }
                        }
                    }
                    PvpBang.notice(zone, "Hết thời gian thi đấu - Kết quả hòa! Bạn sẽ được đưa về phòng chờ sau 5 giây.");
                    distributeRewards(clan1, clan2, false, true);
                    distributeRewards(clan2, clan1, false, true);
                    if (this.pvpBang1 != null) {
                        this.pvpBang1.noticeFinished = "Kết quả PVP với băng " + nameClan2 + " - Hòa.";
                    }
                    if (this.pvpBang2 != null) {
                        this.pvpBang2.noticeFinished = "Kết quả PVP với băng " + nameClan1 + " - Hòa.";
                    }
                }
            }
        } 
        // Trạng thái 4: Chuẩn bị hoàn tất (Hồi sinh, hồi máu và đưa về phòng chờ)
        else if (this.status_pvp == 4 && now >= this.nextStateTime - 1_000L && now < this.nextStateTime) {
            for (int i = 0; i < zone.players.size(); i++) {
                Player pi = zone.players.get(i);
                if (pi != null) {
                    pi.isdie = false;
                    if (pi.ability != null) {
                        pi.hp = (int) Math.max(1000, pi.ability.get_hp_max(true));
                        pi.mp = (int) Math.max(500, pi.ability.get_mp_max(true));
                    }
                    if (pi.getService() != null && pi.ability != null) {
                        pi.getService().use_potion(0, pi.ability.get_hp_max(true));
                        pi.getService().use_potion(1, pi.ability.get_mp_max(true));
                    }
                }
            }
        } 
        else if (this.status_pvp == 4 && now >= this.nextStateTime) {
            List<Player> playerList = new ArrayList<>(zone.players);
            playerList.forEach(l -> {
                try {
                    if (l != null) {
                        l.isdie = false;
                        if (l.ability != null) {
                            l.hp = (int) Math.max(1000, l.ability.get_hp_max(true));
                            l.mp = (int) Math.max(500, l.ability.get_mp_max(true));
                        }
                        if (l.getService() != null && l.ability != null) {
                            l.getService().use_potion(0, l.ability.get_hp_max(true));
                            l.getService().use_potion(1, l.ability.get_mp_max(true));
                        }
                        zone.change_flag(l, -1);
                        l.type_pk = -1;
                        if (l.getService() != null) {
                            l.getService().update_PK(l, false);
                        }

                        // Đưa người chơi thật về phòng chờ Map 260
                        if (!l.isBot) {
                            boolean returnedToWait = false;
                            if (l.clan != null) {
                                if (this.clan1 != null && l.clan.equals(this.clan1) && this.mapWaitClan1 != null && this.pvpBang1 != null) {
                                    Vgo vgo = new Vgo();
                                    vgo.map_go = new Zone[]{this.mapWaitClan1};
                                    vgo.xnew = (short) (480 + core.ZUtil.random(100));
                                    vgo.ynew = 260;
                                    l.goto_map(vgo);
                                    l.dungeon = this.pvpBang1;
                                    returnedToWait = true;
                                } else if (this.clan2 != null && l.clan.equals(this.clan2) && this.mapWaitClan2 != null && this.pvpBang2 != null && !this.isClan2Bot) {
                                    Vgo vgo = new Vgo();
                                    vgo.map_go = new Zone[]{this.mapWaitClan2};
                                    vgo.xnew = (short) (480 + core.ZUtil.random(100));
                                    vgo.ynew = 260;
                                    l.goto_map(vgo);
                                    l.dungeon = this.pvpBang2;
                                    returnedToWait = true;
                                }
                            }
                            if (!returnedToWait) {
                                l.dungeon = null;
                                l.return_to_previous_map();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Xóa toàn bộ bot khỏi mapFight
            for (Player p : playerList) {
                if (p != null && p.isBot && p instanceof bot.Bot) {
                    bot.Bot b = (bot.Bot) p;
                    if (b.map != null) {
                        b.map.remove_obj(b.index_map, 0);
                        b.map.leave_map(b, 0);
                    }
                }
            }

            // Dọn dẹp bot thừa trong phòng chờ để trận tiếp theo bot mới sinh lại cân bằng
            if (this.mapWaitClan1 != null) {
                for (Player pWait : new ArrayList<>(this.mapWaitClan1.players)) {
                    if (pWait != null && pWait.isBot && pWait instanceof bot.Bot) {
                        bot.Bot b = (bot.Bot) pWait;
                        if (b.map != null) {
                            b.map.remove_obj(b.index_map, 0);
                            b.map.leave_map(b, 0);
                        }
                    }
                }
            }

            // Cập nhật trạng thái phòng chờ Clan 1
            if (this.pvpBang1 != null && this.pvpBang1.mapWait != null && this.clan1 != null) {
                this.pvpBang1.mapFight = null;
                this.pvpBang1.maps.remove(zone);
                int ticket1 = PvpBang.getTicket(this.clan1);
                if (ticket1 < 5) {
                    this.pvpBang1.state = PvpBang.WAIT_FIND_FIGHT;
                    this.pvpBang1.timeState = System.currentTimeMillis() + core.ZUtil.random(20_000, 35_000);
                    for (Player pWait : this.pvpBang1.mapWait.players) {
                        if (pWait != null && !pWait.isBot && pWait.conn != null && pWait.getService() != null) {
                            pWait.getService().send_box_ThongBao_OK("Trận đấu PvP Băng đã kết thúc! Bạn đã quay về phòng chờ.\nLượt tham gia hôm nay: " + ticket1 + "/5");
                            pWait.getService().send_time_cool_down(this.pvpBang1.timeState, "Chờ ghép trận", 0);
                        }
                    }
                } else {
                    this.pvpBang1.state = PvpBang.TIME_UP;
                    this.pvpBang1.timeState = System.currentTimeMillis() + 15_000L;
                    for (Player pWait : this.pvpBang1.mapWait.players) {
                        if (pWait != null && !pWait.isBot && pWait.conn != null && pWait.getService() != null) {
                            pWait.getService().send_box_ThongBao_OK("Trận đấu PvP Băng đã kết thúc! Băng của bạn đã hoàn thành 5/5 lượt tham gia hôm nay.\nPhòng chờ sẽ đóng sau 15 giây.");
                            pWait.getService().send_time_cool_down(this.pvpBang1.timeState, "Đóng phòng chờ", 0);
                        }
                    }
                }
            }

            // Cập nhật trạng thái phòng chờ Clan 2
            if (this.isClan2Bot) {
                if (this.pvpBang2 != null && this.pvpBang2.mapWait != null) {
                    PvpBang.ENTRY.remove(this.pvpBang2.mapWait);
                    this.pvpBang2.mapWait.stop_map();
                    Zone.remove_map_plus(this.pvpBang2.mapWait);
                    this.pvpBang2.mapWait = null;
                }
            } else if (this.pvpBang2 != null && this.pvpBang2.mapWait != null && this.clan2 != null) {
                // Dọn bot trong mapWaitClan2 nếu có
                if (this.mapWaitClan2 != null) {
                    for (Player pWait : new ArrayList<>(this.mapWaitClan2.players)) {
                        if (pWait != null && pWait.isBot && pWait instanceof bot.Bot) {
                            bot.Bot b = (bot.Bot) pWait;
                            if (b.map != null) {
                                b.map.remove_obj(b.index_map, 0);
                                b.map.leave_map(b, 0);
                            }
                        }
                    }
                }
                this.pvpBang2.mapFight = null;
                this.pvpBang2.maps.remove(zone);
                int ticket2 = PvpBang.getTicket(this.clan2);
                if (ticket2 < 5) {
                    this.pvpBang2.state = PvpBang.WAIT_FIND_FIGHT;
                    this.pvpBang2.timeState = System.currentTimeMillis() + core.ZUtil.random(20_000, 35_000);
                    for (Player pWait : this.pvpBang2.mapWait.players) {
                        if (pWait != null && !pWait.isBot && pWait.conn != null && pWait.getService() != null) {
                            pWait.getService().send_box_ThongBao_OK("Trận đấu PvP Băng đã kết thúc! Bạn đã quay về phòng chờ.\nLượt tham gia hôm nay: " + ticket2 + "/5");
                            pWait.getService().send_time_cool_down(this.pvpBang2.timeState, "Chờ ghép trận", 0);
                        }
                    }
                } else {
                    this.pvpBang2.state = PvpBang.TIME_UP;
                    this.pvpBang2.timeState = System.currentTimeMillis() + 15_000L;
                    for (Player pWait : this.pvpBang2.mapWait.players) {
                        if (pWait != null && !pWait.isBot && pWait.conn != null && pWait.getService() != null) {
                            pWait.getService().send_box_ThongBao_OK("Trận đấu PvP Băng đã kết thúc! Băng của bạn đã hoàn thành 5/5 lượt tham gia hôm nay.\nPhòng chờ sẽ đóng sau 15 giây.");
                            pWait.getService().send_time_cool_down(this.pvpBang2.timeState, "Đóng phòng chờ", 0);
                        }
                    }
                }
            }

            this.status_pvp = 99;
            zone.stop_map();
            Zone.remove_map_plus(zone);
            zone.pvpBangMapFight = null;
            zone.map_dungeon = null;
        }
    }

    private synchronized void distributeRewards(Clan myClan, Clan oppClan, boolean isWin, boolean isDraw) {
        if (myClan == null || myClan.id < 0) return; // Bỏ qua bot clan

        int xpClan = isWin ? 2000 : (isDraw ? 800 : 500);
        myClan.update_xp(xpClan);

        long clanBeriPool = isWin ? 50_000_000L : (isDraw ? 20_000_000L : 10_000_000L);
        myClan.update_beri(clanBeriPool);

        int beriReward = isWin ? 1_000_000 : (isDraw ? 500_000 : 300_000);
        int rubyReward = isWin ? 100 : (isDraw ? 50 : 30);
        int extraItemReward = isWin ? 5 : (isDraw ? 3 : 2);

        List<template.GiftBox> pvpRewards = new ArrayList<>();
        pvpRewards.add(new template.GiftBox(4, 0, beriReward));
        pvpRewards.add(new template.GiftBox(4, 1, rubyReward));
        pvpRewards.add(new template.GiftBox(7, 1, extraItemReward));

        String oppName = (oppClan != null && oppClan.name != null) ? oppClan.name : "Đối thủ";
        String resultStr = isWin ? "Chiến Thắng" : (isDraw ? "Hòa" : "Thua");

        if (myClan.members != null) {
            for (int i1 = 0; i1 < myClan.members.size(); i1++) {
                String mName = myClan.members.get(i1).name;
                Player p0 = Zone.get_player_by_name_allmap(mName);
                if (p0 != null && (p0.isBot || p0.conn == null)) continue;
                if (p0 != null) {
                    try {
                        Clan.set_data(p0, false);
                        Clan.send_money(p0, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(mName);
                if (ids[0] > 0) {
                    core.MailService.sendMailOffline(ids[0], ids[1], mName, "Bang Hội", "PVP Bang Hội",
                            "Kết quả trận PVP Băng với " + oppName + " (" + resultStr + ")! Phần thưởng đính kèm bên dưới.",
                            core.MailService.MAIL_TYPE_GIFT, false, pvpRewards, 14L * 24 * 3600 * 1000);
                }
            }

            // Quà vinh danh cá nhân Top 1, 2, 3 cống hiến bang hội
            if (!myClan.members.isEmpty()) {
                List<clan.ClanMember> sortedMembers = new ArrayList<>(myClan.members);
                sortedMembers.sort((m1, m2) -> Integer.compare(m2.conghien, m1.conghien));
                int honorCount = Math.min(3, sortedMembers.size());
                for (int hIdx = 0; hIdx < honorCount; hIdx++) {
                    clan.ClanMember cm = sortedMembers.get(hIdx);
                    if (cm == null || cm.name == null || cm.name.isEmpty()) continue;
                    List<template.GiftBox> honorGifts = new ArrayList<>();
                    int hRank = hIdx + 1;
                    long hBeri = (hRank == 1) ? 30_000_000L : (hRank == 2 ? 20_000_000L : 10_000_000L);
                    int hRuby = (hRank == 1) ? 3000 : (hRank == 2 ? 2000 : 1000);
                    int hStoneId = 647 + hIdx; // 647, 648, 649 (Đá Thần Thoại)
                    int hVipTicket = (hRank == 1) ? 5 : (hRank == 2 ? 3 : 2); // 866 = Vé VIP

                    honorGifts.add(new template.GiftBox(4, 0, (int) hBeri));
                    honorGifts.add(new template.GiftBox(4, 1, hRuby));
                    honorGifts.add(new template.GiftBox(4, hStoneId, 1));
                    honorGifts.add(new template.GiftBox(4, 866, hVipTicket));

                    int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(cm.name);
                    if (ids[0] > 0) {
                        core.MailService.sendMailOffline(ids[0], ids[1], cm.name, "Hệ Thống",
                                "Vinh Danh Top " + hRank + " Cống Hiến Bang Hội",
                                "Chúc mừng bạn đã đạt Top " + hRank + " Cống Hiến trong Bang (" + cm.conghien + " điểm) sau trận PVP Băng với " + oppName + "! Phần thưởng vinh danh đính kèm bên dưới.",
                                core.MailService.MAIL_TYPE_GIFT, false, honorGifts, 14L * 24 * 3600 * 1000);
                    }
                }

                try {
                    org.json.simple.JSONObject logData = new org.json.simple.JSONObject();
                    logData.put("clan_id", myClan.id);
                    logData.put("clan_name", myClan.name);
                    logData.put("is_win", isWin);
                    logData.put("beri_pool", clanBeriPool);
                    logData.put("xp_pool", xpClan);
                    logData.put("time", System.currentTimeMillis());
                    historys.HistoryManager.saveSystemLog("CLAN_REWARD_HONOR", logData.toJSONString());
                } catch (Exception ignored) {}
            }

            try {
                if (!myClan.members.isEmpty()) {
                    myClan.chat_on_board(myClan.members.get(0).id,
                            myClan.members.get(0).name,
                            "PVP Băng với " + oppName + " (" + resultStr + "): nhận " + xpClan + " XP Bang và " + (clanBeriPool / 1_000_000) + "M Beri quỹ",
                            -3);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
