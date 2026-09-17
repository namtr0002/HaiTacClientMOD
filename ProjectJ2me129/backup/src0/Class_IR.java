public final class Class_IR extends MainItem {
   public String[] AA = null;
   public int CP;
   public int CQ;
   public int CR;

   public Class_IR(String var1, byte var2, short var3, short var4, byte var5) {
      super.name = var1;
      super.typeObject = var2;
      super.idIcon = var3;
      super.numPotion = var4;
      super.colorName = var5;
   }

   public final void paint(mGraphics var1, int var2, int var3, int var4) {
      var1.drawRegion((mImage)AvMain.imgKhungItem, var2, var3, 3);
      super.paint(var1, var2, var3, var4);
      this.AE(var1, var2, var3, 3);
   }

   public final void AA(mGraphics var1, int var2, int var3) {
      MainImage var4;
      if ((var4 = this.getImage()) != null && var4.img != null) {
         var1.drawRegion(var4.img, 0, 0, 20, 20, 0, var2 + 4, var3 + 4, 0);
      }

      if (this.AA != null) {
         if (this.AA.length == 1) {
            AvMain.AA(var1, this.AA[0], var2 + 30, var3 + 4 + 4, 0, (byte)super.colorName);
            return;
         }

         var3 += 4;

         for(int var5 = 0; var5 < this.AA.length; ++var5) {
            AvMain.AA(var1, this.AA[var5], var2 + 30, var3, 0, (byte)super.colorName);
            var3 += 13;
         }
      }

   }

   public final void AA(int var1, int var2, int var3) {
      this.CP = var1;
      this.CQ = var2;
      this.CR = var3;
   }
}
