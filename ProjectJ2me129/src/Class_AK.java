public final class Class_AK extends MsgDialog {
   private int[][] AA;
   private int[] AB;
   private iCommand AC;
   private iCommand BB;
   private boolean BC = false;

   public final void AA() {
      super.wDia = 160;
      super.hDia = 150;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.hh - super.hDia / 2 - iCommand.hButtonCmdNor / 2;
      this.AA = new int[][]{{50, 1, 0, -8, 33, -1}, {51, 2, 0, -103, 3, 0}, {52, 2, -12, -74, 3, 0}, {53, 2, 4, -58, 3, 0}, {54, 2, -30, -28, 3, 0}, {55, 2, 30, -28, 3, 0}, {56, 2, 10, -83, 3, 1}, {57, 2, 22, -53, 3, 1}, {58, 2, -19, -47, 3, 1}, {59, 2, 0, -25, 3, 1}, {60, 2, -18, -8, 3, 2}, {61, 2, 21, -12, 3, 2}, {62, 2, 0, -120, 3, 3}, {63, 3, 0, -8, 33, -1}};
      super.cmdList.removeAllElements();
      this.AB = new int[this.AA.length];
      this.AC = new iCommand(T.QH, 0, this);
      this.BB = new iCommand(T.QI, 1, this);
      super.AG = new iCommand(T.close, -1, this);
      if (GameCanvas.isTouch) {
         super.AG.setPos(super.AX + super.wDia - 13, super.AY + 13, MainTab.fraCloseTab, "");
         super.cmdList.addElement(super.AG);
         if (this.BB != null) {
            this.BB.setPos(super.AX + super.wDia / 2 - iCommand.wButtonCmd / 2 - 2, super.AY + super.hDia + iCommand.hButtonCmdNor, (FrameImage)null, this.BB.caption);
            super.cmdList.addElement(this.BB);
         }

         if (this.AC != null) {
            this.AC.setPos(super.AX + super.wDia / 2 + iCommand.wButtonCmd / 2 + 2, super.AY + super.hDia + iCommand.hButtonCmdNor, (FrameImage)null, this.AC.caption);
            super.cmdList.addElement(this.AC);
         }

         super.backCMD = super.AG;
         super.DF = this.BB;
         super.DE = this.AC;
      } else {
         super.DA = this.AC;
         super.center = this.BB;
         super.DB = super.AG;
      }
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         GameCanvas.end_Dialog();
         return;
      case 0:
         GlobalService.getInstance().AJ((byte)0);
         return;
      case 1:
         GlobalService.getInstance().AJ((byte)1);
      default:
      }
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = super.AY + 140;
      int var3 = super.AX + super.wDia / 2;
      this.AE(var1, super.AX - 5, super.AY, super.AT + 10, super.hDia, super.AT + 10);

      int var4;
      for(var4 = 0; var4 < this.AA.length; ++var4) {
         MainImage var5 = ObjectData.getImageAll((short)this.AA[var4][0], ObjectData.hashImageItemOther, (short)9000);
         int var6;
         if (var4 == this.AA.length - 1) {
            if (var5 != null && var5.img != null && this.BC) {
               var6 = var5.AC / this.AA[var4][1];
               var1.drawRegion(var5.img, 0, GameCanvas.BJ % this.AA[var4][1] * var6, var5.AB, var6, 0, var3 + this.AA[var4][2], var2 + this.AA[var4][3], this.AA[var4][4]);
            }
         } else if (var5 != null && var5.img != null) {
            var6 = var5.AC / this.AA[var4][1];
            var1.drawRegion(var5.img, 0, this.AB[var4] * var6, var5.AB, var6, 0, var3 + this.AA[var4][2], var2 + this.AA[var4][3], this.AA[var4][4]);
         }
      }

      GameCanvas.resetTrans(var1);
      if (super.cmdList != null) {
         for(var4 = 0; var4 < super.cmdList.size(); ++var4) {
            iCommand var7;
            (var7 = (iCommand)super.cmdList.elementAt(var4)).paint(var1, var7.xCmd, var7.yCmd);
         }
      }

      super.AD(var1);
   }

   public final void update() {
      super.update();
   }

   public final void AA(byte[] var1) {
      boolean var2 = true;
      if (var1 != null && var1.length != 0) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var3 + 1 < this.AB.length) {
               this.AB[var3 + 1] = var1[var3];
            }

            if (var1[var3] == 0) {
               var2 = false;
            }
         }

         this.BC = var2;
      }
   }
}
