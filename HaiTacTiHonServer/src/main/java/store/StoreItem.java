package store;

/**
 * Đại diện cho một dòng dữ liệu trong bảng SQL `store_data`.
 *
 * Mapping store_id -> Shop:
 *   0  = EquipShop (Kiếm Sĩ),  category=3
 *   1  = EquipShop (Hoa Tiêu), category=3
 *   2  = EquipShop (Xạ Thủ),   category=3
 *   3  = EquipShop (Đầu Bếp),  category=3
 *   4  = EquipShop (Chiến Sĩ), category=3
 *   5  = MaterialShop,          category=7
 *   6  = PotionShop,            category=4
 *   7  = StoneShop,             category=4
 */
public class StoreItem {

    /** PK trong bảng store_data */
    public final int id;

    /** ID shop (xem mapping bên trên) */
    public final int storeId;

    /** ID item trong template tương ứng (item3/item4/item7) */
    public final int itemId;

    /** Category của item: 3=trang bị, 4=tiêu hao, 7=nguyên liệu */
    public final int itemCategory;

    /** Giá beri (đồng vàng) */
    public final int priceCoin;

    /** Giá ruby (ngọc) */
    public final int priceRuby;

    /**
     * Điều kiện cấp độ (phụ thuộc vào từng Shop):
     *   EquipShop  : level/10 group
     *   PotionShop : -1=mọi cấp, hoặc level/10 group cụ thể
     *   StoneShop  : không dùng (luôn -1)
     */
    public final int reqLevel;

    /**
     * ID sự kiện yêu cầu:
     *   0  = luôn hiển thị
     *   10 = sự kiện Noel
     *   >0 = eventId bằng Manager.ZEVENT_ID
     */
    public final int eventId;

    /** ID phụ dùng để fake hiển thị item trong shop (khi type2 > -1 thì gửi type2 cho client hiển thị, nhưng nhận item gốc là itemId). -1 = không dùng. */
    public final int type2;

    public StoreItem(int id, int storeId, int itemId, int itemCategory,
                     int priceCoin, int priceRuby, int reqLevel, int eventId, int type2) {
        this.id           = id;
        this.storeId      = storeId;
        this.itemId       = itemId;
        this.itemCategory = itemCategory;
        this.priceCoin    = priceCoin;
        this.priceRuby    = priceRuby;
        this.reqLevel     = reqLevel;
        this.eventId      = eventId;
        this.type2        = type2;
    }
}
