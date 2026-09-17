public final class infoShow {
   public String AA = "";
   public byte AB;
   public byte AC = -1;
   public int AD;
   public int AE;
   public static byte AF = -100;
   public static byte HARDCODE_PAINT_CENTER = -99;
   public static byte AH = -98;
   public static byte HARDCODE_INFO_CO_BAN = 100;
   public static byte AJ = 101;

   public infoShow(int var1, int var2, String var3, byte var4, byte var5) {
      this.AA = var3;
      this.AD = -1;
      this.AE = var2;
      this.AB = var4;
      this.AC = var5;
   }

   public infoShow(int var1, int var2, byte var3, byte var4) {
      this.AD = var1;
      this.AE = var2;
      this.AB = var3;
      this.AC = var4;
   }

   public final String AA() {
      if (this.AD < 0) {
         return this.AA;
      } else {
         String var1 = MainItem.BZ[this.AD].AF + " ";
         return var1 + MainItem.AA(this.AE, MainItem.BZ[this.AD].AC);
      }
   }
}
