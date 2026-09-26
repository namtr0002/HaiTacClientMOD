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
      if (imgpath == null || imgpath.length() == 0) return null;
      mImage i = new mImage();
      int zl = mGraphics.zoomLevel < 1 ? 1 : mGraphics.zoomLevel;
      String cleanPath = imgpath.startsWith("/") ? imgpath : "/" + imgpath;
      try {
         i.image = Image.createImage("/x" + zl + cleanPath);
      } catch (Exception var2) {
         try {
            i.image = Image.createImage("/x1" + cleanPath);
         } catch (Exception var3) {
            try {
               i.image = Image.createImage(cleanPath);
            } catch (Exception var4) {
               try {
                  i.image = Image.createImage(cleanPath.substring(1));
               } catch (Exception var5) {
               }
            }
         }
      }
      return i.image == null ? null : i;
   }

   public static mImage createImageNotZoom(String path) {
      if (path == null || path.length() == 0) return null;
      mImage i = new mImage();
      String cleanPath = path.startsWith("/") ? path : "/" + path;
      try {
         i.image = Image.createImage(cleanPath);
      } catch (Exception e) {
         try {
            i.image = Image.createImage(cleanPath.substring(1));
         } catch (Exception e2) {
            try {
               i.image = Image.createImage("/x1" + cleanPath);
            } catch (Exception e3) {
            }
         }
      }
      return i.image == null ? null : i;
   }

   public static mImage AA(byte[] var0) {
      if (var0 == null || var0.length == 0) return null;
      mImage var1 = new mImage();
      try {
         var1.image = Image.createImage(var0, 0, var0.length);
         return var1;
      } catch (Exception var2) {
      }

      return var1.image == null ? null : var1;
   }

   public static int getImageWidth(Image var0) {
      if (var0 == null) return 0;
      int zl = mGraphics.zoomLevel < 1 ? 1 : mGraphics.zoomLevel;
      return var0.getWidth() / zl;
   }

   public static int getImageHeight(Image var0) {
      if (var0 == null) return 0;
      int zl = mGraphics.zoomLevel < 1 ? 1 : mGraphics.zoomLevel;
      return var0.getHeight() / zl;
   }
   
   public final int getImageWidth() {
      return this.image != null ? getImageWidth(this.image) : 0;
   }
   
   public final int getImageHeight() {
      return this.image != null ? getImageHeight(this.image) : 0;
   }
   
   private int rawW = -1;
   private int rawH = -1;

   public final int getRawWidth() {
      if (this.rawW <= 0 && this.image != null) {
         this.rawW = this.image.getWidth();
      }
      return this.rawW;
   }

   public final int getRawHeight() {
      if (this.rawH <= 0 && this.image != null) {
         this.rawH = this.image.getHeight();
      }
      return this.rawH;
   }

   public final Image getImageObject() {
      return this.image;
   }
}
