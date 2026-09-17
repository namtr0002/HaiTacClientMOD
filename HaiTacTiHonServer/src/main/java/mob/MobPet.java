package mob;
import model.Player;
import map.Zone;
import template.MobTemplate;
/**
 * Class MobPet đại diện cho Quái Linh Thú / Pokemon / Pet dã ngoại trên bản đồ.
 * Tương thích chuẩn Client MonsterWalk.java (AD == 21 / Pet.AT matrix / IdIcon 55, 56).
 */
public class MobPet extends MonsterWalk {
    public byte petType = 21;
    public boolean isCatchable = true;
    public int catchRate = 20; // Tỷ lệ bắt pet (%)
    public int ownerId = -1;   // ID chủ sở hữu (nếu đã bị thu phục)
    public MobPet() {
        super();
        this.typemonster = 21;
    }
    public MobPet(MobTemplate template, Zone zone, short x, short y, int index) {
        super(template, zone, x, y, index);
        if (template != null) {
            this.typemonster = template.typemonster;
            this.icon = template.icon;
        }
        this.petType = 21;
    }
    @Override
    public MobType getMobType() {
        if (this.is_boss || this.boss_inf != null || this.typeSpecMonSter == 2) {
            return MobType.BOSS;
        }
        if (this.isSieuTrum || this.typeSpecMonSter == 1) {
            return MobType.ELITE;
        }
        return MobType.PET;
    }
    @Override
    protected void onDie(Player pKill) {
        if (pKill != null && isCatchable) {
            // Logic bắt Pet / Linh Thú dã ngoại
        }
    }
}
