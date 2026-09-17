public final class TabShop extends MainTabShop {
   public static iCommand cmdBuyItem;
   public static iCommand CE;
   public static iCommand CF;
   public static iCommand CG;
   public static iCommand CH;
   public static iCommand CI;
   public static iCommand CJ;
   public static iCommand CK;
   private static iCommand CM;
   private static iCommand CN;
   private static MainObject CO = new MainObject();
   public boolean CL = true;
   private int[] CP = new int[]{1, 5, 20};
   private short CQ = -1;
   private short CR = 0;

   public TabShop(String var1, mVector var2, byte var3, int var4) {
      super(var1, var2, var2.size(), var4);
      super.BS = var3;
      super.indexIconTab = 1;
      if (var3 == 6) {
         super.indexIconTab = 8;
      }

      if (super.BS == 103) {
         this.AB(-1, 0);
      } else if (super.BS == 112) {
         this.AB(-1, 0);
      } else if (super.BS == 105 || super.BS == 113 || super.BS == 114) {
         AA(-1, (short[])null);
      }

      CF = new iCommand(T.ship, 11, this);
      CG = new iCommand(T.changeship, 12, this);
      CH = new iCommand(T.AW, 13, this);
      CI = new iCommand(T.ME, 13, this);
      super.cmdMenu = new iCommand(T.AS, 10, this);
      CJ = new iCommand(T.AS, 3, 1, this);
      CE = new iCommand(T.BN, 4, this);
      cmdBuyItem = new iCommand(T.BN, 3, 0, this);
      CK = new iCommand(T.AS, 3, 2, this);
      CM = new iCommand(T.GK, 15, this);
      CN = new iCommand(T.AU, 16, this);
      if (super.BS == 113 || super.BS == 114) {
         CJ = new iCommand(T.AS, 3, 3, this);
      }

      this.AB();
   }

   public final void commandPointer(int var1, int var2) {
      (new StringBuffer("vào commandPointer trong TabShop: ")).append(var1).toString();
      mVector var7;
      switch(var1) {
      case 3:
         if (super.itemCur == null) {
            return;
         }

         if (super.BS != 116 && super.BS != 118) {
            if (var2 == 1) {
               String var9 = T.BG + " " + super.itemCur.AF + " " + T.bery + ".";
               if (super.itemCur.AF == 0) {
                  var9 = T.BG + " " + super.itemCur.AJ + " " + T.EW + ".";
               }

               if (super.itemCur.AF <= 0 && super.itemCur.AJ <= 0) {
                  var9 = "";
               }

               GameCanvas.Start_Normal_DiaLog(T.KV + super.itemCur.name + "?\n" + var9, new iCommand(T.BN, 5, 1, this), true);
               return;
            }

            if (var2 == 2) {
               if (super.itemCur.AJ == 0) {
                  GameCanvas.Start_Normal_DiaLog(T.NK + super.itemCur.name + T.NL, new iCommand(T.AS, 5, 1, this), true);
                  return;
               }

               GameCanvas.Start_Normal_DiaLog(T.NK + super.itemCur.name + T.NL + " " + T.BG + " " + super.itemCur.AJ + " " + T.EW + ".", new iCommand(T.AS, 5, 1, this), true);
               return;
            }

            if (var2 == 3) {
               GameCanvas.Start_Normal_DiaLog(T.NK + super.itemCur.name + "?", new iCommand(T.AS, 5, 1, this), true);
               return;
            }

            GameCanvas.Start_Normal_DiaLog(T.KV + "1 " + super.itemCur.name + "?", new iCommand(T.BN, 5, 1, this), true);
            return;
         }

         GameCanvas.Start_Normal_DiaLog(T.NK + super.itemCur.namepaint + ".", new iCommand(T.QI, 5, 1, this), true);
         return;
      case 4:
         if (super.itemCur == null) {
            return;
         }

         if (super.BS != 116 && super.BS != 118) {
            var7 = new mVector();
            String var10;
            if (super.itemCur.AF > 0) {
               var10 = "(" + super.itemCur.AF + " " + T.bery + " / 1 " + T.IY + ")";
            } else {
               var10 = "(" + super.itemCur.AJ + " " + T.EW + " / 1 " + T.IY + ")";
            }

            for(int var11 = 0; var11 < this.CP.length; ++var11) {
               iCommand var13 = new iCommand("x" + this.CP[var11], 14, this.CP[var11], this);
               if (GameCanvas.isTouch) {
                  var13.AH = 3;
               }

               var7.addElement(var13);
            }

            GameCanvas.Start_Normal_DiaLog_New(T.BO + "\n" + var10, var7, true, super.itemCur.name);
            return;
         }

         GameCanvas.Start_Normal_DiaLog(T.NK + super.itemCur.namepaint + ".", new iCommand(T.QI, 5, 1, this), true);
         return;
      case 5:
         if (super.itemCur == null) {
            return;
         }

         GlobalService.getInstance().AA(super.BS, super.itemCur.ID, (short)1, (byte)super.itemCur.typeObject);
         GameCanvas.AA(T.BP, true);
         return;
      case 6:
         if (super.itemCur == null) {
            return;
         }

         boolean var4 = false;

         int var12;
         try {
            if ((var12 = Integer.parseInt(super.input.tfInput.getText())) < 0) {
               var12 = 1;
            }
         } catch (Exception var5) {
            var12 = 1;
         }

         GameCanvas.end_Dialog();
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.BP);
         GlobalService.getInstance().AA(super.BS, super.itemCur.ID, (short)var12, super.itemCur.typeObject);
         return;
      case 7:
      case 8:
      case 9:
      default:
         break;
      case 10:
         if ((var7 = this.getMenuActionItem()) != null) {
            GameCanvas.menu.startAt(var7, 2, T.AU);
            return;
         }
         break;
      case 11:
         GlobalService.getInstance().AE((byte)1);
         return;
      case 12:
         GlobalService.getInstance().AE((byte)0);
         return;
      case 13:
         if (super.itemCur == null) {
            return;
         }

         GlobalService.getInstance().AC(super.itemCur.ID, super.itemCur.typeObject);
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.BP);
         return;
      case 14:
         if (super.CB <= 0) {
            GlobalService.getInstance().AA(super.BS, super.itemCur.ID, (short)var2, super.itemCur.typeObject);
            super.CB = 5;
            return;
         }

         return;
      case 15:
         if (ScreenUpgrade.BJ == 18) {
            GlobalService.getInstance().AB((byte)7, (short)0, (byte)0, (byte)0);
            return;
         }

         if (ScreenUpgrade.BJ == 15) {
            GlobalService.getInstance().AA((byte)7, (short)0, (byte)0, (byte)0);
            return;
         }

         GlobalService.getInstance().Upgrade_Item((byte)7, (short)0, (byte)0);
         return;
      case 16:
         mVector var8 = new mVector();
         iCommand var3 = new iCommand(T.LN, 17, 9, this);
         var8.addElement(var3);
         var3 = new iCommand(T.LR, 17, 12, this);
         var8.addElement(var3);
         var3 = new iCommand(T.LO, 17, 10, this);
         var8.addElement(var3);
         iCommand var6 = new iCommand(T.LQ, 17, 13, this);
         var8.addElement(var6);
         GameCanvas.menuCur.startAt(var8, 2, T.AU);
         return;
      case 17:
         GlobalService.getInstance().Upgrade_Item((byte)((byte)var2), (short)0, (byte)0);
      }

   }

   public final mVector getMenuActionItem() {
      mVector var1 = null;
      MainItem var2;
      if ((var2 = (MainItem)super.vecShop.elementAt(super.IdSelect)) != null) {
         super.itemCur = var2;
         if (!this.CL) {
            return null;
         }

         var1 = super.itemCur.getActionShop(super.BS);
         if (super.BS == 103) {
            this.AB(7, super.itemCur.idIcon);
         } else if (super.BS == 112) {
            this.AB(6, super.itemCur.idIcon);
         } else if (super.BS == 105 || super.BS == 113 || super.BS == 114) {
            AA(7, super.itemCur.BI);
         }
      } else if (super.BS == 103) {
         this.AB(7, GameScreen.player.BW);
      } else if (super.BS == 112) {
         this.AB(6, GameScreen.player.BS);
      } else if (super.BS == 105) {
         AA(-1, (short[])null);
      }

      return var1;
   }

   public final void AA(mGraphics var1, MainItem var2, int var3, int var4) {
      if (MainTabShop.BQ != null && var2.typeObject == MainTabShop.BQ.typeObject && var2.ID == MainTabShop.BQ.ID && super.BS == 101) {
         var1.drawRegion((mImage)AvMain.imgcheck, var3, var4, 40);
      }

   }

   public final void AB(mGraphics var1, int var2, int var3) {
      if (super.BS == 102) {
         if (GameScreen.player.BO != null) {
            for(int var4 = 0; var4 < GameScreen.player.BO.length; ++var4) {
               if (super.itemCur != null && var4 == super.itemCur.AW) {
                  Class_DV.AA(var1, super.itemCur.AN, super.itemCur.AW, var2, var3, 0, 0);
               } else {
                  Class_DV.AA(var1, GameScreen.player.BO[var4], var4, var2, var3, 0, 0);
               }
            }

            Class_DV.AA(var1, (short)0, 100, var2, var3, 0, 0);
            return;
         }
      } else if (super.BS == 103 || super.BS == 105 || super.BS == 112 || super.BS == 113 || super.BS == 114) {
         CO.AC(var1, var2, var3 + 4);
         CO.AA(var1, var2, var3 + 4, true);
      }

   }

   public final void AA(short var1, byte var2) {
      for(int var3 = 0; var3 < super.vecShop.size(); ++var3) {
         MainItem var4;
         if ((var4 = (MainItem)super.vecShop.elementAt(var3)).typeObject == var2 && var4.ID == var1) {
            var4.AF = 0;
            var4.AJ = 0;
            var4.vecInfo.removeAllElements();
            var4.addInfoFrist(T.IV, (byte)1);
            var4.CG = true;
            if (var4.info != null && var4.info.length() > 0) {
               var4.AA(var4.info);
            }

            var4.colorName = 1;
            return;
         }
      }

   }

   public final void AI() {
      if (super.BS != 103 && super.BS != 112) {
         int var4;
         MainItem var5;
         if (super.BS == 102) {
            for(var4 = 0; var4 < super.vecShop.size(); ++var4) {
               if ((var5 = (MainItem)super.vecShop.elementAt(var4)).AF == 0 && var5.AJ == 0) {
                  for(int var6 = 0; var6 < GameScreen.player.BO.length; ++var6) {
                     if (var5.AW == var6) {
                        var5.vecInfo.removeAllElements();
                        if (var5.AN == GameScreen.player.BO[var6]) {
                           var5.colorName = 4;
                           var5.addInfoFrist(T.daTrangBi, (byte)4);
                        } else {
                           var5.colorName = 1;
                           var5.addInfoFrist(T.IV, (byte)1);
                        }
                     }
                  }
               }
            }
         } else if (super.BS == 105 || super.BS == 113) {
            for(var4 = 0; var4 < super.vecShop.size(); ++var4) {
               if ((var5 = (MainItem)super.vecShop.elementAt(var4)).AF == 0 && var5.AJ == 0) {
                  var5.vecInfo.removeAllElements();
                  if (var5.ID == Player.idFashion) {
                     var5.addInfoFrist(T.daTrangBi, (byte)4);
                     var5.colorName = 4;
                     var5.CG = true;
                     if (var5.info.length() > 0) {
                        var5.AA(var5.info);
                     }
                  } else {
                     var5.addInfoFrist(T.IV, (byte)1);
                     var5.colorName = 1;
                     var5.CG = true;
                     if (var5.info.length() > 0) {
                        var5.AA(var5.info);
                     }
                  }
               }
            }
         }
      } else {
         short var1 = GameScreen.player.BW;
         if (super.BS == 112) {
            var1 = GameScreen.player.BS;
         }

         for(int var2 = 0; var2 < super.vecShop.size(); ++var2) {
            MainItem var3;
            if ((var3 = (MainItem)super.vecShop.elementAt(var2)).AF == 0 && var3.AJ == 0) {
               var3.vecInfo.removeAllElements();
               if (var3.idIcon == var1) {
                  var3.addInfoFrist(T.daTrangBi, (byte)4);
                  var3.colorName = 4;
               } else {
                  var3.addInfoFrist(T.IV, (byte)1);
                  var3.colorName = 1;
               }
            }
         }
      }

      this.AB(this.getMenuActionItem());
   }

   private void AB(int var1, int var2) {
      if (var1 == -1) {
         if (GameScreen.player != null) {
            CO.AA(GameScreen.player.BS);
            CO.AB(GameScreen.player.BW);
            CO.BT = GameScreen.player.BT;
            CO.BU = GameScreen.player.BU;
            CO.BV = GameScreen.player.BV;
            CO.clazz = GameScreen.player.clazz;
            CO.PR = 0;
            CO.PS = 0;
            CO.BH();
            return;
         }
      } else {
         if (var1 == 7) {
            CO.BC();
            CO.AB((short)var2);
            this.CQ = (short)var2;
            CO.PR = 0;
            CO.PS = 0;
            return;
         }

         if (var1 == 6) {
            CO.AA((short)var2);
         }
      }

   }

   private static void AA(int var0, short[] var1) {
      if (GameScreen.player != null) {
         CO.AA(GameScreen.player.BS);
         CO.AB(GameScreen.player.BW);
         CO.BT = GameScreen.player.BT;
         CO.BU = GameScreen.player.BU;
         CO.BV = GameScreen.player.BV;
         CO.BX = GameScreen.player.BX;
         CO.PR = 0;
         CO.PS = 0;
         CO.clazz = GameScreen.player.clazz;
         CO.BH();
      }

      if (var0 != -1) {
         CO.AC(var1);
         CO.clazz = GameScreen.player.clazz;
         CO.BH();
      }

   }

   public final iCommand AN() {
      if (super.BS == 6) {
         return CM;
      } else {
         return super.BS == 111 ? CN : null;
      }
   }

   public final void AL() {
      if ((super.BS == 103 || super.BS == 105 || super.BS == 112 || super.BS == 113 || super.BS == 114) && CO != null) {
         if (GameCanvas.gameTick % 100 == 0) {
            if (CO.isDonotShowHat == 1) {
               CO.isDonotShowHat = 0;
            } else {
               CO.isDonotShowHat = 1;
            }
         }

         if (super.BS == 103 && this.CQ == 772 && GameCanvas.gameTick % 60 == 0) {
            CO.AB((short)(this.CQ + this.CR % 3));
            CO.PR = 0;
            ++this.CR;
         }
      }

   }
}
