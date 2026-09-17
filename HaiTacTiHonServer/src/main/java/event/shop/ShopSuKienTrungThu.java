package event.shop;

import event.Event;
import event.SuKienTrungThu;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienTrungThu.
 */
public class ShopSuKienTrungThu extends EventShop {

    public ShopSuKienTrungThu(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(SuKienTrungThu.ITEM_NANG_TRE, 4, 1,    idx++).setPriceBeri(10_000).setLimit(-1)); // Nang Tre
        shopItems.add(new itemz.MainItemShop(SuKienTrungThu.ITEM_DAY_KEM, 4, 1,     idx++).setPriceBeri(10_000).setLimit(-1)); // Dây Kẽm
        shopItems.add(new itemz.MainItemShop(SuKienTrungThu.ITEM_GIAY_MAU, 4, 1,    idx++).setPriceBeri(10_000).setLimit(-1)); // Giấy Màu
        shopItems.add(new itemz.MainItemShop(892, 4, 1,              idx++).setPriceRuby(2).setLimit(50));      // Đèn Tròn
        shopItems.add(new itemz.MainItemShop(893, 4, 1,              idx++).setPriceRuby(5).setLimit(50));      // Đèn Kéo Quân
        shopItems.add(new itemz.MainItemShop(359, 4, 1,              idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi quà bằng Lồng Đèn
        shopItems.add(new itemz.MainItemShop(65, 105, 1, 1, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 150));  // Thời trang Chú Cuội
        shopItems.add(new itemz.MainItemShop(66, 105, 1, 1, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 150));  // Thời trang Chị Hằng
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 200));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 150));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKienTrungThu.ITEM_LONG_DEN, 25));    // Bột siêu cấp x5
    
    }
}
