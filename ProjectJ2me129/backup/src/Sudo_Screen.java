public final class Sudo_Screen extends ChatTabScreen {
   private static iCommand AP;
   private MainSudo AQ;
   private Class_GU AR;
   private Class_GW AS;
   private Sudo_Info AT;
   private Class_FI AU;
   private mVector AV = new mVector();

   public Sudo_Screen(MainSudo var1) {
      this.AQ = var1;
      super.AC = 240;
      super.AD = 220;
      super.AO = 0;
      if (super.AC > MotherCanvas.w) {
         super.AC = MotherCanvas.w;
      }

      if (super.AD > MotherCanvas.h - GameCanvas.hCommand - 10) {
         super.AD = MotherCanvas.h - GameCanvas.hCommand - 10;
      }

      super.AA = MotherCanvas.hw - super.AC / 2;
      if (GameCanvas.isTouch) {
         super.AB = MotherCanvas.hh - super.AD / 2;
      } else {
         super.AB = MotherCanvas.hh - super.AD / 2 - GameCanvas.hCommand / 2;
      }

      super.AJ = 24;
      super.AF = super.AA + super.AJ + super.AE;
      super.AG = super.AB + super.AJ + super.AE;
      super.AI = super.AD - super.AJ - super.AE - (super.AE << 1);
      super.AH = super.AC - (super.AJ << 1) - (super.AE << 1);
      super.AL = (super.AH + (super.AE << 1)) / 4;
      super.AH = (super.AL << 2) - (super.AE << 1);
      super.AK = super.AI / GameCanvas.hText + 2;
      this.AR = new Class_GU(T.AD, (byte)3);
      this.AR.AA(super.AF, super.AG, super.AH, super.AI, super.AE, super.AK);
      this.AS = new Class_GW(T.anhem, (byte)4);
      this.AS.AA(super.AF, super.AG, super.AH, super.AI, super.AE, super.AK);
      super.AM.removeAllElements();
      super.AM.addElement(this.AS);
      this.AT = new Sudo_Info(T.AZ, (byte)5, GameScreen.player.PJ);
      this.AT.AA(super.AF, super.AG, super.AH, super.AI, super.AE, super.AK);
      super.AM.addElement(this.AT);
      this.AU = new Class_FI(T.AZ, (byte)5);
      this.AU.AA(super.AF, super.AG, super.AH, super.AI, super.AE, super.AK);
      int var10000 = super.AA;
      var10000 = super.AC;
      super.AM.size();
      var10000 = super.AL;
      super.AM.size();
      var10000 = super.AL;
      super.AO = 0;
      AP = new iCommand(T.close, 5, this);
      if (GameCanvas.isTouch) {
         AP.setPos(super.AA + super.AC - 13, super.AB + 13, MainTab.fraCloseTab, "");
      } else {
         AP = AvMain.AA(AP, 2);
         super.DB = AP;
      }

      this.AA((iCommand)null, (byte)-1);
      if (this.AQ != null) {
         this.AS.AN = this.AQ.AB;
         this.AT.AB = this.AQ;
         this.AR.AN = this.AQ.AC;
         this.AU.AN = this.AQ.AD;
      }

      super.backCMD = AP;
      GlobalService.getInstance().AQ((byte)2);
      GlobalService.getInstance().AQ((byte)3);
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 5:
         GameCanvas.gameScr.Show();
      default:
      }
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      MainTab.AA(var1, super.AA, super.AB, super.AC, super.AD);
      AvMain.paintRect(var1, super.AF - super.AE, super.AG - super.AE, super.AH + (super.AE << 1) - 1, super.AI + (super.AE << 1), (byte)0, 4);
      var1.AD(super.AA + super.AJ / 2, super.AB, super.AC - super.AJ, super.AJ + (super.AE << 1));
      mGraphics.AC();
      int var10000 = super.AA;
      var10000 = super.AJ;
      var10000 = super.AB;
      var10000 = super.AC;
      var10000 = super.AJ;
      var10000 = super.AJ;
      var10000 = super.AE;
      mGraphics.AD();

      int var2;
      for(var2 = 0; var2 < super.AM.size(); ++var2) {
         ChatDetail var3 = (ChatDetail)super.AM.elementAt(var2);
         byte var4 = 0;
         if (var2 == super.AO) {
            var4 = 1;
         }

         var1.drawRegion(AvMain.imgTabClan, 0, var4 * 19, 25, 19, 0, super.AF - super.AE + var2 * super.AL, super.AB + super.AJ - 18, 0);
         var1.drawRegion(AvMain.imgTabClan, 0, var4 * 19, 25, 19, 2, super.AF - super.AE + var2 * super.AL + super.AL - 25, super.AB + super.AJ - 18, 0);
         if (var2 == 1) {
            AvMain.fraIconClan.drawFrame(var4 + 8, super.AF - super.AE + var2 * super.AL + super.AL / 2, super.AB + super.AJ / 2 + 3, 0, 3, var1);
         } else {
            AvMain.fraIconClan.drawFrame((var2 + 1 << 1) + var4, super.AF - super.AE + var2 * super.AL + super.AL / 2, super.AB + super.AJ / 2 + 3, 0, 3, var1);
         }

         if (var3.AS) {
            var1.drawRegion((mImage)MainEvent.imgNew, super.AF - super.AE + var2 * super.AL + 5, super.AB + 7, 0);
         }
      }

      mGraphics.AE();
      mGraphics.restoreCanvas();
      GameCanvas.resetTrans(var1);
      if (super.AN != null) {
         GameCanvas.resetTrans(var1);
         super.AN.paint(var1);
      }

      if (!GameCanvas.AR()) {
         for(var2 = 0; var2 < this.AV.size(); ++var2) {
            iCommand var5;
            (var5 = (iCommand)this.AV.elementAt(var2)).paint(var1, var5.xCmd, var5.yCmd);
         }
      }

   }

   public final void update() {
      if (super.KP != null) {
         super.KP.update();
      }

      if (super.AN != null) {
         super.AN.update();
         if (super.AN.AS) {
            super.AN.AS = false;
         }
      }

      MainTab.BK.updateTimeCountDownTicket();
   }

   public final void handleKeyPress() {
      int var1 = super.AO;
      if (GameCanvas.AL[4]) {
         GameCanvas.AB(4);
         if (super.AO > 0) {
            --super.AO;
         }
      } else if (GameCanvas.AL[6]) {
         GameCanvas.AB(6);
         if (super.AO < super.AM.size() - 1) {
            ++super.AO;
         }
      }

      if (var1 != super.AO) {
         this.AD(super.AO);
      }

      if (super.AN != null) {
         super.AN.handleKeyPress();
      }

      this.AB();
      this.AS();
   }

   public final void updatePointer() {
      int var1;
      if (this.AV != null) {
         for(var1 = 0; var1 < this.AV.size(); ++var1) {
            ((iCommand)this.AV.elementAt(var1)).AE();
         }
      }

      if (super.AN != null) {
         super.AN.updatePointer();
      }

      if (GameCanvas.isPointerSelect && GameCanvas.isPointer(super.AF - super.AE, super.AB + super.AJ - 24, super.AH + (super.AE << 1), 28)) {
         if ((var1 = (GameCanvas.AY - (super.AF - super.AE)) / super.AL) >= 0 && var1 < super.AM.size()) {
            (new StringBuffer("click tab ")).append(var1).append(" vecTabChat.size() ").append(super.AM.size()).toString();
            super.AO = var1;
            this.AD(super.AO);
         }

         GameCanvas.isPointerSelect = false;
      }

   }

   public final void AD(int var1) {
      super.AO = var1;
      if (var1 >= 0 && var1 <= super.AM.size()) {
         super.AN = (ChatDetail)super.AM.elementAt(var1);
         this.AA((iCommand)null, (byte)-1);
         super.AN.AA();
      }

   }

   private void AA(iCommand var1, byte var2) {
      this.AV.removeAllElements();
      this.AV.addElement(AP);
   }
}
