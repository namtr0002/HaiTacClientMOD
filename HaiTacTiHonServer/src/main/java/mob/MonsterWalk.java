package mob;

import map.Zone;
import template.MobTemplate;

/**
 * Class MonsterWalk đại diện cho Mob dạng thú / bò di chuyển (Animal/Walk Mob) - tương ứng với Client MonsterWalk.java.
 * Dùng icon sprite hình ảnh và chỉ số typemonster quy định bộ khung hoạt ảnh animation (BM matrix ở client).
 */
public class MonsterWalk extends Mob {

    public byte typemonster;
    public short icon;

    public MonsterWalk() {
        super();
    }

    public MonsterWalk(MobTemplate template, Zone zone, short x, short y, int index) {
        super(template, zone, x, y, index);
        if (template != null) {
            this.typemonster = template.typemonster;
            this.icon = template.icon;
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
        return MobType.WALK;
    }
}
