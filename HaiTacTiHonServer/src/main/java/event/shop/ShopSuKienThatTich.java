package event.shop;

import event.Event;
import event.SuKienThatTich;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienThatTich.
 */
public class ShopSuKienThatTich extends EventShop {

    public ShopSuKienThatTich(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();

        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new MainItemShop(SuKienThatTich.ITEM_GO, 4, 1,                   idx++).setPriceBeri(10_000).setLimit(-1)); // Gỗ
        shopItems.add(new MainItemShop(SuKienThatTich.ITEM_DA, 4, 1,                   idx++).setPriceBeri(10_000).setLimit(-1)); // Đá
        shopItems.add(new MainItemShop(SuKienThatTich.ITEM_LONG_CHIM_THUOC, 4, 1,      idx++).setPriceBeri(10_000).setLimit(-1)); // Lông chim thước
        shopItems.add(new MainItemShop(SuKienThatTich.ITEM_GIAY_GOI_QUA, 4, 1,         idx++).setPriceBeri(10_000).setLimit(-1)); // Giấy gói quà
        shopItems.add(new MainItemShop(SuKienThatTich.ITEM_CHE_DAU_DO_DAC_BIET, 4, 1, idx++).setPriceRuby(5).setLimit(50));      // Chè Đậu Đỏ Đặc Biệt
        shopItems.add(new MainItemShop(359, 4, 1,                       idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi quà bằng Chè Đậu Đỏ (569)
        shopItems.add(new MainItemShop(132, 105, 1, 1, idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 150));  // Thời Trang Ngưu Lang
        shopItems.add(new MainItemShop(130, 105, 1, 1, idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 150));  // Thời Trang Chức Nữ
        shopItems.add(new MainItemShop(158, 4, 1, 100, idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 50));   // Rương Đại Ác Quỷ
        shopItems.add(new MainItemShop(690, 4, 1, 5,   idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 200));  // Rương TAQ Tự Chọn
        shopItems.add(new MainItemShop(823, 4, 1, 5,   idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 150));  // Rương Dial Truyền Thuyết
        shopItems.add(new MainItemShop(754, 4, 1, 2,   idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 250));  // Sách Haki Bá Vương
        shopItems.add(new MainItemShop(753, 4, 1, 2,   idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 150));  // Sách Haki Vũ Trang
        shopItems.add(new MainItemShop(794, 4, 1, 5,   idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 100));  // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new MainItemShop(866, 4, 1, 999, idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 20));   // Vé Vòng Quay Sự Kiện
        shopItems.add(new MainItemShop(122, 4, 1, 10,  idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new MainItemShop(135, 4, 1, 50,  idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 10));   // Tinh thể đá
        shopItems.add(new MainItemShop(1, 7, 10, 100,  idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 5));    // Bột cường hóa x10
        shopItems.add(new MainItemShop(4, 7, 10, 100,  idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 10));   // Bột vàng x10
        shopItems.add(new MainItemShop(18, 7, 5, 50,   idx++, 4, SuKienThatTich.ITEM_CHE_DAU_DO, 25));   // Bột siêu cấp x5
    
    }
}
