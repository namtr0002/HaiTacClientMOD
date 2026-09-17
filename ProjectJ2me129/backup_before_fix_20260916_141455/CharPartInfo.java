import java.io.DataInputStream;
import java.util.Enumeration;

public final class CharPartInfo {
   public static MyHashTable hashMyPart = new MyHashTable();

   public static void LoadDataCharPart(DataInputStream dis, int var1) {
      if (dis == null) {
         GlobalService.getInstance().get_DATA((byte)23);
      } else {
         try {
            boolean var7 = false;
            short var8 = dis.readShort();
            int var2 = dis.readInt();
            int var3 = var2;
            DataInputStream var9 = dis;

            try {
               byte[] var4 = null;
               if (var3 > 0) {
                  var4 = new byte[var3];
                  var9.read(var4);
               }

               SmallImage.AA(var4);
            } catch (Exception var5) {
            }

            dis.close();
            GlobalService.AH = var8;
            SaveRms.AA(var8, "VerdataCharPart");
         } catch (Exception var6) {
         }
      }
   }

   public static mPart getPart(int index) {
      mPart mPartp;
      if ((mPartp = (mPart)hashMyPart.get("" + index)) == null) {
         mPartp = new mPart();
         hashMyPart.put("" + index, mPartp);
         GlobalService.getInstance().getDataPart((short)index);
      }

      mPartp.count = GameCanvas.timeNow / 1000L;
      if (mPartp.pi == null) {
         ++mPartp.timeNull;
         if (mPartp.timeNull >= 200) {
            GlobalService.getInstance().getDataPart((short)index);
            mPartp.timeNull = 0;
         }
      }

      return mPartp;
   }

   public static void checkDelHashCharPart(MyHashTable hash, int time) {
      mVector mVec = new mVector();
      Enumeration emn = hash.GetEnumerator();

      while(emn.hasMoreElements()) {
         String elm = (String)emn.nextElement();
         mPart mPartp = (mPart)hash.get(elm);
         if (GameCanvas.timeNow / 1000L - mPartp.count > 240L) {
            mVec.addElement(elm);
         }
      }

      for(int var6 = 0; var6 < mVec.size(); ++var6) {
         hash.remove(mVec.elementAt(var6));
      }

   }
}
