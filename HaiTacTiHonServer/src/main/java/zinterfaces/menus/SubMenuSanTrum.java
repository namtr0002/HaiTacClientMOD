package zinterfaces.menus;

import model.Player;
import mob.Mob;
import model.YesNoDialog;
import template.MobTemplate;
import zinterfaces.iNpc;
import map.Npc;
import map.Zone;
import map.zones.ZSanTrum;
import model.Quest;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SubMenuSanTrum implements iNpc {

    @Override
    public short[] getId() {
        return new short[]{701};
    }

    @Override
    public void sendMenu(Player p, Npc npc) throws IOException {
    }

    @Override
    public void handleMenu(Player p, int index) throws IOException {
        handleMenu(p, (short) 701, index);
    }

    @Override
    public void handleMenu(Player p, int menuId, int index) throws IOException {
        handleMenu(p, (short) menuId, index);
    }

    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (p.sanTrumId < 0 || p.sanTrumId >= ZSanTrum.BOSS.length) {
            p.sanTrumId = core.ZUtil.random(ZSanTrum.BOSS.length);
        }
        if (p.party != null && p.party.list.size() > 1) {
            if (!p.party.list.get(0).equals(p)) {
                p.getService().send_box_ThongBao_OK("Bạn không phải trưởng nhóm");
                return;
            }
            List<String> missingKeys = new ArrayList<>();
            for (Player member : p.party.list) {
                if (member != null && !member.isBot && !member.isDe && !(member instanceof model.DeTu) && !(member instanceof bot.mercenary.MercenaryBot)) {
                    if (member.get_key_boss() < 1) {
                        missingKeys.add(member.name);
                    }
                }
            }
            if (!missingKeys.isEmpty()) {
                p.getService().send_box_ThongBao_OK(String.join(", ", missingKeys) + " không đủ 1 chìa khóa phó bản");
                return;
            }
            p.sanTrumLv = (index + 1) * 10;
            p.sanTrumTick = new boolean[p.party.list.size()];
            p.sanTrumTick[0] = true;
            
            String bossName = ZSanTrum.NAME[p.sanTrumId];
            for (int i = 1; i < p.party.list.size(); i++) {
                final int memberIdx = i;
                Player member = p.party.list.get(i);
                Player p0 = Zone.get_player_by_name_allmap(member.name);
                if (p0 != null && p0.map != null && p0.map.equals(p.map)) {
                    p0.setyesNoDialog(new YesNoDialog(p0, 83, "Thông báo",
                            String.format("%s mời bạn tham gia săn trùm %s cấp %d, bạn có đồng ý?",
                                    p.name, bossName, p.sanTrumLv),
                            new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}, choice -> {
                                if (choice == 0) { // Đồng ý
                                    if (p.party == null || p.party.list.isEmpty() || !p.party.list.get(0).equals(p)) {
                                        return;
                                    }
                                    if (p.sanTrumTick != null && memberIdx < p.sanTrumTick.length) {
                                        p.sanTrumTick[memberIdx] = true;
                                    }
                                    boolean isOk = true;
                                    if (p.sanTrumTick != null) {
                                        for (int j = 1; j < p.sanTrumTick.length; j++) {
                                            if (!p.sanTrumTick[j]) {
                                                isOk = false;
                                                break;
                                            }
                                        }
                                    } else {
                                        isOk = false;
                                    }
                                    if (isOk) {
                                        try {
                                            createAndEnterPartySanTrum(p);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        p0.getService().send_box_ThongBao_OK("Đợi thành viên khác đồng ý vào phó bản");
                                    }
                                } else {
                                    p.getService().send_box_ThongBao_OK(p0.name + " đã từ chối tham gia săn trùm");
                                }
                            }));
                    p0.getService().startYesNo();
                }
            }
        } else {
            // Solo entry
            if (p.get_key_boss() < 1) {
                p.getService().send_box_ThongBao_OK("Bạn không đủ 1 chìa khóa phó bản");
                return;
            }
            p.sanTrumLv = (index + 1) * 10;
            createAndEnterSoloSanTrum(p);
        }
    }

    public static void createAndEnterPartySanTrum(Player leader) throws IOException {
        if (leader == null || leader.party == null || leader.party.list.isEmpty()) return;
        
        List<String> missingKeys = new ArrayList<>();
        for (int i = 0; i < leader.party.list.size(); i++) {
            Player get = leader.party.list.get(i);
            if (get.map == null || !get.map.equals(leader.map)) {
                leader.getService().send_box_ThongBao_OK(get.name + " không ở trong map");
                return;
            }
            if (get != null && !get.isBot && !get.isDe && !(get instanceof model.DeTu) && !(get instanceof bot.mercenary.MercenaryBot)) {
                if (get.get_key_boss() < 1) {
                    missingKeys.add(get.name);
                }
            }
        }
        if (!missingKeys.isEmpty()) {
            String msg = String.join(", ", missingKeys) + " không đủ 1 chìa khóa phó bản";
            for (Player pMember : leader.party.list) {
                if (pMember != null && pMember.getService() != null) {
                    pMember.getService().send_box_ThongBao_OK(msg);
                }
            }
            return;
        }

        if (leader.sanTrumId < 0 || leader.sanTrumId >= ZSanTrum.BOSS.length) {
            leader.sanTrumId = core.ZUtil.random(ZSanTrum.BOSS.length);
        }

        // create map boss
        Zone map_boss = new Zone();
        Zone[] zones59 = Zone.getMapByID(59);
        if (zones59 != null && zones59.length > 0 && zones59[0] != null) {
            map_boss.template = zones59[0].template;
        } else {
            Zone[] zones261 = Zone.getMapByID(261);
            if (zones261 != null && zones261.length > 0 && zones261[0] != null) {
                map_boss.template = zones261[0].template;
            } else {
                map_boss.template = leader.map.template;
            }
        }
        map_boss.zone_id = (byte) 0;
        map_boss.list_mob = new int[0];
        map_boss.mapSanTrum = new ZSanTrum();
        map_boss.mapSanTrum.state = 0;
        map_boss.mapSanTrum.time = System.currentTimeMillis() + 90_000;
        {
            Mob mob = new Mob();
            mob.mtemplate = MobTemplate.get_mob_template(ZSanTrum.BOSS[leader.sanTrumId]);
            mob.x = 408;
            mob.y = 216;
            long maxDame = 0;
            long totalDame = 0;
            if (leader.party != null && leader.party.list != null) {
                for (Player mem : leader.party.list) {
                    if (mem == null) continue;
                    long d = (mem.ability != null) ? mem.ability.get_dame(true) : 1000L;
                    if (mem.ability != null) {
                        d = (d * mem.ability.get_dame_devil_percent()) / 100;
                    }
                    totalDame += d;
                    maxDame = Math.max(maxDame, d);
                }
            } else {
                maxDame = (leader.ability != null) ? leader.ability.get_dame(true) : 1000L;
                totalDame = maxDame;
            }
            long effectiveDame = Math.max(maxDame, (long) (maxDame + (totalDame - maxDame) * 0.5));
            if (effectiveDame < 1000) effectiveDame = 1000;
            // Chuẩn hóa HP Boss Săn Trùm tổ đội (90s): Nerf chuẩn, 8x dame tổ đội, tăng nhẹ theo cấp
            long baseBossHp = 80_000L + (long) leader.sanTrumLv * 4_000L;
            long scaledDameHp = effectiveDame * 8L;
            long finalBossHp = Math.max(baseBossHp, scaledDameHp);
            mob.hp_max = (int) Math.min(finalBossHp, Integer.MAX_VALUE);
            mob.hp = mob.hp_max;
            mob.level = Math.min(100, leader.sanTrumLv);
            mob.isdie = false;
            mob.id_target = -1;
            mob.index = -2;
            mob.map = map_boss;
            mob.is_boss = true;
            mob.boss_inf = null;
            map_boss.mapSanTrum.mob = mob;
            map_boss.mobs.put(mob.index, mob);
        }

        for (int i = 0; i < leader.party.list.size(); i++) {
            Player get = leader.party.list.get(i);
            get.numSanTrum++;
            if (!get.isBot && !get.isDe && !(get instanceof model.DeTu) && !(get instanceof bot.mercenary.MercenaryBot)) {
                get.update_key_boss(-1);
                get.updateMoney();
                get.getService().CountDown_Ticket();
            }
            get.map.leave_map(get, 2);
            get.map = map_boss;
            get.x = 50;
            get.y = 320;
            get.xold = get.x;
            get.yold = get.y;
            get.map.goto_map(get);
            get.getService().update_PK(leader, true);
            leader.getService().update_PK(get, true);
            Quest.update_map_have_side_quest(get, true);
        }

        map_boss.start_map();
        Zone.add_map_plus(map_boss);
    }

    public static void createAndEnterSoloSanTrum(Player p) throws IOException {
        if (p.sanTrumId < 0 || p.sanTrumId >= ZSanTrum.BOSS.length) {
            p.sanTrumId = core.ZUtil.random(ZSanTrum.BOSS.length);
        }
        // create map boss
        Zone map_boss = new Zone();
        Zone[] zones59 = Zone.getMapByID(59);
        if (zones59 != null && zones59.length > 0 && zones59[0] != null) {
            map_boss.template = zones59[0].template;
        } else {
            Zone[] zones261 = Zone.getMapByID(261);
            if (zones261 != null && zones261.length > 0 && zones261[0] != null) {
                map_boss.template = zones261[0].template;
            } else {
                map_boss.template = p.map.template;
            }
        }
        map_boss.zone_id = (byte) 0;
        map_boss.list_mob = new int[0];
        map_boss.mapSanTrum = new ZSanTrum();
        map_boss.mapSanTrum.state = 0;
        map_boss.mapSanTrum.time = System.currentTimeMillis() + 90_000;
        {
            Mob mob = new Mob();
            mob.mtemplate = MobTemplate.get_mob_template(ZSanTrum.BOSS[p.sanTrumId]);
            mob.x = 408;
            mob.y = 216;
            long playerDame = (p.ability != null) ? p.ability.get_dame(true) : 1000L;
            if (p.ability != null) {
                playerDame = (playerDame * p.ability.get_dame_devil_percent()) / 100;
            }
            if (playerDame < 1000) playerDame = 1000;
            // Chuẩn hóa HP Boss Săn Trùm solo (90s): Nerf chuẩn, 5x dame cá nhân, tăng nhẹ theo cấp
            long baseBossHp = 50_000L + (long) p.sanTrumLv * 2_500L;
            long scaledDameHp = playerDame * 5L;
            long finalBossHp = Math.max(baseBossHp, scaledDameHp);
            mob.hp_max = (int) Math.min(finalBossHp, Integer.MAX_VALUE);
            mob.hp = mob.hp_max;
            mob.level = Math.min(100, p.sanTrumLv);
            mob.isdie = false;
            mob.id_target = -1;
            mob.index = -2;
            mob.map = map_boss;
            mob.is_boss = true;
            mob.boss_inf = null;
            map_boss.mapSanTrum.mob = mob;
            map_boss.mobs.put(mob.index, mob);
        }

        p.numSanTrum++;
        p.update_key_boss(-1);
        p.updateMoney();
        p.getService().CountDown_Ticket();
        p.map.leave_map(p, 2);
        p.map = map_boss;
        p.x = 50;
        p.y = 320;
        p.xold = p.x;
        p.yold = p.y;
        p.map.goto_map(p);
        p.getService().update_PK(p, true);
        Quest.update_map_have_side_quest(p, true);

        map_boss.start_map();
        Zone.add_map_plus(map_boss);
    }
}
