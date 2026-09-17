import java.util.Calendar;
import java.util.Hashtable;
import java.util.Random;
import java.util.TimeZone;

public final class CRes {
   private static short[] sin = new short[]{0, 18, 36, 54, 71, 89, 107, 125, 143, 160, 178, 195, 213, 230, 248, 265, 282, 299, 316, 333, 350, 367, 384, 400, 416, 433, 449, 465, 481, 496, 512, 527, 543, 558, 573, 587, 602, 616, 630, 644, 658, 672, 685, 698, 711, 724, 737, 749, 761, 773, 784, 796, 807, 818, 828, 839, 849, 859, 868, 878, 887, 896, 904, 912, 920, 928, 935, 943, 949, 956, 962, 968, 974, 979, 984, 989, 994, 998, 1002, 1005, 1008, 1011, 1014, 1016, 1018, 1020, 1022, 1023, 1023, 1024, 1024};
   private static short[] cos;
   private static int[] tan;
   private static Random rs;

   static {
      new Hashtable();
      rs = new Random();
   }

   public static void loadSinCos() {
      cos = new short[91];
      tan = new int[91];

      for(int var0 = 0; var0 <= 90; ++var0) {
         cos[var0] = sin[90 - var0];
         if (cos[var0] == 0) {
            tan[var0] = 1000000000;
         } else {
            tan[var0] = (sin[var0] << 10) / cos[var0];
         }
      }

   }

   public static int getsin(int var0) {
      if ((var0 = fixangle(var0)) >= 0 && var0 < 90) {
         return sin[var0];
      } else if (var0 >= 90 && var0 < 180) {
         return sin[180 - var0];
      } else {
         return var0 >= 180 && var0 < 270 ? -sin[var0 - 180] : -sin[360 - var0];
      }
   }

   public static int getcos(int var0) {
      if ((var0 = fixangle(var0)) >= 0 && var0 < 90) {
         return cos[var0];
      } else if (var0 >= 90 && var0 < 180) {
         return -cos[180 - var0];
      } else {
         return var0 >= 180 && var0 < 270 ? -cos[var0 - 180] : cos[360 - var0];
      }
   }

   public static int AA(int var0, int var1) {
      int var10000;
      int var2;
      if (var0 != 0) {
         var2 = abs((var1 << 10) / var0);

         label44: {
            for(int var3 = 0; var3 <= 90; ++var3) {
               if (tan[var3] >= var2) {
                  var10000 = var3;
                  break label44;
               }
            }

            var10000 = 0;
         }

         var2 = var10000;
         if (var1 >= 0 && var0 < 0) {
            var2 = 180 - var2;
         }

         if (var1 < 0 && var0 < 0) {
            var2 += 180;
         }

         if (var1 >= 0 || var0 < 0) {
            return var2;
         }

         var10000 = 360 - var2;
      } else {
         var10000 = var1 > 0 ? 90 : 270;
      }

      var2 = var10000;
      return var2;
   }

   public static int fixangle(int var0) {
      if (var0 >= 360) {
         var0 %= 360;
      }

      if (var0 < 0) {
         var0 = 360 + var0 % 360;
      }

      return var0;
   }

   public static int abs(int var0) {
      return var0 < 0 ? -var0 : var0;
   }

   public static int random(int var0) {
      return var0 <= 1 ? 0 : rs.nextInt(var0);
   }

   public static int random_Am_0(int var0) {
      if (var0 <= 1) {
         var0 = 1;
      }

      int var1 = 0;

      while(var1 == 0) {
         var1 = rs.nextInt(var0);
         if (CRes.random(2) == 0) {
            var1 = -var1;
         }
      }

      return var1;
   }

   public static int random_Am(int var0, int var1) {
      if (var1 <= var0) {
         var1 = var0 + 1;
      }

      var0 += rs.nextInt(var1 - var0);
      if (CRes.random(2) == 0) {
         var0 = -var0;
      }

      return var0;
   }

   public static int random(int var0, int var1) {
      if (var1 <= var0) {
         var1 = var0 + 1;
      }

      return var0 + rs.nextInt(var1 - var0);
   }

   public static int AG(int var0) {
      if (var0 <= 0) {
         return 0;
      } else {
         int var1 = (var0 + 1) / 2;

         int var2;
         do {
            var2 = var1;
            var1 = var1 / 2 + var0 / (var1 * 2);
         } while(abs(var2 - var1) > 1);

         return var1;
      }
   }

   public static int AA(int var0, int var1, int var2, int var3) {
      return abs(var0 - var2) + abs(var1 - var3);
   }

   public static void saveRMS(String var0, byte[] var1) {
      try {
         GameMidlet.AA(var0, var1);
      } catch (Exception var2) {
      }
   }

   public static byte[] loadRMS(String var0) {
      return GameMidlet.AA(var0);
   }

