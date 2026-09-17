package bot;

import model.Player;
import ability.Ability;
import map.Zone;
import map.zones.WorldWar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import template.Item_wear;

/**
 * BotPVP — Hệ thống Bot PvP Lôi Đài & Đấu Trường Thông Minh.
 * Tích hợp cơ chế tự chọn Trạng thái chiến đấu (Combat Stance), auto phục hồi và mô phỏng chỉ số người chơi thật.
 */
public class BotPVP extends Bot {

    public enum CombatStance {
        AGGRESSIVE, // Tấn công áp sát dồn dập khi HP đối thủ > 50%
        DEFENSIVE,  // Rút lui giữ khoảng cách, bơm máu khi HP < 30%
        SUPPORT_HEAL // Ưu tiên hồi phục/buff (Doctor/Cook Clazz 3)
    }

    private CombatStance currentStance = CombatStance.AGGRESSIVE;
    private long lastStanceCheckTime = 0;
    private long worldWarRespawnTime = 0;

    public BotPVP(int id, String name) throws Exception {
        super(id, name);
        this.type_pk = -1;
        this.typePirate = -1;
    }

    public CombatStance getCurrentStance() {
        return currentStance;
    }

    @Override
    public void init() {
        this.type_pk = -1;
        this.typePirate = -1;
        this.worldWarRespawnTime = 0;
        this.setAttack(new AttackAround());
        this.setMove(new MoveToTarget(null, 2000));
        if (this.ability != null) {
            this.hp = (int) this.ability.get_hp_max(true);
            this.mp = (int) this.ability.get_mp_max(true);
        }
        this.isdie = false;
    }

