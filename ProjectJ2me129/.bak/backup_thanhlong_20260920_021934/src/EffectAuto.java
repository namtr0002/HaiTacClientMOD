import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public final class EffectAuto extends MainItemMap {
   private MainEffectAuto eff;
   private int nCountReplay = 0;
   private boolean isPaint = true;
   private int typeEffect;
   private int valueEffect;
   private long timeBegin;
   private int indexSound = -1;
   private int timePlaySound = -1;
   private int tickupdate;

   public EffectAuto(String var1) {
      super.TypeItem = 1;
      String[] array = mFont.split(var1, ";");
      super.IDItem = Short.parseShort(array[0]);
      super.IDImage = Short.parseShort(array[1]);
      super.x = Integer.parseInt(array[2]) * LoadMap.wTile;
      super.y = Integer.parseInt(array[3]) * LoadMap.wTile;
      super.dx = Integer.parseInt(array[4]);
      super.dy = Integer.parseInt(array[5]);
      this.typeEffect = Integer.parseInt(array[6]);
      this.valueEffect = Integer.parseInt(array[7]);
      super.wOne = 70;
      super.hOne = 70;
      super.ySort = super.y;
      this.eff = this.loadTemEff(super.IDImage);
      this.timeBegin = GameCanvas.timeNow;
      this.isPaint = true;
   }

   public final void paint(mGraphics g) {
      if (this.eff != null && this.eff.img != null && this.isPaint) {
         short num = this.eff.mRunFrame[super.f];
         int num2 = this.eff.mFrame[num].mpart.length;

         for(int i = 0; i < num2; ++i) {
            MainPartImage mainPartImage;
            if ((mainPartImage = (MainPartImage)this.eff.hashImage.get("" + this.eff.mFrame[num].mpart[i].idPartImage)) != null) {
               g.drawRegion(this.eff.img, mainPartImage.x, mainPartImage.y, mainPartImage.w, mainPartImage.h, 0, super.x + super.dx + this.eff.mFrame[num].mpart[i].x, super.y + super.dy + this.eff.mFrame[num].mpart[i].y, 0);
            }
         }
      }

   }

   public final void update() {
      if (this.eff != null && this.eff.img != null) {
         if (super.f >= this.eff.mRunFrame.length - 1) {
            switch(this.typeEffect) {
            case 0:
               ++this.nCountReplay;
               this.isPaint = false;
               if (this.nCountReplay >= this.valueEffect) {
                  this.nCountReplay = 0;
                  this.isPaint = true;
                  super.f = 0;
                  return;
               }
               break;
            case 1:
               super.f = 0;
               return;
            case 2:
               this.isPaint = false;
               if (GameCanvas.gameTick % 5 == 0 && (GameCanvas.timeNow - this.timeBegin) / 1000L > (long)this.valueEffect) {
                  this.timeBegin = GameCanvas.timeNow;
                  super.f = 0;
                  this.isPaint = true;
                  return;
               }
               break;
            case 3:
               return;
            case 4:
               if (CRes.random(this.valueEffect) == 0) {
                  super.f = 0;
                  if (this.indexSound >= 0 && CRes.random(this.timePlaySound) == 0 && this.AB()) {
                     int var10000 = this.indexSound;
                     float var1 = mSound.volumeSound;
                     mSound.playSound();
                     return;
                  }
               }
               break;
            default:
               return;
            }
         } else {
            ++super.f;
         }

      } else {
         ++this.tickupdate;
         if (this.tickupdate > 50) {
            this.tickupdate = 0;
            this.eff = this.loadTemEff(super.IDImage);
         }

      }
   }

   private MainEffectAuto loadTemEff(short id) {
      MainEffectAuto mainEffectAuto;
      if ((mainEffectAuto = (MainEffectAuto)MainEffectAuto.hashTemEffAuto.get("" + id)) == null) {
         MainEffectAuto mainEffectAuto2 = new MainEffectAuto();
         GlobalService.getInstance().getDataEffAuto(id);
         mainEffectAuto = mainEffectAuto2;
         MainEffectAuto.hashTemEffAuto.put(String.valueOf(id), mainEffectAuto2);
      }

      return mainEffectAuto;
   }

   public static MainEffectAuto readData(byte[] dataeff, boolean var1) {
      MainEffectAuto mainEffectAuto = null;

      try {
         ByteArrayInputStream bais = new ByteArrayInputStream(dataeff);
         DataInputStream var7;
         short var2 = (var7 = new DataInputStream(bais)).readShort();
         byte[] var3 = new byte[var7.readShort()];
         var7.read(var3);
         ByteArrayInputStream var9 = new ByteArrayInputStream(var3);
         DataInputStream var10 = new DataInputStream(var9);
         byte[] var4 = new byte[var7.available()];
         var7.read(var4);
         mainEffectAuto = new MainEffectAuto(var10, var4);
         MainEffectAuto.hashTemEffAuto.put(String.valueOf(var2), mainEffectAuto);
      } catch (Exception var5) {
      }

      return mainEffectAuto;
   }
}
