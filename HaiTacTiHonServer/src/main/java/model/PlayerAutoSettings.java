package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * PlayerAutoSettings — Cấu hình hệ thống Auto toàn diện dành cho từng nhân vật.
 * Được lưu trữ bền vững vào Database (cột inventory trong bảng player).
 */
public class PlayerAutoSettings {

    // =========================================================================
    //  1. CẤU HÌNH AUTO BÁN & XÓA ĐỒ (AUTO SELL / CLEAN GEAR & ITEMS)
    // =========================================================================

    // --- Master Switches ---
    public boolean isAutoDeleteGear = false;       // Tự động quét bán trang bị theo bộ lọc (mặc định TẮT)
    public boolean isAutoDeleteBlacklist = false;  // Tự động quét bán các item trong danh sách chỉ định
    public boolean isAutoSellItem4 = true;         // Cho phép bán vật phẩm/dược phẩm (Item 4) trong danh sách
    public boolean isAutoSellItem7 = true;         // Cho phép bán nguyên liệu/đá (Item 7) trong danh sách
    public boolean isAutoSellToBeri = true;        // Bán lấy Beri thay vì vứt bỏ trắng
    public boolean isAutoSellMode = false;         // Chế độ học: bán item trong shop -> tự ghi vào danh sách

    // --- Danh sách vật phẩm tự bán chỉ định ("cat:id" ví dụ "3:100", "4:207", "7:5") ---
    public Set<String> blacklistItems = new LinkedHashSet<>();

    // --- Bộ Lọc Phẩm Màu (Color Filter) Cho Trang Bị (Item 3) ---
    public boolean sellColorWhite  = true;  // Color 0: Trắng (Thường) - Mặc định BẬT
    public boolean sellColorBlue   = true;  // Color 1: Xanh (Hiếm) - Mặc định BẬT
    public boolean sellColorYellow = false; // Color 2: Vàng (Hoàng Kim) - Mặc định TẮT
    public boolean sellColorPurple = false; // Color 3: Tím (Siêu Cấp) - Mặc định TẮT
    public boolean sellColorOrange = false; // Color 4: Cam (Truyền Thuyết) - Mặc định TẮT
    public boolean sellColorRed    = false; // Color 5: Đỏ (Thần Thoại) - Mặc định TẮT
    public boolean sellColorPink   = false; // Color 6: Hồng (Cực Phẩm) - Mặc định TẮT
    public boolean sellColorGold   = false; // Color 7: Ánh Kim (Tối Thượng) - Mặc định TẮT
    public boolean sellColorLightGold = false; // Alias
    public boolean sellColorGod    = false; // Color 8: Thần Trang (Set Thần) - Mặc định TẮT (Bảo vệ tuyệt đối)

    // --- Bộ Lọc Lỗ Khảm & Đá Đã Khảm (Socket / Gem Filter) ---
    public static final int HOLE_FILTER_ALL         = 0; // Tất cả (không phân biệt lỗ)
    public static final int HOLE_FILTER_NO_HOLES    = 1; // Chỉ bán đồ Không Có Lỗ
    public static final int HOLE_FILTER_HAS_HOLES   = 2; // Chỉ bán đồ Có Lỗ
    public static final int HOLE_FILTER_UNSOCKETED  = 3; // Chỉ bán đồ Chưa Khảm Đá
    public int holeFilterType = HOLE_FILTER_ALL;
    public boolean isProtectSocketedGems = true;          // Bảo vệ đồ đã khảm ngọc/đá (mặc định BẬT)

    // --- Bộ Lọc Khóa An Toàn (Lock Filter) ---
    public boolean isSellLockedGear = false;              // false: Chỉ bán đồ không khóa; true: Bán cả đồ khóa

    // --- Bộ Lọc Cường Hóa (LevelUp / Upgrade Filter) ---
    public boolean isKeepUpgradedGear = true;             // Bảo vệ đồ đã cường hóa (+1 trở lên) - Mặc định BẬT
    public int maxLevelUpToDelete = 0;                    // Mốc cường hóa tối đa để bán (0 = chỉ +0, tối đa 17)

