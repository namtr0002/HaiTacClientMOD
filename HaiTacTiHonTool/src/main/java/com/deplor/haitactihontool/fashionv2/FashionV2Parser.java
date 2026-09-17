package com.deplor.haitactihontool.fashionv2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class FashionV2Parser {
   public static JSONObject toJsonObject(FashionV2Model.FashionV2 fashion) {
      JSONObject json = new JSONObject();
      json.put("fashionId", fashion.fashionId);
      json.put("name", fashion.name);
      json.put("category", fashion.category);
      json.put("genderReq", fashion.genderReq);
      json.put("defaultSequence", fashion.defaultSequence);
      JSONArray spritesArray = new JSONArray();

      for (FashionV2Model.SpriteV2 spr : fashion.sprites) {
         JSONObject sprObj = new JSONObject();
         sprObj.put("spriteId", spr.spriteId);
         sprObj.put("atlasPath", spr.atlasPath);
         sprObj.put("x", spr.x);
         sprObj.put("y", spr.y);
         sprObj.put("width", spr.width);
         sprObj.put("height", spr.height);
         sprObj.put("pivotX", spr.pivotX);
         sprObj.put("pivotY", spr.pivotY);
         spritesArray.put(sprObj);
      }

      json.put("sprites", spritesArray);
      JSONArray partsArray = new JSONArray();

      for (FashionV2Model.PartV2 part : fashion.parts) {
         JSONObject partObj = new JSONObject();
         partObj.put("partId", part.partId);
         partObj.put("spriteId", part.spriteId);
         partObj.put("slotType", part.slotType);
         partObj.put("defaultZOrder", part.defaultZOrder);
         partsArray.put(partObj);
      }

      json.put("parts", partsArray);
      JSONArray sequencesArray = new JSONArray();

      for (FashionV2Model.SequenceV2 seq : fashion.sequences) {
         JSONObject seqObj = new JSONObject();
         seqObj.put("sequenceId", seq.sequenceId);
         seqObj.put("name", seq.name);
         seqObj.put("fps", seq.fps);
         seqObj.put("isLoop", seq.isLoop);
         JSONArray framesArray = new JSONArray();

         for (FashionV2Model.FrameV2 frame : seq.frames) {
            JSONObject frameObj = new JSONObject();
            frameObj.put("frameIndex", frame.frameIndex);
            frameObj.put("durationMs", frame.durationMs);
            JSONArray transformsArray = new JSONArray();

            for (FashionV2Model.FramePartTransformV2 t : frame.partTransforms) {
               JSONObject tObj = new JSONObject();
               tObj.put("partId", t.partId);
               tObj.put("spriteId", t.spriteId);
               tObj.put("posX", t.posX);
               tObj.put("posY", t.posY);
               tObj.put("scaleX", t.scaleX);
               tObj.put("scaleY", t.scaleY);
               tObj.put("rotation", t.rotation);
               tObj.put("flipX", t.flipX);
               tObj.put("flipY", t.flipY);
               tObj.put("zOrder", t.zOrder);
               tObj.put("opacity", t.opacity);
               tObj.put("tintColor", t.tintColor);
               tObj.put("blendMode", t.blendMode);
               transformsArray.put(tObj);
            }

            frameObj.put("partTransforms", transformsArray);
            framesArray.put(frameObj);
         }

         seqObj.put("frames", framesArray);
         JSONArray eventsArray = new JSONArray();

         for (FashionV2Model.FrameEventV2 ev : seq.events) {
            JSONObject evObj = new JSONObject();
            evObj.put("frameIndex", ev.frameIndex);
            evObj.put("eventName", ev.eventName);
            evObj.put("parameter", ev.parameter);
            eventsArray.put(evObj);
         }

         seqObj.put("events", eventsArray);
         sequencesArray.put(seqObj);
      }

      json.put("sequences", sequencesArray);
      return json;
   }

   public static FashionV2Model.FashionV2 fromJsonObject(JSONObject json) {
      FashionV2Model.FashionV2 fashion = new FashionV2Model.FashionV2();
      fashion.fashionId = json.optString("fashionId", "");
      fashion.name = json.optString("name", "");
      fashion.category = json.optInt("category", 0);
      fashion.genderReq = json.optInt("genderReq", 0);
      fashion.defaultSequence = json.optString("defaultSequence", "idle");
      if (json.has("sprites")) {
         JSONArray spritesArray = json.getJSONArray("sprites");

         for (int i = 0; i < spritesArray.length(); i++) {
            JSONObject sObj = spritesArray.getJSONObject(i);
            FashionV2Model.SpriteV2 spr = new FashionV2Model.SpriteV2();
            spr.spriteId = sObj.optString("spriteId", "");
            spr.atlasPath = sObj.optString("atlasPath", "");
            spr.x = sObj.optInt("x", 0);
            spr.y = sObj.optInt("y", 0);
            spr.width = sObj.optInt("width", 0);
            spr.height = sObj.optInt("height", 0);
            spr.pivotX = (float)sObj.optDouble("pivotX", 0.5);
            spr.pivotY = (float)sObj.optDouble("pivotY", 0.5);
            fashion.sprites.add(spr);
         }
      }

      if (json.has("parts")) {
         JSONArray partsArray = json.getJSONArray("parts");

         for (int i = 0; i < partsArray.length(); i++) {
            JSONObject pObj = partsArray.getJSONObject(i);
            FashionV2Model.PartV2 part = new FashionV2Model.PartV2();
            part.partId = pObj.optString("partId", "");
            part.spriteId = pObj.optString("spriteId", "");
            part.slotType = pObj.optInt("slotType", 0);
            part.defaultZOrder = pObj.optInt("defaultZOrder", 0);
            fashion.parts.add(part);
         }
      }

      if (json.has("sequences")) {
         JSONArray seqArray = json.getJSONArray("sequences");

         for (int i = 0; i < seqArray.length(); i++) {
            JSONObject sObj = json.getJSONArray("sequences").getJSONObject(i);
            FashionV2Model.SequenceV2 seq = new FashionV2Model.SequenceV2();
            seq.sequenceId = sObj.optString("sequenceId", "");
            seq.name = sObj.optString("name", "");
            seq.fps = (float)sObj.optDouble("fps", 12.0);
            seq.isLoop = sObj.optBoolean("isLoop", true);
            if (sObj.has("frames")) {
               JSONArray fArray = sObj.getJSONArray("frames");

               for (int j = 0; j < fArray.length(); j++) {
                  JSONObject fObj = fArray.getJSONObject(j);
                  FashionV2Model.FrameV2 frame = new FashionV2Model.FrameV2();
                  frame.frameIndex = fObj.optInt("frameIndex", j);
                  frame.durationMs = fObj.optInt("durationMs", 83);
                  if (fObj.has("partTransforms")) {
                     JSONArray tArray = fObj.getJSONArray("partTransforms");

                     for (int k = 0; k < tArray.length(); k++) {
                        JSONObject tObj = tArray.getJSONObject(k);
                        FashionV2Model.FramePartTransformV2 t = new FashionV2Model.FramePartTransformV2();
                        t.partId = tObj.optString("partId", "");
                        t.spriteId = tObj.optString("spriteId", null);
                        t.posX = (float)tObj.optDouble("posX", 0.0);
                        t.posY = (float)tObj.optDouble("posY", 0.0);
                        t.scaleX = (float)tObj.optDouble("scaleX", 1.0);
                        t.scaleY = (float)tObj.optDouble("scaleY", 1.0);
                        t.rotation = (float)tObj.optDouble("rotation", 0.0);
                        t.flipX = tObj.optBoolean("flipX", false);
                        t.flipY = tObj.optBoolean("flipY", false);
                        t.zOrder = tObj.optInt("zOrder", 0);
                        t.opacity = (float)tObj.optDouble("opacity", 1.0);
                        t.tintColor = tObj.optString("tintColor", "#FFFFFF");
                        t.blendMode = tObj.optString("blendMode", "NORMAL");
                        frame.partTransforms.add(t);
                     }
                  }

                  seq.frames.add(frame);
               }
            }

            if (sObj.has("events")) {
               JSONArray evArray = sObj.getJSONArray("events");

               for (int j = 0; j < evArray.length(); j++) {
                  JSONObject evObj = evArray.getJSONObject(j);
                  FashionV2Model.FrameEventV2 ev = new FashionV2Model.FrameEventV2();
                  ev.frameIndex = evObj.optInt("frameIndex", 0);
                  ev.eventName = evObj.optString("eventName", "");
                  ev.parameter = evObj.optString("parameter", "");
                  seq.events.add(ev);
               }
            }

            fashion.sequences.add(seq);
         }
      }

      return fashion;
   }

   public static byte[] toBinaryFormat(FashionV2Model.FashionV2 fashion) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(baos);
      dos.writeByte(86);
      dos.writeByte(50);
      dos.writeUTF(fashion.fashionId);
      dos.writeUTF(fashion.name);
      dos.writeByte(fashion.category);
      dos.writeByte(fashion.genderReq);
      dos.writeUTF(fashion.defaultSequence);
      dos.writeShort(fashion.sprites.size());

      for (FashionV2Model.SpriteV2 spr : fashion.sprites) {
         dos.writeUTF(spr.spriteId);
         dos.writeUTF(spr.atlasPath);
         dos.writeShort(spr.x);
         dos.writeShort(spr.y);
         dos.writeShort(spr.width);
         dos.writeShort(spr.height);
         dos.writeFloat(spr.pivotX);
         dos.writeFloat(spr.pivotY);
      }

      dos.writeShort(fashion.parts.size());

      for (FashionV2Model.PartV2 part : fashion.parts) {
         dos.writeUTF(part.partId);
         dos.writeUTF(part.spriteId);
         dos.writeByte(part.slotType);
         dos.writeShort(part.defaultZOrder);
      }

      dos.writeShort(fashion.sequences.size());

      for (FashionV2Model.SequenceV2 seq : fashion.sequences) {
         dos.writeUTF(seq.sequenceId);
         dos.writeUTF(seq.name);
         dos.writeFloat(seq.fps);
         dos.writeBoolean(seq.isLoop);
         dos.writeShort(seq.frames.size());

         for (FashionV2Model.FrameV2 frame : seq.frames) {
            dos.writeShort(frame.frameIndex);
            dos.writeShort(frame.durationMs);
            dos.writeShort(frame.partTransforms.size());

            for (FashionV2Model.FramePartTransformV2 t : frame.partTransforms) {
               dos.writeUTF(t.partId);
               dos.writeUTF(t.spriteId != null ? t.spriteId : "");
               dos.writeFloat(t.posX);
               dos.writeFloat(t.posY);
               dos.writeFloat(t.scaleX);
               dos.writeFloat(t.scaleY);
               dos.writeFloat(t.rotation);
               dos.writeBoolean(t.flipX);
               dos.writeBoolean(t.flipY);
               dos.writeShort(t.zOrder);
               dos.writeFloat(t.opacity);
            }
         }

         dos.writeShort(seq.events.size());

         for (FashionV2Model.FrameEventV2 ev : seq.events) {
            dos.writeShort(ev.frameIndex);
            dos.writeUTF(ev.eventName);
            dos.writeUTF(ev.parameter);
         }
      }

      dos.flush();
      return baos.toByteArray();
   }

   public static FashionV2Model.FashionV2 fromBinaryFormat(byte[] data) throws IOException {
      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      DataInputStream dis = new DataInputStream(bais);
      byte b1 = dis.readByte();
      byte b2 = dis.readByte();
      if (b1 == 86 && b2 == 50) {
         FashionV2Model.FashionV2 fashion = new FashionV2Model.FashionV2();
         fashion.fashionId = dis.readUTF();
         fashion.name = dis.readUTF();
         fashion.category = dis.readByte();
         fashion.genderReq = dis.readByte();
         fashion.defaultSequence = dis.readUTF();
         int spriteCount = dis.readShort();

         for (int i = 0; i < spriteCount; i++) {
            FashionV2Model.SpriteV2 spr = new FashionV2Model.SpriteV2();
            spr.spriteId = dis.readUTF();
            spr.atlasPath = dis.readUTF();
            spr.x = dis.readShort();
            spr.y = dis.readShort();
            spr.width = dis.readShort();
            spr.height = dis.readShort();
            spr.pivotX = dis.readFloat();
            spr.pivotY = dis.readFloat();
            fashion.sprites.add(spr);
         }

         int partCount = dis.readShort();

         for (int i = 0; i < partCount; i++) {
            FashionV2Model.PartV2 part = new FashionV2Model.PartV2();
            part.partId = dis.readUTF();
            part.spriteId = dis.readUTF();
            part.slotType = dis.readByte();
            part.defaultZOrder = dis.readShort();
            fashion.parts.add(part);
         }

         int seqCount = dis.readShort();

         for (int i = 0; i < seqCount; i++) {
            FashionV2Model.SequenceV2 seq = new FashionV2Model.SequenceV2();
            seq.sequenceId = dis.readUTF();
            seq.name = dis.readUTF();
            seq.fps = dis.readFloat();
            seq.isLoop = dis.readBoolean();
            int frameCount = dis.readShort();

            for (int j = 0; j < frameCount; j++) {
               FashionV2Model.FrameV2 frame = new FashionV2Model.FrameV2();
               frame.frameIndex = dis.readShort();
               frame.durationMs = dis.readShort();
               int transformCount = dis.readShort();

               for (int k = 0; k < transformCount; k++) {
                  FashionV2Model.FramePartTransformV2 t = new FashionV2Model.FramePartTransformV2();
                  t.partId = dis.readUTF();
                  String subSpr = dis.readUTF();
                  t.spriteId = subSpr.isEmpty() ? null : subSpr;
                  t.posX = dis.readFloat();
                  t.posY = dis.readFloat();
                  t.scaleX = dis.readFloat();
                  t.scaleY = dis.readFloat();
                  t.rotation = dis.readFloat();
                  t.flipX = dis.readBoolean();
                  t.flipY = dis.readBoolean();
                  t.zOrder = dis.readShort();
                  t.opacity = dis.readFloat();
                  frame.partTransforms.add(t);
               }

               seq.frames.add(frame);
            }

            int eventCount = dis.readShort();

            for (int j = 0; j < eventCount; j++) {
               FashionV2Model.FrameEventV2 ev = new FashionV2Model.FrameEventV2();
               ev.frameIndex = dis.readShort();
               ev.eventName = dis.readUTF();
               ev.parameter = dis.readUTF();
               seq.events.add(ev);
            }

            fashion.sequences.add(seq);
         }

         return fashion;
      } else {
         throw new IOException("Invalid FashionV2 Binary Magic Header");
      }
   }
}
