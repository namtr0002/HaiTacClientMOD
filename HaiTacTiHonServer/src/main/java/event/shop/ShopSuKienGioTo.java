package event.shop;

import event.Event;
import event.SuKienGioTo;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienGioTo.
 */
public class ShopSuKienGioTo extends EventShop {

    public ShopSuKienGioTo(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(332, 4, 1, idx++).setPriceBeri(5_000).setLimit(-1));  // Thức ăn Lân
        shopItems.add(new itemz.MainItemShop(839, 4, 1, idx++).setPriceRuby(10).setLimit(-1));   // Vé triệu hồi Lân
        shopItems.add(new itemz.MainItemShop(172, 4, 1, idx++).setPriceRuby(20).setLimit(50));   // Rương Vua Hùng
        shopItems.add(new itemz.MainItemShop(359, 4, 1, idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi bằng Mâm Sính Lễ
        shopItems.add(new itemz.MainItemShop(839, 4, 1, 9999, idx++, 4, 833, 20)); // Vé triệu hồi Lân (Đổi bằng Mâm)
        shopItems.add(new itemz.MainItemShop(837, 4, 1, 50, idx++, 4, 833, 50));   // Mâm Bạc
        shopItems.add(new itemz.MainItemShop(838, 4, 1, 30, idx++, 4, 833, 100));  // Mâm Vàng
        shopItems.add(new itemz.MainItemShop(884, 4, 1, 50, idx++, 4, 833, 50));   // Huy Hiệu Hùng Vương
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, 833, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, 833, 200));   // Rương trái ác quỷ tự chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, 833, 150));   // Rương dial truyền thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, 833, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, 833, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, 833, 100));   // Bảo hiểm chuyển hóa siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, 833, 20));  // Vé vòng quay sự kiện
        shopItems.add(new itemz.MainItemShop(82, 105, 1, 1, idx++, 4, 833, 200));  // Thời trang Hùng Vương Nam
        shopItems.add(new itemz.MainItemShop(83, 105, 1, 1, idx++, 4, 833, 200));  // Thời trang Hùng Vương Nữ
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, 833, 30));   // Rương Cam Cùng Hệ
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, 833, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, 833, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, 833, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, 833, 25));    // Bột siêu cấp x5
        shopItems.add(new itemz.MainItemShop(10, 7, 1, 50, idx++, 4, 833, 20));    // Khiên
    
    }
}
