import java.util.Timer;
import java.util.TimerTask;

final class Class_IH extends TimerTask {
   private final Timer AA;

   Class_IH(AutoRepeatQuest var1, Timer var2) {
      this.AA = var2;
   }

   public final void run() {
      AutoRepeatQuest.AF();
      this.AA.cancel();
   }
}
