public final class Class_BN extends MainEffect {
   private String CG;
   private int CH;
   private int CI;
   private int CJ;
   private int CK;
   private int CL;
   private int CM;
   private int CN;
   private int CO;
   private int CP;
   private mFont CQ;
   private boolean CR = false;
   private boolean CS = false;
   private static int CT = 0;

   public Class_BN(String var1, int var2, int var3, int var4) {
      this.AA(var1, var2, var3, var4);
   }

   public Class_BN(String var1, int var2, int var3, int var4, FrameImage var5, int var6) {
      if (var5 != null) {
         super.fraImgEff = var5;
         super.frame = var6;
      }

      this.AA(var1, var2, var3, var4);
   }

   public Class_BN(int var1, int var2, int var3, int var4, int var5) {
      ++CT;
      super.AB = 1;
      this.CR = false;
      this.CI = 0;
      this.CM = 0;
      super.x = var3;
      super.y = var4;
      this.CH = var5;
      this.CJ = var1;
      this.CK = var2;
      super.vy = -CRes.random(7, 9);
      super.fRemove = CRes.random(26, 30);
      if ((var3 = CT % 9) % 3 != 0) {
         if (CT % 5 % 2 == 1) {
            super.vx = 3;
         } else {
            super.vx = -3;
         }
      }

      this.CL = 6 + var3 / 3;
      if (this.CH == 16 || this.CH == 25) {
         this.CN = var1;
         this.CL = 8;
         if (var1 >= 1000000) {
            this.CS = true;
            this.CN = 500000;
            this.CP = (var1 - this.CN) / 10;
            if (this.CP > 300000) {
               this.CP = 300000;
            }

            super.levelPaint = 1;
            super.fRemove += 20;
         }
      }

   }

   private void AA(String var1, int var2, int var3, int var4) {
      super.AB = 1;
      this.CR = false;
      this.CI = 0;
      this.CG = var1;
      super.x = var2;
      super.y = var3;
      this.CH = var4;
      this.CQ = mFont.tahoma_7b_white;
      if (this.CH < 0) {
         this.CQ = AvMain.AC(-var4);
      } else {
         switch(var4) {
         case 1:
            super.vy = -2;
            super.fRemove = 16;
            this.CQ = mFont.tahoma_7b_yellow;
            return;
         case 2:
         case 6:
         case 7:
         case 9:
         default:
            break;
         case 5:
            this.CQ = mFont.tahoma_7_white;
            break;
         case 8:
            this.CQ = AvMain.AB(var4);
            break;
         case 10:
            super.vy = -1;
            super.fRemove = CRes.random(25, 35);
            return;
         case 24:
            super.vy = -2;
            super.fRemove = 16;
            this.CQ = mFont.tahoma_7b_violet;
            return;
         }
      }

      super.vy = -2;
      super.fRemove = 20;
   }

   public final void paint(mGraphics var1) {
      int var2 = 0;
      if (super.fraImgEff != null) {
         var2 = super.fraImgEff.frameWidth / 2;
         int var3 = mFont.tahoma_7b_white.getWidth(this.CG) / 2;
         super.fraImgEff.drawFrame(super.frame, super.x - var3, super.y + 5, 0, 3, var1);
      }

      switch(this.CH) {
      case 1:
         mFont.tahoma_7b_black.drawString(var1, this.CG, super.x + var2 + 1, super.y + 1, 2);
         break;
      case 2:
         AvMain.AA(var1, this.CG, super.x + var2, super.y, 2);
         return;
      case 3:
         AvMain.FontBorderColor(var1, this.CG, super.x + var2, super.y, 2, (int)1, (int)7);
         return;
      case 4:
         AvMain.FontBorderColor(var1, this.CG, super.x + var2, super.y, 2, (int)4, (int)7);
         return;
      case 5:
         AvMain.AA(var1, this.CG, super.x + var2, super.y, 2, (byte)1);
         return;
      case 6:
         mFont.tahoma_7b_red.drawString(var1, this.CG, super.x + var2, super.y, 2);
         return;
      case 7:
         AvMain.AA(var1, this.CG, super.x + var2, super.y, 2, (byte)5);
         return;
      case 8:
      case 11:
      case 12:
      case 18:
      case 19:
      case 23:
      default:
         break;
      case 9:
         AvMain.AA(var1, this.CG, super.x + var2, super.y, 2, (byte)6);
         return;
      case 10:
         mFont.tahoma_7b_yellow.drawString(var1, this.CG, super.x + var2, super.y, 2);
         return;
      case 13:
      case 14:
      case 15:
      case 20:
         Class_CX.AA(var1, this.CJ, super.x + var2, super.y, this.CH);
         if (this.CK > 0) {
            Class_CX.AA(var1, this.CK, super.x + var2 + 12, super.y + 12, 19);
            return;
         }

         return;
      case 16:
         if (this.CS && this.CN == this.CJ) {
            if (this.CO % 4 <= 3 || this.CO >= 8) {
               Class_CX.AA(var1, this.CN, super.x + var2, super.y, 23);
            }
         } else {
            Class_CX.AA(var1, this.CN, super.x + var2, super.y, this.CH);
         }

         if (this.CK > 0) {
            Class_CX.AA(var1, this.CK, super.x + var2 + 12, super.y + 16, 19);
            return;
         }

         return;
      case 17:
         int var10001 = this.CJ;
         Class_CX.AB(var1, super.x + var2, super.y, this.CH);
         if (this.CK > 0) {
            Class_CX.AA(var1, this.CK, super.x + var2 + 12, super.y + 14, 19);
            return;
         }

         return;
      case 21:
      case 22:
      case 25:
         Class_CX.AA(var1, this.CJ, super.x + var2, super.y, this.CH);
         return;
      case 24:
         mFont.tahoma_7b_black.drawString(var1, this.CG, super.x + var2 + 1, super.y + 1, 2);
      }

      this.CQ.drawString(var1, this.CG, super.x + var2, super.y, 2);
   }

   public final void update() {
      ++super.f;
      super.x += super.vx;
      super.y += super.vy;
      if (super.f >= super.fRemove) {
         super.isStop = true;
      }

      if (this.CS) {
         if (this.CN < this.CJ) {
            this.CN += this.CP + CRes.random(10) * 100;
         } else {
            ++this.CO;
         }

         if (this.CN > this.CJ) {
            this.CN = this.CJ;
         }
      }

      if (this.CI > 0) {
         --this.CI;
      }

      ++this.CM;
      if (this.CH != 14 && this.CH != 13 && this.CH != 15 && this.CH != 20 && this.CH != 21 && this.CH != 22 && this.CH != 17 && this.CH != 16 && this.CH != 25) {
         if (super.vy == 0 && this.CI == 0) {
            this.CI = 5;
         }
      } else if (this.CH != 16 && super.f > super.fRemove - 6) {
         super.vy = 3;
      } else if (this.CM >= this.CL && super.vy < 0) {
         int var1;
         if ((var1 = CRes.abs(super.vy) / 2) < 2) {
            var1 = 2;
         }

         super.vy += var1;
         if (super.vy >= 0) {
            super.vy = 0;
            super.vx = 0;
         }
      }

      if (this.CO >= 6) {
         super.vy = -1;
      }

   }
}
