import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.TextBox;

public final class TField extends AvMain {
   public int AA;
   public int AB;
   public int AC;
   public int AD;
   private boolean AL;
   private boolean AM = false;
   public boolean AE = true;
   private static int AN = 1;
   private static int[] AO = new int[]{18, 14, 11, 9, 6, 4, 2};
   private static String[] AP = new String[]{" 0", ".,@?!_1\"/$-():*+<=>;%&~#%^&*{}[];'/1", "abc2âă", "def3đê", "ghi4", "jkl5", "mno6ôơ", "pqrs7", "tuv8ư", "wxyz9", "*", "#"};
   private static String[] AQ = new String[]{"0", "1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9", "0", "0"};
   private static String[] AR = new String[]{" 0", "er1", "ty2", "ui3", "df4", "gh5", "jk6", "cv7", "bn8", "m9", "0", "0", "qw!", "as?", "zx", "op.", "l,"};
   private String AS = "";
   private String AT = "";
   private String AU = "";
   private int AV = 0;
   private int AW = 0;
   private int AX = 500;
   private int AY = 0;
   private static int AZ = -1984;
   private int BA = 0;
   private int BB = 0;
   private int BC = 10;
   private int BD = 0;
   public static boolean AF;
   private static int BE = 0;
   private static int BF;
   private static int BG;
   public iCommand AG;
   private String BH = "";
   public String AH = "";
   private String BI = "";
   private boolean BJ = false;
   private int BK = 0;
   public boolean AI;
   public static boolean AJ;
   private int BL = -1;
   private int BM = 0;
   private int BN = 0;
   private long BO = 0L;
   private static String BP;
   private int BQ = 0;
   private static int[][] BR;
   TextBox AK = null;

   static {
      String[] var10000 = new String[]{"abc", "Abc", "ABC", "123"};
      BF = 11;
      AJ = false;
      BP = "aáàảãạâấầẩẫậăắằẳẵặeéèẻẽẹêếềểễệiíìỉĩịoóòỏõọôốồổỗộơớờởỡợuúùủũụưứừửữựyýỳỷỹỵ";
      BR = new int[][]{{32, 48}, {49, 69}, {50, 84}, {51, 85}, {52, 68}, {53, 71}, {54, 74}, {55, 67}, {56, 66}, {57, 77}, {42, 128}, {35, 137}, {33, 113}, {63, 97}, {64, 121, 122}, {46, 111}, {44, 108}};
   }

   public final void commandPointer(int var1, int var2) {
      switch(var1) {
      case 0:
         if (this.AL) {
            this.AK();
         }
      default:
      }
   }

   public static void setVendorTypeMode(int var0) {
      AP[0] = " 0";
      AP[10] = "*";
      AP[11] = "#";
      BF = 35;
      BG = 42;
   }

   public final void AA(boolean var1) {
      if (this.AL != var1) {
         BE = 0;
      }

      AZ = -1984;
      MotherCanvas.AC();
      this.AL = var1;
   }

   public final iCommand AA() {
      return this.AG;
   }

   public static int AB() {
      return GameCanvas.isTouch ? 28 : 20;
   }

   private void AI() {
      mFont.tahoma_7_black.getHeight();
      this.AG = new iCommand(T.del, 0, this);
   }

   public TField() {
      this.AS = "";
      this.AI();
      this.AA(false);
      this.AJ();
   }

   public TField(int var1, int var2, int var3) {
      this.AS = "";
      this.AA = var1;
      this.AB = var2;
      this.AC = var3;
      this.AI();
      this.AA(false);
      this.AJ();
   }

   private void AJ() {
      this.AD = 20;
      if (GameCanvas.isTouch) {
         this.AD = 28;
      }

   }

   public final void AA(String var1) {
      this.BI = var1;
   }

   private void AK() {
      if (this.AV > 0 && this.AS.length() > 0) {
         this.AS = this.AS.substring(0, this.AV - 1) + this.AS.substring(this.AV, this.AS.length());
         --this.AV;
         this.AH(0);
         this.AM();
      }

   }

