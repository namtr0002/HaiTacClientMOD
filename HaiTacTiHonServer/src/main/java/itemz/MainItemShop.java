package itemz;

import template.Option;
import java.util.ArrayList;
import java.util.List;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.ItemFashion;
import model.Pet;
import template.DataTemplate;

public class MainItemShop extends MainItem {
    
    public static final byte BUY_BERI = 0;
    public static final byte BUY_RUBY = 1;
    public static final byte BUY_EXTOL = 2;
    public static final byte BUY_ITEM = 3;
    public static final byte BUY_MIXED = 4;
    
    public byte buyType = BUY_BERI; // Default
    public int priceBeri = 0;
    public int priceRuby = 0;
    public int priceExtol = 0;
    
    // Additional requirement items (materials, craft items, event tokens)
    public List<ReqItem> reqItems = new ArrayList<>();
    
    // ======================== CONSTRUCTORS ========================

    public MainItemShop(int id, int cat, int num, int limit, int indexShop, int[][] reqItems) {
        super(id, cat, num, 0, limit, indexShop);
        this.option1 = new ArrayList<>();
        if (reqItems != null) {
            addReqItems(reqItems);
        }
    }

    public MainItemShop(int id, int cat, int num, int limit, int indexShop, int[] reqItem) {
        super(id, cat, num, 0, limit, indexShop);
        this.option1 = new ArrayList<>();
        if (reqItem != null) {
            addReq(reqItem);
        }
    }

    public MainItemShop(int id, int cat, int num, int limit, int indexShop, int reqCat, int reqId, int reqNum) {
        super(id, cat, num, 0, limit, indexShop);
        this.option1 = new ArrayList<>();
        addReq(reqCat, reqId, reqNum);
    }

    public MainItemShop(int id, int cat, int num, int limit, int indexShop) {
        super(id, cat, num, 0, limit, indexShop);
        this.option1 = new ArrayList<>();
    }

    public MainItemShop(int id, int cat, int num, int indexShop, int[][] reqItems) {
        super(id, cat, num, 0, -1, indexShop);
        this.option1 = new ArrayList<>();
        if (reqItems != null) {
            addReqItems(reqItems);
        }
    }

    public MainItemShop(int id, int cat, int num, int indexShop, int[] reqItem) {
        super(id, cat, num, 0, -1, indexShop);
        this.option1 = new ArrayList<>();
        if (reqItem != null) {
            addReq(reqItem);
        }
    }

    public MainItemShop(int id, int cat, int num, int indexShop, int reqCat, int reqId, int reqNum) {
        super(id, cat, num, 0, -1, indexShop);
        this.option1 = new ArrayList<>();
        addReq(reqCat, reqId, reqNum);
    }

    public MainItemShop(int id, int cat, int num, int[][] reqItems) {
        super(id, cat, num, 0, -1, -1);
        this.option1 = new ArrayList<>();
        if (reqItems != null) {
            addReqItems(reqItems);
        }
    }

    public MainItemShop(int id, int cat, int num, int[] reqItem) {
        super(id, cat, num, 0, -1, -1);
        this.option1 = new ArrayList<>();
        if (reqItem != null) {
            addReq(reqItem);
        }
    }

    public MainItemShop(int id, int cat, int num, int reqCat, int reqId, int reqNum) {
        super(id, cat, num, 0, -1, -1);
        this.option1 = new ArrayList<>();
        addReq(reqCat, reqId, reqNum);
    }

    public MainItemShop(int id, int cat, int num, int indexShop) {
        super(id, cat, num, 0, -1, indexShop);
        this.option1 = new ArrayList<>();
    }

    public MainItemShop(int id, int cat, int num) {
        super(id, cat, num);
        this.option1 = new ArrayList<>();
    }

    public MainItemShop() {
        super(-1, -1, -1);
    }
    
    // ======================== PRICE & REQUIREMENT SETTERS ========================

    public MainItemShop setPriceBeri(int price) {
        if (price > 0) {
            this.priceBeri = price;
            if (this.priceRuby > 0 || this.priceExtol > 0 || !this.reqItems.isEmpty()) {
                this.buyType = BUY_MIXED;
            } else {
                this.buyType = BUY_BERI;
            }
        }
        return this;
    }

