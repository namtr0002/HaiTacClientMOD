import java.io.DataInputStream;
import java.io.IOException;

public final class mFont {
   private int AR;
   private int fontHeight;
   private mImage imgFont;
   private String strFont;
   private int[][] fImages;
   private static String AW = " 0123456789+-*='_?.,<>/[]{}!@#$%^&*():aáàảãạâấầẩẫậăắằẳẵặbcdđeéèẻẽẹêếềểễệfghiíìỉĩịjklmnoóòỏõọôốồổỗộơớờởỡợpqrstuúùủũụưứừửữựvxyýỳỷỹỵzwAÁÀẢÃẠĂẰẮẲẴẶÂẤẦẨẪẬBCDĐEÉÈẺẼẸÊẾỀỂỄỆFGHIÍÌỈĨỊJKLMNOÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢPQRSTUÚÙỦŨỤƯỨỪỬỮỰVXYÝỲỶỸỴZW";
   public static mFont tahoma_7b_orange;
   public static mFont tahoma_7b_blue;
   public static mFont tahoma_7b_black;
   public static mFont tahoma_7b_yellow;
   public static mFont tahoma_7b_violet;
   public static mFont tahoma_7b_white;
   public static mFont tahoma_7b_green;
   public static mFont tahoma_7b_red;
   public static mFont tahoma_7b_brown;
   public static mFont tahoma_7_black;
   public static mFont tahoma_7_white;
   public static mFont tahoma_7_yellow;
   public static mFont tahoma_7_orange;
   public static mFont tahoma_7_red;
   public static mFont tahoma_7_blue;
   public static mFont tahoma_7_green;
   public static mFont tahoma_7_violet;
   private String AX;

   public static void loadmFont() {
      tahoma_7b_orange = new mFont(AW, "/mfont/tahoma_7b_orange.png", "/mfont/tahoma_7b", 0);
      tahoma_7b_blue = new mFont(AW, "/mfont/tahoma_7b_blue.png", "/mfont/tahoma_7b", 0);
      tahoma_7b_black = new mFont(AW, "/mfont/tahoma_7b_black.png", "/mfont/tahoma_7b", 0);
      tahoma_7b_yellow = new mFont(AW, "/mfont/tahoma_7b_yellow.png", "/mfont/tahoma_7b", 0);
      tahoma_7b_violet = new mFont(AW, "/mfont/tahoma_7b_violet.png", "/mfont/tahoma_7b", 0);
      tahoma_7b_white = new mFont(AW, "/mfont/tahoma_7b_white.png", "/mfont/tahoma_7b", 0);
      tahoma_7b_green = new mFont(AW, "/mfont/tahoma_7b_green.png", "/mfont/tahoma_7b", 0);
      tahoma_7b_brown = new mFont(AW, "/mfont/tahoma_7b_brown.png", "/mfont/tahoma_7b", 0);
      tahoma_7b_red = new mFont(AW, "/mfont/tahoma_7b_red.png", "/mfont/tahoma_7b", 0);
      tahoma_7_black = new mFont(AW, "/mfont/tahoma_7_black.png", "/mfont/tahoma_7", 0);
      tahoma_7_white = new mFont(AW, "/mfont/tahoma_7_white.png", "/mfont/tahoma_7", 0);
      tahoma_7_yellow = new mFont(AW, "/mfont/tahoma_7_yellow.png", "/mfont/tahoma_7", 0);
      tahoma_7_orange = new mFont(AW, "/mfont/tahoma_7_orange.png", "/mfont/tahoma_7", 0);
      tahoma_7_red = new mFont(AW, "/mfont/tahoma_7_red.png", "/mfont/tahoma_7", 0);
      tahoma_7_blue = new mFont(AW, "/mfont/tahoma_7_blue.png", "/mfont/tahoma_7", 0);
      tahoma_7_green = new mFont(AW, "/mfont/tahoma_7_green.png", "/mfont/tahoma_7", 0);
      tahoma_7_violet = new mFont(AW, "/mfont/tahoma_7_violet.png", "/mfont/tahoma_7", 0);
   }

   private mFont(String strFontp, String var2, String var3, int var4) {
      try {
         this.strFont = strFontp;
         this.AR = 0;
         this.AX = var2;
         DataInputStream dis = null;
         this.imgFont = mImage.createImage(this.AX);

         try {
             dis = GameMidlet.AD(var3);
            this.fImages = new int[dis.readShort()][];

            int height;
            for(height = 0; height < this.fImages.length; ++height) {
               this.fImages[height] = new int[4];
               this.fImages[height][0] = dis.readShort();
               this.fImages[height][1] = dis.readShort();
               this.fImages[height][2] = dis.readShort();
               this.fImages[height][3] = dis.readShort();
            }

            height = this.fImages[this.fImages.length - 1][3];
            this.fontHeight = height;
         } catch (Exception exp) {
            try {
               dis.close();
            } catch (IOException var5) {
               var5.printStackTrace();
            }
         }
      } catch (Exception exp2) {
         exp2.printStackTrace();
         (new StringBuffer("paht data:")).append(var2).toString();
      }
   }

