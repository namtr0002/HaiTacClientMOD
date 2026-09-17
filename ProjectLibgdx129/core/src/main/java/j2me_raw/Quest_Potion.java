public final class Quest_Potion extends MainItem {
   public Quest_Potion(byte var1, short var2, String var3) {
      super((byte)5, var2, var2, var3, (byte)0);
      super.indexSort = 2;
      super.BO = 1;
      super.namepaint = var3;
   }

   public final void AA(String var1, byte var2, byte var3) {
      super.vecInfo.addElement(new Class_FB(-1, 0, var1, var2, var3));
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      super.AA(var1, var2, var3, var4);
      this.AE(var1, var2, var3, var4);
   }

   public final MainImage AD() {
      MainImage var1 = null;

      try {
         var1 = ObjectData.getImageAll(super.idIcon, ObjectData.hashImageQuestPotion, (short)6000);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return var1;
   }
}
