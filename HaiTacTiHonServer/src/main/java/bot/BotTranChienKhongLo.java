package bot;

import ability.Ability;
import clan.Clan;
import clan.ClanMember;
import map.Zone;
import model.Player;
import map.zones.TranChienKhongLo;
import map.zones.MapTranChienKhongLo;
import network.Message;
import core.ZUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BotTranChienKhongLo — Bot phó bản Trận Chiến Khổng Lồ (Dorry vs Brogy).
 *
 * <p>Mỗi bot được gán vào 1 đội (type=4 Dorry hoặc type=5 Brogy), thuộc Clan ảo.
 * Bot có AI di chuyển trong phạm vi chiến trường, tấn công quái và người chơi đối phương,
 * hồi sinh tại căn cứ khi bị hạ gục.
 */
public class BotTranChienKhongLo extends Bot {

    /** Đội của bot: 4 = Dorry, 5 = Brogy */
    public int team;

    /** Thời điểm DPS tích luỹ tiếp theo */
    private long nextDpsTime = 0;
    private long respawnTime = 0;

    /** Chu kỳ DPS cân bằng (35s - 60s / 1 MP) tránh kết thúc trận quá nhanh */
    private static final int DPS_INTERVAL_MIN = 35_000;
    private static final int DPS_INTERVAL_MAX = 60_000;
    private static final int DPS_MP_PER_TICK = 1;

    /** Pool bot đang hoạt động */
    public static final List<BotTranChienKhongLo> POOL = new CopyOnWriteArrayList<>();

    // ======================================================
    //  CONSTRUCTOR
    // ======================================================

    public BotTranChienKhongLo(int id, String name, int team) throws Exception {
        super(id, name);
        this.team = team;
        this.type_pk = (byte) team;
        this.disableBaseChat = false;
        this.id_map_save = 79; // Đảo Little Garden
    }

    // ======================================================
    //  INIT & JOIN
    // ======================================================

    @Override
    public void init() {
        this.type_pk = (byte) this.team;
        this.respawnTime = 0;
        this.setAttack(new AttackAround());
        this.setMove(new MoveAround(2500L));
        if (this.ability != null) {
            this.hp = (int) Math.max(3000, this.ability.get_hp_max(true));
            this.mp = (int) Math.max(1000, this.ability.get_mp_max(true));
        }
        this.isdie = false;
        this.nextDpsTime = System.currentTimeMillis() + ZUtil.random(DPS_INTERVAL_MIN, DPS_INTERVAL_MAX);
    }

