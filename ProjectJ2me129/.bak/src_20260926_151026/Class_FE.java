public final class Class_FE extends Class_GE {
   private iCommand AB;
   public static Class_FE AA;

   public Class_FE(byte var1, mVector var2, String var3, byte var4) {
      super(var1, var2, var3, 200, 180);
      this.AB = new iCommand(T.DR, 14, this);
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 14:
         GameCanvas.gameScr.BA.AD();
         super.BI = true;
         return;
      default:
         super.commandPointer(var1, var2);
      }
   }

   public final void AA() {
      if (super.AY.size() != 0) {
         super.AU = (Class_DS)super.AY.elementAt(super.AW);
         if (super.AU != null && super.AU.AG != GameScreen.player.ID) {
            mVector var1;
            (var1 = new mVector()).addElement(super.BC);
            var1.addElement(super.BF);
            if (var1.size() > 0) {
               GameCanvas.menu.startAt(var1, 2, super.AU.AC);
            }
         }

      }
   }

   public final void AB() {
      mVector var1 = new mVector();
      String var2 = T.AU;
      if (super.AY.size() > 0) {
         super.AU = (Class_DS)super.AY.elementAt(super.AW);
         if (super.AU != null && !GameCanvas.isTouch) {
            var1.addElement(super.BC);
            var2 = super.AU.AC;
         }
      }

      var1.addElement(this.AB);
      if (!GameCanvas.isTouch) {
         var1.addElement(super.BF);
      }

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
