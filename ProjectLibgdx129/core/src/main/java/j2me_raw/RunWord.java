public final class RunWord {
   private boolean AA = false;
   private String[] AB;
   private int AC;
   private int AD;
   private int AE;
   private int AF;
   private int AG;
   private int AH;
   private mFont AI;

   public final void AA(String var1, int var2, int var3, int var4, int var5, mFont var6) {
      this.AI = var6;
      this.AC = var3;
      this.AD = var4;
      this.AG = var5;
      if (this.AG > MotherCanvas.w - 10) {
         this.AG = MotherCanvas.w - 10;
      }

      this.AB = var6.splitFontArray(var1, this.AG);
      this.AH = this.AC;
      this.AA = true;
      this.AE = 0;
      this.AF = 0;
   }

   public final boolean AA() {
      if (this.AF < this.AB.length) {
         this.AF = this.AB.length;
         this.AE = 0;
         return false;
      } else {
         this.AE = this.AF = 0;
         return true;
      }
   }

   public final void AB() {
      if (this.AA && this.AF < this.AB.length) {
         this.AE += 2;
         if (this.AE >= this.AB[this.AF].length()) {
            this.AE = 0;
            ++this.AF;
         }
      }

   }

   public final void AA(mGraphics var1, int var2) {
      int var3 = this.AD;
      if (var2 == 2) {
         for(var2 = 0; var2 < this.AF; ++var2) {
            this.AI.drawString(var1, this.AB[var2], this.AH + this.AG / 2, var3 + var2 * GameCanvas.hText, 2);
         }

         if (this.AF < this.AB.length) {
            this.AI.drawString(var1, this.AB[this.AF].substring(0, this.AE), this.AH + this.AG / 2, var3 + this.AF * GameCanvas.hText, 2);
            return;
         }
      } else {
         for(var2 = 0; var2 < this.AF; ++var2) {
            this.AI.drawString(var1, this.AB[var2], this.AH, var3 + var2 * GameCanvas.hText, 0);
         }

         if (this.AF < this.AB.length) {
            this.AI.drawString(var1, this.AB[this.AF].substring(0, this.AE), this.AH, var3 + this.AF * GameCanvas.hText, 0);
         }
      }

   }
}
