public final class Item_Drop extends MainObject {
   private long AD;
   private int AE;
   private int AF;
   private int AG;
   private int AH;
   private byte AI;
   public byte AA;
   public int num;
   private boolean AJ;
   private boolean AK = false;
   public static MainImage imgMainIconXp;
   private FrameImage AL = null;
   private boolean AM = false;

   public Item_Drop(short var1, byte var2, String var3, int var4, int var5, short var6, byte var7) {
      super.ID = var1;
      super.typeObject = var2;
      super.name = var3;
      if (var4 < 48) {
         var4 = 48;
      }

      if (var4 > GameCanvas.loadmap.maxWMap - 48) {
         var4 = GameCanvas.loadmap.maxWMap - 48;
      }

      if (var5 < 48) {
         var5 = 48;
      }

      if (var5 > GameCanvas.loadmap.maxHMap - 48) {
         var5 = GameCanvas.loadmap.maxHMap - 48;
      }

      super.x = var4;
      super.y = var5;
      super.IdIcon = var6;
      super.colorName = var7;
      super.wOne = super.hOne = -1;
      super.vx = CRes.random_Am(1, 5);
      super.vy = -CRes.random(3, 10);
      this.AF = CRes.random(5, 12);
      super.CN = 16;
      super.vySea = 4;
      super.KA = 0;
      if (super.typeObject == 4 && (var6 == 10 || var6 == 286)) {
         super.KA = 20;
      }

      this.AD = GameCanvas.timeNow;
      super.LK = 60;
      super.NQ = false;
      this.AI = 0;
   }

   public Item_Drop(short var1, byte var2, String var3, int var4, int var5, short var6, byte var7, int var8, int var9) {
      super.ID = var1;
      super.typeObject = var2;
      super.name = var3;
      if (var4 < 48) {
         var4 = 48;
      }

      if (var4 > GameCanvas.loadmap.maxWMap - 48) {
         var4 = GameCanvas.loadmap.maxWMap - 48;
      }

      if (var5 < 48) {
         var5 = 48;
      }

      if (var5 > GameCanvas.loadmap.maxHMap - 48) {
         var5 = GameCanvas.loadmap.maxHMap - 48;
      }

      super.x = var4;
      super.y = var5;
      super.IdIcon = var6;
      super.colorName = var7;
      super.wOne = super.hOne = -1;
      super.CN = 8;
      this.AD = GameCanvas.timeNow;
      super.LK = 60;
      super.NQ = false;
      this.AI = 1;
      int var10001 = var8 - var4;
      int var10002 = var9 - var5;
      Object var10 = null;
      int var12 = var10002;
      int var11 = var10001;
      if (var12 == 0) {
         var12 = 1;
      }

      if (var11 == 0) {
         var11 = 1;
      }

      int var13;
      if ((var13 = MainObject.AD(var11, var12) / super.CN) == 0) {
         var13 = 1;
      }

      var4 = var11 / var13;
      var5 = var12 / var13;
      if (CRes.abs(var4) > CRes.abs(var11)) {
         var4 = var11;
      }

      if (CRes.abs(var5) > CRes.abs(var12)) {
         var5 = var12;
      }

      this.AF = var13;
      super.vx = var4;
      super.vy = var5;
      Object var10000 = null;
   }

