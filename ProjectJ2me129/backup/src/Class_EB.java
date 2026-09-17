public final class Class_EB extends MainItem {
   public Class_EB(short var1, short var2, int var3) {
      super.indexHotKey = (short)(var2 + var3);
      super.idIcon = var1;
      super.ID = var2;
      super.typeObject = 9;
      if (var3 == 1500) {
         super.colorName = 6;
      } else {
         super.colorName = 0;
      }
   }

   public final void AE() {
      if (super.ID != 9 && super.ID != 10) {
         super.colorName = 6;
      } else {
         super.colorName = 1;
      }
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4, int var5) {
      MainImage var6;
      if ((var6 = this.getImage()) != null && var6.img != null) {
         var1.drawRegion((mImage)var6.img, var2, var3, 3);
      } else {
         AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, var2, var3, 0, 3, var1);
      }
   }

   public final MainImage getImage() {
      return ObjectData.getImageAll(super.idIcon, ObjectData.hashImageSkillSmall, (short)4500);
   }
}
