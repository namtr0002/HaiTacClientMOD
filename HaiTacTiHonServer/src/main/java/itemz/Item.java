package itemz;

import model.Player;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import network.Message;
import template.*;

public class Item {
    public static final int DEFAULT_BAG = 30;
    public static final int DEFAULT_BOX = 30;
    public static final int MAX_BAG_LIMIT = 1260;
    public static final int MAX_BOX_LIMIT = 126;

    public short max_bag = DEFAULT_BAG;
    public short max_box = DEFAULT_BOX;
    private final Player p;
    public Item_wear[] bag3 = new Item_wear[max_bag];
    public Item_wear[] box3 = new Item_wear[max_box];
    public static final int MAX_BODY = 16;
    public Item_wear[] it_body = new Item_wear[MAX_BODY];
    public List<ItemBag47> bag47 = new ArrayList<>();
    public List<ItemBag47> box47 = new ArrayList<>();
    public Item_wear it_heart;
    public List<Item_wear> save_item_wear = new ArrayList<>();
    public List<ItemBag47> save_item_47 = new ArrayList<>();

    public Item(Player p) {
        this.p = p;
        if (core.Manager.gI() != null && core.Manager.gI().isTestMode()) {
            this.max_bag = (short) MAX_BAG_LIMIT;
            this.max_box = (short) MAX_BOX_LIMIT;
            this.bag3 = new Item_wear[MAX_BAG_LIMIT];
            this.box3 = new Item_wear[MAX_BOX_LIMIT];
        }
    }

    public void sendNumCellBag() throws IOException {
        Message m = new Message(-12);
        m.writer().writeByte(6);
        m.writer().writeByte(3);
        m.writer().writeShort(max_bag & 0xFFFF); // max bag
        p.addmsg(m);
        m.cleanup();
    }

    public void sendNumCellBox() throws IOException {
        Message m = new Message(-32);
        m.writer().writeByte(6);
        m.writer().writeByte(3);
        m.writer().writeShort(max_box & 0xFFFF); // max box
        p.addmsg(m);
        m.cleanup();
    }

    /**
     * Mở rộng hành trang thêm {@code slots} ô.
     * @return số ô thực tế đã mở thêm (0 nếu đã đạt max)
     */
    public int expand_bag(int slots) {
        int newSize = Math.min(MAX_BAG_LIMIT, (max_bag & 0xFFFF) + slots);
        int added = newSize - (max_bag & 0xFFFF);
        if (added <= 0) return 0;
        max_bag = (short) newSize;
        Item_wear[] newBag = new Item_wear[newSize];
        System.arraycopy(bag3, 0, newBag, 0, Math.min(bag3.length, newSize));
        bag3 = newBag;
        try { sendNumCellBag(); } catch (Exception ignored) {}
        return added;
    }

    /**
     * Mở rộng rương đồ thêm {@code slots} ô.
     * @return số ô thực tế đã mở thêm (0 nếu đã đạt max)
     */
    public int expand_box(int slots) {
        int newSize = Math.min(MAX_BOX_LIMIT, (max_box & 0xFFFF) + slots);
        int added = newSize - (max_box & 0xFFFF);
        if (added <= 0) return 0;
        max_box = (short) newSize;
        Item_wear[] newBox = new Item_wear[newSize];
        System.arraycopy(box3, 0, newBox, 0, Math.min(box3.length, newSize));
        box3 = newBox;
        try { sendNumCellBox(); } catch (Exception ignored) {}
        return added;
    }

    public void updateInventory(boolean flag) {
        try {
            update_bag(4, flag);
            update_bag(7, flag);
            update_bag(3, flag);
            update_bag(5, flag);
        } catch (IOException e) {
        }
    }

    public static boolean isItemExist(int category, int id) {
        switch (category) {
            case 3:
                return ItemTemplate3.get_it_by_id(id) != null;
            case 4:
                return id == 872 || ItemTemplate4.get_it_by_id(id) != null || ItemTemplate8.get_it_by_id(id) != null;
            case 5:
                return id >= 0 && id < DataTemplate.NamePotionquest.length && DataTemplate.NamePotionquest[id] != null && !DataTemplate.NamePotionquest[id].trim().isEmpty();
            case 7:
                return ItemTemplate7.get_it_by_id(id) != null;
            case 8:
                return ItemTemplate8.get_it_by_id(id) != null || ItemTemplate4.get_it_by_id(id) != null;
            default:
                return false;
        }
    }

