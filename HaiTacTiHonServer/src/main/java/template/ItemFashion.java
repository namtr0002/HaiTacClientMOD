package template;

import java.util.List;

public class ItemFashion {
    public static List<ItemFashion> ENTRYS = new java.util.ArrayList<>();
    public final int price;
    public final short ID;
    public final short idIcon;
    public final String name;
    public final String info;
    public final short[] mWearing;
    public final List<Option> op;
    public int hsd;

    public ItemFashion(short ID, short IDIcon, String name, String info, short[] wear,
            List<Option> op, int price, int hsd) {
       
        this.ID = ID;
        this.idIcon = IDIcon;
        this.name = name;
        this.info = info;
        this.mWearing = wear;
        this.op = op;
        this.price = price;
        this.hsd = hsd;
    }

    public static ItemFashion get_item(int id) {
        if (ItemFashion.ENTRYS == null) return null;
        for (int i = 0; i < ItemFashion.ENTRYS.size(); i++) {
            ItemFashion temp = ItemFashion.ENTRYS.get(i);
            if (temp != null && temp.ID == id) {
                return temp;
            }
        }
        return null;
    }
    
}
