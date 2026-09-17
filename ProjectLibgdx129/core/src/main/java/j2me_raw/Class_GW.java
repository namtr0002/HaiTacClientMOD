public final class Class_GW extends ChatDetail {
   private iCommand AB;
   private iCommand AC;
   public static mVector AA = new mVector();
   private static int AD = 0;

   public Class_GW(String var1, byte var2) {
      super(var1, (byte)4);
   }

   public final void AA(int var1, int var2, int var3, int var4, int var5, int var6) {
      super.AF = var1;
      super.AG = var2;
      super.AH = var3;
      super.AI = var4;
      super.AE = var5;
      super.AK = var6;
      super.AJ = 48;
      super.AM = new ListNew(var1, var2, var3, var4, super.AJ, 0, AA.size() * super.AJ - var4, true);
      this.AB = new iCommand(T.DY, 0, this);
      this.AC = new iCommand(T.DR, 1, this);
      if (!GameCanvas.isTouch) {
         super.center = this.AB;
         super.DA = this.AC;
      }

      super.DF = this.AB;
   }

   public final void AA() {
      AD = 0;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (super.AL >= 0 && super.AL <= AA.size()) {
            Class_DS var3 = (Class_DS)AA.elementAt(super.AL);
            GameCanvas.AB(new Class_AO(var3));
         }

         return;
      case 1:
         GlobalService.getInstance().AQ((byte)2);
         AD = 40;
      default:
      }
   }

   public final void paint(mGraphics var1) {
      var1.AD(super.AF - super.AE, super.AG - 2, super.AH + (super.AE << 1), super.AI + 2);
      mGraphics.AC();
      int var10000 = super.AF;
      var10000 = super.AE;
      var10000 = super.AG;
      var10000 = super.AH;
      var10000 = super.AE;
      var10000 = super.AI;
      mGraphics.AD();
      var1.translate(0, -super.AM.AC);
      if (AA != null) {
         AA.size();
      }

      if (AD > 0) {
         MsgDialog.fraImgWaiting.drawFrame(GameCanvas.gameTick / 6 % MsgDialog.fraImgWaiting.nFrame, super.AF + super.AH / 2, super.AG + super.AI / 2, 0, 3, var1);
      } else {
         for(int var2 = 0; var2 < AA.size(); ++var2) {
            int var3 = super.AG + var2 * super.AJ;
            int var4 = super.AF - 2;
            int var5 = super.AH + 4;
            Class_DS var6 = (Class_DS)AA.elementAt(var2);
            if (var6 != null) {
               AvMain.paintRect(var1, var4, var3, var5 - 1, 40, (byte)1, 3);
               AvMain.paintRect(var1, var4 + 45, var3 + 2, var5 - 42 - 10, 14, (byte)1, 1);
               mFont.tahoma_7b_black.drawString(var1, var6.AE, var4 + 45 + (var5 - 52) / 2, var3 + 3, 2);
               mFont.tahoma_7b_white.drawString(var1, var6.AC, var4 + 45 + (var5 - 52) / 2, var3 + 22, 2);
               var1.drawRegion((mImage)AvMain.imgBorderIcon, var4 + 4 + 16, var3 + 4 + 16, 3);
               MainObject.AA(var1, var6.AV, var6.AW, var6.AX, var4 + 2 + 16, var3 + 54, 0);
               AvMain.fraStatusOnline.drawFrame(var6.AP, var4 + 45 + (var5 - 52) / 10, var3 + 22 + 6, 0, 3, var1);
               var1.drawRegion((mImage)AvMain.imgLvClan, var4 + 45 + ((var5 - 52) / 5 << 2) + 5, var3 + 22 - 5, 0);
               mFont.tahoma_7b_white.drawString(var1, String.valueOf(var6.AK), var4 + 45 + ((var5 - 52) / 5 << 2) + 18, var3 + 22 + 2, 2);
            }
         }

         mGraphics.AE();
         mGraphics.restoreCanvas();
         super.paint(var1);
      }
   }

   public final void update() {
      super.AM.AC();
      if (AD > 0) {
         --AD;
      }

   }

   public final void handleKeyPress() {
      int var1 = super.AL;
      if (GameCanvas.AL[2]) {
         GameCanvas.AB(2);
         if (super.AL > 0) {
            --super.AL;
         }
      } else if (GameCanvas.AL[8]) {
         GameCanvas.AB(8);
         if (super.AL < AA.size() - 1) {
            ++super.AL;
         }
      }

      if (var1 != super.AL) {
         int var2 = super.AL * super.AJ - super.AI / 4;
         super.AM.AA(var2);
      }

      super.handleKeyPress();
      this.AS();
   }

   public final void updatePointer() {
      super.AM.update_Pos_UP_DOWN();
      if (GameCanvas.isPointerSelect && AA.size() > 0 && GameCanvas.isPoint(super.AF, super.AG, super.AH, super.AI)) {
         GameCanvas.isPointerSelect = false;
         int var1;
         if ((var1 = (GameCanvas.AZ - super.AG + super.AM.AC) / super.AJ) >= 0 && var1 < AA.size()) {
            super.AL = var1;
            this.AB.AD();
         }
      }

      if (GameCanvas.AO && super.AM.AC < -super.AJ && AD <= 0) {
         this.AC.AD();
         GameCanvas.AO = false;
      }

   }
}
