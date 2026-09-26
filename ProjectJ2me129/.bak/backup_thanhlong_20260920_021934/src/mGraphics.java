import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class mGraphics {
   public Graphics g;
   public static int zoomLevel = 1;
   private int AC;
   private int AD;
   private int AE;
   private int AF;
   private int AG;
   private int AH;
   private boolean AI = false;
   private int AJ;
   private int AK;

   public final void drawImage(mImage var1, int var2, int var3, int var4) {
      if (var1 != null && var1.image != null) {
         this.g.drawImage(var1.image, var2, var3, var4);
      }
   }

   public final void drawRegion(mImage var1, int var2, int var3, int var4) {
      if (var1 != null && var1.image != null) {
         this.g.drawImage(var1.image, var2, var3, var4);
      }
   }

   public final void drawLine(int var1, int var2, int var3, int var4) {
      this.g.drawLine(var1, var2, var3, var4);
   }

   public final void drawRect(int var1, int var2, int var3, int var4) {
      this.g.drawRect(var1, var2, var3, var4);
   }

   public final void drawRegion(mImage var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      if (var1 != null && var1.image != null && this.g != null) {
         try {
            int iw = var1.getRawWidth();
            int ih = var1.getRawHeight();
            if (var2 >= 0 && var3 >= 0 && var4 > 0 && var5 > 0 && var2 + var4 <= iw && var3 + var5 <= ih) {
               this.g.drawRegion(var1.image, var2, var3, var4, var5, var6, var7, var8, var9);
            }
         } catch (Exception e) {}
      }
   }

   private static MyHashTable CACHE_ROTATED = new MyHashTable();

   public static void clearRotatedCache() {
      if (CACHE_ROTATED != null) {
         CACHE_ROTATED.clear();
      }
   }

   public final void drawRegion(mImage var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      int angle = var10 % 360;
      if (angle < 0) {
         angle += 360;
      }
      if (angle == 0) {
         this.drawRegion(var1, var2, var3, var4, var5, var6, var7, var8, var9);
         return;
      }

      if (var1 == null || var1.image == null || this.g == null) {
         return;
      }

      try {
         int iw = var1.image.getWidth();
         int ih = var1.image.getHeight();
         if (var2 < 0 || var3 < 0 || var4 <= 0 || var5 <= 0 || var2 + var4 > iw || var3 + var5 > ih) {
            return;
         }

         String key = var1.hashCode() + "_" + var2 + "_" + var3 + "_" + var4 + "_" + var5 + "_" + var6 + "_" + angle;
         RotatedPart rot = (RotatedPart)CACHE_ROTATED.get(key);
         if (rot == null) {
            rot = createRotatedPart(var1.image, var2, var3, var4, var5, var6, angle);
            if (rot != null) {
               if (CACHE_ROTATED.size() > 400) {
                  CACHE_ROTATED.clear();
               }
               CACHE_ROTATED.put(key, rot);
            }
         }

         if (rot != null && rot.image != null) {
            int rx = var7;
            int ry = var8;
            if ((var9 & 1) != 0) {
               rx -= var4 / 2;
            } else if ((var9 & 8) != 0) {
               rx -= var4;
            }
            if ((var9 & 2) != 0) {
               ry -= var5 / 2;
            } else if ((var9 & 32) != 0) {
               ry -= var5;
            }
            this.g.drawImage(rot.image, rx + rot.dx, ry + rot.dy, 0);
         } else {
            this.drawRegion(var1, var2, var3, var4, var5, var6, var7, var8, var9);
         }
      } catch (Exception e) {
         this.drawRegion(var1, var2, var3, var4, var5, var6, var7, var8, var9);
      }
   }

   private static RotatedPart createRotatedPart(Image srcImg, int x0, int y0, int w, int h, int transform, int angle) {
      try {
         int[] srcPixels = new int[w * h];
         srcImg.getRGB(srcPixels, 0, w, x0, y0, w, h);

         if (transform == 2) {
            for (int y = 0; y < h; y++) {
               int row = y * w;
               for (int x = 0; x < w / 2; x++) {
                  int t = srcPixels[row + x];
                  srcPixels[row + x] = srcPixels[row + w - 1 - x];
                  srcPixels[row + w - 1 - x] = t;
               }
            }
         }

         int effAngle = (transform == 2) ? -angle : angle;
         double rad = (double)effAngle * (3.141592653589793 / 180.0);
         double cos = Math.cos(rad);
         double sin = Math.sin(rad);

         int newW = (int)Math.ceil(Math.abs((double)w * cos) + Math.abs((double)h * sin));
         int newH = (int)Math.ceil(Math.abs((double)w * sin) + Math.abs((double)h * cos));
         if (newW < 1) newW = 1;
         if (newH < 1) newH = 1;

         int[] dstPixels = new int[newW * newH];

         double cxDst = (double)newW / 2.0;
         double cyDst = (double)newH / 2.0;
         double cxSrc = (double)w / 2.0;
         double cySrc = (double)h / 2.0;

         for (int y = 0; y < newH; y++) {
            double dy = ((double)y + 0.5) - cyDst;
            int rowIdx = y * newW;
            for (int x = 0; x < newW; x++) {
               double dx = ((double)x + 0.5) - cxDst;
               double srcX = cxSrc + (dx * cos + dy * sin) - 0.5;
               double srcY = cySrc + (-dx * sin + dy * cos) - 0.5;

               int ix = (int)Math.round(srcX);
               int iy = (int)Math.round(srcY);

               if (ix >= 0 && ix < w && iy >= 0 && iy < h) {
                  dstPixels[rowIdx + x] = srcPixels[iy * w + ix];
               }
            }
         }

         Image rotImg = Image.createRGBImage(dstPixels, newW, newH, true);
         int offX = (w - newW) / 2;
         int offY = (h - newH) / 2;
         return new RotatedPart(rotImg, offX, offY);
      } catch (Throwable t) {
         return null;
      }
   }

   public final void setClip(mImage var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10) {
      if (var10) {
         this.g.setClip(this.AC, this.AD, this.AE, this.AF);
      }

      this.g.drawRegion(var1.image, var2, var3, var4, var5, 0, var7, var8, 20);
   }

   public final void drawString(String var1, int var2, int var3, int var4) {
      this.g.drawString(var1, var2, var3, 2);
   }

   public final void fillRect(int var1, int var2, int var3, int var4) {
      this.g.fillRect(var1, var2, var3, var4);
   }

   public final void fillRoundRect1(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.g.fillRoundRect(var1, var2, var3, var4, 4, 4);
   }
   
    public final void fillRoundRectNew(int x, int y, int w, int h, int arcWidth, int arcHeight) {
        this.g.fillRect(x, y, w, h);
    }

    public final void drawRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight) {
        this.g.drawRect(x, y, w, h);
    }


   public final int getTranslateX() {
      return this.g.getTranslateX() / zoomLevel;
   }

   public final int getTranslateY() {
      return this.g.getTranslateY() / zoomLevel;
   }
