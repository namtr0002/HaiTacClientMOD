package event;

import model.Player;
import core.Manager;
import core.ZUtil;
import itemz.MainItem;
import itemz.MainItemShop;
import itemz.rebuilds.GhepBoHoaRucRo;
import map.Zone;
import mob.Mob;
import model.InputDialog;
import model.YesNoDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SuKienVuLan — SỰ KIỆN VU LAN BÁO HIẾU 2026 (26/08 - 10/09)
 *
 *  NPC: Boa Hancock (tại các Làng khởi đầu) 
 *
 * 1. Menu Sự Kiện:
 *   - Shop đổi quà (Cửa Hàng Vu Lan)
 *   - Tạo Bó Hoa Rực Rỡ (Combie Showtable: 2 Hoa Trắng + 1 Hoa Đỏ + 5 Ruby)
 *   - Hoạt Động Nhận Quà Cán Mốc (Mốc 50, 100, 500, 1000, 2000)
 *   - Mua Hoa Trắng (Dynamic Menu: 10 hoa = 30.000 Beri / 10 hoa = 20 Ruby)
 *   - Mở Bó Hoa Rực Rỡ (1, 5, 10, Tất cả)
 *   - BXH Dâng Hoa Vu Lan
 *   - Hướng dẫn sự kiện
 *   - Tích nạp sự kiện
 *   - Tích tiêu sự kiện
 *
 * 2. Shop Sự Kiện Vu Lan:
 *   - Vé kinh nghiệm đặc biệt 5x = 200 bông hồng trắng (max 99)
 *   - Rương đại ác quỷ 1x = 900 bông hồng đỏ + 50 bó hoa rực rỡ (max 3)
 *   - XP chiêu thức 5x = 200 bông hồng trắng (max 99)
 *   - Bột cường hóa 10x = 100 bông hồng đỏ (max 100)
 *   - Bột vàng 10x = 100 bông hồng đỏ (max 100)
 *   - Đá hải thạch cấp 5 1x = 500 bông hồng trắng + 500 bông hồng đỏ + 10 bó hoa rực rỡ (max 3)
 *   - Rương dial 1x = 900 bông hồng đỏ + 100 bó hoa rực rỡ (max 2)
 *   - Khóa XP 5x = 200 bông hồng trắng
 *   - Mảnh sách haki quan sát 4 1x = 900 bông hồng trắng + 900 bông hồng đỏ + 200 bó hoa rực rỡ (max 2)
 *   - Bông hồng đỏ: 10 hoa đỏ = 10 ruby (max 100 hoa đỏ/ngày = 10 lần mua)
 *
 * 3. Quà Mở Bó Hoa Rực Rỡ:
 *   - Tỉ lệ CAO: Beri (2k-100k), Exp (Level x 5000), Bột cường hóa (1-9), Đá khảm C1 (1-6), Đá khảm C2 (1-3) (~87%)
 *   - Tỉ lệ THẤP: Mảnh Pet Cabibara/Lobby/Lucci x1, Búa đục túi, Rương ác quỷ, Rương đại ác quỷ, Rương dial (~13%)
 *   - 1% cơ hội: Mảnh huy hiệu siêu cấp theo class (Level >= 40)
 *   - +1 Điểm mở hộp mỗi lần mở
 *
 * 4. Sử Dụng Item Sự Kiện (onUseItem):
 *   - Bó hoa rực rỡ (890): Mở chọn số lượng 1, 5, 10, Tất cả
 *   - Hộp thời trang Kyros (906) / Rebecca (907): Mở nhận Thời trang vĩnh viễn
 *   - Hoa hồng trắng (888) / đỏ (889): Mở menu ghép Bó hoa nhanh hoặc gặp Boa Hancock
 *   - Mảnh Pet Capybara (903) / Lobby (904) / Lucci (905): Đủ 100 mảnh ghép Pet vĩnh viễn
 *   - Mảnh Huy Hiệu Siêu Cấp (898-902): Đủ 100 mảnh ghép Huy Hiệu Siêu Cấp vĩnh viễn
 */
public class SuKienVuLan extends Event {

    public static final int ID = Event.ID_SUKIEN_VULAN_2026;

    // Items Sự Kiện
    public static final int ITEM_HOA_HONG_TRANG        = 888;
    public static final int ITEM_HOA_HONG_DO           = 889;
    public static final int ITEM_BO_HOA_RUC_RO         = 890;

    // Mảnh Huy Hiệu Siêu Cấp (theo 5 hệ nhân vật, yêu cầu Lv 40+)
    public static final int ITEM_MANH_HUY_HIEU_VO_SI     = 898;
    public static final int ITEM_MANH_HUY_HIEU_KIEM_KHACH= 899;
    public static final int ITEM_MANH_HUY_HIEU_DAU_BEP   = 900;
    public static final int ITEM_MANH_HUY_HIEU_XA_THU    = 901;
    public static final int ITEM_MANH_HUY_HIEU_HOA_TIEU  = 902;

    // Mảnh Pet
    public static final int ITEM_MANH_PET_CAPYBARA     = 903;
    public static final int ITEM_MANH_PET_LOBBY        = 904;
    public static final int ITEM_MANH_PET_LUCCI        = 905;

    // Hộp Trang Phục Mốc Tối Thượng
    public static final int ITEM_HOP_THOI_TRANG_KYROS   = 906;
    public static final int ITEM_HOP_THOI_TRANG_REBECCA = 907;

    // Fashion IDs
    public static final int FASHION_KYROS              = 153;
    public static final int FASHION_REBECCA            = 154;

