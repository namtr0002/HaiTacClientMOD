public class MainEffect {
   public int typeEffect = 0;
   public int AB;
   public int fRemove;
   public int x;
   public int y;
   public int toX;
   public int toY;
   public int f;
   public int frame;
   public int vMax;
   public int vx;
   public int vy;
   public int AM;
   public int x1000;
   public int y1000;
   public int step;
   public int am_duong = 0;
   public int CFrame;
   public int AS;
   public int AT;
   public int AU;
   public int indexImg;
   public int AW;
   public int AX;
   public byte frameSuper = 0;
   public int AZ;
   public int BA;
   public short timeEnd;
   public byte Dir;
   public int levelPaint = 0;
   public int BE;
   public long timeBegin;
   public int[] mframe;
   public int[] mframeSub;
   public boolean BI;
   public boolean isStop;
   public boolean BK = false;
   public FrameImage fraImgEff;
   public FrameImage fraImgSubEff;
   public FrameImage fraImgSub2Eff;
   public FrameImage fraImgSub3Eff;
   public FrameImage BP;
   public boolean BQ = false;
   public boolean BR = false;
   public boolean BS = false;
   public long BT;
   public long BU;
   public long BV;
   public MainObject objFireMain;
   public MainObject objMainEff;
   public boolean isEff;
   public byte numNextFrame = 1;
   public mVector vecObjsBeFire;
   public int life = 0;
   public int gocT_Arc;
   public int vX1000;
   public int vY1000;
   public int va;
   private int[] CG = new int[]{0, 0, 2, 1, 1, 2, 0, 0, 2, 1, 1, 2, 0, 0, 2, 1, 1, 2, 0, 0, 2, 1, 1, 2};
   private int[] CH = new int[]{2, 2, 3, 3, 3, 4, 5, 5, 5, 5, 5, 1, 0, 0, 0, 0, 0, 7, 6, 6, 6, 6, 6, 2};
   private int[] CI = new int[]{12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13};

   public void paint(mGraphics var1) {
   }

   public void paint(mGraphics var1, int var2, int var3) {
   }

   public boolean CreateEffectSkill() {
      return true;
   }

   public void update() {
      ++this.f;
      this.x += this.vx;
      this.y += this.vy;
   }

   public void replaceHP(mVector var1) {
   }

   public final Point_Focus create_Speed(int var1, int var2, Point_Focus var3) {
      if (var2 == 0) {
         var2 = 1;
      }

      if (var1 == 0) {
         var1 = 1;
      }

      int var6;
      if ((var6 = MainObject.AD(var1, var2) / this.vMax) == 0) {
         var6 = 1;
      }

      int var4 = var1 / var6;
      int var5 = var2 / var6;
      if (CRes.abs(var4) > CRes.abs(var1)) {
         var4 = var1;
      }

      if (CRes.abs(var5) > CRes.abs(var2)) {
         var5 = var2;
      }

      if (var3 != null) {
         var3.x = this.x;
         var3.y = this.y;
         var3.AI = var4;
         var3.vy = var5;
         var3.AK = this.toX;
         var3.AL = this.toY;
         var3.fRe = var6;
      } else {
         this.fRemove = var6;
         this.vx = var4;
         this.vy = var5;
      }

      return var3;
   }

   public final Point_Focus create_Speed(int var1, int var2, Point_Focus var3, int var4, int var5, int var6, int var7) {
      if (var2 == 0) {
         var2 = 1;
      }

      if (var1 == 0) {
         var1 = 1;
      }

      int var10;
      if ((var10 = MainObject.AD(var1, var2) / this.vMax) == 0) {
         var10 = 1;
      }

      int var8 = var1 / var10;
      int var9 = var2 / var10;
      if (CRes.abs(var8) > CRes.abs(var1)) {
         var8 = var1;
      }

      if (CRes.abs(var9) > CRes.abs(var2)) {
         var9 = var2;
      }

      if (var3 != null) {
         var3.x = var4;
         var3.y = var5;
         var3.AI = var8;
         var3.vy = var9;
         var3.AK = var6;
         var3.AL = var7;
         var3.fRe = var10;
      } else {
         this.fRemove = var10;
         this.vx = var8;
         this.vy = var9;
      }

      return var3;
   }

   public final boolean updateAngleNormal(MainObject var1, int var2) {
      if (var1 == null) {
         this.stopUpdateNormal();
         return true;
      } else {
         var2 = var1.x - this.x;
         int var4 = var1.y - (var1.hOne >> 1) - this.y;
         ++this.life;
         if ((CRes.abs(var2) >= this.vMax / 1000 || CRes.abs(var4) >= this.vMax / 1000) && this.life <= this.fRemove) {
            int var3;
            if (CRes.abs((var3 = CRes.AA(var2, var4)) - this.gocT_Arc) < 90 || var2 * var2 + var4 * var4 > 4096) {
               if (CRes.abs(var3 - this.gocT_Arc) < 15) {
                  this.gocT_Arc = var3;
               } else if ((var3 - this.gocT_Arc < 0 || var3 - this.gocT_Arc >= 180) && var3 - this.gocT_Arc >= -180) {
                  this.gocT_Arc = CRes.fixangle(this.gocT_Arc - 15);
               } else {
                  this.gocT_Arc = CRes.fixangle(this.gocT_Arc + 15);
               }
            }

            if (this.va < this.vMax) {
               this.va += 2048;
            }

            this.vX1000 = this.va * CRes.getcos(this.gocT_Arc) >> 10;
            this.vY1000 = this.va * CRes.getsin(this.gocT_Arc) >> 10;
            var2 = var2 + this.vX1000 >> 10;
            this.x += var2;
            var4 = var4 + this.vY1000 >> 10;
            this.y += var4;
            return false;
         } else {
            this.stopUpdateNormal();
            return true;
         }
      }
   }

   public void removeEff() {
   }

   public void stopUpdateNormal() {
   }

   public final void setAva(int var1, MainObject var2) {
      if (var2 != null && !var2.returnAction()) {
         if (var1 == -1) {
            var2.IQ = 0;
         }

         if (var1 == 0) {
            if (var2.dy == 0) {
               var2.dy = 12;
            }
         } else if (var1 == 1) {
            if (this.Dir == 0) {
               var2.IQ = -4;
            } else {
               var2.IQ = 4;
            }

            if (var2.dy == 0) {
               var2.dy = 16;
            }
         } else if (var1 == 2) {
            if (this.Dir == 0) {
               var2.IQ = -6;
            } else {
               var2.IQ = 6;
            }

            if (var2.dy == 0) {
               var2.dy = 20;
            }
         }

         if (LoadMap.specMap == 4) {
            var2.IQ = 0;
         }

         if (var2.typeObject == 1 && (var2.AM() == 9 || var2.AM() == 8 || var2.AM() == 19)) {
            var2.IQ = 0;
         }

         if (var2.Action != 4 && var2.Action != 2 && var2.Hp > 0) {
            var2.Action = 3;
            var2.f = 0;
            var2.resetAction();
         } else {
            var2.IQ = 0;
            var2.dy = 0;
         }
      }
   }

   public static void AB(int var0, MainObject var1) {
      if (var1 != null && !var1.returnAction()) {
         var1.dy = var0;
         if (var1.Action != 4 && var1.Action != 2) {
            var1.Action = 3;
            var1.f = 0;
         }

      }
   }

   public final void paint_Bullet(mGraphics var1, FrameImage var2, int var3, int var4, int var5) {
      var2.drawFrame(this.CG[var3], var4, var5, this.CH[var3], 3, var1);
   }

   public final int setFrameAngle(int var1) {
      if (var1 > 15 && var1 <= 345) {
         if ((var1 = (var1 - 15) / 15 + 1) > 24) {
            var1 = 24;
         }

         var1 = this.CI[var1];
      } else {
         var1 = 12;
      }

      return var1;
   }

   public final void setAngle() {
      this.Dir = 0;
      if (this.x < this.toX) {
         this.Dir = 2;
      }

   }
}
