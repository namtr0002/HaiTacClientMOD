public class LuckyScreen extends MainScreen {
   public static LuckyScreen AH;
   public mVector AI = new mVector();
   public mVector AJ = new mVector("LuckyScreen.vecEff");
   protected int AK;
   protected int AL;
   protected int AM = 260;
   protected int AN = 215;
   protected int AO = -1;
   protected int AP;
   protected int AQ;
   protected int AR;
   protected int AS;
   protected int AT;
   protected int AU;
   protected int AV = 0;
   public int AW;
   public int AX;
   public int AY = 0;
   protected iCommand AZ;
   protected iCommand cmdQuay;
   protected iCommand BB;
   private iCommand AA;
   protected FrameImage BC;
   protected FrameImage BD;
   protected FrameImage BE;
   public static mImage[] BF;
   protected int[] BG = new int[]{0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
   protected int[] BH = new int[]{0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
   protected int[] BI = new int[]{0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1};
   public static mVector BJ = null;
   public int BK = 0;
   private MainItem AB;
   public static boolean BL = false;
   public Item_Drop[] BM;

   public LuckyScreen() {
      this.AK = MotherCanvas.hw - this.AM / 2;
      this.AL = MotherCanvas.hh - this.AN / 2;
      this.AQ = this.AK + 100;
      this.AR = this.AL + this.AN / 2 + 12;
      this.AU = (this.AN - 25) / 5 + 5;
      this.AS = this.AK + this.AM - 40;
      this.AT = this.AL + 15 + this.AU;
      BF = null;
      this.AF();
      this.AZ = new iCommand(T.close, -1, this);
      this.AA = new iCommand("", 1, this);
      this.cmdQuay = new iCommand("", 2, 1, this);
      this.BB = new iCommand("", 2, 2, this);
      this.AA.setPos(this.AS, this.AT + this.AU + 5, this.BC, "");
      this.cmdQuay.setPos(this.AS, this.AT + (this.AU << 1), this.BD, "");
      this.BB.setPos(this.AS, this.AT + this.AU * 3, this.BE, "");
      this.AI = new mVector();
      this.AI.addElement(this.AA);
      this.AI.addElement(this.cmdQuay);
      this.AI.addElement(this.BB);
      this.AI.addElement(this.AZ);
      if (GameCanvas.isTouch) {
         int var1;
         if ((var1 = this.AK + this.AM - 13) > MotherCanvas.w - 9) {
            var1 = MotherCanvas.w - 9;
         }

         this.AZ.setPos(var1, this.AL + 13, MainTab.fraCloseTab, "");
      } else {
         AvMain.AA(this.AZ, 2);
         super.DB = this.AZ;
         this.AO = 1;
         this.cmdQuay.isPlayframe = true;
      }

      this.AJ();
      this.BM = null;
   }

   public void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         return;
      case 1:
         GlobalService.getInstance().AL((byte)4);
         return;
      case 2:
         GlobalService.getInstance().AL((byte)var2);
      case 0:
      default:
      }
   }

   public final void AJ() {
      for(int var1 = 0; var1 < Player.vecInventory.size(); ++var1) {
         MainItem var2;
         if ((var2 = (MainItem)Player.vecInventory.elementAt(var1)).typeObject == 4 && var2.ID == 0) {
            return;
         }
      }

   }

   public void AK() {
      for(int var1 = 0; var1 < BJ.size(); ++var1) {
         MainItem var2 = (MainItem)BJ.elementAt(var1);
         if (var1 < 8) {
            var2.AC = this.AQ + CRes.getcos(CRes.fixangle(var1 * 360 / 8)) * 66 / 1000;
            var2.AD = this.AR + CRes.getsin(CRes.fixangle(var1 * 360 / 8)) * 66 / 1000;
         } else {
            var2.AC = this.AQ + 1 + (CRes.getcos(CRes.fixangle(30 + var1 * 360 / 6)) << 5) / 1000;
            var2.AD = this.AR + (CRes.getsin(CRes.fixangle(30 + var1 * 360 / 6)) << 5) / 1000;
         }
      }

   }

   public void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      if (!GameCanvas.isSmallScreen) {
         AvMain.AG(var1, this.AK, this.AL, this.AM, this.AN);
      }

      this.AB(var1);

      for(int var2 = 0; var2 < this.AI.size(); ++var2) {
         iCommand var3;
         (var3 = (iCommand)this.AI.elementAt(var2)).paint(var1, var3.xCmd, var3.yCmd);
      }

      this.AA(var1, 0);
   }

   public void AB(mGraphics var1) {
      if (BF == null) {
         this.AF();
      } else {
         var1.drawRegion((mImage)BF[5], this.AS, this.AT, 3);
         AvMain.AB(var1, "" + this.AL(), this.AS, this.AT + 12, 2, (int)5);
         int var2;
         int var3;
         int var4;
         if (this.AY == 3 && BJ != null) {
            for(var2 = -1; var2 < 2; ++var2) {
               var3 = this.AV + var2;
               var4 = BJ.size();
               if (var3 < 0) {
                  var3 += var4;
               } else if (var3 >= var4) {
                  var3 -= var4;
               }

               ((MainItem)BJ.elementAt(var3)).AA(var1, this.AQ, this.AR - var2 * 14, 24, 0, (byte)0);
            }
         }

         var2 = 0;
         var3 = 0;
         if (this.AY == 3) {
            var2 = this.BH[this.AP];
            var3 = this.BI[this.AP];
         }

         var1.drawRegion(BF[0], var2 * 95, 0, 95, 190, 0, this.AQ - 94, this.AR - 94, 0);
         var1.drawRegion(BF[0], var2 * 95, 0, 95, 190, 2, this.AQ, this.AR - 94, 0);
         var1.drawRegion(BF[1], 0, var3 * 99, 99, 99, 0, this.AQ, this.AR, 3);
         if (this.AY != 1 && this.AY != 0) {
            label56: {
               if (this.AY == 2) {
                  var4 = this.AR;
                  if (this.AW <= 23) {
                     var1.drawRegion(BF[2], 0, this.BG[this.AP] * 33, 33, 33, 0, this.AQ, var4, 3);
                     break label56;
                  }

                  var1.drawRegion(BF[4], 0, 33, 33, 33, 0, this.AQ, this.AR + 1, 3);
               }

               var1.drawRegion(BF[4], 0, 0, 33, 33, 0, this.AQ, this.AR, 3);
            }
         } else {
            var1.drawRegion(BF[3], 0, 0, 33, 33, 0, this.AQ, this.AR, 3);
         }

         if (this.AY == 2 || this.AY == 0 || this.AY == 1) {
            for(var4 = 0; var4 < BJ.size(); ++var4) {
               MainItem var5 = (MainItem)BJ.elementAt(var4);
               if (var4 >= this.BK) {
                  var5.AA(var1, var5.AC, var5.AD, 24, 0, (byte)0);
               }
            }
         }

      }
   }

   public final void AA(mGraphics var1, int var2) {
      for(var2 = 0; var2 < this.AJ.size(); ++var2) {
         MainEffect var3;
         if ((var3 = (MainEffect)this.AJ.elementAt(var2)).levelPaint >= 0) {
            var3.paint(var1);
         } else if (var3.levelPaint == -1) {
            var3.paint(var1);
         }
      }

   }

   public void AF() {
      if (BF == null) {
         BF = new mImage[6];

         for(int var1 = 0; var1 < BF.length; ++var1) {
            BF[var1] = mImage.createImage("/interface/lucky" + var1 + ".png");
         }
      }

      if (this.BC == null) {
         this.BC = new FrameImage(mImage.createImage("/interface/lucky8.png"), 37, 27);
         this.BD = new FrameImage(mImage.createImage("/interface/lucky6.png"), 40, 42);
         this.BE = new FrameImage(mImage.createImage("/interface/lucky7.png"), 40, 42);
      }

   }

   public void update() {
      if (super.KP != null) {
         super.KP.update();
      }

      for(int var1 = 0; var1 < this.AJ.size(); ++var1) {
         MainEffect var2;
         (var2 = (MainEffect)this.AJ.elementAt(var1)).update();
         if (var2.isStop) {
            this.AJ.removeElement(var2);
            --var1;
         }
      }

      this.AG();
   }

   public void AG() {
      ++this.AW;
      if (this.AY == 1) {
         if (this.AW == 5) {
            if (this.AX == 0) {
               this.AJ.addElement(GameScreen.AA((short)78, 1, this.cmdQuay.xCmd, this.cmdQuay.yCmd, this.AQ - 5, this.AR - 5));
            } else {
               this.AJ.addElement(GameScreen.AA((short)78, 2, this.BB.xCmd, this.BB.yCmd, this.AQ - 5, this.AR - 5));
            }
         }

         if (this.AW == 33) {
            this.AY = 2;
            this.AW = -10;
            this.AP = 0;
            this.AJ.addElement(GameScreen.AA((short)53, 0, this.AQ, this.AR, this.AQ, this.AR));
            this.BK = 0;
         }

         if (this.AW >= 100) {
            this.AY = 2;
            this.AW = -10;
            this.AP = 0;
            this.AJ.addElement(GameScreen.AA((short)53, 0, this.AQ, this.AR, this.AQ, this.AR));
            this.BK = 0;
            return;
         }
      } else {
         float var10000;
         if (this.AY == 2) {
            if (this.AW >= 0) {
               ++this.AP;
            }

            if (this.AP >= this.BG.length) {
               this.AP = this.BG.length - 1;
            }

            if (this.AW >= 30) {
               this.AY = 3;
               this.AW = -5;
               this.AP = 0;
            }

            if (this.AW >= 0) {
               int var1 = this.BK;
               if (this.AW < 6) {
                  if (this.AW % 5 == 0) {
                     ++this.BK;
                  }
               } else if (this.AW < 16) {
                  if (this.AW % 3 == 0) {
                     ++this.BK;
                  }
               } else if (this.AW % 2 == 0) {
                  ++this.BK;
               }

               if (this.BK != var1) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  return;
               }
            }
         } else if (this.AY == 3) {
            if (this.AP % 15 == 0) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            if (GameCanvas.gameTick % 2 == 0) {
               ++this.AV;
            }

            if (BJ != null && this.AV >= BJ.size()) {
               this.AV = 0;
            }

            if (this.AW >= 0) {
               ++this.AP;
            }

            if (this.AP >= this.BH.length) {
               this.AP = this.BH.length - 1;
            }

            if (this.AW >= 100) {
               this.AY = 4;
               this.AW = 0;
               return;
            }
         } else if (this.AY == 4) {
            this.BK = 0;
            if (this.AW >= 5) {
               this.AY = 0;
               this.AW = 0;
            }

            MsgShowGift var2;
            (var2 = new MsgShowGift()).setinfoShow_Gift((byte)20, T.RS, "", this.BM, (short)-1);
            GameCanvas.AA((MainDialog)var2);
         }
      }

   }

   public final void handleKeyPress() {
      boolean var1 = false;
      if (GameCanvas.isKeyPressed(1)) {
         if (this.AO > 0) {
            --this.AO;
         }

         GameCanvas.clearKeyPressed(1);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         if (this.AO < 2) {
            ++this.AO;
         }

         GameCanvas.clearKeyPressed(3);
         var1 = true;
      }

      if (GameCanvas.AL[5] && this.AO != -1) {
         GameCanvas.AB(5);
         ((iCommand)this.AI.elementAt(this.AO)).AD();
      }

      if (var1) {
         for(int var2 = 0; var2 < this.AI.size(); ++var2) {
            iCommand var3 = (iCommand)this.AI.elementAt(var2);
            if (var2 == this.AO) {
               var3.isPlayframe = true;
            } else {
               var3.isPlayframe = false;
            }
         }
      }

      super.handleKeyPress();
   }

   public void updatePointer() {
      if (GameCanvas.isPointerSelect && HuyHieuClanScreen.isRunningOpenXu) {
         HuyHieuClanScreen.instance.updatePointerOpenXu();
      }

      if (this.AY == 0) {
         if (this.AI != null) {
            for(int var1 = 0; var1 < this.AI.size(); ++var1) {
               ((iCommand)this.AI.elementAt(var1)).AE();
            }
         }

         super.updatePointer();
      }
   }

   public int AL() {
      if (this.AB == null || BL) {
         for(int var1 = 0; var1 < Player.vecInventory.size(); ++var1) {
            MainItem var2;
            if ((var2 = (MainItem)Player.vecInventory.elementAt(var1)).typeObject == 4 && var2.ID == 232) {
               this.AB = var2;
               BL = false;
               break;
            }
         }
      }

      return this.AB != null ? this.AB.numPotion : 0;
   }

   public final void AA(Item_Drop[] var1) {
      (new StringBuffer("itemLucky ")).append(var1.length).toString();
      this.BM = null;
      this.BM = var1;
   }

   public final void AA(String var1, int var2, int var3, byte var4, FrameImage var5, int var6) {
      Class_BN var7 = new Class_BN(var1, var2, var3, var4, var5, var6);
      if ((var2 = GameScreen.AA(this.AJ)) == this.AJ.size()) {
         this.AJ.addElement(var7);
      } else {
         this.AJ.setElementAt(var7, var2);
      }
   }
}
