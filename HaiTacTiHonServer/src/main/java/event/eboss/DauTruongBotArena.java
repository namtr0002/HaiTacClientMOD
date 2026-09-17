package event.eboss;

import model.Player;
import ability.Ability;
import bot.AttackAround;
import bot.BotPVP;
import bot.MoveToTarget;
import core.Manager;
import core.ZUtil;
import map.Zone;
import network.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DauTruongBotArena — Đấu Trường Dự Đoán Trận Đấu Bot PvP Sự Kiện Đấu Trường Rực Lửa 2026.
 *
 * <p>Quy tắc:
 * <ul>
 *   <li>Map ngẫu nhiên từ danh sách Map PvP Băng (120, 122, 123).</li>
 *   <li>Thể thức ngẫu nhiên: Solo 1v1, Đồng Đội 3v3 hoặc Đại Chiến 5v5 giữa 2 đội Bot (Đội Đỏ vs Đội Xanh).</li>
 *   <li>Luật 5 Điểm: Đội nào hạ gục toàn bộ thành viên đối phương trước sẽ ghi 1 điểm (hoặc bên thua mất 1 điểm). Chạm 5 điểm là thắng chung cuộc.</li>
 *   <li>Mỗi hiệp kết thúc: Reset vị trí xuất phát 2 bên, hồi phục đầy đủ HP/MP, xóa sạch cooldown skill và tiếp tục hiệp đấu.</li>
 *   <li>Khóa dự đoán: Người chơi phải tham gia dự đoán trước 1 phút trước khi trận đấu bắt đầu.</li>
 *   <li>Khán giả (Spectator): Cho phép người chơi vào xem trực tiếp trong map. Khi kết thúc trận, toàn bộ người xem tự động quay về map lưu ban đầu.</li>
 *   <li>Trao thưởng tự động: Nhân đôi vé cược, tặng Quả Bóng Vàng, Rương World Cup và cộng điểm BXH Top Dự Đoán.</li>
 * </ul>
 */
public class DauTruongBotArena {

    public static final byte STATE_WAITING_BET = 0;           // Đang nhận cược (trước 1 phút)
    public static final byte STATE_LOCKED_BET_PREPARING = 1;  // Khóa cược (còn <= 60s), chuẩn bị sân & spawn bot
    public static final byte STATE_ROUND_COUNTDOWN = 2;       // Đếm ngược 3s chuẩn bị vào hiệp
    public static final byte STATE_FIGHTING = 3;              // Đang thi đấu hiệp
    public static final byte STATE_ROUND_RESET = 4;           // Hiệp kết thúc, reset vị trí, xóa cooldown, chuẩn bị hiệp mới
    public static final byte STATE_MATCH_FINISH = 5;          // Trận kết thúc (đủ 5 điểm), trao thưởng, đưa spectator về map lưu

    public static final int TARGET_SCORE = 5;                 // 5 Điểm chạm
    public static final short[] ICE_MAP_TEMPLATES = {120, 122, 123};

    private static DauTruongBotArena instance;

    public static DauTruongBotArena gI() {
        if (instance == null) {
            instance = new DauTruongBotArena();
        }
        return instance;
    }

    public static class BetEntry {
        public int playerId;
        public String playerName;
        public int teamIndex; // 1 = Đội Đỏ, 2 = Đội Xanh
        public int betTickets; // Số Vé World Cup (#599) cược
        public long betTime;

