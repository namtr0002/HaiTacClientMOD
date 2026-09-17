import com.deplor.haitactihontool.map.AutoMapEngine;
import com.deplor.haitactihontool.map.GameMap;
import com.deplor.haitactihontool.map.MapDataGenerator;
import com.deplor.haitactihontool.map.BatchMapExporter;

import java.util.List;

public class TestAutoMapEngine {
    public static void main(String[] args) {
        System.out.println("=== TESTING AUTOMAPENGINE V6.0 PROCEDURAL GENERATOR ===");

        int testId = 1001;
        for (AutoMapEngine.BiomeArchetype biome : AutoMapEngine.BiomeArchetype.values()) {
            AutoMapEngine.GenConfig config = new AutoMapEngine.GenConfig();
            config.id = testId++;
            config.name = "Test " + biome.name();
            config.width = 50;
            config.height = 17;
            config.biome = biome;
            config.seed = 123456789L + testId;
            config.autoItems = true;
            config.autoMobs = true;
            config.autoVgos = true;

            GameMap map = AutoMapEngine.generateMap(config);

            System.out.println(String.format("Biome [%-20s] -> ID: %d, Tileset: %d, IDBack: %d, HBack: %d, Items: %d, Mobs: %d, VGOs: %d",
                    biome.name(), map.id, map.getTileSetId(), map.IDBack, map.HBack,
                    map.items != null ? map.items.size() : 0,
                    map.list_mob != null ? map.list_mob.size() : 0,
                    map.vgos != null ? map.vgos.size() : 0));

            // Verify basic sanity
            if (map.mapPaint == null || map.mapPaint.length != map.width * map.height) {
                throw new RuntimeException("Invalid mapPaint array length for " + biome);
            }
            if (map.mapType == null || map.mapType.length != map.width * map.height) {
                throw new RuntimeException("Invalid mapType array length for " + biome);
            }

            // Verify SQL generation
            String sql = MapDataGenerator.generateSQL(map);
            if (sql == null || !sql.startsWith("INSERT INTO `maps`")) {
                throw new RuntimeException("Invalid SQL output for map " + map.id);
            }
        }

        System.out.println("\n=== TESTING TERRAIN STYLES & HEIGHTMAP CONTINUITY ===");
        for (AutoMapEngine.TerrainStyle style : AutoMapEngine.TerrainStyle.values()) {
            AutoMapEngine.GenConfig cfg = new AutoMapEngine.GenConfig();
            cfg.terrainStyle = style;
            cfg.width = 40;
            cfg.height = 17;
            cfg.groundHeightRatio = 0.45f;
            cfg.hillRoughness = 0.4f;
            cfg.seed = 42L;
            GameMap map = AutoMapEngine.generateMap(cfg);

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Style [%-16s] -> Ground Ys: ", style.name()));
            int prevY = -1;
            int stepCount = 0;
            for (int x = 0; x < map.width; x++) {
                int surfY = -1;
                for (int y = (style == AutoMapEngine.TerrainStyle.CAVERN_DUNGEON ? 2 : 0); y < map.height; y++) {
                    if (map.getTileId(x, y) != 0) {
                        surfY = y;
                        break;
                    }
                }
                sb.append(surfY).append(" ");
                if (prevY != -1 && surfY != prevY) {
                    stepCount++;
                }
                prevY = surfY;
            }
            System.out.println(sb.toString() + " | (Steps: " + stepCount + ")");
        }

        System.out.println("\n>>> ALL AUTOMAPENGINE TESTS PASSED SUCCESSFULLY! <<<");
    }
}
