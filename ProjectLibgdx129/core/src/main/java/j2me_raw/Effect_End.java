public final class Effect_End extends MainEffect {
   private byte typeSub = 0;
   private mVector VecEff = new mVector();
   private mVector vecSubEff = new mVector();
   private int randomf = 0;
   private int randomf2 = 0;
   private int lengthM;
   private MainObject objTo;
   private boolean isUpdateNormal = true;
   private int[] mPlayFrame;
   private int[][] mPlayFrameVip;
   private long time;
   private int timeRemove;
   private int[] mposy;
   private int maxsize = 0;
   private short idPotion = 0;
   private int[] arrFrame = new int[]{0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4};
   private int tframe;
   private static int[][] colorStar = new int[][]{{-466912, -479160, -1}, {-9732096, -4133256, -1}, {-14369793, -4790017, -1}};
   private int[] colorpaint;
   private int indexColorStar = 0;
   private int xline;
   private int yline;

   public Effect_End(short var1, byte var2, int var3, int var4, int var5, byte var6, MainObject var7) {
      super.f = 0;
      super.typeEffect = var1;
      this.typeSub = var2;
      super.x = var3;
      super.y = var4;
      super.Dir = var6;
      this.timeRemove = var5;
      this.time = GameCanvas.timeNow;
      super.objMainEff = var7;
      super.numNextFrame = 1;
      this.create_Effect();
   }

   public Effect_End(short var1, byte var2, int var3, int var4, byte var5, MainObject var6) {
      super.f = 0;
      super.typeEffect = var1;
      this.typeSub = var2;
      super.x = var3;
      super.y = var4;
      super.Dir = var5;
      super.objMainEff = var6;
      super.numNextFrame = 1;
      this.create_Effect();
   }

   public Effect_End(short var1, byte var2, int var3, int var4, byte var5) {
      super.f = 0;
      super.typeEffect = 169;
      this.typeSub = 0;
      super.x = var3;
      super.y = var4;
      super.Dir = 0;
      super.numNextFrame = 1;
      this.create_Effect();
   }

   public Effect_End(short var1, byte var2, int var3, int var4, int var5, int var6, byte var7, MainObject var8) {
      super.f = 0;
      super.typeEffect = var1;
      this.typeSub = var2;
      super.x = var3;
      super.y = var4;
      super.toX = var5;
      super.toY = var6;
      super.Dir = var7;
      super.objMainEff = var8;
      super.numNextFrame = 1;
      this.create_Effect();
   }

   public Effect_End(short var1, byte var2, int var3, int var4, short var5, byte var6, byte var7, MainObject var8, int var9) {
      super.f = 0;
      super.typeEffect = var1;
      this.typeSub = var2;
      super.x = var3;
      super.y = var4;
      super.Dir = var7;
      super.objMainEff = var8;
      this.objTo = MainObject.get_Object((int)var5, (byte)var6);
      if (this.objTo == null) {
         this.removeEff();
      } else {
         this.timeRemove = var9;
         this.time = GameCanvas.timeNow;
         super.toX = this.objTo.x;
         super.toY = this.objTo.y;
         this.setAngle();
         super.numNextFrame = 1;
         this.create_Effect();
      }
   }

   public Effect_End(short var1, byte var2, int var3, int var4, short var5, byte var6, short var7, byte var8, MainObject var9, int var10) {
      super.f = 0;
      super.typeEffect = var1;
      this.typeSub = var2;
      super.x = var3;
      super.y = var4;
      super.Dir = var8;
      super.objMainEff = var9;
      this.objTo = MainObject.get_Object((int)var5, (byte)var6);
      if (this.objTo == null) {
         this.removeEff();
      } else {
         this.timeRemove = 0;
         this.time = GameCanvas.timeNow;
         this.idPotion = var7;
         super.toX = this.objTo.x;
         super.toY = this.objTo.y;
         this.setAngle();
         super.numNextFrame = 1;
         this.create_Effect();
      }
   }

   private void create_Effect() {
      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      int var3;
      MainSkill var4;
      Point point6;
      Point var7;
      switch(super.typeEffect) {
      case 0:
         super.numNextFrame = 2;
         super.fRemove = 6;
         super.fraImgEff = new FrameImage(3, 30, 50);
         if (this.typeSub == 1) {
            super.fRemove = 5;
            super.fraImgEff = new FrameImage(449, 5);
            return;
         }
         break;
      case 1:
         super.numNextFrame = 1;
         super.fRemove = 4;
         super.fraImgEff = new FrameImage(6, 38, 38);
         return;
      case 2:
         super.fraImgEff = new FrameImage(195, 40, 27, 40, 27);
         super.fraImgSubEff = new FrameImage(32, 45, 45, 34, 34);
         super.fRemove = 5;
         return;
      case 3:
         super.fraImgEff = new FrameImage(18, 20, 21);
         super.numNextFrame = 1;
         super.fRemove = 4;
         return;
      case 4:
         super.fraImgEff = new FrameImage(19, 50, 54, 35, 38);
         if (this.typeSub > 0) {
            super.fraImgEff = new FrameImage(107, 50, 54, 38, 41);
            GameScreen.addEffectEnd((short)47, (byte)(this.typeSub - 1), super.x, super.y, super.Dir, super.objMainEff);
         }

         super.numNextFrame = 1;
         super.fRemove = 5;
         return;
      case 5:
         super.fraImgEff = new FrameImage(21, 20, 21);
         super.numNextFrame = 1;
         super.fRemove = 4;
         return;
      case 6:
         super.vMax = 100;
         super.fraImgEff = new FrameImage(298, 24, 24, 6);
         if (this.typeSub == 1 || this.typeSub == 2 || this.typeSub == 3) {
            if (this.typeSub == 2 || this.typeSub == 3) {
               super.fraImgEff = new FrameImage(299, 26, 26, 2);
            }

            super.fraImgSubEff = new FrameImage(27, 24, 24);
            if (this.typeSub == 3) {
               super.fraImgSub2Eff = new FrameImage(326, 26, 26, 3);
            }
         }

         if (super.objMainEff != null) {
            super.BE = super.objMainEff.MW;
         }

         super.numNextFrame = 1;
         super.fRemove = CRes.random(12, 20);
         super.x *= 100;
         super.y *= 100;
         this.createEndBungmerang();
         return;
      case 7:
         super.objFireMain = super.objMainEff;
         super.fraImgEff = new FrameImage(12, 15, 15);
         super.vMax = 24;
         super.fRemove = 20;
         super.y -= 6;
         if (super.Dir == 0) {
            super.x -= 20;
         } else {
            super.x += 20;
         }

         super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
         return;
      case 8:
         super.fraImgEff = new FrameImage(30, 38, 38);
         super.numNextFrame = 2;
         super.fRemove = 6;
         return;
      case 9:
         super.fraImgEff = new FrameImage(34, 62, 64, 45, 46);
         super.numNextFrame = 2;
         super.fRemove = 6;
         return;
      case 10:
         super.fraImgEff = new FrameImage(15 + CRes.random(3), 55, 55, 40, 40);
         super.numNextFrame = 2;
         super.fRemove = 6;
         return;
      case 11:
         this.typeSub = (byte)(14 + (CRes.random(2) << 1));
         (var7 = new Point()).x = super.x;
         var7.y = super.y;
         var7.fRe = 6;
         if (this.typeSub == 14) {
            var7.AY = new FrameImage(this.typeSub, 55, 55);
         } else {
            var7.AY = new FrameImage(this.typeSub, 55, 55, 40, 40);
         }

         this.VecEff.addElement(var7);
         super.numNextFrame = 2;
         super.fRemove = 6;
         GameScreen.addEffectEnd((short)93, 0, super.x, super.y, super.Dir, super.objMainEff);
         return;
      case 12:
         super.fraImgEff = new FrameImage(36, 28, 28);
         if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(106, 28, 28);
            GameScreen.addEffectEnd((short)36, 0, super.x, super.y, super.Dir, super.objMainEff);
         }

         super.numNextFrame = 2;
         super.fRemove = 6;
         return;
      case 13:
         if (this.objTo != null) {
            super.toX = this.objTo.x;
            super.toY = this.objTo.y - this.objTo.hOne / 2;
         }

         var1 = super.toX - super.x;
         var2 = super.toY - super.y;
         if (this.typeSub != 3 && this.typeSub != 4 && this.typeSub != 5) {
            if (this.typeSub == 1 || this.typeSub == 2) {
               super.fraImgSubEff = new FrameImage(69, 14, 48);
               if (this.typeSub == 2) {
                  super.fraImgSub2Eff = new FrameImage(27, 24, 32);
               }
            }

            super.fraImgEff = new FrameImage(44, 25, 15);
         } else {
            super.fraImgSubEff = new FrameImage(69, 14, 48);
            super.fraImgSub2Eff = new FrameImage(27, 24, 32);
            super.fraImgEff = new FrameImage(275, 35, 25);
            if (this.typeSub == 4 || this.typeSub == 5) {
               super.fraImgEff = new FrameImage(318, 35, 25);
            }

            if (this.typeSub == 5) {
               super.fraImgSubEff = new FrameImage(410, 3);
            }
         }

         super.numNextFrame = 1;
         super.vMax = 12;
         this.create_Speed(var1, var2, (Point_Focus)null);
         this.randomf = CRes.random(super.fraImgEff.nFrame);
         return;
      case 14:
         super.fraImgEff = new FrameImage(42, 53, 48);
         super.vy = 4;
         super.fRemove = 4;
         super.numNextFrame = 2;
         return;
      case 15:
         super.objFireMain = super.objMainEff;
         super.fRemove = 15;
         return;
      case 16:
         super.fRemove = 3;
         if (this.typeSub < 0) {
            super.fraImgEff = new FrameImage(301, 80, 32, 3);
         } else {
            super.fraImgEff = new FrameImage(300, 80, 25, 3);
         }

         if (super.objMainEff != null) {
            super.BE = super.objMainEff.MW;
         }

         super.numNextFrame = 2;
         super.vx = -4;
         if (super.Dir == 2) {
            super.vx = 4;
            return;
         }
         break;
      case 17:
         super.fraImgEff = new FrameImage(39, 53, 28);
         super.fRemove = this.typeSub;
         super.levelPaint = -1;
         return;
      case 18:
         super.fraImgEff = new FrameImage(33, 62, 42, 40, 27);
         super.fRemove = 10;
         super.numNextFrame = 2;
         super.vy = -2;
         return;
      case 19:
         if (this.typeSub == 0) {
            super.fraImgEff = new FrameImage(14, 55, 55);
            super.fraImgSubEff = new FrameImage(15, 55, 55, 40, 40);
         } else if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(16, 55, 55, 40, 40);
            super.fraImgSubEff = new FrameImage(17, 55, 55, 40, 40);
         }

         super.fRemove = 8;
         super.numNextFrame = 2;
         return;
      case 20:
         super.fraImgEff = new FrameImage(49, 70, 70, 50, 50);
         super.numNextFrame = 2;
         super.fRemove = 6;
         this.randomf = CRes.random(8);
         return;
      case 21:
         this.createXuyenGiap();
         return;
      case 22:
         this.createHut_MP_HP();
         return;
      case 23:
         super.vMax = 14;
         this.createPhanDamage();
         this.create_Speed(var1, var2, (Point_Focus)null);
         return;
      case 24:
         this.createFocustouch();
         return;
      case 25:
         if (this.typeSub == 4) {
            super.fraImgEff = new FrameImage(281, 29, 50);
         } else {
            super.fraImgEff = new FrameImage(68, 28, 44);
            if (this.typeSub == 1 || this.typeSub == 2 || this.typeSub == 3) {
               super.fraImgSubEff = new FrameImage(69, 14, 48);
               if (this.typeSub == 2 || this.typeSub == 3) {
                  super.fraImgSub2Eff = new FrameImage(27, 24, 32);
               }
            }
         }

         super.x1000 = super.x - 5;
         if (super.Dir == 0) {
            super.x1000 = super.x + 5;
         }

         super.numNextFrame = 2;
         super.fRemove = 6;
         if (this.typeSub == 3) {
            super.fRemove = 4;
            return;
         }
         break;
      case 26:
         if (this.typeSub == 2) {
            super.fraImgEff = new FrameImage(278, 70, 70);
         } else if (this.typeSub == 3) {
            super.fraImgEff = new FrameImage(279, 70, 70);
         } else if (this.typeSub == 4) {
            super.fraImgEff = new FrameImage(420, 70, 70);
         } else if (this.typeSub == 5) {
            super.fraImgEff = new FrameImage(466, 4);
         } else {
            super.fraImgEff = new FrameImage(70, 52, 60, 36, 41);
         }

         super.numNextFrame = 1;
         super.fRemove = 6;
         this.mPlayFrame = new int[]{0, 1, 2, 0, 3};
         if (this.typeSub == 1) {
            super.fRemove = 4;
            this.mPlayFrame = new int[]{0, 2, 0};
            return;
         }
         break;
      case 27:
         if (this.objTo != null) {
            super.toX = this.objTo.x;
            super.toY = this.objTo.y;
         }

         var1 = super.toX - super.x;
         var2 = super.toY - super.y;
         super.fraImgEff = new FrameImage(72, 32, 60);
         super.fraImgSubEff = new FrameImage(27, 24, 32);
         super.numNextFrame = 2;
         super.vMax = 12;
         this.create_Speed(var1, var2, (Point_Focus)null);
         return;
      case 28:
         super.numNextFrame = 1;
         super.fraImgEff = new FrameImage(9, 63, 21);
         super.fRemove = 24;
         super.vy = -3;
         if (super.objFireMain != null) {
            super.x = super.objFireMain.x;
            super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
         }

         GameScreen.addEffectEnd((short)50, 0, super.x, super.y, super.Dir, super.objMainEff);
         return;
      case 29:
         super.x1000 = super.x;
         super.y1000 = super.y;
         super.fRemove = 20;
         super.fraImgEff = new FrameImage(74, 60, 74);
         super.fraImgSubEff = new FrameImage(75, 82, 25);
         super.fraImgSub2Eff = new FrameImage(18, 20, 21);
         return;
      case 30:
         this.indexColorStar = this.typeSub;
         super.x1000 = super.x * 1000;
         super.y1000 = super.y * 1000;
         super.fRemove = CRes.random(4, 6);
         super.vMax = 5;
         this.xline = 10;
         this.yline = 20;
         this.maxsize = 4;
         this.create_Star_Line_In(super.vMax, this.xline, this.yline, 0, this.maxsize);
         return;
      case 31:
         this.createCharTeleport();
         return;
      case 32:
         this.createCharTeleportNew();
         return;
      case 33:
         super.fRemove = 8;
         super.objFireMain = super.objMainEff;
         return;
      case 34:
         super.fraImgEff = new FrameImage(104, 30, 30);
         super.numNextFrame = 2;
         super.fRemove = 6;
         return;
      case 35:
         super.fraImgEff = new FrameImage(89, 28, 44);
         super.numNextFrame = 2;
         super.fRemove = 6;
         return;
      case 36:
         if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(283, 22, 28);
         } else {
            super.fraImgEff = new FrameImage(78, 22, 28);
         }

         super.vx = CRes.random_Am_0(2);
         super.vy = -3;
         super.fRemove = CRes.random(7, 12);
         return;
      case 37:
         super.x1000 = super.x;
         super.y1000 = super.y;
         super.fRemove = 20;
         super.fraImgEff = new FrameImage(69, 14, 48);
         if (this.typeSub == 1) {
            super.fraImgSubEff = new FrameImage(103, 35, 19, 35, 19);
            super.fraImgSub2Eff = new FrameImage(18, 20, 21);
            return;
         }

         super.fraImgSubEff = new FrameImage(102, 35, 19);
         return;
      case 38:
         super.fRemove = 5;
         if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(104, 30, 30);
            return;
         }

         if (this.typeSub == 2) {
            super.fraImgEff = new FrameImage(243, 36, 36);
            return;
         }

         if (this.typeSub == 3) {
            super.fraImgEff = new FrameImage(82, 30, 30);
            return;
         }

         super.fraImgEff = new FrameImage(82, 30, 30);
         return;
      case 39:
         super.fRemove = 8;
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne - 20;
         }

         if (this.typeSub == 3) {
            super.fraImgEff = new FrameImage(25, 80, 40);
            return;
         }

         if (this.typeSub == 2) {
            super.fraImgEff = new FrameImage(328, 38, 27);
            return;
         }

         super.fraImgEff = new FrameImage(95, 32, 27);
         return;
      case 40:
         super.fRemove = 6;
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne - 20;
         }
         if (this.typeSub == 2) {
            super.fraImgEff = new FrameImage(328, 38, 27);
            super.fraImgSubEff = new FrameImage(329, 56, 31);
         } else if (this.typeSub == 4) {
            super.fraImgEff = new FrameImage(462, 4);
            super.fraImgSubEff = new FrameImage(329, 56, 31);
         } else {
            super.fraImgEff = new FrameImage(95, 32, 27);
            super.fraImgSubEff = new FrameImage(97, 56, 31);
         }

         super.fraImgSub2Eff = new FrameImage(96, 28, 79, 1);
         return;
      case 41:
         super.fRemove = 4;
         super.fraImgEff = new FrameImage(93, 64, 48, 41, 31);
         return;
      case 42:
         super.fRemove = 4;
         super.fraImgEff = new FrameImage(98, 78, 70, 56, 50);
         return;
      case 43:
      case 67:
      case 94:
      case 165:
      default:
         break;
      case 44:
         super.fRemove = 9;
         super.objFireMain = super.objMainEff;
         return;
      case 45:
         super.levelPaint = -1;
         super.fraImgEff = new FrameImage(168, 112, 69, 89, 55);
         super.fRemove = 8;
         super.numNextFrame = 2;
         return;
      case 46:
      case 159:
         this.createClassNami();
         return;
      case 47:
         this.create_Sanji6();
         return;
      case 48:
         this.createUssop9();
         return;
      case 49:
         this.createUssop10();
         return;
      case 50:
         this.createUssop11();
         return;
      case 51:
         super.objFireMain = super.objMainEff;
         super.fRemove = 16;
         if (super.Dir == 0) {
            super.x -= 20;
         } else {
            super.x += 20;
         }

         super.y = super.objFireMain.y - super.objFireMain.hOne / 2;
         super.fraImgSubEff = new FrameImage(23, 24, 24);
         super.fraImgEff = new FrameImage(27, 24, 24);
         return;
      case 52:
         super.fraImgEff = new FrameImage(118, 62, 64, 47, 48);
         super.fRemove = 8;
         super.numNextFrame = 2;
         GameScreen.addEffectEnd((short)63, 0, super.x, super.y, super.Dir, super.objMainEff);
         return;
      case 53:
         super.fraImgEff = new FrameImage(121, 32, 32);
         if ((var1 = (int)(12.0D / (1.0D + (double)(GameCanvas.percentUPCV + GameCanvas.CY) / 100.0D))) <= 0) {
            var1 = 1;
         }

         super.fRemove = var1;
         super.numNextFrame = 2;
         return;
      case 54:
         this.createEND_Lu_S1_Final();
         return;
      case 55:
         super.fraImgEff = new FrameImage(100, 15, 20);
         super.fRemove = 10;
         super.numNextFrame = 2;
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y;
            return;
         }
         break;
      case 56:
         super.fraImgEff = new FrameImage(8, 40, 47, 40, 47);
         super.fRemove = 4;
         if (this.objTo != null && this.objTo.Action != 4) {
            this.objTo.isTanHinh = true;
            return;
         }
         break;
      case 57:
         super.fraImgEff = new FrameImage(107, 50, 54, 38, 41);
         super.fRemove = 10;
         super.numNextFrame = 2;
         return;
      case 58:
         super.fraImgEff = new FrameImage(138, 62, 64, 47, 48);
         super.fRemove = 8;
         super.numNextFrame = 2;
         GameScreen.addEffectEnd((short)63, 0, super.x, super.y, super.Dir, super.objMainEff);
         return;
      case 59:
         this.createRock();
         return;
      case 60:
         this.createHachi2();
         return;
      case 61:
         super.numNextFrame = 1;
         super.fRemove = 4;
         super.fraImgEff = new FrameImage(143, 38, 38);
         return;
      case 62:
         super.levelPaint = -1;
         if (LoadMap.idTile == 2) {
            super.fraImgEff = new FrameImage(154, 20, 15);
         } else {
            super.fraImgEff = new FrameImage(151, 20, 15);
         }

         super.fRemove = CRes.abs(super.toX - super.x) / 10 + 20;
         if (super.fRemove % 2 == 0) {
            ++super.fRemove;
         }

         super.vx = 10;
         if (super.toX < super.x) {
            super.vx = -10;
            return;
         }
         break;
      case 63:
         super.levelPaint = -1;
         if (this.typeSub <= 2) {
            super.frame = CRes.random(3);
         } else {
            super.frame = this.typeSub;
         }

         super.fraImgEff = new FrameImage(245, 25, 21, 3);
         super.fRemove = CRes.random(30, 50);
         return;
      case 64:
         if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(279, 70, 70);
         } else if (this.typeSub == 2) {
            super.fraImgEff = new FrameImage(420, 70, 70);
         } else if (this.typeSub == 3) {
            super.fraImgEff = new FrameImage(466, 4);
         } else {
            super.fraImgEff = new FrameImage(169, 52, 60, 40, 46);
         }

         super.numNextFrame = 1;
         super.fRemove = 4;
         return;
      case 65:
         super.fraImgEff = new FrameImage(171, 153, 84, 100, 54);
         super.numNextFrame = 2;
         super.fRemove = 8;
         super.levelPaint = -1;
         return;
      case 66:
         super.levelPaint = -1;
         super.fraImgEff = new FrameImage(226, 13, 11, 3);
         super.fRemove = CRes.random(15, 20);
         return;
      case 68:
         super.fraImgEff = new FrameImage(177, 48, 56, 40, 47);
         super.fRemove = 8;
         super.numNextFrame = 2;
         GameScreen.addEffectEnd((short)59, 0, super.x, super.y - 10, super.Dir, super.objMainEff);
         return;
      case 69:
         super.timeBegin = GameCanvas.timeNow;
         this.createUrgot_4(2);
         super.timeEnd = 3000;
         return;
      case 70:
         super.fRemove = 40;
         this.createUrgot_4(5);
         return;
      case 71:
         super.fraImgEff = new FrameImage(190, 30, 30);
         super.fRemove = 6;
         super.numNextFrame = 2;
         return;
      case 72:
         super.fRemove = 3;
         super.fraImgEff = new FrameImage(191, 60, 17);
         super.numNextFrame = 2;
         super.vx = -3;
         if (super.Dir == 2) {
            super.vx = 3;
            return;
         }
         break;
      case 73:
         super.vMax = 2;
         super.fRemove = CRes.abs(super.toX - super.x) / super.vMax + 1;
         return;
      case 74:
         super.vMax = 2;
         super.fRemove = CRes.abs(super.toX - super.x) / super.vMax + 1;
         super.levelPaint = -1;
         return;
      case 75:
         super.vMax = 5;
         super.fraImgEff = new FrameImage(192, 25, 25);
         super.fraImgSubEff = new FrameImage(51, 9, 9);
         super.fraImgSub2Eff = new FrameImage(52, 5, 5);
         var1 = super.toX - super.x;
         var2 = super.toY - super.y;
         this.create_Speed(var1, var2, (Point_Focus)null);
         return;
      case 76:
         this.create_Upgrade_Thanh_Cong();
         return;
      case 77:
         super.numNextFrame = 2;
         super.fraImgEff = new FrameImage(85, 34, 34, 28, 28);
         if ((var1 = (int)(12.0D / (1.0D + (double)(GameCanvas.percentUPCV + GameCanvas.CY) / 100.0D))) <= 0) {
            var1 = 1;
         }

         super.fRemove = var1;
         return;
      case 78:
         super.vMax = 8;
         super.fraImgEff = new FrameImage(192, 25, 25);
         super.fraImgSubEff = new FrameImage(51, 9, 9);
         super.fraImgSub2Eff = new FrameImage(52, 5, 5);
         var1 = super.toX - super.x;
         var2 = super.toY - super.y;
         this.create_Speed(var1, var2, (Point_Focus)null);
         return;
      case 79:
         if (AvMain.GG == null) {
            AvMain.GG = new FrameImage(mImage.createImage("/interface/levelup.png"), 115, 35);
         }

         super.fraImgEff = AvMain.GG;
         super.vy = -3;
         if (this.typeSub % 2 == 1) {
            super.vy = -2;
         }

         super.fRemove = 18;
         super.frame = this.typeSub;
         return;
      case 80:
         super.fraImgEff = new FrameImage(11, 40, 50);
         super.fRemove = 4;
         super.numNextFrame = 2;
         return;
      case 81:
         super.fraImgEff = new FrameImage(106, 28, 28);
         super.fRemove = 6;
         super.numNextFrame = 2;
         return;
      case 82:
         super.fraImgEff = new FrameImage(198, 40, 40);
         super.fraImgSubEff = new FrameImage(85, 34, 34, 28, 28);
         super.fRemove = 6;
         super.numNextFrame = 2;
         return;
      case 83:
         this.createRevice();
         return;
      case 84:
         this.createGet_Up();
         return;
      case 85:
         this.indexColorStar = this.typeSub;
         super.x1000 = super.x * 1000;
         super.y1000 = super.y * 1000;
         super.fRemove = CRes.random(4, 6);
         super.vMax = 5;
         this.xline = 10;
         this.yline = 20;
         this.maxsize = 4;
         this.create_Star_Line_In(super.vMax, this.xline, this.yline, 10, this.maxsize);
         super.fraImgEff = new FrameImage(51, 9, 9);
         super.fraImgSubEff = new FrameImage(52, 5, 5);
         return;
      case 86:
         super.numNextFrame = 2;
         super.fRemove = 6;
         super.fraImgEff = new FrameImage(84, 64, 40);
         return;
      case 87:
         super.vMax = 100;
         super.fraImgEff = new FrameImage(80, 30, 15);
         super.numNextFrame = 2;
         super.fRemove = CRes.random(6, 12);
         super.x *= 100;
         super.y *= 100;
         this.createEndBungmerang();
         return;
      case 88:
         super.fraImgEff = new FrameImage(46, 70, 100, 49, 70);
         this.mPlayFrame = new int[]{0, 0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 4, 4};
         super.fRemove = this.mPlayFrame.length;
         GameScreen.addEffectEnd((short)17, CRes.random(20, 30), super.x, super.y, super.Dir, super.objMainEff);
         return;
      case 89:
         super.fraImgEff = new FrameImage(34, 62, 64, 45, 46);
         super.fRemove = 6;
         super.numNextFrame = 2;
         return;
      case 90:
         if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(195, 40, 27, 40, 27);
         } else {
            super.fraImgEff = new FrameImage(26, 40, 20);
         }

         super.numNextFrame = 2;
         super.fRemove = 4;
         return;
      case 91:
         super.fRemove = 4;
         this.mPlayFrame = new int[]{4, 3, 2, 0};
         super.fraImgEff = new FrameImage(121, 32, 32);
         return;
      case 92:
         super.fRemove = 4;
         this.mPlayFrame = new int[]{4, 4, 5, 5};
         super.fraImgEff = new FrameImage(85, 34, 34, 28, 28);
         return;
      case 93:
         this.createEffectSkill1();
         return;
      case 95:
         var1 = CRes.random(5, 8);

         for(var2 = 0; var2 < var1; ++var2) {
            Point var8;
            (var8 = new Point(CRes.random_Am_0(20), CRes.random(10))).y2 = var8.y;
            var8.vy = -CRes.random(2, 4);
            this.VecEff.addElement(var8);
         }

         return;
      case 96:
         this.createClassUssop();
         return;
      case 97:
         this.createClassZoro();
         return;
      case 98:
         super.fraImgEff = new FrameImage(92, 64, 126, 45, 89, 1);
         super.fraImgSubEff = new FrameImage(99, 32, 32);
         super.fRemove = 16;
         return;
      case 99:
         super.fraImgEff = new FrameImage(201, 64, 50, 45, 35);
         super.fraImgSubEff = new FrameImage(202, 40, 92, 30, 69, 1);
         super.fraImgSub2Eff = new FrameImage(99, 32, 32);
         super.fRemove = 12;
         return;
      case 100:
         super.fraImgEff = new FrameImage(144, 37, 55);
         super.numNextFrame = 3;
         super.fRemove = 6;
         return;
      case 101:
         this.createChopper();
         return;
      case 102:
         this.createKuromarimo();
         return;
      case 103:
         this.createMr3_1();
         return;
      case 104:
         this.createLittleHpBoss();
         return;
      case 105:
         this.createLittleDamBoss();
         return;
      case 106:
         super.fRemove = 10;
         super.fraImgEff = new FrameImage(101, 40, 47);
         return;
      case 107:
         this.createRock();
         return;
      case 108:
         if (GameCanvas.isLowGraOrWP_PvP()) {
            super.isStop = true;
         }

         this.createPartical();
         return;
      case 109:
         if (GameCanvas.isLowGraOrWP_PvP()) {
            super.isStop = true;
         }

         super.fraImgEff = new FrameImage(223, 19, 15);
         super.numNextFrame = 2;
         super.fRemove = super.fraImgEff.nFrame * super.numNextFrame;
         super.y -= super.fraImgEff.frameHeight;
         if (super.Dir == 2) {
            super.x -= super.fraImgEff.frameWidth;
            return;
         }
         break;
      case 110:
      case 115:
         if (GameCanvas.isLowGraOrWP_PvP()) {
            super.isStop = true;
         }

         this.createRock();
         return;
      case 111:
         super.fRemove = 60;
         super.fraImgEff = new FrameImage(238, 30, 73);
         super.fraImgSubEff = new FrameImage(67, 3, 25, 1);
         super.fraImgSub2Eff = new FrameImage(239, 38, 22, 38, 22);

         for(var3 = 0; var3 < 3; ++var3) {
            (var7 = new Point(CRes.random_Am_0(20), -5 + CRes.random(10))).vy = -CRes.random(12, 20);
            var7.frame = CRes.random(super.fraImgSubEff.nFrame);
            this.VecEff.addElement(var7);
         }

         return;
      case 112:
         super.levelPaint = -1;
         super.fraImgEff = new FrameImage(246, 49, 21, 35, 15, 4);
         super.fRemove = 40;
         return;
      case 113:
         super.fraImgEff = new FrameImage(252, 62, 64, 40, 41);
         super.fraImgSubEff = new FrameImage(174, 40, 40, 4);
         super.vMax = 12;
         if (this.typeSub == 2) {
            super.vMax = 14;
         }

         super.fRemove = 10;
         super.gocT_Arc = CRes.random(90);
         return;
      case 114:
         super.vx = 3;
         if (super.Dir == 0) {
            super.vx = -3;
         }

         super.fraImgEff = new FrameImage(259, 30, 14);
         super.fRemove = 4;
         super.numNextFrame = 2;
         return;
      case 116:
         if (this.typeSub == 1) {
            super.levelPaint = -1;
         }

         super.fraImgEff = new FrameImage(268, 24, 32);
         super.fRemove = 20;
         return;
      case 117:
         super.levelPaint = -1;
         super.fraImgEff = new FrameImage(269, 24, 10);
         if (CRes.random(3) == 0) {
            super.frame = 1;
         }

         super.fRemove = CRes.random(16, 30);
         return;
      case 118:
         super.fraImgEff = new FrameImage(144, 37, 55);
         super.numNextFrame = 3;
         super.fRemove = 6;
         return;
      case 119:
         this.createEndLuS1L4();
         return;
      case 120:
         this.createUssopS3_Lv4();
         return;
      case 121:
         this.mPlayFrame = new int[]{-1448732, -659216, -198408, -1, -1};
         super.fRemove = 16;
         return;
      case 122:
         super.fRemove = 10;
         super.numNextFrame = 2;
         super.fraImgEff = new FrameImage(289, 60, 60, 3);
         return;
      case 123:
         super.fRemove = 8;
         super.numNextFrame = 2;
         super.fraImgEff = new FrameImage(290, 60, 60, 4);
         if (this.typeSub > 3) {
            this.typeSub = 3;
            return;
         }
         break;
      case 124:
         super.fraImgEff = new FrameImage(246, 49, 21, 4);
         this.mPlayFrame = new int[]{3, 1, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 1, 2, 3};
         super.fRemove = this.mPlayFrame.length;
         super.levelPaint = -1;
         return;
      case 125:
         this.mPlayFrame = new int[]{0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0};
         super.fRemove = this.mPlayFrame.length;
         super.fraImgEff = new FrameImage(294, 57, 57, 3);
         return;
      case 126:
         this.create_Poke_Ok();
         return;
      case 127:
         this.create_Poke_Begin();
         return;
      case 128:
         this.create_Poke_Fail();
         return;
      case 129:
         return;
      case 130:
         this.createLOL_Tru_Tren();
         return;
      case 131:
         super.fraImgEff = new FrameImage(306, 38, 22, 3);
         return;
      case 132:
         super.fraImgEff = new FrameImage(309, 78, 70, 4);
         if (this.typeSub == 0) {
            super.fRemove = 4;
            super.numNextFrame = 1;
            super.vx = -6;
            if (super.Dir == 2) {
               super.vx = 6;
               return;
            }
         } else {
            super.fRemove = 16;
            super.numNextFrame = 1;
            super.vx = -16;
            if (super.Dir == 2) {
               super.vx = 16;
               return;
            }
         }
         break;
      case 133:
         if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(314, 96, 39);
         } else {
            super.fraImgEff = new FrameImage(312, 121, 77);
         }

         super.fRemove = 30;
         super.levelPaint = -1;
         return;
      case 134:
         super.fraImgEff = new FrameImage(314, 96, 39);
         super.fraImgSubEff = new FrameImage(315, 77, 54, 3);
         super.fRemove = 30;
         super.levelPaint = -1;
         return;
      case 135:
         super.fraImgEff = new FrameImage(320, 62, 44);
         super.fRemove = 4;
         super.numNextFrame = 2;
         super.vx = -20;
         if (super.Dir == 2) {
            super.vx = 20;
            return;
         }
         break;
      case 136:
         super.fraImgEff = new FrameImage(321, 52, 70, 3);
         super.numNextFrame = 1;
         this.mPlayFrameVip = new int[][]{new int[1], {3}, new int[0], new int[0], new int[0], {1}, {4}, new int[0], new int[0], new int[0], {2}, {5}, new int[0], {3, 2, 1}, {0, 2, 4}, {3, 5, 4}, new int[0], {3, 2, 1}, {0, 2, 4}, {3, 5, 4}};
         super.fRemove = this.mPlayFrameVip.length;
         return;
      case 137:
         this.createZoro_S3();
         return;
      case 138:
         super.fRemove = 5;
         super.fraImgEff = new FrameImage(243, 36, 36, 4);
         return;
      case 139:
         if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(407, 52, 199, 1);
         } else {
            super.fraImgEff = new FrameImage(327, 38, 198, 1);
         }

         if (this.typeSub == 1) {
            this.mPlayFrame = new int[]{0, 0, -1, 1, -1, 0, 0, -1, 1, 1};
         } else {
            this.mPlayFrame = new int[]{0, 0, -1, 1, -1, 2, 2, -1, 3};
         }

         super.fRemove = this.mPlayFrame.length;
         return;
      case 140:
      case 167:
         this.create_US_S2_L5();
         return;
      case 141:
         super.frameSuper = this.typeSub;
         super.fraImgEff = new FrameImage(335, 80, 80, (byte)2, super.frameSuper);
         super.fRemove = 8;
         super.numNextFrame = 2;
         return;
      case 142:
         super.fraImgEff = new FrameImage(46, 70, 100, 49, 70);
         if (this.typeSub == 1) {
            this.mPlayFrame = new int[]{0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4};
         } else {
            this.mPlayFrame = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4};
         }

         super.fRemove = this.mPlayFrame.length;
         GameScreen.addEffectEnd((short)17, CRes.random(20, 30), super.x, super.y, super.Dir, super.objMainEff);
         return;
      case 143:
         if (this.typeSub >= 0 && this.typeSub <= 11) {
            super.fraImgEff = new FrameImage(338 + this.typeSub, 1);
            super.fRemove = 30;
            super.vy = -9 + CRes.random_Am_0(3);
            return;
         }

         super.isStop = true;
         return;
      case 144:
         this.indexColorStar = this.typeSub;
         super.levelPaint = -1;
         super.x1000 = super.x * 1000;
         super.y1000 = super.y * 1000;
         super.fRemove = CRes.random(4, 6);
         super.vMax = 7;
         this.xline = 15;
         this.yline = 20;
         this.maxsize = 8;
         this.create_Star_Line_In(super.vMax, this.xline, this.yline, 0, this.maxsize);
         return;
      case 145:
         this.indexColorStar = this.typeSub;
         super.levelPaint = -1;
         super.x1000 = super.x * 1000;
         super.y1000 = super.y * 1000;
         super.fRemove = CRes.random(4, 6);
         super.vMax = 5;
         this.xline = 10;
         this.yline = 15;
         this.maxsize = 4;
         this.create_Star_Line_In(super.vMax, this.xline, this.yline, 0, this.maxsize);
         return;
      case 146:
         super.fraImgEff = new FrameImage(85, 34, 34, 28, 28);
         var3 = 30;

         for(var1 = 0; var1 < 4; ++var1) {
            (point6 = new Point(super.x * 1000, super.y * 1000)).vx = CRes.getcos(var3) * 7;
            point6.vy = CRes.getsin(var3) * 7;
            this.VecEff.addElement(point6);
            if (var1 != 0 && var1 != 2) {
               var3 += 60;
            } else {
               var3 += 120;
            }
         }

         var3 = 0;

         for(var1 = 0; var1 < 4; ++var1) {
            (point6 = new Point(super.x * 1000, super.y * 1000)).vx = CRes.getcos(var3) * 12;
            point6.vy = CRes.getsin(var3) * 12;
            this.VecEff.addElement(point6);
            var3 += 90;
         }

         super.fRemove = 10;
         return;
      case 147:
         this.create_Lucci_L2();
         return;
      case 148:
         super.fraImgEff = new FrameImage(312, 121, 77);
         super.fRemove = 20;
         super.levelPaint = -1;
         return;
      case 149:
         super.fraImgEff = new FrameImage(327, 38, 198, 1);
         super.fRemove = 4;
         super.numNextFrame = 1;
         super.y = this.objTo.y;
         return;
      case 150:
         super.fraImgEff = new FrameImage(367, 35, 14);
         super.fRemove = 1000;
         super.numNextFrame = 2;
         super.y = this.objTo.y - this.objTo.hOne;
         return;
      case 151:
         super.fraImgEff = new FrameImage(87, 35, 35, 5);
         super.fRemove = 5;
         super.numNextFrame = 1;
         super.y = this.objTo.y - this.objTo.hOne / 2;
         return;
      case 152:
         super.fraImgEff = new FrameImage(368, 56, 92);
         super.fRemove = 1000;
         this.mPlayFrame = new int[]{-1, -1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1};
         super.y = this.objTo.y + 4;
         return;
      case 153:
         super.fraImgEff = new FrameImage(369, 37, 59);
         super.fraImgSubEff = new FrameImage(370, 14, 16);
         super.fraImgSub2Eff = new FrameImage(221, 5, 5, 4);
         super.fRemove = 250;
         this.objTo.Action = 2;
         MainSkill var6 = new MainSkill((short)9999, (short)256);
         this.objTo.plashNow = new Plash(var6, this.objTo, (mVector)null);
         super.x = this.objTo.x + 3;
         if (this.objTo.type_left_right == 0) {
            super.x = this.objTo.x - 3;
            return;
         }
         break;
      case 154:
      case 155:
         this.create_upgrade6();
         return;
      case 156:
         super.fraImgEff = new FrameImage(381, 64, 35);
         super.vy = -6;
         super.y = this.objTo.y - this.objTo.hOne;
         super.y1000 = 0;
         super.fRemove = 1000;
         return;
      case 157:
         super.fraImgEff = new FrameImage(382, 58, 80, 3);
         super.vx = CRes.random(3);
         if (super.Dir == 0) {
            super.vx = -super.vx;
         }

         super.y -= GameCanvas.gameTick % 10 << 1;
         super.fRemove = 15;
         super.f = CRes.random(4);
         super.levelPaint = -1;
         return;
      case 158:
         super.Dir = (byte)this.objTo.type_left_right;
         super.fraImgEff = new FrameImage(385, 15, 27);
         super.fraImgSubEff = new FrameImage(386, 12, 22);
         super.fraImgSub2Eff = new FrameImage(221, 5, 5, 4);
         super.fraImgSub3Eff = new FrameImage(18, 20, 21);
         super.fRemove = 130;
         this.objTo.Action = 2;
         var4 = new MainSkill((short)9999, (short)257);
         super.y -= 15;
         this.objTo.plashNow = new Plash(var4, this.objTo, (mVector)null);
         super.x = this.objTo.x + 25;
         super.x1000 = super.x - 14;
         if (super.Dir == 0) {
            super.x = this.objTo.x - 25;
            super.x1000 = super.x + 14;
         }

         super.y1000 = super.y;
         super.am_duong = 1;
         if (super.Dir == 2) {
            super.am_duong = -1;
            return;
         }
         break;
      case 160:
         this.create_EE_Valentine();
         return;
      case 161:
         this.create_EE_Valentine_stand();
         return;
      case 162:
         this.create_EE_LAW_HEART();
         return;
      case 163:
         super.fraImgEff = new FrameImage(392, 101, 44);
         super.levelPaint = -1;
         super.fRemove = 190;
         super.x1000 = super.x;
         super.y1000 = super.y + 24;
         super.timeBegin = GameCanvas.timeNow;
         return;
      case 164:
         this.create_Ice_Arc();
         super.levelPaint = -1;
         return;
      case 166:
         return;
      case 168:
         this.createUssopS3_Lv6();
         return;
      case 169:
         this.createEff_Firework();
         return;
      case 170:
         super.fraImgEff = new FrameImage(414, 8);
         super.fRemove = 16;
         super.levelPaint = -1;
         return;
      case 171:
         super.fraImgEff = new FrameImage(408, 4);
         super.fRemove = 8;
         super.numNextFrame = 2;
         if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(448, 10);
            super.fRemove = 10;
            super.numNextFrame = 1;
            return;
         }
         break;
      case 172:
         super.fraImgEff = new FrameImage(402, 4);
         super.fRemove = 12;
         if (this.typeSub == 1) {
            super.fraImgSubEff = new FrameImage(468, 6);
            super.fRemove = 18;
            return;
         }
         break;
      case 173:
         super.fraImgEff = new FrameImage(403, 2);
         super.fRemove = 4;
         super.numNextFrame = 2;
         if (this.typeSub == 1) {
            super.fRemove = 8;
            super.numNextFrame = 4;
            return;
         }
         break;
      case 174:
         super.fraImgEff = new FrameImage(411, 3);
         super.fRemove = 12;
         return;
      case 175:
         super.fRemove = 15;
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne - 20;
         }

         super.fraImgEff = new FrameImage(25, 80, 40);
         super.fraImgSubEff = new FrameImage(329, 56, 31);
         super.fraImgSub2Eff = new FrameImage(238, 30, 73);
         super.fraImgSub3Eff = new FrameImage(195, 2);
         return;
      case 176:
         super.fraImgEff = new FrameImage(71, 5);
         super.fRemove = 5;
         return;
      case 177:
         if (this.typeSub == 0) {
            super.fraImgEff = new FrameImage(mImage.createImage("/interface/pvp3_e.png"), 1);
         } else if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(mImage.createImage("/interface/pvp4_e.png"), 1);
         }

         super.vy = -3;
         if (this.typeSub % 2 == 1) {
            super.vy = -2;
         }

         super.fRemove = 18;
         super.frame = 0;
         return;
      case 178:
         super.fraImgEff = new FrameImage(mImage.createImage("/eff/goal.png"), 4);
         super.fRemove = 30;
         super.fraImgSub2Eff = new FrameImage(mImage.createImage("/eff/khungthanh.png"), 1);
         super.fraImgSubEff = new FrameImage(mImage.createImage("/eff/ball.png"), 4);
         super.x = this.objTo.x;
         super.y = this.objTo.y;
         super.toX = super.x + 200;
         super.toY = super.y - 30;
         super.vx = 5;
         super.vy = -1;
         super.vMax = 10;
         var1 = super.toX - super.x;
         var2 = super.toY - super.y - CRes.random(20);
         this.create_Speed(var1, var2, (Point_Focus)null);
         super.fRemove = 40;
         this.objTo.Dir = 2;
         this.objTo.Action = 2;
         var4 = new MainSkill((short)9999, (short)280);
         this.objTo.plashNow = new Plash(var4, this.objTo, (mVector)null);
         return;
      case 179:
         this.create_event_poke();
         break;
      case 180:
         super.fraImgEff = new FrameImage(446, 10);
         super.fraImgSubEff = new FrameImage(411, 3);
         super.numNextFrame = 2;
         super.fRemove = 20;
         (point6 = new Point()).x = super.x;
         point6.y = super.y - 20;
         if (super.Dir == 0) {
            point6.vx = -6;
         } else {
            point6.vx = 6;
         }

         point6.fRe = 20;
         this.VecEff.addElement(point6);
         return;
      case 181:
         super.fraImgEff = new FrameImage(443, 5);
         super.fRemove = 30;
         super.numNextFrame = 2;
         super.levelPaint = -1;
         return;
      case 182:
         this.create_End_Luffy_S1_L7();
         return;
      case 183:
         this.create_Ussop_S3_L7();
         return;
      case 184:
         super.fraImgEff = new FrameImage(464, 6);
         super.numNextFrame = 4;
         super.fRemove = 24;
         return;
      case 185:
         super.fRemove = 8;
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne - 20;
         }

         if (this.typeSub == 3) {
            super.fraImgEff = new FrameImage(25, 80, 40);
            return;
         }

         if (this.typeSub == 2) {
            super.fraImgEff = new FrameImage(462, 4);
            return;
         }

         super.fraImgEff = new FrameImage(95, 32, 27);
         return;
      }

   }

   private void create_EE_LAW_HEART() {
      GameScreen.beginPaintSpec();
      MainSkill skill = new MainSkill((short)9999, (short)265);
      this.objTo.Action = 2;
      this.objTo.plashNow = new Plash(skill, this.objTo, (mVector)null);
      if (this.objTo == GameScreen.player) {
         Player.isSendMove = false;
      }

      super.fraImgEff = new FrameImage(396, 20, 20);
      super.fraImgSubEff = new FrameImage(393, 110, 110);
      super.fraImgSub2Eff = new FrameImage(394, 126, 41);
      super.fraImgSub3Eff = new FrameImage(395, 69, 32);
      super.timeBegin = GameCanvas.timeNow;
      super.fRemove = 190;
      super.x1000 = super.x;
      super.y1000 = super.y + 24;
      super.x -= 30;
      super.y -= 60;
      super.vy = -2;
      super.vx = 0;
      GameScreen.addEffectEnd_ObjTo((short)163, 0, this.objTo.x, this.objTo.y, (short)this.objTo.ID, (byte)this.objTo.typeObject, (byte)0, this.objTo);
   }

   private void create_EE_Valentine_stand() {
      super.fraImgEff = new FrameImage(388, 8, 7, 4);
      super.fraImgSubEff = new FrameImage(389, 11, 11, 2);
      super.frame = CRes.random(2);
      this.mPlayFrameVip = new int[][]{{1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1}, {1, 1, 1, 1, 0, 1, 0, 1}, {1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1}, {1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1}};
      super.x = this.objTo.x + CRes.random_Am_0(10);
      super.y = this.objTo.y - this.objTo.hOne / 2 - CRes.random(10);
      super.vy = -2;
      super.vx = CRes.random_Am_0(3);
      this.randomf = -3 + CRes.random(6);
      this.randomf2 = CRes.random(this.mPlayFrameVip.length);
      this.lengthM = this.mPlayFrameVip[this.randomf2].length;
      super.fRemove = 25 + this.mPlayFrameVip[this.randomf2].length;
      if (CRes.random(5) == 0) {
         super.levelPaint = -1;
      }

   }

   private void create_EE_Valentine() {
      super.fraImgEff = new FrameImage(388, 8, 7, 4);

      for(int var1 = 0; var1 < 4; ++var1) {
         Point var2;
         (var2 = new Point()).x = this.objTo.x + CRes.random_Am_0(5);
         var2.y = this.objTo.y - this.objTo.hOne + 3 + var1 * 10 + CRes.random_Am_0(3) + GameCanvas.gameTick % 5 * 3;
         var2.vx = -(2 + CRes.random(2));
         var2.dis = 0;
         if (this.objTo.type_left_right == 0) {
            var2.vx = 2 + CRes.random(2);
            var2.dis = 2;
         }

         var2.x += var2.vx << 2;
         if (CRes.random(4) == 0) {
            var2.frame = CRes.random(4);
         } else {
            var2.frame = 2 + CRes.random(2);
         }

         var2.vy = -1;
         this.VecEff.addElement(var2);
      }

      super.fRemove = 17;
      if (CRes.random(5) != 0) {
         super.levelPaint = -1;
      }

   }

   private void create_upgrade6() {
      this.colorpaint = new int[]{-1436848, -2322885, -462555, -12913881, -14176781, -15437743, -3595841};
      super.vMax = 40;
      super.fRemove = 20;
      super.levelPaint = -1;
      int var1 = 270;

      for(int var2 = 0; var2 < 5; ++var2) {
         Point var3;
         (var3 = new Point()).dis = var1;
         var3.x = CRes.getcos(CRes.fixangle(var3.dis)) * super.vMax / 1000 + super.x;
         var3.y = CRes.getsin(CRes.fixangle(var3.dis)) * super.vMax / 1000 + super.y;
         this.VecEff.addElement(var3);
         var1 += 72;
      }

      if (super.typeEffect == 155) {
         super.fraImgEff = new FrameImage(220, 9, 9, 4);
      }

   }

   private void create_Lucci_L2() {
      super.am_duong = 1;
      if (super.Dir == 2) {
         super.am_duong = -1;
      }

      super.fraImgEff = new FrameImage(274, 23, 74, 3);
      super.frame = 0;
      super.vx = super.am_duong * 12;
      super.x1000 = super.x;
      super.x = super.x1000 - super.am_duong * 24;
      super.fRemove = 20;
      if (this.typeSub == 1) {
         super.fraImgSubEff = new FrameImage(273, 24, 24, 4);
         super.frame = 1;
      }

      if (super.frame == 0) {
         super.mframe = new int[]{1, 0, 1, 0, 1, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
      } else {
         super.mframe = new int[]{1, 0, 1, 0, 1, 0, 0, 1, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4};
      }
   }

   public final void paint(mGraphics var1) {
      try {
         Point var2;
         int var3;
         int var4;
         int var8;
         Point var9;
         Point var10;
         Point_Focus var12;
         switch(super.typeEffect) {
         case 0:
         case 1:
         case 3:
         case 4:
         case 5:
         case 8:
         case 10:
         case 12:
         case 18:
         case 24:
         case 28:
         case 34:
         case 36:
         case 41:
         case 42:
         case 53:
         case 55:
         case 57:
         case 61:
         case 71:
         case 77:
         case 80:
         case 81:
         case 86:
         case 89:
         case 90:
         case 114:
         case 118:
         case 135:
         case 143:
         case 150:
         case 171:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 2:
            if (super.f < 4) {
               super.fraImgEff.drawFrame(super.f / 2, super.x, super.y - 5, 0, 3, var1);
            }

            super.fraImgSubEff.drawFrame(super.f, super.x, super.y, 0, 33, var1);
            return;
         case 6:
            if (this.typeSub == 3) {
               for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
                  var9 = (Point)this.VecEff.elementAt(var8);
                  super.fraImgSub2Eff.drawFrameNew(super.BE * super.fraImgEff.maxNumFrame + var9.f % super.fraImgEff.maxNumFrame, var9.x / 100, var9.y / 100, 0, 3, var1);
               }
            }

            super.fraImgEff.drawFrameNew(super.BE * super.fraImgEff.maxNumFrame + super.f / super.numNextFrame % super.fraImgEff.maxNumFrame, super.x / 100, super.y / 100, super.Dir, 3, var1);
            if (super.fraImgSubEff != null) {
               super.fraImgSubEff.drawFrame(CRes.random(super.fraImgSubEff.nFrame), super.x / 100, super.y / 100, super.Dir, 3, var1);
               return;
            }
            break;
         case 7:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var12 = (Point_Focus)this.VecEff.elementAt(var8);
               this.AA(var1, super.fraImgEff, var12.frame, var12.x, var12.y);
            }

            return;
         case 9:
         case 45:
         case 52:
         case 58:
         case 65:
         case 68:
         case 170:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 33, var1);
            return;
         case 11:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               (var9 = (Point)this.VecEff.elementAt(var8)).AY.drawFrame(var9.f / super.numNextFrame % var9.AY.nFrame, var9.x, var9.y, 0, 3, var1);
            }

            return;
         case 13:
            if (super.fraImgSubEff != null) {
               for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
                  var9 = (Point)this.VecEff.elementAt(var8);
                  super.fraImgSubEff.drawFrame(var9.f % super.fraImgSubEff.nFrame, var9.x, var9.y, super.Dir, 3, var1);
               }
            }

            if (super.f <= super.fRemove) {
               super.fraImgEff.drawFrame((super.f / super.numNextFrame + this.randomf) % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               if (super.fraImgSub2Eff != null) {
                  super.fraImgSub2Eff.drawFrame(super.f % super.fraImgSub2Eff.nFrame, super.x, super.y, super.Dir, 3, var1);
                  return;
               }
            }
            break;
         case 14:
            if (super.f < 4) {
               super.fraImgEff.drawFrame(super.f / super.numNextFrame, super.x, super.y, 0, 33, var1);
               return;
            }
            break;
         case 16:
            super.fraImgEff.drawFrameNew(super.BE * super.fraImgEff.maxNumFrame + CRes.abs(this.typeSub), super.x, super.y, super.Dir, 3, var1);
            return;
         case 17:
            var4 = super.f;
            if (super.f >= super.fRemove) {
               var4 = 2 - (super.f - super.fRemove) / 3;
            } else if (var4 > 2) {
               var4 = 2;
            }

            super.fraImgEff.drawFrame(var4, super.x, super.y, 0, 3, var1);
            return;
         case 19:
            if (super.f < 6) {
               super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
            }

            if (super.f > 1) {
               super.fraImgSubEff.drawFrame((super.f - 2 / super.numNextFrame) % super.fraImgSubEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 20:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, this.randomf, 3, var1);
            return;
         case 21:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrame(var9.frame, var9.x, var9.y, 0, 3, var1);
            }

            if (super.f < 6) {
               super.fraImgSubEff.drawFrame(super.f / super.numNextFrame % super.fraImgSubEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 22:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgSubEff.drawFrame(var9.f / super.numNextFrame % super.fraImgSubEff.nFrame, var9.x, var9.y, super.Dir, 3, var1);
            }

            if (this.isUpdateNormal) {
               super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 23:
            if (super.f < 6) {
               super.fraImgSub2Eff.drawFrame((super.f / super.numNextFrame + this.randomf) % super.fraImgSub2Eff.nFrame, super.x1000, super.y1000, super.Dir, 3, var1);
            }

            if (super.f <= super.fRemove) {
               super.fraImgEff.drawFrame((super.f / super.numNextFrame + this.randomf) % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
            }

            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgSubEff.drawFrame(var9.f / super.numNextFrame % super.fraImgSubEff.nFrame, var9.x, var9.y, super.Dir, 3, var1);
            }

            return;
         case 25:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
            if (this.typeSub == 1 || this.typeSub == 2 || this.typeSub == 3) {
               if (super.f < 4) {
                  super.fraImgSubEff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.x1000, super.y, super.Dir, 3, var1);
               }

               if (this.typeSub == 2) {
                  for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
                     var9 = (Point)this.VecEff.elementAt(var8);
                     super.fraImgSub2Eff.drawFrame(var9.f % super.fraImgSub2Eff.nFrame, var9.x, var9.y, super.Dir, 3, var1);
                  }

                  return;
               }
            }
            break;
         case 26:
            if (super.f < this.mPlayFrame.length) {
               super.fraImgEff.drawFrame(this.mPlayFrame[super.f], super.x, super.y, super.Dir, 33, var1);
            }

            if (super.f > 0 && super.f < this.mPlayFrame.length + 1) {
               super.fraImgEff.drawFrame(this.mPlayFrame[super.f - 1], super.x, super.y, super.Dir == 0 ? 2 : 0, 33, var1);
               return;
            }
            break;
         case 27:
            if (super.f <= super.fRemove) {
               if ((var8 = super.f / 2) > this.typeSub) {
                  var8 = this.typeSub;
               }

               super.fraImgEff.drawFrame(var8, super.x, super.y, super.Dir, 3, var1);
            }

            if (this.typeSub == 2) {
               for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
                  var9 = (Point)this.VecEff.elementAt(var8);
                  super.fraImgSubEff.drawFrame(var9.f % super.fraImgSubEff.nFrame, var9.x, var9.y, super.Dir, 3, var1);
               }

               return;
            }
            break;
         case 29:
            if (super.f >= 0 && super.f < 4) {
               super.fraImgEff.drawFrame(super.f / 2, super.x1000, super.y1000, super.Dir, 3, var1);
            }

            if (super.f < super.fRemove && super.f > 1) {
               super.fraImgSubEff.drawFrame(0, super.x, super.y, super.Dir, 3, var1);
            }

            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgSub2Eff.drawFrame(var9.f % super.fraImgSub2Eff.nFrame, var9.x, var9.y, super.Dir, 3, var1);
            }

            return;
         case 30:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               Line var14;
               if ((var14 = (Line)this.VecEff.elementAt(var8)) != null) {
                  var4 = 0;
                  if (var8 / 2 < this.colorpaint.length) {
                     var4 = this.colorpaint[var8 / 2];
                  }

                  var1.setColor(var4);
                  var1.drawLine(var14.x0 / 1000, var14.y0 / 1000, var14.x1 / 1000, var14.y1 / 1000);
                  if (var14.is2Line) {
                     var1.drawLine(var14.x0 / 1000 + 1, var14.y0 / 1000, var14.x1 / 1000 + 1, var14.y1 / 1000);
                  }
               }
            }

            return;
         case 31:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               if ((var9 = (Point)this.VecEff.elementAt(var8)).dis == 1) {
                  super.fraImgEff.drawFrameNew(var9.f / 2 % super.fraImgEff.nFrame, var9.x, var9.y, 0, 33, var1);
               } else if (var9.dis == 0) {
                  super.fraImgSubEff.drawFrameNew((var9.frame + var9.f / 2) % super.fraImgSubEff.nFrame, var9.x, var9.y, 0, 3, var1);
               }
            }

            return;
         case 32:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               if (super.fraImgEff.nFrame != 0) {
                  if (var9.dis == 1) {
                     super.fraImgEff.drawFrameNew(super.fraImgEff.nFrame - var9.f / 2 % super.fraImgEff.nFrame, var9.x, var9.y, 0, 33, var1);
                  } else if (var9.dis == 0) {
                     super.fraImgSubEff.drawFrameNew((var9.frame + var9.f / 2) % super.fraImgSubEff.nFrame, var9.x, var9.y, 0, 3, var1);
                  }
               }
            }

            return;
         case 35:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 37:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgSub2Eff.drawFrame(var9.f % super.fraImgSub2Eff.nFrame, var9.x, var9.y, super.Dir, 3, var1);
            }

            if (super.f >= 0 && super.f < 6) {
               super.fraImgEff.drawFrame(super.f / 2, super.x1000, super.y1000, super.Dir, 3, var1);
            }

            if (super.f < super.fRemove && super.f > 1) {
               if ((var8 = super.f / 2) > 2) {
                  var8 = 2;
               }

               super.fraImgSubEff.drawFrame(var8, super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 38:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrame(var9.f % super.fraImgEff.nFrame, var9.x, var9.y, super.Dir, 3, var1);
            }

            return;
         case 39:
            if ((var8 = super.f) > 2) {
               var8 = 2 + CRes.random(2);
            }

            super.fraImgEff.drawFrame(var8, super.x, super.y, super.Dir, 3, var1);
            return;
         case 40:
            if (super.f % 2 == 0) {
               super.fraImgSubEff.drawFrame(0, super.x, super.y + 12, super.Dir, 3, var1);
            }

            super.fraImgEff.drawFrame(3, super.x, super.y, super.Dir, 3, var1);
            if (super.f % 2 == 0) {
               if (this.typeSub != 1 && this.typeSub != 2) {
                  super.fraImgSub2Eff.drawFrameNew(0, super.x, super.y + 12 + super.f * 14, super.Dir, 33, var1);
                  return;
               }

               super.fraImgSub2Eff.drawFrameNew(0, super.x - 12, super.y + 12 + super.f * 14, super.Dir, 33, var1);
               if (this.typeSub == 2) {
                  super.fraImgSub2Eff.drawFrameNew(1, super.x + 12, super.y + 12 + super.f * 14, super.Dir, 33, var1);
                  return;
               }

               super.fraImgSub2Eff.drawFrameNew(0, super.x + 12, super.y + 12 + super.f * 14, super.Dir, 33, var1);
               return;
            }
            break;
         case 46:
         case 159:
            if (super.f % 3 <= 1 || super.f >= 6) {
               super.fraImgEff.drawFrame(0, super.x, super.y, super.Dir, 33, var1);
            }

            if (super.f >= 2 && super.f <= 7) {
               super.fraImgSubEff.drawFrame((super.f - 2) / 3, super.x, super.y + 5, super.Dir, 33, var1);
               return;
            }
            break;
         case 47:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrame(var9.f / super.numNextFrame % super.fraImgEff.nFrame, var9.x, var9.y, 0, 3, var1);
            }

            return;
         case 48:
         case 50:
            if (super.f < 10) {
               super.fraImgEff.drawFrame(super.f / 2, super.x, super.y, super.Dir, 3, var1);
            }

            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               if ((var2 = (Point)this.VecEff.elementAt(var4)).frame < 2) {
                  super.fraImgSubEff.drawFrame(var2.f / 2 % super.fraImgSubEff.nFrame, var2.x, var2.y, 0, 3, var1);
               } else {
                  super.fraImgSub2Eff.drawFrame(var2.f / 2 % super.fraImgSubEff.nFrame, var2.x, var2.y, 0, 3, var1);
               }
            }

            return;
         case 49:
            int var10006 = super.f;
            this.AA(var1, super.fraImgEff, super.frame, super.x, super.y);
            return;
         case 51:
            if (super.f < super.fRemove) {
               super.fraImgSubEff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               if (super.fraImgEff != null) {
                  super.fraImgEff.drawFrame(CRes.random(super.fraImgEff.nFrame), super.x, super.y, super.Dir, 3, var1);
                  return;
               }
            }
            break;
         case 54:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var2 = (Point)this.VecEff.elementAt(var4);
               if (this.typeSub != 1 && this.typeSub != 3 && this.typeSub != 10 && this.typeSub != 11 && this.typeSub != 12) {
                  if (this.typeSub >= 4 && this.typeSub <= 7) {
                     super.fraImgEff.drawFrameNew(super.frame * super.fraImgEff.maxNumFrame + var2.f % super.fraImgEff.maxNumFrame, var2.x, var2.y, super.Dir, 3, var1);
                  } else if (this.typeSub == 8) {
                     super.fraImgEff.drawFrameNew((2 + var4 % 2) * super.fraImgEff.maxNumFrame + var2.f % super.fraImgEff.maxNumFrame, var2.x, var2.y, super.Dir, 3, var1);
                  } else if (this.typeSub == 9) {
                     super.fraImgEff.drawFrameNew((super.frame + var4 % 2) * super.fraImgEff.maxNumFrame + var2.f % super.fraImgEff.maxNumFrame, var2.x, var2.y, super.Dir, 3, var1);
                  } else {
                     super.fraImgEff.drawFrame(var2.f % super.fraImgEff.nFrame, var2.x, var2.y, super.Dir, 3, var1);
                  }
               } else {
                  super.fraImgEff.drawFrame(3, var2.x, var2.y, super.Dir, 3, var1);
               }
            }

            if (super.fraImgSubEff != null) {
               for(var4 = 0; var4 < this.vecSubEff.size(); ++var4) {
                  var2 = (Point)this.vecSubEff.elementAt(var4);
                  super.fraImgSubEff.drawFrame(var2.f / 2 % super.fraImgSubEff.nFrame, var2.x, var2.y, super.Dir, 33, var1);
               }

               return;
            }
            break;
         case 56:
            if (super.f < 3) {
               super.fraImgEff.drawFrame(super.f, super.toX, super.toY, super.Dir, 33, var1);
               return;
            }

            super.fraImgEff.drawFrame(5 - super.f, super.x, super.y, super.Dir, 33, var1);
            return;
         case 59:
         case 107:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var2 = (Point)this.VecEff.elementAt(var4);
               super.fraImgEff.drawFrame(var2.frame, var2.x, var2.y, 0, 3, var1);
            }

            return;
         case 60:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var2 = (Point)this.VecEff.elementAt(var4);
               super.fraImgEff.drawFrame(var2.frame, var2.x, var2.y, 0, 3, var1);
            }

            if (this.typeSub == 2) {
               if (super.f < super.fraImgSubEff.nFrame) {
                  super.fraImgSubEff.drawFrame(super.f / super.numNextFrame % super.fraImgSubEff.nFrame, super.x, super.y, super.Dir, 3, var1);
                  return;
               }
            } else if (super.f < 6) {
               super.fraImgSubEff.drawFrame(super.f / super.numNextFrame % super.fraImgSubEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 62:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var3 = (var2 = (Point)this.VecEff.elementAt(var4)).frame;
               if (super.f > super.fRemove - 4) {
                  var3 += 3;
               }

               super.fraImgEff.drawFrame(var3, var2.x, var2.y, super.Dir, 3, var1);
            }

            return;
         case 63:
            this.paintNo_Dat_New(var1);
            return;
         case 64:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame, super.x, super.y, 0, 3, var1);
            super.fraImgEff.drawFrame(super.f / super.numNextFrame, super.x, super.y, 2, 3, var1);
            return;
         case 66:
            var4 = 0;
            if (super.f < 2) {
               var4 = 1;
            } else if (super.f > super.fRemove - 4) {
               var4 = 2 - (super.fRemove - super.f) / 2;
            }

            super.fraImgEff.drawFrameNew(this.typeSub * 3 + var4, super.x, super.y, super.Dir, 3, var1);
            return;
         case 69:
         case 70:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrame(var9.frame, this.objTo.x, this.objTo.y + var9.y, super.Dir, 33, var1);
            }

            return;
         case 72:
            super.fraImgEff.drawFrame(CRes.abs(this.typeSub), super.x, super.y, super.Dir, 3, var1);
            return;
         case 73:
            var1.setColor(0);
            var1.drawLine(super.x, super.y, super.toX, super.toY);
            return;
         case 74:
            var1.setColor(-1);
            var1.drawLine(super.x, super.y, super.toX, super.toY);
            return;
         case 75:
         case 78:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               if ((var9 = (Point)this.VecEff.elementAt(var8)).frame == 0) {
                  super.fraImgSubEff.drawFrame(var9.f % super.fraImgSubEff.nFrame, var9.x, var9.y, 0, 3, var1);
               } else {
                  super.fraImgSub2Eff.drawFrame(var9.f % super.fraImgSub2Eff.nFrame, var9.x, var9.y, 0, 3, var1);
               }
            }

            if ((var3 = (int)(10.0D / (1.0D + (double)(GameCanvas.percentUPCV + GameCanvas.CY) / 100.0D))) <= 0) {
               var3 = 1;
            }

            if (super.f < super.fRemove + var3) {
               super.fraImgEff.drawFrame(super.f / 2 % 2, super.x, super.y, 0, 3, var1);
               return;
            }
            break;
         case 76:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               if ((var2 = (Point)this.VecEff.elementAt(var4)).frame == 0) {
                  super.fraImgEff.drawFrame(var2.f % super.fraImgEff.nFrame, var2.x, var2.y, 0, 3, var1);
               } else {
                  super.fraImgSubEff.drawFrame(var2.f % super.fraImgSubEff.nFrame, var2.x, var2.y, 0, 3, var1);
               }
            }

            return;
         case 79:
            if (super.f <= 6 && super.f % 2 != 0) {
               break;
            }

            super.fraImgEff.drawFrame(super.frame, super.x, super.y, 0, 3, var1);
            return;
         case 82:
            super.fraImgSubEff.drawFrame((super.f / super.numNextFrame + 3) % super.fraImgSubEff.nFrame, super.x, super.y, super.Dir, 33, var1);
            if (super.f < 4) {
               super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 33, var1);
               return;
            }
            break;
         case 83:
            if (super.f < 6) {
               super.fraImgSubEff.drawFrame(super.f, this.objTo.x, this.objTo.y - this.objTo.hOne / 2, super.Dir, 3, var1);
            }

            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrame(var9.frame, this.objTo.x, this.objTo.y + var9.y, super.Dir, 33, var1);
            }

            return;
         case 84:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var2 = (Point)this.VecEff.elementAt(var4);
               super.fraImgEff.drawFrame(var2.frame, var2.x, var2.y, var2.dis, 3, var1);
            }

            return;
         case 85:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               Line var13;
               if ((var13 = (Line)this.VecEff.elementAt(var4)) != null) {
                  if (var13.type < 4) {
                     super.fraImgEff.drawFrame(var13.AE % super.fraImgEff.nFrame, var13.x0 / 1000, var13.y0 / 1000, 0, 3, var1);
                  } else if (var13.type < 8) {
                     super.fraImgSubEff.drawFrame(var13.AE % super.fraImgSubEff.nFrame, var13.x0 / 1000, var13.y0 / 1000, 0, 3, var1);
                  } else {
                     var3 = 0;
                     if (var4 / 2 < this.colorpaint.length) {
                        var3 = this.colorpaint[var4 / 2];
                     }

                     var1.setColor(var3);
                     var1.drawLine(var13.x0 / 1000, var13.y0 / 1000, var13.x1 / 1000, var13.y1 / 1000);
                     if (var13.is2Line) {
                        var1.drawLine(var13.x0 / 1000 + 1, var13.y0 / 1000, var13.x1 / 1000 + 1, var13.y1 / 1000);
                     }
                  }
               }
            }

            return;
         case 87:
            super.fraImgEff.drawFrame(this.typeSub * 3 + 2, super.x / 100, super.y / 100, super.Dir, 3, var1);
            return;
         case 88:
            if (super.f < this.mPlayFrame.length && super.fraImgEff.getImageFrame() != null) {
               var1.drawRegion(super.fraImgEff.getImageFrame(), this.mPlayFrame[super.f] * super.fraImgEff.frameWidth, 0, super.fraImgEff.frameWidth, super.fraImgEff.frameHeight, 0, super.x, super.y, 33);
               return;
            }
            break;
         case 91:
         case 92:
            if (super.f < this.mPlayFrame.length) {
               super.fraImgEff.drawFrame(this.mPlayFrame[super.f], super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 93:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrame(var9.frame, var9.x, var9.y, 0, 3, var1);
            }

            return;
         case 96:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var12 = (Point_Focus)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrame(super.f / 3 % 3, var12.x, var12.y, 0, 3, var1);
            }

            return;
         case 97:
            if (super.f % 2 == 1) {
               super.fraImgSubEff.drawFrame(0, super.x, super.y, super.Dir, 33, var1);
               return;
            }

            super.fraImgEff.drawFrame(0, super.x, super.y, super.Dir, 33, var1);
            return;
         case 98:
            if (super.f < 12) {
               super.fraImgEff.drawFrameNew(super.f / 3, super.x, super.y, 0, 33, var1);
            }

            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgSubEff.drawFrame(var9.f % 5, var9.x, var9.y, 0, 3, var1);
            }

            return;
         case 99:
            if (super.f < 8) {
               super.fraImgSubEff.drawFrameNew(super.f / 2, super.x, super.y, 0, 33, var1);
               super.fraImgEff.drawFrame(super.f / 2, super.x, super.y, 0, 33, var1);
            }

            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgSub2Eff.drawFrame(var9.f % 5, var9.x, var9.y, 0, 3, var1);
            }

            return;
         case 101:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               if (this.objTo != null) {
                  super.fraImgEff.drawFrame(var9.f, this.objTo.x + var9.x, this.objTo.y + var9.y, 0, 3, var1);
               }
            }

            return;
         case 102:
            if (super.f < 10) {
               super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.x, super.y, 0, 3, var1);
               return;
            }

            if (super.f < 26) {
               super.fraImgSubEff.drawFrame(super.f / 2 % 3, super.x, super.y, 0, 3, var1);
               return;
            }

            super.fraImgSubEff.drawFrame(3 + super.f / 2 % 2, super.x, super.y, 0, 3, var1);
            return;
         case 103:
            if (super.f < this.mPlayFrame.length && super.f >= 0) {
               super.fraImgEff.drawFrame(this.mPlayFrame[super.f], super.x, super.y, super.Dir, 33, var1);
               return;
            }
            break;
         case 104:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrame(var9.f, MainScreen.cameraMain.xCam + var9.x, var9.y, 0, 3, var1);
            }

            return;
         case 105:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrame(var9.frame, MainScreen.cameraMain.xCam + var9.x, var9.y, 0, 3, var1);
            }

            return;
         case 106:
            super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.x, super.y, 0, 33, var1);
            return;
         case 108:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               if ((var2 = (Point)this.VecEff.elementAt(var4)).dis != 0) {
                  super.fraImgSubEff.drawFrameNew(var2.frame + (this.typeSub << 2), var2.x, var2.y, 0, 3, var1);
               } else {
                  super.fraImgEff.drawFrameNew(var2.frame + (this.typeSub << 2), var2.x, var2.y, 0, 3, var1);
               }
            }

            return;
         case 109:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 0, var1);
            return;
         case 110:
            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               if ((var2 = (Point)this.VecEff.elementAt(var4)).subType == 1) {
                  super.fraImgSubEff.drawFrameNew(this.typeSub * 3 + var2.frame, var2.x, var2.y, 0, 3, var1);
               } else {
                  super.fraImgEff.drawFrameNew((this.typeSub << 2) + var2.frame, var2.x, var2.y, 0, 3, var1);
               }
            }

            return;
         case 111:
            for(var8 = 0; var8 < 5; ++var8) {
               super.fraImgEff.drawFrame(0, super.x, super.y - var8 * super.fraImgEff.frameHeight + 5, CRes.random(2) << 1, 33, var1);
            }

            super.fraImgSub2Eff.drawFrame(super.f / 2 % super.fraImgSub2Eff.nFrame, super.x, super.y + 9, 0, 33, var1);

            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgSubEff.drawFrameNew(var9.frame, super.x + var9.x, super.y + var9.y, super.Dir, 33, var1);
            }

            return;
         case 112:
            if (this.typeSub == 2) {
               super.fraImgEff.drawFrameNew(4 + super.frame, super.x, super.y, super.Dir, 36, var1);
               return;
            }

            super.fraImgEff.drawFrameNew((this.typeSub << 2) + super.frame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 113:
            for(var8 = this.VecEff.size() - 1; var8 >= 0; --var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgSubEff.drawFrameNew(var9.frame * super.fraImgSubEff.maxNumFrame + var9.f / 3 % super.fraImgSubEff.maxNumFrame, var9.x / 1000, var9.y / 1000, 0, 33, var1);
            }

            if (super.f / 3 < super.fraImgEff.nFrame) {
               super.fraImgEff.drawFrame(super.f / 3, super.x, super.y, super.Dir, 33, var1);
               return;
            }
            break;
         case 115:
            super.fraImgSubEff.drawFrame(0, super.x, super.y, 0, 33, var1);

            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var2 = (Point)this.VecEff.elementAt(var4);
               super.fraImgEff.drawFrame(var2.frame, var2.x, var2.y, 0, 3, var1);
            }

            return;
         case 116:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrame(var9.f % super.fraImgEff.nFrame, var9.x, var9.y, 0, 3, var1);
            }

            return;
         case 117:
            if (super.f > super.fRemove - 2 && super.f <= super.fRemove) {
               super.fraImgEff.drawFrame(super.fRemove - super.f, super.x, super.y, 0, 3, var1);
               return;
            }

            super.fraImgEff.drawFrame(0, super.x, super.y, 0, 3, var1);
            return;
         case 119:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               if ((var9 = (Point)this.VecEff.elementAt(var8)).f < this.mPlayFrame.length) {
                  super.fraImgEff.drawFrameNew(this.mPlayFrame[var9.f], var9.x, var9.y, 0, 3, var1);
               }
            }

            return;
         case 120:
            if (super.f < 10) {
               super.fraImgEff.drawFrame(super.f / 2, super.x, super.y, super.Dir, 3, var1);
            }

            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               if ((var2 = (Point)this.VecEff.elementAt(var4)).frame < 2) {
                  super.fraImgSubEff.drawFrameNew(var2.fSmall * super.fraImgSubEff.maxNumFrame + var2.f / 2 % super.fraImgSubEff.maxNumFrame, var2.x, var2.y, 0, 3, var1);
               } else {
                  super.fraImgSub2Eff.drawFrameNew(var2.fSmall * super.fraImgSub2Eff.maxNumFrame + var2.f / 2 % super.fraImgSub2Eff.maxNumFrame, var2.x, var2.y, 0, 3, var1);
               }
            }

            return;
         case 121:
            super.frame = super.f / 2;
            if (super.frame >= this.mPlayFrame.length) {
               super.frame = this.mPlayFrame.length - 1;
            }

            if (super.f >= 10 && super.f % 2 != 0) {
               break;
            }

            var1.setColor(this.mPlayFrame[super.frame]);
            var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
            return;
         case 122:
            super.fraImgEff.drawFrameNew(super.f / super.numNextFrame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 123:
            super.fraImgEff.drawFrameNew(this.typeSub * super.fraImgEff.maxNumFrame + super.f / super.numNextFrame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 124:
            if (this.mPlayFrame[super.f] != 5) {
               super.fraImgEff.drawFrameNew(this.mPlayFrame[super.f], super.x, super.y, 0, 3, var1);
               return;
            }

            if (super.fraImgEff.getImageFrame() != null) {
               var1.drawRegion(super.fraImgEff.getImageFrame(), 0, 0, 30, 21, 0, super.x - 15, super.y, 3);
               var1.drawRegion(super.fraImgEff.getImageFrame(), 19, 0, 30, 21, 0, super.x + 15, super.y, 3);
               return;
            }
            break;
         case 125:
            var8 = super.x;
            if (super.f < 3 || super.f > 10 && super.f < 14) {
               var8 += CRes.random_Am_0(3);
            }

            super.fraImgEff.drawFrameNew(this.mPlayFrame[super.f], var8, super.y, 0, 33, var1);
            return;
         case 126:
            if (super.step != 0 && super.step != 3) {
               super.fraImgEff.drawFrame((super.frame << 1) + 1, super.x / 100, super.y / 100, super.Dir, 3, var1);
            } else {
               super.fraImgEff.drawFrame(super.frame << 1, super.x / 100, super.y / 100, super.Dir, 3, var1);
            }

            if (super.frame == 1) {
               super.fraImgSubEff.drawFrame(super.frame / 2 % super.fraImgSubEff.nFrame, super.x1000, super.y1000, super.Dir, 3, var1);
            }

            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               if ((var12 = (Point_Focus)this.VecEff.elementAt(var8)).frame == 0) {
                  super.fraImgSubEff.drawFrame(var12.AG / 2 % super.fraImgSubEff.nFrame, var12.x, var12.y, super.Dir, 3, var1);
               }

               if (var12.frame == 1) {
                  super.fraImgSub2Eff.drawFrame(0, var12.x, var12.y, super.Dir, 3, var1);
               }
            }

            return;
         case 127:
            var3 = super.y;
            if (super.f < this.mposy.length) {
               var3 -= this.mposy[super.f];
            }

            if (this.typeSub % 10 > 3 && this.typeSub % 10 != 7) {
               super.fraImgEff.drawFrameNew((super.frame << 1) + super.f / 3 % 2, super.x, var3, 0, 3, var1);
               return;
            }

            super.fraImgEff.drawFrameNew(super.frame << 1, super.x, var3, 0, 3, var1);
            return;
         case 128:
            super.fraImgEff.drawFrame(super.frame << 1, super.x / 100, super.y / 100, super.Dir, 3, var1);
            return;
         case 130:
            if (this.objTo != null) {
               for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
                  var9 = (Point)this.VecEff.elementAt(var8);
                  super.fraImgEff.drawFrameNew(var9.frame, this.objTo.x + var9.x, this.objTo.y + var9.y, 0, 33, var1);
               }

               return;
            }
            break;
         case 131:
            if (this.objTo != null) {
               super.fraImgEff.drawFrameNew(3 + super.f / 2 % super.fraImgEff.maxNumFrame, this.objTo.x, this.objTo.y + 4, 0, 33, var1);
               return;
            }
            break;
         case 132:
            if (this.typeSub == 1) {
               super.fraImgEff.drawFrameNew(this.typeSub * super.fraImgEff.maxNumFrame + CRes.random(3), super.x, super.y, 0, 3, var1);
               return;
            }

            super.fraImgEff.drawFrameNew(this.typeSub * super.fraImgEff.maxNumFrame + (3 - super.f), super.x, super.y, 0, 3, var1);
            return;
         case 133:
            byte var11 = 1;
            if (super.f < 2 || super.f > super.fRemove - 3) {
               var11 = 0;
            }

            super.fraImgEff.drawFrame(var11, super.x, super.y, 0, 3, var1);
            return;
         case 134:
            if (super.f >= 2 && super.f <= super.fRemove - 3) {
               super.fraImgEff.drawFrame(1, super.x, super.y, 0, 3, var1);
               super.fraImgSubEff.drawFrameNew(this.typeSub * super.fraImgSubEff.maxNumFrame + CRes.random(3), super.x - 38, super.y - 40, 0, 0, var1);
               return;
            }

            super.fraImgEff.drawFrame(0, super.x, super.y, 0, 3, var1);
            return;
         case 136:
            if (super.f < this.mPlayFrameVip.length) {
               for(var8 = 0; var8 < this.mPlayFrameVip[super.f].length; ++var8) {
                  super.fraImgEff.drawFrameNew(this.mPlayFrameVip[super.f][var8], super.x, super.y, 0, 3, var1);
               }

               return;
            }
            break;
         case 137:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrame(var9.f / 4 % super.fraImgEff.nFrame, var9.x, var9.y, super.Dir, 33, var1);
            }

            return;
         case 138:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrameNew((var9.frame << 2) + var9.f % super.fraImgEff.nFrame, var9.x, var9.y, super.Dir, 3, var1);
            }

            return;
         case 139:
            if (this.mPlayFrame[super.f] >= 0) {
               super.fraImgEff.drawFrameNew(this.mPlayFrame[super.f], super.x, super.y, 0, 33, var1);
               return;
            }
            break;
         case 140:
         case 167:
            var8 = CRes.random(2);

            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               if ((var4 = var3 + var8) >= this.VecEff.size()) {
                  var4 = 0;
               }

               if ((var10 = (Point)this.VecEff.elementAt(var4)).frame == 1) {
                  if (super.f > super.fRemove - 2) {
                     super.frame = 3;
                  } else {
                     super.frame = CRes.random(3);
                  }

                  if (super.typeEffect == 140) {
                     super.fraImgEff.drawFrameNew(super.frame, var10.x, var10.y, 0, 33, var1);
                  }
               } else {
                  if (super.f > super.fRemove - 2) {
                     super.frame = 3;
                  } else {
                     super.frame = CRes.random(3);
                  }

                  byte var5 = 0;
                  byte var6 = 0;
                  if (super.typeEffect == 167) {
                     if (super.Dir == 0) {
                        var5 = 10;
                     } else if (super.Dir == 2) {
                        var5 = -10;
                     }

                     var6 = 10;
                  }

                  super.fraImgSubEff.drawFrameNew(super.frame, var10.x + var5, var10.y + var6, 0, 33, var1);
               }
            }

            return;
         case 141:
            if (super.f / super.numNextFrame % 4 > 1) {
               super.fraImgEff.drawFrameNew(super.f / super.numNextFrame % 4, super.x, super.y, super.Dir, 33, var1);
               return;
            }

            super.fraImgEff.drawFrameNew_BeginSuper(super.f / super.numNextFrame % 4, super.x, super.y, super.Dir, 33, var1);
            return;
         case 142:
            if (super.f < this.mPlayFrame.length && super.fraImgEff.getImageFrame() != null) {
               if (this.mPlayFrame[super.f] == 4) {
                  var1.drawRegion(super.fraImgEff.getImageFrame(), this.mPlayFrame[super.f] * super.fraImgEff.frameWidth, super.fraImgEff.frameHeight - super.fraImgEff.frameHeight / 2, super.fraImgEff.frameWidth, super.fraImgEff.frameHeight / 2, 0, super.x, super.y, 33);
                  return;
               }

               var1.drawRegion(super.fraImgEff.getImageFrame(), this.mPlayFrame[super.f] * super.fraImgEff.frameWidth, 0, super.fraImgEff.frameWidth, super.fraImgEff.frameHeight, 0, super.x, super.y, 33);
               return;
            }
            break;
         case 146:
            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               var10 = (Point)this.VecEff.elementAt(var3);
               super.fraImgEff.drawFrame(3 + var10.f % 3, var10.x / 1000, var10.y / 1000, super.Dir, 3, var1);
            }

            return;
         case 147:
            this.paint_Lucci_L2(var1);
            return;
         case 148:
            super.fraImgEff.drawFrameNew(0, super.x, super.y, 0, 3, var1);
            return;
         case 149:
            super.fraImgEff.drawFrameNew(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 33, var1);
            return;
         case 151:
            super.fraImgEff.drawFrameNew(this.typeSub * super.fraImgEff.maxNumFrame + super.f / super.numNextFrame % super.fraImgEff.maxNumFrame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 152:
            if (this.mPlayFrame[super.f % this.mPlayFrame.length] >= 0) {
               super.fraImgEff.drawFrame(this.mPlayFrame[super.f % this.mPlayFrame.length], super.x, super.y, super.Dir, 33, var1);
               return;
            }
            break;
         case 153:
            super.fraImgEff.drawFrame(super.frame, super.x, super.y, 0, 33, var1);
            super.fraImgSubEff.drawFrame(CRes.random(4), super.x, super.y - 4, 0, 33, var1);

            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               var10 = (Point)this.VecEff.elementAt(var3);
               if (CRes.random(2) == 0) {
                  super.fraImgSub2Eff.drawFrameNew(CRes.random(4) + (var10.frame << 2), var10.x, var10.y, super.Dir, 3, var1);
               }
            }

            return;
         case 154:
            var1.setColor(this.colorpaint[super.f % this.colorpaint.length]);

            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               if (var8 < this.VecEff.size() - 1) {
                  var10 = (Point)this.VecEff.elementAt(var8 + 1);
               } else {
                  var10 = (Point)this.VecEff.elementAt(0);
               }

               var1.drawLine(var9.x, var9.y, var10.x, var10.y);
            }

            return;
         case 155:
            var1.setColor(this.colorpaint[super.f % this.colorpaint.length]);

            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               super.fraImgEff.drawFrameNew(var9.f % 9 + super.f % super.fraImgEff.nFrame, var9.x, var9.y, 0, 3, var1);
            }

            return;
         case 156:
            super.fraImgEff.drawFrame(GameCanvas.language, super.x, super.y + super.y1000, super.Dir, 33, var1);
            return;
         case 157:
            if (super.f >= 10) {
               super.fraImgEff.drawFrameNew(3 + (super.f - 10) / 2, super.x, super.y, super.Dir, 33, var1);
               return;
            }

            super.fraImgEff.drawFrameNew(super.f / 2 % 3, super.x, super.y, super.Dir, 33, var1);
            return;
         case 158:
            if (super.f < 35) {
               super.fraImgSubEff.drawFrame(0, super.x1000 - super.am_duong * 3, super.y1000 + 12, super.Dir, 33, var1);
            }

            super.fraImgEff.drawFrame(super.f / 3 % super.fraImgEff.nFrame, super.x, super.y + 5, super.Dir, 33, var1);
            if (super.f < 30) {
               super.fraImgSub3Eff.drawFrame(CRes.random(4), super.x1000 - super.am_duong * 13, super.y1000 + 6 - super.f / 8, super.Dir, 3, var1);
            }

            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               var10 = (Point)this.VecEff.elementAt(var3);
               if (CRes.random(2) == 0) {
                  super.fraImgSub2Eff.drawFrameNew(CRes.random(4) + (var10.frame << 2), var10.x, var10.y, super.Dir, 3, var1);
               }
            }

            return;
         case 160:
            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               if (!(var10 = (Point)this.VecEff.elementAt(var3)).AW) {
                  super.fraImgEff.drawFrameNew(this.typeSub * super.fraImgEff.maxNumFrame + var10.frame, var10.x, var10.y, var10.dis, 3, var1);
               }
            }

            return;
         case 161:
            if (super.f < 20 - this.randomf) {
               super.fraImgEff.drawFrameNew(this.typeSub * super.fraImgEff.maxNumFrame + super.frame, super.x, super.y, 0, 3, var1);
            } else if (super.f < 20 + this.lengthM - this.randomf) {
               super.fraImgSubEff.drawFrameNew(this.typeSub * super.fraImgSubEff.maxNumFrame + this.mPlayFrameVip[this.randomf2][super.f - (20 - this.randomf)], super.x, super.y, 0, 3, var1);
            }

            for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
               var10 = (Point)this.VecEff.elementAt(var3);
               super.fraImgEff.drawFrameNew(this.typeSub * super.fraImgEff.maxNumFrame + var10.frame, var10.x / 100, var10.y / 100, 0, 3, var1);
            }

            return;
         case 162:
            if (super.f > 115 && super.f < 165) {
               super.fraImgEff.drawFrame(super.f / 3 % super.fraImgEff.maxNumFrame, super.x / 100, super.y / 100, super.Dir, 3, var1);
            }
            if (super.f >= 20 && super.f < 25) {
               if (super.fraImgSubEff.getImageFrame() != null) {
                  var1.drawRegion(super.fraImgSubEff.getImageFrame(), 0, 0, super.fraImgSubEff.frameWidth, 30 + (super.f - 20) * 15, 0, super.x1000, super.y1000, 33);
               }
            } else if (super.f >= 25) {
               var1.drawRegion(super.fraImgSubEff.getImageFrame(), 0, 0, super.fraImgSubEff.frameWidth, 90, 0, super.x1000, super.y1000, 33);
            }

            super.fraImgSub2Eff.drawFrame(super.f / 3 % super.fraImgSub2Eff.maxNumFrame, super.x1000, super.y1000 + 5, super.Dir, 33, var1);
            if (super.f < 60) {
               super.fraImgSub3Eff.drawFrame(0, super.x + CRes.random_Am_0(2), super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 163:
            super.fraImgEff.drawFrame(super.f / 3 % super.fraImgEff.maxNumFrame, super.x1000, super.y1000 - 6, super.Dir, 33, var1);
            return;
         case 164:
            return;
         case 165:
            super.fraImgEff.drawFrame(super.CFrame, super.objMainEff.x, super.objMainEff.y - super.objMainEff.hOne / 2, 0, 3, var1);
            return;
         case 166:
            return;
         case 168:
            if (super.f < 10) {
               super.fraImgEff.drawFrame(super.f / 2, super.x, super.y, super.Dir, 3, var1);
            }

            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               if ((var2 = (Point)this.VecEff.elementAt(var4)).frame < 2) {
                  super.fraImgSubEff.drawFrameNew(var2.fSmall * super.fraImgSubEff.maxNumFrame + var2.f / 2 % super.fraImgSubEff.maxNumFrame, var2.x, var2.y, 0, 3, var1);
               } else {
                  super.fraImgSub2Eff.drawFrameNew(var2.fSmall * super.fraImgSub2Eff.maxNumFrame + var2.f / 2 % super.fraImgSub2Eff.maxNumFrame, var2.x, var2.y, 0, 3, var1);
               }
            }

            return;
         case 169:
            if (super.f < 10) {
               super.fraImgEff.drawFrame(super.f / 2, super.x, super.y, super.Dir, 3, var1);
               return;
            }
            break;
         case 172:
            if (super.f < 12) {
               super.fraImgEff.drawFrameNew(super.f / 3, super.x, super.y, 0, 33, var1);
            }

            if (super.f > 5 && super.fraImgSubEff != null) {
               super.fraImgSubEff.drawFrame((super.f - 6) / 2 % super.fraImgSubEff.nFrame, super.x, super.y + super.objMainEff.hOne / 2, 0, 33, var1);
               return;
            }
            break;
         case 173:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
            return;
         case 174:
            super.fraImgEff.drawFrame(super.f / 2 % super.fraImgEff.nFrame, super.objMainEff.x, super.objMainEff.y, 0, 33, var1);
            return;
         case 175:
            super.fraImgSub3Eff.drawFrame(super.f / 2 % super.fraImgSub3Eff.nFrame, super.x, super.y + 73 + 10, CRes.random(2) << 1, 33, var1);
            super.fraImgSub2Eff.drawFrame(0, super.x, super.y, CRes.random(2) << 1, 17, var1);
            if (super.f % 2 == 0) {
               super.fraImgSubEff.drawFrame(0, super.x, super.y + 12, super.Dir, 3, var1);
            }

            super.fraImgEff.drawFrame(3, super.x, super.y, super.Dir, 3, var1);
            return;
         case 176:
            super.fraImgEff.drawFrame(super.f, super.x, super.y, super.Dir, 3, var1);
            return;
         case 177:
            if (super.f <= 6 && super.f % 2 != 0) {
               break;
            }

            super.fraImgEff.drawFrame(super.frame, super.x, super.y, 0, 3, var1);
            return;
         case 178:
            if (super.f > 15) {
               super.fraImgEff.drawFrame(super.f % super.fraImgEff.nFrame, super.toX, super.toY - 50, 0, 3, var1);
            }

            super.fraImgSub2Eff.drawFrame(0, super.toX, super.toY, 0, 3, var1);
            super.fraImgSubEff.drawFrame(super.f % super.fraImgSubEff.nFrame, super.x, super.y, 0, 3, var1);
            return;
         case 179:
            var3 = super.y;
            if (super.f < this.mposy.length) {
               var3 -= this.mposy[super.f];
            }

            if (this.typeSub % 10 > 3 && this.typeSub % 10 != 7) {
               super.fraImgEff.drawFrameNew((super.frame << 1) + super.f / 3 % 2, super.x, var3, 0, 3, var1);
               return;
            }

            super.fraImgEff.drawFrameNew(super.frame << 1, super.x, var3, 0, 3, var1);
         case 15:
         case 33:
         case 43:
         case 44:
         case 67:
         case 94:
         case 95:
         case 100:
         case 129:
         case 144:
         case 145:
         default:
            return;
         case 180:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               var9 = (Point)this.VecEff.elementAt(var8);
               if (super.f > 15) {
                  super.fraImgSubEff.drawFrameNew(super.f % super.fraImgSubEff.nFrame, var9.x, var9.y, super.Dir, 3, var1);
               }

               super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, var9.x, var9.y, 0, 3, var1);
            }

            return;
         case 181:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 33, var1);
            return;
         case 182:
            for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
               if ((var9 = (Point)this.VecEff.elementAt(var8)).f < this.mPlayFrame.length) {
                  super.fraImgEff.drawFrameNew(this.mPlayFrame[var9.f], var9.x, var9.y, super.Dir, 3, var1);
               }
            }

            return;
         case 183:
            if (this.typeSub == 0) {
               super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, super.Dir, 3, var1);
               return;
            }

            for(var4 = 0; var4 < this.VecEff.size(); ++var4) {
               var2 = (Point)this.VecEff.elementAt(var4);
               super.fraImgEff.drawFrame(var2.f / super.numNextFrame % super.fraImgEff.nFrame, var2.x, var2.y, super.Dir, 3, var1);
            }

            return;
         case 184:
            super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x, super.y, 0, 33, var1);
            return;
         case 185:
            if ((var3 = super.f) > 2) {
               var3 = 2 + CRes.random(2);
            }

            super.fraImgEff.drawFrame(var3, super.x, super.y, super.Dir, 3, var1);
            return;
         }
      } catch (Exception var7) {
         (new StringBuffer("loi End typeeff=")).append(super.typeEffect).append("   , error: ").append(var7.getMessage()).toString();
         var7.printStackTrace();
      }

   }

   private void paint_Lucci_L2(mGraphics var1) {
      int var2;
      Point var3;
      if (super.frame == 1) {
         for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
            var3 = (Point)this.VecEff.elementAt(var2);
            if (super.fraImgSubEff != null && super.fraImgSubEff.imgFrame != null) {
               super.fraImgSubEff.drawFrameNew(CRes.random(super.fraImgSubEff.maxNumFrame), var3.x, var3.y, 0, 3, var1);
            }

            super.fraImgEff.drawFrameNew(6 + var3.frame, var3.x, var3.y, 0, 3, var1);
         }
      } else {
         for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
            var3 = (Point)this.VecEff.elementAt(var2);
            super.fraImgEff.drawFrameNew(6 + var3.frame, var3.x, var3.y, 0, 3, var1);
         }
      }

      if (super.f < super.fRemove) {
         super.fraImgEff.drawFrameNew(6 + super.mframe[super.f], super.x, super.y, super.Dir, 3, var1);
      }

   }

   private void paintNo_Dat_New(mGraphics var1) {
      int var2 = 0;
      if (super.f < 2) {
         var2 = 1;
      } else if (super.f > super.fRemove - 4) {
         var2 = 2 - (super.fRemove - super.f) / 2;
      }

      super.fraImgEff.drawFrameNew(super.frame * 3 + var2, super.x, super.y, super.Dir, 3, var1);
   }

   public final void paint(mGraphics g, int xOBJ, int yOBJ) {
      int i;
      switch(super.typeEffect) {
      case 95:
         for(i = 0; i < this.VecEff.size(); ++i) {
            Point point = (Point)this.VecEff.elementAt(i);
            g.setColor(-1767416);
            g.fillRect(point.x + xOBJ, point.y + yOBJ, 1, point.dis);
         }

         return;
      case 100:
         super.fraImgEff.drawFrame(super.f / super.numNextFrame % super.fraImgEff.nFrame, super.x + xOBJ, super.y + yOBJ, super.Dir, 3, g);
         return;
      case 144:
      case 145:
         for(i = 0; i < this.VecEff.size(); ++i) {
            Line line;
            if ((line = (Line)this.VecEff.elementAt(i)) != null) {
               int color = 0;
               if (i / 2 < this.colorpaint.length) {
                  color = this.colorpaint[i / 2];
               }
               g.setColor(color);
               g.drawLine(xOBJ + line.x0 / 1000, yOBJ + line.y0 / 1000, xOBJ + line.x1 / 1000, yOBJ + line.y1 / 1000);
               if (line.is2Line) {
                  g.drawLine(xOBJ + line.x0 / 1000 + 1, yOBJ + line.y0 / 1000, xOBJ + line.x1 / 1000 + 1, yOBJ + line.y1 / 1000);
               }
            }
         }
      default:
      }
   }

   public final void update() {
      ++super.f;
      int var1;
      Point var2;
      Point var3;
      int num39;
      int var8;
      Point var9;
      byte var12;
      Point_Focus point_Focus5;
      byte var16;
      int var10000;
      int var10001;
      Point var18;
      switch(super.typeEffect) {
      case 0:
      case 1:
      case 3:
      case 4:
      case 5:
      case 8:
      case 9:
      case 10:
      case 86:
      case 90:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 2:
      case 34:
      case 45:
      case 46:
      case 52:
      case 53:
      case 57:
      case 58:
      case 61:
      case 63:
      case 64:
      case 65:
      case 66:
      case 68:
      case 71:
      case 77:
      case 80:
      case 81:
      case 82:
      case 89:
      case 91:
      case 92:
      case 100:
      case 109:
      case 118:
      case 121:
      case 122:
      case 123:
      case 124:
      case 125:
      case 133:
      case 134:
      case 136:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 6:
         if (this.typeSub == 3) {
            for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
               ++(var2 = (Point)this.VecEff.elementAt(var1)).f;
               if (var2.f >= super.fraImgSub2Eff.maxNumFrame) {
                  this.VecEff.removeElement(var1);
               }
            }

            if (super.f < super.fRemove && super.f % 3 == 0 && !GameCanvas.lowGraphic) {
               var9 = new Point(super.x, super.y);
               this.VecEff.addElement(var9);
            }
         }

         super.x += super.vx;
         super.y += super.vy;
         super.vy += super.vMax;
         if (super.f >= super.fRemove && (this.typeSub != 3 || this.VecEff.size() == 0)) {
            if (super.typeEffect == 128) {
               GameScreen.addEffectEnd((short)92, 0, super.x / 100, super.y / 100, super.Dir, super.objMainEff);
            }

            this.removeEff();
            return;
         }
         break;
      case 7:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (point_Focus5 = (Point_Focus)this.VecEff.elementAt(num39)).update_Vx_Vy();
            if (point_Focus5.AG >= point_Focus5.fRe) {
               this.VecEff.removeElement(point_Focus5);
               LoginScreen.AA((short)1, 0, point_Focus5.AK, point_Focus5.AL, super.Dir, super.objMainEff);
               --num39;
            }
         }

         if (super.f == 10 || super.f == 13 || super.f == 16 || super.f == 19) {
            super.toX = super.x + (super.objFireMain.Dir == 0 ? -140 : 140);
            super.toY = super.y;
            this.setAngle();
            num39 = super.toX - super.x;
            var8 = super.toY - super.y;
            var1 = CRes.AA(num39, var8);
            Point_Focus var10 = new Point_Focus();
            (var10 = this.create_Speed(num39, var8, var10)).frame = this.setFrameAngle(var1);
            this.VecEff.addElement(var10);
            LoginScreen.AA((short)3, 0, super.x, super.y, super.Dir, super.objMainEff);
         }

         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 11:
         this.updateZoro4();
         return;
      case 12:
      case 19:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 13:
         this.updateENDLuffy1();
         return;
      case 14:
      case 18:
         super.y += super.vy;
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 15:
         if (super.f == 5) {
            var16 = 20;
            if (super.Dir == 0) {
               var16 = -20;
            }

            LoginScreen.AA((short)16, 1, super.x + var16, super.objMainEff.y - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
         }

         if (super.f == 10) {
            var16 = 30;
            if (super.Dir == 0) {
               var16 = -30;
            }

            LoginScreen.AA((short)16, 2, super.x + var16, super.objFireMain.y - super.objFireMain.hOne / 2 + 5, super.Dir, super.objMainEff);
         }

         if (super.f == 15) {
            var16 = 20;
            if (super.Dir == 0) {
               var16 = -20;
            }

            LoginScreen.AA((short)16, 1, super.x + var16, super.objFireMain.y - super.objFireMain.hOne / 2 - 10 + 5, super.Dir, super.objMainEff);
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 16:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 17:
         if (super.f >= super.fRemove + 6) {
            this.removeEff();
            return;
         }
         break;
      case 20:
      case 26:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 21:
      case 105:
         this.updateXuyenGiap();
         return;
      case 22:
         this.updateHutMP_HP();
         return;
      case 23:
         this.updatePhanDamage();
         return;
      case 24:
         this.updateFocusTouch();
         return;
      case 25:
         this.updateLuffy_6();
         return;
      case 27:
         this.updateZoro9();
         return;
      case 28:
         super.y += super.vy;
         if (super.f >= super.fRemove) {
            this.removeEff();
         }

         if (super.f % 6 == 3 && super.f < 16) {
            GameScreen.addEffectEnd((short)50, 0, super.x + CRes.random_Am_0(20), super.y, super.Dir, super.objMainEff);
         }

         if (this.objTo != null && this.objTo.Action != 4) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne / 2;
            return;
         }
         break;
      case 29:
         this.updateENDSanji2();
         return;
      case 30:
         if (super.f > super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 31:
         super.y += super.vy;
         if (super.f == 6) {
            (var9 = new Point(super.x, super.y)).vy = 0;
            var9.dis = 1;
            var9.frame = 0;
            var9.fRe = 10;
            this.VecEff.addElement(var9);
         }

         if (super.f == 8) {
            super.vy = -25;
         }

         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point)this.VecEff.elementAt(var1)).update();
            if (var2.dis == 1) {
               var2.vy = super.vy;
            }

            if (var2.f >= var2.fRe) {
               this.VecEff.removeElement(var2);
               --var1;
            }
         }

         if (super.f < 10 && super.f % 3 == 2) {
            for(var1 = 0; var1 < 4; ++var1) {
               (var2 = new Point(super.x + CRes.random_Am_0(20), super.y - CRes.random(30) + 10)).vy = -2 - CRes.random(4);
               var2.dis = 0;
               var2.frame = CRes.random(super.fraImgSubEff.nFrame);
               var2.fRe = CRes.random(12, 20);
               this.VecEff.addElement(var2);
            }
         }

         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 32:
         if (this.objTo != null && super.f > 5) {
            super.x = this.objTo.x;
            super.y = this.objTo.y;
         }

         if (super.f < super.fRemove && (super.f % 3 == 2 || super.f < 3)) {
            for(var1 = 0; var1 < 4; ++var1) {
               (var2 = new Point(super.x + CRes.random_Am_0(20), super.y + CRes.random(30) - 72)).vy = 2 + CRes.random(4);
               var2.dis = 0;
               var2.frame = CRes.random(super.fraImgSubEff.nFrame);
               var2.fRe = CRes.random(12, 20);
               this.VecEff.addElement(var2);
            }
         }

         if (super.f == 5) {
            super.vy = 0;
         }

         super.y += super.vy;

         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point)this.VecEff.elementAt(var1)).update();
            if (var2.dis == 1) {
               var2.vy = super.vy;
            }

            if (var2.f >= var2.fRe) {
               this.VecEff.removeElement(var2);
               --var1;
            }
         }

         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 33:
         if (super.f % 5 == 0) {
            var12 = 28;
            if (super.Dir == 0) {
               var12 = -28;
            }

            LoginScreen.AA((short)25, 2, super.objMainEff.x + var12, super.objMainEff.y - super.objMainEff.dy - (super.objMainEff.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 35:
         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 36:
         if (super.f <= super.fRemove) {
            super.x += super.vx;
            super.y += super.vy;
         }

         if (super.f > super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 37:
         this.updateSanji_5();
         return;
      case 38:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 39:
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne - 20;
         }

         if (super.f > super.fRemove) {
            this.removeEff();
            if (this.objTo != null) {
               if (this.typeSub != 3) {
                  GameScreen.addEffectEnd_ObjTo((short)40, this.typeSub, this.objTo.x, this.objTo.y - this.objTo.hOne - 20, (short)this.objTo.ID, (byte)this.objTo.typeObject, this.typeSub, super.objMainEff);
               } else {
                  GameScreen.addEffectEnd_ObjTo((short)175, 0, this.objTo.x, this.objTo.y - this.objTo.hOne - 20, (short)this.objTo.ID, (byte)this.objTo.typeObject, this.typeSub, super.objMainEff);
               }

               GameScreen.addEffectEnd((short)108, 8, this.objTo.x, this.objTo.y - this.objTo.hOne / 2, (byte)0, super.objMainEff);
               return;
            }

            if (this.typeSub != 3) {
               GameScreen.addEffectEnd((short)40, this.typeSub, super.x, super.y, super.Dir, super.objMainEff);
               return;
            }

            GameScreen.addEffectEnd((short)175, 0, super.x, super.y, super.Dir, super.objMainEff);
            return;
         }
         break;
      case 40:
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne - 20;
            if (super.f == 4) {
               GameScreen.addEffectEnd((short)38, 0, this.objTo.x, this.objTo.y - this.objTo.hOne / 2, super.Dir, super.objMainEff);
            }
         }

         if (super.f == 4 && !GameCanvas.lowGraphic && ((var1 = GameCanvas.loadmap.AA(super.x, super.y + 85)) == 0 || var1 == 2)) {
            GameScreen.addEffectEnd((short)63, 0, super.x - 10, super.y + 75, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)110, 0, super.x, super.y + 75, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)63, 0, super.x + 10, super.y + 75, super.Dir, super.objMainEff);
         }

         if (super.f > super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 41:
         if (this.typeSub == 1 && super.f == 1 && this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne - 20;
            GameScreen.addEffectEnd_ObjTo((short)39, 0, this.objTo.x, this.objTo.y - this.objTo.hOne - 20, (short)this.objTo.ID, (byte)this.objTo.typeObject, (byte)0, super.objMainEff);
         }

         if (super.f > super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 42:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 43:
      case 67:
      case 94:
      case 129:
      case 142:
      case 169:
      case 172:
      case 173:
      case 174:
      case 176:
      default:
         if (super.f >= super.fRemove) {
            this.removeEff();
         }
         break;
      case 44:
         if (super.f % 4 == 0) {
            var16 = 25;
            if (super.Dir == 0) {
               var16 = -25;
            }

            LoginScreen.AA((short)35, 0, super.objFireMain.x + var16, super.objFireMain.y - super.objFireMain.dy - (super.objFireMain.hOne / 3 << 1) + 10, super.Dir, super.objMainEff);
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 47:
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point)this.VecEff.elementAt(var1)).update();
            if (var2.f >= var2.fRe) {
               this.VecEff.removeElement(var2);
               --var1;
            }
         }

         if (this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 48:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 49:
         super.x += super.vx;
         super.y += super.vy;
         if (super.f >= super.fRemove) {
            GameScreen.addEffectEnd((short)50, 0, this.objTo.x, this.objTo.y - this.objTo.hOne / 2, super.Dir, super.objMainEff);
            this.setAva(2, this.objTo);
            this.removeEff();
            return;
         }
         break;
      case 50:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 51:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 54:
         this.updateLuffy_S1_Final();
         return;
      case 55:
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y;
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 56:
         if (super.f == 3 && this.objTo != null && this.objTo.Action != 4) {
            this.objTo.x = super.x;
            this.objTo.y = super.y;
            this.objTo.toX = super.x;
            this.objTo.toY = super.y;
         }

         if (super.f >= super.fRemove) {
            this.objTo.isTanHinh = false;
            this.removeEff();
            return;
         }
         break;
      case 59:
         this.updateRock();
         return;
      case 60:
      case 107:
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            ((Point)this.VecEff.elementAt(var1)).update();
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 62:
         if (super.f <= super.fRemove - 20 && super.f % 2 == 1) {
            var9 = new Point(super.x, super.y);
            if (super.f == 1) {
               var9.frame = 0;
            } else if (super.f == super.fRemove - 20) {
               var9.frame = 2;
            } else {
               var9.frame = 1;
            }

            this.VecEff.addElement(var9);
         }

         super.x += super.vx;
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 69:
      case 83:
         if (GameCanvas.timeNow - super.timeBegin >= (long)super.timeEnd) {
            this.removeEff();
         }

         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            if ((var2 = (Point)this.VecEff.elementAt(var1)).vy > 0 && var2.y >= 0 || var2.vy < 0 && var2.y <= -30) {
               var2.vy = -var2.vy;
            }

            var2.y += var2.vy;
         }

         return;
      case 70:
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            if ((var2 = (Point)this.VecEff.elementAt(var1)).vy > 0 && var2.y >= 0 || var2.vy < 0 && var2.y <= -30) {
               var2.vy = -var2.vy;
            }

            var2.y += var2.vy;
         }

         if (super.f == 30) {
            this.objTo.x = super.toX;
            this.objTo.y = super.toY;
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 72:
         super.x += super.vx;
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 73:
      case 74:
         if (this.typeSub == 3) {
            super.x += super.vMax;
            super.toY += super.vMax;
         } else if (this.typeSub == 0) {
            super.toX -= super.vMax;
            super.y += super.vMax;
         } else if (this.typeSub == 1) {
            super.x -= super.vMax;
            super.toY -= super.vMax;
         } else if (this.typeSub == 2) {
            super.toX += super.vMax;
            super.y -= super.vMax;
         } else if (this.typeSub == 4) {
            super.toX += super.vMax;
            super.y += super.vMax;
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 75:
      case 78:
         if (super.f > 10) {
            super.x += super.vx;
            super.y += super.vy;
         }

         if (this.typeSub != 1 && super.f < super.fRemove + 10) {
            var1 = CRes.random(1, 4);
            if (this.typeSub == 0) {
               var1 = 1;
            }

            for(num39 = 0; num39 < var1; ++num39) {
               (var3 = new Point(super.x + CRes.random_Am_0(4), super.y + CRes.random_Am_0(4))).frame = CRes.random(3);
               var3.vy = -1 + CRes.random(3);
               var3.vx = CRes.random_Am_0(2);
               var3.fRe = 8 + CRes.random(4);
               this.VecEff.addElement(var3);
            }
         }

         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point)this.VecEff.elementAt(var1)).update();
            ++var2.vy;
            if (var2.f >= var2.fRe) {
               this.VecEff.removeElement(var2);
               --var1;
            }
         }

         if (super.f >= super.fRemove + 10 && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 76:
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point)this.VecEff.elementAt(var1)).update();
            if (var2.f >= var2.fRe) {
               this.VecEff.removeElement(var2);
               --var1;
            }
         }

         if (this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 79:
         super.y += super.vy;
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 84:
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point)this.VecEff.elementAt(var1)).update();
            if (var2.f > var2.fRe) {
               if (var2.f - var2.fRe == 2) {
                  var2.frame = 2;
               }

               if (var2.f - var2.fRe == 4) {
                  var2.frame = 3;
               }

               if (var2.f - var2.fRe == 6) {
                  this.VecEff.removeElement(var2);
                  --var1;
               }
            }
         }

         if (this.VecEff.size() == 0 && super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 85:
         this.updateLineBuff();
         return;
      case 87:
      case 128:
         super.x += super.vx;
         super.y += super.vy;
         super.vy += super.vMax;
         if (super.f >= super.fRemove) {
            if (super.typeEffect == 128) {
               if (this.typeSub > 20) {
                  GameScreen.addEffectEnd((short)4, 0, super.x / 100, super.y / 100, super.Dir, super.objMainEff);
               } else {
                  GameScreen.addEffectEnd((short)92, 0, super.x / 100, super.y / 100, super.Dir, super.objMainEff);
               }
            }

            this.removeEff();
            return;
         }
         break;
      case 88:
         super.y += super.vy;
         if (super.vy == 0 && this.mPlayFrame[super.f] == 4) {
            super.vy = 3;
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 93:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (var3 = (Point)this.VecEff.elementAt(num39)).update();
            ++var3.vy;
            if (var3.f >= var3.fRe) {
               this.VecEff.removeElement(var3);
               --num39;
            }
         }

         if (super.f > 0 && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 95:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (var3 = (Point)this.VecEff.elementAt(num39)).update();
            if (var3.y < var3.y2 - 60) {
               var3.y = var3.y2;
            }
         }

         return;
      case 96:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            ++(point_Focus5 = (Point_Focus)this.VecEff.elementAt(num39)).AG;
            if (point_Focus5.AG < point_Focus5.fRe) {
               point_Focus5.x += point_Focus5.AI;
               point_Focus5.y += point_Focus5.vy;
            } else {
               if (this.objTo != null) {
                  point_Focus5.AI = 0;
                  point_Focus5.vy = 0;
                  point_Focus5.x = this.objTo.x;
                  point_Focus5.y = this.objTo.y - this.objTo.hOne / 2;
               }

               if (this.objTo == null || this.objTo.Action == 4 || this.objTo.returnAction()) {
                  this.VecEff.removeAllElements();
                  this.removeEff();
               }
            }
         }

         if (GameCanvas.timeNow - this.time >= (long)this.timeRemove) {
            for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
               point_Focus5 = (Point_Focus)this.VecEff.elementAt(num39);

               for(var1 = 0; var1 < 3; ++var1) {
                  GameScreen.addEffectEnd((short)4, 0, point_Focus5.x - 20 + var1 * 20, point_Focus5.y - 20 + var1 % 2 * 20, super.Dir, super.objMainEff);
               }
            }

            this.VecEff.removeAllElements();
            this.removeEff();
            return;
         }
         break;
      case 97:
         super.x += super.vx;
         super.y += super.vy;
         if (super.f == super.fRemove && this.objTo != null) {
            GameScreen.addEffectEnd((short)35, 0, this.objTo.x, this.objTo.y - this.objTo.hOne / 2, super.Dir, super.objMainEff);
         }

         if (super.f >= super.fRemove + 5) {
            this.removeEff();
            return;
         }
         break;
      case 98:
      case 99:
         if (super.f % 3 == 0 && super.f <= super.fRemove - 5) {
            var2 = new Point(super.x + CRes.random_Am_0(30), super.y + CRes.random_Am_0(10));
            this.VecEff.addElement(var2);
         }

         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
         }

         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            ++(var3 = (Point)this.VecEff.elementAt(num39)).f;
            if (var3.f >= 5) {
               this.VecEff.removeElement(var3);
               --num39;
            }
         }

         return;
      case 101:
      case 104:
         if (super.f == 3 || super.f == 6) {
            num39 = CRes.random(4, 7);

            for(var8 = 0; var8 < num39; ++var8) {
               (var9 = new Point(super.x + CRes.random_Am_0(20), super.y + CRes.random_Am_0(10))).fRe = CRes.random(8, 11);
               var9.vy = -CRes.random(5, 7);
               this.VecEff.addElement(var9);
            }
         }

         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (var3 = (Point)this.VecEff.elementAt(num39)).update();
            if (var3.f > var3.fRe) {
               this.VecEff.removeElement(var3);
               --num39;
            }
         }

         if (this.VecEff.size() == 0 && super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 102:
         if (this.objTo != null) {
            if (this.typeSub == 0) {
               super.x = this.objTo.x + 6;
            } else {
               super.x = this.objTo.x - 6;
            }

            super.y = this.objTo.y - this.objTo.hOne / 2 + 14;
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 103:
         if (super.f == 15) {
            super.levelPaint = -1;
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 106:
         if (this.objTo != null && this.objTo.Action == 4) {
            this.objTo.Action = 2;
            this.objTo.frame = 10;
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 108:
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            if ((var2 = (Point)this.VecEff.elementAt(var1)).f % 2 == 0) {
               ++var2.vy;
            }

            var2.update();
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 110:
      case 115:
         this.updateRockNew();
         return;
      case 111:
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y;
         }

         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (var3 = (Point)this.VecEff.elementAt(num39)).update();
            if (var3.y < -210) {
               this.VecEff.removeElement(num39);
               --num39;
            }
         }

         if (super.f % 2 == 1) {
            for(num39 = 0; num39 < 3; ++num39) {
               (var3 = new Point(CRes.random_Am_0(20), -5 + CRes.random(10))).vy = -CRes.random(12, 20);
               var3.frame = CRes.random(super.fraImgSubEff.nFrame);
               this.VecEff.addElement(var3);
            }
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 112:
         if (super.f >= super.fRemove - 6) {
            super.frame = 3 - (super.fRemove - super.f) / 2;
         } else {
            super.frame = 0;
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 113:
         if (super.f == 1) {
            GameScreen.addEffectEnd((short)63, 3, super.x, super.y, super.Dir, super.objMainEff);
         }

         if (super.f < 8) {
            byte var14 = 4;
            var12 = -1;
            if (this.typeSub == 1) {
               var14 = 5;
               var12 = 0;
            } else if (this.typeSub == 2) {
               var14 = 8;
            }

            int var4 = CRes.random(num39 = 360 / var14);

            for(int var5 = 0; var5 < var14; ++var5) {
               Point var6;
               (var6 = new Point()).x = super.x * 1000 + CRes.getcos(CRes.fixangle(var4 + num39 * var5) + super.f * 5) * super.f * super.vMax;
               var6.y = super.y * 1000 + CRes.getsin(CRes.fixangle(var4 + num39 * var5) + super.f * 5) * super.f * (super.vMax - 4);
               var10001 = super.f;
               var6.dis = 0;
               var6.fSmall = 1;
               if (this.typeSub == 2) {
                  if (var5 % 2 == 0) {
                     var6.frame = 1;
                  }
               } else if (var12 == var5) {
                  var6.frame = 1;
               }

               this.VecEff.addElement(var6);
            }
         }

         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            ++(var3 = (Point)this.VecEff.elementAt(num39)).f;
            if (var3.f == 1) {
               if (var3.dis == 0) {
                  if ((var1 = GameCanvas.loadmap.AA(var3.x / 1000, var3.y / 1000)) != 0 && var1 != 2) {
                     var3.AW = true;
                  } else {
                     GameScreen.addEffectEnd((short)66, var3.frame == 1 ? 0 : 1, var3.x / 1000, var3.y / 1000, super.Dir, super.objMainEff);
                  }
               }

               if (CRes.random(6) == 0) {
                  GameScreen.addEffectEnd((short)110, var3.frame == 1 ? 0 : 1, var3.x / 1000, var3.y / 1000, super.Dir, super.objMainEff);
               }
            }

            if (var3.f / 3 >= super.fraImgSubEff.maxNumFrame || var3.AW) {
               this.VecEff.removeElement(var3);
               --num39;
            }
         }

         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 114:
         super.x += super.vx;
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 116:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (var3 = (Point)this.VecEff.elementAt(num39)).update();
            if (var3.f >= var3.fRe) {
               this.VecEff.removeElement(var3);
               --num39;
            }
         }

         if (super.f % 5 == 1 && super.f < super.fRemove && CRes.random(2) == 0) {
            (var2 = new Point()).x = super.x + CRes.random_Am_0(10);
            var2.y = super.y + CRes.random(20);
            var2.vx = CRes.random_Am_0(3);
            var2.vy = -CRes.random(3, 7);
            var2.fRe = CRes.random(12, 18);
            this.VecEff.addElement(var2);
         }

         if (super.f % 4 == 1 && super.f < super.fRemove) {
            (var2 = new Point()).x = super.x + CRes.random_Am_0(15);
            var2.y = super.y + CRes.random(20);
            var2.vx = CRes.random_Am_0(3);
            var2.vy = -CRes.random(3, 7);
            var2.fRe = CRes.random(4, 7);
            this.VecEff.addElement(var2);
         }

         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 117:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 119:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (var3 = (Point)this.VecEff.elementAt(num39)).update();
            if (var3.f >= var3.fRe) {
               this.VecEff.removeElement(num39);
               --num39;
            }
         }

         if (super.f % 3 == 0 && super.f < super.fRemove) {
            (var2 = new Point()).x = super.x;
            var2.y = super.y;
            if (super.Dir == 0) {
               var2.vx = -6;
            } else {
               var2.vx = 6;
            }

            var2.fRe = this.mPlayFrame.length;
            this.VecEff.addElement(var2);
         }

         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 120:
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point)this.VecEff.elementAt(var1)).update();
            ++var2.vy;
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 126:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (point_Focus5 = (Point_Focus)this.VecEff.elementAt(num39)).update_Vx_Vy();
            if (point_Focus5.AG >= point_Focus5.fRe) {
               this.VecEff.removeElement(num39);
               --num39;
            }
         }

         Point_Focus point_Focus6;
         if (super.step == 2 && this.VecEff.size() == 0) {
            super.step = 3;
            (point_Focus6 = new Point_Focus()).frame = 1;
            point_Focus6.fRe = 8;
            point_Focus6.x = super.x / 100;
            point_Focus6.y = super.y / 100;
            point_Focus6.vy = -4;
            this.VecEff.addElement(point_Focus6);
            GameScreen.addEffectEnd((short)108, 7, super.x / 100 - 4, super.y / 100, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)108, 7, super.x / 100 + 4, super.y / 100, super.Dir, super.objMainEff);
         }

         if (super.step == 1) {
            ++super.frame;
            if (super.frame == 8) {
               super.step = 2;
               super.x1000 = super.objMainEff.x;
               super.y1000 = super.objMainEff.y - 15;
               point_Focus6 = new Point_Focus();
               var8 = super.x / 100 - super.x1000;
               var1 = super.y / 100 - super.y1000;
               point_Focus6.frame = 0;
               this.create_Speed(var8, var1, point_Focus6, super.x1000, super.y1000, super.x / 100, super.y / 100);
               this.VecEff.addElement(point_Focus6);
               super.objMainEff.isDie = true;
            }
         }

         if (super.step == 0) {
            super.x += super.vx;
            super.y += super.vy;
            super.vy += super.vMax;
            if (super.vy >= 0) {
               super.vx = 0;
               super.vy = 0;
               super.vMax = 6;
               super.step = 1;
               GameScreen.addEffectEnd((short)85, 0, super.x / 100, super.y / 100, 500, super.Dir, super.objMainEff);
               super.frame = 0;
            }
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 127:
         if (super.f < 4) {
            super.objMainEff.frame = 33;
            var10000 = this.objTo.x;
            var10001 = super.objMainEff.x;
            super.objMainEff.type_left_right = super.Dir;
         }

         super.x += super.vx;
         super.y += super.vy;
         if (super.f >= super.fRemove) {
            if (this.typeSub >= 20) {
               GameScreen.addEffectEnd((short)128, this.typeSub, super.x, super.y, (byte)0, this.objTo);
            } else if (this.typeSub % 10 > 3 && this.typeSub % 10 != 7) {
               if (this.objTo != null) {
                  if (this.typeSub >= 10) {
                     this.objTo.BC = T.RT;
                  } else {
                     this.objTo.BC = T.RU;
                  }
               }
            } else if (this.typeSub >= 10) {
               GameScreen.addEffectEnd((short)128, this.typeSub, super.x, super.y, (byte)0, this.objTo);
            } else {
               GameScreen.addEffectEnd((short)126, this.typeSub, super.x, super.y, (byte)0, this.objTo);
            }

            this.removeEff();
            return;
         }
         break;
      case 130:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            var18 = var3 = (Point)this.VecEff.elementAt(num39);
            var18.x += var3.vx;
            var3.y += var3.vy;
            if (var3.y <= -60) {
               this.VecEff.removeElement(num39);
               --num39;
            }
         }

         if (super.f % 10 == 0) {
            for(num39 = 0; num39 < 5; ++num39) {
               (var3 = new Point()).x = CRes.random_Am_0(15);
               var3.y = 10 - CRes.random(40);
               var3.dis = 1 + CRes.random(3);
               var3.vy = -4;
               var3.frame = CRes.random(12);
               this.VecEff.addElement(var3);
            }
         }

         if ((GameCanvas.timeNow - this.time) / 1000L >= (long)this.timeRemove || this.objTo == null) {
            this.removeEff();
            return;
         }
         break;
      case 131:
      case 152:
         if ((GameCanvas.timeNow - this.time) / 1000L >= (long)this.timeRemove || this.objTo == null) {
            this.removeEff();
            return;
         }
         break;
      case 132:
         super.x += super.vx;
         if (this.typeSub == 1 && super.vx <= 20) {
            super.vx -= 2;
            if (super.Dir == 2) {
               super.vx += 2;
            }
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 135:
         super.x += super.vx;
         super.vx /= 2;
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 137:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            var18 = var3 = (Point)this.VecEff.elementAt(num39);
            var18.x += super.vx;
            ++var3.f;
            if (var3.f >= var3.fRe) {
               this.VecEff.removeElement(num39);
               --num39;
            }
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 138:
         this.updateNami_4();
         return;
      case 139:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 140:
      case 167:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            var3 = (Point)this.VecEff.elementAt(num39);
            if (CRes.random(3) == 0) {
               var3.x = super.x + CRes.random_Am_0(3);
               var3.y = super.y + CRes.random_Am_0(3);
            }
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 141:
      case 148:
      case 149:
      case 151:
      case 159:
      case 170:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 143:
         super.y += super.vy;
         if (super.vy < 0) {
            ++super.vy;
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 144:
         if (super.f > super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 145:
         this.updateLineIn();
         return;
      case 146:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            ((Point)this.VecEff.elementAt(num39)).update();
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 147:
         this.update_Lucci_L2();
         return;
      case 150:
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne;
         }

         if ((GameCanvas.timeNow - this.time) / 1000L >= (long)this.timeRemove || this.objTo == null) {
            this.removeEff();
            return;
         }
         break;
      case 153:
         if (super.f == 20) {
            super.frame = 1;
         } else if (super.f == 40) {
            super.frame = 2;
         } else if (super.f == 50) {
            super.vy = -1;
         } else if (super.f == 60) {
            super.vy = -2;
         }

         if (super.f >= 50) {
            if (CRes.random(6) == 0) {
               super.vx = CRes.random_Am(1, 2);
            } else {
               super.vx = 0;
            }
         } else if (CRes.random(10) == 0) {
            super.vx = CRes.random_Am(1, 2);
         } else {
            super.vx = 0;
         }

         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (var3 = (Point)this.VecEff.elementAt(num39)).update();
            if (var3.x < MainScreen.cameraMain.xCam) {
               var3.x += MotherCanvas.w;
            } else if (var3.x > MainScreen.cameraMain.xCam + MotherCanvas.w) {
               var3.x -= MotherCanvas.w;
            }

            if (var3.f >= var3.fRe) {
               this.VecEff.removeElement(num39);
               --num39;
            }
         }

         if (super.f >= 140 && super.f <= 180) {
            for(num39 = 0; num39 < MotherCanvas.w / 10 - 1; ++num39) {
               if (CRes.random(4) == 0) {
                  (var3 = new Point(MainScreen.cameraMain.xCam + 5 + num39 * 10 + CRes.random(3), super.y)).vy = 5;
                  var3.vx = CRes.random_Am_0(2);
                  var3.fRe = MotherCanvas.h / 5 + CRes.random_Am_0(10);
                  var3.frame = CRes.random(9);
                  this.VecEff.addElement(var3);
               }
            }
         }

         super.x += super.vx;
         super.y += super.vy;
         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 154:
      case 155:
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point)this.VecEff.elementAt(var1)).x = CRes.getcos(CRes.fixangle(var2.dis)) * super.vMax / 1000 + super.x;
            var2.y = CRes.getsin(CRes.fixangle(var2.dis)) * super.vMax / 1000 + super.y;
            var2.dis += 30;
         }

         super.vMax -= 2;
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 156:
         if ((GameCanvas.timeNow - this.time) / 100L >= (long)this.timeRemove || this.objTo == null) {
            this.removeEff();
         }

         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne;
         }

         super.y1000 += super.vy;
         if (super.vy < 0) {
            ++super.vy;
            return;
         }
         break;
      case 157:
         super.x += super.vx;
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 158:
         if (super.f % 3 == 0 || super.f >= 30) {
            GameScreen.addEffectEnd((short)108, (byte)CRes.random(7, 9), super.x - super.am_duong * 5, super.y + 5, (byte)0, super.objMainEff);
         }

         if (super.f >= 30) {
            GameScreen.addEffectEnd((short)108, (byte)CRes.random(7, 9), super.x + super.am_duong * 5, super.y - 5, (byte)0, super.objMainEff);
         }

         if (super.f >= 30 && super.f <= 36) {
            super.vy -= 3;
            super.vx -= super.am_duong;
         }

         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (var3 = (Point)this.VecEff.elementAt(num39)).update();
            if (var3.x < MainScreen.cameraMain.xCam) {
               var3.x += MotherCanvas.w;
            } else if (var3.x > MainScreen.cameraMain.xCam + MotherCanvas.w) {
               var3.x -= MotherCanvas.w;
            }

            if (var3.f >= var3.fRe) {
               this.VecEff.removeElement(num39);
               --num39;
            }
         }

         if (super.f == 50) {
            super.levelPaint = 0;
         }

         if (super.f >= 40 && super.f <= 48 && super.f % 2 == 0 && !GameCanvas.lowGraphic) {
            num39 = MainScreen.cameraMain.xCam + 20 + (super.f - 40) / 2 * (MotherCanvas.w / 5);
            if (super.Dir == 2) {
               num39 = MainScreen.cameraMain.xCam + MotherCanvas.w - 20 - (super.f - 40) / 2 * (MotherCanvas.w / 5);
            }

            GameScreen.addEffectEnd((short)120, 0, num39, MainScreen.cameraMain.yCam + 50, super.Dir, super.objMainEff);
         }

         if (super.f >= 50 && super.f <= 54 && super.f % 2 == 0) {
            num39 = MainScreen.cameraMain.xCam + 40 + (super.f - 50) / 2 * (MotherCanvas.w / 3);
            if (super.Dir == 2) {
               num39 = MainScreen.cameraMain.xCam + MotherCanvas.w - 20 - (super.f - 50) / 2 * (MotherCanvas.w / 3);
            }

            GameScreen.addEffectEnd((short)120, 0, num39, MainScreen.cameraMain.yCam + 30, super.Dir, super.objMainEff);
         }

         if (super.f >= 40 && super.f <= 90) {
            for(num39 = 0; num39 < MotherCanvas.w / 10 - 1; ++num39) {
               if (CRes.random(4) == 0) {
                  (var3 = new Point(MainScreen.cameraMain.xCam + 5 + num39 * 10 + CRes.random(3), MainScreen.cameraMain.yCam - 10)).vy = 5;
                  var3.vx = CRes.random_Am_0(2);
                  var3.fRe = MotherCanvas.h / 5 + CRes.random_Am_0(10);
                  var3.frame = CRes.random(9);
                  this.VecEff.addElement(var3);
               }
            }
         }

         super.x += super.vx;
         super.y += super.vy;
         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 160:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            var18 = var3 = (Point)this.VecEff.elementAt(num39);
            var18.x += var3.vx;
            var3.y += var3.vy;
            if (super.f >= 6 && super.f <= 12) {
               var3.vy = 1;
            } else {
               var3.vy = -1;
            }

            if (super.f > 10) {
               if (num39 != 0 && num39 != this.VecEff.size() - 1) {
                  var3.frame = 2 + CRes.random(2);
               } else {
                  var3.AW = true;
               }
            } else if (super.f > 14 && num39 == 1) {
               var3.AW = true;
            }
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 161:
         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (var3 = (Point)this.VecEff.elementAt(num39)).update();
            if (var3.f >= var3.fRe) {
               this.VecEff.removeElement(num39);
               --num39;
            }
         }

         if (super.f < 20 - this.randomf) {
            if (CRes.random(4) == 0) {
               super.vx = -super.vx;
            }
         } else {
            if (super.vx > 0) {
               --super.vx;
            }

            if (super.vx < 0) {
               ++super.vx;
            }

            if (super.vy < 0) {
               ++super.vy;
            }
         }

         if (super.f == 20 + this.lengthM - this.randomf) {
            num39 = 7 + CRes.random(3);

            for(var8 = 0; var8 < num39; ++var8) {
               (var9 = new Point()).x = super.x * 100 + CRes.random_Am_0(200);
               var9.y = super.y * 100 + CRes.random_Am_0(200);
               var9.vx = CRes.random_Am_0(200);
               var9.vy = CRes.random_Am_0(200);
               var9.frame = 2 + CRes.random(2);
               var9.fRe = 6 + CRes.random(3);
               this.VecEff.addElement(var9);
            }
         }

         super.x += super.vx;
         super.y += super.vy;
         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 162:
         if (super.f == 20) {
            super.vy = 0;
         }

         if (super.f == 100 || super.f == 105 || super.f == 110) {
            float var17 = mSound.volumeSound;
            mSound.playSound();
            GameScreen.addEffectEnd((short)10, 0, this.objTo.x, this.objTo.y - this.objTo.dy - this.objTo.hOne / 2, super.Dir, this.objTo);
         }

         if (super.f > 30 && super.f < 120) {
            this.objTo.dy = (super.f - 20) / 10;
         }

         super.y += super.vy;
         super.x += super.vx;
         if (super.f == 114) {
            super.x = this.objTo.x * 100;
            super.y = (this.objTo.y - this.objTo.dy - this.objTo.hOne / 2) * 100;
            super.vx = 50;
            if (this.objTo.type_left_right == 0) {
               super.vx = -50;
            }

            super.vy = -15;
         }

         if (super.f >= super.fRemove) {
            if (this.objTo == GameScreen.player) {
               Player.isSendMove = true;
               GameCanvas.tabAllScr.AE = 1;
               GameCanvas.tabAllScr.Show((MainScreen)GameCanvas.gameScr);
               GameCanvas.tabAllScr.AD = 0;
               GameScreen.player.resetAction();
               GameCanvas.clearAll();
            }

            GameScreen.isPaintNormal();
            this.removeEff();
            return;
         }
         break;
      case 163:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 164:
         ++super.CFrame;
         if (super.CFrame > 2) {
            super.CFrame = 0;
         }

         boolean var11 = true;

         for(var8 = 0; var8 < this.VecEff.size(); ++var8) {
            if ((var9 = (Point)this.VecEff.elementAt(var8)).f == 0) {
               var11 = false;
               if (super.f >= super.fRemove) {
                  var9.f = 1;
               } else {
                  var9.x += var9.vx;
                  var9.y += var9.vy;
               }
            } else if (var9.f > 0) {
               ++var9.f;
               if ((var9.f - 1) / 2 >= 5) {
                  var9.f = -1;
                  this.VecEff.removeElement(var9);
               }

               var11 = false;
            }
         }

         if (var11) {
            this.removeEff();
            return;
         }
         break;
      case 165:
         if (GameCanvas.gameTick % 2 == 0) {
            ++super.CFrame;
         }

         if (super.CFrame > 3) {
            super.CFrame = 0;
         }

         if (super.objMainEff.typeEfffashion != 5) {
            this.removeEff();
            return;
         }
         break;
      case 166:
         ++this.tframe;
         if (this.tframe > this.arrFrame.length) {
            this.tframe = 0;
            this.removeEff();
         }

         if (this.tframe < this.arrFrame.length - 1) {
            super.CFrame = this.arrFrame[this.tframe];
            return;
         }
         break;
      case 168:
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point)this.VecEff.elementAt(var1)).update();
            ++var2.vy;
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 171:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 175:
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne - 20;
            if (super.f == 4) {
               GameScreen.addEffectEnd((short)38, 0, this.objTo.x, this.objTo.y - this.objTo.hOne / 2, super.Dir, super.objMainEff);
            }
         }

         if (super.f == 4 && !GameCanvas.lowGraphic && ((var1 = GameCanvas.loadmap.AA(super.x, super.y + 85)) == 0 || var1 == 2)) {
            GameScreen.addEffectEnd((short)63, 0, super.x, super.y + 75, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)110, 0, super.x, super.y + 75, super.Dir, super.objMainEff);
         }

         if (super.f > super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 177:
         super.y += super.vy;
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 178:
         super.x += super.vx;
         super.y += super.vy;
         if (super.x > super.toX) {
            super.x = super.toX;
            if (super.y < super.toY + 20) {
               super.y += 5;
            }
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 179:
         if (super.f < 4) {
            super.objMainEff.frame = 33;
            var10000 = this.objTo.x;
            var10001 = super.objMainEff.x;
            super.objMainEff.type_left_right = super.Dir;
         }

         super.x += super.vx;
         super.y += super.vy;
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 180:
         if (super.f < super.fRemove) {
            (var2 = new Point()).x = super.x;
            var2.y = super.y;
            if (super.Dir == 0) {
               var2.vx = -6;
            } else {
               var2.vx = 6;
            }

            var2.frame = super.f;
            this.VecEff.addElement(var2);
         }

         if (super.f > super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
         }

         for(num39 = 0; num39 < this.VecEff.size(); ++num39) {
            (var3 = (Point)this.VecEff.elementAt(num39)).update();
            if (var3.f >= var3.fRe) {
               this.VecEff.removeElement(num39);
               --num39;
            }
         }

         return;
      case 181:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 182:
         for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
            (var2 = (Point)this.VecEff.elementAt(var1)).update();
            if (var2.f >= var2.fRe) {
               this.VecEff.removeElement(var1);
               --var1;
            }
         }

         if (super.f >= super.fRemove && this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
         break;
      case 183:
         if (this.typeSub > 0) {
            for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
               (var2 = (Point)this.VecEff.elementAt(var1)).update();
               ++var2.vy;
            }
         }

         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 184:
         if (super.f >= super.fRemove) {
            this.removeEff();
            return;
         }
         break;
      case 185:
         if (this.objTo != null) {
            super.x = this.objTo.x;
            super.y = this.objTo.y - this.objTo.hOne - 20;
         }

         if (super.f > super.fRemove) {
            this.removeEff();
            if (this.objTo != null) {
               if (this.typeSub != 3) {
                  GameScreen.addEffectEnd_ObjTo((short)40, 4, this.objTo.x, this.objTo.y - this.objTo.hOne - 20, (short)this.objTo.ID, (byte)this.objTo.typeObject, this.typeSub, super.objMainEff);
               } else {
                  GameScreen.addEffectEnd_ObjTo((short)175, 0, this.objTo.x, this.objTo.y - this.objTo.hOne - 20, (short)this.objTo.ID, (byte)this.objTo.typeObject, this.typeSub, super.objMainEff);
               }

               GameScreen.addEffectEnd((short)108, 8, this.objTo.x, this.objTo.y - this.objTo.hOne / 2, (byte)0, super.objMainEff);
               return;
            }

            if (this.typeSub != 3) {
               GameScreen.addEffectEnd((short)40, 4, super.x, super.y, super.Dir, super.objMainEff);
               return;
            }

            GameScreen.addEffectEnd((short)175, 0, super.x, super.y, super.Dir, super.objMainEff);
            return;
         }
      }

   }

   private void update_Lucci_L2() {
      super.x += super.vx;
      super.y += super.vy;
      if (super.f == 2 || super.f == 4 || super.f == 6) {
         super.x = super.x1000 - super.am_duong * 24;
      }

      if (super.f >= 7 && super.vx <= 20) {
         super.vx += super.am_duong << 1;
      }

      int var1;
      Point var2;
      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (super.frame == 0 && var2.f == 2) {
            var2.frame = 0;
         }

         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f < super.fRemove) {
         if (super.frame == 1) {
            for(var1 = 0; var1 <= super.mframe[super.f]; ++var1) {
               label79: {
                  (var2 = new Point()).x = super.x;
                  var2.y = super.y - super.mframe[super.f] * 10 + var1 * 20;
                  if (super.mframe[super.f] >= 2) {
                     if (super.mframe[super.f] == 2) {
                        if (var1 == 1) {
                           var2.fRe = 4;
                        } else {
                           var2.fRe = 2;
                        }
                        break label79;
                     }

                     if (super.mframe[super.f] == 3) {
                        if (var1 != 1 && var1 != 2) {
                           var2.fRe = 2;
                        } else {
                           var2.fRe = 4;
                        }
                        break label79;
                     }

                     if (var1 == 2) {
                        var2.fRe = 6;
                        break label79;
                     }

                     if (var1 == 1 || var1 == 3) {
                        var2.fRe = 4;
                        break label79;
                     }
                  }

                  var2.fRe = 2;
               }

               this.VecEff.addElement(var2);
            }
         } else if (super.mframe[super.f] == 2) {
            Point var3;
            (var3 = new Point()).x = super.x;
            var3.y = super.y;
            var3.frame = 1;
            var3.fRe = 4;
            this.VecEff.addElement(var3);
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateLuffy_6() {
      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         ++(var2 = (Point)this.VecEff.elementAt(var1)).f;
         if (var2.f >= 3) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f >= super.fRemove) {
         if (this.VecEff.size() == 0) {
            this.removeEff();
            return;
         }
      } else if ((this.typeSub == 2 || this.typeSub == 3) && super.f % 2 == 0) {
         Point var3 = new Point(super.x + CRes.random_Am_0(15), super.y + CRes.random_Am_0(20));
         this.VecEff.addElement(var3);
      }

   }

   private void updateSanji_5() {
      if (super.f == super.fRemove) {
         this.setAva(1, this.objTo);
         if (this.typeSub == 1) {
            GameScreen.addEffectEnd((short)25, 0, super.toX, super.toY, super.Dir, super.objMainEff);
         } else {
            GameScreen.addEffectEnd((short)8, 0, super.toX, super.toY, super.Dir, super.objMainEff);
         }

         GameScreen.addEffectEnd((short)93, 2, super.toX, super.toY, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove) {
         if (this.VecEff.size() == 0) {
            this.removeEff();
         }
      } else if (super.f > 0) {
         if (this.typeSub == 1) {
            Point var1 = new Point(super.x, super.y);
            this.VecEff.addElement(var1);
         }

         super.x += super.vx;
         super.y += super.vy;
      }

      int var3;
      if (super.f == 1) {
         if (this.objTo != null) {
            super.toY = this.objTo.y - this.objTo.hOne / 2;
         }

         var3 = super.toX - super.x;
         int var2 = super.toY - super.y;
         super.vMax = 14;
         this.create_Speed(var3, var2, (Point_Focus)null);
         super.fRemove += 2;
      }

      for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
         Point var4;
         ++(var4 = (Point)this.VecEff.elementAt(var3)).f;
         if (var4.f >= 4) {
            this.VecEff.removeElement(var4);
            --var3;
         }
      }

   }

   private void updateNami_4() {
      if (super.f < super.fRemove && super.f % 2 == 0) {
         Point var1;
         (var1 = new Point()).x = super.x + CRes.random_Am_0(20);
         var1.y = super.y + CRes.random_Am_0(20);
         if (super.typeEffect == 138) {
            var1.frame = CRes.random(2);
         }

         this.VecEff.addElement(var1);
      }

      if (super.f > super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

      for(int var3 = 0; var3 < this.VecEff.size(); ++var3) {
         Point var2;
         ++(var2 = (Point)this.VecEff.elementAt(var3)).f;
         if (var2.f >= 6) {
            this.VecEff.removeElement(var2);
            --var3;
         }
      }

   }

   private void updateLuffy_S1_Final() {
      if (super.f >= super.fRemove) {
         this.removeEff();
      }

      int var1;
      int var2;
      Point var3;
      if (super.f == 0) {
         var1 = 0;

         for(var2 = 0; var2 < 8; ++var2) {
            var3 = new Point(super.x + CRes.getcos(var1) * 45 / 1000, super.y + CRes.getsin(var1) * 35 / 1000);
            this.VecEff.addElement(var3);
            var1 += 45;
         }
      }

      var1 = 0;
      Point var4;
      int var5;
      int var6;
      if (super.f == 3) {
         var2 = 0;

         for(var6 = 0; var6 < 12; ++var6) {
            var4 = new Point(super.x + CRes.getcos(var2) * 75 / 1000, super.y + CRes.getsin(var2) * 45 / 1000);
            this.VecEff.addElement(var4);
            if ((this.typeSub == 11 || this.typeSub == 12) && ((var5 = GameCanvas.loadmap.AA(var4.x, var4.y)) == 0 || var5 == 2)) {
               this.vecSubEff.addElement(var4);
               GameScreen.addEffectEnd((short)110, 0, var4.x, var4.y, super.Dir, super.objMainEff);
            }

            var2 += 30;
         }
      }

      if (super.f == 6 && this.typeSub != 12) {
         var2 = 0;

         for(var6 = 0; var6 < 24; ++var6) {
            ++var1;
            var4 = new Point(super.x + CRes.getcos(var2) * 85 / 1000, super.y + CRes.getsin(var2) * 55 / 1000);
            this.VecEff.addElement(var4);
            if (this.typeSub == 1 && ((var5 = GameCanvas.loadmap.AA(var4.x, var4.y)) == 0 || var5 == 2)) {
               this.vecSubEff.addElement(var4);
               if (var1 % 2 == 0) {
                  GameScreen.addEffectEnd((short)110, 0, var4.x, var4.y, super.Dir, super.objMainEff);
               }
            }

            var2 += 15;
         }
      }

      if (super.f == 10 && this.typeSub == 11) {
         var2 = 0;

         for(var6 = 0; var6 < 32; ++var6) {
            ++var1;
            var4 = new Point(super.x + CRes.getcos(var2) * 115 / 1000, super.y + CRes.getsin(var2) * 80 / 1000);
            this.VecEff.addElement(var4);
            if (this.typeSub == 11 && ((var5 = GameCanvas.loadmap.AA(var4.x, var4.y)) == 0 || var5 == 2)) {
               this.vecSubEff.addElement(var4);
               if (var1 % 2 == 0) {
                  GameScreen.addEffectEnd((short)110, 0, var4.x, var4.y, super.Dir, super.objMainEff);
               }
            }

            var2 += 11;
         }
      }

      for(var2 = 0; var2 < this.VecEff.size(); ++var2) {
         ++(var3 = (Point)this.VecEff.elementAt(var2)).f;
         if (var3.f >= 3) {
            this.VecEff.removeElement(var3);
            --var2;
         }
      }

      var1 = 0;

      for(var2 = 0; var2 < this.vecSubEff.size(); ++var2) {
         ++(var3 = (Point)this.vecSubEff.elementAt(var2)).f;
         if (var3.f >= 8) {
            if (var1 % 2 == 0) {
               GameScreen.addEffectEnd((short)63, 0, var3.x, var3.y, super.Dir, super.objMainEff);
            }

            ++var1;
            this.vecSubEff.removeElement(var3);
            --var2;
         }
      }

   }

   private void updateLineIn() {
      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Line var2;
         (var2 = (Line)this.VecEff.elementAt(var1)).AA();
         if (super.f >= super.fRemove) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f >= super.fRemove) {
         if (GameCanvas.timeNow - this.time >= (long)this.timeRemove) {
            this.VecEff.removeAllElements();
            this.removeEff();
            return;
         }

         super.fRemove = CRes.random(4, 6);
         super.f = 0;
         this.create_Star_Line_In(super.vMax, this.xline, this.yline, 0, this.maxsize);
      }

   }

   private void updateLineBuff() {
      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Line var2;
         (var2 = (Line)this.VecEff.elementAt(var1)).AA();
         if (super.f >= super.fRemove) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f >= super.fRemove) {
         if (GameCanvas.timeNow - this.time >= (long)this.timeRemove) {
            this.VecEff.removeAllElements();
            this.removeEff();
            return;
         }

         super.fRemove = CRes.random(4, 6);
         super.f = 0;
         this.create_Star_Line_In(super.vMax, this.xline, this.yline, 10, this.maxsize);
      }

   }

   private void updateENDSanji2() {
      if (super.f == super.fRemove) {
         this.setAva(1, this.objTo);
         GameScreen.addEffectEnd((short)25, 0, super.toX, super.toY, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove) {
         if (this.VecEff.size() == 0) {
            this.removeEff();
         }
      } else if (super.f > 0) {
         Point var1 = new Point(super.x, super.y);
         this.VecEff.addElement(var1);
         super.x += super.vx;
         super.y += super.vy;
      }

      int var3;
      if (super.f == 1) {
         if (this.objTo != null) {
            super.toY = this.objTo.y - this.objTo.hOne / 2;
         }

         var3 = super.toX - super.x;
         int var2 = super.toY - super.y;
         super.vMax = 14;
         this.create_Speed(var3, var2, (Point_Focus)null);
         super.fRemove += 2;
      }

      for(var3 = 0; var3 < this.VecEff.size(); ++var3) {
         Point var4;
         ++(var4 = (Point)this.VecEff.elementAt(var3)).f;
         if (var4.f >= 4) {
            this.VecEff.removeElement(var4);
            --var3;
         }
      }

   }

   private void updateENDLuffy1() {
      if (super.f == super.fRemove && this.objTo != null) {
         this.setAva(1, this.objTo);
         if (this.typeSub == 5) {
            GameScreen.addEffectEnd((short)25, 4, super.toX, super.toY, super.Dir, super.objMainEff);
         } else {
            GameScreen.addEffectEnd((short)25, 0, super.toX, super.toY, super.Dir, super.objMainEff);
         }
      }

      if (super.f >= super.fRemove) {
         if (super.f == super.fRemove && this.objTo != null) {
            byte var1 = 0;
            if (this.typeSub == 5) {
               var1 = 3;
            }

            GameScreen.addEffectEnd((short)93, var1, super.x, super.y, super.Dir, super.objMainEff);
         }

         if (this.VecEff.size() == 0) {
            this.removeEff();
         }
      } else {
         if (this.typeSub > 0) {
            Point var3 = new Point(super.x, super.y);
            this.VecEff.addElement(var3);
         }

         super.x += super.vx;
         super.y += super.vy;
      }

      for(int var4 = 0; var4 < this.VecEff.size(); ++var4) {
         Point var2;
         ++(var2 = (Point)this.VecEff.elementAt(var4)).f;
         if (var2.f >= 3) {
            this.VecEff.removeElement(var2);
            --var4;
         }
      }

   }

   private void updateZoro9() {
      if (super.f == super.fRemove) {
         this.setAva(1, this.objTo);
         if (this.objTo != null) {
            GameScreen.addEffectEnd((short)19, super.f / 3 == 0 ? 0 : 1, this.objTo.x, this.objTo.y - this.objTo.hOne / 2, super.Dir, super.objMainEff);
            GameScreen.addEffectEnd((short)93, 0, this.objTo.x, this.objTo.y - this.objTo.hOne / 2, super.Dir, super.objMainEff);
            if (this.typeSub == 2) {
               GameScreen.addEffectEnd((short)108, 6, this.objTo.x, this.objTo.y - this.objTo.hOne / 2, super.Dir, super.objMainEff);
            }
         }
      }

      Point var2;
      if (super.f >= super.fRemove) {
         if (this.VecEff.size() == 0) {
            this.removeEff();
         }
      } else {
         if (this.typeSub == 2) {
            Point var1 = new Point(super.x, super.y - 16);
            this.VecEff.addElement(var1);
            var2 = new Point(super.x, super.y + 16);
            this.VecEff.addElement(var2);
         }

         super.x += super.vx;
         super.y += super.vy;
      }

      for(int var3 = 0; var3 < this.VecEff.size(); ++var3) {
         ++(var2 = (Point)this.VecEff.elementAt(var3)).f;
         if (var2.f >= 3) {
            this.VecEff.removeElement(var2);
            --var3;
         }
      }

   }

   private void updateFocusTouch() {
      if (this.objTo != null) {
         super.x = this.objTo.x;
         if (this.typeSub == 0) {
            super.y = this.objTo.y - this.objTo.hOne / 2;
         } else if (this.typeSub == 1) {
            super.y = this.objTo.y - this.objTo.hOne - 8;
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateXuyenGiap() {
      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         ++var2.vy;
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateRock() {
      if (super.f == 10) {
         super.levelPaint = -1;
      }

      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (var2.f < var2.fSmall) {
            ++var2.vy;
         } else if (var2.f == var2.fSmall) {
            var2.vx = 0;
            var2.vy = 0;
            int var3;
            if ((var3 = GameCanvas.loadmap.AA(var2.x, var2.y)) != 0 && var3 != 2) {
               var2.f = 100;
            }
         }

         if (var2.f >= 100 || var2.f >= var2.fRe) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f >= super.fRemove) {
         this.removeEff();
      }

   }

   private void updateRockNew() {
      if (super.f == 10) {
         super.levelPaint = -1;
      }

      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         (var2 = (Point)this.VecEff.elementAt(var1)).update();
         if (var2.subType == 1) {
            if (var2.f > var2.fRe - 2) {
               var2.frame = 2 - (var2.fRe - var2.f);
            }

            if (var2.frame > 2 || var2.frame < 0) {
               var2.frame = 2;
            }
         }

         if (var2.f < var2.fSmall) {
            ++var2.vy;
         } else if (var2.f == var2.fSmall) {
            var2.vx = 0;
            var2.vy = 0;
            int var3;
            if ((var3 = GameCanvas.loadmap.AA(var2.x, var2.y)) != 0 && var3 != 2) {
               var2.f = 100;
            } else if (super.typeEffect != 115 && CRes.random(2) == 0 && (var2.frame == 2 || var2.frame == 3)) {
               var2.subType = 1;
               var2.frame = 0;
            }
         }

         if (var2.f >= 100 || var2.f >= var2.fRe) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0 || super.f >= 80) {
         this.removeEff();
      }

   }

   private void updatePhanDamage() {
      int var1;
      Point var2;
      if (super.f <= super.fRemove) {
         for(var1 = 0; var1 < 3; ++var1) {
            (var2 = new Point(super.x + CRes.random_Am_0(4), super.y + CRes.random_Am_0(4))).f = CRes.random(3);
            this.VecEff.addElement(var2);
         }

         super.x += super.vx;
         super.y += super.vy;
      }

      if (super.f == super.fRemove) {
         GameScreen.addEffectEnd((short)1, 0, super.toX, super.toY, super.Dir, super.objMainEff);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var10000 = var2 = (Point)this.VecEff.elementAt(var1);
         var10000.y += 2;
         ++var2.f;
         if (var2.f >= 4) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

   }

   private void updateZoro4() {
      for(int var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var2;
         ++(var2 = (Point)this.VecEff.elementAt(var1)).f;
         if (var2.f >= var2.fRe) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

      if (super.f == 2) {
         Point var3;
         (var3 = new Point()).x = super.x;
         var3.y = super.y;
         var3.fRe = 6;
         var3.AY = new FrameImage(this.typeSub + 1, 55, 55);
         this.VecEff.addElement(var3);
      }

      if (super.f >= super.fRemove && this.VecEff.size() == 0) {
         this.removeEff();
      }

   }

   private void updateHutMP_HP() {
      int var1;
      Point var2;
      if (this.isUpdateNormal) {
         if (super.f % 2 == 0) {
            for(var1 = 0; var1 < 3; ++var1) {
               (var2 = new Point(super.x + CRes.random_Am_0(4), super.y + CRes.random_Am_0(4))).f = CRes.random(3);
               this.VecEff.addElement(var2);
            }
         }

         if (super.f <= 5) {
            super.x += super.vx;
            super.y += super.vy;
         }

         if (super.f == 5) {
            super.vx = 0;
            super.vy = 0;
         }

         if (super.f == 10) {
            super.fRemove = 60;
            super.vMax = 10000;
            super.numNextFrame = 2;
            super.gocT_Arc = 0;
            switch(super.Dir) {
            case 0:
               super.gocT_Arc = 180;
            case 1:
            default:
               break;
            case 2:
               super.gocT_Arc = 0;
            }

            super.va = 4096;
            super.vx = 0;
            super.vy = 0;
            super.life = 0;
            super.vX1000 = super.va * CRes.getcos(super.gocT_Arc) >> 10;
            super.vY1000 = super.va * CRes.getsin(super.gocT_Arc) >> 10;
         }

         if (super.f >= 10) {
            this.updateAngleNormal(this.objTo, 0);
         }
      } else if (this.VecEff.size() == 0) {
         this.removeEff();
         GameScreen.addEffectEnd((short)91, 0, super.x, super.y, super.Dir, super.objMainEff);
      }

      for(var1 = 0; var1 < this.VecEff.size(); ++var1) {
         Point var10000 = var2 = (Point)this.VecEff.elementAt(var1);
         var10000.y += 2;
         ++var2.f;
         if (var2.f >= 6) {
            this.VecEff.removeElement(var2);
            --var1;
         }
      }

   }

   public final void stopUpdateNormal() {
      if (super.typeEffect == 22) {
         this.isUpdateNormal = false;
      } else {
         this.removeEff();
      }
   }

   public final void removeEff() {
      super.isStop = true;
      super.f = -1;
   }

   private void createFocustouch() {
      super.x = this.objTo.x;
      if (this.typeSub == 0) {
         super.fraImgEff = new FrameImage(55, 32, 30);
         super.fRemove = 6;
         super.numNextFrame = 1;
         super.y = this.objTo.y - this.objTo.hOne / 2;
      } else {
         if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(56, 28, 15);
            super.fRemove = 8;
            super.numNextFrame = 2;
            super.y = this.objTo.y - this.objTo.hOne - 8;
         }

      }
   }

   private void createPhanDamage() {
      super.fraImgSub2Eff = new FrameImage(50, 48, 48, 32, 32);
      super.numNextFrame = 2;
      super.fraImgEff = new FrameImage(0, 14, 14);
      super.fraImgSubEff = new FrameImage(52, 5, 5);
      super.x1000 = super.x;
      super.y1000 = super.y;
   }

   private void createHut_MP_HP() {
      switch(this.typeSub) {
      case 0:
         super.fraImgEff = new FrameImage(51, 9, 9);
         super.fraImgSubEff = new FrameImage(52, 5, 5);
         break;
      case 1:
         super.fraImgEff = new FrameImage(53, 9, 9);
         super.fraImgSubEff = new FrameImage(54, 5, 5);
      }

      super.vx = CRes.random(2, 7);
      if (super.Dir == 0) {
         super.vx = -super.vx;
      }

      super.vy = -CRes.random(6, 10);
      super.fRemove = 30;
   }

   private void createXuyenGiap() {
      if (GameCanvas.isLowGraOrWP_PvP()) {
         this.removeEff();
      } else {
         super.numNextFrame = 2;
         super.fraImgEff = new FrameImage(48, 12, 11);
         super.fraImgSubEff = new FrameImage(30, 38, 38);
         super.fRemove = CRes.random(12, 16);
         int var1 = CRes.random(3, 7);

         for(int var2 = 0; var2 < var1; ++var2) {
            Point var3;
            (var3 = new Point()).x = super.x + CRes.random_Am_0(5);
            var3.y = super.y + CRes.random_Am_0(7);
            var3.frame = CRes.random(super.fraImgEff.nFrame);
            var3.dis = CRes.random(2);
            var3.vy = -CRes.random(6, 9);
            var3.vx = CRes.random(1, 4);
            if (var2 % 2 == 0) {
               var3.vx = -var3.vx;
            }

            this.VecEff.addElement(var3);
         }

      }
   }

   private void createEndBungmerang() {
      byte var1 = 0;
      if (this.typeSub == 1) {
         var1 = 50;
      }

      super.vx = CRes.random_Am(var1 + 150, 400 + (var1 << 1));
      super.vy = -CRes.random(var1 + 700, 1000 + (var1 << 1));
   }

   private void create_Star_Line_In(int vline, int minline, int maxline, int numpoint, int numLine) {
      if (super.f == -1) {
         this.VecEff.removeAllElements();
      }

      this.colorpaint = new int[numLine];
      if (maxline <= minline) {
         maxline = minline + 1;
      }

      int num2;
      for(num2 = 0; num2 < numLine; ++num2) {
         if (CRes.random(2) == 0) {
            this.colorpaint[num2] = colorStar[this.indexColorStar][CRes.random(3)];
         } else {
            this.colorpaint[num2] = colorStar[this.indexColorStar][2];
         }
      }

      for(num2 = 0; num2 < numLine; ++num2) {
         Line line = new Line();
         int num5 = 5 + 180 / numLine * num2;
         int num6;
         if ((num6 = 180 / numLine + 180 / numLine * num2 - 5) <= num5) {
            num6 = num5 + 1;
         }

         int num3 = CRes.random(minline, maxline);
         int num4 = CRes.random(vline, vline + 3);
         num5 = CRes.random(num5, num6);
         num6 = CRes.random(13, 23);
         if (numLine == 8) {
            num6 = CRes.random(50, 70);
         }

         boolean is2Line = CRes.random(4) == 0;
         num5 = CRes.fixangle(num5 % 360);
         line.setLine(super.x1000 - CRes.getsin(num5) * (num3 + num6), super.y1000 - CRes.getcos(num5) * (num3 + num6), super.x1000 - CRes.getsin(num5) * num6, super.y1000 - CRes.getcos(num5) * num6, CRes.getsin(num5) * num4, CRes.getcos(num5) * num4, is2Line);
         if (numpoint > 1) {
            line.type = CRes.random(numpoint);
         }

         this.VecEff.addElement(line);
         line = new Line();
         num5 = CRes.fixangle((num5 + 180 + CRes.random_Am(2, 5)) % 360);
         line.setLine(super.x1000 - CRes.getsin(num5) * (num3 + num6), super.y1000 - CRes.getcos(num5) * (num3 + num6), super.x1000 - CRes.getsin(num5) * num6, super.y1000 - CRes.getcos(num5) * num6, CRes.getsin(num5) * num4, CRes.getcos(num5) * num4, is2Line);
         if (numpoint > 1) {
            line.type = CRes.random(numpoint);
         }

         this.VecEff.addElement(line);
      }

   }

   private void createUrgot_4(int var1) {
      super.fraImgEff = new FrameImage(179, 54, 25);

      for(int var2 = 0; var2 < var1; ++var2) {
         Point var3;
         (var3 = new Point()).y = -CRes.random(30);
         var3.vy = CRes.random_Am(3, 8);
         var3.frame = 3 + CRes.random(3);
         this.VecEff.addElement(var3);
      }

      super.toX = super.x;
      super.toY = super.y;
      super.x = this.objTo.x;
      super.y = this.objTo.y;
   }

   private void createRock() {
      super.fRemove = CRes.random(24, 40);
      super.fraImgEff = new FrameImage(139, 10, 10);
      int var1 = CRes.random(4, 8);
      super.fRemove = CRes.random(34, 60);
      if (super.typeEffect == 107) {
         super.fraImgEff = new FrameImage(162, 10, 20);
         var1 = CRes.random(3, 5);
         super.fRemove = CRes.random(10, 16);
      } else if (super.typeEffect == 110) {
         super.fraImgEff = new FrameImage(227, 10, 10, 4);
         super.fraImgSubEff = new FrameImage(226, 13, 11, 3);
         super.fRemove = CRes.random(34, 60);
      } else if (super.typeEffect == 115) {
         var1 = CRes.random(10, 14);
         super.fraImgEff = new FrameImage(262, 20, 15);
         super.fraImgSubEff = new FrameImage(261, 61, 33);
         super.fRemove = CRes.random(104, 130);
      }

      for(int var2 = 0; var2 < var1; ++var2) {
         Point var3;
         (var3 = new Point()).x = super.x + CRes.random_Am_0(5);
         var3.y = super.y + CRes.random_Am_0(7);
         var3.frame = CRes.random(super.fraImgEff.nFrame);
         var3.fSmall = CRes.random(12, 16);
         var3.dis = CRes.random(2);
         var3.vy = CRes.random_Am(2, 5) - 6;
         var3.vx = CRes.random(5);
         var3.fRe = super.fRemove;
         if (super.typeEffect == 110) {
            var3.frame = CRes.random(4);
            var3.fRe = super.fRemove;
         } else if (super.typeEffect == 115) {
            var3.y -= 15;
            if (var2 == 0) {
               var3.frame = 3;
            } else {
               var3.frame = CRes.random(3);
            }

            var3.fSmall = CRes.random(8, 12);
            var3.fRe = var3.fSmall + CRes.random(70, 100);
            var3.vx = 1 + CRes.random(5);
            super.levelPaint = -1;
         } else if (super.typeEffect == 107) {
            var3.dis = CRes.random(2);
            var3.vy = CRes.random_Am(2, 5);
            var3.vx = CRes.random(2, 5);
         }

         if (var2 % 2 == 0) {
            var3.vx = -var3.vx;
         }

         this.VecEff.addElement(var3);
      }

   }

   private void createHachi2() {
      super.fRemove = CRes.random(10, 14);
      int var1 = CRes.random(5, 9);
      if (this.typeSub == 1) {
         var1 = CRes.random(3, 7);
         super.fRemove = CRes.random(6, 10);
         super.fraImgSubEff = new FrameImage(79, 25, 25);
         super.fraImgEff = new FrameImage(142, 10, 10);
      } else if (this.typeSub == 2) {
         super.fraImgSubEff = new FrameImage(87, 35, 35, 28, 28);
         super.fraImgEff = new FrameImage(214, 8, 8);
      } else {
         super.fraImgSubEff = new FrameImage(141, 50, 54, 36, 39);
         super.fraImgEff = new FrameImage(142, 10, 10);
      }

      for(int var2 = 0; var2 < var1; ++var2) {
         Point var3;
         (var3 = new Point()).x = super.x + CRes.random_Am_0(20);
         var3.y = super.y + CRes.random_Am_0(20);
         var3.frame = CRes.random(super.fraImgEff.nFrame);
         var3.dis = CRes.random(2);
         var3.vy = CRes.random_Am(1, 4);
         var3.vx = CRes.random(1, 4);
         if (var2 % 2 == 0) {
            var3.vx = -var3.vx;
         }

         this.VecEff.addElement(var3);
      }

   }

   private void createEND_Lu_S1_Final() {
      super.fraImgEff = new FrameImage(27, 24, 32);
      super.fRemove = 10;
      if (GameCanvas.isLowGraOrWP_PvP() && this.typeSub >= 4) {
         this.typeSub = 0;
      }

      if (this.typeSub != 1 && this.typeSub != 11 && this.typeSub != 12) {
         if (this.typeSub == 2) {
            super.fraImgEff = new FrameImage(225, 24, 32);
         } else if (this.typeSub == 3) {
            super.fraImgEff = new FrameImage(85, 34, 34, 28, 28);
         } else if (this.typeSub >= 4 && this.typeSub <= 7) {
            super.frame = this.typeSub - 4;
            super.fraImgEff = new FrameImage(273, 24, 24, 4);
         } else if (this.typeSub == 8) {
            super.fraImgEff = new FrameImage(273, 24, 24, 4);
         } else if (this.typeSub == 9) {
            super.frame = 2;
            super.fraImgEff = new FrameImage(273, 24, 24, 4);
         } else if (this.typeSub == 10) {
            super.fraImgEff = new FrameImage(99, 32, 32);
         } else if (this.typeSub == 13) {
            super.fraImgEff = new FrameImage(104, 3);
         }
      } else {
         super.fraImgEff = new FrameImage(99, 32, 32);
         super.fraImgSubEff = new FrameImage(138, 62, 64, 47, 48);
         super.fRemove = 14;
      }

      for(int var1 = 0; var1 < 3; ++var1) {
         Point var2 = new Point(super.x + CRes.random_Am_0(15), super.y + CRes.random_Am_0(15));
         this.VecEff.addElement(var2);
      }

   }

   private void createUssop11() {
      super.fraImgEff = new FrameImage(113, 25, 25);
      super.fraImgSubEff = new FrameImage(52, 5, 5);
      super.fRemove = 16;
      super.numNextFrame = 2;

      for(int var1 = 0; var1 < 10; ++var1) {
         Point var2;
         (var2 = new Point(super.x + CRes.random_Am_0(3), super.y + CRes.random_Am_0(3))).vx = CRes.random_Am_0(5);
         var2.vy = -5 + CRes.random_Am_0(5);
         var2.f = CRes.random(4);
         this.VecEff.addElement(var2);
      }

   }

   private void createUssop10() {
      super.fraImgEff = new FrameImage(112, 20, 14);
      super.vMax = 12;
      int var1 = super.toX - super.x;
      int var2 = super.toY - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
      var1 = CRes.AA(var1, var2);
      super.frame = this.setFrameAngle(var1);
   }

   private void createUssop9() {
      if (this.typeSub == 1) {
         super.fraImgEff = new FrameImage(107, 50, 54, 38, 41);
      } else {
         super.fraImgEff = new FrameImage(108, 38, 38, 32, 32);
      }

      super.fraImgSubEff = new FrameImage(110, 5, 5);
      super.fraImgSub2Eff = new FrameImage(109, 9, 9);
      super.fRemove = 30;
      super.numNextFrame = 2;

      for(int var1 = 0; var1 < 30; ++var1) {
         Point var2;
         (var2 = new Point(super.x + CRes.random_Am_0(3), super.y + CRes.random_Am_0(3))).vx = CRes.random_Am_0(5);
         var2.vy = -5 + CRes.random_Am_0(5);
         var2.f = CRes.random(4);
         if (super.frame == 1) {
            var2.frame = CRes.random(super.fraImgSubEff.nFrame);
         } else {
            var2.frame = CRes.random(2 + this.typeSub);
         }

         this.VecEff.addElement(var2);
      }

   }

   private void createUssopS3_Lv4() {
      if (this.typeSub == 1) {
         super.frame = 1;
      } else if (this.typeSub == 2) {
         super.frame = 0;
      } else {
         super.frame = CRes.random(2);
      }

      if (super.frame == 0) {
         super.fraImgEff = new FrameImage(108, 38, 38, 32, 32);
         super.fraImgSubEff = new FrameImage(110, 5, 5);
         super.fraImgSub2Eff = new FrameImage(109, 9, 9);
      } else {
         super.fraImgEff = new FrameImage(113, 25, 25);
         super.fraImgSubEff = new FrameImage(221, 5, 5, 4);
         super.fraImgSub2Eff = new FrameImage(220, 9, 9, 4);
      }

      super.fRemove = 30;
      super.numNextFrame = 2;

      for(int var1 = 0; var1 < 30; ++var1) {
         Point var2;
         (var2 = new Point(super.x + CRes.random_Am_0(3), super.y + CRes.random_Am_0(3))).vx = CRes.random_Am_0(6);
         var2.vy = -7 + CRes.random_Am_0(5);
         var2.f = CRes.random(4);
         if (super.frame == 1) {
            var2.fSmall = CRes.random(9);
         }

         var2.frame = CRes.random(3);
         this.VecEff.addElement(var2);
      }

   }

   private void createUssopS3_Lv6() {
      if (this.typeSub != 0) {
         super.frame = this.typeSub;
      } else {
         super.frame = CRes.random(3);
      }

      if (super.frame == 1) {
         super.fraImgEff = new FrameImage(19, 5);
         super.fraImgSubEff = new FrameImage(110, 5, 5);
         super.fraImgSub2Eff = new FrameImage(109, 9, 9);
      } else if (super.frame == 2) {
         super.fraImgEff = new FrameImage(50, 3);
         super.fraImgSubEff = new FrameImage(109, 9, 9);
         super.fraImgSub2Eff = new FrameImage(220, 9, 9, 4);
      } else {
         super.fraImgEff = new FrameImage(113, 25, 25);
         super.fraImgSubEff = new FrameImage(221, 5, 5, 4);
         super.fraImgSub2Eff = new FrameImage(51, 9, 9);
      }

      super.fRemove = 30;
      super.numNextFrame = 2;

      for(int var1 = 0; var1 < 30; ++var1) {
         Point var2;
         (var2 = new Point(super.x + CRes.random_Am_0(3), super.y + CRes.random_Am_0(3))).vx = CRes.random_Am_0(6);
         var2.vy = -7 + CRes.random_Am_0(5);
         var2.f = CRes.random(4);
         if (super.frame == 1) {
            var2.fSmall = CRes.random(9);
         }

         var2.frame = CRes.random(3);
         this.VecEff.addElement(var2);
      }

   }

   private void create_Ussop_S3_L7() {
      if (this.typeSub == 0) {
         super.fraImgEff = new FrameImage(450, 9);
         super.fRemove = 18;
         super.numNextFrame = 2;
      } else {
         super.fraImgEff = new FrameImage(456, 10);
         super.fRemove = 30;
         super.numNextFrame = 3;

         for(int var1 = 0; var1 < 10; ++var1) {
            Point var2;
            (var2 = new Point(super.x + CRes.random_Am_0(200), super.y + CRes.random_Am_0(50))).vx = CRes.random_Am_0(6);
            var2.vy = -7 + CRes.random_Am_0(5);
            var2.f = 0;
            this.VecEff.addElement(var2);
         }

      }
   }

   private void createEff_Firework() {
      super.frame = CRes.random(3);
      if (super.frame == 0) {
         super.fraImgEff = new FrameImage(108, 5);
      } else if (super.frame == 1) {
         super.fraImgEff = new FrameImage(50, 3);
      } else {
         super.fraImgEff = new FrameImage(113, 5);
      }

      super.fRemove = 10;
   }

   private void create_Sanji6() {
      super.fraImgEff = new FrameImage(78, 22, 28);
      int var1 = CRes.random_Am(1, 4);
      Point var2;
      (var2 = new Point(super.x, super.y)).vx = var1;
      var2.vy = -3;
      var2.fRe = CRes.random(7, 12);
      this.VecEff.addElement(var2);
      (var2 = new Point(super.x, super.y)).vx = -var1;
      var2.vy = 3;
      var2.fRe = CRes.random(7, 12);
      this.VecEff.addElement(var2);
      if (this.typeSub == 1) {
         (var2 = new Point(super.x, super.y)).vx = -var1;
         var2.vy = -3;
         var2.fRe = CRes.random(10, 14);
         this.VecEff.addElement(var2);
         (var2 = new Point(super.x, super.y)).vx = var1;
         var2.vy = 3;
         var2.fRe = CRes.random(10, 14);
         this.VecEff.addElement(var2);
      }

   }

   private void createEffectSkill1() {
      if (GameCanvas.isLowGraOrWP_PvP()) {
         this.removeEff();
      } else {
         if (this.typeSub == 3) {
            super.fraImgEff = new FrameImage(109, 4);
         } else if (this.typeSub == 2) {
            super.fraImgEff = new FrameImage(110, 5, 5);
         } else if (this.typeSub == 1) {
            super.fraImgEff = new FrameImage(52, 5, 5);
         } else {
            super.fraImgEff = new FrameImage(54, 5, 5);
         }

         int var1 = CRes.random(4, 7);

         for(int var2 = 0; var2 < var1; ++var2) {
            Point var3;
            (var3 = new Point(super.x + CRes.random_Am_0(5), super.y + CRes.random_Am_0(5))).vx = CRes.random_Am(1, 5);
            var3.vy = -7 + CRes.random(1, 4);
            var3.fRe = CRes.random(10, 14);
            var3.frame = CRes.random(super.fraImgEff.nFrame);
            this.VecEff.addElement(var3);
         }

      }
   }

   private void createCharTeleportNew() {
      super.fRemove = 15;
      super.fraImgEff = new FrameImage(66, 15, 55, 1);
      super.fraImgSubEff = new FrameImage(67, 3, 25, 1);
      super.y -= 150;
      super.vy = 30;
      Point var1;
      (var1 = new Point(super.x, super.y)).vy = super.vy;
      var1.dis = 1;
      var1.frame = 0;
      var1.fRe = 10;
      this.VecEff.addElement(var1);
   }

   private void createCharTeleport() {
      super.fRemove = 15;
      super.fraImgEff = new FrameImage(66, 15, 55, 1);
      super.fraImgSubEff = new FrameImage(67, 3, 25, 1);

      for(int var1 = 0; var1 < 10; ++var1) {
         Point var2;
         (var2 = new Point(super.x + CRes.random_Am_0(20), super.y - CRes.random(30) + 10)).vy = -2 - CRes.random(4);
         var2.dis = 0;
         var2.frame = CRes.random(super.fraImgSubEff.nFrame);
         var2.fRe = CRes.random(12, 20);
         this.VecEff.addElement(var2);
      }

   }

   private void createGet_Up() {
      super.fraImgEff = new FrameImage(199, 30, 16);
      super.fRemove = 10;

      for(int var1 = 0; var1 < 4; ++var1) {
         Point var2;
         (var2 = new Point()).x = super.x + CRes.random_Am_0(10);
         var2.y = super.y + CRes.random_Am_0(5);
         var2.vx = -2 + (var1 % 2 << 2) + CRes.random_Am_0(2);
         var2.vy = -1 + CRes.random_Am_0(3);
         var2.frame = CRes.random(2);
         var2.fRe = CRes.random(6, 10);
         this.VecEff.addElement(var2);
      }

   }

   private void createRevice() {
      super.timeBegin = GameCanvas.timeNow;
      super.fraImgEff = new FrameImage(179, 54, 25);
      super.fraImgSubEff = new FrameImage(121, 32, 32);
      super.timeEnd = 1000;

      for(int var1 = 0; var1 < 3; ++var1) {
         Point var2;
         (var2 = new Point()).y = -CRes.random(30);
         var2.vy = CRes.random_Am(3, 8);
         var2.frame = 3 + CRes.random(3);
         this.VecEff.addElement(var2);
      }

   }

   private void create_Upgrade_Thanh_Cong() {
      super.fraImgEff = new FrameImage(51, 9, 9);
      super.fraImgSubEff = new FrameImage(52, 5, 5);
      int num = CRes.random(25, 40);

      for(int i = 0; i < num; ++i) {
         Point point;
         (point = new Point(super.x + CRes.random_Am_0(6), super.y + CRes.random_Am_0(6))).fRe = CRes.random(20, 30);
         point.vx = CRes.random_Am(1, 6);
         point.vy = CRes.random_Am(1, 6);
         point.frame = CRes.random(3);
         this.VecEff.addElement(point);
      }

   }

   private void createPartical() {
      super.fRemove = CRes.random(12, 18);
      super.fraImgSubEff = new FrameImage(220, 9, 9, 4);
      super.fraImgEff = new FrameImage(221, 5, 5, 4);
      int var1 = CRes.random(8, 12);

      for(int var2 = 0; var2 < var1; ++var2) {
         Point var3;
         (var3 = new Point()).x = super.x + CRes.random_Am_0(3);
         var3.y = super.y + CRes.random_Am_0(5);
         var3.frame = CRes.random(4);
         var3.dis = CRes.random(3);
         var3.vy = CRes.random_Am(1, 4) - 5;
         var3.vx = CRes.random(6);
         if (var2 % 2 == 0) {
            var3.vx = -var3.vx;
         }

         this.VecEff.addElement(var3);
      }

   }

   private void createLittleDamBoss() {
      if (this.typeSub == 0) {
         super.x = MotherCanvas.w / 2 - 60;
      } else {
         super.x = MotherCanvas.w / 2 + 100;
      }

      super.y = 40;
      super.fRemove = CRes.random(16, 22);
      super.fraImgEff = new FrameImage(139, 10, 10);
      int var1 = CRes.random(5, 9);

      for(int var2 = 0; var2 < var1; ++var2) {
         Point var3;
         (var3 = new Point()).x = super.x + CRes.random_Am_0(5);
         var3.y = super.y + CRes.random_Am_0(7);
         var3.frame = CRes.random(super.fraImgEff.nFrame);
         var3.dis = CRes.random(2);
         var3.vy = -CRes.random(6, 9);
         var3.vx = CRes.random(1, 4);
         if (var2 % 2 == 0) {
            var3.vx = -var3.vx;
         }

         this.VecEff.addElement(var3);
      }

   }

   private void createLittleHpBoss() {
      if (this.typeSub == 0) {
         super.x = MotherCanvas.w / 2 - 60;
      } else {
         super.x = MotherCanvas.w / 2 + 100;
      }

      super.y = 40;
      super.fraImgEff = new FrameImage(204, 7, 7);
      super.fRemove = 12;

      for(int var1 = 0; var1 < 6; ++var1) {
         Point var2;
         (var2 = new Point(super.x + CRes.random_Am_0(30), super.y + CRes.random_Am_0(10))).fRe = CRes.random(8, 11);
         var2.vy = -CRes.random(5, 7);
         this.VecEff.addElement(var2);
      }

   }

   private void createMr3_1() {
      super.fraImgEff = new FrameImage(210, 63, 75, 44, 52);
      this.mPlayFrame = new int[]{0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 0, 0, 0};
      super.fRemove = this.mPlayFrame.length;
   }

   private void createKuromarimo() {
      super.fraImgEff = new FrameImage(207, 14, 14);
      super.fraImgSubEff = new FrameImage(78, 22, 28);
      super.fRemove = 30;
      if (this.objTo != null) {
         if (this.typeSub == 0) {
            super.x = this.objTo.x + 6;
         } else {
            super.x = this.objTo.x - 6;
         }

         super.y = this.objTo.y - this.objTo.hOne / 2 + 14;
      }

   }

   private void createChopper() {
      super.fraImgEff = new FrameImage(204, 7, 7);
      super.fRemove = 12;

      for(int var1 = 0; var1 < 4; ++var1) {
         Point var2;
         (var2 = new Point(super.x + CRes.random_Am_0(20), super.y + CRes.random_Am_0(10))).fRe = CRes.random(8, 11);
         var2.vy = -CRes.random(5, 7);
         this.VecEff.addElement(var2);
      }

   }

   private void createClassZoro() {
      super.vMax = 18;
      int var1 = this.objTo.x - super.x;
      int var2 = this.objTo.y - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
      super.fraImgEff = new FrameImage(10, 40, 47);
      super.fraImgSubEff = new FrameImage(183, 20, 54);
   }

   private void createClassUssop() {
      if (this.objTo != null) {
         super.y -= 25;
         super.vMax = 16;
         super.fraImgEff = new FrameImage(188, 9, 16);
         Point_Focus var1 = new Point_Focus();
         int var2 = this.objTo.x - super.x;
         int var3 = this.objTo.y - this.objTo.hOne / 2 - (super.y - this.objTo.dy);
         var1 = this.create_Speed(var2, var3, var1, super.x, super.y, this.objTo.x, this.objTo.y - this.objTo.hOne / 2);
         this.VecEff.addElement(var1);
      } else {
         super.BI = true;
      }
   }

   private void createClassNami() {
      super.fRemove = 8;
      if (super.typeEffect == 159) {
         super.fraImgEff = new FrameImage(387, 24, 240);
         super.fraImgSubEff = new FrameImage(26, 40, 20);
      } else {
         super.fraImgEff = new FrameImage(24, 15, 240);
         super.fraImgSubEff = new FrameImage(26, 40, 20);
      }

      GameScreen.addEffectEnd((short)59, 0, super.x, super.y, super.Dir, super.objMainEff);
   }

   private void create_US_S2_L5() {
      super.fraImgEff = new FrameImage(283, 22, 28);
      if (super.typeEffect == 140) {
         super.fraImgSubEff = new FrameImage(78, 22, 28);
      } else {
         super.fraImgSubEff = new FrameImage(78, 22, 28, 5);
      }

      super.x += CRes.random_Am_0(5);
      super.y += CRes.random_Am_0(5);
      Point var1;
      (var1 = new Point(super.x + CRes.random_Am_0(3), super.y + CRes.random_Am_0(3))).frame = 0;
      this.VecEff.addElement(var1);
      (var1 = new Point(super.x + CRes.random_Am_0(3), super.y + CRes.random_Am_0(3))).frame = 1;
      this.VecEff.addElement(var1);
      super.fRemove = CRes.random(8, 20);
   }

   private void createZoro_S3() {
      super.vx = -10;
      if (super.Dir == 2) {
         super.vx = 10;
      }

      if (this.typeSub == 1) {
         super.fraImgEff = new FrameImage(423, 32, 60);
      } else if (this.typeSub == 2) {
         super.fraImgEff = new FrameImage(444, 9);
      } else {
         super.fraImgEff = new FrameImage(322, 32, 60);
      }

      super.numNextFrame = 1;
      super.fRemove = 12;
      int var1;
      Point var2;
      int var3;
      if (this.typeSub == 0) {
         for(var1 = 0; var1 < 3; ++var1) {
            var2 = new Point(super.x, super.y - 15 + var1 * 15);
            if (super.Dir == 2) {
               var2.x -= var1 * 6;
            } else {
               var2.x += var1 * 6;
            }

            var2.fRe = 12;
            this.VecEff.addElement(var2);
            var3 = var2.x;
            if (super.Dir == 2) {
               var3 += 10;
            } else {
               var3 -= 10;
            }

            GameScreen.addEffectEnd_ToX_ToY((short)62, 0, var3, var2.y - 17, (int)(var3 + 12 * super.vx), (int)(var2.y - 17), super.Dir, super.objMainEff);
         }

      } else if (this.typeSub == 1) {
         for(var1 = 0; var1 < 4; ++var1) {
            var2 = new Point(super.x, super.y - 21 + var1 * 14);
            if (super.Dir == 2) {
               var2.x -= var1 * 6;
            } else {
               var2.x += var1 * 6;
            }

            var2.fRe = 12;
            this.VecEff.addElement(var2);
            var3 = var2.x;
            if (super.Dir == 2) {
               var3 += 10;
            } else {
               var3 -= 10;
            }

            GameScreen.addEffectEnd_ToX_ToY((short)62, 0, var3, var2.y - 17, (int)(var3 + 12 * super.vx), (int)(var2.y - 17), super.Dir, super.objMainEff);
         }

      } else {
         if (this.typeSub == 2) {
            for(var1 = 0; var1 < 5; ++var1) {
               var2 = new Point(super.x, super.y - 21 + var1 * 14);
               if (super.Dir == 2) {
                  var2.x -= var1 * 6;
               } else {
                  var2.x += var1 * 6;
               }

               var2.fRe = 12;
               this.VecEff.addElement(var2);
               var3 = var2.x;
               if (super.Dir == 2) {
                  var3 += 10;
               } else {
                  var3 -= 10;
               }

               GameScreen.addEffectEnd_ToX_ToY((short)62, 0, var3, var2.y - 17, (int)(var3 + 12 * super.vx), (int)(var2.y - 17), super.Dir, super.objMainEff);
            }
         }

      }
   }

   private void createLOL_Tru_Tren() {
      super.fraImgEff = new FrameImage(307, 4, 5, 6);
      byte var1 = 20;
      if (GameCanvas.isLowGraOrWP_PvP()) {
         var1 = 10;
      }

      this.maxsize = var1;

      for(int var2 = 0; var2 < var1; ++var2) {
         Point var3;
         (var3 = new Point()).x = CRes.random_Am_0(15);
         var3.y = 10 - CRes.random(40);
         var3.dis = 1 + CRes.random(3);
         var3.vy = -4;
         var3.frame = CRes.random(12);
         this.VecEff.addElement(var3);
      }

   }

   private void create_Poke_Ok() {
      super.fraImgEff = new FrameImage(302, 16);
      super.fraImgSubEff = new FrameImage(192, 25, 25);
      super.fraImgSub2Eff = new FrameImage(303, 49, 25);
      super.fRemove = 40;
      super.x *= 100;
      super.y *= 100;
      super.vx = CRes.random_Am(100, 300);
      super.vy = -CRes.random(600, 700);
      super.vMax = 50;
      super.frame = this.typeSub % 10;
   }

   private void create_Poke_Fail() {
      super.fraImgEff = new FrameImage(302, 16);
      super.fRemove = CRes.random(10, 16);
      super.x *= 100;
      super.y *= 100;
      super.vx = CRes.random_Am(200, 500);
      super.vy = -CRes.random(400, 600);
      super.vMax = 50;
      super.frame = this.typeSub % 10;
   }

   private void create_Poke_Begin() {
      super.fraImgEff = new FrameImage(302, 16);
      super.fRemove = 40;
      super.vMax = 8;
      super.y = super.objMainEff.y - super.objMainEff.hOne / 2 + 6;
      super.frame = this.typeSub % 10;
      super.objMainEff.frame = 33;
      if (this.objTo.x < super.objMainEff.x) {
         super.Dir = 0;
         super.objMainEff.Dir = 0;
         super.x -= 10;
      } else {
         super.Dir = 2;
         super.objMainEff.Dir = 2;
         super.x += 10;
      }

      int var1 = this.objTo.x - super.x;
      int var2 = this.objTo.y - 10 - super.y;
      this.create_Speed(var1, var2, (Point_Focus)null);
      if (super.fRemove <= 0) {
         super.fRemove = 1;
      }

      this.mposy = new int[super.fRemove];
      this.mposy[0] = 3;
      var1 = super.fRemove / 2;

      for(var2 = 1; var2 < this.mposy.length; ++var2) {
         if (var2 <= var1) {
            this.mposy[var2] = this.mposy[var2 - 1] + 3;
         } else {
            int var3;
            if ((var3 = this.mposy[var2 - 1] - 3) < 0) {
               var3 = 0;
            }

            this.mposy[var2] = var3;
         }
      }

   }

   private void createEndLuS1L4() {
      super.fraImgEff = new FrameImage(274, 22, 74, 3);
      super.numNextFrame = 1;
      super.fRemove = 7;
      Point var1;
      (var1 = new Point()).x = super.x;
      var1.y = super.y;
      if (super.Dir == 0) {
         var1.vx = -6;
      } else {
         var1.vx = 6;
      }

      if (this.typeSub == 1) {
         this.mPlayFrame = new int[]{3, 4, 4, 4, 3};
         super.fRemove = 5;
      } else if (this.typeSub == 2) {
         this.mPlayFrame = new int[]{3, 4, 4, 3};
         super.fRemove = 4;
      } else if (this.typeSub == 3) {
         this.mPlayFrame = new int[]{0, 4, 2, 6, 2, 4, 0};
         if (super.Dir == 0) {
            var1.vx = -8;
         } else {
            var1.vx = 8;
         }
      } else if (this.typeSub == 4) {
         this.mPlayFrame = new int[]{3, 10, 5, 11, 5, 10, 3};
         if (super.Dir == 0) {
            var1.vx = -4;
         } else {
            var1.vx = 4;
         }
      } else {
         this.mPlayFrame = new int[]{0, 1, 2, 2, 2, 1, 0};
      }

      var1.fRe = this.mPlayFrame.length;
      this.VecEff.addElement(var1);
   }

   private void create_End_Luffy_S1_L7() {
      super.fraImgEff = new FrameImage(447, 6);
      super.numNextFrame = 2;
      super.fRemove = 12;
      Point var1;
      (var1 = new Point()).x = super.x;
      var1.y = super.y;
      if (super.Dir == 0) {
         var1.vx = -6;
      } else {
         var1.vx = 6;
      }

      this.mPlayFrame = new int[]{0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
      var1.fRe = this.mPlayFrame.length;
      this.VecEff.addElement(var1);
   }

   private void create_Ice_Arc() {
      super.fRemove = 25;
      super.vMax = 5;

      for(int var1 = 0; var1 < 16; ++var1) {
         Point var2;
         (var2 = new Point()).x = super.x * 1000;
         var2.y = super.y * 1000;
         var2.vx = 2 * CRes.getcos(var1 * 225 / 10) * super.vMax;
         var2.vy = 1 * CRes.getsin(var1 * 225 / 10) * super.vMax;
         var2.f = 0;
         this.VecEff.addElement(var2);
      }

   }

   private void create_event_poke() {
      MainItem var1 = null;
      int var2 = Player.vecInventory.size();

      int var3;
      for(var3 = 0; var3 < var2 && (var1 = (MainItem)Player.vecInventory.elementAt(var3)).ID != this.idPotion; ++var3) {
      }

      if (var1 != null) {
         super.fraImgEff = new FrameImage(var1.AD(), this.idPotion, 1);
         super.fRemove = 40;
         super.vMax = 8;
         super.y = super.objMainEff.y - super.objMainEff.hOne / 2 + 6;
         super.frame = this.typeSub % 10;
         super.objMainEff.frame = 33;
         if (this.objTo.x < super.objMainEff.x) {
            super.Dir = 0;
            super.objMainEff.Dir = 0;
            super.x -= 10;
         } else {
            super.Dir = 2;
            super.objMainEff.Dir = 2;
            super.x += 10;
         }

         var3 = this.objTo.x - super.x;
         int var4 = this.objTo.y - 10 - super.y;
         this.create_Speed(var3, var4, (Point_Focus)null);
         if (super.fRemove <= 0) {
            super.fRemove = 1;
         }

         this.mposy = new int[super.fRemove];
         this.mposy[0] = 3;
         var4 = super.fRemove / 2;

         for(var2 = 1; var2 < this.mposy.length; ++var2) {
            if (var2 <= var4) {
               this.mposy[var2] = this.mposy[var2 - 1] + 3;
            } else {
               if ((var3 = this.mposy[var2 - 1] - 3) < 0) {
                  var3 = 0;
               }

               this.mposy[var2] = var3;
            }
         }

      }
   }
}
