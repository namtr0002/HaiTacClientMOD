public final class NumberMess {
   public int yNum;
   private int AB;
   private boolean AC = false;

   public final void AA() {
      if (this.yNum == 0) {
         if (CRes.random(50) == 0) {
            this.AB = -CRes.random(2, 4);
            this.AC = true;
         }
      } else if (this.yNum > 0) {
         this.AC = false;
         this.AB = 0;
         this.yNum = 0;
      }

      if (this.AC || this.yNum != 0) {
         this.yNum += this.AB;
      }

      ++this.AB;
   }
}
