final class Class_HT implements Runnable {
   private Session_ME AA;

   Class_HT(Session_ME var1) {
      this.AA = var1;
   }

   public final void run() {
      while(true) {
         try {
            Message var1;
            if (this.AA.AB() && (var1 = this.AA()) != null) {
               try {
                  if (var1.command != -27) {
                     Session_ME var8 = this.AA;
                     Session_ME.AB(var1);
                     continue;
                  }

                  Message var2 = var1;
                  Class_HT var7 = this;
                  byte var3 = var1.reader().readByte();
                  this.AA.key = new byte[var3];

                  int var4;
                  for(var4 = 0; var4 < var3; ++var4) {
                     var7.AA.key[var4] = var2.reader().readByte();
                  }

                  for(var4 = 0; var4 < var7.AA.key.length - 1; ++var4) {
                     byte[] var10000 = var7.AA.key;
                     var10000[var4 + 1] ^= var7.AA.key[var4];
                  }

                  var7.AA.getKeyComplete = true;
               } catch (Exception var5) {
                  var5.printStackTrace();
               }
               continue;
            }
         } catch (Exception var6) {
         }

         if (this.AA.AC) {
            if (Session_ME.AB != null) {
               if (System.currentTimeMillis() - this.AA.AI > 500L) {
                  Session_ME.AB.AB();
               } else {
                  Session_ME.AB.AA();
               }
            }

            if (Session_ME.AA(this.AA) != null) {
               Session_ME.AC(this.AA);
            }
         }

         return;
      }
   }

   private Message AA() {
      try {
         ++Session_ME.AK;
         byte var1 = this.AA.AA.readByte();
         if (this.AA.getKeyComplete) {
            var1 = Session_ME.AA(this.AA, var1);
         }

         if (var1 != -39 && var1 != -101 && var1 != -93 && var1 != 76) {
            int var2;
            if (this.AA.getKeyComplete) {
               byte var3 = this.AA.AA.readByte();
               byte var4 = this.AA.AA.readByte();
               var2 = (Session_ME.AA(this.AA, var3) & 255) << 8 | Session_ME.AA(this.AA, var4) & 255;
            } else {
               var2 = this.AA.AA.readUnsignedShort();
            }

            byte[] var7 = new byte[var2];
            int var8 = 0;
            int var5 = 0;

            while(var8 != -1 && var5 < var2) {
               if ((var8 = this.AA.AA.read(var7, var5, var2 - var5)) > 0) {
                  var5 += var8;
                  Session_ME var10000 = this.AA;
                  var10000.AF += var5 + 5;
               }
            }

            if (this.AA.getKeyComplete) {
               for(var2 = 0; var2 < var7.length; ++var2) {
                  var7[var2] = Session_ME.AA(this.AA, var7[var2]);
               }
            }

            return new Message(var1, var7);
         } else {
            return Session_ME.AB(this.AA, var1);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }
}
