public final class Class_AT extends MsgDialog {
   private static int[] AB;
   private int AC;
   public static boolean AA = false;

   public final void AA() {
      AB = new int[7];

      for(int var1 = 0; var1 < AB.length; ++var1) {
         if (var1 != 0) {
            AB[var1] = 1;
         }
      }

      super.wDia = 180;
      super.AT = super.wDia;
      this.AC = 24;
      super.wItem = 26;
      if (super.wDia < 210) {
         super.wItem = 20;
      }

      super.hDia = 190;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.hh - super.hDia / 2 + 10;
      if (GameCanvas.isTouch) {
         super.AU = 28;
      }

      super.cmdList = new mVector();
      iCommand var2 = new iCommand(T.DA, 15, this);
      if (GameCanvas.isTouch) {
         var2.setPos(super.AX + super.wDia + 3, super.AY - 5, MainTab.fraCloseTab, "");
         super.backCMD = var2;
      } else {
         var2 = AvMain.AA(var2, 2);
         super.DB = var2;
      }

      super.cmdList.addElement(var2);
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = super.AY + this.AC / 2;
      int var3 = super.AX + 15;
      int var10002 = MotherCanvas.hw - super.AS / 2;
      int var10003 = super.AY - 7;
      int var10006 = super.AT;
      this.AD(var1, var10002, var10003, super.AS, super.hDia, 0);
      var1.AD(MotherCanvas.hw - super.AS / 2, 0, super.AS, MotherCanvas.h);
      mGraphics.AC();
      int var10000 = MotherCanvas.hw;
      var10000 = super.AS;
      var10000 = super.AS;
      var10000 = MotherCanvas.h;
      mGraphics.AD();
      if (GameCanvas.isKeyPressed()) {
         var10002 = var3 + 3;
         var10003 = var2 - this.AC / 4 - 1;
         int var8 = super.wDia - 36;
         int var7 = var10003;
         int var6 = var10002;
         var1.setColor(-2458);
         var6 -= super.AU;
         var1.fillRect(var6, var7 + super.idSelect * this.AC, var8 + (super.AU << 1), this.AC);
      }

      int var4;
      for(var4 = 0; var4 < T.WZ.length; ++var4) {
         int var5 = var3 + 5;
         mFont.tahoma_7b_black.drawString(var1, T.WZ[var4], var5 + 14, var2, 0);
         var1.drawRegion(AvMain.imgDaKham, 0, 0, 20, 20, 0, var5, var2 + 6, 3);
         if (AB[var4] == 1) {
            AvMain.fraCheck.drawFrame(2, var5, var2 + 5, 0, 3, var1);
         } else {
            AvMain.fraCheck.drawFrame(1, var5, var2 + 5, 0, 3, var1);
         }

         var2 += this.AC;
      }

      this.paintInfoHelp(var1);
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      if (super.cmdList != null) {
         for(var4 = 0; var4 < super.cmdList.size(); ++var4) {
            iCommand var9;
            (var9 = (iCommand)super.cmdList.elementAt(var4)).paint(var1, var9.xCmd, var9.yCmd);
         }
      }

   }

   public final void handleKeyPress() {
      if (GameCanvas.isKeyPressed(1)) {
         if (super.idSelect > 0) {
            --super.idSelect;
         }

         GameCanvas.clearKeyPressed(1);
      } else if (GameCanvas.isKeyPressed(3)) {
         if (super.idSelect < AB.length) {
            ++super.idSelect;
         }

         GameCanvas.clearKeyPressed(3);
      } else {
         if (GameCanvas.AL[5]) {
            GameCanvas.AB(5);
            if (AB[super.idSelect] == 1) {
               AB[super.idSelect] = 0;
               return;
            }

            AB[super.idSelect] = 1;
         }

      }
   }

   public final void updatePointer() {
      int var1 = super.AY + 5;
      int var2 = super.AX;

      for(int var3 = 0; var3 < AB.length; ++var3) {
         int var4;
         if (GameCanvas.AB(var4 = var2 + 5, var1, super.wDia - 25, this.AC)) {
            if (AB[var3] == 1) {
               AB[var3] = 0;
            } else {
               AB[var3] = 1;
            }

            GameCanvas.isPointerSelect = false;
            this.setInfoHelp(T.XA[var3]);
            super.idSelect = var3;
            return;
         }

         var1 += this.AC;
      }

      super.updatePointer();
   }

   public static boolean AA(int var0, String var1) {
      if (AB == null) {
         return false;
      } else if (var0 >= 0 && var0 < AB.length) {
         if (AB[0] == 1) {
            return true;
         } else if (AB[var0] == 1) {
            return false;
         } else if (var0 != 1) {
            return AB[var0] == 0;
         } else {
            if (Class_FN.AA) {
               for(var0 = 0; var0 < Player.RN.size(); ++var0) {
                  if (((Class_DS)Player.RN.elementAt(var0)).AC.compareTo(var1) == 0) {
                     return false;
                  }
               }
            } else if (!AA) {
               GlobalService.getInstance().AA((byte)2, (int)0);
               AA = true;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static void AK() {
      if (AB != null) {
         CRes.saveRMS("SUB_SPAM", new byte[]{(byte)AB[AB.length - 1]});
      }
   }
}
