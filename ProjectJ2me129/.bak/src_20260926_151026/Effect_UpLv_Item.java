public final class Effect_UpLv_Item {
   private int wnew = 1;
   private int[][] colorBorder = new int[][]{{-1, -1, -1052689, -1052689, -4079167, -4079167, -6250336}, {-14092490, -14092490, -14295246, -14295246, -14563796, -14563796, -14247637}, {-4947201, -4947201, -5801491, -5801491, -6656038, -6656038, -8562000}, {-197061, -197061, -1512905, -1512905, -2828750, -2828750, -5592279}, {-654281, -654281, -2030541, -2030541, -3472338, -3472338, -5504217}, {-14944764, -14944764, -654281, -654281, -1, -1, -1052689}, {-14944764, -14944764, -654281, -14092490, -1, -1, -1052689}, {-14944764, -14944764, -654281, -14092490, -4947201, -1, -1052689}};
   private int[] size = new int[]{2, 1, 1, 1, 1, 1};
   private int[] size15 = new int[]{2, 2, 1, 1, 1, 1, 1, 1};
   private int valueTest = 0;
   private int lv = 0;

   public final void paintUpgradeEffect(int x, int y, int upgrade, int w, mGraphics g, int lech, boolean isBorder) {
      if (upgrade > 0) {
         this.lv = upgrade <= 2 ? 0 : (upgrade <= 5 ? 1 : (upgrade <= 8 ? 2 : (upgrade <= 9 ? 3 : (upgrade <= 10 ? 4 : (upgrade <= 11 ? 1 : (upgrade <= 12 ? 2 : (upgrade <= 13 ? 3 : (upgrade <= 14 ? 4 : 5))))))));
         int num0 = this.lv;
         if (upgrade >= 11) {
            g.setColor(this.colorBorder[GameCanvas.gameTick / 6 % 5][6]);
            g.drawRect(x - w / 2 - lech, y - w / 2 - lech, w, w);
         } else {
            g.setColor(this.colorBorder[num0][6]);
            g.drawRect(x - w / 2 - lech, y - w / 2 - lech, w, w);
         }

         int[] array;
         if (upgrade >= 11) {
            array = this.size15;
         } else {
            array = this.size;
         }

         int num1;
         int num2;
         int num3;
         int num4;
         int num5;
         byte num6;
         for(num1 = 0; num1 < array.length; ++num1) {
            num3 = GameCanvas.gameTick - num1 * this.wnew;
            if ((num2 = (num3 = (num3 + this.valueTest) % (w * 4)) >= 0 && num3 < w ? num3 % w : (w <= num3 && num3 < w << 1 ? w : -1)) != -1) {
               num3 = x - w / 2 + num2;
               num4 = y - w / 2 + this.upgradeEffectY(GameCanvas.gameTick - num1 * this.wnew, w);
               num5 = num1;
               if (num1 > 5) {
                  num5 = 5;
               }

               g.setColor(this.colorBorder[num0][num5]);
               num5 = array[num1];
               num6 = 0;
               if (num2 <= w && num5 == 2) {
                  num6 = 1;
               }

               g.fillRect(num3 - num5 / 2 - lech, num4 - num5 / 2 - lech + num6, num5, num5);
            }
         }

         for(num1 = 0; num1 < array.length; ++num1) {
            num3 = GameCanvas.gameTick + w - num1 * this.wnew;
            num3 = (num3 + this.valueTest) % (w * 4);
            int num8;
            if (w <= num3 && num3 < w << 1) {
               num8 = 0;
            } else if (w << 1 <= num3 && num3 < w * 3) {
               num8 = w - (w - num3 % w);
            } else {
               if (this.valueTest == 0) {
                  this.valueTest = w << 1;
               } else {
                  this.valueTest = 0;
               }

               num8 = -1;
            }

            num2 = num8;
            if (num8 != -1) {
               num3 = x - w / 2 + num2;
               num4 = y - w / 2 + this.upgradeEffectY(GameCanvas.gameTick + w - num1 * this.wnew, w);
               num5 = num1;
               if (num1 > 5) {
                  num5 = 5;
               }

               g.setColor(this.colorBorder[num0][num5]);
               num5 = array[num1];
               num6 = 0;
               if (num2 == 0 && num5 == 2) {
                  num6 = 1;
               }

               g.fillRect(num3 - num5 / 2 - lech + num6, num4 - num5 / 2 - lech, num5, num5);
            }
         }

      }
   }

   private int upgradeEffectY(int tick, int w) {
      if ((tick = (tick + this.valueTest) % (w * 4)) >= 0 && tick < w) {
         return 0;
      } else if (w <= tick && tick < w << 1) {
         return tick % w;
      } else {
         return w << 1 <= tick && tick < w * 3 ? w : w - tick % w;
      }
   }
}
