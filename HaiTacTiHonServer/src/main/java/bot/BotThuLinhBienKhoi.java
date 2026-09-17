package bot;

import ability.Ability;
import clan.Clan;
import clan.ClanMember;
import map.Zone;
import model.Player;
import map.zones.ThuLinhBienKhoi;
import mob.Mob;
import core.ZUtil;
import network.Message;
import skill.Skill_info;
import template.EffTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BotThuLinhBienKhoi — Bot phó bản & sự kiện Thủ Lĩnh Biển Khơi (4 Vùng Biển: Đông, Tây, Nam, Bắc).
 *
 * <p>TÍNH NĂNG NỔI BẬT:
 * <ol>
 *   <li><b>32 Băng Hải Tặc Đa Dạng</b>: Mỗi vùng biển sở hữu 8 Clan độc lập, tạo ra các trận Loạn Chiến nảy lửa.</li>
 *   <li><b>AI Chiến Đấu Thông Minh (Human-Like Cadence)</b>: Có Action Delay (GCD + Jitter), combo xoay vòng chiêu thức, không spam mù quáng.</li>
 *   <li><b>Kiểm Soát Cự Ly & Thả Diều (Kiting)</b>: Xạ thủ / Bác sĩ biết lùi lại bắn tỉa khi bị áp sát; Võ sĩ / Kiếm sĩ / Đầu bếp biết luồn lách né đòn.</li>
 *   <li><b>Cơ Chế Sinh Tồn Bỏ Chạy Khi Yếu Máu (Flee & Recovery)</b>: Máu < 30% lập tức tháo chạy khỏi kẻ địch, liên tục uống bình hồi phục và thả skill khống chế phòng thủ; hồi > 60% máu mới quay lại chiến đấu.</li>
 *   <li><b>Chiến Thuật Đại Chiến</b>: Ưu tiên trả thù kẻ vừa tấn công, dồn sát thương kẻ địch ít máu, và tập trung săn Boss Quái Thú tại Map 179.</li>
 * </ol>
 */
public class BotThuLinhBienKhoi extends Bot {

    public static final List<BotThuLinhBienKhoi> POOL = new CopyOnWriteArrayList<>();

    // State Machine
    public static final byte STATE_IDLE = 0;
    public static final byte STATE_COMBAT = 1;
    public static final byte STATE_FLEE = 2;

    public byte state = STATE_IDLE;

    // 32 Băng Hải Tặc đại diện cho 4 Vùng Biển (8 Clan mỗi biển)
    public static final Clan[][] CLANS_BY_SEA = new Clan[4][8];
    static {
        String[][] seaClanNames = {
            // Biển Đông (Cờ 4)
            {"Băng Mũ Rơm", "Băng Mèo Đen", "Băng Krieg", "Băng Arlong", "Băng Buggy", "Băng Alvida", "Băng Red Force", "Băng Bão Lửa"},
            // Biển Tây (Cờ 5)
            {"Băng Tóc Đỏ", "Băng Thriller Bark", "Băng Capone Gang", "Băng Bát Bảo", "Băng Vua Biển", "Băng Khổng Lồ", "Băng Rồng Lửa", "Băng Quỷ Vương"},
            // Biển Nam (Cờ 6)
            {"Băng Kid", "Băng Bonney", "Băng Bách Thú", "Băng Râu Đen", "Băng Sói Bạc", "Băng Thiết Giáp", "Băng Hắc Ám", "Băng Cuồng Nộ"},
            // Biển Bắc (Cờ 7)
            {"Băng Râu Trắng", "Băng Heart", "Băng Donquixote", "Băng Germa 66", "Băng Hawkins", "Băng Drake", "Băng Cổ Đại", "Băng Tử Thần"}
        };

        short[][] seaClanIcons = {
            {1, 5, 9, 13, 17, 21, 25, 29},
            {2, 6, 10, 14, 18, 22, 26, 30},
            {3, 7, 11, 15, 19, 23, 27, 31},
            {4, 8, 12, 16, 20, 24, 28, 32}
        };

        for (int s = 0; s < 4; s++) {
            for (int c = 0; c < 8; c++) {
                Clan clan = new Clan();
                clan.id = (short) (-7100 - (s * 10) - c);
                clan.name = seaClanNames[s][c];
                clan.icon = seaClanIcons[s][c];
                clan.level = (short) (10 + (c % 5));
                clan.trungsinh = (byte) (1 + (s % 3));
                clan.xp = 15000;
                clan.members = new ArrayList<>();

                ClanMember leader = new ClanMember();
                leader.id = (short) (-(10000 + s * 100 + c));
                leader.name = clan.name + " Trưởng";
                leader.level = (short) (80 + s * 5 + c);
                leader.levelInclan = 0;
                clan.members.add(leader);

                CLANS_BY_SEA[s][c] = clan;
            }
        }
    }