   private void AH(int var1) {
      if (this.BD == 2) {
         this.AU = this.AT;
      } else {
         this.AU = this.AS;
      }

      int var2 = mFont.tahoma_7_black.getWidth(this.AU.substring(0, this.AV));
      if (var1 == -1) {
         if (var2 + this.AY < 15 && this.AV > 0 && this.AV < this.AU.length()) {
            this.AY += mFont.tahoma_7_black.getWidth(this.AU.substring(this.AV, this.AV + 1));
         }
      } else if (var1 == 1) {
         if (var2 + this.AY > this.AC - 25 && this.AV < this.AU.length() && this.AV > 0) {
            this.AY -= mFont.tahoma_7_black.getWidth(this.AU.substring(this.AV - 1, this.AV));
         }
      } else {
         this.AY = -(var2 - (this.AC - 12));
      }

      if (this.AY > 0) {
         this.AY = 0;
      } else {
         if (this.AY < 0) {
            var1 = mFont.tahoma_7_black.getWidth(this.AU) - (this.AC - 12);
            if (this.AY < -var1) {
               this.AY = -var1;
            }
         }

      }
   }

   private void AI(int var1) {
      if (this.BD != 2 && this.BD != 3 || var1 >= 48 && var1 <= 57 || var1 >= 65 && var1 <= 90 || var1 >= 97 && var1 <= 122) {
         if (this.AS.length() < this.AX) {
            String var2 = this.AS.substring(0, this.AV) + (char)var1;
            if (this.AV < this.AS.length()) {
               var2 = var2 + this.AS.substring(this.AV, this.AS.length());
            }

            this.AS = var2;
            ++this.AV;
            this.AM();
            this.AH(0);
         }

      }
   }

   public final boolean AD(int var1) {
      if (var1 != 8 && var1 != -8 && var1 != 204) {
         if (GameCanvas.isKeyPressed() && var1 >= 65 && var1 <= 122) {
            AF = true;
            byte[] var2 = new byte[]{1};

            try {
               CRes.saveRMS("Main_isQty", var2);
            } catch (Exception var5) {
               var5.printStackTrace();
            }
         }

         if (AF) {
            if (var1 == 45) {
               if (var1 == AZ && this.BA < AO[AN]) {
                  this.AS = this.AS.substring(0, this.AV - 1) + '_';
                  this.AU = this.AS;
                  this.AM();
                  this.AH(0);
                  AZ = -1984;
                  return false;
               }

               AZ = 45;
            }

            if (var1 >= 32) {
               this.AI(var1);
               return false;
            }
         }

         if (!AF && var1 == BF) {
            if (++BE > 3) {
               BE = 0;
            }

            AZ = BF;
            MotherCanvas.AC();
            this.BA = 1;
            AZ = var1;
            return false;
         } else if (var1 == BG && this.BD == 0) {
            this.AL();
            return false;
         } else {
            if (var1 == 42) {
               var1 = 58;
            }

            if (var1 == 35) {
               var1 = 59;
            }

            if (var1 >= 48 && var1 <= 59) {
               if (this.BD != 0 && this.BD != 2 && this.BD != 3) {
                  if (this.BD == 1) {
                     this.AI(var1);
                     this.BA = 1;
                  }
               } else {
                  String[] var3;
                  TField var7;
                  if ((var7 = this).BD != 2 && var7.BD != 3) {
                     var3 = AP;
                  } else {
                     var3 = AQ;
                  }

                  char var4;
                  String var8;
                  if (var1 == AZ) {
                     var7.BB = (var7.BB + 1) % var3[var1 - 48].length();
                     var4 = var3[var1 - 48].charAt(var7.BB);
                     if (BE == 0) {
                        var4 = Character.toLowerCase(var4);
                     } else if (BE == 1) {
                        var4 = Character.toUpperCase(var4);
                     } else if (BE == 2) {
                        var4 = Character.toUpperCase(var4);
                     } else {
                        var4 = var3[var1 - 48].charAt(var3[var1 - 48].length() - 1);
                     }

                     var8 = var7.AS.substring(0, var7.AV - 1) + var4;
                     if (var7.AV < var7.AS.length()) {
                        var8 = var8 + var7.AS.substring(var7.AV, var7.AS.length());
                     }

                     var7.AS = var8;
                     var7.BA = AO[AN];
                     var7.AM();
                  } else if (var7.AS.length() < var7.AX) {
                     if (BE == 1 && AZ != -1984) {
                        BE = 0;
                     }

                     var7.BB = 0;
                     var4 = var3[var1 - 48].charAt(var7.BB);
                     if (BE == 0) {
                        var4 = Character.toLowerCase(var4);
                     } else if (BE == 1) {
                        var4 = Character.toUpperCase(var4);
                     } else if (BE == 2) {
                        var4 = Character.toUpperCase(var4);
                     } else {
                        var4 = var3[var1 - 48].charAt(var3[var1 - 48].length() - 1);
                     }

                     var8 = var7.AS.substring(0, var7.AV) + var4;
                     if (var7.AV < var7.AS.length()) {
                        var8 = var8 + var7.AS.substring(var7.AV, var7.AS.length());
                     }

                     var7.AS = var8;
                     var7.BA = AO[AN];
                     ++var7.AV;
                     var7.AM();
                     var7.AH(0);
                  }

                  AZ = var1;
               }
            } else {
               this.BB = 0;
               AZ = -1984;
               if (var1 == 14) {
                  if (this.AV > 0) {
                     --this.AV;
                     this.AH(0);
                     this.BC = 10;
                     return false;
                  }
               } else if (var1 == 15) {
                  if (this.AV < this.AS.length()) {
                     ++this.AV;
                     this.AH(0);
                     this.BC = 10;
                     return false;
                  }
               } else {
                  if (var1 == 19) {
                     this.AK();
                     return false;
                  }

                  AZ = var1;
               }
            }

            return true;
         }
      } else {
         this.AK();
         return true;
      }
   }

