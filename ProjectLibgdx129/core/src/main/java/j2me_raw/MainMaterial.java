public final class MainMaterial extends MainItem {
   public MainMaterial(byte var1, String var2, byte var3, byte var4, int var5, short var6, byte var7) {
      super.ID = var1;
      super.name = var2;
      super.typeMaterial = var3;
      super.idIcon = var4;
      super.AF = var5;
      super.AJ = var6;
      super.BO = var7;
      if (super.LvUpgrade > 0) {
         super.namepaint = var2 + " +" + super.LvUpgrade;
      } else {
         super.namepaint = var2;
      }

      if (super.typeMaterial >= 0 && super.typeMaterial < T.WH.length) {
         super.info = T.WH[super.typeMaterial];
      } else {
         super.info = T.GL;
      }
   }

   public MainMaterial(byte var1, byte var2, String var3, byte var4, byte var5, short var6, int var7, short var8, byte var9) {
      super.typeObject = 7;
      super.ID = var2;
      super.name = var3;
      super.typeMaterial = var4;
      super.idIcon = var5;
      super.numPotion = var6;
      super.AF = var7;
      super.AJ = var8;
      super.BO = var9;
      super.indexSort = 1;
      if (super.LvUpgrade > 0) {
         super.namepaint = var3 + " +" + super.LvUpgrade;
      } else {
         super.namepaint = var3;
      }
   }

   public final MainImage AD() {
      return ObjectData.getImageAll(super.idIcon, ObjectData.hashImageMaterialPotion, (short)6500);
   }

   public final mVector AA(byte var1) {
      mVector var2 = new mVector();
      if (var1 == 0) {
         var2.addElement(GameCanvas.tabInven.CM);
      } else if (var1 == 1) {
         var2.addElement(GameCanvas.tabInven.CH);
      } else if (var1 == 2) {
         var2.addElement(GameCanvas.tabInven.CJ);
         var2.addElement(GameCanvas.tabInven.CL);
      } else if (var1 == 5) {
         if (super.ID != 4 && super.ID != 10) {
            boolean var10000;
            label34: {
               MainMaterial var4 = this;
               if (MainItem.CM != null) {
                  for(int var3 = 0; var3 < MainItem.CM.length; ++var3) {
                     if (var4.ID == MainItem.CM[var3]) {
                        var10000 = true;
                        break label34;
                     }
                  }
               }

               var10000 = false;
            }

            if (!var10000) {
               return var2;
            }
         }

         var2.addElement(GameCanvas.CP.CO);
      }

      return var2;
   }

   public final mVector AB() {
      if (!this.AI() && super.typeMaterial != 3 && super.typeMaterial != 7) {
         return null;
      } else {
         mVector var1 = new mVector();
         if (ScreenUpgrade.instance != null) {
            var1.addElement(ScreenUpgrade.instance.AS);
         }

         return var1;
      }
   }

   public final mVector AB(byte var1) {
      mVector var2;
      (var2 = new mVector()).addElement(Class_BH.CE);
      return var2;
   }

   public final mVector getActionChest() {
      mVector var1;
      (var1 = new mVector()).addElement(TabChest.cmdGetPotion);
      var1.addElement(TabChest.cmdChucnang);
      return var1;
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      super.AA(var1, var2, var3, var4);
      this.AE(var1, var2, var3, var4);
   }

   public final boolean AI() {
      return super.typeMaterial == 2 || super.typeMaterial == 4;
   }
}
