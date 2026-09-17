package database;

import itemz.MainItem;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {

    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    // ==========================================
    // STATIC RANDOM COLLECTIONS FOR ITEMS
    // ==========================================

    // 1. Đá Khảm Cấp 1 -> 6
    public static RandomCollection<MainItem> listDa1 = new RandomCollection<>();
    public static RandomCollection<MainItem> listDa2 = new RandomCollection<>();
    public static RandomCollection<MainItem> listDa3 = new RandomCollection<>();
    public static RandomCollection<MainItem> listDa4 = new RandomCollection<>();
    public static RandomCollection<MainItem> listDa5 = new RandomCollection<>();
    public static RandomCollection<MainItem> listDa6 = new RandomCollection<>();

    // 2. Đá Khảm Siêu Cấp & Thần Thoại
    public static RandomCollection<MainItem> listDaSieuCap = new RandomCollection<>();
    public static RandomCollection<MainItem> listDaThanThoai = new RandomCollection<>();

    // 3. Rương Đồ Cam & Rương Cam Cùng Hệ
    public static RandomCollection<MainItem> listRuongCam = new RandomCollection<>();
    public static RandomCollection<MainItem> listRuongCamHe = new RandomCollection<>();

    // 4. Rương Đồ Đỏ & Rương Đỏ Cùng Hệ
    public static RandomCollection<MainItem> listRuongDo = new RandomCollection<>();
    public static RandomCollection<MainItem> listRuongDoHe = new RandomCollection<>();

    // 5. Mảnh Trang Bị (Đỏ 3x-10x, Cam, Tím, Xanh)
    public static RandomCollection<MainItem> listManhDo = new RandomCollection<>();

    // 6. Rương Châu Báu, Huyền Bí, Tím, Anh Hùng, Vũ Khí
    public static RandomCollection<MainItem> listRuongChauBau = new RandomCollection<>();
    public static RandomCollection<MainItem> listRuongHuyenBi = new RandomCollection<>();
    public static RandomCollection<MainItem> listRuongTim = new RandomCollection<>();
    public static RandomCollection<MainItem> listRuongAnhHung = new RandomCollection<>();
    public static RandomCollection<MainItem> listRuongVuKhi = new RandomCollection<>();

    // 7. Trái Ác Quỷ Phân Loại (Sơ Cấp, Trung Cấp, Cao Cấp, Rương Đại Ác Quỷ 158, Rương Siêu Đại Ác Quỷ 911) & Pet
    public static RandomCollection<MainItem> listTraiAcQuySoCap = new RandomCollection<>();
    public static RandomCollection<MainItem> listTraiAcQuyTrungCap = new RandomCollection<>();
    public static RandomCollection<MainItem> listTraiAcQuyCaoCap = new RandomCollection<>();
    public static RandomCollection<MainItem> listTraiAcQuyDaiAcQuy = new RandomCollection<>();
    public static RandomCollection<MainItem> listTraiAcQuySieuDaiAcQuy = new RandomCollection<>();
    public static RandomCollection<MainItem> listTraiAcQuy = new RandomCollection<>();
    public static RandomCollection<MainItem> listPet = new RandomCollection<>();
    public static RandomCollection<MainItem> listManhPet = new RandomCollection<>();

    // 8. Hộp Trang Phục & Thẻ Thời Trang
    public static RandomCollection<MainItem> listFashionBox = new RandomCollection<>();

    // 9. Mảnh Sách Haki & Mảnh Bản Đồ
    public static RandomCollection<MainItem> listManhHaki = new RandomCollection<>();
    public static RandomCollection<MainItem> listManhBanDo = new RandomCollection<>();

    // 10. Thức Ăn, Thuốc Hồi Phục, Bùa & Vé
    public static RandomCollection<MainItem> listThucAn = new RandomCollection<>();
    public static RandomCollection<MainItem> listThuocHoiPhuc = new RandomCollection<>();
    public static RandomCollection<MainItem> listBuaVaVe = new RandomCollection<>();

    // 11. Rương Sự Kiện Tổng Hợp
    public static RandomCollection<MainItem> listRuongSuKien = new RandomCollection<>();

    static {
        init();
    }

    public static void init() {
        initStones();
        initChests();
        initFragments();
        initDevilFruits();
        initPets();
        initFashion();
        initConsumables();
        initEventBoxes();
    }

    private static void initStones() {
        // Cấp 1
        int[] da1 = {44, 50, 56, 62, 68, 74, 221, 362};
        listDa1.clear();
        for (int id : da1) listDa1.add(10, new MainItem(id, 4, 1));

        // Cấp 2
        int[] da2 = {45, 51, 57, 63, 69, 75, 222, 363};
        listDa2.clear();
        for (int id : da2) listDa2.add(10, new MainItem(id, 4, 1));

        // Cấp 3
        int[] da3 = {46, 52, 58, 64, 70, 76, 223, 364};
        listDa3.clear();
        for (int id : da3) listDa3.add(10, new MainItem(id, 4, 1));

        // Cấp 4
        int[] da4 = {47, 53, 59, 65, 71, 77, 224, 365};
        listDa4.clear();
        for (int id : da4) listDa4.add(10, new MainItem(id, 4, 1));

        // Cấp 5
        int[] da5 = {48, 54, 60, 66, 72, 78, 225, 366};
        listDa5.clear();
        for (int id : da5) listDa5.add(10, new MainItem(id, 4, 1));

        // Cấp 6
        int[] da6 = {49, 55, 61, 67, 73, 79, 226, 367};
        listDa6.clear();
        for (int id : da6) listDa6.add(10, new MainItem(id, 4, 1));

        // Đá Siêu Cấp (241..270, 368..373)
        listDaSieuCap.clear();
        for (int id = 241; id <= 270; id++) listDaSieuCap.add(10, new MainItem(id, 4, 1));
        for (int id = 368; id <= 373; id++) listDaSieuCap.add(10, new MainItem(id, 4, 1));

        // Đá Thần Thoại (647..682)
        listDaThanThoai.clear();
        for (int id = 647; id <= 682; id++) listDaThanThoai.add(10, new MainItem(id, 4, 1));
    }

    private static void initChests() {
        // Rương Châu Báu (7..17)
        listRuongChauBau.clear();
        for (int id = 7; id <= 17; id++) listRuongChauBau.add(10, new MainItem(id, 4, 1));

        // Rương Huyền Bí (18..28)
        listRuongHuyenBi.clear();
        for (int id = 18; id <= 28; id++) listRuongHuyenBi.add(10, new MainItem(id, 4, 1));

        // Rương Tím (136..146)
        listRuongTim.clear();
        for (int id = 136; id <= 146; id++) listRuongTim.add(10, new MainItem(id, 4, 1));

        // Rương Anh Hùng (147..157)
        listRuongAnhHung.clear();
        for (int id = 147; id <= 157; id++) listRuongAnhHung.add(10, new MainItem(id, 4, 1));

        // Rương Cam Lv10 -> 100 (112..121)
        listRuongCam.clear();
        for (int id = 112; id <= 121; id++) listRuongCam.add(10, new MainItem(id, 4, 1));

        // Rương Cam Cùng Hệ Lv10 -> 100 (122..131)
        listRuongCamHe.clear();
        for (int id = 122; id <= 131; id++) listRuongCamHe.add(10, new MainItem(id, 4, 1));

        // Rương Đỏ -> Chuyển thành Rương Cam Lv10 -> 100 (112..121)
        listRuongDo.clear();
        for (int id = 112; id <= 121; id++) listRuongDo.add(10, new MainItem(id, 4, 1));

        // Rương Đỏ Cùng Hệ -> Chuyển thành Rương Cam Cùng Hệ (122..131)
        listRuongDoHe.clear();
        for (int id = 122; id <= 131; id++) listRuongDoHe.add(10, new MainItem(id, 4, 1));

        // Rương Vũ Khí Cam (772)
        listRuongVuKhi.clear();
        listRuongVuKhi.add(10, new MainItem(772, 4, 1));
    }

    private static void initFragments() {
        listManhDo.clear();
        // Mảnh 9x Cam, Tím, Xanh, Trắng (304..315)
        for (int id = 304; id <= 315; id++) {
            listManhDo.add(15, new MainItem(id, 4, 1));
        }

        // Mảnh 10x Cam, Tím, Xanh, Trắng (536..547)
        for (int id = 536; id <= 547; id++) {
            listManhDo.add(10, new MainItem(id, 4, 1));
        }

        // Mảnh Sách Haki (Quan Sát: 639..642, Vũ Trang: 755..758, Bá Vương: 759..762)
        listManhHaki.clear();
        for (int id = 639; id <= 642; id++) listManhHaki.add(10, new MainItem(id, 4, 1));
        for (int id = 755; id <= 758; id++) listManhHaki.add(10, new MainItem(id, 4, 1));
        for (int id = 759; id <= 762; id++) listManhHaki.add(10, new MainItem(id, 4, 1));

        // Mảnh Bản Đồ Kho Báu (442..448)
        listManhBanDo.clear();
        for (int id = 442; id <= 448; id++) listManhBanDo.add(10, new MainItem(id, 4, 1));
    }

    private static void initDevilFruits() {
        // 1. Trái Ác Quỷ Sơ Cấp (Cao Su 33, Tuần Lộc 34, Khói 88, Bò Tót 90, Vẽ Vẽ 91, Chim Ưng 220, Sáp 316, Dao 317, Kilo 318)
        int[] soCap = {33, 34, 88, 90, 91, 220, 316, 317, 318};
        listTraiAcQuySoCap.clear();
        for (int id : soCap) {
            listTraiAcQuySoCap.add(10, new MainItem(id, 4, 1));
        }

        // 2. Trái Ác Quỷ Trung Cấp (Lửa 32, Băng 92, Cát 93, Báo Đốm 219, Tình Yêu 870)
        int[] trungCap = {32, 92, 93, 219, 870};
        listTraiAcQuyTrungCap.clear();
        for (int id : trungCap) {
            listTraiAcQuyTrungCap.add(10, new MainItem(id, 4, 1));
        }

        // 3. Trái Ác Quỷ Cao Cấp Chuẩn (Sét 160, Nham Thạch 161, Chấn Thiên 240, Bóng Tối 427) - KHÔNG Ánh Sáng, Nika
        int[] caoCap = {160, 161, 240, 427};
        listTraiAcQuyCaoCap.clear();
        for (int id : caoCap) {
            listTraiAcQuyCaoCap.add(10.0, new MainItem(id, 4, 1));
        }

        // 4. Rương Đại Ác Quỷ (ID 158): Sơ cấp, Trung cấp (có Tình Yêu), Cao cấp (Sét, Nham Thạch, Chấn Thiên, Bóng Tối). KHÔNG CÓ Ánh Sáng, Nika!
        listTraiAcQuyDaiAcQuy.clear();
        for (int id : soCap) listTraiAcQuyDaiAcQuy.add(15.0, new MainItem(id, 4, 1));     // 9 x 15 = 135 (~58.7%)
        for (int id : trungCap) listTraiAcQuyDaiAcQuy.add(14.0, new MainItem(id, 4, 1));  // 5 x 14 = 70  (~30.4%)
        for (int id : caoCap) listTraiAcQuyDaiAcQuy.add(6.25, new MainItem(id, 4, 1));    // 4 x 6.25 = 25 (~10.9%)

        // 5. Rương Siêu Đại Ác Quỷ (ID 911): Trái Trung Cấp trở lên (Trung cấp + Cao cấp). Tỉ lệ thấp ra Ánh Sáng (869), Nika (873) rất thấp!
        listTraiAcQuySieuDaiAcQuy.clear();
        for (int id : trungCap) listTraiAcQuySieuDaiAcQuy.add(24.0, new MainItem(id, 4, 1)); // 5 x 24 = 120 (~77.9%)
        for (int id : caoCap) listTraiAcQuySieuDaiAcQuy.add(7.0, new MainItem(id, 4, 1));    // 4 x 7 = 28   (~18.2%)
        listTraiAcQuySieuDaiAcQuy.add(4.0, new MainItem(869, 4, 1));                          // Ánh Sáng: ~2.6%
        listTraiAcQuySieuDaiAcQuy.add(2.0, new MainItem(873, 4, 1));                          // Nika: ~1.3%

        // 6. Tổng hợp toàn bộ Trái Ác Quỷ cho pool chung
        listTraiAcQuy.clear();
        for (int id : soCap) listTraiAcQuy.add(15.0, new MainItem(id, 4, 1));
        for (int id : trungCap) listTraiAcQuy.add(8.0, new MainItem(id, 4, 1));
        for (int id : caoCap) listTraiAcQuy.add(3.0, new MainItem(id, 4, 1));
        listTraiAcQuy.add(1.0, new MainItem(869, 4, 1));
        listTraiAcQuy.add(0.5, new MainItem(873, 4, 1));
    }

    private static void initPets() {
        // Pet trực tiếp (728, 729, 730, 731, 825, 826)
        listPet.clear();
        int[] pets = {728, 729, 730, 731, 825, 826};
        for (int id : pets) listPet.add(10, new MainItem(id, 4, 1));

        // Mảnh Pet (879, 895, 903, 904, 905)
        listManhPet.clear();
        int[] petFragments = {879, 895, 903, 904, 905};
        for (int id : petFragments) listManhPet.add(10, new MainItem(id, 4, 1));
    }

    private static void initFashion() {
        int[] fashionBoxes = {
            132, 228, 440, 456, 469, 475, 482, 518, 520, 522,
            568, 581, 589, 600, 621, 622, 637, 638, 726, 736,
            739, 748, 749, 750, 778, 790, 791, 792, 793, 821,
            822, 843, 844, 887, 906, 907
        };
        listFashionBox.clear();
        for (int id : fashionBoxes) {
            listFashionBox.add(10, new MainItem(id, 4, 1));
        }
    }

    private static void initConsumables() {
        // Thức ăn
        int[] foods = {
            2, 3, 4, 5, 85, 98, 100, 173, 174, 207, 208, 209, 210,
            233, 234, 235, 350, 378, 379, 380, 391, 392, 393, 394, 395,
            458, 465, 471, 472, 525, 530, 531, 532, 533, 534, 569, 570,
            876, 877
        };
        listThucAn.clear();
        for (int id : foods) listThucAn.add(10, new MainItem(id, 4, 1));

        // Thuốc hồi phục
        int[] potions = {81, 82, 83, 84, 89, 175, 176, 177, 178, 179, 413, 414, 415, 476};
        listThuocHoiPhuc.clear();
        for (int id : potions) listThuocHoiPhuc.add(10, new MainItem(id, 4, 1));

        // Bùa, Vé, Bảo hiểm
        int[] passes = {31, 41, 42, 80, 214, 271, 339, 416, 548, 549, 550, 551, 794, 801, 802};
        listBuaVaVe.clear();
        for (int id : passes) listBuaVaVe.add(10, new MainItem(id, 4, 1));
    }

    private static void initEventBoxes() {
        int[] eventBoxes = {
            105, 106, 158, 169, 170, 171, 172, 215, 216, 217, 218,
            227, 231, 239, 285, 286, 320, 321, 331, 355, 357, 431,
            432, 433, 439, 455, 459, 462, 470, 483, 484, 485, 492,
            519, 526, 535, 576, 578, 582, 583, 584, 585, 586, 587,
            592, 593, 594, 595, 596, 609, 610, 613, 789, 797, 798,
            799, 800, 803, 804, 809, 823, 837, 838, 887, 890, 891,
            892, 893, 911
        };
        listRuongSuKien.clear();
        for (int id : eventBoxes) {
            listRuongSuKien.add(10, new MainItem(id, 4, 1));
        }
    }

    // ==========================================
    // CONSTRUCTORS & CORE METHODS
    // ==========================================

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.random = random;
    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0 || result == null) {
            return this;
        }
        total += weight;
        map.put(total, result);
        return this;
    }

    public void clear() {
        map.clear();
        total = 0;
    }

    public boolean isEmpty() {
        return map.isEmpty() || total <= 0;
    }

    public int size() {
        return map.size();
    }

    public E next() {
        if (map.isEmpty() || total <= 0) return null;
        double value = random.nextDouble() * total;
        Map.Entry<Double, E> entry = map.higherEntry(value);
        return entry != null ? entry.getValue() : map.lastEntry().getValue();
    }

    /**
     * Lấy MainItem mới clone từ kết quả random (tránh dùng chung instance)
     */
    public MainItem nextMainItem() {
        E val = next();
        if (val instanceof MainItem) {
            MainItem it = (MainItem) val;
            return new MainItem(it.id, it.cat, it.num);
        }
        return null;
    }

    /**
     * Lấy nhanh ID vật phẩm random từ pool
     */
    public int nextId() {
        E val = next();
        if (val instanceof MainItem) {
            return ((MainItem) val).id;
        }
        return -1;
    }

    // ==========================================
    // CONVENIENT STATIC GETTERS
    // ==========================================

    public static MainItem getStone(int level) {
        switch (level) {
            case 1: return listDa1.nextMainItem();
            case 2: return listDa2.nextMainItem();
            case 3: return listDa3.nextMainItem();
            case 4: return listDa4.nextMainItem();
            case 5: return listDa5.nextMainItem();
            case 6: return listDa6.nextMainItem();
            case 7: return listDaSieuCap.nextMainItem();
            case 8: return listDaThanThoai.nextMainItem();
            default: return listDa1.nextMainItem();
        }
    }

    public static int getStoneId(int level) {
        MainItem it = getStone(level);
        return it != null ? it.id : -1;
    }

    public static MainItem getRuongCam() {
        return listRuongCam.nextMainItem();
    }

    public static MainItem getRuongCamHe() {
        return listRuongCamHe.nextMainItem();
    }

    public static MainItem getRuongDo() {
        return listRuongDo.nextMainItem();
    }

    public static MainItem getRuongDoHe() {
        return listRuongDoHe.nextMainItem();
    }

    public static MainItem getManhDo() {
        return listManhDo.nextMainItem();
    }

    public static MainItem getTraiAcQuy() {
        return listTraiAcQuy.nextMainItem();
    }

    public static MainItem getTraiAcQuySoCap() {
        return listTraiAcQuySoCap.nextMainItem();
    }

    public static int getTraiAcQuySoCapId() {
        MainItem it = getTraiAcQuySoCap();
        return it != null ? it.id : -1;
    }

    public static MainItem getTraiAcQuyTrungCap() {
        return listTraiAcQuyTrungCap.nextMainItem();
    }

    public static int getTraiAcQuyTrungCapId() {
        MainItem it = getTraiAcQuyTrungCap();
        return it != null ? it.id : -1;
    }

    public static MainItem getTraiAcQuyCaoCap() {
        return listTraiAcQuyCaoCap.nextMainItem();
    }

    public static int getTraiAcQuyCaoCapId() {
        MainItem it = getTraiAcQuyCaoCap();
        return it != null ? it.id : -1;
    }

    public static MainItem getTraiAcQuyDaiAcQuy() {
        return listTraiAcQuyDaiAcQuy.nextMainItem();
    }

    public static int getTraiAcQuyDaiAcQuyId() {
        MainItem it = getTraiAcQuyDaiAcQuy();
        return it != null ? it.id : 33;
    }

    public static MainItem getTraiAcQuySieuDaiAcQuy() {
        return listTraiAcQuySieuDaiAcQuy.nextMainItem();
    }

    public static int getTraiAcQuySieuDaiAcQuyId() {
        MainItem it = getTraiAcQuySieuDaiAcQuy();
        return it != null ? it.id : 32;
    }

    public static MainItem getTraiAcQuy(int tier) {
        switch (tier) {
            case 1: return getTraiAcQuySoCap();
            case 2: return getTraiAcQuyTrungCap();
            case 3: return getTraiAcQuyCaoCap();
            default: return listTraiAcQuy.nextMainItem();
        }
    }

    public static int getTraiAcQuyId(int tier) {
        MainItem it = getTraiAcQuy(tier);
        return it != null ? it.id : -1;
    }

    public static MainItem getPet() {
        return listPet.nextMainItem();
    }

    public static MainItem getFashionBox() {
        return listFashionBox.nextMainItem();
    }

    public static MainItem getSuKienGift() {
        return listRuongSuKien.nextMainItem();
    }

    public HashMap<E, Integer> test(int times) {
        HashMap<E, Integer> hashmap = new HashMap<>();
        for (int i = 0; i < times; i++) {
            E value = next();
            if (hashmap.containsKey(value)) {
                int quantity = hashmap.get(value);
                hashmap.put(value, quantity + 1);
            } else {
                hashmap.put(value, 1);
            }
        }
        return hashmap;
    }
}
