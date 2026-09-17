package event.shop;

import event.Event;
import event.SuKienHalloween;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienHalloween.
 */
public class ShopSuKienHalloween extends EventShop {

    public ShopSuKienHalloween(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(801, 4, 1, idx++).setPriceBeri(10_000).setLimit(-1)); // Vé Vòng Quay Thường
        shopItems.add(new itemz.MainItemShop(802, 4, 1, idx++).setPriceRuby(5).setLimit(-1));      // Vé Vòng Quay VIP
        shopItems.add(new itemz.MainItemShop(894, 4, 1, idx++).setPriceRuby(5).setLimit(50));      // Huy Hiệu Sói Ma
        shopItems.add(new itemz.MainItemShop(213, 4, 1, idx++).setPriceBeri(10_000).setLimit(-1)); // Hộp Quà Ma Quái
        shopItems.add(new itemz.MainItemShop(359, 4, 1, idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi quà bằng Bí Ngô Quỷ
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, 426, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(68, 105, 1, 1, idx++, 4, 426, 150));  // Thời trang Dracula
        shopItems.add(new itemz.MainItemShop(70, 105, 1, 1, idx++, 4, 426, 150));  // Thời trang Ma Sói
        shopItems.add(new itemz.MainItemShop(135, 105, 1, 1, idx++, 4, 426, 200)); // Bá Tước Bí Ngô
        shopItems.add(new itemz.MainItemShop(727, 4, 1, 2, idx++, 4, 426, 100));   // Trứng Đệ Tử
        shopItems.add(new itemz.MainItemShop(691, 4, 1, 10, idx++, 4, 426, 50));   // Rương Đá Siêu Cấp
        shopItems.add(new itemz.MainItemShop(732, 4, 1, 5, idx++, 4, 426, 100));   // Rương Đá Thần
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, 426, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, 426, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, 426, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, 426, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, 426, 25));    // Bột siêu cấp x5
    
    }
}
