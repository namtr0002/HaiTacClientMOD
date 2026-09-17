
package map.zones;

import clan.Clan;
import model.Player;
import core.ZUtil;
import map.Vgo;
import map.Zone;
import mob.Mob;
import network.Message;
import network.Service;
import org.joda.time.LocalTime;
import template.GiftBox;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.MobTemplate;
import zabstracts.AbsClanDungeon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Phó bản Chiến Chiếm Đảo Bang Hội (ChiemDao).
 * Kế thừa ClanDungeon / AbsDungeon chuẩn xịn theo kiến trúc server.
 */
public class ChiemDao extends AbsClanDungeon {

    // Global Top Clans chiếm giữ 5 đảo
    public static Clan clanTop1;
    public static Clan clanTop2;
    public static Clan clanTop3;
    public static Clan clanTop4;
    public static Clan clanTop5;

    public static long time1;
    public static long time2;
    public static long time3;
    public static long time4;
    public static long time5;

    public static boolean check1;
    public static boolean check2;
    public static boolean check3;
    public static boolean check4;
    public static boolean check5;

    public static int hpTruChinhOrg = (MobTemplate.get_mob_template(133) != null && MobTemplate.get_mob_template(133).hp_max > 50000) ? MobTemplate.get_mob_template(133).hp_max : 5000000;
    public static int hpTruChinh = hpTruChinhOrg;

    // Instance State của 1 trận Chiếm Đảo
    public int islandId; // Map ID cửa ngõ: 25, 33, 49, 69, 83
    public Clan occupyingClan;
    public Mob mainTurret; // Trụ chính phòng thủ
    public boolean isFinished;
    public boolean isClosed;

    public Map<Integer, Long> playerDamageMap = new HashMap<>();

    public ChiemDao() {
        this.maps = new CopyOnWriteArrayList<>();
        this.mobs = new CopyOnWriteArrayList<>();
    }

    public ChiemDao(int islandId) {
        this();
        this.islandId = islandId;
        this.occupyingClan = getClanTop(islandId);
    }

    /**
     * Factory method khởi tạo phó bản Chiếm Đảo chuẩn xịn.
     */
    public static ChiemDao createDungeon(int islandId) {
        ChiemDao dungeon = new ChiemDao(islandId);
        dungeon.create();
        return dungeon;
    }

    /**
     * Kiểm tra Chiếm Đảo có đang mở không — dựa vào TimedDungeonManager schedule CHIEM_DAO.
     */
    public static boolean isOpen() {
        activities.TimedDungeonManager.ScheduleConfig cfg =
            activities.TimedDungeonManager.gI().getConfigs().get("CHIEM_DAO");
        if (cfg != null && activities.TimedDungeonManager.gI().isWithinTimeRange(cfg)) {
            return true;
        }
        if (core.ZUtil.is_DayofWeek(2) || core.ZUtil.is_DayofWeek(4) || core.ZUtil.is_DayofWeek(6)) {
            int hour = org.joda.time.LocalTime.now().getHourOfDay();
            if (hour == 19) return true;
        }
        return false;
    }

    public static boolean isMapHaveGate(int id) {
        return id == 25 || id == 33 || id == 49 || id == 69 || id == 83;
    }

    public static Clan getClanTop(int id) {
        switch (id) {
            case 25:
            case 261:
            case 254:
                return clanTop1;
            case 33:
            case 262:
            case 255:
                return clanTop2;
            case 49:
            case 263:
            case 256:
                return clanTop3;
            case 69:
            case 264:
            case 257:
                return clanTop4;
            case 83:
            case 265:
            case 258:
                return clanTop5;
        }
        return null;
    }

    public static void setClanTop(int id, Clan clan) {
        switch (id) {
            case 25:
            case 261:
            case 254:
                clanTop1 = clan;
                time1 = System.currentTimeMillis() + 60_000 * 10;
                check1 = false;
                break;
            case 33:
            case 262:
            case 255:
                clanTop2 = clan;
                time2 = System.currentTimeMillis() + 60_000 * 10;
                check2 = false;
                break;
            case 49:
            case 263:
            case 256:
                clanTop3 = clan;
                time3 = System.currentTimeMillis() + 60_000 * 10;
                check3 = false;
                break;
            case 69:
            case 264:
            case 257:
                clanTop4 = clan;
                time4 = System.currentTimeMillis() + 60_000 * 10;
                check4 = false;
                break;
            case 83:
            case 265:
            case 258:
                clanTop5 = clan;
                time5 = System.currentTimeMillis() + 60_000 * 10;
                check5 = false;
                break;
        }
    }

