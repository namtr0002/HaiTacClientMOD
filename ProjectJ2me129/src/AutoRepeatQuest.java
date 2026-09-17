import java.util.Timer;

public final class AutoRepeatQuest extends MainObject {
   private static int step = 0;
   private static int AG = 500;
   private static int AH = 237;
   private static boolean isCheckFinish = false;
   public static boolean isStart = false;
   private static boolean AJ = false;
   public static boolean AB = false;
   private static boolean AK = false;
   public static boolean AC = false;
   private boolean AL = false;
   public static boolean AD = true;
   private String AM = "";
   public static MainObject AE = null;

   private void CD() {
      MainObject var1;
      if ((var1 = CL()) != null) {
         int var2 = MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, var1.x, var1.y);
         GameScreen.player.toX = var1.x;
         GameScreen.player.toY = var1.y;
         if (MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, GameScreen.player.toX, GameScreen.player.toY) < GameScreen.player.CN << 1) {
            GameScreen.player.x = var1.x;
            GameScreen.player.y = var1.y;
         } else {
            GameScreen.player.NR = true;
         }

         this.AL = var2 < 10;
         GameScreen.objFocus = var1;
      }

   }

   private static boolean AB(int var0, int var1) {
      if (AJ) {
         return true;
      } else {
         GameScreen.player.toX = var0;
         GameScreen.player.toY = var1;
         int var2 = MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, var0, var1);
         if (MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, GameScreen.player.toX, GameScreen.player.toY) < GameScreen.player.CN << 1) {
            GameScreen.player.x = var0;
            GameScreen.player.y = var1;
         } else {
            GameScreen.player.NR = true;
         }

         boolean var3;
         return var3 = var2 == 0;
      }
   }

   private void AA(MainQuest var1) {
      byte var2 = var1.AB;
      mVector var3 = new mVector();
      switch(var2) {
      case 0:
         iCommand var8 = new iCommand(var1.AH + (var1.AA == 0 ? var1.AA() : ""), 1, var1.AE, this);
         var3.addElement(var8);
         GameCanvas.menu.startAt(var3, 2, T.BU);
         if (!isStart) {
            int var9 = (int)(1000.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
            int var11 = (int)(2000.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
            int var4 = (int)(3000.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
            int var5 = (int)(5000.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
            int var6 = (int)(10000.0D / (1.0D + (double)GameCanvas.percentUPCV / 100.0D));
            Timer var7;
            (var7 = new Timer()).schedule(new Class_IH(this, var7), (long)var9);
            Timer var10;
            (var10 = new Timer()).schedule(new Class_II(this, var10), (long)var11);
            (var10 = new Timer()).schedule(new Class_IJ(this, var10), (long)var4);
            (var10 = new Timer()).schedule(new Class_IK(this, var10), (long)var5);
            (var10 = new Timer()).schedule(new Class_IL(this, var10), (long)var6);
         }
         break;
      case 1:
         if (AB(AG, AH)) {
            isCheckFinish = true;
            return;
         }
      case 2:
      }

   }

   private static MainQuest CE() {
      MainObject var0;
      if ((var0 = CL()) == null) {
         return null;
      } else {
         try {
            MainQuest var1 = null;

            for(int var2 = 0; var2 < Player.QI.size(); ++var2) {
               MainQuest var3;
               if ((var3 = (MainQuest)Player.QI.elementAt(var2)).AC == var0.ID || var3.AD == var0.ID) {
                  var1 = var3;
                  break;
               }
            }

            return var1;
         } catch (Exception var4) {
            return null;
         }
      }
   }

   private static MainQuest CF() {
      MainObject var0;
      if ((var0 = CL()) == null) {
         return null;
      } else {
         try {
            MainQuest var1 = null;

            for(int var2 = 0; var2 < Player.QI.size(); ++var2) {
               MainQuest var3;
               if (((var3 = (MainQuest)Player.QI.elementAt(var2)).AC == var0.ID || var3.AD == var0.ID) && var3.AB == 2) {
                  var1 = var3;
                  break;
               }
            }

            return var1;
         } catch (Exception var4) {
            return null;
         }
      }
   }

   private static MainQuest CG() {
      MainObject var0;
      if ((var0 = CL()) == null) {
         return null;
      } else {
         try {
            MainQuest var1 = null;

            for(int var2 = 0; var2 < Player.QI.size(); ++var2) {
               MainQuest var3;
               if (((var3 = (MainQuest)Player.QI.elementAt(var2)).AC == var0.ID || var3.AD == var0.ID) && var3.AB == 1) {
                  var1 = var3;
                  break;
               }
            }

            return var1;
         } catch (Exception var4) {
            return null;
         }
      }
   }

   private MainObject AB(MainQuest var1) {
      try {
         MainObject var2 = null;
         if (AD) {
            AD = false;
            this.AM = AD(var1);
            if (this.AM != null && this.AM != "") {
               for(int var6 = 0; var6 < GameScreen.vecPlayers.size(); ++var6) {
                  MainObject var3;
                  if ((var3 = (MainObject)GameScreen.vecPlayers.elementAt(var6)).typeObject == 1) {
                     int var4 = MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, var3.x, var3.y);
                     if (this.AM != "" && var3.name.equalsIgnoreCase(this.AM) && !var3.isDie && var3.Hp > 0 && var4 < 140) {
                        var2 = var3;
                        AC = false;
                        break;
                     }
                  }
               }
            }
         }

         return var2;
      } catch (Exception var5) {
         return null;
      }
   }

   private MainObject AC(MainQuest var1) {
      try {
         MainObject var2 = null;
         if (AD) {
            AD = false;
            this.AM = AD(var1);
            if (this.AM != null && this.AM != "") {
               for(int var5 = 0; var5 < GameScreen.vecPlayers.size(); ++var5) {
                  MainObject var3;
                  if ((var3 = (MainObject)GameScreen.vecPlayers.elementAt(var5)).typeObject == 1 && this.AM != "" && var3.name.equalsIgnoreCase(this.AM) && !var3.isDie && var3.Hp > 0) {
                     var2 = var3;
                     AC = true;
                     AJ = false;
                     break;
                  }
               }
            }
         }

         return var2;
      } catch (Exception var4) {
         return null;
      }
   }

   private static String AD(MainQuest var0) {
      try {
         String var1 = "";
         int var2;
         if ((var2 = var0.AM.size()) > 0) {
            for(int var3 = 0; var3 < var2; ++var3) {
               Class_CP var4;
               if ((var4 = (Class_CP)var0.AM.elementAt(var3)).AC > 0 && var4.AD < var4.AC && var0.AA != 0) {
                  var1 = var4.AE;
                  break;
               }
            }
         }

         return var1;
      } catch (Exception var5) {
         return "";
      }
   }

   private static boolean CH() {
      boolean var0 = false;

      try {
         MainDialog var1;
         MsgShowGift var3;
         if ((var1 = GameCanvas.currentDialog) != null && (var3 = (MsgShowGift)var1) != null && var3.type == 0 && var3.nameDialog.indexOf(T.XO) >= 0) {
            var0 = true;
         }

         return var0;
      } catch (Exception var2) {
         return false;
      }
   }

   private static boolean CI() {
      boolean var0 = false;

      try {
         int var1 = Player.vecInventory.size();
         if (Player.maxInventory - var1 < 2) {
            var0 = true;
            AB();
            GameCanvas.end_Dialog();
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.XH);
         }

         return var0;
      } catch (Exception var2) {
         return false;
      }
   }

   public static void AB() {
      Player.AutoFireCur = 0;
      isCheckFinish = false;
      isStart = false;
      GameScreen.isOnRepeatQuest = false;
      GameCanvas.clearAll();
   }

   private static boolean CJ() {
      try {
         return Player.Ticket >= 3;
      } catch (Exception var0) {
         return false;
      }
   }

   private static int CK() {
      int var0 = -1;

      try {
         for(int var1 = 0; var1 < GameScreen.vecPlayers.size(); ++var1) {
            MainObject var2;
            if ((var2 = (MainObject)GameScreen.vecPlayers.elementAt(var1)).name != null && var2.name.equalsIgnoreCase(T.nhiemvu) && var2.typeObject == 2) {
               var0 = var1;
               break;
            }
         }

         return var0;
      } catch (Exception var3) {
         return -1;
      }
   }

   private static MainObject CL() {
      try {
         int var0 = CK();
         MainObject var1 = null;
         if (var0 >= 0) {
            var1 = (MainObject)GameScreen.vecPlayers.elementAt(var0);
         }

         return var1;
      } catch (Exception var2) {
         return null;
      }
   }

   public static void AF() {
      MainQuest var0;
      if ((var0 = CE()) != null) {
         MainQuest var1;
         if ((var1 = MainQuest.AA(var0.AE)) != null) {
            var1.AB((short)var0.AC);
         }

         step = 1;
      }

   }

   public static void AJ() {
      MainQuest var0;
      if ((var0 = CE()) != null && step == 1) {
         GameCanvas.menuCur.AG();
         GlobalService.getInstance().AC((byte)1, (short)var0.AE);
         step = 2;
      }

   }

   public static void AK() {
      if (step == 2) {
         byte var0 = 0;
         MainDialog var1;
         if ((var1 = GameCanvas.currentDialog) != null) {
            if (var1.strinfo[0].indexOf(T.XP) >= 0) {
               var0 = 1;
            } else if (var1.strinfo[0].indexOf(T.XQ) >= 0) {
               var0 = 2;
            }
         }

         switch(var0) {
         case 0:
            ((iCommand)((MsgDialog)GameCanvas.currentDialog).cmdList.elementAt(0)).AD();
            step = 3;
            return;
         case 1:
            AB();
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.XM);
            step = -1;
            return;
         case 2:
            AB();
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.XN);
            step = -1;
         }
      }

   }

   public static void AL() {
      if (step == 3 && CG() != null) {
         AB(AG, AH);
         step = 4;
      }

   }

   public static void CA() {
      if (step == 4) {
         GameCanvas.end_Dialog();
         isCheckFinish = true;
         step = 5;
      }

   }

   public final void StartAutoRepeatQuest() {
      MainQuest var2;
      if (GameScreen.isOnRepeatQuest && isStart && !isCheckFinish) {
         GameScreen.isOnRepeatQuest = true;
         if (!Interface_Game.isAutoFireInterface) {
            GameScreen.interfaceGame.selectPointer(6);
         }

         AD = true;
         AE = null;
         if (GameScreen.player.Hp > 0 && GameScreen.player.Action != 4) {
            Player.AutoFireCur = 0;
            switch((var2 = CE()).AB) {
            case 0:
               if (!CJ()) {
                  AB();
                  GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.XG);
               } else {
                  this.CD();
                  if (this.AL) {
                     isStart = false;
                     this.AL = false;
                     GameCanvas.clearAll();
                     this.AA(var2);
                  }
               }
               break;
            case 1:
               this.AA(var2);
               break;
            case 2:
               isCheckFinish = true;
            }
         } else {
            Player.AutoFireCur = 0;
         }
      }

      if (GameScreen.isOnRepeatQuest && isCheckFinish) {
         GameScreen.isOnRepeatQuest = true;
         if (!Interface_Game.isAutoFireInterface) {
            GameScreen.interfaceGame.selectPointer(6);
         }

         if (GameScreen.player.Hp <= 0 || GameScreen.player.Action == 4) {
            Player.AutoFireCur = 0;
            return;
         }

         if ((var2 = CG()) != null) {
            if (AD) {
               AE = this.AB(var2);
            }

            if (AE == null) {
               AD = true;
               AE = this.AC(var2);
            }

            if (AE == null) {
               Player.AutoFireCur = 0;
               AB = true;
               if (!AK) {
                  if (AB(GameCanvas.loadmap.limitW - 140, AH)) {
                     AK = true;
                  }
               } else if (AB(GameCanvas.loadmap.limitW / 2, AH)) {
                  AK = false;
               }
            } else {
               AB = false;
               if (AE.isDie || AE.Hp <= 0) {
                  AE = null;
                  GameScreen.objFocus = null;
                  AD = true;
                  return;
               }

               if (GameScreen.objFocus != null && !GameScreen.objFocus.isDie && GameScreen.objFocus.Hp > 0) {
                  byte var10000 = GameScreen.objFocus.typeObject;
               }

               if ((GameScreen.objFocus = AE) != null) {
                  Player.AutoFireCur = 0;
                  if (!AC) {
                     GameScreen.player.beginPlayerFire((int)2);
                  }

                  if (AC) {
                     if (AB(GameScreen.objFocus.x - 100, GameScreen.objFocus.y)) {
                        AJ = true;
                     }

                     if (AJ) {
                        GameScreen.player.beginPlayerFire((int)2);
                     }
                  }
               }
            }
         }

         MainQuest var3;
         if ((var3 = CF()) != null) {
            Player.AutoFireCur = 0;
            this.CD();
            if (this.AL) {
               if (CI()) {
                  return;
               }

               GlobalService.getInstance().AC((byte)4, (short)var3.AE);
               Player.QU = (short)var3.AC;
            }
         }

         if (CH()) {
            isCheckFinish = false;
            isStart = true;
         }
      }

   }

   public static boolean CC() {
      if (CE() == null) {
         AB();
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.XI);
         return false;
      } else {
         return true;
      }
   }
}
