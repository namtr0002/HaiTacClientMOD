package event.shop;

import event.Event;
import event.SuKienTet;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienTet.
 */
public class ShopSuKienTet extends EventShop {

    public ShopSuKienTet(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(SuKienTet.ITEM_BANH_CHUNG, 4, 1, idx++).setPriceBeri(10_000).setLimit(-1)); // Bánh Chưng
        shopItems.add(new itemz.MainItemShop(SuKienTet.ITEM_PHAO_HOA, 4, 1,   idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa
        shopItems.add(new itemz.MainItemShop(SuKienTet.ITEM_RUONG_NL, 4, 1,   idx++).setPriceRuby(2).setLimit(-1));      // Rương nguyên liệu tết
        shopItems.add(new itemz.MainItemShop(SuKienTet.ITEM_BAO_LIXI, 4, 1,   idx++).setPriceRuby(5).setLimit(50));      // Bao lì xì

        // 2. Đổi quà bằng Tiền Lì Xì (SuKienTet.ITEM_BAO_LIXI)
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 150));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 100));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 200));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(SuKienTet.ITEM_HOP_TRANG_PHUC_SO, 4, 1, 5, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 50));  // Hộp trang phục sơ
        shopItems.add(new itemz.MainItemShop(SuKienTet.ITEM_THOI_TRANG_30N, 4, 1, 2, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 100));   // Thời trang tết 30 ngày
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKienTet.ITEM_BAO_LIXI, 25));    // Bột siêu cấp x5
    
    }
}
