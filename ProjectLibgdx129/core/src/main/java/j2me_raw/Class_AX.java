public final class Class_AX {
   public int AA;
   public int AB;
   public byte AC;
   public short AD;
   public mVector AE = new mVector();
   private int AG;
   public boolean AF = false;
   private boolean AH = false;
   private int AI;
   private int AJ = 1;
   private int AK = 0;
   private int AL = 29;
   private int AM = 93;
   private int AN = 20;
   private boolean AO = false;
   private int AP;

   public Class_AX(int var1, byte var2, short var3) {
      this.AB = var1;
      this.AC = var2;
      this.AD = var3;
      int var4 = (this.AL + 3) * this.AD - 3;
      this.AG = 0;
      this.AI = 0;
      this.AH = false;
      if (var4 > this.AM) {
         this.AK = var4 - this.AM;
         this.AF = true;
      } else {
         this.AF = false;
      }
   }

   public final void AA(mGraphics var1, int var2, int var3) {
      int var4;
      if (this.AF && this.AD > 3) {
         for(var4 = 0; var4 < this.AD; ++var4) {
            ((Class_IR)this.AE.elementAt(var4)).AA(var1, var2 - this.AG + (this.AL + 3) * var4 + this.AL / 2, var3 + this.AL / 2, this.AL);
         }

      } else {
         for(var4 = 0; var4 < 3; ++var4) {
            Class_IR var5;
            if ((var5 = (Class_IR)this.AE.elementAt(var4)) != null) {
               var5.AA(var1, var2 + (this.AL + 3) * var4 + this.AL / 2, var3 + this.AL / 2, this.AL);
            }
         }

      }
   }

   public final void AA() {
      if (this.AF) {
         ++this.AI;
         if (this.AO) {
            if (this.AI <= this.AP) {
               return;
            }

            this.AO = false;
         }

         if (this.AI > 10) {
            if (!this.AH) {
               this.AG += this.AJ;
            } else {
               this.AG -= this.AJ;
            }
         }

         if (this.AG > this.AK || this.AG < 0) {
            this.AH = !this.AH;
            this.AO = true;
            this.AP = this.AI + this.AN;
            return;
         }
      } else {
         this.AG = 0;
         this.AI = 0;
         this.AO = false;
      }

   }
}
