public final class Class_AH extends MsgDialog {
   private int[][] AB;
   public static int[] AA = new int[3];
   private int AC;

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 6:
         Player.isGetItem = true;
         super.isClose = true;
         SaveRms var10000 = GameCanvas.saveRms;
         SaveRms.AH();
      default:
      }
   }

   public final void AA() {
      this.AB = new int[][]{{5, 7, 8, 6}, {5, 1, 2, 6}, {5, 6}};
      super.fontDia = mFont.tahoma_7_black;
      this.beginDia();
      super.cmdList = new mVector();
      iCommand var1 = new iCommand(T.DA, 6, this);
      super.cmdList.addElement(var1);
      super.DF = var1;
      super.wDia = MotherCanvas.w;
      if (super.wDia > 210) {
         super.wDia = 210;
      }

      if (super.wDia < 190) {
         super.wDia = 190;
      }

      super.AT = super.wDia;
      super.AS = 5;
      this.AC = 28;
      super.wItem = 26;
      if (super.wDia < 210) {
         super.wItem = 20;
      }

      super.hDia = 160;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.hh - super.hDia / 2 - 5;
      if (GameCanvas.isTouch) {
         super.AU = 28;
      }

      this.setPosCmdNew(-2, false);
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = super.AY;
      int var3 = super.AX + 15;
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
      AvMain.FontBorderColor(var1, T.setting, super.AX + super.wDia / 2, var2, 2, (int)6, (int)5);
      var2 += this.AC;
      int var6;
      if (GameCanvas.isKeyPressed()) {
         var10002 = var3 + 3;
         int var10003 = var2 - this.AC / 4 - 1;
         int var8 = super.wDia - 36;
         int var7 = var10003;
         var6 = var10002;
         var1.setColor(-2458);
         var6 -= super.AU;
         var1.fillRect(var6, var7 + super.idSelect * this.AC, var8 + (super.AU << 1), this.AC);
      }

      int var4;
      for(var4 = 0; var4 < this.AB.length; ++var4) {
         int var5 = var3 + 70;
         mFont.tahoma_7b_black.drawString(var1, T.WD[var4], var5 - 14, var2, 1);

         for(var6 = 0; var6 < this.AB[var4].length; ++var6) {
            if (var6 == AA[var4]) {
               var1.drawRegion((mImage)AvMain.imgHotKey, var5, var2 + 6, 3);
            }

            if (MsgDialog.fraAutoMpHp == null) {
               MsgDialog.fraAutoMpHp = new FrameImage(mImage.createImage("/interface/automphp.png"), 20, 20);
            } else {
               MsgDialog.fraAutoMpHp.drawFrame(this.AB[var4][var6], var5, var2 + 6, 0, 3, var1);
            }

            var5 += super.wItem;
         }

         var2 += this.AC;
      }

      this.paintInfoHelp(var1);
      if (super.cmdList != null) {
         for(var4 = 0; var4 < super.cmdList.size(); ++var4) {
            iCommand var9;
            (var9 = (iCommand)super.cmdList.elementAt(var4)).paint(var1, var9.xCmd, var9.yCmd);
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
         if (super.idSelect < 2) {
            ++super.idSelect;
         }

         GameCanvas.clearKeyPressed(3);
      } else if (GameCanvas.isKeyPressed(0)) {
         this.AD(-1);
         GameCanvas.clearKeyPressed(0);
      } else if (GameCanvas.isKeyPressed(2)) {
         this.AD(1);
         GameCanvas.clearKeyPressed(2);
      } else {
         if (GameCanvas.AL[5]) {
            GameCanvas.AB(5);
            if (super.cmdList != null && super.AD < super.cmdList.size()) {
               ((iCommand)super.cmdList.elementAt(super.AD)).AD();
            }
         }

      }
   }

   private void AD(int var1) {
      int[] var10000 = AA;
      int var10001 = super.idSelect;
      var10000[var10001] += var1;
      if (AA[super.idSelect] > this.AB[super.idSelect].length - 1) {
         AA[super.idSelect] = this.AB[super.idSelect].length - 1;
      }

      if (AA[super.idSelect] < 0) {
         AA[super.idSelect] = 0;
      }

      this.setInfoHelp(T.XC[super.idSelect][AA[super.idSelect]]);
   }

   public final void updatePointer() {
      int var1 = super.AY;
      int var2 = super.AX + 15;
      var1 += 15;
      var1 += this.AC;

      for(int var3 = 0; var3 < this.AB.length; ++var3) {
         int var4 = var2 + 70;

         for(int var5 = 0; var5 < this.AB[var3].length; ++var5) {
            if (GameCanvas.AB(var4 - super.wItem / 2, var1 + 6 - this.AC / 2, super.wItem, this.AC)) {
               AA[var3] = var5;
               GameCanvas.isPointerSelect = false;
               this.setInfoHelp(T.XC[var3][var5]);
               return;
            }

            var4 += super.wItem;
         }

         var1 += this.AC;
      }

      super.updatePointer();
   }
}
