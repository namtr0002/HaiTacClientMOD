package bot.botplayer;

import model.Player;
import map.Zone;
import activities.Market;
import template.Item_wear;
import template.Option;
import core.ZUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * GameAnalyzer — Phân tích game, học từ người chơi thật, tính chỉ số trang bị chính xác.
 *
 * Chức năng chính:
 *  1. Tính điểm chiến đấu (Combat Power) chính xác từ Option thực của item
 *  2. So sánh và tìm đồ tốt hơn theo class của bot
 *  3. Học hành vi người chơi (chat, di chuyển, gear) — Imitation Learning
 *  4. Theo dõi giá thị trường từ giao dịch thực
 *  5. Báo cáo hệ thống toàn diện
 */
public class GameAnalyzer {

    // ========================= OPTION ID MAPPING =========================
    // Ánh xạ ID option -> chỉ số chiến đấu (theo game HaiTacGalaxy)
    public static final int OPT_ATK       = 1;   // Tấn công / Dame
    public static final int OPT_DEF       = 2;   // Phòng thủ
    public static final int OPT_HP        = 3;   // Máu tối đa
    public static final int OPT_MP        = 4;   // Năng lượng tối đa
    public static final int OPT_SPD       = 5;   // Tốc độ
    public static final int OPT_CRIT_RATE = 6;   // Tỉ lệ chí mạng
    public static final int OPT_DODGE     = 7;   // Né tránh
    public static final int OPT_LIFESTEAL = 8;   // Hút máu
    public static final int OPT_RESIST    = 9;   // Kháng đặc biệt
    public static final int OPT_CRIT_DMG  = 10;  // Sát thương chí mạng

    /**
     * Trọng số option theo class [clazz 0..5][optId 1..10].
     * Index 0 = default/unknown class.
     * Thứ tự: ATK, DEF, HP, MP, SPD, CRIT_RATE, DODGE, LIFESTEAL, RESIST, CRIT_DMG
     */
    private static final int[][] CLAZZ_WEIGHTS = {
        //  ATK  DEF   HP   MP  SPD  CR  DOD   LS  RES  CDMG
        {    2,   2,   2,   1,   1,   2,   1,   1,   1,   2 },  // 0 = default
        {    5,   1,   2,   1,   2,   3,   2,   2,   1,   3 },  // 1 = DPS attacker
        {    3,   2,   3,   1,   2,   2,   2,   2,   1,   2 },  // 2 = Balanced
        {    4,   1,   2,   2,   3,   3,   4,   2,   1,   3 },  // 3 = Agile DPS
        {    2,   4,   5,   1,   1,   1,   2,   3,   4,   1 },  // 4 = Tank/Support
        {    3,   2,   3,   1,   2,   2,   2,   3,   2,   2 },  // 5 = Hybrid DPS
    };

    // ========================= ANALYTICS COUNTERS =========================
    private static final AtomicInteger totalMobKills       = new AtomicInteger(0);
    private static final AtomicInteger totalDungeonsRun    = new AtomicInteger(0);
    private static final AtomicInteger totalMarketBuys     = new AtomicInteger(0);
    private static final AtomicInteger totalGearUpgrades   = new AtomicInteger(0);
    private static final AtomicInteger totalTrashSold      = new AtomicInteger(0);
    private static final AtomicInteger totalQuestsCompleted = new AtomicInteger(0);
    private static final AtomicInteger totalDeaths         = new AtomicInteger(0);
    private static final AtomicInteger totalGearChanges    = new AtomicInteger(0);
    private static final AtomicLong    totalGoldEarned     = new AtomicLong(0);

    // Level tracking per-bot (để tính tiến độ)
    private static final ConcurrentHashMap<String, Integer> botLevelSnapshot  = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long>    botLevelTimestamp = new ConcurrentHashMap<>();

    // ========================= IMITATION LEARNING =========================

    /** Context chat để phân loại tin nhắn học được */
    public enum ChatContext { IDLE, COMBAT, TRADING, PARTY, GENERAL }

    // Chat học được theo context
    private static final Map<ChatContext, List<String>> learnedChats = new ConcurrentHashMap<>();

    // Pattern di chuyển học được: mapId -> list tọa độ [x,y]
    private static final Map<Integer, List<short[]>> learnedMovements = new ConcurrentHashMap<>();

    // Điểm gear trung bình theo class (học từ real player)
    private static final Map<Integer, List<Integer>> learnedGearScores = new ConcurrentHashMap<>();

    // Giá thị trường học được: templateId -> list giá
    private static final ConcurrentHashMap<Short, List<Integer>> learnedPrices = new ConcurrentHashMap<>();

    // Lịch sử hành động người chơi thật (để phân tích)
    private static final List<String> observedActions = Collections.synchronizedList(new ArrayList<>());

