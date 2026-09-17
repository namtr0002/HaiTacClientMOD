package event.eboss;

import core.Manager;
import core.ZUtil;
import mob.Mob;
import map.Zone;
import model.Player;
import network.Service;
import template.GiftBox;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.DataTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * KaidoRong — Boss Thần Thoại Kaido Rách Thú (Biến hình Rồng Phun Lửa - Boro Breath)
 */
public class KaidoRong {

    private static KaidoRong instance;

    public static KaidoRong gI() {
        if (instance == null) instance = new KaidoRong();
        return instance;
    }

    private KaidoRong() {}

    public static Mob mob;
    private static long deathTime = 0;
    private static final long RESPAWN_DELAY = 3_600_000L; // 1 giờ hồi sinh

    public static void init() throws IOException {
        if (mob == null) {
            template.MobTemplate mobTemp = template.MobTemplate.get_mob_template(163);
            mob = new Mob();
            mob.mtemplate = mobTemp;
            mob.x = 500;
            mob.y = 300;
            mob.hp_max = 2_100_000_000;
            mob.hp = mob.hp_max;
            mob.level = 100;
            mob.isdie = true;
            mob.id_target = -1;
            mob.index = -2002;
            mob.map = null;
            mob.boss_inf = null;
            mob.iMob = (pKill, mTarget) -> onDeath(pKill, mTarget);
        }

        Zone[] mapsSelect = map.MapManager.getInstance()
                .getMap(ZUtil.random(map.MapManager.getInstance().getMaps().size())).zones;
        boolean isSea = false;
        int maxAttempts = 100;
        int attempts = 0;
        while ((mapsSelect[0].list_mob.length < 5
                || mapsSelect.length < 5
                || isSea
                || mapsSelect[0].template.id <= 2) && attempts < maxAttempts) {
            mapsSelect = map.MapManager.getInstance()
                    .getMap(ZUtil.random(map.MapManager.getInstance().getMaps().size())).zones;
            isSea = false;
            for (int i = 0; i < DataTemplate.mSea.length; i++) {
                if (DataTemplate.mSea[i][1] == mapsSelect[0].template.id) {
                    isSea = true;
                    break;
                }
            }
            attempts++;
        }

        Zone map0 = mapsSelect[ZUtil.random(mapsSelect.length)];
        if (map0.hasActiveBoss()) {
            for (Zone z : mapsSelect) {
                if (!z.hasActiveBoss()) {
                    map0 = z;
                    break;
                }
            }
        }

        mob.isdie = false;
        mob.map   = map0;
        mob.hp_max = 2_000_000_000;
        mob.hp    = mob.hp_max;

        Mob mobInMap = (map0.list_mob != null && map0.list_mob.length > 0) ? map0.getMob(map0.list_mob[ZUtil.random(map0.list_mob.length)]) : null;
        if (mobInMap != null && mobInMap.x > 0 && mobInMap.y > 0) {
            mob.x = mobInMap.x;
            mob.y = mobInMap.y;
        } else {
            mob.x = 500;
            mob.y = 300;
        }

        map0.list_mob_custom_add(mob);

        network.Message m = new network.Message(1);
        m.writer().writeByte(1);
        m.writer().writeShort(mob.index);
        m.writer().writeShort(mob.x);
        m.writer().writeShort(mob.y);
        map0.send_msg_all_p(m, null, true);
        m.cleanup();

        Manager.gI().chatKTG(0,
                "[SIÊU EVENT WANO] BOSS SIÊU CẤP KAIDO RỒNG THẦN ĐÃ GIÁO LÂM TẠI " + map0.template.name + " khu " + (map0.zone_id + 1) + "! Toàn bộ Hải Tặc hãy tập hợp tiêu diệt!", 5);
    }

    public static void onDeath(Player pKill, Mob mobTarget) {
        try {
            mobTarget.isdie = true;
            mobTarget.hp = 0;
            if (mobTarget.map != null) {
                mobTarget.map.remove_obj(mobTarget.index, 1);
            }
            deathTime = System.currentTimeMillis();

            // Phần thưởng kết liễu siêu khủng
            List<GiftBox> list_gift = new ArrayList<>();
            GiftBox.addGift(list_gift, 4, 0, ZUtil.random(10_000_000, 50_000_000)); // Beri
            GiftBox.addGift(list_gift, 4, 1, ZUtil.random(1000, 5000)); // Ruby
            GiftBox.addGift(list_gift, 4, 609, 15); // Rương Thần Thoại Wano
            GiftBox.addGift(list_gift, 4, 610, 10); // Cuộn Bí Kíp Samurai
            GiftBox.addGift(list_gift, 4, 135, 30); // Tinh thể đá

            if (!list_gift.isEmpty() && pKill != null) {
                core.RewardService.sendGiftOrMail(pKill, 1, "Săn Boss Kaido Rồng", "Anh hùng dũng cảm kết liễu Boss Thần Thoại Kaido Rồng Bách Thú", list_gift, true);
            }

            // Phần thưởng cho toàn bộ người chơi trong khu
            if (mobTarget.map != null && mobTarget.map.players != null) {
                for (int j = 0; j < mobTarget.map.players.size(); j++) {
                    Player p0 = mobTarget.map.players.get(j);
                    if (p0 != null && !p0.equals(pKill)) {
                        List<GiftBox> list_gift2 = new ArrayList<>();
                        GiftBox.addGift(list_gift2, 4, 0, ZUtil.random(500_000, 2_000_000));
                        GiftBox.addGift(list_gift2, 4, 1, ZUtil.random(100, 500));
                        GiftBox.addGift(list_gift2, 4, 609, 3); // Rương Thần Thoại Wano
                        GiftBox.addGift(list_gift2, 4, 610, 1); // Cuộn Bí Kíp Samurai
                        core.RewardService.sendGiftOrMail(p0, 1, "Săn Boss Kaido Rồng", "Quà tham gia trận đại chiến Kaido Rồng", list_gift2, true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        if (mob != null && mob.isdie) {
            if (deathTime == 0) {
                deathTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - deathTime >= RESPAWN_DELAY) {
                try {
                    init();
                    deathTime = 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
