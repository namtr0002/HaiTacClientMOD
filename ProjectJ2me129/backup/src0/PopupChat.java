public final class PopupChat extends AvMain {
   private int AC;
   private int AD;
   private int AE;
   private int AF;
   public int AA;
   private int AG;
   private int AH;
   private short AI;
   private String[] AJ;
   public static mImage[] mPopup = new mImage[2];
   private boolean AK;
   private byte AL;
   private MainItem AM;
   private int[] AN;

   public PopupChat() {
      this.AC = MotherCanvas.hw;
      this.AD = MotherCanvas.hh;
      this.AH = 0;
      this.AK = true;
      this.AL = 0;
      this.AN = new int[]{-13427660, -16788};
   }

   public final void AA(String var1, boolean var2) {
      this.AK = var2;
      this.AL = 0;
      var1 = var1.trim();
      this.AA = mFont.tahoma_7_black.getWidth(var1);
      if (this.AA > 100) {
         this.AA = 100;
      } else if (this.AA < 20) {
         this.AA = 20;
      }

      this.AE = 8;
      this.AJ = mFont.tahoma_7_black.splitFontArray(var1, this.AA);
      this.AF = this.AJ.length * GameCanvas.hText;
      if (this.AJ.length <= 2) {
         this.AG = 100;
      } else {
         this.AG = 160;
      }
   }

   public final void AA(short var1) {
      this.AK = true;
      this.AL = 1;
      this.AA = 22;
      this.AF = 22;
      this.AE = 8;
      this.AI = var1;
      this.AG = 200;
   }

   public final void AA(short var1, byte var2, short var3) {
      this.AK = true;
      this.AL = 2;
      this.AA = 22;
      this.AF = 22;
      this.AE = 8;
      this.AM = new MainItem(var2, var1, var1);
      this.AM.numPotion = var3;
      this.AG = 100;
   }

   public final void paint(mGraphics var1) {
      if (this.AE > 0) {
         this.AE -= 2;
      }

      mGraphics var2 = var1;
      PopupChat var8 = this;

      try {
         int var3 = var8.AD - var8.AF + var8.AE;
         int var4 = var8.AC - var8.AA / 2;
         var2.setColor(var8.AN[0]);
         var2.fillRect(var4 - 3, var3, var8.AA + 6, var8.AF);
         var2.fillRect(var4, var3 - 3, var8.AA, var8.AF + 6);
         var2.setColor(var8.AN[1]);
         var2.fillRect(var4, var3 - 2, var8.AA, var8.AF + 4);
         var2.fillRect(var4 - 2, var3, var8.AA + 4, var8.AF);
         var2.drawRegion(mPopup[0], 0, 0, 3, 3, 0, var4 - 3, var3 - 3, 0);
         var2.drawRegion(mPopup[0], 0, 3, 3, 3, 0, var4 + var8.AA, var3 - 3, 0);
         var2.drawRegion(mPopup[0], 0, 9, 3, 3, 0, var4 - 3, var3 + var8.AF, 0);
         var2.drawRegion(mPopup[0], 0, 6, 3, 3, 0, var4 + var8.AA, var3 + var8.AF, 0);
         var2.drawRegion((mImage)mPopup[1], var4 + var8.AA / 2 - 3, var3 + var8.AF + 2, 0);
         if (var8.AL != 0) {
            if (var8.AL == 1) {
               MainImage var5;
               if ((var5 = ObjectData.getImageAll(var8.AI, ObjectData.hashImageItemOther, (short)9000)).img != null && var5.frame == -1) {
                  boolean var6 = true;
                  if (var5.img != null) {
                     var5.AC = (short)mImage.getImageHeight(var5.img.image);
                     var5.AB = (short)mImage.getImageWidth(var5.img.image);
                     var5.frame = (short)(var5.AC / 24);
                  }
               }

               int var10;
               if ((var10 = var5.frame + 1) == 0) {
                  var10 = 1;
               }

               if ((var10 = GameCanvas.gameTick / 8 % var10) >= var5.frame) {
                  var10 = var5.frame - 1;
               }

               var2.drawRegion(var5.img, 0, var10 * 24, 24, 24, 0, var4 + var8.AA / 2, var3 + var8.AF / 2, 3);
               return;
            }

            if (var8.AL == 2 && var8.AM != null) {
               var8.AM.AA(var2, var4 + var8.AA / 2, var3 + var8.AF / 2, 22, 0, (byte)0);
               return;
            }
         } else if (var8.AJ != null) {
            for(int var9 = 0; var9 < var8.AJ.length; ++var9) {
               mFont.tahoma_7_black.drawString(var2, var8.AJ[var9], var4 + var8.AA / 2, var3 + 1 + var9 * GameCanvas.hText, 2);
            }

            return;
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public final void AB(int var1, int var2) {
      this.AC = var1;
      this.AD = var2;
   }

   public final boolean AA() {
      --this.AG;
      return this.AG <= 0 && this.AK;
   }
}
