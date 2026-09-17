public final class Class_DY extends MainItem {
   public Class_DY(short var1, short var2, String var3, String var4) {
      super.ID = var1;
      super.idIcon = var2;
      super.typeObject = 107;
      super.name = var3;
      super.info = var4;
      super.namepaint = var3;
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      this.AC(var1, var2, var3, var4);
      MainImage var5;
      if ((var5 = Potion.getIconClan(super.idIcon)) != null && var5.img != null) {
         this.AA(var1, var5, var2, var3);
      } else {
         AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, var2, var3, 0, 3, var1);
      }
   }

   public final mVector AA(byte var1) {
      mVector var2 = new mVector();
      if (super.colorName == 1) {
         var2.addElement(GameCanvas.CO.CP);
      } else {
         var2.addElement(GameCanvas.CO.CD);
      }

      return var2;
   }
}
