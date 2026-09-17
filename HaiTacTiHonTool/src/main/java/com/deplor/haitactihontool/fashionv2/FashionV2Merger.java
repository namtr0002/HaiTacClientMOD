package com.deplor.haitactihontool.fashionv2;

import com.deplor.haitactihontool.part.CharInfoData;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONObject;

public class FashionV2Merger {
   public static final int[] M_SORT_PAINT = new int[]{1, 2, 0, 5, 4, 3, 6};

   public static void clearDirectory(File dir) {
      if (dir != null && dir.exists() && dir.isDirectory()) {
         File[] files = dir.listFiles();
         if (files != null) {
            for (File f : files) {
               if (f.isFile()) {
                  f.delete();
               }
            }
         }

         System.out.println("[CLEARED] Đã xóa toàn bộ data cũ trong: " + dir.getAbsolutePath());
      }
   }

   public static FashionV2Merger.MergeResult mergeFashionFromTemplate(int fashionId, String name, String mwearStr, File partsSqlFile, File iconDir) throws Exception {
      JSONArray mwearArr = new JSONArray(mwearStr);
      List<Integer> validPartIds = new ArrayList<>();
      List<Integer> slotIndices = new ArrayList<>();
      int weaponId = mwearArr.length() > 0 ? mwearArr.getInt(0) : -1;
      int hatId = mwearArr.length() > 1 ? mwearArr.getInt(1) : -1;
      int bodyId = mwearArr.length() > 3 ? mwearArr.getInt(3) : -1;
      int legId = mwearArr.length() > 5 ? mwearArr.getInt(5) : -1;
      int headId = mwearArr.length() > 6 ? mwearArr.getInt(6) : -1;
      int hairId = mwearArr.length() > 7 ? mwearArr.getInt(7) : -1;
      if (headId > 0) {
         validPartIds.add(headId);
         slotIndices.add(0);
      }

      if (bodyId > 0) {
         validPartIds.add(bodyId);
         slotIndices.add(2);
      }

      if (legId > 0) {
         validPartIds.add(legId);
         slotIndices.add(1);
      }

      if (weaponId > 0) {
         validPartIds.add(weaponId);
         slotIndices.add(3);
      }

      if (hatId > 0) {
         validPartIds.add(hatId);
         slotIndices.add(4);
      }

      if (hairId > 0) {
         validPartIds.add(hairId);
         slotIndices.add(5);
      }

      List<FashionV2Merger.MergedPartData> mergedParts = loadPartsFromSql(partsSqlFile, validPartIds, slotIndices);
      FashionV2Model.FashionV2 fashion = new FashionV2Model.FashionV2();
      fashion.fashionId = String.valueOf(fashionId);
      fashion.name = name;
      fashion.category = 0;
      fashion.defaultSequence = "seq_idle";
      List<File> imageFilesToPack = new ArrayList<>();
      List<Integer> imgIdsList = new ArrayList<>();

      for (FashionV2Merger.MergedPartData mp : mergedParts) {
         if (mp.imagesData != null) {
            for (int[] row : mp.imagesData) {
               int imgId = row[0];
               if (!imgIdsList.contains(imgId)) {
                  imgIdsList.add(imgId);
                  int realIconId = imgId >= 10000 ? imgId - 10000 + 26000 : imgId;
                  File imgFile = new File(iconDir, realIconId + ".png");
                  if (imgFile.exists()) {
                     imageFilesToPack.add(imgFile);
                  } else {
                     System.err.println("[WARN] File icon không tồn tại: " + imgFile.getAbsolutePath() + " (SmallImage ID=" + imgId + ")");
                  }
               }
            }
         }
      }

      String atlasFileName = fashionId + ".png";
      FashionV2AtlasMerger.AtlasResult atlasResult = FashionV2AtlasMerger.buildAtlas(imageFilesToPack, atlasFileName);

      for (Entry<String, FashionV2AtlasMerger.SpriteRect> entry : atlasResult.spriteMap.entrySet()) {
         FashionV2AtlasMerger.SpriteRect rect = entry.getValue();
         String rawFileName = rect.spriteId;
         String originalSmallImgId = rawFileName;

         try {
            int realId = Integer.parseInt(rawFileName);
            if (realId >= 26000) {
               int origId = realId - 26000 + 10000;
               originalSmallImgId = String.valueOf(origId);
            }
         } catch (Exception var42) {
         }

         FashionV2Model.SpriteV2 sprite = new FashionV2Model.SpriteV2();
         sprite.spriteId = originalSmallImgId;
         sprite.atlasPath = atlasFileName;
         sprite.x = rect.x;
         sprite.y = rect.y;
         sprite.width = rect.width;
         sprite.height = rect.height;
         sprite.pivotX = 0.5F;
         sprite.pivotY = 0.5F;
         fashion.sprites.add(sprite);
      }

      for (FashionV2Merger.MergedPartData mpx : mergedParts) {
         FashionV2Model.PartV2 part = new FashionV2Model.PartV2();
         part.partId = "part_slot" + mpx.slotIndex + "_" + mpx.partId;
         part.spriteId = "sprite_part_" + mpx.partId;
         part.slotType = mpx.slotIndex;
         part.defaultZOrder = mpx.slotIndex;
         fashion.parts.add(part);
      }

      FashionV2Model.SequenceV2 seqIdle = new FashionV2Model.SequenceV2("seq_idle", "Idle (Đứng chờ)");
      seqIdle.fps = 12.0F;
      seqIdle.isLoop = true;
      int frameCount = CharInfoData.CharInfo.length;

      for (int f = 0; f < frameCount; f++) {
         FashionV2Model.FrameV2 frame = new FashionV2Model.FrameV2(f, 83);

         for (int sortSlot : M_SORT_PAINT) {
            for (FashionV2Merger.MergedPartData mpx : mergedParts) {
               int slotIdx = mpx.type;
               if (slotIdx == sortSlot && slotIdx < CharInfoData.CharInfo[f].length) {
                  int imgIdxInPart = CharInfoData.CharInfo[f][slotIdx][0];
                  int charInfoDx = CharInfoData.CharInfo[f][slotIdx][1];
                  int charInfoDy = CharInfoData.CharInfo[f][slotIdx][2];
                  int partImgDx = 0;
                  int partImgDy = 0;
                  String subSpriteId = null;
                  if (mpx.imagesData != null && imgIdxInPart >= 0 && imgIdxInPart < mpx.imagesData.length) {
                     subSpriteId = String.valueOf(mpx.imagesData[imgIdxInPart][0]);
                     partImgDx = mpx.imagesData[imgIdxInPart][1];
                     partImgDy = mpx.imagesData[imgIdxInPart][2];
                  }

                  float finalX = charInfoDx + partImgDx;
                  float finalY = charInfoDy + partImgDy;
                  String partIdStr = "part_slot" + mpx.slotIndex + "_" + mpx.partId;
                  FashionV2Model.FramePartTransformV2 transform = new FashionV2Model.FramePartTransformV2(partIdStr, subSpriteId, finalX, finalY);
                  transform.zOrder = sortSlot;
                  frame.partTransforms.add(transform);
               }
            }
         }

         seqIdle.frames.add(frame);
      }

      fashion.sequences.add(seqIdle);
      FashionV2Model.SequenceV2 seqAttack = new FashionV2Model.SequenceV2("seq_attack", "Attack (Tấn công)");
      seqAttack.fps = 14.0F;
      seqAttack.isLoop = false;

      for (int f = 0; f < frameCount; f++) {
         FashionV2Model.FrameV2 frame = new FashionV2Model.FrameV2(f, 71);

         for (int sortSlot : M_SORT_PAINT) {
            for (FashionV2Merger.MergedPartData mpxx : mergedParts) {
               int slotIdx = mpxx.type;
               if (slotIdx == sortSlot && slotIdx < CharInfoData.CharInfo[f].length) {
                  int imgIdxInPart = CharInfoData.CharInfo[f][slotIdx][0];
                  int charInfoDx = CharInfoData.CharInfo[f][slotIdx][1];
                  int charInfoDy = CharInfoData.CharInfo[f][slotIdx][2];
                  int partImgDx = 0;
                  int partImgDy = 0;
                  String subSpriteId = null;
                  if (mpxx.imagesData != null && imgIdxInPart >= 0 && imgIdxInPart < mpxx.imagesData.length) {
                     subSpriteId = String.valueOf(mpxx.imagesData[imgIdxInPart][0]);
                     partImgDx = mpxx.imagesData[imgIdxInPart][1];
                     partImgDy = mpxx.imagesData[imgIdxInPart][2];
                  }

                  float finalX = charInfoDx + partImgDx;
                  float finalY = charInfoDy + partImgDy;
                  String partIdStr = "part_slot" + mpxx.slotIndex + "_" + mpxx.partId;
                  FashionV2Model.FramePartTransformV2 transform = new FashionV2Model.FramePartTransformV2(partIdStr, subSpriteId, finalX, finalY);
                  transform.zOrder = sortSlot;
                  if (mpxx.slotIndex == 3) {
                     transform.rotation = f % 6 * 15.0F;
                  }

                  frame.partTransforms.add(transform);
               }
            }
         }

         seqAttack.frames.add(frame);
      }

      fashion.sequences.add(seqAttack);
      FashionV2Merger.MergeResult result = new FashionV2Merger.MergeResult();
      result.fashion = fashion;
      result.atlasImage = atlasResult.atlasImage;
      return result;
   }

