package bot.botplayer;

import activities.Market;
import template.ItemMarket;
import template.PotionMarket;
import template.Item_wear;
import template.ItemBag47;
import core.ZUtil;
import database.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * BotMarket — Tự động giao dịch chợ cho bot.
 *
 * Chức năng:
 *  1. Treo đồ bán ở chợ (item xanh dương trở lên)
 *  2. Mua đồ tốt hơn đồ đang mặc nếu giá hợp lý
 *  3. Tự điều chỉnh giá theo thị trường học được từ GameAnalyzer
 *
 * Không tạo Thread — gọi từ BotPlayerReal state machine (state = MARKET).
 */
public class BotMarket {

    /** Thời gian treo đồ chợ: 24 giờ (ms) */
    private static final long MARKET_DURATION_MS = 24L * 60 * 60 * 1000;

    /** Màu tối thiểu để treo chợ (xanh dương = 2) */
    private static final int MIN_COLOR_TO_LIST = 2;

    /** Số item tối đa treo cùng lúc */
    private static final int MAX_LISTINGS = 5;

    /** Hệ số giá: giá bán = base_price * PRICE_MARKUP */
    private static final double PRICE_MARKUP = 1.15; // +15% so với giá học được

    // ========================= LIST FOR SALE =========================

    /**
     * Treo đồ bán ở chợ.
     * Ưu tiên đồ màu cao (cam > tím > xanh dương).
     * Giá được tính từ GameAnalyzer hoặc fallback formula.
     */
    public static void listItemsForSale(BotPlayerReal bot) {
        if (bot == null || bot.item == null || bot.item.bag3 == null) return;

        // Đếm số item đang treo chợ của bot
        int currentListings = countBotListings(bot.IDPlayer);
        if (currentListings >= MAX_LISTINGS) return;

        for (int i = 0; i < bot.item.bag3.length && currentListings < MAX_LISTINGS; i++) {
            Item_wear it = bot.item.bag3[i];
            if (it == null || it.template == null) continue;
            if (it.getColor() < MIN_COLOR_TO_LIST) continue; // chỉ treo đồ xanh dương+
            if (isItemEquipped(bot, it)) continue; // không treo đồ đang mặc

            // Tính giá
            int basePrice = calcBasePrice(it, bot.clazz);
            int listPrice = (int)(basePrice * PRICE_MARKUP);
            if (listPrice <= 0) continue;

            // Insert vào Market
            boolean listed = insertToMarket(bot, it, i, listPrice);
            if (listed) {
                bot.item.bag3[i] = null; // xóa khỏi túi
                currentListings++;
                GameAnalyzer.incrementMarketBuys();
            }
        }

        // Cập nhật túi nếu có thay đổi
        try {
            if (bot.item != null) bot.item.updateInventory(false);
        } catch (Exception ignored) {}
    }

    // ========================= BUY ITEMS =========================

