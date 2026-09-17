public final class Class_BR extends MainEffect {
   private int CG;
   private int CH;
   private short CI;
   private mVector CJ = new mVector();
   private String[] CK = new String[]{"0 co gi", "choang", "Chay Mau", "Giam tan cong", "giam phong thu", "Hoa Mat", "Điện giật", "Lửa cháy", "Trói chân", "hut mana", "Trúng độc"};
   private int[][] CL;
   private int[] CM;
   private Point CN;
   private int CO = 0;

   public Class_BR(MainObject var1, short var2, short var3) {
      super.objFireMain = var1;
      super.typeEffect = var2;
      super.timeBegin = GameCanvas.timeNow;
      this.CI = var3;
      int var4;
      int var5;
      Point var6;
      Point var7;
      switch(super.typeEffect) {
      case -2:
         super.fraImgEff = new FrameImage(156, 26, 50);
         this.CG = -var1.hOne + 20;
         super.levelPaint = -1;
         return;
      case -1:
         super.fraImgEff = new FrameImage(155, 21, 28);
         this.CG = -var1.hOne + 12;
         super.levelPaint = -1;
         return;
      case 1:
         super.objFireMain.KX = super.objFireMain.Dir;
         super.fraImgEff = new FrameImage(158, 25, 12);
         this.CG = -var1.hOne + 3;
         return;
      case 2:
         super.fraImgEff = new FrameImage(162, 10, 20);
         this.CG = -var1.hOne + 35;
         this.CH = CRes.random_Am_0(8);
         super.vy = 2;
         return;
      case 3:
         super.fraImgEff = new FrameImage(159, 45, 12);
         this.CG = -super.objFireMain.hOne + 5;
         (var7 = new Point(0, this.CG)).vy = 2;
         this.CJ.addElement(var7);
         return;
      case 4:
         super.fraImgEff = new FrameImage(161, 35, 13);
         this.CG = -super.objFireMain.hOne + 10;
         (var7 = new Point(0, this.CG)).vy = 2;
         this.CJ.addElement(var7);
         return;
      case 5:
         super.fraImgEff = new FrameImage(157, 10, 10);
         this.CG = -var1.hOne + 3;

         for(var4 = 0; var4 < 2; ++var4) {
            (var7 = new Point()).x = -5;
            if (var4 == 1) {
               var7.x = 5;
            }

            var7.y = this.CG;
            var7.frame = var4 << 1;
            this.CJ.addElement(var7);
         }

         return;
      case 6:
         super.fraImgEff = new FrameImage(122, 16, 12);

         for(var5 = 0; var5 < 3; ++var5) {
            (var6 = new Point(super.objFireMain.x + CRes.random_Am_0(20), super.objFireMain.y - CRes.random(super.objFireMain.hOne + 10))).frame = CRes.random(3);
            this.CJ.addElement(var6);
         }

         return;
      case 7:
         super.fraImgEff = new FrameImage(160, 9, 14);
         this.CG = -var1.hOne / 2;
         var4 = CRes.random(2, 5);

         for(var5 = 0; var5 < var4; ++var5) {
            (var6 = new Point(CRes.random_Am_0(10), this.CG + CRes.random_Am_0(15))).fRe = CRes.random(6, 10);
            var6.frame = CRes.random(3) * 3;
            var6.dis = CRes.random(3);
            this.CJ.addElement(var6);
         }

         return;
      case 8:
         super.fraImgEff = new FrameImage(170, 32, 24);
         super.levelPaint = -1;
         return;
      case 9:
         super.fraImgEff = new FrameImage(163, 28, 31);
         return;
      case 10:
         super.fraImgEff = new FrameImage(164, 19, 19);
         this.CG = -var1.hOne;
         return;
      case 11:
         super.fraImgEff = new FrameImage(350, 25, 44);
         this.CL = new int[][]{{0, 0, 2, 2, 6, 6, 12, 12, -1, -1, -1, -1}, {0, 0, 1, 1, 1, 1, 2, 2, 0, 0, 0, 0}};
         return;
      case 12:
         this.CN = new Point(0, 0);
         this.CN.vx = -4;
         this.CN.vy = -15;
         super.fraImgEff = new FrameImage(220, 9, 9, 4);
         this.CM = new int[]{4, 7, 5, 7};
      case 0:
      case 13:
      case 14:
      case 16:
      case 17:
      default:
         return;
      case 15:
         super.fraImgEff = new FrameImage(398, 19, 19);
         this.CG = -var1.hOne + 1;
         return;
      case 18:
         super.fraImgEff = new FrameImage(400, 3);
         this.CG = -var1.hOne;
      }
   }

   public final void AB(mGraphics var1) {
      int var2;
      Point var3;
      switch(super.typeEffect) {
      case 3:
      case 4:
         for(var2 = 0; var2 < this.CJ.size(); ++var2) {
            var3 = (Point)this.CJ.elementAt(var2);
            super.fraImgEff.drawFrame(GameCanvas.gameTick / 2 % super.fraImgEff.nFrame / 2 << 1, super.objFireMain.x + var3.x, super.objFireMain.y + var3.y - super.fraImgEff.frameHeight / 2, 0, 3, var1);
         }

         return;
      case 7:
         for(var2 = 0; var2 < this.CJ.size(); ++var2) {
            if ((var3 = (Point)this.CJ.elementAt(var2)).dis == 0) {
               super.fraImgEff.drawFrame(var3.frame + var3.f / 2 % 3, super.objFireMain.x + var3.x, super.objFireMain.y + var3.y, 0, 3, var1);
            }
         }

         return;
      case 8:
         super.fraImgEff.drawFrame(GameCanvas.BJ % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y + this.CG, 0, 3, var1);
         return;
      case 12:
         if (this.CN != null && this.CN.vx > 0) {
            for(var2 = 0; var2 < this.CJ.size(); ++var2) {
               var3 = (Point)this.CJ.elementAt(var2);
               super.fraImgEff.drawFrameNew(this.CM[var3.f / 2], super.objFireMain.x + var3.x, super.objFireMain.y + var3.y / 10, 0, 3, var1);
            }
         }
      case 5:
      case 6:
      case 9:
      case 10:
      case 11:
      default:
      }
   }

   public final void paint(mGraphics var1) {
      int var3;
      int var6;
      Point var8;
      switch(super.typeEffect) {
      case -2:
         super.fraImgEff.drawFrame(GameCanvas.gameTick / 3 % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y + this.CG - (GameCanvas.gameTick / 3 % 3 << 1), super.objFireMain.Dir, 3, var1);
         return;
      case -1:
         super.fraImgEff.drawFrame(GameCanvas.gameTick / 3 % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y + this.CG - GameCanvas.gameTick / 3 % 3, super.objFireMain.Dir, 3, var1);
         return;
      case 0:
      case 8:
      case 13:
      case 14:
      case 16:
      case 17:
      default:
         break;
      case 1:
      case 10:
         super.fraImgEff.drawFrame(GameCanvas.BJ % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y + this.CG, 0, 3, var1);
         return;
      case 2:
         if (this.CO <= super.fRemove) {
            if ((var6 = this.CO / 2) > 2) {
               var6 = 2;
            }

            super.fraImgEff.drawFrame(var6, super.objFireMain.x + this.CH, super.objFireMain.y + this.CG, 0, 3, var1);
            return;
         }
         break;
      case 3:
      case 4:
         for(var6 = 0; var6 < this.CJ.size(); ++var6) {
            var8 = (Point)this.CJ.elementAt(var6);
            super.fraImgEff.drawFrame((GameCanvas.gameTick / 2 % super.fraImgEff.nFrame / 2 << 1) + 1, super.objFireMain.x + var8.x, super.objFireMain.y + var8.y + super.fraImgEff.frameHeight / 2, 0, 3, var1);
         }

         return;
      case 5:
         for(var6 = 0; var6 < this.CJ.size(); ++var6) {
            var8 = (Point)this.CJ.elementAt(var6);
            super.fraImgEff.drawFrame((GameCanvas.gameTick / 6 + var8.frame) % super.fraImgEff.nFrame, super.objFireMain.x + var8.x, super.objFireMain.y + var8.y, 0, 3, var1);
         }

         return;
      case 6:
         mGraphics var7 = var1;
         Class_BR var5 = this;

         for(var3 = 0; var3 < var5.CJ.size(); ++var3) {
            Point var4 = (Point)var5.CJ.elementAt(var3);
            var5.fraImgEff.drawFrame((var4.frame << 1) + var4.f / 2, var5.objFireMain.x + var4.x, var5.objFireMain.y - var4.y, var4.dis, 3, var7);
         }

         return;
      case 7:
         for(var6 = 0; var6 < this.CJ.size(); ++var6) {
            if ((var8 = (Point)this.CJ.elementAt(var6)).dis != 0) {
               super.fraImgEff.drawFrame(var8.frame + var8.f / 2 % 3, super.objFireMain.x + var8.x, super.objFireMain.y + var8.y, 0, 3, var1);
            }
         }

         return;
      case 9:
         super.fraImgEff.drawFrame(GameCanvas.BJ % super.fraImgEff.nFrame, super.objFireMain.x, super.objFireMain.y, 0, 33, var1);
         return;
      case 11:
         var6 = super.f % this.CL[0].length;
         if (this.CL[0][var6] >= 0) {
            var3 = this.CL[0][var6];
            if (super.objFireMain.type_left_right == 0) {
               var3 = -var3;
            }

            super.fraImgEff.drawFrame(this.CL[1][var6], super.objFireMain.x - var3, super.objFireMain.y, super.objFireMain.type_left_right == 0 ? 2 : 0, 33, var1);
            super.fraImgEff.drawFrame(this.CL[1][var6], super.objFireMain.x + var3, super.objFireMain.y, super.objFireMain.type_left_right, 33, var1);
            return;
         }
         break;
      case 12:
         if (this.CN != null && this.CN.vx < 0) {
            for(var3 = 0; var3 < this.CJ.size(); ++var3) {
               Point var2 = (Point)this.CJ.elementAt(var3);
               super.fraImgEff.drawFrameNew(this.CM[var2.f / 2], super.objFireMain.x + var2.x, super.objFireMain.y + var2.y / 10, 0, 3, var1);
            }

            return;
         }
         break;
      case 15:
         super.fraImgEff.drawFrame(GameCanvas.BJ % super.fraImgEff.nFrame, super.objFireMain.x + this.CH, super.objFireMain.y + this.CG, 0, 3, var1);
         return;
      case 18:
         super.fraImgEff.drawFrame(GameCanvas.gameTick / 2 % super.fraImgEff.nFrame, super.objFireMain.x + this.CH, super.objFireMain.y + this.CG, 0, 3, var1);
      }

   }

   public final void update() {
      if ((GameCanvas.timeNow - super.timeBegin) / 100L >= (long)this.CI) {
         super.BI = true;
      } else {
         int var1;
         Point var2;
         Point var3;
         switch(super.typeEffect) {
         case -2:
         case -1:
         case 1:
         case 5:
         case 9:
         case 10:
         case 15:
         case 18:
            return;
         case 0:
         case 8:
         case 13:
         case 14:
         case 16:
         case 17:
         default:
            if (GameCanvas.gameTick % 20 == 0) {
               GameScreen.addEffectNum(this.CK[super.typeEffect], super.objFireMain.x, super.objFireMain.y - super.objFireMain.hOne, (byte)5);
            }
            break;
         case 2:
            ++this.CO;
            if (this.CO >= super.fRemove && (CRes.random(20) == 0 || this.CO > super.fRemove + 15)) {
               this.CH = CRes.random_Am_0(8);
               this.CG = -super.objFireMain.hOne + 35;
               super.fRemove = CRes.random(8, 14);
               this.CO = 0;
            }

            if (this.CO >= 6) {
               this.CG += super.vy;
               return;
            }
            break;
         case 3:
         case 4:
            for(var1 = 0; var1 < this.CJ.size(); ++var1) {
               (var2 = (Point)this.CJ.elementAt(var1)).update();
               if (var2.y >= 0) {
                  if (var2.frame < 5) {
                     var2.y = 0;
                     ++var2.frame;
                     if (var2.frame == 2) {
                        (var3 = new Point(0, this.CG)).vy = 2;
                        this.CJ.addElement(var3);
                     }
                  } else {
                     this.CJ.removeElement(var2);
                     --var1;
                  }
               }
            }

            return;
         case 6:
            if (CRes.random(3) != 0) {
               for(var1 = 0; var1 < 2; ++var1) {
                  (var2 = new Point(CRes.random_Am_0(15), CRes.random(super.objFireMain.hOne + 1))).frame = CRes.random(3);
                  var2.dis = CRes.random(8);
                  this.CJ.addElement(var2);
               }
            }

            for(var1 = 0; var1 < this.CJ.size(); ++var1) {
               ++(var2 = (Point)this.CJ.elementAt(var1)).f;
               if (var2.f >= 4) {
                  this.CJ.removeElement(var2);
                  --var1;
               }
            }

            return;
         case 7:
            if (CRes.random(3) == 0 && this.CJ.size() < 4) {
               var1 = CRes.random(1, 4);

               for(int var4 = 0; var4 < var1; ++var4) {
                  (var3 = new Point(CRes.random_Am_0(10), this.CG + CRes.random_Am_0(15))).fRe = CRes.random(6, 10);
                  var3.frame = CRes.random(3) * 3;
                  var3.vy = -CRes.random(2);
                  var3.dis = CRes.random(3);
                  this.CJ.addElement(var3);
               }
            }

            for(var1 = 0; var1 < this.CJ.size(); ++var1) {
               (var2 = (Point)this.CJ.elementAt(var1)).update();
               if (var2.f > var2.fRe) {
                  this.CJ.removeElement(var2);
                  --var1;
               }
            }

            return;
         case 11:
            ++super.f;
            return;
         case 12:
            for(var1 = 0; var1 < this.CJ.size(); ++var1) {
               (var2 = (Point)this.CJ.elementAt(var1)).update();
               if (var2.f >= 8) {
                  this.CJ.removeElement(var2);
                  --var1;
               }
            }

            this.CN.update();
            if (this.CN.x < -16) {
               this.CN.vx = 4;
            }

            if (this.CN.x > 16) {
               this.CN.vx = -4;
            }

            if (this.CN.y < -500) {
               this.CN.y = 0;
            }

            Point var5;
            (var5 = new Point(this.CN.x, this.CN.y)).vy = CRes.random(30);
            var5.f = 1;
            this.CJ.addElement(var5);
            return;
         }

      }
   }

   public final short AE() {
      return (short)((int)((long)this.CI - (GameCanvas.timeNow - super.timeBegin)));
   }

   public final void AA(int var1) {
      this.CH = var1;
   }
}
