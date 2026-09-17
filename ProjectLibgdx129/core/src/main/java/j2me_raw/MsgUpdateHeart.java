public final class MsgUpdateHeart extends ScreenUpgrade {
   public static MsgUpdateHeart instance;
   private String CQ = "";
   private MainItem CR;
   private int CS = 0;
   private iCommand CT;
   private int[] CU = new int[]{0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2};
   private int[] CV = new int[]{0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 0, 0, 1, 1, 2, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2};
   private int CW = 0;
   private int CX = 0;
   private int CY = 0;
   private int CZ = 0;
   private int IL = 0;
   private int IM = 0;
   private int IN = 0;
   private int IO = 0;
   private int IP = 0;
   private int IQ = 0;
   private int IR = 0;
   private int IS = 0;
   private int IT = 0;

   public MsgUpdateHeart() {
      super.AB = 150;
      super.AC = 190 + GameCanvas.hCommand;
      super.AE = MotherCanvas.hw - super.AB / 2;
      super.AF = MotherCanvas.hh - super.AC / 2 - 5;
      this.CS = 10;
      if (!GameCanvas.isTouch) {
         super.AC -= 30;
         this.CS += 12;
      }

      super.AQ = new mVector();
      super.AT = new iCommand(T.close, 0, this);
      this.CT = new iCommand(T.TT, 1, this);
      super.AQ.addElement(super.AT);
      super.AQ.addElement(this.CT);
      if (GameCanvas.isTouch) {
         super.AT.setPos(super.AE + super.AB / 2 + 60, super.AF + 20, MainTab.fraCloseTab3, "");
         this.CT.setPos(super.AE + super.AB / 2, super.AF + super.AC - GameCanvas.hCommand + 5, MainTab.fraCmdMo, "");
         super.backCMD = super.AT;
         super.DF = this.CT;
      } else {
         super.AT = AvMain.AA(super.AT, 2);
         this.CT = AvMain.AA(this.CT, 0);
         super.DB = super.AT;
         super.center = this.CT;
      }

      MainItem var1;
      if ((var1 = (MainItem)GameScreen.player.LR.get("7")) != null && var1.BF == 1) {
         this.CR = var1;
      }

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
         GlobalService.getInstance().Upgrade_Item((byte)15, (short)-1, (byte)0);
         this.CW = 2;
         super.BD = 0;
         return;
      default:
      }
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      int var10001 = MotherCanvas.hw - 160;
      int var10002 = 3 + GameScreen.h12plus;
      mFont var10003 = mFont.tahoma_7_black;
      Interface_Game.AA(var1, var10001, var10002);
      var10002 = MotherCanvas.hw - super.AB / 2;
      int var10006 = super.AB;
      this.AD(var1, var10002, super.AF, super.AB, super.AC, AvMain.IK);
      if (this.CR != null) {
         mFont.tahoma_7b_white.drawString(var1, T.GO + ": +" + this.CR.LvUpgrade, super.AE + 10, super.AF + 33, 0);
         mFont.tahoma_7b_white.drawString(var1, T.RP + this.CR.AQ, super.AE + 10, super.AF + 33 + GameCanvas.hText, 0);
      }

      if (AvMain.FP == null) {
         AvMain.AT();
      }

      var1.drawRegion((mImage)AvMain.FP[7], super.AE + super.AB / 2, super.AF + 20, 3);
      var1.drawRegion((mImage)AvMain.FP[0], super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS, 3);
      if (this.CW != 0 && this.CW != 2) {
         if (this.CW == 1) {
            if (this.CX < 115 || GameCanvas.gameTick % 3 != 0) {
               if (this.CY > 0) {
                  var1.drawRegion(AvMain.FP[1], 0, 55 - this.CY, this.CY, this.CY, 0, super.AE + super.AB / 2 - 55, super.AF + super.AC / 2 + this.CS - this.CY, 0);
               }

               if (this.CZ > 0) {
                  var1.drawRegion(AvMain.FP[1], 55, 0, this.CZ, this.CZ, 0, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS - 55, 0);
               }

               if (this.IL > 0) {
                  var1.drawRegion(AvMain.FP[1], 110 - this.IL, 55, this.IL, this.IL, 0, super.AE + super.AB / 2 + 55 - this.IL, super.AF + super.AC / 2 + this.CS, 0);
               }

               if (this.IM > 0) {
                  var1.drawRegion(AvMain.FP[1], 55 - this.IM, 110 - this.IM, this.IM, this.IM, 0, super.AE + super.AB / 2 - this.IM, super.AF + super.AC / 2 + this.CS + 55 - this.IM, 0);
               }

               if (this.IN > 0) {
                  var1.drawRegion(AvMain.FP[2], 0, 0, 90, this.IN, 0, super.AE + super.AB / 2 - 45, super.AF + super.AC / 2 + this.CS - 45, 0);
               }

               if (this.IO > 0) {
                  var1.drawRegion(AvMain.FP[3], 0, 0, this.IO, this.IO, 0, super.AE + super.AB / 2 - 32, super.AF + super.AC / 2 + this.CS - 32, 0);
               }

               if (this.IP > 0) {
                  var1.drawRegion(AvMain.FP[3], 62, 0, 2, this.IP, 0, super.AE + super.AB / 2 + 32 - 2, super.AF + super.AC / 2 + this.CS - 32, 0);
                  var1.drawRegion(AvMain.FP[3], 0, 62, this.IP, 2, 0, super.AE + super.AB / 2 - 32, super.AF + super.AC / 2 + this.CS + 32 - 2, 0);
               }

               if (this.IQ > 0) {
                  var1.drawRegion(AvMain.FP[4], 32 - this.IQ, 0, this.IQ, this.IQ, 0, super.AE + super.AB / 2 - this.IQ, super.AF + super.AC / 2 + this.CS - 32, 0);
               }

               if (this.IR > 0) {
                  var1.drawRegion(AvMain.FP[4], 0, 32, this.IR, this.IR, 0, super.AE + super.AB / 2 - 32, super.AF + super.AC / 2 + this.CS, 0);
               }

               if (this.IS > 0) {
                  var1.drawRegion(AvMain.FP[4], 32, 64 - this.IS, this.IS, this.IS, 0, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS + 32 - this.IS, 0);
               }

               if (this.IT > 0) {
                  var1.drawRegion(AvMain.FP[4], 64 - this.IT, 32 - this.IT, this.IT, this.IT, 0, super.AE + super.AB / 2 + 32 - this.IT, super.AF + super.AC / 2 + this.CS - this.IT, 0);
               }
            }

            var1.drawRegion(AvMain.FP[6], 0, 50 * this.CV[this.CX], 48, 50, 0, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS, 3);
         }
      } else {
         var1.drawRegion(AvMain.FP[6], 0, 50 * this.CU[this.CX], 48, 50, 0, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS, 3);
      }

      var1.drawRegion((mImage)AvMain.FP[5], super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS, 3);
      GameCanvas.resetTrans(var1);

      int var2;
      for(var2 = 0; var2 < super.vecEff.size(); ++var2) {
         MainEffect var3;
         if ((var3 = (MainEffect)super.vecEff.elementAt(var2)).levelPaint != -1) {
            var3.paint(var1);
            var3.paint(var1, 0, 0);
         }
      }

      if (this.CW == 0 && super.AQ != null) {
         for(var2 = 0; var2 < super.AQ.size(); ++var2) {
            iCommand var4;
            (var4 = (iCommand)super.AQ.elementAt(var2)).paint(var1, var4.xCmd, var4.yCmd);
         }
      }

   }

   public final void update() {
      if (super.KP != null) {
         super.KP.update();
      }

      for(int var1 = 0; var1 < super.vecEff.size(); ++var1) {
         MainEffect var2;
         (var2 = (MainEffect)super.vecEff.elementAt(var1)).update();
         if (var2.isStop) {
            super.vecEff.removeElement(var2);
            --var1;
         }
      }

      if (this.CW == 2) {
         ++super.BD;
         if (super.BD >= 80) {
            this.CW = 0;
         }
      }

      ++this.CX;
      if (this.CW != 0 && this.CW != 2) {
         if (this.CW == 1) {
            this.CY = this.CX * 5;
            if (this.CY > 55) {
               this.CY = 55;
            }

            this.CZ = (this.CX - 11) * 5;
            if (this.CZ > 55) {
               this.CZ = 55;
            }

            this.IL = (this.CX - 22) * 5;
            if (this.IL > 55) {
               this.IL = 55;
            }

            this.IM = (this.CX - 33) * 5;
            if (this.IM > 55) {
               this.IM = 55;
            }

            this.IN = (this.CX - 44) * 5;
            if (this.IN > 90) {
               this.IN = 90;
            }

            this.IO = (this.CX - 59) * 5;
            if (this.IO > 63) {
               this.IO = 63;
            }

            this.IP = (this.CX - 72) * 5;
            if (this.IP > 64) {
               this.IP = 64;
            }

            this.IQ = (this.CX - 85) * 5;
            if (this.IQ > 32) {
               this.IQ = 32;
            }

            this.IR = (this.CX - 91) * 5;
            if (this.IR > 32) {
               this.IR = 32;
            }

            this.IS = (this.CX - 97) * 5;
            if (this.IS > 32) {
               this.IS = 32;
            }

            this.IT = (this.CX - 103) * 5;
            if (this.IT > 32) {
               this.IT = 32;
            }

            if (this.CX >= this.CV.length) {
               this.CX = 0;
               this.CY = 0;
               this.CZ = 0;
               this.IN = 0;
               this.CW = 0;
               MainItem var3;
               if ((var3 = (MainItem)GameScreen.player.LR.get("7")) != null && var3.BF == 1) {
                  this.CR = var3;
               }

               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(this.CQ);
            }

            float var10000;
            if (this.CX >= this.CV.length - 50 && this.CX <= this.CV.length - 20 && this.CX % 5 == 0) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
               this.AA((short)10, 0, super.AE + super.AB / 2, super.AF + super.AC / 2 + 5, super.AE + super.AB / 2, super.AF + super.AC / 2 + 5);
            }

            if (this.CX == this.CV.length - 20) {
               byte var4;
               if (super.Step == 16) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  var4 = 0;
                  if (GameCanvas.language == 1) {
                     var4 = 2;
                  }

                  this.AA((short)79, var4, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS);
                  this.AA((short)76, 0, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS);
                  this.AA((short)53, 0, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS);
                  return;
               }

               if (super.Step == 17) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  var4 = 1;
                  if (GameCanvas.language == 1) {
                     var4 = 3;
                  }

                  this.AA((short)79, var4, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS);
                  this.AA((short)77, 0, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS, super.AE + super.AB / 2, super.AF + super.AC / 2 + this.CS);
               }
            }
         }
      } else if (this.CX >= this.CU.length) {
         this.CX = 0;
         return;
      }

   }

   public final void updatePointer() {
      if (this.CW == 0 && super.AQ != null) {
         for(int var1 = 0; var1 < super.AQ.size(); ++var1) {
            ((iCommand)super.AQ.elementAt(var1)).AE();
         }
      }

   }

   public final void handleKeyPress() {
      if (this.CW == 0) {
         super.AG();
      }

   }

   public final void updateStepUpgrade(byte var1, String var2) {
      if (var1 == 16 || var1 == 17) {
         this.CX = 0;
         this.CY = 0;
         this.CZ = 0;
         this.IN = 0;
         this.CW = 1;
         super.Step = var1;
      }

      this.CQ = var2;
      if (this.CT != null) {
         this.CT.AG = false;
         this.CT.frameCmd = 0;
      }

   }
}
