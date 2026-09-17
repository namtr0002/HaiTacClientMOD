package map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapManager {
    private static MapManager instance;
    private final List<map.Map> maps;
    private final Map<Integer, map.Map> mapByTemplateId;
    private final Set<Zone> activeZones;
    private ScheduledExecutorService executorService;

    private MapManager() {
        this.maps = new ArrayList<>();
        this.mapByTemplateId = new ConcurrentHashMap<>();
        this.activeZones = ConcurrentHashMap.newKeySet();
    }

    public void registerActiveZone(Zone zone) {
        if (zone != null) {
            zone.setRunning(true);
            activeZones.add(zone);
        }
    }

    public void unregisterActiveZone(Zone zone) {
        if (zone != null) {
            activeZones.remove(zone);
        }
    }

    public Set<Zone> getActiveZones() {
        return activeZones;
    }

    public void start() {
        if (executorService != null) return;
        
        // Attach static map dungeons
        for (int mapId = 272; mapId <= 275; mapId++) {
            Zone[] zonesWar = Zone.getMapByID(mapId);
            if (zonesWar != null) {
                for (Zone z : zonesWar) {
                    if (z != null) {
                        z.map_dungeon = new map.zones.TranChienLon();
                    }
                }
            }
        }

        // Initialize multi-threaded scheduled executor for zone updates
        int poolSize = Math.max(4, Runtime.getRuntime().availableProcessors());
        executorService = Executors.newScheduledThreadPool(
            poolSize,
            r -> {
                Thread t = new Thread(r, "HTTH-Map-Worker");
                t.setDaemon(true);
                return t;
            }
        );
        
        java.util.concurrent.atomic.AtomicLong tickCounter = new java.util.concurrent.atomic.AtomicLong(0);

        // High-frequency map update loop running @ 100ms interval (10 FPS server tickrate)
        executorService.scheduleAtFixedRate(() -> {
            try {
                long currentTick = tickCounter.incrementAndGet();
                boolean isIdleTick = (currentTick % 10 == 0); // Every 1000ms (1s) tick idle zones

                // 1. Dispatch active zones in parallel (chỉ chạy khi có người chơi thật hoặc special match đang diễn ra)
                for (Zone zone : activeZones) {
                    if (zone != null) {
                        if (!zone.hasRealPlayer() && !zone.isSpecialActive()) {
                            activeZones.remove(zone);
                            continue;
                        }
                        zone.setRunning(true);
                        executorService.execute(() -> {
                            try {
                                zone.update();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }

                // 2. Dynamic map zones (MAP_PLUS)
                List<Zone> dynamicZones;
                synchronized (Zone.get_map_plus()) {
                    dynamicZones = new ArrayList<>(Zone.get_map_plus());
                }
                for (Zone zone : dynamicZones) {
                    if (zone != null) {
                        if (!zone.isRun()) {
                            zone.stop_map();
                            synchronized (Zone.get_map_plus()) {
                                Zone.get_map_plus().remove(zone);
                            }
                            continue;
                        }
                        if (!activeZones.contains(zone)) {
                            if (zone.hasRealPlayer() || zone.isSpecialActive()) {
                                activeZones.add(zone);
                                executorService.execute(() -> {
                                    try {
                                        zone.update();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            } else if (isIdleTick) {
                                executorService.execute(() -> {
                                    try {
                                        zone.update_idle();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                    }
                }

                // 3. Quét static zones mỗi 1s:
                // - Nếu có người chơi thật: tự động đồng bộ vào activeZones
                // - Nếu không có người chơi thật: chạy update_idle() bảo toàn HP quái/boss và hồi sinh
                if (isIdleTick) {
                    for (int i = 0; i < maps.size(); i++) {
                        map.Map map = maps.get(i);
                        if (map != null && map.zones != null) {
                            for (int j = 0; j < map.zones.length; j++) {
                                Zone zone = map.zones[j];
                                if (zone != null) {
                                    zone.setRunning(true); // Đảm bảo static map luôn running, không bao giờ bị close
                                    if (zone.hasRealPlayer() || zone.isSpecialActive()) {
                                        if (!activeZones.contains(zone)) {
                                            activeZones.add(zone);
                                        }
                                    } else {
                                        if (activeZones.contains(zone)) {
                                            activeZones.remove(zone);
                                        }
                                        executorService.execute(() -> {
                                            try {
                                                zone.update_idle();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
        
        core.Log.success("MapManager", "Ultra-smooth Map Engine initialized (Active @ 100ms / Idle @ 1000ms, pool=" + poolSize + ")");
    }

    public void startMapPlus(Zone zone) {
        if (zone != null) {
            zone.setRunning(true);
            zone.start_map();
            registerActiveZone(zone);
        }
    }

    public void stop() {
        if (executorService != null) {
            try {
                executorService.shutdown();
                if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (Exception ignored) {
                executorService.shutdownNow();
            }
            executorService = null;
        }
        activeZones.clear();
    }

    public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }

    public List<map.Map> getMaps() {
        return maps;
    }

    public void addMap(map.Map map) {
        this.maps.add(map);
        if (map != null && map.template != null) {
            this.mapByTemplateId.put(map.template.id, map);
        }
    }
    
    public map.Map getMap(int id) {
        if (id >= 0 && id < maps.size()) {
            return maps.get(id);
        }
        return null;
    }

    public map.Map getMapByTemplateId(int templateId) {
        return this.mapByTemplateId.get(templateId);
    }
}


