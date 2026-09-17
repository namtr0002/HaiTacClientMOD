final class ShowInfo implements Runnable {
   private TabInventory invenp;

   ShowInfo(TabInventory inv) {
      this.invenp = inv;
   }

   public final void run() {
      while(TabInventory.numPotionTemp > 0) {
         TabInventory tabinv = this.invenp;
         if (TabInventory.checkFullItem()) {
            TabInventory.numPotionTemp = 0;
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.fullItemQuickOpen);
            return;
         }

         if (TabInventory.focusIDItem == -1) {
            return;
         }

         tabinv = this.invenp;
         if (!TabInventory.checkFullItem()) {
            GlobalService.getInstance().Use_Potion(TabInventory.focusIDItem);
         }

         if (--TabInventory.numPotionTemp < TabInventory.numPotionInput) {
            try {
               int num1;
               Thread.sleep((long)(num1 = (int)(3000.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D))));
            } catch (Exception var2) {
               var2.printStackTrace();
            }
         }
      }

   }
}
