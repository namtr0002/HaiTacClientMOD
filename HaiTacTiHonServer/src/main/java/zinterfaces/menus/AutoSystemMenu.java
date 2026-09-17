package zinterfaces.menus;

import activities.AutoManager;
import model.Player;
import model.PlayerAutoSettings;
import template.ItemBag47;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.Item_wear;
import zinterfaces.iMenu;
import zinterfaces.iMenuDymanic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AutoSystemMenu — Giao Diện & Điều Khiển Toàn Bộ Menu Auto Tiện Ích.
 * Đăng ký tự động qua interface iMenu trong zinterfaces.menus.
 */
public class AutoSystemMenu implements iMenu {

    public static final short MENU_AUTO_MAIN                  = 8740;
    public static final short MENU_AUTO_DELETE_CONFIG          = 8741;
    public static final short MENU_AUTO_ADD_SELECT_CAT         = 8742;
    public static final short MENU_AUTO_ADD_ITEMS_PAGE         = 8743;
    public static final short MENU_AUTO_VIEW_BLACKLIST         = 8744;
    public static final short MENU_AUTO_COLOR_FILTER           = 8745; // Cài đặt lọc theo Phẩm Màu
    public static final short MENU_AUTO_OTHER_CONFIG           = 8746;
    public static final short MENU_AUTO_UPGRADE_SELECT_SLOT    = 8747;
    public static final short MENU_AUTO_UPGRADE_SELECT_TARGET  = 8748;
    public static final short MENU_AUTO_BATCH_BOX_SELECT       = 8749;
    public static final short MENU_AUTO_BATCH_BOX_COUNT        = 8750;
    public static final short MENU_AUTO_DELETE_LEVEL_FILTER    = 8751;
    public static final short MENU_AUTO_SELL_CONFIG            = 8752; // Cài đặt auto bán đồ
    public static final short MENU_AUTO_GEM_COMBINE            = 8753; // Auto hợp thành đá khảm
    public static final short MENU_AUTO_MATERIAL_CRAFT         = 8754; // Auto ghép nguyên liệu
    public static final short MENU_AUTO_DIAL_SELECT            = 8755; // Chọn dial để nâng cấp
    public static final short MENU_AUTO_DIAL_TARGET            = 8756; // Chọn mốc cấp độ dial
    public static final short MENU_AUTO_DISMANTLE_CONFIG       = 8757; // Auto phân giải trang bị rác ra bột
    public static final short MENU_AUTO_LOOT_FILTER            = 8758; // Bộ lọc nhặt đồ
    public static final short MENU_AUTO_HOLE_FILTER            = 8759; // Cài đặt lọc theo Lỗ Khảm & Đá
    public static final short MENU_AUTO_LOCK_FILTER            = 8760; // Cài đặt lọc theo Khóa An Toàn
    public static final short MENU_AUTO_EQUIP_TYPE_FILTER      = 8761; // Cài đặt lọc theo Loại Trang Bị
    public static final short MENU_AUTO_CATEGORY_FILTER        = 8762; // Cài đặt phân loại Item 3/4/7
    public static final short MENU_AUTO_CLEAN_PROMPT           = 8763; // Hộp thoại xác nhận dọn dẹp hành trang

    // Bộ nhớ tạm cho thao tác chọn nhiều bước (theo session của Player)
    public static class MenuSessionState {
        public int selectedCategory = 4;
        public int selectedBagSlot = -1;
        public int selectedDialSlot = -1;
        public byte selectedBoxCategory = 4;
        public short selectedBoxId = -1;
        public List<String> cachedAddList = new ArrayList<>();
        public List<String> cachedBlacklist = new ArrayList<>();
        public List<Integer> cachedUpgradeSlots = new ArrayList<>();
        public List<Integer> cachedDialSlots = new ArrayList<>();
        public List<int[]> cachedBoxList = new ArrayList<>();
    }

    private static final java.util.Map<Player, MenuSessionState> SESSIONS = new java.util.WeakHashMap<>();

    private static synchronized MenuSessionState getSession(Player p) {
        return SESSIONS.computeIfAbsent(p, k -> new MenuSessionState());
    }

    @Override
    public short[] getId() {
        return new short[]{
            MENU_AUTO_MAIN,
            MENU_AUTO_DELETE_CONFIG,
            MENU_AUTO_COLOR_FILTER,
            MENU_AUTO_HOLE_FILTER,
            MENU_AUTO_DELETE_LEVEL_FILTER,
            MENU_AUTO_LOCK_FILTER,
            MENU_AUTO_EQUIP_TYPE_FILTER,
            MENU_AUTO_CATEGORY_FILTER,
            MENU_AUTO_ADD_SELECT_CAT,
            MENU_AUTO_ADD_ITEMS_PAGE,
            MENU_AUTO_VIEW_BLACKLIST,
            MENU_AUTO_OTHER_CONFIG,
            MENU_AUTO_UPGRADE_SELECT_SLOT,
            MENU_AUTO_UPGRADE_SELECT_TARGET,
            MENU_AUTO_BATCH_BOX_SELECT,
            MENU_AUTO_BATCH_BOX_COUNT,
            MENU_AUTO_SELL_CONFIG,
            MENU_AUTO_GEM_COMBINE,
            MENU_AUTO_MATERIAL_CRAFT,
            MENU_AUTO_DIAL_SELECT,
            MENU_AUTO_DIAL_TARGET,
            MENU_AUTO_DISMANTLE_CONFIG,
            MENU_AUTO_LOOT_FILTER,
            MENU_AUTO_CLEAN_PROMPT
        };
    }

    @Override
    public void handleMenu(Player p, short idNPC, int index) throws IOException {
        if (p == null) return;
        switch (idNPC) {
            case MENU_AUTO_MAIN:                 handleMainMenu(p, index); break;
            case MENU_AUTO_DELETE_CONFIG:         handleDeleteConfig(p, index); break;
            case MENU_AUTO_COLOR_FILTER:          handleColorFilter(p, index); break;
            case MENU_AUTO_HOLE_FILTER:           handleHoleFilter(p, index); break;
            case MENU_AUTO_DELETE_LEVEL_FILTER:   handleDeleteLevelFilter(p, index); break;
            case MENU_AUTO_LOCK_FILTER:           handleLockFilter(p, index); break;
            case MENU_AUTO_EQUIP_TYPE_FILTER:     handleEquipTypeFilter(p, index); break;
            case MENU_AUTO_CATEGORY_FILTER:       handleCategoryFilter(p, index); break;
            case MENU_AUTO_ADD_SELECT_CAT:        handleAddSelectCat(p, index); break;
            case MENU_AUTO_ADD_ITEMS_PAGE:        handleAddItemsPage(p, index); break;
            case MENU_AUTO_VIEW_BLACKLIST:        handleViewBlacklist(p, index); break;
            case MENU_AUTO_OTHER_CONFIG:          handleOtherConfig(p, index); break;
            case MENU_AUTO_LOOT_FILTER:           handleLootFilter(p, index); break;
            case MENU_AUTO_GEM_COMBINE:           handleGemCombineMenu(p, index); break;
            case MENU_AUTO_MATERIAL_CRAFT:        handleMaterialCraftMenu(p, index); break;
            case MENU_AUTO_UPGRADE_SELECT_SLOT:   handleUpgradeSelectSlot(p, index); break;
            case MENU_AUTO_UPGRADE_SELECT_TARGET: handleUpgradeSelectTarget(p, index); break;
            case MENU_AUTO_DIAL_SELECT:           handleDialSelectSlot(p, index); break;
            case MENU_AUTO_DIAL_TARGET:           handleDialSelectTarget(p, index); break;
            case MENU_AUTO_DISMANTLE_CONFIG:      handleDismantleMenu(p, index); break;
            case MENU_AUTO_BATCH_BOX_SELECT:      handleBatchBoxSelect(p, index); break;
            case MENU_AUTO_BATCH_BOX_COUNT:       handleBatchBoxCount(p, index); break;
            case MENU_AUTO_SELL_CONFIG:           handleDeleteConfig(p, index); break;
            case MENU_AUTO_CLEAN_PROMPT:          handleCleanPrompt(p, index); break;
        }
    }

    // ==============================================================
    //  1. MENU CHÍNH AUTO
    // ==============================================================

