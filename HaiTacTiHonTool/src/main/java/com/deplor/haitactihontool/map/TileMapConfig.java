package com.deplor.haitactihontool.map;

public class TileMapConfig {
   public static final int TILES_PER_ROW = 2;
   public static final int W_TILE_LOGIC = 24;
   private static int wTileImage = 24;
   public static final int MAX_TILE_SPRITES = 20;
   public static final byte[] M_LOCK_MAP = new byte[]{
      8, 11, 8, 8, 5, 5, 20, 20, 13, 13, 8, 8, 4, 4, 4, 7, 4, 4, 7, 7, 25, 42, 9, 12, 5, 5, 8, 8, 9, 12, 2, 2, 3, 5, 6, 8, 4, 4, 13, 13
   };
   public static final int T_MAP_NULL = -1;
   public static final int T_MAP_NORMAL = 0;
   public static final int T_MAP_STAND = 1;
   public static final int T_MAP_SLOW = 2;

   public static void setWTileFromImage(int imageWidth, int cols) {
      wTileImage = imageWidth / cols;
      System.out.println("[TileMapConfig] Set wTileImage=" + wTileImage + " from imageWidth=" + imageWidth + " cols=" + cols);
   }

   public static int getWTileLogic() {
      return 24;
   }

   public static int getWTileImage() {
      return wTileImage;
   }

   public static int getRenderTileSize() {
      return 24;
   }

   public static float getImageScale() {
      return wTileImage / 24.0F;
   }

   public static int getfWater(int tileSetId) {
      int idx = tileSetId * 2;
      return idx >= M_LOCK_MAP.length ? 8 : M_LOCK_MAP[idx] & 0xFF;
   }

   public static int getfStand(int tileSetId) {
      int idx = tileSetId * 2 + 1;
      return idx >= M_LOCK_MAP.length ? 11 : M_LOCK_MAP[idx] & 0xFF;
   }

   public static int getTileType(int tileId, int tileSetId) {
      int fWater = getfWater(tileSetId);
      int fStand = getfStand(tileSetId);
      if (tileId <= 0) {
         return 0;
      } else if (tileId >= fStand) {
         return 1;
      } else {
         return tileId >= fWater ? 2 : 0;
      }
   }

   public static boolean isWaterTile(int tileId, int tileSetId) {
      int fWater = getfWater(tileSetId);
      int fStand = getfStand(tileSetId);
      return tileId >= fWater && tileId < fStand;
   }

   public static boolean isBlocked(int tileId, int tileSetId) {
      return getTileType(tileId, tileSetId) == 1;
   }

   public static int getWaterFrameCount(int tileSetId) {
      int fWater = getfWater(tileSetId);
      int fStand = getfStand(tileSetId);
      return fStand - fWater;
   }
}
