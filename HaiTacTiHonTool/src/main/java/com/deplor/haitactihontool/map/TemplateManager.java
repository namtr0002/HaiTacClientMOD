package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.AppConfig;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TemplateManager {
   private static TemplateManager instance;
   private Map<Short, ItemMapTemplate> itemTemplates = new HashMap<>();
   private String serverDataPath = this.detectServerDataPath();

   private String detectServerDataPath() {
      String[] possiblePaths = new String[]{
         AppConfig.getResolvedPath("path_template", "Data/Map/ServerData/template/98/"),
         AppConfig.getPath("Data/Map/ServerData/template/98/"),
         AppConfig.getPath("Data/Map/template/98/"),
         AppConfig.getPath("Data/template/98/"),
         "Data/Map/ServerData/template/98/",
         "Data/Map/template/98/",
         "Data/template/98/",
         "../HaiTacTiHonServer/data/template/98/",
         "../../HaiTacTiHonServer/data/template/98/",
         "C:/DepLor/HTTH/Team/HaiTacTiHonServer/data/template/98/"
      };

      for (String path : possiblePaths) {
         if (path == null) {
            continue;
         }

         File dir = new File(path);
         if (dir.exists() && dir.isDirectory()) {
            String p = dir.getAbsolutePath().replace('\\', '/');
            return !p.endsWith("/") ? p + "/" : p;
         }
      }

      String fallback = AppConfig.getPath("Data/Map/ServerData/template/98/");
      return !fallback.endsWith("/") && !fallback.endsWith("\\") ? fallback + "/" : fallback;
   }

   private TemplateManager() {
   }

   public static TemplateManager gI() {
      if (instance == null) {
         instance = new TemplateManager();
      }

      return instance;
   }

   public static void reset() {
      instance = null;
   }

   public void setServerDataPath(String path) {
      this.serverDataPath = path;
   }

   public ItemMapTemplate getItemTemplate(short id) {
      if (this.itemTemplates.containsKey(id)) {
         return this.itemTemplates.get(id);
      } else {
         ItemMapTemplate temp = this.loadItemTemplate(id);
         if (temp != null) {
            this.itemTemplates.put(id, temp);
         }

         return temp;
      }
   }

   private ItemMapTemplate loadItemTemplate(short id) {
      if (this.serverDataPath == null || !(new File(this.serverDataPath)).exists()) {
         this.serverDataPath = this.detectServerDataPath();
      }
      File file = new File(this.serverDataPath + id);
      if (!file.exists()) {
         return null;
      } else {
         try {
            ItemMapTemplate var17;
            try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
               short idImage = dis.readShort();
               byte layer = dis.readByte();
               short dx = dis.readShort();
               short dy = dis.readShort();
               int b = 0;

               try {
                  b = dis.readByte() & 255;
               } catch (EOFException var13) {
               }

               int[][] block = new int[b][2];

               for (int i = 0; i < b; i++) {
                  try {
                     block[i][0] = dis.readByte();
                     block[i][1] = dis.readByte();
                  } catch (EOFException var14) {
                     break;
                  }
               }

               var17 = new ItemMapTemplate(id, idImage, layer, dx, dy, block);
            }

            return var17;
         } catch (IOException var16) {
            System.err.println("[TemplateManager] Error loading template 98/" + id + ": " + var16.getMessage());
            return null;
         }
      }
   }

   public Map<Short, ItemMapTemplate> getAllItemTemplates() {
      if (this.serverDataPath == null || !(new File(this.serverDataPath)).exists()) {
         this.serverDataPath = this.detectServerDataPath();
      }
      if (this.serverDataPath == null) {
         return this.itemTemplates;
      } else {
         if (this.itemTemplates.isEmpty()) {
            File dir = new File(this.serverDataPath);
            if (dir.exists() && dir.isDirectory()) {
               File[] files = dir.listFiles();
               if (files != null) {
                  for (File f : files) {
                     try {
                        short id = Short.parseShort(f.getName());
                        this.getItemTemplate(id);
                     } catch (NumberFormatException var8) {
                     }
                  }
               }
            }
         }

         return this.itemTemplates;
      }
   }

   public boolean saveItemTemplate(ItemMapTemplate temp) {
      File file = new File(this.serverDataPath + temp.idItem);
      File parent = file.getParentFile();
      if (parent != null && !parent.exists()) {
         parent.mkdirs();
      }

      try {
         boolean var12;
         try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            dos.writeShort(temp.idImage);
            dos.writeByte(temp.layer);
            dos.writeShort(temp.dx);
            dos.writeShort(temp.dy);
            if (temp.block == null) {
               dos.writeByte(0);
            } else {
               dos.writeByte(temp.block.length);

               for (int[] b : temp.block) {
                  dos.writeByte(b[0]);
                  dos.writeByte(b[1]);
               }
            }

            this.itemTemplates.put(temp.idItem, temp);
            var12 = true;
         }

         return var12;
      } catch (IOException var11) {
         System.err.println("[TemplateManager] Error saving template 98/" + temp.idItem + ": " + var11.getMessage());
         return false;
      }
   }
}
