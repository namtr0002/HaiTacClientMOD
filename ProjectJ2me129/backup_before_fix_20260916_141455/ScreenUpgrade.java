public class ScreenUpgrade extends MainScreen {
   public int AB;
   public int AC;
   public int AD;
   public int AE;
   public int AF;
   public int AG;
   public int AH = 0;
   public int AI;
   public int AJ;
   public int AK;
   public int AL;
   public int AM;
   private int AA;
   private int CQ;
   public int[][] AN;
   public boolean AO;
   public ListNew AP;
   public mVector AQ = new mVector();
   public MainItem AR;
   public iCommand AS;
   public iCommand AT;
   public iCommand AU;
   public iCommand AV;
   private iCommand CR;
   public iCommand AW;
   public static MainItem[] mItemUpgrade = null;
   public static MainDataUpgrade[] AY = null;
   public mVector vecEff = new mVector("ScreenUpgrade.vecEff");
   public String BA = "";
   public String showServer = "";
   public int Step = 0;
   public int BD = 0;
   public int BE = 0;
   public int BF;
   public byte BG;
   public static short[] BH = null;
   public static short[] BI = null;
   public static byte BJ = -1;
   public FrameImage BK;
   public static ScreenUpgrade instance;
   public byte BM = 0;
   private InputDialog CS;
   public mVector BN = new mVector();
   public int BO = 0;
   public int BP = 0;
   public int BQ = 0;
   public int BR = 0;
   public static MainItem[] mItemUpgradeSaveTemp = new MainItem[6];
   public static boolean BT = false;
   public static boolean BU = false;
   public static boolean BV = false;
   public long BW;
   public String BX = "";
   private int CT = 0;
   private int CU = 0;
   private int CV = 0;
   private int CW = 0;
   public static boolean isClickUpgradeItemToSucess = false;
   private static boolean isContinueUpgradeItem = false;
   public static boolean isUpgradeItemSucess = false;
   private long timeStartUpgradeItemAuto;
   public static byte beriOrRuby = -1;
   public static boolean isClickUpgradeSuperItemToSucess = false;
   private static boolean isContinueUpgradeSuperItem = false;
   public static boolean CC = false;
   private long IL;
   public static int valueLucky = 1;
   private static int IM = 0;
   public int CE = 0;
   public int CF = 0;
   public mImage imghoavan;
   public mImage CH;
   public int frameEff = 0;
   private int frameEndEff = 0;
   private boolean isRunEff = false;
   private byte IP = 0;
   public int indexStep = 1;
   public int CK;
   public static int valueTile = 0;
   public int CM = 0;
   public static int valueMonney_1 = 0;
   public static int valueMonney_2 = 0;
   public static int valueMonney_3 = 0;
   
    private static final int AUTO_UPGRADE_MIN_DELAY_MS = 300;
    private static final int AUTO_UPGRADE_JITTER_MS = 30;

   public ScreenUpgrade() {
   }

   public ScreenUpgrade(byte var1, int var2) {
      this.BG = var1;
      this.BN = Player.vecInventory;
      BJ = this.BG;
      this.Step = 0;
      this.AG = 10;
      this.AB = 290;
      this.AC = 185;
      this.AD = 24;
      if (GameCanvas.isTouch) {
         this.AD = 28;
      }

      if (this.AB > MotherCanvas.w) {
         this.AB = MotherCanvas.w;
      }

      if (this.AC > MotherCanvas.h - (GameCanvas.hCommand << 1)) {
         this.AC = MotherCanvas.h - (GameCanvas.hCommand << 1);
      }

      this.AE = MotherCanvas.hw - this.AB / 2;
      this.AF = MotherCanvas.hh - this.AC / 2 + 5;
      this.AK = this.AB / 2;
      this.AK -= this.AK % this.AD;
      this.AL = this.AC - this.AG * 3 - 5;
      this.AM = this.AK / this.AD;
      this.AI = this.AE + this.AB / 4 - this.AK / 2 + 15;
      this.AJ = this.AF + this.AC / 2 - this.AL / 2;
      int var3 = ((Player.maxInventory - 1) / this.AM + 1) * this.AD - this.AL;
      this.AP = new ListNew(this.AI, this.AJ, this.AK, this.AL, 0, 0, var3, true);
      this.AT = new iCommand(T.close, -1, this);
      this.CR = new iCommand(T.QA, 2, this);
      this.AW = new iCommand(T.AU, 3, this);
      if (GameCanvas.isTouch) {
         this.AT.setPos(this.AE + this.AB / 2 + 60, this.AF - 15 + 10 + 8, MainTab.fraCloseTab, "");
         this.AQ.addElement(this.AT);
         if (var1 != 18 && var1 != 22) {
            AvMain.AA(this.CR, 2);
            if (var1 != 6 && var1 != 21) {
               this.AQ.addElement(this.CR);
            }

            if (GameCanvas.isKeyPressed()) {
               super.backCMD = this.AT;
               super.DE = this.AU;
            }
         } else if (GameCanvas.isKeyPressed()) {
            super.backCMD = this.AT;
            super.DE = this.AU;
         }
      } else {
         this.AT.caption = T.close + " " + T.GK;
         super.DB = this.AW;
         super.DA = this.AU;
      }

      this.BF = this.AI + this.AK + 4;
      this.AA(var1, var2);
      if (var1 == 20) {
         this.AU.caption = T.join;
      }

   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public void AA(byte var1, int var2) {
      int var3 = this.AE + this.AK + (this.AB - this.AG * 3 - this.AK) / 2 + 10;
      int var4 = this.AF + this.AC / 2 - this.AD / 2 - 10;
      int var6;
      if (var2 < 0) {
         int var5 = 270;
         if ((var2 = (this.AB - this.AG * 3 - this.AK) / 2 - 20) < this.AD) {
            var2 = this.AD;
         }

         this.BA = T.GO;
         this.AN = mSystem.new_M_Int(5, 2);
         if (var1 == 15) {
            this.AN = mSystem.new_M_Int(6, 2);
            var4 += this.AD / 4;
            this.BA = T.SR;
         }

         if (var1 == 18) {
            this.AN = mSystem.new_M_Int(7, 2);
            var4 += this.AD / 4;
            this.BA = T.SS;
         }

         if (var1 == 22) {
            this.AN = mSystem.new_M_Int(6, 2);
            var4 += this.AD / 4;
            this.BA = T.VI;
            this.BM = 1;
            this.AH = -1;
         }

         this.AN[0][0] = var3;
         this.AN[0][1] = var4;
         if (var1 == 22) {
            var5 = 180;
         }

         for(var6 = 1; var6 < this.AN.length; ++var6) {
            if (var1 != 18 && var1 != 22) {
               this.AN[var6][0] = var3 + CRes.getcos(CRes.fixangle(var5)) * var2 / 1000;
               this.AN[var6][1] = var4 + CRes.getsin(CRes.fixangle(var5)) * var2 / 1000;
            } else {
               this.AN[var6][0] = var3 + CRes.getsin(CRes.fixangle(var5)) * var2 / 1000;
               this.AN[var6][1] = var4 + CRes.getcos(CRes.fixangle(var5)) * var2 / 1000;
            }

            var5 += 360 / (this.AN.length - 1);
         }

         this.AS = new iCommand(T.GN, 0, this);
         this.AU = new iCommand(T.GK, 1, this);
         if (var1 == 15) {
            this.AS = new iCommand(T.GN, 4, this);
            this.AU = new iCommand(T.GK, 5, this);
         }

         if (var1 == 18) {
            this.AS = new iCommand(T.GN, 11, this);
            this.AU = new iCommand(T.GK, 12, this);
         }

         if (var1 == 22) {
            this.AS = new iCommand(T.GN, 13, this);
            this.AU = new iCommand(T.GK, 14, this);
         }

         if (var1 != 18 && var1 != 22 && GameCanvas.isTouch) {
            this.AU.setPos(this.AN[0][0] + this.AD / 2, this.AF + this.AC - iCommand.hButtonCmdNor / 2 - 10, (FrameImage)null, this.AU.caption);
            this.AQ.addElement(this.AU);
         }

         if (!GameCanvas.isTouch) {
            super.DB = this.AW;
            super.DA = this.AU;
         }
      } else {
         var3 = this.AE + this.AK + (this.AB - this.AG * 3 - this.AK) / 6 + 5;
         var4 = this.AF + this.AC / 2 - this.AD / 2 - 5;
         boolean var8 = false;
         this.AN = mSystem.new_M_Int(var2 + 1, 2);
         var6 = var2 % 2;
         if (var2 <= 3) {
            if (var6 == 0) {
               var2 = this.AF + this.AC / 2 - 5 - var2 / 2 * (this.AD + this.AD / 4);
            } else {
               var2 = var4 - var2 / 2 * (this.AD + this.AD / 4);
            }
         } else {
            var2 = var4 - (this.AD + this.AD / 4);
         }

         for(int var7 = 0; var7 < this.AN.length; ++var7) {
            this.AN[var7][0] = var3 + var7 / 3 * (this.AD + this.AD / 4);
            this.AN[var7][1] = var2 + var7 % 3 * (this.AD + this.AD / 4);
            if (var7 == this.AN.length - 1) {
               this.AN[this.AN.length - 1][0] = var3 + ((this.AB - this.AG * 3 - this.AK) / 6 << 2);
               this.AN[this.AN.length - 1][1] = var4;
            }
         }

         this.AU = new iCommand(T.GK, 10, this);
         super.DA = this.AU;
      }

      mItemUpgrade = new MainItem[this.AN.length];
      if (this.AH >= 0) {
         this.AH();
      }

   }

   public void commandPointer(int var1, int var2) {
      (new StringBuffer("Vào commandPointer trong ScreenUp:")).append(var1).toString();
      iCommand var6;
      boolean var7;
      switch(var1) {
      case -1:
         if (BT) {
            SkinUpgradeScreen.AW();
         }

         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         return;
      case 0:
         if (this.AR != null) {
            if (this.AR.typeObject == 3) {
               GlobalService.getInstance().Upgrade_Item((byte)4, (short)this.AR.ID, (byte)0);
               return;
            }

            if (this.AR.typeObject == 7 && this.AR.AI()) {
               GlobalService.getInstance().Upgrade_Item((byte)5, (short)this.AR.ID, (byte)((byte)var2));
               return;
            }

            if (this.AR.typeObject == 7 && this.AR.typeMaterial == 3) {
               GlobalService.getInstance().Upgrade_Item((byte)6, (short)this.AR.ID, (byte)((byte)var2));
               return;
            }
         }
         break;
      case 1:
         if (mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.KU);
            return;
         }

         GlobalService.getInstance().Upgrade_Item((byte)1, (short)mItemUpgrade[0].ID, (byte)0);
         return;
      case 2:
         GlobalService.getInstance().Upgrade_Item((byte)8, (short)0, (byte)0);
         return;
      case 3:
         mVector var8;
         (var8 = new mVector()).addElement(this.CR);
         var8.addElement(this.AT);
         GameCanvas.menuCur.startAt(var8, 2, T.GK);
         return;
      case 4:
         if (this.AR != null) {
            if (this.AR.typeObject == 3) {
               GlobalService.getInstance().AA((byte)4, this.AR.ID, (byte)0, (byte)0);
               return;
            }

            if (this.AR.typeObject == 7 && this.AR.AI()) {
               GlobalService.getInstance().AA((byte)5, this.AR.ID, (byte)((byte)var2), (byte)1);
               return;
            }

            if (this.AR.typeObject == 7 && this.AR.typeMaterial == 3) {
               if (var2 == 0) {
                  GlobalService.getInstance().AA((byte)6, this.AR.ID, (byte)0, (byte)0);
                  return;
               }

               this.CS = new InputDialog();
               var6 = new iCommand(T.GN, 6, this);
               this.CS.AA(T.soluong, var6, false, this.AR.namepaint);
               GameCanvas.Start_Current_Dialog((MainDialog)this.CS);
               return;
            }

            if (this.AR.typeObject == 7 && this.AR.typeMaterial == 7) {
               GlobalService.getInstance().AA((byte)14, this.AR.ID, (byte)((byte)var2), (byte)1);
               return;
            }
         }
         break;
      case 5:
         if (mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.KU);
            return;
         }

         GlobalService.getInstance().AA((byte)1, mItemUpgrade[0].ID, (byte)0, (byte)0);
         return;
      case 6:
         if (this.AR != null) {
            var7 = false;

            try {
               if ((var1 = Integer.parseInt(this.CS.tfInput.getText())) < 0) {
                  var1 = 1;
               }
            } catch (Exception var5) {
               var1 = 1;
            }

            GlobalService.getInstance().AA((byte)6, this.AR.ID, (byte)1, (byte)((byte)var1));
         }

         GameCanvas.AK();
         return;
      case 7:
         if (this.AR != null) {
            var7 = false;

            try {
               if ((var1 = Integer.parseInt(this.CS.tfInput.getText())) < 0) {
                  var1 = 1;
               }
            } catch (Exception var4) {
               var1 = 1;
            }

            GlobalService.getInstance().AB((byte)5, this.AR.ID, (byte)1, (byte)((byte)var1));
         }

         GameCanvas.AK();
         return;
      case 8:
         if (this.AR != null) {
            var7 = false;

            try {
               if ((var1 = Integer.parseInt(this.CS.tfInput.getText())) < 0) {
                  var1 = 1;
               }
            } catch (Exception var3) {
               var1 = 1;
            }

            GlobalService.getInstance().AB((byte)6, this.AR.ID, (byte)1, (byte)((byte)var1));
         }

         GameCanvas.AK();
         return;
      case 9:
      default:
         break;
      case 10:
         GlobalService.getInstance().AB((byte)20, mItemUpgrade[mItemUpgrade.length - 1].ID, mItemUpgrade[mItemUpgrade.length - 1].typeObject, (short)0);
         return;
      case 11:
         if (this.AR != null) {
            if (this.AR.typeObject == 3) {
               GlobalService.getInstance().AB((byte)4, this.AR.ID, (byte)0, (byte)0);
               return;
            }

            if (this.AR.typeObject == 7 && this.AR.AI()) {
               if (var2 == 0) {
                  GlobalService.getInstance().AB((byte)5, this.AR.ID, (byte)0, (byte)0);
                  return;
               }

               this.CS = new InputDialog();
               var6 = new iCommand(T.GN, 7, this);
               this.CS.AA(T.soluong, var6, false, this.AR.namepaint);
               GameCanvas.Start_Current_Dialog((MainDialog)this.CS);
               return;
            }

            if (this.AR.typeObject == 7 && this.AR.typeMaterial == 3) {
               if (var2 == 0) {
                  GlobalService.getInstance().AB((byte)6, this.AR.ID, (byte)0, (byte)0);
                  return;
               }

               this.CS = new InputDialog();
               var6 = new iCommand(T.GN, 8, this);
               this.CS.AA(T.soluong, var6, false, this.AR.namepaint);
               GameCanvas.Start_Current_Dialog((MainDialog)this.CS);
               return;
            }

            if (this.AR.typeObject == 7 && this.AR.typeMaterial == 7) {
               GlobalService.getInstance().AB((byte)14, this.AR.ID, (byte)var2, (byte)1);
               return;
            }
         }
         break;
      case 12:
         if (mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.KU);
            return;
         }

         GlobalService.getInstance().AB((byte)1, mItemUpgrade[0].ID, (byte)0, (byte)0);
      }

   }

   public void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      this.AG(var1);
      if (this.Step == 0) {
         this.AJ(var1);
      } else if (this.BG != 22) {
         if (this.BK == null) {
            this.BK = new FrameImage(mImage.createImage("/interface/effupgrade.png"), 58, 50);
         }

         this.BK.drawFrame(this.frameEff % this.BK.nFrame, this.AI + this.AK / 2 + 10, this.AJ + this.AL / 2, 0, 3, var1);
      }

      GameCanvas.resetTrans(var1);
      Interface_Game.paintNumMess(var1, -Interface_Game.xNumMess + 8, -Interface_Game.yNumMess + 3);
      this.AA(var1, -1);
      this.AE(var1);
      this.AH(var1);
      this.AB(var1);
      if (this.AQ != null && GameCanvas.AL()) {
         for(int var2 = 0; var2 < this.AQ.size(); ++var2) {
            iCommand var3;
            (var3 = (iCommand)this.AQ.elementAt(var2)).paint(var1, var3.xCmd, var3.yCmd);
         }
      }

      if (this.AO && this.Step == 0 && this.AR != null) {
         MainTab.AA(var1, this.AR, (mVector)null, (byte)0, this.AA, this.CQ, this.AR.BS, this.AR.BT, false, (MainObject)null, 0);
      }

      super.paint(var1);
      Interface_Game.AA(var1, MotherCanvas.hw - Interface_Game.BW / 2, 0 + GameScreen.h12plus, true, 0);
      this.AF(var1);
      this.AA(var1, 0);
   }

   public void AB(mGraphics var1) {
      int var2;
      int var3;
      if (this.BG == 20) {
         mFont.tahoma_7b_black.drawString(var1, T.NS + valueTile + "%", this.BF, this.AJ, 0);
         var2 = this.AI + this.AK + 20;
         var3 = this.AJ + this.AL - 20;
         AvMain.paintRect(var1, var2, var3, Interface_Game.BW - 50, 30, (byte)1, 4);
         var3 += 9;
         AvMain.fraMoney.drawFrame(0, var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth / 2 - 3, var3, 0, 3, var1);
         AvMain.fraMoney.drawFrame(1, var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth / 2 + 45, var3, 0, 3, var1);
         AvMain.fraMoney.drawFrame(7, var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth / 2 - 3, var3 + 15, 0, 3, var1);
         mFont.tahoma_7_yellow.drawString(var1, " " + AvMain.AB((long)valueMonney_1), var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth - 4, var3 - 5, 0);
         mFont.tahoma_7_red.drawString(var1, " " + AvMain.AA((long)valueMonney_2), var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth + 45, var3 - 5, 0);
         mFont.tahoma_7_green.drawString(var1, " " + AvMain.AB((long)valueMonney_3), var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth - 4, var3 - 5 + 15, 0);
      } else if (this.BG == 18) {
         mFont.tahoma_7b_black.drawString(var1, T.SQ + IM + "%", this.BF, this.AJ, 0);
         var2 = this.AI + this.AK + 20;
         var3 = this.AJ + this.AL - 20;
         AvMain.paintRect(var1, var2, var3, Interface_Game.BW - 50, 30, (byte)1, 4);
         var3 += 9;
         AvMain.fraMoney.drawFrame(0, var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth / 2 - 3, var3, 0, 3, var1);
         AvMain.fraMoney.drawFrame(1, var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth / 2 + 45, var3, 0, 3, var1);
         AvMain.fraMoney.drawFrame(7, var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth / 2 - 3, var3 + 15, 0, 3, var1);
         mFont.tahoma_7_yellow.drawString(var1, " " + AvMain.AB((long)valueMonney_1), var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth - 4, var3 - 5, 0);
         mFont.tahoma_7_red.drawString(var1, " " + AvMain.AA((long)valueMonney_2), var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth + 45, var3 - 5, 0);
         mFont.tahoma_7_green.drawString(var1, " " + AvMain.AB((long)valueMonney_3), var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth - 4, var3 - 5 + 15, 0);
      } else if (this.BG == 15 && mItemUpgrade[0] != null) {
         mFont.tahoma_7b_black.drawString(var1, T.SQ + IM + "%", this.BF, this.AJ, 0);
      } else {
         if (mItemUpgrade[0] != null && mItemUpgrade[0].LvUpgrade < BH.length) {
            if (mItemUpgrade[1] != null) {
               mFont.tahoma_7b_black.drawString(var1, T.NS + BH[mItemUpgrade[0].LvUpgrade] / 10 + "% + " + BH[mItemUpgrade[0].LvUpgrade] / valueLucky + "%", this.BF, this.AJ, 0);
               return;
            }

            mFont.tahoma_7b_black.drawString(var1, T.NS + BH[mItemUpgrade[0].LvUpgrade] / 10 + "%", this.BF, this.AJ, 0);
         }

      }
   }

   public void AE(mGraphics var1) {
   }

   public void AF(mGraphics var1) {
   }

   public void AG(mGraphics var1) {
      int var10003 = this.AF - 17;
      int var10005 = this.AC + 17;
      int var10006 = this.AB;
      this.AD(var1, this.AE, var10003, this.AB, var10005, 0);
      AvMain.paintRect(var1, this.AI, this.AJ, this.AK, this.AL, (byte)0, 3);
      var1.setColor(-2573687);
      var1.fillRoundRectNew(this.AE + this.AB / 2 - 60, this.AF - 15 + 10, 120, 16, 4, 4);
      AvMain.FontBorderColor(var1, this.BA, this.AE + this.AB / 2, this.AF - 15 + 12, 2, (int)6, (int)5);
      if (this.BG == 15 || this.BG == 18 || this.BG == 22) {
         if (this.imghoavan == null) {
            this.imghoavan = mImage.createImage("/interface/u_hoavan.png");
            this.CH = mImage.createImage("/interface/u_tron.png");
         }

         var1.drawRegion(this.imghoavan, 0, 0, 35, 35, 0, this.AE + this.AB - 35 - 10, this.AF + this.AC - 35 - 10, 0);
         var1.drawRegion(this.imghoavan, 0, 0, 35, 35, 1, this.AE + this.AB - 35 - 10, this.AF - 5, 0);
         var1.drawRegion(this.imghoavan, 0, 0, 35, 35, 2, this.AE + 10, this.AF + this.AC - 35 - 10, 0);
         var1.drawRegion(this.imghoavan, 0, 0, 35, 35, 3, this.AE + 10, this.AF - 5, 0);
         var1.drawRegion((mImage)this.CH, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, 3);
      }

      if (this.BG == 21) {
         MainImage var2;
         if ((var2 = ObjectData.getImageOther(SplitScreen.instance.CU, (short)0)) != null && var2.img != null) {
            var1.drawRegion((mImage)var2.img, this.AN[0][0] + this.AD / 2, this.AN[0][1] - this.AD / 6, 33);
         }
         if (SplitScreen.instance.CT.size() > 0) {
            mFont.tahoma_7b_red.drawString(var1, SplitScreen.instance.CV, this.AN[0][0] + this.AD / 2, this.AJ, 2);
         } else {
            mFont.tahoma_7b_black.drawString(var1, SplitScreen.instance.CV, this.AN[0][0] + this.AD / 2, this.AJ, 2);
         }

         if (mItemUpgrade[0] != null && mItemUpgrade[0].info != null) {
            String[] var3;
            if ((var3 = mFont.tahoma_7_black.splitFontArray(mItemUpgrade[0].info, 120)).length == 1) {
               mFont.tahoma_7_black.drawString(var1, mItemUpgrade[0].info, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD * 5 / 4, 2);
               return;
            }

            if (var3.length > 1) {
               mFont.tahoma_7_black.drawString(var1, var3[0], this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD * 5 / 4 - 5, 2);
               mFont.tahoma_7_black.drawString(var1, var3[1], this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD * 5 / 4 + 5, 2);
            }
         }
      }

   }

   public void AH(mGraphics var1) {
      for(int var2 = 0; var2 < this.AN.length; ++var2) {
         AvMain.paintRect(var1, this.AN[var2][0], this.AN[var2][1], this.AD, this.AD, (byte)1, 3);
         mFont var3;
         String var4;
         int var5;
         int var6;
         if (this.BG == 22) {
            this.BO = this.AN[1][0] + 10;
            this.BP = this.AN[1][1] - 10;
            var1.drawRegion((mImage)AvMain.imgBorderCombo, this.BO, this.BP, 3);
            var3 = mFont.tahoma_7b_black;
            var4 = "Auto";
            var5 = var3.getHeight();
            var6 = var3.getWidth(var4);
            var3.drawString(var1, var4, this.BO + 10, this.BP - 5, 0);
            this.BQ = var6 + 20;
            this.BR = var5;
            if (BT) {
               AvMain.fraCheck.drawFrame(2, this.BO, this.BP, 0, 3, var1);
            }
         } else if (this.BG == 5 || this.BG == 15) {
            this.CT = this.AN[1][0] + 40;
            this.CU = this.AN[1][1] - 15;
            var1.drawRegion((mImage)AvMain.imgBorderCombo, this.CT, this.CU, 3);
            var3 = mFont.tahoma_7b_black;
            var4 = "Auto";
            var5 = var3.getHeight();
            var6 = var3.getWidth(var4);
            var3.drawString(var1, var4, this.CT + 10, this.CU - 5, 0);
            this.CV = var6 + 20;
            this.CW = var5;
            if (isClickUpgradeItemToSucess && this.BG == 5) {
               AvMain.fraCheck.drawFrame(2, this.CT, this.CU, 0, 3, var1);
            }

            if (isClickUpgradeSuperItemToSucess && this.BG == 15) {
               AvMain.fraCheck.drawFrame(2, this.CT, this.CU, 0, 3, var1);
            }
         }

         if (mItemUpgrade[var2] != null && !mItemUpgrade[var2].CE) {
            mItemUpgrade[var2].AC(var1, this.AN[var2][0] + this.AD / 2, this.AN[var2][1] + this.AD / 2, this.AD - 1);
            if (this.BG == 20) {
               if (mItemUpgrade[var2].CH) {
                  mItemUpgrade[var2].AA(var1, this.AN[var2][0] + this.AD / 2, this.AN[var2][1] + this.AD / 2, this.AD, 0, mItemUpgrade[var2].colorName, mItemUpgrade[var2].numPotion);
               }
            } else {
               mItemUpgrade[var2].AA(var1, this.AN[var2][0] + this.AD / 2, this.AN[var2][1] + this.AD / 2, this.AD, 0, mItemUpgrade[var2].colorName);
            }
         }
      }

      this.AI(var1);
   }

   public void AI(mGraphics var1) {
   }

   public final void AA(mGraphics var1, int var2) {
      for(int var3 = 0; var3 < this.vecEff.size(); ++var3) {
         MainEffect var4 = (MainEffect)this.vecEff.elementAt(var3);
         if (var2 == 0 && var4.levelPaint >= 0) {
            var4.paint(var1);
         } else if (var4.levelPaint == -1) {
            var4.paint(var1);
         }
      }

   }

   public void AA(mGraphics var1, int var2, int var3, MainItem var4) {
      for(int var5 = 0; var5 < mItemUpgrade.length - 2; ++var5) {
         if (mItemUpgrade[var5] != null && mItemUpgrade[var5].ID == var4.ID && mItemUpgrade[var5].typeObject == var4.typeObject) {
            var1.drawRegion((mImage)AvMain.imgcheck, var2, var3, 36);
         }
      }

   }

   public void AJ(mGraphics var1) {
      var1.AD(this.AI - 1, this.AJ + 1, this.AK + 2, this.AL - 1);
      mGraphics.AC();
      int var10000 = this.AI;
      var10000 = this.AJ;
      var10000 = this.AK;
      var10000 = this.AL;
      mGraphics.AD();
      var1.translate(this.AI, this.AJ);
      var1.translate(0, -this.AP.AC);

      int var2;
      for(var2 = 0; var2 < this.BN.size(); ++var2) {
         MainItem var3;
         if ((var3 = (MainItem)this.BN.elementAt(var2)).typeObject == 3) {
            var3.AC(var1, var2 % this.AM * this.AD + this.AD / 2, var2 / this.AM * this.AD + this.AD / 2, this.AD);
         }

         var3.paint(var1, var2 % this.AM * this.AD + this.AD / 2, var2 / this.AM * this.AD + this.AD / 2, this.AD);
         this.AA(var1, var2 % this.AM * this.AD + 3, var2 / this.AM * this.AD + this.AD - 2, var3);
         this.AA(var1, var3, var2 % this.AM * this.AD + this.AD / 2, var2 / this.AM * this.AD + this.AD / 2);
         int var10003 = var2 % this.AM * this.AD + this.AD / 2;
         int var7 = var2 / this.AM * this.AD + this.AD / 2;
         int var6 = var10003;
         boolean var8 = false;
         if (this.BM == 1) {
            if (var3.typeObject == -8) {
               var8 = false;
            } else if (var3.typeObject != 4 || var3.ID < 44 || var3.ID > 79 && var3.ID < 221 || var3.ID > 226 && var3.ID < 241 || var3.ID > 270 && var3.ID < 362 || var3.ID > 373 && var3.ID < 493 || var3.ID > 517) {
               var8 = true;
            }
         } else if (this.BM == 2) {
            if (var3.typeObject == -8) {
               var8 = false;
            } else if (var3.typeObject != 4) {
               if (var3.typeObject != 3) {
                  var8 = true;
               }
            } else if (var3.ID < 44 || var3.ID > 79 && var3.ID < 221 || var3.ID > 226 && var3.ID < 241 || var3.ID > 270 && var3.ID < 362 && var3.ID != 324 && var3.ID != 325 && var3.ID != 326 || var3.ID > 373) {
               var8 = true;
            }

            if (var3.BQ == -18 || var3.BQ == 66 || var3.BQ == 12) {
               var8 = false;
            }
         } else {
            label129: {
               if (this.BM == 3) {
                  if (var3.typeObject == -8) {
                     var8 = false;
                     break label129;
                  }

                  if (var3.typeObject == 3) {
                     break label129;
                  }
               } else {
                  if (this.BM == 4) {
                     if (var3.typeObject != 3 && (var3.typeObject != 4 || var3.ID != 323 && var3.ID != 339)) {
                        var8 = true;
                     }
                     break label129;
                  }

                  if (this.BM != 5 || var3.typeObject == 3 || var3.typeObject == 4 && var3.ID == 457) {
                     break label129;
                  }
               }

               var8 = true;
            }
         }

         if (var8) {
            var1.drawRegion(AvMain.imgDelay, 0, 0, this.AD, this.AD, 0, var6, var7, 3);
         }

         if (this.AH == var2 && this.BE == 0) {
            var1.setColor(-1);
            var1.drawRect(var2 % this.AM * this.AD + 1, var2 / this.AM * this.AD + 1, this.AD - 2, this.AD - 2);
            if (!GameCanvas.isSmallScreen) {
               var1.drawRect(var2 % this.AM * this.AD + 2, var2 / this.AM * this.AD + 2, this.AD - 4, this.AD - 4);
            }
         }
      }

      if (Player.maxInventory % this.AM != 0) {
         for(var2 = Player.maxInventory; var2 < Player.maxInventory + (this.AM - Player.maxInventory % this.AM); ++var2) {
            var1.drawRegion(AvMain.imgDelay, 0, 0, this.AD - 1, this.AD - 1, 0, var2 % this.AM * this.AD + 1, var2 / this.AM * this.AD + 1, 0);
         }
      }

      var1.setColor(-2701384);

      for(var2 = 0; var2 < this.AM - 1; ++var2) {
         var1.fillRect(this.AD + var2 * this.AD, 1, 1, this.AD * ((Player.maxInventory - 1) / this.AM + 1));
      }

      for(var2 = 0; var2 <= (Player.maxInventory - 1) / this.AM + 1; ++var2) {
         var1.fillRect(1, var2 * this.AD, this.AK - 1, 1);
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
   }

   public void AA(mGraphics var1, MainItem var2, int var3, int var4) {
   }

   public void update() {
      if (super.KP != null) {
         super.KP.update();
      }

      int var1 = this.AP.AC;
      this.AP.AC();
      if (this.AP.AC == var1 && !this.AP.AE) {
         if (this.AR != null) {
            if (!this.AO) {
               ++this.IP;
               if (this.IP >= MainTab.AY) {
                  this.AO = true;
                  this.AF();
               }
            } else {
               this.IP = 0;
            }
         }
      } else {
         this.AO = false;
      }

      for(var1 = 0; var1 < this.vecEff.size(); ++var1) {
         MainEffect mainEffect;
         (mainEffect = (MainEffect)this.vecEff.elementAt(var1)).update();
         if (mainEffect.isStop) {
            this.vecEff.removeElement(mainEffect);
            --var1;
         }
      }

      if (this.Step != 0) {
         if (this.frameEff == 0 && CRes.random(7) < 3) {
            this.isRunEff = true;
            this.frameEndEff = (1 + CRes.random(2)) * 3;
         }

         if (this.isRunEff) {
            ++this.frameEff;
         }

         if (this.frameEff >= this.frameEndEff) {
            this.isRunEff = false;
            this.frameEff = 0;
         }
      }

      super.update();
      this.setStep();
      long delay;
      int num2;
      if (isContinueUpgradeItem && isClickUpgradeItemToSucess) {
         long now = System.currentTimeMillis();
         int base = calcAutoUpgradeDelay(); // dùng hàm mới
         if (now - this.timeStartUpgradeItemAuto >= (long)base) {
            if (beriOrRuby != 1 && beriOrRuby != 2) {
               stopUpgradeItemAuto();
            } else {
               // gửi request nâng cấp (giữ nguyên hành vi)
               GlobalService.getInstance().Upgrade_Item((byte)2, (short)ReadMessenge.idItemUpgrade, (byte)beriOrRuby);
               isContinueUpgradeItem = false;
               // cập nhật timeStart để lần sau tính tiếp (nếu có set isContinue true lần nữa)
               this.timeStartUpgradeItemAuto = now;
            }
         }
      }


      if (isContinueUpgradeSuperItem && isClickUpgradeSuperItemToSucess) {
         delay = System.currentTimeMillis();
         num2 = (int)(800.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
         if (delay - this.IL >= (long)num2) {
            int[] var8 = new int[3];
            if (mItemUpgradeSaveTemp[1] != null) {
               var8[0] = AA((short)mItemUpgradeSaveTemp[1].ID, (int)1) ? 0 : 1;
            } else {
               var8[0] = -1;
            }

            if (mItemUpgradeSaveTemp[2] != null) {
               var8[1] = AA((short)mItemUpgradeSaveTemp[2].ID, (int)mItemUpgradeSaveTemp[2].numPotion) ? 0 : 1;
            } else {
               var8[1] = -1;
            }

            if (mItemUpgradeSaveTemp[3] != null) {
               var8[2] = AA((short)mItemUpgradeSaveTemp[3].ID, (int)1) ? 0 : 1;
            } else {
               var8[2] = -1;
            }

            var8 = var8;
            String var9 = "";

            for(int var3 = 0; var3 < var8.length; ++var3) {
               if (var8[var3] == 1) {
                  if (var3 == 0) {
                     var9 = T.YN;
                  }

                  if (var3 == 1) {
                     if (!var9.equals("")) {
                        var9 = var9 + T.YO + T.YM;
                     } else {
                        var9 = T.YM;
                     }
                  }

                  if (var3 == 2) {
                     if (!var9.equals("")) {
                        var9 = var9 + T.YO + T.YL;
                     } else {
                        var9 = T.YL;
                     }
                  }
               }
            }

            if (!var9.equals("")) {
               stopUpgradeSuperItemAuto();
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(var9 + T.YK);
            } else {
               for(var1 = 1; var1 < mItemUpgradeSaveTemp.length; ++var1) {
                  if (mItemUpgradeSaveTemp[var1] != null) {
                     if (var1 == 1) {
                        GlobalService.getInstance().AA((byte)5, mItemUpgradeSaveTemp[var1].ID, (byte)1, (byte)1);
                     } else if (var1 == 2) {
                        GlobalService.getInstance().AA((byte)6, mItemUpgradeSaveTemp[var1].ID, (byte)1, (byte)((byte)mItemUpgradeSaveTemp[var1].numPotion));
                     } else if (var1 == 3) {
                        GlobalService.getInstance().AA((byte)14, mItemUpgradeSaveTemp[var1].ID, (byte)1, (byte)1);
                     }
                  }
               }

               if (mItemUpgradeSaveTemp[0] != null && beriOrRuby != -1) {
                  GlobalService.getInstance().AA((byte)2, mItemUpgradeSaveTemp[0].ID, (byte)beriOrRuby, (byte)0);
               } else {
                  stopUpgradeSuperItemAuto();
               }
            }

            isContinueUpgradeSuperItem = false;
         }
      }

   }

   public void AF() {
      this.AA(this.AR, this.AI, this.AJ + (this.AH / this.AM + 1) * this.AD - this.AP.AC + 4);
   }

   public final void AA(MainItem var1, int var2, int var3) {
      int var4 = 100;
      int var5 = 50;
      if (var1 != null) {
         var4 = var1.BS;
         var5 = var1.BT;
      }

      this.AA = var2 - var4 / 2;
      if (this.AA + var4 > MotherCanvas.w - 8) {
         this.AA = MotherCanvas.w - var4 - 8;
      }

      if (this.AA < 8) {
         this.AA = 8;
      }

      this.CQ = var3;
      if (this.CQ + var5 > MotherCanvas.h - GameCanvas.hCommand - 8) {
         this.CQ = MotherCanvas.h - GameCanvas.hCommand - var5 - 8;
      }

      if (this.CQ < 8) {
         this.CQ = 8;
      }

   }

   public final void AK(mGraphics var1) {
      super.paint(var1);
   }

   public void handleKeyPress() {
      if (this.Step == 0) {
         boolean var1 = false;
         if (GameCanvas.isKeyPressed(0)) {
            --this.AH;
            GameCanvas.clearKeyPressed(0);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(2)) {
            ++this.AH;
            GameCanvas.clearKeyPressed(2);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(1)) {
            if (this.AH >= this.AM) {
               this.AH -= this.AM;
            }

            GameCanvas.clearKeyPressed(1);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(3)) {
            if (this.AH < this.BN.size() - this.AM) {
               this.AH += this.AM;
            }

            GameCanvas.clearKeyPressed(3);
            var1 = true;
         }

         if (var1) {
            this.AH();
         }

         super.handleKeyPress();
         this.AS();
      }
   }

   public final void AG() {
      super.handleKeyPress();
   }

   public void updatePointer() {
      if (GameCanvas.isPointerSelect && this.BG == 5 && GameCanvas.isPoint(this.CT - 10, this.CU - 12, this.CV, this.CW + 10)) {
         if (!(isClickUpgradeItemToSucess = !isClickUpgradeItemToSucess)) {
            stopUpgradeItemAuto();
         }

         GameCanvas.isPointerSelect = false;
      }

      if (GameCanvas.isPointerSelect && this.BG == 15 && GameCanvas.isPoint(this.CT - 10, this.CU - 12, this.CV, this.CW + 10)) {
         if (!(isClickUpgradeSuperItemToSucess = !isClickUpgradeSuperItemToSucess)) {
            stopUpgradeSuperItemAuto();
         }

         GameCanvas.isPointerSelect = false;
      }

      if (this.Step == 0) {
         this.AP.update_Pos_UP_DOWN();
         int var1;
         if (GameCanvas.AB(this.AI, this.AJ, this.AK, this.AL)) {
            var1 = (GameCanvas.AY - this.AI) / this.AD + (GameCanvas.AZ - this.AJ + this.AP.AC) / this.AD * this.AM;
            int var2 = this.BN.size();
            if (var1 >= 0 && var1 < var2) {
               GameCanvas.isPointerSelect = false;
               if (var1 == this.AH) {
                  if (this.AR != null && (this.AR.typeObject == 3 || this.AR.typeObject == 7 && (this.AR.AI() || this.AR.typeMaterial == 3 || this.AR.typeMaterial == 7) || this.AR.typeObject == 4 && (this.AR.ID == 457 || this.AR.ID == 323 || this.AR.ID == 339)) && this.AS != null) {
                     this.AS.AD();
                  }
               } else {
                  this.AO = false;
                  this.AH = var1;
                  this.AA(this.getMenuActionItem());
               }
            } else {
               this.AO = false;
               this.AH = -1;
               this.AA((mVector)null);
            }
         }

         if (this.AQ != null) {
            for(var1 = 0; var1 < this.AQ.size(); ++var1) {
               ((iCommand)this.AQ.elementAt(var1)).AE();
            }
         }

         super.updatePointer();
      }
   }

   public void AH() {
      this.AO = false;
      this.AH = AvMain.AA(this.AH, this.BN.size() - 1, false);
      if (GameCanvas.isKeyPressed()) {
         this.AP.AA((this.AH / this.AM + 1) * this.AD - this.AC / 2);
      }

      if (this.AH >= 0) {
         this.AA(this.getMenuActionItem());
      }

   }

   public mVector getMenuActionItem() {
      mVector var1 = null;
      MainItem var2;
      if ((var2 = (MainItem)this.BN.elementAt(this.AH)) != null) {
         if (this.BG == 20) {
            this.AR = var2;
            return null;
         }

         this.AR = var2;
         this.AS.caption = T.GN;
         this.AS.AF = 1;
         if (this.AA(this.AR.ID, this.AR.typeObject)) {
            this.AS.caption = T.GM;
            this.AS.AF = 0;
         }
      }

      if (this.AR != null) {
         var1 = this.AR.getActionUpgrade();
      }

      if (this.BG == 15 || this.BG == 18) {
         if (mItemUpgrade[0] != null && (mItemUpgrade[0].LvUpgrade == 10 || this.BG == 18 && mItemUpgrade[0].LvUpgrade == 0)) {
            IM = 0;
         } else if (this.BG == 18) {
            IM = this.CM - this.CE;
            if (mItemUpgrade[3] != null) {
               IM -= 20;
            }
         } else {
            int var3 = 0;
            if (mItemUpgrade[2] != null) {
               var3 = mItemUpgrade[2].numPotion * 15;
            }

            if (mItemUpgrade[3] != null) {
               var3 += 20;
            }

            if (var3 > 80) {
               var3 = 80;
            }

            IM = 80 - var3;
         }
      }

      return var1;
   }

   public void AA(mVector var1) {
      super.DA = null;
      super.center = null;
      this.AQ.removeAllElements();
      if (var1 != null) {
         int var2;
         iCommand var3;
         if (GameCanvas.isTouch) {
            this.AQ = var1;

            for(var2 = 0; var2 < this.AQ.size(); ++var2) {
               var3 = (iCommand)this.AQ.elementAt(var2);
               if (var2 == 0) {
                  var3 = AvMain.AA(var3, 1);
                  super.DE = var3;
               }

               if (var2 == 1) {
                  var3 = AvMain.AA(var3, 2);
                  super.backCMD = var3;
               }
            }
         } else {
            for(var2 = 0; var2 < var1.size(); ++var2) {
               var3 = (iCommand)var1.elementAt(var2);
               if (var2 == 0) {
                  super.center = var3;
               }

               if (var2 == 1) {
                  super.DA = var3;
               }
            }
         }
      }

      if (GameCanvas.isTouch) {
         if (this.BG == 20) {
            this.AQ.addElement(this.AT);
            super.DA = this.AU;
            if (GameCanvas.isKeyPressed()) {
               super.backCMD = this.AT;
               super.DF = this.AU;
               return;
            }
         } else if (this.BG != 18 && this.BG != 22) {
            this.AQ.addElement(this.AT);
            this.AQ.addElement(this.AU);
            iCommand var4;
            if ((var4 = this.AJ()) != null) {
               this.AQ.addElement(var4);
            }

            if (GameCanvas.isKeyPressed()) {
               super.backCMD = this.AT;
               super.DF = this.AU;
               return;
            }
         } else {
            this.AQ.addElement(this.AT);
            super.DB = this.AU;
            if (GameCanvas.isKeyPressed()) {
               super.backCMD = this.AT;
               super.DF = this.AU;
               return;
            }
         }
      } else {
         if (this.BG == 20) {
            super.DA = this.AU;
            return;
         }

         super.DB = this.AW;
         super.DA = this.AU;
      }

   }

   public iCommand AJ() {
      return this.CR;
   }

   public boolean AA(short var1, byte var2) {
      for(int var3 = 1; var3 < mItemUpgrade.length; ++var3) {
         if (mItemUpgrade[var3] != null && mItemUpgrade[var3].ID == var1 && mItemUpgrade[var3].typeObject == var2) {
            return true;
         }
      }

      return false;
   }

   public void AK() {
      boolean var1 = false;
      boolean var2 = false;
      this.AO = false;
      this.AH();
      MainItem var3;
      if (mItemUpgrade[1] != null) {
         if ((var3 = MainItem.getItemVec((byte)7, mItemUpgrade[1].ID, this.BN)) != null && var3.numPotion > 0) {
            GlobalService.getInstance().Upgrade_Item((byte)5, (short)var3.ID, (byte)1);
            var2 = false;
         } else {
            mItemUpgrade[1] = null;
            var2 = true;
         }
      }

      if (mItemUpgrade[2] != null) {
         if ((var3 = MainItem.getItemVec((byte)7, mItemUpgrade[2].ID, this.BN)) != null && var3.numPotion > 0) {
            GlobalService.getInstance().Upgrade_Item((byte)6, (short)var3.ID, (byte)1);
            var1 = false;
         } else {
            mItemUpgrade[2] = null;
            var1 = true;
         }
      }

      if (isContinueUpgradeItem && (var2 || var1)) {
         stopUpgradeItemAuto();
         if (var2 && var1) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.YN + " và " + T.YM + T.YK);
            return;
         }

         if (var2) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.YN + T.YK);
            return;
         }

         if (var1) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.YM + T.YK);
         }
      }

   }

   public final void setDataUpgrade() {
      if (mItemUpgrade[0] != null) {
         MainDataUpgrade var1 = AY[mItemUpgrade[0].LvUpgrade];

         int var2;
         for(var2 = 3; var2 < mItemUpgrade.length; ++var2) {
            mItemUpgrade[var2] = null;
         }

         var2 = 3;

         for(int var3 = 0; var3 < var1.AA.length; ++var3) {
            MainMaterial var4 = (MainMaterial)MainItem.hashMaterialTem.get("" + var1.AA[var3][0]);
            if (mItemUpgrade[0].colorName == var1.AA[var3][2] || var1.AA[var3][2] == -1) {
               byte var5 = 5;
               MainItem var6;
               if ((var6 = MainItem.getItemVec((byte)7, var1.AA[var3][0], this.BN)) == null || var6.numPotion < var1.AA[var3][1]) {
                  var5 = 6;
               }

               MainItem var7 = new MainItem((byte)7, var4.ID, var4.idIcon, var1.AA[var3][1], var5, (byte)0);
               mItemUpgrade[var2] = var7;
               ++var2;
            }
         }

      }
   }

   public void setStep() {
      int var3;
      if (this.Step != 1 && this.Step != 2) {
         if (this.Step == 3) {
            ++this.BD;
            if (this.BD >= this.CK) {
               this.Step = 0;
               this.CK = 0;
               this.BD = 0;
               this.indexStep = 1;
               if (this.BG == 5 && isClickUpgradeItemToSucess) {
                  if (isUpgradeItemSucess) {
                     stopUpgradeItemAuto();
                  } else {
                     isContinueUpgradeItem = true;
                     this.timeStartUpgradeItemAuto = System.currentTimeMillis();
                  }
               }

               if (this.BG == 15 && isClickUpgradeSuperItemToSucess) {
                  if (CC) {
                     stopUpgradeSuperItemAuto();
                  } else {
                     isContinueUpgradeSuperItem = true;
                     this.IL = System.currentTimeMillis();
                  }
               }

               if (!isContinueUpgradeItem && !isContinueUpgradeSuperItem) {
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog(this.showServer);
               }

               if (this.BG == 20) {
                  this.indexStep = 0;

                  for(var3 = 0; var3 < mItemUpgrade.length - 1; ++var3) {
                     MainItem var2;
                     if ((var2 = MainItem.getItemVec(mItemUpgrade[var3].typeObject, mItemUpgrade[var3].ID, this.BN)) != null) {
                        mItemUpgrade[var3].numPotion = var2.numPotion;
                     } else {
                        mItemUpgrade[var3].numPotion = 0;
                     }

                     mItemUpgrade[var3].CH = true;
                  }

                  return;
               }

               if (this.BG == 18) {
                  GlobalService.getInstance().AB((byte)4, mItemUpgrade[0].ID, (byte)0, (byte)0);

                  for(var3 = 0; var3 < mItemUpgrade.length; ++var3) {
                     mItemUpgrade[var3] = null;
                  }

                  return;
               }

               if (this.BG == 15) {
                  GlobalService.getInstance().AA((byte)4, mItemUpgrade[0].ID, (byte)0, (byte)0);

                  for(var3 = 0; var3 < mItemUpgrade.length; ++var3) {
                     mItemUpgrade[var3] = null;
                  }

                  return;
               }

               if (this.BG != 12) {
                  GlobalService.getInstance().Upgrade_Item((byte)4, (short)mItemUpgrade[0].ID, (byte)0);
                  this.AK();
               }
            }
         }
      } else {
         float var10000;
         if (this.BD % 15 == 0) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         ++this.BD;
         this.AO();
         if (this.BD % 5 == 0) {
            if (this.indexStep < mItemUpgrade.length) {
               if (this.BD > 50) {
                  this.indexStep = mItemUpgrade.length;
               }

               if (this.BG == 20) {
                  for(var3 = this.indexStep; var3 < mItemUpgrade.length - 1; ++var3) {
                     if (mItemUpgrade[var3] != null) {
                        this.AA((short)75, 0, this.AN[var3][0] + this.AD / 2, this.AN[var3][1] + this.AD / 2, this.AN[mItemUpgrade.length - 1][0] + this.AD / 2, this.AN[mItemUpgrade.length - 1][1] + this.AD / 2);
                        mItemUpgrade[var3].CH = false;
                        this.indexStep = var3 + 1;
                        this.CK = this.BD + 11 + ((this.AB - this.AG * 3 - this.AK) / 2 - 15) / 5;
                        return;
                     }
                  }

                  return;
               }

               for(var3 = this.indexStep; var3 < mItemUpgrade.length; ++var3) {
                  if (mItemUpgrade[var3] != null) {
                     this.AA((short)75, 0, this.AN[var3][0] + this.AD / 2, this.AN[var3][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
                     mItemUpgrade[var3].CE = true;
                     this.indexStep = var3 + 1;
                     this.CK = this.BD + 11 + ((this.AB - this.AG * 3 - this.AK) / 2 - 15) / 5;
                     return;
                  }
               }

               return;
            }

            if (this.BD >= this.CK) {
               byte var1;
               if (this.Step == 1) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  var1 = 0;
                  if (GameCanvas.language == 1) {
                     var1 = 2;
                  }

                  if (this.BG == 20) {
                     this.AA((short)79, var1, this.AN[this.AN.length - 1][0] + this.AD / 2, this.AN[this.AN.length - 1][1] + this.AD / 2, this.AN[this.AN.length - 1][0] + this.AD / 2, this.AN[this.AN.length - 1][1] + this.AD / 2);
                     this.AA((short)76, 0, this.AN[this.AN.length - 1][0] + this.AD / 2, this.AN[this.AN.length - 1][1] + this.AD / 2, this.AN[this.AN.length - 1][0] + this.AD / 2, this.AN[this.AN.length - 1][1] + this.AD / 2);
                  } else {
                     this.AA((short)79, var1, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
                     this.AA((short)76, 0, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
                  }

                  this.AA((short)53, 0, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
               } else if (this.Step == 2) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  var1 = 1;
                  if (GameCanvas.language == 1) {
                     var1 = 3;
                  }

                  if (this.BG == 20) {
                     this.AA((short)79, var1, this.AN[this.AN.length - 1][0] + this.AD / 2, this.AN[this.AN.length - 1][1] + this.AD / 2, this.AN[this.AN.length - 1][0] + this.AD / 2, this.AN[this.AN.length - 1][1] + this.AD / 2);
                     this.AA((short)77, 0, this.AN[this.AN.length - 1][0] + this.AD / 2, this.AN[this.AN.length - 1][1] + this.AD / 2, this.AN[this.AN.length - 1][0] + this.AD / 2, this.AN[this.AN.length - 1][1] + this.AD / 2);
                  } else {
                     this.AA((short)79, var1, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
                     this.AA((short)77, 0, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
                  }
               }

               this.Step = 3;
               this.BD = 0;
               this.CK = 20;
               return;
            }
         }
      }

   }

   public final void AO() {
      if (this.BG != 20) {
         if (this.BG != 15 && this.BG != 18 && this.BG != 22) {
            int var1;
            if (GameCanvas.gameTick % 15 == 0) {
               for(var1 = 1; var1 < this.AN.length; ++var1) {
                  if (var1 == this.AN.length - 1) {
                     this.AA((short)73, var1 - 1, this.AN[var1][0] + this.AD / 2, this.AN[var1][1] + this.AD / 2, this.AN[1][0] + this.AD / 2, this.AN[1][1] + this.AD / 2);
                  } else {
                     this.AA((short)73, var1 - 1, this.AN[var1][0] + this.AD / 2, this.AN[var1][1] + this.AD / 2, this.AN[var1 + 1][0] + this.AD / 2, this.AN[var1 + 1][1] + this.AD / 2);
                  }
               }
            }

            if (GameCanvas.gameTick % 15 == 5) {
               for(var1 = 1; var1 < this.AN.length; ++var1) {
                  if (var1 == this.AN.length - 1) {
                     this.AA((short)74, var1 - 1, this.AN[var1][0] + this.AD / 2, this.AN[var1][1] + this.AD / 2, this.AN[1][0] + this.AD / 2, this.AN[1][1] + this.AD / 2);
                  } else {
                     this.AA((short)74, var1 - 1, this.AN[var1][0] + this.AD / 2, this.AN[var1][1] + this.AD / 2, this.AN[var1 + 1][0] + this.AD / 2, this.AN[var1 + 1][1] + this.AD / 2);
                  }
               }
            }

         } else {
            if (GameCanvas.gameTick % 15 == 0 || GameCanvas.gameTick % 15 == 5) {
               this.AA((short)154, 0, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
            }

         }
      }
   }

   public final void AA(short var1, int var2, int var3, int var4, int var5, int var6) {
      Effect_End var7 = new Effect_End(var1, (byte)var2, var3, var4, var5, var6, (byte)0, (MainObject)null);
      this.vecEff.addElement(var7);
   }

   public final void AP() {
      if (this.Step != 1 && this.Step != 3) {
         if (this.Step == 2 || this.Step == 4) {
            ++this.BD;
            if (this.BD >= 20) {
               this.Step = 0;
               this.BD = 0;
               this.CK = 0;
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(this.showServer);
               if (this.BG == 8 || this.BG == 13) {
                  mItemUpgrade[0] = null;
                  mItemUpgrade[1] = null;
                  return;
               }

               if (this.BG == 9) {
                  this.AK();
                  if (mItemUpgrade[0] != null) {
                     GlobalService.getInstance().AB((byte)9, mItemUpgrade[0].ID, mItemUpgrade[0].typeObject, (short)1);
                  }

                  mItemUpgrade[0] = null;
                  mItemUpgrade[1] = null;
                  return;
               }

               if (mItemUpgrade[0] != null) {
                  MainItem var6 = MainItem.getItemVec(mItemUpgrade[0].typeObject, mItemUpgrade[0].ID, this.BN);
                  if (this.BG == 10 || this.BG == 11) {
                     if (var6 != null && this.Step == 4) {
                        GlobalService.getInstance().AB(this.BG, (byte)1, mItemUpgrade[0].ID, mItemUpgrade[0].typeObject, (short)1);
                        return;
                     } else {
                        mItemUpgrade[0] = null;
                        return;
                     }
                  }

                  if (var6 != null && var6.mDaKham != null && var6.numLoKham > var6.mDaKham.length) {
                     GlobalService.getInstance().AB(this.BG, (byte)1, mItemUpgrade[0].ID, mItemUpgrade[0].typeObject, (short)1);
                     return;
                  }

                  mItemUpgrade[0] = null;
               }
            }
         }
      } else {
         ++this.BD;
         float var10000;
         if (this.CK == 0) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
            var10000 = mSound.volumeSound;
            mSound.playSound();
            int var10003 = this.AN[0][0] + this.AD / 2;
            int var10004 = this.AN[0][1] + this.AD / 2;
            boolean var5 = true;
            int var3 = var10004;
            int var2 = var10003;
            var5 = false;
            var5 = true;
            Effect_End var4 = new Effect_End((short)30, (byte)0, var2, var3, 1000, (byte)0, (MainObject)null);
            this.vecEff.addElement(var4);
            this.AA((short)75, 0, this.AN[1][0] + this.AD / 2, this.AN[1][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
            mItemUpgrade[1] = null;
            this.CK = this.BD + 11 + ((this.AB - this.AG * 3 - this.AK) / 6 << 2) / 5;
            return;
         }

         if (this.BD >= this.CK) {
            if (this.BG != 8 && this.BG != 9 && this.BG != 13) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
               this.AA((short)53, 0, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
            } else {
               byte var1;
               if (this.Step == 1) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  var1 = 0;
                  if (GameCanvas.language == 1) {
                     var1 = 2;
                  }

                  this.AA((short)79, var1, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
                  this.AA((short)76, 0, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
                  this.AA((short)53, 0, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
               } else if (this.Step == 3) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  var1 = 1;
                  if (GameCanvas.language == 1) {
                     var1 = 3;
                  }

                  this.AA((short)79, var1, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
                  this.AA((short)77, 0, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2, this.AN[0][0] + this.AD / 2, this.AN[0][1] + this.AD / 2);
               }
            }

            this.Step = 2;
            this.BD = 0;
            this.CK = 0;
            return;
         }
      }

   }

   public final void AQ() {
      super.updatePointer();
   }

   public static void setInfo_money(int valueTile, int valueMonney_1, int valueMonney_2, int valueMonney_3) {
      ScreenUpgrade.valueTile = valueTile;
      ScreenUpgrade.valueMonney_1 = valueMonney_1;
      ScreenUpgrade.valueMonney_2 = valueMonney_2;
      ScreenUpgrade.valueMonney_3 = valueMonney_3;
   }

   public final void setInfo(MainItem[] listItem) {
      mItemUpgrade = listItem;
      this.indexStep = 0;
   }

   public static void stopUpgradeItemAuto() {
      isClickUpgradeItemToSucess = false;
      isContinueUpgradeItem = false;
      beriOrRuby = -1;
   }

   public static void stopUpgradeSuperItemAuto() {
      isClickUpgradeSuperItemToSucess = false;
      isContinueUpgradeSuperItem = false;
      beriOrRuby = -1;
      mItemUpgradeSaveTemp = new MainItem[6];
   }

   private static boolean AA(short var0, int var1) {
      for(int var2 = 0; var2 < Player.vecInventory.size(); ++var2) {
         MainItem var3;
         if ((var3 = (MainItem)Player.vecInventory.elementAt(var2)) != null && var3.ID == var0 && var3.typeObject == 7 && var3.numPotion >= var1) {
            return true;
         }
      }

      return false;
   }
   private int calcAutoUpgradeDelay() {
        int base = 0;
        int computed = (int)(base / (1.0 + (double)GameCanvas.percentUPCV / 100.0));
        if (computed < AUTO_UPGRADE_MIN_DELAY_MS) {
            computed = AUTO_UPGRADE_MIN_DELAY_MS;
        }
        computed += CRes.random(AUTO_UPGRADE_JITTER_MS);
        return computed;
    }
}
