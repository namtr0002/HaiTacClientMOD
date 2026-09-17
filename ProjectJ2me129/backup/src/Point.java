public final class Point {
   public int x;
   public int y;
   public int AC;
   public int AD;
   public int AE = 0;
   public int AF;
   public int vx;
   public int vy;
   public int x2;
   public int y2;
   public int AK;
   public int AL;
   public int AM;
   public int dis;
   public int f;
   public int fRe;
   public int frame;
   public int AR;
   public int fSmall;
   public int AT;
   public int subType;
   public String name;
   public boolean AW = false;
   public boolean AX = false;
   public FrameImage AY;
   public MainObject AZ;

   public Point() {
   }

   public Point(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public final void update() {
      ++this.f;
      this.x += this.vx;
      this.y += this.vy;
   }
}