    public MainItemShop setPriceBeri(long price) {
        return setPriceBeri((int) Math.min(price, Integer.MAX_VALUE));
    }

    public MainItemShop setPriceRuby(int price) {
        if (price > 0) {
            this.priceRuby = price;
            if (this.priceBeri > 0 || this.priceExtol > 0 || !this.reqItems.isEmpty()) {
                this.buyType = BUY_MIXED;
            } else {
                this.buyType = BUY_RUBY;
            }
        }
        return this;
    }

    public MainItemShop setPriceExtol(int price) {
        if (price > 0) {
            this.priceExtol = price;
            if (this.priceBeri > 0 || this.priceRuby > 0 || !this.reqItems.isEmpty()) {
                this.buyType = BUY_MIXED;
            } else {
                this.buyType = BUY_EXTOL;
            }
        }
        return this;
    }
    
    /**
     * Thêm vật phẩm yêu cầu theo chuẩn: itemcat, itemid, quantity
     */
    public MainItemShop addReq(int cat, int id, int num) {
        if (num > 0) {
            reqItems.add(new ReqItem(cat, id, num));
            if (this.priceBeri > 0 || this.priceRuby > 0 || this.priceExtol > 0) {
                this.buyType = BUY_MIXED;
            } else {
                this.buyType = BUY_ITEM;
            }
        }
        return this;
    }

    /**
     * Thêm mảng 1 vật phẩm yêu cầu: {cat, id, num}
     */
    public MainItemShop addReq(int[] req) {
        if (req != null && req.length >= 3) {
            addReq(req[0], req[1], req[2]);
        }
        return this;
    }

    /**
     * Thêm nhiều vật phẩm yêu cầu: {{cat1, id1, num1}, {cat2, id2, num2}, ...}
     */
    public MainItemShop addReqItems(int[][] reqs) {
        if (reqs != null) {
            for (int[] req : reqs) {
                addReq(req);
            }
        }
        return this;
    }

    public MainItemShop setReqItems(int[][] reqs) {
        this.reqItems.clear();
        return addReqItems(reqs);
    }
    
    /**
     * Hỗ trợ tương thích ngược cú pháp cũ: (id, cat, num)
     */
    public MainItemShop addReqItem(int id, int cat, int num) {
        return addReq(cat, id, num);
    }

    public MainItemShop addReqItem(ReqItem req) {
        if (req != null) {
            reqItems.add(new ReqItem(req.cat, req.id, req.num));
            if (this.priceBeri > 0 || this.priceRuby > 0 || this.priceExtol > 0) {
                this.buyType = BUY_MIXED;
            } else {
                this.buyType = BUY_ITEM;
            }
        }
        return this;
    }

    public MainItemShop clearReqItems() {
        this.reqItems.clear();
        return this;
    }
    
    @Override
    public MainItemShop setLimit(int limit) {
        this.limitNumBuy = limit;
        return this;
    }

    @Override
    public MainItemShop setIndexShop(int index) {
        this.indexShop = index;
        return this;
    }

    @Override
    public MainItemShop setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public MainItemShop setIcon(short icon) {
        this.idIcon = icon;
        return this;
    }

    @Override
    public MainItemShop setIcon(int icon) {
        this.idIcon = (short) icon;
        return this;
    }

    @Override
    public MainItemShop setInfo(String info) {
        this.info = info;
        return this;
    }

    @Override
    public MainItemShop setNum(int num) {
        this.num = num;
        return this;
    }

    @Override
    public MainItemShop setPrice(int price) {
        this.price = price;
        return this;
    }
    
    @Override
    public MainItemShop clearOptions() {
        if (this.option1 != null) {
            this.option1.clear();
        } else {
            this.option1 = new ArrayList<>();
        }
        if (this.option2 != null) {
            this.option2.clear();
        }
        return this;
    }

    @Override
    public MainItemShop addOption(int opId, int param) {
        if (this.option1 == null) {
            this.option1 = new ArrayList<>();
        }
        this.option1.add(new Option(opId, param));
        return this;
    }

    @Override
    public MainItemShop addOption(Option op) {
        if (op != null) {
            if (this.option1 == null) {
                this.option1 = new ArrayList<>();
            }
            this.option1.add(new Option(op.id, op.getParam()));
        }
        return this;
    }

