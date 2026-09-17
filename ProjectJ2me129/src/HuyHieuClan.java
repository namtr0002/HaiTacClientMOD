final class HuyHieuClan implements Runnable {
   private HuyHieuClanScreen mhuyhieu;

   HuyHieuClan(HuyHieuClanScreen mhhieu) {
      this.mhuyhieu = mhhieu;
   }

   public final void run() {
      if (HuyHieuClanScreen.isRunningOpenXu) {
         if (this.mhuyhieu.potionQuay.numPotion <= 0) {
            this.mhuyhieu.isContinue = false;
            HuyHieuClanScreen.isRunningOpenXu = false;
            HuyHieuClanScreen.numXuInput = 0;
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.notEnoughXuHanhTrinh);
            return;
         }

         if (HuyHieuClanScreen.numXuInput == 0) {
            this.mhuyhieu.isContinue = false;
            HuyHieuClanScreen.isRunningOpenXu = false;
         }

         if (HuyHieuClanScreen.numXuInput > 0) {
            --HuyHieuClanScreen.numXuInput;
            GlobalService.getInstance().AA((byte)3, (byte)1, (short)this.mhuyhieu.potionQuay.ID);
            if (HuyHieuClanScreen.numXuInput > 1 && this.mhuyhieu.isContinue) {
               try {
                  int sleep;
                  Thread.sleep((long)(sleep = (int)(800.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D))));
                  return;
               } catch (Exception var2) {
                  var2.printStackTrace();
                  return;
               }
            }
         } else {
            this.mhuyhieu.stopOpenXu();
         }
      }

   }
}
