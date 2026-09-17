package database;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import core.Log;

public class CacheManager {
    private static final CacheManager instance = new CacheManager();
    public static CacheManager gI() {
        return instance;
    }

    private static final String CACHE_DIR = "./.cache/db";
    private static final byte XOR_KEY = (byte) 0x6A;
    
    private int targetMaxMemEntries = 1000;
    private int maxMemEntries = 1000;

    // Thread-safe LRU Cache for memory entries with auto-spill to SSD disk cache
    private final Map<String, Map<String, Object>> memoryCache = new LinkedHashMap<String, Map<String, Object>>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Map<String, Object>> eldest) {
            if (size() > maxMemEntries) {
                // Auto-offload evicted RAM entry to SSD disk cache before purging from memory
                if (core.Manager.gI().cache_enable_ssd && eldest != null && eldest.getValue() != null) {
                    try {
                        File file = new File(CACHE_DIR, sanitizeKey(eldest.getKey()) + ".dat");
                        CacheFileCodec.saveJsonMap(file, eldest.getValue(), XOR_KEY);
                    } catch (Exception ignored) {}
                }
                return true;
            }
            return false;
        }
    };

    private boolean initialized = false;

    public synchronized void init() {
        if (initialized) return;
        initialized = true;

        int configMax = core.Manager.gI().cache_max_ram_entries;
        if (configMax > 0) {
            targetMaxMemEntries = configMax;
            Log.info("CacheManager", "RAM cache target limit configured: " + targetMaxMemEntries + " entries");
        } else {
            // Determine machine strength based on JVM memory limits
            long maxMemory = Runtime.getRuntime().maxMemory();
            if (maxMemory < 512L * 1024 * 1024) { // < 512 MB
                targetMaxMemEntries = 200;
            } else if (maxMemory < 1024L * 1024 * 1024) { // < 1 GB
                targetMaxMemEntries = 500;
            } else if (maxMemory < 4096L * 1024 * 1024) { // < 4 GB
                targetMaxMemEntries = 2000;
            } else { // >= 4 GB
                targetMaxMemEntries = 5000;
            }
            Log.info("CacheManager", "RAM cache limit set to " + targetMaxMemEntries + " entries (JVM Max: " + (maxMemory / 1024 / 1024) + " MB)");
        }
        maxMemEntries = targetMaxMemEntries;

        try {
            if (core.Manager.gI().cache_enable_ssd) {
                File dir = new File(CACHE_DIR);
                if (dir.exists()) {
                    cleanDirectory(dir);
                }
                dir.mkdirs();
                Log.success("CacheManager", "Initialized DB cache directory: " + CACHE_DIR);
            } else {
                Log.info("CacheManager", "SSD Cache storage fallback is disabled.");
            }
            
            // Start background monitor thread
            startMonitorThread();
        } catch (Exception e) {
            initialized = false;
            Log.error("CacheManager", "Failed to initialize CacheManager", e);
        }
    }

    private void cleanDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    cleanDirectory(f);
                }
                f.delete();
            }
        }
    }

    private void startMonitorThread() {
        Thread monitor = new Thread(new Runnable() {
            @Override
            public void run() {
                //System.out.println("[CacheManager] Background Monitor Thread started.");
                while (initialized) {
                    try {
                        Thread.sleep(60000); // Check every 60 seconds
                        checkAndCleanMemory();
                        checkAndCleanDisk();
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e) {
                        System.err.println("[CacheManager] Error in monitor thread: " + e.getMessage());
                    }
                }
            }
        });
        monitor.setName("CacheManager-Monitor");
        monitor.setDaemon(true);
        monitor.start();
    }

    private void checkAndCleanMemory() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        double freePercent = (double) (maxMemory - usedMemory) / maxMemory;

        if (freePercent < 0.20) {
            System.out.println("[CacheManager] Low JVM free memory (" + String.format("%.2f", freePercent * 100) + "%). Offloading RAM entries to SSD...");
            
            maxMemEntries = targetMaxMemEntries / 2;
            if (maxMemEntries < 50) maxMemEntries = 50;

            synchronized (this) {
                int toEvict = memoryCache.size() - maxMemEntries;
                if (toEvict > 0) {
                    Iterator<Map.Entry<String, Map<String, Object>>> it = memoryCache.entrySet().iterator();
                    while (it.hasNext() && toEvict > 0) {
                        Map.Entry<String, Map<String, Object>> entry = it.next();
                        // Offload to SSD before removing from RAM
                        if (core.Manager.gI().cache_enable_ssd && entry.getValue() != null) {
                            try {
                                File file = new File(CACHE_DIR, sanitizeKey(entry.getKey()) + ".dat");
                                CacheFileCodec.saveJsonMap(file, entry.getValue(), XOR_KEY);
                            } catch (Exception ignored) {}
                        }
                        it.remove();
                        toEvict--;
                    }
                }
            }
        } else if (freePercent > 0.40 && maxMemEntries < targetMaxMemEntries) {
            maxMemEntries = targetMaxMemEntries;
            System.out.println("[CacheManager] JVM memory recovered (" + String.format("%.2f", freePercent * 100) + "%). Restored cache limit to " + maxMemEntries);
        }
    }

    private void checkAndCleanDisk() {
        if (!core.Manager.gI().cache_enable_ssd) return;
        File dir = new File(CACHE_DIR);
        if (!dir.exists()) return;

        File[] files = dir.listFiles();
        if (files == null) return;

        long totalSize = 0;
        for (File f : files) {
            if (f.isFile()) {
                totalSize += f.length();
            }
        }

        // Cap at 2 GB
        long limit = 2L * 1024 * 1024 * 1024;
        long target = 1500L * 1024 * 1024; // Trim down to 1.5 GB

        if (totalSize > limit) {
            System.out.println("[CacheManager] SSD Cache size (" + (totalSize / 1024 / 1024) + " MB) exceeded limit. Cleaning oldest files...");
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    return Long.compare(f1.lastModified(), f2.lastModified());
                }
            });

            for (File f : files) {
                if (f.isFile()) {
                    long size = f.length();
                    if (f.delete()) {
                        totalSize -= size;
                        if (totalSize <= target) {
                            break;
                        }
                    }
                }
            }
            System.out.println("[CacheManager] SSD Cache size trimmed down to " + (totalSize / 1024 / 1024) + " MB.");
        }
    }

    public synchronized Map<String, Object> get(String key) {
        init();
        
        Map<String, Object> cached = memoryCache.get(key);
        if (cached != null) {
            return cached;
        }

        if (core.Manager.gI().cache_enable_ssd) {
            File file = new File(CACHE_DIR, sanitizeKey(key) + ".dat");
            if (file.exists()) {
                try {
                    Map<String, Object> data = CacheFileCodec.loadJsonMap(file, XOR_KEY);
                    if (data != null) {
                        memoryCache.put(key, data);
                        return data;
                    }
                } catch (Exception e) {
                    System.err.println("[CacheManager] Error reading cache file for " + key + ": " + e.getMessage());
                }
            }
        }
        return null;
    }

    public synchronized void put(String key, Map<String, Object> value) {
        init();
        if (value == null) return;
        
        memoryCache.put(key, value);

        if (core.Manager.gI().cache_enable_ssd) {
            File file = new File(CACHE_DIR, sanitizeKey(key) + ".dat");
            try {
                CacheFileCodec.saveJsonMap(file, value, XOR_KEY);
            } catch (Exception e) {
                System.err.println("[CacheManager] Error writing cache file for " + key + ": " + e.getMessage());
            }
        }
    }

    public synchronized void remove(String key) {
        init();
        memoryCache.remove(key);
        // [FIX MEMORY LEAK] Xóa khỏi dirtyKeys và lastFlushTime để tránh tích lũy vô hạn
        dirtyKeys.remove(key);
        lastFlushTime.remove(key);
        if (core.Manager.gI().cache_enable_ssd) {
            File file = new File(CACHE_DIR, sanitizeKey(key) + ".dat");
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized List<Map<String, Object>> getList(String key) {
        Map<String, Object> wrapper = get(key);
        if (wrapper != null && Boolean.TRUE.equals(wrapper.get("is_list_wrapper"))) {
            Object listData = wrapper.get("data_list");
            if (listData instanceof List) {
                return (List<Map<String, Object>>) listData;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public synchronized void putList(String key, List<Map<String, Object>> value) {
        if (value == null) return;
        Map<String, Object> wrapper = new LinkedHashMap<>();
        wrapper.put("is_list_wrapper", true);
        
        JSONArray jsonList = new JSONArray();
        for (Map<String, Object> map : value) {
            JSONObject obj = new JSONObject();
            obj.putAll(map);
            jsonList.add(obj);
        }
        wrapper.put("data_list", jsonList);
        put(key, wrapper);
    }

    private String sanitizeKey(String key) {
        return key.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }

    private final java.util.concurrent.ConcurrentHashMap<String, Boolean> dirtyKeys = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.concurrent.ConcurrentHashMap<String, Long> lastFlushTime = new java.util.concurrent.ConcurrentHashMap<>();

    public void markDirty(String key) {
        if (key == null) return;
        dirtyKeys.put(key, true);
    }

    public void markClean(String key) {
        if (key == null) return;
        dirtyKeys.put(key, false);
        lastFlushTime.put(key, System.currentTimeMillis());
    }

    public boolean isDirty(String key) {
        return key != null && Boolean.TRUE.equals(dirtyKeys.get(key));
    }

    public void flushAllDirtyToDb() {
        Log.info("CacheManager", "Flushing all pending dirty cache keys to SQL database...");
        int flushedCount = 0;
        for (String key : dirtyKeys.keySet()) {
            if (isDirty(key)) {
                try {
                    if (key.startsWith("players_") && !key.startsWith("players_detu_") && !key.startsWith("players_bot_")) {
                        String name = key.substring("players_".length());
                        model.Player p = network.SessionManager.PLAYERS_BY_NAME.get(name);
                        if (p != null) {
                            p.flush(p, false, "players");
                            markClean(key);
                            flushedCount++;
                        }
                    } else if (key.startsWith("players_bot_")) {
                        String name = key.substring("players_bot_".length());
                        model.Player p = network.SessionManager.PLAYERS_BY_NAME.get(name);
                        if (p != null) {
                            p.flush(p, false, "players_bot");
                            markClean(key);
                            flushedCount++;
                        }
                    }
                } catch (Exception e) {
                    Log.error("CacheManager", "Error flushing dirty key " + key + ": " + e.getMessage());
                }
            }
        }
        Log.success("CacheManager", "Flushed " + flushedCount + " dirty cache keys to database successfully.");
    }

    public synchronized void clear() {
        memoryCache.clear();
        dirtyKeys.clear();
        lastFlushTime.clear();
    }
}
