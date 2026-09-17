public final class TabQuest extends MainTab {
   private ListNew BP;
   private MainQuest BQ;
   public static iCommand BN;
   private LoadImageStatic BR = new LoadImageStatic(0);
   private LoadImageStatic BS = new LoadImageStatic(0);
   private Scroll BT = new Scroll();
   private int BU = 0;
   public static boolean BO = false;

   public TabQuest(String var1) {
      super.nameTab = var1;
      this.BP = new ListNew();
      super.indexIconTab = 4;
      super.AN = 32;
      if (super.AW) {
         super.AN = 36;
      }

      BN = new iCommand(T.CE, 0, this);
      this.BR = new LoadImageStatic(super.AQ - (super.AS + 16 + 14));
      this.BS = new LoadImageStatic(super.AQ - (super.AS + 16 + 20));
      this.BU = super.AR / super.AN + 1;
   }

   public final void AB() {
      int var1 = Player.QI.size() * super.AN - super.AR + super.AS * 3;
      this.BP = new ListNew(super.AO, super.AP, super.AQ, super.AR, 0, 0, var1, true);
      this.BT.AA(super.AO + super.AQ + super.AS, super.AP + super.AS / 2, super.AR - (super.AS << 1), -7967666);
      if (GameCanvas.isKeyPressed()) {
         super.IdSelect = 0;
         if (Player.QI.size() > 0) {
            this.AD(super.IdSelect);
            if (this.BQ != null) {
               super.center = this.BQ.AB();
            }
         } else {
            super.center = null;
         }

         super.DF = super.center;
      } else {
         super.IdSelect = -1;
      }
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (this.BQ == null) {
            return;
         } else {
            this.AL();
         }
      default:
      }
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      AvMain.paintRect(var1, super.AO, super.AP, super.AQ, super.AR - super.AS, (byte)0, 4);
      mGraphics.AC();
      int var10000 = super.AO;
      var10000 = super.AP;
      var10000 = super.AQ;
      var10000 = super.AR;
      var10000 = super.AS;
      mGraphics.AD();
      this.AB(var1);
      int var2 = super.AS << 1;
      int var3 = super.AS + 16;
      int var4;
      int var5 = (var4 = this.BP.AC / super.AN) + this.BU;
      var2 += super.AN * var4;

      MainScreen var7;
      for(var4 = var4; var4 < var5; ++var4) {
         if (var4 >= 0 && var4 < Player.QI.size()) {
            label77: {
               MainQuest var6 = (MainQuest)Player.QI.elementAt(var4);
               AvMain.fraQuest.drawFrame(var6.AB + 1, var3 + 6, var2 + super.AN / 4, 0, 3, var1);
               if ((this.BR.AF || this.BS.AF) && super.IdSelect == var4) {
                  var7 = GameCanvas.currentScreen;
                  if (MainScreen.AD((byte)1) && this.BP.AC == this.BP.AB) {
                     var1.AD(var3 + 14, var2 - 2, this.BR.AB, super.AN);
                     if (var6.AB == 0) {
                        mFont.tahoma_7_blue.drawString(var1, T.FC + (var6.AA == 0 ? var6.AA() : ""), var3 + 14 - this.BR.AD, var2 - 2, 0);
                     } else {
                        mFont.tahoma_7b_white.drawString(var1, var6.AH + var6.AA(), var3 + 14 - this.BR.AD, var2 - 2, 0);
                     }

                     mFont.tahoma_7_white.drawString(var1, var6.AL, var3 + 20 - this.BS.AD, var2 + GameCanvas.hText - 2 - 2, 0);
                     this.AB(var1);
                     break label77;
                  }
               }

               if (var6.AB == 0) {
                  mFont.tahoma_7_blue.drawString(var1, T.FC + (var6.AA == 0 ? var6.AA() : ""), var3 + 14, var2 - 2, 0);
               } else {
                  mFont.tahoma_7b_white.drawString(var1, var6.AH + var6.AA(), var3 + 14, var2 - 2, 0);
               }

               mFont.tahoma_7_white.drawString(var1, var6.AL, var3 + 20, var2 + GameCanvas.hText - 2 - 2, 0);
            }

            if (super.IdSelect == var4) {
               var7 = GameCanvas.currentScreen;
               if (MainScreen.AD((byte)1)) {
                  var1.drawRegion(AvMain.imgSelect, 0, 0, 12, 16, 4, var3 - GameCanvas.gameTick / 2 % 3, var2 + 4, 10);
               }
            }
         }

         var2 += super.AN;
      }

      var7 = GameCanvas.currentScreen;
      if (MainScreen.AD((byte)1)) {
         super.paint(var1);
         if (this.BP.AD > 0) {
            this.BT.AA(var1);
         }
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
   }

   private void AB(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      var1.AD(super.AO - 1, super.AP + 1, super.AQ + 2, super.AR - 1 - super.AS - 1);
      var1.translate(super.AO, super.AP);
      var1.translate(0, -this.BP.AC);
   }

   public final void update() {
      this.BP.AC();
      this.BT.AA(this.BP.AC, this.BP.AD);
      this.BR.AA();
      LoadImageStatic var1;
      if ((var1 = this.BS).AF) {
         ++var1.AE;
         if (var1.AE > 10) {
            var1.AD += var1.AC;
         }

         if (var1.AD < var1.AA) {
            return;
         }
      }

      var1.AD = 0;
      var1.AE = 0;
   }

   public final void handleKeyPress() {
      boolean var1 = false;
      if (GameCanvas.isKeyPressed(1)) {
         --super.IdSelect;
         GameCanvas.clearKeyPressed(1);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         ++super.IdSelect;
         GameCanvas.clearKeyPressed(3);
         var1 = true;
      }

      if (GameCanvas.isKeyPressed(0) || GameCanvas.isKeyPressed(2)) {
         MainScreen var10000 = GameCanvas.currentScreen;
         MainScreen.AC((byte)0);
         GameCanvas.clearKeyPressed(0);
         GameCanvas.clearKeyPressed(2);
      }

      if (var1) {
         super.IdSelect = AvMain.AA(super.IdSelect, Player.QI.size() - 1, false);
         this.AD(super.IdSelect);
         if (this.BQ != null) {
            super.center = this.BQ.AB();
            super.DF = super.center;
         }

         if (GameCanvas.isKeyPressed()) {
            this.BP.AA((super.IdSelect + 1) * super.AN - super.AR / 2);
         }
      }

      super.handleKeyPress();
   }

   public final void updatePointer() {
      this.BP.update_Pos_UP_DOWN();
      if (GameCanvas.isPointerSelect && Player.QI.size() > 0 && GameCanvas.isPoint(super.AO, super.AP, super.AQ, super.AR)) {
         int var1;
         if ((var1 = (GameCanvas.AZ - super.AP + this.BP.AC) / super.AN) >= 0 && var1 < Player.QI.size()) {
            if (var1 == super.IdSelect) {
               this.AL();
            } else {
               super.IdSelect = var1;
            }

            this.AD(super.IdSelect);
            super.center = this.BQ.AB();
         }

         GameCanvas.isPointerSelect = false;
      }

      super.updatePointer();
   }

   private void AD(int var1) {
      this.BQ = (MainQuest)Player.QI.elementAt(var1);
      if (this.BQ != null) {
         String var2 = this.BQ.AH + this.BQ.AA();
         if (this.BQ.AB == 0) {
            var2 = T.FC;
         }

         this.BR.AA(var2, mFont.tahoma_7b_black);
         this.BS.AA(this.BQ.AL, mFont.tahoma_7_black);
      }

   }

   private void AL() {
      MsgDialog var1;
      (var1 = new MsgDialog()).AA(this.BQ, false);
      GameCanvas.AA((MainDialog)var1);
   }
}
