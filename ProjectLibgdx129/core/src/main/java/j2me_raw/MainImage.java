public final class MainImage {
   public mImage img;
   public short AB;
   public short AC;
   public short frame = -1;
   public long AE = -1L;
   public int AF = 0;

   public MainImage() {
   }

   public MainImage(mImage var1) {
      this.img = var1;
      this.AE = 0L;
      this.AB = (short)mImage.getImageWidth(var1.image);
      this.AC = (short)mImage.getImageHeight(var1.image);
   }

   public final void AA() {
      if (this.img != null) {
         this.AB = (short)mImage.getImageWidth(this.img.image);
         this.AC = (short)mImage.getImageHeight(this.img.image);
      }

   }

   public final void set_Frame() {
      if (this.img != null) {
         this.AC = (short)mImage.getImageHeight(this.img.image);
         this.AB = (short)mImage.getImageWidth(this.img.image);
         this.frame = this.AB > 0 ? (short)(this.AC / this.AB) : 1;
      }

   }
}
