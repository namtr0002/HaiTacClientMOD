package mob;

import map.Zone;
import template.MobTemplate;

/**
 * Class MonsterHuman đại diện cho Mob dạng người (Humanoid Mob) - tương ứng với Client MonsterHuman.java.
 * Quái có tạo hình người, mặc trang bị (wearing), đầu (head), tóc (hair).
 */
public class MonsterHuman extends Mob {

    public short head;
    public short hair;
    public short[] wearing;

    public MonsterHuman() {
        super();
    }

    public MonsterHuman(MobTemplate template, Zone zone, short x, short y, int index) {
        super(template, zone, x, y, index);
        if (template != null) {
            this.head = template.head;
            this.hair = template.hair;
            this.wearing = template.wearing;
        }
    }

    @Override
    public MobType getMobType() {
        if (this.is_boss || this.boss_inf != null || this.typeSpecMonSter == 2) {
            return MobType.BOSS;
        }
        if (this.isSieuTrum || this.typeSpecMonSter == 1) {
            return MobType.ELITE;
        }
        return MobType.HUMAN;
    }
}
