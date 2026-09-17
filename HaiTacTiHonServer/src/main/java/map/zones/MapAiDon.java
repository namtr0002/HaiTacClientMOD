package map.zones;

import model.Player;
import map.Zone;
import mob.Mob;
import core.ZUtil;
import network.Service;
import template.GiftBox;
import template.ItemTemplate4;
import template.ItemTemplate7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import zabstracts.AbsDungeon;

public class MapAiDon extends AbsDungeon {

    public int hpScale;
    public int playerLevel;

    public static MapAiDon createDungeon(Player p, byte mode) {
        MapAiDon dungeon = new MapAiDon();
        dungeon.mode = mode;
        dungeon.playerLevel = p != null ? p.level : 35;
        long dame = (p != null && p.ability != null) ? p.ability.get_dame(true) : 1000L;
        if (p != null && p.ability != null) {
            dame = (dame * p.ability.get_dame_devil_percent()) / 100;
        }
        dungeon.hpScale = (int) Math.max(1000L, dame);
        if (p != null && !p.isBot && !p.isDe && !(p instanceof model.DeTu) && !(p instanceof bot.mercenary.MercenaryBot)) {
            p.aidonMax--;
        }
        p.addDungeon(dungeon);
        dungeon.create();
        List<Player> list = java.util.Collections.singletonList(p);
        database.DungeonSessionCache.gI().registerInstance(dungeon, p.name, list, 167, dungeon.time, p.level, dungeon.hpScale, (byte) 0, 0);
        return dungeon;
    }

    @Override
    public void join(Player p) throws IOException {
        if (maps != null && !maps.isEmpty()) {
            p.save_previous_map();
            map.Vgo vgo = new map.Vgo();
            vgo.map_go = new Zone[]{maps.get(0)};
            vgo.xnew = 350;
            vgo.ynew = 260;
            p.goto_map(vgo);
            p.addDungeon(this);
            p.getService().send_time_cool_down(this.time, "Thời gian", 0);
        }
    }

    @Override
    public void create() {
        this.time = System.currentTimeMillis() + 60_000L * 15;
        this.checkG = ConcurrentHashMap.newKeySet();
        this.checkG.add(167);
        maps = new CopyOnWriteArrayList<>();
        mobs = new CopyOnWriteArrayList<>();
        int index = -2;
        for (int j = 167; j < 177; j++) {
            // create map
            Zone[] templates = Zone.getMapByID(j);
            if (templates == null || templates.length == 0 || templates[0] == null) continue;
            Zone mapTemplate = templates[0];
            Zone map_dungeon = new Zone();
            map_dungeon.template = mapTemplate.template;
            map_dungeon.zone_id = (byte) 0;
            map_dungeon.list_mob = new int[0];
            for (int i = 0; i < mapTemplate.list_mob.length; i++) {
                Mob temp = mapTemplate.getMob(mapTemplate.list_mob[i]);
                if (temp == null) {
                    temp = Mob.ENTRYS.get(mapTemplate.list_mob[i]);
                }
                if (temp == null || temp.mtemplate == null) continue;
                Mob mob_add = new Mob();
                mob_add.mtemplate = temp.mtemplate;
                mob_add.x = temp.x;
                mob_add.y = temp.y;
                
                // Chuẩn hóa HP quái Ải Đơn (10 phòng): Nerf chuẩn, quái và boss dễ chịu, tăng độ khó nhẹ theo mode
                long minHp = 10_000L + (long) this.mode * 10_000L;
                long scaleMult = 2L + (long) this.mode;
                long scaledHp = (long) Math.max(this.hpScale, 1000) * scaleMult;
                long finalHp = Math.max(minHp, scaledHp);
                
                boolean isBossRoom = (j == 175 || i == mapTemplate.list_mob.length - 1);
                if (isBossRoom) {
                    finalHp = (finalHp * 150L) / 100L;
                }
                
                mob_add.hp_max = (int) Math.min(finalHp, Integer.MAX_VALUE);
                mob_add.hp = mob_add.hp_max;
                mob_add.level = 35 + this.mode * 10;
                if (mob_add.level > 100) {
                    mob_add.level = 100;
                }
                mob_add.isdie = false;
                mob_add.id_target = -1;
                mob_add.index = index--;
                mob_add.map = map_dungeon;
                mob_add.is_boss = isBossRoom;
                mob_add.boss_inf = null;
                mobs.add(mob_add);
                map_dungeon.mobs.put(mob_add.index, mob_add);
            }
            map_dungeon.start_map();
            map_dungeon.map_dungeon = this;
            Zone.add_map_plus(map_dungeon);
            maps.add(map_dungeon);
        }
    }


