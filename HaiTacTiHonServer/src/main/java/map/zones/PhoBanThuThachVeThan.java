package map.zones;

import model.Player;
import model.Quest;
import core.ZUtil;
import map.Zone;
import mob.Mob;
import network.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashMap;
import java.util.List;
import template.GiftBox;
import template.ItemTemplate4;
import template.ItemTemplate7;

public class PhoBanThuThachVeThan extends zabstracts.AbsEventDungeon implements zinterfaces.iMob {

    public long time_state;
    public boolean isFinish = false;
    public boolean isReceiv = false;
    private byte[] listcheck = new byte[]{0, 0};
    public int type;
    public HashMap<Mob, Long> listRemoveMap;
    public boolean gifted;
    public int levelMob;
    public int hpScale;
    public int mapId;

    public static PhoBanThuThachVeThan createDungeon(List<Player> listP) {
        String creator = (listP != null && !listP.isEmpty() && listP.get(0) != null) ? listP.get(0).name : "Unknown";
        int levelMob = -1;
        long maxDame = 0;
        long totalDame = 0;
        for (Player p0 : listP) {
            if (p0 == null) continue;
            if (p0.isBot || p0.isDe || p0 instanceof model.DeTu || p0 instanceof bot.mercenary.MercenaryBot) {
                long dame = (p0.ability != null) ? p0.ability.get_dame(true) : 1000L;
                totalDame += dame;
                maxDame = Math.max(maxDame, dame);
                continue;
            }
            try {
                p0.update_key_boss(-1);
                p0.updateMoney();
                if (p0.getService() != null) {
                    p0.getService().CountDown_Ticket();
                }
                p0.ttvtMax--;
                p0.tableTickOption = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            levelMob = Math.max(levelMob, p0.level);
            long dame = (p0.ability != null) ? p0.ability.get_dame(true) : 1000L;
            if (p0.ability != null) {
                dame = (dame * p0.ability.get_dame_devil_percent()) / 100;
            }
            totalDame += dame;
            maxDame = Math.max(maxDame, dame);
        }
        long effectiveScale = Math.max(maxDame, (long) (maxDame + (totalDame - maxDame) * 0.5));
        if (effectiveScale < 1000) effectiveScale = 1000;
        int hpScale = (int) Math.min(effectiveScale, Integer.MAX_VALUE);
        PhoBanThuThachVeThan dungeon = new PhoBanThuThachVeThan(levelMob, hpScale, 0, 984, 60000L);
        dungeon.create();
        database.DungeonSessionCache.gI().registerInstance(dungeon, creator, listP, 984, dungeon.time_state, levelMob, hpScale, (byte) 0, 0);
        return dungeon;
    }

    public PhoBanThuThachVeThan() {
        this.listRemoveMap = new HashMap<>();
    }

    public PhoBanThuThachVeThan(int levelMob, int hpScale, int type, int mapId, long timeStateOffset) {
        this.levelMob = levelMob;
        this.hpScale = hpScale;
        this.type = type;
        this.mapId = mapId;
        this.time_state = System.currentTimeMillis() + timeStateOffset;
        this.listRemoveMap = new HashMap<>();
    }

    public boolean okP(Player p) {
        if (p == null || p.map == null) {
            return true;
        }
        List<Player> realPlayers = new ArrayList<>();
        for (int i = 0; i < p.map.players.size(); i++) {
            Player p0 = p.map.players.get(i);
            if (!Red_Line.isBotPlayer(p0)) {
                realPlayers.add(p0);
            }
        }
        if (realPlayers.size() <= 1) {
            return true;
        }
        int index = realPlayers.indexOf(p);
        if (index >= 0 && index < listcheck.length) {
            listcheck[index] = 1;
        } else {
            listcheck[0] = 1;
        }
        for (int i = 0; i < Math.min(realPlayers.size(), listcheck.length); i++) {
            if (listcheck[i] == 0) {
                return false;
            }
        }
        return true;
    }

    public void update_okP() {
        listcheck = new byte[]{0, 0};
    }

    @Override
    public void create() {
        this.gifted = false;
        this.time = this.time_state;
        this.mobs = new CopyOnWriteArrayList<>();
        this.maps = new CopyOnWriteArrayList<>();

        Zone mapTemplate = Zone.getMapByID(this.mapId)[0];
        Zone map_boss = new Zone();
        map_boss.template = mapTemplate.template;
        map_boss.zone_id = (byte) 0;
        map_boss.list_mob = new int[0];
        map_boss.map_ThuThachVeThan = this;
        map_boss.map_dungeon = this;

        if (this.type == 2) {
            int index_mob = -2;
            int floor = Math.max(1, this.mapId - 912); // Tầng 1 -> 5 (map 913 -> 917)
            for (int i = 0; i < mapTemplate.list_mob.length; i++) {
                Mob temp = mapTemplate.getMob(mapTemplate.list_mob[i]);
                if (temp == null || temp.mtemplate == null) continue;
                Mob mob_add = new Mob();
                mob_add.mtemplate = temp.mtemplate;
                mob_add.x = temp.x;
                mob_add.y = temp.y;
                
                // Chuẩn hóa HP Vệ Thần (120s): Nerf chuẩn, tăng nhẹ dần theo tầng
                long baseMultiplier = 3L + (long) (floor - 1) * 2L;
                long hpNow = (long) Math.max(this.hpScale, 1000) * baseMultiplier;
                long minFloorHp = 40_000L + (long) (floor - 1) * 25_000L;
                hpNow = Math.max(hpNow, minFloorHp);
                
                mob_add.hp_max = (int) Math.min(hpNow, Integer.MAX_VALUE);
                mob_add.hp = mob_add.hp_max;
                mob_add.level = this.levelMob;
                mob_add.isdie = false;
                mob_add.id_target = -1;
                mob_add.index = index_mob--;
                mob_add.map = map_boss;
                mob_add.is_boss = true;
                mob_add.boss_inf = null;
                mob_add.iMob = this;
                this.mobs.add(mob_add);
                map_boss.mobs.put(mob_add.index, mob_add);
            }
        }
        map_boss.start_map();
        Zone.add_map_plus(map_boss);
        this.maps.add(map_boss);
    }

    @Override
    public void join(Player p) throws IOException {
        if (maps != null && !maps.isEmpty()) {
            p.save_previous_map();
            p.map = maps.get(0);
            p.x = 300;
            p.y = 200;
            p.xold = p.x;
            p.yold = p.y;
            p.map.goto_map(p);
            p.addDungeon(this);
            p.time_key_red_line = 1;
            p.getService().send_time_cool_down(this.time_state, this.type == 0 ? "Phòng chờ" : "Tầng " + (this.mapId - 912), 0);
            p.getService().update_PK(p, true);
            p.getService().pet(p, true);
            Quest.update_map_have_side_quest(p, true);

            if (this.type == 0 && !Red_Line.isBotPlayer(p)) {
                Red_Line.init_key_TTVT(p);
            }
        }
    }

    public void join(List<Player> list) {
        if (list == null) return;
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

    @Override
    public void update(Zone zone) throws IOException {
        database.DungeonSessionCache.gI().updateInstanceState(this);
        if (this.type == 2) {
            try {
                List<Mob> toRemove = new ArrayList<>();
                for (java.util.Map.Entry<Mob, Long> en : this.listRemoveMap.entrySet()) {
                    if (en.getValue() < System.currentTimeMillis()) {
                        zone.remove_obj(en.getKey().index, 1);
                        toRemove.add(en.getKey());
                    }
                }
                toRemove.forEach(m -> this.listRemoveMap.remove(m));
            } catch (Exception e) {
            }
            if (this.time_state < System.currentTimeMillis()) {
                zone.setRunning(false);
                if (zone.template.id == 917 || !this.gifted) {
                    List<Player> playerList = new ArrayList<>(zone.players);
                    notifyDungeonEnd();
                    playerList.forEach(l -> {
                        if (l != null) {
                            l.dungeon = null;
                            l.return_to_previous_map();
                        }
                    });
                } else {
                    List<Player> listP = new ArrayList<>(zone.players);
                    PhoBanThuThachVeThan nextDungeon = new PhoBanThuThachVeThan(this.levelMob, this.hpScale, 2, zone.template.id + 1, 60_000L * 2);
                    nextDungeon.instanceId = this.instanceId;
                    nextDungeon.creatorName = this.creatorName;
                    nextDungeon.participantNames = this.participantNames;
                    nextDungeon.create();
                    database.DungeonSessionCache.gI().registerInstance(nextDungeon, this.creatorName, listP, zone.template.id + 1, nextDungeon.time_state, this.levelMob, this.hpScale, (byte) 0, 2);
                    listP.forEach(p0 -> p0.removeDungeon(PhoBanThuThachVeThan.this));
                    nextDungeon.join(listP);
                }
            } else {
                if (!this.gifted && this.mobs.isEmpty()) {
                    this.gifted = true;
                    this.time_state = System.currentTimeMillis() + 6000L;
                    List<GiftBox> listGift = new ArrayList<>();
                    {
                        GiftBox gb_ = new GiftBox();
                        ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(451);
                        gb_.id = it4.id;
                        gb_.type = 4;
                        gb_.name = it4.name;
                        gb_.icon = it4.icon;
                        gb_.num = ZUtil.random(4, 25);
                        gb_.color = 0;
                        listGift.add(gb_);
                    }
                    {
                        GiftBox gb_ = new GiftBox();
                        ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(454);
                        gb_.id = it4.id;
                        gb_.type = 4;
                        gb_.name = it4.name;
                        gb_.icon = it4.icon;
                        gb_.num = ZUtil.random(4, 25);
                        gb_.color = 0;
                        listGift.add(gb_);
                    }
                    {
                        GiftBox gb_ = new GiftBox();
                        ItemTemplate7 it4 = ItemTemplate7.get_it_by_id(13);
                        gb_.id = it4.id;
                        gb_.type = 7;
                        gb_.name = it4.name;
                        gb_.icon = it4.icon;
                        gb_.num = ZUtil.random(10, 20);
                        gb_.color = 0;
                        listGift.add(gb_);
                    }
                    {
                        GiftBox gb = new GiftBox();
                        ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);
                        gb.id = it_temp4.id;
                        gb.type = 4;
                        gb.name = it_temp4.name;
                        gb.icon = it_temp4.icon;
                        gb.num = ZUtil.random(100000, 300000);
                        gb.color = 0;
                        listGift.add(gb);
                    }
                    int floorNumber = zone.template.id - 912;
                    event.EventManager.dispatchMapDrop(zone, listGift, floorNumber);
                    for (int i = 0; i < zone.players.size(); i++) {
                        Player p0 = zone.players.get(i);
                        if (p0 == null || p0.isBot || p0.conn == null || Red_Line.isBotPlayer(p0)) continue;
                        p0.getService().send_time_cool_down(this.time_state, "Tầng " + floorNumber, 0);
                        core.RewardService.sendGiftOrMail(p0, 1, "Thử thách vệ thần",
                                "Hoàn thành tầng " + floorNumber, listGift, true);
                    }
                }
            }
        } else if (this.type == 0) {
            if (this.time_state < System.currentTimeMillis()) {
                List<Player> playerList = new ArrayList<>(zone.players);
                int levelMob = -1;
                long maxDame = 0;
                long totalDame = 0;

                if (this.isReceiv && this.isFinish) {
                    for (int i = 0; i < zone.players.size(); i++) {
                        Player p0 = zone.players.get(i);
                        if (p0 != null) {
                            p0.key_red_line.clear();
                            levelMob = Math.max(levelMob, p0.level);
                            long dame = (p0.ability != null) ? p0.ability.get_dame(true) : 1000L;
                            if (p0.ability != null) {
                                dame = (dame * p0.ability.get_dame_devil_percent()) / 100;
                            }
                            totalDame += dame;
                            maxDame = Math.max(maxDame, dame);
                        }
                    }
                    long effectiveScale = Math.max(maxDame, (long) (maxDame + (totalDame - maxDame) * 0.5));
                    if (effectiveScale < 1000) effectiveScale = 1000;
                    int hpScale = (int) Math.min(effectiveScale, Integer.MAX_VALUE);
                    Zone[] nextCheck = Zone.getMapByID(913);
                    if (nextCheck != null && nextCheck.length > 0 && nextCheck[0] != null) {
                        PhoBanThuThachVeThan nextDungeon = new PhoBanThuThachVeThan(levelMob, hpScale, 2, 913, 60_000L * 2);
                        nextDungeon.instanceId = this.instanceId;
                        nextDungeon.creatorName = this.creatorName;
                        nextDungeon.participantNames = this.participantNames;
                        nextDungeon.create();
                        database.DungeonSessionCache.gI().registerInstance(nextDungeon, this.creatorName, playerList, 913, nextDungeon.time_state, levelMob, hpScale, (byte) 0, 2);
                        playerList.forEach(p0 -> p0.removeDungeon(PhoBanThuThachVeThan.this));
                        nextDungeon.join(playerList);
                    } else {
                        notifyDungeonEnd();
                        playerList.forEach(l -> {
                            if (l != null) {
                                l.dungeon = null;
                                l.return_to_previous_map();
                            }
                        });
                    }
                } else {
                    notifyDungeonEnd();
                    playerList.forEach(l -> {
                        if (l != null) {
                            l.dungeon = null;
                            l.return_to_previous_map();
                        }
                    });
                }
                zone.setRunning(false);
            }
        }
    }

    @Override
    public void onDeath(Player pKill, Mob mob) {
        try {
            if (pKill != null) {
                pKill = pKill.getOwnerPlayer();
            }
            if (pKill != null && Math.abs(pKill.level - mob.level) <= 10) {
                if (15 > ZUtil.random(120)) {
                    map.LeaveItemMap.leave_item4(mob.map, mob, pKill);
                } else if (15 > ZUtil.random(120)) {
                    map.LeaveItemMap.leave_item7(mob.map, mob, pKill);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mobs.remove(mob);
        mob.map.mobs.remove(mob.index);
        this.listRemoveMap.put(mob, System.currentTimeMillis() + 1_500);
        database.DungeonSessionCache.gI().updateInstanceState(this);
    }

    @Override
    public void update(Mob mob) {
    }
}
