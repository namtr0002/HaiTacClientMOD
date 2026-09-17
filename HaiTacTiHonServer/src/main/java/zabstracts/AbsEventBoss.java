package zabstracts;

import model.Player;
import core.Manager;
import mob.Mob;
import map.Zone;
import template.MobTemplate;
import zinterfaces.iMob;

/**
 * iEventBoss — Abstract base class cho mọi boss sự kiện.
 *
 * Implements iMob — được gắn vào mob.iMob bình thường.
 *
 * ============================================================
 *  CÁCH TẠO EVENT BOSS MỚI (~30 dòng):
 * ============================================================
 *
 *  public class LanSuTu extends iEventBoss {
 *
 *      public LanSuTu(int slotId) {
 *          super(slotId,
 *                153,          // mobTemplateId
 *                500,          // hp max
 *                99,           // level
 *                2, 4,         // mapId, zoneId mặc định
 *                "Lân Sư Tử"); // tên thông báo
 *      }
 *
 *      Override
 *      public void onKillRewardAll(Player killer) {
 *          for (Player p : killer.map.players) {
 *              SuKienGioTo.gI().giftKillBossALL(p);
 *          }
 *      }
 *
 *      Override
 *      public void onKillReward(Player killer) {
 *          SuKienGioTo.gI().giftKillBoss(killer);
 *      }
 *  }
 *
 * ============================================================
 *  Framework tự lo: init mob, gán iMob, remove_obj, chatKTG.
 *  Subclass CHỈ override reward methods.
 *  afterDeath() hook cho logic đặc biệt (recovery boss, v.v.)
 * ============================================================
 */
public abstract class AbsEventBoss implements iMob {

    // ======================================================
    //  FIELDS
    // ======================================================

    /** Slot index (0-based) trong danh sách boss cùng loại */
    public final int  slotId;

    /** Mob object gắn vào game world */
    public final Mob  mob;

    /** Thời điểm boss tự despawn (ms). -1 = không tự despawn */
    public long       timeLive = -1;

    /** Prefix tên boss cho chat thông báo (VD: "Bí Ngô Ma Quái") */
    protected final String announcePrefix;

    // ======================================================
    //  CONSTRUCTOR
    // ======================================================

    /**
     * Khởi tạo event boss và đăng ký vào game world.
     *
     * @param slotId         slot index trong pool boss
     * @param mobTemplateId  template ID của mob
     * @param hpMax          máu tối đa
     * @param level          cấp độ
     * @param defaultMapId   mapId mặc định khi idle (thường map lobby)
     * @param defaultZoneId  zone index trong map
     * @param announcePrefix tên boss để thông báo khi chết
     */
    protected AbsEventBoss(int slotId, int mobTemplateId, int hpMax, int level,
                         int defaultMapId, int defaultZoneId,
                         String announcePrefix) {
        this.slotId        = slotId;
        this.announcePrefix = announcePrefix;

        // Khởi tạo mob object
        Mob m = new Mob();
        m.iMob          = this;
        m.keyflag       = getClass().getSimpleName();  // dùng tên class làm keyflag
        m.mtemplate  = MobTemplate.get_mob_template(mobTemplateId);
        m.hp_max        = hpMax;
        m.hp            = 0;
        m.level         = level;
        m.isdie         = true;
        m.id_target     = -1;
        m.index         = Manager.gI().index_mob.getAndIncrement();

        // Tọa độ mặc định (idle position)
        m.x = getDefaultX();
        m.y = getDefaultY();

        // Gắn vào map mặc định
        Zone[] zones = Zone.getMapByID(defaultMapId);
        if (zones != null && defaultZoneId < zones.length) {
            m.map = zones[defaultZoneId];
        }

        // Đăng ký vào global và map mob table
        Mob.ENTRYS.put(m.index, m);
        if (m.map != null) {
            m.map.mobs.put(m.index, m);
        }

        this.mob = m;
    }

