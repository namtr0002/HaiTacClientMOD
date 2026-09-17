public final class Class_FA {
   public byte AA = -1;
   public byte AB;
   public short[] AC;

   public final void AA(byte var1) {
      this.AA = var1;
      this.AC = new short[8];

      int var2;
      for(var2 = 0; var2 < this.AC.length; ++var2) {
         MainItem var3;
         if ((var3 = (MainItem)GameScreen.player.LR.get("" + var2)) != null) {
            this.AC[var2] = var3.ID;
         } else {
            this.AC[var2] = -1;
         }
      }

      for(var2 = 0; var2 < Player.QK.size(); ++var2) {
         ((Class_FA)Player.QK.elementAt(var2)).AB = 0;
      }

      this.AB = 1;
      Interface_Game.addInfoPlayerNormal(T.WT[var1] + T.RB, mFont.tahoma_7_yellow);
   }

   public final void AA() {
      for(int var1 = 0; var1 < this.AC.length; ++var1) {
         if (this.AC[var1] >= 0) {
            boolean var2 = false;

            for(int var3 = 0; var3 < Player.vecInventory.size() && !var2; ++var3) {
               MainItem var4;
               if ((var4 = (MainItem)Player.vecInventory.elementAt(var3)).typeObject == 3 && var4.ID == this.AC[var1]) {
                  GlobalService.getInstance().AC((short)this.AC[var1], (byte)3);
                  var2 = true;
               }
            }
         }
      }

      this.AB = 1;
      Interface_Game.addInfoPlayerNormal(T.WT[this.AA] + T.RA, mFont.tahoma_7_yellow);
   }

   public static void AA(boolean var0) {
      if (Player.QK.size() != 0 || var0) {
         int var1;
         MainItem var2;
         int var3;
         Class_FA var4;
         int var5;
         for(var1 = 0; var1 < Player.vecInventory.size(); ++var1) {
            (var2 = (MainItem)Player.vecInventory.elementAt(var1)).BC = -1;
            if (!var0 && var2.typeObject == 3) {
               for(var3 = 0; var3 < Player.QK.size(); ++var3) {
                  var4 = (Class_FA)Player.QK.elementAt(var3);

                  for(var5 = 0; var5 < var4.AC.length; ++var5) {
                     if (var2.ID == var4.AC[var5]) {
                        if (var2.BC == -1) {
                           var2.BC = var4.AA;
                        } else {
                           var2.BC = 2;
                        }
                     }
                  }
               }
            }
         }

         for(var1 = 0; var1 < 8; ++var1) {
            if ((var2 = (MainItem)GameScreen.player.LR.get("" + var1)) != null) {
               var2.BC = -1;
               if (!var0 && var2.typeObject == 3) {
                  for(var3 = 0; var3 < Player.QK.size(); ++var3) {
                     var4 = (Class_FA)Player.QK.elementAt(var3);

                     for(var5 = 0; var5 < var4.AC.length; ++var5) {
                        if (var2.ID == var4.AC[var5]) {
                           if (var2.BC == -1) {
                              var2.BC = var4.AA;
                           } else {
                              var2.BC = 2;
                           }
                        }
                     }
                  }
               }
            }
         }

      }
   }
}
