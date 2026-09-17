public final class PvPScreen extends MainScreen {
   private int AF = 180;
   private int AG = 190;
   private int AH;
   private int AI;
   private int AJ;
   private int AK;
   private int AL;
   private int AM;
   public MainObject AA;
   private String[] AN;
   private iCommand AO;
   private iCommand AP;
   private iCommand AQ;
   private iCommand AR;
   private mVector AS = new mVector();
   public boolean AB = false;
   public boolean AC = false;
   private boolean AT = false;
   private long AU = 0L;
   private short AV = 0;
   public short AD;
   public static PvPScreen instance;

   public PvPScreen() {
      this.AH = MotherCanvas.hw - this.AF / 2;
      this.AI = MotherCanvas.hh - this.AG / 2 + GameCanvas.hCommand / 2;
      this.AJ = this.AI + 78;
      this.AO = new iCommand(T.KI, 0, this);
      this.AP = new iCommand(T.CD, 1, this);
      this.AQ = new iCommand(T.KJ, 2, this);
      this.AR = new iCommand(T.KK, 3, this);
      if (AvMain.imgPvpObjdef == null || AvMain.imgPvpVs == null || AvMain.imgPvpOk == null) {
         LoadImageStatic.LoadImgPvP();
      }

   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         GlobalService.getInstance().AG((byte)1);
         return;
      case 1:
         GlobalService.getInstance().AG((byte)5);
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         }
         break;
      case 2:
         GlobalService.getInstance().AG((byte)4);
         return;
      case 3:
         GlobalService.getInstance().AG((byte)2);
      }

   }

   public final void AA(byte var1) {
      this.AT = false;
      this.AS.removeAllElements();
      switch(var1) {
      case 0:
         this.AL = 0;
         this.AM = 0;
         this.AV = 0;
         this.AB = false;
         this.AC = false;
         this.AA = null;
         this.AS.addElement(this.AO);
         this.AS.addElement(this.AP);
         this.AA(T.KL + " " + this.AD + T.PM);
         if (instance != null) {
            instance.Show((MainScreen)GameCanvas.gameScr);
         }
         break;
      case 1:
         this.AT = true;
         this.AB = false;
         this.AC = false;
         this.AM = 5;
         this.AA(T.KM);
         this.AS.addElement(this.AR);
         break;
      case 2:
         this.AB = false;
         this.AC = false;
         this.AM = -5;
         this.AA(T.KO);
         this.AS.addElement(this.AO);
         this.AS.addElement(this.AP);
         this.AV = 0;
         break;
      case 3:
         this.AU = GameCanvas.timeNow;
         this.AV = 30;
         this.AA(T.KN);
         this.AS.addElement(this.AQ);
         this.AS.addElement(this.AP);
         break;
      case 4:
         if (this.AB && this.AC) {
            this.AV = 0;
            this.AA(T.KT);
         } else if (this.AB) {
            this.AV = 0;
            this.AA(T.KR);
         } else if (this.AC) {
            this.AS.addElement(this.AQ);
            this.AS.addElement(this.AP);
            this.AA(T.KS);
         }
      case 5:
      default:
         break;
      case 6:
         this.AB = false;
         this.AC = false;
         this.AA = null;
         this.AM = -5;
         this.AS.addElement(this.AO);
         this.AS.addElement(this.AP);
         this.AV = 0;
      }

      this.AK = 0;
      iCommand var2;
      if (this.AS.size() == 1) {
         (var2 = (iCommand)this.AS.elementAt(0)).setPos(MotherCanvas.hw, this.AI + this.AG - iCommand.hButtonCmdNor, (FrameImage)null, var2.caption);
         if (!GameCanvas.isTouch) {
            var2.AG = true;
         }

         super.DF = var2;
      }

      if (this.AS.size() == 2) {
         (var2 = (iCommand)this.AS.elementAt(0)).setPos(MotherCanvas.hw - iCommand.wButtonCmd / 2, this.AI + this.AG - iCommand.hButtonCmdNor, (FrameImage)null, var2.caption);
         if (!GameCanvas.isTouch) {
            var2.AG = true;
         }

         super.DF = var2;
         (var2 = (iCommand)this.AS.elementAt(1)).setPos(MotherCanvas.hw + iCommand.wButtonCmd / 2, this.AI + this.AG - iCommand.hButtonCmdNor, (FrameImage)null, var2.caption);
         if (!GameCanvas.isTouch) {
            var2.AG = false;
         }

         super.backCMD = var2;
      }

   }

   public final void AA(String var1) {
      this.AN = mFont.tahoma_7_black.splitFontArray(var1, this.AF - 10);
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      AvMain.AB(var1, this.AH - 12, this.AI - 20, this.AF + 24, this.AG + 25, 2);
      GameScreen.player.AA(var1, this.AH + this.AF / 2 - this.AL, this.AJ, true);
      mFont.tahoma_7_black.drawString(var1, GameScreen.player.name, this.AH + this.AF / 2 - this.AL, this.AJ - 60, 2);
      if (this.AA != null) {
         this.AA.AA(var1, this.AH + this.AF / 2 + this.AL, this.AJ, true);
         mFont.tahoma_7_black.drawString(var1, this.AA.name, this.AH + this.AF / 2 + this.AL, this.AJ - 60, 2);
      } else if (this.AL == this.AF / 4 || this.AM != 0) {
         var1.drawRegion((mImage)AvMain.imgPvpObjdef, this.AH + this.AF / 2 + this.AL, this.AJ + 2, 33);
      }

      if (this.AL > 12) {
         var1.drawRegion((mImage)AvMain.imgPvpVs, this.AH + this.AF / 2, this.AJ - 24, 3);
      }

      if (this.AB) {
         var1.drawRegion((mImage)AvMain.imgPvpOk, this.AH + this.AF / 2 - this.AL - 20, this.AJ, 33);
      }

      if (this.AC) {
         if (AvMain.imgPvpOk == null) {
            LoadImageStatic.LoadImgPvP();
         } else {
            var1.drawRegion((mImage)AvMain.imgPvpOk, this.AH + this.AF / 2 + this.AL + 20, this.AJ, 33);
         }
      }

      int var2 = this.AJ + GameCanvas.hText / 2;
      mFont.tahoma_7_black.drawString(var1, T.KP + GameScreen.player.KQ, this.AH + 10, var2, 0);
      var2 += GameCanvas.hText;
      mFont.tahoma_7_black.drawString(var1, T.KQ + GameScreen.player.BP[0] + "/" + GameScreen.player.BP[1], this.AH + 10, var2, 0);
      var2 += GameCanvas.hText;
      int var3;
      if (this.AN != null) {
         for(var3 = 0; var3 < this.AN.length; ++var3) {
            String var4 = this.AN[var3];
            if (this.AT) {
               if (GameCanvas.gameTick % 20 < 5) {
                  var4 = var4 + ".";
               } else if (GameCanvas.gameTick % 20 < 10) {
                  var4 = var4 + "..";
               } else if (GameCanvas.gameTick % 20 < 15) {
                  var4 = var4 + "...";
               }
            }

            if (this.AV > 0 && var3 == this.AN.length - 1) {
               var4 = var4 + " " + this.AV + "s.";
            }

            mFont.tahoma_7_black.drawString(var1, var4, this.AH + this.AF / 2, var2, 2);
            var2 += GameCanvas.hText;
         }
      }

      Interface_Game.AA(var1, MotherCanvas.hw - Interface_Game.BW / 2, 0 + GameScreen.h12plus, false, 1);

      for(var3 = 0; var3 < this.AS.size(); ++var3) {
         iCommand var5;
         (var5 = (iCommand)this.AS.elementAt(var3)).paint(var1, var5.xCmd, var5.yCmd);
      }

   }

   public final void update() {
      if (super.KP != null) {
         super.KP.update();
      }

      if (this.AM > 0) {
         if (this.AL < this.AF / 4) {
            this.AL += this.AM;
         } else {
            this.AM = 0;
            this.AL = this.AF / 4;
         }
      } else if (this.AM < 0) {
         if (this.AL > 0) {
            this.AL += this.AM;
         } else {
            this.AL = 0;
            this.AM = 0;
         }
      }

      if (this.AV > 0 && GameCanvas.timeNow - this.AU > 1000L) {
         --this.AV;
         this.AU += 1000L;
      }

   }

   public final void handleKeyPress() {
      int var1 = this.AS.size();
      if (!GameCanvas.isTouch && var1 > 0) {
         int var2 = this.AK;
         if (GameCanvas.isKeyPressed(0)) {
            --this.AK;
            GameCanvas.clearKeyPressed(0);
         } else if (GameCanvas.isKeyPressed(2)) {
            ++this.AK;
            GameCanvas.clearKeyPressed(2);
         }

         this.AK = AvMain.AA(this.AK, var1 - 1, false);
         if (var2 != this.AK && GameCanvas.isKeyPressed()) {
            for(var2 = 0; var2 < var1; ++var2) {
               iCommand var3 = (iCommand)this.AS.elementAt(var2);
               if (var2 == this.AK) {
                  var3.AG = true;
               } else {
                  var3.AG = false;
               }
            }
         }
      }

      if (GameCanvas.AL[5] && this.AK < this.AS.size()) {
         ((iCommand)this.AS.elementAt(this.AK)).AD();
         GameCanvas.AB(5);
      }

      this.AS();
   }

   public final void updatePointer() {
      for(int var1 = 0; var1 < this.AS.size(); ++var1) {
         ((iCommand)this.AS.elementAt(var1)).AE();
      }

      super.updatePointer();
   }
}
