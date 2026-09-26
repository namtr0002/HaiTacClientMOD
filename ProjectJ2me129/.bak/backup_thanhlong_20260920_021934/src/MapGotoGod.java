public final class MapGotoGod {
   private static mVector AB = new mVector("MapGotoGod.Axe");
   private static int AC = 24;
   private static int AD = 14;
   private static mImage AE;
   private static int AF;
   private static int AG;
   private static int AH;
   private static int AI;
   private static MainImage AJ;
   private static MainImage AK;
   private static int[] AL;
   private static byte[] AM;
   private static int AN;
   private static int AO;
   public static int AA;
   private static int AP;
   private static int AQ;
   private static int AR;
   private static int AS;
   private static int AT;
   private static byte AU;
   private static byte AV;
   private static int AW;
   private static byte AX;
   private static byte AY;

   static {
      AN = MotherCanvas.h / 6 << 2;
      AO = MotherCanvas.w / 2;
      AA = 6;
      AP = MotherCanvas.w / 6;
      AQ = MotherCanvas.h - 20;
   }

   public static void setPos() {
      AA = 6;
      AT = 0;
      AU = 0;
      AV = 0;
      AW = 15;
      AX = 0;
      AY = 0;
      MapOff_RedLine.AI = 0;
      MapOff_RedLine.AK = 0L;
      AB.removeAllElements();
      GameCanvas.loadMapScr.mItemMap = null;
      MainScreen.cameraMain.xCam = 0;
      MainScreen.cameraMain.yCam = 0;
      GameScreen.player.x = AO;
      GameScreen.player.y = AN;
      GameScreen.player.Dir = 2;
      GameScreen.player.type_left_right = 2;
      GameScreen.player.boatSea = new Boat(GameScreen.player.ID, GameScreen.player.x, GameScreen.player.y, 0, (byte)GameScreen.player.type_left_right);
      GameScreen.player.boatSea.AA(GameScreen.player.BO, GameScreen.player.typePirate);
      GameScreen.player.setSpeed(2, 2);
      GameScreen.player.NR = true;
      ObjectData.getImageOther((short)AD, (short)20);
      if (LoadMapScreen.isMapSky != 1) {
         LoadMapScreen.isMapSky = 1;
         LoadImageStatic.loadImageEffBoat();
      }

      AH = MotherCanvas.w / AC + 1;
      AI = MotherCanvas.h / AC + 1;
      AF = 45;
      AG = 18;
      AJ = null;
      AK = null;
      AS = (AM = new byte[]{8, 11, 8, 8, 5, 5, 20, 20, 13, 13, 8, 8, 4, 4, 4, 7, 4, 4, 7, 7, 25, 42, 9, 12, 5, 5, 8, 8, 9, 12})[AD << 1];
      AR = AM[(AD << 1) + 1];
      AL = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 23, 24, 25, 24, 25, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 11, 9, 10, 9, 10, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 20, 21, 22, 21, 22, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19};
   }

   public static void AA(byte var0) {
      GameScreen.player.SH = var0;
   }

   public static void AB() {
   }

   public static void AC() {
      if (GameCanvas.mapBack != null) {
         GameCanvas.mapBack.AA();
      }

      AT -= AA;
      if (GameScreen.player.SH == 0) {
         GameScreen.player.SH = 1;
      } else if (GameScreen.player.SH == 1) {
         if (CRes.abs(GameScreen.player.x - GameScreen.player.toX) < 4 && CRes.abs(GameScreen.player.y - GameScreen.player.toY) < 4 && CRes.random(25) == 0 && AV == 0) {
            if (AU == 0) {
               AU = 1;
               GameScreen.player.toX = AO + AP;
               AV = 1;
            } else {
               AU = 0;
               GameScreen.player.toX = AO - AP;
               AV = 1;
            }
         }
      } else if (GameScreen.player.SH == 2) {
         if (AY == 0) {
            GameScreen.player.toX = AO;
            AV = 1;
            AW = 15;
            AY = 1;
         }
      } else {
         int var10000 = GameScreen.player.SH;
      }

      if (AV != 0) {
         if (AW > 0) {
            --AW;
         } else {
            AV = 0;
            AW = 15;
            Point var0;
            (var0 = new Point()).dis = 0;
            if (GameScreen.player.SH == 2) {
               var0.x2 = -MotherCanvas.w / 2;
               var0.subType = 1;
            } else if (AU == 0) {
               var0.x2 = -MotherCanvas.w / 2 + (AP << 1);
            } else {
               var0.x2 = -MotherCanvas.w / 2 - (AP << 1);
            }

            AB.addElement(var0);
         }
      }

      for(int var6 = 0; var6 < AB.size(); ++var6) {
         Point var1;
         if ((var1 = (Point)AB.elementAt(var6)).subType == 1 && var1.y + var1.y2 + AE.height >= AN) {
            if (AX == 0) {
               AX = 1;
               AA = 0;
               GameScreen.player.vx = -4;
               GameScreen.player.vy = -4;
            }
         } else {
            var1.dis += 6;
         }

         int var5 = MotherCanvas.w / 2;
         int var4 = var1.dis;
         int var3 = MotherCanvas.h;
         int var2 = MotherCanvas.w / 2;
         var3 /= 2;
         var4 -= 90;
         var2 += CRes.getcos(var4) * var5 / 1000;
         var3 += CRes.getsin(var4) * var5 / 1000;
         Point var7 = new Point(var2, var3);
         var1.x = var7.x;
         var1.y = var7.y;
         if (var1.dis >= 225) {
            AB.removeElement(var1);
            --var6;
         }
      }

      if (GameScreen.player.SH == 1) {
         if (GameCanvas.isKeyPressed(1)) {
            GlobalService.getInstance().AC((byte)3, (byte)1);
            GameCanvas.clearKeyPressed(1);
            ++Player.RC;
         } else if (GameCanvas.isKeyPressed(2)) {
            GlobalService.getInstance().AC((byte)3, (byte)2);
            GameCanvas.clearKeyPressed(2);
            ++Player.RC;
         } else if (GameCanvas.isKeyPressed(3)) {
            GlobalService.getInstance().AC((byte)3, (byte)3);
            GameCanvas.clearKeyPressed(3);
            ++Player.RC;
         } else if (GameCanvas.isKeyPressed(0)) {
            GlobalService.getInstance().AC((byte)3, (byte)0);
            GameCanvas.clearKeyPressed(0);
            ++Player.RC;
         }
      }

      GameScreen.player.AC(AX);
      if (Interface_Game.timePointer > 0) {
         --Interface_Game.timePointer;
      }

   }

   public static void AA(mGraphics var0) {
      if (AE == null) {
         (AE = mImage.createImage("/bg/axe.png")).setDefault();
      }

      if (GameCanvas.mapBack != null) {
         GameCanvas.mapBack.paint(var0);
      }

      int var1 = MainScreen.cameraMain.xCam / AC - 1;
      int var2 = MainScreen.cameraMain.yCam / AC - 1;
      if (var1 < 0) {
         var1 = 0;
      }

      if (var2 < 0) {
         var2 = 0;
      }

      int var3 = var1 + AH + 2;
      int var4 = var2 + AI + 2;
      if (var3 > AF) {
         var3 = AF;
      }

      if (var4 > AG) {
         var4 = AG;
      }

      if ((AK == null || AK.img == null) && !GameCanvas.lowGraphic) {
         AK = ObjectData.getImageOther((short)AD, (short)70);
      }

      if (AJ != null && AJ.img != null) {
         int var6;
         for(var1 = var1; var1 < var3; ++var1) {
            for(int var5 = var2; var5 < var4; ++var5) {
               if ((var6 = var5 * AF + var1) < AL.length) {
                  var6 = AL[var6] - 1;

                  int var7;
                  for(var7 = var1 * AC + AT; var7 <= -AC; var7 += var3 * AC) {
                  }

                  if (!GameCanvas.lowGraphic && var6 >= AS - 1 && var6 < AR - 1 && GameCanvas.gameTick % 14 < 7) {
                     int var8 = 0;
                     if (GameCanvas.gameTick / 14 % 2 == 0) {
                        var8 = AR - AS;
                     }

                     if (AK != null && AK.img != null) {
                        var0.drawRegion(AK.img, (var8 + var6 - (AS - 1)) / 10 * AC, (var8 + var6 - (AS - 1)) % 10 * AC, AC, AC, 0, var7, var5 * AC, 0);
                     }
                  } else if (var6 >= 0 && AJ != null && AJ.img != null) {
                     var0.drawRegion(AJ.img, var6 / 10 * AC, var6 % 10 * AC, AC, AC, 0, var7, var5 * AC, 0);
                  }
               }
            }
         }

         Point var10;
         for(var1 = 0; var1 < AB.size(); ++var1) {
            if ((var10 = (Point)AB.elementAt(var1)).y < GameScreen.player.y) {
               AA(var0, var10);
            }
         }

         GameScreen.player.AE(var0);

         for(var1 = 0; var1 < AB.size(); ++var1) {
            if ((var10 = (Point)AB.elementAt(var1)).y >= GameScreen.player.y) {
               AA(var0, var10);
            }
         }

         GameCanvas.resetTrans(var0);
         if (ReadMessenge.AH != null) {
            var0.setColor(0);
            var0.drawRect(MotherCanvas.hw - 40 - 1, AQ - 20 - 1, 81, 8);
            if ((var1 = (int)((long)MapOff_RedLine.AI - (GameCanvas.timeNow - MapOff_RedLine.AK))) <= 0 && GameScreen.player.SH == 1) {
               var1 = 0;
            }

            Interface_Game.AA(var0, (byte)1, var1, MapOff_RedLine.AI, MotherCanvas.hw - 40, AQ - 20, 0, 7, 80, 4, false, 0, false, 0);
            mGraphics var9 = var0;
            var2 = MotherCanvas.hw - ReadMessenge.AH.length / 2 * Interface_Game.BQ;
            var3 = ReadMessenge.AH.length;

            for(var4 = 0; var4 < var3; ++var4) {
               var6 = Interface_Game.BQ;
               if (var4 == 0) {
                  var6 = 0;
               } else if (var4 == var3 - 1) {
                  var6 = Interface_Game.BQ << 1;
               }

               var9.drawRegion(AvMain.imgCombo, var6, 0, Interface_Game.BQ, Interface_Game.BQ, 0, var2 + var4 * Interface_Game.BQ - Interface_Game.BQ / 2, AQ - Interface_Game.BQ / 2, 0);
               var9.drawRegion((mImage)ReadMessenge.AH[var4], var2 + var4 * Interface_Game.BQ, AQ, 3);
               if (var4 <= Player.RC) {
                  var9.drawRegion(AvMain.imgDelay, 0, 0, Interface_Game.BQ, Interface_Game.BQ, 0, var2 + var4 * Interface_Game.BQ - Interface_Game.BQ / 2, AQ - Interface_Game.BQ / 2, 0);
               }
            }
         }

         if (GameCanvas.isTouch) {
            var0.drawRegion((mImage)Interface_Game.imgMove[0], Interface_Game.xPointMove, Interface_Game.yPointMove, 3);

            for(var1 = 0; var1 < 4; ++var1) {
               if (Interface_Game.timePointer > 0 && GameScreen.interfaceGame.AR[var1] == Interface_Game.keyPoint) {
                  Interface_Game var10000 = GameScreen.interfaceGame;
                  Interface_Game.AA(var0, var1);
               }
            }
         }

      } else {
         AJ = ObjectData.getImageOther((short)AD, (short)20);
         var0.setColor(-1331884);
         var0.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
      }
   }

   private static void AA(mGraphics var0, Point var1) {
      int var2 = var1.x + var1.x2;
      int var3 = var1.y + var1.y2 + AE.height / 2;

      for(int var4 = -1; var4 < 2; ++var4) {
         var0.drawLine(MotherCanvas.w / 4 + var1.x2 + var4, -MotherCanvas.h / 2 + var4, var2 + var4, var3 - AE.height / 2 + var4);
      }

      var0.drawRegion((mImage)AE, var2, var3, 33);
   }
}