    @Override
    public void update(Zone zone) throws IOException {
        database.DungeonSessionCache.gI().updateInstanceState(this);
        Player p_select = null;
        boolean ok_out_map = false;
        if (!zone.players.isEmpty()) {
            Player p0 = zone.players.get(0);
            int num_mob = 0;
            for (int i = 0; i < this.mobs.size(); i++) {
                Mob mob = this.mobs.get(i);
                if (mob != null && mob.map != null && mob.map.equals(zone) && !mob.isdie) {
                    num_mob++;
                }
            }
            if (num_mob == 0 && this.time > System.currentTimeMillis()) {
                if (zone.template.id == 175 && !p0.is_complete_dungeon) {
                    this.time = System.currentTimeMillis() + 10_000L;
                    p0.getService().send_time_cool_down(this.time, "Thời gian", 2);
                    p0.is_complete_dungeon = true;
                }
                if (!this.checkG.contains(zone.template.id)) {
                    this.checkG.add(zone.template.id);
                    byte mode_dungeon = this.mode;
                    List<GiftBox> list_gift = new ArrayList<>();
                    int beri_receiv = ZUtil.random(4_000, 8_000) * (mode_dungeon + 1);
                    ItemTemplate4 it_temp4;
                    if (mode_dungeon == 11 && 35 > ZUtil.random(150)) {
                        GiftBox gb_beri = new GiftBox();
                        it_temp4 = ItemTemplate4.get_it_by_id(0);
                        if (it_temp4 != null) {
                            gb_beri.id = it_temp4.id;
                            gb_beri.type = 4;
                            gb_beri.name = it_temp4.name;
                            gb_beri.icon = it_temp4.icon;
                            gb_beri.num = beri_receiv * 2;
                            gb_beri.color = 0;
                            list_gift.add(gb_beri);
                        }
                        GiftBox gb_beri2 = new GiftBox();
                        it_temp4 = ItemTemplate4.get_it_by_id(365);
                        if (it_temp4 != null) {
                            gb_beri2.id = it_temp4.id;
                            gb_beri2.type = 4;
                            gb_beri2.name = it_temp4.name;
                            gb_beri2.icon = it_temp4.icon;
                            gb_beri2.num = 1;
                            gb_beri2.color = 0;
                            list_gift.add(gb_beri2);
                        }
                    } else {
                        GiftBox gb_beri = new GiftBox();
                        it_temp4 = ItemTemplate4.get_it_by_id(0);
                        if (it_temp4 != null) {
                            gb_beri.id = it_temp4.id;
                            gb_beri.type = 4;
                            gb_beri.name = it_temp4.name;
                            gb_beri.icon = it_temp4.icon;
                            gb_beri.num = beri_receiv;
                            gb_beri.color = 0;
                            list_gift.add(gb_beri);
                        }
                    }
                    GiftBox gb_botvang = new GiftBox();
                    ItemTemplate7 it_temp7 = ItemTemplate7.get_it_by_id(4);
                    if (it_temp7 != null) {
                        gb_botvang.id = it_temp7.id;
                        gb_botvang.type = 7;
                        gb_botvang.name = it_temp7.name;
                        gb_botvang.icon = it_temp7.icon;
                        gb_botvang.num = ZUtil.random(1, (mode_dungeon + 2));
                        gb_botvang.color = 0;
                        list_gift.add(gb_botvang);
                    }

                    if (35 > ZUtil.random(300 - mode_dungeon * 10)) { // da ho phach
                        GiftBox gb_ = new GiftBox();
                        it_temp4 = ItemTemplate4.get_it_by_id((45 > ZUtil.random(120)) ? ((15 > ZUtil.random(120)) ? 364 : 363)
                                : 362);
                        if (it_temp4 != null) {
                            gb_.id = it_temp4.id;
                            gb_.type = 4;
                            gb_.name = it_temp4.name;
                            gb_.icon = it_temp4.icon;
                            gb_.num = ZUtil.random(1, 3);
                            gb_.color = 0;
                            list_gift.add(gb_);
                        }
                    }
                    if (80 > ZUtil.random(150 - mode_dungeon * 10)) { // ruong huyen bi
                        GiftBox gb_ = new GiftBox();
                        it_temp4 = ItemTemplate4.get_it_by_id(18 + (p0.level / 10));
                        if (it_temp4 != null) {
                            gb_.id = it_temp4.id;
                            gb_.type = 4;
                            gb_.name = it_temp4.name;
                            gb_.icon = it_temp4.icon;
                            int num = ZUtil.random(1, (mode_dungeon + 2));
                            gb_.num = (num < 2) ? num : (num / 2);
                            gb_.color = 0;
                            list_gift.add(gb_);
                        }
                    }
                    // Append quà sự kiện (nếu có sự kiện đang chạy)
                    event.EventManager.dispatchMapDrop(this, list_gift, mode_dungeon + 1);

                    if (p0 != null && !p0.isBot && p0.conn != null) {
                        core.MailService.sendMail(p0, "Hệ Thống", "Phó Bản Ải Đơn",
                                "Chúc mừng bạn đã hoàn thành Ải Đơn cấp độ " + (mode_dungeon + 3) + "! Phần thưởng đính kèm bên dưới.",
                                core.MailService.MAIL_TYPE_GIFT, false, list_gift, 14L * 24 * 3600 * 1000);
                        p0.aiDonLevel = Math.max(p0.aiDonLevel, (mode_dungeon + 1));
                    }
                }
            }
            if (this.time < System.currentTimeMillis()) {
                ok_out_map = true;
                p_select = p0;
            }
        }
        if (ok_out_map) {
            List<Player> outList = new ArrayList<>(zone.players);
            notifyDungeonEnd();
            for (Player pOut : outList) {
                if (pOut != null && pOut.conn != null) {
                    pOut.dungeon = null;
                    pOut.is_complete_dungeon = false;
                    pOut.updateArchiDaily(12);
                    pOut.return_to_previous_map();
                }
            }
        }
        if (zone.players.isEmpty() && this.time < System.currentTimeMillis()) {
            notifyDungeonEnd();
            zone.setRunning(false);
        }
    }


}
