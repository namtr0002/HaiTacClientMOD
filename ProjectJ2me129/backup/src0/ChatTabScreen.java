public class ChatTabScreen extends MainScreen {
   public int AA;
   public int AB;
   public int AC = 225;
   public int AD = 194;
   public int AE = 5;
   public int AF;
   public int AG;
   public int AH;
   public int AI;
   public int AJ;
   public int AK;
   public int AL;
   public mVector AM = new mVector("ChatTabScreen.vecTabChat");
   public ChatDetail AN;
   public int AO = 0;
   private ListNew AP;
   private ListNew AQ;
   private iCommand AR;
   private iCommand AS;
   private iCommand AT;
   private int AU;
   private int AV;

   public ChatTabScreen() {
      if (this.AC > MotherCanvas.w) {
         this.AC = MotherCanvas.w;
      }

      if (this.AD > MotherCanvas.h - GameCanvas.hCommand - 10) {
         this.AD = MotherCanvas.h - GameCanvas.hCommand - 10;
      }

      this.AA = MotherCanvas.hw - this.AC / 2;
      if (GameCanvas.isTouch) {
         this.AB = MotherCanvas.hh - this.AD / 2;
      } else {
         this.AB = MotherCanvas.hh - this.AD / 2 - GameCanvas.hCommand / 2;
      }

      this.AJ = 24;
      this.AF = this.AA + this.AJ + this.AE;
      this.AG = this.AB + this.AJ + this.AE;
      this.AI = this.AD - this.AJ - this.AE - (this.AE << 1);
      this.AH = this.AC - (this.AJ << 1) - (this.AE << 1);
      this.AK = this.AI / GameCanvas.hText + 2;
      this.AP = new ListNew();
      this.AQ = new ListNew();
      this.AL = 60;
      this.AR = new iCommand(T.close, 0, this);
      if (GameCanvas.isTouch) {
         this.AR.setPos(this.AA + this.AC - this.AJ / 2, this.AB + this.AJ / 10 + this.AE / 2 + (this.AJ / 5 << 1), MainTab.fraCloseTab, "");
      } else {
         this.AR = AvMain.AA(this.AR, 1);
      }

      super.DA = this.AR;
      super.DE = super.DA;
      this.AS = new iCommand(T.close + " " + T.CP, 1, this);
      new iCommand(T.close + " " + T.CU, 2, this);
      this.AT = new iCommand(T.AD, 3, this);
   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public final void Show(MainScreen var1) {
      ChatTabScreen var2 = this;

      for(int var3 = 0; var3 < var2.AM.size(); ++var3) {
         ChatDetail var4;
         if ((var4 = (ChatDetail)var2.AM.elementAt(var3)).AU != 3 && var4.AU != 5 && var4.AN.size() > 40) {
            int var5 = var4.AN.size() - 30;

            for(int var6 = 0; var6 < var5; ++var6) {
               var4.AN.removeElement(0);
            }
         }
      }

      this.AD(this.AO);
      super.Show(var1);
   }

   public void commandPointer(int var1, int var2) {
      System.out.println("commandPointer trong ChatTabScreen:" + var1);
      switch(var1) {
      case 0:
         this.AS.AD();
         return;
      case 1:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }

         GameCanvas.gameScr.Show();
         return;
      case 2:
         if (this.AO == 0) {
            this.AS.AD();
            return;
         }

         if (this.AO >= 0 && this.AO < this.AM.size()) {
            this.AM.removeElement(this.AO);
         }

         this.AO = AvMain.AA(this.AO, this.AM.size() - 1, false);
         this.AD(this.AO);
         if (this.AM.size() == 1 && !GameCanvas.isTouch) {
            this.AR.caption = T.close;
            return;
         }
         break;
      case 3:
         if (this.AN != null && this.AN.AT != null) {
            this.AN.AA(GameScreen.player.name);
         }
      }

   }

   public void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      MainTab.AA(var1, this.AA, this.AB, this.AC, this.AD);
      AvMain.paintRect(var1, this.AF - this.AE, this.AG - this.AE, this.AH + (this.AE << 1), this.AI + (this.AE << 1), (byte)0, 4);
      var1.AD(this.AA + this.AJ / 2, this.AB, this.AC - this.AJ, this.AJ + (this.AE << 1));
      mGraphics.AC();
      int var10000 = this.AA;
      var10000 = this.AJ;
      var10000 = this.AB;
      var10000 = this.AC;
      var10000 = this.AJ;
      var10000 = this.AJ;
      var10000 = this.AE;
      mGraphics.AD();
      var1.translate(-this.AQ.AC, 0);
      int var2 = this.AF;

      int var3;
      ChatDetail var4;
      byte var5;
      for(var3 = 0; var3 < this.AO && var3 < this.AM.size(); ++var3) {
         var4 = (ChatDetail)this.AM.elementAt(var3);
         var5 = 2;
         if (var4.AS && (var4.AR + GameCanvas.gameTick) % 8 < 4) {
            var5 = 1;
         }

         AvMain.paintRect(var1, var2, this.AB + this.AJ / 10 + this.AE / 2, this.AL / 2, this.AJ / 5 << 2, (byte)1, var5);
         mFont.tahoma_7_white.drawString(var1, var4.AP, var2 + 2, this.AB + this.AJ / 10 + this.AE / 2 + 2, 0);
         var2 += this.AL / 2;
      }

      for(var3 = this.AM.size() - 1; var3 > this.AO && var3 < this.AM.size() && var3 >= 0; --var3) {
         var4 = (ChatDetail)this.AM.elementAt(var3);
         var5 = 2;
         if (var4.AS && (var4.AR + GameCanvas.gameTick) % 8 < 4) {
            var5 = 1;
         }

         AvMain.paintRect(var1, this.AF + this.AL / 2 + var3 * (this.AL / 2), this.AB + this.AJ / 10 + this.AE / 2, this.AL / 2 + 1, this.AJ / 5 << 2, (byte)1, var5);
         mFont.tahoma_7_white.drawString(var1, var4.AP, this.AF + this.AL / 2 + var3 * (this.AL / 2) + 4, this.AB + this.AJ / 10 + this.AE / 2 + 2, 0);
      }

      if (this.AN != null) {
         AvMain.paintRect(var1, this.AF + this.AL / 2 * this.AO, this.AB + this.AJ / 10 + this.AE / 2, this.AL, this.AJ / 5 << 2, (byte)1, 4);
         byte var6 = 0;
         int var7;
         if (this.AO == 0 && !GameCanvas.AQ()) {
            var6 = 9;
            var7 = GameCanvas.IndexServer;
            if (GameCanvas.IndexServer >= 3) {
               var7 = GameCanvas.IndexServer + 1;
            }

            Interface_Game.fraBorderNoti4.drawFrame(GameCanvas.language == 0 ? var7 : 3, this.AF + this.AL / 2 * this.AO + 9, this.AB + this.AJ / 10 + this.AE / 2 + 2 + 5, 0, 3, var1);
         }

         mFont.tahoma_7b_white.drawString(var1, this.AN.AQ, this.AF + this.AL / 2 * this.AO + this.AL / 2 + var6, this.AB + this.AJ / 10 + this.AE / 2 + 2, 2);
         mGraphics.AE();
         mGraphics.restoreCanvas();
         GameCanvas.resetTrans(var1);
         if (this.AN.AT != null) {
            this.AN.AT.paint(var1);
         }

         var1.AD(this.AF - this.AE, this.AG - this.AE, this.AH + (this.AE << 1), this.AI - (this.AN.AT != null ? this.AN.AT.AD : -this.AE) + 2);
         mGraphics.AC();
         var10000 = this.AF;
         var10000 = this.AE;
         var10000 = this.AG;
         var10000 = this.AE;
         var10000 = this.AH;
         var10000 = this.AE;
         var10000 = this.AI;
         if (this.AN.AT != null) {
            var10000 = this.AN.AT.AD;
         } else {
            var10000 = this.AE;
         }

         mGraphics.AD();
         var1.translate(0, -this.AP.AC);
         this.AU = this.AP.AC / GameCanvas.hText - 2;
         if (this.AU < 0) {
            this.AU = 0;
         }

         this.AV = this.AU + this.AK;

         for(var7 = this.AU; var7 <= this.AV; ++var7) {
            if (var7 < this.AN.AN.size() && var7 >= 0) {
               mSystem var8;
               AvMain.AB((var8 = (mSystem)this.AN.AN.elementAt(var7)).AE).drawString(var1, var8.AA, this.AF, this.AG + var7 * GameCanvas.hText, 0);
            }
         }

         mGraphics.AE();
         mGraphics.restoreCanvas();
      }

      super.paint(var1);
      if (this.AN != null && this.AP.AD > 0) {
         this.AN.AW.AA(var1);
      }

   }

   public void update() {
      if (super.KP != null) {
         super.KP.update();
      }

      this.AQ.AC();
      this.AP.AC();
      if (this.AN != null) {
         this.AN.AW.AA(this.AP.AC, this.AP.AD);
      }

      if (this.AN.AT != null) {
         this.AN.AT.update();
      }

      super.update();
   }

   public void handleKeyPress() {
      int var1 = this.AO;
      if (GameCanvas.AL[4]) {
         GameCanvas.AB(4);
         if (this.AO > 0) {
            --this.AO;
         }
      } else if (GameCanvas.AL[6]) {
         GameCanvas.AB(6);
         if (this.AO < this.AM.size() - 1) {
            ++this.AO;
         }
      } else {
         ListNew var10000;
         if (GameCanvas.AL[2]) {
            GameCanvas.AB(2);
            var10000 = this.AP;
            var10000.AB -= GameCanvas.hText;
            if (this.AP.AB < 0) {
               this.AP.AB = 0;
            }
         } else if (GameCanvas.AL[8]) {
            GameCanvas.AB(8);
            var10000 = this.AP;
            var10000.AB += GameCanvas.hText;
            if (this.AP.AB > this.AP.AD) {
               this.AP.AB = this.AP.AD;
            }
         }
      }

      if (var1 != this.AO) {
         this.AD(this.AO);
         this.AQ.AA(this.AF + this.AO * this.AL / 2 + this.AL / 2 - this.AC / 2);
      }

      super.handleKeyPress();
      if (GameCanvas.AG(5)) {
         if (super.DF != null) {
            GameCanvas.AA(5);
            GameCanvas.AB(5);
            super.DF.AD();
            return;
         }
      } else if (GameCanvas.isTouch && GameCanvas.AG(12)) {
         if (super.DE != null) {
            GameCanvas.AA(12);
            GameCanvas.AB(12);
            super.DE.AD();
            return;
         }
      } else if (GameCanvas.isTouch && GameCanvas.AG(13) && super.backCMD != null) {
         GameCanvas.AA(13);
         GameCanvas.AB(13);
         super.backCMD.AD();
      }

   }

   public final void AB() {
      super.handleKeyPress();
   }

   public void updatePointer() {
      this.AQ.AB();
      this.AP.update_Pos_UP_DOWN();
      if (GameCanvas.AB(this.AA + this.AJ / 2, this.AB, this.AC - this.AJ, this.AJ + (this.AE << 1))) {
         int var1;
         if ((var1 = (this.AQ.AC + GameCanvas.AY - this.AF) / (this.AL / 2)) > this.AO) {
            --var1;
         }

         if (var1 >= 0 && var1 < this.AM.size() && var1 != this.AO) {
            this.AD(var1);
         }
      }

      if (this.AN.AT != null) {
         this.AN.AT.updatePointer();
      }

      super.updatePointer();
   }

   public final void AA(int var1) {
      if (this.AN.AT != null) {
         this.AN.AT.AD(var1);
      }

      super.AA(var1);
   }

   public final void AA(int var1, byte var2) {
      int var3 = this.AI;
      if (this.AN.AT != null) {
         var3 -= this.AN.AT.AD;
      }

      if (this.AN != null) {
         if (var2 == 1) {
            byte var4;
            int var5;
            if ((var5 = this.AP.AB) != 0 && var5 != this.AP.AD) {
               if (var5 < this.AP.AD - this.AI) {
                  var4 = 1;
               } else {
                  var4 = 2;
               }
            } else {
               var4 = 0;
            }

            if (this.AP == null) {
               this.AP = new ListNew(this.AF, this.AG, this.AH, var3, 0, 0, this.AN.AN.size() * GameCanvas.hText - var3, true);
            } else {
               this.AP.AD = this.AN.AN.size() * GameCanvas.hText - var3;
               if (this.AP.AD < 0) {
                  this.AP.AD = 0;
               }
            }

            if (var4 == 0) {
               this.AP.AA(this.AP.AD);
               return;
            }

            if (var4 == 1) {
               this.AP.AA(var5);
               this.AP.AC = var5;
               return;
            }

            this.AP.AA(var5 + var1 * GameCanvas.hText);
            return;
         }

         if (var2 == 0) {
            this.AP = new ListNew(this.AF, this.AG, this.AH, var3, 0, 0, this.AN.AN.size() * GameCanvas.hText - var3, true);
            this.AP.AA(this.AP.AD);
            this.AP.AC = this.AP.AD;
         }
      }

   }

   public final void AA(String var1, String var2, String var3, byte var4, boolean var5) {
      if (!Class_AT.AA(2, var1)) {
         this.AB(var1, var2, var3, (byte)0, false);
      } else {
         for(int var6 = 0; var6 < this.AM.size(); ++var6) {
            if (((ChatDetail)this.AM.elementAt(var6)).AO.compareTo(var1) == 0) {
               this.AB(var1, var2, var3, (byte)0, false);
               return;
            }
         }

      }
   }

   public final void AB(String var1, String var2, String var3, byte var4, boolean var5) {
      this.addNewChat(var1, var2, var3, var4, var5, -1);
   }

   public final void addNewChat(String var1, String var2, String var3, byte var4, boolean var5, int var6) {
      if (var3 != null) {
         if (var3.length() > 0) {
            String[] var7;
            String var8 = (var7 = mFont.tahoma_7_white.splitFontArray(var3, MainEvent.AO - 50))[0];
            if (var7.length > 1) {
               var8 = var8 + "...";
            }

            Class_DS.AA(-1, (byte)2, var1, var8, 0, 0);
         }

         for(int var9 = 0; var9 < this.AM.size(); ++var9) {
            ChatDetail var11;
            if ((var11 = (ChatDetail)this.AM.elementAt(var9)).AO.compareTo(var1) == 0) {
               if (var5) {
                  this.AO = var9;
               }

               if (var3.length() > 0) {
                  var11.AA(var2 + var3, var1, var6);
               }

               return;
            }
         }

         ChatDetail var10 = new ChatDetail(var1, var4);
         if (var3.length() > 0) {
            var10.AA(var2 + var3, var1, var6);
         }

         this.AM.addElement(var10);
         if (var5) {
            this.AO = this.AM.size() - 1;
         }

         if (!GameCanvas.isTouch) {
            this.AR.caption = T.close;
         }

         int var12;
         if ((var12 = (this.AM.size() + 1) * this.AL / 2 - this.AH) > 0) {
            this.AQ.AD = var12;
         }

      }
   }

   public void AD(int var1) {
      this.AO = var1;
      if (this.AO >= 0 && this.AO < this.AM.size()) {
         this.AN = (ChatDetail)this.AM.elementAt(this.AO);
         if (GameCanvas.eventScr != null && GameCanvas.eventScr.AY != null) {
            for(var1 = 0; var1 < GameCanvas.eventScr.AY.size(); ++var1) {
               Class_DS var2;
               if ((var2 = (Class_DS)GameCanvas.eventScr.AY.elementAt(var1)).AC.compareTo(this.AN.AO) == 0 && var2.AQ == 2) {
                  var2.AY = false;
                  break;
               }
            }
         }

         this.AN.AS = false;
         this.AA(0, (byte)0);
         if (GameCanvas.isKeyPressed() && this.AN != null) {
            if (this.AN.AT != null) {
               if (!GameCanvas.isTouch) {
                  super.DB = this.AN.AT.AG;
               }

               this.AN.AT.AA(true);
               super.center = this.AT;
            } else {
               super.DB = null;
               super.center = null;
            }
         }

         if (this.AN.AO.compareTo(T.RH) == 0) {
            Player.QR = "";
         }
      }

   }

   public final void AF() {
      this.AQ = new ListNew(this.AA, this.AB, this.AH, this.AJ + (this.AE << 1), 0, 0, (this.AM.size() + 1) * this.AL / 2 - this.AH, true);
   }
}
