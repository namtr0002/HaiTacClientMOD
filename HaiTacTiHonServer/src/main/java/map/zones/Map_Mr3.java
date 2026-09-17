package map.zones;

import model.Player;
import core.ZUtil;
import map.Zone;
import mob.Mob;
import network.Message;
import network.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashMap;
import java.util.List;
import template.GiftBox;
import template.ItemTemplate4;
import template.ItemTemplate7;

public class Map_Mr3 extends zabstracts.AbsEventDungeon implements zinterfaces.iMob {

    public static String[] CHAT = new String[]{"HaHaHaHa","Một mình Tao Chấp Hếttttttttt","hihi","Mấy con gà thì biết cái gì!!!!!"};
    public boolean gifted;
    public HashMap<Mob, Long> listRemoveMap;
    public int levelMob;
    public int hpScale;
    public long timeChat;

    public static Map_Mr3 createDungeon(List<Player> listP) {
        int levelMob = -1;
        long maxDame = 0;
        long totalDame = 0;
        String creator = (listP != null && !listP.isEmpty() && listP.get(0) != null) ? listP.get(0).name : "Unknown";
        for (Player p0 : listP) {
            if (p0 == null) continue;
            if (!p0.isBot && !p0.isDe && !(p0 instanceof model.DeTu) && !(p0 instanceof bot.mercenary.MercenaryBot)) {
                try {
                    p0.update_key_boss(-2);
                    p0.updateMoney();
                    if (p0.getService() != null) {
                        p0.getService().CountDown_Ticket();
                    }
                    p0.mr3Max--;
                    p0.tableTickOption = null;
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
        Map_Mr3 dungeon = new Map_Mr3(levelMob, hpScale);
        dungeon.create();
        database.DungeonSessionCache.gI().registerInstance(dungeon, creator, listP, 80, dungeon.time, levelMob, hpScale, (byte) 0, 0);
        return dungeon;
    }

    public Map_Mr3() {
        this.listRemoveMap = new HashMap<>();
    }

    public Map_Mr3(int levelMob, int hpScale) {
        this.levelMob = levelMob;
        this.hpScale = hpScale;
        this.listRemoveMap = new HashMap<>();
    }

    @Override
    public void create() {
        this.time = System.currentTimeMillis() + (60_000L * 2);
        this.gifted = false;
        this.mobs = new CopyOnWriteArrayList<>();
        this.maps = new CopyOnWriteArrayList<>();

        Zone mapTemplate = Zone.getMapByID(80)[0];
        Zone map_boss = new Zone();
        map_boss.template = mapTemplate.template;
        map_boss.zone_id = (byte) 0;
        map_boss.list_mob = new int[0];
        map_boss.map_Mr3 = this;
        map_boss.map_dungeon = this;

        int index_mob = -2;
        for (int i = 0; i < mapTemplate.list_mob.length; i++) {
            Mob temp = mapTemplate.getMob(mapTemplate.list_mob[i]);
            if (temp == null || temp.mtemplate == null) continue;
            Mob mob_add = new Mob();
            mob_add.mtemplate = temp.mtemplate;
            mob_add.x = temp.x;
            mob_add.y = temp.y;
            
            // Chuẩn hóa HP Boss Mr Candle (120s): Nerf chuẩn, 4x effective scale, tối thiểu 75.000 HP mỗi boss
            long hpNow = (long) Math.max(this.hpScale, 1000) * 4L;
            hpNow = Math.max(hpNow, 75_000L);
            
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
            p.getService().send_time_cool_down(this.time, "Mr Candle", 0);
            p.getService().update_PK(p, true);
            p.getService().pet(p, true);
            model.Quest.update_map_have_side_quest(p, true);
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
            if (zone.players.isEmpty()) {
                zone.setRunning(false);
                return;
            }
            List<Player> playerList = new ArrayList<>(zone.players);
            playerList.forEach(l -> {
                if (l != null) {
                    l.dungeon = null;
                    l.return_to_previous_map();
                }
            });
            notifyDungeonEnd();
            zone.setRunning(false);
        } else {
            try {
                if (this.timeChat < System.currentTimeMillis() && !this.mobs.isEmpty()) {
                    this.timeChat = System.currentTimeMillis() + 3_000;
                    for (int i = 0; i < this.mobs.size(); i++) {
                        if (!this.mobs.get(i).isdie
                                && (this.mobs.get(i).mtemplate.mob_id == 69
                                || this.mobs.get(i).mtemplate.mob_id == 70)) {
                            Message mc = new Message(17);
                            mc.writer().writeShort(this.mobs.get(i).index);
                            mc.writer().writeByte(1);
                            mc.writer().writeUTF(CHAT[ZUtil.random(CHAT.length)]);
                            zone.send_msg_all_p(mc, null, true);
                            mc.cleanup();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
            }
            if (!this.gifted && this.mobs.isEmpty()) {
                this.gifted = true;
                this.time = System.currentTimeMillis() + 6000L;
                List<GiftBox> listGift = new ArrayList<>();
                {
                    GiftBox gb = new GiftBox();
                    ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(0);
                    gb.id = it_temp4.id;
                    gb.type = 4;
                    gb.name = it_temp4.name;
                    gb.icon = it_temp4.icon;
                    gb.num = 100000;
                    gb.color = 0;
                    listGift.add(gb);
                }
                {
                    GiftBox gb_ = new GiftBox();
                    ItemTemplate7 it4 = ItemTemplate7.get_it_by_id(1);
                    gb_.id = it4.id;
                    gb_.type = 7;
                    gb_.name = it4.name;
                    gb_.icon = it4.icon;
                    gb_.num = ZUtil.random(15, 20);
                    gb_.color = 0;
                    listGift.add(gb_);
                }
                {
                    GiftBox gb_ = new GiftBox();
                    ItemTemplate7 it4 = ItemTemplate7.get_it_by_id(4);
                    gb_.id = it4.id;
                    gb_.type = 7;
                    gb_.name = it4.name;
                    gb_.icon = it4.icon;
                    gb_.num = ZUtil.random(10, 15);
                    gb_.color = 0;
                    listGift.add(gb_);
                }
                {
                    GiftBox gb_ = new GiftBox();
                    ItemTemplate7 it4 = ItemTemplate7.get_it_by_id(3);
                    gb_.id = it4.id;
                    gb_.type = 7;
                    gb_.name = it4.name;
                    gb_.icon = it4.icon;
                    gb_.num = ZUtil.random(5, 10);
                    gb_.color = 0;
                    listGift.add(gb_);
                }
                if (15 > ZUtil.random(50)) {
                    GiftBox gb_ = new GiftBox();
                    ItemTemplate7 it4 = ItemTemplate7.get_it_by_id(6);
                    gb_.id = it4.id;
                    gb_.type = 7;
                    gb_.name = it4.name;
                    gb_.icon = it4.icon;
                    gb_.num = ZUtil.random(4, 10);
                    gb_.color = 0;
                    listGift.add(gb_);
                }
                event.EventManager.dispatchMapDrop(zone, listGift, 1);
                for (int i = 0; i < zone.players.size(); i++) {
                    Player p0 = zone.players.get(i);
                    if (p0 == null || p0.isBot || p0.conn == null) continue;
                    p0.getService().send_time_cool_down(this.time, "Kết Thúc Sau", 0);
                    core.RewardService.sendGiftOrMail(p0, 1, "Bộ đôi Mr Candle",
                            "Hoàn thành", listGift, true);
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
