public final class Class_AN extends MsgDialog {
   private Class_DS AA;
   private int AB = 120;
   private iCommand AC;
   private iCommand BB;
   private iCommand BC;
   private iCommand BD;
   private iCommand BE;
   private iCommand BF;
   private iCommand BG;
   private iCommand BH;
   private iCommand BI;
   private iCommand BJ;
   private iCommand BK;
   private iCommand BL;
   private InputDialog BM;

   public Class_AN(Class_DS var1) {
      this.AA = var1;
      if (var1 != null) {
         super.cmdList.removeAllElements();
         super.AG = new iCommand(T.close, 1, this);
         if (var1.AC.compareTo(GameScreen.player.name) != 0) {
            this.AC = new iCommand(T.CA, 2, this);
            this.BB = new iCommand(T.kickClan, 10, this);
            this.BD = new iCommand(T.strDonate, 4, this);
            this.BE = new iCommand(T.HJ, 5, this);
            this.BF = new iCommand(T.CP, 6, this);
            this.BG = new iCommand(T.CN, 7, this);
            this.BC = new iCommand(T.phongchuc, 8, this);
            super.cmdList.addElement(this.BD);
            super.cmdList.addElement(this.AC);
         } else {
            if (Player.QN == 0) {
               this.BJ = new iCommand(T.PA, 14, this);
               super.cmdList.addElement(this.BJ);
               this.BK = new iCommand(T.GI, 15, this);
               super.cmdList.addElement(this.BK);
               this.BL = new iCommand(T.UT, 17, this);
               super.cmdList.addElement(this.BL);
            } else if (Player.QN == 1) {
               this.BJ = new iCommand(T.PA, 14, this);
               super.cmdList.addElement(this.BJ);
               this.BH = new iCommand(T.clanLeave, 11, this);
               super.cmdList.addElement(this.BH);
               this.BK = new iCommand(T.GI, 15, this);
               super.cmdList.addElement(this.BK);
            } else {
               this.BH = new iCommand(T.clanLeave, 11, this);
               super.cmdList.addElement(this.BH);
            }

            this.BI = new iCommand(T.NJ, 12, this);
            super.cmdList.addElement(this.BI);
         }

         if (!GameCanvas.isTouch) {
            super.cmdList.addElement(super.AG);
         }

         super.wDia = 160;
         super.hDia = 140;
         if (Player.QN != 1 && Player.QN != 0) {
            if (!GameCanvas.isTouch) {
               super.hDia += GameCanvas.hCommand + 10;
            }
         } else {
            super.hDia += GameCanvas.hCommand + 10;
         }

         super.AX = MotherCanvas.hw - super.wDia / 2;
         super.AY = MotherCanvas.hh - super.hDia / 2;
         super.wItem = 27;
         this.setPosCmdNew(15, false);
         if (GameCanvas.isTouch) {
            super.cmdList.addElement(super.AG);
            super.AG.setPos(MotherCanvas.hw + (super.wDia - 50) / 2, super.AY - GameCanvas.hCommand / 2, MainTab.fraCloseTab, "");
         }

         super.backCMD = super.AG;
      }
   }

