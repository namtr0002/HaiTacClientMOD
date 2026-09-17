public final class ListNew {
   private int maxW;
   public int maxH;
   private int x;
   private int y;
   private int AI;
   public int AB;
   public int AC;
   public int AD;
   private int AJ;
   private int AK;
   private int[] pointerDownLastX = new int[3];
   public boolean AE;
   private boolean AM;
   private int AN;
   private int AO;
   private int AP;

   public ListNew() {
   }

   public ListNew(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      this.x = var1;
      this.y = var2;
      this.maxW = var3;
      this.maxH = var4;
      this.AD = var7;
      if (var8 && this.AD < 0) {
         this.AD = 0;
      }

   }

   public final void update_Pos_UP_DOWN() {
      int var1;
      int var2;
      if (GameCanvas.isPointerDown) {
         if (!this.AE && GameCanvas.isPointer(this.x, this.y, this.maxW, this.maxH)) {
            for(var1 = 0; var1 < this.pointerDownLastX.length; ++var1) {
               this.pointerDownLastX[0] = GameCanvas.AZ;
            }

            this.AK = GameCanvas.AZ;
            this.AE = true;
            this.AM = this.AN != 0;
            this.AN = 0;
         } else if (this.AE) {
            ++this.AJ;
            if (this.AJ > 5 && this.AK == GameCanvas.AZ && !this.AM) {
               this.AK = -1000;
            }

            if ((var1 = GameCanvas.AZ - this.pointerDownLastX[0]) != 0 && this.AI != -1) {
               this.AI = -1;
            }

            for(var2 = this.pointerDownLastX.length - 1; var2 > 0; --var2) {
               this.pointerDownLastX[var2] = this.pointerDownLastX[var2 - 1];
            }

            this.pointerDownLastX[0] = GameCanvas.AZ;
            this.AB -= var1;
            if (this.AB < 0) {
               this.AB = 0;
            }

            if (this.AB > this.AD) {
               this.AB = this.AD;
            }

            if (this.AC < 0 || this.AC > this.AD) {
               var1 /= 2;
            }

            this.AC -= var1;
         }
      }

      if (GameCanvas.AR && this.AE) {
         var1 = GameCanvas.AZ - this.pointerDownLastX[0];
         GameCanvas.AR = false;
         if (CRes.abs(var1) < 20 && CRes.abs(GameCanvas.AZ - this.AK) < 20 && !this.AM && GameCanvas.isPointerSelect) {
            this.AN = 0;
            this.AB = this.AC;
            this.AK = -1000;
            this.AJ = 0;
         } else if (this.AI != -1 && this.AJ > 5) {
            this.AJ = 0;
         } else if (this.AI == -1 && !this.AM) {
            if (this.AC < 0) {
               this.AB = 0;
            } else if (this.AC > this.AD) {
               this.AB = this.AD;
            } else {
               byte var3;
               if ((var2 = GameCanvas.AZ - this.pointerDownLastX[0] + (this.pointerDownLastX[0] - this.pointerDownLastX[1]) + (this.pointerDownLastX[1] - this.pointerDownLastX[2])) > 10) {
                  var3 = 10;
               } else if (var2 < -10) {
                  var3 = -10;
               } else {
                  var3 = 0;
               }

               this.AN = -var3 * 100;
            }
         }

         this.AE = false;
         this.AJ = 0;
         GameCanvas.AR = false;
      }

   }

   public final void AB() {
      int var1;
      int var2;
      if (GameCanvas.isPointerDown) {
         if (!this.AE && GameCanvas.isPointer(this.x, this.y, this.maxW, this.maxH)) {
            for(var1 = 0; var1 < this.pointerDownLastX.length; ++var1) {
               this.pointerDownLastX[0] = GameCanvas.AY;
            }

            this.AK = GameCanvas.AY;
            this.AE = true;
            this.AM = this.AN != 0;
            this.AN = 0;
         } else if (this.AE) {
            ++this.AJ;
            if (this.AJ > 5 && this.AK == GameCanvas.AY && !this.AM) {
               this.AK = -1000;
            }

            if ((var1 = GameCanvas.AY - this.pointerDownLastX[0]) != 0 && this.AI != -1) {
               this.AI = -1;
            }

            for(var2 = this.pointerDownLastX.length - 1; var2 > 0; --var2) {
               this.pointerDownLastX[var2] = this.pointerDownLastX[var2 - 1];
            }

            this.pointerDownLastX[0] = GameCanvas.AY;
            this.AB -= var1;
            if (this.AB < 0) {
               this.AB = 0;
            }

            if (this.AB > this.AD) {
               this.AB = this.AD;
            }

            if (this.AC < 0 || this.AC > this.AD) {
               var1 /= 2;
            }

            this.AC -= var1;
         }
      }

      if (GameCanvas.AR && this.AE) {
         var1 = GameCanvas.AY - this.pointerDownLastX[0];
         GameCanvas.AR = false;
         if (CRes.abs(var1) < 20 && CRes.abs(GameCanvas.AY - this.AK) < 20 && !this.AM && GameCanvas.isPointerSelect) {
            this.AN = 0;
            this.AB = this.AC;
            this.AK = -1000;
            this.AJ = 0;
         } else if (this.AI != -1 && this.AJ > 5) {
            this.AJ = 0;
         } else if (this.AI == -1 && !this.AM) {
            if (this.AC < 0) {
               this.AB = 0;
            } else if (this.AC > this.AD) {
               this.AB = this.AD;
            } else {
               byte var3;
               if ((var2 = GameCanvas.AY - this.pointerDownLastX[0] + (this.pointerDownLastX[0] - this.pointerDownLastX[1]) + (this.pointerDownLastX[1] - this.pointerDownLastX[2])) > 10) {
                  var3 = 10;
               } else if (var2 < -10) {
                  var3 = -10;
               } else {
                  var3 = 0;
               }

               this.AN = -var3 * 100;
            }
         }

         this.AE = false;
         this.AJ = 0;
         GameCanvas.AR = false;
      }

   }

   public final void AC() {
      if (this.AN != 0 && !this.AE) {
         this.AB += this.AN / 100;
         if (this.AB < 0) {
            this.AB = 0;
         } else if (this.AB > this.AD) {
            this.AB = this.AD;
         } else {
            this.AC = this.AB;
         }

         this.AN = this.AN * 9 / 10;
         if (this.AN < 100 && this.AN > -100) {
            this.AN = 0;
         }
      }

      if (this.AC != this.AB && !this.AE) {
         this.AO = this.AB - this.AC << 2;
         this.AP += this.AO;
         this.AC += this.AP >> 4;
         this.AP &= 15;
      }

   }

   public final void AA(int var1) {
      if (var1 < 0) {
         var1 = 0;
      }

      if (var1 > this.AD) {
         var1 = this.AD;
      }

      this.AB = var1;
   }
}
