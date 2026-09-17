package core;

import itemz.MainItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class ZCollection {

    private ZCollection() {
    }

    // =========================
    // ITEM POOLS
    // =========================
    public static final ItemPool listDa1 = new ItemPool();
    public static final ItemPool listDa2 = new ItemPool();
    public static final ItemPool listDa3 = new ItemPool();
    public static final ItemPool listDa4 = new ItemPool();
    public static final ItemPool listDa5 = new ItemPool();
    public static final ItemPool listDa6 = new ItemPool();

    public static final ItemPool listDaCamThachSieuCap = new ItemPool();
    public static final ItemPool listDaTopAzSieuCap = new ItemPool();
    public static final ItemPool listDaRubySieuCap = new ItemPool();
    public static final ItemPool listDaNgocSieuCap = new ItemPool();
    public static final ItemPool listDaSaphiaSieuCap = new ItemPool();
    public static final ItemPool listDaThachAnhSieuCap = new ItemPool();
    public static final ItemPool listDaHoPhachSieuCap = new ItemPool();
    public static final ItemPool listDaThanThoai = new ItemPool();

    public static final ItemPool listManhDo = new ItemPool();

    public static final ItemPool listRuongGo = new ItemPool();
    public static final ItemPool listRuongHuyenBi = new ItemPool();
    public static final ItemPool listRuongTim = new ItemPool();
    public static final ItemPool listRuongCam = new ItemPool();
    public static final ItemPool listRuongCamCungHe = new ItemPool();
    public static final ItemPool listRuongDo = new ItemPool();
    public static final ItemPool listRuongDoCungHe = new ItemPool();
    public static final ItemPool listRuongVuKhi = new ItemPool();

    public static final ItemPool listRuongTraiAcQuy = new ItemPool();
    public static final ItemPool listRuongDaTuChon = new ItemPool();
    public static final ItemPool listRuongTrangPhuc = new ItemPool();
    public static final ItemPool listRuongKyBi = new ItemPool();
    public static final ItemPool listRuongMaThuat = new ItemPool();

    static {
        initStones();
        initSpecialStones();
        initChests();
        initMisc();
    }

    private static void initStones() {
        addStoneLevel(listDa1, 44);
        addStoneLevel(listDa2, 45);
        addStoneLevel(listDa3, 46);
        addStoneLevel(listDa4, 47);
        addStoneLevel(listDa5, 48);
        addStoneLevel(listDa6, 49);
    }

    private static void initSpecialStones() {
        addRange(listDaCamThachSieuCap, 241, 245);
        addRange(listDaTopAzSieuCap, 246, 250);
        addRange(listDaRubySieuCap, 251, 255);
        addRange(listDaNgocSieuCap, 256, 260);
        addRange(listDaSaphiaSieuCap, 261, 265);
        addRange(listDaThachAnhSieuCap, 266, 270);
        addRange(listDaHoPhachSieuCap, 368, 373);
        addRange(listDaThanThoai, 647, 682);
    }

    private static void initChests() {
        // Rương gỗ / châu báu
        addRange(listRuongGo, 7, 17);

        // Rương huyền bí
        addRange(listRuongHuyenBi, 18, 28);

        // Rương tím
        addRange(listRuongTim, 136, 146);

        // Rương cam
        addRange(listRuongCam, 112, 121);

        // Rương cam cùng hệ
        addRange(listRuongCamCungHe, 122, 131);

        // Rương đỏ -> chuyển thành rương cam
        addRange(listRuongDo, 112, 121);

        // Rương đỏ cùng hệ -> chuyển thành rương cam cùng hệ
        addRange(listRuongDoCungHe, 122, 131);

        // Rương vũ khí cam
        addRange(listRuongVuKhi, 772, 772);
    }

    private static void initMisc() {
        // Mảnh trang bị cam/tím
        addRange(listManhDo, 304, 315);
        addRange(listManhDo, 536, 547);

        // Rương / vật phẩm đặc biệt
        addRange(listRuongTraiAcQuy, 29, 29);
        addRange(listRuongDaTuChon, 691, 691);
        addRange(listRuongDaTuChon, 732, 732);
        addRange(listRuongTrangPhuc, 228, 228);
        addRange(listRuongTrangPhuc, 622, 622);
        addRange(listRuongKyBi, 462, 470);
        addRange(listRuongMaThuat, 215, 218);
    }

    // =========================
    // RANDOM ITEM / ID HELPERS
    // =========================

    public static int randomId(ItemPool pool) {
        return pool.randomId();
    }

    public static MainItem randomItem(ItemPool pool) {
        int id = pool.randomId();
        return id < 0 ? null : new MainItem(id, 4, 1);
    }

    public static int randomStoneLevel(int level) {
        switch (level) {
            case 1:
                return listDa1.randomId();
            case 2:
                return listDa2.randomId();
            case 3:
                return listDa3.randomId();
            case 4:
                return listDa4.randomId();
            case 5:
                return listDa5.randomId();
            case 6:
                return listDa6.randomId();
            default:
                return -1;
        }
    }

    public static MainItem randomStoneItem(int level) {
        int id = randomStoneLevel(level);
        return id < 0 ? null : new MainItem(id, 4, 1);
    }

    public static int randomSpecialStoneByType(int type) {
        switch (type) {
            case 1:
                return listDaCamThachSieuCap.randomId();
            case 2:
                return listDaTopAzSieuCap.randomId();
            case 3:
                return listDaRubySieuCap.randomId();
            case 4:
                return listDaNgocSieuCap.randomId();
            case 5:
                return listDaSaphiaSieuCap.randomId();
            case 6:
                return listDaThachAnhSieuCap.randomId();
            case 7:
                return listDaHoPhachSieuCap.randomId();
            case 8:
                return listDaThanThoai.randomId();
            default:
                return -1;
        }
    }

    public static int randomChestGo() {
        return listRuongGo.randomId();
    }

    public static int randomChestHuyenBi() {
        return listRuongHuyenBi.randomId();
    }

    public static int randomChestTim() {
        return listRuongTim.randomId();
    }

    public static int randomChestCam() {
        return listRuongCam.randomId();
    }

    public static int randomChestCamCungHe() {
        return listRuongCamCungHe.randomId();
    }

    public static int randomChestDo() {
        return listRuongDo.randomId();
    }

    public static int randomChestDoCungHe() {
        return listRuongDoCungHe.randomId();
    }

    public static int randomChestVuKhi() {
        return listRuongVuKhi.randomId();
    }

    public static int randomChestTraiAcQuy() {
        return listRuongTraiAcQuy.randomId();
    }

    public static int randomChestDaTuChon() {
        return listRuongDaTuChon.randomId();
    }

    public static int randomChestTrangPhuc() {
        return listRuongTrangPhuc.randomId();
    }

    public static int randomChestKyBi() {
        return listRuongKyBi.randomId();
    }

    public static int randomChestMaThuat() {
        return listRuongMaThuat.randomId();
    }

    public static int randomAnyChestNormal() {
        switch (ThreadLocalRandom.current().nextInt(4)) {
            case 0:
                return randomChestGo();
            case 1:
                return randomChestHuyenBi();
            case 2:
                return randomChestTim();
            default:
                return randomChestCam();
        }
    }

    public static int randomAnyChestHigh() {
        switch (ThreadLocalRandom.current().nextInt(4)) {
            case 0:
                return randomChestCamCungHe();
            case 1:
                return randomChestDo();
            case 2:
                return randomChestDoCungHe();
            default:
                return randomChestVuKhi();
        }
    }

    public static int randomAnyStone() {
        int level = 1 + ThreadLocalRandom.current().nextInt(6);
        return randomStoneLevel(level);
    }

    // =========================
    // INTERNAL HELPERS
    // =========================

    private static void addStoneLevel(ItemPool pool, int startId) {
        for (int i = 0; i < 6; i++) {
            pool.add(startId + (i * 6));
        }
    }

    private static void addRange(ItemPool pool, int fromId, int toId) {
        for (int id = fromId; id <= toId; id++) {
            pool.add(id);
        }
    }

    public static final class ItemPool {
        private final List<Integer> ids = new ArrayList<>();

        public ItemPool add(int id) {
            ids.add(id);
            return this;
        }

        public ItemPool add(int id, int weight) {
            for (int i = 0; i < weight; i++) {
                ids.add(id);
            }
            return this;
        }

        public ItemPool addRange(int fromId, int toId) {
            for (int id = fromId; id <= toId; id++) {
                ids.add(id);
            }
            return this;
        }

        public boolean isEmpty() {
            return ids.isEmpty();
        }

        public int size() {
            return ids.size();
        }
        
        public short randomId() {
            if (ids.isEmpty()) {
                return -1;
            }
            return (short) ids.get(ThreadLocalRandom.current().nextInt(ids.size())).intValue();
        }

        public MainItem randomItem() {
            int id = randomId();
            return id < 0 ? null : new MainItem(id, 4, 1);
        }
    }

    public enum ChestType {
        GO,
        HUYENBI,
        TIM,
        CAM,
        CAM_CUNG_HE,
        DO,
        DO_CUNG_HE,
        VU_KHI,
        TRAI_AC_QUY,
        DA_TU_CHON,
        TRANG_PHUC,
        KY_BI,
        MA_THUAT
    }

    public static int randomChest(ChestType type) {
        switch (type) {
            case GO:
                return randomChestGo();
            case HUYENBI:
                return randomChestHuyenBi();
            case TIM:
                return randomChestTim();
            case CAM:
                return randomChestCam();
            case CAM_CUNG_HE:
                return randomChestCamCungHe();
            case DO:
                return randomChestDo();
            case DO_CUNG_HE:
                return randomChestDoCungHe();
            case VU_KHI:
                return randomChestVuKhi();
            case TRAI_AC_QUY:
                return randomChestTraiAcQuy();
            case DA_TU_CHON:
                return randomChestDaTuChon();
            case TRANG_PHUC:
                return randomChestTrangPhuc();
            case KY_BI:
                return randomChestKyBi();
            case MA_THUAT:
                return randomChestMaThuat();
            default:
                return -1;
        }
    }
}