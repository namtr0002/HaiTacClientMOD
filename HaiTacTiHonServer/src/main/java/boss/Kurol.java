package boss;

import zabstracts.AbsBoss;
import model.Player;
import core.ZUtil;
import core.Manager;
import java.util.List;
import template.GiftBox;

public class Kurol extends AbsBoss {

    public Kurol() {
        super(23);
    }

    @Override
    public void init() {
    }

    @Override
    public int getHpMax() {
        return 280000;
    }

    @Override
    public int getLevel() {
        return 25;
    }

    @Override
    public short[] getSkills() {
        return new short[]{10};
    }

    @Override
    public short[] getSpawnLocation() {
        return new short[]{27, 500, 260};
    }

    @Override
    public boolean isSieuTrum() {
        return true;
    }

    @Override
    public boolean isLevelUpOnDeath() {
        return true;
    }

    @Override
    public byte[] getSpawnHours() {
        return null; // Mở 24/24 theo chu kỳ hồi sinh 30 phút (:00 và :30)
    }

    @Override
    public int getRespawnTimeSeconds() {
        return -1; // Spawns daily at scheduled hours, no timer-based respawn
    }

    @Override
    public long getMaxDamageReceived() {
        return (long) this.hp_max * 10L / 100L; // Giới hạn tối đa 10% HP boss/hit
    }

    @Override
    public long modifyIncomingDamage(Player p, long rawDamage) {
        return super.modifyIncomingDamage(p, rawDamage);
    }

    @Override
    public void onHpMilestone10Percent(map.Zone map, Player p, int stepIndex) {
        // Custom quà hoặc hiệu ứng mỗi khi mất 10% HP tại đây
    }

    @Override
    public void update() {
    }

    @Override
    public void dropReward(Player pKill, List<GiftBox> list_gift) {
        StringBuilder notice = new StringBuilder("Tiêu diệt siêu trùm " + this.mtemplate.name + ": ");

        if (50 > ZUtil.random(120)) {
            GiftBox gb_rcam = new GiftBox();
            template.ItemTemplate4 it_temp4_in = template.ItemTemplate4.get_it_by_id((((pKill.level < 11 ? 11 : pKill.level) / 10) + 111));
            if (it_temp4_in != null) {
                gb_rcam.id = it_temp4_in.id;
                gb_rcam.type = 4;
                gb_rcam.name = it_temp4_in.name;
                gb_rcam.icon = it_temp4_in.icon;
                gb_rcam.num = 1;
                gb_rcam.color = 0;
                list_gift.add(gb_rcam);
                notice.append("x1 rương cam cùng cấp, ");
            }
        }

        if (30 > ZUtil.random(120)) {
            GiftBox gb_rcam = new GiftBox();
            template.ItemTemplate7 it_temp7_in = template.ItemTemplate7.get_it_by_id(10);
            if (it_temp7_in != null) {
                gb_rcam.id = it_temp7_in.id;
                gb_rcam.type = 7;
                gb_rcam.name = it_temp7_in.name;
                gb_rcam.icon = it_temp7_in.icon;
                gb_rcam.num = 1;
                gb_rcam.color = 0;
                list_gift.add(gb_rcam);
                notice.append("x1 đá thạch anh, ");
            }
        }

        int beri_receiv = 5000;
        if (this.levelBoss <= 10) {
            beri_receiv = (beri_receiv / 100) * (100 + this.levelBoss * 20);
        }
        GiftBox gb_beri = new GiftBox();
        template.ItemTemplate4 it_temp4 = template.ItemTemplate4.get_it_by_id(0);
        if (it_temp4 != null) {
            gb_beri.id = it_temp4.id;
            gb_beri.type = 4;
            gb_beri.name = it_temp4.name;
            gb_beri.icon = it_temp4.icon;
            gb_beri.num = beri_receiv;
            gb_beri.color = 0;
            list_gift.add(gb_beri);
            notice.append(beri_receiv).append(" beri, ");
        }

        String noticeStr = notice.toString();
        if (noticeStr.endsWith(", ")) {
            noticeStr = noticeStr.substring(0, noticeStr.length() - 2);
        }
        Manager.gI().chatKTG(0, noticeStr, 5);
    }
}
