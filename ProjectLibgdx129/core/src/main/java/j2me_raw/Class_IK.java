import java.util.Timer;
import java.util.TimerTask;

final class Class_IK extends TimerTask {
   private final Timer AA;

   Class_IK(AutoRepeatQuest var1, Timer var2) {
      this.AA = var2;
   }

   public final void run() {
      AutoRepeatQuest.AL();
      this.AA.cancel();
   }
}
