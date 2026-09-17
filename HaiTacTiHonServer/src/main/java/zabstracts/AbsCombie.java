package zabstracts;

import model.Player;
import java.io.IOException;

public abstract class AbsCombie {

    public static final java.util.Map<Byte, AbsCombie> COMBIE_MAP = new java.util.concurrent.ConcurrentHashMap<>();

    public static void register(AbsCombie combie) {
        if (combie != null) {
            COMBIE_MAP.put(combie.getType(), combie);
        }
    }

    public static AbsCombie get(byte type) {
        if (COMBIE_MAP.isEmpty()) {
            initRegistry();
        }
        return COMBIE_MAP.get(type);
    }

    public static void processCombie(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException {
        AbsCombie combie = get(type);
        if (combie != null) {
            p.setCombie(combie);
            combie.process(p, type, action, idItem, cat, num);
        }
    }

    public static synchronized void initRegistry() {
        if (!COMBIE_MAP.isEmpty()) return;
        java.util.List<Class<? extends AbsCombie>> classes = core.ZUtil.findSubclasses(AbsCombie.class);
        for (Class<? extends AbsCombie> clazz : classes) {
            try {
                AbsCombie instance = clazz.getDeclaredConstructor().newInstance();
                register(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected String key;

    public String getSeasonKey() {
        return key != null ? key : "Combie_" + getType();
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    public abstract byte getType();
    public abstract void process(Player p, byte type, byte action, short idItem, byte cat, short num) throws IOException;
}
