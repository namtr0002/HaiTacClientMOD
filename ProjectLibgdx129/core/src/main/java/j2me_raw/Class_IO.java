public final class Class_IO extends AvMain {
   public TField AA = new TField();
   private static Class_IO AC;
   public static boolean AB = false;

   public static Class_IO AA() {
      return AC == null ? (AC = new Class_IO()) : AC;
   }

   public final void AB() {
      if (AB = !AB) {
         this.AA.AG();
      }

   }

   public final void AC(int var1, int var2) {
      System.out.println("commandTab trong ChatTextField:" + var1);
      switch(var1) {
      case 0:
         GameCanvas.clearAll();
         this.AA.AB("");
         AB = false;
         if (!GameCanvas.isTouch) {
            this.AA.AA(true);
            return;
         }
         break;
      case 1:
         this.AF();
      }

   }

   protected Class_IO() {
      this.AA.AE = false;
      this.AA.AA(true);
      this.AA.AB = MotherCanvas.h - iCommand.hButtonCmdNor - this.AA.AD - 5;
      this.AA.AC = MotherCanvas.w - 20;
      this.AA.AA = (MotherCanvas.w - this.AA.AC) / 2;
      this.AA.AF(70);
      this.AA.AA(T.AD);
      if (!GameCanvas.isTouch) {
         super.DA = new iCommand(T.close, 0);
         super.center = new iCommand(T.AD, 1);
         super.DB = this.AA.AA();
      }

   }

   public final void AA(int var1) {
      this.AA.AD(var1);
   }

   public final void handleKeyPress() {
      this.AA.update();
      super.handleKeyPress();
   }

   public final void paint(mGraphics var1) {
      super.paint(var1);
      this.AA.paint(var1);
   }

   public final void updatePointer() {
      this.AA.updatePointer();
      super.updatePointer();
   }

   public final void AF() {
      if (this.AA.getText().length() > 0) {
         GameScreen.player.BC = this.AA.getText();
         GlobalService.getInstance().AA(this.AA.getText());
         this.AA.AB("");
      }

      if (GameCanvas.isTouch) {
         AB = false;
      }

   }
}
