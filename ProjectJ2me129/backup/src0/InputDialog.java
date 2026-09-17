public final class InputDialog extends MainDialog {
   public TField tfInput;
   private iCommand AC;
   public TField[] AB;
   private boolean AD = false;
   private boolean AE = false;
   private String AF;
   private String[] AG;
   private static int AH = 0;
   private static int AI = 0;
   private short AJ;
   private mFont AK;
   private static iCommand AL;
   private ListNew AM;

   public InputDialog() {
      this.AK = mFont.tahoma_7_white;
      this.AC = new iCommand(T.close, -1);
      AL = new iCommand(T.DA, 0);
      AI = 15;
      if (GameCanvas.isTouch) {
         AH = iCommand.hButtonCmdNor + 5;
      }

   }

   public final void AC(int var1, int var2) {
      switch(var1) {
      case -1:
         if (GameCanvas.currentDialog != null) {
            GameCanvas.currentDialog = null;
         } else {
            GameCanvas.subDialog = null;
         }
         break;
      case 0:
         String[] var3;
         if (this.AB != null) {
            var3 = new String[this.AB.length];

            for(int var4 = 0; var4 < this.AB.length; ++var4) {
               var3[var4] = this.AB[var4].getText();
            }
         } else {
            if (this.tfInput == null) {
               return;
            }

            (var3 = new String[1])[0] = this.tfInput.getText();
         }

         GlobalService.getInstance().AA(this.AJ, var3);
         GameCanvas.end_Dialog();
         GameCanvas.AA(T.BP, true);
      }

      super.AC(var1, var2);
   }

   public final void AA(String var1, iCommand var2, boolean var3, String var4) {
      this.AM = null;
      this.AD = false;
      this.AE = false;
      super.DA = null;
      super.DB = null;
      super.center = null;
      if (var2 == null) {
         GameCanvas.currentDialog = null;
      }

      super.wDia = MotherCanvas.w - 30;
      if (super.wDia > 200) {
         super.wDia = 200;
      }

      super.strinfo = this.AK.splitFontArray(var1, super.wDia - 20);
      this.AF = var4;
      super.hDia = 15 * super.strinfo.length + 10 + TField.AB() + GameCanvas.hCommand;
      super.hDia += AH + AI;
      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.h - (GameCanvas.hCommand << 1) - super.hDia + 15;
      this.tfInput = new TField(super.AX + 10, super.AY + super.hDia - AH - (TField.AB() + 8), super.wDia - 20);
      this.tfInput.AF(100);
      if (var3) {
         this.tfInput.AG(1);
      }

      this.tfInput.AB("");
      if (GameCanvas.isTouch) {
         var2.setPos(super.AX + super.wDia / 2, super.AY + super.hDia - iCommand.hButtonCmdNor / 2 - 5, (FrameImage)null, var2.caption);
         this.AC.setPos(super.AX + super.wDia - 13, super.AY + 13, MainTab.fraCloseTab, "");
         super.DB = this.AC;
         super.DA = var2;
         this.tfInput.AH = var1;
         if (GameCanvas.isKeyPressed()) {
            this.tfInput.AA(true);
         }

         super.backCMD = super.DB;
         super.DF = super.DA;
      } else {
         super.center = var2;
         super.DA = this.AC;
         this.tfInput.AA(true);
         super.DB = this.tfInput.AG;
      }
   }

   public final void AA(String[] var1, String var2, short var3, iCommand var4, String var5) {
      if (var4 == null) {
         var4 = AL;
      }

      this.AM = null;
      this.AG = null;
      this.AJ = var3;
      this.AD = true;
      this.AE = false;
      super.DA = null;
      super.DB = null;
      super.center = null;
      super.wDia = MotherCanvas.w - 30;
      if (super.wDia > 160) {
         super.wDia = 160;
      }

      this.AB = new TField[var1.length];
      super.strinfo = var1;
      this.AF = var2;
      super.hDia = (TField.AB() + 18) * super.strinfo.length + 6 + GameCanvas.hCommand;
      super.hDia += AH + AI;
      if (var5 != null && var5.length() > 0) {
         this.AG = mFont.tahoma_7b_red.splitFontArray(var5, super.wDia - 10);
         super.hDia += GameCanvas.hText * this.AG.length;
      }

      int var6 = 0;
      if (GameCanvas.isTouch) {
         if (super.hDia > MotherCanvas.h - GameCanvas.hCommand) {
            var6 = super.hDia;
            super.hDia = MotherCanvas.h - GameCanvas.hCommand;
         }
      } else if (super.hDia > MotherCanvas.h - (GameCanvas.hCommand << 1)) {
         var6 = super.hDia;
         super.hDia = MotherCanvas.h - (GameCanvas.hCommand << 1);
      }

      super.AX = MotherCanvas.hw - super.wDia / 2;
      super.AY = MotherCanvas.h - (GameCanvas.hCommand << 1) - super.hDia + 15;
      if (var6 > 0) {
         if (GameCanvas.isTouch) {
            super.AY = MotherCanvas.h - GameCanvas.hCommand - super.hDia + 15;
         } else {
            super.AY = MotherCanvas.h - (GameCanvas.hCommand << 1) - super.hDia + 15;
         }

         this.AM = new ListNew(super.AX, super.AY, super.wDia, super.hDia, 0, 0, var6 - super.hDia, true);
      }

      for(var6 = 0; var6 < this.AB.length; ++var6) {
         this.AB[var6] = new TField(super.AX + 10, super.AY + 8 + (TField.AB() + 18) * var6 + AI + GameCanvas.hCommand, super.wDia - 20);
         this.AB[var6].AB("");
      }

      if (GameCanvas.isTouch) {
         this.AC.setPos(super.AX + super.wDia - 13, super.AY + 13, MainTab.fraCloseTab, "");
         super.DB = this.AC;
         var4.setPos(super.AX + super.wDia / 2, super.AY + super.hDia - iCommand.hButtonCmdNor / 2 - 3, (FrameImage)null, var4.caption);
         super.DA = var4;
         if (GameCanvas.isKeyPressed()) {
            this.AB[0].AA(true);
            super.backCMD = super.DB;
            super.DF = super.DA;
         }
      } else {
         super.center = var4;
         super.DA = this.AC;
      }

      if (!GameCanvas.isTouch) {
         this.AB[0].AA(true);
         super.DB = this.AB[0].AG;
      }

   }

   public final void AA(String[] var1) {
      if (this.AB != null) {
         for(int var2 = 0; var2 < this.AB.length; ++var2) {
            if (var2 < var1.length) {
               this.AB[var2].AB(var1[var2]);
            }
         }
      }

   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      AvMain.paintRect(var1, super.AX, super.AY, super.wDia, super.hDia, (byte)2, 4);
      var1.setColor(-805042);
      var1.fillRoundRectNew(super.AX + 15, super.AY + 5, super.wDia - 25, 16, 4, 4);
      mFont.tahoma_7b_black.drawString(var1, this.AF, super.AX + super.wDia / 2, super.AY + 7, 2);
      int var2 = super.AY + AI + GameCanvas.hCommand;
      if (this.AM != null) {
         if (GameCanvas.isTouch) {
            var1.AD(super.AX, var2 - GameCanvas.hCommand / 2, super.wDia, super.hDia - (GameCanvas.hCommand << 1) - GameCanvas.hCommand / 2);
         } else {
            var1.AD(super.AX, var2 - GameCanvas.hCommand / 2, super.wDia, super.hDia - GameCanvas.hCommand - GameCanvas.hCommand / 2);
         }

         mGraphics.AC();
         int var10000 = super.AX;
         var10000 = GameCanvas.hCommand;
         var10000 = super.wDia;
         var10000 = super.hDia;
         var10000 = GameCanvas.hCommand;
         var10000 = GameCanvas.hCommand;
         mGraphics.AD();
         var1.translate(0, -this.AM.AC);
      }

      int var3;
      if (this.AD) {
         for(var3 = 0; var3 < this.AB.length; ++var3) {
            this.AK.drawString(var1, super.strinfo[var3], MotherCanvas.w / 2, var2 - 5 + var3 * (TField.AB() + 18), 2);
            this.AB[var3].AB(var1);
         }

         if (this.AG != null) {
            var3 = var2 - 5 + this.AB.length * (TField.AB() + 18);

            for(var2 = 0; var2 < this.AG.length; ++var2) {
               mFont.tahoma_7b_white.drawString(var1, this.AG[var2], super.AX + 5, var3 + var2 * GameCanvas.hText, 0);
            }
         }
      } else {
         for(var3 = 0; var3 < super.strinfo.length; ++var3) {
            this.AK.drawString(var1, super.strinfo[var3], MotherCanvas.w / 2, var2 + var3 * 15 - 5, 2);
         }

         this.tfInput.paint(var1);
      }

      if (this.AM != null) {
         mGraphics.AE();
         mGraphics.restoreCanvas();
         GameCanvas.resetTrans(var1);
      }

      super.AD(var1);
   }

   public final void AA(int var1) {
      if (!this.AD) {
         this.tfInput.AD(var1);
      } else {
         for(int var2 = 0; var2 < this.AB.length; ++var2) {
            if (this.AB[var2].AF()) {
               this.AB[var2].AD(var1);
               return;
            }
         }

      }
   }

   public final void update() {
      this.handleKeyPress();
      this.updatePointer();
      if (this.AD) {
         if (this.AM != null) {
            this.AM.AC();
         }

         for(int var1 = 0; var1 < this.AB.length; ++var1) {
            this.AB[var1].update();
         }
      } else if (this.tfInput != null) {
         this.tfInput.update();
         if (!GameCanvas.isTouch && super.DB != this.tfInput.AG) {
            super.DB = this.tfInput.AG;
         }
      }

      super.update();
   }

   public final void handleKeyPress() {
      if (this.AD) {
         int var1;
         if (GameCanvas.AL[8]) {
            for(var1 = 0; var1 < this.AB.length; ++var1) {
               if (this.AB[var1].AF()) {
                  this.AB[var1].AA(false);
                  if (var1 < this.AB.length - 1) {
                     this.AB[var1 + 1].AA(true);
                     if (!GameCanvas.isTouch) {
                        super.DB = this.AB[var1 + 1].AG;
                     }

                     if (this.AM != null) {
                        this.AM.AA((var1 + 1) * (TField.AB() + 18) - super.hDia / 4);
                     }
                  } else {
                     this.AB[0].AA(true);
                     if (!GameCanvas.isTouch) {
                        super.DB = this.AB[0].AG;
                     }

                     if (this.AM != null) {
                        this.AM.AA(0);
                     }
                  }
                  break;
               }
            }

            GameCanvas.AB(8);
         } else if (GameCanvas.AL[2]) {
            for(var1 = 0; var1 < this.AB.length; ++var1) {
               if (this.AB[var1].AF()) {
                  this.AB[var1].AA(false);
                  if (var1 > 0) {
                     this.AB[var1 - 1].AA(true);
                     if (this.AM != null) {
                        this.AM.AA((var1 - 1) * (TField.AB() + 18) - super.hDia / 4);
                     }
                  } else {
                     this.AB[this.AB.length - 1].AA(true);
                     if (this.AM != null) {
                        this.AM.AA(this.AM.AD);
                     }
                  }
                  break;
               }
            }

            GameCanvas.AB(2);
         }
      }

      super.handleKeyPress();
      this.AS();
   }

   public final void updatePointer() {
      if (this.AD) {
         int var1;
         if (this.AM != null) {
            this.AM.update_Pos_UP_DOWN();
            var1 = super.AY + AI + GameCanvas.hCommand / 2;
            if (GameCanvas.isPoint(super.AX, var1, super.wDia, super.hDia - (GameCanvas.hCommand << 1))) {
               for(var1 = 0; var1 < this.AB.length; ++var1) {
                  this.AB[var1].AE(this.AM.AC);
               }
            }
         } else {
            for(var1 = 0; var1 < this.AB.length; ++var1) {
               this.AB[var1].updatePointer();
            }
         }
      } else if (this.tfInput != null) {
         this.tfInput.updatePointer();
      }

      super.updatePointer();
   }
}
