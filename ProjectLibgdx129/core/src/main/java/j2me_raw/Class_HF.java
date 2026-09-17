public final class Class_HF extends Class_GE {
   public static Class_HF AA;
   private iCommand AB;
   private iCommand AC;
   private iCommand AD;
   private iCommand AE;
   private iCommand AF;
   private mVector AG = new mVector();
   private byte AH;
   private int AI;
   private int AJ;
   private int AK;
   private int AL;

   public Class_HF(byte var1, mVector var2) {
      super(var1, var2, "", 190, 180);
      if (var1 == 0) {
         super.AZ = T.TW;
      } else if (var1 == 3) {
         super.AZ = T.TX;
      }

      this.AH = 0;
      super.BQ = 42;
      super.AQ = T.UD;
      this.AI();
      switch(super.BL) {
      case 0:
         this.AL = 0;
      case 1:
      case 2:
      default:
         break;
      case 3:
         this.AL = 1;
         break;
      case 4:
         this.AL = 2;
      }

      this.AB = new iCommand(T.DS, 12, this);
      this.AC = new iCommand(T.DT, 13, this);
      super.BC.indexMenu = 20;
      this.AD = new iCommand(T.MF, 14, this);
      this.AE = new iCommand(T.UA, 15, this);
      this.AF = new iCommand(T.UB, 16, this);
      this.AG.addElement(this.AD);
      this.AG.addElement(this.AE);
      this.AG.addElement(this.AF);
      if (GameCanvas.isTouch) {
         super.BB.setPos(super.BM + 20 + super.BO - 34, super.BN - 10, MainTab.fraCloseTab, "");
         super.DB = super.BB;
      }

      this.AA(super.AS);
      if (Interface_Game.AD == null) {
         Interface_Game.AE();
      }

   }

   public final void commandPointer(int var1, int var2) {
      boolean var3 = false;
      byte var4 = this.AH;
      switch(var1) {
      case 12:
         ++this.AH;
         this.AH = (byte)AvMain.AA(this.AH, this.AI - 1, false);
         if (var4 == this.AH) {
            GameCanvas.Start_Normal_DiaLog(T.TZ, new mVector(), true);
         } else {
            var3 = true;
         }
         break;
      case 13:
         --this.AH;
         this.AH = (byte)AvMain.AA(this.AH, this.AI - 1, false);
         if (var4 == this.AH) {
            GameCanvas.Start_Normal_DiaLog(T.TY, new mVector(), true);
         } else {
            var3 = true;
         }
         break;
      case 14:
         GlobalService.getInstance().AL((byte)0, (short)-1);
         break;
      case 15:
         GlobalService.getInstance().AL((byte)3, (short)-1);
         break;
      case 16:
         GlobalService.getInstance().AE((byte)4, (short)-1, (byte)0);
      case 17:
      case 18:
      case 19:
      default:
         break;
      case 20:
         if (super.BL == 0) {
            GlobalService.getInstance().AE((byte)1, (short)super.AU.AG, (byte)0);
            return;
         }

         if (super.BL == 3) {
            GlobalService.getInstance().AE((byte)5, (short)super.AU.AG, (byte)0);
         }

         return;
      }

      if (var3) {
         super.AW = 0;
         this.AI();
         this.AH();
      } else {
         super.commandPointer(var1, var2);
      }
   }

   public final void AA() {
      if (super.AY.size() != 0) {
         super.AU = (Class_DS)super.AY.elementAt(super.AW + this.AH * 10);
         if (super.AU != null) {
            mVector var1;
            (var1 = new mVector()).addElement(super.BC);
            GameCanvas.menu.startAt(var1, 2, super.AU.BC.name);
         }

      }
   }

   public final void AB() {
      mVector var1 = new mVector();
      String var2 = T.AU;
      if (super.AY.size() > 0) {
         super.AU = (Class_DS)super.AY.elementAt(super.AW + this.AH * 10);
         if (super.AU != null) {
            var1.addElement(super.BC);
            var2 = super.AU.BC.name;
         }
      }

      var1.addElement(this.AB);
      var1.addElement(this.AC);

      for(int var3 = 0; var3 < this.AG.size(); ++var3) {
         if (var3 != this.AL) {
            var1.addElement(this.AG.elementAt(var3));
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
         var1.AD(super.BM, super.BN + GameCanvas.hCommand + super.AV, super.BO, super.AR - super.AV);
         mGraphics.AC();
         int var10000 = super.BM;
         var10000 = super.BN;
         var10000 = GameCanvas.hCommand;
         var10000 = super.AV;
         var10000 = super.BO;
         var10000 = super.AR;
         var10000 = super.AV;
         mGraphics.AD();
         var1.translate(0, -super.AT.AC);
         int var2 = super.BM + 30;
         int var3 = super.BN + GameCanvas.hCommand + 10;
         int var4;
         if (super.BI) {
            MsgDialog.fraImgWaiting.drawFrame(GameCanvas.gameTick / 6 % MsgDialog.fraImgWaiting.nFrame, super.BM + super.BO / 2, var3 + super.BQ, 0, 3, var1);
         } else if (super.AY.size() == 0) {
            mFont.tahoma_7_black.drawString(var1, super.AQ, super.BM + super.BO / 2, super.BN + (super.BP - 20) / 2, 2);
         } else {
            if (super.AW >= 0) {
               this.AA(var1, var2, var3 - 2, super.BO - 40);
            }

            for(var4 = 0; var4 < 10; ++var4) {
               if (var3 - super.AT.AC + super.BQ >= GameCanvas.hCommand + super.BN && var3 - super.AT.AC - super.BQ <= GameCanvas.hCommand + super.AR + super.BN) {
                  Class_DS var5 = (Class_DS)super.AY.elementAt(var4 + this.AH * 10);
                  this.AA(var1, var5, var2, var3, var4, super.BO - 60);
               }

               var3 += super.BQ;
            }
         }

         mGraphics.AE();
         mGraphics.restoreCanvas();
         GameCanvas.resetTrans(var1);
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
         AvMain.AH(var1, var3 + 1, var4, super.BO - 61, 39);
         var3 += 42;
         mFont.tahoma_7b_black.drawString(var1, var2.BC.name, var3, var4 + 1, 0);
         if (super.BL == 0) {
            mFont.tahoma_7_black.drawString(var1, T.TV + ": " + var2.AC, var3, var4 + GameCanvas.hText - 2, 0);
         } else if (super.BL == 3) {
            mFont.tahoma_7_black.drawString(var1, T.BY + ": " + var2.BD, var3, var4 + GameCanvas.hText - 2, 0);
         }

         AvMain.fraMoney.drawFrame(0, var3 - 1, var4 + (GameCanvas.hText << 1) - 6, 0, 0, var1);
         mFont.tahoma_7_black.drawString(var1, AvMain.AA((long)var2.BC.KP), var3 + 12, var4 + (GameCanvas.hText << 1) - 5, 0);
         var3 -= 42;
         if (super.BL == 3 && var2.BE == 1) {
            var1.drawRegion((mImage)AvMain.imgComplete, var3 + (super.BO - 60) / 2 - 7, var4 + 1, 0);
         }

         if (super.BL == 0) {
            if (var2.AP == 0) {
               var1.drawRegion(Interface_Game.AD[3], 0, 14, 7, 7, 0, var3 + super.BO - 60 - 10 - 1, var4 + (GameCanvas.hText << 1) - 3, 0);
            } else {
               var1.drawRegion(Interface_Game.AD[3], 0, 7, 7, 7, 0, var3 + super.BO - 60 - 10 - 1, var4 + (GameCanvas.hText << 1) - 3, 0);
            }
         }

         MainObject.AA(var1, var2.BC.BS, var2.BC.BW, var2.BC.BX, var3 + 21, var4 + super.BQ / 2 + 35 - 4, 0);
         var1.drawRegion(Interface_Game.imgIconMPHP, 0, 22, 10, 11, 0, var3 + 11, var4 + (GameCanvas.hText << 1) - 3, 0);
         mFont.tahoma_7_black.drawString(var1, String.valueOf(var2.BC.Lv), var3 + 24, var4 + (GameCanvas.hText << 1) - 5, 0);
      }
   }

   public final void AB(mGraphics var1) {
      AvMain.AB(var1, super.BM, super.BN - 20, super.BO, super.BP + 20, 1);
      mFont.tahoma_7b_brown.drawString(var1, super.AZ, super.BM + super.BO / 2, super.BN + GameCanvas.hCommand / 2 + 3, 2);
      mFont.tahoma_7b_black.drawString(var1, T.PL + (this.AH + 1) + "/" + this.AI, super.BM + super.BO / 2, super.BN + super.BP - GameCanvas.hCommand / 2 - 20, 2);
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      var1.setColor(-4840926);
      var1.drawRect(var2, var3 + super.AW * super.BQ + 1, super.BO - 60, super.BQ - 2);
   }

   public final void handleKeyPress() {
      super.handleKeyPress();
      super.AW = AvMain.AA(super.AW, this.AJ - 1, false);
   }

   public final void updatePointer() {
      super.updatePointer();
   }

   public final void AF() {
      int var1 = this.AJ * super.BQ - super.AR + (super.AV << 1);
      super.AT = new ListNew(super.BM, super.BN + GameCanvas.hCommand, super.BO, super.AR, 0, 0, var1, true);
      super.AT.AA((super.AW + 1) * super.BQ - super.AR / 2);
   }

   private void AI() {
      this.AJ = super.AY.size() - this.AH * 10;
      this.AJ = AvMain.AA(this.AJ, this.AJ >= 10 ? 10 : this.AJ, false);
      this.AI = super.AY.size() / 10 + (super.AY.size() % 10 != 0 ? 1 : 0);
      if (this.AI == 0) {
         this.AI = 1;
      }

   }

   public final void AA(int var1, mVector var2) {
      super.BM -= this.AK / 2;
      super.AA(var1, var2);
      super.BM += this.AK / 2;
   }
}
