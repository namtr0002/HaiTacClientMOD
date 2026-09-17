package bot;

import ability.Ability;
import clan.Clan;
import clan.ClanMember;
import map.Vgo;
import map.Zone;
import model.Player;
import map.zones.ChiemDao;
import mob.Mob;
import core.ZUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BotChiemDao — Hệ thống Bot Hoạt Động & Phó Bản Chiếm Đảo Bang Hội.
 *
 * <p>Tính năng:
 * <ul>
 *   <li><b>Chế độ Map Làng / Cổng Đảo (25, 33, 49, 69, 83)</b>: Xuất hiện ở Khu 0, tự động di chuyển
 *       tuần tra quanh khu vực cổng Vgo chiếm đảo, chat tạo không khí chuẩn bị giao tranh.</li>
 *   <li><b>Chế độ Chiến Trường (5 Đảo 261..265)</b>: Cân bằng số lượng giữa 5 đảo, phân chia Phe Thủ (Team 5)
 *       và Phe Công (Team 4).</li>
 *   <li><b>AI Tấn Công Trụ Chuẩn Logic</b>: Phe Công ưu tiên diệt toàn bộ Trụ Phụ (mob 132) trước,
 *       sau khi sạch Trụ Phụ mới chuyển sang tấn công Trụ Chính (mob 133).</li>
 * </ul>
 */
public class BotChiemDao extends Bot {

    public static final List<BotChiemDao> POOL = new CopyOnWriteArrayList<>();

    public int team; // 4 = Phe Công, 5 = Phe Thủ / Chiếm giữ
    public boolean isVillageBot = false;
    public int targetGateMapId = 25;
    public short gateX = 0;
    public short gateY = 0;

    public boolean isReturningToArena = false;
    public Zone targetArenaZone = null;
    public int targetArenaMapId = 261;
    public long reEnterArenaTime = 0;

    private long respawnTime = 0;
    private long lastAtkTime = 0;
    private long lastChatTime = 0;
    private long lastPatrolTime = 0;

    public BotChiemDao(int id, String name, int team, boolean isVillageBot) throws Exception {
        super(id, name);
        this.team = team;
        this.isVillageBot = isVillageBot;
        this.type_pk = (byte) (isVillageBot ? -1 : team);
        this.disableBaseChat = true;
    }

    @Override
    public void init() {
        this.type_pk = (byte) (this.isVillageBot ? -1 : this.team);
        this.respawnTime = 0;
        this.isReturningToArena = false;
        this.reEnterArenaTime = 0;
        this.lastChatTime = System.currentTimeMillis() + ZUtil.random(5000, 15000);
        this.lastPatrolTime = 0;

        if (this.isVillageBot) {
            this.setAttack(null);
            this.setMove(new MoveAround(3000L));
        } else {
            this.setAttack(new AttackAround());
            this.setMove(new MoveAround(2500L));
        }

        if (this.ability != null) {
            this.hp = (int) Math.max(5000, this.ability.get_hp_max(true));
            this.mp = (int) Math.max(2000, this.ability.get_mp_max(true));
        }
        this.isdie = false;
    }

