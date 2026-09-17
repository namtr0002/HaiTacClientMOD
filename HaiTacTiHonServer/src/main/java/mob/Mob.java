package mob;

import event.EventManager;
import zabstracts.AbsBoss;
import zinterfaces.iMob;
import model.Player;
import core.Manager;
import core.ZUtil;
import network.Message;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import map.Zone;
import template.MobTemplate;

/**
 * Class Mob cơ sở cho tất cả loại Quái vật / Boss trên Server.
 * Chuẩn hóa logic tương thích với Client (MainMonster / MonsterHuman / MonsterWalk).
 */
public class Mob {

    public final static HashMap<Integer, Mob> ENTRYS = new HashMap<>();
    public final static int TIME_RESPAWN = 7;

    public short x, y;
    public int hp, hp_max;
    public int level;
    public MobTemplate mtemplate;
    public boolean isdie;
    public int id_target = -1;
    public int index;
    public long time_skill;
    public long time_refresh;
    public AbsBoss boss_inf;
    public Zone map;
    public boolean isChoang = false;
    public long timeChoang;
    public String keyflag;
    public boolean isSieuTrum;
    public boolean is_boss;
    public iMob iMob;

    /** Loại đặc biệt tương thích Client (0: Thường, 1: Tinh Anh/Thủ Lĩnh, 2: Trùm/Boss) */
    public byte typeSpecMonSter = 0;

    // --- CẤU HÌNH CUSTOM STATS (CHỈNH TRỰC TIẾP TỪ BẤT KỲ ĐÂU) ---
    public int customDame = -1;       // Nếu >= 0, dùng giá trị này thay cho dame mặc định
    public double dameMultiplier = 1.0; // Hệ số nhân dame
    public int customDef = -1;        // Nếu >= 0, dùng giá trị này thay cho def mặc định
    public double defMultiplier = 1.0;  // Hệ số nhân def
    public int customAtkCooldown = -1; // Cooldown đánh của quái (ms), mặc định game là 2000ms

    public Mob() {
    }

    public Mob(MobTemplate template, Zone zone, short x, short y, int index) {
        this.mtemplate = template;
        this.map = zone;
        this.x = x;
        this.y = y;
        this.index = index;
        if (template != null) {
            this.hp_max = template.hp_max;
            this.hp = this.hp_max;
            this.level = template.level;
        }
        this.isdie = false;
        this.id_target = -1;
    }

    /** Trả về đối tượng Boss nếu Mob này là Boss hoặc được gắn boss_inf */
    public AbsBoss getBoss() {
        if (this instanceof AbsBoss) {
            return (AbsBoss) this;
        }
        return this.boss_inf;
    }

    /** Kiểm tra xem mob có nằm trong bản đồ phó bản không */
    public boolean isDungeonMob() {
        return this.map != null && this.map.isDungeon();
    }

    /** Boss thuộc phó bản / phụ bản */
    public boolean isDungeonBoss() {
        return (this.is_boss || this.boss_inf != null || this.typeSpecMonSter == 2) && isDungeonMob();
    }

    /** Boss ngoài bản đồ (Boss Thế Giới, Siêu Trùm, Boss Sự Kiện ngoài map) */
    public boolean isOutdoorBoss() {
        return (this.is_boss || this.boss_inf != null || this.typeSpecMonSter == 2 || this instanceof AbsBoss) && !isDungeonMob();
    }

    /** Trả về loại Mob chuẩn */
    public MobType getMobType() {
        if (is_boss || boss_inf != null || typeSpecMonSter == 2) {
            return MobType.BOSS;
        }
        if (isSieuTrum || typeSpecMonSter == 1) {
            return MobType.ELITE;
        }
        if (mtemplate != null) {
            if (mtemplate.ishuman == 1) {
                return MobType.HUMAN;
            }
            if (mtemplate.ishuman == 0) {
                return MobType.WALK;
            }
        }
        return MobType.NORMAL;
    }

    /** Game loop update định kỳ cho mob */
    public void update() {
        if (this.isdie) {
            return;
        }
        if (this.isChoang && System.currentTimeMillis() > this.timeChoang) {
            this.isChoang = false;
        }
        if (this.iMob != null) {
            this.iMob.update(this);
        }
        onUpdate();
    }

    /** Hook để các subclass đè logic update riêng */
    protected void onUpdate() {
    }

    public void sendMove() {
        try {
            Message m_local = new Message(1);
            m_local.writer().writeByte(1);
            m_local.writer().writeShort(index);
            m_local.writer().writeShort(x);
            m_local.writer().writeShort(y);
            if (map != null && map.players != null) {
                for (Player p0 : map.players) {
                    p0.addmsg(m_local);
                }
            }
            m_local.cleanup();
        } catch (IOException e) {
        }
    }

