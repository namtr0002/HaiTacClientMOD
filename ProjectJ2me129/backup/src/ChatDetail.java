public class ChatDetail extends AvMain {
   public int AE = 5;
   public int AF;
   public int AG;
   public int AH;
   public int AI;
   public int AJ;
   public int AK;
   public int AL;
   public ListNew AM;
   public mVector AN = new mVector("ChatDetail.vecDetail");
   public String AO;
   public String AP;
   public String AQ;
   public byte AR = -1;
   public boolean AS = false;
   public TField AT;
   public byte AU = 0;
   public int AV = 0;
   private int AA = 0;
   private byte AB = 0;
   public Scroll AW = new Scroll();

   public ChatDetail(String var1, byte var2) {
      this.AO = var1;
      this.AU = var2;
      this.AP = var1;
      if (var1.length() >= 5) {
         this.AP = var1.substring(0, 5);
      }

      this.AQ = var1;
      if (var1.length() >= 10) {
         this.AQ = var1.substring(0, 9) + "...";
      }

      if (this.AU == 0) {
         this.AT = new TField(GameCanvas.chatTabScr.AF, GameCanvas.chatTabScr.AG + GameCanvas.chatTabScr.AI - TField.AB() - GameCanvas.chatTabScr.AE / 2, GameCanvas.chatTabScr.AH);
         this.AT.AI = false;
      } else if (this.AU == 2) {
         this.AO = T.CN;
      }

      if (var1.compareTo(T.CQ) != 0 && var1.compareTo(T.CR) != 0 && var1.compareTo(T.CS) != 0) {
         this.AB = 0;
      } else {
         this.AB = 1;
      }

      this.AW.AA(GameCanvas.chatTabScr.AF + GameCanvas.chatTabScr.AH + (GameCanvas.chatTabScr.AE << 1), GameCanvas.chatTabScr.AG, GameCanvas.chatTabScr.AI, -7967666);
   }

   public final void AA(String var1, String var2, int var3) {
      if (var1.length() > 0) {
         String[] var4 = mFont.tahoma_7_white.splitFontArray(var1, GameCanvas.chatTabScr.AH);
         byte var5;
         if (var3 >= 0) {
            var5 = (byte)var3;
         } else {
            var5 = this.AB(var2);
         }

         mSystem[] var6;
         if ((var6 = AA(var4, var5)) != null) {
            for(var3 = 0; var3 < var6.length; ++var3) {
               this.AN.addElement(var6[var3]);
            }
         }

         this.AG();
         if (this.AV > 0 && GameCanvas.currentScreen == GameCanvas.chatTabScr && GameCanvas.chatTabScr.AN != null && GameCanvas.chatTabScr.AN == this) {
            GameCanvas.chatTabScr.AA(var4.length, (byte)1);
         }

         if ((GameCanvas.chatTabScr.AN != null && GameCanvas.chatTabScr.AN != this || GameCanvas.currentScreen != GameCanvas.chatTabScr) && this.AO.compareTo(T.CR) != 0) {
            this.AS = true;
            this.AR = (byte)CRes.random(1, 11);
         }
      }

   }

   public final void AA(String var1) {
      String var2 = "";
      if (this.AT != null) {
         var2 = this.AT.getText();
      }

      if (var2.length() > 0) {
         String[] var3;
         mSystem[] var5;
         if ((var5 = AA(var3 = mFont.tahoma_7_white.splitFontArray(GameScreen.player.name + ": " + var2, GameCanvas.chatTabScr.AH), this.AB(var1))) != null) {
            for(int var4 = 0; var4 < var5.length; ++var4) {
               this.AN.addElement(var5[var4]);
            }
         }

         this.AG();
         if (GameCanvas.currentScreen == GameCanvas.chatTabScr && GameCanvas.chatTabScr.AN != null && GameCanvas.chatTabScr.AN == this) {
            GameCanvas.chatTabScr.AA(var3.length, (byte)1);
         }

         GlobalService.getInstance().AA(this.AO, var2);
      }

      if (this.AT != null) {
         this.AT.AB("");
      }

   }

   public final void AG() {
      this.AV = this.AN.size() * GameCanvas.hText - (GameCanvas.chatTabScr.AI - (this.AU == 0 ? TField.AB() + 2 : 0));
      if (this.AV < 0) {
         this.AV = 0;
      }

   }

   public static mSystem[] AA(String[] var0, byte var1) {
      if (var0 != null && var0.length != 0) {
         mSystem[] var2 = new mSystem[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            var2[var3] = new mSystem(var0[var3], var1);
         }

         return var2;
      } else {
         return null;
      }
   }

   private byte AB(String var1) {
      int var2;
      if (this.AB == 1) {
         var2 = this.AA % 2 == 0 ? 0 : (GameCanvas.IndexServer == 1 ? 1 : 5);
         ++this.AA;
      } else if (var1.compareTo(GameScreen.player.name) == 0) {
         var2 = 5;
      } else {
         if (GameCanvas.IndexServer == 1) {
            return 1;
         }

         var2 = 0;
      }

      return (byte)var2;
   }

   public void paint(mGraphics var1) {
      super.paint(var1);
   }

   public void AA(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   public void update() {
   }

   public void updatePointer() {
   }

   public void handleKeyPress() {
      super.handleKeyPress();
   }

   public void AA(short var1, String var2, String var3, String var4, byte var5, byte var6, short var7, long var8) {
   }

   public final void AA(int var1, byte var2) {
      int var3 = this.AI;
      if (this.AT != null) {
         var3 -= this.AT.AD;
      }

      if (var2 != 1) {
         if (var2 == 0) {
            this.AM = new ListNew(this.AF, this.AG, this.AH, var3, 0, 0, this.AN.size() * GameCanvas.hText - var3, true);
            this.AM.AA(this.AM.AD);
            this.AM.AC = this.AM.AD;
            this.AL = this.AN.size() - 1;
         }

      } else {
         byte var4;
         int var5;
         if ((var5 = this.AM.AB) != 0 && var5 != this.AM.AD) {
            if (var5 < this.AM.AD - this.AI) {
               var4 = 1;
            } else {
               var4 = 2;
            }
         } else {
            var4 = 0;
         }

         if (this.AM == null) {
            this.AM = new ListNew(this.AF, this.AG, this.AH, var3, 0, 0, this.AN.size() * GameCanvas.hText - var3, true);
         } else {
            this.AM.AD = this.AN.size() * GameCanvas.hText - var3;
            if (this.AM.AD < 0) {
               this.AM.AD = 0;
            }
         }

         if (var4 == 0) {
            this.AM.AA(this.AM.AD);
         } else if (var4 == 1) {
            this.AM.AA(var5);
            this.AM.AC = var5;
         } else {
            this.AM.AA(var5 + var1 * GameCanvas.hText);
         }
      }
   }

   public void f_() {
      if (this.AT != null && this.AT.getText().length() > 0 && this.AU == 3) {
         GlobalService.getInstance().Clan_CMD((byte)0, this.AT.getText(), 0, (byte)0);
         this.AT.AB("");
      }

   }

   public void AA() {
   }

   public final void AA(mGraphics var1, byte var2, byte var3, int var4, int var5, int var6, boolean var7) {
      if (!GameCanvas.isKeyPressed()) {
         var7 = false;
      }

      if (var2 < 0 || var2 > 9) {
         var2 = 4;
      }

      int var10001;
      switch(var2) {
      case 0:
         var10001 = -10250290;
         break;
      case 1:
         var10001 = -8147265;
         break;
      case 2:
         var10001 = -1942704;
         break;
      case 3:
         var10001 = -1930179;
         break;
      case 4:
         var10001 = -4683678;
         break;
      case 5:
         var10001 = -8009115;
         break;
      case 6:
         var10001 = -336031;
         break;
      case 7:
         var10001 = -2764079;
         break;
      case 8:
         var10001 = -3704225;
         break;
      case 9:
         var10001 = -986896;
         break;
      default:
         var10001 = -4683678;
      }

      var1.setColor(var10001);
      int var8 = this.AF - 2 + var4;
      int var9 = var6 - 1;
      var4 = this.AH + 4 - (var4 << 1);
      var1.fillRect(var8, var9, var4, var5);
      if (var7 && GameCanvas.gameTick % 12 < 6) {
         var2 = 10;
      }

      switch(var2) {
      case 0:
         var10001 = -14131303;
         break;
      case 1:
         var10001 = -12750201;
         break;
      case 2:
         var10001 = -6345178;
         break;
      case 3:
         var10001 = -6203113;
         break;
      case 4:
         var10001 = -7708871;
         break;
      case 5:
         var10001 = -10970049;
         break;
      case 6:
         var10001 = -2183124;
         break;
      case 7:
         var10001 = -8027262;
         break;
      case 8:
         var10001 = -6598604;
         break;
      case 9:
         var10001 = -5131855;
         break;
      case 10:
         var10001 = -16777216;
         break;
      default:
         var10001 = -7708871;
      }

      var1.setColor(var10001);
      AvMain.fraBorderClan.drawFrame(var2 << 2, var8, var9, 0, 0, var1);
      AvMain.fraBorderClan.drawFrame((var2 << 2) + 1, var8 - this.AE + var4 + 1, var9, 0, 0, var1);
      AvMain.fraBorderClan.drawFrame((var2 << 2) + 2, var8, var9 + var5 - 4, 0, 0, var1);
      AvMain.fraBorderClan.drawFrame((var2 << 2) + 3, var8 - this.AE + var4 + 1, var9 + var5 - 4, 0, 0, var1);
      var1.fillRect(var8, var6 + 3, 1, var5 - 8);
      var1.fillRect(var8 - 1 + var4, var6 + 3, 1, var5 - 8);
      var1.fillRect(var8 + 4, var9, var4 - 8, 1);
      var1.fillRect(var8 + 4, var6 + var5 - 2, var4 - 8, 1);
      if (var3 == 1) {
         var1.drawRegion(AvMain.imgChatClan, 0, 7, 7, 7, 0, var8 - 6, var9 + 12, 0);
      } else {
         if (var3 == 2) {
            var1.drawRegion(AvMain.imgChatClan, 0, 0, 7, 7, 0, var8 + var4 - 1, var9 + 12, 0);
         }

      }
   }
}