//setClip
   public final void setClip_(int var1, int var2, int var3, int var4) {
      if (var3 < 0) var3 = 0;
      if (var4 < 0) var4 = 0;
      var1 *= zoomLevel;
      var2 *= zoomLevel;
      var3 *= zoomLevel;
      var4 *= zoomLevel;
      this.AG = var1;
      this.AH = var2;
      if (this.AI) {
         this.AG -= this.AJ;
         this.AH -= this.AK;
      }

      this.AC = this.AG;
      this.AD = this.AH;
      this.AE = var3;
      this.AF = var4;
      if (this.g != null) {
         this.g.setClip(var1, var2, var3, var4);
      }
   }

   public final void AD(int var1, int var2, int var3, int var4) {
      this.setClip_(var1, var2, var3, var4);
   }

   public final void setColor(int var1) {
      this.g.setColor(var1);
   }

   public final void translate(int var1, int var2) {
      var1 *= zoomLevel;
      var2 *= zoomLevel;
      this.AJ = var1;
      this.AK = var2;
      this.AI = true;
      if (this.AJ == 0 && this.AK == 0) {
         this.AI = false;
      }

      this.g.translate(var1, var2);
   }

   public static void AC() {
   }

   public final void fillRecAlpla(int var1, int var2, int var3, int var4, int var5) {
      this.drawRecAlpa(0, 0, GameCanvas.loadmap.mapW * 24, var2, var5);
      this.drawRecAlpa(0, var2, var1, GameCanvas.loadmap.mapH * 24 - var2, var5);
      this.drawRecAlpa(var1, var2 + var4, GameCanvas.loadmap.mapW * 24 - var1, GameCanvas.loadmap.mapH * 24 - (var2 + var4), var5);
      this.drawRecAlpa(var1 + var3, var2, GameCanvas.loadmap.mapW * 24 - (var1 + var3), var4, var5);
      this.drawRecAlpa(0, -100, GameCanvas.loadmap.mapW * 24, 100, var5);
   }

   public final void drawRecAlpa(int var1, int var2, int var3, int var4, int var5) {
      this.setColor(var5);
      this.fillRect(var1, var2, var3, var4);
   }

   public static void AD() {
   }

   public static void AE() {
   }

   public static void restoreCanvas() {
   }
   
   public static void setZoomLevel(int zl) {
        if (zl < 1 || zl > 4) {
            zoomLevel = 1;
        } else {
            zoomLevel = zl;
        }
        MotherCanvas.w = (MotherCanvas.w * 1) / zoomLevel;
        MotherCanvas.h = (MotherCanvas.h * 1) / zoomLevel;
        MotherCanvas.hw = MotherCanvas.w / 2;
        MotherCanvas.hh = MotherCanvas.h / 2;
    }
   
   public final void setClip(int x, int y, int w, int h) {
        if (w < 0) w = 0;
        if (h < 0) h = 0;
        if (this.g != null) {
            this.g.setClip(x, y, w, h);
        }
    }
   
   public final void clearClip() {
        this.clipStackTop = 0;
        if (this.g != null) {
            this.g.setClip(0, 0, MotherCanvas.w * zoomLevel, MotherCanvas.h * zoomLevel);
        }
    }

   public final void resetClipStack() {
        this.clipStackTop = 0;
   }

   private int[] clipStack = new int[128];
   private int clipStackTop = 0;

   public final void pushClip() {
      if (this.g != null) {
         if (this.clipStackTop + 4 <= this.clipStack.length) {
            this.clipStack[this.clipStackTop++] = this.g.getClipX();
            this.clipStack[this.clipStackTop++] = this.g.getClipY();
            this.clipStack[this.clipStackTop++] = this.g.getClipWidth();
            this.clipStack[this.clipStackTop++] = this.g.getClipHeight();
         }
      }
   }

   public final void popClip() {
      if (this.g != null && this.clipStackTop >= 4) {
         int ch = this.clipStack[--this.clipStackTop];
         int cw = this.clipStack[--this.clipStackTop];
         int cy = this.clipStack[--this.clipStackTop];
         int cx = this.clipStack[--this.clipStackTop];
         if (cw < 0) cw = 0;
         if (ch < 0) ch = 0;
         this.g.setClip(cx, cy, cw, ch);
      }
   }

   public final void clipRectIntersect(int x, int y, int w, int h) {
      if (this.g == null) return;
      int curX = this.g.getClipX();
      int curY = this.g.getClipY();
      int curW = this.g.getClipWidth();
      int curH = this.g.getClipHeight();

      int newX = Math.max(x, curX);
      int newY = Math.max(y, curY);
      int newRight = Math.min(x + Math.max(0, w), curX + Math.max(0, curW));
      int newBottom = Math.min(y + Math.max(0, h), curY + Math.max(0, curH));
      int newW = newRight - newX;
      int newH = newBottom - newY;

      if (newW > 0 && newH > 0) {
         this.g.setClip(newX, newY, newW, newH);
      } else {
         this.g.setClip(0, 0, 0, 0);
      }
   }

   public static class ClipState {
      public int clipX;
      public int clipY;
      public int clipW;
      public int clipH;
      public int AC;
      public int AD;
      public int AE;
      public int AF;
      public int AG;
      public int AH;
   }

   public final ClipState getClipState() {
      ClipState state = new ClipState();
      state.clipX = this.g.getClipX();
      state.clipY = this.g.getClipY();
      state.clipW = this.g.getClipWidth();
      state.clipH = this.g.getClipHeight();
      state.AC = this.AC;
      state.AD = this.AD;
      state.AE = this.AE;
      state.AF = this.AF;
      state.AG = this.AG;
      state.AH = this.AH;
      return state;
   }

   public final void setClipState(ClipState state) {
      if (state != null) {
         this.g.setClip(state.clipX, state.clipY, state.clipW, state.clipH);
         this.AC = state.AC;
         this.AD = state.AD;
         this.AE = state.AE;
         this.AF = state.AF;
         this.AG = state.AG;
         this.AH = state.AH;
      }
   }

}