    // --- Bộ Lọc Loại Trang Bị (TypeEquip Filter) ---
    public int gearTypeEquipFilter = -1;                  // -1: Tất cả; 0: Vũ khí; 1: Nón; 2: Áo; 3: Nhẫn; 4: Dây chuyền; 5: Giày; 7: Dial

    // =========================================================================
    //  2. AUTO BƠM MÁU & NĂNG LƯỢNG
    // =========================================================================
    public boolean isAutoHp = false;               // Tự động bơm máu
    public int     hpThresholdPercent = 40;        // Ngưỡng HP (< 40%)
    public boolean isAutoMp = false;               // Tự động bơm năng lượng
    public int     mpThresholdPercent = 30;        // Ngưỡng MP (< 30%)

    // =========================================================================
    //  3. AUTO TIỆN ÍCH KHÁC
    // =========================================================================
    public boolean isAutoRevive = false;           // Tự động hồi sinh về làng khi tử trận
    public boolean isAutoLoot = false;             // Tự động nhặt vật phẩm rơi xung quanh
    public int     lootFilterType = 0;             // 0: Tất cả, 1: Chỉ Beri, 2: Chỉ Trang Bị, 3: Chỉ Đá & Nguyên Liệu

    // =========================================================================
    //  4. RUNTIME COOLDOWNS & TIMESTAMPS (Không lưu DB)
    // =========================================================================
    public long lastAutoHpTime = 0;
    public long lastAutoMpTime = 0;
    public long lastAutoLootTime = 0;
    public long lastAutoCleanTime = 0;
    public long lastAutoReviveTime = 0;

    public PlayerAutoSettings() {}

    // =========================================================================
    //  5. HELPER METHODS: TÊN PHẨM MÀU, LỖ KHẢM, CƯỜNG HÓA
    // =========================================================================

    public static final String[] COLOR_NAMES = {
        "Trắng", "Xanh", "Vàng", "Tím", "Cam", "Đỏ", "Hồng", "Ánh Kim", "Thần Trang"
    };

    public static final String[] COLOR_FULL_NAMES = {
        "Trắng (Thường)",
        "Xanh (Hiếm)",
        "Vàng (Hoàng Kim)",
        "Tím (Siêu Cấp)",
        "Cam (Truyền Thuyết)",
        "Đỏ (Thần Thoại)",
        "Hồng (Cực Phẩm)",
        "Ánh Kim (Tối Thượng)",
        "Thần Trang (Set Thần)"
    };

    public static String getColorName(int color) {
        if (color >= 0 && color < COLOR_NAMES.length) {
            return COLOR_NAMES[color];
        }
        return "Màu #" + color;
    }

    public static String getColorFullName(int color) {
        if (color >= 0 && color < COLOR_FULL_NAMES.length) {
            return COLOR_FULL_NAMES[color];
        }
        return "Phẩm màu #" + color;
    }

    public boolean isColorAllowedToSell(int color) {
        switch (color) {
            case 0: return sellColorWhite;
            case 1: return sellColorBlue;
            case 2: return sellColorYellow;
            case 3: return sellColorPurple;
            case 4: return sellColorOrange;
            case 5: return sellColorRed;
            case 6: return sellColorPink;
            case 7: return sellColorGold;
            case 8: return sellColorGod;
            default: return false;
        }
    }

    public void setColorAllowedToSell(int color, boolean allow) {
        switch (color) {
            case 0: sellColorWhite = allow; break;
            case 1: sellColorBlue = allow; break;
            case 2: sellColorYellow = allow; break;
            case 3: sellColorPurple = allow; break;
            case 4: sellColorOrange = allow; break;
            case 5: sellColorRed = allow; break;
            case 6: sellColorPink = allow; break;
            case 7: sellColorGold = allow; break;
            case 8: sellColorGod = allow; break;
        }
    }

    public static String getHoleFilterName(int type) {
        switch (type) {
            case HOLE_FILTER_NO_HOLES:   return "Chỉ đồ Không Có Lỗ";
            case HOLE_FILTER_HAS_HOLES:  return "Chỉ đồ Có Lỗ Khảm";
            case HOLE_FILTER_UNSOCKETED: return "Chỉ đồ Chưa Khảm Đá";
            default:                     return "Tất cả (Không phân biệt)";
        }
    }

