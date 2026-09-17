package com.deplor.haitactihontool.map;

import org.json.JSONObject;

public class MapDataGenerator {
   public static String generateSQL(GameMap map) {
      if (map == null) {
         return "";
      } else {
         try {
            JSONObject json = MapDataExporter.exportToSqlJson(map);
            StringBuilder sb = new StringBuilder();
            sb.append(
               "INSERT INTO `maps` (`id`, `name`, `mobs`, `maxzone`, `maxplayer`, `npcs`, `boat`, `typeViewPlayer`, `b`, `specMap`, `vgos`, `IDBack`, `HBack`, `maxW`, `maxH`, `w`, `h`, `tile_id`, `id_eff_map`, `level`, `typeChangeMap`, `mPosMapTrain`, `strTimeChange`) VALUES ("
            );
            sb.append(json.getInt("id")).append(", ");
            sb.append("'").append(json.getString("name").replace("'", "''")).append("', ");
            sb.append("'").append(json.getJSONArray("mobs").toString()).append("', ");
            sb.append(json.getInt("maxzone")).append(", ");
            sb.append(json.getInt("maxplayer")).append(", ");
            sb.append("'").append(json.getJSONArray("npcs").toString()).append("', ");
            sb.append("'").append(json.getJSONArray("boat").toString()).append("', ");
            sb.append(json.getInt("typeViewPlayer")).append(", ");
            sb.append(json.getInt("b")).append(", ");
            sb.append(json.getInt("specMap")).append(", ");
            sb.append("'").append(json.getJSONArray("vgos").toString()).append("', ");
            sb.append(json.getInt("IDBack")).append(", ");
            sb.append(json.getInt("HBack")).append(", ");
            sb.append(json.getInt("maxW")).append(", ");
            sb.append(json.getInt("maxH")).append(", ");
            sb.append(json.getInt("w")).append(", ");
            sb.append(json.getInt("h")).append(", ");
            sb.append(json.getInt("tile_id")).append(", ");
            sb.append(json.getInt("id_eff_map")).append(", ");
            sb.append(json.getInt("level")).append(", ");
            sb.append(json.getInt("typeChangeMap")).append(", ");
            sb.append("'").append(json.getJSONArray("mPosMapTrain").toString()).append("', ");
            sb.append("'").append(json.optString("strTimeChange", "").replace("'", "''")).append("'");
            sb.append(");");
            return sb.toString();
         } catch (Exception var3) {
            return "/* Error generating SQL: " + var3.getMessage() + " */";
         }
      }
   }

   public static String generateUpdateSQL(GameMap map) {
      if (map == null) {
         return "";
      } else {
         try {
            JSONObject json = MapDataExporter.exportToSqlJson(map);
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE `maps` SET ");
            sb.append("`name`='").append(json.getString("name").replace("'", "''")).append("', ");
            sb.append("`mobs`='").append(json.getJSONArray("mobs").toString()).append("', ");
            sb.append("`maxzone`=").append(json.getInt("maxzone")).append(", ");
            sb.append("`maxplayer`=").append(json.getInt("maxplayer")).append(", ");
            sb.append("`npcs`='").append(json.getJSONArray("npcs").toString()).append("', ");
            sb.append("`boat`='").append(json.getJSONArray("boat").toString()).append("', ");
            sb.append("`typeViewPlayer`=").append(json.getInt("typeViewPlayer")).append(", ");
            sb.append("`b`=").append(json.getInt("b")).append(", ");
            sb.append("`specMap`=").append(json.getInt("specMap")).append(", ");
            sb.append("`vgos`='").append(json.getJSONArray("vgos").toString()).append("', ");
            sb.append("`IDBack`=").append(json.getInt("IDBack")).append(", ");
            sb.append("`HBack`=").append(json.getInt("HBack")).append(", ");
            sb.append("`maxW`=").append(json.getInt("maxW")).append(", ");
            sb.append("`maxH`=").append(json.getInt("maxH")).append(", ");
            sb.append("`w`=").append(json.getInt("w")).append(", ");
            sb.append("`h`=").append(json.getInt("h")).append(", ");
            sb.append("`tile_id`=").append(json.getInt("tile_id")).append(", ");
            sb.append("`id_eff_map`=").append(json.getInt("id_eff_map")).append(", ");
            sb.append("`level`=").append(json.getInt("level")).append(", ");
            sb.append("`typeChangeMap`=").append(json.getInt("typeChangeMap")).append(", ");
            sb.append("`mPosMapTrain`='").append(json.getJSONArray("mPosMapTrain").toString()).append("', ");
            sb.append("`strTimeChange`='").append(json.optString("strTimeChange", "").replace("'", "''")).append("' ");
            sb.append("WHERE `id`=").append(json.getInt("id")).append(";");
            return sb.toString();
         } catch (Exception var3) {
            return "/* Error generating UPDATE SQL: " + var3.getMessage() + " */";
         }
      }
   }

   public static String generateJSON(GameMap map) {
      if (map == null) {
         return "{}";
      } else {
         try {
            return MapDataExporter.exportToSqlJson(map).toString(2);
         } catch (Exception var2) {
            return "{\"error\": \"" + var2.getMessage() + "\"}";
         }
      }
   }
}
