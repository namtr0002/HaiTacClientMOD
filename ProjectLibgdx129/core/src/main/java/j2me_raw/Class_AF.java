public final class Class_AF extends MsgDialog {
   private byte[] AA;
   private int AB = 0;
   private int AC;
   private int BB;
   private int BC;
   private iCommand BD;
   private byte BE = 0;
   private int BF = 0;

   public final void AA(byte[] var1, byte var2) {
      this.AA = var1;
      this.BE = var2;
      super.nameDialog = T.EU;
      if (var2 == 1) {
         super.nameDialog = T.PL;
      }

      super.fontDia = mFont.tahoma_7_black;
      this.beginDia();
      super.AS = 5;
      super.wItem = 30;
      this.AC = 30;
      this.BB = 24;
      this.BC = 120;
      this.AB = 5;
      if (this.AB > var1.length) {
         this.AB = var1.length;
      }

      super.AP = new ListNew(super.AX, super.AY, super.wDia, super.hDia, 0, 0, 0, true);
      super.wDia = this.AB * super.wItem;
      if (super.wDia < 120) {
         super.wDia = 120;
      }

      if (this.BC > super.wDia - 20) {
         this.BC = super.wDia - 20;
      }

      super.AT = super.wDia;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.hDia = ((var1.length - 1) / this.AB + 1) * this.AC;
      this.BD = new iCommand(T.AS, 0, this);
      super.AG = new iCommand(T.close, 1, this);
      if (super.hDia > 140) {
         int var3 = super.hDia;
         super.hDia = 140;
         super.AY = MotherCanvas.hh - super.hDia / 2 - 5;
         super.AP = new ListNew(super.AX, super.AY, super.wDia, 140, 0, 0, var3 - 140, true);
      } else {
         super.AY = MotherCanvas.hh - super.hDia / 2 - 5;
      }

      if (!GameCanvas.isTouch) {
         super.DB = super.AG;
         super.DA = this.BD;
      } else {
         if (GameCanvas.isKeyPressed()) {
            super.idSelect = 0;
         } else {
            super.idSelect = -1;
         }

         super.AG.setPos(super.AX + super.AT / 2 + this.BC / 2, super.AY - 20 + 8, MainTab.fraCloseTab, "");
      }

      super.backCMD = super.AG;
      super.DF = this.BD;
      GameCanvas.isPointerSelect = false;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (super.idSelect >= 0 && super.idSelect < this.AA.length) {
            if (this.BE == 0) {
               GlobalService.getInstance().AB((byte)0, (byte)((byte)super.idSelect));
            } else if (this.BE == 1) {
               byte var3 = -1;
               if (GameCanvas.currentScreen == GameCanvas.CJ) {
                  var3 = ((MainTab)GameCanvas.CJ.AB.elementAt(GameCanvas.CJ.AE)).AU;
               }

               GlobalService.getInstance().AC((byte)1, var3, (short)super.idSelect, (byte)0, (short)1);
            }
         }

         GameCanvas.end_Dialog();
         break;
      case 1:
         GameCanvas.end_Dialog();
      }

      super.commandPointer(var1, var2);
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = super.AY + 4;
      int var3 = super.AX + 4;
      int var10002 = super.AX - 5;
      int var10003 = super.AY - 32;
      int var10004 = super.AT + 10;
      int var10005 = super.hDia + 44;
      int var10006 = super.AT;
      this.AD(var1, var10002, var10003, var10004, var10005, 0);
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.AX + super.AT / 2 - this.BC / 2, super.AY - 20, this.BC, 16, 4, 4);
      AvMain.FontBorderColor(var1, super.nameDialog, super.AX + super.AT / 2, super.AY - 18, 2, (int)6, (int)5);
      var1.AD(MotherCanvas.hw - super.AT / 2, super.AY, super.AT, super.hDia);
      mGraphics.AC();
      int var10000 = MotherCanvas.hw;
      var10000 = super.AT;
      var10000 = super.AY;
      var10000 = super.AT;
      var10000 = super.hDia;
      mGraphics.AD();
      var1.translate(0, -super.AP.AC);

      for(int var4 = 0; var4 < this.AA.length; ++var4) {
         AvMain.AA(var1, var3 + var4 % this.AB * super.wItem, var2 + var4 / this.AB * this.AC, this.BB, this.BB, super.idSelect == var4 ? 1 : 0);
         byte var5 = 1;
         if (this.AA[var4] == 1) {
            var5 = 3;
         } else if (this.AA[var4] == 2) {
            var5 = 6;
         } else if (this.AA[var4] == 3) {
            var5 = 2;
         }

         AvMain.FontBorderColor(var1, "" + (var4 + 1), var3 + var4 % this.AB * super.wItem + this.BB / 2, var2 + var4 / this.AB * this.AC + GameCanvas.hText / 2, 2, (int)var5, (int)7);
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      this.AD(var1);
      if (GameCanvas.isTouch) {
         super.AG.paint(var1, super.AG.xCmd, super.AG.yCmd);
      }

   }

   public final void update() {
      super.AP.AC();
      this.handleKeyPress();
      this.updatePointer();
      if (this.BF > 0) {
         --this.BF;
         if (GameCanvas.isTouch && this.BF == 0) {
            super.idSelect = -1;
         }
      }

   }

   public final void handleKeyPress() {
      boolean var1 = false;
      if (GameCanvas.isKeyPressed(2)) {
         ++super.idSelect;
         GameCanvas.clearKeyPressed(2);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(0)) {
         --super.idSelect;
         GameCanvas.clearKeyPressed(0);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         super.idSelect += this.AB;
         GameCanvas.clearKeyPressed(3);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(1)) {
         super.idSelect -= this.AB;
         GameCanvas.clearKeyPressed(1);
         var1 = true;
      } else if (GameCanvas.AL[5]) {
         this.BD.AD();
         GameCanvas.AB(5);
      }

      if (var1) {
         super.idSelect = AvMain.AA(super.idSelect, this.AA.length - 1, true);
         super.AP.AA(super.idSelect / 3 * this.AC - super.hDia / 2);
      }

      super.handleKeyPress();
   }

   public final void updatePointer() {
      super.AG.AE();
      int var1;
      if (GameCanvas.isPointerDown && GameCanvas.isPoint(super.AX, super.AY, super.wDia, super.hDia) && (var1 = (GameCanvas.AY - super.AX) / super.wItem + (GameCanvas.AZ - super.AY + super.AP.AC) / this.AC * this.AB) >= 0 && var1 < this.AA.length) {
         super.idSelect = var1;
         this.BF = 10;
      }

      if (GameCanvas.AQ) {
         this.BF = 0;
         super.idSelect = -1;
      }

      if (GameCanvas.AB(super.AX, super.AY, super.wDia, super.hDia)) {
         if ((var1 = (GameCanvas.AY - super.AX) / super.wItem + (GameCanvas.AZ - super.AY + super.AP.AC) / this.AC * this.AB) >= 0 && var1 < this.AA.length) {
            this.BD.AD();
         }

         GameCanvas.isPointerSelect = false;
      }

      super.AP.update_Pos_UP_DOWN();
      super.updatePointer();
   }
}
