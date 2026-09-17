package bot.mercenary;

import bot.Bot;
import clan.Clan;
import bot.mercenary.MercenaryTemplate.MercEntry;
import model.Player;
import ability.Ability;
import map.Zone;
import mob.Mob;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import core.ZUtil;
import network.Message;
import network.SessionManager;
import skill.Skill_info;
import map.MapBossInfo;
import zabstracts.AbsBoss;
import boss.SuperBossManager;
import boss.BossPica;
import boss.BossTheGioi;
import event.eboss.LucciGioTo;
import map.zones.ChiemDao;

/**
 * MercenaryBot — Bot Lính Đánh Thuê Chuẩn Xịn.
 *
 * ==============================================================
 * ĐẶC ĐIỂM HOÀN THIỆN:
 *   1. Sử dụng trực tiếp setupBotEquip & setupBotSkills từ class Player (dùng 'this').
 *   2. Thừa hưởng cài đặt trang bị chuẩn Full 8 Món không bao giờ NullPointer hay thiếu đồ.
 *   3. AI Tàn Sát Quái Full Map: Truy tìm quái toàn bản đồ, xả skill áp sát mượt mà.
 *   4. Thưởng 60% EXP & Loot rớt về cho Chủ Thuê.
 * ==============================================================
 */
public class MercenaryBot extends Bot {

    public final int ownerPlayerId;
    public final MercEntry entry;
    public final int currencyUsed;

    public static final int STATUS_HOME    = 0; // Về nhà / Rút lính về / Ẩn
    public static final int STATUS_FOLLOW  = 1; // Đi theo chủ nhân (không đánh)
    public static final int STATUS_PROTECT = 2; // Bảo vệ chủ nhân trong phạm vi
    public static final int STATUS_ATTACK  = 3; // Tấn công toàn bản đồ

    public static final String[] STATUS_NAMES = {"Về nhà / Ẩn", "Đi theo", "Bảo vệ", "Tấn công"};

    public int mercStatus = STATUS_ATTACK;

    /**
     * Flag chống race condition: khi Manager đang chủ động di chuyển bot sang map mới
     * (onPlayerChangeMap / toggleOrSummon), set = true để tick() không gọi followOwnerToMap() song song.
     */
    public volatile boolean isChangingMap = false;
    /** Thời điểm join map gần nhất — debounce 800ms để không join 2 lần liên tiếp */
    public volatile long lastJoinTime = 0;

    public Player getOwner() {
        return SessionManager.PLAYERS_MAP.get(ownerPlayerId);
    }
    private long nextAttackTime = 0;
    private long nextChatTime = 0;
    private long respawnTimer = 0;
    private long nextIdleMoveTime = 0;
    private short idleTargetX = 0;
    private short idleTargetY = 0;

    // Chat ngẫu nhiên theo 3 phe (0: mặc định, 1: Hải Quân, 2: Hải Tặc, 3: Quân Cách Mạng)
    private static final String[][] FACTION_CHAT = new String[][]{
        {"Xông lên!", "Sẵn sàng chiến đấu!", "Tiến lên đồng đội!"},
        {"Bảo vệ hòa bình!", "Hải quân bất diệt!", "Quyết tiêu diệt hải tặc!", "Công lý sẽ chiến thắng!"},
        {"Ta là vua hải tặc!", "Kho báu là của ta!", "Xông lên đồng đội!", "Tiền beri rơi đầy đường!"},
        {"Tự do cho đại dương!", "Cách mạng sẽ chiến thắng!", "Lật đổ chính quyền!", "Vì lý tưởng lớn!"}
    };

    private MercenaryBot(int id, String name, int ownerPlayerId, MercEntry entry, int currencyUsed) throws Exception {
        super(id, name);
        this.ownerPlayerId = ownerPlayerId;
        this.entry = entry;
        this.currencyUsed = currencyUsed;
        this.disableBaseChat = true;
    }

