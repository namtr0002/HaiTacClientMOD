package map.zones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import clan.Clan;
import model.Player;
import ability.Ability;
import core.ZUtil;
import network.Message;
import map.Zone;
import map.Vgo;

public class PvpBang extends zabstracts.AbsClanDungeon {

    public static final short WAIT_FIND_FIGHT = 0;
    public static final short WAIT_TO_BEGIN = 1;
    public static final short FIGHTING = 2;
    public static final short TIME_UP = 3;

    public static final List<Zone> ENTRY = new CopyOnWriteArrayList<>();
    public static final ConcurrentHashMap<Clan, Integer> mapClanTicket = new ConcurrentHashMap<>();
    public static int typeFight = 0;
    
    public int state;
    public long timeState;
    public Clan clan;
    public Zone mapWait;
    public Zone mapFight;
    public HashMap<String, String> mapOpponent;
    public byte win, lose;
    public String noticeFinished = "";
    public boolean isWin;
    public boolean isBotClan = false;

    public static boolean isOpen() {
        return activities.TimedDungeonManager.gI().isDungeonOpen("PVP_BANG");
    }

    public static PvpBang createDungeon(Clan clan) {
        PvpBang dungeon = new PvpBang(clan);
        dungeon.create();
        return dungeon;
    }

    public PvpBang() {
        this.maps = new ArrayList<>();
    }

    public PvpBang(Clan clan) {
        this.clan = clan;
        this.state = 0;
        this.maps = new ArrayList<>();
    }

    @Override
    public void create() {
        Zone mapTemplate = Zone.getMapByID(260)[0];
        Zone mapPvPClan = new Zone();
        mapPvPClan.template = mapTemplate.template;
        mapPvPClan.zone_id = (byte) 0;
        mapPvPClan.list_mob = new int[0];
        mapPvPClan.pvpBang = this;
        mapPvPClan.map_dungeon = this;
        
        this.timeState = System.currentTimeMillis() + ZUtil.random(20_000, 35_000);
        this.mapWait = mapPvPClan;
        if (this.clan != null) {
            this.clan.map_create = mapPvPClan;
        }
        mapPvPClan.start_map();
        Zone.add_map_plus(mapPvPClan);
        PvpBang.addMap(mapPvPClan);
        this.maps.add(mapPvPClan);
    }

    @Override
    public void join(Player p) throws IOException {
        if (maps != null && !maps.isEmpty()) {
            p.save_previous_map();
            Vgo vgo = new Vgo();
            vgo.map_go = new Zone[]{maps.get(0)};
            vgo.xnew = 530;
            vgo.ynew = 260;
            p.goto_map(vgo);
            p.dungeon = this;
            if (p.getService() != null) {
                p.getService().send_time_cool_down(this.timeState, "Chờ ghép trận", 0);
            }
        }
    }