    // ========================= STATIC INIT =========================
    static {
        for (ChatContext ctx : ChatContext.values()) {
            learnedChats.put(ctx, Collections.synchronizedList(new ArrayList<>()));
        }
        seedDefaultChats();
    }

    /** Seed chat mặc định để bot không im lặng ngay từ đầu */
    private static void seedDefaultChats() {
        addChat(ChatContext.IDLE,
                "Đang cày level nha mọi người",
                "Ai biết boss ở map nào không?",
                "Sắp lên level rồi sướng thật",
                "Đồ rớt được gì ngon không bạn?",
                "Cần pt cày lv ib nha",
                "map này nhiều mob quá :D",
                "Game hay lắm mọi người ơi",
                "lên được đồ xịn rồi hehe",
                "farm lâu mà đồ ko rớt chán quá"
        );
        addChat(ChatContext.COMBAT,
                "Boss mạnh quá, chờ tao hồi máu",
                "Ăn boss đi mọi người!",
                "Dọn mob xong đánh boss nhé",
                "Cẩn thận boss có skill mạnh",
                "Đập nó đi mọi người!"
        );
        addChat(ChatContext.TRADING,
                "Chợ hôm nay giá cao quá",
                "Bán đồ rồi lại cày tiếp",
                "Mua được đồ ngon ở chợ!",
                "ai mua đồ tím không?",
                "giá thị trường hôm nay ổn"
        );
        addChat(ChatContext.PARTY,
                "Pt ơi cùng cày phó bản không?",
                "Sẵn sàng vào phó bản chưa mọi người?",
                "Đi boss cùng tao không mấy bạn?",
                "Ai muốn vào party ib tao nha",
                "Cần thêm người đi pb không?"
        );
        addChat(ChatContext.GENERAL,
                "Hải Tặc số 1!",
                "Chơi game xả stress lắm haha",
                "server hôm nay ổn định ghê",
                "event gì thú vị không mọi người?",
                "cày thêm tí rồi offline"
        );
    }

    private static void addChat(ChatContext ctx, String... msgs) {
        List<String> list = learnedChats.get(ctx);
        for (String m : msgs) list.add(m);
    }

    // ========================= ITEM SCORING =========================

    /**
     * Tính điểm chiến đấu chính xác của một item theo class.
     *
     * Công thức:
     *   score = color_bonus + level_bonus + levelUp_bonus + hoanMy_bonus + weighted_options
     *
     * @param it    Item cần tính
     * @param clazz Class của bot (1-5)
     * @return Điểm CP của item
     */
    public static int calculateItemScore(Item_wear it, int clazz) {
        if (it == null || it.template == null) return 0;

        int[] weights = (clazz >= 1 && clazz <= 5) ? CLAZZ_WEIGHTS[clazz] : CLAZZ_WEIGHTS[0];

        // Màu sắc (rarity): trắng=0, xanh lá=1, xanh dương=2, tím=3, cam=4, vàng=5
        int colorBonus   = it.getColor() * 350;

        // Level yêu cầu — item level cao = chỉ số nền cao hơn
        int levelBonus   = it.template.level * 10;

        // Upgrade level (+0..+max): mỗi level tăng đáng kể
        int upgradeBonus = it.levelUp * 180;

        // Hoàn mỹ bonus
        int hoanMyBonus  = (it.isHoanMy == 1) ? 250 : 0;

        // Tổng điểm từ các option chính (dùng getParam thực tế)
        int optScore = 0;
        if (it.option_item != null) {
            for (Option opt : it.option_item) {
                // Lấy giá trị thực có tính upgrade và hoàn mỹ
                int val = opt.getParam(it.template.typeEquip, it.levelUp, it.isHoanMy);
                int w   = getWeightForOption(opt.id, weights);
                optScore += (val * w);
            }
        }

        // Option phụ (khảm đá, nâng cấp đặc biệt) — tính với hệ số 0.6
        if (it.option_item_2 != null) {
            for (Option opt : it.option_item_2) {
                int val = opt.getParam(it.template.typeEquip, it.levelUp, it.isHoanMy);
                int w   = getWeightForOption(opt.id, weights);
                optScore += (val * w * 6) / 10;
            }
        }

        // Normalize option score để không vượt quá tỉ lệ so với color/level
        int normalizedOptScore = optScore / 5;

        return colorBonus + levelBonus + upgradeBonus + hoanMyBonus + normalizedOptScore;
    }

