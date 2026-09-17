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
import template.ItemTemplate4;

public class BossNgua9HongMao extends AbsBoss {

    public BossNgua9HongMao() {
        super(174);
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
        return 10000000;
    }

    @Override
    public int getLevel() {
        return 80;
    }

    @Override
    public short[] getSkills() {
        return new short[]{10};
    }

    @Override
    public short[] getSpawnLocation() {
        return new short[]{2, 300, 300};
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
            Manager.gI().chatKTG(0, pKill.name + " đã hạ gục Boss Ngựa 9 Hồng Mao!", 0);
            
            List<GiftBox> list_gift = new ArrayList<>();
            template.ItemTemplate4 it_temp4 = ItemTemplate4.get_it_by_id(355);
            if (it_temp4 != null) {
                GiftBox gb = new GiftBox();
                gb.id = it_temp4.id;
                gb.type = 4;
                gb.name = it_temp4.name;
                gb.icon = it_temp4.icon;
                gb.num = 10;
                gb.color = 0;
                list_gift.add(gb);
            }
            
            pKill.updateMoney();
            if (!list_gift.isEmpty()) {
                core.RewardService.sendGiftOrMail(pKill, 1, "Boss Ngựa 9 Hồng Mao", "Phần thưởng hạ gục", list_gift, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropReward(Player pKill, List<GiftBox> list_gift) {
    }
}
