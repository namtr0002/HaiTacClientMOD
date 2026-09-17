public final class InfoShowNotify {
   public String strShow;
   public boolean AB = false;
   public boolean AC = true;
   public short AD = -1;
   public mFont fontpaint;
   public int AF;
   public int AG;
   public int AH;
   private int AJ;
   private int AK;
   private int AL;
   public byte AI;

   public InfoShowNotify(String var1, byte var2) {
      this.fontpaint = mFont.tahoma_7_white;
      this.AF = 0;
      this.strShow = var1;
      this.AI = var2;
   }

   public final void setValue(mFont var1) {
      if (this.strShow != null && this.strShow.length() != 0) {
         this.AF = 0;
         this.AB = false;
         this.AB(var1);
         if (this.AI == 10) {
            this.AK = 0;
            this.AL = 70;
            this.AG = Interface_Game.wInfoServer / 2;
         } else {
            this.AJ = this.fontpaint.getWidth(this.strShow);
            if (this.AD >= 0) {
               this.AJ += 30;
            }

            this.AG = 0;
            this.AL = 500;
            this.AH = 25;
            if (this.AJ < Interface_Game.wInfoServer) {
               this.AK = 0;
               this.AL = 150;
               this.AG = Interface_Game.wInfoServer / 2 + this.AJ / 2;
            } else {
               this.AK = 2;
            }
         }
      } else {
         this.AB = true;
      }
   }

   public final void AB(mFont var1) {
      if (var1 != null) {
         this.fontpaint = var1;
      } else {
         switch(this.AI) {
         case 0:
            this.fontpaint = mFont.tahoma_7_white;
            return;
         case 1:
            this.fontpaint = mFont.tahoma_7b_white;
            return;
         case 2:
            this.fontpaint = mFont.tahoma_7b_yellow;
            return;
         case 5:
            this.fontpaint = mFont.tahoma_7b_white;
         case 3:
         case 4:
         case 6:
         case 7:
         case 8:
         case 9:
         default:
            return;
         case 10:
            this.fontpaint = mFont.tahoma_7_white;
         }
      }
   }

   public final void AA() {
      if (this.AC) {
         ++this.AF;
         this.AG += this.AK;
      }

      if (this.AF <= this.AL && this.AG <= this.AJ + Interface_Game.wInfoServer) {
         if (this.AH > 0) {
            this.AH -= 10;
            if (this.AH < 0) {
               this.AH = 0;
            }
         }

      } else {
         this.AB = true;
      }
   }
}