    public void join(List<Player> list) {
        list.forEach(p0 -> {
            try {
                if (p0 != null) {
                    p0.save_previous_map();
                    this.join(p0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void update(Zone zone) throws java.io.IOException {
        if (this.state == TIME_UP && this.timeState < System.currentTimeMillis()) {
            List<Player> playerList = new ArrayList<>(zone.players);
            playerList.forEach(l -> {
                if (l != null) {
                    try {
                        zone.change_flag(l, -1);
                        l.type_pk = -1;
                        if (l.getService() != null) {
                            l.getService().update_PK(l, false);
                        }
                        l.dungeon = null;
                        if (!l.isBot) {
                            l.return_to_previous_map();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (this.clan != null) this.clan.map_create = null;
            zone.stop_map();
            Zone.remove_map_plus(zone);
            ENTRY.remove(zone);
            zone.pvpBang = null;
            zone.map_dungeon = null;
        }
    }

    public static void addMap(Zone map_dungeon) {
        if (!ENTRY.contains(map_dungeon)) {
            ENTRY.add(map_dungeon);
        }
    }
    
    public static void spawnFriendlyBots(PvpBang pvpBang, int count, short avgLv, Player humanRef) {
        if (pvpBang == null || pvpBang.clan == null || pvpBang.mapWait == null || count <= 0) return;
        long baseHp = (humanRef != null && humanRef.ability != null) ? humanRef.ability.get_hp_max(true) : (avgLv * 250L + 3000);
        if (baseHp <= 0) baseHp = avgLv * 250L + 3000;
        long baseMp = (humanRef != null && humanRef.ability != null) ? humanRef.ability.get_mp_max(true) : (avgLv * 30L + 500);
        if (baseMp <= 0) baseMp = avgLv * 30L + 500;

        for (int bi = 0; bi < count; bi++) {
            try {
                int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                byte botClazz = (byte) ((bi % 5) + 1);
                String botName = bot.botplayer.BotNameGenerator.getRandomBotName();
                bot.BotPVP b = new bot.BotPVP(botId, botName);
                b.isBot = true;
                b.clan = pvpBang.clan;
                b.clazz = botClazz;
                b.level = (short) Math.max(1, avgLv + ZUtil.random(-1, 1));
                b.autoAllocatePotentialPoints(botClazz);
                int botTier = b.level >= 90 ? 4 : (b.level >= 75 ? 3 : (b.level >= 50 ? 2 : 1));
                b.setupBotEquip(botClazz, b.level, botTier, true, true, true);
                b.setupBotSkills(botClazz, (short) Math.max(15, Math.min(30, b.level / 2)), botTier, 0);
                b.setupBotAppearance(botClazz, botTier);
                b.setin4();
                b.init();
                b.ability = new Ability(b);
                b.updateParts();

                // Cân bằng HP/MP theo người chơi (95% - 105%)
                long targetHp = (long) (baseHp * (0.95 + ZUtil.random(0, 10) / 100.0));
                b.hp = (int) Math.max(1000, targetHp);
                b.mp = (int) Math.max(500, baseMp);
                b.isdie = false;

                b.join(pvpBang.mapWait, (short) (480 + bi * 30), (short) (260 + ZUtil.random(-20, 20)));
            } catch (Exception ignored) {}
        }
    }
    
    public static void matchTwoClans(PvpBang pvpBang1, PvpBang pvpBang2) {
        if (pvpBang1 == null || pvpBang2 == null || pvpBang1.clan == null || pvpBang2.clan == null) return;
        if (pvpBang1.state != WAIT_FIND_FIGHT || pvpBang2.state != WAIT_FIND_FIGHT) return;
        
        // Tìm human player mẫu để cân bằng chỉ số
        Player humanRef = null;
        short avgLv = 50;
        int sum = 0, count = 0;
        for (Player p : pvpBang1.mapWait.players) {
            if (p != null && !p.isBot) {
                if (humanRef == null) humanRef = p;
                sum += p.level;
                count++;
            }
        }
        for (Player p : pvpBang2.mapWait.players) {
            if (p != null && !p.isBot) {
                if (humanRef == null) humanRef = p;
                sum += p.level;
                count++;
            }
        }
        if (count > 0) avgLv = (short) Math.max(1, sum / count);

        // Tự động quét và cân bằng số lượng thành viên 2 team:
        int size1 = pvpBang1.mapWait.players.size();
        int size2 = pvpBang2.mapWait.players.size();
        int targetSize = Math.max(3, Math.max(size1, size2));
        if (size1 < targetSize) {
            spawnFriendlyBots(pvpBang1, targetSize - size1, avgLv, humanRef);
        }
        if (size2 < targetSize) {
            spawnFriendlyBots(pvpBang2, targetSize - size2, avgLv, humanRef);
        }

        pvpBang1.setBeginFight_1(pvpBang2);
        pvpBang2.setBeginFight_1(pvpBang1);
        
        Zone mapTemplate = Zone.getMapByID(120)[0];
        Zone mapFight = new Zone();
        mapFight.template = mapTemplate.template;
        mapFight.zone_id = (byte) 0;
        mapFight.list_mob = new int[0];
        mapFight.pvpBangMapFight = new PvpBangMapFight();
        mapFight.pvpBangMapFight.clan1 = pvpBang1.clan;
        mapFight.pvpBangMapFight.clan2 = pvpBang2.clan;
        mapFight.pvpBangMapFight.mapWaitClan1 = pvpBang1.mapWait;
        mapFight.pvpBangMapFight.mapWaitClan2 = pvpBang2.mapWait;
        mapFight.pvpBangMapFight.pvpBang1 = pvpBang1;
        mapFight.pvpBangMapFight.pvpBang2 = pvpBang2;
        mapFight.pvpBangMapFight.isClan2Bot = false;
        mapFight.map_dungeon = mapFight.pvpBangMapFight;
        Zone.add_map_plus(mapFight);
        
        pvpBang1.mapFight = mapFight;
        pvpBang2.mapFight = mapFight;
        if (!pvpBang1.maps.contains(mapFight)) pvpBang1.maps.add(mapFight);
        if (!pvpBang2.maps.contains(mapFight)) pvpBang2.maps.add(mapFight);
        
        Message m33 = new Message(-73);
        try {
            m33.writer().writeByte(4);
            m33.writer().writeShort(15);
            m33.writer().writeByte(pvpBang1.win);
            m33.writer().writeByte(pvpBang1.lose);
            pvpBang1.mapWait.send_msg_all_p(m33, null, true);
            m33.cleanup();
            m33 = new Message(-73);
            m33.writer().writeByte(4);
            m33.writer().writeShort(15);
            m33.writer().writeByte(pvpBang2.win);
            m33.writer().writeByte(pvpBang2.lose);
            pvpBang2.mapWait.send_msg_all_p(m33, null, true);
            m33.cleanup();
        } catch (Exception ignored) {}
        
        PvpBang.updateTicket(pvpBang1.clan);
        PvpBang.updateTicket(pvpBang2.clan);
    }

    public static void matchWithBotClan(PvpBang pvpBang1) {
        if (pvpBang1 == null || pvpBang1.clan == null || pvpBang1.mapWait == null) return;
        if (pvpBang1.state != WAIT_FIND_FIGHT) return;

        // Tìm human player mẫu để cân bằng chỉ số
        Player humanRef = null;
        short avgLv = 50;
        int sum = 0, count = 0;
        for (Player p : pvpBang1.mapWait.players) {
            if (p != null && !p.isBot) {
                if (humanRef == null) humanRef = p;
                sum += p.level;
                count++;
            }
        }
        if (count > 0) avgLv = (short) Math.max(1, sum / count);

        // Tự động quét và cân bằng số lượng thành viên:
        int currentMembers = pvpBang1.mapWait.players.size();
        int targetTeamSize = Math.max(3, Math.min(5, currentMembers));
        int neededFriendlyBots = targetTeamSize - currentMembers;
        if (neededFriendlyBots > 0) {
            spawnFriendlyBots(pvpBang1, neededFriendlyBots, avgLv, humanRef);
        }

        Clan botOpponentClan = new Clan();
        botOpponentClan.id = (short) (-888 - ZUtil.random(1000));
        botOpponentClan.name = bot.botplayer.BotNameGenerator.generateRandomClanName();
        botOpponentClan.icon = (short) ZUtil.random(1, 10);
        botOpponentClan.level = (short) Math.max(1, (humanRef != null && humanRef.clan != null && humanRef.clan.level > 0) ? humanRef.clan.level : Math.max(1, avgLv / 8));
        botOpponentClan.trungsinh = (byte) ((humanRef != null && humanRef.clan != null) ? humanRef.clan.trungsinh : Math.min(6, avgLv / 20));
        botOpponentClan.members = new ArrayList<>();
        clan.ClanMember bLeader = new clan.ClanMember();
        bLeader.id = botOpponentClan.id;
        bLeader.name = "ThuyềnTrưởng_" + botOpponentClan.name.replace(" ", "");
        bLeader.level = avgLv;
        bLeader.levelInclan = 0;
        botOpponentClan.members.add(bLeader);

        PvpBang pvpBang2 = new PvpBang(botOpponentClan);
        pvpBang2.isBotClan = true;
        pvpBang2.create();

        long baseHp = (humanRef != null && humanRef.ability != null) ? humanRef.ability.get_hp_max(true) : (avgLv * 250L + 3000);
        if (baseHp <= 0) baseHp = avgLv * 250L + 3000;
        long baseMp = (humanRef != null && humanRef.ability != null) ? humanRef.ability.get_mp_max(true) : (avgLv * 30L + 500);
        if (baseMp <= 0) baseMp = avgLv * 30L + 500;

        // Sinh số lượng bot Clan 2 bằng đúng số lượng thành viên Clan 1 (Cân bằng tuyệt đối 3v3, 4v4, 5v5)
        for (int bi = 0; bi < targetTeamSize; bi++) {
            try {
                int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                byte botClazz = (byte) ((bi % 5) + 1);
                String botName = bot.botplayer.BotNameGenerator.getRandomBotName();
                bot.BotPVP b = new bot.BotPVP(botId, botName);
                b.isBot = true;
                b.clan = botOpponentClan;
                b.clazz = botClazz;
                b.level = (short) Math.max(1, avgLv + ZUtil.random(-1, 1));
                b.autoAllocatePotentialPoints(botClazz);
                int botTier = b.level >= 90 ? 4 : (b.level >= 75 ? 3 : (b.level >= 50 ? 2 : 1));
                b.setupBotEquip(botClazz, b.level, botTier, true, true, true);
                b.setupBotSkills(botClazz, (short) Math.max(15, Math.min(30, b.level / 2)), botTier, 0);
                b.setupBotAppearance(botClazz, botTier);
                b.setin4();
                b.init();
                b.ability = new Ability(b);
                b.updateParts();

                // Cân bằng HP/MP theo người chơi (95% - 105%)
                long targetHp = (long) (baseHp * (0.95 + ZUtil.random(0, 10) / 100.0));
                b.hp = (int) Math.max(1000, targetHp);
                b.mp = (int) Math.max(500, baseMp);
                b.isdie = false;

                b.join(pvpBang2.mapWait, (short) (480 + bi * 30), (short) (260 + ZUtil.random(-20, 20)));
            } catch (Exception ignored) {}
        }

        pvpBang1.setBeginFight_1(pvpBang2);
        pvpBang2.setBeginFight_1(pvpBang1);

        Zone mapTemplate = Zone.getMapByID(120)[0];
        Zone mapFight = new Zone();
        mapFight.template = mapTemplate.template;
        mapFight.zone_id = (byte) 0;
        mapFight.list_mob = new int[0];
        mapFight.pvpBangMapFight = new PvpBangMapFight();
        mapFight.pvpBangMapFight.clan1 = pvpBang1.clan;
        mapFight.pvpBangMapFight.clan2 = botOpponentClan;
        mapFight.pvpBangMapFight.mapWaitClan1 = pvpBang1.mapWait;
        mapFight.pvpBangMapFight.mapWaitClan2 = pvpBang2.mapWait;
        mapFight.pvpBangMapFight.pvpBang1 = pvpBang1;
        mapFight.pvpBangMapFight.pvpBang2 = pvpBang2;
        mapFight.pvpBangMapFight.isClan2Bot = true;
        mapFight.map_dungeon = mapFight.pvpBangMapFight;
        Zone.add_map_plus(mapFight);

        pvpBang1.mapFight = mapFight;
        pvpBang2.mapFight = mapFight;
        if (!pvpBang1.maps.contains(mapFight)) pvpBang1.maps.add(mapFight);
        if (!pvpBang2.maps.contains(mapFight)) pvpBang2.maps.add(mapFight);

        try {
            Message m33 = new Message(-73);
            m33.writer().writeByte(4);
            m33.writer().writeShort(15);
            m33.writer().writeByte(pvpBang1.win);
            m33.writer().writeByte(pvpBang1.lose);
            pvpBang1.mapWait.send_msg_all_p(m33, null, true);
            m33.cleanup();
        } catch (Exception ignored) {}

        PvpBang.updateTicket(pvpBang1.clan);
    }

    public static synchronized boolean triggerQuickMatch(Player p) {
        if (p == null || p.clan == null) return false;
        Zone currentZone = p.map;
        PvpBang pvpBang = (currentZone != null && currentZone.pvpBang != null) ? currentZone.pvpBang : null;
        if (pvpBang == null && p.clan.map_create != null) {
            pvpBang = p.clan.map_create.pvpBang;
        }
        if (pvpBang == null || pvpBang.state != WAIT_FIND_FIGHT) {
            return false;
        }

        PvpBang opponent = null;
        for (Zone z : ENTRY) {
            if (z != null && z.pvpBang != null && z.pvpBang != pvpBang 
                    && z.pvpBang.state == WAIT_FIND_FIGHT && z.pvpBang.clan != null 
                    && !z.pvpBang.clan.equals(pvpBang.clan)) {
                opponent = z.pvpBang;
                break;
            }
        }

        if (opponent != null) {
            matchTwoClans(pvpBang, opponent);
        } else {
            matchWithBotClan(pvpBang);
        }
        return true;
    }

    public void setBeginFight_1(PvpBang otherClan) {
        state = WAIT_TO_BEGIN;
        timeState = System.currentTimeMillis() + 14_500;
        mapOpponent = new HashMap<>();
        win = 0;
        lose = 0;
        String oppName = (otherClan != null && otherClan.clan != null) ? otherClan.clan.name : "Đối thủ";
        notice(mapWait, "Ghép thành công với băng " + oppName + "! Trận đấu sẽ bắt đầu sau 15 giây.");
        if (mapWait != null) {
            for (Player p : mapWait.players) {
                if (p != null && p.conn != null) {
                    try {
                        p.getService().send_time_cool_down(timeState, "Chuẩn bị chiến đấu", 0);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    public synchronized static void update() {
        try {
            List<Zone> listMapNotEnoughTurn = new ArrayList<>();
            List<Zone> listMapStartFight = new ArrayList<>();
            List<Zone> listMapFinished = new ArrayList<>();
            List<Zone> listWaitingReal = new ArrayList<>();

            for (Zone waitZone : ENTRY) {
                if (waitZone == null || waitZone.pvpBang == null || waitZone.pvpBang.clan == null) {
                    continue;
                }
                int ticket = PvpBang.getTicket(waitZone.pvpBang.clan);
                if (waitZone.pvpBang.state == WAIT_FIND_FIGHT) {
                    if (ticket >= 5) {
                        listMapNotEnoughTurn.add(waitZone);
                    } else if (!waitZone.pvpBang.isBotClan) {
                        listWaitingReal.add(waitZone);
                    }
                } 
                else if (waitZone.pvpBang.state == FIGHTING && waitZone.pvpBang.timeState < System.currentTimeMillis()) {
                    waitZone.pvpBang.state = TIME_UP;
                } 
                else if (waitZone.pvpBang.state == TIME_UP) {
                    listMapFinished.add(waitZone);
                } 
                else if (waitZone.pvpBang.state == WAIT_TO_BEGIN && waitZone.pvpBang.timeState < System.currentTimeMillis() && waitZone.pvpBang.mapFight != null) {
                    listMapStartFight.add(waitZone);
                }
            }

            // 1. Ưu tiên ghép giữa 2 clan người thật với nhau trước
            while (listWaitingReal.size() >= 2) {
                Zone waitZone1 = listWaitingReal.remove(0);
                Zone waitZone2 = listWaitingReal.remove(0);
                if (waitZone1 != null && waitZone1.pvpBang != null && waitZone2 != null && waitZone2.pvpBang != null) {
                    matchTwoClans(waitZone1.pvpBang, waitZone2.pvpBang);
                }
            }

            // 2. Nếu chỉ có 1 clan đang chờ: chỉ khi hết thời gian chờ ngẫu nhiên mới ghép với Bot Clan
            for (Zone waitZone : listWaitingReal) {
                if (waitZone != null && waitZone.pvpBang != null && waitZone.pvpBang.state == WAIT_FIND_FIGHT
                        && waitZone.pvpBang.timeState < System.currentTimeMillis()) {
                    matchWithBotClan(waitZone.pvpBang);
                }
            }

            // 3. Bắt đầu trận đấu
            Set<Zone> mapToStart = new HashSet<>();
            for (Zone map : listMapStartFight) {
                if (map == null || map.pvpBang == null || map.pvpBang.mapFight == null) continue;
                if (map.pvpBang.state != WAIT_TO_BEGIN) continue;

                Zone fightMap = map.pvpBang.mapFight;
                short mapW = (fightMap.template != null && fightMap.template.maxW > 0)
                        ? fightMap.template.maxW : 1000;
                boolean isClan1 = (fightMap.pvpBangMapFight != null && fightMap.pvpBangMapFight.clan1 != null 
                        && fightMap.pvpBangMapFight.clan1.equals(map.pvpBang.clan));
                short spawnStartX = isClan1 ? (short) 220 : (short) Math.max(500, mapW - 220);
                byte teamFlag = (byte) (isClan1 ? 4 : 5);

                Vgo vgo = new Vgo();
                vgo.map_go = new Zone[1];
                vgo.map_go[0] = fightMap;
                vgo.xnew = spawnStartX;
                vgo.ynew = 250;

                List<Player> listPlayer = new ArrayList<>();
                List<bot.Bot> listBot = new ArrayList<>();
                for (Player p : map.players) {
                    if (p != null) {
                        if (p.isBot && p instanceof bot.Bot) {
                            listBot.add((bot.Bot) p);
                        } else {
                            listPlayer.add(p);
                        }
                    }
                }

                // Chuyển người chơi thật sang mapFight
                listPlayer.forEach(l -> {
                    try {
                        l.type_pk = teamFlag;
                        l.goto_map(vgo);
                        if (l.map != null) {
                            l.map.change_flag(l, teamFlag);
                        }
                    } catch (IOException ignored) {}
                });

                // Chuyển bot thủ công sang mapFight
                for (int bi = 0; bi < listBot.size(); bi++) {
                    bot.Bot b = listBot.get(bi);
                    try {
                        if (b.map != null) {
                            b.map.remove_obj(b.index_map, 0);
                            b.map.leave_map(b, 0);
                        }
                        b.type_pk = teamFlag;
                        short botPosX = isClan1 ? (short) (spawnStartX + (bi + 1) * 35) : (short) (spawnStartX - (bi + 1) * 35);
                        b.join(fightMap, botPosX, (short) (250 + ZUtil.random(-15, 15)));
                        if (fightMap != null) {
                            fightMap.change_flag(b, teamFlag);
                        }
                    } catch (Exception ignored) {}
                }

                mapToStart.add(fightMap);
                map.pvpBang.state = FIGHTING;
                map.pvpBang.timeState = System.currentTimeMillis() + 60_000 * 3 + 12_000L;
                
                // Chỉ kích hoạt startFightCountdown nếu chưa kích hoạt
                if (fightMap.pvpBangMapFight != null && fightMap.pvpBangMapFight.status_pvp == -1) {
                    fightMap.pvpBangMapFight.time_pvp = 3;
                    fightMap.pvpBangMapFight.startFightCountdown();
                }

                for (Player pFight : fightMap.players) {
                    if (pFight != null && pFight.conn != null && pFight.getService() != null) {
                        pFight.getService().send_time_cool_down(map.pvpBang.timeState, "Thời gian thi đấu", 0);
                    }
                }
            }
            mapToStart.forEach(s -> {
                if (s != null) s.start_map();
            });

            // 4. Xử lý map hết lượt
            for (Zone map : listMapNotEnoughTurn) {
                if (map != null && map.pvpBang != null && map.pvpBang.timeState < System.currentTimeMillis()) {
                    map.pvpBang.timeState = System.currentTimeMillis() + 15_000;
                    PvpBang.notice(map, "Hôm nay đã hoàn thành 5/5 lượt tham gia PvP Băng! Phòng chờ sẽ đóng sau 15 giây.");
                    map.pvpBang.state = TIME_UP;
                }
            }

            // 5. Dọn dẹp phòng đã kết thúc
            List<Zone> listRemove = new ArrayList<>();
            for (Zone map : listMapFinished) {
                if (map != null && map.pvpBang != null && map.pvpBang.state == TIME_UP 
                        && map.pvpBang.timeState < System.currentTimeMillis()) {
                    List<Player> playerList = new ArrayList<>(map.players);
                    playerList.forEach(l -> {
                        if (l != null) {
                            try {
                                l.isdie = false;
                                if (l.ability != null) {
                                    l.hp = (int) Math.max(1000, l.ability.get_hp_max(true));
                                    l.mp = (int) Math.max(500, l.ability.get_mp_max(true));
                                }
                                map.change_flag(l, -1);
                                l.dungeon = null;
                                if (!l.isBot) {
                                    l.return_to_previous_map();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    if (map.pvpBang.clan != null) map.pvpBang.clan.map_create = null;
                    map.stop_map();
                    Zone.remove_map_plus(map);
                    listRemove.add(map);
                }
            }
            ENTRY.removeAll(listRemove);
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void buyTicket(Clan clan) {
        if (clan == null) return;
        getTicket(clan);
        int oldValue = PvpBang.mapClanTicket.get(clan);
        PvpBang.mapClanTicket.put(clan, Math.max(0, oldValue - 1));
    }

    public static void updateTicket(Clan clan) {
        if (clan == null) return;
        int oldValue = getTicket(clan);
        PvpBang.mapClanTicket.put(clan, oldValue + 1);
    }

    public static int getTicket(Clan clan) {
        if (clan == null) return 0;
        return PvpBang.mapClanTicket.computeIfAbsent(clan, k -> 0);
    }

    public static void notice(Zone map, String notice) {
        if (map == null || notice == null) return;
        try {
            Message m = new Message(-31);
            m.writer().writeByte(0);
            m.writer().writeUTF(notice);
            m.writer().writeByte(0);
            m.writer().writeShort(-1);
            map.send_msg_all_p(m, null, true);
            m.cleanup();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendChatNpc() throws IOException {
        Message m3 = new Message(17);
        m3.writer().writeShort(-84);
        m3.writer().writeByte(2);
        m3.writer().writeUTF("Phó bản PvP Băng Hải Tặc mở cả ngày, mỗi ngày 5 lượt thi đấu giáp lá cà nhận Beri, Ruby và XP Bang cực khủng!");
        Zone[] mapArrSelect = Zone.getMapByID(33);
        if (mapArrSelect != null) {
            for (int i = 0; i < mapArrSelect.length; i++) {
                if (mapArrSelect[i] != null) {
                    mapArrSelect[i].send_msg_all_p(m3, null, true);
                }
            }
        }
        m3.cleanup();
    }
}
