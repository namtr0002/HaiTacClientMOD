public class AvMain {
   public iCommand DA;
   public iCommand DB;
   public iCommand center;
   public iCommand backCMD;
   public iCommand DE;
   public iCommand DF;
   public static int DG = 130;
   public static int DH = 154;
   public static mImage imgSelect;
   public static mImage imgHotKey;
   public static mImage imgIconDel;
   public static mImage imgDieChar;
   public static mImage imgDelay;
   public static mImage imgLg;
   public static mImage imgEffCur;
   public static mImage imgMess;
   public static mImage imgEye;
   public static mImage imgXp;
   public static mImage DS;
   public static mImage DT;
   public static mImage imgcheck;
   public static mImage DV;
   public static mImage imgShadowSmall;
   public static mImage imgChat;
   public static mImage imgCombo;
   public static mImage imgBorderCombo;
   public static mImage EA;
   public static mImage imgPvpVs;
   public static mImage imgPvpOk;
   public static mImage imgPvpObjdef;
   public static mImage imgPlus12_2;
   public static mImage imgBgnum;
   public static mImage imgNenfocus;
   public static mImage imgBgnum2;
   public static mImage imgDaKham;
   public static mImage EJ;
   public static mImage imgTabClan;
   public static mImage imgChatClan;
   public static mImage imgDonateClan;
   public static mImage imgLvClan;
   public static mImage imgBannerClan;
   public static mImage imgPlusClan;
   public static mImage imgTimePvp;
   public static mImage imgTimePvpSmall;
   public static mImage imgBorderIcon;
   public static mImage imgReward;
   public static mImage EU;
   public static mImage EV;
   public static mImage imgLock;
   public static mImage imgHinhnhan;
   public static mImage imgLvDevilSkill;
   public static mImage imgBeri;
   public static mImage imgArrowListServer;
   public static mImage imgInfoLock;
   public static mImage imgInfoClass;
   public static mImage imgInfoStar;
   public static mImage imgBannerRuong;
   public static mImage[] imgPaper;
   public static mImage[] imgButton;
   public static mImage[] mimgBgA;
   public static mImage[] imgPaperDoc;
   public static mImage[] mimgWanted;
   public static mImage[] FK;
   public static mImage[] FL;
   public static mImage[] mImgRoomW;
   public static mImage[] FN;
   public static mImage[] mImgThanhTich;
   private static mImage[] AA;
   public static mImage[] FP;
   public static mImage[] mimgWanted2;
   public static FrameImage fraPk;
   public static FrameImage fraPk2;
   public static FrameImage fraPirate;
   public static FrameImage fraQuest;
   public static FrameImage fratf;
   public static FrameImage fratf1;
   public static FrameImage fraMoveTo;
   public static FrameImage FY;
   public static FrameImage fraCheck;
   public static FrameImage imgLoadImage;
   public static FrameImage fraIconfocus;
   public static FrameImage fraStatusOnline;
   public static FrameImage fraButtonTiemNang;
   public static FrameImage fraTwoTab;
   public static FrameImage fraMoney;
   public static FrameImage GG;
   public static FrameImage fraImgEffOnMap0;
   public static FrameImage fraDelay;
   public static FrameImage fraDelay2;
   public static FrameImage fraDiePlayer;
   public static FrameImage fraComboSkill;
   public static FrameImage fraEquip;
   public static FrameImage fraIconNpc;
   public static FrameImage fraShadowFocus;
   public static FrameImage fraEffBoss;
   public static FrameImage fraIconClan;
   public static FrameImage fraBorderClan;
   public static FrameImage fraBorderClan2;
   public static FrameImage fraAutoFire;
   public static FrameImage GU;
   public static FrameImage fraEffItem;
   public static FrameImage fraEffItem2;
   public static FrameImage GX;
   public static FrameImage fraEventMoon;
   public static FrameImage GZ;
   public static FrameImage fraBorderSkill;
   public static FrameImage fraThongThao;
   public static FrameImage fraNenThongThao;
   public static FrameImage fraBanhLai;
   public static FrameImage fraBorderWanted;
   public static FrameImage fraIconWanted;
   public static FrameImage fraIconMenu;
   public static FrameImage fraIconServer;
   public static FrameImage fraUniform;
   public static FrameImage fraBtLogin;
   public static FrameImage fraBtBanhlai;
   public static FrameImage fraNew;
   public static FrameImage fraIconHome;
   public static FrameImage fraEffOpen;
   public static FrameImage fraEffDasieucap;
   public static FrameImage fraPlus;
   public static FrameImage fraCmdNhanNapThe;
   public static FrameImage fraNauBanh;
   public static FrameImage fraTrongCay;
   public static FrameImage fraMenuAuto;
   public static mImage[] imgDuoi;
   public static mImage[] imgGoc;
   public static mImage[] imgGiua;
   public static mImage[] imgPhai;
   public static mImage[] imgTrai;
   public static mImage[] imgTren;
   public static mImage[] imgKhung;
   public static mImage[] imgDialogTrangTri;
   public static mImage imgTrangTri;
   public static mImage imgDemoWanted;
   public static mImage imgComplete;
   public static mImage imgKhungItem;
   public static mImage imgKhungMem;
   public static int[][] IG = new int[][]{{-479136, -2586584}, {-2582464, -4167672}, {-6058120, -8690870}, {-5271191, -9811158}, {-7967666, -9811158}, {-805042, -1933560}, {-10794443, -12045539}};
   public static int[][] IH = new int[][]{{-10598349, -12306904, -7900837, -9346480, -330796}, {-7312567, -10203849, -3887989, -5466503, -330796}, {-6458293, -9744329, -3032423, -4545404, -330796}};
   private static int[] AB = new int[]{-2701394, -6517641, -9411756, -7832985, -11844807, -3625596};
   public static int[] II = new int[]{-12052464, -477112, -6778760, -2043720, -2573687};
   public static byte IJ = 0;
   public static byte IK = 1;
   private static int[] AC = new int[]{6, 3, 5, 1, 4, 0, 2};

   public void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      if (GameCanvas.currentDialog == null && GameCanvas.subDialog == null && !GameCanvas.menuCur.isShowMenu) {
         this.AD(var1);
      }

   }

   public void AC(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      if (GameCanvas.currentDialog == null && GameCanvas.subDialog == null && !GameCanvas.menuCur.isShowMenu) {
         this.AD(var1);
      }

   }

   public final void AD(mGraphics var1) {
      if (this.DA != null) {
         if (this.DA.xCmd > 0 && this.DA.yCmd > 0) {
            this.DA.paint(var1, this.DA.xCmd, this.DA.yCmd);
         } else {
            this.DA.paint(var1, GameCanvas.BG - 3, MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2);
         }
      }

      if (this.DB != null) {
         this.DB.paint(var1, MotherCanvas.w - GameCanvas.BG + 3, MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2);
      }

      if (this.center != null) {
         this.center.paint(var1, MotherCanvas.hw, MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2);
      }

   }

   public void update() {
   }

   public void AC(int var1, int var2) {
   }

   public void commandPointer(int var1, int var2) {
   }

   public void handleKeyPress() {
      if (GameCanvas.AL[5]) {
         if (this.center != null) {
            GameCanvas.AA(5);
            GameCanvas.AB(5);
            this.center.AD();
            return;
         }
      } else if (GameCanvas.AL[12]) {
         if (this.DA != null) {
            GameCanvas.AA(12);
            GameCanvas.AB(12);
            this.DA.AD();
            return;
         }
      } else if (GameCanvas.AL[13] && this.DB != null) {
         GameCanvas.AA(13);
         GameCanvas.AB(13);
         this.DB.AD();
      }

   }

   public final void AS() {
      if (GameCanvas.AG(5)) {
         if (this.DF != null) {
            GameCanvas.AA(5);
            GameCanvas.AB(5);
            this.DF.AD();
            return;
         }
      } else if (!GameCanvas.AG(40) && (!GameCanvas.isTouch || !GameCanvas.AG(12))) {
         if ((GameCanvas.AG(41) || GameCanvas.isTouch && GameCanvas.AG(13)) && this.backCMD != null) {
            GameCanvas.AA(41);
            GameCanvas.AB(41);
            this.backCMD.AD();
         }
      } else if (this.DE != null) {
         GameCanvas.AA(40);
         GameCanvas.AB(40);
         this.DE.AD();
         return;
      }

   }

   public void updatePointer() {
      if (GameCanvas.isTouch) {
         if (this.DA != null) {
            if (this.DA.AF()) {
               this.DA.AE();
            } else if (GameCanvas.AB(0, MotherCanvas.h - GameCanvas.hCommand - 5, GameCanvas.BG << 1, GameCanvas.hCommand + 10)) {
               this.DA.AD();
            }
         }

         if (this.DB != null) {
            if (this.DB.AF()) {
               this.DB.AE();
            } else if (GameCanvas.AB(MotherCanvas.w - (GameCanvas.BG << 1), MotherCanvas.h - GameCanvas.hCommand - 5, GameCanvas.BG << 1, GameCanvas.hCommand + 10)) {
               this.DB.AD();
            }
         }

         if (this.center != null) {
            if (this.center.AF()) {
               this.center.AE();
               return;
            }

            if (GameCanvas.AB(MotherCanvas.hw - GameCanvas.BG, MotherCanvas.h - GameCanvas.hCommand - 5, GameCanvas.BG << 1, GameCanvas.hCommand + 10)) {
               this.center.AD();
            }
         }
      }

   }

   public static int AA(int var0, int var1, boolean var2) {
      if (var0 < 0) {
         if (!var2) {
            var0 = 0;
            return var0;
         }
      } else {
         if (var0 <= var1) {
            return var0;
         }

         if (var2) {
            var0 = 0;
            return var0;
         }
      }

      var0 = var1;
      return var0;
   }

   public static void AB(mGraphics var0, int var1, int var2, int var3, int var4) {
      if (var3 < 76) {
         var3 = 76;
      }

      if (var4 > 36) {
         if (Interface_Game.AI == null) {
            Interface_Game.AI = LoadImageStatic.LoadNewInterface("/infonew.png");
         }

         if (var3 == 76) {
            var0.drawRegion(Interface_Game.AI, 0, 0, 38, 45, 0, var1, var2, 0);
            var0.drawRegion(Interface_Game.AI, 54, 0, 38, 45, 0, var1 + 38, var2, 0);
         } else {
            var0.drawRegion(Interface_Game.AI, 18, 0, 74, 36, 0, var1 + var3 - 74, var2, 0);
            if ((var3 -= 74) > 74) {
               var3 = 74;
            }

            var0.drawRegion(Interface_Game.AI, 0, 0, var3, 36, 0, var1, var2, 0);
         }
      } else if (var3 == 76) {
         var0.drawRegion((mImage)imgNenfocus, var1, var2, 0);
      } else {
         var0.drawRegion(imgNenfocus, 2, 0, 74, 36, 0, var1 + var3 - 74, var2, 0);
         if ((var3 -= 74) > 74) {
            var3 = 74;
         }

         var0.drawRegion(imgNenfocus, 0, 0, var3, 36, 0, var1, var2, 0);
      }
   }

   public static void AC(mGraphics var0, int var1, int var2, int var3, int var4) {
      if (var3 > 72) {
         var3 = 72;
      }

      if (var4 > 32) {
         var4 = 32;
      }

      var0.drawRegion(imgNenfocus, 2, 2, var3, var4, 0, var1, var2, 0);
   }

   public static void paintRect(mGraphics var0, int var1, int var2, int var3, int var4, byte var5, int var6) {
      if (var6 >= IG.length) {
         var6 = (byte)(IG.length - 1);
      }

      int var7 = var6 << 2;
      if (var5 == 1) {
         var0.setColor(-330796);
         var0.fillRect(var1 + 1, var2 - 1, var3 - 1, var4 + 3);
         var0.fillRect(var1, var2, var3 + 1, var4 + 1);
         var0.fillRect(var1 - 1, var2 + 1, var3 + 3, var4 - 1);
         fratf1.drawFrame(0, var1 - 2, var2 - 2, 0, 0, var0);
         fratf1.drawFrame(1, var1 - 2, var2 + var4 - 3, 0, 0, var0);
         fratf1.drawFrame(2, var1 + var3 - 3, var2 - 2, 0, 0, var0);
         fratf1.drawFrame(3, var1 + var3 - 3, var2 + var4 - 3, 0, 0, var0);
      } else if (var5 == 3) {
         var0.setColor(-727269);
         var0.fillRect(var1 + 1, var2 - 1, var3 - 1, var4 + 3);
         var0.fillRect(var1, var2, var3 + 1, var4 + 1);
         var0.fillRect(var1 - 1, var2 + 1, var3 + 3, var4 - 1);
         fratf1.drawFrame(4, var1 - 2, var2 - 2, 0, 0, var0);
         fratf1.drawFrame(5, var1 - 2, var2 + var4 - 3, 0, 0, var0);
         fratf1.drawFrame(6, var1 + var3 - 3, var2 - 2, 0, 0, var0);
         fratf1.drawFrame(7, var1 + var3 - 3, var2 + var4 - 3, 0, 0, var0);
      }

      var0.setColor(IG[var6][0]);
      var0.fillRect(var1 + 4, var2, var3 - 7, var4 + 1);
      var0.fillRect(var1, var2 + 4, var3 + 1, var4 - 7);
      var0.setColor(IG[var6][1]);
      var0.fillRect(var1 + 4, var2, var3 - 7, 1);
      var0.fillRect(var1, var2 + 4, 1, var4 - 7);
      if (var5 == 2) {
         fratf.drawFrame(var7 + 2, var1 + var3 - 3, var2 - 2, 0, 0, var0);
         fratf.drawFrame(var7 + 3, var1 + var3 - 3, var2 + var4 - 3, 0, 0, var0);
         fratf.drawFrame(var7, var1 - 2, var2 - 2, 0, 0, var0);
         fratf.drawFrame(var7 + 1, var1 - 2, var2 + var4 - 3, 0, 0, var0);
         var0.setColor(IG[var6][1]);
         var0.fillRect(var1 + 1, var2 + var4, var3 - 1, 1);
         var0.fillRect(var1 + var3, var2 + 1, 1, var4 - 1);
      } else {
         fratf.drawFrame(var7 + 2, var1 + var3 - 3, var2 - 2, 0, 0, var0);
         fratf.drawFrame(var7 + 3, var1 + var3 - 3, var2 + var4 - 3, 0, 0, var0);
         fratf.drawFrame(var7, var1 - 2, var2 - 2, 0, 0, var0);
         fratf.drawFrame(var7 + 1, var1 - 2, var2 + var4 - 3, 0, 0, var0);
      }
   }

   public static void AD(mGraphics var0, int var1, int var2, int var3, int var4) {
      var0.setColor(IH[2][1]);
      var0.fillRect(var1, var2, var3, var4);
      var0.fillRect(var1 - 1, var2 + 1, 1, var4 - 2);
      var0.fillRect(var1 + 1, var2 - 1, var3 - 2, 1);
      var0.fillRect(var1 + var3 - 1 + 1, var2 + 1, 1, var4 - 2);
      var0.fillRect(var1 + 1, var2 + var4, var3 - 2, 1);
   }

   public static void AA(mGraphics var0, int var1, int var2, int var3, int var4, int var5) {
      if (var5 >= IG.length) {
         var5 = (byte)(IG.length - 1);
      }

      var0.setColor(IH[var5][4]);
      var0.fillRect(var1, var2, var3, var4);
      var0.fillRect(var1 - 1, var2 + 1, 1, var4 - 2);
      var0.fillRect(var1 + 1, var2 - 1, var3 - 2, 1);
      var0.fillRect(var1 + var3 - 1 + 1, var2 + 1, 1, var4 - 2);
      var0.fillRect(var1 + 1, var2 + var4, var3 - 2, 1);
      var0.setColor(IH[var5][2]);
      var0.fillRect(var1 + 1, var2 + 1, var3 - 2, var4 / 2 - 1);
      var0.setColor(IH[var5][3]);
      var0.fillRect(var1 + 1, var2 + 1 + var4 / 2 - 1, var3 - 2, var4 - var4 / 2 - 1);
      var0.setColor(IH[var5][0]);
      var0.fillRect(var1, var2 + 1, 1, var4 - 2);
      var0.fillRect(var1 + 1, var2, var3 - 2, 1);
      var0.setColor(IH[var5][1]);
      var0.fillRect(var1 + var3 - 1, var2 + 1, 1, var4 - 2);
      var0.fillRect(var1 + 1, var2 + var4 - 1, var3 - 2, 1);
   }

   public static void AB(mGraphics var0, int var1, int var2, int var3, int var4, int var5) {
      if (mimgBgA == null) {
         AF(var0, var1 + 16, var2 + 16, var3 - 32, var4 - 32, var5);
      } else {
         byte var6 = 0;
         byte var7 = 36;
         byte var8 = 72;
         if (var5 == 1) {
            var6 = 22;
            var7 = 32;
            var8 = 68;
         } else if (var5 == 2) {
            var6 = 19;
            var7 = 32;
            var8 = 68;
         }

         var0.drawRegion((mImage)mimgBgA[var6 + 0], var1, var2, 0);
         var0.drawRegion((mImage)mimgBgA[var6 + 2], var1 + var3 - 36, var2, 0);
         int var9 = (var3 - 73) / var7 + 1;

         int var10;
         for(var10 = 0; var10 <= var9; ++var10) {
            if (var10 == var9) {
               var0.drawRegion((mImage)mimgBgA[var6 + 1], var1 + var3 - var8, var2, 0);
            } else {
               var0.drawRegion((mImage)mimgBgA[var6 + 1], var1 + var7 + var10 * var7, var2, 0);
            }
         }

         var9 = (var3 - 73) / 36 + 1;

         for(var10 = 0; var10 < var9; ++var10) {
            if (var10 == var9 - 1) {
               var0.drawRegion((mImage)mimgBgA[7], var1 + var3 - 72, var2 + var4 - 38, 0);
            } else {
               var0.drawRegion((mImage)mimgBgA[7], var1 + 36 + var10 * 36, var2 + var4 - 38, 0);
            }
         }

         var10 = (var4 - 36 - 39) / 38 + 1;

         int var11;
         for(var11 = 0; var11 < var10; ++var11) {
            if (var11 == var10 - 1) {
               var0.drawRegion((mImage)mimgBgA[3], var1 + 4, var2 + var4 - 76, 0);
               var0.drawRegion((mImage)mimgBgA[5], var1 + var3 - 4 - 36, var2 + var4 - 76, 0);
            } else {
               var0.drawRegion((mImage)mimgBgA[3], var1 + 4, var2 + 36 + var11 * 38, 0);
               var0.drawRegion((mImage)mimgBgA[5], var1 + var3 - 4 - 36, var2 + 36 + var11 * 38, 0);
            }
         }

         var0.drawRegion((mImage)mimgBgA[4], var1 + 4, var2 + var4 - 38, 0);
         var0.drawRegion((mImage)mimgBgA[6], var1 + var3 - 36 - 4, var2 + var4 - 38, 0);
         int var12 = var1 + 12;
         int var13 = var2 + 22;
         if (var5 == 2) {
            var13 = var2 + 26;
         }

         var4 -= 30;
         int var14 = var3 - 24;
         var0.drawRegion((mImage)mimgBgA[8], var12, var13, 0);
         var0.drawRegion((mImage)mimgBgA[10], var12 + var14 - 36, var13, 0);
         var9 = (var14 - 73) / 36 + 1;

         for(var11 = 0; var11 < var9; ++var11) {
            if (var11 == var9 - 1) {
               var0.drawRegion((mImage)mimgBgA[9], var12 + var14 - 72, var13, 0);
               var0.drawRegion((mImage)mimgBgA[15], var12 + var14 - 72, var13 + var4 - 36, 0);
            } else {
               var0.drawRegion((mImage)mimgBgA[9], var12 + 36 + var11 * 36, var13, 0);
               var0.drawRegion((mImage)mimgBgA[15], var12 + 36 + var11 * 36, var13 + var4 - 36, 0);
            }
         }

         var10 = (var4 - 36 - 37) / 36 + 1;

         for(var11 = 0; var11 < var10; ++var11) {
            if (var11 == var10 - 1) {
               var0.drawRegion((mImage)mimgBgA[11], var12, var13 + var4 - 72, 0);
               var0.drawRegion((mImage)mimgBgA[13], var12 + var14 - 36, var13 + var4 - 72, 0);
            } else {
               var0.drawRegion((mImage)mimgBgA[11], var12, var13 + 36 + var11 * 36, 0);
               var0.drawRegion((mImage)mimgBgA[13], var12 + var14 - 36, var13 + 36 + var11 * 36, 0);
            }
         }

         var0.drawRegion((mImage)mimgBgA[12], var12, var13 + var4 - 36, 0);
         var0.drawRegion((mImage)mimgBgA[14], var12 + var14 - 36, var13 + var4 - 36, 0);
         var0.setColor(-530490);
         var0.fillRect(var12 + 35, var13 + 35, var14 - 70, var4 - 70);
         if (var5 == 0) {
            var0.drawRegion((mImage)mimgBgA[16], var1 + var3 / 2 - 84, var2 - 24, 0);
            var0.drawRegion((mImage)mimgBgA[18], var1 + var3 / 2 + 84 - 24, var2 - 24, 0);

            for(var11 = 0; var11 < 5; ++var11) {
               var0.drawRegion((mImage)mimgBgA[17], var1 + var3 / 2 - 84 + 24 + var11 * 24, var2 - 24, 0);
            }
         }

      }
   }

   public static void AE(mGraphics var0, int var1, int var2, int var3, int var4) {
      if (GameCanvas.lowGraphic) {
         AG(var0, var1, var2, var3, var4, 1);
      } else {
         int var5;
         if (FK == null && !GameCanvas.lowGraphic) {
            FK = new mImage[9];

            for(var5 = 0; var5 < FK.length; ++var5) {
               FK[var5] = mImage.createImage("/interface/bgb" + var5 + ".png");
            }
         }

         var0.drawRegion((mImage)FK[0], var1, var2, 0);
         var0.drawRegion((mImage)FK[2], var1 + var3 - 36, var2, 0);
         var5 = (var3 - 73) / 36 + 1;

         int var6;
         for(var6 = 0; var6 < var5; ++var6) {
            if (var6 == var5 - 1) {
               var0.drawRegion((mImage)FK[1], var1 + var3 - 72, var2, 0);
               var0.drawRegion((mImage)FK[7], var1 + var3 - 72, var2 + var4 - 36, 0);
            } else {
               var0.drawRegion((mImage)FK[1], var1 + 36 + var6 * 36, var2, 0);
               var0.drawRegion((mImage)FK[7], var1 + 36 + var6 * 36, var2 + var4 - 36, 0);
            }
         }

         var6 = (var4 - 36 - 37) / 36 + 1;

         for(var5 = 0; var5 < var6; ++var5) {
            if (var5 == var6 - 1) {
               var0.drawRegion((mImage)FK[3], var1, var2 + var4 - 72, 0);
               var0.drawRegion((mImage)FK[5], var1 + var3 - 36, var2 + var4 - 72, 0);
            } else {
               var0.drawRegion((mImage)FK[3], var1, var2 + 36 + var5 * 36, 0);
               var0.drawRegion((mImage)FK[5], var1 + var3 - 36, var2 + 36 + var5 * 36, 0);
            }
         }

         var0.drawRegion((mImage)FK[4], var1, var2 + var4 - 36, 0);
         var0.drawRegion((mImage)FK[6], var1 + var3 - 36, var2 + var4 - 36, 0);
         var0.setColor(-1384243);
         var0.fillRect(var1 + 35, var2 + 35, var3 - 70, var4 - 70);
      }
   }

   public static void AF(mGraphics var0, int var1, int var2, int var3, int var4) {
      if (GameCanvas.lowGraphic) {
         AG(var0, var1, var2, var3, var4, 1);
      } else {
         int var5;
         if (FL == null && !GameCanvas.lowGraphic) {
            FL = new mImage[9];

            for(var5 = 0; var5 < FL.length; ++var5) {
               FL[var5] = mImage.createImage("/interface/bgc" + var5 + ".png");
            }
         }

         var0.drawRegion((mImage)FL[0], var1, var2, 0);
         var0.drawRegion((mImage)FL[2], var1 + var3 - 46, var2, 0);
         var5 = (var3 - 93) / 46 + 1;

         int var6;
         for(var6 = 0; var6 < var5; ++var6) {
            if (var6 == var5 - 1) {
               var0.drawRegion((mImage)FL[1], var1 + var3 - 92, var2, 0);
               var0.drawRegion((mImage)FL[7], var1 + var3 - 92, var2 + var4 - 46, 0);
            } else {
               var0.drawRegion((mImage)FL[1], var1 + 46 + var6 * 46, var2, 0);
               var0.drawRegion((mImage)FL[7], var1 + 46 + var6 * 46, var2 + var4 - 46, 0);
            }
         }

         var6 = (var4 - 46 - 47) / 46 + 1;

         for(var5 = 0; var5 < var6; ++var5) {
            if (var5 == var6 - 1) {
               var0.drawRegion((mImage)FL[3], var1, var2 + var4 - 92, 0);
               var0.drawRegion((mImage)FL[5], var1 + var3 - 46, var2 + var4 - 72, 0);
            } else {
               var0.drawRegion((mImage)FL[3], var1, var2 + 46 + var5 * 46, 0);
               var0.drawRegion((mImage)FL[5], var1 + var3 - 46, var2 + 46 + var5 * 46, 0);
            }
         }

         var0.drawRegion((mImage)FL[4], var1, var2 + var4 - 46, 0);
         var0.drawRegion((mImage)FL[6], var1 + var3 - 46, var2 + var4 - 46, 0);
         var0.setColor(-67617);
         var0.fillRect(var1 + 45, var2 + 45, var3 - 90, var4 - 90);
      }
   }

   public static void AG(mGraphics var0, int var1, int var2, int var3, int var4) {
      if (mimgBgA == null) {
         AF(var0, var1 + 16, var2 + 16, var3 - 32, var4 - 32, 2);
      } else {
         var0.setColor(-1926578);
         var0.fillRect(var1 + 36, var2 + 36, var3 - 72, var4 - 72);
         var0.drawRegion((mImage)mimgBgA[19], var1, var2, 0);
         var0.drawRegion((mImage)mimgBgA[21], var1 + var3 - 36, var2, 0);
         int var5 = (var3 - 73) / 32 + 1;

         int var6;
         for(var6 = 0; var6 <= var5; ++var6) {
            if (var6 == var5) {
               var0.drawRegion((mImage)mimgBgA[20], var1 + var3 - 68, var2, 0);
            } else {
               var0.drawRegion((mImage)mimgBgA[20], var1 + 32 + (var6 << 5), var2, 0);
            }
         }

         var5 = (var3 - 73) / 36 + 1;

         for(var6 = 0; var6 < var5; ++var6) {
            if (var6 == var5 - 1) {
               var0.drawRegion((mImage)mimgBgA[7], var1 + var3 - 72, var2 + var4 - 38, 0);
            } else {
               var0.drawRegion((mImage)mimgBgA[7], var1 + 36 + var6 * 36, var2 + var4 - 38, 0);
            }
         }

         var6 = (var4 - 36 - 39) / 38 + 1;

         for(var5 = 0; var5 < var6; ++var5) {
            if (var5 == var6 - 1) {
               var0.drawRegion((mImage)mimgBgA[3], var1 + 4, var2 + var4 - 76, 0);
               var0.drawRegion((mImage)mimgBgA[5], var1 + var3 - 4 - 36, var2 + var4 - 76, 0);
               var0.drawRegion((mImage)mimgBgA[25], var1 + var3 - 72 - 4 - 36, var2 + var4 - 76, 0);
               var0.drawRegion((mImage)mimgBgA[25], var1 + var3 - 36 - 4 - 36, var2 + var4 - 76, 0);
            } else {
               var0.drawRegion((mImage)mimgBgA[3], var1 + 4, var2 + 36 + var5 * 38, 0);
               var0.drawRegion((mImage)mimgBgA[5], var1 + var3 - 4 - 36, var2 + 36 + var5 * 38, 0);
               var0.drawRegion((mImage)mimgBgA[25], var1 + var3 - 72 - 4 - 36, var2 + 36 + var5 * 38, 0);
               var0.drawRegion((mImage)mimgBgA[25], var1 + var3 - 36 - 4 - 36, var2 + 36 + var5 * 38, 0);
            }
         }

         var0.drawRegion((mImage)mimgBgA[4], var1 + 4, var2 + var4 - 38, 0);
         var0.drawRegion((mImage)mimgBgA[6], var1 + var3 - 36 - 4, var2 + var4 - 38, 0);
      }
   }

   private static void AF(mGraphics var0, int var1, int var2, int var3, int var4, int var5) {
      if (var5 == 0) {
         AG(var0, var1 + var3 / 2 - 84, var2 - 5 - 16, 168, 22, 1);
         var1 += 10;
         var3 -= 20;
         var2 += 14;
         var4 -= 18;
      }

      AG(var0, var1, var2, var3, var4, var5);
   }

   public static void AC(mGraphics var0, int var1, int var2, int var3, int var4, int var5) {
      int var6 = var3;
      if (mimgWanted == null) {
         var6 = 0;
      }

      if (var5 == 0) {
         AI(var0, var1 - 14, var2 - 6, var3, 0);
         AI(var0, var1 - 8, var2 - 3, var3, 0);
      } else if (var5 == 1) {
         AI(var0, var1 + var6 + 14, var2 + 6, var3, 1);
         AI(var0, var1 + var6 + 8, var2 + 3, var3, 1);
      } else if (var5 == 2) {
         AI(var0, var1 - 8, var2 - 3, var3, 0);
         AI(var0, var1 + var6 + 8, var2 + 3, var3, 1);
      }

      if (mimgWanted == null) {
         AF(var0, var1, var2, var3, var4, 5);
         FontBorderColor(var0, T.QT, var1 + var3 / 2, var2 + GameCanvas.hCommand / 4, 2, (int)0, (int)7);
         var0.setColor(-7246254);
         var0.fillRect(var1 + 16, var2 + 24, var3 - 32, 62);
         var0.fillRect(var1 + 16, var2 + 98, var3 - 32, 20);
         var0.drawRegion((mImage)imgBeri, var1 + 20, var2 + 130, 3);
      } else {
         var0.setColor(-2374497);
         var0.fillRect(var1 + 10, var2 + 10, var3 - 20, var4 - 20);
         var0.drawRegion((mImage)mimgWanted[0], var1, var2, 0);
         var0.drawRegion((mImage)mimgWanted[2], var1 + var3 - 15, var2, 0);
         var5 = (var3 - 30) / 38;

         for(var6 = 0; var6 <= var5; ++var6) {
            if (var6 == var5) {
               var0.drawRegion((mImage)mimgWanted[1], var1 + var3 - 53, var2, 0);
               var0.drawRegion((mImage)mimgWanted[7], var1 + var3 - 53, var2 + var4 - 15, 0);
            } else {
               var0.drawRegion((mImage)mimgWanted[1], var1 + 15 + var6 * 38, var2, 0);
               var0.drawRegion((mImage)mimgWanted[7], var1 + 15 + var6 * 38, var2 + var4 - 15, 0);
            }
         }

         var6 = (var4 - 30) / 40;

         for(var5 = 0; var5 <= var6; ++var5) {
            if (var5 == var6) {
               var0.drawRegion((mImage)mimgWanted[3], var1, var2 + var4 - 55, 0);
               var0.drawRegion((mImage)mimgWanted[5], var1 + var3 - 15, var2 + var4 - 55, 0);
            } else {
               var0.drawRegion((mImage)mimgWanted[3], var1, var2 + 15 + var5 * 40, 0);
               var0.drawRegion((mImage)mimgWanted[5], var1 + var3 - 15, var2 + 15 + var5 * 40, 0);
            }
         }

         var0.drawRegion((mImage)mimgWanted[4], var1, var2 + var4 - 15, 0);
         var0.drawRegion((mImage)mimgWanted[6], var1 + var3 - 15, var2 + var4 - 15, 0);
         var0.drawRegion((mImage)mimgWanted[12], var1 + var3 / 2, var2 + 12, 3);
         var0.setColor(-3757444);
         var0.fillRect(var1 + 15, var2 + 22, var3 - 30, 66);
         var0.fillRect(var1 + 15, var2 + 100, var3 - 32, 16);
         var0.setColor(-5995930);
         var0.fillRect(var1 + 16, var2 + 23, var3 - 30, 64);
         var0.fillRect(var1 + 16, var2 + 101, var3 - 32, 14);
         var0.drawRegion((mImage)mimgWanted[13], var1 + var3 / 2, var2 + 94, 3);
         var0.drawRegion((mImage)mimgWanted[14], var1 + 23, var2 + 130, 3);
         var0.drawRegion((mImage)mimgWanted[16], var1 + 7, var2 + 93, 0);
         var0.drawRegion(mimgWanted[16], 0, 0, 6, 46, 2, var1 + var3 - 13, var2 + 93, 0);
         var0.drawRegion((mImage)mimgWanted[15], var1 + 102, var2 + 145, 3);
         var0.drawRegion((mImage)mimgWanted[17], var1 + 48, var2 + 144, 3);
      }
   }

   private static void AI(mGraphics var0, int var1, int var2, int var3, int var4) {
      if (GameCanvas.lowGraphic) {
         AF(var0, var1, var2, var3, 154, 5);
      } else if (mimgWanted != null) {
         int var5 = var1;
         if (var4 == 0) {
            var0.drawRegion((mImage)mimgWanted[0], var1, var2, 0);
            var0.drawRegion((mImage)mimgWanted[4], var1, var2 + 154 - 15, 0);
            var0.drawRegion((mImage)mimgWanted[2], var1 + var3 - 15, var2, 0);
         } else {
            var5 = var1 - 15;
            var0.drawRegion((mImage)mimgWanted[2], var5, var2, 0);
            var0.drawRegion((mImage)mimgWanted[6], var5, var2 + 154 - 15, 0);
            var0.drawRegion((mImage)mimgWanted[4], var5 - var3 + 15, var2 + 154 - 15, 0);
         }

         var1 = (var3 - 30) / 38;

         for(int var6 = 0; var6 <= var1; ++var6) {
            if (var4 == 0) {
               if (var6 == var1) {
                  var0.drawRegion((mImage)mimgWanted[1], var5 + var3 - 53, var2, 0);
               } else {
                  var0.drawRegion((mImage)mimgWanted[1], var5 + 15 + var6 * 38, var2, 0);
               }
            } else if (var6 == var1) {
               var0.drawRegion((mImage)mimgWanted[7], var5 - var3 + 30, var2 + 154 - 15, 0);
            } else {
               var0.drawRegion((mImage)mimgWanted[7], var5 - 38 - var6 * 38, var2 + 154 - 15, 0);
            }
         }

         for(var1 = 0; var1 <= 3; ++var1) {
            if (var1 == 3) {
               if (var4 == 0) {
                  var0.drawRegion((mImage)mimgWanted[3], var5, var2 + 154 - 55, 0);
               } else {
                  var0.drawRegion((mImage)mimgWanted[5], var5, var2 + 154 - 55, 0);
               }
            } else if (var4 == 0) {
               var0.drawRegion((mImage)mimgWanted[3], var5, var2 + 15 + var1 * 40, 0);
            } else {
               var0.drawRegion((mImage)mimgWanted[5], var5, var2 + 15 + var1 * 40, 0);
            }
         }

      }
   }

   private static void AG(mGraphics var0, int var1, int var2, int var3, int var4, int var5) {
      var0.setColor(-330796);
      var0.fillRect(var1, var2, var3, var4);
      var0.setColor(AB[var5]);
      var0.fillRect(var1 + 1, var2 + 1, var3 - 2, var4 - 2);
   }

   public static void AA(mGraphics var0, String var1, int var2, int var3, int var4) {
      mFont.tahoma_7b_black.drawString(var0, var1, var2 + 1, var3 + 1, var4);
      mFont.tahoma_7b_white.drawString(var0, var1, var2, var3, var4);
   }

   public static void AA(mGraphics var0, String var1, int var2, int var3, int var4, byte var5) {
      mFont.tahoma_7b_black.drawString(var0, var1, var2 + 1, var3 + 1, var4);
      AC(var5).drawString(var0, var1, var2, var3, var4);
   }

   public static void AB(mGraphics var0, String var1, int var2, int var3, int var4, byte var5) {
      mFont.tahoma_7_black.drawString(var0, var1, var2 + 1, var3 + 1, 2);
      AB(var5).drawString(var0, var1, var2, var3, 2);
   }

   public static void AA(mGraphics var0, String var1, int var2, int var3, int var4, byte var5, byte var6) {
      AC(0).drawString(var0, var1, var2 + 1, var3 + 1, 0);
      AC(7).drawString(var0, var1, var2, var3, 0);
   }

   public void paintSelect(mGraphics var1, int var2, int var3, int var4, int var5) {
      var1.setColor(-2458);
      var1.fillRect(var2, var3, var4, var5);
   }

   public static mFont AB(int var0) {
      switch(var0) {
      case 0:
         return mFont.tahoma_7_white;
      case 1:
         return mFont.tahoma_7_green;
      case 2:
         return mFont.tahoma_7_violet;
      case 3:
         return mFont.tahoma_7_orange;
      case 4:
         return mFont.tahoma_7_blue;
      case 5:
         return mFont.tahoma_7_yellow;
      case 6:
         return mFont.tahoma_7_red;
      case 7:
         return mFont.tahoma_7_black;
      default:
         return mFont.tahoma_7_white;
      }
   }

   public static mFont AC(int var0) {
      switch(var0) {
      case 0:
         return mFont.tahoma_7b_white;
      case 1:
         return mFont.tahoma_7b_green;
      case 2:
         return mFont.tahoma_7b_violet;
      case 3:
         return mFont.tahoma_7b_orange;
      case 4:
         return mFont.tahoma_7b_blue;
      case 5:
         return mFont.tahoma_7b_yellow;
      case 6:
         return mFont.tahoma_7b_red;
      case 7:
         return mFont.tahoma_7b_black;
      case 8:
         return mFont.tahoma_7b_brown;
      default:
         return mFont.tahoma_7b_white;
      }
   }

   public final void AD(mGraphics var1, int var2, int var3, int var4, int var5, int var6) {
      if (GameCanvas.lowGraphic) {
         paintRect(var1, var2 + 4, var3 + 4, var4 - 8, var5 - 8, (byte)1, 3);
      } else {
         int var7 = -464704;
         mImage[] var10;
         if (var6 == IK) {
            if (AA == null) {
               AT();
               return;
            }

            var10 = AA;
            var7 = -6767646;
         } else {
            var10 = imgPaper;
         }

         if (var5 % 2 == 0) {
            ++var5;
         }

         var1.setColor(var7);
         var1.fillRect(var2, var3 + 5, var4, var5 - 10);
         var7 = 15;
         int var8;
         if ((var8 = var4 / 2 - 15) < 0) {
            var8 = 0;
         }

         int var9;
         for(var9 = 0; var9 <= var8; var9 += 30) {
            var1.drawRegion((mImage)var10[5], var2 + var4 / 2 + var9 - 15, var3 + 3, 0);
            var1.drawRegion((mImage)var10[6], var2 + var4 / 2 + var9 - 15, var3 + var5 - 1 - 8, 0);
            if (var9 != 0) {
               var1.drawRegion((mImage)var10[5], var2 + var4 / 2 - var9 - 15, var3 + 3, 0);
               var1.drawRegion((mImage)var10[6], var2 + var4 / 2 - var9 - 15, var3 + var5 - 1 - 8, 0);
            }

            var7 = var9 + 15;
         }

         if ((var9 = (var4 / 2 - 15) % 30) != 0 && var9 > 0) {
            var1.drawRegion(var10[5], 0, 0, var9, 8, 0, var2 + var4 / 2 + var7, var3 + 3, 0);
            var1.drawRegion(var10[6], 0, 0, var9, 8, 0, var2 + var4 / 2 + var7, var3 + var5 - 1 - 8, 0);
            var1.drawRegion(var10[5], 0, 0, var9, 8, 0, var2 + var4 / 2 - var7 - var9, var3 + 3, 0);
            var1.drawRegion(var10[6], 0, 0, var9, 8, 0, var2 + var4 / 2 - var7 - var9, var3 + var5 - 1 - 8, 0);
         }

         if (var4 >= 20) {
            for(var7 = 0; var7 < var5 - 38; var7 += 10) {
               var1.drawRegion(var10[7], 0, 0, 8, 10, 2, var2 + var4, var3 + 19 + var7, 24);
            }

            var1.drawRegion(var10[3], 0, 0, 8, 16, 2, var2 + var4, var3 + 3, 24);
            var1.drawRegion(var10[4], 0, 0, 8, 17, 2, var2 + var4, var3 + var5 - 2 - 16, 24);
         }

         if (var4 >= 20) {
            for(var7 = 0; var7 < var5 - 37; var7 += 10) {
               var1.drawRegion((mImage)var10[7], var2, var3 + 19 + var7, 0);
            }

            var1.drawRegion((mImage)var10[3], var2, var3 + 3, 0);
            var1.drawRegion((mImage)var10[4], var2, var3 + var5 - 2 - 16, 0);
         }

         for(var7 = 0; var7 < var5 - 37; var7 += 10) {
            var1.drawRegion(var10[1], 0, 0, 14, 10, 2, var2 + var4 + 14, var3 + 19 + var7, 24);
         }

         var1.drawRegion(var10[0], 0, 0, 14, 19, 2, var2 + var4 + 14, var3, 24);
         var1.drawRegion(var10[2], 0, 0, 14, 19, 2, var2 + var4 + 14, var3 + var5 - 19, 24);

         for(var7 = 0; var7 < var5 - 38; var7 += 10) {
            var1.drawRegion((mImage)var10[1], var2 - 14, var3 + 19 + var7, 0);
         }

         var1.drawRegion((mImage)var10[0], var2 - 14, var3, 0);
         var1.drawRegion((mImage)var10[2], var2 - 14, var3 + var5 - 19, 0);
      }
   }

   public static void AT() {
      AA = new mImage[8];

      int var0;
      for(var0 = 0; var0 < AA.length; ++var0) {
         AA[var0] = mImage.createImage("/interface/papern" + var0 + ".png");
      }

      FP = new mImage[8];

      for(var0 = 0; var0 < FP.length; ++var0) {
         FP[var0] = mImage.createImage("/interface/law" + var0 + ".png");
      }

   }

   public final void AE(mGraphics var1, int var2, int var3, int var4, int var5, int var6) {
      if (imgPaperDoc == null) {
         this.AD(var1, var2, var3 - 4, var4, var5 + 4, 0);
      } else {
         if (var4 % 2 == 0) {
            ++var4;
         }

         for(var6 = 0; var6 < var4 - 38; var6 += 10) {
            var1.drawRegion(imgPaperDoc[1], 0, 0, 10, 14, 0, var2 - 2 + 19 + var6, var3 + 9, 36);
         }

         var1.drawRegion(imgPaperDoc[2], 0, 0, 19, 14, 0, var2 - 1, var3 + 9, 36);
         var1.drawRegion(imgPaperDoc[2], 0, 0, 19, 14, 2, var2 - 2 + var4 - 19, var3 + 9, 36);
         var1.setColor(-464704);
         var1.fillRect(var2 + 5, var3, var4 - 10, var5);
         var6 = 15;
         int var7;
         if ((var7 = var5 / 2 - 15) < 0) {
            var7 = 0;
         }

         int var8;
         for(var8 = 0; var8 <= var7; var8 += 30) {
            var1.drawRegion(imgPaperDoc[6], 0, 0, 8, 30, 0, var2 + 3, var3 + var5 / 2 + var8 - 15, 0);
            var1.drawRegion(imgPaperDoc[6], 0, 0, 8, 30, 2, var2 + var4 - 1 - 8, var3 + var5 / 2 + var8 - 15, 0);
            if (var8 != 0) {
               var1.drawRegion(imgPaperDoc[6], 0, 0, 8, 30, 0, var2 + 3, var3 + var5 / 2 - var8 - 15, 0);
               var1.drawRegion(imgPaperDoc[6], 0, 0, 8, 30, 2, var2 + var4 - 1 - 8, var3 + var5 / 2 - var8 - 15, 0);
            }

            var6 = var8 + 15;
         }

         if ((var8 = (var5 / 2 - 15) % 30) != 0 && var8 > 0) {
            var1.drawRegion(imgPaperDoc[6], 0, 0, 8, var8, 0, var2 + 3, var3 + var5 / 2 + var6, 0);
            var1.drawRegion(imgPaperDoc[6], 0, 0, 8, var8, 2, var2 + var4 - 1 - 8, var3 + var5 / 2 + var6, 0);
            var1.drawRegion(imgPaperDoc[6], 0, 0, 8, var8, 0, var2 + 3, var3 + var5 / 2 - var6 - var8, 0);
            var1.drawRegion(imgPaperDoc[6], 0, 0, 8, var8, 2, var2 + var4 - 1 - 8, var3 + var5 / 2 - var6 - var8, 0);
         }

         if (var5 >= 20) {
            for(var6 = 0; var6 < var4 - 38; var6 += 10) {
               var1.drawRegion(imgPaperDoc[7], 0, 0, 10, 8, 0, var2 + 19 + var6, var3, 0);
            }

            var1.drawRegion(imgPaperDoc[4], 0, 0, 17, 8, 0, var2 + 3, var3, 0);
            var1.drawRegion(imgPaperDoc[4], 0, 0, 17, 8, 2, var2 + var4 - 2 - 16, var3, 0);
         }

         if (var5 >= 20) {
            for(var6 = 0; var6 < var4 - 38; var6 += 10) {
               var1.drawRegion(imgPaperDoc[7], 0, 0, 10, 8, 1, var2 + 19 + var6, var3 + var5, 36);
            }

            var1.drawRegion(imgPaperDoc[4], 0, 0, 17, 8, 1, var2 + 3, var3 + var5, 36);
            var1.drawRegion(imgPaperDoc[4], 0, 0, 17, 8, 3, var2 + var4 - 2 - 16, var3 + var5, 36);
         }

         for(var6 = 0; var6 < var4 - 38; var6 += 10) {
            var1.drawRegion(imgPaperDoc[1], 0, 0, 10, 14, 1, var2 + 2 + 19 + var6, var3 + var5, 0);
         }

         var1.drawRegion(imgPaperDoc[2], 0, 0, 19, 14, 1, var2 + 2, var3 + var5, 0);
         var1.drawRegion(imgPaperDoc[2], 0, 0, 19, 14, 3, var2 + 2 + var4 - 19, var3 + var5, 0);
      }
   }

   public static String AA(long var0) {
      String var2 = "";
      boolean var3;
      if (var3 = var0 < 0L) {
         var0 = -var0;
      }

      long var4 = var0 / 1000L + 1L;

      for(int var6 = 0; (long)var6 < var4; ++var6) {
         if (var0 < 1000L) {
            var2 = var0 + var2;
            break;
         }

         long var7;
         if ((var7 = var0 % 1000L) == 0L) {
            var2 = ".000" + var2;
         } else if (var7 < 10L) {
            var2 = ".00" + var7 + var2;
         } else if (var7 < 100L) {
            var2 = ".0" + var7 + var2;
         } else {
            var2 = "." + var7 + var2;
         }

         var0 /= 1000L;
      }

      if (var3) {
         var2 = "-" + var2;
      }

      return var2;
   }

   public static String AB(long var0) {
      String var2;
      if (var0 >= 1000000000L) {
         var2 = var0 / 1000000000L + "," + var0 % 1000000000L / 100000000L + "B";
      } else {
         if (var0 <= 1000000L) {
            return AA(var0);
         }

         var2 = var0 / 1000000L + "," + var0 % 1000000L / 100000L + "M";
      }

      return var2;
   }

   public static void FontBorderColor(mGraphics var0, String var1, int var2, int var3, int var4, int var5, int var6) {
      AC(var6).drawString(var0, var1, var2 - 1, var3 - 1, var4);
      AC(var6).drawString(var0, var1, var2 - 1, var3 + 1, var4);
      AC(var6).drawString(var0, var1, var2 + 1, var3 - 1, var4);
      AC(var6).drawString(var0, var1, var2 + 1, var3 + 1, var4);
      AC(var6).drawString(var0, var1, var2 - 1, var3, var4);
      AC(var6).drawString(var0, var1, var2 + 1, var3, var4);
      AC(var6).drawString(var0, var1, var2, var3 - 1, var4);
      AC(var6).drawString(var0, var1, var2, var3 + 1, var4);
      AC(var5).drawString(var0, var1, var2, var3, var4);
   }

   public static void AA(mGraphics var0, String var1, int var2, int var3, int var4, int var5) {
      if (var5 == -1) {
         mFont.tahoma_7b_black.drawString(var0, var1, var2 + 1, var3 + 1, 2);
      } else if (var5 >= 0) {
         AC(var5).drawString(var0, var1, var2 - 1, var3 - 1, 2);
         AC(var5).drawString(var0, var1, var2 - 1, var3 + 1, 2);
         AC(var5).drawString(var0, var1, var2 + 1, var3 - 1, 2);
         AC(var5).drawString(var0, var1, var2 + 1, var3 + 1, 2);
         AC(var5).drawString(var0, var1, var2 - 1, var3, 2);
         AC(var5).drawString(var0, var1, var2 + 1, var3, 2);
         AC(var5).drawString(var0, var1, var2, var3 - 1, 2);
         AC(var5).drawString(var0, var1, var2, var3 + 1, 2);
      }

      AC(AC[GameCanvas.gameTick / 7 % AC.length]).drawString(var0, var1, var2, var3, 2);
   }

   public static void AB(mGraphics var0, String var1, int var2, int var3, int var4, int var5) {
      mFont.tahoma_7_black.drawString(var0, var1, var2 - 1, var3, var4);
      mFont.tahoma_7_black.drawString(var0, var1, var2 + 1, var3, var4);
      mFont.tahoma_7_black.drawString(var0, var1, var2, var3 - 1, var4);
      mFont.tahoma_7_black.drawString(var0, var1, var2, var3 + 1, var4);
      AB(var5).drawString(var0, var1, var2, var3, var4);
   }

   public static iCommand AA(iCommand var0, int var1) {
      if (var1 == 1) {
         if (GameCanvas.isTaiTho) {
            var0.setPos(GameCanvas.BG - 3 + 5, MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2, (FrameImage)null, var0.caption);
         } else {
            var0.setPos(GameCanvas.BG - 3, MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2, (FrameImage)null, var0.caption);
         }
      } else if (var1 == 2) {
         if (GameCanvas.isTaiTho) {
            var0.setPos(MotherCanvas.w - GameCanvas.BG + 3 - 5, MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2, (FrameImage)null, var0.caption);
         } else {
            var0.setPos(MotherCanvas.w - GameCanvas.BG + 3, MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2, (FrameImage)null, var0.caption);
         }
      } else if (var1 == 0) {
         var0.setPos(MotherCanvas.hw, MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2, (FrameImage)null, var0.caption);
      }

      return var0;
   }

   public static void AH(mGraphics var0, int var1, int var2, int var3, int var4) {
      var0.drawRegion(imgKhungMem, 0, 0, 10, 10, 0, var1, var2, 0);
      var0.drawRegion(imgKhungMem, 0, 0, 10, 10, 2, var1 + var3 - 10, var2, 0);
      var0.drawRegion(imgKhungMem, 0, 0, 10, 10, 1, var1, var2 + var4 - 10, 0);
      var0.drawRegion(imgKhungMem, 0, 0, 10, 10, 7, var1 + var3 - 10, var2 + var4 - 10, 0);
      var0.setColor(-2573687);
      var0.fillRect(var1 + 10, var2, var3 - 20, var4);
      var0.fillRect(var1, var2 + 10, var3, var4 - 20);
   }
}
