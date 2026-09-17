package event;

import event.eboss.BigMom;
import model.Player;
import core.Manager;
import core.ZUtil;
import itemz.MainItem;
import map.Zone;
import mob.Mob;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SuKienBigMom — Sự Kiện Tiệc Bánh Ngọt BigMom (Event ID 16)
 *
 * - Nguyên liệu: Bột Mì (874), Đường trắng (875)
 * - Bánh: Bánh Kem Mê Hoặc (876), Bánh Kem Siêu Cấp (877)
 * - Huy hiệu & Vật phẩm: Huy Hiệu Big Mom (878), Mảnh Ghép Tiểu Merry (879)
 *
 * Giới hạn hoạt động kiếm nguyên liệu / ngày (Yêu cầu cấp độ 40+):
 * - Đánh quái: max 80 nl/ngày (index 0)
 * - Vận buôn: 2 nl/lần, max 10 nl/ngày (index 1)
 * - Thắng truy nã: 10 nl/lần, max 30 nl/ngày (index 2)
 * - Chiến thắng PB vườn cam Nami vòng 10: 5 nl/lần, max 50 nl/ngày (index 3)
 * - Chiến thắng PB liên tầng tầng 5: 5 nl/lần, max 50 nl/ngày (index 4)
 * - Điểm hài lòng cống nạp Big Mom (index 5)
 */
public class SuKienBigMom extends Event {

    public static final int ITEM_BOT_MI           = 874; // Bột Mì
    public static final int ITEM_DUONG_TRANG      = 875; // Đường trắng
    public static final int ITEM_BANH_ME_HOAC     = 876; // Bánh Kem Mê Hoặc
    public static final int ITEM_BANH_SIEU_CAP    = 877; // Bánh Kem Siêu Cấp
    public static final int ITEM_HUY_HIEU_BIGMOM  = 878; // Huy Hiệu Big Mom
    public static final int ITEM_MANH_TIEU_MERRY  = 879; // Mảnh Ghép Tiểu Merry

    public static final int IDX_DANH_QUAI       = 0; // max 80
    public static final int IDX_VAN_BUON        = 1; // max 10
    public static final int IDX_TRUY_NA         = 2; // max 30
    public static final int IDX_VUON_CAM        = 3; // max 50
    public static final int IDX_LIEN_TANG       = 4; // max 50
    public static final int IDX_DIEM_HAI_LONG   = 5; // tổng điểm hài lòng

    public static final int LIMIT_DANH_QUAI = 80;
    public static final int LIMIT_VAN_BUON  = 10;
    public static final int LIMIT_TRUY_NA   = 30;
    public static final int LIMIT_VUON_CAM  = 50;
    public static final int LIMIT_LIEN_TANG = 50;

    /**
     * Danh sách map ngoài làng (1-1, 2-1, 3-1... id next của làng).
     * Loại trừ map bờ biển (is_map_sea), map boss (is_map_boss), phụ bản.
     */
    public static final int[] MAP_OUTER_VILLAGES = new int[]{
        2,   // 1-1 Rừng làng (Next Làng Cối Xay Gió - 1)
        10,  // 2-1 Bến tàu (Next Thị trấn Vỏ Sò - 9)
        18,  // 3-1 Làng chài (Next Thị trấn Orange - 17)
        26,  // 4-1 Đường làng (Next Làng Sirup - 25)
        34,  // 5-1 Nhà hàng Barati 2 (Next Nhà hàng Barati - 33)
        42,  // 6-1 Chợ Dừa (Next Làng Hạt Dẻ - 41)
        50,  // 7-1 Công Viên (Next Thị trấn Khởi Đầu - 49)
        67,  // Next Làng Whishey (66)
        70,  // Next Thị Trấn Cát (69)
        80,  // Next Đảo Giáng Sinh (79)
        84,  // Next Làng Thiên Đường (83)
        94,  // Next Làng Mây Trắng (93)
        108, // Next Đảo Jaza (107)
        115, // 11-2 Thử thách khởi cầu (Next Thị Trấn Thiên Sứ - 113)
        192  // 12-1 Nhà Franky (Next Kinh Đô Nước - 191)
    };

    public static SuKienBigMom instance;

    public static SuKienBigMom gI() {
        if (instance == null) {
            instance = new SuKienBigMom();
        }
        return instance;
    }

    public List<BigMom> listBoss = new java.util.concurrent.CopyOnWriteArrayList<>();

