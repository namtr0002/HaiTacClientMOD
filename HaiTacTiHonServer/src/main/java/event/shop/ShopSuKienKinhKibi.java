package event.shop;

import event.Event;
import event.SuKienKinhKibi;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienKinhKibi.
 */
public class ShopSuKienKinhKibi extends EventShop {

    public ShopSuKienKinhKibi(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(SuKienKinhKibi.ITEM_CHIA_KHOA_BI_AN, 4, 1, idx++).setPriceBeri(10_000).setLimit(-1)); // Chìa Khóa Bí Ẩn
        shopItems.add(new itemz.MainItemShop(SuKienKinhKibi.ITEM_MANH_BAN_DO_CO, 4, 1,  idx++).setPriceBeri(10_000).setLimit(-1)); // Mảnh Bản Đồ Cổ
        shopItems.add(new itemz.MainItemShop(SuKienKinhKibi.ITEM_RUONG_KI_BI_S, 4, 1,   idx++).setPriceRuby(10).setLimit(50));      // Rương Kì Bí Cấp S
        shopItems.add(new itemz.MainItemShop(359, 4, 1,                  idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi quà bằng Kinh Kì Bí
        shopItems.add(new itemz.MainItemShop(85, 105, 1, 1, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 150));  // Thời trang Tôn Ngộ Không
        shopItems.add(new itemz.MainItemShop(86, 105, 1, 1, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 150));  // Thời trang Đường Tăng
        shopItems.add(new itemz.MainItemShop(87, 105, 1, 1, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 150));  // Thời trang Trư Bát Giới Nam
        shopItems.add(new itemz.MainItemShop(89, 105, 1, 1, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 150));  // Thời trang Sa Tăng
        shopItems.add(new itemz.MainItemShop(77, 105, 1, 1, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 150));  // Chuột Thần Tài
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 200));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 150));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKienKinhKibi.ITEM_KINH_KI_BI, 25));    // Bột siêu cấp x5
    
    }
}
