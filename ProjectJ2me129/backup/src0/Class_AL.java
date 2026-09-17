public final class Class_AL extends MsgDialog {
   private static byte AA = 4;
   private byte AB = 0;
   private short[][] AC;
   private byte[] BB;
   private iCommand BC;
   private iCommand BD;
   private boolean BE = false;
   private mVector BF = new mVector();

   public Class_AL() {
   }

   public Class_AL(byte var1) {
      this.AB = var1;
   }

   public final void AA(short[][] var1) {
      super.wDia = 220;
      super.hDia = 200;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.hh - super.hDia / 2;
      this.AC = var1;
      super.cmdList.removeAllElements();
      this.BB = new byte[this.AC.length];
      this.BC = new iCommand(T.QH, 0, this);
      this.BD = new iCommand(T.QI, 1, this);
      super.AG = new iCommand(T.close, -1, this);
      if (GameCanvas.isTouch) {
         super.AG.setPos(super.AX + super.wDia - 13, super.AY + 11, MainTab.fraCloseTab, "");
         super.cmdList.addElement(super.AG);
         if (this.BD != null) {
            this.BD.setPos(GameCanvas.BG - 3, MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2, (FrameImage)null, this.BD.caption);
            super.cmdList.addElement(this.BD);
         }

         if (this.BC != null) {
            this.BC.setPos(MotherCanvas.w - GameCanvas.BG + 3, MotherCanvas.h - iCommand.hButtonCmdNor / 2 + 2, (FrameImage)null, this.BC.caption);
            super.cmdList.addElement(this.BC);
         }

         super.backCMD = super.AG;
         super.DF = this.BD;
         super.DE = this.BC;
      } else {
         super.DA = this.BC;
         super.center = this.BD;
         super.DB = super.AG;
      }

      this.BF.removeAllElements();
      this.BE = false;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         GameCanvas.end_Dialog();
         return;
      case 0:
         GlobalService.getInstance().AF(this.AB, (byte)0);
         return;
      case 1:
         GlobalService.getInstance().AF(this.AB, (byte)1);
      default:
      }
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = super.AY + super.hDia / 2;
      int var3 = super.AX + super.wDia / 2;
      int var5;
      int var6;
      if (this.AB == AA) {
         this.AE(var1, super.AX, super.AY, super.wDia, super.hDia, super.wDia);
      } else {
         int var8 = super.hDia;
         int var7 = super.wDia;
         var6 = super.AY;
         var5 = super.AX;
         mGraphics var4 = var1;
         var1.translate(var5, var6);
         int var9 = var7 - 25;
         int var10 = var8 - 25;

         int var11;
         for(var11 = 25; var11 < var9; var11 += 25) {
            var4.drawRegion((mImage)AvMain.imgDialogTrangTri[6], var11, var10, 0);
            var4.drawRegion((mImage)AvMain.imgDialogTrangTri[1], var11, 0, 0);
         }

         for(var11 = 25; var11 < var10; var11 += 25) {
            var4.drawRegion((mImage)AvMain.imgDialogTrangTri[3], 0, var11, 0);
            var4.drawRegion((mImage)AvMain.imgDialogTrangTri[4], var9, var11, 0);
         }

         var4.drawRegion((mImage)AvMain.imgDialogTrangTri[7], var9, var10, 0);
         var4.drawRegion((mImage)AvMain.imgDialogTrangTri[5], 0, var10, 0);
         var4.drawRegion((mImage)AvMain.imgDialogTrangTri[2], var9, 0, 0);
         var4.drawRegion((mImage)AvMain.imgDialogTrangTri[0], 0, 0, 0);
         var9 = var7 - 20;
         var10 = var8 - 20;
         var4.AD(20, 20, var9 - 20, var10 - 20);
         mGraphics.AC();
         mGraphics.AD();

         for(var11 = 20; var11 < var10; var11 += 36) {
            for(int var12 = 20; var12 < var9; var12 += 36) {
               var4.drawRegion((mImage)AvMain.imgDialogTrangTri[8], var12, var11, 0);
            }
         }

         mGraphics.restoreCanvas();
         GameCanvas.resetTrans(var4);
         var4.translate(var5, var6);
         var9 = var7 - 25 - 40;
         var10 = var8 - 25 - 40;

         for(var11 = 65; var11 < var9; var11 += 40) {
            var4.drawRegion((mImage)AvMain.imgDialogTrangTri[15], var11, var10, 0);
            var4.drawRegion((mImage)AvMain.imgDialogTrangTri[10], var11, 25, 0);
         }

         for(var11 = 65; var11 < var10; var11 += 40) {
            var4.drawRegion((mImage)AvMain.imgDialogTrangTri[12], 25, var11, 0);
            var4.drawRegion((mImage)AvMain.imgDialogTrangTri[13], var9, var11, 0);
         }

         var4.drawRegion((mImage)AvMain.imgDialogTrangTri[16], var9, var10, 0);
         var4.drawRegion((mImage)AvMain.imgDialogTrangTri[14], 25, var10, 0);
         var4.drawRegion((mImage)AvMain.imgDialogTrangTri[11], var9, 25, 0);
         var4.drawRegion((mImage)AvMain.imgDialogTrangTri[9], 25, 25, 0);
         var4.translate(-var5, -var6);
      }

      int var13;
      if (this.BE) {
         for(var13 = 0; var13 < this.BF.size(); ++var13) {
            ((Effect_End)this.BF.elementAt(var13)).paint(var1);
         }
      }

      for(var13 = this.AC.length - 1; var13 >= 0; --var13) {
         MainImage var14;
         if ((var14 = ObjectData.getImageAll(this.AC[var13][0], ObjectData.hashImageItemOther, (short)9000)) != null && var14.img != null) {
            if (this.BE && this.AC[var13][5] < 0) {
               var6 = var14.AC / this.AC[var13][1];
               var1.drawRegion(var14.img, 0, GameCanvas.BJ % this.AC[var13][1] * var6, var14.AB, var6, 0, var3 + this.AC[var13][2], var2 + this.AC[var13][3], this.AC[var13][4]);
            } else if (!this.BE && this.AC[var13][5] >= 0) {
               if (this.AC[var13][1] > 2) {
                  var6 = var14.AC / this.AC[var13][1];
                  var1.drawRegion(var14.img, 0, GameCanvas.BJ % this.AC[var13][1] * var6, var14.AB, var6, 0, var3 + this.AC[var13][2], var2 + this.AC[var13][3], this.AC[var13][4]);
               } else {
                  var6 = var14.AC / this.AC[var13][1];
                  byte var17 = 0;
                  if (this.AC[var13][1] == 2 && this.BB[var13] == this.AC[var13][5]) {
                     var17 = 1;
                  }

                  var1.drawRegion(var14.img, 0, var17 * var6, var14.AB, var6, 0, var3 + this.AC[var13][2], var2 + this.AC[var13][3], this.AC[var13][4]);
                  if (this.AC[var13][5] > 1 && this.BB[var13] < this.AC[var13][5]) {
                     mFont.tahoma_7b_black.drawString(var1, this.BB[var13] + "/" + this.AC[var13][5], var3 + this.AC[var13][2] + this.AC[var13][6], var2 + this.AC[var13][3] + this.AC[var13][7], 2);
                  }
               }
            }
         }
      }

      MainImage var15;
      if (this.BE && (var15 = ObjectData.getImageAll((short)192, ObjectData.HashImageEffClient, (short)24000)) != null && var15.img != null) {
         var5 = var15.AC / 2;
         var1.drawRegion(var15.img, 0, GameCanvas.BJ / 2 % 2 * var5, var15.AB, var5, 0, var3 - 71, var2 - 58, 3);
         var1.drawRegion(var15.img, 0, GameCanvas.BJ / 2 % 2 * var5, var15.AB, var5, 0, var3 + 70, var2 - 58, 3);
      }

      GameCanvas.resetTrans(var1);
      if (super.cmdList != null) {
         for(var13 = 0; var13 < super.cmdList.size(); ++var13) {
            iCommand var16;
            (var16 = (iCommand)super.cmdList.elementAt(var13)).paint(var1, var16.xCmd, var16.yCmd);
         }
      }

      super.AD(var1);
   }

   public final void update() {
      super.update();
      int var1;
      if (this.BE && GameCanvas.gameTick % 6 == 0) {
         var1 = CRes.random(5);

         for(int var2 = 0; var2 < var1; ++var2) {
            int var3 = CRes.random(super.AX + 50, super.AX + super.wDia - 50);
            int var4 = CRes.random(super.AY + 50, super.AY + super.wDia - 50);
            this.BF.addElement(new Effect_End((short)169, (byte)0, var3, var4, (byte)0));
         }
      }

      for(var1 = 0; var1 < this.BF.size(); ++var1) {
         Effect_End var5;
         (var5 = (Effect_End)this.BF.elementAt(var1)).update();
         if (var5.isStop) {
            this.BF.removeElement(var5);
            --var1;
         }
      }

   }

   public final void AA(short[] var1, byte[] var2) {
      if (var1 != null && var1.length != 0) {
         int var4;
         for(int var3 = 0; var3 < var1.length; ++var3) {
            for(var4 = 0; var4 < this.AC.length; ++var4) {
               if (this.AC[var4][0] == var1[var3]) {
                  if (var2[var3] > this.AC[var4][5]) {
                     this.BB[var4] = (byte)this.AC[var4][5];
                  } else {
                     this.BB[var4] = var2[var3];
                  }

                  var2[var3] -= this.BB[var4];
                  if (var2[var3] == 0) {
                     break;
                  }
               }
            }
         }

         boolean var5 = true;

         for(var4 = 0; var4 < this.AC.length; ++var4) {
            if (this.BB[var4] < this.AC[var4][5]) {
               var5 = false;
               break;
            }
         }

         this.BE = var5;
      } else {
         this.BE = false;
      }
   }
}
