public final class Class_BS {
   private int AA = 1;
   private int[][] AB = new int[][]{{-1, -1, -1052689, -1052689, -4079167, -4079167, -6250336}, {-14092490, -14092490, -14295246, -14295246, -14563796, -14563796, -14247637}, {-4947201, -4947201, -5801491, -5801491, -6656038, -6656038, -8562000}, {-197061, -197061, -1512905, -1512905, -2828750, -2828750, -5592279}, {-654281, -654281, -2030541, -2030541, -3472338, -3472338, -5504217}, {-14944764, -14944764, -654281, -654281, -1, -1, -1052689}, {-14944764, -14944764, -654281, -14092490, -1, -1, -1052689}, {-14944764, -14944764, -654281, -14092490, -4947201, -1, -1052689}};
   private int[] AC = new int[]{2, 1, 1, 1, 1, 1};
   private int[] AD = new int[]{2, 2, 1, 1, 1, 1, 1, 1};
   private int AE = 0;
   private int AF = 0;

   public final void AA(int var1, int var2, int var3, int var4, mGraphics var5, int var6, boolean var7) {
      if (var3 > 0) {
         this.AF = var3 <= 2 ? 0 : (var3 <= 5 ? 1 : (var3 <= 8 ? 2 : (var3 <= 9 ? 3 : (var3 <= 10 ? 4 : (var3 <= 11 ? 1 : (var3 <= 12 ? 2 : (var3 <= 13 ? 3 : (var3 <= 14 ? 4 : 5))))))));
         int var15 = this.AF;
         if (var3 >= 11) {
            var5.setColor(this.AB[GameCanvas.gameTick / 6 % 5][6]);
            var5.drawRect(var1 - var4 / 2 - var6, var2 - var4 / 2 - var6, var4, var4);
         } else {
            var5.setColor(this.AB[var15][6]);
            var5.drawRect(var1 - var4 / 2 - var6, var2 - var4 / 2 - var6, var4, var4);
         }

         int[] var14;
         if (var3 >= 11) {
            var14 = this.AD;
         } else {
            var14 = this.AC;
         }

         int var8;
         int var9;
         int var10;
         int var11;
         int var12;
         byte var13;
         for(var8 = 0; var8 < var14.length; ++var8) {
            var10 = GameCanvas.gameTick - var8 * this.AA;
            if ((var9 = (var10 = (var10 + this.AE) % (var4 * 4)) >= 0 && var10 < var4 ? var10 % var4 : (var4 <= var10 && var10 < var4 << 1 ? var4 : -1)) != -1) {
               var10 = var1 - var4 / 2 + var9;
               var11 = var2 - var4 / 2 + this.AA(GameCanvas.gameTick - var8 * this.AA, var4);
               var12 = var8;
               if (var8 > 5) {
                  var12 = 5;
               }

               var5.setColor(this.AB[var15][var12]);
               var12 = var14[var8];
               var13 = 0;
               if (var9 <= var4 && var12 == 2) {
                  var13 = 1;
               }

               var5.fillRect(var10 - var12 / 2 - var6, var11 - var12 / 2 - var6 + var13, var12, var12);
            }
         }

         for(var8 = 0; var8 < var14.length; ++var8) {
            var10 = GameCanvas.gameTick + var4 - var8 * this.AA;
            var10 = (var10 + this.AE) % (var4 * 4);
            int var10000;
            if (var4 <= var10 && var10 < var4 << 1) {
               var10000 = 0;
            } else if (var4 << 1 <= var10 && var10 < var4 * 3) {
               var10000 = var4 - (var4 - var10 % var4);
            } else {
               if (this.AE == 0) {
                  this.AE = var4 << 1;
               } else {
                  this.AE = 0;
               }

               var10000 = -1;
            }

            var9 = var10000;
            if (var10000 != -1) {
               var10 = var1 - var4 / 2 + var9;
               var11 = var2 - var4 / 2 + this.AA(GameCanvas.gameTick + var4 - var8 * this.AA, var4);
               var12 = var8;
               if (var8 > 5) {
                  var12 = 5;
               }

               var5.setColor(this.AB[var15][var12]);
               var12 = var14[var8];
               var13 = 0;
               if (var9 == 0 && var12 == 2) {
                  var13 = 1;
               }

               var5.fillRect(var10 - var12 / 2 - var6 + var13, var11 - var12 / 2 - var6, var12, var12);
            }
         }

      }
   }

   private int AA(int var1, int var2) {
      if ((var1 = (var1 + this.AE) % (var2 * 4)) >= 0 && var1 < var2) {
         return 0;
      } else if (var2 <= var1 && var1 < var2 << 1) {
         return var1 % var2;
      } else {
         return var2 << 1 <= var1 && var1 < var2 * 3 ? var2 : var2 - var1 % var2;
      }
   }
}