    /**
     * Mua đồ tốt hơn đồ đang mặc từ chợ.
     * Chỉ mua khi: giá hợp lý và đồ thực sự tốt hơn theo điểm CP.
     */
    public static void buyUpgradeItems(BotPlayerReal bot) {
        if (bot == null) return;
        long botGold = bot.get_vang();
        if (botGold < 50000) return; // không đủ tiền

        try {
            // Quét tất cả market listings
            for (Market market : Market.ENTRY) {
                if (market == null || market.item3 == null) continue;
                for (int i = 0; i < market.item3.size(); i++) {
                    ItemMarket im = market.item3.get(i);
                    if (im == null || im.template == null) continue;
                    if (im.time_market < System.currentTimeMillis()) continue; // hết hạn
                    if (im.type_market == 2) continue; // đã bán
                    if (im.seller_id == bot.IDPlayer) continue; // của mình

                    if (im.template.level > bot.level) continue; // chưa đủ level mặc

                    int price = im.price_market;
                    if (price > botGold * 0.3) continue; // không mua quá 30% số vàng

                    // Kiểm tra có tốt hơn đồ đang mặc không
                    Item_wear currentGear = null;
                    if (bot.item != null && bot.item.it_body != null
                            && im.template.typeEquip >= 0
                            && im.template.typeEquip < bot.item.it_body.length) {
                        currentGear = bot.item.it_body[im.template.typeEquip];
                    }

                    if (GameAnalyzer.isBetterGearByTemplate(currentGear, im, bot.level, bot.clazz)) {
                        // Mua: trừ tiền, thêm vào túi
                        if (performBuy(bot, im, price)) {
                            bot.update_vang(-price);
                            GameAnalyzer.incrementMarketBuys();
                            break; // mua 1 lần rồi dừng
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Ignore market errors
        }
    }

    // ========================= AUTO SELL TRASH =========================

    /**
     * Tự động bán đồ rác (màu trắng + xanh lá chưa upgrade) tại NPC.
     * Tương tự BotPlayerReal.sellTrashItems() nhưng tính giá chi tiết hơn.
     */
    public static void sellTrashAtNpc(BotPlayerReal bot) {
        if (bot == null || bot.item == null || bot.item.bag3 == null) return;
        try {
            int soldCount = 0;
            long totalGold = 0;
            for (int i = 0; i < bot.item.bag3.length; i++) {
                Item_wear it = bot.item.bag3[i];
                if (it == null || it.template == null) continue;
                if (it.getColor() >= MIN_COLOR_TO_LIST) continue; // giữ lại đồ tốt
                if (it.levelUp > 0) continue; // giữ lại đồ đã nâng cấp

                // Công thức giá NPC
                int basePrice = 30 + (it.getColor() * 50) + (it.template.level * 2);
                int sellPrice = Math.min(basePrice, 5000);

                bot.update_vang(sellPrice);
                totalGold += sellPrice;
                bot.item.bag3[i] = null;
                soldCount++;
                GameAnalyzer.incrementTrashSold();
            }
            if (soldCount > 0) {
                bot.item.updateInventory(false);
                GameAnalyzer.addGoldEarned(totalGold);
            }
        } catch (Exception ignored) {}
    }

    // ========================= PRICE CALCULATION =========================

    /**
     * Tính giá cơ bản của item dựa vào:
     * - Màu sắc (rarity)
     * - Level yêu cầu
     * - Upgrade level
     * - Giá học được từ GameAnalyzer (nếu có)
     */
    private static int calcBasePrice(Item_wear it, int clazz) {
        if (it == null || it.template == null) return 0;

        // Lấy giá thị trường học được (nếu có)
        int learnedPrice = GameAnalyzer.getLearnedPrice((short) it.template.id, -1);
        if (learnedPrice > 0) return learnedPrice;

        // Fallback: công thức tính giá
        int colorBase = switch (it.getColor()) {
            case 0 -> 1_000;      // trắng
            case 1 -> 5_000;      // xanh lá
            case 2 -> 50_000;     // xanh dương
            case 3 -> 500_000;    // tím
            case 4 -> 3_000_000;  // cam
            case 5 -> 15_000_000; // vàng
            default -> 10_000;
        };
        int levelMulti = Math.max(1, it.template.level / 10 + 1);
        int upgradeBonus = it.levelUp * 200_000;
        int hoanMyBonus  = (it.isHoanMy == 1) ? colorBase / 2 : 0;

        return colorBase * levelMulti + upgradeBonus + hoanMyBonus;
    }

    // ========================= DB HELPERS =========================

    /**
     * Đếm số item đang treo chợ của bot.
     */
    private static int countBotListings(int botId) {
        int count = 0;
        try {
            for (Market market : Market.ENTRY) {
                if (market == null || market.item3 == null) continue;
                for (ItemMarket im : market.item3) {
                    if (im != null && im.seller_id == botId
                            && im.time_market > System.currentTimeMillis()
                            && im.type_market != 2) {
                        count++;
                    }
                }
            }
        } catch (Exception ignored) {}
        return count;
    }

    /**
     * Insert item vào Market.
     * Trả về true nếu thành công.
     */
    private static boolean insertToMarket(BotPlayerReal bot, Item_wear it, int bagSlot, int price) {
        try {
            int type_market = (it.template.typeEquip == 0 || it.template.typeEquip == 1 || it.template.typeEquip == 7) ? 0 :
                    ((it.template.typeEquip == 3 || it.template.typeEquip == 5) ? 1 : 2);
            Market targetMarket = Market.get_list_by_type(type_market);
            if (targetMarket == null) return false;

            // Tạo ItemMarket từ Item_wear
            ItemMarket im = new ItemMarket();
            im.clone_from_item_wear(it);
            if (im.template == null) return false;

            im.seller_id    = bot.IDPlayer;
            im.buyer_id     = -1;
            im.market_type  = (byte) type_market;
            im.price_market = price;
            im.time_market  = System.currentTimeMillis() + MARKET_DURATION_MS;
            im.type_market  = 1; // 1 = đang bán

            Market.insertItem(im);
            synchronized (targetMarket.item3) {
                targetMarket.item3.add(im);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Thực hiện mua item từ market.
     * Thêm item vào túi bot và đánh dấu đã bán.
     */
    private static boolean performBuy(BotPlayerReal bot, ItemMarket im, int price) {
        try {
            if (bot.item == null || bot.item.bag3 == null) return false;
            // Tìm slot trống trong túi và tạo lại Item_wear từ ItemMarket
            for (int i = 0; i < bot.item.bag3.length; i++) {
                if (bot.item.bag3[i] == null) {
                    // Tạo Item_wear mới từ ItemMarket
                    Item_wear newItem = new Item_wear();
                    newItem.template    = im.template;
                    newItem.levelUp     = im.levelUp;
                    newItem.typelock    = im.typelock;
                    newItem.numHoleDaDuc = im.numHoleDaDuc;
                    newItem.timeUse     = im.timeUse;
                    newItem.isHoanMy    = im.isHoanMy;
                    newItem.valueKichAn = im.valueKichAn;
                    newItem.option_item  = im.option_item;
                    newItem.option_item_2 = im.option_item_2;
                    newItem.mdakham     = im.mdakham;
                    newItem.numLoKham   = im.numLoKham;
                    newItem.index       = (short) i;

                    bot.item.bag3[i] = newItem;
                    im.buyer_id      = bot.IDPlayer;
                    im.time_market   = 0;
                    im.type_market   = 2; // đánh dấu đã bán
                    Market.updateItemStatus(im);
                    return true;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    // ========================= UTIL =========================

    /**
     * Kiểm tra item có đang được mặc không.
     */
    private static boolean isItemEquipped(BotPlayerReal bot, Item_wear it) {
        if (bot.item == null || bot.item.it_body == null) return false;
        for (Item_wear equipped : bot.item.it_body) {
            if (equipped != null && equipped == it) return true;
        }
        return false;
    }
}
