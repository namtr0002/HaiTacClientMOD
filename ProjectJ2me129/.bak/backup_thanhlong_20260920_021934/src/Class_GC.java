public final class Class_GC extends Class_GT {
   public static Class_GC AA;
   private ListNew AB;
   private Class_DS AC;
   private iCommand AD;
   private mVector AE = new mVector();
   private int AF;
   private int AG = 0;
   private int AH;
   private int AI;
   private Scroll AJ = new Scroll();

   public Class_GC(byte var1, Class_DS var2) {
      super(var1);
      this.AC = var2;
      if (Class_GD.AA.BO > 180) {
         super.BO = Class_GD.AA.BO - 20;
      } else {
         super.BO = Class_GD.AA.BO;
      }

      super.BP = Class_GD.AA.BP;
      super.BM = MotherCanvas.hw - super.BO / 2;
      super.BN = MotherCanvas.hh - super.BP / 2;
      this.AF = 0;
      this.AG = 0;
      super.BQ = 29;
      this.AI = 5;
      this.AH = super.BP - 80 - iCommand.hButtonCmdNor;
      this.AD = new iCommand(T.close, -1, this);
      this.AE.removeAllElements();
      this.AE.addElement(this.AD);
      this.AA(0, this.AE);
      int var4 = var2.BH.AD * super.BQ - this.AH + (this.AI << 1);
      this.AB = new ListNew(super.BM + 10, super.BN + 68, super.BO - 20 - this.AI, this.AH, 0, 0, var4, true);
      this.AJ.AA(super.BM + super.BO - 10, super.BN + 69, this.AH, -7967666);

      for(var4 = 0; var4 < var2.BH.AE.size(); ++var4) {
         Class_IR var3;
         (var3 = (Class_IR)var2.BH.AE.elementAt(var4)).AA = mFont.tahoma_7_white.splitFontArray(var3.numPotion + " " + var3.name, super.BO - 70);
      }

   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         Class_GD.AA.Show((MainScreen)GameCanvas.gameScr);
         return;
      case 0:
         GlobalService.getInstance().AH((byte)1, (byte)((byte)this.AC.AG));
      default:
      }
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      int var10002 = super.BM + 10;
      int var5 = super.BN + 38;
      int var4 = var10002;
      int var10006 = super.BO;
      this.AD(var1, super.BM, super.BN, super.BO, super.BP, 0);
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.BM + 10, super.BN + 16, super.BO - 20, 16, 4, 4);
      AvMain.FontBorderColor(var1, T.UF, super.BM + super.BO / 2, super.BN + 18, 2, (int)6, (int)5);
      mFont.tahoma_7_black.drawString(var1, T.UG + " " + Interface_Game.AA(this.AC.BH.AB) + " " + T.QF, var4, var5, 0);
      mFont.tahoma_7_black.drawString(var1, T.nhanduoc, var4, var5 + 14, 0);
      AvMain.paintRect(var1, var4, var5 + 30, super.BO - 20 - this.AI, this.AH, (byte)0, 4);
      GameCanvas.resetTrans(var1);
      var1.AD(super.BM + 10, super.BN + 68, super.BO - 20 - this.AI, this.AH);
      mGraphics.AC();
      int var10000 = super.BM;
      var10000 = super.BN;
      var10000 = super.BO;
      var10000 = this.AI;
      var10000 = this.AH;
      mGraphics.AD();
      var1.translate(0, -this.AB.AC);
      this.AA(var1, super.BM + 10, super.BN + 73);
      GameCanvas.resetTrans(var1);
      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      if (this.AB.AD > 0) {
         this.AJ.AA(var1);
      }

      if (this.AE != null) {
         for(int var2 = 0; var2 < this.AE.size(); ++var2) {
            iCommand var3;
            (var3 = (iCommand)this.AE.elementAt(var2)).paint(var1, var3.xCmd, var3.yCmd);
         }
      }

   }

   private void AA(mGraphics var1, int var2, int var3) {
      if (this.AC != null) {
         for(int var4 = 0; var4 < this.AC.BH.AD; ++var4) {
            ((Class_IR)this.AC.BH.AE.elementAt(var4)).AA(var1, var2 + this.AI, var3);
            var3 += super.BQ;
         }

      }
   }

   public final void update() {
      if (super.KP != null) {
         super.KP.update();
      }

      super.update();
      this.AB.AC();
      this.AJ.AA(this.AB.AC, this.AB.AD);
   }

   public final void handleKeyPress() {
      if (this.AE != null) {
         int var1 = this.AE.size();
         if (GameCanvas.isKeyPressed() && var1 > 0) {
            int var2 = this.AF;
            if (GameCanvas.isKeyPressed(0)) {
               --this.AF;
               GameCanvas.clearKeyPressed(0);
            } else if (GameCanvas.isKeyPressed(2)) {
               ++this.AF;
               GameCanvas.clearKeyPressed(2);
            }

            this.AF = AvMain.AA(this.AF, var1 - 1, false);
            if (var2 != this.AF && GameCanvas.isKeyPressed()) {
               for(var2 = 0; var2 < var1; ++var2) {
                  iCommand var3 = (iCommand)this.AE.elementAt(var2);
                  if (var2 == this.AF) {
                     var3.AG = true;
                  } else {
                     var3.AG = false;
                  }
               }
            }
         }
      }

      boolean var4 = false;
      if (GameCanvas.isKeyPressed(1)) {
         --this.AG;
         GameCanvas.clearKeyPressed(1);
         var4 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         ++this.AG;
         GameCanvas.clearKeyPressed(3);
         var4 = true;
      }

      if (var4) {
         this.AG = AvMain.AA(this.AG, this.AC.BH.AD - 1, false);
         this.AB.AA((this.AG + 1) * super.BQ - this.AH / 2);
      }

      if (GameCanvas.AL[5]) {
         GameCanvas.AB(5);
         if (this.AE != null && this.AF < this.AE.size()) {
            ((iCommand)this.AE.elementAt(this.AF)).AD();
         }
      }

      this.AS();
   }

   public final void updatePointer() {
      super.updatePointer();
      this.AB.update_Pos_UP_DOWN();
      if (this.AE != null) {
         for(int var1 = 0; var1 < this.AE.size(); ++var1) {
            ((iCommand)this.AE.elementAt(var1)).AE();
         }
      }

   }
}
