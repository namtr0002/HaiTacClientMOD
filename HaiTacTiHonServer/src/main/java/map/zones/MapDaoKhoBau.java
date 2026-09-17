package map.zones;

import model.Player;
import network.Service;
import map.Zone;
import mob.Mob;
import zabstracts.AbsTreasureDungeon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Dungeon implementation for Treasure Island (Map Dao Kho Bau).
 */
public class MapDaoKhoBau extends AbsTreasureDungeon {

    public int playerLevel;
    public int ticketId;
    public boolean gifted = false;

    public static MapDaoKhoBau createDungeon(Player p, int ticketId) {
        MapDaoKhoBau dungeon = new MapDaoKhoBau(p.level, ticketId);
        dungeon.creatorName = p.name;
        dungeon.create();
        List<Player> list = java.util.Collections.singletonList(p);
        database.DungeonSessionCache.gI().registerInstance(dungeon, p.name, list, 266, dungeon.time, p.level, 0, (byte) 0, ticketId);
        return dungeon;
    }

    public MapDaoKhoBau() {
        this.mobs = new CopyOnWriteArrayList<>();
        this.maps = new CopyOnWriteArrayList<>();
    }

    public MapDaoKhoBau(int playerLevel, int ticketId) {
        this.playerLevel = playerLevel;
        this.ticketId = ticketId;
        this.mobs = new CopyOnWriteArrayList<>();
        this.maps = new CopyOnWriteArrayList<>();
    }

    @Override
    public int getDungeonId() {
        return -84;
    }

