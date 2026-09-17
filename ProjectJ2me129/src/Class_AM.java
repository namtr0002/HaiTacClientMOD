public final class Class_AM extends MsgDialog {
   private Item_Drop[] AA;
   private int AB = 22;
   private MainItem AC;
   private static byte BB = 0;
   private static byte BC = 1;

   public final void AA(byte var1, String var2, Item_Drop[] var3, byte var4, String var5, MainItem var6) {
      super.type = var1;
      this.AA = var3;
      super.nameDialog = var2;
      this.AC = var6;
      super.wItem = 46;
      this.AB = 22;
      super.wDia = 212;
      if (super.wDia > MotherCanvas.w) {
         super.wDia = MotherCanvas.w;
      }

      if (var3.length < 4) {
         super.wDia = var3.length * 46 + 28;
      }

      super.hDia = ((var3.length - 1) / 4 + 2) * 46 + 5;
      if (super.hDia > 200) {
         super.hDia = 200;
      }

      super.AT = super.wDia;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.hh - super.hDia / 2 - 5;
      if (var3.length / 4 >= 3) {
         int var7 = super.hDia - this.AB - this.AB / 2 - 10;
         super.AP = new ListNew(super.AX, this.AB + 10, super.wDia, var7, 0, 0, ((var3.length - 1) / 4 + 1) * super.wItem - (var7 - 5), false);
      }

      iCommand var8 = new iCommand(var5, 14, 0, this);
      super.cmdList.addElement(var8);
      super.AG = new iCommand(T.close, 10, this);
      if (var1 == BC) {
         super.AG = new iCommand(T.close, -1, this);
      }

      super.cmdList.addElement(super.AG);
      if (GameCanvas.isKeyPressed()) {
         super.center = var8;
         super.DB = super.AG;
         super.AG = AvMain.AA(super.AG, 2);
      }

      if (GameCanvas.isTouch) {
         super.AG.setPos(super.AX + super.wDia - 20, super.AY + 19, MainTab.fraCloseTab, "");
      }

      AvMain.AA(var8, 0);
      super.AS = super.wDia;
      super.idSelect = -1;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 14:
         if (super.type == 0) {
            GlobalService.getInstance().AP((byte)var2);
            return;
         } else {
            if (super.type == BC) {
               if (super.idSelect >= 0 && super.idSelect <= this.AA.length - 1) {
                  String[] var4 = new String[]{this.AA[super.idSelect].name, this.AC.name};
                  mVector var5 = new mVector();
                  iCommand var3 = new iCommand(T.strconfirm, 16, this);
                  var5.addElement(var3);
                  GameCanvas.Start_Normal_DiaLog(GameMidlet.Start_Normal_DiaLog(T.TN, var4), var5, true);
                  return;
               }

               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.TO);
            }

            return;
         }
      case 15:
      default:
         super.commandPointer(var1, var2);
         return;
      case 16:
         if (this.AC != null) {
            GlobalService.getInstance().AB(this.AC.ID, this.AC.typeObject, (byte)super.idSelect);
         }

         GameCanvas.end_Dialog();
      }
   }

   public final void paint(mGraphics var1) {
      int var10002 = MotherCanvas.hw - super.AS / 2;
      int var10006 = super.AT;
      this.AD(var1, var10002, super.AY, super.AS, super.hDia, 0);
      GameCanvas.resetTrans(var1);
      int var2 = super.AY + this.AB + 10;
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.AX + 20, super.AY + 12, super.wDia - 40, 16, 4, 4);
      AvMain.FontBorderColor(var1, super.nameDialog, super.AX + super.wDia / 2, super.AY + 15, 2, (int)6, (int)5);
      AvMain.paintRect(var1, super.AX + 10, var2, super.wDia - 20, super.hDia - this.AB - this.AB / 2 - 10, (byte)0, 4);
      var1.AD(MotherCanvas.hw - super.AS / 2, var2 + 2, super.AS, super.hDia - this.AB - this.AB / 2 - 14);
      mGraphics.AC();
      int var10000 = MotherCanvas.hw;
      var10000 = super.AS;
      var10000 = super.AS;
      var10000 = super.hDia;
      var10000 = this.AB;
      var10000 = this.AB;
      mGraphics.AD();
      if (super.AP != null) {
         var1.translate(0, -super.AP.AC);
      }

      int var3;
      for(var3 = 0; var3 < this.AA.length; ++var3) {
         if (super.AP == null || var3 / 4 >= super.AP.AC / super.wItem && var3 / 4 < (super.AP.AC + super.wItem / 2) / super.wItem + 4) {
            int var4 = super.AX + 14 + super.wItem / 2 + var3 % 4 * super.wItem + 2;
            int var5 = var2 + super.wItem / 2 + var3 / 4 * super.wItem + 5;
            Item_Drop var6 = this.AA[var3];
            byte var7 = 1;
            int var8 = -8628420;
            if (var6.AA == 1 || super.type == BC && super.idSelect == var3) {
               var7 = 2;
               var8 = -14689466;
            }

            int var10001 = var4 - super.wItem / 2;
            var10002 = var5 - super.wItem / 2;
            int var10003 = super.wItem - 4;
            int var10004 = super.wItem - 4;
            byte var13 = var7;
            int var12 = var10004;
            int var11 = var10003;
            int var10 = var10002;
            int var9 = var10001;
            var11 += var11 % 2;
            var12 += var12 % 2;
            if (var7 >= AvMain.IG.length) {
               var13 = (byte)(AvMain.IG.length - 1);
            }

            var1.setColor(AvMain.IH[var13][2]);
            var1.fillRect(var9 + 2, var10 + 2, var11 - 4, var12 - 4);
            var1.drawRegion(AvMain.imgButton[0], 0, var13 * 20, 5, 5, 0, var9, var10, 0);
            var1.drawRegion(AvMain.imgButton[0], 0, var13 * 20 + 5, 5, 5, 0, var9 + var11, var10, 24);
            var1.drawRegion(AvMain.imgButton[0], 0, var13 * 20 + 10, 5, 5, 0, var9, var10 + var12, 36);
            var1.drawRegion(AvMain.imgButton[0], 0, var13 * 20 + 15, 5, 5, 0, var9 + var11, var10 + var12, 40);
            var1.setColor(AvMain.IH[var13][3]);
            var1.fillRect(var9 + 3, var10 + var12 / 2, var11 - 6, var12 / 2 - 4);
            var1.fillRect(var9 + 4, var10 + var12 - 4, var11 - 8, 1);
            var1.drawRegion(AvMain.imgButton[1], 0, var13 * 12, 15, 6, 0, var9 + 3, var10 + 3, 0);
            var1.drawRegion(AvMain.imgButton[1], 0, var13 * 12 + 6, 15, 6, 0, var9 + var11 - 3, var10 + var12 - 3, 40);
            var1.setColor(AvMain.IH[var13][4]);
            var1.fillRect(var9 + 3, var10, var11 - 6, 1);
            var1.fillRect(var9 + 3, var10 + var12 - 1, var11 - 6, 1);
            var1.fillRect(var9, var10 + 3, 1, var12 - 6);
            var1.fillRect(var9 + var11 - 1, var10 + 3, 1, var12 - 6);
            var1.setColor(AvMain.IH[var13][0]);
            var1.fillRect(var9 + 3, var10 + 1, var11 - 6, 1);
            var1.fillRect(var9 + 1, var10 + 3, 1, var12 - 6);
            var1.setColor(AvMain.IH[var13][1]);
            var1.fillRect(var9 + 3, var10 + var12 - 2, var11 - 6, 1);
            var1.fillRect(var9 + var11 - 2, var10 + 3, 1, var12 - 6);
            if (var6.IdIcon >= 0) {
               var6.AA(var1, var4 + 3, var5 - 8);
               if (var6.AA == 2) {
                  MsgDialog.fraAutoMpHp.drawFrame(4, var4 - 9, var5 - 8, 0, 3, var1);
               } else {
                  mFont.tahoma_7b_white.drawString(var1, String.valueOf(var6.num), var4 - 13, var5 - 12, 2);
               }

               var1.setColor(var8);
               var1.fillRoundRectNew(var4 - super.wItem / 2 + 2, var5 + super.wItem / 2 - 15, super.wItem - 8, 10, 4, 4);
               mFont.tahoma_7_white.drawString(var1, var6.name, var4 - 2, var5 + super.wItem / 2 - 16, 2);
            }
         }
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);

      for(var3 = 0; var3 < super.cmdList.size(); ++var3) {
         iCommand var14;
         (var14 = (iCommand)super.cmdList.elementAt(var3)).paint(var1, var14.xCmd, var14.yCmd);
      }

   }

   public final void update() {
      if (super.AP != null) {
         super.AP.AC();
      }

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
      if (super.AP != null) {
         if (GameCanvas.isKeyPressed(1)) {
            GameCanvas.clearKeyPressed(1);
            super.AP.AA(super.AP.AB - GameCanvas.hText);
         } else if (GameCanvas.isKeyPressed(3)) {
            GameCanvas.clearKeyPressed(3);
            super.AP.AA(super.AP.AB + GameCanvas.hText);
         }
      }

      if (super.type == BC) {
         if (GameCanvas.isKeyPressed(0)) {
            GameCanvas.clearKeyPressed(0);
            if (super.idSelect > 0) {
               --super.idSelect;
            }

            if (super.idSelect == -1) {
               super.idSelect = 0;
            }
         } else if (GameCanvas.isKeyPressed(2)) {
            GameCanvas.clearKeyPressed(2);
            if (super.idSelect < this.AA.length - 1) {
               ++super.idSelect;
            }
         }
      }

      this.AJ();
   }

   public final void updatePointer() {
      if (super.AP != null) {
         super.AP.update_Pos_UP_DOWN();
      }

      if (super.type == BC) {
         int var1 = super.AY + this.AB + 10;

         for(int var2 = 0; var2 < this.AA.length; ++var2) {
            int var3 = super.AX + 14 + var2 % 4 * super.wItem + 2;
            int var4 = var1 + var2 / 4 * super.wItem + 5;
            if (GameCanvas.AB(var3, var4, super.wItem, super.wItem)) {
               super.idSelect = var2;
            }
         }
      }

      super.updatePointer();
   }
}
