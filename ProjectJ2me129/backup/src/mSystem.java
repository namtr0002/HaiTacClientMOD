public class mSystem {
   public String AA;
   public String AB;
   public String AC;
   public String AD;
   public byte AE;
   public byte AF;
   public byte AG;
   public byte AH;
   public byte AI;
   public long AJ;
   public short AK;
   public short AL;

   public mSystem() {
   }

   public static int[][] new_M_Int(int var0, int var1) {
      return new int[var0][2];
   }

   public mSystem(String var1, byte var2) {
      this.AB = "";
      this.AC = "";
      this.AF = -1;
      this.AI = 0;
      this.AJ = -1L;
      this.AK = -1;
      this.AA = var1;
      this.AE = var2;
   }

   public void AA() {
      long var1;
      if ((var1 = (GameCanvas.timeNow - this.AJ) / 1000L) < 60L) {
         this.AC = 1 + T.phuttruoc;
      } else if (var1 < 3600L) {
         this.AC = var1 / 60L + T.phuttruoc;
      } else if (var1 < 86400L) {
         this.AC = var1 / 3600L + T.gio + " " + var1 % 3600L / 60L + T.phuttruoc;
      } else if (var1 < 604800L) {
         this.AC = var1 / 86400L + " " + T.NX;
      } else {
         this.AC = T.NY;
      }
   }
}