   public final void paint(mGraphics var1) {
      if (this.AK) {
         this.AA(var1, MainScreen.cameraMain.xCam + super.x, super.y);
      } else {
         MainImage var2 = this.AB();
         if (!this.AM) {
            this.AA(var2);
         }

         if (var2.img != null) {
            if (super.wOne < 0) {
               super.wOne = mImage.getImageWidth(var2.img.image);
            }

            if (super.hOne < 0) {
               super.hOne = mImage.getImageHeight(var2.img.image);
            }

            int var3 = super.y;
            if (this.AF <= 0) {
               var1.drawRegion((mImage)AvMain.imgShadowSmall, super.x, var3 - super.CH / 10 - 4 + super.KA, 33);
            }

            if (this.AL != null) {
               int var4 = GameCanvas.gameTick / 3 % this.AL.nFrame;
               this.AL.drawFrame(var4 > this.AL.nFrame - 1 ? 0 : var4, super.x, super.y, 0, 3, var1);
            } else if (super.CH / 10 != 0) {
               var1.drawRegion(var2.img, 0, 0, super.wOne, super.hOne + super.CH / 10, 0, super.x, var3, 33);
            } else {
               var1.drawRegion((mImage)var2.img, super.x, var3, 33);
            }
         }

         if (Interface_Game.typeTouch == 1) {
            if (super.typeObject != 4 || super.IdIcon != 10 && super.IdIcon != 286) {
               byte var5 = 0;
               if (super.typeObject == 3) {
                  var5 = super.colorName;
               }

               this.AA(var1, (byte)var5, 0);
               return;
            }
         } else if (super.IW > 0) {
            AvMain.FontBorderColor(var1, "( " + super.IW + " )", super.x, super.y - super.dy - super.hOne - 18, 2, (int)6, (int)7);
         }

      }
   }

   public final void AA(mGraphics var1, int var2, int var3) {
      MainImage var4;
      if ((var4 = this.AB()) != null && var4.img != null) {
         if (!this.AM) {
            this.AA(var4);
         }

         if (this.AL != null) {
            int var5 = GameCanvas.gameTick / 3 % this.AL.nFrame;
            this.AL.drawFrame(var5 > this.AL.nFrame - 1 ? 0 : var5, var2, var3, 0, 3, var1);
            return;
         }

         if (super.hOne == 0) {
            super.hOne = mImage.getImageHeight(var4.img.image);
         }

         var1.drawRegion((mImage)var4.img, var2, var3, 3);
      }

   }

   private void AA(MainImage var1) {
      if (var1 != null && var1.img != null) {
         int var2 = mImage.getImageWidth(var1.img.image);
         if (mImage.getImageHeight(var1.img.image) / 2 >= var2) {
            this.AL = new FrameImage(var1.img, var2, var2);
         }

         this.AM = true;
         super.hOne = var2;
      }

   }

   public final void AA(mGraphics var1, byte var2, int var3) {
      if (!GameScreen.getIsOffAdmin((byte)0)) {
         if (var3 == 0) {
            AvMain.AB(super.colorName).drawString(var1, super.name, super.x, super.y - super.dy - super.hOne - 18, 2);
         }

         if (super.IW > 0) {
            AvMain.FontBorderColor(var1, "( " + super.IW + " )", super.x, super.y - super.dy - super.hOne - 28, 2, (int)6, (int)7);
            AvMain.FontBorderColor(var1, super.BD, super.x, super.y - super.dy - super.hOne - 38, 2, (int)5, (int)7);
         }

      }
   }

   private MainImage AB() {
      if (super.IdIcon < 0) {
         return null;
      } else if (super.typeObject == 99) {
         return imgMainIconXp;
      } else if (super.typeObject == 3) {
         return ObjectData.getImageAll(super.IdIcon, ObjectData.hashImageItem, (short)3000);
      } else if (super.typeObject == 4) {
         return ObjectData.getImageAll(super.IdIcon, ObjectData.hashImagePotion, (short)2000);
      } else if (super.typeObject == 5) {
         return ObjectData.getImageAll(super.IdIcon, ObjectData.hashImageQuestPotion, (short)6000);
      } else if (super.typeObject == 7) {
         return ObjectData.getImageAll(super.IdIcon, ObjectData.hashImageMaterialPotion, (short)6500);
      } else if (super.typeObject == 109) {
         return ObjectData.getImageAll(super.IdIcon, ObjectData.HashImageOtherNew, (short)23000);
      } else {
         return super.typeObject == 105 ? ObjectData.getImageAll(super.IdIcon, ObjectData.HashImageFashion, (short)20000) : null;
      }
   }

   public final void AB(mGraphics var1, int var2, int var3) {
      AvMain.AA(var1, super.name, var2 + 48, var3, 1, (byte)super.colorName);
   }

