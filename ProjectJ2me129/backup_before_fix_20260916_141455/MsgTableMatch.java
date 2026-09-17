public final class MsgTableMatch extends MsgDialog {
   private mVector AA = new mVector("MsgTableMatch.vecMatch");

   public final void AB(mVector var1) {
      if (var1 != null && var1.size() != 0) {
         this.AA.removeAllElements();
         super.wDia = 190;
         super.hDia = 200;
         super.wItem = 80;
         if (super.wDia > MotherCanvas.w) {
            super.wDia = MotherCanvas.w;
         }

         if (super.hDia > MotherCanvas.h - 26) {
            super.hDia = GameCanvas.hCommand - 26;
         }

         if (var1.size() * super.wItem + 10 < super.hDia) {
            super.hDia = var1.size() * super.wItem + 10;
            super.AP = new ListNew(super.AX, super.AY, super.wDia, super.hDia - 16, 0, 0, 0, false);
         } else {
            super.AP = new ListNew(super.AX, super.AY, super.wDia, super.hDia - 16, 0, 0, var1.size() * super.wItem - (super.hDia - 15), false);
         }

         super.AX = MotherCanvas.w / 2 - super.wDia / 2;
         super.AY = MotherCanvas.h / 2 - super.hDia / 2 + 13;
         this.AA = var1;
         super.AG = new iCommand(T.close, -1, this);
         super.AG = AvMain.AA(super.AG, 2);
         if (GameCanvas.isKeyPressed()) {
            super.DB = super.AG;
            super.backCMD = super.AG;
         }

         if (AvMain.EV == null || AvMain.EU == null || AvMain.GX == null) {
            AvMain.EV = mImage.createImage("/interface/fightmatch.png");
            AvMain.EU = mImage.createImage("/interface/starmatch.png");
            if (GameCanvas.language == 1) {
               AvMain.GX = new FrameImage(mImage.createImage("/interface/match_e.png"), 93, 23);
               return;
            }

            AvMain.GX = new FrameImage(mImage.createImage("/interface/match.png"), 93, 23);
         }

      }
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         GameCanvas.end_Dialog();
      default:
      }
   }

   public final void paint(mGraphics var1) {
      this.AE(var1, super.AX, super.AY, super.wDia, super.hDia, super.hDia);
      var1.AD(super.AX + 10, super.AY + 8, super.wDia - 20, super.hDia - 16);
      mGraphics.AC();
      int var10000 = super.AX;
      var10000 = super.AY;
      var10000 = super.wDia;
      var10000 = super.hDia;
      mGraphics.AD();
      var1.translate(0, -super.AP.AC);
      int var2 = super.AY;
      int var3 = super.AX + 5;

      for(int var4 = 0; var4 < this.AA.size(); ++var4) {
         Class_DR var5 = (Class_DR)this.AA.elementAt(var4);
         AvMain.GX.drawFrame(var5.AB, var3 + super.wDia / 2, var2 + 20, 0, 3, var1);
         int var6 = var5.AA.length;
         int var7 = var2 + 20 + 30;
         if (var5.AB == 1) {
            var7 = var2 + 35;
         }

         var1.drawRegion((mImage)AvMain.EV, var3 + super.wDia / 2, var2 + super.wItem / 2 + 18, 3);

         int var8;
         for(var8 = 0; var8 < var6; ++var8) {
            if (var5.AB == 2) {
               if (var8 < var6 / 2) {
                  mFont.tahoma_7b_red.drawString(var1, T.Clan, var3 + super.wDia / 4, var7 - GameCanvas.hText / 2, 2);
                  mFont.tahoma_7b_red.drawString(var1, var5.AA[var8], var3 + super.wDia / 4, var7 + GameCanvas.hText / 2, 2);
               } else {
                  mFont.tahoma_7b_blue.drawString(var1, T.Clan, var3 + super.wDia / 4 * 3, var7 - GameCanvas.hText / 2, 2);
                  mFont.tahoma_7b_blue.drawString(var1, var5.AA[var8], var3 + super.wDia / 4 * 3, var7 + GameCanvas.hText / 2, 2);
               }
            } else if (var8 < var6 / 2) {
               if (var5.AC == 0) {
                  AvMain.FontBorderColor(var1, var5.AA[var8], var3 + super.wDia / 4, var7 + 30 * (var8 % (var6 / 2)), 2, (int)6, (int)7);
               } else {
                  mFont.tahoma_7b_red.drawString(var1, var5.AA[var8], var3 + super.wDia / 4, var7 + 30 * (var8 % (var6 / 2)), 2);
               }
            } else if (var5.AC == 1) {
               AvMain.FontBorderColor(var1, var5.AA[var8], var3 + super.wDia / 4 * 3, var7 + 30 * (var8 % (var6 / 2)), 2, (int)4, (int)7);
            } else {
               mFont.tahoma_7b_blue.drawString(var1, var5.AA[var8], var3 + super.wDia / 4 * 3, var7 + 30 * (var8 % (var6 / 2)), 2);
            }
         }

         if (var4 < this.AA.size() - 1) {
            for(var8 = 0; var8 < 3; ++var8) {
               if (var8 == 2) {
                  var1.drawRegion(AvMain.EU, 0, 0, 38, 5, 0, var3 + super.wDia / 2 - 60 + 80, var2 + super.wItem + 4, 0);
               } else {
                  var1.drawRegion((mImage)AvMain.EU, var3 + super.wDia / 2 - 60 + var8 * 40, var2 + super.wItem + 4, 0);
               }
            }
         }

         var2 += super.wItem;
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      if (!GameCanvas.isTouch && super.DB != null) {
         super.DB.paint(var1, super.DB.xCmd, super.DB.yCmd);
      }

   }

   public final void update() {
      super.AP.AC();
      this.updatePointer();
      this.handleKeyPress();
   }

   public final void handleKeyPress() {
      boolean var1 = false;
      if (GameCanvas.isKeyPressed(1)) {
         --super.idSelect;
         GameCanvas.clearKeyPressed(1);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         ++super.idSelect;
         GameCanvas.clearKeyPressed(3);
         var1 = true;
      }

      if (var1) {
         super.idSelect = AvMain.AA(super.idSelect, this.AA.size() - 1, false);
         if (GameCanvas.isKeyPressed()) {
            super.AP.AA((super.idSelect + 1) * super.wItem - (super.hDia - 16) / 2);
         }
      }

      super.AJ();
   }

   public final void updatePointer() {
      super.AP.update_Pos_UP_DOWN();
      if (GameCanvas.isPointerSelect && !GameCanvas.isPoint(super.AX, super.AY, super.wDia, super.hDia)) {
         GameCanvas.end_Dialog();
         GameCanvas.isPointerSelect = false;
      }

   }
}
