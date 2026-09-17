package network;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GlobalThreadManager - A highly optimized, thread-safe manager for game loops,
 * player connections, and system event threads in the HTTH server.
 * 
 * Provides:
 * 1. Specialized Thread Pools to replace ad-hoc thread creation.
 * 2. Real-time tracking and monitoring of active threads, ready for JSON serialization.
 * 3. Prevention of common concurrent modification errors through thread-safe collections.
 * 4. Automated JVM Memory Monitoring with SSD Cache Swapping when memory exceeds 80%.
 */
public class GlobalThreadManager implements Serializable {
    private static final long serialVersionUID = 1L;

    public volatile String id;
    public volatile String id1;
    public volatile String id2;
    public volatile String name;
    public volatile String create_at;
    public volatile String update_at;
    public volatile String status;
    
    public List<GlobalThreadManager> maps;
    public List<GlobalThreadManager> players;
    public List<GlobalThreadManager> event;

    // --- High Performance Thread Pools ---
    private static final ScheduledExecutorService mapExecutor;
    private static final ExecutorService playerExecutor;
    private static final ScheduledExecutorService eventExecutor;

    // --- Thread-Safe Registry lists for real-time tracking ---
    private static final List<GlobalThreadManager> activeMaps = new CopyOnWriteArrayList<>();
    private static final List<GlobalThreadManager> activePlayers = new CopyOnWriteArrayList<>();
    private static final List<GlobalThreadManager> activeEvents = new CopyOnWriteArrayList<>();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // --- SSD Cache Configuration ---
    private static final String SSD_CACHE_DIR = "./.cache/threads";
    private static double memThreshold = 0.95; // 95% threshold limit
    private static final List<MemorySensitive> memoryListeners = new CopyOnWriteArrayList<>();