   private void AL() {
      this.BO = System.currentTimeMillis() / 100L;
      if (this.BL != -1) {
         ++this.BN;
         if (this.BN >= 6) {
            this.BN = 0;
         }

         String var5 = this.AS.substring(0, this.BL);
         String var6 = this.AS.substring(this.BL + 1);
         String var7 = BP.substring(this.BM + this.BN, this.BM + this.BN + 1);
         this.AS = var5 + var7 + var6;
      } else {
         for(int var1 = this.AV; var1 > 0; --var1) {
            char var2 = this.AS.charAt(var1 - 1);

            for(int var3 = 0; var3 < BP.length(); ++var3) {
               char var4 = BP.charAt(var3);
               if (var2 == var4) {
                  this.BM = var3;
                  this.BN = 0;
                  this.BL = var1 - 1;
                  return;
               }
            }
         }

         this.BL = -1;
      }
   }

   public final void paint(mGraphics var1) {
      boolean var2 = this.AL;
      mFont var3 = mFont.tahoma_7_black;
      byte var4 = 0;
      if (this.BD == 2) {
         this.AU = this.AT;
         var4 = 3;
      } else {
         this.AU = this.AS;
      }

      int var5 = 0;
      var1.setColor(-4155296);
      ++this.BQ;
      if (var2 && this.AU.length() > 0 && this.AV > 0) {
         var5 = mFont.tahoma_7_black.getWidth(this.AU.substring(0, this.AV));
      }

      byte var6 = 0;
      if (!GameCanvas.isTouch && !var2) {
         var6 = 1;
      }

      AvMain.paintRect(var1, this.AA, this.AB, this.AC, this.AD + 1, (byte)0, var6);
      var1.AD(this.AA + 2, this.AB + 2, this.AC - 4, this.AD - 3);
      int var8 = var1.getTranslateX();
      int var7 = var1.getTranslateY();
      var1.translate(-this.BK, 0);
      if (this.AU.length() == 0) {
         var4 = 0;
         this.AU = this.BI;
         var3 = mFont.tahoma_7_white;
      }

      var3.drawString(var1, this.AU, this.AA + 4, this.AB + this.AD / 2 - 5 + var4, 0);
      if (var2 && this.BQ % 16 > 12 && !GameCanvas.AR()) {
         var1.setColor(0);
         var1.fillRect(this.AA + 3 + var5, this.AB + this.AD / 2 - 7, 1, 14);
      }

      var1.AD(0, 0, MotherCanvas.w, MotherCanvas.h);
      GameCanvas.resetTrans(var1);
      var1.translate(var8, var7);
   }

