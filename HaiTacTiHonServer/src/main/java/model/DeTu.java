package model;

import ability.Ability;
import clan.Clan;
import core.ZUtil;
import core.Manager;
import mob.Mob;
import map.Zone;
import map.zones.ChiemDao;
import template.EffTemplate;
import skill.Skill_info;
import template.Option;
import template.Item_wear;
import skill.Skill_Template;
import template.ItemFashionP;
import template.ItemFashionP2;
import template.ItemBoatP;
import org.json.simple.JSONValue;
import database.DbManager;
import itemz.Item;
import template.ItemTemplate3;
import network.Message;
import map.MapManager;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DeTu extends Player {

    public Player master;

    public Player targetPlayer = null;
    public int interactStep = 0;
    public long nextInteractTime = 0;

    // Smooth movement targets
    public short targetX = -1;
    public short targetY = -1;
    private long nextMoveTime = 0;
    
    // Players who rejected this disciple
    public java.util.Set<Integer> rejectedPlayers = new java.util.HashSet<>();
    public long wildSpawnTime = System.currentTimeMillis();
    public long nextWildChatTime = 0;

    public static final String[] WILD_DETU_CHAT_MSGS = new String[]{
        "Cần mở chức năng Sư Phụ tại NPC Garp mới nhận được ta nhé!",
        "Ai đã mở chức năng Sư Phụ chưa? Hãy đến nhận ta làm đệ tử nào!",
        "Hãy đến gặp Phó Đô Đốc Garp mở chức năng Sư Phụ để thu nhận ta nhé!",
        "Chỉ Sư Phụ đã mở chức năng Sư Đồ mới có thể nhận đệ tử hoang đấy!",
        "Muốn nhận ta làm đệ tử thì hãy tìm Chứng nhận sư phụ và gặp Garp nhé!"
    };

    // =========================================================
    // === HỆ THỐNG TRẠNG THÁI ĐỆ Tử ===
    // =========================================================
    public static final int STATUS_HOME    = 0; // Về nhà - ẩn, flush DB
    public static final int STATUS_FOLLOW  = 1; // Đi theo sư phụ (mặc định)
    public static final int STATUS_PROTECT = 2; // Bảo vệ - đánh mục tiêu trong 70xy của sư phụ
    public static final int STATUS_ATTACK  = 3; // Tấn công - toàn map, ưu tiên kẻ đánh sư phụ
    public static final int STATUS_FUSION  = 4; // Hợp thể - merge chỉ số vào sư phụ 10 phút

    public static final String[] STATUS_NAMES = {"Về nhà", "Đi theo", "Bảo vệ", "Tấn công", "Hợp thể"};

    public int  detuStatus      = STATUS_FOLLOW; // Trạng thái hiện tại
    public long fusionEndTime   = 0;             // Thời gian kết thúc hợp thể
    public long fusionCooldown  = 0;             // Cooldown trước khi dùng hợp thể lại
    public Player lastAttackerOfMaster = null;   // Kẻ tấn công sư phụ gần nhất (cho STATUS_ATTACK)
    private long nextAttackTime = 0;
    private long respawnTimer   = 0;

    public DeTu(String name, Player master) {
        super(null, name);
        this.master = master;
        this.isDe = true;
        this.date = org.joda.time.DateTime.now();
        this.rms = new byte[11][];
        for (int i = 0; i < this.rms.length; i++) {
            this.rms[i] = new byte[0];
        }
    }

    /**
     * Thay đổi trạng thái đệ tử.
     * @param newStatus STATUS_HOME / STATUS_FOLLOW / STATUS_PROTECT / STATUS_ATTACK / STATUS_FUSION
     */
    public void setStatus(int newStatus) {
        if (master == null) return;

        // Thoát fusion cũ trước khi đổi sang trạng thái khác
        if (this.detuStatus == STATUS_FUSION && newStatus != STATUS_FUSION) {
            removeFusion();
        }

        switch (newStatus) {
            case STATUS_HOME: {
                // Ẩn đệ tử khỏi map, flush DB, unload
                if (this.map != null) {
                    this.map.leave_map(this, 2);
                    this.map = null;
                }
                this.detuStatus = STATUS_HOME;
                master.isDeOnl = false;
                saveDeTu(false);
                try {
                    if (master.getService() != null)
                        master.getService().send_box_ThongBao_OK("Đệ tử đã về nhà (trạng thái ẩn/offline)!");
                } catch (Exception ignored) {}
                return;
            }
            case STATUS_FUSION: {
                if (fusionCooldown > System.currentTimeMillis()) {
                    long left = (fusionCooldown - System.currentTimeMillis()) / 1000;
                    try {
                        if (master.getService() != null)
                            master.getService().send_box_ThongBao_OK("Hợp thể đang hồi lại! Còn " + left + " giây.");
                    } catch (Exception ignored) {}
                    return;
                }
                this.detuStatus = STATUS_FUSION;
                applyFusion();
                if (this.map != null) {
                    this.map.leave_map(this, 2);
                    this.map = null;
                }
                this.fusionEndTime = System.currentTimeMillis() + 10L * 60 * 1000; // 10 phút
                saveDeTu(false);
                try {
                    if (master.getService() != null) {
                        master.getService().send_box_ThongBao_OK("Hợp thể thành công! Chỉ số được cộng thêm 10 phút.");
                        master.getService().Main_char_Info(false);
                    }
                } catch (Exception ignored) {}
                return;
            }
            default: {
                // FOLLOW / PROTECT / ATTACK
                this.detuStatus = newStatus;
                master.isDeOnl = true;
                if (master != null) {
                    this.clan = master.clan;
                    this.typePirate = master.typePirate;
                    this.type_pk = master.type_pk;
                }
                // Đảm bảo đệ tử được trong map
                if (this.map == null && master != null && master.map != null) {
                    this.map = master.map;
                    this.x = (short) (master.x + 25);
                    this.y = (short) (master.y + 10);
                    master.map.enter_map(this);
                    for (Player p0 : master.map.players) {
                        if (p0 != null && p0.getService() != null) {
                            try {
                                p0.getService().update_PK(this, false);
                            } catch (Exception ignored) {}
                        }
                    }
                }
                saveDeTu(false);
                try {
                    if (master.getService() != null)
                        master.getService().send_box_ThongBao_OK("Đệ tử đã đổi sang trạng thái: " + STATUS_NAMES[newStatus] + "!");
                } catch (Exception ignored) {}
                return;
            }
        }
    }

    /**
     * Áp dụng chỉ số hợp thể: chỉ lấy HP, MP, Dame / 2 (50%) của đệ tử vào sư phụ.
     */
    public void applyFusion() {
        if (master == null) return;
        int detuHp   = this.ability.get_hp_max(true);
        int detuMp   = this.ability.get_mp_max(true);
        int detuDame = this.ability.get_dame(true);
        int oldBonusHp = master.fusionBonusHp;
        master.fusionBonusHp   = detuHp / 2;
        master.fusionBonusMp   = detuMp / 2;
        master.fusionBonusDame = detuDame / 2;
        master.fusionBonusDef  = 0;
        master.isFusion = true;
        // Phải recalculate trước khi đọc hpMax mới — không dùng cached get_hp_max(true)
        master.setAbility();
        master.hp = Math.min(master.hp + (master.fusionBonusHp - oldBonusHp), master.hpMax);
    }

    /**
     * Thoát hợp thể: xóa bonus, đệ tử đưa về map STATUS_PROTECT.
     */
    public void removeFusion() {
        if (master != null) {
            master.fusionBonusHp   = 0;
            master.fusionBonusMp   = 0;
            master.fusionBonusDame = 0;
            master.fusionBonusDef  = 0;
            master.isFusion = false;
            master.setAbility();
            // Kiểm tra HP sau khi bỏ bonus
            int hpMax = master.ability.get_hp_max(true);
            if (master.hp > hpMax) master.hp = hpMax;
            // Set cooldown 5 phút
            this.fusionCooldown = System.currentTimeMillis() + 5L * 60 * 1000;
            // Báo master
            try {
                if (master.getService() != null) {
                    master.getService().send_box_ThongBao_OK("Hợp thể đã kết thúc. Đệ tử tách ra và chuyển sang trạng thái Bảo vệ.");
                    master.getService().Main_char_Info(false);
                }
            } catch (Exception ignored) {}
        }
        this.fusionEndTime = 0;
        this.detuStatus = STATUS_PROTECT;
        // Thêm đệ tử vào map ở chỗ sư phụ
        if (this.map == null && master != null && master.map != null) {
            this.map = master.map;
            this.x = (short) (master.x + 25);
            this.y = (short) (master.y + 10);
            this.clan = master.clan;
            this.typePirate = master.typePirate;
            this.type_pk = master.type_pk;
            master.isDeOnl = true;
            try {
                master.map.enter_map(this);
            } catch (Exception ignored) {}
        }
    }

    private byte lastClanRole = -1;

    public void syncMasterIdentity() {
        if (master == null) return;
        byte oldPk = this.type_pk;
        int oldPirate = this.typePirate;
        Clan oldClan = this.clan;
        byte currentRole = (master.clan != null) ? (byte) master.clan.getTypeMem(master) : (byte) 10;
        byte oldRole = this.lastClanRole;

        this.clan = master.clan;
        this.typePirate = master.typePirate;
        if (this.map != null && (this.map.map_little_garden != null || this.map.template.id == 81)) {
            if (this.map.map_little_garden != null) {
                boolean isClan1 = (master.clan != null && master.clan.equals(this.map.map_little_garden.clan1));
                this.type_pk = (byte) (isClan1 ? 4 : 5);
            } else {
                this.type_pk = master.type_pk;
            }
        } else {
            this.type_pk = master.type_pk;
        }
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
    public void update() {
        if (master != null) {
            updateMasterAI();
        } else {
            updateWildAI();
        }
    }

    public void updateMasterAI() {
        if (master == null || master.conn == null) return;

        // 0. Ẩn/rời map nếu sư phụ đang ở Chế Độ Xem (Spectator)
        if (master.isSpectator) {
            if (this.map != null) {
                this.map.leave_map(this, 2);
                this.map = null;
            }
            return;
        }

        // 1. Kiểm tra trạng thái Về nhà (Ẩn đệ tử offline)
        if (this.detuStatus == STATUS_HOME) {
            if (this.map != null) {
                this.map.leave_map(this, 2);
                this.map = null;
            }
            master.isDeOnl = false;
            return;
        }

        // 2. Kiểm tra trạng thái Hợp thể
        if (this.detuStatus == STATUS_FUSION) {
            if (System.currentTimeMillis() > this.fusionEndTime) {
                removeFusion();
            }
            return;
        }

        master.isDeOnl = true;

        // 3. Tự động hồi sinh sau 60 giây nếu đệ tử tử trận (xác giữ nguyên tại map chết)
        if (this.isdie) {
            long now = System.currentTimeMillis();
            if (respawnTimer == 0) {
                respawnTimer = now + 60_000L; // 60 giây
            }
            if (now >= respawnTimer) {
                this.isdie = false;
                this.respawnTimer = 0;
                int maxHp = this.ability.get_hp_max(true);
                int hp10Percent = Math.max(100, maxHp / 10); // 10% HP như về làng của nhân vật
                this.hp = hp10Percent;
                this.mp = Math.max(100, this.ability.get_mp_max(true) / 10);

                if (this.map != null) {
                    try {
                        Message mRevive = new Message(-71);
                        mRevive.writer().writeByte(1);
                        mRevive.writer().writeShort(this.index_map);
                        mRevive.writer().writeByte(0);
                        mRevive.writer().writeInt(1);
                        this.map.send_msg_all_p(mRevive, null, true);
                        mRevive.cleanup();

                        Message mHp = new Message(-83);
                        mHp.writer().writeShort(this.index_map);
                        mHp.writer().writeByte(0);
                        mHp.writer().writeInt(maxHp);
                        mHp.writer().writeInt(this.hp);
                        mHp.writer().writeInt(this.hp);
                        mHp.writer().writeInt(this.ability.get_mp_max(true));
                        mHp.writer().writeInt(this.mp);
                        mHp.writer().writeInt(0);
                        this.map.send_msg_all_p(mHp, null, true);
                        mHp.cleanup();
                    } catch (Throwable ignored) {}
                }
            }
            return; // Trong thời gian chết, xác giữ nguyên tại map chết, không theo sư phụ đổi map
        }

        // 4. Theo sư phụ đổi map
        if (master.map != null && (this.map == null || !this.map.equals(master.map))) {
            if (master.isSpectator || master.isClosed || this.detuStatus == STATUS_HOME || this.detuStatus == STATUS_FUSION) {
                if (this.map != null) {
                    this.map.leave_map(this, 2);
                    this.map = null;
                }
                return;
            }
            if (this.map != null) {
                this.map.leave_map(this, 2);
            }
            this.map = master.map;
            this.x = (short) (master.x + 25);
            this.y = (short) (master.y + 10);
            syncMasterIdentity();
            this.map.enter_map(this);
            return;
        }

        if (this.map == null) return;

        syncMasterIdentity();
        super.update(); // Tick hiệu ứng, buff, hồi phục

        double distToMaster = Math.hypot(master.x - this.x, master.y - this.y);

        // 5. Dịch chuyển tức thời về cạnh sư phụ nếu quá xa (> 450px)
        if (distToMaster > 450) {
            this.x = (short) (master.x + 25);
            this.y = (short) (master.y + 10);
            try {
                this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
            } catch (Exception ignored) {}
            return;
        }

        long now = System.currentTimeMillis();

        // 6. Xử lý hành vi theo đúng từng trạng thái
        switch (this.detuStatus) {
            case STATUS_FOLLOW: {
                // Đi theo sư phụ (không tấn công quái hay kẻ địch)
                if (distToMaster > 75) {
                    short targetX = (short) (master.x - 30);
                    short targetY = (short) (master.y + 10);
                    moveTowardsTarget(targetX, targetY, 55);
                }
                break;
            }
            case STATUS_PROTECT: {
                // Bảo vệ sư phụ trong phạm vi bán kính 160px xung quanh sư phụ
                Object threat = findThreatNearMaster(160);
                if (threat != null) {
                    short tX, tY;
                    if (threat instanceof Mob) {
                        tX = ((Mob) threat).x;
                        tY = ((Mob) threat).y;
                    } else {
                        tX = ((Player) threat).x;
                        tY = ((Player) threat).y;
                    }
                    double dToThreat = Math.hypot(tX - this.x, tY - this.y);
                    Skill_info skill = selectSkill(threat);
                    int skillRange = (skill != null && skill.temp != null && skill.temp.range > 0) ? skill.temp.range : 75;

                    if (dToThreat > Math.min(skillRange, 50)) {
                        moveTowardsTarget(tX, tY, 65);
                    }
                    if (dToThreat <= Math.max(skillRange, 90)) {
                        if (now >= nextAttackTime) {
                            long attackDelay = 600L;
                            if (threat instanceof Mob) {
                                attackDelay = attackMob((Mob) threat, skill);
                            } else {
                                attackDelay = attackPlayer((Player) threat, skill);
                            }
                            nextAttackTime = now + attackDelay;
                        }
                    }
                } else {
                    // Không có mối đe dọa -> đi sát sư phụ
                    if (distToMaster > 75) {
                        short targetX = (short) (master.x - 30);
                        short targetY = (short) (master.y + 10);
                        moveTowardsTarget(targetX, targetY, 55);
                    }
                }
                break;
            }
            case STATUS_ATTACK: {
                // Tấn công toàn bản đồ (săn quái / kẻ địch)
                Object target = findFullMapTarget();
                if (target != null) {
                    short tX, tY;
                    if (target instanceof Mob) {
                        tX = ((Mob) target).x;
                        tY = ((Mob) target).y;
                    } else {
                        tX = ((Player) target).x;
                        tY = ((Player) target).y;
                    }
                    double dToTarget = Math.hypot(tX - this.x, tY - this.y);
                    Skill_info skill = selectSkill(target);
                    int skillRange = (skill != null && skill.temp != null && skill.temp.range > 0) ? skill.temp.range : 75;

                    if (dToTarget > Math.min(skillRange, 50)) {
                        moveTowardsTarget(tX, tY, 70);
                    }
                    if (dToTarget <= Math.max(skillRange, 90)) {
                        if (now >= nextAttackTime) {
                            long attackDelay = 600L;
                            if (target instanceof Mob) {
                                attackDelay = attackMob((Mob) target, skill);
                            } else {
                                attackDelay = attackPlayer((Player) target, skill);
                            }
                            nextAttackTime = now + attackDelay;
                        }
                    }
                } else {
                    // Đã sạch quái -> quay về đi theo sư phụ
                    if (distToMaster > 75) {
                        short targetX = (short) (master.x - 30);
                        short targetY = (short) (master.y + 10);
                        moveTowardsTarget(targetX, targetY, 55);
                    }
                }
                break;
            }
        }
    }

    private Object findThreatNearMaster(int radius) {
        if (this.map == null || this.master == null) return null;

        // 1. Kẻ tấn công sư phụ gần nhất
        if (master.lastAttacker != null && !master.lastAttacker.isdie && master.lastAttacker.map != null
                && master.lastAttacker.map.equals(this.map)) {
            double d = Math.hypot(master.lastAttacker.x - master.x, master.lastAttacker.y - master.y);
            if (d <= radius + 60 && canAttackPlayer(master.lastAttacker)) {
                return master.lastAttacker;
            }
        }
        if (lastAttackerOfMaster != null && !lastAttackerOfMaster.isdie && lastAttackerOfMaster.map != null
                && lastAttackerOfMaster.map.equals(this.map)) {
            double d = Math.hypot(lastAttackerOfMaster.x - master.x, lastAttackerOfMaster.y - master.y);
            if (d <= radius + 60 && canAttackPlayer(lastAttackerOfMaster)) {
                return lastAttackerOfMaster;
            }
        }

        // 2. Quái vật trong bán kính xung quanh sư phụ
        Mob nearestMob = null;
        double minMobDist = Double.MAX_VALUE;
        if (this.map.mobs != null) {
            synchronized (this.map.mobs) {
                for (Mob mob : this.map.mobs.values()) {
                    if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(mob)) continue;
                    double d = Math.hypot(mob.x - master.x, mob.y - master.y);
                    if (d <= radius && d < minMobDist) {
                        minMobDist = d;
                        nearestMob = mob;
                    }
                }
            }
        }
        if (nearestMob == null && this.map.list_mob != null) {
            for (int idx : this.map.list_mob) {
                Mob mob = this.map.getMob(idx);
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(mob)) continue;
                double d = Math.hypot(mob.x - master.x, mob.y - master.y);
                if (d <= radius && d < minMobDist) {
                    minMobDist = d;
                    nearestMob = mob;
                }
            }
        }
        if (nearestMob != null) return nearestMob;

        // 3. Người chơi đối địch trong bán kính xung quanh sư phụ
        if (this.map.can_PK && this.map.players != null) {
            for (Player p : this.map.players) {
                if (p == null || p.isdie || p.equals(this) || p.equals(master)) continue;
                if (canAttackPlayer(p)) {
                    double d = Math.hypot(p.x - master.x, p.y - master.y);
                    if (d <= radius) return p;
                }
            }
        }
        return null;
    }

    private Object findFullMapTarget() {
        if (this.map == null || this.master == null) return null;

        // 1. Kẻ tấn công sư phụ
        if (master.lastAttacker != null && !master.lastAttacker.isdie && master.lastAttacker.map != null
                && master.lastAttacker.map.equals(this.map) && canAttackPlayer(master.lastAttacker)) {
            return master.lastAttacker;
        }

        // 2. Quái vật gần nhất trên toàn map
        Mob nearestMob = null;
        double minMobDist = Double.MAX_VALUE;
        if (this.map.mobs != null) {
            synchronized (this.map.mobs) {
                for (Mob mob : this.map.mobs.values()) {
                    if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(mob)) continue;
                    double d = Math.hypot(mob.x - this.x, mob.y - this.y);
                    if (d < minMobDist) {
                        minMobDist = d;
                        nearestMob = mob;
                    }
                }
            }
        }
        if (nearestMob == null && this.map.list_mob != null) {
            for (int idx : this.map.list_mob) {
                Mob mob = this.map.getMob(idx);
                if (mob == null || mob.isdie || mob.hp <= 0 || !canAttackMob(mob)) continue;
                double d = Math.hypot(mob.x - this.x, mob.y - this.y);
                if (d < minMobDist) {
                    minMobDist = d;
                    nearestMob = mob;
                }
            }
        }
        if (nearestMob == null && this.map.mapSanTrum != null && this.map.mapSanTrum.mob != null
                && !this.map.mapSanTrum.mob.isdie && this.map.mapSanTrum.mob.hp > 0 && canAttackMob(this.map.mapSanTrum.mob)) {
            nearestMob = this.map.mapSanTrum.mob;
        }
        if (nearestMob != null) return nearestMob;

        // 3. Người chơi đối địch trong map
        if (this.map.can_PK && this.map.players != null) {
            Player nearestP = null;
            double minPDist = Double.MAX_VALUE;
            for (Player p : this.map.players) {
                if (p == null || p.isdie || p.equals(this) || p.equals(master)) continue;
                if (canAttackPlayer(p)) {
                    double d = Math.hypot(p.x - this.x, p.y - this.y);
                    if (d < minPDist) {
                        minPDist = d;
                        nearestP = p;
                    }
                }
            }
            if (nearestP != null) return nearestP;
        }
        return null;
    }

    /**
     * Tự động chọn kỹ năng chuẩn Player từ A-Z:
     * - Chỉ chọn các chiêu ĐÃ HẾT COOLDOWN (sẵn sàng thi triển).
     * - Kiểm tra MP (năng lượng tiêu hao).
     * - Lọc bỏ chiêu nội tại bị động (typeSkill == 3) & chiêu pháo thuyền (ID 3) trên đất liền.
     * - Random ngẫu nhiên trong danh sách chiêu đặc biệt đang sẵn sàng (Active 1, Active 2, Trái Ác Quỷ, Haki, v.v.).
     * - Khi tất cả chiêu đặc biệt đang cooldown hoặc thiếu MP -> Tự động chuyển về chiêu thường cơ bản.
     */
    public Skill_info selectSkill(Object target) {
        if (this.skill_point == null || this.skill_point.isEmpty()) {
            this.skill_point = getDefaultSkills(this.clazz);
            for (int i = 0; i < this.skill_point.size(); i++) {
                Skill_info sk = this.skill_point.get(i);
                if (sk != null && sk.temp != null && (sk.temp.ID == 0 || sk.temp.ID == 1 || sk.temp.ID == 2)) {
                    if (sk.exp == -1) {
                        Skill_Template.learn_skill(sk);
                    }
                }
            }
        }
        if (this.skill_point == null || this.skill_point.isEmpty()) return null;

        long now = System.currentTimeMillis();
        boolean isSea = (this.map != null && (this.map.template.specMap == 4 || this.map.isMapSea()));

        // Map Biển: Chỉ dùng Skill 3 (Pháo thuyền hải chiến)
        if (isSea) {
            for (Skill_info sk : this.skill_point) {
                if (sk != null && sk.temp != null && sk.temp.ID == 3) {
                    if (this.mp >= sk.temp.manaLost && isSkillReady(sk.temp.ID, now)) {
                        return sk;
                    }
                    return sk;
                }
            }
            return null;
        }

        // Tự động kiểm tra hồi MP / dùng bình mana nếu MP cạn (< 25%)
        checkAutoMana();

        double distToTarget = Double.MAX_VALUE;
        if (target != null) {
            if (target instanceof Mob) {
                Mob m = (Mob) target;
                distToTarget = Math.hypot(m.x - this.x, m.y - this.y);
            } else if (target instanceof Player) {
                Player p = (Player) target;
                distToTarget = Math.hypot(p.x - this.x, p.y - this.y);
            }
        }

        List<Skill_info> readySpecialSkills = new ArrayList<>();
        Skill_info basicSkill = null;
        int baseClassIndex = (this.clazz > 0 ? this.clazz - 1 : 0) * 60;

        for (Skill_info sk : this.skill_point) {
            if (sk == null || sk.temp == null) continue;

            // Loại bỏ chiêu pháo thuyền (ID 3) trên đất liền
            if (sk.temp.ID == 3) continue;

            // Loại bỏ chiêu nội tại bị động (typeSkill == 3)
            if (sk.temp.typeSkill == 3) continue;

            // Kiểm tra chiêu đã học / mở khóa (exp > -1 hoặc Lv_RQ > 0)
            if (sk.exp == -1 && sk.temp.Lv_RQ <= 0) continue;

            int skId = sk.temp.ID;
            int indexInServer = sk.temp.indexSkillInServer;
            boolean isBasic = (skId == 0 || indexInServer == baseClassIndex);

            if (isBasic) {
                if (basicSkill == null) basicSkill = sk;
            } else {
                // Kiểm tra Cooldown & Mana MP của chiêu đặc biệt
                if (isSkillReady(skId, now) && this.mp >= sk.temp.manaLost) {
                    // Kiểm tra phạm vi (nếu có target)
                    int skillRange = sk.temp.range > 0 ? sk.temp.range : 75;
                    if (target == null || distToTarget <= Math.max(skillRange, 90)) {
                        readySpecialSkills.add(sk);
                    }
                }
            }
        }

        // 1. RANDOM CHỌN TRONG DANH SÁCH CHIÊU ĐẶC BIỆT ĐÃ SẴN SÀNG (KHÔNG COOLDOWN)
        if (!readySpecialSkills.isEmpty()) {
            return readySpecialSkills.get(ZUtil.random(readySpecialSkills.size()));
        }

        // 2. Nếu tất cả chiêu đặc biệt đang CD hoặc chưa đủ MP -> Dùng chiêu thường cơ bản
        if (basicSkill != null && isSkillReady(basicSkill.temp.ID, now)) {
            return basicSkill;
        }

        return basicSkill;
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

    public boolean isSkillReady(int skillId, long now) {
        if (skillId >= 0 && this.getSkCooldown(skillId) > now) return false;
        if (this.time_use_skill != null && this.time_use_skill.containsKey(skillId)) {
            if (this.time_use_skill.get(skillId) > now) return false;
        }
        return true;
    }

    public long applySkillCooldownAndCost(Skill_info sk, long now) {
        if (sk == null || sk.temp == null) return 600L;

        int skillId = sk.temp.ID;
        long cdMs = calculateSkillCooldown(sk.temp);

        // Cập nhật Cooldown chuẩn timeDelay qua calculateSkillCooldown
        if (skillId >= 0) {
            this.setSkCooldown(skillId, now + cdMs);
        }
        if (this.time_use_skill != null) {
            this.time_use_skill.put(skillId, now + cdMs);
        }

        // Trừ tiêu hao MP
        if (sk.temp.manaLost > 0) {
            this.mp = Math.max(0, this.mp - sk.temp.manaLost);
        }

        // Hút HP / MP khi đánh
        if (this.ability != null) {
            int hpAbsorb = this.ability.get_hp_atk_absorb(true);
            int mpAbsorb = this.ability.get_mp_atk_absorb(true);
            if (hpAbsorb > 0) {
                this.hp = Math.min(this.ability.get_hp_max(true), this.hp + hpAbsorb);
            }
            if (mpAbsorb > 0) {
                this.mp = Math.min(this.ability.get_mp_max(true), this.mp + mpAbsorb);
            }
        }
        return cdMs;
    }

    public void checkAutoMana() {
        if (this.ability == null) return;
        int maxMp = this.ability.get_mp_max(true);
        if (maxMp <= 0) maxMp = 1000;
        if (this.mp < (maxMp * 25) / 100) {
            // Tự động sử dụng bình năng lượng hồi phục 40% MP như người chơi
            this.mp = Math.min(maxMp, this.mp + (maxMp * 40) / 100);
        }
    }

    public void attackMob(Mob target) {
        attackMob(target, selectSkill(target));
    }

    public long attackMob(Mob target, Skill_info skill) {
        if (map == null || target == null || target.isdie || target.hp <= 0) return 600L;
        if (skill == null || skill.temp == null) {
            skill = selectSkill(target);
        }
        if (skill == null || skill.temp == null) return 600L;

        long now = System.currentTimeMillis();
        long skillCd = applySkillCooldownAndCost(skill, now);

        Skill_info basicSkill = getBasicSkill();
        long basicDelay = (basicSkill != null && basicSkill.temp != null) ? calculateSkillCooldown(basicSkill.temp) : skillCd;
        long attackDelay = (skill == basicSkill) ? skillCd : basicDelay;

        int idSkill = skill.temp.ID;
        long dame = calculateDamage(skill);

        try {
            int maxTargets = skill.temp.nTarget > 0 ? skill.temp.nTarget : 1;
            short rangeLan = skill.temp.rangeLan > 0 ? skill.temp.rangeLan : 80;

            List<Mob> targetsList = new ArrayList<>();
            targetsList.add(target);

            if (maxTargets > 1 && map.mobs != null && !map.mobs.isEmpty()) {
                for (Mob nearby : map.mobs.values()) {
                    if (nearby != null && !nearby.isdie && nearby != target && nearby.hp > 0 && canAttackMob(nearby)) {
                        double d = Math.hypot(nearby.x - target.x, nearby.y - target.y);
                        if (d <= rangeLan) {
                            targetsList.add(nearby);
                            if (targetsList.size() >= maxTargets) break;
                        }
                    }
                }
            }

            Mob[] targets = targetsList.toArray(new Mob[0]);
            long[] exp_up = map.Fire_Monster(targets, this, idSkill, dame);

            if (exp_up != null) {
                if (exp_up[0] > 0) {
                    long expGained = (exp_up[0] * (1000L + (this.ability != null ? this.ability.get_xp_more() : 0))) / 1000L;
                    this.update_exp(expGained, true);
                    if (master != null) {
                        long masterExp = expGained / 2;
                        if (masterExp > 0) {
                            master.update_exp(masterExp, true);
                        }
                    }
                }
                if (exp_up[1] > 0) {
                    long expSkillGained = (exp_up[1] * (1000L + (this.ability != null ? this.ability.get_xp_skill_more() : 0))) / 1000L;
                    this.updateExpSkill((short) idSkill, expSkillGained);
                }
            }
        } catch (Throwable ignored) {}
        return attackDelay;
    }

    public void attackPlayer(Player target) {
        attackPlayer(target, selectSkill(target));
    }

    public long attackPlayer(Player target, Skill_info skill) {
        if (map == null || target == null || target.isdie || target.map == null || !target.map.equals(map)) return 600L;
        if (skill == null || skill.temp == null) {
            skill = selectSkill(target);
        }
        if (skill == null || skill.temp == null) return 600L;

        long now = System.currentTimeMillis();
        long skillCd = applySkillCooldownAndCost(skill, now);

        Skill_info basicSkill = getBasicSkill();
        long basicDelay = (basicSkill != null && basicSkill.temp != null) ? calculateSkillCooldown(basicSkill.temp) : skillCd;
        long attackDelay = (skill == basicSkill) ? skillCd : basicDelay;

        int idSkill = skill.temp.ID;
        long dame = calculateDamage(skill);

        try {
            int maxTargets = skill.temp.nTarget > 0 ? skill.temp.nTarget : 1;
            short rangeLan = skill.temp.rangeLan > 0 ? skill.temp.rangeLan : 80;

            List<Player> targetsList = new ArrayList<>();
            targetsList.add(target);

            if (maxTargets > 1 && map.players != null && !map.players.isEmpty()) {
                for (Player nearby : map.players) {
                    if (nearby != null && !nearby.isdie && nearby != target && nearby.map != null && nearby.map.equals(map) && canAttackPlayer(nearby)) {
                        double d = Math.hypot(nearby.x - target.x, nearby.y - target.y);
                        if (d <= rangeLan) {
                            targetsList.add(nearby);
                            if (targetsList.size() >= maxTargets) break;
                        }
                    }
                }
            }

            Player[] targets = targetsList.toArray(new Player[0]);
            map.Fire_Player(targets, this, idSkill, dame);
            try {
                this.updateExpSkill((short) idSkill, 50L);
            } catch (Throwable ignored) {}
        } catch (Throwable ignored) {}
        return attackDelay;
    }

    private void moveTowardsTarget(short tx, short ty, int speed) {
        if (map == null) return;
        int dx = tx - x;
        int dy = ty - y;
        double dist = Math.hypot(dx, dy);
        if (dist < 5) return;

        if (dist > speed) {
            this.x = (short) (x + (int) (dx * speed / dist));
            this.y = (short) (y + (int) (dy * speed / dist));
        } else {
            this.x = tx;
            this.y = ty;
        }
        try {
            this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
        } catch (Exception ignored) {}
    }

    public int selectSkillId() {
        Skill_info sk = selectSkill(null);
        if (sk != null && sk.temp != null) {
            return sk.temp.ID;
        }
        switch (this.clazz) {
            case 1: return 0;
            case 2: return 60;
            case 3: return 120;
            case 4: return 180;
            case 5: return 240;
            default: return 0;
        }
    }

    public int calculateDamage() {
        return (int) calculateDamage(null);
    }

    public long calculateDamage(Skill_info skill) {
        long baseDame = (ability != null && ability.get_dame(true) > 0) ? ability.get_dame(true) : (long) level * 80;
        if (master != null && master.ability != null) {
            long masterAtk = master.ability.get_dame(true);
            if (masterAtk > baseDame) {
                baseDame = (long) (masterAtk * 0.85);
            }
        }
        if (ability != null) {
            baseDame = (baseDame * ability.get_dame_devil_percent()) / 100;
        }
        EffTemplate eff = get_eff(5); // combo
        if (eff != null) {
            baseDame *= 2;
        }
        eff = get_eff(18); // bộc phá
        if (eff != null) {
            baseDame = (baseDame * eff.param) / 100;
        }
        if (baseDame > 2 && get_eff(21) != null) { // zombie
            baseDame /= 2;
        }
        eff = get_eff(23); // kaido
        if (eff != null) {
            baseDame /= 2;
        }
        if (skill != null && skill.temp != null && (skill.temp.ID == 2057 || skill.temp.ID == 2058)) { // buff bóng tối
            baseDame = (baseDame * 12) / 10;
        }
        if (clan != null && clan.map_create != null && clan.map_create.map_thuLinhBienKhoi != null) {
            if (clan.map_create.map_thuLinhBienKhoi.time >= System.currentTimeMillis() && clan.map_create.map_thuLinhBienKhoi.buff == 1) {
                baseDame = (baseDame * 15) / 10;
            }
        }
        if (baseDame <= 0) baseDame = 100;
        return baseDame + ZUtil.random(20, 80);
    }

    public boolean canAttackPlayer(Player target) {
        if (target == null || target.isdie || target.map == null || this.map == null) return false;
        if (!this.map.can_PK) return false;
        if (master == null) return false;
        if (target.IDPlayer == master.IDPlayer || target.IDPlayer == this.IDPlayer) return false;
        if (target instanceof DeTu && ((DeTu) target).master != null && ((DeTu) target).master.IDPlayer == master.IDPlayer) return false;
        if (target instanceof bot.mercenary.MercenaryBot && ((bot.mercenary.MercenaryBot) target).ownerPlayerId == master.IDPlayer) return false;

        // Trong map PvP (Lôi Đài, Giao Hữu, Siêu Hạng, PvP NPC...): Tuân thủ 100% canAttackTargetPlayer của sư phụ
        if (this.map != null && this.map.map_vp != null) {
            return master.canAttackTargetPlayer(target);
        }

        // Trong Phó Bản Khổng Lồ, Bảo Vệ Pháo Đài, PvP Băng, Chiếm Đảo: Tuân thủ 100% canAttackTargetPlayer của sư phụ
        if (this.map.map_little_garden != null || this.map.template.id == 81
                || this.map.baoVePhaoDai != null || this.map.IsMapBaoVePhaoDai()
                || this.map.pvpBangMapFight != null || this.map.template.id == 120
                || this.map.map_dungeon instanceof ChiemDao
                || (this.map.template != null && ((this.map.template.id >= 254 && this.map.template.id <= 258) || (this.map.template.id >= 261 && this.map.template.id <= 265)))) {
            return master.canAttackTargetPlayer(target);
        }

        if (master.type_pk == 0 || target.type_pk == 0) {
            if (master.type_pk == 3 || target.type_pk == 3) return true;
            return false;
        }
        if (master.type_pk == 3 || target.type_pk == 3) return true;
        if (master.type_pk != target.type_pk && master.type_pk != -1 && target.type_pk != -1) return true;
        if (master.typePirate != 0 && target.typePirate != 0 && master.typePirate != target.typePirate) return true;
        return false;
    }

    @Override
    public boolean canAttackTargetPlayer(Player p) {
        return canAttackPlayer(p);
    }

    /**
     * Kiểm tra xem mục tiêu Mob (quái vật, trùm, trụ) có thể tấn công được không.
     * Tự động nhận diện trụ phe mình trong Chiếm Đảo, Bảo Vệ Pháo Đài để KHÔNG đánh.
     */
    public boolean canAttackMob(Mob mob) {
        if (mob == null || mob.isdie || mob.hp <= 0) return false;
        if (master == null) return false;
        Zone currentZone = this.map != null ? this.map : mob.map;
        if (currentZone == null) return false;

        // 1. Kiểm tra Bản Đồ Chiếm Đảo Bang Hội (Zone 254..258, 261..265, map_dungeon instanceof ChiemDao)
        boolean isChiemDaoMap = (currentZone.map_dungeon instanceof ChiemDao)
                || (currentZone.template != null && ((currentZone.template.id >= 254 && currentZone.template.id <= 258) || (currentZone.template.id >= 261 && currentZone.template.id <= 265)));

        if (isChiemDaoMap && mob.mtemplate != null) {
            int mobId = mob.mtemplate.mob_id;
            if (mobId == 132 || mobId == 133) {
                clan.Clan topClan = Zone.getChiemDaoTopClan(currentZone.template != null ? currentZone.template.id : 0);
                if (currentZone.map_dungeon instanceof ChiemDao && ((ChiemDao) currentZone.map_dungeon).occupyingClan != null) {
                    topClan = ((ChiemDao) currentZone.map_dungeon).occupyingClan;
                }

                boolean isDefender = (master.type_pk == 5 || this.type_pk == 5)
                        || (master.clan != null && topClan != null && (master.clan.id == topClan.id || (master.clan.name != null && master.clan.name.equalsIgnoreCase(topClan.name))));

                if (isDefender || master.clan == null) {
                    return false;
                }

                if (mobId == 133) {
                    int aliveSubTurrets = Zone.getChiemDaoAliveSubTurrets(currentZone);
                    if (aliveSubTurrets > 0) {
                        return false;
                    }
                }
                return true;
            }
        }

        // 2. Bản Đồ Bảo Vệ Pháo Đài
        if (currentZone.IsMapBaoVePhaoDai() || currentZone.baoVePhaoDai != null || (currentZone.template != null && currentZone.template.id >= 267 && currentZone.template.id <= 271)) {
            if (currentZone.baoVePhaoDai != null) {
                boolean isTeamA = (master.type_pk == 4 || this.type_pk == 4)
                        || (master.clan != null && currentZone.baoVePhaoDai.clanA != null && master.clan.equals(currentZone.baoVePhaoDai.clanA));
                boolean isTeamB = (master.type_pk == 5 || this.type_pk == 5)
                        || (master.clan != null && currentZone.baoVePhaoDai.clanB != null && master.clan.equals(currentZone.baoVePhaoDai.clanB));

                if (mob == currentZone.baoVePhaoDai.truThuongATren || mob == currentZone.baoVePhaoDai.truThuongADuoi || mob == currentZone.baoVePhaoDai.truChinhA) {
                    if (isTeamA) return false;
                }
                if (mob == currentZone.baoVePhaoDai.truThuongBTren || mob == currentZone.baoVePhaoDai.truThuongBDuoi || mob == currentZone.baoVePhaoDai.truChinhB) {
                    if (isTeamB) return false;
                }
            }
        }

        return true;
    }

    public void updateWildAI() {
        if (this.map == null || this.isdie) return;
        long now = System.currentTimeMillis();

        // 0. Định kỳ chat thông báo cho người chơi trong map biết cần mở chức năng Sư Phụ
        if (now >= this.nextWildChatTime) {
            this.nextWildChatTime = now + ZUtil.random(15000, 25000);
            if (!this.map.players.isEmpty()) {
                String chatMsg = WILD_DETU_CHAT_MSGS[ZUtil.random(WILD_DETU_CHAT_MSGS.length)];
                core.GlobalChatService.getInstance().chatPublic(this, chatMsg);
            }
        }

        // Nhắc nhở người chơi ở cự ly gần nếu chưa mở chức năng Sư Phụ
        for (int i = 0; i < this.map.players.size(); i++) {
            Player pNear = this.map.players.get(i);
            if (pNear != null && !pNear.isBot && pNear.conn != null && !pNear.isdie && pNear.detu == null) {
                activities.Sudo sNear = activities.Sudo.getSuDo(pNear);
                if (sNear == null || sNear.chucInSudo != 1) {
                    int distNear = Math.abs(pNear.x - this.x) + Math.abs(pNear.y - this.y);
                    if (distNear <= 90 && now >= this.nextWildChatTime - 8000) {
                        this.nextWildChatTime = now + 15000;
                        core.GlobalChatService.getInstance().chatPublic(this, pNear.name + " ơi, bạn cần mở chức năng Sư Phụ tại NPC Garp mới nhận được ta nhé!");
                        break;
                    }
                }
            }
        }

        // 1. Quét tìm người chơi phù hợp (chưa có đệ tử, chưa từ chối)
        if (this.targetPlayer == null || this.targetPlayer.map != this.map || this.targetPlayer.isdie || this.targetPlayer.detu != null) {
            this.targetPlayer = null;
            if (now >= this.nextInteractTime) {
                this.nextInteractTime = now + 2000;
                Player bestCandidate = null;
                int bestDist = 600;
                for (int i = 0; i < this.map.players.size(); i++) {
                    Player p = this.map.players.get(i);
                    if (p != null && !p.isBot && p.conn != null && !p.isdie && p.detu == null && (p.nameDe == null || p.nameDe.isBlank())) {
                        if (this.rejectedPlayers != null && this.rejectedPlayers.contains(p.IDPlayer)) continue;
                        activities.Sudo pSudo = activities.Sudo.getSuDo(p);
                        if (pSudo == null || pSudo.chucInSudo != 1) continue;
                        int dist = Math.abs(p.x - this.x) + Math.abs(p.y - this.y);
                        if (dist < bestDist) {
                            bestDist = dist;
                            bestCandidate = p;
                        }
                    }
                }
                this.targetPlayer = bestCandidate;
            }
        }

        // 2. Nếu có mục tiêu người chơi -> Tiếp cận và gửi đề nghị bái sư
        if (this.targetPlayer != null) {
            int dx = this.targetPlayer.x - this.x;
            int dy = this.targetPlayer.y - this.y;
            int dist = Math.abs(dx) + Math.abs(dy);

            if (dist <= 120) {
                // Đã đến gần người chơi -> Dừng lại và gửi lời mời
                if (now >= this.nextInteractTime) {
                    this.nextInteractTime = now + 15000; // Cooldown 15s trước khi hỏi lại cùng người
                    promptCaptureWildDeTu(this.targetPlayer, this);
                }
            } else {
                // Di chuyển lại gần người chơi
                if (now >= this.nextMoveTime) {
                    this.nextMoveTime = now + 400;
                    short stepX = (short) (dx != 0 ? (dx > 0 ? Math.min(18, dx) : Math.max(-18, dx)) : 0);
                    short stepY = (short) (dy != 0 ? (dy > 0 ? Math.min(12, dy) : Math.max(-12, dy)) : 0);
                    this.x += stepX;
                    this.y += stepY;
                    try {
                        this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                    } catch (Exception ignored) {}
                }
            }
            return;
        }

        // 3. Nếu không có người chơi mục tiêu -> Tuần tra ngẫu nhiên quanh map
        if (now >= this.nextMoveTime) {
            if (this.targetX <= 0 || (Math.abs(this.targetX - this.x) < 20 && Math.abs(this.targetY - this.y) < 20)) {
                this.targetX = (short) ZUtil.random(150, Math.max(200, this.map.template.maxW - 150));
                this.targetY = (short) ZUtil.random(150, Math.max(200, this.map.template.maxH - 150));
                this.nextMoveTime = now + ZUtil.random(2000, 4000);
            } else {
                this.nextMoveTime = now + 400;
                int dx = this.targetX - this.x;
                int dy = this.targetY - this.y;
                short stepX = (short) (dx != 0 ? (dx > 0 ? Math.min(14, dx) : Math.max(-14, dx)) : 0);
                short stepY = (short) (dy != 0 ? (dy > 0 ? Math.min(10, dy) : Math.max(-10, dy)) : 0);
                this.x += stepX;
                this.y += stepY;
                try {
                    this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                } catch (Exception ignored) {}
            }
        }
    }

    public static Item_wear createDefaultItem(int templateId, int levelUp, int typelock, int index, Option... options) {
        Item_wear it = new Item_wear();
        it.template = ItemTemplate3.get_it_by_id((short) templateId);
        it.levelUp = (byte) levelUp;
        it.typelock = (byte) typelock;
        it.index = (short) index;
        it.valueKichAn = -1;
        it.option_item = new ArrayList<>();
        if (options != null) {
            for (Option o : options) {
                it.option_item.add(o);
            }
        }
        it.option_item_2 = new ArrayList<>();
        it.mdakham = new short[0];
        return it;
    }

    public static List<Item_wear> getDefaultEquip(int clazz) {
        List<Item_wear> list = new ArrayList<>();
        switch (clazz) {
            case 1:
                list.add(createDefaultItem(0, 0, 1, 0, new Option(1, 61), new Option(4, 7)));  // Vũ khí (slot 0)
                list.add(createDefaultItem(40, 0, 1, 3, new Option(3, 4), new Option(15, 1))); // Áo (slot 3)
                list.add(createDefaultItem(80, 0, 1, 5, new Option(3, 3), new Option(15, 3))); // Quần (slot 5)
                break;
            case 2:
                list.add(createDefaultItem(8, 0, 1, 0, new Option(1, 72)));                     // Vũ khí (slot 0)
                list.add(createDefaultItem(48, 0, 1, 3, new Option(3, 2), new Option(15, 3))); // Áo (slot 3)
                list.add(createDefaultItem(88, 0, 1, 5, new Option(3, 1), new Option(15, 5))); // Quần (slot 5)
                break;
            case 3:
                list.add(createDefaultItem(16, 0, 1, 0, new Option(1, 73)));                    // Vũ khí (slot 0)
                list.add(createDefaultItem(56, 0, 1, 3, new Option(3, 3), new Option(15, 2))); // Áo (slot 3)
                list.add(createDefaultItem(96, 0, 1, 5, new Option(3, 3), new Option(15, 4))); // Quần (slot 5)
                break;
            case 4:
                list.add(createDefaultItem(24, 0, 1, 0, new Option(1, 55), new Option(23, 18)));// Vũ khí (slot 0)
                list.add(createDefaultItem(64, 0, 1, 3, new Option(3, 2), new Option(15, 3))); // Áo (slot 3)
                list.add(createDefaultItem(104, 0, 1, 5, new Option(3, 1), new Option(15, 5)));// Quần (slot 5)
                break;
            case 5:
                list.add(createDefaultItem(32, 0, 1, 0, new Option(1, 66), new Option(16, 1)));// Vũ khí (slot 0)
                list.add(createDefaultItem(72, 0, 1, 3, new Option(3, 3), new Option(15, 2))); // Áo (slot 3)
                list.add(createDefaultItem(112, 0, 1, 5, new Option(3, 2), new Option(15, 4)));// Quần (slot 5)
                break;
        }
        return list;
    }

    public static List<Skill_info> getDefaultSkills(int clazz) {
        List<Skill_info> list = new ArrayList<>();
        int[] activeSkills = {};
        switch (clazz) {
            case 1: activeSkills = new int[]{0, 20, 40}; break;
            case 2: activeSkills = new int[]{60, 80, 100}; break;
            case 3: activeSkills = new int[]{120, 140, 160}; break;
            case 4: activeSkills = new int[]{180, 200, 220}; break;
            case 5: activeSkills = new int[]{240, 260, 280}; break;
        }
        for (int i = 0; i < activeSkills.length; i++) {
            Skill_info sk = Player.createBotSkillInfo(activeSkills[i]);
            if (sk != null) {
                sk.lvdevil = 0;
                sk.devilpercent = 0;
                list.add(sk);
            }
        }
        int[] passiveSkills = {300, 305, 310, 315, 320, 325, 552, 557, 667};
        // Add class-specific passive skills
        int classPassive1 = 0, classPassive2 = 0;
        switch (clazz) {
            case 1: classPassive1 = 375; classPassive2 = 487; break;
            case 2: classPassive1 = 395; classPassive2 = 492; break;
            case 3: classPassive1 = 415; classPassive2 = 497; break;
            case 4: classPassive1 = 435; classPassive2 = 502; break;
            case 5: classPassive1 = 455; classPassive2 = 507; break;
        }
        int[] allPassives = new int[passiveSkills.length + 2];
        System.arraycopy(passiveSkills, 0, allPassives, 0, passiveSkills.length);
        allPassives[passiveSkills.length] = classPassive1;
        allPassives[passiveSkills.length + 1] = classPassive2;
        
        for (int skId : allPassives) {
            if (skId <= 0) continue;
            Skill_info sk = Player.createBotSkillInfo(skId);
            if (sk != null) {
                sk.lvdevil = 0;
                sk.devilpercent = 0;
                list.add(sk);
            }
        }

        // Kỹ năng nghề nghiệp phe Đại Chiến Thế Giới (Hải Tặc 691, Hải Quân 697, Cách Mạng 803)
        int[] factionSkills = {691, 697, 803};
        for (int skId : factionSkills) {
            Skill_info sk = Player.createBotSkillInfo(skId);
            if (sk != null) {
                sk.lvdevil = 0;
                sk.devilpercent = 0;
                list.add(sk);
            }
        }
        return list;
    }

    @Override
    public void setDefaultFashion(int clazz) {
        super.setDefaultFashion(clazz);
    }

    @Override
    public boolean setup() {
        return setup("players_detu");
    }

    @Override
    public boolean setup(String tableName) {
        if (this.master == null) {
            return false;
        }
        String cacheKey = "players_detu_" + master.IDPlayer;
        Map<String, Object> cachedData = database.CacheManager.gI().get(cacheKey);
        if (cachedData != null) {
            try {
                loadFromResultSet(new database.DbResultRow(cachedData));
                return true;
            } catch (Exception e) {
                System.err.println("Error loading DeTu from cache: " + e.getMessage() + ", falling back to DB...");
            }
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DbManager.gI().getConnect();
            
            // 1. Try to load from players_detu by owner_id
            ps = connection.prepareStatement("SELECT * FROM `players_detu` WHERE `owner_id` = ? LIMIT 1;");
            ps.setInt(1, master.IDPlayer);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                // Convert ResultSet to Map
                Map<String, Object> rowMap = new java.util.LinkedHashMap<>();
                java.sql.ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                for (int i = 1; i <= colCount; i++) {
                    rowMap.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                database.CacheManager.gI().put(cacheKey, rowMap);

                loadFromResultSet(new database.DbResultRow(rs));
                return true;
            }
            rs.close();
            ps.close();
            
            // 2. If not found in players_detu, try one-time migration from players table
            String lookupName = (master.nameDe != null && !master.nameDe.isBlank()) ? master.nameDe : (master.name + "_detu");
            ps = connection.prepareStatement("SELECT * FROM `players` WHERE `name` = ? LIMIT 1;");
            ps.setString(1, lookupName);
            rs = ps.executeQuery();
            
            if (!rs.next() && !lookupName.equals(master.name + "_detu")) {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                ps = connection.prepareStatement("SELECT * FROM `players` WHERE `name` = ? LIMIT 1;");
                ps.setString(1, master.name + "_detu");
                rs = ps.executeQuery();
            }

            if (rs != null && rs.next()) {
                loadFromResultSet(new database.DbResultRow(rs));
                int oldId = rs.getInt("id");
                rs.close();
                ps.close();
                
                // Save to players_detu
                saveDeTuToDb(connection);
                
                // Delete from players
                try (Statement st = connection.createStatement()) {
                    st.execute("DELETE FROM `players` WHERE `id` = " + oldId);
                }
                // System.out.println("Migrated disciple " + this.name + " from players to players_detu.");
                return true;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void loadFromResultSet(database.DbResultRow rs) throws Exception {
        IDPlayer = rs.getInt("id");
        index_map = (short) (master.IDPlayer + 25_000);
        clazz = rs.getByte("clazz");
        
        // 0. name
        String dtName = rs.getString("name");
        if (dtName != null && !dtName.isBlank()) {
            this.name = dtName;
        } else if (this.name == null || this.name.isBlank()) {
            if (this.master != null && this.master.nameDe != null && !this.master.nameDe.isBlank()) {
                this.name = this.master.nameDe;
            } else if (this.master != null) {
                this.name = this.master.name;
            }
        }
        if (this.master != null) {
            this.master.nameDe = this.name;
        }
        
        // 1. level
        {
            String raw = rs.getString("level");
            JSONObject o = parseObj(raw);
            if (o != null) {
                level = jShort(o, "lv", (short) 1);
                exp = jLong(o, "exp", 0L);
                thongthao = jShort(o, "tt", (short) 0);
            } else {
                JSONArray a = parseArr(raw);
                if (a != null && a.size() >= 3) {
                    level = Short.parseShort(a.get(0).toString());
                    exp = Long.parseLong(a.get(1).toString());
                    thongthao = Short.parseShort(a.get(2).toString());
                }
            }
        }

        // 2. body
        {
            String raw = rs.getString("body");
            JSONObject o = parseObj(raw);
            if (o != null) {
                head = jShort(o, "head", (short) 0);
                hair = jShort(o, "hair", (short) 0);
                part_body = jShort(o, "body", (short) -1);
                part_leg = jShort(o, "leg", (short) -1);
                part_ring = jShort(o, "ring", (short) -1);
                part_weapon = jShort(o, "weapon", (short) -1);
            } else {
                JSONArray a = parseArr(raw);
                if (a != null && a.size() >= 2) {
                    head = Short.parseShort(a.get(0).toString());
                    hair = Short.parseShort(a.get(1).toString());
                    part_body = a.size() > 2 ? Short.parseShort(a.get(2).toString()) : -1;
                    part_leg = a.size() > 3 ? Short.parseShort(a.get(3).toString()) : -1;
                    part_ring = a.size() > 4 ? Short.parseShort(a.get(4).toString()) : -1;
                    part_weapon = a.size() > 5 ? Short.parseShort(a.get(5).toString()) : -1;
                }
            }
            // Sanitize head & hair values
            if (head >= 1 && head <= 4) {
                head = 0;
            }
            if (head < 0) {
                head = 0;
            }
            if (hair <= 0) {
                switch (clazz) {
                    case 1: hair = 1; break;
                    case 2: hair = 24; break;
                    case 3: hair = 28; break;
                    case 4: hair = 32; break;
                    case 5: hair = 36; break;
                    default: hair = 1; break;
                }
            }
        }

        // 3. potential
        {
            String raw = rs.getString("potential");
            JSONObject o = parseObj(raw);
            list_op_thongthao = new ArrayList<>();
            if (o != null) {
                pointAttribute = jLong(o, "attr", 0L);
                point1 = jLong(o, "p1", 0L);
                point2 = jLong(o, "p2", 0L);
                point3 = jLong(o, "p3", 0L);
                point4 = jLong(o, "p4", 0L);
                point5 = jLong(o, "p5", 0L);
                pointAttributeThongThao = jLong(o, "tt", 0L);
                JSONArray ops = (JSONArray) o.get("op_tt");
                if (ops != null) {
                    for (int i = 0; i < ops.size(); i++) {
                        Object elem = ops.get(i);
                        if (elem instanceof JSONArray) {
                            JSONArray js_in_1 = (JSONArray) elem;
                            if (js_in_1.size() >= 2) {
                                list_op_thongthao.add(new Option(Byte.parseByte(js_in_1.get(0).toString()), Integer.parseInt(js_in_1.get(1).toString())));
                            }
                        } else if (elem instanceof JSONObject) {
                            JSONObject jso = (JSONObject) elem;
                            list_op_thongthao.add(new Option((byte) jInt(jso, "id", 0), jInt(jso, "param", 0)));
                        }
                    }
                }
            } else {
                JSONArray a = parseArr(raw);
                if (a != null && a.size() >= 7) {
                    pointAttribute = Long.parseLong(a.get(0).toString());
                    point1 = Long.parseLong(a.get(1).toString());
                    point2 = Long.parseLong(a.get(2).toString());
                    point3 = Long.parseLong(a.get(3).toString());
                    point4 = Long.parseLong(a.get(4).toString());
                    point5 = Long.parseLong(a.get(5).toString());
                    pointAttributeThongThao = Long.parseLong(a.get(6).toString());
                    if (a.size() > 7) {
                        JSONArray js_in = (JSONArray) a.get(7);
                        if (js_in != null) {
                            for (int i = 0; i < js_in.size(); i++) {
                                JSONArray js_in_1 = (JSONArray) js_in.get(i);
                                list_op_thongthao.add(new Option(Byte.parseByte(js_in_1.get(0).toString()), Integer.parseInt(js_in_1.get(1).toString())));
                            }
                        }
                    }
                }
            }
        }

        this.tieuRuby = 0;
        this.wanted_point = 0;

        // 4. it_body
        this.item = new Item(this);
        this.item.it_body = new Item_wear[itemz.Item.MAX_BODY];
        {
            String raw = rs.getString("it_body");
            JSONArray a = parseArr(raw);
            if (a != null) {
                for (int i = 0; i < a.size(); i++) {
                    Object elem = a.get(i);
                    Item_wear w = new Item_wear();
                    Item.readUpdateItem(elem instanceof String ? (String) elem : ((org.json.simple.JSONAware) elem).toJSONString(), w);
                    if (w.template != null && ItemTemplate3.get_it_by_id(w.template.id) != null) {
                        if (w.template.id == 11000 || w.template.typeEquip == 6 || w.index == 6) {
                            w.index = 6;
                        } else if (w.template.typeEquip == 7) {
                            w.index = 7;
                        } else if (w.template.typeEquip == 3 && w.index == 1) {
                            w.index = 3;
                        } else if (w.template.typeEquip == 5 && w.index == 3) {
                            w.index = 5;
                        } else if (w.template.typeEquip >= 0 && w.template.typeEquip < this.item.it_body.length) {
                            w.index = w.template.typeEquip;
                        }
                        if (w.index >= 0 && w.index < this.item.it_body.length) {
                            boolean canWear = true;
                            if (this.level < w.template.level) {
                                canWear = false;
                            }
                            if (w.template.clazz != 0 && this.clazz != w.template.clazz) {
                                canWear = false;
                            }
                            if (canWear) {
                                this.item.it_body[w.index] = w;
                                if (w.index == 6 || w.template.typeEquip == 6 || w.template.id == 11000) {
                                    this.item.it_heart = w;
                                    this.item.it_heart.typelock = 1;
                                    this.item.it_body[6] = w;
                                }
                            } else {
                                this.item.add_item_bag3(w);
                            }
                        }
                    }
                }
            }
            // Đồng bộ an toàn Quả Tim của đệ tử
            if (this.item.it_heart != null) {
                this.item.it_heart.index = 6;
                this.item.it_heart.typelock = 1;
                this.item.it_body[6] = this.item.it_heart;
            } else if (this.item.it_body.length > 6 && this.item.it_body[6] != null && (this.item.it_body[6].template.typeEquip == 6 || this.item.it_body[6].template.id == 11000)) {
                this.item.it_heart = this.item.it_body[6];
                this.item.it_heart.index = 6;
                this.item.it_heart.typelock = 1;
            }
            // Auto-populate default equips if disciple has empty it_body
            boolean hasAnyEquip = false;
            for (Item_wear itW : this.item.it_body) {
                if (itW != null) {
                    hasAnyEquip = true;
                    break;
                }
            }
            if (!hasAnyEquip) {
                List<Item_wear> equips = getDefaultEquip(this.clazz);
                for (Item_wear w : equips) {
                    if (w != null && w.index >= 0 && w.index < this.item.it_body.length) {
                        this.item.it_body[w.index] = w;
                        if (w.index == 6) {
                            this.item.it_heart = w;
                        }
                    }
                }
            }
        }

        // Share inventory bag/box and saves with master to prevent inventory bugs
        if (this.master != null && this.master.item != null) {
            this.item.bag3 = this.master.item.bag3;
            this.item.box3 = this.master.item.box3;
            this.item.bag47 = this.master.item.bag47;
            this.item.box47 = this.master.item.box47;
            this.item.save_item_wear = this.master.item.save_item_wear;
            this.item.save_item_47 = this.master.item.save_item_47;
        } else {
            this.item.bag3 = new Item_wear[this.item.max_bag];
            this.item.box3 = new Item_wear[this.item.max_box];
            this.item.bag47 = new ArrayList<>();
            this.item.box47 = new ArrayList<>();
        }

        // 5. skill
        this.skill_point = new ArrayList<>();
        {
            String raw = rs.getString("skill");
            JSONArray a = parseArr(raw);
            if (a != null) {
                for (int i = 0; i < a.size(); i++) {
                    Object elem = a.get(i);
                    JSONObject o = elem instanceof JSONObject ? (JSONObject) elem : null;
                    if (o != null) {
                        Skill_info sk = new Skill_info();
                        sk.exp = jLong(o, "exp", 0L);
                        sk.temp = Skill_Template.get_temp(jShort(o, "id", (short)0), sk.exp);
                        sk.lvdevil = (byte) jInt(o, "lv", 0);
                        sk.devilpercent = (byte) jInt(o, "pct", 0);
                        if (sk.temp != null) this.skill_point.add(sk);
                    } else if (elem instanceof JSONArray) {
                        JSONArray js2 = (JSONArray) elem;
                        if (js2.size() >= 2) {
                            Skill_info sk = new Skill_info();
                            sk.exp = Long.parseLong(js2.get(1).toString());
                            sk.temp = Skill_Template.get_temp(Short.parseShort(js2.get(0).toString()), sk.exp);
                            if (js2.size() > 2) sk.lvdevil = Byte.parseByte(js2.get(2).toString());
                            if (js2.size() > 3) sk.devilpercent = Byte.parseByte(js2.get(3).toString());
                            if (sk.temp != null) this.skill_point.add(sk);
                        }
                    }
                }
            }
            if (this.skill_point.isEmpty()) {
                this.skill_point = getDefaultSkills(this.clazz);
            }
        }

        // 6. eff
        this.list_eff = new CopyOnWriteArrayList<>();
        {
            String raw = rs.getString("eff");
            JSONArray a = parseArr(raw);
            if (a != null) {
                for (int i = 0; i < a.size(); i++) {
                    Object elem = a.get(i);
                    JSONObject o = elem instanceof JSONObject ? (JSONObject) elem : null;
                    if (o != null) {
                        list_eff.add(new EffTemplate(
                                (byte) jInt(o, "id", 0),
                                jInt(o, "param", 0),
                                System.currentTimeMillis() + jLong(o, "time", 0L)
                        ));
                    } else if (elem instanceof JSONArray) {
                        JSONArray js2 = (JSONArray) elem;
                        if (js2.size() >= 3) {
                            list_eff.add(new EffTemplate(
                                    Byte.parseByte(js2.get(0).toString()),
                                    Integer.parseInt(js2.get(1).toString()),
                                    System.currentTimeMillis() + Long.parseLong(js2.get(2).toString())
                            ));
                        }
                    }
                }
            }
        }

        // 7. fashion
        this.itfashionP = new ArrayList<>();
        this.fashion    = new ArrayList<>();
        this.itemboat   = new ArrayList<>();
        {
            String raw = rs.getString("fashion");
            JSONObject fo = parseObj(raw);
            if (fo != null) {
                // Modern JSON format with "fp", "f2", "boat"
                JSONArray fpArr = jArr(fo, "fp");
                if (fpArr != null) {
                    for (Object o : fpArr) {
                        JSONObject eo = (JSONObject) o;
                        itfashionP.add(new ItemFashionP(
                            jShort(eo, "id", (short) 0), jShort(eo, "icon", (short) 0),
                            jByte(eo, "cat", (byte) 0), jBool(eo, "use", false)));
                    }
                }
                JSONArray f2Arr = jArr(fo, "f2");
                if (f2Arr != null) {
                    boolean hasEquippedFashion = false;
                    for (Object o : f2Arr) {
                        JSONObject eo = (JSONObject) o;
                        ItemFashionP2 f2 = new ItemFashionP2();
                        f2.id = jShort(eo, "id", (short) 0);
                        boolean wantUse = jBool(eo, "use", false);
                        if (wantUse && !hasEquippedFashion) {
                            f2.is_use = true;
                            hasEquippedFashion = true;
                        } else {
                            f2.is_use = false;
                        }
                        f2.level = jByte(eo, "lv", (byte) 0);
                        f2.expires = jLong(eo, "exp", -1L);
                        fashion.add(f2);
                    }
                }
                JSONArray boatArr = jArr(fo, "boat");
                if (boatArr != null) {
                    for (Object o : boatArr) {
                        JSONObject eo = (JSONObject) o;
                        ItemBoatP b = new ItemBoatP();
                        b.id = jByte(eo, "id", (byte) 0);
                        b.is_use = jBool(eo, "use", false);
                        itemboat.add(b);
                    }
                }
            } else {
                // Legacy format: array of ItemFashionP2 or array[3]
                JSONArray a = parseArr(raw);
                if (a != null) {
                    if (!a.isEmpty() && a.get(0) instanceof JSONArray) {
                        // Old 3-array format [[itfashionP],[fashion],[itemboat]]
                        JSONArray fp = (JSONArray) a.get(0);
                        for (int i = 0; i < fp.size(); i++) {
                            JSONArray t = (JSONArray) fp.get(i);
                            itfashionP.add(new ItemFashionP(
                                Short.parseShort(t.get(1).toString()), Short.parseShort(t.get(2).toString()),
                                Byte.parseByte(t.get(0).toString()), Byte.parseByte(t.get(3).toString()) == 1));
                        }
                        if (a.size() > 1) {
                            JSONArray f2arr = (JSONArray) a.get(1);
                            boolean hasEquippedFashion = false;
                            for (int i = 0; i < f2arr.size(); i++) {
                                JSONArray t = (JSONArray) JSONValue.parse(f2arr.get(i).toString());
                                ItemFashionP2 f2 = new ItemFashionP2();
                                f2.id = Short.parseShort(t.get(0).toString());
                                boolean wantUse = Byte.parseByte(t.get(1).toString()) == 1;
                                if (wantUse && !hasEquippedFashion) {
                                    f2.is_use = true;
                                    hasEquippedFashion = true;
                                } else {
                                    f2.is_use = false;
                                }
                                f2.level = Byte.parseByte(t.get(2).toString());
                                f2.expires = t.size() >= 4 ? Long.parseLong(t.get(3).toString()) : -1L;
                                fashion.add(f2);
                            }
                        }
                        if (a.size() > 2) {
                            JSONArray boats = (JSONArray) a.get(2);
                            for (int i = 0; i < boats.size(); i++) {
                                JSONArray t = (JSONArray) JSONValue.parse(boats.get(i).toString());
                                ItemBoatP b = new ItemBoatP();
                                b.id = Byte.parseByte(t.get(0).toString());
                                b.is_use = Byte.parseByte(t.get(1).toString()) == 1;
                                itemboat.add(b);
                            }
                        }
                    } else {
                        // Legacy single array of ItemFashionP2 [{id,use,lv}...]
                        boolean hasEquippedFashion = false;
                        for (int i = 0; i < a.size(); i++) {
                            Object elem = a.get(i);
                            JSONObject o = elem instanceof JSONObject ? (JSONObject) elem : null;
                            if (o != null) {
                                ItemFashionP2 nn = new ItemFashionP2();
                                nn.id = jShort(o, "id", (short) 0);
                                boolean wantUse = jBool(o, "use", false);
                                if (wantUse && !hasEquippedFashion) {
                                    nn.is_use = true;
                                    hasEquippedFashion = true;
                                } else {
                                    nn.is_use = false;
                                }
                                nn.level = (byte) jInt(o, "lv", 0);
                                fashion.add(nn);
                            }
                        }
                    }
                }
            }
            // Auto-init default fashion (hair & face) if itfashionP is empty
            if (this.itfashionP.isEmpty()) {
                this.setDefaultFashion(this.clazz);
            }
        }

        this.updateParts();

        this.list_quest = new ArrayList<>();

        // 8. site
        {
            String raw = rs.getString("site");
            JSONObject o = parseObj(raw);
            if (o != null) {
                try {
                    Zone[] maps = Zone.getMapByID(jInt(o, "map", 1));
                    byte zone_id = (byte) jInt(o, "zone", 0);
                    int zone_goto = zone_id < maps.length ? zone_id : 0;
                    this.map = maps[zone_goto];
                } catch (Exception e) {
                    this.map = Zone.getMapByID(1)[0];
                }
                this.hp = jInt(o, "hp", -1);
                this.mp = jInt(o, "mp", -1);
                x = jShort(o, "x", (short) 0);
                y = jShort(o, "y", (short) 0);
                is_show_hat = jBool(o, "hat", true);
                is_show_weapon = jBool(o, "wpn", true);
                is_hide_fashion_hair = jBool(o, "hfhair", false);
                is_hide_fashion_head = jBool(o, "hfhead", false);
                pointPk = jInt(o, "pk", 0);
                this.detuStatus = jInt(o, "status", STATUS_FOLLOW);
            } else {
                JSONArray a = parseArr(raw);
                if (a != null && a.size() >= 8) {
                    try {
                        Zone[] maps = Zone.getMapByID(Integer.parseInt(a.get(0).toString()));
                        byte zone_id = Byte.parseByte(a.get(1).toString());
                        int zone_goto = zone_id < maps.length ? zone_id : 0;
                        this.map = maps[zone_goto];
                    } catch (Exception e) {
                        this.map = Zone.getMapByID(1)[0];
                    }
                    this.hp = Integer.parseInt(a.get(2).toString());
                    this.mp = Integer.parseInt(a.get(3).toString());
                    x = Short.parseShort(a.get(4).toString());
                    y = Short.parseShort(a.get(5).toString());
                    is_show_hat = Byte.parseByte(a.get(6).toString()) == 1;
                    pointPk = Integer.parseInt(a.get(7).toString());
                } else {
                    this.map = Zone.getMapByID(1)[0];
                }
            }
        }

        this.ability = new Ability(this);
        if (this.master != null) {
            this.clan = master.clan;
            this.typePirate = master.typePirate;
            this.type_pk = master.type_pk;
        } else {
            this.type_pk = -1;
        }
        this.setAbility();
        int hpMax = this.ability.get_hp_max(true);
        int mpMax = this.ability.get_mp_max(true);
        if (this.hp <= 0 || this.hp > hpMax) {
            this.hp = hpMax;
        }
        if (this.mp <= 0 || this.mp > mpMax) {
            this.mp = mpMax;
        }

        // 9. rms
        this.rms = new byte[11][];
        for (int i = 0; i < 11; i++) this.rms[i] = new byte[0];
        try {
            String rawRms = rs.getString("rms");
            if (rawRms != null && !rawRms.isEmpty()) {
                JSONArray arr = parseArr(rawRms);
                if (arr != null) {
                    for (int i = 0; i < arr.size() && i < this.rms.length; i++) {
                        Object obj = arr.get(i);
                        if (obj instanceof JSONArray) {
                            JSONArray row = (JSONArray) obj;
                            this.rms[i] = new byte[row.size()];
                            for (int j = 0; j < row.size(); j++) this.rms[i][j] = Byte.parseByte(row.get(j).toString());
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        this.ensureRms0();
    }

    public synchronized void saveDeTuToDb(Connection conn) throws SQLException {
        boolean isUpdate = this.IDPlayer > 0;
        if (!isUpdate && this.master != null && this.master.IDPlayer > 0) {
            // Check if record already exists by owner_id to prevent duplicate rows
            try (PreparedStatement checkPs = conn.prepareStatement("SELECT `id` FROM `players_detu` WHERE `owner_id` = ? LIMIT 1")) {
                checkPs.setInt(1, this.master.IDPlayer);
                try (ResultSet checkRs = checkPs.executeQuery()) {
                    if (checkRs.next()) {
                        this.IDPlayer = checkRs.getInt("id");
                        isUpdate = true;
                    }
                }
            } catch (Exception ignored) {}
        }
        if (this.name == null || this.name.isBlank()) {
            if (this.master != null && this.master.nameDe != null && !this.master.nameDe.isBlank()) {
                this.name = this.master.nameDe;
            } else if (this.master != null) {
                this.name = this.master.name;
            }
        }
        String sql = isUpdate 
            ? "UPDATE `players_detu` SET `clazz` = ?, `body` = ?, `level` = ?, `exp` = ?, `potential` = ?, `it_body` = ?, `skill` = ?, `eff` = ?, `fashion` = ?, `site` = ?, `name` = ?, `rms` = ? WHERE `id` = ?"
            : "INSERT INTO `players_detu` (`clazz`, `body`, `level`, `exp`, `potential`, `it_body`, `skill`, `eff`, `fashion`, `site`, `owner_id`, `name`, `rms`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setByte(1, this.clazz);
            
            // 2. body
            {
                JSONObject o = new JSONObject();
                short safeHead = (this.head >= 1 && this.head <= 4) ? 0 : (this.head >= 0 ? this.head : 0);
                this.head = safeHead;
                o.put("head", safeHead);
                
                short safeHair = this.hair;
                if (safeHair <= 0) {
                    switch (this.clazz) {
                        case 1: safeHair = 1; break;
                        case 2: safeHair = 24; break;
                        case 3: safeHair = 28; break;
                        case 4: safeHair = 32; break;
                        case 5: safeHair = 36; break;
                        default: safeHair = 1; break;
                    }
                }
                this.hair = safeHair;
                o.put("hair", safeHair);
                
                this.updateParts();
                o.put("weapon", this.part_weapon);
                o.put("body", this.part_body);
                o.put("leg", this.part_leg);
                o.put("ring", this.part_ring);
                
                ps.setString(2, o.toJSONString());
            }
            
            // 3. level
            {
                JSONObject o = new JSONObject();
                o.put("lv", this.level);
                o.put("exp", this.exp);
                o.put("tt", this.thongthao);
                ps.setString(3, o.toJSONString());
            }
            
            ps.setLong(4, this.exp);
            
            // 5. potential
            {
                JSONObject o = new JSONObject();
                o.put("attr", this.pointAttribute);
                o.put("p1", this.point1);
                o.put("p2", this.point2);
                o.put("p3", this.point3);
                o.put("p4", this.point4);
                o.put("p5", this.point5);
                o.put("tt", this.pointAttributeThongThao);
                JSONArray ops = new JSONArray();
                for (int i = 0; i < this.list_op_thongthao.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", this.list_op_thongthao.get(i).id);
                    jo.put("param", this.list_op_thongthao.get(i).getParam());
                    ops.add(jo);
                }
                o.put("op_tt", ops);
                ps.setString(5, o.toJSONString());
            }
            
            // 6. it_body
            {
                JSONArray js = new JSONArray();
                if (this.item != null && this.item.it_body != null) {
                    if (this.item.it_heart != null && this.item.it_heart.template != null && ItemTemplate3.get_it_by_id(this.item.it_heart.template.id) != null) {
                        this.item.it_heart.index = 6;
                        this.item.it_body[6] = this.item.it_heart;
                    } else if (this.item.it_body.length > 6 && this.item.it_body[6] != null && (this.item.it_body[6].template != null && (this.item.it_body[6].template.typeEquip == 6 || this.item.it_body[6].template.id == 11000))) {
                        this.item.it_heart = this.item.it_body[6];
                        this.item.it_heart.index = 6;
                    }
                    for (int i = 0; i < this.item.it_body.length; i++) {
                        Item_wear it = (i == 6 && this.item.it_heart != null) ? this.item.it_heart : this.item.it_body[i];
                        if (it != null && it.template != null && ItemTemplate3.get_it_by_id(it.template.id) != null) {
                            it.index = (short) i;
                            JSONObject js_temp = Item.it_data_to_json(it);
                            if (js_temp != null && !js_temp.isEmpty()) js.add(js_temp);
                        }
                    }
                }
                ps.setString(6, js.toJSONString());
            }
            
            // 7. skill
            {
                JSONArray js = new JSONArray();
                for (int i = 0; i < this.skill_point.size(); i++) {
                    JSONObject o = new JSONObject();
                    o.put("id", this.skill_point.get(i).temp.indexSkillInServer);
                    o.put("exp", this.skill_point.get(i).exp);
                    o.put("lv", this.skill_point.get(i).lvdevil);
                    o.put("pct", this.skill_point.get(i).devilpercent);
                    js.add(o);
                }
                ps.setString(7, js.toJSONString());
            }
            
            // 8. eff
            {
                JSONArray js = new JSONArray();
                if (this.list_eff != null) {
                    for (EffTemplate eff_temp : this.list_eff) {
                        if (eff_temp != null && EffTemplate.check_eff_can_save(eff_temp.id)) {
                            JSONObject o = new JSONObject();
                            o.put("id", eff_temp.id);
                            o.put("param", eff_temp.param);
                            o.put("time", eff_temp.time - System.currentTimeMillis());
                            js.add(o);
                        }
                    }
                }
                ps.setString(8, js.toJSONString());
            }
            
            // 9. fashion (save itfashionP, fashion, and itemboat)
            {
                JSONObject fo = new JSONObject();
                JSONArray fp = new JSONArray();
                if (this.itfashionP != null) {
                    for (ItemFashionP f : this.itfashionP) {
                        if (f != null) {
                            JSONObject o = new JSONObject();
                            o.put("cat", f.category);
                            o.put("id", f.id);
                            o.put("icon", f.icon);
                            o.put("use", f.is_use ? 1 : 0);
                            fp.add(o);
                        }
                    }
                }
                JSONArray f2 = new JSONArray();
                if (this.fashion != null) {
                    for (ItemFashionP2 f : this.fashion) {
                        if (f != null) {
                            JSONObject o = new JSONObject();
                            o.put("id", f.id);
                            o.put("use", f.is_use ? 1 : 0);
                            o.put("lv", f.level);
                            o.put("exp", f.expires);
                            f2.add(o);
                        }
                    }
                }
                JSONArray boats = new JSONArray();
                if (this.itemboat != null) {
                    for (ItemBoatP b : this.itemboat) {
                        if (b != null) {
                            JSONObject o = new JSONObject();
                            o.put("id", b.id);
                            o.put("use", b.is_use ? 1 : 0);
                            boats.add(o);
                        }
                    }
                }
                fo.put("fp", fp);
                fo.put("f2", f2);
                fo.put("boat", boats);
                ps.setString(9, fo.toJSONString());
            }
            
            // 10. site
            {
                JSONObject o = new JSONObject();
                o.put("map", this.map != null ? this.map.template.id : 1);
                o.put("zone", this.map != null ? this.map.zone_id : 0);
                o.put("hp", this.hp);
                o.put("mp", this.mp);
                o.put("x", this.x);
                o.put("y", this.y);
                o.put("hat", this.is_show_hat);
                o.put("wpn", this.is_show_weapon);
                o.put("hfhair", this.is_hide_fashion_hair);
                o.put("hfhead", this.is_hide_fashion_head);
                o.put("pk", this.pointPk);
                o.put("status", this.detuStatus);
                ps.setString(10, o.toJSONString());
            }
            
            String detuName = (this.name != null && !this.name.isBlank()) ? this.name : (master != null ? master.name : "");
            if (this.master != null) {
                this.master.nameDe = detuName;
            }
            String rmsJson = "[]";
            if (this.rms != null) {
                JSONArray a = new JSONArray();
                for (byte[] row : this.rms) {
                    if (row == null) { a.add(new JSONArray()); continue; }
                    JSONArray r = new JSONArray(); for (byte b : row) r.add(b); a.add(r);
                }
                rmsJson = a.toJSONString();
            }

            if (isUpdate) {
                ps.setString(11, detuName);
                ps.setString(12, rmsJson);
                ps.setInt(13, this.IDPlayer);
            } else {
                ps.setInt(11, master.IDPlayer);
                ps.setString(12, detuName);
                ps.setString(13, rmsJson);
            }
            
            ps.executeUpdate();
            if (!isUpdate) {
                try (java.sql.ResultSet rsKey = ps.getGeneratedKeys()) {
                    if (rsKey.next()) {
                        this.IDPlayer = rsKey.getInt(1);
                    }
                }
            }
        }
        database.CacheManager.gI().remove("players_detu_" + master.IDPlayer);
    }

    public synchronized int saveDeTu(boolean print) {
        if (this.IDPlayer <= 0 && (this.master == null || this.master.IDPlayer <= 0)) return 1;
        Connection conn = null;
        try {
            conn = DbManager.gI().getConnect();
            saveDeTuToDb(conn);
            if (print) {
                // System.out.println("Saved disciple " + this.name + " to database.");
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static DeTu createNewDeTu(Player master, int clazz, short head, short hair) throws Exception {
        String nameDe = master.name;
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DbManager.gI().getConnect();
            try (Statement st = connection.createStatement()) {
                st.execute("DELETE FROM `players_detu` WHERE `owner_id` = " + master.IDPlayer);
            } catch (Exception ignored) {}
            database.CacheManager.gI().remove("players_detu_" + master.IDPlayer);

            String sql = "INSERT INTO `players_detu` (`owner_id`, `name`, `clazz`) VALUES (?, ?, ?)";
            ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, master.IDPlayer);
            ps.setString(2, nameDe);
            ps.setInt(3, clazz);
            ps.executeUpdate();

            int generatedId = 0;
            try (ResultSet rsKey = ps.getGeneratedKeys()) {
                if (rsKey.next()) {
                    generatedId = rsKey.getInt(1);
                }
            }

            master.nameDe = nameDe;
            DeTu pCreate = new DeTu(nameDe, master);
            pCreate.IDPlayer = generatedId;
            pCreate.index_map = (short) (master.IDPlayer + 25_000);
            pCreate.clazz = (byte) clazz;
            
            // Set default head and hair
            if (head < 0 || (head >= 1 && head <= 4)) head = 0;
            if (hair <= 0) {
                switch (clazz) {
                    case 1: hair = 1; break;
                    case 2: hair = 24; break;
                    case 3: hair = 28; break;
                    case 4: hair = 32; break;
                    case 5: hair = 36; break;
                    default: hair = 1; break;
                }
            }
            pCreate.head = head;
            pCreate.hair = hair;
            pCreate.part_body = -1;
            pCreate.part_leg = -1;
            pCreate.part_ring = -1;
            pCreate.part_weapon = -1;

            pCreate.level = 1;
            pCreate.exp = 0;
            pCreate.thongthao = 0;

            pCreate.pointAttribute = 5;
            pCreate.point1 = 1;
            pCreate.point2 = 1;
            pCreate.point3 = 1;
            pCreate.point4 = 1;
            pCreate.point5 = 1;
            pCreate.pointAttributeThongThao = 0;
            pCreate.list_op_thongthao = new ArrayList<>();

            pCreate.item = new Item(pCreate);
            pCreate.item.it_body = new Item_wear[itemz.Item.MAX_BODY];
            if (master != null && master.item != null) {
                pCreate.item.bag3 = master.item.bag3;
                pCreate.item.box3 = master.item.box3;
                pCreate.item.bag47 = master.item.bag47;
                pCreate.item.box47 = master.item.box47;
                pCreate.item.save_item_wear = master.item.save_item_wear;
                pCreate.item.save_item_47 = master.item.save_item_47;
            } else {
                pCreate.item.bag3 = new Item_wear[pCreate.item.max_bag];
                pCreate.item.box3 = new Item_wear[pCreate.item.max_box];
                pCreate.item.bag47 = new ArrayList<>();
                pCreate.item.box47 = new ArrayList<>();
            }

            List<Item_wear> equips = getDefaultEquip(clazz);
            for (Item_wear w : equips) {
                if (w.index >= 0 && w.index < pCreate.item.it_body.length) {
                    pCreate.item.it_body[w.index] = w;
                    if (w.index == 6) {
                        pCreate.item.it_heart = w;
                    }
                }
            }

            pCreate.skill_point = getDefaultSkills(clazz);
            pCreate.setDefaultFashion(clazz);
            pCreate.updateParts();
            pCreate.list_eff = new CopyOnWriteArrayList<>();
            pCreate.clan = master.clan;
            pCreate.typePirate = master.typePirate;
            pCreate.type_pk = master.type_pk;

            pCreate.map = Zone.getMapByID(1)[0];
            pCreate.hp = -1;
            pCreate.mp = -1;
            pCreate.setin4();
            pCreate.setUsername("a_test");
            pCreate.x = -1;
            pCreate.y = -1;

            pCreate.fashion = new ArrayList<>();
            if (master.fashion != null && !master.fashion.isEmpty()) {
                for (int i = 0; i < master.fashion.size(); i++) {
                    template.ItemFashionP2 get = master.fashion.get(i);
                    if (get != null) {
                        template.ItemFashionP2 nn = new template.ItemFashionP2();
                        nn.id = get.id;
                        nn.is_use = false;
                        nn.level = get.level;
                        pCreate.fashion.add(nn);
                    }
                }
            }

            for (int i = 0; i < pCreate.skill_point.size(); i++) {
                if (pCreate.skill_point.get(i).temp.ID == 1 && pCreate.skill_point.get(i).temp.Lv_RQ == -1) {
                    Skill_Template.learn_skill(pCreate.skill_point.get(i));
                    break;
                }
            }
            for (int i = 0; i < pCreate.skill_point.size(); i++) {
                if (pCreate.skill_point.get(i).temp.ID == 2 && pCreate.skill_point.get(i).temp.Lv_RQ == -1) {
                    Skill_Template.learn_skill(pCreate.skill_point.get(i));
                    break;
                }
            }

            pCreate.ensureRms0();
            pCreate.saveDeTuToDb(connection);

            return pCreate;
        } finally {
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        }
    }

    /**
     * Mở hộp thoại Yes/No xác nhận xóa đệ tử cho Sư phụ (Owner).
     */
    public static void confirmDeleteDeTu(Player master) {
        if (master == null) return;
        if (master instanceof DeTu || master.isDe) {
            try {
                if (master.getService() != null) {
                    master.getService().send_box_ThongBao_OK("Bạn đang điều khiển Đệ tử! Vui lòng quay về nhân vật Sư phụ trước khi thực hiện.");
                }
            } catch (Exception ignored) {}
            return;
        }
        if (master.detu == null && (master.nameDe == null || master.nameDe.isBlank())) {
            try {
                if (master.getService() != null) {
                    master.getService().send_box_ThongBao_OK("Bạn chưa có Đệ tử nào để xóa!");
                }
            } catch (Exception ignored) {}
            return;
        }

        String dtName = (master.detu != null && master.detu.name != null && !master.detu.name.isBlank())
                ? master.detu.name
                : ((master.nameDe != null && !master.nameDe.isBlank()) ? master.nameDe : "Đệ tử");

        YesNoDialog dlg = new YesNoDialog(master, 87102, "Xác Nhận Xóa Đệ Tử",
                "Bạn có chắc chắn muốn xóa/thả đệ tử [" + dtName + "] không?\n"
                + "Toàn bộ thông tin, cấp độ, tiềm năng, kỹ năng và dữ liệu đệ tử sẽ bị XÓA VĨNH VIỄN khỏi máy chủ và không thể khôi phục!",
                new String[]{"Xác nhận xóa", "Hủy"},
                new byte[]{-1, -1});
        dlg.setHandler(val -> {
            if (val == 0) {
                executeDeleteDeTu(master);
            }
        });
        master.setyesNoDialog(dlg);
        try {
            master.getService().startYesNo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Thực hiện kick đệ tử khỏi server, hủy trạng thái hợp thể, xóa dữ liệu SQL và xóa cache.
     */
    public static void executeDeleteDeTu(Player master) {
        if (master == null) return;
        if (master instanceof DeTu || master.isDe) {
            try {
                if (master.getService() != null) {
                    master.getService().send_box_ThongBao_OK("Không thể xóa đệ tử khi đang ở thể xác đệ tử!");
                }
            } catch (Exception ignored) {}
            return;
        }

        // 1. Kick đệ tử ở server & giải phóng bộ nhớ runtime
        DeTu dt = master.detu;
        if (dt != null) {
            // Hủy trạng thái hợp thể nếu đang bật
            if (master.isFusion || dt.detuStatus == STATUS_FUSION) {
                dt.removeFusion();
            }
            master.fusionBonusHp = 0;
            master.fusionBonusMp = 0;
            master.fusionBonusDame = 0;
            master.fusionBonusDef = 0;
            master.isFusion = false;
            try {
                master.setAbility();
                if (master.hp > master.hpMax) master.hp = master.hpMax;
            } catch (Exception ignored) {}

            // Xóa đệ tử khỏi Map / Zone hiện tại
            if (dt.map != null) {
                dt.map.leave_map(dt, 2);
                dt.map = null;
            }
            dt.isdie = true;
            dt.detuStatus = STATUS_HOME;
            dt.master = null;
        }

        // Reset trạng thái đệ tử trên nhân vật Sư phụ
        master.detu = null;
        master.nameDe = "";
        master.isDeOnl = false;

        // 2. Xóa sạch dữ liệu đệ tử trong Database SQL & Cache
        Connection connection = null;
        Statement st = null;
        try {
            connection = DbManager.gI().getConnect();
            st = connection.createStatement();
            st.execute("DELETE FROM `players_detu` WHERE `owner_id` = " + master.IDPlayer);
            if (dt != null && dt.IDPlayer > 0) {
                st.execute("DELETE FROM `players_detu` WHERE `id` = " + dt.IDPlayer);
            }
            st.execute("DELETE FROM `players` WHERE `name` = '" + master.name + "_detu'");
            try {
                st.execute("UPDATE `players` SET `name_de` = '' WHERE `id` = " + master.IDPlayer);
            } catch (Exception ignored) {}

            database.CacheManager.gI().remove("players_detu_" + master.IDPlayer);
            database.CacheManager.gI().remove("players_" + master.name + "_detu");
            if (dt != null && dt.IDPlayer > 0) {
                database.CacheManager.gI().remove("players_detu_" + dt.IDPlayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (Exception ignored) {}
            try {
                if (connection != null) connection.close();
            } catch (Exception ignored) {}
        }

        // 3. Flush Sư phụ để lưu thay đổi vào DB
        try {
            master.flush(master, false);
        } catch (Exception ignored) {}

        // 4. Thông báo và cập nhật UI cho Sư phụ
        try {
            if (master.getService() != null) {
                master.getService().Main_char_Info(false);
                master.getService().send_box_ThongBao_OK("Đã xóa vĩnh viễn đệ tử và toàn bộ dữ liệu thành công!");
            }
        } catch (Exception ignored) {}
    }

    public static void deleteDeTu(Player master) {
        executeDeleteDeTu(master);
    }

    public static void renameDeTu(Player master, String newName) {
        Connection connection = null;
        Statement st = null;
        try {
            if (master.detu != null) {
                master.detu.name = newName;
            }
            master.nameDe = newName;
            connection = DbManager.gI().getConnect();
            st = connection.createStatement();
            st.execute("update `players_detu` set `name` = '" + newName + "' where `owner_id` = " + master.IDPlayer);
            st.execute("update `players` set `name` = '" + newName + "' where `name` = '" + master.name + "_detu' limit 1");
            database.CacheManager.gI().remove("players_detu_" + master.IDPlayer);
            database.CacheManager.gI().remove("players_" + master.name + "_detu");
            database.CacheManager.gI().remove("players_" + newName + "_detu");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (Exception e) {}
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {}
        }
    }

    @Override
    public synchronized void update_ruby(long par) {
        if (this.master != null) {
            this.master.update_ruby(par);
            this.ruby = this.master.ruby;
        } else {
            super.update_ruby(par);
        }
    }

    @Override
    public synchronized void update_coin(long par) {
        if (this.master != null) {
            this.master.update_coin(par);
            this.coin = this.master.coin;
        } else {
            super.update_coin(par);
        }
    }

    @Override
    public synchronized void updateVnd(long par) {
        if (this.master != null) {
            this.master.updateVnd(par);
            this.vnd = this.master.vnd;
        } else {
            super.updateVnd(par);
        }
    }

    @Override
    public synchronized void update_ngoc(long par) {
        if (this.master != null) {
            this.master.update_ngoc(par);
            this.kimcuong = this.master.kimcuong;
        } else {
            super.update_ngoc(par);
        }
    }

    @Override
    public synchronized void update_ngoc_ex(long par) {
        if (this.master != null) {
            this.master.update_ngoc_ex(par);
            this.kimcuong = this.master.kimcuong;
        } else {
            super.update_ngoc_ex(par);
        }
    }

    @Override
    public synchronized void update_vang(long par) {
        if (this.master != null) {
            this.master.update_vang(par);
            this.vang = this.master.vang;
        } else {
            super.update_vang(par);
        }
    }

    @Override
    public synchronized void update_tieuRuby(long par) {
        if (this.master != null) {
            this.master.update_tieuRuby(par);
            this.tieuRuby = this.master.tieuRuby;
        } else {
            super.update_tieuRuby(par);
        }
    }

    public static final java.util.Map<Integer, DeTu> WILD_DETU_MAP = new java.util.concurrent.ConcurrentHashMap<>();

    // Danh sách các map thích hợp để đệ tử hoang xuất hiện (làng, thị trấn, khu vực phiêu lưu phổ biến)
    public static final int[] WILD_DETU_MAP_IDS = new int[]{
        1,   // Làng Cối Xay Gió (Foosha)
        2,   // Rừng Làng Foosha
        9,   // Thị Trấn Vỏ Sò (Shells Town)
        10,  // Căn Cứ Hải Quân
        17,  // Thị Trấn Orange
        25,  // Làng Syrup
        33,  // Nhà Hàng Baratie
        41,  // Làng Hạt Dẻ (Cocoyasi)
        49,  // Thị Trấn Khởi Đầu (Loguetown)
        69,  // Thị Trấn Whiskey Peak
        83,  // Thị Trấn Horn (Đảo Drum)
        93,  // Thành Phố Nanohana (Alabasta)
        107  // Thành Phố Rainbase (Alabasta)
    };

    // Giảm số lượng đệ tử hoang trên toàn server xuống tối đa 5 con cùng lúc
    public static final int MAX_WILD_DETU = 5;

    public static synchronized void updateWildSpawning() {
        try {
            long now = System.currentTimeMillis();

            // 1. Kiểm tra và dọn dẹp các đệ tử hoang không còn hợp lệ (chết, đã có sư phụ, hoặc quá 15 phút cần chuyển map)
            List<Integer> toRemove = new ArrayList<>();
            for (java.util.Map.Entry<Integer, DeTu> entry : WILD_DETU_MAP.entrySet()) {
                int key = entry.getKey();
                DeTu dt = entry.getValue();
                boolean invalid = false;

                if (dt == null || dt.map == null || dt.isdie || dt.master != null) {
                    invalid = true;
                } else if (now - dt.wildSpawnTime > 15 * 60 * 1000L) { // Tồn tại > 15 phút -> chuyển đổi vị trí ngẫu nhiên
                    invalid = true;
                } else {
                    boolean foundInZone = false;
                    if (dt.map.players != null && dt.map.players.contains(dt)) {
                        foundInZone = true;
                    }
                    if (!foundInZone) {
                        invalid = true;
                    }
                }

                if (invalid) {
                    toRemove.add(key);
                    if (dt != null) {
                        if (dt.map != null) {
                            try {
                                dt.map.leave_map(dt, 2);
                            } catch (Exception ignored) {}
                        }
                        if (dt.index_map < 0) {
                            database.IDManager.putID(dt.index_map, database.IDManager.FAKE_BOT);
                        }
                    }
                }
            }

            for (Integer k : toRemove) {
                WILD_DETU_MAP.remove(k);
            }

            // 2. Xuất hiện đệ tử hoang mới nếu số lượng hiện tại < MAX_WILD_DETU
            if (WILD_DETU_MAP.size() < MAX_WILD_DETU) {
                List<Integer> availableMaps = new ArrayList<>();
                for (int mid : WILD_DETU_MAP_IDS) {
                    boolean mapHasWild = Zone.hasActiveWildDeTuInMap(mid);
                    if (!mapHasWild) {
                        availableMaps.add(mid);
                    }
                }

                if (!availableMaps.isEmpty()) {
                    java.util.Collections.shuffle(availableMaps);
                    int spawnCount = Math.min(MAX_WILD_DETU - WILD_DETU_MAP.size(), availableMaps.size());
                    for (int i = 0; i < spawnCount; i++) {
                        int mapId = availableMaps.get(i);
                        if (Zone.hasActiveWildDeTuInMap(mapId)) continue;

                        Zone[] zones = Zone.getMapByID(mapId);
                        if (zones == null || zones.length == 0) continue;

                        Zone targetZone = zones[ZUtil.random(zones.length)];
                        if (targetZone != null && targetZone.template != null) {
                            short spawnX = (short) ZUtil.random(150, Math.max(200, targetZone.template.maxW - 150));
                            short spawnY = 260;
                            if (targetZone.template.vgos != null && !targetZone.template.vgos.isEmpty()) {
                                spawnY = targetZone.template.vgos.get(0).ynew;
                            }

                            int randomClazz = ZUtil.random(1, 5);
                            DeTu newWild = createWildDeTu(randomClazz, spawnX, spawnY);
                            newWild.wildSpawnTime = now;
                            newWild.joinWild(targetZone, spawnX, spawnY);
                            WILD_DETU_MAP.put((int) newWild.index_map, newWild);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasWildDeTuInMap(int mapId) {
        return Zone.hasActiveWildDeTuInMap(mapId);
    }

    public static DeTu spawnWildDeTuAt(Zone targetZone, short x, short y, int clazz) {
        try {
            if (targetZone == null || targetZone.template == null) return null;
            int mapId = targetZone.template.id;
            if (x <= 0 || y <= 0) {
                x = (short) ZUtil.random(150, Math.max(200, targetZone.template.maxW - 150));
                y = 260;
                if (targetZone.template.vgos != null && !targetZone.template.vgos.isEmpty()) {
                    y = targetZone.template.vgos.get(0).ynew;
                }
            }
            if (clazz <= 0 || clazz > 5) {
                clazz = ZUtil.random(1, 5);
            }
            DeTu newWild = createWildDeTu(clazz, x, y);
            newWild.wildSpawnTime = System.currentTimeMillis();
            newWild.joinWild(targetZone, x, y);
            WILD_DETU_MAP.put((int) newWild.index_map, newWild);
            return newWild;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void promptCaptureWildDeTu(Player p, DeTu wildDt) {
        if (p == null || wildDt == null || wildDt.master != null || p.map != wildDt.map) return;
        if (p.detu != null || (p.nameDe != null && !p.nameDe.isBlank())) {
            try {
                if (p.getService() != null) p.getService().send_box_ThongBao_OK("Bạn đã có đệ tử rồi!");
            } catch (Exception ignored) {}
            return;
        }
        activities.Sudo mySudo = activities.Sudo.getSuDo(p);
        if (mySudo == null || mySudo.chucInSudo != 1) {
            try {
                core.GlobalChatService.getInstance().chatPublic(wildDt, p.name + " ơi, bạn cần mở chức năng Sư Phụ tại NPC Garp mới nhận được ta nhé!");
                if (p.getService() != null) {
                    p.getService().send_box_ThongBao_OK("Bạn chưa mở chức năng Sư Đồ! Hãy đến gặp Phó Đô Đốc Garp để mở chức năng Sư Phụ trước khi nhận đệ tử hoang.");
                }
            } catch (Exception ignored) {}
            return;
        }
        try {
            core.GlobalChatService.getInstance().chatPublic(wildDt, "Sư phụ " + p.name + " ơi! Hãy nhận con làm đệ tử với ạ!");
            model.YesNoDialog ynd = new model.YesNoDialog(p, 9999, "Nhận đệ tử",
                    "Đệ tử hoang muốn bái bạn làm sư phụ, bạn có đồng ý?",
                    new String[]{"Đồng ý", "Hủy"},
                    new byte[]{-1, -1});
            ynd.setHandler(val -> {
                if (val == 0) {
                    try {
                        activities.Sudo mySudoCheck = activities.Sudo.getSuDo(p);
                        if (mySudoCheck == null || mySudoCheck.chucInSudo != 1) {
                            core.GlobalChatService.getInstance().chatPublic(wildDt, p.name + " ơi, bạn chưa mở chức năng Sư Phụ tại NPC Garp kìa!");
                            if (p.getService() != null) {
                                p.getService().send_box_ThongBao_OK("Bạn chưa mở chức năng Sư Đồ! Hãy mở trước khi nhận đệ tử.");
                            }
                            return;
                        }
                        if (p.detu == null && (p.nameDe == null || p.nameDe.isBlank())) {
                            DeTu newDt = createNewDeTu(p, wildDt.clazz, wildDt.head, wildDt.hair);
                            p.detu = newDt;
                            p.isDeOnl = true;
                            p.detu.map = p.map;
                            p.detu.x = p.x;
                            p.detu.y = p.y;
                            p.detu.hp = p.detu.ability.get_hp_max(true);
                            p.detu.mp = p.detu.ability.get_mp_max(true);
                            if (p.map != null) {
                                p.map.enter_map(p.detu);
                            }
                            p.update_info_to_all();
                            core.GlobalChatService.getInstance().chatPublic(newDt, "Cảm ơn Sư Phụ " + p.name + " đã thu nhận con!");
                            if (p.getService() != null) {
                                p.getService().send_box_ThongBao_OK("Chúc mừng bạn đã nhận Đệ tử thành công!");
                            }
                            if (wildDt.map != null) {
                                WILD_DETU_MAP.remove((int) wildDt.index_map);
                                wildDt.map.leave_map(wildDt, 2);
                            }
                            if (wildDt.index_map < 0) {
                                database.IDManager.putID(wildDt.index_map, database.IDManager.FAKE_BOT);
                            }
                        } else {
                            if (p.getService() != null) {
                                p.getService().send_box_ThongBao_OK("Bạn đã có đệ tử rồi!");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    core.GlobalChatService.getInstance().chatPublic(wildDt, "Tiếc quá, con sẽ tiếp tục đi tìm Sư Phụ khác!");
                    if (wildDt.rejectedPlayers != null) {
                        wildDt.rejectedPlayers.add(p.IDPlayer);
                    }
                    wildDt.targetPlayer = null;
                    wildDt.nextInteractTime = System.currentTimeMillis() + 30000;
                }
            });
            ynd.startYesNo();
        } catch (Exception ignored) {}
    }

    public static DeTu createWildDeTu(int clazz, short x, short y) throws Exception {
        String name = "Đệ Tử Hoang";
        DeTu dt = new DeTu(name, null);
        dt.isBot = true;
        dt.isDe = true;
        dt.type_pk = -1;
        short botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
        if (botId >= 0) {
            botId = (short) -ZUtil.random(25000, 31000);
        }
        dt.index_map = botId;
        dt.IDPlayer = dt.index_map;
        dt.wildSpawnTime = System.currentTimeMillis();
        dt.clazz = (byte) clazz;
        dt.level = 1;
        dt.exp = 0;
        dt.thongthao = 0;
        dt.point1 = 1;
        dt.point2 = 1;
        dt.point3 = 1;
        dt.point4 = 1;
        dt.point5 = 0;
        
        dt.ability = new Ability(dt);
        dt.item = new Item(dt);
        dt.item.it_body = new Item_wear[itemz.Item.MAX_BODY];
        dt.item.bag3 = new Item_wear[dt.item.max_bag];
        dt.item.bag47 = new ArrayList<>();
        dt.skill_point = new ArrayList<>();
        dt.list_eff = new CopyOnWriteArrayList<>();
        dt.itfashionP = new ArrayList<>();
        dt.fashion = new ArrayList<>();
        dt.itemboat = new ArrayList<>();
        
        dt.x = x;
        dt.y = y;
        
        short head = 0, hair = 1;
        switch (clazz) {
            case 1: hair = 1; break;
            case 2: hair = 24; break;
            case 3: hair = 28; break;
            case 4: hair = 32; break;
            case 5: hair = 36; break;
            default: hair = 1; break;
        }
        
        dt.head = head;
        dt.hair = hair;
        
        List<Item_wear> equips = getDefaultEquip(clazz);
        for (Item_wear w : equips) {
            if (w.index >= 0 && w.index < dt.item.it_body.length) {
                dt.item.it_body[w.index] = w;
                if (w.index == 6) {
                    dt.item.it_heart = w;
                }
            }
        }
        
        dt.skill_point = getDefaultSkills(clazz);
        dt.setDefaultFashion(clazz);
        dt.updateParts();
        dt.setAbility();
        
        dt.hp = dt.ability.get_hp_max(true);
        dt.mp = dt.ability.get_mp_max(true);
        dt.setService(new network.NoService(dt));
        
        return dt;
    }

    public void joinWild(Zone z, short x, short y) {
        if (z == null) return;
        this.map = z;
        this.x = x;
        this.y = y;
        this.xold = x;
        this.yold = y;
        this.lastValidX = x;
        this.lastValidY = y;
        this.hp = this.ability.get_hp_max(true);
        this.mp = this.ability.get_mp_max(true);
        this.isdie = false;
        try {
            z.enter_map(this);
            
            Message m_spawn = new Message(1);
            m_spawn.writer().writeByte(0);
            m_spawn.writer().writeShort(this.index_map);
            m_spawn.writer().writeShort(this.x);
            m_spawn.writer().writeShort(this.y);
            
            for (int i = 0; i < z.players.size(); i++) {
                Player p0 = z.players.get(i);
                if (p0 == null || p0.isBot || p0.conn == null) continue;
                try {
                    p0.addmsg(m_spawn);
                    z.send_char_in4_inmap(p0, this.index_map);
                    if (p0.getService() != null) {
                        p0.getService().update_PK(this, false);
                        p0.getService().charWearing(this, false);
                        p0.getService().Weapon_fashion(this, false);
                    }
                } catch (Exception ignored) {}
            }
            m_spawn.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // === XEM THÔNG TIN, KỸ NĂNG & TRẠNG THÁI ĐỆ TỬ ===
    // =========================================================

    public static void showDeTuInfo(Player p) {
        if (p == null) return;
        if (p.detu == null) {
            try {
                p.getService().send_box_ThongBao_OK("Bạn chưa sở hữu Đệ tử!");
            } catch (Exception ignored) {}
            return;
        }
        DeTu dt = p.detu;
        if (dt.ability == null) {
            dt.ability = new ability.Ability(dt);
        }
        try {
            dt.setAbility();
        } catch (Exception ignored) {}

        StringBuilder sb = new StringBuilder();
        sb.append("=== THÔNG TIN ĐỆ TỬ ===\n");
        sb.append("• Tên: ").append(dt.name).append("\n");
        String className = "Vô phái";
        if (dt.clazz >= 1 && dt.clazz <= 5) {
            className = Clazz.NAME[dt.clazz - 1];
        }
        sb.append("• Hệ phái: ").append(className).append("\n");
        sb.append("• Cấp độ: ").append(dt.level).append(" (").append(dt.percentLv()).append("%)\n");
        String statusStr = (dt.detuStatus >= 0 && dt.detuStatus < STATUS_NAMES.length) ? STATUS_NAMES[dt.detuStatus] : "Đi theo";
        sb.append("• Trạng thái: ").append(statusStr).append(" [").append(dt.isDeOnl ? "Xuất hiện" : "Ẩn").append("]\n");

        int maxHp = dt.ability.get_hp_max(true);
        int maxMp = dt.ability.get_mp_max(true);
        sb.append("• HP: ").append(ZUtil.number_format(dt.hp > 0 ? dt.hp : maxHp)).append(" / ").append(ZUtil.number_format(maxHp)).append("\n");
        sb.append("• MP: ").append(ZUtil.number_format(dt.mp > 0 ? dt.mp : maxMp)).append(" / ").append(ZUtil.number_format(maxMp)).append("\n");
        sb.append("• Tấn công: ").append(ZUtil.number_format(dt.ability.get_dame(true))).append("\n");
        sb.append("• Phòng thủ: ").append(ZUtil.number_format(dt.ability.get_def(true))).append("\n");
        sb.append("• Chí mạng: ").append(dt.ability.get_crit(true)).append("%\n");

        byte[] optIds = new byte[]{10, 11, 12, 13, 14, 47, 48, 49, 50, 51, 52};
        for (byte opId : optIds) {
            template.ItemOptionTemplate opTemp = template.ItemOptionTemplate.ENTRYS.get((int) opId);
            if (opTemp != null) {
                int val = dt.ability.view_in4(opId);
                if (val > 0) {
                    sb.append("• ").append(opTemp.name).append(": ");
                    if (opTemp.percent == 1) {
                        sb.append(String.format("%.1f%%", ((float) val) / 10f));
                    } else {
                        sb.append(ZUtil.number_format(val));
                    }
                    sb.append("\n");
                }
            }
        }

        if (dt.pointAttribute > 0 || dt.point1 > 0 || dt.point2 > 0 || dt.point3 > 0 || dt.point4 > 0 || dt.point5 > 0) {
            sb.append("• Tiềm năng khả dụng: ").append(dt.pointAttribute).append("\n");
            sb.append("  - Sức mạnh: ").append(dt.point1).append(" | Phòng thủ: ").append(dt.point2).append("\n");
            sb.append("  - Thể lực: ").append(dt.point3).append(" | Tinh thần: ").append(dt.point4).append(" | Nhanh nhẹn: ").append(dt.point5).append("\n");
        }

        if (dt.item != null && dt.item.it_heart != null) {
            sb.append("• Tim: Cấp ").append(dt.item.it_heart.levelUp).append("/99 (Chế tác: ").append(dt.item.it_heart.valueChetac).append("/100)\n");
        }

        if (!dt.my_pet.isEmpty()) {
            sb.append("• Pet theo cùng: ").append(dt.my_pet.get(0).template.name).append("\n");
        }

        if (dt.level >= 100 && dt.pointAttributeThongThao > 0) {
            sb.append("• Điểm thông thạo: ").append(dt.pointAttributeThongThao).append("\n");
        }

        try {
            p.getService().send_box_ThongBao_OK(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDeTuSkillMenu(Player p) {
        if (p == null) return;
        if (p.detu == null) {
            try {
                p.getService().send_box_ThongBao_OK("Bạn chưa sở hữu Đệ tử!");
            } catch (Exception ignored) {}
            return;
        }
        DeTu dt = p.detu;
        if (dt.skill_point == null || dt.skill_point.isEmpty()) {
            try {
                p.getService().send_box_ThongBao_OK("Đệ tử chưa học kỹ năng nào.");
            } catch (Exception ignored) {}
            return;
        }

        List<String> names = new ArrayList<>();
        List<Integer> icons = new ArrayList<>();
        p.tempSkillList = new ArrayList<>();

        for (Skill_info sk : dt.skill_point) {
            if (sk == null || sk.temp == null) continue;

            String prefix = "";
            int lv = (sk.temp.Lv_RQ > 0) ? sk.temp.Lv_RQ : 1;

            boolean isDevil = (sk.temp.ID > 2000 
                    || (sk.temp.indexSkillInServer >= 475 && sk.temp.indexSkillInServer <= 486) 
                    || (sk.temp.indexSkillInServer >= 512 && sk.temp.indexSkillInServer <= 551) 
                    || (sk.temp.indexSkillInServer >= 656 && sk.temp.indexSkillInServer <= 659)
                    || (sk.temp.indexSkillInServer >= 791 && sk.temp.indexSkillInServer <= 802)
                    || sk.temp.typeDevil > 0)
                    && !(sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511)
                    && !(sk.temp.ID >= 1010 && sk.temp.ID <= 1014);

            if (isDevil) {
                prefix = "[Ác Quỷ] ";
                lv = sk.lvdevil > 0 ? sk.lvdevil : (sk.temp.Lv_RQ > 0 ? sk.temp.Lv_RQ : 1);
            } else if (sk.temp.indexSkillInServer >= 672 && sk.temp.indexSkillInServer <= 690) {
                prefix = "[Haki] ";
            } else if ((sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511) || (sk.temp.ID >= 1010 && sk.temp.ID <= 1014)) {
                prefix = "[Buff Phái] ";
            } else if (sk.temp.typeSkill == 2 || sk.temp.typeSkill == 3) {
                prefix = "[Nội tại] ";
            } else {
                prefix = "[Chủ động] ";
            }

            names.add(prefix + sk.temp.name + " (Lv." + lv + ")");
            icons.add((int) sk.temp.idIcon);
            p.tempSkillList.add(sk);
        }

        if (names.isEmpty()) {
            try {
                p.getService().send_box_ThongBao_OK("Đệ tử chưa học kỹ năng nào.");
            } catch (Exception ignored) {}
            return;
        }

        try {
            p.getService().send_dynamic_menu_type4(955, 0, "Kỹ năng: " + dt.name, names, icons);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleDeTuSkillSelect(Player p, int index) {
        if (p == null || p.detu == null) return;
        DeTu dt = p.detu;
        Skill_info sk = null;
        if (p.tempSkillList != null && index >= 0 && index < p.tempSkillList.size()) {
            sk = p.tempSkillList.get(index);
        } else if (dt.skill_point != null && index >= 0 && index < dt.skill_point.size()) {
            sk = dt.skill_point.get(index);
        }
        if (sk == null || sk.temp == null) return;

        boolean isDevil = (sk.temp.ID > 2000 
                || (sk.temp.indexSkillInServer >= 475 && sk.temp.indexSkillInServer <= 486) 
                || (sk.temp.indexSkillInServer >= 512 && sk.temp.indexSkillInServer <= 551) 
                || (sk.temp.indexSkillInServer >= 656 && sk.temp.indexSkillInServer <= 659)
                || (sk.temp.indexSkillInServer >= 791 && sk.temp.indexSkillInServer <= 802)
                || sk.temp.typeDevil > 0)
                && !(sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511)
                && !(sk.temp.ID >= 1010 && sk.temp.ID <= 1014);

        String typeStr = "Chủ động tấn công";
        if (isDevil) {
            typeStr = "Năng lực Trái Ác Quỷ (Devil Fruit)";
        } else if (sk.temp.indexSkillInServer >= 672 && sk.temp.indexSkillInServer <= 690) {
            typeStr = "Bí kỹ Haki Tối Thượng";
        } else if ((sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511) || (sk.temp.ID >= 1010 && sk.temp.ID <= 1014)) {
            typeStr = "Kỹ năng Buff Môn Phái";
        } else if (sk.temp.typeSkill == 2 || sk.temp.typeSkill == 3) {
            typeStr = "Kỹ năng Bị Động / Nội Tại";
        } else if (sk.temp.typeSkill == 1) {
            typeStr = "Kỹ năng Buff / Hỗ trợ";
        }

        int lv = sk.temp.Lv_RQ > 0 ? sk.temp.Lv_RQ : (sk.lvdevil > 0 ? sk.lvdevil : 1);

        StringBuilder detail = new StringBuilder();
        detail.append(sk.temp.name).append("\n");
        detail.append("- Phân loại: ").append(typeStr).append("\n");
        detail.append("- Cấp độ chiêu: Cấp ").append(lv);
        if (sk.lvdevil > 0) {
            detail.append(" (Ác quỷ Lv.").append(sk.lvdevil).append(" ").append(sk.devilpercent).append("%)");
        }
        detail.append("\n");
        if (sk.temp.percentDame > 0) {
            detail.append("- Sát thương: ").append(sk.temp.percentDame).append("%\n");
        }
        if (sk.temp.manaLost > 0) {
            detail.append("- Tiêu hao: ").append(sk.temp.manaLost).append(" MP\n");
        }
        if (sk.temp.timeDelay > 0) {
            detail.append("- Thời gian hồi: ").append(String.format("%.1fs", sk.temp.timeDelay / 1000.0)).append("\n");
        }
        if (sk.temp.range > 0 || sk.temp.rangeLan > 0) {
            detail.append("- Phạm vi: ").append(sk.temp.rangeLan > 0 ? sk.temp.rangeLan + "m" : sk.temp.range + "m").append("\n");
        }
        if (sk.temp.nTarget > 1) {
            detail.append("- Số mục tiêu: ").append(sk.temp.nTarget).append("\n");
        }
        String desc = sk.temp.info;
        if (desc == null || desc.isBlank()) {
            desc = sk.temp.getInfo((byte) 1, dt.clazz);
        }
        detail.append("- Mô tả: ").append(desc != null && !desc.isBlank() ? desc : "Kỹ năng chiến đấu của Đệ Tử.");

        try {
            final Skill_info selectedSk = sk;
            p.setyesNoDialog(new model.YesNoDialog(p, 9551, "Chi Tiết Kỹ Năng", detail.toString(),
                    new String[]{"Cường Hóa Ác Quỷ", "Đóng"}, new byte[]{-1, -1}, val -> {
                        if (val == 0) {
                            try {
                                p.switchCharacter(dt);
                                activities.UpgradeDevil.show_table(dt, 1);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }));
            p.getService().startYesNo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendInfoToMaster() {
        if (master == null) return;
        showDeTuInfo(master);
    }

    public void sendSkillInfoToMaster() {
        if (master == null) return;
        showDeTuSkillMenu(master);
    }

    public static void showDeTuStatusMenu(Player p) {
        if (p == null) return;
        if (p.detu == null) {
            p.getService().send_box_ThongBao_OK("Bạn chưa sở hữu Đệ tử!");
            return;
        }
        p.getService().send_dynamic_menu(956, 0, "Trạng Thái Đệ Tử",
            new String[]{"1. Đi theo", "2. Bảo vệ", "3. Tấn công", "4. Hợp thể", "5. Về nhà"});
    }

    public static void handleDeTuStatusSelect(Player p, int index) {
        if (p == null || p.detu == null) return;
        int targetStatus = STATUS_FOLLOW;
        switch (index) {
            case 0: targetStatus = STATUS_FOLLOW; break;
            case 1: targetStatus = STATUS_PROTECT; break;
            case 2: targetStatus = STATUS_ATTACK; break;
            case 3: targetStatus = STATUS_FUSION; break;
            case 4: targetStatus = STATUS_HOME; break;
        }
        p.detu.setStatus(targetStatus);
    }
}
