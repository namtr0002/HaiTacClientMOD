import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;

public abstract class MotherCanvas extends Canvas implements Runnable {
   public static int w;
   public static int h;
   public static int hw;
   public static int hh;
   private static boolean hasPointerEvents;

   public static void setDisplay(GameMidlet mid) {
      Display.getDisplay(mid).setCurrent(GameCanvas.instance);
   }

   public MotherCanvas() {
      this.setFullScreenMode(true);
      int width = super.getWidth();
      int height = super.getHeight();
      int num = width;
      mGraphics.zoomLevel = 1;
      (new StringBuffer("mGraphics.zoomLevel = ")).append(mGraphics.zoomLevel).toString();
      w = (num + mGraphics.zoomLevel - 1) / mGraphics.zoomLevel;
      h = (height + mGraphics.zoomLevel - 1) / mGraphics.zoomLevel;
      hw = w / 2;
      hh = h / 2;
      width = w;
      width = h;
   }

   public final void update() {
      (new Thread(this)).start();
   }

   protected final void pointerDragged(int x, int y) {
      x /= mGraphics.zoomLevel;
      y /= mGraphics.zoomLevel;
      this.AA(x, y);
   }

   protected final void pointerPressed(int x, int y) {
      x /= mGraphics.zoomLevel;
      y /= mGraphics.zoomLevel;
      this.AB(x, y);
   }

   protected final void pointerReleased(int x, int y) {
      x /= mGraphics.zoomLevel;
      y /= mGraphics.zoomLevel;
      this.AC(x, y);
   }

   protected abstract void AA(int var1, int var2);

   protected abstract void AB(int var1, int var2);

   protected abstract void AC(int var1, int var2);

   protected abstract void AB();

   public static int AC() {
      return (int)(System.currentTimeMillis() / 1000L);
   }

   public static int targetFPS = 30;
   public static long targetSleepMs = 33L;
   public static int currentFPS = 30;
   private static int fpsCounter = 0;
   private static long lastFpsCheckTime = 0L;

   public static void setFPS(int fps) {
      if (fps <= 0) fps = 30;
      targetFPS = fps;
      targetSleepMs = 1000L / (long)fps;
   }

   public void run() {
      hasPointerEvents = true;

      while(hasPointerEvents) {
         long var1 = System.currentTimeMillis();
         fpsCounter++;
         if (var1 - lastFpsCheckTime >= 1000L) {
            currentFPS = fpsCounter;
            fpsCounter = 0;
            lastFpsCheckTime = var1;
         }

         int speed = AThMadaraMOD.gameSpeed;
         if (speed < 1) speed = 1;
         if (speed > 10) speed = 10;
         for (int sp = 0; sp < speed; sp++) {
            this.AB();
         }
         this.repaint();
         this.serviceRepaints();
         long var3 = System.currentTimeMillis() - var1;

         try {
            if (var3 < targetSleepMs) {
               Thread.sleep(targetSleepMs - var3);
            } else {
               Thread.sleep(1L);
            }
         } catch (InterruptedException var5) {
         }
      }
   }
}
