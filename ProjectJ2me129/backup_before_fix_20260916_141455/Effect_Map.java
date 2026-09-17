public final class Effect_Map {
   private mVector AB = new mVector();
   private mVector AC = new mVector();
   public byte AA;
   private byte AD;
   private int AE = 0;
   private int AF;
   private int AG = 0;
   private int AH;
   private int AI;
   private static byte[][] AJ = new byte[][]{{0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1}, {0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7}};
   private FrameImage AK;
   private FrameImage AL;
   private int AM;

   public Effect_Map(byte var1, byte var2) {
      if (var1 == 4) {
         if (LoadMapScreen.isMapSky == 1) {
            this.AK = new FrameImage(mImage.createImage("/bg/sea1fly.png"), 24, 24);
            this.AL = new FrameImage(mImage.createImage("/bg/sea2fly.png"), 24, 24);
         } else {
            this.AK = new FrameImage(mImage.createImage("/bg/sea1.png"), 24, 24);
            this.AL = new FrameImage(mImage.createImage("/bg/sea2.png"), 24, 24);
         }

         this.AI = 3;
      } else {
         this.AA = var1;
         this.AB.removeAllElements();
         short var3 = 8000;
         this.AD = 5;
         if (var2 >= 3) {
            this.AD = 6;
         }

         switch(var1) {
         case 1:
         case 6:
            var3 = 3000;
         case 2:
         case 4:
         case 5:
         case 7:
         default:
            break;
         case 3:
            var3 = 2000;
            break;
         case 8:
            var3 = 1500;
         }

         int var6 = MotherCanvas.w * MotherCanvas.h / var3;
         if (var2 == 0) {
            var6 /= 4;
         }

         if (var2 == 1) {
            var6 /= 2;
         }

         if (var2 == 3) {
            var6 = var6 * 3 / 2;
         }

         if (var2 == 4) {
            var6 <<= 1;
         }

         if (var6 <= 0) {
            var6 = 1;
         }

         this.AC();
         int var4;
         Point var5;
         switch(var1) {
         case 0:
            this.AM = 30;
            this.AK = new FrameImage(mImage.createImage("/bg/leaf.png"), 11, 11);

            for(var4 = 0; var4 < var6; ++var4) {
               (var5 = new Point()).x = MainScreen.cameraMain.xCam + this.AM + CRes.random(MotherCanvas.w);
               var5.y = MainScreen.cameraMain.yCam - this.AM + CRes.random(MotherCanvas.h);
               var5.vx = this.AE;
               var5.vy = this.AF;
               this.AB(var5);
               this.AB.addElement(var5);
            }

            return;
         case 1:
            this.AM = 30;
            this.AK = new FrameImage(mImage.createImage("/bg/snow.png"), 7, 7);

            for(var4 = 0; var4 < var6; ++var4) {
               (var5 = new Point()).x = MainScreen.cameraMain.xCam + this.AM + CRes.random(MotherCanvas.w);
               var5.y = MainScreen.cameraMain.yCam - this.AM + CRes.random(MotherCanvas.h);
               var5.vx = this.AE;
               var5.AR = this.AK.nFrame;
               this.AB(var5);
               this.AB.addElement(var5);
            }

            return;
         case 2:
         case 5:
         case 6:
         case 7:
            this.AM = 30;
            if (var1 == 2) {
               this.AK = new FrameImage(mImage.createImage("/bg/flower.png"), 9, 10);
            } else if (var1 == 5) {
               this.AK = new FrameImage(mImage.createImage("/bg/flowermai.png"), 9, 10);
            } else if (var1 == 7) {
               this.AK = new FrameImage(mImage.createImage("/bg/flowerdao.png"), 9, 10);
            } else {
               this.AK = new FrameImage(mImage.createImage("/bg/flowerphao.png"), 7, 7);
            }

            for(var4 = 0; var4 < var6; ++var4) {
               (var5 = new Point()).x = MainScreen.cameraMain.xCam + this.AM + CRes.random(MotherCanvas.w);
               var5.y = MainScreen.cameraMain.yCam - this.AM + CRes.random(MotherCanvas.h);
               var5.vx = this.AE;
               var5.vy = this.AF;
               var5.AR = this.AK.nFrame;
               this.AB(var5);
               this.AB.addElement(var5);
            }

            return;
         case 3:
         case 8:
            this.AM = 10;
            this.AK = new FrameImage(mImage.createImage("/bg/rain.png"), 12, 7);

            for(var4 = 0; var4 < var6; ++var4) {
               (var5 = new Point()).x = MainScreen.cameraMain.xCam + this.AM + CRes.random(MotherCanvas.w);
               var5.y = MainScreen.cameraMain.yCam - this.AM + CRes.random(MotherCanvas.h);
               var5.vx = this.AE;
               this.AB(var5);
               this.AB.addElement(var5);
            }
         case 4:
         default:
         }
      }
   }

   public final void AA(mGraphics var1) {
      int var2;
      Point var3;
      switch(this.AA) {
      case 0:
         for(var2 = 0; var2 < this.AB.size(); ++var2) {
            var3 = (Point)this.AB.elementAt(var2);
            this.AK.drawFrame(AJ[var3.dis][(var3.f + var3.frame) % var3.AR], var3.x, var3.y, 0, 3, var1);
         }

         return;
      case 1:
      case 2:
      case 5:
      case 6:
      case 7:
         for(var2 = 0; var2 < this.AB.size(); ++var2) {
            var3 = (Point)this.AB.elementAt(var2);
            this.AK.drawFrame(var3.frame, var3.x, var3.y, 0, 3, var1);
         }

         return;
      case 3:
      case 8:
         var1.setColor(-1);

         for(var2 = 0; var2 < this.AB.size(); ++var2) {
            var3 = (Point)this.AB.elementAt(var2);
            var1.drawLine(var3.x, var3.y, var3.x - 1, var3.y + var3.AD);
         }

         for(var2 = 0; var2 < this.AC.size(); ++var2) {
            if (!(var3 = (Point)this.AC.elementAt(var2)).AW) {
               this.AK.drawFrame(var3.dis + var3.f / 3 % this.AK.nFrame, var3.x, var3.y, 0, 3, var1);
            }
         }
      case 4:
      default:
      }
   }

   public final void paintSea(mGraphics var1) {
      for(int var2 = 0; var2 < this.AB.size(); ++var2) {
         Point var3;
         if ((var3 = (Point)this.AB.elementAt(var2)).frame == 0) {
            this.AK.drawFrame(var3.f / this.AI % this.AK.nFrame, var3.x, var3.y, var3.dis, 0, var1);
         } else {
            this.AL.drawFrame(var3.f / this.AI % this.AL.nFrame, var3.x, var3.y, var3.dis, 0, var1);
         }
      }

   }

   public final void AA() {
      int var1;
      for(var1 = 0; var1 < this.AB.size(); ++var1) {
         Point var2;
         ++(var2 = (Point)this.AB.elementAt(var1)).f;
         if (var2.f >= var2.fRe) {
            this.AB.removeElement(var2);
            --var1;
         }
      }

      if (GameCanvas.gameTick % 5 == 0) {
         var1 = MainScreen.cameraMain.xCam / LoadMap.wTile - 1;
         int var9 = MainScreen.cameraMain.yCam / LoadMap.wTile - 1;
         if (var1 < 0) {
            var1 = 0;
         }

         if (var9 < 0) {
            var9 = 0;
         }

         int var3 = var1 + GameCanvas.loadmap.maxX + 2;
         int var4 = var9 + GameCanvas.loadmap.maxY + 2;
         if (var3 > GameCanvas.loadmap.mapW) {
            var3 = GameCanvas.loadmap.mapW;
         }

         if (var4 > GameCanvas.loadmap.mapH) {
            var4 = GameCanvas.loadmap.mapH;
         }

         for(var1 = var1; var1 < var3; ++var1) {
            for(int var5 = var9; var5 < var4; ++var5) {
               if (GameCanvas.loadmap.AT[var5 * GameCanvas.loadmap.mapW + var1] == 2 && CRes.random(20) == 0) {
                  int var10001 = var1 * LoadMap.wTile;
                  int var8 = var5 * LoadMap.wTile;
                  int var7 = var10001;
                  Point var10;
                  (var10 = new Point(var7, var8)).frame = CRes.random(2);
                  var8 = CRes.random(20);
                  var10.dis = var8 < 19 ? 0 : 2;
                  var10.fRe = 7 * this.AI;
                  this.AB.addElement(var10);
               }
            }
         }
      }

   }

   private void AC() {
      this.AH = 0;
      this.AG = CRes.random(30, 100);
      switch(this.AA) {
      case 0:
      case 2:
      case 5:
      case 6:
      case 7:
         this.AF = 1;
         this.AE = -CRes.random(2, 5);
         return;
      case 1:
         this.AE = -CRes.random(3, 6);
         return;
      case 3:
         this.AE = -CRes.random(2, 4);
         return;
      case 8:
         this.AE = -CRes.random(3, 5);
      case 4:
      default:
      }
   }

   public final void AB() {
      ++this.AH;
      boolean var1 = false;
      if (this.AH > this.AG) {
         this.AC();
         var1 = true;
      }

      Effect_Map var2 = this;
      int var3;
      Point var4;
      label166:
      switch(this.AA) {
      case 0:
      case 1:
      case 2:
      case 5:
      case 6:
      case 7:
         var3 = 0;

         while(true) {
            if (var3 >= var2.AB.size()) {
               break label166;
            }

            if ((var4 = (Point)var2.AB.elementAt(var3)).x < MainScreen.cameraMain.xCam - var2.AM || var4.y > MainScreen.cameraMain.yCam + MotherCanvas.h + var2.AM) {
               var4 = var2.AA(var4);
            }

            if (var4.x > MainScreen.cameraMain.xCam + MotherCanvas.w + (var2.AM << 1)) {
               var4.x -= MotherCanvas.w + var2.AM;
            }

            ++var3;
         }
      case 3:
      case 8:
         for(var3 = 0; var3 < var2.AB.size(); ++var3) {
            if ((var4 = (Point)var2.AB.elementAt(var3)).frame == 0) {
               if (var4.x < MainScreen.cameraMain.xCam - var2.AM || var4.y > MainScreen.cameraMain.yCam + MotherCanvas.h + var2.AM) {
                  var4 = var2.AA(var4);
               }

               if (var4.x > MainScreen.cameraMain.xCam + MotherCanvas.w + (var2.AM << 1)) {
                  var4.x -= MotherCanvas.w + var2.AM;
               }
            }
         }
      case 4:
      }

      int var7;
      Point var8;
      switch(this.AA) {
      case 0:
         for(var7 = 0; var7 < this.AB.size(); ++var7) {
            var8 = (Point)this.AB.elementAt(var7);
            if (var1) {
               var8.vx = this.AE;
               var8.vy = this.AF;
            }

            var8.update();
            if (var8.dis == 0 && CRes.random(80) == 0) {
               var8.dis = 1;
               var8.AR = AJ[var8.dis].length;
            } else if (var8.dis == 1 && var8.f == 7 && CRes.random(3) == 0) {
               var8.dis = 0;
               var8.AR = AJ[var8.dis].length;
            }
         }

         return;
      case 1:
         for(var7 = 0; var7 < this.AB.size(); ++var7) {
            var8 = (Point)this.AB.elementAt(var7);
            if (var1) {
               var8.vx = this.AE;
               if (var8.vy == 1 && CRes.random(30) == 0) {
                  var8.vy = 0;
               } else if (CRes.random(10) == 0 && var8.vy != 1) {
                  var8.vy = 1;
               }
            }

            var8.update();
         }

         return;
      case 2:
      case 5:
      case 6:
      case 7:
         for(var7 = 0; var7 < this.AB.size(); ++var7) {
            var8 = (Point)this.AB.elementAt(var7);
            if (var1) {
               var8.vx = this.AE;
               var8.vy = this.AF;
            }

            if (CRes.random(5) == 0) {
               var8.frame = CRes.random(var8.AR);
            }

            var8.update();
         }

         return;
      case 3:
      case 8:
         for(var7 = 0; var7 < this.AB.size(); ++var7) {
            var8 = (Point)this.AB.elementAt(var7);
            if (var1) {
               var8.vx = this.AE;
            }

            var8.update();
            if (CRes.random(40) == 0) {
               int var9 = 200;
               int var5 = 1;
               if (GameCanvas.mapBack != null) {
                  var9 = GameCanvas.mapBack.AA;
                  if (GameCanvas.loadmap.maxHMap > var9) {
                     var5 = CRes.random(GameCanvas.loadmap.maxHMap - var9);
                  }
               }

               Point var6;
               (var6 = new Point()).x = MainScreen.cameraMain.xCam + CRes.random(MotherCanvas.w);
               var6.y = var9 + var5;
               var6.frame = 1;
               var6.dis = (CRes.random(4) == 0 ? 0 : 1) << 1;
               this.AC.addElement(var6);
               this.AA(var8);
            }
         }

         for(var7 = 0; var7 < this.AC.size(); ++var7) {
            ++(var8 = (Point)this.AC.elementAt(var7)).f;
            if (var8.f >= 6) {
               if (var8.AW) {
                  this.AC.removeElement(var8);
                  --var7;
               } else {
                  var8.AW = true;
               }
            }
         }
      case 4:
      default:
      }
   }

   private Point AA(Point var1) {
      switch(this.AA) {
      case 0:
      case 1:
      case 2:
      case 5:
      case 6:
      case 7:
         if (CRes.random(3) != 0) {
            var1.x = MainScreen.cameraMain.xCam + MotherCanvas.w + CRes.random(this.AM);
            var1.y = MainScreen.cameraMain.yCam - this.AM + CRes.random(MotherCanvas.h);
         } else {
            var1.x = MainScreen.cameraMain.xCam + this.AM + CRes.random(MotherCanvas.w);
            var1.y = MainScreen.cameraMain.yCam - CRes.random(this.AM);
         }

         this.AB(var1);
         break;
      case 3:
      case 8:
         if (CRes.random(3) == 0) {
            var1.x = MainScreen.cameraMain.xCam + MotherCanvas.w + CRes.random(this.AM);
            var1.y = MainScreen.cameraMain.yCam - this.AM + CRes.random(MotherCanvas.h);
         } else {
            var1.x = MainScreen.cameraMain.xCam + this.AM + CRes.random(MotherCanvas.w);
            var1.y = MainScreen.cameraMain.yCam - CRes.random(this.AM);
         }

         this.AB(var1);
      case 4:
      }

      return var1;
   }

   private Point AB(Point var1) {
      switch(this.AA) {
      case 0:
         var1.AT = CRes.random(10);
         var1.dis = CRes.random(15) == 1 ? 1 : 0;
         var1.AR = AJ[var1.dis].length;
         var1.frame = CRes.random(var1.AR);
         break;
      case 1:
         var1.AT = CRes.random(10);
         var1.frame = CRes.random(var1.AR);
         var1.vy = CRes.random(10) > 0 ? 1 : 0;
         break;
      case 2:
      case 5:
      case 6:
      case 7:
         var1.AT = CRes.random(10);
         var1.frame = CRes.random(var1.AR);
         break;
      case 3:
      case 8:
         var1.AT = CRes.random(10);
         var1.AD = CRes.random(1, this.AD);
         var1.vy = this.AD;
         var1.frame = 0;
      case 4:
      }

      return var1;
   }
}
