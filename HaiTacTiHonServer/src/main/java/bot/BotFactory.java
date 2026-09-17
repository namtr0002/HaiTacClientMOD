package bot;

import model.Player;
import core.ZUtil;
import map.Zone;

public class BotFactory {

    private static final BotFactory instance = new BotFactory();

    public static BotFactory getInstance() {
        return instance;
    }

    public Bot newBot(int id, String username, String name, Zone targetMap) {
        try {
            byte clazz = (byte) ZUtil.random(1, 5);
            short level = (short) (targetMap != null && targetMap.template != null ? Math.max(20, targetMap.template.id * 2) : 50);
            double tier = level >= 85 ? 3.5 : (level >= 60 ? 2.5 : (level >= 30 ? 2.0 : 1.5));
            int fruitId = tier >= 3.5 ? Player.BOT_TOP_TIER_FRUITS[ZUtil.random(Player.BOT_TOP_TIER_FRUITS.length)]
                    : (tier >= 2.5 ? Player.BOT_MID_TIER_FRUITS[ZUtil.random(Player.BOT_MID_TIER_FRUITS.length)]
                    : Player.BOT_BASIC_TIER_FRUITS[ZUtil.random(Player.BOT_BASIC_TIER_FRUITS.length)]);
            
            Bot bot = BotBalanceEngine.createMemoryBot(id, name != null ? name : username, clazz, level, tier, tier >= 2.0, true, tier >= 2.0, fruitId, targetMap);
            return bot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BotPVP newBotPVP(int id, String username, String name, Zone targetMap) {
        try {
            byte clazz = (byte) ZUtil.random(1, 5);
            short level = (short) (targetMap != null && targetMap.template != null ? Math.max(30, targetMap.template.id) : 60);
            double tier = level >= 85 ? 3.5 : (level >= 60 ? 2.5 : (level >= 30 ? 2.0 : 1.5));
            int fruitId = tier >= 3.5 ? Player.BOT_TOP_TIER_FRUITS[ZUtil.random(Player.BOT_TOP_TIER_FRUITS.length)]
                    : (tier >= 2.5 ? Player.BOT_MID_TIER_FRUITS[ZUtil.random(Player.BOT_MID_TIER_FRUITS.length)]
                    : Player.BOT_BASIC_TIER_FRUITS[ZUtil.random(Player.BOT_BASIC_TIER_FRUITS.length)]);

            BotPVP bot = new BotPVP(id, name != null ? name : username);
            bot.isBot = true;
            bot.clazz = clazz;
            bot.level = level;
            bot.autoAllocatePotentialPoints(clazz);
            bot.setupBotEquip(clazz, level, tier, tier >= 2.0, true, tier >= 2.0);
            bot.setupBotSkills(clazz, (short) Math.max(15, Math.min(30, level >= 80 ? 30 : level / 3 + 5)), tier, fruitId);
            bot.setupBotAppearance(clazz, tier);
            bot.setin4();
            bot.init();
            bot.ability = new ability.Ability(bot);
            bot.updateParts();
            if (bot.ability != null) {
                bot.hp = (int) Math.max(5000, bot.ability.get_hp_max(true));
                bot.mp = (int) Math.max(2000, bot.ability.get_mp_max(true));
            }
            bot.isdie = false;

            if (targetMap != null && targetMap.template != null) {
                bot.join(targetMap, (short) (targetMap.template.maxW / 2), (short) (targetMap.template.maxH / 2));
            }
            return bot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BotKhoBau newBotKhoBau(int id, String username, String name, Zone targetMap) {
        try {
            BotKhoBau bot = new BotKhoBau(id, name != null ? name : username);
            bot.isBot = true;
            bot.clazz = (byte) ZUtil.random(1, 5);
            bot.level = (short) ZUtil.random(75, 100);
            double tier = bot.level >= 85 ? 3.5 : 2.5;
            int fruitId = tier >= 3.5 ? Player.BOT_TOP_TIER_FRUITS[ZUtil.random(Player.BOT_TOP_TIER_FRUITS.length)]
                    : Player.BOT_MID_TIER_FRUITS[ZUtil.random(Player.BOT_MID_TIER_FRUITS.length)];

            bot.autoAllocatePotentialPoints(bot.clazz);
            bot.setupBotEquip(bot.clazz, bot.level, tier, true, true, true);
            bot.setupBotSkills(bot.clazz, (short) Math.max(20, Math.min(30, bot.level >= 80 ? 30 : bot.level / 3 + 5)), tier, fruitId);
            bot.setupBotAppearance(bot.clazz, tier);
            bot.setin4();
            bot.init();
            bot.ability = new ability.Ability(bot);
            bot.updateParts();
            if (bot.ability != null) {
                bot.hp = (int) Math.max(25000, bot.ability.get_hp_max(true));
                bot.mp = (int) Math.max(5000, bot.ability.get_mp_max(true));
            }
            bot.isdie = false;

            if (targetMap != null && targetMap.template != null) {
                bot.join(targetMap, (short) (targetMap.template.maxW / 2), (short) (targetMap.template.maxH / 2));
            }
            return bot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BotTruyNa newBotTruyNa(int id, String username, String name, Zone targetMap) {
        try {
            BotTruyNa bot = new BotTruyNa(id, name != null ? name : username);
            bot.isBot = true;
            bot.clazz = (byte) ZUtil.random(1, 5);
            bot.level = (short) ZUtil.random(75, 100);
            double tier = bot.level >= 85 ? 3.5 : 2.5;
            int fruitId = tier >= 3.5 ? Player.BOT_TOP_TIER_FRUITS[ZUtil.random(Player.BOT_TOP_TIER_FRUITS.length)]
                    : Player.BOT_MID_TIER_FRUITS[ZUtil.random(Player.BOT_MID_TIER_FRUITS.length)];

            bot.autoAllocatePotentialPoints(bot.clazz);
            bot.setupBotEquip(bot.clazz, bot.level, tier, true, true, true);
            bot.setupBotSkills(bot.clazz, (short) Math.max(20, Math.min(30, bot.level >= 80 ? 30 : bot.level / 3 + 5)), tier, fruitId);
            bot.setupBotAppearance(bot.clazz, tier);
            bot.setin4();
            bot.init();
            bot.ability = new ability.Ability(bot);
            bot.updateParts();
            if (bot.ability != null) {
                bot.hp = (int) Math.max(20000, bot.ability.get_hp_max(true));
                bot.mp = (int) Math.max(5000, bot.ability.get_mp_max(true));
            }
            bot.isdie = false;

            if (targetMap != null && targetMap.template != null) {
                bot.join(targetMap, (short) (targetMap.template.maxW / 2), (short) (targetMap.template.maxH / 2));
            }
            return bot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
