public final class FrameImage {
   public int frameWidth;
   public int frameHeight;
   public int nFrame = 1;
   public int maxNumFrame = 1;
   private int indexSuper = 0;
   public mImage imgFrame;
   private int Id = -1;
   private boolean lowG = false;
   private boolean isFormFrame = false;

   public FrameImage(mImage img, int width, int height) {
      this.imgFrame = img;
      this.frameWidth = width;
      this.frameHeight = height;
      this.nFrame = mImage.getImageHeight(img.image) / height;
   }

   public FrameImage(mImage var1, int var2, int var3, int var4) {
      this.imgFrame = var1;
      this.frameWidth = var2;
      this.frameHeight = var3;
      this.maxNumFrame = 5;
      this.nFrame = mImage.getImageWidth(this.imgFrame.image) / var2 * 5;
   }

   public FrameImage(int ID, int width, int height) {
      this.Id = ID;
      this.frameWidth = width;
      this.frameHeight = height;
      this.imgFrame = this.getImage();
      if (this.imgFrame != null && this.imgFrame.image != null) {
         this.nFrame = mImage.getImageHeight(this.imgFrame.image) / height;
         this.maxNumFrame = this.nFrame;
      }
   }

   public FrameImage(mImage var1, int var2) {
      this.imgFrame = var1;
      this.nFrame = var2;
      this.maxNumFrame = this.nFrame;
      this.frameWidth = mImage.getImageWidth(this.imgFrame.image);
      this.frameHeight = mImage.getImageHeight(this.imgFrame.image) / var2;
   }

   public FrameImage(int var1, int var2) {
      this.Id = var1;
      this.nFrame = var2;
      this.maxNumFrame = this.nFrame;
      this.imgFrame = this.getImage();
      this.isFormFrame = true;
      if (this.imgFrame != null && this.imgFrame.image != null) {
         this.frameWidth = mImage.getImageWidth(this.imgFrame.image);
         this.frameHeight = mImage.getImageHeight(this.imgFrame.image) / var2;
      }
   }

   public FrameImage(MainImage var1, int var2, int var3) {
      try {
         this.Id = var2;
         this.nFrame = 1;
         this.maxNumFrame = this.nFrame;
         this.imgFrame = var1.img;
         this.isFormFrame = true;
         if (this.imgFrame != null && this.imgFrame.image != null) {
            this.frameWidth = mImage.getImageWidth(this.imgFrame.image);
            this.frameHeight = mImage.getImageHeight(this.imgFrame.image);
         }
      } catch (Exception var4) {
      }
   }

   public FrameImage(int var1, int var2, int var3, int var4, int var5) {
      this.Id = var1;
      if (GameCanvas.lowGraphic) {
         this.frameWidth = var4;
         this.frameHeight = var5;
         this.lowG = true;
      } else {
         this.frameWidth = var2;
         this.frameHeight = var3;
      }

      this.imgFrame = this.getImage();
      if (this.imgFrame != null && this.imgFrame.image != null) {
         this.nFrame = mImage.getImageHeight(this.imgFrame.image) / this.frameHeight;
         this.maxNumFrame = this.nFrame;
      }
   }

   public FrameImage(int var1, int var2, int var3, int var4) {
      this.createFrameImgNew(var1, var2, var3, var4);
   }

   public FrameImage(mImage ImagePotion, int var2, int var3, int var4, int var5) {
      this.Id = var2;
      this.frameWidth = var3;
      this.frameHeight = var4;
      this.maxNumFrame = 1;
      this.imgFrame = ImagePotion;
      if (this.imgFrame != null && this.imgFrame.image != null) {
         this.nFrame = mImage.getImageWidth(this.imgFrame.image) / var3;
      }
   }

   public FrameImage(int ID, int width, int height, byte maxNumFrame, byte frameSuper) {
      this.indexSuper = frameSuper;
      this.createFrameImgNew(ID, width, height, maxNumFrame);
   }

   private void createFrameImgNew(int ID, int width, int height, int maxNumFrame) {
      this.Id = ID;
      this.frameWidth = width;
      this.frameHeight = height;
      this.maxNumFrame = maxNumFrame;
      this.imgFrame = this.getImage();
      if (this.imgFrame != null && this.imgFrame.image != null) {
         this.nFrame = mImage.getImageWidth(this.imgFrame.image) / width * maxNumFrame;
      }
   }

