public final class SplitScreen extends ScreenUpgrade {
   public static SplitScreen instance;
   public short CQ = -1;
   private MainItem CW;
   private boolean CX = false;
   private InputDialog CY;
   public int CR = 0;
   public byte CS = 0;
   public mVector CT = new mVector();
   public short CU = -1;
   public String CV = "";

   public SplitScreen(byte var1, int var2) {
      super(var1, -1);
   }

   public final void AA(byte var1, int var2) {
      super.BG = var1;
      var2 = super.AE + super.AK + (super.AB - super.AG * 3 - super.AK) / 6 + 4;
      int var3 = super.AF + super.AC / 2 - super.AD / 2;
      this.CW = null;
      this.CX = false;
      if (var1 == 21) {
         super.AN = mSystem.new_M_Int(1, 2);
         super.AN[0][0] = var2 + super.AD * 3 / 2;
         super.AN[0][1] = var3 + (super.AD << 1) / 3;
      } else {
         super.AN = mSystem.new_M_Int(2, 2);
         super.AN[0][0] = var2;
         super.AN[0][1] = var3;
         super.AN[1][0] = var2 + ((super.AB - super.AG * 3 - super.AK) / 6 << 2);
         super.AN[1][1] = var3;
         if (var1 == 19) {
            super.AN[0][1] = var3 + super.AD - 5;
         }
      }

      super.AM = super.AK / super.AD;
      var2 = ((Player.maxInventory - 1) / super.AM + 1) * super.AD - super.AL;
      super.AP = new ListNew(super.AI, super.AJ, super.AK, super.AL, 0, 0, var2, true);
      super.AS = new iCommand(T.GN, 0, this);
      super.AU = new iCommand(T.split, 1, this);
      super.AW = new iCommand(T.AU, 10, this);
      super.BA = T.GP;
      if (var1 == 1) {
         super.BM = 2;
         super.AU = new iCommand(T.LJ, 2, this);
         super.BA = T.LN;
      } else if (var1 == 21) {
         super.BM = 1;
         super.AU = new iCommand(T.LJ, 22, this);
         super.BA = T.LN;
      } else if (var1 == 2) {
         super.BM = 4;
         super.AU = new iCommand(T.LK, 3, this);
         super.BA = T.LO;
      } else if (var1 == 3) {
         super.BM = 2;
         super.AU = new iCommand(T.LP, 4, this);
         super.BA = T.LQ;
      } else if (var1 == 4) {
         super.BM = 1;
         super.AU = new iCommand(T.LT, 5, this);
         super.AV = new iCommand(T.allMaterial, 8, this);
         super.BA = T.LR;
      } else if (var1 == 7) {
         super.BM = 3;
         super.AU = new iCommand(T.NQ, 7, this);
         super.BA = T.NR;
      } else if (var1 == 8) {
         super.AU = new iCommand(T.GK, 12, this);
         super.BA = T.PY;
      } else if (var1 == 10) {
         super.BM = 2;
         super.AU = new iCommand(T.RM, 13, this);
         super.BA = T.RN;
      } else if (var1 == 11) {
         super.BM = 2;
         super.AU = new iCommand(T.RQ, 14, this);
         super.BA = T.RR;
      } else if (var1 == 12) {
         super.BM = 3;
         super.AU = new iCommand(T.RV, 15, this);
         super.BA = T.RW;
         super.AN = mSystem.new_M_Int(1, 2);
         super.AN[0][0] = super.AE + super.AK + (super.AB - super.AG * 3 - super.AK) / 2;
         super.AN[0][1] = var3;
      } else if (var1 == 13) {
         super.BM = 1;
         super.AU = new iCommand(T.RV, 18, this);
         super.BA = T.SB;
      } else if (var1 == 14) {
         super.AU = new iCommand(T.SL, 19, this);
         super.BA = T.SM;
      } else if (var1 == 15) {
         super.BM = 5;
         super.AU = new iCommand(T.LK, 20, this);
         super.BA = T.LO;
      } else if (var1 == 19) {
         super.BM = 5;
         super.AU = new iCommand(T.LK, 3, this);
         super.BA = T.LO;
         this.CX = true;
      }

      AvMain.AA(super.AW, 2);
      if (var1 != 19 && var1 != 21) {
         super.AQ.addElement(super.AW);
      }

      if (GameCanvas.isTouch) {
         super.AU.setPos(super.AE + super.AK + (super.AB - super.AG * 3 - super.AK) / 2 + super.AD * 3 / 4, super.AF + super.AC - iCommand.hButtonCmdNor / 2 - 10, (FrameImage)null, super.AU.caption);
         super.AQ.addElement(super.AU);
      }

      ScreenUpgrade.mItemUpgrade = new MainItem[super.AN.length];
      if (!GameCanvas.isTouch) {
         AvMain.AA(super.AU, 1);
         super.AT.caption = T.close + " " + T.QB;
         if (var1 != 19 && var1 != 21) {
            super.DB = super.AW;
         }

         super.DA = super.AU;
      }

      if (GameCanvas.isKeyPressed()) {
         super.backCMD = super.AT;
         super.DF = super.AU;
      }

      if (super.AH >= 0 && var1 != 21) {
         this.AH();
      }

   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         break;
      case 0:
         if (super.AR != null) {
            if (super.BG == 21) {
               ScreenUpgrade.mItemUpgrade[0] = super.AR;
               return;
            }

            if (super.BG == 0) {
               GlobalService.getInstance().Split_Item((byte)0, (byte)0, (short)super.AR.ID, super.AR.typeObject, (short)1);
               return;
            }

            mVector var7;
            if (super.BG == 4) {
               if (super.AV == null) {
                  super.AV = new iCommand(T.allMaterial, 8, this);
               }

               (var7 = new mVector()).addElement(super.AV);
               var7.addElement(new iCommand(T.soluong, 9, this));
               GameCanvas.Start_Normal_DiaLog(T.nhapsoluong, var7, true);
               return;
            }

            if (super.BG == 7) {
               GlobalService.getInstance().AA((byte)1, (short)super.AR.ID, (short)0);
               return;
            }

            if (super.BG == 8) {
               GlobalService.getInstance().AB((byte)14, super.AR.ID, super.AR.typeObject, (short)1);
               return;
            }

            if (super.BG == 13) {
               (var7 = new mVector()).addElement(new iCommand(T.SC, 17, 28, this));
               var7.addElement(new iCommand(T.SD, 17, 29, this));
               GameCanvas.menuCur.startAt(var7, 2, T.SB);
               return;
            }

            if (super.BG == 2) {
               if (super.AR.typeObject == 3) {
                  GlobalService.getInstance().AB(super.BG, (byte)1, super.AR.ID, super.AR.typeObject, (short)var2);
                  return;
               }

               if (super.AR.typeObject == 4) {
                  if (this.CW != null && super.AR.ID == this.CW.ID) {
                     this.CW = null;
                     return;
                  }

                  if (super.AR.ID == 323 || super.AR.ID == 339) {
                     this.CW = super.AR;
                     return;
                  }
               }
            } else if (super.BG == 19) {
               if (super.AR.typeObject == 3) {
                  GlobalService.getInstance().AB(super.BG, (byte)1, super.AR.ID, super.AR.typeObject, (short)var2);
                  return;
               }

               if (super.AR.typeObject == 4) {
                  if (this.CW != null && super.AR.ID == this.CW.ID) {
                     this.CW = null;
                     return;
                  }

                  if (super.AR.ID == 457) {
                     this.CW = super.AR;
                     return;
                  }
               }
            } else if (super.BG == 15) {
               if (super.AR.typeObject == 3) {
                  GlobalService.getInstance().AB(super.BG, (byte)1, super.AR.ID, super.AR.typeObject, (short)var2);
                  return;
               }

               if (super.AR.typeObject == -8) {
                  if (this.CW != null && super.AR.ID == this.CW.ID) {
                     this.CW = null;
                     return;
                  }

                  this.CW = super.AR;
                  return;
               }
            } else {
               GlobalService.getInstance().AB(super.BG, (byte)1, super.AR.ID, super.AR.typeObject, (short)var2);
               if ((super.BG == 2 || super.BG == 3 || super.BG == 4 || super.BG == 15) && ScreenUpgrade.mItemUpgrade[1] != null) {
                  ScreenUpgrade.mItemUpgrade[1] = null;
                  return;
               }
            }
         }
         break;
      case 1:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.KU);
            return;
         } else {
            GlobalService.getInstance().Split_Item((byte)0, (byte)1, (short)ScreenUpgrade.mItemUpgrade[0].ID, ScreenUpgrade.mItemUpgrade[0].typeObject, (short)1);
            return;
         }
      case 2:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LL);
            return;
         }

         if (ScreenUpgrade.mItemUpgrade[1] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LM);
            return;
         }

         GlobalService.getInstance().AB(super.BG, (byte)4, (short)0, (byte)0, (short)0);
         return;
      case 3:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LL);
            return;
         }

         if (ScreenUpgrade.mItemUpgrade[1] != null) {
            ScreenUpgrade.mItemUpgrade[1] = null;
         }

         if (this.CW == null) {
            GlobalService.getInstance().AB(super.BG, (byte)7, ScreenUpgrade.mItemUpgrade[0].ID, ScreenUpgrade.mItemUpgrade[0].typeObject, (short)1);
            return;
         }

         if (this.CW.ID == 339) {
            GlobalService.getInstance().AB(super.BG, (byte)33, ScreenUpgrade.mItemUpgrade[0].ID, ScreenUpgrade.mItemUpgrade[0].typeObject, (short)1);
            return;
         }

         if (this.CW.ID == 323) {
            GlobalService.getInstance().AB(super.BG, (byte)34, ScreenUpgrade.mItemUpgrade[0].ID, ScreenUpgrade.mItemUpgrade[0].typeObject, (short)1);
            return;
         }

         if (super.BG == 19) {
            (new StringBuffer(" potionBua id = ")).append(this.CW.ID).toString();
            GlobalService.getInstance().AA(super.BG, (byte)7, ScreenUpgrade.mItemUpgrade[0].ID, ScreenUpgrade.mItemUpgrade[0].typeObject, (short)1, this.CW.ID);
            return;
         }
         break;
      case 4:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LL);
            return;
         }

         if (ScreenUpgrade.mItemUpgrade[1] != null) {
            ScreenUpgrade.mItemUpgrade[1] = null;
         }

         GlobalService.getInstance().AB(super.BG, (byte)6, ScreenUpgrade.mItemUpgrade[0].ID, ScreenUpgrade.mItemUpgrade[0].typeObject, (short)1);
         return;
      case 5:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LL);
            return;
         }

         if (ScreenUpgrade.mItemUpgrade[1] != null) {
            ScreenUpgrade.mItemUpgrade[1] = null;
         }

         GlobalService.getInstance().AB(super.BG, (byte)5, ScreenUpgrade.mItemUpgrade[0].ID, ScreenUpgrade.mItemUpgrade[0].typeObject, ScreenUpgrade.mItemUpgrade[0].numPotion);
         return;
      case 6:
         boolean var5 = false;

         try {
            if ((var1 = Integer.parseInt(this.CY.tfInput.getText())) < 0) {
               var1 = 1;
            }
         } catch (Exception var4) {
            var1 = 1;
         }

         this.AD(var1);
         GameCanvas.end_Dialog();
         return;
      case 7:
         if (ScreenUpgrade.mItemUpgrade[0] != null && ScreenUpgrade.mItemUpgrade[1] != null) {
            GlobalService.getInstance().AA((byte)2, (short)ScreenUpgrade.mItemUpgrade[0].ID, (short)ScreenUpgrade.mItemUpgrade[1].ID);
            return;
         }
         break;
      case 8:
         if (super.AR != null) {
            this.AD(super.AR.numPotion);
         }

         GameCanvas.end_Dialog();
         return;
      case 9:
         GameCanvas.end_Dialog();
         this.CY = GameCanvas.Start_Input_Dialog(T.nhapsoluong, new iCommand(T.strconfirm, 6, 0, this), true, T.LR);
         GameCanvas.subDialog = this.CY;
         return;
      case 10:
         mVector var6 = new mVector();
         if (!GameCanvas.isTouch) {
            var6.addElement(super.AT);
         }

         iCommand var3;
         if (super.BG != 1) {
            var3 = new iCommand(T.LN, 11, 9, this);
            var6.addElement(var3);
         }

         if (super.BG != 4) {
            var3 = new iCommand(T.LR, 11, 12, this);
            var6.addElement(var3);
         }

         if (super.BG != 2) {
            var3 = new iCommand(T.LO, 11, 10, this);
            var6.addElement(var3);
         }

         if (super.BG != 19) {
            var3 = new iCommand(T.LO, 11, 10, this);
            var6.addElement(var3);
         }

         if (super.BG != 3) {
            var3 = new iCommand(T.LQ, 11, 13, this);
            var6.addElement(var3);
         }

         if (super.BG != 6) {
            var3 = new iCommand(T.QA, 11, 11, this);
            var6.addElement(var3);
         }

         if (super.BG != 15) {
            var3 = new iCommand(T.LO, 11, 10, this);
            var6.addElement(var3);
         }

         GameCanvas.menuCur.startAt(var6, 2, T.AU);
         return;
      case 11:
         GlobalService.getInstance().Upgrade_Item((byte)((byte)var2), (short)0, (byte)0);
         return;
      case 12:
         if (ScreenUpgrade.mItemUpgrade[0] != null && ScreenUpgrade.mItemUpgrade[1] != null) {
            GlobalService.getInstance().AB((byte)17, ScreenUpgrade.mItemUpgrade[0].ID, ScreenUpgrade.mItemUpgrade[0].typeObject, (short)ScreenUpgrade.mItemUpgrade[0].numPotion);
            return;
         }
         break;
      case 13:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LL);
            return;
         }

         if (ScreenUpgrade.mItemUpgrade[1] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.RO);
            return;
         }

         GlobalService.getInstance().AB(super.BG, (byte)20, (short)0, (byte)0, (short)0);
         return;
      case 14:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LL);
            return;
         }

         if (ScreenUpgrade.mItemUpgrade[1] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.RO);
            return;
         }

         GlobalService.getInstance().AB(super.BG, (byte)22, (short)0, (byte)0, (short)0);
         return;
      case 15:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LL);
            return;
         }

         GlobalService.getInstance().AB(super.BG, (byte)24, (short)0, (byte)0, (short)0);
         return;
      case 16:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LL);
            return;
         }

         GlobalService.getInstance().AB(super.BG, (byte)26, (short)0, (byte)0, (short)0);
         return;
      case 17:
         if (super.AR != null) {
            GlobalService.getInstance().AB(super.BG, (byte)var2, super.AR.ID, super.AR.typeObject, (short)0);
            return;
         }
         break;
      case 18:
         if (ScreenUpgrade.mItemUpgrade[0] != null && ScreenUpgrade.mItemUpgrade[1] != null) {
            GlobalService.getInstance().AB(super.BG, (byte)26, (short)0, (byte)0, (short)0);
            return;
         }

         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.SJ);
         return;
      case 19:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.SJ);
            return;
         }

         if (ScreenUpgrade.mItemUpgrade[1] != null) {
            ScreenUpgrade.mItemUpgrade[1].CE = true;
         }

         GlobalService.getInstance().AB(super.BG, (byte)31, (short)0, (byte)0, (short)0);
         return;
      case 20:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LL);
            return;
         }

         if (ScreenUpgrade.mItemUpgrade[1] != null) {
            ScreenUpgrade.mItemUpgrade[1] = null;
         }

         if (this.CW != null) {
            GlobalService.getInstance().AB(super.BG, (byte)7, ScreenUpgrade.mItemUpgrade[0].ID, ScreenUpgrade.mItemUpgrade[0].typeObject, (short)1, this.CW.ID);
            return;
         }

         GlobalService.getInstance().AB(super.BG, (byte)7, ScreenUpgrade.mItemUpgrade[0].ID, ScreenUpgrade.mItemUpgrade[0].typeObject, (short)1, (short)-1);
         return;
      case 21:
      default:
         break;
      case 22:
         if (ScreenUpgrade.mItemUpgrade[0] == null) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.LL);
            return;
         }

         if (super.BG == 21) {
            GlobalService.getInstance().AG((byte)2, (short)ScreenUpgrade.mItemUpgrade[0].ID);
            float var10000 = mSound.volumeSound;
            mSound.playSound();
            this.AA((short)53, 0, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2);
            return;
         }
         break;
      case 23:
         if (ScreenUpgrade.mItemUpgrade[0] != null && super.BG == 21) {
            GlobalService.getInstance().AG((byte)3, (short)ScreenUpgrade.mItemUpgrade[0].ID);
            return;
         }
      }

   }

   private void AD(int var1) {
      MainItem var2;
      if ((var2 = MainItem.getItemVec(super.AR.typeObject, super.AR.ID, super.BN)) != null) {
         if (var1 > var2.numPotion) {
            var1 = var2.numPotion;
         }

         if (super.AR != null) {
            GlobalService.getInstance().AB(super.BG, (byte)1, super.AR.ID, super.AR.typeObject, (short)var1);
         }
      }

   }

   public final void AB(mGraphics var1) {
      if (super.BG == 4) {
         if (ScreenUpgrade.mItemUpgrade[0] != null) {
            int var2 = (ScreenUpgrade.mItemUpgrade[0].ID - 44) % 6;
            if (ScreenUpgrade.mItemUpgrade[0].ID >= 221) {
               var2 = (ScreenUpgrade.mItemUpgrade[0].ID - 221) % 6;
            }

            if (var2 + 1 < ScreenUpgrade.BI.length) {
               mFont.tahoma_7b_black.drawString(var1, T.NS + ScreenUpgrade.BI[var2 + 1] + "%", super.BF, super.AJ, 0);
               return;
            }
         }
      } else if (super.BG == 7) {
         if (super.Step == 0) {
            if (AvMain.DS == null) {
               AvMain.DS = mImage.createImage("/interface/muiten.png");
               return;
            }

            var1.drawRegion(AvMain.DS, 20, 0, 33, 14, 0, super.AE + super.AK + (super.AB - super.AK) / 2 - 2, super.AN[0][1] + super.AD, 33);
            return;
         }
      } else if (super.BG == 8) {
         if (ScreenUpgrade.mItemUpgrade[0] != null) {
            mFont.tahoma_7b_black.drawString(var1, T.NS + this.CS + "%", super.BF, super.AJ, 0);
            return;
         }
      } else if (super.BG == 8) {
         if (ScreenUpgrade.mItemUpgrade[0] != null) {
            mFont.tahoma_7b_black.drawString(var1, T.NS + this.CS + "%", super.BF, super.AJ, 0);
            return;
         }
      } else if (super.BG != 11 && super.BG != 10) {
         if (super.BG == 13 && ScreenUpgrade.mItemUpgrade[0] != null && ScreenUpgrade.mItemUpgrade[1] != null) {
            mFont.tahoma_7b_black.drawString(var1, T.NS + this.CS + "%", super.BF, super.AJ, 0);
         }
      } else if (ScreenUpgrade.mItemUpgrade[1] != null) {
         String var3 = "< 5";
         if (ScreenUpgrade.mItemUpgrade[1].ID == 226) {
            var3 = "25";
         }

         mFont.tahoma_7b_black.drawString(var1, T.NS + var3 + "%", super.BF, super.AJ, 0);
         if (ScreenUpgrade.mItemUpgrade[1].ID >= 221 && ScreenUpgrade.mItemUpgrade[1].ID <= 226) {
            mFont.tahoma_7b_black.drawString(var1, T.RX + T.WW[ScreenUpgrade.mItemUpgrade[1].ID - 221], super.BF, super.AJ + GameCanvas.hText, 0);
            return;
         }
      }

   }

   public final void AI(mGraphics var1) {
      if (super.BG == 19) {
         AvMain.paintRect(var1, super.AN[0][0], super.AN[0][1] - 2 * super.AD + 5, super.AD, super.AD, (byte)1, 3);
         if (this.CW != null) {
            this.CW.AA(var1, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2 - 2 * super.AD + 5, super.AD, 0, this.CW.colorName, (short)1);
            return;
         }
      } else if (this.CX) {
         AvMain.paintRect(var1, super.AN[0][0], super.AN[0][1] - super.AD - 3, super.AD, super.AD, (byte)1, 3);
         if (this.CW != null) {
            this.CW.AA(var1, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2 - super.AD - 3, super.AD, 0, this.CW.colorName, (short)1);
         }
      }

   }

   public final void AA(mGraphics var1, int var2, int var3, MainItem var4) {
      if (ScreenUpgrade.mItemUpgrade[0] != null && ScreenUpgrade.mItemUpgrade[0].ID == var4.ID && ScreenUpgrade.mItemUpgrade[0].typeObject == var4.typeObject) {
         var1.drawRegion((mImage)AvMain.imgcheck, var2, var3 - super.AD / 2 + 2, 36);
      }

   }

   public final boolean AA(short var1, byte var2) {
      return false;
   }

   public final void AK() {
      super.AO = false;
      this.AH();
   }

   public final mVector getMenuActionItem() {
      mVector var1 = null;
      MainItem var2;
      if ((var2 = (MainItem)super.BN.elementAt(super.AH)) != null) {
         super.AR = var2;
         super.AS.caption = T.GN;
         super.AS.AF = 1;
         byte var3 = super.AR.typeObject;
         short var4 = super.AR.ID;
      }

      if (super.AR != null) {
         var1 = super.AR.getActionSplit();
      }

      return var1;
   }

   public final void setStep() {
      float var10000;
      if (super.BG == 0) {
         if (super.Step != 1) {
            return;
         }

         ++super.BD;
         if (super.BD % 5 != 0) {
            return;
         }

         if (super.CK != 0) {
            if (super.BD >= super.CK) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
               this.AA((short)53, 0, super.AN[1][0] + super.AD / 2, super.AN[1][1] + super.AD / 2, super.AN[1][0] + super.AD / 2, super.AN[1][1] + super.AD / 2);
               super.Step = 0;
               super.BD = 0;
               super.CK = 0;
               ScreenUpgrade.mItemUpgrade[0] = null;
               if (ScreenUpgrade.mItemUpgrade[1] != null) {
                  ScreenUpgrade.mItemUpgrade[1].CE = false;
                  return;
               }
            }

            return;
         }

         var10000 = mSound.volumeSound;
         mSound.playSound();
         var10000 = mSound.volumeSound;
         mSound.playSound();
         this.AA((short)75, 0, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2, super.AN[1][0] + super.AD / 2, super.AN[1][1] + super.AD / 2);
         ScreenUpgrade.mItemUpgrade[0].CE = true;
         super.CK = super.BD + 11 + ((super.AB - super.AG * 3 - super.AK) / 6 << 2) / 5;
      } else {
         if (super.BG == 1 || super.BG == 10 || super.BG == 11 || super.BG == 13 || super.BG == 8 || super.BG == 21) {
            this.AP();
            return;
         }

         if (super.BG != 2 && super.BG != 19 && super.BG != 3 && super.BG != 4 && super.BG != 7 && super.BG != 14 && super.BG != 15) {
            if (super.BG == 12 && super.Step == 1) {
               ++super.BD;
               if (super.BD == 2) {
                  var10000 = mSound.volumeSound;
                  mSound.playSound();
                  this.AA((short)76, 0, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2);
                  this.AA((short)53, 0, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2, super.AN[0][0] + super.AD / 2, super.AN[0][1] + super.AD / 2);
               }

               if (super.BD >= 30) {
                  super.Step = 0;
                  super.BD = 0;
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog(super.showServer);
               }
            }
         } else {
            SplitScreen var1 = this;
            if (super.Step != 1) {
               if (super.Step == 2) {
                  ++super.BD;
                  if (super.BD >= 20) {
                     super.Step = 0;
                     super.BD = 0;
                     super.CK = 0;
                     GameCanvas.Start_Normal_Only_CmdClose_DiaLog(super.showServer);
                     return;
                  }
               }

               return;
            }

            ++super.BD;
            int var2;
            if (super.CK == 0) {
               var10000 = mSound.volumeSound;
               mSound.playSound();
               var10000 = mSound.volumeSound;
               mSound.playSound();
               var2 = 0;
               if (super.BG == 7) {
                  ScreenUpgrade.mItemUpgrade[0].LvUpgrade = 0;
                  var2 = super.AD / 4;
               } else {
                  ScreenUpgrade.mItemUpgrade[0].CE = true;
               }

               this.AA((short)75, 0, super.AN[0][0] + super.AD / 2 + var2, super.AN[0][1] + super.AD / 2 + var2, super.AN[1][0] + super.AD / 2 + var2, super.AN[1][1] + super.AD / 2 + var2);
               if (this.CW != null) {
                  this.AA((short)75, 0, super.AN[0][0] + super.AD / 2 + var2, super.AN[0][1] + super.AD / 2 + var2 - super.AD - 2, super.AN[1][0] + super.AD / 2 + var2, super.AN[1][1] + super.AD / 2 + var2);
                  this.CW = null;
               }

               super.CK = super.BD + 11 + ((super.AB - var2 * 3 - super.AK) / 6 << 2) / 5;
            } else {
               if (super.BD < super.CK) {
                  return;
               }

               var10000 = mSound.volumeSound;
               mSound.playSound();
               var2 = 0;
               if (super.BG == 7) {
                  var2 = super.AD / 4;
               }

               this.AA((short)53, 0, super.AN[1][0] + super.AD / 2 + var2, super.AN[1][1] + super.AD / 2 + var2, super.AN[1][0] + super.AD / 2 + var2, super.AN[1][1] + super.AD / 2 + var2);
               super.Step = 2;
               super.BD = 0;
               super.CK = 0;
               if (ScreenUpgrade.mItemUpgrade[0] != null) {
                  if (super.BG == 7) {
                     ScreenUpgrade.mItemUpgrade[1].LvUpgrade = ReadMessenge.AI;
                  } else {
                     if (super.BG != 4) {
                        ScreenUpgrade.mItemUpgrade[1] = ScreenUpgrade.mItemUpgrade[0];
                     }

                     if (ScreenUpgrade.mItemUpgrade[1] != null) {
                        ScreenUpgrade.mItemUpgrade[1].CE = false;
                     }

                     ScreenUpgrade.mItemUpgrade[0] = null;
                     this.CW = null;
                  }
               }

               if (super.BG != 14) {
                  return;
               }

               for(var2 = 0; var2 < var1.BN.size(); ++var2) {
                  MainItem var3;
                  if ((var3 = (MainItem)var1.BN.elementAt(var2)).typeObject == 3 && var3.ID == var1.CQ) {
                     var3 = new MainItem((byte)3, var3.ID, var3.idIcon, (short)0, var3.colorName, var3.LvUpgrade);
                     ScreenUpgrade.mItemUpgrade[1] = var3;
                  }
               }
            }
         }
      }

   }

   public final void AV() {
      if (super.BG == 2) {
         this.CX = false;
         if (ScreenUpgrade.mItemUpgrade[0] != null) {
            if (ScreenUpgrade.mItemUpgrade[0].numLoKham >= 4) {
               this.CX = true;
               return;
            }

            this.CW = null;
            return;
         }
      } else if (super.BG == 19) {
         this.CX = false;
         if (ScreenUpgrade.mItemUpgrade[0] != null) {
            this.CX = true;
            return;
         }
      } else if (super.BG == 15) {
         this.CX = false;
         if (ScreenUpgrade.mItemUpgrade[0] != null) {
            this.CX = true;
         }
      }

   }

   public final iCommand AJ() {
      return super.BG != 19 && super.BG != 21 ? super.AW : null;
   }

   public final void AA(short var1) {
      for(int var2 = 0; var2 < super.BN.size(); ++var2) {
         if (((MainItem)super.BN.elementAt(var2)).ID == var1) {
            super.BN.removeElement(var2);
            return;
         }
      }

   }

   public final void AW() {
      if (ScreenUpgrade.mItemUpgrade[0] == null) {
         super.AU.caption = T.LJ;
         super.AU.indexMenu = 22;
      } else {
         super.AU.caption = T.LP;
         super.AU.indexMenu = 23;
      }
   }
}
