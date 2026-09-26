import java.util.Enumeration;
import java.util.Hashtable;

public final class MyHashTable {
   private Hashtable hash = new Hashtable();
   public String str = "";
   public byte typeKey = -1;

   public MyHashTable(byte typeKey, String var1) {
      this.hash = new Hashtable();
      this.typeKey = typeKey;
      this.str = var1;
   }

   public MyHashTable(String var1) {
      this.hash = new Hashtable();
      this.str = var1;
   }

   public MyHashTable() {
      this.hash = new Hashtable();
   }

   public final Object get(Object var1) {
      return this.hash.get(var1);
   }

   public final void clear() {
      this.hash.clear();
   }

   public final void AA() {
      this.clear();
   }

   public final Enumeration GetEnumerator() {
      return this.hash.keys();
   }

   public final boolean equals(Object var1) {
      return this.hash.equals(var1);
   }

   public final void put(Object var1, Object var2) {
      this.hash.put(var1, var2);
   }

   public final void remove(Object var1) {
      this.hash.remove(var1);
   }

   public final int size() {
      return this.hash.size();
   }

   public final boolean containsKey(Object var1) {
      return this.hash.containsKey(var1);
   }

   public final Enumeration keys() {
      return this.hash.keys();
   }
}
