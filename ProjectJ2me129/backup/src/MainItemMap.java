public class MainItemMap extends MainObject {
   public byte TypeItem;
   public byte AC;
   public short IDItem;
   public short IDImage;
   public int[][] AF;
   public boolean AG = true;

   public MainItemMap() {
   }

   public MainItemMap(short var1) {
      this.IDItem = var1;
      this.AF = mSystem.new_M_Int(0, 2);
      this.IDImage = -1;
      this.AC = -1;
      super.dx = 0;
      super.dy = 0;
      this.AG = false;
   }

   public MainItemMap(short var1, short var2, int var3, int var4, int[][] var5, byte var6) {
      this.IDItem = var1;
      this.IDImage = var2;
      super.dx = var3;
      super.dy = var4;
      this.AF = var5;
      this.AC = var6;
      this.AG = true;
   }

   public void paint(mGraphics var1) {
   }

   public void update() {
   }

   public boolean AB() {
      MainImage var1;
      if ((super.hOne == 0 || super.wOne == 0) && this.TypeItem != 1 && (var1 = ObjectData.getImageAll(this.IDImage, ObjectData.HashImageItemMap, (short)0)).img != null) {
         super.wOne = mImage.getImageWidth(var1.img.image);
         super.hOne = mImage.getImageHeight(var1.img.image);
      }

      return super.x + super.dx + super.wOne >= MainScreen.cameraMain.xCam && super.x + super.dx - super.wOne <= MainScreen.cameraMain.xCam + MotherCanvas.w && super.y + super.dy + super.hOne >= MainScreen.cameraMain.yCam && super.y + super.dy - super.hOne <= MainScreen.cameraMain.yCam + MotherCanvas.h;
   }
}
