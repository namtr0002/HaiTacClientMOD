public final class TabInfo extends MainTab {
   private iCommand BN;
   private ListNew BO;
   private ListNew BP;
   private int BQ = 0;
   private Scroll BR = new Scroll();
   private Scroll BS = new Scroll();
   private int BT;
   private int BU;
   private int BV = 20;
   private int BW;
   private int BX;
   private int[] BY = new int[]{1, 2, 10};
   private static boolean BZ = true;

   public TabInfo(String var1) {
      super.nameTab = var1;
      int var10000 = super.AQ;
      this.AL();
      this.BO = new ListNew();
      super.indexIconTab = 2;
   }

   public final void AB() {
      int var1 = GameScreen.player.AF.size() * GameCanvas.hText - super.AR + (super.AS << 1);
      this.BO = new ListNew(super.AO, super.AP, super.AQ, super.AR, 0, 0, var1, true);
      this.BR.AA(super.AO + super.AQ + super.AS, super.AP + super.AS / 2, super.AR - (super.AS << 1), -7967666);
      this.BO.AC = this.BQ;
      this.BO.AB = this.BQ;
      this.BW = GameCanvas.hText << 2;
      var1 = Player.QF.length * this.BW - super.AR + (super.AS << 1) + GameCanvas.hText * 3 / 2;
      this.BP = new ListNew(super.AO, super.AP, super.AQ, super.AR, 0, 0, var1, true);
      this.BS.AA(super.AO + super.AQ + super.AS, super.AP + super.AS / 2, super.AR - (super.AS << 1), -7967666);
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (super.IdSelect >= 0 && super.IdSelect < Player.QF.length && Player.QF[super.IdSelect].AA < 80) {
            if (Player.AS <= 1) {
               if (Player.AS == 1) {
                  GlobalService.getInstance().Add_Point_Attribute((byte)((byte)super.IdSelect), (short)1);
                  return;
               } else {
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.GG);
                  return;
               }
            } else {
               mVector var8 = new mVector();
               var2 = 0;

               for(int var3 = 0; var3 < this.BY.length; ++var3) {
                  int var4;
                  if ((var4 = this.BY[var3]) > Player.AS) {
                     var4 = Player.AS;
                  }

                  if (var4 > 80 - Player.QF[super.IdSelect].AA) {
                     var4 = 80 - Player.QF[super.IdSelect].AA;
                  }

                  iCommand var5 = new iCommand("+" + var4, 2, var4, this);
                  if (GameCanvas.isTouch) {
                     var5.AH = 3;
                  }

                  if (var2 != var4) {
                     var2 = var4;
                     var8.addElement(var5);
                  }

                  if (this.BY[var3] >= Player.AS) {
                     break;
                  }
               }

               GameCanvas.Start_Normal_DiaLog_New(T.IZ + T.VZ[super.IdSelect] + "?", var8, true, T.tabAttribute);
               return;
            }
         } else {
            return;
         }
      case 1:
         boolean var7 = false;

         try {
            if ((var1 = Integer.parseInt(((InputDialog)null).tfInput.getText())) < 0) {
               var1 = 1;
            }
         } catch (Exception var6) {
            var1 = 1;
         }

         GlobalService.getInstance().Add_Point_Attribute((byte)super.IdSelect, (short)var1);
         GameCanvas.end_Dialog();
         return;
      case 2:
         GlobalService.getInstance().Add_Point_Attribute((byte)super.IdSelect, (short)var2);
         GameCanvas.end_Dialog();
      default:
      }
   }

   private void AL() {
      this.BN = new iCommand(T.BE, 0, this);
      this.BN.setPos(MotherCanvas.hw, MotherCanvas.h - iCommand.hButtonCmdNor / 2, (FrameImage)null, this.BN.caption);
      if (super.levelTab == 1) {
         if (GameCanvas.isKeyPressed()) {
            super.center = this.BN;
            super.DF = super.center;
            return;
         }
      } else {
         super.center = null;
         super.DF = null;
      }

   }
   
