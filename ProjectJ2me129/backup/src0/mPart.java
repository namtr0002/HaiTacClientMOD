public final class mPart {
   public int timeNull;
   public long count = -1L;
   public PartImage[] pi;

   public mPart() {
   }

   public mPart(int type) {
      if (type == 0) {
         this.pi = new PartImage[5];
      }

      if (type == 2) {
         this.pi = new PartImage[15];
      }

      if (type == 1) {
         this.pi = new PartImage[20];
      }

      if (type == 4) {
         this.pi = new PartImage[2];
      }

      if (type == 5) {
         this.pi = new PartImage[2];
      }

      if (type == 3) {
         this.pi = new PartImage[24];
      }

   }
}
