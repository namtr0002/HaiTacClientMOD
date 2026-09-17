public final class MapGotoSky {
   private static int AC = 140;
   private static int AD;
   private static int AE;
   private static int AF;
   private static int AG;
   private static int AH;
   private static int AI;
   private static mVector AJ;
   private static mVector AK;
   private static mVector AL;
   private static mVector AM;
   private static mVector AN;
   private static mVector AO;
   private static mVector AP;
   private static mVector AQ;
   private static mVector AR;
   private static FrameImage AS;
   private static FrameImage AT;
   private static FrameImage AU;
   private static FrameImage AV;
   private static FrameImage AW;
   private static FrameImage AX;
   private static FrameImage AY;
   private static mImage[] AZ;
   private static mImage[] BA;
   private static int[] BB;
   private static int[] BC;
   private static int[][] BD;
   private static int[][] BE;
   private static int[][] BF;
   public static boolean isBeginEffBoat;
   public static boolean AB;
   private static int BG;
   private static int BH;

   static {
      AD = MotherCanvas.h - 100;
      AG = 0;
      AJ = new mVector("MapGotoSky.Song_Ria");
      AK = new mVector("MapGotoSky.Song_Giua");
      AL = new mVector("MapGotoSky.Song_Nho");
      AM = new mVector("MapGotoSky.Song_Keo");
      AN = new mVector("MapGotoSky.Song_Bien");
      AO = new mVector("MapGotoSky.Song_Bien_Nho");
      AP = new mVector("MapGotoSky.May");
      AQ = new mVector("MapGotoSky.Die");
      AR = new mVector("MapGotoSky.May_Goto");
      AZ = new mImage[3];
      BA = new mImage[3];
      BB = new int[]{0, 1, 1, 1, 1, 1, 2};
      BC = new int[]{0, 2, 2, 2, 2, 2, 4};
      BD = new int[][]{{180, 90}, {220, 120}, {200, 190}, {244, 270}, {184, 320}};
      BF = new int[][]{{244, 420}, {184, 600}};
      isBeginEffBoat = false;
      AB = false;
      BG = 0;
   }

   public static void setPos() {
      AH = 700;
      AI = 480;
      AC = 140;
      AD = AH - 120;
      AE = 170;
      AF = 504;
      AS = new FrameImage(mImage.createImage("/bg/eff_sky0.png"), 24, 24);
      AT = new FrameImage(mImage.createImage("/bg/eff_sky1.png"), 24, 24);
      AU = new FrameImage(mImage.createImage("/bg/eff_sky2.png"), 36, 32);
      AV = new FrameImage(mImage.createImage("/bg/eff_sky3.png"), 24, 24);
      AW = new FrameImage(mImage.createImage("/bg/eff_sky4.png"), 24, 24);
      AX = new FrameImage(mImage.createImage("/bg/sea1.png"), 24, 24);
      AY = new FrameImage(mImage.createImage("/bg/sea2.png"), 24, 24);
      new FrameImage(mImage.createImage("/bg/redline6.png"), 48, 48);

      int var0;
      for(var0 = 0; var0 < AZ.length; ++var0) {
         AZ[var0] = mImage.createImage("/bg/cloud" + var0 + ".png");
      }

      for(var0 = 0; var0 < BA.length; ++var0) {
         BA[var0] = mImage.createImage("/bg/goto" + var0 + ".png");
      }

      AJ.removeAllElements();

      Point var1;
      Point var2;
      for(var0 = 0; var0 <= AF / 24; ++var0) {
         (var1 = new Point(AC + 1, AD - 24 - var0 * 24)).dis = 2;
         var1.f = CRes.random(AS.nFrame << 1);
         var1.vy = -8;
         AJ.addElement(var1);
         (var2 = new Point(AC - 1 + AE, AD - 24 - var0 * 24)).dis = 0;
         var2.f = CRes.random(AS.nFrame << 1);
         var2.vy = -8;
         AJ.addElement(var2);
      }

      AK.removeAllElements();

      for(var0 = 0; var0 < MotherCanvas.h / 60; ++var0) {
         var1 = new Point(AC + 12, AD - 15);
         if (var0 > 0) {
            var1.y -= var0 * 80 + CRes.random(40);
         }

         var1.frame = 0;
         var1.vy = -8;
         AK.addElement(var1);
      }

      AL.removeAllElements();

      for(var0 = 0; var0 < MotherCanvas.h / 60; ++var0) {
         (var1 = new Point(AC - 24 + CRes.random(AE + 48), AD - 15 - CRes.random(AF))).frame = 0;
         var1.vy = -CRes.random(7, 12);
         AL.addElement(var1);
      }

      AM.removeAllElements();

      for(var0 = 0; var0 < 12; ++var0) {
         if ((var1 = new Point(AC - 24 + CRes.random(AE + 48), AD + 40)).x - AC < (AE + 36) / 3) {
            var1.frame = 1;
            var1.dis = 0;
            var1.vx = 2;
         } else if (var1.x - AC > (AE + 36) / 3 << 1) {
            var1.frame = 1;
            var1.dis = 2;
            var1.vx = CRes.random_Am_0(2);
         } else {
            var1.frame = 0;
            var1.dis = 0;
            var1.vx = -2;
         }

         var1.vy = -7;
         AM.addElement(var1);
      }

      AN.removeAllElements();

      for(var0 = 0; var0 < AE / LoadMap.wTile + 5; ++var0) {
         (var1 = new Point((AC / 24 - 2) * 24 + var0 * 24, AD / 24 * 24)).frame = CRes.random(AX.nFrame + 1 << 1);
         var1.dis = CRes.random(5) == 0 ? 0 : 2;
         AN.addElement(var1);
         (var2 = new Point((AC / 24 - 2) * 24 + var0 * 24, (AD / 24 + 1) * 24)).frame = CRes.random(AX.nFrame + 1 << 1);
         var2.dis = CRes.random(5) == 0 ? 0 : 2;
         AN.addElement(var2);
         if (var0 != 0 && var0 != AE / LoadMap.wTile + 4) {
            (var1 = new Point((AC / 24 - 2) * 24 + var0 * 24, (AD / 24 + 2) * 24)).frame = CRes.random(AX.nFrame + 1 << 1);
            var1.dis = CRes.random(5) == 0 ? 0 : 2;
            AN.addElement(var1);
            if (var0 <= 2 || var0 >= AE / LoadMap.wTile) {
               (var1 = new Point((AC / 24 - 2) * 24 + var0 * 24, (AD / 24 - 1) * 24)).frame = CRes.random(AX.nFrame + 1 << 1);
               var1.dis = CRes.random(5) == 0 ? 0 : 2;
               AN.addElement(var1);
            }
         }
      }

      AO.removeAllElements();

      for(var0 = 0; var0 <= AI / LoadMap.wTile; ++var0) {
         for(int var3 = 0; var3 <= 160 / LoadMap.wTile; ++var3) {
            (var2 = new Point(var0 * LoadMap.wTile, AH - 160 + var3 * LoadMap.wTile)).frame = CRes.random(14);
            var2.fSmall = CRes.random(6);
            var2.dis = CRes.random(5);
            var2.vx = 0;
            var2.vy = 0;
            AO.addElement(var2);
         }
      }

      AP.removeAllElements();
      AQ.removeAllElements();
      BE = mSystem.new_M_Int(BD.length, 2);

      for(var0 = 0; var0 < BD.length; ++var0) {
         BE[var0] = new int[2];
         BE[var0][0] = BD[var0][0];
         BE[var0][1] = AH - 30 - BD[var0][1];
      }

      for(var0 = 0; var0 <= 10; ++var0) {
         (var1 = new Point(var0 * 45, AH - 30 - 550 - 20)).frame = 0;
         AR.addElement(var1);
      }

      for(var0 = 0; var0 <= 5; ++var0) {
         (var1 = new Point(var0 * 86, AH - 30 - 550 - 6 - 25)).frame = 1;
         AR.addElement(var1);
      }

      for(var0 = 0; var0 <= 6; ++var0) {
         (var1 = new Point(var0 * 70, AH - 30 - 550 - 20 - 20)).frame = 2;
         AR.addElement(var1);
      }

      GameCanvas.loadMapScr.mItemMap = null;
      AG = 0;
      GameScreen.player.setSpeed(2, 2);
      GameScreen.player.x = 50;
      GameScreen.player.y = AH - 30;
      GameScreen.player.Dir = 2;
      GameScreen.player.type_left_right = 2;
      GameScreen.player.boatSea = new Boat(GameScreen.player.ID, GameScreen.player.x, GameScreen.player.y, 0, (byte)GameScreen.player.type_left_right);
      GameScreen.player.boatSea.AA(GameScreen.player.BO, GameScreen.player.typePirate);
      GameScreen.player.toX = BE[AG][0];
      GameScreen.player.toY = BE[AG][1];
      GameScreen.player.NR = true;
      MainScreen.cameraMain.xCam = 0;
      MainScreen.cameraMain.yCam = 0;
      MainScreen.cameraMain.setAll(AI - MotherCanvas.w, AH - MotherCanvas.h, GameScreen.player.x - MotherCanvas.hw, GameScreen.player.y - MotherCanvas.hh);
      isBeginEffBoat = false;
      AB = false;
      BG = 0;
      BH = 0;
      MapOff_RedLine.AI = 0;
      MapOff_RedLine.AK = 0L;
   }

   public static void AA(mGraphics var0) {
      (var0 = var0).setColor(-12410121);
      var0.fillRect(0, AH - 200, AI, 200);
      if (GameCanvas.mapBack != null) {
         GameCanvas.mapBack.paint(var0);
      }

      int var1;
      Point var2;
      for(var1 = 0; var1 < AO.size(); ++var1) {
         if ((var2 = (Point)AO.elementAt(var1)).frame / 2 < 7) {
            if (var2.fSmall == 0) {
               AX.drawFrame(var2.frame / 2, var2.x, var2.y, var2.dis == 0 ? 2 : 0, 0, var0);
            } else {
               AY.drawFrame((3 + var2.frame / 2) % AY.nFrame, var2.x, var2.y, var2.dis == 0 ? 2 : 0, 0, var0);
            }
         }
      }

      for(var1 = 0; var1 < AN.size(); ++var1) {
         if ((var2 = (Point)AN.elementAt(var1)).frame / 2 < AX.nFrame) {
            AX.drawFrame(var2.frame / 2, var2.x, var2.y, var2.dis, 0, var0);
         }
      }

      for(var1 = 0; var1 < AP.size(); ++var1) {
         if ((var2 = (Point)AP.elementAt(var1)).dis != 0) {
            var0.drawRegion((mImage)AZ[var2.frame], var2.x, var2.y, 3);
         }
      }

      if (GameScreen.player.SH == 3) {
         var0.setColor(-2492417);
         var0.fillRect(MainScreen.cameraMain.xCam, AH - 30 - 550 - 60, MotherCanvas.w, 40);

         for(var1 = 0; var1 < AR.size(); ++var1) {
            if ((var2 = (Point)AR.elementAt(var1)).frame == 0 && (var2.x + 25 > MainScreen.cameraMain.xCam || var2.x - 25 < MainScreen.cameraMain.xCam + MotherCanvas.w)) {
               var0.drawRegion((mImage)BA[0], var2.x, var2.y, 0);
            }
         }
      }

      var0.setColor(-11221251);
      var0.fillRect(AC + 3, AD - AF, AE - 6, AF - 24);

      for(var1 = 0; var1 < 7; ++var1) {
         AV.drawFrame(BC[var1] + GameCanvas.gameTick / 2 % 2, AC + 1 + var1 * 24, AD - 24, 0, 0, var0);
      }

      for(var1 = 0; var1 < AJ.size(); ++var1) {
         var2 = (Point)AJ.elementAt(var1);
         AS.drawFrame(var2.f / 2 % AS.nFrame, var2.x, var2.y, var2.dis, 3, var0);
      }

      int var3;
      for(var1 = 0; var1 < AK.size(); ++var1) {
         var2 = (Point)AK.elementAt(var1);

         for(var3 = 0; var3 < 7; ++var3) {
            AT.drawFrame(BB[var3], var2.x + var3 * 24, var2.y, 0, 3, var0);
         }
      }

      for(var1 = 0; var1 < AL.size(); ++var1) {
         var2 = (Point)AL.elementAt(var1);
         AU.drawFrame(var2.f / 2 % AU.nFrame, var2.x, var2.y, 0, 3, var0);
      }

      for(var1 = 0; var1 < AM.size(); ++var1) {
         var2 = (Point)AM.elementAt(var1);
         AW.drawFrame((var2.frame << 2) + var2.fSmall, var2.x, var2.y, var2.dis, 3, var0);
      }

      GameScreen.player.AE(var0);

      for(var1 = 0; var1 < AQ.size(); ++var1) {
         var2 = (Point)AQ.elementAt(var1);
         AW.drawFrame((var2.frame << 2) + var2.fSmall, var2.x, var2.y, var2.dis, 3, var0);
      }

      for(var1 = 0; var1 < AP.size(); ++var1) {
         if ((var2 = (Point)AP.elementAt(var1)).dis == 0) {
            var0.drawRegion((mImage)AZ[var2.frame], var2.x, var2.y, 3);
         }
      }

      if (GameScreen.player.SH == 3) {
         for(var1 = 0; var1 < AR.size(); ++var1) {
            if ((var2 = (Point)AR.elementAt(var1)).frame != 0 && (var2.x + 45 > MainScreen.cameraMain.xCam || var2.x - 45 < MainScreen.cameraMain.xCam + MotherCanvas.w)) {
               var0.drawRegion((mImage)BA[var2.frame], var2.x, var2.y, 0);
            }
         }
      }

      GameCanvas.resetTrans(var0);
      if (ReadMessenge.AH != null) {
         var0.setColor(0);
         var0.drawRect(MotherCanvas.hw - 40 - 1, MapOff_RedLine.AP - 20 - 1, 81, 8);
         if ((var1 = (int)((long)MapOff_RedLine.AI - (GameCanvas.timeNow - MapOff_RedLine.AK))) <= 0 && GameScreen.player.SH == 1) {
            var1 = 0;
         }

         Interface_Game.AA(var0, (byte)1, var1, MapOff_RedLine.AI, MotherCanvas.hw - 40, MapOff_RedLine.AP - 20, 0, 7, 80, 4, false, 0, false, 0);
         mGraphics var8 = var0;
         int var7 = MotherCanvas.hw - ReadMessenge.AH.length / 2 * Interface_Game.BQ;
         var3 = ReadMessenge.AH.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            int var6 = Interface_Game.BQ;
            if (var4 == 0) {
               var6 = 0;
            } else if (var4 == var3 - 1) {
               var6 = Interface_Game.BQ << 1;
            }

            var8.drawRegion(AvMain.imgCombo, var6, 0, Interface_Game.BQ, Interface_Game.BQ, 0, var7 + var4 * Interface_Game.BQ - Interface_Game.BQ / 2, MapOff_RedLine.AP - Interface_Game.BQ / 2, 0);
            var8.drawRegion((mImage)ReadMessenge.AH[var4], var7 + var4 * Interface_Game.BQ, MapOff_RedLine.AP, 3);
            if (var4 <= Player.RC) {
               var8.drawRegion(AvMain.imgDelay, 0, 0, Interface_Game.BQ, Interface_Game.BQ, 0, var7 + var4 * Interface_Game.BQ - Interface_Game.BQ / 2, MapOff_RedLine.AP - Interface_Game.BQ / 2, 0);
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

   }

   public static void AB() {
      if (++BG == 2000) {
         BH = 10;
      } else if (BG == 2500) {
         BH = 15;
      } else if (BG == 3500) {
         BH = 20;
      }

      Point var10000;
      int var0;
      Point var1;
      for(var0 = 0; var0 < AJ.size(); ++var0) {
         var10000 = var1 = (Point)AJ.elementAt(var0);
         var10000.y += var1.vy;
         ++var1.f;
         if (var1.y < AD - AF) {
            var1.y = AD - 24;
         }
      }

      for(var0 = 0; var0 < AK.size(); ++var0) {
         var10000 = var1 = (Point)AK.elementAt(var0);
         var10000.y += var1.vy;
         ++var1.f;
         if (var1.y < AD - AF) {
            if (var1.frame == 0) {
               var1.y = AD - 15;
            } else {
               AK.removeElement(var0);
               --var0;
            }
         }
      }

      for(var0 = 0; var0 < AL.size(); ++var0) {
         var10000 = var1 = (Point)AL.elementAt(var0);
         var10000.y += var1.vy;
         ++var1.f;
         if (var1.y < AD - AF) {
            AL.removeElement(var0);
            --var0;
         }
      }

      for(var0 = 0; var0 < AL.size(); ++var0) {
         var10000 = var1 = (Point)AL.elementAt(var0);
         var10000.y += var1.vy;
         ++var1.f;
         if (var1.y < AD - AF) {
            AL.removeElement(var0);
            --var0;
         }
      }

      for(var0 = 0; var0 < AM.size(); ++var0) {
         var10000 = var1 = (Point)AM.elementAt(var0);
         var10000.y += var1.vy;
         var1.x += var1.vx;
         ++var1.f;
         if (var1.f < 4) {
            var1.fSmall = var1.f;
         } else if (var1.f <= 6) {
            var1.fSmall = 3;
         } else if (var1.f > 6 && var1.f < 10) {
            var1.fSmall = 9 - var1.f;
         } else {
            var1.fSmall = 0;
         }

         if (var1.y < AD - 24) {
            AM.removeElement(var0);
            --var0;
         }
      }

      for(var0 = 0; var0 < AN.size(); ++var0) {
         ++(var1 = (Point)AN.elementAt(var0)).frame;
         if (var1.frame / 2 >= AX.nFrame && CRes.random(4) == 0) {
            var1.frame = 0;
            var1.dis = CRes.random(5) == 0 ? 0 : 2;
         }
      }

      for(var0 = 0; var0 < AO.size(); ++var0) {
         ++(var1 = (Point)AO.elementAt(var0)).frame;
         if (var1.frame / 2 >= AX.nFrame && CRes.random(10) == 0) {
            var1.frame = 0;
            var1.fSmall = CRes.random(6);
            var1.dis = CRes.random(5);
         }
      }

      for(var0 = 0; var0 < AP.size(); ++var0) {
         (var1 = (Point)AP.elementAt(var0)).update();
         if (var1.x < MainScreen.cameraMain.xCam - 50 || var1.y > MainScreen.cameraMain.yCam + MotherCanvas.h + 15) {
            AP.removeElement(var0);
            --var0;
         }
      }

      Point var4;
      if (CRes.random(20) == 0) {
         (var4 = new Point(AC + 18, AD - 15)).frame = 1;
         var4.vy = -CRes.random(6, 10);
         AK.addElement(var4);
      }

      if (CRes.random(5) == 0) {
         (var4 = new Point(AC + 18 + CRes.random(AE - 36), AD - 15 - CRes.random(10))).frame = 0;
         var4.vy = -CRes.random(7, 12);
         AL.addElement(var4);
      }

      if (CRes.random(3) == 0) {
         var0 = CRes.random(2, 4);

         for(int var3 = 0; var3 < var0; ++var3) {
            Point var2;
            if ((var2 = new Point(AC - 24 + CRes.random(AE + 48), AD + 40)).x - AC < (AE + 36) / 3) {
               var2.frame = 1;
               var2.dis = 0;
               var2.vx = 1;
            } else if (var2.x - AC > (AE + 36) / 3 << 1) {
               var2.frame = 1;
               var2.dis = 2;
               var2.vx = CRes.random_Am_0(2);
            } else {
               var2.frame = 0;
               var2.dis = 0;
               var2.vx = -1;
            }

            var2.vy = -7;
            AM.addElement(var2);
         }
      }

      if (BG > 1000 && CRes.random(30 - BH) == 0) {
         (var4 = new Point()).x = MainScreen.cameraMain.xCam + 10 + CRes.random(MotherCanvas.w);
         var4.y = MainScreen.cameraMain.yCam - 10;
         var4.vx = -CRes.random(2);
         var4.vy = 2;
         var4.frame = CRes.random(3);
         var4.dis = CRes.random(4);
         AP.addElement(var4);
      }

      if (GameScreen.player.SH == 0) {
         if (AG < BE.length && CRes.abs(GameScreen.player.x - BE[AG][0]) < 10 && CRes.abs(GameScreen.player.y - BE[AG][1]) < 10) {
            if (++AG >= BE.length) {
               GameScreen.player.vx = 0;
               GameScreen.player.vy = 0;
               GameScreen.player.SH = 1;
            } else {
               AC();
               if (AG == 1) {
                  isBeginEffBoat = true;
                  GameScreen.player.setSpeed(4, 4);
               }
            }
         }
      } else if (GameScreen.player.SH == 1) {
         if (CRes.abs(GameScreen.player.x - GameScreen.player.toX) < 4 && CRes.abs(GameScreen.player.y - GameScreen.player.toY) < 4 && CRes.random(25) == 0) {
            GameScreen.player.toX = AC + 20 + CRes.random(AE - 76);
         }
      } else if (GameScreen.player.SH == 3) {
         if (AG < BE.length && CRes.abs(GameScreen.player.x - BE[AG][0]) < 10 && CRes.abs(GameScreen.player.y - BE[AG][1]) < 10 && ++AG < BE.length) {
            AC();
         }

         if (MainScreen.cameraMain.AD <= AH - 30 - 540 - 45) {
            MainScreen.cameraMain.AD = AH - 30 - 540 - 45;
            AB = true;
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

      GameScreen.player.CC();
      MainScreen.cameraMain.AA();
      if (Interface_Game.timePointer > 0) {
         --Interface_Game.timePointer;
      }

   }

   public static void AC() {
      GameScreen.player.toX = BE[AG][0];
      GameScreen.player.toY = BE[AG][1];
      GameScreen.player.NR = true;
   }

   public static void AA(byte var0) {
      GameScreen.player.SH = var0;
      int var2;
      if (var0 == 2) {
         GameScreen.player.vy = -12;
         GameScreen.player.vx = -8;
         GameScreen.player.boatSea.AU.removeAllElements();
         AB = true;

         for(var2 = 0; var2 < 3; ++var2) {
            Point var1;
            (var1 = new Point()).x = GameScreen.player.x + CRes.random_Am_0(15);
            var1.y = GameScreen.player.y + CRes.random_Am_0(5);
         }

      } else {
         if (var0 == 3) {
            AG = 0;
            BE = mSystem.new_M_Int(BD.length, 2);

            for(var2 = 0; var2 < BF.length; ++var2) {
               BE[var2] = new int[2];
               BE[var2][0] = BF[var2][0];
               BE[var2][1] = AH - 30 - BF[var2][1];
            }

            GameScreen.player.toX = BE[AG][0];
            GameScreen.player.toY = BE[AG][1];
         }

      }
   }
}
