public final class Boat extends MainItemMap {
   public short AA = -1;
   private boolean QA = true;
   public boolean AH = true;
   public boolean AI = false;
   public int AJ = 12;
   public static int AK = 12;
   private byte QB = 2;
   public static mImage imgShip;
   public static mImage imgEffSea;
   public static mImage imgEffSea2;
   public static mImage imgEffSea3;
   public static FrameImage fraEffSea;
   public static FrameImage fraEffSea2;
   public static FrameImage fraEffSea3;
   public static FrameImage fraPirateUnity;
   public static FrameImage fraEffSea4;
   public mVector AU = new mVector("Boat.vecEff");
   public int AV;
   public int AW;
   public int AX;
   public int AY;
   public short[] AZ = new short[]{0, 1, 2, 3};
   private int QC = 40;
   private int QD = 0;
   private int QE = 0;

   public Boat(short var1, int var2, int var3, int var4, byte var5) {
      super.wOne = 100;
      super.hOne = 70;
      super.CQ = -50;
      super.vySea = 3;
      super.ID = var1;
      super.y = var3;
      super.ySort = var3 + super.CQ;
      super.dy = 0;
      super.Dir = (byte)(var5 == 2 ? 2 : 0);
      super.x = var2 + (var5 == 2 ? this.AJ : -this.AJ);
      int var10000 = GameCanvas.loadmap.maxHMap;
      super.TypeItem = 1;
      this.AA();
   }

   public final void AA() {
      MainObject var1;
      if ((var1 = MainObject.get_Object((int)super.ID, (byte)0)) != null && var1.clan != null && GameCanvas.currentScreen != GameCanvas.CG) {
         this.AA = var1.clan.idIcon;
      }

   }

   public final void AA(short[] var1, byte var2) {
      this.AZ = var1;
      this.QB = var2;
   }

   public final void paint(mGraphics var1) {
      if (this.AH) {
         this.AB(var1);
         this.AE(var1);
         this.AG(var1);
      }

   }

   public final void update() {
      this.AF();
      this.AG();
      if (this.QC <= 0) {
         super.y = ReadMessenge.AF;
      }

      super.ySort = super.y + super.CQ;
   }

   public final void AA(int var1, int var2, int var3, byte var4) {
      super.y = var2;
      super.dy = var3;
      super.Dir = (byte)(var4 == 2 ? 2 : 0);
      super.x = var1 + (var4 == 2 ? this.AJ : -this.AJ);
      this.AB(0, 0);
      this.QC = 40;
   }

   public final void AB(mGraphics var1) {
      for(int var2 = 0; var2 < this.AU.size(); ++var2) {
         Point var3;
         if ((var3 = (Point)this.AU.elementAt(var2)).AT == -1) {
            if (var3.frame == 2) {
               fraEffSea2.drawFrame(var3.f / var3.fSmall % fraEffSea2.nFrame, var3.x, var3.y, var3.dis, 3, var1);
            } else {
               FrameImage var4;
               if (var3.frame == -1) {
                  var4 = fraEffSea;
                  if (MapGotoSky.isBeginEffBoat) {
                     var4 = fraEffSea4;
                  }

                  var4.drawFrame(var3.f / var3.fSmall % var4.nFrame, super.x + (super.Dir == 2 ? 37 : -37), super.y + super.dy / 2 + AK - 3, super.Dir, 3, var1);
               } else {
                  var4 = fraEffSea;
                  if (MapGotoSky.isBeginEffBoat) {
                     var4 = fraEffSea4;
                  }

                  var4.drawFrame(var3.f / var3.fSmall % var4.nFrame, var3.x, var3.y, var3.dis, 3, var1);
               }
            }
         }
      }

      if (this.AZ != null) {
         Class_DV.AA(var1, this.AZ[0], 0, super.x, super.y, super.dy, super.Dir);
      }

   }

