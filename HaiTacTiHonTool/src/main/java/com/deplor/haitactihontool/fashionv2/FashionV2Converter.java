package com.deplor.haitactihontool.fashionv2;

import com.deplor.haitactihontool.part.CharInfoData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;

public class FashionV2Converter {
   public static FashionV2Model.FashionV2 convertLegacyToV2(short partId, byte type, int[][] partImagesData) {
      FashionV2Model.FashionV2 fashion = new FashionV2Model.FashionV2();
      fashion.fashionId = "fashion_legacy_" + partId;
      fashion.name = "Legacy Fashion Part #" + partId;
      fashion.category = type;
      FashionV2Model.PartV2 mainPart = new FashionV2Model.PartV2();
      mainPart.partId = "part_" + partId;
      mainPart.spriteId = "sprite_part_" + partId;
      mainPart.slotType = type;
      mainPart.defaultZOrder = type;
      fashion.parts.add(mainPart);
      FashionV2Model.SequenceV2 seq = new FashionV2Model.SequenceV2("seq_legacy", "legacy_anim");
      seq.fps = 12.0F;
      seq.isLoop = true;
      int matrixFrameCount = CharInfoData.CharInfo.length;

      for (int f = 0; f < matrixFrameCount; f++) {
         FashionV2Model.FrameV2 frame = new FashionV2Model.FrameV2(f, 83);

         int slotIdx = switch (type) {
            case 0 -> 0;
            case 1 -> 2;
            case 2 -> 1;
            case 3 -> 3;
            case 4 -> 4;
            case 5 -> 5;
            case 6 -> 6;
            default -> type;
         };
         if (slotIdx >= 0 && slotIdx < CharInfoData.CharInfo[f].length) {
            int imgIdxInPart = CharInfoData.CharInfo[f][slotIdx][0];
            int charInfoDx = CharInfoData.CharInfo[f][slotIdx][1];
            int charInfoDy = CharInfoData.CharInfo[f][slotIdx][2];
            int partImgDx = 0;
            int partImgDy = 0;
            if (partImagesData != null && imgIdxInPart >= 0 && imgIdxInPart < partImagesData.length) {
               partImgDx = partImagesData[imgIdxInPart][1];
               partImgDy = partImagesData[imgIdxInPart][2];
            }

            float finalX = charInfoDx + partImgDx;
            float finalY = charInfoDy + partImgDy;
            FashionV2Model.FramePartTransformV2 t = new FashionV2Model.FramePartTransformV2(mainPart.partId, finalX, finalY);
            t.zOrder = type;
            frame.partTransforms.add(t);
         }

         seq.frames.add(frame);
      }

      fashion.sequences.add(seq);
      return fashion;
   }

   public static FashionV2Model.FashionV2 parseLegacyPartFromSql(File sqlFile, int targetPartId) {
      if (sqlFile != null && sqlFile.exists()) {
         Pattern pattern = Pattern.compile("VALUES\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*'([^']+)'\\s*\\)", 2);

         try (BufferedReader br = new BufferedReader(new FileReader(sqlFile))) {
            String line;
            while ((line = br.readLine()) != null) {
               Matcher m = pattern.matcher(line);
               if (m.find()) {
                  int pId = Integer.parseInt(m.group(1));
                  if (pId == targetPartId) {
                     byte pType = Byte.parseByte(m.group(2));
                     String jsonStr = m.group(3);
                     JSONArray arr = new JSONArray(jsonStr);
                     int[][] partImgs = new int[arr.length()][3];

                     for (int i = 0; i < arr.length(); i++) {
                        JSONArray item = arr.getJSONArray(i);
                        partImgs[i][0] = item.getInt(0);
                        partImgs[i][1] = item.getInt(1);
                        partImgs[i][2] = item.getInt(2);
                     }

                     return convertLegacyToV2((short)pId, pType, partImgs);
                  }
               }
            }

            return null;
         } catch (Exception var15) {
            var15.printStackTrace();
            return null;
         }
      } else {
         return null;
      }
   }

   public static String exportV2ToLegacySql(FashionV2Model.FashionV2 fashion, int targetPartId) {
      StringBuilder sql = new StringBuilder();
      sql.append("-- Exported Legacy Parts SQL for FashionV2: ").append(fashion.fashionId).append("\n");
      sql.append("REPLACE INTO `parts` (`id`, `type`, `data`) VALUES (");
      sql.append(targetPartId).append(", ");
      sql.append(fashion.category).append(", ");
      FashionV2Model.SequenceV2 targetSeq = null;

      for (FashionV2Model.SequenceV2 s : fashion.sequences) {
         if (s.sequenceId.equalsIgnoreCase(fashion.defaultSequence) || s.name.equalsIgnoreCase(fashion.defaultSequence)) {
            targetSeq = s;
            break;
         }
      }

      if (targetSeq == null && !fashion.sequences.isEmpty()) {
         targetSeq = fashion.sequences.get(0);
      }

      StringBuilder dataJson = new StringBuilder("[");
      if (targetSeq != null && !targetSeq.frames.isEmpty()) {
         List<FashionV2Model.FrameV2> frames = targetSeq.frames;

         for (int i = 0; i < frames.size(); i++) {
            FashionV2Model.FrameV2 f = frames.get(i);
            int dx = 0;
            int dy = 0;
            if (!f.partTransforms.isEmpty()) {
               FashionV2Model.FramePartTransformV2 t = f.partTransforms.get(0);
               dx = Math.round(t.posX);
               dy = Math.round(t.posY);
            }

            if (i > 0) {
               dataJson.append(",");
            }

            dataJson.append("[").append(i).append(",").append(dx).append(",").append(dy).append("]");
         }
      }

      dataJson.append("]");
      sql.append("'").append(dataJson.toString()).append("');\n");
      return sql.toString();
   }
}