    @Override
    public void create() {
        this.time = System.currentTimeMillis() + 600_000L; // 10 phút thời gian phó bản
        this.gifted = false;
        int index_mob = -2;
        // create map
        Zone[] tplMaps = Zone.getMapByID(266);
        if (tplMaps == null || tplMaps.length == 0 || tplMaps[0] == null) return;
        Zone mapTemplate = tplMaps[0];
        Zone map_boss = new Zone();
        map_boss.template = mapTemplate.template;
        map_boss.zone_id = (byte) 0;
        map_boss.list_mob = new int[0];
        map_boss.map_DaoKhoBau = this;
        map_boss.map_dungeon = this;

        if (mapTemplate.list_mob != null) {
            for (int i = 0; i < mapTemplate.list_mob.length; i++) {
                Mob temp = mapTemplate.getMob(mapTemplate.list_mob[i]);
                if (temp == null) {
                    temp = Mob.ENTRYS.get(mapTemplate.list_mob[i]);
                }
                template.MobTemplate mobTpl = template.MobTemplate.ENTRYS.get(151);
                if (mobTpl == null && temp != null) {
                    mobTpl = temp.mtemplate;
                }
                if (mobTpl == null) continue;

                Mob mob_add = new Mob();
                mob_add.mtemplate = mobTpl;
                mob_add.x = temp != null ? temp.x : 400;
                mob_add.y = temp != null ? temp.y : 300;
                mob_add.hp_max = 100;
                if (this.ticketId == 385) {
                    mob_add.hp_max = 150;
                }
                mob_add.hp = mob_add.hp_max;
                mob_add.level = this.playerLevel;
                mob_add.isdie = false;
                mob_add.id_target = -1;
                mob_add.index = index_mob--;
                mob_add.map = map_boss;
                mob_add.boss_inf = null;
                mob_add.time_refresh = System.currentTimeMillis() + 600_000L;
                //
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
            p.x = 400;
            p.y = 300;
            p.xold = p.x;
            p.yold = p.y;
            p.map.goto_map(p);
            p.addDungeon(this);
            p.dungeon = this;
            p.getService().update_PK(p, true);
            p.getService().pet(p, true);
            p.getService().send_time_cool_down(this.time, "Thời gian", 0);
            model.Quest.update_map_have_side_quest(p, true);
        }
    }

    @Override
    public void update(Zone zone) throws IOException {
        database.DungeonSessionCache.gI().updateInstanceState(this);
        int aliveMobCount = 0;
        if (this.mobs != null) {
            for (Mob mob : this.mobs) {
                if (mob != null && !mob.isdie && (mob.map == null || mob.map.equals(zone))) {
                    aliveMobCount++;
                }
            }
        }

        // Khi toàn bộ quái trên Đảo Kho Báu đã bị tiêu diệt
        if (aliveMobCount == 0 && this.time > System.currentTimeMillis()) {
            if (!this.gifted) {
                this.gifted = true;
                this.time = System.currentTimeMillis() + 5_000L; // Đếm ngược 5 giây trước khi tự động về map cũ
                for (int i = 0; i < zone.players.size(); i++) {
                    Player p0 = zone.players.get(i);
                    if (p0 == null || p0.isBot || p0.conn == null) continue;
                    p0.getService().send_time_cool_down(this.time, "Rời map sau", 0);
                    p0.getService().send_box_ThongBao_OK("Bạn đã tiêu diệt toàn bộ quái vật trên Đảo Kho Báu! Tự động rời map sau 5 giây.");

                    List<template.GiftBox> list_gift = new ArrayList<>();
                    short[] ids = new short[]{159, 133, -10, 9, 4, 1, 362, 363, 364};
                    byte[] type = new byte[]{4, 4, 4, 7, 7, 7, 4, 4, 4};
                    if (this.ticketId == 385) {
                        ids = new short[]{159, 133, -10, 9, 4, 1, 362, 363};
                        type = new byte[]{4, 4, 4, 7, 7, 7, 4, 4};
                    }
                    for (int j = 0; j < type.length; j++) {
                        if (ids[j] == -10) {
                            template.GiftBox.addGift(list_gift, type[j], (p0.level < 10 ? 10 : p0.level) / 10 + 111, 1);
                        } else {
                            template.GiftBox.addGift(list_gift, type[j], ids[j], 1);
                        }
                    }
                    // Append quà sự kiện (nếu có sự kiện đang chạy)
                    event.EventManager.dispatchMapDrop(this, list_gift, 1);

                    if (!list_gift.isEmpty()) {
                        core.RewardService.sendGiftOrMail(p0, 1, "Đảo Kho Báu", "Hoàn thành phó bản", list_gift, true);
                    }
                }
            }
        }

        // Hết thời gian hoặc đã xong 5 giây đếm ngược -> đưa người chơi về map lưu
        if (this.time < System.currentTimeMillis()) {
            List<Player> playerList = new ArrayList<>(zone.players);
            for (int i = 0; i < playerList.size(); i++) {
                Player p0 = playerList.get(i);
                if (p0 == null) continue;
                p0.isdie = false;
                if (p0.ability != null) {
                    p0.hp = p0.ability.get_hp_max(true);
                    p0.mp = p0.ability.get_mp_max(true);
                }
                p0.dungeon = null;
                p0.removeDungeon(this);
                p0.return_to_previous_map();
                p0.timeEnterMap = System.currentTimeMillis() + 20_000;
            }
            notifyDungeonEnd();
            zone.setRunning(false);
            zone.map_DaoKhoBau = null;
            zone.map_dungeon = null;
        }
    }

    @Override
    public void leave(Player p) throws IOException {
        super.leave(p);
        if (p != null) {
            p.dungeon = null;
            p.removeDungeon(this);
        }
    }

   public static void processBossGift(Zone map, Player p, Mob mobTarget, long dame, String nameMap) throws IOException {
      int value2 = 0;
      int max_hp = mobTarget.hp_max;
      int percent = max_hp / (100 / 5); // 5%
      int value1 = (mobTarget.hp - 1) / percent;
      if (percent > 0) {
         value2 = (int) (((mobTarget.hp - dame) - 1) / percent);
      }
      boolean ch = false;
      List<template.GiftBox> list_gift = new ArrayList<>();
      for (int j = value1 - 1; j >= value2; j--) {
         ch = true;
      }
      if (ch) {
         if (list_gift.size() > 0) {
             try {
                for (int j = 0; j < map.players.size(); j++) {
                   Player p0 = map.players.get(j);
                   if (p0 != null && !p0.isBot && p0.conn != null) {
                      core.RewardService.sendGiftOrMail(p0, 1, "Đảo Kho Báu - " + nameMap,
                              "Phần thưởng gây sát thương 5% HP Boss", list_gift, true);
                   }
                }
             } catch (Exception e) {
                e.printStackTrace();
             }
         }
      }
   }
}

