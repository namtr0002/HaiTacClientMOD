public class MainTabShop extends MainTab {
   public mVector vecShop = new mVector("MainTabShop.vecShop");
   public mVector vecInfoSS = new mVector("MainTabShop.vecInfoSS");
   private mVector CD = new mVector();
   public MainItem itemCur;
   public static MainItem BQ;
   public static boolean BR = false;
   public byte BS = 0;
   public iCommand cmdMenu;
   public iCommand BU;
   public InputDialog input;
   public static int BW = 5;
   public static int BX;
   public ListNew BY;
   public Scroll BZ = new Scroll();
   public static boolean CA = false;
   public int CB = 0;
   public static MainTabShop mainTab;

   public MainTabShop(String var1, mVector var2, int var3, int var4) {
      mainTab = this;
      super.nameTab = var1;
      this.vecShop = var2;
      super.AQ = MainTab.AE * BW;
      super.AO = var4 + MainTab.AG / 2 - super.AQ / 2 + 10;
      super.AP = MainTab.AI + 32;
      super.AD = var3;
      int var5 = ((var3 - 1) / BW + 1) * MainTab.AE - super.AR + super.AS;
      this.BY = new ListNew(super.AO, super.AP, super.AQ, super.AR, 0, 0, var5, true);
      this.BZ.AA(super.AO + super.AQ + super.AS, super.AP + super.AS / 2, super.AR - (super.AS << 1), -7967666);
   }

   public final void AB() {
      super.AV = false;
      super.AX = 0;
      if (GameCanvas.isKeyPressed()) {
         super.IdSelect = 0;
         if (this.vecShop.size() > 0) {
            this.AB(this.getMenuActionItem());
         }
      } else {
         super.IdSelect = -1;
         this.AB((mVector)null);
         this.itemCur = null;
         if (this.BS == 101) {
            super.IdSelect = 0;
            if (this.vecShop.size() > 0) {
               this.AB(this.getMenuActionItem());
            }
         }
      }

      if (CA) {
         this.vecShop = MainItem.SortVecItem(this.vecShop);
         CA = false;
      }

   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      AvMain.paintRect(var1, super.AO, super.AP, super.AQ, super.AR - super.AS, (byte)0, 3);
      var1.AD(super.AO - 1, super.AP + 1, super.AQ + 2, super.AR - 1 - super.AS);
      mGraphics.AC();
      int var10000 = super.AO;
      var10000 = super.AP;
      var10000 = super.AQ;
      var10000 = super.AR;
      var10000 = super.AS;
      mGraphics.AD();
      var1.translate(super.AO, super.AP);
      var1.translate(0, -this.BY.AC);

      int var2;
      MainScreen var5;
      for(var2 = 0; var2 < this.vecShop.size(); ++var2) {
         if (var2 / BW * MainTab.AE + MainTab.AE / 2 + MainTab.AE / 2 - 2 >= this.BY.AC && var2 / BW * MainTab.AE + MainTab.AE / 2 - MainTab.AE / 2 + 2 <= this.BY.AC + (super.AR - 1 - super.AS)) {
            MainItem var3;
            if ((var3 = (MainItem)this.vecShop.elementAt(var2)).typeObject == 3 || var3.typeObject == 102 || var3.typeObject == 103) {
               var3.AC(var1, var2 % BW * MainTab.AE + MainTab.AE / 2, var2 / BW * MainTab.AE + MainTab.AE / 2, MainTab.AE);
            }

            var3.paint(var1, var2 % BW * MainTab.AE + MainTab.AE / 2, var2 / BW * MainTab.AE + MainTab.AE / 2, MainTab.AE);
            if (var3.typeObject == 4 && super.indexIconTab == 0) {
               DelaySkill.getDelay(var3.indexHotKey).AA(var1, var2 % BW * MainTab.AE + 1, var2 / BW * MainTab.AE + 1, MainTab.AE - 1);
            }

            this.AA(var1, var3, var2 % BW * MainTab.AE + MainTab.AE - 1, var2 / BW * MainTab.AE + MainTab.AE - 1);
            if (super.IdSelect == var2) {
               var5 = GameCanvas.currentScreen;
               if (MainScreen.AD((byte)1)) {
                  var1.setColor(-1);
                  var1.drawRect(var2 % BW * MainTab.AE + 1, var2 / BW * MainTab.AE + 1, MainTab.AE - 2, MainTab.AE - 2);
                  if (!GameCanvas.isSmallScreen) {
                     var1.drawRect(var2 % BW * MainTab.AE + 2, var2 / BW * MainTab.AE + 2, MainTab.AE - 4, MainTab.AE - 4);
                  }
               }
            }
         }
      }

      if (super.AD % BW != 0) {
         for(var2 = super.AD; var2 < super.AD + (BW - super.AD % BW); ++var2) {
            var1.drawRegion(AvMain.imgDelay, 0, 0, MainTab.AE - 1, MainTab.AE - 1, 0, var2 % BW * MainTab.AE + 1, var2 / BW * MainTab.AE + 1, 0);
         }
      }

      var1.setColor(-2701384);

      for(var2 = 0; var2 < BW - 1; ++var2) {
         var1.fillRect(MainTab.AE + var2 * MainTab.AE, 1, 1, MainTab.AE * ((super.AD - 1) / BW + 1));
      }

      for(var2 = 0; var2 <= (super.AD - 1) / BW + 1; ++var2) {
         var1.fillRect(1, var2 * MainTab.AE, super.AQ - 1, 1);
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      var1.setColor(-9811158);
      var1.drawRect(super.AO, super.AP + 2, 0, super.AR - super.AS - 4);
      this.AB(var1, MainTab.xTab + 22, MainTab.AI + MainTab.AH - 22);
      var5 = GameCanvas.currentScreen;
      if (MainScreen.AD((byte)1)) {
         if (this.BY.AD > 0) {
            this.BZ.AA(var1);
         }

         if (super.AV && this.itemCur != null) {
            this.AA(var1, this.itemCur, this.vecInfoSS, (byte)0, super.AL, super.AM, false, (MainObject)null, TabInventory.CZ);
            if (this.BU != null) {
               this.BU.AB(var1, this.BU.xCmd, this.BU.yCmd);
            }
         }

         if (this.CD != null && GameCanvas.AL()) {
            for(var2 = 0; var2 < this.CD.size(); ++var2) {
               iCommand var4;
               (var4 = (iCommand)this.CD.elementAt(var2)).paint(var1, var4.xCmd, var4.yCmd);
            }
         }

         super.paint(var1);
      }

   }

   public void AA(mGraphics var1, MainItem var2, int var3, int var4) {
   }

   public void AB(mGraphics var1, int var2, int var3) {
   }

   public final void update() {
      int var1 = this.BY.AC;
      this.BY.AC();
      this.BZ.AA(this.BY.AC, this.BY.AD);
      if (this.BY.AC == var1 && !this.BY.AE) {
         if (this.itemCur != null) {
            this.AF();
         }
      } else {
         super.AV = false;
      }

      if (this.itemCur != null && this.itemCur.CE) {
         this.itemCur = null;
         this.AP();
      }

      if (BR) {
         MainTabShop var4 = this;
         BR = false;
         if (this.itemCur != null && BQ != null) {
            for(int var2 = 0; var2 < var4.vecShop.size(); ++var2) {
               MainItem var3;
               if ((var3 = (MainItem)var4.vecShop.elementAt(var2)).typeObject == BQ.typeObject && var3.ID == BQ.ID) {
                  var4.IdSelect = var2;
                  var4.AH();
                  break;
               }
            }
         }
      }

      this.AL();
      if (this.CB > 0) {
         --this.CB;
      }

   }

   public void AL() {
   }

   public final void handleKeyPress() {
      boolean var1 = false;
      if (GameCanvas.isKeyPressed(0)) {
         if (super.IdSelect % BW == 0) {
            MainScreen var10000 = GameCanvas.currentScreen;
            MainScreen.AC((byte)0);
         } else {
            --super.IdSelect;
         }

         GameCanvas.clearKeyPressed(0);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(2)) {
         ++super.IdSelect;
         GameCanvas.clearKeyPressed(2);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(1)) {
         if (super.IdSelect >= BW) {
            super.IdSelect -= BW;
         }

         GameCanvas.clearKeyPressed(1);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         if (super.IdSelect < this.vecShop.size() - BW) {
            super.IdSelect += BW;
         }

         GameCanvas.clearKeyPressed(3);
         var1 = true;
      }

      if (var1) {
         this.AP();
      }

      super.handleKeyPress();
      this.AS();
   }

   public final void updatePointer() {
      this.BY.update_Pos_UP_DOWN();
      int var1;
      if (GameCanvas.AB(super.AO, super.AP, super.AQ, super.AR)) {
         var1 = (GameCanvas.AY - super.AO) / MainTab.AE + (GameCanvas.AZ - super.AP + this.BY.AC) / MainTab.AE * BW;
         int var2 = this.vecShop.size();
         if (var1 >= 0 && var1 < var2) {
            GameCanvas.isPointerSelect = false;
            if (var1 == super.IdSelect) {
               (new StringBuffer("menu perform id ")).append(super.IdSelect).toString();
               this.cmdMenu.AD();
            } else {
               super.AV = false;
               super.IdSelect = var1;
               this.AB(this.getMenuActionItem());
            }

            MainScreen var10000 = GameCanvas.currentScreen;
            if (!MainScreen.AD((byte)1)) {
               var10000 = GameCanvas.currentScreen;
               MainScreen.AC((byte)1);
            }
         } else {
            this.itemCur = null;
            super.AV = false;
            super.IdSelect = -1;
            this.AB((mVector)null);
         }
      }

      if (this.CD != null) {
         for(var1 = 0; var1 < this.CD.size(); ++var1) {
            ((iCommand)this.CD.elementAt(var1)).AE();
         }
      }

      if (super.AV && this.BU != null) {
         this.BU.AE();
      }

      super.updatePointer();
   }

   public mVector getMenuActionItem() {
      return null;
   }

   public final void AB(mVector var1) {
      super.DA = null;
      super.center = null;
      this.CD.removeAllElements();
      if (var1 != null) {
         iCommand var2;
         if ((var2 = this.AN()) != null) {
            var1.addElement(var2);
         }

         if (var1.size() > 2) {
            if (GameCanvas.isTouch) {
               this.cmdMenu = AvMain.AA(this.cmdMenu, 0);
               this.CD.addElement(this.cmdMenu);
               super.DF = this.cmdMenu;
            } else {
               super.center = this.cmdMenu;
            }
         } else {
            iCommand var3;
            int var4;
            if (GameCanvas.isTouch) {
               this.CD = var1;

               for(var4 = 0; var4 < this.CD.size(); ++var4) {
                  var3 = AvMain.AA((iCommand)this.CD.elementAt(var4), var4);
                  if (var4 == 0) {
                     super.DF = var3;
                  }

                  if (var4 == 1) {
                     super.DE = var3;
                  }
               }

            } else {
               for(var4 = 0; var4 < var1.size(); ++var4) {
                  var3 = (iCommand)var1.elementAt(var4);
                  if (var4 == 0) {
                     super.center = var3;
                  }

                  if (var4 == 1) {
                     super.DA = var3;
                  }
               }

            }
         }
      }
   }

   public iCommand AN() {
      return null;
   }

   public final void AG() {
      this.AA(this.itemCur, super.AO + MainTab.AG, super.AP + (super.IdSelect / BW + 1) * MainTab.AE - this.BY.AC + 4);
      if (GameCanvas.isTouch) {
         this.AO();
      }

   }

   public void AO() {
   }

   public final void AH() {
      MainItem var1;
      if ((var1 = (MainItem)this.vecShop.elementAt(super.IdSelect)) != null && this.itemCur != null && var1 != this.itemCur) {
         this.itemCur = var1;
         super.AV = false;
      }

   }

   public final void AP() {
      super.AV = false;
      super.IdSelect = AvMain.AA(super.IdSelect, this.vecShop.size() - 1, false);
      this.BY.AA((super.IdSelect / BW + 1) * MainTab.AE - super.AR / 2);
      if (super.IdSelect >= 0) {
         this.AB(this.getMenuActionItem());
      }

   }
}
