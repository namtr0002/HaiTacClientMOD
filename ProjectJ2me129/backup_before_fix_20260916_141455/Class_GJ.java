public final class Class_GJ extends LuckyScreen {
   public static Class_GJ AA;
   private iCommand AC;
   private byte AD = -1;
   private boolean AE = false;
   private static mImage AF;
   private static mImage AG;
   private byte[] BN;
   public String AB = "";
   private int BO = 0;
   private MainItem BP;

   public Class_GJ() {
      super.AM = 280;
      super.AN = 230;
      if (super.AN > MotherCanvas.h) {
         super.AN = MotherCanvas.h;
      }

      super.AK = MotherCanvas.hw - super.AM / 2;
      super.AL = MotherCanvas.hh - super.AN / 2;
      super.AQ = super.AK + 110;
      super.AR = super.AL + super.AN / 2 + 6;
      super.AU = (super.AN - 25) / 5 + 5;
      super.AS = super.AK + super.AM - 50;
      super.AT = super.AL + 22 + super.AU;
      LuckyScreen.BF = null;
      this.AF();
      super.AZ = new iCommand(T.close, -1, this);
      this.AC = new iCommand("", 1, 4, this);
      super.cmdQuay = new iCommand("", 1, 3, this);
      this.AC.setPos(super.AS, super.AT + super.AU + 30, super.BC, "");
      super.cmdQuay.setPos(super.AS, super.AT + (super.AU << 1) + 25, super.BD, "");
      super.AI = new mVector();
      super.AI.addElement(this.AC);
      super.AI.addElement(super.cmdQuay);
      super.AI.addElement(super.AZ);
      if (GameCanvas.isTouch) {
         int var1;
         if ((var1 = super.AK + super.AM - 13) > MotherCanvas.w - 9) {
            var1 = MotherCanvas.w - 9;
         }

         super.AZ.setPos(var1, super.AL + 13, MainTab.fraCloseTab, "");
      } else {
         AvMain.AA(super.AZ, 2);
         super.DB = super.AZ;
         super.AO = 1;
         super.cmdQuay.isPlayframe = true;
      }

      this.AJ();
      super.BM = null;
      this.AE = false;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         return;
      case 1:
         GlobalService.getInstance().AN((byte)var2);
         this.AE = !this.AE;
         if (var2 == 4) {
            super.AX = 4;
            return;
         } else {
            super.AX = 3;
         }
      case 0:
      default:
      }
   }

   public final void AK() {
      for(int var1 = 0; var1 < LuckyScreen.BJ.size(); ++var1) {
         MainItem var2 = (MainItem)LuckyScreen.BJ.elementAt(var1);
         if (var1 < 16) {
            var2.AC = super.AQ + CRes.getcos(CRes.fixangle(var1 * 360 / 16)) * 66 / 1000;
            var2.AD = super.AR + CRes.getsin(CRes.fixangle(var1 * 360 / 16)) * 66 / 1000;
         } else {
            var2.AC = super.AQ + 1 + (CRes.getcos(CRes.fixangle(30 + var1 * 360 / 6)) << 5) / 1000;
            var2.AD = super.AR + (CRes.getsin(CRes.fixangle(30 + var1 * 360 / 6)) << 5) / 1000;
         }
      }

   }

   public final void AF() {
      if (LuckyScreen.BF == null) {
         LuckyScreen.BF = new mImage[6];

         for(int var1 = 0; var1 < LuckyScreen.BF.length; ++var1) {
            LuckyScreen.BF[var1] = mImage.createImage("/interface/lucky" + var1 + ".png");
         }
      }

      if (super.BC == null) {
         super.BC = new FrameImage(mImage.createImage("/interface/oc_sen.png"), 28, 27);
         super.BD = new FrameImage(mImage.createImage("/interface/lucky6.png"), 40, 42);
         super.BE = new FrameImage(mImage.createImage("/interface/lucky7.png"), 40, 42);
      }

   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      if (!GameCanvas.isSmallScreen) {
         AvMain.AB(var1, super.AK, super.AL, super.AM, super.AN, 0);
      }

      AvMain.FontBorderColor(var1, this.AB, MotherCanvas.hw, super.AL - 20, 2, (int)6, (int)5);
      this.AB(var1);

      for(int var2 = 0; var2 < super.AI.size(); ++var2) {
         iCommand var3;
         (var3 = (iCommand)super.AI.elementAt(var2)).paint(var1, var3.xCmd, var3.yCmd);
      }

      MainTab.AA(var1, MotherCanvas.w - 78, 4 + GameScreen.h12plus, false);
      this.AA(var1, 0);
   }

   public final void AB(mGraphics var1) {
      if (LuckyScreen.BF == null) {
         this.AF();
      } else {
         MainImage var2;
         if (AG == null && (var2 = ObjectData.getImageAll((short)800, ObjectData.HashImageOtherNew, (short)23000)) != null) {
            AG = var2.img;
         }

         if (AG != null) {
            var1.drawRegion((mImage)AG, super.AS, super.AT, 3);
         }

         AvMain.AB(var1, "" + this.AL(), super.AS + 1, super.AT + 13, 2, (int)5);
         int var3;
         int var4;
         int var5;
         if ((super.AY == 3 || super.AY == 4) && LuckyScreen.BJ != null) {
            for(var5 = -1; var5 < 2; ++var5) {
               var3 = super.AV + var5;
               var4 = LuckyScreen.BJ.size();
               if (var3 < 0) {
                  var3 += var4;
               } else if (var3 >= var4) {
                  var3 -= var4;
               }

               ((MainItem)LuckyScreen.BJ.elementAt(var3)).AA(var1, super.AQ, super.AR - var5 * 14, 24, 0, (byte)0);
            }
         }

         var5 = 0;
         var3 = 0;
         if (super.AY == 5) {
            var5 = super.BH[super.AP];
            var3 = super.BI[super.AP];
         }

         var1.drawRegion(LuckyScreen.BF[0], var5 * 95, 0, 95, 190, 0, super.AQ - 94, super.AR - 94, 0);
         var1.drawRegion(LuckyScreen.BF[0], var5 * 95, 0, 95, 190, 2, super.AQ, super.AR - 94, 0);
         var1.drawRegion(LuckyScreen.BF[1], 0, var3 * 99, 99, 99, 0, super.AQ, super.AR, 3);
         if (super.AY != 1 && super.AY != 0) {
            label99: {
               if (super.AY == 2) {
                  var4 = super.AR;
                  if (super.AW <= 23) {
                     var1.drawRegion(LuckyScreen.BF[2], 0, super.BG[super.AP] * 33, 33, 33, 0, super.AQ, var4, 3);
                     break label99;
                  }

                  var1.drawRegion(LuckyScreen.BF[4], 0, 33, 33, 33, 0, super.AQ, super.AR + 1, 3);
               }

               var1.drawRegion(LuckyScreen.BF[4], 0, 0, 33, 33, 0, super.AQ, super.AR, 3);
            }
         } else {
            var1.drawRegion(LuckyScreen.BF[4], 0, 33, 33, 33, 0, super.AQ, super.AR, 3);
         }

         if (super.AY != 5) {
            for(var4 = 0; var4 < LuckyScreen.BJ.size(); ++var4) {
               MainItem var6 = (MainItem)LuckyScreen.BJ.elementAt(var4);
               if (var4 < 16 && var4 % 2 != 0) {
                  AvMain.fraBorderSkill.drawFrame(0, var6.AC, var6.AD, 0, 3, var1);
               }

               if (var4 == super.AV) {
                  if (AF == null && (var2 = ObjectData.getImageAll((short)801, ObjectData.HashImageOtherNew, (short)23000)) != null) {
                     AF = var2.img;
                  }

                  if (AF != null) {
                     var1.drawRegion(AF, 0, 0, 27, 27, 0, var6.AC, var6.AD, 3);
                  }
               }

               if (this.BN != null && this.BN[var4] == 1) {
                  var1.drawRegion(LuckyScreen.BF[3], 0, 0, 33, 33, 0, var6.AC, var6.AD, 3);
               } else {
                  var6.AA(var1, var6.AC, var6.AD, 24, 0, (byte)0);
               }
            }
         }

         if (super.AY == 5) {
            var1.drawRegion(LuckyScreen.BF[2], 0, super.BG[super.AP] * 33, 33, 33, 0, super.AQ, super.AR, 3);
            if (super.AP % 2 == 0) {
               MainImage var7;
               if (AF == null && (var7 = ObjectData.getImageAll((short)801, ObjectData.HashImageOtherNew, (short)23000)) != null) {
                  AF = var7.img;
               }

               if (AF != null) {
                  var1.drawRegion((mImage)AF, super.AQ, super.AR, 3);
               }
            }

            ((MainItem)LuckyScreen.BJ.elementAt(super.AV)).AA(var1, super.AQ, super.AR, 24, 0, (byte)0);
         }

      }
   }

   public final void AG() {
      ++super.AW;
      if (super.AY == 1) {
         if (this.BO <= 0) {
            super.AY = 4;
            super.AW = 20;
            return;
         }

         if (super.AW == 5) {
            if (super.AX == 4) {
               super.AJ.addElement(GameScreen.AA((short)78, 1, this.AC.xCmd, this.AC.yCmd, super.AQ - 5, super.AR - 5));
            } else {
               super.AJ.addElement(GameScreen.AA((short)78, 2, super.cmdQuay.xCmd, super.cmdQuay.yCmd, super.AQ - 5, super.AR - 5));
            }
         }

         if (super.AW == 33) {
            super.AY = 2;
            super.AW = -10;
            super.AP = 0;
            super.AJ.addElement(GameScreen.AA((short)53, 0, super.AQ, super.AR, super.AQ, super.AR));
         }

         if (super.AW >= 100) {
            super.AY = 2;
            super.AW = -10;
            super.AP = 0;
            super.AJ.addElement(GameScreen.AA((short)53, 0, super.AQ, super.AR, super.AQ, super.AR));
         }
      } else if (super.AY == 2) {
         if (super.AW >= 0) {
            ++super.AP;
         }

         if (super.AP >= super.BG.length) {
            super.AP = super.BG.length - 1;
         }

         if (super.AW >= 30) {
            super.AY = 3;
            super.AW = -5;
            super.AP = 0;
         }
      } else {
         float var10000;
         int var1;
         if (super.AY == 3) {
            if (super.AW >= 0) {
               var1 = super.AV;
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

               if (super.AV != var1) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }
            }

            if (super.AP % 15 == 0) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
            }

            if (super.AW >= 100) {
               super.AY = 4;
               super.AW = 0;
            }
         } else {
            MainItem var3;
            if (super.AY == 4) {
               if (super.AW >= 0) {
                  var1 = super.AV;
                  if (super.AW < 10) {
                     if (super.AW % 4 == 0) {
                        this.AH();
                     }
                  } else if (super.AW % 6 == 0) {
                     this.AH();
                  }

                  if (super.AV != var1) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                  }
               }

               if (super.AW > 15 && super.AV == this.AD) {
                  var3 = (MainItem)LuckyScreen.BJ.elementAt(this.AD);
                  super.AJ.addElement(GameScreen.AA((short)53, 0, var3.AC, var3.AD, var3.AC, var3.AD));
                  super.AY = 5;
                  super.AJ.addElement(GameScreen.AA((short)78, 1, var3.AC, var3.AD, super.AQ, super.AR));
                  super.AW = 0;
               }

               if (super.AP % 15 == 0) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }
            } else if (super.AY == 5) {
               if (super.AW >= 0 && super.AW % 4 == 0) {
                  ++super.AP;
               }

               if (super.AP >= super.BH.length) {
                  super.AP = super.BH.length - 1;
               }

               if (super.AW == 24) {
                  super.AJ.addElement(GameScreen.AA((short)53, 0, super.AQ, super.AR, super.AQ, super.AR));
               }

               MainImage var2;
               if (super.AW == 34 && (var2 = (var3 = (MainItem)LuckyScreen.BJ.elementAt(this.AD)).getImage()) != null && var2.img != null) {
                  if (var2.frame == -1) {
                     var2.set_Frame();
                  }

                  this.AA(" x " + Interface_Game.AA(var3.AB), super.AQ, super.AR, (byte)3, new FrameImage(var2.img, var2.frame), 0);
               }

               if (super.AW == 70) {
                  super.AY = 0;
                  super.AW = 0;
                  if (this.AD >= 0 && this.AD < this.BN.length) {
                     this.BN[this.AD] = 1;
                  }

                  if (this.BO > 0) {
                     this.AH();
                  }
               }
            }
         }
      }

      if (LuckyScreen.BJ != null) {
         if (super.AV >= LuckyScreen.BJ.size()) {
            super.AV = 0;
         }

         if (super.AV < 0) {
            super.AV = LuckyScreen.BJ.size() - 1;
         }
      }

   }

   public final void AA(byte[] var1) {
      this.BN = var1;

      for(int var2 = 0; var2 < this.BN.length; ++var2) {
         if (this.BN[var2] == 0) {
            ++this.BO;
         }
      }

   }

   public final void AA(byte var1) {
      if (this.AD != var1) {
         this.AD = var1;
         --this.BO;
         float var10000 = mSound.volumeSound;
         mSound.playSound();
         super.AY = 1;
         super.AW = 0;
      }
   }

   private void AH() {
      do {
         super.AV += this.AE ? 1 : -1;
         if (super.AV >= LuckyScreen.BJ.size()) {
            super.AV = 0;
         }

         if (super.AV < 0) {
            super.AV = LuckyScreen.BJ.size() - 1;
         }
      } while(this.BN[super.AV] == 1);

   }

   public final int AL() {
      if (this.BP == null || LuckyScreen.BL) {
         for(int var1 = 0; var1 < Player.vecInventory.size(); ++var1) {
            MainItem var2;
            if ((var2 = (MainItem)Player.vecInventory.elementAt(var1)).typeObject == 4 && var2.ID == 441) {
               this.BP = var2;
               LuckyScreen.BL = false;
               break;
            }
         }
      }

      return this.BP != null ? this.BP.numPotion : 0;
   }
}
