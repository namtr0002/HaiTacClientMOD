package event.shop;

import event.Event;
import event.SuKien20Thang10;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKien20Thang10.
 */
public class ShopSuKien20Thang10 extends EventShop {

    public ShopSuKien20Thang10(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(SuKien20Thang10.ITEM_GIAY_GOI, 4, 1, idx++).setPriceBeri(10_000).setLimit(-1)); // Giấy Gói Quà
        shopItems.add(new itemz.MainItemShop(SuKien20Thang10.ITEM_RUY_BANG_TIM, 4, 1, idx++).setPriceRuby(2).setLimit(-1)); // Ruy Băng Tím
        shopItems.add(new itemz.MainItemShop(SuKien20Thang10.ITEM_RUONG_DAU_LAU, 4, 1, idx++).setPriceRuby(10).setLimit(50)); // Rương Đầu Lâu
        shopItems.add(new itemz.MainItemShop(SuKien20Thang10.ITEM_PHAO_HOA, 4, 1, idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Vật phẩm đổi bằng Cành Hoa 20.10
        shopItems.add(new itemz.MainItemShop(84, 105, 1, 1, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 150));  // Thời trang Boa Hancock
        shopItems.add(new itemz.MainItemShop(1, 105, 1, 1, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 150));   // Thời trang Nico Robin
        shopItems.add(new itemz.MainItemShop(130, 105, 1, 1, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 150)); // Thời trang Nefertari Vivi
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 200));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 150));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 30));   // Rương Cam Cùng Hệ
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKien20Thang10.ITEM_CANH_HOA, 25));    // Bột siêu cấp x5
    
    }
}