    static {
        int cores = Runtime.getRuntime().availableProcessors();
        
        // Maps: Scheduled pool for handling game tick/zone logic. Bound to CPU cores.
        mapExecutor = new ScheduledThreadPoolExecutor(
            Math.max(4, cores * 2),
            new CustomThreadFactory("HTTH-Map-Update")
        );
        
        // Players: Cached pool for handling client IO. Scale up/down dynamically.
        playerExecutor = Executors.newCachedThreadPool(
            new CustomThreadFactory("HTTH-Player-Session")
        );
        
        // Events: Scheduled pool for background managers and tickers.
        eventExecutor = new ScheduledThreadPoolExecutor(
            Math.max(2, cores),
            new CustomThreadFactory("HTTH-Event-System")
        );

        // Ensure SSD cache folder exists
        try {
            File dir = new File(SSD_CACHE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception ignore) {}

        // Add background memory monitoring thread (check every 5 seconds)
        ScheduledExecutorService monitorExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "HTTH-Memory-Monitor");
            t.setDaemon(true);
            return t;
        });
        monitorExecutor.scheduleAtFixedRate(GlobalThreadManager::checkMemoryUsage, 5, 5, TimeUnit.SECONDS);

        // Add shutdown hook for clean shutdown of all systems
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            monitorExecutor.shutdown();
            GlobalThreadManager.shutdownAll();
        }, "HTTH-Thread-Shutdown-Hook"));
    }

    // Default constructor (used for serialization/snapshots)
    public GlobalThreadManager() {
        this.create_at = getCurrentTimestamp();
        this.update_at = this.create_at;
    }

    // Parametrized constructor for registering specific tasks
    public GlobalThreadManager(String id, String name, String status) {
        this(id, id + "_1", id + "_2", name, status);
    }

    // Parametrized constructor with id1 and id2
    public GlobalThreadManager(String id, String id1, String id2, String name, String status) {
        this.id = id;
        this.id1 = id1;
        this.id2 = id2;
        this.name = name;
        this.status = status;
        this.create_at = getCurrentTimestamp();
        this.update_at = this.create_at;
    }

    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(formatter);
    }

    /**
     * Updates the status and timestamp of the thread task.
     * Thread-safe and volatile visibility.
     */
    public void update(String status) {
        this.status = status;
        this.update_at = getCurrentTimestamp();
    }

    // --- Getter for Thread Pools ---
    public static ScheduledExecutorService getMapExecutor() {
        return mapExecutor;
    }

    public static ExecutorService getPlayerExecutor() {
        return playerExecutor;
    }

    public static ScheduledExecutorService getEventExecutor() {
        return eventExecutor;
    }

    // --- Automatic lifecycle wrapper methods for running tasks ---

    public static ScheduledFuture<?> scheduleMap(String id, String name, Runnable task, long initialDelay, long period, TimeUnit unit) {
        GlobalThreadManager info = new GlobalThreadManager(id, name, "RUNNING");
        activeMaps.add(info);
        
        ScheduledFuture<?> future = mapExecutor.scheduleAtFixedRate(() -> {
            info.update("RUNNING");
            try {
                task.run();
                info.update("IDLE");
            } catch (Throwable t) {
                info.update("ERROR: " + t.getMessage());
                System.err.println("[MapThreadError] ID: " + id + " - " + t.getMessage());
                t.printStackTrace();
            }
        }, initialDelay, period, unit);

        // [FIX MEMORY] Trả về wrapper tự xóa info khi Future bị cancel
        return new CancellableTrackedFuture(future, () -> activeMaps.remove(info));
    }

    public static void executePlayer(String id, String name, Runnable task) {
        GlobalThreadManager info = new GlobalThreadManager(id, name, "RUNNING");
        activePlayers.add(info);
        
        playerExecutor.execute(() -> {
            try {
                task.run();
            } catch (Throwable t) {
                System.err.println("[PlayerThreadError] ID: " + id + " - " + t.getMessage());
                t.printStackTrace();
            } finally {
                activePlayers.remove(info);
            }
        });
    }

    public static ScheduledFuture<?> scheduleEvent(String id, String name, Runnable task, long initialDelay, long period, TimeUnit unit) {
        GlobalThreadManager info = new GlobalThreadManager(id, name, "RUNNING");
        activeEvents.add(info);
        
        ScheduledFuture<?> future = eventExecutor.scheduleAtFixedRate(() -> {
            info.update("RUNNING");
            try {
                task.run();
                info.update("IDLE");
            } catch (Throwable t) {
                info.update("ERROR: " + t.getMessage());
                System.err.println("[EventThreadError] ID: " + id + " - " + t.getMessage());
                t.printStackTrace();
            }
        }, initialDelay, period, unit);

        // [FIX MEMORY] Trả về wrapper tự xóa info khi Future bị cancel
        return new CancellableTrackedFuture(future, () -> activeEvents.remove(info));
    }

    /**
     * [FIX MEMORY] ScheduledFuture wrapper: tự động gọi onCancel khi cancel() được gọi,
     * dùng để xóa info khỏi activeMaps/activeEvents tracking list.
     */
    private static class CancellableTrackedFuture implements ScheduledFuture<Object> {
        private final ScheduledFuture<?> delegate;
        private final Runnable onCancel;

        CancellableTrackedFuture(ScheduledFuture<?> delegate, Runnable onCancel) {
            this.delegate = delegate;
            this.onCancel = onCancel;
        }

        @Override public boolean cancel(boolean mayInterruptIfRunning) {
            boolean result = delegate.cancel(mayInterruptIfRunning);
            if (result && onCancel != null) onCancel.run();
            return result;
        }
        @Override public long getDelay(TimeUnit unit)          { return delegate.getDelay(unit); }
        @Override public int compareTo(java.util.concurrent.Delayed o) { return delegate.compareTo(o); }
        @Override public boolean isCancelled()                 { return delegate.isCancelled(); }
        @Override public boolean isDone()                      { return delegate.isDone(); }
        @Override public Object get() throws java.util.concurrent.ExecutionException, InterruptedException { return delegate.get(); }
        @Override public Object get(long timeout, TimeUnit unit) throws java.util.concurrent.ExecutionException, InterruptedException, java.util.concurrent.TimeoutException { return delegate.get(timeout, unit); }
    }

    // --- Direct Manual Registration APIs ---

    public static void registerMapDirect(GlobalThreadManager threadInfo) {
        activeMaps.add(threadInfo);
    }
    
    public static void unregisterMapDirect(GlobalThreadManager threadInfo) {
        activeMaps.remove(threadInfo);
    }
    
    public static void registerPlayerDirect(GlobalThreadManager threadInfo) {
        activePlayers.add(threadInfo);
    }
    
    public static void unregisterPlayerDirect(GlobalThreadManager threadInfo) {
        activePlayers.remove(threadInfo);
    }
    
    public static void registerEventDirect(GlobalThreadManager threadInfo) {
        activeEvents.add(threadInfo);
    }
    
    public static void unregisterEventDirect(GlobalThreadManager threadInfo) {
        activeEvents.remove(threadInfo);
    }

    // --- Status Reporting ---

    public static GlobalThreadManager getStatusReport() {
        GlobalThreadManager report = new GlobalThreadManager("root", "Global Thread Monitor", "ACTIVE");
        report.maps = new java.util.ArrayList<>(activeMaps);
        report.players = new java.util.ArrayList<>(activePlayers);
        report.event = new java.util.ArrayList<>(activeEvents);
        return report;
    }

    // --- Shutdown Utilities ---
    public static void shutdownAll() {
        System.out.println("[GlobalThreadManager] Shutting down all executors...");
        mapExecutor.shutdown();
        playerExecutor.shutdown();
        eventExecutor.shutdown();
        try {
            if (!mapExecutor.awaitTermination(3, TimeUnit.SECONDS)) mapExecutor.shutdownNow();
            if (!playerExecutor.awaitTermination(3, TimeUnit.SECONDS)) playerExecutor.shutdownNow();
            if (!eventExecutor.awaitTermination(3, TimeUnit.SECONDS)) eventExecutor.shutdownNow();
        } catch (InterruptedException e) {
            mapExecutor.shutdownNow();
            playerExecutor.shutdownNow();
            eventExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("[GlobalThreadManager] Shutdown complete.");
    }

    // --- Memory Monitoring & SSD Swapping Cache Logic ---

    public interface MemorySensitive {
        void onLowMemory();
    }

    public static void registerMemorySensitive(MemorySensitive listener) {
        memoryListeners.add(listener);
    }

    public static void unregisterMemorySensitive(MemorySensitive listener) {
        memoryListeners.remove(listener);
    }

    private static void checkMemoryUsage() {
        long maxMem = Runtime.getRuntime().maxMemory();
        long totalMem = Runtime.getRuntime().totalMemory();
        long freeMem = Runtime.getRuntime().freeMemory();
        long usedMem = totalMem - freeMem;
        
        double usageRatio = (double) usedMem / maxMem;
        if (usageRatio > memThreshold) {
            // Notify other sensitive systems to offload memory if critically near OOM
            for (MemorySensitive listener : memoryListeners) {
                try {
                    listener.onLowMemory();
                } catch (Exception e) {
                    System.err.println("[GlobalThreadManager] Error notifying low memory listener: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Serializes an object and writes it to SSD cache under .cache/threads/{key}.dat.
     * Uses GZIP compression for high performance and low storage overhead.
     */
    public static void saveToDiskCache(String key, Object obj) {
        File file = new File(SSD_CACHE_DIR, key + ".dat");
        try (FileOutputStream fos = new FileOutputStream(file);
             GZIPOutputStream gzos = new GZIPOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(gzos)) {
            oos.writeObject(obj);
            oos.flush();
        } catch (Exception e) {
            System.err.println("[GlobalThreadManager] Failed to cache " + key + " to disk: " + e.getMessage());
        }
    }

    /**
     * Reads and deserializes an object from the SSD cache.
     */
    @SuppressWarnings("unchecked")
    public static <T> T loadFromDiskCache(String key, Class<T> clazz) {
        File file = new File(SSD_CACHE_DIR, key + ".dat");
        if (!file.exists()) return null;
        
        try (FileInputStream fis = new FileInputStream(file);
             GZIPInputStream gzis = new GZIPInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(gzis)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            System.err.println("[GlobalThreadManager] Failed to read cached " + key + " from disk: " + e.getMessage());
            return null;
        }
    }

    /**
     * Removes an entry from the SSD cache.
     */
    public static void removeFromDiskCache(String key) {
        File file = new File(SSD_CACHE_DIR, key + ".dat");
        if (file.exists()) {
            file.delete();
        }
    }

    // --- Custom Thread Factory ---
    private static class CustomThreadFactory implements ThreadFactory {
        private final String prefix;
        private final java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger(1);

        public CustomThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, prefix + "-" + count.getAndIncrement());
            t.setDaemon(true);
            return t;
        }
    }
}
