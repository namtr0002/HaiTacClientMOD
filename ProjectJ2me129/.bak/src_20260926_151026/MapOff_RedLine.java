public final class MapOff_RedLine {
   public static mImage[] mImgMapOffline;
   public static mVector AB = new mVector("MapOff_RedLine.SongTuong");
   public static mVector AC = new mVector("MapOff_RedLine.SongBien");
   public static mVector AD = new mVector("MapOff_RedLine.DaBien");
   public static mVector AE = new mVector("MapOff_RedLine.EffDie");
   public static mVector AF = new mVector("MapOff_RedLine.EffSongTuong");
   public static int AG;
   public static int AH;
   private static int AQ;
   private static int AR;
   public static int AI;
   private static int AS = 0;
   public static int AJ = 0;
   public static long AK;
   public static int AL;
   public static int xHardCodeMapRedLine;
   public static int AN;
   private static int[] AT;
   public static boolean isFinish;
   public static int AP;
   private static int AU;

   static {
      AL = MotherCanvas.h / 5 << 2;
      xHardCodeMapRedLine = MotherCanvas.w / 3;
      AN = 6;
      AT = new int[]{2, 0, 1, 2, 3};
      isFinish = false;
      AP = MotherCanvas.h - 20;
      AU = 0;
   }

   public static void setTypeMoveredLine(byte var0) {
      GameScreen.player.SE = var0;
      if (var0 == 1) {
         Point var1;
         (var1 = new Point()).dis = GameScreen.player.SB;
         var1.x = MotherCanvas.w + 48;
         var1.y = AL - 48 + var1.dis * 24 - (MotherCanvas.w - xHardCodeMapRedLine) / 48 * 10;
         var1.frame = CRes.random(2);
         AD.addElement(var1);
      } else {
         if (var0 == 2) {
            Player.SG = 2;
            GameScreen.player.SD = 0;
         }

      }
   }

   public static void AA(mGraphics var0) {
      if (AS > 0) {
         --AS;
      } else {
         int var2;
         int var3;
         int var4;
         int var5;
         int var6;
         mGraphics var9;
         if (isFinish) {
            var9 = var0;
            if (GameCanvas.mapBack != null) {
               GameCanvas.mapBack.paint(var0);
            }

            if (mImgMapOffline == null) {
               AD();
            }

            var2 = MotherCanvas.w / 48 + 2;
            var3 = 0 + AQ % 48;
            var4 = AH + AR % 10;

            for(var5 = 0; var5 < var2; ++var5) {
               var9.drawRegion(mImgMapOffline[2], 0, 0, 48, 128, 2, var3 + var5 * 48, var4 - 128 + var5 * 10, 0);
            }

            var5 = 0;

            for(var6 = 0; var6 < var2; ++var6) {
               var9.drawRegion(mImgMapOffline[5], var5 % 3 * 24, 0, 48, 24, 0, var3 + var6 * 48, var4 + var6 * 10 - 6, 0);

               for(int var7 = 0; var7 <= (MotherCanvas.h - var4 + var6 * 10 + 6) / 24; ++var7) {
                  ++var5;
                  var9.drawRegion(mImgMapOffline[5], var5 % 3 * 24, 0, 48, 24, 0, var3 + var6 * 48, var4 + var6 * 10 - 6 + (var7 + 1) * 24, 0);
               }

               ++var5;
            }

            Point var8;
            for(var6 = 0; var6 < AB.size(); ++var6) {
               var8 = (Point)AB.elementAt(var6);
               var9.drawRegion(mImgMapOffline[7], 0, var8.frame * 24, 24, 24, 0, var3 + var8.x, var4 + var8.y - 24, 0);
            }

            for(var6 = 0; var6 < AC.size(); ++var6) {
               if ((var8 = (Point)AC.elementAt(var6)).AX) {
                  var9.drawRegion(mImgMapOffline[7], 0, var8.frame * 24, 24, 24, 0, var8.x2 + var8.x + var8.AK, var8.y2 + var8.y + var8.AL, 0);
               }
            }

            for(var6 = 0; var6 < AF.size(); ++var6) {
               var8 = (Point)AF.elementAt(var6);
               var9.drawRegion(mImgMapOffline[6], 0, AT[var8.fSmall] * 48, 48, 48, 0, var8.x + var8.x2, var8.y + var8.y2 - 3, 33);
            }

            GameScreen.player.paint(var9);
         } else {
            if (mImgMapOffline == null) {
               AD();
            }

            if (GameCanvas.mapBack != null) {
               GameCanvas.mapBack.paint(var0);
            }

            int var1 = MotherCanvas.w / 48 + 2;
            var2 = 0 + AQ % 48;
            var3 = AH + AR % 10;

            for(var4 = 0; var4 < var1; ++var4) {
               var0.drawRegion((mImage)mImgMapOffline[2], var2 + var4 * 48, var3 - 128 - var4 * 10, 0);
            }

            var4 = 0;

            for(var5 = 0; var5 < var1; ++var5) {
               var0.drawRegion(mImgMapOffline[5], var4 % 3 * 24, 0, 48, 24, 0, var2 + var5 * 48, var3 - var5 * 10 - 6, 0);

               for(var6 = 0; var6 <= (MotherCanvas.h - var3 + var5 * 10 + 6) / 24; ++var6) {
                  ++var4;
                  var0.drawRegion(mImgMapOffline[5], var4 % 3 * 24, 0, 48, 24, 0, var2 + var5 * 48, var3 - var5 * 10 - 6 + (var6 + 1) * 24, 0);
               }

               ++var4;
            }

            Point var10;
            for(var5 = 0; var5 < AB.size(); ++var5) {
               var10 = (Point)AB.elementAt(var5);
               var0.drawRegion(mImgMapOffline[0], 0, var10.frame * 24, 24, 24, 0, var2 + var10.x, var3 + var10.y - 24, 0);
            }

            for(var5 = 0; var5 < AC.size(); ++var5) {
               if ((var10 = (Point)AC.elementAt(var5)).AX) {
                  var0.drawRegion(mImgMapOffline[1], 0, var10.frame * 24, 24, 24, 0, var10.x2 + var10.x + var10.AK, var10.y2 + var10.y + var10.AL, 0);
               }
            }

            for(var5 = 0; var5 < AD.size(); ++var5) {
               if ((var10 = (Point)AD.elementAt(var5)).dis < GameScreen.player.SC) {
                  AA(var0, var10);
               }
            }

            for(var5 = 0; var5 < AF.size(); ++var5) {
               var10 = (Point)AF.elementAt(var5);
               var0.drawRegion(mImgMapOffline[6], 0, AT[var10.fSmall] * 48, 48, 48, 0, var10.x + var10.x2, var10.y + var10.y2 - 3, 33);
            }

            GameScreen.player.paint(var0);

            for(var5 = 0; var5 < AD.size(); ++var5) {
               if ((var10 = (Point)AD.elementAt(var5)).dis >= GameScreen.player.SC) {
                  AA(var0, var10);
               }
            }

            for(var5 = 0; var5 < AE.size(); ++var5) {
               var10 = (Point)AE.elementAt(var5);
               var0.drawRegion(mImgMapOffline[6], 0, AT[var10.fSmall] * 48, 48, 48, 0, var10.x, var10.y, 33);
            }

            if (ReadMessenge.AH != null) {
               var0.setColor(0);
               var0.drawRect(MotherCanvas.hw - 40 - 1, AP - 20 - 1, 81, 8);
               if ((var5 = (int)((long)AI - (GameCanvas.timeNow - AK))) <= 0 && GameScreen.player.SH == 1) {
                  var5 = 0;
               }

               Interface_Game.AA(var0, (byte)1, var5, AI, MotherCanvas.hw - 40, AP - 20, 0, 7, 80, 4, false, 0, false, 0);
               var9 = var0;
               var2 = MotherCanvas.hw - ReadMessenge.AH.length / 2 * Interface_Game.BQ;
               var3 = ReadMessenge.AH.length;

               for(var4 = 0; var4 < var3; ++var4) {
                  var6 = Interface_Game.BQ;
                  if (var4 == 0) {
                     var6 = 0;
                  } else if (var4 == var3 - 1) {
                     var6 = Interface_Game.BQ << 1;
                  }

                  var9.drawRegion(AvMain.imgCombo, var6, 0, Interface_Game.BQ, Interface_Game.BQ, 0, var2 + var4 * Interface_Game.BQ - Interface_Game.BQ / 2, AP - Interface_Game.BQ / 2, 0);
                  var9.drawRegion((mImage)ReadMessenge.AH[var4], var2 + var4 * Interface_Game.BQ, AP, 3);
                  if (var4 <= Player.RC) {
                     var9.drawRegion(AvMain.imgDelay, 0, 0, Interface_Game.BQ, Interface_Game.BQ, 0, var2 + var4 * Interface_Game.BQ - Interface_Game.BQ / 2, AP - Interface_Game.BQ / 2, 0);
                  }
               }
            }

            if (GameCanvas.isTouch) {
               var0.drawRegion((mImage)Interface_Game.imgMove[0], Interface_Game.xPointMove, Interface_Game.yPointMove, 3);

               for(var5 = 0; var5 < 4; ++var5) {
                  if (Interface_Game.timePointer > 0 && GameScreen.interfaceGame.AR[var5] == Interface_Game.keyPoint) {
                     Interface_Game var10000 = GameScreen.interfaceGame;
                     Interface_Game.AA(var0, var5);
                  }
               }
            }

         }
      }
   }

   private static void AA(mGraphics var0, Point var1) {
      if (mImgMapOffline == null) {
         AD();
      }

      var0.drawRegion((mImage)mImgMapOffline[3 + var1.frame], var1.x + var1.x2, var1.y + var1.y2 + 10, 33);
      var0.drawRegion(mImgMapOffline[6], 0, (2 + GameCanvas.gameTick / 3 % 2) * 48, 48, 48, 0, var1.x + var1.x2 - 24 + 6, var1.y + var1.y2 + 14, 33);
      if (var1.AX) {
         var0.drawRegion(mImgMapOffline[6], 0, AT[var1.fSmall] * 48, 48, 48, 0, var1.x + var1.x2 - 24 + var1.fSmall * 3, var1.y + var1.y2 + 14, 33);
      }

   }

   private static void AD() {
      mImgMapOffline = new mImage[8];

      for(int var0 = 0; var0 < mImgMapOffline.length; ++var0) {
         if (var0 == 5) {
            mImgMapOffline[var0] = mImage.createImage("/bg/b03.png");
         } else {
            mImgMapOffline[var0] = mImage.createImage("/bg/redline" + var0 + ".png");
         }
      }

   }

   public static void AA() {
      Point var10000;
      int var0;
      Point var1;
      Point var2;
      if (isFinish) {
         if (GameCanvas.mapBack != null) {
            GameCanvas.mapBack.AB();
         }

         AR = (AQ -= AN) * 5 / 24;
         AF();
         AE();
         if (CRes.random(10) == 0) {
            (var2 = new Point()).x = 0 + CRes.random(MotherCanvas.w);
            var2.y = AH + var2.x / 24 * 5;
            AF.addElement(var2);
         }

         for(var0 = 0; var0 < AF.size(); ++var0) {
            var10000 = var1 = (Point)AF.elementAt(var0);
            var10000.x2 -= AN;
            var1.y2 = var1.x2 * 5 / 24;
            if (GameCanvas.gameTick % 2 == 0) {
               ++var1.fSmall;
            }

            if (var1.fSmall >= AT.length) {
               AF.removeElement(var1);
               --var0;
            }
         }

         GameScreen.player.CB();
      } else {
         if (GameCanvas.mapBack != null) {
            GameCanvas.mapBack.AB();
         }

         if (Interface_Game.timePointer > 0) {
            --Interface_Game.timePointer;
         }

         AR = -((AQ -= AN) * 5) / 24;
         AF();
         AE();

         for(var0 = 0; var0 < AE.size(); ++var0) {
            (var1 = (Point)AE.elementAt(var0)).update();
            if (GameCanvas.gameTick % 2 == 0) {
               ++var1.fSmall;
            }

            if (var1.fSmall >= AT.length) {
               AE.removeElement(var1);
               --var0;
            }
         }

         for(var0 = 0; var0 < AF.size(); ++var0) {
            var10000 = var1 = (Point)AF.elementAt(var0);
            var10000.x2 -= AN;
            var1.y2 = -(var1.x2 * 5) / 24;
            if (GameCanvas.gameTick % 2 == 0) {
               ++var1.fSmall;
            }

            if (var1.fSmall >= AT.length) {
               AF.removeElement(var1);
               --var0;
            }
         }

         if (AU > 0) {
            --AU;
         } else if (CRes.random(80) == 0 && Player.SG == 0) {
            AU = 70;
            (var2 = new Point()).dis = GameScreen.player.SB;
            var2.x = MotherCanvas.w + 48;
            var2.y = AL - 48 + var2.dis * 24 - (MotherCanvas.w - xHardCodeMapRedLine) / 48 * 10;
            var2.frame = CRes.random(2);
            AD.addElement(var2);
         }

         for(var0 = 0; var0 < AD.size(); ++var0) {
            var10000 = var1 = (Point)AD.elementAt(var0);
            var10000.x2 -= AN;
            var1.y2 = -(var1.x2 * 5) / 24;
            if (var1.AX) {
               if (GameCanvas.gameTick % 3 == 0) {
                  ++var1.fSmall;
                  if (var1.fSmall >= AT.length) {
                     var1.AX = false;
                  }
               }
            } else if (CRes.random(4) == 0) {
               var1.fSmall = 0;
               var1.AX = true;
            }

            if (var1.x2 < -(MotherCanvas.w + 72)) {
               AD.removeElement(var1);
               --var0;
            }
         }

         GameScreen.player.CA();
         if (GameCanvas.isKeyPressed(1)) {
            GlobalService.getInstance().AC((byte)0, (byte)1);
            GameCanvas.clearKeyPressed(1);
            ++Player.RC;
         } else if (GameCanvas.isKeyPressed(2)) {
            GlobalService.getInstance().AC((byte)0, (byte)2);
            GameCanvas.clearKeyPressed(2);
            ++Player.RC;
         } else if (GameCanvas.isKeyPressed(3)) {
            GlobalService.getInstance().AC((byte)0, (byte)3);
            GameCanvas.clearKeyPressed(3);
            ++Player.RC;
         } else if (GameCanvas.isKeyPressed(0)) {
            GlobalService.getInstance().AC((byte)0, (byte)0);
            GameCanvas.clearKeyPressed(0);
            ++Player.RC;
         }

         if ((Player.SG == 1 || Player.SG == 11) && CRes.random(3) == 0) {
            (var2 = new Point()).x = GameScreen.player.x + 10 + CRes.random_Am_0(30);
            var2.y = GameScreen.player.y + 18 + CRes.random_Am_0(4);
            var2.vx = GameScreen.player.vx;
            var2.vy = GameScreen.player.vy;
            AE.addElement(var2);
         }

         if (CRes.random(10) == 0) {
            (var2 = new Point()).x = 0 + CRes.random(MotherCanvas.w);
            var2.y = AH - var2.x / 24 * 5;
            AF.addElement(var2);
         }

         if (GameScreen.player.SE == 0) {
            if (AJ > 0) {
               --AJ;
               return;
            }

            if ((long)AI - (GameCanvas.timeNow - AK) <= 0L) {
               GlobalService.getInstance().AC((byte)0, (byte)10);
               AJ = 40;
            }
         }

      }
   }

   private static void AE() {
      for(int var0 = 0; var0 < AC.size(); ++var0) {
         Point var1;
         ++(var1 = (Point)AC.elementAt(var0)).f;
         if (var1.x2 == 0) {
            var1.x2 = 0;
            var1.y2 = AH;
            var1.AK = 0;
            var1.AL = 0;
         }

         if (var1.AX) {
            if (var1.f % 3 == 0) {
               ++var1.frame;
            }

            var1.AK -= 3;
            var1.AL = -(var1.AK * 5) / 24;
         } else if (CRes.random(100) == 0) {
            var1.AX = true;
            var1.x2 = 0;
            var1.y2 = AH;
            var1.AK = 0;
            var1.AL = 0;
         }

         if (var1.frame >= 7) {
            var1.frame = 0;
            if (CRes.random(100) == 0) {
               var1.AX = true;
               var1.x2 = 0;
               var1.y2 = AH;
               var1.AK = 0;
               var1.AL = 0;
            } else {
               var1.AX = false;
            }
         }
      }

   }

   private static void AF() {
      for(int var0 = 0; var0 < AB.size(); ++var0) {
         Point var1;
         ++(var1 = (Point)AB.elementAt(var0)).f;
         if (var1.AX) {
            if (var1.f % 3 == 0) {
               ++var1.frame;
            }
         } else if (CRes.random(5) == 0) {
            var1.AX = true;
         }

         if (var1.frame >= 7) {
            var1.frame = 0;
            if (CRes.random(3) == 0) {
               var1.AX = true;
            } else {
               var1.AX = false;
            }
         }
      }

   }

   public static void AB() {
      if (GameCanvas.isPointerSelect && GameCanvas.AD(Interface_Game.xPointMove - 2 * Interface_Game.wArrowMove, Interface_Game.yPointMove - 2 * Interface_Game.wArrowMove, Interface_Game.wArrowMove << 2, Interface_Game.wArrowMove << 2)) {
         int var0;
         byte var1;
         if ((var0 = CRes.AA(GameCanvas.AY - Interface_Game.xPointMove, GameCanvas.AZ - Interface_Game.yPointMove)) > 45 && var0 <= 135) {
            var1 = 3;
         } else if (var0 > 135 && var0 <= 225) {
            var1 = 0;
         } else if (var0 > 225 && var0 <= 315) {
            var1 = 2;
         } else {
            var1 = 1;
         }

         GameCanvas.AH();
         GameCanvas.isPointerDown = true;
         GameCanvas.isPointerSelect = false;
         Interface_Game.keyPoint = GameScreen.interfaceGame.AR[var1];
         Interface_Game.timePointer = 3;
         GameCanvas.AL[Interface_Game.keyPoint] = true;
      }

   }

   public static void AC() {
      ReadMessenge var10000 = GameCanvas.readMessage;
      ReadMessenge.LoadRedLine(true);
      AS = 5;
   }
}
