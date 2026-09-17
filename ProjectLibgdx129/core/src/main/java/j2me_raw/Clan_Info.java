public final class Clan_Info extends ChatDetail {
   private int[] AB;
   private static mFont AC;
   private static mFont AD;
   public MainClan AA;
   private String[] AX;
   private iCommand AY;
   private iCommand AZ;
   private iCommand BA;
   private int BB = 0;
   private InputDialog BC;
   private int BD = 2;
   private int BE = 0;
   private int BF = 0;
   private int[] BG = new int[]{0, 4, 8, 16, 20, 20};

   public Clan_Info(String var1, byte var2, MainClan var3) {
      super(var1, (byte)5);
      this.AA = var3;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 1:
         GlobalService.getInstance().Clan_CMD((byte)6, "", 1, (byte)this.BB);
         return;
      case 2:
         this.BC = new InputDialog();
         this.BC.AA(T.NF, new iCommand(T.CT, 4, this), false, T.NG);
         GameCanvas.AA((MainDialog)this.BC);
         return;
      case 3:
         GlobalService.getInstance().Clan_CMD((byte)13, "", 0, (byte)0);
         return;
      case 4:
         String var3;
         if ((var3 = this.BC.tfInput.getText()).length() > 0) {
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
      AC = mFont.tahoma_7_white;
      AD = mFont.tahoma_7b_white;
      this.AB = new int[T.WI.length];

      for(var5 = 0; var5 < T.WI.length; ++var5) {
         this.AB[var5] = AC.getWidth(T.WI[var5]);
      }

      this.AX = null;
      if (this.AA != null) {
         this.AA(this.AA.AC, var3 - 6);
      }

      var5 = T.WI.length + 3 + T.VZ.length;
      this.BD = 2;
      if (this.AX != null) {
         this.BD = this.AX.length;
      }

      if (this.BD < 2) {
         this.BD = 2;
      }

      var5 += this.BD;
      super.AM = new ListNew(var1, var2, var3, var4, super.AJ, 0, var5 * super.AJ + 5 - var4, true);
      super.AL = 0;
      this.AZ = new iCommand(T.BI, 1, this);
      this.AY = new iCommand(T.CT, 2, this);
      this.BA = new iCommand(T.NW, 3, this);
   }

   public final void AA(String var1, int var2) {
      this.AX = AD.splitFontArray(var1, var2);
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
      this.AA(var1, (byte)3, (byte)-1, 0, super.AJ << 2, var2, super.AL == 0);
      var1.drawRegion(AvMain.imgBannerClan, 0, 0, 51, 20, 0, super.AF + super.AH / 2 - 51, var2 + 1, 0);
      var1.drawRegion(AvMain.imgBannerClan, 0, 0, 51, 20, 2, super.AF + super.AH / 2, var2 + 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.tabInfo, var3 + super.AH / 2, var2 + 3, 2);
      var2 += super.AJ + 2;

      int var4;
      for(var4 = 0; var4 < 3; ++var4) {
         AC.drawString(var1, T.WI[var4], var3, var2, 0);
         if (var4 == 0) {
            AD.drawString(var1, this.AA.name, var3 + this.AB[var4], var2, 0);
         } else if (var4 == 1) {
            AD.drawString(var1, this.AA.AB, var3 + this.AB[var4], var2, 0);
         } else {
            MainImage var5;
            if (var4 == 2 && (var5 = Potion.AC(this.AA.idIcon)) != null && var5.img != null) {
               if (var5.frame == -1) {
                  var5.set_Frame();
               }

               if (var5.frame <= 1) {
                  var1.drawRegion((mImage)var5.img, var3 + this.AB[var4] + 11, var2 + 4, 3);
               } else {
                  byte var6;
                  if (this.BF >= var5.frame - 1) {
                     var6 = 15;
                  } else {
                     var6 = 3;
                  }

                  if (CRes.abs(GameCanvas.gameTick - this.BE) > var6) {
                     ++this.BF;
                     if (this.BF >= var5.frame) {
                        this.BF = 0;
                     }

                     this.BE = GameCanvas.gameTick;
                  }

                  var1.drawRegion(var5.img, 0, this.BF * var5.AB, var5.AB, var5.AB, 0, var3 + this.AB[var4] + 11, var2 + 4, 3);
               }
            }
         }

         var2 += super.AJ;
      }

      this.AA(var1, (byte)0, (byte)-1, 0, super.AJ * 5, var2, super.AL == 1);
      var1.drawRegion(AvMain.imgBannerClan, 0, 20, 51, 20, 0, super.AF + super.AH / 2 - 51, var2 + 1, 0);
      var1.drawRegion(AvMain.imgBannerClan, 0, 20, 51, 20, 2, super.AF + super.AH / 2, var2 + 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.chiso, var3 + super.AH / 2, var2 + 3, 2);
      var2 += super.AJ + 2;
      if (this.AA.AR > 0 && Player.QN == 0) {
         if (AvMain.GU == null) {
            AvMain.GU = LoadImageStatic.AA("/interface/clanlevelup.png", 12, 12);
         }

         AvMain.GU.drawFrame(GameCanvas.gameTick / 3 % AvMain.GU.nFrame, var3 + (super.AH / 5 << 1) - 8, var2 + 5, 0, 3, var1);
      }

      int var8;
      for(var4 = 3; var4 < 7; ++var4) {
         AC.drawString(var1, T.WI[var4], var3, var2, 0);
         if (var4 != 3) {
            if (var4 == 4) {
               AD.drawString(var1, this.AA.AE + "/" + this.AA.AF, var3 + this.AB[var4], var2, 0);
               AC.drawString(var1, T.RJ, var3 + (super.AH / 5 << 1), var2, 0);
               var8 = AC.getWidth(T.RJ);
               AD.drawString(var1, String.valueOf(this.AA.AO), var3 + (super.AH / 5 << 1) + var8, var2, 0);
            } else if (var4 == 5) {
               AC.drawString(var1, T.PC, var3 + (super.AH / 5 << 1), var2, 0);
               var8 = AC.getWidth(T.PC);
               AD.drawString(var1, String.valueOf(this.AA.AS), var3 + (super.AH / 5 << 1) + var8, var2, 0);
               AD.drawString(var1, "" + this.AA.AI, var3 + this.AB[var4], var2, 0);
            } else if (var4 == 6) {
               AD.drawString(var1, String.valueOf(this.AA.AM), var3 + this.AB[var4], var2, 0);
               AC.drawString(var1, T.bery + ": ", var3 + (super.AH / 5 << 1), var2, 0);
               var8 = AC.getWidth(T.bery + ": ");
               AD.drawString(var1, String.valueOf(this.AA.AN), var3 + (super.AH / 5 << 1) + var8, var2, 0);
            }
         } else {
            if (this.AA.AR == 2 && Player.QN == 0) {
               AD.drawString(var1, T.PB, var3 + (super.AH / 5 << 1), var2, 0);
            } else {
               AC.drawString(var1, T.HK, var3 + (super.AH / 5 << 1), var2, 0);
               var8 = AC.getWidth(T.HK);
               AD.drawString(var1, this.AA.AG + "/" + this.AA.AH, var3 + (super.AH / 5 << 1) + var8, var2, 0);
            }

            AD.drawString(var1, "" + this.AA.AD, var3 + this.AB[var4], var2, 0);
         }

         var2 += super.AJ;
      }

      this.AA(var1, (byte)0, (byte)-1, 0, super.AJ * (T.VZ.length + 1), var2, super.AL == 2);
      var1.drawRegion(AvMain.imgBannerClan, 0, 20, 51, 20, 0, super.AF + super.AH / 2 - 51, var2 + 1, 0);
      var1.drawRegion(AvMain.imgBannerClan, 0, 20, 51, 20, 2, super.AF + super.AH / 2, var2 + 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.tabAttribute, var3 + super.AH / 2, var2 + 3, 2);
      byte var7 = 0;
      if (this.AA.AK > 0) {
         mFont.tahoma_7_yellow.drawString(var1, "+" + this.AA.AK, var3 + super.AH - 2, var2 + 3, 1);
         var7 = 1;
      }

      var2 += super.AJ + 2;

      for(var8 = 0; var8 < T.VZ.length; ++var8) {
         AC.drawString(var1, T.VZ[var8], var3 + 55, var2, 1);
         Interface_Game.AA(var1, (byte)103, this.AA.AX[var8], this.AA.AL, var3 + 59, var2 + 1, 0, 10, super.AH - 80, 0, false, 0, false, 0);
         if (Player.QN == 0) {
            var1.drawRegion(AvMain.imgPlusClan, 0, var7 * 12, 12, 12, 0, var3 + super.AH - 10, var2 + 6, 3);
            if (super.AL == 2 && GameCanvas.gameTick % 10 > 6 && var8 == this.BB) {
               var1.drawRegion(AvMain.imgPlusClan, 0, 0, 12, 12, 0, var3 + super.AH - 10, var2 + 6, 3);
            }
         }

         var2 += super.AJ;
      }

      this.AA(var1, (byte)1, (byte)-1, 0, super.AJ * (this.BD + 1), var2, super.AL == 3);
      var1.drawRegion(AvMain.imgBannerClan, 0, 40, 51, 20, 0, super.AF + super.AH / 2 - 51, var2 + 1, 0);
      var1.drawRegion(AvMain.imgBannerClan, 0, 40, 51, 20, 2, super.AF + super.AH / 2, var2 + 1, 0);
      mFont.tahoma_7b_black.drawString(var1, T.WI[7], var3 + super.AH / 2, var2 + 3, 2);
      var2 += super.AJ + 3;
      if (this.AX != null) {
         for(var8 = 0; var8 < this.AX.length; ++var8) {
            AD.drawString(var1, this.AX[var8], var3 + 2, var2, 0);
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
         if (GameCanvas.isPointer(super.AF, var1 - super.AM.AC + super.AG, super.AH, (this.BD + 1) * super.AJ) && (Player.QN == 0 || Player.QN == 1)) {
            this.AY.AD();
            GameCanvas.isPointerSelect = false;
         }

         var1 = 4 * super.AJ;
         if (GameCanvas.isPointer(super.AF, var1 - super.AM.AC + super.AG, super.AH, 5 * super.AJ) && Player.QN == 0 && GameScreen.player.clan.AR > 0) {
            this.BA.AD();
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
      if (super.AL == 2 && Player.QN == 0 && this.AA.AK > 0) {
         if (GameCanvas.AL[2]) {
            GameCanvas.AB(2);
            if (this.BB > 0) {
               --this.BB;
            } else {
               --super.AL;
               this.AF();
            }
         } else if (GameCanvas.AL[8]) {
            GameCanvas.AB(8);
            if (this.BB < T.VZ.length - 1) {
               ++this.BB;
            } else {
               ++super.AL;
               this.AF();
            }
         }
      } else {
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
            this.AF();
         }
      }

      if (var1 != super.AL) {
         super.center = null;
         if (super.AL == 2 && GameScreen.player.clan.AK > 0 && Player.QN == 0) {
            super.center = this.AZ;
         } else if (super.AL == 1 && GameScreen.player.clan.AR > 0 && Player.QN == 0) {
            super.center = this.BA;
         } else if (super.AL == 3 && (Player.QN == 0 || Player.QN == 1)) {
            super.center = this.AY;
         }
      }

      super.handleKeyPress();
   }

   private void AF() {
      int var1 = 0;
      if (super.AL >= 0 && super.AL < this.BG.length) {
         var1 = this.BG[super.AL] * super.AJ - super.AI / 4;
      }

      if (super.AL == 0) {
         var1 = 0;
      }

      if (super.AL == 3) {
         var1 = super.AM.AD;
      }

      super.AM.AA(var1);
   }
}
