public class Menu extends AvMain {
   public boolean isShowMenu;
   public mVector menuItems = new mVector("Menu.menuItems");
   public int AC;
   public int menuX;
   public int menuY;
   public int menuW;
   public int menuH;
   public int menuTemY;
   private int hPlus;
   public int cmtoX;
   public int cmx;
   private int cmxLim;
   private int xc;
   private int pos;
   public int sizeMenu;
   private String nameMenu = "";
   private String[] AV;
   public RunWord AL;
   private static boolean AW = true;
   private static boolean AX = true;
   private static byte isNPCMenu;
   public byte AM = 0;
   public iCommand cmdClose;
   public int AO;
   private int AZ;
   private byte BA;
   private byte BB = 0;
   private int BC;
   private int BD;
   private int BE;
   public int wUni;
   private int BF;
   private int BG;
   private int[] BH = new int[3];
   private boolean BI;
   private boolean BJ;
   private int BK;
   private int BL;
   private int BM = 2;
   private int BN;
   private int BO;
   private int BP = 10;

   public final void beginMenu() {
      this.cmdClose = new iCommand(T.close, 1, this);
      AX = false;
      this.BK = 0;
      this.AL = null;
      super.DB = null;
      this.AC = 0;
      this.menuItems.removeAllElements();
      super.backCMD = this.cmdClose;
   }

   public final void startAt(mVector menuItems, int pos, String name) {
      this.beginMenu();
      isNPCMenu = 0;
      this.nameMenu = name;
      this.pos = pos;
      if (menuItems != null && menuItems.size() != 0) {
         this.menuItems = menuItems;
         this.isShowMenu = true;
         iCommand var6;
         if (pos == -1) {
            this.menuItems.addElement(this.cmdClose);
            this.hPlus = 0;
            this.menuW = 60;
            this.menuH = 60;

            for(pos = 0; pos < menuItems.size(); ++pos) {
               var6 = (iCommand)menuItems.elementAt(pos);
               if (mFont.tahoma_7_yellow.getWidth(var6.caption) > this.menuW - 8) {
                  var6.subCaption = mFont.tahoma_7b_yellow.splitFontArray(var6.caption, this.menuW - 8);
               }
            }

            this.wUni = menuItems.size() * this.menuW - 1;
            if (this.wUni > MotherCanvas.w - 2) {
               this.wUni = MotherCanvas.w - 2;
            }

            this.menuX = MotherCanvas.hw - this.wUni / 2;
            if (this.menuX <= 0) {
               this.menuX = 1;
            }

            this.menuY = MotherCanvas.h - this.menuH - (GameCanvas.hCommand + 1);
            if (GameCanvas.isTouch) {
               this.menuY -= 3;
            }

            this.menuY += 27;
            this.menuTemY = this.menuY;
            this.cmxLim = this.menuItems.size() * this.menuW - MotherCanvas.w;
            if (this.cmxLim < 0) {
               this.cmxLim = 0;
            }

            this.cmtoX = 0;
            this.cmx = 0;
            this.xc = 50;
         } else {
            this.menuW = GameCanvas.hCommand;
            if (GameCanvas.isTouch) {
               this.menuW = 32;
            }

            this.sizeMenu = MotherCanvas.h / 4 * 3 / this.menuW - 1;
            this.wUni = MotherCanvas.w / 3;
            if (this.wUni < mFont.tahoma_7b_white.getWidth(name) + 30) {
               this.wUni = mFont.tahoma_7b_white.getWidth(name) + 30;
            }

            this.hPlus = GameCanvas.hCommand;
            pos = 120;
            for(int var4 = 0; var4 < menuItems.size(); ++var4) {
               var6 = (iCommand)menuItems.elementAt(var4);
               int var5;
               if ((var5 = mFont.tahoma_7b_white.getWidth(var6.caption) + 30) > pos) {
                  pos = var5;
               }
               if (var6.AK > 0 && var5 + var6.AK > pos) {
                  pos = var5 + var6.AK;
               }
            }
            if (this.wUni < pos) {
               this.wUni = pos;
            }

            if (this.wUni > MotherCanvas.w) {
               this.wUni = MotherCanvas.w;
            }

            this.cmtoX = 0;
            this.cmx = 0;
            iCommand var7 = null;
            if (GameCanvas.isTouch) {
               var7 = this.cmdClose;
            } else {
               this.menuItems.addElement(this.cmdClose);
            }

            if (menuItems.size() > this.sizeMenu) {
               this.menuH = this.sizeMenu * this.menuW + 8;
               this.cmxLim = (menuItems.size() - this.sizeMenu) * this.menuW;
            } else {
               this.menuH = menuItems.size() * this.menuW + 8;
               this.cmxLim = 0;
            }

            this.AI();
            this.menuTemY = this.menuY;
            if (var7 != null) {
               var7.setPos(this.menuX + this.wUni - 11, this.menuY - this.hPlus + GameCanvas.hCommand / 2 + 1 + 3, MainTab.fraCloseTab, "");
               super.DB = var7;
            }
         }

         if (GameCanvas.isKeyPressed()) {
            this.AC = 0;
         } else {
            this.AC = -1;
         }

         AX = true;
         this.AB();
         GameCanvas.AA(GameCanvas.menu);
      }
   }