    /**
     * Tấn công quái vật vào người chơi.
     * Tự động điều hướng đến iMob.attack() nếu có tùy biến,
     * hoặc thực thi cơ chế tấn công gốc baseAttack().
     */
    public void attack(Player target) throws IOException {
        if (this.iMob != null) {
            this.iMob.attack(this, target);
        } else {
            baseAttack(target);
        }
    }

    /**
     * Tấn công gốc của quái vật (Super Mob Fire gốc)
     */
    public void baseAttack(Player target) throws IOException {
        if (target == null || target.isdie || target.isSpectator || target.wait_change_map) {
            this.id_target = -1;
            return;
        }

        if (map != null && !map.players.contains(target)) {
            this.id_target = -1;
            return;
        }

        if (Math.hypot(this.x - target.x, this.y - target.y) > 220) {
            this.id_target = -1;
            if (map != null) {
                map.mob_non_focus(this);
            }
            return;
        }

        long cooldown = this.customAtkCooldown >= 0 ? this.customAtkCooldown : 2000L;
        if (!this.isdie && !this.isChoang && this.time_skill <= System.currentTimeMillis()) {
            if (target.time_can_mob_atk > System.currentTimeMillis()) {
                return;
            }

            this.time_skill = System.currentTimeMillis() + cooldown;

            if (true) {
                // 1. Tính toán sát thương (Dame)
                long dame = calculateDamage(target);
                
                // 2. Tính toán phòng thủ (Def)
                long def = calculateDefense(target);

                // Cân bằng Siêu Trùm: chỉ áp dụng cho Siêu Trùm dã ngoại ngoài map, KHÔNG áp dụng cho Boss/Quái phó bản
                if ((this.isSieuTrum || (this.mtemplate != null && (this.mtemplate.mob_id >= 135 && this.mtemplate.mob_id <= 140))) && !isDungeonMob()) {
                    dame = (dame * (100L + (long) this.level * 2L)) / 100L;
                }

                if (map != null && map.IsMapBaoVePhaoDai() && map.baoVePhaoDai != null 
                    && this.mtemplate != null && this.mtemplate.mob_id >= 122 && this.mtemplate.mob_id <= 125) {
                    dame = target.ability.get_hp_max(true) / 25;
                }

                dame -= def;
                if (dame < 0) {
                    dame = 1;
                } else if (target.get_eff(23) != null) {
                    dame /= 2;
                }
                
                // Kiểm tra né tránh (Miss)
                boolean miss = (target.get_eff(205) != null || target.ability.get_miss(true) > ZUtil.random(1000));
                if (miss) {
                    dame = 0;
                }

                // Kích ẩn phòng thủ của người chơi khi bị quái đánh (Độc quyền 1 hiệu ứng)
                if (dame > 0 && target.ability != null) {
                    if (target.get_eff(300) != null) {
                        dame = 0;
                    } else {
                        List<Integer> defKichAnList = new ArrayList<>();
                        for (int kaId = 0; kaId <= 2; kaId++) {
                            int kaCount = target.ability.get_kich_an(kaId);
                            if (kaCount > 0 && target.get_eff(400 + kaId) == null) {
                                for (int k = 0; k < kaCount; k++) {
                                    defKichAnList.add(kaId);
                                }
                            }
                        }
                        if (!defKichAnList.isEmpty()) {
                            int chosenDefKa = defKichAnList.get(ZUtil.random(defKichAnList.size()));
                            int kaCount = target.ability.get_kich_an(chosenDefKa);
                            if (chosenDefKa == 0) { // 0: Bất tử (Tier S: 3.5% base)
                                int chance = 35 + Math.min(50, (kaCount - 1) * 10);
                                if (chance > ZUtil.random(1000)) {
                                    dame = 0;
                                    target.add_new_eff(300, 1, 2_000); // 2.0s
                                    target.add_new_eff(400, 1, 60_000);
                                    if (map != null) {
                                        map.getService().send_kich_an(target, target, 2, 0, 0, 0);
                                    }
                                }
                            } else if (chosenDefKa == 1) { // 1: Lời cảm ơn (Tier B Lỏ: 13.0% base)
                                int chance = 130 + Math.min(100, (kaCount - 1) * 20);
                                if (chance > ZUtil.random(1000)) {
                                    int hpMax = target.ability.get_hp_max(true);
                                    long healPct = 5L + (kaCount - 1);
                                    long damePct = 8L + (kaCount - 1) * 2L;
                                    int heal = (int) Math.min((hpMax * healPct) / 100L, (dame * damePct) / 100L);
                                    target.hp = Math.min(hpMax, target.hp + heal);
                                    target.add_new_eff(401, 1, 30_000);
                                    if (map != null) {
                                        map.getService().send_kich_an(target, target, 1, 1, 0, heal);
                                    }
                                    dame = 0;
                                }
                            } else if (chosenDefKa == 2) { // 2: Lá chắn (Tier A: 6.0% base)
                                int chance = 60 + Math.min(75, (kaCount - 1) * 15);
                                if (chance > ZUtil.random(1000)) {
                                    target.add_new_eff(402, 1, 60_000);
                                    this.isChoang = true;
                                    this.timeChoang = System.currentTimeMillis() + 1_500 + Math.min(1000, (kaCount - 1) * 200); // 1.5s -> 2.5s
                                    if (map != null) {
                                        map.getService().send_kich_an(target, target, 1, 2, 5, 50);
                                        map.getService().send_choang_mob(target, this, 1_500 + Math.min(1000, (kaCount - 1) * 200));
                                    }
                                    dame = 0;
                                }
                            }
                        }
                    }

                    // 9 & 10: Kích ẩn phòng thủ tích lũy (Nén đau 15 hits / Giải phóng năng lượng 22 hits)
                    target.processDefensiveKichAnHit(this, map);
                }

                // 3. Cập nhật HP mục tiêu
                if (target.get_eff(9) != null || target.get_eff(300) != null) {
                    dame = 0;
                }
                if (dame > 0) {
                    if (target.hp == target.ability.get_hp_max(true) && dame >= target.hp) {
                        target.hp = 1;
                    } else {
                        target.hp -= dame;
                    }

                    // [HẤP THỤ SÁT THƯƠNG DEFENDER op58 + op73 KHI BỊ QUÁI ĐÁNH]
                    if (target.hp > 0 && !miss) {
                        int absorb_pct = target.ability.total_param_item(58, true) + target.ability.total_param_item(73, true);
                        if (absorb_pct > 0 && absorb_pct > ZUtil.random(1000)) {
                            long absorbHeal = (dame * (long) Math.min(120, absorb_pct)) / 1000L;
                            long maxAbsorbCap = (target.ability.get_hp_max(true) * 5L) / 100L; // Cap 5% Max HP
                            absorbHeal = Math.min(absorbHeal, maxAbsorbCap);
                            target.hp = Math.min(target.ability.get_hp_max(true), (int)(target.hp + absorbHeal));
                            target.getService().use_potion(0, (int) absorbHeal);
                        }
                    }

                    // [PHẢN ĐÒN VÀO QUÁI & SIÊU TRÙM op14: % phản đòn là tỷ lệ xuất hiện, kích hoạt phản 100% dame]
                    // - Vào quái: đến mốc hOne không trừ HP quái nữa (không có hOne thì dừng ở 1 HP).
                    // - Vào siêu trùm: đến mốc hOne nếu có, không thì 1 HP.
                    // - Không bao giờ làm chết quái hoặc siêu trùm (chỉ chết khi người chơi trực tiếp tấn công).
                    if (target.get_eff(300) == null && target.get_eff(9) == null && !miss) {
                        int react_pct = target.ability.get_dame_react(true);
                        if (react_pct > 0 && react_pct > ZUtil.random(1000)) {
                            long reactDame = dame;
                            if (reactDame > 0 && this.hp > 0 && !this.isdie) {
                                int minHp = (this.mtemplate != null && this.mtemplate.hOne > 0) ? this.mtemplate.hOne : 1;
                                if (this.hp > minHp) {
                                    long maxDeduct = (long) (this.hp - minHp);
                                    long actualReact = Math.min(reactDame, maxDeduct);
                                    if (actualReact > 0) {
                                        this.hp -= (int) actualReact;
                                        if (map != null) {
                                            map.update_hp_mp_eff(null, this, 1, (int) -actualReact);
                                        }
                                    }
                                }
                                // Đã đạt mốc minHp (hOne hoặc 1 HP) thì không trừ HP nữa
                            }
                        }
                    }
                }
                if (target.get_eff(9) != null || target.get_eff(300) != null) {
                    if (target.hp < 1) target.hp = 1;
                }

                // tu choi tu than
                if (target.hp <= 0 && target.get_eff(10) == null
                        && target.ability.get_TuChoiTuThan() > 0) {
                    int time_eff = 5;
                    target.add_new_eff(9, 1, time_eff * 1_000);
                    target.add_new_eff(10, 1, 150_000);
                    target.getService().send_eff(21, 50);
                    target.hp = Math.max(1, target.ability.get_hp_max(true) / 10);
                }

                // Xử lý mục tiêu chết
                if (target.hp <= 0) {
                    target.hp = 0;
                    target.isdie = true;
                    this.id_target = -1;
                    if (map != null) {
                        map.mob_non_focus(this);
                        if (map.IsMapBaoVePhaoDai()) {
                            map.die_player(target, null);
                        } else if (map.template.id == 179) {
                            if (target.clan != null && target.clan.map_create != null 
                                && target.clan.map_create.map_thuLinhBienKhoi != null) {
                                if (target.clan.map_create.map_thuLinhBienKhoi.time < System.currentTimeMillis()) {
                                    target.clan.map_create.map_thuLinhBienKhoi.buff = -1;
                                }
                                target.time_hs_little_garden = System.currentTimeMillis() + (target.clan.map_create.map_thuLinhBienKhoi.buff == 0 ? 2_000 : 10_000L);
                                target.getService().send_time_cool_down(target.time_hs_little_garden, "Hồi sinh", 3);
                            }
                        }
                    }
                }

                // 5. Gửi Message tấn công đến client (Message 100)
                short skillId = (this.mtemplate != null && this.mtemplate.skill != null && this.mtemplate.skill.length > 0)
                        ? this.mtemplate.skill[ZUtil.random(this.mtemplate.skill.length)]
                        : 0;

                Message m = new Message(100);
                m.writer().writeShort(this.index);
                m.writer().writeByte(1);
                m.writer().writeInt(this.hp); // hp
                m.writer().writeInt(this.hp); // mp
                m.writer().writeShort(skillId);
                m.writer().writeByte(1); // size target
                m.writer().writeShort(target.index_map);
                m.writer().writeByte(0);
                m.writer().writeInt((int) dame);
                m.writer().writeInt(0); // dame plus
                m.writer().writeInt(target.hp);
                m.writer().writeByte(0);
                if (map != null) {
                    map.send_msg_all_p(m, target, true);
                }
                m.cleanup();

                // Hook callback cho các class kế thừa khi tấn công
                onAttack(target, (int) dame);

                // 6. Xử lý player chết thực sự
                if (target.hp <= 0) {
                    target.timeAtkBot = System.currentTimeMillis() + 35_000;
                    if (map != null) {
                        map.die_player(target, target);
                    }
                }

                // Kiểm tra tầm xa của mục tiêu để dừng đánh
                if (this.id_target != -1 && !(Math.abs(this.x - target.x) < 200 && Math.abs(this.y - target.y) < 200)) {
                    this.id_target = -1;
                    if (map != null) {
                        map.mob_non_focus(this);
                    }
                }
            }
        }
    }

