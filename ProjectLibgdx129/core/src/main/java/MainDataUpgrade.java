import java.io.DataInputStream;

public final class MainDataUpgrade {
   public short[][] AA;

   public static void LoadDataUpgrade(DataInputStream var0, boolean var1) {
      if (var0 == null) {
         GlobalService.getInstance().get_DATA((byte)12);
      } else {
         try {
            byte var2;
            ScreenUpgrade.AY = new MainDataUpgrade[var2 = var0.readByte()];

            for(int var3 = 0; var3 < var2; ++var3) {
               ScreenUpgrade.AY[var3] = new MainDataUpgrade();
               MainDataUpgrade[] var10000 = ScreenUpgrade.AY;
               var0.readByte();
               var10000 = ScreenUpgrade.AY;
               var0.readShort();
               var10000 = ScreenUpgrade.AY;
               var0.readByte();
               var10000 = ScreenUpgrade.AY;
               var0.readInt();
               var10000 = ScreenUpgrade.AY;
               var0.readInt();
               var10000 = ScreenUpgrade.AY;
               var0.readShort();
               var10000 = ScreenUpgrade.AY;
               var0.readShort();
               byte var4 = var0.readByte();
               ScreenUpgrade.AY[var3].AA = new short[var4][];

               for(int var5 = 0; var5 < var4; ++var5) {
                  ScreenUpgrade.AY[var3].AA[var5] = new short[3];
                  ScreenUpgrade.AY[var3].AA[var5][2] = var0.readByte();
                  ScreenUpgrade.AY[var3].AA[var5][0] = var0.readByte();
                  ScreenUpgrade.AY[var3].AA[var5][1] = var0.readShort();
               }
            }

            if (var1) {
               SaveRms.AA(GlobalService.AL = var0.readShort(), "VerdataUpgradeSave");
            }

            var0.close();
         } catch (Exception var6) {
         }
      }
   }
}