    static {
        loadDb();
    }

    public static void loadDb() {
        try {
            Clan[] tops = historys.ChiemDaoHistory.loadDb();
            if (tops != null) {
                if (tops[0] != null) clanTop1 = tops[0];
                if (tops[1] != null) clanTop2 = tops[1];
                if (tops[2] != null) clanTop3 = tops[2];
                if (tops[3] != null) clanTop4 = tops[3];
                if (tops[4] != null) clanTop5 = tops[4];
            }
        } catch (Exception ignored) {}
    }

    public static void updateDb() {
        historys.ChiemDaoHistory.updateDb(clanTop1, clanTop2, clanTop3, clanTop4, clanTop5);
    }

    /**
     * Đếm số lượng Trụ Phụ (mob_id 132) còn sống trong bản đồ Chiếm Đảo.
     */
    public static int getAliveSubTurretCount(Zone map) {
        if (map == null) return 0;
        int count = 0;
        java.util.HashSet<Integer> countedIndices = new java.util.HashSet<>();
        if (map.mobs != null && !map.mobs.isEmpty()) {
            for (Mob m : map.mobs.values()) {
                if (m != null && countedIndices.add(m.index)) {
                    if (m.mtemplate != null && m.mtemplate.mob_id == 132 && !m.isdie && m.hp > 0) {
                        count++;
                    }
                }
            }
        }
        if (map.list_mob != null) {
            for (int idx : map.list_mob) {
                if (countedIndices.add(idx)) {
                    Mob m = map.getMob(idx);
                    if (m != null && m.mtemplate != null && m.mtemplate.mob_id == 132 && !m.isdie && m.hp > 0) {
                        count++;
                    }
                }
            }
        }
        if (map.map_dungeon != null && map.map_dungeon.mobs != null) {
            for (Mob m : map.map_dungeon.mobs) {
                if (m != null && countedIndices.add(m.index)) {
                    if (m.mtemplate != null && m.mtemplate.mob_id == 132 && !m.isdie && m.hp > 0) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Xử lý toàn bộ logic khi Trụ Chính (mob 133) bị tiêu diệt:
     * 1. Gán chủ quyền Đảo cho Bang hội kết liễu Trụ Chính.
     * 2. Thông báo KTG toàn server.
     * 3. Gửi Message 63 cập nhật cờ/banner Bang hội cho map.
     * 4. Cập nhật PK tức thì: Bang thắng -> Thủ (5), các Bang khác -> Công (4).
     * 5. Hồi sinh đồng bộ 100% Full HP cho tất cả Trụ Phụ (132) và Trụ Chính (133).
     */
    public static void onMainTurretDestroyed(Zone map, Player killer, Mob mainTurret) {
        if (map == null) return;
        Player p = (killer != null) ? killer.getOwnerPlayer() : null;
        Clan winningClan = (p != null) ? p.clan : null;

        if (winningClan != null) {
            // 1. Gán chủ quyền đảo cho bang hội tiêu diệt Trụ Chính
            setClanTop(map.template.id, winningClan);
            updateDb();

            String islandName = activities.ChiemDaoMenu.getIslandName(map.template.id);
            core.Manager.gI().chatKTG(0, "Bang hội [" + winningClan.name + "] đã xuất sắc chiếm lĩnh thành công " + islandName + "!", 5);

            // 2. Gửi cờ chiếm đảo (Message 63) cập nhật banner toàn map
            try {
                Message mm = new Message(63);
                mm.writer().writeShort(winningClan.id);
                mm.writer().writeShort(winningClan.icon);
                mm.writer().writeUTF(winningClan.name);
                mm.writer().writeUTF(winningClan.name);
                mm.writer().writeShort(winningClan.level);
                mm.writer().writeByte(4);
                mm.writer().writeByte(4);
                mm.writer().writeInt(1);
                map.send_msg_all_p(mm, null, true);
                mm.cleanup();
            } catch (Exception ignored) {}

            // 3. Cập nhật PK mode toàn bộ người chơi trong map
            if (map.players != null) {
                for (int j = 0; j < map.players.size(); j++) {
                    Player p0 = map.players.get(j);
                    if (p0 != null) {
                        try {
                            boolean isDef = (p0.clan != null && (p0.clan.id == winningClan.id || (p0.clan.name != null && p0.clan.name.equalsIgnoreCase(winningClan.name))));
                            p0.type_pk = (byte) (isDef ? 5 : 4);
                            if (p0 instanceof bot.BotChiemDao) {
                                ((bot.BotChiemDao) p0).team = p0.type_pk;
                            }
                            p0.getService().update_PK(p0, true);
                            map.change_flag(p0, p0.type_pk);
                        } catch (Exception ignored) {}
                    }
                }
            }
        }

        // 4. HỒI SINH TOÀN BỘ TRỤ: Tất cả Trụ Phụ (132) và Trụ Chính (133) hồi sinh 100% máu sau khi đổi chủ
        respawnAllTurrets(map, mainTurret);
    }

    /**
     * Hồi sinh toàn bộ Trụ Phụ (mob 132) và Trụ Chính (mob 133) sau khi đảo bị chiếm xong.
     */
    public static void respawnAllTurrets(Zone map, Mob mainTurret) {
        if (map == null) return;
        try {
            List<Mob> allMobs = new ArrayList<>();
            java.util.HashSet<Integer> addedIndices = new java.util.HashSet<>();
            if (map.mobs != null) {
                for (Mob m : map.mobs.values()) {
                    if (m != null && addedIndices.add(m.index)) {
                        allMobs.add(m);
                    }
                }
            }
            if (map.list_mob != null) {
                for (int i2 = 0; i2 < map.list_mob.length; i2++) {
                    Mob m = map.getMob(map.list_mob[i2]);
                    if (m != null && addedIndices.add(m.index)) {
                        allMobs.add(m);
                    }
                }
            }
            if (map.map_dungeon != null && map.map_dungeon.mobs != null) {
                for (Mob m : map.map_dungeon.mobs) {
                    if (m != null && addedIndices.add(m.index)) {
                        allMobs.add(m);
                    }
                }
            }

            // 1. Hồi sinh toàn bộ Trụ Phụ (mob 132)
            for (Mob mob : allMobs) {
                if (mob != null && mob.mtemplate != null && mob.mtemplate.mob_id == 132) {
                    mob.isdie = false;
                    if (mob.hp_max <= 0 || mob.hp_max < 500) {
                        mob.hp_max = 500;
                    }
                    mob.hp = mob.hp_max;
                    mob.id_target = -1;
                    mob.time_refresh = 0;
                    if (map.mobs != null) {
                        map.mobs.put(mob.index, mob);
                    }
                    if (map.map_dungeon != null && map.map_dungeon.mobs != null && !map.map_dungeon.mobs.contains(mob)) {
                        map.map_dungeon.mobs.add(mob);
                    }

                    try {
                        Message m_local = new Message(1);
                        m_local.writer().writeByte(1);
                        m_local.writer().writeShort(mob.index);
                        m_local.writer().writeShort(mob.x);
                        m_local.writer().writeShort(mob.y);
                        map.send_msg_all_p(m_local, null, true);
                        m_local.cleanup();

                        m_local = new Message(4);
                        m_local.writer().writeShort(mob.index);
                        m_local.writer().writeShort(mob.mtemplate.mob_id);
                        m_local.writer().writeShort(mob.x);
                        m_local.writer().writeShort(mob.y);
                        m_local.writer().writeShort(mob.level);
                        m_local.writer().writeInt(mob.hp);
                        m_local.writer().writeInt(mob.hp_max);
                        m_local.writer().writeShort(mob.mtemplate.skill != null && mob.mtemplate.skill.length > 0 ? mob.mtemplate.skill[0] : 0);
                        m_local.writer().writeShort(0); // time_respawn = 0 (Không tự hồi sinh trên client)
                        m_local.writer().writeByte(mob.mtemplate.typemonster);
                        m_local.writer().writeByte(mob.boss_inf != null ? mob.boss_inf.levelBoss : 0);
                        map.send_msg_all_p(m_local, null, true);
                        m_local.cleanup();
                    } catch (Exception ignored) {}
                }
            }

            // 2. Hồi sinh Trụ Chính (mob 133)
            Mob turretToRespawn = mainTurret;
            if (turretToRespawn == null) {
                for (Mob m : allMobs) {
                    if (m != null && m.mtemplate != null && m.mtemplate.mob_id == 133) {
                        turretToRespawn = m;
                        break;
                    }
                }
            }
            if (turretToRespawn != null) {
                turretToRespawn.isdie = false;
                if (isOpen()) {
                    long newHp = ((long) hpTruChinh * 12L) / 10L;
                    if (newHp > 2_000_000_000) newHp = 2_000_000_000;
                    hpTruChinh = (int) newHp;
                    turretToRespawn.hp_max = (int) newHp;
                } else {
                    turretToRespawn.hp_max = Math.max(hpTruChinhOrg, 5000000);
                }
                turretToRespawn.hp = turretToRespawn.hp_max;
                turretToRespawn.id_target = -1;
                turretToRespawn.time_refresh = 0;
                if (map.mobs != null) {
                    map.mobs.put(turretToRespawn.index, turretToRespawn);
                }
                if (map.map_dungeon != null && map.map_dungeon.mobs != null && !map.map_dungeon.mobs.contains(turretToRespawn)) {
                    map.map_dungeon.mobs.add(turretToRespawn);
                }

                try {
                    Message m_local = new Message(1);
                    m_local.writer().writeByte(1);
                    m_local.writer().writeShort(turretToRespawn.index);
                    m_local.writer().writeShort(turretToRespawn.x);
                    m_local.writer().writeShort(turretToRespawn.y);
                    map.send_msg_all_p(m_local, null, true);
                    m_local.cleanup();

                    m_local = new Message(4);
                    m_local.writer().writeShort(turretToRespawn.index);
                    m_local.writer().writeShort(turretToRespawn.mtemplate.mob_id);
                    m_local.writer().writeShort(turretToRespawn.x);
                    m_local.writer().writeShort(turretToRespawn.y);
                    m_local.writer().writeShort(turretToRespawn.level);
                    m_local.writer().writeInt(turretToRespawn.hp);
                    m_local.writer().writeInt(turretToRespawn.hp_max);
                    m_local.writer().writeShort(turretToRespawn.mtemplate.skill != null && turretToRespawn.mtemplate.skill.length > 0 ? turretToRespawn.mtemplate.skill[0] : 0);
                    m_local.writer().writeShort(0); // time_respawn = 0 (Không tự hồi sinh trên client)
                    m_local.writer().writeByte(turretToRespawn.mtemplate.typemonster);
                    m_local.writer().writeByte(turretToRespawn.boss_inf != null ? turretToRespawn.boss_inf.levelBoss : 0);
                    map.send_msg_all_p(m_local, null, true);
                    m_local.cleanup();
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    //  ABS DUNGEON IMPLEMENTATION
    // ======================================================

    @Override
    public void create() {
        this.startTime = System.currentTimeMillis();
        this.time = System.currentTimeMillis() + 60_000 * 15; // 15 phút tham gia
        this.isFinished = false;
        this.isClosed = false;

        // Chọn map ID phó bản tương ứng với cửa ngõ islandId (Ví dụ: 254/255/256/257/258)
        int mapDungeonId = getMapDungeonId(this.islandId);
        Zone mapTemplate = Zone.getMapByID(mapDungeonId)[0];

        Zone mapDungeon = new Zone();
        mapDungeon.template = mapTemplate.template;
        mapDungeon.zone_id = (byte) 0;
        mapDungeon.list_mob = new int[0];
        mapDungeon.map_dungeon = this;

        int index_mob = -2;
        for (int i = 0; i < mapTemplate.list_mob.length; i++) {
            Mob temp = mapTemplate.getMob(mapTemplate.list_mob[i]);
            Mob mob_add = new Mob();
            mob_add.mtemplate = temp.mtemplate;
            mob_add.x = temp.x;
            mob_add.y = temp.y;
            mob_add.hp_max = temp.hp_max > 0 ? (int) temp.hp_max : hpTruChinhOrg;
            mob_add.hp = mob_add.hp_max;
            mob_add.level = 100;
            mob_add.isdie = false;
            mob_add.id_target = -1;
            mob_add.index = index_mob--;
            mob_add.map = mapDungeon;

            if (mob_add.mtemplate.mob_id == 133) {
                this.mainTurret = mob_add;
            }
            this.mobs.add(mob_add);
            mapDungeon.mobs.put(mob_add.index, mob_add);
        }

        mapDungeon.start_map();
        Zone.add_map_plus(mapDungeon);
        this.maps.add(mapDungeon);

        // Tự động phân phối bot thủ đảo và đa dạng các phe bang hội công đảo
        clan.Clan occClan = this.occupyingClan;
        if (occClan == null) {
            occClan = new clan.Clan();
            occClan.id = (short) -800;
            occClan.name = "Băng Trấn Thủ Đảo";
            occClan.icon = (short) core.ZUtil.random(1, 10);
        }
        bot.BotChiemDao.autoFill(mapDungeon, 5, 6, (short) 80, occClan);

        String[] challengerClanNames = {"Băng Mũ Rơm", "Băng Tóc Đỏ", "Băng Bách Thú", "Băng Big Mom", "Băng Râu Đen"};
        for (int ac = 0; ac < 4; ac++) {
            clan.Clan atkClan = new clan.Clan();
            atkClan.id = (short) (-850 - ac);
            atkClan.name = (ac < challengerClanNames.length) ? challengerClanNames[ac] : bot.botplayer.BotNameGenerator.generateRandomClanName();
            atkClan.icon = (short) core.ZUtil.random(1, 10);
            bot.BotChiemDao.autoFill(mapDungeon, 4, 3, (short) 80, atkClan);
        }
    }

    private int getMapDungeonId(int islandId) {
        switch (islandId) {
            case 25: return 254;
            case 33: return 255;
            case 49: return 256;
            case 69: return 257;
            case 83: return 258;
            default: return 254;
        }
    }

    @Override
    public String canJoin(Player p) {
        if (!isOpen()) {
            return "Hoạt động Chiếm Đảo chưa đến giờ mở cửa (19h T2, T4, T6)!";
        }
        if (p.clan == null) {
            return "Bạn cần gia nhập Bang Hội để tham gia Chiếm Đảo!";
        }
        if (isClosed || isTimedOut()) {
            return "Trận Chiến Chiếm Đảo đã kết thúc!";
        }
        return null;
    }

    @Override
    public JoinMode getJoinMode() {
        return JoinMode.YES_NO;
    }

    @Override
    public String getJoinTitle() {
        return "Chiếm Đảo Bang Hội";
    }

    @Override
    public String getJoinDesc() {
        return "Bạn có muốn tham gia trận Chiến Chiếm Đảo cùng Bang Hội không?";
    }

    @Override
    public int getDungeonId() {
        return -69; // Unique Dungeon ID
    }

    @Override
    public void join(Player p) throws IOException {
        if (maps != null && !maps.isEmpty()) {
            p.save_previous_map();
            Zone targetZone = maps.get(0);
            Vgo vgo = new Vgo();
            vgo.map_go = new Zone[]{targetZone};
            vgo.xnew = getJoinX();
            vgo.ynew = getJoinY();

            p.goto_map(vgo);
            p.dungeon = this;

            // Đặt PK Bang Hội cho các thành viên tham chiến
            boolean isDefender = (this.occupyingClan != null && p.clan != null && (p.clan.id == this.occupyingClan.id || (p.clan.name != null && p.clan.name.equalsIgnoreCase(this.occupyingClan.name))));
            p.type_pk = (byte) (isDefender ? 5 : 4);
            p.getService().update_PK(p, true);
            p.getService().pet(p, true);
            p.getService().send_time_cool_down(this.time, "Chiếm Đảo", 2);
        }
    }

    @Override
    public void onJoinSuccess(Player p) throws IOException {
        p.getService().send_box_ThongBao_OK("Bạn đã tiến vào đảo! Hãy cùng Bang Hội tiêu diệt Trụ Chính!");
    }

    @Override
    public void update(Zone zone) throws IOException {
        if (isClosed) return;

        // 1. Kiểm tra hết thời gian
        if (isTimedOut()) {
            onTimeout();
            return;
        }

        // 2. Kiểm tra điều kiện hoàn thành (Trụ chính bị hạ)
        if (!isFinished && mainTurret != null && mainTurret.isdie) {
            isFinished = true;
            onTurretDestroyed();
        }
    }

    /**
     * Xử lý khi Trụ Chính bị tiêu diệt và cập nhật Bang Hội chiến thắng.
     */
    private void onTurretDestroyed() throws IOException {
        int highestDmgPlayerId = -1;
        long maxDmg = -1;

        for (Map.Entry<Integer, Long> entry : playerDamageMap.entrySet()) {
            if (entry.getValue() > maxDmg) {
                maxDmg = entry.getValue();
                highestDmgPlayerId = entry.getKey();
            }
        }

        Player winnerPlayer = null;
        if (!maps.isEmpty()) {
            for (Player p : maps.get(0).players) {
                if (p.IDPlayer == highestDmgPlayerId) {
                    winnerPlayer = p;
                    break;
                }
            }
        }

        if (winnerPlayer != null && winnerPlayer.clan != null) {
            setClanTop(this.islandId, winnerPlayer.clan);
            updateDb();

            broadcastMessage("Trụ chính đã sụp đổ! Bang hội [" + winnerPlayer.clan.name + "] đã xuất sắc chiếm được đảo!");
            rewardClan(winnerPlayer.clan);
        } else {
            broadcastMessage("Trận chiến Chiếm Đảo đã kết thúc!");
        }

        this.time = System.currentTimeMillis() + 10_000; // Đưa về 10s trước khi đóng
    }

    private void rewardClan(Clan clan) throws IOException {
        if (maps.isEmpty() || clan == null) return;
        List<GiftBox> rewards = new ArrayList<>();

        GiftBox beri = new GiftBox();
        ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(0);
        if (it4 != null) {
            beri.id = it4.id; beri.type = 4; beri.name = it4.name; beri.icon = it4.icon;
            beri.num = 500_000; beri.color = 0;
            rewards.add(beri);
        }

        GiftBox nguyenLieu = new GiftBox();
        ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(13);
        if (it7 != null) {
            nguyenLieu.id = it7.id; nguyenLieu.type = 7; nguyenLieu.name = it7.name; nguyenLieu.icon = it7.icon;
            nguyenLieu.num = 20; nguyenLieu.color = 0;
            rewards.add(nguyenLieu);
        }

        for (Player p : maps.get(0).players) {
            if (p != null && !p.isBot && p.conn != null && p.clan != null && p.clan.equals(clan)) {
                core.MailService.sendChiemDaoMail(p, clan.name, rewards);
            }
        }
    }

    @Override
    public void onTimeout() throws IOException {
        if (isClosed) return;
        isClosed = true;

        broadcastMessage("Thời gian Chiếm Đảo đã kết thúc!");

        for (Zone m : new ArrayList<>(maps)) {
            List<Player> players = new ArrayList<>(m.players);
            for (Player p : players) {
                try {
                    leave(p);
                    p.return_to_previous_map();
                } catch (Exception ignored) {}
            }
            m.setRunning(false);
            m.map_dungeon = null;
        }
    }

    @Override
    public void leave(Player p) throws IOException {
        super.leave(p);
        p.type_pk = -1;
        p.getService().update_PK(p, true);
    }

    private void broadcastMessage(String msgText) throws IOException {
        if (maps.isEmpty()) return;
        Message m = new Message(-31);
        m.writer().writeByte(0);
        m.writer().writeUTF(msgText);
        m.writer().writeByte(5);
        m.writer().writeShort(-1);

        for (Player p : maps.get(0).players) {
            if (p.conn != null) {
                p.conn.addmsg(m);
            }
        }
        m.cleanup();
    }
}

