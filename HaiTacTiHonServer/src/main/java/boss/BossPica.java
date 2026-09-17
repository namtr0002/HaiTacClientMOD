package boss;

import zabstracts.AbsWorldBoss;
import map.zones.ThuLinhBienKhoi;
import model.Player;
import network.Service;
import core.ZUtil;
import core.Manager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.Zone;
import mob.Mob;
import template.GiftBox;

public class BossPica extends AbsWorldBoss {
    private static BossPica instance;

    public static BossPica gI() {
        if (instance == null) {
            instance = new BossPica();
        }
        return instance;
    }

    public static Mob mob;

    private BossPica() {
        super(173);
        mob = this;
    }

    @Override
    protected int getMobTemplateId() { return 173; }

    @Override
    public int getHpMax() { return 20000; }

    @Override
    public int getLevel() { return 50; }

    @Override
    protected int getMobIndex() { return -10; }

    @Override
    protected String getDisplayName() { return "Boss Pica"; }

    @Override
    protected boolean isValidMapId(int templateId) {
        return templateId < 111;
    }

    @Override
    protected Zone selectZoneFrom(Zone[] zones) {
        if (zones == null || zones.length == 0) return null;
        if (zones.length <= 3) {
            return zones[ZUtil.random(zones.length)];
        }
        return zones[ZUtil.random(3, zones.length)];
    }

    public static void initialize() {
        gI();
        mob = gI();
    }

    public static void start() throws IOException {
        gI().spawn();
        mob = gI();
    }

    @Override
    public long getMaxDamageReceived() {
        return 1L; // Boss Pica chỉ nhận tối đa 1 dame mỗi đòn đánh
    }

    // -- Phần thưởng tiêu diệt --
    @Override
    public void onDeath(Player pKill) {
        try {
            this.isdie = true;
            pKill.map.remove_obj(this.index_mob_save, 1);
            Manager.gI().chatKTG(0, pKill.name + " đã hạ gục Boss Pica!", 5);

            // Thưởng cho người giết
            int capItem = (pKill.level < 10 ? 10 : pKill.level) / 10 + 694;
            List<GiftBox> listKiller = new ArrayList<>();
            GiftBox.addGift(listKiller, 4, 1, 500);
            GiftBox.addGift(listKiller, 4, 339, 2);
            GiftBox.addGift(listKiller, 4, capItem, 5);
            if (!listKiller.isEmpty() && pKill != null && !pKill.isBot && pKill.conn != null) {
                core.RewardService.sendGiftOrMail(pKill, 1, "Phần thưởng", "Tiêu diệt", listKiller, true);
            }

            // Thưởng cho toàn khu
            for (int j = 0; j < pKill.map.players.size(); j++) {
                Player p0 = pKill.map.players.get(j);
                if (p0 != null && !p0.isBot && p0.conn != null && !p0.equals(pKill)) {
                    List<GiftBox> listZone = new ArrayList<>();
                    GiftBox.addGift(listZone, 4, 1, ZUtil.random(100, 200));
                    GiftBox.addGift(listZone, 4, 400, 1);
                    core.RewardService.sendGiftOrMail(p0, 1, "Boss Pica", "Quà Tiêu diệt cả khu nhận", listZone, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