    public static String getLevelFilterText(boolean keepUpgraded, int maxLv) {
        if (keepUpgraded || maxLv <= 0) return "Chỉ đồ +0 (Bảo vệ đồ đã đập)";
        if (maxLv >= 17) return "Tất cả cấp độ (Đến +17)";
        return "Đến cấp +" + maxLv;
    }

    public static String getEquipTypeName(int type) {
        switch (type) {
            case 0: return "Vũ khí";
            case 1: return "Nón";
            case 2: return "Áo";
            case 3: return "Nhẫn";
            case 4: return "Dây chuyền";
            case 5: return "Giày";
            case 7: return "Dial";
            case 8: return "Thần Trang - Vũ Khí";
            case 9: return "Thần Trang - Nón";
            case 10: return "Thần Trang - Dây Chuyền";
            case 11: return "Thần Trang - Áo";
            case 12: return "Thần Trang - Nhẫn";
            case 13: return "Thần Trang - Quần";
            default: return "Tất Cả Loại Trang Bị";
        }
    }

    public static String getLockFilterName(boolean sellLocked) {
        return sellLocked ? "Cho phép bán cả đồ khóa" : "Chỉ bán đồ không khóa (An toàn)";
    }

    // =========================================================================
    //  6. SERIALIZATION (JSON DATABASE PERSISTENCE)
    // =========================================================================

    /**
     * Xuất dữ liệu cấu hình sang JSONObject để lưu trữ vào database.
     */
    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();

        // Master & Action Switches
        obj.put("del_gear", isAutoDeleteGear ? 1 : 0);
        obj.put("del_black", isAutoDeleteBlacklist ? 1 : 0);
        obj.put("sell_item4", isAutoSellItem4 ? 1 : 0);
        obj.put("sell_item7", isAutoSellItem7 ? 1 : 0);
        obj.put("sell_beri", isAutoSellToBeri ? 1 : 0);
        obj.put("auto_sell_mode", isAutoSellMode ? 1 : 0);

        // Color Filters
        obj.put("col_white",  sellColorWhite ? 1 : 0);
        obj.put("col_blue",   sellColorBlue ? 1 : 0);
        obj.put("col_yellow", sellColorYellow ? 1 : 0);
        obj.put("col_purple", sellColorPurple ? 1 : 0);
        obj.put("col_orange", sellColorOrange ? 1 : 0);
        obj.put("col_red",    sellColorRed ? 1 : 0);
        obj.put("col_pink",   sellColorPink ? 1 : 0);
        obj.put("col_gold",   sellColorGold ? 1 : 0);
        obj.put("col_god",    sellColorGod ? 1 : 0);

        // Hole & Socket Filters
        obj.put("hole_filter", holeFilterType);
        obj.put("protect_gem", isProtectSocketedGems ? 1 : 0);

        // Lock Filter
        obj.put("sell_locked", isSellLockedGear ? 1 : 0);

        // LevelUp & Upgrade Filters
        obj.put("keep_upgraded", isKeepUpgradedGear ? 1 : 0);
        obj.put("del_max_lv", maxLevelUpToDelete);

        // Equip Type Filter
        obj.put("equip_filter", gearTypeEquipFilter);

        // Blacklist Items
        JSONArray blArr = new JSONArray();
        for (String itemKey : blacklistItems) {
            if (itemKey != null && !itemKey.isBlank()) {
                blArr.add(itemKey);
            }
        }
        obj.put("blacklist", blArr);

        // Auto HP / MP
        obj.put("auto_hp", isAutoHp ? 1 : 0);
        obj.put("hp_pct", hpThresholdPercent);
        obj.put("auto_mp", isAutoMp ? 1 : 0);
        obj.put("mp_pct", mpThresholdPercent);

        // Auto Utilities
        obj.put("auto_revive", isAutoRevive ? 1 : 0);
        obj.put("auto_loot", isAutoLoot ? 1 : 0);
        obj.put("loot_filter", lootFilterType);

