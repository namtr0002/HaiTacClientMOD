package map.zones;

import event.EventManager;

import model.Player;
import model.Quest;
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

public class Map_Sieu_Lien_Tang extends zabstracts.AbsBossDungeon implements zinterfaces.iMob {

    public boolean gifted;
    public HashMap<Mob, Long> listRemoveMap;
    public int levelMob;
    public int hpScale;
    public long timeChat;
    public int mapId;

    public static Map_Sieu_Lien_Tang createDungeon(List<Player> listP, int mapId) {
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
                    p0.ltMax--;
                    p0.updateArchiDaily(14);
                    p0.updateArchiDaily(12);
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
        // Siêu Liên Tầng dành cho cấp cao (Lv 80 - 100+) -> Tính hpScale theo tổng lực tổ đội
        long effectiveScale = Math.max(maxDame, (long) (maxDame + (totalDame - maxDame) * 0.5));
        if (effectiveScale < 1000) effectiveScale = 1000;
        int hpScale = (int) Math.min(effectiveScale, Integer.MAX_VALUE);
        Map_Sieu_Lien_Tang dungeon = new Map_Sieu_Lien_Tang(levelMob, hpScale, mapId);
        dungeon.create();
        database.DungeonSessionCache.gI().registerInstance(dungeon, creator, listP, mapId, dungeon.time, levelMob, hpScale, (byte) 0, 0);
        return dungeon;
    }

    public Map_Sieu_Lien_Tang() {
        this.listRemoveMap = new HashMap<>();
    }

    public Map_Sieu_Lien_Tang(int levelMob, int hpScale, int mapId) {
        this.levelMob = levelMob;
        this.hpScale = hpScale;
        this.mapId = mapId;
        this.listRemoveMap = new HashMap<>();
    }

    public static final String[] CHAT = new String[]{"HaHaHaHa", "Một mình ta chấp hết!", "Mấy con gà thì biết cái gì!", "Đến đây nạp mạng đi!"};

