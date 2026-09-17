public final class Class_CX {
   private static FrameImage AA;
   private static FrameImage AB;
   private static FrameImage AC;
   private static FrameImage AD;
   private static FrameImage AE;
   private static FrameImage AF;
   private static FrameImage AG;
   private static FrameImage AH;
   private static FrameImage AI;
   private static FrameImage AJ;
   private static FrameImage AK;
   private static FrameImage AL;
   private static FrameImage AM;

   public static void AA(mGraphics var0, int var1, int var2, int var3, int var4) {
      FrameImage var5;
      if ((var5 = AA(var4)) == null) {
         AB(var4);
      } else {
         int var6;
         byte var7 = AA((long)(var6 = CRes.abs(var1)));
         byte var8 = 8;
         int var9 = 0;
         if (var4 != 16 && var4 != 25) {
            if (var4 == 23) {
               var8 = 15;
            }
         } else {
            var9 += 16;
            var8 = 12;
         }

         int var10 = 10;
         var9 += (var7 - 1) * (var8 / 2);
         if (var1 < 0) {
            var9 += 5;
         }

         for(int var11 = 0; var11 < var7; ++var11) {
            byte var12 = (byte)(var6 % var10 / (var10 / 10));
            var10 *= 10;
            var5.drawFrameNew(var12, var2 + var9, var3, 0, 3, var0);
            var9 -= var8;
            if (var4 == 23 && var11 % 3 == 2) {
               var9 -= 2;
            }
         }

         if (var1 < 0) {
            var5.drawFrameNew(11, var2 + var9 - 2, var3, 0, 3, var0);
         }

         if (var4 == 16) {
            var9 -= 10;
            if (AD == null) {
               AB(var4);
            } else {
               AD.drawFrame(GameCanvas.gameTick / 3 % AD.nFrame, var2 + var9, var3, 0, 3, var0);
            }
         } else {
            if (var4 == 25) {
               var9 -= 28;
               if (AM == null) {
                  AB(var4);
                  return;
               }

               AM.drawFrame(GameCanvas.gameTick / 4 % AM.nFrame, var2 + var9, var3, 0, 3, var0);
            }

         }
      }
   }

   public static void AA(mGraphics var0, int var1, int var2, int var3) {
      FrameImage var4;
      if ((var4 = AA(18)) == null) {
         AB(18);
      } else {
         long var5;
         byte var14 = AA(var5 = (long)var1 * 100L);
         long var9 = 10L;
         int var7 = 0 + (var14 - 1 << 2) + (var14 - 1) / 3 * 3;

         for(int var8 = 0; var8 < var14; ++var8) {
            int var11 = (int)(var5 % var9 / (var9 / 10L));
            var9 *= 10L;
            var4.drawFrameNew(var11, var2 + var7, var3, 0, 3, var0);
            if (var8 % 3 == 2 && var8 < var14 - 1) {
               var7 -= 3;
               var4.drawFrameNew(10, var2 + var7, var3, 0, 3, var0);
            }

            var7 -= 8;
         }

      }
   }

   public static void AB(mGraphics var0, int var1, int var2, int var3) {
      switch(var3) {
      case 17:
         if (AE == null) {
            AB(var3);
            return;
         } else {
            AE.drawFrameNew(0, var1, var2, 0, 3, var0);
         }
      default:
      }
   }

   private static FrameImage AA(int var0) {
      if (var0 == 13) {
         return AA;
      } else if (var0 == 14) {
         return AB;
      } else if (var0 == 15) {
         return AC;
      } else if (var0 == 19) {
         return AH;
      } else if (var0 == 20) {
         return AI;
      } else if (var0 == 22) {
         return AK;
      } else if (var0 == 21) {
         return AJ;
      } else if (var0 == 16) {
         return AF;
      } else if (var0 == 23) {
         return AL;
      } else if (var0 == 18) {
         return AG;
      } else {
         return var0 == 25 ? AI : null;
      }
   }

   private static byte AA(long var0) {
      if (var0 < 10L) {
         return 1;
      } else if (var0 < 100L) {
         return 2;
      } else if (var0 < 1000L) {
         return 3;
      } else if (var0 < 10000L) {
         return 4;
      } else if (var0 < 100000L) {
         return 5;
      } else if (var0 < 1000000L) {
         return 6;
      } else if (var0 < 10000000L) {
         return 7;
      } else if (var0 < 100000000L) {
         return 8;
      } else if (var0 < 1000000000L) {
         return 9;
      } else {
         return (byte)(var0 < 10000000000L ? 10 : 11);
      }
   }

   private static void AB(int var0) {
      if (var0 == 13) {
         AA = new FrameImage(237, 12, 12, 5);
      } else if (var0 == 14) {
         AB = new FrameImage(235, 12, 12, 5);
      } else if (var0 == 15) {
         AC = new FrameImage(236, 12, 12, 5);
      } else if (var0 == 17) {
         AE = new FrameImage(232, 47, 14);
      } else if (var0 == 19) {
         AH = new FrameImage(234, 12, 12, 5);
      } else if (var0 == 20) {
         AI = new FrameImage(228, 12, 12, 5);
      } else if (var0 == 22) {
         AK = new FrameImage(229, 12, 12, 5);
      } else if (var0 == 21) {
         AJ = new FrameImage(230, 12, 12, 5);
      } else if (var0 != 16 && var0 != 23) {
         if (var0 == 18) {
            AG = new FrameImage(mImage.createImage("/interface/numwanted.png"), 7, 11, 5);
         } else {
            if (var0 == 25) {
               AI = new FrameImage(228, 12, 12, 5);
               AM = new FrameImage(399, 61, 16);
            }

         }
      } else {
         AL = new FrameImage(390, 22, 22, 5);
         AF = new FrameImage(231, 16, 16, 5);
         AD = new FrameImage(233, 32, 14);
      }
   }

   public static void AC(mGraphics var0, int var1, int var2, int var3) {
      FrameImage var12;
      if ((var12 = AA(18)) == null) {
         AB(18);
      } else {
         long var5;
         byte var11 = AA(var5 = (long)var1);
         long var9 = 10L;
         int var4 = 0 + (var11 - 1 << 2) + (var11 - 1) / 3 * 3;

         for(int var7 = 0; var7 < var11; ++var7) {
            int var8 = (int)(var5 % var9 / (var9 / 10L));
            var9 *= 10L;
            var12.drawFrameNew(var8, var2 + var4, 128, 0, 3, var0);
            if (var7 % 3 == 2 && var7 < var11 - 1) {
               var4 -= 3;
               var12.drawFrameNew(10, var2 + var4, 128, 0, 3, var0);
            }

            var4 -= 8;
         }

      }
   }
}
