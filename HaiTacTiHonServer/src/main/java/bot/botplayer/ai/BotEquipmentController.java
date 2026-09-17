package bot.botplayer.ai;

import bot.botplayer.BotPlayerReal;
import bot.botplayer.GameAnalyzer;
import bot.botplayer.BotWorldAnnounce;
import template.Item_wear;
import template.ItemTemplate3;
import template.ItemBag47;
import model.UseItem;
import skill.Skill_info;
import core.ZUtil;

/**
 * BotEquipmentController — Quản lý việc tự động mặc đồ tốt hơn và nâng cấp trang bị.
 *
 * Chuẩn theo game logic thực tế:
 * - Hoàn Mỹ / Kích Ẩn: cần vật liệu ID 226 (cat 4) + 5 ruby, color >= 2, valueChetac >= 50
 * - Cường hóa Kỹ năng Ác Quỷ: cần 10x Đá Ác Quỷ (cat 7, id 9) + 50k vàng, xác suất 42%
 * - Tự động mở Rương Ác Quỷ / Rương Đại Ác Quỷ và ăn Trái Ác Quỷ
 */
public class BotEquipmentController {

    private final BotPlayerReal bot;

    // ID vật liệu kích ẩn / hoàn mỹ (cat 4): 221-226, ID 226 là ngọc xịn nhất
    private static final int KICH_AN_MATERIAL_ID = 226;
    // ID Đá Ác Quỷ (cat 7)
    private static final int DA_AC_QUY_ID = 9;
    private static final int DA_AC_QUY_CAT = 7;
    // Rương ác quỷ IDs (cat 4)
    private static final int RUONG_AC_QUY_ID = 29;
    private static final int RUONG_DAI_AC_QUY_ID = 158;
    // Trái ác quỷ IDs (cat 4)
    private static final int[] DEVIL_FRUIT_IDS = {86, 87, 92, 93, 219, 220, 316, 317};

    public BotEquipmentController(BotPlayerReal bot) {
        this.bot = bot;
    }