    @Override
    public void create() {
        this.time = System.currentTimeMillis() + (60_000L * 2);
        this.gifted = false;
        this.mobs = new CopyOnWriteArrayList<>();
        this.maps = new CopyOnWriteArrayList<>();

        Zone[] templates = Zone.getMapByID(this.mapId);
        if (templates == null || templates.length == 0 || templates[0] == null) {
            core.Log.error("Map_Sieu_Lien_Tang", "Map template not found for mapId: " + this.mapId);
            return;
        }
        Zone mapTemplate = templates[0];
        Zone map_boss = new Zone();
        map_boss.template = mapTemplate.template;
        map_boss.zone_id = (byte) 0;
        map_boss.list_mob = new int[0];
        map_boss.map_SieuLienTang = this;
        map_boss.map_dungeon = this;

        int index_mob = -2;
        if (mapTemplate.list_mob != null) {
            int floor = Math.max(1, this.mapId - 198); // Tầng 1 -> 20 (map 199 -> 218)
            for (int i = 0; i < mapTemplate.list_mob.length; i++) {
                Mob temp = mapTemplate.getMob(mapTemplate.list_mob[i]);
                if (temp == null || temp.mtemplate == null) continue;
                Mob mob_add = mob.MobFactory.createMob(temp.mtemplate, map_boss, temp.x, temp.y, index_mob--);
                
                // Chuẩn hóa HP Boss Siêu Liên Tầng (120s/tầng): Nerf chuẩn, tăng thử thách vừa phải
                long baseMultiplier = 4L + (long) ((floor - 1) * 3L / 4L);
                long hpNow = (long) Math.max(this.hpScale, 1000) * baseMultiplier;
                long minFloorHp = 50_000L + (long) (floor - 1) * 20_000L;
                hpNow = Math.max(hpNow, minFloorHp);
                
                mob_add.hp_max = (int) Math.min(hpNow, Integer.MAX_VALUE);
                mob_add.hp = mob_add.hp_max;
                mob_add.level = this.levelMob;
                mob_add.isdie = false;
                mob_add.id_target = -1;
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
            p.x = 100;
            p.y = 300;
            p.xold = p.x;
            p.yold = p.y;
            p.map.goto_map(p);
            p.addDungeon(this);
            p.getService().send_time_cool_down(this.time, "Tầng " + (this.mapId - 198), 0);
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
            if (zone.template.id == 211 || !this.gifted) {
                List<Player> playerList = new ArrayList<>(zone.players);
                notifyDungeonEnd();
                playerList.forEach(l -> {
                    if (l != null) {
                        l.dungeon = null;
                        l.return_to_previous_map();
                    }
                });
            } else {
                int nextMapId = zone.template.id + 1;
                Zone[] nextCheck = Zone.getMapByID(nextMapId);
                if (nextCheck == null || nextCheck.length == 0 || nextCheck[0] == null) {
                    List<Player> listP2 = new ArrayList<>(zone.players);
                    notifyDungeonEnd();
                    listP2.forEach(l -> {
                        if (l != null) {
                            l.dungeon = null;
                            l.return_to_previous_map();
                        }
                    });
                } else {
                    List<Player> listP = new ArrayList<>(zone.players);
                    Map_Sieu_Lien_Tang nextDungeon = new Map_Sieu_Lien_Tang(this.levelMob, this.hpScale, nextMapId);
                    nextDungeon.instanceId = this.instanceId;
                    nextDungeon.creatorName = this.creatorName;
                    nextDungeon.participantNames = this.participantNames;
                    nextDungeon.create();
                    database.DungeonSessionCache.gI().registerInstance(nextDungeon, this.creatorName, listP, nextMapId, nextDungeon.time, this.levelMob, this.hpScale, (byte) 0, 0);
                    listP.forEach(p0 -> p0.removeDungeon(Map_Sieu_Lien_Tang.this));
                    nextDungeon.join(listP);
                }
            }
            zone.setRunning(false);
        } else {
            try {
                if (this.timeChat < System.currentTimeMillis() && !this.mobs.isEmpty()) {
                    this.timeChat = System.currentTimeMillis() + 5_000;
                    for (int i = 0; i < this.mobs.size(); i++) {
                        if (!this.mobs.get(i).isdie
                                && (this.mobs.get(i).mtemplate.mob_id == 4
                                || this.mobs.get(i).mtemplate.mob_id == 10
                                || this.mobs.get(i).mtemplate.mob_id == 16
                                || this.mobs.get(i).mtemplate.mob_id == 23
                                || this.mobs.get(i).mtemplate.mob_id == 29
                                || this.mobs.get(i).mtemplate.mob_id == 36
                                || this.mobs.get(i).mtemplate.mob_id == 43
                                || this.mobs.get(i).mtemplate.mob_id == 78
                                || this.mobs.get(i).mtemplate.mob_id == 70
                                || this.mobs.get(i).mtemplate.mob_id == 79
                                || this.mobs.get(i).mtemplate.mob_id == 68
                                || this.mobs.get(i).mtemplate.mob_id == 92
                                || this.mobs.get(i).mtemplate.mob_id == 87
                                || this.mobs.get(i).mtemplate.mob_id == 88)) {
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
                switch (zone.template.id - 198) {
                    case 1: {
                        GiftBox.addGift(listGift, 4, 0, 50_000);
                        GiftBox.addGift(listGift, 7, 1, 25);
                        GiftBox.addGift(listGift, 7, 4, 10);
                    }
                    break;
                    case 2: {
                        GiftBox.addGift(listGift, 4, 0, 50_000);
                        GiftBox.addGift(listGift, 7, 1, 30);
                        GiftBox.addGift(listGift, 7, 4, 15);
                    }
                    break;
                    case 3: {
                        GiftBox.addGift(listGift, 4, 0, 50_000);
                        GiftBox.addGift(listGift, 7, 1, 35);
                        GiftBox.addGift(listGift, 7, 4, 20);
                    }
                    break;
                    case 4: {
                        GiftBox.addGift(listGift, 4, 0, 50_000);
                        GiftBox.addGift(listGift, 7, 1, 40);
                        GiftBox.addGift(listGift, 7, 4, 25);
                        GiftBox.addGift(listGift, 7, 7, 5);
                    }
                    break;
                    case 5: {
                        GiftBox.addGift(listGift, 4, 0, 50_000);
                        GiftBox.addGift(listGift, 7, 1, 45);
                        GiftBox.addGift(listGift, 7, 3, 30);
                    }
                    break;
                    case 6: {
                        GiftBox.addGift(listGift, 4, 0, 80_000);
                        GiftBox.addGift(listGift, 7, 1, 50);
                        GiftBox.addGift(listGift, 7, 3, 35);
                    }
                    break;
                    case 7: {
                        GiftBox.addGift(listGift, 4, 0, 80_000);
                        GiftBox.addGift(listGift, 7, 1, 50);
                        GiftBox.addGift(listGift, 7, 3, 40);
                        GiftBox.addGift(listGift, 4, 133, 5);
                    }
                    break;
                    case 8: {
                        GiftBox.addGift(listGift, 4, 0, 80_000);
                        GiftBox.addGift(listGift, 7, 1, 50);
                        GiftBox.addGift(listGift, 7, 4, 40);
                    }
                    break;
                    case 9: {
                        GiftBox.addGift(listGift, 4, 0, 80_000);
                        GiftBox.addGift(listGift, 7, 1, 50);
                        GiftBox.addGift(listGift, 7, 4, 40);
                    }
                    break;
                    case 10: {
                        GiftBox.addGift(listGift, 4, 0, 80_000);
                        GiftBox.addGift(listGift, 7, 1, 50);
                        GiftBox.addGift(listGift, 7, 4, 40);
                    }
                    break;
                    case 11: {
                        GiftBox.addGift(listGift, 4, 0, 100_000);
                        GiftBox.addGift(listGift, 7, 1, 50);
                        GiftBox.addGift(listGift, 7, 4, 40);
                    }
                    break;
                    case 12: {
                        GiftBox.addGift(listGift, 4, 0, 120_000);
                        GiftBox.addGift(listGift, 7, 1, 50);
                        GiftBox.addGift(listGift, 7, 4, 40);
                    }
                    break;
                    case 13: {
                        GiftBox.addGift(listGift, 4, 0, 200_000);
                        GiftBox.addGift(listGift, 7, 1, 50);
                        GiftBox.addGift(listGift, 4, 1, 80);
                        GiftBox.addGift(listGift, 7, 4, 40);
                    }
                    break;
                }
                int floorNumber = zone.template.id - 198;
                event.EventManager.dispatchMapDrop(zone, listGift, floorNumber);
                if (!listGift.isEmpty()) {
                    for (int i = 0; i < zone.players.size(); i++) {
                        Player p0 = zone.players.get(i);
                        if (p0 == null || p0.isBot || p0.conn == null) continue;
                        p0.getService().send_time_cool_down(this.time, "Tầng " + floorNumber, 0);
                        List<GiftBox> playerGift = new ArrayList<>(listGift);
                        // Tỉ lệ 1% (1/100 nextInt) đánh xong ải hiện sendgift hên lắm mới ra Chứng nhận sư phụ (item 627)
                        if (floorNumber == 13 && new java.util.Random().nextInt(100) == 0) {
                            GiftBox.addGift(playerGift, 4, 627, 1);
                        }
                        core.RewardService.sendGiftOrMail(p0, 1, "Phó bản siêu liên tầng",
                                "Hoàn thành tầng " + floorNumber, playerGift, true);
                        if (floorNumber == 13) {
                            try {
                                event.EventManager.dispatchOnSieuLienTang(p0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
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
