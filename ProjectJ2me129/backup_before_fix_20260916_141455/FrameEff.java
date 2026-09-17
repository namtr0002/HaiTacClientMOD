public final class FrameEff {
   public mVector listPartTop = new mVector();
   public mVector listPartBottom = new mVector();
   public mVector AB = listPartBottom;
   public byte xShadow;
   public byte yShadow;

   public FrameEff(mVector var1, mVector var2) {
      this.listPartTop = var1;
      this.listPartBottom = var2;
      this.AB = var2;
   }

   public mVector getListPartPaint() {
      mVector mVector2 = new mVector();
      if (this.listPartBottom != null) {
         for (int i = 0; i < this.listPartBottom.size(); i++) {
            mVector2.addElement(this.listPartBottom.elementAt(i));
         }
      }
      if (this.listPartTop != null) {
         for (int j = 0; j < this.listPartTop.size(); j++) {
            mVector2.addElement(this.listPartTop.elementAt(j));
         }
      }
      return mVector2;
   }
}
