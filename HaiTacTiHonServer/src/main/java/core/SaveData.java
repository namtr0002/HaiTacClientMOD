package core;

import rank.Ranked;
import map.zones.ChiemDao;
import model.DauGia;

import activities.Market;
import model.VongQuayOcSen;
import clan.Clan;
import map.zones.WorldWar;
import activities.Sudo;
import model.Player;
import database.DbManager;
import network.SessionManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import boss.SuperBossManager;

public class SaveData {

    /**
     * Bounded thread pool for parallel player data flushing and server system persistence.
     */
    private static final int THREAD_COUNT = Math.max(4, Math.min(16, Runtime.getRuntime().availableProcessors() * 2));
    private static final ExecutorService SAVE_POOL = Executors.newFixedThreadPool(
        THREAD_COUNT,
        r -> {
            Thread t = new Thread(r, "HTTH-Save-Worker");
            t.setDaemon(true);
            return t;
        }
    );

    private static int botSaveOffset = 0;

    public static void process() {
        if (Manager.gI().server_admin) {
            return;
        }
        long t = System.currentTimeMillis();

        // 1. Separate real players and active bots
        List<Player> realPlayers = new ArrayList<>();
        List<Player> bots = new ArrayList<>();

        for (Player p0 : SessionManager.PLAYERS_MAP.values()) {
            if (p0 == null || p0.isClosed) continue;
            if (p0.isBot || p0 instanceof bot.botplayer.BotPlayerReal) {
                bots.add(p0);
            } else if (p0.conn != null) {
                realPlayers.add(p0);
            }
        }

        // 1a. Parallel save for ALL real players (Priority 1)
        List<Future<?>> futures = new ArrayList<>();
        for (Player realP : realPlayers) {
            futures.add(SAVE_POOL.submit(() -> {
                try {
                    realP.flush(realP, false);
                } catch (Exception e) {
                    Log.error("SaveData", "Failed to save player " + realP.name + ": " + e.getMessage());
                }
            }));
        }

        // 1b. Batched round-robin save for bots (Max 25 bots per cycle to prevent DB pool exhaustion)
        if (!bots.isEmpty()) {
            int botCount = bots.size();
            int batchSize = Math.min(25, botCount);
            int startIdx = botSaveOffset % botCount;
            for (int i = 0; i < batchSize; i++) {
                int idx = (startIdx + i) % botCount;
                Player botP = bots.get(idx);
                futures.add(SAVE_POOL.submit(() -> {
                    try {
                        botP.flush(botP, false, "players_bot");
                    } catch (Exception ignored) {}
                }));
            }
            botSaveOffset = (startIdx + batchSize) % botCount;
        }

        // Wait for saves to complete (max 10s per cycle)
        for (Future<?> f : futures) {
            try {
                f.get(10, TimeUnit.SECONDS);
            } catch (java.util.concurrent.TimeoutException ex) {
                Log.warn("SaveData", "A player save task timed out after 10s");
            } catch (Exception ex) {
                Log.error("SaveData", "Save task exception: " + ex.getMessage());
            }
        }

        // 2. Parallel flush of independent server systems (Market, Clan, DauGia, etc.)
        List<java.util.concurrent.CompletableFuture<Void>> systemFutures = new ArrayList<>();
        
        systemFutures.add(java.util.concurrent.CompletableFuture.runAsync(() -> {
            try { Market.update(); } catch (Exception e) { e.printStackTrace(); }
        }, SAVE_POOL));
        
        systemFutures.add(java.util.concurrent.CompletableFuture.runAsync(() -> {
            try { Clan.update(); } catch (Exception e) { e.printStackTrace(); }
        }, SAVE_POOL));

        systemFutures.add(java.util.concurrent.CompletableFuture.runAsync(() -> {
            try { VongQuayOcSen.updateDB(); } catch (Exception e) { e.printStackTrace(); }
        }, SAVE_POOL));

        systemFutures.add(java.util.concurrent.CompletableFuture.runAsync(() -> {
            try { DauGia.saveSettings(); } catch (Exception e) { e.printStackTrace(); }
        }, SAVE_POOL));

        systemFutures.add(java.util.concurrent.CompletableFuture.runAsync(() -> {
            try { WorldWar.saveData(); } catch (Exception e) { e.printStackTrace(); }
        }, SAVE_POOL));

        systemFutures.add(java.util.concurrent.CompletableFuture.runAsync(() -> {
            try { Sudo.updateDb(); } catch (Exception e) { e.printStackTrace(); }
        }, SAVE_POOL));

        systemFutures.add(java.util.concurrent.CompletableFuture.runAsync(() -> {
            try { SuperBossManager.update_reward(); } catch (Exception e) { e.printStackTrace(); }
        }, SAVE_POOL));

        systemFutures.add(java.util.concurrent.CompletableFuture.runAsync(() -> {
            try { ChiemDao.updateDb(); } catch (Exception e) { e.printStackTrace(); }
        }, SAVE_POOL));

        try {
            java.util.concurrent.CompletableFuture.allOf(systemFutures.toArray(new java.util.concurrent.CompletableFuture[0])).get(15, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.error("SaveData", "System data save wait error: " + e.getMessage());
        }

        // 3. Trigger asynchronous Ranked cache refresh in background (zero block on save cycle!)
        try {
            Ranked.updateAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long duration = System.currentTimeMillis() - t;
        if (duration > 1500) {
            Log.warn("SaveData", String.format("Save cycle took %.2f seconds", duration / 1000.0));
        }
    }
    
    public static void shutdownPool() {
        try {
            if (SAVE_POOL != null && !SAVE_POOL.isShutdown()) {
                SAVE_POOL.shutdown();
                if (!SAVE_POOL.awaitTermination(3, TimeUnit.SECONDS)) {
                    SAVE_POOL.shutdownNow();
                }
            }
        } catch (Exception e) {
            SAVE_POOL.shutdownNow();
        }
        try {
            rank.Ranked.shutdownPool();
        } catch (Exception ignored) {}
    }

    public static void BaoTri() {
        if (ServerManager.gI().isBaoTri) {
            return;
        }
        ServerManager.gI().isBaoTri = true;
        Log.warn("BaoTri", "Initiating maintenance shutdown sequence (closing in 10s)...");

        // 1. Broadcast notification to all online players
        try {
            Manager.gI().chatKTG(1, "Hệ thống chuẩn bị bảo trì định kỳ sau 10 giây. Người chơi vui lòng lưu ý!", 0);
        } catch (Exception ignored) {}

        // 2. 10s countdown
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        
        // 3. Flush active cache and player data before disconnect
        Log.info("BaoTri", "Flushing active player data before disconnect...");
        try {
            SaveData.process();
            database.CacheManager.gI().flushAllDirtyToDb();
        } catch (Exception e) {
            Log.error("BaoTri", "Error flushing pre-disconnect data: " + e.getMessage());
        }
        
        // 4. Disconnect active clients gracefully
        Log.info("BaoTri", "Disconnecting active players...");
        synchronized (SessionManager.CLIENT_ENTRYS) {
            for (int i = SessionManager.CLIENT_ENTRYS.size() - 1; i >= 0; i--) {
                try {
                    SessionManager.CLIENT_ENTRYS.get(i).disconnect();
                    SessionManager.CLIENT_ENTRYS.get(i).disconnectSC();
                } catch (Exception e) {
                    Log.error("BaoTri", "Error kicking client: " + e.getMessage());
                }
            }
        }
        Log.info("BaoTri", "All active clients disconnected successfully.");
        try {
            Thread.sleep(1500L);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        // 5. Final flush to database
        Log.info("BaoTri", "Flushing player & server data to database...");
        try {
            SaveData.process();
            database.CacheManager.gI().flushAllDirtyToDb();
        } catch (Exception e) {
            Log.error("BaoTri", "Error during final data flush: " + e.getMessage());
        }

        // 6. Close ServerManager (ServerSocketChannel)
        try {
            ServerManager.gI().close();
        } catch (Exception e) {
            Log.error("BaoTri", "Error closing ServerManager: " + e.getMessage());
        }
        Log.info("BaoTri", "Data save completed.");

        // 7. Stop Manager & background services
        try {
            Manager.gI().close();
        } catch (Exception e) {
            Log.error("BaoTri", "Error closing Manager: " + e.getMessage());
        }

        // 8. Shutdown thread pools
        try {
            shutdownPool();
            network.GlobalThreadManager.shutdownAll();
            map.MapManager.getInstance().stop();
        } catch (Exception e) {
            Log.error("BaoTri", "Error stopping thread pools: " + e.getMessage());
        }

        // 9. Close Database connection pool
        try {
            DbManager.gI().close();
        } catch (Exception e) {
            Log.error("BaoTri", "Error closing DbManager: " + e.getMessage());
        }

        // 10. Clean RAM & free memory
        try {
            database.TemplateCache.invalidateAll();
            database.CacheManager.gI().clear();
            System.gc();
        } catch (Exception ignored) {}

        Log.success("BaoTri", "Server gracefully closed and stopped.");

        // 11. Auto-restart if enabled
        if (Manager.gI().auto_restart) {
            restartServer();
        }
        System.exit(0);
    }

    public static void restartServer() {
        try {
            Log.info("Restart", "Preparing to restart server instance...");

            // 1. Check if running under a loop runner script (e.g. run.bat, run_release.bat, run_jar.bat, run_release.sh)
            String runnerEnv = System.getenv("HTTH_RUNNER");
            if (runnerEnv != null && !runnerEnv.trim().isEmpty()) {
                Log.info("Restart", "Server is managed by runner script (HTTH_RUNNER=" + runnerEnv + "). Runner will automatically restart server upon exit.");
                return;
            }

            String os = System.getProperty("os.name").toLowerCase();
            String javaBin = System.getProperty("java.home") + java.io.File.separator + "bin" + java.io.File.separator + "java";

            java.io.File jarFile = null;
            try {
                java.net.URI uri = Start.class.getProtectionDomain().getCodeSource().getLocation().toURI();
                jarFile = new java.io.File(uri);
            } catch (Exception ignored) {}

            boolean isTempJar = jarFile != null && (
                jarFile.getAbsolutePath().toLowerCase().contains("htth_svr_") ||
                jarFile.getAbsolutePath().toLowerCase().contains("temp") ||
                jarFile.getAbsolutePath().toLowerCase().contains("tmp")
            );

            if (os.contains("win")) {
                String restartCmd;
                if (new java.io.File("HaiTacTiHonServer.exe").exists()) {
                    restartCmd = "ping -n 4 127.0.0.1 >nul & start \"HaiTacTiHonServer\" \"HaiTacTiHonServer.exe\"";
                } else if (new java.io.File("run.bat").exists()) {
                    restartCmd = "ping -n 4 127.0.0.1 >nul & start \"HaiTacTiHonServer\" cmd.exe /c run.bat";
                } else if (new java.io.File("run_release.bat").exists()) {
                    restartCmd = "ping -n 4 127.0.0.1 >nul & start \"HaiTacTiHonServer\" cmd.exe /c run_release.bat";
                } else if (jarFile != null && jarFile.isFile() && jarFile.getName().endsWith(".jar") && !isTempJar) {
                    restartCmd = "ping -n 4 127.0.0.1 >nul & start \"HaiTacTiHonServer\" \"" + javaBin + "\" -server -Xms512M -Xmx2048M -XX:+UseG1GC -Dfile.encoding=UTF-8 -jar \"" + jarFile.getAbsolutePath() + "\"";
                } else {
                    restartCmd = "ping -n 4 127.0.0.1 >nul & start \"HaiTacTiHonServer\" \"" + javaBin + "\" -server -Xms512M -Xmx2048M -XX:+UseG1GC -Dfile.encoding=UTF-8 -cp \"" + System.getProperty("java.class.path") + "\" core.Start";
                }

                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", restartCmd);
                pb.directory(new java.io.File("."));
                pb.start();
            } else {
                String restartCmd;
                if (new java.io.File("run_release.sh").exists()) {
                    restartCmd = "sleep 3; bash run_release.sh";
                } else if (jarFile != null && jarFile.isFile() && jarFile.getName().endsWith(".jar") && !isTempJar) {
                    restartCmd = "sleep 3; \"" + javaBin + "\" -server -Dfile.encoding=UTF-8 -jar \"" + jarFile.getAbsolutePath() + "\"";
                } else {
                    restartCmd = "sleep 3; \"" + javaBin + "\" -server -Dfile.encoding=UTF-8 -cp \"" + System.getProperty("java.class.path") + "\" core.Start";
                }

                ProcessBuilder pb = new ProcessBuilder("nohup", "bash", "-c", restartCmd, ">/dev/null", "2>&1", "&");
                pb.directory(new java.io.File("."));
                pb.start();
            }

            Log.success("Restart", "Server auto-restart command dispatched successfully.");
        } catch (Exception e) {
            Log.error("Restart", "Failed to auto-restart server: " + e.getMessage(), e);
        }
    }
}
