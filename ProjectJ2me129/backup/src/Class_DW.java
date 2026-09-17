public final class Class_DW extends MainItem {
   public Class_DW(short var1, short var2, String var3, String var4, short[] var5) {
      super.ID = var1;
      super.idIcon = var2;
      super.typeObject = 105;
      super.name = var3;
      super.info = var4;
      super.namepaint = var3;
      super.BI = var5;
   }

   public final void paint(mGraphics var1, int var2, int var3, int var4) {
      MainImage var5 = this.AF();
      this.AC(var1, var2, var3, var4);
      if (var5 != null && var5.img != null) {
         this.AA(var1, var5, var2, var3);
      } else {
         AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, var2, var3, 0, 3, var1);
      }

      if (super.LvUpgrade > 0) {
         AvMain.FontBorderColor(var1, "+" + super.LvUpgrade, var2 + MainTab.AE / 2 - 2, var3 + MainTab.AE / 2 - 9 - 2, 1, (int)6, (int)0);
      }

   }

   public final mVector getActionShop(byte var1) {
      mVector var2 = new mVector();
      if (var1 != 114 || super.AF != 0 || super.AJ != 0) {
         if (super.AF == 0 && super.AJ == 0) {
            if (super.colorName == 4) {
               var2.addElement(TabShop.CI);
            } else {
               var2.addElement(TabShop.CH);
            }
         } else {
            var2.addElement(TabShop.CJ);
         }
      }

      return var2;
   }
}
