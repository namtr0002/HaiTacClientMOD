package mob;

import map.Zone;
import template.MobTemplate;

/**
 * Class MobSea đại diện cho Quái Biển / Quái Hải Trình (Oceanic/Sea Mob).
 * Tương ứng với specMap == 4 ở Client (quái chiến đấu trên mặt nước, tạo hiệu ứng sóng biển).
 */
public class MobSea extends Mob {

    public MobSea() {
        super();
    }

    public MobSea(MobTemplate template, Zone zone, short x, short y, int index) {
        super(template, zone, x, y, index);
    }

    @Override
    public MobType getMobType() {
        return MobType.SEA;
    }
}