    /**
     * Tự động quét túi và mặc trang bị mạnh hơn (chỉ số CP cao hơn).
     */
    public void autoWearBetterGear() {
        if (bot.item == null || bot.item.bag3 == null) {
            return;
        }
        try {
            boolean changed = false;
            for (int slot = 0; slot <= 7; slot++) {
                Item_wear bestCandidate = GameAnalyzer.findBestItemForSlot(bot, slot);
                if (bestCandidate != null) {
                    bot.wear_item(bestCandidate);
                    changed = true;
                    GameAnalyzer.incrementGearChanges();
                }
            }
            if (changed) {
                bot.update_info_to_all();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra xem Bot có cần đi đập đồ không.
     */
    public boolean needsUpgrade() {
        if (bot.item == null || bot.item.it_body == null) {
            return false;
        }
        for (Item_wear it : bot.item.it_body) {
            if (it != null && it.template != null && it.getColor() >= 2 && it.levelUp < 5) {
                long upgradeCost = 500_000L + (it.levelUp * 200_000L);
                if (bot.get_vang() > upgradeCost) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Tự động nâng cấp trang bị mô phỏng thực tế (Cường hóa +1..+10).
     * Quy trình: Swap trang bị trắng vào -> tháo đồ xịn -> đập đồ -> mặc lại.
     */
    public void autoUpgradeGear() {
        if (bot.item == null || bot.item.it_body == null) {
            return;
        }
        try {
            // Tự mua bổ sung nguyên liệu cường hóa trước
            bot.getInventoryController().buyUpgradeMaterials();

            for (int slot = 0; slot < bot.item.it_body.length; slot++) {
                Item_wear equipped = bot.item.it_body[slot];
                if (equipped == null || equipped.template == null) {
                    continue;
                }
                if (equipped.getColor() < 2) {
                    continue; // Chỉ nâng cấp đồ xanh dương trở lên
                }
                if (equipped.levelUp >= 10) {
                    continue; // Đã tối đa cấp 10
                }
                long upgradeCost = 300_000L + (equipped.levelUp * 150_000L);
                if (bot.get_vang() < upgradeCost) {
                    continue;
                }

                // 1. Tạo trang bị trắng tạm thời để đổi chỗ tháo đồ xịn ra túi
                ItemTemplate3 whiteTemplate = null;
                if (ItemTemplate3.ENTRYS != null) {
                    for (ItemTemplate3 t : ItemTemplate3.ENTRYS) {
                        if (t != null && t.color == 0 && t.typeEquip == equipped.template.typeEquip
                                && (t.clazz == 0 || t.clazz == bot.clazz)) {
                            whiteTemplate = t;
                            break;
                        }
                    }
                }

                if (whiteTemplate != null) {
                    // Tạo Item_wear trắng
                    Item_wear whiteItem = new Item_wear();
                    whiteItem.setup_template_by_id(whiteTemplate);

                    // Đặt whiteItem vào túi bag3
                    for (int b = 0; b < bot.item.bag3.length; b++) {
                        if (bot.item.bag3[b] == null) {
                            bot.item.bag3[b] = whiteItem;
                            break;
                        }
                    }

                    // 2. Mặc trang bị trắng -> Đồ xịn tự rơi về bag3
                    bot.wear_item(whiteItem);

                    // 3. Tính toán và đập đồ
                    bot.update_vang(-upgradeCost);
                    bot.updateMoney();

                    int successRate = Math.max(20, 90 - (equipped.levelUp * 12));
                    boolean isSuccess = ZUtil.random(100) < successRate;

                    if (isSuccess) {
                        equipped.levelUp++;
                        GameAnalyzer.incrementGearUpgrades();
                        bot.getSocialController().chat("Đập thành công " + equipped.template.name + " lên +" + equipped.levelUp + "!");
                        // Phát thông báo kênh thế giới nếu đập lên mốc cao (+5/+8/+10)
                        if (equipped.levelUp == 5 || equipped.levelUp == 8 || equipped.levelUp == 10) {
                            BotWorldAnnounce.announceGearUpgrade(bot.name, equipped.template.name, equipped.levelUp);
                        }
                    } else {
                        if (equipped.levelUp > 0 && ZUtil.random(100) < 40) {
                            equipped.levelUp--;
                        }
                        bot.getSocialController().chat("Đập " + equipped.template.name + " bị xịt rồi!");
                    }

                    // 4. Mặc lại trang bị xịn từ bag3 lên người
                    bot.wear_item(equipped);

                    // 5. Dọn dẹp đồ trắng trong bag3
                    for (int b = 0; b < bot.item.bag3.length; b++) {
                        if (bot.item.bag3[b] == whiteItem) {
                            bot.item.bag3[b] = null;
                            break;
                        }
                    }

                    bot.update_info_to_all();
                    break; // Mỗi chu kỳ chỉ đập 1 món
                } else {
                    // Fallback nếu không tìm thấy mẫu đồ trắng
                    bot.update_vang(-upgradeCost);
                    bot.updateMoney();
                    int successRate = Math.max(20, 90 - (equipped.levelUp * 12));
                    if (ZUtil.random(100) < successRate) {
                        equipped.levelUp++;
                        GameAnalyzer.incrementGearUpgrades();
                        bot.getSocialController().chat("Đập thành công " + equipped.template.name + " lên +" + equipped.levelUp + "!");
                        if (equipped.levelUp == 5 || equipped.levelUp == 8 || equipped.levelUp == 10) {
                            BotWorldAnnounce.announceGearUpgrade(bot.name, equipped.template.name, equipped.levelUp);
                        }
                    }
                    bot.update_info_to_all();
                    break; // Mỗi chu kỳ chỉ đập 1 món
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tự động Hoàn Mỹ trang bị tím/cam.
     * Điều kiện chuẩn game: color >= 2, valueChetac >= 50, chưa isHoanMy.
     * Vật liệu: 1x Ngọc Cẩm Tinh (cat 4, ID 226) + 5 ruby.
     * Tỉ lệ thành công: 25/150 17% (chuẩn game).
     */
    public void autoHoanMyGear() {
        if (bot.item == null || bot.item.it_body == null) return;
        try {
            // Đảm bảo có vật liệu Hoàn Mỹ (Ngọc Cẩm Tinh ID 226)
            int matCount = bot.item.total_item_bag_by_id(4, KICH_AN_MATERIAL_ID);
            if (matCount < 1) {
                return; // Không có vật liệu thì không làm
            }

            for (Item_wear equipped : bot.item.it_body) {
                if (equipped == null || equipped.template == null) continue;
                if (equipped.getColor() < 2) continue;
                if (equipped.valueChetac < 50) continue;
                if (equipped.isHoanMy == 1) continue; // Đã hoàn mỹ rồi

                // Kiểm tra đủ ruby (5 ruby)
                if (bot.get_ngoc_val() < 5) {
                    return; // Không đủ ruby thì không hoàn mỹ
                }

                // Trừ vật liệu và ruby
                bot.item.remove_item47(4, KICH_AN_MATERIAL_ID, 1);
                bot.update_ngoc(-5);
                bot.updateMoney();

                // Xác suất thành công chuẩn game: 25/150 17%
                boolean success = ZUtil.random(150) < 25;
                if (success) {
                    equipped.isHoanMy = 1;
                    GameAnalyzer.incrementGearUpgrades();
                    bot.getSocialController().chat("Hoàn mỹ thành công " + equipped.template.name + "!");
                    BotWorldAnnounce.announceCustom(bot.name,
                        "[Hệ thống] " + bot.name + " vừa hoàn mỹ thành công " + equipped.template.name + " hoàn hảo!");
                } else {
                    // Thất bại: giảm điểm chế tác (1-5 điểm khi dùng ngọc 226)
                    int reduce = ZUtil.random(1, 6);
                    equipped.valueChetac = (short) Math.max(0, equipped.valueChetac - reduce);
                    bot.getSocialController().chat("Hoàn mỹ " + equipped.template.name + " thất bại, mất " + reduce + " điểm chế tác!");
                }
                bot.item.updateInventory(false);
                bot.update_info_to_all();
                break; // 1 món / tick
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tự động Kích Ẩn trang bị tím/cam.
     * Điều kiện chuẩn game: color >= 2, valueChetac >= 50, chưa có ẩn (valueKichAn == 0).
     * Vật liệu: 1x Ngọc Cẩm Tinh (cat 4, ID 226) + 5 ruby.
     * Tỉ lệ thành công: 25/120 21% (chuẩn game).
     */
    public void autoKichAnGear() {
        if (bot.item == null || bot.item.it_body == null) return;
        try {
            // Đảm bảo có vật liệu Kích Ẩn (Ngọc Cẩm Tinh ID 226)
            int matCount = bot.item.total_item_bag_by_id(4, KICH_AN_MATERIAL_ID);
            if (matCount < 1) {
                return;
            }

            for (Item_wear equipped : bot.item.it_body) {
                if (equipped == null || equipped.template == null) continue;
                if (equipped.getColor() < 2) continue;
                if (equipped.valueChetac < 50) continue;
                if (equipped.valueKichAn >= 0) continue; // Đã kích ẩn rồi (0..12)

                // Kiểm tra đủ ruby (5 ruby)
                if (bot.get_ngoc_val() < 5) {
                    return;
                }

                // Trừ vật liệu và ruby
                bot.item.remove_item47(4, KICH_AN_MATERIAL_ID, 1);
                bot.update_ngoc(-5);
                bot.updateMoney();

                // Xác suất thành công chuẩn game: 25/120 21%
                boolean success = ZUtil.random(120) < 25;
                if (success) {
                    equipped.valueKichAn = ZUtil.rollKichAn(); // 0-12
                    if (equipped.valueKichAn == 12) {
                        equipped.typelock = -1;
                    }
                    bot.getSocialController().chat("Kích ẩn thành công " + equipped.template.name + " (Ẩn " + equipped.valueKichAn + ")!");
                } else {
                    // Thất bại: giảm điểm chế tác (1-5 điểm khi dùng ngọc 226)
                    int reduce = ZUtil.random(1, 6);
                    equipped.valueChetac = (short) Math.max(0, equipped.valueChetac - reduce);
                    bot.getSocialController().chat("Kích ẩn " + equipped.template.name + " thất bại, mất " + reduce + " điểm chế tác!");
                }
                bot.item.updateInventory(false);
                bot.update_info_to_all();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tự động xử lý Trái ác quỷ, Rương ác quỷ & Nâng cấp kỹ năng ác quỷ.
     *
     * Chuẩn game logic:
     * 1. Mở Rương Ác Quỷ (ID 29) / Rương Đại Ác Quỷ (ID 158) qua UseItem.use_item_4
     * 2. Ghép nâng Rương Ác Quỷ -> Rương Đại Ác Quỷ khi có 10x Đá Ác Quỷ
     * 3. Ăn Trái Ác Quỷ nếu chưa có trái
     * 4. Cường hóa Kỹ Năng Ác Quỷ (lvdevil) chuẩn xác suất game: 50/120 42%
     *    Chi phí: 10x Đá Ác Quỷ + 50.000 vàng/lần thử
     */
    public void autoProcessDevilFruitsAndChests() {
        if (bot.item == null) return;
        try {
            // 1. Mở rương ác quỷ (ID 29 / 158) nếu có trong túi bag47
            if (bot.item.bag47 != null) {
                synchronized (bot.item.bag47) {
                    for (int i = 0; i < bot.item.bag47.size(); i++) {
                        ItemBag47 item = bot.item.bag47.get(i);
                        if (item != null && item.quant > 0
                                && (item.id == RUONG_AC_QUY_ID || item.id == RUONG_DAI_AC_QUY_ID)) {
                            // Mở rương qua UseItem (đúng game logic)
                            UseItem.use_item_4(bot, item.id);
                            bot.getSocialController().chat("Vừa mở " + (item.id == RUONG_DAI_AC_QUY_ID ? "Rương Đại Ác Quỷ" : "Rương Ác Quỷ") + "!");
                            break;
                        }
                    }
                }
            }

            // 2. Nâng cấp rương ác quỷ (ID 29 -> ID 158) khi có 10x Đá ác quỷ (cat 7, ID 9)
            int numRuong = bot.item.total_item_bag_by_id(4, RUONG_AC_QUY_ID);
            int numDa = bot.item.total_item_bag_by_id(DA_AC_QUY_CAT, DA_AC_QUY_ID);
            if (numRuong > 0 && numDa >= 10) {
                bot.item.remove_item47(DA_AC_QUY_CAT, DA_AC_QUY_ID, 10);
                bot.item.remove_item47(4, RUONG_AC_QUY_ID, 1);
                bot.item.add_item_bag47(4, RUONG_DAI_AC_QUY_ID, 1);
                bot.getSocialController().chat("Đã ghép thành công Rương Đại Ác Quỷ!");
                bot.item.updateInventory(false);
            }

            // 3. Tự động ăn Trái ác quỷ nếu có trong túi bag47 và chưa ăn trái nào
            if (bot.item.bag47 != null && !bot.check_already_have_devil_fruit()) {
                synchronized (bot.item.bag47) {
                    for (int i = 0; i < bot.item.bag47.size(); i++) {
                        ItemBag47 item = bot.item.bag47.get(i);
                        if (item != null && item.quant > 0 && isDevilFruitItem(item.id)) {
                            UseItem.use_item_4(bot, item.id);
                            bot.getSocialController().chat("Vừa ăn Trái Ác Quỷ xịn!!");
                            BotWorldAnnounce.announceCustom(bot.name,
                                "[Hệ thống] " + bot.name + " vừa hấp thụ thành công Trái Ác Quỷ!");
                            break;
                        }
                    }
                }
            }

            // 4. Cường hóa kỹ năng ác quỷ (lvdevil) chuẩn game
            // Chi phí: 10x Đá Ác Quỷ + 50.000 vàng. Tỉ lệ: 50/120 42%
            numDa = bot.item.total_item_bag_by_id(DA_AC_QUY_CAT, DA_AC_QUY_ID);
            if (bot.skill_point != null && numDa >= 10 && bot.get_vang() >= 50_000L) {
                for (Skill_info sk : bot.skill_point) {
                    if (sk != null && sk.temp != null && sk.lvdevil < 5) {
                        // Tính % tăng theo lvdevil (chuẩn game): lv0=10, lv1=8, lv2=6, lv3=5, lv4=4
                        int percentGain = switch (sk.lvdevil) {
                            case 0 -> 10;
                            case 1 -> 8;
                            case 2 -> 6;
                            case 3 -> 5;
                            default -> 4;
                        };

                        // Trừ nguyên liệu và vàng (dùng vàng thay vì ruby để tiết kiệm)
                        bot.item.remove_item47(DA_AC_QUY_CAT, DA_AC_QUY_ID, 10);
                        bot.update_vang(-50_000L);
                        bot.updateMoney();

                        // Xác suất chuẩn game: 50/120 42%
                        boolean suc = ZUtil.random(120) < 50;
                        if (suc) {
                            sk.devilpercent += percentGain;
                            if (sk.devilpercent >= 100) {
                                sk.devilpercent = 0;
                                sk.lvdevil++;
                                bot.getSocialController().chat("Kỹ năng " + sk.temp.name
                                    + " đã tăng lên Cường hóa Ác Quỷ Cap +" + sk.lvdevil + "!");
                            }
                            bot.send_skill();
                            bot.update_info_to_all();
                        }
                        bot.item.updateInventory(false);
                        break; // 1 skill / tick
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tự động Khảm Đá cấp cao (Tier 5/6 Gems) cho tất cả trang bị đang mặc chuẩn theo Class.
     */
    public void autoSocketGems() {
        if (bot.item == null || bot.item.it_body == null) return;
        try {
            boolean updated = false;
            for (int slot = 0; slot < bot.item.it_body.length; slot++) {
                Item_wear it = bot.item.it_body[slot];
                if (it != null && slot != 6) { // slot 6 = thời trang/danh hiệu
                    if (it.mdakham == null || it.mdakham.length == 0 || it.option_item_2 == null || it.option_item_2.isEmpty()) {
                        bot.setupBotItemGems(it, slot, Math.min(6, (bot.level / 20) + 2), bot.clazz);
                        updated = true;
                    }
                }
            }
            if (updated) {
                bot.setin4();
                bot.update_info_to_all();
            }
        } catch (Exception ignored) {}
    }

    private boolean isDevilFruitItem(int itemId) {
        for (int id : DEVIL_FRUIT_IDS) {
            if (itemId == id) return true;
        }
        return false;
    }
}
