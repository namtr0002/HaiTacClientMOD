package zabstracts;

import model.Player;
import java.io.IOException;

public abstract class AbsUpgrade {

    public static final java.util.Map<Byte, AbsUpgrade> UPGRADE_MAP = new java.util.concurrent.ConcurrentHashMap<>();

    public static void register(AbsUpgrade upgrade) {
        if (upgrade != null) {
            UPGRADE_MAP.put(upgrade.getType(), upgrade);
        }
    }

    public static AbsUpgrade get(byte type) {
        if (UPGRADE_MAP.isEmpty()) {
            initRegistry();
        }
        return UPGRADE_MAP.get(type);
    }

    public static AbsUpgrade get(Class<? extends AbsUpgrade> clazz) {
        if (UPGRADE_MAP.isEmpty()) {
            initRegistry();
        }
        for (AbsUpgrade upgrade : UPGRADE_MAP.values()) {
            if (clazz.isInstance(upgrade)) {
                return upgrade;
            }
        }
        return null;
    }

    public static void processUpgrade(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException {
        AbsUpgrade upgrade = get(type);
        if (upgrade != null) {
            p.setUpgrade(upgrade);
            upgrade.process(p, type, action, idItem, cat, num);
        }
    }

    public static void showTable(Player p, byte type) throws IOException {
        AbsUpgrade upgrade = get(type);
        if (upgrade != null) {
            upgrade.showTable(p);
        }
    }

    public static void showTable(Player p, Class<? extends AbsUpgrade> clazz) throws IOException {
        AbsUpgrade upgrade = get(clazz);
        if (upgrade != null) {
            upgrade.showTable(p);
        }
    }

    public static synchronized void initRegistry() {
        if (!UPGRADE_MAP.isEmpty()) return;
        java.util.List<Class<? extends AbsUpgrade>> classes = core.ZUtil.findSubclasses(AbsUpgrade.class);
        for (Class<? extends AbsUpgrade> clazz : classes) {
            try {
                AbsUpgrade instance = clazz.getDeclaredConstructor().newInstance();
                register(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected String key;

    public String getSeasonKey() {
        String baseKey = (key != null && !key.trim().isEmpty()) ? key : ("Upgrade_" + getType());
        event.Event activeEvent = event.EventManager.gI().getActiveEvent();
        if (activeEvent != null && activeEvent.getSeasonKey() != null && !activeEvent.getSeasonKey().trim().isEmpty()) {
            return historys.HistoryManager.formatKey(baseKey, activeEvent.getSeasonKey());
        }
        return baseKey;
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    public abstract byte getType();
    public abstract void process(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException;
    public abstract void showTable(Player p) throws IOException;

    public void show_table(Player p) throws IOException {
        showTable(p);
    }
}
