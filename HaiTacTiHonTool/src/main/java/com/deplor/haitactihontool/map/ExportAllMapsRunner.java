package com.deplor.haitactihontool.map;

import java.io.File;
import java.util.List;

public class ExportAllMapsRunner {
   public static void main(String[] args) {
      System.out.println("==================================================");
      System.out.println("   HAITACTIHON MAP EXPORTER - LOCAL BATCH         ");
      System.out.println("==================================================");

      try {
         System.out.println("[1/4] Preloading tile and item images...");
         ImageCache.preloadAll("");
         System.out.println("[2/4] Loading maps from local Data/Map/ServerData/...");
         List<GameMap> maps = MapDataLoader.loadAllMaps("");
         if (maps == null || maps.isEmpty()) {
            maps = MapDataLoader.loadAllMapsFromFile("Data/Map/ServerData/maps.json");
         }

         System.out.println("Loaded " + maps.size() + " maps.");
         if (maps.isEmpty()) {
            System.err.println("No maps loaded! Aborting.");
            return;
         }

         System.out.println("[3/4] Exporting binary data ({id}_data, {id}_item)...");
         File binaryDir = new File("Data/Map/ServerData/binary/");
         int binCount = MapDataExporter.exportAllBinary(maps, binaryDir, null);
         System.out.println("  -> Exported " + binCount + " binary map pairs to " + binaryDir.getAbsolutePath());
         System.out.println("[4/4] Exporting transparent PNG map images ({id}.png)...");
         File imagesDir = new File("Data/Map/Export/images/");
         int imgCount = MapImageExporter.exportAllMapImages(maps, imagesDir, (cur, total, msg) -> {
            if (cur % 25 == 0 || cur == total) {
               System.out.println("  PNG progress: " + cur + "/" + total);
            }
         });
         System.out.println("  -> Exported " + imgCount + " PNG images to " + imagesDir.getAbsolutePath());
         System.out.println("Updating local maps.sql and maps.json...");
         File sqlFile = new File("Data/Map/ServerData/maps.sql");
         File jsonFile = new File("Data/Map/ServerData/maps.json");
         BatchMapExporter.exportBatchToSql(maps, sqlFile);
         BatchMapExporter.exportBatchToJson(maps, jsonFile);
         System.out.println("==================================================");
         System.out.println("   LOCAL BATCH EXPORT COMPLETED SUCCESSFULLY!    ");
         System.out.println("   Total Maps: " + maps.size());
         System.out.println("   Output: " + new File(".").getAbsolutePath());
         System.out.println("==================================================");
      } catch (Exception var8) {
         System.err.println("Fatal error during batch export: " + var8.getMessage());
         var8.printStackTrace();
      }
   }
}
