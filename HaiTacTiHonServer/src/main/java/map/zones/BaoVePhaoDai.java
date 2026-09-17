package map.zones;

import model.Player;
import clan.Clan;
import network.Message;
import network.Service;
import core.ZUtil;
import map.Zone;
import mob.Mob;
import map.Vgo;
import template.GiftBox;
import zabstracts.AbsDungeon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MOBA Clan Battle Dungeon - Protection of the Fortress.
 * Refactored to extend AbsDungeon and use tick-based update instead of threads.
 */
public class BaoVePhaoDai extends AbsDungeon {

    public static boolean is_open = true;
    public Mob truThuongATren;
    public Mob truThuongADuoi;
    public Mob truThuongBTren;
    public Mob truThuongBDuoi;
    public Mob truChinhA;
    public Mob truChinhB;
    public Mob bossDuongTren;
    public Mob bossDuongGiua;
    public Mob bossDuongDuoi;
    public boolean isClose = false;
    
    public Clan clanA;
    public Clan clanB;
    public Zone mapClanA;
    public Zone mapClanB;
    public int pointA;
    private int killA;
    public int pointB;
    private int killB;

    // Timestamps for spawning bosses to replace background Threads and sleep
    private long spawnBossTrenTime;
    private long spawnBossDuoiTime;
    private long spawnBossGiuaTime;
    private boolean announcedBossTren = false;
    private boolean announcedBossDuoi = false;
    private boolean announcedBossGiua = false;

    private Mob tempBossTren;
    private Mob tempBossDuoi;
    private Mob tempBossGiua;

    private static class RespawnEvent {
        Mob turret;
        long respawnTime;
        RespawnEvent(Mob turret, long respawnTime) {
            this.turret = turret;
            this.respawnTime = respawnTime;
        }
    }
    
    private final List<RespawnEvent> respawnQueue = new ArrayList<>();

    @Override
    public void create() {
        // Handled in legacy Init for compatibility
    }