    private void update_bag(int type, boolean flag) throws IOException {
        Message m = new Message(-12);
        m.writer().writeByte(0);
        m.writer().writeByte(type);
        switch (type) {
            case 3: {
                m.writer().writeShort(this.quant_item_inbag(3));
                for (int i = 0; i < bag3.length; i++) {
                    if (bag3[i] != null && bag3[i].template != null && ItemTemplate3.get_it_by_id(bag3[i].template.id) != null) {
                        bag3[i].index = (short) i;
                        Item.readUpdateItem(m.writer(), bag3[i], p);
                    }
                }
                break;
            }
            case 4:
            case 8: {
                m.writer().writeShort(this.quant_item_inbag(4));
                for (int i = 0; i < bag47.size(); i++) {
                    ItemBag47 it = bag47.get(i);
                    if (it != null && it.category == 4 && it.quant > 0 && isItemExist(4, it.id)) {
                        m.writer().writeShort(it.id);
                        m.writer().writeShort(it.quant);
                    }
                }
                break;
            }
            case 5: {
                m.writer().writeShort(this.quant_item_inbag(5));
                for (int i = 0; i < bag47.size(); i++) {
                    ItemBag47 it = bag47.get(i);
                    if (it != null && it.category == 5 && it.quant > 0 && isItemExist(5, it.id)) {
                        m.writer().writeShort(it.id);
                        m.writer().writeUTF(DataTemplate.NamePotionquest[it.id]);
                        m.writer().writeShort(it.quant);
                    }
                }
                break;
            }
            case 7: {
                m.writer().writeShort(this.quant_item_inbag(7));
                for (int i = 0; i < bag47.size(); i++) {
                    ItemBag47 it = bag47.get(i);
                    if (it != null && it.category == 7 && it.quant > 0 && isItemExist(7, it.id)) {
                        m.writer().writeByte(it.id);
                        m.writer().writeShort(it.quant);
                    }
                }
                break;
            }
        }
        if (flag) {
            p.msgs.add(m);
        } else {
            p.addmsg(m);
        }
        m.cleanup();
    }

    public void update_Inventory_box(int type, boolean b) throws IOException {
        if (type == -1) {
            update_box(4, b);
            update_box(7, b);
            update_box(3, b);
        } else {
            update_box(type, b);
        }
    }
    public void update_Inventory_bag(int type, boolean b) throws IOException {
        if (type == -1) {
            update_bag(4, b);
            update_bag(7, b);
            update_bag(3, b);
        } else {
            update_bag(type, b);
        }
    }

    private void update_box(int type, boolean b) throws IOException {
        Message m = new Message(-32);
        m.writer().writeByte(0);
        m.writer().writeByte(type);
        switch (type) {
            case 3: {
                m.writer().writeShort(this.quant_item_inbox(3));
                for (int i = 0; i < box3.length; i++) {
                    if (box3[i] != null && box3[i].template != null && ItemTemplate3.get_it_by_id(box3[i].template.id) != null) {
                        box3[i].index = (short) i;
                        Item.readUpdateItem(m.writer(), box3[i], p);
                    }
                }
                break;
            }
            case 4:
            case 8: {
                m.writer().writeShort(this.quant_item_inbox(4));
                for (int i = 0; i < box47.size(); i++) {
                    ItemBag47 it = box47.get(i);
                    if (it != null && it.category == 4 && it.quant > 0 && isItemExist(4, it.id)) {
                        m.writer().writeShort(it.id);
                        m.writer().writeShort(it.quant);
                    }
                }
                break;
            }
            case 5: {
                m.writer().writeShort(0);
                break;
            }
            case 7: {
                m.writer().writeShort(this.quant_item_inbox(7));
                for (int i = 0; i < box47.size(); i++) {
                    ItemBag47 it = box47.get(i);
                    if (it != null && it.category == 7 && it.quant > 0 && isItemExist(7, it.id)) {
                        m.writer().writeByte(it.id);
                        m.writer().writeShort(it.quant);
                    }
                }
                break;
            }
        }
        if (b) {
            p.msgs.add(m);
        } else {
            p.addmsg(m);
        }
        m.cleanup();
    }

    public void updateMoney(boolean b) throws IOException {
        Message m = new Message(-12);
        m.writer().writeByte(3);
        m.writer().writeByte(6);
        //
        m.writer().writeLong(p.get_vang());
        m.writer().writeInt(p.get_ngoc());
        m.writer().writeShort(p.get_ticket()); // ticket
        m.writer().writeShort(p.get_ticket_max()); // max ticket
        m.writer().writeByte((byte) p.get_pvp_ticket());
        m.writer().writeByte(p.get_pvp_ticket_max()); // max pvp ticket
        m.writer().writeByte((byte) p.get_key_boss());
        m.writer().writeByte(p.get_key_boss_max()); // max key boss
        m.writer().writeInt((int) p.get_vnd()); // vnd
        m.writer().writeInt((int) p.get_bua()); // bua
        m.writer().writeInt(p.get_coin()); // diem nap
        m.writer().writeInt(p.getCoin()); // coin
        if (b) {
            p.msgs.add(m);
        } else {
            p.addmsg(m);
        }
        m.cleanup();
    }

    public void update_assets_Box(boolean b) throws IOException {
        Message m = new Message(-32);
        m.writer().writeByte(3);
        m.writer().writeByte(6);
        //
        m.writer().writeLong(p.get_vang());
        m.writer().writeInt(p.get_ngoc());
        m.writer().writeInt((int) p.get_vnd()); // vnd
        m.writer().writeInt(0); // bua
        m.writer().writeInt((int) p.get_bua()); // diem nap
        m.writer().writeInt(p.getCoin()); // coin
        if (b) {
            p.msgs.add(m);
        } else {
            p.addmsg(m);
        }
        m.cleanup();
    }

