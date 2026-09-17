public final class Sudo_Info extends ChatDetail {
   public static Sudo_Info AA;
   private int[] AX;
   private static mFont AY;
   public MainSudo AB;
   private String[] AZ;
   private iCommand BA;
   private iCommand BB;
   private iCommand BC;
   private int BD = 0;
   private InputDialog BE;
   public short AC = 0;
   private int BF = 2;
   public static mVector AD = new mVector();
   private int[] BG = new int[]{0, 4, 8, 16, 20, 20};

   public Sudo_Info(String var1, byte var2, MainSudo var3) {
      super(var1, (byte)5);
      this.AB = var3;
      AA = this;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 1:
         GlobalService.getInstance().Clan_CMD((byte)6, "", 1, (byte)this.BD);
         return;
      case 2:
         this.BE = new InputDialog();
         this.BE.AA(T.NF, new iCommand(T.CT, 4, this), false, T.NG);
         GameCanvas.AA((MainDialog)this.BE);
         return;
      case 3:
         GlobalService.getInstance().Clan_CMD((byte)13, "", 0, (byte)0);
         return;
      case 4:
         String var3;
         if ((var3 = this.BE.tfInput.getText()).length() > 0) {
            GlobalService.getInstance().Clan_CMD((byte)5, var3, 0, (byte)0);
         }

         GameCanvas.end_Dialog();
      default:
      }
   }

   public final void AA(int var1, int var2, int var3, int var4, int var5, int var6) {
      super.AF = var1;
      super.AG = var2;
      super.AH = var3;
      super.AI = var4;
      super.AE = var5;
      super.AK = var6;
      super.AJ = GameCanvas.hText + 4;
      AY = mFont.tahoma_7_white;
      mFont var10000 = mFont.tahoma_7b_white;
      this.AX = new int[T.WI.length];

      for(var1 = 0; var1 < T.WI.length; ++var1) {
         this.AX[var1] = AY.getWidth(T.WI[var1]);
      }

      this.AZ = null;
      this.AF();
      super.AL = 0;
      this.BB = new iCommand(T.BI, 1, this);
      this.BA = new iCommand(T.CT, 2, this);
      this.BC = new iCommand(T.NW, 3, this);
   }

   public final void AF() {
      int var1 = T.WI.length + 3 + T.VZ.length;
      this.BF = 2;
      if (this.BF < 2) {
         this.BF = 2;
      }

      var1 += this.BF - 1;
      super.AM = new ListNew(super.AF, super.AG, super.AH, super.AI, super.AJ, 0, var1 * super.AJ + 5 - super.AI, true);
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
      int var2 = super.AG;
      int var3 = super.AF + 2;
      this.AA(var1, (byte)3, (byte)-1, 0, super.AJ * 6, var2, super.AL == 0);
      var1.drawRegion(AvMain.imgBannerClan, 0, 0, 51, 20, 0, super.AF + super.AH / 2 - 51, var2 + 1, 0);
      var1.drawRegion(AvMain.imgBannerClan, 0, 0, 51, 20, 2, super.AF + super.AH / 2, var2 + 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.VP, var3 + super.AH / 2, var2 + 3, 2);
      var2 += super.AJ + 2;
      AvMain.paintRect(var1, var3 + super.AH / 2 - 30, var2, 60, 66, (byte)0, 4);
      GameScreen.player.AA(var1, var3 + super.AH / 2, var2 + 40 + GameScreen.player.hOne / 4 + 5, true);
      mFont.tahoma_7b_black.drawString(var1, GameScreen.player.name, var3 + super.AH / 2, var2 + 72, 2);
      var2 += super.AJ * 5;
      this.AA(var1, (byte)0, (byte)-1, 0, super.AJ * 5, var2, super.AL == 1);
      var1.drawRegion(AvMain.imgBannerClan, 0, 20, 51, 20, 0, super.AF + super.AH / 2 - 51, var2 + 1, 0);
      var1.drawRegion(AvMain.imgBannerClan, 0, 20, 51, 20, 2, super.AF + super.AH / 2, var2 + 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.tabInfo, var3 + super.AH / 2, var2 + 3, 2);
      var2 += super.AJ + 2;
      int var4;
      MainInfoItem var5;
      if (AD.size() > 0) {
         for(var4 = 0; var4 < 4; ++var4) {
            var5 = (MainInfoItem)AD.elementAt(var4);
            if (var4 == 0) {
               mFont.tahoma_7b_yellow.drawString(var1, var5.AF, var3 + 5, var2, 0);
            } else if (var4 == 1) {
               mFont.tahoma_7_white.drawString(var1, var5.AF, var3 + 5, var2, 0);
               mFont.tahoma_7b_white.drawString(var1, String.valueOf(var5.AE), var3 + 80, var2, 0);
            } else if (var4 == 2) {
               mFont.tahoma_7_white.drawString(var1, var5.AF, var3 + 5, var2, 0);
            } else {
               mFont.tahoma_7b_white.drawString(var1, var5.AF, var3 + 5, var2, 0);
               Interface_Game.AA(var1, (byte)2, this.AC, 100, super.AF + super.AH / 3, var2, 0, (super.AE << 1) + 2, super.AH / 5 * 3, 1, false, 0, false, 0);
            }

            var2 += super.AJ;
         }
      }

      this.AA(var1, (byte)1, (byte)-1, 0, super.AJ * ((this.BF << 1) + 1), var2, super.AL == 3);
      var1.drawRegion(AvMain.imgBannerClan, 0, 40, 51, 20, 0, super.AF + super.AH / 2 - 51, var2 + 1, 0);
      var1.drawRegion(AvMain.imgBannerClan, 0, 40, 51, 20, 2, super.AF + super.AH / 2, var2 + 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.chiso, var3 + super.AH / 2, var2 + 3, 2);
      var2 += super.AJ + 2;
      if (AD.size() >= 4) {
         for(var4 = 4; var4 < AD.size(); ++var4) {
            var5 = (MainInfoItem)AD.elementAt(var4);
            mFont.tahoma_7b_yellow.drawString(var1, "- " + var5.AF, var3 + 5, var2, 0);
            var2 += super.AJ;
         }
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      super.paint(var1);
   }

   public final void update() {
      super.AM.AC();
   }

   public final void updatePointer() {
      super.AM.update_Pos_UP_DOWN();
      if (GameCanvas.isPointerSelect && GameCanvas.isPoint(super.AF, super.AG, super.AH, super.AI)) {
         int var1 = (T.WI.length + 1 + T.VZ.length + 1) * super.AJ;
         if (GameCanvas.isPointer(super.AF, var1 - super.AM.AC + super.AG, super.AH, (this.BF + 1) * super.AJ) && (Player.QN == 0 || Player.QN == 1)) {
            this.BA.AD();
            GameCanvas.isPointerSelect = false;
         }

         var1 = 4 * super.AJ;
         if (GameCanvas.isPointer(super.AF, var1 - super.AM.AC + super.AG, super.AH, 5 * super.AJ) && Player.QN == 0 && GameScreen.player.clan.AR > 0) {
            this.BC.AD();
            GameCanvas.isPointerSelect = false;
         }

         var1 = (T.WI.length + 2) * super.AJ;
         if (GameCanvas.isPointer(super.AF, var1 - super.AM.AC + super.AG, super.AH, T.VZ.length * super.AJ) && GameScreen.player.clan.AK > 0 && Player.QN == 0) {
            if ((var1 = (GameCanvas.AZ - (var1 - super.AM.AC + super.AG)) / super.AJ) >= 0 && var1 < T.VZ.length) {
               GlobalService.getInstance().Clan_CMD((byte)6, "", 1, (byte)var1);
            }

            GameCanvas.isPointerSelect = false;
         }
      }

   }

   public final void handleKeyPress() {
      int var1 = super.AL;
      if (super.AL == 2 && Player.QN == 0) {
         MainSudo var10000 = this.AB;
      }

      if (GameCanvas.AL[2]) {
         GameCanvas.AB(2);
         if (super.AL > 0) {
            --super.AL;
         }
      } else if (GameCanvas.AL[8]) {
         GameCanvas.AB(8);
         if (super.AL < 3) {
            ++super.AL;
         }
      }

      if (var1 != super.AL) {
         int var3 = 0;
         if (super.AL >= 0 && super.AL < this.BG.length) {
            var3 = this.BG[super.AL] * super.AJ - super.AI / 4;
         }

         if (super.AL == 0) {
            var3 = 0;
         }

         if (super.AL == 3) {
            var3 = super.AM.AD;
         }

         super.AM.AA(var3);
      }

      if (var1 != super.AL) {
         super.center = null;
         if (super.AL == 2 && GameScreen.player.clan.AK > 0 && Player.QN == 0) {
            super.center = this.BB;
         } else if (super.AL == 1 && GameScreen.player.clan.AR > 0 && Player.QN == 0) {
            super.center = this.BC;
         } else if (super.AL == 3 && (Player.QN == 0 || Player.QN == 1)) {
            super.center = this.BA;
         }
      }

      super.handleKeyPress();
   }
}
