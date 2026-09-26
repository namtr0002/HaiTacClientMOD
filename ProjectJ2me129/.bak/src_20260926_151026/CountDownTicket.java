public final class CountDownTicket {
   public int timeCountDown = 0;
   private long tickBeginCount = 0L;
   public String strInfo = "";
   public byte typeTime = 0;
   public int valueLeft = 0;
   public int valueright = 0;

   public final void setCountDown(int time) {
      this.timeCountDown = time;
      this.tickBeginCount = GameCanvas.timeNow;
   }

   public final void updateTimeCountDownTicket() {
      if (this.timeCountDown > 0) {
         if (GameCanvas.timeNow - this.tickBeginCount > 2000L) {
            short var1 = (short)((int)((GameCanvas.timeNow - this.tickBeginCount) / 1000L));
            this.timeCountDown -= var1;
            this.tickBeginCount += (long)(var1 * 1000);
         }

         if (GameCanvas.timeNow - this.tickBeginCount > 1000L) {
            --this.timeCountDown;
            this.tickBeginCount += 1000L;
         }
      }

   }

   public final void paintCountDownTicket(mGraphics g, mFont f, int x, int y, int anchor) {
      anchor = 0;
      if (this.timeCountDown > 0) {
         anchor = this.timeCountDown;
      }

      f.drawString(g, timeShow(anchor), x, y, 2);
   }

   public final void paintCountDownTicketHour(mGraphics g, mFont f, int x, int y, int anchor) {
      if (this.timeCountDown >= 3600) {
         String text = "";
         int num = (anchor = this.timeCountDown / 60) / 60;
         int num2 = anchor % 60;
         String text2;
         if (num < 10) {
            text2 = "0" + num;
         } else {
            text2 = "" + num;
         }

         f.drawString(g, text2, x, y, 0);
         anchor = f.getWidth(text2);
         if (GameCanvas.gameTick % 25 < 20) {
            f.drawString(g, ":", x + anchor + 1, y, 0);
         }

         if (num2 < 10) {
            text = text + "0" + num2;
         } else {
            text = text + num2;
         }

         f.drawString(g, text, x + anchor + 4, y, 0);
      } else {
         f.drawString(g, timeShow(this.timeCountDown), x, y, 0);
      }
   }

   public static String timeShow(int var0) {
      String var1 = "";
      if (var0 >= 3600) {
         var0 /= 60;
         var1 = var1 + var0 / 60;
         if (GameCanvas.gameTick % 25 < 20) {
            var1 = var1 + ":";
         } else {
            var1 = var1 + " ";
         }

         if (var0 % 60 < 10) {
            var1 = var1 + "0" + var0 % 60;
         } else {
            var1 = var1 + var0 % 60;
         }
      } else {
         if (var0 < 60) {
            var1 = var1 + "00";
         } else if (var0 < 600) {
            var1 = var1 + "0" + var0 / 60;
         } else {
            var1 = var1 + var0 / 60;
         }

         if (var0 % 60 < 10) {
            var1 = var1 + ":0" + var0 % 60;
         } else {
            var1 = var1 + ":" + var0 % 60;
         }
      }

      return var1;
   }
}
