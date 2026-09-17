public final class Scroll {
   private int AB;
   private int AC;
   private int AD;
   private int AE;
   private int AF;
   private int AG;
   public static int hRectScroll = 24;
   private static int AH = 24;
   private int AI = 0;

   public final void AA(int var1, int var2, int var3, int var4) {
      this.AB = var1;
      this.AC = var2;
      this.AD = var3;
      this.AE = var4;
      hRectScroll = AH;
      this.AG = var3 - AH;
   }

   public final void AA(mGraphics var1) {
      var1.setColor(this.AE);
      var1.fillRect(this.AB - 2, this.AC - 1, 3, 1);
      var1.fillRect(this.AB - 3, this.AC, 1, this.AD - 1);
      var1.fillRect(this.AB + 1, this.AC, 1, this.AD - 1);
      var1.fillRect(this.AB - 2, this.AC + this.AD - 1, 3, 1);
      if (this.AI <= 0) {
         var1.fillRect(this.AB - 2, this.AC + this.AF, 3, hRectScroll - CRes.abs(this.AI));
      } else {
         var1.fillRect(this.AB - 2, this.AC + this.AF + CRes.abs(this.AI), 3, hRectScroll - CRes.abs(this.AI));
      }
   }

   public final void AA(int var1, int var2) {
      if (var2 > 0) {
         this.AF = var1 * this.AG / var2;
         if (this.AF >= 0 && this.AF <= this.AG) {
            this.AI = 0;
         } else {
            if (this.AF > this.AG) {
               this.AI = (this.AF - this.AG) / 18;
               this.AF = this.AG;
            }

            if (this.AF < 0) {
               this.AI = this.AF / 18;
               this.AF = 0;
            }

            if (this.AI > 12) {
               this.AI = 12;
            } else {
               if (this.AI < -12) {
                  this.AI = -12;
               }

            }
         }
      }
   }
}
