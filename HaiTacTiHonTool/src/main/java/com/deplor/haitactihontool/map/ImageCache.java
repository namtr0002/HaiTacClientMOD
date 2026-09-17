package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.AppConfig;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;

public class ImageCache {
   private static final Map<Integer, BufferedImage> serverImages = new ConcurrentHashMap<>();
   private static boolean isPreloaded = false;

   public static void clearCache() {
      serverImages.clear();
      isPreloaded = false;
   }

   public static BufferedImage getServerImage(int id) {
      if (serverImages.containsKey(id)) {
         return serverImages.get(id);
      } else {
         String baseDir;
         if (id >= 23020 && id < 23070) {
            baseDir = AppConfig.getResolvedPath("path_tile", "Data/Map/ServerImage/");
         } else if (id >= 23070 && id < 23120) {
            baseDir = AppConfig.getResolvedPath("path_water", "Data/Map/ServerImage/");
         } else {
            baseDir = AppConfig.getResolvedPath("path_map_img", "Data/Map/ServerImage/");
         }

         if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
            baseDir = baseDir + "/";
         }

         File f = new File(baseDir + id + ".png");
         if (f.exists()) {
            try {
               BufferedImage img = ImageIO.read(f);
               serverImages.put(id, img);
               return img;
            } catch (Exception var4) {
            }
         }

         return null;
      }
   }

   public static BufferedImage getTileImage(int tileSetId, String basePath) {
      int id = 23020 + tileSetId;
      return getServerImage(id);
   }

   public static BufferedImage getWaterImage(int tileSetId, String basePath) {
      int id = 23070 + tileSetId;
      return getServerImage(id);
   }

   public static void preloadAll(String basePath) {
      preloadAll(basePath, null);
   }

   public static void preloadAll(String basePath, LoadingCallback callback) {
      if (!isPreloaded) {
         Set<String> dirs = new HashSet<>();
         if (basePath != null && !basePath.isEmpty()) {
            dirs.add(basePath + "/Data/Map/ServerImage/");
         } else {
            dirs.add(AppConfig.getResolvedPath("path_tile", "Data/Map/ServerImage/"));
            dirs.add(AppConfig.getResolvedPath("path_water", "Data/Map/ServerImage/"));
            dirs.add(AppConfig.getResolvedPath("path_map_img", "Data/Map/ServerImage/"));
         }

         long start = System.currentTimeMillis();
         int numThreads = Math.max(2, Runtime.getRuntime().availableProcessors());
         ExecutorService executor = Executors.newFixedThreadPool(numThreads);
         List<File> allFiles = new ArrayList<>();

         for (String dirPath : dirs) {
            File dir = new File(dirPath);
            if (dir.exists() && dir.isDirectory()) {
               File[] files = dir.listFiles((d, name) -> name.endsWith(".png"));
               if (files != null) {
                  allFiles.addAll(Arrays.asList(files));
               }
            }
         }

         int total = allFiles.size();
         AtomicInteger count = new AtomicInteger(0);
         if (total > 0) {
            for (File f : allFiles) {
               executor.submit(() -> {
                  try {
                     String name = f.getName().replace(".png", "");
                     int id = Integer.parseInt(name);
                     BufferedImage img = ImageIO.read(f);
                     if (img != null) {
                        serverImages.put(id, img);
                     }
                  } catch (Exception var11x) {
                  } finally {
                     int c = count.incrementAndGet();
                     if (callback != null) {
                        callback.onProgress(c, total, "Loaded image " + f.getName());
                     }
                  }
               });
            }
         }

         executor.shutdown();

         try {
            executor.awaitTermination(60L, TimeUnit.SECONDS);
         } catch (InterruptedException var12) {
            Thread.currentThread().interrupt();
         }

         long elapsed = System.currentTimeMillis() - start;
         System.out.println("[ImageCache] Preloaded " + serverImages.size() + " server images using " + numThreads + " CPU threads in " + elapsed + "ms.");
         isPreloaded = true;
      }
   }
}
