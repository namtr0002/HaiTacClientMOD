public final class Point_Focus {
   public boolean AA = false;
   public byte AB;
   public int x;
   public int y;
   public int dis;
   public int fRe;
   public int AG;
   public int frame;
   public int AI;
   public int vy;
   public int AK;
   public int AL;
   public int AM;
   public int AN;
   public int AO;
   public int AP;
   public int AQ;
   public MainObject AR = null;

   public Point_Focus(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public Point_Focus() {
   }

   public final boolean AA() {
      if (this.x < this.AK) {
         if (this.AI < 4) {
            ++this.AI;
         }
      } else if (this.AI > -4) {
         --this.AI;
      }

      if (this.y < this.AL) {
         if (this.vy < 4) {
            ++this.vy;
         }
      } else if (this.vy > -4) {
         --this.vy;
      }

      int var1 = this.AK - this.x;
      int var2 = this.AL - this.y;
      if (CRes.abs(var1) < this.AM && CRes.abs(var2) < this.AM) {
         this.AI = 0;
         this.vy = 0;
         return true;
      } else {
         this.x += this.AI;
         this.y += this.vy;
         return false;
      }
   }

   public final void update_Vx_Vy() {
      ++this.AG;
      this.x += this.AI;
      this.y += this.vy;
   }
}