    @Override
    public MainItemShop addOption2(int opId, int param) {
        if (this.option2 == null) {
            this.option2 = new ArrayList<>();
        }
        this.option2.add(new Option(opId, param));
        return this;
    }

    @Override
    public MainItemShop setExpireDay(int day) {
        this.limitDay = day;
        this.time = (long) day * 86400000L;
        return this;
    }

    @Override
    public MainItemShop setTime(long millis) {
        this.time = millis;
        return this;
    }

    @Override
    public MainItemShop setPermanent() {
        this.time = -1;
        this.limitDay = 0;
        return this;
    }

    @Override
    public MainItemShop cloneItem() {
        MainItemShop clone = new MainItemShop(this.id, this.cat, this.num, this.indexShop);
        clone.buyType = this.buyType;
        clone.priceBeri = this.priceBeri;
        clone.priceRuby = this.priceRuby;
        clone.priceExtol = this.priceExtol;
        clone.price = this.price;
        clone.limitNumBuy = this.limitNumBuy;
        clone.name = this.name;
        clone.idIcon = this.idIcon;
        clone.info = this.info;
        clone.time = this.time;
        clone.limitDay = this.limitDay;
        
        for (ReqItem req : this.reqItems) {
            clone.reqItems.add(new ReqItem(req.cat, req.id, req.num));
        }
        if (this.option1 != null) {
            clone.option1 = new ArrayList<>();
            for (Option op : this.option1) {
                clone.option1.add(new Option(op.id, op.getParam()));
            }
        }
        if (this.option2 != null) {
            clone.option2 = new ArrayList<>();
            for (Option op : this.option2) {
                clone.option2.add(new Option(op.id, op.getParam()));
            }
        }
        return clone;
    }
    
    // ======================== TEMPLATE NAME RESOLUTION ========================

    public static String getItemName(int cat, int id) {
        String resName = "";
        try {
            switch (cat) {
                case 3: {
                    ItemTemplate3 it3 = ItemTemplate3.get_it_by_id(id);
                    if (it3 != null && it3.name != null && !it3.name.isEmpty()) {
                        resName = it3.name;
                    }
                    break;
                }
                case 4: {
                    ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(id);
                    if (it4 != null && it4.name != null && !it4.name.isEmpty()) {
                        resName = it4.name;
                    } else {
                        resName = ItemTemplate4.get_item_name(id);
                    }
                    break;
                }
                case 7: {
                    ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(id);
                    if (it7 != null && it7.name != null && !it7.name.isEmpty()) {
                        resName = it7.name;
                    } else {
                        resName = ItemTemplate7.get_item_name(id);
                    }
                    break;
                }
                case 105: {
                    ItemFashion itf = ItemFashion.get_item(id);
                    if (itf != null && itf.name != null && !itf.name.isEmpty()) {
                        resName = itf.name;
                    }
                    break;
                }
                case 110: {
                    Pet pet = Pet.getTemplate(id);
                    if (pet != null && pet.name != null && !pet.name.isEmpty()) {
                        resName = pet.name;
                    }
                    break;
                }
            }
        } catch (Exception ignored) {}
        
        if (resName == null || resName.isEmpty()) {
            if (cat == 3) resName = "Trang bị " + id;
            else if (cat == 105) resName = "Thời trang " + id;
            else if (cat == 110) resName = "Thú cưỡi " + id;
            else resName = "Vật phẩm " + id;
        }
        return resName;
    }
    
    // ======================== REQ ITEM CLASS ========================

    public static class ReqItem {
        public byte cat;
        public int id;
        public int num;
        public String name;

        public ReqItem(int cat, int id, int num) {
            this.cat = (byte) cat;
            this.id = id;
            this.num = num;
            this.name = getItemName(this.cat, this.id);
        }

        public ReqItem(int id, int cat, int num, boolean idFirst) {
            this.cat = (byte) cat;
            this.id = id;
            this.num = num;
            this.name = getItemName(this.cat, this.id);
        }

        public String getName() {
            if (this.name == null || this.name.isEmpty()) {
                this.name = getItemName(this.cat, this.id);
            }
            return this.name;
        }
    }
}
