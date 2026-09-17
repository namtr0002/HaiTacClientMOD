public class MainTab extends AvMain {
   public String nameTab;
   public int IdSelect;
   public byte indexIconTab = 0;
   public int AD = 0;
   public static int AE;
   public static int xTab;
   public static int AG = 225;
   public static int AH = 194;
   public static int AI;
   public static int AJ;
   public static int AK = 0;
   private int BN;
   private int BO;
   public int AL;
   public int AM;
   public int AN;
   public int AO;
   public int AP;
   public int AQ;
   public int AR;
   public int AS;
   public int levelTab = 0;
   public byte AU = -1;
   public boolean AV = true;
   public boolean AW = false;
   public byte AX = 0;
   public static byte AY = 0;
   public static int AZ;
   public static int BA;
   public static mImage[] mImgTab;
   public static FrameImage fraCloseTab;
   public static FrameImage fraCloseTab2;
   public static FrameImage fraCmdMo;
   public static FrameImage fraCloseTab3;
   public static CountDownTicket BG = new CountDownTicket();
   public static CountDownTicket BH = new CountDownTicket();
   public static CountDownTicket BI = new CountDownTicket();
   public static CountDownTicket BJ = new CountDownTicket();
   public static CountDownTicket BK = new CountDownTicket();
   private static int BP = -1;
   private static int BQ;
   private static int BR = -9809615;
   private static int BS = -399954;
   public static int BL = 0;
   public static int BM = 0;

   public MainTab() {
      if (MotherCanvas.h >= 300 && GameCanvas.isTouch) {
         this.AW = true;
      }

      AZ = MotherCanvas.w - 77;
      BA = 4;
      this.AS = 5;
      this.BN = 100;
      this.BO = 40;
      this.AQ = AG - 70;
      this.AR = AH - 32;
      this.AO = xTab + AG / 2 - this.AQ / 2 + 10;
      this.AP = AI + 32;
      AY = 15;
   }

   public final void createValue() {
      if (this.AW) {
         AH = 240;
         AG = 260;
      }

      if (AG > MotherCanvas.w) {
         AG = MotherCanvas.w;
      }

      if (AH > MotherCanvas.h - 55 - GameCanvas.hCommand) {
         AH = MotherCanvas.h - 55 - GameCanvas.hCommand;
      }

      if (GameCanvas.AV) {
         AH += 30;
      }

      AJ = 28;
      if (28 << 3 > AH) {
         AJ = AH / 8;
      }

      if (GameCanvas.isSmallScreen) {
         AJ = 16;
      }

      if (GameCanvas.isTouch) {
         if (this.AW) {
            AE = 32;
            AJ = 30;
         } else {
            AE = 28;
            AJ = 28;
         }

         if (AJ * 6 > AH) {
            AJ = AH / 6;
         }
      } else if (GameCanvas.isSmallScreen) {
         AE = 20;
      } else {
         AE = 24;
      }

      if ((MainTabShop.BW = (AG - 60) / AE) > 6) {
         MainTabShop.BW = 6;
      }

      if ((MainTabShop.BX = (AH - (this.AS << 1)) / AE) > 6) {
         MainTabShop.BX = 6;
      }

      xTab = MotherCanvas.hw - AG / 2;
      AI = MotherCanvas.hh + 25 - GameCanvas.hCommand / 2 - AH / 2;
      if (GameCanvas.AV) {
         AI -= 15;
      }

   }

   public void AA(int var1) {
   }

   public void AB() {
      this.AV = false;
      if (GameCanvas.isKeyPressed()) {
         this.IdSelect = 0;
      } else {
         this.IdSelect = -1;
      }
   }

   public void paint(mGraphics var1) {
      super.paint(var1);
   }

   public void update() {
   }

   public void handleKeyPress() {
      super.handleKeyPress();
   }

   public void updatePointer() {
      super.updatePointer();
   }

   public final void AA(mGraphics var1, MainItem var2, mVector var3, byte var4, int var5, int var6, boolean var7, MainObject var8, int var9) {
      AA(var1, var2, var3, (byte)0, var5, var6, this.BN, this.BO, var7, var8, var9);
   }

   public static void AA(mGraphics var0, MainItem var1, mVector var2, byte var3, int var4, int var5, int var6, int var7, boolean var8, MainObject var9, int var10) {
      var6 = var6;
      var7 = var7;
      boolean var11 = false;
      byte var12;
      int var17;
      if (var1 != null) {
         var6 = var1.BS;
         var7 = var1.BT - var1.CO;
         var17 = var1.CO;
         var12 = 40;
         if (var1.BE >= 0) {
            var12 = 100;
         }

         if (var17 > 0) {
            if (BQ >= var12) {
               ++BP;
            } else if (BQ >= 0) {
               BP = 0;
            }

            if ((BP << 1) / 3 > var17 + GameCanvas.hText / 2) {
               if (BQ > 0) {
                  BQ = -70;
               } else if (BQ >= -10) {
                  BP = 0;
                  BQ = 0;
               }
            }

            ++BQ;
         } else {
            BP = -1;
            BQ = 0;
         }
      } else {
         BP = -1;
         BQ = 0;
      }

      AvMain.paintRect(var0, var4 - 2, var5 - 2, var6 + 4, var7 + 4, (byte)1, 6);
      var12 = 0;
      var17 = 0;
      if (var1.AY == 1) {
         var17 = 4;
         var12 = 1;
      }

      if (var1.BP > 0 && var1.BP <= 5) {
         if (var1.AY == 1) {
            var17 = 0;
            var12 = 2;
         } else {
            var17 = -4;
            var12 = 1;
         }
      }

      if (var8) {
         AvMain.AA(var0, var1.namepaint + " " + T.gohome + "." + var1.Lv_RQ, var4 + var6 / 2 + var17, var5, 2, (byte)var1.colorName);
      } else if ((var1.BD != 1 || var1.BE < 0) && var1.colorName != 9) {
         AvMain.AA(var0, var1.namepaint, var4 + var6 / 2 + var17, var5, 2, (byte)var1.colorName);
      } else {
         AvMain.AA(var0, var1.namepaint, var4 + var6 / 2 + var17, var5, 2, (int)7);
      }

      int var14 = 0;
      if (var12 > 0) {
         var14 = mFont.tahoma_7b_white.getWidth(var1.namepaint);
         if (var12 == 2) {
            var17 = 8;
         } else {
            var17 = CRes.abs(var17);
         }
      }

      if (var1.AY == 1) {
         var0.drawRegion((mImage)AvMain.imgInfoLock, var4 + var6 / 2 - var14 / 2 - var17, var5 + 5, 3);
      }

      if (var1.BP > 0 && var1.BP <= 5) {
         var0.drawRegion(AvMain.imgInfoClass, 0, (var1.BP - 1) * 15, 15, 15, 0, var4 + var6 / 2 + var14 / 2 + var17, var5 + 5, 3);
      }

      int var13;
      if (var3 == 1) {
         var13 = var5 + AE;
         var7 -= AE;
      } else {
         var13 = var5 + GameCanvas.hText;
         var7 -= GameCanvas.hText;
      }

      if (var1.BD == 1) {
         var0.drawRegion((mImage)AvMain.imgInfoStar, var4 + var6 / 2, var13 + 4, 3);
         var13 += 14;
         var7 -= 14;
      }

      if (var1.BH != null && var1.AX > 0) {
         var5 = (var1.AX - 1) * 22 / 2;

         for(var14 = 0; var14 < var1.AX; ++var14) {
            if (var14 >= var1.AX - var1.AZ) {
               var0.drawRegion(AvMain.imgDaKham, 0, 20, 20, 20, 0, var4 + var6 / 2 + var14 * 22 - var5, var13 + 10, 3);
            } else {
               var0.drawRegion(AvMain.imgDaKham, 0, 0, 20, 20, 0, var4 + var6 / 2 + var14 * 22 - var5, var13 + 10, 3);
            }

            Potion var19;
            if (var1.BH != null && var14 <= var1.BH.length - 1 && (var19 = Potion.AD(var1.BH[var14])).CR) {
               var19.AA(var0, var4 + var6 / 2 + var14 * 22 - var5, var13 + 10, 20);
            }
         }

         var13 += 24;
         var7 -= 22;
      }

      if (BP >= 0) {
         var0.AD(var4, var13 - 2, var6, var7);
         mGraphics.AC();
         mGraphics.AD();
         var0.translate(0, -((BP << 1) / 3));
      }

      for(var5 = 0; var5 < var1.vecInfo.size(); ++var5) {
         Class_FB var15 = (Class_FB)var1.vecInfo.elementAt(var5);
         byte var20 = 0;
         MainImage var16;
         if (var1.BE > 0 && var5 == 0 && (var16 = ObjectData.getImageAll((short)(var1.BE + 400), ObjectData.hashImageSkillSmall, (short)4500)) != null && var16.img != null) {
            var0.drawRegion((mImage)var16.img, var4 + 6, var13 + 5, 3);
            var20 = 12;
         }

         if (var1.typeObject == 105 && var1.LvUpgrade > 0 && var5 == var1.vecInfo.size() - 2) {
            AvMain.AB(5).drawString(var0, var15.AA(), var4 + var20 + 4, var13, 0);
         } else if (var15.AC >= Class_FB.AJ) {
            if (var9 != null && var9.MY + 100 >= var15.AC) {
               AvMain.AB(1).drawString(var0, var15.AA(), var4 + var20 + 4, var13, 0);
            } else {
               AvMain.AB(var15.AB).drawString(var0, var15.AA(), var4 + var20 + 4, var13, 0);
            }
         } else if (var15.AC == Class_FB.AG) {
            AvMain.AB(var15.AB).drawString(var0, var15.AA(), var4 + var20 + var6 / 2, var13, 2);
         } else if (var15.AC == Class_FB.AH) {
            var7 = 0;
            if (var10 > 0) {
               var7 = var10;
            } else if (var1 != null) {
               var7 = var1.LvUpgrade;
            }

            AvMain.AB(var15.AB).drawString(var0, var15.AA() + "+" + var7 + " ---", var4 + var20 + var6 / 2, var13, 2);
         } else if (var15.AC == Class_FB.AI) {
            var7 = 0;
            if (var10 > 0) {
               var7 = var10;
            } else if (var1 != null) {
               var7 = var1.LvUpgrade;
            }

            String var18;
            if (var15.AD < 0) {
               var18 = var15.AA;
            } else {
               var18 = MainItem.AA((short)var15.AD, var15.AE, (int)var7, (int)var1.LvUpgrade);
            }

            AvMain.AB(var15.AB).drawString(var0, var18, var4 + var20 + 4, var13, 0);
            if (var2 != null && var5 < var2.size()) {
               var15 = (Class_FB)var2.elementAt(var5);
               var7 = AvMain.AB(6).getWidth(var18) + 7;
               AvMain.AB(var15.AB).drawString(var0, var15.AA, var4 + var7, var13, 0);
            }
         } else {
            AvMain.AB(var15.AB).drawString(var0, var15.AA(), var4 + var20 + 4, var13, 0);
         }

         var13 += GameCanvas.hText;
      }

      if (BP >= 0) {
         mGraphics.AE();
         mGraphics.restoreCanvas();
         GameCanvas.resetTrans(var0);
      }

   }

   public static void AA(mGraphics var0, int var1, boolean var2, byte var3) {
      if (GameCanvas.AV && MainTabShop.mainTab.BS != 118) {
         int var10001 = MotherCanvas.hw - 160;
         int var10002 = 3 + GameScreen.h12plus;
         mFont var10003 = mFont.tahoma_7_black;
         Interface_Game.AA(var0, var10001, var10002);
      } else {
         Interface_Game.paintInfoPlayer(var0, 3, 3 + GameScreen.h12plus, true, mFont.tahoma_7_black);
         AA(var0, MotherCanvas.w - 78, 4 + GameScreen.h12plus, var2);
      }

      if (var3 == 0) {
         AA(var0, var1, AI, AG, AH);
      } else {
         AvMain.AB(var0, var1, AI, AG, AH, 0);
      }
   }

   public static void AA(mGraphics var0, int var1, int var2, int var3, int var4) {
      var0.setColor(BS);
      var0.fillRect(var1 + 9, var2, var3 - 18, var4);

      for(int var5 = 0; var5 < var4 - 39; var5 += 40) {
         var0.drawRegion(mImgTab[0], 0, 0, 20, 40, 2, var1, var2 + var5, 0);
         var0.drawRegion((mImage)mImgTab[0], var1 + var3, var2 + var5, 24);
      }

      if (var4 % 40 != 0) {
         var0.drawRegion(mImgTab[0], 0, 0, 20, var4 % 40, 2, var1, var2 + var4 - var4 % 40, 0);
         var0.drawRegion(mImgTab[0], 0, 0, 20, var4 % 40, 0, var1 + var3, var2 + var4 - var4 % 40, 24);
      }

      var0.setColor(BR);
      var0.fillRect(var1 + 9, var2 - 1, var3 - 18, 1);
      var0.fillRect(var1 + 9, var2 + var4, var3 - 18, 1);
      var0.drawRegion(mImgTab[1], 0, 0, 9, 3, 3, var1, var2 - 3, 0);
      var0.drawRegion(mImgTab[1], 0, 0, 9, 3, 1, var1 + var3, var2 - 3, 24);
      var0.drawRegion(mImgTab[1], 0, 0, 9, 3, 2, var1, var2 + var4, 0);
      var0.drawRegion(mImgTab[1], 0, 0, 9, 3, 0, var1 + var3, var2 + var4, 24);
   }

   public static void AA(mGraphics var0, int var1, mVector var2, int var3) {
      var1 += 22;
      int var4;
      if ((var4 = AI + 36) + var2.size() * AJ > AI + AH + AJ / 2) {
         var4 = AI + AH / 2 - var2.size() * AJ / 2 + AJ / 2;
      }

      var0.setColor(-2573687);
      MainScreen var10000 = GameCanvas.currentScreen;
      if (MainScreen.AD((byte)1)) {
         var0.setColor(-805042);
      }

      var0.fillRoundRectNew(var1 + (AG - 22) / 2 - AG / 4 * 3 / 2, AI + 7, AG / 4 * 3, 16, 4, 4);
      if (var2 != null) {
         for(int var5 = 0; var5 < var2.size(); ++var5) {
            MainTab var6 = (MainTab)var2.elementAt(var5);
            short var7 = (short)(200 + var6.indexIconTab);
            if (GameCanvas.isSmallScreen) {
               var7 = (short)(260 + var6.indexIconTab);
            }

            MainImage var8;
            if ((var8 = ObjectData.getImageAll(var7, ObjectData.hashImageItemOther, (short)9000)) != null && var8.img != null) {
               var0.drawRegion((mImage)var8.img, var1, var4 + var5 * AJ, 3);
            }

            if (var3 == var5) {
               var10000 = GameCanvas.currentScreen;
               if ((MainScreen.AD((byte)1) || BL % 16 < 7) && (var8 = ObjectData.getImageAll((short)(230 + var6.indexIconTab), ObjectData.hashImageItemOther, (short)9000)) != null && var8.img != null) {
                  var0.drawRegion((mImage)var8.img, var1, var4 + var5 * AJ, 3);
               }

               if (var6.indexIconTab != 2) {
                  var10000 = GameCanvas.currentScreen;
                  if (MainScreen.AD((byte)1)) {
                     AvMain.FontBorderColor(var0, var6.nameTab, var1 + (AG - 22) / 2, AI + 9, 2, (int)6, (int)5);
                  } else {
                     mFont.tahoma_7b_black.drawString(var0, var6.nameTab, var1 + (AG - 22) / 2, AI + 9, 2);
                  }
               }
            }

            if ((var6.indexIconTab == 2 && Player.AS > 0 || var6.indexIconTab == 3 && Player.isSkillready || var6.indexIconTab == 4 && TabQuest.BO || var6.indexIconTab == 6 && GameScreen.numMess > 0) && GameCanvas.gameTick % 10 < 8) {
               var0.drawRegion((mImage)MainEvent.imgNew, var1 + 10, var4 - 10 + var5 * AJ, 3);
            }
         }

      }
   }

   public static void AA(mGraphics var0, int var1, int var2, boolean var3) {
      AvMain.paintRect(var0, var1, var2, 74, 54, (byte)1, 2);
      var0.setColor(-8690870);
      AvMain.fraMoney.drawFrame(0, var1 + 13, var2 + 7, 0, 3, var0);
      if (var3) {
         if (GameScreen.player.clan != null) {
            mFont.tahoma_7_yellow.drawString(var0, AvMain.AA((long)GameScreen.player.clan.AN), var1 + 20, var2 + 2, 0);
         }
      } else {
         mFont.tahoma_7_yellow.drawString(var0, AvMain.AA(Player.SN), var1 + 20, var2 + 2, 0);
      }

      int var4 = var2 + 13;
      var0.fillRect(var1 + 5, var4, 64, 1);
      AvMain.fraMoney.drawFrame(1, var1 + 13, var4 + 7, 0, 3, var0);
      if (var3) {
         if (GameScreen.player.clan != null) {
            mFont.tahoma_7_yellow.drawString(var0, AvMain.AA((long)GameScreen.player.clan.AM), var1 + 20, var4 + 2, 0);
         }
      } else {
         mFont.tahoma_7_red.drawString(var0, AvMain.AA((long)GameScreen.player.Ruby), var1 + 20, var4 + 2, 0);
      }

      var4 += 13;
      var0.fillRect(var1 + 5, var4, 64, 1);
      if (!var3) {
         AvMain.fraMoney.drawFrame(7, var1 + 12, var4 + 7, 0, 3, var0);
         mFont.tahoma_7_green.drawString(var0, AvMain.AA((long)GameScreen.player.PD), var1 + 20, var4 + 2, 0);
         var4 += 13;
         var0.fillRect(var1 + 5, var4, 64, 1);
         AvMain.fraMoney.drawFrame(9, var1 + 12, var4 + 7, 0, 3, var0);
         mFont.tahoma_7_yellow.drawString(var0, AvMain.AA((long)GameScreen.player.PG), var1 + 20, var4 + 2, 0);
      }

   }

   public static void AA(mGraphics var0, int var1, int var2) {
      AvMain.paintRect(var0, var1, var2, 74, 54, (byte)1, 2);
      var0.setColor(-8690870);
      AvMain.fraMoney.drawFrame(0, var1 + 13, var2 + 7, 0, 3, var0);
      mFont.tahoma_7_yellow.drawString(var0, AvMain.AA(Player.SN), var1 + 20, var2 + 2, 0);
      int var3 = var2 + 13;
      var0.fillRect(var1 + 5, var3, 64, 1);
      AvMain.fraMoney.drawFrame(1, var1 + 13, var3 + 7, 0, 3, var0);
      mFont.tahoma_7_red.drawString(var0, AvMain.AA((long)GameScreen.player.Ruby), var1 + 20, var3 + 2, 0);
      var3 += 13;
      var0.fillRect(var1 + 5, var3, 64, 1);
      AvMain.fraMoney.drawFrame(7, var1 + 12, var3 + 7, 0, 3, var0);
      mFont.tahoma_7_green.drawString(var0, AvMain.AA((long)GameScreen.player.PD), var1 + 20, var3 + 2, 0);
      var3 += 13;
      var0.fillRect(var1 + 5, var3, 64, 1);
      AvMain.fraMoney.drawFrame(8, var1 + 12, var3 + 7, 0, 3, var0);
      mFont.tahoma_7_orange.drawString(var0, AvMain.AA((long)GameScreen.player.PF), var1 + 20, var3 + 2, 0);
   }

   public void AF() {
      if (!this.AV) {
         ++this.AX;
         if (this.AX >= AY) {
            this.AV = true;
            this.AG();
            return;
         }
      } else {
         this.AX = 0;
      }

   }

   public void AG() {
   }

   public final void AA(MainItem var1, int var2, int var3) {
      int var4 = this.BN;
      int var5 = this.BO;
      if (var1 != null) {
         var4 = var1.BS;
         var5 = var1.BT - var1.CO;
      }

      this.AL = var2 - var4 / 2;
      if (this.AL + var4 > MotherCanvas.w - 4) {
         this.AL = MotherCanvas.w - var4 - 4;
      }

      if (this.AL < 8) {
         this.AL = 8;
      }

      this.AM = var3;
      if (this.AM < AI + GameCanvas.hCommand) {
         this.AM = AI + GameCanvas.hCommand;
      }

      if (this.AM + var5 > MotherCanvas.h - GameCanvas.hCommand - 8) {
         this.AM = MotherCanvas.h - GameCanvas.hCommand - var5 - 8;
      }

   }

   public void AH() {
   }

   public void AA(short var1, byte var2) {
   }

   public void AI() {
   }

   public void AJ() {
   }

   public iCommand setCmdEndInfo() {
      return null;
   }

   public void AA(mVector var1) {
   }
}
