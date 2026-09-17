package com.deplor.haitactihontool.effect;

import java.awt.image.BufferedImage;

public class SmallImageDef {
   public final int id;
   public int x;
   public int y;
   public int w;
   public int h;

   public SmallImageDef(int id, int x, int y, int w, int h) {
      this.id = id;
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
   }

   public BufferedImage crop(BufferedImage atlas) {
      if (atlas == null) {
         return null;
      } else {
         int cx = this.x * 4;
         int cy = this.y * 4;
         int cw = this.w * 4;
         int ch = this.h * 4;
         if (cx >= atlas.getWidth()) {
            cx = 0;
         }

         if (cy >= atlas.getHeight()) {
            cy = 0;
         }

         if (cx + cw > atlas.getWidth()) {
            cw = atlas.getWidth() - cx;
         }

         if (cy + ch > atlas.getHeight()) {
            ch = atlas.getHeight() - cy;
         }

         return cw > 0 && ch > 0 ? atlas.getSubimage(cx, cy, cw, ch) : null;
      }
   }

   @Override
   public String toString() {
      return String.format("SmallImg#%d [x=%d y=%d w=%d h=%d]", this.id, this.x, this.y, this.w, this.h);
   }
}
