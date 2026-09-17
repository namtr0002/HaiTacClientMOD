import com.deplor.haitactihontool.map.AutoMapEngine;
import com.deplor.haitactihontool.map.GameMap;
import com.deplor.haitactihontool.map.ItemMapEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class GenerateAllMapsVisualTest {
    public static void main(String[] args) throws Exception {
        JSONArray allMaps = new JSONArray();

        for (AutoMapEngine.BiomeArchetype biome : AutoMapEngine.BiomeArchetype.values()) {
            for (AutoMapEngine.TerrainStyle style : AutoMapEngine.TerrainStyle.values()) {
                AutoMapEngine.GenConfig config = new AutoMapEngine.GenConfig();
                config.id = 1000 + biome.ordinal() * 10 + style.ordinal();
                config.name = biome.name() + "_" + style.name();
                config.width = 45;
                config.height = 17;
                config.biome = biome;
                config.terrainStyle = style;
                config.seed = 42L + config.id;
                config.enablePlatforms = true;
                config.autoItems = true;
                config.autoMobs = true;
                config.autoVgos = true;

                GameMap map = AutoMapEngine.generateMap(config);

                JSONObject mapObj = new JSONObject();
                mapObj.put("id", map.id);
                mapObj.put("name", map.name);
                mapObj.put("biome", biome.name());
                mapObj.put("style", style.name());
                mapObj.put("width", map.width);
                mapObj.put("height", map.height);
                mapObj.put("tileSetId", map.getTileSetId());
                mapObj.put("idBack", (int) map.IDBack);
                mapObj.put("hBack", (int) map.HBack);

                JSONArray gridArr = new JSONArray();
                for (int y = 0; y < map.height; y++) {
                    JSONArray row = new JSONArray();
                    for (int x = 0; x < map.width; x++) {
                        row.put(map.getTileId(x, y));
                    }
                    gridArr.put(row);
                }
                mapObj.put("grid", gridArr);

                JSONArray itemsArr = new JSONArray();
                if (map.items != null) {
                    for (ItemMapEntity it : map.items) {
                        JSONObject itObj = new JSONObject();
                        itObj.put("templateId", it.templateId);
                        itObj.put("tileX", it.tileX);
                        itObj.put("tileY", it.tileY);
                        itemsArr.put(itObj);
                    }
                }
                mapObj.put("items", itemsArr);

                allMaps.put(mapObj);
            }
        }

        File outFile = new File("scratch/all_generated_maps.json");
        outFile.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(outFile, StandardCharsets.UTF_8)) {
            fw.write(allMaps.toString(2));
        }

        System.out.println("Exported " + allMaps.length() + " generated maps to scratch/all_generated_maps.json");
    }
}
