public final class Class_GN extends MainTab {
   private mVector BN;
   private ListNew BO;
   private MainItem BP;
   private mVector BQ = new mVector();
   private mVector BR = new mVector();
   private iCommand BS;
   private iCommand BT;
   private iCommand BU;
   private iCommand BV;
   private iCommand BW;
   private iCommand BX;
   private iCommand BY;
   private TabScreen BZ;

   public Class_GN(String var1, mVector var2, TabScreen var3, byte var4) {
      this.BZ = var3;
      super.nameTab = var1;
      this.BN = null;
      super.AU = var4;
      if (super.AU == 0) {
         super.indexIconTab = 9;
      } else if (super.AU == 1) {
         super.indexIconTab = 10;
      } else if (super.AU == 2) {
         super.indexIconTab = 11;
      } else if (super.AU == 3) {
         super.indexIconTab = 7;
      } else if (super.AU == 4) {
         super.indexIconTab = 0;
      } else if (super.AU == 5) {
         super.indexIconTab = 12;
      } else if (super.AU == 6) {
         super.indexIconTab = 13;
      }

      MainTab.AE = 28;
      if (super.AW) {
         MainTab.AE = 32;
      }

      super.AN = 44;
      int var5 = 0;
      if (this.BN != null) {
         var5 = this.BN.size();
      }

      var5 = var5 * super.AN - super.AR + super.AS * 3;
      this.BO = new ListNew(super.AO, super.AP, super.AQ, super.AR, 0, 0, var5, true);
      super.AO = var3.AG + MainTab.AG / 2 - super.AQ / 2 + 10;
      this.AB();
      this.AL();
   }

