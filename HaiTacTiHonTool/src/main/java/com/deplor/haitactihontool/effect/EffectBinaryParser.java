package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.ImageZoomHelper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class EffectBinaryParser {
   private String dataDir = "data/effect/data/";
   private String imgDir = "data/effect/img/";

   public EffectBinaryParser() {
   }

   public EffectBinaryParser(String dataDir, String imgDir) {
      if (dataDir != null) {
         this.dataDir = !dataDir.endsWith("/") && !dataDir.endsWith("\\") ? dataDir + "/" : dataDir;
      }

      if (imgDir != null) {
         this.imgDir = !imgDir.endsWith("/") && !imgDir.endsWith("\\") ? imgDir + "/" : imgDir;
      }
   }

   public EffectModel parse(int id) throws IOException {
      File dataFile = new File(this.dataDir + id);
      if (!dataFile.exists()) {
         dataFile = new File(this.dataDir, id + ".eff");
         if (!dataFile.exists()) {
            dataFile = new File(this.dataDir, "effect_" + id + ".eff");
            if (!dataFile.exists()) {
               dataFile = new File(this.dataDir, "autosave/effect_" + id + ".eff");
               if (!dataFile.exists()) {
                  String appData = AppConfig.getAppDirectory().replace('\\', '/') + "/Data/Effect/data/" + id;
                  File appFile = new File(appData);
                  if (appFile.exists()) {
                     dataFile = appFile;
                  }
               }
            }
         }
      }

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
                  } else {
                     String appImg = AppConfig.getAppDirectory().replace('\\', '/') + "/Data/Effect/img/" + id + ".png";
                     File fAppImg = new File(appImg);
                     if (fAppImg.exists()) {
                        imgFile = fAppImg;
                        detectedZoom = -1;
                     }
                  }
               }
            }
         }
      }

      if (imgFile.exists()) {
         try {
            atlas = ImageIO.read(imgFile);
         } catch (Exception var8) {
         }
      }

      if (!dataFile.exists() && atlas == null) {
         throw new FileNotFoundException("Effect data & image not found for ID #" + id + " at: " + this.dataDir);
      } else {
         EffectModel model = new EffectModel(id);
         model.atlasImage = atlas;
         if (dataFile.exists()) {
            byte[] raw = readAllBytes(dataFile);
            this.parseData(model, raw);
         } else {
            model.smallImages = new SmallImageDef[0];
            model.frames = new EffFrame[]{EffFrame.createEmpty()};
            model.sequence = new int[]{0};
         }

         if (model.atlasImage != null) {
            int zoom = detectedZoom;
            if (detectedZoom == -1) {
               zoom = ImageZoomHelper.detectImageZoom(model.atlasImage, model.smallImages);
            }

            model.atlasImage = ImageZoomHelper.scaleToZoom4(model.atlasImage, zoom);
         }

         return model;
      }
   }

   public void parseData(EffectModel model, byte[] raw) throws IOException {
      if (raw != null && raw.length != 0) {
         if ((raw.length < 8 || raw[0] != -119 || raw[1] != 80 || raw[2] != 78 || raw[3] != 71)
            && (raw.length < 12 || raw[4] != -119 || raw[5] != 80 || raw[6] != 78 || raw[7] != 71)) {
            try {
               this.parseModernFormat(model, raw);
            } catch (Exception var7) {
               try {
                  this.parseLegacyFormat(model, raw);
               } catch (Exception var6) {
                  model.smallImages = new SmallImageDef[0];
                  model.frames = new EffFrame[0];
                  model.sequence = new int[0];
                  throw new IOException("Không thể đọc định dạng dữ liệu Effect ID " + model.id + ": " + var7.getMessage(), var7);
               }
            }
         } else {
            if (model.atlasImage == null) {
               try {
                  int offset = raw[0] == -119 ? 0 : 4;
                  model.atlasImage = ImageIO.read(new ByteArrayInputStream(raw, offset, raw.length - offset));
               } catch (Exception var5) {
               }
            }

            model.smallImages = new SmallImageDef[0];
            model.frames = new EffFrame[0];
            model.sequence = new int[0];
         }
      } else {
         model.smallImages = new SmallImageDef[0];
         model.frames = new EffFrame[0];
         model.sequence = new int[0];
      }
   }

   private void parseModernFormat(EffectModel model, byte[] raw) throws IOException {
      DataInputStream dis = new DataInputStream(new ByteArrayInputStream(raw));
      int numImages = dis.readByte() & 255;
      SmallImageDef[] smallImages = new SmallImageDef[numImages];

      for (int i = 0; i < numImages; i++) {
         int imageId = dis.readUnsignedByte();
         if (imageId == 255) {
            int sid = dis.readUnsignedByte();
            int sx = dis.readShort();
            int sy = dis.readShort();
            int sw = dis.readShort();
            int sh = dis.readShort();
            smallImages[i] = new SmallImageDef(sid, sx, sy, sw, sh);
         } else {
            int sx = dis.readUnsignedByte();
            int sy = dis.readUnsignedByte();
            int sw = dis.readUnsignedByte();
            int sh = dis.readUnsignedByte();
            smallImages[i] = new SmallImageDef(imageId, sx, sy, sw, sh);
         }
      }

      model.smallImages = smallImages;
      int numFrames = dis.readShort();
      if (numFrames >= 0 && numFrames <= 10000) {
         EffFrame[] frames = new EffFrame[numFrames];

         for (int j = 0; j < numFrames; j++) {
            int numParts = dis.readByte() & 255;
            List<EffPartFrame> bottom = new ArrayList<>();
            List<EffPartFrame> top = new ArrayList<>();
            List<EffPartFrame> all = new ArrayList<>();

            for (int k = 0; k < numParts; k++) {
               int dx = dis.readShort();
               int dy = dis.readShort();
               int idSmall = dis.readByte() & 255;
               int flipMarker = dis.readUnsignedByte();
               int flip = flipMarker;
               int rotate = 0;
               if (flipMarker == 128) {
                  flip = dis.readByte();
                  rotate = dis.readShort();
               }

               int onTop = dis.readByte() & 255;
               int absDy = Math.abs(dy);
               if (absDy < model.minAbsDy) {
                  model.minAbsDy = absDy;
               }

               if (idSmall >= numImages) {
                  idSmall = 0;
               }

               EffPartFrame pf = new EffPartFrame(dx, dy, idSmall, flip, onTop, rotate);
               all.add(pf);
               if (onTop == 0) {
                  bottom.add(pf);
               } else {
                  top.add(pf);
               }
            }

            frames[j] = new EffFrame(bottom, top, all);
         }

         model.frames = frames;
         int seqLen = dis.readUnsignedByte();
         int[] sequence = new int[seqLen];

         for (int l = 0; l < seqLen; l++) {
            sequence[l] = (byte)dis.readShort();
            if (sequence[l] < 0) {
               sequence[l] += 256;
            }

            if (frames.length > 0 && sequence[l] >= frames.length) {
               sequence[l] = 0;
            }
         }

         model.sequence = sequence;

         try {
            dis.readByte();
            int len0 = dis.readByte() & 255;
            model.frameChar[0] = new int[len0];

            for (int m = 0; m < len0; m++) {
               model.frameChar[0][m] = dis.readByte() & 255;
            }

            int len1 = dis.readByte() & 255;
            model.frameChar[1] = new int[len1];

            for (int m = 0; m < len1; m++) {
               model.frameChar[1][m] = dis.readByte() & 255;
            }

            int len3 = dis.readByte() & 255;
            model.frameChar[3] = new int[len3];

            for (int m = 0; m < len3; m++) {
               model.frameChar[3][m] = dis.readByte() & 255;
            }

            model.indexSplash[0] = dis.readByte();
            model.indexSplash[1] = dis.readByte();
            model.indexSplash[2] = dis.readByte();
            model.indexSplash[3] = model.indexSplash[2];
         } catch (EOFException var23) {
         }

         dis.close();
      } else {
         throw new IOException("Số frames không hợp lệ: " + numFrames);
      }
   }

   private void parseLegacyFormat(EffectModel model, byte[] raw) throws IOException {
      DataInputStream dis = new DataInputStream(new ByteArrayInputStream(raw));
      int numImages = dis.readByte() & 255;
      SmallImageDef[] smallImages = new SmallImageDef[numImages];

      for (int i = 0; i < numImages; i++) {
         int imageId = dis.readUnsignedByte();
         if (imageId == 255) {
            int sid = dis.readUnsignedByte();
            int sx = dis.readShort();
            int sy = dis.readShort();
            int sw = dis.readShort();
            int sh = dis.readShort();
            smallImages[i] = new SmallImageDef(sid, sx, sy, sw, sh);
         } else {
            int sx = dis.readUnsignedByte();
            int sy = dis.readUnsignedByte();
            int sw = dis.readUnsignedByte();
            int sh = dis.readUnsignedByte();
            smallImages[i] = new SmallImageDef(imageId, sx, sy, sw, sh);
         }
      }

      model.smallImages = smallImages;
      int numFrames = dis.readShort();
      if (numFrames >= 0 && numFrames <= 10000) {
         EffFrame[] frames = new EffFrame[numFrames];

         for (int j = 0; j < numFrames; j++) {
            int numParts = dis.readByte() & 255;
            List<EffPartFrame> bottom = new ArrayList<>();
            List<EffPartFrame> top = new ArrayList<>();
            List<EffPartFrame> all = new ArrayList<>();

            for (int k = 0; k < numParts; k++) {
               int dx = dis.readShort();
               int dy = dis.readShort();
               int idSmall = dis.readByte() & 255;
               int absDy = Math.abs(dy);
               if (absDy < model.minAbsDy) {
                  model.minAbsDy = absDy;
               }

               if (idSmall >= numImages) {
                  idSmall = 0;
               }

               EffPartFrame pf = new EffPartFrame(dx, dy, idSmall, 0, 1);
               all.add(pf);
               top.add(pf);
            }

            frames[j] = new EffFrame(bottom, top, all);
         }

         model.frames = frames;
         int seqLen = 0;
         if (dis.available() >= 2) {
            int s = dis.readShort() & '\uffff';
            if (s <= 1000 && dis.available() >= s * 2) {
               seqLen = s;
            } else {
               seqLen = s >> 8;
            }
         } else if (dis.available() == 1) {
            seqLen = dis.readUnsignedByte();
         }

         int[] sequence = new int[seqLen];

         for (int l = 0; l < seqLen; l++) {
            sequence[l] = (byte)dis.readShort();
            if (sequence[l] < 0) {
               sequence[l] += 256;
            }

            if (frames.length > 0 && sequence[l] >= frames.length) {
               sequence[l] = 0;
            }
         }

         model.sequence = sequence;
         dis.close();
      } else {
         throw new IOException("Số frames không hợp lệ: " + numFrames);
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

   private static byte[] readAllBytes(File f) throws IOException {
      byte[] var2;
      try (FileInputStream fis = new FileInputStream(f)) {
         var2 = fis.readAllBytes();
      }

      return var2;
   }
}
