public final class Class_FQ extends MainScreen {
   public String AA = "";
   public static Class_FQ AB;
   private int AE;
   private int AF;
   private int AG = 260;
   private int AH = 215;
   private static int[][] AI = new int[][]{{50, 55}, {60, 105}, {45, 155}, {90, 175}, {110, 125}, {100, 75}, {150, 45}, {210, 55}, {165, 95}, {210, 125}, {160, 155}, {210, 180}};
   private FrameImage AJ;
   private iCommand AK;
   private mVector AL = new mVector();
   public String[] AC;
   public short[] AD;

   public Class_FQ() {
      this.AE = MotherCanvas.hw - this.AG / 2;
      this.AF = MotherCanvas.hh - this.AH / 2;
      this.AK = new iCommand(T.close, -1, this);
      this.AL.addElement(this.AK);
      if (GameCanvas.isTouch) {
         this.AK.setPos(this.AE + this.AG - 13, this.AF + 13, MainTab.fraCloseTab, "");
      } else {
         super.DB = this.AK;
      }
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      AvMain.AB(var1, this.AE, this.AF, this.AG, this.AH, 0);
      AvMain.FontBorderColor(var1, this.AA, MotherCanvas.hw, this.AF - 20, 2, (int)6, (int)5);

      int var2;
      for(var2 = 1; var2 < AI.length; ++var2) {
         var1.setColor(-65536);
         var1.drawLine(this.AE + AI[var2][0], this.AF + AI[var2][1], this.AE + AI[var2 - 1][0], this.AF + AI[var2 - 1][1]);
      }

      for(var2 = 0; var2 < AI.length; ++var2) {
         if (this.AJ == null) {
            this.AJ = new FrameImage(mImage.createImage("/interface/kham_hanh_trinh.png"), 2);
         } else if (this.AD[var2] == -1) {
            this.AJ.drawFrame(1, this.AE + AI[var2][0], this.AF + AI[var2][1], 0, 3, var1);
         } else {
            this.AJ.drawFrame(0, this.AE + AI[var2][0], this.AF + AI[var2][1], 0, 3, var1);
         }

         MainImage var3;
         if (this.AD[var2] != -1 && (var3 = ObjectData.getImageAll(this.AD[var2], ObjectData.hashImagePotion, (short)2000)) != null && var3.img != null) {
            var1.drawRegion((mImage)var3.img, this.AE + AI[var2][0], this.AF + AI[var2][1], 3);
         }

         if (AI[var2][1] >= 175) {
            mFont.tahoma_7_black.drawString(var1, this.AC[var2], this.AE + AI[var2][0], this.AF + AI[var2][1] + 13, 2);
         } else {
            mFont.tahoma_7_black.drawString(var1, this.AC[var2], this.AE + AI[var2][0], this.AF + AI[var2][1] - 23, 2);
         }
      }

      for(var2 = 0; var2 < this.AL.size(); ++var2) {
         iCommand var4;
         (var4 = (iCommand)this.AL.elementAt(var2)).paint(var1, var4.xCmd, var4.yCmd);
      }

   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case -1:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
            return;
         } else {
            GameCanvas.gameScr.Show();
         }
      default:
      }
   }

   public final void updatePointer() {
      if (this.AL != null) {
         for(int var1 = 0; var1 < this.AL.size(); ++var1) {
            ((iCommand)this.AL.elementAt(var1)).AE();
         }
      }

      super.updatePointer();
   }
}
