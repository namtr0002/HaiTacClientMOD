public final class iCommand {
   public String caption;
   public String[] subCaption;
   public IAction action;
   public AvMain Pointer;
   public int indexMenu;
   public int AF = -1;
   public boolean AG = false;
   public int AH = 0;
   private int BA;
   private int BB;
   public int timeSelect;
   public FrameImage AJ;
   private FrameImage BC;
   public int AK;
   private int BD;
   private int BE;
   public int AL;
   private int BF;
   public int xCmd = -1;
   public int yCmd = -1;
   public int frameCmd;
   public static int wButtonCmd = 70;
   public static int hButtonCmdNor = 30;
   public static int hButtonCmdSpec = 46;
   private byte BG = 0;
   public static byte AS = 0;
   private static byte BH = 1;
   private static byte BI = 2;
   private static byte BJ = 3;
   private static byte BK = 4;
   private boolean BL = false;
   public boolean AT = false;
   public boolean isDonotCloseMenu = false;
   public boolean isPlayframe = false;
   private mFont BM;
   public MainItem AW;
   private short BN = -1;
   public short AX;
   private CountDownTicket BO = new CountDownTicket();
   public boolean isDisplay = false;
   public boolean isSaved = false;
   private int BP;
   private int BQ;
   private int BR;
   private int BS;
   private int BT = 0;

   public iCommand(String var1, IAction var2) {
      this.caption = var1;
      this.action = null;
   }

   public iCommand(String var1, int var2) {
      this.caption = var1;
      this.indexMenu = (byte)var2;
   }

   public iCommand(String var1, int var2, AvMain var3) {
      this.caption = var1;
      this.indexMenu = (byte)var2;
      this.Pointer = var3;
   }

   public iCommand(String var1, int var2, int var3, AvMain var4) {
      this.caption = var1;
      this.indexMenu = var2;
      this.AF = var3;
      this.Pointer = var4;
   }

   public final void setTypeSpec() {
      this.subCaption = mFont.tahoma_7b_white.splitFontArray(this.caption, wButtonCmd - 4);
      this.BG = BH;
   }

   public final void AB() {
      this.BG = BK;
   }

   public final void AC() {
      this.BG = BJ;
   }

   public final void AA(byte var1) {
      this.BG = var1;
   }

   public final void AA(boolean var1) {
      this.BL = true;
   }

   public final void AA(mFont var1) {
      this.BM = var1;
   }

   public final void AA(int var1) {
      this.BO.setCountDown(var1);
   }

   public final void AA(short var1) {
      this.BN = var1;
      this.AK = 20;
      this.BD = 20;
      this.BT = 4;
      if (GameCanvas.isSmallScreen) {
         this.BE = mFont.tahoma_7_white.getWidth(this.caption);
      } else {
         this.BE = mFont.tahoma_7b_white.getWidth(this.caption);
      }
   }

   public final void AA(MainItem var1) {
      this.AW = var1;
      this.AK = 20;
      this.BD = 20;
      this.BT = 4;
      if (GameCanvas.isSmallScreen) {
         this.BE = mFont.tahoma_7_white.getWidth(this.caption);
      } else {
         this.BE = mFont.tahoma_7b_white.getWidth(this.caption);
      }
   }

   public final void AA(int var1, int var2, int var3, int var4) {
      this.BP = var1;
      this.BQ = var2;
      this.BR = var3;
      this.BS = var4;
      this.BG = BI;
   }

   public final void AA(FrameImage var1) {
      this.AJ = var1;
      this.AK = this.AJ.frameWidth;
      this.BD = this.AJ.frameHeight;
      this.BA = this.AJ.nFrame;
      this.BB = 0;
      if (GameCanvas.isSmallScreen) {
         this.BE = mFont.tahoma_7_white.getWidth(this.caption);
      } else {
         this.BE = mFont.tahoma_7b_white.getWidth(this.caption);
      }
   }

   public final void setFraCaption(FrameImage var1, int var2, int var3, int var4) {
      this.AJ = var1;
      this.AK = this.AJ.frameWidth;
      this.BD = this.AJ.frameHeight;
      this.BT = var4;
      this.BA = var2;
      this.BB = var3;
      if (GameCanvas.isSmallScreen) {
         this.BE = mFont.tahoma_7_white.getWidth(this.caption);
      } else {
         this.BE = mFont.tahoma_7b_white.getWidth(this.caption);
      }
   }

   public final void AA(int var1, int var2, mImage var3, String var4, int var5) {
      FrameImage var6 = new FrameImage(var3, mImage.getImageWidth(var3.image), mImage.getImageHeight(var3.image) / 2);
      this.setPos(var1, var2, var6, var4);
   }

   public final void setPos(int var1, int var2, FrameImage var3, String var4) {
      this.caption = var4;
      this.xCmd = var1;
      this.yCmd = var2;
      this.BC = var3;
      if (this.BC != null) {
         this.AL = this.BC.frameWidth;
         this.BF = this.BC.frameHeight;
         if (this.AL < 28) {
            this.AL = 28;
         }

         if (this.BF < 28) {
            this.BF = 28;
            return;
         }
      } else {
         this.AL = wButtonCmd;
         this.BF = hButtonCmdNor;
         if (this.BG == BH) {
            this.BF = hButtonCmdSpec;
         }
      }

   }

   public final void AA(int var1, int var2) {
      this.xCmd = var1;
      this.yCmd = var2;
   }

   public final void AD() {
      this.timeSelect = 5;
      float var10000;
      if (this.action != null) {
         IAction var1 = this.action;
         GameCanvas.isPointerSelect = false;
         GameCanvas.AH();
         GameCanvas.AG();
         var10000 = mSound.volumeSound;
         mSound.playSound();
      } else {
         var10000 = mSound.volumeSound;
         mSound.playSound();
         if (this.Pointer != null) {
            this.Pointer.commandPointer(this.indexMenu, this.AF);
            GameCanvas.isPointerSelect = false;
            GameCanvas.AH();
            GameCanvas.AG();
         } else if (GameCanvas.currentDialog != null) {
            GameCanvas.currentDialog.AC(this.indexMenu, this.AF);
            GameCanvas.isPointerSelect = false;
            GameCanvas.AH();
            GameCanvas.AG();
         } else if (Class_IO.AB) {
            Class_IO.AA().AC(this.indexMenu, this.AF);
         } else if (GameCanvas.subDialog != null) {
            GameCanvas.subDialog.AC(this.indexMenu, this.AF);
         } else {
            GameCanvas.currentScreen.AC(this.indexMenu, this.AF);
         }
      }
   }

   public final void paint(mGraphics g, int x, int y) {
      if (this.timeSelect > 0) {
         --this.timeSelect;
      }

      int var4 = this.frameCmd;
      if (this.isPlayframe && this.frameCmd == 0) {
         var4 = GameCanvas.gameTick / 8 % 2;
      }

      if (this.AF()) {
         if (this.BC != null) {
            this.BC.drawFrame(var4, this.xCmd, this.yCmd, 0, 3, g);
         } else {
            this.AD(g, this.xCmd, this.yCmd);
         }

         this.AA((mGraphics)g, this.xCmd, this.yCmd - 6, (int)2);
      } else {
         if (this.BC != null) {
            this.BC.drawFrame(var4, x, y, 0, 3, g);
         } else {
            this.AD(g, x, y);
         }

         this.AA((mGraphics)g, x, y - 6, (int)2);
      }
   }

   public final void AA(mGraphics var1) {
      if (this.timeSelect > 0) {
         --this.timeSelect;
      }

      int var2 = this.frameCmd;
      if (this.isPlayframe && this.frameCmd == 0) {
         var2 = GameCanvas.gameTick / 8 % 2;
      }

      if (var2 == 0) {
         this.BC.drawFrame(var2, this.xCmd, this.yCmd, 0, 3, var1);
      } else {
         int var5 = this.yCmd;
         int var4 = this.xCmd;
         byte var6 = 0;
         int var7 = (int)(20.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
         int var8 = (int)(4.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
         int var9 = (int)(8.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
         if (this.AG || this.timeSelect > 0) {
            if (GameCanvas.gameTick % var7 < var8) {
               var6 = 0;
            } else if (GameCanvas.gameTick % var7 < var9) {
               var6 = 1;
            } else {
               var6 = 2;
            }
         }

         AvMain.AA(var1, var4 - 26, var5 - 10, 52, 20, var6);
      }

      this.AA((mGraphics)var1, this.xCmd, this.yCmd - 6, (int)2);
   }

   public final void AB(mGraphics var1, int var2, int var3) {
      if (this.timeSelect > 0) {
         --this.timeSelect;
      }

      int var4 = this.frameCmd;
      if (this.isPlayframe && this.frameCmd == 0) {
         var4 = GameCanvas.gameTick / 8 % 2;
      }

      if (this.BC != null) {
         this.BC.drawFrame(var4, var2, var3, 0, 3, var1);
      } else {
         this.AD(var1, var2, var3);
      }

      this.AA((mGraphics)var1, var2, var3 - 6, (int)2);
   }

   public final void AC(mGraphics var1, int var2, int var3) {
      this.AA((mGraphics)var1, var2, var3, (int)2);
   }

   private void AA(mGraphics var1, int var2, int var3, int var4) {
      if (!this.BL || GameCanvas.gameTick % 20 <= 15) {
         if (this.BG == BH) {
            var4 = this.subCaption.length;

            for(int var6 = 0; var6 < var4; ++var6) {
               if (var4 == 3) {
                  AvMain.AA(var1, this.subCaption[var6], var2, var3 - hButtonCmdSpec / 7 * (var4 - 1) + var6 * ((hButtonCmdSpec << 1) / 7), 2, (byte)0);
               } else {
                  AvMain.AA(var1, this.subCaption[var6], var2, var3 - hButtonCmdSpec / 5 * (var4 - 1) + var6 * ((hButtonCmdSpec << 1) / 5), 2, (byte)0);
               }
            }

         } else {
            label65: {
               var4 = 0;
               if (this.AJ != null) {
                  this.AJ.drawFrame(this.BB + GameCanvas.BJ % this.BA, var2 - this.BE / 2 - 1, var3 + this.BD / 2 - 1 - this.BT, 0, 3, var1);
               } else {
                  if (this.AW == null) {
                     break label65;
                  }

                  this.AW.AA(var1, var2 - this.BE / 2 - 1, var3 + this.BD / 2 - 1 - this.BT, 0, 0, (byte)7);
                  if (this.AK == 20 && this.AW.AF() != null && this.AW.AF().img != null) {
                     this.AK = mImage.getImageWidth(this.AW.AF().img.image);
                     if (this.AK == 20) {
                        this.AK = 21;
                     }
                  }
               }

               var4 = this.AK / 2 + 1;
            }

            String var5 = this.caption;
            if (this.BO.timeCountDown > 0) {
               var5 = CountDownTicket.timeShow(this.BO.timeCountDown);
               this.BO.updateTimeCountDownTicket();
            }

            if (this.BG == BK) {
               AvMain.AA(var1, var5, var2 + var4, var3, 2, (byte)1);
            } else if (this.BG == BJ) {
               AvMain.AA(var1, var5, var2 + var4, var3, 2, (byte)6);
            } else if (this.BG == BI) {
               AvMain.AA(var1, var5, var2 + var4, var3, 2, (byte)5);
            } else if (GameCanvas.isSmallScreen) {
               mFont.tahoma_7_white.drawString(var1, var5, var2 + var4, var3, 2);
            } else if (this.isDisplay) {
               AvMain.AA(var1, var5, var2 + var4, var3, 2, (byte)5);
            } else {
               AvMain.AA(var1, var5, var2 + var4, var3, 2, (byte)0);
            }
         }
      }
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4, boolean var5) {
      if (this.caption != null) {
         int var6 = 0;
         if (this.AJ != null) {
            if (var4 == 2) {
               this.AJ.drawFrame(this.BB + GameCanvas.BJ % this.BA, var2 - this.BE / 2 - 1, var3 + this.BD / 2 - 1 - this.BT, 0, 3, var1);
               var6 = this.AK / 2 + 1;
            } else if (var4 == 0) {
               this.AJ.drawFrame(this.BB + GameCanvas.BJ % this.BA, var2 + this.AK / 2, var3 + this.BD / 2 - 1 - this.BT, 0, 3, var1);
               var6 = this.AK + 6;
            }
         } else if (this.AW != null) {
            if (var4 == 2) {
               this.AW.AA(var1, var2 - this.BE / 2 - 1, var3 + this.BD / 2 - 1 - this.BT, 0, 0, (byte)7);
               var6 = this.AK / 2 + 1;
            } else if (var4 == 0) {
               this.AW.AA(var1, var2 + this.AK / 2, var3 + this.BD / 2 - 1 - this.BT, 0, 0, (byte)7);
               var6 = this.AK + 6;
            }
         } else {
            MainImage var7;
            if (this.BN > 0 && (var7 = Potion.getIconClan(this.BN)) != null && var7.img != null) {
               if (var4 == 2) {
                  var1.drawRegion((mImage)var7.img, var2 - this.BE / 2 - 1, var3 + this.BD / 2 - 1 - this.BT, 3);
                  var6 = this.AK / 2 + 1;
               } else if (var4 == 0) {
                  var1.drawRegion((mImage)var7.img, var2 + this.AK / 2, var3 + this.BD / 2 - 1 - this.BT, 3);
                  var6 = this.AK + 6;
               }
            }
         }

         if (GameCanvas.isSmallScreen) {
            mFont.tahoma_7_black.drawString(var1, this.caption, var2 + var6, var3 - 1, var4);
         } else if (var5) {
            mFont.tahoma_7b_red.drawString(var1, this.caption, var2 + var6, var3 - 1, var4);
         } else {
            if (this.BM != null) {
               this.BM.drawString(var1, this.caption, var2 + var6, var3 - 1, var4);
            } else {
               mFont.tahoma_7b_black.drawString(var1, this.caption, var2 + var6, var3 - 1, var4);
            }

            if (this.AT && GameScreen.numMess > 0 && GameCanvas.gameTick % 10 < 8) {
               int var8 = mFont.tahoma_7b_black.getWidth(this.caption);
               var1.drawRegion((mImage)MainEvent.imgNew, var2 + var8 / 2 + 4, var3, 3);
            }

         }
      }
   }

   public final void AA(mGraphics var1, int var2, int var3, byte var4) {
      this.AJ.drawFrame(this.BB + var4 % this.BA, var2, var3, 0, 3, var1);
   }

   public final void AE() {
      if (GameCanvas.isTouch) {
         if (this.BG == BI) {
            if (GameCanvas.AB(this.BP, this.BQ, this.BR, this.BS)) {
               this.AD();
               GameCanvas.isPointerSelect = false;
               this.frameCmd = 0;
               return;
            }
         } else {
            int var1 = this.AL;
            if (this.AH > 0) {
               var1 -= this.AH * 6;
            }

            if (!GameCanvas.isPointerDown && !GameCanvas.AQ) {
               if (!GameCanvas.isTouchAndKey()) {
                  this.AG = false;
                  this.frameCmd = 0;
               }
            } else if (GameCanvas.isPoint(this.xCmd - var1 / 2 - 5, this.yCmd - this.BF / 2 - 5, var1 + 10, this.BF + 10)) {
               this.frameCmd = 1;
               this.AG = true;
            } else {
               this.frameCmd = 0;
               this.AG = false;
            }

            if (GameCanvas.AB(this.xCmd - var1 / 2 - 5, this.yCmd - this.BF / 2 - 5, var1 + 10, this.BF + 10)) {
               this.AD();
               GameCanvas.isPointerSelect = false;
               this.frameCmd = 0;
            }
         }
      }

   }

   public final void AB(int var1) {
      if (GameCanvas.isTouch) {
         if (this.BG == BI) {
            if (GameCanvas.AB(this.BP, this.BQ, this.BR, this.BS)) {
               this.AD();
               GameCanvas.isPointerSelect = false;
               this.frameCmd = 0;
               return;
            }
         } else {
            int var2 = this.AL;
            if (this.AH > 0) {
               var2 -= this.AH * 6;
            }

            if (!GameCanvas.isPointerDown && !GameCanvas.AQ) {
               if (!GameCanvas.isTouchAndKey()) {
                  this.AG = false;
                  this.frameCmd = 0;
               }
            } else if (GameCanvas.isPoint(this.xCmd - var2 / 2 - 5, this.yCmd - var1 - this.BF / 2 - 5, var2 + 10, this.BF + 10)) {
               this.frameCmd = 1;
               this.AG = true;
            } else {
               this.frameCmd = 0;
               this.AG = false;
            }

            if (GameCanvas.AB(this.xCmd - var2 / 2 - 5, this.yCmd - var1 - this.BF / 2 - 5, var2 + 10, this.BF + 10)) {
               this.AD();
               GameCanvas.isPointerSelect = false;
               this.frameCmd = 0;
            }
         }
      }

   }

   public final void AB(int var1, int var2) {
      int var3 = this.AL;
      if (this.AH > 0) {
         var3 -= this.AH * 6;
      }

      if (GameCanvas.isPointerDown && !GameCanvas.AQ) {
         if (GameCanvas.isPoint(var1 - var3 / 2 - 5, var2 - 10 - 5, var3 + 10, 30)) {
            this.frameCmd = 1;
            this.AG = true;
         } else {
            this.frameCmd = 0;
            this.AG = false;
         }
      } else if (!GameCanvas.isTouchAndKey() || GameCanvas.AQ) {
         this.AG = false;
         this.frameCmd = 0;
      }

      if (GameCanvas.AB(var1 - var3 / 2 - 5, var2 - 10 - 5, var3 + 10, 30)) {
         this.AD();
         GameCanvas.isPointerSelect = false;
         this.frameCmd = 0;
      }

   }

   public final void AC(int var1, int var2) {
      if (this.AF()) {
         if (GameCanvas.isPointerDown || GameCanvas.AQ) {
            if (GameCanvas.isPoint(this.xCmd - this.AL / 2 - 3 - var1, this.yCmd - this.BF / 2 - 3, this.AL + 6, this.BF + 6)) {
               this.frameCmd = 1;
               return;
            }

            this.frameCmd = 0;
            return;
         }

         this.frameCmd = 0;
      }

   }

   private void AD(mGraphics var1, int var2, int var3) {
      if (this.BG != BI) {
         int var4 = 24;
         int var5 = wButtonCmd - 2;
         if (this.AH > 0) {
            var5 -= 6 * this.AH;
         }

         if (this.BG == BH) {
            var4 = hButtonCmdSpec;
         } else if (24 > hButtonCmdNor) {
            var4 = hButtonCmdNor;
         }

         byte var6 = 0;
         if (this.AG || this.timeSelect > 0) {
            int var9 = (int)(20.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
            int var7 = (int)(4.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
            int var8 = (int)(8.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
            if (GameCanvas.gameTick % var9 < var7) {
               var6 = 0;
            } else if (GameCanvas.gameTick % var9 < var8) {
               var6 = 1;
            } else {
               var6 = 2;
            }
         }

         if (this.isSaved && !this.AG) {
            AvMain.AD(var1, var2 - var5 / 2, var3 - var4 / 2, var5, var4);
         } else {
            AvMain.AA(var1, var2 - var5 / 2, var3 - var4 / 2, var5, var4, var6);
         }
      }
   }

   public final boolean AF() {
      return this.xCmd >= 0 && this.yCmd >= 0;
   }
}
