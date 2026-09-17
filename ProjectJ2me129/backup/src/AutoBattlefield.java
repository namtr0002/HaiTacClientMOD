public final class AutoBattlefield extends MainObject {
   private static int dFocus = 140;
   public static boolean isChangeFocus = false;

   private static MainObject findEnemyFocus() {
      try {
         MainObject mainObject = null;
         byte typePK = GameScreen.player.typePK;
         short num = GameScreen.player.clan == null ? -1 : GameScreen.player.clan.ID;

         for(int i = 0; i < GameScreen.vecPlayers.size(); ++i) {
            MainObject mainObject2;
            if ((mainObject2 = (MainObject)GameScreen.vecPlayers.elementAt(i)) != null && mainObject2.ID != GameScreen.player.ID) {
               byte typePK2 = mainObject2.typePK;
               int distance = MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, mainObject2.x, mainObject2.y);
               short num2 = mainObject2.clan != null ? mainObject2.clan.ID : -1;
               if (typePK > 0 && (typePK2 != typePK || typePK2 == 3 && num != num2) && typePK2 != -1 && mainObject2.typeObject == 0 && (!mainObject2.isDie || mainObject2.Hp > 0) && distance < dFocus) {
                  mainObject = mainObject2;
                  break;
               }
            }
         }

         return mainObject;
      } catch (Exception e) {
         return null;
      }
   }

   private static MainObject findEnemyFocusFar() {
      try {
         MainObject var0 = null;
         byte var1 = GameScreen.player.typePK;
         short var2 = GameScreen.player.clan == null ? -1 : GameScreen.player.clan.ID;

         for(int var3 = 0; var3 < GameScreen.vecPlayers.size(); ++var3) {
            MainObject var4;
            if ((var4 = (MainObject)GameScreen.vecPlayers.elementAt(var3)) != null && var4.ID != GameScreen.player.ID) {
               byte var5 = var4.typePK;
               int var6 = MainObject.getDistance(GameScreen.player.x, GameScreen.player.y, var4.x, var4.y);
               short var7 = var4.clan != null ? var4.clan.ID : -1;
               if (var1 > 0 && (var5 != var1 || var5 == 3 && var2 != var7) && var5 != -1 && var4.typeObject == 0 && (!var4.isDie || var4.Hp > 0) && var6 >= dFocus) {
                  var0 = var4;
                  break;
               }
            }
         }

         return var0;
      } catch (Exception var8) {
         return null;
      }
   }

   public static boolean checkMapPb() {
      int idMap;
      return (idMap = GameCanvas.loadmap.idMapLoadMap) == 157 || idMap == 159 || idMap == 161 || idMap == 163 || idMap == 165;
   }

   public static void CheckMapAutoBattlefield() {
      if (checkMapPb()) {
         GameScreen.isOnAutoPB = true;
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.notifyOnAutoPB);
      } else {
         StopAutoBattlefield();
         GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.noAutoPB);
      }
   }

   public static void StartAutoBattlefield() {
      if (GameScreen.isOnAutoPB && isChangeFocus) {
         MainObject mainObject;
         if ((mainObject = findEnemyFocus()) == null) {
            mainObject = findEnemyFocusFar();
         }

         if (mainObject != null) {
            GameScreen.objFocus = mainObject;
            if (GameScreen.player.Hp <= 0 || GameScreen.player.Action == 4) {
               Player.AutoFireCur = 0;
               return;
            }

            GameScreen.player.beginPlayerFire((int)2);
            isChangeFocus = false;
         }
      }

   }

   public static void StopAutoBattlefield() {
      GameScreen.isOnAutoPB = false;
      isChangeFocus = false;
   }
}
