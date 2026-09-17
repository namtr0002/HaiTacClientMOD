package bot.botplayer.ai;

import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotPlayerReal;
import bot.botplayer.GameAnalyzer;
import mob.Mob;
import model.Player;
import core.ZUtil;
import template.ItemBag47;
import map.Zone;
import skill.Skill_info;
import java.util.*;

/**
 * BotCombatController — Xử lý nhắm mục tiêu, chọn skill combo theo hệ phái, hồi chiêu, tự dùng bình máu/năng lượng và tấn công quái/boss/người chơi.
 */
public class BotCombatController {

    private final BotPlayerReal bot;
    public int ksCount = 0; // Đếm số lần bị ks
    private long lastAttackTime = 0;
    private long lastPotionUseTime = 0;
    private int lastUsedSkillId = -1;  // Tránh spam cùng 1 skill liên tiếp

    private boolean checkAttackCooldown() {
        long now = System.currentTimeMillis();
        skill.Skill_info basicSkill = null;
        if (bot.skill_point != null) {
            int baseClassIdx = (bot.clazz > 0 ? bot.clazz - 1 : 0) * 60;
            for (skill.Skill_info sk : bot.skill_point) {
                if (sk != null && sk.temp != null && (sk.temp.ID == 0 || sk.temp.indexSkillInServer == baseClassIdx)) {
                    basicSkill = sk;
                    break;
                }
            }
        }
        long gcd = (basicSkill != null && basicSkill.temp != null) ? bot.calculateSkillCooldown(basicSkill.temp) : 650L;
        if (now - lastAttackTime < gcd) {
            return false;
        }
        lastAttackTime = now;
        return true;
    }

    public BotCombatController(BotPlayerReal bot) {
        this.bot = bot;
    }

