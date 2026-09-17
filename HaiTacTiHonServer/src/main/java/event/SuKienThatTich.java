package event;

import model.Player;
import core.Manager;
import core.ZUtil;
import itemz.MainItem;
import itemz.MainItemShop;
import mob.Mob;
import map.Zone;
import model.YesNoDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SuKienThatTich — Sự Kiện Thất Tịch Ngưu Lang Chức Nữ (Event ID 19).
 * - Vật phẩm chuẩn:
 *   + 569: Chè đậu đỏ (Icon 543, Type 4)
 *   + 570: Chè đậu đỏ đặc biệt (Icon 544, Type 4)
 *   + 571: Gỗ (Icon 545, Type 4)
 *   + 572: Đá (Icon 546, Type 4)
 *   + 573: Lông chim thước (Icon 547, Type 4)
 *   + 574: Phiếu giao hàng (Icon 550, Type 4)
 *   + 575: Giấy gói quà (Icon 104, Type 4)
 *   + 576: Hộp bánh thượng hạng (Icon 165, Type 4)
 *   + 24:  Chim Thước (Icon 549, Type 8)
 * - BXH:
 *   + SubType 1901: Top Nấu Chè Đậu Đỏ (point_event1)
 *   + SubType 1902: Top Xây Cầu Ô Thước (point_event2)
 */
public class SuKienThatTich extends Event {

    public static final int ID = Event.ID_SUKIEN_THATTICH;

    // Standard items
    public static final int ITEM_CHE_DAU_DO          = 569;
    public static final int ITEM_CHE_DAU_DO_DAC_BIET = 570;
    public static final int ITEM_GO                  = 571;
    public static final int ITEM_DA                  = 572;
    public static final int ITEM_LONG_CHIM_THUOC     = 573;
    public static final int ITEM_PHIEU_GIAO_HANG     = 574;
    public static final int ITEM_GIAY_GOI_QUA        = 575;
    public static final int ITEM_HOP_BANH_THUONG_HANG= 576;
    public static final int ITEM_CHIM_THUOC          = 24;

    private static SuKienThatTich instance;

    public static SuKienThatTich gI() {
        if (instance == null) {
            instance = new SuKienThatTich();
        }
        return instance;
    }

    public SuKienThatTich() {
        super(ID, "Sự Kiện Thất Tịch 2026");
        this.shopName = "Cửa Hàng Thất Tịch";
        this.costItemId = ITEM_CHE_DAU_DO;
        this.costItemType = 4;
        this.bxhSubTypes = new int[]{1901, 1902};
        this.bxhNames = new String[]{"Top Nấu Chè Đậu Đỏ", "Top Xây Cầu Ô Thước"};
        this.time = "0:00:00 01/08/2026 > 23:59:59 31/08/2026";
        this.timex2pay = "0:00:00 01/08/2026 > 23:59:59 31/08/2026";
        this.timechangeitem = "0:00:00 01/08/2026 > 23:59:59 31/08/2026";
        this.timedropitem = "0:00:00 01/08/2026 > 23:59:59 31/08/2026";
        this.timeremoveitem = "0:00:00 01/08/2026 > 23:59:59 31/08/2026";
        this.sellableItems = new short[0];
        initShop();
    }

    @Override
    public int[] getEventItemsToRemove() {
        return new int[]{
            ITEM_CHE_DAU_DO, ITEM_CHE_DAU_DO_DAC_BIET,
            ITEM_GO, ITEM_DA, ITEM_LONG_CHIM_THUOC,
            ITEM_PHIEU_GIAO_HANG
        };
    }

        @Override
    public void initShop() {
        if (this.eventShop == null) {
            this.eventShop = new event.shop.ShopSuKienThatTich(this);
        }
        this.eventShop.initShop();
        this.shopItems = new java.util.ArrayList<>(this.eventShop.getShopItems());
    }

    @Override
    public void init() throws Exception {
        initShop();
        System.out.println("[SuKienThatTich] Init completed with items 569-576 & Season MD5 Key: " + getSeasonKey());
    }