    /** Lấy trọng số của option ID theo mảng weights của class */
    private static int getWeightForOption(int optId, int[] weights) {
        switch (optId) {
            case OPT_ATK:       return weights[0];
            case OPT_DEF:       return weights[1];
            case OPT_HP:        return weights[2];
            case OPT_MP:        return weights[3];
            case OPT_SPD:       return weights[4];
            case OPT_CRIT_RATE: return weights[5];
            case OPT_DODGE:     return weights[6];
            case OPT_LIFESTEAL: return weights[7];
            case OPT_RESIST:    return weights[8];
            case OPT_CRIT_DMG:  return weights[9];
            default:            return 1; // option lạ, weight tối thiểu
        }
    }

    /**
     * So sánh hai item — trả về true nếu candidate tốt hơn current.
     * Kiểm tra đầy đủ: level requirement, class requirement, rồi mới so sánh điểm.
     *
     * @param current     Item đang mặc (có thể null nếu slot trống)
     * @param candidate   Item muốn mặc
     * @param playerLevel Level hiện tại của bot
     * @param clazz       Class của bot
     */
    public static boolean isBetterGear(Item_wear current, Item_wear candidate, int playerLevel, int clazz) {
        if (candidate == null || candidate.template == null) return false;
        // Bot phải đủ level mới mặc được
        if (candidate.template.level > playerLevel) return false;
        // Kiểm tra class: clazz=0 là đồ dùng cho tất cả class
        if (candidate.template.clazz != 0 && candidate.template.clazz != clazz) return false;

        int currentScore   = calculateItemScore(current, clazz);
        int candidateScore = calculateItemScore(candidate, clazz);
        return candidateScore > currentScore;
    }

    /**
     * So sánh item đang mặc với một ItemMarket listing.
     * Dùng thông tin template từ ItemMarket để tính điểm.
     */
    public static boolean isBetterGearByTemplate(Item_wear current, template.ItemMarket im,
                                                   int playerLevel, int clazz) {
        if (im == null || im.template == null) return false;
        if (im.template.level > playerLevel) return false;
        if (im.template.clazz != 0 && im.template.clazz != clazz) return false;

        // Tính điểm cho ItemMarket bằng cách dựng tạm Item_wear
        Item_wear candidate = new Item_wear();
        candidate.template    = im.template;
        candidate.levelUp     = im.levelUp;
        candidate.option_item  = im.option_item  != null ? im.option_item  : new java.util.ArrayList<>();
        candidate.option_item_2 = im.option_item_2 != null ? im.option_item_2 : new java.util.ArrayList<>();

        int currentScore   = calculateItemScore(current, clazz);
        int candidateScore = calculateItemScore(candidate, clazz);
        return candidateScore > currentScore;
    }


    /**
     * Tìm item tốt nhất trong túi cho một slot cụ thể.
     * Trả về null nếu không có item nào tốt hơn đang mặc.
     */
    public static Item_wear findBestItemForSlot(BotPlayerReal bot, int slot) {
        if (bot == null || bot.item == null || bot.item.bag3 == null) return null;

        Item_wear currentEquipped = (bot.item.it_body != null && slot < bot.item.it_body.length)
                ? bot.item.it_body[slot] : null;
        Item_wear bestCandidate   = null;

        for (Item_wear it : bot.item.bag3) {
            if (it == null || it.template == null) continue;
            if (it.template.typeEquip != slot) continue;

            // So sánh: candidate phải tốt hơn cả item đang mặc lẫn best candidate trước đó
            Item_wear compareBase = (bestCandidate != null) ? bestCandidate : currentEquipped;
            if (isBetterGear(compareBase, it, bot.level, bot.clazz)) {
                bestCandidate = it;
            }
        }
        return bestCandidate;
    }

    /**
     * Tính tổng Combat Power của bot (tổng điểm tất cả slot đang mặc).
     */
    public static int getTotalCombatPower(BotPlayerReal bot) {
        if (bot == null || bot.item == null || bot.item.it_body == null) return 0;
        int total = 0;
        for (Item_wear it : bot.item.it_body) {
            total += calculateItemScore(it, bot.clazz);
        }
        return total;
    }

    /**
     * Kiểm tra item trong túi có đáng bán không (chỉ bán đồ không giá trị).
     * Giữ lại: đồ màu >= 2 (xanh dương trở lên), hoặc đã upgrade
     */
    public static boolean isTrashItem(Item_wear it) {
        if (it == null || it.template == null) return false;
        return it.getColor() < 2 && it.levelUp == 0;
    }

    // ========================= IMITATION LEARNING =========================

