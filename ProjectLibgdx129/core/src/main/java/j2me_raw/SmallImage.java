import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public final class SmallImage {
   private static int[][] AE;
   public short x;
   public short y;
   public short w;
   public short h;

   public static void loadBigRMS() {
   }

   public SmallImage(int var1, int var2, int var3, int var4, int var5) {
      this.x = (short)var2;
      this.y = (short)var3;
      this.w = (short)var4;
      this.h = (short)var5;
   }

   public SmallImage() {
   }

   public static void AA(byte[] var0) {
      try {
         short var1;
         DataInputStream var4;
         if ((var1 = (var4 = new DataInputStream(new ByteArrayInputStream(var0))).readShort()) > 600) {
            var1 = 600;
         }

         AE = new int[var1][];

         for(int var2 = 0; var2 < var1; ++var2) {
            AE[var2] = new int[5];
            AE[var2][0] = var4.readUnsignedByte();
            AE[var2][1] = var4.readShort();
            AE[var2][2] = var4.readShort();
            AE[var2][3] = var4.readShort();
            AE[var2][4] = var4.readShort();
         }

      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public static void AA(mGraphics var0, int var1, int var2, int var3, int var4, int var5) {
      MainImage var6;
      if ((var6 = ObjectData.getImageAll((short)var1, ObjectData.HashImageCharPart, (short)10000)).img != null) {
         var0.drawRegion(var6.img, 0, 0, var6.AB, var6.AC, var4, var2, var3, var5);
      }

   }
}
