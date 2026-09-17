package zabstracts;

import model.Player;
import network.Message;
import java.io.IOException;

public abstract class AbsUpgradeDevil {

    public static final java.util.Map<Integer, AbsUpgradeDevil> UPGRADE_DEVIL_MAP = new java.util.concurrent.ConcurrentHashMap<>();

    public static void register(AbsUpgradeDevil handler) {
        if (handler != null) {
            UPGRADE_DEVIL_MAP.put(handler.getId(), handler);
        }
    }

    public static AbsUpgradeDevil get(int id) {
        if (UPGRADE_DEVIL_MAP.isEmpty()) {
            initRegistry();
        }
        return UPGRADE_DEVIL_MAP.get(id);
    }

    public static void showTable(Player p, int id) throws IOException {
        AbsUpgradeDevil handler = get(id);
        if (handler != null) {
            p.setUpgradeDevil(handler);
            handler.show_table(p);
        }
    }

    public static synchronized void initRegistry() {
        if (!UPGRADE_DEVIL_MAP.isEmpty()) return;
        java.util.List<Class<? extends AbsUpgradeDevil>> classes = core.ZUtil.findSubclasses(AbsUpgradeDevil.class);
        for (Class<? extends AbsUpgradeDevil> clazz : classes) {
            try {
                AbsUpgradeDevil instance = clazz.getDeclaredConstructor().newInstance();
                register(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected String key;

    public String getSeasonKey() {
        String baseKey = (key != null && !key.trim().isEmpty()) ? key : ("UpgradeDevil_" + getId());
        event.Event activeEvent = event.EventManager.gI().getActiveEvent();
        if (activeEvent != null && activeEvent.getSeasonKey() != null && !activeEvent.getSeasonKey().trim().isEmpty()) {
            return historys.HistoryManager.formatKey(baseKey, activeEvent.getSeasonKey());
        }
        return baseKey;
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    public abstract int getId();
    public abstract byte getClientType();
    public abstract void show_table(Player p) throws IOException;
    public abstract boolean checkProcess(Player p, byte action, short id, byte cat, short num) throws IOException;
    public abstract void process(Player p, byte action, short id, byte cat, short num) throws IOException;

    public void process(Player p, Message m2) throws IOException {
        // Default implementation for dispatcher/backward compatibility
    }
}
