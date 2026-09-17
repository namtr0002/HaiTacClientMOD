package zinterfaces;

import model.Player;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import map.Npc;
import map.Zone;

/**
 * iNpc — Interface cho mọi NPC handler.
 *
 * Auto-discovery: scan package interfaces.npcmenus, đăng ký tự động.
 *
 * Dynamic menu & Npc init override:
 *   Override onInitNpcForMap(Zone zone) để tự động thêm/cài đặt NPC vào map linh hoạt.
 */
public interface iNpc {
    short[] getId();
    void sendMenu(Player p, Npc npc) throws IOException;
    default void handleMenu(Player p, int index) throws IOException {
        if (p.menus != null && index >= 0 && index < p.menus.size()) {
            p.menus.get(index).execute(p, index);
        }
    }
    default void handleMenu(Player p, int menuId, int index) throws IOException {
        handleMenu(p, index);
    }
    
    /**
     * Tùy chọn override để tự động khởi tạo/thêm NPC động vào map.
     * Ví dụ:
     * if (zone.template.id == 20) {
     *     zone.template.npcs.add(iNpc.createNpc((short) -106, "Zosaku Hỗ Trợ", "Hướng Dẫn Viên", "Xin chào!", 300, 300, 0, 1, new short[]{-1,-1,-1,-1}));
     * }
     */
    default void onInitNpcForMap(Zone zone) {
    }

    /**
     * Tạo một đối tượng Npc đầy đủ thông tin chuẩn như NPC trong game.
     */
    static Npc createNpc(
            short idmenu, 
            String name, 
            String namegt, 
            String chat, 
            short x, short y,
            byte isPerson, byte typeIcon, byte wBlock, byte hBlock,
            short head, short hair, short[] wearing) {
        Npc npc = new Npc();
        npc.idmenu = idmenu;
        npc.name = name != null ? name : "";
        npc.namegt = namegt != null ? namegt : "";
        npc.chat = chat != null ? chat : "";
        npc.x = x;
        npc.y = y;
        npc.isPerson = isPerson;
        npc.typeIcon = typeIcon;
        npc.wBlock = wBlock > 0 ? wBlock : 20;
        npc.hBlock = hBlock > 0 ? hBlock : 20;
        npc.b3 = 0;
        npc.head = head;
        npc.hair = hair;
        npc.wearing = wearing != null ? wearing : new short[]{-1, -1, -1, -1};
        npc.dataFrame = new byte[]{(byte) (head > 0 ? head : 1), (byte) (hair > 0 ? hair : 2)};
        return npc;
    }

    /** Overload tiện ích ngắn gọn cho Npc thông thường */
    static Npc createNpc(short idmenu, String name, String namegt, String chat, short x, short y, short head, short hair, short[] wearing) {
        return createNpc(idmenu, name, namegt, chat, x, y, (byte) 1, (byte) 1, (byte) 20, (byte) 20, head, hair, wearing);
    }

    default String getChatText() {
        return null;
    }

    default String[] getChatTexts() {
        for (short id : getId()) {
            String[] cfg = NPC_CHAT_CONFIGS.get(id);
            if (cfg != null) {
                return cfg;
            }
        }
        return null;
    }

    Map<Short, String[]> NPC_CHAT_CONFIGS = new java.util.concurrent.ConcurrentHashMap<>();

    static void configureChat(short npcId, String[] chats) {
        NPC_CHAT_CONFIGS.put(npcId, chats);
    }

    static String[] getChatConfig(short npcId) {
        return NPC_CHAT_CONFIGS.get(npcId);
    }
    
    Map<Short, iNpc> NPCS = new HashMap<>();

    static void init() {
        synchronized (NPCS) {
            if (!NPCS.isEmpty()) return;
            java.util.List<Class<? extends iNpc>> classes = core.ZUtil.findSubclasses(iNpc.class);
            for (Class<? extends iNpc> clazz : classes) {
                try {
                    java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    iNpc npc = (iNpc) constructor.newInstance();
                    register(npc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            iMenuDymanic.init();
        }
    }

    private static void register(iNpc npc) {
        for (short id : npc.getId()) {
            NPCS.put(id, npc);
        }
    }

    static iNpc get(short id) {
        synchronized (NPCS) {
            if (NPCS.isEmpty()) {
                init();
            }
            return NPCS.get(id);
        }
    }

    /**
     * Dispatch gọi tất cả iNpc handlers để tự động thêm NPC động vào map khi được khởi tạo.
     */
    static void dispatchInitNpcsForMap(Zone zone) {
        if (zone == null || zone.template == null) return;
        synchronized (NPCS) {
            if (NPCS.isEmpty()) {
                init();
            }
            java.util.Set<iNpc> uniqueHandlers = new java.util.HashSet<>(NPCS.values());
            for (iNpc handler : uniqueHandlers) {
                if (handler != null) {
                    try {
                        handler.onInitNpcForMap(zone);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
