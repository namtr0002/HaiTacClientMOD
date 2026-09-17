public final class Class_DX extends MainItem {
   private byte AA = 0;

   public Class_DX(short var1, short var2, String var3, byte var4) {
      super.ID = var1;
      super.idIcon = var2;
      super.typeObject = var4;
      super.name = var3;
      super.namepaint = var3;
      if (var4 == 103) {
         this.AA = 5;
      } else {
         this.AA = 0;
      }
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      super.BV = super.idIcon;
      if (super.idIcon == -1) {
         super.BV = 0;
      }

      MainObject.AF(var1, super.BV, this.AA, var2, var3, 2);
   }

   public final mVector AB(byte var1) {
      mVector var2 = new mVector();
      if (super.AF == 0 && super.AJ == 0) {
         var2.addElement(Class_BH.CH);
      } else {
         var2.addElement(Class_BH.CJ);
      }

      return var2;
   }
}