    public void Init(Clan clan1, Clan clan2, List<Player> listP) {
        // Ghép random 50/50 phe Đỏ (Clan A) hoặc phe Xanh (Clan B)
        if (ZUtil.random(2) == 0) {
            this.clanA = clan1;
            this.clanB = clan2;
        } else {
            this.clanA = clan2;
            this.clanB = clan1;
        }
        this.time = System.currentTimeMillis() + 60_000 * 15;
        this.startTime = System.currentTimeMillis();
        this.spawnBossTrenTime = startTime + 60_000 * 5;
        this.spawnBossDuoiTime = startTime + 60_000 * 5;
        this.spawnBossGiuaTime = startTime + 60_000 * 10;

        // Initialize maps list inherited from AbsDungeon
        this.maps = new ArrayList<>();
        int index_mob = -2;

        for (int i2 = 267; i2 <= 271; i2++) {
            Zone mapTemplate = Zone.getMapByID(i2)[0];
            Zone map_boss = new Zone();
            map_boss.template = mapTemplate.template;
            map_boss.zone_id = (byte) 0;
            map_boss.baoVePhaoDai = this;
            map_boss.map_dungeon = this; // Hook Zone update to this dungeon
            map_boss.list_mob = new int[mapTemplate.list_mob.length];

            for (int i = 0; i < map_boss.list_mob.length; i++) {
                Mob temp = mapTemplate.getMob(mapTemplate.list_mob[i]);
                Mob mob_add = new Mob();
                mob_add.mtemplate = temp.mtemplate;
                mob_add.x = temp.x;
                mob_add.y = temp.y;
                mob_add.hp_max = (int) temp.hp_max;
                mob_add.hp = mob_add.hp_max;
                mob_add.level = 100;
                mob_add.isdie = false;
                mob_add.id_target = -1;
                mob_add.index = index_mob--;
                mob_add.map = map_boss;
                mob_add.boss_inf = null;

                switch (i2) {
                    case 268 -> {
                        if (mob_add.mtemplate.mob_id == 122 && i == 0) {
                            truThuongATren = mob_add;
                        } else if (mob_add.mtemplate.mob_id == 124) {
                            truThuongBTren = mob_add;
                        } else {
                            tempBossTren = mob_add;
                        }
                    }
                    case 269 -> {
                        if (mob_add.mtemplate.mob_id == 122 && i == 0) {
                            truThuongADuoi = mob_add;
                        } else if (mob_add.mtemplate.mob_id == 124) {
                            truThuongBDuoi = mob_add;
                        } else {
                            tempBossDuoi = mob_add;
                        }
                    }
                    case 270 -> {
                        if (mob_add.mtemplate.mob_id == 123 && i == 0) {
                            truChinhA = mob_add;
                        } else if (mob_add.mtemplate.mob_id == 125) {
                            truChinhB = mob_add;
                        } else {
                            tempBossGiua = mob_add;
                        }
                    }
                }
                map_boss.mobs.put(mob_add.index, mob_add);
            }
            map_boss.list_mob = new int[0];
            switch (i2) {
                case 267 -> mapClanA = map_boss;
                case 271 -> mapClanB = map_boss;
            }
            map_boss.start_map();
            Zone.add_map_plus(map_boss);
            this.maps.add(map_boss);
        }

        short avgLv = 60;
        if (listP != null && !listP.isEmpty()) {
            int sum = 0, cnt = 0;
            for (Player p : listP) {
                if (p != null) { sum += p.level; cnt++; }
            }
            if (cnt > 0) avgLv = (short) Math.max(1, sum / cnt);
        }

        // Đếm số lượng người chơi thật của mỗi phe
        int countA = 0;
        int countB = 0;
        if (listP != null) {
            for (Player p : listP) {
                if (p != null && p.clan != null) {
                    if (p.clan.equals(clanA)) countA++;
                    else if (p.clan.equals(clanB)) countB++;
                }
            }
        }

        int[] laneIndicesA = new int[]{1, 3, 2, 3, 1}; // maps 268 (Top), 270 (Mid), 269 (Bot)
        int[] laneIndicesB = new int[]{1, 3, 2, 3, 1};

        // 1. Xử lý Bot cho Clan A (Phe Đỏ - Cờ 4, Spawn bên trái X ~ 450)
        if (clanA != null && maps != null && maps.size() >= 5) {
            if (clanA.id < 0) {
                // Clan A là Bot Clan -> Sinh 3-5 bot đối thủ
                int botCount = Math.max(3, Math.min(5, Math.max(3, countB)));
                for (int i = 0; i < botCount; i++) {
                    try {
                        int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                        byte botClazz = (byte) ((i % 5) + 1);
                        String botName = bot.botplayer.BotNameGenerator.getRandomBotName();
                        bot.BotPVP b = new bot.BotPVP(botId, botName);
                        b.isBot = true;
                        b.clan = clanA;
                        b.clazz = botClazz;
                        b.level = (short) Math.max(1, avgLv + ZUtil.random(-2, 2));

                        if (listP != null && !listP.isEmpty()) {
                            bot.BotBalanceEngine.balanceAgainstTeam(b, listP, avgLv);
                        } else {
                            int botTier = b.level >= 90 ? 4 : (b.level >= 75 ? 3 : (b.level >= 50 ? 2 : 1));
                            b.setupBotEquip(botClazz, b.level, botTier, true, true, true);
                            b.setupBotSkills(botClazz, (short) Math.max(20, Math.min(30, b.level / 2)), botTier, 0);
                            b.setupBotAppearance(botClazz, botTier);
                            b.autoAllocatePotentialPoints(botClazz);
                            b.setin4();
                            b.init();
                            b.ability = new ability.Ability(b);
                            b.updateParts();
                        }
                        b.type_pk = 4;

                        int targetMapIdx = laneIndicesA[i % laneIndicesA.length];
                        Zone targetLane = maps.get(targetMapIdx);
                        short spawnX = (short) (450 + ZUtil.random(-30, 30));
                        short spawnY = (short) (260 + ZUtil.random(-20, 20));
                        b.join(targetLane, spawnX, spawnY);
                        targetLane.change_flag(b, 4);
                    } catch (Exception ignored) {}
                }
            } else {
                // Clan A là Real Clan -> Bù bot đồng đội nếu dưới 3 người
                int neededBots = Math.max(0, 3 - countA);
                for (int i = 0; i < neededBots; i++) {
                    try {
                        int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                        byte botClazz = (byte) ((i % 5) + 1);
                        String botName = "ĐồngĐội_" + bot.botplayer.BotNameGenerator.getRandomBotName();
                        bot.BotPVP b = new bot.BotPVP(botId, botName);
                        b.isBot = true;
                        b.clan = clanA;
                        b.clazz = botClazz;
                        b.level = (short) Math.max(1, avgLv + ZUtil.random(-2, 2));

                        if (listP != null && !listP.isEmpty()) {
                            bot.BotBalanceEngine.balanceAgainstTeam(b, listP, avgLv);
                        } else {
                            int botTier = b.level >= 90 ? 4 : (b.level >= 75 ? 3 : (b.level >= 50 ? 2 : 1));
                            b.setupBotEquip(botClazz, b.level, botTier, true, true, true);
                            b.setupBotSkills(botClazz, (short) Math.max(20, Math.min(30, b.level / 2)), botTier, 0);
                            b.setupBotAppearance(botClazz, botTier);
                            b.autoAllocatePotentialPoints(botClazz);
                            b.setin4();
                            b.init();
                            b.ability = new ability.Ability(b);
                            b.updateParts();
                        }
                        b.type_pk = 4;

                        int targetMapIdx = laneIndicesA[i % laneIndicesA.length];
                        Zone targetLane = maps.get(targetMapIdx);
                        short spawnX = (short) (450 + ZUtil.random(-30, 30));
                        short spawnY = (short) (260 + ZUtil.random(-20, 20));
                        b.join(targetLane, spawnX, spawnY);
                        targetLane.change_flag(b, 4);
                    } catch (Exception ignored) {}
                }
            }
        }

        // 2. Xử lý Bot cho Clan B (Phe Xanh - Cờ 5, Spawn bên phải X ~ 950)
        if (clanB != null && maps != null && maps.size() >= 5) {
            if (clanB.id < 0) {
                // Clan B là Bot Clan -> Sinh 3-5 bot đối thủ
                int botCount = Math.max(3, Math.min(5, Math.max(3, countA)));
                for (int i = 0; i < botCount; i++) {
                    try {
                        int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                        byte botClazz = (byte) ((i % 5) + 1);
                        String botName = bot.botplayer.BotNameGenerator.getRandomBotName();
                        bot.BotPVP b = new bot.BotPVP(botId, botName);
                        b.isBot = true;
                        b.clan = clanB;
                        b.clazz = botClazz;
                        b.level = (short) Math.max(1, avgLv + ZUtil.random(-2, 2));

                        if (listP != null && !listP.isEmpty()) {
                            bot.BotBalanceEngine.balanceAgainstTeam(b, listP, avgLv);
                        } else {
                            int botTier = b.level >= 90 ? 4 : (b.level >= 75 ? 3 : (b.level >= 50 ? 2 : 1));
                            b.setupBotEquip(botClazz, b.level, botTier, true, true, true);
                            b.setupBotSkills(botClazz, (short) Math.max(20, Math.min(30, b.level / 2)), botTier, 0);
                            b.setupBotAppearance(botClazz, botTier);
                            b.autoAllocatePotentialPoints(botClazz);
                            b.setin4();
                            b.init();
                            b.ability = new ability.Ability(b);
                            b.updateParts();
                        }
                        b.type_pk = 5;

                        int targetMapIdx = laneIndicesB[i % laneIndicesB.length];
                        Zone targetLane = maps.get(targetMapIdx);
                        short spawnX = (short) (950 + (i >= 3 ? 100 : 0) + ZUtil.random(-30, 30));
                        short spawnY = (short) (260 + ZUtil.random(-20, 20));
                        b.join(targetLane, spawnX, spawnY);
                        targetLane.change_flag(b, 5);
                    } catch (Exception ignored) {}
                }
            } else {
                // Clan B là Real Clan -> Bù bot đồng đội nếu dưới 3 người
                int neededBots = Math.max(0, 3 - countB);
                for (int i = 0; i < neededBots; i++) {
                    try {
                        int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                        byte botClazz = (byte) ((i % 5) + 1);
                        String botName = "ĐồngĐội_" + bot.botplayer.BotNameGenerator.getRandomBotName();
                        bot.BotPVP b = new bot.BotPVP(botId, botName);
                        b.isBot = true;
                        b.clan = clanB;
                        b.clazz = botClazz;
                        b.level = (short) Math.max(1, avgLv + ZUtil.random(-2, 2));

                        if (listP != null && !listP.isEmpty()) {
                            bot.BotBalanceEngine.balanceAgainstTeam(b, listP, avgLv);
                        } else {
                            int botTier = b.level >= 90 ? 4 : (b.level >= 75 ? 3 : (b.level >= 50 ? 2 : 1));
                            b.setupBotEquip(botClazz, b.level, botTier, true, true, true);
                            b.setupBotSkills(botClazz, (short) Math.max(20, Math.min(30, b.level / 2)), botTier, 0);
                            b.setupBotAppearance(botClazz, botTier);
                            b.autoAllocatePotentialPoints(botClazz);
                            b.setin4();
                            b.init();
                            b.ability = new ability.Ability(b);
                            b.updateParts();
                        }
                        b.type_pk = 5;

                        int targetMapIdx = laneIndicesB[i % laneIndicesB.length];
                        Zone targetLane = maps.get(targetMapIdx);
                        short spawnX = (short) (950 + (i >= 3 ? 100 : 0) + ZUtil.random(-30, 30));
                        short spawnY = (short) (260 + ZUtil.random(-20, 20));
                        b.join(targetLane, spawnX, spawnY);
                        targetLane.change_flag(b, 5);
                    } catch (Exception ignored) {}
                }
            }
        }

        // Chuyển người chơi thật (và lính đánh thuê) vào trận
        if (listP != null) {
            listP.forEach(p0 -> {
                try {
                    p0.dungeon = this;
                    p0.save_previous_map();
                    p0.time_hs_little_garden = 0;
                    boolean isTeamA = p0.clan != null && p0.clan.equals(clanA);
                    p0.type_pk = (byte) (isTeamA ? 4 : 5);
                    Vgo vgo = new Vgo();
                    vgo.map_go = new Zone[]{isTeamA ? mapClanA : mapClanB};
                    vgo.xnew = (short) (vgo.map_go[0].template != null && vgo.map_go[0].template.maxW > 0 ? vgo.map_go[0].template.maxW / 2 : 300);
                    vgo.ynew = (short) (vgo.map_go[0].template != null && vgo.map_go[0].template.maxH > 0 ? vgo.map_go[0].template.maxH / 2 : 250);
                    p0.goto_map(vgo);
                    if (p0.map != null) {
                        p0.map.change_flag(p0, p0.type_pk);
                    }
                    this.SendInfoMap(p0);
                } catch (Exception ignored) {}
            });
        }
    }