    public static void openAutoMainMenu(Player p) throws IOException {
        if (p == null || p.autoSettings == null) return;

        String delState    = (p.autoSettings.isAutoDeleteGear || p.autoSettings.isAutoDeleteBlacklist) ? "[BẬT]" : "[TẮT]";
        String otherState  = (p.autoSettings.isAutoLoot || p.autoSettings.isAutoRevive) ? "[BẬT]" : "[TẮT]";

        if (AdminSystemMenu.isAdmin(p)) {
            iMenuDymanic.buildAndSend(p, MENU_AUTO_MAIN,
                "HỆ THỐNG MENU AUTO TIỆN ÍCH [ADMIN]",
                new String[]{
                    "Cài Đặt Bán & Xóa Đồ " + delState,
                    "Cài Đặt Nhặt Đồ & Hồi Sinh " + otherState,
                    "Auto Hợp Thành Đá Khảm",
                    "Auto Ghép Nguyên Liệu",
                    "Auto Phân Giải Trang Bị Ra Bột",
                    "Auto Mở Rương Hàng Loạt",
                    "Dọn Dẹp Hành Trang Ngay",
                    "[ADMIN] Auto Cường Hóa Nhanh (+5 Đến +15)",
                    "[ADMIN] Auto Nâng Cấp Dial Nhanh",
                    "Hướng Dẫn Sử Dụng Auto",
                    "Đóng"
                },
                new short[]{110, 110, 133, 133, 110, 155, 110, 155, 133, 118, 134}
            );
        } else {
            iMenuDymanic.buildAndSend(p, MENU_AUTO_MAIN,
                "HỆ THỐNG MENU AUTO TIỆN ÍCH",
                new String[]{
                    "Cài Đặt Bán & Xóa Đồ " + delState,
                    "Cài Đặt Nhặt Đồ & Hồi Sinh " + otherState,
                    "Auto Hợp Thành Đá Khảm",
                    "Auto Ghép Nguyên Liệu",
                    "Auto Phân Giải Trang Bị Ra Bột",
                    "Auto Mở Rương Hàng Loạt",
                    "Dọn Dẹp Hành Trang Ngay",
                    "Hướng Dẫn Sử Dụng Auto",
                    "Đóng"
                },
                new short[]{110, 110, 133, 133, 110, 155, 110, 118, 134}
            );
        }
    }

    private void handleMainMenu(Player p, int index) throws IOException {
        if (AdminSystemMenu.isAdmin(p)) {
            switch (index) {
                case 0: openDeleteConfig(p); break;
                case 1: openOtherConfig(p); break;
                case 2: openGemCombineMenu(p); break;
                case 3: openMaterialCraftMenu(p); break;
                case 4: openDismantleMenu(p); break;
                case 5: openBatchBoxSelect(p); break;
                case 6: openCleanPromptMenu(p); break;
                case 7: openUpgradeSelectSlot(p); break;
                case 8: openDialSelectSlot(p); break;
                case 9: showAutoHelp(p); break;
                case 10: break;
            }
        } else {
            switch (index) {
                case 0: openDeleteConfig(p); break;
                case 1: openOtherConfig(p); break;
                case 2: openGemCombineMenu(p); break;
                case 3: openMaterialCraftMenu(p); break;
                case 4: openDismantleMenu(p); break;
                case 5: openBatchBoxSelect(p); break;
                case 6: openCleanPromptMenu(p); break;
                case 7: showAutoHelp(p); break;
                case 8: break;
            }
        }
    }

    // ==============================================================
    //  2. CÀI ĐẶT BÁN & XÓA ĐỒ (TRUNG TÂM ĐIỀU KHIỂN CHUẨN HÓA)
    // ==============================================================

    public static void openDeleteConfig(Player p) throws IOException {
        if (p == null || p.autoSettings == null) return;

        String sGear   = p.autoSettings.isAutoDeleteGear ? "[BẬT]" : "[TẮT]";
        String sBlack  = p.autoSettings.isAutoDeleteBlacklist ? "[BẬT]" : "[TẮT]";
        String sMode   = p.autoSettings.isAutoSellToBeri ? "[BÁN RA BERI]" : "[XÓA VỨT BỎ]";
        String sLearn  = p.autoSettings.isAutoSellMode ? "[BẬT - ĐANG HỌC]" : "[TẮT]";
        String sHole   = PlayerAutoSettings.getHoleFilterName(p.autoSettings.holeFilterType);
        String sMoc    = PlayerAutoSettings.getLevelFilterText(p.autoSettings.isKeepUpgradedGear, p.autoSettings.maxLevelUpToDelete);
        String sLock   = p.autoSettings.isSellLockedGear ? "[BÁN CẢ KHÓA]" : "[CHỈ KHÔNG KHÓA]";
        String sEquip  = PlayerAutoSettings.getEquipTypeName(p.autoSettings.gearTypeEquipFilter);

        iMenuDymanic.buildAndSend(p, MENU_AUTO_DELETE_CONFIG,
            "CÀI ĐẶT TỰ ĐỘNG BÁN & XÓA ĐỒ",
            new String[]{
                "Tự Động Bán Trang Bị (Item 3): " + sGear,
                "Bộ Lọc Phẩm Màu (Trắng, Xanh...): [XEM & CÀI ĐẶT]",
                "Bộ Lọc Lỗ Khảm & Đá: [" + sHole + "]",
                "Bộ Lọc Cường Hóa: [" + sMoc + "]",
                "Bộ Lọc Khóa An Toàn: " + sLock,
                "Bộ Lọc Loại Trang Bị: [" + sEquip + "]",
                "Tùy Chọn Phân Loại (Item 3, 4, 7): [CÀI ĐẶT]",
                "Chế Độ Xử Lý: " + sMode,
                "Chế Độ Ghi Nhớ Khi Bán Shop: " + sLearn,
                "Thêm Vật Phẩm Vào DS Chỉ Định",
                "Xem DS Chỉ Định (" + p.autoSettings.blacklistItems.size() + " món)",
                "DỌN DẸP / BÁN HÀNH TRANG NGAY",
                "Hướng Dẫn Bán & Xóa Đồ",
                "Quay lại"
            },
            new short[]{110, 133, 133, 133, 110, 133, 133, 110, 110, 133, 110, 155, 118, 134}
        );
    }

