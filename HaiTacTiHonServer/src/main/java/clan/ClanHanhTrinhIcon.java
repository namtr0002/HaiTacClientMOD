package clan;

import core.ZUtil;

import java.util.ArrayList;
import java.util.List;
import template.Option;

public class ClanHanhTrinhIcon {
   public static List<ClanHanhTrinhIcon> ENTRY = new ArrayList<>();
   public int id;
   public short icon;
   public String name, info;
   public List<Option> op;
   public int rd;

   public static ClanHanhTrinhIcon getById(int id) {
      if (ClanHanhTrinhIcon.ENTRY == null) return null;
      for (int i = 0; i < ClanHanhTrinhIcon.ENTRY.size(); i++) {
         if (ClanHanhTrinhIcon.ENTRY.get(i).id == id) {
            return ClanHanhTrinhIcon.ENTRY.get(i);
         }
      }
      return null;
   }

   public static ClanHanhTrinhIcon getRd() {
      if (ClanHanhTrinhIcon.ENTRY == null || ClanHanhTrinhIcon.ENTRY.isEmpty()) {
         return null;
      }
      List<ClanHanhTrinhIcon> listRd = new ArrayList<>();
      for (int i = 0; i < ClanHanhTrinhIcon.ENTRY.size(); i++) {
         ClanHanhTrinhIcon item = ClanHanhTrinhIcon.ENTRY.get(i);
         if (item != null && item.rd > 0) {
            for (int j = 0; j < item.rd; j++) {
               listRd.add(item);
            }
         }
      }
      if (listRd.isEmpty()) {
         return ClanHanhTrinhIcon.ENTRY.get(ZUtil.random(ClanHanhTrinhIcon.ENTRY.size()));
      }
      return listRd.get(ZUtil.random(listRd.size()));
   }
}
