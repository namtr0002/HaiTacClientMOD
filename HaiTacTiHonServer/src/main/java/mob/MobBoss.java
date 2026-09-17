package mob;

import zabstracts.AbsBoss;
import model.Player;
import map.Zone;
import template.MobTemplate;

/**
 * Class MobBoss đại diện cho Quái Trùm / Boss Map / Event Boss.
 * Tương ứng với MR == 2 / typeSpecMonSter == 2 ở Client (có vòng ánh sáng Boss).
 */
public class MobBoss extends Mob {

    public MobBoss() {
        super();
        this.typeSpecMonSter = 2;
        this.is_boss = true;
    }

    public MobBoss(MobTemplate template, Zone zone, short x, short y, int index) {
        super(template, zone, x, y, index);
        this.typeSpecMonSter = 2;
        this.is_boss = true;
        this.customAtkCooldown = 1500; // Cooldown đánh nhanh hơn quái thường
    }

    public MobBoss(AbsBoss boss, Zone zone, short x, short y, int index) {
        super(boss != null ? boss.mtemplate : null, zone, x, y, index);
        this.boss_inf = boss;
        this.typeSpecMonSter = 2;
        this.is_boss = true;
    }

    @Override
    public MobType getMobType() {
        return MobType.BOSS;
    }

    @Override
    protected void onUpdate() {
        // AI đặc thù của Boss: Tự động hồi phục khi không bị đánh, hoặc cuồng nộ khi máu dưới 20%
        if (!isdie && hp < hp_max / 5 && dameMultiplier < 1.15) {
            dameMultiplier = 1.15; // Trạng thái Cuồng Nộ (Enraged) +15% Dame
        }
    }

    @Override
    protected void onDie(Player pKill) {
        if (boss_inf != null) {
            // Callback khi Boss sự kiện / Boss thế giới bị tiêu diệt
        }
    }
}