    public static Clan getRandomClanForSea(int seaIdx) {
        if (seaIdx < 0 || seaIdx >= 4) seaIdx = 0;
        return CLANS_BY_SEA[seaIdx][ZUtil.random(8)];
    }

    public int seaFlag; // 4=Đông, 5=Tây, 6=Nam, 7=Bắc
    public int tier;    // 0=Cùi/Tân binh, 1=Vừa/Chiến binh, 2=VIP/Thuyền trưởng

    private Player targetPlayer;
    private Mob targetMob;
    private Player priorityTarget;
    private long priorityExpireTime = 0;

    private long nextActionTime = 0;
    private long nextMoveTime = 0;
    private long nextPotionTime = 0;
    private long nextChatTime = 0;
    private long respawnTime = 0;

    private short escapeTargetX = -1;
    private short escapeTargetY = -1;
    private long fleeStartTime = 0;
    private short patrolTargetX = -1;
    private short patrolTargetY = -1;

    public BotThuLinhBienKhoi(int id, String name, int seaFlag, int tier) throws Exception {
        super(id, name);
        this.seaFlag = seaFlag;
        this.tier = tier;
        this.type_pk = (byte) seaFlag;
        this.disableBaseChat = true;
        this.state = STATE_IDLE;
    }

    @Override
    public void onAttackedBy(Player attacker) {
        if (attacker != null && !attacker.equals(this) && !attacker.isdie) {
            if (this.canAttackTargetPlayer(attacker)) {
                this.priorityTarget = attacker;
                this.priorityExpireTime = System.currentTimeMillis() + 12_000L;
            }
        }
    }

