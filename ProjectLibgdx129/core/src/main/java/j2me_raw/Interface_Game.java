public final class Interface_Game {
   public static mImage[] imgMove;
   private static mImage[] imgFire;
   public static mImage[] imgOther;
   public static mImage[] mImgPvPType;
   public static mImage[] AD;
   public static mImage imgInfo;
   public static mImage imgHoavan;
   public static mImage imgIconMPHP;
   public static mImage imgInfoServer;
   public static mImage AI;
   public static mImage imgIconMPHP2;
   private static mImage CR;
   public static mImage imgRankSkill;
   private static mImage CS;
   private static mImage CT;
   public static mImage imgBorderNoti;
   public static mImage imgBorderNoti2;
   public static FrameImage fraBorderNoti;
   public static FrameImage fraBorderNoti4;
   public static int[][] AP = mSystem.new_M_Int(6, 2);
   private static int[][] CU = mSystem.new_M_Int(6, 2);
   private static int[][] CV = mSystem.new_M_Int(6, 2);
   private static int[][] mPosMove = mSystem.new_M_Int(4, 2);
   public static int[][] mPosOther = mSystem.new_M_Int(6, 2);
   private static int[][] CX = mSystem.new_M_Int(6, 2);
   private static int[][] CY = mSystem.new_M_Int(6, 2);
   private static int[][] CZ = mSystem.new_M_Int(12, 2);
   public int[] AR = new int[]{4, 6, 2, 8};
   private static int[] DA = new int[]{1, 3, 5, 7, 9, 0};
   public static short[][] AS = new short[][]{{1, 1}, {3, 1}, {1, 1}, {5, 1}, {0, 1}, {5, 1}, {2, 1}, {4, 1}, {2, 1}};
   public static int AT;
   public static int AU;
   public static int AV;
   public static int AW;
   public static int xPointMove;
   public static int yPointMove;
   public static int keyPoint;
   public static int wArrowMove = 30;
   public static int timePointer = 0;
   
   
   
   private static int DB = 285;
   private static int DC = 50;
   private static int xFocus;
   private static int DE;
   private static int DF;
   private static int wSkill = 28;
   private static int yInfoServer = 50;
   private static int xInfoServer;
   private static int hInfoServer;
   public static int wInfoServer;
   private static int DK;
   private static int DL;
   private static int DM;
   private static int DN;
   private static int DO;
   private static int DP;
   private static int yQuickChat;
   private int DR;
   private int DS;
   private int maxTimeChange;
   private short[] DU;
   private int DV;
   private boolean isFocusTime;
   private int timepointer;
   public static boolean isPaintInfoFocus;
   public static byte typeTouch = 1;
   private static byte DY = -1;
   private static byte DZ;
   public static byte BF;
   public static byte typeTitleRoomFight;
   private static boolean isMove = true;
   public static mVector vecEffCurrent = new mVector("Interface.vecEffCurrent");
   public static mVector vecEventShow = new mVector("Interface.vecEventShow");
   public static mVector vecInfoServer = new mVector("Interface.vecInfoServer");
   public static mVector vecQuickChatLoL = new mVector("Interface.vecQuickChatLoL");
   private static int EB;
   private static int EC;
   private static int ED;
   private int valueSmallScreen;
   public static CountDownTicket BL = new CountDownTicket();
   public static CountDownTicket BM = new CountDownTicket();
   private static boolean isSmallInfoServer = false;
   public static boolean BN = true;
   private static FrameImage[] EG;
   public static Class_EB[] BO;
   public static int BP;
   public static int BQ;
   public static int BR;
   public static int BS;
   public static int BT;
   private static int EH;
   public static boolean BU;
   private static boolean EI;
   private static boolean EJ;
   private static int[] EK;
   private static int[] EL;
   public static byte BV;
   public static int BW;
   public static int BX;
   public static int BY;
   private static int EM;
   public static int BZ;
   private static int EN;
   private static boolean EO;
   private static int[][] EP;
   private static int EQ;
   private static int ER;
   private int ES;
   private int ET;
   private int EU;
   private boolean EV;
   public static mVector CA;
   private int EW;
   private static int EX;
   private static int EY;
   private static int EZ;
   private static int FA;
   public static Class_DS CB;
   public static InfoShowNotify CC;
   public static InfoShowNotify CD;
   public static InfoShowNotify CE;
   private static InfoShowNotify infoPlayer;
   private static int wShowInfoPlayer;
   private static int FD;
   private static int FE;
   private static int yEffInfoPlayer;
   public static int xNumMess;
   public static int yNumMess;
   public static int xAutoFire;//xAutoFire;
   public static int yAutoFire;//yAutoFire;
   public static boolean isAutoFireInterface;//isAutoFireInterface;
   public static NumberMess numMess;
   private static NumberMess numClan;
   private static int FM;
   private static int FN;
   public static int CI;
   public static int CJ;
   public static int CK;
   public static int CL;
   private static int FO;
   private static int FP;
   public static String nameMap;
   public static int indexPaintTable;
   private static int FQ;
   private static int FR;
   public static int CO;
   public static mVector vecClanDam;
   private byte xShow;
   private byte FT;
   private boolean FU;
   private int lastTick;
   private int framepaint;
   private static int FX;

   static {
      short[][] var10000 = new short[][]{{227}, {228, 229}, {230, 231}, {232, 234}, {235, 236}, {237, 238}, {239, 240}, {241}, {242}, {243, 244}};
      String[] var0 = new String[]{"Cau su", "Lửa", "Băng", "Khói", "Cát", "Sét", "Nham thạch", "Chim ưng", "Báo đóm", "Chấn thiên"};
      BP = 0;
      BQ = 17;
      BS = -1;
      BU = true;
      EI = false;
      EJ = false;
      EK = new int[]{-1365437, -1161637, -104071, -1365437};
      EL = new int[]{-13983020, -13649947, -9712641, -13983020};
      BV = -1;
      BW = 145;
      BX = 5;
      BY = 0;
      EN = 0;
      EO = true;
      EP = new int[][]{{-3145696, -11523546, -3107426}, {-9591036, -14338047, -4269175}, {-4979594, -11468235, -3172933}, {-2131196, -11194622, -2307921}, {-9466447, -16636335, -5192226}, {-16559697, -16627423, -6175055}, {-2051582, -11187943, -2305104}, {-16469814, -16626343, -5386284}, {-4258801, -11730426, -3166287}, {-13105720, -15465912, -5595443}};
      EQ = 0;
      ER = 0;
      CA = new mVector("Interface.vecfocus");
      EX = 0;
      EY = 0;
      EZ = 6;
      FA = 100;
      CB = null;
      CC = null;
      CD = null;
      CE = null;
      infoPlayer = new InfoShowNotify("", (byte)10);
      wShowInfoPlayer = 0;
      FD = 0;
      FE = 0;
      yEffInfoPlayer = 0;
      isAutoFireInterface = true;
      numMess = new NumberMess();
      numClan = new NumberMess();
      FM = 0;
      FN = 0;
      CK = -1;
      CL = -1;
      FO = 60;
      FP = 20;
      nameMap = "";
      indexPaintTable = 0;
      CO = 0;
      vecClanDam = new mVector("Interface.vecClanDam");
      FX = 0;
   }

   public Interface_Game() {
      int[] var10000 = new int[]{2, 0, 3, 1};
      this.DV = 0;
      this.valueSmallScreen = 0;
      this.ES = 0;
      this.EV = false;
      short[] msetweapon = new short[]{-1, 687, 688, 689, 690};
      String[] msetSkillDevil = new String[]{"causu 1", "lửa 1", "lửa 2", "băng 1", "băng 2", "khói 1", "khói 2", "cát 1", "cát 2", "sét 1", "sét 2", "nham 1", "nham 2", "chim 1", "báo 1", "chấn thiên 1", "chấn thiên 2", "dao 1", "dao 2", "sáp 2", "sáp 1", "kilo 1"};
      this.EW = 40;
      this.xShow = 75;
      this.lastTick = 0;
      this.framepaint = 0;
   }

   public final void load_Image_Pointer() {
      if (!GameCanvas.isTouch) {
         typeTouch = 0;
      }

      this.maxTimeChange = 5;
      xFocus = MotherCanvas.w - 56;
      hInfoServer = 20;
      xNumMess = 38;
      xAutoFire = MotherCanvas.w - 29;
      yAutoFire = MotherCanvas.h - 170;
      if (!GameCanvas.isTouch) {
         xNumMess = 35;
      }

      yNumMess = 50;
      wInfoServer = 120;
      if (MotherCanvas.w > 330) {
         wInfoServer = 140;
      }

      if (wInfoServer > MotherCanvas.w - ((xNumMess << 1) + 40)) {
         wInfoServer = MotherCanvas.w - ((xNumMess << 1) + 40);
      }

      if (wInfoServer < 100) {
         wInfoServer = 100;
      }

      if (!GameCanvas.isTouch) {
         xAutoFire = 5;
         yAutoFire = 50;
      }

      if (MotherCanvas.w >= 320) {
         yInfoServer = 3;
         xInfoServer = MotherCanvas.hw - wInfoServer / 2;
      } else {
         xInfoServer = MotherCanvas.hw - wInfoServer / 2;
         yInfoServer = 3;
         if (xInfoServer < 92) {
            yInfoServer = 48;
            xInfoServer = MotherCanvas.w - wInfoServer - 3;
            isSmallInfoServer = true;
         }
      }

      if (GameCanvas.isSmallScreen) {
         this.valueSmallScreen = 10;
      }

      yNumMess -= this.valueSmallScreen;
      yInfoServer -= this.valueSmallScreen;
      yQuickChat = MotherCanvas.h - 50;
      if (GameCanvas.isTouch) {
         wSkill = 32;
         xPointMove = 55;
         yPointMove = MotherCanvas.h - 55;
         imgMove = new mImage[2];

         int i;
         for(i = 0; i < imgMove.length; ++i) {
            imgMove[i] = mImage.createImage("/point/move_" + i + ".png");
         }

         for(i = 0; i < mPosMove.length; ++i) {
            mPosMove[i][0] = xPointMove + (i < 2 ? -wArrowMove + (wArrowMove << 1) * (i % 2) : 0);
            mPosMove[i][1] = yPointMove + (i > 1 ? -wArrowMove + (wArrowMove << 1) * (i % 2) : 0);
         }

         imgFire = new mImage[3];

         for(i = 0; i < imgFire.length; ++i) {
            imgFire[i] = mImage.createImage("/point/fire_" + i + ".png");
         }

         mPosOther[0][0] = 3;
         mPosOther[0][1] = 3;
         mPosOther[1][0] = 4;
         mPosOther[1][1] = 46;
         mPosOther[2][0] = MotherCanvas.w - 30;
         mPosOther[2][1] = MotherCanvas.h - 30;
         mPosOther[3][0] = MotherCanvas.w - 30;
         mPosOther[3][1] = MotherCanvas.h - 145;
         mPosOther[4][0] = -2;
         mPosOther[4][1] = MotherCanvas.h / 2 - 20;
         mPosOther[5][0] = 66;
         mPosOther[5][1] = 45;
         setPosTouch();
         setPosMenu_TaiTho();
         imgOther = new mImage[6];

         for(i = 0; i < imgOther.length; ++i) {
            imgOther[i] = mImage.createImage("/point/other_" + i + ".png");
            if (i == 0) {
               CX[i][0] = 92;
               CX[i][1] = 45;
            } else {
               CX[i][0] = mImage.getImageWidth(imgOther[i].image);
               CX[i][1] = mImage.getImageHeight(imgOther[i].image) / 2;
            }
         }

         QuickMenu.fraQuickMenu = new FrameImage[16];

         for(i = 0; i < QuickMenu.fraQuickMenu.length; ++i) {
            QuickMenu.fraQuickMenu[i] = new FrameImage(mImage.createImage("/point/quick_" + i + ".png"), 30, 30);
         }

         QuickMenu.imgNenMenu = mImage.createImage("/point/nenmenu.png");
         QuickMenu.imgTamGiac = mImage.createImage("/point/tamgiac.png");
      }

      AC();
      AI();
      AD();
   }

   public static void setPosMenu_TaiTho() {
      mPosOther[4][0] = -2;
      mPosOther[0][0] = 3;
      mPosOther[5][0] = 66;
      xNumMess = 38;
      if (GameCanvas.isTaiTho) {
         mPosOther[4][0] = 30;
         mPosOther[0][0] = 8;
         mPosOther[5][0] = 71;
         xNumMess = 43;
      }

   }

   public static void AC() {
      DO = MotherCanvas.hw;
      DP = 34 + GameScreen.h12plus;
      if (MotherCanvas.w < 280) {
         DO = MotherCanvas.w - 52;
         DP = 72 + GameScreen.h12plus;
      }

   }

   public static void AD() {
      mPosOther[0][1] = 3 + GameScreen.h12plus;
      mPosOther[1][1] = 46 + GameScreen.h12plus;
      mPosOther[5][1] = 45 + GameScreen.h12plus;
      yNumMess = 50 + GameScreen.h12plus;
      if (GameCanvas.isSmallScreen) {
         yNumMess -= 10;
      }

   }

   public final void paintInGame(mGraphics var1) {
      if (GameCanvas.isTouch && this.xShow < 75 && vecClanDam != null && vecClanDam.size() > 0) {
         this.paintInfoClanDam(var1);
      } else {
         int var2;
         int var4;
         if (GameScreen.player != null && GameScreen.player.Action != 4) {
            if (GameCanvas.isTouch && typeTouch == 0) {
               var1.drawRegion((mImage)imgMove[0], xPointMove, yPointMove, 3);

               for(var2 = 0; var2 < 4; ++var2) {
                  if (timePointer > 0 && this.AR[var2] == keyPoint) {
                     AA(var1, var2);
                  }
               }
            }

            BP = 90;
            if (GameCanvas.loadmap.mapLang() && BP <= 0 && !Player.isGhost) {
               if (typeTouch == 0 && GameCanvas.isTouch && GameScreen.objFocus != null) {
                  if (AvMain.DT == null) {
                     AvMain.DT = mImage.createImage("/interface/icontrade.png");
                  } else {
                     var1.drawRegion((mImage)AvMain.DT, AP[2][0], AP[2][1], 3);
                  }
               }
            } else {
               if (BP > 0) {
                  --BP;
               }

               if (GameScreen.tickPvP <= 0) {
                  var2 = 0;

                  label199:
                  while(true) {
                     int var3;
                     if (var2 >= AP.length) {
                        if (GameCanvas.loadmap.mapLang() || !GameCanvas.isTouch || !GameScreen.isShowSkillBuff) {
                           break;
                        }

                        var2 = 0;

                        while(true) {
                           if (var2 >= CU.length) {
                              break label199;
                           }

                           var3 = CU[var2][0];
                           var4 = CU[var2][1];
                           if (GameCanvas.isTouch) {
                              byte var9 = 0;
                              if (timePointer > 0 && keyPoint == var2 + 200) {
                                 var9 = 1;
                              }

                              Hotkey var11;
                              if ((var11 = Player.hotkeyBuffPlayer[var2]) != null) {
                                 AA(var1, var11, var3, var4, 20, false);
                                 var1.drawRegion(imgFire[2], 0, var9 * 50, 50, 50, 0, var3, var4, 3);
                              }
                           }

                           ++var2;
                        }
                     }

                     var3 = AP[var2][0] + CY[var2][0] * this.DS / 100;
                     var4 = AP[var2][1] + CY[var2][1] * this.DS / 100;
                     if (this.DS == 0 && !GameCanvas.isTouch) {
                        mFont.tahoma_7b_black.drawString(var1, "" + DA[var2], AP[var2][0] - wSkill / 2 + 9, AP[var2][1], 0);
                     }

                     Hotkey var5 = Player.hotkeyPlayer[Player.currentTab][var2];
                     boolean var6 = false;
                     if (var2 == 2 && (GameCanvas.loadmap.mapLang() ? true : !GameScreen.player.AC(GameScreen.objFocus))) {
                        var6 = true;
                     }

                     if (Player.isGhost) {
                        var1.drawRegion((mImage)AvMain.imgHotKey, var3, var4, 3);
                     } else if (GameCanvas.isTouch) {
                        byte var7 = 0;
                        if (timePointer > 0 && keyPoint == DA[var2]) {
                           var7 = 1;
                        }

                        AA(var1, var5, var3, var4, 20, var6);
                        if (var5.skill == null || var5.skill.lvDevil == 0 || var6) {
                           if (var2 == 2 && typeTouch == 0) {
                              var1.drawRegion(imgFire[1], 0, var7 * 50, 50, 50, 0, var3, var4, 3);
                           } else {
                              var1.drawRegion(imgFire[0], 0, var7 * 50, 50, 50, 0, var3, var4, 3);
                           }
                        }
                     } else {
                        AA(var1, var5, var3, var4, 20, var6);
                        var1.drawRegion((mImage)AvMain.imgHotKey, var3, var4, 3);
                     }

                     ++var2;
                  }
               }

               if (Player.QG != null && Player.QG.length > 0 && !Player.isGhost && !GameCanvas.loadmap.mapLang()) {
                  AO(var1);
               }
            }
         }

         if (GameCanvas.isTouch) {
            for(var2 = 0; var2 < mPosOther.length; ++var2) {
               if (var2 != 0 && (var2 != 2 || !GameCanvas.loadmap.mapLang() || BP > 0) && (var2 != 5 || GameScreen.player.clan != null)) {
                  byte var8 = 0;
                  if (timePointer > 0 && keyPoint == var2 + 100) {
                     var8 = 1;
                  }

                  if (GameScreen.player.Action != 4 || var2 != 2) {
                     var4 = mPosOther[var2][0];
                     int var10 = mPosOther[var2][1];
                     if (var2 == 5 && Clan_Screen.AT) {
                        var1.drawRegion(imgOther[var2], 0, var8 * CX[var2][1], CX[var2][0], CX[var2][1], 0, var4, var10 + numClan.yNum, 0);
                        if (GameCanvas.gameTick % 10 < 5) {
                           var1.drawRegion((mImage)MainEvent.imgNew, var4, var10 + 3 + numClan.yNum, 0);
                        }
                     } else {
                        var1.drawRegion(imgOther[var2], 0, var8 * CX[var2][1], CX[var2][0], CX[var2][1], 0, var4, var10, 0);
                     }
                  }
               }
            }
         }

         if (GameCanvas.currentScreen == GameCanvas.gameScr) {
            AQ(var1);
            if (!GameCanvas.isTouch) {
               this.paintInfoClanDam(var1);
            }
         }

         if (this.DS > 0) {
            this.AP(var1);
         }

      }
   }

   public static void paintVecEffKickAn(mGraphics var0) {
      if (BO != null) {
         int var1 = 9 + GameScreen.h12plus;
         int var2;
         if (GameScreen.isPvPNew) {
            var1 = yNumMess + 22 + GameScreen.h12plus;

            for(var2 = 0; var2 < BO.length; ++var2) {
               if (BO[var2] == null) {
                  return;
               }

               var0.drawRegion((mImage)AvMain.imgBgnum, 22 + var2 * 28, var1, 3);
               BO[var2].AA(var0, 12 + var2 * 28, var1, 11, 0);
               AvMain.AB(var0, "" + BO[var2].numPotion, 25 + var2 * 28, var1 - 5, 2, (int)BO[var2].colorName);
            }

         } else {
            for(var2 = 0; var2 < BO.length; ++var2) {
               if (BO[var2] == null) {
                  return;
               }

               var0.drawRegion((mImage)AvMain.imgBgnum, 112, var1 + 12 * var2, 3);
               BO[var2].AA(var0, 102, var1 + 12 * var2, 11, 0);
               AvMain.AB(var0, "" + BO[var2].numPotion, 115, var1 + 12 * var2 - 5, 2, (int)BO[var2].colorName);
            }

         }
      }
   }

   public static void AA(mGraphics var0, int var1) {
      if (var1 == 0) {
         var0.drawRegion(imgMove[1], 0, 34, 38, 34, 0, mPosMove[var1][0], mPosMove[var1][1], 3);
      } else if (var1 == 1) {
         var0.drawRegion(imgMove[1], 63, 34, 38, 34, 0, mPosMove[var1][0], mPosMove[var1][1], 3);
      } else if (var1 == 2) {
         var0.drawRegion(imgMove[1], 34, 0, 34, 38, 0, mPosMove[var1][0], mPosMove[var1][1], 3);
      } else {
         if (var1 == 3) {
            var0.drawRegion(imgMove[1], 34, 63, 34, 38, 0, mPosMove[var1][0], mPosMove[var1][1], 3);
         }

      }
   }

   public static void AC(mGraphics var0) {
      int var1 = MotherCanvas.w - 20;

      for(int var2 = 0; var2 < GameScreen.vecPlayers.size(); ++var2) {
         MainObject var3;
         if ((var3 = (MainObject)GameScreen.vecPlayers.elementAt(var2)).indexTeam == 1) {
            AA(var0, (byte)1, var3.Hp, var3.maxHp, 27, 15 + var3.MM * 30 - 8, 0, 9, 66, 0, false, var3.KI, false, 0);
            AA(var0, (byte)2, var3.Mp, var3.maxMp, 27, 15 + var3.MM * 30 + 11 - 8, 0, 9, 66, 0, false, 0, false, 0);
            if (AvMain.FY == null) {
               AvMain.FY = new FrameImage(mImage.createImage("/interface/border.png"), 26, 26);
            } else {
               AvMain.FY.drawFrame(0, 18, 15 + var3.MM * 30 + 2, 0, 3, var0);
            }

            var3.AB(var0, 18, 15 + var3.MM * 30 - 2, 2);
            mFont.tahoma_7b_white.drawString(var0, "" + var3.Lv, 26, 15 + var3.MM * 30 + 2, 2);
         } else if (var3.indexTeam == 2) {
            AA(var0, (byte)1, var3.Hp, var3.maxHp, var1 - 7 - 66, 15 + var3.MM * 30 - 8, 0, 9, 66, 0, true, var3.KI, false, 0);
            AA(var0, (byte)2, var3.Mp, var3.maxMp, var1 - 7 - 66, 15 + var3.MM * 30 + 11 - 8, 0, 9, 66, 0, true, 0, false, 0);
            if (AvMain.FY == null) {
               AvMain.FY = new FrameImage(mImage.createImage("/interface/border.png"), 26, 26);
            } else {
               AvMain.FY.drawFrame(0, var1 + 2, 15 + var3.MM * 30 + 2, 0, 3, var0);
            }

            var3.AB(var0, var1 + 2, 15 + var3.MM * 30 - 2, 0);
            mFont.tahoma_7b_white.drawString(var0, "" + var3.Lv, var1 - 6, 15 + var3.MM * 30 + 2, 2);
         }
      }

   }

   private static void AO(mGraphics var0) {
      short[] var1;
      int var2 = (var1 = Player.QG).length;
      int var3 = BQ;
      int var4 = DL;
      int var10000 = DK;
      var10000 = BQ;
      boolean var5 = false;
      if (AS()) {
         var4 += BQ;
      }

      int var6;
      int var8;
      int var9;
      if (!BU) {
         for(var6 = 0; var6 < var2; ++var6) {
            var9 = DK + var6 * var3 - var2 / 2 * var3 - var2 % 2 * var3 / 2 + var3 / 2;
            var8 = BQ;
            if (var6 == 0) {
               var8 = 0;
            } else if (var6 == var2 - 1) {
               var8 = BQ << 1;
            }

            var0.drawRegion(AvMain.imgCombo, var8, 0, BQ, BQ, 0, var9 - BQ / 2, var4 - BQ / 2, 0);
            if (var6 <= BS && EH % 4 < 2) {
               var0.setColor(-1891261);
               var0.fillRect(var9 - BQ / 2 + 2, var4 - BQ / 2 + 2, BQ - 4, BQ - 4);
            }
         }

         ++EH;
         if (BS < var2) {
            ++BS;
         }

         if (EH >= var2 + 6) {
            EH = 0;
            BS = -1;
            BU = true;
            Player.QG = null;
            Player.RB = -1;
            return;
         }
      } else if (BR > 0) {
         --BR;

         for(var6 = 0; var6 < var2; ++var6) {
            var9 = DK + var6 * var3 - var2 / 2 * var3 - var2 % 2 * var3 / 2 + var3 / 2;
            var8 = BQ;
            if (var6 == 0) {
               var8 = 0;
            } else if (var6 == var2 - 1) {
               var8 = BQ << 1;
            }

            var0.drawRegion(AvMain.imgCombo, var8, 0, BQ, BQ, 0, var9 - BQ / 2, var4 - BQ / 2, 0);
            if (var6 == BS) {
               var0.setColor(-1);
               var0.fillRect(var9 - BQ / 2, var4 - BQ / 2, BQ, BQ);
            }

            if (var6 < BS) {
               Skill_Info.paintIcon(var0, var9, var4, var1[var6], (byte)0);
            }
         }

         if (++BS > var2) {
            BR = 0;
            BS = -1;
            return;
         }
      } else {
         for(var6 = 0; var6 < var2; ++var6) {
            var9 = DK + var6 * var3 - var2 / 2 * var3 - var2 % 2 * var3 / 2 + var3 / 2;
            var8 = BQ;
            if (var6 == 0) {
               var8 = 0;
            } else if (var6 == var2 - 1) {
               var8 = BQ << 1;
            }

            var0.drawRegion(AvMain.imgCombo, var8, 0, BQ, BQ, 0, var9 - BQ / 2, var4 - BQ / 2, 0);
            if (var6 == BS) {
               AvMain.fraComboSkill.drawFrame(BT / 2, var9, var4, 0, 3, var0);
               if (++BT / 2 >= 4) {
                  BS = -1;
                  BT = 0;
               }
            } else {
               Skill_Info.paintIcon(var0, var9, var4, var1[var6], (byte)0);
               if (var6 <= Player.RB) {
                  var0.drawRegion(AvMain.imgDelay, 0, 0, BQ, BQ, 0, var9 - BQ / 2, var4 - BQ / 2, 0);
               }

               if (var6 == Player.RB + 1) {
                  var0.drawRegion((mImage)AvMain.imgBorderCombo, var9, var4, 3);
               }
            }
         }
      }

   }

   private void AP(mGraphics var1) {
      for(int var2 = 0; var2 < AP.length; ++var2) {
         int var3 = CV[var2][0] - CY[var2][0] * this.DS / 100;
         int var4 = CV[var2][1] - CY[var2][1] * this.DS / 100;
         Hotkey var5 = Player.hotkeyPlayer[Player.currentTab][var2];
         if (GameCanvas.isTouch) {
            byte var6 = 0;
            if (timePointer > 0 && keyPoint == DA[var2]) {
               var6 = 1;
            }

            AA(var1, var5, var3, var4, 20, false);
            if (var5.skill == null || var5.skill.lvDevil == 0) {
               if (var2 == 2 && typeTouch == 0) {
                  var1.drawRegion(imgFire[1], 0, var6 * 50, 50, 50, 0, var3, var4, 3);
               } else {
                  var1.drawRegion(imgFire[0], 0, var6 * 50, 50, 50, 0, var3, var4, 3);
               }
            }
         } else {
            AA(var1, var5, var3, var4, 20, false);
            var1.drawRegion((mImage)AvMain.imgHotKey, var3, var4, 3);
         }
      }

      ++this.DS;
      if (this.DS >= this.maxTimeChange) {
         this.DS = 0;
         Player.currentTab = (byte)(Player.currentTab == 0 ? 1 : 0);
      }

   }

   private static void AA(mGraphics var0, Hotkey var1, int var2, int var3, int var4, boolean var5) {
      if (var5) {
         AvMain.fraQuest.drawFrame(2, var2, var3 - 2 + GameCanvas.gameTick / 5 % 3, 0, 3, var0);
      } else {
         byte var8 = 20;
         if (var1.skill != null) {
            var1.skill.AA(var0, var2, var3, var1.skill.lvDevil);
         } else if (var1.itemcur != null) {
            var1.itemcur.AA(var0, var2, var3, var8, 0);
         }

         DelaySkill.getDelay(var1.skill != null ? var1.skill.AB : (var1.itemcur != null ? var1.itemcur.indexHotKey : -1)).AA(var0, var2 - 10, var3 - 10, 20);
      }
   }

   public static void paintInfoPlayer(mGraphics var0, int var1, int var2, boolean var3, mFont var4) {
      if (GameCanvas.isTaiTho) {
         var1 += 5;
      }

      mImage var5;
      if (GameScreen.player.Lv == 100) {
         var5 = imgIconMPHP2;
      } else {
         var5 = imgIconMPHP;
      }

      if (var3) {
         if (GameCanvas.currentScreen == GameCanvas.gameScr && LoadMap.specMap == 7) {
            if (AI == null) {
               AI = LoadImageStatic.LoadNewInterface("/infonew.png");
            }

            var0.drawRegion((mImage)AI, var1, var2, 0);
         } else if (!GameCanvas.lowGraphic) {
            var0.drawRegion((mImage)imgInfo, var1, var2, 0);
            var0.drawRegion((mImage)imgHoavan, var1 + 3, var2 + 20, 0);
            var0.drawRegion(imgHoavan, 0, 0, 23, 23, 3, var1 + 66, var2 + 2, 0);
         } else {
            var0.setColor(-530490);
            var0.fillRect(var1 + 2, var2, 88, 45);
         }

         var0.drawRegion((mImage)var5, var1 + 7, var2 + 7, 0);
         var2 += 7;
      } else {
         var1 -= 19;
      }

      AA(var0, (byte)1, GameScreen.player.Hp, GameScreen.player.maxHp, var1 + 18, var2, 0, 9, 66, 0, false, GameScreen.player.KI, EI, GameScreen.player.MA);
      var2 += 11;
      AA(var0, (byte)2, GameScreen.player.Mp, GameScreen.player.maxMp, var1 + 18, var2, 0, 9, 66, 0, false, 0, EJ, 0);
      var2 += 8;
      int var6;
      if (GameScreen.player.Lv >= 100) {
         var4.drawString(var0, GameScreen.player.LvThongThao + " + " + GameScreen.player.KS / 10 + "," + GameScreen.player.KS % 10 + "%", var1 + 20, var2, 0);
         var2 += 10;
         var6 = GameScreen.player.KS / 10 * 65 / 100;
      } else {
         var4.drawString(var0, GameScreen.player.Lv + " + " + GameScreen.player.KO / 10 + "," + GameScreen.player.KO % 10 + "%", var1 + 20, var2, 0);
         var2 += 10;
         var6 = GameScreen.player.KO / 10 * 65 / 100;
      }

      if (var6 > 65) {
         var6 = 65;
      }

      var0.setColor(-15519213);
      var0.fillRect(var1 + 18, var2, 65, 2);
      if (var6 > 0) {
         var0.setColor(-13263058);
         var0.fillRect(var1 + 18, var2, var6, 2);
      }

      for(var6 = 1; var6 < 5; ++var6) {
         var0.setColor(-1);
         var0.fillRect(var1 + 18 + var6 * 13, var2, 1, 2);
      }

   }

   public static void AA(mGraphics var0, int var1, int var2) {
      mImage var3;
      if (GameScreen.player.Lv == 100) {
         var3 = imgIconMPHP2;
      } else {
         var3 = imgIconMPHP;
      }

      AvMain.paintRect(var0, var1, var2, 320, 15, (byte)1, 4);
      var0.drawRegion(var3, 0, 0, 10, 11, 0, var1 + 8, var2 + 9, 3);
      AA(var0, (byte)1, GameScreen.player.Hp, GameScreen.player.maxHp, var1 + 15, var2 + 3, 0, 9, 66, 0, false, GameScreen.player.KI, EI, GameScreen.player.MA);
      var0.drawRegion(var3, 0, 11, 10, 11, 0, var1 + 17 + 70, var2 + 9, 3);
      AA(var0, (byte)2, GameScreen.player.Mp, GameScreen.player.maxMp, var1 + 15 + 10 + 70, var2 + 3, 0, 9, 66, 0, false, 0, EJ, 0);
      var1 += 165;
      var2 += 7;
      AvMain.fraMoney.drawFrame(0, var1 + BX + AvMain.fraMoney.frameWidth / 2 - 3, var2, 0, 3, var0);
      AvMain.fraMoney.drawFrame(1, var1 + BX + AvMain.fraMoney.frameWidth / 2 + 45, var2, 0, 3, var0);
      AvMain.fraMoney.drawFrame(7, var1 + BX + AvMain.fraMoney.frameWidth / 2 + 90, var2, 0, 3, var0);
      if (GameScreen.player != null) {
         mFont.tahoma_7_yellow.drawString(var0, " " + AvMain.AB(Player.SN), var1 + BX + AvMain.fraMoney.frameWidth - 4, var2 - 5, 0);
         mFont.tahoma_7_red.drawString(var0, " " + AvMain.AA((long)GameScreen.player.Ruby), var1 + BX + AvMain.fraMoney.frameWidth + 45, var2 - 5, 0);
         mFont.tahoma_7_green.drawString(var0, " " + AvMain.AB((long)GameScreen.player.PD), var1 + BX + AvMain.fraMoney.frameWidth + 91, var2 - 5, 0);
      }

   }

   public static void paintPvPNew(mGraphics var0, MainObject var1, MainObject var2) {
      int var3 = MotherCanvas.hw;
      if (AD == null) {
         AE();
      } else {
         var0.drawRegion((mImage)AD[0], var3 - 84, 30, 3);
         var0.drawRegion(AD[0], 0, 0, 129, 40, 2, var3 + 84, 30, 3);
         var0.drawRegion((mImage)AD[4], var3, 18, 3);
         BL.paintCountDownTicket(var0, mFont.tahoma_7_white, var3 + 1, 13, 2);
         if (var1 != null) {
            AA(var0, (byte)(var1.Lv == 100 ? 96 : 98), var1.Hp, var1.maxHp, var3 - 110, 14, 0, 9, 87, 0, false, var1.KI, true, 0);
            AA(var0, (byte)97, var1.Mp, var1.maxMp, var3 - 112 + 5, 28, 0, 9, 50, 4, false, 0, true, 0);
            var1.AB(var0, var3 - 130, 26, 2);
            if (var1.Lv < 100) {
               AvMain.AB(var0, String.valueOf(var1.Lv), var3 - 116, 36, 2, (byte)0);
            } else {
               AvMain.AB(var0, String.valueOf(var1.LvThongThao), var3 - 115, 36, 2, (byte)5);
            }
         }

         if (var2 != null) {
            AA(var0, (byte)(var2.Lv == 100 ? 96 : 98), var2.Hp, var2.maxHp, var3 + 24, 14, 0, 9, 87, 0, true, var2.KI, true, 0);
            AA(var0, (byte)97, var2.Mp, var2.maxMp, var3 + 57, 28, 0, 9, 50, 4, true, 0, true, 0);
            var2.AB(var0, var3 + 130, 26, 0);
            if (var2.Lv < 100) {
               AvMain.AB(var0, String.valueOf(var2.Lv), var3 + 116, 36, 2, (byte)0);
            } else {
               AvMain.AB(var0, String.valueOf(var2.LvThongThao), var3 + 117, 36, 2, (byte)5);
            }
         }

         if (BV > 0 && var1 != null && var2 != null) {
            for(int var4 = 1; var4 <= BV; ++var4) {
               if (var1.typePK == 4) {
                  var0.drawRegion(AD[3], 0, BL.valueLeft >= var4 ? 14 : 0, 7, 7, 0, var3 - 9 - (var4 << 3), 30, 3);
                  var0.drawRegion(AD[3], 0, BL.valueright >= var4 ? 7 : 0, 7, 7, 0, var3 + 9 + (var4 << 3), 30, 3);
               } else {
                  var0.drawRegion(AD[3], 0, BL.valueLeft >= var4 ? 14 : 0, 7, 7, 0, var3 + 9 + (var4 << 3), 30, 3);
                  var0.drawRegion(AD[3], 0, BL.valueright >= var4 ? 7 : 0, 7, 7, 0, var3 - 9 - (var4 << 3), 30, 3);
               }
            }
         }

      }
   }

   public static void AE() {
      AD = new mImage[5];

      for(int var0 = 0; var0 < AD.length; ++var0) {
         AD[var0] = mImage.createImage("/interface/pvpnew" + var0 + ".png");
      }

   }

   public static void AA(mGraphics var0, int var1, int var2, boolean var3, int var4) {
      if (var4 == 0) {
         if (var3 || var2 + EM + 18 > 0) {
            var4 = var2 + EM;
            if (var3) {
               var4 = var2;
            }

            AvMain.paintRect(var0, var1, var4, BW, 18, (byte)1, 4);
            var4 += 9;
            AvMain.fraMoney.drawFrame(0, var1 + BX + AvMain.fraMoney.frameWidth / 2 - 3, var4, 0, 3, var0);
            AvMain.fraMoney.drawFrame(1, var1 + BX + AvMain.fraMoney.frameWidth / 2 + 45, var4, 0, 3, var0);
            AvMain.fraMoney.drawFrame(7, var1 + BX + AvMain.fraMoney.frameWidth / 2 + 90, var4, 0, 3, var0);
            if (GameScreen.player != null) {
               mFont.tahoma_7_yellow.drawString(var0, " " + AvMain.AB(Player.SN), var1 + BX + AvMain.fraMoney.frameWidth - 4, var4 - 5, 0);
               mFont.tahoma_7_red.drawString(var0, " " + AvMain.AA((long)GameScreen.player.Ruby), var1 + BX + AvMain.fraMoney.frameWidth + 45, var4 - 5, 0);
               mFont.tahoma_7_green.drawString(var0, " " + AvMain.AB((long)GameScreen.player.PD), var1 + BX + AvMain.fraMoney.frameWidth + 91, var4 - 5, 0);
            }
         }
      } else if (var4 == 1 && (var3 || var2 + EM + 18 > 0)) {
         var4 = var2 + EM;
         if (var3) {
            var4 = var2;
         }

         AvMain.paintRect(var0, var1, var4, BW, 18, (byte)1, 3);
         var4 += 9;
         AvMain.fraMoney.drawFrame(2, var1 + BX + AvMain.fraMoney.frameWidth / 2, var4, 0, 3, var0);
         AvMain.fraMoney.drawFrame(3, var1 + BX + AvMain.fraMoney.frameWidth / 2 + 45, var4, 0, 3, var0);
         AvMain.fraMoney.drawFrame(4, var1 + BX + AvMain.fraMoney.frameWidth / 2 + 93, var4, 0, 3, var0);
         if (GameScreen.player != null) {
            mFont.tahoma_7_yellow.drawString(var0, " " + Player.Ticket, var1 + BX + AvMain.fraMoney.frameWidth, var4 - 5, 0);
            mFont.tahoma_7_yellow.drawString(var0, " " + Player.RG, var1 + BX + AvMain.fraMoney.frameWidth + 45, var4 - 5, 0);
            mFont.tahoma_7_yellow.drawString(var0, " " + Player.RH, var1 + BX + AvMain.fraMoney.frameWidth + 93, var4 - 5, 0);
         }
      }

      if (BY > 0) {
         --BY;
         if (EM < 0 && (EM += 5) > 0) {
            EM = 0;
            return;
         }
      } else if (EM > -25 && (EM -= 3) < -25) {
         EM = -25;
      }

   }

   private static void AQ(mGraphics var0) {
      EI = false;
      EJ = false;
      int var1 = 0;

      for(int var2 = 0; var2 < vecEffCurrent.size(); ++var2) {
         MainItem var3;
         if (var2 < 10 && (var3 = (MainItem)vecEffCurrent.elementAt(var2)) != null && DelaySkill.getDelay(var3.indexHotKey).AC > 0) {
            if (var3.typeObject == 4) {
               if (var3.BQ == 1) {
                  EI = true;
               }

               if (var3.BQ == 2) {
                  EJ = true;
               }

               var3.AA(var0, CZ[var1][0], CZ[var1][1] - 4, 20, -4);
               DelaySkill.getDelay(var3.indexHotKey).AA(var0, CZ[var1][0] - 10, CZ[var1][1] - 10 - 4, 20);
            } else if (var3.typeObject == 9) {
               var3.AA(var0, CZ[var1][0], CZ[var1][1] - 4, 20, 0);
               var0.drawRegion((mImage)AvMain.imgBgnum, CZ[var1][0], CZ[var1][1] + 10, 3);
               DelaySkill var10000 = DelaySkill.getDelay(var3.indexHotKey);
               int var10002 = CZ[var1][0] - 11;
               int var10003 = CZ[var1][1] - 1;
               byte var7 = var3.colorName;
               boolean var9 = true;
               int var6 = var10003;
               int var5 = var10002;
               DelaySkill var10 = var10000;
               int var8;
               String var11;
               if ((var8 = var10000.AC / 1000) == 0) {
                  var11 = "0." + var10.AC % 1000 / 100;
               } else {
                  var11 = "" + var8;
               }

               AvMain.AB(var7).drawString(var0, var11, var5 + 11, var6 + 11 - 5, 2);
            }

            ++var1;
         }
      }

   }

   public static void paintIconFocus(mGraphics var0) {
      if (GameScreen.objFocus != null && GameScreen.objFocus.typeObject != 0 && GameScreen.objFocus.typeObject != 1 && !GameScreen.objFocus.returnAction() && !GameScreen.objFocus.isTanHinh) {
         MainObject var1 = GameScreen.objFocus;
         if (AvMain.fraIconfocus.nFrame == 1) {
            var0.drawRegion((mImage)AvMain.fraIconfocus.imgFrame, var1.x, var1.y - var1.hOne - var1.dy - var1.hIconFocus - GameCanvas.gameTick % 5, 3);
            return;
         }

         if (EO) {
            if (GameCanvas.gameTick % 3 == 0) {
               ++EN;
            }

            if (EN >= AvMain.fraIconfocus.nFrame - 1) {
               EO = false;
            }
         } else {
            if (GameCanvas.gameTick % 3 == 0) {
               --EN;
            }

            if (EN <= 0) {
               EO = true;
            }
         }

         AvMain.fraIconfocus.drawFrame(EN, var1.x, var1.y - var1.hOne - var1.dy - var1.hIconFocus - 3, 0, 3, var0);
      }

   }

   public static void paintInfoFocus(mGraphics var0) {
      if (GameScreen.objFocus != null && !GameScreen.objFocus.returnAction() && (GameScreen.objFocus.name.length() > 0 || GameScreen.objFocus.BQ() == 99)) {
         int var1 = xFocus - 2;
         if (GameCanvas.isTaiTho) {
            var1 -= 5;
         }

         int var2 = 4 + GameScreen.h12plus;
         GameScreen.objFocus.AB(var0, var1, var2);
      }

   }

   public static void AA(mGraphics var0, byte var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10, int var11, boolean var12, int var13) {
      int var14 = var2;
      if (var9 == 1) {
         var14 = var2 / 10;
      }

      int var15 = -3145696;
      int var16 = -11325920;
      if (var1 < 0) {
         if (CRes.abs(var1) == 2) {
            var15 = -14512428;
            var16 = -11970730;
         }
      } else if (var1 == 2) {
         var15 = -14512428;
         var16 = -11970730;
      } else if (var1 == 3) {
         var15 = -12990179;
         var16 = -15846905;
      } else if (var1 == 99) {
         var16 = -3145696;
         var15 = -14644472;
      } else if (var1 == 100) {
         var16 = -14644472;
         var15 = -13823279;
      } else if (var1 == 101) {
         var16 = -13823279;
         var15 = -15219243;
      } else if (var1 != 102 && var1 != 104 && var1 != 106) {
         if (var1 == 103) {
            var15 = -10561537;
            var16 = -13848338;
            var0.setColor(-16755238);
            var0.fillRect(var4 - 1, var5 - 1, var8 + 2, var7 + 2);
         } else if (var1 == 98 || var1 == 97 || var1 == 96) {
            var15 = -10561537;
            var16 = -13848338;
         }
      } else {
         if ((var13 /= 10) < 0) {
            var13 = 0;
         } else if (var13 >= EP.length) {
            var13 = EP.length - 1;
         }

         if (var2 < var3 / 10 && GameCanvas.gameTick % 5 < 2) {
            var15 = EP[var13][2];
         } else {
            var15 = EP[var13][0];
         }

         var16 = EP[var13][1];
      }

      if (var1 != 98 && var1 != 97 && var1 != 96) {
         var0.setColor(var16);
         var0.fillRect(var4, var5, var8, var7);
      }

      boolean var19 = false;
      if (var3 > 0 && var14 > 0) {
         if (var3 > 1000000) {
            var13 = var14 / 1000 * var8 / (var3 / 1000);
         } else {
            var13 = var14 * var8 / var3;
         }

         if (var13 <= 0) {
            var13 = 1;
         } else if (var13 > var8) {
            var13 = var8;
         }

         if (var11 > 0) {
            if (var3 > 1000000) {
               var11 = var11 / 1000 * var8 / (var3 / 1000);
            } else {
               var11 = var11 * var8 / var3;
            }

            if (var11 <= 0) {
               var11 = 1;
            } else if (var11 > var8) {
               var11 = var8;
            }

            if (var1 != 98 && var1 != 97 && var1 != 96) {
               var0.setColor(0);
               if (!var10) {
                  var0.fillRect(var4, var5, var11, var7);
               } else {
                  var0.fillRect(var4 + var8 - var11, var5, var11, var7);
               }
            } else if ((var1 == 98 || var1 == 96) && var11 > 0) {
               if (!var10) {
                  var0.drawRegion(AD[1], 0, var7 + var7, var11, var7, 0, var4, var5, 0);
               } else {
                  var0.drawRegion(AD[1], 0, var7 + var7, var11, var7, 2, var4 + (var8 - var11), var5, 0);
               }
            }
         }

         var0.setColor(var15);
         byte var18;
         if (var1 != 98 && var1 != 97 && var1 != 96) {
            if (var1 < 0) {
               var0.fillRect(var4, var5, var13, var7);
            } else {
               if (!var10) {
                  var0.fillRect(var4, var5, var13, var7);
               } else {
                  var0.fillRect(var4 + var8 - var13, var5, var13, var7);
               }

               if (var1 == 106) {
                  var0.setColor(-656556);
                  if (!var10) {
                     var0.fillRect(var4, var5, var13, 1);
                  } else {
                     var0.fillRect(var4 + var8 - var13, var5, var13, 1);
                  }
               }
            }

            if (var12 && var14 > EK.length << 1) {
               if (var1 == 1) {
                  if (EQ + (EK.length << 1) - 1 <= var13) {
                     for(var11 = 0; var11 < EK.length; ++var11) {
                        var0.setColor(EK[var11]);
                        var18 = 2;
                        if (var11 == EK.length - 1) {
                           var18 = 1;
                        }

                        var0.fillRect(var4 + (var11 << 1) + EQ, var5, var18, var7);
                     }
                  }

                  if ((EQ += 2) + (EK.length << 1) > var8) {
                     EQ = 0;
                  }
               } else {
                  if (ER + (EL.length << 1) - 1 <= var13) {
                     for(var11 = 0; var11 < EL.length; ++var11) {
                        var0.setColor(EL[var11]);
                        var18 = 2;
                        if (var11 == EL.length - 1) {
                           var18 = 1;
                        }

                        var0.fillRect(var4 + (var11 << 1) + ER, var5, var18, var7);
                     }
                  }

                  if ((ER += 2) + (EL.length << 1) > var8) {
                     ER = 0;
                  }
               }
            }
         } else {
            var11 = 0;
            var18 = 1;
            if (var1 == 96) {
               var11 = var7;
            }

            if (var1 == 97) {
               var18 = 2;
            }

            if (var13 > 0) {
               if (!var10) {
                  var0.drawRegion(AD[var18], 0, var11, var13, var7, 0, var4, var5, 0);
               } else {
                  var0.drawRegion(AD[var18], 0, var11, var13, var7, 2, var4 + (var8 - var13), var5, 0);
               }
            }
         }
      }

      String var21 = "" + var3;
      String var20 = "" + var2;
      if (var3 >= 1000000000) {
         var21 = var3 / 1000000000 + "," + var3 % 1000000000 / 100000000 + "B";
      } else if (var3 > 1000000) {
         var21 = var3 / 1000000 + "," + var3 % 1000000 / 100000 + "M";
      } else if (var3 >= 100000) {
         var21 = var3 / 1000 + "k";
      }

      if (var2 >= 100000) {
         var20 = var2 / 1000 + "k";
      }

      if (var9 == 0) {
         mFont.tahoma_7_white.drawString(var0, var20 + "/" + var21, var4 + var8 / 2, var5 - 1 + var6 + (var7 - 9) / 2, 2);
      } else if (var9 == 1) {
         mFont.tahoma_7_white.drawString(var0, MainItem.AA(var2, (byte)1), var4 + var8 / 2, var5 - 1 + var6 + (var7 - 9) / 2, 2);
      } else if (var9 == 2) {
         if (var1 == 104) {
            int var17 = mFont.tahoma_7_black.getWidth(var20 + "/" + var21) + 4;
            var0.setColor(0);
            var0.fillRect(var4 + var8 / 2 - 10 - var17 / 2, var5 - 1 + var6 + (var7 - 9) / 2, var17, 11);
         }

         AvMain.AB(var0, var20 + "/" + var21, var4 + var8 / 2 - 10, var5 - 1 + var6 + (var7 - 9) / 2, 2, (byte)0);
      } else if (var9 == 3) {
         AvMain.AB(var0, var20 + "/" + var21, var4 + var8 / 2, var5 - 1 + var6 + (var7 - 9) / 2, 2, (byte)0);
      } else {
         if (var9 == 4) {
            mFont.tahoma_7_white.drawString(var0, var20, var4 + var8 / 2, var5 - 1 + var6 + (var7 - 9) / 2, 2);
         }

      }
   }

   public static String AA(int var0) {
      String var1 = "" + var0;
      if (var0 >= 1000000000) {
         var1 = var0 / 1000000000 + "," + var0 % 1000000000 / 100000000 + "B";
      } else if (var0 >= 1000000) {
         var1 = var0 / 1000000 + "," + var0 % 1000000 / 100000 + var0 % 100000 / 10000 + "M";
      } else if (var0 >= 1000) {
         var1 = var0 / 1000 + "k";
      }

      return var1;
   }

   public static String AB(int var0) {
      if (var0 < 0) {
         return "00:00:00";
      } else {
         String var1 = "";
         if (var0 / 60 / 60 < 10) {
            var1 = var1 + "0" + var0 / 60 / 60;
         } else {
            var1 = var1 + var0 / 60 / 60;
         }

         var1 = var1 + ":";
         if (var0 / 60 % 60 < 10) {
            var1 = var1 + "0" + var0 / 60 % 60;
         } else {
            var1 = var1 + var0 / 60 % 60;
         }

         var1 = var1 + ":";
         if (var0 % 60 < 10) {
            var1 = var1 + "0" + var0 % 60;
         } else {
            var1 = var1 + var0 % 60;
         }

         return var1;
      }
   }

   public static void AA(mGraphics var0, int var1, int var2, int var3, int var4, int var5, boolean var6) {
      int var7 = -5204871;
      byte var8 = 0;
      if (var6) {
         var7 = -1319;
         var8 = 2;
      }

      if (AvMain.HC == null) {
         AvMain.HC = LoadImageStatic.AA("/interface/thong_thao_2.png", 10, 12);
      }

      var0.setColor(var7);
      var0.fillRect(var1 - 1, var2, var3 + 2, 12);

      int var9;
      for(var9 = 0; var9 <= (var3 - 1) / 10; ++var9) {
         if (var9 < (var3 - 1) / 10) {
            AvMain.HC.drawFrame(var8 + 0, var1 + var9 * 10, var2, 0, 0, var0);
         } else {
            var0.drawRegion(AvMain.HC.imgFrame, 0, var8 * 12, (var3 - 1) % 10 + 1, 12, 0, var1 + var9 * 10, var2, 0);
         }
      }

      if ((var9 = var4 * var3 / var5) < 0) {
         var9 = 0;
      } else if (var9 > var3) {
         var9 = var3;
      }

      if (var9 > 0) {
         for(var7 = 0; var7 <= (var9 - 1) / 10; ++var7) {
            if (var7 < (var9 - 1) / 10) {
               AvMain.HC.drawFrame(var8 + 1, var1 + var7 * 10, var2, 0, 0, var0);
            } else {
               var0.drawRegion(AvMain.HC.imgFrame, 0, 12 + var8 * 12, (var9 - 1) % 10 + 1, 12, 0, var1 + var7 * 10, var2, 0);
            }
         }
      }

      mFont.tahoma_7_white.drawString(var0, var4 + "/" + var5, var1 + var3 / 2, var2, 2);
   }

   public static void PaintLoadData(mGraphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var0.setColor(-11775147);
      var0.fillRect(var3 - 5, var4 + 1 + 15, 104, 10);
      var0.fillRect(var3 - 2, var4 - 2 + 15, 98, 16);
      var0.setColor(-14078674);
      var0.fillRect(var3 - 4, var4 + 15, 102, 12);
      var0.fillRect(var3 - 4 + 1, var4 + 14, 100, 1);
      var0.fillRect(var3 - 4 + 1, var4 + 15 + 12, 100, 1);
      var0.setColor(-13749709);
      var0.fillRect(var3 - 4 + 1, var4 + 15, 100, 12);
      boolean var8 = false;
      if (var2 > 0 && var1 > 0) {
         if ((var5 = var1 * 100 / var2) <= 0) {
            var5 = 1;
         } else if (var5 > 100) {
            var5 = 100;
         }

         var0.setColor(-6437568);
         var0.fillRect(var3 - 4 + 1, var4 + 15, var5, 12);
      }

      var0.setColor(-14078674);
      var0.fillRect(var3 - 3, var4 + 12 + 3, 1, 1);
      var0.fillRect(var3 - 3, var4 + 14 + 12, 1, 1);
      var0.fillRect(var3 + 100 - 4, var4 + 12 + 3, 1, 1);
      var0.fillRect(var3 + 100 - 4, var4 + 14 + 12, 1, 1);
      mFont.tahoma_7b_white.drawString(var0, var1 + "/" + var2, var3 + 50 - 3, var4 + 15, 2);
   }

   public final void AF() {
      if (timePointer > 0) {
         --timePointer;
      }

      int var2;
      int var3;
      if (CO > 0 && GameCanvas.isPointerSelect) {
         var3 = GameCanvas.hText + 8;

         for(var2 = 0; var2 < CA.size(); ++var2) {
            if (GameCanvas.isPoint(MotherCanvas.w - 70, 60 + var3 * var2, 70, var3)) {
               GameCanvas.isPointerSelect = false;
               MainObject var9;
               if ((var9 = (MainObject)CA.elementAt(var2)) != null && !var9.returnAction()) {
                  GameScreen.objFocus = var9;
                  var9.BD();
                  isPaintInfoFocus = true;
                  CO = 0;
               }
               break;
            }
         }
      }

      if (LoadMap.specMap != 3) {
         isMove = true;
         if (vecClanDam != null && vecClanDam.size() != 0) {
            if (GameCanvas.isPoint(MotherCanvas.w - 14, MotherCanvas.h / 2 - 35, 16, 40)) {
               if (GameCanvas.isPointerSelect) {
                  GameCanvas.isPointerSelect = false;
                  this.isFocusTime = false;
                  this.FU = true;
               } else if (GameCanvas.isPointerDown || GameCanvas.AQ) {
                  this.isFocusTime = true;
                  this.timepointer = 3;
               }
            }

            if ((GameCanvas.AR || GameCanvas.isPointerDown) && this.xShow == 0) {
               this.FU = true;
            }

            if (this.timepointer > 0) {
               --this.timepointer;
            }

            if (this.FU) {
               if (this.xShow == 0) {
                  this.FT = 5;
               }

               if (this.xShow == 75) {
                  this.FT = -5;
               }

               this.xShow += this.FT;
               if (this.xShow == 0 || this.xShow == 75) {
                  this.FU = false;
               }

               if (this.FT > 0 && this.xShow >= 75) {
                  this.xShow = 75;
               } else if (this.FT < 0 && this.xShow <= 0) {
                  this.xShow = 0;
               }
            }
         }

         for(var2 = 0; var2 < mPosOther.length; ++var2) {
            if ((var2 != 2 || !GameCanvas.loadmap.mapLang() || BP > 0) && (var2 != 5 || GameScreen.player.clan != null) && GameCanvas.isPoint(mPosOther[var2][0] - 2, mPosOther[var2][1] - 2, CX[var2][0] + 4, CX[var2][1] + 4)) {
               if (GameCanvas.isPointerSelect) {
                  GameCanvas.isPointerSelect = false;
                  this.selectPointer(var2);
                  isMove = false;
                  break;
               }

               if (GameCanvas.isPointerDown || GameCanvas.AQ) {
                  keyPoint = var2 + 100;
                  timePointer = 3;
                  break;
               }
            }
         }

         if (GameCanvas.isPoint(xNumMess, yNumMess - 3, 22, 22)) {
            if (GameCanvas.isPointerSelect) {
               GameCanvas.isPointerSelect = false;
               this.selectPointer(7);
               isMove = false;
            } else if (GameCanvas.isPointerDown || GameCanvas.AQ) {
               keyPoint = 106;
               timePointer = 3;
            }
         }
         int menuX = xAutoFire + 3;
            int menuY = yAutoFire - 20;
            // mở rộng vùng chạm cho dễ bấm
            int pad = AThMadaraMOD.MENU_TOUCH_PAD; // bạn đã khai báo hằng này ở AThMadaraMOD
            int menuW = AThMadaraMOD.MENU_AUTO_W;
            int menuH = AThMadaraMOD.MENU_AUTO_H;

            if (GameCanvas.isPoint(menuX - pad, menuY - pad, menuW + pad * 2, menuH + pad * 2)) {
                if (GameCanvas.isPointerSelect) {
                    GameCanvas.isPointerSelect = false;
                    // mở menu auto
                    AThMadaraMOD.getInstance().openMenuAuto();
                    isMove = false;
                    // nếu muốn, show thông báo debug:
                    // Interface_Game.addInfoPlayerNormal("Opened MenuAuto", mFont.tahoma_7_yellow);
                    // dừng kiểm tra vùng khác
                } else if (GameCanvas.isPointerDown || GameCanvas.AQ) {
                    // giữ visual feedback giống các nút khác
                    keyPoint = 106; // (hoặc bạn có thể dùng một key khác nếu muốn)
                    timePointer = 3;
                }
            }
         if (GameCanvas.isPoint(xAutoFire, yAutoFire - 3, 22, 22) && GameCanvas.isPointerSelect) {
            GameCanvas.isPointerSelect = false;
            this.selectPointer(6);
            isMove = false;
         }

         if (GameCanvas.AB(MotherCanvas.w - 74, 0, 74, 40) && GameScreen.objFocus != null && (GameScreen.objFocus.typeObject == 0 || GameScreen.objFocus.typeObject == 2)) {
            GameCanvas.isPointerSelect = false;
            GameScreen.objFocus.BD();
         }

         if (isMove) {
            if (this.DS == 0) {
               for(var2 = 0; var2 < AP.length; ++var2) {
                  if (GameCanvas.isPoint(AP[var2][0] - wSkill / 2, AP[var2][1] - wSkill / 2, wSkill, wSkill)) {
                     if (GameCanvas.isPointerSelect) {
                        Hotkey var10;
                        if (GameScreen.objFocus == null && (var10 = Player.hotkeyPlayer[Player.currentTab][var2]).skill != null && !var10.skill.isBuff) {
                           GameScreen.player.AC(true);
                        }

                        Player.IndexFire = var2;
                        GameScreen.player.setActionHotKey(var2);
                        break;
                     }

                     if (GameCanvas.isPointerDown || GameCanvas.AQ) {
                        keyPoint = DA[var2];
                        timePointer = 3;
                     }
                  }
               }

               if (!GameCanvas.loadmap.mapLang() && GameCanvas.isTouch && GameScreen.isShowSkillBuff) {
                  for(var2 = 0; var2 < CU.length; ++var2) {
                     if (GameCanvas.isPoint(CU[var2][0] - wSkill / 2, CU[var2][1] - wSkill / 2, wSkill, wSkill) && GameCanvas.isPointerSelect) {
                        GameScreen.player.setActionHotKeyBuff(var2);
                        keyPoint = var2 + 200;
                        timePointer = 3;
                        break;
                     }
                  }
               }
            }

            Interface_Game var11 = this;
            int var4;
            int var5;
            if (this.ES > 0) {
               --this.ES;
            } else {
               var3 = GameCanvas.AY + MainScreen.cameraMain.xCam;
               var4 = GameCanvas.AZ + MainScreen.cameraMain.yCam;

               for(var5 = 0; var5 < LoadMap.vecPointChange.size(); ++var5) {
                  Point var6;
                  if (MainObject.getDistance((var6 = (Point)LoadMap.vecPointChange.elementAt(var5)).x, var6.y, var3, var4) < 28 && CRes.abs(GameScreen.player.x - var6.x) < 48 && CRes.abs(GameScreen.player.y - var6.y) < 72 && GameScreen.player.Hp > 0 && GameScreen.player.typeActionBoat == 0 && GameScreen.player.Action == 0) {
                     var11.ES = 20;
                     GlobalService.getInstance().Obj_Move((short)var6.x, (short)var6.y);
                  }
               }
            }

            MainObject var13;
            if (typeTouch == 1) {
               if (!Player.isGhost && !Player.isBlock) {
                  var3 = GameCanvas.AY + MainScreen.cameraMain.xCam;
                  var4 = GameCanvas.AZ + MainScreen.cameraMain.yCam;
                  var13 = null;
                  if (GameCanvas.isPointerSelect && this.DR == 0) {
                     this.EV = false;
                     if (MainObject.getDistance(var3, var4, GameScreen.player.x, GameScreen.player.y) <= Player.wFocus - 15 || GameScreen.player.Action == 4) {
                        var13 = this.AA(var3, var4);
                     }

                     if (var13 != null && var13.typeObject != 1) {
                        Player.setStart_EndAutoFire(false);
                     }

                     if (var13 != null) {
                        GameScreen.objFocus = var13;
                        if (MainObject.getDistance(var13.x, var13.y, GameScreen.player.x, GameScreen.player.y) <= Player.wFocus && GameScreen.player.Action != 4) {
                           GameCanvas.isPointerSelect = false;
                           GameScreen.objFocus.a_();
                           isPaintInfoFocus = true;
                           if (GameScreen.player.AC(GameScreen.objFocus)) {
                              GameScreen.addEffectEnd_ObjTo((short)24, 0, GameScreen.objFocus.x, GameScreen.objFocus.y, (short)GameScreen.objFocus.ID, (byte)GameScreen.objFocus.typeObject, (byte)0, (MainObject)null);
                              this.EV = true;
                           }

                           this.DU = null;
                        }
                     }

                     int var14;
                     if ((var14 = GameCanvas.loadmap.AA(var3, var4)) == -1 || var14 == 1) {
                        int var1 = var3 - 24;
                        var5 = var4 + 24;
                        boolean var7 = false;
                        if (var4 + MainScreen.cameraMain.yCam > GameCanvas.loadmap.maxHMap - 140) {
                           var5 = var4 - 24;
                        }

                        for(int var8 = 0; var8 < 3; ++var8) {
                           int var15;
                           if ((var15 = GameCanvas.loadmap.AA(var1 + var8 * 24, var5)) != -1 && var15 != 1) {
                              var3 = var1 + var8 * 24;
                              var4 = var5;
                              var14 = GameCanvas.loadmap.AA(var3, var5);
                              break;
                           }
                        }
                     }

                     if (var14 != -1 && var14 != 1 && GameScreen.player.Action != 4) {
                        if (var3 > GameScreen.player.x) {
                           this.DV = 2;
                        } else {
                           this.DV = 0;
                        }

                        this.DU = GameCanvas.loadmap.AA(var3 / LoadMap.wTile, var4 / LoadMap.wTile, GameScreen.player.x / LoadMap.wTile, GameScreen.player.y / LoadMap.wTile, 100, GameScreen.player);
                        this.ET = var3;
                        this.EU = var4;
                        if (this.DU != null && this.DU.length > 100) {
                           this.DU = null;
                        }

                        this.DR = 2;
                     } else {
                        this.DU = null;
                        if (CA.size() > 0) {
                           GameCanvas.isPointerSelect = false;
                        }
                     }
                  }

                  if (this.DR > 0) {
                     if (this.DR == 1 && this.DU != null && GameScreen.player.Action != 4 && GameScreen.player.Action != 2) {
                        GameScreen.player.AD = 0;
                        GameScreen.player.AE = 0;
                        GameScreen.player.toX = GameScreen.player.x;
                        GameScreen.player.toY = GameScreen.player.y;
                        if (GameScreen.player.posTransRoad != null) {
                           GameScreen.player.AC = 2;
                        }

                        GameScreen.player.posTransRoad = this.DU;
                        GameScreen.player.Dir = this.DV;
                        var3 = this.EU;
                        EB = this.ET;
                        EC = var3;
                        ED = 0;
                        this.DU = null;
                        if (!this.EV) {
                           Player.setStart_EndAutoFire(false);
                           if (isPaintInfoFocus) {
                              isPaintInfoFocus = false;
                           }

                           if (GameScreen.player.skillCurrent != null) {
                              GameScreen.player.skillCurrent = null;
                           }
                        }

                        GameCanvas.isPointerSelect = false;
                     }

                     --this.DR;
                  }

                  AR();
                  return;
               }
            } else if (typeTouch == 0) {
               if ((GameCanvas.isPointerDown || GameCanvas.AQ) && GameCanvas.AD(xPointMove - 2 * wArrowMove, yPointMove - 2 * wArrowMove, wArrowMove << 2, wArrowMove << 2)) {
                  byte var12;
                  if ((var3 = CRes.AA(GameCanvas.AY - xPointMove, GameCanvas.AZ - yPointMove)) > 45 && var3 <= 135) {
                     var12 = 3;
                  } else if (var3 > 135 && var3 <= 225) {
                     var12 = 0;
                  } else if (var3 > 225 && var3 <= 315) {
                     var12 = 2;
                  } else {
                     var12 = 1;
                  }

                  GameCanvas.AH();
                  GameCanvas.isPointerDown = true;
                  GameCanvas.isPointerSelect = false;
                  keyPoint = this.AR[var12];
                  GameCanvas.AL[keyPoint] = true;
                  timePointer = 3;
                  Player.setStart_EndAutoFire(false);
                  if (GameScreen.player.skillCurrent != null) {
                     GameScreen.player.skillCurrent = null;
                  }
               }

               if (GameCanvas.isPointerSelect && !GameCanvas.isPointer(xPointMove - 50, yPointMove - 50, 100, 100)) {
                  var3 = GameCanvas.AY + MainScreen.cameraMain.xCam;
                  var4 = GameCanvas.AZ + MainScreen.cameraMain.yCam;
                  if ((var13 = this.AA(var3, var4)) != null) {
                     GameScreen.objFocus = var13;
                     var13.a_();
                  }
               }
            }
         }
      }

   }

   public final void AG() {
      AR();
   }

   private static void AR() {
      if (GameCanvas.currentScreen == GameCanvas.gameScr) {
         if (GameCanvas.AQ) {
            if (!GameScreen.CC && (CRes.abs(GameCanvas.AY - GameCanvas.BA) > 48 || CRes.abs(GameCanvas.AZ - GameCanvas.BB) > 48)) {
               GameScreen.CC = true;
            }

            GameScreen.CF = GameCanvas.AY - GameCanvas.BA;
            GameScreen.CG = GameCanvas.AZ - GameCanvas.BB;
            GameScreen.CJ = 40;
            return;
         }

         if (GameCanvas.isPointerDown) {
            GameScreen.CH = MainScreen.cameraMain.xCam;
            GameScreen.CI = MainScreen.cameraMain.yCam;
            GameScreen.CF = 0;
            GameScreen.CG = 0;
         }
      }

   }

   public final void selectPointer(int var1) {
      switch(var1) {
      case 0:
         GameCanvas.gameScr.AP.AD();
         return;
      case 1:
         GameCanvas.gameScr.AX.AD();
         return;
      case 2:
         if (this.DS <= 0 && !Player.isGhost && GameScreen.player.Action != 4) {
            this.DS = 1;
            if (GameCanvas.isTouch && typeTouch != 1) {
               for(var1 = 0; var1 < CY.length; ++var1) {
                  CY[var1][0] = (MotherCanvas.w - AP[var1][0]) * 200 / this.maxTimeChange;
                  CY[var1][1] = 0;
               }
            } else {
               for(var1 = 0; var1 < CY.length; ++var1) {
                  CY[var1][0] = 0;
                  CY[var1][1] = (MotherCanvas.h - AP[var1][1]) * 200 / this.maxTimeChange;
               }
            }

            GameCanvas.clearAll();
            return;
         }
         break;
      case 3:
         GameCanvas.gameScr.AL.AD();
         return;
      case 4:
         QuickMenu.AI().AJ();
         return;
      case 5:
         GameCanvas.gameScr.BM.AD();
         return;
      case 6:
         if (!isAutoFireInterface && Player.typeAutoFireMain == -1) {
            addInfoPlayerNormal(T.NU, mFont.tahoma_7_yellow);
            return;
         }

         isAutoFireInterface = !isAutoFireInterface;
         break;
      case 7:
         GameCanvas.eventScr.Show((MainScreen)GameCanvas.gameScr);
         return;
      }

   }

   public static void AH() {
      QuickMenu.AI().AJ();
   }

   public static void AI() {
      int var0;
      if (GameCanvas.isTouch) {
         int var1;
         if (typeTouch == 0) {
            DE = MotherCanvas.w - 35;
            DF = MotherCanvas.h - 50;
            var0 = DB;
            var1 = DB - 30;

            int var2;
            for(var2 = 0; var2 < AP.length; ++var2) {
               if (var2 == 2) {
                  AP[var2][0] = DE;
                  AP[var2][1] = DF;
               } else {
                  AP[var2][0] = DE + CRes.getcos(CRes.fixangle(var0)) * DC / 1000;
                  AP[var2][1] = DF + CRes.getsin(CRes.fixangle(var0)) * DC / 1000;
                  var0 -= 35;
               }

               CU[var2][0] = DE + CRes.getcos(CRes.fixangle(var1)) * (DC + 40) / 1000;
               CU[var2][1] = DF + CRes.getsin(CRes.fixangle(var1)) * (DC + 40) / 1000;
               var1 -= 20;
               CV[var2][0] = (MotherCanvas.w << 1) - AP[var2][0];
               CV[var2][1] = AP[var2][1];
            }

            DE = wSkill / 2;
            DF = 80;

            for(var2 = 0; var2 < AP.length; ++var2) {
               CZ[var2][0] = DE;
               CZ[var2][1] = DF + var2 * wSkill;
               CZ[var2 + 6][0] = DE + wSkill / 2;
               CZ[var2 + 6][1] = DF + var2 * wSkill;
            }
         } else if (typeTouch == 1) {
            DE = MotherCanvas.w - wSkill * 6 - wSkill / 2;
            DF = MotherCanvas.h - 24;

            for(var0 = 0; var0 < AP.length; ++var0) {
               AP[var0][0] = DE + var0 * wSkill;
               AP[var0][1] = DF;
               CV[var0][0] = AP[var0][0];
               CV[var0][1] = (MotherCanvas.h << 1) - AP[var0][1];
               CZ[var0][0] = DE + var0 * wSkill;
               CZ[var0][1] = DF - wSkill;
               CZ[var0 + 6][0] = DE + var0 * wSkill;
               CZ[var0 + 6][1] = DF - (wSkill << 1) + 4;
            }

            DE = wSkill / 2;
            var0 = wSkill;
            if (GameCanvas.isTaiTho) {
               DE += 10;
            }

            for(var1 = 0; var1 < CU.length; ++var1) {
               CU[var1][0] = DE + var1 % 3 * var0;
               CU[var1][1] = DF - var0 * (var1 / 3);
            }
         }
      } else {
         DE = MotherCanvas.hw - 70;
         DF = MotherCanvas.h - GameCanvas.hCommand - 5;

         for(var0 = 0; var0 < AP.length; ++var0) {
            AP[var0][0] = DE + var0 * wSkill;
            AP[var0][1] = DF;
            CV[var0][0] = AP[var0][0];
            CV[var0][1] = (MotherCanvas.h << 1) - AP[var0][1];
            CZ[var0][0] = DE + var0 * wSkill;
            CZ[var0][1] = DF - wSkill;
         }
      }

      DL = yInfoServer + 40;
      DK = MotherCanvas.w / 2;
   }

   public static void setPosTouch() {
      if (typeTouch == 1) {
         mPosOther[2][1] = MotherCanvas.h - 37;
         mPosOther[3][1] = MotherCanvas.h - 67;
         yAutoFire = MotherCanvas.h - 92;
         yQuickChat = MotherCanvas.h - 100;
      } else {
         if (typeTouch == 0) {
            mPosOther[2][1] = MotherCanvas.h - 30;
            mPosOther[3][1] = MotherCanvas.h - 145;
            yAutoFire = MotherCanvas.h - 170;
            yQuickChat = MotherCanvas.h - 94 - 74;
         }

      }
   }

   private MainObject AA(int var1, int var2) {
      MainObject var3 = null;
      CA.removeAllElements();
      int var4 = this.EW;

      for(int var5 = 0; var5 < GameScreen.vecPlayers.size(); ++var5) {
         MainObject var6;
         int var7;
         if ((var6 = (MainObject)GameScreen.vecPlayers.elementAt(var5)) != null && var6 != GameScreen.player && !var6.returnAction() && var6.typeObject != 10 && (var6.Action != 4 || var6.typeObject != 1) && (var7 = MainObject.getDistance(var1, var2, var6.x, var6.y - var6.hOne / 2)) <= this.EW) {
            if (var3 == null) {
               var3 = var6;
               var4 = var7;
            } else if (var3.typeSpecMonSter != 1 && var7 < var4 && var3.typeObject != 2) {
               var3 = var6;
               var4 = var7;
            }

            if (var6.typeObject != 1 && !GameScreen.player.AC(var6) && (var6.typeObject == 0 || var3.typeObject == 2)) {
               if (var6.typeObject == 2) {
                  CA.insertElementAt(var6, 0);
               } else if (CA.size() < 5) {
                  CA.addElement(var6);
               }
            }

            if (!GameScreen.isShowNameSUPER_BOSS && var6.typeSpecMonSter == 1) {
               var3 = var6;
            }
         }
      }

      if (CA.size() > 0) {
         FQ = MotherCanvas.w;
         FR = 0;
         CO = 80;
         if (CA.size() > 1 && GameScreen.player.typePK == -1 && var3 != null && var3.typeSpecMonSter != 1) {
            var3 = null;
         }
      }

      return var3;
   }

   public static void AA(MainItem var0) {
      if (var0 != null) {
         for(int var1 = 0; var1 < vecEffCurrent.size(); ++var1) {
            MainItem var2;
            if ((var2 = (MainItem)vecEffCurrent.elementAt(var1)).typeObject == var0.typeObject && var2.indexHotKey == var0.indexHotKey) {
               vecEffCurrent.removeElement(var2);
               --var1;
            }
         }

         if (var0.typeObject == 4) {
            vecEffCurrent.insertElementAt(var0, 0);
         } else {
            vecEffCurrent.addElement(var0);
         }
      }
   }

   public static void AA(Class_DS var0) {
      vecEventShow.addElement(var0);
   }

   public static void AF(mGraphics var0) {
      if (CB != null && GameScreen.IQ) {
         MainEvent.AA(var0, MotherCanvas.hw, EX - MainEvent.AP + GameScreen.h12plus, CB);
      }

   }

   public static void paintShowHelp(mGraphics var0, boolean var1) {
      if (GameScreen.CU != null) {
         for(int var2 = 0; var2 < GameScreen.CU.size(); ++var2) {
            MainHelp var3;
            if ((var3 = (MainHelp)GameScreen.CU.elementAt(var2)).isInMap == var1) {
               var3.paint(var0);
               if (var3.isBreak) {
                  break;
               }
            }
         }
      }

   }

   public static void AK() {
      if (GameScreen.CU != null) {
         for(int var0 = 0; var0 < GameScreen.CU.size(); ++var0) {
            MainHelp var1;
            (var1 = (MainHelp)GameScreen.CU.elementAt(var0)).update();
            if (var1.isRemove) {
               GameScreen.CU.removeElement(var1);
            } else if (var1.isBreak) {
               break;
            }
         }
      }

   }

   public static void AL() {
      if (CB != null) {
         if (++EY < FA) {
            if (EX < MainEvent.AP + 3 && (EX += EZ) > MainEvent.AP + 3) {
               EX = MainEvent.AP + 3;
            }
         } else if (EX > 0) {
            EX -= EZ;
         } else {
            CB = null;
         }

         if (GameCanvas.isTouch && GameCanvas.AB(MotherCanvas.hw - MainEvent.AO / 2, EX - MainEvent.AP, MainEvent.AO, MainEvent.AP)) {
            GameCanvas.isPointerSelect = false;
            Class_DS var0;
            if ((var0 = Class_DS.AA(CB.AC, CB.AQ)) != null) {
               GameCanvas.eventScr.AU = var0;
            }

            var0.AA();
            CB = null;
         }
      }

      if (CB == null && vecEventShow.size() > 0) {
         CB = (Class_DS)vecEventShow.elementAt(0);
         EX = 0;
         EY = 0;
         vecEventShow.removeElement(0);
      }

   }

   public static void AG(mGraphics var0) {
      int var1 = yInfoServer;
      int var2 = xInfoServer;
      if (LoadMap.specMap == 10) {
         var1 += 66 + GameScreen.h12plus;
      } else if (AS() && BL.typeTime == 4) {
         var1 += 50 + GameScreen.h12plus;
      } else if (CB != null && GameScreen.IQ) {
         var1 += EX + GameScreen.h12plus;
      } else {
         var1 += GameScreen.h12plus;
      }

      if (CB == null) {
         var1 += EM + 25;
      }

      int var10000;
      if (CE != null) {
         GameCanvas.resetTrans(var0);
         AB(var0, var2, var1 + CE.AH, 3);
         var0.AD(var2, var1 + CE.AH, wInfoServer, hInfoServer);
         mGraphics.AC();
         var10000 = CE.AH;
         var10000 = wInfoServer;
         var10000 = hInfoServer;
         mGraphics.AD();
         byte var3 = 0;
         if (CE.AD >= 0) {
            var2 -= 10;
            MainImage var5;
            if ((var5 = Potion.getIconClan(CE.AD)) != null && var5.img != null) {
               if (var5.frame == -1) {
                  var5.set_Frame();
               }

               if (var5.frame <= 1) {
                  var0.drawRegion((mImage)var5.img, var2 + wInfoServer - CE.AG + 9, var1 + CE.AH + hInfoServer / 2 - 1, 3);
               } else {
                  byte var4;
                  if (FE >= var5.frame - 1) {
                     var4 = 15;
                  } else {
                     var4 = 3;
                  }

                  if (CRes.abs(GameCanvas.gameTick - FD) > var4) {
                     if (++FE >= var5.frame) {
                        FE = 0;
                     }

                     FD = GameCanvas.gameTick;
                  }

                  var0.drawRegion(var5.img, 0, FE * var5.AB, var5.AB, var5.AB, 0, var2 + wInfoServer - CE.AG + 9, var1 + CE.AH + hInfoServer / 2 - 1, 3);
               }
            }

            var3 = 18;
         }

         if (GameCanvas.lowGraphic) {
            mFont.tahoma_7b_black.drawString(var0, CE.strShow, var2 + wInfoServer - CE.AG + 1 + var3, var1 + CE.AH + GameCanvas.hText / 4 + 1, 0);
         }

         CE.fontpaint.drawString(var0, CE.strShow, var2 + wInfoServer - CE.AG + var3, var1 + CE.AH + GameCanvas.hText / 4, 0);
         var1 += hInfoServer + CE.AH + 2;
         mGraphics.restoreCanvas();
      }

      if (CC != null) {
         GameCanvas.resetTrans(var0);
         AB(var0, var2, var1 + CC.AH, 5);
         var0.AD(var2, var1 + CC.AH, wInfoServer, hInfoServer);
         mGraphics.AC();
         var10000 = CC.AH;
         var10000 = wInfoServer;
         var10000 = hInfoServer;
         mGraphics.AD();
         if (GameCanvas.lowGraphic) {
            mFont.tahoma_7_black.drawString(var0, CC.strShow, var2 + wInfoServer - CC.AG + 1, var1 + CC.AH + GameCanvas.hText / 4 + 1, 0);
         }

         CC.fontpaint.drawString(var0, CC.strShow, var2 + wInfoServer - CC.AG, var1 + CC.AH + GameCanvas.hText / 4, 0);
         var1 += hInfoServer + CC.AH + 2;
         mGraphics.restoreCanvas();
      }

      if (BN && CD != null) {
         GameCanvas.resetTrans(var0);
         AB(var0, var2, var1 + CD.AH, 0);
         var0.AD(var2, var1 + CD.AH, wInfoServer, hInfoServer);
         mGraphics.AC();
         var10000 = CD.AH;
         var10000 = wInfoServer;
         var10000 = hInfoServer;
         mGraphics.AD();
         if (GameCanvas.lowGraphic) {
            mFont.tahoma_7_black.drawString(var0, CD.strShow, var2 + wInfoServer - CD.AG + 1, var1 + CD.AH + GameCanvas.hText / 4 + 1, 0);
         }

         CD.fontpaint.drawString(var0, CD.strShow, var2 + wInfoServer - CD.AG, var1 + CD.AH + GameCanvas.hText / 4, 0);
         var1 += hInfoServer + CD.AH + 2;
         mGraphics.restoreCanvas();
      }

      if (infoPlayer != null && !infoPlayer.AB) {
         int var6 = MotherCanvas.hw;
         if (isSmallInfoServer) {
            var6 = MotherCanvas.w - wShowInfoPlayer / 2 - 4;
         }

         GameCanvas.resetTrans(var0);
         AA(var0, var6, var1 + infoPlayer.AH, wShowInfoPlayer);
         var0.AD(var6 - wShowInfoPlayer / 2, var1 + infoPlayer.AH, wShowInfoPlayer, hInfoServer);
         mGraphics.AC();
         var10000 = wShowInfoPlayer;
         var10000 = infoPlayer.AH;
         var10000 = wShowInfoPlayer;
         var10000 = hInfoServer;
         mGraphics.AD();
         if (GameCanvas.lowGraphic) {
            AvMain.AB(var0, infoPlayer.strShow, var6 + 1, var1 + infoPlayer.AH + GameCanvas.hText / 4 + yEffInfoPlayer, 2, (int)0);
         } else {
            infoPlayer.fontpaint.drawString(var0, infoPlayer.strShow, var6, var1 + infoPlayer.AH + GameCanvas.hText / 4 + yEffInfoPlayer, 2);
         }

         mGraphics.restoreCanvas();
      }

      GameCanvas.resetTrans(var0);
   }

   private static void AB(mGraphics var0, int var1, int var2, int var3) {
      if (wInfoServer > 140) {
         var0.drawRegion((mImage)imgInfoServer, var1, var2, 0);
         var0.drawRegion(imgInfoServer, 0, 0, wInfoServer - 140, 20, 0, var1 + 140, var2, 0);
      } else {
         var0.drawRegion(imgInfoServer, 0, 0, wInfoServer, 20, 0, var1, var2, 0);
      }

      if (var3 == 1) {
         var0.drawRegion((mImage)imgBorderNoti, var1 - 4, var2 + hInfoServer / 2, 3);
      } else if (var3 == 2) {
         var0.drawRegion((mImage)imgBorderNoti2, var1 - 4, var2 + hInfoServer / 2, 3);
      } else if (var3 == 3) {
         if (GameCanvas.language == 0 && !GameCanvas.AQ()) {
            if (GameCanvas.IndexServer == 3) {
               fraBorderNoti.drawFrame(6, var1 - 4, var2 + hInfoServer / 2, 0, 3, var0);
            } else {
               fraBorderNoti.drawFrame(GameCanvas.IndexServer + 1, var1 - 4, var2 + hInfoServer / 2, 0, 3, var0);
            }
         } else {
            fraBorderNoti.drawFrame(0, var1 - 4, var2 + hInfoServer / 2, 0, 3, var0);
         }
      } else {
         if (var3 == 4 || var3 == 5) {
            fraBorderNoti.drawFrame(var3, var1 - 4, var2 + hInfoServer / 2, 0, 3, var0);
         }
      }
   }

   public static void AA(mGraphics var0, int var1, int var2, int var3) {
      if (var3 > 140 && var3 <= 280) {
         var0.drawRegion((mImage)imgInfoServer, var1 - var3 / 2, var2, 0);
         var0.drawRegion(imgInfoServer, 0, 0, var3 - 140, 20, 0, var1 - var3 / 2 + 140, var2, 0);
      } else {
         var0.drawRegion(imgInfoServer, 0, 0, var3, 20, 0, var1 - var3 / 2, var2, 0);
      }
   }

   public static void AM() {
      int var0;
      InfoShowNotify var1;
      if (CE != null) {
         CE.AA();
         if (CE.AB) {
            CE = null;
         }
      } else {
         for(var0 = 0; var0 < vecInfoServer.size(); ++var0) {
            if ((var1 = (InfoShowNotify)vecInfoServer.elementAt(var0)).AI == 1 || var1.AI == 2) {
               (CE = new InfoShowNotify(var1.strShow, var1.AI)).setValue(var1.fontpaint);
               CE.AD = var1.AD;
               String var2 = "";
               if (CE.AD >= 0) {
                  var2 = T.Clan + " ";
               }

               GameCanvas.chatTabScr.AB(T.CR, "", var2 + var1.strShow, (byte)1, false);
               vecInfoServer.removeElement(var1);
               break;
            }
         }
      }

      CD = AA((InfoShowNotify)CD, (byte)0);
      CC = AA((InfoShowNotify)CC, (byte)5);
      if (infoPlayer != null && !infoPlayer.AB) {
         infoPlayer.AA();
         if (yEffInfoPlayer > 0) {
            yEffInfoPlayer -= 2;
         }
      }

      for(var0 = 0; var0 < vecQuickChatLoL.size(); ++var0) {
         ++(var1 = (InfoShowNotify)vecQuickChatLoL.elementAt(var0)).AF;
         if (var1.AF >= 200) {
            vecQuickChatLoL.removeElement(var0);
            --var0;
         }
      }

   }

   private static InfoShowNotify AA(InfoShowNotify var0, byte var1) {
      if (var0 != null) {
         var0.AC = BN;
         var0.AA();
         if (var0.AB) {
            var0 = null;
         }
      } else {
         for(int var2 = 0; var2 < vecInfoServer.size(); ++var2) {
            InfoShowNotify var3;
            if ((var3 = (InfoShowNotify)vecInfoServer.elementAt(var2)).AI == var1) {
               (var0 = new InfoShowNotify(var3.strShow, var3.AI)).setValue(var3.fontpaint);
               GameCanvas.chatTabScr.AB(T.CR, "", var3.strShow, (byte)1, false);
               vecInfoServer.removeElement(var3);
               break;
            }
         }
      }

      return var0;
   }

   public static void AA(InfoShowNotify var0) {
      if (var0.AI == 2) {
         vecInfoServer.insertElementAt(var0, 0);
      } else {
         vecInfoServer.addElement(var0);
      }
   }

   public static void addInfoPlayerNormal(String var0, mFont var1) {
      infoPlayer.strShow = var0;
      infoPlayer.setValue(var1);
      yEffInfoPlayer = 20;
      wShowInfoPlayer = infoPlayer.fontpaint.getWidth(infoPlayer.strShow) + 4;
   }

   public static void paintNumMess(mGraphics g, int x, int y) {
      byte num = 0;
      if (timePointer > 0 && keyPoint == 106) {
         num = 1;
      }
      x += xNumMess;
      y += yNumMess;
      if (GameCanvas.isTouch) {
         g.drawRegion(AvMain.imgMess, 0, num << 4, 22, 16, 0, x, y + numMess.yNum, 0);
      } else {
         g.drawRegion((mImage)AvMain.imgMess, x, y + numMess.yNum, 0);
      }
      if (!GameCanvas.isTouch && GameCanvas.currentScreen == GameCanvas.gameScr) {
         g.drawRegion((mImage)AvMain.imgChat, xAutoFire, yAutoFire + GameScreen.h12plus, 0);
      }

      if (GameScreen.numMess > 0) {
         byte num4 = -3;
         if (GameCanvas.isTouch) {
            num4 = 0;
         }

         String text = "" + GameScreen.numMess;
         if (GameScreen.numMess > 9) {
            text = "9+";
         }

         mFont.tahoma_7_black.drawString(g, text, x + num4, y + numMess.yNum + 2, 2);
      }

      if (GameCanvas.isTouch && GameCanvas.currentScreen == GameCanvas.gameScr) {
         num = 2;
         if (!isAutoFireInterface) {
            num = 0;
         } else if (GameCanvas.gameTick % 12 < 7) {
            num = 1;
         }

         AvMain.fraAutoFire.drawFrame(num, xAutoFire, yAutoFire, 0, 0, g);
         if (GameCanvas.isTouch && GameCanvas.currentScreen == GameCanvas.gameScr) {
            num = 2;
            if (!isAutoFireInterface) {
                num = 0;
            } else if (GameCanvas.gameTick % 12 < 7) {
                num = 1;
            }
            AvMain.fraAutoFire.drawFrame(num, xAutoFire, yAutoFire, 0, 0, g);
            int idxMenu = (GameCanvas.gameTick / 6) % 3;
            AvMain.fraMenuAuto.drawFrame(idxMenu, xAutoFire+3, yAutoFire - 20, 0, 0, g);
        }
      }

   }

   public static void AN() {
      if (GameScreen.numMess > 0) {
         numMess.AA();
      } else {
         numMess.yNum = 0;
      }

      if (Clan_Screen.AT) {
         numClan.AA();
      }

   }

   public static void AO() {
      if (ED < 10) {
         ++ED;
      }

   }

   public static void paintMoveTo(mGraphics var0) {
      if (ED < 10 && AvMain.fraMoveTo != null) {
         AvMain.fraMoveTo.drawFrame(ED / 2, EB, EC, 0, 3, var0);
      }

   }

   public final void AI(mGraphics var1) {
      if (CL >= 0) {
         var1.setColor(-3426402);
         var1.fillRect(MotherCanvas.w - 60 - 2, 77, 58, 12);
         AA(var1, (byte)1, CK, CL, MotherCanvas.w - 60 - 1, 78, 0, 10, 56, 0, false, 0, false, 0);
      }

      int var4;
      int var5;
      int var6;
      if (LoadMap.specMap != 10 && AS() && !GameCanvas.menuCur.isShowMenu && (!GameCanvas.isTouch || this.xShow >= 75 || vecClanDam == null || vecClanDam.size() <= 0) && !GameScreen.isPvPNew) {
         if (BL.typeTime != 4 && GameCanvas.loadmap.idMapLoadMap != 995) {
            var4 = FO;
            var5 = 42;
            var6 = MotherCanvas.w - var4 / 2 - 3;
            if (!GameCanvas.isTouch && vecClanDam != null && vecClanDam.size() > 0) {
               var5 = 80;
               var6 = 35;
            }

            if (BL.typeTime == 2) {
               var6 = MotherCanvas.w - 27;
               var5 = 94 + GameScreen.h12plus;
               if (GameCanvas.loadmap.idMapLoadMap >= 167 && GameCanvas.loadmap.idMapLoadMap <= 175) {
                  var5 = 42 + GameScreen.h12plus;
               }

               AvMain.AC(var1, var6 - 27 - 2, var5 + 3, 54, 27);
            } else {
               AvMain.paintRect(var1, var6 - var4 / 2 - 1, var5, var4, 32, (byte)1, 4);
            }

            mFont.tahoma_7b_white.drawString(var1, BL.strInfo, var6, var5 + FP / 5, 2);
            BL.paintCountDownTicket(var1, mFont.tahoma_7b_white, var6, var5 + FP / 2 + FP / 5 + 4, 2);
         } else {
            mImage var3;
            if (indexPaintTable == 1) {
               var3 = null;
               var4 = MotherCanvas.w / 2 + 8;
               if (GameScreen.CN == 1) {
                  var4 = MotherCanvas.w / 2;
               }

               var5 = 2 + GameScreen.h12plus;
               if (MotherCanvas.w < 280) {
                  if (AvMain.imgTimePvpSmall == null) {
                     AvMain.imgTimePvpSmall = mImage.createImage("/interface/timepvpsmall2.png");
                  } else {
                     var3 = AvMain.imgTimePvpSmall;
                     var1.drawRegion((mImage)var3, var4, 29, 3);
                     AvMain.FontBorderColor(var1, String.valueOf(BL.valueLeft), var4 - 21, 4 + FP / 5, 2, (int)0, (int)7);
                     AvMain.FontBorderColor(var1, String.valueOf(BL.valueright), var4 + 21, 4 + FP / 5, 2, (int)0, (int)7);
                     BL.paintCountDownTicket(var1, mFont.tahoma_7_white, var4, 4 + FP / 2 + FP / 5 + 4 + 8, 2);
                  }
               } else if (AvMain.imgTimePvp == null) {
                  AvMain.imgTimePvp = mImage.createImage("/interface/timepvp2.png");
               } else {
                  var3 = AvMain.imgTimePvp;
                  var1.drawRegion((mImage)var3, var4, var5 + 25, 3);
                  AvMain.FontBorderColor(var1, String.valueOf(BL.valueLeft), var4 - 34, var5 + 5 + FP / 5, 2, (int)0, (int)7);
                  AvMain.FontBorderColor(var1, String.valueOf(BL.valueright), var4 + 34, var5 + FP / 5 + 5, 2, (int)0, (int)7);
                  BL.paintCountDownTicket(var1, mFont.tahoma_7_white, var4, var5 + FP / 2 + FP / 5 + 4 + 4, 2);
               }
            } else {
               var3 = null;
               var4 = MotherCanvas.w / 2 + 8;
               if (GameScreen.CN == 1) {
                  var4 = MotherCanvas.w / 2;
               }

               var5 = 2 + GameScreen.h12plus;
               if (MotherCanvas.w < 280) {
                  if (AvMain.imgTimePvpSmall == null) {
                     AvMain.imgTimePvpSmall = mImage.createImage("/interface/timepvpsmall.png");
                  } else {
                     var3 = AvMain.imgTimePvpSmall;
                     var1.drawRegion((mImage)var3, var4, 29, 3);
                     AvMain.FontBorderColor(var1, String.valueOf(BL.valueLeft), var4 - 21, 4 + FP / 5, 2, (int)6, (int)7);
                     AvMain.FontBorderColor(var1, String.valueOf(BL.valueright), var4 + 21, 4 + FP / 5, 2, (int)1, (int)7);
                     BL.paintCountDownTicket(var1, mFont.tahoma_7_white, var4, 4 + FP / 2 + FP / 5 + 4 + 8, 2);
                  }
               } else if (AvMain.imgTimePvp == null) {
                  AvMain.imgTimePvp = mImage.createImage("/interface/timepvp.png");
               } else {
                  var3 = AvMain.imgTimePvp;
                  var1.drawRegion((mImage)var3, var4, var5 + 25, 3);
                  AvMain.FontBorderColor(var1, String.valueOf(BL.valueLeft), var4 - 34, var5 + 5 + FP / 5, 2, (int)6, (int)7);
                  AvMain.FontBorderColor(var1, String.valueOf(BL.valueright), var4 + 34, var5 + FP / 5 + 5, 2, (int)1, (int)7);
                  BL.paintCountDownTicket(var1, mFont.tahoma_7_white, var4, var5 + FP / 2 + FP / 5 + 4 + 4, 2);
               }
            }
         }
      }

      AR(var1);
      if (BM.timeCountDown > 0) {
         var5 = 124 + GameScreen.h12plus;
         var6 = MotherCanvas.w - 27 - 2;
         if (LoadMap.specMap == 10) {
            var5 = DP + 28;
            var6 = DO;
         }

         AvMain.AC(var1, var6 - 27, var5 + 3, 54, 27);
         mFont.tahoma_7b_white.drawString(var1, BM.strInfo, var6, var5 + FP / 5, 2);
         BM.paintCountDownTicket(var1, mFont.tahoma_7b_white, var6, var5 + FP / 2 + FP / 5 + 4, 2);
      }

      if (FM > 0 && GameScreen.player != null && GameScreen.player.Lv > 1) {
         AvMain.AB(var1, MotherCanvas.hw - CJ / 2, FM + GameScreen.h12plus - 40, CJ, 36);
         AvMain.AA(var1, nameMap, MotherCanvas.hw, FM + GameScreen.h12plus - 40 + GameCanvas.hText / 2, 2, (byte)0);
         mFont.tahoma_7_white.drawString(var1, "- " + T.EU + " " + LoadMap.AA(LoadMap.AX) + " -", MotherCanvas.hw, FM + GameScreen.h12plus - 40 + GameCanvas.hText * 3 / 2, 2);
      }

      if (DY >= 0) {
         boolean var8 = true;
         var4 = MotherCanvas.hw;
         switch(DY) {
         case 0:
         case 2:
         case 3:
         case 4:
            if (mImgPvPType == null) {
               AT();
            } else {
               var1.drawRegion((mImage)mImgPvPType[DY], var4 + DM, 80 + DN, 3);
            }
            break;
         case 1:
            if (mImgPvPType == null) {
               AT();
            } else {
               byte var7;
               if (DZ >= 75) {
                  var7 = 5;
               } else if (DZ >= 60) {
                  var7 = 4;
               } else if (DZ >= 50) {
                  var7 = 3;
               } else if (DZ >= 35) {
                  var7 = 2;
               } else if (DZ >= 25) {
                  var7 = 1;
               } else {
                  var7 = 0;
               }

               var1.drawRegion(mImgPvPType[1], 0, var7 * 20, 20, 20, 0, var4 + DM, 80 + DN, 3);
            }
         }
      }

      if (LoadMap.specMap == 10) {
         this.loadImageLOL(var1);
      }

      if (GameScreen.isShowNameWW) {
         if (CT == null) {
            CT = mImage.createImage("/interface/wwborder.png");
            return;
         }

         var1.drawRegion((mImage)CT, MotherCanvas.w / 2, 15 + GameScreen.h12plus, 3);
         mFont.tahoma_7_white.drawString(var1, "" + GameScreen.player.IX, MotherCanvas.w / 2, 3 + GameScreen.h12plus, 2);
         mFont.tahoma_7b_green.drawString(var1, "" + GameScreen.player.IY, MotherCanvas.w / 2 - 12, 15 + GameScreen.h12plus, 2);
         mFont.tahoma_7b_red.drawString(var1, "" + GameScreen.player.IZ, MotherCanvas.w / 2 + 12, 15 + GameScreen.h12plus, 2);
      }

   }

   private static void AR(mGraphics var0) {
      for(int var1 = 0; var1 < vecQuickChatLoL.size(); ++var1) {
         InfoShowNotify var10000 = (InfoShowNotify)vecQuickChatLoL.elementAt(var1);
         int var10002 = MotherCanvas.w - 4;
         int var5 = yQuickChat - GameCanvas.hText - var1 * GameCanvas.hText;
         int var4 = var10002;
         InfoShowNotify var2 = var10000;
         if (var10000.AI == 3) {
            AvMain.AB(var0, var2.strShow, var4, var5, 1, (int)0);
         } else if (var2.AI == 4) {
            AvMain.AB(var0, var2.strShow, var4, var5, 1, (int)5);
         }
      }

   }

   private static boolean AS() {
      if (BL != null && GameCanvas.loadmap != null) {
         return BL.timeCountDown > 0 || GameCanvas.loadmap.idMapLoadMap == 995 || GameCanvas.loadmap.idMapLoadMap == 997 || GameCanvas.loadmap.idMapLoadMap == 997 || GameCanvas.loadmap.idMapLoadMap >= 988 && GameCanvas.loadmap.idMapLoadMap >= 995;
      } else {
         return false;
      }
   }

   private void loadImageLOL(mGraphics var1) {
      int var2;
      if (EG == null) {
         EG = new FrameImage[6];

         for(var2 = 0; var2 < EG.length; ++var2) {
            EG[var2] = new FrameImage(mImage.createImage("/interface/lol" + var2 + ".png"), 2);
         }

         if (!GameCanvas.lowGraphic) {
            CR = mImage.createImage("/interface/lol10.png");
         }

         CS = mImage.createImage("/interface/lol12.png");
      }

      if (!GameCanvas.lowGraphic) {
         var1.drawRegion((mImage)CR, DO, DP, 3);
      }

      BL.paintCountDownTicket(var1, mFont.tahoma_7_white, DO, DP - 30, 2);
      AvMain.AB(var1, "" + AT, DO - 37, DP - 29, 2, (int)6);
      AvMain.AB(var1, "" + AU, DO - 23, DP - 28, 2, (int)6);
      AvMain.AB(var1, "" + AV, DO + 38, DP - 29, 2, (int)1);
      AvMain.AB(var1, "" + AW, DO + 24, DP - 28, 2, (int)1);
      if (BF == 1) {
         var1.drawRegion((mImage)CS, DO - 60, DP - 8, 3);
      }

      for(var2 = 0; var2 < AS.length; ++var2) {
         EG[AS[var2][0]].drawFrame(AS[var2][1], DO - 35 + var2 / 3 * 35, DP + 3 - 12 + 16 * (var2 % 3), 0, 3, var1);
      }

   }

   public static void AP() {
      if (BL.timeCountDown > 0) {
         BL.updateTimeCountDownTicket();
      }

      if (BM.timeCountDown > 0) {
         BM.updateTimeCountDownTicket();
      }

      if (CI >= 0) {
         if (++CI <= 80) {
            FM = MotherCanvas.h / 5 + 14;
            if (GameCanvas.isSmallScreen) {
               FM = 50;
            }
         } else {
            FN -= 2;
            if ((FM += FN) <= 0) {
               CI = -1;
            }
         }

         if (CI == 80) {
            FN = 8;
         }
      }

      if (DY >= 0) {
         ++FX;
         if (DY == 0) {
            if (FX < 10) {
               if (DM < 0 && (DM += 50) > 0) {
                  DM = 0;
               }
            } else if (FX > 20 && DM < MotherCanvas.w) {
               DM += 50;
            }
         } else if (DY == 2) {
            if (FX < 10) {
               if (DM > 0 && (DM -= 80) < 0) {
                  DM = 0;
               }
            } else if (FX > 20 && DM > -MotherCanvas.w) {
               DM -= 50;
            }
         } else if (DY == 3) {
            if (FX < 10 && DN > 0 && (DN -= 80) < 0) {
               DN = 0;
            }
         } else if (DY == 4 && FX < 10 && DN < 0 && (DN += 10) > 0) {
            DN = 0;
         }

         if (--DZ <= 0) {
            DY = -1;
         }
      }

   }

   public static void paintShowNear(mGraphics var0) {
      if (CO > -5) {
         int var1 = GameCanvas.hText + 8;

         for(int var2 = 0; var2 < CA.size(); ++var2) {
            MainObject var3;
            String var4 = (var3 = (MainObject)CA.elementAt(var2)).name;
            if (var3.BQ() == 99) {
               var4 = T.EU;
            }

            if (var4.length() > 11) {
               var4 = var3.name.substring(0, 10);
            }

            byte var5 = 1;
            if (var3.typeObject == 2) {
               var5 = 3;
            }

            int var6 = FQ;
            AvMain.paintRect(var0, var6, 60 + var1 * var2, 68, var1 - 4, var5, 4);
            if (var3.BQ() == 1 && var3.typePlayer != 2 && var3.typePlayer != 3) {
               mFont.tahoma_7_white.drawString(var0, var4, var6 + 21, 60 + GameCanvas.hText / 4 + var1 * var2, 0);
               var3.AB(var0, var6 + 10, 60 + GameCanvas.hText / 2 + var1 * var2, 2);
            } else {
               mFont.tahoma_7_white.drawString(var0, var4, var6 + 35, 60 + GameCanvas.hText / 4 + var1 * var2, 2);
            }
         }
      }
   }

   public final void paintShowtime(mGraphics g) {
      if (vecClanDam != null && vecClanDam.size() != 0) {
         if (!GameCanvas.menuCur.isShowMenu) {
            byte num = 0;
            if (this.timepointer > 0 && this.isFocusTime) {
               num = 1;
            }

            boolean flag = true;
            if (this.xShow < 75) {
               flag = false;
            }

            if (flag) {
               g.drawRegion(imgOther[4], 0, num * 40, 16, 40, 2, MotherCanvas.w - 14, MotherCanvas.h / 2 - 35, 0);
            }

         }
      }
   }

   private void paintInfoClanDam(mGraphics g) {
      if (vecClanDam != null && vecClanDam.size() != 0) {
         int num = GameCanvas.hText;

         for(int var3 = 0; var3 < vecClanDam.size(); ++var3) {
            MainClan mainClan;
            String text;
            if ((text = (mainClan = (MainClan)vecClanDam.elementAt(var3)).name).length() > 11) {
               text = mainClan.name.substring(0, 10);
            }
            int num2 = MotherCanvas.w - 70 + this.xShow;
            AvMain.paintRect(g, num2, 60 + (num << 1) * var3, 68, (num << 1) - 4, (byte)1, 4);
            mFont.tahoma_7_white.drawString(g, text, num2 + 21, 60 + GameCanvas.hText / 4 + (num << 1) * var3 - 2, 0);
            MainImage iconClan;
            if ((iconClan = Potion.getIconClan(mainClan.idIcon)) != null && iconClan.img != null) {
               if (iconClan.frame == -1) {
                  iconClan.set_Frame();
               }

               if (iconClan.frame <= 1) {
                  g.drawRegion((mImage)iconClan.img, num2 + 10, 60 + GameCanvas.hText / 2 + (num << 1) * var3, 3);
               } else {
                  byte num3;
                  if (this.framepaint >= iconClan.frame - 1) {
                     num3 = 15;
                  } else {
                     num3 = 3;
                  }

                  if (CRes.abs(GameCanvas.gameTick - this.lastTick) > num3) {
                     ++this.framepaint;
                     if (this.framepaint >= iconClan.frame) {
                        this.framepaint = 0;
                     }
                     this.lastTick = GameCanvas.gameTick;
                  }

                  g.drawRegion(iconClan.img, 0, this.framepaint * iconClan.AB, iconClan.AB, iconClan.AB, 0, num2 + 10, 60 + GameCanvas.hText / 2 + (num << 1) * var3, 3);
               }
            }

            AvMain.FontBorderColor(g, "" + mainClan.AG, num2 + 34, 60 + (num << 1) * var3 + num, 2, (int)6, (int)7);
         }

      }
   }

   public static void AQ() {
      if (--CO > 0) {
         if (FQ > MotherCanvas.w - 70) {
            FR += 10;
            if ((FQ -= FR) < MotherCanvas.w - 70) {
               FQ = MotherCanvas.w - 70;
               FR = 0;
               return;
            }
         }
      } else if (FQ < MotherCanvas.w) {
         FR += 10;
         FQ += FR;
      }

   }

   public static void setTypePvP(byte var0) {
      DY = var0;
      DN = 0;
      DM = 0;
      FX = 0;
      switch(DY) {
      case 0:
         DZ = 25;
         DM = -MotherCanvas.w / 2;
         return;
      case 1:
         DZ = 75;
      default:
         return;
      case 2:
         DZ = 25;
         DM = MotherCanvas.w / 2;
         return;
      case 3:
         DZ = 60;
         DN = MotherCanvas.h - 80;
         return;
      case 4:
         DN = -80;
         DZ = 60;
      }
   }

   private static void AT() {
      mImgPvPType = new mImage[5];

      for(int var0 = 0; var0 < mImgPvPType.length; ++var0) {
         if (var0 != 1 && GameCanvas.language == 1) {
            mImgPvPType[var0] = mImage.createImage("/interface/pvp" + var0 + "_e.png");
         } else {
            mImgPvPType[var0] = mImage.createImage("/interface/pvp" + var0 + ".png");
         }
      }

   }

   public static void paintAutoRepeatQuest(mGraphics var0) {
      if (GameScreen.isOnRepeatQuest) {
         mFont var1 = mFont.tahoma_7b_white;
         String var2 = T.XK;
         int var3 = var1.getHeight();
         int var4 = var1.getWidth(var2) + 10;
         var0.drawRegion(imgInfoServer, 0, 0, var4, 20, 0, 0, 72, 0);
         var1.drawString(var0, T.XK, 5, 82 - var3 / 2, 0);
      }

   }

   public static void paintWaitingForMonster(mGraphics var0) {
      if (GameScreen.isOnRepeatQuest && AutoRepeatQuest.AB) {
         mFont var1 = mFont.tahoma_7b_white;
         String var2 = T.XK;
         int var3 = var1.getHeight();
         int var4 = var1.getWidth(var2) + 10;
         var0.drawRegion(imgInfoServer, 0, 0, var4, 20, 0, 0, 96, 0);
         if (++GameScreen.KH < 20) {
            var1.drawString(var0, T.XR, 5, 106 - var3 / 2, 0);
            return;
         }

         if (GameScreen.KH < 40) {
            var1.drawString(var0, T.XR + ".", 5, 106 - var3 / 2, 0);
            return;
         }

         if (GameScreen.KH < 60) {
            var1.drawString(var0, T.XR + "..", 5, 106 - var3 / 2, 0);
            return;
         }

         if (GameScreen.KH < 80) {
            var1.drawString(var0, T.XR + "...", 5, 106 - var3 / 2, 0);
            return;
         }

         GameScreen.KH = 0;
         var1.drawString(var0, T.XR, 5, 106 - var3 / 2, 0);
      }

   }

   public static void paintAutoSuperBoss(mGraphics var0) {
      if (GameScreen.isOnSuperBoss) {
         mFont var1 = mFont.tahoma_7b_white;
         String var2 = T.XV;
         int var3 = var1.getHeight();
         int var4 = var1.getWidth(var2) + 10;
         var0.drawRegion(imgInfoServer, 0, 0, var4, 20, 0, 0, 72, 0);
         var1.drawString(var0, T.XV, 5, 82 - var3 / 2, 0);
      }

   }
}