    // EventData Data Indexes
    public static final int IDX_DIEM_TICH_LUY          = 10; // Tổng số Bó Hoa đã mở (Điểm mở hộp)
    public static final int IDX_MOC_50                 = 11; // Đã nhận mốc 50 điểm
    public static final int IDX_MOC_100                = 12; // Đã nhận mốc 100 điểm
    public static final int IDX_MOC_500                = 13; // Đã nhận mốc 500 điểm
    public static final int IDX_MOC_1000               = 14; // Đã nhận mốc 1000 điểm
    public static final int IDX_MOC_2000               = 15; // Đã nhận mốc 2000 điểm

    public static final int IDX_DAILY_DANH_QUAI        = 16;
    public static final int IDX_DAILY_VAN_BUON         = 17;
    public static final int IDX_DAILY_NV_LAP          = 18;
    public static final int IDX_DAILY_TRUY_NA          = 19;
    public static final int IDX_DAILY_VUON_CAM         = 20;
    public static final int IDX_DAILY_LIEN_TANG        = 21;
    public static final int IDX_DAILY_BUY_WHITE_BERI   = 22;
    public static final int IDX_DAILY_BUY_WHITE_RUBY   = 23;
    public static final int IDX_DAILY_BUY_RED_RUBY     = 24;

    // Giới hạn hàng ngày
    public static final int LIMIT_DANH_QUAI            = 15;  // 15 hoa/ngày
    public static final int LIMIT_VAN_BUON             = 5;   // 5 lần (10 hoa/lần = 50 hoa/ngày)
    public static final int LIMIT_NV_LAP              = 5;   // 5 lần (10 hoa/lần = 50 hoa/ngày)
    public static final int LIMIT_TRUY_NA              = 5;   // 5 lần (3 hoa/lần = 15 hoa/ngày)
    public static final int LIMIT_VUON_CAM             = 5;   // 5 lần (10 hoa/lần = 50 hoa/ngày)
    public static final int LIMIT_LIEN_TANG            = 5;   // 5 lần (10 hoa/lần = 50 hoa/ngày)
    public static final int LIMIT_BUY_WHITE_BERI       = 50;  // max 50 hoa/ngày (5 lần x 10)
    public static final int LIMIT_BUY_WHITE_RUBY       = 100; // max 100 hoa/ngày (10 lần x 10)
    public static final int LIMIT_BUY_RED_RUBY         = 100; // max 100 hoa/ngày (10 lần x 10)

    private static SuKienVuLan instance;

    public static SuKienVuLan gI() {
        if (instance == null) {
            instance = new SuKienVuLan();
        }
        return instance;
    }

