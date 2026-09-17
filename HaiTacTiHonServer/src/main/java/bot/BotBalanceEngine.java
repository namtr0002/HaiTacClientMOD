package bot;

import ability.Ability;
import core.ZUtil;
import map.Zone;
import model.Player;
import template.Item_wear;

import java.util.List;

/**
 * BotBalanceEngine — Bộ máy Trung Tâm Quản Lý RAM Cache, Khởi Tạo Bot Không Cần SQL
 * và Cân Bằng Động Thích Ứng Toàn Diện (Adaptive Dynamic Balancing & Anti-OneHit Protection).
 *
 * <p>Đặc tính nổi bật:
 * 1. <b>Zero SQL Overhead</b>: Khởi tạo toàn bộ trang bị (Full 8 món), ngọc khảm, kích ẩn, kỹ năng,
 *    tiềm năng, ngoại hình và ability 100% trong RAM Cache.
 * 2. <b>Adaptive Dynamic Balancing</b>: Tự động phân tích sức mạnh người chơi thật (Cường hóa +11..+16,
 *    Set Hoàn Mỹ, Kích Ẩn, Đá Khảm Thần Thoại/Siêu Cấp, Trái Ác Quỷ, Level, HP/Dame) để nâng tầm Bot
 *    lên khoảng 88% - 110% sức mạnh tương xứng, không bị "cùi" và không quá áp đảo.
 * 3. <b>Universal Bi-directional Anti-OneHit</b>: Kiểm soát sát thương tối đa / tối thiểu mỗi đòn đánh
 *    trong giao tranh giữa Người vs Bot và Bot vs Người, đảm bảo trận đấu có nhịp độ chuẩn 7 - 12 hit.
 */
public class BotBalanceEngine {

