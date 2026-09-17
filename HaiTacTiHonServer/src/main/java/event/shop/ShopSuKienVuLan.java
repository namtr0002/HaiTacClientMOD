package event.shop;

import event.Event;
import event.SuKienVuLan;
import itemz.MainItemShop;
import java.util.ArrayList;

/**
 * Shop sự kiện riêng biệt cho SuKienVuLan.
 */
public class ShopSuKienVuLan extends EventShop {

    public ShopSuKienVuLan(Event event) {
        super(event);
    }

    @Override
    public void initShop() {
        int idx = 0;
        shopItems.clear();

        // 1. Rương đại ác quỷ
        shopItems.add(new MainItemShop(158, 4, 1, 5, idx++, new int[][]{
                {4, SuKienVuLan.ITEM_HOA_HONG_DO, 500},
                {4, SuKienVuLan.ITEM_BO_HOA_RUC_RO, 20}
        }));

        // 2. Rương TAQ Tự Chọn
        shopItems.add(new MainItemShop(690, 4, 1, 2, idx++, new int[][]{
                {4, SuKienVuLan.ITEM_HOA_HONG_DO, 900},
                {4, SuKienVuLan.ITEM_BO_HOA_RUC_RO, 50}
        }));

        // 3. Rương Dial Truyền Thuyết
        shopItems.add(new MainItemShop(823, 4, 1, 2, idx++, new int[][]{
                {4, SuKienVuLan.ITEM_HOA_HONG_DO, 800},
                {4, SuKienVuLan.ITEM_BO_HOA_RUC_RO, 40}
        }));

        // 4. Sách Haki Bá Vương
        shopItems.add(new MainItemShop(754, 4, 1, 1, idx++, new int[][]{
                {4, SuKienVuLan.ITEM_HOA_HONG_TRANG, 900},
                {4, SuKienVuLan.ITEM_HOA_HONG_DO, 900},
                {4, SuKienVuLan.ITEM_BO_HOA_RUC_RO, 100}
        }));

        // 5. Sách Haki Vũ Trang
        shopItems.add(new MainItemShop(753, 4, 1, 1, idx++, new int[][]{
                {4, SuKienVuLan.ITEM_HOA_HONG_TRANG, 600},
                {4, SuKienVuLan.ITEM_HOA_HONG_DO, 600},
                {4, SuKienVuLan.ITEM_BO_HOA_RUC_RO, 50}
        }));

        // 6. Sách Haki Quan Sát
        shopItems.add(new MainItemShop(684, 4, 1, 1, idx++, new int[][]{
                {4, SuKienVuLan.ITEM_HOA_HONG_TRANG, 600},
                {4, SuKienVuLan.ITEM_HOA_HONG_DO, 600},
                {4, SuKienVuLan.ITEM_BO_HOA_RUC_RO, 50}
        }));

        // 7. Bảo Hiểm Chuyển Hóa Siêu
        shopItems.add(new MainItemShop(794, 4, 1, 3, idx++, new int[][]{
                {4, SuKienVuLan.ITEM_HOA_HONG_DO, 500},
                {4, SuKienVuLan.ITEM_BO_HOA_RUC_RO, 30}
        }));

        // 8. Vé Vòng Quay Sự Kiện
        shopItems.add(new MainItemShop(866, 4, 1, 999, idx++, 4, SuKienVuLan.ITEM_HOA_HONG_TRANG, 100));

        // 9. Thời Trang Boa Hancock
        shopItems.add(new MainItemShop(84, 105, 1, 1, idx++, new int[][]{
                {4, SuKienVuLan.ITEM_HOA_HONG_TRANG, 1000},
                {4, SuKienVuLan.ITEM_BO_HOA_RUC_RO, 100}
        }));

        // 10. Bột Cường Hóa x10
        shopItems.add(new MainItemShop(1, 7, 10, 100, idx++, 4, SuKienVuLan.ITEM_HOA_HONG_DO, 100));

        // 11. Bột Vàng x10
        shopItems.add(new MainItemShop(4, 7, 10, 100, idx++, 4, SuKienVuLan.ITEM_HOA_HONG_DO, 100));

        // 12. Bột Siêu Cấp x5
        shopItems.add(new MainItemShop(18, 7, 5, 50, idx++, 4, SuKienVuLan.ITEM_HOA_HONG_DO, 250));

        // 13. Khiên
        shopItems.add(new MainItemShop(10, 7, 1, 50, idx++, 4, SuKienVuLan.ITEM_HOA_HONG_DO, 200));

        // 14. Bông hồng đỏ: mua bằng ruby
        shopItems.add(new MainItemShop(SuKienVuLan.ITEM_HOA_HONG_DO, 4, 10, idx++)
                .setPriceRuby(10)
                .setLimit(10));
    }
}
