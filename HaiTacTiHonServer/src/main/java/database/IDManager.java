package database;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import core.Log;
import network.SessionManager;

public class IDManager {
    public static ConcurrentHashMap<Short, Boolean> idShipPet;
    public static ConcurrentHashMap<Short, Boolean> idSanta;
    public static ConcurrentHashMap<Short, Boolean> idFakeBot;
    public static ConcurrentHashMap<Short, Boolean> idBotKhoBau;

    private static final ConcurrentLinkedQueue<Short> poolShipPet = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Short> poolSanta = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Short> poolFakeBot = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Short> poolBotKhoBau = new ConcurrentLinkedQueue<>();

    private static final AtomicInteger cursorShipPet = new AtomicInteger(0);
    private static final AtomicInteger cursorSanta = new AtomicInteger(0);
    private static final AtomicInteger cursorFakeBot = new AtomicInteger(0);
    private static final AtomicInteger cursorBotKhoBau = new AtomicInteger(0);

    public static final int SHIP_PET = 0;
    public static final int SAN_TA = 1;
    public static final int FAKE_BOT = 3;
    public static final int BOT_KHO_BAU = 4;

    // Ranges
    private static final int BASE_SHIP_PET = -20000;
    private static final int SIZE_SHIP_PET = 1000; // -20000 .. -19001

    private static final int BASE_SAN_TA = -19000;
    private static final int SIZE_SAN_TA = 1000; // -19000 .. -18001

    private static final int BASE_FAKE_BOT = -31000;
    private static final int SIZE_FAKE_BOT = 10000; // -31000 .. -21001 (10,000 IDs)

    private static final int BASE_BOT_KHO_BAU = -32000;
    private static final int SIZE_BOT_KHO_BAU = 1000; // -32000 .. -31001

    public static void init() {
        idShipPet = new ConcurrentHashMap<>();
        poolShipPet.clear();
        cursorShipPet.set(0);
        for (short i = BASE_SHIP_PET; i < BASE_SHIP_PET + SIZE_SHIP_PET; i++) {
            idShipPet.put(i, true);
            poolShipPet.offer(i);
        }

        idSanta = new ConcurrentHashMap<>();
        poolSanta.clear();
        cursorSanta.set(0);
        for (short i = BASE_SAN_TA; i < BASE_SAN_TA + SIZE_SAN_TA; i++) {
            idSanta.put(i, true);
            poolSanta.offer(i);
        }

        idFakeBot = new ConcurrentHashMap<>();
        poolFakeBot.clear();
        cursorFakeBot.set(0);
        for (short i = BASE_FAKE_BOT; i < BASE_FAKE_BOT + SIZE_FAKE_BOT; i++) {
            idFakeBot.put(i, true);
            poolFakeBot.offer(i);
        }

        idBotKhoBau = new ConcurrentHashMap<>();
        poolBotKhoBau.clear();
        cursorBotKhoBau.set(0);
        for (short i = BASE_BOT_KHO_BAU; i < BASE_BOT_KHO_BAU + SIZE_BOT_KHO_BAU; i++) {
            idBotKhoBau.put(i, true);
            poolBotKhoBau.offer(i);
        }
        Log.success("IDManager", "Initialized dynamic ID pools (Ship/Pet: " + SIZE_SHIP_PET + ", Santa: " + SIZE_SAN_TA + ", FakeBot: " + SIZE_FAKE_BOT + ", BotKhoBau: " + SIZE_BOT_KHO_BAU + ")");
    }

    public static short takeID(int type) {
        ConcurrentLinkedQueue<Short> pool = getPool(type);
        if (pool != null) {
            Short id = pool.poll();
            if (id != null) {
                ConcurrentHashMap<Short, Boolean> map = get(type);
                if (map != null) map.remove(id);
                return id;
            }
            // Seamless dynamic allocation with active session collision avoidance
            return getFallbackID(type);
        }
        return -1;
    }

    public static void putID(short id, int type) {
        ConcurrentLinkedQueue<Short> pool = getPool(type);
        if (pool != null) {
            if (!pool.contains(id)) {
                pool.offer(id);
                ConcurrentHashMap<Short, Boolean> map = get(type);
                if (map != null) map.put(id, true);
            }
        }
    }

    public static ConcurrentHashMap<Short, Boolean> get(int type) {
        switch (type) {
            case SHIP_PET:
                return idShipPet;
            case SAN_TA:
                return idSanta;
            case FAKE_BOT:
                return idFakeBot;
            case BOT_KHO_BAU:
                return idBotKhoBau;
            default:
                return null;
        }
    }

    private static ConcurrentLinkedQueue<Short> getPool(int type) {
        switch (type) {
            case SHIP_PET:
                return poolShipPet;
            case SAN_TA:
                return poolSanta;
            case FAKE_BOT:
                return poolFakeBot;
            case BOT_KHO_BAU:
                return poolBotKhoBau;
            default:
                return null;
        }
    }

    private static short getFallbackID(int type) {
        int base;
        int size;
        AtomicInteger cursor;

        switch (type) {
            case SHIP_PET:
                base = BASE_SHIP_PET;
                size = SIZE_SHIP_PET;
                cursor = cursorShipPet;
                break;
            case SAN_TA:
                base = BASE_SAN_TA;
                size = SIZE_SAN_TA;
                cursor = cursorSanta;
                break;
            case FAKE_BOT:
                base = BASE_FAKE_BOT;
                size = SIZE_FAKE_BOT;
                cursor = cursorFakeBot;
                break;
            case BOT_KHO_BAU:
                base = BASE_BOT_KHO_BAU;
                size = SIZE_BOT_KHO_BAU;
                cursor = cursorBotKhoBau;
                break;
            default:
                return -1;
        }

        // Rotate through the allocated ID space and pick an ID not currently active in PLAYERS_BY_INDEX
        for (int attempt = 0; attempt < 50; attempt++) {
            int offset = Math.abs(cursor.getAndIncrement() % size);
            short candidate = (short) (base + offset);
            if (SessionManager.PLAYERS_BY_INDEX == null || !SessionManager.PLAYERS_BY_INDEX.containsKey((int) candidate)) {
                return candidate;
            }
        }

        // Final fallback if all attempts were busy
        int finalOffset = Math.abs(cursor.getAndIncrement() % size);
        return (short) (base + finalOffset);
    }
}