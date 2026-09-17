final class Class_HV implements Runnable {
   private Class_HU AA;

   Class_HV(Class_HU var1) {
      this.AA = var1;
   }

   public final void run() {
      try {
         Thread.sleep(20000L);
      } catch (InterruptedException var3) {
      }

      if (this.AA.AA.AD) {
         try {
            Session_ME.AA(this.AA.AA).close();
         } catch (Exception var2) {
         }

         Session_ME.AJ = true;
         this.AA.AA.AD = false;
         this.AA.AA.AC = false;
         Session_ME.AB.AA();
      }

   }
}