   public final void updateMenuGame(mVector var1) {
      this.menuItems = var1;
   }

   public final void AA(mVector var1, int var2, int var3, int var4, String var5) {
      this.beginMenu();
      if (var1 != null) {
         this.nameMenu = var5;
         isNPCMenu = 0;
         this.AO = var3;
         this.AZ = var4;
         this.pos = var2;
         this.isShowMenu = true;
         this.menuItems = new mVector();
         this.menuW = GameCanvas.hCommand;
         if (GameCanvas.isTouch) {
            this.menuW = 32;
         }

         this.sizeMenu = MotherCanvas.h / 4 * 3 / this.menuW - 1;
         this.wUni = MotherCanvas.w / 3;
         this.hPlus = GameCanvas.hCommand;
         var2 = 120;
         if (120 < mFont.tahoma_7b_white.getWidth(var5) + 45) {
            var2 = mFont.tahoma_7b_white.getWidth(var5) + 45;
         }

         for(var3 = 0; var3 < var1.size(); ++var3) {
            iCommand var7;
            (var7 = (iCommand)var1.elementAt(var3)).indexMenu = 2;
            var7.Pointer = this;
            int var8 = mFont.tahoma_7b_white.getWidth(var7.caption) + 30;
            if (var7.AJ != null) {
               var8 += var7.AJ.frameWidth;
            }

            if (var7.AW != null) {
               var8 += 20;
            }

            if (var8 > var2) {
               var2 = var8;
            }
         }

         this.menuItems = var1;
         iCommand var6 = null;
         if (GameCanvas.isTouch) {
            var6 = this.cmdClose;
         } else {
            this.menuItems.addElement(this.cmdClose);
         }

         this.wUni = var2;
         if (this.wUni > MotherCanvas.w) {
            this.wUni = MotherCanvas.w;
         }

         if (this.menuItems.size() > this.sizeMenu) {
            this.menuH = this.sizeMenu * this.menuW + 8;
            this.cmxLim = (this.menuItems.size() - this.sizeMenu) * this.menuW;
         } else {
            this.menuH = this.menuItems.size() * this.menuW + 8;
            this.cmxLim = 0;
         }

         this.cmtoX = 0;
         this.cmx = 0;
         this.AI();
         this.menuTemY = this.menuY;
         if (var6 != null) {
            var6.setPos(this.menuX + this.wUni - 8, this.menuY - this.hPlus + GameCanvas.hCommand / 2 + 1 + 3, MainTab.fraCloseTab, "");
            super.DB = var6;
         }

         if (GameCanvas.isKeyPressed()) {
            this.AC = 0;
         } else {
            this.AC = -1;
         }

         AX = true;
         this.AB();
         GameCanvas.AA(GameCanvas.menu);
      }
   }

