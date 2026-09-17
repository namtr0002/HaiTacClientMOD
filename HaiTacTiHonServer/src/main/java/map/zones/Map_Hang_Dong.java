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

public class Map_Hang_Dong extends zabstracts.AbsClanDungeon implements zinterfaces.iMob {

    public boolean gifted;
    public HashMap<Mob, Long> listRemoveMap;
    public int levelMob;
    public int level;
    public int hpScale;

    public static Map_Hang_Dong createDungeon(List<Player> listP) {
        int levelMob = -1;
        long maxDame = 0;
        long totalDame = 0;
        String creator = (listP != null && !listP.isEmpty() && listP.get(0) != null) ? listP.get(0).name : "Unknown";
        for (Player p0 : listP) {
            if (p0 == null) continue;
            if (!p0.isBot && !p0.isDe && !(p0 instanceof model.DeTu) && !(p0 instanceof bot.mercenary.MercenaryBot)) {
                try {
                    p0.update_key_boss(-10);
                    p0.updateMoney();
                    if (p0.getService() != null) {
                        p0.getService().CountDown_Ticket();
                    }
                    p0.update_hd_max(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        Map_Hang_Dong dungeon = new Map_Hang_Dong(levelMob, hpScale, 1);
        dungeon.create();
        database.DungeonSessionCache.gI().registerInstance(dungeon, creator, listP, 301, dungeon.time, levelMob, hpScale, (byte) 0, 1);
        return dungeon;
    }

    public Map_Hang_Dong() {
        this.listRemoveMap = new HashMap<>();
    }

    public Map_Hang_Dong(int levelMob, int hpScale, int level) {
        this.levelMob = levelMob;
        this.hpScale = hpScale;
        this.level = level;
        this.listRemoveMap = new HashMap<>();
    }

    @Override
    public void create() {
        this.time = System.currentTimeMillis() + 90_000L; // 90 giây mỗi tầng
        this.gifted = false;
        this.mobs = new CopyOnWriteArrayList<>();
        this.maps = new CopyOnWriteArrayList<>();

        Zone mapTemplate = Zone.getMapByID(301)[0];
        Zone map_boss = new Zone();
        map_boss.template = mapTemplate.template;
        map_boss.zone_id = (byte) 0;
        map_boss.list_mob = new int[0];
        map_boss.map_Hang = this;
        map_boss.map_dungeon = this;

        int index_mob = -2;
        for (int i = 0; i < mapTemplate.list_mob.length; i++) {
            Mob temp = mapTemplate.getMob(mapTemplate.list_mob[i]);
            if (temp == null || temp.mtemplate == null) continue;
            Mob mob_add = new Mob();
            mob_add.mtemplate = temp.mtemplate;
            mob_add.x = temp.x;
            mob_add.y = temp.y;
            
            // Chuẩn hóa độ khó Hang Động (90s): Nerf chuẩn, tăng độ khó nhẹ nhàng theo tầng
            long baseMultiplier = 2L + (long) ((this.level - 1) / 10);
            long hpNow = (long) Math.max(this.hpScale, 1000) * baseMultiplier;
            long minHp = 20_000L + (long) (this.level - 1) * 3_000L;
            hpNow = Math.max(hpNow, minHp);
            
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
        map_boss.start_map();
        Zone.add_map_plus(map_boss);
        this.maps.add(map_boss);
    }

    @Override
    public void join(Player p) throws IOException {
        if (maps != null && !maps.isEmpty()) {
            p.save_previous_map();
            p.map = maps.get(0);
            p.x = 100;
            p.y = 300;
            p.xold = p.x;
            p.yold = p.y;
            p.map.goto_map(p);
            p.addDungeon(this);
            p.getService().send_time_cool_down(this.time, "Hang " + this.level, 0);
            p.getService().update_PK(p, true);
            p.getService().pet(p, true);
            Quest.update_map_have_side_quest(p, true);
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
        if (this.time < System.currentTimeMillis()) {
            if (this.level == 50 || !this.gifted) {
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
                Map_Hang_Dong nextDungeon = new Map_Hang_Dong(this.levelMob, this.hpScale, this.level + 1);
                nextDungeon.instanceId = this.instanceId;
                nextDungeon.creatorName = this.creatorName;
                nextDungeon.participantNames = this.participantNames;
                nextDungeon.create();
                database.DungeonSessionCache.gI().registerInstance(nextDungeon, this.creatorName, listP, 301, nextDungeon.time, this.levelMob, this.hpScale, (byte) 0, nextDungeon.level);
                listP.forEach(p0 -> p0.removeDungeon(Map_Hang_Dong.this));
                nextDungeon.join(listP);
            }
            zone.setRunning(false);
        } else {
            if (!this.gifted && this.mobs.isEmpty()) {
                this.gifted = true;
                this.time = System.currentTimeMillis() + 6000L;
                List<GiftBox> listGift = new ArrayList<>();
                
                // 1. Phân bổ Beri theo độ sâu tầng hang (Hardcore Nerf - cày khó)
                int beriNum;
                if (this.level <= 10) {
                    beriNum = ZUtil.random(1_000, 3_000);
                } else if (this.level <= 25) {
                    beriNum = ZUtil.random(3_000, 6_000);
                } else if (this.level <= 40) {
                    beriNum = ZUtil.random(6_000, 12_000);
                } else if (this.level < 50) {
                    beriNum = ZUtil.random(12_000, 20_000);
                } else {
                    // Boss cuối tầng 50
                    beriNum = 100_000;
                }
                GiftBox.addGift(listGift, 4, 0, beriNum);

                // 2. Thưởng Ruby đặc biệt tại tầng 50
                if (this.level == 50) {
                    GiftBox.addGift(listGift, 4, 1, 10); // 10 Ruby
                }

                // 3. Đá khảm (ItemTemplate7 id 10)
                if (this.level == 50) {
                    GiftBox.addGift(listGift, 7, 10, 1);
                } else if (5 > ZUtil.random(120)) {
                    GiftBox.addGift(listGift, 7, 10, 1);
                }

                // 4. Thẻ Bài Hang Động (ItemTemplate4 id 841) theo các mốc
                if (this.level == 50) {
                    GiftBox.addGift(listGift, 4, 841, 10);
                } else if (this.level >= 40) {
                    if (30 > ZUtil.random(100)) {
                        GiftBox.addGift(listGift, 4, 841, 5);
                    }
                } else if (this.level >= 30) {
                    if (25 > ZUtil.random(100)) {
                        GiftBox.addGift(listGift, 4, 841, 3);
                    }
                } else if (this.level >= 20) {
                    if (20 > ZUtil.random(100)) {
                        GiftBox.addGift(listGift, 4, 841, 2);
                    }
                } else if (this.level >= 10) {
                    if (20 > ZUtil.random(100)) {
                        GiftBox.addGift(listGift, 4, 841, 1);
                    }
                }
                event.EventManager.dispatchMapDrop(zone, listGift, this.level);
                for (int i = 0; i < zone.players.size(); i++) {
                    Player p0 = zone.players.get(i);
                    if (p0 == null || p0.isBot || p0.conn == null) continue;
                    p0.getService().send_time_cool_down(this.time, "Hang " + (this.level), 0);
                    core.RewardService.sendGiftOrMail(p0, 1, "Phó bản hang động",
                            "Hoàn thành Hang " + (this.level), listGift, true);
                    p0.update_point_hang_dong(1);
                    if (this.level == 50) {
                        p0.updateArchiDaily(8);
                    }
                }
            }
        }
    }

    @Override
    public void onDeath(Player pKill, Mob mob) {
        try {
            if (Math.abs(pKill.level - mob.level) <= 10) {
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
