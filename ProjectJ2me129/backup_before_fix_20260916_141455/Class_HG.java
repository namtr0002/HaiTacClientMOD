public final class Class_HG extends Class_GT {
   private Class_DS AB;
   public static Class_HG AA;
   private iCommand AC;
   private iCommand AD;
   private iCommand AE;
   private iCommand AF;
   private iCommand AG;
   private mVector AH = new mVector();
   private int AI;
   private int AJ;

   public Class_HG(byte var1, Class_DS var2) {
      super(var1);
      this.AB = var2;
      super.BO = AvMain.DG;
      super.BP = AvMain.DH;
      super.BM = MotherCanvas.hw - super.BO / 2;
      super.BN = MotherCanvas.hh - super.BP / 2;
      this.AI = 0;
      this.AC = new iCommand(T.close, -1, this);
      this.AD = new iCommand(T.BY, 0, this);
      this.AE = new iCommand(T.CD, 1, this);
      this.AF = new iCommand(T.UC, 2, this);
      this.AG = new iCommand(T.del, 3, this);
      this.AH.removeAllElements();
      if (var1 == 1) {
         this.AH.addElement(this.AD);
      } else if ((var1 == 5 || var1 == 4) && var2.BE == 0) {
         this.AH.addElement(this.AE);
      } else if (var1 == 4 && var2.BE == 1) {
         this.AH.addElement(this.AF);
      } else if (var1 == 5 && var2.BF == 1) {
         this.AH.addElement(this.AG);
      }

      this.AH.addElement(this.AC);
      this.AA(this.AH);
      if (this.AB.BC.hOne == 0) {
         this.AB.BC.BH();
         this.AJ = super.BP * 7 / 31 + (super.BP * 3 / 5 - super.BP * 7 / 31) / 2 + this.AB.BC.hOne / 2 - 7;
         if (this.AB.BC.hOne > 52) {
            this.AJ -= 3;
         }
      }

   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         Class_HF.AA.Show((MainScreen)GameCanvas.gameScr);
         return;
      case 0:
         GlobalService.getInstance().AL((byte)2, (short)this.AB.AG);
         GameCanvas.gameScr.Show();
         return;
      case 1:
         GlobalService.getInstance().AE(super.BL, (short)this.AB.AG, (byte)1);
         GameCanvas.gameScr.Show();
         return;
      case 2:
         GlobalService.getInstance().AE(super.BL, (short)this.AB.AG, (byte)2);
         GameCanvas.gameScr.Show();
         return;
      case 3:
         GlobalService.getInstance().AE((byte)5, (short)this.AB.AG, (byte)3);
      default:
      }
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      int var5 = super.BN;
      int var4 = super.BM;
      if (GameCanvas.isTouch) {
         var1.drawRegion((mImage)AvMain.imgDemoWanted, var4, var5, 0);
      } else {
         int var10 = super.BP;
         int var9 = super.BO;
         mGraphics var6 = var1;
         var1.translate(var4, var5);
         var9 -= 30;
         var10 -= 30;
         int var11 = 0;

         int var12;
         for(var12 = 30; var12 < var10; var12 += 30) {
            for(int var13 = 30; var13 < var9; var13 += 30) {
               var6.drawRegion((mImage)AvMain.imgGiua[var11], var13, var12, 0);
               ++var11;
               if (var11 >= AvMain.imgGiua.length) {
                  var11 = 0;
               }
            }
         }

         var11 = 0;
         var12 = 30;

         while(var12 < var9) {
            var6.drawRegion((mImage)AvMain.imgDuoi[var11], var12, var10, 0);
            var6.drawRegion((mImage)AvMain.imgTren[var11], var12, 0, 0);
            var12 += mImage.getImageWidth(AvMain.imgTren[var11].image);
            ++var11;
            if (var11 >= AvMain.imgTren.length) {
               var11 = 0;
            }
         }

         var11 = 0;
         var12 = 30;

         while(var12 < var10) {
            var6.drawRegion((mImage)AvMain.imgTrai[var11], 0, var12, 0);
            var6.drawRegion((mImage)AvMain.imgPhai[var11], var9, var12, 0);
            var12 += mImage.getImageHeight(AvMain.imgTrai[var11].image);
            ++var11;
            if (var11 >= AvMain.imgTrai.length) {
               var11 = 0;
            }
         }

         var6.drawRegion((mImage)AvMain.imgGoc[3], var9, var10, 0);
         var6.drawRegion((mImage)AvMain.imgGoc[2], 0, var10, 0);
         var6.drawRegion((mImage)AvMain.imgGoc[1], var9, 0, 0);
         var6.drawRegion((mImage)AvMain.imgGoc[0], 0, 0, 0);
         var6.translate(-var4, -var5);
         var1.translate(var4, var5);
         var1.drawRegion((mImage)AvMain.imgTrangTri, super.BO / 2, (super.BP << 2) / 31, 3);
         var1.setColor(-3757444);
         var1.fillRect(15, super.BP * 7 / 31, super.BO - 30, 52);
         var1.fillRect(15, 100, super.BO - 32, 16);
         var1.setColor(-5995930);
         var1.fillRect(16, super.BP * 7 / 31 + 1, super.BO - 30, 50);
         var1.fillRect(16, 101, super.BO - 32, 14);
         var1.drawRegion((mImage)AvMain.mimgWanted[13], super.BO / 2, 94, 3);
         var1.drawRegion((mImage)AvMain.mimgWanted[14], 23, 130, 3);
         var1.drawRegion((mImage)AvMain.mimgWanted[16], 7, 93, 0);
         var1.drawRegion(AvMain.mimgWanted[16], 0, 0, 6, 46, 2, super.BO - 13, 93, 0);
         var1.drawRegion((mImage)AvMain.mimgWanted[15], 102, 145, 3);
         var1.drawRegion((mImage)AvMain.mimgWanted[17], 48, 144, 3);
         var1.translate(-var4, -var5);
      }

      if (this.AH != null) {
         for(int var2 = 0; var2 < this.AH.size(); ++var2) {
            iCommand var3;
            (var3 = (iCommand)this.AH.elementAt(var2)).paint(var1, var3.xCmd, var3.yCmd);
         }
      }

      int var10005 = super.BP;
      int var14 = super.BO;
      var5 = super.BN;
      var4 = super.BM;
      if (this.AB != null && this.AB.BC != null) {
         var1.translate(var4, var5);
         this.AB.BC.AA(var1, var14 / 2, this.AJ, false);
         AvMain.FontBorderColor(var1, this.AB.BC.name, var14 / 2, 103, 2, (int)0, (int)8);
         Class_CX.AC(var1, this.AB.BC.KP, var14 / 2 + 4, 128);
         var1.translate(-var4, -var5);
      }

   }

   public final void update() {
      if (super.KP != null) {
         super.KP.update();
      }

      super.update();
   }

   public final void handleKeyPress() {
      if (this.AH != null) {
         int var1 = this.AH.size();
         if (GameCanvas.isKeyPressed() && var1 > 0) {
            int var2 = this.AI;
            if (GameCanvas.isKeyPressed(0)) {
               --this.AI;
               GameCanvas.clearKeyPressed(0);
            } else if (GameCanvas.isKeyPressed(2)) {
               ++this.AI;
               GameCanvas.clearKeyPressed(2);
            }

            this.AI = AvMain.AA(this.AI, var1 - 1, false);
            if (var2 != this.AI && GameCanvas.isKeyPressed()) {
               for(var2 = 0; var2 < var1; ++var2) {
                  iCommand var3 = (iCommand)this.AH.elementAt(var2);
                  if (var2 == this.AI) {
                     var3.AG = true;
                  } else {
                     var3.AG = false;
                  }
               }
            }
         }
      }

      if (GameCanvas.AL[5]) {
         GameCanvas.AB(5);
         if (this.AH != null && this.AI < this.AH.size()) {
            ((iCommand)this.AH.elementAt(this.AI)).AD();
         }
      }

      this.AS();
   }

   public final void updatePointer() {
      super.updatePointer();
      if (this.AH != null) {
         for(int var1 = 0; var1 < this.AH.size(); ++var1) {
            ((iCommand)this.AH.elementAt(var1)).AE();
         }
      }

   }
}