   public final void AA(mGraphics var1, boolean var2) {
      boolean var3 = this.AL;
      mFont var4 = mFont.tahoma_7_black;
      byte var5 = 0;
      if (this.BD == 2) {
         this.AU = this.AT;
         var5 = 3;
      } else {
         this.AU = this.AS;
      }

      int var6 = 0;
      var1.setColor(-4155296);
      ++this.BQ;
      if (var3 && this.AU.length() > 0 && this.AV > 0) {
         var6 = mFont.tahoma_7_black.getWidth(this.AU.substring(0, this.AV));
      }

      byte var7 = 0;
      if (!GameCanvas.isTouch && !var3) {
         var7 = 1;
      }

      AvMain.paintRect(var1, this.AA, this.AB, this.AC, this.AD + 1, (byte)0, var7);
      if (var2) {
         var1.AD(this.AA + 2, this.AB + 2, this.AC - 4, this.AD - 3);
      }

      int var8 = var1.getTranslateX();
      int var9 = var1.getTranslateY();
      var1.translate(-this.BK, 0);
      if (this.AU.length() == 0) {
         var5 = 0;
         this.AU = this.BI;
         var4 = mFont.tahoma_7_white;
      }

      var4.drawString(var1, this.AU, this.AA + 4, this.AB + this.AD / 2 - 5 + var5, 0);
      if (var3 && this.BQ % 16 > 12 && this.BL == -1) {
         var1.setColor(0);
         var1.fillRect(this.AA + 3 + var6, this.AB + this.AD / 2 - 7, 1, 14);
      }

      var1.AD(0, 0, MotherCanvas.w, MotherCanvas.h);
      GameCanvas.resetTrans(var1);
      var1.translate(var8, var9);
   }

   public final void AB(mGraphics var1) {
      boolean var2 = this.AL;
      mFont var3 = mFont.tahoma_7_black;
      byte var4 = 0;
      if (this.BD == 2) {
         this.AU = this.AT;
         var4 = 3;
      } else {
         this.AU = this.AS;
      }

      int var5 = 0;
      var1.setColor(-4155296);
      ++this.BQ;
      if (var2 && this.AU.length() > 0 && this.AV > 0) {
         var5 = mFont.tahoma_7_black.getWidth(this.AU.substring(0, this.AV));
      }

      byte var6 = 0;
      if (!GameCanvas.isTouch && !var2) {
         var6 = 1;
      }

      AvMain.paintRect(var1, this.AA, this.AB, this.AC, this.AD + 1, (byte)0, var6);
      var1.translate(-this.BK, 0);
      if (this.AU.length() == 0) {
         var4 = 0;
         this.AU = this.BI;
         var3 = mFont.tahoma_7_white;
      }

      var3.drawString(var1, this.AU, this.AA + 4, this.AB + this.AD / 2 - 5 + var4, 0);
      if (var2 && this.BQ % 16 > 12 && this.BL == -1) {
         var1.setColor(0);
         var1.fillRect(this.AA + 3 + var5, this.AB + this.AD / 2 - 7, 1, 14);
      }

   }

   public final boolean AF() {
      return this.AL;
   }

