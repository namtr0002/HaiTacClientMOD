package com.deplor.haitactihontool.map;

public class ItemMapTemplate {
   public short idItem;
   public short idImage;
   public byte layer;
   public short dx;
   public short dy;
   public int[][] block;
   public transient String[] tags;

   public ItemMapTemplate(short idItem, short idImage, byte layer, short dx, short dy, int[][] block) {
      this.idItem = idItem;
      this.idImage = idImage;
      this.layer = layer;
      this.dx = dx;
      this.dy = dy;
      this.block = block;
   }
}
