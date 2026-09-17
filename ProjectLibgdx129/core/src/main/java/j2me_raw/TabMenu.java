public final class TabMenu extends MainTab {
   private Menu BN = new Menu();

   public TabMenu(String var1, mVector var2) {
      super.nameTab = var1;
      super.AN = GameCanvas.hCommand;
      if (GameCanvas.isTouch) {
         super.AN = 32;
      }

      this.BN.menuX = super.AO;
      this.BN.wUni = super.AQ;
      this.BN.menuH = super.AR - super.AS * 3;
      this.BN.menuW = super.AN;
      this.BN.menuY = super.AP;
      this.BN.menuTemY = this.BN.menuY;
      this.BN.AB(var2);
      if (GameCanvas.isTouch) {
         this.BN.AC = -1;
      } else {
         this.BN.AC = 0;
      }

      super.indexIconTab = 6;
   }

   public final void AB() {
      if (GameCanvas.isTouch) {
         this.BN.AC = -1;
      } else {
         this.BN.AC = 0;
      }

      this.BN.cmtoX = 0;
      this.BN.cmx = 0;
      this.BN.AB(GameCanvas.gameScr.AG());
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      var1.AD(super.AO - 1, super.AP + 1, super.AQ + 2, super.AR - 1 - super.AS - 1);
      mGraphics.AC();
      int var10000 = super.AO;
      var10000 = super.AP;
      var10000 = super.AQ;
      var10000 = super.AR;
      var10000 = super.AS;
      mGraphics.AD();
      var1.translate(0, -this.BN.cmx);

      int var2;
      for(var2 = 0; var2 < this.BN.menuItems.size() - 1; ++var2) {
         var1.setColor(AvMain.II[4]);
         var1.fillRect(this.BN.menuX + 8, this.BN.menuY + 3 + this.BN.menuW + var2 * this.BN.menuW - 1, this.BN.wUni - 16, 2);
         var1.fillRect(this.BN.menuX + 8 + 1, this.BN.menuY + 3 + this.BN.menuW + var2 * this.BN.menuW - 2, this.BN.wUni - 16 - 2, 4);
      }

      if ((var2 = this.BN.cmx / this.BN.menuW - 1) < 0) {
         var2 = 0;
      }

      int var3;
      if ((var3 = var2 + this.BN.sizeMenu + 2) > this.BN.menuItems.size() && (var2 = (var3 = this.BN.menuItems.size()) - this.BN.sizeMenu - 2) < 0) {
         var2 = 0;
      }

      MainScreen var6 = GameCanvas.currentScreen;
      if (MainScreen.AD((byte)1) && this.BN.AC >= 0 && (!GameCanvas.isTouch || this.BN.AM > 0)) {
         this.paintSelect(var1, this.BN.menuX + 10, this.BN.menuY + 7 + this.BN.AC * this.BN.menuW, this.BN.wUni - 20, this.BN.menuW - 8);
      }

      for(var2 = var2; var2 < var3; ++var2) {
         ((iCommand)this.BN.menuItems.elementAt(var2)).AA(var1, this.BN.menuX + this.BN.wUni / 2, this.BN.menuY + 4 + this.BN.menuW / 4 + var2 * this.BN.menuW, 2, false);
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      super.paint(var1);
   }

   public final void update() {
      this.BN.AH();
      super.update();
   }

   public final void handleKeyPress() {
      if (GameCanvas.isKeyPressed(0) || GameCanvas.isKeyPressed(2)) {
         MainScreen var10000 = GameCanvas.currentScreen;
         MainScreen.AC((byte)0);
         GameCanvas.clearKeyPressed(0);
         GameCanvas.clearKeyPressed(2);
      }

      this.BN.AF();
      super.handleKeyPress();
   }

   public final void updatePointer() {
      super.updatePointer();
   }
}