    /** Tính toán Dame phát ra của Mob */
    protected long calculateDamage(Player target) {
        long dame;
        if (this.customDame >= 0) {
            dame = this.customDame;
        } else {
            dame = (this.level * (long) this.level) / 3L;
            if (this.level < 10) {
                dame = ZUtil.random(10, 20);
            }
            int lvDelta = this.level - target.level;
            if (lvDelta > 5) {
                dame = (dame / 4);
            }
            if (map != null) {
                if (map.map_dungeon != null) {
                    // Phó bản tăng nhẹ 5% dame (tăng khó 1 tí thôi, không nhân x2 như trước)
                    dame = (dame * 105L) / 100L;
                }
                if (target.level < this.level) {
                    int lvDiff = Math.min(10, this.level - target.level);
                    dame = ((dame * (100L + lvDiff * 2L)) / 100);
                }
                if (map.vuonCam != null) {
                    int wave = Math.min(20, map.vuonCam.level);
                    dame = (dame * (100L + wave * 1L)) / 100L;
                }
            }
        }
        if (dame <= 0) {
            dame = ZUtil.random(10, 20);
        }
        return (long) (dame * this.dameMultiplier);
    }

    /** Tính toán Def của mục tiêu đối với Mob */
    protected long calculateDefense(Player target) {
        long def;
        if (this.customDef >= 0) {
            def = this.customDef;
        } else {
            def = target.ability.get_def(true);
            def = (def * (1000L + target.ability.get_def_percent(true))) / 1000L;
        }
        return (long) (def * this.defMultiplier);
    }

    /** Hook khi tấn công trúng người chơi */
    protected void onAttack(Player target, int dameDealt) {
    }

    /** Xử lý khi Mob chết */
    public void mobDie(Player pKill) {
        if (pKill != null) {
            pKill = pKill.getOwnerPlayer();
        }
        EventManager.dispatchOnMobKilled(pKill, this);

        if (this.iMob != null) {
            this.iMob.onDeath(pKill, this);
        }
        onDie(pKill);
    }

    /** Hook khi Mob chết */
    protected void onDie(Player pKill) {
    }
}