    @Override
    public void update(Zone zone) throws IOException {
        if (isClose) return;

        // Timeout: hết 15 phút -> kick toàn bộ player về map trước, stop 5 zones
        if (maps != null && !maps.isEmpty() && zone.equals(maps.get(0))) {
            long now = System.currentTimeMillis();
            if (now > this.time) {
                isClose = true;
                Clan winClan = (pointA > pointB || (pointA == pointB && killA >= killB)) ? clanA : clanB;
                Clan loseClan = winClan.equals(clanA) ? clanB : clanA;
                sendEndGameRewards(winClan, loseClan);
                // Kick tất cả player về map trước
                for (Zone m : new ArrayList<>(maps)) {
                    List<model.Player> pList = new ArrayList<>(m.players);
                    for (model.Player p0 : pList) {
                        try {
                            p0.type_pk = -1;
                            if (p0.getService() != null) {
                                p0.getService().update_PK(p0, false);
                            }
                            p0.dungeon = null;
                            p0.time_hs_little_garden = 0;
                            p0.return_to_previous_map();
                        } catch (Exception ignored) {}
                    }
                    m.setRunning(false);
                    m.baoVePhaoDai = null;
                    m.map_dungeon = null;
                }
                if (clanA != null) clanA.map_create = null;
                if (clanB != null) clanB.map_create = null;
                return;
            }

            // Handle boss spawning timestamps
            if (!announcedBossTren && now > spawnBossTrenTime && tempBossTren != null) {
                announcedBossTren = true;
                bossDuongTren = tempBossTren;
                sendBossSpawnMessage(bossDuongTren, "Boss đường trên xuất hiện");
            }
            if (!announcedBossDuoi && now > spawnBossDuoiTime && tempBossDuoi != null) {
                announcedBossDuoi = true;
                bossDuongDuoi = tempBossDuoi;
                sendBossSpawnMessage(bossDuongDuoi, "Boss đường dưới xuất hiện");
            }
            if (!announcedBossGiua && now > spawnBossGiuaTime && tempBossGiua != null) {
                announcedBossGiua = true;
                bossDuongGiua = tempBossGiua;
                sendBossSpawnMessage(bossDuongGiua, "Boss đường giữa xuất hiện");
            }

            // Handle turret respawn queue
            List<RespawnEvent> toRemove = new ArrayList<>();
            for (RespawnEvent event : respawnQueue) {
                if (now > event.respawnTime) {
                    toRemove.add(event);
                    Mob turret = event.turret;
                    turret.hp = turret.hp_max;
                    turret.isdie = false;

                    if (turret.mtemplate.mob_id == 122) {
                        if (turret.y < 300) truThuongATren = turret;
                        else truThuongADuoi = turret;
                    } else {
                        if (turret.y < 300) truThuongBTren = turret;
                        else truThuongBDuoi = turret;
                    }

                    Message m_local = new Message(1);
                    m_local.writer().writeByte(1);
                    m_local.writer().writeShort(turret.index);
                    m_local.writer().writeShort(turret.x);
                    m_local.writer().writeShort(turret.y);
                    turret.map.send_msg_all_p(m_local, null, true);
                    m_local.cleanup();
                    UpdateInfo();
                }
            }
            respawnQueue.removeAll(toRemove);
        }
    }