   public FrameImage(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.Id = var1;
      if (GameCanvas.lowGraphic) {
         this.frameWidth = var4;
         this.frameHeight = var5;
         this.lowG = true;
      } else {
         this.frameWidth = var2;
         this.frameHeight = var3;
      }

      this.maxNumFrame = var6;
      this.imgFrame = this.getImage();
      if (this.imgFrame != null && this.imgFrame.image != null) {
         this.nFrame = mImage.getImageWidth(this.imgFrame.image) / this.frameWidth * var6;
      }
   }

   public final void drawFrame(int var1, int var2, int var3, int var4, int var5, mGraphics var6) {
      if (this.imgFrame == null) {
         this.imgFrame = this.getImage();
         if (this.imgFrame != null && this.imgFrame.image != null) {
            if (this.isFormFrame) {
               this.frameWidth = mImage.getImageWidth(this.imgFrame.image);
               this.frameHeight = mImage.getImageHeight(this.imgFrame.image) / this.nFrame;
            } else {
               this.nFrame = mImage.getImageHeight(this.imgFrame.image) / this.frameHeight;
               this.maxNumFrame = this.nFrame;
            }
         }
      } else {
         if (var1 >= 0 && var1 < this.nFrame) {
            var6.drawRegion(this.imgFrame, 0, var1 * this.frameHeight, this.frameWidth, this.frameHeight, var4, var2, var3, var5);
         }

      }
   }

   public final mImage getImageFrame() {
      if (this.imgFrame != null) {
         return this.imgFrame;
      } else {
         if (this.lowG) {
            this.imgFrame = ObjectData.getImageAll((short)this.Id, ObjectData.HashImageEffClientLow, (short)25000).img;
         } else {
            this.imgFrame = ObjectData.getImageAll((short)this.Id, ObjectData.HashImageEffClient, (short)24000).img;
         }

         return this.imgFrame;
      }
   }

   private mImage getImage() {
      return this.lowG ? ObjectData.getImageAll((short)this.Id, ObjectData.HashImageEffClientLow, (short)25000).img : ObjectData.getImageAll((short)this.Id, ObjectData.HashImageEffClient, (short)24000).img;
   }

   public final void drawFrameNew_BeginSuper(int idx, int x, int y, int trans, int orthor, mGraphics g) {
      idx += this.indexSuper * this.maxNumFrame;
      if (this.imgFrame == null) {
         this.imgFrame = this.getImage();
         if (this.imgFrame != null && this.imgFrame.image != null) {
            if (this.isFormFrame) {
               this.frameWidth = mImage.getImageWidth(this.imgFrame.image);
               this.frameHeight = mImage.getImageHeight(this.imgFrame.image) / this.nFrame;
            } else {
               this.nFrame = mImage.getImageWidth(this.imgFrame.image) / this.frameWidth * this.maxNumFrame;
            }
         }
      } else {
         if (idx >= 0 && idx < this.nFrame) {
            g.drawRegion(this.imgFrame, idx / this.maxNumFrame * this.frameWidth, idx % this.maxNumFrame * this.frameHeight, this.frameWidth, this.frameHeight, trans, x, y, orthor);
         }

      }
   }

   public final void drawFrameNew(int idx, int x, int y, int trans, int orthor, mGraphics g) {
      if (this.imgFrame == null) {
         this.imgFrame = this.getImage();
         if (this.imgFrame != null && this.imgFrame.image != null) {
            if (this.isFormFrame) {
               this.frameWidth = mImage.getImageWidth(this.imgFrame.image);
               this.frameHeight = mImage.getImageHeight(this.imgFrame.image) / this.nFrame;
            } else {
               this.nFrame = mImage.getImageWidth(this.imgFrame.image) / this.frameWidth * this.maxNumFrame;
            }
         }
      } else {
         if (idx >= 0 && idx < this.nFrame) {
            g.drawRegion(this.imgFrame, idx / this.maxNumFrame * this.frameWidth, idx % this.maxNumFrame * this.frameHeight, this.frameWidth, this.frameHeight, trans, x, y, orthor);
         }

      }
   }
}