    /**
     * Tạo một Bot hoàn chỉnh 100% trong RAM Cache (Zero SQL).
     */
    public static Bot createMemoryBot(int id, String name, byte clazz, short level, double tier,
                                     boolean hasHoanMy, boolean hasKhamDa, boolean hasKichAn,
                                     int fruitId, Zone targetMap) {
        try {
            Bot bot = new Bot(id, name);
            bot.isBot = true;
            bot.clazz = (byte) ((clazz >= 1 && clazz <= 5) ? clazz : ZUtil.random(1, 5));
            bot.level = (short) Math.max(1, level);

            // 1. Phân bổ điểm tiềm năng theo Class & Level
            bot.autoAllocatePotentialPoints(bot.clazz);

            // 2. Nạp trang bị chuẩn (Hoàn Mỹ, Đá Khảm, Kích Ẩn theo Tier)
            bot.setupBotEquip(bot.clazz, bot.level, tier, hasHoanMy, hasKhamDa, hasKichAn);

            // 3. Nạp bộ kỹ năng đầy đủ (Chủ động, Nộ, Bị động, Haki, TAQ)
            short skillLv = (short) Math.max(10, Math.min(30, bot.level / 3));
            bot.setupBotSkills(bot.clazz, skillLv, tier, fruitId);

            // 4. Ngoại hình tương xứng Tier
            bot.setupBotAppearance(bot.clazz, tier);

            // 5. Khởi tạo chỉ số chiến đấu
            try { bot.setin4(); } catch (Exception ignored) {}
            bot.init();
            bot.ability = new Ability(bot);
            try { bot.updateParts(); } catch (Exception ignored) {}

            long hpMax = bot.ability.get_hp_max(true);
            long mpMax = bot.ability.get_mp_max(true);
            bot.hp = (int) Math.max(1000, hpMax);
            bot.mp = (int) Math.max(500, mpMax);
            bot.isdie = false;

            if (targetMap != null && targetMap.template != null) {
                short spawnX = (short) (targetMap.template.maxW / 2);
                short spawnY = (short) (targetMap.template.maxH / 2);
                bot.join(targetMap, spawnX, spawnY);
            }
            return bot;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Phân tích sức mạnh thực tế của người chơi thật (Level, HP, MP, Dame, Def, Trang bị, Kích Ẩn, Khảm Đá)
     * và tự động cân bằng Bot lên mức sức mạnh tương xứng chuẩn tỉ lệ (92% - 106% HP, 90% - 104% Dame, 85% - 99% Def).
     * Đảm bảo bot ghép trận không bị cùi/giấy máu và không bị quá VIP/ảo diệu.
     */
    public static void balanceAgainstPlayer(Bot bot, Player targetPlayer) {
        Bot.StatTier tier = (bot != null && bot.statTier != null) ? bot.statTier : Bot.StatTier.NORMAL;
        balanceAgainstPlayer(bot, targetPlayer, tier);
    }

    public static void balanceAgainstPlayer(Bot bot, Player targetPlayer, Bot.StatTier statTier) {
        if (bot == null) return;
        if (statTier == null) statTier = Bot.StatTier.NORMAL;
        try {
            if (targetPlayer == null) {
                // Fallback: Cân bằng theo level hiện tại của bot
                int botTier = bot.level >= 90 ? 4 : (bot.level >= 65 ? 3 : (bot.level >= 35 ? 2 : 1));
                bot.autoAllocatePotentialPoints(bot.clazz);
                bot.setupBotEquip(bot.clazz, bot.level, botTier, botTier >= 2, botTier >= 2, botTier >= 2);
                bot.setupBotSkills(bot.clazz, (short) Math.max(15, Math.min(30, bot.level / 3)), botTier, botTier >= 3 ? ZUtil.random(1, 15) : 0);
                bot.setupBotAppearance(bot.clazz, botTier);
                bot.isBalancedStats = false;
                try { bot.setin4(); } catch (Exception ignored) {}
                bot.ability = new Ability(bot);
                bot.setAbility();
                try { bot.updateParts(); } catch (Exception ignored) {}

                int lv = Math.max(1, (int) bot.level);
                double scale = (statTier == Bot.StatTier.VIP) ? 1.0 : 0.55;
                long baseHp = (long) (Math.max(5000L, 5000L + lv * 800L + (lv > 50 ? (lv - 50L) * 1500L : 0L)) * scale);
                long baseMp = (long) (Math.max(1500L, 1000L + lv * 100L) * scale);
                long baseDame = (long) (Math.max(1000L, 800L + lv * 150L + (lv > 50 ? (lv - 50L) * 300L : 0L)) * scale);
                long baseDef = (long) (Math.max(400L, 300L + lv * 60L + (lv > 50 ? (lv - 50L) * 120L : 0L)) * scale);

                bot.hpMax = (int) Math.max(1500, baseHp);
                bot.hp = bot.hpMax;
                bot.mpMax = (int) Math.max(800, baseMp);
                bot.mp = bot.mpMax;
                bot.dame = (int) Math.max(400, baseDame);
                bot.damePercent = (statTier == Bot.StatTier.VIP) ? 1000 : 500;
                bot.def = (int) Math.max(150, baseDef);
                bot.defPercent = 0;
                bot.agility = (statTier == Bot.StatTier.VIP) ? 300 : 200;
                bot.crit = (statTier == Bot.StatTier.VIP) ? 250 : 120;
                bot.pierce = (statTier == Bot.StatTier.VIP) ? 200 : 100;
                bot.miss = (statTier == Bot.StatTier.VIP) ? 100 : 50;
                bot.resPhys = (statTier == Bot.StatTier.VIP) ? 150 : 80;
                bot.resMag = (statTier == Bot.StatTier.VIP) ? 150 : 80;
                bot.isBalancedStats = true;
                bot.isdie = false;
                return;
            }

            // 1. Cân bằng Level (bám sát người chơi ±0..1 cấp)
            short targetLv = targetPlayer.level > 0 ? targetPlayer.level : 50;
            bot.level = (short) Math.max(1, targetLv + ZUtil.random(-1, 1));

            // 2. Đánh giá phẩm chất trang bị thực tế của Người Chơi
            int hoanMyCount = 0;
            int maxUpgradeLv = 0;
            int totalGems = 0;
            int kichAnCount = 0;

            if (targetPlayer.item != null && targetPlayer.item.it_body != null) {
                for (Item_wear it : targetPlayer.item.it_body) {
                    if (it != null) {
                        if (it.isHoanMy == 1) hoanMyCount++;
                        if (it.levelUp > maxUpgradeLv) maxUpgradeLv = it.levelUp;
                        if (it.mdakham != null) totalGems += it.mdakham.length;
                        if (it.valueKichAn >= 0) kichAnCount++;
                    }
                }
            }

            long playerHpMax = (targetPlayer.ability != null) ? targetPlayer.ability.get_hp_max(true) : targetPlayer.hp;
            if (playerHpMax <= 0) playerHpMax = Math.max(10_000, targetPlayer.hpMax);
            long playerMpMax = (targetPlayer.ability != null) ? targetPlayer.ability.get_mp_max(true) : targetPlayer.mp;
            if (playerMpMax <= 0) playerMpMax = Math.max(2_000, targetPlayer.mpMax);
            long playerDame = (targetPlayer.ability != null) ? targetPlayer.ability.get_dame(true) : 1000;
            if (playerDame <= 0) playerDame = Math.max(1_000, targetPlayer.dame);
            long playerDef = (targetPlayer.ability != null) ? targetPlayer.ability.get_def(true) : 500;
            if (playerDef <= 0) playerDef = Math.max(500, targetPlayer.def);
            int playerDamePercent = (targetPlayer.ability != null) ? targetPlayer.ability.get_dame_percent(true) : 0;
            int playerDefPercent = (targetPlayer.ability != null) ? targetPlayer.ability.get_def_percent(true) : 0;
            int playerCrit = (targetPlayer.ability != null) ? targetPlayer.ability.get_crit(true) : 150;
            int playerPierce = (targetPlayer.ability != null) ? targetPlayer.ability.get_pierce(true) : 100;
            int playerMiss = (targetPlayer.ability != null) ? targetPlayer.ability.get_miss(true) : 50;
            int playerAgility = (targetPlayer.ability != null) ? targetPlayer.ability.get_agility(true) : 200;
            int playerResPhys = (targetPlayer.ability != null) ? targetPlayer.ability.get_dame_resist(true) : 50;
            int playerResMag = (targetPlayer.ability != null) ? targetPlayer.ability.get_dame_resist_ap(true) : 50;

            // Xác định Tier Bot tương thích sức mạnh người chơi
            double calculatedTier;
            boolean hasPlayerThanTrang = false;
            if (targetPlayer.item != null && targetPlayer.item.it_body != null) {
                for (Item_wear it : targetPlayer.item.it_body) {
                    if (it != null && it.isThanTrang()) {
                        hasPlayerThanTrang = true;
                        break;
                    }
                }
            }

            if (hasPlayerThanTrang || maxUpgradeLv >= 15 || hoanMyCount >= 3 || playerHpMax >= 150_000 || playerDame >= 20_000 || targetLv >= 90) {
                // Người chơi Siêu VIP
                calculatedTier = 3.5 + (ZUtil.random(0, 10) / 20.0);
            } else if (maxUpgradeLv >= 13 || hoanMyCount >= 2 || playerHpMax >= 70_000 || playerDame >= 10_000 || targetLv >= 65) {
                // Người chơi Cao Thủ
                calculatedTier = 2.5 + (ZUtil.random(0, 10) / 20.0);
            } else if (maxUpgradeLv >= 11 || playerHpMax >= 30_000 || playerDame >= 4_000 || targetLv >= 35) {
                // Người chơi Tầm Trung
                calculatedTier = 2.0;
            } else {
                // Người chơi Cơ Bản
                calculatedTier = 1.5;
            }

            // Điều chỉnh tier theo statTier nếu là BOT NORMAL
            if (statTier == Bot.StatTier.NORMAL && calculatedTier > 2.5) {
                calculatedTier = 2.0;
            }

            boolean useHM = (calculatedTier >= 2.0 || hoanMyCount > 0);
            boolean useKD = true;
            boolean useKA = (calculatedTier >= 2.0 || kichAnCount > 0);
            
            int fruitId = 0;
            if (calculatedTier >= 3.5) {
                fruitId = Player.BOT_TOP_TIER_FRUITS[ZUtil.random(Player.BOT_TOP_TIER_FRUITS.length)];
            } else if (calculatedTier >= 2.5) {
                fruitId = Player.BOT_MID_TIER_FRUITS[ZUtil.random(Player.BOT_MID_TIER_FRUITS.length)];
            } else {
                fruitId = Player.BOT_BASIC_TIER_FRUITS[ZUtil.random(Player.BOT_BASIC_TIER_FRUITS.length)];
            }

            // 3. Cài đặt toàn bộ Trang bị, Kỹ năng, Tiềm năng, Ngoại hình
            int chosenThanTrangSet = Player.selectThanTrangSetIndex(bot.clazz, fruitId, bot.name);
            bot.autoAllocatePotentialPoints(bot.clazz);
            bot.setupBotEquip(bot.clazz, bot.level, calculatedTier, useHM, useKD, useKA, chosenThanTrangSet);
            bot.setupBotSkills(bot.clazz, (short) Math.max(15, Math.min(30, bot.level >= 80 ? 30 : bot.level / 3 + 5)), calculatedTier, fruitId);
            bot.setupBotAppearance(bot.clazz, calculatedTier);
            bot.isBalancedStats = false;
            try { bot.setin4(); } catch (Exception ignored) {}
            bot.ability = new Ability(bot);
            bot.setAbility();
            try { bot.updateParts(); } catch (Exception ignored) {}

            // 4. Cân bằng Chỉ Số Chuẩn Toàn Diện theo tỉ lệ chuẩn
            double hpFactor;
            double mpFactor;
            double dameFactor;
            double defFactor;
            double critScale;

            if (statTier == Bot.StatTier.VIP) {
                // BOT VIP: 95% - 105% stats (tối đa 110%) của người chơi thật
                hpFactor = 0.95 + (ZUtil.random(0, 10) / 100.0);
                mpFactor = 0.90 + (ZUtil.random(0, 10) / 100.0);
                dameFactor = 0.95 + (ZUtil.random(0, 10) / 100.0);
                defFactor = 0.90 + (ZUtil.random(0, 10) / 100.0);
                critScale = 0.95 + (ZUtil.random(0, 10) / 100.0);
            } else {
                // BOT NORMAL: 50% - 60% stats của người chơi thật (giúp người chơi vui vẻ, dễ thở)
                hpFactor = 0.50 + (ZUtil.random(0, 10) / 100.0);
                mpFactor = 0.50 + (ZUtil.random(0, 10) / 100.0);
                dameFactor = 0.50 + (ZUtil.random(0, 10) / 100.0);
                defFactor = 0.45 + (ZUtil.random(0, 10) / 100.0);
                critScale = 0.55 + (ZUtil.random(0, 10) / 100.0);
            }

            bot.hpMax = (int) Math.max(1500, (long) (playerHpMax * hpFactor));
            bot.hp = bot.hpMax;

            bot.mpMax = (int) Math.max(800, (long) (playerMpMax * mpFactor));
            bot.mp = bot.mpMax;

            bot.dame = (int) Math.max(300, (long) (playerDame * dameFactor));
            bot.damePercent = Math.max(300, playerDamePercent > 0 ? (int) (playerDamePercent * dameFactor) : (statTier == Bot.StatTier.VIP ? 1000 : 500));

            bot.def = (int) Math.max(100, (long) (playerDef * defFactor));
            bot.defPercent = (int) (playerDefPercent > 0 ? playerDefPercent * defFactor : 0);

            bot.crit = (int) Math.min(650, Math.max(80, (long) (playerCrit * critScale)));
            bot.pierce = (int) Math.min(650, Math.max(60, (long) (playerPierce * critScale)));
            bot.miss = (int) Math.min(400, Math.max(30, (long) (playerMiss * critScale)));
            bot.agility = (int) Math.min(450, Math.max(180, playerAgility > 0 ? (int) (playerAgility * (statTier == Bot.StatTier.VIP ? 0.95 : 0.70)) : 280));
            bot.resPhys = (int) Math.min(500, Math.max(30, (long) (playerResPhys * critScale)));
            bot.resMag = (int) Math.min(500, Math.max(30, (long) (playerResMag * critScale)));

            bot.isBalancedStats = true;
            bot.isdie = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cân bằng Bot theo đội hình hoặc phòng đấu có nhiều người chơi thật.
     */
    public static void balanceAgainstTeam(Bot bot, List<Player> teamPlayers, int defaultLevel) {
        if (bot == null) return;
        if (teamPlayers == null || teamPlayers.isEmpty()) {
            balanceAgainstPlayer(bot, null);
            return;
        }

        try {
            long sumHp = 0;
            long sumDame = 0;
            int sumLv = 0;
            int realCount = 0;
            Player strongestPlayer = null;
            long maxHp = 0;

            for (Player p : teamPlayers) {
                if (p != null && !p.isBot && !p.isDe && !(p instanceof bot.mercenary.MercenaryBot)) {
                    long hp = (p.ability != null) ? p.ability.get_hp_max(true) : p.hp;
                    long dame = (p.ability != null) ? p.ability.get_dame(true) : 1000;
                    sumHp += hp;
                    sumDame += dame;
                    sumLv += p.level;
                    realCount++;

                    if (hp > maxHp) {
                        maxHp = hp;
                        strongestPlayer = p;
                    }
                }
            }

            if (realCount == 0 || strongestPlayer == null) {
                balanceAgainstPlayer(bot, null);
                return;
            }

            // Cân bằng theo người chơi tiêu biểu / mạnh trong đội
            balanceAgainstPlayer(bot, strongestPlayer);

            // Hiệu chỉnh level bot theo trung bình cộng level đội
            short avgLv = (short) Math.max(1, sumLv / realCount);
            bot.level = (short) Math.max(1, avgLv + ZUtil.random(-1, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Thuật toán Cân Bằng Sát Thương Giao Tranh Chống One-Hit Hai Chiều Đối Xứng Chuẩn (Universal Symmetric Combat Damage Balance).
     * Được gọi trực tiếp trong `Fire_Player` trước khi trừ máu mục tiêu.
     *
     * @param attacker Người hoặc Bot tấn công.
     * @param target Người hoặc Bot bị tấn công.
     * @param currentDamage Sát thương tính toán ban đầu.
     * @param isCrit Đòn đánh có chí mạng hay không.
     * @return Sát thương đã được cân bằng chuẩn xác, nhịp độ 7 - 12 hit, không one-hit và không quá yếu.
     */
    public static long applyCombatDamageBalance(Player attacker, Player target, long currentDamage, boolean isCrit) {
        if (attacker == null || target == null || currentDamage <= 0 || target.get_eff(9) != null || target.get_eff(300) != null) {
            return 0;
        }

        // =========================================================================
        // CASE 1: Người chơi thật (hoặc Bot) tấn công BOT
        // =========================================================================
        if (target.isBot) {
            long botHpMax = (target.ability != null) ? target.ability.get_hp_max(true) : target.hp;
            if (botHpMax <= 0) botHpMax = Math.max(5000, target.hp);

            // Giới hạn sát thương tối đa: 10% (thường) hoặc 16% (chí mạng/nộ)
            long maxAllowed = (long) (botHpMax * (isCrit ? 0.16 : 0.10));
            if (maxAllowed < 500) maxAllowed = 500;

            // Đảm bảo sát thương tối thiểu: 2.5% HP của Bot
            long minAllowed = (long) (botHpMax * 0.025);
            if (minAllowed < 50) minAllowed = 50;

            if (currentDamage > maxAllowed) {
                currentDamage = maxAllowed;
            } else if (currentDamage < minAllowed) {
                currentDamage = minAllowed;
            }
            return currentDamage;
        }

        // =========================================================================
        // CASE 2: BOT tấn công Người chơi thật (CÂN BẰNG ĐỐI XỨNG HOÀN TOÀN)
        // =========================================================================
        if (attacker.isBot && !target.isBot) {
            long playerHpMax = (target.ability != null) ? target.ability.get_hp_max(true) : target.hp;
            if (playerHpMax <= 0) playerHpMax = Math.max(5000, target.hp);

            // Giới hạn sát thương tối đa của bot: 10% (thường) hoặc 16% (chí mạng/nộ)
            long maxAllowed = (long) (playerHpMax * (isCrit ? 0.16 : 0.10));
            if (maxAllowed < 500) maxAllowed = 500;

            // Sát thương tối thiểu của bot: 2.5% Max HP của người chơi
            long minAllowed = (long) (playerHpMax * 0.025);
            if (minAllowed < 50) minAllowed = 50;

            if (currentDamage > maxAllowed) {
                currentDamage = maxAllowed;
            } else if (currentDamage < minAllowed) {
                currentDamage = minAllowed;
            }
            return currentDamage;
        }

        // =========================================================================
        // CASE 3: Người chơi thật vs Người chơi thật (Giữ nguyên cơ chế gốc)
        // =========================================================================
        return currentDamage;
    }
}
