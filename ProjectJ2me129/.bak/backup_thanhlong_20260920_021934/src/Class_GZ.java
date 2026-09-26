public final class Class_GZ extends LuckyScreen {
   private String AB = "";
   public static Class_GZ AA;
   private int AC;
   private int AD;
   private int AE;
   private int AF;
   private iCommand AG;
   private iCommand BN;
   private iCommand BO;
   private iCommand BP;
   private iCommand BQ;
   private iCommand BR;
   private iCommand BS;
   private iCommand BT;
   private iCommand BU;
   private iCommand BV;
   private iCommand BW;
   private iCommand BX;
   private iCommand BY;
   private iCommand BZ;
   private iCommand CA;
   private iCommand CB;
   private iCommand CC;
   private iCommand CD;
   private iCommand CE;
   private iCommand CF;
   private iCommand CG;
   private iCommand CH;
   private iCommand CI;
   private FrameImage CJ;
   private FrameImage CK;
   private int CL = 1600000;
   private int CM = 160000;
   private int CN = 600000;
   private short CO = 60;
   private byte CP = -1;
   private byte CQ = 1;
   private byte CR = 1;
   private byte CS = 2;
   private byte CT = 3;
   private InputDialog CU;
   private long CV;
   private long CW;
   private boolean CX = false;
   private int CY = -1;
   private int CZ = 0;
   private int IL = 1;
   private int IM = 2;

   public Class_GZ(String var1, int var2, int var3, int var4, byte var5, short var6) {
      this.AB = var1;
      this.CL = var2;
      this.CM = var3;
      this.CN = var4;
      this.CP = var5;
      this.CO = var6;
      this.AE = 260;
      this.AF = 215;
      this.AC = MotherCanvas.hw - this.AE / 2;
      this.AD = MotherCanvas.hh - this.AF / 2;
      this.AG = new iCommand(T.close, -1, this);
      this.BN = new iCommand(T.UU, 1, this);
      this.BO = new iCommand(T.UV, 2, this);
      this.BN.setPos(this.AC + this.AE / 4, this.AD + 190, (FrameImage)null, T.UU);
      this.BO.setPos(this.AC + this.AE / 4 * 3, this.AD + 190, (FrameImage)null, T.UV);
      super.AI = new mVector();
      super.AI.addElement(this.BN);
      super.AI.addElement(this.BO);
      super.AI.addElement(this.AG);
      if (GameCanvas.isTouch) {
         this.AG.setPos(this.AC + this.AE / 2 + 60, this.AD + 10 + 8, MainTab.fraCloseTab, "");
      } else {
         super.DB = this.AG;
      }

      this.CJ = new FrameImage(mImage.createImage("/interface/taixiu.png"), 4);
      this.CK = new FrameImage(mImage.createImage("/interface/xucxac.png"), 6);
      this.BP = new iCommand(T.UU + " " + T.UW, 11, this);
      this.BQ = new iCommand(T.UU + " " + T.UX, 12, this);
      this.BR = new iCommand(T.UU, 111, this);
      this.BS = new iCommand(T.UU, 121, this);
      this.BT = new iCommand(T.UV + " " + T.UW, 21, this);
      this.BV = new iCommand(T.UV + " " + T.UW + " 1M", 31, this);
      this.BW = new iCommand(T.UV + " " + T.UW + " 2M", 32, this);
      this.BX = new iCommand(T.UV + " " + T.UW + " 3M", 33, this);
      this.BY = new iCommand(T.UV + " " + T.UW + " 4M", 34, this);
      this.BZ = new iCommand(T.UV + " " + T.UW + " 5M", 35, this);
      this.CA = new iCommand(T.UV + " " + T.UW + " 10M", 36, this);
      this.CB = new iCommand(T.UV + " " + T.UW + " 20M", 37, this);
      this.BU = new iCommand(T.UV + " " + T.UX, 22, this);
      this.CC = new iCommand(T.UV + " " + T.UX + " 1M", 41, this);
      this.CD = new iCommand(T.UV + " " + T.UX + " 2M", 42, this);
      this.CE = new iCommand(T.UV + " " + T.UX + " 3M", 43, this);
      this.CF = new iCommand(T.UV + " " + T.UX + " 4M", 44, this);
      this.CG = new iCommand(T.UV + " " + T.UX + " 5M", 45, this);
      this.CH = new iCommand(T.UV + " " + T.UX + " 10M", 46, this);
      this.CI = new iCommand(T.UV + " " + T.UX + " 20M", 47, this);
      this.CV = System.currentTimeMillis();
      this.CW = System.currentTimeMillis();
      this.CZ = CRes.random(6);
      this.IL = CRes.random(6);
      this.IM = CRes.random(6);
      this.CY = -1;
      super.AY = 0;
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      MainTab.AA(var1, MotherCanvas.w - 78, 4 + GameScreen.h12plus, false);
      int var10006 = this.AE;
      this.AD(var1, this.AC, this.AD, this.AE, this.AF, 0);
      var1.setColor(-2573687);
      var1.fillRoundRectNew(this.AC + this.AE / 2 - 60, this.AD + 10, 120, 16, 4, 4);
      AvMain.FontBorderColor(var1, this.AB, this.AC + this.AE / 2, this.AD + 12, 2, (int)6, (int)5);
      AvMain.FontBorderColor(var1, String.valueOf(this.CO), MotherCanvas.hw, this.AD + 35, 2, (int)1, (int)7);
      int var2 = this.AC + this.AE / 4 - 15;
      int var3 = this.AD + 60;
      this.CJ.drawFrame(0, var2, var3, 0, 3, var1);
      AvMain.paintRect(var1, var2 - 30, var3 + 40, 60, 15, (byte)1, 4);
      String var4;
      if (this.CL < 1000000) {
         var4 = AvMain.AA((long)this.CL);
      } else {
         var4 = AvMain.AB((long)this.CL);
      }

      mFont.tahoma_7_yellow.drawString(var1, var4, var2, var3 + 40 + 2, 2);
      int var5 = this.AC + this.AE / 4 * 3 + 15;
      this.CJ.drawFrame(2, var5, var3, 0, 3, var1);
      AvMain.paintRect(var1, var5 - 30, var3 + 40, 60, 15, (byte)1, 4);
      if (this.CM < 1000000) {
         var4 = AvMain.AA((long)this.CM);
      } else {
         var4 = AvMain.AB((long)this.CM);
      }

      mFont.tahoma_7_yellow.drawString(var1, var4, var5, var3 + 40 + 2, 2);
      int var7 = MotherCanvas.hw + 2;
      var3 = this.AD + 150;
      mFont.tahoma_7_black.drawString(var1, T.UY, var7, var3, 1);
      mFont.tahoma_7_red.drawString(var1, AvMain.AA((long)this.CN), var7 + 2, var3, 0);
      mFont.tahoma_7_black.drawString(var1, T.UZ, var7, var3 + 15, 1);
      if (this.CP == -1) {
         mFont.tahoma_7_red.drawString(var1, "...", var7 + 2, var3 + 15, 0);
      } else if (this.CP == 0) {
         mFont.tahoma_7_red.drawString(var1, T.UX, var7 + 2, var3 + 15, 0);
      } else {
         mFont.tahoma_7_red.drawString(var1, T.UW, var7 + 2, var3 + 15, 0);
      }

      var7 = MotherCanvas.hw;
      int var10000 = this.AD;
      if (super.AY == 4) {
         var3 = this.AD + 100;
         this.CK.drawFrame(this.CR - 1, var7 - 18, var3 - 5, 0, 3, var1);
         this.CK.drawFrame(this.CS - 1, var7 + 20, var3 + 5, 0, 3, var1);
         this.CK.drawFrame(this.CT - 1, var7 - 5, var3 + 20, 0, 3, var1);
         if (this.CQ != -1) {
            var3 = this.AD + 60;
            if (this.CQ == 1) {
               this.CJ.drawFrame(1, var2, var3, 0, 3, var1);
            } else if (this.CQ == 0) {
               this.CJ.drawFrame(3, var5, var3, 0, 3, var1);
            }
         }
      } else if (super.AY != 4) {
         var3 = this.AD + 100;
         this.CK.drawFrame(this.CZ, var7 - 18, var3 - 5, 0, 3, var1);
         this.CK.drawFrame(this.IL, var7 + 20, var3 + 5, 0, 3, var1);
         this.CK.drawFrame(this.IM, var7 - 5, var3 + 20, 0, 3, var1);
         if (this.CY != -1) {
            var3 = this.AD + 60;
            if (this.CY == 1) {
               this.CJ.drawFrame(1, var2, var3, 0, 3, var1);
            } else if (this.CY == 0) {
               this.CJ.drawFrame(3, var5, var3, 0, 3, var1);
            }
         }
      }

      for(var2 = 0; var2 < super.AI.size(); ++var2) {
         iCommand var6;
         (var6 = (iCommand)super.AI.elementAt(var2)).paint(var1, var6.xCmd, var6.yCmd);
      }

      this.AA(var1, 0);
   }

   public final void commandPointer(int var1, int var2) {
      boolean var5;
      mVector var6;
      switch(var1) {
      case -1:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         return;
      case 1:
         (var6 = new mVector()).addElement(this.BP);
         var6.addElement(this.BQ);
         GameCanvas.menu.startAt(var6, 2, T.UU);
         return;
      case 2:
         (var6 = new mVector()).addElement(this.BT);
         var6.addElement(this.BU);
         GameCanvas.menu.startAt(var6, 2, T.UV);
         return;
      case 11:
         this.CU = GameCanvas.Start_Input_Dialog(T.VA, this.BR, true, T.UU + " " + T.UW);
         GameCanvas.subDialog = this.CU;
         return;
      case 12:
         this.CU = GameCanvas.Start_Input_Dialog(T.VA, this.BS, true, T.UU + " " + T.UX);
         GameCanvas.subDialog = this.CU;
         return;
      case 21:
         (var6 = new mVector()).addElement(this.BV);
         var6.addElement(this.BW);
         var6.addElement(this.BX);
         var6.addElement(this.BY);
         var6.addElement(this.BZ);
         var6.addElement(this.CA);
         var6.addElement(this.CB);
         GameCanvas.menu.startAt(var6, 2, T.UV + " " + T.UW);
         return;
      case 22:
         (var6 = new mVector()).addElement(this.CC);
         var6.addElement(this.CD);
         var6.addElement(this.CE);
         var6.addElement(this.CF);
         var6.addElement(this.CG);
         var6.addElement(this.CH);
         var6.addElement(this.CI);
         GameCanvas.menu.startAt(var6, 2, T.UV + " " + T.UX);
         return;
      case 31:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)1000000, (byte)1, (byte)1);
         return;
      case 32:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)2000000, (byte)1, (byte)1);
         return;
      case 33:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)3000000, (byte)1, (byte)1);
         return;
      case 34:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)4000000, (byte)1, (byte)1);
         return;
      case 35:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)5000000, (byte)1, (byte)1);
         return;
      case 36:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)10000000, (byte)1, (byte)1);
         return;
      case 37:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)20000000, (byte)1, (byte)1);
         return;
      case 41:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)1000000, (byte)0, (byte)1);
         return;
      case 42:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)2000000, (byte)0, (byte)1);
         return;
      case 43:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)3000000, (byte)0, (byte)1);
         return;
      case 44:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)4000000, (byte)0, (byte)1);
         return;
      case 45:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)5000000, (byte)0, (byte)1);
         return;
      case 46:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)10000000, (byte)0, (byte)1);
         return;
      case 47:
         GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)20000000, (byte)0, (byte)1);
      default:
         return;
      case 111:
         var5 = false;

         try {
            if ((var1 = Integer.parseInt(this.CU.tfInput.getText())) < 0) {
               var1 = 0;
            }
         } catch (Exception var4) {
            var1 = 0;
         }

         GameCanvas.end_Dialog();
         if (var1 / 1000 > 0 && var1 % 1000 == 0 && var1 <= 500000) {
            GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)var1, (byte)1, (byte)0);
            return;
         }

         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.VB);
         return;
      case 121:
         var5 = false;

         try {
            if ((var1 = Integer.parseInt(this.CU.tfInput.getText())) < 0) {
               var1 = 0;
            }
         } catch (Exception var3) {
            var1 = 0;
         }

         GameCanvas.end_Dialog();
         if (var1 / 1000 > 0 && var1 % 1000 == 0 && var1 <= 500000) {
            GlobalService.getInstance().TaiXiu((byte)0, (byte)1, (int)var1, (byte)0, (byte)0);
         } else {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.VB);
         }
      }
   }

   public final void update() {
      super.update();
      if (GameCanvas.timeNow - this.CV >= 1000L && this.CO > 0) {
         this.CV = GameCanvas.timeNow;
         --this.CO;
         if (this.CO == 0) {
            this.CX = true;
         }
      }

      if (this.CX && GameCanvas.timeNow - this.CV >= 3000L) {
         GlobalService.getInstance().TaiXiu((byte)0, (byte)2);
         this.CX = false;
      }

      if (GameCanvas.timeNow - this.CW >= 5000L) {
         this.CW = GameCanvas.timeNow;
         GlobalService.getInstance().TaiXiu((byte)0, (byte)3);
      }

   }

   public final void updatePointer() {
      if (super.AI != null) {
         for(int var1 = 0; var1 < super.AI.size(); ++var1) {
            ((iCommand)super.AI.elementAt(var1)).AE();
         }
      }

      super.updatePointer();
   }

   private void AH() {
      ++this.CY;
      if (this.CY > 1) {
         this.CY = 0;
      }

      float var10000 = mSound.volumeSound;
      mSound.playSound();
      this.CZ = CRes.random(6);
      this.IL = CRes.random(6);
      this.IM = CRes.random(6);
   }

   public final void AG() {
      ++super.AW;
      if (super.AY == 1) {
         if (super.AW >= 0) {
            if (super.AW < 12) {
               if (super.AW % 6 == 0) {
                  this.AH();
               }
            } else if (super.AW < 20) {
               if (super.AW % 4 == 0) {
                  this.AH();
               }
            } else if (super.AW % 3 == 0) {
               this.AH();
            }
         }

         if (super.AW >= 100) {
            super.AY = 3;
            super.AW = 0;
            return;
         }
      } else if (super.AY == 3) {
         if (super.AW >= 0) {
            if (super.AW < 20) {
               if (super.AW % 4 == 0) {
                  this.AH();
               }
            } else if (super.AW % 6 == 0) {
               this.AH();
            }
         }

         if (super.AW > 50 && this.CY == this.CQ) {
            super.AY = 4;
            super.AW = 0;
            return;
         }
      } else if (super.AY == 4) {
         if (super.AW == 200) {
            super.AY = 0;
            super.AW = 0;
            this.CY = -1;
            GlobalService.getInstance().TaiXiu((byte)0, (byte)0);
         }

         if (super.AW == 1 && this.CP != -1) {
            float var10000;
            if (this.CQ == this.CP) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
               this.AA((short)177, 0, this.AC + this.AE / 2, this.AD + this.AF / 2 + 10, this.AC + this.AE / 2, this.AD + this.AF / 2 + 10);
               this.AA((short)76, 0, this.AC + this.AE / 2, this.AD + this.AF / 2 + 10, this.AC + this.AE / 2, this.AD + this.AF / 2 + 10);
               return;
            }

            if (this.CQ != this.CP) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
               this.AA((short)177, 1, this.AC + this.AE / 2, this.AD + this.AF / 2 + 10, this.AC + this.AE / 2, this.AD + this.AF / 2 + 10);
               this.AA((short)77, 0, this.AC + this.AE / 2, this.AD + this.AF / 2 + 10, this.AC + this.AE / 2, this.AD + this.AF / 2 + 10);
            }
         }
      }

   }

   public final void AA(byte var1, byte var2, byte var3, byte var4, boolean var5) {
      this.CQ = var1;
      this.CR = var2;
      this.CS = var3;
      this.CT = var4;
      if (var5) {
         super.AY = 1;
         super.AW = 0;
      }

      this.CZ = var2 - 1;
      this.IL = var3 - 1;
      this.IM = var4 - 1;
   }

   public final void AA(int var1, int var2, int var3, byte var4) {
      this.CL = var1;
      this.CM = var2;
      this.CN = var3;
      this.CP = var4;
   }

   public final void AB(int var1, int var2) {
      this.CL = var1;
      this.CM = var2;
   }

   private void AA(short var1, int var2, int var3, int var4, int var5, int var6) {
      Effect_End var7 = new Effect_End(var1, (byte)var2, var3, var4, var5, var6, (byte)0, (MainObject)null);
      super.AJ.addElement(var7);
   }
}
