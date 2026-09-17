package com.deplor.haitactihontool.fashionv2;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class FashionV2AtlasMerger {
   public static FashionV2AtlasMerger.AtlasResult buildAtlas(List<File> imageFiles, String atlasName) throws IOException {
      List<BufferedImage> images = new ArrayList<>();
      List<String> spriteIds = new ArrayList<>();

      for (File f : imageFiles) {
         if (f.exists()) {
            BufferedImage img = ImageIO.read(f);
            if (img != null) {
               images.add(img);
               String id = f.getName().replace(".png", "").replace(".jpg", "");
               spriteIds.add(id);
            }
         }
      }

      if (images.isEmpty()) {
         BufferedImage dummy = new BufferedImage(64, 64, 2);
         Graphics2D g = dummy.createGraphics();
         g.setColor(new Color(255, 100, 150, 180));
         g.fillRect(0, 0, 64, 64);
         g.dispose();
         images.add(dummy);
         spriteIds.add("placeholder");
      }

      int padding = 2;
      int totalArea = 0;
      int maxW = 0;
      int maxH = 0;

      for (BufferedImage img : images) {
         totalArea += (img.getWidth() + padding) * (img.getHeight() + padding);
         maxW = Math.max(maxW, img.getWidth());
         maxH = Math.max(maxH, img.getHeight());
      }

      int atlasWidth = 256;

      while (atlasWidth * atlasWidth < totalArea) {
         atlasWidth *= 2;
      }

      if (atlasWidth < maxW + padding * 2) {
         atlasWidth = Math.max(256, maxW + padding * 2);
      }

      int curX = padding;
      int curY = padding;
      int rowMaxH = 0;
      FashionV2AtlasMerger.AtlasResult result = new FashionV2AtlasMerger.AtlasResult();
      List<FashionV2AtlasMerger.SpriteRect> rects = new ArrayList<>();

      for (int i = 0; i < images.size(); i++) {
         BufferedImage img = images.get(i);
         String spriteId = spriteIds.get(i);
         if (curX + img.getWidth() + padding > atlasWidth) {
            curX = padding;
            curY += rowMaxH + padding;
            rowMaxH = 0;
         }

         FashionV2AtlasMerger.SpriteRect rect = new FashionV2AtlasMerger.SpriteRect(spriteId, curX, curY, img.getWidth(), img.getHeight(), img);
         rects.add(rect);
         result.spriteMap.put(spriteId, rect);
         curX += img.getWidth() + padding;
         rowMaxH = Math.max(rowMaxH, img.getHeight());
      }

      int atlasHeight = curY + rowMaxH + padding;
      BufferedImage atlasImage = new BufferedImage(atlasWidth, atlasHeight, 2);
      Graphics2D g2d = atlasImage.createGraphics();
      g2d.setComposite(AlphaComposite.Clear);
      g2d.fillRect(0, 0, atlasWidth, atlasHeight);
      g2d.setComposite(AlphaComposite.SrcOver);

      for (FashionV2AtlasMerger.SpriteRect rect : rects) {
         g2d.drawImage(rect.image, rect.x, rect.y, null);
      }

      g2d.dispose();
      result.atlasImage = atlasImage;
      return result;
   }

   public static BufferedImage createScaledImage(BufferedImage original, float scaleFactor) {
      int newWidth = Math.max(1, Math.round(original.getWidth() * scaleFactor));
      int newHeight = Math.max(1, Math.round(original.getHeight() * scaleFactor));
      BufferedImage scaled = new BufferedImage(newWidth, newHeight, 2);
      Graphics2D g2d = scaled.createGraphics();
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setComposite(AlphaComposite.Clear);
      g2d.fillRect(0, 0, newWidth, newHeight);
      g2d.setComposite(AlphaComposite.SrcOver);
      g2d.drawImage(original, 0, 0, newWidth, newHeight, null);
      g2d.dispose();
      return scaled;
   }

   public static void saveMultiScaleAtlas(BufferedImage atlasImage4x, File outputDir, String baseIdName) throws IOException {
      saveAtlasPng(atlasImage4x, new File(outputDir, baseIdName + ".png"));
      saveAtlasPng(atlasImage4x, new File(outputDir, "4/" + baseIdName + ".png"));
      saveAtlasPng(atlasImage4x, new File(outputDir, "zoom4/" + baseIdName + ".png"));
      BufferedImage atlasImage3x = createScaledImage(atlasImage4x, 0.75F);
      saveAtlasPng(atlasImage3x, new File(outputDir, baseIdName + "_x3.png"));
      saveAtlasPng(atlasImage3x, new File(outputDir, "3/" + baseIdName + ".png"));
      saveAtlasPng(atlasImage3x, new File(outputDir, "zoom3/" + baseIdName + ".png"));
      BufferedImage atlasImage2x = createScaledImage(atlasImage4x, 0.5F);
      saveAtlasPng(atlasImage2x, new File(outputDir, baseIdName + "_x2.png"));
      saveAtlasPng(atlasImage2x, new File(outputDir, "2/" + baseIdName + ".png"));
      saveAtlasPng(atlasImage2x, new File(outputDir, "zoom2/" + baseIdName + ".png"));
      BufferedImage atlasImage1x = createScaledImage(atlasImage4x, 0.25F);
      saveAtlasPng(atlasImage1x, new File(outputDir, baseIdName + "_x1.png"));
      saveAtlasPng(atlasImage1x, new File(outputDir, "1/" + baseIdName + ".png"));
      saveAtlasPng(atlasImage1x, new File(outputDir, "zoom1/" + baseIdName + ".png"));
      System.out.println("[SUCCESS] Auto Exported Multi-Scale Atlases (x4, x3, x2, x1) for base: " + baseIdName);
   }

   public static void saveAtlasPng(BufferedImage atlasImage, File outputFile) throws IOException {
      if (!outputFile.getParentFile().exists()) {
         outputFile.getParentFile().mkdirs();
      }

      ImageIO.write(atlasImage, "PNG", outputFile);
      System.out.println("[SUCCESS] Saved Texture Atlas PNG: " + outputFile.getAbsolutePath());
   }

   public static class AtlasResult {
      public BufferedImage atlasImage;
      public Map<String, FashionV2AtlasMerger.SpriteRect> spriteMap = new HashMap<>();
   }

   public static class SpriteRect {
      public String spriteId;
      public int x;
      public int y;
      public int width;
      public int height;
      public BufferedImage image;

      public SpriteRect(String spriteId, int x, int y, int width, int height, BufferedImage image) {
         this.spriteId = spriteId;
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
         this.image = image;
      }
   }
}
