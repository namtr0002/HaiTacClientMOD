public final class Class_AO extends MsgDialog {
   private Class_DS AA;
   private int AB = 120;
   private iCommand AC;
   private iCommand BB;
   private iCommand BC;
   private iCommand BD;
   private iCommand BE;
   private iCommand BF;
   private InputDialog BG;

   public Class_AO(Class_DS var1) {
      this.AA = var1;
      if (var1 != null) {
         super.cmdList.removeAllElements();
         super.AG = new iCommand(T.close, 1, this);
         (new StringBuffer("mem.name = ")).append(var1.AC).append(" GameScreen.player.name ").append(GameScreen.player.name).toString();
         if (var1.AC.compareTo(GameScreen.player.name) != 0) {
            this.AC = new iCommand(T.CA, 2, this);
            this.BB = new iCommand(T.kickClan, 10, this);
            new iCommand(T.strDonate, 4, this);
            this.BC = new iCommand(T.HJ, 5, this);
            this.BD = new iCommand(T.CP, 6, this);
            this.BE = new iCommand(T.CN, 7, this);
            new iCommand(T.phongchuc, 8, this);
            super.cmdList.addElement(this.AC);
         } else {
            (new StringBuffer("Player.ChucInSudo ")).append(Player.QO).append(" mem.chucInSudo ").append(var1.AU).toString();
            if (Player.QO != 1 && Player.QO == 2) {
               this.BF = new iCommand(T.VU, 11, this);
               super.cmdList.addElement(this.BF);
            }
         }

         if (!GameCanvas.isTouch) {
            super.cmdList.addElement(super.AG);
         }

         super.wDia = 160;
         super.hDia = 140;
         if (!GameCanvas.isTouch) {
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
         (var5 = new mVector()).addElement(this.BC);
         var5.addElement(this.BD);
         var5.addElement(this.BE);
         if (Player.QO == 1) {
            var5.addElement(this.BB);
         }

         GameCanvas.menu.startAt(var5, 2, this.AA.AC);
         return;
      case 3:
         GlobalService.getInstance().AB((byte)16, this.AA.AC, (short)this.AA.AG, (byte)0);
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
         GlobalService.getInstance().AB((byte)17, "", (short)this.AA.AG, (byte)0);
         GameCanvas.end_Dialog();
         return;
      case 10:
         GameCanvas.Start_Normal_DiaLog(T.banmuon + this.AA.AC + " " + T.VT, new iCommand(T.kickClan, 3, this), true);
         return;
      case 11:
         GameCanvas.Start_Normal_DiaLog(T.banmuon + T.VT, new iCommand(T.VU, 9, this), true);
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
         GameCanvas.tabShopScr = new TabScreen(MainTab.xTab, (byte)0);
         mVector var6 = new mVector();
         GameCanvas.tabShopScr.isShopClan = true;
         (GameCanvas.tabInvenClan = new TabInventory(T.PA, Player.AW, (byte)4, MainTab.xTab)).initCmd();
         var6.addElement(GameCanvas.tabInvenClan);
         GameCanvas.tabShopScr.addVecTab(var6);
         GameCanvas.tabShopScr.idSelect = 0;
         GameCanvas.tabShopScr.Show((MainScreen)GameCanvas.ClanScr);
         GameCanvas.tabShopScr.typeCurrent = 1;
         GameCanvas.tabShopScr.setTabSelect();
         return;
      case 15:
         if (this.BG == null) {
            this.BG = new InputDialog();
            var3 = new iCommand(T.AD, 16, this);
            this.BG.AA(T.GJ, var3, false, T.GI);
         } else {
            this.BG.tfInput.AB("");
         }

         GameCanvas.AB(this.BG);
         return;
      case 16:
         GlobalService.getInstance().World_Chanel((byte)1, (String)this.BG.tfInput.getText());
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
      mFont.tahoma_7b_black.drawString(var1, this.AA.AU == 1 ? T.VL : T.VM, var3 + (super.AU << 1) + 16, var2 + GameCanvas.hText - 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.capdo + ": " + this.AA.AK, var3, var2 + (GameCanvas.hText << 1) - 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.VS + ": " + this.AA.AF, var3, var2 + GameCanvas.hText * 3 - 1, 0);
      if (super.cmdList != null) {
         for(var2 = 0; var2 < super.cmdList.size(); ++var2) {
            iCommand var5;
            (var5 = (iCommand)super.cmdList.elementAt(var2)).paint(var1, var5.xCmd, var5.yCmd);
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
