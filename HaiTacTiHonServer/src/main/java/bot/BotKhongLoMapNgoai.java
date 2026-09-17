package bot;

import ability.Ability;
import map.Zone;
import model.Player;
import map.zones.TranChienKhongLo;
import core.ZUtil;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BotKhongLoMapNgoai — Bot chiến trận roaming ở các map ngoài & Little Garden trong thời gian Phó Bản Khổng Lồ diễn ra.
 *
 * <p>Tính năng:
 * <ul>
 *   <li>Tự động đánh quái farm level/item trong map.</li>
 *   <li>Chủ động tấn công người chơi khi phát hiện trong tầm nhìn.</li>
 *   <li><b>Ưu tiên phản đòn cực mạnh (Revenge / Retaliation)</b> khi bị người chơi tấn công.</li>
 *   <li>Chỉ số & Level phân bổ cân bằng theo mức độ từng khu vực bản đồ.</li>
 * </ul>
 */
public class BotKhongLoMapNgoai extends Bot {

    public static final List<BotKhongLoMapNgoai> POOL = new CopyOnWriteArrayList<>();
    private long respawnTime = 0;
    public int mapTier = 1;

    public BotKhongLoMapNgoai(int id, String name, int mapTier) throws Exception {
        super(id, name);
        this.mapTier = mapTier;
        this.type_pk = (byte) (ZUtil.random(100) < 50 ? 4 : 5);
        this.disableBaseChat = false;
        this.id_map_save = 79; // Đảo Little Garden, tuyệt đối không trôi về Map 1
    }

    @Override
    public void init() {
        this.type_pk = (byte) (ZUtil.random(100) < 50 ? 4 : 5);
        this.respawnTime = 0;
        this.setAttack(new AttackAround());
        this.setMove(new MoveAround(2000L));
        if (this.ability != null) {
            this.hp = (int) Math.max(5000, this.ability.get_hp_max(true));
            this.mp = (int) Math.max(1000, this.ability.get_mp_max(true));
        }
        this.isdie = false;
    }

    @Override
    public void update() {
        if (!TranChienKhongLo.isOpen()) {
            this.leave();
            return;
        }

        if (this.isdie) {
            long now = System.currentTimeMillis();
            if (respawnTime == 0) {
                respawnTime = now + 10_000L;
            } else if (now >= respawnTime) {
                respawnTime = 0;
                this.isdie = false;
                if (this.map != null && this.map.template != null && this.map.template.id != 1 && this.map.template.id != 0) {
                    short[] pt = bot.SmartMovement.getRandomGroundPoint(this.map);
                    this.x = pt[0];
                    this.y = pt[1];
                    if (this.ability != null) {
                        this.hp = (int) this.ability.get_hp_max(true);
                        this.mp = (int) this.ability.get_mp_max(true);
                    }
                    this.type_pk = (byte) (ZUtil.random(100) < 50 ? 4 : 5);
                    try {
                        this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                        this.map.change_flag(this, this.type_pk);
                    } catch (Exception ignored) {}
                }
            }
            return;
        }

        super.update();
    }

    @Override
    public void join(Zone map, short x, short y) {
        super.join(map, x, y);
        try {
            if (map != null) {
                map.change_flag(this, this.type_pk);
            }
        } catch (Exception ignored) {}
        POOL.add(this);
    }

    @Override
    public void leave() {
        POOL.remove(this);
        super.leave();
    }

    /**
     * Tự động điều động bot ra các map ngoài và khu vực lân cận Little Garden.
     */
    public static void dispatchBots() {
        clearBots();

        // Danh sách các map phân cấp
        int[][] mapTiers = new int[][]{
            {75, 76},       // Tier 1: Level 45 - 55
            {77, 78, 79},   // Tier 2: Level 60 - 75
            {80, 81, 82}    // Tier 3: Level 80 - 95
        };

        for (int tierIdx = 0; tierIdx < mapTiers.length; tierIdx++) {
            int tier = tierIdx + 1;
            int[] maps = mapTiers[tierIdx];

            for (int mapId : maps) {
                Zone[] zones = Zone.getMapByID(mapId);
                if (zones != null && zones.length > 0 && zones[0] != null) {
                    Zone zone = zones[0];
                    int botsToSpawn = 2; // 2 bot mỗi map

                    for (int i = 0; i < botsToSpawn; i++) {
                        try {
                            int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                            byte botClazz = (byte) ZUtil.random(1, 5);
                            String botName = bot.botplayer.BotNameGenerator.getRandomBotName();

                            BotKhongLoMapNgoai bot = new BotKhongLoMapNgoai(botId, botName, tier);
                            bot.isBot = true;
                            bot.clazz = botClazz;

                            short botLv;
                            if (tier == 1) botLv = (short) ZUtil.random(45, 55);
                            else if (tier == 2) botLv = (short) ZUtil.random(60, 75);
                            else botLv = (short) ZUtil.random(80, 95);

                            bot.level = botLv;
                            if (zone.players != null && !zone.players.isEmpty()) {
                                BotBalanceEngine.balanceAgainstTeam(bot, zone.players, botLv);
                            } else {
                                bot.autoAllocatePotentialPoints(botClazz);
                                bot.setupBotEquip(botClazz, botLv, tier >= 2 ? tier + 1 : tier, tier >= 2, tier >= 2, tier >= 2);
                                bot.setupBotSkills(botClazz, (short) Math.max(15, botLv / 3), tier, tier >= 2 ? ZUtil.random(1, 15) : 0);
                                bot.setupBotAppearance(botClazz, tier);
                                bot.setin4();
                                bot.init();
                                bot.ability = new Ability(bot);
                                bot.updateParts();
                                if (bot.ability != null) {
                                    bot.hp = (int) Math.max(10000, bot.ability.get_hp_max(true));
                                    bot.mp = (int) Math.max(3000, bot.ability.get_mp_max(true));
                                }
                            }

                            short spawnX = (short) ZUtil.random(100, Math.max(200, zone.template.maxW - 100));
                            short spawnY = (short) ZUtil.random(150, Math.max(200, zone.template.maxH - 100));
                            bot.join(zone, spawnX, spawnY);
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
    }

    /**
     * Dọn sạch bot khi kết thúc hoạt động.
     */
    public static void clearBots() {
        for (BotKhongLoMapNgoai bot : POOL) {
            try {
                bot.leave();
            } catch (Exception ignored) {}
        }
        POOL.clear();
    }
}
