public final class Class_FD extends Class_GE {
   public static Class_FD AA;
   private mVector AB = new mVector();
   private mVector AC = new mVector();
   private mVector AD = new mVector();
   private static mVector AE = new mVector();
   private int[] AF = new int[4];
   private int AG = 0;
   private int AH = 29;
   private long AI;
   private String[][] AJ;
   private int AK = 2;

   public Class_FD(mVector var1) {
      super((byte)0, var1, "", 320, 200);
      super.BO = MotherCanvas.w - 148 - 40;
      int var4 = 0;
      if (GameCanvas.isTouch && super.BO < 250) {
         var4 = (250 - super.BO) / 2;
         super.BO = 250;
      }

      if (!GameCanvas.isTouch) {
         super.BO = MotherCanvas.w - 20;
         this.AG = 10;
         super.BN += 10;
      }

      this.AF[0] = 0;
      this.AF[1] = super.BO / 9 * 3;
      this.AF[2] = this.AF[1] + (super.BO / 9 << 1);
      this.AF[3] = this.AF[2] + (super.BO / 9 << 1);
      super.BM = MotherCanvas.hw - super.BO / 2 - var4;
      super.BH = 35;
      super.BQ = 38;
      super.AR = super.BP - super.BH - 10;
      super.AQ = T.UD;
      this.AI = System.currentTimeMillis();
      super.BC.indexMenu = 10;
      super.AS.removeAllElements();
      if (!GameCanvas.isTouch) {
         super.AS.addElement(super.BA);
      }

      if (GameCanvas.isTouch) {
         super.BB.setPos(super.BM + super.BO, super.BN + 11 + 8, MainTab.fraCloseTab, "");
      } else {
         super.AS.addElement(super.BB);
      }

      this.AA(super.AS);
      this.AJ = new String[super.AY.size()][];

      for(int var2 = 0; var2 < super.AY.size(); ++var2) {
         Class_DS var3 = (Class_DS)super.AY.elementAt(var2);
         iCommand var5;
         (var5 = new iCommand(T.UI, 7, var2, this)).setPos(super.BM + this.AF[1] + 26 + 10, super.BN + super.BH + (super.BQ + this.AK) * var2 + 26, AvMain.fraCmdNhanNapThe, var5.caption);
         if (var3.BG == 1) {
            var5.AC();
         }

         this.AB.addElement(var5);
         (var5 = new iCommand(T.BN, 8, var2, this)).setPos(super.BM + this.AF[2] + 26 + 10, super.BN + super.BH + (super.BQ + this.AK) * var2 + 26, AvMain.fraCmdNhanNapThe, var5.caption);
         this.AC.addElement(var5);
         (var5 = new iCommand(T.BY, 9, var2, this)).setPos(super.BM + this.AF[1] + 26 + 10, super.BN + super.BH + (super.BQ + this.AK) * var2 + 26, AvMain.fraCmdNhanNapThe, var5.caption);
         var5.AB();
         this.AD.addElement(var5);
         this.AJ[var2] = mFont.tahoma_7b_brown.splitFontArray(var3.BI.name, this.AF[1] - 5 - this.AH);
      }

   }

   public final void commandPointer(int var1, int var2) {
      Class_DS var3 = (Class_DS)super.AY.elementAt(var2);
      switch(var1) {
      case 2:
         GlobalService.getInstance().AJ((byte)-91, (byte)4);
      case 3:
      case 4:
      case 5:
      case 6:
      default:
         break;
      case 7:
         GlobalService.getInstance().AC((byte)-91, (byte)1, (byte)var3.AG);
         break;
      case 8:
         GlobalService.getInstance().AC((byte)-91, (byte)3, (byte)var3.AG);
         break;
      case 9:
         GlobalService.getInstance().AC((byte)-91, (byte)2, (byte)var3.AG);
         break;
      case 10:
         var3 = (Class_DS)super.AY.elementAt(super.AW);
         (Class_FU.AA = new Class_FU(super.BL, var3)).Show((MainScreen)this);
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
            var1.addElement(super.BC);
            iCommand var3;
            if (super.AU.BG == 1) {
               var3 = (iCommand)this.AD.elementAt(super.AW);
               var1.addElement(var3);
            }

            var3 = (iCommand)this.AB.elementAt(super.AW);
            iCommand var4 = (iCommand)this.AC.elementAt(super.AW);
            var1.addElement(var3);
            var1.addElement(var4);
         }
      }

