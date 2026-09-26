public final class PartyScreen extends Class_GE {
   private iCommand AB;
   private iCommand AC;
   private iCommand AD;
   private iCommand AE;
   private iCommand AF;
   public static PartyScreen instance;

   public static PartyScreen AI() {
      return instance == null ? (instance = new PartyScreen((byte)-1, Player.vecParty)) : instance;
   }

   private PartyScreen(byte var1, mVector var2) {
      super((byte)-1, var2, T.CF, 180, 180);
      this.AB = new iCommand(T.CM, 3, 0, this);
      this.AC = new iCommand(T.CL + " " + T.CH, 7, 0, this);
      this.AD = new iCommand(T.CD + " " + T.CH, 8, 0, this);
      this.AE = new iCommand(T.CP + " " + T.CH, 9, 0, this);
      this.AF = new iCommand(T.KZ, 10, 0, this);
      super.backCMD = super.BB;
      super.DF = super.BG;
      super.DE = super.BA;
   }

   public final void g_() {
      if (GameCanvas.isKeyPressed()) {
         super.AX = 0;
      }

      this.AA(0, super.AS);
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 3:
         if (super.AU != null) {
            GlobalService.getInstance().AD((byte)2, (short)((short)super.AU.AG));
         }

         return;
      case 4:
      case 5:
      case 6:
      default:
         super.commandPointer(var1, var2);
         return;
      case 7:
         GlobalService.getInstance().AD((byte)2, (short)GameScreen.player.ID);
         return;
      case 8:
         GlobalService.getInstance().AD((byte)3, (short)GameScreen.player.ID);
         return;
      case 9:
         GameCanvas.chatTabScr.AB(T.CH, "", "", (byte)0, true);
         GameCanvas.chatTabScr.Show((MainScreen)AI());
         return;
      case 10:
         MsgDialog var3;
         (var3 = new MsgDialog()).AA(GameScreen.player.AG);
         GameCanvas.Start_Current_Dialog((MainDialog)var3);
      }
   }

   public final void AA() {
      if (super.AY.size() != 0) {
         super.AU = (Class_DS)super.AY.elementAt(super.AW);
         if (super.AU != null && super.AU.AG != GameScreen.player.ID) {
            mVector var1 = new mVector();
            Class_DS var2 = Class_DS.AA(GameScreen.player.ID, Player.vecParty);
            byte var3 = 1;
            if (var2 != null) {
               var3 = var2.AO;
            }

            var1.addElement(super.BC);
            var1.addElement(super.BD);
            var1.addElement(super.BE);
            if (var3 == 1) {
               var1.addElement(this.AB);
            }

            GameCanvas.menu.startAt(var1, 2, super.AU.AC);
         }

      }
   }

   public final void AB() {
      if (super.AY.size() != 0) {
         super.AU = (Class_DS)super.AY.elementAt(super.AW);
         if (super.AU != null) {
            mVector var1;
            (var1 = new mVector()).addElement(this.AC);
            var1.addElement(this.AE);
            var1.addElement(this.AF);
            Class_DS var2 = Class_DS.AA(GameScreen.player.ID, Player.vecParty);
            byte var3 = 1;
            if (var2 != null) {
               var3 = var2.AO;
            }

            if (var3 == 1) {
               var1.addElement(this.AD);
               if (!GameCanvas.isTouch) {
                  var1.addElement(this.AB);
               }
            }

            if (!GameCanvas.isTouch) {
               var1.addElement(super.BC);
               var1.addElement(super.BD);
               var1.addElement(super.BE);
            }

            GameCanvas.menu.startAt(var1, 2, super.AU.AC);
         }

      }
   }

   public final void AA(mGraphics var1, Class_DS var2, int var3, int var4, int var5, int var6) {
      String var7 = var2.AC;
      if (var2.AK >= 0) {
         var7 = var7 + " - " + var2.AK;
      }

      if (var5 == 0) {
         AvMain.AA(var1, var7, var3, var4, 0, (byte)0, (byte)7);
      } else {
         mFont.tahoma_7b_blue.drawString(var1, var7, var3, var4, 0);
      }

      Interface_Game.AA(var1, (byte)1, var2.AI, var2.AJ, var3, var4 + 12, 0, 6, 66, -1, false, 0, false, 0);
      mFont.tahoma_7_black.drawString(var1, LoadMap.AB(var2.AA) + " " + T.EU + " " + LoadMap.AA((byte)var2.AB), var3 + super.AV, var4 + 19, 0);
   }

   public final void update() {
      super.update();
      if (GameCanvas.gameTick % 100 == 0) {
         for(int var1 = 0; var1 < GameScreen.vecPlayers.size(); ++var1) {
            MainObject var2 = (MainObject)GameScreen.vecPlayers.elementAt(var1);

            for(int var3 = 0; var3 < Player.vecParty.size(); ++var3) {
               Class_DS var4;
               if ((var4 = (Class_DS)Player.vecParty.elementAt(var3)).AG == var2.ID && var2.typeObject == 0) {
                  var4.AA(var2.Hp, var2.maxHp, var2.Lv);
               }
            }
         }
      }

   }
}