    private void sendBossSpawnMessage(Mob boss, String announcement) throws IOException {
        Message m_local = new Message(1);
        m_local.writer().writeByte(1);
        m_local.writer().writeShort(boss.index);
        m_local.writer().writeShort(boss.x);
        m_local.writer().writeShort(boss.y);
        boss.map.send_msg_all_p(m_local, null, true);
        m_local.cleanup();
        KTG(announcement);
        UpdateInfo();
    }

    private void UpdateInfo() throws IOException {
        List<Player> listP = new ArrayList<>();
        for (Zone map : maps) {
            listP.addAll(map.players);
        }
        for (Player p0 : listP) {
            p0.getService().send_time_cool_down(this.time, "Thời gian", 2);
        }
        Message m = new Message(51);
        m.writer().writeByte(0);
        m.writer().writeByte(9);
        m.writer().writeBoolean(truThuongATren == null);
        m.writer().writeBoolean(truChinhA == null);
        m.writer().writeBoolean(truThuongADuoi == null);
        m.writer().writeBoolean(bossDuongTren == null);
        m.writer().writeBoolean(bossDuongGiua == null);
        m.writer().writeBoolean(bossDuongDuoi == null);
        m.writer().writeBoolean(truThuongBTren == null);
        m.writer().writeBoolean(truChinhB == null);
        m.writer().writeBoolean(truThuongBDuoi == null);
        for (Player p0 : listP) {
            p0.addmsg(m);
        }
        m.cleanup();
        m = new Message(51);
        m.writer().writeByte(3);
        m.writer().writeShort(killA);
        m.writer().writeShort(pointA);
        m.writer().writeShort(killB);
        m.writer().writeShort(pointB);
        for (Player p0 : listP) {
            p0.addmsg(m);
        }
    }

