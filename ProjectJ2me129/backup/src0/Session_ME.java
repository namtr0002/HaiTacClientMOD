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
   long AI;
   public static boolean AJ;
   private static mVector recieveMsg = new mVector();
   public static int AK = 0;

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
      if (!this.AC && !this.AD) {
         this.sender.AA();
         this.getKeyComplete = false;
         this.AN = null;
         this.AP = new Thread(new Class_HU(this, host, port));
         System.out.println("connect server " + host + ":" + port);
         this.AP.start();
         if (LoadMap.specMap == 3) {
            GameScreen.player.setAutoFire(true);
         }

      }
   }

   public static void AB(Message m) {
      GameMidlet.AE();
      AB.onMessage(m);
   }

   public final void sendMessage(Message m) {
      this.sender.AA(m);
   }

   private synchronized void doSendMessage(Message m) {
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
      } catch (IOException var4) {
         var4.printStackTrace();
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

         System.gc();
      } catch (Exception var2) {
         var2.printStackTrace();
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
                   var6 = instance.AF + instance.sendByteCount;
                   (new StringBuffer(String.valueOf(var6 / 1024))).append(".").append(var6 % 1024 / 102).append("Kb").toString();
               }
           }
           
           if (var0.getKeyComplete) {
               for(var6 = 0; var6 < var8.length; ++var6) {
                   var8[var6] = var0.readKey(var8[var6]);
               }
           }
           
           return new Message(var1, var8);
       } catch (IOException ex) {
           ex.printStackTrace();
       }
       return null;
   }
}
