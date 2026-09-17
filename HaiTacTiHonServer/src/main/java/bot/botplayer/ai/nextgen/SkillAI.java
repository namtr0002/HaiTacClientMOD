package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerReal;
import skill.Skill_info;

/**
 * SkillAI — Quản lý combo skill thông minh, theo dõi cooldown, mana, range, AOE, buff, debuff.
 */
public class SkillAI {

    public static Skill_info selectOptimalSkill(BotPlayerReal bot, double targetDistance, int nearbyEnemiesCount) {
        if (bot.skill_point == null || bot.skill_point.isEmpty()) return null;

        Skill_info bestSkill = null;
        int highestDamage = -1;
        long now = System.currentTimeMillis();

        for (Skill_info sk : bot.skill_point) {
            if (sk == null || sk.temp == null) continue;

            // Check MP cost
            if (bot.mp < sk.temp.manaLost) continue;

            // Check Cooldown
            Long cdTime = bot.time_use_skill.get(sk.temp.ID);
            if (cdTime != null && now < cdTime) continue;

            // Range check
            if (targetDistance > sk.temp.range) continue;

            // Prioritize AOE skills when surrounded by 3+ mobs
            boolean isAOE = (sk.temp.typeSkill == 2 || sk.temp.typeSkill == 3);
            int score = sk.temp.damage;
            if (nearbyEnemiesCount >= 3 && isAOE) {
                score *= 2;
            }

            if (score > highestDamage) {
                highestDamage = score;
                bestSkill = sk;
            }
        }

        return bestSkill != null ? bestSkill : (bot.skill_point.isEmpty() ? null : bot.skill_point.get(0));
    }
}
