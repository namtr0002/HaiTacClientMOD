import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public final class UpdateImageScreen extends MainScreen {
   public static int AA;
   public static int AB;
   private int AD = -1;
   private int AE = 122;
   private int AF;
   private int AG;
   private long AH = 0L;
   public static byte AC = 0;
   private static String AI = "";
   private mImage AJ;
   private mImage AK;
   private mImage AL;
   private mImage AM;
   private mImage AN;
   private mImage AO;
   private int AP;
   private int AQ;
   private int AR;
   private boolean AS = false;

   public UpdateImageScreen() {
      this.AF = MotherCanvas.hw;
      this.AG = (MotherCanvas.h / 5 << 2) - 7;
      if (GameCanvas.isIos()) {
         this.AS = true;
      } else {
         this.AS = false;
         Session_ME.getInstance().close();
         GameCanvas.connectDownload();
         GlobalService.getInstance().Request_Image_Android();
      }

      this.AH = System.currentTimeMillis();
      AC = 0;
      AI = T.BP;
      if (GameCanvas.language == 1) {
         this.AJ = mImage.createImage("/new/lgv_e.png");
      } else {
         this.AJ = mImage.createImage("/new/lgv.png");
      }

      this.AM = mImage.createImage("/new/koload.png");
      this.AN = mImage.createImage("/new/load.png");
      this.AO = mImage.createImage("/new/thuyen.png");
      this.AK = mImage.createImageNotZoom("/up0.png");
      this.AL = mImage.createImageNotZoom("/up1.png");
      this.AP = mImage.getImageWidth(this.AL.image);
      this.AR = mImage.getImageHeight(this.AL.image);
      this.AQ = mImage.getImageWidth(this.AK.image);
      mImage.getImageHeight(this.AK.image);
   }

   public final void paint(mGraphics var1) {
      var1.setColor(-10762241);
      var1.fillRect(0, 0, MotherCanvas.w, MotherCanvas.h / 2);
      var1.setColor(-11397);
      var1.fillRect(0, MotherCanvas.h / 2, MotherCanvas.w, MotherCanvas.h / 2);

      int var2;
      for(var2 = 0; var2 <= MotherCanvas.w / this.AP; ++var2) {
         var1.drawRegion((mImage)this.AL, var2 * this.AP, MotherCanvas.hh - this.AR / 2, 0);
      }

      for(var2 = 0; var2 <= MotherCanvas.w / this.AQ; ++var2) {
         var1.drawRegion((mImage)this.AK, var2 * this.AQ, MotherCanvas.hh + this.AR / 2, 0);
      }

      if (this.AJ != null) {
         var1.drawRegion((mImage)this.AJ, MotherCanvas.hw, MotherCanvas.h / 5, 3);
      }

      var1.setColor(0);
      if (this.AS) {
         var1.drawString((String)T.SU, MotherCanvas.hw, this.AG - 20 + 7, 2);
         var1.drawString((String)T.SV, MotherCanvas.hw, this.AG - 5 + 7, 2);
         var1.setColor(-16777216);
         var1.fillRect(MotherCanvas.hw - 50, this.AG - 5 + 7 + 20 - 10, 30, 20);
         var1.fillRect(MotherCanvas.hw - 15, this.AG - 5 + 7 + 20 - 10, 30, 20);
         var1.fillRect(MotherCanvas.hw + 20, this.AG - 5 + 7 + 20 - 10, 30, 20);
         var1.setColor(-1);
         var1.fillRect(MotherCanvas.hw - 50 + 1, this.AG - 5 + 7 + 20 - 10 + 1, 28, 18);
         var1.fillRect(MotherCanvas.hw - 15 + 1, this.AG - 5 + 7 + 20 - 10 + 1, 28, 18);
         var1.fillRect(MotherCanvas.hw + 20 + 1, this.AG - 5 + 7 + 20 - 10 + 1, 28, 18);
         var1.drawString((String)T.SX, MotherCanvas.hw - 35, this.AG + 5 + 20, 2);
         var1.drawString((String)T.SW, MotherCanvas.hw, this.AG + 5 + 20, 2);
         var1.drawString((String)T.SY, MotherCanvas.hw + 35, this.AG + 5 + 20, 2);
      } else {
         var1.drawString((String)AI, MotherCanvas.hw, this.AG - 20 + 7, 2);
         if (AC == 2 || AC == 3) {
            var1.drawRegion((mImage)this.AM, this.AF - 61, this.AG - 8, 0);
            if (this.AD >= 0) {
               var1.drawRegion(this.AN, 0, 0, this.AD, 16, 0, this.AF - 61, this.AG - 8, 0);
            }

            if ((var2 = this.AD) < 10) {
               var2 = 10;
            }

            if (var2 > this.AE - 12) {
               var2 = this.AE - 12;
            }

            var1.drawString((String)(AB + " / " + AA), MotherCanvas.hw, this.AG + 4, 2);
            var1.drawRegion((mImage)this.AO, this.AF - 60 + var2, this.AG, 3);
         }

      }
   }

   public final void update() {
      if (!this.AS) {
         if (AA > 0) {
            this.AD = this.AE * AB / AA;
            if (this.AD > this.AE) {
               this.AD = this.AE;
            }
         }

         if (AC == 0 && (GameCanvas.timeNow - this.AH) / 1000L > 15L) {
            if (GameCanvas.DA == 0) {
               Session_ME.getInstance().close();
               ++GameCanvas.DA;
               GameCanvas.connectDownload();
               GlobalService.getInstance().Request_Image_Android();
               this.AH = GameCanvas.timeNow;
            } else {
               AI = T.MM;
               AC = 1;
            }
         }

         if (AC == 3 && SaveImageRMS.AB.size() == 0) {
            GameCanvas.instance.AD();
            AF();
         }

      }
   }

   public final void updatePointer() {
      if (this.AS) {
         if (GameCanvas.isPoint(MotherCanvas.hw - 50, this.AG - 5 + 7 + 20 - 10, 30, 20)) {
            GameMidlet.ZOOM_IOS = 1;
            GameCanvas.instance.AD();
            AF();
            CRes.saveRMS("SUB_ZOOMIOS", new byte[]{GameMidlet.ZOOM_IOS});
         }

         if (GameCanvas.isPoint(MotherCanvas.hw - 15, this.AG - 5 + 7 + 20 - 10, 30, 20)) {
            GameMidlet.ZOOM_IOS = 2;
            GameCanvas.instance.AD();
            AF();
            CRes.saveRMS("SUB_ZOOMIOS", new byte[]{GameMidlet.ZOOM_IOS});
         } else {
            if (GameCanvas.isPoint(MotherCanvas.hw + 20, this.AG - 5 + 7 + 20 - 10, 30, 20)) {
               GameCanvas.instance.AD();
               AF();
            }

         }
      } else {
         if (AC == 1 && GameCanvas.isPointerDown) {
            Session_ME.getInstance().close();
            ++GameCanvas.DA;
            GameCanvas.connectDownload();
            GlobalService.getInstance().Request_Image_Android();
            this.AH = GameCanvas.timeNow;
            AC = 0;
            AI = T.BP;
         }

         super.updatePointer();
      }
   }

   public static void AB(int var0, int var1) {
      AB = 0;
      if (var1 >= 0) {
         AA = var1;
      }

      AC = 2;
   }

   public static void AA(String var0) {
      AI = var0;
   }

   private static void AF() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeUTF("1.2.9");
         CRes.saveRMS("Main_Load_Image_Android_OK", var0.toByteArray());
         var1.close();
      } catch (Exception var2) {
      }
   }
}
