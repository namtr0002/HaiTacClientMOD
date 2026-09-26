import java.util.Timer;
import java.util.TimerTask;

final class Class_IL extends TimerTask {
   private final Timer AA;

   Class_IL(AutoRepeatQuest var1, Timer var2) {
      this.AA = var2;
   }

   public final void run() {
      AutoRepeatQuest.CA();
      this.AA.cancel();
   }
}
