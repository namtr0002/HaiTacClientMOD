package map;

import model.Player;
import map.Zone;
import mob.Mob;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class MapBossInfo {
    public static List<MapBossInfo> ENTRY = new CopyOnWriteArrayList<>();
    public Zone map;
    public short x_new, y_new;
    public List<Mob> mob = new CopyOnWriteArrayList<>();

    public static Mob get_mob(Player p, int id) {
        for (int i = 0; i < MapBossInfo.ENTRY.size(); i++) {
            for (int k = 0; k < MapBossInfo.ENTRY.get(i).mob.size(); k++) {
                if (MapBossInfo.ENTRY.get(i).mob.get(k).map.equals(p.map) && MapBossInfo.ENTRY.get(i).mob.get(k).index == id) {
                    return MapBossInfo.ENTRY.get(i).mob.get(k);
                }
            }
        }
        return null;
    }

    public synchronized static void add(MapBossInfo t) {
        MapBossInfo.ENTRY.add(t);
    }

    public synchronized static void remove(MapBossInfo t) {
        MapBossInfo.ENTRY.remove(t);
    }

    public static List<Mob> get_list_mob(Zone map) {
        for (int i = 0; i < MapBossInfo.ENTRY.size(); i++) {
            if (MapBossInfo.ENTRY.get(i).map.equals(map)) {
                return MapBossInfo.ENTRY.get(i).mob;
            }
        }
        return null;
    }
}
