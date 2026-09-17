package com.deplor.haitactihontool.map;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GenerateAllMapsVisualTest {

    public static void main(String[] args) {
        System.out.println("Starting procedural map generation for all 15 biomes x 5 terrain styles...");
        
        List<GameMap> generatedMaps = new ArrayList<>();
        int mapIdCounter = 1000;

        File pngDir = new File("scratch/rendered_accurate_maps");
        pngDir.mkdirs();

        for (AutoMapEngine.BiomeArchetype biome : AutoMapEngine.BiomeArchetype.values()) {
            for (AutoMapEngine.TerrainStyle style : AutoMapEngine.TerrainStyle.values()) {
                AutoMapEngine.GenConfig config = new AutoMapEngine.GenConfig();
                config.id = mapIdCounter;
                config.name = biome.name() + "_" + style.name();
                config.width = 50;
                config.height = 17;
                config.biome = biome;
                config.terrainStyle = style;
                config.autoItems = true;
                config.autoMobs = true;
                config.autoVgos = true;
                config.seed = 12345L + mapIdCounter;

                GameMap map = AutoMapEngine.generateMap(config);
                generatedMaps.add(map);

                // Export to PNG using MapImageExporter
                File pngFile = new File(pngDir, map.id + "_" + config.name + ".png");
                try {
                    MapImageExporter.exportMapToPng(map, pngFile);
                } catch (Exception e) {
                    System.err.println("Error exporting PNG: " + e.getMessage());
                }

                mapIdCounter++;
            }
        }

        System.out.println("Generated " + generatedMaps.size() + " maps successfully.");

        // Export all generated maps to JSON with tile and item data
        JSONArray mapsArray = new JSONArray();
        for (GameMap m : generatedMaps) {
            JSONObject jobj = new JSONObject();
            jobj.put("id", m.id);
            jobj.put("name", m.name);
            jobj.put("w", m.width);
            jobj.put("h", m.height);
            jobj.put("tile_id", m.getTileSetId());
            jobj.put("IDBack", m.IDBack);
            jobj.put("HBack", m.HBack);

            JSONArray tilesArr = new JSONArray();
            if (m.mapPaint != null) {
                for (int t : m.mapPaint) {
                    tilesArr.put(t);
                }
            }
            jobj.put("tiles", tilesArr);

            JSONArray itemsArr = new JSONArray();
            if (m.items != null) {
                for (ItemMapEntity it : m.items) {
                    JSONArray itemTuple = new JSONArray();
                    itemTuple.put(it.templateId);
                    itemTuple.put(it.tileX);
                    itemTuple.put(it.tileY);
                    itemsArr.put(itemTuple);
                }
            }
            jobj.put("items", itemsArr);

            mapsArray.put(jobj);
        }

        File outFile = new File("scratch/all_generated_maps.json");
        outFile.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(outFile, StandardCharsets.UTF_8)) {
            fw.write(mapsArray.toString(2));
            System.out.println("Saved all generated maps to: " + outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