        public BetEntry(int playerId, String playerName, int teamIndex, int betTickets) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.teamIndex = teamIndex;
            this.betTickets = betTickets;
            this.betTime = System.currentTimeMillis();
        }
    }

    public int matchId = 1;
    public byte state = STATE_WAITING_BET;
    public int timeState = 120; // Giây đếm ngược của state hiện tại

    public int formatType = 1; // 1 = 1v1, 3 = 3v3, 5 = 5v5
    public String team1Name = "Đội Đỏ (Hải Tặc)";
    public String team2Name = "Đội Xanh (Hải Quân)";

    public int scoreTeam1 = 0;
    public int scoreTeam2 = 0;
    public int currentRound = 1;

    public Zone arenaZone = null;
    public final List<BotPVP> team1Bots = new ArrayList<>();
    public final List<BotPVP> team2Bots = new ArrayList<>();

    public final Map<Integer, BetEntry> mapBets = new HashMap<>();
    public String lastMatchResultInfo = "Chưa có trận đấu trước đó.";

    public DauTruongBotArena() {
        initNewMatchCycle();
    }

    /**
     * Khởi tạo chu kỳ trận đấu mới
     */
    public synchronized void initNewMatchCycle() {
        state = STATE_WAITING_BET;
        timeState = 120; // 2 phút nhận cược (trong đó phút đầu cho cược, phút cuối khóa cược)
        scoreTeam1 = 0;
        scoreTeam2 = 0;
        currentRound = 1;
        mapBets.clear();

        // Chọn thể thức ngẫu nhiên
        int[] formats = {1, 3, 5};
        formatType = formats[ZUtil.random(formats.length)];

        // Tên đội bóng / hải tặc ngầu
        String[][] teamPairs = {
            {"Đội Đỏ (Mũ Rơm)", "Đội Xanh (Hải Quân G5)"},
            {"Đội Đỏ (Đức)", "Đội Xanh (Pháp)"},
            {"Đội Đỏ (Brazil)", "Đội Xanh (Tây Ban Nha)"},
            {"Đội Đỏ (Hỏa Quyền)", "Đội Xanh (Băng Băng)"},
            {"Đội Đỏ (Tứ Hoàng)", "Đội Xanh (Thất Vũ Hải)"},
            {"Đội Đỏ (Quỷ Đỏ)", "Đội Xanh (Chiến Hạm Băng)"}
        };
        int pairIdx = ZUtil.random(teamPairs.length);
        team1Name = teamPairs[pairIdx][0];
        team2Name = teamPairs[pairIdx][1];

        // Dọn dẹp zone cũ nếu còn sót
        destroyArenaMap();
    }

    /**
     * Cập nhật vòng lặp mỗi giây (được gọi từ Event.update)
     */
    public synchronized void update() {
        if (!event.EventManager.isActive(13)) {
            if (arenaZone != null) {
                destroyArenaMap();
            }
            return;
        }

        if (timeState > 0) {
            timeState--;
        }

        switch (state) {
            case STATE_WAITING_BET:
                // Nếu thời gian đếm ngược còn <= 60s -> Khóa cược (trước 1 phút) và bắt đầu chuẩn bị sân đấu
                if (timeState <= 60) {
                    state = STATE_LOCKED_BET_PREPARING;
                    createArenaMapAndBots();
                    Manager.gI().chatKTG(0, "[Đấu Trường Rực Lửa] Đã khóa nhận dự đoán Trận #" + matchId + " (" 
                            + team1Name + " VS " + team2Name + " - Thể thức " + formatType + "vs" + formatType + ")! Trận đấu sẽ bắt đầu sau " + timeState + " giây!", 0);
                }
                break;

            case STATE_LOCKED_BET_PREPARING:
                if (timeState <= 0) {
                    // Hết 60s chuẩn bị -> Bắt đầu Hiệp 1
                    startRound();
                }
                break;

            case STATE_ROUND_COUNTDOWN:
                if (timeState <= 0) {
                    // Đếm ngược 3s xong -> Lao vào đánh nhau
                    state = STATE_FIGHTING;
                    timeState = 90; // Thời gian tối đa cho 1 hiệp (90s)
                    enableBotCombat();
                    sendArenaNotification("HIỆP " + currentRound + " BẮT ĐẦU! TỈ SỐ: " + scoreTeam1 + " - " + scoreTeam2);
                }
                break;

            case STATE_FIGHTING:
                checkFightingRound();
                break;

            case STATE_ROUND_RESET:
                if (timeState <= 0) {
                    // Kiểm tra xem đã có đội nào đạt 5 điểm chưa
                    if (scoreTeam1 >= TARGET_SCORE || scoreTeam2 >= TARGET_SCORE) {
                        finishMatch();
                    } else {
                        // Tiếp tục hiệp đấu tiếp theo
                        currentRound++;
                        resetBotsAndArena();
                        state = STATE_ROUND_COUNTDOWN;
                        timeState = 3;
                        sendArenaNotification("Chuẩn bị Hiệp " + currentRound + "! Tỉ số hiện tại: " + team1Name + " [" + scoreTeam1 + "] - [" + scoreTeam2 + "] " + team2Name);
                    }
                }
                break;

            case STATE_MATCH_FINISH:
                if (timeState <= 0) {
                    // Kết thúc hoàn tất, giải phóng map và bắt đầu trận mới
                    matchId++;
                    initNewMatchCycle();
                }
                break;
        }
    }

    /**
     * Tạo map đấu trường và spawn bot 2 phe
     */
    private void createArenaMapAndBots() {
        try {
            int targetMapId = ICE_MAP_TEMPLATES[ZUtil.random(ICE_MAP_TEMPLATES.length)];
            Zone[] tZones = Zone.getMapByID(targetMapId);
            if (tZones == null || tZones.length == 0 || tZones[0] == null) {
                targetMapId = 120;
                tZones = Zone.getMapByID(120);
            }

            arenaZone = new Zone();
            arenaZone.template = tZones[0].template;
            arenaZone.zone_id = 0;
            arenaZone.list_mob = new int[0];
            arenaZone.start_map();
            Zone.add_map_plus(arenaZone);

            team1Bots.clear();
            team2Bots.clear();

            short mapW = arenaZone.template.maxW > 0 ? arenaZone.template.maxW : 1200;
            short leftSpawnX = 220;
            short rightSpawnX = (short) Math.max(550, mapW - 260);

            // 1. Tạo Bot Đội Đỏ (Team 1)
            for (int i = 0; i < formatType; i++) {
                int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                String name = "Đỏ_" + bot.botplayer.BotNameGenerator.getRandomBotName();
                BotPVP bot = new BotPVP(botId, name);
                bot.isBot = true;
                bot.clazz = (byte) ((i % 5) + 1);
                bot.level = (short) ZUtil.random(85, 95);
                bot.autoAllocatePotentialPoints(bot.clazz);
                int botTier = 3;
                bot.ability = new Ability(bot);
                bot.setupBotEquip(bot.clazz, bot.level, botTier, true, true, true);
                bot.setupBotSkills(bot.clazz, (short) 30, botTier, 0);
                bot.setupBotAppearance(bot.clazz, botTier);
                bot.setin4();
                bot.init();
                bot.updateParts();

                bot.type_pk = 14; // Cờ Đỏ
                bot.join(arenaZone, (short) (leftSpawnX + i * 35), (short) (240 + ZUtil.random(-20, 20)));
                team1Bots.add(bot);
            }

            // 2. Tạo Bot Đội Xanh (Team 2)
            for (int i = 0; i < formatType; i++) {
                int botId = database.IDManager.takeID(database.IDManager.FAKE_BOT);
                String name = "Xanh_" + bot.botplayer.BotNameGenerator.getRandomBotName();
                BotPVP bot = new BotPVP(botId, name);
                bot.isBot = true;
                bot.clazz = (byte) ((i % 5) + 1);
                bot.level = (short) ZUtil.random(85, 95);
                bot.autoAllocatePotentialPoints(bot.clazz);
                int botTier = 3;
                bot.ability = new Ability(bot);
                bot.setupBotEquip(bot.clazz, bot.level, botTier, true, true, true);
                bot.setupBotSkills(bot.clazz, (short) 30, botTier, 0);
                bot.setupBotAppearance(bot.clazz, botTier);
                bot.setin4();
                bot.init();
                bot.updateParts();

                bot.type_pk = 15; // Cờ Xanh
                bot.join(arenaZone, (short) (rightSpawnX - i * 35), (short) (240 + ZUtil.random(-20, 20)));
                team2Bots.add(bot);
            }

            // Gán cờ PK trực quan
            for (BotPVP b : team1Bots) {
                arenaZone.change_flag(b, (short) 14);
            }
            for (BotPVP b : team2Bots) {
                arenaZone.change_flag(b, (short) 15);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Bắt đầu một hiệp đấu
     */
    private void startRound() {
        state = STATE_ROUND_COUNTDOWN;
        timeState = 3; // 3 giây đếm ngược
        resetBotsAndArena();
        sendArenaNotification("CHUẨN BỊ HIỆP " + currentRound + "! Trận đấu 5 Điểm Chạm bắt đầu sau 3 giây!");
    }

    /**
     * Bật AI tấn công cho bot sau đếm ngược
     */
    private void enableBotCombat() {
        for (BotPVP b : team1Bots) {
            if (b != null && !b.isdie) {
                b.type_pk = 14;
                b.setAttack(new AttackAround());
                b.setMove(new MoveToTarget(null, 2000));
            }
        }
        for (BotPVP b : team2Bots) {
            if (b != null && !b.isdie) {
                b.type_pk = 15;
                b.setAttack(new AttackAround());
                b.setMove(new MoveToTarget(null, 2000));
            }
        }
    }

    /**
     * Kiểm tra trạng thái hiệp đấu đang diễn ra
     */
    private void checkFightingRound() {
        if (arenaZone == null) return;

        int aliveTeam1 = 0;
        int aliveTeam2 = 0;

        for (BotPVP b : team1Bots) {
            if (b != null && !b.isdie && b.hp > 0) aliveTeam1++;
        }
        for (BotPVP b : team2Bots) {
            if (b != null && !b.isdie && b.hp > 0) aliveTeam2++;
        }

        boolean roundOver = false;
        String roundWinnerName = "";

        if (aliveTeam1 == 0 && aliveTeam2 == 0) {
            // Cả 2 cùng gục -> Mỗi bên cộng 1 điểm
            scoreTeam1++;
            scoreTeam2++;
            roundWinnerName = "Hòa Hiệp";
            roundOver = true;
        } else if (aliveTeam1 == 0) {
            // Toàn bộ Đội 1 chết -> Đội 2 ghi 1 điểm (Đội 1 mất 1 điểm)
            scoreTeam2++;
            roundWinnerName = team2Name;
            roundOver = true;
        } else if (aliveTeam2 == 0) {
            // Toàn bộ Đội 2 chết -> Đội 1 ghi 1 điểm (Đội 2 mất 1 điểm)
            scoreTeam1++;
            roundWinnerName = team1Name;
            roundOver = true;
        } else if (timeState <= 0) {
            // Hết 90s hiệp đấu: Đội nào còn nhiều máu hơn thắng hiệp
            long hpSum1 = 0, hpSum2 = 0;
            for (BotPVP b : team1Bots) if (b != null && !b.isdie) hpSum1 += b.hp;
            for (BotPVP b : team2Bots) if (b != null && !b.isdie) hpSum2 += b.hp;

            if (hpSum1 > hpSum2) {
                scoreTeam1++;
                roundWinnerName = team1Name;
            } else if (hpSum2 > hpSum1) {
                scoreTeam2++;
                roundWinnerName = team2Name;
            } else {
                scoreTeam1++;
                scoreTeam2++;
                roundWinnerName = "Hòa Hiệp";
            }
            roundOver = true;
        }

        if (roundOver) {
            state = STATE_ROUND_RESET;
            timeState = 4; // Nghỉ 4 giây giữa 2 hiệp
            String announce = "KẾT THÚC HIỆP " + currentRound + "! (" + roundWinnerName + " ghi điểm) -> TỈ SỐ: " 
                    + team1Name + " [" + scoreTeam1 + "] - [" + scoreTeam2 + "] " + team2Name;
            sendArenaNotification(announce);
        }
    }

    /**
     * Reset vị trí, hồi phục 100% HP/MP và xóa sạch cooldown skill cho tất cả Bot
     */
    private void resetBotsAndArena() {
        if (arenaZone == null) return;

        short mapW = arenaZone.template.maxW > 0 ? arenaZone.template.maxW : 1200;
        short leftSpawnX = 220;
        short rightSpawnX = (short) Math.max(550, mapW - 260);

        List<BotPVP> allBots = new ArrayList<>();
        allBots.addAll(team1Bots);
        allBots.addAll(team2Bots);

        for (BotPVP b : allBots) {
            if (b == null) continue;
            try {
                b.isdie = false;
                int maxHp = b.ability != null ? b.ability.get_hp_max(true) : 10000;
                int maxMp = b.ability != null ? b.ability.get_mp_max(true) : 1000;
                b.hp = maxHp;
                b.mp = maxMp;

                // XÓA SẠCH COOLDOWN SKILL VÀ HIỆU ỨNG BẤT LỢI 2 BÊN
                b.time_use_skill.clear();
                b.list_eff.clear();

                // Đặt lại tọa độ xuất phát 2 đầu sân
                if (team1Bots.contains(b)) {
                    int idx = team1Bots.indexOf(b);
                    b.x = (short) (leftSpawnX + idx * 35);
                    b.y = (short) (240 + idx * 10);
                    b.type_pk = 14;
                } else {
                    int idx = team2Bots.indexOf(b);
                    b.x = (short) (rightSpawnX - idx * 35);
                    b.y = (short) (240 + idx * 10);
                    b.type_pk = 15;
                }
                b.xold = b.x;
                b.yold = b.y;

                // Đồng bộ hồi sinh và máu tới spectators
                Message mHp = new Message(-83);
                mHp.writer().writeShort(b.index_map);
                mHp.writer().writeByte(0);
                mHp.writer().writeInt(maxHp);
                mHp.writer().writeInt(b.hp);
                mHp.writer().writeInt(maxHp);
                mHp.writer().writeInt(maxMp);
                mHp.writer().writeInt(b.mp);
                mHp.writer().writeInt(0);
                arenaZone.send_msg_all_p(mHp, null, true);
                mHp.cleanup();

                Message mRevive = new Message(-71);
                mRevive.writer().writeByte(1);
                mRevive.writer().writeShort(b.index_map);
                mRevive.writer().writeByte(0);
                mRevive.writer().writeInt(1);
                arenaZone.send_msg_all_p(mRevive, null, true);
                mRevive.cleanup();

                arenaZone.getService().move((byte) 0, b.index_map, b.x, b.y);
            } catch (Exception ignored) {}
        }
    }

    /**
     * Kết thúc toàn bộ trận đấu khi có đội đạt 5 điểm
     */
    private void finishMatch() {
        state = STATE_MATCH_FINISH;
        timeState = 10; // Đợi 10 giây để người xem thấy kết quả rồi đưa về map lưu

        int winnerTeam = (scoreTeam1 >= TARGET_SCORE) ? 1 : 2;
        String winnerName = (winnerTeam == 1) ? team1Name : team2Name;
        lastMatchResultInfo = "Trận #" + matchId + ": " + winnerName + " CHIẾN THẮNG với tỉ số " + scoreTeam1 + " - " + scoreTeam2;

        Manager.gI().chatKTG(0, "[Đấu Trường Rực Lửa] Trận #" + matchId + " đã kết thúc! " + winnerName 
                + " đã xuất sắc giành chiến thắng chung cuộc (Tỉ số: " + scoreTeam1 + " - " + scoreTeam2 
                + ")! Xin chúc mừng các thuyền trưởng dự đoán chính xác!", 0);

        sendArenaNotification("TRẬN ĐẤU KẾT THÚC! " + winnerName + " CHIẾN THẮNG CHUNG CUỘC! Khán giả sẽ được đưa về map lưu...");

        // 1. Trao thưởng cho người chơi đã đặt cược
        for (BetEntry bet : mapBets.values()) {
            if (bet == null) continue;
            try {
                Player p = Zone.get_player_by_id_allmap(bet.playerId);
                if (bet.teamIndex == winnerTeam) {
                    // Thắng cược!
                    int rewardTickets = bet.betTickets * 2;
                    int bonusGoldBalls = Math.max(1, bet.betTickets / 5);
                    int eventPoints = bet.betTickets * 2;

                    if (p != null && p.conn != null) {
                        p.update_pointEvent2(eventPoints); // Top Dự Đoán
                        List<template.GiftBox> gifts = new ArrayList<>();
                        gifts.add(new template.GiftBox(4, 599, rewardTickets));
                        gifts.add(new template.GiftBox(4, 799, bonusGoldBalls));
                        gifts.add(new template.GiftBox(4, 609, 1));
                        core.RewardService.sendGiftOrMail(p, 1, "Thưởng Dự Đoán", "Dự đoán đúng chiến thắng " + winnerName, gifts, true);
                    } else {
                        // Gửi thư nếu offline
                        List<template.GiftBox> gifts = new ArrayList<>();
                        gifts.add(new template.GiftBox(4, 599, rewardTickets));
                        gifts.add(new template.GiftBox(4, 799, bonusGoldBalls));
                        gifts.add(new template.GiftBox(4, 609, 1));
                        int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(bet.playerName);
                        if (ids[0] > 0) {
                            core.MailService.sendMailOffline(ids[0], ids[1], bet.playerName, "Đấu Trường Rực Lửa", "Thưởng Dự Đoán Trận Đấu",
                                    "Chúc mừng bạn đã dự đoán chính xác trận #" + matchId + "! Phần thưởng đính kèm bên dưới.",
                                    core.MailService.MAIL_TYPE_GIFT, false, gifts, 14L * 24 * 3600 * 1000);
                        }
                    }
                } else {
                    // Thua cược
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Dọn dẹp map đấu trường, đưa toàn bộ Spectator về map lưu trước khi vào xem
     */
    public synchronized void destroyArenaMap() {
        if (arenaZone != null) {
            // Đưa toàn bộ người chơi đang xem về map lưu ban đầu
            List<Player> players = new ArrayList<>(arenaZone.players);
            for (Player p : players) {
                if (p != null) {
                    if (!p.isBot) {
                        p.exitSpectatorMode();
                    } else {
                        arenaZone.players.remove(p);
                    }
                }
            }
            arenaZone.stop_map();
            arenaZone.map_vp = null;
            arenaZone.map_dungeon = null;
            arenaZone = null;
        }
        team1Bots.clear();
        team2Bots.clear();
    }

    /**
     * Gửi thông báo trong map đấu trường
     */
    public void sendArenaNotification(String text) {
        if (arenaZone == null) return;
        try {
            Message m = new Message(-31);
            m.writer().writeByte(0);
            m.writer().writeUTF(text);
            m.writer().writeByte(0);
            m.writer().writeShort(-1);
            arenaZone.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (Exception ignored) {}
    }

    /**
     * Cho phép người chơi vào xem trận đấu (Spectator)
     */
    public void enterSpectator(Player p) throws IOException {
        if (p == null) return;
        if (arenaZone == null || !arenaZone.isRun()) {
            p.getService().send_box_ThongBao_OK("Trận đấu hiện chưa mở sân đấu hoặc đang trong thời gian giải lao!");
            return;
        }
        short spawnX = (short) (arenaZone.template.maxW / 2);
        short spawnY = 240;
        p.enterSpectatorMode(arenaZone, spawnX, spawnY);
        p.getService().send_box_ThongBao_OK("Bạn đang xem trực tiếp Trận #" + matchId + " (" + team1Name + " VS " + team2Name 
                + ")\nTỉ số: " + scoreTeam1 + " - " + scoreTeam2 + "\nBấm vào NPC hoặc dùng menu để Thoát xem bất kỳ lúc nào.");
    }

    /**
     * Người chơi tham gia đặt cược dự đoán
     */
    public synchronized boolean placeBet(Player p, int teamIndex, int ticketCount) throws IOException {
        if (p == null) return false;

        // 1. Kiểm tra trạng thái nhận cược (phải trước 1 phút khi trận bắt đầu)
        if (state != STATE_WAITING_BET || timeState <= 60) {
            p.getService().send_box_ThongBao_OK("Đã hết thời gian dự đoán (Phải dự đoán trước 1 phút trước khi trận đấu bắt đầu)! Hãy chờ trận tiếp theo hoặc vào xem trận đấu.");
            return false;
        }

        if (mapBets.containsKey(p.IDPlayer)) {
            p.getService().send_box_ThongBao_OK("Bạn đã đặt cược cho trận đấu này rồi! Vui lòng chờ kết quả.");
            return false;
        }

        if (ticketCount <= 0) ticketCount = 1;

        // 2. Kiểm tra Vé World Cup (#599)
        if (p.item.total_item_bag_by_id(4, 599) < ticketCount) {
            p.getService().send_box_ThongBao_OK("Bạn không có đủ " + ticketCount + " Vé World Cup để dự đoán!");
            return false;
        }

        // 3. Trừ vé cược
        p.item.remove_item47(4, 599, ticketCount);
        p.item.updateInventory(false);

        BetEntry entry = new BetEntry(p.IDPlayer, p.name, teamIndex, ticketCount);
        mapBets.put(p.IDPlayer, entry);

        String chosenName = (teamIndex == 1) ? team1Name : team2Name;
        p.getService().send_box_ThongBao_OK("Dự đoán thành công! Bạn đã đặt " + ticketCount + " Vé World Cup cho [" + chosenName + "] chiến thắng.\nThời gian còn lại để khóa cược: " + (timeState - 60) + " giây.");
        return true;
    }

    /**
     * Lấy chuỗi trạng thái trận đấu hiện tại hiển thị trên Menu
     */
    public String getMatchStatusText() {
        if (state == STATE_WAITING_BET) {
            int remainBet = Math.max(0, timeState - 60);
            return "Đang nhận dự đoán (Còn " + remainBet + "s)";
        } else if (state == STATE_LOCKED_BET_PREPARING) {
            return "Đã khóa cược - Bắt đầu sau " + timeState + "s";
        } else if (state == STATE_ROUND_COUNTDOWN || state == STATE_FIGHTING || state == STATE_ROUND_RESET) {
            return "Đang thi đấu Hiệp " + currentRound + " (Tỉ số: " + scoreTeam1 + " - " + scoreTeam2 + ")";
        } else {
            return "Trận đấu kết thúc - Chuẩn bị trận mới";
        }
    }
}