   public final void AA(mVector var1, String var2, int var3, byte var4, boolean var5, int var6, boolean var7) {
      this.beginMenu();
      isNPCMenu = 1;
      this.nameMenu = var2;
      this.AZ = var3;
      this.BA = var4;
      this.BD = var6;
      if (var1 != null && var1.size() != 0) {
         this.menuItems = var1;
      } else {
         this.menuItems = new mVector();
      }

      int var8;
      iCommand var11;
      for(var8 = 0; var8 < this.menuItems.size(); ++var8) {
         var11 = (iCommand)this.menuItems.elementAt(var8);
         if (!var5) {
            var11.setTypeSpec();
         }

         if (var7) {
            var11.indexMenu = 3;
            var11.AF = var8;
            var11.Pointer = this;
         }

         if (var8 == 0 && !GameCanvas.isTouch) {
            var11.AG = true;
         }
      }

      this.isShowMenu = true;
      this.menuW = GameCanvas.hCommand;
      if (GameCanvas.isTouch) {
         this.menuW = 32;
      }

      this.sizeMenu = 0;
      this.wUni = MotherCanvas.w - 10;
      if (this.wUni > 300) {
         this.wUni = 300;
      }

      this.AV = mFont.tahoma_7_black.splitFontArray(var2, this.wUni - 20);
      this.hPlus = GameCanvas.hCommand;
      this.cmtoX = 0;
      this.cmx = 0;
      var8 = this.AV.length;
      var11 = this.cmdClose;
      if (!var5) {
         var11.setTypeSpec();
      }

      this.menuItems.addElement(var11);
      this.menuH = (var8 + 2) * GameCanvas.hText + iCommand.hButtonCmdSpec + 5;
      this.cmxLim = 0;
      this.menuX = MotherCanvas.hw - this.wUni / 2;
      this.menuY = MotherCanvas.h - this.menuH - 10;
      this.menuTemY = this.menuY;
      this.AL = new RunWord();
      this.AL.AA(var2, 0, this.menuX + 10, this.menuY + 10 + GameCanvas.hText, this.wUni - 20, mFont.tahoma_7_white);
      int var9 = iCommand.hButtonCmdSpec;
      Menu var10 = this;
      var3 = this.menuItems.size();
      this.BE = var3 * ((iCommand.wButtonCmd + 6) / 2) - 3;

      for(int var12 = 0; var12 < var3; ++var12) {
         iCommand var13;
         (var13 = (iCommand)var10.menuItems.elementAt(var12)).setPos(var10.menuX + iCommand.wButtonCmd / 2 + var10.wUni / 2 - var10.BE + var12 * (iCommand.wButtonCmd + 6), var10.menuY + var10.menuH - var9 / 2 - 5, (FrameImage)null, var13.caption);
         if (GameCanvas.isKeyPressed() && var12 == 0) {
            var13.AG = true;
         }
      }

      if (GameCanvas.isKeyPressed()) {
         this.AC = 0;
      } else {
         this.AC = -1;
      }

      AX = true;
      this.AB();
      GameCanvas.AA(GameCanvas.menu);
   }

   public final void AA(mVector var1, String var2, int var3, byte var4, boolean var5, int var6) {
      this.beginMenu();
      isNPCMenu = 1;
      this.nameMenu = var2;
      this.AZ = var3;
      this.BA = var4;
      this.BD = 0;
      if (var1 != null && var1.size() != 0) {
         this.menuItems = var1;
      } else {
         this.menuItems = new mVector();
      }

      this.isShowMenu = true;
      this.menuW = GameCanvas.hCommand;
      if (GameCanvas.isTouch) {
         this.menuW = 32;
      }

      this.sizeMenu = 0;
      this.wUni = MotherCanvas.w - 10;
      if (this.wUni > 300) {
         this.wUni = 300;
      }

      this.AV = mFont.tahoma_7_black.splitFontArray(var2, this.wUni - 20);
      this.hPlus = GameCanvas.hCommand;
      this.cmtoX = 0;
      this.cmx = 0;
      if ((var3 = this.AV.length) < 3) {
         var3 = 3;
      }

      this.menuH = (var3 + 2) * GameCanvas.hText + iCommand.hButtonCmdNor / 2;
      this.cmxLim = 0;
      this.menuX = MotherCanvas.hw - this.wUni / 2;
      this.menuY = MotherCanvas.h - this.menuH - 10;
      this.menuTemY = this.menuY;
      this.AL = new RunWord();
      this.AL.AA(var2, 0, this.menuX + 10, this.menuY + 10 + GameCanvas.hText, this.wUni - 20, mFont.tahoma_7_white);

      for(int var7 = 0; var7 < var1.size(); ++var7) {
         iCommand var8;
         (var8 = (iCommand)var1.elementAt(var7)).setPos(this.menuX + this.wUni - iCommand.wButtonCmd / 2, this.menuY + this.menuH - iCommand.hButtonCmdNor / 2, (FrameImage)null, var8.caption);
         var8.AA(this.menuX, this.menuY, this.wUni, this.menuH);
         var8.AA(true);
      }

      if (GameCanvas.isKeyPressed()) {
         this.AC = 0;
      } else {
         this.AC = -1;
      }

      AX = true;
      this.AB();
      GameCanvas.AA(GameCanvas.menu);
   }

