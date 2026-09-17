package com.deplor.haitactihontool.map;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
   public int id;
   public String name;
   public byte max_zone;
   public byte max_player;
   public byte type_view_p;
   public byte isOnlinemap = 1;
   public byte specMap;
   public byte id_eff_map;
   public byte level;
   public byte typeChangeMap;
   public String strTimeChange;
   public byte IDBack;
   public int HBack;
   public short maxW;
   public short maxH;
   public List<GameMap.Vgo> vgos = new ArrayList<>();
   public List<GameMap.Npc> npcs = new ArrayList<>();
   public List<GameMap.Boat_In_Map> list_boat = new ArrayList<>();
   public List<GameMap.Mob> list_mob = new ArrayList<>();
   public byte[][] data = new byte[2][];
   public byte[][] mPosMapTrain;
   public int width;
   public int height;
   public int[] mapPaint;
   public int[] mapType;
   public List<ItemMapEntity> items = new ArrayList<>();
   public byte tileSetId = 0;

   public GameMap() {
   }

   public GameMap(int id, String name, int w, int h, int tileSetId) {
      this.id = id;
      this.name = name;
      this.width = w;
      this.height = h;
      this.setTileSetId(tileSetId);
      int total = Math.max(0, w * h);
      this.mapPaint = new int[total];
      this.mapType = new int[total];
   }

   public void ensureBuffers() {
      int total = Math.max(0, this.width * this.height);
      if (this.mapPaint == null || this.mapPaint.length != total) {
         int[] oldPaint = this.mapPaint;
         this.mapPaint = new int[total];
         if (oldPaint != null) {
            System.arraycopy(oldPaint, 0, this.mapPaint, 0, Math.min(oldPaint.length, total));
         }
      }

      if (this.mapType == null || this.mapType.length != total) {
         int[] oldType = this.mapType;
         this.mapType = new int[total];
         if (oldType != null) {
            System.arraycopy(oldType, 0, this.mapType, 0, Math.min(oldType.length, total));
         } else {
            for (int i = 0; i < total; i++) {
               this.mapType[i] = TileMapConfig.getTileType(this.mapPaint[i], this.getTileSetId());
            }
         }
      }
   }

   public void resize(int newW, int newH) {
      if (newW != this.width || newH != this.height) {
         this.ensureBuffers();
         int[] newPaint = new int[newW * newH];
         int[] newType = new int[newW * newH];

         for (int y = 0; y < Math.min(this.height, newH); y++) {
            for (int x = 0; x < Math.min(this.width, newW); x++) {
               newPaint[y * newW + x] = this.mapPaint[y * this.width + x];
               newType[y * newW + x] = this.mapType[y * this.width + x];
            }
         }

         this.width = newW;
         this.height = newH;
         this.mapPaint = newPaint;
         this.mapType = newType;
      }
   }

   public int getTileSetId() {
      return this.tileSetId & 0xFF;
   }

   public void setTileSetId(int id) {
      this.tileSetId = (byte)id;
   }

   public void setTileId(int x, int y, int tileId) {
      if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
         this.ensureBuffers();
         int idx = y * this.width + x;
         this.mapPaint[idx] = tileId;
         this.mapType[idx] = TileMapConfig.getTileType(tileId, this.getTileSetId());
      }
   }

   public int getTileId(int x, int y) {
      if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
         this.ensureBuffers();
         return this.mapPaint[y * this.width + x];
      } else {
         return 0;
      }
   }

   public int getCollision(int x, int y) {
      if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
         this.ensureBuffers();
         return this.mapType[y * this.width + x];
      } else {
         return 1;
      }
   }

   public void addItem(short templateId, int tileX, int tileY) {
      this.addItem(templateId, tileX, tileY, -1);
   }

   public void addItem(short templateId, int tileX, int tileY, int overrideLayer) {
      this.ensureBuffers();
      this.removeItem(tileX, tileY);
      ItemMapEntity entity = new ItemMapEntity(templateId, tileX, tileY);
      entity.template = TemplateManager.gI().getItemTemplate(templateId);
      if (overrideLayer != -1 && entity.template != null) {
         entity.template.layer = (byte)overrideLayer;
      }

      this.items.add(entity);
      if (entity.template != null && entity.template.block != null) {
         for (int[] b : entity.template.block) {
            int nx = tileX + b[0];
            int ny = tileY + b[1];
            if (nx >= 0 && nx < this.width && ny >= 0 && ny < this.height) {
               this.mapType[ny * this.width + nx] = 1;
            }
         }
      }
   }

   public void removeItem(int tileX, int tileY) {
      this.ensureBuffers();
      ItemMapEntity toRemove = this.getItemAt(tileX, tileY);
      if (toRemove != null) {
         if (toRemove.template != null && toRemove.template.block != null) {
            for (int[] b : toRemove.template.block) {
               int nx = tileX + b[0];
               int ny = tileY + b[1];
               if (nx >= 0 && nx < this.width && ny >= 0 && ny < this.height) {
                  int tid = this.mapPaint[ny * this.width + nx];
                  this.mapType[ny * this.width + nx] = TileMapConfig.getTileType(tid, this.getTileSetId());
               }
            }
         }

         this.items.remove(toRemove);
      }
   }

   public ItemMapEntity getItemAt(int tileX, int tileY) {
      for (ItemMapEntity it : this.items) {
         if (it.tileX == tileX && it.tileY == tileY) {
            return it;
         }
      }

      return null;
   }

   public static class Boat_In_Map {
      public short x;
      public short y;
   }

   public static class Mob {
      public int templateId;
      public short x;
      public short y;
   }

   public static class Npc {
      public short iditem;
      public String name;
      public String namegt;
      public String chat;
      public short x;
      public short y;
      public byte isPerson;
      public byte typeIcon;
      public byte wBlock;
      public byte hBlock;
      public byte b3;
      public byte[] dataFrame;
      public short head;
      public short hair;
      public short[] wearing;
   }

   public static class Vgo {
      public short id_map_go;
      public short xold;
      public short yold;
      public short xnew;
      public short ynew;
   }
}
