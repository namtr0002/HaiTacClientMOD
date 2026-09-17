public final class TabScreen extends MainScreen {
   public static boolean AA = false;
   public mVector AB = new mVector("TabScreen.vecTabs");
   private static mVector AH = new mVector("TabScreen.vecEff");
   public MainTab AC = null;
   private iCommand AI;
   public byte AD = 0;
   private byte AJ;
   public int AE = 0;
   public boolean AF = false;
   public int AG = 0;

   public TabScreen(int var1, byte var2) {
      int[] var10000 = new int[]{0, 1, 2, 5, 3, 4};
      this.AG = var1;
      this.AJ = 0;
      this.AI = new iCommand(T.close, 0, this);
      var1 = this.AG + 22 + (MainTab.AG - 22) / 2 - MainTab.AG / 4 * 3 / 2 + MainTab.AG / 4 * 3;
      if (GameCanvas.isTouch) {
         this.AI.setPos(var1, MainTab.AI + 7 + 8, MainTab.fraCloseTab, "");
      }

      super.DB = this.AI;
      super.backCMD = this.AI;
   }

   public final void Show(MainScreen var1) {
      if (!GameCanvas.isTouch) {
         this.AD = 0;
         this.AF();
         this.AG();
      } else {
         this.AD = 1;
         this.AF();
         this.AC.AB();
      }

      super.Show(var1);
   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public final void commandPointer(int var1, int var2) {
      (new StringBuffer("vào commandPointer trong TabScreen: ")).append(var1).toString();
      switch(var1) {
      case 0:
         if (GameCanvas.isTouch) {
            super.KP.Show(super.KP.KP);
            if (GameScreen.CV == 16) {
               GameScreen.AB((int)16, (int)1);
               return;
            }
         } else if (this.AD == 0) {
            super.KP.Show(super.KP.KP);
            return;
         } else if (this.AD == 1) {
            this.AD = 0;
         }
      default:
      }
   }

   public final void addVecTab(mVector var1) {
      this.AB = var1;
      if (this.AC == null && this.AB.size() > 0) {
         this.AC = (MainTab)this.AB.elementAt(0);
         this.AE = 0;
      }

   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      MainTab.AA(var1, this.AG, this.AF, this.AJ);
      MainTab.AA(var1, this.AG, this.AB, this.AE);
      Interface_Game.paintNumMess(var1, -Interface_Game.xNumMess + 8, 0);
      super.paint(var1);
      if (this.AC != null) {
         this.AC.paint(var1);
         if (this.AC.indexIconTab == 2 && GameCanvas.isTouch) {
            this.AI.paint(var1, this.AI.xCmd, this.AI.yCmd);
         }
      }

      for(int var2 = 0; var2 < AH.size(); ++var2) {
         MainEffect var3;
         if ((var3 = (MainEffect)AH.elementAt(var2)) != null && !var3.BI && !var3.isStop) {
            var3.paint(var1);
         }
      }

   }

   public final void update() {
      ++MainTab.BL;
      if (super.KP != null) {
         super.KP.update();
      }

      if (!GameCanvas.isTouch) {
         if (this.AD == 0) {
            if (this.AI.caption != T.close) {
               this.AI.caption = T.close;
            }
         } else if (this.AD == 1 && this.AI.caption != T.AG) {
            this.AI.caption = T.AG;
         }
      }

      if (this.AC != null) {
         this.AC.update();
         if (AA) {
            AA = false;
            this.AC.AH();
         }
      }

      for(int var1 = 0; var1 < AH.size(); ++var1) {
         MainEffect var2;
         if ((var2 = (MainEffect)AH.elementAt(var1)) != null && !var2.BI && !var2.isStop) {
            var2.update();
         }
      }

   }

   public final void handleKeyPress() {
      if (this.AD == 0) {
         int var1 = this.AE;
         if (GameCanvas.isKeyPressed(1)) {
            --this.AE;
            GameCanvas.clearKeyPressed(1);
         } else if (GameCanvas.isKeyPressed(3)) {
            ++this.AE;
            GameCanvas.clearKeyPressed(3);
         }

         if (GameCanvas.isKeyPressed(0) || GameCanvas.isKeyPressed(2)) {
            this.AD = 1;
            this.AC.AB();
            GameCanvas.clearKeyPressed(0);
            GameCanvas.clearKeyPressed(2);
         }

         if (var1 != this.AE) {
            MainTab.BL = 0;
            this.AE = AvMain.AA(this.AE, this.AB.size() - 1, true);
            this.AF();
            this.AG();
            this.AC.AB();
         }
      } else {
         if (this.AC != null) {
            this.AC.handleKeyPress();
         }

         if (this.AD == 0) {
            this.AG();
         }
      }

      super.handleKeyPress();
      this.AS();
   }

   public final void updatePointer() {
      int var1 = this.AG + 22 - MainTab.AJ / 2;
      int var2;
      if ((var2 = MainTab.AI + 36 - MainTab.AJ / 2) + this.AB.size() * MainTab.AJ > MainTab.AI + MainTab.AH) {
         var2 = MainTab.AI + MainTab.AH / 2 - this.AB.size() * MainTab.AJ / 2;
      }

      if (GameCanvas.AB(var1, var2, MainTab.AJ, MainTab.AJ * this.AB.size())) {
         var1 = (GameCanvas.AZ - var2) / MainTab.AJ;
         var2 = this.AB.size();
         if (var1 >= 0 && var1 < var2) {
            GameCanvas.isPointerSelect = false;
            if (var1 != this.AE) {
               this.AE = var1;
               this.AF();
               this.AC.AB();
            }

            MainScreen var10000 = GameCanvas.currentScreen;
            if (!MainScreen.AD((byte)1)) {
               var10000 = GameCanvas.currentScreen;
               MainScreen.AC((byte)1);
            }
         }
      }

      if (this.AC != null) {
         this.AC.updatePointer();
      }

      super.updatePointer();
   }

   public final void AF() {
      this.AC = (MainTab)this.AB.elementAt(this.AE);
   }

   private void AG() {
      super.DA = null;
      super.center = null;
      super.DB = this.AI;
      if (this.AC != null) {
         super.DA = this.AC.setCmdEndInfo();
      }

   }

   public static void AA(String var0, int var1, int var2, byte var3, FrameImage var4, int var5) {
      Class_BN var6 = new Class_BN(var0, var1, var2, 10, var4, var5);
      if ((var1 = GameScreen.AA(AH)) == AH.size()) {
         AH.addElement(var6);
      } else {
         AH.setElementAt(var6, var1);
      }
   }

   public final void AA(byte var1, mVector var2) {
      if (var1 >= 0 && var1 < this.AB.size()) {
         for(int var3 = 0; var3 < this.AB.size(); ++var3) {
            MainTab var4;
            if ((var4 = (MainTab)this.AB.elementAt(var3)).AU == var1) {
               var4.AA(var2);
               return;
            }
         }
      }

   }
}
