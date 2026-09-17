package activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.Player;
import core.ZUtil;
import network.Message;

public class Wanted {
    private static final List<Player> LIST = new ArrayList<>();
    private static final Map<Player, Long> WAIT_TIMESTAMPS = new ConcurrentHashMap<>();
    public static final long MATCHMAKING_TIMEOUT_MS = 12_000L; // 12 seconds wait for real player before fallback to bot

    public static void show_table(Player p) throws IOException {
        Message m = new Message(-85);
        m.writer().writeByte(0);
        p.addmsg(m);
        m.cleanup();
    }

    public synchronized static void add_player_wait(Player p) {
        if (p == null) return;
        if (!LIST.contains(p)) {
            LIST.add(p);
            WAIT_TIMESTAMPS.put(p, System.currentTimeMillis());
        }
    }

    public synchronized static void remove_player_wait(Player p) {
        if (p == null) return;
        LIST.remove(p);
        WAIT_TIMESTAMPS.remove(p);
    }

    public synchronized static Player[] get_p_random_waiting() throws IOException {
        Player[] result = new Player[] {null, null};
        // Clean disconnected players
        LIST.removeIf(p -> p == null || (!p.isBot && p.conn == null));

        if (LIST.isEmpty()) {
            return result;
        }

        long now = System.currentTimeMillis();

        // Priority 1: Real online player vs Real online player matching first
        for (int i = 0; i < LIST.size(); i++) {
            Player p0 = LIST.get(i);
            if (p0 == null || p0.isBot || p0.conn == null) continue;

            int maxLevelDiff0 = Math.max(3, (int) (p0.level * 0.15));

            for (int j = i + 1; j < LIST.size(); j++) {
                Player p1 = LIST.get(j);
                if (p1 == null || p1.isBot || p1.conn == null) continue;

                int maxLevelDiff1 = Math.max(3, (int) (p1.level * 0.15));
                int maxAllowedDiff = Math.max(maxLevelDiff0, maxLevelDiff1);

                if (Math.abs(p0.level - p1.level) <= maxAllowedDiff) {
                    wait_to_enter_round(p0);
                    wait_to_enter_round(p1);
                    result[0] = p0;
                    result[1] = p1;
                    return result;
                }
            }
        }

        // Priority 2: Check waiting real player timeout before falling back to Bot
        Player timedOutRealPlayer = null;
        for (Player p : LIST) {
            if (p != null && !p.isBot && p.conn != null) {
                long waitTime = now - WAIT_TIMESTAMPS.getOrDefault(p, now);
                if (waitTime >= MATCHMAKING_TIMEOUT_MS) {
                    timedOutRealPlayer = p;
                    break;
                }
            }
        }

        if (timedOutRealPlayer != null) {
            Player p0 = timedOutRealPlayer;
            Player p1 = null;
            int maxLevelDiff = Math.max(3, (int) (p0.level * 0.15));

            // Priority 2a: Active online bot waiting in queue (BotPlayerReal)
            for (Player p2 : LIST) {
                if (p2 != null && p2.isBot && p2 != p0 && Math.abs(p0.level - p2.level) <= maxLevelDiff) {
                    p1 = p2;
                    break;
                }
            }

            // Priority 2b: Fast RAM Cache PvP Bot lookup (BotPvpCacheManager)
            if (p1 == null) {
                p1 = bot.BotPVP.findOfflineSqlBot(p0);
            }

            // Priority 2c: Dynamic Analyzed Bot creation (with stats matching p0)
            if (p1 == null) {
                p1 = bot.BotPVP.createAnalyzedBot(database.IDManager.takeID(database.IDManager.FAKE_BOT), p0);
            }

            if (p1 != null) {
                wait_to_enter_round(p0);
                if (!p1.isBot) {
                    wait_to_enter_round(p1);
                } else {
                    LIST.remove(p1);
                    WAIT_TIMESTAMPS.remove(p1);
                }
                result[0] = p0;
                result[1] = p1;
                return result;
            }
        }

        // Priority 3: If NO real players are waiting in queue, allow waiting bots to match
        boolean hasAnyRealPlayer = false;
        for (Player p : LIST) {
            if (p != null && !p.isBot && p.conn != null) {
                hasAnyRealPlayer = true;
                break;
            }
        }
        if (!hasAnyRealPlayer && LIST.size() >= 2) {
            Player b0 = LIST.get(0);
            Player b1 = LIST.get(1);
            if (b0 != null && b1 != null && b0.isBot && b1.isBot) {
                wait_to_enter_round(b0);
                wait_to_enter_round(b1);
                result[0] = b0;
                result[1] = b1;
                return result;
            }
        }

        return result;
    }

    private static void wait_to_enter_round(Player p) throws IOException {
        LIST.remove(p);
        WAIT_TIMESTAMPS.remove(p);
        if (!p.isBot && p.conn != null) {
            Message m = new Message(-85);
            m.writer().writeByte(2);
            p.addmsg(m);
            m.cleanup();
        }
    }
}
