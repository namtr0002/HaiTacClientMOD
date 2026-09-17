import java.io.DataInputStream;

public final class CatalogyMonster {
   public int idCat;
   public int AB;
   public int AC;
   public byte AD;
   public byte AE;
   public byte AF;
   public short AG;
   public short AH;
   public short AI;
   public short AJ;
   public short[] AK;
   public String AL = "";
   public boolean AM = false;
   public long AN = 0L;

   public CatalogyMonster(int var1) {
      this.idCat = var1;
   }

   private CatalogyMonster(int var1, int var2, int var3, byte var4, String var5, byte var6, short var7, byte var8) {
      this.idCat = var1;
      this.AB = var2;
      this.AC = var3;
      this.AD = var4;
      this.AL = var5;
      this.AE = var6;
      this.AI = var7;
      this.AF = var8;
   }

   private void AA(short var1, short var2, short[] var3) {
      this.AG = var1;
      this.AH = var2;
      this.AK = var3;
   }

   public static void AA(DataInputStream var0, boolean var1) {
      try {
         if (var0 == null) {
            GlobalService.getInstance().get_DATA((byte)15);
         } else {
            short var2 = var0.readShort();

            for(int var3 = 0; var3 < var2; ++var3) {
               short var4 = var0.readShort();
               String var5 = var0.readUTF();
               short var6 = var0.readShort();
               short var7 = var0.readShort();
               int var8 = var0.readInt();
               byte var9 = var0.readByte();
               byte var10 = var0.readByte();
               byte var11 = var0.readByte();
               CatalogyMonster var13 = new CatalogyMonster(var4, var6, var8, var9, var5, var10, var7, var11);
               if (var10 != 1) {
                  var13.AJ = var0.readShort();
               } else {
                  var6 = var0.readShort();
                  var7 = var0.readShort();
                  byte var14;
                  short[] var15 = new short[var14 = var0.readByte()];

                  int var16;
                  for(var16 = 0; var16 < var15.length; ++var16) {
                     var15[var16] = -1;
                  }

                  for(var16 = 0; var16 < var14; ++var16) {
                     if (var0.readByte() == 1) {
                        var15[var16] = var0.readShort();
                     }
                  }

                  var13.AA(var6, var7, var15);
               }

               var13.AM = true;
               MainMonster.AL.put("" + var4, var13);
            }

            LoadMapScreen.isLoadDataMon = true;
            if (var1) {
               SaveRms.AA(GlobalService.AD = var0.readShort(), "VerdataMon");
            }

            var0.close();
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }
   }

   public static void AA(DataInputStream var0) {
      try {
         short var1 = var0.readShort();
         String var2 = var0.readUTF();
         short var3 = var0.readShort();
         short var4 = var0.readShort();
         int var5 = var0.readInt();
         byte var6 = var0.readByte();
         byte var7 = var0.readByte();
         byte var8 = var0.readByte();
         CatalogyMonster var10 = new CatalogyMonster(var1, var3, var5, var6, var2, var7, var4, var8);
         if (var7 == 1) {
            var3 = var0.readShort();
            var4 = var0.readShort();
            byte var11;
            short[] var12 = new short[var11 = var0.readByte()];

            int var13;
            for(var13 = 0; var13 < var12.length; ++var13) {
               var12[var13] = -1;
            }

            for(var13 = 0; var13 < var11; ++var13) {
               if (var0.readByte() == 1) {
                  var12[var13] = var0.readShort();
               }
            }

            var10.AA(var3, var4, var12);
         } else {
            var10.AJ = var0.readShort();
         }

         var10.AM = true;
         MainMonster.AL.put("" + var1, var10);
         var0.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      }
   }
}
