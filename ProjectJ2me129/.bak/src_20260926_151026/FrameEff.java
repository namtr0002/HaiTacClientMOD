public final class FrameEff {
   public mVector listPartTop = new mVector();
   public mVector listPartBottom = new mVector();
   public mVector listPartPaint;
   public mVector AB = listPartBottom;
   public byte xShadow;
   public byte yShadow;

   public FrameEff(mVector var1, mVector var2) {
      this.listPartTop = (var1 != null) ? var1 : new mVector();
      this.listPartBottom = (var2 != null) ? var2 : new mVector();
      this.AB = this.listPartBottom;
      this.listPartPaint = new mVector();
      for (int i = 0; i < this.listPartBottom.size(); i++) {
         this.listPartPaint.addElement(this.listPartBottom.elementAt(i));
      }
      for (int j = 0; j < this.listPartTop.size(); j++) {
         this.listPartPaint.addElement(this.listPartTop.elementAt(j));
      }
   }

   public mVector getListPartPaint() {
      return this.listPartPaint;
   }
}
