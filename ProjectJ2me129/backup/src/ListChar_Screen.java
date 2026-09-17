public final class ListChar_Screen extends MainScreen {
   private int AC;
   public static mVector AA = new mVector("ListChar_Screen.vecListChar");
   private int[][] AD;
   private iCommand AE;
   private iCommand AF;
   private iCommand AG;
   private iCommand AH;
   public static byte IndexCharSelected = -1;
   private static short AI = 0;
   private static boolean AJ = false;
   private int AK = 0;
   private mVector AL = new mVector("ListChar_Screen.vecDelButton");
   private byte AM = 0;
   private int[] AN = new int[]{8, 8, 8, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10};
   private Boat AO;
   private static int AP = 0;
   private static boolean AQ = true;
   private int AR;
   private int AS;
   private int AT;
   private int AU;

   public ListChar_Screen() {
      this.AO = new Boat((short)0, MotherCanvas.w / 7 << 2, MotherCanvas.h - 105, 0, (byte)2);
      this.AR = 40;
      this.AS = 80;
      this.AT = 4;
   }

   public static ListChar_Screen AF() {
      if (GameCanvas.CG == null) {
         GameCanvas.CG = new ListChar_Screen();
      }

      return GameCanvas.CG;
   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public final void commandPointer(int var1, int var2) {
      SaveRms var10000;
      Class_ET var3;
      switch(var1) {
      case 0:
         if (AJ) {
            AJ = false;
            this.AG();
         } else if (this.AC > AA.size() - 1) {
            CreateChar_Screen.AB().Show((MainScreen)this);
         } else if ((var3 = (Class_ET)AA.elementAt(this.AC)).MJ == 1) {
            GlobalService.getInstance().Del_Char((byte)1, (short)var3.ID);
         } else if (this.AM == 0) {
            IndexCharSelected = (byte)this.AC;
            AI = var3.ID;
            var3.Action = 2;
            var3.f = 0;
            this.AM = 2;
            this.AG();
         } else if (this.AM == 1) {
            if (var3.ID != AI) {
               var3.Action = 2;
               var3.f = 0;
               this.AM = 2;
               var3.BC = T.HM;
            }
         } else if (this.AM == 2) {
            GlobalService.getInstance().Select_Char((short)AI, (byte)1, (short)var3.ID);
            GlobalService.getInstance().get_DATA((byte)3);
            GameCanvas.AA(T.BP, false);
            var10000 = GameCanvas.saveRms;
            SaveRms.AA(GameCanvas.loginScr.AC.getText(), GameCanvas.loginScr.AD.getText());
         }
         break;
      case 1:
         GameCanvas.loginScr.Show();
         break;
      case 2:
         if (AJ) {
            if (!GameCanvas.isTouch) {
               this.commandPointer(4, this.AC);
            }
         } else {
            GameCanvas.Start_Normal_DiaLog(T.FP, new iCommand("Ok", 3, this), true);
         }
         break;
      case 3:
         AJ = true;
         this.AG();
         GameCanvas.end_Dialog();
         break;
      case 4:
         this.AC = var2;
         if (this.AC < 0 || this.AC >= AA.size()) {
            return;
         }

         var3 = (Class_ET)AA.elementAt(this.AC);
         GlobalService.getInstance().Del_Char((byte)0, (short)var3.ID);
         GameCanvas.AA(T.BP, true);
         break;
      case 5:
         GlobalService.getInstance().Select_Char((short)AI, (byte)0, (short)0);
         GlobalService.getInstance().get_DATA((byte)3);
         var10000 = GameCanvas.saveRms;
         SaveRms.AA(GameCanvas.loginScr.AC.getText(), GameCanvas.loginScr.AD.getText());
      }

      super.commandPointer(var1, var2);
   }

   public final void Show() {
      super.center = null;
      super.DA = null;
      super.DB = null;
      super.DF = null;
      this.AM = 0;
      AJ = false;
      this.AK = 0;
      this.AD = mSystem.new_M_Int(3, 2);
      this.AD[0][0] = MotherCanvas.hw;
      this.AD[0][1] = MotherCanvas.h - 50;
      this.AD[1][0] = MotherCanvas.hw - 60;
      this.AD[1][1] = MotherCanvas.h - 40;
      this.AD[2][0] = MotherCanvas.hw + 60;
      this.AD[2][1] = MotherCanvas.h - 40;
      this.AE = new iCommand(T.AS, 0, this);
      this.AF = new iCommand(T.AI, 1, this);
      this.AF = AvMain.AA(this.AF, 1);
      this.AH = new iCommand(T.GD, 5, this);
      this.AH = AvMain.AA(this.AH, 0);
      if (!GameCanvas.isTouch) {
         this.AH = AvMain.AA(this.AH, 2);
      }

      this.AG = new iCommand(T.FV, 2, this);
      this.AG = AvMain.AA(this.AG, 2);
      this.AG();

      for(int var1 = 0; var1 < AA.size(); ++var1) {
         Class_ET var2;
         (var2 = (Class_ET)AA.elementAt(var1)).x = this.AD[var1][0];
         var2.y = this.AD[var1][1];
      }

      GameScreen.player = null;
      if (GameCanvas.mapBack == null) {
         GameCanvas.mapBack = new MapBackGround();
      }

      if (GameCanvas.currentScreen != GameCanvas.loginScr && GameCanvas.currentScreen != CreateChar_Screen.AB()) {
         GameCanvas.mapBack.AC();
      }

      if (GameCanvas.currentScreen == GameCanvas.loginScr) {
         this.AC = 0;
      }

      this.AO.AH = true;
      if (LoadMapScreen.isMapSky != 0) {
         LoadMapScreen.isMapSky = 0;
         LoadImageStatic.loadImageEffBoat();
      }

      super.Show();
      GameCanvas.clearAll();
   }

   public final void paint(mGraphics var1) {
      try {
         if (GameCanvas.mapBack != null) {
            GameCanvas.mapBack.AC(var1);
            GameCanvas.mapBack.AE(var1);
         }
      } catch (Exception var6) {
      }

      LoginScreen.paintLogo(var1, MotherCanvas.hw);

      int var2;
      for(var2 = 0; var2 < this.AL.size(); ++var2) {
         iCommand var3;
         (var3 = (iCommand)this.AL.elementAt(var2)).paint(var1, var3.xCmd, var3.yCmd);
      }

      if (this.AO.AH) {
         this.AO.AB(var1);
         this.AO.AE(var1);
         this.AO.AA(var1, GameCanvas.AF);
      }

      Class_ET var7;
      for(var2 = 0; var2 < 3; ++var2) {
         int var4;
         if (var2 < AA.size()) {
            (var7 = (Class_ET)AA.elementAt(var2)).AC(var1, var7.x);
            var4 = var7.y - var7.dy;
            if (var7.boatSea != null) {
               var7.boatSea.AB(var1);
               var7.boatSea.AE(var1);
               var4 = var7.y - this.AU / 10;
            }

            var7.AI(var1, var7.x, var4, var7.type_left_right, var7.frame);
            if (var7.boatSea != null) {
               var7.boatSea.AA(var1, GameCanvas.AF);
            }
         } else if (!AJ) {
            var1.drawRegion((mImage)MainObject.imgShadow, this.AD[var2][0] + 1, this.AD[var2][1], 3);
            if (var2 < 2 || AA.size() > 1) {
               AvMain.paintRect(var1, this.AD[var2][0] - 18, this.AD[var2][1] - 32, 36, 16, (byte)1, 4);
               mFont.tahoma_7b_white.drawString(var1, T.AR, this.AD[var2][0], this.AD[var2][1] - 30, 2);
            }
         }

         if (var2 == this.AC && GameCanvas.isKeyPressed()) {
            if (AJ) {
               var1.drawRegion((mImage)AvMain.imgIconDel, this.AD[var2][0], this.AD[var2][1] - 52, 3);
            } else {
               int var10001 = this.AD[var2][0];
               int var5 = this.AD[var2][1] - 52;
               var4 = var10001;
               if (AvMain.fraIconfocus.nFrame == 1) {
                  var1.drawRegion((mImage)AvMain.fraIconfocus.imgFrame, var4, var5 - GameCanvas.gameTick % 5, 3);
               } else {
                  if (AQ) {
                     if (GameCanvas.gameTick % 3 == 0) {
                        ++AP;
                     }

                     if (AP >= AvMain.fraIconfocus.nFrame - 1) {
                        AQ = false;
                     }
                  } else {
                     if (GameCanvas.gameTick % 3 == 0) {
                        --AP;
                     }

                     if (AP <= 0) {
                        AQ = true;
                     }
                  }

                  AvMain.fraIconfocus.drawFrame(AP, var4, var5 - 3, 0, 3, var1);
               }
            }
         }
      }

      if (GameCanvas.mapBack != null) {
         GameCanvas.mapBack.AD(var1);
      }

      for(var2 = 0; var2 < AA.size(); ++var2) {
         if (var2 < AA.size()) {
            var7 = (Class_ET)AA.elementAt(var2);
            byte var8 = 0;
            if (var7.MJ == 1) {
               var8 = 6;
            } else if (var7.MJ == 2) {
               var8 = 3;
            }

            var7.paintName(var1, (byte)var8, -1);
            var7.AB(var1, var8);
            if (var7.LS != null) {
               var7.LS.paint(var1);
            }
         }
      }

      if (super.DB != null) {
         super.DB.paint(var1, super.DB.xCmd, super.DB.yCmd);
      }

   }

   public final void update() {
      LoginScreen.updateYPaintLogo(LoginScreen.hLogo);
      if (AA.size() > 1) {
         CRes.quickSort(AA);
      }

      this.AO.AA(MotherCanvas.w / 7 << 2, MotherCanvas.h - 105, this.AU / 10, (byte)this.AO.Dir);
      if (CRes.random(40) == 0) {
         if (CRes.random(2) == 0) {
            this.AT = 4;
         } else {
            this.AT = -4;
         }
      }

      if (this.AU > 0 && this.AT > 0) {
         this.AT = -4;
      } else if (this.AU < -50 && this.AT < 0) {
         this.AT = 4;
      }

      this.AU += this.AT;
      if (GameCanvas.mapBack != null) {
         GameCanvas.mapBack.AD();
      }

      int var1;
      Class_ET var2;
      if (this.AK % 100 == 0 && (var1 = this.AK / 100 % 3) < AA.size()) {
         if ((var2 = (Class_ET)AA.elementAt(var1)).MJ == 1) {
            var2.BC = T.FT + " " + CRes.AH((int)var2.timeDie);
         } else if (var2.MJ == 2) {
            var2.BC = T.FU + " " + CRes.AH((int)var2.timeDie);
         } else {
            this.AK += 90;
         }
      }

      for(var1 = 0; var1 < AA.size(); ++var1) {
         (var2 = (Class_ET)AA.elementAt(var1)).ySort = var2.y;
         var2.AF((int)14);
         var2.BL();
         ++var2.f;
         if (var2.Action == 0) {
            if (var2.f > var2.feStand.length - 1) {
               var2.f = 0;
            }

            var2.frame = var2.feStand[var2.f];
         } else if (var2.Action == 2) {
            if (var2.f > this.AN.length - 1) {
               var2.f = 0;
               var2.Action = 1;
               var2.toX = this.AO.x - 10;
               var2.toY = this.AO.y + 24;
               if (this.AM == 2) {
                  this.AE.AD();
               }
            } else {
               var2.frame = this.AN[var2.f];
            }
         } else if (var2.Action == 1) {
            if (var2.f > var2.PP.length - 1) {
               var2.f = 0;
            }

            var2.frame = var2.PP[var2.f];
            var2.x += var2.vx;
            var2.y += var2.vy;
            if (MainObject.getDistance(var2.x, var2.y, var2.toX, var2.toY) < 12) {
               var2.vx = 0;
               var2.vy = 0;
               var2.Action = 5;
               if (this.AM == 1) {
                  var2.vx = (this.AO.x - 10 - var2.x) / 6;
                  var2.vy = (this.AO.y - var2.y) / 6;
               } else {
                  var2.vx = (this.AO.x - 10 - var2.x) / 6;
                  var2.vy = (this.AO.y - 2 - var2.y) / 6;
               }

               var2.f = 0;
               var2.type_left_right = 2;
               var2.Dir = 2;
            } else {
               var2.BB();
            }
         } else if (var2.Action == 5) {
            var2.x += var2.vx;
            var2.y += var2.vy;
            var2.frame = 60;
            if (var2.f < 2) {
               var2.dy = (var2.f + 1) * 5;
            } else if (var2.f != 2 && var2.f != 3) {
               if (var2.dy > 3) {
                  var2.dy = 12 - (var2.f - 3) * 5;
               }
            } else {
               var2.dy = 12;
            }

            if (var2.f == 5) {
               var2.dy = 0;
               var2.Action = 0;
               var2.vx = 0;
               var2.vy = 0;
               if (this.AM == 1) {
                  var2.x = this.AO.x - 10;
                  var2.y = this.AO.y;
                  this.AO.AH = false;
                  var2.boatSea = this.AO;
                  if (AA.size() > 1) {
                     var2.BC = T.HL;
                  }
               } else {
                  var2.x = this.AO.x - 30;
                  var2.y = this.AO.y - 2;
               }

               if (AA.size() == 1) {
                  GlobalService.getInstance().Select_Char((short)AI, (byte)0, (short)var2.ID);
                  GlobalService.getInstance().get_DATA((byte)3);
                  GameCanvas.AA(T.BP, false);
               }
               break;
            }
         }
      }

      ++this.AK;
      if (this.AM == 1 && AA.size() > 1 && (this.AC < 0 || this.AC >= AA.size() || this.AC == IndexCharSelected)) {
         ++this.AC;
      }

   }

   public final void handleKeyPress() {
      if (this.AM != 2) {
         if (GameCanvas.isKeyPressed(0)) {
            if (this.AM == 1 && IndexCharSelected == 1) {
               if (this.AC == 2) {
                  this.AC = 0;
               }
            } else if (this.AC == 0 || AA.size() > 1) {
               if (this.AC == 0) {
                  this.AC = 1;
               } else if (this.AC == 1) {
                  this.AC = 2;
               } else if (this.AC == 2) {
                  this.AC = 0;
               }
            }

            GameCanvas.clearKeyPressed(0);
         } else if (GameCanvas.isKeyPressed(2)) {
            if (this.AM == 1 && IndexCharSelected == 0) {
               if (this.AC == 1) {
                  this.AC = 2;
               }
            } else if (this.AC == 1 || AA.size() > 1) {
               if (this.AC == 0) {
                  this.AC = 2;
               } else if (this.AC == 1) {
                  this.AC = 0;
               } else if (this.AC == 2) {
                  this.AC = 1;
               }
            }

            GameCanvas.clearKeyPressed(2);
         }

         if (AJ) {
            this.AC = AvMain.AA(this.AC, AA.size() - 1, true);
         } else {
            this.AC = AvMain.AA(this.AC, 2, true);
         }

         super.handleKeyPress();
      }
   }

   public final void updatePointer() {
      if (this.AM != 2) {
         int var1;
         if (GameCanvas.isPointerSelect) {
            for(var1 = 0; var1 < this.AD.length; ++var1) {
               if (GameCanvas.isPoint(this.AD[var1][0] - this.AR / 2, this.AD[var1][1] - this.AS + 10, this.AR, this.AS)) {
                  if (AJ) {
                     if (var1 < AA.size()) {
                        this.AC = var1;
                        this.AG.AD();
                     }
                  } else if (var1 <= AA.size()) {
                     this.AC = var1;
                     this.AE.AD();
                  }

                  GameCanvas.isPointerSelect = false;
                  break;
               }
            }
         }

         for(var1 = 0; var1 < this.AL.size(); ++var1) {
            ((iCommand)this.AL.elementAt(var1)).AE();
         }

         super.updatePointer();
      }
   }

   private void AG() {
      this.AL.removeAllElements();
      if (AJ) {
         if (GameCanvas.isKeyPressed()) {
            this.AG = new iCommand(T.FV, 2, this);
            super.center = this.AG;
         }

         this.AE = new iCommand(T.AG, 0, this);
         this.AE = AvMain.AA(this.AE, 2);
         super.DB = this.AE;
         super.DA = this.AF;
         if (GameCanvas.isTouch) {
            for(int var1 = 0; var1 < AA.size(); ++var1) {
               Class_ET var2 = (Class_ET)AA.elementAt(var1);
               iCommand var3;
               (var3 = new iCommand(T.del, 4, var1, this)).setPos(var2.x, var2.y - 92, (FrameImage)null, var3.caption);
               var3.AH = 3;
               this.AL.addElement(var3);
            }

            return;
         }
      } else if (this.AM == 0) {
         this.AE = new iCommand(T.AS, 0, this);
         if (GameCanvas.isKeyPressed()) {
            super.center = this.AE;
            return;
         }
      } else {
         if (this.AM == 1) {
            if (AA.size() > 1) {
               this.AE = new iCommand(T.GE, 0, this);
               if (GameCanvas.isKeyPressed()) {
                  super.center = this.AE;
               }

               super.DA = null;
               super.DB = this.AH;
               return;
            }

            super.center = null;
            super.DA = null;
            super.DB = null;
            return;
         }

         if (this.AM == 2) {
            super.center = null;
            super.DA = null;
            super.DB = null;
         }
      }

   }
}
