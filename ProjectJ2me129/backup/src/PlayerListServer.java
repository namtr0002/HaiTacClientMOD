public final class PlayerListServer extends Class_GE {
   private iCommand AB;
   private iCommand AC;
   private iCommand AD;
   private iCommand AE;
   private iCommand AF;
   private byte AG;
   public static PlayerListServer instance;
   private int AH = 0;
   private int AI = 0;

   public PlayerListServer(byte var1, mVector var2, String var3, byte var4) {
      super(var1, var2, var3, 200, 180);
      this.AG = var4;
      this.AB = new iCommand(T.DS, 12, this);
      this.AC = new iCommand(T.DT, 13, this);
      this.AD = new iCommand(T.DR, 14, this);
      this.AE = new iCommand(T.SH, 15, this);
      this.AF = new iCommand(T.DA, 17, this);
      if (GameCanvas.isTouch) {
         super.BB.setPos(super.BM + 20 + super.BO - 34, super.BN - 10, MainTab.fraCloseTab, "");
         super.DB = super.BB;
      }

      if (var1 == 15) {
         this.AE = AvMain.AA(this.AE, 0);
         this.AF = AvMain.AA(this.AF, 1);
         super.center = this.AE;
         super.DA = this.AF;
         super.AS.removeAllElements();
         super.AS.addElement(super.BB);
         if (!GameCanvas.isTouch) {
            super.AS.addElement(this.AE);
         }

         super.AS.addElement(this.AF);
      } else {
         this.AA(GameCanvas.hCommand + 12, super.AS);
      }
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 12:
         GlobalService.getInstance().AA((byte)3, (byte)super.BL, (byte)((byte)(this.AG + 1)));
         return;
      case 13:
         GlobalService.getInstance().AA((byte)3, (byte)super.BL, (byte)((byte)(this.AG - 1)));
         return;
      case 14:
         GameCanvas.gameScr.BA.AD();
         super.BI = true;
         return;
      case 15:
         this.AI();
         break;
      case 16:
         if (super.AU != null) {
            int var3 = super.AU.AN;

            for(int var4 = 0; var4 < super.AY.size(); ++var4) {
               Class_DS var5;
               if ((var5 = (Class_DS)super.AY.elementAt(var4)).AN == var2) {
                  var5.AN = var3;
               }
            }

            super.AU.AN = var2;
         }
         break;
      case 17:
         CRes.AA(super.AY);
         GlobalService.getInstance().AA((byte)6, (mVector)super.AY);
         super.BB.AD();
      }

      super.commandPointer(var1, var2);
   }

   private void AI() {
      if (super.AU != null) {
         mVector var1 = new mVector();
         int var2 = super.AY.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            iCommand var4 = new iCommand(T.SI + (var3 + 1), 16, var3, this);
            var1.addElement(var4);
         }

         GameCanvas.menuCur.startAt(var1, 2, T.SH);
      }

   }

   public final void AA() {
      if (super.AY.size() != 0) {
         super.AU = (Class_DS)super.AY.elementAt(super.AW);
         if (super.BL == 15) {
            this.AI();
         } else {
            if (super.AU != null && super.AU.AG != GameScreen.player.ID) {
               mVector var1 = new mVector();
               if (super.AU.AP == 1) {
                  var1.addElement(super.BC);
               }

               if (super.BL == 2) {
                  var1.addElement(super.BF);
               }

               if (var1.size() > 0) {
                  GameCanvas.menu.startAt(var1, 2, super.AU.AC);
               }
            }

         }
      }
   }

   public final void AB() {
      mVector var1 = new mVector();
      String var2 = T.AU;
      if (super.AY.size() > 0) {
         super.AU = (Class_DS)super.AY.elementAt(super.AW);
         if (super.AU != null && !GameCanvas.isTouch) {
            var1.addElement(super.BC);
            var2 = super.AU.AC;
         }
      }

      if (super.BL != 7 && super.BL != 6 && super.BL != 4 && super.BL != 11 && super.BL != 12 && super.BL != 13 && super.BL != 14 && super.BL != 17 && super.BL != 16) {
         if (super.BL == 2) {
            var1.addElement(this.AD);
            if (!GameCanvas.isTouch) {
               var1.addElement(super.BF);
            }
         } else if (super.BL == 15) {
            var1.addElement(this.AE);
         }
      } else {
         var1.addElement(this.AB);
         var1.addElement(this.AC);
      }

      GameCanvas.menu.startAt(var1, 2, var2);
   }

   public final void AA(mGraphics var1, Class_DS var2, int var3, int var4, int var5, int var6) {
      if (var2 != null) {
         var1.setColor(-4942486);
         var1.fillRect(var3, var4 - 1, 28, 29);
         var1.fillRect(var3 + 32, var4 - 1, super.BO - 32 - 60, 29);
         var1.setColor(-7373732);
         var1.fillRect(var3 + 1, var4, 26, 27);
         var1.fillRect(var3 + 32 + 1, var4, super.BO - 34 - 60, 27);
         if (super.BL == 15) {
            mFont.tahoma_7b_white.drawString(var1, String.valueOf(var2.AN + 1), var3 + 14, var4 + 8, 2);
         } else if (var2.AN < 10) {
            if (AvMain.GZ == null) {
               AvMain.GZ = new FrameImage(mImage.createImage("/interface/icontop.png"), 24, 24);
            } else if (var2.AN < 3) {
               AvMain.GZ.drawFrame(var2.AN, var3 + 14, var4 + 14, 0, 3, var1);
            } else {
               AvMain.GZ.drawFrame(3, var3 + 14, var4 + 14, 0, 3, var1);
            }
         } else {
            mFont.tahoma_7b_white.drawString(var1, String.valueOf(var2.AN + 1), var3 + 14, var4 + 8, 2);
         }

         var3 += 32;
         if (AA(super.BL)) {
            MainImage var11;
            if ((var11 = Potion.getIconClan(var2.AA)) != null && var11.img != null) {
               if (var11.frame == -1) {
                  var11.set_Frame();
               }

               if (var11.frame <= 1) {
                  var1.drawRegion((mImage)var11.img, var3 + 10, var4 + 8, 3);
               } else {
                  byte var12;
                  if (this.AI >= var11.frame - 1) {
                     var12 = 15;
                  } else {
                     var12 = 3;
                  }

                  if (CRes.abs(GameCanvas.gameTick - this.AH) > var12) {
                     ++this.AI;
                     if (this.AI >= var11.frame) {
                        this.AI = 0;
                     }

                     this.AH = GameCanvas.gameTick;
                  }

                  var1.drawRegion(var11.img, 0, this.AI * var11.AB, var11.AB, var11.AB, 0, var3 + 10, var4 + 8, 3);
               }
            }

            mFont.tahoma_7b_black.drawString(var1, var2.AC, var3 + 20, var4 + 2, 0);
            mFont.tahoma_7_black.drawString(var1, var2.AD, var3 + 2, var4 + 1 + GameCanvas.hText, 0);
         } else {
            String var10;
            if (super.BL == 15) {
               var10 = var2.AC;
               if (var2.AZ) {
                  AvMain.FontBorderColor(var1, var10, var3 + 31 + 20, var4, 2, (int)0, (int)7);
               } else {
                  mFont.tahoma_7b_black.drawString(var1, var10, var3 + 30 + 20, var4, 2);
               }

               mFont.tahoma_7_black.drawString(var1, var2.AD, var3 + 25 + 25, var4 + GameCanvas.hText, 2);
            } else {
               var10 = var2.AC;
               AvMain.fraStatusOnline.drawFrame(var2.AP, var3 + 25, var4 + 5, 0, 3, var1);
               if (var2.AZ) {
                  AvMain.FontBorderColor(var1, var10, var3 + 31, var4, 0, (int)0, (int)7);
               } else {
                  mFont.tahoma_7b_black.drawString(var1, var10, var3 + 30, var4, 0);
               }

               var1.AD(var3 + 25, super.BK, 80, 100);
               mFont.tahoma_7_black.drawString(var1, var2.AD, var3 + 25, var4 + GameCanvas.hText, 0, true);
               var1.translate(-var1.getTranslateX(), -var1.getTranslateY());
               var1.AD(super.BM, super.BN + GameCanvas.hCommand + super.AV, super.BO - 30, super.AR - super.AV);
               var1.translate(0, -super.AT.AC);
               MainObject.AB(var1, var2.AV, var2.AW, var2.AX, var3 + 10, var4 + super.BQ / 2 + 32, 0);
            }
         }
      }
   }

   public final void AB(mGraphics var1) {
      AvMain.AB(var1, super.BM, super.BN - 20, super.BO, super.BP + 20, 1);
      mFont.tahoma_7b_brown.drawString(var1, super.AZ, super.BM + super.BO / 2, super.BN + GameCanvas.hCommand / 2 + 3, 2);
      if (super.BL != 15) {
         mFont.tahoma_7b_black.drawString(var1, T.PL + (this.AG + 1), super.BM + super.BO / 2, super.BN + super.BP - GameCanvas.hCommand / 2 - 20, 2);
      }

   }

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      var1.setColor(-4840926);
      var1.fillRect(var2, var3 + super.AW * super.BQ, 29, 30);
      var1.fillRect(var2 + 31, var3 + super.AW * super.BQ, super.BO - 32 - 60 + 1, 30);
   }

   public static boolean AA(byte var0) {
      return var0 == 6 || var0 == 11 || var0 == 12 || var0 == 14 || var0 == 13;
   }
}
