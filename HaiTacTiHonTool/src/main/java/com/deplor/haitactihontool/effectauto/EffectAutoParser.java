package com.deplor.haitactihontool.effectauto;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.ImageZoomHelper;
import com.deplor.haitactihontool.effect.EffFrame;
import com.deplor.haitactihontool.effect.EffPartFrame;
import com.deplor.haitactihontool.effect.SmallImageDef;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class EffectAutoParser {
   private String dataDir = "Data/EffectAuto/data/";
   private String imgDir = "Data/EffectAuto/img/";

   public EffectAutoParser() {
      String base = AppConfig.getResolvedPath("path_effectauto", "Data/EffectAuto/");
      base = base.replace('\\', '/');
      if (!base.endsWith("/")) {
         base = base + "/";
      }

      this.dataDir = base + "data/";
      this.imgDir = base + "img/";
   }

   public EffectAutoParser(String dataDir, String imgDir) {
      if (dataDir != null) {
         this.dataDir = !dataDir.endsWith("/") && !dataDir.endsWith("\\") ? dataDir + "/" : dataDir;
      }

      if (imgDir != null) {
         this.imgDir = !imgDir.endsWith("/") && !imgDir.endsWith("\\") ? imgDir + "/" : imgDir;
      }
   }

   public EffectAutoModel parse(int id) throws IOException {
      File dataFile = new File(this.dataDir + id);
      if (!dataFile.exists()) {
         throw new FileNotFoundException("EffectAuto data not found: " + dataFile.getAbsolutePath());
      } else {
         EffectAutoModel model = new EffectAutoModel(id);
         BufferedImage atlas = null;
         int detectedZoom = 4;
         File imgFile = new File(this.imgDir + "x4/" + id + ".png");
         if (imgFile.exists()) {
            detectedZoom = 4;
         } else {
            imgFile = new File(this.imgDir + "x3/" + id + ".png");
            if (imgFile.exists()) {
               detectedZoom = 3;
            } else {
               imgFile = new File(this.imgDir + "x2/" + id + ".png");
               if (imgFile.exists()) {
                  detectedZoom = 2;
               } else {
                  imgFile = new File(this.imgDir + "x1/" + id + ".png");
                  if (imgFile.exists()) {
                     detectedZoom = 1;
                  } else {
                     imgFile = new File(this.imgDir + id + ".png");
                     if (imgFile.exists()) {
                        detectedZoom = -1;
                     }
                  }
               }
            }
         }

         if (imgFile.exists()) {
            try {
               atlas = ImageIO.read(imgFile);
            } catch (Exception var24) {
            }
         }

         DataInputStream dis = new DataInputStream(new FileInputStream(dataFile));

         try {
            dis.skipBytes(4);
            int numImages = dis.readByte() & 255;
            SmallImageDef[] smallImages = new SmallImageDef[numImages];

            for (int i = 0; i < numImages; i++) {
               int sid = dis.readUnsignedByte();
               int sx = dis.readUnsignedByte();
               int sy = dis.readUnsignedByte();
               int sw = dis.readUnsignedByte();
               int sh = dis.readUnsignedByte();
               smallImages[i] = new SmallImageDef(sid, sx, sy, sw, sh);
            }

            model.smallImages = smallImages;
            int numFrames = dis.readShort();
            EffFrame[] frames = new EffFrame[numFrames];

            for (int j = 0; j < numFrames; j++) {
               int numParts = dis.readByte() & 255;
               List<EffPartFrame> all = new ArrayList<>();
               List<EffPartFrame> top = new ArrayList<>();
               List<EffPartFrame> bottom = new ArrayList<>();

               for (int k = 0; k < numParts; k++) {
                  int dx = dis.readShort();
                  int dy = dis.readShort();
                  int idSmall = dis.readByte() & 255;
                  EffPartFrame pf = new EffPartFrame(dx, dy, idSmall, 0, 1);
                  all.add(pf);
                  top.add(pf);
               }

               frames[j] = new EffFrame(bottom, top, all);
            }

            model.frames = frames;
            int seqLen = dis.readShort();
            int[] sequence = new int[seqLen];

            for (int l = 0; l < seqLen; l++) {
               sequence[l] = dis.readByte() & 255;
            }

            model.sequence = sequence;

            try {
               model.typeEffect = dis.readByte() & 255;
               model.valueEffect = dis.readByte() & 255;
            } catch (EOFException var23) {
            }
         } catch (Throwable var25) {
            try {
               dis.close();
            } catch (Throwable var22) {
               var25.addSuppressed(var22);
            }

            throw var25;
         }

         dis.close();
         if (atlas != null) {
            int zoom = detectedZoom;
            if (detectedZoom == -1) {
               zoom = ImageZoomHelper.detectImageZoom(atlas, model.smallImages);
            }

            model.atlasImage = ImageZoomHelper.scaleToZoom4(atlas, zoom);
         }

         return model;
      }
   }

   public List<Integer> scanIds() {
      List<Integer> ids = new ArrayList<>();
      File dir = new File(this.dataDir);
      if (dir.isDirectory()) {
         File[] files = dir.listFiles();
         if (files != null) {
            for (File f : files) {
               try {
                  ids.add(Integer.parseInt(f.getName()));
               } catch (NumberFormatException var9) {
               }
            }
         }
      }

      ids.sort(Integer::compareTo);
      return ids;
   }
}