    @Override
    public void join(Zone map, short x, short y) {
        this.type_pk = (byte) (this.isVillageBot ? -1 : this.team);
        if (this.isVillageBot) {
            this.setAttack(null);
            this.setMove(new MoveAround(3000L));
        } else {
            this.setAttack(new AttackAround());
            this.setMove(new MoveAround(2500L));
        }
        super.join(map, x, y);
        POOL.add(this);
        if (!this.isVillageBot && this.map != null && this.map.players != null) {
            for (int i = 0; i < this.map.players.size(); i++) {
                Player p0 = this.map.players.get(i);
                if (p0 != null && p0.getService() != null && !p0.equals(this)) {
                    try {
                        p0.getService().update_PK(this, false);
                        if (this.clan != null) {
                            Clan.send_me_to_other(this, p0, false);
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    @Override
    public void leave() {
        POOL.remove(this);
        super.leave();
    }

    @Override
    public void update() {
        if (this.map == null) return;
        long now = System.currentTimeMillis();

        // 1. Logic cho Bot ở Map Làng / Cổng Đảo
        if (this.isVillageBot) {
            // Tìm tọa độ cổng Vgo nếu chưa xác định
            if (this.gateX == 0 || this.gateY == 0) {
                locateGateCoords();
            }

            // Tự động di chuyển tuần tra quanh cổng Vgo
            if (now - lastPatrolTime > 4000) {
                lastPatrolTime = now;
                if (this.gateX > 0 && this.gateY > 0) {
                    int minX = bot.SmartMovement.getSafeMinX(this.map);
                    int maxX = bot.SmartMovement.getSafeMaxX(this.map);
                    int minY = bot.SmartMovement.getSafeMinY(this.map);
                    int maxY = bot.SmartMovement.getSafeMaxY(this.map);
                    short targetX = (short) Math.max(minX, Math.min(maxX, this.gateX + ZUtil.random(-60, 60)));
                    short targetY = (short) Math.max(minY, Math.min(maxY, this.gateY + ZUtil.random(-25, 25)));
                    try {
                        this.x = targetX;
                        this.y = targetY;
                        this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                    } catch (Exception ignored) {}
                }
            }

            // Chat định kỳ tạo không khí chuẩn bị Chiếm Đảo
            if (now >= this.lastChatTime) {
                this.lastChatTime = now + ZUtil.random(18000, 35000);
                speakVillageChat();
            }
            return;
        }

        // 2. Logic khi Bot trong Map Chiến Trường Chiếm Đảo (261..265) bị chết -> Hồi sinh về Cổng Đảo (254..258)
        if (this.isdie) {
            if (respawnTime == 0) {
                respawnTime = now + 5000L;
            } else if (now >= respawnTime) {
                respawnTime = 0;
                this.isdie = false;
                if (this.ability != null) {
                    this.hp = (int) this.ability.get_hp_max(true);
                    this.mp = (int) this.ability.get_mp_max(true);
                }

                // Lưu lại Map Đấu Trường đích đến
                if (this.map != null && this.map.template != null) {
                    this.targetArenaZone = this.map;
                    this.targetArenaMapId = this.map.template.id;
                } else if (this.targetArenaMapId <= 0) {
                    this.targetArenaMapId = 261;
                }

                int gateMapId = Zone.getIslandEntranceMap(this.targetArenaMapId);
                Zone[] gateZones = Zone.getMapByID(gateMapId);
                if (gateZones != null && gateZones.length > 0 && gateZones[0] != null) {
                    Zone gateZone = gateZones[0];
                    this.leave();

                    short gateSpawnX = (short) (349 + ZUtil.random(-30, 30));
                    short gateSpawnY = (short) (345 + ZUtil.random(-20, 20));
                    this.join(gateZone, gateSpawnX, gateSpawnY);
                    this.type_pk = -1; // Cổng đảo an toàn

                    this.isReturningToArena = true;
                    this.reEnterArenaTime = now + ZUtil.random(2000, 3500); // 2-3.5s di chuyển tới cổng Vgo rồi vào lại

                    // Tìm tọa độ Vgo trong map Cổng Đảo hướng vào Đấu Trường để bot di chuyển tới
                    short targetVgoX = gateSpawnX;
                    short targetVgoY = gateSpawnY;
                    if (gateZone.template != null && gateZone.template.vgos != null) {
                        for (Vgo v : gateZone.template.vgos) {
                            if (v != null && (v.id_map_go == this.targetArenaMapId || (v.id_map_go >= 261 && v.id_map_go <= 265))) {
                                targetVgoX = v.xold;
                                targetVgoY = v.yold;
                                break;
                            }
                        }
                    }
                    try {
                        this.x = targetVgoX;
                        this.y = targetVgoY;
                        this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                    } catch (Exception ignored) {}
                }
            }
            return;
        }

        // 3. Logic Bot đang ở Cổng Đảo (254..258) chuẩn bị bước qua Vgo vào Đấu Trường (261..265)
        if (this.isReturningToArena) {
            if (now >= this.reEnterArenaTime) {
                this.isReturningToArena = false;
                Zone destArena = (this.targetArenaZone != null && this.targetArenaZone.isRunning()) 
                        ? this.targetArenaZone 
                        : (Zone.getMapByID(this.targetArenaMapId) != null && Zone.getMapByID(this.targetArenaMapId).length > 0 ? Zone.getMapByID(this.targetArenaMapId)[0] : null);

                if (destArena != null) {
                    this.leave();

                    int minX = bot.SmartMovement.getSafeMinX(destArena);
                    int maxX = bot.SmartMovement.getSafeMaxX(destArena);
                    int minY = bot.SmartMovement.getSafeMinY(destArena);
                    int maxY = bot.SmartMovement.getSafeMaxY(destArena);
                    short enterX = (short) (this.team == 4 ? ZUtil.random(minX + 40, minX + 240) : ZUtil.random(Math.max(minX + 240, maxX - 240), maxX - 40));
                    short enterY = (short) ZUtil.random(Math.max(minY, maxY - 100), maxY);

                    this.join(destArena, enterX, enterY);
                    this.type_pk = (byte) this.team;
                    if (destArena.players != null) {
                        for (int i = 0; i < destArena.players.size(); i++) {
                            Player p0 = destArena.players.get(i);
                            if (p0 != null && p0.getService() != null && !p0.equals(this)) {
                                try {
                                    p0.getService().update_PK(this, false);
                                    if (this.clan != null) {
                                        Clan.send_me_to_other(this, p0, false);
                                    }
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                    try {
                        this.map.getService().move((byte) 0, this.index_map, this.x, this.y);
                    } catch (Exception ignored) {}
                }
            }
            return;
        }

        // Tấn công Trụ: Phe Công (team 4) ưu tiên Trụ Phụ -> hết Trụ Phụ mới đánh Trụ Chính
        if (this.team == 4 && now - lastAtkTime > 2500 && this.map != null) {
            Mob targetTurret = null;

            // 2.1. Tìm Trụ Phụ (mob 132) còn sống
            if (this.map.mobs != null) {
                for (Mob m : this.map.mobs.values()) {
                    if (m != null && m.mtemplate != null && m.mtemplate.mob_id == 132 && !m.isdie && m.hp > 0) {
                        targetTurret = m;
                        break;
                    }
                }
            }
            if (targetTurret == null && this.map.list_mob != null) {
                for (int idx : this.map.list_mob) {
                    Mob m = this.map.getMob(idx);
                    if (m != null && m.mtemplate != null && m.mtemplate.mob_id == 132 && !m.isdie && m.hp > 0) {
                        targetTurret = m;
                        break;
                    }
                }
            }

            // 2.2. Nếu toàn bộ Trụ Phụ đã bị diệt -> Nhắm Trụ Chính (mob 133)
            if (targetTurret == null && ChiemDao.getAliveSubTurretCount(this.map) == 0) {
                if (this.map.map_dungeon instanceof ChiemDao) {
                    ChiemDao cd = (ChiemDao) this.map.map_dungeon;
                    if (cd.mainTurret != null && !cd.mainTurret.isdie && cd.mainTurret.hp > 0) {
                        targetTurret = cd.mainTurret;
                    }
                } else if (this.map.mobs != null) {
                    for (Mob m : this.map.mobs.values()) {
                        if (m != null && m.mtemplate != null && m.mtemplate.mob_id == 133 && !m.isdie && m.hp > 0) {
                            targetTurret = m;
                            break;
                        }
                    }
                }
                if (targetTurret == null && this.map.list_mob != null) {
                    for (int idx : this.map.list_mob) {
                        Mob m = this.map.getMob(idx);
                        if (m != null && m.mtemplate != null && m.mtemplate.mob_id == 133 && !m.isdie && m.hp > 0) {
                            targetTurret = m;
                            break;
                        }
                    }
                }
            }

            if (targetTurret != null) {
                double dist = Math.hypot(this.x - targetTurret.x, this.y - targetTurret.y);
                if (dist <= 250) {
                    lastAtkTime = now;
                    int dame = (this.ability != null && this.ability.get_dame(true) > 0) ? this.ability.get_dame(true) : 1500;
                    try {
                        this.map.Fire_Monster(new Mob[]{targetTurret}, this, 0, dame);
                    } catch (Exception ignored) {}
                }
            }
        }

        super.update();
    }

    private void locateGateCoords() {
        if (this.map == null || this.map.template == null) return;
        if (this.map.template.vgos != null) {
            for (Vgo v : this.map.template.vgos) {
                if (v != null && ((v.id_map_go >= 254 && v.id_map_go <= 258) || (v.id_map_go >= 261 && v.id_map_go <= 265))) {
                    this.gateX = v.xold;
                    this.gateY = v.yold;
                    return;
                }
            }
        }
        this.gateX = (short) (this.map.template.maxW / 2);
        this.gateY = (short) (this.map.template.maxH / 2);
    }

    private void speakVillageChat() {
        if (this.map == null) return;
        String[] chats = {
            "Chuẩn bị tập hợp tại cổng Chiếm Đảo thôi anh em ơi!",
            "Cổng đảo mở là xông vào ngay nhé mọi người!",
            "Đảo này chắc chắn sẽ thuộc về bang chúng ta!",
            "Anh em nhớ tiêu diệt hết Trụ Phụ trước rồi mới đánh Trụ Chính nhé!",
            "Ai vào Chiếm Đảo cùng bang không, ghép phó bản nào!"
        };
        String msg = chats[ZUtil.random(chats.length)];
        try {
            this.map.send_chat_popup(0, this.index_map, msg);
        } catch (Exception ignored) {}
    }

    // =========================================================================
    //  STATIC DISPATCHERS & BOT MANAGEMENT
    // =========================================================================

    /**
     * Vô hiệu hóa việc spawn bot tĩnh đứng ở làng theo yêu cầu chuẩn hóa hệ thống AI:
     * Bot phải tự cày, tự học hành vi người chơi, không đứng tụ tập nhân tạo ở làng.
     */
    public static void dispatchVillageBots() {
        clearVillageBots();
    }

    /**
     * Phân bổ Bot chiến đấu ĐÔNG ĐẢO, ĐA DẠNG NHIỀU PHE BANG HỘI cho toàn bộ CẢ 5 ĐẢO (map 261..265).
     */
    public static void dispatchAllIslands() {
        clearIslandBots();

        int[] islandMaps = {261, 262, 263, 264, 265};
        int[] gateIds = {25, 33, 49, 69, 83};
        
        String[][] clanThemes = {
            {"Băng Mũ Rơm", "Băng Tóc Đỏ", "Băng Bách Thú", "Băng Râu Đen", "Băng Heart"},
            {"Băng Râu Trắng", "Băng Big Mom", "Băng Phượng Hoàng", "Băng Siêu Tân Tinh", "Băng Hắc Ám"},
            {"Băng Cuồng Nộ", "Băng Hải Vương", "Băng Đại Hải Trình", "Băng Hỏa Quyền", "Băng Mặt Trời"},
            {"Băng Quái Thú", "Băng Chiến Hạm", "Băng Cuồng Phong", "Băng Sấm Sét", "Băng Thợ Săn"},
            {"Băng Bá Vương", "Băng Quỷ Đỏ", "Băng Huyền Thoại", "Băng Tân Thế Giới", "Băng Vô Địch"}
        };

        for (int i = 0; i < islandMaps.length; i++) {
            int islandMapId = islandMaps[i];
            int gateId = gateIds[i];

            Zone[] zones = Zone.getMapByID(islandMapId);
            if (zones == null || zones.length == 0 || zones[0] == null) continue;
            Zone zone = zones[0];

            Clan occupyingClan = ChiemDao.getClanTop(gateId);
            if (occupyingClan == null) {
                occupyingClan = new Clan();
                occupyingClan.id = (short) (-800 - i);
                occupyingClan.name = "Băng Trấn Thủ Đảo " + (i + 1);
                occupyingClan.icon = (short) ZUtil.random(1, 10);
                occupyingClan.level = (short) (10 + (i % 5));
                occupyingClan.trungsinh = (byte) (1 + (i % 4));
                occupyingClan.xp = ZUtil.random(5000, 20000);
                occupyingClan.members = new ArrayList<>();
                clan.ClanMember leader = new clan.ClanMember();
                leader.id = occupyingClan.id;
                leader.name = "Đảo Chủ " + (i + 1);
                leader.level = (short) (85 + i * 2);
                leader.levelInclan = 0;
                occupyingClan.members.add(leader);
            }

            // 1. Phe Phòng Thủ (Team 5): 6 bot bảo vệ Trụ thuộc clan thủ đảo
            autoFill(zone, 5, 6, (short) 80, occupyingClan);

            // 2. Phe Tấn Công (Team 4): Tạo 4 - 5 Bang Hội TẤN CÔNG KHÁC NHAU cùng tham gia công đảo tranh cướp
            int numAttackingClans = 4;
            String[] namesForIsland = (i < clanThemes.length) ? clanThemes[i] : clanThemes[0];
            for (int ac = 0; ac < numAttackingClans; ac++) {
                Clan attackingClan = new Clan();
                attackingClan.id = (short) (-850 - (i * 20) - ac);
                attackingClan.name = (ac < namesForIsland.length) ? namesForIsland[ac] : bot.botplayer.BotNameGenerator.generateRandomClanName();
                attackingClan.icon = (short) ZUtil.random(1, 15);
                attackingClan.level = (short) (8 + ZUtil.random(1, 6));
                attackingClan.trungsinh = (byte) ZUtil.random(1, 3);
                attackingClan.xp = ZUtil.random(3000, 15000);
                attackingClan.members = new ArrayList<>();
                clan.ClanMember aLeader = new clan.ClanMember();
                aLeader.id = attackingClan.id;
                aLeader.name = "Thuyền Trưởng " + attackingClan.name;
                aLeader.level = (short) (80 + ZUtil.random(10));
                aLeader.levelInclan = 0;
                attackingClan.members.add(aLeader);
                // Mỗi Bot Clan công đảo gồm 3 - 4 thành viên (tổng 12 - 16 bot công mỗi đảo)
                int botCountInClan = ZUtil.random(3, 4);
                autoFill(zone, 4, botCountInClan, (short) 80, attackingClan);
            }
        }
    }

    public static void autoFill(Zone map, int team, int count, short avgLv, Clan clan) {
        if (map == null || count <= 0) return;
        short lv = (avgLv > 0) ? avgLv : 75;

        for (int i = 0; i < count; i++) {
            try {
                int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                byte clazz = (byte) ((i % 5) + 1);
                String name = bot.botplayer.BotNameGenerator.getRandomBotName();

                BotChiemDao bot = new BotChiemDao(botId, name, team, false);
                bot.isVillageBot = false;
                bot.level = (short) (lv + ZUtil.random(-3, 3));
                bot.clazz = clazz;
                bot.clan = clan;

                if (map.players != null && !map.players.isEmpty()) {
                    BotBalanceEngine.balanceAgainstTeam(bot, map.players, lv);
                } else {
                    int tier = bot.level >= 90 ? 4 : (bot.level >= 75 ? 3 : 2);
                    bot.autoAllocatePotentialPoints(clazz);
                    bot.setupBotEquip(clazz, bot.level, tier, true, true, true);
                    bot.setupBotSkills(clazz, (short) Math.max(15, bot.level / 3), tier, ZUtil.random(1, 15));
                    bot.setupBotAppearance(clazz, tier);
                    bot.setin4();
                    bot.init();
                    bot.ability = new Ability(bot);
                    bot.updateParts();
                    if (bot.ability != null) {
                        bot.hp = (int) Math.max(15000, bot.ability.get_hp_max(true));
                        bot.mp = (int) Math.max(5000, bot.ability.get_mp_max(true));
                    }
                }

                short spawnX = (short) (team == 4 ? ZUtil.random(100, 300) : ZUtil.random(Math.max(300, map.template.maxW - 400), map.template.maxW - 100));
                short spawnY = (short) ZUtil.random(220, Math.min(350, map.template.maxH - 50));

                bot.join(map, spawnX, spawnY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearVillageBots() {
        List<BotChiemDao> toRemove = new ArrayList<>();
        for (BotChiemDao b : POOL) {
            if (b.isVillageBot) {
                try { b.leave(); } catch (Exception ignored) {}
                toRemove.add(b);
            }
        }
        POOL.removeAll(toRemove);
    }

    public static void clearIslandBots() {
        List<BotChiemDao> toRemove = new ArrayList<>();
        for (BotChiemDao b : POOL) {
            if (!b.isVillageBot) {
                try { b.leave(); } catch (Exception ignored) {}
                toRemove.add(b);
            }
        }
        POOL.removeAll(toRemove);
    }

    public static void clearBots() {
        for (BotChiemDao b : POOL) {
            try { b.leave(); } catch (Exception ignored) {}
        }
        POOL.clear();
    }
}
