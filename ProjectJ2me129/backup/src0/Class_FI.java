public final class Class_FI extends ChatDetail {
   private int AA;
   private int AB;

   public Class_FI(String var1, byte var2) {
      super(var1, (byte)5);
   }

   public final void AA(int var1, int var2, int var3, int var4, int var5, int var6) {
      super.AF = var1;
      super.AG = var2;
      super.AH = var3;
      super.AI = var4;
      super.AE = var5;
      super.AK = var6;
      this.AA(0, (byte)0);
   }

   public final void AA() {
      super.AM.AC = 0;
      super.AM.AB = 0;
      super.AL = 0;
      this.AF();
   }

   public final void paint(mGraphics var1) {
      if (super.AT != null) {
         super.AT.paint(var1);
      }

      var1.AD(super.AF - super.AE, super.AG - 2, super.AH + (super.AE << 1), super.AI - (super.AT != null ? super.AT.AD : -super.AE) + 2);
      mGraphics.AC();
      int var10000 = super.AF;
      var10000 = super.AE;
      var10000 = super.AG;
      var10000 = super.AH;
      var10000 = super.AE;
      var10000 = super.AI;
      if (super.AT != null) {
         var10000 = super.AT.AD;
      } else {
         var10000 = super.AE;
      }

      mGraphics.AD();
      var1.translate(0, -super.AM.AC);
      this.AA = super.AM.AC / GameCanvas.hText - 8;
      if (this.AA < 0) {
         this.AA = 0;
      }

      this.AB = this.AA + super.AK + 10;

      for(int var2 = this.AA; var2 <= this.AB; ++var2) {
         if (var2 < super.AN.size() && var2 >= 0) {
            mSystem var3;
            if ((var3 = (mSystem)super.AN.elementAt(var2)).AI > 0) {
               this.AA(var1, var3.AG, (byte)-1, 0, var3.AI * GameCanvas.hText - 3, super.AG + var2 * GameCanvas.hText, false);
               AvMain.FontBorderColor(var1, var3.AA, super.AF + super.AE + 2, super.AG + var2 * GameCanvas.hText + 1, 0, (int)var3.AE, (int)7);
            } else if (var3.AJ > -1L) {
               mFont.tahoma_7_black.drawString(var1, var3.AC, super.AF - super.AE + super.AH, super.AG + var2 * GameCanvas.hText - 2, 1);
            } else {
               mFont.tahoma_7b_black.drawString(var1, var3.AA, super.AF + super.AE + 2, super.AG + var2 * GameCanvas.hText, 0);
            }

            if (var3.AB.length() > 0) {
               mFont.tahoma_7_white.drawString(var1, var3.AB, super.AF + super.AH - super.AE, super.AG + var2 * GameCanvas.hText + 1, 1);
            }
         }
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
   }

   public final void update() {
      super.AM.AC();
      if (super.AT != null) {
         super.AT.update();
      }

      if (GameCanvas.gameTick % 500 == 0) {
         this.AF();
      }

   }

   private void AF() {
      for(int var1 = 0; var1 < super.AN.size(); ++var1) {
         mSystem var2;
         if ((var2 = (mSystem)super.AN.elementAt(var1)).AJ > 0L) {
            var2.AA();
         }
      }

   }

   public final void updatePointer() {
      super.AM.update_Pos_UP_DOWN();
      if (super.AT != null) {
         super.AT.updatePointer();
      }

   }

   public final void AA(short var1, String var2, String var3, String var4, byte var5, byte var6, short var7, long var8) {
      if (var2.length() > 0) {
         String var10 = var3 + "\n" + var2;
         String[] var11;
         mSystem[] var12;
         if ((var12 = ChatDetail.AA(var11 = mFont.tahoma_7b_white.splitFontArray(var10, GameCanvas.chatTabScr.AH - 12), (byte)0)) != null) {
            for(int var13 = 0; var13 < var12.length; ++var13) {
               if (var13 == 0) {
                  var12[0].AI = (byte)(var12.length + 1);
                  var12[0].AG = var5;
               }

               super.AN.addElement(var12[var13]);
            }

            mSystem var14;
            (var14 = new mSystem("", (byte)0)).AJ = var8;
            var14.AA();
            super.AN.addElement(var14);
         }

         this.AG();
         if (super.AV > 0) {
            this.AA(var11.length, (byte)1);
         }
      }

   }

   public final void handleKeyPress() {
      int var1 = super.AL;
      if (GameCanvas.AL[2]) {
         GameCanvas.AB(2);
         if (super.AL > 0) {
            --super.AL;
         }
      } else if (GameCanvas.AL[8]) {
         GameCanvas.AB(8);
         if (super.AL < super.AN.size() - 1) {
            ++super.AL;
         }
      }

      if (var1 != super.AL) {
         int var2;
         if ((var2 = super.AL * GameCanvas.hText) > super.AM.AD) {
            super.AL = super.AM.AD / GameCanvas.hText + 1;
         }

         super.AM.AA(var2);
      }

      super.handleKeyPress();
   }
}
