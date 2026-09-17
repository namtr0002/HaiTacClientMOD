public final class PartFrame {
   public short idSmallImg;
   public short dx;
   public short AC;
   public byte AD;
   public byte AE = 0;

   public PartFrame(int var1, int var2, int var3) {
      this.idSmallImg = (short)var3;
      this.dx = (short)var1;
      this.AC = (short)var2;
   }
}
