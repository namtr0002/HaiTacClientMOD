package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.AppConfig;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class MapDataExporter {
   public static byte[] buildBlockData(GameMap map) throws IOException {
      int mapW = Math.max(0, Math.min(127, map.width));
      int mapH = Math.max(0, Math.min(127, map.height));
      int totalTiles = mapW * mapH;
      ByteArrayOutputStream baos = new ByteArrayOutputStream(3 + totalTiles);
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeByte(mapW);
      dos.writeByte(mapH);
      dos.writeByte(map.getTileSetId());
      if (map.mapPaint != null && map.mapPaint.length >= totalTiles) {
         for (int y = 0; y < mapH; y++) {
            for (int x = 0; x < mapW; x++) {
               dos.writeByte(map.mapPaint[y * map.width + x]);
            }
         }
      } else {
         for (int j = 0; j < totalTiles; j++) {
            dos.writeByte(0);
         }
      }

      dos.flush();
      return baos.toByteArray();
   }

   public static byte[] buildItemData(GameMap map) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      List<ItemMapEntity> items = (List<ItemMapEntity>)(map.items != null ? map.items : new ArrayList<>());
      dos.writeShort(items.size());

      for (ItemMapEntity item : items) {
         dos.writeShort(item.templateId);
         dos.writeShort(item.tileX);
         dos.writeShort(item.tileY);
      }

      dos.writeShort(0);
      dos.flush();
      return baos.toByteArray();
   }

   public static void exportMapBinaryFiles(GameMap map, File outputDir) throws IOException {
      if (map != null) {
         if (!outputDir.exists()) {
            outputDir.mkdirs();
         }

         File dataFile = new File(outputDir, map.id + "_data");
         File itemFile = new File(outputDir, map.id + "_item");
         byte[] blockBytes = buildBlockData(map);
         byte[] itemBytes = buildItemData(map);

         try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            fos.write(blockBytes);
         }

         try (FileOutputStream var13 = new FileOutputStream(itemFile)) {
            var13.write(itemBytes);
         }
      }
   }

   public static int exportAllBinary(List<GameMap> maps, File outputDir, LoadingCallback callback) {
      if (maps != null && !maps.isEmpty()) {
         if (!outputDir.exists()) {
            outputDir.mkdirs();
         }

         int total = maps.size();
         int exported = 0;

         for (int i = 0; i < total; i++) {
            GameMap map = maps.get(i);
            if (map != null) {
               try {
                  exportMapBinaryFiles(map, outputDir);
                  exported++;
               } catch (Exception var8) {
                  System.err.println("[MapDataExporter] Error exporting binary for map " + map.id + ": " + var8.getMessage());
               }

               if (callback != null) {
                  callback.onProgress(i + 1, total, "Exporting binary data map " + map.id + " (" + (i + 1) + "/" + total + ")");
               }
            }
         }

         return exported;
      } else {
         return 0;
      }
   }

   public static void parseBlockData(GameMap map, byte[] blockBytes) {
      if (map != null && blockBytes != null && blockBytes.length != 0) {
         int offset = 0;
         if (blockBytes.length >= 3) {
            int headerW = blockBytes[0] & 255;
            int headerH = blockBytes[1] & 255;
            int headerTileId = blockBytes[2] & 255;
            if (blockBytes.length == headerW * headerH + 3
               || map.width > 0 && map.height > 0 && blockBytes.length == map.width * map.height + 3
               || headerW == map.width && headerH == map.height) {
               offset = 3;
               if (map.width <= 0) {
                  map.width = headerW;
               }

               if (map.height <= 0) {
                  map.height = headerH;
               }

               if (map.getTileSetId() == 0 && headerTileId > 0) {
                  map.setTileSetId(headerTileId);
               }
            }
         }

         int total = map.width * map.height;
         if (total > 0) {
            map.mapPaint = new int[total];
            map.mapType = new int[total];

            for (int j = 0; j < total && offset + j < blockBytes.length; j++) {
               int rawByte = blockBytes[offset + j] & 255;
               map.mapPaint[j] = rawByte;
               map.mapType[j] = TileMapConfig.getTileType(rawByte, map.getTileSetId());
            }
         }
      }
   }

   public static void parseItemData(GameMap map, byte[] itemBytes) {
      if (map != null && itemBytes != null && itemBytes.length >= 2) {
         map.items.clear();

         try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(itemBytes))) {
            int numItems = dis.readShort() & '\uffff';

            for (int j = 0; j < numItems && dis.available() >= 6; j++) {
               short id = dis.readShort();
               short tileX = dis.readShort();
               short tileY = dis.readShort();
               ItemMapEntity entity = new ItemMapEntity(id, tileX, tileY);
               entity.template = TemplateManager.gI().getItemTemplate(id);
               map.items.add(entity);
            }
         } catch (IOException var11) {
            System.err.println("[MapDataExporter] parseItemData error: " + var11.getMessage());
         }
      }
   }

   public static byte[] buildData0(GameMap map) throws IOException {
      int mapW = Math.max(0, Math.min(127, map.width));
      int mapH = Math.max(0, Math.min(127, map.height));
      int totalTiles = mapW * mapH;
      ByteArrayOutputStream baos = new ByteArrayOutputStream(3 + totalTiles);
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeByte(mapW);
      dos.writeByte(mapH);
      dos.writeByte(map.getTileSetId());
      if (map.mapPaint != null && map.mapPaint.length >= map.width * map.height) {
         for (int y = 0; y < mapH; y++) {
            for (int x = 0; x < mapW; x++) {
               dos.writeByte(map.mapPaint[y * map.width + x]);
            }
         }
      } else {
         for (int j = 0; j < totalTiles; j++) {
            dos.writeByte(0);
         }
      }

      dos.flush();
      return baos.toByteArray();
   }

   public static byte[] buildData1(GameMap map) throws IOException {
      return buildItemData(map);
   }

   public static void parseData0(GameMap map, byte[] d0) {
      if (d0 != null && d0.length >= 3) {
         try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(d0));
            int mapW = dis.readByte() & 255;
            int mapH = dis.readByte() & 255;
            int idTile = dis.readByte() & 255;
            if (mapW == 0 || mapH == 0) {
               return;
            }

            map.width = mapW;
            map.height = mapH;
            map.setTileSetId((byte)idTile);
            int total = mapW * mapH;
            byte[] blockBytes = new byte[total];
            int read = dis.read(blockBytes);
            if (read > 0) {
               parseBlockData(map, blockBytes);
            }
         } catch (IOException var9) {
            System.err.println("[MapDataExporter] parseData0 error: " + var9.getMessage());
         }
      }
   }

   public static void parseData1(GameMap map, byte[] d1) {
      parseItemData(map, d1);
   }

   public static JSONObject exportToSqlJson(GameMap map) throws IOException {
      JSONObject row = new JSONObject();
      row.put("id", map.id);
      row.put("name", map.name != null ? map.name : "");
      row.put("maxzone", map.max_zone);
      row.put("maxplayer", map.max_player);
      row.put("typeViewPlayer", map.type_view_p);
      row.put("b", map.isOnlinemap);
      row.put("specMap", map.specMap);
      row.put("id_eff_map", map.id_eff_map);
      row.put("level", map.level);
      row.put("typeChangeMap", map.typeChangeMap);
      row.put("strTimeChange", map.strTimeChange != null ? map.strTimeChange : "");
      row.put("w", map.width);
      row.put("h", map.height);
      row.put("tile_id", map.getTileSetId());
      row.put("IDBack", map.IDBack);
      row.put("HBack", map.HBack);
      short maxW = map.maxW > 0 ? map.maxW : (short)(map.width * 24);
      short maxH = map.maxH > 0 ? map.maxH : (short)(map.height * 24);
      row.put("maxW", maxW);
      row.put("maxH", maxH);
      JSONArray mapBack = new JSONArray();
      mapBack.put(map.IDBack);
      mapBack.put(map.HBack);
      mapBack.put(maxW);
      mapBack.put(maxH);
      row.put("MapBack", mapBack);
      JSONArray npcsArr = new JSONArray();
      if (map.npcs != null) {
         for (GameMap.Npc npc : map.npcs) {
            JSONArray n = new JSONArray();
            n.put(npc.iditem);
            n.put(npc.name != null ? npc.name : "");
            n.put(npc.namegt != null ? npc.namegt : "");
            n.put(npc.chat != null ? npc.chat : "");
            n.put(npc.x);
            n.put(npc.y);
            n.put(npc.isPerson);
            n.put(npc.typeIcon);
            n.put(npc.wBlock);
            n.put(npc.hBlock);
            n.put(npc.b3);
            n.put(bytesToJsonArray(npc.dataFrame != null ? npc.dataFrame : new byte[0]));
            n.put(npc.head);
            n.put(npc.hair);
            n.put(shortsToJsonArray(npc.wearing != null ? npc.wearing : new short[0]));
            npcsArr.put(n);
         }
      }

      row.put("npcs", npcsArr);
      JSONArray vgosArr = new JSONArray();
      if (map.vgos != null) {
         for (GameMap.Vgo vgo : map.vgos) {
            JSONArray v = new JSONArray();
            v.put(vgo.id_map_go);
            v.put(vgo.xold);
            v.put(vgo.yold);
            v.put(vgo.xnew);
            v.put(vgo.ynew);
            vgosArr.put(v);
         }
      }

      row.put("vgos", vgosArr);
      JSONArray boatArr = new JSONArray();
      if (map.list_boat != null) {
         for (GameMap.Boat_In_Map b : map.list_boat) {
            JSONArray bArr = new JSONArray();
            bArr.put(b.x);
            bArr.put(b.y);
            boatArr.put(bArr);
         }
      }

      row.put("boat", boatArr);
      JSONArray mobsArr = new JSONArray();
      if (map.list_mob != null) {
         for (GameMap.Mob mob : map.list_mob) {
            JSONArray m = new JSONArray();
            m.put(mob.templateId);
            m.put(mob.x);
            m.put(mob.y);
            mobsArr.put(m);
         }
      }

      row.put("mobs", mobsArr);
      JSONArray trainArr = new JSONArray();
      if (map.mPosMapTrain != null) {
         for (byte[] row2 : map.mPosMapTrain) {
            trainArr.put(bytesToJsonArray(row2));
         }
      }

      row.put("mPosMapTrain", trainArr);
      return row;
   }

   public static GameMap importFromSqlJson(JSONObject row) throws IOException {
      GameMap map = new GameMap();
      map.id = row.getInt("id");
      map.name = row.optString("name", "");
      map.max_zone = (byte)row.optInt("maxzone", 10);
      map.max_player = (byte)row.optInt("maxplayer", 30);
      map.type_view_p = (byte)row.optInt("typeViewPlayer", 0);
      map.isOnlinemap = (byte)row.optInt("b", 1);
      map.specMap = (byte)row.optInt("specMap", 0);
      map.id_eff_map = (byte)row.optInt("id_eff_map", -1);
      map.level = (byte)row.optInt("level", 1);
      map.typeChangeMap = (byte)row.optInt("typeChangeMap", 0);
      map.strTimeChange = row.optString("strTimeChange", "");
      if (row.has("w")) {
         map.width = row.getInt("w");
      }

      if (row.has("h")) {
         map.height = row.getInt("h");
      }

      if (row.has("tile_id")) {
         map.setTileSetId((byte)row.getInt("tile_id"));
      }

      if (row.has("IDBack")) {
         map.IDBack = (byte)row.getInt("IDBack");
      }

      if (row.has("HBack")) {
         map.HBack = (short)row.getInt("HBack");
      }

      if (row.has("maxW")) {
         map.maxW = (short)row.getInt("maxW");
      }

      if (row.has("maxH")) {
         map.maxH = (short)row.getInt("maxH");
      }

      if (row.has("MapBack")) {
         JSONArray mb = toJsonArray(row.get("MapBack"));
         if (mb != null && mb.length() >= 4) {
            map.IDBack = (byte)mb.getInt(0);
            map.HBack = (short)mb.getInt(1);
            if (map.maxW <= 0) {
               map.maxW = (short)mb.getInt(2);
            }

            if (map.maxH <= 0) {
               map.maxH = (short)mb.getInt(3);
            }
         }
      }

      if (row.has("data")) {
         JSONArray dataOuter = toJsonArray(row.get("data"));
         if (dataOuter != null) {
            if (dataOuter.length() >= 1) {
               byte[] d0 = jsonArrayToBytes(toJsonArray(dataOuter.get(0)));
               parseData0(map, d0);
            }

            if (dataOuter.length() >= 2) {
               byte[] d1 = jsonArrayToBytes(toJsonArray(dataOuter.get(1)));
               parseData1(map, d1);
            }
         }
      }

      map.npcs = new ArrayList<>();
      if (row.has("npcs")) {
         JSONArray npcsArr = toJsonArray(row.get("npcs"));
         if (npcsArr != null) {
            for (int i = 0; i < npcsArr.length(); i++) {
               try {
                  JSONArray n = toJsonArray(npcsArr.get(i));
                  if (n != null && n.length() >= 15) {
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
                     JSONArray dfArr = toJsonArray(n.get(11));
                     if (dfArr != null) {
                        npc.dataFrame = jsonArrayToBytes(dfArr);
                     }

                     npc.head = (short)n.getInt(12);
                     npc.hair = (short)n.getInt(13);
                     JSONArray wArr = toJsonArray(n.get(14));
                     if (wArr != null) {
                        npc.wearing = jsonArrayToShorts(wArr);
                     }

                     map.npcs.add(npc);
                  }
               } catch (Exception var11) {
                  System.err.println("[MapDataExporter] Skipping NPC " + i + ": " + var11.getMessage());
               }
            }
         }
      }

      map.vgos = new ArrayList<>();
      if (row.has("vgos")) {
         JSONArray vgosArr = toJsonArray(row.get("vgos"));
         if (vgosArr != null) {
            for (int i = 0; i < vgosArr.length(); i++) {
               try {
                  JSONArray v = toJsonArray(vgosArr.get(i));
                  if (v != null && v.length() >= 5) {
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
               } catch (Exception var10) {
                  System.err.println("[MapDataExporter] Skipping VGO " + i + ": " + var10.getMessage());
               }
            }
         }
      }

      map.list_boat = new ArrayList<>();
      if (row.has("boat")) {
         JSONArray boatArr = toJsonArray(row.get("boat"));
         if (boatArr != null) {
            for (int i = 0; i < boatArr.length(); i++) {
               try {
                  JSONArray b = toJsonArray(boatArr.get(i));
                  if (b != null && b.length() >= 2) {
                     GameMap.Boat_In_Map boat = new GameMap.Boat_In_Map();
                     boat.x = (short)b.getInt(0);
                     boat.y = (short)b.getInt(1);
                     map.list_boat.add(boat);
                  }
               } catch (Exception var9) {
                  System.err.println("[MapDataExporter] Skipping boat " + i + ": " + var9.getMessage());
               }
            }
         }
      }

      map.list_mob = new ArrayList<>();
      if (row.has("mobs")) {
         JSONArray mobsArr = toJsonArray(row.get("mobs"));
         if (mobsArr != null) {
            for (int i = 0; i < mobsArr.length(); i++) {
               try {
                  JSONArray m = toJsonArray(mobsArr.get(i));
                  if (m != null && m.length() >= 3) {
                     GameMap.Mob mob = new GameMap.Mob();
                     mob.templateId = m.getInt(0);
                     mob.x = (short)m.getInt(1);
                     mob.y = (short)m.getInt(2);
                     map.list_mob.add(mob);
                  }
               } catch (Exception var8) {
                  System.err.println("[MapDataExporter] Skipping mob " + i + ": " + var8.getMessage());
               }
            }
         }
      }

      if (row.has("mPosMapTrain")) {
         JSONArray trainArr = toJsonArray(row.get("mPosMapTrain"));
         if (trainArr != null) {
            map.mPosMapTrain = new byte[trainArr.length()][];

            for (int i = 0; i < trainArr.length(); i++) {
               JSONArray inner = toJsonArray(trainArr.get(i));
               map.mPosMapTrain[i] = inner != null ? jsonArrayToBytes(inner) : new byte[0];
            }
         }
      }

      if (map.mapPaint == null || map.mapPaint.length == 0) {
         String bin1 = AppConfig.getPath("Data/Map/ServerData/binary/");
         String bin2 = AppConfig.getPath("Data/Map/binary/");
         if (!SQLMapLoader.loadMapFromBinaryFiles(map, bin1)) {
            SQLMapLoader.loadMapFromBinaryFiles(map, bin2);
         }
      }

      map.ensureBuffers();
      return map;
   }

   public static void saveAllToJsonFile(List<GameMap> maps, String filePath) throws IOException {
      JSONArray arr = new JSONArray();

      for (GameMap map : maps) {
         arr.put(exportToSqlJson(map));
      }

      try (FileWriter fw = new FileWriter(filePath)) {
         fw.write(arr.toString(2));
      }
   }

   public static List<GameMap> loadAllFromJsonFile(String filePath) throws IOException {
      System.out.println("[MapDataExporter] Loading maps from: " + filePath);
      List<GameMap> maps = new ArrayList<>();
      StringBuilder sb = new StringBuilder();
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));

      String line;
      try {
         while ((line = br.readLine()) != null) {
            sb.append(line);
         }
      } catch (Throwable var13) {
         try {
            br.close();
         } catch (Throwable var11) {
            var13.addSuppressed(var11);
         }

         throw var13;
      }

      br.close();
      String text = sb.toString().trim();
      if (text.startsWith("\ufeff")) {
         text = text.substring(1);
      }

      String var15 = text.trim();
      JSONArray arr;
      if (var15.startsWith("[")) {
         arr = new JSONArray(var15);
      } else {
         if (!var15.startsWith("{")) {
            throw new IOException("Invalid JSON format: must start with '[' or '{'");
         }

         JSONObject wrapper = new JSONObject(var15);
         if (wrapper.has("RECORDS")) {
            arr = wrapper.getJSONArray("RECORDS");
         } else {
            String firstKey = (String)wrapper.keys().next();
            arr = wrapper.getJSONArray(firstKey);
         }
      }

      System.out.println("[MapDataExporter] Found " + arr.length() + " map records.");
      File jsonFile = new File(filePath);
      String binDirRelative = jsonFile.getParentFile() != null
         ? new File(jsonFile.getParentFile(), "binary").getPath()
         : AppConfig.getPath("Data/Map/ServerData/binary/");
      String bin1 = AppConfig.getPath("Data/Map/ServerData/binary/");
      String bin2 = AppConfig.getPath("Data/Map/binary/");

      for (int i = 0; i < arr.length(); i++) {
         try {
            GameMap m = importFromSqlJson(arr.getJSONObject(i));
            if ((m.mapPaint == null || m.mapPaint.length == 0)
               && !SQLMapLoader.loadMapFromBinaryFiles(m, binDirRelative)
               && !SQLMapLoader.loadMapFromBinaryFiles(m, bin1)) {
               SQLMapLoader.loadMapFromBinaryFiles(m, bin2);
            }

            m.ensureBuffers();
            maps.add(m);
         } catch (Exception var12) {
            System.err.println("[MapDataExporter] Skipping map at index " + i + ": " + var12.getMessage());
         }
      }

      return maps;
   }

   private static JSONArray toJsonArray(Object value) {
      if (value == null || value == JSONObject.NULL) {
         return new JSONArray();
      } else if (value instanceof JSONArray) {
         return (JSONArray)value;
      } else if (value instanceof String) {
         String s = ((String)value).trim();
         if (!s.isEmpty() && !s.equals("null")) {
            try {
               return new JSONArray(s);
            } catch (Exception var3) {
               return new JSONArray();
            }
         } else {
            return new JSONArray();
         }
      } else {
         return new JSONArray();
      }
   }

   private static JSONArray bytesToJsonArray(byte[] bytes) {
      JSONArray a = new JSONArray();
      if (bytes != null) {
         for (byte b : bytes) {
            a.put(b);
         }
      }

      return a;
   }

   private static JSONArray shortsToJsonArray(short[] shorts) {
      JSONArray a = new JSONArray();
      if (shorts != null) {
         for (short s : shorts) {
            a.put(s);
         }
      }

      return a;
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
