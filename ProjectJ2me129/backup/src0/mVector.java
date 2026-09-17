import java.util.Vector;

public final class mVector {
   private Vector myvec = new Vector();

   public mVector(String var1) {
   }

   public mVector() {
   }

   public final void addElement(Object var1) {
      this.myvec.addElement(var1);
   }

   public final int size() {
      return this.myvec == null ? 0 : this.myvec.size();
   }

   public final Object elementAt(int var1) {
      return var1 >= 0 && var1 < this.myvec.size() ? this.myvec.elementAt(var1) : null;
   }

   public final void setElementAt(Object var1, int var2) {
      if (var2 >= 0 && var2 < this.myvec.size()) {
         this.myvec.setElementAt(var1, var2);
      }

   }

   public final int indexOf(Object var1) {
      return this.myvec.indexOf(var1);
   }

   public final void removeElement(int var1) {
      if (var1 >= 0 && var1 < this.myvec.size()) {
         this.myvec.removeElementAt(var1);
      }

   }

   public final void removeElement(Object var1) {
      this.myvec.removeElement(var1);
   }

   public final void removeAllElements() {
      this.myvec.removeAllElements();
   }

   public final void insertElementAt(Object var1, int var2) {
      this.myvec.insertElementAt(var1, 0);
   }
   
   public final boolean isEmpty() {
       return myvec.isEmpty();
   }
}
