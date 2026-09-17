package com.deplor.haitactihontool.part;

public class mPart {
   public int id;
   public int type;
   public PartImage[] pi;

   public mPart() {
   }

   public mPart(int type) {
      this.type = type;
      int len = 0;
      switch (type) {
         case 0:
            len = 5;
            break;
         case 1:
            len = 20;
            break;
         case 2:
            len = 15;
            break;
         case 3:
            len = 24;
            break;
         case 4:
            len = 2;
            break;
         case 5:
            len = 2;
      }

      this.pi = new PartImage[len];

      for (int i = 0; i < len; i++) {
         this.pi[i] = new PartImage();
      }
   }
}