    public void SendInfoMap(Player p) throws IOException {
        p.getService().send_time_cool_down(this.time, "Thời gian", 2);
        Message m = new Message(51);
        m.writer().writeByte(0);
        m.writer().writeByte(9);
        m.writer().writeBoolean(truThuongATren == null);
        m.writer().writeBoolean(truChinhA == null);
        m.writer().writeBoolean(truThuongADuoi == null);
        m.writer().writeBoolean(bossDuongTren == null);
        m.writer().writeBoolean(bossDuongGiua == null);
        m.writer().writeBoolean(bossDuongDuoi == null);
        m.writer().writeBoolean(truThuongBTren == null);
        m.writer().writeBoolean(truChinhB == null);
        m.writer().writeBoolean(truThuongBDuoi == null);
        p.addmsg(m);
        m.cleanup();
        m = new Message(51);
        m.writer().writeByte(3);
        m.writer().writeShort(killA);
        m.writer().writeShort(pointA);
        m.writer().writeShort(killB);
        m.writer().writeShort(pointB);
        p.addmsg(m);
        UpdateMob(p);
    }

    private void UpdateMob(Player p) throws IOException {
        MobFlag(p, truThuongATren, 4);
        MobFlag(p, truThuongADuoi, 4);
        MobFlag(p, truChinhA, 4);
        MobFlag(p, truThuongBTren, 5);
        MobFlag(p, truThuongBDuoi, 5);
        MobFlag(p, truChinhB, 5);
        MobFlag(p, bossDuongDuoi, -1);
        MobFlag(p, bossDuongGiua, -1);
        MobFlag(p, bossDuongTren, -1);
    }