    private void handleDeleteConfig(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                p.autoSettings.isAutoDeleteGear = !p.autoSettings.isAutoDeleteGear;
                openDeleteConfig(p);
                break;
            }
            case 1: openColorFilter(p); break;
            case 2: openHoleFilter(p); break;
            case 3: openDeleteLevelFilter(p); break;
            case 4: openLockFilter(p); break;
            case 5: openEquipTypeFilter(p); break;
            case 6: openCategoryFilter(p); break;
            case 7: {
                p.autoSettings.isAutoSellToBeri = !p.autoSettings.isAutoSellToBeri;
                openDeleteConfig(p);
                break;
            }
            case 8: {
                p.autoSettings.isAutoSellMode = !p.autoSettings.isAutoSellMode;
                if (p.autoSettings.isAutoSellMode) {
                    p.getService().send_box_ThongBao_OK(
                        "CHẾ ĐỘ HỌC: BẬT\n" +
                        "Hãy vào bất kỳ Shop nào và bán item rác cần tự bán.\n" +
                        "Server sẽ tự động ghi nhớ toàn bộ item bạn đã bán!\n" +
                        "Sau khi bán xong, hãy Tắt Chế Độ Học để hoàn tất."
                    );
                } else {
                    p.getService().send_box_ThongBao_OK("Đã TẮT chế độ học. Danh sách đã ghi nhớ được lưu an toàn!");
                }
                openDeleteConfig(p);
                break;
            }
            case 9: openAddSelectCat(p); break;
            case 10: openViewBlacklist(p); break;
            case 11: openCleanPromptMenu(p); break;
            case 12: showDeleteHelp(p); break;
            case 13: openAutoMainMenu(p); break;
        }
    }

    // ==============================================================
    //  3. BỘ LỌC PHẨM MÀU TRANG BỊ (COLOR FILTER)
    // ==============================================================

    public static void openColorFilter(Player p) throws IOException {
        if (p == null || p.autoSettings == null) return;

        PlayerAutoSettings s = p.autoSettings;

        iMenuDymanic.buildAndSend(p, MENU_AUTO_COLOR_FILTER,
            "CÀI ĐẶT BỘ LỌC PHẨM MÀU TRANG BỊ",
            new String[]{
                "1. Đồ Trắng (Thường): " + (s.sellColorWhite ? "[BÁN]" : "[GIỮ]"),
                "2. Đồ Xanh (Hiếm): " + (s.sellColorBlue ? "[BÁN]" : "[GIỮ]"),
                "3. Đồ Vàng (Hoàng Kim): " + (s.sellColorYellow ? "[BÁN]" : "[GIỮ]"),
                "4. Đồ Tím (Siêu Cấp): " + (s.sellColorPurple ? "[BÁN]" : "[GIỮ]"),
                "5. Đồ Cam (Truyền Thuyết): " + (s.sellColorOrange ? "[BÁN]" : "[GIỮ]"),
                "6. Đồ Đỏ (Thần Thoại): " + (s.sellColorRed ? "[BÁN]" : "[GIỮ]"),
                "7. Đồ Hồng (Cực Phẩm): " + (s.sellColorPink ? "[BÁN]" : "[GIỮ]"),
                "8. Đồ Ánh Kim (Tối Thượng): " + (s.sellColorGold ? "[BÁN]" : "[GIỮ]"),
                "9. Thần Trang (Set Thần): " + (s.sellColorGod ? "[BÁN]" : "[BẢO VỆ]"),
                "Preset 1: Chỉ Bán Đồ Trắng",
                "Preset 2: Bán Đồ Trắng + Xanh",
                "Preset 3: Bán Đồ Trắng + Xanh + Vàng",
                "Quay lại"
            },
            new short[]{110, 110, 110, 110, 110, 110, 110, 110, 155, 133, 133, 133, 134}
        );
    }

    private void handleColorFilter(Player p, int index) throws IOException {
        PlayerAutoSettings s = p.autoSettings;
        switch (index) {
            case 0: s.sellColorWhite  = !s.sellColorWhite; break;
            case 1: s.sellColorBlue   = !s.sellColorBlue; break;
            case 2: s.sellColorYellow = !s.sellColorYellow; break;
            case 3: s.sellColorPurple = !s.sellColorPurple; break;
            case 4: s.sellColorOrange = !s.sellColorOrange; break;
            case 5: s.sellColorRed    = !s.sellColorRed; break;
            case 6: s.sellColorPink   = !s.sellColorPink; break;
            case 7: s.sellColorGold   = !s.sellColorGold; break;
            case 8: s.sellColorGod    = !s.sellColorGod; break;
            case 9: {
                // Preset 1: Chỉ Trắng
                s.sellColorWhite = true;
                s.sellColorBlue = false;
                s.sellColorYellow = false;
                s.sellColorPurple = false;
                s.sellColorOrange = false;
                s.sellColorRed = false;
                s.sellColorPink = false;
                s.sellColorGold = false;
                s.sellColorGod = false;
                p.getService().send_box_ThongBao_OK("Đã chọn Preset 1: Chỉ tự bán trang bị Trắng (Thường)!");
                break;
            }
            case 10: {
                // Preset 2: Trắng + Xanh
                s.sellColorWhite = true;
                s.sellColorBlue = true;
                s.sellColorYellow = false;
                s.sellColorPurple = false;
                s.sellColorOrange = false;
                s.sellColorRed = false;
                s.sellColorPink = false;
                s.sellColorGold = false;
                s.sellColorGod = false;
                p.getService().send_box_ThongBao_OK("Đã chọn Preset 2: Tự bán trang bị Trắng + Xanh!");
                break;
            }
            case 11: {
                // Preset 3: Trắng + Xanh + Vàng
                s.sellColorWhite = true;
                s.sellColorBlue = true;
                s.sellColorYellow = true;
                s.sellColorPurple = false;
                s.sellColorOrange = false;
                s.sellColorRed = false;
                s.sellColorPink = false;
                s.sellColorGold = false;
                s.sellColorGod = false;
                p.getService().send_box_ThongBao_OK("Đã chọn Preset 3: Tự bán trang bị Trắng + Xanh + Vàng!");
                break;
            }
            case 12: openDeleteConfig(p); return;
        }
        openColorFilter(p);
    }

    // ==============================================================
    //  4. BỘ LỌC LỖ KHẢM & ĐÁ (SOCKET / GEM FILTER)
    // ==============================================================

    public static void openHoleFilter(Player p) throws IOException {
        if (p == null || p.autoSettings == null) return;

        String sGem = p.autoSettings.isProtectSocketedGems ? "[BẬT - AN TOÀN]" : "[TẮT]";

        iMenuDymanic.buildAndSend(p, MENU_AUTO_HOLE_FILTER,
            "CÀI ĐẶT BỘ LỌC LỖ KHẢM & ĐÁ",
            new String[]{
                "1. Bán Tất Cả (Không phân biệt lỗ)",
                "2. Chỉ Bán Đồ Không Có Lỗ (0 lỗ)",
                "3. Chỉ Bán Đồ Có Lỗ Khảm (>= 1 lỗ)",
                "4. Chỉ Bán Đồ Chưa Khảm Đá",
                "5. Bảo Vệ Đồ Đã Khảm Ngọc: " + sGem,
                "Quay lại"
            },
            new short[]{110, 110, 110, 110, 155, 134}
        );
    }

    private void handleHoleFilter(Player p, int index) throws IOException {
        switch (index) {
            case 0: p.autoSettings.holeFilterType = PlayerAutoSettings.HOLE_FILTER_ALL; break;
            case 1: p.autoSettings.holeFilterType = PlayerAutoSettings.HOLE_FILTER_NO_HOLES; break;
            case 2: p.autoSettings.holeFilterType = PlayerAutoSettings.HOLE_FILTER_HAS_HOLES; break;
            case 3: p.autoSettings.holeFilterType = PlayerAutoSettings.HOLE_FILTER_UNSOCKETED; break;
            case 4: p.autoSettings.isProtectSocketedGems = !p.autoSettings.isProtectSocketedGems; break;
            case 5: openDeleteConfig(p); return;
        }
        openHoleFilter(p);
    }

    // ==============================================================
    //  5. BỘ LỌC CẤP CƯỜNG HÓA (LEVELUP / UPGRADE FILTER)
    // ==============================================================

    public static void openDeleteLevelFilter(Player p) throws IOException {
        if (p == null || p.autoSettings == null) return;

        String sKeep = p.autoSettings.isKeepUpgradedGear ? "[BẬT - AN TOÀN]" : "[TẮT]";

        iMenuDymanic.buildAndSend(p, MENU_AUTO_DELETE_LEVEL_FILTER,
            "CÀI ĐẶT BỘ LỌC CẤP CƯỜNG HÓA",
            new String[]{
                "Bảo Vệ Đồ Đã Cường Hóa (+1 trở lên): " + sKeep,
                "Chỉ bán trang bị +0",
                "Bán trang bị đến +1",
                "Bán trang bị đến +3",
                "Bán trang bị đến +5",
                "Bán trang bị đến +7",
                "Bán trang bị đến +10",
                "Bán trang bị đến +12",
                "Bán trang bị đến +15",
                "Bán trang bị đến +17 (Tất cả)",
                "Quay lại"
            },
            new short[]{155, 110, 110, 110, 110, 110, 110, 110, 110, 155, 134}
        );
    }

    private void handleDeleteLevelFilter(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                p.autoSettings.isKeepUpgradedGear = !p.autoSettings.isKeepUpgradedGear;
                if (p.autoSettings.isKeepUpgradedGear) {
                    p.autoSettings.maxLevelUpToDelete = 0;
                }
                break;
            }
            case 1: {
                p.autoSettings.isKeepUpgradedGear = true;
                p.autoSettings.maxLevelUpToDelete = 0;
                break;
            }
            case 2: {
                p.autoSettings.isKeepUpgradedGear = false;
                p.autoSettings.maxLevelUpToDelete = 1;
                break;
            }
            case 3: {
                p.autoSettings.isKeepUpgradedGear = false;
                p.autoSettings.maxLevelUpToDelete = 3;
                break;
            }
            case 4: {
                p.autoSettings.isKeepUpgradedGear = false;
                p.autoSettings.maxLevelUpToDelete = 5;
                break;
            }
            case 5: {
                p.autoSettings.isKeepUpgradedGear = false;
                p.autoSettings.maxLevelUpToDelete = 7;
                break;
            }
            case 6: {
                p.autoSettings.isKeepUpgradedGear = false;
                p.autoSettings.maxLevelUpToDelete = 10;
                break;
            }
            case 7: {
                p.autoSettings.isKeepUpgradedGear = false;
                p.autoSettings.maxLevelUpToDelete = 12;
                break;
            }
            case 8: {
                p.autoSettings.isKeepUpgradedGear = false;
                p.autoSettings.maxLevelUpToDelete = 15;
                break;
            }
            case 9: {
                p.autoSettings.isKeepUpgradedGear = false;
                p.autoSettings.maxLevelUpToDelete = 17;
                break;
            }
            case 10: openDeleteConfig(p); return;
        }
        openDeleteLevelFilter(p);
    }

    // ==============================================================
    //  6. BỘ LỌC KHÓA AN TOÀN (LOCK FILTER)
    // ==============================================================

    public static void openLockFilter(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_AUTO_LOCK_FILTER,
            "CÀI ĐẶT BỘ LỌC KHÓA AN TOÀN",
            new String[]{
                "1. Chỉ Bán Đồ Không Khóa (Khuyên Dùng - An Toàn)",
                "2. Cho Phép Bán Cả Đồ Khóa (Cẩn Trọng!)",
                "Quay lại"
            },
            new short[]{110, 155, 134}
        );
    }

    private void handleLockFilter(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                p.autoSettings.isSellLockedGear = false;
                p.getService().send_box_ThongBao_OK("Đã chọn: Chỉ bán đồ KHÔNG KHÓA (Bảo vệ an toàn 100%)!");
                break;
            }
            case 1: {
                p.autoSettings.isSellLockedGear = true;
                p.getService().send_box_ThongBao_OK("CẢNH BÁO: Đã bật cho phép bán cả đồ đã khóa an toàn!");
                break;
            }
            case 2: openDeleteConfig(p); return;
        }
        openDeleteConfig(p);
    }

    // ==============================================================
    //  7. BỘ LỌC LOẠI TRANG BỊ (TYPE_EQUIP FILTER)
    // ==============================================================

    public static void openEquipTypeFilter(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_AUTO_EQUIP_TYPE_FILTER,
            "CÀI ĐẶT LỌC THEO LOẠI TRANG BỊ",
            new String[]{
                "1. Tất Cả Loại Trang Bị",
                "2. Chỉ Bán Vũ Khí",
                "3. Chỉ Bán Nón",
                "4. Chỉ Bán Áo",
                "5. Chỉ Bán Quần",
                "6. Chỉ Bán Nhẫn",
                "7. Chỉ Bán Dây Chuyền",
                "8. Chỉ Bán Giày",
                "9. Chỉ Bán Dial",
                "Quay lại"
            },
            new short[]{110, 110, 110, 110, 110, 110, 110, 110, 110, 134}
        );
    }

    private void handleEquipTypeFilter(Player p, int index) throws IOException {
        switch (index) {
            case 0: p.autoSettings.gearTypeEquipFilter = -1; break; // Tất cả
            case 1: p.autoSettings.gearTypeEquipFilter = 0; break;  // Vũ khí
            case 2: p.autoSettings.gearTypeEquipFilter = 1; break;  // Nón
            case 3: p.autoSettings.gearTypeEquipFilter = 2; break;  // Áo
            case 4: p.autoSettings.gearTypeEquipFilter = 3; break;  // Nhẫn
            case 5: p.autoSettings.gearTypeEquipFilter = 4; break;  // Dây chuyền
            case 6: p.autoSettings.gearTypeEquipFilter = 5; break;  // Giày
            case 7: p.autoSettings.gearTypeEquipFilter = 7; break;  // Dial
            case 8: p.autoSettings.gearTypeEquipFilter = 7; break;
            case 9: openDeleteConfig(p); return;
        }
        openDeleteConfig(p);
    }

    // ==============================================================
    //  8. CÀI ĐẶT PHÂN LOẠI TỰ ĐỘNG BÁN (ITEM 3 / 4 / 7)
    // ==============================================================

    public static void openCategoryFilter(Player p) throws IOException {
        if (p == null || p.autoSettings == null) return;

        String sGear  = p.autoSettings.isAutoDeleteGear ? "[BẬT]" : "[TẮT]";
        String sItem4 = p.autoSettings.isAutoSellItem4 ? "[BẬT]" : "[TẮT]";
        String sItem7 = p.autoSettings.isAutoSellItem7 ? "[BẬT]" : "[TẮT]";
        String sBlack = p.autoSettings.isAutoDeleteBlacklist ? "[BẬT]" : "[TẮT]";

        iMenuDymanic.buildAndSend(p, MENU_AUTO_CATEGORY_FILTER,
            "CÀI ĐẶT PHÂN LOẠI TỰ ĐỘNG BÁN",
            new String[]{
                "1. Bán Trang Bị (Item 3) Theo Bộ Lọc: " + sGear,
                "2. Bán Vật Phẩm / Dược Phẩm (Item 4) Trong DS: " + sItem4,
                "3. Bán Nguyên Liệu / Đá (Item 7) Trong DS: " + sItem7,
                "4. Quét Toàn Bộ Danh Sách Chỉ Định: " + sBlack,
                "Quay lại"
            },
            new short[]{110, 110, 110, 133, 134}
        );
    }

    private void handleCategoryFilter(Player p, int index) throws IOException {
        switch (index) {
            case 0: p.autoSettings.isAutoDeleteGear = !p.autoSettings.isAutoDeleteGear; break;
            case 1: p.autoSettings.isAutoSellItem4 = !p.autoSettings.isAutoSellItem4; break;
            case 2: p.autoSettings.isAutoSellItem7 = !p.autoSettings.isAutoSellItem7; break;
            case 3: p.autoSettings.isAutoDeleteBlacklist = !p.autoSettings.isAutoDeleteBlacklist; break;
            case 4: openDeleteConfig(p); return;
        }
        openCategoryFilter(p);
    }

    // ==============================================================
    //  9. THÊM ITEM VÀO DANH SÁCH CHỈ ĐỊNH (CHỌN TỪ HÀNH TRANG)
    // ==============================================================

    public static void openAddSelectCat(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_AUTO_ADD_SELECT_CAT,
            "THÊM VẬT PHẨM VÀO DANH SÁCH TỰ BÁN",
            new String[]{
                "Chọn từ Trang Bị Trong Túi (Item 3)",
                "Chọn từ Vật Phẩm Trong Túi (Item 4)",
                "Chọn từ Nguyên Liệu Trong Túi (Item 7)",
                "Nhập ID Vật Phẩm Trực Tiếp",
                "Quay lại"
            },
            new short[]{133, 110, 110, 155, 134}
        );
    }

    private void handleAddSelectCat(Player p, int index) throws IOException {
        MenuSessionState session = getSession(p);
        switch (index) {
            case 0: {
                session.selectedCategory = 3;
                openAddItemsPage(p, 3);
                break;
            }
            case 1: {
                session.selectedCategory = 4;
                openAddItemsPage(p, 4);
                break;
            }
            case 2: {
                session.selectedCategory = 7;
                openAddItemsPage(p, 7);
                break;
            }
            case 3: {
                p.sendInput("Nhập Loại_Item (3, 4 hoặc 7) và ID_Item (Ví dụ: 4 207 hoặc 7 5):", new String[]{"Category ID"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        try {
                            String[] parts = inputs[0].trim().split("\\s+");
                            int cat = Integer.parseInt(parts[0]);
                            int id  = Integer.parseInt(parts[1]);
                            AutoManager.addBlacklistItem(p, cat, id);
                            String name = AutoManager.getItemNameFromKey(cat + ":" + id);
                            p.getService().send_box_ThongBao_OK("Đã thêm [" + name + "] vào danh sách tự động bán!");
                        } catch (Exception e) {
                            try { p.getService().send_box_ThongBao_OK("Dữ liệu nhập không hợp lệ! Vui lòng nhập: [Loại] [ID]"); } catch (Exception ignored) {}
                        }
                    }
                });
                break;
            }
            case 4: openDeleteConfig(p); break;
        }
    }

    public static void openAddItemsPage(Player p, int category) throws IOException {
        MenuSessionState session = getSession(p);
        session.cachedAddList.clear();

        List<String> menuNames = new ArrayList<>();
        List<Short> menuIcons = new ArrayList<>();

        if (category == 3 && p.item != null && p.item.bag3 != null) {
            for (Item_wear it : p.item.bag3) {
                if (it != null && it.template != null) {
                    String key = "3:" + it.template.id;
                    if (!session.cachedAddList.contains(key)) {
                        session.cachedAddList.add(key);
                        String colorStr = PlayerAutoSettings.getColorName(it.getColor());
                        menuNames.add(it.template.name + " (" + colorStr + ") (ID: " + it.template.id + ")");
                        menuIcons.add((short) (it.template.icon > 0 ? it.template.icon : 110));
                    }
                }
            }
        } else if (p.item != null && p.item.bag47 != null) {
            for (ItemBag47 it47 : p.item.bag47) {
                if (it47 != null && it47.category == category) {
                    String key = category + ":" + it47.id;
                    if (!session.cachedAddList.contains(key)) {
                        session.cachedAddList.add(key);
                        String name = AutoManager.getItemNameFromKey(key);
                        menuNames.add(name + " x" + it47.quant + " (ID: " + it47.id + ")");
                        menuIcons.add((short) 110);
                    }
                }
            }
        }

        if (menuNames.isEmpty()) {
            menuNames.add("Không có vật phẩm nào trong mục này");
            menuIcons.add((short) 118);
        }

        menuNames.add("Quay lại");
        menuIcons.add((short) 134);

        short[] iconsArr = new short[menuIcons.size()];
        for (int i = 0; i < menuIcons.size(); i++) iconsArr[i] = menuIcons.get(i);

        iMenuDymanic.buildAndSend(p, MENU_AUTO_ADD_ITEMS_PAGE,
            "CHỌN VẬT PHẨM ĐỂ THÊM VÀO DANH SÁCH",
            menuNames.toArray(new String[0]),
            iconsArr
        );
    }

    private void handleAddItemsPage(Player p, int index) throws IOException {
        MenuSessionState session = getSession(p);
        if (index >= 0 && index < session.cachedAddList.size()) {
            String itemKey = session.cachedAddList.get(index);
            p.autoSettings.blacklistItems.add(itemKey);
            String name = AutoManager.getItemNameFromKey(itemKey);
            p.getService().send_box_ThongBao_OK("Đã thêm [" + name + "] vào danh sách tự động bán!");
            openAddSelectCat(p);
        } else {
            openAddSelectCat(p);
        }
    }

    // ==============================================================
    //  10. XEM & QUẢN LÝ DANH SÁCH CHỈ ĐỊNH (VIEW BLACKLIST)
    // ==============================================================

    public static void openViewBlacklist(Player p) throws IOException {
        if (p == null || p.autoSettings == null) return;
        MenuSessionState session = getSession(p);
        session.cachedBlacklist.clear();
        session.cachedBlacklist.addAll(p.autoSettings.blacklistItems);

        List<String> menuNames = new ArrayList<>();
        List<Short> menuIcons = new ArrayList<>();

        for (String key : session.cachedBlacklist) {
            String name = AutoManager.getItemNameFromKey(key);
            menuNames.add("Xóa khỏi DS: " + name);
            menuIcons.add((short) 118);
        }

        if (menuNames.isEmpty()) {
            menuNames.add("Danh sách hiện đang trống");
            menuIcons.add((short) 110);
        } else {
            menuNames.add("XÓA TOÀN BỘ DANH SÁCH");
            menuIcons.add((short) 134);
        }

        menuNames.add("Quay lại");
        menuIcons.add((short) 134);

        short[] iconsArr = new short[menuIcons.size()];
        for (int i = 0; i < menuIcons.size(); i++) iconsArr[i] = menuIcons.get(i);

        iMenuDymanic.buildAndSend(p, MENU_AUTO_VIEW_BLACKLIST,
            "DANH SÁCH VẬT PHẨM TỰ ĐỘNG BÁN",
            menuNames.toArray(new String[0]),
            iconsArr
        );
    }

    private void handleViewBlacklist(Player p, int index) throws IOException {
        MenuSessionState session = getSession(p);
        int listSize = session.cachedBlacklist.size();

        if (index >= 0 && index < listSize) {
            String key = session.cachedBlacklist.get(index);
            p.autoSettings.blacklistItems.remove(key);
            String name = AutoManager.getItemNameFromKey(key);
            p.getService().send_box_ThongBao_OK("Đã xóa [" + name + "] khỏi danh sách!");
            openViewBlacklist(p);
        } else if (listSize > 0 && index == listSize) {
            p.autoSettings.blacklistItems.clear();
            p.getService().send_box_ThongBao_OK("Đã xóa toàn bộ danh sách thành công!");
            openViewBlacklist(p);
        } else {
            openDeleteConfig(p);
        }
    }

    // ==============================================================
    //  6. CÀI ĐẶT NHẶT ĐỒ & HỒI SINH
    // ==============================================================

    public static String getLootFilterName(int type) {
        switch (type) {
            case 1: return "Chỉ Beri & Ruby";
            case 2: return "Chỉ Trang Bị";
            case 3: return "Chỉ Đá & Nguyên Liệu";
            default: return "Tất Cả Vật Phẩm";
        }
    }

    public static void openOtherConfig(Player p) throws IOException {
        if (p == null || p.autoSettings == null) return;

        String sLoot   = p.autoSettings.isAutoLoot ? "[BẬT]" : "[TẮT]";
        String sRevive = p.autoSettings.isAutoRevive ? "[BẬT]" : "[TẮT]";
        String sFilter = getLootFilterName(p.autoSettings.lootFilterType);

        iMenuDymanic.buildAndSend(p, MENU_AUTO_OTHER_CONFIG,
            "CÀI ĐẶT NHẶT ĐỒ & HỒI SINH",
            new String[]{
                "Tự Động Nhặt Đồ Rơi: " + sLoot,
                "Bộ Lọc Nhặt Đồ: [" + sFilter + "]",
                "Tự Động Hồi Sinh Về Làng: " + sRevive,
                "Hướng Dẫn Tiện Ích",
                "Quay lại"
            },
            new short[]{110, 133, 110, 118, 134}
        );
    }

    private void handleOtherConfig(Player p, int index) throws IOException {
        switch (index) {
            case 0: {
                p.autoSettings.isAutoLoot = !p.autoSettings.isAutoLoot;
                openOtherConfig(p);
                break;
            }
            case 1: {
                openLootFilter(p);
                break;
            }
            case 2: {
                p.autoSettings.isAutoRevive = !p.autoSettings.isAutoRevive;
                openOtherConfig(p);
                break;
            }
            case 3: showOtherHelp(p); break;
            case 4: openAutoMainMenu(p); break;
        }
    }

    public static void openLootFilter(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_AUTO_LOOT_FILTER,
            "CHỌN BỘ LỌC TỰ ĐỘNG NHẶT ĐỒ",
            new String[]{
                "Nhặt Tất Cả Vật Phẩm Rơi",
                "Chỉ Nhặt Beri & Ruby",
                "Chỉ Nhặt Trang Bị (Đồ Mặc)",
                "Chỉ Nhặt Đá Khảm & Nguyên Liệu",
                "Quay lại"
            },
            new short[]{110, 110, 133, 133, 134}
        );
    }

    private void handleLootFilter(Player p, int index) throws IOException {
        switch (index) {
            case 0: p.autoSettings.lootFilterType = 0; break;
            case 1: p.autoSettings.lootFilterType = 1; break;
            case 2: p.autoSettings.lootFilterType = 2; break;
            case 3: p.autoSettings.lootFilterType = 3; break;
            case 4: openOtherConfig(p); return;
        }
        openOtherConfig(p);
    }

    // ==============================================================
    //  7. AUTO HỢP THÀNH ĐÁ KHẢM & NGỌC
    // ==============================================================

    public static void openGemCombineMenu(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_AUTO_GEM_COMBINE,
            "AUTO HỢP THÀNH ĐÁ KHẢM & NGỌC",
            new String[]{
                "Ghép Toàn Bộ Đá Cấp 1 -> Cấp 2",
                "Ghép Toàn Bộ Đá Cấp 2 -> Cấp 3",
                "Ghép Toàn Bộ Đá Cấp 3 -> Cấp 4",
                "Ghép Toàn Bộ Đá Cấp 4 -> Cấp 5",
                "Ghép Toàn Bộ Đá Cấp 5 -> Cấp 6",
                "Ghép Toàn Bộ Hải Thạch (Cấp 1..5)",
                "Ghép Toàn Bộ Hổ Phách (Cấp 1..5)",
                "SIÊU TỰ ĐỘNG: Ghép Tất Cả Đá Lên Cấp Cao Nhất",
                "Hướng Dẫn Hợp Thành Đá",
                "Quay lại"
            },
            new short[]{110, 110, 110, 110, 133, 133, 133, 155, 118, 134}
        );
    }

    private void handleGemCombineMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: AutoManager.executeAutoGemCombine(p, 1); break;
            case 1: AutoManager.executeAutoGemCombine(p, 2); break;
            case 2: AutoManager.executeAutoGemCombine(p, 3); break;
            case 3: AutoManager.executeAutoGemCombine(p, 4); break;
            case 4: AutoManager.executeAutoGemCombine(p, 5); break;
            case 5: AutoManager.executeAutoGemCombine(p, 6); break;
            case 6: AutoManager.executeAutoGemCombine(p, 7); break;
            case 7: AutoManager.executeAutoGemCombine(p, 0); break;
            case 8: showGemCombineHelp(p); break;
            case 9: openAutoMainMenu(p); break;
        }
    }

    // ==============================================================
    //  8. AUTO GHÉP NGUYÊN LIỆU CHẾ TẠO
    // ==============================================================

    public static void openMaterialCraftMenu(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_AUTO_MATERIAL_CRAFT,
            "AUTO GHÉP NGUYÊN LIỆU CHẾ TẠO",
            new String[]{
                "Ghép Bột Cường Hóa (5 Đá Ngũ Sắc)",
                "Ghép Bột Tím (5 Bột Than + 1 Bột CH)",
                "Ghép Đá Ác Quỷ (5 Tinh Thể)",
                "Tách Đá Ác Quỷ (1 Đá Ác Quỷ -> 4 Tinh Thể)",
                "Ghép Thiên Thạch May Mắn (5 Ngôi Sao)",
                "GHÉP TOÀN BỘ NGUYÊN LIỆU KHẢ DỤNG",
                "Hướng Dẫn Ghép Nguyên Liệu",
                "Quay lại"
            },
            new short[]{110, 110, 133, 133, 133, 155, 118, 134}
        );
    }

    private void handleMaterialCraftMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: AutoManager.executeAutoMaterialCraft(p, 0); break;
            case 1: AutoManager.executeAutoMaterialCraft(p, 1); break;
            case 2: AutoManager.executeAutoMaterialCraft(p, 2); break;
            case 3: AutoManager.executeAutoMaterialCraft(p, 3); break;
            case 4: AutoManager.executeAutoMaterialCraft(p, 4); break;
            case 5: AutoManager.executeAutoMaterialCraft(p, 5); break;
            case 6: showMaterialCraftHelp(p); break;
            case 7: openAutoMainMenu(p); break;
        }
    }

    // ==============================================================
    //  9. AUTO CƯỜNG HÓA NHANH TRANG BỊ (+5 ĐẾN +15)
    // ==============================================================

    public static void openUpgradeSelectSlot(Player p) throws IOException {
        if (!AdminSystemMenu.isAdmin(p)) {
            p.getService().send_box_ThongBao_OK("Chức năng Auto Cường Hóa Nhanh chỉ hỗ trợ cho Admin!\nNgười chơi vui lòng tới NPC Thợ Rèn để cường hóa trang bị theo quy định.");
            return;
        }

        MenuSessionState session = getSession(p);
        session.cachedUpgradeSlots.clear();

        List<String> menuNames = new ArrayList<>();
        List<Short> menuIcons = new ArrayList<>();

        if (p.item != null && p.item.bag3 != null) {
            for (int i = 0; i < p.item.bag3.length; i++) {
                Item_wear it = p.item.bag3[i];
                // Chặn Quả Tim (typeEquip == 6 hoặc ID == 11000) và Dial (typeEquip == 7)
                if (it != null && it.template != null && it.template.typeEquip != 6 && it.template.typeEquip != 7 && it.template.id != 11000 && it.levelUp < 15) {
                    session.cachedUpgradeSlots.add(i);
                    menuNames.add(it.template.name + " (+" + it.levelUp + ")");
                    menuIcons.add((short) (it.template.icon > 0 ? it.template.icon : 110));
                }
            }
        }

        if (menuNames.isEmpty()) {
            menuNames.add("Không có trang bị nào có thể cường hóa");
            menuIcons.add((short) 118);
        }

        menuNames.add("Quay lại");
        menuIcons.add((short) 134);

        short[] iconsArr = new short[menuIcons.size()];
        for (int i = 0; i < menuIcons.size(); i++) iconsArr[i] = menuIcons.get(i);

        iMenuDymanic.buildAndSend(p, MENU_AUTO_UPGRADE_SELECT_SLOT,
            "CHỌN TRANG BỊ CẦN AUTO CƯỜNG HÓA",
            menuNames.toArray(new String[0]),
            iconsArr
        );
    }

    private void handleUpgradeSelectSlot(Player p, int index) throws IOException {
        MenuSessionState session = getSession(p);
        if (index >= 0 && index < session.cachedUpgradeSlots.size()) {
            session.selectedBagSlot = session.cachedUpgradeSlots.get(index);
            openUpgradeSelectTarget(p);
        } else {
            openAutoMainMenu(p);
        }
    }

    public static void openUpgradeSelectTarget(Player p) throws IOException {
        MenuSessionState session = getSession(p);
        if (p.item == null || session.selectedBagSlot < 0 || session.selectedBagSlot >= p.item.bag3.length) {
            openAutoMainMenu(p);
            return;
        }

        Item_wear it = p.item.bag3[session.selectedBagSlot];
        if (it == null) {
            openAutoMainMenu(p);
            return;
        }

        iMenuDymanic.buildAndSend(p, MENU_AUTO_UPGRADE_SELECT_TARGET,
            "MỤC TIÊU CHO [" + it.template.name + " +" + it.levelUp + "]",
            new String[]{
                "Đập lên +5",
                "Đập lên +7",
                "Đập lên +10",
                "Đập lên +12",
                "Đập lên +15",
                "Quay lại"
            },
            new short[]{110, 110, 110, 133, 133, 134}
        );
    }

    private void handleUpgradeSelectTarget(Player p, int index) throws IOException {
        MenuSessionState session = getSession(p);
        int targetLv = -1;
        switch (index) {
            case 0: targetLv = 5; break;
            case 1: targetLv = 7; break;
            case 2: targetLv = 10; break;
            case 3: targetLv = 12; break;
            case 4: targetLv = 15; break;
            case 5: openUpgradeSelectSlot(p); return;
        }

        if (targetLv > 0 && session.selectedBagSlot >= 0) {
            AutoManager.executeAutoUpgrade(p, session.selectedBagSlot, targetLv);
        }
    }

    // ==============================================================
    //  10. AUTO NÂNG CẤP DIAL (+1 ĐẾN +5)
    // ==============================================================

    public static void openDialSelectSlot(Player p) throws IOException {
        if (!AdminSystemMenu.isAdmin(p)) {
            p.getService().send_box_ThongBao_OK("Chức năng Auto Nâng Cấp Dial Nhanh chỉ hỗ trợ cho Admin!\nNgười chơi vui lòng tới NPC để nâng cấp Dial theo quy định.");
            return;
        }

        MenuSessionState session = getSession(p);
        session.cachedDialSlots.clear();

        List<String> menuNames = new ArrayList<>();
        List<Short> menuIcons = new ArrayList<>();

        if (p.item != null && p.item.bag3 != null) {
            for (int i = 0; i < p.item.bag3.length; i++) {
                Item_wear it = p.item.bag3[i];
                if (it != null && it.template != null && it.template.typeEquip == 7 && it.levelUp < 5) {
                    session.cachedDialSlots.add(i);
                    menuNames.add(it.template.name + " (+" + it.levelUp + "/5)");
                    menuIcons.add((short) (it.template.icon > 0 ? it.template.icon : 110));
                }
            }
        }

        if (menuNames.isEmpty()) {
            menuNames.add("Không tìm thấy Dial nào cần nâng cấp trong túi");
            menuIcons.add((short) 118);
        }

        menuNames.add("Quay lại");
        menuIcons.add((short) 134);

        short[] iconsArr = new short[menuIcons.size()];
        for (int i = 0; i < menuIcons.size(); i++) iconsArr[i] = menuIcons.get(i);

        iMenuDymanic.buildAndSend(p, MENU_AUTO_DIAL_SELECT,
            "CHỌN DIAL CẦN NÂNG CẤP",
            menuNames.toArray(new String[0]),
            iconsArr
        );
    }

    private void handleDialSelectSlot(Player p, int index) throws IOException {
        MenuSessionState session = getSession(p);
        if (index >= 0 && index < session.cachedDialSlots.size()) {
            session.selectedDialSlot = session.cachedDialSlots.get(index);
            openDialSelectTarget(p);
        } else {
            openAutoMainMenu(p);
        }
    }

    public static void openDialSelectTarget(Player p) throws IOException {
        MenuSessionState session = getSession(p);
        if (p.item == null || session.selectedDialSlot < 0 || session.selectedDialSlot >= p.item.bag3.length) {
            openAutoMainMenu(p);
            return;
        }

        Item_wear it = p.item.bag3[session.selectedDialSlot];
        if (it == null) {
            openAutoMainMenu(p);
            return;
        }

        iMenuDymanic.buildAndSend(p, MENU_AUTO_DIAL_TARGET,
            "MỤC TIÊU CHO [" + it.template.name + " +" + it.levelUp + "]",
            new String[]{
                "Nâng cấp lên +1",
                "Nâng cấp lên +2",
                "Nâng cấp lên +3",
                "Nâng cấp lên +4",
                "Nâng cấp lên +5 (Tối đa)",
                "Quay lại"
            },
            new short[]{110, 110, 110, 133, 155, 134}
        );
    }

    private void handleDialSelectTarget(Player p, int index) throws IOException {
        MenuSessionState session = getSession(p);
        int targetLv = -1;
        switch (index) {
            case 0: targetLv = 1; break;
            case 1: targetLv = 2; break;
            case 2: targetLv = 3; break;
            case 3: targetLv = 4; break;
            case 4: targetLv = 5; break;
            case 5: openDialSelectSlot(p); return;
        }

        if (targetLv > 0 && session.selectedDialSlot >= 0) {
            AutoManager.executeAutoDialUpgrade(p, session.selectedDialSlot, targetLv);
        }
    }

    // ==============================================================
    //  11. AUTO PHÂN GIẢI TRANG BỊ RA BỘT
    // ==============================================================

    public static void openDismantleMenu(Player p) throws IOException {
        iMenuDymanic.buildAndSend(p, MENU_AUTO_DISMANTLE_CONFIG,
            "AUTO PHÂN GIẢI TRANG BỊ RA BỘT",
            new String[]{
                "Tách Trang Bị Trắng/Xanh (+0) -> Bột Than/CH",
                "Tách Trang Bị Tím (+0) -> Bột Tím",
                "Tách Trang Bị Cam (+0) -> Bột Vàng",
                "PHÂN GIẢI TẤT CẢ TRANG BỊ RÁC (+0)",
                "Hướng Dẫn Phân Giải",
                "Quay lại"
            },
            new short[]{110, 133, 133, 155, 118, 134}
        );
    }

    private void handleDismantleMenu(Player p, int index) throws IOException {
        switch (index) {
            case 0: AutoManager.executeAutoDismantle(p, 1); break;
            case 1: AutoManager.executeAutoDismantle(p, 2); break;
            case 2: AutoManager.executeAutoDismantle(p, 3); break;
            case 3: AutoManager.executeAutoDismantle(p, 0); break;
            case 4: showDismantleHelp(p); break;
            case 5: openAutoMainMenu(p); break;
        }
    }

    // ==============================================================
    //  12. AUTO MỞ RƯƠNG HÀNG LOẠT
    // ==============================================================

    public static void openBatchBoxSelect(Player p) throws IOException {
        MenuSessionState session = getSession(p);
        session.cachedBoxList.clear();

        List<String> menuNames = new ArrayList<>();
        List<Short> menuIcons = new ArrayList<>();

        if (p.item != null && p.item.bag47 != null) {
            for (ItemBag47 it47 : p.item.bag47) {
                if (it47 != null && (it47.category == 4 || it47.category == 7)) {
                    String name = AutoManager.getItemNameFromKey(it47.category + ":" + it47.id);
                    if (name.toLowerCase().contains("rương") || name.toLowerCase().contains("hộp")
                            || name.toLowerCase().contains("gói") || name.toLowerCase().contains("quà")
                            || name.toLowerCase().contains("thẻ")) {
                        session.cachedBoxList.add(new int[]{it47.category, it47.id, it47.quant});
                        menuNames.add(name + " (Số lượng: " + it47.quant + ")");
                        menuIcons.add((short) 110);
                    }
                }
            }
        }

        if (menuNames.isEmpty()) {
            menuNames.add("Không tìm thấy rương/hộp quà nào trong túi");
            menuIcons.add((short) 118);
        }

        menuNames.add("Quay lại");
        menuIcons.add((short) 134);

        short[] iconsArr = new short[menuIcons.size()];
        for (int i = 0; i < menuIcons.size(); i++) iconsArr[i] = menuIcons.get(i);

        iMenuDymanic.buildAndSend(p, MENU_AUTO_BATCH_BOX_SELECT,
            "CHỌN RƯƠNG / HỘP QUÀ CẦN MỞ",
            menuNames.toArray(new String[0]),
            iconsArr
        );
    }

    private void handleBatchBoxSelect(Player p, int index) throws IOException {
        MenuSessionState session = getSession(p);
        if (index >= 0 && index < session.cachedBoxList.size()) {
            int[] boxData = session.cachedBoxList.get(index);
            session.selectedBoxCategory = (byte) boxData[0];
            session.selectedBoxId = (short) boxData[1];
            openBatchBoxCount(p, boxData[2]);
        } else {
            openAutoMainMenu(p);
        }
    }

    public static void openBatchBoxCount(Player p, int totalQuant) throws IOException {
        MenuSessionState session = getSession(p);
        String name = AutoManager.getItemNameFromKey(session.selectedBoxCategory + ":" + session.selectedBoxId);
        boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);

        String[] countNames = isTest
                ? new String[]{
                    "Mở 1 cái",
                    "Mở 10 cái",
                    "Mở 20 cái",
                    "Mở 50 cái",
                    "Mở 100 cái",
                    "Mở 200 cái",
                    "Mở 500 cái",
                    "Mở toàn bộ (" + totalQuant + " cái)",
                    "Quay lại"
                }
                : new String[]{
                    "Mở 1 cái",
                    "Mở 5 cái",
                    "Mở 10 cái",
                    "Quay lại"
                };

        short[] countIcons = isTest
                ? new short[]{110, 110, 110, 110, 133, 133, 155, 155, 134}
                : new short[]{110, 110, 110, 134};

        iMenuDymanic.buildAndSend(p, MENU_AUTO_BATCH_BOX_COUNT,
            "MỞ HÀNG LOẠT: " + name + " (Có: " + totalQuant + ")",
            countNames,
            countIcons
        );
    }

    private void handleBatchBoxCount(Player p, int index) throws IOException {
        MenuSessionState session = getSession(p);
        int totalQuant = p.item.total_item_bag_by_id(session.selectedBoxCategory, session.selectedBoxId);
        boolean isTest = core.Manager.gI().isTestMode() || (p.admin == 1);
        int count = 0;

        if (!isTest) {
            switch (index) {
                case 0: count = 1; break;
                case 1: count = 5; break;
                case 2: count = 10; break;
                case 3: openBatchBoxSelect(p); return;
            }
        } else {
            switch (index) {
                case 0: count = 1; break;
                case 1: count = 10; break;
                case 2: count = 20; break;
                case 3: count = 50; break;
                case 4: count = 100; break;
                case 5: count = 200; break;
                case 6: count = 500; break;
                case 7: count = totalQuant; break;
                case 8: openBatchBoxSelect(p); return;
            }
        }

        if (count > 0 && session.selectedBoxId > 0) {
            AutoManager.executeBatchOpenBoxes(p, session.selectedBoxCategory, session.selectedBoxId, count);
        }
    }

    // ==============================================================
    //  13. HỆ THỐNG HƯỚNG DẪN CHI TIẾT (HELP_FROM_SERVER)
    // ==============================================================

    public static void showAutoHelp(Player p) {
        if (p == null) return;
        try {
            String text = "HƯỚNG DẪN HỆ THỐNG AUTO TIỆN ÍCH\n\n"
                + "Chào mừng bạn đến với hệ thống Tự Động Hóa Hải Tặc Tí Hon!\b"
                + "1. TỰ ĐỘNG BÁN & XÓA ĐỒ:\n"
                + "- Tự động quét và dọn rác hành trang mỗi 3 giây.\n"
                + "- Có thể chọn bán ra Beri hoặc xóa hủy vĩnh viễn.\n"
                + "- Mức Cấp Độ Xóa: Tùy chỉnh xóa trang bị từ +0 đến +17.\n"
                + "- Chế Độ Học: Bật rồi vào Shop bán item, server sẽ tự ghi nhớ để tự động bán các lần sau.\b"
                + "2. BẢO VỆ TRANG BỊ QUÝ GIÁ:\n"
                + "- Trang bị đang MẶC TRÊN NGƯỜI tuyệt đối KHÔNG bị xóa hay phân giải.\n"
                + "- Trang bị Trái Tim và trang bị đã KHÓA tuyệt đối KHÔNG bị ảnh hưởng.\b"
                + "3. AUTO HỢP THÀNH ĐÁ KHẢM & NGUYÊN LIỆU:\n"
                + "- Tự động ghép đá khảm cấp 1 -> 6, Hải Thạch, Hổ Phách với 1 chạm.\n"
                + "- Tự động ghép Bột Cường Hóa, Bột Tím, Đá Ác Quỷ, Thiên Thạch May Mắn.\b"
                + "4. AUTO CƯỜNG HÓA & NÂNG CẤP DIAL:\n"
                + "- Cường hóa nhanh trang bị lên các mốc +5 đến +15.\n"
                + "- Nâng cấp Dial nhanh lên mốc +1 đến +5 an toàn, tự dừng khi đạt mốc hoặc hết nguyên liệu.\b"
                + "5. AUTO PHÂN GIẢI TRANG BỊ:\n"
                + "- Tách toàn bộ trang bị rác (+0) trong hành trang để nhận Bột Than, Bột Cường Hóa, Bột Tím, Bột Vàng.\b"
                + "6. AUTO NHẶT ĐỒ & HỒI SINH:\n"
                + "- Tự động nhặt đồ rơi quanh nhân vật (kèm bộ lọc: Tất cả, Chỉ Beri, Chỉ Trang Bị, Chỉ Đá/Nguyên Liệu).\n"
                + "- Tự động hồi sinh về làng khi tử trận.";
            p.getService().Help_From_Server(-997, text);
        } catch (Exception ignored) {}
    }

    public static void showDeleteHelp(Player p) {
        if (p == null) return;
        try {
            String text = "HƯỚNG DẪN TỰ ĐỘNG BÁN & XÓA ĐỒ\n\n"
                + "- Tự Động Xóa Trang Bị: Dọn toàn bộ trang bị trong túi có cấp cường hóa <= Mốc Cấp Độ đã chọn.\n"
                + "- Mốc Cấp Độ: Cho phép chọn từ +0 (chưa nâng cấp), +1, +3, +5, +7, +10, +12, +15, +16 đến +17 (tất cả cấp độ).\b"
                + "- Chế Độ Học Thông Minh: Bật 'Chế Độ Ghi Nhớ Khi Bán Shop', sau đó đến bất kỳ Shop nào và bán các món rác. Server sẽ tự động ghi nhớ toàn bộ các món đó vào danh sách Auto Bán. Khi xong, hãy Tắt Chế Độ Học để lưu.\b"
                + "- Bảo Vệ An Toàn: Trang bị đang mặc trên người, Trái Tim và trang bị đã KHÓA sẽ không bao giờ bị xóa!";
            p.getService().Help_From_Server(-997, text);
        } catch (Exception ignored) {}
    }

    public static void showGemCombineHelp(Player p) {
        if (p == null) return;
        try {
            String text = "HƯỚNG DẪN HỢP THÀNH ĐÁ KHẢM\n\n"
                + "- Quy tắc ghép: Cần tối thiểu 3 viên đá cùng loại để ghép lên 1 viên cấp cao hơn.\n"
                + "- Tỷ lệ thành công: Cấp 1->2 (100%), Cấp 2->3 (85%), Cấp 3->4 (70%), Cấp 4->5 (55%), Cấp 5->6 (40%).\n"
                + "- Khi thất bại: Chỉ tiêu hao 2 viên đá (giữ lại 1 viên).\n"
                + "- Siêu Tự Động: Tự động chạy dây chuyền nâng cấp toàn bộ đá từ cấp thấp lên cấp cao nhất có thể trong 1 chạm!";
            p.getService().Help_From_Server(-997, text);
        } catch (Exception ignored) {}
    }

    public static void showMaterialCraftHelp(Player p) {
        if (p == null) return;
        try {
            String text = "HƯỚNG DẪN GHÉP NGUYÊN LIỆU\n\n"
                + "- Bột Cường Hóa: 5 Đá Ngũ Sắc -> 1 Bột Cường Hóa\n"
                + "- Bột Tím: 5 Bột Than + 1 Bột Cường Hóa -> 1 Bột Tím\n"
                + "- Đá Ác Quỷ: 5 Tinh Thể Đá Ác Quỷ -> 1 Đá Ác Quỷ\n"
                + "- Tách Đá Ác Quỷ: 1 Đá Ác Quỷ -> 4 Tinh Thể\n"
                + "- Thiên Thạch May Mắn: 5 Ngôi Sao May Mắn -> 1 Thiên Thạch May Mắn\n"
                + "- Tính năng 'Ghép Toàn Bộ' sẽ tự tính số lượng tối đa ghép được và hoàn thành tức thì!";
            p.getService().Help_From_Server(-997, text);
        } catch (Exception ignored) {}
    }

    public static void showDismantleHelp(Player p) {
        if (p == null) return;
        try {
            String text = "HƯỚNG DẪN PHÂN GIẢI TRANG BỊ\n\n"
                + "- Cho phép tách các trang bị thừa/rác (+0) trong hành trang để thu về bột nguyên liệu:\n"
                + "  + Trang bị Trắng / Xanh lá: Nhận Bột than / Bột cường hóa\n"
                + "  + Trang bị Tím: Nhận Bột tím\n"
                + "  + Trang bị Cam: Nhận Bột vàng\n"
                + "- Bảo vệ an toàn: Trang bị đang mặc, trang bị Trái Tim, trang bị đã Khóa và trang bị đã cường hóa (> +0) sẽ không bao giờ bị phân giải.";
            p.getService().Help_From_Server(-997, text);
        } catch (Exception ignored) {}
    }

    public static void showOtherHelp(Player p) {
        if (p == null) return;
        try {
            String text = "HƯỚNG DẪN NHẶT ĐỒ & HỒI SINH\n\n"
                + "- Auto Nhặt Đồ: Tự động gom vật phẩm rơi trên bản đồ quanh nhân vật mỗi 1.2 giây.\n"
                + "- Bộ Lọc Nhặt Đồ: Tùy chọn lọc chỉ nhặt loại đồ mong muốn (Tất cả, Chỉ Beri/Ruby, Chỉ Trang Bị, Chỉ Đá/Nguyên Liệu).\n"
                + "- Auto Hồi Sinh: Khi nhân vật tử trận khi đánh boss/quái, sau thời gian đếm ngược sẽ tự động hồi sinh về làng an toàn.";
            p.getService().Help_From_Server(-997, text);
        } catch (Exception ignored) {}
    }

    // ==============================================================
    //  14. DỌN DẸP HÀNH TRANG NGAY (XÁC NHẬN, HƯỚNG DẪN & LƯU/XÓA)
    // ==============================================================

    public static void openCleanPromptMenu(Player p) throws IOException {
        if (p == null || p.autoSettings == null) return;

        StringBuilder sbSummary = new StringBuilder();
        sbSummary.append("XÁC NHẬN DỌN DẸP HÀNH TRANG\n");
        sbSummary.append("Hệ thống sẽ quét và dọn theo cấu hình:\n");
        sbSummary.append("• Xử lý: ").append(p.autoSettings.isAutoSellToBeri ? "Bán lấy Beri" : "Xóa vứt bỏ").append("\n");

        List<String> colors = new ArrayList<>();
        if (p.autoSettings.sellColorWhite) colors.add("Trắng");
        if (p.autoSettings.sellColorBlue) colors.add("Xanh");
        if (p.autoSettings.sellColorYellow) colors.add("Vàng");
        if (p.autoSettings.sellColorPurple) colors.add("Tím");
        if (p.autoSettings.sellColorOrange) colors.add("Cam");
        if (p.autoSettings.sellColorRed) colors.add("Đỏ");
        if (p.autoSettings.sellColorPink) colors.add("Hồng");
        if (p.autoSettings.sellColorGold) colors.add("Ánh Kim");
        if (p.autoSettings.sellColorGod) colors.add("Thần Trang");
        sbSummary.append("• Phẩm màu dọn: ").append(colors.isEmpty() ? "Không chọn phẩm nào" : String.join(", ", colors)).append("\n");

        String lvText = PlayerAutoSettings.getLevelFilterText(p.autoSettings.isKeepUpgradedGear, p.autoSettings.maxLevelUpToDelete);
        sbSummary.append("• Mốc cấp độ: ").append(lvText).append("\n");
        sbSummary.append("• Danh sách đen: ").append(p.autoSettings.blacklistItems.size()).append(" món\n");
        sbSummary.append("(*) Đồ đang mặc & đồ Trái Tim tuyệt đối AN TOÀN!\n");
        sbSummary.append("Chọn thao tác bạn muốn thực hiện:");

        iMenuDymanic.buildAndSend(p, MENU_AUTO_CLEAN_PROMPT,
            sbSummary.toString(),
            new String[]{
                "1. Dọn Dẹp & Lưu Vào Thùng Rác",
                "2. Dọn Dẹp & Xóa Vĩnh Viễn",
                "3. Tùy Chọn Muốn Dọn Những Gì",
                "4. Hướng Dẫn Chi Tiết Cho Người Mới",
                "Đóng"
            },
            new short[]{110, 118, 133, 118, 134}
        );
    }

    private void handleCleanPrompt(Player p, int index) throws IOException {
        switch (index) {
            case 0:
                // Dọn dẹp và lưu vào Thùng Rác (add_item_save)
                AutoManager.performAutoClean(p, true, true);
                break;
            case 1:
                // Dọn dẹp và xóa vĩnh viễn (không lưu thùng rác)
                AutoManager.performAutoClean(p, true, false);
                break;
            case 2:
                // Tùy chọn muốn dọn những gì -> chuyển đến Cài Đặt Bán & Xóa Đồ
                openDeleteConfig(p);
                break;
            case 3:
                // Hướng dẫn chi tiết cho người mới
                showCleanDetailHelp(p);
                break;
            case 4:
                // Đóng menu
                break;
        }
    }

    public static void showCleanDetailHelp(Player p) {
        if (p == null) return;
        try {
            String text = "HƯỚNG DẪN DỌN DẸP HÀNH TRANG CHO NGƯỜI MỚI\n\n"
                + "1. CÁC MỤC ĐƯỢC DỌN DẸP:\n"
                + "- Trang bị (Item 3): Được quét và dọn dựa theo Phẩm Màu (Trắng, Xanh, Vàng, Tím, Cam, Đỏ, Hồng, Ánh Kim, Thần Trang) và Mốc Cấp Độ (+0 đến +17) bạn đã cài đặt.\n"
                + "- Lỗ Khảm & Đá: Có thể tùy chọn giữ lại trang bị có lỗ khảm hoặc đã gắn đá quý.\n"
                + "- Danh Sách Chỉ Định (Blacklist): Các vật phẩm Dược Phẩm (Item 4) hoặc Nguyên Liệu (Item 7) bạn thêm vào danh sách đen sẽ được tự động dọn.\b"
                + "2. CƠ CHẾ BẢO VỆ AN TOÀN:\n"
                + "- Trang bị ĐANG MẶC TRÊN NGƯỜI: Tuyệt đối KHÔNG bao giờ bị dọn hay xóa.\n"
                + "- Trang bị TRÁI TIM: Tuyệt đối AN TOÀN.\n"
                + "- Trang bị ĐÃ KHÓA: Có tùy chọn 'Chỉ Không Khóa' để không bán đồ đã khóa.\b"
                + "3. PHƯƠNG THỨC DỌN DẸP:\n"
                + "- [Lưu Thùng Rác]: Trang bị sau khi dọn sẽ được lưu vào Thùng Rác (tối đa 90 món). Bạn có thể vào Cửa Hàng / Thùng Rác để chuộc lại nếu lỡ dọn nhầm.\n"
                + "- [Xóa Vĩnh Viễn]: Trang bị và vật phẩm sẽ bị xóa hủy hoàn toàn khỏi túi, không đưa vào thùng rác để giải phóng tối đa bộ nhớ.\b"
                + "4. CÁCH CÀI ĐẶT:\n"
                + "- Vào mục '3. Tùy Chọn Muốn Dọn Những Gì' hoặc 'Cài Đặt Bán & Xóa Đồ' để bật/tắt chính xác những món bạn muốn dọn.";
            p.getService().Help_From_Server(-996, text);
        } catch (Exception ignored) {}
    }
}
