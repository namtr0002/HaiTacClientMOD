public final class AutoSuperBoss extends MainObject {
   public static boolean AA = false;
   private static boolean AB = false;
   private static String AC = "";
   private boolean AD = false;
   private static boolean AE = false;

   public static void AB() {
      Player.AutoFireCur = 0;
      AC = "";
      AA = false;
      AB = false;
      AE = false;
      GameScreen.isOnSuperBoss = false;
      GameCanvas.clearAll();
   }

   private static MainObject AJ() {
      MainObject var0 = null;

      for(int var1 = 0; var1 < GameScreen.vecPlayers.size(); ++var1) {
         MainObject var2;
         if ((var2 = (MainObject)GameScreen.vecPlayers.elementAt(var1)).typeObject == 1 && var2.typeSpecMonSter == 1 && var2.MR == 2 && var2.Hp > 0) {
            var0 = var2;
            break;
         }
      }

      return var0;
   }

   public final void StartAutoSuperBoss() {
      MainObject var2;
      if (GameScreen.isOnSuperBoss && AA && !AB) {
         GameScreen.isOnSuperBoss = true;
         if (!Interface_Game.isAutoFireInterface) {
            GameScreen.interfaceGame.selectPointer(6);
         }

         if ((var2 = AJ()) == null) {
            if (AC.equals("")) {
               AB();
               GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.XX);
            }
         } else {
            if (GameScreen.objFocus == null || GameScreen.objFocus.isDie || GameScreen.objFocus.Hp <= 0 || GameScreen.objFocus.typeObject != 1 || GameScreen.objFocus.typeSpecMonSter != 1 || GameScreen.objFocus.MR != 2) {
               GameScreen.objFocus = var2;
            }

            if (GameScreen.objFocus != null) {
               AC = GameScreen.objFocus.name;
               if (MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, GameScreen.objFocus.x, GameScreen.objFocus.y) > 180) {
                  AA = false;
                  AE = true;
               } else {
                  GameScreen.player.beginPlayerFire((int)2);
               }
            }
         }

         if ((var2 = GameScreen.objFocus) != null && AC.indexOf("10") >= 0 && (var2.isDie || var2.Hp <= 0)) {
            AB = true;
            AB();
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.XW);
         }
      }

      if (GameScreen.isOnSuperBoss && AE) {
         if (!Interface_Game.isAutoFireInterface) {
            GameScreen.interfaceGame.selectPointer(6);
         }

         if ((var2 = AJ()) != null) {
            int var3 = MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, var2.x, var2.y);
            if (var2 != null) {
               GameScreen.player.toX = var2.x;
               GameScreen.player.toY = var2.y;
               if (MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, GameScreen.player.toX, GameScreen.player.toY) < GameScreen.player.CN << 1) {
                  GameScreen.player.x = var2.x;
                  GameScreen.player.y = var2.y;
               } else {
                  GameScreen.player.NR = true;
               }

               this.AD = var3 < 160;
            }
         }

         if (this.AD) {
            AE = false;
            AA = true;
         }
      }

   }
}
