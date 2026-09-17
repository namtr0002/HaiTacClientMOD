package mob;

import model.Player;
import map.Zone;
import template.MobTemplate;

/**
 * Class MobElite đại diện cho Quái Tinh Anh / Thủ Lĩnh (Super Mob / Elite).
 * Tương ứng với MR == 1 / typeSpecMonSter == 1 ở Client (có vòng hiệu ứng aura Tinh Anh).
 */
public class MobElite extends Mob {

    public MobElite() {
        super();
        this.typeSpecMonSter = 1;
        this.isSieuTrum = true;
    }

    public MobElite(MobTemplate template, Zone zone, short x, short y, int index) {
        super(template, zone, x, y, index);
        this.typeSpecMonSter = 1;
        this.isSieuTrum = true;
        // Quái tinh anh gia tăng nhẹ chỉ số HP x1.5 và Dame x1.15
        this.hp_max = (int) (this.hp_max * 1.5);
        this.hp = this.hp_max;
        this.dameMultiplier = 1.15;
    }

    @Override
    public MobType getMobType() {
        return MobType.ELITE;
    }

    @Override
    protected void onDie(Player pKill) {
        // Callback thưởng khi tiêu diệt Quái Tinh Anh
        if (pKill != null && pKill.getService() != null) {
            //pKill.getService().send_box_ThongBao_OK(""+ "Bạn vừa tiêu diệt Thủ Lĩnh " + (mtemplate != null ? mtemplate.name : ""));
        }
    }
}