   public final void commandPointer(int var1, int var2) {
      GameScreen var10000;
      iCommand var3;
      mVector var5;
      switch(var1) {
      case 1:
         GameCanvas.end_Dialog();
         return;
      case 2:
         (var5 = new mVector()).addElement(this.BE);
         var5.addElement(this.BF);
         var5.addElement(this.BG);
         if (Player.QN == 0 || Player.QN == 1) {
            var5.addElement(this.BC);
            var5.addElement(this.BB);
         }

         GameCanvas.menu.startAt(var5, 2, this.AA.AC);
         return;
      case 3:
         GlobalService.getInstance().Clan_CMD((byte)1, this.AA.AC, (short)this.AA.AG, (byte)0);
         GameCanvas.end_Dialog();
         return;
      case 4:
         GlobalService.getInstance().Clan_CMD((byte)2, this.AA.AC, (short)this.AA.AG, (byte)0);
         return;
      case 5:
         var10000 = GameCanvas.gameScr;
         GameScreen.AA(this.AA.AC);
         return;
      case 6:
         var10000 = GameCanvas.gameScr;
         GameScreen.AB(this.AA.AC);
         return;
      case 7:
         GlobalService.getInstance().AA((byte)0, (int)this.AA.AG);
         return;
      case 8:
         var5 = new mVector();
         iCommand var7;
         if (Player.QN == 0) {
            var7 = new iCommand(T.thuyenpho, 13, 1, this);
            var3 = new iCommand(T.hoatieu, 13, 2, this);
            iCommand var4 = new iCommand(T.thanhvien, 13, 10, this);
            var5.addElement(var7);
            var5.addElement(var3);
            var5.addElement(var4);
         }

         if (Player.QN == 1) {
            var7 = new iCommand(T.hoatieu, 13, 2, this);
            var3 = new iCommand(T.thanhvien, 13, 10, this);
            var5.addElement(var7);
            var5.addElement(var3);
         }

         GameCanvas.menu.startAt(var5, 2, this.AA.AC);
         return;
      case 9:
         GlobalService.getInstance().Clan_CMD((byte)4, "", (short)this.AA.AG, (byte)0);
         GameCanvas.end_Dialog();
         return;
      case 10:
         GameCanvas.Start_Normal_DiaLog(T.banmuon + this.AA.AC + " " + T.ND, new iCommand(T.kickClan, 3, this), true);
         return;
      case 11:
         GameCanvas.Start_Normal_DiaLog(T.banmuon + T.ND, new iCommand(T.clanLeave, 9, this), true);
         return;
      case 12:
         GlobalService.getInstance().Clan_CMD((byte)15, "", 0, (byte)0);
         GameCanvas.end_Dialog();
         return;
      case 13:
         GlobalService.getInstance().Clan_CMD((byte)3, this.AA.AC, (short)this.AA.AG, (byte)var2);
         return;
      case 14:
         GameCanvas.end_Dialog();
         GameCanvas.CI = new TabScreen(MainTab.xTab, (byte)0);
         mVector var6 = new mVector();
         GameCanvas.CI.AF = true;
         (GameCanvas.CO = new TabInventory(T.PA, Player.AW, (byte)4, MainTab.xTab)).AQ();
         var6.addElement(GameCanvas.CO);
         GameCanvas.CI.addVecTab(var6);
         GameCanvas.CI.AE = 0;
         GameCanvas.CI.Show((MainScreen)GameCanvas.ClanScr);
         GameCanvas.CI.AD = 1;
         GameCanvas.CI.AF();
         return;
      case 15:
         if (this.BM == null) {
            this.BM = new InputDialog();
            var3 = new iCommand(T.AD, 16, this);
            this.BM.AA(T.GJ, var3, false, T.GI);
         } else {
            this.BM.tfInput.AB("");
         }

         GameCanvas.AB(this.BM);
         return;
      case 16:
         GlobalService.getInstance().World_Chanel((byte)1, (String)this.BM.tfInput.getText());
         GameCanvas.end_Dialog();
         return;
      case 17:
         GlobalService.getInstance().AJ((byte)-95, (byte)1);
      default:
      }
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = super.AY + 4;
      int var3 = super.AX + super.AU * 3;
      this.AE(var1, super.AX - 5, super.AY - 32, super.AT + 10, super.hDia + 44, super.AT + 10);
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.AX + super.wDia / 2 - this.AB / 2, super.AY - 20, this.AB, 16, 4, 4);
      AvMain.FontBorderColor(var1, T.AZ, super.AX + super.AT / 2, super.AY - 18, 2, (int)6, (int)5);
      MainObject.AA(var1, this.AA.AV, this.AA.AW, this.AA.AX, var3 + (super.AU << 1), var2 + super.AU + 45, 2);
      AvMain.FontBorderColor(var1, this.AA.AC, var3 + (super.AU << 1) + 16, var2 + super.AU / 2, 0, (int)0, (int)7);
      Class_DS var10002 = this.AA;
      mFont.tahoma_7b_black.drawString(var1, Class_DS.AA(this.AA.AS), var3 + (super.AU << 1) + 16, var2 + GameCanvas.hText - 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.capdo + ": " + this.AA.AK, var3, var2 + (GameCanvas.hText << 1) - 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.tangqua + ": " + this.AA.AL, var3, var2 + GameCanvas.hText * 3 - 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.nhiemvu + ": " + this.AA.AM, var3, var2 + (GameCanvas.hText << 2) - 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.NJ + ": " + this.AA.BA + " " + T.EW, var3, var2 + GameCanvas.hText * 5 - 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.VE + ": " + this.AA.BB + " " + T.VG, var3, var2 + GameCanvas.hText * 6 - 1, 0);
      if (super.cmdList != null) {
         for(var2 = 0; var2 < super.cmdList.size(); ++var2) {
            iCommand var4;
            (var4 = (iCommand)super.cmdList.elementAt(var2)).paint(var1, var4.xCmd, var4.yCmd);
         }
      }

   }

   public final void update() {
      if (super.isClose) {
         this.closeDialog();
      } else {
         this.updateAnimation();
         if (GameCanvas.isKeyPressed()) {
            this.handleKeyPress();
         }

         this.updatePointer();
      }
   }

   public final void handleKeyPress() {
      if (GameCanvas.isKeyPressed(0)) {
         if (super.AD > 0) {
            --super.AD;
         } else {
            super.AD = super.cmdList.size() - 1;
         }

         GameCanvas.clearKeyPressed(0);
      } else if (GameCanvas.isKeyPressed(2)) {
         if (super.AD < super.cmdList.size() - 1) {
            ++super.AD;
         } else {
            super.AD = 0;
         }

         GameCanvas.clearKeyPressed(2);
      } else if (GameCanvas.AL[5]) {
         GameCanvas.AB(5);
         if (super.cmdList != null && super.AD < super.cmdList.size()) {
            ((iCommand)super.cmdList.elementAt(super.AD)).AD();
         }
      }

      if (GameCanvas.isKeyPressed()) {
         if (super.AD >= 0 && super.AD < super.cmdList.size() && ((iCommand)super.cmdList.elementAt(super.AD)).caption.length() == 0) {
            super.AD = 0;
         }

         for(int var1 = 0; var1 < super.cmdList.size(); ++var1) {
            iCommand var2 = (iCommand)super.cmdList.elementAt(var1);
            if (var1 == super.AD) {
               var2.AG = true;
            } else {
               var2.AG = false;
            }
         }
      }

      this.AS();
   }

   public final void updatePointer() {
      super.updatePointer();
      if (super.cmdList != null) {
         for(int var1 = 0; var1 < super.cmdList.size(); ++var1) {
            ((iCommand)super.cmdList.elementAt(var1)).AE();
         }
      }

   }
}
