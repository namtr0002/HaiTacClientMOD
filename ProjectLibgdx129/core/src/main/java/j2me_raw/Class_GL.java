public final class Class_GL extends MainScreen {
   public static Class_GL AA;
   private String AB = "";
   private mVector AC = new mVector();
   private Class_DS AD;
   private int AE;
   private int AF;
   private int AG = 240;
   private int AH = 215;
   private int AI = 56;
   private int AJ = -1;
   private int AK;
   private iCommand AL;
   private iCommand AM;
   private iCommand AN;
   private iCommand AO;
   private int[][] AP = new int[][]{{190, 60}, {190, 125}};

   public Class_GL() {
      this.AE = MotherCanvas.hw - this.AG / 2;
      this.AF = MotherCanvas.hh - this.AH / 2;
      if (!GameCanvas.isTouch) {
         this.AF -= 18;
      }

      this.AD = new Class_DS(GameScreen.player.ID);
      this.AD.AC = GameScreen.player.name;
      this.AD.BC = GameScreen.player;
      this.AL = new iCommand(T.close, -1, this);
      this.AM = new iCommand(T.KI, 1, this);
      this.AN = new iCommand(T.KK, 2, this);
      this.AO = new iCommand(T.open, 3, this);
      if (GameCanvas.isTouch) {
         this.AM.setPos(this.AE + 190, this.AF + 169, (FrameImage)null, this.AM.caption);
         this.AN.setPos(this.AE + 190, this.AF + 169, (FrameImage)null, this.AN.caption);
         this.AL.setPos(this.AE + this.AG - 13, this.AF + 13, MainTab.fraCloseTab, "");
      } else {
         AvMain.AA(this.AM, 0);
         AvMain.AA(this.AN, 0);
         AvMain.AA(this.AL, 2);
         AvMain.AA(this.AO, 1);
         this.AJ = 0;
      }
   }

   public final void AD(int var1) {
      if (var1 > 0 && this.AM != null) {
         this.AM.AA(var1);
      }

   }

   public final void AA(byte var1) {
      this.AC.removeAllElements();
      this.AK = var1;
      if (var1 == 0) {
         AA.Show((MainScreen)GameCanvas.gameScr);
         AA.AB = T.QQ;
         this.AC.addElement(this.AM);
         this.AC.addElement(this.AL);
         super.DF = this.AM;
         super.backCMD = this.AL;
         super.center = this.AM;
         super.DB = this.AL;
         this.AM.timeSelect = 0;
         this.AM.AG = false;
      } else if (var1 == 1) {
         AA.AB = T.QO;
         this.AC.addElement(this.AN);
         this.AC.addElement(this.AL);
         super.DF = this.AN;
         super.backCMD = this.AL;
         super.center = this.AN;
         super.DB = this.AL;
      } else if (var1 == 2) {
         AA.AB = T.QP;
         super.DF = null;
         super.backCMD = null;
         super.center = null;
         super.DB = null;
      } else {
         if (var1 == 3) {
            AA.AB = T.QQ;
            this.AC.addElement(this.AM);
            this.AC.addElement(this.AL);
            super.DF = this.AM;
            super.backCMD = this.AL;
            super.center = this.AM;
            super.DB = this.AL;
            this.AM.timeSelect = 0;
            this.AM.AG = false;
         }

      }
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         GlobalService.getInstance().AK((byte)-1);
         GameCanvas.AB(T.BP, true);
      case 0:
      default:
         break;
      case 1:
         GlobalService.getInstance().AK((byte)1);
         break;
      case 2:
         GlobalService.getInstance().AK((byte)3);
         break;
      case 3:
         if (Player.QX[this.AJ] != null) {
            GlobalService.getInstance().AJ((byte)0, (short)Player.QX[this.AJ].ID);
         }
      }

      super.commandPointer(var1, var2);
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      if (!GameCanvas.isSmallScreen) {
         AvMain.AG(var1, this.AE, this.AF, this.AG, this.AH);
      }

      if (GameCanvas.lowGraphic) {
         AvMain.AC(var1, this.AE + 22, this.AF + 30, AvMain.DG + 6, AvMain.DH, 3);
      } else {
         AvMain.AC(var1, this.AE + 22, this.AF + 30, AvMain.DG, AvMain.DH, 3);
      }

      int var10002 = this.AE + 22;
      int var10003 = this.AF + 30;
      int var10005 = AvMain.DH;
      Class_HI.AA(var1, this.AD, var10002, var10003, AvMain.DG);
      AvMain.paintRect(var1, this.AE + 22, this.AF + this.AH - 22 - 5, this.AG - 44, 20, (byte)1, 1);
      String var2 = this.AB;
      int var3;
      if (this.AK == 1) {
         for(var3 = 0; var3 < GameCanvas.gameTick / 6 % 3; ++var3) {
            var2 = var2 + ".";
         }
      }

      mFont.tahoma_7b_white.drawString(var1, var2, this.AE + this.AG / 2, this.AF + this.AH - 12 - 6 - 5, 2);

      for(var3 = 0; var3 < Player.QX.length; ++var3) {
         if (GameCanvas.lowGraphic) {
            var1.setColor(-8440780);
            var1.drawRect(this.AE + this.AP[var3][0] + 3 - 30, this.AF + this.AP[var3][1] + 3 - 30, 54, 54);
            if (var3 == this.AJ) {
               var1.setColor(-6798);
               var1.drawRect(this.AE + this.AP[var3][0] + 2 - 30, this.AF + this.AP[var3][1] + 2 - 30, 56, 56);
            }

            if (Player.QX[var3] != null) {
               Player.QX[var3].AB(var1, this.AE + this.AP[var3][0] + 1, this.AF + this.AP[var3][1], this.AI);
            }
         } else {
            var1.drawRegion((mImage)AvMain.mImgRoomW[0], this.AE + this.AP[var3][0], this.AF + this.AP[var3][1], 3);
            if (Player.QX[var3] != null) {
               Player.QX[var3].AB(var1, this.AE + this.AP[var3][0] + 1, this.AF + this.AP[var3][1], this.AI);
            } else {
               var1.drawRegion((mImage)AvMain.mImgRoomW[1], this.AE + this.AP[var3][0], this.AF + this.AP[var3][1], 3);
            }

            if (!GameCanvas.isTouch && var3 == this.AJ) {
               var1.drawRegion(AvMain.mImgRoomW[4], 0, 70 * (GameCanvas.gameTick / 4 % 2), 72, 70, 0, this.AE + this.AP[var3][0], this.AF + this.AP[var3][1], 3);
            }
         }
      }

      if (!GameCanvas.isTouch) {
         super.paint(var1);
      } else {
         for(var3 = 0; var3 < this.AC.size(); ++var3) {
            iCommand var4;
            (var4 = (iCommand)this.AC.elementAt(var3)).paint(var1, var4.xCmd, var4.yCmd);
         }

      }
   }

   public final void update() {
      if (this.AM != null && this.AM.timeSelect == 0) {
         this.AM.AG = false;
      }

      if (super.KP != null) {
         super.KP.update();
      }

      for(int var1 = 0; var1 < Player.QX.length; ++var1) {
         if (Player.QX[var1] != null && Player.QX[var1].timeUse > 0) {
            int var10000;
            CountDownTicket var2;
            if ((var2 = Player.QX[var1].marketTime).timeCountDown > 0) {
               var2.updateTimeCountDownTicket();
               var10000 = var2.timeCountDown > 0 ? 1 : 0;
            } else {
               var10000 = -1;
            }

            if (var10000 == 0) {
               Player.QX[var1].timeUse = 0;
            }
         }
      }

   }

   public final void handleKeyPress() {
      boolean var1 = false;
      if (GameCanvas.isKeyPressed(1)) {
         if (this.AJ > 0) {
            --this.AJ;
         }

         GameCanvas.clearKeyPressed(1);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         if (this.AJ <= 0) {
            ++this.AJ;
         }

         GameCanvas.clearKeyPressed(3);
         var1 = true;
      }

      if (var1) {
         if (Player.QX[this.AJ] != null) {
            super.DA = this.AO;
            super.DE = this.AO;
         } else {
            super.DA = null;
            super.DE = null;
         }
      }

      super.handleKeyPress();
   }

   public final void updatePointer() {
      int var1;
      for(var1 = 0; var1 < this.AP.length; ++var1) {
         if (GameCanvas.isPointerSelect && GameCanvas.isPoint(this.AE + this.AP[var1][0] - this.AI / 2, this.AF + this.AP[var1][1] - this.AI / 2, this.AI, this.AI)) {
            GameCanvas.isPointerSelect = false;
            this.AJ = var1;
            if (Player.QX[var1] != null) {
               this.AO.AD();
            }
         }
      }

      for(var1 = 0; var1 < this.AC.size(); ++var1) {
         ((iCommand)this.AC.elementAt(var1)).AE();
      }

      super.updatePointer();
   }
}
