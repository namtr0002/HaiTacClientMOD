package bot;

import ability.Ability;
import map.Zone;
import model.Player;
import map.zones.DauTruongTuDo;
import core.ZUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BotDauTruongTuDo — Hệ thống Bot Đấu Trường Tự Do Thông Minh & Cân Bằng Đa Dạng.
 *
 * <p>Tính năng nổi bật:
 * 1. 4 Lối Build (Archetypes) thực tế mô phỏng người chơi:
 *    - Ép Cấp / Thánh Skill: Level thấp nhưng Skill Max 28-30, TAQ Lv 4-5, Đồ khảm ngọc kích ẩn cao.
 *    - Cày Cấp Cao / Skill Vừa: Level cao, Skill 18-24, TAQ 2-3, Lượng máu và thủ cực trâu.
 *    - Đại Gia Siêu VIP: Level cao, Skill Max 30, TAQ Lv 5 Max, Đồ Thần Thoại +15..+17, Ngọc 9-10.
 *    - Đấu Sĩ Chuẩn Mực: Chỉ số cân bằng hài hòa giữa công, thủ và kỹ năng.
 * 2. Cân bằng động (Adaptive Balancing) theo người chơi thật trong khu vực.
 * 3. Tự động điều tiết số lượng bot (Auto Throttling): Tự tắt bot khi đông người thật, bổ sung khi vắng.
 */
public class BotDauTruongTuDo extends Bot {

    public static final int BUILD_LOW_LV_HIGH_SKILL = 0; // Ép cấp - Skill cao
    public static final int BUILD_HIGH_LV_MID_SKILL  = 1; // Cấp cao - Skill vừa
    public static final int BUILD_SUPER_VIP_MAX      = 2; // Siêu VIP - Full Max
    public static final int BUILD_BALANCED_STANDARD  = 3; // Chuẩn mực - Cân bằng

    public static final List<BotDauTruongTuDo> POOL = new CopyOnWriteArrayList<>();
    private static long lastPeriodicCheckTime = 0;

    private long respawnTime = 0;
    private byte originalTypePk = 4;
    private int tier = 2;
    private int buildType = BUILD_BALANCED_STANDARD;

    public BotDauTruongTuDo(int id, String name) throws Exception {
        super(id, name);
        this.disableBaseChat = true;
    }