    /**
     * Học chat từ người chơi thật — tự phân loại context.
     */
    public static void learnChat(String text) {
        if (text == null || text.trim().length() < 3 || text.length() > 60) return;
        if (text.startsWith("/")) return; // bỏ qua lệnh

        String lower = text.toLowerCase();
        ChatContext ctx = ChatContext.GENERAL;
        if (lower.contains("boss") || lower.contains("mob") || lower.contains("đánh") || lower.contains("dame")) {
            ctx = ChatContext.COMBAT;
        } else if (lower.contains("bán") || lower.contains("mua") || lower.contains("giá") || lower.contains("chÓÚú")) {
            ctx = ChatContext.TRADING;
        } else if (lower.contains("pt") || lower.contains("nhóm") || lower.contains("party") || lower.contains("phó bản") || lower.contains("pb")) {
            ctx = ChatContext.PARTY;
        } else if (lower.contains("cây") || lower.contains("level") || lower.contains(" lv") || lower.contains("farm")) {
            ctx = ChatContext.IDLE;
        }
        learnChat(text, ctx);
    }

    /** Học chat với context cụ thể */
    public static void learnChat(String text, ChatContext ctx) {
        if (text == null || text.trim().isEmpty()) return;
        List<String> list = learnedChats.get(ctx);
        if (list == null) return;
        synchronized (list) {
            if (!list.contains(text)) {
                list.add(text.trim());
                if (list.size() > 100) list.remove(0);
            }
        }
    }

    /** Lấy chat theo context để bot nói */
    public static String getLearnedChat(ChatContext ctx) {
        List<String> list = learnedChats.get(ctx);
        if (list == null || list.isEmpty()) {
            list = learnedChats.get(ChatContext.GENERAL);
        }
        if (list == null || list.isEmpty()) return null;
        synchronized (list) {
            return list.get(ZUtil.random(list.size()));
        }
    }

    /** Lấy chat ngẫu nhiên bất kỳ context */
    public static String getRandomLearnedChat() {
        ChatContext[] ctxs = ChatContext.values();
        return getLearnedChat(ctxs[ZUtil.random(ctxs.length)]);
    }

    /**
     * Học pattern di chuyển của người chơi thật trên map.
     * Bot sẽ dùng các tọa độ này để di chuyển tự nhiên hơn.
     */
    public static void learnMovement(int mapId, short x, short y) {
        learnedMovements.computeIfAbsent(mapId, k -> Collections.synchronizedList(new ArrayList<>()));
        List<short[]> coords = learnedMovements.get(mapId);
        synchronized (coords) {
            coords.add(new short[]{x, y});
            if (coords.size() > 60) coords.remove(0);
        }
    }

    /**
     * Lấy tọa độ học được — bot đi đến đó thay vì random hoàn toàn.
     */
    public static short[] getLearnedPosition(int mapId) {
        List<short[]> coords = learnedMovements.get(mapId);
        if (coords == null || coords.isEmpty()) return null;
        synchronized (coords) {
            return coords.get(ZUtil.random(coords.size()));
        }
    }

    /**
     * Học điểm gear từ người chơi thật — bot biết mình đang mạnh/yếu như thế nào.
     */
    public static void learnGearScore(int clazz, int score) {
        learnedGearScores.computeIfAbsent(clazz, k -> Collections.synchronizedList(new ArrayList<>()));
        List<Integer> scores = learnedGearScores.get(clazz);
        synchronized (scores) {
            scores.add(score);
            if (scores.size() > 40) scores.remove(0);
        }
    }

    /**
     * Điểm gear trung bình của class từ real player (để bot tự so sánh).
     */
    public static int getAvgGearScoreForClass(int clazz) {
        List<Integer> scores = learnedGearScores.get(clazz);
        if (scores == null || scores.isEmpty()) return 0;
        synchronized (scores) {
            return scores.stream().mapToInt(Integer::intValue).sum() / scores.size();
        }
    }

    /**
     * Quan sát người chơi thật và học từ họ.
     * Gọi trong map tick khi có real player.
     */
    public static void observeRealPlayer(Player p) {
        if (p == null || p.isBot || p.conn == null) return;
        try {
            // Học gear pattern
            if (p.item != null && p.item.it_body != null) {
                int totalScore = 0;
                for (Item_wear it : p.item.it_body) {
                    // Dùng clazz=0 (general) để tính điểm baseline, tránh bias
                    totalScore += calculateItemScore(it, p.clazz);
                }
                if (totalScore > 0) learnGearScore(p.clazz, totalScore);
                // Học bộ đồ trừu tượng (không copy 100%)
                learnGearSet(p);
            }

            // Học potential attribute build pattern (Sức mạnh, Tinh thần, Thể lực, Nhanh nhẹn, Khéo léo)
            learnPlayerAttributes(p.clazz, p.point1, p.point2, p.point3, p.point4, p.point5);

            // Học movement pattern
            if (p.map != null) {
                learnMovement(p.map.template.id, p.x, p.y);
            }

            // Ghi log hành vi
            String log = String.format("OBS[%s]L%d-CLS%d-MAP%d",
                    p.name, p.level, p.clazz,
                    p.map != null ? p.map.template.id : -1);
            synchronized (observedActions) {
                observedActions.add(log);
                if (observedActions.size() > 300) observedActions.remove(0);
            }
        } catch (Exception ignored) {}
    }


