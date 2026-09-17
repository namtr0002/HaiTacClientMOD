import java.io.IOException;
import java.util.Vector;

final class Sender implements Runnable {
   private Vector AA;
   private Session_ME AB;

   public Sender(Session_ME var1) {
      this.AB = var1;
      this.AA = new Vector();
   }

   public final void AA(Message var1) {
      this.AA.addElement(var1);
   }

   public final void AA() {
      if (this.AA != null) {
         this.AA.removeAllElements();
      }

   }

   public final void run() {
      try {
         while(this.AB.AC) {
            if (this.AB.getKeyComplete) {
               while(this.AA.size() > 0) {
                  Message var1 = (Message)this.AA.elementAt(0);
                  this.AA.removeElementAt(0);
                  Session_ME.AA(this.AB, var1);
               }
            }

            try {
               Thread.sleep(10L);
            } catch (InterruptedException var2) {
            }
         }

      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }
}
