package event.shop;

import event.Event;
import event.SuKienBigMom;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop su kien rieng biet cho SuKienBigMom.
 */
public class ShopSuKienBigMom extends EventShop {

    public ShopSuKienBigMom(Event event) {
        super(event);
    }

    @Override
    public void initShop() {

        int idx = 0;
        shopItems.clear();
        // 1. Vật phẩm bán trực tiếp bằng Beri / Ruby
        shopItems.add(new itemz.MainItemShop(SuKienBigMom.ITEM_BOT_MI, 4, 1,          idx++).setPriceBeri(10_000).setLimit(-1)); // Bột Mì
        shopItems.add(new itemz.MainItemShop(SuKienBigMom.ITEM_DUONG_TRANG, 4, 1,     idx++).setPriceBeri(10_000).setLimit(-1)); // Đường Trắng
        shopItems.add(new itemz.MainItemShop(SuKienBigMom.ITEM_BANH_SIEU_CAP, 4, 1,   idx++).setPriceRuby(5).setLimit(50));      // Bánh Kem Siêu Cấp
        shopItems.add(new itemz.MainItemShop(SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 4, 1, idx++).setPriceRuby(10).setLimit(50));     // Huy Hiệu Big Mom
        shopItems.add(new itemz.MainItemShop(359, 4, 1,                  idx++).setPriceBeri(10_000).setLimit(-1)); // Pháo hoa

        // 2. Đổi quà bằng Huy Hiệu Big Mom
        shopItems.add(new itemz.MainItemShop(SuKienBigMom.ITEM_MANH_TIEU_MERRY, 4, 1, 10, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 20));  // Mảnh Ghép Tiểu Merry
        shopItems.add(new itemz.MainItemShop(132, 105, 1, 1, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 200));  // Thời Trang Katakuri
        shopItems.add(new itemz.MainItemShop(158, 4, 1, 100, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 50));  // Rương Đại Ác Quỷ
        shopItems.add(new itemz.MainItemShop(690, 4, 1, 5, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 200));   // Rương TAQ Tự Chọn
        shopItems.add(new itemz.MainItemShop(823, 4, 1, 5, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 150));   // Rương Dial Truyền Thuyết
        shopItems.add(new itemz.MainItemShop(754, 4, 1, 2, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 250));   // Sách Haki Bá Vương
        shopItems.add(new itemz.MainItemShop(753, 4, 1, 2, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 150));   // Sách Haki Vũ Trang
        shopItems.add(new itemz.MainItemShop(794, 4, 1, 5, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 100));   // Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new itemz.MainItemShop(866, 4, 1, 999, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 20));  // Vé Vòng Quay Sự Kiện
        shopItems.add(new itemz.MainItemShop(122, 4, 1, 10, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 30));   // Rương Cam Cùng Hệ Lv10
        shopItems.add(new itemz.MainItemShop(135, 4, 1, 50, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 10));   // Tinh thể đá
        shopItems.add(new itemz.MainItemShop(1, 7, 10, 100, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 5));    // Bột cường hóa x10
        shopItems.add(new itemz.MainItemShop(4, 7, 10, 100, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 10));   // Bột vàng x10
        shopItems.add(new itemz.MainItemShop(18, 7, 5, 50, idx++, 4, SuKienBigMom.ITEM_HUY_HIEU_BIGMOM, 25));    // Bột siêu cấp x5
    
    }
}
