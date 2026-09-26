public final class FrameImage {
   public static FrameImage getFrameImage(mImage img, int width, int height) {
      return new FrameImage(img, width, height);
   }

   public static FrameImage getFrameImage(int ID, int width, int height) {
      return new FrameImage(ID, width, height);
   }

   public static FrameImage getFrameImage(int ID, int numframe) {
      return new FrameImage(ID, numframe);
   }

   public static FrameImage getFrameImage(int ID, int width, int height, int maxNumFrame) {
      return new FrameImage(ID, width, height, maxNumFrame);
   }

   public static FrameImage getFrameImage(int ID, int width, int height, int width2, int height2) {
      return new FrameImage(ID, width, height, width2, height2);
   }

   public static FrameImage getFrameImage(int ID, int width, int height, int width2, int height2, int maxNumFrame) {
      return new FrameImage(ID, width, height, width2, height2, maxNumFrame);
   }

   public int frameWidth;
   public int frameHeight;
   public int nFrame = 1;
   public int maxNumFrame = 1;
   private int indexSuper = 0;
   public mImage imgFrame;
   private int Id = -1;
   private boolean lowG = false;
   private boolean isFormFrame = false;
   private boolean isLoaded = false;

   private int normalizeId(int id) {
      if (id >= 25000) {
         this.lowG = true;
         return id - 25000;
      } else if (id >= 24000) {
         return id - 24000;
      }
      return id;
   }

   public FrameImage(mImage img, int width, int height) {
      this.imgFrame = img;
      this.frameWidth = width;
      this.frameHeight = height;
      if (img != null && img.image != null && height > 0) {
         this.nFrame = mImage.getImageHeight(img.image) / height;
         if (this.nFrame < 1) this.nFrame = 1;
         this.isLoaded = true;
      } else {
         this.nFrame = 1;
         this.isLoaded = false;
      }
      this.maxNumFrame = this.nFrame;
   }

   public FrameImage(mImage var1, int var2, int var3, int var4) {
      this.imgFrame = var1;
      this.frameWidth = var2;
      this.frameHeight = var3;
      this.maxNumFrame = (var4 > 0) ? var4 : 1;
      if (this.imgFrame != null && this.imgFrame.image != null && var2 > 0) {
         this.nFrame = mImage.getImageWidth(this.imgFrame.image) / var2 * this.maxNumFrame;
         if (this.nFrame < 1) this.nFrame = 1;
         this.isLoaded = true;
      } else {
         this.nFrame = 1;
         this.isLoaded = false;
      }
   }

   public FrameImage(int ID, int width, int height) {
      this.Id = this.normalizeId(ID);
      this.frameWidth = width;
      this.frameHeight = height;
      this.imgFrame = this.getImage();
      if (this.imgFrame != null && this.imgFrame.image != null && height > 0) {
         this.nFrame = mImage.getImageHeight(this.imgFrame.image) / height;
         if (this.nFrame < 1) this.nFrame = 1;
         this.isLoaded = true;
      } else {
         this.nFrame = 1;
         this.isLoaded = false;
      }
      this.maxNumFrame = this.nFrame;
   }

   public FrameImage(mImage var1, int var2) {
      this.imgFrame = var1;
      this.nFrame = var2 > 0 ? var2 : 1;
      this.maxNumFrame = this.nFrame;
      this.isFormFrame = true;
      if (this.imgFrame != null && this.imgFrame.image != null) {
         this.frameWidth = mImage.getImageWidth(this.imgFrame.image);
         this.frameHeight = var2 > 0 ? mImage.getImageHeight(this.imgFrame.image) / var2 : mImage.getImageHeight(this.imgFrame.image);
         this.isLoaded = true;
      } else {
         this.isLoaded = false;
      }
   }

   public FrameImage(int var1, int var2) {
      this.Id = this.normalizeId(var1);
      this.nFrame = var2 > 0 ? var2 : 1;
      this.maxNumFrame = this.nFrame;
      this.imgFrame = this.getImage();
      this.isFormFrame = true;
      if (this.imgFrame != null && this.imgFrame.image != null) {
         this.frameWidth = mImage.getImageWidth(this.imgFrame.image);
         this.frameHeight = var2 > 0 ? mImage.getImageHeight(this.imgFrame.image) / var2 : mImage.getImageHeight(this.imgFrame.image);
         this.isLoaded = true;
      } else {
         this.isLoaded = false;
      }
   }

   public FrameImage(MainImage var1, int var2, int var3) {
      try {
         this.Id = this.normalizeId(var2);
         this.nFrame = 1;
         this.maxNumFrame = this.nFrame;
         if (var1 != null) {
            this.imgFrame = var1.img;
         }
         this.isFormFrame = true;
         if (this.imgFrame != null && this.imgFrame.image != null) {
            this.frameWidth = mImage.getImageWidth(this.imgFrame.image);
            this.frameHeight = mImage.getImageHeight(this.imgFrame.image);
            this.isLoaded = true;
         } else {
            this.isLoaded = false;
         }
      } catch (Exception var4) {
      }
   }

   public FrameImage(int var1, int var2, int var3, int var4, int var5) {
      this.Id = this.normalizeId(var1);
      if (GameCanvas.lowGraphic) {
         this.frameWidth = var4;
         this.frameHeight = var5;
         this.lowG = true;
      } else {
         this.frameWidth = var2;
         this.frameHeight = var3;
      }

      this.imgFrame = this.getImage();
      if (this.imgFrame != null && this.imgFrame.image != null && this.frameHeight > 0) {
         this.nFrame = mImage.getImageHeight(this.imgFrame.image) / this.frameHeight;
         if (this.nFrame < 1) this.nFrame = 1;
         this.isLoaded = true;
      } else {
         this.nFrame = 1;
         this.isLoaded = false;
      }
      this.maxNumFrame = this.nFrame;
   }

   public FrameImage(int var1, int var2, int var3, int var4) {
      this.createFrameImgNew(var1, var2, var3, var4);
   }

   public FrameImage(mImage ImagePotion, int var2, int var3, int var4, int var5) {
      this.Id = this.normalizeId(var2);
      this.frameWidth = var3;
      this.frameHeight = var4;
      this.maxNumFrame = 1;
      this.imgFrame = ImagePotion;
      if (this.imgFrame != null && this.imgFrame.image != null && var3 > 0) {
         this.nFrame = mImage.getImageWidth(this.imgFrame.image) / var3;
         if (this.nFrame < 1) this.nFrame = 1;
         this.isLoaded = true;
      } else {
         this.nFrame = 1;
         this.isLoaded = false;
      }
   }

   public FrameImage(int ID, int width, int height, byte maxNumFrame, byte frameSuper) {
      this.indexSuper = frameSuper;
      this.createFrameImgNew(ID, width, height, maxNumFrame);
   }

   private void createFrameImgNew(int ID, int width, int height, int maxNumFrame) {
      this.Id = this.normalizeId(ID);
      this.frameWidth = width;
      this.frameHeight = height;
      this.maxNumFrame = maxNumFrame > 0 ? maxNumFrame : 1;
      this.imgFrame = this.getImage();
      if (this.imgFrame != null && this.imgFrame.image != null && width > 0) {
         this.nFrame = mImage.getImageWidth(this.imgFrame.image) / width * this.maxNumFrame;
         if (this.nFrame < 1) this.nFrame = 1;
         this.isLoaded = true;
      } else {
         this.nFrame = 1;
         this.isLoaded = false;
      }
   }

   public FrameImage(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.Id = this.normalizeId(var1);
      if (GameCanvas.lowGraphic) {
         this.frameWidth = var4;
         this.frameHeight = var5;
         this.lowG = true;
      } else {
         this.frameWidth = var2;
         this.frameHeight = var3;
      }

      this.maxNumFrame = var6 > 0 ? var6 : 1;
      this.imgFrame = this.getImage();
      if (this.imgFrame != null && this.imgFrame.image != null && this.frameWidth > 0) {
         this.nFrame = mImage.getImageWidth(this.imgFrame.image) / this.frameWidth * this.maxNumFrame;
         if (this.nFrame < 1) this.nFrame = 1;
         this.isLoaded = true;
      } else {
         this.nFrame = 1;
         this.isLoaded = false;
      }
   }

   public final void updateDimensions() {
      if (this.imgFrame == null || this.imgFrame.image == null) return;
      int imgW = mImage.getImageWidth(this.imgFrame.image);
      int imgH = mImage.getImageHeight(this.imgFrame.image);
      if (imgW <= 0 && this.imgFrame.image != null) imgW = this.imgFrame.image.getWidth();
      if (imgH <= 0 && this.imgFrame.image != null) imgH = this.imgFrame.image.getHeight();

      if (this.isFormFrame) {
         if (imgW > 0) this.frameWidth = imgW;
         if (this.nFrame > 0 && imgH > 0) {
            this.frameHeight = imgH / this.nFrame;
         } else if (imgH > 0) {
            this.frameHeight = imgH;
         }
         this.isLoaded = true;
      } else if (this.maxNumFrame > 1 && this.frameWidth > 0) {
         if (imgW > 0) {
            this.nFrame = (imgW / this.frameWidth) * this.maxNumFrame;
            if (this.nFrame < 1) this.nFrame = 1;
            if (this.frameHeight <= 0 && imgH > 0) {
               this.frameHeight = imgH / this.maxNumFrame;
            }
            this.isLoaded = true;
         }
      } else if (this.frameHeight > 0) {
         if (imgH > 0) {
            this.nFrame = imgH / this.frameHeight;
            if (this.nFrame < 1) this.nFrame = 1;
            this.maxNumFrame = this.nFrame;
            if (this.frameWidth <= 0 && imgW > 0) {
               this.frameWidth = imgW;
            }
            this.isLoaded = true;
         }
      } else if (this.frameWidth > 0) {
         if (imgW > 0) {
            this.nFrame = imgW / this.frameWidth;
            if (this.nFrame < 1) this.nFrame = 1;
            this.maxNumFrame = 1;
            if (this.frameHeight <= 0 && imgH > 0) {
               this.frameHeight = imgH;
            }
            this.isLoaded = true;
         }
      }
   }

   public final void drawFrame(int var1, int var2, int var3, int var4, int var5, mGraphics var6) {
      if (var6 == null) return;
      if (this.imgFrame == null || this.imgFrame.image == null) {
         this.imgFrame = this.getImage();
         if (this.imgFrame == null || this.imgFrame.image == null) return;
         this.isLoaded = false;
      }
      if (!this.isLoaded || (this.isFormFrame && (this.frameWidth <= 0 || this.frameHeight <= 0))) {
         this.updateDimensions();
      }
      if (var1 >= 0 && var1 < this.nFrame && this.frameHeight > 0 && this.frameWidth > 0) {
         var6.drawRegion(this.imgFrame, 0, var1 * this.frameHeight, this.frameWidth, this.frameHeight, var4, var2, var3, var5);
      }
   }

   public final mImage getImageFrame() {
      if (this.imgFrame != null && this.imgFrame.image != null) {
         return this.imgFrame;
      } else {
         this.imgFrame = this.getImage();
         return this.imgFrame;
      }
   }

   public mImage getImage() {
      try {
         if (this.imgFrame != null && this.imgFrame.image != null) {
            return this.imgFrame;
         }
         if (this.Id != -1) {
            mImage localImg = mImage.createImage("/eff/" + this.Id + ".png");
            if (localImg != null && localImg.image != null) {
               this.imgFrame = localImg;
               this.isLoaded = false;
               return this.imgFrame;
            }
            MainImage mainImg = ObjectData.getImage(this.lowG ? IconType.EFF_CLIENT_LOW : IconType.EFF_CLIENT, this.Id);
            if (mainImg != null && mainImg.img != null && mainImg.img.image != null) {
               this.imgFrame = mainImg.img;
               this.isLoaded = false;
            }
         }
         return this.imgFrame;
      } catch (Exception e) {
         return null;
      }
   }

   public final void drawFrameNew_BeginSuper(int idx, int x, int y, int trans, int orthor, mGraphics g) {
      if (g == null) return;
      if (this.imgFrame == null || this.imgFrame.image == null) {
         this.imgFrame = this.getImage();
         if (this.imgFrame == null || this.imgFrame.image == null) return;
      }
      if (!this.isLoaded) {
         this.updateDimensions();
      }
      int maxN = this.maxNumFrame > 0 ? this.maxNumFrame : 1;
      int num = idx + this.indexSuper * maxN;
      if (num >= 0 && num < this.nFrame && maxN > 0 && this.frameWidth > 0 && this.frameHeight > 0) {
         g.drawRegion(this.imgFrame, num / maxN * this.frameWidth, num % maxN * this.frameHeight, this.frameWidth, this.frameHeight, trans, x, y, orthor);
      }
   }

   public final void drawFrameNew(int idx, int x, int y, int trans, int orthor, mGraphics g) {
      if (g == null) return;
      if (this.imgFrame == null || this.imgFrame.image == null) {
         this.imgFrame = this.getImage();
         if (this.imgFrame == null || this.imgFrame.image == null) return;
      }
      if (!this.isLoaded) {
         this.updateDimensions();
      }
      int maxN = this.maxNumFrame > 0 ? this.maxNumFrame : 1;
      if (idx >= 0 && idx < this.nFrame && maxN > 0 && this.frameWidth > 0 && this.frameHeight > 0) {
         g.drawRegion(this.imgFrame, idx / maxN * this.frameWidth, idx % maxN * this.frameHeight, this.frameWidth, this.frameHeight, trans, x, y, orthor);
      }
   }
}
