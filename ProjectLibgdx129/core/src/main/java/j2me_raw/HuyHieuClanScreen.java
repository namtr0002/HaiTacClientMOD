public final class HuyHieuClanScreen extends LuckyScreen {
   public static HuyHieuClanScreen instance;
   private FrameImage fraRuong;
   private static mImage[] mImgHuyHieu;
   public MainItem potionQuay;
   public Potion potionNhan;
   public boolean isThanhCong = false;
   public static int numXuInput = 1;
   private InputDialog input;
   public boolean isContinue = false;
   public static boolean isRunningOpenXu = false;
   private MainImage imgNhan;
   private FrameImage fraNhan;
   private int tickRuong = 0;

   public HuyHieuClanScreen() {
      super.AY = 0;
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
      this.AF();
      super.AZ = new iCommand(T.close, -1, this);
      super.cmdQuay = new iCommand("", 1, this);
      super.cmdQuay.setPos(super.AS + 1, super.AT + super.AU * 5 / 2, super.BD, "");
      super.AI = new mVector();
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
   }

   public final void commandPointer(int var1, int var2) {
      (new StringBuffer("Vào commandPointer trong HuyHieuClanScreen:")).append(var1).toString();
      switch(var1) {
      case -1:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         return;
      case 1:
         if (this.potionQuay.numPotion <= 0) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.YR);
            return;
         }

         iCommand var3 = new iCommand(T.XF, 2, this);
         this.input = GameCanvas.Start_Input_Dialog(T.nhapsoluong, var3, true, T.YP);
         GameCanvas.subDialog = this.input;
         return;
      case 2:
         GameCanvas.end_Dialog();
         short var10000 = CRes.AC(this.input.tfInput.getText()) ? (short)Integer.parseInt(this.input.tfInput.getText()) : 1;
         numXuInput = var10000;
         if (var10000 > 0) {
            if (numXuInput > this.potionQuay.numPotion) {
               numXuInput = this.potionQuay.numPotion;
            }

            isRunningOpenXu = true;
            this.AN();
            return;
         } else {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.YQ);
         }
      case 0:
      default:
      }
   }

   public final void AF() {
      if (mImgHuyHieu == null) {
         mImgHuyHieu = new mImage[3];

         for(int var1 = 0; var1 < mImgHuyHieu.length; ++var1) {
            mImgHuyHieu[var1] = mImage.createImage("/interface/huyhieu" + var1 + ".png");
         }
      }

      super.BD = new FrameImage(mImage.createImage("/interface/lucky6.png"), 40, 42);
      this.fraRuong = new FrameImage(mImage.createImage("/interface/huyhieu3.png"), 12);
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      AvMain.AB(var1, super.AK, super.AL, super.AM, super.AN, 0);
      AvMain.FontBorderColor(var1, T.VH, MotherCanvas.hw, super.AL - 20, 2, (int)6, (int)5);
      this.AB(var1);
      int var2 = super.AT + super.AU * 3 / 2;
      AvMain.paintRect(var1, super.AS - 16, var2 - 16, 32, 32, (byte)1, 4);
      if (this.potionQuay != null) {
         this.potionQuay.AD(var1, super.AS, var2, 32);
      }

      if (isRunningOpenXu) {
         super.cmdQuay.frameCmd = 1;
      }

      for(var2 = 0; var2 < super.AI.size(); ++var2) {
         iCommand var3;
         (var3 = (iCommand)super.AI.elementAt(var2)).paint(var1, var3.xCmd, var3.yCmd);
      }

      MainTab.AA(var1, MotherCanvas.w - 78, 4 + GameScreen.h12plus, false);
      this.AA(var1, 0);
   }

   public final void AB(mGraphics var1) {
      if (mImgHuyHieu == null) {
         this.AF();
      } else {
         var1.drawRegion((mImage)mImgHuyHieu[2], super.AS, super.AT, 3);
         (new StringBuffer("numpotion quay ")).append(this.potionQuay.numPotion).toString();
         AvMain.AB(var1, "" + this.potionQuay.numPotion, super.AS + 1, super.AT + 13, 2, (int)5);
         mFont.tahoma_7b_black.drawString(var1, "0", super.AS + 1, super.AT + 23, 2);
         int var2 = 0;
         int var3 = 0;
         if (super.AY == 3) {
            var2 = super.BH[super.AP];
            var3 = super.BI[super.AP];
         }

         var1.drawRegion(mImgHuyHieu[0], var2 * 90, 0, 90, 180, 0, super.AQ - 89, super.AR - 89, 0);
         var1.drawRegion(mImgHuyHieu[0], var2 * 90, 0, 90, 180, 2, super.AQ, super.AR - 89, 0);
         var1.drawRegion(mImgHuyHieu[1], 0, var3 * 105, 105, 105, 0, super.AQ, super.AR, 3);
         this.fraRuong.drawFrame(this.tickRuong, super.AQ + 3, super.AR - 8, 0, 3, var1);
      }
   }

   public final void AG() {
      if (this.potionNhan != null) {
         this.imgNhan = Potion.getIconClan(this.potionNhan.idIcon);
      }

      int var1;
      if (this.imgNhan != null && this.imgNhan.img != null) {
         var1 = mImage.getImageWidth(this.imgNhan.img.image);
         if (mImage.getImageHeight(this.imgNhan.img.image) / 2 >= var1) {
            this.fraNhan = new FrameImage(this.imgNhan.img, var1, var1);
         }
      }

      ++super.AW;
      if (super.AY == 0) {
         this.tickRuong = super.AW / 4 % 6;
      } else {
         int var2;
         if (super.AY == 1) {
            this.tickRuong = 0;
            if ((var2 = (int)(5.0D / (1.0D + (double)(GameCanvas.percentUPCV + GameCanvas.CY) / 100.0D))) == 0) {
               var2 = 1;
            }

            if (super.AW == var2) {
               if (super.AX == 0) {
                  super.AJ.addElement(GameScreen.AA((short)78, 1, super.cmdQuay.xCmd, super.cmdQuay.yCmd, super.AQ - 5, super.AR - 5));
               } else {
                  super.AJ.addElement(GameScreen.AA((short)78, 2, super.BB.xCmd, super.BB.yCmd, super.AQ - 5, super.AR - 5));
               }
            }

            if ((var1 = (int)(33.0D / (1.0D + (double)(GameCanvas.percentUPCV + GameCanvas.CY) / 100.0D))) <= var2) {
               var1 = var2 + 1;
            }

            if (super.AW == var1) {
               super.AY = 2;
               super.AW = -10;
               super.AP = 0;
               super.AJ.addElement(GameScreen.AA((short)53, 0, super.AQ, super.AR, super.AQ, super.AR));
               super.BK = 0;
            }

            if ((var2 = (int)(100.0D / (1.0D + (double)(GameCanvas.percentUPCV + GameCanvas.CY) / 100.0D))) <= var1) {
               var2 = var1 + 1;
            }

            if (super.AW >= var2) {
               super.AY = 2;
               super.AW = -10;
               super.AP = 0;
               super.AJ.addElement(GameScreen.AA((short)53, 0, super.AQ, super.AR, super.AQ, super.AR));
               super.BK = 0;
               return;
            }
         } else {
            float var10000;
            if (super.AY == 2) {
               if (super.AW >= 0) {
                  ++super.AP;
               }

               if (super.AP >= super.BG.length) {
                  super.AP = super.BG.length - 1;
               }

               if ((var2 = (int)(10.0D / (1.0D + (double)(GameCanvas.percentUPCV + GameCanvas.CY) / 100.0D))) <= 0) {
                  var2 = 1;
               }

               if (super.AW >= var2) {
                  super.AY = 3;
                  super.AW = -5;
                  if (GameCanvas.percentUPCV != 0) {
                     super.AW = -2;
                  }

                  super.AP = 0;
               }

               if (super.AW >= 0) {
                  var1 = super.BK;
                  if (super.AW < 6) {
                     if (super.AW % 5 == 0) {
                        ++super.BK;
                     }
                  } else if (super.AW < 16) {
                     if (super.AW % 3 == 0) {
                        ++super.BK;
                     }
                  } else if (super.AW % 2 == 0) {
                     ++super.BK;
                  }

                  if (super.BK != var1) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                     return;
                  }
               }
            } else if (super.AY == 3) {
               if (super.AP % 15 == 0) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
               }

               if (GameCanvas.gameTick % 2 == 0) {
                  ++super.AV;
               }

               if (LuckyScreen.BJ != null && super.AV >= LuckyScreen.BJ.size()) {
                  super.AV = 0;
               }

               if (super.AW >= 0) {
                  ++super.AP;
               }

               if (super.AP >= super.BH.length) {
                  super.AP = super.BH.length - 1;
               }

               var2 = (int)(100.0D / (1.0D + (double)(GameCanvas.percentUPCV + GameCanvas.CY) / 100.0D));
               if (super.AW >= var2) {
                  super.AY = 4;
                  super.AW = 0;
                  return;
               }
            } else if (super.AY == 4) {
               super.BK = 0;
               if (super.AW < 36) {
                  this.tickRuong = super.AW / 3;
               }

               byte var3;
               if (super.AW == 36) {
                  Interface_Game.BN = true;
                  if (!this.isThanhCong) {
                     var10000 = mSound.volumeSound;
                     mSound.playSound();
                     var3 = 1;
                     if (GameCanvas.language == 1) {
                        var3 = 3;
                     }

                     this.AA((short)79, var3, super.AQ + 3, super.AR - 15 + 10, super.AQ + 3, super.AR - 15 + 10);
                     this.AA((short)77, 0, super.AQ + 3, super.AR - 15 + 10, super.AQ + 3, super.AR - 15 + 10);
                     this.isContinue = true;
                     this.AN();
                  } else {
                     (new StringBuffer("potionNhan cat ")).append(this.potionNhan.typeObject).append(" icon ").append(this.potionNhan.idIcon).toString();
                     this.AA((short)53, 0, super.AQ + 3, super.AR - 15, super.AQ + 3, super.AR - 15);
                     this.AA("", super.AQ + 3, super.AR - 15, (byte)3, this.fraNhan, 0);
                     this.isThanhCong = false;
                     this.isContinue = false;
                     isRunningOpenXu = false;
                  }
               }

               var3 = 100;
               if (GameCanvas.percentUPCV > 0) {
                  if (GameCanvas.percentUPCV <= 25) {
                     var3 = 60;
                  } else if (GameCanvas.percentUPCV <= 50) {
                     var3 = 40;
                  }
               }

               if (super.AW == var3) {
                  super.AY = 0;
                  super.AW = 0;
               }
            }
         }

      }
   }

   private void AA(short var1, int var2, int var3, int var4, int var5, int var6) {
      Effect_End var7 = new Effect_End(var1, (byte)var2, var3, var4, var5, var6, (byte)0, (MainObject)null);
      super.AJ.addElement(var7);
   }

   private void AN() {
      (new Thread(new HuyHieuClan(this))).start();
   }

   public final void stopOpenXu() {
      isRunningOpenXu = false;
      this.isContinue = false;
      super.cmdQuay.frameCmd = 0;
   }

   public final void updatePointerOpenXu() {
      if (GameCanvas.isPoint(super.cmdQuay.xCmd - 25, super.cmdQuay.yCmd - 25, 47, 47)) {
         if (isRunningOpenXu) {
            this.stopOpenXu();
         }
         GameCanvas.isPointerSelect = false;
      }

   }
}
