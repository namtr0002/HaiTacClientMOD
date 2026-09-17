import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Session_ME implements Class_HQ {
   private static Session_ME instance = new Session_ME();
   private DataOutputStream dos;
   public DataInputStream AA;
   public static IMessageHandler AB;
   private TcpClient AN;
   public boolean AC;
   public boolean AD;
   private final Sender sender = new Sender(this);
   private Thread AP;
   public Thread AE;
   private int sendByteCount;
   public int AF;
   boolean getKeyComplete;
   public byte[] key = null;
   private byte curR;
   private byte curW;
   long currentimestamp;
   public static boolean AJ;
   private static mVector recieveMsg = new mVector();
   public static int AK = 0;
   
   private static final long CONNECTION_TIMEOUT = 60000; 
   private static final long HEARTBEAT_TIMEOUT = 120000; 
   private long lastHeartbeat = 0;
   private boolean isConnectionAlive = false;
   private long lastActivity = 0; 

   public static Session_ME getInstance() {
      return instance;
   }

   public final boolean AB() {
      return this.AC;
   }

   public static void setHandler(IMessageHandler var0) {
      AB = var0;
   }

   public final void connectServer(String host, int port) {
      if (!UpdateServer.isAllowedHost(host)) {
         AThMadaraMOD.cancelAutoReconnect();
         this.forceStopConnection();
         this.close();
         UpdateServer.showBlockedDialog();
         return;
      }
      // Nếu đang pending kết nối hoặc đã kết nối cũ – force stop để tạo kết nối mới
      if (this.AD || this.AC) {
         this.forceStopConnection();
         Session_ME.AJ = false;
      }

      this.sender.AA();
      this.getKeyComplete = false;
      this.AN = null;
      this.AP = new Thread(new Class_HU(this, host, port));
      this.AP.start();
      if (LoadMap.specMap == 3 && GameScreen.player != null) {
         GameScreen.player.setAutoFire(true);
      }
   }
   public final void forceStopConnection() {
      Session_ME.AJ = true;
      this.AD = false;
      this.AC = false;
      if (this.AP != null && this.AP.isAlive()) {
         this.AP.interrupt();
         this.AP = null;
      }
      this.close();
      // KHÔNG reset AJ=false ở đây – caller tự reset nếu cần tạo kết nối mới
   }

   public static void AB(Message m) {
      GameMidlet.AE();
      AB.onMessage(m);
   }

   public final void sendMessage(Message m) {
      this.sender.AA(m);
   }

   private synchronized void doSendMessage(Message m) {
      //this.checkConnectionTimeout();
      
      if (!this.AC || this.dos == null) {
         return;
      }
      
      byte[] var2 = m.getData();

      try {
         byte var5;
         if (this.getKeyComplete) {
            var5 = this.writeKey(m.command);
            this.dos.writeByte(var5);
         } else {
            this.dos.writeByte(m.command);
         }

         if (var2 != null) {
            int var6 = var2.length;
            if (this.getKeyComplete) {
               byte var3 = this.writeKey((byte)(var6 >> 8));
               this.dos.writeByte(var3);
               var5 = this.writeKey((byte)var6);
               this.dos.writeByte(var5);
            } else {
               this.dos.writeShort(var6);
            }

            if (this.getKeyComplete) {
               for(int var7 = 0; var7 < var2.length; ++var7) {
                  var2[var7] = this.writeKey(var2[var7]);
               }
            }

            this.dos.write(var2);
            this.sendByteCount += 5 + var2.length;
         } else {
            this.dos.writeShort(0);
            this.sendByteCount += 5;
         }

         this.dos.flush();
         this.updateHeartbeat();
      } catch (IOException var4) {
      }
   }

   private byte readKey(byte result) {
      byte[] var10000 = this.key;
      byte b2 = this.curR;
      this.curR = (byte)(b2 + 1);
      result = (byte)(var10000[b2] & 255 ^ result & 255);
      if (this.curR >= this.key.length) {
         this.curR = (byte)(this.curR % this.key.length);
      }
      return result;
   }

   private byte writeKey(byte var1) {
      byte[] var10000 = this.key;
      byte var10003 = this.curW;
      this.curW = (byte)(var10003 + 1);
      var1 = (byte)(var10000[var10003] & 255 ^ var1 & 255);
      if (this.curW >= this.key.length) {
         this.curW = (byte)(this.curW % this.key.length);
      }

      return var1;
   }

   public final void close() {
      this.cleanNetwork();
   }
   public void checkConnectionTimeout() {
      long currentTime = System.currentTimeMillis();
      if (!this.AC) {
         return;
      }
      // Bỏ ngắt kết nối heartbeat timeout khi treo máy (AFK)
      // Socket hỏng/mất mạng thật sự đã được thread đọc dữ liệu (Class_HT) bắt lỗi IOException tự đóng
      
      // Nếu kết nối mở nhưng chưa nhận key xác thực sau CONNECTION_TIMEOUT → ngắt
      if (this.currentimestamp > 0 &&
          (currentTime - this.currentimestamp) > CONNECTION_TIMEOUT &&
          !this.getKeyComplete) {
         this.forceDisconnect("key handshake timeout");
      }
   }
   
   private void forceDisconnect(String reason) {
      this.isConnectionAlive = false;
      this.close();
      if (Session_ME.AB != null) {
         Session_ME.AB.AA();
      }
   }
   
   public void updateHeartbeat() {
      this.lastHeartbeat = System.currentTimeMillis();
      this.isConnectionAlive = true;
   }
   
   public void updateActivity() {
      this.lastActivity = System.currentTimeMillis();
      this.isConnectionAlive = true;
   }
   
   public boolean isConnectionAlive() {
      return this.isConnectionAlive && this.AC;
   }
   
   public void sendKeepAlive() {
//      if (this.AC && this.dos != null) {
//         try {
//            this.dos.writeByte((byte)0xFF);
//            this.dos.flush();
//            this.updateActivity();
//         } catch (Exception e) {
//         }
//      }
   }

   private void cleanNetwork() {
      this.key = null;
      this.curR = 0;
      this.curW = 0;

      try {
         this.AC = false;
         this.AD = false;
         if (this.AN != null) {
            this.AN.close();
            this.AN = null;
         }

         if (this.dos != null) {
            this.dos.close();
            this.dos = null;
         }

         if (this.AA != null) {
            this.AA.close();
            this.AA = null;
         }

         this.AE = null;
         if (this.AP != null && this.AP.isAlive()) {
            this.AP.interrupt();
            this.AP = null;
         }
      } catch (Exception var2) {
      }
   }

   static TcpClient AA(Session_ME var0) {
      return var0.AN;
   }

   static void AA(Session_ME var0, TcpClient var1) {
      var0.AN = var1;
   }

   static void AA(Session_ME var0, DataOutputStream var1) {
      var0.dos = var1;
   }

   static Sender AB(Session_ME var0) {
      return var0.sender;
   }

   static void AA(Session_ME var0, Message var1) {
      var0.doSendMessage(var1);
   }

   static void AC(Session_ME var0) {
      var0.cleanNetwork();
   }

   static byte AA(Session_ME var0, byte var1) {
      return var0.readKey(var1);
   }

   static Message AB(Session_ME var0, byte var1) {
       try {
           byte var2 = (var0 = var0).readKey(var0.AA.readByte());
           byte var3 = var0.readKey(var0.AA.readByte());
           byte var4 = var0.readKey(var0.AA.readByte());
           byte var5 = var0.readKey(var0.AA.readByte());
           int var7;
           byte[] var8 = new byte[var7 = (var2 & 255) << 24 | (var3 & 255) << 16 | (var4 & 255) << 8 | var5 & 255];
           int var9 = 0;
           int var10 = 0;
           
           int var6;
           while(var9 != -1 && var10 < var7) {
               if ((var9 = var0.AA.read(var8, var10, var7 - var10)) > 0) {
                   var10 += var9;
                   var0.AF += var10 + 5;
               }
           }
           
           if (var0.getKeyComplete) {
               for(var6 = 0; var6 < var8.length; ++var6) {
                   var8[var6] = var0.readKey(var8[var6]);
               }
           }
           
           return new Message(var1, var8);
       } catch (IOException ex) {
       }
       return null;
   }
}
