public final class SkinUpgradeScreen extends ScreenUpgrade {
   public static SkinUpgradeScreen instance;
   public static mVector vecSkin = new mVector();
   public static mVector vecDa = new mVector();
   private ListNew listDa;
   private int CT;
   private int CU;

   public SkinUpgradeScreen(byte var1, int var2, mVector var3, mVector var4) {
      super((byte)22, -1);
      (new StringBuffer("SkinUpgradeScreen type ")).append(22).append(" vecSkin ").append(vecSkin.size()).append(" vecDa ").append(vecDa.size()).toString();
      vecSkin = var3;
      vecDa = var4;
      super.AH = 0;
      this.CU = -1;
      super.AK = super.AB / 2;
      super.AK -= super.AK % super.AD;
      super.AL = super.AC - super.AG * 3 - 5;
      super.AM = super.AK / super.AD;
      super.AI = super.AE + super.AB / 4 - super.AK / 2 + 15;
      super.AJ = super.AF + super.AC / 2 - super.AL / 2;
      this.CT = super.AJ + super.AL / 2 + 4;
      super.AL = super.AL / 2 - 4;
      int var5 = ((vecSkin.size() - 1) / super.AM + 1) * super.AD - super.AL;
      super.AP = new ListNew(super.AI, super.AJ, super.AK, super.AL, 0, 0, var5, true);
      var5 = ((vecDa.size() - 1) / super.AM + 1) * super.AD - super.AL;
      this.listDa = new ListNew(super.AI, this.CT, super.AK, super.AL, 0, 0, var5, true);
      super.AO = false;
      ScreenUpgrade.valueTile = 0;
   }

