package bot.botplayer;

import core.ZUtil;
import bot.botplayer.ai.Pathfinder;
import mob.Mob;
import map.Zone;
import map.Vgo;
import map.MapTemplate;

/**
 * TrainBoss — Bot tự động cày boss theo level bracket.
 *
 * Level bracket -> Boss Map ID:
 *   1x  (lv 10-19)  -> map 5   (boss lv1x)
 *   2x  (lv 20-29)  -> map 13  (boss lv2x)
 *   3x  (lv 30-39)  -> map 21  (boss lv3x)
 *   4x  (lv 40-49)  -> map 29  (boss lv4x)
 *   5x  (lv 50-59)  -> map 37  (boss lv5x)
 *   6x  (lv 60-69)  -> map 45  (boss lv6x)
 *   7x  (lv 70-79)  -> map 53  (boss lv7x)
 *   8x  (lv 80-89)  -> map 73  (boss lv8x)
 *   9x  (lv 90-99)  -> map 87  (boss lv9x)
 *   10x (lv 100+)   -> map 102 (boss lv10x)
 *
 * Bot tự xác định map boss phù hợp, di chuyển đến đó và đánh boss.
 */
public class TrainBoss {

    /**
     * Bảng boss map theo level bracket.
     * [minLevel, maxLevel, bossMapId]
     */
    private static final int[][] BOSS_MAP_TABLE = {
        {10,  19,  5},
        {20,  29,  13},
        {30,  39,  21},
        {40,  49,  29},
        {50,  59,  37},
        {60,  69,  45},
        {70,  79,  53},
        {80,  89,  73},
        {90,  99,  87},
        {100, 999, 102},
    };

    /**
     * Lấy map boss phù hợp với level hiện tại của bot.
     * Trả về -1 nếu chưa đủ level (< 10).
     */
    public static int getBossMapForLevel(int level) {
        for (int[] row : BOSS_MAP_TABLE) {
            if (level >= row[0] && level <= row[1]) return row[2];
        }
        return -1; // chưa đủ level đi boss
    }

    /**
     * Kiểm tra bot có đủ điều kiện vào boss không:
     * - Level >= 10
     * - Có key_boss > 0 (hoặc bỏ qua check key — bot không cần key)
     * - HP > 50% (không vào boss khi HP thấp)
     */
    public static boolean canTrainBoss(BotPlayerReal bot) {
        if (bot == null) return false;
        if (bot.level < 10) return false;
        // Kiểm tra HP > 50%
        try {
            int hpMax = bot.ability.get_hp_max(true);
            if (hpMax > 0 && bot.hp < hpMax / 2) return false;
        } catch (Exception ignored) {}
        return true;
    }

    /**
     * Logic cày boss chính — gọi từ BotPlayerReal state machine (state = BOSS).
     *
     * Flow:
     *  1. Tính map boss phù hợp level
     *  2. Nếu chưa ở đúng map -> di chuyển
     *  3. Nếu đang ở map boss -> tìm mob boss và tấn công
     *  4. Nếu boss đã chết -> đợi respawn hoặc về farm
     */
    public static void tick(BotPlayerReal bot) {
        if (bot == null || bot.map == null || bot.map.template == null) return;

        int bossMapId = getBossMapForLevel(bot.level);
        if (bossMapId < 0) {
            // Chưa đủ level — về FARM
            bot.state = "FARM";
            return;
        }

        int curMapId = bot.map.template.id;

        // Chưa ở đúng map boss -> set path
        if (curMapId != bossMapId) {
            if (bot.targetMapId != bossMapId || bot.currentPath == null) {
                bot.targetMapId = bossMapId;
                bot.currentPath = Pathfinder.findPath(curMapId, bossMapId);
                bot.pathIndex   = 0;
                // Để traversePath() xử lý trong state PORTAL
            }
            bot.state = "PORTAL";
            return;
        }

        // Đang ở map boss — tìm mob boss và đánh
        Mob boss = findBossMob(bot);
        if (boss != null && !boss.isdie) {
            double dist = Math.hypot(boss.x - bot.x, boss.y - bot.y);
            if (dist > 100) {
                bot.moveTowardsPublic(boss.x, boss.y);
            } else {
                bot.attackBossMob(boss);
            }
        } else {
            // Không có boss (chưa spawn hoặc đã chết) -> về FARM
            bot.state = "FARM";
            bot.currentPath = null;
            bot.targetMapId = -1;
        }
    }

    /**
     * Tìm mob boss trong map hiện tại (mob có HP cao nhất, là loại boss).
     */
    private static Mob findBossMob(BotPlayerReal bot) {
        if (bot.map == null || bot.map.mobs == null) return null;
        Mob bestBoss = null;
        long maxHp = 0;
        for (Mob mob : bot.map.mobs.values()) {
            if (mob == null || mob.isdie || mob.mtemplate == null) continue;
            // Boss thường có HP rất cao (> 100,000)
            if (mob.hp_max > maxHp) {
                maxHp = mob.hp_max;
                bestBoss = mob;
            }
        }
        // Chỉ trả về nếu thực sự là mob boss (hp_max > 10000 để phân biệt với mob thường)
        return (maxHp > 10000) ? bestBoss : null;
    }

    /**
     * Kiểm tra nếu đang ở map boss mà boss đã chết -> cần về farm.
     */
    public static boolean isBossDeadInCurrentMap(BotPlayerReal bot) {
        if (bot.map == null || !Zone.is_map_boss(bot.map.template.id)) return false;
        for (Mob mob : bot.map.mobs.values()) {
            if (mob != null && !mob.isdie && mob.hp_max > 10000) return false; // boss còn sống
        }
        return true; // boss đã chết hoặc không có boss
    }
}
