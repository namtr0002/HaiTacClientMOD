package map.zones;

import clan.Clan;
import model.Player;
import model.Quest;
import map.Zone;
import mob.Mob;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import network.Message;

/**
 * MapTranChienKhongLo — Instance dungeon cho Phó Bản Khổng Lồ (Dorry vs Brogy).
 *
 * <p>Mỗi instance là 1 trận giữa 2 clan (hoặc 1 clan vs bot-clan nếu clan2 == null).
 * Extends ClanDungeon vì đây là hoạt động PvEvP cấp Bang Hội.
 */
public class MapTranChienKhongLo extends zabstracts.AbsClanDungeon {
    public int hp_1 = 20;
    public int mp_1 = 0;
    public int hp_2 = 20;
    public int mp_2 = 0;
    public boolean is_finish = false;
    public Clan clan1;
    public Clan clan2;

    public void update_hp_mp() throws IOException {
        Message m = new Message(-80);
        m.writer().writeByte(2);
        m.writer().writeByte(hp_1);
        m.writer().writeByte(mp_1);
        m.writer().writeByte(hp_2);
        m.writer().writeByte(mp_2);
        this.maps.get(0).send_msg_all_p(m, null, true);
        m.cleanup();
    }

    public static MapTranChienKhongLo createDungeon(Clan c1, Clan c2) {
        MapTranChienKhongLo dungeon = new MapTranChienKhongLo(c1, c2);
        dungeon.create();
        return dungeon;
    }

    /**
     * Tạo dungeon solo: clan thật vs bot.
     * Người chơi thật đi cùng thành viên trong clan / lính đánh thuê (KHÔNG thêm bot vào clan người thật).
     * Phía đối thủ sinh ra Đội Bot Clan đối địch (Phe Brogy - Cờ 5).
     */
    public static MapTranChienKhongLo createDungeonSolo(Clan clan1) {
        Clan botClan = new Clan();
        botClan.id = (short) (-555 - core.ZUtil.random(100));
        botClan.name = bot.botplayer.BotNameGenerator.generateRandomClanName();
        botClan.icon = (short) core.ZUtil.random(1, 10);
        botClan.level = (short) Math.max(1, (clan1 != null && clan1.level > 0) ? clan1.level : (short) core.ZUtil.random(5, 12));
        botClan.trungsinh = (clan1 != null && clan1.trungsinh > 0) ? clan1.trungsinh : (byte) core.ZUtil.random(0, 2);
        botClan.members = new ArrayList<>();
        clan.ClanMember botLeader = new clan.ClanMember();
        botLeader.id = botClan.id;
        botLeader.name = "Brogy " + bot.botplayer.BotNameGenerator.getRandomBotName();
        botLeader.level = (short) (botClan.level * 6 + core.ZUtil.random(10, 20));
        botLeader.levelInclan = 0;
        botClan.members.add(botLeader);

        MapTranChienKhongLo dungeon = new MapTranChienKhongLo(clan1, botClan);
        dungeon.create();
        if (!dungeon.maps.isEmpty()) {
            Zone mapFight = dungeon.maps.get(0);
            if (clan1 != null) clan1.map_create = mapFight;
            botClan.map_create = mapFight;

            // Tính avgLv từ clan1 members
            int sum = 0, cnt = 0;
            if (clan1 != null && clan1.members != null) {
                for (int i = 0; i < clan1.members.size(); i++) {
                    Player p0 = Zone.get_player_by_name_allmap(clan1.members.get(i).name);
                    if (p0 != null) { sum += p0.level; cnt++; }
                }
            }
            short avgLv = cnt > 0 ? (short) Math.max(1, sum / cnt) : 50;
            int team2BotCount = Math.max(4, Math.min(6, cnt > 0 ? cnt : 4));

            // Tuyệt đối KHÔNG thêm bot vào Team 1 (người chơi thật)
            // Điền bot vào Team 2 đối địch (Brogy - Cờ 5)
            bot.BotTranChienKhongLo.autoFill(mapFight, 5, team2BotCount, avgLv, botClan);
        }
        return dungeon;
    }

