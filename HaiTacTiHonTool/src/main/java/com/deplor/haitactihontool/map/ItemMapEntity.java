package com.deplor.haitactihontool.map;

public class ItemMapEntity {
   public short templateId;
   public int tileX;
   public int tileY;
   public transient ItemMapTemplate template;

   public ItemMapEntity(short templateId, int tileX, int tileY) {
      this.templateId = templateId;
      this.tileX = tileX;
      this.tileY = tileY;
   }
}
