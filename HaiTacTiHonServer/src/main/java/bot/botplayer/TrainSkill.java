package bot.botplayer;

import skill.Skill_info;
import skill.Skill_Template;
import core.ZUtil;

/**
 * TrainSkill — Tự động học và nâng cấp skill cho bot.
 *
 * Ưu tiên skill theo class của bot:
 *  - Class 1 (DPS): Skill tấn công mạnh nhất
 *  - Class 2 (Balanced): Skill tấn công + buff
 *  - Class 3 (Agile DPS): Skill tốc độ cao
 *  - Class 4 (Tank): Skill phòng thủ
 *  - Class 5 (Hybrid): Cân bằng ATK/DEF
 */
public class TrainSkill {

    /** Bản đồ ưu tiên skill theo class [clazz 0..5] -> danh sách typeSkill ưu tiên */
    // typeSkill: 1 = tấn công, 2 = phòng thủ/buff, 3 = hỗ trợ
    private static final int[][] CLASS_SKILL_PRIORITY = {
        {1, 2, 3},  // 0 = default: atk > def > support
        {1, 3, 2},  // 1 = DPS: atk > support > def
        {1, 2, 3},  // 2 = Balanced: atk > def > support
        {1, 3, 2},  // 3 = Agile DPS: atk > support > def
        {2, 3, 1},  // 4 = Tank: def > support > atk
        {1, 2, 3},  // 5 = Hybrid
    };

    /** Số lượng skill tối đa bot nên có */
    private static final int MAX_SKILL_SLOTS = 6;

    /**
     * Gọi từ BotBrain mỗi 60 giây để học / nâng skill phù hợp.
     * Không tạo thread — gọi đồng bộ từ game loop.
     */
    public static void tick(BotPlayerReal bot) {
        if (bot == null) return;
        try {
            // Học skill mới nếu còn slot và có đủ điểm kinh nghiệm
            learnNewSkills(bot);
            // Tích lũy exp skill khi cày
            gainSkillExp(bot);
        } catch (Exception ignored) {}
    }

    // ========================= PRIVATE =========================

    private static void learnNewSkills(BotPlayerReal bot) {
        if (bot.skill_point == null) return;
        if (bot.skill_point.size() >= MAX_SKILL_SLOTS) return;

        // Lấy ưu tiên theo class
        int clazzIdx = (bot.clazz >= 0 && bot.clazz < CLASS_SKILL_PRIORITY.length) ? bot.clazz : 0;
        int[] priority = CLASS_SKILL_PRIORITY[clazzIdx];

        // Tìm skill template phù hợp chưa có trong skill_point
        if (Skill_Template.ENTRYS == null) return;
        for (int preferredType : priority) {
            for (Skill_Template st : Skill_Template.ENTRYS) {
                if (st == null) continue;
                if (st.typeSkill != preferredType) continue;
                // Dùng Lv_RQ (tên field thực tế trong Skill_Template)
                if ((st.Lv_RQ & 0xFF) > bot.level) continue;
                if (alreadyHasSkill(bot, st.ID)) continue;

                // Kiểm tra có đủ tiền không (mỗi skill tốn 10k + N*5k vàng)
                long cost = 10_000L + (long) (st.Lv_RQ & 0xFF) * 5_000L;
                if (bot.get_vang() < cost) continue;

                // Học skill
                Skill_info newSk = new Skill_info();
                newSk.temp = st;
                newSk.exp = 0;
                newSk.lvdevil = 0;
                newSk.devilpercent = 0;
                bot.skill_point.add(newSk);
                try {
                    bot.update_vang(-cost);
                    bot.updateMoney();
                    bot.send_skill();
                } catch (Exception ignored) {}

                // Thông báo chat ngẫu nhiên
                if (ZUtil.random(100) < 40 && bot.map != null) {
                    try {
                        bot.map.send_chat_popup(0, bot.index_map,
                            "Vừa học được chiêu " + st.name + "!");
                    } catch (Exception ignored) {}
                }
                return; // Học 1 skill / lần tick
            }
        }
    }

    /**
     * Tích lũy exp cho các skill đã học (mô phỏng việc dùng skill trong combat).
     * Mỗi tick sẽ cộng một lượng exp nhỏ vào skill đang training.
     */
    private static void gainSkillExp(BotPlayerReal bot) {
        if (bot.skill_point == null || bot.skill_point.isEmpty()) return;

        // Chỉ tích exp khi đang trạng thái FARM/BOSS/DUNGEON
        String state = bot.state;
        if (!"FARM".equals(state) && !"BOSS".equals(state) && !"DUNGEON".equals(state)
                && !"EVENT".equals(state) && !"WORLD_WAR".equals(state)) {
            return;
        }

        // Ưu tiên tích exp cho skill tấn công (typeSkill == 1) theo class
        int clazzIdx = (bot.clazz >= 0 && bot.clazz < CLASS_SKILL_PRIORITY.length) ? bot.clazz : 0;
        int[] priority = CLASS_SKILL_PRIORITY[clazzIdx];

        for (int preferredType : priority) {
            for (Skill_info sk : bot.skill_point) {
                if (sk == null || sk.temp == null) continue;
                if (sk.exp < 0) continue; // Skill locked
                if (sk.temp.typeSkill != preferredType) continue;

                // Tích exp tỷ lệ theo level bot (higher level = more exp per tick)
                long expGain = (long) bot.level * 50L + ZUtil.random(100);
                sk.exp += expGain;

                // Giới hạn exp tối đa (tránh overflow)
                if (sk.exp > Long.MAX_VALUE / 2) {
                    sk.exp = Long.MAX_VALUE / 2;
                }
                return; // Chỉ 1 skill / lần tick
            }
        }
    }

    private static boolean alreadyHasSkill(BotPlayerReal bot, int skillId) {
        if (bot.skill_point == null) return false;
        for (Skill_info sk : bot.skill_point) {
            if (sk != null && sk.temp != null && sk.temp.ID == skillId) return true;
        }
        return false;
    }
}