   private void AL() {
      this.BY = new iCommand(T.TF, 8, this);
      this.BQ.removeAllElements();
      if (this.BN == null) {
         this.BW = new iCommand(T.DR, 2, this);
         AvMain.AA(this.BW, 0);
         this.BQ.addElement(this.BW);
         if (!GameCanvas.isTouch) {
            super.center = this.BW;
         }

         super.DF = this.BW;
      } else if (super.AU == 3) {
         this.BV = new iCommand(T.PR, 3, this);
         AvMain.AA(this.BV, 0);
         this.BU = new iCommand(T.BQ, 6, this);
         AvMain.AA(this.BU, 1);
         this.BX = new iCommand(T.PS, 5, this);
         AvMain.AA(this.BX, 1);
      } else {
         this.BS = new iCommand(T.BN, 0, this);
         AvMain.AA(this.BS, 0);
         this.BT = new iCommand(T.PP, 1, this);
         AvMain.AA(this.BT, 1);
         this.BQ.addElement(this.BS);
         this.BQ.addElement(this.BT);
         if (!GameCanvas.isTouch) {
            super.DA = this.BT;
            super.center = this.BS;
         }

         super.DE = this.BT;
         super.DF = this.BS;
      }
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (this.BP == null) {
            return;
         }

         GlobalService.getInstance().AC((byte)0, super.AU, this.BP.AR, this.BP.typeObject, (short)1);
         return;
      case 1:
         GlobalService.getInstance().AC((byte)8, super.AU, (short)0, (byte)0, (short)1);
         return;
      case 2:
         GlobalService.getInstance().AC((byte)9, super.AU, (short)0, (byte)0, (short)1);
         return;
      case 3:
         if (this.BP == null) {
            return;
         }

         GlobalService.getInstance().AC((byte)5, super.AU, this.BP.AR, this.BP.typeObject, (short)1);
         return;
      case 4:
         if (this.BP == null) {
            return;
         }

         GlobalService.getInstance().AC((byte)7, super.AU, this.BP.AR, this.BP.typeObject, (short)1);
         return;
      case 5:
         if (this.BP == null) {
            return;
         }

         GlobalService.getInstance().AC((byte)6, super.AU, this.BP.AR, this.BP.typeObject, (short)1);
         return;
      case 6:
         if (this.BP == null) {
            return;
         } else {
            if (this.BP.typeObject == 3) {
               GlobalService.getInstance().AC((byte)11, super.AU, this.BP.AR, this.BP.typeObject, (short)1);
               return;
            }

            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.TI);
            return;
         }
      case 8:
         if (this.BP != null) {
            MainItem var3 = (MainItem)GameScreen.player.LR.get("" + this.BP.typeEquip);
            if (var2 == 0) {
               TabInventory.CZ = var3.LvUpgrade;
               this.BR = MainItem.AA(this.BP, TabInventory.CZ);
               this.BY.caption = "  +" + this.BP.LvUpgrade;
               this.BY.AF = 1;
               return;
            } else if (var2 == 1) {
               TabInventory.CZ = 0;
               this.BR = MainItem.AA(this.BP);
               this.BY.caption = "  +" + var3.LvUpgrade;
               this.BY.AF = 0;
            }
         }
      case 7:
      default:
      }
   }

   public final void AB() {
      this.AL();
      if (GameCanvas.isKeyPressed()) {
         super.IdSelect = 0;
         this.AM();
      } else {
         super.IdSelect = -1;
      }
   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      AvMain.paintRect(var1, super.AO, super.AP, super.AQ, super.AR - super.AS, (byte)0, 4);
      int var2 = super.AS;
      MainScreen var10000;
      int var3;
      if (this.BQ != null && GameCanvas.AL()) {
         var10000 = GameCanvas.currentScreen;
         if (MainScreen.AD((byte)1) || GameCanvas.isTouch) {
            for(var3 = 0; var3 < this.BQ.size(); ++var3) {
               iCommand var4;
               (var4 = (iCommand)this.BQ.elementAt(var3)).paint(var1, var4.xCmd, var4.yCmd);
            }
         }
      }

      if (this.BN == null) {
         mFont.tahoma_7b_white.drawString(var1, T.PQ, 2 + super.AQ / 2, var2 + MainTab.AE + 2, 2);
         GameCanvas.resetTrans(var1);
      } else {
         GameCanvas.resetTrans(var1);
         var1.AD(super.AO + 2, super.AP + 1, super.AQ - 4, super.AR - 1 - super.AS - 1);
         mGraphics.AC();
         int var8 = super.AO;
         var8 = super.AP;
         var8 = super.AQ;
         var8 = super.AR;
         var8 = super.AS;
         mGraphics.AD();
         var1.translate(super.AO, super.AP);
         var1.translate(0, -this.BO.AC);
         var3 = this.BO.AC / super.AN - 1;
         int var7 = super.AR / super.AN + 3 + var3;
         var2 += super.AN * var3;

         for(var3 = var3; var3 < var7; ++var3) {
            if (var3 >= 0 && var3 < this.BN.size()) {
               var1.setColor(-2701384);
               var1.drawRect(2, var2 + 1, super.AQ - 5, super.AN - 2);
               if (super.IdSelect == var3) {
                  var10000 = GameCanvas.currentScreen;
                  if (MainScreen.AD((byte)1)) {
                     var1.setColor(-1);
                     var1.drawRect(3, var2 + 2, super.AQ - 7, super.AN - 4);
                  }
               }

               MainItem var5;
               (var5 = (MainItem)this.BN.elementAt(var3)).paint(var1, 2 + MainTab.AE / 2, var2 + MainTab.AE / 2 + 1, MainTab.AE);
               if (var5.typeObject == 3) {
                  AvMain.AC(var5.colorName).drawString(var1, var5.name, 2 + MainTab.AE + 2, var2 + MainTab.AE / 2 - 3, 0);
               } else {
                  String var6 = String.valueOf(var5.numPotion);
                  if (var5.typeObject == 4 && var5.ID == 0) {
                     var6 = var6 + "M";
                  }

                  AvMain.AC(var5.colorName).drawString(var1, var6 + " x " + var5.name, 2 + MainTab.AE + 2, var2 + MainTab.AE / 2 - 3, 0);
               }

               mFont.tahoma_7_yellow.drawString(var1, T.BG + " " + AvMain.AA((long)var5.AG), 2 + super.AS, var2 + MainTab.AE, 0);
               if (super.AU == 3 && var5.BY != 1) {
                  if (var5.BY >= 0 && var5.BY < T.WS.length) {
                     mFont.tahoma_7_yellow.drawString(var1, T.WS[var5.BY], 2 + super.AQ - 6, var2 + MainTab.AE, 1);
                  }
               } else {
                  var5.marketTime.paintCountDownTicketHour(var1, mFont.tahoma_7_white, 2 + super.AQ - 32, var2 + MainTab.AE, 0);
               }
            }

            var2 += super.AN;
         }

         mGraphics.AE();
         mGraphics.restoreCanvas();
         GameCanvas.resetTrans(var1);
         if (super.AV) {
            var10000 = GameCanvas.currentScreen;
            if (MainScreen.AD((byte)1) && this.BP != null) {
               this.AA(var1, this.BP, this.BR, (byte)0, super.AL, super.AM, false, (MainObject)null, TabInventory.CZ);
               if (this.BY != null) {
                  this.BY.AB(var1, this.BY.xCmd, this.BY.yCmd);
               }
            }
         }
      }

      Interface_Game.AA(var1, MotherCanvas.hw - Interface_Game.BW / 2, 0 + GameScreen.h12plus, false, 0);
   }

   public final void update() {
      if (this.BN != null) {
         for(int var1 = 0; var1 < this.BN.size(); ++var1) {
            MainItem var2;
            (var2 = (MainItem)this.BN.elementAt(var1)).marketTime.updateTimeCountDownTicket();
            if (var2.marketTime.timeCountDown <= 0 && var2.BY == 1) {
               var2.BY = 3;
            }
         }
      }

      this.BO.AC();
      if (this.BP != null) {
         this.AF();
         if (this.BP.CE) {
            this.BP = null;
            this.AM();
         }
      }

   }

   public final void AF() {
      if (!super.AV) {
         ++super.AX;
         if (super.AX >= 5) {
            super.AV = true;
            this.AG();
            return;
         }
      } else {
         super.AX = 0;
      }

   }

   public final void handleKeyPress() {
      if (GameCanvas.isKeyPressed(0) || GameCanvas.isKeyPressed(2)) {
         MainScreen var10000 = GameCanvas.currentScreen;
         MainScreen.AC((byte)0);
         GameCanvas.clearKeyPressed(0);
         GameCanvas.clearKeyPressed(2);
      }

      boolean var1 = false;
      int var2 = super.IdSelect;
      if (GameCanvas.isKeyPressed(1)) {
         --super.IdSelect;
         GameCanvas.clearKeyPressed(1);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         ++super.IdSelect;
         GameCanvas.clearKeyPressed(3);
         var1 = true;
      }

      if (this.BN == null) {
         super.IdSelect = -1;
      } else if (var1) {
         super.IdSelect = AvMain.AA(super.IdSelect, this.BN.size() - 1, false);
         if (var2 != super.IdSelect && super.IdSelect >= 0) {
            if (GameCanvas.isKeyPressed()) {
               int var3 = super.IdSelect * super.AN - super.AR / 2;
               if (super.IdSelect > 0) {
                  var3 += super.AN / 2;
               }

               this.BO.AA(var3);
            }

            this.AM();
            super.AV = false;
         }
      }

      super.handleKeyPress();
      this.AS();
   }

   public final void updatePointer() {
      this.BO.update_Pos_UP_DOWN();
      int var1;
      if (GameCanvas.isPointerSelect && this.BN != null && this.BN.size() > 0 && GameCanvas.isPoint(super.AO, super.AP, super.AQ, super.AR)) {
         if ((var1 = (GameCanvas.AZ - super.AP + this.BO.AC) / super.AN) == super.IdSelect) {
            super.IdSelect = var1;
         } else {
            super.IdSelect = var1;
            this.AM();
         }

         GameCanvas.isPointerSelect = false;
      }

      if (this.BQ != null) {
         for(var1 = 0; var1 < this.BQ.size(); ++var1) {
            ((iCommand)this.BQ.elementAt(var1)).AE();
         }
      }

      if (super.AV && this.BY != null) {
         this.BY.AE();
      }

      super.updatePointer();
   }

   public final void AG() {
      this.AA(this.BP, super.AO + MainTab.AG + 24, super.AP + 2);
      if (GameCanvas.isTouch) {
         TabInventory.CZ = 0;
         if (this.BP == null || this.BP.typeObject != 3 || this.BP.charClass != GameScreen.player.clazz && this.BP.charClass > 0) {
            if (this.BY != null) {
               this.BY.setPos(-50, -50, AvMain.fraPlus, "");
            }

            return;
         }

         MainItem var2 = (MainItem)GameScreen.player.LR.get("" + this.BP.typeEquip);
         if (this.BP.LvUpgrade != var2.LvUpgrade) {
            this.BY = new iCommand(T.TF, 8, 0, this);
            int var3 = this.BP.BT - this.BP.CO;
            this.BY.setPos(super.AL + this.BP.BS - 10, super.AM + var3 - 10, AvMain.fraPlus, "  +" + var2.LvUpgrade);
            return;
         }

         this.BY.setPos(-50, -50, AvMain.fraPlus, "");
      }

   }

   public final void AH() {
      if (this.BN != null) {
         MainItem var1;
         if ((var1 = (MainItem)this.BN.elementAt(super.IdSelect)) != null && this.BP != null && var1 != this.BP) {
            this.BP = var1;
            super.AV = false;
         }

      }
   }

   private void AM() {
      if (this.BN != null) {
         super.AV = false;
         super.IdSelect = AvMain.AA(super.IdSelect, this.BN.size() - 1, false);
         this.BP = (MainItem)this.BN.elementAt(super.IdSelect);
         if (super.AU == 3) {
            this.AN();
         }

         this.BR = MainItem.AA(this.BP);
      }
   }

   private void AN() {
      if (super.AU == 3) {
         this.BQ.removeAllElements();
         if (!GameCanvas.isTouch) {
            super.center = null;
            super.DA = null;
            super.DB = null;
         }

         if (this.BP != null) {
            if (this.BP.BY == 1) {
               this.BQ.addElement(this.BX);
               if (!GameCanvas.isTouch) {
                  super.DA = this.BX;
               }

               super.DE = this.BX;
            }

            if (this.BP.BY == 0 || this.BP.BY == 3 || this.BP.BY == 4) {
               this.BQ.addElement(this.BV);
               if (this.BP.typeObject == 3) {
                  this.BQ.addElement(this.BU);
               }

               if (!GameCanvas.isTouch) {
                  if (this.BP.typeObject == 3) {
                     super.DA = this.BU;
                  }

                  super.center = this.BV;
               }

               if (this.BP.typeObject == 3) {
                  super.DE = this.BU;
               }

               super.DF = this.BV;
            }

            if (this.BP.BY == 2) {
               this.BQ.addElement(this.BV);
               if (!GameCanvas.isTouch) {
                  super.center = this.BV;
               }

               super.DF = this.BV;
            }

         }
      }
   }

   public final void AA(mVector var1) {
      this.BN = var1;
      int var2 = 0;
      if (this.BN != null) {
         var2 = this.BN.size();
      }

      var2 = var2 * super.AN - super.AR + super.AS * 3;
      this.BO = new ListNew(super.AO, super.AP, super.AQ, super.AR, 0, 0, var2, true);
      this.AL();
      if (this == this.BZ.AC) {
         this.AB();
         this.AN();
      }

   }
}
