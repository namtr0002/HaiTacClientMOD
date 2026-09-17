package event.eboss;

import core.Manager;
import core.ZUtil;
import mob.Mob;
import map.Zone;
import model.Player;
import network.Message;
import template.DataTemplate;
import template.GiftBox;
import template.ItemTemplate4;
import template.MobTemplate;
import itemz.MainItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LucciGioTo {

    private static LucciGioTo instance;

    public static LucciGioTo gI() {
        if (instance == null) instance = new LucciGioTo();
        return instance;
    }

    private LucciGioTo() {}

    public static Mob mob;
    private static long deathTime = 0;
    private static final long RESPAWN_DELAY = 600_000L; // 10 minutes respawn delay

    public static void init() throws IOException {
        if (mob == null) {
            MobTemplate mobTemp = MobTemplate.get_mob_template(163);
            mob = new Mob();
            mob.mtemplate = mobTemp;
            mob.x = 630;
            mob.y = 275;
            mob.hp_max = 2_000_000_000;
            mob.hp = mob.hp_max;
            mob.level = 99;
            mob.isdie = true;
            mob.id_target = -1;
            mob.index = -2;
            mob.map = null;
            mob.boss_inf = null;
        }

        Zone[] mapsSelect = map.MapManager.getInstance()
                .getMap(ZUtil.random(map.MapManager.getInstance().getMaps().size())).zones;
        boolean isSea = false;
        int maxAttempts = 100;
        int attempts = 0;
        while ((mapsSelect[0].list_mob.length < 5
                || mapsSelect.length < 5
                || isSea
                || mapsSelect[0].template.id > 2) && attempts < maxAttempts) {
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
            mob.x = 630;
            mob.y = 275;
        }

        map0.list_mob_custom_add(mob);

        Message m = new Message(1);
        m.writer().writeByte(1);
        m.writer().writeShort(mob.index);
        m.writer().writeShort(mob.x);
        m.writer().writeShort(mob.y);
        map0.send_msg_all_p(m, null, true);
        m.cleanup();

        Manager.gI().chatKTG(0, "Boss Lucci Giỗ Tổ đã xuất hiện tại " + map0.template.name + " khu " + (map0.zone_id + 1), 0);
    }

    public static void update() {
        if (!event.EventManager.isActive(event.Event.ID_SUKIEN_GIOTOHUNGVUONG_2026)) return;
        if (mob == null || (mob.isdie && deathTime > 0 && (System.currentTimeMillis() - deathTime >= RESPAWN_DELAY))) {
            try {
                init();
                deathTime = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void onDeath(Player killer, Mob mob_target) {
        deathTime = System.currentTimeMillis();
        if (mob != null) {
            mob.isdie = true;
        }
        if (killer != null && killer.map != null) {
            killer.map.remove_obj(mob_target.index, 1);
            Manager.gI().chatKTG(0, "Người chơi [" + killer.name + "] đã xuất sắc tiêu diệt Boss Lucci Giỗ Tổ!", 0);

            // Trao quà cho người kết liễu
            List<MainItem> listKiller = new ArrayList<>();
            listKiller.add(new MainItem(158, 4, 2)); // 2 Rương Đại Ác Quỷ
            listKiller.add(new MainItem(838, 4, 2)); // 2 Mâm Vàng
            listKiller.add(new MainItem(226, 4, 1)); // 1 Đá Hải Thạch C6
            MainItem.showGiftBox(killer, "Hạ Gục Lucci Giỗ Tổ", "Chúc mừng bạn đã kết liễu Boss Lucci Giỗ Tổ!", listKiller, true, true);
            killer.update_pointEvent2(5);

            // Trao quà cho toàn bộ người chơi trong map
            for (Player p : killer.map.players) {
                if (p != null && p.conn != null && p.conn.status == 1 && !p.equals(killer)) {
                    List<MainItem> listAll = new ArrayList<>();
                    listAll.add(new MainItem(837, 4, 1)); // 1 Mâm Bạc
                    listAll.add(new MainItem(172, 4, 1)); // 1 Rương Vua Hùng
                    MainItem.showGiftBox(p, "Quà Chung Boss Giỗ Tổ", "Thưởng tham gia diệt Boss Lucci Giỗ Tổ cùng đồng đội!", listAll, true, true);
                    p.update_pointEvent2(1);
                }
            }
        }
    }
}