   public final void AJ(mGraphics var1) {
      var1.AD(super.AI - 1, super.AJ + 1, super.AK + 2, super.AL - 1);
      mGraphics.AC();
      int var10000 = super.AI;
      var10000 = super.AJ;
      var10000 = super.AK;
      var10000 = super.AL;
      mGraphics.AD();
      var1.translate(super.AI, super.AJ);
      var1.translate(0, -super.AP.AC);

      int var2;
      MainItem var3;
      for(var2 = 0; var2 < vecSkin.size(); ++var2) {
         if ((var3 = (MainItem)vecSkin.elementAt(var2)).typeObject == 105) {
            var3.AC(var1, var2 % super.AM * super.AD + super.AD / 2, var2 / super.AM * super.AD + super.AD / 2, super.AD);
         }

         var3.AA(var1, var2 % super.AM * super.AD + super.AD / 2, var2 / super.AM * super.AD + super.AD / 2, super.AD);
         this.AA(var1, var2 % super.AM * super.AD + 3, var2 / super.AM * super.AD + super.AD - 2, var3);
         this.AA(var1, var3, var2 % super.AM * super.AD + super.AD / 2, var2 / super.AM * super.AD + super.AD / 2);
         if (super.AH == var2 && super.BE == 0) {
            var1.setColor(-1);
            var1.drawRect(var2 % super.AM * super.AD + 1, var2 / super.AM * super.AD + 1, super.AD - 2, super.AD - 2);
            if (!GameCanvas.isSmallScreen) {
               var1.drawRect(var2 % super.AM * super.AD + 2, var2 / super.AM * super.AD + 2, super.AD - 4, super.AD - 4);
            }
         }
      }

      if (Player.maxInventory % super.AM != 0) {
         for(var2 = Player.maxInventory; var2 < Player.maxInventory + (super.AM - Player.maxInventory % super.AM); ++var2) {
            var1.drawRegion(AvMain.imgDelay, 0, 0, super.AD - 1, super.AD - 1, 0, var2 % super.AM * super.AD + 1, var2 / super.AM * super.AD + 1, 0);
         }
      }

      var1.setColor(-2701384);

      for(var2 = 0; var2 < super.AM - 1; ++var2) {
         var1.fillRect(super.AD + var2 * super.AD, 1, 1, super.AD * ((Player.maxInventory - 1) / super.AM + 1));
      }

      for(var2 = 0; var2 <= (Player.maxInventory - 1) / super.AM + 1; ++var2) {
         var1.fillRect(1, var2 * super.AD, super.AK - 1, 1);
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      AvMain.paintRect(var1, super.AI, this.CT, super.AK, super.AL, (byte)0, 3);
      var1.AD(super.AI - 1, this.CT + 1, super.AK + 2, super.AL - 1);
      mGraphics.AC();
      var10000 = super.AI;
      var10000 = this.CT;
      var10000 = super.AK;
      var10000 = super.AL;
      mGraphics.AD();
      var1.translate(super.AI, this.CT);
      var1.translate(0, -this.listDa.AC);

      for(var2 = 0; var2 < vecDa.size(); ++var2) {
         (var3 = (MainItem)vecDa.elementAt(var2)).AA(var1, var2 % super.AM * super.AD + super.AD / 2, var2 / super.AM * super.AD + super.AD / 2, super.AD);
         this.AA(var1, var2 % super.AM * super.AD + 3, var2 / super.AM * super.AD + super.AD - 2, var3);
         this.AA(var1, var3, var2 % super.AM * super.AD + super.AD / 2, var2 / super.AM * super.AD + super.AD / 2);
         if (this.CU == var2 && super.BE == 0) {
            var1.setColor(-1);
            var1.drawRect(var2 % super.AM * super.AD + 1, var2 / super.AM * super.AD + 1, super.AD - 2, super.AD - 2);
            if (!GameCanvas.isSmallScreen) {
               var1.drawRect(var2 % super.AM * super.AD + 2, var2 / super.AM * super.AD + 2, super.AD - 4, super.AD - 4);
            }
         }
      }

      if (Player.maxInventory % super.AM != 0) {
         for(var2 = Player.maxInventory; var2 < Player.maxInventory + (super.AM - Player.maxInventory % super.AM); ++var2) {
            var1.drawRegion(AvMain.imgDelay, 0, 0, super.AD - 1, super.AD - 1, 0, var2 % super.AM * super.AD + 1, var2 / super.AM * super.AD + 1, 0);
         }
      }

      var1.setColor(-2701384);

      for(var2 = 0; var2 < super.AM - 1; ++var2) {
         var1.fillRect(super.AD + var2 * super.AD, 1, 1, super.AD * ((Player.maxInventory - 1) / super.AM + 1));
      }

      for(var2 = 0; var2 <= (Player.maxInventory - 1) / super.AM + 1; ++var2) {
         var1.fillRect(1, var2 * super.AD, super.AK - 1, 1);
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
   }

   public final void AB(mGraphics var1) {
      if (super.CF > 0 && ScreenUpgrade.mItemUpgrade[5] != null) {
         mFont.tahoma_7b_black.drawString(var1, T.NS + ScreenUpgrade.valueTile + "% + " + super.CF + "%", super.BF, super.AJ, 0);
      } else {
         mFont.tahoma_7b_black.drawString(var1, T.NS + ScreenUpgrade.valueTile + "%", super.BF, super.AJ, 0);
      }

      int var2 = super.AI + super.AK + 20;
      int var3 = super.AJ + (super.AL + 4 << 1) - 20;
      AvMain.paintRect(var1, var2, var3, Interface_Game.BW - 50, 30, (byte)1, 4);
      var3 += 9;
      AvMain.fraMoney.drawFrame(0, var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth / 2 - 3, var3, 0, 3, var1);
      AvMain.fraMoney.drawFrame(1, var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth / 2 + 45, var3, 0, 3, var1);
      AvMain.fraMoney.drawFrame(7, var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth / 2 - 3, var3 + 15, 0, 3, var1);
      mFont.tahoma_7_yellow.drawString(var1, " " + AvMain.AB((long)ScreenUpgrade.valueMonney_1), var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth - 4, var3 - 5, 0);
      mFont.tahoma_7_red.drawString(var1, " " + AvMain.AA((long)ScreenUpgrade.valueMonney_2), var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth + 45, var3 - 5, 0);
      mFont.tahoma_7_green.drawString(var1, " " + AvMain.AB((long)ScreenUpgrade.valueMonney_3), var2 + Interface_Game.BX + AvMain.fraMoney.frameWidth - 4, var3 - 5 + 15, 0);
   }

   public final void AG(mGraphics var1) {
      int var10003 = super.AF - 17;
      int var10005 = super.AC + 17;
      int var10006 = super.AB;
      this.AD(var1, super.AE, var10003, super.AB, var10005, 0);
      var1.setColor(-2573687);
      var1.fillRoundRectNew(super.AE + super.AB / 2 - 60, super.AF - 15 + 10, 120, 16, 4, 4);
      AvMain.FontBorderColor(var1, super.BA, super.AE + super.AB / 2, super.AF - 15 + 12, 2, (int)6, (int)5);
      if (super.BG == 15 || super.BG == 18 || super.BG == 22) {
         if (super.imghoavan == null) {
            super.imghoavan = mImage.createImage("/interface/u_hoavan.png");
            super.CH = mImage.createImage("/interface/u_tron.png");
         }

         var1.drawRegion(super.imghoavan, 0, 0, 35, 35, 0, super.AE + super.AB - 35 - 10, super.AF + super.AC - 35 - 10, 0);
         var1.drawRegion(super.imghoavan, 0, 0, 35, 35, 1, super.AE + super.AB - 35 - 10, super.AF - 5, 0);
         var1.drawRegion(super.imghoavan, 0, 0, 35, 35, 2, super.AE + 10, super.AF + super.AC - 35 - 10, 0);
         var1.drawRegion(super.imghoavan, 0, 0, 35, 35, 3, super.AE + 10, super.AF - 5, 0);
         var1.drawRegion((mImage)super.CH, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2, 3);
      }

      if (super.Step == 0) {
         AvMain.paintRect(var1, super.AI, super.AJ, super.AK, super.AL, (byte)0, 3);
      } else {
         if (super.Step != 0) {
            AvMain.paintRect(var1, super.AI, super.AJ, super.AK, super.AL + 4 << 1, (byte)0, 3);
            if (super.BK == null) {
               super.BK = new FrameImage(mImage.createImage("/interface/effupgrade.png"), 58, 50);
            }

            super.BK.drawFrame(super.frameEff % super.BK.nFrame, super.AI + super.AK / 2 + 10, super.AJ + super.AL, 0, 3, var1);
         }

      }
   }

   public final void AF() {
      if (super.AH >= 0) {
         this.AA(super.AR, super.AI, super.AJ + (super.AH / super.AM + 1) * super.AD - super.AP.AC + 4);
      }

      if (this.CU >= 0) {
         this.AA(super.AR, super.AI, this.CT + (this.CU / super.AM + 1) * super.AD - this.listDa.AC + 4);
      }

   }

   public final void update() {
      super.update();
      int var1 = this.listDa.AC;
      this.listDa.AC();
      if (this.listDa.AC == var1 && !this.listDa.AE) {
         if (super.AR != null) {
            super.AO = false;
         }
      } else {
         super.AO = false;
      }

      if (ScreenUpgrade.BV) {
         long var3 = System.currentTimeMillis();
         int var2 = (int)(800.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
         if (var3 - super.BW >= (long)var2) {
            mVector var5 = new mVector();

            for(var2 = 0; var2 < ScreenUpgrade.mItemUpgradeSaveTemp.length; ++var2) {
               if (ScreenUpgrade.mItemUpgradeSaveTemp[var2] != null) {
                  var5.addElement(ScreenUpgrade.mItemUpgradeSaveTemp[var2]);
               }
            }

            GlobalService.getInstance().AB((byte)2, var5);
            ScreenUpgrade.BV = false;
         }
      }

   }

   public final void updatePointer() {
      if (GameCanvas.isPointerSelect && GameCanvas.isPoint(super.BO - 10, super.BP - 15, super.BQ, super.BR + 10)) {
         if (!(ScreenUpgrade.BT = !ScreenUpgrade.BT)) {
            AW();
         }

         GameCanvas.isPointerSelect = false;
      }

      if (super.Step != 0 || !ScreenUpgrade.BU && !ScreenUpgrade.BV) {
         if (super.Step == 0) {
            super.AP.update_Pos_UP_DOWN();
            int var1;
            int var2;
            if (GameCanvas.AB(super.AI, super.AJ, super.AK, super.AL)) {
               var1 = (GameCanvas.AY - super.AI) / super.AD + (GameCanvas.AZ - super.AJ + super.AP.AC) / super.AD * super.AM;
               var2 = vecSkin.size();
               if (var1 >= 0 && var1 < var2) {
                  GameCanvas.isPointerSelect = false;
                  if (var1 == super.AH) {
                     if (super.AR != null && super.AS != null) {
                        super.AS.AD();
                     }
                  } else {
                     super.AO = false;
                     super.AH = var1;
                     this.CU = -1;
                     this.AA(this.getMenuActionItem());
                     (new StringBuffer("select ")).append(super.AH).append(" select2 ").append(this.CU).toString();
                  }
               } else {
                  super.AO = false;
                  super.AH = -1;
                  this.CU = -1;
                  this.AA((mVector)null);
               }
            }

            this.listDa.update_Pos_UP_DOWN();
            if (GameCanvas.AB(super.AI, this.CT, super.AK, super.AL)) {
               var1 = (GameCanvas.AY - super.AI) / super.AD + (GameCanvas.AZ - this.CT + this.listDa.AC) / super.AD * super.AM;
               var2 = vecDa.size();
               if (var1 >= 0 && var1 < var2) {
                  GameCanvas.isPointerSelect = false;
                  if (var1 == this.CU) {
                     if (super.AR != null && super.AS != null) {
                        super.AS.AD();
                     }
                  } else {
                     super.AO = false;
                     this.CU = var1;
                     super.AH = -1;
                     this.AA(this.getMenuActionItem());
                     (new StringBuffer("select ")).append(super.AH).append(" select2 ").append(this.CU).toString();
                  }
               } else {
                  super.AO = false;
                  this.CU = -1;
                  super.AH = -1;
                  this.AA((mVector)null);
               }
            }

            if (super.AQ != null) {
               for(var1 = 0; var1 < super.AQ.size(); ++var1) {
                  ((iCommand)super.AQ.elementAt(var1)).AE();
               }
            }

            if (GameCanvas.isTouch) {
               if (super.DA != null) {
                  if (super.DA.AF()) {
                     super.DA.AE();
                  } else if (GameCanvas.AB(0, MotherCanvas.h - GameCanvas.hCommand - 5, GameCanvas.BG << 1, GameCanvas.hCommand + 10)) {
                     super.DA.AD();
                  }
               }

               if (super.DB != null) {
                  if (super.DB.AF()) {
                     super.DB.AE();
                  } else if (GameCanvas.AB(MotherCanvas.w - (GameCanvas.BG << 1), MotherCanvas.h - GameCanvas.hCommand - 5, GameCanvas.BG << 1, GameCanvas.hCommand + 10)) {
                     super.DB.AD();
                  }
               }

               if (super.center != null) {
                  if (super.center.AF()) {
                     super.center.AE();
                     return;
                  }

                  if (GameCanvas.AB(MotherCanvas.hw - GameCanvas.BG, MotherCanvas.h - GameCanvas.hCommand - 5, GameCanvas.BG << 1, GameCanvas.hCommand + 10)) {
                     super.center.AD();
                  }
               }
            }

         }
      }
   }

   public final mVector getMenuActionItem() {
      mVector var1 = new mVector();
      MainItem var2 = null;
      if (super.AH >= 0) {
         var2 = (MainItem)vecSkin.elementAt(super.AH);
      } else if (this.CU >= 0) {
         var2 = (MainItem)vecDa.elementAt(this.CU);
      }

      if (var2 != null) {
         super.AR = var2;
         super.AS.caption = T.GN;
         super.AS.AF = 1;
         if (this.AA(super.AR.ID, super.AR.typeObject)) {
            super.AS.caption = T.GM;
            super.AS.AF = 0;
         }

         var1.addElement(super.AS);
      }

      return var1;
   }

   public final void AH() {
      super.AO = false;
      if (super.AH >= 0) {
         super.AH = AvMain.AA(super.AH, vecSkin.size() - 1, false);
         if (GameCanvas.isKeyPressed()) {
            super.AP.AA((super.AH / super.AM + 1) * super.AD - super.AL);
         }

         this.AA(this.getMenuActionItem());
      }

      if (this.CU >= 0) {
         this.CU = AvMain.AA(this.CU, vecDa.size() - 1, false);
         if (GameCanvas.isKeyPressed()) {
            this.listDa.AA((this.CU / super.AM + 1) * super.AD - super.AL);
         }

         this.AA(this.getMenuActionItem());
      }

   }

   public final void commandPointer(int var1, int var2) {
      super.commandPointer(var1, var2);
      mVector var5;
      byte var10;
      switch(var1) {
      case 13:
         if (super.AR != null) {
            byte var7 = 0;
            if (var2 != 0) {
               if (super.AR.typeObject == 7) {
                  var7 = 1;
               } else if (super.AR.typeObject == 105) {
                  var7 = 0;
               } else if (super.AR.typeObject == 4) {
                  var7 = -1;

                  for(var10 = 2; var10 < ScreenUpgrade.mItemUpgrade.length; ++var10) {
                     if (ScreenUpgrade.mItemUpgrade[var10] == null) {
                        var7 = var10;
                        break;
                     }
                  }
               }
            } else {
               var10 = super.AR.typeObject;
               short var8 = super.AR.ID;
               byte var4 = 1;

               byte var10000;
               while(true) {
                  if (var4 >= ScreenUpgrade.mItemUpgrade.length) {
                     var10000 = -1;
                     break;
                  }

                  if (ScreenUpgrade.mItemUpgrade[var4] != null && ScreenUpgrade.mItemUpgrade[var4].ID == var8 && ScreenUpgrade.mItemUpgrade[var4].typeObject == var10) {
                     var10000 = var4;
                     break;
                  }

                  ++var4;
               }

               var7 = var10000;
            }

            if (var7 > 0 && ScreenUpgrade.mItemUpgrade[0] == null) {
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.KU);
               return;
            }

            if (var7 >= 0) {
               GlobalService.getInstance().AA((byte)1, super.AR.typeObject, (short)super.AR.ID, var7, (byte)((byte)var2));
               return;
            }
         }
         break;
      case 14:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.KU);
            return;
         }

         GlobalService.getInstance().AK((byte)4, ScreenUpgrade.mItemUpgrade[0].ID);
         return;
      case 15:
         var5 = new mVector();

         for(var10 = 0; var10 < ScreenUpgrade.mItemUpgrade.length; ++var10) {
            if (ScreenUpgrade.mItemUpgrade[var10] != null) {
               var5.addElement(ScreenUpgrade.mItemUpgrade[var10]);
            }
         }

         GlobalService.getInstance().AB((byte)2, var5);
         GameCanvas.end_Dialog();
         return;
      case 16:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.KU);
            return;
         }