    public SuKienBigMom() {
        super(Event.ID_SUKIEN_BIGMOM, "Sự Kiện Tiệc Bánh Ngọt BigMom");
        this.shopName = "Cửa Hàng Big Mom";
        this.costItemId = ITEM_HUY_HIEU_BIGMOM;
        this.costItemType = 4;
        this.sellableItems = new short[0];

        this.time = "0:00:00 01/01/2026 > 23:59:59 31/12/2026";
        this.timex2pay = "0:00:00 01/01/2026 > 23:59:59 31/12/2026";
        this.timechangeitem = "0:00:00 01/01/2026 > 23:59:59 31/12/2026";
        this.bxhSubType = 16;
        this.bxhNames = new String[]{"Top Điểm Hài Lòng Big Mom"};
        initShop();
        instance = this;
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{874, 875, 876, 877, 878, 879, 885};
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienBigMom(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();

        // Khởi tạo pool Boss Big Mom cho các map ngoài làng
        listBoss = new java.util.concurrent.CopyOnWriteArrayList<>();
        int slot = 0;
        for (int mapId : MAP_OUTER_VILLAGES) {
            BigMom bm = new BigMom(slot++, mapId, 0);
            listBoss.add(bm);
        }

        // [NPC_MIGRATED] // [NPC_MIGRATED] initNpc(); // NPC đã chuyển sang class iNpc riêng // NPC đã chuyển sang class iNpc riêng trong package zinterfaces.menus.npcs.event
        System.out.println("[SuKienBigMom] Init completed. Spawned " + listBoss.size() + " Big Mom bosses in outer village maps.");
    }

    // ======================== MENU NPC ĐẦU BẾP SANJI (LAMBDA MENU) ========================

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!EventManager.isActive(this.id)) return false;
        if (npcId == -1048 || npcId == -154 || npcId == -100 || npcId == this.id) {
            List<model.Menu> menus = new ArrayList<>();
            menus.add(new model.Menu("Cửa Hàng Big Mom", () -> {
                try { openShop(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Làm Bánh Kem Mê Hoặc", () -> {
                try { new itemz.rebuilds.GhepBanhMeHoac().show_table(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Làm Bánh Kem Siêu Cấp (VIP)", () -> {
                try { new itemz.rebuilds.GhepBanhSieuCap().show_table(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Vòng Quay Tiệc Trà Big Mom", () -> {
                try { openLuckyWheel(p, 10, -1); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("BXH Điểm Hài Lòng", () -> {
                try {
                    p.typeBXH = 16;
                    showEventRank(p);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Hướng dẫn sự kiện", () -> {
                try { sendHelp(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Tích nạp sự kiện", () -> {
                try { showTichNap(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            menus.add(new model.Menu("Tích tiêu sự kiện", () -> {
                try { showTichTieu(p); } catch (Exception e) { e.printStackTrace(); }
            }));

            p.getService().openMenu(-1048, "Đầu Bếp Sanji", menus);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        return false;
    }

    @Override
    public void update(int hour, int min, int sec) throws Exception {
        if (!EventManager.isActive(this.id)) return;
        for (BigMom bm : listBoss) {
            if (bm != null) {
                bm.update(bm.mob);
            }
        }
    }

    @Override
    public void resetDailyData(EventData data) {
        if (data == null || data.data == null) return;
        data.data[IDX_DANH_QUAI] = 0;
        data.data[IDX_VAN_BUON] = 0;
        data.data[IDX_TRUY_NA] = 0;
        data.data[IDX_VUON_CAM] = 0;
        data.data[IDX_LIEN_TANG] = 0;
    }

    /**
     * Trao nguyên liệu Bột Mì (874) hoặc Đường Trắng (875) theo giới hạn ngày.
     */
    public synchronized void giveMaterial(Player p, int amount, int limitIndex, int maxLimit, String activityName) {
        if (p == null || p.level < 40 || !EventManager.isActive(this.id)) return;

        EventData data = getOrCreateEventData(p);
        if (data == null || data.data == null) return;

        int current = data.data[limitIndex];
        if (current >= maxLimit) return;

        int toAdd = Math.min(amount, maxLimit - current);
        if (toAdd <= 0) return;

        data.data[limitIndex] += toAdd;

        int botMiCount = 0;
        int duongCount = 0;

        for (int i = 0; i < toAdd; i++) {
            if (ZUtil.random(2) == 0) {
                botMiCount++;
            } else {
                duongCount++;
            }
        }

        if (botMiCount > 0) p.item.add_item_bag47(4, ITEM_BOT_MI, botMiCount);
        if (duongCount > 0) p.item.add_item_bag47(4, ITEM_DUONG_TRANG, duongCount);

        p.item.updateInventory(false);
    }

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!EventManager.isActive(this.id) || mob == null || mob.is_boss || p == null || p.level < 40) return;
        if (mob.map != null) {
            int mapId = mob.map.template.id;
            if (Zone.is_map_sea(mapId) || Zone.is_map_boss(mapId) || Zone.is_map_dungeon(mapId)) {
                return;
            }
        }
        int[] ingredients = new int[]{ITEM_BOT_MI, ITEM_DUONG_TRANG};
        dropEventItemRandom(p, mob, ingredients, 15, LIMIT_DANH_QUAI, IDX_DANH_QUAI);
    }

    @Override
    public void onVanChuyen(Player p) {
        giveMaterial(p, 2, IDX_VAN_BUON, LIMIT_VAN_BUON, "Vận Buôn");
    }

    @Override
    public void onWanted(Player p) {
        giveMaterial(p, 10, IDX_TRUY_NA, LIMIT_TRUY_NA, "Thắng Truy Nã");
    }

    @Override
    public void onVuonCam(Player p, int round) {
        if (round == 10) {
            giveMaterial(p, 5, IDX_VUON_CAM, LIMIT_VUON_CAM, "PB Vườn Cam Nami Vòng 10");
        }
    }

    @Override
    public void onLienTang(Player p, int floor) {
        if (floor == 5) {
            giveMaterial(p, 5, IDX_LIEN_TANG, LIMIT_LIEN_TANG, "PB Liên Tầng Tầng 5");
        }
    }

    @Override
    public boolean onUseItem(Player p, int itemId) throws IOException {
        if (!isEventActive()) {
            if (itemId == ITEM_BANH_ME_HOAC || itemId == ITEM_BANH_SIEU_CAP) {
                p.getService().send_box_ThongBao_OK("Sự kiện Big Mom đã kết thúc!");
                return false;
            }
            return false;
        }
        if (itemId == ITEM_BANH_ME_HOAC || itemId == ITEM_BANH_SIEU_CAP) {
            if (p.level < 40) {
                p.getService().send_box_ThongBao_OK("Cần đạt cấp độ 40 trở lên để cống nạp bánh ngọt cho Big Mom!");
                return false;
            }

            if (p.map == null) return false;

            // Tìm Boss Big Mom trong bản đồ hiện tại của người chơi
            BigMom targetBoss = null;
            BigMom bossInOtherZone = null;
            for (BigMom bm : listBoss) {
                if (bm != null && bm.mob != null && bm.mob.map != null
                        && bm.mob.map.template.id == p.map.template.id) {
                    if (bm.mob.map.zone_id == p.map.zone_id) {
                        if (!bm.mob.isdie && bm.mob.hp > 0) {
                            targetBoss = bm;
                            break;
                        }
                    } else if (!bm.mob.isdie && bm.mob.hp > 0) {
                        bossInOtherZone = bm;
                    }
                }
            }

            // Fallback: Tìm trong map mobs nếu có instance BigMom
            if (targetBoss == null) {
                for (Mob m : p.map.mobs.values()) {
                    if (m != null && m.iMob instanceof BigMom && !m.isdie && m.hp > 0) {
                        targetBoss = (BigMom) m.iMob;
                        break;
                    }
                }
            }

            if (targetBoss == null) {
                if (bossInOtherZone != null && bossInOtherZone.mob != null && bossInOtherZone.mob.map != null) {
                    p.getService().send_box_ThongBao_OK("Big Mom đang ở Khu " + (bossInOtherZone.mob.map.zone_id + 1)
                            + " của bản đồ này!\nHãy chuyển sang Khu " + (bossInOtherZone.mob.map.zone_id + 1) + " để cống nạp bánh.");
                } else {
                    p.getService().send_box_ThongBao_OK("Không tìm thấy Big Mom ở khu vực này!\nHãy tìm Big Mom tại các bản đồ ngoài làng (như 1-1, 2-1...).");
                }
                return false;
            }

            return targetBoss.offerCake(p, itemId);
        }
        return false;
    }
}
