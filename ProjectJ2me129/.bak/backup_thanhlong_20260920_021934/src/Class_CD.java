public final class Class_CD extends MainItemMap {
   private static mImage AH;
   private FrameImage AI;
   public byte AA = -1;
   private byte AJ = 0;

   public Class_CD(short var1, short var2, int var3, int var4, int[][] var5, byte var6) {
      super(var1, var2, var3, var4, var5, var6);
      super.TypeItem = 0;
   }

   public final void AB(int var1, int var2) {
      super.x = var1;
      super.y = var2;
      super.ySort = var2;
   }

   public final void paint(mGraphics var1) {
      MainImage var2 = this.AA();
      if (this.AA >= 0) {
         switch(this.AA) {
         case 0:
            if (this.AI == null) {
               this.AI = new FrameImage(391, 28, 13);
            } else {
               this.AI.drawFrame(0, super.x + super.dx + 11, super.y + super.dy + 14, 0, 3, var1);
            }
            break;
         case 1:
            if (this.AI == null) {
               this.AI = new FrameImage(391, 28, 13);
            } else if (this.AJ == 0) {
               this.AI.drawFrame(0, super.x + super.dx + 11, super.y + super.dy + 14, 0, 3, var1);
               if (GameCanvas.gameTick % 200 == 140) {
                  this.AJ = 1;
               }
            } else {
               if (GameCanvas.gameTick % 200 == 190 || GameCanvas.gameTick % 200 == 195) {
                  this.AI.drawFrame(0, super.x + super.dx + 11, super.y + super.dy + 14, 0, 3, var1);
               }

               if (GameCanvas.gameTick % 200 == 199) {
                  this.AJ = 0;
               }
            }
         }
      }

      if (!GameScreen.IZ && (super.IDImage == 259 || super.IDImage == 263)) {
         if (AH == null) {
            AH = mImage.createImage("/bg/gate.png");
         }

         var1.drawRegion((mImage)AH, super.x + super.dx, super.y + super.dy, 0);
      } else if (var2.img != null) {
         var1.drawRegion((mImage)var2.img, super.x + super.dx, super.y + super.dy, 0);
      }

      byte var10000 = this.AA;
   }

   public final MainImage AA() {
      return ObjectData.getImageAll(super.IDImage, ObjectData.HashImageItemMap, (short)0);
   }
}
