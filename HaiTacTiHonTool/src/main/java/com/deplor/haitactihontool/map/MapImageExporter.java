package com.deplor.haitactihontool.map;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;

public class MapImageExporter {
   public static final int TILE_SIZE = 24;

   public static BufferedImage renderMapImage(GameMap map) {
      if (map != null && map.width > 0 && map.height > 0) {
         int widthPx = map.width * 24;
         int heightPx = map.height * 24;
         BufferedImage mapImage = new BufferedImage(widthPx, heightPx, 2);
         Graphics2D g2d = mapImage.createGraphics();
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
         g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
         g2d.setComposite(AlphaComposite.SrcOver);
         int tileSetId = map.getTileSetId();
         BufferedImage imgTile = ImageCache.getTileImage(tileSetId, "");
         BufferedImage imgTileWater = ImageCache.getWaterImage(tileSetId, "");
         int assetZoom = getAssetZoom(imgTile);
         paintItemsByLayer(g2d, map, -1, assetZoom);
         paintTiles(g2d, map, imgTile, assetZoom);
         paintWater(g2d, map, imgTileWater, assetZoom);
         paintItemsByLayer(g2d, map, 0, assetZoom);
         paintItemsByLayer(g2d, map, 1, assetZoom);
         paintItemsByLayer(g2d, map, 2, assetZoom);
         paintLayer3Sorted(g2d, map, assetZoom);
         paintItemsByLayer(g2d, map, 4, assetZoom);
         paintItemsByLayer(g2d, map, 5, assetZoom);
         g2d.dispose();
         return mapImage;
      } else {
         return null;
      }
   }

   public static boolean exportMapToPng(GameMap map, File outputFile) throws IOException {
      BufferedImage img = renderMapImage(map);
      if (img == null) {
         return false;
      } else {
         File parent = outputFile.getParentFile();
         if (parent != null && !parent.exists()) {
            parent.mkdirs();
         }

         return ImageIO.write(img, "PNG", outputFile);
      }
   }

   public static int exportAllMapImages(List<GameMap> maps, File outputDir, LoadingCallback callback) {
      if (maps != null && !maps.isEmpty()) {
         if (!outputDir.exists()) {
            outputDir.mkdirs();
         }

         int total = maps.size();
         int exported = 0;

         for (int i = 0; i < total; i++) {
            GameMap map = maps.get(i);
            if (map != null) {
               File outFile = new File(outputDir, map.id + ".png");

               try {
                  if (exportMapToPng(map, outFile)) {
                     exported++;
                  }
               } catch (Exception var9) {
                  System.err.println("[MapImageExporter] Error exporting map " + map.id + " to PNG: " + var9.getMessage());
               }

               if (callback != null) {
                  callback.onProgress(i + 1, total, "Exporting PNG map " + map.id + " (" + (i + 1) + "/" + total + ")");
               }
            }
         }

         return exported;
      } else {
         return 0;
      }
   }

   private static int getAssetZoom(BufferedImage imgTile) {
      if (imgTile != null) {
         int w = imgTile.getWidth();
         int h = imgTile.getHeight();
         if (w % 96 == 0 && h % 96 == 0) {
            return 4;
         }

         if (w % 72 == 0 && h % 72 == 0) {
            return 3;
         }

         if (w % 48 == 0 && h % 48 == 0) {
            return 2;
         }

         if (w % 24 == 0 && h % 24 == 0) {
            return 1;
         }

         for (int z = 4; z >= 1; z--) {
            if (h % (z * 24) == 0) {
               return z;
            }
         }
      }

      return 4;
   }

   private static void paintTiles(Graphics2D g, GameMap map, BufferedImage imgTile, int assetZoom) {
      if (imgTile != null && map.mapPaint != null) {
         int fWater = TileMapConfig.getfWater(map.getTileSetId());
         int fStand = TileMapConfig.getfStand(map.getTileSetId());
         int physicalTileW = assetZoom * 24;

         for (int y = 0; y < map.height; y++) {
            for (int x = 0; x < map.width; x++) {
               int idx = y * map.width + x;
               if (idx >= map.mapPaint.length) {
                  break;
               }

               int id = map.mapPaint[idx] & 0xFF;
               if (id > 0) {
                  boolean isWater = id >= fWater && id < fStand;
                  if (!isWater) {
                     drawTile(g, imgTile, id - 1, x, y, physicalTileW);
                  }
               }
            }
         }
      }
   }

   private static void paintWater(Graphics2D g, GameMap map, BufferedImage imgTileWater, int assetZoom) {
      if (imgTileWater != null && map.mapPaint != null) {
         int fWater = TileMapConfig.getfWater(map.getTileSetId());
         int fStand = TileMapConfig.getfStand(map.getTileSetId());
         int waterSize = fStand - fWater;
         if (waterSize > 0) {
            int physicalTileW = assetZoom * 24;

            for (int y = 0; y < map.height; y++) {
               for (int x = 0; x < map.width; x++) {
                  int idx = y * map.width + x;
                  if (idx >= map.mapPaint.length) {
                     break;
                  }

                  int id = map.mapPaint[idx] & 0xFF;
                  if (id > 0) {
                     boolean isWater = id >= fWater && id < fStand;
                     if (isWater) {
                        int waterIdx = id - fWater;
                        drawTile(g, imgTileWater, waterIdx, x, y, physicalTileW);
                     }
                  }
               }
            }
         }
      }
   }

   private static void drawTile(Graphics2D g, BufferedImage img, int idx, int tx, int ty, int physicalTileW) {
      if (img != null && idx >= 0 && physicalTileW > 0) {
         int srcX = idx / 10 * physicalTileW;
         int srcY = idx % 10 * physicalTileW;
         if (srcX + physicalTileW <= img.getWidth() && srcY + physicalTileW <= img.getHeight()) {
            g.drawImage(img, tx * 24, ty * 24, (tx + 1) * 24, (ty + 1) * 24, srcX, srcY, srcX + physicalTileW, srcY + physicalTileW, null);
         }
      }
   }

   private static void paintItemsByLayer(Graphics2D g, GameMap map, int layer, int assetZoom) {
      if (map.items != null) {
         for (ItemMapEntity item : map.items) {
            if (item.template != null && item.template.layer == layer) {
               drawItem(g, item, assetZoom);
            }
         }
      }
   }

   private static void paintLayer3Sorted(Graphics2D g, GameMap map, int assetZoom) {
      if (map.items != null) {
         List<ItemMapEntity> layer3Items = new ArrayList<>();

         for (ItemMapEntity item : map.items) {
            if (item.template != null && item.template.layer == 3) {
               layer3Items.add(item);
            }
         }

         layer3Items.sort(Comparator.comparingInt(a -> a.tileY));

         for (ItemMapEntity itemx : layer3Items) {
            drawItem(g, itemx, assetZoom);
         }
      }
   }

   private static void drawItem(Graphics2D g, ItemMapEntity item, int assetZoom) {
      if (item.template != null) {
         BufferedImage img = ImageCache.getServerImage(item.template.idImage);
         if (img != null) {
            int px = item.tileX * 24 + item.template.dx;
            int py = item.tileY * 24 + item.template.dy;
            int dw = (int)Math.round((double)img.getWidth() / assetZoom);
            int dh = (int)Math.round((double)img.getHeight() / assetZoom);
            g.drawImage(img, px, py, dw, dh, null);
         }
      }
   }
}
