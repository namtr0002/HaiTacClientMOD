import java.util.Vector;

public final class mVector {
   private Vector myvec = new Vector();

   public mVector(String var1) {
   }

   public mVector() {
   }

   public final void addElement(Object var1) {
      if (this.myvec != null) {
         this.myvec.addElement(var1);
      }
   }

   public final int size() {
      return this.myvec == null ? 0 : this.myvec.size();
   }

   public final Object elementAt(int var1) {
      try {
         return (this.myvec != null && var1 >= 0 && var1 < this.myvec.size()) ? this.myvec.elementAt(var1) : null;
      } catch (Exception e) {
         return null;
      }
   }

   public final void setElementAt(Object var1, int var2) {
      try {
         if (this.myvec != null && var2 >= 0 && var2 < this.myvec.size()) {
            this.myvec.setElementAt(var1, var2);
         }
      } catch (Exception e) {
      }
   }

   public final int indexOf(Object var1) {
      return this.myvec != null ? this.myvec.indexOf(var1) : -1;
   }

   public final void removeElement(int var1) {
      try {
         if (this.myvec != null && var1 >= 0 && var1 < this.myvec.size()) {
            this.myvec.removeElementAt(var1);
         }
      } catch (Exception e) {
      }
   }

   public final void removeElementAt(int var1) {
      try {
         if (this.myvec != null && var1 >= 0 && var1 < this.myvec.size()) {
            this.myvec.removeElementAt(var1);
         }
      } catch (Exception e) {
      }
   }

   public final void removeElement(Object var1) {
      try {
         if (this.myvec != null) {
            this.myvec.removeElement(var1);
         }
      } catch (Exception e) {
      }
   }

   public final void removeAllElements() {
      if (this.myvec != null) {
         this.myvec.removeAllElements();
      }
   }

   public final void insertElementAt(Object var1, int var2) {
      try {
         if (this.myvec != null) {
            int idx = (var2 >= 0 && var2 <= this.myvec.size()) ? var2 : 0;
            this.myvec.insertElementAt(var1, idx);
         }
      } catch (Exception e) {
      }
   }
   
   public final boolean isEmpty() {
       return this.myvec == null || myvec.isEmpty();
   }

   public final boolean contains(Object var1) {
       return this.myvec != null && this.myvec.contains(var1);
   }
}
