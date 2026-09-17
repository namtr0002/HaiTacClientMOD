public final class Class_AI extends MsgDialog {
   public static int AA = 30;
   public static int AB = 30;
   public static int AC = 0;

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 4:
         Player.isMPHP = true;
         super.isClose = true;
         GameScreen.player.SI = null;
         GameScreen.player.SJ = null;
         SaveRms var10000 = GameCanvas.saveRms;
         SaveRms.AF();
      default:
      }
   }

   public final void AA() {
      super.fontDia = mFont.tahoma_7_black;
      this.beginDia();
      super.cmdList = new mVector();
      iCommand var1 = new iCommand(T.DA, 4, this);
      super.cmdList.addElement(var1);
      super.DF = var1;
      super.wDia = 160;
      super.AT = super.wDia;
      super.AS = 5;
      super.wItem = 28;
      super.hDia = 160;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.hh - super.hDia / 2 - 5;
      this.setPosCmdNew(-2, false);
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = super.AY;
      int var3 = super.AX + 30;
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
      var2 += super.wItem;
      if (GameCanvas.isKeyPressed()) {
         int var10003 = var2 - super.wItem / 4 - 1;
         int var8 = 100 + super.wItem;
         int var7 = var10003;
         var1.setColor(-2458);
         int var6 = var3 - super.wItem / 2;
         var1.fillRect(var6 + super.AU / 2, var7 + super.idSelect * super.wItem, var8 - super.AU / 2, super.wItem);
      }

      var1.drawRegion(Interface_Game.imgIconMPHP, 0, 0, 10, 10, 0, var3 + 4, var2 + 6, 3);
      Interface_Game.AA(var1, (byte)1, AB * 10, 100, var3 + 10, var2, 0, 12, 90, 1, false, 0, false, 0);
      var2 += super.wItem;
      var1.drawRegion(Interface_Game.imgIconMPHP, 0, 10, 10, 10, 0, var3 + 4, var2 + 6, 3);
      Interface_Game.AA(var1, (byte)2, AA * 10, 100, var3 + 10, var2, 0, 12, 90, 1, false, 0, false, 0);
      var2 += super.wItem;
      mFont.tahoma_7b_black.drawString(var1, T.DC, var3, var2, 0);
      var1.drawRegion((mImage)AvMain.imgHotKey, var3 + 55 + 30 * AC, var2 + 6, 3);
      if (MsgDialog.fraAutoMpHp == null) {
         MsgDialog.fraAutoMpHp = new FrameImage(mImage.createImage("/interface/automphp.png"), 20, 20);
      } else {
         MsgDialog.fraAutoMpHp.drawFrame(0, var3 + 55, var2 + 6, 0, 3, var1);
         MsgDialog.fraAutoMpHp.drawFrame(1, var3 + 85, var2 + 6, 0, 3, var1);
      }

      this.paintInfoHelp(var1);
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
      } else if (GameCanvas.AL[5]) {
         GameCanvas.AB(5);
         if (super.cmdList != null && super.AD < super.cmdList.size()) {
            ((iCommand)super.cmdList.elementAt(super.AD)).AD();
         }
      }

      this.AS();
   }

   private void AD(int var1) {
      if (super.idSelect == 0) {
         if ((AB += var1 * 10) > 90) {
            AB = 90;
         }

         if (AB < 0) {
            AB = 0;
         }

         this.setInfoHelp(T.mHelpAutoMPHP[0] + AB + "%");
      } else if (super.idSelect == 1) {
         if ((AA += var1 * 10) > 90) {
            AA = 90;
         }

         if (AA < 0) {
            AA = 0;
         }

         this.setInfoHelp(T.mHelpAutoMPHP[1] + AA + "%");
      } else {
         if (super.idSelect == 2) {
            if (++AC > 1) {
               AC = 0;
            }

            this.setInfoHelp(T.mHelpAutoMPHP[2 + AC]);
         }

      }
   }

   public final void updatePointer() {
      int var1 = super.AY;
      int var2 = super.AX + 30;
      var1 += 15 + super.wItem - super.wItem / 4;
      if (GameCanvas.AD(var2 + 10, var1, 90, super.wItem)) {
         if (GameCanvas.isPointerDown || GameCanvas.AQ) {
            AB = AvMain.AA((GameCanvas.AY - (var2 + 10) + 5) / 9 * 10, 90, false);
            this.setInfoHelp(T.mHelpAutoMPHP[0] + AB + "%");
         }
      } else if (GameCanvas.AD(var2 + 10, var1 + super.wItem, 90, super.wItem)) {
         if (GameCanvas.isPointerDown || GameCanvas.AQ) {
            AA = AvMain.AA((GameCanvas.AY - (var2 + 10) + 5) / 9 * 10, 90, false);
            this.setInfoHelp(T.mHelpAutoMPHP[1] + AA + "%");
         }
      } else if (GameCanvas.isPoint(var2 + 55 - 15, var1 + (super.wItem << 1), 60, super.wItem) && GameCanvas.isPointerSelect) {
         if (GameCanvas.AY < var2 + 55 + 15) {
            AC = 0;
         } else {
            AC = 1;
         }

         GameCanvas.isPointerSelect = false;
         this.setInfoHelp(T.mHelpAutoMPHP[2 + AC]);
      }

      super.updatePointer();
   }
}
