package com.deplor.haitactihontool.effect;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class EffectWriter {
   public void write(EffectModel model, File outData) throws IOException {
      this.write(model, outData, true);
   }

   public void write(EffectModel model, File outData, boolean includeAngle) throws IOException {
      byte[] data = this.getBytes(model, includeAngle);
      if (outData.getParentFile() != null) {
         outData.getParentFile().mkdirs();
      }

      try (FileOutputStream fos = new FileOutputStream(outData)) {
         fos.write(data);
      }
   }

   public byte[] getBytes(EffectModel model) throws IOException {
      return this.getBytes(model, true);
   }

   public byte[] getBytes(EffectModel model, boolean includeAngle) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      int ni = model.smallImages != null ? model.smallImages.length : 0;
      dos.writeByte(ni);
      if (model.smallImages != null) {
         for (SmallImageDef s : model.smallImages) {
            if (s.x <= 255 && s.y <= 255 && s.w <= 255 && s.h <= 255 && s.id <= 254) {
               dos.writeByte(s.id & 0xFF);
               dos.writeByte(s.x & 0xFF);
               dos.writeByte(s.y & 0xFF);
               dos.writeByte(s.w & 0xFF);
               dos.writeByte(s.h & 0xFF);
            } else {
               dos.writeByte(255);
               dos.writeByte(s.id & 0xFF);
               dos.writeShort(s.x);
               dos.writeShort(s.y);
               dos.writeShort(s.w);
               dos.writeShort(s.h);
            }
         }
      }

      int nf = model.frames != null ? model.frames.length : 0;
      dos.writeShort(nf);
      if (model.frames != null) {
         for (EffFrame frame : model.frames) {
            int np = frame.allParts.size();
            dos.writeByte(np & 0xFF);

            for (EffPartFrame p : frame.allParts) {
               dos.writeShort(p.dx);
               dos.writeShort(p.dy);
               dos.writeByte(p.idSmallImg & 0xFF);
               if (includeAngle && p.rotate != 0) {
                  dos.writeByte(128);
                  dos.writeByte(p.flip & 0xFF);
                  dos.writeShort(p.rotate);
               } else {
                  dos.writeByte(p.flip & 0xFF);
               }

               dos.writeByte(p.onTop & 0xFF);
            }
         }
      }

      int sl = model.sequence != null ? model.sequence.length : 0;
      dos.writeByte(sl & 0xFF);
      if (model.sequence != null) {
         for (int v : model.sequence) {
            dos.writeShort(v & 0xFF);
         }
      }

      dos.writeByte(0);

      for (int ci : new int[]{0, 1, 3}) {
         int[] fc = model.frameChar != null && model.frameChar.length > ci && model.frameChar[ci] != null ? model.frameChar[ci] : new int[0];
         dos.writeByte(fc.length & 0xFF);

         for (int v : fc) {
            dos.writeByte(v & 0xFF);
         }
      }

      if (model.indexSplash != null && model.indexSplash.length >= 3) {
         dos.writeByte(model.indexSplash[0]);
         dos.writeByte(model.indexSplash[1]);
         dos.writeByte(model.indexSplash[2]);
      } else {
         dos.writeByte(0);
         dos.writeByte(0);
         dos.writeByte(0);
      }

      dos.flush();
      return baos.toByteArray();
   }

   public JSONObject toJsonObject(EffectModel model) {
      return this.toJsonObject(model, true);
   }

   public JSONObject toJsonObject(EffectModel model, boolean includeAngle) {
      JSONObject root = new JSONObject();
      root.put("id", model.id);
      JSONArray arrSi = new JSONArray();
      if (model.smallImages != null) {
         for (SmallImageDef s : model.smallImages) {
            JSONObject si = new JSONObject();
            si.put("id", s.id);
            si.put("x", s.x);
            si.put("y", s.y);
            si.put("w", s.w);
            si.put("h", s.h);
            arrSi.put(si);
         }
      }

      root.put("smallImages", arrSi);
      JSONArray arrFr = new JSONArray();
      if (model.frames != null) {
         for (EffFrame frame : model.frames) {
            JSONObject fr = new JSONObject();
            JSONArray arrPa = new JSONArray();

            for (EffPartFrame p : frame.allParts) {
               JSONObject pa = new JSONObject();
               pa.put("dx", p.dx);
               pa.put("dy", p.dy);
               pa.put("imgId", p.idSmallImg);
               pa.put("flip", p.flip);
               pa.put("onTop", p.onTop);
               pa.put("rotate", includeAngle ? p.rotate : 0);
               arrPa.put(pa);
            }

            fr.put("parts", arrPa);
            arrFr.put(fr);
         }
      }

      root.put("frames", arrFr);
      JSONArray arrSeq = new JSONArray();
      if (model.sequence != null) {
         for (int v : model.sequence) {
            arrSeq.put(v);
         }
      }

      root.put("sequence", arrSeq);
      JSONArray arrFcRoot = new JSONArray();
      if (model.frameChar != null) {
         for (int[] fc : model.frameChar) {
            JSONArray fcArr = new JSONArray();
            if (fc != null) {
               for (int v : fc) {
                  fcArr.put(v);
               }
            }

            arrFcRoot.put(fcArr);
         }
      }

      root.put("frameChar", arrFcRoot);
      JSONArray arrSp = new JSONArray();
      if (model.indexSplash != null) {
         for (int v : model.indexSplash) {
            arrSp.put(v);
         }
      }

      root.put("indexSplash", arrSp);
      return root;
   }

   public JSONArray toJsonArray(EffectModel model) {
      return this.toJsonArray(model, true);
   }

   public JSONArray toJsonArray(EffectModel model, boolean includeAngle) {
      JSONArray root = new JSONArray();
      root.put(model.id);
      JSONArray arrSi = new JSONArray();
      if (model.smallImages != null) {
         for (SmallImageDef s : model.smallImages) {
            JSONArray item = new JSONArray();
            item.put(s.id);
            item.put(s.x);
            item.put(s.y);
            item.put(s.w);
            item.put(s.h);
            arrSi.put(item);
         }
      }

      root.put(arrSi);
      JSONArray arrFr = new JSONArray();
      if (model.frames != null) {
         for (EffFrame frame : model.frames) {
            JSONArray frameItems = new JSONArray();

            for (EffPartFrame p : frame.allParts) {
               JSONArray part = new JSONArray();
               part.put(p.dx);
               part.put(p.dy);
               part.put(p.idSmallImg);
               part.put(p.flip);
               part.put(p.onTop);
               part.put(includeAngle ? p.rotate : 0);
               frameItems.put(part);
            }

            arrFr.put(frameItems);
         }
      }

      root.put(arrFr);
      JSONArray arrSeq = new JSONArray();
      if (model.sequence != null) {
         for (int v : model.sequence) {
            arrSeq.put(v);
         }
      }

      root.put(arrSeq);
      JSONArray arrFcRoot = new JSONArray();
      if (model.frameChar != null) {
         for (int[] fc : model.frameChar) {
            JSONArray fcArr = new JSONArray();
            if (fc != null) {
               for (int v : fc) {
                  fcArr.put(v);
               }
            }

            arrFcRoot.put(fcArr);
         }
      }

      root.put(arrFcRoot);
      JSONArray arrSp = new JSONArray();
      if (model.indexSplash != null) {
         for (int v : model.indexSplash) {
            arrSp.put(v);
         }
      }

      root.put(arrSp);
      return root;
   }

   public String generateEffectDataSql(EffectModel model) {
      return this.generateEffectDataSql(model, true);
   }

   public String generateEffectDataSql(EffectModel model, boolean includeAngle) {
      int nImg = model.smallImages != null ? model.smallImages.length : 0;
      JSONArray spritesArr = new JSONArray();
      if (model.smallImages != null) {
         for (SmallImageDef s : model.smallImages) {
            JSONObject si = new JSONObject();
            si.put("idx", s.id);
            si.put("id", s.id);
            si.put("x", s.x);
            si.put("y", s.y);
            si.put("w", s.w);
            si.put("h", s.h);
            spritesArr.put(si);
         }
      }

      JSONArray framesArr = new JSONArray();
      if (model.frames != null) {
         for (EffFrame frame : model.frames) {
            JSONObject fr = new JSONObject();
            JSONArray partsArr = new JSONArray();

            for (EffPartFrame p : frame.allParts) {
               JSONObject pa = new JSONObject();
               pa.put("dx", p.dx);
               pa.put("dy", p.dy);
               pa.put("img_id", p.idSmallImg);
               pa.put("imgId", p.idSmallImg);
               pa.put("flip", p.flip);
               pa.put("on_top", p.onTop);
               pa.put("onTop", p.onTop);
               pa.put("rotate", includeAngle ? p.rotate : 0);
               partsArr.put(pa);
            }

            fr.put("parts", partsArr);
            framesArr.put(fr);
         }
      }

      JSONArray seqArr = new JSONArray();
      if (model.sequence != null) {
         for (int v : model.sequence) {
            seqArr.put(v);
         }
      }

      JSONArray fcArr = new JSONArray();
      if (model.frameChar != null) {
         for (int[] fc : model.frameChar) {
            JSONArray subArr = new JSONArray();
            if (fc != null) {
               for (int v : fc) {
                  subArr.put(v);
               }
            }

            fcArr.put(subArr);
         }
      }

      JSONArray spArr = new JSONArray();
      if (model.indexSplash != null) {
         for (int v : model.indexSplash) {
            spArr.put(v);
         }
      }

      String escSprites = spritesArr.toString().replace("'", "''");
      String escFrames = framesArr.toString().replace("'", "''");
      String escSeq = seqArr.toString().replace("'", "''");
      String escFc = fcArr.toString().replace("'", "''");
      String escSp = spArr.toString().replace("'", "''");
      return "INSERT INTO `effect_data` (`id`, `nImg`, `sprites`, `frames`, `squence`, `frame_char`, `splash`)\nVALUES ("
         + model.id
         + ", "
         + nImg
         + ", '"
         + escSprites
         + "', '"
         + escFrames
         + "', '"
         + escSeq
         + "', '"
         + escFc
         + "', '"
         + escSp
         + "')\nON DUPLICATE KEY UPDATE `nImg` = VALUES(`nImg`), `sprites` = VALUES(`sprites`), `frames` = VALUES(`frames`), `squence` = VALUES(`squence`), `frame_char` = VALUES(`frame_char`), `splash` = VALUES(`splash`);\n";
   }

   public String generateSqlInsert(EffectModel model, byte[] binaryData, String jsonStr) {
      StringBuilder hex = new StringBuilder();

      for (byte b : binaryData) {
         hex.append(String.format("%02X", b));
      }

      String escapedJson = jsonStr.replace("'", "''");
      return this.generateEffectDataSql(model)
         + "\n-- Also for skill_effects table:\nINSERT INTO `skill_effects` (`id`, `data_binary`, `data_json`) \nVALUES ("
         + model.id
         + ", UNHEX('"
         + hex
         + "'), '"
         + escapedJson
         + "')\nON DUPLICATE KEY UPDATE `data_binary` = UNHEX('"
         + hex
         + "'), `data_json` = '"
         + escapedJson
         + "';";
   }

   public void writeText(String content, File outFile) throws IOException {
      try (FileOutputStream fos = new FileOutputStream(outFile)) {
         fos.write(content.getBytes(StandardCharsets.UTF_8));
      }
   }

   public static EffectModel createBlank(int id) {
      EffectModel m = new EffectModel(id);
      m.smallImages = new SmallImageDef[0];
      m.frames = new EffFrame[0];
      m.sequence = new int[0];
      m.frameChar = new int[4][0];
      m.indexSplash = new int[4];
      return m;
   }
}
