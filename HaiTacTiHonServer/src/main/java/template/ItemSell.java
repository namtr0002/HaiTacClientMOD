package template;

import event.SuKienHalloween;
import event.eboss.BiNgoMa;
import event.SuKienNoel;
import event.eboss.SantaNoel;
import event.eboss.QuaiVatTuyetNoel;
import event.EventData;
import model.Player;
import core.Manager;
import network.Service;
import event.EventManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemSell {

    public int id;
    public int price;
    public static HashMap<Integer, List<List<ItemSell>>> ENTRYS = new HashMap<>();
    public static short[] ITEM_POTION_SELL = new short[]{2, 3, 85, 5, 4, 7, 18, 43, 40, 89,
        80, 31, 6, 232, 361, 548, 173, 174, 271};// bán item ở shop thức ăn
    public static byte[] ITEM_MATERIAL_SELL = new byte[]{1, 2, 3, 4, 5, 6, 9};

    public static void init() {
        ItemSell.ENTRYS.clear();
        for (int i = 0; i < ItemTemplate3.ENTRYS.size(); i++) {
            ItemTemplate3 it_temp = ItemTemplate3.ENTRYS.get(i);
            if (it_temp != null && it_temp.beri > 0) {
                ItemSell temp = new ItemSell(it_temp.id, it_temp.beri);
                int lvlGrp = it_temp.level / 10;
                List<List<ItemSell>> list_temp = ItemSell.ENTRYS.get(lvlGrp);
                if (list_temp == null) {
                    list_temp = new ArrayList<>();
                    for (int j = 0; j < 5; j++) {
                        list_temp.add(new ArrayList<>());
                    }
                    ItemSell.ENTRYS.put(lvlGrp, list_temp);
                }
                if (it_temp.clazz >= 1 && it_temp.clazz <= 5) {
                    List<ItemSell> temp1 = list_temp.get(it_temp.clazz - 1);
                    temp1.add(temp);
                }
            }
        }
    }

    static {
        init();
    }

    public ItemSell(int id, int price) {
        this.id = id;
        this.price = price;
    }

    public static List<ItemSell> get_it_sell(int level, int clazz) {
        List<ItemSell> result = new ArrayList<>();
        level /= 10;
        if (level < 2) {
            result.addAll(ItemSell.ENTRYS.get(0).get(clazz));
            result.addAll(ItemSell.ENTRYS.get(1).get(clazz));
            result.addAll(ItemSell.ENTRYS.get(2).get(clazz));
        } else if (level > 7) {
            result.addAll(ItemSell.ENTRYS.get(7).get(clazz));
            result.addAll(ItemSell.ENTRYS.get(8).get(clazz));
            result.addAll(ItemSell.ENTRYS.get(9).get(clazz));
        } else {
            result.addAll(ItemSell.ENTRYS.get(level - 1).get(clazz));
            result.addAll(ItemSell.ENTRYS.get(level).get(clazz));
            result.addAll(ItemSell.ENTRYS.get(level + 1).get(clazz));
        }
        return result;
    }

    public static boolean check_item_sell_potion(Player p, short id) throws IOException {
        for (int i = 0; i < ItemSell.ITEM_POTION_SELL.length; i++) {
            if (ItemSell.ITEM_POTION_SELL[i] == id) {
                return true;
            }
        }
        for (int i = 7; i <= 28; i++) { // ruong kho bau (7-17), ruong huyen bi (18-28)
            if (i == id) {
                return true;
            }
        }
        return false;
    }

    public static short[] get_it_sell_potion(Player p) {
        int lvGrp = (p != null) ? Math.min(10, Math.max(0, p.level / 10)) : 0;
        short[] result = new short[ItemSell.ITEM_POTION_SELL.length];
        for (int i = 0; i < ItemSell.ITEM_POTION_SELL.length; i++) {
            result[i] = ItemSell.ITEM_POTION_SELL[i];
        }
        if (result.length > 6) {
            result[5] = (short) (7 + lvGrp);
            result[6] = (short) (18 + lvGrp);
        }
        return result;
    }

    public static byte[] get_it_sell_material() {
        return ItemSell.ITEM_MATERIAL_SELL;
    }

    public static boolean check_item_sell_material(Player p, short id) throws IOException {
        for (int i = 0; i < ItemSell.ITEM_MATERIAL_SELL.length; i++) {
            if (ItemSell.ITEM_MATERIAL_SELL[i] == id) {
                return true;
            }
        }
        return false;
    }
}
