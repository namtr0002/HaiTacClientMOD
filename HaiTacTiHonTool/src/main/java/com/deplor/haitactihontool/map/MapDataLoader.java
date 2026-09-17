package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.AppConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapDataLoader {
   private static MapDataLoader.SaveFormat saveFormat = MapDataLoader.SaveFormat.NEW_FORMAT;
   private static boolean preferSQL = true;
   private static List<GameMap> cachedMaps = null;
   public static final String JSON_PATH = "Data/Map/ServerData/maps.json";
   public static final String IMAGE_DIR = "Data/Map/ServerImage/";

   public static MapDataLoader.SaveFormat getSaveFormat() {
      return saveFormat;
   }

   public static void setSaveFormat(MapDataLoader.SaveFormat format) {
      saveFormat = format;
      clearCache();
   }

   public static void clearCache() {
      cachedMaps = null;
   }

   public static List<GameMap> getCachedMaps() {
      return cachedMaps;
   }

   public static void setCachedMaps(List<GameMap> maps) {
      cachedMaps = maps;
   }

   public static List<GameMap> loadAllMaps(String basePath) {
      return loadAllMaps(basePath, null);
   }

   public static List<GameMap> loadAllMaps(String basePath, LoadingCallback callback) {
      if (cachedMaps != null && !cachedMaps.isEmpty()) {
         return cachedMaps;
      } else {
         List<GameMap> loaded = new ArrayList<>();
         if (saveFormat == MapDataLoader.SaveFormat.OLD_FORMAT) {
            if (callback != null) {
               callback.onProgress(50, 100, "Loading Maps from Legacy Binary...");
            }

            SQLMapLoader sqlLoader = new SQLMapLoader();
            loaded = sqlLoader.loadFromBinary(basePath);
         } else {
            if (callback != null) {
               callback.onProgress(0, 100, "Checking SQL Connection...");
            }

            if (preferSQL) {
               SQLMapLoader sqlLoader = new SQLMapLoader();
               loaded = sqlLoader.loadAllMaps(basePath);
            }

            if (loaded.isEmpty()) {
               if (callback != null) {
                  callback.onProgress(50, 100, "Loading Maps from JSON...");
               }

               String full = basePath != null && !basePath.isEmpty()
                  ? basePath + "/Data/Map/ServerData/maps.json"
                  : AppConfig.getResolvedPath("path_map", "Data/Map/ServerData/maps.json");
               File f = new File(full);
               if (f.isDirectory()) {
                  full = new File(f, "maps.json").getPath();
               }
               loaded = loadAllMapsFromFile(full);
            }
         }

         if (callback != null) {
            callback.onProgress(100, 100, "Maps Loaded: " + loaded.size());
         }

         cachedMaps = loaded;
         return cachedMaps;
      }
   }

   public static void setPreferSQL(boolean prefer) {
      preferSQL = prefer;
   }

   public static List<GameMap> loadAllMapsFromFile(String filePath) {
      try {
         return MapDataExporter.loadAllFromJsonFile(filePath);
      } catch (FileNotFoundException var2) {
         System.out.println("[MapDataLoader] JSON not found: " + filePath);
         return new ArrayList<>();
      } catch (IOException var3) {
         System.err.println("[MapDataLoader] Error loading: " + var3.getMessage());
         return new ArrayList<>();
      }
   }

   public static void saveAllMaps(List<GameMap> maps, String basePath) throws IOException {
      String full = basePath != null && !basePath.isEmpty()
         ? basePath + "/Data/Map/ServerData/maps.json"
         : AppConfig.getResolvedPath("path_map", "Data/Map/ServerData/maps.json");
      File f = new File(full);
      if (f.isDirectory()) {
         full = new File(f, "maps.json").getPath();
      }
      File dir = new File(full).getParentFile();
      if (dir != null && !dir.exists()) {
         dir.mkdirs();
      }

      MapDataExporter.saveAllToJsonFile(maps, full);
      cachedMaps = maps;
   }

   public static void saveMapBinary(GameMap map, String basePath) throws IOException {
      String dirPath = basePath != null && !basePath.isEmpty() ? basePath + "/Data/Map/ServerData/binary/" : AppConfig.getPath("Data/Map/ServerData/binary/");
      File dir = new File(dirPath);
      MapDataExporter.exportMapBinaryFiles(map, dir);
      System.out.println("[MapDataLoader] Saved binary map " + map.id + " to: " + dir.getAbsolutePath());
   }

   public static void saveMap(GameMap map, String basePath) throws IOException {
      if (saveFormat == MapDataLoader.SaveFormat.OLD_FORMAT) {
         saveMapBinary(map, basePath);
         if (cachedMaps != null) {
            boolean found = false;

            for (int i = 0; i < cachedMaps.size(); i++) {
               if (cachedMaps.get(i).id == map.id) {
                  cachedMaps.set(i, map);
                  found = true;
                  break;
               }
            }

            if (!found) {
               cachedMaps.add(map);
            }
         }
      } else {
         List<GameMap> allMaps = loadAllMaps(basePath);
         boolean found = false;

         for (int ix = 0; ix < allMaps.size(); ix++) {
            if (allMaps.get(ix).id == map.id) {
               allMaps.set(ix, map);
               found = true;
               break;
            }
         }

         if (!found) {
            allMaps.add(map);
         }

         saveAllMaps(allMaps, basePath);
      }
   }

   public static String getTileImagePath(int tileSetId, String basePath) {
      int imageId = 23020 + tileSetId;
      String dir = basePath != null && !basePath.isEmpty() ? basePath + "/Data/Map/ServerImage/" : "Data/Map/ServerImage/";
      return dir + imageId + ".png";
   }

   public static String getWaterImagePath(int tileSetId, String basePath) {
      int imageId = 23070 + tileSetId;
      String dir = basePath != null && !basePath.isEmpty() ? basePath + "/Data/Map/ServerImage/" : "Data/Map/ServerImage/";
      return dir + imageId + ".png";
   }

   public static boolean tileImageExists(int tileSetId, String basePath) {
      return new File(getTileImagePath(tileSetId, basePath)).exists();
   }

   public static enum SaveFormat {
      NEW_FORMAT,
      OLD_FORMAT;
   }
}
