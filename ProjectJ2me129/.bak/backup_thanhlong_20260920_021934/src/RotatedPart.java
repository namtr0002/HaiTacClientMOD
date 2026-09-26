import javax.microedition.lcdui.Image;

public final class RotatedPart {
   public Image image;
   public int dx;
   public int dy;

   public RotatedPart(Image img, int dx, int dy) {
      this.image = img;
      this.dx = dx;
      this.dy = dy;
   }
}
