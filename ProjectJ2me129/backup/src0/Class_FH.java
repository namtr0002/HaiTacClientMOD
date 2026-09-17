public final class Class_FH extends ChatDetail {
   private iCommand AA;
   private iCommand AB;
   private iCommand AC;
   private iCommand AD;
   private Class_DS AX;
   private mSystem AY;
   private int AZ;
   private int BA;

   public Class_FH(String var1, byte var2) {
      super(var1, (byte)3);
   }

   public final void AA(int var1, int var2, int var3, int var4, int var5, int var6) {
      super.AF = var1;
      super.AG = var2;
      super.AH = var3;
      super.AI = var4;
      super.AE = var5;
      super.AK = var6;
      super.AT = new TField(var1 + 60, var2 + var4 - TField.AB(), var3 - 60);
      super.AT.AA(T.CP);
      super.AT.AA(true);
      this.AA(0, (byte)0);
      this.AA = new iCommand(T.DY, 0, this);
      this.AB = new iCommand(T.CJ, 1, this);
      this.AC = new iCommand(T.NH, 2, this);
      this.AD = new iCommand(T.AD, 3, this);
      if (!GameCanvas.isTouch) {
         super.center = this.AD;
      }

   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (this.AX != null) {
            mVector var3;
            (var3 = new mVector()).addElement(this.AB);
            var3.addElement(this.AC);
            GameCanvas.Start_Normal_DiaLog(this.AX.AC + T.NI, var3, true);
            return;
         }
         break;
      case 1:
         GameCanvas.end_Dialog();
         if (this.AX != null) {
            GlobalService.getInstance().Clan_CMD((byte)7, this.AX.AC, (short)this.AX.AH, (byte)0);
            return;
         }
         break;
      case 2:
         GameCanvas.end_Dialog();
         if (this.AX != null) {
            GlobalService.getInstance().Clan_CMD((byte)16, this.AX.AC, (short)this.AX.AH, (byte)0);
            return;
         }
         break;
      case 3:
         this.f_();
      }

   }

   public final void AA() {
      super.AL = super.AN.size() - 1;

      for(int var1 = super.AL; var1 >= 0; --var1) {
         if (((mSystem)super.AN.elementAt(var1)).AI > 0) {
            super.AL = var1;
            break;
         }
      }

      this.AI();
      this.AH();
      this.AF();
   }

   public final void paint(mGraphics var1) {
      if (super.AT != null) {
         super.AT.paint(var1);
      }

      AvMain.paintRect(var1, super.AF, super.AG + super.AI - TField.AB(), 60 - super.AE, TField.AB() + 1, (byte)0, 1);
      if (GameCanvas.isTouch) {
         var1.drawRegion((mImage)AvMain.imgDonateClan, super.AF + 30 - super.AE / 2, super.AG + super.AI - TField.AB() + 2 + 7, 3);
         if (MainTab.BK.timeCountDown <= 0) {
            mFont.tahoma_7_white.drawString(var1, T.tangqua, super.AF + 30 - super.AE / 2, super.AG + super.AI - TField.AB() + 17, 2);
         } else {
            MainTab.BK.paintCountDownTicketHour(var1, mFont.tahoma_7_white, super.AF + 18 - super.AE / 2, super.AG + super.AI - TField.AB() + 17, 0);
         }
      } else if (MainTab.BK.timeCountDown <= 0) {
         var1.drawRegion((mImage)AvMain.imgDonateClan, super.AF + 10 - super.AE / 2, super.AG + super.AI - TField.AB() + 2 + 10, 3);
         mFont.tahoma_7_white.drawString(var1, T.tangqua, super.AF + 37 - super.AE / 2, super.AG + super.AI - TField.AB() + 17 - 12, 2);
      } else {
         var1.drawRegion((mImage)AvMain.imgDonateClan, super.AF + 16 - super.AE / 2, super.AG + super.AI - TField.AB() + 2 + 10, 3);
         MainTab.BK.paintCountDownTicketHour(var1, mFont.tahoma_7_white, super.AF + 39 - super.AE / 2, super.AG + super.AI - TField.AB() + 17 - 11, 0);
      }

      var1.AD(super.AF - super.AE, super.AG - 2, super.AH + (super.AE << 1), super.AI - (super.AT != null ? super.AT.AD : -super.AE) + 2);
      mGraphics.AC();
      int var10000 = super.AF;
      var10000 = super.AE;
      var10000 = super.AG;
      var10000 = super.AH;
      var10000 = super.AE;
      var10000 = super.AI;
      if (super.AT != null) {
         var10000 = super.AT.AD;
      } else {
         var10000 = super.AE;
      }

      mGraphics.AD();
      var1.translate(0, -super.AM.AC);
      this.AZ = super.AM.AC / GameCanvas.hText - 4;
      if (this.AZ < 0) {
         this.AZ = 0;
      }

      this.BA = this.AZ + super.AK;

      for(int var2 = this.AZ; var2 <= this.BA; ++var2) {
         if (var2 < super.AN.size() && var2 >= 0) {
            mSystem var3;
            if ((var3 = (mSystem)super.AN.elementAt(var2)).AI > 0) {
               this.AA(var1, var3.AG, var3.AF, super.AE, GameCanvas.hText * var3.AI - 2, super.AG + var2 * GameCanvas.hText, var2 == super.AL);
               if (var3.AG == 2) {
                  AvMain.AC(var3.AE).drawString(var1, var3.AA, super.AF + super.AE + 2, super.AG + var2 * GameCanvas.hText, 0);
               } else {
                  AvMain.FontBorderColor(var1, var3.AA, super.AF + super.AE + 2, super.AG + var2 * GameCanvas.hText + 1, 0, (int)var3.AE, (int)7);
               }

               if (var3.AH >= 0 && MainClan.AA()) {
                  AvMain.paintRect(var1, super.AF + super.AE + 2, super.AG + var2 * GameCanvas.hText + (var3.AI - 1) * GameCanvas.hText - 2, 30, GameCanvas.hText - 5, (byte)3, 1);
                  mFont.tahoma_7_white.drawString(var1, T.DY, super.AF + super.AE + 2 + 15, super.AG + var2 * GameCanvas.hText + (var3.AI - 1) * GameCanvas.hText - 2, 2);
               }
            } else if (var3.AJ > -1L) {
               mFont.tahoma_7_black.drawString(var1, var3.AC, super.AF - super.AE + super.AH, super.AG + var2 * GameCanvas.hText - 2, 1);
            } else {
               AvMain.AC(var3.AE).drawString(var1, var3.AA, super.AF + super.AE + 2, super.AG + var2 * GameCanvas.hText, 0);
            }

            if (var3.AB.length() > 0) {
               mFont.tahoma_7_white.drawString(var1, var3.AB, super.AF + super.AH - super.AE, super.AG + var2 * GameCanvas.hText + 1, 1);
            }
         }
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      super.paint(var1);
   }

   public final void update() {
      super.AM.AC();
      if (super.AT != null) {
         super.AT.update();
      }

      if (GameCanvas.gameTick % 500 == 0) {
         this.AF();
         if (super.AM.AC == super.AM.AD) {
            this.AI();
         }
      }

   }

   private void AF() {
      for(int var1 = 0; var1 < super.AN.size(); ++var1) {
         mSystem var2;
         if ((var2 = (mSystem)super.AN.elementAt(var1)).AJ > 0L) {
            var2.AA();
         }
      }

   }

   public final void handleKeyPress() {
      int var1 = super.AL;
      if (GameCanvas.AL[2]) {
         GameCanvas.AB(2);
         if (var1 > 0) {
            --var1;
         }
      } else if (GameCanvas.AL[8]) {
         GameCanvas.AB(8);
         if (var1 < super.AN.size() - 1) {
            ++var1;
         }
      }

      if (var1 != super.AL) {
         mSystem var2 = null;
         if (var1 < super.AL) {
            for(var1 = var1; var1 >= 0; --var1) {
               if ((var2 = (mSystem)super.AN.elementAt(var1)).AI > 0) {
                  super.AL = var1;
                  break;
               }
            }
         } else {
            for(var1 = var1; var1 < super.AN.size(); ++var1) {
               if ((var2 = (mSystem)super.AN.elementAt(var1)).AI > 0) {
                  super.AL = var1;
                  break;
               }
            }
         }

         this.AY = var2;
         super.DA = null;
         if (MainClan.AA()) {
            Class_FH var5 = this;
            if (super.AL >= 0 && super.AL < super.AN.size()) {
               for(int var4 = super.AL; var4 >= 0; --var4) {
                  mSystem var3;
                  if ((var3 = (mSystem)var5.AN.elementAt(var4)).AI > 0) {
                     if (var3.AH < 0) {
                        break;
                     }

                     if (var3.AH == 1) {
                        var5.AX = new Class_DS(var3.AK);
                        var5.AX.AC = var3.AD;
                        var5.AX.AH = var3.AL;
                        var5.DA = var5.AA;
                     }
                  }
               }
            }
         }

         this.AH();
      }

      super.handleKeyPress();
   }

   private void AH() {
      int var1 = super.AL;
      if (this.AY != null) {
         var1 += this.AY.AI;
      }

      var1 = var1 * GameCanvas.hText - super.AI / 4;
      super.AM.AA(var1);
   }

   public final void updatePointer() {
      super.AM.update_Pos_UP_DOWN();
      if (super.AT != null) {
         super.AT.updatePointer();
      }

      if (GameCanvas.isPointerSelect && super.AN.size() > 0 && GameCanvas.isPoint(super.AF, super.AG, super.AH, super.AI)) {
         GameCanvas.isPointerSelect = false;
         int var1;
         if ((var1 = (GameCanvas.AZ - super.AG + super.AM.AC) / GameCanvas.hText) >= 0 && var1 < super.AN.size()) {
            super.AL = var1;
            Class_FH var4 = this;
            if (super.AL >= 0 && super.AL < super.AN.size()) {
               for(int var2 = super.AL; var2 >= 0; --var2) {
                  mSystem var3;
                  if ((var3 = (mSystem)var4.AN.elementAt(var2)).AI > 0) {
                     var4.AL = var2;
                     if (var3.AH >= 0 && var3.AH == 1 && MainClan.AA() && (Player.QN == 0 || Player.QN == 1 || Player.QN == 2)) {
                        var4.AX = new Class_DS(var3.AK);
                        var4.AX.AC = var3.AD;
                        var4.AX.AH = var3.AL;
                        var4.AA.AD();
                        return;
                     }
                     break;
                  }
               }
            }
         }
      }

   }

   public final void AA(short var1, String var2, String var3, String var4, byte var5, byte var6, short var7, long var8) {
      if (var2.length() > 0) {
         if (var5 != 2) {
            var2 = var3 + "\n" + var2;
         } else {
            var2 = var3 + " " + var2;
         }

         mSystem[] var10;
         String[] var12;
         if ((var10 = ChatDetail.AA(var12 = mFont.tahoma_7b_white.splitFontArray(var2, GameCanvas.chatTabScr.AH - 12), (byte)0)) != null) {
            for(int var11 = 0; var11 < var10.length; ++var11) {
               if (var11 == 0) {
                  if (var5 != 2) {
                     var10[0].AB = var4;
                     if (var5 == 1) {
                        var10[0].AF = 1;
                     } else if (var5 == 0) {
                        var10[0].AF = 2;
                     }
                  }

                  var10[0].AI = (byte)(var10.length + 1);
                  var10[0].AG = var5;
                  var10[0].AH = var6;
                  var10[0].AD = var3;
                  var10[0].AK = var7;
                  if (var10[0].AA.compareTo(GameScreen.player.name) == 0) {
                     var10[0].AA = T.NZ;
                     var10[0].AE = 4;
                  }
               }

               var10[var11].AL = var1;
               super.AN.addElement(var10[var11]);
            }

            mSystem var13;
            (var13 = new mSystem("", (byte)0)).AJ = var8;
            var13.AA();
            var13.AL = var1;
            super.AN.addElement(var13);
         }

         this.AG();
         if (super.AV > 0) {
            this.AA(var12.length, (byte)1);
         }
      }

   }

   public final void AA(short var1) {
      boolean var2 = false;

      int var3;
      for(var3 = 0; var3 < super.AN.size(); ++var3) {
         if (((mSystem)super.AN.elementAt(var3)).AL == var1) {
            super.AN.removeElement(var3);
            --var3;
            var2 = true;
         } else if (var2) {
            break;
         }
      }

      var3 = super.AI;
      if (super.AT != null) {
         var3 -= super.AT.AD;
      }

      super.AV = super.AN.size() * GameCanvas.hText - var3;
      if (super.AV > 0 && super.AV != super.AM.AD) {
         super.AM.AD = super.AV;
      }

   }

   private void AI() {
      int var1 = 0;

      for(int var2 = super.AN.size(); var2 >= 0; --var2) {
         mSystem var3;
         if ((var3 = (mSystem)super.AN.elementAt(var2)) != null && var1 <= 30) {
            if (var3.AI > 0) {
               ++var1;
            }
         } else {
            super.AN.removeElement(var2);
         }
      }

      if (var1 > 30) {
         this.AG();
         if (super.AV > 0) {
            this.AA(0, (byte)0);
         }

         this.AH();
      }

   }
}