    public static MercenaryBot create(Player owner, MercEntry entry, int currency) {
        if (owner == null || entry == null) return null;
        if (owner.map != null && isRestrictedPvpMap(owner.map)) return null;
        try {
            // Khử trùng lặp: Xóa bất kỳ bot lính cũ nào của cùng chủ nhân có cùng entry
            for (Player p : new ArrayList<>(SessionManager.PLAYERS_MAP.values())) {
                if (p instanceof MercenaryBot) {
                    MercenaryBot oldBot = (MercenaryBot) p;
                    if (oldBot.ownerPlayerId == owner.IDPlayer && oldBot.entry != null && oldBot.entry.id == entry.id) {
                        oldBot.despawn();
                    }
                }
            }

            int botId = -(100_000 + ZUtil.random(900_000));
            String botName = entry.name;

            MercenaryBot bot = new MercenaryBot(botId, botName, owner.IDPlayer, entry, currency);
            bot.isBot = true;
            bot.type_pk = owner.type_pk;
            bot.clan  = owner.clan;
            bot.typePirate = owner.typePirate;
            bot.clazz = (byte) (entry.clazz > 0 ? entry.clazz : ZUtil.random(1, 5));

            int ownerLv = Math.max(1, (int) owner.level);
            int targetMax = Math.min(ownerLv, Math.min(110, entry.maxLv));
            int targetMin = Math.max(1, Math.min(targetMax, targetMax - 3));
            bot.level = (short) Math.max(1, Math.min(targetMax, ZUtil.random(targetMin, targetMax)));
            bot.exp   = 0;
            bot.head  = 0;

            int defaultHairIcon;
            switch (bot.clazz) {
                case 1: defaultHairIcon = 1;  break;  // Võ sĩ
                case 2: defaultHairIcon = 24; break;  // Kiếm sĩ
                case 3: defaultHairIcon = 28; break;  // Đầu bếp
                case 4: defaultHairIcon = 32; break;  // Bác sĩ
                case 5: defaultHairIcon = 36; break;  // Xạ thủ
                default: defaultHairIcon = 1; break;
            }
            bot.hair = (short) defaultHairIcon;

            // Phân bổ điểm chỉ số
            long totalPoints = (long) bot.level * 10;
            bot.pointAttribute = 0;
            bot.point1 = totalPoints / 5;
            bot.point2 = totalPoints / 5;
            bot.point3 = totalPoints / 5;
            bot.point4 = totalPoints / 5;
            bot.point5 = totalPoints - (bot.point1 * 4);

            double maxTier = MercenaryTemplate.getMaxAllowedTier(bot.level);
            double effectiveTier = Math.min(maxTier, entry.tier);
            if (currency == MercenaryTemplate.CURRENCY_EXTOL && effectiveTier < 3.5 && bot.level >= 80) {
                effectiveTier = Math.min(maxTier, 3.5);
            }

            int fruitId = MercenaryTemplate.getFruitIdForEntry(entry);
            int chosenThanTrangSet = Player.selectThanTrangSetIndex(bot.clazz, fruitId, bot.name);

            boolean hasHoanMy = (effectiveTier >= 2.0);
            boolean hasKhamDa = (effectiveTier >= 1.5);
            boolean hasKichAn = (effectiveTier >= 2.5);

            // Khởi tạo trang bị, kỹ năng, ngoại hình chuẩn cân bằng theo cấp độ
            bot.setupBotEquip(bot.clazz, bot.level, effectiveTier, hasHoanMy, hasKhamDa, hasKichAn, chosenThanTrangSet);
            bot.setupBotSkills(bot.clazz, (short) Math.max(entry.skillLv, bot.level >= 80 ? 30 : (bot.level >= 50 ? 20 : 12)), effectiveTier, fruitId);
            bot.setupBotAppearance(bot.clazz, effectiveTier);

            bot.list_eff   = new CopyOnWriteArrayList<>();
            bot.list_quest = new ArrayList<>();
            bot.list_op_thongthao = new ArrayList<>();
            bot.type_pk    = owner.type_pk;

            bot.setupBalancedMercenaryStats();

            SessionManager.PLAYERS_MAP.put(bot.IDPlayer, bot);
            SessionManager.PLAYERS_BY_NAME.put(bot.name, bot);

            Zone ownerMap = owner.map;
            if (ownerMap != null) {
                bot.lastJoinTime = System.currentTimeMillis();
                bot.join(ownerMap, (short) (owner.x + bot.getOffsetX()), (short) (owner.y + bot.getOffsetY()));
            }

            return bot;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MercenaryBot createPreview(Player owner, MercEntry entry) {
        if (owner == null || entry == null) return null;
        try {
            int botId = -(900_000 + ZUtil.random(99_999));
            String botName = entry.name;

            MercenaryBot bot = new MercenaryBot(botId, botName, owner.IDPlayer, entry, MercenaryTemplate.CURRENCY_EXTOL);
            bot.isBot = true;
            int ownerLv = Math.max(1, (int) owner.level);
            int targetMax = Math.min(ownerLv, Math.min(110, entry.maxLv));
            bot.level = (short) targetMax;
            bot.clan  = owner.clan;
            bot.typePirate = owner.typePirate;
            bot.exp   = 0;
            bot.head  = (short) (bot.clazz - 1);

            int defaultHairIcon;
            switch (bot.clazz) {
                case 1: defaultHairIcon = 1;  break;  // Võ sĩ
                case 2: defaultHairIcon = 24; break;  // Kiếm sĩ
                case 3: defaultHairIcon = 28; break;  // Đầu bếp
                case 4: defaultHairIcon = 32; break;  // Bác sĩ
                case 5: defaultHairIcon = 36; break;  // Xạ thủ
                default: defaultHairIcon = 1; break;
            }
            bot.hair = (short) defaultHairIcon;

            long totalPoints = (long) bot.level * 10;
            bot.pointAttribute = 0;
            bot.point1 = totalPoints / 5;
            bot.point2 = totalPoints / 5;
            bot.point3 = totalPoints / 5;
            bot.point4 = totalPoints / 5;
            bot.point5 = totalPoints - (bot.point1 * 4);

            double maxTier = MercenaryTemplate.getMaxAllowedTier(bot.level);
            double effectiveTier = Math.min(maxTier, entry.tier);
            int fruitId = MercenaryTemplate.getFruitIdForEntry(entry);
            int chosenThanTrangSet = Player.selectThanTrangSetIndex(bot.clazz, fruitId, bot.name);
            boolean hasHoanMy = (effectiveTier >= 2.0);
            boolean hasKhamDa = (effectiveTier >= 1.5);
            boolean hasKichAn = (effectiveTier >= 2.5);

            bot.setupBotEquip(bot.clazz, bot.level, effectiveTier, hasHoanMy, hasKhamDa, hasKichAn, chosenThanTrangSet);
            bot.setupBotSkills(bot.clazz, (short) Math.max(entry.skillLv, bot.level >= 80 ? 30 : (bot.level >= 50 ? 20 : 12)), effectiveTier, fruitId);
            bot.setupBotAppearance(bot.clazz, effectiveTier);

            bot.list_eff   = new CopyOnWriteArrayList<>();
            bot.list_quest = new ArrayList<>();
            bot.list_op_thongthao = new ArrayList<>();
            bot.type_pk    = owner.type_pk;

            bot.setupBalancedMercenaryStats();

            return bot;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isRestrictedPvpMap(Zone zone) {
        if (zone == null || zone.template == null) return false;

        int mapId = zone.template.id;

        // 1. Chặn lính đánh thuê ở các map phòng chờ:
        // - Map 1000: Phòng chờ PvP Siêu Hạng / Đơn
        // - Map 119:  Phòng chờ Đấu Trường Truy Nã
        // - Map 260:  Phòng chờ PvP Băng
        if (mapId == 1000 || mapId == 119 || mapId == 260 || zone.pvpBang != null) {
            return true;
        }

        // 2. Chặn lính đánh thuê ở các map đấu trường PvP đơn, truy nã, giao hữu, thách đấu:
        // - Tất cả map có gắn MapPvp (SieuHangFight, WantedFight, FriendlyFight, BetFight)
        // - Map 58: Đấu trường PvP Đơn / Siêu Hạng
        // - Map 122, 123: Đấu trường PvP
        // - Map 120 khi là sàn đấu PvP (MapPvp hoặc không thuộc trận PvP Bang)
        if (zone.map_vp != null) {
            return true;
        }
        if (mapId == 58 || mapId == 122 || mapId == 123) {
            return true;
        }
        if (mapId == 120 && zone.pvpBangMapFight == null) {
            return true;
        }

        return false;
    }

    /**
     * Kiểm tra map có phải loại phó bản đặc biệt mà bot cần được đồng bộ clan/type không.
     * Gồm: phó bản nhóm, PvP Băng (chiến đấu), Trận Chiến Khổng Lồ.
     */
    private static boolean isSpecialDungeonMap(Zone zone) {
        if (zone == null) return false;
        return zone.pvpBangMapFight != null
            || zone.map_little_garden != null
            || zone.map_dungeon != null;
    }

    public void mercTick() {
        if (isdie) {
            handleDeath();
            return;
        }

        long now = System.currentTimeMillis();

        // 1. Chat ngẫu nhiên theo phe
        if (now >= nextChatTime && map != null) {
            nextChatTime = now + 15_000L + ZUtil.random(10000);
            int faction = (entry != null && entry.faction >= 0 && entry.faction < FACTION_CHAT.length) ? entry.faction : 0;
            String[] chats = FACTION_CHAT[faction];
            if (chats != null && chats.length > 0) {
                try {
                    map.send_chat_popup(0, index_map, chats[ZUtil.random(chats.length)]);
                } catch (Throwable ignored) {}
            }
        }

        // 2. Tìm chủ thuê (Dùng getOwner() an toàn)
        Player owner = getOwner();
        if (owner == null || owner.conn == null || owner.isClosed || owner.isSpectator) {
            leave();
            return;
        }

        // 2.1. Nếu ở trạng thái Về nhà / Ẩn hoặc chủ nhân đặt STATUS_HOME -> Rời map và dừng tick
        if (this.mercStatus == STATUS_HOME || MercenaryManager.gI().getPlayerMercStatus(owner) == STATUS_HOME) {
            if (this.map != null) {
                leave();
            }
            return;
        }

        // 2.3. Tự động thích ứng cấp độ và sức mạnh theo tiến trình của Chủ Nhân (Adaptive Progression)
        checkAdaptiveProgression(owner);

        // 2.5. Lắng nghe khẩu lệnh chat từ chủ thuê (Follow / Attack / Heal / Fruit Awakening)
        checkOwnerCommands(owner);

        // 3. Ẩn/rời map nếu chủ nhân vào map PvP 1v1 / Thách đấu / Truy nã / Phòng chờ PvP Băng
        if (owner.map != null && isRestrictedPvpMap(owner.map)) {
            leave();
            return;
        }

        // 4. Đổi map theo chủ (Trận chiến khổng lồ, Phó bản nhóm, Map chiến đấu PvP Băng, Map thường)
        if (owner.map != null && (map == null || !owner.map.equals(map))) {
            followOwnerToMap(owner);
            return;
        }

        // 5. Đồng bộ liên tục clan/type/map_boss_info từ chủ nhân (đặc biệt quan trọng trong phó bản nhóm/bang)
        syncOwnerIdentity(owner);

        // 5.1. Dịch chuyển tức thời lính về cạnh chủ nếu khoảng cách quá xa (> 450px)
        double distToOwner = Math.hypot(owner.x - x, owner.y - y);
        if (distToOwner > 450) {
            teleportToOwner(owner);
            return;
        }

        // 6. Xử lý hành vi theo trạng thái:
        if (this.mercStatus == STATUS_FOLLOW) {
            // Trạng thái Đi Theo: Chỉ bám sát chủ nhân, không tấn công
            if (distToOwner > 85) {
                short targetX = (short) (owner.x + getOffsetX(owner));
                short targetY = (short) (owner.y + getOffsetY(owner));
                int followSpeed = 55 + Math.abs(IDPlayer % 25);
                moveTowardsTarget(targetX, targetY, followSpeed);
            } else {
                if (now >= nextIdleMoveTime) {
                    nextIdleMoveTime = now + 2500L + ZUtil.random(4000);
                    idleTargetX = (short) (owner.x + getSlotBaseOffsetX(owner) + ZUtil.random(-25, 25));
                    idleTargetY = (short) (owner.y + getSlotBaseOffsetY(owner) + ZUtil.random(-20, 20));
                }
                if (idleTargetX != 0 && idleTargetY != 0) {
                    double distToIdleTarget = Math.hypot(idleTargetX - x, idleTargetY - y);
                    if (distToIdleTarget > 15 && distToOwner <= 90) {
                        moveTowardsTarget(idleTargetX, idleTargetY, 35 + ZUtil.random(15));
                    }
                }
            }
            return;
        }

        if (this.mercStatus == STATUS_PROTECT) {
            // Trạng thái Bảo Vệ: Chỉ tấn công kẻ địch trong phạm vi 150px của chủ nhân hoặc kẻ đánh chủ
            Player threatP = null;
            if (owner.lastAttacker != null && !owner.lastAttacker.isdie && owner.lastAttacker.map != null
                    && owner.lastAttacker.map.equals(map) && canAttackPlayer(owner, owner.lastAttacker)) {
                double d = Math.hypot(owner.lastAttacker.x - owner.x, owner.lastAttacker.y - owner.y);
                if (d <= 200) threatP = owner.lastAttacker;
            }
            if (threatP == null) {
                threatP = findEnemyPlayerNearOwner(owner, 150);
            }
            if (threatP != null) {
                short tX = (short) (threatP.x + getOffsetX(owner));
                short tY = (short) (threatP.y + getOffsetY(owner));
                double dToEnemy = Math.hypot(tX - x, tY - y);
                if (dToEnemy > 40) {
                    moveTowardsTarget(tX, tY, 70);
                }
                if (dToEnemy <= 250) {
                    if (now >= nextAttackTime) {
                        long attackDelay = attackPlayer(owner, threatP);
                        nextAttackTime = now + attackDelay;
                    }
                }
                return;
            }

            Mob threatMob = findMobNearOwner(owner, 150);
            if (threatMob != null) {
                short tX = threatMob.x;
                short tY = threatMob.y;
                double dToMob = Math.hypot(tX - x, tY - y);
                if (dToMob > 40) {
                    moveTowardsTarget(tX, tY, 70);
                }
                if (dToMob <= 250) {
                    if (now >= nextAttackTime) {
                        long attackDelay = attackMob(owner, threatMob);
                        nextAttackTime = now + attackDelay;
                    }
                }
                return;
            }

            // Không có mối đe dọa gần chủ -> Đi theo chủ
            if (distToOwner > 85) {
                short targetX = (short) (owner.x + getOffsetX(owner));
                short targetY = (short) (owner.y + getOffsetY(owner));
                int followSpeed = 55 + Math.abs(IDPlayer % 25);
                moveTowardsTarget(targetX, targetY, followSpeed);
            } else {
                if (now >= nextIdleMoveTime) {
                    nextIdleMoveTime = now + 2500L + ZUtil.random(4000);
                    idleTargetX = (short) (owner.x + getSlotBaseOffsetX(owner) + ZUtil.random(-25, 25));
                    idleTargetY = (short) (owner.y + getSlotBaseOffsetY(owner) + ZUtil.random(-20, 20));
                }
                if (idleTargetX != 0 && idleTargetY != 0) {
                    double distToIdleTarget = Math.hypot(idleTargetX - x, idleTargetY - y);
                    if (distToIdleTarget > 15 && distToOwner <= 90) {
                        moveTowardsTarget(idleTargetX, idleTargetY, 35 + ZUtil.random(15));
                    }
                }
            }
            return;
        }

        // Trạng thái Tấn Công (STATUS_ATTACK): Hỗ trợ tấn công quái bảo vệ chủ
        // Leash Guard: Nếu lính cách Chủ Nhân > 380px, hủy truy đuổi quái xa và lập tức chạy về theo chủ
        if (distToOwner > 380) {
            short targetX = (short) (owner.x + getOffsetX(owner));
            short targetY = (short) (owner.y + getOffsetY(owner));
            moveTowardsTarget(targetX, targetY, 70);
            return;
        }

        // 6. Ưu tiên 1: Tấn công người chơi / bot thù địch có thể tấn công trong phạm vi
        Player enemyPlayer = findAttackablePlayer(owner);
        if (enemyPlayer != null) {
            short tX = (short) (enemyPlayer.x + getOffsetX(owner));
            short tY = (short) (enemyPlayer.y + getOffsetY(owner));
            double dToEnemy = Math.hypot(tX - x, tY - y);
            if (dToEnemy > 40) {
                moveTowardsTarget(tX, tY, 70);
            }
            if (dToEnemy <= 250) {
                if (now >= nextAttackTime) {
                    long attackDelay = attackPlayer(owner, enemyPlayer);
                    nextAttackTime = now + attackDelay;
                }
            }
            return;
        }

        // 7. Ưu tiên 2: Tấn công quái / trùm / boss trong bán kính bảo vệ quanh chủ nhân
        Mob target = findFullMapTarget(owner);

        if (target != null) {
            short tX = target.x;
            short tY = target.y;
            double dToMob = Math.hypot(tX - x, tY - y);
            if (dToMob > 40) {
                moveTowardsTarget(tX, tY, 70);
            }
            if (dToMob <= 250) {
                if (now >= nextAttackTime) {
                    long attackDelay = attackMob(owner, target);
                    nextAttackTime = now + attackDelay;
                }
            }
            return;
        }

        // 8. Khi bản đồ đã sạch bóng quái / boss / kẻ địch: đi theo chủ thuê
        if (distToOwner > 85) {
            short targetX = (short) (owner.x + getOffsetX(owner));
            short targetY = (short) (owner.y + getOffsetY(owner));
            int followSpeed = 55 + Math.abs(IDPlayer % 25);
            moveTowardsTarget(targetX, targetY, followSpeed);
        } else {
            // Chủ nhân ở gần: đứng yên tự nhiên, thỉnh thoảng nhích nhẹ ngẫu nhiên
            if (now >= nextIdleMoveTime) {
                nextIdleMoveTime = now + 2500L + ZUtil.random(4000);
                idleTargetX = (short) (owner.x + getSlotBaseOffsetX(owner) + ZUtil.random(-25, 25));
                idleTargetY = (short) (owner.y + getSlotBaseOffsetY(owner) + ZUtil.random(-20, 20));
            }

            if (idleTargetX != 0 && idleTargetY != 0) {
                double distToIdleTarget = Math.hypot(idleTargetX - x, idleTargetY - y);
                if (distToIdleTarget > 15 && distToOwner <= 90) {
                    moveTowardsTarget(idleTargetX, idleTargetY, 35 + ZUtil.random(15));
                }
            }
        }
    }

    public int getBotSlotIndex(Player owner) {
        if (owner != null) {
            List<MercenaryBot> activeBots = MercenaryManager.gI().getActiveBots(owner);
            int idx = activeBots.indexOf(this);
            if (idx >= 0) return idx;
        }
        return Math.abs(this.IDPlayer) % 3;
    }

    public int getSlotBaseOffsetX(Player owner) {
        int slot = getBotSlotIndex(owner);
        switch (slot) {
            case 0: return -40; // Lính 1: Bên trái (-40px)
            case 1: return 40;  // Lính 2: Bên phải (+40px)
            case 2: return 0;   // Lính 3: Phía trước / dưới (0px)
            default: return (slot % 2 == 0) ? -45 : 45;
        }
    }

    public int getSlotBaseOffsetY(Player owner) {
        int slot = getBotSlotIndex(owner);
        switch (slot) {
            case 0: return -15; // Lính 1: Phía trên lệch trái
            case 1: return -15; // Lính 2: Phía trên lệch phải
            case 2: return 30;  // Lính 3: Phía dưới
            default: return 25;
        }
    }

    public int getOffsetX(Player owner) {
        return getSlotBaseOffsetX(owner) + (IDPlayer % 15 - 7);
    }

    public int getOffsetY(Player owner) {
        return getSlotBaseOffsetY(owner) + (IDPlayer % 11 - 5);
    }

    public int getOffsetX() {
        Player owner = SessionManager.PLAYERS_MAP.get(ownerPlayerId);
        return getOffsetX(owner);
    }

    public int getOffsetY() {
        Player owner = SessionManager.PLAYERS_MAP.get(ownerPlayerId);
        return getOffsetY(owner);
    }

    private byte lastClanRole = -1;

    /**
     * Đồng bộ clan, typePirate, type_pk, map_boss_info từ chủ nhân về bot.
     * Dùng cả trong tick (liên tục) lẫn khi follow map mới.
     */
    public void syncOwnerIdentity(Player owner) {
        if (owner == null) return;
        byte oldPk = this.type_pk;
        int oldPirate = this.typePirate;
        Clan oldClan = this.clan;
        byte currentRole = (owner.clan != null) ? (byte) owner.clan.getTypeMem(owner) : (byte) 10;
        byte oldRole = this.lastClanRole;

        this.clan = owner.clan;
        this.typePirate = owner.typePirate;
        if (this.map != null && (this.map.map_little_garden != null || this.map.template.id == 81)) {
            if (this.map.map_little_garden != null) {
                boolean isClan1 = (owner.clan != null && owner.clan.equals(this.map.map_little_garden.clan1));
                this.type_pk = (byte) (isClan1 ? 4 : 5);
            } else {
                this.type_pk = owner.type_pk;
            }
        } else if (this.map != null && (this.map.map_dungeon instanceof ChiemDao || (this.map.template != null && ((this.map.template.id >= 254 && this.map.template.id <= 258) || (this.map.template.id >= 261 && this.map.template.id <= 265))))) {
            Clan topClan = ChiemDao.getClanTop(this.map.template != null ? this.map.template.id : 0);
            if (this.map.map_dungeon instanceof ChiemDao && ((ChiemDao) this.map.map_dungeon).occupyingClan != null) {
                topClan = ((ChiemDao) this.map.map_dungeon).occupyingClan;
            }
            boolean isDefender = (owner.type_pk == 5)
                    || (owner.clan != null && topClan != null && (owner.clan.id == topClan.id || (owner.clan.name != null && owner.clan.name.equalsIgnoreCase(topClan.name))));
            this.type_pk = (byte) (isDefender ? 5 : 4);
        } else {
            this.type_pk = owner.type_pk;
        }
        this.map_boss_info = owner.map_boss_info;
        this.lastClanRole = currentRole;

        boolean pkChanged = (oldPk != this.type_pk || oldPirate != this.typePirate);
        boolean clanChanged = (oldClan != this.clan || oldRole != currentRole);

        if ((pkChanged || clanChanged) && map != null) {
            try {
                map.change_flag(this, this.type_pk);
                for (int i = 0; i < map.players.size(); i++) {
                    Player p0 = map.players.get(i);
                    if (p0 != null) {
                        if (pkChanged && p0.getService() != null) {
                            p0.getService().update_PK(this, false);
                        }
                        if (clanChanged) {
                            if (this.clan != null) {
                                Clan.send_me_to_other(this, p0, false);
                            } else {
                                Message m = new Message(-52);
                                m.writer().writeByte(10);
                                m.writer().writeShort(this.index_map);
                                p0.addmsg(m);
                                m.cleanup();
                            }
                        }
                    }
                }
            } catch (Throwable ignored) {}
        }
    }

    @Override
    public void updateBotTypePk() {
        Player owner = getOwner();
        if (owner != null) {
            if (this.map != null && (this.map.map_little_garden != null || this.map.template.id == 81)) {
                if (this.map.map_little_garden != null) {
                    boolean isClan1 = (owner.clan != null && owner.clan.equals(this.map.map_little_garden.clan1));
                    this.type_pk = (byte) (isClan1 ? 4 : 5);
                } else {
                    this.type_pk = owner.type_pk;
                }
            } else if (this.map != null && (this.map.map_dungeon instanceof ChiemDao || (this.map.template != null && ((this.map.template.id >= 254 && this.map.template.id <= 258) || (this.map.template.id >= 261 && this.map.template.id <= 265))))) {
                Clan topClan = ChiemDao.getClanTop(this.map.template != null ? this.map.template.id : 0);
                if (this.map.map_dungeon instanceof ChiemDao && ((ChiemDao) this.map.map_dungeon).occupyingClan != null) {
                    topClan = ((ChiemDao) this.map.map_dungeon).occupyingClan;
                }
                boolean isDefender = (owner.type_pk == 5)
                        || (owner.clan != null && topClan != null && (owner.clan.id == topClan.id || (owner.clan.name != null && owner.clan.name.equalsIgnoreCase(topClan.name))));
                this.type_pk = (byte) (isDefender ? 5 : 4);
            } else {
                this.type_pk = owner.type_pk;
            }
            this.typePirate = owner.typePirate;
            this.clan = owner.clan;
        } else {
            this.type_pk = -1;
        }
    }


    /**
     * Tìm đối thủ (Player) có thể tấn công được trong map dựa trên chế độ PK, trạng thái đồ sát, phe phái, v.v.
     */
    private Player findAttackablePlayer(Player owner) {
        if (map == null || Zone.isMapLang(map.template.id) || !map.can_PK) return null;

        // 1. Ưu tiên 1: Kẻ địch đang tấn công chủ nhân
        if (owner.lastAttacker != null) {
            Player attacker = owner.lastAttacker;
            if (attacker != null && !attacker.isdie && attacker.map != null
                    && attacker.map.equals(map) && attacker.IDPlayer != owner.IDPlayer
                    && canAttackPlayer(owner, attacker)) {
                return attacker;
            }
        }

        // 2. Ưu tiên 2: Người chơi thù địch / có trạng thái PK có thể tấn công trong map
        Player nearest = null;
        double minDist = Double.MAX_VALUE;

        if (map.players != null && !map.players.isEmpty()) {
            for (Player p : map.players) {
                if (p == null || p.isdie || p.map == null || !p.map.equals(map)) continue;
                if (p.IDPlayer == owner.IDPlayer || p.IDPlayer == this.IDPlayer) continue;
                if (p instanceof MercenaryBot) {
                    MercenaryBot merc = (MercenaryBot) p;
                    if (merc.ownerPlayerId == owner.IDPlayer) continue; // Bỏ qua lính thuộc cùng chủ
                }
                if (canAttackPlayer(owner, p)) {
                    double d = Math.hypot(p.x - x, p.y - y);
                    if (d < minDist) {
                        minDist = d;
                        nearest = p;
                    }
                }
            }
        }

        return nearest;
    }

    public Mob findMobNearOwner(Player owner, int radius) {
        if (map == null || owner == null) return null;
        Mob nearest = null;
        double minDist = Double.MAX_VALUE;

        if (map.map_ThuThachVeThan != null && map.map_ThuThachVeThan.mobs != null) {
            for (Mob mob : map.map_ThuThachVeThan.mobs) {
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                double d = Math.hypot(mob.x - owner.x, mob.y - owner.y);
                if (d <= radius && d < minDist) {
                    minDist = d;
                    nearest = mob;
                }
            }
        }
        if (nearest == null && map.mobs != null) {
            synchronized (map.mobs) {
                for (Mob mob : map.mobs.values()) {
                    if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                    double d = Math.hypot(mob.x - owner.x, mob.y - owner.y);
                    if (d <= radius && d < minDist) {
                        minDist = d;
                        nearest = mob;
                    }
                }
            }
        }
        if (nearest == null && map.list_mob != null) {
            for (int idx : map.list_mob) {
                Mob mob = map.getMob(idx);
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                double d = Math.hypot(mob.x - owner.x, mob.y - owner.y);
                if (d <= radius && d < minDist) {
                    minDist = d;
                    nearest = mob;
                }
            }
        }
        return nearest;
    }

    public Player findEnemyPlayerNearOwner(Player owner, int radius) {
        if (map == null || owner == null || !map.can_PK || map.players == null) return null;
        for (Player p : map.players) {
            if (p == null || p.isdie || p.equals(this) || p.equals(owner)) continue;
            if (p instanceof MercenaryBot && ((MercenaryBot) p).ownerPlayerId == owner.IDPlayer) continue;
            if (canAttackPlayer(owner, p)) {
                double d = Math.hypot(p.x - owner.x, p.y - owner.y);
                if (d <= radius) return p;
            }
        }
        return null;
    }

    /**
     * Kiểm tra xem mục tiêu Mob (quái vật, trùm, trụ) có thể tấn công được không.
     * Tự động nhận diện trụ phe mình trong Chiếm Đảo, Bảo Vệ Pháo Đài để KHÔNG đánh.
     */
    public boolean canAttackMob(Player owner, Mob mob) {
        if (mob == null || mob.isdie || mob.hp <= 0) return false;
        if (owner == null) return false;
        Zone zone = this.map != null ? this.map : mob.map;
        if (zone == null) return false;

        // 1. Kiểm tra Bản Đồ Chiếm Đảo Bang Hội (Zone 254..258, 261..265, map_dungeon instanceof ChiemDao)
        boolean isChiemDaoMap = (zone.map_dungeon instanceof ChiemDao)
                || (zone.template != null && ((zone.template.id >= 254 && zone.template.id <= 258) || (zone.template.id >= 261 && zone.template.id <= 265)));

        if (isChiemDaoMap && mob.mtemplate != null) {
            int mobId = mob.mtemplate.mob_id;
            // Nếu mob là Trụ Phụ (132) hoặc Trụ Chính (133)
            if (mobId == 132 || mobId == 133) {
                Clan topClan = ChiemDao.getClanTop(zone.template != null ? zone.template.id : 0);
                if (zone.map_dungeon instanceof ChiemDao && ((ChiemDao) zone.map_dungeon).occupyingClan != null) {
                    topClan = ((ChiemDao) zone.map_dungeon).occupyingClan;
                }

                // Xác định xem Chủ thuê / Bot có thuộc Phe Thủ (Phe Chiếm Giữ / Trụ phe mình) không
                boolean isDefender = (owner.type_pk == 5 || this.type_pk == 5)
                        || (owner.clan != null && topClan != null && (owner.clan.id == topClan.id || (owner.clan.name != null && owner.clan.name.equalsIgnoreCase(topClan.name))));

                // Nếu là Phe Thủ: Trụ là trụ phe mình -> TUYỆT ĐỐI KHÔNG ĐÁNH TRỤ!
                if (isDefender) {
                    return false;
                }

                // Nếu người chơi không có Bang Hội: Không thể gây dame lên trụ Chiếm Đảo -> Không đánh
                if (owner.clan == null) {
                    return false;
                }

                // Nếu là Phe Công (Phe Tấn Công):
                // - Nếu là Trụ Chính (133): Chỉ được phép đánh khi TẤT CẢ Trụ Phụ (132) đã bị tiêu diệt
                if (mobId == 133) {
                    int aliveSubTurrets = ChiemDao.getAliveSubTurretCount(zone);
                    if (aliveSubTurrets > 0) {
                        return false; // Còn Trụ Phụ -> Chưa được đánh Trụ Chính
                    }
                }
                // - Nếu là Trụ Phụ (132): Được phép đánh
                return true;
            }
        }

        // 2. Kiểm tra Bản Đồ Bảo Vệ Pháo Đài (Zone 267..271, baoVePhaoDai != null)
        if (zone.IsMapBaoVePhaoDai() || zone.baoVePhaoDai != null || (zone.template != null && zone.template.id >= 267 && zone.template.id <= 271)) {
            if (zone.baoVePhaoDai != null) {
                boolean isTeamA = (owner.type_pk == 4 || this.type_pk == 4)
                        || (owner.clan != null && zone.baoVePhaoDai.clanA != null && owner.clan.equals(zone.baoVePhaoDai.clanA));
                boolean isTeamB = (owner.type_pk == 5 || this.type_pk == 5)
                        || (owner.clan != null && zone.baoVePhaoDai.clanB != null && owner.clan.equals(zone.baoVePhaoDai.clanB));

                // Trụ của Team A
                if (mob == zone.baoVePhaoDai.truThuongATren || mob == zone.baoVePhaoDai.truThuongADuoi || mob == zone.baoVePhaoDai.truChinhA) {
                    if (isTeamA) return false; // Trụ phe mình -> Không đánh
                }
                // Trụ của Team B
                if (mob == zone.baoVePhaoDai.truThuongBTren || mob == zone.baoVePhaoDai.truThuongBDuoi || mob == zone.baoVePhaoDai.truChinhB) {
                    if (isTeamB) return false; // Trụ phe mình -> Không đánh
                }
            }
        }

        return true;
    }

    private boolean canAttackPlayer(Player owner, Player target) {
        if (target == null || target.isdie || target.map == null) return false;
        if (target.IDPlayer == owner.IDPlayer || target.IDPlayer == this.IDPlayer) return false;

        // Bỏ qua lính thuộc cùng chủ thuê
        if (target instanceof MercenaryBot) {
            MercenaryBot merc = (MercenaryBot) target;
            if (merc.ownerPlayerId == owner.IDPlayer) return false;
        }

        // Bỏ qua đệ tử thuộc cùng chủ thuê
        if (target instanceof model.DeTu) {
            model.DeTu dt = (model.DeTu) target;
            if (dt.master != null && dt.master.IDPlayer == owner.IDPlayer) return false;
        }

        // Trong map PvP (Lôi Đài, Giao Hữu, Siêu Hạng...): Tuân thủ 100% canAttackTargetPlayer của chủ nhân
        if (this.map != null && this.map.map_vp != null) {
            return owner.canAttackTargetPlayer(target);
        }

        // Trong Phó Bản Khổng Lồ (Little Garden Map 81): Tuân thủ 100% canAttackTargetPlayer của chủ nhân
        if (this.map != null && (this.map.map_little_garden != null || this.map.template.id == 81)) {
            return owner.canAttackTargetPlayer(target);
        }

        // Trong Bảo Vệ Pháo Đài (Map 267..271): Tuân thủ 100% canAttackTargetPlayer của chủ nhân
        if (this.map != null && (this.map.baoVePhaoDai != null || this.map.IsMapBaoVePhaoDai())) {
            return owner.canAttackTargetPlayer(target);
        }

        // Trong PvP Băng (Map 120 / pvpBangMapFight): Tuân thủ 100% canAttackTargetPlayer của chủ nhân
        if (this.map != null && (this.map.pvpBangMapFight != null || this.map.template.id == 120)) {
            return owner.canAttackTargetPlayer(target);
        }

        // Trong Chiếm Đảo Bang Hội (Map 254..258, 261..265, map_dungeon instanceof ChiemDao): Tuân thủ 100% canAttackTargetPlayer của chủ nhân
        if (this.map != null && (this.map.map_dungeon instanceof ChiemDao || (this.map.template != null && ((this.map.template.id >= 254 && this.map.template.id <= 258) || (this.map.template.id >= 261 && this.map.template.id <= 265))))) {
            return owner.canAttackTargetPlayer(target);
        }

        // Trong Trận Chiến Lớn (type_pk 11..13): Khác phe là TẤN CÔNG NGAY, cùng phe không đánh
        if (this.type_pk >= 11 && this.type_pk <= 13 && target.type_pk >= 11 && target.type_pk <= 13) {
            return this.type_pk != target.type_pk;
        }

        // Trong Chiếm Đảo / Phó Bản / Đấu Trường (type_pk 4..11): Cùng cờ KHÔNG đánh, khác cờ ĐÁNH
        if (this.type_pk >= 4 && this.type_pk <= 11 && target.type_pk >= 4 && target.type_pk <= 11) {
            return this.type_pk != target.type_pk;
        }

        // Cùng Clan ngoài các hoạt động trên thì không đánh
        if (owner.clan != null && target.clan != null && owner.clan.equals(target.clan)) {
            if (owner.type_pk != 14 && owner.type_pk != 15 && target.type_pk != 14 && target.type_pk != 15) {
                return false;
            }
        }

        byte p1_pk = this.type_pk;
        byte p2_pk = target.type_pk;
        int p1_pirate = this.typePirate;
        int p2_pirate = target.typePirate;

        return ((p1_pirate == 0 && p2_pirate == 2)
                || (p1_pirate == 2 && p2_pirate == 0)
                || (p1_pirate == 1 && p2_pirate == 2)
                || (p1_pirate == 2 && p2_pirate == 1)
                || (p1_pk == 14 && p2_pk == 15)
                || (p1_pk == 15 && p2_pk == 14)
                || (p1_pk >= 11 && p1_pk <= 13 && p2_pk >= 11 && p2_pk <= 13 && p1_pk != p2_pk)
                || (p1_pirate == 2 && p2_pirate == 2)
                || (p1_pk == 0)
                || (p2_pk == 1) // Target bật đồ sát
                || (p1_pk == 1) // Bot/Owner bật đồ sát
                || (p1_pk == 3 && p2_pk == 3)
                || (p2_pk == 0)
                || (p1_pk == 3 && p2_pk >= 4 && p2_pk <= 8)
                || (p2_pk == 3 && p1_pk >= 4 && p1_pk <= 8)
                || (p1_pk >= 4 && p1_pk <= 8 && p2_pk >= 4 && p2_pk <= 8 && p1_pk != p2_pk));
    }

    @Override
    public boolean canAttackTargetPlayer(Player p) {
        Player owner = getOwner();
        if (owner == null) return false;
        return canAttackPlayer(owner, p);
    }

    public Skill_info getBasicSkill() {
        if (this.skill_point != null) {
            int baseClassIdx = (this.clazz > 0 ? this.clazz - 1 : 0) * 60;
            for (Skill_info sk : this.skill_point) {
                if (sk != null && sk.temp != null) {
                    if (sk.temp.ID == 0 || sk.temp.indexSkillInServer == baseClassIdx) {
                        return sk;
                    }
                }
            }
        }
        return null;
    }

    public Skill_info selectSkill() {
        boolean isSea = (this.map != null && (this.map.template.specMap == 4 || this.map.isMapSea()));
        if (isSea) {
            Skill_info skSea = get_skill_temp(3);
            if (skSea != null) return skSea;
        }

        if (this.skill_point == null || this.skill_point.isEmpty()) {
            setupBotSkills(this.clazz, (short) (this.level > 0 ? this.level : 50), (this.entry != null ? this.entry.tier : 1.0));
        }
        if (this.skill_point != null && !this.skill_point.isEmpty()) {
            long nowNano = System.nanoTime();
            List<Skill_info> readySpecialSkills = new ArrayList<>();
            Skill_info basicSkill = null;

            for (Skill_info sk : this.skill_point) {
                if (sk != null && sk.temp != null) {
                    // Chặn skill 3 (pháo thuyền) ở map đất liền
                    if (sk.temp.ID == 3 && !isSea) continue;

                    boolean isActive = sk.temp.typeSkill != 2 && sk.temp.typeSkill != 3;
                    if (!isActive) continue;

                    int skId = sk.temp.ID;
                    int indexInServer = sk.temp.indexSkillInServer;
                    int baseClassIdx = (this.clazz > 0 ? this.clazz - 1 : 0) * 60;
                    boolean isBasic = (skId == 0 || indexInServer == baseClassIdx);

                    if (isBasic) {
                        if (basicSkill == null) basicSkill = sk;
                    } else {
                        if (!this.time_use_skill.containsKey(skId) || this.time_use_skill.get(skId) <= nowNano) {
                            readySpecialSkills.add(sk);
                        }
                    }
                }
            }

            if (!readySpecialSkills.isEmpty()) {
                return readySpecialSkills.get(ZUtil.random(readySpecialSkills.size()));
            } else if (basicSkill != null) {
                return basicSkill;
            } else {
                for (Skill_info sk : this.skill_point) {
                    if (sk != null && sk.temp != null && (sk.temp.ID != 3 || isSea) && sk.temp.typeSkill != 2 && sk.temp.typeSkill != 3) {
                        return sk;
                    }
                }
            }
        }
        return null;
    }

    public int selectSkillId() {
        Skill_info sk = selectSkill();
        return (sk != null && sk.temp != null) ? sk.temp.ID : 0;
    }

    private long attackPlayer(Player owner, Player target) {
        if (map == null || target == null || target.isdie || target.map == null || !target.map.equals(map)) return 650L;

        Skill_info skill = selectSkill();
        if (skill == null || skill.temp == null) return 650L;

        int idSkill = skill.temp.ID;
        long nowNano = System.nanoTime();
        long skillCdMs = calculateSkillCooldown(skill.temp);
        this.time_use_skill.put(idSkill, nowNano + skillCdMs * 1_000_000L);
        applySkillCooldown(idSkill, skill.temp);

        Skill_info basicSkill = getBasicSkill();
        long basicDelay = (basicSkill != null && basicSkill.temp != null) ? calculateSkillCooldown(basicSkill.temp) : skillCdMs;
        long attackDelay = (skill == basicSkill) ? skillCdMs : basicDelay;

        int baseDame = calculateDamage();
        // Cân bằng PvP: Lính chỉ đóng vai trò hỗ trợ quấy rối, giảm 70% sát thương và cap tối đa 1.000 input (~2k - 4.5k hit thực tế)
        int pvpDame = Math.max(30, Math.min(1000, baseDame / 3));

        try {
            if (target.hp > 0 && !target.isdie) {
                List<Player> targetList = new ArrayList<>();
                targetList.add(target);
                if (map.players != null && !map.players.isEmpty()) {
                    for (Player nearbyP : map.players) {
                        if (nearbyP != null && !nearbyP.isdie && nearbyP != target && canAttackPlayer(owner, nearbyP)) {
                            double d = Math.hypot(nearbyP.x - target.x, nearbyP.y - target.y);
                            if (d <= 80 && targetList.size() < 4) {
                                targetList.add(nearbyP);
                            }
                        }
                    }
                }
                map.Fire_Player(targetList.toArray(new Player[0]), this, idSkill, pvpDame);
            }
        } catch (Throwable ignored) {}
        return attackDelay;
    }

    private void handleDeath() {
        long now = System.currentTimeMillis();
        if (respawnTimer == 0) {
            respawnTimer = now + 45_000L; // Auto-hồi sinh sau 45 giây (cân bằng, tránh bất tử lính)
            // Logic chết: Xóa xác ngay lập tức khỏi bản đồ
            try {
                leave();
            } catch (Throwable ignored) {}
        }
        if (now >= respawnTimer) {
            Player owner = network.SessionManager.PLAYERS_MAP.get(ownerPlayerId);
            if (owner != null && owner.map != null && !owner.isSpectator && owner.conn != null) {
                if (this.mercStatus == STATUS_HOME || MercenaryManager.gI().getPlayerMercStatus(owner) == STATUS_HOME) {
                    return;
                }
                // 1. Hồi phục 100% HP và MP theo chỉ số cân bằng
                isdie = false;
                setupBalancedMercenaryStats();
                x = (short) (owner.x + getOffsetX(owner));
                y = (short) (owner.y + getOffsetY(owner));
                respawnTimer = 0;

                // 2. Xuất hiện tại vị trí của chủ nhân 100% HP
                try {
                    syncOwnerIdentity(owner);
                    lastJoinTime = System.currentTimeMillis();
                    join(owner.map, x, y);
                    if (map != null) {
                        map.send_chat_popup(0, index_map, "Vẫn chưa xong đâu! Ta trở lại đây!");
                    }
                } catch (Throwable ignored) {}
            }
        }
    }

    public void teleportToOwner(Player owner) {
        if (owner == null || owner.map == null) return;
        if (isRestrictedPvpMap(owner.map)) return;
        if (this.mercStatus == STATUS_HOME || MercenaryManager.gI().getPlayerMercStatus(owner) == STATUS_HOME) {
            leave();
            return;
        }
        try {
            if (this.map == null || !this.map.equals(owner.map)) {
                followOwnerToMap(owner);
            } else {
                this.x = (short) (owner.x + getOffsetX(owner));
                this.y = (short) (owner.y + getOffsetY(owner));
                if (this.map != null) {
                    this.map.getService().move((byte) 0, index_map, this.x, this.y);
                }
            }
        } catch (Throwable ignored) {}
    }

    private void followOwnerToMap(Player owner) {
        // Guard 1: Manager đang chủ động di chuyển bot — tick không được chen vào
        if (isChangingMap) return;

        // Guard 1.5: Bot hoặc chủ nhân ở trạng thái Về Nhà -> Rời map và không join
        if (this.mercStatus == STATUS_HOME || (owner != null && MercenaryManager.gI().getPlayerMercStatus(owner) == STATUS_HOME)) {
            leave();
            return;
        }

        // Guard 2: Debounce — không join map 2 lần trong vòng 800ms
        long now = System.currentTimeMillis();
        if (now - lastJoinTime < 800) return;

        try {
            if (owner == null || owner.isSpectator || owner.map == null || isRestrictedPvpMap(owner.map)) {
                leave();
                return;
            }

            // Rời map cũ
            leave();

            // Đồng bộ danh tính chủ nhân TRƯỚC khi join map mới
            syncOwnerIdentity(owner);

            // Join map của chủ với vị trí lệch X, Y
            lastJoinTime = System.currentTimeMillis();
            join(owner.map, (short) (owner.x + getOffsetX(owner)), (short) (owner.y + getOffsetY(owner)));
        } catch (Throwable ignored) {}
    }

    private void moveTowardsTarget(short tx, short ty, int speed) {
        if (map == null) return;
        int minX = bot.SmartMovement.getSafeMinX(map);
        int maxX = bot.SmartMovement.getSafeMaxX(map);
        int minY = bot.SmartMovement.getSafeMinY(map);
        int maxY = bot.SmartMovement.getSafeMaxY(map);

        tx = (short) Math.max(minX, Math.min(maxX, tx));
        ty = (short) Math.max(minY, Math.min(maxY, ty));

        int dx = tx - x;
        int dy = ty - y;
        double dist = Math.hypot(dx, dy);
        if (dist < 5) return;

        int newX, newY;
        if (dist > speed) {
            newX = x + (int) (dx * speed / dist);
            newY = y + (int) (dy * speed / dist);
        } else {
            newX = tx;
            newY = ty;
        }

        newX = Math.max(minX, Math.min(newX, maxX));
        newY = Math.max(minY, Math.min(newY, maxY));

        x = (short) newX;
        y = (short) newY;

        try {
            map.getService().move((byte) 0, index_map, x, y);
        } catch (Throwable ignored) {}
    }

    private Mob findFullMapTarget(Player owner) {
        if (map == null || owner == null) return null;

        Mob nearest = null;
        double minDist = Double.MAX_VALUE;
        final double MAX_GUARD_RADIUS = 350.0;

        // 1. Boss Map Mobs / Bosses từ MapBossInfo
        List<Mob> bossInfoMobs = MapBossInfo.get_list_mob(map);
        if (bossInfoMobs != null && !bossInfoMobs.isEmpty()) {
            for (Mob mob : bossInfoMobs) {
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                if (Math.hypot(mob.x - owner.x, mob.y - owner.y) > MAX_GUARD_RADIUS) continue;
                double d = Math.hypot(mob.x - x, mob.y - y);
                if (d < minDist) {
                    minDist = d;
                    nearest = mob;
                }
            }
        }

        // 2. Boss từ SuperBossManager
        if (SuperBossManager.ENTRYS != null && !SuperBossManager.ENTRYS.isEmpty()) {
            for (AbsBoss b : SuperBossManager.ENTRYS) {
                if (b != null && !b.isdie && b.hp > 0 && b.map != null && b.map.template != null && map.template != null && b.map.template.id == map.template.id && canAttackMob(owner, b)) {
                    if (Math.hypot(b.x - owner.x, b.y - owner.y) > MAX_GUARD_RADIUS) continue;
                    double d = Math.hypot(b.x - x, b.y - y);
                    if (d < minDist) {
                        minDist = d;
                        nearest = b;
                    }
                }
            }
        }

        // 3. Trùm Đơn Độc Toàn Cầu / Event
        if (BossPica.mob != null && !BossPica.mob.isdie && BossPica.mob.hp > 0 && BossPica.mob.map != null && map.template != null && BossPica.mob.map.template.id == map.template.id && canAttackMob(owner, BossPica.mob)) {
            if (Math.hypot(BossPica.mob.x - owner.x, BossPica.mob.y - owner.y) <= MAX_GUARD_RADIUS) {
                double d = Math.hypot(BossPica.mob.x - x, BossPica.mob.y - y);
                if (d < minDist) { minDist = d; nearest = BossPica.mob; }
            }
        }
        if (BossTheGioi.mob != null && !BossTheGioi.mob.isdie && BossTheGioi.mob.hp > 0 && BossTheGioi.mob.map != null && map.template != null && BossTheGioi.mob.map.template.id == map.template.id && canAttackMob(owner, BossTheGioi.mob)) {
            if (Math.hypot(BossTheGioi.mob.x - owner.x, BossTheGioi.mob.y - owner.y) <= MAX_GUARD_RADIUS) {
                double d = Math.hypot(BossTheGioi.mob.x - x, BossTheGioi.mob.y - y);
                if (d < minDist) { minDist = d; nearest = BossTheGioi.mob; }
            }
        }
        if (LucciGioTo.mob != null && !LucciGioTo.mob.isdie && LucciGioTo.mob.hp > 0 && LucciGioTo.mob.map != null && map.template != null && LucciGioTo.mob.map.template.id == map.template.id && canAttackMob(owner, LucciGioTo.mob)) {
            if (Math.hypot(LucciGioTo.mob.x - owner.x, LucciGioTo.mob.y - owner.y) <= MAX_GUARD_RADIUS) {
                double d = Math.hypot(LucciGioTo.mob.x - x, LucciGioTo.mob.y - y);
                if (d < minDist) { minDist = d; nearest = LucciGioTo.mob; }
            }
        }

        // 4. Boss / Quái trong Map Săn Trùm, Mr3, Thử Thách Vệ Thần, Liên Tầng, Siêu Liên Tầng, Phó Bản
        if (map.mapSanTrum != null && map.mapSanTrum.mob != null && !map.mapSanTrum.mob.isdie && map.mapSanTrum.mob.hp > 0 && canAttackMob(owner, map.mapSanTrum.mob)) {
            if (Math.hypot(map.mapSanTrum.mob.x - owner.x, map.mapSanTrum.mob.y - owner.y) <= MAX_GUARD_RADIUS) {
                double d = Math.hypot(map.mapSanTrum.mob.x - x, map.mapSanTrum.mob.y - y);
                if (d < minDist) { minDist = d; nearest = map.mapSanTrum.mob; }
            }
        }

        if (map.map_Mr3 != null && map.map_Mr3.mobs != null) {
            for (Mob mob : map.map_Mr3.mobs) {
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                if (Math.hypot(mob.x - owner.x, mob.y - owner.y) > MAX_GUARD_RADIUS) continue;
                double d = Math.hypot(mob.x - x, mob.y - y);
                if (d < minDist) { minDist = d; nearest = mob; }
            }
        }

        if (map.map_ThuThachVeThan != null && map.map_ThuThachVeThan.mobs != null) {
            for (Mob mob : map.map_ThuThachVeThan.mobs) {
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                if (Math.hypot(mob.x - owner.x, mob.y - owner.y) > MAX_GUARD_RADIUS) continue;
                double d = Math.hypot(mob.x - x, mob.y - y);
                if (d < minDist) { minDist = d; nearest = mob; }
            }
        }

        if (map.map_LienTang != null && map.map_LienTang.mobs != null) {
            for (Mob mob : map.map_LienTang.mobs) {
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                if (Math.hypot(mob.x - owner.x, mob.y - owner.y) > MAX_GUARD_RADIUS) continue;
                double d = Math.hypot(mob.x - x, mob.y - y);
                if (d < minDist) { minDist = d; nearest = mob; }
            }
        }

        if (map.map_SieuLienTang != null && map.map_SieuLienTang.mobs != null) {
            for (Mob mob : map.map_SieuLienTang.mobs) {
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                if (Math.hypot(mob.x - owner.x, mob.y - owner.y) > MAX_GUARD_RADIUS) continue;
                double d = Math.hypot(mob.x - x, mob.y - y);
                if (d < minDist) { minDist = d; nearest = mob; }
            }
        }

        if (map.map_dungeon != null && map.map_dungeon.mobs != null) {
            for (Mob mob : map.map_dungeon.mobs) {
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                if (Math.hypot(mob.x - owner.x, mob.y - owner.y) > MAX_GUARD_RADIUS) continue;
                double d = Math.hypot(mob.x - x, mob.y - y);
                if (d < minDist) { minDist = d; nearest = mob; }
            }
        }

        if (map.map_DaoKhoBau != null && map.map_DaoKhoBau.mobs != null) {
            for (Mob mob : map.map_DaoKhoBau.mobs) {
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                if (Math.hypot(mob.x - owner.x, mob.y - owner.y) > MAX_GUARD_RADIUS) continue;
                double d = Math.hypot(mob.x - x, mob.y - y);
                if (d < minDist) { minDist = d; nearest = mob; }
            }
        }

        // 5. Quái trong Zone.mobs HashMap (Tất cả quái hiện có trong Zone active)
        if (map.mobs != null && !map.mobs.isEmpty()) {
            synchronized (map.mobs) {
                for (Mob mob : map.mobs.values()) {
                    if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                    if (Math.hypot(mob.x - owner.x, mob.y - owner.y) > MAX_GUARD_RADIUS) continue;
                    double d = Math.hypot(mob.x - x, mob.y - y);
                    if (d < minDist) {
                        minDist = d;
                        nearest = mob;
                    }
                }
            }
        }

        // 6. Map thường list_mob[]
        if (nearest == null && map.list_mob != null && map.list_mob.length > 0) {
            for (int idx : map.list_mob) {
                Mob mob = map.getMob(idx);
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(owner, mob)) continue;
                if (Math.hypot(mob.x - owner.x, mob.y - owner.y) > MAX_GUARD_RADIUS) continue;
                double d = Math.hypot(mob.x - x, mob.y - y);
                if (d < minDist) {
                    minDist = d;
                    nearest = mob;
                }
            }
        }

        return nearest;
    }

    private long attackMob(Player owner, Mob target) {
        if (map == null || target == null || target.isdie || !canAttackMob(owner, target)) return 650L;

        Skill_info skill = selectSkill();
        if (skill == null || skill.temp == null) return 650L;

        int idSkill = skill.temp.ID;
        long nowNano = System.nanoTime();
        long skillCdMs = calculateSkillCooldown(skill.temp);
        this.time_use_skill.put(idSkill, nowNano + skillCdMs * 1_000_000L);
        applySkillCooldown(idSkill, skill.temp);

        Skill_info basicSkill = getBasicSkill();
        long basicDelay = (basicSkill != null && basicSkill.temp != null) ? calculateSkillCooldown(basicSkill.temp) : skillCdMs;
        long attackDelay = (skill == basicSkill) ? skillCdMs : basicDelay;

        int dame = calculateDamage();
        if (dame <= 0) dame = 50;

        try {
            if (target.hp <= 0 || target.isdie) return attackDelay;

            // Tìm quái lân cận trong bán kính 60px để tung skill AOE (tối đa 2 mục tiêu thay vì 4)
            List<Mob> targetsList = new ArrayList<>();
            targetsList.add(target);
            if (map.mobs != null && !map.mobs.isEmpty()) {
                for (Mob nearby : map.mobs.values()) {
                    if (nearby != null && !nearby.isdie && nearby != target && canAttackMob(owner, nearby)) {
                        double d = Math.hypot(nearby.x - target.x, nearby.y - target.y);
                        if (d <= 60 && targetsList.size() < 2) {
                            targetsList.add(nearby);
                        }
                    }
                }
            }

            Mob[] targets = targetsList.toArray(new Mob[0]);
            map.Fire_Monster(targets, this, idSkill, dame);

            for (Mob m : targets) {
                if (m.isdie || m.hp <= 0) {
                    m.time_refresh = System.currentTimeMillis() + Mob.TIME_RESPAWN * 1000L;
                    if (owner != null && !owner.isdie && owner.map != null && owner.map.equals(map)) {
                        double distToOwner = Math.hypot(owner.x - x, owner.y - y);
                        if (distToOwner <= 400) {
                            int levelDiff = Math.abs(owner.level - m.level);
                            if (levelDiff <= 15) {
                                long baseMobExp = (long) (m.level * 80L);
                                long ownerExp = (long) (baseMobExp * 0.25); // Chia sẻ 25% EXP
                                if (levelDiff > 10) {
                                    ownerExp = ownerExp * 30 / 100; // Giảm 70% nếu chênh lệch > 10 cấp
                                }
                                if (ownerExp > 0) {
                                    try {
                                        owner.update_exp(ownerExp, false);
                                    } catch (Throwable ignored) {}
                                }
                            }
                        }
                    }
                }
            }

        } catch (Throwable ignored) {}
        return attackDelay;
    }

    private long lastAdaptiveCheck = 0;

    /**
     * Tự động thích ứng cấp độ, trang bị và kỹ năng theo sự tiến bộ của Chủ Nhân (Adaptive Progression).
     */
    private void checkAdaptiveProgression(Player owner) {
        if (owner == null) return;
        long now = System.currentTimeMillis();
        if (now - lastAdaptiveCheck < 10000) return; // Kiểm tra mỗi 10 giây
        lastAdaptiveCheck = now;

        // Cấp độ lính luôn bị khống chế không bao giờ vượt quá cấp độ của Chủ Nhân
        short targetLevel = (short) Math.max(1, Math.min(owner.level, Math.min(110, (this.entry != null ? this.entry.maxLv : owner.level))));
        if (this.level != targetLevel) {
            this.level = targetLevel;
            long totalPoints = (long) this.level * 10;
            this.pointAttribute = 0;
            this.point1 = totalPoints / 5;
            this.point2 = totalPoints / 5;
            this.point3 = totalPoints / 5;
            this.point4 = totalPoints / 5;
            this.point5 = totalPoints - (this.point1 * 4);

            double maxAllowedTier = MercenaryTemplate.getMaxAllowedTier(this.level);
            double effectiveTier = Math.min(maxAllowedTier, (this.entry != null) ? this.entry.tier : 1.0);
            if (this.currencyUsed == MercenaryTemplate.CURRENCY_EXTOL && effectiveTier < 3.5 && this.level >= 80) {
                effectiveTier = Math.min(maxAllowedTier, 3.5);
            }

            int fruitId = MercenaryTemplate.getFruitIdForEntry(this.entry);
            int chosenThanTrangSet = Player.selectThanTrangSetIndex(this.clazz, fruitId, this.name);
            boolean hasHoanMy = (effectiveTier >= 2.0);
            boolean hasKhamDa = (effectiveTier >= 1.5);
            boolean hasKichAn = (effectiveTier >= 2.5);

            setupBotEquip(this.clazz, this.level, effectiveTier, hasHoanMy, hasKhamDa, hasKichAn, chosenThanTrangSet);
            setupBotSkills(this.clazz, (short) Math.max((this.entry != null ? this.entry.skillLv : 10), this.level >= 80 ? 30 : (this.level >= 50 ? 20 : 12)), effectiveTier, fruitId);

            setupBalancedMercenaryStats();
        }
    }

    /**
     * Khởi tạo chỉ số chiến đấu chuẩn cân bằng cho Lính Đánh Thuê.
     * Vừa giữ nguyên toàn bộ trang bị "max all" (Thần Trang + Hoàn Mỹ + Đá Khảm)
     * vừa khống chế chỉ số HP, Def, Dame, Né không bị ảo quá mức.
     */
    public void setupBalancedMercenaryStats() {
        this.isBalancedStats = false;
        try { this.setin4(); } catch (Exception ignored) {}
        this.ability = new Ability(this);
        try { this.updateParts(); } catch (Exception ignored) {}

        int lv = Math.max(1, (int) this.level);
        double maxTier = MercenaryTemplate.getMaxAllowedTier(this.level);
        double effectiveTier = Math.min(maxTier, (this.entry != null) ? this.entry.tier : 1.0);
        if (this.currencyUsed == MercenaryTemplate.CURRENCY_EXTOL && effectiveTier < 3.5 && this.level >= 80) {
            effectiveTier = Math.min(maxTier, 3.5);
        }

        // 1. Cân bằng Máu tối đa (HP Max) theo Cấp độ và Tier (Không bị ảo hàng trăm nghìn)
        // Tier 4 (100-110): ~42,000 - 58,000 HP
        // Tier 3 (85-99):   ~25,000 - 36,000 HP
        // Tier 2 (55-84):   ~14,000 - 22,000 HP
        // Tier 1 (1-54):    ~5,000 - 12,000 HP
        long baseHp = 4500L + lv * 260L + (lv > 50 ? (lv - 50L) * 150L : 0L);
        double tierFactor = 0.65 + (effectiveTier * 0.15); // Tier 1: 0.80, Tier 2: 0.95, Tier 3: 1.10, Tier 4: 1.25
        double classHpBonus = (this.clazz == 1 ? 1.15 : (this.clazz == 3 ? 1.08 : (this.clazz == 4 ? 0.90 : (this.clazz == 5 ? 0.95 : 1.0))));
        this.hpMax = (int) Math.max(3000, Math.min(65000, baseHp * tierFactor * classHpBonus));
        this.hp = this.hpMax;

        // 2. Cân bằng Năng lượng (MP Max)
        this.mpMax = (int) Math.max(1000, 1500 + lv * 100);
        this.mp = this.mpMax;

        // 3. Cân bằng Giáp (Def) & Kháng
        long baseDef = 180L + lv * 11L + (long) (effectiveTier * 140L);
        double classDefBonus = (this.clazz == 1 ? 1.12 : (this.clazz == 2 ? 1.05 : 0.95));
        this.def = (int) Math.max(150, Math.min(2500, baseDef * classDefBonus));
        this.defPercent = (int) Math.min(25, 5 + effectiveTier * 4.5);

        // 4. Các chỉ số phụ chuẩn cân bằng
        this.miss = (int) Math.min(150, 40 + effectiveTier * 25);
        this.crit = (int) Math.min(450, 100 + effectiveTier * 70);
        this.pierce = (int) Math.min(400, 80 + effectiveTier * 65);
        this.agility = 320;
        this.resPhys = (int) Math.min(300, 50 + effectiveTier * 50);
        this.resMag = (int) Math.min(300, 50 + effectiveTier * 50);

        // 5. Cài đặt cờ cân bằng để Ability không bị nhân option chồng chéo
        this.isBalancedStats = true;
        this.isdie = false;
    }

    private int calculateDamage() {
        Player owner = getOwner();
        int ownerAtk = (owner != null && owner.ability != null) ? owner.ability.get_dame(true) : 0;

        double maxTier = MercenaryTemplate.getMaxAllowedTier(level);
        double tierSeed = Math.min(maxTier, (entry != null) ? entry.tier : 1.0);

        // Chuẩn hóa sát thương cơ sở để khi đi qua các bộ nhân của Fire_Monster (Skill, Crit, Final Dame)
        // sẽ đạt mức sát thương thực tế cân bằng (~12k - 25k ở Tier 4, ~7k - 14k ở Tier 3, ~3k - 6.5k ở Tier 2, ~1k - 2.5k ở Tier 1)
        int baseInput = (int) (150 + level * 18 + tierSeed * 280);
        if (ownerAtk > 0) {
            // Tỷ lệ hỗ trợ theo sức mạnh của chủ nhân (khoảng 3.8% input dame -> qua skill đạt ~35% DPS chủ)
            int scaledFromOwner = (int) (Math.min(ownerAtk, 90_000) * 0.038);
            baseInput = (int) (baseInput * 0.5 + scaledFromOwner * 0.5);
        }
        return Math.max(50, Math.min(4500, baseInput));
    }

    private long lastCommandCheck = 0;
    private long lastAwakeningTime = 0;
    private long lastHealTime = 0;

    /**
     * Lắng nghe và phản hồi trực tiếp các khẩu lệnh chat từ chủ nhân.
     */
    private void checkOwnerCommands(Player owner) {
        if (owner == null || map == null) return;
        long now = System.currentTimeMillis();
        if (now - lastCommandCheck < 2000) return;
        lastCommandCheck = now;

        // Nếu máu chủ nhân < 25%, tự động bơm cứu thương cho chủ nhân (cooldown 60s, hồi 10% HP)
        if (owner.ability != null) {
            long hpMax = owner.ability.get_hp_max(true);
            if (hpMax > 0 && ((double) owner.hp / hpMax) < 0.25) {
                if (now - lastHealTime > 60000) {
                    lastHealTime = now;
                    owner.hp = (int) Math.min(hpMax, owner.hp + (hpMax * 10 / 100));
                    try {
                        map.send_chat_popup(0, index_map, "Đã bảo vệ Thuyền trưởng! Hồi sinh lực!");
                    } catch (Throwable ignored) {}
                }
            }
        }
    }

    /**
     * Trái Ác Quỷ Thức Tỉnh — Tung skill nổ sát thương đại diện.
     */
    public void triggerFruitAwakening(Player owner, Mob target) {
        if (target == null || target.isdie || map == null || !canAttackMob(owner, target)) return;
        long now = System.currentTimeMillis();
        if (now - lastAwakeningTime < 90000) return; // Cooldown 90s (cũ 60s)
        lastAwakeningTime = now;

        try {
            int burstDame = (int) (calculateDamage() * 1.25); // 1.25 lần dame thường (cũ 1.5x)
            map.send_chat_popup(0, index_map, "TRÁI ÁC QUỶ THỨC TỈNH!");
            map.Fire_Monster(new Mob[]{target}, this, selectSkillId(), burstDame);
        } catch (Throwable ignored) {}
    }

    public void despawn() {
        try {
            leave();
        } catch (Throwable ignored) {}
        network.SessionManager.PLAYERS_MAP.remove(IDPlayer);
        network.SessionManager.PLAYERS_BY_NAME.remove(name);
//        core.Log.info("MercenaryBot", "Despawned: " + name + " (owner=" + ownerPlayerId + ")");
    }

    @Override
    public void leave() {
        if (this.map != null) {
            Player owner = network.SessionManager.PLAYERS_MAP.get(ownerPlayerId);
            if (owner != null && owner.conn != null) {
                try {
                    Message m2 = new Message(2);
                    m2.writer().writeShort(this.index_map);
                    m2.writer().writeByte(0);
                    owner.addmsg(m2);
                    m2.cleanup();

                    Message m3 = new Message(3);
                    m3.writer().writeShort(this.index_map);
                    m3.writer().writeByte(0);
                    owner.addmsg(m3);
                    m3.cleanup();

                    if (owner.id_meet_in_map != null) {
                        owner.id_meet_in_map.remove("" + this.index_map);
                    }
                } catch (Throwable ignored) {}
            }
            this.map.remove_obj(this.index_map, 0);
            this.map.leave_map(this, 0);
            this.map = null;
        }
    }

    @Override
    public void addmsg(Message m) {
        try {
            if (m != null) {
                m.cleanup();
            }
        } catch (Throwable ignored) {}
    }

    @Override
    public synchronized int flush(Player p, boolean print) {
        return 0;
    }

    @Override
    public synchronized int flush(Player p, boolean print, String tableName) {
        return 0;
    }

    @Override
    public void join(map.Zone zone, short x, short y) {
        if (this.mercStatus == STATUS_HOME) {
            return;
        }
        Player owner = getOwner();
        if (owner != null && MercenaryManager.gI().getPlayerMercStatus(owner) == STATUS_HOME) {
            return;
        }
        if (isRestrictedPvpMap(zone)) {
            return;
        }
        super.join(zone, x, y);
    }

    @Override
    public boolean setup() {
        return true;
    }
}