    public void setOriginalTypePk(byte pk) {
        this.originalTypePk = pk;
        this.type_pk = pk;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public void setBuildType(int buildType) {
        this.buildType = buildType;
    }

    @Override
    public void init() {
        this.type_pk = this.originalTypePk;
        this.respawnTime = 0;
        this.setAttack(new AttackAround());
        this.setMove(new MoveToTarget(null, 1500));
        this.ability = new Ability(this);
        if (this.ability != null) {
            this.hp = (int) Math.max(15000, this.ability.get_hp_max(true));
            this.mp = (int) Math.max(3000, this.ability.get_mp_max(true));
        }
        this.isdie = false;
    }

    @Override
    public void update() {
        if (!DauTruongTuDo.IsOpen()) {
            this.leave();
            return;
        }

        // Kiểm tra và điều tiết số lượng bot định kỳ toàn bộ đấu trường
        checkAndAdjustAllZones();

        if (this.isdie) {
            long now = System.currentTimeMillis();
            if (respawnTime == 0) {
                respawnTime = now + ZUtil.random(3000, 5000);
            } else if (now >= respawnTime) {
                respawnTime = 0;
                this.isdie = false;
                if (this.map != null) {
                    short[] pt = bot.SmartMovement.getRandomGroundPoint(this.map);
                    this.x = pt[0];
                    this.y = pt[1];
                    this.type_pk = this.originalTypePk;
                    if (this.ability != null) {
                        this.hp = (int) this.ability.get_hp_max(true);
                        this.mp = (int) this.ability.get_mp_max(true);
                    }
                    // Cân bằng lại máu nếu gặp người chơi thật có chỉ số cao
                    balanceWithZoneRealPlayers();
                    try {
                        this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                        for (int i = 0; i < this.map.players.size(); i++) {
                            Player p0 = this.map.players.get(i);
                            if (p0 != null && p0.getService() != null) {
                                p0.getService().update_PK(this, false);
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
            return;
        }

        // Tự bơm máu thông minh khi HP < 35%
        if (this.ability != null && this.hp < this.ability.get_hp_max(true) * 0.35) {
            this.hp = Math.min((int) this.ability.get_hp_max(true), this.hp + (int) (this.ability.get_hp_max(true) * 0.3));
        }

        super.update();
    }

    /**
     * Cân bằng động sức mạnh của Bot theo người chơi thật đang có mặt trong khu.
     */
    public void balanceWithZoneRealPlayers() {
        if (this.map == null) return;
        List<Player> realPlayers = getRealPlayersInZone(this.map);
        if (realPlayers.isEmpty()) return;

        BotBalanceEngine.balanceAgainstTeam(this, realPlayers, this.level);
    }

    @Override
    public void join(Zone map, short x, short y) {
        super.join(map, x, y);
        POOL.add(this);
        balanceWithZoneRealPlayers();
    }

    @Override
    public void leave() {
        POOL.remove(this);
        super.leave();
    }

    /**
     * Lấy danh sách người chơi thật (không tính bot, đệ tử, lính đánh thuê) trong khu.
     */
    public static List<Player> getRealPlayersInZone(Zone zone) {
        List<Player> result = new ArrayList<>();
        if (zone == null || zone.players == null) return result;
        for (int i = 0; i < zone.players.size(); i++) {
            Player p = zone.players.get(i);
            if (p != null && !p.isBot && !p.isDe && !(p instanceof bot.mercenary.MercenaryBot)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Lấy danh sách Bot Đấu Trường đang có mặt trong khu.
     */
    public static List<BotDauTruongTuDo> getBotsInZone(Zone zone) {
        List<BotDauTruongTuDo> result = new ArrayList<>();
        if (zone == null) return result;
        for (BotDauTruongTuDo b : POOL) {
            if (b != null && b.map != null && b.map.equals(zone)) {
                result.add(b);
            }
        }
        return result;
    }

    /**
     * Tự động điều tiết số lượng Bot trong mỗi khu vực theo số lượng người thật:
     * - 0 người thật: Duy trì 3 bot để tạo không khí.
     * - 1 - 2 người thật: Duy trì 2 - 3 bot làm đối thủ.
     * - 3 - 5 người thật: Giảm xuống còn 1 bot.
     * - >= 6 người thật: Tắt toàn bộ bot (0 bot) để nhường toàn bộ sân đấu cho người thật.
     */
    public static void adjustBotsInZone(Zone zone) {
        if (zone == null || !DauTruongTuDo.IsOpen()) return;
        int mapId = zone.template.id;
        if (mapId != 70 && mapId != 71 && mapId != 72 && mapId != 74) return;

        List<Player> realPlayers = getRealPlayersInZone(zone);
        List<BotDauTruongTuDo> botsInZone = getBotsInZone(zone);
        int realCount = realPlayers.size();

        int targetBots = 3;
        if (realCount >= 6) {
            targetBots = 0;
        } else if (realCount >= 3) {
            targetBots = 1;
        } else if (realCount >= 1) {
            targetBots = 2;
        }

        int currentBots = botsInZone.size();
        if (currentBots > targetBots) {
            int removeCount = currentBots - targetBots;
            for (int i = 0; i < removeCount && i < botsInZone.size(); i++) {
                BotDauTruongTuDo b = botsInZone.get(i);
                if (b != null) {
                    b.leave();
                }
            }
        } else if (currentBots < targetBots) {
            int addCount = targetBots - currentBots;
            for (int i = 0; i < addCount; i++) {
                spawnBotForZone(zone, currentBots + i);
            }
        }

        // Cân bằng động màu cờ giữa 2 phe (Phe 4 vs Phe 5)
        int total4 = 0;
        int total5 = 0;
        if (zone.players != null) {
            for (int i = 0; i < zone.players.size(); i++) {
                Player p = zone.players.get(i);
                if (p != null) {
                    if (p.type_pk == 4) total4++;
                    else if (p.type_pk == 5) total5++;
                }
            }
        }
        if (Math.abs(total4 - total5) > 1) {
            for (BotDauTruongTuDo b : getBotsInZone(zone)) {
                if (total4 > total5 && b.type_pk == 4) {
                    b.setOriginalTypePk((byte) 5);
                    b.type_pk = 5;
                    total4--;
                    total5++;
                    try { zone.change_flag(b, 5); } catch (Exception ignored) {}
                } else if (total5 > total4 && b.type_pk == 5) {
                    b.setOriginalTypePk((byte) 4);
                    b.type_pk = 4;
                    total5--;
                    total4++;
                    try { zone.change_flag(b, 4); } catch (Exception ignored) {}
                }
                if (Math.abs(total4 - total5) <= 1) break;
            }
        }
    }

    /**
     * Kiểm tra và điều tiết định kỳ mỗi 5 giây cho toàn bộ các map đấu trường.
     */
    public static void checkAndAdjustAllZones() {
        if (!DauTruongTuDo.IsOpen()) return;
        long now = System.currentTimeMillis();
        if (now - lastPeriodicCheckTime < 5000L) return;
        lastPeriodicCheckTime = now;

        int[] mapIds = new int[]{70, 71, 72, 74};
        for (int mapId : mapIds) {
            Zone[] zones = Zone.getMapByID(mapId);
            if (zones == null) continue;
            for (int zIdx = 0; zIdx < Math.min(2, zones.length); zIdx++) {
                Zone z = zones[zIdx];
                if (z != null) {
                    adjustBotsInZone(z);
                }
            }
        }
    }

    /**
     * Sinh 1 Bot đấu sĩ chuẩn cho Zone theo các Archetype build phong phú.
     */
    public static void spawnBotForZone(Zone zone, int botIndex) {
        if (zone == null || zone.template == null) return;
        int mapId = zone.template.id;
        int tier = (mapId == 70 ? 2 : (mapId == 71 ? 3 : (mapId == 72 ? 4 : 5)));
        int archetype = ZUtil.random(0, 4); // Random 1 trong 4 phong cách build

        try {
            int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
            byte botClazz = (byte) ZUtil.random(1, 5);
            String botName = bot.botplayer.BotNameGenerator.getRandomBotName();

            BotDauTruongTuDo bot = new BotDauTruongTuDo(botId, botName);
            bot.isBot = true;
            bot.clazz = botClazz;
            bot.setTier(tier);
            bot.setBuildType(archetype);

            short botLv = 50;
            short skillLv = 20;
            boolean allowHM = true;
            boolean allowKA = true;
            boolean allowKD = true;

            switch (mapId) {
                case 70: // 5x - 6x đầu
                    if (archetype == BUILD_LOW_LV_HIGH_SKILL) {
                        botLv = (short) ZUtil.random(50, 53); // Ép cấp thấp
                        skillLv = (short) ZUtil.random(28, 30); // Skill max
                    } else if (archetype == BUILD_HIGH_LV_MID_SKILL) {
                        botLv = (short) ZUtil.random(62, 64); // Cấp cao
                        skillLv = (short) ZUtil.random(18, 22); // Skill vừa
                    } else if (archetype == BUILD_SUPER_VIP_MAX) {
                        botLv = (short) ZUtil.random(60, 64);
                        skillLv = 30;
                    } else { // BUILD_BALANCED_STANDARD
                        botLv = (short) ZUtil.random(54, 59);
                        skillLv = (short) ZUtil.random(22, 25);
                    }
                    break;

                case 71: // 6x - 7x
                    if (archetype == BUILD_LOW_LV_HIGH_SKILL) {
                        botLv = (short) ZUtil.random(65, 68);
                        skillLv = (short) ZUtil.random(28, 30);
                    } else if (archetype == BUILD_HIGH_LV_MID_SKILL) {
                        botLv = (short) ZUtil.random(77, 79);
                        skillLv = (short) ZUtil.random(22, 25);
                    } else if (archetype == BUILD_SUPER_VIP_MAX) {
                        botLv = (short) ZUtil.random(75, 79);
                        skillLv = 30;
                    } else {
                        botLv = (short) ZUtil.random(69, 74);
                        skillLv = (short) ZUtil.random(25, 28);
                    }
                    break;

                case 72: // 8x - 9x
                    if (archetype == BUILD_LOW_LV_HIGH_SKILL) {
                        botLv = (short) ZUtil.random(80, 83);
                        skillLv = 30;
                    } else if (archetype == BUILD_HIGH_LV_MID_SKILL) {
                        botLv = (short) ZUtil.random(92, 94);
                        skillLv = (short) ZUtil.random(24, 27);
                    } else if (archetype == BUILD_SUPER_VIP_MAX) {
                        botLv = (short) ZUtil.random(90, 94);
                        skillLv = 30;
                    } else {
                        botLv = (short) ZUtil.random(84, 89);
                        skillLv = (short) ZUtil.random(28, 30);
                    }
                    break;

                case 74: // 95+ / 1xx Siêu cấp
                default:
                    if (archetype == BUILD_LOW_LV_HIGH_SKILL) {
                        botLv = (short) ZUtil.random(95, 98);
                        skillLv = 30;
                    } else if (archetype == BUILD_HIGH_LV_MID_SKILL) {
                        botLv = (short) ZUtil.random(115, 125);
                        skillLv = (short) ZUtil.random(26, 29);
                    } else if (archetype == BUILD_SUPER_VIP_MAX) {
                        botLv = (short) ZUtil.random(110, 130);
                        skillLv = 30;
                    } else {
                        botLv = (short) ZUtil.random(100, 110);
                        skillLv = 30;
                    }
                    break;
            }

            bot.level = botLv;
            int count4 = 0;
            int count5 = 0;
            if (zone.players != null) {
                for (int i = 0; i < zone.players.size(); i++) {
                    Player p0 = zone.players.get(i);
                    if (p0 != null) {
                        if (p0.type_pk == 4) count4++;
                        else if (p0.type_pk == 5) count5++;
                    }
                }
            }
            byte assignedPk = (byte) (count4 <= count5 ? 4 : 5);
            bot.setOriginalTypePk(assignedPk);

            // Điểm tiềm năng chuẩn
            bot.autoAllocatePotentialPoints(botClazz);

            // Trang bị chuẩn theo archetype và tier
            bot.setupBotEquip(botClazz, botLv, tier, allowHM, allowKA, allowKD);

            // Kỹ năng và Trái Ác Quỷ thức tỉnh
            bot.setupBotSkills(botClazz, skillLv, tier >= 3 ? 1 : 2, 0);
            bot.setupBotAppearance(botClazz, tier);
            bot.setin4();
            bot.init();
            bot.updateParts();

            short spawnX = (short) ZUtil.random(150, Math.max(200, zone.template.maxW - 150));
            short spawnY = (short) ZUtil.random(150, Math.max(200, zone.template.maxH - 150));
            bot.join(zone, spawnX, spawnY);
        } catch (Exception ignored) {}
    }

    /**
     * Tự động điều động bot ban đầu vào các map Đấu Trường Tự Do (Maps 70, 71, 72, 74).
     */
    public static void dispatchBots() {
        clearBots();
        int[] mapIds = new int[]{70, 71, 72, 74};
        for (int mapId : mapIds) {
            Zone[] zones = Zone.getMapByID(mapId);
            if (zones != null && zones.length > 0) {
                for (int zIndex = 0; zIndex < Math.min(2, zones.length); zIndex++) {
                    Zone arenaZone = zones[zIndex];
                    if (arenaZone != null) {
                        adjustBotsInZone(arenaZone);
                    }
                }
            }
        }
    }

    /**
     * Dọn sạch toàn bộ bot khi Đấu Trường đóng.
     */
    public static void clearBots() {
        for (BotDauTruongTuDo bot : POOL) {
            try {
                bot.leave();
            } catch (Exception ignored) {}
        }
        POOL.clear();
    }
}