   public final void AE(mGraphics var1) {
      short var3 = this.AA;
      if (this.AZ != null) {
         Class_DV.AA(var1, this.AZ[1], 1, super.x, super.y, super.dy, super.Dir);
         Class_DV.AA(var1, this.AZ[2], 2, super.x, super.y, super.dy, super.Dir);
         MainImage var6;
         if (var3 >= 0 && (var6 = Potion.AC(var3)) != null && var6.img != null) {
            int var4 = GameCanvas.gameTick / 6 % 2;
            int var5 = -(16 - var4);
            if (super.Dir == 2) {
               var5 = 16 - var4;
            }

            if (var6.frame == -1) {
               var6.set_Frame();
            }

            if (var6.frame <= 1) {
               var1.drawRegion((mImage)var6.img, super.x + var5, super.y - 30 - super.dy, 3);
            } else {
               byte var8;
               if (this.QE >= var6.frame - 1) {
                  var8 = 15;
               } else {
                  var8 = 3;
               }

               if (CRes.abs(GameCanvas.gameTick - this.QD) > var8) {
                  ++this.QE;
                  if (this.QE >= var6.frame) {
                     this.QE = 0;
                  }

                  this.QD = GameCanvas.gameTick;
               }

               var1.drawRegion(var6.img, 0, this.QE * var6.AB, var6.AB, var6.AB, 0, super.x + var5, super.y - 30 - super.dy, 3);
            }
         }

         byte var7 = this.QB;
         if (this.QB == -1) {
            var7 = 3;
         }

         Class_DV.AA(var1, (short)0, var7 + 100, super.x, super.y, super.dy, super.Dir);
      }

   }

   public final void AF(mGraphics var1) {
      int var10000 = super.Dir;
      var1.drawRegion(imgShip, 0, 0, 59, 39, super.Dir, super.x, super.y + AK - 6 - super.dy, 33);
   }

   public final void AG(mGraphics var1) {
      byte var2 = 0;
      if (GameCanvas.mapBack != null && GameCanvas.mapBack.AB) {
         var2 = GameCanvas.BO;
      }

      this.AA(var1, var2);
   }

   public final void AA(mGraphics var1, byte var2) {
      if (this.AZ != null) {
         Class_DV.AA(var1, this.AZ[3], 3, super.x, super.y, super.dy, super.Dir);
      }

      if (!GameCanvas.lowGraphic) {
         if (var2 == 0) {
            if (MapGotoSky.isBeginEffBoat) {
               if (GameScreen.player.SH != 2) {
                  var1.drawRegion(imgEffSea3, 0, 12 * (GameCanvas.gameTick / 12 % 2), 78, 12, super.Dir, super.x, super.y + AK + 2, 3);
               }
            } else {
               var1.drawRegion(imgEffSea, 0, 12 * (GameCanvas.gameTick / 12 % 2), 78, 12, super.Dir, super.x, super.y + AK + 2, 3);
            }
         } else {
            var1.drawRegion(imgEffSea2, 0, 12 * (GameCanvas.gameTick / 12 % 2), 78, 12, super.Dir, super.x, super.y + AK + 2, 3);
         }
      }

      if (var2 != GameCanvas.BO) {
         for(int var6 = 0; var6 < this.AU.size(); ++var6) {
            Point var3;
            if ((var3 = (Point)this.AU.elementAt(var6)).AT >= 0) {
               if (var3.frame == 2) {
                  fraEffSea2.drawFrame(var3.f / var3.fSmall % fraEffSea2.nFrame, var3.x, var3.y, var3.dis, 3, var1);
               } else {
                  FrameImage var4;
                  if (var3.frame == -1) {
                     var4 = fraEffSea;
                     if (MapGotoSky.isBeginEffBoat) {
                        var4 = fraEffSea4;
                     }

                     var4.drawFrame(var3.f / var3.fSmall % var4.nFrame, super.x + (super.Dir == 2 ? 37 : -37), super.y + super.dy / 2 + AK - 3, super.Dir, 3, var1);
                  } else {
                     var4 = fraEffSea;
                     if (MapGotoSky.isBeginEffBoat) {
                        var4 = fraEffSea4;
                     }

                     var4.drawFrame(var3.f / var3.fSmall % var4.nFrame, var3.x, var3.y, var3.dis, 3, var1);
                  }
               }
            }
         }

      }
   }

