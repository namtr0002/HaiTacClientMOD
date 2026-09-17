import java.io.DataInputStream;

public final class MainQuest extends AvMain {
   public byte AA;
   public byte AB;
   public int AC;
   public int AD = -32000;
   public short AE;
   public short AF;
   public short AG;
   public String AH;
   public String AI;
   public String AJ;
   private String[] AN;
   public String AK;
   public String AL = "";
   public mVector AM = new mVector("MainQuest.vecTypeQuest");
   private int AO = 0;

   public MainQuest(short var1) {
      this.AE = var1;
   }

   public static MainQuest AA(short var0) {
      for(int var1 = 0; var1 < Player.QI.size(); ++var1) {
         MainQuest var2;
         if ((var2 = (MainQuest)Player.QI.elementAt(var1)).AE == var0) {
            return var2;
         }
      }

      return null;
   }

   public final String AA() {
      if (this.AA == 0) {
         return T.BV;
      } else if (this.AA == 2) {
         return T.FB;
      } else {
         return this.AA == 3 ? T.NV : T.BW;
      }
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (GameCanvas.menuCur.AL != null && !GameCanvas.menuCur.AL.AA()) {
            return;
         }

         this.AF();
         return;
      case 1:
         if (GameCanvas.menuCur.AL != null && !GameCanvas.menuCur.AL.AA()) {
            return;
         }

         MainObject var3;
         if ((var3 = MainObject.get_Object((int)this.AC, (byte)2)) != null) {
            var3.LS = null;
         }

         String var10000 = this.AK;
         GameScreen.player.LS = null;
         iCommand var4 = null;
         if (this.AB == 0) {
            var4 = new iCommand(T.BY, 3, this);
         } else if (this.AB == 2) {
            var4 = new iCommand(T.BZ, 3, this);
         }

         Player.RL = null;
         GameCanvas.AH();
         GameCanvas.isPointerSelect = false;
         GameCanvas.menuCur.isShowMenu = false;
         var4.AD();
         return;
      case 2:
         if (this.AB == 0 && GameScreen.player.Lv < this.AG && this.AA == 0) {
            GameCanvas.menuCur.AG();
            GameCanvas.end_Dialog();
            return;
         }

         GameCanvas.menuCur.AG();
         GlobalService.getInstance().AC((byte)1, (short)this.AE);
         GameCanvas.end_Dialog();
         GameCanvas.AA(T.BP, true);
         return;
      case 3:
         GameCanvas.end_Dialog();
         GameCanvas.menuCur.AG();
         this.AO = 0;
         Player.RL = this;
         this.AF();
         return;
      case 4:
         GameCanvas.menuCur.AG();
         GlobalService.getInstance().AC((byte)4, (short)this.AE);
         GameCanvas.end_Dialog();
         GameCanvas.AA(T.BP, true);
         Player.QU = (short)this.AC;
      default:
      }
   }

   public final void AB(short var1) {
      this.AO = 0;
      MainObject var2;
      if (this.AC == var1) {
         if (this.AB == 2 || this.AB == 0) {
            this.commandPointer(0, 0);
            return;
         }

         if (this.AB == 1 && (var2 = MainObject.get_Object((int)this.AC, (byte)2)) != null) {
            var2.BC = this.AJ;
            return;
         }
      } else if (this.AB == 2 && this.AD == var1 && (var2 = MainObject.get_Object((int)this.AD, (byte)2)) != null) {
         var2.BC = this.AJ;
      }

   }

   private void AF() {
      if (MainObject.get_Object((int)this.AC, (byte)2) == null) {
         Player.RL = null;
      } else {
         if (this.AN == null) {
            if (this.AB == 0 && GameScreen.player.Lv < this.AG && this.AA == 0) {
               this.AN = mFont.split(T.QV + this.AG + ".", ">");
            } else {
               this.AN = mFont.split(this.AI, ">");
            }
         }

         if (this.AN == null) {
            Player.RL = null;
         } else {
            mVector var1;
            iCommand var2;
            if (this.AN[this.AO].trim().startsWith("0")) {
               MainObject.get_Object((int)this.AC, (byte)2).LS = null;
               var1 = new mVector();
               var2 = this.AG();
               var1.addElement(var2);
               GameCanvas.menu.AA(var1, GameMidlet.AA(this.AN[this.AO], 1, this.AN[this.AO].length()), GameScreen.player.ID, (byte)0, true, 0);
            } else {
               GameScreen.player.LS = null;
               var1 = new mVector();
               var2 = this.AG();
               var1.addElement(var2);
               GameCanvas.menu.AA(var1, GameMidlet.AA(this.AN[this.AO], 1, this.AN[this.AO].length()), this.AC, (byte)2, true, 0);
            }

            ++this.AO;
         }
      }
   }

   private iCommand AG() {
      iCommand var1;
      if (this.AO < this.AN.length - 1) {
         var1 = new iCommand(T.AK + " >>", 0, this);
      } else if (this.AB == 2) {
         var1 = new iCommand(T.AK + " >>", 4, this);
      } else {
         var1 = new iCommand(T.AK + " >>", 2, this);
      }

      return var1;
   }

   public final iCommand AB() {
      return this.AB == 0 ? TabQuest.BN : TabQuest.BN;
   }

   public static void AA(DataInputStream var0, boolean var1) {
      if (var0 == null) {
         GlobalService.getInstance().get_DATA((byte)7);
      } else {
         try {
            short var2;
            Class_CP.AF = new String[var2 = var0.readShort()];

            for(int var3 = 0; var3 < var2; ++var3) {
               Class_CP.AF[var3] = var0.readUTF();
            }

            if (var1) {
               SaveRms.AA(GlobalService.AI = var0.readShort(), "VerdataNamePotionquest");
            }

            var0.close();
         } catch (Exception var4) {
         }
      }
   }
}
