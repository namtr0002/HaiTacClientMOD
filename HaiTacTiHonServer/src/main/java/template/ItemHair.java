package template;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class ItemHair {
    public static List<ItemHair> ENTRYS = new ArrayList<>();
    public static HashMap<Integer, ItemHair> hashMap = new HashMap<>();
    public final short ID;
    public final short idIcon;
    public final String name;
    public final int beri;
    public final short ruby;
    public byte type;
    public ArrayList<Option> options;

    public ItemHair(int ID, int IDIcon, String name, int b, int r, int type, ArrayList<Option> options) {
        this.ID = (short) ID;
        this.idIcon = (short) IDIcon;
        this.name = name != null ? name : "";
        this.beri = b;
        this.ruby = (short) r;
        this.type = (byte) type;
        this.options = options != null ? options : new ArrayList<>();
    }

    public static int getCompoundKey(int id, int type) {
        return (type << 16) | (id & 0xFFFF);
    }

    public static void putItem(ItemHair item) {
        if (item == null) return;
        ENTRYS.add(item);
        hashMap.put(getCompoundKey(item.ID, item.type), item);
        if (item.type == 103) {
            hashMap.put((int) item.ID, item);
        }
    }

    public static ItemHair get_item(int id, int type) {
        ItemHair found = hashMap.get(getCompoundKey(id, type));
        if (found != null) return found;
        for (int i = 0; i < ItemHair.ENTRYS.size(); i++) {
            ItemHair temp = ItemHair.ENTRYS.get(i);
            if (temp != null && temp.ID == id && temp.type == type) {
                return temp;
            }
        }
        return null;
    }
    
    public static ItemHair getItemHair(int id) {
        return get_item(id, 103);
    }

    public static ItemHair getItemHair(int id, int type) {
        return get_item(id, type);
    }

    public static int get_size_type(int type) {
        int size = 0;
        for (int i = 0; i < ItemHair.ENTRYS.size(); i++) {
            ItemHair temp = ItemHair.ENTRYS.get(i);
            if (temp != null && temp.type == type) {
                size++;
            }
        }
        return size;
    }

    public static ItemHair read_json_it_hair(database.DbResultRow r) throws Exception {
        short b = r.getShort("id");
        String name = r.getString("name");
        if (name == null) name = "";
        short idicon = r.getShort("icon");
        int price = r.getInt("beri");
        short priceRuby = r.getShort("ruby");
        byte type = 103;
        try {
            byte t = r.getByte("type");
            if (t > 0) {
                type = t;
            }
        } catch (Exception ignored) {}
        ArrayList<Option> options = new ArrayList<>();
        JSONArray js = (JSONArray) JSONValue.parse(r.getString("options"));
        if (js != null) {
            for (int i = 0; i < js.size(); i++) {
                JSONArray js2 = (JSONArray) JSONValue.parse(js.get(i).toString());
                options.add(new Option(Short.parseShort(js2.get(0).toString()), Short.parseShort(js2.get(1).toString())));
            }
        }
        return new ItemHair(b, idicon, name, price, priceRuby, type, options);
    }

    public static ItemHair read_json_it_hair(ResultSet rs) throws SQLException {
        short b = rs.getShort("id");
        String name = rs.getString("name");
        if (name == null) name = "";
        short idicon = rs.getShort("icon");
        int price = rs.getInt("beri");
        short priceRuby = rs.getShort("ruby");
        byte type = 103;
        try {
            type = rs.getByte("type");
            if (type <= 0) type = 103;
        } catch (Exception ignored) {}
        ArrayList<Option> options = new ArrayList<>();
        JSONArray js = (JSONArray) JSONValue.parse(rs.getString("options"));
        if (js != null) {
            for(int i = 0; i < js.size(); i++) {
                JSONArray js2 = (JSONArray) JSONValue.parse(js.get(i).toString());
                options.add(new Option(Short.parseShort(js2.get(0).toString()), Short.parseShort(js2.get(1).toString())));
            }
        }
        return new ItemHair(b, idicon, name, price, priceRuby, type, options);
    }
}
