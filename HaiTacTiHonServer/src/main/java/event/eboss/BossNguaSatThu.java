package event.eboss;

import zabstracts.AbsBoss;
import model.Player;
import core.Manager;
import core.ZUtil;
import network.Service;
import map.Zone;
import java.util.ArrayList;
import java.util.List;
import template.GiftBox;

public class BossNguaSatThu extends AbsBoss {

    public BossNguaSatThu() {
        super(175);
        this.boss_inf = this;
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
        return 1800; // 30 minutes
    }

    @Override
    public void init() {
    }

    @Override
    public int getHpMax() {
        return 5000000;
    }

    @Override
    public int getLevel() {
        return 100;
    }

    @Override
    public short[] getSkills() {
        return new short[]{10};
    }

    @Override
    public short[] getSpawnLocation() {
        return new short[]{124, 200, 200};
    }

    @Override
    public long getMaxDamageReceived() {
        return Long.MAX_VALUE; // Có thể chỉnh giới hạn dame tối đa nhận vào mỗi hit (vd: 50000L)
    }

    @Override
    public long modifyIncomingDamage(Player p, long rawDamage) {
        return super.modifyIncomingDamage(p, rawDamage);
    }

    @Override
    public void onHpMilestone10Percent(Zone map, Player p, int stepIndex) {
        // Custom quà hoặc hiệu ứng mỗi khi mất 10% HP tại đây
    }

    @Override
    public void update() {
    }

    @Override
    public void onDeath(Player pKill) {
        try {
            this.isdie = true;
            this.timeDeath = System.currentTimeMillis();
            this.map.remove_obj(this.index, 1);
            
            List<GiftBox> list_gift = new ArrayList<>();
            template.ItemTemplate4 it_temp4 = template.ItemTemplate4.get_it_by_id(355);
            if (it_temp4 != null) {
                GiftBox gb = new GiftBox();
                gb.id = it_temp4.id;
                gb.type = 4;
                gb.name = it_temp4.name;
                gb.icon = it_temp4.icon;
                gb.num = 5;
                gb.color = 0;
                list_gift.add(gb);
            }
            
            pKill.updateMoney();
            if (!list_gift.isEmpty()) {
                core.RewardService.sendGiftOrMail(pKill, 1, "Ngựa Sát Thủ", "Phần thưởng hạ gục", list_gift, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropReward(Player pKill, List<GiftBox> list_gift) {
    }
}
