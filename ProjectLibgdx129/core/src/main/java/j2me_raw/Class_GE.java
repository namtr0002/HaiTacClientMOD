public class Class_GE extends Class_GT {
   public String AQ = "";
   public int AR = 0;
   public mVector AS = new mVector();
   public ListNew AT;
   public Class_DS AU;
   private int AA = 0;
   public int AV = 5;
   public int AW = 0;
   public int AX;
   public mVector AY;
   public String AZ = "";
   public iCommand BA;
   public iCommand BB;
   public iCommand BC;
   public iCommand BD;
   public iCommand BE;
   public iCommand BF;
   public iCommand BG;
   public int BH;
   public boolean BI = false;
   public boolean BJ = false;
   public int BK;

   public Class_GE(byte var1, mVector var2, String var3, int var4, int var5) {
      super(var1);
      this.AY = var2;
      this.AQ = T.DO;
      this.AZ = var3;
      super.BO = MotherCanvas.w - 30;
      super.BQ = 34;
      if (super.BO > var4) {
         super.BO = var4;
      }

      super.BP = var5;
      if (super.BP > MotherCanvas.h - GameCanvas.hCommand) {
         super.BP = MotherCanvas.h - GameCanvas.hCommand;
      }

      super.BM = MotherCanvas.hw - super.BO / 2;
      super.BN = MotherCanvas.hh - super.BP / 2;
      this.AR = super.BP - GameCanvas.hCommand - 10 - iCommand.hButtonCmdNor;
      this.BA = new iCommand(T.CO, 0, 0, this);
      this.BB = new iCommand(T.close, 2, 0, this);
      this.BF = new iCommand(T.FF, -1, 0, this);
      this.BG = new iCommand(T.AU, 100, 0, this);
      this.AS.removeAllElements();
      this.AS.addElement(this.BA);
      if (GameCanvas.isTouch) {
         this.BB.setPos(super.BM + 20 + super.BO - 40, super.BN + GameCanvas.hCommand / 2 - 2 + 8, MainTab.fraCloseTab, "");
         super.DB = this.BB;
      } else {
         this.AS.addElement(this.BB);
      }

      this.AX = 0;
      this.AT = new ListNew();
      this.AA(0, this.AS);
      this.BC = new iCommand(T.AZ, 4, 0, this);
      this.BD = new iCommand(T.AD, 5, 0, this);
      this.BE = new iCommand(T.CN, 6, 0, this);
      super.backCMD = this.BB;
      super.DF = this.BG;
      super.DE = this.BA;
   }

   public void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         if (this.AU != null) {
            GlobalService.getInstance().AA(this.AU.AG);
            return;
         }
         break;
      case 0:
         this.AB();
         return;
      case 1:
         this.AA();
         return;
      case 2:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         return;
      case 4:
         if (this.AU != null) {
            GameScreen var10000 = GameCanvas.gameScr;
            GameScreen.AA(this.AU.AC);
            return;
         }
         break;
      case 5:
         if (this.AU != null) {
            GameCanvas.chatTabScr.AB(this.AU.AC, "", "", (byte)0, true);
            GameCanvas.chatTabScr.Show((MainScreen)GameCanvas.gameScr);
            return;
         }
         break;
      case 6:
         if (this.AU != null) {
            GlobalService.getInstance().AA((byte)0, (int)this.AU.AG);
            return;
         }
         break;
      case 100:
         this.AA();
      }

   }

   public void AF() {
      int var1 = this.AY.size() * super.BQ - this.AR + (this.AV << 1);
      this.AT = new ListNew(super.BM, super.BN + GameCanvas.hCommand, super.BO, this.AR, 0, 0, var1, true);
   }

   public final void Show(MainScreen var1) {
      super.Show(var1);
      this.g_();
      this.AH();
   }

   public void g_() {
   }

   public void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      if (GameCanvas.currentScreen != GameCanvas.chatTabScr) {
         GameCanvas.resetTrans(var1);
         this.AB(var1);
         var1.AD(super.BM, super.BN + GameCanvas.hCommand + this.AV, super.BO - 30, this.AR - this.AV);
         mGraphics.AC();
         int var10000 = super.BM;
         var10000 = super.BN;
         var10000 = GameCanvas.hCommand;
         var10000 = this.AV;
         var10000 = super.BO;
         var10000 = this.AR;
         var10000 = this.AV;
         mGraphics.AD();
         var1.translate(0, -this.AT.AC);
         int var2 = super.BM + 30;
         int var3 = super.BN + GameCanvas.hCommand + 10;
         int var4;
         if (this.BI) {
            MsgDialog.fraImgWaiting.drawFrame(GameCanvas.gameTick / 6 % MsgDialog.fraImgWaiting.nFrame, super.BM + super.BO / 2, var3 + super.BQ, 0, 3, var1);
         } else if (this.AY.size() == 0) {
            mFont.tahoma_7_black.drawString(var1, this.AQ, super.BM + super.BO / 2, super.BN + super.BP / 2, 2);
         } else {
            if (this.AW >= 0 && (GameCanvas.isKeyPressed() || this.AA > 0)) {
               this.AA(var1, var2, var3 - 2, super.BO - 40);
            }

            for(var4 = 0; var4 < this.AY.size(); ++var4) {
               Class_DS var5 = (Class_DS)this.AY.elementAt(var4);
               if (var4 == 0) {
                  this.BK = var3;
               }

               this.AA(var1, var5, var2, var3, var4, super.BO - 60);
               var3 += super.BQ;
            }
         }

         mGraphics.AE();
         mGraphics.restoreCanvas();
         GameCanvas.resetTrans(var1);
         if (this.AS != null) {
            for(var4 = 0; var4 < this.AS.size(); ++var4) {
               iCommand var6;
               (var6 = (iCommand)this.AS.elementAt(var4)).paint(var1, var6.xCmd, var6.yCmd);
            }
         }

         if (super.DB != null) {
            super.DB.paint(var1, super.DB.xCmd, super.DB.yCmd);
         }

      }
   }

   public void AB(mGraphics var1) {
      this.AE(var1, super.BM, super.BN, super.BO, super.BP, super.BP);
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.BM + 20, super.BN + GameCanvas.hCommand / 2 - 2, super.BO - 40, 16, 4, 4);
      mFont.tahoma_7b_red.drawString(var1, this.AZ, super.BM + super.BO / 2, super.BN + GameCanvas.hCommand / 2, 2);
   }

   public void AA(mGraphics var1, int var2, int var3, int var4) {
      var2 -= 10;
      var1.setColor(-4147789);
      var1.fillRect(var2 + this.AV / 2, var3 + this.AW * super.BQ, var4 - this.AV / 2, super.BQ);
   }

   public void AA(mGraphics var1, Class_DS var2, int var3, int var4, int var5, int var6) {
   }

   public void update() {
      this.AT.AC();
      if (super.KP != null) {
         super.KP.update();
      }

      if (!GameCanvas.menuCur.isShowMenu && GameCanvas.currentDialog == null && this.AA > 0) {
         --this.AA;
      }

   }

   public void handleKeyPress() {
      if (this.AS != null) {
         int var1 = this.AS.size();
         if (GameCanvas.isKeyPressed() && var1 > 0) {
            int var2 = this.AX;
            if (GameCanvas.isKeyPressed(0)) {
               --this.AX;
               GameCanvas.clearKeyPressed(0);
            } else if (GameCanvas.isKeyPressed(2)) {
               ++this.AX;
               GameCanvas.clearKeyPressed(2);
            }

            this.AX = AvMain.AA(this.AX, var1 - 1, false);
            if (var2 != this.AX && GameCanvas.isKeyPressed()) {
               for(var2 = 0; var2 < var1; ++var2) {
                  iCommand var3 = (iCommand)this.AS.elementAt(var2);
                  if (var2 == this.AX) {
                     var3.AG = true;
                  } else {
                     var3.AG = false;
                  }
               }
            }
         }
      }

      boolean var4 = false;
      if (GameCanvas.isKeyPressed(1)) {
         --this.AW;
         GameCanvas.clearKeyPressed(1);
         var4 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         ++this.AW;
         GameCanvas.clearKeyPressed(3);
         var4 = true;
      }

      if (var4) {
         this.AW = AvMain.AA(this.AW, this.AY.size() - 1, false);
         this.AT.AA((this.AW + 1) * super.BQ - this.AR / 2);
      }

      if (GameCanvas.AL[5]) {
         GameCanvas.AB(5);
         if (this.AS != null && this.AX < this.AS.size()) {
            ((iCommand)this.AS.elementAt(this.AX)).AD();
         }
      }

      this.AS();
   }

   public void updatePointer() {
      this.AT.update_Pos_UP_DOWN();
      super.updatePointer();
      int var1;
      if (this.AS != null) {
         for(var1 = 0; var1 < this.AS.size(); ++var1) {
            ((iCommand)this.AS.elementAt(var1)).AE();
         }
      }

      if (GameCanvas.isPointerSelect && this.AY.size() > 0 && GameCanvas.isPoint(super.BM, super.BN + GameCanvas.hCommand, super.BO, this.AR)) {
         GameCanvas.isPointerSelect = false;
         if ((var1 = (GameCanvas.AZ - (super.BN + GameCanvas.hCommand) + this.AT.AC) / super.BQ) >= 0 && var1 < this.AY.size()) {
            if (this.BJ) {
               if (GameCanvas.AY < super.BM + 30 + (super.BO - 60) + 15 && GameCanvas.AY > super.BM + 30 + (super.BO - 60) - 15 && var1 > 0) {
                  Class_DS var2 = (Class_DS)this.AY.elementAt(var1);
                  this.AA(var2);
                  return;
               }

               this.AW = var1;
               this.AA();
               this.AA = 5;
               return;
            }

            this.AW = var1;
            this.AA();
            this.AA = 5;
         }
      }

   }

   public final void AH() {
      this.AF();
      if (!GameCanvas.isTouch) {
         if (this.AY.size() == 0) {
            this.AW = -1;
         }

         if (this.AW >= this.AY.size()) {
            this.AW = 0;
         }
      }

   }

   public void AA() {
   }

   public void AB() {
   }

   public void AA(Class_DS var1) {
   }

   public final void AE(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      var1.AD(super.BM, super.BN + this.BH - 1, super.BO, this.AR - this.AV);
      mGraphics.AC();
      int var10000 = super.BM;
      var10000 = super.BN;
      var10000 = this.BH;
      var10000 = super.BO;
      var10000 = this.AR;
      var10000 = this.AV;
      mGraphics.AD();
      var1.translate(0, -this.AT.AC);
   }
}
