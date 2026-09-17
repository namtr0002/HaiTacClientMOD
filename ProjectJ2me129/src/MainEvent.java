public final class MainEvent extends Class_GE {
   private iCommand BR;
   public iCommand AA;
   public iCommand AB;
   public iCommand AC;
   public iCommand AD;
   public iCommand AE;
   public iCommand AF;
   public iCommand AG;
   public iCommand AH;
   public iCommand AI;
   public iCommand AJ;
   public iCommand AK;
   public static iCommand AL;
   public static FrameImage fraEvent;
   public static mImage imgNew;
   public static int AO = 120;
   public static int AP = 40;

   public MainEvent(byte var1, mVector var2) {
      super((byte)-3, var2, T.DV, 180, 180);
      super.AR = super.BP - GameCanvas.hCommand - 10;
      if (!GameCanvas.isTouch) {
         super.AR -= iCommand.hButtonCmdNor;
      }

      this.AF();
      this.BR = new iCommand(T.DY, 15, this);
      this.AA = new iCommand(T.CN, 16, this);
      this.AB = new iCommand(T.CJ, 17, this);
      this.AC = new iCommand(T.del, 18, this);
      AL = new iCommand(T.CJ, 19, this);
      this.AD = new iCommand(T.CJ, 20, this);
      this.AE = new iCommand(T.CJ, 21, this);
      this.AF = new iCommand(T.AZ, 22, this);
      this.AG = new iCommand(T.DY, 23, this);
      this.AH = new iCommand(T.CJ, 24, this);
      this.AI = new iCommand(T.CJ, 25, 0, this);
      this.AJ = new iCommand(T.CD, 25, 1, this);
      this.AK = new iCommand(T.CJ, 26, this);
      super.AS.removeAllElements();
      if (!GameCanvas.isTouch) {
         super.AS.addElement(this.BR);
      }

      if (GameCanvas.isTouch) {
         super.DB = super.BB;
      } else {
         super.AS.addElement(super.BB);
      }

      super.backCMD = super.BB;
      super.DF = this.BR;
      super.AX = 0;
      super.BJ = true;
      this.AA(0, super.AS);
   }

   public final void g_() {
      if (!GameCanvas.isTouch) {
         super.AX = 0;
      }

      this.AA(0, super.AS);
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 2:
         GameScreen.AF();
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         return;
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      default:
         break;
      case 15:
         if (super.AW >= 0 && super.AW < super.AY.size()) {
            super.AU = (Class_DS)super.AY.elementAt(super.AW);
         }

         if (super.AU != null) {
            super.AU.AA();
         }

         return;
      case 16:
         if (super.AU != null) {
            GlobalService.getInstance().AA((byte)3, (int)super.AU.AG);
            super.AY.removeElement(super.AU);
            this.AH();
            GameCanvas.end_Dialog();
         }

         return;
      case 17:
         if (super.AU != null) {
            GlobalService.getInstance().AD((byte)4, (short)((short)super.AU.AG));
            super.AY.removeElement(super.AU);
            this.AH();
            GameCanvas.end_Dialog();
         }

         return;
      case 18:
         this.AA(super.AU);
         GameCanvas.end_Dialog();
         return;
      case 19:
         if (super.AU != null) {
            GlobalService.getInstance().AA((byte)1, (short)((short)super.AU.AG), (byte)super.AU.AT);
            super.AY.removeElement(super.AU);
            this.AH();
            GameCanvas.end_Dialog();
         }

         return;
      case 20:
         if (super.AU != null) {
            GlobalService.getInstance().AA((byte)6, (short)super.AU.AG, (byte)0, 1, "");
            super.AY.removeElement(super.AU);
            this.AH();
            GameCanvas.end_Dialog();
         }

         return;
      case 21:
         if (super.AU != null) {
            GlobalService.getInstance().AD((byte)6, (short)((short)super.AU.AG));
            super.AY.removeElement(super.AU);
            this.AH();
            GameCanvas.end_Dialog();
         }

         return;
      case 22:
         if (super.AU != null) {
            GameScreen var10000 = GameCanvas.gameScr;
            GameScreen.AA(super.AU.AC);
            MsgOtherCharInfo.AA = super.AU;
         }
         break;
      case 23:
         if (super.AU != null && GameCanvas.currentScreen != GameCanvas.chatTabScr) {
            GameCanvas.chatTabScr.AB(super.AU.AC, "", "", (byte)0, true);
            GameCanvas.chatTabScr.Show(GameCanvas.currentScreen);
         }

         return;
      case 24:
         if (super.AU != null) {
            GlobalService.getInstance().Clan_CMD((byte)12, "", (short)super.AU.AG, (byte)0);
         }
         break;
      case 25:
         if (super.AU != null) {
            GlobalService.getInstance().AD((byte)(var2 == 0 ? 1 : 2), (short)super.AU.AG, super.AU.AT);
            GameCanvas.end_Dialog();
         }
         break;
      case 26:
         if (super.AU != null) {
            GlobalService.getInstance().AB((byte)18, super.AU.AC, (short)super.AU.AG, (byte)0);
         }
      }

      super.commandPointer(var1, var2);
   }

   public final void AA() {
      this.BR.AD();
   }

   public final void AB() {
      if (super.AY.size() != 0) {
         if (super.AW >= 0 && super.AW < super.AY.size()) {
            super.AU = (Class_DS)super.AY.elementAt(super.AW);
            if (super.AU != null && super.AU.AG != GameScreen.player.ID) {
               mVector var1;
               (var1 = new mVector()).addElement(this.BR);
               if (super.AW != 0) {
                  var1.addElement(this.AC);
               }

               GameCanvas.menu.startAt(var1, 2, super.AU.AC);
            }
         }

      }
   }

   public static void AA(mGraphics var0, int var1, int var2, Class_DS var3) {
      if (var3 != null) {
         var1 -= AO / 2;
         AvMain.paintRect(var0, var1, var2, AO, AP, (byte)1, 2);
         fraEvent.drawFrame(var3.AR, var1 + 15, var2 + AP / 2, 0, 3, var0);
         mFont.tahoma_7b_black.drawString(var0, var3.AC, var1 + 30, var2 + GameCanvas.hText / 2, 0);
         mFont.tahoma_7_black.drawString(var0, var3.AD, var1 + 35, var2 + GameCanvas.hText / 2 * 3, 0);
      }
   }

   public final void AA(mGraphics var1, Class_DS var2, int var3, int var4, int var5, int var6) {
      if (var2 != null) {
         String var7 = var2.AC;
         mFont.tahoma_7b_black.drawString(var1, var7, var3 + 25, var4, 0);
         mFont.tahoma_7_black.drawString(var1, var2.AD, var3 + 30, var4 + GameCanvas.hText, 0);
         fraEvent.drawFrame(var2.AR, var3 + 10, var4 + super.BQ / 2, 0, 3, var1);
         if (var2.AY) {
            var1.drawRegion((mImage)imgNew, var3 + 10 + 8, var4 + super.BQ / 2 - 8, 3);
         }

         if (var5 < super.AY.size() - 1) {
            int var9 = var4 - 6;
            var1.setColor(AvMain.II[4]);
            var1.fillRect(var3 + 4, var9 + 3 + super.BQ - 1, var6 - 8, 2);
            var1.fillRect(var3 + 4 + 1, var9 + 3 + super.BQ - 2, var6 - 8 - 2, 4);
         }

         if (var5 != 0 && GameCanvas.isTouch) {
            var1.drawRegion((mImage)AvMain.imgIconDel, var3 + var6, var4 + super.BQ / 2, 3);
         }

      }
   }

   public final void AA(Class_DS var1) {
      if (var1 != null) {
         if (var1.AY) {
            var1.AY = false;
         }

         if (var1.AQ == 2) {
            String var3 = var1.AC;
            ChatTabScreen var2 = GameCanvas.chatTabScr;

            for(int var4 = 0; var4 < var2.AM.size(); ++var4) {
               if (((ChatDetail)var2.AM.elementAt(var4)).AO.compareTo(var3) == 0) {
                  var2.AM.removeElement(var4);
                  break;
               }
            }

            var2.AO = AvMain.AA(var2.AO, var2.AM.size() - 1, false);
         }

         super.AY.removeElement(var1);
         this.AH();
         if (GameCanvas.isKeyPressed()) {
            super.AW = AvMain.AA(super.AW, super.AY.size() - 1, false);
         }

         GameScreen.AF();
      }

   }
}
