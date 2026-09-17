package com.deplor.haitactihontool.fashionv2;

import java.util.ArrayList;
import java.util.List;

public class FashionV2Model {
   public static class EffectV2 {
      public String effectId;
      public String type;
      public String targetPartId;
      public String colorHex = "#00FFFF";
      public float intensity = 1.0F;
      public FashionV2Model.ParticleEmitterV2 particleEmitter;
   }

   public static class FashionV2 {
      public String fashionId;
      public String name;
      public int category = 0;
      public int genderReq = 0;
      public String defaultSequence = "idle";
      public List<FashionV2Model.SpriteV2> sprites = new ArrayList<>();
      public List<FashionV2Model.PartV2> parts = new ArrayList<>();
      public List<FashionV2Model.SequenceV2> sequences = new ArrayList<>();

      public FashionV2() {
      }

      public FashionV2(String fashionId, String name) {
         this.fashionId = fashionId;
         this.name = name;
      }
   }

   public static class FrameEventV2 {
      public int frameIndex;
      public String eventName;
      public String parameter;
   }

   public static class FramePartTransformV2 {
      public String partId;
      public String spriteId;
      public float posX;
      public float posY;
      public float scaleX = 1.0F;
      public float scaleY = 1.0F;
      public float rotation = 0.0F;
      public boolean flipX = false;
      public boolean flipY = false;
      public int zOrder = 0;
      public float opacity = 1.0F;
      public String tintColor = "#FFFFFF";
      public String blendMode = "NORMAL";

      public FramePartTransformV2() {
      }

      public FramePartTransformV2(String partId, float posX, float posY) {
         this.partId = partId;
         this.posX = posX;
         this.posY = posY;
      }

      public FramePartTransformV2(String partId, String spriteId, float posX, float posY) {
         this.partId = partId;
         this.spriteId = spriteId;
         this.posX = posX;
         this.posY = posY;
      }
   }

   public static class FrameV2 {
      public int frameIndex;
      public int durationMs = 83;
      public List<FashionV2Model.FramePartTransformV2> partTransforms = new ArrayList<>();
      public List<FashionV2Model.EffectV2> frameEffects = new ArrayList<>();

      public FrameV2() {
      }

      public FrameV2(int frameIndex, int durationMs) {
         this.frameIndex = frameIndex;
         this.durationMs = durationMs;
      }
   }

   public static class PartV2 {
      public String partId;
      public String spriteId;
      public int slotType;
      public int defaultZOrder;

      public PartV2() {
      }

      public PartV2(String partId, String spriteId, int slotType, int defaultZOrder) {
         this.partId = partId;
         this.spriteId = spriteId;
         this.slotType = slotType;
         this.defaultZOrder = defaultZOrder;
      }
   }

   public static class ParticleEmitterV2 {
      public String emitterId;
      public String particleType;
      public float posX;
      public float posY;
      public int emitCount = 10;
      public float lifetime = 1.0F;
      public String colorGradient = "#FFD700->#FF0000";
   }

   public static class SequenceV2 {
      public String sequenceId;
      public String name;
      public float fps = 12.0F;
      public boolean isLoop = true;
      public List<FashionV2Model.FrameV2> frames = new ArrayList<>();
      public List<FashionV2Model.FrameEventV2> events = new ArrayList<>();

      public SequenceV2() {
      }

      public SequenceV2(String sequenceId, String name) {
         this.sequenceId = sequenceId;
         this.name = name;
      }
   }

   public static class SpriteV2 {
      public String spriteId;
      public String atlasPath;
      public int x;
      public int y;
      public int width;
      public int height;
      public float pivotX = 0.5F;
      public float pivotY = 0.5F;

      public SpriteV2() {
      }

      public SpriteV2(String spriteId, String atlasPath, int x, int y, int width, int height) {
         this.spriteId = spriteId;
         this.atlasPath = atlasPath;
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
      }
   }
}