    public static void readUpdateItem(DataOutputStream dos, Item_wear it, Player p)
            throws IOException {
        if (it == null || dos == null || it.template == null) return;
        ItemTemplate3 tpl = ItemTemplate3.get_it_by_id(it.template.id);
        if (tpl == null) {
            tpl = it.template;
        }
        if (it.option_item == null) it.option_item = new ArrayList<>();
        if (it.option_item_2 == null) it.option_item_2 = new ArrayList<>();
        if (it.mdakham == null) it.mdakham = new short[0];

        dos.writeShort(it.index);
        dos.writeUTF(tpl.name != null ? tpl.name : "");
        dos.writeByte(tpl.clazz);
        dos.writeByte(tpl.typeEquip);
        dos.writeShort(tpl.icon);
        dos.writeShort(tpl.level);
        dos.writeByte(it.levelUp);
        dos.writeByte(it.getColor());
        dos.writeByte(0);
        dos.writeByte(it.typelock);
        dos.writeByte(it.numHoleDaDuc);
        dos.writeInt(it.timeUse);
        dos.writeShort(it.valueChetac);
        dos.writeByte(it.isHoanMy);
        dos.writeByte(it.getSanitizedValueKichAn());
        //
        int typeEquip = it.template != null ? it.template.typeEquip : 0;
        boolean hasLevelUpBonus = !it.isThanTrang() && typeEquip < 6 && it.levelUp > 10;
        if (hasLevelUpBonus) {
            dos.writeByte(it.option_item.size() + 1);
        } else {
            dos.writeByte(it.option_item.size());
        }
        for (int i = 0; i < it.option_item.size(); i++) {
            template.Option op = it.option_item.get(i);
            if (op != null) {
                dos.writeByte(op.id);
                int val = op.getParam(typeEquip, it.levelUp, it.isHoanMy);
                if (val < 0) val = 0;
                if (val > 0xFFFF) val = 0xFFFF;
                dos.writeShort(val);
            } else {
                dos.writeByte(0);
                dos.writeShort(0);
            }
        }
        if (hasLevelUpBonus) {
            switch (typeEquip) {
                case 0: { // Vũ khí thường
                    dos.writeByte(46);
                    dos.writeShort((15 * (it.levelUp - 10)));
                    break;
                }
                case 2: { // Áo thường
                    dos.writeByte(53);
                    dos.writeShort((10 * (it.levelUp - 10)));
                    break;
                }
                case 1:
                case 3:
                case 5: { // Nón, Nhẫn, Giày thường
                    dos.writeByte(56);
                    dos.writeShort((30 * (it.levelUp - 10)));
                    break;
                }
                case 4: { // Dây chuyền thường
                    dos.writeByte(47);
                    dos.writeShort((10 * (it.levelUp - 10)));
                    break;
                }
            }
        }
        dos.writeByte(it.option_item_2.size());
        for (int i = 0; i < it.option_item_2.size(); i++) {
            template.Option op2 = it.option_item_2.get(i);
            if (op2 != null) {
                dos.writeByte(op2.id);
                int val2 = op2.getParam();
                if (val2 < 0) val2 = 0;
                if (val2 > 0xFFFF) val2 = 0xFFFF;
                dos.writeShort(val2);
            } else {
                dos.writeByte(0);
                dos.writeShort(0);
            }
        }
        dos.writeByte(it.numLoKham);
        dos.writeByte(it.mdakham.length);
        for (int i = 0; i < it.mdakham.length; i++) {
            dos.writeShort(it.mdakham[i]);
        }
    }

