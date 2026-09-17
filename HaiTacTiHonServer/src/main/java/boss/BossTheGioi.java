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

public class BossTheGioi extends AbsWorldBoss {
    private static BossTheGioi instance;

    public static BossTheGioi gI() {
        if (instance == null) {
            instance = new BossTheGioi();
        }
        return instance;
    }

    //public static Mob mob;
    public static boolean is_spawned = false;

    public static void spawn_boss() {
        try {
            is_spawned = true;
            gI().spawn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BossTheGioi() {
        super(172);
        mob = this;
    }

    @Override
    protected int getMobTemplateId() { return 172; }

    @Override
    public int getHpMax() { return 50000; }

    @Override
    public int getLevel() { return 80; }

    @Override
    protected int getMobIndex() { return -2; }

    @Override
    protected String getDisplayName() { return "Boss Thế Giới"; }

    @Override
    protected boolean isValidMapId(int templateId) {
        return templateId <= 28;
    }

    @Override
    public boolean isSieuTrum() {
        return false;
    }

    @Override
    public boolean isLevelUpOnDeath() {
        return false;
    }

    @Override
    public byte[] getSpawnHours() {
        return null;
    }

    @Override
    public int getRespawnTimeSeconds() {
        return -1; // Controlled by ServerEventManager triggers
    }

    public static void initialize() throws IOException {
        gI().spawn();
        mob = gI();
    }

    @Override
    public long getMaxDamageReceived() {
        return 1L; // Boss Thế Giới chỉ nhận 1 sát thương mỗi đòn đánh
    }

    @Override
    public void onHpMilestone10Percent(Zone map, Player p, int stepIndex) {
        try {
            List<GiftBox> list_gift = new ArrayList<>();
            if (23 > ZUtil.random(120)) {
                GiftBox.addGift(list_gift, 7, 10, 1);
            } else if (23 > ZUtil.random(120)) {
                GiftBox.addGift(list_gift, 4, 0, 100_000);
            }
            if (!list_gift.isEmpty()) {
                core.RewardService.sendGiftOrMail(p, 1, "Boss Thế Giới", "Gây sát thương 10% Hp", list_gift, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -- Phần thưởng tiêu diệt --
    @Override
    public void onDeath(Player pKill) {
        try {
            this.isdie = true;
            pKill.map.remove_obj(this.index_mob_save, 1);
            Manager.gI().chatKTG(0, pKill.name + " đã hạ gục Boss Thế Giới!", 5);

            // Thưởng cho người kết liễu (Last Hit)
            List<GiftBox> listKiller = new ArrayList<>();
            GiftBox.addGift(listKiller, 4, 0, 3_000_000);
            GiftBox.addGift(listKiller, 4, 1, 300);
            GiftBox.addGift(listKiller, 7, 10, 3);
            GiftBox.addGift(listKiller, 4, 29, 1);  // 1 Rương ác quỷ
            GiftBox.addGift(listKiller, 4, 106, 1); // 1 Rương vàng
            if (!listKiller.isEmpty()) {
                core.RewardService.sendGiftOrMail(pKill, 1, "Boss Thế Giới", "Tiêu diệt", listKiller, true);
            }

            // Thưởng cho toàn khu vực
            for (int j = 0; j < pKill.map.players.size(); j++) {
                Player p0 = pKill.map.players.get(j);
                if (!p0.equals(pKill)) {
                    List<GiftBox> listZone = new ArrayList<>();
                    GiftBox.addGift(listZone, 4, 0, ZUtil.random(50_000, 200_000));
                    GiftBox.addGift(listZone, 4, 1, ZUtil.random(20, 50));
                    GiftBox.addGift(listZone, 4, 18, 1); // 1 Rương gỗ
                    core.RewardService.sendGiftOrMail(p0, 1, "Boss Thế Giới", "Quà Tiêu diệt cả khu nhận", listZone, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