    public Mob GetMob(int idx) {
        if (truThuongATren != null && truThuongATren.index == idx) return truThuongATren;
        if (truThuongADuoi != null && truThuongADuoi.index == idx) return truThuongADuoi;
        if (truChinhA != null && truChinhA.index == idx) return truChinhA;
        if (truThuongBTren != null && truThuongBTren.index == idx) return truThuongBTren;
        if (truThuongBDuoi != null && truThuongBDuoi.index == idx) return truThuongBDuoi;
        if (truChinhB != null && truChinhB.index == idx) return truChinhB;
        if (bossDuongDuoi != null && bossDuongDuoi.index == idx) return bossDuongDuoi;
        if (bossDuongGiua != null && bossDuongGiua.index == idx) return bossDuongGiua;
        if (bossDuongTren != null && bossDuongTren.index == idx) return bossDuongTren;
        return null;
    }

    private void MobFlag(Player p, Mob tru, int flag) throws IOException {
        if (tru != null && tru.map.equals(p.map)) {
            Message m = new Message(1);
            m.writer().writeByte(1);
            m.writer().writeShort(tru.index);
            m.writer().writeShort(tru.x);
            m.writer().writeShort(tru.y);
            p.addmsg(m);
            p.getService().send_mob_info(tru);
            m = new Message(51);
            m.writer().writeByte(1);
            m.writer().writeShort(tru.index);
            m.writer().writeByte(flag);
            p.addmsg(m);
        }
    }

