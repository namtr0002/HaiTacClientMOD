package com.deplor.haitactihontool.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AutoMapEngine {
   private static final Map<AutoMapEngine.BiomeArchetype, AutoMapEngine.BiomeProfile> PROFILES = new EnumMap<>(AutoMapEngine.BiomeArchetype.class);

   private static void initProfiles() {
      AutoMapEngine.BiomeProfile p0 = new AutoMapEngine.BiomeProfile();
      p0.archetype = AutoMapEngine.BiomeArchetype.ISLAND_BEACH;
      p0.tileSetId = 0;
      p0.idBack = 1;
      p0.hBack = 260;
      p0.skyTiles = new int[]{0};
      p0.topBorderTiles = new int[]{10, 9};
      p0.subSurfaceTiles = new int[]{8, 9};
      p0.groundTiles = new int[]{9, 7, 10, 6, 2, 1};
      p0.bottomBaseTiles = new int[]{4, 5, 9};
      p0.platformTiles = new int[]{10, 9};
      p0.layer6Items = List.of((short)18);
      p0.layer3Items = List.of((short)11, (short)12, (short)8, (short)7, (short)9, (short)10);
      p0.layer2Items = List.of((short)0, (short)124, (short)125, (short)137);
      p0.layer5Items = List.of((short)9, (short)10, (short)6, (short)4);
      p0.mobTemplates = List.of(2, 57, 59, 113, 0, 94);
      p0.hasWaterAtBottom = true;
      PROFILES.put(AutoMapEngine.BiomeArchetype.ISLAND_BEACH, p0);
      AutoMapEngine.BiomeProfile p1 = new AutoMapEngine.BiomeProfile();
      p1.archetype = AutoMapEngine.BiomeArchetype.STONE_CAVERN;
      p1.tileSetId = 1;
      p1.idBack = 4;
      p1.hBack = 280;
      p1.skyTiles = new int[]{0, 26, 23};
      p1.topBorderTiles = new int[]{26, 8, 27, 29};
      p1.subSurfaceTiles = new int[]{23, 4};
      p1.groundTiles = new int[]{5, 3, 6, 18, 2, 20};
      p1.bottomBaseTiles = new int[]{19, 11, 10, 16};
      p1.ceilingTiles = new int[]{20, 21, 23, 26};
      p1.platformTiles = new int[]{26, 8};
      p1.layer1Items = List.of((short)25, (short)26);
      p1.layer3Items = List.of((short)89, (short)81, (short)24, (short)84, (short)83, (short)80);
      p1.mobTemplates = List.of(6, 7, 5, 8, 10, 9);
      p1.isIndoorCeiling = true;
      PROFILES.put(AutoMapEngine.BiomeArchetype.STONE_CAVERN, p1);
      AutoMapEngine.BiomeProfile p2 = new AutoMapEngine.BiomeProfile();
      p2.archetype = AutoMapEngine.BiomeArchetype.HARBOR_TOWN;
      p2.tileSetId = 2;
      p2.idBack = 3;
      p2.hBack = 250;
      p2.skyTiles = new int[]{0};
      p2.topBorderTiles = new int[]{5, 4};
      p2.subSurfaceTiles = new int[]{4, 1};
      p2.groundTiles = new int[]{1, 7, 2, 3};
      p2.bottomBaseTiles = new int[]{6, 7};
      p2.platformTiles = new int[]{5, 4};
      p2.layer3Items = List.of((short)27, (short)28, (short)39, (short)40, (short)29, (short)34, (short)35, (short)32);
      p2.mobTemplates = List.of(12, 13, 11, 16, 48, 14);
      PROFILES.put(AutoMapEngine.BiomeArchetype.HARBOR_TOWN, p2);
      AutoMapEngine.BiomeProfile p3 = new AutoMapEngine.BiomeProfile();
      p3.archetype = AutoMapEngine.BiomeArchetype.DEEP_JUNGLE;
      p3.tileSetId = 3;
      p3.idBack = 5;
      p3.hBack = 255;
      p3.skyTiles = new int[]{0, 25, 21};
      p3.topBorderTiles = new int[]{25, 37, 27, 36};
      p3.subSurfaceTiles = new int[]{21, 22, 4};
      p3.groundTiles = new int[]{19, 17, 18, 15, 16};
      p3.bottomBaseTiles = new int[]{19, 28, 29, 30, 31};
      p3.platformTiles = new int[]{25, 37};
      p3.layer0Items = List.of((short)44, (short)45);
      p3.layer1Items = List.of((short)42);
      p3.layer3Items = List.of((short)41, (short)99, (short)300, (short)301, (short)19, (short)14);
      p3.mobTemplates = List.of(19, 18, 17, 114, 61, 20);
      PROFILES.put(AutoMapEngine.BiomeArchetype.DEEP_JUNGLE, p3);
      AutoMapEngine.BiomeProfile p4 = new AutoMapEngine.BiomeProfile();
      p4.archetype = AutoMapEngine.BiomeArchetype.GRAND_PALACE;
      p4.tileSetId = 4;
      p4.idBack = 8;
      p4.hBack = 240;
      p4.skyTiles = new int[]{0, 20, 28};
      p4.topBorderTiles = new int[]{16, 15, 34};
      p4.subSurfaceTiles = new int[]{7, 11, 3};
      p4.groundTiles = new int[]{1, 3, 2, 12};
      p4.bottomBaseTiles = new int[]{14, 42, 40, 41, 12};
      p4.ceilingTiles = new int[]{15, 16, 20};
      p4.platformTiles = new int[]{16, 15};
      p4.layer3Items = List.of((short)52, (short)53, (short)51, (short)48, (short)334, (short)46, (short)50, (short)49);
      p4.mobTemplates = List.of(25, 26, 24, 57, 52, 29);
      p4.isIndoorCeiling = true;
      PROFILES.put(AutoMapEngine.BiomeArchetype.GRAND_PALACE, p4);
      AutoMapEngine.BiomeProfile p5 = new AutoMapEngine.BiomeProfile();
      p5.archetype = AutoMapEngine.BiomeArchetype.PIRATE_GALLEON;
      p5.tileSetId = 5;
      p5.idBack = 7;
      p5.hBack = 300;
      p5.skyTiles = new int[]{0, 10, 21};
      p5.topBorderTiles = new int[]{10, 11, 17, 5};
      p5.subSurfaceTiles = new int[]{21, 8, 22};
      p5.groundTiles = new int[]{7, 2, 19, 4, 6};
      p5.bottomBaseTiles = new int[]{15, 16, 12, 23};
      p5.platformTiles = new int[]{10, 11};
      p5.layer1Items = List.of((short)73, (short)71, (short)72);
      p5.layer2Items = List.of((short)69, (short)64, (short)67, (short)66, (short)68, (short)65);
      p5.layer3Items = List.of((short)98, (short)96, (short)61, (short)94, (short)56, (short)95);
      p5.mobTemplates = List.of(31, 30, 32, 36, 33, 34);
      PROFILES.put(AutoMapEngine.BiomeArchetype.PIRATE_GALLEON, p5);
      AutoMapEngine.BiomeProfile p6 = new AutoMapEngine.BiomeProfile();
      p6.archetype = AutoMapEngine.BiomeArchetype.LOGUETOWN_PLAZA;
      p6.tileSetId = 6;
      p6.idBack = 15;
      p6.hBack = 296;
      p6.skyTiles = new int[]{0};
      p6.topBorderTiles = new int[]{4, 3, 9, 6};
      p6.subSurfaceTiles = new int[]{1, 3, 2};
      p6.groundTiles = new int[]{3, 1, 5, 2};
      p6.bottomBaseTiles = new int[]{8, 3, 7, 5, 6};
      p6.platformTiles = new int[]{4, 3};
      p6.layer3Items = List.of((short)90, (short)89, (short)195, (short)78, (short)310, (short)255, (short)76, (short)74);
      p6.mobTemplates = List.of(38, 37, 39, 132, 133, 43);
      PROFILES.put(AutoMapEngine.BiomeArchetype.LOGUETOWN_PLAZA, p6);
      AutoMapEngine.BiomeProfile p7 = new AutoMapEngine.BiomeProfile();
      p7.archetype = AutoMapEngine.BiomeArchetype.SLUM_OUTPOST;
      p7.tileSetId = 7;
      p7.idBack = 18;
      p7.hBack = 260;
      p7.skyTiles = new int[]{0, 9, 12};
      p7.topBorderTiles = new int[]{7, 9, 12, 8};
      p7.subSurfaceTiles = new int[]{7, 1, 2};
      p7.groundTiles = new int[]{1, 2, 3, 6};
      p7.bottomBaseTiles = new int[]{5, 6, 4, 1};
      p7.platformTiles = new int[]{7, 9};
      p7.layer3Items = List.of((short)104, (short)99, (short)100, (short)103, (short)102, (short)101, (short)19);
      p7.mobTemplates = List.of(65, 66, 64, 59, 63, 67);
      PROFILES.put(AutoMapEngine.BiomeArchetype.SLUM_OUTPOST, p7);
      AutoMapEngine.BiomeProfile p8 = new AutoMapEngine.BiomeProfile();
      p8.archetype = AutoMapEngine.BiomeArchetype.PRIMEVAL_GARDEN;
      p8.tileSetId = 8;
      p8.idBack = 23;
      p8.hBack = 340;
      p8.skyTiles = new int[]{0};
      p8.topBorderTiles = new int[]{5, 4, 20, 8};
      p8.subSurfaceTiles = new int[]{1, 2, 3};
      p8.groundTiles = new int[]{1, 2, 3, 7};
      p8.bottomBaseTiles = new int[]{9, 8, 7, 6};
      p8.platformTiles = new int[]{5, 4};
      p8.layer2Items = List.of((short)0, (short)67, (short)69, (short)68);
      p8.layer3Items = List.of((short)105, (short)107, (short)106, (short)7, (short)8);
      p8.mobTemplates = List.of(144, 141, 82, 165, 166, 80);
      PROFILES.put(AutoMapEngine.BiomeArchetype.PRIMEVAL_GARDEN, p8);
      AutoMapEngine.BiomeProfile p9 = new AutoMapEngine.BiomeProfile();
      p9.archetype = AutoMapEngine.BiomeArchetype.SNOW_ISLAND;
      p9.tileSetId = 9;
      p9.idBack = 20;
      p9.hBack = 300;
      p9.skyTiles = new int[]{0};
      p9.topBorderTiles = new int[]{10, 9, 19};
      p9.subSurfaceTiles = new int[]{1, 2, 6};
      p9.groundTiles = new int[]{1, 2, 7, 8, 6};
      p9.bottomBaseTiles = new int[]{11, 12, 9, 1};
      p9.platformTiles = new int[]{10, 9};
      p9.layer1Items = List.of((short)113, (short)112);
      p9.layer3Items = List.of((short)121, (short)108, (short)110, (short)111, (short)116, (short)109);
      p9.mobTemplates = List.of(76, 73, 75, 59, 72, 77);
      PROFILES.put(AutoMapEngine.BiomeArchetype.SNOW_ISLAND, p9);
      AutoMapEngine.BiomeProfile p10 = new AutoMapEngine.BiomeProfile();
      p10.archetype = AutoMapEngine.BiomeArchetype.ALABASTA_DESERT;
      p10.tileSetId = 10;
      p10.idBack = 31;
      p10.hBack = 270;
      p10.skyTiles = new int[]{0};
      p10.topBorderTiles = new int[]{6, 26, 10, 19};
      p10.subSurfaceTiles = new int[]{6, 27, 22};
      p10.groundTiles = new int[]{6, 22, 19, 18, 20};
      p10.bottomBaseTiles = new int[]{44, 5, 1, 2, 45};
      p10.platformTiles = new int[]{6, 26};
      p10.layer1Items = List.of((short)194);
      p10.layer2Items = List.of((short)131, (short)139, (short)137, (short)125, (short)124, (short)132);
      p10.layer3Items = List.of((short)154, (short)156, (short)155, (short)129, (short)153, (short)130);
      p10.mobTemplates = List.of(83, 82, 84, 95, 59, 97);
      PROFILES.put(AutoMapEngine.BiomeArchetype.ALABASTA_DESERT, p10);
      AutoMapEngine.BiomeProfile p11 = new AutoMapEngine.BiomeProfile();
      p11.archetype = AutoMapEngine.BiomeArchetype.SKY_ANGEL_ISLAND;
      p11.tileSetId = 11;
      p11.idBack = 37;
      p11.hBack = 260;
      p11.skyTiles = new int[]{0};
      p11.topBorderTiles = new int[]{15, 17, 16, 18};
      p11.subSurfaceTiles = new int[]{1, 2, 8, 7};
      p11.groundTiles = new int[]{1, 2, 8, 7, 4};
      p11.bottomBaseTiles = new int[]{12, 18, 13, 19};
      p11.platformTiles = new int[]{15, 17};
      p11.layer0Items = List.of((short)182, (short)183, (short)184, (short)180, (short)179);
      p11.layer1Items = List.of((short)181, (short)161);
      p11.layer3Items = List.of((short)99);
      p11.mobTemplates = List.of(106, 107, 101, 102, 103, 110);
      PROFILES.put(AutoMapEngine.BiomeArchetype.SKY_ANGEL_ISLAND, p11);
      AutoMapEngine.BiomeProfile p12 = new AutoMapEngine.BiomeProfile();
      p12.archetype = AutoMapEngine.BiomeArchetype.CASINO_ROYALE;
      p12.tileSetId = 12;
      p12.idBack = 33;
      p12.hBack = 385;
      p12.skyTiles = new int[]{0};
      p12.topBorderTiles = new int[]{1, 45, 19};
      p12.subSurfaceTiles = new int[]{2, 4};
      p12.groundTiles = new int[]{2, 3, 4, 1};
      p12.bottomBaseTiles = new int[]{5, 6, 2};
      p12.ceilingTiles = new int[]{1, 2, 4};
      p12.platformTiles = new int[]{1, 45};
      p12.layer2Items = List.of((short)191);
      p12.layer3Items = List.of((short)189, (short)190, (short)185, (short)186, (short)188, (short)187);
      p12.mobTemplates = List.of(96, 84, 85, 86, 92);
      p12.isIndoorCeiling = true;
      PROFILES.put(AutoMapEngine.BiomeArchetype.CASINO_ROYALE, p12);
      AutoMapEngine.BiomeProfile p13 = new AutoMapEngine.BiomeProfile();
      p13.archetype = AutoMapEngine.BiomeArchetype.FROZEN_SUMMIT;
      p13.tileSetId = 13;
      p13.idBack = 33;
      p13.hBack = 250;
      p13.skyTiles = new int[]{0};
      p13.topBorderTiles = new int[]{8, 20, 17};
      p13.subSurfaceTiles = new int[]{4, 5, 6};
      p13.groundTiles = new int[]{3, 5, 2, 18, 9};
      p13.bottomBaseTiles = new int[]{19, 11, 10, 9};
      p13.platformTiles = new int[]{8, 20};
      p13.layer1Items = List.of((short)385, (short)384, (short)386);
      p13.layer2Items = List.of((short)387, (short)389, (short)288);
      p13.layer3Items = List.of((short)381, (short)379);
      p13.mobTemplates = List.of(144, 141, 142, 143, 145, 148);
      PROFILES.put(AutoMapEngine.BiomeArchetype.FROZEN_SUMMIT, p13);
      AutoMapEngine.BiomeProfile p14 = new AutoMapEngine.BiomeProfile();
      p14.archetype = AutoMapEngine.BiomeArchetype.WATER_SEVEN_ENIES;
      p14.tileSetId = 19;
      p14.idBack = 63;
      p14.hBack = 385;
      p14.skyTiles = new int[]{0};
      p14.topBorderTiles = new int[]{13, 14, 15, 19};
      p14.subSurfaceTiles = new int[]{1, 2, 3};
      p14.groundTiles = new int[]{4, 7, 5, 8, 10};
      p14.bottomBaseTiles = new int[]{19, 16, 17, 18};
      p14.platformTiles = new int[]{13, 14};
      p14.layer3Items = List.of((short)421, (short)422, (short)416, (short)420, (short)412, (short)414, (short)415, (short)417);
      p14.mobTemplates = List.of(164, 165, 166, 158, 159, 160);
      PROFILES.put(AutoMapEngine.BiomeArchetype.WATER_SEVEN_ENIES, p14);
   }

   public static AutoMapEngine.BiomeProfile getProfile(AutoMapEngine.BiomeArchetype archetype) {
      return PROFILES.getOrDefault(archetype, PROFILES.get(AutoMapEngine.BiomeArchetype.ISLAND_BEACH));
   }

   public static GameMap generateMap(AutoMapEngine.GenConfig config) {
      AutoMapEngine.BiomeArchetype biome = config.biome != null
         ? config.biome
         : (config.type != null ? config.type.toBiome() : AutoMapEngine.BiomeArchetype.ISLAND_BEACH);
      AutoMapEngine.BiomeProfile profile = getProfile(biome);
      int tileSetId = config.tileSetId >= 0 ? config.tileSetId : profile.tileSetId;
      GameMap map = new GameMap(config.id, config.name, config.width, config.height, tileSetId);
      map.IDBack = (byte)(config.idBack >= 0 ? config.idBack : profile.idBack);
      map.HBack = (short)(config.hBack >= 0 ? config.hBack : profile.hBack);
      map.maxW = (short)(config.width * 24);
      map.maxH = (short)(config.height * 24);
      map.level = (byte)Math.max(1, Math.min(100, config.mobLevel));
      map.max_zone = 10;
      map.max_player = 30;
      map.type_view_p = 0;
      map.isOnlinemap = 1;
      map.id_eff_map = -1;
      int[] surfaceHeights = generateTerrainLayers(map, config, profile);
      if (config.enablePlatforms) {
         generatePlatforms(map, config, profile, surfaceHeights);
      }

      if (config.autoItems) {
         generateDecorations(map, config, profile, surfaceHeights);
      }

      if (config.autoMobs) {
         generateMobs(map, config, profile, surfaceHeights);
      }

      if (config.autoVgos) {
         generateWarpGatesAndBoats(map, config, profile, surfaceHeights);
      }

      map.ensureBuffers();
      return map;
   }

   private static int[] generateTerrainLayers(GameMap map, AutoMapEngine.GenConfig config, AutoMapEngine.BiomeProfile profile) {
      int w = map.width;
      int h = map.height;
      int[] surfaceHeights = new int[w];
      Random rand = new Random(config.seed);
      float[][] noise2D = MapNoiseUtils.generateValueNoise(w, h, config.seed + 100L, 0.2F);
      int baseGroundY = Math.max(4, Math.min(h - 5, Math.round(h * (1.0F - config.groundHeightRatio))));
      AutoMapEngine.TerrainStyle style = config.terrainStyle != null ? config.terrainStyle : AutoMapEngine.TerrainStyle.FLAT_WALKWAY;
      int currentX;
      int currentY;
      switch (style) {
         case FLAT_WALKWAY:
            Arrays.fill(surfaceHeights, baseGroundY);
            break;
         case STEP_TERRACES:
            currentX = 0;
            currentY = baseGroundY;

            while (currentX < w) {
               int segLen = 14 + rand.nextInt(12);
               int endX = Math.min(w, currentX + segLen);

               for (int x = currentX; x < endX; x++) {
                  surfaceHeights[x] = currentY;
               }

               currentX = endX;
               if (endX < w) {
                  int delta = rand.nextBoolean() ? 1 : -1;
                  currentY = Math.max(4, Math.min(h - 5, currentY + delta));
               }
            }
            break;
         case GENTLE_HILLS:
            currentX = 0;
            currentY = baseGroundY;
            int trend = rand.nextBoolean() ? 1 : -1;

            while (currentX < w) {
               int segLen = 10 + rand.nextInt(10);
               int endX = Math.min(w, currentX + segLen);

               for (int x = currentX; x < endX; x++) {
                  surfaceHeights[x] = currentY;
               }

               currentX = endX;
               if (endX < w) {
                  currentY = Math.max(4, Math.min(h - 5, currentY + trend));
                  if (currentY >= h - 5 || currentY <= 4 || rand.nextFloat() < 0.4F) {
                     trend = -trend;
                  }
               }
            }
            break;
         case CAVERN_DUNGEON:
            currentX = 0;
            currentY = Math.min(h - 5, baseGroundY + 1);

            while (currentX < w) {
               int segLen = 12 + rand.nextInt(12);
               int endX = Math.min(w, currentX + segLen);

               for (int x = currentX; x < endX; x++) {
                  surfaceHeights[x] = currentY;
               }

               currentX = endX;
               if (endX < w) {
                  int step = rand.nextBoolean() ? 1 : -1;
                  currentY = Math.max(5, Math.min(h - 4, currentY + step));
               }
            }
            break;
         case FLOATING_ISLANDS:
            Arrays.fill(surfaceHeights, baseGroundY);
      }

      int defaultSky = profile.skyTiles != null && profile.skyTiles.length > 0 ? profile.skyTiles[0] : 0;
      int defaultTop = profile.topBorderTiles != null && profile.topBorderTiles.length > 0 ? profile.topBorderTiles[0] : 1;
      int defaultSub = profile.subSurfaceTiles != null && profile.subSurfaceTiles.length > 0 ? profile.subSurfaceTiles[0] : defaultTop;
      int defaultBase = profile.bottomBaseTiles != null && profile.bottomBaseTiles.length > 0 ? profile.bottomBaseTiles[0] : 4;
      boolean hasCeiling = profile.isIndoorCeiling || style == AutoMapEngine.TerrainStyle.CAVERN_DUNGEON;

      for (int x = 0; x < w; x++) {
         int surfY = surfaceHeights[x];

         for (int y = 0; y < h; y++) {
            if (y < surfY) {
               if (hasCeiling && y == 0 && profile.ceilingTiles != null && profile.ceilingTiles.length > 0) {
                  map.setTileId(x, y, profile.ceilingTiles[rand.nextInt(profile.ceilingTiles.length)]);
               } else if (hasCeiling && y == 1 && profile.ceilingTiles != null && profile.ceilingTiles.length > 1) {
                  map.setTileId(x, y, profile.ceilingTiles[1]);
               } else {
                  map.setTileId(x, y, defaultSky);
               }
            } else if (y == surfY) {
               int chosenTop = defaultTop;
               if (profile.topBorderTiles != null && profile.topBorderTiles.length > 1 && rand.nextFloat() < 0.15F) {
                  chosenTop = profile.topBorderTiles[rand.nextInt(profile.topBorderTiles.length)];
               }

               map.setTileId(x, y, chosenTop);
            } else if (y == surfY + 1 && y < h - 2) {
               int chosenSub = defaultSub;
               if (profile.subSurfaceTiles != null && profile.subSurfaceTiles.length > 1) {
                  chosenSub = profile.subSurfaceTiles[rand.nextInt(profile.subSurfaceTiles.length)];
               }

               map.setTileId(x, y, chosenSub);
            } else if (y >= h - 2) {
               int chosenBase = defaultBase;
               if (profile.bottomBaseTiles != null && profile.bottomBaseTiles.length > 1) {
                  chosenBase = profile.bottomBaseTiles[rand.nextInt(profile.bottomBaseTiles.length)];
               }

               map.setTileId(x, y, chosenBase);
            } else {
               int chosenGround = pickWeightedTile(profile.groundTiles, noise2D[y][x], rand);
               map.setTileId(x, y, chosenGround);
            }
         }
      }

      return surfaceHeights;
   }

   private static int pickWeightedTile(int[] tiles, float noiseVal, Random rand) {
      if (tiles != null && tiles.length != 0) {
         if (tiles.length == 1) {
            return tiles[0];
         } else if (noiseVal < 0.7F) {
            return tiles[0];
         } else {
            int detailIdx = 1 + rand.nextInt(tiles.length - 1);
            return tiles[detailIdx];
         }
      } else {
         return 1;
      }
   }

   private static void generatePlatforms(GameMap map, AutoMapEngine.GenConfig config, AutoMapEngine.BiomeProfile profile, int[] surfaceHeights) {
      int[] platTiles = profile.platformTiles != null && profile.platformTiles.length > 0
         ? profile.platformTiles
         : (profile.topBorderTiles != null && profile.topBorderTiles.length > 0 ? profile.topBorderTiles : new int[]{1});
      int w = map.width;
      int h = map.height;
      Random rand = new Random(config.seed + 200L);
      int numPlatforms = Math.max(1, Math.min(3, (int)(w * config.platformDensity * 0.08F)));
      int platformTile = platTiles[0];
      int minGround = h;

      for (int x = 0; x < w; x++) {
         minGround = Math.min(minGround, surfaceHeights[x]);
      }

      int py = Math.max(2, minGround - 3);
      int spacing = (w - 16) / (numPlatforms + 1);

      for (int i = 0; i < numPlatforms; i++) {
         int pLen = 5 + rand.nextInt(4);
         int px = 8 + i * spacing + rand.nextInt(3);
         if (px + pLen < w - 6) {
            for (int dx = 0; dx < pLen; dx++) {
               map.setTileId(px + dx, py, platformTile);
            }
         }
      }
   }

   private static void generateDecorations(GameMap map, AutoMapEngine.GenConfig config, AutoMapEngine.BiomeProfile profile, int[] surfaceHeights) {
      map.items.clear();
      int w = map.width;
      Random rand = new Random(config.seed + 300L);
      int targetItems = Math.max(4, (int)(w * config.itemDensity * 1.2F));
      int placed = 0;
      List<Short> trees = !profile.layer6Items.isEmpty() ? profile.layer6Items : profile.layer1Items;
      if (!trees.isEmpty()) {
         int treeSpacing = Math.max(10, w / 4);

         for (int tx = 5; tx < w - 7; tx += treeSpacing + rand.nextInt(4)) {
            if (tx + 1 < w && surfaceHeights[tx] == surfaceHeights[tx + 1]) {
               int groundY = surfaceHeights[tx];
               short tmpl = trees.get(rand.nextInt(trees.size()));
               map.addItem(tmpl, tx, groundY);
               placed++;
            }
         }
      }

      if (!profile.layer3Items.isEmpty()) {
         int numClusters = Math.max(1, w / 16);

         for (int c = 0; c < numClusters && placed < targetItems; c++) {
            int clusterCenterX = 6 + rand.nextInt(Math.max(1, w - 12));
            int clusterSize = 2;

            for (int ci = 0; ci < clusterSize; ci++) {
               int propX = clusterCenterX + ci;
               if (propX >= 2 && propX < w - 2) {
                  int groundY = surfaceHeights[propX];
                  if (map.getItemAt(propX, groundY) == null) {
                     short tmpl = profile.layer3Items.get(rand.nextInt(profile.layer3Items.size()));
                     map.addItem(tmpl, propX, groundY);
                     placed++;
                  }
               }
            }
         }
      }

      List<Short> foliage = !profile.layer5Items.isEmpty() ? profile.layer5Items : profile.layer2Items;
      if (!foliage.isEmpty()) {
         for (int attempts = 0; attempts < 25 && placed < targetItems + 4; attempts++) {
            int fx = 3 + rand.nextInt(Math.max(1, w - 6));
            int groundY = surfaceHeights[fx];
            if (map.getItemAt(fx, groundY) == null) {
               short tmpl = foliage.get(rand.nextInt(foliage.size()));
               map.addItem(tmpl, fx, groundY);
               placed++;
            }
         }
      }
   }

   private static void generateMobs(GameMap map, AutoMapEngine.GenConfig config, AutoMapEngine.BiomeProfile profile, int[] surfaceHeights) {
      map.list_mob.clear();
      List<Integer> pool = !config.mobTemplates.isEmpty() ? config.mobTemplates : profile.mobTemplates;
      if (!pool.isEmpty() && config.mobCount > 0) {
         int w = map.width;
         Random rand = new Random(config.seed + 400L);
         int placed = 0;
         int spacing = Math.max(3, (w - 10) / Math.max(1, config.mobCount));

         for (int x = 5; x < w - 5 && placed < config.mobCount; x += spacing + rand.nextInt(3)) {
            int groundY = surfaceHeights[x];
            GameMap.Mob mob = new GameMap.Mob();
            mob.templateId = pool.get(rand.nextInt(pool.size()));
            mob.x = (short)(x * 24 + 12);
            mob.y = (short)(groundY * 24);
            map.list_mob.add(mob);
            placed++;
         }
      }
   }

   private static void generateWarpGatesAndBoats(GameMap map, AutoMapEngine.GenConfig config, AutoMapEngine.BiomeProfile profile, int[] surfaceHeights) {
      map.vgos.clear();
      map.list_boat.clear();
      int w = map.width;
      if (config.targetLeftMapId > 0) {
         int leftY = surfaceHeights[1];
         GameMap.Vgo leftVgo = new GameMap.Vgo();
         leftVgo.id_map_go = config.targetLeftMapId;
         leftVgo.xold = 24;
         leftVgo.yold = (short)(leftY * 24);
         leftVgo.xnew = (short)(w * 24 - 48);
         leftVgo.ynew = 250;
         map.vgos.add(leftVgo);
      }

      short rightTarget = config.targetRightMapId > 0 ? config.targetRightMapId : config.targetMapId;
      if (rightTarget > 0) {
         int rightX = w - 2;
         int rightY = surfaceHeights[rightX];
         GameMap.Vgo rightVgo = new GameMap.Vgo();
         rightVgo.id_map_go = rightTarget;
         rightVgo.xold = (short)(rightX * 24);
         rightVgo.yold = (short)(rightY * 24);
         rightVgo.xnew = 48;
         rightVgo.ynew = 250;
         map.vgos.add(rightVgo);
      }

      if (profile.hasWaterAtBottom) {
         GameMap.Boat_In_Map boat = new GameMap.Boat_In_Map();
         boat.x = 48;
         boat.y = (short)((map.height - 2) * 24);
         map.list_boat.add(boat);
      }
   }

   static {
      initProfiles();
   }

   public static enum BiomeArchetype {
      ISLAND_BEACH("0: Biển Đảo / Làng Cối Xay Gió", 0, 1, 260),
      STONE_CAVERN("1: Hang Đá / Hầm Mỏ Khai Thác", 1, 4, 280),
      HARBOR_TOWN("2: Phố Cảng / Thị Trấn Orange", 2, 3, 250),
      DEEP_JUNGLE("3: Rừng Rậm Siphon", 3, 5, 255),
      GRAND_PALACE("4: Cung Điện / Nhà Hàng Baratie", 4, 8, 240),
      PIRATE_GALLEON("5: Boong Tàu / Chiến Hạm Hải Tặc", 5, 7, 300),
      LOGUETOWN_PLAZA("6: Đại Đô Thị / Quảng Trường Loguetown", 6, 15, 296),
      SLUM_OUTPOST("7: Khu Ổ Chuột / Whiskey Peak", 7, 18, 260),
      PRIMEVAL_GARDEN("8: Rừng Cổ Đại Little Garden", 8, 23, 340),
      SNOW_ISLAND("9: Đảo Tuyết Drum / Thị Trấn Horn", 9, 20, 300),
      ALABASTA_DESERT("10: Sa Mạc Alabasta & Lăng Mộ", 10, 31, 270),
      SKY_ANGEL_ISLAND("11: Đảo Trên Mây Skypiea", 11, 37, 260),
      CASINO_ROYALE("12: Sòng Bạc Hoàng Gia VIP", 12, 33, 385),
      FROZEN_SUMMIT("13: Đỉnh Băng Giá Vĩnh Cửu", 13, 33, 250),
      WATER_SEVEN_ENIES("14: Kinh Đô Nước & Pháo Đài Tư Pháp", 19, 63, 385);

      public final String displayName;
      public final int defaultTileSet;
      public final int defaultIdBack;
      public final int defaultHBack;

      private BiomeArchetype(String displayName, int defaultTileSet, int defaultIdBack, int defaultHBack) {
         this.displayName = displayName;
         this.defaultTileSet = defaultTileSet;
         this.defaultIdBack = defaultIdBack;
         this.defaultHBack = defaultHBack;
      }

      @Override
      public String toString() {
         return this.displayName;
      }
   }

   public static class BiomeProfile {
      public AutoMapEngine.BiomeArchetype archetype;
      public int tileSetId;
      public int idBack;
      public int hBack;
      public int[] skyTiles;
      public int[] topBorderTiles;
      public int[] subSurfaceTiles;
      public int[] groundTiles;
      public int[] bottomBaseTiles;
      public int[] ceilingTiles;
      public int[] platformTiles;
      public List<Short> layer0Items = new ArrayList<>();
      public List<Short> layer1Items = new ArrayList<>();
      public List<Short> layer2Items = new ArrayList<>();
      public List<Short> layer3Items = new ArrayList<>();
      public List<Short> layer4Items = new ArrayList<>();
      public List<Short> layer5Items = new ArrayList<>();
      public List<Short> layer6Items = new ArrayList<>();
      public List<Integer> mobTemplates = new ArrayList<>();
      public List<Short> npcTemplates = new ArrayList<>();
      public boolean hasWaterAtBottom = false;
      public boolean isIndoorCeiling = false;
   }

   public static class GenConfig {
      public int id = 999;
      public String name = "Auto Map";
      public int width = 50;
      public int height = 17;
      public AutoMapEngine.BiomeArchetype biome = AutoMapEngine.BiomeArchetype.ISLAND_BEACH;
      public AutoMapEngine.TerrainType type = AutoMapEngine.TerrainType.ISLAND;
      public AutoMapEngine.TerrainStyle terrainStyle = AutoMapEngine.TerrainStyle.FLAT_WALKWAY;
      public int tileSetId = -1;
      public int idBack = -1;
      public int hBack = -1;
      public long seed = System.currentTimeMillis();
      public float groundHeightRatio = 0.45F;
      public float hillRoughness = 0.35F;
      public boolean enablePlatforms = true;
      public float platformDensity = 0.25F;
      public float landRatio = 0.65F;
      public float detailRatio = 0.2F;
      public boolean autoItems = true;
      public float itemDensity = 0.12F;
      public List<Short> itemTemplates = new ArrayList<>();
      public boolean autoMobs = true;
      public int mobCount = 8;
      public int mobLevel = 10;
      public List<Integer> mobTemplates = new ArrayList<>();
      public boolean autoNpcs = false;
      public List<Short> npcTemplates = new ArrayList<>();
      public boolean autoVgos = true;
      public short targetLeftMapId = -1;
      public short targetRightMapId = 1;
      public short targetMapId = 1;
   }

   public static enum TerrainStyle {
      FLAT_WALKWAY("Đồng Bằng / Bến Cảng Phẳng (Tiêu Chuẩn)"),
      STEP_TERRACES("Bậc Thang Giật Cấp (2-3 Tầng Rộng)"),
      GENTLE_HILLS("Đồi Núi Thoải (Plateau Uốn Lượn)"),
      CAVERN_DUNGEON("Hầm Ngục / Hang Động Khép Kín"),
      FLOATING_ISLANDS("Quần Đảo Mây / Lơ Lửng Skypiea");

      public final String displayName;

      private TerrainStyle(String displayName) {
         this.displayName = displayName;
      }

      @Override
      public String toString() {
         return this.displayName;
      }
   }

   public static enum TerrainType {
      ISLAND,
      DUNGEON,
      FOREST,
      INTERIOR,
      OCEAN;

      public AutoMapEngine.BiomeArchetype toBiome() {
         switch (this) {
            case ISLAND:
            case OCEAN:
            default:
               return AutoMapEngine.BiomeArchetype.ISLAND_BEACH;
            case DUNGEON:
               return AutoMapEngine.BiomeArchetype.STONE_CAVERN;
            case FOREST:
               return AutoMapEngine.BiomeArchetype.DEEP_JUNGLE;
            case INTERIOR:
               return AutoMapEngine.BiomeArchetype.GRAND_PALACE;
         }
      }
   }
}
