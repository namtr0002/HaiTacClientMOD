public final class MainInfoItem {
   public byte AA;
   public byte AB;
   public byte AC;
   public byte AD = -1;
   public int AE;
   public String AF;

   public MainInfoItem(byte var1, int var2) {
      this.AA = var1;
      this.AE = var2;
   }

   public MainInfoItem(byte var1, int var2, byte var3) {
      this.AA = var1;
      this.AE = var2;
      this.AD = var3;
   }

   public MainInfoItem(String var1, byte var2, byte var3) {
      this.AF = var1;
      this.AB = var2;
      this.AC = var3;
      this.AD = -1;
   }

   public MainInfoItem(String var1, int var2) {
      this.AF = var1;
      this.AE = var2;
   }
}
