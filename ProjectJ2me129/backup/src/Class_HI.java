public final class Class_HI extends MainScreen {
   public static Class_HI AA;
   private iCommand AB;
   private int AC;
   private int AD;
   private int AE;
   private int AF;
   private int AG;
   private mVector AH = new mVector();

   public Class_HI() {
   }

   public Class_HI(mVector var1, byte var2, byte var3) {
      this.AH = var1;
      this.AE = AvMain.DG;
      this.AF = AvMain.DH;
      this.AC = MotherCanvas.hw - this.AE / 2;
      this.AD = MotherCanvas.hh - this.AF / 2;
      this.AB = new iCommand(T.close, -1, this);
      this.AB = AvMain.AA(this.AB, 2);
      super.DB = this.AB;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
         }

         return;
      default:
      }
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      int var5 = this.AD;
      int var4 = this.AC;
      byte var6 = 2;
      if (this.AH.size() == 1) {
         var6 = 3;
      } else if (this.AG == 0) {
         var6 = 1;
      } else if (this.AG == this.AH.size() - 1) {
         var6 = 0;
      }

      AvMain.AC(var1, var4, var5, this.AE, this.AF, var6);
      if (this.AG >= 0 && this.AG < this.AH.size()) {
         Class_DS var2 = (Class_DS)this.AH.elementAt(this.AG);
         int var10005 = this.AF;
         AA(var1, var2, this.AC, this.AD, this.AE);
      }

      super.AD(var1);
   }

   public static void AA(mGraphics var0, Class_DS var1, int var2, int var3, int var4) {
      if (var1 != null) {
         AvMain.FontBorderColor(var0, var1.AC, var2 + var4 / 2, var3 + 102, 2, (int)0, (int)8);
         if (var1.BC != null) {
            var1.BC.AA(var0, var2 + var4 / 2, var3 + 80, false);
            Class_CX.AA(var0, var1.BC.KP, var2 + var4 / 2 + 4, var3 + 130);
         }
      }
   }

   public final void update() {
      if (super.KP != null) {
         super.KP.update();
      }

      super.update();
   }

   public final void handleKeyPress() {
      if (GameCanvas.isKeyPressed(0)) {
         if (this.AG > 0) {
            --this.AG;
         }

         GameCanvas.clearKeyPressed(0);
      } else if (GameCanvas.isKeyPressed(2)) {
         if (this.AG < this.AH.size() - 1) {
            ++this.AG;
         }

         GameCanvas.clearKeyPressed(2);
      }

      super.handleKeyPress();
   }

   public final void updatePointer() {
      super.updatePointer();
      if (GameCanvas.isPointerSelect && GameCanvas.isPoint(0, 0, MotherCanvas.hw, MotherCanvas.h)) {
         GameCanvas.isPointerSelect = false;
         if (this.AG > 0) {
            --this.AG;
         }
      }

      if (GameCanvas.isPointerSelect && GameCanvas.isPoint(MotherCanvas.hw, 0, MotherCanvas.hw, MotherCanvas.h)) {
         GameCanvas.isPointerSelect = false;
         if (this.AG < this.AH.size() - 1) {
            ++this.AG;
         }
      }

   }
}