   private void AM() {
      if (this.BD == 2) {
         this.AT = "";

         for(int var1 = 0; var1 < this.AS.length(); ++var1) {
            this.AT = this.AT + "*";
         }

         if (this.BA > 0 && this.AV > 0) {
            this.AT = this.AT.substring(0, this.AV - 1) + this.AS.charAt(this.AV - 1) + this.AT.substring(this.AV, this.AT.length());
         }
      }

   }

   public final void updatePointer() {
      this.AJ(0);
   }

   public final void AE(int var1) {
      this.AJ(var1);
   }

   public final void update() {
      label66: {
         if (this.AL) {
            String var1;
            if (this.BD == 2) {
               var1 = this.AT;
            } else {
               var1 = this.AS;
            }

            this.BK = -this.AC / 2 + this.AV * 5 + 4;
            int var2 = mFont.tahoma_7_black.getWidth(var1) - this.AC + 8;
            if (this.BK > var2) {
               this.BK = var2;
            }

            if (this.BK >= 0) {
               break label66;
            }
         }

         this.BK = 0;
      }

      ++this.AW;
      if (this.BA > 0) {
         --this.BA;
         if (this.BA == 0 || BE > 2) {
            this.BB = 0;
            if (this.AL && BE == 1 && AZ != BF) {
               BE = 0;
            }

            AZ = -1984;
            this.AM();
         }
      }

      if (this.BC > 0) {
         --this.BC;
      }

      if (this.BL != -1 && System.currentTimeMillis() / 100L - this.BO > 5L) {
         this.BL = -1;
      }

      if (this.AL) {
         if (GameCanvas.AK[4]) {
            --this.AV;
            if (this.AV < 0) {
               this.AV = 0;
            }

            this.AH(-1);
            GameCanvas.AK[4] = false;
            return;
         }

         if (GameCanvas.AK[6]) {
            ++this.AV;
            if (this.AV > this.AS.length()) {
               this.AV = this.AS.length();
            }

            this.AH(1);
            GameCanvas.AK[6] = false;
         }
      }

   }

   private void AJ(int var1) {
      if (GameCanvas.isPointerSelect && GameCanvas.isPoint(0, 0, MotherCanvas.w, MotherCanvas.h - GameCanvas.hCommand / 2)) {
         if (GameCanvas.isPoint(this.AA, this.AB - var1 - 6, this.AC, this.AD + 12)) {
            if (!this.AL) {
               this.AL = true;
               if (!AJ) {
                  AJ = true;
                  GameCanvas.isPointerSelect = false;
                  this.AN();
                  return;
               }
            } else if (!AJ) {
               AJ = true;
               GameCanvas.isPointerSelect = false;
               this.AN();
               return;
            }
         } else if (this.AE) {
            this.AL = false;
         }
      }

   }

   public final void AG() {
      this.AL = true;
      AJ = true;
      GameCanvas.isPointerSelect = false;
      if (GameCanvas.isTouch) {
         this.AN();
      }

   }

   public final String getText() {
      return this.AS;
   }

   public final void AB(String var1) {
      if (var1 != null) {
         AZ = -1984;
         this.BA = 0;
         this.BB = 0;
         this.AS = var1;
         this.AU = var1;
         this.AM();
         this.AV = var1.length();
         this.AH(0);
      }
   }

   public final void AF(int var1) {
      this.AX = var1;
   }

   public final void AG(int var1) {
      this.BD = var1;
   }

   private void AN() {
      this.AK = new TextBox(this.AH, this.BH, 200, 0);
      this.AK.addCommand(new Command("OK", 4, 0));
      this.AK.addCommand(new Command("Cancel", 3, 0));
      this.AK.setCommandListener(new Class_EE(this));

      try {
         if (this.BD == 2) {
            this.AK.setConstraints(65536);
         } else if (this.BD == 1) {
            this.AK.setConstraints(2);
         } else {
            this.AK.setConstraints(0);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.AK.setString(this.AS);
      this.AK.setMaxSize(this.AX);
      Display.getDisplay(GameMidlet.AD).setCurrent(this.AK);
   }

   static void AA(TField var0, boolean var1) {
      var0.AL = false;
   }
}