    protected String key;

    public String getSeasonKey() {
        return key != null ? key : "EventBoss_" + slotId;
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    // ======================================================
    //  ABSTRACT — subclass implement phần thưởng
    // ======================================================

    /**
     * Thưởng cho TẤT CẢ player trong map khi boss chết.
     * Gọi TRƯỚC onKillReward().
     */
    public abstract void onKillRewardAll(Player killer);

    /**
     * Thưởng riêng cho người giết boss.
     * Gọi SAU onKillRewardAll().
     */
    public abstract void onKillReward(Player killer);

    // ======================================================
    //  OPTIONAL OVERRIDE
    // ======================================================

    /**
     * Hook sau khi framework xử lý xong onDeath (remove mob, chatKTG, reward).
     * Subclass override nếu cần thêm logic: recovery boss, đặt lại timer...
     */
    public void afterDeath(Player killer, Mob mob) {}

    /**
     * Toạ độ X mặc định khi idle — override nếu cần.
     */
    public short getDefaultX() { return 500; }

    /**
     * Toạ độ Y mặc định khi idle — override nếu cần.
     */
    public short getDefaultY() { return 250; }

    // ======================================================
    //  TEMPLATE: onDeath — FINAL, không override
    // ======================================================

    /**
     * Framework xử lý boss chết:
     * 1. Gửi thưởng toàn map
     * 2. Gửi thưởng người giết
     * 3. Remove mob khỏi map
     * 4. Chat thông báo toàn server
     * 5. Gọi afterDeath() hook
     */
    @Override
    public final void onDeath(Player killer, Mob mob) {
        // 1+2. Reward
        onKillRewardAll(killer);
        onKillReward(killer);

        // 3. Remove mob khỏi map
        mob.map.remove_obj(mob.index, 1);

        // 4. Thông báo toàn server
        Manager.gI().chatKTG(0,
                killer.name + " đã hạ gục " + announcePrefix + " #" + (slotId + 1), 0);

        // 5. Hook
        afterDeath(killer, mob);
    }

    // ======================================================
    //  TEMPLATE: update — default không làm gì
    // ======================================================

    /**
     * Game loop của boss — gọi từ Event.update().
     * Default: tự despawn khi hết timeLive.
     * Override để thêm AI, di chuyển...
     */
    @Override
    public void update(Mob mob) {
        if (timeLive > 0 && !mob.isdie && System.currentTimeMillis() > timeLive) {
            mob.isdie = true;
            mob.hp    = 0;
            mob.map.remove_obj(mob.index, 1);
        }
    }

    // ======================================================
    //  SPAWN HELPER
    // ======================================================

    /**
     * Spawn boss vào map + zone được chỉ định.
     * Reset trạng thái: hp đầy, isdie=false, timeLive.
     *
     * @param mapId    ID map
     * @param zoneId   zone index
     * @param x, y     toạ độ spawn
     * @param liveSec  thời gian sống (giây). -1 = không tự despawn
     */
    public void spawn(int mapId, int zoneId, short x, short y, long liveSec) {
        Zone[] zones = Zone.getMapByID(mapId);
        if (zones == null || zoneId >= zones.length) return;

        Zone zone = zones[zoneId];
        mob.map    = zone;
        mob.x      = x;
        mob.y      = y;
        mob.hp     = mob.hp_max;
        mob.isdie  = false;
        mob.id_target = -1;
        timeLive   = (liveSec > 0) ? System.currentTimeMillis() + liveSec * 1000L : -1;

        // Đảm bảo đã đăng ký trong map mobs
        zone.mobs.putIfAbsent(mob.index, mob);
    }

    // ======================================================
    //  GETTERS
    // ======================================================

    public boolean isAlive()   { return !mob.isdie; }
    public int     getSlotId() { return slotId; }
    public Mob     getMob()    { return mob; }
}
