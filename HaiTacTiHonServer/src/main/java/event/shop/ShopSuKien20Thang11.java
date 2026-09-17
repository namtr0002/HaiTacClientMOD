package event.shop;

import event.Event;
import event.SuKien20Thang11;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKien20Thang11.
 */
public class ShopSuKien20Thang11 extends EventShop {

    public ShopSuKien20Thang11(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(SuKien20Thang11.ITEM_HOA_HONG_TRANG, 4, 1, idx++).setPriceBeri(5_000).setLimit(-1)); // Hoa hồng trắng
        shopItems.add(new itemz.MainItemShop(SuKien20Thang11.ITEM_HOA_HONG_DO, 4, 1, idx++).setPriceRuby(2).setLimit(-1)); // Hoa hồng đỏ
        shopItems.add(new itemz.MainItemShop(SuKien20Thang11.ITEM_PHAO_HOA, 4, 1, idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Vật phẩm đổi bằng Bó Hoa Rực Rỡ (890)
        shopItems.add(new itemz.MainItemShop(21, 105, 1, 1, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 150));  // Thời trang Minh Vương Rayleigh
        shopItems.add(new itemz.MainItemShop(84, 105, 1, 1, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 150));  // Thời trang Boa Hancock
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 200));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 150));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 30));   // Rương Cam Cùng Hệ
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKien20Thang11.ITEM_BO_HOA_RUC_RO, 25));    // Bột siêu cấp x5
    
    }
}
