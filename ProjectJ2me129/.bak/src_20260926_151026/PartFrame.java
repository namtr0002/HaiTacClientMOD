public final class PartFrame {
   public short idSmallImg;
   public short dx;
   public short dy;
   public short AC;
   public byte flip;
   public byte AD;
   public byte onTop = 0;
   public byte AE = 0;
   public short rotate;

   public PartFrame(int var1, int var2, int var3) {
      this.idSmallImg = (short)var3;
      this.dx = (short)var1;
      this.dy = (short)var2;
      this.AC = (short)var2;
   }
}
