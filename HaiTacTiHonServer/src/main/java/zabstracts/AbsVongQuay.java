package zabstracts;

import model.Player;
import network.Message;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public abstract class AbsVongQuay {
    protected int id;
    protected String name;
    protected String key;

    public String getSeasonKey() {
        String baseKey = (key != null && !key.trim().isEmpty()) ? key : ("VongQuay_" + id);
        event.Event activeEvent = event.EventManager.gI().getActiveEvent();
        if (activeEvent != null && activeEvent.getSeasonKey() != null && !activeEvent.getSeasonKey().trim().isEmpty()) {
            return historys.HistoryManager.formatKey(baseKey, activeEvent.getSeasonKey());
        }
        return baseKey;
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    private static final Map<Integer, AbsVongQuay> vongQuays = new HashMap<>();

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        if (!vongQuays.isEmpty()) return;
        java.util.List<Class<? extends AbsVongQuay>> classes = core.ZUtil.findSubclasses(AbsVongQuay.class);
        for (Class<? extends AbsVongQuay> clazz : classes) {
            try {
                if (java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) continue;
                java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                AbsVongQuay instance = (AbsVongQuay) constructor.newInstance();
                if (instance != null) {
                    vongQuays.put(instance.getId(), instance);
                }
            } catch (NoSuchMethodException ignored) {
                // Class không có no-arg constructor, bỏ qua
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static AbsVongQuay get(int id) {
        return vongQuays.get(id);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract void showTable(Player p) throws IOException;
    public abstract void process(Player p, byte action, Message m) throws IOException;
}