   private static List<FashionV2Merger.MergedPartData> loadPartsFromSql(File sqlFile, List<Integer> targetIds, List<Integer> slotIndices) throws Exception {
      List<FashionV2Merger.MergedPartData> results = new ArrayList<>();
      if (sqlFile != null && sqlFile.exists()) {
         List<String> lines = Files.readAllLines(sqlFile.toPath());

         for (int i = 0; i < targetIds.size(); i++) {
            int targetId = targetIds.get(i);
            int slotIdx = slotIndices.get(i);

            for (String line : lines) {
               if (line.contains("VALUES (" + targetId + ",") || line.contains("VALUES (" + targetId + " ,")) {
                  int firstParen = line.indexOf("(");
                  int lastParen = line.lastIndexOf(")");
                  if (firstParen != -1 && lastParen != -1) {
                     String content = line.substring(firstParen + 1, lastParen);
                     String[] parts = content.split(",", 3);
                     byte pType = Byte.parseByte(parts[1].trim());
                     String jsonStr = parts[2].trim();
                     if (jsonStr.startsWith("'") && jsonStr.endsWith("'")) {
                        jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
                     }

                     JSONArray arr = new JSONArray(jsonStr);
                     int[][] imgData = new int[arr.length()][3];

                     for (int k = 0; k < arr.length(); k++) {
                        JSONArray item = arr.getJSONArray(k);
                        imgData[k][0] = item.getInt(0);
                        imgData[k][1] = item.getInt(1);
                        imgData[k][2] = item.getInt(2);
                     }

                     results.add(new FashionV2Merger.MergedPartData(targetId, slotIdx, pType, imgData));
                     break;
                  }
               }
            }
         }

         return results;
      } else {
         return results;
      }
   }

