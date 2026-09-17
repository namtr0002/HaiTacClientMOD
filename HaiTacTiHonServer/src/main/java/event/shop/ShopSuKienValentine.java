package event.shop;

import event.Event;
import event.SuKienValentine;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienValentine.
 */
public class ShopSuKienValentine extends EventShop {

    public ShopSuKienValentine(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(SuKienValentine.ITEM_SOCOLA_TRANG, 4, 1,    idx++).setPriceBeri(10_000).setLimit(-1)); // Socola Trắng
        shopItems.add(new itemz.MainItemShop(SuKienValentine.ITEM_SOCOLA_SUA, 4, 1,      idx++).setPriceBeri(10_000).setLimit(-1)); // Socola Sữa
        shopItems.add(new itemz.MainItemShop(SuKienValentine.ITEM_BANH_KEM_SOCOLA, 4, 1, idx++).setPriceRuby(5).setLimit(50));     // Bánh Kem Socola
        shopItems.add(new itemz.MainItemShop(359, 4, 1,                  idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi bằng Hộp Socola
        shopItems.add(new itemz.MainItemShop(80, 105, 1, 1, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 150));  // Thời trang Thiên Thần Nam
        shopItems.add(new itemz.MainItemShop(81, 105, 1, 1, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 150));  // Thời trang Thiên Thần Nữ
        shopItems.add(new itemz.MainItemShop(SuKienValentine.ITEM_RUONG_TINH_YEU, 4, 1, 20, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 30));  // Rương Tình Yêu
        shopItems.add(new itemz.MainItemShop(SuKienValentine.ITEM_BO_HOA_TINH_YEU, 4, 1, 10, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 50)); // Bó Hoa Tình Yêu
        shopItems.add(new itemz.MainItemShop(870, 4, 1, 2, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 100));   // Trái tình yêu
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 50));  // Rương đại ác quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 200));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 150));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKienValentine.ITEM_HOP_SOCOLA, 25));    // Bột siêu cấp x5
    
    }
}
