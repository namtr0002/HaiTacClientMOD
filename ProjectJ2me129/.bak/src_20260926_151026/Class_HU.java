final class Class_HU implements Runnable {
   private final String AB;
   private final int AC;
   final Session_ME AA;
   // Giảm timeout xuống 8s – đủ cho mạng di động bình thường.
   // TcpClient mới sẽ tự timeout sau đúng khoảng này.
   private static final int CONNECTION_TIMEOUT = 8000;
   // Giảm retry xuống 2 – tổng thời gian chờ tối đa ~18s (8s×2 + 2s delay)
   private static final int MAX_RETRY_ATTEMPTS = 2;

   Class_HU(Session_ME var1, String var2, int var3) {
      this.AA = var1;
      this.AB = var2;
      this.AC = var3;
   }

   public final void run() {
      Session_ME.AJ = false;
      (new Thread(new Class_HV(this))).start();
      this.AA.AD = true;
      this.AA.AC = false;
      Thread.currentThread().setPriority(1);

      boolean connected = false;
      int retryCount = 0;

      while (!connected && retryCount < MAX_RETRY_ATTEMPTS && !Session_ME.AJ) {
         try {
            if (Thread.currentThread().isInterrupted()) {
               break;
            }

            int var3 = this.AC;
            String var2 = this.AB;
            // TcpClient(host, port, timeoutMs) – giờ có timeout thật
            TcpClient client = new TcpClient(var2, var3, CONNECTION_TIMEOUT);
            Session_ME.AA(this.AA, client);

            // socketp null nghĩa là timeout hoặc lỗi kết nối
            if (Session_ME.AA(this.AA) != null && Session_ME.AA(this.AA).socketp != null) {
               Session_ME.AA(this.AA);
               Session_ME.AA(this.AA, Session_ME.AA(this.AA).AB());
               this.AA.AA = Session_ME.AA(this.AA).AC();

               if (this.AA.AA != null) {
                  this.AA.AC = true;
                  this.AA.AD = false;
                  (new Thread(Session_ME.AB(this.AA))).start();
                  this.AA.AE = new Thread(new Class_HT(this.AA));
                  this.AA.AE.start();
                  this.AA.currentimestamp = System.currentTimeMillis();
                  Session_ME.AA(this.AA, new Message((byte)-27));
                  Session_ME.AB.GlobalLogicHandlerV();
                  connected = true;
               }
            }

         } catch (Exception var5) {
            // lỗi bất ngờ – không retry thêm
         }

         if (!connected) {
            retryCount++;
            if (retryCount < MAX_RETRY_ATTEMPTS && !Session_ME.AJ) {
               try {
                  Thread.sleep(2000L);
               } catch (InterruptedException var4) {
                  break;
               }
               // Kiểm tra interrupt sau khi sleep
               if (Thread.currentThread().isInterrupted()) {
                  break;
               }
            }
         }
      }

      // Quan trọng: dù kết nối thành công hay thất bại,
      // đảm bảo AD = false để connectServer() có thể chạy lại sau
      if (!connected) {
         this.AA.AD = false;
         this.AA.AC = false;
         if (!Session_ME.AJ) {
            if (Session_ME.AB != null) {
               this.AA.close();
               Session_ME.AB.AA();
            } else {
               this.AA.close();
            }
         } else {
            // Bị force-stop, chỉ close không gọi callback
            this.AA.close();
         }
      }
   }
}
