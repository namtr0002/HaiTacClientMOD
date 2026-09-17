package event.shop;

import event.Event;
import event.SuKienNoel;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienNoel.
 */
public class ShopSuKienNoel extends EventShop {

    public ShopSuKienNoel(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(SuKienNoel.ITEM_KEO_GIANG_SINH, 4, 1, idx++).setPriceBeri(10_000).setLimit(-1)); // Kẹo Giáng Sinh
        shopItems.add(new itemz.MainItemShop(168, 4, 1,                 idx++).setPriceRuby(5).setLimit(50));      // Vé Triệu Hồi Quái Tuyết
        shopItems.add(new itemz.MainItemShop(789, 4, 1,                 idx++).setPriceRuby(10).setLimit(20));     // Cây Thông Noel
        shopItems.add(new itemz.MainItemShop(359, 4, 1,                 idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi quà bằng Vé Noel
        shopItems.add(new itemz.MainItemShop(227, 4, 1, 999, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 10));  // Hộp quà Noel
        shopItems.add(new itemz.MainItemShop(789, 4, 1, 999, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 10));  // Cây Thông Noel
        shopItems.add(new itemz.MainItemShop(791, 4, 1, 5, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 50));    // Hộp Trang Phục Noel
        shopItems.add(new itemz.MainItemShop(895, 4, 1, 50, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 30));   // Mảnh Ghép Pet Chopper
        shopItems.add(new itemz.MainItemShop(896, 4, 1, 100, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 10));  // Hoa Tuyết
        shopItems.add(new itemz.MainItemShop(897, 4, 1, 50, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 20));   // Nón Giáng Sinh
        shopItems.add(new itemz.MainItemShop(45, 105, 1, 1, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 150));  // Thời Trang Noel Nữ
        shopItems.add(new itemz.MainItemShop(75, 105, 1, 1, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 150));  // Hoàng Tử Tuyết
        shopItems.add(new itemz.MainItemShop(76, 105, 1, 1, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 150));  // Công Chúa Tuyết
        shopItems.add(new itemz.MainItemShop(141, 105, 1, 1, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 150)); // Tuần Lộc Đáng Yêu
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 200));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 150));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKienNoel.ITEM_VE_NOEL, 25));    // Bột siêu cấp x5
    
    }
}
