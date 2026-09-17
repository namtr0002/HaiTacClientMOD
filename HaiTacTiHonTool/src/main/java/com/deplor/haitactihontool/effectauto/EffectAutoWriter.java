package com.deplor.haitactihontool.effectauto;

import com.deplor.haitactihontool.effect.EffFrame;
import com.deplor.haitactihontool.effect.EffPartFrame;
import com.deplor.haitactihontool.effect.SmallImageDef;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class EffectAutoWriter {
   public void write(EffectAutoModel model, File outData) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeInt(72);
      int ni = model.smallImages != null ? model.smallImages.length : 0;
      dos.writeByte(ni);
      if (model.smallImages != null) {
         for (SmallImageDef s : model.smallImages) {
            dos.writeByte(s.id & 0xFF);
            dos.writeByte(s.x & 0xFF);
            dos.writeByte(s.y & 0xFF);
            dos.writeByte(s.w & 0xFF);
            dos.writeByte(s.h & 0xFF);
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
            }
         }
      }

      int sl = model.getSeqLen();
      dos.writeShort(sl);
      if (model.sequence != null) {
         for (int v : model.sequence) {
            dos.writeByte(v & 0xFF);
         }
      }

      dos.writeByte(model.typeEffect & 0xFF);
      dos.writeByte(model.valueEffect & 0xFF);
      dos.flush();
      if (outData.getParentFile() != null) {
         outData.getParentFile().mkdirs();
      }

      try (FileOutputStream fos = new FileOutputStream(outData)) {
         fos.write(baos.toByteArray());
      }
   }

   public JSONObject toJsonObject(EffectAutoModel model) {
      JSONObject root = new JSONObject();
      root.put("id", model.id);
      root.put("typeEffect", model.typeEffect);
      root.put("valueEffect", model.valueEffect);
      JSONArray spritesArr = new JSONArray();
      if (model.smallImages != null) {
         for (SmallImageDef s : model.smallImages) {
            JSONObject spr = new JSONObject();
            spr.put("id", s.id);
            spr.put("x", s.x);
            spr.put("y", s.y);
            spr.put("w", s.w);
            spr.put("h", s.h);
            spritesArr.put(spr);
         }
      }

      root.put("sprites", spritesArr);
      JSONArray framesArr = new JSONArray();
      if (model.frames != null) {
         for (int f = 0; f < model.frames.length; f++) {
            EffFrame frame = model.frames[f];
            JSONObject fObj = new JSONObject();
            fObj.put("index", f);
            JSONArray partsArr = new JSONArray();

            for (EffPartFrame p : frame.allParts) {
               JSONObject pObj = new JSONObject();
               pObj.put("dx", p.dx);
               pObj.put("dy", p.dy);
               pObj.put("idSmallImg", p.idSmallImg);
               partsArr.put(pObj);
            }

            fObj.put("parts", partsArr);
            framesArr.put(fObj);
         }
      }

      root.put("frames", framesArr);
      JSONArray seqArr = new JSONArray();
      if (model.sequence != null) {
         for (int v : model.sequence) {
            seqArr.put(v);
         }
      }

      root.put("sequence", seqArr);
      return root;
   }

   public String generateEffectAutoDataSql(EffectAutoModel model) {
      String json = this.toJsonObject(model).toString(2).replace("'", "''");
      return "INSERT INTO `effect_auto_data` (`id`, `type_effect`, `value_effect`, `data_json`)\nVALUES ("
         + model.id
         + ", "
         + model.typeEffect
         + ", "
         + model.valueEffect
         + ", '"
         + json
         + "')\nON DUPLICATE KEY UPDATE `type_effect` = VALUES(`type_effect`), `value_effect` = VALUES(`value_effect`), `data_json` = VALUES(`data_json`);\n";
   }

   public void writeText(String content, File outFile) throws IOException {
      if (outFile.getParentFile() != null) {
         outFile.getParentFile().mkdirs();
      }

      try (FileOutputStream fos = new FileOutputStream(outFile)) {
         fos.write(content.getBytes(StandardCharsets.UTF_8));
      }
   }
}
