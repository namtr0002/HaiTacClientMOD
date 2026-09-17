public final class Class_FN extends Class_GE {
   public static boolean AA = false;
   private iCommand AB;
   private iCommand AC;
   private static Class_FN AD;

   public static Class_FN AG() {
      return AD == null ? (AD = new Class_FN((byte)-2, Player.RN)) : AD;
   }

   private Class_FN(byte var1, mVector var2) {
      super((byte)-2, var2, T.DP, 200, 180);
      this.AB = new iCommand(T.DU, 10, this);
      this.AC = new iCommand(T.DR, 11, this);
      super.AS.removeAllElements();
      if (!GameCanvas.isTouch) {
         super.AS.addElement(super.BA);
      } else {
         super.AS.addElement(this.AC);
      }

      if (GameCanvas.isTouch) {
         super.DB = super.BB;
      } else {
         super.AS.addElement(super.BB);
      }

      super.backCMD = super.BB;
      super.DE = super.BA;
      super.DF = super.BA;
      super.AX = 0;
      this.AA(0, super.AS);
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 10:
         if (super.AU != null) {
            GlobalService.getInstance().AA((byte)1, (int)super.AU.AG);
         }

         return;
      case 11:
         GlobalService.getInstance().AA((byte)2, (int)0);
         return;
      default:
         super.commandPointer(var1, var2);
      }
   }

   public final void AA() {
      if (super.AY.size() != 0) {
         if (super.AW >= 0 && super.AW < super.AY.size()) {
            super.AU = (Class_DS)super.AY.elementAt(super.AW);
            if (super.AU != null && super.AU.AG != GameScreen.player.ID) {
               mVector var1;
               (var1 = new mVector()).addElement(super.BC);
               var1.addElement(super.BD);
               var1.addElement(this.AB);
               var1.addElement(super.BF);
               GameCanvas.menu.startAt(var1, 2, super.AU.AC);
            }
         }

      }
   }

   public final void AB() {
      mVector var1 = new mVector();
      String var2 = T.AU;
      if (super.AW >= 0 && super.AW < super.AY.size()) {
         super.AU = (Class_DS)super.AY.elementAt(super.AW);
         if (super.AU != null) {
            var1.addElement(super.BC);
            var1.addElement(super.BD);
            var1.addElement(this.AB);
            var1.addElement(super.BF);
            var2 = super.AU.AC;
         }
      }

      var1.addElement(this.AC);
      GameCanvas.menu.startAt(var1, 2, var2);
   }

   public final void AA(mGraphics var1, Class_DS var2, int var3, int var4, int var5, int var6) {
      if (var2 != null) {
         String var7 = var2.AC;
         if (var2.AK >= 0) {
            var7 = var7 + " - " + T.gohome + " " + var2.AK;
         }

         AvMain.fraStatusOnline.drawFrame(var2.AP, var3 + 25, var4 + 5, 0, 3, var1);
         mFont.tahoma_7b_black.drawString(var1, var7, var3 + 30, var4, 0);
         mFont.tahoma_7_black.drawString(var1, var2.AD, var3 + 25, var4 + GameCanvas.hText, 0);
         MainObject.AA(var1, var2.AV, var2.AW, var2.AX, var3 + 10, var4 + super.BQ / 2 + 32, 0);
      }
   }
}