   public final void update() {
      if (!super.isRemove) {
         if (this.AF > 0) {
            super.x += super.vx;
            super.y += super.vy;
            if (this.AI == 0) {
               super.vy += 2;
            }

            --this.AF;
         }

         if (this.AF == 0 && GameScreen.CV == 8) {
            MainHelp.AA(false);
         }

         if (this.AJ) {
            ++this.AE;
            super.x += super.vx;
            super.y += super.vy;
            if (this.AE >= this.AG) {
               super.isRemove = true;
            }
         } else if (this.AF <= 0) {
            if (this.AF == 0) {
               super.CN = 8;
            }

            int var2;
            int var3;
            int var4;
            if (super.LO != null) {
               super.toX = super.LO.x;
               super.toY = super.LO.y - super.LO.hOne / 2;
               var2 = super.toX - super.x;
               if ((var3 = super.toY - super.y) == 0) {
                  var3 = 1;
               }

               if (var2 == 0) {
                  var2 = 1;
               }

               if ((var4 = MainObject.AD(var2, var3) / super.CN) == 0) {
                  var4 = 1;
               }

               super.vx = var2 / var4;
               super.vy = var3 / var4;
               if (CRes.abs(super.vx) > CRes.abs(var2)) {
                  super.vx = var2;
               }

               if (CRes.abs(super.vy) > CRes.abs(var3)) {
                  super.vy = var3;
               }

               this.AE = 0;
               this.AG = var4 + 1;
               this.AF = 0;
               this.AJ = true;
            }

            if (super.LP != null) {
               if (super.LP.AA == 0) {
                  super.toX = MotherCanvas.hw - 40;
               } else {
                  super.toX = MotherCanvas.hw + 100;
               }

               super.toY = 60;
               super.x -= MainScreen.cameraMain.xCam;
               var2 = super.toX - super.x;
               if ((var3 = super.toY - super.y) == 0) {
                  var3 = 1;
               }

               if (var2 == 0) {
                  var2 = 1;
               }

               if ((var4 = MainObject.AD(var2, var3) / super.CN) == 0) {
                  var4 = 1;
               }

               super.vx = var2 / var4;
               super.vy = var3 / var4;
               if (CRes.abs(super.vx) > CRes.abs(var2)) {
                  super.vx = var2;
               }

               if (CRes.abs(super.vy) > CRes.abs(var3)) {
                  super.vy = var3;
               }

               this.AE = 0;
               this.AG = var4 + 1;
               this.AF = 0;
               this.AJ = true;
               this.AK = true;
            }
         }

         if (super.NQ) {
            ++this.AH;
            if (this.AH > 40) {
               super.NQ = false;
               this.AH = 0;
            }
         }

         if ((GameCanvas.timeNow - this.AD) / 1000L >= (long)super.LK) {
            super.isRemove = true;
         }

         if (LoadMap.specMap == 4) {
            this.AG();
         }

         if (super.IW > 0 && GameCanvas.timeNow - super.LJ > 1000L) {
            --super.IW;
            super.LJ += 1000L;
         }

      }
   }

   public final void AG() {
      if (CRes.random(40) == 0) {
         if (CRes.random(2) == 0) {
            super.vySea = 4;
         } else {
            super.vySea = -4;
         }
      }

      if (super.CH > 0 && super.vySea > 0) {
         super.vySea = -4;
      } else if (super.CH < -50 && super.vySea < 0) {
         super.vySea = 4;
      }

      super.CH += super.vySea;
   }

   public final void a_() {
      this.setFireObject(2);
   }

   public final void setFireObject(int var1) {
      if (!super.isRemove) {
         if (super.BD == "" && super.BQ != null) {
            MsgShowGift var2;
            (var2 = new MsgShowGift()).setinfoShow_Gift((byte)1, super.name, T.EJ, super.BQ, (short)-1);
            GameCanvas.AA((MainDialog)var2);
            super.isRemove = true;
            return;
         }

         GlobalService.getInstance().Get_Item_Map(super.ID, super.typeObject);
         super.NQ = true;
      }

   }
}
