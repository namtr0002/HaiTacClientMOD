package event.shop;

import event.Event;
import event.SuKienTetThieuNhi;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienTetThieuNhi.
 */
public class ShopSuKienTetThieuNhi extends EventShop {

    public ShopSuKienTetThieuNhi(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(SuKienTetThieuNhi.ITEM_BANH_QUI_BO, 4, 1,   idx++).setPriceBeri(10_000).setLimit(-1)); // Bánh Qui Bơ
        shopItems.add(new itemz.MainItemShop(SuKienTetThieuNhi.ITEM_KEO_DEO_GAU, 4, 1,   idx++).setPriceBeri(10_000).setLimit(-1)); // Kẹo Dẻo Gấu
        shopItems.add(new itemz.MainItemShop(SuKienTetThieuNhi.ITEM_KEO_BONG_GON, 4, 1,  idx++).setPriceRuby(5).setLimit(50));      // Kẹo Bông Gòn VIP
        shopItems.add(new itemz.MainItemShop(SuKienTetThieuNhi.ITEM_BONG_BONG, 4, 1,     idx++).setPriceRuby(5).setLimit(50));      // Bong Bóng Tuổi Thơ
        shopItems.add(new itemz.MainItemShop(359, 4, 1,                idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi bằng Bánh qui
        shopItems.add(new itemz.MainItemShop(787, 4, 1, 1, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 50));  // Danh Hiệu Thiếu Nhi
        shopItems.add(new itemz.MainItemShop(790, 4, 1, 5, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 50));  // Hộp trang phục Squid Game
        shopItems.add(new itemz.MainItemShop(99, 105, 1, 1, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 150));  // Thời trang Squid Tam Giác
        shopItems.add(new itemz.MainItemShop(100, 105, 1, 1, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 150)); // Thời trang Squid Tròn
        shopItems.add(new itemz.MainItemShop(101, 105, 1, 1, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 150)); // Thời trang Squid Vuông
        shopItems.add(new itemz.MainItemShop(102, 105, 1, 1, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 150)); // Trang phục Chopper khổng lồ
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 200));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 150));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKienTetThieuNhi.ITEM_BANH_QUI, 25));    // Bột siêu cấp x5
    
    }
}
