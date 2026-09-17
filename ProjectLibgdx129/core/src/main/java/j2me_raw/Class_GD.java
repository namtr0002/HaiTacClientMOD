public final class Class_GD extends Class_GE {
   public static Class_GD AA;
   private int AB = 0;
   private mVector AC = new mVector();
   private int AD = 0;
   private Scroll AE = new Scroll();
   private int AF = 93;

   public Class_GD(mVector var1, int var2) {
      super((byte)0, var1, T.UE, 200, 180);
      this.AB = var2;
      if (!GameCanvas.isTouch) {
         if (super.BO > 160) {
            super.BO = 160;
         }

         super.BM = MotherCanvas.hw - super.BO / 2;
      }

      super.BH = 55;
      super.BQ = 53;
      super.AR = super.BP - super.BH - 10;
      super.AS.removeAllElements();
      if (!GameCanvas.isTouch) {
         super.AS.addElement(super.BA);
      }

      if (GameCanvas.isTouch) {
         super.BB.setPos(super.BM + 20 + super.BO - 36, super.BN + 16 + 8, MainTab.fraCloseTab, "");
      } else {
         super.AS.addElement(super.BB);
      }

      super.BC.indexMenu = 7;
      super.BC.AF = -1;

      for(int var3 = 0; var3 < super.AY.size(); ++var3) {
         Class_DS var4;
         iCommand var5;
         if ((var4 = (Class_DS)super.AY.elementAt(var3)).BH.AC == 0) {
            var5 = new iCommand(T.DY, 7, var3, this);
         } else if (var4.BH.AC == 1) {
            (var5 = new iCommand(T.BY, 8, var3, this)).AC();
         } else {
            (var5 = new iCommand(T.UB, 9, var3, this)).AB();
         }

         var5.setPos(super.BM + 150, super.BN + super.BH + super.BQ * var3 + 28, AvMain.fraCmdNhanNapThe, var5.caption);
         this.AC.addElement(var5);
         if (this.AD == 0) {
            this.AD = var5.AL;
         }
      }

      this.AA(super.AS);
      this.AE.AA(super.BM + super.BO - 20 + super.AV, super.BN + super.BH - 1, super.AR - super.AV, -7967666);
   }

   public final void AA(byte var1) {
      iCommand var2;
      (var2 = (iCommand)this.AC.elementAt(var1)).caption = T.UB;
      var2.indexMenu = 9;
      var2.AB();
   }

   public final void commandPointer(int var1, int var2) {
      Class_DS var3;
      switch(var1) {
      case 7:
         if (var2 != -1) {
            super.AW = var2;
         }

         var3 = (Class_DS)super.AY.elementAt(super.AW);
         (Class_GC.AA = new Class_GC(super.BL, var3)).Show((MainScreen)GameCanvas.gameScr);
         break;
      case 8:
         super.AW = var2;
         var3 = (Class_DS)super.AY.elementAt(super.AW);
         GlobalService.getInstance().AH((byte)1, (byte)((byte)var3.AG));
         break;
      case 9:
         super.AW = var2;
         var3 = (Class_DS)super.AY.elementAt(super.AW);
         GlobalService.getInstance().AH((byte)1, (byte)((byte)var3.AG));
      }

      super.commandPointer(var1, var2);
   }

   public final void AA() {
      if (super.AY.size() != 0) {
         super.AU = (Class_DS)super.AY.elementAt(super.AW);
         if (super.AU != null) {
            mVector var1;
            (var1 = new mVector()).addElement(super.BC);
            GameCanvas.menu.startAt(var1, 2, T.AZ);
         }

      }
   }

   public final void AB() {
      mVector var1 = new mVector();
      String var2 = T.AU;
      if (super.AY.size() > 0) {
         super.AU = (Class_DS)super.AY.elementAt(super.AW);
         if (super.AU != null && !GameCanvas.isTouch) {
            iCommand var3 = (iCommand)this.AC.elementAt(super.AW);
            var1.addElement(var3);
         }
      }

      GameCanvas.menu.startAt(var1, 2, var2);
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      if (GameCanvas.currentScreen != GameCanvas.chatTabScr) {
         GameCanvas.resetTrans(var1);
         this.AB(var1);
         super.BB.paint(var1, super.BB.xCmd, super.BB.yCmd);
         int var2 = super.BM + 20;
         int var3 = super.BN + super.BH;
         this.AE(var1);
         int var4;
         if (super.BI) {
            MsgDialog.fraImgWaiting.drawFrame(GameCanvas.gameTick / 6 % MsgDialog.fraImgWaiting.nFrame, super.BM + super.BO / 2, var3 + super.BQ, 0, 3, var1);
         } else {
            if (super.AW >= 0) {
               this.AA(var1, var2, var3 - 1, super.BO - 40);
            }

            for(var4 = 0; var4 < super.AY.size(); ++var4) {
               if (var3 - super.AT.AC + super.BQ >= super.BH + super.BN && var3 - super.AT.AC - super.BQ <= super.BH + super.AR + super.BN) {
                  Class_DS var5 = (Class_DS)super.AY.elementAt(var4);
                  this.AA(var1, var5, var2, var3, var4, super.BO - 40);
               }

               var3 += super.BQ;
            }
         }

         mGraphics.AE();
         mGraphics.restoreCanvas();
         GameCanvas.resetTrans(var1);
         if (super.AT.AD > 0) {
            this.AE.AA(var1);
         }

         if (super.AS != null) {
            for(var4 = 0; var4 < super.AS.size(); ++var4) {
               iCommand var6;
               (var6 = (iCommand)super.AS.elementAt(var4)).paint(var1, var6.xCmd, var6.yCmd);
            }
         }

         if (super.DB != null) {
            super.DB.paint(var1, super.DB.xCmd, super.DB.yCmd);
         }

      }
   }

   public final void AA(mGraphics var1, Class_DS var2, int var3, int var4, int var5, int var6) {
      if (var2 != null) {
         var1.setColor(-2573687);
         var1.fillRect(var3, var4, var6, 48);
         var1.setColor(-5271191);
         var1.fillRect(var3 + 2, var4 + 2, var6 - 4, 11);
         AvMain.AA(var1, T.UG + " " + Interface_Game.AA(var2.BH.AB) + " " + T.QF, var3 + var6 / 2, var4 + 2, 2, (byte)5);
         if (GameCanvas.isTouch) {
            iCommand var7;
            iCommand var10000 = var7 = (iCommand)this.AC.elementAt(var5);
            int var10002 = var7.xCmd;
            var10002 = var7.yCmd;
            var10000.AA(var1);
         }

         int var8;
         if (GameCanvas.isTouch) {
            var8 = (var6 - this.AF - this.AD - 5) / 2;
         } else {
            var8 = (var6 - this.AF) / 2;
         }

         if (var8 < 0) {
            var8 = 0;
         }

         if (super.AW == var5 && var4 - super.AT.AC + 16 > super.BN + super.BH && var4 - super.AT.AC + 16 + 29 < super.BN + super.BH + super.AR - super.AV) {
            var1.AD(var3 + var8, var4, this.AF + 1, super.BQ);
            mGraphics.AC();
            int var9 = this.AF;
            var9 = super.BQ;
            mGraphics.AD();
            var2.BH.AF = true;
            var2.BH.AA(var1, var3 + var8, var4 + 2 + 14);
            mGraphics.restoreCanvas();
            this.AE(var1);
         } else {
            var2.BH.AF = false;
            var2.BH.AA(var1, var3 + var8, var4 + 2 + 14);
         }
      }
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      var1.setColor(-4840926);
      var1.drawRect(var2 - 1, var3 + super.AW * super.BQ, var4 + 1, 49);
   }

   public final void AB(mGraphics var1) {
      int var10006 = super.BO;
      this.AD(var1, super.BM, super.BN, super.BO, super.BP, 0);
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.BM + 20, super.BN + 16, super.BO - 40, 16, 4, 4);
      AvMain.FontBorderColor(var1, super.AZ, super.BM + super.BO / 2, super.BN + 18, 2, (int)6, (int)5);
      AvMain.FontBorderColor(var1, T.QF + " :", super.BM + super.BO / 2 - 2, super.BN + 37, 1, (int)1, (int)7);
      mFont.tahoma_7b_black.drawString(var1, Interface_Game.AA(this.AB), super.BM + super.BO / 2 + 2, super.BN + 38, 0);
   }

   public final void update() {
      super.update();

      for(int var1 = 0; var1 < super.AY.size(); ++var1) {
         ((Class_DS)super.AY.elementAt(var1)).BH.AA();
      }

      this.AE.AA(super.AT.AC, super.AT.AD);
   }

   public final void updatePointer() {
      super.AT.update_Pos_UP_DOWN();
      if (GameCanvas.isTouch) {
         if (super.DA != null) {
            if (super.DA.AF()) {
               super.DA.AE();
            } else if (GameCanvas.AB(0, MotherCanvas.h - GameCanvas.hCommand - 5, GameCanvas.BG << 1, GameCanvas.hCommand + 10)) {
               super.DA.AD();
            }
         }

         if (super.DB != null) {
            if (super.DB.AF()) {
               super.DB.AE();
            } else if (GameCanvas.AB(MotherCanvas.w - (GameCanvas.BG << 1), MotherCanvas.h - GameCanvas.hCommand - 5, GameCanvas.BG << 1, GameCanvas.hCommand + 10)) {
               super.DB.AD();
            }
         }

         if (super.center != null) {
            if (super.center.AF()) {
               super.center.AE();
            } else if (GameCanvas.AB(MotherCanvas.hw - GameCanvas.BG, MotherCanvas.h - GameCanvas.hCommand - 5, GameCanvas.BG << 1, GameCanvas.hCommand + 10)) {
               super.center.AD();
            }
         }
      }

      int var1;
      if (super.AS != null) {
         for(var1 = 0; var1 < super.AS.size(); ++var1) {
            ((iCommand)super.AS.elementAt(var1)).AE();
         }
      }

      for(var1 = 0; var1 < this.AC.size(); ++var1) {
         int var2 = super.BN + super.BH + super.BQ * var1 + 28 - super.AT.AC;
         ((iCommand)this.AC.elementAt(var1)).AB(super.BM + 150, var2);
      }

      if (GameCanvas.isPointerSelect && super.AY.size() > 0 && GameCanvas.isPoint(super.BM, super.BN + super.BH, super.BO, super.AR)) {
         GameCanvas.isPointerSelect = false;
         if ((var1 = (GameCanvas.AZ - (super.BN + super.BH) + super.AT.AC) / super.BQ) >= 0 && var1 < super.AY.size()) {
            if (super.AW != var1) {
               super.AW = var1;
               return;
            }

            this.AA();
         }
      }

   }

   public final void AF() {
      int var1 = super.AY.size() * super.BQ - super.AR + 2;
      super.AT = new ListNew(super.BM + 20, super.BN + GameCanvas.hCommand, super.BO - 40, super.AR, 0, 0, var1, true);
      super.AT.AA((super.AW + 1) * super.BQ - super.AR / 2);
   }
}
