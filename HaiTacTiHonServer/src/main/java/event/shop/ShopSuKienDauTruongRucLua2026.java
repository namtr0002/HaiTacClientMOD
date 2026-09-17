package event.shop;

import event.Event;
import event.SuKienDauTruongRucLua2026;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienDauTruongRucLua2026.
 */
public class ShopSuKienDauTruongRucLua2026 extends EventShop {

    public ShopSuKienDauTruongRucLua2026(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new MainItemShop(599, 4, 1,    idx++).setPriceBeri(10_000).setLimit(-1)); // Vé World Cup
        shopItems.add(new MainItemShop(600, 4, 1,    idx++).setPriceRuby(5).setLimit(50));      // Thẻ TT Trọng Tài
        shopItems.add(new MainItemShop(799, 4, 1,    idx++).setPriceRuby(10).setLimit(20));     // Quả Bóng Vàng
        shopItems.add(new MainItemShop(359, 4, 1,    idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi bằng Vé World Cup
        shopItems.add(new MainItemShop(887, 4, 1, 5, idx++, 4, 599, 50));  // Hộp Quà Euro 2024
        shopItems.add(new MainItemShop(805, 4, 1, 10, idx++, 4, 599, 50));  // Cúp vàng
        shopItems.add(new MainItemShop(798, 4, 1, 20, idx++, 4, 599, 30));  // Cúp bạc
        shopItems.add(new MainItemShop(137, 105, 1, 1, idx++, 4, 599, 150));  // Trang Phục Đội Tuyển Đức
        shopItems.add(new MainItemShop(138, 105, 1, 1, idx++, 4, 599, 150));  // Trang Phục Đội Tuyển Brazil
        shopItems.add(new MainItemShop(139, 105, 1, 1, idx++, 4, 599, 150));  // Trang Phục Đội Tuyển Pháp
        shopItems.add(new MainItemShop(98, 105, 1, 1, idx++, 4, 599, 150));   // Trang phục Euro 2024
        shopItems.add(new MainItemShop(121, 105, 1, 1, idx++, 4, 599, 150));  // Trọng tài worldcup
        shopItems.add(new MainItemShop(158, 4, 1, 100, idx++, 4, 599, 50));   // Rương đại ác quỷ
        shopItems.add(new MainItemShop(690, 4, 1, 5, idx++, 4, 599, 200));    // Rương TAQ Tự Chọn
        shopItems.add(new MainItemShop(823, 4, 1, 5, idx++, 4, 599, 150));    // Rương Dial Truyền Thuyết
        shopItems.add(new MainItemShop(754, 4, 1, 2, idx++, 4, 599, 250));    // Sách Haki Bá Vương
        shopItems.add(new MainItemShop(753, 4, 1, 2, idx++, 4, 599, 150));    // Sách Haki Vũ Trang
        shopItems.add(new MainItemShop(794, 4, 1, 5, idx++, 4, 599, 100));    // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new MainItemShop(866, 4, 1, 999, idx++, 4, 599, 20));   // Vé Vòng Quay Sự Kiện
        shopItems.add(new MainItemShop(122, 4, 1, 10, idx++, 4, 599, 30));    // Rương Cam Cùng Hệ Lv10
        shopItems.add(new MainItemShop(135, 4, 1, 50, idx++, 4, 599, 10));    // Tinh thể đá
        shopItems.add(new MainItemShop(1, 7, 10, 100, idx++, 4, 599, 5));     // Bột cường hóa x10
        shopItems.add(new MainItemShop(4, 7, 10, 100, idx++, 4, 599, 10));    // Bột vàng x10
        shopItems.add(new MainItemShop(18, 7, 5, 50, idx++, 4, 599, 25));     // Bột siêu cấp x5
    
    }
}