   public final int getHeight() {
      return this.fontHeight;
   }

   public final int getWidth(String var1) {
      int var3 = 0;

      for(int var4 = 0; var4 < var1.length(); ++var4) {
         int var2;
         if ((var2 = this.strFont.indexOf(var1.charAt(var4))) == -1) {
            var2 = 0;
         }

         var3 += this.fImages[var2][2] + this.AR;
      }

      return var3;
   }

   public final void drawString(mGraphics var1, String var2, int var3, int var4, int var5) {
      int var6 = var2.length();
      if (var5 == 0) {
         var5 = var3;
      } else if (var5 == 1) {
         var5 = var3 - this.getWidth(var2);
      } else {
         var5 = var3 - (this.getWidth(var2) >> 1);
      }

      for(int var7 = 0; var7 < var6; ++var7) {
         if ((var3 = this.strFont.indexOf(var2.charAt(var7))) == -1) {
            var3 = 0;
         }

         if (var3 >= 0) {
            var1.drawRegion(this.imgFont, this.fImages[var3][0], this.fImages[var3][1], this.fImages[var3][2], this.fImages[var3][3], 0, var5, var4, 20);
         }

         var5 += this.fImages[var3][2] + this.AR;
      }

   }

   public final void drawString(mGraphics var1, String var2, int var3, int var4, int var5, boolean var6) {
      int var8 = var2.length();
      var5 = var3;

      for(int var7 = 0; var7 < var8; ++var7) {
         if ((var3 = this.strFont.indexOf(var2.charAt(var7))) == -1) {
            var3 = 0;
         }

         if (var3 >= 0) {
            var1.setClip(this.imgFont, this.fImages[var3][0], this.fImages[var3][1], this.fImages[var3][2], this.fImages[var3][3], 0, var5, var4, 20, true);
         }

         var5 += this.fImages[var3][2] + this.AR;
      }

   }
   
   public void drawStringInBox(mGraphics g, String text, int x, int y, int w, int align) {
        if (text == null) return;
        String[] words = text.split(" ");
        String line = "";
        int lineHeight = this.getHeight();
        int yy = y;

        for (int i = 0; i < words.length; i++) {
            String test = line.isEmpty() ? words[i] : (line + " " + words[i]);
            if (this.getWidth(test) > w) {
                // vẽ dòng trước đó
                this.drawString(g, line, x, yy, align, false);
                yy += lineHeight;
                line = words[i];
            } else {
                line = test;
            }
        }
        if (!line.isEmpty()) {
            this.drawString(g, line, x, yy, align, false);
        }
    }
   public static String[] split(String var0, String var1) {
      mVector var2 = new mVector();

      for(int var3 = var0.indexOf(var1); var3 >= 0; var3 = (var0 = var0.substring(var3 + var1.length())).indexOf(var1)) {
         var2.addElement(var0.substring(0, var3));
      }

      var2.addElement(var0);
      String[] var4 = new String[var2.size()];
      if (var2.size() > 0) {
         for(int var5 = 0; var5 < var2.size(); ++var5) {
            var4[var5] = (String)var2.elementAt(var5);
         }
      }

      return var4;
   }

   public final String[] splitFontArray(String var1, int var2) {
      int num = var2;
      String strspl = var1;
      mFont mFontspl = this;
      mVector mVecspl = new mVector();
      String strspl2 = "";

      for(int var6 = 0; var6 < strspl.length(); ++var6) {
         if (strspl.charAt(var6) == '\n') {
            mVecspl.addElement(strspl2);
            strspl2 = "";
         } else {
            strspl2 = strspl2 + strspl.charAt(var6);
            if (mFontspl.getWidth(strspl2) > num) {
               int var7;
               for(var7 = strspl2.length() - 1; var7 >= 0 && strspl2.charAt(var7) != ' '; --var7) {
               }

               if (var7 < 0) {
                  var7 = strspl2.length() - 1;
               }

               mVecspl.addElement(strspl2.substring(0, var7));
               var6 = var6 - (strspl2.length() - var7) + 1;
               strspl2 = "";
            }

            if (var6 == strspl.length() - 1 && !strspl2.trim().equals("")) {
               mVecspl.addElement(strspl2);
            }
         }
      }

      mVector var9 = mVecspl;
      String[] var11 = new String[mVecspl.size()];

      for(num = 0; num < var9.size(); ++num) {
         var11[num] = var9.elementAt(num).toString();
      }

      return var11;
   }
   // thêm helper (đặt vào cùng class MsgSellItems hoặc AThMadaraMOD nếu bạn thích)
    public static mFont getFontByColor(int colorName) {
        switch (colorName) {
            case 0: return mFont.tahoma_7b_black;   // trắng (hoặc màu hiển thị mặc định)
            case 1: return mFont.tahoma_7b_brown;   // xanh nhạt / brown (điều chỉnh theo project)
            case 2: return mFont.tahoma_7b_green;
            case 3: return mFont.tahoma_7_red;
            case 4: return mFont.tahoma_7b_yellow;
            case 5: return mFont.tahoma_7b_violet;
            default: return mFont.tahoma_7b_black;
        }
    }

}
