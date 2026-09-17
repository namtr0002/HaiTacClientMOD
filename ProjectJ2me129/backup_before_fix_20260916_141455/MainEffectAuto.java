import java.io.DataInputStream;
import java.util.Hashtable;

public final class MainEffectAuto {
   public static Hashtable hashTemEffAuto = new Hashtable();
   public Hashtable hashImage = new Hashtable();
   public MainFrameEff[] mFrame;
   public short[] mRunFrame;
   public mImage img;

   public MainEffectAuto() {
   }

   public MainEffectAuto(DataInputStream var1, byte[] var2) {
      this.AA(var1);
      this.img = mImage.AA(var2);
   }

   private void AA(DataInputStream var1) {
      try {
         byte var2 = var1.readByte();

         for(int var3 = 0; var3 < var2; ++var3) {
            MainPartImage var4 = new MainPartImage(var1.readByte(), var1.readByte(), var1.readByte(), var1.readByte(), var1.readByte());
            this.hashImage.put("" + var4.ID, var4);
         }

         short var8 = var1.readShort();
         this.mFrame = new MainFrameEff[var8];

         for(int var9 = 0; var9 < var8; ++var9) {
            var2 = var1.readByte();
            this.mFrame[var9] = new MainFrameEff();
            this.mFrame[var9].mpart = new Part[var2];

            for(int var5 = 0; var5 < var2; ++var5) {
               this.mFrame[var9].mpart[var5] = new Part();
               this.mFrame[var9].mpart[var5].x = var1.readShort();
               this.mFrame[var9].mpart[var5].y = var1.readShort();
               this.mFrame[var9].mpart[var5].idPartImage = var1.readByte();
            }
         }

         short var10 = var1.readShort();
         this.mRunFrame = new short[var10];

         for(int var7 = 0; var7 < var10; ++var7) {
            this.mRunFrame[var7] = var1.readShort();
         }

         var1.readByte();
         var1.readByte();
      } catch (Exception var6) {
         var6.printStackTrace();
      }
   }
}
