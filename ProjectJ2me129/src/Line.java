public final class Line {
   public int x0;
   public int y0;
   public int x1;
   public int y1;
   private int AH;
   private int AI;
   public int AE;
   public int type;
   public boolean is2Line = false;

   public final void setLine(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      this.x0 = var1;
      this.y0 = var2;
      this.x1 = var3;
      this.y1 = var4;
      this.AH = var5;
      this.AI = var6;
      this.is2Line = var7;
   }

   public final void AA() {
      this.x0 += this.AH;
      this.x1 += this.AH;
      this.y0 += this.AI;
      this.y1 += this.AI;
      ++this.AE;
   }
}
