public final class Camera {
   public int xCam;
   public int yCam;
   public int AC;
   public int AD;
   public int AE;
   public int AF;
   private int AG;
   private int AH;
   private int AI;
   private int AJ;

   public final void setAll(int var1, int var2, int var3, int var4) {
      this.AE = var1;
      this.AF = var2;
      if (this.AF < 0) {
         this.AF = 0;
      }

      if (this.AE < 0) {
         this.AE = 0;
      }

      if (var3 > var1) {
         var3 = var1;
      }

      if (var3 < 0) {
         var3 = 0;
      }

      if (var4 > var2) {
         var4 = var2;
      }

      if (var4 < 0) {
         var4 = 0;
      }

      this.xCam = var3;
      this.yCam = var4;
      this.AC = var3;
      this.AD = var4;
   }

   public final void AA(int var1, int var2, boolean var3) {
      if (var1 < 0) {
         var1 = 0;
      }

      if (var1 > this.AE) {
         var1 = this.AE;
      }

      if (var3) {
         if (var2 < 0) {
            var2 = 0;
         }
      } else if (var2 < -50) {
         var2 = -50;
      }

      if (var2 > this.AF) {
         var2 = this.AF;
      }

      this.xCam = var1;
      this.yCam = var2;
      this.AC = var1;
      this.AD = var2;
   }

   public final void AA(int var1, int var2) {
      this.AC = var1;
      this.AD = var2;
   }

   public final void AA() {
      if (this.xCam != this.AC) {
         this.AG = this.AC - this.xCam << 1;
         this.AH += this.AG;
         this.xCam += this.AH >> 4;
         this.AH &= 15;
         if (this.xCam < 0) {
            this.xCam = 0;
         }

         if (this.xCam > this.AE) {
            this.xCam = this.AE;
         }
      }

      if (this.yCam != this.AD) {
         this.AI = this.AD - this.yCam << 1;
         this.AJ += this.AI;
         this.yCam += this.AJ >> 4;
         this.AJ &= 15;
         if (this.yCam > this.AF) {
            this.yCam = this.AF;
         }
      }

   }
}
