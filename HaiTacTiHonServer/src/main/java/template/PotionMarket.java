package template;

import activities.Market;
import org.json.simple.JSONArray;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PotionMarket {
    public int id_db;
    public int seller_id;
    public String seller = "";
    public int buyer_id = -1;
    public byte market_type;
    public short index;
    public short id;
    public byte category;
    public short quant;
    public long time_market;
    public int price_market;
    public byte type_market = 1;

    public boolean fromResultSet(ResultSet rs) throws SQLException {
        this.id_db = rs.getInt("id");
        this.seller_id = rs.getInt("seller_id");
        this.buyer_id = rs.getInt("buyer_id");
        this.market_type = rs.getByte("market_type");
        this.category = rs.getByte("category");
        this.id = rs.getShort("item_id");
        this.quant = rs.getShort("quantity");
        this.time_market = rs.getLong("time_market");
        this.price_market = rs.getInt("price_market");
        this.type_market = rs.getByte("type_market");
        this.index = Market.get_index();
        return true;
    }

    public void load_json(JSONArray js) {
        this.index = -1;
        this.id = Short.parseShort(js.get(0).toString());
        this.category = Byte.parseByte(js.get(1).toString());
        if (this.category == 4) {
            ItemTemplate4 itemTemplate4 = ItemTemplate4.get_it_by_id(id);
            if (itemTemplate4 != null || id == 0) { // id 0 = beri
                this.quant = Short.parseShort(js.get(2).toString());
                this.time_market = System.currentTimeMillis() + Long.parseLong(js.get(3).toString());
                this.price_market = Integer.parseInt(js.get(4).toString());
                String sellerVal = js.get(5).toString();
                try {
                    this.seller_id = Integer.parseInt(sellerVal);
                } catch (Exception e) {
                    this.seller_id = database.DbManager.getPlayerIdByName(sellerVal);
                }
                this.type_market = Byte.parseByte(js.get(6).toString());
                this.market_type = 6;
                this.index = Market.get_index();
            }
        } else if (this.category == 7) {
            ItemTemplate7 itemTemplate7 = ItemTemplate7.get_it_by_id(id);
            if (itemTemplate7 != null) {
                this.quant = Short.parseShort(js.get(2).toString());
                this.time_market = System.currentTimeMillis() + Long.parseLong(js.get(3).toString());
                this.price_market = Integer.parseInt(js.get(4).toString());
                String sellerVal = js.get(5).toString();
                try {
                    this.seller_id = Integer.parseInt(sellerVal);
                } catch (Exception e) {
                    this.seller_id = database.DbManager.getPlayerIdByName(sellerVal);
                }
                this.type_market = Byte.parseByte(js.get(6).toString());
                this.market_type = 5;
                this.index = Market.get_index();
            }
        }
    }
}