    public static void readUpdateItem(String jsdata, Item_wear it) {
        if (jsdata == null || jsdata.trim().isEmpty()) return;
        Object parsed = JSONValue.parse(jsdata);
        if (parsed instanceof JSONObject) {
            JSONObject js = (JSONObject) parsed;
            it.template = ItemTemplate3.get_it_by_id(js.containsKey("id") ? ((Long) js.get("id")).shortValue() : 0);
            it.levelUp = js.containsKey("lv") ? ((Long) js.get("lv")).byteValue() : 0;
            it.color = js.containsKey("color") ? ((Long) js.get("color")).byteValue() : (it.template != null ? it.template.color : 0);
            it.typelock = js.containsKey("lock") ? ((Long) js.get("lock")).byteValue() : 0;
            it.numHoleDaDuc = js.containsKey("holes") ? ((Long) js.get("holes")).byteValue() : 0;
            it.timeUse = js.containsKey("time") ? ((Long) js.get("time")).intValue() : 0;
            it.valueChetac = js.containsKey("craft") ? ((Long) js.get("craft")).shortValue() : 0;
            it.isHoanMy = js.containsKey("perf") ? ((Long) js.get("perf")).byteValue() : 0;
            it.valueKichAn = js.containsKey("hidden") ? ((Long) js.get("hidden")).byteValue() : -1;
            
            boolean isDial = (it.template != null && it.template.typeEquip == 7);
            it.option_item = new ArrayList<>();
            JSONArray js2 = (JSONArray) js.get("op1");
            if (js2 != null) {
                for (int i = 0; i < js2.size(); i++) {
                    Object elem = js2.get(i);
                    if (elem instanceof JSONObject) {
                        JSONObject o = (JSONObject) elem;
                        byte id = ((Long) o.get("id")).byteValue();
                        int value = ((Long) o.get("param")).intValue();
                        if (it.template != null && it.template.typeEquip < 6 && (id == 46 || id == 53 || id == 56 || id == 47)) {
                            continue;
                        }
                        addOrMergeOption(it.option_item, id, value, isDial);
                    } else if (elem instanceof JSONArray) {
                        JSONArray js_3 = (JSONArray) elem;
                        if (js_3.size() >= 2) {
                            byte id = Byte.parseByte(js_3.get(0).toString());
                            int value = Short.parseShort(js_3.get(1).toString());
                            if (it.template != null && it.template.typeEquip < 6 && (id == 46 || id == 53 || id == 56 || id == 47)) {
                                continue;
                            }
                            addOrMergeOption(it.option_item, id, value, isDial);
                        }
                    }
                }
            }
            
            it.option_item_2 = new ArrayList<>();
            JSONArray js4 = (JSONArray) js.get("op2");
            if (js4 != null) {
                for (int i = 0; i < js4.size(); i++) {
                    Object elem = js4.get(i);
                    if (elem instanceof JSONObject) {
                        JSONObject o = (JSONObject) elem;
                        addOrMergeOption(it.option_item_2, ((Long) o.get("id")).byteValue(),
                                ((Long) o.get("param")).intValue(), false);
                    } else if (elem instanceof JSONArray) {
                        JSONArray js_3 = (JSONArray) elem;
                        if (js_3.size() >= 2) {
                            addOrMergeOption(it.option_item_2, Byte.parseByte(js_3.get(0).toString()),
                                    Short.parseShort(js_3.get(1).toString()), false);
                        }
                    }
                }
            }
            
            it.numLoKham = js.containsKey("gems") ? ((Long) js.get("gems")).byteValue() : 0;
            JSONArray js5 = (JSONArray) js.get("gem_ids");
            if (js5 != null) {
                it.mdakham = new short[js5.size()];
                for (int i = 0; i < it.mdakham.length; i++) {
                    it.mdakham[i] = ((Long) js5.get(i)).shortValue();
                }
            } else {
                it.mdakham = new short[0];
            }
            it.index = js.containsKey("idx") ? ((Long) js.get("idx")).shortValue() : 0;
        } else if (parsed instanceof JSONArray) {
            JSONArray js = (JSONArray) parsed;
            it.template = ItemTemplate3.get_it_by_id(Short.parseShort(js.get(0).toString()));
            it.levelUp = Byte.parseByte(js.get(1).toString());
            it.color = (it.template != null ? it.template.color : 0);
            it.typelock = Byte.parseByte(js.get(2).toString());
            it.numHoleDaDuc = Byte.parseByte(js.get(3).toString());
            it.timeUse = Integer.parseInt(js.get(4).toString());
            it.valueChetac = Short.parseShort(js.get(5).toString());
            it.isHoanMy = Byte.parseByte(js.get(6).toString());
            it.valueKichAn = Byte.parseByte(js.get(7).toString());
            
            boolean isDial = (it.template != null && it.template.typeEquip == 7);
            it.option_item = new ArrayList<>();
            JSONArray js2 = (JSONArray) JSONValue.parse(js.get(8).toString());
            for (int i = 0; i < js2.size(); i++) {
                JSONArray js_3 = (JSONArray) JSONValue.parse(js2.get(i).toString());
                int a = Byte.parseByte(js_3.get(0).toString());
                if (it.template != null && it.template.typeEquip < 6 && (a == 46 || a == 53 || a == 56 || a == 47)) {
                    continue;
                }
                byte id = Byte.parseByte(js_3.get(0).toString());
                int value = Short.parseShort(js_3.get(1).toString());
                addOrMergeOption(it.option_item, id, value, isDial);
            }
            
            it.option_item_2 = new ArrayList<>();
            JSONArray js4 = (JSONArray) JSONValue.parse(js.get(9).toString());
            for (int i = 0; i < js4.size(); i++) {
                JSONArray js_3 = (JSONArray) JSONValue.parse(js4.get(i).toString());
                addOrMergeOption(it.option_item_2, Byte.parseByte(js_3.get(0).toString()),
                        Short.parseShort(js_3.get(1).toString()), false);
            }
            
            it.numLoKham = Byte.parseByte(js.get(10).toString());
            JSONArray js5 = (JSONArray) JSONValue.parse(js.get(11).toString());
            it.mdakham = new short[js5.size()];
            for (int i = 0; i < it.mdakham.length; i++) {
                it.mdakham[i] = Short.parseShort(js5.get(i).toString());
            }
            it.index = Short.parseShort(js.get(12).toString());
        }

        if (it != null && it.template != null) {
            if (it.template.typeEquip == 7) {
                UpgradeDial.sanitizeDialOptions(it);
            }
            if (it.isThanTrang()) {
                it.color = 8;
                it.numLoKham = 0;
                it.numHoleDaDuc = 0;
                it.mdakham = new short[0];
                it.isHoanMy = 0;
                it.valueKichAn = -1;
                it.valueChetac = 0;
            }
            if (!it.canHaveKichAn() || it.valueKichAn < 0 || it.valueKichAn > 12) {
                it.valueKichAn = -1;
            }
        }
    }

