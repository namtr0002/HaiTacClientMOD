package com.deplor.haitactihontool.map;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class BatchMapExporter {
   public static List<GameMap> generateBatchChain(BatchMapExporter.BatchConfig config) {
      List<GameMap> maps = new ArrayList<>();

      for (int i = 0; i < config.count; i++) {
         AutoMapEngine.GenConfig genConfig = new AutoMapEngine.GenConfig();
         int curId = config.startId + i;
         genConfig.id = curId;
         genConfig.name = config.namePrefix + (i + 1);
         genConfig.width = config.width;
         genConfig.height = config.height;
         genConfig.biome = config.biome != null ? config.biome : AutoMapEngine.BiomeArchetype.ISLAND_BEACH;
         genConfig.tileSetId = genConfig.biome.defaultTileSet;
         genConfig.idBack = genConfig.biome.defaultIdBack;
         genConfig.hBack = genConfig.biome.defaultHBack;
         genConfig.seed = System.currentTimeMillis() + i * 7919L;
         genConfig.autoItems = config.autoItems;
         genConfig.autoMobs = config.autoMobs;
         if (i > 0) {
            genConfig.targetLeftMapId = (short)(curId - 1);
         } else {
            genConfig.targetLeftMapId = -1;
         }

         if (i < config.count - 1) {
            genConfig.targetRightMapId = (short)(curId + 1);
         } else {
            genConfig.targetRightMapId = config.targetMapId;
         }

         GameMap generated = AutoMapEngine.generateMap(genConfig);
         maps.add(generated);
      }

      return maps;
   }

   public static List<GameMap> generateBatch(BatchMapExporter.BatchConfig config) {
      return generateBatchChain(config);
   }

   public static void exportBatchToSql(List<GameMap> maps, File outputFile) throws IOException {
      StringBuilder sb = new StringBuilder();
      sb.append("/* Auto-generated maps batch SQL export */\n");
      sb.append("SET NAMES utf8mb4;\n");
      sb.append("SET FOREIGN_KEY_CHECKS = 0;\n\n");
      sb.append("DROP TABLE IF EXISTS `maps`;\n");
      sb.append("CREATE TABLE `maps` (\n");
      sb.append("  `id` int NOT NULL,\n");
      sb.append("  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,\n");
      sb.append("  `mobs` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,\n");
      sb.append("  `maxzone` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `maxplayer` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `npcs` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,\n");
      sb.append("  `boat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,\n");
      sb.append("  `typeViewPlayer` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `b` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `specMap` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `vgos` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,\n");
      sb.append("  `IDBack` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `HBack` smallint NULL DEFAULT NULL,\n");
      sb.append("  `maxW` smallint NULL DEFAULT NULL,\n");
      sb.append("  `maxH` smallint NULL DEFAULT NULL,\n");
      sb.append("  `w` smallint NULL DEFAULT NULL,\n");
      sb.append("  `h` smallint NULL DEFAULT NULL,\n");
      sb.append("  `tile_id` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `id_eff_map` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `level` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `typeChangeMap` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `mPosMapTrain` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,\n");
      sb.append("  `strTimeChange` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,\n");
      sb.append("  PRIMARY KEY (`id`)\n");
      sb.append(") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;\n\n");

      for (GameMap map : maps) {
         sb.append(MapDataGenerator.generateSQL(map)).append("\n");
      }

      File parent = outputFile.getParentFile();
      if (parent != null && !parent.exists()) {
         parent.mkdirs();
      }

      try (FileWriter writer = new FileWriter(outputFile, StandardCharsets.UTF_8)) {
         writer.write(sb.toString());
      }
   }

   public static void exportBatchToJson(List<GameMap> maps, File outputFile) throws IOException {
      JSONArray array = new JSONArray();

      for (GameMap map : maps) {
         JSONObject json = MapDataExporter.exportToSqlJson(map);
         array.put(json);
      }

      File parent = outputFile.getParentFile();
      if (parent != null && !parent.exists()) {
         parent.mkdirs();
      }

      try (FileWriter writer = new FileWriter(outputFile, StandardCharsets.UTF_8)) {
         writer.write(array.toString(2));
      }
   }

   public static int exportBatchBinary(List<GameMap> maps, File outputDir, LoadingCallback callback) {
      return MapDataExporter.exportAllBinary(maps, outputDir, callback);
   }

   public static int exportBatchImages(List<GameMap> maps, File outputDir, LoadingCallback callback) {
      return MapImageExporter.exportAllMapImages(maps, outputDir, callback);
   }

   public static class BatchConfig {
      public int count = 5;
      public int startId = 200;
      public String namePrefix = "Auto Map ";
      public int width = 50;
      public int height = 17;
      public AutoMapEngine.BiomeArchetype biome = AutoMapEngine.BiomeArchetype.ISLAND_BEACH;
      public AutoMapEngine.TerrainType terrainType = AutoMapEngine.TerrainType.ISLAND;
      public int[] tileSets = new int[]{0, 1, 3};
      public boolean autoMobs = true;
      public boolean autoItems = true;
      public short targetMapId = 1;
   }
}
