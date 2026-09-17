public final class Class_DQ {
   public String AA;
   public String AB;
   public int AC;
   public int AD;
   public int AE;
   public short AF;
   public byte AG = -1;
   public static byte AH = -1;
   public static byte AI = 0;
   public static byte AJ = 1;
   public static byte AK = 2;

   public Class_DQ() {
   }

   public Class_DQ(String var1, String var2, int var3, int var4, short var5, byte var6) {
      this.AA = var1;
      this.AB = var2;
      this.AC = var4;
      this.AD = var3;
      this.AF = var5;
      this.AG = var6;
      int var7 = 260;
      if (260 > MotherCanvas.w) {
         var7 = MotherCanvas.w;
      }

      var7 -= 130;
      this.AE = mFont.tahoma_7_black.getWidth(var2) - var7;
      if (this.AE > 0) {
         this.AE += 30;
      }

   }
}
