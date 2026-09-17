public final class Class_EP extends MainObject {
   private byte AB;
   private byte AC;
   private int AD = 7;
   private int AE = 0;
   private int AF = 0;
   private int AG;
   public byte AA = 0;
   private static int[][] AH = new int[][]{{1, 8, -19, 0, 1}, {2, 2, -24, 1, 1}, {3, 8, -21, 1, 1}, {36, 10, -22, 3, 1}, {35, -10, -23, 4, 1}, {34, 5, -20, 0, 1}, {33, -1, -22, 1, 1}, {32, -7, -25, 1, 1}, {29, 6, -19, 3, 1}, {28, 8, -24, 4, 1}, {27, -8, -17, 2, 0}, {22, 4, -25, 0, 1}, {21, 10, -20, 1, 1}, {20, 8, -22, 1, 1}, {19, -8, -12, 2, 0}, {18, 8, -20, 0, 1}, {17, 0, -10, 2, 0}, {16, 8, -24, 3, 1}, {15, -8, -17, 2, 0}, {14, 10, -21, 4, 1}, {13, -12, -17, 2, 0}, {12, 10, -25, 1, 1}, {11, -2, -23, 1, 1}, {10, -8, -17, 2, 0}, {9, -2, -19, 1, 1}, {37, 7, -27, 3, 1}, {38, 4, -25, 4, 1}, {39, 6, -27, 1, 1}, {60, 16, -10, 2, 0}, {59, 6, -22, 0, 1}, {58, 6, -22, 3, 1}};
   private int AI = 0;
   private int AJ = 0;

   public Class_EP(String var1, String var2, short var3, short var4, short var5, byte var6, byte var7, byte var8) {
      super.name = var1;
      super.BF = var2;
      super.ID = var3;
      super.x = var4;
      super.y = var5;
      super.typeObject = 2;
      this.AB = var8;
      super.Hp = 100;
      super.maxHp = 100;
      super.CQ = 0;
      super.Action = 0;
      GameCanvas.loadmap.AA(var4, var5, var6, var7);
      this.AC = (byte)CRes.random(7);
      super.colorName = 5;
      super.vySea = 4;
      this.BU();
   }

   public final void AA(byte var1, byte var2) {
      super.IdIcon = var1;
      super.IO = var2;
      this.AD = 7;
      if (super.IdIcon == 36) {
         this.AE = 12;
      } else if (super.IdIcon == 18) {
         this.AE = 18;
      } else if (super.IdIcon == 30) {
         this.AF = -8;
         this.AE = 3;
      } else if (super.IdIcon == 31) {
         this.AF = -10;
         this.AE = 13;
      } else if (super.IdIcon == 66) {
         super.KA = 3;
         this.AD = 12;
      } else {
         if (super.IdIcon == 68) {
            this.AE = 6;
         }

      }
   }

   public final void paint(mGraphics var1) {
      if (this.AA == 1 || this.AA == 98) {
         var1.drawRegion((mImage)MainObject.imgShadow, super.x + 1, super.y - 3 - super.CH / 10 + super.KA, 3);
      }

      if (this.AB == 1) {
         this.AD(var1, 0);
      } else if (this.AB == 0) {
         MainImage var2;
         if ((var2 = ObjectData.getImageAll(super.IdIcon, ObjectData.hashImageNPC, (short)5000)).img != null) {
            if (super.wOne == 0) {
               super.hOne = mImage.getImageHeight(var2.img.image) / super.IO;
               super.wOne = mImage.getImageWidth(var2.img.image);
            }

            var1.drawRegion(var2.img, 0, (this.AC + GameCanvas.gameTick / this.AD) % super.IO * super.hOne, super.wOne, super.hOne + super.CH / 10, 0, super.x, super.y, 33);
         }

         if (this.AA == 99) {
            byte var3 = 0;
            if (LoadMap.specMap == 4) {
               var3 = 3;
            }

            mFont.tahoma_7b_black.drawString(var1, "" + LoadMap.AA(LoadMap.AX), super.x, super.y - 22 - super.CH / 10 - var3, 2);
         } else {
            MainImage var6;
            if (this.AA == 98 && GameScreen.ClanDao != null && (var6 = Potion.AC(GameScreen.ClanDao.idIcon)) != null && var6.img != null) {
               byte var4 = 0;
               if ((this.AC + GameCanvas.gameTick / this.AD) % 2 == 1) {
                  var4 = -2;
               }

               if (var6.frame == -1) {
                  var6.set_Frame();
               }

               if (var6.frame <= 1) {
                  var1.drawRegion((mImage)var6.img, super.x + var4 + 2, super.y - 52, 3);
               } else {
                  byte var5;
                  if (this.AJ >= var2.frame - 1) {
                     var5 = 15;
                  } else {
                     var5 = 3;
                  }

                  if (CRes.abs(GameCanvas.gameTick - this.AI) > var5) {
                     ++this.AJ;
                     if (this.AJ >= var2.frame) {
                        this.AJ = 0;
                     }

                     this.AI = GameCanvas.gameTick;
                  }

                  var1.drawRegion(var6.img, 0, this.AJ * var6.AB, var6.AB, var6.AB, 0, super.x + var4 + 2, super.y - 52, 3);
               }
            }
         }

         if (super.MU > 0) {
            if (super.MU == 1) {
               AvMain.fraEventMoon.drawFrameNew(0 + AH[this.AG][3] * 3, super.x + AH[this.AG][1], super.y + AH[this.AG][2] - 3 + (this.AC + GameCanvas.gameTick / 7) % 2 * AH[this.AG][4], 0, 24, var1);
            }

            if (super.MU == 2) {
               AvMain.fraEventMoon.drawFrameNew(1 + GameCanvas.gameTick / 4 % 2 + AH[this.AG][3] * 3, super.x + AH[this.AG][1], super.y + AH[this.AG][2] - 3 + (this.AC + GameCanvas.gameTick / 7) % 2 * AH[this.AG][4], 0, 24, var1);
            }
         }

         if (super.ID == -993 && GameScreen.player.RZ) {
            AvMain.fraNauBanh.drawFrame(2, super.x, super.y - super.hOne - 30, 0, 3, var1);
            var1.setColor(-16711822);
            var1.fillRect(super.x + 20, super.y - super.hOne - 30 - 3, 3, 7);
            var1.setColor(-196864);
            if (GameScreen.player.RY < 87) {
               var1.fillRect(super.x - 43 + GameScreen.player.RY, super.y - super.hOne - 30 - 3, 1, 7);
            }
         }
      }

      if (this.AA != 99 && this.AA != 98) {
         this.AA(var1, super.colorName, 0);
      }

   }

   public final void AA(mGraphics var1, byte var2, int var3) {
      if (!GameScreen.getIsOffAdmin((byte)0)) {
         if (GameScreen.objFocus == null || GameScreen.objFocus != this || var3 != 0) {
            if (this.AA != 99 && this.AA != 98) {
               byte var4 = 0;
               if (super.Action == 4) {
                  var4 = 5;
               }

               int var9 = super.y - super.dy - super.hOne - 18 + var4;
               int var5;
               if (super.IT > 51) {
                  var1.drawRegion(AvMain.imgCombo, 0, 2, 4, 13, 0, super.x - super.IT / 2 + 2, var9 + 6, 3);
                  var1.drawRegion(AvMain.imgCombo, 47, 2, 4, 13, 0, super.x + super.IT / 2 - 2, var9 + 6, 3);
                  var5 = (super.IT - 8) / 40;

                  for(int var6 = 0; var6 <= (super.IT - 8) / 40; ++var6) {
                     if (var6 == var5) {
                        var1.drawRegion(AvMain.imgCombo, 4, 2, (super.IT - 8) % 40, 13, 0, super.x - super.IT / 2 + 4 + 20 + var6 * 40 - 20 + (super.IT - 8) % 40 / 2, var9 + 6, 3);
                     } else {
                        var1.drawRegion(AvMain.imgCombo, 4, 2, 40, 13, 0, super.x - super.IT / 2 + 4 + 20 + var6 * 40, var9 + 6, 3);
                     }
                  }
               } else {
                  var1.drawRegion(AvMain.imgCombo, 0, 2, 51, 13, 0, super.x, var9 + 6, 3);
               }

               var5 = super.x;
               if (super.MI > 0) {
                  var5 -= 10;
               }

               if (super.MP >= 0) {
                  AvMain.fraIconNpc.drawFrame(super.MP, var5, super.y - super.hOne - 30, 0, 3, var1);
                  var5 += 20;
               } else {
                  var5 = super.x;
               }

               if (super.MI > 0 && GameCanvas.gameTick % 14 < 12) {
                  AvMain.fraQuest.drawFrame(super.MI, var5, super.y - super.hOne - 31, 0, 3, var1);
               }

               if (var3 == 1) {
                  AvMain.FontBorderColor(var1, super.name, super.x, var9, 2, (int)var2, (int)7);
               } else {
                  AvMain.AA(var1, super.name, super.x, var9, 2, (byte)var2);
               }

               this.AJ(var1);
            }
         }
      }
   }

   public final void AB(mGraphics var1, int var2, int var3, int var4) {
      if (this.AA == 1) {
         if (this.AB == 1) {
            MainObject.AA(var1, super.BS, super.BW, super.BX, var2, var3 + 38, var4);
         } else {
            MainImage var7;
            if ((var7 = ObjectData.getImageAll(super.IdIcon, ObjectData.hashImageNPC, (short)5000)).img != null) {
               if (super.wOne == 0) {
                  super.hOne = mImage.getImageHeight(var7.img.image) / super.IO;
                  super.wOne = mImage.getImageWidth(var7.img.image);
               }

               int var5 = 18;
               int var6 = 18;
               if (18 > super.hOne) {
                  var5 = super.hOne;
               }

               if (18 > super.wOne) {
                  var6 = super.wOne;
               }

               var1.drawRegion(var7.img, super.wOne / 2 - var6 / 2 + this.AF, this.AE, var6, var5, 0, var2, var3, 3);
            }

         }
      }
   }

   public final void AG() {
      if (super.ID != -119) {
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
   }

   public final void update() {
      this.BJ();
      this.AF(super.LG);
      if (LoadMap.specMap == 4) {
         this.AG();
      }

      if (GameScreen.objFocus != null && GameScreen.objFocus == this && this.AA == 99 && GameCanvas.gameTick % 100 == 0) {
         GlobalService.getInstance().AK();
      }

   }

   public final void BD() {
      if (this.AA == 98) {
         if (GameScreen.ClanDao != null) {
            if (GameScreen.player.clan != null && GameScreen.player.clan.ID == GameScreen.ClanDao.ID) {
               GlobalService.getInstance().AD(super.ID);
               return;
            }

            MsgDialog var1;
            (var1 = new MsgDialog()).AA(GameScreen.ClanDao);
            GameCanvas.AA((MainDialog)var1);
            return;
         }
      } else {
         if (this.AA != 2 && this.AA != 1) {
            GlobalService.getInstance().AD(super.ID);
            return;
         }

         this.BE();
      }

   }

   public final iCommand getCenterCmd() {
      return GameScreen.BY;
   }

   public final void a_() {
      if (Interface_Game.CA.size() <= 1) {
         this.setFireObject(2);
         Interface_Game.CO = 0;
      }

   }

   public final void setFireObject(int var1) {
      if (var1 == 2) {
         this.BD();
      }

   }

   public final int BQ() {
      return this.AA;
   }

   public final void BY() {
      if (super.MU == 1 || super.MU == 2) {
         for(int var1 = 0; var1 < AH.length; ++var1) {
            if (super.IdIcon == AH[var1][0]) {
               this.AG = var1;
               return;
            }
         }
      }

   }
}