   public static void AA(mVector var0) {
      AA(var0, 0, var0.size() - 1);
   }

   private static void AA(mVector var0, int var1, int var2) {
      if (var2 - var1 > 0) {
         try {
            int var3 = ((Class_DS)var0.elementAt(var2)).AN;
            var3 = AA(var0, var1, var2, var3);
            AA(var0, var1, var3 - 1);
            AA(var0, var3 + 1, var2);
         } catch (Exception var4) {
            (new StringBuffer("recQuickSortMemList Error message: ")).append(var4.getMessage()).toString();
            var4.printStackTrace();
         }
      }
   }

   private static int AA(mVector var0, int var1, int var2, int var3) {
      --var1;
      int var4 = var2;

      try {
         while(true) {
            do {
               ++var1;
            } while(((Class_DS)var0.elementAt(var1)).AN < var3);

            while(var4 > 0) {
               --var4;
               if (((Class_DS)var0.elementAt(var4)).AN <= var3) {
                  break;
               }
            }

            if (var1 >= var4) {
               AB(var0, var1, var2);
               break;
            }

            AB(var0, var1, var4);
         }
      } catch (Exception var5) {
         (new StringBuffer("partitionItMemList Error message: ")).append(var5.getMessage()).toString();
         var5.printStackTrace();
      }

      return var1;
   }

   private static void AB(mVector var0, int var1, int var2) {
      Object var3 = var0.elementAt(var2);
      if (((Class_DS)var0.elementAt(var2)).AN != ((Class_DS)var0.elementAt(var1)).AN) {
         var0.setElementAt(var0.elementAt(var1), var2);
         var0.setElementAt(var3, var1);
      }

   }

   public static void quickSort(mVector var0) {
      AC(var0, 0, var0.size() - 1);
   }

   private static void AC(mVector var0, int var1, int var2) {
      if (var2 - var1 > 0) {
         try {
            int var3 = ((MainObject)var0.elementAt(var2)).ySort;
            var3 = AB(var0, var1, var2, var3);
            AC(var0, var1, var3 - 1);
            AC(var0, var3 + 1, var2);
         } catch (Exception var4) {
            (new StringBuffer("recQuickSort Error message: ")).append(var4.getMessage()).toString();
            var4.printStackTrace();
         }
      }
   }

   private static int AB(mVector var0, int var1, int var2, int var3) {
      --var1;
      int var4 = var2;

      try {
         while(true) {
            do {
               ++var1;
            } while(((MainObject)var0.elementAt(var1)).ySort < var3);

            while(var4 > 0) {
               --var4;
               if (((MainObject)var0.elementAt(var4)).ySort <= var3) {
                  break;
               }
            }

            if (var1 >= var4) {
               AD(var0, var1, var2);
               break;
            }

            AD(var0, var1, var4);
         }
      } catch (Exception var5) {
         (new StringBuffer("partitionIt Error message: ")).append(var5.getMessage()).toString();
         var5.printStackTrace();
      }

      return var1;
   }

   private static void AD(mVector var0, int var1, int var2) {
      Object var3 = var0.elementAt(var2);
      if (((MainObject)var0.elementAt(var2)).ySort != ((MainObject)var0.elementAt(var1)).ySort) {
         var0.setElementAt(var0.elementAt(var1), var2);
         var0.setElementAt(var3, var1);
      }

   }

   public static boolean AB(String var0) {
      return var0.length() >= 9 && var0.substring(0, 9).compareTo("SUB_image") == 0;
   }

   public static String AH(int var0) {
      if (var0 >= 1440) {
         return var0 / 1440 + " " + T.FQ;
      } else {
         return var0 > 60 ? var0 / 60 + " " + T.FR : var0 + " " + T.FS;
      }
   }

   public static boolean AA(Boat var0, Boat var1) {
      int var10000 = var0.AV;
      int var7 = var1.AY;
      int var6 = var1.AX;
      int var5 = var1.AW;
      int var4 = var1.AV;
      int var3 = var0.AY;
      int var2 = var0.AX;
      int var9 = var0.AW;
      int var8 = var10000;
      return var10000 >= var4 && var8 <= var4 + var6 && var9 > var5 && var9 < var5 + var7 || var4 >= var8 && var4 <= var8 + var2 && var5 >= var9 && var5 <= var9 + var3 || var8 >= var4 && var8 <= var4 + var6 && var5 >= var9 && var5 <= var9 + var3 || var4 >= var8 && var4 <= var8 + var2 && var9 > var5 && var9 < var5 + var7;
   }

   public static boolean AC(String var0) {
      try {
         Integer.parseInt(var0);
         return true;
      } catch (Exception var1) {
         return false;
      }
   }
   
   public static Calendar getTime() {
      return Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
   }
}
