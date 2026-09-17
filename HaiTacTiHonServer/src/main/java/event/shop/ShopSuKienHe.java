package event.shop;

import event.Event;
import event.SuKienHe;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienHe.
 */
public class ShopSuKienHe extends EventShop {

    public ShopSuKienHe(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(180, 4, 10, idx++).setPriceBeri(50_000).setLimit(-1)); // Bóng thường x10
        shopItems.add(new itemz.MainItemShop(181, 4, 5,  idx++).setPriceBeri(50_000).setLimit(-1)); // Bóng hiếm x5
        shopItems.add(new itemz.MainItemShop(182, 4, 5,  idx++).setPriceRuby(10).setLimit(-1));    // Bóng đặc biệt x5
        shopItems.add(new itemz.MainItemShop(190, 4, 1,  idx++).setPriceRuby(50).setLimit(20));    // Bộ vật phẩm Pokemon
        shopItems.add(new itemz.MainItemShop(191, 4, 1,  idx++).setPriceRuby(5).setLimit(50));     // Vé triệu hồi Pokemon
        shopItems.add(new itemz.MainItemShop(359, 4, 1,  idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi quà bằng Vé Tuổi Thơ
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, 192, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, 192, 150));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, 192, 100));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, 192, 200));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, 192, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(684, 4, 1, 2, idx++, 4, 192, 150));   // Sách Haki Quan Sát
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, 192, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, 192, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(589, 4, 1, 2, idx++, 4, 192, 50));    // Hộp trang phục Raid Suit
        shopItems.add(new itemz.MainItemShop(821, 4, 1, 2, idx++, 4, 192, 50));    // Hộp trang phục Boa Hancock
        shopItems.add(new itemz.MainItemShop(822, 4, 1, 2, idx++, 4, 192, 50));    // Hộp trang phục Nami Wano
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, 192, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, 192, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, 192, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, 192, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, 192, 25));    // Bột siêu cấp x5
        shopItems.add(new itemz.MainItemShop(10, 7, 1, 50, idx++, 4, 192, 20));    // Khiên
    
    }
}
