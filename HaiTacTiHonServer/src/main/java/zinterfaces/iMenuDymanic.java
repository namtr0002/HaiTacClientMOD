package zinterfaces;

import model.Player;
import java.io.IOException;
import java.util.*;

/**
 * iMenuDymanic — Hệ thống Dynamic Menu tự động gắn vào NPC.
 */
public interface iMenuDymanic {

    enum Position {
        TOP,
        BOTTOM,
        INDEX
    }

    class DynamicEntry {
        public final String       name;
        public final short        menuId;
        public final short        icon;
        public final iMenuDymanic handler;

        public DynamicEntry(String name, short menuId, short icon, iMenuDymanic handler) {
            this.name    = name;
            this.menuId  = menuId;
            this.icon    = icon;
            this.handler = handler;
        }
    }

    class MenuSnapshot {
        public final boolean       isDynamic;
        public final int           baseIndex;
        public final DynamicEntry  entry;

        public MenuSnapshot(int baseIndex) {
            this.isDynamic = false;
            this.baseIndex = baseIndex;
            this.entry     = null;
        }

        public MenuSnapshot(DynamicEntry entry) {
            this.isDynamic = true;
            this.baseIndex = -1;
            this.entry     = entry;
        }
    }

    short[] getNpcId();

    String[] getMenuNames();

    default int getEventId() { return -1; }

    default short[] getIcons() { return null; }

    default Position getPosition() { return Position.BOTTOM; }

    default int getPositionIndex() { return 0; }

    default boolean shouldShow(Player p) { return true; }

    void handleSelect(Player p, short npcId, short menuId) throws IOException;

    Map<Short, List<DynamicEntry>> NPC_REGISTRY = new LinkedHashMap<>();

    static void init() {
        synchronized (NPC_REGISTRY) {
            if (!NPC_REGISTRY.isEmpty()) return;
            java.util.List<Class<? extends iMenuDymanic>> classes = core.ZUtil.findSubclasses(iMenuDymanic.class);
            for (Class<? extends iMenuDymanic> clazz : classes) {
                try {
                    java.lang.reflect.Constructor<?> ctor = clazz.getDeclaredConstructor();
                    ctor.setAccessible(true);
                    iMenuDymanic dyn = (iMenuDymanic) ctor.newInstance();
                    register(dyn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static void register(iMenuDymanic dyn) {
        String[] names  = dyn.getMenuNames();
        short[]  icons  = dyn.getIcons();

        for (short npcId : dyn.getNpcId()) {
            List<DynamicEntry> entries = NPC_REGISTRY.computeIfAbsent(npcId, k -> new ArrayList<>());
            for (int i = 0; i < names.length; i++) {
                String name  = (names != null && i < names.length) ? names[i] : "?";
                short  icon  = (icons != null && i < icons.length) ? icons[i] : -1;
                entries.add(new DynamicEntry(name, (short) i, icon, dyn));
            }
        }
    }

    static List<DynamicEntry> getActiveFor(short npcId) {
        List<DynamicEntry> all = NPC_REGISTRY.getOrDefault(npcId, Collections.emptyList());
        List<DynamicEntry> active = new ArrayList<>();
        for (DynamicEntry e : all) {
            if (e.handler.getEventId() == -1
                    || event.EventManager.isActive(e.handler.getEventId())) {
                active.add(e);
            }
        }
        return active;
    }

    /**
     * Helper tương thích ngược: gửi menu động với npcId kiểu int.
     * Tương đương p.getService().openDynamicMenu(npcId, title, options, icons).
     */
    static void buildAndSend(Player p, int npcId, String title, String[] options, short[] icons) throws IOException {
        p.getService().openDynamicMenu(npcId, title, options, icons);
    }

    /**
     * Overload với npcId kiểu short (các file cũ dùng short).
     */
    static void buildAndSend(Player p, short npcId, String title, String[] options, short[] icons) throws IOException {
        p.getService().openDynamicMenu((int) npcId, title, options, icons);
    }
}