   public final void AB(mVector var1) {
      this.beginMenu();
      isNPCMenu = 0;
      this.AC = 0;
      this.menuItems = var1;
      this.isShowMenu = true;
      this.hPlus = GameCanvas.hCommand;
      this.sizeMenu = this.menuH / this.menuW;
      this.cmtoX = 0;
      this.cmx = 0;
      if (this.sizeMenu - 1 < var1.size()) {
         this.cmxLim = var1.size() * this.menuW - this.menuH;
      } else {
         this.cmxLim = 0;
      }

      if (this.cmxLim < 0) {
         this.cmxLim = 0;
      }

      if (GameCanvas.isKeyPressed()) {
         this.AC = 0;
      } else {
         this.AC = -1;
      }

      AX = true;
      this.AB();
   }

   public final void AB() {
      for(int var1 = 0; var1 < this.BH.length; ++var1) {
         this.BH[var1] = 0;
      }

      this.BF = 0;
      this.BG = 0;
      this.BI = false;
      this.BJ = false;
      this.BK = 0;
      this.BL = 0;
      this.BB = 0;
      if (GameScreen.player != null) {
         GameScreen.player.resetAction();
      }

   }

   private void AI() {
      switch(this.pos) {
      case 0:
         this.menuX = 2;
         this.menuY = MotherCanvas.h - GameCanvas.hCommand - this.menuH - 2;
         if (GameCanvas.isTouch) {
            this.menuY += GameCanvas.hCommand;
            return;
         }
         break;
      case 1:
         this.menuX = MotherCanvas.w - this.wUni - 2;
         this.menuY = MotherCanvas.h - GameCanvas.hCommand - this.menuH - 2;
         if (GameCanvas.isTouch) {
            this.menuY += GameCanvas.hCommand;
            return;
         }
         break;
      case 2:
      case 4:
         this.menuX = MotherCanvas.hw - this.wUni / 2;
         this.menuY = MotherCanvas.h / 2 - this.menuH / 2 - 2 + this.menuW / 2 + 6 - GameCanvas.hCommand / 2;
         return;
      case 3:
         this.menuX = 2;
         this.menuY = 2;
      }

   }

   public void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         iCommand var3;
         if (!(var3 = (iCommand)this.menuItems.elementAt(this.AC)).isDonotCloseMenu) {
            this.isShowMenu = false;
         }

