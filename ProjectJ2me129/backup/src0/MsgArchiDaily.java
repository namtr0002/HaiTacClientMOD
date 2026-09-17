public final class MsgArchiDaily extends MsgDialog {
   public static mVector vecArchi = new mVector("MsgArchiDaily.vecArchi");
   private iCommand AB;

   public final void AA(String var1, mVector var2) {
      vecArchi.removeAllElements();
      vecArchi = var2;
      super.wDia = 260;
      super.hDia = 180;
      if (super.wDia > MotherCanvas.w) {
         super.wDia = MotherCanvas.w;
      }

      if (super.hDia > MotherCanvas.h - 26) {
         super.hDia = GameCanvas.hCommand - 26;
      }

      super.AX = MotherCanvas.w / 2 - super.wDia / 2;
      super.AY = MotherCanvas.h / 2 - super.hDia / 2 + 13;
      super.wItem = 46;
      super.nameDialog = var1;
      super.AP = new ListNew(super.AX, super.AY + 38, super.wDia, super.hDia - 40, 0, 0, var2.size() * super.wItem - (super.hDia - 60), false);
      this.AB = new iCommand(T.PG, 13, this);
      this.AB = AvMain.AA(this.AB, 0);
      super.AG = new iCommand(T.close, -1, this);
      if (!GameCanvas.isTouch) {
         super.idSelect = 0;
         super.DB = super.AG;
         super.AG = AvMain.AA(super.AG, 2);
         this.AA();
      } else {
         if (GameCanvas.isKeyPressed()) {
            super.idSelect = 0;
         } else {
            super.idSelect = -1;
         }

         super.AG.setPos(super.AX + super.wDia / 2 + 72, super.AY - 14, MainTab.fraCloseTab, "");
         if (AvMain.mimgBgA == null) {
            iCommand var10000 = super.AG;
            var10000.yCmd += 19;
         }
      }

      super.backCMD = super.AG;
      super.cmdList.addElement(super.AG);
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         GameCanvas.end_Dialog();
      default:
         return;
      case 13:
         GlobalService.getInstance().AD((byte)((byte)super.idSelect), (byte)1);
      }
   }

   public final void paint(mGraphics var1) {
      AvMain.AB(var1, super.AX, super.AY, super.wDia, super.hDia, 0);
      if (AvMain.mimgBgA == null) {
         AvMain.FontBorderColor(var1, super.nameDialog, super.AX + super.wDia / 2, super.AY, 2, (int)0, (int)8);
      } else {
         AvMain.FontBorderColor(var1, super.nameDialog, super.AX + super.wDia / 2, super.AY - 19, 2, (int)0, (int)8);
      }

      int var2 = super.AX + 30;
      int var3 = super.AY + 38;
      int var4 = super.wDia - 60;
      mGraphics.AC();
      int var10000 = super.AX;
      var10000 = super.AY;
      var10000 = super.wDia;
      var10000 = super.hDia;
      mGraphics.AD();
      this.AE(var1);

      int var5;
      for(var5 = 0; var5 < vecArchi.size(); ++var5) {
         var1.setColor(-6124184);
         if (super.idSelect == var5) {
            var1.setColor(-4948685);
            var1.fillRect(var2 - 1, var3 + 5 - 1, var4 + 2, super.wItem - 10 + 2);
            var1.setColor(-2044008);
         }

         var1.fillRect(var2, var3 + 5, var4, super.wItem - 10);
         var1.drawRegion((mImage)AvMain.imgBorderIcon, var2 + 2 + 16, var3 + super.wItem / 2, 3);
         Class_DQ var6;
         short var7 = (var6 = (Class_DQ)vecArchi.elementAt(var5)).AF;
         if (var6.AG == Class_DQ.AH) {
            var7 = 19;
         }

         MainImage var9;
         if ((var9 = ObjectData.getImageAll(var7, ObjectData.hashImageItemOther, (short)9000)).img != null) {
            var1.drawRegion((mImage)var9.img, var2 + 2 + 16, var3 + super.wItem / 2, 3);
         }

         var1.setColor(-3561096);
         if (super.idSelect == var5) {
            var1.setColor(-595792);
         }

         var1.fillRect(var2 + 40, var3 + 5 + 4, var4 - 44, 12);
         if (var6.AG == Class_DQ.AH) {
            mFont.tahoma_7b_black.drawString(var1, T.PI, var2 + 42, var3 + 9, 0);
         } else {
            mFont.tahoma_7b_black.drawString(var1, var6.AA, var2 + 42, var3 + 9, 0);
            mFont.tahoma_7_black.drawString(var1, var6.AD + "/" + var6.AC, var2 + var4 - 6, var3 + 9, 1);
         }

         if (-super.AP.AC + var3 + 12 + 12 > super.AY + 38 - 2 && -super.AP.AC + var3 + 12 + 12 < super.AY + 38 - 2 + super.hDia - 70) {
            if (var6.AE > 0 && var6.AG == 0) {
               var1.AD(var2 + 40, var3 + 5 + 12, var4 - 44, 20);
               mFont.tahoma_7_black.drawString(var1, var6.AB, var2 + 55 - GameCanvas.gameTick % var6.AE, var3 + 12 + 12, 0);
               this.AE(var1);
            } else if (var6.AG == Class_DQ.AJ) {
               var1.drawRegion(AvMain.imgReward, 0, 0, 28, 15, 0, var2 + var4 - 20, var3 + 30, 3);
               mFont.tahoma_7_green.drawString(var1, T.PG, var2 + var4 - 30, var3 + 25, 1);
            } else if (var6.AG == Class_DQ.AK) {
               var1.drawRegion(AvMain.imgReward, 0, 15, 28, 15, 0, var2 + var4 - 20, var3 + 30, 3);
               mFont.tahoma_7_white.drawString(var1, T.PH, var2 + var4 - 30, var3 + 25, 1);
            } else if (var6.AG == Class_DQ.AH) {
               var1.drawRegion(AvMain.imgReward, 0, 30, 28, 15, 0, var2 + var4 - 20, var3 + 30, 3);
               mFont.tahoma_7_white.drawString(var1, T.strlock, var2 + var4 - 30, var3 + 25, 1);
            } else {
               mFont.tahoma_7_black.drawString(var1, var6.AB, var2 + 42, var3 + 12 + 12, 0);
            }
         }

         var3 += super.wItem;
         if (var5 < vecArchi.size() - 1) {
            var1.setColor(-2573687);
            var1.fillRect(var2 + 6, var3 - 1, var4 - 12, 2);
         }
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      if (super.cmdList != null) {
         for(var5 = 0; var5 < super.cmdList.size(); ++var5) {
            iCommand var8;
            (var8 = (iCommand)super.cmdList.elementAt(var5)).paint(var1, var8.xCmd, var8.yCmd);
         }
      }

   }

   private void AE(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      var1.AD(super.AX + 30 - 2, super.AY + 38 - 2, super.wDia - 60 + 4, super.hDia - 60);
      var1.translate(0, -super.AP.AC);
   }

   public final void update() {
      super.AP.AC();
      this.updatePointer();
      this.handleKeyPress();
      if (GameCanvas.isTouch && GameCanvas.AQ && super.idSelect != -1) {
         super.idSelect = -1;
      }

   }

   public final void updatePointer() {
      super.AP.update_Pos_UP_DOWN();
      if (GameCanvas.isPointerSelect && vecArchi.size() > 0) {
         int var1 = super.AX + 30;
         int var2 = super.AY + 38;
         int var3 = super.wDia - 60;
         int var4 = super.hDia - 60;
         if (GameCanvas.isPoint(var1, var2, var3, var4)) {
            if ((var1 = (GameCanvas.AZ - var2 + super.AP.AC) / super.wItem) >= 0 && var1 < vecArchi.size()) {
               if (((Class_DQ)vecArchi.elementAt(var1)).AG == Class_DQ.AJ) {
                  GlobalService.getInstance().AD((byte)((byte)var1), (byte)1);
               }

               super.idSelect = var1;
            }

            GameCanvas.isPointerSelect = false;
         }
      }

      super.updatePointer();
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
         super.idSelect = AvMain.AA(super.idSelect, vecArchi.size() - 1, false);
         this.AA();
         if (GameCanvas.isKeyPressed()) {
            super.AP.AA((super.idSelect + 1) * super.wItem - (super.hDia - 60) / 2);
         }
      }

      this.AS();
      if (!GameCanvas.isTouch) {
         this.AJ();
      }

   }

   private void AA() {
      super.cmdList.removeAllElements();
      if (super.idSelect >= 0 && super.idSelect < vecArchi.size()) {
         if (((Class_DQ)vecArchi.elementAt(super.idSelect)).AG == Class_DQ.AJ) {
            super.center = this.AB;
            super.DF = this.AB;
            super.cmdList.addElement(this.AB);
         } else {
            super.center = null;
            super.DF = null;
         }
      }

      super.cmdList.addElement(super.AG);
   }
}