    @Override
    public boolean sendMenu(Player p, int npcId) throws IOException {
        if (!EventManager.isActive(ID)) return false;
        if (npcId == -100 || npcId == -1048 || npcId == ID) {
            p.menus.clear();
            p.menus.add(new model.Menu("Cửa Hàng Thất Tịch", (short) 104, () -> {
                try { openShop(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("Nấu Chè Đậu Đỏ", (short) 165, () -> {
                try {
                    new itemz.rebuilds.GhepCheDauDo().show_table(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            p.menus.add(new model.Menu("Nấu Chè Đặc Biệt", (short) 165, () -> {
                try {
                    new itemz.rebuilds.GhepCheDauDoDacBiet().show_table(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            p.menus.add(new model.Menu("Xây Cầu Ô Thước", (short) 165, () -> {
                showMenuXayCau(p);
            }));
            p.menus.add(new model.Menu("Gói Hộp Bánh", (short) 165, () -> {
                try {
                    new itemz.rebuilds.GhepHopBanhThuongHang().show_table(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            p.menus.add(new model.Menu("BXH Nấu Chè", (short) 141, () -> {
                p.typeBXH = 1901;
                showEventRank(p);
            }));
            p.menus.add(new model.Menu("BXH Xây Cầu Ô Thước", (short) 141, () -> {
                p.typeBXH = 1902;
                showEventRank(p);
            }));
            p.menus.add(new model.Menu("Hướng dẫn", (short) 123, () -> {
                try {
                    String txt = "SỰ KIỆN LỄ THẤT TỊCH - NGƯU LANG CHỨC NỮ\n"
                            + "1. THU THẬP NGUYÊN LIỆU:\n"
                            + "- Đánh quái rơi: Gỗ, Đá, Lông chim thước, Giấy gói quà.\n"
                            + "- Hoạt động phó bản, liên tầng, vận chuyển nhận: Phiếu giao hàng, Chim Thước, nguyên liệu.\n"
                            + "2. HOẠT ĐỘNG CHÍNH:\n"
                            + "- Nấu Chè Đậu Đỏ (569): 5 Gỗ + 5 Đá + 5 Lông chim thước + 10.000 Beri -> +1 Điểm Top Nấu Chè.\n"
                            + "- Nấu Chè Đậu Đỏ Đặc Biệt (570): 5 Gỗ + 5 Đá + 5 Lông chim thước + 5 Ruby -> +2 Điểm Top Nấu Chè.\n"
                            + "- Xây Cầu Ô Thước: Dùng Gỗ, Đá, Lông chim hoặc Chim Thước để nối cầu Ô Thước nhận quà VIP và điểm Top Xây Cầu!\n"
                            + "- Gói Hộp Bánh Thượng Hạng (576): 5 Giấy gói quà + 2 Chè đậu đỏ -> Hộp bánh quà VIP.\n"
                            + "3. SHOP SỰ KIỆN:\n"
                            + "- Đổi Chè Đậu Đỏ lấy Thời Trang Ngưu Lang/Chức Nữ, Rương TAQ, Sách Haki, Vé vòng quay!";
                    p.getService().Help_From_Server(npcId, txt);
                } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("Tích nạp sự kiện", (short) 134, () -> {
                try { showTichNap(p); } catch (Exception e) { e.printStackTrace(); }
            }));
            p.menus.add(new model.Menu("Tích tiêu sự kiện", (short) 135, () -> {
                try { showTichTieu(p); } catch (Exception e) { e.printStackTrace(); }
            }));

            p.getService().openDynamicMenu(npcId, "Sự Kiện Thất Tịch", p.menus);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMenu(Player p, int npcId, int index) throws IOException {
        if (!EventManager.isActive(ID)) return false;
        if (npcId == -100 || npcId == -1048 || npcId == ID) {
            if (p.menus != null && index >= 0 && index < p.menus.size()) {
                p.menus.get(index).execute(p, index);
                return true;
            }
        }
        return false;
    }

    private void showMenuXayCau(Player p) {
        p.setyesNoDialog(new YesNoDialog(p, 9951, "Xây Cầu Ô Thước", "Chọn hình thức xây cầu Ô Thước:",
            new String[]{"Xây Cầu Thường", "Xây Cầu VIP", "Bảng Ghép Đồ", "Hủy"}, new byte[]{-1, -1, -1, 1},
            value -> {
                try {
                    if (value == 0) {
                        buildBridgeNormal(p);
                    } else if (value == 1) {
                        buildBridgeVip(p);
                    } else if (value == 2) {
                        new itemz.rebuilds.XayCauOThuoc().show_table(p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        p.getService().startYesNo();
    }

    public void cookCheDauDo(Player p) {
        try {
            if (p.item.total_item_bag_by_id(4, ITEM_GO) < 5 || p.item.total_item_bag_by_id(4, ITEM_DA) < 5 || p.item.total_item_bag_by_id(4, ITEM_LONG_CHIM_THUOC) < 5) {
                p.getService().send_box_ThongBao_OK("Cần 5 Gỗ, 5 Đá và 5 Lông chim thước để nấu 1 Bát Chè Đậu Đỏ!");
                return;
            }
            if (p.get_vang() < 10_000) {
                p.getService().send_box_ThongBao_OK("Cần 10.000 Beri làm công nấu chè!");
                return;
            }
            p.item.remove_item47(4, ITEM_GO, 5);
            p.item.remove_item47(4, ITEM_DA, 5);
            p.item.remove_item47(4, ITEM_LONG_CHIM_THUOC, 5);
            p.update_vang(-10_000);
            p.update_pointEvent1(1);
            p.updateMoney();
            List<template.GiftBox> listGift = new ArrayList<>();
            listGift.add(new template.GiftBox(4, ITEM_CHE_DAU_DO, 1));
            core.RewardService.sendGiftOrMail(p, 1, "Nấu Chè Đậu Đỏ", "Nấu thành công 1 Bát Chè Đậu Đỏ (+1 Điểm Top)", listGift, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cookCheDauDoDacBiet(Player p) {
        try {
            if (p.item.total_item_bag_by_id(4, ITEM_GO) < 5 || p.item.total_item_bag_by_id(4, ITEM_DA) < 5 || p.item.total_item_bag_by_id(4, ITEM_LONG_CHIM_THUOC) < 5) {
                p.getService().send_box_ThongBao_OK("Cần 5 Gỗ, 5 Đá và 5 Lông chim thước để nấu 1 Bát Chè Đậu Đỏ Đặc Biệt!");
                return;
            }
            if (p.get_ngoc() < 5) {
                p.getService().send_box_ThongBao_OK("Cần 5 Ruby để nấu Chè Đậu Đỏ Đặc Biệt!");
                return;
            }
            p.item.remove_item47(4, ITEM_GO, 5);
            p.item.remove_item47(4, ITEM_DA, 5);
            p.item.remove_item47(4, ITEM_LONG_CHIM_THUOC, 5);
            p.update_ngoc(-5);
            p.update_pointEvent1(2);
            p.updateMoney();
            List<template.GiftBox> listGift = new ArrayList<>();
            listGift.add(new template.GiftBox(4, ITEM_CHE_DAU_DO_DAC_BIET, 1));
            core.RewardService.sendGiftOrMail(p, 1, "Nấu Chè Đậu Đỏ", "Nấu thành công 1 Bát Chè Đậu Đỏ Đặc Biệt (+2 Điểm Top)", listGift, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildBridgeNormal(Player p) {
        try {
            if (p.item.total_item_bag_by_id(4, ITEM_GO) < 10 || p.item.total_item_bag_by_id(4, ITEM_DA) < 10 || p.item.total_item_bag_by_id(4, ITEM_LONG_CHIM_THUOC) < 10) {
                p.getService().send_box_ThongBao_OK("Cần có ít nhất 10 Gỗ, 10 Đá và 10 Lông chim thước để Xây Cầu Ô Thước!");
                return;
            }
            if (p.get_vang() < 20_000) {
                p.getService().send_box_ThongBao_OK("Cần 20.000 Beri để xây cầu!");
                return;
            }

            p.item.remove_item47(4, ITEM_GO, 10);
            p.item.remove_item47(4, ITEM_DA, 10);
            p.item.remove_item47(4, ITEM_LONG_CHIM_THUOC, 10);
            p.update_vang(-20_000);

            p.update_pointEvent2(1);
            p.item.updateInventory(false);
            p.updateMoney();

            long exp = p.level * 8000L;
            p.update_exp(exp, true);

            List<MainItem> listGift = new ArrayList<>();
            listGift.add(new MainItem(ITEM_CHE_DAU_DO, 4, 1));
            if (ZUtil.random(100) < 40) {
                listGift.add(new MainItem(135, 4, ZUtil.random(1, 3))); // Tinh thể đá
            }
            if (ZUtil.random(100) < 25) {
                listGift.add(new MainItem(1, 7, ZUtil.random(2, 5)));   // Bột cường hóa
            }
            if (ZUtil.random(100) < 20) {
                listGift.add(new MainItem(ITEM_PHIEU_GIAO_HANG, 4, 1)); // Phiếu giao hàng
            }
            if (ZUtil.random(100) < 10) {
                listGift.add(new MainItem(122, 4, 1));                  // Rương Cam Cùng Hệ Lv10
            }

            MainItem.showGiftBox(p, "Xây Cầu Ô Thước", "Xây cầu thành công! +1 Điểm Top Xây Cầu Ô Thước", listGift, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildBridgeVip(Player p) {
        try {
            boolean hasChim8 = p.item.total_item_bag_by_id(8, ITEM_CHIM_THUOC) >= 1;
            boolean hasChim4 = p.item.total_item_bag_by_id(4, ITEM_CHIM_THUOC) >= 1;
            boolean useChim = hasChim8 || hasChim4;
            if (!useChim && p.get_ngoc() < 10) {
                p.getService().send_box_ThongBao_OK("Cần 1 Chim Thước hoặc 10 Ruby để Xây Cầu Ô Thước VIP!");
                return;
            }

            if (useChim) {
                if (hasChim8) {
                    p.item.remove_item47(8, ITEM_CHIM_THUOC, 1);
                } else {
                    p.item.remove_item47(4, ITEM_CHIM_THUOC, 1);
                }
            } else {
                if (p.item.total_item_bag_by_id(4, ITEM_LONG_CHIM_THUOC) < 10) {
                    p.getService().send_box_ThongBao_OK("Cần thêm 10 Lông chim thước để xây cầu VIP!");
                    return;
                }
                p.item.remove_item47(4, ITEM_LONG_CHIM_THUOC, 10);
                p.update_ngoc(-10);
            }

            p.update_pointEvent2(3);
            p.item.updateInventory(false);
            p.updateMoney();

            long exp = p.level * 25000L;
            p.update_exp(exp, true);

            List<MainItem> listGift = new ArrayList<>();
            listGift.add(new MainItem(ITEM_CHE_DAU_DO_DAC_BIET, 4, 1));
            listGift.add(new MainItem(ITEM_HOP_BANH_THUONG_HANG, 4, 1));
            if (ZUtil.random(100) < 50) {
                listGift.add(new MainItem(866, 4, ZUtil.random(1, 3))); // Vé vòng quay
            }
            if (ZUtil.random(100) < 30) {
                listGift.add(new MainItem(823, 4, 1));                  // Rương Dial Truyền Thuyết
            }
            if (ZUtil.random(100) < 15) {
                listGift.add(new MainItem(690, 4, 1));                  // Rương TAQ Tự Chọn
            }
            if (ZUtil.random(100) < 10) {
                listGift.add(new MainItem(754, 4, 1));                  // Sách Haki Bá Vương
            }
            if (ZUtil.random(100) < 5) {
                listGift.add(new MainItem(ZUtil.random(100) < 50 ? 132 : 130, 105, 1)); // Thời Trang Ngưu Lang / Chức Nữ
            }

            MainItem.showGiftBox(p, "Cầu Ô Thước VIP", "Nối nhịp Cầu Ô Thước VIP thành công! +3 Điểm Top Xây Cầu", listGift, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUseItem(Player p, int id) throws IOException {
        if (!isEventActive()) {
            if (id == ITEM_CHE_DAU_DO || id == ITEM_CHE_DAU_DO_DAC_BIET || id == ITEM_PHIEU_GIAO_HANG || id == ITEM_HOP_BANH_THUONG_HANG || id == ITEM_CHIM_THUOC) {
                p.getService().send_box_ThongBao_OK("Sự kiện Thất Tịch đã kết thúc!");
                return false;
            }
            return false;
        }

        if (id == ITEM_CHE_DAU_DO) {
            if (p.item.total_item_bag_by_id(4, ITEM_CHE_DAU_DO) < 1) {
                p.getService().send_box_ThongBao_OK("Bạn không có Bát Chè Đậu Đỏ!");
                return false;
            }
            
            p.item.remove_item47(4, ITEM_CHE_DAU_DO, 1);
            p.item.updateInventory(false);

            long exp = p.level * 5000L;
            int beri = ZUtil.random(50_000, 150_000);
            p.update_vang(beri);
            p.update_exp(exp, true);
            p.updateMoney();

            List<MainItem> gifts = new ArrayList<>();
            gifts.add(new MainItem(ITEM_GO, 4, ZUtil.random(2, 5)));
            if (ZUtil.random(100) < 35) {
                gifts.add(new MainItem(1, 7, ZUtil.random(1, 3))); // Bột cường hóa
            }
            if (ZUtil.random(100) < 20) {
                gifts.add(new MainItem(135, 4, 1));                 // Tinh thể đá
            }
            if (ZUtil.random(100) < 10) {
                gifts.add(new MainItem(122, 4, 1));                 // Rương Cam Cùng Hệ Lv10
            }
            MainItem.showGiftBox(p, "Thưởng Chè Đậu Đỏ", "Thưởng thức chè đậu đỏ thơm ngon may mắn!", gifts, true, true);
            return true;
        } else if (id == ITEM_CHE_DAU_DO_DAC_BIET) {
            if (p.item.total_item_bag_by_id(4, ITEM_CHE_DAU_DO_DAC_BIET) < 1) {
                p.getService().send_box_ThongBao_OK("Bạn không có Chè Đậu Đỏ Đặc Biệt!");
                return false;
            }
            
            p.item.remove_item47(4, ITEM_CHE_DAU_DO_DAC_BIET, 1);
            p.item.updateInventory(false);

            long exp = p.level * 15000L;
            int beri = ZUtil.random(200_000, 500_000);
            p.update_vang(beri);
            p.update_exp(exp, true);
            p.updateMoney();

            List<MainItem> gifts = new ArrayList<>();
            gifts.add(new MainItem(866, 4, ZUtil.random(1, 3))); // Vé vòng quay
            if (ZUtil.random(100) < 40) {
                gifts.add(new MainItem(135, 4, ZUtil.random(2, 5))); // Tinh thể đá
            }
            if (ZUtil.random(100) < 25) {
                gifts.add(new MainItem(18, 7, 1));                   // Bột siêu cấp
            }
            if (ZUtil.random(100) < 15) {
                gifts.add(new MainItem(158, 4, 1));                  // Rương đại ác quỷ
            }
            if (ZUtil.random(100) < 10) {
                gifts.add(new MainItem(823, 4, 1));                  // Rương Dial
            }
            if (ZUtil.random(100) < 5) {
                gifts.add(new MainItem(690, 4, 1));                  // Rương TAQ tự chọn
            }
            MainItem.showGiftBox(p, "Chè Đậu Đỏ Đặc Biệt", "Thưởng thức chè đậu đỏ đặc biệt ngọt ngào phúc lộc!", gifts, true, true);
            return true;
        } else if (id == ITEM_PHIEU_GIAO_HANG) {
            if (p.item.total_item_bag_by_id(4, ITEM_PHIEU_GIAO_HANG) < 1) {
                p.getService().send_box_ThongBao_OK("Bạn không có Phiếu Giao Hàng!");
                return false;
            }
            
            p.item.remove_item47(4, ITEM_PHIEU_GIAO_HANG, 1);
            p.item.updateInventory(false);

            long exp = p.level * 8000L;
            int beri = ZUtil.random(100_000, 300_000);
            p.update_vang(beri);
            p.update_exp(exp, true);
            p.updateMoney();

            List<MainItem> gifts = new ArrayList<>();
            gifts.add(new MainItem(ITEM_LONG_CHIM_THUOC, 4, ZUtil.random(3, 8)));
            if (ZUtil.random(100) < 30) {
                gifts.add(new MainItem(1, 7, 5)); // Bột cường hóa x5
            }
            if (ZUtil.random(100) < 15) {
                gifts.add(new MainItem(122, 4, 1)); // Rương Cam Cùng Hệ Lv10
            }
            MainItem.showGiftBox(p, "Phiếu Giao Hàng", "Giao hàng thành công nhận thù lao xứng đáng!", gifts, true, true);
            return true;
        } else if (id == ITEM_HOP_BANH_THUONG_HANG) {
            if (p.item.total_item_bag_by_id(4, ITEM_HOP_BANH_THUONG_HANG) < 1) {
                p.getService().send_box_ThongBao_OK("Bạn không có Hộp Bánh Thượng Hạng!");
                return false;
            }
            
            p.item.remove_item47(4, ITEM_HOP_BANH_THUONG_HANG, 1);
            p.item.updateInventory(false);

            long exp = p.level * 20000L;
            int beri = ZUtil.random(500_000, 1_000_000);
            p.update_vang(beri);
            p.update_exp(exp, true);
            p.updateMoney();

            List<MainItem> gifts = new ArrayList<>();
            gifts.add(new MainItem(866, 4, ZUtil.random(2, 5))); // Vé vòng quay
            gifts.add(new MainItem(135, 4, ZUtil.random(3, 8))); // Tinh thể đá
            if (ZUtil.random(100) < 35) {
                gifts.add(new MainItem(18, 7, 2));               // Bột siêu cấp
            }
            if (ZUtil.random(100) < 20) {
                gifts.add(new MainItem(794, 4, 1));              // Bảo hiểm chuyển hóa siêu
            }
            if (ZUtil.random(100) < 15) {
                gifts.add(new MainItem(753, 4, 1));              // Sách Haki Vũ Trang
            }
            if (ZUtil.random(100) < 10) {
                gifts.add(new MainItem(158, 4, 1));              // Rương Đại Ác Quỷ
            }
            MainItem.showGiftBox(p, "Hộp Bánh Thượng Hạng", "Mở Hộp Bánh Thượng Hạng nhận vô vàn bảo vật quý hiếm!", gifts, true, true);
            return true;
        } else if (id == ITEM_CHIM_THUOC) {
            buildBridgeVip(p);
            return true;
        }
        return false;
    }

    @Override
    public void onMobKilled(Player p, Mob mob) {
        if (!EventManager.isActive(ID) || mob == null || mob.is_boss || p == null || p.level < 10) return;
        if (!isDropItemActive()) return;

        int[] items = new int[]{ITEM_GO, ITEM_DA, ITEM_LONG_CHIM_THUOC, ITEM_GIAY_GOI_QUA};
        dropEventItemRandom(p, mob, items, 20, 150, 0);
    }

    @Override
    public void onVanChuyen(Player p) {
        if (!isEventActive() || p == null || p.isBot) return;
        if (ZUtil.random(100) < 60) {
            p.item.add_item_bag47(4, ITEM_PHIEU_GIAO_HANG, 1);
            p.item.add_item_bag47(4, ITEM_LONG_CHIM_THUOC, ZUtil.random(2, 5));
            p.item.updateInventory(false);
        }
    }
}
