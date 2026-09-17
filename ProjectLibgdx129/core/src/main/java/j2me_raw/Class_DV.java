public final class Class_DV extends MainItem {
   public Class_DV(short var1, short var2, short var3, String var4, byte var5) {
      super.ID = var1;
      super.idIcon = (short)(var2 + 500);
      super.AN = var3;
      super.typeObject = 102;
      super.name = var4;
      super.namepaint = var4;
      super.AW = var5;
   }

   public final MainImage AD() {
      return ObjectData.getImageAll(super.idIcon, ObjectData.hashImageBoat, (short)8000);
   }

   public static void AA(mGraphics var0, short var1, int var2, int var3, int var4, int var5, int var6) {
      int var7;
      if (var2 >= 100) {
         int var10 = GameCanvas.gameTick / 6 % 2;
         var7 = var6 == 2 ? 4 : -4;
         Boat.fraPirateUnity.drawFrame((var2 - 100 << 1) + var10, var3 + var7, var4 + Boat.AK - 57 - var5, var6, 33, var0);
      } else {
         MainImage var9;
         if ((var9 = ObjectData.getImageAll(var1, ObjectData.hashImageBoat, (short)8000)) != null && var9.img != null) {
            var7 = var6 == 2 ? 11 : -11;
            if (var2 == 0) {
               var0.drawRegion(var9.img, 0, 0, var9.AB, var9.AC + var5, var6, var3, var4 + Boat.AK, 33);
            }

            if (var2 == 1) {
               var0.drawRegion(var9.img, 0, 0, var9.AB, var9.AC, var6, var3 + var7, var4 + Boat.AK - 15 - var5, 33);
            }

            if (var2 == 2) {
               int var8 = GameCanvas.gameTick / 6 % 2;
               var0.drawRegion(var9.img, 0, var9.AC / 2 * var8, var9.AB, var9.AC / 2, var6, var3 + var7, var4 + Boat.AK - 23 - var5, 33);
            }

            if (var2 == 3) {
               var0.drawRegion(var9.img, 0, 0, var9.AB, var9.AC + var5, var6, var3, var4 + Boat.AK, 33);
            }
         }

      }
   }

   public final mVector AB(byte var1) {
      mVector var2 = new mVector();
      if (super.AF == 0 && super.AJ == 0) {
         var2.addElement(Class_BH.CH);
      } else {
         var2.addElement(Class_BH.CD);
      }

      return var2;
   }
}
