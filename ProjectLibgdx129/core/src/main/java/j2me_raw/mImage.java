import javax.microedition.lcdui.Image;

public final class mImage {
   public Image image;
   public int width;
   public int height;

   public final void setDefault() {
      if (this.image != null) {
         this.width = getImageWidth(this.image);
         this.height = getImageHeight(this.image);
      }

   }

   public static mImage createImage(String imgpath) {
      mImage i = new mImage();
      try {
         i.image = Image.createImage("/x" + mGraphics.zoomLevel + imgpath);
      } catch (Exception var2) {
      }
      return i.image == null ? null : i;
   }

   public static mImage createImageNotZoom(String path) {
      mImage i = new mImage();
      try {
         i.image = Image.createImage(path);
      } catch (Exception e) {
      }
      return i.image == null ? null : i;
   }

   public static mImage AA(byte[] var0) {
      mImage var1 = new mImage();
      try {
         if (var0 != null) {
            var1.image = Image.createImage(var0, 0, var0.length);
            return var1;
         }
      } catch (Exception var2) {
      }

      return var1.image == null ? null : var1;
   }

   public static int getImageWidth(Image var0) {
      return var0.getWidth() / mGraphics.zoomLevel;
   }

   public static int getImageHeight(Image var0) {
      return var0.getHeight() / mGraphics.zoomLevel;
   }
   
   public final int getImageWidth() {
        return this.image != null ? getImageWidth(this.image) : 0;
    }
    public final int getImageHeight() {
        return this.image != null ? getImageHeight(this.image) : 0;
    }
    public final Image getImageObject() {
        return this.image;
    }

}