    public void join(List<Player> list) {
        if (list == null || list.isEmpty()) return;
        list.forEach(p0 -> {
            if (p0 != null) {
                p0.save_previous_map();
                if (p0.map != null) {
                    p0.map.leave_map(p0, 2);
                }
            }
        });
        list.forEach(p0 -> {
            if (p0 != null) {
                try {
                    this.join(p0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MapTranChienKhongLo() {
        this.mobs = new CopyOnWriteArrayList<>();
    }

    public MapTranChienKhongLo(Clan clan1, Clan clan2) {
        this.clan1 = clan1;
        this.clan2 = clan2;
        this.mobs = new CopyOnWriteArrayList<>();
    }

    @Override
    public void create() {
        this.time = System.currentTimeMillis() + 60_000L * 15;
        this.mobs = new CopyOnWriteArrayList<>();
        this.maps = new CopyOnWriteArrayList<>();

        int index_mob = -2;
        Zone mapTemplate = Zone.getMapByID(81)[0];
        Zone map_dungeon = new Zone();
        map_dungeon.template = mapTemplate.template;
        map_dungeon.zone_id = (byte) 0;
        map_dungeon.list_mob = new int[0];
        map_dungeon.map_little_garden = this;
        map_dungeon.map_dungeon = this;

        for (int i = 0; i < mapTemplate.list_mob.length; i++) {
            Mob temp = mapTemplate.getMob(mapTemplate.list_mob[i]);
            if (temp != null && temp.mtemplate != null) {
                Mob mob_add = new Mob();
                mob_add.mtemplate = temp.mtemplate;
                mob_add.x = temp.x;
                mob_add.y = temp.y;
                mob_add.hp_max = temp.mtemplate.hp_max;
                mob_add.hp = mob_add.hp_max;
                mob_add.level = 75;
                mob_add.isdie = false;
                mob_add.id_target = -1;
                mob_add.index = index_mob--;
                mob_add.map = map_dungeon;
                mob_add.boss_inf = null;
                this.mobs.add(mob_add);
                map_dungeon.mobs.put(mob_add.index, mob_add);
            }
        }
        map_dungeon.start_map();
        Zone.add_map_plus(map_dungeon);
        this.maps.add(map_dungeon);
    }

    @Override
    public void join(Player p) throws IOException {
        if (maps != null && !maps.isEmpty() && p != null) {
            p.save_previous_map();
            p.map = maps.get(0);
            if (p.clan != null && p.clan.equals(this.clan1)) {
                p.type_pk = 4;
            } else if (p.clan != null && p.clan.equals(this.clan2)) {
                p.type_pk = 5;
            } else if (p.type_pk != 4 && p.type_pk != 5) {
                p.type_pk = 4;
            }
            p.x = (short) (p.type_pk == 4 ? 350 : 1400);
            p.y = 260;
            p.xold = p.x;
            p.yold = p.y;
            p.map.goto_map(p);
            p.dungeon = this;
            p.map.change_flag(p, p.type_pk);
            
            // Đồng bộ cờ PK cho Đệ Tử
            if (p.detu != null) {
                p.detu.type_pk = p.type_pk;
                p.detu.clan = p.clan;
                p.detu.typePirate = p.typePirate;
                p.map.change_flag(p.detu, p.type_pk);
            }
            
            // Đồng bộ cờ PK cho Lính Đánh Thuê
            try {
                java.util.List<bot.mercenary.MercenaryBot> activeBots = bot.mercenary.MercenaryManager.gI().getActiveBots(p);
                if (activeBots != null) {
                    for (bot.mercenary.MercenaryBot merc : activeBots) {
                        if (merc != null) {
                            merc.type_pk = p.type_pk;
                            merc.clan = p.clan;
                            merc.typePirate = p.typePirate;
                            if (merc.map != null) {
                                merc.map.change_flag(merc, p.type_pk);
                            }
                        }
                    }
                }
            } catch (Throwable ignored) {}

            if (p.getService() != null) {
                p.getService().update_PK(p, true);
                p.getService().pet(p, true);
                Quest.update_map_have_side_quest(p, true);
                TranChienKhongLo.send_info(p);
                p.getService().send_time_cool_down(this.time, "Thời gian", 2);
            }
        }
    }

    public void join() {
        if (maps == null || maps.isEmpty()) return;
        if (clan1 != null) {
            clan1.map_create = maps.get(0);
            if (clan1.members != null) {
                for (int i = 0; i < clan1.members.size(); i++) {
                    Player p0 = Zone.get_player_by_name_allmap(clan1.members.get(i).name);
                    if (p0 != null) {
                        p0.save_previous_map();
                        p0.type_pk = 4;
                        if (p0.map != null) {
                            p0.map.leave_map(p0, 2);
                        }
                        try {
                            this.join(p0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (clan2 != null) {
            clan2.map_create = maps.get(0);
            if (clan2.members != null) {
                for (int i = 0; i < clan2.members.size(); i++) {
                    Player p0 = Zone.get_player_by_name_allmap(clan2.members.get(i).name);
                    if (p0 != null) {
                        p0.save_previous_map();
                        p0.type_pk = 5;
                        if (p0.map != null) {
                            p0.map.leave_map(p0, 2);
                        }
                        try {
                            this.join(p0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update(Zone zone) throws IOException {
        for (int i = 0; i < this.mobs.size(); i++) {
            Mob mob = this.mobs.get(i);
            if (mob != null) {
                if (mob.isdie) {
                    if (mob.time_refresh <= 0 || mob.time_refresh < System.currentTimeMillis()) {
                        mob.isdie = false;
                        mob.hp = mob.hp_max;
                        mob.id_target = -1;
                        mob.time_refresh = 0;
                        zone.mobs.put(mob.index, mob);
                        try {
                            // Gửi gói tin tạo lại quái chuẩn đầy đủ (Message 4)
                            Message m_add = new Message(4);
                            m_add.writer().writeShort(mob.index);
                            m_add.writer().writeShort(mob.mtemplate.mob_id);
                            m_add.writer().writeShort(mob.x);
                            m_add.writer().writeShort(mob.y);
                            m_add.writer().writeShort(mob.level);
                            m_add.writer().writeInt(mob.hp);
                            m_add.writer().writeInt(mob.hp_max);
                            short skill0 = (mob.mtemplate.skill != null && mob.mtemplate.skill.length > 0) ? mob.mtemplate.skill[0] : 0;
                            m_add.writer().writeShort(skill0);
                            m_add.writer().writeShort((short) Mob.TIME_RESPAWN);
                            m_add.writer().writeByte(mob.mtemplate.typemonster);
                            m_add.writer().writeByte(mob.boss_inf != null ? mob.boss_inf.levelBoss : 0);
                            zone.send_msg_all_p(m_add, null, true);
                            m_add.cleanup();

                            // Gửi gói tin di chuyển / cập nhật tọa độ (Message 1)
                            Message m_local = new Message(1);
                            m_local.writer().writeByte(1);
                            m_local.writer().writeShort(mob.index);
                            m_local.writer().writeShort(mob.x);
                            m_local.writer().writeShort(mob.y);
                            zone.send_msg_all_p(m_local, null, true);
                            m_local.cleanup();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        if (this.is_finish || this.time < System.currentTimeMillis()) {
            int xp_receiv1 = 400;
            int xp_receiv2 = 400;
            int rb_receiv1 = 150;
            int rb_receiv2 = 150;
            if (this.hp_1 <= 0) {
                xp_receiv2 = 1000;
                xp_receiv1 = 300;
                rb_receiv2 = 200;
                rb_receiv1 = 100;
            } else if (this.hp_2 <= 0) {
                xp_receiv1 = 1000;
                xp_receiv2 = 300;
                rb_receiv1 = 200;
                rb_receiv2 = 100;
            }

            // Trao thưởng Clan 1
            if (this.clan1 != null) {
                this.clan1.update_xp(xp_receiv1);
                this.clan1.update_ruby(rb_receiv1);
                if (this.clan1.members != null) {
                    for (int i1 = 0; i1 < this.clan1.members.size(); i1++) {
                        Player p0 = Zone.get_player_by_name_allmap(this.clan1.members.get(i1).name);
                        if (p0 != null) {
                            try {
                                clan.Clan.set_data(p0, false);
                                clan.Clan.send_money(p0, false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        if (!this.clan1.members.isEmpty() && this.clan1.members.get(0) != null) {
                            this.clan1.chat_on_board(
                                    this.clan1.members.get(0).id,
                                    this.clan1.members.get(0).name,
                                    ("Phó bản khổng lồ với: " + (this.clan2 != null ? this.clan2.name : "Đội Bot")
                                    + ": nhận được " + xp_receiv1 + " xp băng và " + rb_receiv1
                                    + " ruby băng"),
                                    -3);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                this.clan1.map_create = null;
            }

            // Trao thưởng Clan 2 (nếu là người thật)
            if (this.clan2 != null) {
                this.clan2.update_xp(xp_receiv2);
                this.clan2.update_ruby(rb_receiv2);
                if (this.clan2.members != null) {
                    for (int i1 = 0; i1 < this.clan2.members.size(); i1++) {
                        Player p0 = Zone.get_player_by_name_allmap(this.clan2.members.get(i1).name);
                        if (p0 != null) {
                            try {
                                clan.Clan.set_data(p0, false);
                                clan.Clan.send_money(p0, false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        if (!this.clan2.members.isEmpty() && this.clan2.members.get(0) != null) {
                            this.clan2.chat_on_board(
                                    this.clan2.members.get(0).id,
                                    this.clan2.members.get(0).name,
                                    ("Phó bản khổng lồ với: " + (this.clan1 != null ? this.clan1.name : "băng khác")
                                    + ": nhận được " + xp_receiv2 + " xp băng và " + rb_receiv2
                                    + " ruby băng"),
                                    -3);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                this.clan2.map_create = null;
            }

            // Xoá bot nếu solo clan vs bots
            if (this.clan2 == null) {
                for (bot.BotTranChienKhongLo b : bot.BotTranChienKhongLo.POOL) {
                    if (!this.maps.isEmpty() && this.maps.get(0).equals(b.map)) {
                        b.leave();
                    }
                }
            }

            map.Vgo vgo = new map.Vgo();
            vgo.map_go = Zone.getMapByID(33);
            vgo.xnew = 710;
            vgo.ynew = 320;
            List<Player> playerList = new ArrayList<>();
            for (int i = 0; i < zone.players.size(); i++) {
                Player p0 = zone.players.get(i);
                if (p0 != null && !p0.isBot && p0.conn != null) {
                    playerList.add(p0);
                }
            }

            // Gửi quà vào Hộp Thư cho người chơi thật tham gia
            List<template.GiftBox> gifts1 = new ArrayList<>();
            gifts1.add(new template.GiftBox(4, 0, (hp_1 > hp_2) ? 1_000_000 : 400_000));
            gifts1.add(new template.GiftBox(4, 1, (hp_1 > hp_2) ? 100 : 30));
            gifts1.add(new template.GiftBox(7, 1, (hp_1 > hp_2) ? 5 : 2));

            List<template.GiftBox> gifts2 = new ArrayList<>();
            gifts2.add(new template.GiftBox(4, 0, (hp_2 > hp_1) ? 1_000_000 : 400_000));
            gifts2.add(new template.GiftBox(4, 1, (hp_2 > hp_1) ? 100 : 30));
            gifts2.add(new template.GiftBox(7, 1, (hp_2 > hp_1) ? 5 : 2));

            for (Player p : playerList) {
                if (p == null || p.isBot || p.conn == null) continue;
                if (this.clan1 != null && p.clan != null && p.clan.equals(this.clan1)) {
                    core.MailService.sendMail(p, "Bang Hội", "Phó Bản Khổng Lồ",
                            "Phần thưởng tham gia Phó Bản Khổng Lồ (" + (hp_1 > hp_2 ? "Thắng" : (hp_1 == hp_2 ? "Hòa" : "Thua")) + ")!",
                            core.MailService.MAIL_TYPE_GIFT, false, gifts1, 14L * 24 * 3600 * 1000);
                } else if (this.clan2 != null && p.clan != null && p.clan.equals(this.clan2)) {
                    core.MailService.sendMail(p, "Bang Hội", "Phó Bản Khổng Lồ",
                            "Phần thưởng tham gia Phó Bản Khổng Lồ (" + (hp_2 > hp_1 ? "Thắng" : (hp_2 == hp_1 ? "Hòa" : "Thua")) + ")!",
                            core.MailService.MAIL_TYPE_GIFT, false, gifts2, 14L * 24 * 3600 * 1000);
                }
            }

            playerList.forEach(l -> {
                if (l != null) {
                    try {
                        zone.change_flag(l, -1);
                        l.type_pk = -1;
                        l.dungeon = null;
                        if (l.pre_map_id > 0 && !Zone.map_cant_save_site(l.pre_map_id)) {
                            l.return_to_previous_map();
                        } else {
                            map.Vgo vgoExit = new map.Vgo();
                            vgoExit.map_go = Zone.getMapByID(79);
                            vgoExit.xnew = 350;
                            vgoExit.ynew = 250;
                            l.goto_map(vgoExit);
                        }
                        if (l.getService() != null) {
                            l.getService().update_PK(l, false);
                            l.getService().send_box_ThongBao_OK("Trận Chiến Khổng Lồ đã kết thúc!");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            zone.setRunning(false);
            zone.map_little_garden = null;
            zone.map_dungeon = null;
        }
    }
}
