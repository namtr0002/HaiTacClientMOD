public final class TabEquip extends MainTab {
   private int BP;
   private int BQ;
   private int BR;
   private int BS;
   private int BT;
   public static int BN = 10;
   private int BU;
   private int BV;
   private int BW;
   private int BX;
   private int BY;
   private int BZ = 0;
   private MainItem CA = null;
   private iCommand CB;
   private iCommand CC;
   private iCommand CD;
   public static int[][] BO;
   private int CE = 0;
   private int CF = 0;
   private byte[] CG = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8};

   public TabEquip(String var1) {
      super.nameTab = var1;
      if (GameCanvas.isTouch) {
         super.IdSelect = -1;
      } else {
         super.IdSelect = 0;
      }

      super.AN = MainTab.AJ;
      if (GameCanvas.isTouch) {
         if (super.AW) {
            super.AN = 32;
         } else {
            super.AN = 28;
         }
      }

      this.BP = super.AR / 3;
      this.BQ = super.AQ / 5;
      this.CE = GameCanvas.hText;
      int var2 = super.AS;
      if (super.AN > super.AR / 6 + 1) {
         super.AN = super.AR / 6 + 1;
      }

      if (GameCanvas.isTouch && super.AN < 23 && (!GameCanvas.isIos() || MotherCanvas.h >= 240)) {
         super.AN = 23;
         this.CF = 3;
      }

      if (super.AN % 2 == 0) {
         --super.AN;
      }

      this.BY = GameCanvas.hText;
      if (GameCanvas.isSmallScreen) {
         var2 = 2;
         this.BY = GameCanvas.hText - 4;
         this.CF = 2;
      }

      this.BW = super.AO + var2;
      this.BX = super.AP + (this.BP << 1) + var2;
      BN = 10;
      this.BR = this.BP / this.BY;
      this.BT = (Player.RQ.length - 1) / this.BR + 1;
      if (this.BT <= 0) {
         this.BT = 1;
      }

      this.BS = 2;
      if ((var2 = BN) > 8) {
         var2 = 8;
      }

      this.BU = super.AP + ((this.BP << 1) - ((var2 + 1) / 2 - 1) * super.AN) / 2 - 2;
      this.BV = super.AO + this.BQ / 2 - super.AN / 2;
      super.indexIconTab = 1;
      BO = mSystem.new_M_Int(BN, 2);

      for(var2 = 0; var2 < BO.length; ++var2) {
         if (var2 == 8) {
            BO[var2][0] = this.BV + var2 % 2 * ((this.BQ << 2) + 3) + super.AN * 5 / 4;
            BO[var2][1] = this.BU - super.AN / 2 + var2 / 2 * super.AN - super.AN * 5 / 4 - this.CF;
         } else if (var2 == 9) {
            BO[var2][0] = this.BV + var2 % 2 * ((this.BQ << 2) + 3) - super.AN * 5 / 4;
            BO[var2][1] = this.BU - super.AN / 2 + var2 / 2 * super.AN - super.AN * 5 / 4 - this.CF;
         } else {
            BO[var2][0] = this.BV + var2 % 2 * ((this.BQ << 2) + 3);
            BO[var2][1] = this.BU - super.AN / 2 + var2 / 2 * super.AN - this.CF;
         }
      }

      this.CB = new iCommand(T.CV, 0, this);
      this.CB = AvMain.AA(this.CB, 0);
      this.CC = new iCommand(T.RC, 1, this);
      this.CC = AvMain.AA(this.CC, 1);
      this.CD = new iCommand(T.TH, 3, this);
   }

   public final void AB() {
      super.AV = false;
      if (GameCanvas.isKeyPressed()) {
         super.IdSelect = 0;
         this.CA = (MainItem)GameScreen.player.LR.get("" + super.IdSelect);
      } else {
         super.IdSelect = -1;
         this.CA = null;
      }

      super.center = this.CB;
      this.BT = (Player.RQ.length - 1) / this.BR + 1;
      if (this.BT <= 0) {
         this.BT = 1;
      }

      super.DA = this.CC;
   }

   public final void commandPointer(int var1, int var2) {
      SaveRms var10000;
      mVector var6;
      switch(var1) {
      case 0:
         if (super.IdSelect < 0) {
            return;
         } else {
            var6 = new mVector();
            byte var9 = this.CG[super.IdSelect];
            if (this.CA != null) {
               var9 = this.CA.typeEquip;
            }

            var1 = 0;

            for(; var1 < Player.vecInventory.size(); ++var1) {
               MainItem var4;
               if ((var4 = (MainItem)Player.vecInventory.elementAt(var1)).typeObject == 3 && var4.typeEquip == var9 && (var4.charClass == 0 || var4.charClass == GameScreen.player.clazz)) {
                  var6.addElement(var4);
               }
            }

            if (var6.size() == 0) {
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.CW);
               return;
            } else {
               Class_AP var7;
               (var7 = new Class_AP()).AA(var6, BO[super.IdSelect][0], BO[super.IdSelect][1] + MainTab.AE / 2, super.AN, super.IdSelect % 2 == 0 ? 2 : 0);
               GameCanvas.Start_Current_Dialog((MainDialog)var7);
               return;
            }
         }
      case 1:
         var6 = new mVector();

         for(var1 = 0; var1 < T.WT.length; ++var1) {
            iCommand var8 = new iCommand(T.WT[var1], 2, var1, this);
            var6.addElement(var8);
         }

         var6.addElement(this.CD);
         GameCanvas.menuCur.startAt(var6, 2, T.RC);
         return;
      case 2:
         for(var1 = 0; var1 < Player.QK.size(); ++var1) {
            Class_FA var3;
            if ((var3 = (Class_FA)Player.QK.elementAt(var1)).AA == var2) {
               var3.AA((byte)var2);
               Class_FA.AA(false);
               var10000 = GameCanvas.saveRms;
               SaveRms.AG();
               return;
            }
         }

         Class_FA var5;
         (var5 = new Class_FA()).AA((byte)var2);
         Player.QK.addElement(var5);
         Class_FA.AA(false);
         var10000 = GameCanvas.saveRms;
         SaveRms.AG();
         return;
      case 3:
         Player.QK.removeAllElements();
         var10000 = GameCanvas.saveRms;
         SaveRms.AG();
         Class_FA.AA(true);
      default:
      }
   }

   public final void paint(mGraphics var1) {
      byte var2 = 0;
      if (GameCanvas.isSmallScreen) {
         var2 = 3;
      }

      AvMain.paintRect(var1, super.AO + super.AQ / 2 - this.BQ * 3 / 2 - var2, super.AP + super.AS - this.CF, this.BQ * 3, (this.BP << 1) - (super.AS << 1) + this.CF, (byte)0, 3);
      int var3 = super.AO + super.AQ / 2 - this.BQ * 3 / 2 - var2 + 3 + 14;
      int var4 = super.AP + super.AS + 4 - this.CF;
      AvMain.fraMoney.drawFrame(2, var3 - 8, var4 + 5, 0, 3, var1);
      if (MainTab.BG.timeCountDown > 0 && MainTab.BM >= 60) {
         MainTab.BG.paintCountDownTicketHour(var1, mFont.tahoma_7_yellow, var3, var4, 0);
      } else {
         mFont.tahoma_7_white.drawString(var1, Player.Ticket + "/" + Player.MaxTicket, var3, var4, 0);
      }

      AvMain.fraMoney.drawFrame(4, var3 - 8, var4 + this.CE + 5, 0, 3, var1);
      if (MainTab.BH.timeCountDown > 0 && MainTab.BM >= 60) {
         MainTab.BH.paintCountDownTicketHour(var1, mFont.tahoma_7_yellow, var3, var4 + this.CE, 0);
      } else {
         mFont.tahoma_7_white.drawString(var1, Player.RH + "/" + Player.RK, var3, var4 + this.CE, 0);
      }

      AvMain.fraMoney.drawFrame(3, var3 - 8, var4 + (this.CE << 1) + 5, 0, 3, var1);
      if (MainTab.BI.timeCountDown > 0 && MainTab.BM >= 60) {
         MainTab.BI.paintCountDownTicketHour(var1, mFont.tahoma_7_yellow, var3, var4 + (this.CE << 1), 0);
      } else {
         mFont.tahoma_7_white.drawString(var1, Player.RG + "/" + Player.RJ, var3, var4 + (this.CE << 1), 0);
      }

      AvMain.fraMoney.drawFrame(6, var3 - 8, var4 + this.CE * 3 + 5, 0, 3, var1);
      if (MainTab.BJ.timeCountDown <= 0) {
         mFont.tahoma_7_white.drawString(var1, "00:00", var3, var4 + this.CE * 3, 0);
      } else {
         MainTab.BJ.paintCountDownTicketHour(var1, mFont.tahoma_7_yellow, var3, var4 + this.CE * 3, 0);
      }

      AvMain.fraMoney.drawFrame(5, var3 - 8, var4 + 5 + (this.CE << 2), 0, 3, var1);
      mFont.tahoma_7_white.drawString(var1, "" + GameScreen.player.KN, var3, var4 + (this.CE << 2), 0);

      MainScreen var10000;
      for(var3 = 0; var3 < BN; ++var3) {
         if (var3 < 8) {
            AvMain.paintRect(var1, BO[var3][0] - 1, BO[var3][1] - 1, super.AN - 2, super.AN - 2, (byte)0, 3);
         }

         MainItem var5;
         if ((var5 = (MainItem)GameScreen.player.LR.get("" + var3)) != null) {
            var5.AC(var1, BO[var3][0] + super.AN / 2 - 1, BO[var3][1] + super.AN / 2 - 1, super.AN - 3);
            var5.AB(var1, BO[var3][0] + super.AN / 2 - 1, BO[var3][1] + super.AN / 2 - 1, super.AN, 1);
         } else if (AvMain.fraEquip != null) {
            AvMain.fraEquip.drawFrame(var3, BO[var3][0] + super.AN / 2 - 1, BO[var3][1] + super.AN / 2 - 1, 0, 3, var1);
         }

         if (super.IdSelect == var3) {
            var10000 = GameCanvas.currentScreen;
            if (MainScreen.AD((byte)1)) {
               var1.setColor(-1);
               var1.drawRect(BO[var3][0], BO[var3][1], super.AN - 4, super.AN - 4);
               var1.drawRect(BO[var3][0] + 1, BO[var3][1] + 1, super.AN - 6, super.AN - 6);
            }
         }
      }

      var1.drawRegion((mImage)MainObject.imgShadow, super.AO + super.AQ / 5 * 3 + 1 - var2, super.AP + this.BP + GameScreen.player.hOne / 4, 3);
      GameScreen.player.AA(var1, super.AO + super.AQ / 5 * 3 - var2, super.AP + this.BP + GameScreen.player.hOne / 4, true);
      AvMain.paintRect(var1, super.AO, super.AP + (this.BP << 1), super.AQ, this.BP - super.AS, (byte)0, 4);
      if (GameCanvas.isSmallScreen || this.BR == 2 && !GameCanvas.isIos()) {
         for(var3 = 0; var3 < Player.RQ.length; ++var3) {
            if (GameCanvas.gameTick % 60 < 20) {
               mFont.tahoma_7_white.drawString(var1, T.WB[var3], this.BW + var3 / this.BR * super.AQ / this.BT, this.BX + var3 % this.BR * this.BY, 0);
            } else {
               mFont.tahoma_7_white.drawString(var1, Player.RQ[var3], this.BW + var3 / this.BR * super.AQ / this.BT, this.BX + var3 % this.BR * this.BY, 0);
            }
         }
      } else {
         for(var3 = 0; var3 < Player.RQ.length; ++var3) {
            mFont.tahoma_7_black.drawString(var1, T.WB[var3], this.BW + var3 / this.BR * super.AQ / this.BT, this.BX + var3 % this.BR * this.BY, 0);
            mFont.tahoma_7_white.drawString(var1, Player.RQ[var3], this.BW + var3 / this.BR * super.AQ / this.BT + 25, this.BX + var3 % this.BR * this.BY, 0);
         }
      }

      GameCanvas.resetTrans(var1);
      var10000 = GameCanvas.currentScreen;
      if (MainScreen.AD((byte)1) && !GameCanvas.menuCur.isShowMenu && GameCanvas.currentDialog == null && super.IdSelect >= 0) {
         if (super.AV && this.CA != null) {
            this.AA(var1, this.CA, (mVector)null, (byte)0, super.AL, super.AM, false, GameScreen.player, 0);
         }

         super.paint(var1);
      }

   }

   public final void update() {
      if (this.CA != null) {
         this.AF();
      }

      MainTab.BM = GameCanvas.gameTick % 120;
      MainTab.BG.updateTimeCountDownTicket();
      MainTab.BH.updateTimeCountDownTicket();
      MainTab.BI.updateTimeCountDownTicket();
      MainTab.BJ.updateTimeCountDownTicket();
   }

   public final void handleKeyPress() {
      boolean var1 = false;
      int var2 = super.IdSelect;
      if (GameCanvas.isKeyPressed(0)) {
         if (super.IdSelect == 8) {
            super.IdSelect = 6;
         } else if (super.IdSelect % this.BS == 0) {
            MainScreen var10000 = GameCanvas.currentScreen;
            MainScreen.AC((byte)0);
         } else {
            --super.IdSelect;
         }

         GameCanvas.clearKeyPressed(0);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(2)) {
         if (super.IdSelect == 9) {
            super.IdSelect = 7;
         } else {
            ++super.IdSelect;
         }

         GameCanvas.clearKeyPressed(2);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(1)) {
         if (super.IdSelect >= this.BS) {
            super.IdSelect -= this.BS;
         }

         GameCanvas.clearKeyPressed(1);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         if (super.IdSelect < BN - this.BS) {
            super.IdSelect += this.BS;
         }

         GameCanvas.clearKeyPressed(3);
         var1 = true;
      }

      if (var1) {
         super.AV = false;
         super.IdSelect = AvMain.AA(super.IdSelect, BN - 1, false);
         if (super.IdSelect == 8) {
            if ((MainItem)GameScreen.player.LR.get("" + super.IdSelect) == null) {
               if ((MainItem)GameScreen.player.LR.get("9") == null) {
                  super.IdSelect = var2;
                  return;
               }

               super.IdSelect = 9;
            }
         } else if (super.IdSelect == 9 && (MainItem)GameScreen.player.LR.get("" + super.IdSelect) == null) {
            if ((MainItem)GameScreen.player.LR.get("8") == null) {
               super.IdSelect = var2;
               return;
            }

            super.IdSelect = 8;
         }

         this.CA = (MainItem)GameScreen.player.LR.get("" + super.IdSelect);
      }

      super.handleKeyPress();
   }

   public final void updatePointer() {
      if (GameCanvas.AB(super.AO, super.AP, super.AQ, super.AR)) {
         for(int var1 = 0; var1 < BN; ++var1) {
            if (GameCanvas.AB(BO[var1][0] - 2, BO[var1][1] - 2, super.AN, super.AN)) {
               if ((var1 == 8 || var1 == 9) && (MainItem)GameScreen.player.LR.get("" + var1) == null) {
                  GameCanvas.isPointerSelect = false;
               } else {
                  if (var1 != super.IdSelect) {
                     super.IdSelect = var1;
                     this.CA = (MainItem)GameScreen.player.LR.get("" + super.IdSelect);
                     super.center = this.CB;
                     super.DF = super.center;
                     super.AV = false;
                  } else {
                     this.CB.AD();
                  }

                  GameCanvas.isPointerSelect = false;
               }
               break;
            }
         }
      }

      super.updatePointer();
   }

   public final void AG() {
      if (MotherCanvas.w >= 400) {
         this.AA(this.CA, super.AO + MainTab.AG, BO[super.IdSelect][1] + 1);
      } else if (super.IdSelect % 2 == 0) {
         this.AA(this.CA, BO[super.IdSelect][0] + super.AN + this.CA.BS / 2 + 2, BO[super.IdSelect][1] + super.AN);
      } else {
         this.AA(this.CA, BO[super.IdSelect][0] - this.CA.BS / 2 - 6, BO[super.IdSelect][1] + super.AN);
      }
   }

   public final void AH() {
      MainItem var1;
      if ((var1 = (MainItem)GameScreen.player.LR.get("" + super.IdSelect)) != null && this.CA != null && var1 != this.CA) {
         this.CA = var1;
         super.AV = false;
      }

   }
}
