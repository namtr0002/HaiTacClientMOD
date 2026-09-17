public final class QuickMenu extends Menu {
   private static QuickMenu AT;
   public static FrameImage[] fraQuickMenu;
   public static mImage imgNenMenu;
   public static mImage imgTamGiac;
   private ListNew AU;
   private boolean AV = false;
   private int AW = 10;
   private int AX;
   private static int[][] AY = new int[][]{new int[2], {12, 12}, {7, 7}, {2, 2}, {14, 14}, {15, 15}, {13, 13}, {3, 3}, {5, 5}, {6, 6}, {9, 8}, {11, 11}, {8, 9}};
   private int[] AZ = new int[]{-7387904, -1, -1, -5360};

   public static QuickMenu AI() {
      return AT == null ? (AT = new QuickMenu()) : AT;
   }

   public final void AJ() {
      this.beginMenu();
      this.AV = false;
      super.cmdClose = new iCommand("", -1, this);
      if (GameCanvas.isKeyPressed()) {
         super.AC = 0;
      } else {
         super.AC = -1;
      }

      super.menuItems = new mVector();

      for(int var1 = 0; var1 < AY.length; ++var1) {
         if (AY[var1][0] <= 5 || AY[var1][0] == 6 && Player.vecParty.size() > 0 || AY[var1][0] == 7 || AY[var1][0] == 8 || AY[var1][0] == 9 || AY[var1][0] == 12 || AY[var1][0] == 11 || AY[var1][0] == 10 && GameScreen.player.clan != null || AY[var1][0] == 13 || AY[var1][0] == 14 || AY[var1][0] == 15) {
            int var2;
            iCommand var3;
            (var3 = new iCommand((var2 = AY[var1][0]) >= 0 && var2 < T.WJ.length ? T.WJ[var2] : T.AS, AY[var1][0], this)).AA(fraQuickMenu[AY[var1][1]]);
            super.menuItems.addElement(var3);
         }
      }

      super.wUni = 60;
      this.AX = -40;
      super.menuW = 50;
      super.menuX = 0;
      if (GameCanvas.isTaiTho) {
         super.menuX = 30;
      }

      super.menuY = 0;
      this.AU = new ListNew(super.menuX, super.menuY, super.menuW, MotherCanvas.h, 0, 0, super.menuItems.size() * super.menuW - MotherCanvas.h, true);
      this.AB();
      super.isShowMenu = true;
      GameCanvas.AA((Menu)AI());
      super.backCMD = super.cmdClose;
   }

   public final void commandPointer(int var1, int var2) {
      mVector var3;
      switch(var1) {
      case -1:
         this.AV = true;
         return;
      case 0:
         (var3 = new mVector()).addElement(GameCanvas.gameScr.AZ);
         var3.addElement(GameCanvas.gameScr.BA);
         GameCanvas.menu.startAt(var3, 2, T.MF);
         return;
      case 1:
         GameCanvas.gameScr.BA.AD();
         return;
      case 2:
         GameCanvas.gameScr.AW.AD();
         return;
      case 3:
         (var3 = new mVector()).addElement(GameCanvas.gameScr.AQ);
         var3.addElement(GameCanvas.gameScr.AR);
         GameCanvas.menu.startAt(var3, 2, T.IQ);
         return;
      case 4:
         GameCanvas.gameScr.AR.AD();
         return;
      case 5:
         GameCanvas.gameScr.BC.AD();
         return;
      case 6:
         GameCanvas.gameScr.AS.AD();
         return;
      case 7:
         GameCanvas.gameScr.BQ.AD();
         return;
      case 8:
         GameCanvas.gameScr.AU.AD();
         return;
      case 9:
         GameCanvas.gameScr.BJ.AD();
         return;
      case 10:
         GameCanvas.gameScr.BM.AD();
         return;
      case 11:
         if (!GameCanvas.isIos()) {
            GameCanvas.gameScr.BL.AD();
            return;
         }
         break;
      case 12:
         GameCanvas.gameScr.BP.AD();
         return;
      case 13:
         GameCanvas.gameScr.BU.AD();
         return;
      case 14:
         GameCanvas.gameScr.BV.AD();
         return;
      case 15:
         GameCanvas.gameScr.BX.AD();
      }

   }

   public final void AB(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = super.menuX + this.AX;

      int var3;
      for(var3 = 0; var3 <= MotherCanvas.h / super.wUni; ++var3) {
         var1.drawRegion((mImage)imgNenMenu, var2, var3 * super.wUni, 0);
      }

      if (GameCanvas.isTaiTho) {
         for(var3 = 0; var3 <= MotherCanvas.h / super.wUni; ++var3) {
            var1.drawRegion((mImage)imgNenMenu, var2 - 60, var3 * super.wUni, 0);
         }
      }

      byte var6 = 0;
      if (this.AX != 0) {
         var6 = 1;
      }

      var1.drawRegion(imgTamGiac, 0, var6 * 24, 13, 24, 0, var2 + super.wUni, MotherCanvas.h / 2 - 12, 0);
      var1.setColor(this.AZ[var6 << 1]);
      var1.fillRect(var2 + super.wUni + 1, 0, 1, MotherCanvas.h / 2 - 12);
      var1.fillRect(var2 + super.wUni + 1, MotherCanvas.h / 2 + 12, 1, MotherCanvas.h / 2 - 12);
      var1.setColor(this.AZ[(var6 << 1) + 1]);
      var1.fillRect(var2 + super.wUni, 0, 1, MotherCanvas.h / 2 - 11);
      var1.fillRect(var2 + super.wUni, MotherCanvas.h / 2 + 11, 1, MotherCanvas.h / 2 - 11);
      var1.translate(0, -this.AU.AC);

      for(var3 = 0; var3 < super.menuItems.size(); ++var3) {
         iCommand var4 = (iCommand)super.menuItems.elementAt(var3);
         byte var5 = 0;
         if (super.AC == var3) {
            var5 = 1;
         }

         var4.AA(var1, var2 + super.wUni / 2, super.menuY + super.menuW / 2 + var3 * super.menuW - 5, var5);
         mFont.tahoma_7_white.drawString(var1, var4.caption, var2 + super.wUni / 2, super.menuY + super.menuW / 2 + 7 + var3 * super.menuW, 2);
      }

   }

   public final void AH() {
      if (!this.AV) {
         if (this.AX < 0) {
            this.AX += this.AW;
         }

         this.AU.AC();
         this.updatePointer();
      } else {
         this.AX -= this.AW;
         if (this.AX < -super.wUni) {
            super.isShowMenu = false;
         }

      }
   }

   public final void updatePointer() {
      if (GameCanvas.isPointerSelect) {
         if (!GameCanvas.isPoint(super.menuX, super.menuY, super.menuW, MotherCanvas.h)) {
            this.AV = true;
         } else {
            int var1;
            if ((var1 = (this.AU.AC + GameCanvas.AZ) / super.menuW) >= 0 && var1 < super.menuItems.size()) {
               ((iCommand)super.menuItems.elementAt(var1)).AD();
               super.isShowMenu = false;
            }
         }
      }

      this.AU.update_Pos_UP_DOWN();
      super.updatePointer();
   }

   public final void AF() {
      int var1 = super.AC;
      if (!GameCanvas.isKeyPressed(0) && !GameCanvas.isKeyPressed(1)) {
         if (GameCanvas.isKeyPressed(2) || GameCanvas.isKeyPressed(3)) {
            ++super.AC;
            GameCanvas.clearKeyPressed(2);
            GameCanvas.clearKeyPressed(3);
         }
      } else {
         --super.AC;
         GameCanvas.clearKeyPressed(0);
         GameCanvas.clearKeyPressed(1);
      }

      super.AC = AvMain.AA(super.AC, super.menuItems.size() - 1, false);
      if (GameCanvas.AL[5]) {
         GameCanvas.AB(5);
         if (super.AC < super.menuItems.size() && super.AC >= 0) {
            ((iCommand)super.menuItems.elementAt(super.AC)).AD();
            super.isShowMenu = false;
         }
      }

      if (var1 != super.AC && GameCanvas.isKeyPressed()) {
         this.AU.AA((super.AC + 1) * super.menuW - MotherCanvas.h / 2);
      }

      this.AS();
   }
}
