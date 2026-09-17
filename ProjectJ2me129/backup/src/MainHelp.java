public final class MainHelp extends AvMain {
   private int x;
   private int y;
   private int w;
   private int h;
   private int type;
   private int typeSub;
   private int xbegin;
   private int ybegin;
   private int archor;
   private String str;
   private String[] strShow;
   public boolean isBreak;
   public boolean isRemove;
   public boolean isInMap;
   private iCommand cmd;
   private mFont fontPaint;
   private Point_Focus p;
   private RunWord runText;

   public MainHelp(int var1, int var2) {
      super();
      this.runText = new RunWord();
      this.type = var1;
      this.typeSub = var2;
      this.isBreak = true;
      this.w = 100;
      this.fontPaint = mFont.tahoma_7_white;
      label211:
      switch(var1) {
      case 0:
         this.cmd = new iCommand(T.close, 2, this);
         GameCanvas.Start_Normal_DiaLog(T.WK[0], this.cmd, false);
         this.isRemove = true;
         break;
      case 1:
         this.str = T.WK[2];
         this.x = 100;
         this.y = 5;
         this.archor = 0;
         this.AA(this.x, this.y + 20, 0);
         GameScreen.CV = var1;
         SaveRms.AJ();
         break;
      case 2:
         if (this.typeSub == 0) {
            this.str = T.WK[3];
         } else if (this.typeSub == 1) {
            if (GameCanvas.isTouch) {
               this.str = T.WK[5];
            } else {
               this.str = T.WK[4];
            }
         }

         this.strShow = this.fontPaint.splitFontArray(this.str, this.w);
         this.h = this.strShow.length * GameCanvas.hText;
         if (GameCanvas.isTouch && Interface_Game.typeTouch != 1) {
            this.x = Interface_Game.AP[1][0];
            this.y = Interface_Game.AP[1][1] - 30;
            this.archor = 1;
            this.AA(this.x, this.y + this.h, 5);
         } else {
            this.x = Interface_Game.AP[2][0];
            this.y = Interface_Game.AP[2][1] - 30;
            this.archor = 33;
            this.AA(this.x, this.y, 5);
         }

         GameScreen.CV = this.type;
         SaveRms.AJ();
         break;
      case 3:
         if (GameCanvas.isTouch) {
            if (Interface_Game.typeTouch == 1) {
               this.str = T.WK[8];
               this.x = MotherCanvas.hw;
               this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
               this.archor = 33;
            } else {
               this.str = T.WK[7];
               this.x = Interface_Game.xPointMove + 15;
               this.y = Interface_Game.yPointMove - 45;
               this.archor = 0;
               this.strShow = this.fontPaint.splitFontArray(this.str, this.w);
               this.h = this.strShow.length * GameCanvas.hText;
               this.AA(this.x, this.y + this.h, 3);
            }
         } else {
            this.str = T.WK[6];
            this.x = MotherCanvas.hw;
            this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
            this.archor = 33;
         }

         GameScreen.CV = this.type;
         SaveRms.AJ();
         break;
      case 4:
         if (GameCanvas.isTouch) {
            if (Interface_Game.typeTouch == 1) {
               this.str = T.WK[8];
               this.x = MotherCanvas.hw;
               this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
               this.archor = 33;
            } else {
               this.str = T.WK[10];
               this.x = Interface_Game.mPosOther[3][0] - 15;
               this.y = Interface_Game.mPosOther[3][1];
               this.archor = 1;
               this.AA(this.x, this.y + 20, 1);
            }
         } else {
            this.str = T.WK[9];
            this.x = MotherCanvas.hw;
            this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
            this.archor = 33;
         }

         GameScreen.CV = var1;
         SaveRms.AJ();
         break;
      case 5:
         if (GameCanvas.isTouch) {
            this.str = T.WK[18];
            this.x = Interface_Game.mPosOther[2][0] - 15;
            this.y = Interface_Game.mPosOther[2][1] + 20;
            this.archor = -1;
            this.AA(this.x, this.y - 5, 1);
         } else {
            this.str = T.WK[17];
            this.x = MotherCanvas.hw;
            this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
            this.archor = 33;
         }

         GameScreen.CV = var1;
         SaveRms.AJ();
         break;
      case 6:
         if (GameCanvas.isTouch) {
            this.str = T.WK[13];
            this.x = Interface_Game.mPosOther[1][0] + 35;
            this.y = Interface_Game.mPosOther[1][1];
            this.archor = 0;
            this.AA(this.x, this.y + 15, 0);
         } else {
            this.str = T.WK[12];
            this.x = MotherCanvas.hw;
            this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
            this.archor = 33;
         }

         GameScreen.CV = var1;
         SaveRms.AJ();
         break;
      case 7:
         if (GameCanvas.isTouch) {
            this.str = T.WK[15];
            this.x = Interface_Game.xNumMess + 35;
            this.y = Interface_Game.yNumMess;
            this.archor = 0;
            this.AA(this.x, this.y + 15, 0);
         } else {
            this.str = T.WK[14];
            this.x = Interface_Game.xNumMess + 35;
            this.y = Interface_Game.yNumMess;
            this.archor = 0;
            this.AA(this.x, this.y + 15, 0);
         }

         GameScreen.CV = var1;
         SaveRms.AJ();
         break;
      case 8:
         if (GameCanvas.isTouch) {
            this.str = T.WK[20];
         } else {
            this.str = T.WK[19];
         }

         for(var1 = 0; var1 < GameScreen.vecPlayers.size(); ++var1) {
            MainObject var6;
            if ((var6 = (MainObject)GameScreen.vecPlayers.elementAt(var1)).typeObject == 3 || var6.typeObject == 4 || var6.typeObject == 7) {
               this.x = var6.x;
               this.y = var6.y - 40;
               this.archor = 33;
               this.AA(this.x, this.y, 3);
               this.isInMap = true;
               break;
            }
         }

         GameScreen.CV = 9;
         SaveRms.AJ();
         break;
      case 9:
         if (GameCanvas.isTouch) {
            this.str = T.WK[22];
            this.x = 100;
            this.y = 5;
            this.archor = 0;
            this.AA(this.x, this.y + 20, 0);
         } else {
            this.str = T.WK[21];
            this.x = 20;
            this.y = MotherCanvas.h - 30;
            this.archor = 2;
            this.AA(this.x, this.y, 3);
         }

         GameScreen.CV = 10;
         SaveRms.AJ();
         break;
      case 10:
         this.x = MotherCanvas.hw;
         this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
         this.archor = 33;
         if (var2 == 0) {
            this.w = 140;
            this.str = T.WK[23];
            GameScreen.CV = 10;
            SaveRms.AJ();
            this.AA(MainTab.xTab + 22 + 20, MainTab.AI + 36, 0);
         } else if (var2 == 1) {
            this.str = T.WK[24];
            var1 = MainTab.xTab + MainTab.AG / 2 - MainTab.AE * MainTabShop.BW / 2 + 10;
            var2 = MainTab.AI + 32;

            for(int var3 = 0; var3 < Player.vecInventory.size(); ++var3) {
               if (((MainItem)Player.vecInventory.elementAt(var3)).typeObject == 4) {
                  this.AA(var1 + var3 % MainTabShop.BW * MainTab.AE + MainTab.AE / 2 + 14, var2 + var3 / MainTabShop.BW * MainTab.AE + MainTab.AE / 2, 10);
                  break label211;
               }
            }
         }
         break;
      case 11:
         this.x = MotherCanvas.hw;
         this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
         this.archor = 33;
         this.w = 140;
         if (var2 == 0) {
            if (GameCanvas.currentScreen == GameCanvas.tabAllScr) {
               GameCanvas.tabAllScr.idSelect = 1;
               GameCanvas.tabAllScr.setTabSelect();
               GameCanvas.tabAllScr.AC.AB();
            }

            this.str = T.WK[25];
            this.AA(MainTab.xTab + 22 + 20, MainTab.AI + 36 + MainTab.AJ, 0);
         } else if (var2 == 1) {
            this.str = T.WK[26];
            var1 = TabEquip.BO[1][0] + 20;
            var2 = TabEquip.BO[1][1];
            this.AA(var1 + 12, var2 + 12, 10);
         } else if (var2 == 2) {
            this.str = T.WK[27];
         }
         break;
      case 12:
         this.x = MotherCanvas.hw;
         this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
         this.archor = 33;
         if (var2 == 0) {
            this.w = 140;
            this.str = T.WK[36];
            this.AA(MainTab.xTab + 22 + 20, MainTab.AI + 36 + (MainTab.AJ << 1), 0);
            if (GameCanvas.currentScreen == GameCanvas.tabAllScr) {
               GameCanvas.tabAllScr.idSelect = 2;
               GameCanvas.tabAllScr.setTabSelect();
               GameCanvas.tabAllScr.AC.AB();
            }
         } else if (var2 == 1) {
            this.w = 140;
            this.str = T.WK[37];
            this.AA(MainTab.xTab + MainTab.AG / 2 - (MainTab.AG - 70) / 2 + 10 + (MainTab.AG - 22) / 4, MainTab.AI + 20, 0);
         } else if (var2 == 2) {
            this.w = 140;
            this.str = T.WK[38];
            this.AA(MainTab.xTab + MainTab.AG / 2 - (MainTab.AG - 70) / 2 + 10 + (MainTab.AG - 22) / 4 * 3, MainTab.AI + 20, 0);
            if (GameCanvas.currentScreen == GameCanvas.tabAllScr) {
               GameCanvas.tabAllScr.AC.AJ();
            }
         } else if (var2 == 3) {
            this.w = 140;
            this.str = T.WK[39];
         }
         break;
      case 13:
         this.x = MotherCanvas.hw;
         this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
         this.archor = 33;
         this.w = 140;
         this.str = T.WK[32];
         break;
      case 14:
         this.x = MotherCanvas.hw;
         this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
         this.archor = 33;
         if (var2 == 0) {
            this.w = 140;
            this.str = T.WK[28];
            GameScreen.CV = 14;
            SaveRms.AJ();
         } else if (var2 == 1) {
            this.str = T.WK[29];
            this.x = 100;
            this.y = 5;
            this.archor = 0;
            this.AA(this.x, this.y + 20, 0);
         } else if (var2 == 2) {
            this.str = T.WK[30];
            this.AA(MainTab.xTab + 22 + 20, MainTab.AI + 36 + (MainTab.AJ << 2), 0);
         } else if (var2 == 3) {
            this.str = T.WK[31];
         }
         break;
      case 15:
         this.x = MotherCanvas.hw;
         this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
         this.archor = 33;
         if (var2 == 0) {
            this.w = 140;
            this.str = T.WK[33];
            this.AA(MainTab.xTab + 22 + 20, MainTab.AI + 36 + MainTab.AJ * 3, 0);
            if (GameCanvas.currentScreen == GameCanvas.tabAllScr) {
               GameCanvas.tabAllScr.idSelect = 3;
               GameCanvas.tabAllScr.setTabSelect();
               GameCanvas.tabAllScr.AC.AB();
            }
         } else if (var2 == 1) {
            this.w = 140;
            this.str = T.WK[34];
            this.AA(MainTab.xTab + MainTab.AG / 2 - (MainTab.AG - 70) / 2 + 10 + 40 + 20, MainTab.AI + 32 + 24, 0);
         }
         break;
      case 16:
         this.x = MotherCanvas.hw;
         this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
         this.archor = 33;
         GameScreen.CV = 17;
         SaveRms.AJ();
         if (var2 == 0) {
            if (!GameCanvas.isTouch) {
               this.w = 140;
               this.str = T.WK[40];
               this.AA(MainTab.xTab + 22 + 20, MainTab.AI + 36 + MainTab.AJ * 5, 0);
               if (GameCanvas.currentScreen == GameCanvas.tabAllScr) {
                  GameCanvas.tabAllScr.idSelect = 5;
                  GameCanvas.tabAllScr.setTabSelect();
                  GameCanvas.tabAllScr.AC.AB();
               }
            } else {
               this.isRemove = true;
            }
         } else if (var2 == 1) {
            if (GameCanvas.isTouch) {
               this.w = 140;
               this.str = T.WK[41];
               this.archor = 0;
               this.x = Interface_Game.mPosOther[4][0] + 35;
               this.y = Interface_Game.mPosOther[4][1] + 35;
               this.AA(this.x, this.y, 0);
            } else {
               this.isRemove = true;
            }
         }
         break;
      case 17:
         this.isRemove = true;
         break;
      case 18:
         mVector var5;
         (var5 = new mVector()).addElement(new iCommand(T.IO, 1, 1, this));
         var5.addElement(new iCommand(T.IS, 1, 0, this));
         GameCanvas.Start_Normal_DiaLog(T.WK[1], var5, false);
         this.isRemove = true;
         break;
      case 19:
         this.w = 140;
         this.x = MotherCanvas.hw;
         this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
         this.archor = 33;
         this.str = T.WK[42];
         break;
      case 20:
         this.w = 140;
         this.x = MotherCanvas.hw;
         this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
         this.archor = 33;
         this.str = T.WK[43];
         break;
      case 21:
         this.w = 140;
         this.x = MotherCanvas.hw;
         this.y = MotherCanvas.h - (GameCanvas.hCommand << 1);
         this.archor = 33;
         this.str = T.WK[44];
      }

      if (this.str != null) {
         this.strShow = this.fontPaint.splitFontArray(this.str, this.w);
         this.h = this.strShow.length * GameCanvas.hText;
      }

      if (this.archor == 0) {
         this.xbegin = this.x;
         this.ybegin = this.y;
      } else if (this.archor == 1) {
         this.xbegin = this.x - this.w;
         this.ybegin = this.y;
      } else if (this.archor == 33) {
         this.xbegin = this.x - this.w / 2;
         this.ybegin = this.y - this.h;
      } else if (this.archor == 3) {
         this.xbegin = this.x - this.w / 2;
         this.ybegin = this.y - this.h / 2;
      } else if (this.archor == -1) {
         this.xbegin = this.x - this.w;
         this.ybegin = this.y - this.h;
      } else if (this.archor == 2) {
         this.xbegin = this.x;
         this.ybegin = this.y - this.h;
      }

      if (this.str != null) {
         this.runText.AA(this.str, 0, this.xbegin + 3, this.ybegin + 3, this.w, this.fontPaint);
      }

      GameCanvas.clearAll();
   }

   public final void commandPointer(int index, int subIndex) {
      SaveRms var10000;
      switch(index) {
      case 0:
         mVector var3;
         (var3 = new mVector()).addElement(new iCommand(T.IO, 1, 1, this));
         var3.addElement(new iCommand(T.IS, 1, 0, this));
         GameCanvas.Start_Normal_DiaLog(T.WK[1], var3, false);
         break;
      case 1:
         GameCanvas.end_Dialog();
         if (subIndex == 1) {
            GameScreen.CV = 2;
            GameScreen.AB((int)1, (int)0);
         } else {
            GameScreen.CV = -1;
         }

         var10000 = GameCanvas.saveRms;
         SaveRms.AJ();
         break;
      case 2:
         GameCanvas.end_Dialog();
         GameScreen.CV = 18;
         var10000 = GameCanvas.saveRms;
         SaveRms.AJ();
         GlobalService.getInstance().AJ();
      }

      super.commandPointer(index, subIndex);
   }

   public final void paint(mGraphics var1) {
      if (this.strShow != null) {
         AvMain.paintRect(var1, this.xbegin, this.ybegin, this.w + 3, this.h, (byte)1, 4);
         if (this.runText != null) {
            this.runText.AA(var1, 0);
         }
      }

      if (this.p != null) {
         if (AvMain.EA == null) {
            AvMain.EA = mImage.createImage("/interface/hand.png");
            return;
         }

         var1.drawRegion(AvMain.EA, 0, 0, 14, 16, this.p.dis, this.p.x, this.p.y, 3);
      }

   }

   public final void update() {
      if (GameCanvas.currentDialog == null && GameCanvas.subDialog == null && !GameCanvas.menu.isShowMenu) {
         this.handleKeyPress();
         this.updatePointer();
      }

      if (this.runText != null) {
         this.runText.AB();
      }

      if (this.p != null) {
         ++this.p.AG;
         if (this.p.AG < 10) {
            Point_Focus var10000 = this.p;
            var10000.x += this.p.AI;
            var10000 = this.p;
            var10000.y += this.p.vy;
            if (this.p.AI > 0) {
               --this.p.AI;
            }

            if (this.p.AI < 0) {
               ++this.p.AI;
            }

            if (this.p.vy > 0) {
               --this.p.vy;
            }

            if (this.p.vy < 0) {
               ++this.p.vy;
               return;
            }
         } else if (this.p.AG == 15) {
            this.p.x = this.p.AK;
            this.p.y = this.p.AL;
            this.p.AI = this.p.AN;
            this.p.vy = this.p.AO;
            this.p.AG = 0;
         }
      }

   }

   public final void handleKeyPress() {
      switch(this.type) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 19:
      case 20:
      case 21:
         if (GameCanvas.AM()) {
            this.AA();
         }
      case 8:
      case 17:
      case 18:
      default:
      }
   }

   public final void updatePointer() {
      switch(this.type) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 19:
      case 20:
      case 21:
         if (GameCanvas.isPointerSelect) {
            this.AA();
         }
      case 8:
      case 17:
      case 18:
      default:
      }
   }

   private void AA() {
      if (this.runText != null && !this.runText.AA()) {
         GameCanvas.clearAll();
      } else {
         SaveRms var10000;
         switch(this.type) {
         case 1:
            GameScreen.AB((int)3, (int)0);
            break;
         case 2:
            if (this.typeSub == 0) {
               GameScreen.AB((int)2, (int)1);
            } else if (this.typeSub != 1) {
               return;
            }
            break;
         case 3:
            GameScreen.AB((int)2, (int)0);
            break;
         case 4:
            GameScreen.AB((int)5, (int)0);
            break;
         case 5:
            GameScreen.AB((int)6, (int)0);
            break;
         case 6:
            GameScreen.AB((int)7, (int)0);
            GameCanvas.chatTabScr.AB(T.IT, "", T.IU, (byte)0, false);
            break;
         case 7:
            this.isRemove = true;
            GameScreen.CV = 8;
            var10000 = GameCanvas.saveRms;
            SaveRms.AJ();
            return;
         case 8:
         case 17:
         case 18:
         default:
            return;
         case 9:
            this.isRemove = true;
            return;
         case 10:
            if (this.typeSub == 0) {
               GameScreen.AB((int)10, (int)1);
            } else {
               if (this.typeSub != 1) {
                  return;
               }

               GameScreen.AB((int)11, (int)0);
            }
            break;
         case 11:
            if (this.typeSub == 0) {
               GameScreen.AB((int)11, (int)1);
            } else if (this.typeSub == 1) {
               GameScreen.AB((int)11, (int)2);
            } else {
               if (this.typeSub != 2) {
                  return;
               }

               GameScreen.CV = 14;
               var10000 = GameCanvas.saveRms;
               SaveRms.AJ();
            }
            break;
         case 12:
            if (this.typeSub == 0) {
               GameScreen.AB((int)12, (int)1);
            } else if (this.typeSub == 1) {
               GameScreen.AB((int)12, (int)2);
            } else if (this.typeSub == 2) {
               GameScreen.AB((int)12, (int)3);
            } else {
               if (this.typeSub != 3) {
                  return;
               }

               GameScreen.CV = 16;
               var10000 = GameCanvas.saveRms;
               SaveRms.AJ();
            }
            break;
         case 13:
            if (this.typeSub != 0) {
               return;
            }

            GameScreen.CV = 15;
            var10000 = GameCanvas.saveRms;
            SaveRms.AJ();
            break;
         case 14:
            if (this.typeSub != 0 && this.typeSub != 1) {
               if (this.typeSub == 2) {
                  GameScreen.AB((int)14, (int)3);
               } else {
                  if (this.typeSub != 3) {
                     return;
                  }

                  GameScreen.CV = 13;
                  var10000 = GameCanvas.saveRms;
                  SaveRms.AJ();
               }
            }
            break;
         case 15:
            if (this.typeSub == 0) {
               GameScreen.AB((int)15, (int)1);
            } else {
               if (this.typeSub != 1) {
                  return;
               }

               GameScreen.AB((int)12, (int)0);
            }
            break;
         case 16:
            if (this.typeSub != 0 && this.typeSub != 1) {
               return;
            }
         case 19:
         case 20:
         case 21:
         }

         this.isRemove = true;
      }
   }

   private void AA(int var1, int var2, int var3) {
      this.p = new Point_Focus(var1, var2);
      this.p.AK = this.p.x;
      this.p.AL = this.p.y;
      if (var3 == 0) {
         this.p.AI = -5;
         this.p.vy = 0;
      } else if (var3 == 1) {
         this.p.AI = 5;
         this.p.vy = 0;
         this.p.dis = 2;
      } else if (var3 == 2) {
         this.p.AI = 0;
         this.p.vy = -5;
         this.p.dis = 5;
      } else if (var3 != 3 && var3 != 5) {
         if (var3 == 10) {
            this.p.AI = -3;
            this.p.vy = 0;
         }
      } else {
         this.p.AI = 0;
         this.p.vy = 5;
         if (var3 == 3) {
            this.p.dis = 6;
         } else {
            this.p.dis = 7;
         }
      }

      this.p.AN = this.p.AI;
      this.p.AO = this.p.vy;
   }

   public static void AA(int var0) {
      if (GameScreen.CU != null) {
         for(int var1 = 0; var1 < GameScreen.CU.size(); ++var1) {
            MainHelp var2;
            if ((var2 = (MainHelp)GameScreen.CU.elementAt(var1)).type == var0) {
               GameScreen.CU.removeElement(var2);
            }
         }

      }
   }

   public static void AA(boolean var0) {
      if (GameScreen.CV >= 0) {
         switch(GameScreen.CV) {
         case 2:
            GameScreen.AB((int)4, (int)0);
            AA(2);
            return;
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 11:
         case 12:
         case 13:
         default:
            GameScreen.AB((int)GameScreen.CV, (int)0);
            break;
         case 8:
            GameScreen.AB((int)8, (int)0);
            return;
         case 9:
            GameScreen.AB((int)9, (int)0);
            return;
         case 10:
            if (var0) {
               GameScreen.AB((int)10, (int)0);
               return;
            }
            break;
         case 14:
            if (var0 && Player.QI.size() > 0) {
               GameScreen.AB((int)14, (int)2);
               if (GameCanvas.currentScreen == GameCanvas.tabAllScr) {
                  GameCanvas.tabAllScr.idSelect = 4;
                  GameCanvas.tabAllScr.setTabSelect();
                  GameCanvas.tabAllScr.AC.AB();
                  return;
               }
            }
            break;
         case 15:
            if (var0) {
               GameScreen.AB((int)15, (int)0);
               return;
            }
            break;
         case 16:
            if (var0 && !GameCanvas.isTouch) {
               GameScreen.AB((int)16, (int)0);
               return;
            }
         }

      }
   }
}
