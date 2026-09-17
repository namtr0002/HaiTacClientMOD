public final class Class_DS {
   public short AA;
   public short AB;
   public String AC;
   public String AD = "";
   private String BJ;
   public String AE;
   public String AF;
   public int AG;
   public int AH;
   public int AI;
   public int AJ;
   public int AK;
   private int BK;
   public int AL;
   public int AM;
   public int AN;
   public byte AO = 0;
   public byte AP = -1;
   public byte AQ;
   public byte AR;
   public byte AS;
   public byte AT;
   public byte AU = -1;
   public short AV;
   public short AW;
   public short AX;
   public boolean AY = false;
   public boolean AZ = false;
   public int BA = 0;
   public int BB = 0;
   public MainObject BC;
   public String BD;
   public byte BE;
   public byte BF;
   public byte BG;
   public Class_AX BH;
   public Class_IR BI;

   public Class_DS(int var1) {
      this.AG = var1;
      this.AK = -1;
   }

   public final void AA(short var1, short var2, short var3) {
      this.AV = var1;
      this.AW = var2;
      this.AX = var3;
   }

   public final void AA(String var1, short var2, byte var3, byte var4) {
      this.AC = var1;
      this.AA = var2;
      this.AO = var3;
      this.AB = var4;
   }

   public final void AA(int var1, int var2, int var3) {
      this.AI = var1;
      this.AJ = var2;
      this.AK = var3;
   }

   public static void AA(int var0, byte var1, String var2, String var3, int var4, int var5) {
      boolean var10000;
      label90: {
         if (var1 == 0) {
            if (Class_AT.AA(2, "")) {
               GameCanvas.chatTabScr.AB(T.RH, "", var2 + T.XB[2] + GameCanvas.AT() + "\n", (byte)1, false);
               var10000 = true;
               break label90;
            }
         } else if (var1 == 1) {
            if (Class_AT.AA(3, "")) {
               GameCanvas.chatTabScr.AB(T.RH, "", var2 + T.XB[3] + GameCanvas.AT() + "\n", (byte)1, false);
               var10000 = true;
               break label90;
            }
         } else if (var1 == 4) {
            if (Class_AT.AA(4, "")) {
               GameCanvas.chatTabScr.AB(T.RH, "", var2 + T.XB[4] + GameCanvas.AT() + "\n", (byte)1, false);
               var10000 = true;
               break label90;
            }
         } else if (var1 == 3 && Class_AT.AA(5, "")) {
            GameCanvas.chatTabScr.AB(T.RH, "", var2 + T.XB[5] + GameCanvas.AT() + "\n", (byte)1, false);
            var10000 = true;
            break label90;
         }

         var10000 = false;
      }

      if (!var10000) {
         Class_DS var6 = AA(var2, var1);
         boolean var7 = false;
         if (var6 == null) {
            var6 = new Class_DS(var0);
            var7 = true;
         }

         var6.AQ = var1;
         var6.AC = var2;
         var6.AY = true;
         var6.AD = var3;
         var6.BK = var4;
         if (var6.AQ == 0) {
            var6.AR = 1;
            if (var7) {
               Interface_Game.AA(var6);
            }
         } else if (var6.AQ == 1) {
            var6.AR = 2;
            if (var7) {
               Interface_Game.AA(var6);
            }
         } else if (var6.AQ == 2) {
            var6.AR = 0;
            var6.AY = false;
            if (var2.compareTo(T.CR) != 0 && var3.length() > 0) {
               if (var7) {
                  Interface_Game.AA(var6);
               }

               var6.AY = true;
            }
         } else if (var6.AQ == 3) {
            var6.AR = 3;
            var6.AT = (byte)var5;
            if (var7) {
               Interface_Game.AA(var6);
            }
         } else if (var6.AQ == 4) {
            var6.AR = 4;
            if (var7) {
               Interface_Game.AA(var6);
            }
         } else if (var6.AQ == 5) {
            var6.AR = 2;
            var6.BJ = var2 + var3;
            var6.AD = var2;
            if (var6.AD.length() > 16) {
               var6.AD = var6.AD.substring(0, 15);
            }

            var6.AC = T.LH;
            if (var7) {
               Interface_Game.AA(var6);
            }
         } else if (var6.AQ == 6) {
            var6.AR = 5;
            if (var7) {
               Interface_Game.AA(var6);
            }
         } else if (var6.AQ == 7) {
            var6.AR = 6;
            var6.AT = (byte)var5;
            if (var7) {
               Interface_Game.AA(var6);
            }
         }

         if (var7) {
            Player.vecEvent.addElement(var6);
         }

         GameScreen.AF();
      }
   }

   public static Class_DS AA(int var0, mVector var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         Class_DS var3;
         if ((var3 = (Class_DS)var1.elementAt(var2)).AG == var0) {
            return var3;
         }
      }

      return null;
   }

   public final void AA() {
      if (this.AY) {
         this.AY = false;
      }

      GameScreen.AF();
      mVector var1 = new mVector();
      switch(this.AQ) {
      case 0:
         var1.addElement(GameCanvas.eventScr.AA);
         var1.addElement(GameCanvas.eventScr.AC);
         GameCanvas.Start_Normal_DiaLog(this.AC + " " + T.DN, var1, true);
         return;
      case 1:
         var1.addElement(GameCanvas.eventScr.AB);
         var1.addElement(GameCanvas.eventScr.AC);
         GameCanvas.Start_Normal_DiaLog(this.AC + T.CI, var1, true);
         return;
      case 2:
         if (GameCanvas.currentScreen != GameCanvas.chatTabScr) {
            if (!GameCanvas.isTouch && this.AC.compareTo(T.CR) != 0) {
               var1.addElement(GameCanvas.eventScr.AG);
               var1.addElement(GameCanvas.eventScr.AC);
               GameCanvas.Start_Normal_DiaLog(T.CP + " " + this.AC, var1, true);
               return;
            }

            GameCanvas.chatTabScr.AB(this.AC, "", "", (byte)0, true);
            GameCanvas.chatTabScr.Show(GameCanvas.currentScreen);
            return;
         }
         break;
      case 3:
         var1.addElement(MainEvent.AL);
         var1.addElement(GameCanvas.eventScr.AF);
         var1.addElement(GameCanvas.eventScr.AC);
         String var2;
         if (this.AT == 1) {
            var2 = GameMidlet.Start_Normal_DiaLog(T.SA, new String[]{this.AC, "" + this.BK});
         } else {
            var2 = GameMidlet.Start_Normal_DiaLog(T.EN, new String[]{this.AC, "" + this.BK});
         }

         GameCanvas.Start_Normal_DiaLog(var2, var1, true);
         return;
      case 4:
         var1.addElement(GameCanvas.eventScr.AD);
         var1.addElement(GameCanvas.eventScr.AC);
         GameCanvas.Start_Normal_DiaLog(this.AC + " " + T.yeucaugiaodich, var1, true);
         return;
      case 5:
         var1.addElement(GameCanvas.eventScr.AE);
         var1.addElement(GameCanvas.eventScr.AC);
         GameCanvas.Start_Normal_DiaLog(this.BJ, var1, true);
         return;
      case 6:
         var1.addElement(GameCanvas.eventScr.AH);
         var1.addElement(GameCanvas.eventScr.AC);
         GameCanvas.Start_Normal_DiaLog(this.AC + T.NP, var1, true);
         return;
      case 7:
         var1.addElement(GameCanvas.eventScr.AI);
         var1.addElement(GameCanvas.eventScr.AJ);
         GameCanvas.Start_Normal_DiaLog(this.AC + T.SK, var1, true);
         return;
      case 8:
         var1.addElement(GameCanvas.eventScr.AK);
         var1.addElement(GameCanvas.eventScr.AC);
         GameCanvas.Start_Normal_DiaLog(this.AC + T.VR, var1, false);
      }

   }

   public static Class_DS AA(String var0, byte var1) {
      for(int var2 = 0; var2 < Player.vecEvent.size(); ++var2) {
         Class_DS var3;
         if ((var3 = (Class_DS)Player.vecEvent.elementAt(var2)).AC.compareTo(var0) == 0 && var3.AQ == var1) {
            return var3;
         }
      }

      return null;
   }

   public static String AA(byte var0) {
      if (var0 == 0) {
         return T.thuyentruong;
      } else if (var0 == 1) {
         return T.thuyenpho;
      } else {
         return var0 == 2 ? T.hoatieu : T.thanhvien;
      }
   }
}
