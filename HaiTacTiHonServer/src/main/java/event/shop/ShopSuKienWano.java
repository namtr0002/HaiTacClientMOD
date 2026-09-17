package event.shop;

import event.Event;
import event.SuKienWano;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienWano.
 */
public class ShopSuKienWano extends EventShop {

    public ShopSuKienWano(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(600, 4, 1,                   idx++).setPriceBeri(10_000).setLimit(-1)); // Rượu Sake
        shopItems.add(new itemz.MainItemShop(SuKienWano.EVENT_ITEM_HOADANG, 4, 1,    idx++).setPriceBeri(10_000).setLimit(-1)); // Hoa Đăng Wano
        shopItems.add(new itemz.MainItemShop(610, 4, 1,                   idx++).setPriceRuby(10).setLimit(50));     // Cuộn Bí Kíp Samurai
        shopItems.add(new itemz.MainItemShop(359, 4, 1,                   idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi quà bằng Hoa Đăng Wano
        shopItems.add(new itemz.MainItemShop(609, 4, 1, 10, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 100)); // Rương Thần Thoại Wano
        shopItems.add(new itemz.MainItemShop(822, 4, 1, 5, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 50));   // Hộp trang phục Nami Wano
        shopItems.add(new itemz.MainItemShop(748, 4, 1, 5, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 50));   // Hộp trang phục Kaido
        shopItems.add(new itemz.MainItemShop(125, 105, 1, 1, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 150)); // Thời trang Nami Wano
        shopItems.add(new itemz.MainItemShop(122, 105, 1, 1, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 200)); // Trang phục Kaido Bách Thú
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 200));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 150));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKienWano.EVENT_ITEM_HOADANG, 25));    // Bột siêu cấp x5
    
    }
}
