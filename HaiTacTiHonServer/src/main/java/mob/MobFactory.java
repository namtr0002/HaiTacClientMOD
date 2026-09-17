package mob;

import zabstracts.AbsBoss;
import map.Zone;
import template.MobTemplate;

/**
 * Factory khởi tạo đối tượng Mob phù hợp theo đặc tính của MobTemplate và Map (Factory Pattern).
 * Đảm bảo phân loại chính xác giữa MonsterHuman, MonsterWalk, MobPet, MobElite, MobBoss, MobSea, MobGuild.
 */
public class MobFactory {

    /**
     * Khởi tạo Mob chuẩn từ MobTemplate và Zone.
     */
    public static Mob createMob(MobTemplate template, Zone zone, short x, short y, int index) {
        if (template == null) {
            return new Mob(null, zone, x, y, index);
        }

        // 1. Quái pháo đài / Căn cứ bang (mob_id 122..125)
        if (template.mob_id >= 122 && template.mob_id <= 125) {
            return new MobGuild(template, zone, x, y, index);
        }

        // 2. Quái Biển / Hải Trình (specMap == 4)
        if (zone != null && zone.template != null && zone.template.specMap == 4) {
            return new MobSea(template, zone, x, y, index);
        }

        // 3. Quái Tinh Anh (Elite)
        if (template.mob_id == 111 || template.mob_id == 114) {
            return new MobElite(template, zone, x, y, index);
        }

        // 4. Quái Linh Thú / Pokemon / Pet dã ngoại (typemonster == 21 hoặc icon 55/56)
        if (template.typemonster == 21 || template.icon == 55 || template.icon == 56) {
            return new MobPet(template, zone, x, y, index);
        }

        // 5. Phân loại theo cấu trúc hình thể từ MobTemplate (Tương thích Client)
        if (template.ishuman == 1) {
            return new MonsterHuman(template, zone, x, y, index);
        } else {
            return new MonsterWalk(template, zone, x, y, index);
        }
    }

    /**
     * Khởi tạo Quái Tinh Anh thủ công.
     */
    public static MobElite createElite(MobTemplate template, Zone zone, short x, short y, int index) {
        return new MobElite(template, zone, x, y, index);
    }

    /**
     * Khởi tạo Mob Boss từ Boss object.
     */
    public static MobBoss createBoss(AbsBoss boss, Zone zone, short x, short y, int index) {
        return new MobBoss(boss, zone, x, y, index);
    }

    /**
     * Khởi tạo Quái Pet / Linh Thú dã ngoại thủ công.
     */
    public static MobPet createPet(MobTemplate template, Zone zone, short x, short y, int index) {
        return new MobPet(template, zone, x, y, index);
    }
}
