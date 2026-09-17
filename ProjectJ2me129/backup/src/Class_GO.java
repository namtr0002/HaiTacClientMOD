public final class Class_GO extends MainScreen {
   private int AA;
   private int AB;
   private int AC;
   private int AD;
   private int AE;
   private int AF;
   private static Class_GO AG;
   private ListNew AH;
   private iCommand AI;

   public static Class_GO AF() {
      if (AG == null) {
         AG = new Class_GO();
      }

      return AG;
   }

   public Class_GO() {
      this.AA = MotherCanvas.w - 10;
      this.AB = MotherCanvas.h - 10;
      if (this.AA > 180) {
         this.AA = 180;
      }

      if (this.AB > 240) {
         this.AB = 240;
      }

      this.AC = MotherCanvas.hw - this.AA / 2;
      this.AD = MotherCanvas.hh - this.AB / 2;
      this.AE = 24;
      if (!GameCanvas.isTouch) {
         this.AE = 20;
      }

      this.AI = new iCommand(T.close, 0, this);
      if (GameCanvas.isTouch) {
         this.AI.setPos(this.AC - 14 + this.AA, this.AD + 14, MainTab.fraCloseTab2, "");
      }

      super.DB = this.AI;
      super.backCMD = this.AI;
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         GameCanvas.gameScr.Show();
      default:
      }
   }

   public final void AG() {
      if (this.AB > (Player.AX.size() + 2) * this.AE + this.AE / 4) {
         this.AB = (Player.AX.size() + 2) * this.AE + this.AE / 4;
      }

      this.AH = new ListNew(this.AC, this.AD + (GameCanvas.hCommand << 1), this.AA, this.AB - (GameCanvas.hCommand << 1), this.AE, Player.AX.size(), Player.AX.size() * this.AE - (this.AB - (GameCanvas.hCommand << 1)), true);
   }

   public final void paint(mGraphics var1) {
      if (super.KP != null) {
         super.KP.paint(var1);
      }

      GameCanvas.resetTrans(var1);
      AvMain.AE(var1, this.AC, this.AD, this.AA, this.AB);
      int var2 = this.AD + this.AE;
      int var3 = 25;
      if (!GameCanvas.lowGraphic) {
         var3 = AvMain.fraThongThao.frameWidth / 2;
         AvMain.fraBanhLai.drawFrame(0, this.AC + this.AA / 2 - var3 - 12, var2, 0, 3, var1);
         AvMain.fraBanhLai.drawFrame(1, this.AC + this.AA / 2 + var3 + 12, var2, 0, 3, var1);
         AvMain.fraThongThao.drawFrame(GameCanvas.language == 0 ? 0 : 1, this.AC + this.AA / 2, var2, 0, 3, var1);
      } else {
         mFont.tahoma_7b_black.drawString(var1, T.thongthao, this.AC + this.AA / 2, var2, 2);
      }

      mFont.tahoma_7_yellow.drawString(var1, String.valueOf(GameScreen.player.LvThongThao), this.AC + this.AA / 2 - var3 - 12 + 1, var2 - 5, 2);
      mFont.tahoma_7_white.drawString(var1, String.valueOf(Player.pointMaxLevelAttri), this.AC + this.AA / 2 + var3 + 12 + 1, var2 - 5, 2);
      var2 += this.AE + this.AE / 4;
      if (Player.AX != null) {
         var1.AD(this.AC, var2 - this.AE / 4, this.AA, this.AB - (GameCanvas.hCommand << 1) - this.AE / 4);
         mGraphics.AC();
         int var10000 = this.AC;
         var10000 = this.AE;
         var10000 = this.AA;
         var10000 = this.AB;
         var10000 = GameCanvas.hCommand;
         var10000 = this.AE;
         mGraphics.AD();
         if (this.AH.AD != 0) {
            var1.translate(0, -this.AH.AC);
         }

         if ((var3 = this.AH.AC / this.AE - 2) < 0) {
            var3 = 0;
         }

         int var4;
         if ((var4 = this.AB / this.AE + 1 + var3) >= Player.AX.size()) {
            var4 = Player.AX.size();
         }

         for(var3 = var3; var3 < var4; ++var3) {
            MaxLevelAttribute var5 = (MaxLevelAttribute)Player.AX.elementAt(var3);
            mFont.tahoma_7b_black.drawString(var1, MainItem.BZ[var5.AC].AF + ": ", this.AC + 80, var2 - 4, 1);
            boolean var6 = false;
            if (var3 == this.AF && GameCanvas.isKeyPressed()) {
               var6 = true;
            }

            Interface_Game.paintHP_Thong_Thao(var1, this.AC + 80, var2 - 4, this.AA - 90, var5.AA, var5.AB, var6);
            var2 += this.AE;
         }

         mGraphics.AE();
         mGraphics.restoreCanvas();
      }

      GameCanvas.resetTrans(var1);
      if (!GameCanvas.lowGraphic && AvMain.FK != null) {
         var1.drawRegion((mImage)AvMain.FK[8], this.AC + this.AA - 36, this.AD + this.AB - 36, 0);
      }

      if (GameCanvas.isTouch) {
         this.AI.paint(var1, this.AI.xCmd, this.AI.yCmd);
      } else {
         super.AD(var1);
      }
   }

   public final void update() {
      if (this.AH.AD > 0) {
         this.AH.AC();
      }

      if (super.KP != null) {
         super.KP.update();
      }

   }

   public final void handleKeyPress() {
      boolean var1 = false;
      if (GameCanvas.isKeyPressed(1)) {
         --this.AF;
         GameCanvas.clearKeyPressed(1);
         var1 = true;
      } else if (GameCanvas.isKeyPressed(3)) {
         ++this.AF;
         GameCanvas.clearKeyPressed(3);
         var1 = true;
      } else if (GameCanvas.AL[5]) {
         GameCanvas.AB(5);
         AD(this.AF);
      }

      if (var1) {
         this.AF = AvMain.AA(this.AF, Player.AX.size() - 1, false);
         if (this.AH.AD > 0) {
            this.AH.AA(this.AF * this.AE - this.AH.maxH / 2);
         }
      }

      super.handleKeyPress();
   }

   public final void updatePointer() {
      if (this.AH.AD > 0) {
         this.AH.update_Pos_UP_DOWN();
      }

      super.updatePointer();
      if (GameCanvas.isPointerSelect) {
         int var1 = this.AD + (this.AE << 1) - this.AE / 2;
         if (GameCanvas.isPoint(this.AC, this.AD, this.AA, this.AB)) {
            if ((var1 = (GameCanvas.AZ - (var1 - this.AH.AC)) / this.AE) >= 0 && var1 < Player.AX.size()) {
               this.AF = var1;
               if (Player.pointMaxLevelAttri > 0) {
                  AD(this.AF);
               }
            }

            GameCanvas.isPointerSelect = false;
         }
      }

   }

   private static void AD(int var0) {
      if (var0 >= 0 && var0 < Player.AX.size()) {
         MaxLevelAttribute var1 = (MaxLevelAttribute)Player.AX.elementAt(var0);
         GlobalService.getInstance().AI((byte)0, (short)((short)var1.AC));
      }

   }
}
