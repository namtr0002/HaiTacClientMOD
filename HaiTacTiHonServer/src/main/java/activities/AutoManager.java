package activities;

import core.Log;
import core.ZUtil;
import itemz.Join_Item;
import itemz.Rebuild_Item;
import itemz.UpgradeDial;
import itemz.UpgradeItem;
import itemz.UpgradeSuperItem;
import model.Player;
import model.UseItem;
import network.Message;
import template.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AutoManager — Trung Tâm Xử Lý Tất Cả Tính Năng Tự Động (Auto Service Engine).
 * Cung cấp:
 * - Auto Xóa / Bán Trang Bị Cùi (< +17 hoặc mốc tùy chọn)
 * - Auto Xóa / Bán Vật Phẩm Danh Sách Đen (Blacklist)
 * - Auto Hợp Thành Đá Khảm (Cấp 1-6, Hải Thạch, Hổ Phách)
 * - Auto Ghép Nguyên Liệu (Bột CH, Bột Tím, Đá Ác Quỷ, Thiên Thạch)
 * - Auto Nâng Cấp Dial Nhanh (+1 đến +5)
 * - Auto Phân Giải Trang Bị Rác Ra Bột
 * - Auto Hồi Sinh Về Làng khi tử trận
 * - Auto Nhặt Đồ Rơi trên Map (Kèm Bộ Lọc Thông Minh)
 * - Auto Cường Hóa Nhanh (Đập Đồ Đến +17)
 * - Auto Mở Rương Hàng Loạt
 */
public class AutoManager {

    /**
     * Vòng lặp kiểm tra và thực thi các tiến trình Auto cho Player (gọi trong Player.update()).
     */
    public static void updatePlayerAuto(Player p) {
        if (p == null || p.isClosed || p.autoSettings == null) {
            return;
        }

        long now = System.currentTimeMillis();

        // 1. Auto Hồi Sinh Về Làng
        if (p.autoSettings.isAutoRevive && p.isdie) {
            if (p.time_hs_little_garden < now) {
                if (now >= p.autoSettings.lastAutoReviveTime + 4000L) {
                    p.autoSettings.lastAutoReviveTime = now;
                    triggerAutoRevive(p);
                }
            }
        }

        // 2. Auto Nhặt Đồ Rơi Xung Quanh (Kèm bộ lọc)
        if (p.autoSettings.isAutoLoot && !p.isdie && p.map != null && p.map.list_it_map != null) {
            if (now >= p.autoSettings.lastAutoLootTime + 1200L) {
                p.autoSettings.lastAutoLootTime = now;
                triggerAutoLoot(p);
            }
        }

        // 3. Auto Xóa Đồ / Dọn Rác Hành Trang
        if ((p.autoSettings.isAutoDeleteGear || p.autoSettings.isAutoDeleteBlacklist)) {
            if (now >= p.autoSettings.lastAutoCleanTime + 3000L) {
                p.autoSettings.lastAutoCleanTime = now;
                performAutoClean(p, false);
            }
        }
    }

    // ==============================================================
    //  1. THỰC THI AUTO HỒI SINH VỀ LÀNG
    // ==============================================================

    private static void triggerAutoRevive(Player p) {
        if (p.map == null) return;
        try {
            Message m = new Message((byte) 6, new byte[]{0}); // 0: hồi sinh về làng an toàn
            p.request_live_from_die(m);
            m.cleanup();
        } catch (Exception e) {
            Log.error("AutoManager", "triggerAutoRevive error: " + e.getMessage());
        }
    }

    // ==============================================================
    //  2. THỰC THI AUTO NHẶT ĐỒ RƠI TRÊN MAP (KÈM BỘ LỌC)
    // ==============================================================

