public class MarqueeText {
   public int limit;
   public int maxW;
   public int speed = 1;
   public int xplus;
   public int time;
   public String text = "";
   public boolean isRun;
   public mFont fontPaint;
   public boolean isLeftRight;

   public MarqueeText(int maxW) {
      this.maxW = maxW;
   }

   public void setdata(String str, mFont font) {
      this.fontPaint = font;
      this.text = str;
      if (font != null && str != null) {
         int width = font.getWidth(str);
         this.xplus = 0;
         this.time = 0;
         this.isLeftRight = false;
         if (width > this.maxW) {
            this.limit = width - this.maxW + 15;
            this.isRun = true;
         } else {
            this.isRun = false;
         }
      } else {
         this.isRun = false;
      }
   }

   public void update() {
      if (this.isRun) {
         this.time++;
         if (this.time > 10) {
            this.xplus += this.speed;
         }
         if (this.xplus >= this.limit) {
            this.xplus = 0;
            this.time = 0;
         }
      } else {
         this.xplus = 0;
         this.time = 0;
      }
   }

   public void update2() {
      if (this.isRun) {
         ++this.time;
         if (this.time > 15) {
            if (!this.isLeftRight) {
               this.xplus += this.speed;
            } else {
               this.xplus -= this.speed;
            }
         }

         if (this.xplus > this.limit || this.xplus < -5) {
            this.isLeftRight = !this.isLeftRight;
         }
      } else {
         this.xplus = 0;
         this.time = 0;
      }
   }

   public void paint(mGraphics g, int x, int y, int align) {
      if (this.fontPaint != null && this.text != null) {
         this.fontPaint.drawString(g, this.text, x - this.xplus, y, align);
      }
   }

   public int getxPlus() {
      return this.xplus;
   }
}