    @Override
    public void update() {
        if (Zone.isWaitingOrUnstartedMatch(this.map)) {
            this.isdie = false;
            if (this.hp <= 0 && this.ability != null) {
                this.hp = (int) Math.max(1000, this.ability.get_hp_max(true));
                this.mp = (int) Math.max(500, this.ability.get_mp_max(true));
            }
            if (this.map == null || this.map.map_vp == null) {
                this.type_pk = -1;
            }
            return;
        }

        if (this.isdie) {
            // Kiểm tra nếu đang ở map Bảo Vệ Pháo Đài (267..271)
            if (this.map != null && (this.map.baoVePhaoDai != null || this.map.IsMapBaoVePhaoDai())) {
                long nowTime = System.currentTimeMillis();
                if (worldWarRespawnTime == 0) {
                    worldWarRespawnTime = nowTime + 8_000L;
                } else if (nowTime >= worldWarRespawnTime) {
                    worldWarRespawnTime = 0;
                    try {
                        short[] pt = bot.SmartMovement.getRandomGroundPoint(this.map);
                        this.x = pt[0];
                        this.y = pt[1];
                        this.isdie = false;
                        if (this.ability != null) {
                            this.hp = (int) this.ability.get_hp_max(true);
                            this.mp = (int) this.ability.get_mp_max(true);
                        }
                        this.type_pk = (byte) (this.clan != null && this.map.baoVePhaoDai != null && this.clan.equals(this.map.baoVePhaoDai.clanA) ? 4 : 5);
                        this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                        this.map.change_flag(this, this.type_pk);
                    } catch (Exception ignored) {}
                }
                return;
            }

            // Kiểm tra nếu đang ở map Trận Chiến Lớn (272..275)
            if (this.map != null && WorldWar.runnning && this.map.template.id >= 272 && this.map.template.id <= 275) {
                long nowTime = System.currentTimeMillis();
                if (worldWarRespawnTime == 0) {
                    // Chờ ngẫu nhiên 0 - 3 giây trước khi thoát ra vào lại
                    worldWarRespawnTime = nowTime + core.ZUtil.random(3001);
                } else if (nowTime >= worldWarRespawnTime) {
                    worldWarRespawnTime = 0;
                    try {
                        // 1. Gửi gói tin biến mất (rời map) tới tất cả người chơi khác
                        network.Message m = new network.Message(3);
                        m.writer().writeShort(this.index_map);
                        m.writer().writeByte(2); // type left
                        for (int i = 0; i < this.map.players.size(); i++) {
                            Player p0 = this.map.players.get(i);
                            if (p0 != null && p0.IDPlayer != this.IDPlayer) {
                                p0.addmsg(m);
                                p0.id_meet_in_map.remove("" + this.index_map);
                            }
                        }
                        m.cleanup();
                        
                        // 2. Cập nhật tọa độ ngẫu nhiên mới và hồi sinh trong vùng an toàn
                        short[] pt = bot.SmartMovement.getRandomGroundPoint(this.map);
                        this.x = pt[0];
                        this.y = pt[1];
                        this.isdie = false;
                        this.hp = this.ability != null ? (int) this.ability.get_hp_max(true) : 10000;
                        this.mp = this.ability != null ? (int) this.ability.get_mp_max(true) : 1000;
                        
                        // 3. Gửi gói tin di chuyển mới để client các người chơi khác nhận diện và nạp lại thông tin NPC/Bot
                        this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                        
                        // 4. Đồng bộ lại cờ PK
                        WorldWar.setType(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return;
        }

        if (this.getAttack() == null) {
            this.setAttack(new AttackAround());
        }
        if (this.getMove() == null) {
            this.setMove(new MoveToTarget(null, 2000));
        }

        // Tự động kiểm tra và đồng bộ cờ PK trong PvP Băng (Map 120)
        if (this.map != null && (this.map.pvpBangMapFight != null || (this.map.template != null && this.map.template.id == 120))) {
            if (this.map.pvpBangMapFight != null && this.map.pvpBangMapFight.status_pvp == 3) {
                byte desiredPk = (byte) (this.clan != null && this.clan.equals(this.map.pvpBangMapFight.clan1) ? 4 : 5);
                if (this.type_pk != desiredPk) {
                    this.type_pk = desiredPk;
                    try { this.map.change_flag(this, desiredPk); } catch (Exception ignored) {}
                }
            }
        }

        // Cập nhật trạng thái chiến đấu (Combat Stance) ngẫu nhiên và linh hoạt theo tỉ lệ HP thực
        long now = System.currentTimeMillis();
        if (now - lastStanceCheckTime > 2500) {
            lastStanceCheckTime = now;
            if (this.ability != null) {
                long hpMax = this.ability.get_hp_max(true);
                double hpRatio = hpMax > 0 ? (double) this.hp / hpMax : 1.0;
                
                if (hpRatio < 0.35) {
                    currentStance = CombatStance.DEFENSIVE;
                    // Evasive Strafe (Né skill): Di chuyển né vệt bắn khi HP thấp nhưng luôn an toàn trong biên map
                    short newX = (short) (this.x + core.ZUtil.random(-40, 40));
                    short newY = (short) (this.y + core.ZUtil.random(-25, 25));
                    bot.SmartMovement.moveTowards(this, newX, newY, 9999);
                } else if (this.clazz == 3 && hpRatio < 0.65) {
                    currentStance = CombatStance.SUPPORT_HEAL;
                } else {
                    currentStance = CombatStance.AGGRESSIVE;
                }
            }
        }

        super.update();
    }

    @Override
    public void join(Zone map, short x, short y) {
        if (map != null && Zone.isWaitingOrUnstartedMatch(map)) {
            this.type_pk = -1;
            this.setAttack(null);
            this.setMove(null);
        } else {
            if (this.type_pk <= 0 && map != null && map.pvpBangMapFight != null) {
                this.type_pk = (byte) (this.clan != null && this.clan.equals(map.pvpBangMapFight.clan1) ? 4 : 5);
            }
            if (this.getAttack() == null) this.setAttack(new AttackAround());
            if (this.getMove() == null) this.setMove(new MoveToTarget(null, 2000));
        }
        super.join(map, x, y);
    }

    @Override
    public void leave() {
        super.leave();
        this.setAttack(null);
        this.setMove(null);
    }

    public static BotPVP create(int id, String username, String name, Zone targetMap) {
        return BotFactory.getInstance().newBotPVP(id, username, name, targetMap);
    }

    /**
     * Priority 2: Fast lookup for offline player / bot profile from RAM cache (`BotPvpCacheManager`).
     * Prevents continuous SQL performance bottlenecks.
     */
    public static BotPVP findOfflineSqlBot(Player p) {
        if (p == null) return null;
        
        bot.botplayer.ai.BotPvpCacheManager.BotProfile profile = bot.botplayer.ai.BotPvpCacheManager.findCachedProfile(p);
        String botName = (profile != null && profile.name != null) ? profile.name : bot.botplayer.BotNameGenerator.getRandomBotName();
        byte clazz = (profile != null && profile.clazz >= 1 && profile.clazz <= 5) ? profile.clazz : (byte) core.ZUtil.random(1, 5);

        try {
            int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
            BotPVP bot = new BotPVP(botId, botName);
            bot.isBot = true;
            bot.clazz = clazz;
            bot.level = (short) Math.max(1, p.level + core.ZUtil.random(-1, 2));
            if (p.map != null) bot.map = p.map;

            // Cân bằng toàn diện theo người chơi thật p (Hoàn Mỹ, Khảm Đá, Kích Ẩn, TAQ, Tiềm năng, Skills)
            bot.setupBotBalancedAgainst(p);

            bot.setAttack(new AttackAround());
            bot.setMove(new MoveToTarget(null, 2000));
            return bot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Priority 3: Khởi tạo Bot PVP Phân tích Chỉ số Cân bằng trong RAM.
     * Cấp độ sát người chơi (±1..2 cấp), Trang bị theo Tier chuẩn level, HP/MP/Dame cân bằng thích ứng.
     */
    public static BotPVP createAnalyzedBot(int id, Player p) {
        try {
            byte clazz = (byte) core.ZUtil.random(1, 5);
            if (p != null && p.clazz >= 1 && p.clazz <= 5 && clazz == p.clazz && core.ZUtil.random(100) < 80) {
                clazz = (byte) ((clazz % 5) + 1);
            }

            String fakeName = bot.botplayer.BotNameGenerator.getRandomBotName();
            BotPVP bot = new BotPVP(id, fakeName);
            bot.isBot = true;
            bot.clazz = clazz;
            bot.level = (short) Math.max(1, p != null ? (p.level + core.ZUtil.random(-1, 2)) : 50);
            if (p != null && p.map != null) {
                bot.map = p.map;
            } else {
                try {
                    bot.map = Zone.getMapByID(1)[0];
                } catch (Exception ignored) {}
            }

            // Cân bằng toàn diện theo người chơi thật p (Hoàn Mỹ, Khảm Đá, Kích Ẩn, TAQ, Tiềm năng, Skills)
            bot.setupBotBalancedAgainst(p);

            bot.setAttack(new AttackAround());
            bot.setMove(new MoveToTarget(null, 2000));
            return bot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BotPVP createFakeBot(int id, int targetLevel) {
        Player temp = new Player(null, "temp");
        temp.level = (short) targetLevel;
        return createAnalyzedBot(id, temp);
    }

    private static template.ItemTemplate3 findSuitableItemTemplate(byte clazz, byte typeEquip, int targetLevel) {
        template.ItemTemplate3 best = null;
        int minDiff = Integer.MAX_VALUE;
        for (template.ItemTemplate3 it : template.ItemTemplate3.ENTRYS) {
            if (it != null && (it.clazz == clazz || it.clazz == 0) && it.typeEquip == typeEquip) {
                int diff = Math.abs(it.level - targetLevel);
                if (diff < minDiff) {
                    minDiff = diff;
                    best = it;
                }
            }
        }
        return best;
    }

    private static short getDefaultHead(int clazz) {
        return (short) (clazz - 1);
    }

    private static short getDefaultHair(int clazz) {
        switch (clazz) {
            case 1: return 1;
            case 2: return 24;
            case 3: return 28;
            case 4: return 32;
            case 5: return 36;
            default: return 1;
        }
    }

    /**
     * Khởi tạo Bot Trận Chiến Lớn hoàn toàn động từ RAM (Code Init, KHÔNG truy vấn SQL `players_bot`).
     * VipTier: 0 = Cùi (đồ thường, lv vừa), 1 = Vừa Phải (đồ trung bình, trang bị vừa), 2 = VIP (đồ cao cấp, chỉ số cao).
     */
    public static BotPVP createDynamicWorldWarBot(int id, byte faction, int targetLevel, int vipTier) {
        try {
            byte clazz = (byte) core.ZUtil.random(1, 5);
            String[] vGamerNames = {
                "ThanhPhong", "TieuBao", "HiepKhach", "PhongVan", "KiemMa", "KiemDoc", "DocBa", "VoDich", "HuyenThoai", "ThoSan", "HacLong", "BachLong",
                "SatThu", "ChienThan", "VuongGia", "LangTu", "LaoDai", "TieuMuoi", "DaiCa", "SieuNhan", "PhiLong", "HoangDe", "QuanSu", "PhongThan",
                "SonTung", "AnhTuan", "HoangNam", "QuocAnh", "MinhQuan", "DucThinh", "TienDat", "GiaBao", "HuuTho", "NgocLinh", "PhuongAnh", "KhanhHuyen",
                "MinhThu", "ThuTrang", "ThuyTien", "BichPhuong", "MyLinh", "ThanhHang", "BaoNgoc", "QuynhTrang", "MinhTri", "QuangVinh", "DucManh", "ThanhSon",
                "ZoroHaiTac", "LuffyMuRom", "SanjiDauBep", "AceLua", "ShanksTocDo", "LawRoom", "KaidoRong", "NamiClima", "RobinKhaoCo", "ChopperYSi",
                "SaboLua", "YamatoThan", "MihawkKiem", "KidKien", "SmokerKhoi", "HancockYeu", "KobyAnhHung", "GarpNamDam", "AokijiBang", "KizaruAnhSang"
            };
            
            String botName = vGamerNames[core.ZUtil.random(vGamerNames.length)];
            if (core.ZUtil.random(100) < 30) {
                String[] simpleSuffix = {"Pro", "VN", "Kute", "PVP", "Gamer", "Solo", "Top", "99", "102"};
                botName += simpleSuffix[core.ZUtil.random(simpleSuffix.length)];
            } else if (core.ZUtil.random(100) < 20) {
                botName += core.ZUtil.random(10, 99);
            }

            BotPVP bot = new BotPVP(id, botName);
            bot.isBot = true;
            bot.clazz = clazz;
            bot.level = (short) Math.max(10, Math.min(100, targetLevel + core.ZUtil.random(-3, 3)));
            bot.huongnghiep = faction;
            bot.type_pk = (faction == 1) ? (byte) 11 : ((faction == 2) ? (byte) 13 : (byte) 12);
            bot.head = (short) (clazz - 1);
            bot.hair = getDefaultHair(clazz);

            // Phân bổ điểm tiềm năng theo class & level
            bot.autoAllocatePotentialPoints(clazz);

            // Cấu hình trang bị tương ứng VipTier (0=Cùi, 1=Vừa, 2=VIP/Thần Thoại)
            double botTier = (vipTier >= 2) ? (bot.level >= 80 ? 3.5 : 3.0) : ((vipTier == 1) ? 2.0 : 1.0);
            boolean allowHM = (vipTier >= 2 || (vipTier == 1 && core.ZUtil.random(100) < 30));
            boolean allowKA = (vipTier >= 2 || (vipTier == 1 && core.ZUtil.random(100) < 30));
            boolean allowKD = (vipTier >= 1);
            int fruitId = (vipTier >= 2 && bot.level >= 70) ? Player.BOT_TOP_TIER_FRUITS[core.ZUtil.random(Player.BOT_TOP_TIER_FRUITS.length)] : 0;
            int chosenThanTrangSet = Player.selectThanTrangSetIndex(clazz, fruitId, bot.name);
            bot.setupBotEquip(clazz, bot.level, botTier, allowHM, allowKA, allowKD, chosenThanTrangSet);

            // Kỹ năng theo cấp độ
            short skillLv = (short) Math.max(5, Math.min(30, bot.level / 3));
            bot.setupBotSkills(clazz, skillLv, botTier, fruitId);

            // Ngoại hình tương ứng VipTier
            bot.setupBotAppearance(clazz, botTier);

            bot.ability = new Ability(bot);
            bot.setin4();
            bot.init();
            bot.updateParts();

            int lv = (int) bot.level;
            long calculatedHp = Math.max(5000L, 5000L + lv * 800L + (lv > 50 ? (lv - 50L) * 1500L : 0L));
            long calculatedMp = Math.max(1500L, 1000L + lv * 100L);
            long calculatedDame = Math.max(1000L, 800L + lv * 150L + (lv > 50 ? (lv - 50L) * 300L : 0L));
            long calculatedDef = Math.max(400L, 300L + lv * 60L + (lv > 50 ? (lv - 50L) * 120L : 0L));
            
            // Stats boost nhẹ theo VipTier (0: 100%, 1: 120%, 2: 145% - Vừa phải, không "ảo")
            double multiplier = (vipTier == 2) ? 1.45 : ((vipTier == 1) ? 1.20 : 1.0);
            bot.hpMax = (int) Math.max(2000, calculatedHp * multiplier);
            bot.hp = bot.hpMax;
            bot.mpMax = (int) Math.max(1000, calculatedMp * multiplier);
            bot.mp = bot.mpMax;
            bot.dame = (int) Math.max(500, calculatedDame * multiplier);
            bot.damePercent = 1000;
            bot.def = (int) Math.max(200, calculatedDef * multiplier);
            bot.defPercent = 0;
            bot.agility = 300;
            bot.crit = (int) (250 * multiplier);
            bot.pierce = (int) (200 * multiplier);
            bot.miss = 100;
            bot.resPhys = (int) (150 * multiplier);
            bot.resMag = (int) (150 * multiplier);
            bot.isBalancedStats = true;
            bot.isdie = false;

            bot.setAttack(new AttackAround());
            bot.setMove(new MoveToTarget(null, 2000));
            return bot;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
