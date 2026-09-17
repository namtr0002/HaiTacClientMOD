import java.util.Timer;
import java.util.TimerTask;

final class Class_IJ extends TimerTask {
   private final Timer AA;

   Class_IJ(AutoRepeatQuest var1, Timer var2) {
      this.AA = var2;
   }

   public final void run() {
      AutoRepeatQuest.AK();
      this.AA.cancel();
   }
}