    public SuKienVuLan() {
        super(ID, "Sự Kiện Vu Lan Báo Hiếu 2026");
        this.shopName = "Cửa Hàng Vu Lan";
        this.costItemId = -1; // Sử dụng MainItemShop linh hoạt
        this.costItemType = 4;
        this.sellableItems = new short[0];

        this.time = "0:00:00 26/8/2026 > 23:59:59 10/9/2026";
        this.timex2pay = "0:00:00 26/8/2026 > 23:59:59 10/9/2026";
        this.timechangeitem = "0:00:00 26/8/2026 > 23:59:59 10/9/2026";
        this.timedropitem = "0:00:00 26/8/2026 > 23:59:59 10/9/2026";
        this.timeremoveitem = "0:00:00 26/8/2026 > 23:59:59 10/9/2026";

        this.bxhSubTypes = new int[]{1801, 1802};
        this.bxhNames = new String[]{"Top Hoa Hồng Trắng", "Top Mở Bó Hoa Vu Lan"};

        initShop();
        instance = this;
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            ITEM_HOA_HONG_TRANG, ITEM_HOA_HONG_DO, ITEM_BO_HOA_RUC_RO
        };
    }

    @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienVuLan(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();
        System.out.println("[SuKienVuLan] Init completed with " + shopItems.size() + " shop items.");
    }

    // ======================== HOẠT ĐỘNG THU THẬP HOA ========================

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!EventManager.isActive(this.id) || mob == null || mob.is_boss) return;
        if (!isDropItemActive()) return;

        p = p.getOwnerPlayer();
        if (p.isBot || p.item == null) return;

        if (Math.abs(p.level - mob.level) <= 10) {
            EventData evData = getOrCreateEventData(p);
            if (evData != null && evData.data[IDX_DAILY_DANH_QUAI] < LIMIT_DANH_QUAI) {
                if (ZUtil.random(100) < 15) { // 15% tỷ lệ rơi Hoa hồng trắng
                    evData.data[IDX_DAILY_DANH_QUAI]++;
                    p.item.add_item_bag47(4, ITEM_HOA_HONG_TRANG, 1);
                    p.item.updateInventory(false);
                }
            }
        }
    }

    @Override
    public void onVanChuyen(Player p) {
        if (!EventManager.isActive(this.id) || !isDropItemActive()) return;
        giveRewardItem(p, ITEM_HOA_HONG_TRANG, 10, IDX_DAILY_VAN_BUON, LIMIT_VAN_BUON, "Vận Buôn", "Hoa Hồng Trắng");
    }

    @Override
    public void onNhiemVuLap(Player p) {
        if (!EventManager.isActive(this.id) || !isDropItemActive()) return;
        giveRewardItem(p, ITEM_HOA_HONG_TRANG, 10, IDX_DAILY_NV_LAP, LIMIT_NV_LAP, "Nhiệm Vụ Lặp", "Hoa Hồng Trắng");
    }

    @Override
    public void onWanted(Player p) {
        if (!EventManager.isActive(this.id) || !isDropItemActive()) return;
        giveRewardItem(p, ITEM_HOA_HONG_DO, 3, IDX_DAILY_TRUY_NA, LIMIT_TRUY_NA, "Thắng Truy Nã", "Hoa Hồng Đỏ");
    }

    @Override
    public void onVuonCam(Player p, int round) {
        if (!EventManager.isActive(this.id) || !isDropItemActive()) return;
        if (round >= 10) {
            giveRewardItem(p, ITEM_HOA_HONG_DO, 10, IDX_DAILY_VUON_CAM, LIMIT_VUON_CAM, "PB Vườn Cam Vòng 10", "Hoa Hồng Đỏ");
        }
    }

    @Override
    public void onLienTang(Player p, int floor) {
        if (!EventManager.isActive(this.id) || !isDropItemActive()) return;
        if (floor >= 5) {
            giveRewardItem(p, ITEM_HOA_HONG_DO, 10, IDX_DAILY_LIEN_TANG, LIMIT_LIEN_TANG, "PB Liên Tầng Tầng 5", "Hoa Hồng Đỏ");
        }
    }

    private void giveRewardItem(Player p, int itemId, int amountPerTurn, int dataIndex, int maxTurns, String source, String itemName) {
        if (p == null || p.isBot || p.item == null) return;
        if (p.item.able_bag() < 1) return;

        EventData evData = getOrCreateEventData(p);
        if (evData != null && evData.data[dataIndex] < maxTurns) {
            evData.data[dataIndex]++;
            p.item.add_item_bag47(4, itemId, amountPerTurn);
            p.item.updateInventory(false);
        }
    }

    // ======================== MENU NPC BOA HANCOCK ========================

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!EventManager.isActive(this.id)) return false;
        if (npcId == -888 || npcId == -1055 || npcId == -100 || npcId == ID) { // Boa Hancock NPC
            p.getService().openDynamicMenu(npcId, "Boa Hancock", new String[]{
                "Shop đổi quà",
                "Tạo Bó Hoa Rực Rỡ",
                "Hoạt Động Nhận Quà Cán Mốc",
                "Mua Hoa Trắng",
                "Mở Bó Hoa Rực Rỡ",
                "BXH Hoa Hồng Trắng",
                "BXH Mở Bó Hoa",
                "Hướng dẫn sự kiện",
                "Tích nạp sự kiện",
                "Tích tiêu sự kiện"
            }, null);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!EventManager.isActive(this.id)) return false;
        if (npcId == -888 || npcId == -1055 || npcId == -100 || npcId == ID) {
            switch (index) {
                case 0:
                    openShop(p);
                    break;
                case 1:
                    new GhepBoHoaRucRo().show_table(p);
                    break;
                case 2:
                    showMilestoneHubMenu(p);
                    break;
                case 3:
                    showBuyWhiteFlowerDynamicMenu(p);
                    break;
                case 4:
                    showOpenBouquetMenu(p);
                    break;
                case 5:
                    p.typeBXH = 1801;
                    showEventRank(p);
                    break;
                case 6:
                    p.typeBXH = 1802;
                    showEventRank(p);
                    break;
                case 7: {
                    String help = "SỰ KIỆN VU LAN 2026 (26/08 - 10/09) \n\n"
                            + "Tham gia các hoạt động để nhận Hoa Hồng:\n"
                            + "- Đánh quái: Hoa Trắng (max 15/ngày)\n"
                            + "- Vận buôn: Hoa Trắng (10/lần - max 5/ngày)\n"
                            + "- Hoàn thành NVL: Hoa Trắng (10/lần - max 5/ngày)\n"
                            + "- Thắng truy nã: Hoa Đỏ (3/lần - max 5/ngày)\n"
                            + "- PB Vườn Cam (V-10): Hoa Đỏ (10/lần - max 5/ngày)\n"
                            + "- PB Liên Tầng (T-5): Hoa Đỏ (10/lần - max 5/ngày)\n\n"
                            + "Dùng Hoa Hồng đổi quà tại NPC Boa Hancock\n"
                            + "Shop bán thêm: Hoa Đỏ 10 hoa = 10 ruby (max 100/ngày)\n"
                            + "Menu Mua Hoa Trắng: 10 hoa = 30.000 Beri (max 50/ngày) hoặc 10 hoa = 20 Ruby (max 100/ngày)\n"
                            + "Ghép Bó Hoa Rực Rỡ: 2 Hoa Trắng + 1 Hoa Đỏ + 5 Ruby\n\n"
                            + "Chi tiết xem thêm tại haitactihon.com";
                    p.getService().Help_From_Server(npcId, help);
                    break;
                }
                case 8:
                    showTichNap(p);
                    break;
                case 9:
                    showTichTieu(p);
                    break;
            }
            return true;
        }
        return false;
    }

    // ======================== MUA HOA TRẮNG (DYNAMIC MENU + LAMBDA YESNO) ========================

    public void showBuyWhiteFlowerDynamicMenu(Player p) throws IOException {
        p.getService().openDynamicMenu(-1055, "Mua Hoa Hồng Trắng", new String[]{
            "10 Hoa Trắng (30.000 Beri)",
            "10 Hoa Trắng (20 Ruby)",
            "Đóng"
        }, null);

        p.setyesNoDialog(new YesNoDialog(p, 9889, "Mua Hoa Trắng", "Chọn phương thức mua 10 Hoa Hồng Trắng:",
            new String[]{
                "10 Hoa (30.000 Beri)",
                "10 Hoa (20 Ruby)",
                "Đóng"
            },
            new byte[]{-1, -1, 1},
            val -> {
                try {
                    if (val == 0) {
                        buyWhiteFlowerBeriLambda(p);
                    } else if (val == 1) {
                        buyWhiteFlowerRubyLambda(p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        p.getService().startYesNo();
    }

    private void buyWhiteFlowerBeriLambda(Player p) {
        EventData evData = getOrCreateEventData(p);
        int boughtToday = (evData != null) ? evData.data[IDX_DAILY_BUY_WHITE_BERI] : 0;
        int remaining = Math.max(0, LIMIT_BUY_WHITE_BERI - boughtToday);

        if (remaining < 10) {
            try {
                p.getService().send_box_ThongBao_OK("Bạn đã mua hết giới hạn " + LIMIT_BUY_WHITE_BERI + " Hoa Trắng bằng Beri trong ngày hôm nay!");
            } catch (Exception ignored) {}
            return;
        }

        p.setyesNoDialog(new YesNoDialog(p, 9890, "Xác nhận mua Hoa Trắng",
            "Bạn có đồng ý mua 10 Bông hoa hồng trắng với giá 30.000 Beri?",
            new String[]{"Đồng ý", "Hủy"},
            new byte[]{0, 1},
            val -> {
                if (val == 0) {
                    try {
                        if (p.get_vang() < 30000) {
                            p.getService().send_box_ThongBao_OK("Không đủ Beri! Cần 30.000 Beri (hiện có: " + ZUtil.number_format(p.get_vang()) + ")");
                            return;
                        }
                        if (p.item.able_bag() < 1) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return;
                        }

                        p.update_vang(-30000);
                        evData.data[IDX_DAILY_BUY_WHITE_BERI] += 10;
                        p.updateMoney();
                        List<template.GiftBox> listGift = new ArrayList<>();
                        listGift.add(new template.GiftBox(4, ITEM_HOA_HONG_TRANG, 10));
                        core.RewardService.sendGiftOrMail(p, 1, "Mua Hoa Trắng", "Mua thành công 10 Hoa Trắng", listGift, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        p.getService().startYesNo();
    }

    private void buyWhiteFlowerRubyLambda(Player p) {
        EventData evData = getOrCreateEventData(p);
        int boughtToday = (evData != null) ? evData.data[IDX_DAILY_BUY_WHITE_RUBY] : 0;
        int remaining = Math.max(0, LIMIT_BUY_WHITE_RUBY - boughtToday);

        if (remaining < 10) {
            try {
                p.getService().send_box_ThongBao_OK("Bạn đã mua hết giới hạn " + LIMIT_BUY_WHITE_RUBY + " Hoa Trắng bằng Ruby trong ngày hôm nay!");
            } catch (Exception ignored) {}
            return;
        }

        p.setyesNoDialog(new YesNoDialog(p, 9891, "Xác nhận mua Hoa Trắng",
            "Bạn có đồng ý mua 10 Bông hoa hồng trắng với giá 20 Ruby?",
            new String[]{"Đồng ý", "Hủy"},
            new byte[]{0, 1},
            val -> {
                if (val == 0) {
                    try {
                        if (p.get_ngoc() < 20) {
                            p.getService().send_box_ThongBao_OK("Không đủ Ruby! Cần 20 Ruby (hiện có: " + p.get_ngoc() + ")");
                            return;
                        }
                        if (p.item.able_bag() < 1) {
                            p.getService().send_box_ThongBao_OK("Hành trang không đủ chỗ trống!");
                            return;
                        }

                        p.update_ngoc(-20);
                        evData.data[IDX_DAILY_BUY_WHITE_RUBY] += 10;
                        p.updateMoney();
                        List<template.GiftBox> listGift = new ArrayList<>();
                        listGift.add(new template.GiftBox(4, ITEM_HOA_HONG_TRANG, 10));
                        core.RewardService.sendGiftOrMail(p, 1, "Mua Hoa Trắng", "Mua thành công 10 Hoa Trắng", listGift, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        p.getService().startYesNo();
    }

    // ======================== HOẠT ĐỘNG NHẬN QUÀ CÁN MỐC ========================

    public void showMilestoneHubMenu(Player p) throws IOException {
        EventData evData = getOrCreateEventData(p);
        int points = (evData != null) ? evData.data[IDX_DIEM_TICH_LUY] : 0;

        p.setyesNoDialog(new YesNoDialog(p, 9893, "Hoạt Động Nhận Quà Cán Mốc (Điểm: " + points + ")",
            "Mỗi lần mở 1 Bó Hoa Rực Rỡ được +1 điểm mở hộp.\nĐiểm hiện tại của bạn: " + points + " điểm.",
            new String[]{"Nhận Quà", "Xem thông tin", "Danh sách quà", "Đóng"},
            new byte[]{-1, -1, -1, 1},
            val -> {
                try {
                    if (val == 0) {
                        showClaimMilestoneDialog(p);
                    } else if (val == 1) {
                        showMilestoneInfoHelp(p);
                    } else if (val == 2) {
                        showMilestoneRewardListHelp(p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        p.getService().startYesNo();
    }

    private void showMilestoneInfoHelp(Player p) throws IOException {
        String info = "Boa Hancock\n\n"
                + "CÁCH NHẬN ĐIỂM: Mỗi lần dùng (mở) 1 Bó Hoa Rực Rỡ được +1 điểm mở hộp.\n\n"
                + "Ghép Bó Hoa từ 2 Hoa Trắng + 1 Hoa Đỏ + 5 Ruby tại NPC (Tạo Bó Hoa Rực Rỡ).\n\n"
                + "Điểm KHÔNG tự nhận quà - hãy vào 'Hoạt Động Nhận Quà Cán Mốc' -> 'Nhận Quà' để nhận quà các mốc đã đạt.";
        p.getService().Help_From_Server(-1055, info);
    }

    private void showMilestoneRewardListHelp(Player p) throws IOException {
        String list = "Boa Hancock\n\n"
                + "MỐC ĐIỂM MỞ HỘP - PHẦN THƯỞNG:\n\n"
                + "- Mốc 50: 10 Rương Cam cùng hệ\n"
                + "- Mốc 100: 2 Rương Đại Ác Quỷ\n"
                + "- Mốc 500: 2 Rương Dial\n"
                + "- Mốc 1000: 2 Đá Hải Thạch Cấp 6\n"
                + "- Mốc 2000: Thời Trang Kyros (ReBecca cho Hoa Tiêu) vĩnh viễn";
        p.getService().Help_From_Server(-1055, list);
    }

    private void showClaimMilestoneDialog(Player p) {
        EventData evData = getOrCreateEventData(p);
        int points = (evData != null) ? evData.data[IDX_DIEM_TICH_LUY] : 0;

        String status50 = (evData != null && evData.data[IDX_MOC_50] > 0) ? " [Đã nhận]" : (points >= 50 ? " [Có thể nhận]" : " [" + points + "/50]");
        String status100 = (evData != null && evData.data[IDX_MOC_100] > 0) ? " [Đã nhận]" : (points >= 100 ? " [Có thể nhận]" : " [" + points + "/100]");
        String status500 = (evData != null && evData.data[IDX_MOC_500] > 0) ? " [Đã nhận]" : (points >= 500 ? " [Có thể nhận]" : " [" + points + "/500]");
        String status1000 = (evData != null && evData.data[IDX_MOC_1000] > 0) ? " [Đã nhận]" : (points >= 1000 ? " [Có thể nhận]" : " [" + points + "/1000]");
        String status2000 = (evData != null && evData.data[IDX_MOC_2000] > 0) ? " [Đã nhận]" : (points >= 2000 ? " [Có thể nhận]" : " [" + points + "/2000]");

        p.setyesNoDialog(new YesNoDialog(p, 9892, "Nhận Quà Cán Mốc (Điểm: " + points + ")", "Chọn mốc quà bạn muốn nhận:",
            new String[]{
                "Mốc 50 (10 Rương Cam)" + status50,
                "Mốc 100 (2 Rương Đại Ác Quỷ)" + status100,
                "Mốc 500 (2 Rương Dial)" + status500,
                "Mốc 1000 (2 Đá Hải Thạch C6)" + status1000,
                "Mốc 2000 (Thời Trang VV)" + status2000,
                "Hủy"
            },
            new byte[]{-1, -1, -1, -1, -1, 1},
            val -> {
                try {
                    if (val >= 0 && val <= 4) {
                        claimMilestone(p, val);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        p.getService().startYesNo();
    }

    private void claimMilestone(Player p, int milestoneIndex) throws IOException {
        EventData evData = getOrCreateEventData(p);
        if (evData == null) return;
        int points = evData.data[IDX_DIEM_TICH_LUY];

        switch (milestoneIndex) {
            case 0: { // Mốc 50
                if (evData.data[IDX_MOC_50] > 0) {
                    p.getService().send_box_ThongBao_OK("Bạn đã nhận phần thưởng Mốc 50 điểm rồi!");
                    return;
                }
                if (points < 50) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa đạt đủ 50 điểm tích lũy! (Hiện có: " + points + ")");
                    return;
                }
                evData.data[IDX_MOC_50] = 1;
                int chestId = getOrangeChestByLevel(p.level);
                List<template.GiftBox> giftList = new ArrayList<>();
                giftList.add(new template.GiftBox(4, chestId, 10));
                core.RewardService.sendGiftOrMail(p, 1, "Thưởng Mốc 50 Điểm", "Chúc mừng bạn đã đạt mốc 50 điểm!", giftList, true);
                break;
            }
            case 1: { // Mốc 100
                if (evData.data[IDX_MOC_100] > 0) {
                    p.getService().send_box_ThongBao_OK("Bạn đã nhận phần thưởng Mốc 100 điểm rồi!");
                    return;
                }
                if (points < 100) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa đạt đủ 100 điểm tích lũy! (Hiện có: " + points + ")");
                    return;
                }
                evData.data[IDX_MOC_100] = 1;
                List<template.GiftBox> giftList = new ArrayList<>();
                giftList.add(new template.GiftBox(4, 158, 2)); // 2 Rương Đại Ác Quỷ
                core.RewardService.sendGiftOrMail(p, 1, "Thưởng Mốc 100 Điểm", "Chúc mừng bạn đã đạt mốc 100 điểm!", giftList, true);
                break;
            }
            case 2: { // Mốc 500
                if (evData.data[IDX_MOC_500] > 0) {
                    p.getService().send_box_ThongBao_OK("Bạn đã nhận phần thưởng Mốc 500 điểm rồi!");
                    return;
                }
                if (points < 500) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa đạt đủ 500 điểm tích lũy! (Hiện có: " + points + ")");
                    return;
                }
                evData.data[IDX_MOC_500] = 1;
                List<template.GiftBox> giftList = new ArrayList<>();
                giftList.add(new template.GiftBox(4, 455, 2)); // 2 Rương Dial
                core.RewardService.sendGiftOrMail(p, 1, "Thưởng Mốc 500 Điểm", "Chúc mừng bạn đã đạt mốc 500 điểm!", giftList, true);
                break;
            }
            case 3: { // Mốc 1000
                if (evData.data[IDX_MOC_1000] > 0) {
                    p.getService().send_box_ThongBao_OK("Bạn đã nhận phần thưởng Mốc 1000 điểm rồi!");
                    return;
                }
                if (points < 1000) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa đạt đủ 1000 điểm tích lũy! (Hiện có: " + points + ")");
                    return;
                }
                evData.data[IDX_MOC_1000] = 1;
                List<template.GiftBox> giftList = new ArrayList<>();
                giftList.add(new template.GiftBox(4, 226, 2)); // 2 Đá Hải Thạch Cấp 6
                core.RewardService.sendGiftOrMail(p, 1, "Thưởng Mốc 100 Điểm", "Chúc mừng bạn đã đạt mốc 1000 điểm!", giftList, true);
                break;
            }
            case 4: { // Mốc 2000 (MỐC TỐI THƯỢNG)
                if (evData.data[IDX_MOC_2000] > 0) {
                    p.getService().send_box_ThongBao_OK("Bạn đã nhận phần thưởng Mốc Tối Thượng 2000 điểm rồi!");
                    return;
                }
                if (points < 2000) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa đạt đủ 2000 điểm tích lũy! (Hiện có: " + points + ")");
                    return;
                }
                evData.data[IDX_MOC_2000] = 1;

                int fashionId = (p.clazz == 5) ? FASHION_REBECCA : FASHION_KYROS;
                String fashionName = (p.clazz == 5) ? "Thời Trang Vĩnh Viễn REBECCA" : "Thời Trang Vĩnh Viễn KYROS";

                List<template.GiftBox> giftList = new ArrayList<>();
                giftList.add(new template.GiftBox(105, fashionId, 1));

                core.RewardService.sendGiftOrMail(p, 1, "MỐC TỐI THƯỢNG VU LAN 2026", "Chúc mừng bạn đã chạm mốc 2000 Điểm và nhận được " + fashionName + "!", giftList, true);
                Manager.gI().chatKTG(0, "Vinh danh người chơi [" + p.name + "] đã xuất sắc đạt MỐC TỐI THƯỢNG 2.000 điểm Vu Lan 2026 và sở hữu [" + fashionName + "]!", 0);
                break;
            }
        }
    }

    private int getOrangeChestByLevel(int level) {
        if (level < 20) return 122;      // Lv10
        else if (level < 30) return 123; // Lv20
        else if (level < 40) return 124; // Lv30
        else if (level < 50) return 125; // Lv40
        else if (level < 60) return 126; // Lv50
        else if (level < 70) return 127; // Lv60
        else if (level < 80) return 128; // Lv70
        else if (level < 90) return 129; // Lv80
        else if (level < 100) return 130;// Lv90
        else return 131;                 // Lv100
    }

    // ======================== MỞ BÓ HOA RỰC RỠ ========================

    public void showOpenBouquetMenu(Player p) {
        int total = p.item.total_item_bag_by_id(4, ITEM_BO_HOA_RUC_RO);
        if (total <= 0) {
            try {
                p.getService().send_box_ThongBao_OK("Hành trang của bạn không có Bó Hoa Rực Rỡ nào!");
            } catch (Exception ignored) {}
            return;
        }

        p.setyesNoDialog(new YesNoDialog(p, 9891, "Mở Bó Hoa Rực Rỡ", "Bạn đang có " + total + " Bó Hoa Rực Rỡ. Bạn muốn mở bao nhiêu bó?",
            new String[]{"Mở 1 Bó", "Mở 5 Bó", "Mở 10 Bó", "Mở Tất Cả (" + total + ")", "Hủy"},
            new byte[]{-1, -1, -1, -1, 1},
            val -> {
                try {
                    int toOpen = 0;
                    if (val == 0) toOpen = 1;
                    else if (val == 1) toOpen = 5;
                    else if (val == 2) toOpen = 10;
                    else if (val == 3) toOpen = total;

                    if (toOpen > 0) {
                        openBouquet(p, toOpen);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }));
        p.getService().startYesNo();
    }

    public void openBouquet(Player p, int quantity) throws IOException {
        if (p == null || p.item == null || p.isBot) return;
        int have = p.item.total_item_bag_by_id(4, ITEM_BO_HOA_RUC_RO);
        if (have < quantity) {
            quantity = have;
        }
        if (quantity <= 0) {
            p.getService().send_box_ThongBao_OK("Bạn không có đủ Bó Hoa Rực Rỡ!");
            return;
        }

        p.item.remove_item47(4, ITEM_BO_HOA_RUC_RO, quantity);
        p.item.updateInventory(false);

        EventData evData = getOrCreateEventData(p);
        if (evData != null) {
            evData.data[IDX_DIEM_TICH_LUY] += quantity;
        }

        // 100% Nhận EXP theo cấp
        long totalExp = (long) p.level * 5000L * quantity;
        long totalBeri = 0;
        List<MainItem> listGifts = new ArrayList<>();

        // Đá khảm C1: 44, 50, 56, 62, 68, 74
        int[] daKhamC1 = {44, 50, 56, 62, 68, 74};
        // Đá khảm C2: 45, 51, 57, 63, 69, 75
        int[] daKhamC2 = {45, 51, 57, 63, 69, 75};

        for (int i = 0; i < quantity; i++) {
            // 100% Nhận Beri: Min 2.000 Beri, max 100.000 Beri
            int gold = ZUtil.random(2000, 100000);
            totalBeri += gold;

            int roll = ZUtil.random(1000);

            // ==================== TỈ LỆ CAO (~87%) ====================
            // 1. Bột Cường Hóa (1 - 9): 35%
            if (roll < 350) {
                int botQty = ZUtil.random(1, 9);
                addGiftToList(listGifts, 1, 7, botQty);
            }
            // 2. Đá Khảm C1 (1 - 6): 30%
            else if (roll < 650) {
                int daId = daKhamC1[ZUtil.random(daKhamC1.length)];
                int daQty = ZUtil.random(1, 6);
                addGiftToList(listGifts, daId, 4, daQty);
            }
            // 3. Đá Khảm C2 (1 - 3): 22%
            else if (roll < 870) {
                int daId = daKhamC2[ZUtil.random(daKhamC2.length)];
                int daQty = ZUtil.random(1, 3);
                addGiftToList(listGifts, daId, 4, daQty);
            }
            // ==================== TỈ LỆ THẤP (~13%) ====================
            // 4. Mảnh ghép Pet Cabibara / Lobby / Lucci x1: 5.5%
            else if (roll < 925) {
                int petRoll = ZUtil.random(3);
                int petShardId = (petRoll == 0) ? ITEM_MANH_PET_CAPYBARA : (petRoll == 1 ? ITEM_MANH_PET_LOBBY : ITEM_MANH_PET_LUCCI);
                addGiftToList(listGifts, petShardId, 4, 1);
            }
            // 5. Búa đục túi: 3.5%
            else if (roll < 960) {
                addGiftToList(listGifts, 416, 4, 1);
            }
            // 6. Rương Ác Quỷ: 2.5%
            else if (roll < 985) {
                addGiftToList(listGifts, 29, 4, 1);
            }
            // 7. Rương Đại Ác Quỷ: 1.0%
            else if (roll < 995) {
                addGiftToList(listGifts, 158, 4, 1);
            }
            // 8. Rương Dial: 0.5% (Siêu phẩm KTG)
            else {
                addGiftToList(listGifts, 455, 4, 1);
                Manager.gI().chatKTG(0, "Chúc mừng người chơi [" + p.name + "] đã may mắn nhận được [Rương Dial] siêu phẩm từ Bó Hoa Rực Rỡ mùa Vu Lan 2026!", 0);
            }

            // 1% Cơ hội độc lập: MẢNH HUY HIỆU SIÊU CẤP THEO CLASS (Level >= 40)
            if (p.level >= 40 && ZUtil.random(100) < 1) { // 1%
                int badgeId = getBadgeIdByClass(p.clazz);
                addGiftToList(listGifts, badgeId, 4, 1);
            }
        }

        // Chuyển sang List<GiftBox> chuẩn bao gồm cả EXP và Beri
        List<template.GiftBox> finalGifts = new ArrayList<>();
        finalGifts.add(new template.GiftBox(99, 0, (int) Math.min(Integer.MAX_VALUE, totalExp)));
        if (totalBeri > 0) {
            finalGifts.add(new template.GiftBox(4, 0, (int) Math.min(Integer.MAX_VALUE, totalBeri)));
        }
        for (MainItem gift : listGifts) {
            finalGifts.add(new template.GiftBox(gift.cat, gift.id, gift.num));
        }

        // Show gift box
        int currentPoints = (evData != null) ? evData.data[IDX_DIEM_TICH_LUY] : 0;
        String title = "Mở " + quantity + " Bó Hoa Rực Rỡ";
        String info = "Nhận được: " + ZUtil.number_format(totalExp) + " Exp"
                + (totalBeri > 0 ? ("\n" + ZUtil.number_format(totalBeri) + " Beri") : "")
                + "\nĐiểm mở bó hoa rực rỡ: " + currentPoints + " điểm";

        core.RewardService.sendGiftOrMail(p, 1, title, info, finalGifts, true);
    }

    private void addGiftToList(List<MainItem> list, int id, int cat, int num) {
        for (MainItem item : list) {
            if (item.id == id && item.cat == cat) {
                item.num += num;
                return;
            }
        }
        list.add(new MainItem(id, cat, num));
    }

    private int getBadgeIdByClass(int clazz) {
        switch (clazz) {
            case 1: return ITEM_MANH_HUY_HIEU_VO_SI;
            case 2: return ITEM_MANH_HUY_HIEU_KIEM_KHACH;
            case 3: return ITEM_MANH_HUY_HIEU_DAU_BEP;
            case 4: return ITEM_MANH_HUY_HIEU_XA_THU;
            case 5: return ITEM_MANH_HUY_HIEU_HOA_TIEU;
            default: return ITEM_MANH_HUY_HIEU_VO_SI;
        }
    }

    // ======================== DÙNG ITEM SỰ KIỆN ========================

    @Override
    public boolean onUseItem(Player p, int id) throws IOException {
        if (!isEventActive()) {
            if (id == ITEM_BO_HOA_RUC_RO || id == ITEM_HOP_THOI_TRANG_KYROS || id == ITEM_HOP_THOI_TRANG_REBECCA || id == ITEM_HOA_HONG_TRANG || id == ITEM_HOA_HONG_DO) {
                p.getService().send_box_ThongBao_OK("Sự kiện Vu Lan đã kết thúc!");
                return false;
            }
            return false;
        }

        // 1. Mở Bó hoa rực rỡ
        if (id == ITEM_BO_HOA_RUC_RO) {
            int total = p.item.total_item_bag_by_id(4, ITEM_BO_HOA_RUC_RO);
            if (total > 1) {
                showOpenBouquetMenu(p);
            } else {
                openBouquet(p, 1);
            }
            return false;
        }
        // 2. Mở Hộp Thời Trang Kyros
        else if (id == ITEM_HOP_THOI_TRANG_KYROS) {
            if (p.item.total_item_bag_by_id(4, id) < 1) return false;
            p.item.remove_item47(4, id, 1);
            p.item.updateInventory(false);

            List<MainItem> giftList = new ArrayList<>();
            MainItem fashionItem = new MainItem(FASHION_KYROS, 105, 1);
            fashionItem.time = -1; // Vĩnh viễn
            giftList.add(fashionItem);
            MainItem.showGiftBox(p, "Thời Trang Kyros", "Bạn đã mở Hộp Thời Trang Kyros vĩnh viễn!", giftList, true, true);
            return true;
        }
        // 3. Mở Hộp Thời Trang Rebecca
        else if (id == ITEM_HOP_THOI_TRANG_REBECCA) {
            if (p.item.total_item_bag_by_id(4, id) < 1) return false;
            p.item.remove_item47(4, id, 1);
            p.item.updateInventory(false);

            List<MainItem> giftList = new ArrayList<>();
            MainItem fashionItem = new MainItem(FASHION_REBECCA, 105, 1);
            fashionItem.time = -1; // Vĩnh viễn
            giftList.add(fashionItem);
            MainItem.showGiftBox(p, "Thời Trang Rebecca", "Bạn đã mở Hộp Thời Trang Rebecca vĩnh viễn!", giftList, true, true);
            return true;
        }
        // 4. Hoa hồng trắng & Hoa hồng đỏ
        else if (id == ITEM_HOA_HONG_TRANG || id == ITEM_HOA_HONG_DO) {
            p.getService().openDynamicMenu(-1055, "Hoa Hồng Sự Kiện", new String[]{
                "Tạo Bó Hoa Rực Rỡ",
                "Đến gặp Boa Hancock",
                "Đóng"
            }, null);
            p.setyesNoDialog(new YesNoDialog(p, 9895, "Hoa Hồng Sự Kiện Vu Lan",
                "Hoa Hồng dùng để đổi quà tại NPC Boa Hancock hoặc ghép thành Bó Hoa Rực Rỡ (2 Hoa Trắng + 1 Hoa Đỏ + 5 Ruby).",
                new String[]{"Ghép Bó Hoa", "Đến gặp Boa Hancock", "Đóng"},
                new byte[]{-1, -1, 1},
                val -> {
                    try {
                        if (val == 0) {
                            new GhepBoHoaRucRo().show_table(p);
                        } else if (val == 1) {
                            sendMenu(p, -1055);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }));
            p.getService().startYesNo();
            return false;
        }
        // 5. Mảnh ghép Pet Capybara / Lobby / Lucci
        else if (id == ITEM_MANH_PET_CAPYBARA || id == ITEM_MANH_PET_LOBBY || id == ITEM_MANH_PET_LUCCI) {
            handleCraftPet(p, id);
            return false;
        }
        // 6. Mảnh Huy Hiệu Siêu Cấp theo Class
        else if (id >= ITEM_MANH_HUY_HIEU_VO_SI && id <= ITEM_MANH_HUY_HIEU_HOA_TIEU) {
            handleCraftSuperBadge(p, id);
            return false;
        }

        return false;
    }

    private void handleCraftPet(Player p, int shardId) throws IOException {
        int count = p.item.total_item_bag_by_id(4, shardId);
        String petName;
        int petId;
        if (shardId == ITEM_MANH_PET_CAPYBARA) {
            petName = "Pet Capybara";
            petId = 709;
        } else if (shardId == ITEM_MANH_PET_LOBBY) {
            petName = "Pet Lobby";
            petId = 710;
        } else {
            petName = "Pet Lucci";
            petId = 711;
        }

        if (count < 100) {
            p.getService().send_box_ThongBao_OK("Bạn đang có " + count + "/100 Mảnh " + petName + ".\nHãy thu thập đủ 100 Mảnh từ Bó Hoa Rực Rỡ để ghép Pet vĩnh viễn!");
            return;
        }

        p.setyesNoDialog(new YesNoDialog(p, 9896, "Ghép " + petName,
            "Bạn đang có " + count + " Mảnh " + petName + ".\nBạn có muốn dùng 100 Mảnh để ghép thành " + petName + " vĩnh viễn không?",
            new String[]{"Ghép ngay", "Hủy"},
            new byte[]{0, 1},
            val -> {
                if (val == 0) {
                    try {
                        if (p.item.total_item_bag_by_id(4, shardId) < 100) {
                            p.getService().send_box_ThongBao_OK("Không đủ 100 Mảnh!");
                            return;
                        }
                        p.item.remove_item47(4, shardId, 100);
                        p.item.updateInventory(false);

                        List<MainItem> petGifts = new ArrayList<>();
                        MainItem petItem = new MainItem(petId, 110, 1);
                        petItem.time = -1; // Vĩnh viễn
                        petGifts.add(petItem);

                        MainItem.showGiftBox(p, "Ghép Pet Thành Công", "Chúc mừng bạn đã sở hữu " + petName + " vĩnh viễn!", petGifts, true, true);
                        Manager.gI().chatKTG(0, "Chúc mừng người chơi [" + p.name + "] đã ghép thành công [" + petName + "] vĩnh viễn mùa Vu Lan 2026!", 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        p.getService().startYesNo();
    }

    private void handleCraftSuperBadge(Player p, int shardId) throws IOException {
        int count = p.item.total_item_bag_by_id(4, shardId);
        String badgeName;
        switch (shardId) {
            case ITEM_MANH_HUY_HIEU_VO_SI: badgeName = "Huy Hiệu Siêu Võ Sĩ"; break;
            case ITEM_MANH_HUY_HIEU_KIEM_KHACH: badgeName = "Huy Hiệu Siêu Kiếm Khách"; break;
            case ITEM_MANH_HUY_HIEU_DAU_BEP: badgeName = "Huy Hiệu Siêu Đầu Bếp"; break;
            case ITEM_MANH_HUY_HIEU_XA_THU: badgeName = "Huy Hiệu Siêu Xạ Thủ"; break;
            case ITEM_MANH_HUY_HIEU_HOA_TIEU: badgeName = "Huy Hiệu Siêu Hoa Tiêu"; break;
            default: badgeName = "Huy Hiệu Siêu Cấp"; break;
        }

        if (count < 100) {
            p.getService().send_box_ThongBao_OK("Bạn đang có " + count + "/100 Mảnh " + badgeName + ".\nHãy thu thập đủ 100 Mảnh từ Bó Hoa Rực Rỡ để ghép Huy Hiệu Siêu Cấp!");
            return;
        }

        p.setyesNoDialog(new YesNoDialog(p, 9897, "Ghép " + badgeName,
            "Bạn đang có " + count + " Mảnh " + badgeName + ".\nBạn có muốn dùng 100 Mảnh để ghép thành " + badgeName + " không?",
            new String[]{"Ghép ngay", "Hủy"},
            new byte[]{0, 1},
            val -> {
                if (val == 0) {
                    try {
                        if (p.item.total_item_bag_by_id(4, shardId) < 100) {
                            p.getService().send_box_ThongBao_OK("Không đủ 100 Mảnh!");
                            return;
                        }
                        p.item.remove_item47(4, shardId, 100);
                        p.item.updateInventory(false);

                        List<template.GiftBox> badgeGifts = new ArrayList<>();
                        badgeGifts.add(new template.GiftBox(4, 188, 1));
                        core.RewardService.sendGiftOrMail(p, 1, "Ghép Huy Hiệu", "Chúc mừng bạn đã ghép thành công 1 " + badgeName + "!", badgeGifts, true);
                        Manager.gI().chatKTG(0, "Chúc mừng cao thủ [" + p.name + "] đã ghép thành công [" + badgeName + "] siêu cấp!", 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        p.getService().startYesNo();
    }
}