         AA(var3);
         break;
      case 1:
         this.AG();
         break;
      case 2:
         GlobalService.getInstance().AA((short)this.AZ, (byte)this.AO, (byte)this.AC);
         this.isShowMenu = false;
         GameCanvas.isPointerSelect = false;
         break;
      case 3:
         GlobalService.getInstance().AA((short)this.AZ, (byte)this.AO, (byte)var2);
         this.isShowMenu = false;
         GameCanvas.isPointerSelect = false;
      }

      super.commandPointer(var1, var2);
   }

   public void AF() {
      if (AW) {
         if (this.isShowMenu) {
            if (this.AM > 0) {
               --this.AM;
            }

            boolean var1 = false;
            int var2;
            if (isNPCMenu == 1) {
               var2 = this.AC;
               if (!GameCanvas.isKeyPressed(0) && !GameCanvas.isKeyPressed(1)) {
                  if (GameCanvas.isKeyPressed(2) || GameCanvas.isKeyPressed(3)) {
                     ++this.AC;
                     GameCanvas.clearKeyPressed(2);
                     GameCanvas.clearKeyPressed(3);
                  }
               } else {
                  --this.AC;
                  GameCanvas.clearKeyPressed(0);
                  GameCanvas.clearKeyPressed(1);
               }

               this.AC = AvMain.AA(this.AC, this.menuItems.size() - 1, false);
               if (var2 != this.AC && GameCanvas.isKeyPressed()) {
                  for(var2 = 0; var2 < this.menuItems.size(); ++var2) {
                     iCommand var3 = (iCommand)this.menuItems.elementAt(var2);
                     if (var2 == this.AC) {
                        var3.AG = true;
                     } else {
                        var3.AG = false;
                     }
                  }
               }

               if (GameCanvas.AL[5]) {
                  GameCanvas.AB(5);
                  if (this.AC < this.menuItems.size() && this.AC >= 0) {
                     ((iCommand)this.menuItems.elementAt(this.AC)).AD();
                  }
               }
            } else if (isNPCMenu == 0) {
               if (this.pos == -1) {
                  if (GameCanvas.isKeyPressed(0)) {
                     var1 = true;
                     --this.AC;
                     if (this.AC < 0) {
                        this.AC = this.menuItems.size() - 1;
                     }

                     GameCanvas.clearKeyPressed(0);
                  } else if (GameCanvas.isKeyPressed(2)) {
                     var1 = true;
                     ++this.AC;
                     if (this.AC > this.menuItems.size() - 1) {
                        this.AC = 0;
                     }

                     GameCanvas.clearKeyPressed(2);
                  }
               } else if (GameCanvas.isKeyPressed(1)) {
                  var1 = true;
                  --this.AC;
                  if (this.AC < 0) {
                     this.AC = this.menuItems.size() - 1;
                  }

                  GameCanvas.clearKeyPressed(1);
               } else if (GameCanvas.isKeyPressed(3)) {
                  var1 = true;
                  ++this.AC;
                  if (this.AC > this.menuItems.size() - 1) {
                     this.AC = 0;
                  }

                  GameCanvas.clearKeyPressed(3);
               }
            }

            if (var1) {
               if (this.pos == -1) {
                  this.cmtoX = this.AC * this.menuW + this.menuW - MotherCanvas.w / 2;
               } else {
                  this.cmtoX = (this.AC + 1) * this.menuW - this.menuH / 2;
               }

               if (this.cmtoX > this.cmxLim) {
                  this.cmtoX = this.cmxLim;
               }

               if (this.cmtoX < 0) {
                  this.cmtoX = 0;
               }

               if (this.AC == this.menuItems.size() - 1 || this.AC == 0) {
                  this.cmx = this.cmtoX;
               }
            }

            if (isNPCMenu == 0) {
               if (this.pos == -1) {
                  this.AJ();
               } else {
                  Menu var4 = this;
                  iCommand var5;
                  if (GameCanvas.AK[5]) {
                     GameCanvas.AH();
                     GameCanvas.AG();
                     if (!(var5 = (iCommand)this.menuItems.elementAt(this.AC)).isDonotCloseMenu) {
                        this.AG();
                     }

                     AA(var5);
                  } else if (GameCanvas.AK[12]) {
                     GameCanvas.AH();
                     GameCanvas.AG();
                     if (!(var5 = (iCommand)this.menuItems.elementAt(this.AC)).isDonotCloseMenu) {
                        this.AG();
                     }

                     AA(var5);
                  }

                  int var6;
                  if (GameCanvas.isPointerDown) {
                     if (!this.BI && GameCanvas.isPointer(this.menuX, this.menuY, this.wUni, this.menuH)) {
                        for(var2 = 0; var2 < var4.BH.length; ++var2) {
                           var4.BH[0] = GameCanvas.AZ;
                        }

                        var4.BG = GameCanvas.AZ;
                        var4.BI = true;
                        var4.BJ = var4.BL != 0;
                        var4.BL = 0;
                     } else if (this.BI) {
                        ++this.BF;
                        if (this.BF > this.BM && this.BG == GameCanvas.AZ && !this.BJ) {
                           this.BG = -1000;
                           this.AC = (this.cmtoX + GameCanvas.AZ - this.menuY) / this.menuW;
                        }

                        if ((var2 = GameCanvas.AZ - this.BH[0]) != 0 && this.AC != -1) {
                           this.AC = -1;
                        }

                        for(var6 = this.BH.length - 1; var6 > 0; --var6) {
                           var4.BH[var6] = var4.BH[var6 - 1];
                        }

                        var4.BH[0] = GameCanvas.AZ;
                        var4.cmtoX -= var2;
                        if (var4.cmtoX < 0) {
                           var4.cmtoX = 0;
                        }

                        if (var4.cmtoX > var4.cmxLim) {
                           var4.cmtoX = var4.cmxLim;
                        }

                        if (var4.cmx < 0 || var4.cmx > var4.cmxLim) {
                           var2 /= 2;
                        }

                        var4.cmx -= var2;
                     }
                  }

                  if (GameCanvas.AR && var4.BI) {
                     var2 = GameCanvas.AZ - var4.BH[0];
                     GameCanvas.AR = false;
                     if (CRes.abs(var2) < 20 && CRes.abs(GameCanvas.AZ - var4.BG) < 20 && !var4.BJ && GameCanvas.isPointerSelect) {
                        var4.BL = 0;
                        var4.cmtoX = var4.cmx;
                        var4.BG = -1000;
                        var4.AC = (var4.cmtoX + GameCanvas.AZ - var4.menuY) / var4.menuW;
                        var4.BF = 0;
                        var4.BK = 1;
                     } else if (var4.AC != -1 && var4.BF > var4.BM) {
                        var4.BF = 0;
                        var4.BK = 1;
                     } else if (var4.AC == -1 && !var4.BJ) {
                        var4.AM = 0;
                        if (var4.cmx < 0) {
                           var4.cmtoX = 0;
                        } else if (var4.cmx > var4.cmxLim) {
                           var4.cmtoX = var4.cmxLim;
                        } else {
                           byte var7;
                           if ((var6 = GameCanvas.AZ - var4.BH[0] + (var4.BH[0] - var4.BH[1]) + (var4.BH[1] - var4.BH[2])) > 10) {
                              var7 = 10;
                           } else if (var6 < -10) {
                              var7 = -10;
                           } else {
                              var7 = 0;
                           }

                           var4.BL = -var7 * 100;
                        }
                     }

                     var4.BI = false;
                     var4.BF = 0;
                  }

                  if (var4.AC != -1 && GameCanvas.isPointerDown && !GameCanvas.AQ) {
                     var4.AM = 5;
                  }

                  if (GameCanvas.AO && var4.BI) {
                     var4.BI = false;
                  }
               }

               if (GameCanvas.isPointerSelect && GameCanvas.menuCur == this && !GameCanvas.isPoint(this.menuX - 5, this.menuTemY - 5 - this.hPlus, this.wUni + 10, this.menuH + 10 + this.hPlus)) {
                  this.AG();
               }
            } else if (isNPCMenu == 2) {
               this.AJ();
               if (GameCanvas.isPointerSelect && !GameCanvas.isPoint(this.menuX - 5, this.menuY - 5, this.wUni + 10, this.menuH + 10)) {
                  this.BB = -1;
               }
            }

            super.handleKeyPress();
            this.AS();
         }
      }
   }

   private void AJ() {
      int var1;
      int var2;
      if (GameCanvas.isPointerDown) {
         if (!this.BI && GameCanvas.isPointer(this.menuX, this.menuY, this.wUni, this.menuH)) {
            for(var1 = 0; var1 < this.BH.length; ++var1) {
               this.BH[0] = GameCanvas.AY;
            }

            this.BG = GameCanvas.AY;
            this.BI = true;
            this.BJ = this.BL != 0;
            this.BL = 0;
         } else if (this.BI) {
            ++this.BF;
            if (this.BF > 5 && this.BG == GameCanvas.AY && !this.BJ) {
               this.BG = -1000;
               this.AC = (this.cmtoX + GameCanvas.AY - this.menuX) / this.menuW;
            }

            if ((var1 = GameCanvas.AY - this.BH[0]) != 0 && this.AC != -1) {
               this.AC = -1;
            }

            for(var2 = this.BH.length - 1; var2 > 0; --var2) {
               this.BH[var2] = this.BH[var2 - 1];
            }

            this.BH[0] = GameCanvas.AY;
            this.cmtoX -= var1;
            if (this.cmtoX < 0) {
               this.cmtoX = 0;
            }

            if (this.cmtoX > this.cmxLim) {
               this.cmtoX = this.cmxLim;
            }

            if (this.cmx < 0 || this.cmx > this.cmxLim) {
               var1 /= 2;
            }

            this.cmx -= var1;
         }
      }

      if (GameCanvas.AR && this.BI) {
         var1 = GameCanvas.AY - this.BH[0];
         GameCanvas.AR = false;
         if (CRes.abs(var1) < 20 && CRes.abs(GameCanvas.AY - this.BG) < 20 && !this.BJ) {
            this.BL = 0;
            this.cmtoX = this.cmx;
            this.BG = -1000;
            this.AC = (this.cmtoX + GameCanvas.AY - this.menuX) / this.menuW;
            this.BF = 0;
            this.BK = 1;
         } else if (this.AC != -1 && this.BF > 5) {
            this.BF = 0;
            this.BK = 1;
         } else if (this.AC == -1 && !this.BJ) {
            if (this.cmx < 0) {
               this.cmtoX = 0;
            } else if (this.cmx > this.cmxLim) {
               this.cmtoX = this.cmxLim;
            } else {
               byte var3;
               if ((var2 = GameCanvas.AY - this.BH[0] + (this.BH[0] - this.BH[1]) + (this.BH[1] - this.BH[2])) > 10) {
                  var3 = 10;
               } else if (var2 < -10) {
                  var3 = -10;
               } else {
                  var3 = 0;
               }

               this.BL = -var3 * 100;
            }
         }

         this.BI = false;
         this.BF = 0;
         GameCanvas.AR = false;
      }

      if (GameCanvas.AO && this.BI) {
         this.BI = false;
      }

   }

   public void AB(mGraphics var1) {
      GameCanvas.resetTrans(var1);
      if (AX) {
         if (isNPCMenu == 1) {
            mGraphics var8 = var1;
            Menu var7 = this;
            int var11 = this.menuX + 6;
            int var6 = this.menuY + 8;
            AvMain.paintRect(var1, this.menuX, this.menuTemY, this.wUni, this.menuH, (byte)2, 4);
            MainObject var9;
            if ((var9 = MainObject.get_Object(this.AZ, this.BA)) != null) {
               AvMain.AA(var1, var9.name, var11 + 10, var6, 0);
               if (this.AL != null) {
                  this.AL.AA(var1, this.BD);
               }

               GameCanvas.resetTrans(var1);

               for(var6 = 0; var6 < var7.menuItems.size(); ++var6) {
                  iCommand var10;
                  (var10 = (iCommand)var7.menuItems.elementAt(var6)).paint(var8, var10.xCmd, var10.yCmd);
               }
            }

         } else {
            if (isNPCMenu == 0) {
               this.AE(var1, this.menuX - this.BP, this.menuTemY - GameCanvas.hCommand, this.wUni + (this.BP << 1), this.menuH + GameCanvas.hCommand + this.BP, this.menuH + GameCanvas.hCommand + this.BP);
               var1.setColor(-805042);
               var1.fillRoundRectNew(this.menuX + this.BP, this.menuTemY - GameCanvas.hCommand + this.BP / 2 + 3, this.wUni - (this.BP << 1), 16, 4, 4);
               AvMain.FontBorderColor(var1, this.nameMenu, this.menuX + this.wUni / 2, this.menuTemY - this.hPlus + GameCanvas.hCommand / 4 + 1 + 3, 2, (int)6, (int)5);
               if (!AW) {
                  return;
               }

               if (this.pos != -1) {
                  var1.AD(this.menuX + 3, this.menuY + 3, this.wUni - 6, this.menuH - 6);
                  mGraphics.AC();
                  int var10000 = this.menuX;
                  var10000 = this.menuY;
                  var10000 = this.wUni;
                  var10000 = this.menuH;
                  mGraphics.AD();
                  var1.translate(0, -this.cmx);
                  var1.setColor(AvMain.II[4]);
                  int var2;
                  if (this.pos == 2 || this.pos == 4 || this.pos == 0) {
                     for(var2 = 0; var2 < this.menuItems.size() - 1; ++var2) {
                        var1.setColor(AvMain.II[4]);
                        var1.fillRect(this.menuX + 8, this.menuY + 3 + this.menuW + var2 * this.menuW - 1, this.wUni - 16, 2);
                        var1.fillRect(this.menuX + 8 + 1, this.menuY + 3 + this.menuW + var2 * this.menuW - 2, this.wUni - 16 - 2, 4);
                     }
                  }

                  if ((var2 = this.cmx / this.menuW - 1) < 0) {
                     var2 = 0;
                  }

                  int var3;
                  if ((var3 = var2 + this.sizeMenu + 2) > this.menuItems.size() && (var2 = (var3 = this.menuItems.size()) - this.sizeMenu - 2) < 0) {
                     var2 = 0;
                  }

                  if (this.AC >= 0) {
                     this.paintSelect(var1, this.menuX + 10, this.menuY + 7 + this.AC * this.menuW, this.wUni - 20, this.menuW - 8);
                  }

                  for(var2 = var2; var2 < var3; ++var2) {
                     iCommand var4 = (iCommand)this.menuItems.elementAt(var2);
                     boolean var5 = false;
                     if (this.AC == var2) {
                        var5 = true;
                     }

                     if (this.pos == 2) {
                        var4.AA(var1, this.menuX + this.wUni / 2, this.menuY + 6 + this.menuW / 4 + var2 * this.menuW, 2, var5);
                     } else if (this.pos != 0 && this.pos != 3) {
                        if (this.pos == 1) {
                           var4.AA(var1, this.menuX + this.wUni - 6, this.menuY + 6 + this.menuW / 4 + var2 * this.menuW, 1, var5);
                        } else if (this.pos == 4) {
                           var4.AA(var1, this.menuX + 12, this.menuY + 6 + this.menuW / 4 + var2 * this.menuW, 0, var5);
                        }
                     } else {
                        var4.AA(var1, this.menuX + 12, this.menuY + 6 + this.menuW / 4 + var2 * this.menuW, 0, var5);
                     }
                  }

                  mGraphics.AE();
                  mGraphics.restoreCanvas();
                  GameCanvas.resetTrans(var1);
                  this.AD(var1);
               }
            }

         }
      }
   }

   public final void AG() {
      if (this == GameCanvas.menuCur) {
         this.isShowMenu = false;
      }

      GameCanvas.isPointerSelect = false;
      GameCanvas.AR = false;
      GameCanvas.AS = true;
   }

   public void AH() {
      if (this.BB > 0) {
         ++this.BB;
         if (this.BC < this.wUni) {
            this.BC += this.menuH;
            if (this.BC >= this.wUni) {
               this.BC = this.wUni;
               this.BB = 0;
            }
         }
      } else if (this.BB < 0) {
         --this.BB;
         if (this.BC > 0) {
            this.BC -= this.menuH;
            if (this.BC <= 0) {
               this.BC = 0;
               this.BB = 0;
               this.AG();
            }
         }
      }

      if (AX) {
         if (this.BL != 0 && !this.BI) {
            this.cmtoX += this.BL / 100;
            if (this.cmtoX < 0) {
               this.cmtoX = 0;
            } else if (this.cmtoX > this.cmxLim) {
               this.cmtoX = this.cmxLim;
            } else {
               this.cmx = this.cmtoX;
            }

            this.BL = this.BL * 9 / 10;
            if (this.BL < 100 && this.BL > -100) {
               this.BL = 0;
            }
         }

         if (this.cmx != this.cmtoX && !this.BI) {
            this.BN = this.cmtoX - this.cmx << 2;
            this.BO += this.BN;
            this.cmx += this.BO >> 4;
            this.BO &= 15;
         }

         int var1;
         if (isNPCMenu == 1) {
            if (this.AL != null) {
               this.AL.AB();
            }

            for(var1 = 0; var1 < this.menuItems.size(); ++var1) {
               ((iCommand)this.menuItems.elementAt(var1)).AE();
            }
         } else if (isNPCMenu == 2 && !GameCanvas.AQ && this.BB == 0) {
            for(var1 = 0; var1 < this.menuItems.size(); ++var1) {
               ((iCommand)this.menuItems.elementAt(var1)).AC(this.cmx, 0);
            }
         }

         if (this.menuTemY > this.menuY) {
            if ((var1 = this.menuTemY - this.menuY >> 1) <= 0) {
               var1 = 1;
            }

            this.menuTemY -= var1;
         }

         if (this.xc != 0) {
            this.xc >>= 1;
            if (this.xc < 0) {
               this.xc = 0;
            }
         }

         if (this.BK > 0) {
            --this.BK;
            if (this.BK == 0) {
               if (this.AC >= 0 && this.AC < this.menuItems.size()) {
                  iCommand var3;
                  if (!(var3 = (iCommand)this.menuItems.elementAt(this.AC)).isDonotCloseMenu) {
                     if (this == GameCanvas.menuCur) {
                        this.isShowMenu = false;
                     } else {
                        this.AG();
                     }
                  }

                  AA(var3);
                  this.AM = 5;
                  GameCanvas.AH();
                  GameCanvas.AG();
                  GameCanvas.AS = true;
                  GameCanvas.isPointerSelect = false;
               } else if (this == GameCanvas.menuCur) {
                  this.isShowMenu = false;
               } else {
                  this.AG();
               }
            }
         }

         super.updatePointer();
      }
   }

   private static void AA(iCommand var0) {
      if (var0 != null) {
         if (var0.action != null) {
            IAction var10000 = var0.action;
         } else if (var0.Pointer != null) {
            var0.Pointer.commandPointer(var0.indexMenu, var0.AF);
         } else {
            MainScreen var1 = GameCanvas.currentScreen;
            int var2 = var0.indexMenu;
            var2 = var0.AF;
         }

         GameCanvas.isPointerSelect = false;
         float var3 = mSound.volumeSound;
         mSound.playSound();
      }

   }
}
