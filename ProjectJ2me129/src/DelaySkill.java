public final class DelaySkill {
   public byte AA;
   public long AB;
   public int AC = 0;
   public int AD;

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      FrameImage var5 = AvMain.fraDelay;
      if (var4 > 32) {
         var4 = 32;
      }

      if (var4 <= 24) {
         var5 = AvMain.fraDelay2;
      }

      if (this.AD > 0) {
         if (this.AC > 0) {
            int var6;
            if ((var6 = 4 - this.AC / (this.AD / 5)) >= 0 && var6 <= 5) {
               var1.drawRegion(var5.imgFrame, var5.frameWidth / 2 - var4 / 2, var5.frameWidth / 2 - var4 / 2 + var6 * var5.frameHeight, var4, var4, 0, var2, var3, 0);
            }

            int var7;
            String var8;
            if ((var7 = this.AC / 1000) == 0) {
               var8 = "0." + this.AC % 1000 / 100;
            } else {
               var8 = "" + var7;
            }

            mFont.tahoma_7_white.drawString(var1, var8, var2 + var4 / 2, var3 + var4 / 2 - 5, 2);
            return;
         }

         if (this.AC > -150) {
            var1.setColor(-1118516);
            var1.fillRoundRectNew(var2 + 1, var3 + 1, var4 - 2, var4 - 2, 4, 4);
         }
      }

   }

   public final boolean isCoolDown() {
      return this.AC <= 0;
   }

   public static DelaySkill getDelay(int var0) {
      DelaySkill var1;
      if ((var1 = (DelaySkill)Player.QE.get("" + var0)) == null) {
         (var1 = new DelaySkill()).AC = -150;
         var1.AD = 0;
         var1.AB = GameCanvas.timeNow;
         Player.QE.put("" + var0, var1);
      }

      return var1;
   }
}