      GameCanvas.menu.startAt(var1, 2, var2);
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      this.AB(var1);
      super.BB.paint(var1, super.BB.xCmd, super.BB.yCmd);
      int var2 = super.BM + 10;
      int var3 = super.BN + super.BH;
      this.AE(var1);
      int var4;
      if (super.AY.size() == 0) {
         mFont.tahoma_7_black.drawString(var1, super.AQ, super.BM + super.BO / 2, super.BN + (super.BP - 20) / 2, 2);
      } else {
         if (super.AW >= 0) {
            this.AA(var1, var2, var3 - 1, super.BO - 20);
         }

         for(var4 = 0; var4 < super.AY.size(); ++var4) {
            if (var3 - super.AT.AC + super.BQ >= super.BH + super.BN && var3 - super.AT.AC - super.BQ <= super.BH + super.AR + super.BN) {
               Class_DS var5 = (Class_DS)super.AY.elementAt(var4);
               this.AA(var1, var5, var2, var3, var4, super.BO - 20);
            }

            var3 += super.BQ + this.AK;
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

      MainTab.AA(var1, MotherCanvas.w - 78, 4 + GameScreen.h12plus);

      for(var4 = 0; var4 < AE.size(); ++var4) {
         MainEffect var7;
         if ((var7 = (MainEffect)AE.elementAt(var4)) != null && !var7.BI && !var7.isStop) {
            var7.paint(var1);
         }
      }

   }

   public final void AA(mGraphics var1, Class_DS var2, int var3, int var4, int var5, int var6) {
      if (var2 != null) {
         var1.setColor(-2573687);
         AvMain.AH(var1, var3, var4, var6, super.BQ);
         var2.BI.paint(var1, var3 + 4 + this.AH / 2, var4 + 4 + this.AH / 2, this.AH);
         int var7;
         if (this.AG == 0 && this.AJ[var5] != null) {
            var7 = var4 + 5;

            for(int var8 = 0; var8 < this.AJ[var5].length; ++var8) {
               mFont.tahoma_7b_brown.drawString(var1, this.AJ[var5][var8], var3 + 2 + 33, var7, 0);
               var7 += 12;
            }
         }

         var7 = var3;
         if (!GameCanvas.isTouch) {
            var7 = super.BM;
         }

         AvMain.fraMoney.drawFrame(8, var7 + this.AF[1] + 2, var4 + 2 + this.AG, 0, 0, var1);
         AvMain.AA(var1, String.valueOf(var2.BI.CP), var7 + this.AF[1] + 15, var4 + 3 + this.AG, 0, (byte)5);
         mFont var10 = mFont.tahoma_7b_black;
         if (GameCanvas.isSmallScreen) {
            var10 = mFont.tahoma_7_black;
         }

         if (var2.BI.CQ == -1) {
            mFont.tahoma_7b_red.drawString(var1, T.UP, var7 + this.AF[3] + (var6 - this.AF[3]) / 2, var4 + 13, 2);
         } else {
            var10.drawString(var1, Interface_Game.AB(var2.BI.CQ), var7 + this.AF[3] + (var6 - this.AF[3]) / 2, var4 + 13, 2);
         }

         if (var2.BI.CR > 0) {
            AvMain.fraMoney.drawFrame(8, var7 + this.AF[2] + 2, var4 + 2 + this.AG, 0, 0, var1);
            AvMain.AA(var1, String.valueOf(var2.BI.CR), var7 + this.AF[2] + 15, var4 + 3 + this.AG, 0, (byte)5);
         } else {
            mFont.tahoma_7_black.drawString(var1, "---", var7 + this.AF[2] + 26, var4 + 13, 2);
         }

         if (GameCanvas.isTouch) {
            iCommand var9;
            iCommand var10000;
            int var10002;
            if (var2.BI.CQ <= 0 && var2.BG != 0) {
               if (var2.BG == 1) {
                  var10000 = var9 = (iCommand)this.AD.elementAt(var5);
                  var10002 = var9.xCmd;
                  var10002 = var9.yCmd;
                  var10000.AA(var1);
               }
            } else {
               var10000 = var9 = (iCommand)this.AB.elementAt(var5);
               var10002 = var9.xCmd;
               var10002 = var9.yCmd;
               var10000.AA(var1);
            }

            if (var2.BI.CR > 0) {
               var10000 = var9 = (iCommand)this.AC.elementAt(var5);
               var10002 = var9.xCmd;
               var10002 = var9.yCmd;
               var10000.AA(var1);
            }
         }

         if (var2.BI.CQ <= 0 && var2.BI.CQ != -1) {
            var1.drawRegion((mImage)AvMain.imgComplete, var3 + this.AH + 8, var4 + 1, 0);
         }

      }
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      var1.setColor(-4840926);
      var1.drawRect(var2 - 1, var3 + super.AW * (super.BQ + this.AK), var4 + 1, super.BQ + 1);
   }

   public final void AB(mGraphics var1) {
      int var10006 = super.BO;
      this.AD(var1, super.BM, super.BN, super.BO, super.BP, 0);
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.BM + 5, super.BN + 12, super.BO - 10, 16, 4, 4);
      AvMain.FontBorderColor(var1, GameCanvas.isTouch ? T.UH : T.UL, super.BM + this.AF[0] + (this.AF[1] - this.AF[0]) / 2, super.BN + 14, 2, (int)6, (int)5);
      AvMain.FontBorderColor(var1, GameCanvas.isTouch ? T.UI : T.UM, super.BM + this.AF[1] + (this.AF[2] - this.AF[1]) / 2, super.BN + 14, 2, (int)6, (int)5);
      AvMain.FontBorderColor(var1, GameCanvas.isTouch ? T.UJ : T.UN, super.BM + this.AF[2] + (this.AF[3] - this.AF[2]) / 2, super.BN + 14, 2, (int)6, (int)5);
      AvMain.FontBorderColor(var1, GameCanvas.isTouch ? T.UK : T.UO, super.BM + this.AF[3] + (super.BO - this.AF[3]) / 2 - 5, super.BN + 14, 2, (int)6, (int)5);
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

      for(var1 = 0; var1 < super.AY.size(); ++var1) {
         Class_DS var2;
         if ((var2 = (Class_DS)super.AY.elementAt(var1)).BI.CQ > 0) {
            ((iCommand)this.AB.elementAt(var1)).AB(super.AT.AC);
            if (var2.BI.CR > 0) {
               ((iCommand)this.AC.elementAt(var1)).AB(super.AT.AC);
            }
         } else if (var2.BG == 1) {
            ((iCommand)this.AD.elementAt(var1)).AB(super.AT.AC);
         }
      }

      if (GameCanvas.isPointerSelect && super.AY.size() > 0 && GameCanvas.isPoint(super.BM, super.BN + super.BH, super.BO, super.AR)) {
         GameCanvas.isPointerSelect = false;
         if ((var1 = (GameCanvas.AZ - (super.BN + super.BH) + super.AT.AC) / (super.BQ + this.AK)) >= 0 && var1 < super.AY.size()) {
            if (super.AW != var1) {
               super.AW = var1;
               return;
            }

            this.AA();
         }
      }

   }

