final class Class_HU implements Runnable {
   private final String AB;
   private final int AC;
   final Session_ME AA;

   Class_HU(Session_ME var1, String var2, int var3) {
      this.AA = var1;
      this.AB = var2;
      this.AC = var3;
   }

   public final void run() {
      Session_ME.AJ = false;
      (new Thread(new Class_HV(this))).start();
      this.AA.AD = true;
      Thread.currentThread().setPriority(1);
      this.AA.AC = true;

      try {
         int var3 = this.AC;
         String var2 = this.AB;
         Session_ME.AA(this.AA, new TcpClient(var2, var3));
         Session_ME.AA(this.AA);
         Session_ME.AA(this.AA, Session_ME.AA(this.AA).AB());
         this.AA.AA = Session_ME.AA(this.AA).AC();
         (new Thread(Session_ME.AB(this.AA))).start();
         this.AA.AE = new Thread(new Class_HT(this.AA));
         this.AA.AE.start();
         this.AA.AI = System.currentTimeMillis();
         Session_ME.AA(this.AA, new Message((byte)-27));
         this.AA.AD = false;
         Session_ME.AB.GlobalLogicHandlerV();
      } catch (Exception var5) {
         try {
            Thread.sleep(500L);
         } catch (InterruptedException var4) {
         }

         if (!Session_ME.AJ) {
            if (Session_ME.AB != null) {
               this.AA.close();
               Session_ME.AB.AA();
            }

         }
      }
   }
}