    /**
     * Tự động kiểm tra và sử dụng bình HP/MP khi đang trong chiến đấu hoặc dưới ngưỡng an toàn.
     */
    public void autoUsePotionsInCombat() {
        long now = System.currentTimeMillis();
        if (now - lastPotionUseTime < 2000) { // Cooldown dùng bình 2s
            return;
        }
        if (bot.isdie || bot.ability == null) {
            return;
        }

        long hpMax = bot.ability.get_hp_max(true);
        long mpMax = bot.ability.get_mp_max(true);

        // Ngưỡng cần bơm: HP < 55% hoặc MP < 30%
        boolean needHp = (hpMax > 0 && ((double) bot.hp / hpMax) < 0.55);
        boolean needMp = (mpMax > 0 && ((double) bot.mp / mpMax) < 0.30);

        if (!needHp && !needMp) {
            return;
        }

        lastPotionUseTime = now;

        // Bơm khẩn cấp bằng tăng chỉ số trực tiếp
        if (needHp && hpMax > 0) {
            long healAmount = (long) (hpMax * 0.30);
            bot.hp = (int) Math.min(hpMax, bot.hp + healAmount);
        }
        if (needMp && mpMax > 0) {
            long manaAmount = (long) (mpMax * 0.30);
            bot.mp = (int) Math.min(mpMax, bot.mp + manaAmount);
        }

        // Thử kích hoạt item bình máu thật trong hành trang túi bag47 nếu có
        if (bot.item != null && bot.item.bag47 != null) {
            synchronized (bot.item.bag47) {
                for (ItemBag47 item : bot.item.bag47) {
                    if (item != null && item.quant > 0 && item.category == 4) { // Potion category
                        item.quant--;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Kiểm tra trạng thái rút lui sinh tồn (Kite / Evasive stance) khi máu quá thấp.
     */
    public boolean shouldRetreat() {
        if (bot.ability == null || bot.isdie) return false;
        long hpMax = bot.ability.get_hp_max(true);
        return hpMax > 0 && ((double) bot.hp / hpMax) < 0.25;
    }

    /**
     * Tìm quái thường phù hợp nhất (ưu tiên máu thấp và gần).
     */
    public Mob findBestMob() {
        if (bot.map == null || bot.map.mobs == null || bot.map.mobs.isEmpty()) {
            return null;
        }
        Mob best = null;
        double bestScore = Double.MAX_VALUE;
        synchronized (bot.map.mobs) {
            for (Mob mob : bot.map.mobs.values()) {
                if (mob == null || mob.isdie) {
                    continue;
                }
                // Bỏ qua quái máu cao ở map thường khi đang FARM (để tránh boss thế giới)
                if ("FARM".equals(bot.state) && mob.hp_max > 10000 
                        && !Zone.is_map_boss(bot.map.template.id) 
                        && !Zone.is_map_dungeon(bot.map.template.id)) {
                    continue;
                }

                // Kiểm tra xem quái có đang bị người chơi thật đánh không -> Nếu có, nhường quái
                boolean isTargetedByRealPlayer = false;
                if (bot.map.players != null) {
                    for (int pi = 0; pi < bot.map.players.size(); pi++) {
                        Player p = bot.map.players.get(pi);
                        if (p != null && !p.isBot && !p.isdie && p.conn != null) {
                            if (mob.id_target == p.IDPlayer) {
                                isTargetedByRealPlayer = true;
                                break;
                            }
                            double distToRealPlayer = Math.hypot(mob.x - p.x, mob.y - p.y);
                            if (distToRealPlayer < 90) {
                                isTargetedByRealPlayer = true;
                                break;
                            }
                        }
                    }
                }
                if (isTargetedByRealPlayer) {
                    continue; // Nhường quái này cho người thật
                }

                double dist = Math.hypot(mob.x - bot.x, mob.y - bot.y);
                double hpRatio = (double) mob.hp / mob.hp_max;
                double score = dist + (hpRatio * 500);
                
                if (score < bestScore) {
                    bestScore = score;
                    best = mob;
                }
            }
        }
        return best;
    }

    /**
     * Tìm boss trong map hiện tại (mobs có hp_max > 10000).
     */
    public Mob findBossMob() {
        if (bot.map == null || bot.map.mobs == null) {
            return null;
        }
        Mob bestBoss = null;
        long maxHp = 0;
        synchronized (bot.map.mobs) {
            for (Mob mob : bot.map.mobs.values()) {
                if (mob == null || mob.isdie || mob.mtemplate == null) {
                    continue;
                }
                if (mob.hp_max > maxHp) {
                    maxHp = mob.hp_max;
                    bestBoss = mob;
                }
            }
        }
        return (maxHp > 10000) ? bestBoss : null;
    }

    /**
     * Chọn kỹ năng tối ưu theo hệ phái (Class 1-5 Combo Rotation) & Trạng thái hồi chiêu.
     * - Kiểm tra MP trước khi dùng skill có chi phí
     * - Không spam cùng 1 skill 2 lần liên tiếp (Combo Rotation)
     * - Fallback về basic attack (skillId=0) khi mọi skill đều đang cooldown
     */
    /**
     * Tự động kiểm tra và buff hỗ trợ/tăng chỉ số theo Class trước khi tấn công.
     */
    public void autoCastClassBuffs() {
        if (bot.isdie || bot.ability == null || bot.time_use_skill == null) return;
        long now = System.currentTimeMillis();
        
        // Mỗi class có skill buff riêng
        int buffSkillId = -1;
        switch (bot.clazz) {
            case 1 -> buffSkillId = 4;   // Kiếm sĩ: Buff Tăng công / Bạo kích
            case 2 -> buffSkillId = 64;  // Võ sĩ: Buff Giáp / Phòng thủ
            case 3 -> buffSkillId = 124; // Bác sĩ / Đầu bếp: Buff Hồi máu / Tốc độ
            case 4 -> buffSkillId = 184; // Xạ thủ: Buff Né tránh / Tốc đánh
            case 5 -> buffSkillId = 244; // Nhẫn giả: Buff Ẩn thân / Sát thương bạo
        }
        
        if (buffSkillId > 0) {
            Long lastUse = bot.time_use_skill.get(buffSkillId);
            if (lastUse == null || now >= lastUse) {
                // Đủ chi phí mana
                int manaReq = 30 + bot.level * 2;
                if (bot.mp >= manaReq) {
                    bot.mp -= manaReq;
                    bot.time_use_skill.put(buffSkillId, now + 30000L); // Cooldown buff 30s
                    try {
                        if (bot.map != null) {
                            bot.map.send_chat_popup(0, bot.index_map, "Buff!");
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    /**
     * Kiểm tra và hỗ trợ hồi máu cho đồng đội trong nhóm (Class 3 Cook/Doctor).
     */
    public void autoHealPartyAllies() {
        if (bot.clazz != 3 || bot.isdie || bot.map == null) return;
        long now = System.currentTimeMillis();
        Long lastHeal = bot.time_use_skill.get(124);
        if (lastHeal != null && now < lastHeal) return;

        if (bot.map.players != null) {
            for (Player p : bot.map.players) {
                if (p != null && !p.isdie && p.IDPlayer != bot.IDPlayer && (p.party == bot.party || p instanceof bot.mercenary.MercenaryBot || p.isDe)) {
                    double dist = Math.hypot(p.x - bot.x, p.y - bot.y);
                    if (dist < 200 && p.ability != null) {
                        long hpMax = p.ability.get_hp_max(true);
                        if (hpMax > 0 && ((double) p.hp / hpMax) < 0.50) {
                            // Hồi 25% máu cho đồng đội
                            p.hp = (int) Math.min(hpMax, p.hp + (hpMax * 25 / 100));
                            bot.time_use_skill.put(124, now + 15000L);
                            try {
                                bot.map.send_chat_popup(0, bot.index_map, "Cứu thương!");
                            } catch (Exception ignored) {}
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Đếm số quái vật xung quanh vị trí chỉ định trong bán kính r (bán kính AOE).
     */
    public int countNearbyMobs(int x, int y, int radius) {
        if (bot.map == null || bot.map.mobs == null) return 0;
        int count = 0;
        for (Mob mob : bot.map.mobs.values()) {
            if (mob != null && !mob.isdie) {
                if (Math.hypot(mob.x - x, mob.y - y) <= radius) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Chọn kỹ năng tối ưu theo hệ phái (Class 1-5 Combo Rotation) & Trạng thái hồi chiêu.
     * - Tự động nhận diện AOE khi có >= 2 quái xung quanh
     * - Kiểm tra MP trước khi dùng skill có chi phí
     * - Không spam cùng 1 skill 2 lần liên tiếp (Combo Rotation)
     * - Fallback về basic attack (skillId=0) khi mọi skill đều đang cooldown
     */
    private int[] getBestAvailableSkill() {
        int bestSkill = 0; // 0 = basic attack
        int damagePercent = 20;
        long now = System.currentTimeMillis();
        
        // 0. Auto Cast Buff
        autoCastClassBuffs();
        autoHealPartyAllies();
        
        // 1. Đồng bộ các chiêu thức chủ động đã học từ skill_point sang time_use_skill
        if (bot.skill_point != null) {
            for (Skill_info sk : bot.skill_point) {
                if (sk != null && sk.temp != null) {
                    boolean isActive = sk.temp.typeSkill != 2 && sk.temp.typeSkill != 3;
                    if (isActive) {
                        int skillId = sk.temp.ID;
                        if (!bot.time_use_skill.containsKey(skillId)) {
                            bot.time_use_skill.put(skillId, 0L);
                        }
                    }
                }
            }
        }
        
        // 2. Tìm các chiêu thức đã hồi chiêu xong
        if (bot.time_use_skill != null && !bot.time_use_skill.isEmpty()) {
            List<Integer> availableSkills = new ArrayList<>();
            for (Map.Entry<Integer, Long> entry : bot.time_use_skill.entrySet()) {
                if (entry.getValue() == 0 || now >= entry.getValue()) {
                    availableSkills.add(entry.getKey());
                }
            }

            if (!availableSkills.isEmpty()) {
                // Loại bỏ skill vừa dùng để tạo Combo Rotation (không spam 1 skill)
                if (lastUsedSkillId > 0 && availableSkills.size() > 1) {
                    availableSkills.remove(Integer.valueOf(lastUsedSkillId));
                }

                int nearbyMobCount = countNearbyMobs(bot.x, bot.y, 140);
                boolean isAoeScenario = nearbyMobCount >= 2;

                // Ưu tiên combo theo hệ phái (Clazz 1-5)
                List<Integer> learnedForClass = GameAnalyzer.getLearnedSkills(bot.clazz);
                if (learnedForClass != null && !learnedForClass.isEmpty()) {
                    // Doctor/Cook (Clazz 3): Ưu tiên skill hỗ trợ/hồi phục khi máu < 60%
                    if (bot.clazz == 3 && bot.ability != null) {
                        long hpMax = bot.ability.get_hp_max(true);
                        if (hpMax > 0 && ((double) bot.hp / hpMax) < 0.60) {
                            for (int skillId : learnedForClass) {
                                if (availableSkills.contains(skillId) && (skillId % 2 == 0)) {
                                    bestSkill = skillId;
                                    break;
                                }
                            }
                        }
                    }
                    if (bestSkill == 0) {
                        // Nếu đông quái -> Ưu tiên AOE Skill (ID lớn hơn hoặc chia hết cho 3)
                        if (isAoeScenario) {
                            for (int i = learnedForClass.size() - 1; i >= 0; i--) {
                                int skillId = learnedForClass.get(i);
                                if (availableSkills.contains(skillId)) {
                                    bestSkill = skillId;
                                    break;
                                }
                            }
                        }
                        if (bestSkill == 0) {
                            for (int skillId : learnedForClass) {
                                if (availableSkills.contains(skillId)) {
                                    bestSkill = skillId;
                                    break;
                                }
                            }
                        }
                    }
                }
                
                if (bestSkill == 0) {
                    // Sắp xếp giảm dần, ưu tiên chiêu ID cao nhất (kỹ năng mạnh nhất)
                    availableSkills.sort(Collections.reverseOrder());
                    bestSkill = availableSkills.get(0);
                    
                    // Combo: 30% tỷ lệ dùng chiêu ID khác để tạo chuỗi combo tự nhiên
                    if (availableSkills.size() > 1 && ZUtil.random(100) < 30) {
                        bestSkill = availableSkills.get(ZUtil.random(availableSkills.size()));
                    }
                }
                
                // Tìm thông số kỹ năng trong skill_point
                Skill_info skSelected = null;
                if (bot.skill_point != null) {
                    for (Skill_info sk : bot.skill_point) {
                        if (sk != null && sk.temp != null && sk.temp.ID == bestSkill) {
                            skSelected = sk;
                            break;
                        }
                    }
                }
                
                // Kiểm tra MP trước khi dùng skill (tránh spam khi cạn MP)
                if (skSelected != null && skSelected.temp != null && skSelected.temp.manaLost > 0) {
                    if (bot.mp < skSelected.temp.manaLost) {
                        // Không đủ MP -> dùng basic attack thay thế
                        bestSkill = 0;
                        damagePercent = 20;
                        return new int[]{bestSkill, damagePercent};
                    }
                    // Trừ MP thực tế khi dùng skill
                    bot.mp = Math.max(0, bot.mp - skSelected.temp.manaLost);
                }

                long actualCooldown = 600L;
                if (skSelected != null && skSelected.temp != null) {
                    damagePercent = skSelected.temp.damage / 10;
                    if (damagePercent < 20) damagePercent = 20;
                    actualCooldown = bot.calculateSkillCooldown(skSelected.temp);
                } else {
                    damagePercent = 20 + (bestSkill * 10) + ZUtil.random(30);
                    skill.Skill_info basicSkill = null;
                    if (bot.skill_point != null) {
                        int baseClassIdx = (bot.clazz > 0 ? bot.clazz - 1 : 0) * 60;
                        for (skill.Skill_info sk : bot.skill_point) {
                            if (sk != null && sk.temp != null && (sk.temp.ID == 0 || sk.temp.indexSkillInServer == baseClassIdx)) {
                                basicSkill = sk;
                                break;
                            }
                        }
                    }
                    actualCooldown = (basicSkill != null && basicSkill.temp != null) ? bot.calculateSkillCooldown(basicSkill.temp) : 600L;
                }
                
                bot.time_use_skill.put(bestSkill, now + actualCooldown);
                bot.applySkillCooldown(bestSkill, skSelected != null ? skSelected.temp : null);
                lastUsedSkillId = bestSkill;
            }
        }
        return new int[]{bestSkill, damagePercent};
    }

    /**
     * Tấn công mục tiêu quái thường (Smart skill selection + Potion Check).
     */
    public void attackTarget(Mob target) {
        if (target == null || target.isdie) {
            return;
        }
        autoUsePotionsInCombat();
        if (!checkAttackCooldown()) {
            return;
        }
        try {
            long dame = bot.ability != null ? bot.ability.get_dame(true) : 10;
            int[] skillData = getBestAvailableSkill();
            long skillDame = dame + (long)(dame * skillData[1] / 100.0);
            
            // Check KS (Có người chơi thật đứng sát mục tiêu)
            if (bot.map != null && bot.map.players != null) {
                boolean isNearPlayer = false;
                for (int i = 0; i < bot.map.players.size(); i++) {
                    Player p = bot.map.players.get(i);
                    if (p != null && !p.isdie && p.IDPlayer != bot.IDPlayer && !p.isBot) {
                        double distToPlayer = Math.hypot(p.x - target.x, p.y - target.y);
                        if (distToPlayer < 60) {
                            isNearPlayer = true;
                            break;
                        }
                    }
                }
                if (isNearPlayer) {
                    ksCount++;
                } else {
                    ksCount = 0;
                }
            } else {
                ksCount = 0;
            }

            bot.attackMob(new Mob[]{target}, skillData[0], skillDame);
            bot.lastMobId = (target.mtemplate != null) ? target.mtemplate.mob_id : -1;
            GameAnalyzer.incrementMobKills();
        } catch (Exception ignored) {}
    }

    /**
     * Tấn công quái boss (kết hợp các skill tấn công + Potion Check).
     */
    public void attackBossMob(Mob boss) {
        if (boss == null || boss.isdie) {
            return;
        }
        autoUsePotionsInCombat();
        if (!checkAttackCooldown()) {
            return;
        }
        try {
            long dame = bot.ability != null ? bot.ability.get_dame(true) : 10;
            int[] skillData = getBestAvailableSkill();
            long skillDame = dame + (long)(dame * skillData[1] / 100.0);

            // GỌI HỘI ĐÁNH BOSS (Phân tích sức mạnh)
            if (boss.hp_max > (bot.ability != null ? bot.ability.get_hp_max(true) : 1000) * 10) {
                if (ZUtil.random(100) < 5) {
                    if (bot.map != null && bot.map.template != null) {
                        BotPlayerManager.broadcastBossAssist(bot.map.template.id);
                        try {
                            bot.map.send_chat_popup(0, bot.index_map, "AE tới map này đập boss giùm với!");
                        } catch (Exception ignored) {}
                    }
                }
            }

            bot.attackMob(new Mob[]{boss}, skillData[0], skillDame);
            GameAnalyzer.incrementMobKills();
        } catch (Exception ignored) {}
    }

    /**
     * Kiểm tra boss trong map đã chết chưa.
     */
    public boolean isBossDeadInCurrentMap() {
        if (bot.map == null || bot.map.template == null
                || !Zone.is_map_boss(bot.map.template.id)) {
            return false;
        }
        if (bot.map.mobs == null) {
            return true;
        }
        for (Mob mob : bot.map.mobs.values()) {
            if (mob != null && !mob.isdie && mob.hp_max > 10000) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tìm đối thủ (người chơi khác phe / hợp lệ PK) gần nhất để PVP.
     */
    public Player findNearestPlayerToPVP() {
        return findNearestEnemyToPVP();
    }

    /**
     * Tìm đối thủ (người chơi khác phe / hợp lệ PK) gần nhất để PVP.
     */
    public Player findNearestEnemyToPVP() {
        if (bot.map == null || bot.map.players == null || bot.map.players.isEmpty()) {
            return null;
        }
        Player closest = null;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < bot.map.players.size(); i++) {
            try {
                Player p = bot.map.players.get(i);
                if (p == null || p.isdie || p.IDPlayer == bot.IDPlayer) {
                    continue;
                }
                if (!canPVP(p)) {
                    continue;
                }
                double dist = Math.hypot(p.x - bot.x, p.y - bot.y);
                if (dist < minDist) {
                    minDist = dist;
                    closest = p;
                }
            } catch (Exception ignored) {}
        }
        return closest;
    }

    /**
     * Kiểm tra trạng thái PK hợp lệ đối với mục tiêu (Ủy quyền hoàn toàn cho game logic chuẩn trong Player.java).
     */
    public boolean canPVP(Player target) {
        if (target == null || target.isdie || bot.isdie) {
            return false;
        }
        if (bot.map == null || target.map == null || !bot.map.equals(target.map)) {
            return false;
        }
        if (Zone.isMapLang(bot.map.template.id)) {
            return false;
        }
        return bot.canAttackTargetPlayer(target);
    }

    /**
     * Tấn công người chơi với kiểm tra hồi phục và rút lui phòng thủ khi HP < 25%.
     */
    public void attackPlayer(Player target) {
        if (target == null || target.isdie || !canPVP(target)) {
            return;
        }
        autoUsePotionsInCombat();
        
        // Trạng thái né chiêu/rút lui phòng thủ khi sinh lực quá thấp
        if (shouldRetreat()) {
            double dist = Math.hypot(target.x - bot.x, target.y - bot.y);
            if (dist < 150) {
                // Di chuyển lùi lại giữ khoảng cách sinh tồn
                short newX = (short) (bot.x + (bot.x > target.x ? 60 : -60));
                short newY = (short) (bot.y + (bot.y > target.y ? 40 : -40));
                bot.getMovementController().moveTowards(newX, newY);
            }
        }

        if (!checkAttackCooldown()) {
            return;
        }
        try {
            long dame = bot.ability != null ? bot.ability.get_dame(true) : 10;
            int[] skillData = getBestAvailableSkill();
            long skillDame = dame + (long)(dame * skillData[1] / 100.0);
            
            bot.attackPlayer(new Player[]{target}, skillData[0], skillDame);
        } catch (Exception ignored) {}
    }

    /**
     * Tấn công danh sách người chơi với skill và sát thương chỉ định.
     */
    public void attackPlayer(Player[] targets, int skillId, long dame) {
        if (targets == null || targets.length == 0) {
            return;
        }
        autoUsePotionsInCombat();
        try {
            bot.attackPlayer(targets, skillId, dame);
        } catch (Exception ignored) {}
    }
}