//   public final void paintFix(mGraphics g) {
//    g.setColor(-2573687);
//    byte idx = 0;
//    MainScreen var10000 = GameCanvas.currentScreen;
//    if (MainScreen.Pointer((byte)1)) {
//        if (super.levelTab == 1) {
//            g.fillRoundRectNew(MainTab.xTab + 22 + (MainTab.wTab - 22) / 2 - MainTab.wTab / 4 * 3 / 2,
//                    MainTab.yTab + 7, MainTab.wTab / 4 * 3 / 2, 16, 4, 4);
//            idx = 2;
//            AvMain.FontBorderColor(g, T.tabAttribute,
//                    MainTab.xTab + 22 + (MainTab.wTab - 22) / 2 + 15,
//                    MainTab.yTab + 9, 0, 6, 5);
//            mFont.tahoma_7b_black.drawString(g, T.fraCloseTab3,
//                    MainTab.xTab + 22 + (MainTab.wTab - 22) / 2 - 15,
//                    MainTab.yTab + 9, 1);
//        } else if (super.levelTab == 0) {
//            g.fillRoundRectNew(MainTab.xTab + 22 + (MainTab.wTab - 22) / 2,
//                    MainTab.yTab + 7, MainTab.wTab / 4 * 3 / 2, 16, 4, 4);
//            idx = 1;
//            mFont.tahoma_7b_black.drawString(g, T.tabAttribute,
//                    MainTab.xTab + 22 + (MainTab.wTab - 22) / 2 + 15,
//                    MainTab.yTab + 9, 0);
//            AvMain.FontBorderColor(g, T.fraCloseTab3,
//                    MainTab.xTab + 22 + (MainTab.wTab - 22) / 2 - 15,
//                    MainTab.yTab + 9, 1, 6, 5);
//        }
//    } else {
//        mFont.tahoma_7b_black.drawString(g, T.tabAttribute,
//                MainTab.xTab + 22 + (MainTab.wTab - 22) / 2 + 15,
//                MainTab.yTab + 9, 0);
//        mFont.tahoma_7b_black.drawString(g, T.fraCloseTab3,
//                MainTab.xTab + 22 + (MainTab.wTab - 22) / 2 - 15,
//                MainTab.yTab + 9, 1);
//    }
//
//    AvMain.fraTwoTab.drawFrame(idx,
//            MainTab.xTab + 22 + (MainTab.wTab - 22) / 2,
//            MainTab.yTab + 9 + 6, 0, 3, g);
//
//    if (Player.pointAttribute > 0 && GameCanvas.gameTick % 10 < 8) {
//        g.drawImage(MainEvent.imgNew,
//                MainTab.xTab + 22 + (MainTab.wTab - 22) / 2 + 9,
//                MainTab.yTab + 9, 3);
//    }
//
//    GameCanvas.resetTrans(g);
//    AvMain.paintRect(g, super.xCurBegin, super.yCurBegin,
//            super.wCur, super.hCur - super.miniItem, (byte) 0, 4);
//
//    g.setClip(super.xCurBegin - 1, super.yCurBegin + 1,
//            super.wCur + 2, super.hCur - 1 - super.miniItem - 1);
//
//    mGraphics.saveCanvas();
//    mGraphics.Pointer();
//    g.translate(super.xCurBegin, super.yCurBegin);
//
//    if (super.levelTab == 1) {
//        g.translate(-this.xcur, -this.listAttri.isPoint);
//        this.paintTiemNang(g);
//    } else if (super.levelTab == 0) {
//        g.translate(-this.xcur, -this.listInfo.isPoint);
//        this.h(g);
//    }
//
//    mGraphics.indexMenu();
//      mGraphics.VecEffect();
//}

   public final void paint(mGraphics var1) {
    var1.setColor(-2573687);
    byte var2 = 0;
    MainScreen var10000 = GameCanvas.currentScreen;

    if (MainScreen.AD((byte) 1)) {
        if (super.levelTab == 1) {
            var1.fillRoundRectNew(MainTab.xTab + 22 + (MainTab.AG - 22) / 2 - MainTab.AG / 4 * 3 / 2,
                    MainTab.AI + 7, MainTab.AG / 4 * 3 / 2, 16, 4, 4);
            var2 = 2;
            AvMain.FontBorderColor(var1, T.tabAttribute,
                    MainTab.xTab + 22 + (MainTab.AG - 22) / 2 + 15,
                    MainTab.AI + 9, 0, 6, 5);
            mFont.tahoma_7b_black.drawString(var1, T.tabInfo,
                    MainTab.xTab + 22 + (MainTab.AG - 22) / 2 - 15,
                    MainTab.AI + 9, 1);
        } else if (super.levelTab == 0) {
            var1.fillRoundRectNew(MainTab.xTab + 22 + (MainTab.AG - 22) / 2,
                    MainTab.AI + 7, MainTab.AG / 4 * 3 / 2, 16, 4, 4);
            var2 = 1;
            mFont.tahoma_7b_black.drawString(var1, T.tabAttribute,
                    MainTab.xTab + 22 + (MainTab.AG - 22) / 2 + 15,
                    MainTab.AI + 9, 0);
            AvMain.FontBorderColor(var1, T.tabInfo,
                    MainTab.xTab + 22 + (MainTab.AG - 22) / 2 - 15,
                    MainTab.AI + 9, 1, 6, 5);
        }
    } else {
        mFont.tahoma_7b_black.drawString(var1, T.tabAttribute,
                MainTab.xTab + 22 + (MainTab.AG - 22) / 2 + 15,
                MainTab.AI + 9, 0);
        mFont.tahoma_7b_black.drawString(var1, T.tabInfo,
                MainTab.xTab + 22 + (MainTab.AG - 22) / 2 - 15,
                MainTab.AI + 9, 1);
    }

    AvMain.fraTwoTab.drawFrame(var2,
            MainTab.xTab + 22 + (MainTab.AG - 22) / 2,
            MainTab.AI + 9 + 6, 0, 3, var1);

    if (Player.AS > 0 && GameCanvas.gameTick % 10 < 8) {
        var1.drawRegion((mImage) MainEvent.imgNew,
                MainTab.xTab + 22 + (MainTab.AG - 22) / 2 + 9,
                MainTab.AI + 9, 3);
    }

    GameCanvas.resetTrans(var1);
    AvMain.paintRect(var1, super.AO, super.AP,
            super.AQ, super.AR - super.AS, (byte) 0, 4);

    var1.AD(super.AO - 1, super.AP + 1,
            super.AQ + 2, super.AR - 1 - super.AS - 1);

    mGraphics.AC();
    mGraphics.AD();
    var1.translate(super.AO, super.AP);

    if (super.levelTab == 1) {
        var1.translate(-this.BU, -this.BP.AC);
        this.paintTiemNang(var1);
    } else if (super.levelTab == 0) {
        var1.translate(-this.BU, -this.BO.AC);
        this.paintThongTin(var1);
    }

    mGraphics.AE();
    mGraphics.restoreCanvas();
}
private void paintTiemNang(mGraphics var1) {
    if (GameCanvas.isKeyPressed()) {
        int var5 = super.AS / 2 + GameCanvas.hText * 3 / 2;
        int var6 = super.AQ;
        var1.setColor(-330796);
        var1.fillRect(var6 + super.AS / 2, var5 + super.IdSelect * this.BW, 1, this.BW);
        var1.fillRect(var6 + super.AQ - super.AS / 2 - 1, var5 + super.IdSelect * this.BW, 1, this.BW);
        var1.fillRect(var6 + super.AS / 2 + 1, var5 + super.IdSelect * this.BW - 1, super.AQ - super.AS - 1, 1);
        var1.fillRect(var6 + super.AS / 2 + 1, var5 + super.IdSelect * this.BW + this.BW, super.AQ - super.AS - 1, 1);
    }

    int var3 = super.AS;
    int var4 = super.AQ;
    mFont.tahoma_7b_yellow.drawString(var1, T.BJ + ": " + Player.AS, var4 + super.AS, var3, 0);
    var3 += GameCanvas.hText + GameCanvas.hText / 2;

    if (BZ) {
        for (int var5 = 0; var5 < Player.QF.length; ++var5) {
            Class_CV var11 = Player.QF[var5];
            mFont.tahoma_7b_white.drawString(var1, var11.AC + ": " + var11.AA, var4 + this.AS, var3, 0);

            if (var11.AB > 0) {
                int var7 = mFont.tahoma_7b_white.getWidth(var11.AC + ": " + var11.AA + " ");
                mFont.tahoma_7b_blue.drawString(var1, "+" + var11.AB, var4 + this.AS + var7, var3, 0);
            }

            for (int var7 = 0; var7 < var11.AD.length; ++var7) {
                String var8 = MainItem.AA(var11.AD[var7]);
                mFont.tahoma_7_green.drawString(var1, var8, var4 + (this.AS << 1), var3 + (GameCanvas.hText - 2) * (var7 + 1), 0);
            }

            if (!GameCanvas.isSmallScreen && !GameCanvas.lowGraphic) {
                byte var12 = 0;
                if (Player.AS > 0 && var11.AA < 80) {
                    if (this.BX > 0 && var5 == this.IdSelect) {
                        var12 = 1;
                    }
                } else {
                    var12 = 2;
                }

                AvMain.fraButtonTiemNang.drawFrame(var12,
                        var4 + this.AQ - AvMain.fraButtonTiemNang.frameHeight / 2 - (GameCanvas.hText * 3 - AvMain.fraButtonTiemNang.frameWidth / 2) / 2,
                        var3 + GameCanvas.hText * 3 / 2 - this.AS / 2,
                        0, 3, var1);
            }

            var3 += this.BW;
        }
    }

    if (MainScreen.AD((byte) 1)) {
        super.paint(var1);
        if (this.BP.AD > 0) {
            this.BS.AA(var1);
        }
    }
}


   private void paintThongTin(mGraphics var1) {
      for(int var2 = 0; var2 < GameScreen.player.AF.size(); ++var2) {
         MainInfoItem var3;
         String var4 = MainItem.AA(var3 = (MainInfoItem)GameScreen.player.AF.elementAt(var2));
         mFont.tahoma_7_white.drawString(var1, var4, super.AS, super.AS + var2 * GameCanvas.hText, 0);
         if (GameScreen.player.vecEffBuff.size() > 0) {
            int var7 = super.AS + var2 * GameCanvas.hText;
            byte var6 = var3.AA;
            int var8 = 0;

            int var9;
            for(var9 = 0; var9 < GameScreen.player.vecEffBuff.size(); ++var9) {
               MainBuff var10;
               if ((var10 = (MainBuff)GameScreen.player.vecEffBuff.elementAt(var9)).AH.size() > 0) {
                  for(int var11 = 0; var11 < var10.AH.size(); ++var11) {
                     MainInfoItem var12;
                     if ((var12 = (MainInfoItem)var10.AH.elementAt(var11)).AA == var6) {
                        var8 += var12.AE;
                        break;
                     }
                  }
               }
            }

            for(var9 = 0; var9 < GameScreen.player.AG.size(); ++var9) {
               MainInfoItem var13;
               if ((var13 = (MainInfoItem)GameScreen.player.AG.elementAt(var9)).AA == var6) {
                  var8 += var13.AE;
                  break;
               }
            }

            if (var8 != 0) {
               var9 = mFont.tahoma_7_white.getWidth(var4);
               String var14 = MainItem.AA(var8, MainItem.BZ[var6].AC);
               if (var8 > 0) {
                  var14 = "+" + MainItem.AA(var8, MainItem.BZ[var6].AC);
                  mFont.tahoma_7_green.drawString(var1, var14, (super.AS << 1) + var9, var7, 0);
               } else {
                  mFont.tahoma_7_red.drawString(var1, var14, (super.AS << 1) + var9, var7, 0);
               }
            }
         }
      }

      MainScreen var10000 = GameCanvas.currentScreen;
      if (MainScreen.AD((byte)1)) {
         super.paint(var1);
         if (this.BO.AD > 0) {
            this.BR.AA(var1);
         }
      }

   }
   
   public final void update() {
      if (super.levelTab == 0) {
         this.BO.AC();
         this.BR.AA(this.BO.AC, this.BO.AD);
      } else if (super.levelTab == 1) {
         this.BP.AC();
         this.BS.AA(this.BP.AC, this.BP.AD);
      }

      if (this.BX > 0) {
         --this.BX;
      }

      if (this.BU < this.BT && super.levelTab == 1) {
         this.BU += this.BV;
         this.BV += 10;
         if (this.BU > this.BT) {
            this.BU = this.BT;
         }
      }

      if (this.BU > this.BT && super.levelTab == 0) {
         this.BU -= this.BV;
         this.BV += 10;
         if (this.BU < this.BT) {
            this.BU = this.BT;
         }
      }

   }

   public final void handleKeyPress() {
      if (super.levelTab == 0) {
         if (GameCanvas.isKeyPressed(1)) {
            GameCanvas.clearKeyPressed(1);
            this.BO.AA(this.BO.AB - MainTab.AE);
            this.BQ = this.BO.AB;
         } else if (GameCanvas.isKeyPressed(3)) {
            this.BO.AA(this.BO.AB + MainTab.AE);
            this.BQ = this.BO.AB;
            GameCanvas.clearKeyPressed(3);
         } else if (GameCanvas.isKeyPressed(0)) {
            MainScreen var10000 = GameCanvas.currentScreen;
            MainScreen.AC((byte)0);
            GameCanvas.clearKeyPressed(0);
         } else if (GameCanvas.isKeyPressed(2)) {
            GameCanvas.clearKeyPressed(2);
            super.levelTab = 1;
            this.BT = super.AQ;
            this.BV = 20;
            this.BU = 0;
            this.AL();
         }
      } else if (super.levelTab == 1) {
         boolean var1 = false;
         if (GameCanvas.isKeyPressed(1)) {
            GameCanvas.clearKeyPressed(1);
            --super.IdSelect;
            var1 = true;
         } else if (GameCanvas.isKeyPressed(3)) {
            GameCanvas.clearKeyPressed(3);
            ++super.IdSelect;
            var1 = true;
         } else if (GameCanvas.isKeyPressed(0)) {
            GameCanvas.clearKeyPressed(0);
            this.BT = 0;
            this.BV = 20;
            this.BU = super.AQ;
            super.levelTab = 0;
            this.AL();
         }

         if (var1) {
            super.IdSelect = AvMain.AA(super.IdSelect, Player.QF.length - 1, true);
            if (GameCanvas.isKeyPressed()) {
               this.BP.AA((super.IdSelect + 1) * this.BW - super.AR / 2);
            }
         }
      }

      super.handleKeyPress();
      this.AS();
   }

   public final void updatePointer() {
      if (GameCanvas.AB(MainTab.xTab + 22 + (MainTab.AG - 22) / 2 - MainTab.AG / 4 * 3 / 2 - 6, MainTab.AI + 7, MainTab.AG / 4 * 3 - 20, 28)) {
         GameCanvas.isPointerSelect = false;
         if (super.levelTab == 0) {
            super.levelTab = 1;
            this.BT = super.AQ;
            this.BU = 0;
         } else if (super.levelTab == 1) {
            super.levelTab = 0;
            this.BT = 0;
            this.BU = super.AQ;
         }

         this.BV = 20;
         this.AL();
      }

      if (super.levelTab == 0) {
         this.BO.update_Pos_UP_DOWN();
      } else if (super.levelTab == 1) {
         this.BP.update_Pos_UP_DOWN();
         if (GameCanvas.isPointerSelect && GameCanvas.isPoint(super.AO, super.AP, super.AQ, super.AR)) {
            int var1;
            if ((var1 = (GameCanvas.AZ - (super.AP + super.AS + GameCanvas.hText * 3 / 2) + this.BP.AC) / this.BW) >= 0 && var1 < Player.QF.length) {
               super.IdSelect = var1;
               this.BX = 5;
               if (Player.AS > 0) {
                  this.BN.AD();
               }
            }

            GameCanvas.isPointerSelect = false;
         }
      }

      super.updatePointer();
   }

   public static void AA(Class_CV[] var0) {
      BZ = false;
      Player.QF = var0;
      BZ = true;
   }

   public final void AJ() {
      if (super.levelTab == 0) {
         super.levelTab = 1;
      } else {
         super.levelTab = 0;
      }

      this.BT = super.AQ;
      this.BV = 20;
      this.BU = 0;
      this.AL();
   }
}