   public final void AF() {
      int var1 = super.AY.size() * (super.BQ + this.AK) - super.AR + super.AV;
      super.AT = new ListNew(super.BM + 10, super.BN + super.BH, super.BO - 20, super.AR, 0, 0, var1, true);
      super.AT.AA((super.AW + 1) * (super.BQ + this.AK) - super.AR / 2);
   }

   public final void update() {
      super.update();
      int var1;
      if (GameCanvas.timeNow - this.AI >= 1000L) {
         this.AI += 1000L;

         for(var1 = 0; var1 < super.AY.size(); ++var1) {
            Class_DS var2;
            if ((var2 = (Class_DS)super.AY.elementAt(var1)).BI.CQ > 0) {
               --var2.BI.CQ;
            }
         }
      }

      for(var1 = 0; var1 < AE.size(); ++var1) {
         MainEffect var3;
         if ((var3 = (MainEffect)AE.elementAt(var1)) != null && !var3.BI && !var3.isStop) {
            var3.update();
         }
      }

   }

   public final void AA(byte var1, short var2, int var3, int var4) {
      (new StringBuffer("id = ")).append(var1).append(" Set new value dau gia = ").append(var3).append(" time = ").append(var4).toString();

      for(int var5 = 0; var5 < super.AY.size(); ++var5) {
         Class_DS var6;
         if ((var6 = (Class_DS)super.AY.elementAt(var5)).AG == var1) {
            if (var2 == GameScreen.player.ID) {
               var6.BG = 1;
               ((iCommand)this.AB.elementAt(var5)).AC();
            } else {
               var6.BG = 0;
               ((iCommand)this.AB.elementAt(var5)).AA((byte)0);
            }

            var6.BI.CP = var3;
            var6.BI.CQ = var4;
            return;
         }
      }

   }

   public static void AA(String var0, int var1, int var2, byte var3, FrameImage var4, int var5) {
      Class_BN var6 = new Class_BN(var0, var1, var2, 10, var4, 8);
      if ((var1 = GameScreen.AA(AE)) == AE.size()) {
         AE.addElement(var6);
      } else {
         AE.setElementAt(var6, var1);
      }
   }
}
