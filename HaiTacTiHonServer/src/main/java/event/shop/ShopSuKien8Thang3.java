package event.shop;

import event.Event;
import event.SuKien8Thang3;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKien8Thang3.
 */
public class ShopSuKien8Thang3 extends EventShop {

    public ShopSuKien8Thang3(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(SuKien8Thang3.ITEM_RUY_BANG, 4, 1,    idx++).setPriceBeri(10_000).setLimit(-1)); // Ruy Băng
        shopItems.add(new itemz.MainItemShop(819, 4, 1,              idx++).setPriceBeri(10_000).setLimit(-1)); // Giấy Màu
        shopItems.add(new itemz.MainItemShop(SuKien8Thang3.ITEM_LO_NUOC_HOA, 4, 1, idx++).setPriceRuby(5).setLimit(50));     // Lọ Nước Hoa
        shopItems.add(new itemz.MainItemShop(359, 4, 1,              idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi bằng Bó Hoa Đỏ
        shopItems.add(new itemz.MainItemShop(821, 4, 1, 5, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 20));  // Hộp trang phục Boa Hancock
        shopItems.add(new itemz.MainItemShop(822, 4, 1, 5, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 20));  // Hộp trang phục Nami Wano
        shopItems.add(new itemz.MainItemShop(824, 4, 1, 1, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 30));  // Danh Hiệu Ai Mà Xinh Thế
        shopItems.add(new itemz.MainItemShop(84, 105, 1, 1, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 150));  // Thời trang Boa Hancock
        shopItems.add(new itemz.MainItemShop(125, 105, 1, 1, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 150)); // Thời trang Nami Wano
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 200));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 150));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 30));   // Rương Cam Cùng Hệ
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKien8Thang3.ITEM_BO_HOA_DO, 25));    // Bột siêu cấp x5
    
    }
}
