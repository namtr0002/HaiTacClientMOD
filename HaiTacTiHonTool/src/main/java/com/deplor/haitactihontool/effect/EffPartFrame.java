package com.deplor.haitactihontool.effect;

public class EffPartFrame {
   public int dx;
   public int dy;
   public int idSmallImg;
   public int flip;
   public int onTop;
   public int rotate = 0;

   public EffPartFrame() {
      this(0, 0, 0, 0, 0, 0);
   }

   public EffPartFrame(int dx, int dy, int idSmallImg, int flip, int onTop) {
      this(dx, dy, idSmallImg, flip, onTop, 0);
   }

   public EffPartFrame(int dx, int dy, int idSmallImg, int flip, int onTop, int rotate) {
      this.dx = dx;
      this.dy = dy;
      this.idSmallImg = idSmallImg;
      this.flip = flip;
      this.onTop = onTop;
      this.rotate = rotate;
   }

   public EffPartFrame deepClone() {
      return new EffPartFrame(this.dx, this.dy, this.idSmallImg, this.flip, this.onTop, this.rotate);
   }

   @Override
   public String toString() {
      return String.format("Part[img=%d dx=%d dy=%d flip=%d top=%d rot=%d]", this.idSmallImg, this.dx, this.dy, this.flip, this.onTop, this.rotate);
   }
}
