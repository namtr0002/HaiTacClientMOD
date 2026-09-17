public class MainScreen extends AvMain {
   public MainScreen KP;
   public static Camera cameraMain;
   public static Camera cameraSub;

   public void Show() {
      GameCanvas.AG();
      GameCanvas.menuCur.isShowMenu = false;
      GameCanvas.end_Dialog();
      GameCanvas.currentScreen = this;
      this.AM();
      this.setxyPlus12();
   }

   public void Show(MainScreen var1) {
      if (var1 != null) {
         this.KP = var1;
      }

      GameCanvas.AG();
      GameCanvas.currentScreen = this;
      GameCanvas.end_Dialog();
      this.AM();
      this.setxyPlus12();
   }

   public void setxyPlus12() {
      GameCanvas.xPlus12 = 2;
      GameCanvas.yPlus12 = 2;
   }

   public final void AM() {
      if (super.DA != null) {
         super.DA.timeSelect = 0;
      }

      if (super.DB != null) {
         super.DB.timeSelect = 0;
      }

      if (super.center != null) {
         super.center.timeSelect = 0;
      }

   }

   public void paint(mGraphics var1) {
      super.paint(var1);
   }

   public void update() {
   }

   public void AA(int var1) {
   }

   public static void AC(byte var0) {
      if (GameCanvas.currentScreen == GameCanvas.tabAllScr) {
         GameCanvas.tabAllScr.typeCurrent = var0;
      }

      if (GameCanvas.currentScreen == GameCanvas.tabShopScr) {
         GameCanvas.tabShopScr.typeCurrent = var0;
      }

      if (GameCanvas.currentScreen == GameCanvas.CJ) {
         GameCanvas.CJ.typeCurrent = var0;
      }

   }

   public static boolean AD(byte var0) {
      if (GameCanvas.currentScreen == GameCanvas.tabAllScr && GameCanvas.tabAllScr.typeCurrent == 1) {
         return true;
      } else if (GameCanvas.currentScreen == GameCanvas.tabShopScr && GameCanvas.tabShopScr.typeCurrent == 1) {
         return true;
      } else {
         return GameCanvas.currentScreen == GameCanvas.CJ && GameCanvas.CJ.typeCurrent == 1;
      }
   }
}
