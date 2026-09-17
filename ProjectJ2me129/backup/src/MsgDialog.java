public class MsgDialog extends MainDialog {
   public int AD;
   public mVector cmdList = new mVector();
   private static mVector AA = new mVector("MsgDialog.vecEff");
   public mVector vecEffUni = new mVector("MsgShowGift.vecEffUni");
   public iCommand AG;
   public iCommand AH;
   public mFont fontDia;
   public static FrameImage fraImgWaiting;
   public static FrameImage fraAutoMpHp;
   private int AB;
   public int wItem;
   public int idSelect;
   public static int hPlus;
   private MainQuest AC;
   public boolean isClose;
   private boolean BB;
   public ListNew AP;
   public String nameDialog;
   private static int BC;
   private static int BD;
   private String BE;
   private int BF;
   private int BG;
   private CountDownTicket BH;
   private mVector BI;
   public static boolean isAuroReconect = false;
   private MainClan BJ;
   private String BK;
   public int AS;
   public int AT;
   private int BL;
   private Skill_Info BM;
   private int[] BN;
   private int BO;
   private int BP;
   public int AU;
   private int BQ;
   private int BR;

   public MsgDialog() {
      this.fontDia = mFont.tahoma_7_black;
      this.AB = 0;
      this.isClose = false;
      this.BB = false;
      this.AP = new ListNew();
      this.nameDialog = "";
      this.BE = "";
      this.BF = 0;
      this.BG = 0;
      this.BK = "";
      this.AT = 160;
      this.BL = 6;
      this.AU = 5;
      this.BQ = 0;
      this.BR = 0;
   }

   public void commandPointer(int var1, int var2) {
      System.out.println("vào cmd Pointer trong MsgDialog:" + var1);
      GameCanvas.clearAll();
      switch(var1) {
      case -1:
         GameCanvas.end_Dialog();
         return;
      case 0:
         if (this.BK.length() > 0) {
            GameMidlet.AB(this.BK);
            return;
         }
         break;
      case 1:
         if (this.AC == null) {
            return;
         }

         GlobalService.getInstance().AC((byte)2, (short)this.AC.AE);
         GameCanvas.end_Dialog();
         return;
      case 2:
         this.isClose = true;
         if (super.type == 2 && GameScreen.CV == 18) {
            MainHelp.AA(false);
            return;
         }
      case 3:
      case 4:
      case 6:
      case 7:
      case 8:
      case 9:
      case 12:
      case 13:
      default:
         break;
      case 5:
         if (this.BM != null) {
            GlobalService.getInstance().AE((byte)0, (short)this.BM.indexSkillInServer);
            GameCanvas.end_Dialog();
            return;
         }
         break;
      case 10:
         GameCanvas.AK();
         if (Player.QW != null && Player.QU == Player.QW.AC) {
            GameCanvas.menuCur.isShowMenu = false;
            GameCanvas.menuCur.AL = null;
            Player.QW.AB((short)Player.QW.AC);
            Player.QU = -1;
            return;
         }
         break;
      case 11:
         if (MsgShowGift.AA != null) {
            GlobalService.getInstance().Use_Potion(MsgShowGift.AA.ID);
         }

         GameCanvas.end_Dialog();
         return;
      case 14:
         GlobalService.getInstance().AP((byte)var2);
         return;
      case 15:
         GameCanvas.end_Dialog();
         Class_AT.AK();
      }

   }

   public final void beginDia() {
      super.DA = null;
      super.DB = null;
      super.center = null;
      this.BB = false;
      this.cmdList.removeAllElements();
      AA.removeAllElements();
      if (GameScreen.player != null) {
         GameScreen.player.resetAction();
      }

   }

   public final void AA(String var1, String var2) {
      this.BK = var2;
      mVector var3;
      (var3 = new mVector()).addElement(new iCommand(T.AT, 0, this));
      this.AA(var1, var3, true, (byte)0);
   }

   public final void AA(String var1, mVector var2, boolean var3, byte var4) {
      this.beginDia();
      super.type = var4;
      if (var4 == 9 && Player.SO != 6) {
         isAuroReconect = true;
      }

      if (var2 != null) {
         this.cmdList = var2;
      } else {
         this.cmdList = new mVector();
      }

      if (var3) {
         this.AG = new iCommand(T.close, 2, this);
         this.cmdList.addElement(this.AG);
         super.backCMD = this.AG;
      }

      if (this.cmdList.size() == 0) {
         GameCanvas.end_Dialog();
      } else {
         super.wDia = MotherCanvas.w - 30;
         if (super.wDia > 200) {
            super.wDia = 200;
         }

         this.AT = super.wDia;
         if (GameCanvas.currentDialog == null) {
            this.AS = 5;
         } else {
            this.AS = this.AT;
         }

         int var5 = this.cmdList.size();
         super.strinfo = this.fontDia.splitFontArray(var1, super.wDia - 20);
         super.hDia = GameCanvas.hText * super.strinfo.length + hPlus + ((var5 - 1) / 2 + 1) * (iCommand.hButtonCmdNor + 5);
         super.AX = MotherCanvas.hw - super.wDia / 2;
         super.AY = MotherCanvas.h - (GameCanvas.hCommand << 1) - super.hDia - 5;
         this.setPosCmdNew(0, false);
      }
   }

   public final void AA(String var1, mVector var2, mVector var3, boolean var4) {
      this.beginDia();
      super.type = 7;
      this.BB = true;
      this.BI = var2;
      this.cmdList = new mVector();
      this.AG = new iCommand(T.close, 2, this);
      this.cmdList.addElement(this.AG);
      super.backCMD = this.AG;
      if (this.cmdList.size() == 0) {
         GameCanvas.end_Dialog();
      } else {
         this.wItem = 22;
         super.wDia = MotherCanvas.w - 30;
         if (super.wDia > 180) {
            super.wDia = 180;
         }

         this.AT = super.wDia;
         if (GameCanvas.currentDialog == null) {
            this.AS = 5;
         } else {
            this.AS = this.AT;
         }

         int var5 = this.cmdList.size();
         super.strinfo = this.fontDia.splitFontArray(var1, super.wDia - 20);
         super.hDia = GameCanvas.hText * super.strinfo.length + (this.BI.size() - 1) * this.wItem + ((var5 - 1) / 2 + 1) * (iCommand.hButtonCmdNor + 5) + 5;
         super.AX = MotherCanvas.hw - super.wDia / 2;
         super.AY = MotherCanvas.h - (GameCanvas.hCommand << 1) - super.hDia - 5;
         this.setPosCmdNew(0, false);
      }
   }

   public final void AA(String var1, mVector var2, boolean var3, String var4) {
      if (!GameCanvas.isTouch) {
         this.AA(var1, var2, var3, (byte)0);
      } else {
         this.beginDia();
         this.BB = true;
         this.nameDialog = var4;
         super.type = 0;
         if (var2 != null) {
            this.cmdList = var2;
         } else {
            this.cmdList = new mVector();
         }

         super.wDia = MotherCanvas.w - 30;
         if (super.wDia > 200) {
            super.wDia = 200;
         }

         this.AT = super.wDia;
         if (GameCanvas.currentDialog == null) {
            this.AS = 5;
         } else {
            this.AS = this.AT;
         }

         int var8 = this.cmdList.size();
         super.strinfo = this.fontDia.splitFontArray(var1, super.wDia - 20);
         super.hDia = GameCanvas.hText * super.strinfo.length + hPlus + iCommand.hButtonCmdNor + 5;
         super.AX = MotherCanvas.hw - super.wDia / 2;
         super.AY = MotherCanvas.h - (GameCanvas.hCommand << 1) - super.hDia - 5;
         int var7 = iCommand.wButtonCmd;
         int var9 = 0;
         if (var8 % 2 == 0) {
            var9 = var7 / 2;
         }

         for(int var5 = 0; var5 < var8; ++var5) {
            iCommand var6;
            (var6 = (iCommand)this.cmdList.elementAt(var5)).AG = false;
            var6.setPos(MotherCanvas.hw - this.cmdList.size() / 2 * var7 + var5 * var7 + var9, super.AY + super.hDia - GameCanvas.hCommand, (FrameImage)null, var6.caption);
            if (var5 == 0 && GameCanvas.isKeyPressed()) {
               var6.AG = true;
            }
         }

         if (var3) {
            this.AG = new iCommand(T.close, 2, this);
            this.AG.setPos(MotherCanvas.hw + (super.wDia - 50) / 2, super.AY - GameCanvas.hCommand / 2 + 7, MainTab.fraCloseTab, "");
            this.cmdList.addElement(this.AG);
            super.backCMD = this.AG;
         }
      }

      BC = super.AX + super.wDia / 2;
      BD = super.AY + super.hDia - GameCanvas.hCommand * 3;
   }

   public final void AA(String var1, mVector var2, boolean var3, byte var4, int var5) {
      this.beginDia();
      super.type = var4;
      if (var2 != null) {
         this.cmdList = var2;
      } else {
         this.cmdList = new mVector();
      }

      if (var3) {
         this.AG = new iCommand(T.close, 2, this);
         this.cmdList.addElement(this.AG);
         super.backCMD = this.AG;
      }

      super.wDia = MotherCanvas.w - 30;
      if (super.wDia > 200) {
         super.wDia = 200;
      }

      this.AT = super.wDia;
      this.AS = this.AT;
      super.strinfo = this.fontDia.splitFontArray(var1, super.wDia - 20);
      super.hDia = GameCanvas.hText * super.strinfo.length + (var3 ? iCommand.hButtonCmdNor : 0) + 28 + hPlus;
      if (var5 > 0) {
         super.hDia += 24;
         this.BH = new CountDownTicket();
         this.BH.setCountDown(var5);
      }

      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.h - (GameCanvas.hCommand << 1) - super.hDia - 5;
      int var6 = this.cmdList.size();
      int var7 = iCommand.wButtonCmd;
      int var8 = 0;
      if (var6 % 2 == 0) {
         var8 = var7 / 2;
      }

      for(int var9 = 0; var9 < var6; ++var9) {
         iCommand var10;
         (var10 = (iCommand)this.cmdList.elementAt(var9)).AG = false;
         var10.setPos(MotherCanvas.hw - this.cmdList.size() / 2 * var7 + var9 * var7 + var8, super.AY + super.hDia - GameCanvas.hCommand, (FrameImage)null, var10.caption);
         if (var9 == 0 && GameCanvas.isKeyPressed()) {
            var10.AG = true;
         }
      }

      this.setPosCmdNew(0, false);
   }

   public final void AA(MainQuest var1, boolean var2) {
      this.AC = var1;
      this.fontDia = mFont.tahoma_7_black;
      this.beginDia();
      super.type = 2;
      this.cmdList = new mVector();
      this.AG = new iCommand(T.close, 2, this);
      this.AG.AH = 1;
      if (var1.AA != 0 && var1.AB != 0 && !var2) {
         iCommand var7;
         (var7 = new iCommand(T.IW, 1, this)).AH = 1;
         this.cmdList.addElement(var7);
      }

      this.cmdList.addElement(this.AG);
      super.backCMD = this.AG;
      super.wDia = MotherCanvas.w - 30;
      if (super.wDia > 160) {
         super.wDia = 160;
      }

      this.AT = super.wDia;
      if (GameCanvas.currentDialog == null) {
         this.AS = 5;
      } else {
         this.AS = this.AT;
      }

      int var8 = this.cmdList.size();
      this.nameDialog = T.FE;
      String var3;
      if (var1.AB == 0) {
         this.nameDialog = T.FC + (var1.AA == 0 ? var1.AA() : "");
         var3 = GameMidlet.AA(T.FD, var1.AL);
      } else {
         boolean var4;
         int var5;
         Class_CP var6;
         if (var1.AB == 2) {
            var3 = var1.AH + "\n" + var1.AK;
            var4 = false;

            for(var5 = 0; var5 < var1.AM.size(); ++var5) {
               if ((var6 = (Class_CP)var1.AM.elementAt(var5)).AA == 2 || var6.AA == 1) {
                  var3 = var3 + "\n " + T.EI;
                  var4 = true;
                  break;
               }
            }

            if (var4) {
               var3 = var3 + "\n " + T.EK + var1.AL;
            }
         } else {
            var3 = var1.AH + "\n" + var1.AK;
            var4 = false;

            for(var5 = 0; var5 < var1.AM.size(); ++var5) {
               if ((var6 = (Class_CP)var1.AM.elementAt(var5)).AA == 2 || var6.AA == 1) {
                  if (!var4) {
                     if (var1.AA != 2 && var1.AA != 3) {
                        var3 = var3 + "(" + LoadMap.AB(var1.AF) + ")";
                     }

                     var3 = var3 + "\n " + T.EH;
                     var4 = true;
                  }

                  if (var6.AA == 2) {
                     var3 = var3 + "\n  " + var6.AE + ": " + var6.AD + "/" + var6.AC;
                  }

                  if (var6.AA == 1) {
                     if (var6.AE != null && var6.AE.length() == 0) {
                        var6.AA();
                     }

                     var3 = var3 + "\n  " + var6.AE + ": " + var6.AD + "/" + var6.AC;
                  }
               }
            }
         }
      }

      super.strinfo = this.fontDia.splitFontArray(var3, super.wDia - 20);
      super.hDia = GameCanvas.hText * super.strinfo.length + hPlus + ((var8 - 1) / 2 + 1) * (iCommand.hButtonCmdNor + 5);
      super.hDia += GameCanvas.hCommand;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.h / 2 - super.hDia / 2;
      this.setPosCmdNew(0, true);
   }

   public final void AA(Skill_Info var1) {
      this.BM = var1;
      this.fontDia = mFont.tahoma_7_black;
      this.beginDia();
      super.type = 4;
      this.cmdList = new mVector();
      iCommand var2;
      (var2 = new iCommand(T.EP, 5, this)).AH = 1;
      this.cmdList.addElement(var2);
      this.AG = new iCommand(T.close, -1, this);
      this.AG.AH = 1;
      this.cmdList.addElement(this.AG);
      super.backCMD = this.AG;
      super.wDia = MotherCanvas.w - 30;
      if (super.wDia > 160) {
         super.wDia = 160;
      }

      this.AT = super.wDia;
      int var4 = this.cmdList.size();
      var1.setVecInfo(super.wDia);
      super.hDia = GameCanvas.hText * var1.vecInfo.size() + hPlus + ((var4 - 1) / 2 + 1) * (iCommand.hButtonCmdNor + 5);
      super.hDia += GameCanvas.hCommand;
      int var3 = 0;
      if (super.hDia > MotherCanvas.h - (GameCanvas.hCommand << 1)) {
         var3 = super.hDia - (MotherCanvas.h - (GameCanvas.hCommand << 1));
         super.hDia = MotherCanvas.h - (GameCanvas.hCommand << 1);
      }

      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.hh - super.hDia / 2;
      if (var3 > 0) {
         this.AP = new ListNew(super.AX, super.AY, super.wDia, super.hDia, 0, 0, var3, true);
      }

      this.AS = this.AT;
      this.setPosCmdNew(0, false);
      super.DF = this.AG;
   }

   public final void AA(mVector var1) {
      if (var1 != null && var1.size() != 0) {
         this.beginDia();
         this.fontDia = mFont.tahoma_7_white;
         this.nameDialog = T.KZ;
         super.type = 5;
         this.AG = new iCommand(T.close, 2, this);
         this.cmdList.addElement(this.AG);
         super.backCMD = this.AG;
         super.wDia = MotherCanvas.w - 30;
         if (super.wDia > 160) {
            super.wDia = 160;
         }

         this.AT = super.wDia;
         if (GameCanvas.currentDialog == null) {
            this.AS = 5;
         } else {
            this.AS = this.AT;
         }

         int var2 = this.cmdList.size();
         super.strinfo = new String[var1.size()];

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            MainInfoItem var4 = (MainInfoItem)var1.elementAt(var3);
            super.strinfo[var3] = MainItem.AA(var4);
         }

         super.hDia = GameCanvas.hText * super.strinfo.length + hPlus + ((var2 - 1) / 2 + 1) * (iCommand.hButtonCmdNor + 15);
         if (GameCanvas.isTouch) {
            super.hDia = GameCanvas.hText * super.strinfo.length + hPlus + 20;
         }

         super.AX = MotherCanvas.hw - super.wDia / 2;
         super.AY = MotherCanvas.hh - super.hDia / 2;
         if (GameCanvas.isTouch) {
            this.AG.setPos(super.AX + super.wDia - 25, super.AY + 4 + 8, MainTab.fraCloseTab, "");
         } else {
            this.setPosCmdNew(0, false);
         }
      }
   }

   public final void AA(MainClan var1) {
      this.BJ = var1;
      this.BN = new int[T.WI.length];
      this.wItem = GameCanvas.hText + 4;

      for(int var2 = 0; var2 < T.WI.length; ++var2) {
         this.BN[var2] = mFont.tahoma_7_white.getWidth(T.WI[var2]);
      }

      this.beginDia();
      this.BB = true;
      this.nameDialog = T.Clan + " " + this.BJ.name;
      super.type = 10;
      this.cmdList = new mVector();
      super.wDia = MotherCanvas.w - 30;
      if (super.wDia > 180) {
         super.wDia = 180;
      }

      this.AT = super.wDia;
      if (GameCanvas.currentDialog == null) {
         this.AS = 5;
      } else {
         this.AS = this.AT;
      }

      super.hDia = 120;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.h / 2 - super.hDia / 2 - 5;
      this.AG = new iCommand(T.close, 2, this);
      if (GameCanvas.isTouch) {
         this.AG.setPos(MotherCanvas.hw + (super.wDia - 50) / 2, super.AY + 4 + 7, MainTab.fraCloseTab, "");
      } else {
         this.AG = AvMain.AA(this.AG, 2);
      }

      this.cmdList.addElement(this.AG);
      super.backCMD = this.AG;
      BC = super.AX + super.wDia / 2;
      BD = super.AY + super.hDia - GameCanvas.hCommand * 3;
   }

   public final void setPosCmdNew(int var1, boolean var2) {
      this.AD = 0;
      if (this.cmdList.size() > 0) {
         int var3;
         if ((var3 = this.cmdList.size()) == 1) {
            this.BO = super.AX + super.wDia / 2;
            this.BP = 0;
         } else {
            boolean var10001 = true;
            this.BP = 10;
            this.BO = super.AX + super.wDia / 2 - this.BP / 2 - iCommand.wButtonCmd / 2;
         }

         for(int var4 = 0; var4 < var3; ++var4) {
            iCommand var5;
            (var5 = (iCommand)this.cmdList.elementAt(var4)).AG = false;
            if (var3 == 3 && var4 == 2) {
               var5.setPos(super.AX + super.wDia / 2, super.AY + super.hDia - iCommand.hButtonCmdNor - (var3 - 1) / 2 * (iCommand.hButtonCmdNor + 5) + iCommand.hButtonCmdNor / 2 + 2 + var4 / 2 * (iCommand.hButtonCmdNor + 5) + var1 - 8, (FrameImage)null, var5.caption);
            } else {
               var5.setPos(this.BO + var4 % 2 * (iCommand.wButtonCmd + this.BP), super.AY + super.hDia - iCommand.hButtonCmdNor / 2 - (var3 - 1) / 2 * (iCommand.hButtonCmdNor + 5) + 2 + var4 / 2 * (iCommand.hButtonCmdNor + 5) + var1 - 8, (FrameImage)null, var5.caption);
            }

            if (var2) {
               if (var4 == var3 - 1 && GameCanvas.isKeyPressed()) {
                  var5.AG = true;
                  this.AD = var4;
               }
            } else if (var4 == 0 && GameCanvas.isKeyPressed()) {
               var5.AG = true;
               this.AD = 0;
            }
         }
      }

   }

   public void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      int var2 = 0;
      int var3;
      int var4;
      iCommand var14;
      int var10000;
      int var10002;
      int var10003;
      int var10005;
      int var10006;
      switch(super.type) {
      case 0:
      case 9:
         if (this.BB) {
            var2 = GameCanvas.hCommand;
         }

         var10002 = MotherCanvas.hw - this.AS / 2;
         var10003 = super.AY - this.AU - var2;
         var10005 = super.hDia + (this.AU << 1) + var2;
         var10006 = this.AT;
         this.AD(var1, var10002, var10003, this.AS, var10005, 0);
         var1.AD(MotherCanvas.hw - this.AS / 2, 0, this.AS, MotherCanvas.h);
         mGraphics.AC();
         var10000 = MotherCanvas.hw;
         var10000 = this.AS;
         var10000 = this.AS;
         var10000 = MotherCanvas.h;
         mGraphics.AD();
         if (this.BB) {
            var1.setColor(-805042);
            var1.fillRoundRectNew(super.AX + 25, super.AY - var2 + 12, super.wDia - 50, 16, 4, 4);
            AvMain.FontBorderColor(var1, this.nameDialog, super.AX + super.wDia / 2, super.AY + 15 - var2, 2, (int)6, (int)5);
         }

         for(var3 = 0; var3 < super.strinfo.length; ++var3) {
            this.fontDia.drawString(var1, super.strinfo[var3], MotherCanvas.w / 2, super.AY + 10 + var3 * GameCanvas.hText, 2);
         }

         for(var3 = 0; var3 < AA.size(); ++var3) {
            ((MainEffect)AA.elementAt(var3)).paint(var1);
         }

         if (this.cmdList != null) {
            for(var3 = 0; var3 < this.cmdList.size(); ++var3) {
               (var14 = (iCommand)this.cmdList.elementAt(var3)).paint(var1, var14.xCmd, var14.yCmd);
            }
         }

         mGraphics.restoreCanvas();
         return;
      case 1:
      case 6:
      case 8:
         var10002 = MotherCanvas.hw - this.AS / 2;
         var10003 = super.AY - this.AU;
         var10005 = super.hDia + (this.AU << 1);
         var10006 = this.AT;
         this.AD(var1, var10002, var10003, this.AS, var10005, 0);
         var1.AD(MotherCanvas.hw - this.AS / 2, 0, this.AS, MotherCanvas.h);
         mGraphics.AC();
         var10000 = MotherCanvas.hw;
         var10000 = this.AS;
         var10000 = this.AS;
         var10000 = MotherCanvas.h;
         mGraphics.AD();

         for(var3 = 0; var3 < super.strinfo.length; ++var3) {
            this.fontDia.drawString(var1, super.strinfo[var3], MotherCanvas.w / 2, super.AY + 10 + var3 * GameCanvas.hText, 2);
         }

         if (super.type != 1 && super.type != 8) {
            if (super.type == 6 && this.BH != null) {
               this.BH.paintCountDownTicket(var1, mFont.tahoma_7b_black, MotherCanvas.hw, super.AY + 12 + super.strinfo.length * GameCanvas.hText, 2);
            }
         } else {
            fraImgWaiting.drawFrame(this.AB / 2 % fraImgWaiting.nFrame, MotherCanvas.hw, super.AY + 22 + super.strinfo.length * GameCanvas.hText, 0, 3, var1);
         }

         if (this.cmdList != null) {
            for(var3 = 0; var3 < this.cmdList.size(); ++var3) {
               (var14 = (iCommand)this.cmdList.elementAt(var3)).paint(var1, var14.xCmd, var14.yCmd);
            }
         }

         mGraphics.restoreCanvas();
         return;
      case 2:
         var10002 = MotherCanvas.hw - this.AS / 2;
         var10006 = this.AT;
         this.AD(var1, var10002, super.AY, this.AS, super.hDia, 0);
         GameCanvas.resetTrans(var1);
         var1.AD(MotherCanvas.hw - this.AS / 2, 0, this.AS, MotherCanvas.h);
         mGraphics.AC();
         var10000 = MotherCanvas.hw;
         var10000 = this.AS;
         var10000 = this.AS;
         var10000 = MotherCanvas.h;
         mGraphics.AD();
         var1.setColor(-805042);
         var1.fillRoundRectNew(super.AX + 10, super.AY + 12, super.wDia - 20, 16, 4, 4);
         if (this.AC != null) {
            AvMain.FontBorderColor(var1, this.nameDialog, super.AX + super.wDia / 2, super.AY + 15, 2, (int)6, (int)5);
         }

         for(var3 = 0; var3 < super.strinfo.length; ++var3) {
            if (var3 == 0 && this.AC.AB != 0) {
               mFont.tahoma_7b_black.drawString(var1, super.strinfo[var3], MotherCanvas.w / 2, super.AY + GameCanvas.hCommand + 10 + var3 * GameCanvas.hText, 2);
            } else {
               this.fontDia.drawString(var1, super.strinfo[var3], super.AX + 10, super.AY + GameCanvas.hCommand + 10 + var3 * GameCanvas.hText, 0);
            }
         }

         if (this.cmdList != null) {
            for(var3 = 0; var3 < this.cmdList.size(); ++var3) {
               (var14 = (iCommand)this.cmdList.elementAt(var3)).paint(var1, var14.xCmd, var14.yCmd);
            }
         }

         mGraphics.restoreCanvas();
         return;
      case 3:
      default:
         break;
      case 4:
         var10002 = MotherCanvas.hw - this.AS / 2;
         var10006 = this.AT;
         this.AD(var1, var10002, super.AY, this.AS, super.hDia, 0);
         var1.setColor(-2573687);
         var1.fillRoundRectNew(super.AX + 10, super.AY + 12, super.wDia - 20, 16, 4, 4);
         if (this.BM != null) {
            var10003 = super.AX + super.wDia / 2;
            int var10004 = super.AY + 15;
            boolean var11 = true;
            var11 = true;
            var11 = true;
            int var17 = var10004;
            int var13 = var10003;
            String var15 = this.BM.name;
            short var16 = this.BM.idIcon;
            int var7 = 0;
            MainImage var18;
            if ((var18 = Skill_Info.getImage(var16)) != null && var18.img != null) {
               var7 = mImage.getImageWidth(var18.img.image);
               int var8 = mFont.tahoma_7b_black.getWidth(var15);
               var7 = var7 / 2 + 1;
               var1.drawRegion((mImage)var18.img, var13 - var8 / 2 - 2, var17 + 5, 3);
            }

            AvMain.FontBorderColor(var1, var15, var13 + var7, var17, 2, (int)3, (int)5);
            var1.AD(super.AX, super.AY + GameCanvas.hCommand + 10, super.wDia, super.hDia - (GameCanvas.hCommand + iCommand.hButtonCmdNor + 20));
            mGraphics.AC();
            var10000 = super.AX;
            var10000 = super.AY;
            var10000 = GameCanvas.hCommand;
            var10000 = super.wDia;
            var10000 = super.hDia;
            var10000 = GameCanvas.hCommand;
            var10000 = iCommand.hButtonCmdNor;
            mGraphics.AD();
            if (this.AP != null) {
               var1.translate(0, -this.AP.AC);
            }

            for(var3 = 0; var3 < this.BM.vecInfo.size(); ++var3) {
               infoShow var19 = (infoShow)this.BM.vecInfo.elementAt(var3);
               this.fontDia.drawString(var1, var19.AA, super.AX + 12, super.AY + GameCanvas.hCommand + 10 + var3 * GameCanvas.hText, 0);
            }

            mGraphics.AE();
            mGraphics.restoreCanvas();
         }

         GameCanvas.resetTrans(var1);
         if (this.cmdList != null) {
            for(var3 = 0; var3 < this.cmdList.size(); ++var3) {
               (var14 = (iCommand)this.cmdList.elementAt(var3)).paint(var1, var14.xCmd, var14.yCmd);
            }

            return;
         }
         break;
      case 5:
         AvMain.paintRect(var1, MotherCanvas.hw - this.AS / 2, super.AY - this.AU, this.AS, super.hDia + (this.AU << 1) - 5, (byte)1, 4);
         var1.AD(MotherCanvas.hw - this.AS / 2, 0, this.AS, MotherCanvas.h);
         mGraphics.AC();
         var10000 = MotherCanvas.hw;
         var10000 = this.AS;
         var10000 = this.AS;
         var10000 = MotherCanvas.h;
         mGraphics.AD();
         var1.setColor(-805042);
         var1.fillRoundRectNew(super.AX + 25, super.AY + 4, super.wDia - 50, 16, 4, 4);
         AvMain.FontBorderColor(var1, this.nameDialog, super.AX + super.wDia / 2, super.AY + 7, 2, (int)6, (int)5);

         for(var3 = 0; var3 < super.strinfo.length; ++var3) {
            this.fontDia.drawString(var1, super.strinfo[var3], super.AX + 4, super.AY + 27 + var3 * GameCanvas.hText, 0);
         }

         if (this.cmdList != null) {
            for(var3 = 0; var3 < this.cmdList.size(); ++var3) {
               (var14 = (iCommand)this.cmdList.elementAt(var3)).paint(var1, var14.xCmd, var14.yCmd);
            }
         }

         mGraphics.restoreCanvas();
         return;
      case 7:
         if (this.BB) {
            var2 = GameCanvas.hCommand;
         }

         var10002 = MotherCanvas.hw - this.AS / 2;
         var10003 = super.AY - this.AU - var2;
         var10005 = super.hDia + (this.AU << 1) + var2;
         var10006 = this.AT;
         this.AD(var1, var10002, var10003, this.AS, var10005, 0);
         var1.AD(MotherCanvas.hw - this.AS / 2, 0, this.AS, MotherCanvas.h);
         mGraphics.AC();
         var10000 = MotherCanvas.hw;
         var10000 = this.AS;
         var10000 = this.AS;
         var10000 = MotherCanvas.h;
         mGraphics.AD();
         if (this.BI != null) {
            if (this.BB) {
               var1.setColor(-805042);
               var1.fillRoundRectNew(super.AX + 25, super.AY - var2 + 10, super.wDia - 50, 20, 4, 4);
               MainItem var9 = (MainItem)this.BI.elementAt(0);
               var4 = mFont.tahoma_7b_black.getWidth(var9.name);
               var9.AA(var1, super.AX + super.wDia / 2 - var4 / 2, super.AY + 9 + this.wItem / 2 - var2, this.wItem, 0, (byte)0);
               AvMain.FontBorderColor(var1, var9.name, super.AX + super.wDia / 2 + this.wItem / 2, super.AY + 15 - var2, 2, (int)6, (int)5);
            }

            var3 = super.AY + 10;

            for(var4 = 0; var4 < super.strinfo.length; ++var4) {
               this.fontDia.drawString(var1, super.strinfo[var4], MotherCanvas.w / 2, var3, 2);
               var3 += GameCanvas.hText;
            }

            for(var4 = 1; var4 < this.BI.size(); ++var4) {
               MainItem var10;
               (var10 = (MainItem)this.BI.elementAt(var4)).AA(var1, super.AX + 10 + this.wItem / 2, var3 + 5 + (var4 - 1) * this.wItem, this.wItem, 0, (byte)0);
               mFont.tahoma_7b_black.drawString(var1, var10.name, super.AX + 11 + this.wItem, var3 + (var4 - 1) * this.wItem, 0);
            }
         }

         if (this.cmdList != null) {
            for(var3 = 0; var3 < this.cmdList.size(); ++var3) {
               (var14 = (iCommand)this.cmdList.elementAt(var3)).paint(var1, var14.xCmd, var14.yCmd);
            }
         }

         mGraphics.restoreCanvas();
         return;
      case 10:
         var10002 = MotherCanvas.hw - this.AS / 2;
         var10003 = super.AY - this.AU;
         var10005 = super.hDia + (this.AU << 1);
         var10006 = this.AT;
         this.AD(var1, var10002, var10003, this.AS, var10005, 0);
         var1.AD(MotherCanvas.hw - this.AS / 2, 0, this.AS, MotherCanvas.h);
         mGraphics.AC();
         var10000 = MotherCanvas.hw;
         var10000 = this.AS;
         var10000 = this.AS;
         var10000 = MotherCanvas.h;
         mGraphics.AD();
         var1.setColor(-805042);
         var1.fillRoundRectNew(super.AX + 25, super.AY + 4, super.wDia - 50, 16, 4, 4);
         AvMain.FontBorderColor(var1, this.nameDialog, super.AX + super.wDia / 2, super.AY + 7, 2, (int)6, (int)5);
         var3 = super.AY + 10 + GameCanvas.hText;
         var4 = super.AX + 10;

         for(var2 = 1; var2 < 6; ++var2) {
            mFont.tahoma_7_black.drawString(var1, T.WI[var2], var4, var3, 0);
            if (var2 == 1) {
               mFont.tahoma_7b_brown.drawString(var1, this.BJ.AB, var4 + this.BN[var2], var3, 0);
            } else if (var2 == 2) {
               MainImage var5;
               if ((var5 = Potion.AC(this.BJ.idIcon)) != null && var5.img != null) {
                  if (var5.frame == -1) {
                     var5.set_Frame();
                  }

                  if (var5.frame <= 1) {
                     var1.drawRegion((mImage)var5.img, var4 + this.BN[var2] + 14, var3 + 4, 3);
                  } else {
                     byte var6;
                     if (this.BR >= var5.frame - 1) {
                        var6 = 15;
                     } else {
                        var6 = 3;
                     }

                     if (CRes.abs(GameCanvas.gameTick - this.BQ) > var6) {
                        ++this.BR;
                        if (this.BR >= var5.frame) {
                           this.BR = 0;
                        }

                        this.BQ = GameCanvas.gameTick;
                     }

                     var1.drawRegion(var5.img, 0, this.BR * var5.AB, var5.AB, var5.AB, 0, var4 + this.BN[var2] + 14, var3 + 4, 3);
                  }
               }
            } else if (var2 == 3) {
               mFont.tahoma_7b_brown.drawString(var1, String.valueOf(this.BJ.AD), var4 + this.BN[3], var3, 0);
            } else if (var2 == 4) {
               mFont.tahoma_7b_brown.drawString(var1, this.BJ.AE + "/" + this.BJ.AF, var4 + this.BN[4], var3, 0);
            } else if (var2 == 5) {
               mFont.tahoma_7b_brown.drawString(var1, String.valueOf(this.BJ.AS), var4 + this.BN[5], var3, 0);
            }

            var3 += this.wItem;
         }

         if (this.cmdList != null) {
            for(var2 = 0; var2 < this.cmdList.size(); ++var2) {
               iCommand var12;
               (var12 = (iCommand)this.cmdList.elementAt(var2)).paint(var1, var12.xCmd, var12.yCmd);
            }
         }

         mGraphics.restoreCanvas();
      }

   }

   public final void updateAnimation() {
      if (this.AS < this.AT) {
         this.AS += this.BL;
         if (this.AS > this.AT) {
            this.AS = this.AT;
            this.BL = 15;
         }

         if (this.BL < 100) {
            this.BL += 15;
            if (this.BL > 100) {
               this.BL = 100;
            }
         }
      }

   }

   public final void closeDialog() {
      if (this.AS > 0) {
         if (this.AS <= 5) {
            AH();
         }

         this.AS -= this.BL;
         if (this.AS < 5) {
            this.AS = 5;
            this.BL = 15;
         }

         if (this.BL < 100) {
            this.BL += 15;
            if (this.BL > 100) {
               this.BL = 100;
            }
         }
      }

   }

   public void update() {
      if (this.isClose) {
         this.closeDialog();
      } else {
         if (super.type != 1 && super.type != 8) {
            if (super.type == 6) {
               if (this.BH != null) {
                  this.BH.updateTimeCountDownTicket();
                  if (this.BH.timeCountDown <= 0) {
                     AH();
                  }
               } else {
                  AH();
               }
            } else if (super.type == 9) {
               ++this.AB;
               if (this.AB >= 500 && isAuroReconect) {
                  GameScreen.CA.AD();
                  this.AB = 0;
               }
            }
         } else {
            ++this.AB;
            if (super.type == 8) {
               int var10000 = this.AB;
            }

            if (this.AB > 1200) {
               AH();
               if (Session_ME.getInstance().AB() && GameCanvas.currentScreen != GameCanvas.loginScr && GameCanvas.currentScreen != GameCanvas.loadMapScr) {
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.ET);
               } else {
                  GameCanvas.AS();
               }
            }
         }

         if (super.type == 4) {
            if (this.AP != null) {
               this.AP.AC();
            }
         } else {
            this.updateAnimation();
         }

         this.handleKeyPress();
         this.updatePointer();

         for(int var1 = 0; var1 < AA.size(); ++var1) {
            MainEffect var2;
            (var2 = (MainEffect)AA.elementAt(var1)).update();
            if (var2.isStop) {
               AA.removeElement(var2);
               --var1;
            }
         }

      }
   }

   public void handleKeyPress() {
      if (!this.isClose) {
         if (super.type == 4 && this.AP != null) {
            if (GameCanvas.isKeyPressed(1)) {
               GameCanvas.clearKeyPressed(1);
               this.AP.AA(this.AP.AB - GameCanvas.hText);
            } else if (GameCanvas.isKeyPressed(3)) {
               GameCanvas.clearKeyPressed(3);
               this.AP.AA(this.AP.AB + GameCanvas.hText);
            }
         }

         if (this.cmdList != null) {
            int var1 = this.cmdList.size();
            if (GameCanvas.isKeyPressed() && var1 > 0) {
               int var2 = this.AD;
               if (GameCanvas.isKeyPressed(0)) {
                  --this.AD;
                  GameCanvas.clearKeyPressed(0);
               } else if (GameCanvas.isKeyPressed(2)) {
                  ++this.AD;
                  GameCanvas.clearKeyPressed(2);
               }

               if (this.AP == null || this.AP.AD <= 0) {
                  if (GameCanvas.isKeyPressed(1)) {
                     GameCanvas.clearKeyPressed(1);
                     this.AD -= 2;
                  } else if (GameCanvas.isKeyPressed(3)) {
                     GameCanvas.clearKeyPressed(3);
                     this.AD += 2;
                  }
               }

               this.AD = AvMain.AA(this.AD, var1 - 1, false);
               if (var2 != this.AD && GameCanvas.isKeyPressed()) {
                  if (((iCommand)this.cmdList.elementAt(this.AD)).caption.length() == 0) {
                     this.AD = 0;
                  }

                  for(var2 = 0; var2 < var1; ++var2) {
                     iCommand var3 = (iCommand)this.cmdList.elementAt(var2);
                     if (var2 == this.AD) {
                        var3.AG = true;
                     } else {
                        var3.AG = false;
                     }
                  }
               }
            }
         }

         if (GameCanvas.AL[5]) {
            System.out.println("nhấn key 5");
            if (this.cmdList != null && this.AD < this.cmdList.size()) {
               ((iCommand)this.cmdList.elementAt(this.AD)).AD();
               GameCanvas.AB(5);
            }
         }

         super.handleKeyPress();
         this.AS();
      }
   }

   public void updatePointer() {
      if (!this.isClose) {
         if (super.type == 4) {
            this.AP.update_Pos_UP_DOWN();
         }

         if (this.cmdList != null) {
            for(int var1 = 0; var1 < this.cmdList.size(); ++var1) {
               ((iCommand)this.cmdList.elementAt(var1)).AE();
            }
         }

      }
   }

   public static void AH() {
      if (GameCanvas.currentDialog != null) {
         GameCanvas.currentDialog = null;
      } else {
         GameCanvas.subDialog = null;
      }
   }

   public static void AA(MainEffect var0) {
      if (var0 != null) {
         var0.x = BC;
         var0.y = BD;
         AA.addElement(var0);
      }

   }

   public final void setInfoHelp(String var1) {
      this.BE = var1;
      this.BF = mFont.tahoma_7_white.getWidth(var1) + 6;
      this.BG = 100;
   }

   public final void paintInfoHelp(mGraphics var1) {
      if (this.BE.length() > 0) {
         Interface_Game.AA(var1, super.AX + super.wDia / 2, super.AY + super.hDia + 5, this.BF);
         mFont.tahoma_7_white.drawString(var1, this.BE, super.AX + super.wDia / 2, super.AY + super.hDia + 10, 2);
      }

   }

   public final void updateDialog() {
      if (this.BE.length() > 0) {
         --this.BG;
         if (this.BG == 0) {
            this.BE = "";
         }
      }

   }

   public final void AJ() {
      super.handleKeyPress();
   }
}
