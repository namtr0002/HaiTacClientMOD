package template;

import java.util.HashMap;

/**
 * Một vật phẩm trong hệ thống Đấu Giá.
 * - session: phiên đấu giá (ví dụ "MUA_1") — dùng để lọc hiển thị và ẩn item phiên cũ
 * - priceStart: giá khởi điểm
 * - priceNow: giá hiện tại (tăng dần theo từng lượt đấu)
 * - minPricePlus: mức tăng tối thiểu mỗi lượt đấu
 * - minPriceDutDiem: giá dứt điểm ban đầu (min)
 * - maxPriceDutDiem: giá dứt điểm tối đa (max)
 * - priceDutDiem: giá dứt điểm hiện tại (tự động tăng khi có người đặt giá)
 * - name_own: tên người đang giữ giá cao nhất / chốt cuối
 * - id_own: ID người đang giữ giá cao nhất / chốt cuối
 * - time: timestamp kết thúc (ms)
 * - isClose: đã kết thúc / chốt dứt điểm chưa
 * - isClaimed: người thắng đã nhận vật phẩm chưa
 * - listPrice: map playerId -> tổng búa đã đặt
 */
public class ItemDauGia {
    public int id;
    public String session = "";
    public long time;
    public boolean isClose;
    public boolean isClaimed;
    public int priceStart;
    public int priceNow;
    public int minPricePlus;
    public int minPriceDutDiem;
    public int maxPriceDutDiem;
    public int priceDutDiem;
    public short itemID;
    public String name_own = "";
    public int id_own = -1;
    public HashMap<Integer, Integer> listPrice = new HashMap<>();
}
