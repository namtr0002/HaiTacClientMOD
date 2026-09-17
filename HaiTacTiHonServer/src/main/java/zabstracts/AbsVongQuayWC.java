package zabstracts;

import model.Player;
import network.Message;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbsVongQuayWC {
    protected int id = 82;
    protected String name = "Vòng Quay World Cup";
    protected String key = "VongQuayWC";

    private static final Map<Integer, AbsVongQuayWC> vongQuays = new HashMap<>();

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        if (!vongQuays.isEmpty()) return;
        java.util.List<Class<? extends AbsVongQuayWC>> classes = core.ZUtil.findSubclasses(AbsVongQuayWC.class);
        for (Class<? extends AbsVongQuayWC> clazz : classes) {
            try {
                java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                AbsVongQuayWC instance = (AbsVongQuayWC) constructor.newInstance();
                vongQuays.put(instance.getId(), instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static AbsVongQuayWC get(int id) {
        return vongQuays.get(id);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract void showTable(Player p) throws IOException;
    public abstract void sendListItems(Player p) throws IOException;
    public abstract void sendInfo(Player p) throws IOException;
    public abstract void processQuay(Player p, byte typeQuay) throws IOException;
    public abstract void process(Player p, byte action, Message m) throws IOException;
}