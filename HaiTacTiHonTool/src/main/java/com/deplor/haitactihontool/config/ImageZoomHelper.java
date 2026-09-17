package com.deplor.haitactihontool.config;

import com.deplor.haitactihontool.effect.SmallImageDef;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageZoomHelper {
   public static void saveImageWithZoomLevels(BufferedImage original, String baseDir, String fileName) {
      if (original != null) {
         try {
            baseDir = baseDir.replace('\\', '/');
            if (!baseDir.endsWith("/")) {
               baseDir = baseDir + "/";
            }

            new File(baseDir + "x1/").mkdirs();
            new File(baseDir + "x2/").mkdirs();
            new File(baseDir + "x3/").mkdirs();
            new File(baseDir + "x4/").mkdirs();
            ImageIO.write(original, "png", new File(baseDir + "x4/" + fileName));
            saveScaled(original, 0.75, new File(baseDir + "x3/" + fileName));
            saveScaled(original, 0.5, new File(baseDir + "x2/" + fileName));
            saveScaled(original, 0.25, new File(baseDir + "x1/" + fileName));
         } catch (Exception var4) {
            System.err.println("[ImageZoomHelper] Error saving zoom levels: " + var4.getMessage());
            var4.printStackTrace();
         }
      }
   }

   public static void saveImageWithZoomLevels(File srcFile, String baseDir, String fileName) {
      try {
         if (!srcFile.exists()) {
            return;
         }

         BufferedImage original = ImageIO.read(srcFile);
         if (original != null) {
            saveImageWithZoomLevels(original, baseDir, fileName);
         }
      } catch (Exception var4) {
         System.err.println("[ImageZoomHelper] Error reading/saving file: " + var4.getMessage());
      }
   }

   private static void saveScaled(BufferedImage original, double scale, File destFile) throws IOException {
      int w = (int)Math.round(original.getWidth() * scale);
      int h = (int)Math.round(original.getHeight() * scale);
      w = Math.max(1, w);
      h = Math.max(1, h);
      BufferedImage scaled = new BufferedImage(w, h, 2);
      Graphics2D g2 = scaled.createGraphics();
      g2.setComposite(AlphaComposite.Src);
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
      g2.drawImage(original, 0, 0, w, h, null);
      g2.dispose();
      ImageIO.write(scaled, "png", destFile);
   }

   public static BufferedImage scaleToZoom4(BufferedImage original, int currentZoom) {
      if (original != null && currentZoom != 4 && currentZoom > 0) {
         double scale = 4.0 / currentZoom;
         int w = (int)Math.round(original.getWidth() * scale);
         int h = (int)Math.round(original.getHeight() * scale);
         w = Math.max(1, w);
         h = Math.max(1, h);
         BufferedImage scaled = new BufferedImage(w, h, 2);
         Graphics2D g2 = scaled.createGraphics();
         g2.setComposite(AlphaComposite.Src);
         g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
         g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
         g2.drawImage(original, 0, 0, w, h, null);
         g2.dispose();
         return scaled;
      } else {
         return original;
      }
   }

   public static int detectImageZoom(BufferedImage original, SmallImageDef[] smallImages) {
      if (original != null && smallImages != null && smallImages.length != 0) {
         int maxLogicalX = 1;
         int maxLogicalY = 1;

         for (SmallImageDef s : smallImages) {
            maxLogicalX = Math.max(maxLogicalX, s.x + s.w);
            maxLogicalY = Math.max(maxLogicalY, s.y + s.h);
         }

         double ratioX = (double)original.getWidth() / maxLogicalX;
         double ratioY = (double)original.getHeight() / maxLogicalY;
         double ratio = Math.max(ratioX, ratioY);
         int zoom = (int)Math.round(ratio);
         if (zoom < 1) {
            zoom = 1;
         }

         if (zoom > 4) {
            zoom = 4;
         }

         return zoom;
      } else {
         return 4;
      }
   }
}
