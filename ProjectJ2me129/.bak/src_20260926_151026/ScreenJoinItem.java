public final class ScreenJoinItem extends MainScreen {
   private MainItem[] AB;
   private int AC;
   private int[][] AD;
   private InputDialog AE;
   private mVector AF;
   private mVector AG = new mVector("ScreenJoinItem.vecEff");
   private iCommand AH;
   private iCommand AI;
   private iCommand AJ;
   private iCommand AK;
   private int AL;
   private int AM;
   private int AN;
   private int AO;
   private int AP;
   private int AQ;
   private String[] AR;
   private short AS = 1;
   public static ScreenJoinItem instance;
   private int AT = 0;
   private int AU = 0;
   private int AV = 0;

   public final void AA(String var1, MainItem[] var2) {
      if (var2 != null && var2.length != 0) {
         this.AB = var2;
         this.AF = new mVector();
         this.AJ = new iCommand(T.soluong, 3, this);
         this.AF.addElement(this.AJ);
         this.AI = new iCommand(T.join, 1, this);
         this.AF.addElement(this.AI);
         this.AH = new iCommand(T.close, 0, this);
         this.AF.addElement(this.AH);
         this.AK = new iCommand(T.MA, 4, this);
         this.AL = MotherCanvas.w;
         if (this.AL > 210) {
            this.AL = 210;
         }

         if (this.AL < 190) {
            this.AL = 190;
         }

         this.AC = 40;
         this.AM = 28;
         if (this.AL < 210) {
            this.AM = 20;
         }

         this.AR = mFont.tahoma_7_black.splitFontArray(var1, this.AL - 20);
         this.AN = (this.AR.length + 1) * GameCanvas.hText + (var2.length - 1) * this.AC + iCommand.hButtonCmdNor + 40;
         if (GameCanvas.isKeyPressed()) {
            this.AN += iCommand.hButtonCmdNor;
         }

         this.AO = MotherCanvas.hw - this.AL / 2;
         this.AP = MotherCanvas.hh - this.AN / 2 - 5;
         int var5 = this.AO + this.AL / 2;
         int var3 = this.AP + (this.AR.length + 1) * GameCanvas.hText + 15;
         this.AD = mSystem.new_M_Int(var2.length, 2);
         this.AD[0][0] = var5 + this.AL / 4 - this.AM / 2;
         this.AD[0][1] = var3 + (var2.length - 1) * this.AC / 2;

         for(int var4 = 1; var4 < var2.length; ++var4) {
            this.AD[var4][0] = var5 - this.AL / 4 - this.AM / 2;
            this.AD[var4][1] = var3 + (var4 - 1) * this.AC + this.AC / 2;
         }

         this.AQ = 0;
         if (!GameCanvas.isKeyPressed()) {
            this.AJ.setPos(this.AO + this.AL / 2 - iCommand.wButtonCmd / 2 - 10, this.AP + this.AN - iCommand.hButtonCmdNor / 2 - 5, (FrameImage)null, this.AJ.caption);
            this.AI.setPos(this.AO + this.AL / 2 + iCommand.wButtonCmd / 2 + 10, this.AP + this.AN - iCommand.hButtonCmdNor / 2 - 5, (FrameImage)null, this.AI.caption);
            this.AH.setPos(this.AO + this.AL - 10, this.AP + 20, MainTab.fraCloseTab, "");
         } else {
            this.AJ.setPos(this.AO + this.AL / 2 - iCommand.wButtonCmd / 2 - 10, this.AP + this.AN - iCommand.hButtonCmdNor / 2 - 10 - iCommand.hButtonCmdNor, (FrameImage)null, this.AJ.caption);
            this.AJ.AG = true;
            this.AI.setPos(this.AO + this.AL / 2 + iCommand.wButtonCmd / 2 + 10, this.AP + this.AN - iCommand.hButtonCmdNor / 2 - 10 - iCommand.hButtonCmdNor, (FrameImage)null, this.AI.caption);
            this.AH.setPos(this.AO + this.AL / 2, this.AP + this.AN + iCommand.hButtonCmdNor / 2 - 10 - iCommand.hButtonCmdNor, (FrameImage)null, this.AH.caption);
            this.AJ.AG = true;
            super.backCMD = this.AH;
         }

         this.AT = 0;
         this.AS = 1;
      }
   }

   public final void AA(MainItem var1, byte var2) {
      if (this.AB != null && this.AB.length > 0) {
         this.AB[0] = var1;
         this.AB[0].CE = true;

         for(int var3 = 1; var3 < this.AB.length; ++var3) {
            if (var2 == 0) {
               this.AB[var3].numPotion *= this.AB[0].numPotion;
            } else {
               this.AB[var3].numPotion *= this.AS;
            }
         }
      }

      this.AT = 1;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         return;
      case 1:
         if (this.AS <= 0) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LZ);
            return;
         }

         GameCanvas.end_Dialog();
         GameCanvas.AA(T.BP, true);
         GlobalService.getInstance().Split_Item((byte)1, (byte)1, (short)this.AB[0].ID, this.AB[0].typeObject, (short)this.AS);
         return;
      case 2:
         if (this.AB[0] == null) {
            return;
         }

         boolean var4 = false;

         short var5;
         try {
            if ((var5 = (short)Integer.parseInt(this.AE.tfInput.getText())) <= 0) {
               var5 = 1;
            }
         } catch (Exception var3) {
            var5 = 1;
         }

         this.AS = var5;
         GameCanvas.end_Dialog();
         return;
      case 3:
         this.AE = GameCanvas.Start_Input_Dialog(T.nhapsoluongmuonghep, new iCommand(T.strconfirm, 2, 0, this), true, T.joinItem);
         GameCanvas.currentDialog = this.AE;
         return;
      case 4:
         GameCanvas.end_Dialog();
         GlobalService.getInstance().Split_Item((byte)1, (byte)4, (short)0, (byte)0, (short)0);
         return;
      default:
         super.commandPointer(var1, var2);
      }
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      int var2 = this.AP;
      int var10000 = this.AO;
      int var10002 = MotherCanvas.hw - this.AL / 2;
      int var10006 = this.AL;
      this.AD(var1, var10002, var2, this.AL, this.AN, 0);
      Interface_Game.paintNumMess(var1, -Interface_Game.xNumMess + 8, -Interface_Game.yNumMess + 3);
      var1.AD(MotherCanvas.hw - this.AL / 2, 0, this.AL, MotherCanvas.h);
      mGraphics.AC();
      var10000 = MotherCanvas.hw;
      var10000 = this.AL;
      var10000 = this.AL;
      var10000 = MotherCanvas.h;
      mGraphics.AD();
      var2 += 12;
      var1.setColor(-2573687);
      var1.fillRoundRectNew(this.AO + 10, var2, this.AL - 20, 16, 4, 4);
      var2 += 3;
      mFont.tahoma_7b_red.drawString(var1, T.joinItem, this.AO + this.AL / 2, var2, 2);
      var2 += GameCanvas.hText;

      int var3;
      for(var3 = 0; var3 < this.AR.length; ++var3) {
         mFont.tahoma_7_black.drawString(var1, this.AR[var3], MotherCanvas.w / 2, var2 + 10, 2);
         var2 += GameCanvas.hText;
      }

      if (this.AT == 0) {
         if (AvMain.DS == null) {
            AvMain.DS = mImage.createImage("/interface/muiten.png");
         } else {
            var1.drawRegion((mImage)AvMain.DS, this.AO + this.AL / 2, this.AD[0][1] + this.AM / 2, 3);
         }
      }

      for(var3 = 0; var3 < this.AD.length; ++var3) {
         AvMain.paintRect(var1, this.AD[var3][0], this.AD[var3][1], this.AM, this.AM, (byte)1, 3);
         if (this.AB[var3] != null && !this.AB[var3].CE) {
            this.AB[var3].AC(var1, this.AD[var3][0] + this.AM / 2, this.AD[var3][1] + this.AM / 2, this.AM - 1);
            this.AB[var3].AA(var1, this.AD[var3][0] + this.AM / 2, this.AD[var3][1] + this.AM / 2, this.AM, 0, this.AB[var3].colorName, (short)(this.AB[var3].numPotion * this.AS));
         }
      }

      if (this.AF != null) {
         for(var3 = 0; var3 < this.AF.size(); ++var3) {
            iCommand var4;
            (var4 = (iCommand)this.AF.elementAt(var3)).paint(var1, var4.xCmd, var4.yCmd);
         }
      }

      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);

      for(var3 = 0; var3 < this.AG.size(); ++var3) {
         MainEffect var5;
         if ((var5 = (MainEffect)this.AG.elementAt(var3)).levelPaint >= 0) {
            var5.paint(var1);
         }
      }

   }

   public final void update() {
      if (super.KP != null) {
         super.KP.update();
      }

      if (GameCanvas.currentDialog == null) {
         this.handleKeyPress();
         this.updatePointer();
      }

      ScreenJoinItem var1 = this;
      if (this.AT == 1) {
         ++this.AU;
         float var10000;
         if (this.AV == 0) {
            this.AF.removeAllElements();
            this.AS = 1;
            if (this.AU % 10 == 0) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
               var10000 = mSound.volumeSound;
               mSound.playSound();

               for(int var2 = 1; var2 < var1.AD.length; ++var2) {
                  var1.AG.addElement(GameScreen.AA((short)78, 0, var1.AD[var2][0] + var1.AM / 2, var1.AD[var2][1] + var1.AM / 2, var1.AD[0][0] + var1.AM / 2, var1.AD[0][1] + var1.AM / 2));
                  var1.AB[var2].CE = true;
               }

               var1.AV = var1.AU + 11 + var1.AL / 2 / 8;
            }
         } else if (this.AU >= this.AV) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
            this.AG.addElement(GameScreen.AA((short)53, 0, this.AD[0][0] + this.AM / 2, this.AD[0][1] + this.AM / 2, this.AD[0][0] + this.AM / 2, this.AD[0][1] + this.AM / 2));
            this.AT = 2;
            this.AU = 0;
            this.AV = 0;
            this.AS = 1;
            if (this.AB != null && this.AB.length > 0) {
               this.AB[0].CE = false;
            }

            this.AF.removeAllElements();
            this.AF.addElement(this.AK);
            this.AF.addElement(this.AH);
            this.AQ = 0;
            if (!GameCanvas.isKeyPressed()) {
               this.AK.setPos(this.AO + this.AL / 2, this.AP + this.AN - iCommand.hButtonCmdNor / 2 - 5, (FrameImage)null, this.AK.caption);
            } else {
               this.AK.setPos(this.AO + this.AL / 2 - iCommand.wButtonCmd / 2 - 10, this.AP + this.AN - 10 - iCommand.hButtonCmdNor, (FrameImage)null, this.AK.caption);
               this.AK.AG = true;
               this.AH.setPos(this.AO + this.AL / 2 + iCommand.wButtonCmd / 2 + 10, this.AP + this.AN - 10 - iCommand.hButtonCmdNor, (FrameImage)null, this.AH.caption);
            }
         }
      }

      for(int var3 = 0; var3 < this.AG.size(); ++var3) {
         MainEffect var4;
         (var4 = (MainEffect)this.AG.elementAt(var3)).update();
         if (var4.isStop) {
            this.AG.removeElement(var4);
            --var3;
         }
      }

   }

   public final void handleKeyPress() {
      if (this.AT != 1) {
         if (this.AF != null) {
            int var1 = this.AF.size();
            if (GameCanvas.isKeyPressed() && var1 > 0) {
               int var2 = this.AQ;
               if (GameCanvas.isKeyPressed(0)) {
                  --this.AQ;
                  GameCanvas.clearKeyPressed(0);
               } else if (GameCanvas.isKeyPressed(2)) {
                  ++this.AQ;
                  GameCanvas.clearKeyPressed(2);
               }

               this.AQ = AvMain.AA(this.AQ, var1 - 1, false);
               if (((iCommand)this.AF.elementAt(this.AQ)).caption.length() == 0) {
                  this.AQ = 0;
               }

               if (var2 != this.AQ && GameCanvas.isKeyPressed()) {
                  for(var2 = 0; var2 < var1; ++var2) {
                     iCommand var3 = (iCommand)this.AF.elementAt(var2);
                     if (var2 == this.AQ) {
                        var3.AG = true;
                     } else {
                        var3.AG = false;
                     }
                  }
               }
            }
         }

         if (GameCanvas.AL[5]) {
            GameCanvas.AB(5);
            if (this.AF != null && this.AQ < this.AF.size()) {
               ((iCommand)this.AF.elementAt(this.AQ)).AD();
            }
         }

         this.AS();
      }
   }

   public final void updatePointer() {
      if (this.AT != 1) {
         if (this.AF != null) {
            for(int var1 = 0; var1 < this.AF.size(); ++var1) {
               ((iCommand)this.AF.elementAt(var1)).AE();
            }
         }

      }
   }
}
