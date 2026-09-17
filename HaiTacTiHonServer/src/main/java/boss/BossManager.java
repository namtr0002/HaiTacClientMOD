package boss;

import zabstracts.AbsBoss;
import java.util.ArrayList;
import java.util.List;
import map.Zone;
import mob.Mob;

public class BossManager {
    private static BossManager instance = null;
    private final List<AbsBoss> bosses;
    private final java.util.Map<Integer, Class<? extends AbsBoss>> bossRegistry = new java.util.HashMap<>();

    public static final byte[] BOSS_LIVE = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final byte[] BOSS_AREA = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    private BossManager() {
        this.bosses = new ArrayList<>();
        regClassBoss();
        initBosses();
    }

    public static BossManager gI() {
        if (instance == null) {
            instance = new BossManager();
        }
        return instance;
    }

    private void regClassBoss() {
        bossRegistry.clear();
        List<Class<?>> classes = core.ZUtil.getClasses("boss");
        for (Class<?> clazz : classes) {
            if (AbsBoss.class.isAssignableFrom(clazz) && !clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                @SuppressWarnings("unchecked")
                Class<? extends AbsBoss> bossClazz = (Class<? extends AbsBoss>) clazz;
                register(bossClazz);
            }
        }
    }

    private void register(Class<? extends AbsBoss> clazz) {
        try {
            java.lang.reflect.Constructor<? extends AbsBoss> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            AbsBoss instance = constructor.newInstance();
            bossRegistry.put(instance.id, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBosses() {
        for (int id : bossRegistry.keySet()) {
            AbsBoss b = createBoss(id);
            if (b != null) {
                bosses.add(b);
            }
        }
    }

    public List<AbsBoss> getBosses() {
        return bosses;
    }

    public AbsBoss getBossById(int mob_id) {
        for (AbsBoss boss : bosses) {
            if (boss.id == mob_id) {
                return boss;
            }
        }
        AbsBoss b = createBoss(mob_id);
        if (b != null) {
            bosses.add(b);
            return b;
        }
        return null;
    }

    public AbsBoss createBoss(int mob_id) {
        Class<? extends AbsBoss> clazz = bossRegistry.get(mob_id);
        if (clazz != null) {
            try {
                java.lang.reflect.Constructor<? extends AbsBoss> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void update() {
        for (AbsBoss boss : bosses) {
            if (!boss.isdie) {
                boss.update();
            }
        }
    }
}
