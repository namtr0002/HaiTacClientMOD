import java.io.IOException;
import java.util.Vector;

final class Sender implements Runnable {
   private Vector AA;
   private Session_ME AB;

   public Sender(Session_ME var1) {
      this.AB = var1;
      this.AA = new Vector();
   }

   public final synchronized void AA(Message var1) {
      this.AA.addElement(var1);
      this.notify();
   }

   public final synchronized void AA() {
      if (this.AA != null) {
         this.AA.removeAllElements();
      }
      this.notify();
   }

   public final void run() {
      try {
         while(this.AB.AC) {
            if (this.AB.getKeyComplete) {
               while(true) {
                  Message var1 = null;
                  synchronized(this) {
                     if (this.AA.size() > 0) {
                        var1 = (Message)this.AA.elementAt(0);
                        this.AA.removeElementAt(0);
                     }
                  }
                  if (var1 == null) {
                     break;
                  }
                  Session_ME.AA(this.AB, var1);
               }
            }

            try {
               synchronized(this) {
                  if (this.AA.size() == 0) {
                     this.wait(50L);
                  }
               }
            } catch (InterruptedException var2) {
            }
         }

      } catch (Exception var3) {
      }
   }
}