    private static void addOrMergeOption(List<Option> list, byte id, int value, boolean isDial) {
        if (list == null) return;
        if (isDial && id == 56) {
            id = 17; // Chuyển Máu cuối thành Tăng HP % cho Dial
        }
        for (Option op : list) {
            if (op != null && op.id == id) {
                op.setParam(Math.max(op.getParam(), value));
                return;
            }
        }
        list.add(new Option(id, value));
    }

    public boolean add_item_bag3(Item_wear it_add) {
        if (it_add == null || it_add.template == null || ItemTemplate3.get_it_by_id(it_add.template.id) == null) return false;
        if (able_bag() > 0) {
            for (int i = 0; i < bag3.length; i++) {
                if (bag3[i] == null) {
                    bag3[i] = it_add;
                    it_add.index = (short) i;
                    return true;
                }
            }
        }
        save_item_wear.add(it_add);
        while (save_item_wear.size() > 90) {
            save_item_wear.remove(0);
        }
        return false;
    }

    public boolean add_item_box3(Item_wear it_add) {
        if (it_add == null || it_add.template == null || ItemTemplate3.get_it_by_id(it_add.template.id) == null) return false;
        if (able_box() > 0) {
            for (int i = 0; i < box3.length; i++) {
                if (box3[i] == null) {
                    box3[i] = it_add;
                    it_add.index = (short) i;
                    return true;
                }
            }
        }
        return false;
    }

    public int able_bag() {
        return (max_bag & 0xFFFF) - this.quant_item_inbag(3) - this.quant_item_inbag(4)
                - this.quant_item_inbag(5) - this.quant_item_inbag(7);
    }

    public int able_box() {
        return (max_box & 0xFFFF) - this.quant_item_inbox(3) - this.quant_item_inbox(4)
                - this.quant_item_inbox(7);
    }

    private int quant_item_inbag(int type) {
        int par = 0;
        switch (type) {
            case 3: {
                for (int i = 0; i < bag3.length; i++) {
                    if (bag3[i] != null && bag3[i].template != null && ItemTemplate3.get_it_by_id(bag3[i].template.id) != null) {
                        par++;
                    }
                }
                break;
            }
            case 4:
            case 5:
            case 7: {
                for (int i = 0; i < bag47.size(); i++) {
                    ItemBag47 it = bag47.get(i);
                    if (it != null && it.category == type && it.quant > 0 && isItemExist(type, it.id)) {
                        par++;
                    }
                }
                break;
            }
        }
        return par;
    }