    public void sendChatBubble(String text) {
        if (this.map == null || text == null || text.isEmpty()) return;
        try {
            Message m = new Message(17);
            m.writer().writeShort(this.index_map);
            m.writer().writeByte(0);
            m.writer().writeUTF(text);
            this.map.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (IOException ignored) {}
    }

    public String getSeaName() {
        switch (this.seaFlag) {
            case 4: return "Biển Đông";
            case 5: return "Biển Tây";
            case 6: return "Biển Nam";
            case 7: return "Biển Bắc";
            default: return "Biển Khơi";
        }
    }

    @Override
    public void init() {
        this.type_pk = (byte) this.seaFlag;
        this.respawnTime = 0;
        this.state = STATE_IDLE;
        this.targetPlayer = null;
        this.targetMob = null;
        this.priorityTarget = null;
        this.priorityExpireTime = 0;
        if (this.ability != null) {
            this.hp = (int) Math.max(10000, this.ability.get_hp_max(true));
            this.mp = (int) Math.max(3000, this.ability.get_mp_max(true));
        }
        this.isdie = false;
    }

    @Override
    public void join(Zone map, short x, short y) {
        this.type_pk = (byte) this.seaFlag;
        super.join(map, x, y);
        POOL.add(this);
        if (map != null) {
            try {
                map.change_flag(this, this.seaFlag);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void leave() {
        POOL.remove(this);
        super.leave();
    }

    @Override
    public void update() {
        if (!ThuLinhBienKhoi.isOpen()) {
            this.leave();
            return;
        }

        long now = System.currentTimeMillis();

        // 1. Xử lý trạng thái khi chết & hồi sinh mượt mà
        if (this.isdie) {
            if (respawnTime == 0) {
                respawnTime = now + ZUtil.random(3500, 6000);
            } else if (now >= respawnTime) {
                respawnTime = 0;
                this.isdie = false;
                this.state = STATE_IDLE;
                this.targetPlayer = null;
                this.targetMob = null;
                this.priorityTarget = null;
                if (this.ability != null) {
                    this.hp = (int) this.ability.get_hp_max(true);
                    this.mp = (int) this.ability.get_mp_max(true);
                }
                if (this.map != null) {
                    short[] pt = bot.SmartMovement.getRandomGroundPoint(this.map);
                    this.x = pt[0];
                    this.y = pt[1];
                    this.type_pk = (byte) this.seaFlag;
                    try {
                        this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                        this.map.change_flag(this, this.seaFlag);
                        for (Player p0 : this.map.players) {
                            if (p0 != null && p0.getService() != null) {
                                p0.getService().update_PK(this, false);
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
            return;
        }

        if (this.map == null || this.ability == null) {
            return;
        }

        long maxHp = this.ability.get_hp_max(true);
        double hpPercent = maxHp > 0 ? ((double) this.hp / maxHp) : 1.0;

        // 2. Cơ chế Sinh Tồn / Bỏ Chạy Khi Yếu Máu (Flee & Recovery AI)
        if (hpPercent < 0.30 && state != STATE_FLEE) {
            state = STATE_FLEE;
            fleeStartTime = now;
            escapeTargetX = -1;
            escapeTargetY = -1;
            if (now >= nextChatTime) {
                nextChatTime = now + ZUtil.random(10000, 18000);
                String[] fleeChats = {
                    "Máu quá yếu rồi, mau rút lui hồi phục!",
                    "Đợi ta hồi máu xong sẽ quay lại tính sổ!",
                    "Không ổn rồi, lùi lại thôi!",
                    "Đồng đội yểm trợ cho ta với!",
                    "Tránh xa ta ra tên kia!"
                };
                sendChatBubble(fleeChats[ZUtil.random(fleeChats.length)]);
            }
        }

        if (state == STATE_FLEE) {
            // Khi đã hồi phục an toàn (> 60% HP) -> Quay lại chiến trường!
            if (hpPercent >= 0.60) {
                state = STATE_COMBAT;
                targetPlayer = null;
                targetMob = null;
                if (now >= nextChatTime) {
                    nextChatTime = now + ZUtil.random(10000, 18000);
                    String[] reEngageChats = {
                        "Máu đã hồi đầy, xông lên tiêu diệt kẻ địch!",
                        "Ta đã trở lại, chiến tiếp nào!",
                        "Vì danh dự Băng " + (this.clan != null ? this.clan.name : "Hải Tặc") + ", tiến lên!",
                        "Nếm thử sức mạnh của ta đây!"
                    };
                    sendChatBubble(reEngageChats[ZUtil.random(reEngageChats.length)]);
                }
            } else {
                // Đang tháo chạy: Tự động bơm bình hồi phục HP/MP
                if (now >= nextPotionTime) {
                    nextPotionTime = now + 1800;
                    int heal = (int) (maxHp * 0.28);
                    this.hp = Math.min((int) maxHp, this.hp + heal);
                    this.mp = Math.min((int) this.ability.get_mp_max(true), this.mp + (int) (this.ability.get_mp_max(true) * 0.25));
                }

                // Tìm mối đe dọa gần nhất để chạy ngược hướng
                Player nearestThreat = findNearestEnemy();
                if (nearestThreat != null) {
                    // Nếu kẻ địch đuổi quá sát (< 75px), 40% cơ hội tung chiêu khống chế làm chậm truy đuổi
                    double threatDist = Math.hypot(nearestThreat.x - this.x, nearestThreat.y - this.y);
                    if (threatDist < 75 && now >= nextActionTime && ZUtil.random(100) < 40) {
                        castDefensiveSkill(nearestThreat);
                    }

                    // Tính toán điểm chạy trốn ngược hướng kẻ địch
                    int fleeDirX = this.x > nearestThreat.x ? 1 : -1;
                    escapeTargetX = (short) Math.max(80, Math.min(this.map.template.maxW - 80, this.x + fleeDirX * 350));
                    escapeTargetY = this.y;
                } else if (escapeTargetX == -1 || Math.hypot(escapeTargetX - this.x, escapeTargetY - this.y) < 30) {
                    short[] pt = bot.SmartMovement.getRandomGroundPoint(this.map);
                    escapeTargetX = pt[0];
                    escapeTargetY = pt[1];
                }

                // Di chuyển nhanh tháo chạy
                bot.SmartMovement.moveTowards(this, escapeTargetX, escapeTargetY, 48);
                return; // Khi đang chạy trốn, không thực hiện tấn công bình thường
            }
        }

        // 3. Tự động bơm máu duy trì khi ở trạng thái chiến đấu (HP < 60%)
        if (hpPercent < 0.60 && now >= nextPotionTime) {
            nextPotionTime = now + 2000;
            int heal = (int) (maxHp * 0.22);
            this.hp = Math.min((int) maxHp, this.hp + heal);
            this.mp = Math.min((int) this.ability.get_mp_max(true), this.mp + (int) (this.ability.get_mp_max(true) * 0.20));
        }

        // 4. Tìm kiếm mục tiêu (Targeting Logic)
        findBestTarget();

        // 5. Điều hướng chiến thuật (Tactical Movement & Kiting)
        handleTacticalMovement();

        // 6. Thực hiện tấn công với Action Delay & Combo Skill tự nhiên
        handleSmartCombat(now);

        super.update();
    }

    private Player findNearestEnemy() {
        if (this.map == null || this.map.players == null) return null;
        Player nearest = null;
        double minDist = Double.MAX_VALUE;
        for (Player p : this.map.players) {
            if (p != null && p.IDPlayer != this.IDPlayer && !p.isdie && !p.isSpectator) {
                if (this.canAttackTargetPlayer(p)) {
                    double d = Math.hypot(p.x - this.x, p.y - this.y);
                    if (d < minDist) {
                        minDist = d;
                        nearest = p;
                    }
                }
            }
        }
        return nearest;
    }

    private void castDefensiveSkill(Player threat) {
        if (threat == null || this.skill_point == null || this.skill_point.isEmpty()) return;
        long nowNano = System.nanoTime();
        for (Skill_info sk : this.skill_point) {
            if (sk != null && sk.temp != null && sk.temp.typeSkill != 2 && sk.temp.typeSkill != 3) {
                int skId = sk.temp.ID;
                if (!this.time_use_skill.containsKey(skId) || this.time_use_skill.get(skId) <= nowNano) {
                    long cdMs = this.calculateSkillCooldown(sk.temp);
                    this.time_use_skill.put(skId, (nowNano + cdMs * 1_000_000L));
                    this.applySkillCooldown(skId, sk.temp);
                    long dame = (this.ability != null ? this.ability.get_dame(true) : 1000) / 2;
                    try {
                        this.attackPlayer(new Player[]{threat}, skId, dame);
                    } catch (Exception ignored) {}
                    this.nextActionTime = System.currentTimeMillis() + cdMs;
                    break;
                }
            }
        }
    }

    private void findBestTarget() {
        if (this.map == null) return;

        // Ưu tiên 1: Trả thù kẻ vừa tấn công bot
        if (priorityTarget != null && priorityExpireTime > System.currentTimeMillis()
                && !priorityTarget.isdie && priorityTarget.map != null && priorityTarget.map.equals(this.map)) {
            if (this.canAttackTargetPlayer(priorityTarget)) {
                targetPlayer = priorityTarget;
                targetMob = null;
                return;
            }
        }

        // Ưu tiên 2: Tại map 179, săn Boss Quái Thú để kiếm 500 điểm cho Clan
        if (this.map.template.id == 179 && ThuLinhBienKhoi.mob != null && !ThuLinhBienKhoi.mob.isdie 
                && ThuLinhBienKhoi.mob.map != null && ThuLinhBienKhoi.mob.map.equals(this.map)) {
            if (priorityTarget == null || priorityExpireTime <= System.currentTimeMillis()) {
                if (ZUtil.random(100) < 45 || targetPlayer == null) {
                    targetMob = ThuLinhBienKhoi.mob;
                    targetPlayer = null;
                    return;
                }
            }
        }

        // Ưu tiên 3: Quét tìm người chơi / bot phe đối địch trong khu
        List<Player> enemies = new ArrayList<>();
        for (Player p : this.map.players) {
            if (p != null && p.IDPlayer != this.IDPlayer && !p.isdie && !p.isSpectator) {
                if (this.canAttackTargetPlayer(p)) {
                    enemies.add(p);
                }
            }
        }

        if (!enemies.isEmpty()) {
            Player bestEnemy = null;
            double bestScore = -Double.MAX_VALUE;

            for (Player enemy : enemies) {
                double dist = Math.hypot(enemy.x - this.x, enemy.y - this.y);
                long eHp = enemy.hp;
                long eMaxHp = enemy.ability != null ? enemy.ability.get_hp_max(true) : enemy.hp;
                double eHpRatio = eMaxHp > 0 ? ((double) eHp / eMaxHp) : 1.0;

                // Điểm ưu tiên: Càng gần càng tốt, nếu máu < 35% thì được cộng điểm lớn để dứt điểm (finisher)
                double score = 1000.0 - dist;
                if (eHpRatio < 0.35) {
                    score += 400.0;
                }
                if (score > bestScore) {
                    bestScore = score;
                    bestEnemy = enemy;
                }
            }

            targetPlayer = bestEnemy;
            targetMob = null;
            return;
        }

        targetPlayer = null;

        // Ưu tiên 4: Đánh quái trong map nếu không có đối thủ
        if (this.map.list_mob != null && this.map.list_mob.length > 0) {
            if (targetMob == null || targetMob.isdie || targetMob.map != this.map) {
                for (int mIndex : this.map.list_mob) {
                    Mob m = this.map.getMob(mIndex);
                    if (m != null && !m.isdie) {
                        targetMob = m;
                        break;
                    }
                }
            }
        } else {
            targetMob = null;
        }
    }

    private void handleTacticalMovement() {
        if (this.map == null) return;
        long now = System.currentTimeMillis();

        if (targetPlayer != null && !targetPlayer.isdie && targetPlayer.map == this.map) {
            double dist = Math.hypot(targetPlayer.x - this.x, targetPlayer.y - this.y);
            boolean isRanged = (this.clazz == 4 || this.clazz == 5); // Bác sĩ hoặc Xạ thủ

            if (isRanged) {
                // Xạ thủ / Bác sĩ: Thả diều (Kiting) - Giữ khoảng cách 120 - 220px
                if (dist < 85) {
                    // Đối thủ áp sát quá gần -> Lùi lại thả diều!
                    int retreatDir = this.x >= targetPlayer.x ? 1 : -1;
                    short newX = (short) Math.max(80, Math.min(this.map.template.maxW - 80, this.x + retreatDir * 120));
                    bot.SmartMovement.moveTowards(this, newX, this.y, 35);
                } else if (dist > 230) {
                    // Quá xa -> Tiến lại cự ly tầm bắn
                    bot.SmartMovement.moveTowards(this, targetPlayer.x, targetPlayer.y, 35);
                }
            } else {
                // Cận chiến (Võ sĩ, Kiếm sĩ, Đầu bếp): Áp sát cự ly 35 - 60px
                if (dist > 55) {
                    bot.SmartMovement.moveTowards(this, targetPlayer.x, targetPlayer.y, 40);
                } else {
                    // Đã ở sát mục tiêu -> Luồn lách nhẹ tránh đòn
                    if (now >= nextMoveTime) {
                        nextMoveTime = now + ZUtil.random(800, 1500);
                        short strafeX = (short) (targetPlayer.x + (ZUtil.random(100) < 50 ? 30 : -30));
                        bot.SmartMovement.moveTowards(this, strafeX, targetPlayer.y, 15);
                    }
                }
            }
            return;
        }

        if (targetMob != null && !targetMob.isdie && targetMob.map == this.map) {
            double dist = Math.hypot(targetMob.x - this.x, targetMob.y - this.y);
            if (dist > 60) {
                bot.SmartMovement.moveTowards(this, targetMob.x, targetMob.y, 35);
            }
            return;
        }

        // Không có mục tiêu -> Tuần tra ngẫu nhiên trên mặt đất
        if (now >= nextMoveTime) {
            nextMoveTime = now + ZUtil.random(2500, 4500);
            short[] pt = bot.SmartMovement.getRandomGroundPoint(this.map);
            patrolTargetX = pt[0];
            patrolTargetY = pt[1];
        }

        if (patrolTargetX != -1 && patrolTargetY != -1) {
            double dist = Math.hypot(patrolTargetX - this.x, patrolTargetY - this.y);
            if (dist > 20) {
                bot.SmartMovement.moveTowards(this, patrolTargetX, patrolTargetY, 25);
            }
        }
    }

    private void handleSmartCombat(long now) {
        if (targetPlayer == null && targetMob == null) return;
        if (now < nextActionTime) return;

        if (this.skill_point == null || this.skill_point.isEmpty()) {
            this.setupBotSkills(this.clazz, (short) Math.max(20, (int) this.level / 2), this.tier >= 1 ? 3 : 2, 0);
        }

        // Kiểm tra cự ly trước khi ra đòn
        double targetDist = targetPlayer != null 
                ? Math.hypot(targetPlayer.x - this.x, targetPlayer.y - this.y)
                : Math.hypot(targetMob.x - this.x, targetMob.y - this.y);

        boolean isRanged = (this.clazz == 4 || this.clazz == 5);
        if (!isRanged && targetDist > 85) return; // Cận chiến quá xa thì không đánh gió
        if (isRanged && targetDist > 260) return; // Tầm xa vượt cự ly thì tiến lại

        // Xoay vòng kỹ năng chủ động (Skill Rotation)
        long nowNano = System.nanoTime();
        List<Skill_info> readySpecialSkills = new ArrayList<>();
        Skill_info basicSkill = null;

        if (this.skill_point != null) {
            for (Skill_info sk : this.skill_point) {
                if (sk != null && sk.temp != null) {
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
        }

        Skill_info selectedSkill = null;
        if (!readySpecialSkills.isEmpty()) {
            selectedSkill = readySpecialSkills.get(ZUtil.random(readySpecialSkills.size()));
        } else if (basicSkill != null) {
            selectedSkill = basicSkill;
        } else if (this.skill_point != null && !this.skill_point.isEmpty()) {
            for (Skill_info sk : this.skill_point) {
                if (sk != null && sk.temp != null && sk.temp.typeSkill != 2 && sk.temp.typeSkill != 3) {
                    selectedSkill = sk;
                    break;
                }
            }
        }

        if (selectedSkill != null && selectedSkill.temp != null) {
            int idSkill = selectedSkill.temp.ID;
            long skillCdMs = this.calculateSkillCooldown(selectedSkill.temp);
            this.time_use_skill.put(idSkill, (nowNano + skillCdMs * 1_000_000L));
            this.applySkillCooldown(idSkill, selectedSkill.temp);

            long basicDelay = (basicSkill != null && basicSkill.temp != null) ? this.calculateSkillCooldown(basicSkill.temp) : skillCdMs;
            long actionDelay = (selectedSkill == basicSkill) ? skillCdMs : basicDelay;
            this.nextActionTime = now + actionDelay;

            long dame = this.ability.get_dame(true);
            dame = (dame * this.ability.get_dame_devil_percent()) / 100;

            EffTemplate eff = this.get_eff(5); // combo
            if (eff != null) dame *= 2;

            eff = this.get_eff(18); // bộc phá
            if (eff != null) dame = (dame * eff.param) / 100;

            // Cân bằng sát thương chống sốc đòn
            if (targetPlayer != null && targetPlayer.ability != null) {
                long tHpMax = targetPlayer.ability.get_hp_max(true);
                if (tHpMax > 0) {
                    long maxAllowed = Math.max(600, (long) (tHpMax * 0.14));
                    long minAllowed = Math.max(60, (long) (tHpMax * 0.03));
                    dame = Math.max(minAllowed, Math.min(maxAllowed, dame));
                }
            }

            try {
                if (targetPlayer != null) {
                    this.attackPlayer(new Player[]{targetPlayer}, idSkill, dame);
                } else if (targetMob != null) {
                    this.attackMob(new Mob[]{targetMob}, idSkill, dame);
                }
            } catch (Exception ignored) {}

            // Hội thoại chiến trận ngẫu nhiên
            if (now >= nextChatTime && ZUtil.random(100) < 30) {
                nextChatTime = now + ZUtil.random(12000, 20000);
                String[] battleChats = {
                    "Chiến đấu vì " + getSeaName() + "!",
                    "Băng " + (this.clan != null ? this.clan.name : "Hải Tặc") + " vô địch!",
                    "Đỡ lấy chiêu này!",
                    "Đứng lại đó, không được chạy!",
                    "Mau tiêu diệt hết kẻ địch!",
                    "Hải tặc chân chính không bao giờ lùi bước!"
                };
                sendChatBubble(battleChats[ZUtil.random(battleChats.length)]);
            }
        }
    }

    /**
     * Phân phối Bot số lượng lớn tới tất cả các bản đồ của Thủ Lĩnh Biển Khơi.
     */
    public static void dispatchBots() {
        clearBots();

        // 1. Phân bổ Bot trên 3 Map Chiến Trường & PvP chính
        int[] combatMaps = {179, 180, 184};

        for (int mapId : combatMaps) {
            Zone[] zones = Zone.getMapByID(mapId);
            if (zones == null || zones.length == 0 || zones[0] == null) continue;
            Zone zone = zones[0];

            // Số lượng bot mỗi cờ: Map 179 (4-5 bot/cờ = 16-20 bot), Map 180 (3-4 bot/cờ = 12-16 bot), Map 184 (3-4 bot/cờ = 12-16 bot)
            int botsPerSea = (mapId == 179) ? 4 : 3;

            for (int seaIdx = 0; seaIdx < 4; seaIdx++) {
                int flag = 4 + seaIdx;
                for (int b = 0; b < botsPerSea; b++) {
                    spawnSingleBot(zone, seaIdx, flag, mapId);
                }
            }
        }

        // 2. Phân bổ Bot bảo vệ căn cứ 4 Vùng Biển riêng biệt (182: Đông, 183: Tây, 181: Nam, 178: Bắc)
        int[] baseMaps = {182, 183, 181, 178};
        for (int seaIdx = 0; seaIdx < 4; seaIdx++) {
            int baseMapId = baseMaps[seaIdx];
            int flag = 4 + seaIdx;
            Zone[] zones = Zone.getMapByID(baseMapId);
            if (zones != null && zones.length > 0 && zones[0] != null) {
                Zone zone = zones[0];
                for (int b = 0; b < 2; b++) {
                    spawnSingleBot(zone, seaIdx, flag, baseMapId);
                }
            }
        }
    }

    private static void spawnSingleBot(Zone zone, int seaIdx, int flag, int mapId) {
        try {
            int botTier = (mapId == 179) ? (ZUtil.random(100) < 55 ? 2 : 1) : (ZUtil.random(100) < 50 ? 1 : 0);
            int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
            byte botClazz = (byte) ZUtil.random(1, 5);

            String botName = bot.botplayer.BotNameGenerator.getRandomBotName();
            Clan botClan = getRandomClanForSea(seaIdx);

            BotThuLinhBienKhoi bot = new BotThuLinhBienKhoi(botId, botName, flag, botTier);
            bot.isBot = true;
            bot.clan = botClan;
            bot.clazz = botClazz;

            short botLv;
            if (botTier == 0) {
                botLv = (short) ZUtil.random(45, 60);
            } else if (botTier == 1) {
                botLv = (short) ZUtil.random(65, 80);
            } else {
                botLv = (short) ZUtil.random(85, 100);
            }

            bot.level = botLv;
            if (zone != null && zone.players != null && !zone.players.isEmpty()) {
                BotBalanceEngine.balanceAgainstTeam(bot, zone.players, botLv);
            } else {
                bot.autoAllocatePotentialPoints(botClazz);
                int itemTier = (botTier == 2) ? 4 : (botTier == 1 ? 3 : 2);
                int fruitId = (botTier == 2) ? ZUtil.random(1, 15) : (botTier == 1 && ZUtil.random(100) < 60 ? ZUtil.random(1, 10) : 0);

                bot.setupBotEquip(botClazz, botLv, itemTier, botTier >= 1, true, botTier == 2);
                bot.setupBotSkills(botClazz, (short) Math.max(20, Math.min(30, botLv / 3 * 2)), itemTier, fruitId);
                bot.setupBotAppearance(botClazz, itemTier);
                bot.setin4();
                bot.init();
                bot.ability = new Ability(bot);
                bot.updateParts();
                if (bot.ability != null) {
                    bot.hp = (int) Math.max(15000, bot.ability.get_hp_max(true));
                    bot.mp = (int) Math.max(5000, bot.ability.get_mp_max(true));
                }
            }

            short spawnX = (short) ZUtil.random(150, Math.max(250, zone.template.maxW - 150));
            short spawnY = (short) ZUtil.random(200, Math.max(250, zone.template.maxH - 100));
            bot.join(zone, spawnX, spawnY);
        } catch (Exception ignored) {}
    }

    /**
     * Tự động kiểm tra và bổ sung bot định kỳ nếu số lượng bot bị giảm sút.
     */
    public static void checkAndMaintainBots() {
        if (!ThuLinhBienKhoi.isOpen()) {
            if (!POOL.isEmpty()) clearBots();
            return;
        }

        if (POOL.size() < 25) {
            dispatchBots();
        }
    }

    public static void clearBots() {
        for (BotThuLinhBienKhoi bot : POOL) {
            try {
                bot.leave();
            } catch (Exception ignored) {}
        }
        POOL.clear();
    }
}