    @Override
    public void join(Zone map, short x, short y) {
        this.type_pk = (byte) this.team;
        this.setAttack(new AttackAround());
        this.setMove(new MoveAround(2500L));
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

    // ======================================================
    //  UPDATE — Di chuyển, chiến đấu và hồi sinh
    // ======================================================

    @Override
    public void update() {
        if (this.map == null) return;

        // Chỉ hoạt động khi trong map Little Garden
        if (this.map.map_little_garden == null) return;

        MapTranChienKhongLo game = this.map.map_little_garden;
        if (game.is_finish) {
            this.leave();
            return;
        }

        // Xử lý khi bot bị hạ gục -> hồi sinh tại góc căn cứ
        if (this.isdie) {
            long now = System.currentTimeMillis();
            if (respawnTime == 0) {
                respawnTime = now + 7000L; // 7 giây hồi sinh
            } else if (now >= respawnTime) {
                respawnTime = 0;
                this.isdie = false;
                if (this.ability != null) {
                    this.hp = (int) this.ability.get_hp_max(true);
                    this.mp = (int) this.ability.get_mp_max(true);
                }
                // Hồi sinh ở góc căn cứ của phe mình
                this.x = (short) (this.team == 4 ? ZUtil.random(320, 420) : ZUtil.random(1380, 1480));
                this.y = (short) ZUtil.random(240, 280);
                this.type_pk = (byte) this.team;
                try {
                    this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                    this.map.change_flag(this, this.type_pk);
                } catch (Exception ignored) {}
            }
            return;
        }

        // Tự động kiểm tra phạm vi di chuyển trong map Little Garden (tránh văng ra ngoài)
        if (this.x < 150) this.x = 200;
        if (this.x > 1650) this.x = 1600;
        if (this.y < 200) this.y = 240;
        if (this.y > 320) this.y = 280;

        // Bot tự động nhặt vật phẩm Little Garden trên mặt đất
        if (this.map != null && this.map.list_it_map != null) {
            for (int i = 0; i < this.map.list_it_map.length; i++) {
                template.ItemMap itm = this.map.list_it_map[i];
                if (itm != null && itm.category == 4 && itm.id >= 94 && itm.id <= 100) {
                    if (itm.id_master == -1 || itm.id_master == this.index_map) {
                        try {
                            this.map.pick_item(this, itm.index, (byte) 4);
                        } catch (Exception ignored) {}
                        break;
                    }
                }
            }
        }

        // Tích luỹ MP vào khổng lồ định kỳ
        long now = System.currentTimeMillis();
        if (now >= nextDpsTime) {
            nextDpsTime = now + ZUtil.random(DPS_INTERVAL_MIN, DPS_INTERVAL_MAX);
            try {
                TranChienKhongLo.update_mp(this.map, this.team, DPS_MP_PER_TICK);
            } catch (IOException ignored) {}
        }

        super.update();
    }

    // ======================================================
    //  FACTORY — Tạo và điền bot khi thiếu người
    // ======================================================

    /**
     * Tự động điền bot cân bằng cho phó bản Trận Chiến Khổng Lồ.
     */
    public static void autoFill(Zone mapFight, int team, int needed, short avgLv, Clan botClan) {
        if (mapFight == null || needed <= 0) return;
        for (int i = 0; i < needed; i++) {
            try {
                int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                byte botClazz = (byte) ((i % 5) + 1);
                String botName = bot.botplayer.BotNameGenerator.getRandomBotName();

                BotTranChienKhongLo b = new BotTranChienKhongLo(botId, botName, team);
                b.isBot = true;
                b.clazz = botClazz;
                b.clan = botClan;
                b.level = (short) Math.max(1, avgLv + ZUtil.random(-2, 2));

                // Cân bằng theo toàn bộ người chơi thật trong trận
                if (mapFight.players != null && !mapFight.players.isEmpty()) {
                    BotBalanceEngine.balanceAgainstTeam(b, mapFight.players, avgLv);
                } else {
                    int botTier = b.level >= 90 ? 4 : (b.level >= 75 ? 3 : (b.level >= 50 ? 2 : 1));
                    b.setupBotEquip(botClazz, b.level, botTier, true, true, true);
                    b.setupBotSkills(botClazz, (short) Math.max(20, Math.min(30, b.level / 2)), botTier, 0);
                    b.setupBotAppearance(botClazz, botTier);
                    b.autoAllocatePotentialPoints(botClazz);
                    b.setin4();
                    b.init();
                    b.ability = new Ability(b);
                    b.updateParts();
                }

                // Vị trí spawn theo đội
                short spawnX = (short) (team == 4 ? (320 + i * 35) : (1380 + i * 35));
                short spawnY = (short) ZUtil.random(240, 280);
                b.join(mapFight, spawnX, spawnY);
            } catch (Exception ignored) {}
        }
    }

    public static void autoFill(Zone mapFight, int team, int needed, short avgLv) {
        Clan defaultClan = new Clan();
        defaultClan.id = (short) (team == 4 ? -444 : -555);
        defaultClan.name = (team == 4) ? "Băng Dorry" : "Băng Brogy";
        defaultClan.icon = (short) (team == 4 ? 3 : 5);
        defaultClan.level = (short) Math.max(1, avgLv / 8);
        defaultClan.trungsinh = (byte) Math.min(6, avgLv / 20);
        defaultClan.members = new ArrayList<>();
        ClanMember captain = new ClanMember();
        captain.id = defaultClan.id;
        captain.name = (team == 4 ? "Dorry Người Khổng Lồ" : "Brogy Người Khổng Lồ");
        captain.level = avgLv;
        captain.levelInclan = 0;
        defaultClan.members.add(captain);
        autoFill(mapFight, team, needed, avgLv, defaultClan);
    }

    /**
     * Đếm số bot của 1 đội hiện có trên 1 map.
     */
    public static int countBotsOnMap(Zone map, int team) {
        int count = 0;
        for (Player p : map.players) {
            if (p instanceof BotTranChienKhongLo && ((BotTranChienKhongLo) p).team == team) {
                count++;
            }
        }
        return count;
    }

    /**
     * Tính level trung bình đội thật trên map.
     */
    public static short calcAvgLv(Zone map, int team) {
        int sum = 0, count = 0;
        for (Player p : map.players) {
            if (!p.isBot && p.type_pk == team) {
                sum += p.level;
                count++;
            }
        }
        return count > 0 ? (short) Math.max(1, sum / count) : 50;
    }
}