         String var3 = "Bạn có muốn bật cường hoá tự động không?";
         var5 = new mVector();
         iCommand var9 = new iCommand(T.CJ, 17, 0, instance);
         var5.addElement(var9);
         var9 = new iCommand(T.close, 18, 0, this);
         var5.addElement(var9);
         GameCanvas.Start_Normal_DiaLog(var3, var5, false);
         return;
      case 17:
         var5 = new mVector();

         for(byte var6 = 0; var6 < ScreenUpgrade.mItemUpgrade.length; ++var6) {
            if (ScreenUpgrade.mItemUpgrade[var6] != null) {
               var5.addElement(ScreenUpgrade.mItemUpgrade[var6]);
            }
         }

         GlobalService.getInstance().AB((byte)2, var5);
         GameCanvas.end_Dialog();
         return;
      case 18:
         GameCanvas.end_Dialog();
         return;
      case 19:
         AW();
      }

   }

   public static void AA(int var0, short var1, int var2) {
      int var3 = 0;
      boolean var4 = false;

      for(byte var5 = 1; var5 < ScreenUpgrade.mItemUpgrade.length; ++var5) {
         if (ScreenUpgrade.mItemUpgrade[var5] != null) {
            if (ScreenUpgrade.mItemUpgrade[var5].typeObject == 7 && ScreenUpgrade.mItemUpgrade[var5].ID == 17) {
               var4 = true;
            } else {
               var3 += ScreenUpgrade.mItemUpgrade[var5].BG;
            }

            (new StringBuffer("perSucc ")).append(ScreenUpgrade.mItemUpgrade[var5].BG).append(" tile = ").append(var3).toString();
         }
      }

      ScreenUpgrade.valueTile = var3 - (ScreenUpgrade.mItemUpgrade[0].LvUpgrade << 2);
      if (var4) {
         ScreenUpgrade.valueTile <<= 1;
      }

      if (ScreenUpgrade.valueTile <= 0) {
         ScreenUpgrade.valueTile = 0;
      }

      (new StringBuffer("valueTile = ")).append(ScreenUpgrade.valueTile).toString();
      if (var0 != 0 || var1 != 0 || var2 != 0) {
         ScreenUpgrade.valueMonney_1 = var0;
         ScreenUpgrade.valueMonney_2 = var1;
         ScreenUpgrade.valueMonney_3 = var2;
      }
   }

   public final void AA(mGraphics var1, int var2, int var3, MainItem var4) {
      for(int var5 = 0; var5 < ScreenUpgrade.mItemUpgrade.length; ++var5) {
         if (ScreenUpgrade.mItemUpgrade[var5] != null && ScreenUpgrade.mItemUpgrade[var5].ID == var4.ID && ScreenUpgrade.mItemUpgrade[var5].typeObject == var4.typeObject) {
            var1.drawRegion((mImage)AvMain.imgcheck, var2, var3, 36);
         }
      }

   }

   public final void setStep() {
      int var9;
      if (super.Step != 1 && super.Step != 2) {
         if (super.Step == 3) {
            ++super.BD;
            if (super.BD >= super.CK) {
               System.out.println("Vào đâyyyy 222222");
               super.Step = 0;
               super.CK = 0;
               super.BD = 0;
               super.indexStep = 1;
               if (!ScreenUpgrade.BU) {
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog(super.showServer);
               }

               GlobalService.getInstance().AA((byte)1, ScreenUpgrade.mItemUpgrade[0].typeObject, (short)ScreenUpgrade.mItemUpgrade[0].ID, (byte)0, (byte)1);
               if (ScreenUpgrade.BU) {
                  ScreenUpgrade.mItemUpgradeSaveTemp = new MainItem[ScreenUpgrade.mItemUpgrade.length];

                  SkinUpgradeScreen var2;
                  for(var9 = 0; var9 < ScreenUpgrade.mItemUpgrade.length; ++var9) {
                     ScreenUpgrade.mItemUpgradeSaveTemp[var9] = ScreenUpgrade.mItemUpgrade[var9];
                     if (ScreenUpgrade.mItemUpgradeSaveTemp[var9] != null) {
                        byte var4 = ScreenUpgrade.mItemUpgradeSaveTemp[var9].typeObject;
                        short var3 = ScreenUpgrade.mItemUpgradeSaveTemp[var9].ID;
                        var2 = this;

                        for(int var5 = 0; var5 < Player.vecInventory.size(); ++var5) {
                           MainItem var6;
                           if ((var6 = (MainItem)Player.vecInventory.elementAt(var5)) != null && var6.ID == var3 && var6.typeObject == var4) {
                              var2.BX.equals("");
                              var2.BX = var6.name;
                           }
                        }
                     }
                  }

                  var2 = this;
                  boolean var10 = true;

                  for(int var11 = 1; var11 < ScreenUpgrade.mItemUpgradeSaveTemp.length; ++var11) {
                     if (ScreenUpgrade.mItemUpgradeSaveTemp[var11] != null) {
                        short var12 = ScreenUpgrade.mItemUpgradeSaveTemp[var11].ID;
                        SkinUpgradeScreen var14 = var2;
                        short var13 = -1;

                        for(int var7 = 0; var7 < var14.BN.size(); ++var7) {
                           MainItem var8;
                           if ((var8 = (MainItem)var14.BN.elementAt(var7)) != null && var8.ID == var12) {
                              var13 = var8.numPotion;
                              break;
                           }
                        }

                        if (var13 <= 0) {
                           var10 = false;
                           break;
                        }
                     }
                  }

                  if (var10) {
                     for(var9 = 1; var9 < ScreenUpgrade.mItemUpgradeSaveTemp.length; ++var9) {
                        if (ScreenUpgrade.mItemUpgradeSaveTemp[var9] != null) {
                           GlobalService.getInstance().AA((byte)1, ScreenUpgrade.mItemUpgradeSaveTemp[var9].typeObject, (short)ScreenUpgrade.mItemUpgradeSaveTemp[var9].ID, (byte)var9, (byte)1);
                           ScreenUpgrade.BV = true;
                           super.BW = System.currentTimeMillis();
                        }
                     }

                     return;
                  }

                  AW();

                  for(var9 = 0; var9 < ScreenUpgrade.mItemUpgrade.length; ++var9) {
                     ScreenUpgrade.mItemUpgrade[var9] = null;
                  }

                  if (super.BX.equals("")) {
                     AW();
                     GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.YH + T.YI);
                     return;
                  }

                  AW();
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog(super.BX + T.YI);
                  return;
               }

               for(var9 = 0; var9 < ScreenUpgrade.mItemUpgrade.length; ++var9) {
                  ScreenUpgrade.mItemUpgrade[var9] = null;
               }
            }
         }
      } else {
         float var10000;
         if (super.BD % 15 == 0) {
            var10000 = mSound.volumeSound;
            mSound.playSound();
         }

         ++super.BD;
         this.AO();
         if (super.BD % 5 == 0) {
            System.out.println("Vào đâyyyy 11111");
            if (super.indexStep < ScreenUpgrade.mItemUpgrade.length) {
               if (super.BD > 50) {
                  super.indexStep = ScreenUpgrade.mItemUpgrade.length;
               }

               for(var9 = super.indexStep; var9 < ScreenUpgrade.mItemUpgrade.length; ++var9) {
                  if (ScreenUpgrade.mItemUpgrade[var9] != null) {
                     this.AA((short)75, 0, super.AN[var9][0] + super.AD / 2, super.AN[var9][1] + super.AD / 2, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2);
                     ScreenUpgrade.mItemUpgrade[var9].CE = true;
                     super.indexStep = var9 + 1;
                     super.CK = super.BD + 11 + ((super.AB - super.AG * 3 - super.AK) / 2 - 15) / 5;
                     return;
                  }
               }

               return;
            }

            if (super.BD >= super.CK) {
               byte var1;
               if (super.Step == 1) {
                  System.out.println("Vào đâyyyy 222222");
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  var1 = 0;
                  if (GameCanvas.language == 1) {
                     var1 = 2;
                  }

                  this.AA((short)79, var1, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2);
                  this.AA((short)76, 0, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2);
                  this.AA((short)53, 0, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2);
               } else if (super.Step == 2) {
                  System.out.println("Vào đâyyyy 333333");
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  var1 = 1;
                  if (GameCanvas.language == 1) {
                     var1 = 3;
                  }

                  this.AA((short)79, var1, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2);
                  this.AA((short)77, 0, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2);
               }

               super.Step = 3;
               super.BD = 0;
               super.CK = 20;
               return;
            }
         }
      }

   }

   public final void handleKeyPress() {
      if (super.Step == 0) {
         boolean var1 = false;
         if (GameCanvas.isKeyPressed(0)) {
            if (super.AH > 0) {
               --super.AH;
            }

            if (this.CU > 0) {
               --this.CU;
            }

            GameCanvas.clearKeyPressed(0);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(2)) {
            if (super.AH != -1 && super.AH < vecSkin.size() - 1) {
               ++super.AH;
            }

            if (this.CU != -1 && this.CU < vecDa.size() - 1) {
               ++this.CU;
            }

            GameCanvas.clearKeyPressed(2);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(1)) {
            if (super.AH >= super.AM) {
               super.AH -= super.AM;
            } else if (this.CU >= super.AM) {
               this.CU -= super.AM;
            } else if (this.CU != -1 && this.CU < super.AM) {
               this.CU = -1;
               super.AH = vecSkin.size() - 1;
            }

            GameCanvas.clearKeyPressed(1);
            var1 = true;
         } else if (GameCanvas.isKeyPressed(3)) {
            if (super.AH != -1 && super.AH < vecSkin.size() - super.AM) {
               super.AH += super.AM;
            } else if (this.CU != -1 && this.CU < vecDa.size() - super.AM) {
               this.CU += super.AM;
            } else if (super.AH >= vecSkin.size() - super.AM) {
               this.CU = 0;
               super.AH = -1;
            }

            GameCanvas.clearKeyPressed(3);
            var1 = true;
         }

         if (super.AH >= 0 && super.AH < vecSkin.size()) {
            this.CU = -1;
         }

         if (this.CU >= 0 && this.CU < vecDa.size()) {
            super.AH = -1;
         }

         if (var1) {
            this.AH();
         }

         this.AS();
      }
   }

   public final void AV() {
      mVector var1 = new mVector();

      for(byte var2 = 2; var2 < ScreenUpgrade.mItemUpgrade.length; ++var2) {
         if (ScreenUpgrade.mItemUpgrade[var2] != null) {
            var1.addElement(ScreenUpgrade.mItemUpgrade[var2]);
            (new StringBuffer("add item ")).append(var2).toString();
         } else {
            (new StringBuffer("add item ")).append(var2).append(" null").toString();
         }
      }

      if (var1.size() == 4) {
         GlobalService.getInstance().AA((byte)6, (short)ScreenUpgrade.mItemUpgrade[0].ID, (mVector)var1);
      } else {
         super.CF = 0;
      }
   }

   public static void AW() {
      ScreenUpgrade.mItemUpgradeSaveTemp = new MainItem[6];
      ScreenUpgrade.BT = false;
      ScreenUpgrade.BU = false;
      ScreenUpgrade.BV = false;
   }
}