    public void MobFlag(Player p) throws IOException {
        if (p.map.template.id == 268) {
            if (truThuongATren != null) sendFlagMsg(p, truThuongATren.index, 4);
            if (truThuongBTren != null) sendFlagMsg(p, truThuongBTren.index, 5);
        } else if (p.map.template.id == 269) {
            if (truThuongADuoi != null) sendFlagMsg(p, truThuongADuoi.index, 4);
            if (truThuongBDuoi != null) sendFlagMsg(p, truThuongBDuoi.index, 5);
        } else if (p.map.template.id == 270) {
            if (truChinhA != null) sendFlagMsg(p, truChinhA.index, 4);
            if (truChinhB != null) sendFlagMsg(p, truChinhB.index, 5);
        }
    }

    private void sendFlagMsg(Player p, int idx, int flag) throws IOException {
        Message m = new Message(51);
        m.writer().writeByte(1);
        m.writer().writeByte(1);
        m.writer().writeShort(idx);
        m.writer().writeByte(flag);
        p.addmsg(m);
    }

    public void UpdateDie(Clan cl) throws IOException {
        if (cl != null && cl.equals(clanA)) killA++;
        else killB++;
        UpdateInfo();
    }

    public void UpdateDie(Player p, Mob target) throws IOException {
        List<Player> listP = new ArrayList<>();
        for (Zone map : maps) {
            listP.addAll(map.players);
        }
        long now = System.currentTimeMillis();

        if (truThuongATren != null && truThuongATren.equals(target)) {
            final Mob mOld = truThuongATren;
            respawnQueue.add(new RespawnEvent(mOld, now + 120_000));
            truThuongATren = null;
            pointB++;
        } else if (truThuongADuoi != null && truThuongADuoi.equals(target)) {
            final Mob mOld = truThuongADuoi;
            respawnQueue.add(new RespawnEvent(mOld, now + 120_000));
            truThuongADuoi = null;
            pointB++;
        } else if (truChinhA != null && truChinhA.equals(target)) {
            truChinhA = null;
            KTG("Trụ chính Đội Đỏ đã bị phá hủy! Đội Xanh (" + (clanB != null ? clanB.name : "") + ") giành chiến thắng!");
            sendEndGameRewards(clanB, clanA);
            this.time = System.currentTimeMillis() + 5_000; // Đóng sau 5s
        } else if (truThuongBTren != null && truThuongBTren.equals(target)) {
            final Mob mOld = truThuongBTren;
            respawnQueue.add(new RespawnEvent(mOld, now + 120_000));
            truThuongBTren = null;
            pointA++;
        } else if (truThuongBDuoi != null && truThuongBDuoi.equals(target)) {
            final Mob mOld = truThuongBDuoi;
            respawnQueue.add(new RespawnEvent(mOld, now + 120_000));
            truThuongBDuoi = null;
            pointA++;
        } else if (truChinhB != null && truChinhB.equals(target)) {
            truChinhB = null;
            KTG("Trụ chính Đội Xanh đã bị phá hủy! Đội Đỏ (" + (clanA != null ? clanA.name : "") + ") giành chiến thắng!");
            sendEndGameRewards(clanA, clanB);
            this.time = System.currentTimeMillis() + 5_000; // Đóng sau 5s
        } else if (bossDuongDuoi != null && bossDuongDuoi.equals(target)) {
            bossDuongDuoi = null;
            boolean isTeamA = (p != null && ((p.clan != null && p.clan.equals(clanA)) || p.type_pk == 4));
            for (Player p0 : listP) {
                if (p0 != null && ((p0.clan != null && p0.clan.equals(isTeamA ? clanA : clanB)) || p0.type_pk == (isTeamA ? 4 : 5))) {
                    p0.add_new_eff(51, 1, 60_000 * 2);
                }
            }
            KTG("Đội " + (isTeamA ? "Đỏ" : "Xanh") + " tiêu diệt Boss đường dưới nhận +50% dame trong 2 phút");
        } else if (bossDuongGiua != null && bossDuongGiua.equals(target)) {
            bossDuongGiua = null;
            boolean isTeamA = (p != null && ((p.clan != null && p.clan.equals(clanA)) || p.type_pk == 4));
            for (Player p0 : listP) {
                if (p0 != null && ((p0.clan != null && p0.clan.equals(isTeamA ? clanA : clanB)) || p0.type_pk == (isTeamA ? 4 : 5))) {
                    p0.add_new_eff(52, 1, 60_000 * 2);
                }
            }
            KTG("Đội " + (isTeamA ? "Đỏ" : "Xanh") + " tiêu diệt Boss đường giữa nhận được bùa hồi sinh");
        } else if (bossDuongTren != null && bossDuongTren.equals(target)) {
            bossDuongTren = null;
            boolean isTeamA = (p != null && ((p.clan != null && p.clan.equals(clanA)) || p.type_pk == 4));
            for (Player p0 : listP) {
                if (p0 != null && ((p0.clan != null && p0.clan.equals(isTeamA ? clanA : clanB)) || p0.type_pk == (isTeamA ? 4 : 5))) {
                    p0.add_new_eff(53, 1, 60_000 * 2);
                }
            }
            KTG("Đội " + (isTeamA ? "Đỏ" : "Xanh") + " tiêu diệt Boss đường trên nhận +50% dame trong 2 phút");
        }
        UpdateInfo();
    }