    private int quant_item_inbox(int type) {
        int par = 0;
        switch (type) {
            case 3: {
                for (int i = 0; i < box3.length; i++) {
                    if (box3[i] != null && box3[i].template != null && ItemTemplate3.get_it_by_id(box3[i].template.id) != null) {
                        par++;
                    }
                }
                break;
            }
            case 4:
            case 7: {
                for (int i = 0; i < box47.size(); i++) {
                    ItemBag47 it = box47.get(i);
                    if (it != null && it.category == type && it.quant > 0 && isItemExist(type, it.id)) {
                        par++;
                    }
                }
                break;
            }
        }
        return par;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject it_data_to_json(Item_wear it) {
        if (it == null || it.template == null || ItemTemplate3.get_it_by_id(it.template.id) == null) {
            return new JSONObject();
        }
        JSONObject js = new JSONObject();
        js.put("id", (int) it.template.id);
        js.put("lv", (int) it.levelUp);
        js.put("color", (int) it.getColor());
        js.put("lock", (int) it.typelock);
        js.put("holes", (int) it.numHoleDaDuc);
        js.put("time", it.timeUse);
        js.put("craft", (int) it.valueChetac);
        js.put("perf", (int) it.isHoanMy);
        js.put("hidden", (int) it.valueKichAn);
        
        JSONArray js_2 = new JSONArray();
        for (int i = 0; i < it.option_item.size(); i++) {
            JSONObject js_3 = new JSONObject();
            js_3.put("id", (int) it.option_item.get(i).id);
            js_3.put("param", it.option_item.get(i).getParam());
            js_2.add(js_3);
        }
        js.put("op1", js_2);
        
        JSONArray js_4 = new JSONArray();
        for (int i = 0; i < it.option_item_2.size(); i++) {
            JSONObject js_3 = new JSONObject();
            js_3.put("id", (int) it.option_item_2.get(i).id);
            js_3.put("param", (int) it.option_item_2.get(i).getParam());
            js_4.add(js_3);
        }
        js.put("op2", js_4);
        
        js.put("gems", (int) it.numLoKham);
        
        JSONArray js_5 = new JSONArray();
        for (int i = 0; i < it.mdakham.length; i++) {
            js_5.add((int) it.mdakham[i]);
        }
        js.put("gem_ids", js_5);
        js.put("idx", (int) it.index);
        return js;
    }

    public boolean add_item_bag47(int type, int id, int num) {
        if (num <= 0) return false;

        // Bảo vệ: EXP nhân vật (type 99)
        if (type == 99) {
            if (this.p != null) {
                try {
                    this.p.update_exp(num, false);
                } catch (Exception ignored) {}
            }
            return true;
        }

        // Bảo vệ: Các loại tiền tệ và tài nguyên hệ thống (type 4)
        if (type == 4) {
            if (id == 0) { // Beri
                if (this.p != null) {
                    this.p.update_vang(num);
                    try { this.p.updateMoney(); } catch (Exception ignored) {}
                }
                return true;
            } else if (id == 1) { // Ruby
                if (this.p != null) {
                    this.p.update_ngoc(num);
                    try { this.p.updateMoney(); } catch (Exception ignored) {}
                }
                return true;
            } else if (id == 2 || id == 908) { // Extol
                if (this.p != null) {
                    this.p.updateVnd(num);
                    try { this.p.updateMoney(); } catch (Exception ignored) {}
                }
                return true;
            } else if (id == 6) { // Vé
                if (this.p != null) {
                    this.p.update_ticket(num);
                    try { this.p.updateMoney(); } catch (Exception ignored) {}
                }
                return true;
            } else if (id == 333) { // EXP Skill
                if (this.p != null) {
                    try {
                        this.p.updateHk(num);
                    } catch (Exception ignored) {}
                }
                return true;
            } else if (id == -10) { // EXP Bang
                if (this.p != null && this.p.clan != null) {
                    this.p.clan.update_xp(num);
                }
                return true;
            } else if (id == -11) { // Beri Bang
                if (this.p != null && this.p.clan != null) {
                    this.p.clan.update_beri(num);
                }
                return true;
            } else if (id == -12) { // Ruby Bang
                if (this.p != null && this.p.clan != null) {
                    this.p.clan.update_ruby(num);
                }
                return true;
            }
        }

        if (!isItemExist(type, id)) {
            return false;
        }

        int remaining = num;

        // 1. Tự động lấp đầy các stack hiện có chưa đạt 9999
        for (int i = 0; i < bag47.size(); i++) {
            ItemBag47 it = bag47.get(i);
            if (it != null && it.category == type && it.id == id) {
                if (it.quant < DataTemplate.MAX_ITEM_IN_BAG) {
                    int space = DataTemplate.MAX_ITEM_IN_BAG - it.quant;
                    int toAdd = Math.min(remaining, space);
                    it.quant += toAdd;
                    remaining -= toAdd;
                    if (remaining <= 0) {
                        return true;
                    }
                }
            }
        }

        // 2. Nếu vẫn còn dư, tách phần thừa thành các stack mới (tối đa 9999 mỗi ô)
        while (remaining > 0 && able_bag() > 0) {
            int toAdd = Math.min(remaining, DataTemplate.MAX_ITEM_IN_BAG);
            ItemBag47 it_new = new ItemBag47();
            it_new.category = (byte) type;
            it_new.id = (short) id;
            it_new.quant = (short) toAdd;
            this.bag47.add(it_new);
            remaining -= toAdd;
        }

        if (remaining <= 0) {
            return true;
        }

        // 3. Nếu hành trang không còn ô trống -> Phần còn lại lưu an toàn vào save_item_47
        while (remaining > 0) {
            int toSave = Math.min(remaining, DataTemplate.MAX_ITEM_IN_BAG);
            ItemBag47 it_save = new ItemBag47();
            it_save.category = (byte) type;
            it_save.id = (short) id;
            it_save.quant = (short) toSave;
            save_item_47.add(it_save);
            while (save_item_47.size() > 90) {
                save_item_47.remove(0);
            }
            remaining -= toSave;
        }
        return false;
    }

    public boolean add_item_box47(int type, int id, int num) {
        if (num <= 0) {
            return false;
        }

        // Bảo vệ: EXP & Tiền tệ không thể lưu vào rương đồ
        if (type == 99) return false;
        if (type == 4 && (id == 0 || id == 1 || id == 2 || id == 908 || id == 6 || id == 333 || id == -10 || id == -11 || id == -12)) {
            return false;
        }

        if (!isItemExist(type, id)) {
            return false;
        }

        int remaining = num;

        // 1. Tự động lấp đầy các stack hiện có trong rương chưa đạt 9999
        for (int i = 0; i < box47.size(); i++) {
            ItemBag47 it = box47.get(i);
            if (it != null && it.category == type && it.id == id) {
                if (it.quant < DataTemplate.MAX_ITEM_IN_BAG) {
                    int space = DataTemplate.MAX_ITEM_IN_BAG - it.quant;
                    int toAdd = Math.min(remaining, space);
                    it.quant += toAdd;
                    remaining -= toAdd;
                    if (remaining <= 0) {
                        return true;
                    }
                }
            }
        }

        // 2. Tách phần thừa thành các stack mới trong rương đồ nếu còn ô trống
        while (remaining > 0 && able_box() > 0) {
            int toAdd = Math.min(remaining, DataTemplate.MAX_ITEM_IN_BAG);
            ItemBag47 it_new = new ItemBag47();
            it_new.category = (byte) type;
            it_new.id = (short) id;
            it_new.quant = (short) toAdd;
            this.box47.add(it_new);
            remaining -= toAdd;
        }

        return remaining == 0;
    }

    /**
     * Kiểm tra người chơi có đủ chỗ trống hành trang để chứa toàn bộ `num` item (type, id) không.
     */
    public boolean can_add_item_bag47(int type, int id, int num) {
        if (num <= 0) return true;
        if (type == 99) return true;
        if (type == 4 && (id == 0 || id == 1 || id == 2 || id == 908 || id == 6 || id == 333 || id == -10 || id == -11 || id == -12)) {
            return true;
        }
        if (!isItemExist(type, id)) {
            return false;
        }
        int availableInExisting = 0;
        for (int i = 0; i < bag47.size(); i++) {
            ItemBag47 it = bag47.get(i);
            if (it != null && it.category == type && it.id == id) {
                if (it.quant < DataTemplate.MAX_ITEM_IN_BAG) {
                    availableInExisting += (DataTemplate.MAX_ITEM_IN_BAG - it.quant);
                }
            }
        }
        if (num <= availableInExisting) {
            return true;
        }
        int remaining = num - availableInExisting;
        int slotsNeeded = (remaining + DataTemplate.MAX_ITEM_IN_BAG - 1) / DataTemplate.MAX_ITEM_IN_BAG;
        return able_bag() >= slotsNeeded;
    }

    /**
     * Kiểm tra rương đồ có đủ chỗ trống để chứa toàn bộ `num` item (type, id) không.
     */
    public boolean can_add_item_box47(int type, int id, int num) {
        if (num <= 0) return true;
        if (type == 99) return false;
        if (type == 4 && (id == 0 || id == 1 || id == 2 || id == 908 || id == 6 || id == 333 || id == -10 || id == -11 || id == -12)) {
            return false;
        }
        if (!isItemExist(type, id)) {
            return false;
        }
        int availableInExisting = 0;
        for (int i = 0; i < box47.size(); i++) {
            ItemBag47 it = box47.get(i);
            if (it != null && it.category == type && it.id == id) {
                if (it.quant < DataTemplate.MAX_ITEM_IN_BAG) {
                    availableInExisting += (DataTemplate.MAX_ITEM_IN_BAG - it.quant);
                }
            }
        }
        if (num <= availableInExisting) {
            return true;
        }
        int remaining = num - availableInExisting;
        int slotsNeeded = (remaining + DataTemplate.MAX_ITEM_IN_BAG - 1) / DataTemplate.MAX_ITEM_IN_BAG;
        return able_box() >= slotsNeeded;
    }

    /**
     * Lấy số lượng tối đa có thể thêm vào hành trang cho một loại item (kết hợp stack cũ + ô trống)
     */
    public int get_max_addable_bag47(int type, int id) {
        if (type == 99 || (type == 4 && (id == 0 || id == 1 || id == 2 || id == 908 || id == 6 || id == 333 || id == -10 || id == -11 || id == -12))) {
            return Integer.MAX_VALUE;
        }
        if (!isItemExist(type, id)) {
            return 0;
        }
        long totalAddable = 0;
        for (int i = 0; i < bag47.size(); i++) {
            ItemBag47 it = bag47.get(i);
            if (it != null && it.category == type && it.id == id) {
                if (it.quant < DataTemplate.MAX_ITEM_IN_BAG) {
                    totalAddable += (DataTemplate.MAX_ITEM_IN_BAG - it.quant);
                }
            }
        }
        int freeSlots = able_bag();
        if (freeSlots > 0) {
            totalAddable += (long) freeSlots * DataTemplate.MAX_ITEM_IN_BAG;
        }
        return (int) Math.min(Integer.MAX_VALUE, totalAddable);
    }

    /**
     * Lấy số lượng tối đa có thể thêm vào rương đồ cho một loại item (kết hợp stack cũ + ô trống)
     */
    public int get_max_addable_box47(int type, int id) {
        if (type == 99 || (type == 4 && (id == 0 || id == 1 || id == 2 || id == 908 || id == 6 || id == 333 || id == -10 || id == -11 || id == -12))) {
            return 0;
        }
        if (!isItemExist(type, id)) {
            return 0;
        }
        long totalAddable = 0;
        for (int i = 0; i < box47.size(); i++) {
            ItemBag47 it = box47.get(i);
            if (it != null && it.category == type && it.id == id) {
                if (it.quant < DataTemplate.MAX_ITEM_IN_BAG) {
                    totalAddable += (DataTemplate.MAX_ITEM_IN_BAG - it.quant);
                }
            }
        }
        int freeSlots = able_box();
        if (freeSlots > 0) {
            totalAddable += (long) freeSlots * DataTemplate.MAX_ITEM_IN_BAG;
        }
        return (int) Math.min(Integer.MAX_VALUE, totalAddable);
    }

    /**
     * Tự động gom (compact) các stack rời rạc của một loại vật phẩm trong hành trang thành các stack chuẩn x9999
     */
    public void compact_bag47(int type, int id) {
        int totalQuant = 0;
        int matchingCount = 0;
        for (int i = 0; i < bag47.size(); i++) {
            ItemBag47 it = bag47.get(i);
            if (it != null && it.category == type && it.id == id) {
                totalQuant += it.quant;
                matchingCount++;
            }
        }
        if (matchingCount <= 1 && totalQuant <= DataTemplate.MAX_ITEM_IN_BAG) {
            return;
        }
        for (int i = bag47.size() - 1; i >= 0; i--) {
            ItemBag47 it = bag47.get(i);
            if (it != null && it.category == type && it.id == id) {
                bag47.remove(i);
            }
        }
        while (totalQuant > 0) {
            int chunk = Math.min(totalQuant, DataTemplate.MAX_ITEM_IN_BAG);
            bag47.add(new ItemBag47(id, type, chunk));
            totalQuant -= chunk;
        }
    }

    /**
     * Tự động gom (compact) các stack rời rạc của một loại vật phẩm trong rương đồ thành các stack chuẩn x9999
     */
    public void compact_box47(int type, int id) {
        int totalQuant = 0;
        int matchingCount = 0;
        for (int i = 0; i < box47.size(); i++) {
            ItemBag47 it = box47.get(i);
            if (it != null && it.category == type && it.id == id) {
                totalQuant += it.quant;
                matchingCount++;
            }
        }
        if (matchingCount <= 1 && totalQuant <= DataTemplate.MAX_ITEM_IN_BAG) {
            return;
        }
        for (int i = box47.size() - 1; i >= 0; i--) {
            ItemBag47 it = box47.get(i);
            if (it != null && it.category == type && it.id == id) {
                box47.remove(i);
            }
        }
        while (totalQuant > 0) {
            int chunk = Math.min(totalQuant, DataTemplate.MAX_ITEM_IN_BAG);
            box47.add(new ItemBag47(id, type, chunk));
            totalQuant -= chunk;
        }
    }

    public int total_item_bag_by_id(int type, int id) {
        if (!isItemExist(type, id)) return 0;
        int par = 0;
        switch (type) {
            case 4:
            case 5:
            case 7: {
                for (int i = 0; i < bag47.size(); i++) {
                    ItemBag47 it = bag47.get(i);
                    if (it != null && it.category == type && it.id == id && it.quant > 0) {
                        par += it.quant;
                    }
                }
                break;
            }
        }
        return par;
    }

    public int total_item_box_by_id(int type, int id) {
        if (!isItemExist(type, id)) return 0;
        int par = 0;
        switch (type) {
            case 4:
            case 7: {
                for (int i = 0; i < box47.size(); i++) {
                    ItemBag47 it = box47.get(i);
                    if (it != null && it.category == type && it.id == id && it.quant > 0) {
                        par += it.quant;
                    }
                }
                break;
            }
        }
        return par;
    }

    public void remove_item47(int type, int id, int num) {
        if (num <= 0) return;
        int remaining = num;
        for (int i = bag47.size() - 1; i >= 0 && remaining > 0; i--) {
            ItemBag47 it = bag47.get(i);
            if (it != null && it.category == type && it.id == id) {
                if (it.quant <= remaining) {
                    remaining -= it.quant;
                    bag47.remove(i);
                } else {
                    it.quant -= remaining;
                    remaining = 0;
                    break;
                }
            }
        }
    }

    public void remove_item47_box(int type, int id, int num) {
        if (num <= 0) return;
        int remaining = num;
        for (int i = box47.size() - 1; i >= 0 && remaining > 0; i--) {
            ItemBag47 it = box47.get(i);
            if (it != null && it.category == type && it.id == id) {
                if (it.quant <= remaining) {
                    remaining -= it.quant;
                    box47.remove(i);
                } else {
                    it.quant -= remaining;
                    remaining = 0;
                    break;
                }
            }
        }
    }

    public void remove_item_wear(Item_wear item_wear) {
        for (int i = 0; i < bag3.length; i++) {
            if (bag3[i] != null && bag3[i].equals(item_wear)) {
                bag3[i] = null;
                break;
            }
        }
    }

    public void add_item_save(Item_wear item_wear) {
        if (item_wear != null && item_wear.template != null && ItemTemplate3.get_it_by_id(item_wear.template.id) != null) {
            save_item_wear.add(item_wear);
            while (save_item_wear.size() > 90) {
                save_item_wear.remove(0);
            }
        }
    }
}