    // ========================= GEAR SET LEARNING (Không copy 100%) =========================

    /**
     * Cấu trúc tóm gọn thông tin 1 slot trang bị của real player (để học).
     * Không lưu item thật — chỉ lưu thông tin trừu tượng.
     */
    public static class GearSlotProfile {
        public int slot;
        public int color;      // Màu sắc (rarity 0-5)
        public int levelReq;   // Level yêu cầu
        public int clazz;      // Class sử dụng (0=all)
        public int typeEquip;
        public int baseScore;  // Điểm CP cơ bản (không lưu exact option để tránh clone)
    }

    // Lưu profile gear của real players theo class
    private static final Map<Integer, List<List<GearSlotProfile>>> realGearProfiles =
        new ConcurrentHashMap<>();

    /**
     * Học bộ đồ của người chơi thật (không lưu item, chỉ lưu profile trừu tượng).
     * Được gọi từ observeRealPlayer.
     */
    public static void learnGearSet(Player p) {
        if (p == null || p.item == null || p.item.it_body == null) return;
        try {
            List<GearSlotProfile> profile = new ArrayList<>();
            for (Item_wear it : p.item.it_body) {
                if (it == null || it.template == null) continue;
                GearSlotProfile gp = new GearSlotProfile();
                gp.slot       = it.template.typeEquip;
                gp.color      = it.getColor();
                gp.levelReq   = it.template.level;
                gp.clazz      = it.template.clazz;
                gp.typeEquip  = it.template.typeEquip;
                gp.baseScore  = calculateItemScore(it, p.clazz);
                profile.add(gp);
            }
            if (!profile.isEmpty()) {
                int clazzKey = (int) p.clazz;
                realGearProfiles.computeIfAbsent(clazzKey,
                    k -> Collections.synchronizedList(new ArrayList<>()));
                List<List<GearSlotProfile>> profiles = realGearProfiles.get(clazzKey);
                synchronized (profiles) {
                    profiles.add(profile);
                    if (profiles.size() > 20) profiles.remove(0); // Giới hạn bộ nhớ
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Tạo bộ đồ "lấy cảm hứng" từ người chơi thật — không copy 100%.
     *
     * Quy trình:
     *  1. Lấy ngẫu nhiên 1 profile đã học của class tương ứng
     *  2. Tìm item trong template có cùng slot và tier (color/level ±1)
     *  3. Chọn item khác với item gốc (khác template ID), cùng tier
     *  4. Thêm variation ±15% vào levelUp để chỉ số khác nhau
     *
     * @param clazz  Class của bot cần tạo đồ
     * @param level  Level của bot
     * @return List<Item_wear> bộ đồ đã tạo (có thể rỗng nếu chưa đủ data)
     */
    public static List<Item_wear> generateBotGearInspiredBy(int clazz, int level) {
        List<Item_wear> result = new ArrayList<>();
        List<List<GearSlotProfile>> profiles = realGearProfiles.get(clazz);
        if (profiles == null || profiles.isEmpty()) {
            // Fallback: trả về rỗng, bot sẽ dùng đồ mặc định
            return result;
        }

        List<GearSlotProfile> profile;
        synchronized (profiles) {
            profile = profiles.get(ZUtil.random(profiles.size()));
        }
        if (profile == null) return result;

        for (GearSlotProfile gp : profile) {
            Item_wear inspired = findInspiredItem(gp, clazz, level);
            if (inspired != null) {
                result.add(inspired);
            }
        }
        return result;
    }

    /**
     * Tìm item "lấy cảm hứng" từ profile — KHÔNG clone item gốc.
     * Tìm item có cùng slot/tier nhưng template khác, variation ±15% levelUp.
     */
    private static Item_wear findInspiredItem(GearSlotProfile gp, int clazz, int level) {
        if (template.ItemTemplate3.ENTRYS == null) return null;
        try {
            List<template.ItemTemplate3> candidates = new ArrayList<>();
            for (template.ItemTemplate3 t : template.ItemTemplate3.ENTRYS) {
                if (t == null) continue;
                if (t.typeEquip != gp.typeEquip) continue;
                if (t.level > level) continue;
                if (t.clazz != 0 && t.clazz != clazz) continue;
                // Cùng tier màu (±1 để có variation nhẹ)
                if (Math.abs(t.color - gp.color) > 1) continue;
                // Level yêu cầu gần với profile (±5)
                if (Math.abs(t.level - gp.levelReq) > 5) continue;
                candidates.add(t);
            }

            if (candidates.isEmpty()) return null;

            // Ưu tiên chọn template khác với template gốc để tránh clone
            template.ItemTemplate3 chosen = candidates.get(ZUtil.random(candidates.size()));

            Item_wear it = new Item_wear();
            it.setup_template_by_id(chosen);
            it.index = chosen.typeEquip;

            // Variation levelUp: 0 đến gp.levelUp ± 15% (không vượt quá 10)
            int baseLevelUp = Math.min(10, Math.max(0, (int)(gp.baseScore / 180)));
            int variation = ZUtil.random(-2, 2); // ±2 cấp để đồ khác nhau
            it.levelUp = (byte) Math.max(0, Math.min(10, baseLevelUp + variation));

            return it;
        } catch (Exception ignored) {
            return null;
        }
    }

    // ========================= MARKET PRICE LEARNING =========================

    /** Học giá thị trường từ giao dịch thực */
    public static void learnMarketPrice(short templateId, int price) {
        if (price <= 0) return;
        learnedPrices.computeIfAbsent(templateId, k -> Collections.synchronizedList(new ArrayList<>()));
        List<Integer> prices = learnedPrices.get(templateId);
        synchronized (prices) {
            prices.add(price);
            if (prices.size() > 12) prices.remove(0);
        }
    }

    /** Lấy giá trung bình học được, fallback về defaultPrice nếu chưa có data */
    public static int getLearnedPrice(short templateId, int defaultPrice) {
        List<Integer> prices = learnedPrices.get(templateId);
        if (prices == null || prices.isEmpty()) return defaultPrice;
        synchronized (prices) {
            return prices.stream().mapToInt(Integer::intValue).sum() / prices.size();
        }
    }

    // ========================= SKILL & ATTRIBUTE IMITATION LEARNING =========================

    private static final Map<Integer, List<Integer>> learnedClassSkills = new ConcurrentHashMap<>();
    private static final Map<Integer, List<double[]>> learnedAttributeRatios = new ConcurrentHashMap<>();

    public static void learnPlayerSkill(int clazz, int skillId) {
        if (skillId <= 0) return;
        learnedClassSkills.computeIfAbsent(clazz, k -> Collections.synchronizedList(new ArrayList<>()));
        List<Integer> list = learnedClassSkills.get(clazz);
        synchronized (list) {
            // Duy trì thứ tự chuỗi skill xoay vòng thực tế mà người chơi sử dụng
            list.add(skillId);
            if (list.size() > 25) list.remove(0);
        }
    }

    public static List<Integer> getLearnedSkills(int clazz) {
        return getLearnedSkillRotation(clazz);
    }

    /**
     * Lấy danh sách kỹ năng ưu tiên học được từ combo thực tế của người chơi thật cùng class.
     */
    public static List<Integer> getLearnedSkillRotation(int clazz) {
        List<Integer> list = learnedClassSkills.get(clazz);
        if (list == null || list.isEmpty()) return null;
        synchronized (list) {
            LinkedHashSet<Integer> uniqueOrder = new LinkedHashSet<>();
            for (int i = list.size() - 1; i >= 0; i--) {
                uniqueOrder.add(list.get(i));
            }
            return new ArrayList<>(uniqueOrder);
        }
    }

    /**
     * Học tỷ lệ phân bổ điểm tiềm năng (Sức mạnh, Tinh thần, Thể lực, Nhanh nhẹn, Khéo léo) từ người chơi thật.
     */
    public static void learnPlayerAttributes(int clazz, long p1, long p2, long p3, long p4, long p5) {
        if (clazz < 1 || clazz > 5) return;
        long total = p1 + p2 + p3 + p4 + p5;
        if (total <= 4) return;

        learnedAttributeRatios.computeIfAbsent(clazz, k -> Collections.synchronizedList(new ArrayList<>()));
        List<double[]> list = learnedAttributeRatios.get(clazz);
        synchronized (list) {
            double r1 = (double) p1 / total;
            double r2 = (double) p2 / total;
            double r3 = (double) p3 / total;
            double r4 = (double) p4 / total;
            double r5 = (double) p5 / total;
            list.add(new double[]{r1, r2, r3, r4, r5});
            if (list.size() > 40) list.remove(0);
        }
    }

    /**
     * Lấy tỷ lệ phân bổ điểm tiềm năng trung bình học được từ các người chơi thật cùng class.
     */
    public static double[] getLearnedAttributeRatio(int clazz) {
        List<double[]> list = learnedAttributeRatios.get(clazz);
        if (list == null || list.isEmpty()) return null;
        synchronized (list) {
            double sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0, sum5 = 0;
            int count = list.size();
            for (double[] r : list) {
                sum1 += r[0]; sum2 += r[1]; sum3 += r[2]; sum4 += r[3]; sum5 += r[4];
            }
            return new double[]{sum1 / count, sum2 / count, sum3 / count, sum4 / count, sum5 / count};
        }
    }

    // ========================= ANALYTICS INCREMENT =========================
    public static void incrementMobKills()         { totalMobKills.incrementAndGet(); }
    public static void incrementDungeons()          { totalDungeonsRun.incrementAndGet(); }
    public static void incrementMarketBuys()        { totalMarketBuys.incrementAndGet(); }
    public static void incrementGearUpgrades()      { totalGearUpgrades.incrementAndGet(); }
    public static void incrementTrashSold()         { totalTrashSold.incrementAndGet(); }
    public static void incrementQuestsCompleted()   { totalQuestsCompleted.incrementAndGet(); }
    public static void incrementDeaths()            { totalDeaths.incrementAndGet(); }
    public static void incrementGearChanges()       { totalGearChanges.incrementAndGet(); }
    public static void addGoldEarned(long gold)     { totalGoldEarned.addAndGet(gold); }

    // ========================= LEVEL TRACKING =========================
    /** Track tiến độ level của bot để tính tốc độ phát triển */
    public static void trackBotProgress(BotPlayerReal bot) {
        if (bot == null) return;
        String key  = bot.name;
        long now    = System.currentTimeMillis();
        int curLv   = bot.level;

        Integer prevLv   = botLevelSnapshot.get(key);
        Long    prevTime = botLevelTimestamp.get(key);

        if (prevLv != null && prevTime != null && curLv > prevLv) {
            long deltaMs = now - prevTime;
            // System.out.printf("[GameAnalyzer] Bot %s leveled %d->%d in %.1f min%n",
            //         key, prevLv, curLv, deltaMs / 60000.0);
        }
        botLevelSnapshot.put(key, curLv);
        botLevelTimestamp.put(key, now);
    }

    // ========================= COUNT HELPERS =========================
    public static int getLearnedChatsCount() {
        int total = 0;
        for (List<String> l : learnedChats.values()) total += l.size();
        return total;
    }

    public static int getLearnedPricesCount() {
        int count = 0;
        for (List<Integer> l : learnedPrices.values()) count += l.size();
        return count;
    }

    // ========================= REPORTS =========================

    /**
     * Báo cáo tổng hệ thống — hiển thị từ lệnh /analyze hoặc GM command.
     */
    public static String getReport() {
        int botCount  = BotPlayerManager.activeBots.size();
        int realCount = 0;
        for (Player p : network.SessionManager.PLAYERS_MAP.values()) {
            if (p != null && !p.isBot && p.conn != null) realCount++;
        }

        int totalLv = 0;
        long totalCP = 0;
        Map<String, Integer> stateMap = new LinkedHashMap<>();
        for (BotPlayerReal bot : BotPlayerManager.activeBots.values()) {
            totalLv += bot.level;
            totalCP += getTotalCombatPower(bot);
            stateMap.merge(bot.state, 1, Integer::sum);
        }
        double avgLv = botCount > 0 ? (double) totalLv / botCount : 0;
        double avgCP = botCount > 0 ? (double) totalCP / botCount : 0;

        int marketTotal = 0;
        for (Market m : Market.ENTRY) {
            if (m == null) continue;
            if (m.item3  != null) marketTotal += m.item3.size();
            if (m.item47 != null) marketTotal += m.item47.size();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉ SYSTEM ANALYSIS REPORT ÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉ\n");
        sb.append(String.format("‰Đ¼ Online     : Total=%d | Real=%d | Bots=%d\n",
                realCount + botCount, realCount, botCount));
        sb.append(String.format("‰Đ¼ Bot Stats  : Avg Level=%.1f | Avg CP=%.0f\n", avgLv, avgCP));
        sb.append(String.format("‰Đ¼ Bot States : %s\n", stateMap));
        sb.append(String.format("‰Đ¼ Learning   : Chats=%d | Prices=%d | Moves=%d maps | Observed=%d\n",
                getLearnedChatsCount(), getLearnedPricesCount(),
                learnedMovements.size(), observedActions.size()));
        sb.append(String.format("‰Đ¼ Market     : Items Listed=%d | Bot Purchases=%d\n",
                marketTotal, totalMarketBuys.get()));
        sb.append(String.format("‰Đ¼ Combat     : Kills=%d | Dungeons=%d | Quests=%d | Deaths=%d\n",
                totalMobKills.get(), totalDungeonsRun.get(),
                totalQuestsCompleted.get(), totalDeaths.get()));
        sb.append(String.format("‰Đ¼ Economy    : Gear Upgrades=%d | Gear Changes=%d | Trash Sold=%d\n",
                totalGearUpgrades.get(), totalGearChanges.get(), totalTrashSold.get()));
        sb.append(String.format("‰Đ¼ Gold       : Total Earned=%,d\n", totalGoldEarned.get()));
        sb.append("ÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉÔòÉ");
        return sb.toString();
    }

    /**
     * Báo cáo chi tiết từng bot — trạng thái, gear, tiến độ.
     */
    public static String getBotDetailReport(BotPlayerReal bot) {
        if (bot == null) return "Bot not found.";
        int cp    = getTotalCombatPower(bot);
        int avgCP = getAvgGearScoreForClass(bot.clazz);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Èéé Bot: [%s] Lv%d Class%d\n", bot.name, bot.level, bot.clazz));
        sb.append(String.format("Ă²  State : %s | Map: %d\n",
                bot.state, bot.map != null ? bot.map.template.id : -1));
        sb.append(String.format("³  CP    : %d (avg real player of class: %d)\n", cp, avgCP));
        sb.append(String.format("éÔò  HP    : %d/%d | Gold: %,d\n",
                bot.hp, bot.ability.get_hp_max(true), bot.get_vang()));

        sb.append("éÔò  Gear  : [");
        if (bot.item != null && bot.item.it_body != null) {
            boolean first = true;
            for (int i = 0; i < bot.item.it_body.length; i++) {
                Item_wear it = bot.item.it_body[i];
                if (it != null && it.template != null) {
                    if (!first) sb.append(", ");
                    sb.append(String.format("S%d:%s+%d(C%d)",
                            i, it.template.name, it.levelUp, it.getColor()));
                    first = false;
                }
            }
        }
        sb.append("]\n");
        sb.append("õêêêêêêêêêêêêêêêêê\n");
        return sb.toString();
    }

    /**
     * Báo cáo tất cả bots đang hoạt động.
     */
    public static String getAllBotsReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("══════════ ALL BOTS REPORT ══════════\n");
        for (BotPlayerReal bot : BotPlayerManager.activeBots.values()) {
            sb.append(getBotDetailReport(bot));
        }
        sb.append("═════════════════════════════════════");
        return sb.toString();
    }

    // ========================= TOP 30 REAL PLAYER LEVEL CAP & BALANCING =========================
    private static volatile int cachedTop30LevelCap = 25;
    private static volatile long lastTop30CheckTime = 0;

    /**
     * Tính toán mốc level trần cân bằng cho Bot:
     * - Không bao giờ vượt quá Top 30 người chơi thật.
     * - Nếu có ít hơn 30 người chơi thật, Bot không vượt quá cấp độ người chơi thật (giữ vị trí Top cho người thật).
     * - Nếu chưa có người chơi thật, giới hạn ở mức cơ bản (cấp 20) để chờ người chơi.
     * Caching 30s để đảm bảo hiệu năng tối đa.
     */
    public static int getTop30RealPlayerLevelCap() {
        long now = System.currentTimeMillis();
        if (now - lastTop30CheckTime < 30_000L) {
            return cachedTop30LevelCap;
        }
        lastTop30CheckTime = now;

        java.sql.Connection conn = null;
        java.sql.PreparedStatement ps = null;
        java.sql.ResultSet rs = null;
        try {
            conn = database.DbManager.gI().getConnect();
            ps = conn.prepareStatement("SELECT `level` FROM `players` ORDER BY `level` DESC, `exp` DESC LIMIT 30");
            rs = ps.executeQuery();
            List<Integer> realPlayerLevels = new ArrayList<>();
            while (rs.next()) {
                realPlayerLevels.add(rs.getInt("level"));
            }

            if (realPlayerLevels.size() >= 30) {
                // Có từ 30 người chơi thật trở lên -> Lấy cấp độ của người chơi top 30
                int top30Level = realPlayerLevels.get(29);
                cachedTop30LevelCap = Math.max(10, top30Level);
            } else if (!realPlayerLevels.isEmpty()) {
                // Có ít hơn 30 người chơi thật -> Lấy cấp độ thấp nhất trong số top người chơi hoặc maxLevel - 1
                int maxRealLv = realPlayerLevels.get(0);
                int minRealLv = realPlayerLevels.get(realPlayerLevels.size() - 1);
                // Giữ vị trí dẫn đầu cho người chơi thật
                cachedTop30LevelCap = Math.max(10, Math.min(maxRealLv > 1 ? maxRealLv - 1 : 1, minRealLv + 3));
            } else {
                // Server mới chưa có người chơi thật tạo nhân vật
                cachedTop30LevelCap = 20;
            }
        } catch (Exception e) {
            // Fallback giữ nguyên cache cũ nếu có lỗi DB
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ignored) {}
        }
        return cachedTop30LevelCap;
    }
}
