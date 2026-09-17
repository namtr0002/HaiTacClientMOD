package event.shop;

import event.Event;
import event.SuKienTrongCay;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienTrongCay.
 */
public class ShopSuKienTrongCay extends EventShop {

    public ShopSuKienTrongCay(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(SuKienTrongCay.ITEM_HAT_GIONG, 4, 1,    idx++).setPriceBeri(10_000).setLimit(-1)); // Hạt Giống Thường
        shopItems.add(new itemz.MainItemShop(388, 4, 1,               idx++).setPriceRuby(5).setLimit(50));      // Hạt Giống May Mắn
        shopItems.add(new itemz.MainItemShop(SuKienTrongCay.ITEM_PHAN, 4, 1,         idx++).setPriceBeri(5_000).setLimit(-1));  // Phân Bón
        shopItems.add(new itemz.MainItemShop(SuKienTrongCay.ITEM_NUOC, 4, 1,         idx++).setPriceBeri(5_000).setLimit(-1));  // Nước Tưới
        shopItems.add(new itemz.MainItemShop(359, 4, 1,               idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi bằng Giỏ Trái Cây
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 200));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 150));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(221, 4, 1, 100, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 10));  // Đá Hải Thạch cấp 1
        shopItems.add(new itemz.MainItemShop(369, 4, 1, 10, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 50));   // Hổ phách - Topaz siêu cấp
        shopItems.add(new itemz.MainItemShop(370, 4, 1, 10, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 50));   // Hổ phách - Ruby siêu cấp
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKienTrongCay.ITEM_GIO_TRAI_CAY, 25));    // Bột siêu cấp x5
    
    }
}
