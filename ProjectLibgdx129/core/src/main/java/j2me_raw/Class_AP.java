public final class Class_AP extends MsgDialog {
   private mVector AA;
   private MainItem AB;
   private mVector AC;
   private int BB;
   private int BC;
   private int BD = 100;
   private int BE = 40;
   private int BF;

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 3:
         MainItem var3;
         if ((var3 = (MainItem)this.AA.elementAt(super.idSelect)) != null) {
            GlobalService.getInstance().AC(var3.ID, var3.typeObject);
            GameCanvas.end_Dialog();
         }

         return;
      default:
         super.commandPointer(var1, var2);
      }
   }

   public final void AA(mVector var1, int var2, int var3, int var4, int var5) {
      this.BF = var5;
      this.AA = var1;
      if (this.AA != null && this.AA.size() != 0) {
         super.AG = new iCommand(T.close, -1, this);
         super.AG = AvMain.AA(super.AG, 2);
         super.AH = new iCommand(T.tabEquip, 3, this);
         super.AH = AvMain.AA(super.AH, 0);
         super.wDia = var4;
         super.hDia = this.AA.size() * var4;
         if (super.hDia > var4 * 5) {
            super.hDia = var4 * 5;
         }

         super.AX = var2 + var4 + 2;
         if (var5 == 0) {
            super.AX = var2 - var4 - 4;
         }

         super.AY = var3 - super.hDia / 2;
         if (super.AY < 2) {
            super.AY = 2;
         }

         if (super.AY + super.hDia + 2 > MotherCanvas.h - GameCanvas.hCommand) {
            super.AY = MotherCanvas.h - super.hDia - 2 - GameCanvas.hCommand;
         }

         super.AP = new ListNew(super.AX, super.AY, super.wDia, super.hDia, 0, 0, this.AA.size() * var4 - super.hDia, true);
         super.wItem = var4;
         if (GameCanvas.isKeyPressed()) {
            super.idSelect = 0;
            this.AA();
            super.DB = super.AG;
            super.center = super.AH;
            super.backCMD = super.DB;
            super.DF = super.AH;
         } else {
            super.idSelect = -1;
         }
      }
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      AvMain.paintRect(var1, super.AX - 2, super.AY - 2, super.wDia + 2, super.hDia + 2, (byte)1, 2);
      var1.AD(super.AX, super.AY, super.wDia, super.hDia);
      mGraphics.AC();
      int var10000 = super.AX;
      var10000 = super.AY;
      var10000 = super.wDia;
      var10000 = super.hDia;
      mGraphics.AD();
      var1.translate(0, -super.AP.AC);
      int var2;
      int var3 = (var2 = super.AP.AC / super.wItem - 1) + super.hDia / super.wItem + 2;

      for(var2 = var2; var2 < var3; ++var2) {
         if (var2 >= 0 && var2 < this.AA.size()) {
            MainItem var4;
            (var4 = (MainItem)this.AA.elementAt(var2)).AC(var1, super.AX + super.wItem / 2, super.AY + super.wItem / 2 + var2 * super.wItem, super.wItem - 3);
            var4.AC(var1, super.AX + super.wItem / 2, super.AY + super.wItem / 2 + var2 * super.wItem, super.wItem - 2, 1);
            var4.AD(var1, super.AX + super.wItem / 2, super.AY + super.wItem / 2 + var2 * super.wItem, super.wItem - 2, 1);
            int var10001 = super.AX + 1;
            int var10002 = super.AY + 1 + super.wItem / 2 - super.wItem / 2 + var2 * super.wItem;
            int var8;
            int var7 = var8 = super.wItem - 3;
            int var6 = var10002;
            int var5 = var10001;
            var1.setColor(-330796);
            var1.fillRect(var5 + 1, var6 - 1, var7 - 2, 1);
            var1.fillRect(var5 + 1, var6 + var8, var7 - 2, 1);
            var1.fillRect(var5 - 1, var6 + 1, 1, var8 - 2);
            var1.fillRect(var5 + var7, var6 + 1, 1, var8 - 2);
            var1.fillRect(var5, var6, 1, 1);
            var1.fillRect(var5 + var7 - 1, var6, 1, 1);
            var1.fillRect(var5, var6 + var8 - 1, 1, 1);
            var1.fillRect(var5 + var7 - 1, var6 + var8 - 1, 1, 1);
            if (var2 == super.idSelect) {
               var1.setColor(-1);
               var1.drawRect(super.AX + 1, super.AY + super.wItem / 2 - super.wItem / 2 + 1 + var2 * super.wItem, super.wItem - 4, super.wItem - 4);
               var1.setColor(-2423758);
               var1.drawRect(super.AX, super.AY + super.wItem / 2 - super.wItem / 2 + var2 * super.wItem, super.wItem - 2, super.wItem - 2);
            }
         }
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      if (this.AB != null) {
         MainTab.AA(var1, this.AB, this.AC, (byte)0, this.BB, this.BC, this.BD, this.BE, false, (MainObject)null, 0);
      }

      if (!GameCanvas.menuCur.isShowMenu) {
         this.AD(var1);
      }

      if (super.cmdList != null) {
         for(var2 = 0; var2 < super.cmdList.size(); ++var2) {
            iCommand var9;
            (var9 = (iCommand)super.cmdList.elementAt(var2)).paint(var1, var9.xCmd, var9.yCmd);
         }
      }

   }

   public final void update() {
      if (super.AP != null) {
         super.AP.AC();
      }

      this.handleKeyPress();
      this.updatePointer();
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
         this.AA();
         super.AP.AA((super.idSelect + 1) * super.wItem - super.wDia / 2);
      }

      super.handleKeyPress();
      this.AS();
   }

   public final void updatePointer() {
      super.AP.update_Pos_UP_DOWN();
      if (GameCanvas.AB(super.AX, super.AY, super.wDia, super.hDia)) {
         int var1;
         if ((var1 = (GameCanvas.AZ - super.AY + super.AP.AC) / super.wItem) == super.idSelect) {
            super.AH.AD();
         } else if (var1 >= 0 && var1 < this.AA.size()) {
            super.idSelect = var1;
            this.AA();
         }

         GameCanvas.isPointerSelect = false;
      }

      if (GameCanvas.isPointerSelect && !GameCanvas.isPoint(super.AX, super.AY, super.wDia, super.hDia)) {
         super.AG.AD();
      }

   }

   private void AA() {
      if (super.idSelect >= 0 && super.idSelect < this.AA.size()) {
         MainItem var1;
         if ((var1 = (MainItem)this.AA.elementAt(super.idSelect)) != null) {
            this.AB = var1;
            this.AC = MainItem.AA(this.AB);
            int var2 = (super.wItem << 1) + this.BD / 2;
            if (this.BF == 0) {
               var2 = -super.wItem - this.BD / 2 - 2;
            }

            int var10002 = super.AX + var2;
            int var4 = super.AY;
            int var3 = var10002;
            MainItem var7 = this.AB;
            int var5 = this.BD;
            int var6 = this.BE;
            if (var7 != null) {
               var5 = var7.BS;
               var6 = var7.BT;
            }

            this.BB = var3 - var5 / 2;
            if (this.BB + var5 > MotherCanvas.w - 8) {
               this.BB = MotherCanvas.w - var5 - 8;
            }

            if (this.BB < 8) {
               this.BB = 8;
            }

            this.BC = var4;
            if (this.BC + var6 > MotherCanvas.h - GameCanvas.hCommand - 8) {
               this.BC = MotherCanvas.h - GameCanvas.hCommand - var6 - 8;
            }

            if (this.BC < 8) {
               this.BC = 8;
            }
         }

      } else {
         this.AB = null;
      }
   }
}
