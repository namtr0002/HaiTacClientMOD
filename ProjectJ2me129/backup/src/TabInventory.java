public final class TabInventory extends MainTabShop {
   private byte IL;
   public iCommand CD;
   public iCommand CE;
   public iCommand CF;
   public iCommand CG;
   public iCommand CH;
   public iCommand cmdSetChestItem;
   public iCommand CJ;
   public iCommand CK;
   private iCommand IM;
   private iCommand IN;
   public iCommand CL;
   public iCommand CM;
   public iCommand CN;
   public iCommand cmdSellMarket;
   private iCommand IO;
   private iCommand IP;
   public iCommand CP;
   public static iCommand CQ;
   public static iCommand CR;
   private static iCommand IQ;
   public static short priceItemSell;
   public static short maxPriceItemSell;
   public static short CU;
   public iCommand CV;
   public static int numPotionTemp = 0;
   public static int numPotionInput = -1;
   public static short focusIDItem = -1;
   private static short numSell = 1;
   private static mVector IS = new mVector("TabInventory.vecsell");
   public static int CZ = 0;

   public TabInventory(String var1, mVector var2, byte var3, int var4) {
      super(var1, var2, Player.maxInventory, var4);
      this.IL = var3;
      super.indexIconTab = 0;
   }

   public final void AA(byte var1) {
      this.IL = var1;
   }

   public final void initCmd() {
      if (this.IL == 7) {
         this.CD = new iCommand(T.cmdUse, 34, this);
         this.CP = new iCommand(T.ME, 35, this);
         super.cmdMenu = new iCommand(T.AS, 10, this);
      } else if (this.IL == 6) {
         this.CD = new iCommand(T.cmdUse, 32, this);
         this.CP = new iCommand(T.ME, 33, this);
         super.cmdMenu = new iCommand(T.AS, 10, this);
      } else if (this.IL == 4) {
         this.CD = new iCommand(T.cmdUse, 0, this);
         super.cmdMenu = new iCommand(T.AS, 10, this);
      } else if (this.IL == 5) {
         this.cmdSellMarket = new iCommand(T.BQ, 27, this);
         super.cmdMenu = new iCommand(T.AS, 10, this);
         this.IP = new iCommand(T.TD, 29, this);
      } else {
         this.CD = new iCommand(T.cmdUse, 0, this);
         this.CE = new iCommand(T.BC, 1, this);
         this.CF = new iCommand(T.cmdUse, 7, this);
         this.CH = new iCommand(T.BQ, 26, 0, this);
         this.CG = new iCommand(T.remove, 26, 1, this);
         this.CK = new iCommand(T.FL, 17, this);
         this.cmdSetChestItem = new iCommand(T.EC, 11, 0, this);
         this.CJ = new iCommand(T.EC, 21, 0, this);
         super.cmdMenu = new iCommand(T.AS, 10, this);
         new iCommand(T.GK, 18, this);
         this.IM = new iCommand(T.HP, 19, this);
         this.IN = new iCommand(T.KD, 28, 0, this);
         this.IO = new iCommand(T.KE, 28, 1, this);
         this.CL = new iCommand(T.CO, 23, this);
         this.CM = new iCommand(T.AS, 24, this);
         this.CN = new iCommand(T.AS, 25, this);
         CQ = new iCommand(T.FH, 14, this);
         CR = new iCommand(T.FJ, 15, this);
         IQ = new iCommand(T.BQ, 16, this);
         super.BU = new iCommand(T.TF, 31, this);
         this.CV = new iCommand(T.cmdQuickOpen, 36, 10, this);
      }
   }

   public final void commandPointer(int index, int subIndex) {
      MainItem var9;
      iCommand var10;
      boolean var11;
      mVector menuActionItem;
      switch(index) {
      case 0:
         if (super.itemCur != null && (super.itemCur.typeObject == 4 || super.itemCur.typeObject == 8)) {
            super.itemCur.Use_Item();
            return;
         }

         return;
      case 1:
         TabInventory tabinven = this;
         mVector var13 = new mVector();

         for(int var3 = 3; var3 < 5; ++var3) {
            iCommand var4;
            if (GameCanvas.isTouch) {
               var4 = new iCommand(T.BD + " " + (var3 + 1), 2, var3, tabinven);
            } else if (TField.AF) {
               var4 = new iCommand(T.BD + " " + T.VY[var3], 2, var3, tabinven);
            } else {
               var4 = new iCommand(T.BD + " " + ((var3 << 1) + 1), 2, var3, tabinven);
            }

            var13.addElement(var4);
         }

         GameCanvas.menu.startAt(var13, 2, T.BC);
         return;
      case 2:
         if (super.itemCur != null && super.itemCur.typeObject == 4) {
            Player.AA(subIndex, (MainSkill)null, super.itemCur);
            Interface_Game.BP = 100;
            return;
         }

         return;
      case 3:
      case 4:
      case 5:
      case 6:
      default:
         break;
      case 7:
         if (super.itemCur == null) {
            return;
         }

         GlobalService.getInstance().AC(super.itemCur.ID, super.itemCur.typeObject);
         return;
      case 8:
         if (super.itemCur == null) {
            return;
         }

         numSell = 1;
         if (super.itemCur.typeObject != 3 && super.input != null) {
            try {
               if ((numSell = (short)Integer.parseInt(super.input.tfInput.getText())) < 0) {
                  numSell = 1;
               }
            } catch (Exception var6) {
               numSell = 1;
            }

            if (numSell > super.itemCur.numPotion) {
               numSell = super.itemCur.numPotion;
            }
         }

         if (subIndex == 0) {
            if (super.itemCur.typeObject == 3) {
               if ((index = 30 + ((super.itemCur.colorName << 1) + super.itemCur.Lv_RQ / 10 + 1) * priceItemSell) > maxPriceItemSell) {
                  index = maxPriceItemSell;
               }
            } else {
               index = numSell * CU;
            }

            GameCanvas.Start_Normal_DiaLog(GameMidlet.Start_Normal_DiaLog(T.HoiSellENG, new String[]{String.valueOf(numSell), super.itemCur.name, String.valueOf(index), T.bery}), new iCommand(T.BQ, 9, 0, this), true);
            return;
         }

         GameCanvas.Start_Normal_DiaLog(GameMidlet.Start_Normal_DiaLog(T.HoiRemoveENG, new String[]{"" + numSell, super.itemCur.name}), new iCommand(T.remove, 9, 1, this), true);
         return;
      case 9:
         if (super.itemCur == null) {
            return;
         }

         GlobalService.getInstance().Sell_Item((byte)subIndex, super.itemCur.ID, super.itemCur.typeObject, numSell);
         GameCanvas.end_Dialog();
         return;
      case 10:
         if ((menuActionItem = this.getMenuActionItem()) != null) {
            GameCanvas.menu.startAt(menuActionItem, 2, T.AU);
            return;
         }
         break;
      case 11:
         if (super.itemCur == null) {
            return;
         }

         GlobalService.getInstance().Chest((byte)1, super.itemCur.ID, (byte)super.itemCur.typeObject, (int)1);
         return;
      case 12:
         GameCanvas.end_Dialog();
         if (super.itemCur == null) {
            return;
         }

         if (super.itemCur.numPotion == 1) {
            GlobalService.getInstance().Chest((byte)1, super.itemCur.ID, (byte)super.itemCur.typeObject, (int)1);
         } else {
            super.input = GameCanvas.Start_Input_Dialog(T.nhapsoluong, new iCommand(T.EC, 13, 0, this), true, super.itemCur.namepaint);
         }

         GameCanvas.subDialog = super.input;
         return;
      case 13:
         if (super.itemCur == null) {
            return;
         }

         var11 = false;

         try {
            if ((index = Integer.parseInt(super.input.tfInput.getText())) < 0) {
               index = 1;
            }
         } catch (Exception var8) {
            index = 1;
         }

         MainItem var12;
         if ((var12 = MainItem.getItemVec(super.itemCur.typeObject, super.itemCur.ID, Player.vecInventory)) != null) {
            if (index > var12.numPotion) {
               index = var12.numPotion;
            }

            GameCanvas.end_Dialog();
            GlobalService.getInstance().Chest((byte)1, super.itemCur.ID, (byte)super.itemCur.typeObject, (int)index);
            return;
         }
         break;
      case 14:
         AC(super.vecShop);
         return;
      case 15:
         AD(super.vecShop);
         return;
      case 16:
         AU();
         return;
      case 17:
         (menuActionItem = new mVector()).addElement(CQ);
         menuActionItem.addElement(CR);
         GameCanvas.menu.startAt(menuActionItem, 2, T.FL);
         return;
      case 18:
         if (super.itemCur == null) {
            return;
         }

         GlobalService.getInstance().Upgrade_Item((byte)1, (short)super.itemCur.ID, (byte)0);
         return;
      case 19:
         menuActionItem = new mVector();
         var10 = new iCommand(T.RD, 20, 1, this);
         menuActionItem.addElement(var10);
         var10 = new iCommand(T.RE, 20, 2, this);
         menuActionItem.addElement(var10);
         var10 = new iCommand(T.RF, 20, 3, this);
         menuActionItem.addElement(var10);
         var10 = new iCommand(T.off, 20, 4, this);
         menuActionItem.addElement(var10);
         GameCanvas.Start_Normal_DiaLog(T.HQ, menuActionItem, false);
         return;
      case 20:
         SaveRms var16;
         if (subIndex == 4) {
            Player.QP = 0;
            var16 = GameCanvas.saveRms;
            SaveRms.AE();
            GameCanvas.end_Dialog();
            return;
         }

         Player.QP = (byte)subIndex;
         var16 = GameCanvas.saveRms;
         SaveRms.AE();
         if (Player.QP == 1 || Player.QP == 3) {
            Player.AD((byte)7);
         }

         if (Player.QP == 2 || Player.QP == 3) {
            Player.CH();
         }

         GameCanvas.end_Dialog();
         return;
      case 21:
         if (super.itemCur == null) {
            return;
         }

         if (super.itemCur.numPotion == 1) {
            GlobalService.getInstance().Chest((byte)1, super.itemCur.ID, (byte)super.itemCur.typeObject, (int)1);
            return;
         }

         menuActionItem = new mVector();
         var10 = new iCommand(T.allMaterial, 22, 0, this);
         menuActionItem.addElement(var10);
         var10 = new iCommand(T.soluong, 12, 0, this);
         menuActionItem.addElement(var10);
         GameCanvas.Start_Normal_DiaLog_New(T.KH + super.itemCur.namepaint, menuActionItem, true, super.itemCur.name);
         return;
      case 22:
         if (super.itemCur == null) {
            return;
         }

         if (subIndex == 0) {
            GlobalService.getInstance().Chest((byte)1, super.itemCur.ID, (byte)super.itemCur.typeObject, (int)super.itemCur.numPotion);
         }

         GameCanvas.end_Dialog();
         return;
      case 23:
         (menuActionItem = new mVector()).addElement(this.IN);
         menuActionItem.addElement(this.IO);
         menuActionItem.addElement(this.IM);
         GameCanvas.menu.startAt(menuActionItem, 2, T.AU);
         return;
      case 24:
         (menuActionItem = new mVector()).addElement(this.CG);
         GameCanvas.menu.startAt(menuActionItem, 2, T.AU);
         return;
      case 25:
         (menuActionItem = new mVector()).addElement(this.CH);
         GameCanvas.menu.startAt(menuActionItem, 2, T.AU);
         return;
      case 26:
         if (super.itemCur.typeObject == 3) {
            this.commandPointer(8, subIndex);
            return;
         }

         if (subIndex == 0) {
            super.input = GameCanvas.Start_Input_Dialog(T.nhapsoluong, new iCommand(T.BQ, 8, 0, this), true, super.itemCur.namepaint);
         } else {
            super.input = GameCanvas.Start_Input_Dialog(T.nhapsoluong, new iCommand(T.remove, 8, 1, this), true, super.itemCur.namepaint);
         }

         GameCanvas.subDialog = super.input;
         return;
      case 27:
         if (super.itemCur == null) {
            return;
         }

         if (super.itemCur.typeObject == 3) {
            GlobalService.getInstance().AC((byte)10, super.AU, super.itemCur.ID, super.itemCur.typeObject, (short)1);
            return;
         }

         super.input = GameCanvas.Start_Input_Dialog(T.nhapsoluong, new iCommand(T.BQ, 30, 1, this), true, super.itemCur.namepaint);
         GameCanvas.subDialog = super.input;
         return;
      case 28:
         if (subIndex == 0) {
            Player.AD((byte)7);
         }

         if (subIndex == 1) {
            Player.CH();
            return;
         }
         break;
      case 29:
         super.input = GameCanvas.Start_Input_Dialog(T.TE, new iCommand(T.BQ, 30, 0, this), true, T.bery);
         GameCanvas.subDialog = super.input;
         return;
      case 30:
         var11 = false;

         try {
            if ((index = Integer.parseInt(super.input.tfInput.getText())) < 0) {
               index = 1;
            }
         } catch (Exception var5) {
            index = 1;
         }

         if (subIndex == 0) {
            GlobalService.getInstance().AC((byte)10, super.AU, (short)0, (byte)4, (short)index);
            return;
         }

         if (super.itemCur == null) {
            return;
         }

         GlobalService.getInstance().AC((byte)10, super.AU, super.itemCur.ID, super.itemCur.typeObject, (short)index);
         return;
      case 31:
         if (GameCanvas.DB == 0) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.TG);

            try {
               CRes.saveRMS("SUB_SSITEM", new byte[]{1});
            } catch (Exception var7) {
               var7.printStackTrace();
            }

            GameCanvas.DB = 1;
         }

         if (super.itemCur != null) {
            var9 = (MainItem)GameScreen.player.LR.get("" + super.itemCur.typeEquip);
            if (subIndex == 0) {
               CZ = var9.LvUpgrade;
               super.vecInfoSS = MainItem.AA(super.itemCur, CZ);
               super.BU.caption = "  +" + super.itemCur.LvUpgrade;
               super.BU.AF = 1;
               return;
            }

            if (subIndex == 1) {
               CZ = 0;
               super.vecInfoSS = MainItem.AA(super.itemCur);
               super.BU.caption = "  +" + var9.LvUpgrade;
               super.BU.AF = 0;
               return;
            }
         }
         break;
      case 32:
         var9 = (MainItem)super.vecShop.elementAt(super.IdSelect);
         GlobalService.getInstance().AA((byte)2, (byte)1, (short)var9.idIcon);
         return;
      case 33:
         var9 = (MainItem)super.vecShop.elementAt(super.IdSelect);
         GlobalService.getInstance().AA((byte)2, (byte)0, (short)var9.idIcon);
         return;
      case 34:
         var9 = (MainItem)super.vecShop.elementAt(super.IdSelect);
         GlobalService.getInstance().AB((byte)4, (byte)1, (short)var9.ID);
         return;
      case 35:
         var9 = (MainItem)super.vecShop.elementAt(super.IdSelect);
         GlobalService.getInstance().AB((byte)4, (byte)0, (short)var9.ID);
         return;
      case 36:
         super.input = GameCanvas.Start_Input_Dialog(T.nhapsoluong, new iCommand(T.XF, 37, 1, this), true, super.itemCur.namepaint);
         GameCanvas.subDialog = super.input;
         return;
      case 37:
         GameCanvas.end_Dialog();
         short var10000 = CRes.AC(super.input.tfInput.getText()) ? (short)Integer.parseInt(super.input.tfInput.getText()) : -1;
         numPotionInput = var10000;
         if (var10000 > super.itemCur.numPotion) {
            numPotionInput = super.itemCur.numPotion;
         }

         if (numPotionInput < 0) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.YQ);
            super.input = GameCanvas.Start_Input_Dialog(T.nhapsoluong, new iCommand(T.XF, 37, 1, this), true, super.itemCur.namepaint);
            GameCanvas.subDialog = super.input;
            return;
         }

         numPotionTemp = numPotionInput;
         focusIDItem = super.itemCur.ID;
         (new Thread(new ShowInfo(this))).start();
      }

   }

   private static void AU() {
      for(int var0 = 0; var0 < IS.size(); ++var0) {
         MainItem var1 = (MainItem)IS.elementAt(var0);
         GlobalService.getInstance().Sell_Item((byte)0, var1.ID, (byte)var1.typeObject, (short)1);
      }

      GameCanvas.end_Dialog();
   }

   private static void AC(mVector var0) {
      IS.removeAllElements();

      for(int var1 = 0; var1 < var0.size(); ++var1) {
         MainItem var2;
         if ((var2 = (MainItem)var0.elementAt(var1)).typeObject == 3 && var2.colorName == 0 && var2.typeEquip >= 0 && var2.typeEquip <= 5) {
            IS.addElement(var2);
         }
      }

      if (IS.size() > 0) {
         GameCanvas.Start_Normal_DiaLog(T.FG + IS.size() + " " + T.FM + " " + T.FH + "?", IQ, true);
      } else {
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.FI);
      }
   }

   private static void AD(mVector var0) {
      IS.removeAllElements();

      for(int var1 = 0; var1 < var0.size(); ++var1) {
         MainItem var2;
         if ((var2 = (MainItem)var0.elementAt(var1)).typeObject == 3 && (var2.colorName == 0 || var2.colorName == 1) && var2.typeEquip >= 0 && var2.typeEquip <= 5) {
            IS.addElement(var2);
         }
      }

      if (IS.size() > 0) {
         GameCanvas.Start_Normal_DiaLog(T.FG + IS.size() + " " + T.FM + " " + T.FJ + "?", IQ, true);
      } else {
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.FK);
      }
   }

   public final mVector getMenuActionItem() {
      mVector var1 = null;
      MainItem var2 = (MainItem)super.vecShop.elementAt(super.IdSelect);

      for(int var3 = 0; var3 < GameScreen.vecQuickOpenPotion.size(); ++var3) {
         ItemQuickOpen var4 = (ItemQuickOpen)GameScreen.vecQuickOpenPotion.elementAt(var3);
         if (var2.ID == var4.AA) {
            var2.CN = true;
         }
      }

      if (var2 != null) {
         super.itemCur = var2;
         var1 = super.itemCur.getActionInven(this.IL);
         if (this.IL == 0) {
            super.vecInfoSS = MainItem.AA(super.itemCur);
         }

         if (this.IL == 5) {
            if (var1 == null) {
               var1 = new mVector();
            }

            var1.addElement(this.IP);
         }
      }

      return var1;
   }

   public final void AO() {
      CZ = 0;
      if (super.itemCur == null || super.itemCur.typeObject != 3 || super.itemCur.charClass != GameScreen.player.clazz && super.itemCur.charClass > 0) {
         if (super.BU != null) {
            super.BU.setPos(-50, -50, AvMain.fraPlus, "");
         }

      } else {
         MainItem var1 = (MainItem)GameScreen.player.LR.get("" + super.itemCur.typeEquip);
         if (super.itemCur != null && var1 != null && super.itemCur.LvUpgrade != var1.LvUpgrade) {
            super.BU = new iCommand(T.TF, 31, 0, this);
            int var2 = super.itemCur.BT - super.itemCur.CO;
            super.BU.setPos(super.AL + super.itemCur.BS - 10, super.AM + var2 - 10, AvMain.fraPlus, "  +" + var1.LvUpgrade);
         } else {
            if (super.BU != null) {
               super.BU.setPos(-50, -50, AvMain.fraPlus, "");
            }

         }
      }
   }

   public final void AA(int var1) {
      super.AD = var1;
      var1 = ((super.AD - 1) / MainTabShop.BW + 1) * MainTab.AE - super.AR + super.AS;
      super.BY = new ListNew(super.AO, super.AP, super.AQ, super.AR, 0, 0, var1, true);
      super.BZ.AA(super.AO + super.AQ + super.AS, super.AP + super.AS / 2, super.AR - (super.AS << 1), -7967666);
   }

   public final void AA(short var1) {
      for(int var2 = 0; var2 < super.vecShop.size(); ++var2) {
         MainItem var3 = (MainItem)super.vecShop.elementAt(var2);
         if (this.IL == 7) {
            if (var3.ID == var1) {
               super.IdSelect = var2;
               var3.colorName = 1;
            } else {
               var3.colorName = 0;
            }
         } else if (var3.idIcon == var1) {
            super.IdSelect = var2;
            var3.colorName = 1;
         } else {
            var3.colorName = 0;
         }
      }

      super.AV = false;
      this.AB(this.getMenuActionItem());
   }

   public static boolean checkFullItem() {
      boolean var0 = false;

      try {
         int var1 = Player.vecInventory.size();
         if (Player.maxInventory - var1 <= 2) {
            var0 = true;
         }

         return var0;
      } catch (Exception var2) {
         return false;
      }
   }
}
