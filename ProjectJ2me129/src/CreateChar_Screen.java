public final class CreateChar_Screen extends MainScreen {
   public static CreateChar_Screen instance;
   private TField AB;
   private iCommand AC;
   private iCommand AD;
   private int AE;
   private Class_ET AF;
   private mVector AG = new mVector("CreateChar_Screen.vecHeadShow");
   private int AH = 30;
   private static short[][] AI = new short[][]{{1, 41}, {-1, 2, 42}, {3, 43}, {44, 4}, {45, 24}, {-1, 46, 25}, {47, 26}, {48, 27}, {49, 28}, {-1, 29, 50}, {30, 51}, {31, 52}, {32, 53}, {-1, 33, 58}, {34, 55}, {35, 56}, {36, 57}, {-1, 37}, {38, 59}, {39, 60}};

   public static CreateChar_Screen AB() {
      return instance == null ? (instance = new CreateChar_Screen()) : instance;
   }

   public final void Show(MainScreen var1) {
      super.Show(var1);
      this.AF = new Class_ET((short)0, (byte)0, "", MotherCanvas.hw, this.AE - 10);
      this.AF.clazz = (byte)CRes.random(1, 6);
      this.AF = AA(this.AF, false);
      this.AB.AB("");
   }

   public final void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public CreateChar_Screen() {
      this.AE = MotherCanvas.h - 55;
      int var10000 = MotherCanvas.hw;
      this.AB = new TField(MotherCanvas.hw - 40, this.AE, 80);
      this.AB.AA(T.EZ);
      this.AC = new iCommand(T.AR, 0, this);
      this.AD = new iCommand(T.AG, 1, this);
      if (!GameCanvas.isTouch) {
         this.AD = AvMain.AA(this.AD, 1);
         this.AC = AvMain.AA(this.AC, 2);
         super.center = this.AC;
         this.AC = AvMain.AA(this.AC, 0);
         super.DB = this.AB.AG;
         super.DA = this.AD;
      } else {
         this.AD = AvMain.AA(this.AD, 2);
         this.AC = AvMain.AA(this.AC, 1);
         super.DA = this.AC;
         super.DB = this.AD;
         super.backCMD = this.AD;
         super.DF = this.AC;
      }

      if (GameCanvas.isKeyPressed()) {
         this.AB.AA(true);
      }

      if (this.AG.size() == 0) {
         for(int var1 = 1; var1 < 6; ++var1) {
            this.AF = new Class_ET((short)0, (byte)0, "", 0, 0);
            this.AF.clazz = (byte)var1;
            this.AF = AA(this.AF, false);
            this.AG.addElement(this.AF);
         }
      }

   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (this.AB.getText().length() > 0) {
            GlobalService.getInstance().Create_Char(this.AB.getText(), this.AF.clazz, this.AF.BS, this.AF.BW);
         } else {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.CZ);
         }
         break;
      case 1:
         if (super.KP != null) {
            super.KP.Show(super.KP.KP);
         } else {
            GameCanvas.loginScr.Show();
         }
         break;
      case 2:
         ++this.AF.clazz;
         if (this.AF.clazz > 5) {
            this.AF.clazz = 1;
         }

         this.AF = AA(this.AF, false);
      }

      super.commandPointer(var1, var2);
   }

   public final void paint(mGraphics var1) {
      if (GameCanvas.mapBack != null) {
         GameCanvas.mapBack.AC(var1);
         GameCanvas.mapBack.AE(var1);
         GameCanvas.mapBack.AD(var1);
      }

      for(int var2 = 0; var2 < this.AG.size(); ++var2) {
         Class_ET var3 = (Class_ET)this.AG.elementAt(var2);
         byte var4 = 0;
         if (var3.clazz == this.AF.clazz) {
            var4 = 1;
         }

         if (AvMain.FY == null) {
            AvMain.FY = new FrameImage(mImage.createImage("/interface/border.png"), 26, 26);
         } else {
            AvMain.FY.drawFrame(var4, this.AH / 2, this.AH / 2 + var2 * this.AH + GameScreen.h12plus, 0, 3, var1);
         }

         MainObject.AA(var1, var3.BS, var3.BW, var3.BX, this.AH / 2 + 2, this.AH / 2 + var2 * this.AH + 37 + GameScreen.h12plus, 2);
      }

      GameCanvas.resetTrans(var1);
      this.AB.paint(var1);
      this.AF.AC(var1, this.AF.x);
      this.AF.AA(var1, this.AF.x, this.AF.y, true);
      if (this.AF.LS != null) {
         this.AF.LS.paint(var1);
      }

      super.paint(var1);
   }

   public final void update() {
      this.AB.update();
      this.AF.AF((int)10);
      if (GameCanvas.mapBack != null) {
         GameCanvas.mapBack.AD();
      }

   }

   public final void handleKeyPress() {
      int var1 = this.AF.clazz;
      super.handleKeyPress();
      if (GameCanvas.AL[8]) {
         GameCanvas.AB(8);
         ++var1;
      } else if (GameCanvas.AL[2]) {
         GameCanvas.AB(2);
         --var1;
      }

      if (var1 != this.AF.clazz) {
         if (var1 > 5) {
            this.AF.clazz = 1;
         } else if (var1 <= 0) {
            this.AF.clazz = 5;
         } else {
            this.AF.clazz = (byte)var1;
         }

         this.AF = AA(this.AF, false);
      }

   }

   public final void updatePointer() {
      this.AB.updatePointer();
      if (GameCanvas.AB(0, 0 + GameScreen.h12plus, this.AH, this.AH * 5)) {
         int var1;
         if ((var1 = (GameCanvas.AZ - GameScreen.h12plus) / this.AH) > 4) {
            var1 = 4;
         }

         this.AF.clazz = (byte)(var1 + 1);
         this.AF = AA(this.AF, false);
         GameCanvas.isPointerSelect = false;
      }

      super.updatePointer();
   }

   public final void AA(int var1) {
      this.AB.AD(var1);
   }

   public static Class_ET AA(Class_ET var0, boolean var1) {
      switch(var0.clazz) {
      case 1:
         var0.BS = 0;
         if (var1) {
            var0.BW = AB(0, 0);
         } else {
            var0.BW = 1;
         }

         if (var1) {
            var0.BX = AB(0, 1);
         } else {
            var0.BX = -1;
         }

         if (var1) {
            var0.BT = AB(0, 2);
         } else {
            var0.BT = 3;
         }

         if (var1) {
            var0.BU = AB(0, 3);
         } else {
            var0.BU = 4;
         }

         var0.BV = -1;
         var0.BL = 33;
         break;
      case 2:
         var0.BS = 0;
         if (var1) {
            var0.BW = AB(1, 0);
         } else {
            var0.BW = 24;
         }

         if (var1) {
            var0.BX = AB(1, 1);
         } else {
            var0.BX = -1;
         }

         if (var1) {
            var0.BT = AB(1, 2);
         } else {
            var0.BT = 26;
         }

         if (var1) {
            var0.BU = AB(1, 3);
         } else {
            var0.BU = 27;
         }

         var0.BV = 5;
         var0.BL = 15;
         break;
      case 3:
         var0.BS = 0;
         if (var1) {
            var0.BW = AB(2, 0);
         } else {
            var0.BW = 28;
         }

         if (var1) {
            var0.BX = AB(2, 1);
         } else {
            var0.BX = -1;
         }

         if (var1) {
            var0.BT = AB(2, 2);
         } else {
            var0.BT = 30;
         }

         if (var1) {
            var0.BU = AB(2, 3);
         } else {
            var0.BU = 31;
         }

         var0.BV = 180;
         var0.BL = 44;
         break;
      case 4:
         var0.BS = 0;
         if (var1) {
            var0.BW = AB(3, 0);
         } else {
            var0.BW = 32;
         }

         if (var1) {
            var0.BX = AB(3, 1);
         } else {
            var0.BX = -1;
         }

         if (var1) {
            var0.BT = AB(3, 2);
         } else {
            var0.BT = 34;
         }

         if (var1) {
            var0.BU = AB(3, 3);
         } else {
            var0.BU = 35;
         }

         var0.BV = 6;
         var0.BL = 51;
         break;
      case 5:
         var0.BS = 0;
         var0.BW = 36;
         var0.BX = -1;
         var0.BT = 38;
         var0.BU = 39;
         var0.BV = 7;
         var0.BL = 7;
         break;
      case 6:
         var0.BS = 8;
         var0.BW = -1;
         var0.BX = 11;
         var0.BT = 9;
         var0.BU = 10;
         var0.BV = -1;
         var0.BL = 7;
      }

      if (var0.clazz < T.WG.length) {
         var0.BC = T.WG[var0.clazz];
      }

      return var0;
   }

   private static short AB(int var0, int var1) {
      return AI[(var0 << 2) + var1][CRes.random(AI[var0 << 2].length)];
   }
}
