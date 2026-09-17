package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.AppConfig;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class SQLMapLoader {
   public List<GameMap> loadAllMaps(String basePath) {
      new ArrayList();
      List<GameMap> maps = this.loadFromSQLFile(basePath);
      if (!maps.isEmpty()) {
         System.out.println("[SQLMapLoader] Loaded " + maps.size() + " maps from SQL File");
         return maps;
      } else {
         maps = this.loadFromJSON(basePath);
         if (!maps.isEmpty()) {
            System.out.println("[SQLMapLoader] Loaded " + maps.size() + " maps from JSON File");
            return maps;
         } else {
            maps = this.loadFromBinary(basePath);
            if (!maps.isEmpty()) {
               System.out.println("[SQLMapLoader] Loaded " + maps.size() + " maps from Binary Files");
               return maps;
            } else {
               System.out.println("[SQLMapLoader] No maps found in local Data directory");
               return maps;
            }
         }
      }
   }

   public static void attachBinaryData(List<GameMap> maps, String basePath) {
      if (maps != null) {
         String binDir1 = basePath != null && !basePath.isEmpty()
            ? basePath + "/Data/Map/ServerData/binary/"
            : AppConfig.getPath("Data/Map/ServerData/binary/");
         String binDir2 = basePath != null && !basePath.isEmpty() ? basePath + "/Data/Map/binary/" : AppConfig.getPath("Data/Map/binary/");

         for (GameMap m : maps) {
            if ((m.mapPaint == null || m.mapPaint.length == 0) && !loadMapFromBinaryFiles(m, binDir1)) {
               loadMapFromBinaryFiles(m, binDir2);
            }

            m.ensureBuffers();
         }
      }
   }

   public List<GameMap> loadFromSQLFile(String basePath) {
      List<GameMap> maps = new ArrayList<>();
      String sqlPath = basePath != null && !basePath.isEmpty() ? basePath + "/Data/Map/ServerData/maps.sql" : AppConfig.getPath("Data/Map/ServerData/maps.sql");
      File file = new File(sqlPath);
      if (file.isDirectory()) {
         file = new File(file, "maps.sql");
      }
      if (!file.exists()) {
         file = new File(AppConfig.getPath("Data/maps.sql"));
         if (!file.exists()) {
            return maps;
         }
      }

      try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
         StringBuilder currentStmt = new StringBuilder();

         String line;
         while ((line = br.readLine()) != null) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("--")
               && !trimmed.startsWith("/*")
               && !trimmed.startsWith("#")
               && (trimmed.toUpperCase().startsWith("INSERT INTO") || currentStmt.length() > 0)) {
               currentStmt.append(line).append("\n");
               if (trimmed.endsWith(";")) {
                  String stmt = currentStmt.toString();
                  currentStmt.setLength(0);

                  try {
                     List<GameMap> parsedList = parseInsertStatement(stmt);
                     if (parsedList != null && !parsedList.isEmpty()) {
                        maps.addAll(parsedList);
                     }
                  } catch (Exception var12) {
                  }
               }
            }
         }

         attachBinaryData(maps, basePath);
      } catch (Exception var14) {
         System.err.println("[SQLMapLoader] Error reading SQL file: " + var14.getMessage());
      }

      return maps;
   }

   public List<GameMap> loadFromJSON(String basePath) {
      String jsonPath = basePath != null && !basePath.isEmpty()
         ? basePath + "/Data/Map/ServerData/maps.json"
         : AppConfig.getResolvedPath("path_map", "Data/Map/ServerData/maps.json");
      File file = new File(jsonPath);
      if (file.isDirectory()) {
         file = new File(file, "maps.json");
         jsonPath = file.getPath();
      }
      if (!file.exists()) {
         file = new File(AppConfig.getPath("Data/maps.json"));
         if (!file.exists()) {
            return new ArrayList<>();
         }

         jsonPath = file.getPath();
      }

      try {
         List<GameMap> maps = MapDataExporter.loadAllFromJsonFile(jsonPath);
         attachBinaryData(maps, basePath);
         return maps;
      } catch (IOException var5) {
         System.err.println("[SQLMapLoader] Error loading JSON: " + var5.getMessage());
         return new ArrayList<>();
      }
   }

   public List<GameMap> loadFromBinary(String basePath) {
      List<GameMap> maps = new ArrayList<>();
      String binaryPath = basePath != null && !basePath.isEmpty() ? basePath + "/Data/Map/ServerData/binary/" : AppConfig.getPath("Data/Map/ServerData/binary/");
      File dir = new File(binaryPath);
      if (!dir.exists() || !dir.isDirectory()) {
         dir = new File(AppConfig.getPath("Data/Map/binary/"));
         if (!dir.exists() || !dir.isDirectory()) {
            return maps;
         }

         binaryPath = dir.getPath() + "/";
      }

      File[] files = dir.listFiles();
      if (files == null) {
         return maps;
      } else {
         for (File f : files) {
            if (f.isFile() && f.getName().endsWith("_data")) {
               try {
                  String idStr = f.getName().replace("_data", "");
                  int id = Integer.parseInt(idStr);
                  GameMap map = new GameMap();
                  map.id = id;
                  map.name = "Map " + id;
                  loadMapFromBinaryFiles(map, binaryPath);
                  maps.add(map);
               } catch (NumberFormatException var13) {
               }
            }
         }

         attachBinaryData(maps, basePath);
         return maps;
      }
   }

   public static boolean loadMapFromBinaryFiles(GameMap map, String dirPath) {
      if (map != null && dirPath != null) {
         File dir = new File(dirPath);
         if (!dir.exists()) {
            return false;
         } else {
            File dataFile = new File(dir, map.id + "_data");
            if (!dataFile.exists()) {
               return false;
            } else {
               try {
                  byte[] blockBytes = Files.readAllBytes(dataFile.toPath());
                  MapDataExporter.parseBlockData(map, blockBytes);
                  File itemFile = new File(dir, map.id + "_item");
                  if (itemFile.exists()) {
                     byte[] itemBytes = Files.readAllBytes(itemFile.toPath());
                     MapDataExporter.parseItemData(map, itemBytes);
                  }

                  return true;
               } catch (Exception var7) {
                  return false;
               }
            }
         }
      } else {
         return false;
      }
   }

   public static List<GameMap> parseInsertStatement(String sql) throws Exception {
      List<GameMap> result = new ArrayList<>();
      sql = sql.trim();
      int insertIdx = sql.toUpperCase().indexOf("INSERT INTO");
      int valuesIdx = sql.toUpperCase().indexOf("VALUES");
      if (insertIdx != -1 && valuesIdx != -1) {
         List<String> explicitCols = new ArrayList<>();
         int lp = sql.indexOf(40, insertIdx);
         if (lp != -1 && lp < valuesIdx) {
            int rp = sql.indexOf(41, lp);
            if (rp != -1 && rp < valuesIdx) {
               String colPart = sql.substring(lp + 1, rp);

               for (String col : colPart.split(",")) {
                  explicitCols.add(col.trim().replace("`", "").replace("\"", ""));
               }
            }
         }

         String valuesPart = sql.substring(valuesIdx + 6).trim();
         if (valuesPart.endsWith(";")) {
            valuesPart = valuesPart.substring(0, valuesPart.length() - 1).trim();
         }

         for (String tupleStr : extractTuples(valuesPart)) {
            try {
               GameMap map = parseSingleTuple(tupleStr, explicitCols);
               if (map != null) {
                  result.add(map);
               }
            } catch (Exception var12) {
            }
         }

         return result;
      } else {
         throw new Exception("Invalid SQL: Must be an INSERT INTO statement");
      }
   }

   private static List<String> extractTuples(String text) {
      List<String> tuples = new ArrayList<>();
      boolean inString = false;
      boolean escape = false;
      int depth = 0;
      int start = -1;

      for (int i = 0; i < text.length(); i++) {
         char c = text.charAt(i);
         if (escape) {
            escape = false;
         } else if (c == '\\') {
            escape = true;
         } else if (c == '\'') {
            if (inString && i + 1 < text.length() && text.charAt(i + 1) == '\'') {
               i++;
            } else {
               inString = !inString;
            }
         } else if (!inString) {
            if (c == '(') {
               if (++depth == 1) {
                  start = i + 1;
               }
            } else if (c == ')') {
               if (--depth == 0 && start != -1) {
                  tuples.add(text.substring(start, i));
                  start = -1;
               }
            }
         }
      }

      if (tuples.isEmpty()) {
         int firstParen = text.indexOf(40);
         int lastParen = text.lastIndexOf(41);
         if (firstParen != -1 && lastParen > firstParen) {
            tuples.add(text.substring(firstParen + 1, lastParen));
         }
      }

      return tuples;
   }

   private static GameMap parseSingleTuple(String tupleStr, List<String> explicitCols) {
      List<String> values = splitTupleValues(tupleStr);
      if (values.isEmpty()) {
         return null;
      } else {
         GameMap map = new GameMap();
         List<String> colNames = new ArrayList<>(explicitCols);
         if (colNames.isEmpty()) {
            if (values.size() >= 23) {
               String[] newCols = new String[]{
                  "id",
                  "name",
                  "mobs",
                  "maxzone",
                  "maxplayer",
                  "npcs",
                  "boat",
                  "typeViewPlayer",
                  "b",
                  "specMap",
                  "vgos",
                  "IDBack",
                  "HBack",
                  "maxW",
                  "maxH",
                  "w",
                  "h",
                  "tile_id",
                  "id_eff_map",
                  "level",
                  "typeChangeMap",
                  "mPosMapTrain",
                  "strTimeChange"
               };

               for (int i = 0; i < Math.min(newCols.length, values.size()); i++) {
                  colNames.add(newCols[i]);
               }
            } else {
               String[] legacyCols = new String[]{
                  "id",
                  "name",
                  "mobs",
                  "maxzone",
                  "maxplayer",
                  "npcs",
                  "boat",
                  "typeViewPlayer",
                  "b",
                  "specMap",
                  "vgos",
                  "data",
                  "MapBack",
                  "id_eff_map",
                  "level",
                  "typeChangeMap",
                  "mPosMapTrain",
                  "strTimeChange"
               };

               for (int i = 0; i < Math.min(legacyCols.length, values.size()); i++) {
                  colNames.add(legacyCols[i]);
               }
            }
         }

         for (int i = 0; i < Math.min(colNames.size(), values.size()); i++) {
            String colName = colNames.get(i).toLowerCase();
            String val = values.get(i);
            if (!val.equalsIgnoreCase("null")) {
               if (val.startsWith("'") && val.endsWith("'") && val.length() >= 2) {
                  val = val.substring(1, val.length() - 1).replace("''", "'").replace("\\'", "'");
               } else if (val.startsWith("'") && val.length() > 1) {
                  val = val.substring(1);
                  if (val.endsWith("'")) {
                     val = val.substring(0, val.length() - 1);
                  }

                  val = val.replace("''", "'").replace("\\'", "'");
               }

               switch (colName) {
                  case "id":
                     map.id = parseInt(val, 0);
                     break;
                  case "name":
                     map.name = val;
                     break;
                  case "maxzone":
                     map.max_zone = (byte)parseInt(val, 10);
                     break;
                  case "maxplayer":
                     map.max_player = (byte)parseInt(val, 30);
                     break;
                  case "typeviewplayer":
                     map.type_view_p = (byte)parseInt(val, 0);
                     break;
                  case "b":
                     map.isOnlinemap = (byte)parseInt(val, 1);
                     break;
                  case "specmap":
                     map.specMap = (byte)parseInt(val, 0);
                     break;
                  case "id_eff_map":
                     map.id_eff_map = (byte)parseInt(val, -1);
                     break;
                  case "level":
                     map.level = (byte)parseInt(val, 1);
                     break;
                  case "typechangemap":
                     map.typeChangeMap = (byte)parseInt(val, 0);
                     break;
                  case "strtimechange":
                     map.strTimeChange = val;
                     break;
                  case "w":
                     map.width = parseInt(val, 45);
                     break;
                  case "h":
                     map.height = parseInt(val, 17);
                     break;
                  case "tile_id":
                     map.setTileSetId((byte)parseInt(val, 0));
                     break;
                  case "idback":
                     map.IDBack = (byte)parseInt(val, 0);
                     break;
                  case "hback":
                     map.HBack = (short)parseInt(val, 250);
                     break;
                  case "maxw":
                     map.maxW = (short)parseInt(val, 0);
                     break;
                  case "maxh":
                     map.maxH = (short)parseInt(val, 0);
                     break;
                  case "mapback":
                     if (!val.isEmpty()) {
                        JSONArray mapBack = parseJsonString(val);
                        if (mapBack != null && mapBack.length() >= 4) {
                           map.IDBack = (byte)mapBack.getInt(0);
                           map.HBack = (short)mapBack.getInt(1);
                           if (map.maxW <= 0) {
                              map.maxW = (short)mapBack.getInt(2);
                           }

                           if (map.maxH <= 0) {
                              map.maxH = (short)mapBack.getInt(3);
                           }
                        }
                     }
                     break;
                  case "data":
                     if (!val.isEmpty()) {
                        JSONArray dataArr = parseJsonString(val);
                        if (dataArr != null && dataArr.length() >= 2) {
                           MapDataExporter.parseData0(map, jsonArrayToBytes(dataArr.getJSONArray(0)));
                           MapDataExporter.parseData1(map, jsonArrayToBytes(dataArr.getJSONArray(1)));
                        }
                     }
                     break;
                  case "npcs":
                     map.npcs = new ArrayList<>();
                     if (val.isEmpty()) {
                        break;
                     }

                     JSONArray npcsArr = parseJsonString(val);
                     if (npcsArr != null) {
                        for (int jxxx = 0; jxxx < npcsArr.length(); jxxx++) {
                           JSONArray n = npcsArr.getJSONArray(jxxx);
                           if (n.length() >= 15) {
                              GameMap.Npc npc = new GameMap.Npc();
                              npc.iditem = (short)n.getInt(0);
                              npc.name = n.optString(1, "");
                              npc.namegt = n.optString(2, "");
                              npc.chat = n.optString(3, "");
                              npc.x = (short)n.getInt(4);
                              npc.y = (short)n.getInt(5);
                              npc.isPerson = (byte)n.getInt(6);
                              npc.typeIcon = (byte)n.getInt(7);
                              npc.wBlock = (byte)n.getInt(8);
                              npc.hBlock = (byte)n.getInt(9);
                              npc.b3 = (byte)n.getInt(10);
                              npc.dataFrame = jsonArrayToBytes(n.optJSONArray(11));
                              npc.head = (short)n.getInt(12);
                              npc.hair = (short)n.getInt(13);
                              npc.wearing = jsonArrayToShorts(n.optJSONArray(14));
                              map.npcs.add(npc);
                           }
                        }
                     }
                     break;
                  case "boat":
                     map.list_boat = new ArrayList<>();
                     if (val.isEmpty()) {
                        break;
                     }

                     JSONArray boatArr = parseJsonString(val);
                     if (boatArr != null) {
                        for (int jxx = 0; jxx < boatArr.length(); jxx++) {
                           JSONArray b = boatArr.getJSONArray(jxx);
                           if (b.length() >= 2) {
                              GameMap.Boat_In_Map boat = new GameMap.Boat_In_Map();
                              boat.x = (short)b.getInt(0);
                              boat.y = (short)b.getInt(1);
                              map.list_boat.add(boat);
                           }
                        }
                     }
                     break;
                  case "vgos":
                     map.vgos = new ArrayList<>();
                     if (val.isEmpty()) {
                        break;
                     }

                     JSONArray vgosArr = parseJsonString(val);
                     if (vgosArr != null) {
                        for (int jx = 0; jx < vgosArr.length(); jx++) {
                           JSONArray v = vgosArr.getJSONArray(jx);
                           if (v.length() >= 5) {
                              GameMap.Vgo vgo = new GameMap.Vgo();
                              vgo.id_map_go = (short)v.getInt(0);
                              vgo.xold = (short)v.getInt(1);
                              vgo.yold = (short)v.getInt(2);
                              vgo.xnew = (short)v.getInt(3);
                              vgo.ynew = (short)v.getInt(4);
                              if (vgo.id_map_go != -1) {
                                 map.vgos.add(vgo);
                              }
                           }
                        }
                     }
                     break;
                  case "mposmaptrain":
                     if (val.isEmpty()) {
                        break;
                     }

                     JSONArray trainArr = parseJsonString(val);
                     if (trainArr == null) {
                        break;
                     }

                     map.mPosMapTrain = new byte[trainArr.length()][];

                     for (int jx = 0; jx < trainArr.length(); jx++) {
                        map.mPosMapTrain[jx] = jsonArrayToBytes(trainArr.optJSONArray(jx));
                     }
                     break;
                  case "mobs":
                     map.list_mob = new ArrayList<>();
                     if (!val.isEmpty() && !val.equals("[]")) {
                        JSONArray mobsArr = parseJsonString(val);
                        if (mobsArr != null) {
                           for (int j = 0; j < mobsArr.length(); j++) {
                              JSONArray m = mobsArr.getJSONArray(j);
                              if (m.length() >= 3) {
                                 GameMap.Mob mob = new GameMap.Mob();
                                 mob.templateId = m.getInt(0);
                                 mob.x = (short)m.getInt(1);
                                 mob.y = (short)m.getInt(2);
                                 map.list_mob.add(mob);
                              }
                           }
                        }
                     }
               }
            }
         }

         if ((map.mapPaint == null || map.mapPaint.length == 0) && !loadMapFromBinaryFiles(map, "Data/Map/ServerData/binary/")) {
            loadMapFromBinaryFiles(map, "Data/Map/binary/");
         }

         map.ensureBuffers();
         return map;
      }
   }

   private static List<String> splitTupleValues(String tupleStr) {
      List<String> values = new ArrayList<>();
      boolean inString = false;
      boolean escape = false;
      StringBuilder current = new StringBuilder();

      for (int i = 0; i < tupleStr.length(); i++) {
         char c = tupleStr.charAt(i);
         if (escape) {
            escape = false;
            current.append(c);
         } else if (c == '\\') {
            escape = true;
         } else if (c == '\'') {
            if (inString && i + 1 < tupleStr.length() && tupleStr.charAt(i + 1) == '\'') {
               current.append('\'');
               i++;
            } else {
               inString = !inString;
            }
         } else if (!inString && c == ',') {
            values.add(current.toString().trim());
            current.setLength(0);
         } else {
            current.append(c);
         }
      }

      values.add(current.toString().trim());
      return values;
   }

   private static int parseInt(String val, int def) {
      try {
         return Integer.parseInt(val.trim());
      } catch (Exception var3) {
         return def;
      }
   }

   private static JSONArray parseJsonString(String str) {
      if (str != null && !str.isEmpty() && !str.equals("null")) {
         try {
            return new JSONArray(str);
         } catch (Exception var2) {
            return null;
         }
      } else {
         return null;
      }
   }

   private static byte[] jsonArrayToBytes(JSONArray arr) {
      if (arr == null) {
         return new byte[0];
      } else {
         byte[] result = new byte[arr.length()];

         for (int i = 0; i < arr.length(); i++) {
            result[i] = (byte)arr.getInt(i);
         }

         return result;
      }
   }

   private static short[] jsonArrayToShorts(JSONArray arr) {
      if (arr == null) {
         return new short[0];
      } else {
         short[] result = new short[arr.length()];

         for (int i = 0; i < arr.length(); i++) {
            result[i] = (short)arr.getInt(i);
         }

         return result;
      }
   }
}
