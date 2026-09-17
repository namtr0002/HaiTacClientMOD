import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class MotherCanvas {
   public static int rawWidth = 800;
   public static int rawHeight = 600;
   public static int w = 800;
   public static int h = 600;
   public static int hw = 400;
   public static int hh = 300;
   private static boolean hasPointerEvents;

   public static int targetFPS = 60;
   public static long frameTime = 16;

   public static MotherCanvas instance;

   public MotherCanvas() {
      instance = this;
      this.checkZoomLevel();
   }

   public static void setDisplay(GameMidlet mid) {
   }

   public void setFullScreenMode(boolean mode) {
   }

   public boolean hasPointerEvents() {
      return true;
   }

   public void checkZoomLevel() {
      int width = rawWidth > 0 ? rawWidth : 800;
      int height = rawHeight > 0 ? rawHeight : 600;

      int zoom = mGraphics.zoomLevel > 0 ? mGraphics.zoomLevel : 1;
      w = (width + zoom - 1) / zoom;
      h = (height + zoom - 1) / zoom;
      if (w < 320) w = 320;
      if (h < 240) h = 240;
      hw = w / 2;
      hh = h / 2;
   }

   public static void setFPS(int fps) {
      if (fps < 10) fps = 10;
      if (fps > 240) fps = 240;
      targetFPS = fps;
      frameTime = 1000L / (long)targetFPS;
   }

   public static void loadFPSSetting() {
   }

   public static void AC() {
   }

   public static void getByteMap() {
   }

   public static void byteMap() {
   }

   public void sizeChanged(int w, int h) {
      rawWidth = w;
      rawHeight = h;
      checkZoomLevel();
   }

   public void keyReleased(int keyCode) {
   }

   public void keyPressed(int keyCode) {
   }

   public void keyRepeated(int keyCode) {
   }

   public void pointerPressed(int x, int y) {
   }

   public void pointerReleased(int x, int y) {
   }

   public void pointerDragged(int x, int y) {
   }

   public abstract void AB();
}
