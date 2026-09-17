import javax.microedition.lcdui.Graphics;

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
            int iw = var1.image.getWidth();
            int ih = var1.image.getHeight();
            if (var2 >= 0 && var3 >= 0 && var4 > 0 && var5 > 0 && var2 + var4 <= iw && var3 + var5 <= ih) {
               this.g.drawRegion(var1.image, var2, var3, var4, var5, var6, var7, var8, var9);
            }
         } catch (Exception e) {}
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
      this.g.setClip(var1, var2, var3, var4);
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
        this.g.setClip(x, y, w, h);
    }
   
   public final void clearClip() {
        this.g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);
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
