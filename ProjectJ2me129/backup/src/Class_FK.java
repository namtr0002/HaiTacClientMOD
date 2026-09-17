public final class Class_FK extends ChatDetail {
   private iCommand AB;
   private iCommand AC;
   private int AD;
   private int AX;
   public static int AA = 0;

   public Class_FK(String var1, byte var2) {
      super(var1, (byte)4);
   }

   public final void AA(int var1, int var2, int var3, int var4, int var5, int var6) {
      super.AF = var1;
      super.AG = var2;
      super.AH = var3;
      super.AI = var4;
      super.AE = var5;
      super.AK = var6;
      super.AJ = 30;
      super.AM = new ListNew(var1, var2, var3, var4, super.AJ, 0, super.AN.size() * super.AJ - var4, true);
      this.AB = new iCommand(T.DY, 0, this);
      this.AC = new iCommand(T.DR, 1, this);
      if (!GameCanvas.isTouch) {
         super.center = this.AB;
         super.DA = this.AC;
      }

      super.DF = this.AB;
   }

   public final void AA() {
      AA = 0;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (super.AL >= 0 && super.AL <= super.AN.size()) {
            Class_DS var3 = (Class_DS)super.AN.elementAt(super.AL);
            GameCanvas.AB(new Class_AN(var3));
         }

         return;
      case 1:
         GlobalService.getInstance().Clan_CMD((byte)17, "", 0, (byte)0);
         AA = 40;
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
      this.AD = 0;
      this.AX = 0;
      if (super.AN != null) {
         this.AX = super.AN.size();
      }

      if (AA > 0) {
         MsgDialog.fraImgWaiting.drawFrame(GameCanvas.gameTick / 6 % MsgDialog.fraImgWaiting.nFrame, super.AF + super.AH / 2, super.AG + super.AI / 2, 0, 3, var1);
      } else {
         for(int var2 = 0; var2 <= this.AX; ++var2) {
            if (var2 < super.AN.size() && var2 >= 0) {
               Class_DS var3 = (Class_DS)super.AN.elementAt(var2);
               int var4 = super.AG + var2 * super.AJ;
               int var5 = var3.AC.compareTo(GameScreen.player.name) == 0 ? 5 : 4;
               this.AA(var1, (byte)var5, (byte)-1, 0, super.AJ - 2, var4, var2 == super.AL);
               int var6 = super.AF - 2;
               int var7 = super.AH + 4;
               var1.drawRegion((mImage)AvMain.imgDonateClan, var6 + var7 - 47, var4 - 1 + 2, 0);
               int var10001;
               switch(var5) {
               case 4:
                  var10001 = -6790581;
                  break;
               case 5:
                  var10001 = -12537804;
                  break;
               default:
                  var10001 = -6790581;
               }

               var1.setColor(var10001);
               var1.fillRect(var6 + var7 - 51, var4 - 1 + 17, 22, 9);
               var1.drawRegion((mImage)AvMain.imgLvClan, var6 + var7 - 28, var4 - 1 + 1, 0);
               AvMain.FontBorderColor(var1, var3.AC, super.AF + (super.AE << 1) + 16, var4 + super.AE / 2, 0, (int)0, (int)7);
               mFont.tahoma_7b_black.drawString(var1, Class_DS.AA(var3.AS), super.AF + (super.AE << 1) + 16 + 6, var4 + GameCanvas.hText - 1, 0);
               mFont.tahoma_7_white.drawString(var1, String.valueOf(var3.AL), super.AF - 2 + super.AH + 4 - 40, var4 + 15, 2);
               mFont.tahoma_7b_white.drawString(var1, String.valueOf(var3.AK), super.AF - 2 + super.AH + 4 - 15, var4 + 7, 2);
               MainObject.AA(var1, var3.AV, var3.AW, var3.AX, super.AF + (super.AE << 1) + 2, var4 + super.AE + 45, 2);
               AvMain.fraStatusOnline.drawFrame(var3.AP, super.AF + (super.AE << 1) + 16 + 1, var4 + super.AE + GameCanvas.hText, 0, 3, var1);
            }
         }

         mGraphics.AE();
         mGraphics.restoreCanvas();
         super.paint(var1);
      }
   }

   public final void update() {
      super.AM.AC();
      if (AA > 0) {
         --AA;
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
         if (super.AL < super.AN.size() - 1) {
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
      if (GameCanvas.isPointerSelect && super.AN.size() > 0 && GameCanvas.isPoint(super.AF, super.AG, super.AH, super.AI)) {
         GameCanvas.isPointerSelect = false;
         int var1;
         if ((var1 = (GameCanvas.AZ - super.AG + super.AM.AC) / super.AJ) >= 0 && var1 < super.AN.size()) {
            super.AL = var1;
            this.AB.AD();
         }
      }

      if (GameCanvas.AO && super.AM.AC < -super.AJ && AA <= 0) {
         this.AC.AD();
         GameCanvas.AO = false;
      }

   }

   public final void AB() {
      int var1;
      if ((var1 = super.AN.size() * super.AJ - super.AI) > 0) {
         super.AM.AD = var1;
      }

   }
}
