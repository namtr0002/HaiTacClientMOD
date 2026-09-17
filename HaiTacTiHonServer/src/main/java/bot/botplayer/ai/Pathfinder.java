package bot.botplayer.ai;

import map.MapTemplate;
import map.Vgo;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pathfinder — Xử lý tìm đường đi giữa các map bằng BFS qua các cổng dịch chuyển (Vgo).
 *
 * Cải tiến:
 *  - Null safety cho ENTRYS và vgos
 *  - Cache kết quả BFS (ConcurrentHashMap) tránh tính lại
 *  - Thêm findTransportPath() tự động né 15 map boss_info nguy hiểm.
 */
public class Pathfinder {

    private static final List<Integer> NO_PATH_SENTINEL = Collections.singletonList(-1);

    private static final Map<String, List<Integer>> pathCache = new ConcurrentHashMap<>();
    private static final Map<String, List<Integer>> transportPathCache = new ConcurrentHashMap<>();

    // Danh sách 15 map boss_info nguy hiểm cần né khi bot vận chuyển hàng
    private static final Set<Integer> BOSS_INFO_MAPS = new HashSet<>(Arrays.asList(
        6, 14, 22, 30, 38, 46, 54, 68, 74, 82, 88, 103, 111, 126, 197
    ));

    public static boolean isBossInfoMap(int mapId) {
        return BOSS_INFO_MAPS.contains(mapId);
    }

    public static void clearCache() {
        pathCache.clear();
        transportPathCache.clear();
    }

    public static List<Integer> findPath(int startMapId, int targetMapId) {
        return findPathInternal(startMapId, targetMapId, false);
    }

    public static List<Integer> findTransportPath(int startMapId, int targetMapId) {
        return findPathInternal(startMapId, targetMapId, true);
    }

    private static List<Integer> findPathInternal(int startMapId, int targetMapId, boolean isTransport) {
        if (startMapId == targetMapId) {
            return new ArrayList<>();
        }
        if (MapTemplate.ENTRYS == null || MapTemplate.ENTRYS.isEmpty()) {
            return null;
        }

        String cacheKey = (isTransport ? "TR_" : "STD_") + startMapId + "_" + targetMapId;
        Map<String, List<Integer>> targetCache = isTransport ? transportPathCache : pathCache;

        List<Integer> cached = targetCache.get(cacheKey);
        if (cached != null) {
            if (cached == NO_PATH_SENTINEL || (cached.size() == 1 && cached.get(0) == -1)) {
                return null;
            }
            return new ArrayList<>(cached);
        }

        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> parentMap = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(startMapId);
        visited.add(startMapId);

        boolean found = false;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            if (cur == targetMapId) {
                found = true;
                break;
            }

            MapTemplate temp = null;
            for (MapTemplate t : MapTemplate.ENTRYS) {
                if (t != null && t.id == cur) {
                    temp = t;
                    break;
                }
            }

            if (temp != null && temp.vgos != null) {
                for (Vgo vgo : temp.vgos) {
                    if (vgo == null) continue;
                    int nb = vgo.id_map_go;

                    // Nếu là lộ trình vận chuyển hàng, bỏ qua các map boss_info (trừ khi đó là map đích/bắt đầu)
                    if (isTransport && nb != targetMapId && nb != startMapId && isBossInfoMap(nb)) {
                        continue;
                    }

                    if (!visited.contains(nb)) {
                        visited.add(nb);
                        parentMap.put(nb, cur);
                        queue.add(nb);
                    }
                }
            }
        }

        if (!found) {
            targetCache.put(cacheKey, NO_PATH_SENTINEL);
            return null;
        }

        List<Integer> path = new ArrayList<>();
        int cur = targetMapId;
        while (cur != startMapId) {
            path.add(0, cur);
            Integer parent = parentMap.get(cur);
            if (parent == null) break;
            cur = parent;
        }

        targetCache.put(cacheKey, new ArrayList<>(path));
        return path;
    }
}