    private static void triggerAutoLoot(Player p) {
        if (p.map == null || p.map.list_it_map == null) return;
        try {
            for (int i = 0; i < p.map.list_it_map.length; i++) {
                ItemMap itm = p.map.list_it_map[i];
                if (itm == null) continue;

                // Chỉ nhặt nếu là đồ của bản thân hoặc đồ vô chủ
                if (itm.id_master == -1 || itm.id_master == p.index_map) {
                    // Áp dụng bộ lọc lootFilterType:
                    // 0 = Tất cả
                    // 1 = Chỉ Beri / Ruby (category == 0)
                    // 2 = Chỉ Trang Bị (category == 3)
                    // 3 = Chỉ Đá & Nguyên Liệu (category == 4 || category == 7)
                    int filter = (p.autoSettings != null) ? p.autoSettings.lootFilterType : 0;
                    if (filter == 1 && itm.category != 0) continue;
                    if (filter == 2 && itm.category != 3) continue;
                    if (filter == 3 && itm.category != 4 && itm.category != 7) continue;

                    try {
                        byte[] data = new byte[3];
                        data[0] = (byte) ((itm.index >> 8) & 0xFF);
                        data[1] = (byte) (itm.index & 0xFF);
                        data[2] = itm.category;
                        Message m = new Message((byte) 0, data);
                        p.map.pick_item(p, m);
                        m.cleanup();
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception e) {
            Log.error("AutoManager", "triggerAutoLoot error: " + e.getMessage());
        }
    }

    // ==============================================================
    //  4. THỰC THI AUTO XÓA / BÁN ĐỒ RÁC & BLACKLIST (CHUẨN HÓA)
    // ==============================================================

    public static class CleanReport {
        public int gearCount = 0;
        public int otherItemCount = 0;
        public int item4Count = 0;
        public int item7Count = 0;
        public int[] gearByColor = new int[9]; // 0..8 (Trắng, Xanh, Vàng, Tím, Cam, Đỏ, Hồng, Ánh Kim, Thần Trang)
        public long totalBeriEarned = 0;
    }

    /**
     * Dọn dẹp túi đồ theo cấu hình Auto chuẩn hóa toàn diện.
     * @param p Player
     * @param notify Có gửi hộp thoại thông báo kết quả hay không.
     */
    public static CleanReport performAutoClean(Player p, boolean notify) {
        return performAutoClean(p, notify, true);
    }

    /**
     * Dọn dẹp túi đồ theo cấu hình Auto chuẩn hóa toàn diện.
     * @param p Player
     * @param notify Có gửi hộp thoại thông báo kết quả hay không.
     * @param saveToTrash true = Lưu vào Thùng Rác (add_item_save), false = Xóa vĩnh viễn.
     */
    public static CleanReport performAutoClean(Player p, boolean notify, boolean saveToTrash) {
        CleanReport report = new CleanReport();
        if (p == null || p.item == null || p.autoSettings == null) {
            return report;
        }

        boolean hasChanged = false;

        // 1. Quét trang bị (Item 3)
        if (p.item.bag3 != null) {
            for (int i = 0; i < p.item.bag3.length; i++) {
                Item_wear it = p.item.bag3[i];
                if (it == null || it.template == null) continue;

                // BẢO VỆ TUYỆT ĐỐI AN TOÀN TRANG BỊ:
                // - Không xóa nếu đang mặc trên người
                if (isEquippedOnBody(p, it)) continue;
                // - Không xóa nếu là vật phẩm Trái Tim
                if (it == p.item.it_heart) continue;
                // - Bảo vệ Thần Trang (Set Thần) nếu không chủ động bật bán Thần Trang
                if (it.isThanTrang() && !p.autoSettings.sellColorGod) continue;

                // - Kiểm tra Khóa an toàn:
                if (!p.autoSettings.isSellLockedGear && it.typelock == 1) continue;

                // - Kiểm tra Đá đã khảm:
                if (p.autoSettings.isProtectSocketedGems && it.mdakham != null && it.mdakham.length > 0) continue;

                // - Kiểm tra Lỗ khảm:
                int numHoles = it.numLoKham;
                if (p.autoSettings.holeFilterType == model.PlayerAutoSettings.HOLE_FILTER_NO_HOLES && numHoles > 0) continue;
                if (p.autoSettings.holeFilterType == model.PlayerAutoSettings.HOLE_FILTER_HAS_HOLES && numHoles == 0) continue;
                if (p.autoSettings.holeFilterType == model.PlayerAutoSettings.HOLE_FILTER_UNSOCKETED && it.mdakham != null && it.mdakham.length > 0) continue;

                // - Kiểm tra Cường hóa / LevelUp:
                if (p.autoSettings.isKeepUpgradedGear && it.levelUp > 0) continue;
                if (it.levelUp > p.autoSettings.maxLevelUpToDelete) continue;

                // - Kiểm tra Phẩm màu (Color):
                int color = it.getColor();
                boolean colorAllowed = p.autoSettings.isColorAllowedToSell(color);

                // - Kiểm tra Loại trang bị (TypeEquip):
                if (p.autoSettings.gearTypeEquipFilter >= 0 && it.template.typeEquip != p.autoSettings.gearTypeEquipFilter) continue;

                // Xác định điều kiện xóa/bán:
                boolean shouldDelete = false;

                // Điều kiện A: Auto bán trang bị theo bộ lọc phẩm màu
                if (p.autoSettings.isAutoDeleteGear && colorAllowed) {
                    shouldDelete = true;
                }

                // Điều kiện B: Thuộc danh sách chỉ định tự động bán
                if (p.autoSettings.isAutoDeleteBlacklist && isItemBlacklisted(p, 3, it.template.id)) {
                    shouldDelete = true;
                }

                if (shouldDelete) {
                    // Tính giá trị Beri nếu bật bán ra vàng
                    if (p.autoSettings.isAutoSellToBeri) {
                        int vang = 30 + (2 * it.getColor() + (it.template.level / 10) + 1)
                                * DataTemplate.TabInventory_ItemSell[0];
                        if (vang > DataTemplate.TabInventory_ItemSell[1]) {
                            vang = DataTemplate.TabInventory_ItemSell[1];
                        }
                        report.totalBeriEarned += vang;
                    }

                    if (saveToTrash) {
                        p.item.add_item_save(it);
                    }
                    p.item.bag3[i] = null;
                    report.gearCount++;
                    if (color >= 0 && color < report.gearByColor.length) {
                        report.gearByColor[color]++;
                    }
                    hasChanged = true;
                }
            }
        }

        // 2. Quét vật phẩm / dược phẩm / nguyên liệu (Category 4 & 7) theo Blacklist
        if (p.autoSettings.isAutoDeleteBlacklist && p.item.bag47 != null) {
            List<ItemBag47> toRemove = new ArrayList<>();
            for (ItemBag47 it47 : p.item.bag47) {
                if (it47 == null) continue;
                if (it47.category == 4 && !p.autoSettings.isAutoSellItem4) continue;
                if (it47.category == 7 && !p.autoSettings.isAutoSellItem7) continue;
                if (isItemBlacklisted(p, it47.category, it47.id)) {
                    toRemove.add(it47);
                }
            }

            for (ItemBag47 it47 : toRemove) {
                int quant = it47.quant;
                if (quant <= 0) continue;

                if (p.autoSettings.isAutoSellToBeri) {
                    report.totalBeriEarned += (long) DataTemplate.TabInventory_ItemSell[2] * quant;
                }

                p.item.remove_item47(it47.category, it47.id, quant);
                report.otherItemCount += quant;
                if (it47.category == 4) report.item4Count += quant;
                if (it47.category == 7) report.item7Count += quant;
                hasChanged = true;
            }
        }

        // 3. Cập nhật tài nguyên và hành trang
        if (hasChanged) {
            if (report.totalBeriEarned > 0) {
                p.update_vang(report.totalBeriEarned);
                try { p.updateMoney(); } catch (Exception ignored) {}
            }
            p.item.updateInventory(false);
        }

        if (notify) {
            try {
                if (report.gearCount == 0 && report.otherItemCount == 0) {
                    p.getService().send_box_ThongBao_OK("Hành trang của bạn đã sạch sẽ, không có rác nào cần dọn!");
                } else {
                    StringBuilder sb = new StringBuilder("KẾT QUẢ DỌN RÁC HÀNH TRANG:\n");
                    sb.append("• Phương thức: ").append(saveToTrash ? "Lưu Thùng Rác (có thể chuộc lại)\n" : "Xóa vĩnh viễn (không lưu)\n");
                    if (report.gearCount > 0) {
                        sb.append("- Đã xử lý: ").append(report.gearCount).append(" món Trang Bị");
                        List<String> colorDetails = new ArrayList<>();
                        for (int c = 0; c < report.gearByColor.length; c++) {
                            if (report.gearByColor[c] > 0) {
                                colorDetails.add(model.PlayerAutoSettings.getColorName(c) + ": " + report.gearByColor[c]);
                            }
                        }
                        if (!colorDetails.isEmpty()) {
                            sb.append(" (").append(String.join(", ", colorDetails)).append(")");
                        }
                        sb.append("\n");
                    }
                    if (report.item4Count > 0) {
                        sb.append("- Đã bán: ").append(report.item4Count).append(" Dược phẩm/Vật phẩm (Túi 4)\n");
                    }
                    if (report.item7Count > 0) {
                        sb.append("- Đã bán: ").append(report.item7Count).append(" Nguyên liệu/Đá (Túi 7)\n");
                    }
                    if (report.totalBeriEarned > 0) {
                        sb.append("- Thu về: +").append(String.format("%,d", report.totalBeriEarned)).append(" Beri!");
                    }
                    p.getService().send_box_ThongBao_OK(sb.toString().trim());
                }
            } catch (Exception ignored) {}
        }

        return report;
    }

    private static boolean isEquippedOnBody(Player p, Item_wear it) {
        if (p.item.it_body == null) return false;
        for (Item_wear bodyIt : p.item.it_body) {
            if (bodyIt != null && bodyIt.equals(it)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra một vật phẩm có nằm trong danh sách đen của Player hay không.
     */
    public static boolean isItemBlacklisted(Player p, int category, int id) {
        if (p.autoSettings == null || p.autoSettings.blacklistItems == null) return false;
        String key = category + ":" + id;
        return p.autoSettings.blacklistItems.contains(key);
    }

    /**
     * Thêm vật phẩm vào danh sách đen.
     */
    public static boolean addBlacklistItem(Player p, int category, int id) {
        if (p.autoSettings == null) return false;
        String key = category + ":" + id;
        return p.autoSettings.blacklistItems.add(key);
    }

    /**
     * Xóa vật phẩm khỏi danh sách đen.
     */
    public static boolean removeBlacklistItem(Player p, String key) {
        if (p.autoSettings == null) return false;
        return p.autoSettings.blacklistItems.remove(key);
    }

    /**
     * Lấy tên hiển thị chuẩn xác của một item key ("cat:id") kèm phẩm màu tiếng Việt.
     */
    public static String getItemNameFromKey(String key) {
        try {
            if (key.contains(":")) {
                String[] parts = key.split(":");
                int cat = Integer.parseInt(parts[0]);
                int id  = Integer.parseInt(parts[1]);
                if (cat == 3) {
                    ItemTemplate3 t = ItemTemplate3.get_it_by_id(id);
                    if (t != null) {
                        return "[Trang Bị] " + t.name + " (" + model.PlayerAutoSettings.getColorFullName(t.color) + ")";
                    }
                    return "Trang bị #" + id;
                } else if (cat == 4) {
                    ItemTemplate4 t = ItemTemplate4.get_it_by_id(id);
                    return t != null ? "[Vật Phẩm] " + t.name : "Vật phẩm #" + id;
                } else if (cat == 7) {
                    ItemTemplate7 t = ItemTemplate7.get_it_by_id(id);
                    return t != null ? "[Nguyên Liệu] " + t.name : "Nguyên liệu #" + id;
                }
            } else {
                int id = Integer.parseInt(key);
                ItemTemplate4 t4 = ItemTemplate4.get_it_by_id(id);
                if (t4 != null) return "[Vật Phẩm] " + t4.name;
                ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(id);
                if (t3 != null) return "[Trang Bị] " + t3.name + " (" + model.PlayerAutoSettings.getColorFullName(t3.color) + ")";
                ItemTemplate7 t7 = ItemTemplate7.get_it_by_id(id);
                if (t7 != null) return "[Nguyên Liệu] " + t7.name;
            }
        } catch (Exception ignored) {}
        return "Item [" + key + "]";
    }

    // ==============================================================
    //  5. AUTO CƯỜNG HÓA NHANH (AUTO UPGRADE TO +X)
    // ==============================================================

    /**
     * Tự động cường hóa một trang bị lên mốc targetLevel (tối đa +15).
     */
    public static void executeAutoUpgrade(Player p, int bagSlot, int targetLevel) {
        if (p == null || p.item == null || bagSlot < 0 || bagSlot >= p.item.bag3.length) {
            return;
        }

        if (!core.Manager.gI().isTestMode() && p.admin != 1) {
            try {
                p.getService().send_box_ThongBao_OK("Chức năng Auto Cường Hóa Nhanh chỉ hỗ trợ cho Admin!\nNgười chơi vui lòng tới NPC Thợ Rèn để cường hóa trang bị theo quy định.");
            } catch (Exception ignored) {}
            return;
        }

        Item_wear it = p.item.bag3[bagSlot];
        if (it == null || it.template == null) {
            try { p.getService().send_box_ThongBao_OK("Vật phẩm không tồn tại trong hành trang!"); } catch (Exception ignored) {}
            return;
        }

        if (it.template.typeEquip == 6 || it.template.typeEquip == 7 || it.template.id == 11000) {
            try { p.getService().send_box_ThongBao_OK("Vật phẩm Quả Tim (Type 6) / Dial không thể cường hóa!"); } catch (Exception ignored) {}
            return;
        }

        if (targetLevel > 15) targetLevel = 15;
        if (it.levelUp >= targetLevel) {
            try { p.getService().send_box_ThongBao_OK("Trang bị đã đạt hoặc vượt mốc +" + targetLevel + " rồi!"); } catch (Exception ignored) {}
            return;
        }

        int startLevel = it.levelUp;
        int successCount = 0;
        int failCount = 0;
        long totalBeriSpent = 0;
        int totalRubySpent = 0;
        int maxAttempts = 500; // Giới hạn số lần đập tối đa tránh treo vòng lặp

        String stopReason = "Đạt mốc mong muốn +" + targetLevel;

        for (int step = 0; step < maxAttempts; step++) {
            if (it.levelUp >= targetLevel) {
                break;
            }

            int currentLv = it.levelUp;

            // Phase 1: Nâng cấp thông thường từ +0 lên +10 (qua UpgradeItem)
            if (currentLv < 10) {
                if (currentLv >= UpgradeItem.DATA.size()) {
                    stopReason = "Đã đạt cấp tối đa của hệ thống thường!";
                    break;
                }

                int[] material_req = UpgradeItem.get_material(currentLv, it.getColor());
                if (material_req[0] == -1) {
                    stopReason = "Lỗi dữ liệu nguyên liệu!";
                    break;
                }

                int botReq = material_req[1];
                int daReq  = material_req[3];
                int beriReq = UpgradeItem.DATA.get(currentLv).beri;

                if (p.item.total_item_bag_by_id(7, material_req[0]) < botReq) {
                    stopReason = "Hết " + ItemTemplate7.get_it_by_id(material_req[0]).name + "!";
                    break;
                }
                if (p.item.total_item_bag_by_id(7, material_req[2]) < daReq) {
                    stopReason = "Hết " + ItemTemplate7.get_it_by_id(material_req[2]).name + "!";
                    break;
                }
                if (p.get_vang() < beriReq) {
                    stopReason = "Hết vàng Beri!";
                    break;
                }

                // Trừ chi phí
                p.update_vang(-beriReq);
                totalBeriSpent += beriReq;
                p.item.remove_item47(7, material_req[0], botReq);
                p.item.remove_item47(7, material_req[2], daReq);

                // Tính tỷ lệ thành công
                int per = UpgradeItem.DATA.get(currentLv).per;
                boolean suc = per > ZUtil.random(1000);

                if (suc) {
                    it.levelUp++;
                    successCount++;
                    p.updateArchiDaily(6);
                } else {
                    failCount++;
                    it.levelUp = UpgradeItem.DATA.get(currentLv).prelevel;
                }
            }
            // Phase 2: Siêu cường hóa từ +10 lên +15 (qua UpgradeSuperItem)
            else {
                if (it.levelUp >= 15) {
                    break;
                }

                int material_0 = UpgradeSuperItem.get_material(0, it);
                int material_1 = UpgradeSuperItem.get_material(1, it);
                int id_matrial_1 = it.getColor() >= 3 ? 4 : 3;
                int beriReq = UpgradeSuperItem.get_material(2, it);

                if (p.item.total_item_bag_by_id(7, 1) < material_0) {
                    stopReason = "Hết " + material_0 + " Đá Cường Hóa!";
                    break;
                }
                if (p.item.total_item_bag_by_id(7, id_matrial_1) < material_1) {
                    stopReason = "Hết " + material_1 + " " + ItemTemplate7.get_it_by_id(id_matrial_1).name + "!";
                    break;
                }
                if (p.get_vang() < beriReq) {
                    stopReason = "Hết vàng Beri!";
                    break;
                }

                // Trừ chi phí
                p.update_vang(-beriReq);
                totalBeriSpent += beriReq;
                p.item.remove_item47(7, 1, material_0);
                p.item.remove_item47(7, id_matrial_1, material_1);

                int percent_suc = 1;
                switch (it.levelUp) {
                    case 10: case 11: case 12: case 13:
                        percent_suc = 15;
                        break;
                    case 14:
                        percent_suc = 7;
                        break;
                }

                boolean suc = percent_suc > ZUtil.random(1000 + it.levelUp * 2);

                if (suc) {
                    it.levelUp++;
                    successCount++;
                    p.updateArchiDaily(6);
                } else {
                    failCount++;
                    int percent_decrease_level = (it.levelUp == 13 || it.levelUp == 14) ? 120 : 80;
                    if (it.levelUp > 10 && percent_decrease_level > ZUtil.random(120)) {
                        it.levelUp -= ZUtil.random(1, 4);
                        if (it.levelUp < 10) {
                            it.levelUp = 10;
                        }
                    }
                }
            }
        }

        // Cập nhật lại giao diện và tiền tệ của nhân vật
        try {
            p.updateMoney();
            p.item.updateInventory(false);
            p.setAbility();
            p.update_info_to_all();
            p.getService().send_box_ThongBao_OK(
                "KẾT QUẢ AUTO CƯỜNG HÓA:\n" +
                "- Trang bị: " + it.template.name + "\n" +
                "- Cấp độ: +" + startLevel + " -> +" + it.levelUp + "\n" +
                "- Thành công: " + successCount + " lần | Thất bại: " + failCount + " lần\n" +
                "- Tiêu hao: " + String.format("%,d", totalBeriSpent) + " Beri\n" +
                "- Trạng thái: " + stopReason
            );
        } catch (Exception ignored) {}
    }

    // ==============================================================
    //  6. AUTO MỞ RƯƠNG HÀNG LOẠT
    // ==============================================================

    /**
     * Mở rương hoặc hộp quà hàng loạt.
     */
    public static void executeBatchOpenBoxes(Player p, byte category, short itemId, int count) {
        if (p == null || p.item == null || count <= 0) return;

        int totalInBag = p.item.total_item_bag_by_id(category, itemId);
        if (totalInBag < count) {
            count = totalInBag;
        }

        if (count <= 0) {
            try { p.getService().send_box_ThongBao_OK("Bạn không có vật phẩm này trong hành trang!"); } catch (Exception ignored) {}
            return;
        }

        int openedCount = 0;
        for (int i = 0; i < count; i++) {
            if (p.item.total_item_bag_by_id(category, itemId) <= 0) {
                break;
            }
            try {
                if (category == 4) {
                    boolean ok = UseItem.use_item_potion(p, itemId);
                    if (!ok) break;
                } else if (category == 7) {
                    UseItem.use_item_7(p, itemId);
                }
                openedCount++;
            } catch (Exception e) {
                break;
            }
        }

        p.item.updateInventory(false);
        try {
            p.getService().send_box_ThongBao_OK("Đã mở thành công " + openedCount + " rương/hộp quà!");
        } catch (Exception ignored) {}
    }

    // ==============================================================
    //  7. AUTO HỢP THÀNH ĐÁ KHẢM (AUTO GEM FUSION)
    // ==============================================================

    /**
     * Tự động hợp thành đá khảm theo từng mốc hoặc toàn bộ.
     * @param p Player
     * @param mode 1..5: Cấp 1->2, 2->3, 3->4, 4->5, 5->6; 6: Hải Thạch; 7: Hổ Phách; 0: Siêu Tự Động tất cả
     */
    public static void executeAutoGemCombine(Player p, int mode) {
        if (p == null || p.item == null || p.item.bag47 == null) return;

        int totalCrafts = 0;
        int totalSuccess = 0;
        int totalFail = 0;
        int totalGemsUsed = 0;

        boolean didCombine = false;
        int maxPasses = (mode == 0) ? 6 : 1;

        for (int pass = 0; pass < maxPasses; pass++) {
            boolean passCombined = false;

            // Thu thập danh sách ID đá hợp lệ đang có trong túi
            List<Short> gemIdsInBag = new ArrayList<>();
            for (ItemBag47 it47 : p.item.bag47) {
                if (it47 != null && it47.category == 4 && it47.quant >= 3) {
                    if (!gemIdsInBag.contains(it47.id)) {
                        gemIdsInBag.add(it47.id);
                    }
                }
            }

            for (short id : gemIdsInBag) {
                int tier = -1;
                boolean matchMode = false;

                // 1. Đá khảm thường (44..78)
                if (id >= 44 && id <= 78) {
                    int t = (id - 44) % 6;
                    if (t < 5) {
                        tier = t;
                        if (mode == 0 || mode == (t + 1)) {
                            matchMode = true;
                        }
                    }
                }
                // 2. Hải Thạch (221..225)
                else if (id >= 221 && id <= 225) {
                    tier = id - 221;
                    if (mode == 0 || mode == 6) {
                        matchMode = true;
                    }
                }
                // 3. Hổ Phách (362..366)
                else if (id >= 362 && id <= 366) {
                    tier = id - 362;
                    if (mode == 0 || mode == 7) {
                        matchMode = true;
                    }
                }

                if (!matchMode || tier < 0 || tier >= Rebuild_Item.PERCENT_HOP_NGOC.length) continue;

                int quant = p.item.total_item_bag_by_id(4, id);
                if (quant < 3) continue;

                int percent = Rebuild_Item.PERCENT_HOP_NGOC[tier];
                int suc = 0;
                int fail = 0;

                int workingQuant = quant;
                while (workingQuant >= 3) {
                    if (percent > ZUtil.random(120)) {
                        suc++;
                    } else {
                        fail++;
                    }
                    workingQuant -= 3;
                }

                int numUsed = (2 * fail) + (3 * suc);
                if (numUsed > 0) {
                    p.item.remove_item47(4, id, numUsed);
                    if (suc > 0) {
                        p.item.add_item_bag47(4, (short) (id + 1), suc);
                    }
                    totalCrafts += (suc + fail);
                    totalSuccess += suc;
                    totalFail += fail;
                    totalGemsUsed += numUsed;
                    passCombined = true;
                    didCombine = true;
                }
            }

            if (!passCombined) break;
        }

        if (didCombine) {
            p.item.updateInventory(false);
            try {
                p.getService().send_box_ThongBao_OK(
                    "KẾT QUẢ AUTO HỢP THÀNH ĐÁ KHẢM:\n" +
                    "- Tổng lượt ghép: " + totalCrafts + " lần\n" +
                    "- Thành công: " + totalSuccess + " lần\n" +
                    "- Thất bại: " + totalFail + " lần\n" +
                    "- Đã tiêu hao: " + totalGemsUsed + " viên đá\n" +
                    "- Tỷ lệ thành công: " + (totalCrafts > 0 ? (totalSuccess * 100 / totalCrafts) : 0) + "%"
                );
            } catch (Exception ignored) {}
        } else {
            try {
                p.getService().send_box_ThongBao_OK("Không tìm thấy đủ đá khảm (tối thiểu 3 viên cùng loại) để hợp thành!");
            } catch (Exception ignored) {}
        }
    }

    // ==============================================================
    //  8. AUTO GHÉP NGUYÊN LIỆU (AUTO MATERIAL CRAFTING)
    // ==============================================================

    /**
     * Tự động ghép nguyên liệu chế tạo từ Join_Item.
     */
    public static void executeAutoMaterialCraft(Player p, int recipeIndex) {
        if (p == null || p.item == null) return;

        int totalCrafted = 0;
        List<String> reportLines = new ArrayList<>();

        int[] recipesToRun = (recipeIndex == 5) ? new int[]{0, 1, 2, 4} : new int[]{recipeIndex};

        for (int rIndex : recipesToRun) {
            if (rIndex < 0 || rIndex >= Join_Item.ID.length) continue;

            short targetId = Join_Item.ID[rIndex][0];
            short targetGainPerCraft = Join_Item.NUM[rIndex][0];

            // Tính số lượng tối đa có thể ghép
            int maxPossible = Integer.MAX_VALUE;
            for (int j = 1; j < Join_Item.ID[rIndex].length; j++) {
                short reqId = Join_Item.ID[rIndex][j];
                short reqNum = Join_Item.NUM[rIndex][j];
                int available = p.item.total_item_bag_by_id(7, reqId);
                int possible = available / reqNum;
                if (possible < maxPossible) {
                    maxPossible = possible;
                }
            }

            if (maxPossible > 0 && maxPossible != Integer.MAX_VALUE) {
                // Trừ nguyên liệu
                for (int j = 1; j < Join_Item.ID[rIndex].length; j++) {
                    short reqId = Join_Item.ID[rIndex][j];
                    short reqNum = Join_Item.NUM[rIndex][j];
                    p.item.remove_item47(7, reqId, maxPossible * reqNum);
                }

                // Cộng thành phẩm
                int totalGain = maxPossible * targetGainPerCraft;
                p.item.add_item_bag47(7, targetId, totalGain);

                String resName = ItemTemplate7.get_it_by_id(targetId).name;
                reportLines.add("+ Ghép " + maxPossible + " lần -> +" + totalGain + " " + resName);
                totalCrafted += maxPossible;
            }
        }

        if (totalCrafted > 0) {
            p.item.updateInventory(false);
            StringBuilder sb = new StringBuilder("KẾT QUẢ AUTO GHÉP NGUYÊN LIỆU:\n");
            for (String line : reportLines) {
                sb.append(line).append("\n");
            }
            try { p.getService().send_box_ThongBao_OK(sb.toString().trim()); } catch (Exception ignored) {}
        } else {
            try { p.getService().send_box_ThongBao_OK("Bạn không có đủ nguyên liệu để ghép vật phẩm này!"); } catch (Exception ignored) {}
        }
    }

    // ==============================================================
    //  9. AUTO NÂNG CẤP DIAL (AUTO UPGRADE DIAL)
    // ==============================================================

    /**
     * Tự động nâng cấp Dial trong hành trang lên mốc targetLevel (+1 đến +5).
     */
    public static void executeAutoDialUpgrade(Player p, int bagSlot, int targetLevel) {
        if (p == null || p.item == null || bagSlot < 0 || bagSlot >= p.item.bag3.length) return;

        if (!core.Manager.gI().isTestMode() && p.admin != 1) {
            try {
                p.getService().send_box_ThongBao_OK("Chức năng Auto Nâng Cấp Dial chỉ hỗ trợ cho Admin!\nNgười chơi vui lòng tới NPC để nâng cấp Dial theo quy định.");
            } catch (Exception ignored) {}
            return;
        }

        Item_wear it = p.item.bag3[bagSlot];
        if (it == null || it.template == null || it.template.typeEquip != 7) {
            try { p.getService().send_box_ThongBao_OK("Vui lòng chọn một trang bị Dial hợp lệ!"); } catch (Exception ignored) {}
            return;
        }

        if (targetLevel > 5) targetLevel = 5;
        if (it.levelUp >= targetLevel) {
            try { p.getService().send_box_ThongBao_OK("Dial đã đạt hoặc vượt mốc +" + targetLevel + " rồi!"); } catch (Exception ignored) {}
            return;
        }

        int maxAttempts = 300;
        UpgradeDial.executeAutoUpgradeDial(p, (short) bagSlot, maxAttempts, targetLevel);
    }

    // ==============================================================
    //  10. AUTO PHÂN GIẢI TRANG BỊ RA BỘT (AUTO DISMANTLE GEAR)
    // ==============================================================

    /**
     * Tự động phân giải trang bị rác (+0) trong hành trang để nhận Bột Cường Hóa / Bột Tím / Bột Vàng.
     * @param p Player
     * @param colorFilter 1: Trắng/Xanh, 2: Tím, 3: Cam, 0: Tất cả (+0)
     */
    public static void executeAutoDismantle(Player p, int colorFilter) {
        if (p == null || p.item == null || p.item.bag3 == null) return;

        int dismantledCount = 0;
        int botCHGain = 0;
        int botTimGain = 0;
        int botVangGain = 0;

        for (int i = 0; i < p.item.bag3.length; i++) {
            Item_wear it = p.item.bag3[i];
            if (it == null || it.template == null) continue;

            // Bảo vệ an toàn tuyệt đối:
            if (isEquippedOnBody(p, it)) continue;
            if (it == p.item.it_heart) continue;
            if (it.typelock == 1) continue;
            if (it.levelUp > 0) continue; // Chỉ tách đồ +0 để bảo vệ đồ đã cường hóa

            int color = it.getColor();
            boolean match = false;
            if (colorFilter == 0) match = true;
            else if (colorFilter == 1 && color <= 1) match = true;
            else if (colorFilter == 2 && color == 2) match = true;
            else if (colorFilter == 3 && color >= 3) match = true;

            if (match) {
                p.item.add_item_save(it);
                p.item.bag3[i] = null;
                dismantledCount++;

                if (color >= 3) {
                    p.item.add_item_bag47(7, (short) 4, 1); // Bột vàng (ID 4)
                    botVangGain++;
                } else if (color == 2) {
                    p.item.add_item_bag47(7, (short) 3, 1); // Bột tím (ID 3)
                    botTimGain++;
                } else {
                    p.item.add_item_bag47(7, (short) 2, 1); // Bột than/CH (ID 2)
                    botCHGain++;
                }
            }
        }

        if (dismantledCount > 0) {
            p.item.updateInventory(false);
            StringBuilder sb = new StringBuilder("KẾT QUẢ PHÂN GIẢI TRANG BỊ:\n");
            sb.append("- Đã phân giải: ").append(dismantledCount).append(" món trang bị rác\n");
            if (botCHGain > 0) sb.append("- Nhận được: +").append(botCHGain).append(" Bột than\n");
            if (botTimGain > 0) sb.append("- Nhận được: +").append(botTimGain).append(" Bột tím\n");
            if (botVangGain > 0) sb.append("- Nhận được: +").append(botVangGain).append(" Bột vàng\n");
            try { p.getService().send_box_ThongBao_OK(sb.toString().trim()); } catch (Exception ignored) {}
        } else {
            try { p.getService().send_box_ThongBao_OK("Không tìm thấy trang bị rác (+0) phù hợp để phân giải!"); } catch (Exception ignored) {}
        }
    }
}
