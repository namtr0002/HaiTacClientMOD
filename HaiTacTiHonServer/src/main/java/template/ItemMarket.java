package template;

import activities.Market;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemMarket {
    public int id_db;
    public int seller_id;
    public String seller = "";
    public int buyer_id = -1;
    public byte market_type;
    public ItemTemplate3 template;
    public byte levelUp;
    public byte typelock;
    public byte color;
    public byte numHoleDaDuc;
    public int timeUse;
    public short valueChetac;
    public byte isHoanMy;
    public byte valueKichAn = -1;
    public List<Option> option_item = new ArrayList<>();
    public List<Option> option_item_2 = new ArrayList<>();
    public byte numLoKham;
    public short[] mdakham = new short[0];
    public short index;
    public long time_market;
    public int price_market;
    public byte type_market = 1;

    public byte getColor() {
        if (this.color > 0) return this.color;
        if (this.template != null) return this.template.color;
        return 0;
    }

    public boolean isThanTrang() {
        return this.template != null && this.template.isThanTrang();
    }

    public boolean canHaveKichAn() {
        if (this.template == null) return false;
        if (this.template.typeEquip >= 6) return false;
        if (this.template.isThanTrang()) return false;
        if (this.getColor() < 2) return false;
        return true;
    }

    public byte getSanitizedValueKichAn() {
        if (!canHaveKichAn()) {
            return -1;
        }
        if (this.valueKichAn < 0 || this.valueKichAn > 12) {
            return -1;
        }
        return this.valueKichAn;
    }

    public void clone_from_item_wear(Item_wear it_temp) {
        this.index = -1;
        if (it_temp != null && it_temp.template != null) {
            this.template = it_temp.template;
            this.levelUp = it_temp.levelUp;
            this.color = it_temp.getColor();
            this.typelock = it_temp.typelock;
            this.numHoleDaDuc = it_temp.numHoleDaDuc;
            this.timeUse = it_temp.timeUse;
            this.valueChetac = it_temp.valueChetac;
            this.isHoanMy = it_temp.isHoanMy;
            this.valueKichAn = it_temp.getSanitizedValueKichAn();
            this.option_item = new ArrayList<>();
            if (it_temp.option_item != null) {
                for (int i = 0; i < it_temp.option_item.size(); i++) {
                    this.option_item.add(new Option(it_temp.option_item.get(i).id,
                            it_temp.option_item.get(i).getParam()));
                }
            }
            this.option_item_2 = new ArrayList<>();
            if (it_temp.option_item_2 != null) {
                for (int i = 0; i < it_temp.option_item_2.size(); i++) {
                    this.option_item_2.add(new Option(it_temp.option_item_2.get(i).id,
                            it_temp.option_item_2.get(i).getParam()));
                }
            }
            this.numLoKham = it_temp.numLoKham;
            if (it_temp.mdakham != null) {
                this.mdakham = new short[it_temp.mdakham.length];
                System.arraycopy(it_temp.mdakham, 0, this.mdakham, 0, it_temp.mdakham.length);
            } else {
                this.mdakham = new short[0];
            }
            this.type_market = 1;
            this.index = Market.get_index();
        }
    }

    public boolean fromResultSet(ResultSet rs) throws SQLException {
        this.id_db = rs.getInt("id");
        this.seller_id = rs.getInt("seller_id");
        this.buyer_id = rs.getInt("buyer_id");
        this.market_type = rs.getByte("market_type");
        int itemId = rs.getInt("item_id");
        this.template = ItemTemplate3.get_it_by_id(itemId);
        if (this.template == null) {
            return false;
        }
        this.levelUp = rs.getByte("level_up");
        this.typelock = rs.getByte("type_lock");
        this.numHoleDaDuc = rs.getByte("num_hole_da_duc");
        this.timeUse = rs.getInt("time_use");
        this.valueChetac = rs.getShort("value_chetac");
        this.isHoanMy = rs.getByte("is_hoan_my");
        this.valueKichAn = rs.getByte("value_kich_an");
        this.valueKichAn = this.getSanitizedValueKichAn();
        
        this.option_item = new ArrayList<>();
        String op1Str = rs.getString("option_item");
        if (op1Str != null && !op1Str.trim().isEmpty() && !op1Str.equals("[]")) {
            try {
                JSONArray jsOp1 = (JSONArray) JSONValue.parse(op1Str);
                if (jsOp1 != null) {
                    for (int i = 0; i < jsOp1.size(); i++) {
                        JSONArray item = (JSONArray) jsOp1.get(i);
                        this.option_item.add(new Option(Byte.parseByte(item.get(0).toString()),
                                Integer.parseInt(item.get(1).toString())));
                    }
                }
            } catch (Exception ignored) {}
        }
        
        this.option_item_2 = new ArrayList<>();
        String op2Str = rs.getString("option_item_2");
        if (op2Str != null && !op2Str.trim().isEmpty() && !op2Str.equals("[]")) {
            try {
                JSONArray jsOp2 = (JSONArray) JSONValue.parse(op2Str);
                if (jsOp2 != null) {
                    for (int i = 0; i < jsOp2.size(); i++) {
                        JSONArray item = (JSONArray) jsOp2.get(i);
                        this.option_item_2.add(new Option(Byte.parseByte(item.get(0).toString()),
                                Integer.parseInt(item.get(1).toString())));
                    }
                }
            } catch (Exception ignored) {}
        }
        
        this.numLoKham = rs.getByte("num_lo_kham");
        String mdaStr = rs.getString("mdakham");
        if (mdaStr != null && !mdaStr.trim().isEmpty() && !mdaStr.equals("[]")) {
            try {
                JSONArray jsMda = (JSONArray) JSONValue.parse(mdaStr);
                if (jsMda != null) {
                    this.mdakham = new short[jsMda.size()];
                    for (int i = 0; i < jsMda.size(); i++) {
                        this.mdakham[i] = Short.parseShort(jsMda.get(i).toString());
                    }
                } else {
                    this.mdakham = new short[0];
                }
            } catch (Exception e) {
                this.mdakham = new short[0];
            }
        } else {
            this.mdakham = new short[0];
        }

        this.time_market = rs.getLong("time_market");
        this.price_market = rs.getInt("price_market");
        this.type_market = rs.getByte("type_market");
        this.index = Market.get_index();
        return true;
    }

    @SuppressWarnings("unchecked")
    public String getOptionItemJson() {
        JSONArray js = new JSONArray();
        if (this.option_item != null) {
            for (Option op : this.option_item) {
                JSONArray item = new JSONArray();
                item.add(op.id);
                item.add(op.getParam());
                js.add(item);
            }
        }
        return js.toJSONString();
    }

    @SuppressWarnings("unchecked")
    public String getOptionItem2Json() {
        JSONArray js = new JSONArray();
        if (this.option_item_2 != null) {
            for (Option op : this.option_item_2) {
                JSONArray item = new JSONArray();
                item.add(op.id);
                item.add(op.getParam());
                js.add(item);
            }
        }
        return js.toJSONString();
    }

    @SuppressWarnings("unchecked")
    public String getMdakhamJson() {
        JSONArray js = new JSONArray();
        if (this.mdakham != null) {
            for (short s : this.mdakham) {
                js.add(s);
            }
        }
        return js.toJSONString();
    }

    public void load_json(JSONArray js) {
        this.index = -1;
        ItemTemplate3 itTemplate = ItemTemplate3.get_it_by_id(Integer.parseInt(js.get(0).toString()));
        if (itTemplate != null) {
            this.template = itTemplate;
            this.levelUp = Byte.parseByte(js.get(1).toString());
            this.typelock = Byte.parseByte(js.get(2).toString());
            this.numHoleDaDuc = Byte.parseByte(js.get(3).toString());
            this.timeUse = Integer.parseInt(js.get(4).toString());
            this.valueChetac = Short.parseShort(js.get(5).toString());
            this.isHoanMy = Byte.parseByte(js.get(6).toString());
            this.valueKichAn = Byte.parseByte(js.get(7).toString());
            this.valueKichAn = this.getSanitizedValueKichAn();
            this.option_item = new ArrayList<>();
            JSONArray js2 = (JSONArray) js.get(8);
            for (int i = 0; i < js2.size(); i++) {
                JSONArray js3 = (JSONArray) js2.get(i);
                this.option_item.add(new Option(Byte.parseByte(js3.get(0).toString()),
                        Integer.parseInt(js3.get(1).toString())));
            }
            js2.clear();
            this.option_item_2 = new ArrayList<>();
            js2 = (JSONArray) js.get(9);
            for (int i = 0; i < js2.size(); i++) {
                JSONArray js3 = (JSONArray) js2.get(i);
                this.option_item_2.add(new Option(Byte.parseByte(js3.get(0).toString()),
                        Integer.parseInt(js3.get(1).toString())));
            }
            js2.clear();
            this.numLoKham = Byte.parseByte(js.get(10).toString());
            js2 = (JSONArray) js.get(11);
            this.mdakham = new short[js2.size()];
            for (int i = 0; i < this.mdakham.length; i++) {
                this.mdakham[i] = Short.parseShort(js2.get(i).toString());
            }
            this.time_market = System.currentTimeMillis() + Long.parseLong(js.get(12).toString());
            this.price_market = Integer.parseInt(js.get(13).toString());
            String sellerVal = js.get(14).toString();
            try {
                this.seller_id = Integer.parseInt(sellerVal);
            } catch (Exception e) {
                this.seller_id = database.DbManager.getPlayerIdByName(sellerVal);
            }
            this.type_market = Byte.parseByte(js.get(15).toString());
            this.index = Market.get_index();
        }
    }
}
