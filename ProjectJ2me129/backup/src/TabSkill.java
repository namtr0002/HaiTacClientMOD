public final class TabSkill extends MainTab {
   private Skill_Info BP;
   private mVector BQ = new mVector();
   public static iCommand cmdSetHotKey;
   private InputDialog BR;
   private ListNew BS;
   private LoadImageStatic BT = new LoadImageStatic(0);
   private Scroll BU = new Scroll();
   private static int BV = 0;
   private static int BW;
   private static int BX = 0;
   private static int BY = 0;
   private static int BZ = 0;
   private static int CA = 0;
   private static int CB = 0;
   private static int CC = 0;
   private static int CD = 0;
   private static int CE = 0;
   private static int CF = 0;
   private static int CG = 0;
   private static int CH = 0;
   private static int CI = 0;
   public static int BO = 0;
   private static int CJ = 0;
   private static int CK = 0;
   private static mVector CL = new mVector();

   public TabSkill(String var1) {
      super.nameTab = var1;
      new iCommand(T.BI, 0, this);
      cmdSetHotKey = new iCommand(T.BC, 3, this);
      this.BS = new ListNew();
      super.indexIconTab = 3;
      super.AN = 32;
      if (super.AW) {
         super.AN = 36;
      }

      this.BT = new LoadImageStatic(super.AQ - (super.AN + (super.AS << 1) + 3));
   }

   public final void AB() {
      this.BP = null;
      BW = 0;
      BX = 0;
      BY = 0;
      BZ = 0;
      CA = 0;
      CB = 0;
      CC = 0;
      CD = 0;
      CE = 0;
      CF = 0;
      CG = 0;
      CH = 0;
      CI = 0;
      BO = 0;
      CJ = 0;
      CK = 0;
      mVector var1 = new mVector();
      mVector var2 = new mVector();
      mVector var3 = new mVector();
      mVector var4 = new mVector();
      mVector var5 = new mVector();
      mVector var6 = new mVector();
      CL.removeAllElements();

      int var7;
      for(var7 = 0; var7 < Player.vecListSkill.size(); ++var7) {
         Skill_Info var8;
         if ((var8 = (Skill_Info)Player.vecListSkill.elementAt(var7)).Lv_RQ != -1) {
            if (var8.typeDevil != 0 || var8.typeSkill != 1 && var8.typeSkill != 4) {
               if (var8.typeDevil == 1) {
                  var2.addElement(var8);
                  ++CH;
               } else if (var8.typeDevil == 2) {
                  var5.addElement(var8);
                  ++CJ;
               } else if (var8.typeSkill == 6) {
                  var6.addElement(var8);
                  ++CK;
               } else if (var8.typeSkill == 2) {
                  var3.addElement(var8);
                  ++CI;
               } else if (var8.typeSkill == 3) {
                  var4.addElement(var8);
                  ++BO;
               }
            } else {
               var1.addElement(var8);
               ++CG;
            }
         }
      }

      var7 = 0;
      int var12 = 0;
      int var9 = 0;
      int var10;
      Skill_Info var11;
      if (CG > 0) {
         for(var10 = 0; var10 < CG; ++var10) {
            var11 = (Skill_Info)var1.elementAt(var10);
            CL.addElement(var11);
         }

         var12 = CG;
         var7 = CG + 1;
         ++var9;
      }

      if (CH > 0) {
         for(var10 = 0; var10 < CH; ++var10) {
            var11 = (Skill_Info)var2.elementAt(var10);
            CL.addElement(var11);
         }

         CB = var12;
         var12 += CH;
         BW = var7;
         var7 = var7 + CH + 1;
         ++var9;
      }

      if (CI > 0) {
         for(var10 = 0; var10 < CI; ++var10) {
            var11 = (Skill_Info)var3.elementAt(var10);
            CL.addElement(var11);
         }

         CC = var12;
         var12 += CI;
         BX = var7;
         var7 = var7 + CI + 1;
         ++var9;
      }

      if (BO > 0) {
         for(var10 = 0; var10 < BO; ++var10) {
            var11 = (Skill_Info)var4.elementAt(var10);
            CL.addElement(var11);
         }

         CD = var12;
         var12 += BO;
         BY = var7;
         var7 = var7 + BO + 1;
         ++var9;
      }

      if (CJ > 0) {
         for(var10 = 0; var10 < CJ; ++var10) {
            var11 = (Skill_Info)var5.elementAt(var10);
            CL.addElement(var11);
         }

         CE = var12;
         var12 += CJ;
         BZ = var7;
         var7 = var7 + CJ + 1;
         ++var9;
      }

      if (CK > 0) {
         for(var10 = 0; var10 < CK; ++var10) {
            var11 = (Skill_Info)var6.elementAt(var10);
            CL.addElement(var11);
         }

         CF = var12;
         CA = var7;
         ++var9;
      }

      var10 = ((BV = CL.size()) + var9) * super.AN - super.AR + super.AS * 3;
      this.BS = new ListNew(super.AO, super.AP, super.AQ, super.AR, 0, 0, var10, true);
      this.BU.AA(super.AO + super.AQ + super.AS, super.AP + super.AS / 2, super.AR - (super.AS << 1), 8809550);
      super.AV = false;
      if (GameCanvas.isKeyPressed()) {
         super.IdSelect = 0;
         this.AL();
      } else {
         super.IdSelect = -1;
         this.AB((mVector)null);
      }
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (this.BP != null) {
            if (Player.QA[this.BP.indexHotKey] >= Skill_Info.maxLv) {
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.BM);
               this.BQ = this.BP.getActionInven();
               return;
            }

            if (Player.AR > 1) {
               this.BR = GameCanvas.Start_Input_Dialog(T.BH, new iCommand(T.BE, 1, 0, this), true, T.BI);
               GameCanvas.subDialog = this.BR;
               return;
            }

            if (Player.AR == 1) {
               this.commandPointer(2, 0);
               return;
            }
         }
         break;
      case 1:
         if (this.BP != null) {
            if (var2 == 0) {
               boolean var6 = false;

               try {
                  if ((var1 = Integer.parseInt(this.BR.tfInput.getText())) < 0) {
                     var1 = 1;
                  }
               } catch (Exception var4) {
                  var1 = 1;
               }

               GlobalService.getInstance().Add_Point_Skill(this.BP.indexHotKey, (short)var1);
               GameCanvas.end_Dialog();
               return;
            }

            GlobalService.getInstance().Add_Point_Skill((short)this.BP.indexHotKey, (short)1);
            GameCanvas.end_Dialog();
            return;
         }
         break;
      case 2:
         GameCanvas.Start_Normal_DiaLog(T.BL, new iCommand(T.BI, 1, 1, this), true);
         return;
      case 3:
         if (this.BP != null && this.BP.Lv_RQ > 0) {
            if (this.BP != null && (LoadMap.specMap == 4 && this.BP.typeSkill == 1 || LoadMap.specMap != 4 && this.BP.typeSkill == 4)) {
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.HT);
               return;
            }

            mVector var5 = new mVector();

            for(int var8 = 0; var8 < 6; ++var8) {
               if (var8 != 2 && (GameCanvas.isTouch || var8 != 5)) {
                  iCommand var7;
                  if (GameCanvas.isTouch) {
                     var7 = new iCommand(T.BD + " " + (var8 + 1), 4, var8, this);
                  } else if (TField.AF) {
                     var7 = new iCommand(T.BD + " " + T.VY[var8], 4, var8, this);
                  } else {
                     var7 = new iCommand(T.BD + " " + ((var8 << 1) + 1), 4, var8, this);
                  }

                  var5.addElement(var7);
               }
            }

            GameCanvas.menu.startAt(var5, 2, T.BC);
            return;
         }

         return;
      case 4:
         if (this.BP != null) {
            MainSkill var3;
            (var3 = new MainSkill(this.BP.ID, (short)-1)).AB = this.BP.indexHotKey;
            var3.idIcon = this.BP.idIcon;
            var3.isBuff = this.BP.typeSkill == 2;
            Player.AA(var2, var3, (MainItem)null);
            Interface_Game.BP = 100;
         }
      }

   }

   public final void paint(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      AvMain.paintRect(var1, super.AO, super.AP, super.AQ, super.AR - super.AS, (byte)0, 4);
      mGraphics.AC();
      int var10000 = super.AO;
      var10000 = super.AP;
      var10000 = super.AQ;
      var10000 = super.AR;
      var10000 = super.AS;
      mGraphics.AD();
      this.AB(var1);
      int var2 = super.AS;
      AvMain.AA(var1, T.skillActive, var2, 0 + super.AN / 2 - 2, 0);
      int var3 = 0 + super.AN;
      int var6;
      MainScreen var12;
      if (super.IdSelect >= 0 && super.IdSelect < BV) {
         var12 = GameCanvas.currentScreen;
         if (MainScreen.AD((byte)1)) {
            var6 = super.AN;
            if (CH > 0 && super.IdSelect >= CB) {
               var6 += super.AN;
            }

            if (CI > 0 && super.IdSelect >= CC) {
               var6 += super.AN;
            }

            if (BO > 0 && super.IdSelect >= CD) {
               var6 += super.AN;
            }

            if (CJ > 0 && super.IdSelect >= CE) {
               var6 += super.AN;
            }

            if (CK > 0 && super.IdSelect >= CF) {
               var6 += super.AN;
            }

            var1.setColor(-330796);
            var1.fillRect(super.AS / 2, var6 + super.IdSelect * super.AN, 1, super.AN);
            var1.fillRect(super.AQ - super.AS / 2 - 1, var6 + super.IdSelect * super.AN, 1, super.AN);
            var1.fillRect(super.AS / 2 + 1, var6 + super.IdSelect * super.AN - 1, super.AQ - super.AS - 1, 1);
            var1.fillRect(super.AS / 2 + 1, var6 + super.IdSelect * super.AN + super.AN, super.AQ - super.AS - 1, 1);
         }
      }

      int var4 = this.BS.AC / super.AN;
      int var5 = super.AR / super.AN + 1 + var4;
      var4 = AD(var4);
      var5 = AD(var5);

      for(var6 = 0; var6 < BV; ++var6) {
         if (CH > 0 && var6 == CB) {
            AvMain.AA(var1, T.ID, var2, var3 + super.AN / 2 - 6, 0);
            var3 += super.AN;
         }

         if (CI > 0 && var6 == CC) {
            AvMain.AA(var1, T.skillBuff, var2, var3 + super.AN / 2 - 6, 0);
            var3 += super.AN;
         }

         if (BO > 0 && var6 == CD) {
            AvMain.AA(var1, T.skillPassive + ": " + BO + "/" + Player.QM, var2, var3 + super.AN / 2 - 6, 0);
            var3 += super.AN;
         }

         if (CJ > 0 && var6 == CE) {
            AvMain.AA(var1, T.IE, var2, var3 + super.AN / 2 - 6, 0);
            var3 += super.AN;
         }

         if (CK > 0 && var6 == CF) {
            AvMain.AA(var1, T.skillJob, var2, var3 + super.AN / 2 - 6, 0);
            var3 += super.AN;
         }

         if (var6 >= var4 && var6 <= var5) {
            Skill_Info var7;
            String var8 = (var7 = (Skill_Info)CL.elementAt(var6)).name;
            var7.paint(var1, var2 + super.AN / 2, var3 + super.AN / 2);
            if (var7.Lv_RQ == Skill_Info.maxLv) {
               var8 = var7.name + " " + T.max;
            }

            if (var7.Lv_RQ < 0) {
               mFont.tahoma_7b_blue.drawString(var1, var8, var2 + super.AN + super.AS, var3 + super.AS / 2 + 1, 0);
            } else {
               label205: {
                  if (super.IdSelect == var6) {
                     var12 = GameCanvas.currentScreen;
                     if (MainScreen.AD((byte)1) && this.BT.AF) {
                        var1.AD(var2 + super.AN + super.AS, var3, this.BT.AB - 1, super.AN);
                        mFont.tahoma_7b_white.drawString(var1, var8, var2 + super.AN + super.AS - this.BT.AD, var3 + super.AS / 2 + 1, 0);
                        this.AB(var1);
                        break label205;
                     }
                  }

                  mFont.tahoma_7b_white.drawString(var1, var8, var2 + super.AN + super.AS, var3 + super.AS / 2 + 1, 0);
               }
            }

            if (var7.typeDevil == 1) {
               if (var7.typeSkill == 1) {
                  var8 = T.IF;
               } else if (var7.typeSkill == 3) {
                  var8 = T.IG;
               } else {
                  var8 = T.IH;
               }

               mFont.tahoma_7_green.drawString(var1, var8, var2 + super.AN + super.AS, var3 + super.AS / 2 + GameCanvas.hText + 1, 0);
            } else if (var7.typeDevil == 2) {
               if (var7.typeSkill == 1) {
                  var8 = T.II;
               } else if (var7.typeSkill == 3) {
                  var8 = T.IJ;
               } else {
                  var8 = T.IK;
               }

               mFont.tahoma_7_green.drawString(var1, var8, var2 + super.AN + super.AS, var3 + super.AS / 2 + GameCanvas.hText + 1, 0);
            } else if (var7.typeSkill != 3 && var7.typeSkill != 2) {
               if (var7.Lv_RQ >= Skill_Info.maxLv) {
                  mFont.tahoma_7_green.drawString(var1, T.QE, var2 + super.AN + super.AS, var3 + super.AS / 2 + GameCanvas.hText + 1, 0);
               } else if (GameCanvas.isSmallScreen) {
                  mFont.tahoma_7_green.drawString(var1, T.gohome + ": " + var7.Lv_RQ + "+" + MainItem.AA(var7.percentLv, (byte)1), var2 + super.AN + super.AS, var3 + super.AS / 2 + GameCanvas.hText + 1, 0);
               } else {
                  mFont.tahoma_7_green.drawString(var1, T.gohome + ": " + var7.Lv_RQ, var2 + super.AN + super.AS, var3 + super.AS / 2 + GameCanvas.hText + 1, 0);
                  Interface_Game.AA(var1, (byte)3, var7.percentLv, 100, var2 + (super.AN << 1), var3 + super.AS / 2 + GameCanvas.hText + 1, 0, super.AS << 1, super.AQ / 2, 1, false, 0, false, 0);
               }
            } else {
               mFont.tahoma_7_green.drawString(var1, T.gohome + ": " + var7.Lv_RQ + " " + var7.getStrType(), var2 + super.AN + super.AS, var3 + super.AS / 2 + GameCanvas.hText + 1, 0);
            }

            if (var7.phanTramDevilSkill > 0) {
               int var11;
               if ((var11 = var7.phanTramDevilSkill / 5) < 20) {
                  ++var11;
               } else if (var11 == 20) {
                  var11 += 2;
               } else {
                  var11 = 22;
               }

               var1.drawRegion(AvMain.imgLvDevilSkill, 0, 22 - var11, 22, var11, 0, var2 + super.AN / 2, var3 + super.AN / 2 + 11, 33);
            }
         }

         var3 += super.AN;
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      var12 = GameCanvas.currentScreen;
      if (MainScreen.AD((byte)1)) {
         if (this.BS.AD > 0) {
            this.BU.AA(var1);
         }

         if (super.AV && this.BP != null) {
            boolean var10 = false;
            if (this.BP.typeSkill == 1 || this.BP.typeSkill == 4) {
               var10 = true;
            }

            this.AA(var1, this.BP, (mVector)null, (byte)0, super.AL, super.AM, var10, (MainObject)null, 0);
         }

         if (this.BQ != null && GameCanvas.AL()) {
            for(var6 = 0; var6 < this.BQ.size(); ++var6) {
               iCommand var9;
               (var9 = (iCommand)this.BQ.elementAt(var6)).paint(var1, var9.xCmd, var9.yCmd);
            }
         }

         super.paint(var1);
      }

   }

   private void AB(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      var1.AD(super.AO + 2, super.AP + 1, super.AQ - 4, super.AR - 1 - super.AS - 1);
      var1.translate(super.AO, super.AP);
      var1.translate(0, -this.BS.AC);
   }

   public final void update() {
      int var1 = this.BS.AC;
      this.BS.AC();
      this.BU.AA(this.BS.AC, this.BS.AD);
      if (this.BS.AC == var1 && !this.BS.AE) {
         if (this.BP != null) {
            this.AF();
         }
      } else {
         super.AV = false;
      }

      this.BT.AA();
   }

   public final void handleKeyPress() {
      boolean var1 = false;
      if (GameCanvas.isKeyPressed(1)) {
         --super.IdSelect;
         GameCanvas.clearKeyPressed(1);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         ++super.IdSelect;
         GameCanvas.clearKeyPressed(3);
         var1 = true;
      }

      if (GameCanvas.isKeyPressed(0) || GameCanvas.isKeyPressed(2)) {
         MainScreen var10000 = GameCanvas.currentScreen;
         MainScreen.AC((byte)0);
         GameCanvas.clearKeyPressed(0);
         GameCanvas.clearKeyPressed(2);
      }

      if (var1) {
         super.IdSelect = AvMain.AA(super.IdSelect, BV - 1, false);
         if (super.IdSelect >= 0) {
            if (GameCanvas.isKeyPressed()) {
               int var2 = (super.IdSelect + 2) * super.AN - super.AR / 2;
               if (CH > 0 && super.IdSelect >= CB) {
                  var2 += super.AN;
               }

               if (CI > 0 && super.IdSelect >= CC) {
                  var2 += super.AN;
               }

               if (BO > 0 && super.IdSelect >= CD) {
                  var2 += super.AN;
               }

               if (CJ > 0 && super.IdSelect >= CE) {
                  var2 += super.AN;
               }

               if (CK > 0 && super.IdSelect >= CF) {
                  var2 += super.AN;
               }

               this.BS.AA(var2);
            }

            this.AL();
            super.AV = false;
         } else {
            this.BP = null;
         }
      }

      super.handleKeyPress();
      this.AS();
   }

   public final void updatePointer() {
      this.BS.update_Pos_UP_DOWN();
      int var1;
      if (GameCanvas.isPointerSelect && BV > 0 && GameCanvas.isPoint(super.AO, super.AP, super.AQ, super.AR)) {
         if ((var1 = (GameCanvas.AZ - super.AP + this.BS.AC) / super.AN) != 0 && var1 != BW && var1 != BX && var1 != BY && var1 != BZ && var1 != CA && (var1 = AD(var1)) != super.IdSelect && var1 >= 0 && var1 < BV) {
            super.IdSelect = var1;
            this.AL();
         }

         GameCanvas.isPointerSelect = false;
      }

      if (this.BQ != null) {
         for(var1 = 0; var1 < this.BQ.size(); ++var1) {
            ((iCommand)this.BQ.elementAt(var1)).AE();
         }
      }

      super.updatePointer();
   }

   private static int AD(int var0) {
      int var1 = var0 - 1;
      if (BW > 0 && var0 > BW) {
         --var1;
      }

      if (BX > 0 && var0 > BX) {
         --var1;
      }

      if (BY > 0 && var0 > BY) {
         --var1;
      }

      if (BZ > 0 && var0 > BZ) {
         --var1;
      }

      if (CA > 0 && var0 > CA) {
         --var1;
      }

      return var1;
   }

   private void AL() {
      if (super.IdSelect >= 0 && super.IdSelect < BV) {
         Skill_Info var1;
         if ((var1 = (Skill_Info)CL.elementAt(super.IdSelect)).Lv_RQ == -1) {
            this.BP = null;
            return;
         }

         if (this.BP == null || this.BP != var1) {
            this.BP = var1;
            if (this.BP != null) {
               this.AB(this.BP.getActionInven());
            }

            this.BT.AA(var1.name, mFont.tahoma_7b_black);
         }
      }

   }

   public final void AG() {
      if (this.BP.Lv_RQ != -1) {
         if (this.BP != null) {
            this.BP.setVecInfo(this.BP.BS);
         }

         this.AA((MainItem)this.BP, super.AO + MainTab.AG, super.AP + (super.IdSelect + 1) * super.AN - this.BS.AC + 1 + (super.AS << 1));
      }
   }

   private void AB(mVector var1) {
      super.DA = null;
      super.center = null;
      this.BQ.removeAllElements();
      if (var1 != null) {
         int var2;
         iCommand var3;
         if (GameCanvas.isTouch) {
            this.BQ = var1;

            for(var2 = 0; var2 < this.BQ.size(); ++var2) {
               var3 = AvMain.AA((iCommand)this.BQ.elementAt(var2), var2);
               if (var2 == 0) {
                  super.DF = var3;
               }

               if (var2 == 1) {
                  super.DE = var3;
               }
            }

         } else {
            for(var2 = 0; var2 < var1.size(); ++var2) {
               var3 = (iCommand)var1.elementAt(var2);
               if (var2 == 0) {
                  super.center = var3;
               }

               if (var2 == 1) {
                  super.DA = var3;
               }
            }

         }
      }
   }
}