   public static void saveMergedResult(FashionV2Merger.MergeResult result, File outputDir, String baseIdName) throws Exception {
      if (!outputDir.exists()) {
         outputDir.mkdirs();
      }

      String atlasFileName = baseIdName + ".png";
      if (result.fashion != null && result.fashion.sprites != null) {
         for (FashionV2Model.SpriteV2 spr : result.fashion.sprites) {
            spr.atlasPath = atlasFileName;
         }
      }

      if (result.atlasImage != null) {
         FashionV2AtlasMerger.saveMultiScaleAtlas(result.atlasImage, outputDir, baseIdName);
      }

      File jsonFile = new File(outputDir, baseIdName + ".json");
      JSONObject json = FashionV2Parser.toJsonObject(result.fashion);
      Files.writeString(jsonFile.toPath(), json.toString(2));
      System.out.println("[SUCCESS] Saved Fashion V2 JSON: " + jsonFile.getAbsolutePath());
      byte[] binData = FashionV2Parser.toBinaryFormat(result.fashion);
      File binFile1 = new File(outputDir, baseIdName + ".bin");
      Files.write(binFile1.toPath(), binData);
      File binFile2 = new File(outputDir, baseIdName + ".binary");
      Files.write(binFile2.toPath(), binData);
      System.out.println("[SUCCESS] Saved Fashion V2 BINARY: " + binFile1.getAbsolutePath());
   }

   public static class MergeResult {
      public FashionV2Model.FashionV2 fashion;
      public BufferedImage atlasImage;
   }

   public static class MergedPartData {
      public int partId;
      public int slotIndex;
      public byte type;
      public int[][] imagesData;

      public MergedPartData(int partId, int slotIndex, byte type, int[][] imagesData) {
         this.partId = partId;
         this.slotIndex = slotIndex;
         this.type = type;
         this.imagesData = imagesData;
      }
   }
}
