public final class Class_AS extends MsgDialog {
   private boolean AA;

   public final void AA() {
      super.fontDia = mFont.tahoma_7_black;
      this.beginDia();
      super.cmdList = new mVector();
      iCommand var1 = new iCommand(T.DA, 12, this);
      super.cmdList.addElement(var1);
      super.DF = var1;
      super.wDia = 100;
      super.AT = super.wDia;
      super.AS = 5;
      super.wItem = 28;
      super.hDia = 130;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.hh - super.hDia / 2 - 5;
      this.AA = mSound.AD;
      this.setPosCmdNew(-2, false);
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 12:
         byte[] var3;
         (var3 = new byte[2])[0] = (byte)(mSound.AD ? 1 : 0);
         var3[1] = (byte)(mSound.AE ? 1 : 0);
         CRes.saveRMS("MAIN_SOUND", var3);
         if (!mSound.AD) {
            mSound.AD();
         } else if (!this.AA) {
            mSound.AC = -1;
            LoadMapScreen.AF();
         }

         MsgDialog.AH();
      default:
      }
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = super.AY;
      int var3 = super.AX + 20;
      int var10002 = MotherCanvas.hw - super.AS / 2;
      int var10006 = super.AT;
      this.AD(var1, var10002, var2, super.AS, super.hDia, 0);
      var1.AD(MotherCanvas.hw - super.AS / 2, 0, super.AS, MotherCanvas.h);
      mGraphics.AC();
      int var10000 = MotherCanvas.hw;
      var10000 = super.AS;
      var10000 = super.AS;
      var10000 = MotherCanvas.h;
      mGraphics.AD();
      var2 += 12;
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.AX + 10, var2, super.wDia - 20, 16, 4, 4);
      var2 += 3;
      AvMain.FontBorderColor(var1, T.MB, super.AX + super.wDia / 2, var2, 2, (int)6, (int)5);
      var2 += super.wItem;
      if (GameCanvas.isKeyPressed()) {
         int var10003 = var2 - super.wItem / 4 - 1;
         int var8 = 58 + super.wItem;
         int var7 = var10003;
         var1.setColor(-2458);
         int var6 = var3 - super.wItem / 2;
         var1.fillRect(var6 + super.AU / 2, var7 + super.idSelect * super.wItem, var8 - super.AU / 2, super.wItem);
      }

      var1.drawRegion((mImage)AvMain.imgBorderCombo, var3, var2 + 5, 3);
      mFont.tahoma_7b_black.drawString(var1, T.MC, var3 + 12, var2, 0);
      if (mSound.AD) {
         AvMain.fraCheck.drawFrame(2, var3, var2 + 5, 0, 3, var1);
      }

      var2 += super.wItem;
      var1.drawRegion((mImage)AvMain.imgBorderCombo, var3, var2 + 5, 3);
      if (mSound.AE) {
         AvMain.fraCheck.drawFrame(2, var3, var2 + 5, 0, 3, var1);
      }

      mFont.tahoma_7b_black.drawString(var1, T.MD, var3 + 12, var2, 0);
      if (super.cmdList != null) {
         for(var2 = 0; var2 < super.cmdList.size(); ++var2) {
            iCommand var9;
            (var9 = (iCommand)super.cmdList.elementAt(var2)).paint(var1, var9.xCmd, var9.yCmd);
         }
      }

      mGraphics.restoreCanvas();
   }

   public final void update() {
      this.updateDialog();
      if (super.isClose) {
         this.closeDialog();
      } else {
         this.updateAnimation();
         if (GameCanvas.isKeyPressed()) {
            this.handleKeyPress();
         }

         this.updatePointer();
      }
   }

   public final void handleKeyPress() {
      if (GameCanvas.isKeyPressed(1)) {
         if (super.idSelect > 0) {
            --super.idSelect;
         }

         GameCanvas.clearKeyPressed(1);
      } else if (GameCanvas.isKeyPressed(3)) {
         if (super.idSelect <= 0) {
            ++super.idSelect;
         }

         GameCanvas.clearKeyPressed(3);
      } else if (!GameCanvas.isKeyPressed(0) && !GameCanvas.isKeyPressed(2)) {
         if (GameCanvas.AL[5]) {
            GameCanvas.AB(5);
            if (super.cmdList != null && super.AD < super.cmdList.size()) {
               ((iCommand)super.cmdList.elementAt(super.AD)).AD();
            }
         }
      } else {
         if (super.idSelect == 0) {
            mSound.AD = !mSound.AD;
         } else if (super.idSelect == 1) {
            mSound.AE = !mSound.AE;
         }

         GameCanvas.clearKeyPressed(0);
         GameCanvas.clearKeyPressed(2);
      }

      this.AS();
   }

   public final void updatePointer() {
      int var1 = super.AY;
      int var2 = super.AX;
      var1 += 15 + super.wItem - super.wItem / 4;
      if (GameCanvas.AB(var2 + 10, var1, 80, super.wItem)) {
         mSound.AD = !mSound.AD;
      } else if (GameCanvas.AB(var2 + 10, var1 + super.wItem, 80, super.wItem)) {
         mSound.AE = !mSound.AE;
      }

      super.updatePointer();
   }
}
