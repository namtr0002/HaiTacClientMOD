public final class Pet extends MainMonster {
   public static int[][] AS = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1}, {3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6}, {7, 7, 7, 7, 7, 8, 8, 8}, {2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, {2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};
   private static int[][] AV = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1}, {1, 1, 1, 2, 2, 2, 1, 1, 1, 3, 3, 3}, {1, 1, 4, 4, 4, 4, 4, 4}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}};
   private static int[][] AW = new int[][]{{0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1}, {3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5}, {0, 0, 0, 1, 1, 1, 2, 2, 2, 1, 1, 1}, {0, 0, 0, 1, 1, 1, 2, 2, 2, 1, 1, 1}, {0, 0, 0, 1, 1, 1, 2, 2, 2, 1, 1, 1}};
   private static int[][] AX = new int[][]{{0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2}, {0, 0, 0, 1, 1, 1, 2, 2, 2}, {0, 0, 0, 1, 1, 1, 2, 2, 2}, {0, 0, 0, 1, 1, 1, 2, 2, 2}, {0, 0, 0, 1, 1, 1, 2, 2, 2}};
   public static int[][] AT = new int[][]{{0, 0, 0, 0, 0, 1, 1, 1, 1, 1}, {2, 2, 3, 3, 4, 4}, {2, 2, 3, 3, 4, 4}, {2, 2, 3, 3, 4, 4}, {2, 2, 3, 3, 4, 4}};
   private static int[] AY = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2};
   public static int[] AU = new int[]{5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6};

   public Pet(short var1, short var2, short var3, byte var4) {
      super.IDMainShiper = var2;
      this.setDataPet(var1, var3, var4);
   }

   public final void setDataPet(short var1, short var2, byte var3) {
      super.ID = var1;
      super.MS = var3;
      super.IdIcon = var2;
      super.wOne = super.hOne = -1;
      super.colorName = 5;
      super.LO = MainObject.get_Object((int)super.IDMainShiper, (byte)0);
      if (super.LO != null) {
         this.CB();
      }

      super.f = 0;
      super.typeObject = 10;
      super.Action = 0;
      this.setSpeed(5, 5);
      super.LY = 1;
      super.CG = 0;
      switch(super.MS) {
      case 0:
         super.BM = AS;
         super.BN = AY;
         super.IO = 9;
         return;
      case 1:
         super.BM = MonsterWalk.AX;
         super.BN = MonsterWalk.AX[0];
         super.IO = 9;
         return;
      case 2:
         super.BM = AV;
         super.BN = AV[0];
         break;
      case 3:
      case 5:
         super.BM = AW;
         super.BN = AW[0];
         super.IO = 6;
         this.setSpeed(3, 3);
         super.CF = 3;
         super.LY = -1;
         super.CG = 10;
         return;
      case 4:
         super.BM = AX;
         super.BN = AX[0];
         super.IO = 3;
         super.CF = 20;
         super.CG = 15;
         return;
      case 21:
         this.setSpeed(6, 6);
         super.BM = AT;
         super.BN = AT[0];
         if (super.IdIcon == 55 || super.IdIcon == 56) {
            super.IO = 7;
            super.BN = AU;
            if (super.IdIcon == 56) {
               super.LY = 0;
               return;
            }

            return;
         }
         break;
      default:
         return;
      }

      super.IO = 5;
   }

   public final void paint(mGraphics var1) {
      MainImage var2 = ObjectData.getImageAll(super.IdIcon, ObjectData.HashImageMonster, (short)1000);
      if (LoadMap.specMap != 4 && super.LY >= 0) {
         this.AA(var1, super.x, -3, super.LY);
      }

      if (super.LO != null) {
         if (LoadMap.specMap != 4 || super.LO.Action != 4) {
            int var3;
            if ((var3 = super.Action) > super.BM.length - 1) {
               var3 = 0;
            }

            if (super.NI && super.Action == 0) {
               if (super.f > super.BN.length - 1) {
                  super.f = 0;
               }
            } else if (super.f > super.BM[var3].length - 1) {
               super.f = 0;
            }

            if (var2.img != null) {
               if (super.wOne < 0) {
                  super.hOne = mImage.getImageHeight(var2.img.image) / super.IO;
                  super.wOne = mImage.getImageWidth(var2.img.image);
               }

               if (super.NI && super.Action == 0) {
                  var3 = super.BN[super.f];
               } else {
                  var3 = super.BM[var3][super.f];
               }

               if (super.Action != 4) {
                  var1.drawRegion(var2.img, 0, var3 * super.hOne, super.wOne, super.hOne, super.Dir == 2 ? 2 : 0, super.x, super.y - super.CF - super.LO.CH / 10, 33);
               }
            }

            if (LoadMap.specMap != 4) {
               AvMain.AB(var1, super.LO.name, super.x, super.y - super.CF - 1 - super.hOne - 10, 2, (int)super.colorName);
            }

         }
      }
   }

   public final void update() {
      if (super.LO == null) {
         if (GameCanvas.gameTick % 100 == 0) {
            super.LO = MainObject.get_Object((int)super.IDMainShiper, (byte)0);
            if (super.LO != null) {
               this.CB();
            }
         }

      } else {
         if (super.f == 0 && super.Action == 0) {
            if (CRes.random(6) == 0) {
               super.NI = true;
            } else {
               super.NI = false;
            }
         }

         super.x += super.vx;
         super.y += super.vy;
         this.AA(true);
         this.BB();
         if (super.skillCurrent == null) {
            this.AF();
         }

         if (super.Action == 0) {
            this.CA();
         } else if (super.Action == 1 && MainObject.getDistance(super.x, super.y, super.toX, super.toY) <= 24) {
            this.CA();
         }

         if (super.Action != 0 && super.NI) {
            super.NI = false;
         }

      }
   }

   private void CA() {
      if (super.LO.typeActionBoat == 0) {
         if (LoadMap.specMap == 4) {
            if (super.LO.boatSea != null) {
               if (super.LO.boatSea.Dir == 0) {
                  super.x = super.LO.boatSea.x + 30;
               } else {
                  super.x = super.LO.boatSea.x - 30;
               }

               super.y = super.LO.boatSea.y + 1;
               super.dy = 5;
               super.toX = super.x;
               super.toY = super.y;
               super.Dir = super.LO.boatSea.Dir;
            }

            if (super.Action == 1) {
               super.Action = 0;
            }

         } else {
            boolean var1 = false;
            int var2 = MainObject.getDistance(super.x, super.y, super.LO.x, super.LO.y);
            int var3 = CRes.random(100);
            if (var2 > 250) {
               this.setSpeed(14, 14);
               var1 = true;
            }

            if (!var1 && var2 > 150) {
               this.setSpeed(8, 8);
               var1 = true;
            }

            if (!var1 && var2 > 100) {
               this.setSpeed(super.CN, super.CN);
               var1 = true;
            }

            if (!var1 && var3 < 2) {
               this.setSpeed(super.CN, super.CN);
               var1 = true;
            }

            if (!var1 && var2 > 48 && var3 < 25) {
               this.setSpeed(super.CN, super.CN);
               var1 = true;
            }

            var2 = 0;
            if (var1) {
               int var4;
               int var5;
               do {
                  ++var2;
                  var5 = super.LO.x + CRes.random_Am(12, 48);
                  var3 = super.LO.y + CRes.random_Am(12, 24);
                  if (super.Action == 1) {
                     if (var2 < 5) {
                        var3 = super.LO.y - 12;
                     } else {
                        var3 = super.LO.y + 12;
                     }
                  }
               } while((var4 = GameCanvas.loadmap.AA(var5, var3)) != 0 && var4 != 2 && var2 < 10);

               super.toX = var5;
               super.toY = var3;
            }

         }
      }
   }

   private void CB() {
      super.x = super.LO.x;
      super.y = super.LO.y - super.CG;
      super.toX = super.LO.x;
      super.toY = super.LO.y - super.CG;
      super.LO.NJ = true;
      if (super.LO == GameScreen.player) {
         super.colorName = 0;
      }

   }

   public final void BW() {
      if (super.Action != 2 || super.Action != 4) {
         super.Action = 2;
         super.f = 0;
      }

   }
}
