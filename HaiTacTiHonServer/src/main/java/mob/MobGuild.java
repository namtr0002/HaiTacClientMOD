package mob;

import map.Zone;
import template.MobTemplate;

/**
 * Class MobGuild đại diện cho Quái Căn Cứ Bang Hội / Pháo Đài Bảo Vệ.
 * Tương ứng với BR == 19 / IdIcon 58 ở Client.
 */
public class MobGuild extends Mob {

    public int guildId;

    public MobGuild() {
        super();
        this.keyflag = "GUILD_MOB";
    }

    public MobGuild(MobTemplate template, Zone zone, short x, short y, int index) {
        super(template, zone, x, y, index);
        this.keyflag = "GUILD_MOB";
    }

    @Override
    public MobType getMobType() {
        return MobType.GUILD;
    }
}