        return obj;
    }

    /**
     * Tải dữ liệu cấu hình từ JSONObject đọc từ database (tương thích ngược hoàn toàn).
     */
    public void fromJsonObject(JSONObject obj) {
        if (obj == null) return;
        try {
            // Mặc định luôn tắt auto bán khi vừa login lại để đảm bảo an toàn
            isAutoDeleteGear = false;
            isAutoDeleteBlacklist = false;
            isAutoSellMode = false;

            if (obj.containsKey("sell_item4")) {
                isAutoSellItem4 = ((Number) obj.get("sell_item4")).intValue() == 1;
            }
            if (obj.containsKey("sell_item7")) {
                isAutoSellItem7 = ((Number) obj.get("sell_item7")).intValue() == 1;
            }
            if (obj.containsKey("sell_beri")) {
                isAutoSellToBeri = ((Number) obj.get("sell_beri")).intValue() == 1;
            }

            // Colors
            if (obj.containsKey("col_white"))  sellColorWhite  = ((Number) obj.get("col_white")).intValue() == 1;
            if (obj.containsKey("col_blue"))   sellColorBlue   = ((Number) obj.get("col_blue")).intValue() == 1;
            if (obj.containsKey("col_yellow")) sellColorYellow = ((Number) obj.get("col_yellow")).intValue() == 1;
            if (obj.containsKey("col_purple")) sellColorPurple = ((Number) obj.get("col_purple")).intValue() == 1;
            if (obj.containsKey("col_orange")) sellColorOrange = ((Number) obj.get("col_orange")).intValue() == 1;
            if (obj.containsKey("col_red"))    sellColorRed    = ((Number) obj.get("col_red")).intValue() == 1;
            if (obj.containsKey("col_pink"))   sellColorPink   = ((Number) obj.get("col_pink")).intValue() == 1;
            if (obj.containsKey("col_gold"))   sellColorGold   = ((Number) obj.get("col_gold")).intValue() == 1;
            if (obj.containsKey("col_god"))    sellColorGod    = ((Number) obj.get("col_god")).intValue() == 1;

            // Hole & Gem
            if (obj.containsKey("hole_filter")) {
                holeFilterType = ((Number) obj.get("hole_filter")).intValue();
            }
            if (obj.containsKey("protect_gem")) {
                isProtectSocketedGems = ((Number) obj.get("protect_gem")).intValue() == 1;
            }

            // Lock
            if (obj.containsKey("sell_locked")) {
                isSellLockedGear = ((Number) obj.get("sell_locked")).intValue() == 1;
            }

            // LevelUp
            if (obj.containsKey("keep_upgraded")) {
                isKeepUpgradedGear = ((Number) obj.get("keep_upgraded")).intValue() == 1;
            }
            if (obj.containsKey("del_max_lv")) {
                maxLevelUpToDelete = ((Number) obj.get("del_max_lv")).intValue();
            }

            // Equip Type
            if (obj.containsKey("equip_filter")) {
                gearTypeEquipFilter = ((Number) obj.get("equip_filter")).intValue();
            }

            // Blacklist
            if (obj.containsKey("blacklist")) {
                blacklistItems.clear();
                Object blObj = obj.get("blacklist");
                if (blObj instanceof JSONArray) {
                    JSONArray arr = (JSONArray) blObj;
                    for (Object item : arr) {
                        if (item != null) {
                            blacklistItems.add(item.toString());
                        }
                    }
                }
            }

            // Auto HP / MP
            if (obj.containsKey("auto_hp")) {
                isAutoHp = ((Number) obj.get("auto_hp")).intValue() == 1;
            }
            if (obj.containsKey("hp_pct")) {
                hpThresholdPercent = ((Number) obj.get("hp_pct")).intValue();
            }
            if (obj.containsKey("auto_mp")) {
                isAutoMp = ((Number) obj.get("auto_mp")).intValue() == 1;
            }
            if (obj.containsKey("mp_pct")) {
                mpThresholdPercent = ((Number) obj.get("mp_pct")).intValue();
            }

            // Auto Utilities
            if (obj.containsKey("auto_revive")) {
                isAutoRevive = ((Number) obj.get("auto_revive")).intValue() == 1;
            }
            if (obj.containsKey("auto_loot")) {
                isAutoLoot = ((Number) obj.get("auto_loot")).intValue() == 1;
            }
            if (obj.containsKey("loot_filter")) {
                lootFilterType = ((Number) obj.get("loot_filter")).intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
