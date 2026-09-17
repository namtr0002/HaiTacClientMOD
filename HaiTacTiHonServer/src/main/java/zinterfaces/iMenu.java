package zinterfaces;

import model.Player;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface iMenu {
    short[] getId();
    void handleMenu(Player p, short idNPC, int index) throws IOException;

    Map<Short, iMenu> MENUS = new HashMap<>();

    static void init() {
        synchronized (MENUS) {
            if (!MENUS.isEmpty()) return;
            java.util.List<Class<? extends iMenu>> classes = core.ZUtil.findSubclasses(iMenu.class);
            for (Class<? extends iMenu> clazz : classes) {
                try {
                    java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    iMenu menu = (iMenu) constructor.newInstance();
                    register(menu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void register(iMenu menu) {
        for (short id : menu.getId()) {
            MENUS.put(id, menu);
        }
    }

    static iMenu get(short id) {
        synchronized (MENUS) {
            if (MENUS.isEmpty()) {
                init();
            }
            return MENUS.get(id);
        }
    }
}
