public final class ListDungeon extends Class_GE {
   private iCommand AD;
   private iCommand AE;
   public static mVector AA = new mVector("ListDungeon.vecDungeon");
   public static boolean AB = false;
   public static ListDungeon instance;

   public ListDungeon(byte var1, mVector var2, String var3) {
      super((byte)-4, var2, var3, 180, 180);
      this.AD = new iCommand(T.KJ, 0, this);
      this.AE = new iCommand(T.CD, 1, this);
      super.AS.removeAllElements();
      if (AB) {
         this.AD.caption = T.IP;
      }

      super.DB = null;
      super.AS.addElement(this.AD);
      super.AS.addElement(this.AE);
      this.AA(0, super.AS);
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         GlobalService.getInstance().AI((byte)1);
         return;
      case 1:
         GlobalService.getInstance().AI((byte)2);
         GameCanvas.gameScr.Show();
         return;
      default:
         super.commandPointer(var1, var2);
      }
   }

   public final void AA(mGraphics var1, Class_DS var2, int var3, int var4, int var5, int var6) {
      String var7 = var2.AC;
      if (var2.AK >= 0) {
         var7 = var7 + " - " + var2.AK;
      }

      var1.drawRegion((mImage)AvMain.imgBorderCombo, var3 + 8, var4 + 6, 3);
      if (var2.AP == 1) {
         AvMain.fraCheck.drawFrame(2, var3 + 8, var4 + 6, 0, 3, var1);
      }

      if (var2.AP == 2) {
         AvMain.fraCheck.drawFrame(1, var3 + 8, var4 + 6, 0, 3, var1);
      }

      AvMain.AA(var1, var7, var3 + 20, var4, 0, (byte)0, (byte)7);
      mFont.tahoma_7_black.drawString(var1, LoadMap.AB(var2.AA), var3 + super.AV, var4 + 17, 0);
   }
}