   public final void AF() {
      for(int var1 = 0; var1 < this.AU.size(); ++var1) {
         Point var2;
         Point var10000 = var2 = (Point)this.AU.elementAt(var1);
         var10000.x += var2.vx;
         ++var2.f;
         if (var2.f >= var2.fRe) {
            this.AU.removeElement(var2);
         }
      }

      if (this.QC > 0) {
         --this.QC;
      }

   }

   public final void AG() {
      if (CRes.random(40) == 0) {
         if (CRes.random(2) == 0) {
            super.vySea = 3;
         } else {
            super.vySea = -3;
         }
      }

      if (super.CH > 0 && super.vySea > 0) {
         super.vySea = -3;
      } else if (super.CH < -30 && super.vySea < 0) {
         super.vySea = 3;
      }

      super.CH += super.vySea;
      super.dy = super.CH / 10;
   }

   public final void AA(int var1, int var2, int var3, byte var4, int var5) {
      this.QA = true;
      if (!GameCanvas.lowGraphic) {
         Point var6;
         (var6 = new Point()).frame = var3;
         if (var3 == -1) {
            var6.x = super.x;
            var6.y = super.y;
            var6.vx = var5;
            var6.fSmall = 3;
         } else if (var3 == 0) {
            var3 = CRes.random(20, 48);
            var6.x = var1 + (super.Dir == 2 ? var3 : -var3);
            var6.y = var2 + AK - 3 + super.dy / 2;
            var6.vx = var5;
            var6.fSmall = 2;
         } else if (var3 == 1) {
            var3 = CRes.random_Am_0(30);
            var6.x = var1 + (super.Dir == 2 ? this.AJ : -this.AJ) + var3;
            var6.y = var2 + AK - 3 + super.dy / 2;
            var6.vx = var5;
            var6.fSmall = 3;
         } else if (var3 == 2) {
            var6.x = var1 + (super.Dir == 2 ? this.AJ : -this.AJ);
            var6.y = var2 + AK - 4 + super.dy / 2;
            var6.vx = var5;
            var6.fSmall = 3;
            var6.AT = -1;
         }

         var6.dis = var4;
         var6.fRe = 7 * var6.fSmall;
         this.AU.addElement(var6);
      }

   }

   public final void AH() {
      if (this.QA) {
         for(int var1 = 0; var1 < this.AU.size(); ++var1) {
            ((Point)this.AU.elementAt(var1)).AT = -1;
         }
      }

      this.QA = false;
   }

   public final void AB(int var1, int var2) {
      this.AV = super.x - (super.Dir == 2 ? this.AJ - 10 : -this.AJ + 10) - 30 + var1;
      this.AX = 60;
      this.AW = super.y + var2;
      this.AY = 8;
   }

   public final void AI() {
      this.AV = super.x - (super.Dir == 2 ? this.AJ - 15 : -this.AJ + 15) - 14;
      this.AX = 90;
      this.AW = super.y - 6;
      this.AY = 12;
   }

   public final boolean AB() {
      return super.x + super.dx + super.wOne / 2 >= MainScreen.cameraMain.xCam && super.x + super.dx - super.wOne / 2 <= MainScreen.cameraMain.xCam + MotherCanvas.w && super.y + super.dy + 10 >= MainScreen.cameraMain.yCam && super.y + super.dy + 10 - super.hOne <= MainScreen.cameraMain.yCam + MotherCanvas.h;
   }
}
