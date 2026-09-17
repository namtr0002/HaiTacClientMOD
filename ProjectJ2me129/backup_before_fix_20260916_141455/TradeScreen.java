public final class TradeScreen extends ScreenUpgrade {
   public MainObject AA;
   public MainObject CQ;
   private int[][] CS;
   private iCommand CT;
   private iCommand CU;
   private iCommand CV;
   private iCommand CW;
   private iCommand CX;
   private iCommand CY;
   private InputDialog CZ;
   private int IL;
   private int IM;
   public static TradeScreen instance;

   public TradeScreen(byte var1, int var2) {
      super((byte)6, 1);
   }

   public final void AA(byte var1, int var2) {
      if (super.AD << 3 > super.AB - 30) {
         super.AD = (super.AB - 30) / 8;
      }

      super.AK -= super.AK % super.AD;
      this.IL = super.AE + super.AK + (super.AB - super.AK) / 2 - (super.AD + 2 << 1) + 5;
      this.IM = super.AJ;
      this.CS = mSystem.new_M_Int(4, 2);
      this.CS[0][0] = this.IL;
      this.CS[0][1] = this.IM;
      this.CS[1][0] = this.IL;
      this.CS[1][1] = this.IM + 18 + super.AD + 1;
      this.CS[2][0] = this.IL;
      this.CS[2][1] = this.IM + 18 + super.AD + 13;
      this.CS[3][0] = this.IL;
      this.CS[3][1] = this.IM + 18 + super.AD + 15 + super.AD + 18;
      super.AN = mSystem.new_M_Int(8, 2);

      int var3;
      for(var3 = 0; var3 < super.AN.length; ++var3) {
         super.AN[var3][0] = this.IL + var3 % 4 * (super.AD + 2);
         super.AN[var3][1] = this.IM + 15 + var3 / 4 * (30 + super.AD);
      }

      super.AM = super.AK / super.AD;
      var3 = ((Player.maxInventory - 1) / super.AM + 1) * super.AD - super.AL;
      super.AP = new ListNew(super.AI, super.AJ, super.AK, super.AL, 0, 0, var3, true);
      this.AA = new MainObject();
      this.AA.name = GameScreen.player.name;
      if (this.AA.name.length() > 12) {
         this.AA.name = GameScreen.player.name.substring(0, 8) + "...";
      }

      this.CQ = new MainObject();
      this.CZ = new InputDialog();
      super.AS = new iCommand(T.GN, 0, this);
      this.CU = new iCommand(T.strlock, 1, this);
      this.CT = new iCommand(T.tradeMoney, 2, this);
      this.CV = new iCommand(T.trade, 4, this);
      this.CX = new iCommand(T.CD, 5, this);
      this.CW = new iCommand(T.CP, 6, this);
      this.CY = new iCommand(T.AU, 8, this);
      super.AT = new iCommand(T.close, -1, this);
      if (GameCanvas.isTouch) {
         super.AT.setPos(super.AE + super.AB - 15, super.AF - 15 + 10 + 8, MainTab.fraCloseTab, "");
         this.CU.setPos(super.AE + super.AK + (super.AB - super.AK) / 2 - 4 + iCommand.wButtonCmd / 2 + 7, super.AF + super.AC - iCommand.hButtonCmdNor / 2 - 5, (FrameImage)null, this.CU.caption);
         this.CT.setPos(super.AE + super.AK + (super.AB - super.AK) / 2 - iCommand.wButtonCmd / 2 + 4 + 7, super.AF + super.AC - iCommand.hButtonCmdNor / 2 - 5, (FrameImage)null, this.CT.caption);
         super.AQ.addElement(this.CU);
         super.AQ.addElement(this.CT);
         this.CX.setPos(super.AE + super.AK + (super.AB - super.AK) / 2 + iCommand.wButtonCmd / 2 - 4, super.AF + super.AC - iCommand.hButtonCmdNor / 2 - 5, (FrameImage)null, this.CX.caption);
         this.CV.setPos(super.AE + super.AK + (super.AB - super.AK) / 2 - iCommand.wButtonCmd / 2 + 4 + 5, super.AF + super.AC - iCommand.hButtonCmdNor / 2 - 5, (FrameImage)null, this.CV.caption);
         var3 = mFont.tahoma_7b_black.getWidth(this.AA.name);
         this.CW.AA(this.IL + var3 + 13, this.IM + 4, Interface_Game.imgOther[1], "", 2);
         super.AQ.addElement(this.CW);
         this.AX();
      }

      ScreenUpgrade.mItemUpgrade = new MainItem[super.AN.length];
      super.BA = T.trade;
      if (!GameCanvas.isTouch) {
         super.DB = super.AT;
         super.DA = super.AS;
      }

      super.DE = super.AS;
      super.DF = this.CY;
      super.backCMD = super.AT;
   }

   private void AX() {
      for(int var1 = 0; var1 < super.AQ.size(); ++var1) {
         ((iCommand)super.AQ.elementAt(var1)).AH = 1;
      }

   }

   public final void AA(String var1) {
      this.CQ.name = var1;
      super.BE = 0;
      if (GameCanvas.isKeyPressed()) {
         super.AH = 0;
      } else {
         super.AH = -1;
      }

      if (super.AH >= 0) {
         this.AA(this.getMenuActionItem());
      }

   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
         } else {
            GameCanvas.gameScr.Show();
         }

         GlobalService.getInstance().AA((byte)5, (short)0, (byte)0, 1, "");
         return;
      case 0:
         if (super.AR != null) {
            if (super.AR.typeObject != 3 && !this.AA(super.AR)) {
               this.CZ.AA(T.nhapsoluongmuongiaodich, new iCommand(T.strconfirm, 3, 1, this), true, T.FM);
               GameCanvas.currentDialog = this.CZ;
               return;
            }

            GlobalService.getInstance().AA((byte)1, super.AR.ID, super.AR.typeObject, 1, "");
            return;
         }
         break;
      case 1:
         GlobalService.getInstance().AA((byte)3, (short)0, (byte)0, 1, "");
         return;
      case 2:
         this.CZ.AA(T.nhapsotien, new iCommand(T.strconfirm, 3, 0, this), true, T.tradeMoney);
         GameCanvas.currentDialog = this.CZ;
         return;
      case 3:
         boolean var5 = false;

         try {
            if ((var1 = Integer.parseInt(this.CZ.tfInput.getText())) < 0) {
               var1 = 0;
            }
         } catch (Exception var3) {
            var1 = 0;
         }

         if (var2 == 0) {
            GlobalService.getInstance().AA((byte)1, (short)0, (byte)6, var1, "");
         } else if (var2 == 1) {
            GlobalService.getInstance().AA((byte)1, super.AR.ID, super.AR.typeObject, var1, "");
         }

         GameCanvas.end_Dialog();
         this.CZ = new InputDialog();
         return;
      case 4:
         GlobalService.getInstance().AA((byte)4, (short)0, (byte)0, 1, "");
         return;
      case 5:
         GlobalService.getInstance().AA((byte)5, (short)0, (byte)0, 1, "");
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         return;
      case 6:
         this.CZ.AA(T.GJ, new iCommand(T.AD, 7, this), false, T.CP);
         GameCanvas.currentDialog = this.CZ;
         return;
      case 7:
         String var4;
         if ((var4 = this.CZ.tfInput.getText()) != null && var4.length() > 0) {
            GlobalService.getInstance().AA((byte)2, (short)0, (byte)0, 1, var4);
            this.AA.BC = var4;
         }

         GameCanvas.end_Dialog();
         this.CZ = new InputDialog();
         return;
      case 8:
         mVector var6 = new mVector();
         this.CW.caption = T.CP;
         var6.addElement(this.CW);
         if (this.AA.PB == 0) {
            var6.addElement(this.CT);
            var6.addElement(this.CU);
         } else if (this.AA.PB == 1) {
            var6.addElement(this.CV);
            var6.addElement(this.CX);
         } else if (this.AA.PB == 2) {
            var6.addElement(this.CX);
         }

         GameCanvas.menu.startAt(var6, 2, T.CO);
      }

   }

   public final void AE(mGraphics var1) {
      mFont.tahoma_7b_black.drawString(var1, this.AA.name, this.CS[0][0], this.CS[0][1], 0);
      AvMain.fraMoney.drawFrame(0, this.CS[1][0] + AvMain.fraMoney.frameWidth / 2, this.CS[1][1] + 5, 0, 3, var1);
      mFont.tahoma_7_black.drawString(var1, AvMain.AA(this.AA.Beri), this.CS[1][0] + AvMain.fraMoney.frameWidth + 1, this.CS[1][1], 0);
      mFont.tahoma_7b_black.drawString(var1, this.CQ.name, this.CS[2][0], this.CS[2][1], 0);
      AvMain.fraMoney.drawFrame(0, this.CS[3][0] + AvMain.fraMoney.frameWidth / 2, this.CS[3][1] + 5, 0, 3, var1);
      mFont.tahoma_7_black.drawString(var1, AvMain.AA(this.CQ.Beri), this.CS[3][0] + AvMain.fraMoney.frameWidth + 1, this.CS[3][1], 0);
      if (this.AA.PB > 0) {
         var1.setColor(-65536);
         var1.drawRect(this.IL - 2, this.IM - 2 + 15, (super.AD + 2 << 2) + 4, super.AD + 4);
      }

      if (this.AA.PB == 2) {
         var1.setColor(-16776961);
         var1.drawRect(this.IL - 3, this.IM - 3 + 15, (super.AD + 2 << 2) + 6, super.AD + 6);
      }

      if (this.CQ.PB > 0) {
         var1.setColor(-65536);
         var1.drawRect(this.IL - 2, this.IM - 2 + 45 + super.AD, (super.AD + 2 << 2) + 4, super.AD + 4);
      }

      if (this.CQ.PB == 2) {
         var1.setColor(-16776961);
         var1.drawRect(this.IL - 3, this.IM - 3 + 45 + super.AD, (super.AD + 2 << 2) + 6, super.AD + 6);
      }

   }

   public final void AH(mGraphics var1) {
      int var2;
      for(var2 = 0; var2 < super.AN.length; ++var2) {
         AvMain.paintRect(var1, super.AN[var2][0], super.AN[var2][1], super.AD, super.AD, (byte)1, 3);
      }

      MainItem var3;
      for(var2 = 0; var2 < this.AA.vecTrade.size(); ++var2) {
         if ((var3 = (MainItem)this.AA.vecTrade.elementAt(var2)).typeObject == 3) {
            var3.AC(var1, super.AN[var2][0] + super.AD / 2, super.AN[var2][1] + super.AD / 2, super.AD);
         }

         var3.paint(var1, super.AN[var2][0] + super.AD / 2, super.AN[var2][1] + super.AD / 2, super.AD);
      }

      for(var2 = 0; var2 < this.CQ.vecTrade.size(); ++var2) {
         if ((var3 = (MainItem)this.CQ.vecTrade.elementAt(var2)).typeObject == 3) {
            var3.AC(var1, super.AN[var2 + 4][0] + super.AD / 2, super.AN[var2 + 4][1] + super.AD / 2, super.AD);
         }

         var3.paint(var1, super.AN[var2 + 4][0] + super.AD / 2, super.AN[var2 + 4][1] + super.AD / 2, super.AD);
      }

      if (super.BE == 1) {
         for(var2 = 0; var2 < super.AN.length; ++var2) {
            if (super.AH == var2) {
               var1.setColor(-1);
               var1.drawRect(super.AN[var2][0] + 1, super.AN[var2][1] + 1, super.AD - 2, super.AD - 2);
               if (!GameCanvas.isSmallScreen) {
                  var1.drawRect(super.AN[var2][0] + 2, super.AN[var2][1] + 2, super.AD - 4, super.AD - 4);
               }
            }
         }
      }

   }

   public final void AF(mGraphics var1) {
      if (this.AA.LS != null) {
         this.AA.LS.paint(var1);
      }

      if (this.CQ.LS != null) {
         this.CQ.LS.paint(var1);
      }

   }

   public final void AA(mGraphics var1, int var2, int var3, MainItem var4) {
      if (this.AA(var4)) {
         if (AvMain.DV == null) {
            AvMain.DV = mImage.createImage("/interface/icontrade2.png");
            return;
         }

         var1.drawRegion((mImage)AvMain.DV, var2, var3, 36);
      }

   }

   public final void update() {
      super.update();
      this.AA(this.AA);
      this.AA(this.CQ);
      if (GameCanvas.isTouch && !GameCanvas.menu.isShowMenu && this.CW.caption.length() > 0) {
         this.CW.caption = "";
      }

   }

   public final void handleKeyPress() {
      boolean var1 = false;
      if (super.BE == 0) {
         if (GameCanvas.isKeyPressed(0)) {
            --super.AH;
            GameCanvas.clearKeyPressed(0);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(2)) {
            if (super.AH % super.AM != super.AM - 1 && super.AH < Player.vecInventory.size() - 1) {
               ++super.AH;
            } else {
               super.BE = 1;
               super.AH = 0;
            }

            GameCanvas.clearKeyPressed(2);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(1)) {
            if (super.AH >= super.AM) {
               super.AH -= super.AM;
            }

            GameCanvas.clearKeyPressed(1);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(3)) {
            if (super.AH < Player.vecInventory.size() - super.AM) {
               super.AH += super.AM;
            }

            GameCanvas.clearKeyPressed(3);
            var1 = true;
         }
      } else if (super.BE == 1) {
         if (GameCanvas.isKeyPressed(0)) {
            if (super.AH % 4 == 0) {
               super.BE = 0;
               super.AH = 0;
            } else {
               --super.AH;
            }

            GameCanvas.clearKeyPressed(0);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(2)) {
            if (super.AH % 4 < 3) {
               ++super.AH;
               var1 = true;
            }

            GameCanvas.clearKeyPressed(2);
         } else if (GameCanvas.isKeyPressed(1)) {
            if (super.AH >= 4) {
               super.AH -= 4;
            }

            GameCanvas.clearKeyPressed(1);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(3)) {
            if (super.AH < 4) {
               super.AH += 4;
            }

            GameCanvas.clearKeyPressed(3);
            var1 = true;
         }
      }

      if (var1) {
         this.AH();
      }

      super.handleKeyPress();
      this.AS();
   }

   public final void updatePointer() {
      if (GameCanvas.AB(super.AI, super.AJ, super.AK, super.AL)) {
         super.BE = 0;
      } else {
         for(int var1 = 0; var1 < super.AN.length; ++var1) {
            if (GameCanvas.AB(super.AN[var1][0], super.AN[var1][1], super.AD, super.AD)) {
               super.BE = 1;
               super.AH = var1;
               this.AH();
               break;
            }
         }
      }

      super.updatePointer();
   }

   public final void AH() {
      super.AO = false;
      if (super.BE == 0) {
         super.AH = AvMain.AA(super.AH, Player.vecInventory.size() - 1, false);
         if (!GameCanvas.isTouch) {
            super.AP.AA((super.AH / super.AM + 1) * super.AD - super.AC / 2);
         }
      }

      if (super.AH >= 0) {
         this.AA(this.getMenuActionItem());
      }

   }

   public final mVector getMenuActionItem() {
      mVector var1 = null;
      MainItem var2;
      if (super.BE == 0) {
         if ((var2 = (MainItem)Player.vecInventory.elementAt(super.AH)) != null) {
            super.AR = var2;
         }

         if (super.AR != null) {
            super.AS.caption = T.GN;
            if (this.AA(super.AR)) {
               super.AS.caption = T.GM;
            }

            MainItem var10000 = super.AR;
            var1 = MainItem.AH();
         }
      } else if (super.BE == 1) {
         super.AR = null;
         if (super.AH < 4) {
            if (super.AH < this.AA.vecTrade.size() && (var2 = (MainItem)this.AA.vecTrade.elementAt(super.AH)) != null) {
               super.AR = var2;
            }
         } else if (super.AH < 8 && super.AH - 4 < this.CQ.vecTrade.size() && (var2 = (MainItem)this.CQ.vecTrade.elementAt(super.AH - 4)) != null) {
            super.AR = var2;
         }
      }

      return var1;
   }

   public final void AA(mVector var1) {
      super.DA = null;
      super.center = null;
      super.AQ.removeAllElements();
      if (var1 != null) {
         int var2;
         iCommand var3;
         if (GameCanvas.isTouch) {
            super.AQ = var1;

            for(var2 = 0; var2 < super.AQ.size(); ++var2) {
               var3 = (iCommand)super.AQ.elementAt(var2);
               if (var2 == 0) {
                  var3 = AvMain.AA(var3, 1);
               }

               if (var2 == 1) {
                  AvMain.AA(var3, 2);
               }
            }
         } else {
            for(var2 = 0; var2 < var1.size(); ++var2) {
               var3 = (iCommand)var1.elementAt(var2);
               if (var2 == 0) {
                  super.center = var3;
               }

               if (var2 == 1) {
                  super.DA = var3;
               }
            }
         }
      }

      if (GameCanvas.isTouch) {
         super.AQ.addElement(super.AT);
         super.AQ.addElement(this.CW);
         if (this.AA.PB == 0) {
            super.AQ.addElement(this.CU);
            super.AQ.addElement(this.CT);
         } else if (this.AA.PB == 1) {
            super.AQ.addElement(this.CV);
            super.AQ.addElement(this.CX);
         } else if (this.AA.PB == 2) {
            super.AQ.addElement(this.CX);
         }
      } else {
         super.DB = super.AT;
         super.DA = this.CY;
      }

      this.AX();
   }

   public final boolean AA(short var1, byte var2) {
      return false;
   }

   public final void AK() {
      super.AO = false;
      this.AH();
   }

   public final void setStep() {
   }

   private boolean AA(MainItem var1) {
      for(int var2 = 0; var2 < this.AA.vecTrade.size(); ++var2) {
         MainItem var3;
         if ((var3 = (MainItem)this.AA.vecTrade.elementAt(var2)).ID == var1.ID && var3.typeObject == var1.typeObject) {
            return true;
         }
      }

      return false;
   }

   public final void AA(byte var1, byte var2, MainItem var3) {
      MainObject var4;
      if (var1 == 0) {
         var4 = this.AA;
      } else {
         var4 = this.CQ;
      }

      if (var2 == 0) {
         for(int var6 = 0; var6 < var4.vecTrade.size(); ++var6) {
            MainItem var5;
            if ((var5 = (MainItem)var4.vecTrade.elementAt(var6)).ID == var3.ID && var5.typeObject == var3.typeObject) {
               var4.vecTrade.removeElement(var5);
               --var6;
            }
         }
      } else if (var2 == 1) {
         var4.vecTrade.addElement(var3);
      }

      if (var1 == 0) {
         this.AK();
      }

   }

   public final void AV() {
      for(int var1 = 0; var1 < super.AQ.size(); ++var1) {
         iCommand var2;
         if ((var2 = (iCommand)super.AQ.elementAt(var1)) != super.AT) {
            super.AQ.removeElement(var2);
            --var1;
         }
      }

      super.AQ.addElement(this.CV);
      super.AQ.addElement(this.CX);
      if (GameCanvas.isTouch) {
         super.AQ.addElement(this.CW);
      }

      this.AX();
   }

   public final void AW() {
      for(int var1 = 0; var1 < super.AQ.size(); ++var1) {
         iCommand var2;
         if ((var2 = (iCommand)super.AQ.elementAt(var1)) != super.AT) {
            super.AQ.removeElement(var2);
            --var1;
         }
      }

      this.CX.setPos(super.AE + super.AK + (super.AB - super.AK) / 2, super.AF + super.AC - iCommand.hButtonCmdNor / 2 - 5, (FrameImage)null, this.CX.caption);
      super.AQ.addElement(this.CX);
      if (GameCanvas.isTouch) {
         super.AQ.addElement(this.CW);
      }

      this.AX();
   }

   private void AA(MainObject var1) {
      if (var1.BC != null) {
         String var3 = var1.BC;
         if (var1.LS == null) {
            var1.LS = new PopupChat();
         }

         var1.LS.AA(var3, true);
         if (var1 == this.AA) {
            var1.LS.AB(this.IL + var1.LS.AA / 2, this.CS[0][1] - 5);
         } else {
            var1.LS.AB(this.IL + var1.LS.AA / 2, this.CS[2][1] - 5);
         }

         var1.BC = null;
      }

      if (var1.LS != null && var1.LS.AA()) {
         var1.LS = null;
      }

   }

   public final void AF() {
      if (super.BE == 0) {
         this.AA(super.AR, super.AI, super.AJ + (super.AH / super.AM + 1) * super.AD - super.AP.AC + 4);
      } else {
         if (super.BE == 1) {
            this.AA(super.AR, super.AI + super.AK - super.AR.BS, super.AJ + 15 + super.AH / 4 * (30 + super.AD) - super.AP.AC + 4);
         }

      }
   }

   public final void AA(mGraphics var1, MainItem var2, int var3, int var4) {
      if (var2.BO != 0) {
         var1.drawRegion(AvMain.imgDelay, 0, 0, super.AD, super.AD, 0, var3, var4, 3);
      }

   }
}