    private void sendEndGameRewards(Clan winClan, Clan loseClan) {
        List<GiftBox> winRewards = new ArrayList<>();
        winRewards.add(new GiftBox(4, 0, 1_000_000)); // 1m Beri
        winRewards.add(new GiftBox(4, 1, 200));       // 200 Ruby
        winRewards.add(new GiftBox(7, 1, 10));        // 10 Đá Cường Hóa

        List<GiftBox> loseRewards = new ArrayList<>();
        loseRewards.add(new GiftBox(4, 0, 300_000));  // 300k Beri
        loseRewards.add(new GiftBox(4, 1, 50));       // 50 Ruby
        loseRewards.add(new GiftBox(7, 1, 3));        // 3 Đá Cường Hóa

        List<Player> listP = new ArrayList<>();
        if (maps != null) {
            for (Zone map : maps) {
                listP.addAll(map.players);
            }
        }

        for (Player p : listP) {
            if (p == null || p.isBot || p.conn == null) continue;
            if (winClan != null && p.clan != null && p.clan.equals(winClan)) {
                core.MailService.sendMail(p, "Bang Hội", "Bảo Vệ Pháo Đài",
                        "Chúc mừng Bang [" + winClan.name + "] đã giành chiến thắng Bảo Vệ Pháo Đài! Phần thưởng chiến thắng đính kèm bên dưới.",
                        core.MailService.MAIL_TYPE_GIFT, false, winRewards, 14L * 24 * 3600 * 1000);
            } else if (loseClan != null && p.clan != null && p.clan.equals(loseClan)) {
                core.MailService.sendMail(p, "Bang Hội", "Bảo Vệ Pháo Đài",
                        "Cảm ơn bạn đã tham gia Bảo Vệ Pháo Đài cùng Bang [" + loseClan.name + "]! Phần thưởng khích lệ đính kèm bên dưới.",
                        core.MailService.MAIL_TYPE_GIFT, false, loseRewards, 14L * 24 * 3600 * 1000);
            }
        }
    }

    private void KTG(String s) throws IOException {
        Message m = new Message(-31);
        m.writer().writeByte(0);
        m.writer().writeUTF(s);
        m.writer().writeByte(5);
        m.writer().writeShort(-1);
        List<Player> listP = new ArrayList<>();
        for (Zone map : maps) {
            listP.addAll(map.players);
        }
        for (Player p0 : listP) {
            p0.addmsg(m);
        }
        m.cleanup();
    }
}
