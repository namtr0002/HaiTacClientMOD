public final class Class_FU extends Class_GT {
   public static Class_FU AA;
   private Class_DS AB;
   private iCommand AC;
   private mVector AD = new mVector();
   private int AE;

   public Class_FU(byte var1, Class_DS var2) {
      super(var1);
      this.AB = var2;
      super.BO = 160;
      super.BP = 120;
      super.BM = MotherCanvas.hw - super.BO / 2;
      super.BN = MotherCanvas.hh - super.BP / 2;
      this.AE = 0;
      this.AC = new iCommand(T.close, -1, this);
      this.AD.addElement(this.AC);
      this.AA(0, this.AD);
      var2.BI.AA = mFont.tahoma_7_white.splitFontArray(var2.BI.numPotion + " " + var2.BI.name, super.BO - 20 - 32 - 15);
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         Class_FD.AA.Show((MainScreen)GameCanvas.gameScr);
      default:
      }
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      int var10006 = super.BO;
      this.AD(var1, super.BM, super.BN, super.BO, super.BP, 0);
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.BM + 10, super.BN + 16, super.BO - 20, 16, 4, 4);
      AvMain.FontBorderColor(var1, T.AZ, super.BM + super.BO / 2, super.BN + 18, 2, (int)6, (int)5);
      AvMain.paintRect(var1, super.BM + 10, super.BN + 40, super.BO - 20, 32, (byte)0, 4);
      if (this.AB != null) {
         GameCanvas.resetTrans(var1);
         this.AB.BI.AA(var1, super.BM + 10, super.BN + 40);
      }

      GameCanvas.resetTrans(var1);
      if (this.AD != null) {
         for(int var2 = 0; var2 < this.AD.size(); ++var2) {
            iCommand var3;
            (var3 = (iCommand)this.AD.elementAt(var2)).paint(var1, var3.xCmd, var3.yCmd);
         }
      }

   }

   public final void updatePointer() {
      super.updatePointer();
      if (this.AD != null) {
         for(int var1 = 0; var1 < this.AD.size(); ++var1) {
            ((iCommand)this.AD.elementAt(var1)).AE();
         }
      }

   }

   public final void handleKeyPress() {
      if (this.AD != null) {
         int var1 = this.AD.size();
         if (GameCanvas.isKeyPressed() && var1 > 0) {
            int var2 = this.AE;
            if (GameCanvas.isKeyPressed(0)) {
               --this.AE;
               GameCanvas.clearKeyPressed(0);
            } else if (GameCanvas.isKeyPressed(2)) {
               ++this.AE;
               GameCanvas.clearKeyPressed(2);
            }

            this.AE = AvMain.AA(this.AE, var1 - 1, false);
            if (var2 != this.AE && GameCanvas.isKeyPressed()) {
               for(var2 = 0; var2 < var1; ++var2) {
                  iCommand var3 = (iCommand)this.AD.elementAt(var2);
                  if (var2 == this.AE) {
                     var3.AG = true;
                  } else {
                     var3.AG = false;
                  }
               }
            }
         }
      }

      if (GameCanvas.AL[5]) {
         GameCanvas.AB(5);
         if (this.AD != null && this.AE < this.AD.size()) {
            ((iCommand)this.AD.elementAt(this.AE)).AD();
         }
      }

      this.AS();
   }

   public final void update() {
      super.update();
      if (super.KP != null) {
         super.KP.update();
      }

   }
}
