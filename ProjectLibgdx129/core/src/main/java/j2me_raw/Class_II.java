import java.util.Timer;
import java.util.TimerTask;

final class Class_II extends TimerTask {
   private final Timer AA;

   Class_II(AutoRepeatQuest var1, Timer var2) {
      this.AA = var2;
   }

   public final void run() {
      AutoRepeatQuest.AJ();
      this.AA.cancel();
   }
}
