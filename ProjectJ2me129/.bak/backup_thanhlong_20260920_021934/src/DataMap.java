public final class DataMap {
   public static MyHashTable hashDataMap = new MyHashTable();
   public byte[] dataItem;
   public byte[] data;
   public mVector vecPointMap = new mVector("DataMap.vecPointMap");
   public short HBack;
   public byte IDBack;
   public String nameMap;

   public final void setDataMap(byte[] var1) {
      this.data = var1;
   }

   public final void setDataItemMap(byte[] var1) {
      this.dataItem = var1;
   }
}
