package map.zones;

import model.Player;
import core.Manager;
import core.ZUtil;
import network.Service;
import network.Message;
import map.Zone;
import map.Vgo;
import zabstracts.AbsEventDungeon;
import template.ItemTemplate4;
import template.GiftBox;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TranChienLon — Đại Chiến Thế Giới / Lễ Hội Hải Tặc (WorldWar Dungeon).
 *
 * <p>Kế thừa chuẩn {@link zabstracts.AbsEventDungeon}, điều phối 3 thế lực:
 * <ul>
 *   <li>Hải Quân (Type PK 11)</li>
 *   <li>Quân Cách Mạng (Type PK 12)</li>
 *   <li>Hải Tặc (Type PK 13)</li>
 * </ul>
 */
public class TranChienLon extends AbsEventDungeon {

    public static final byte TYPE_PK_HAI_QUAN = 11;
    public static final byte TYPE_PK_QUAN_CACH_MANG = 12;
    public static final byte TYPE_PK_HAI_TAC = 13;

    public static final byte STATUS_CLOSE = 0;
    public static final byte STATUS_REGISTER = 1;
    public static final byte STATUS_WAR = 2;

    public static byte status = STATUS_CLOSE;
    public static boolean runnning = false;

    public static final ConcurrentHashMap<String, Integer> mem_haiquan = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Integer> mem_haitac = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Integer> mem_quancachmang = new ConcurrentHashMap<>();

    public static HashMap<String, Integer> wins = new HashMap<>();
    public static HashMap<String, Integer> top3 = new HashMap<>();

    public static long registerStartTime = 0;
    public static long warStartTime = 0;
    public static long closeTime = 0;
    public static String currentSessionToken = "";

    // ======================================================
    //  EVENT DUNGEON OVERRIDES
    // ======================================================

    @Override
    public int getDungeonId() {
        return -991;
    }

    @Override
    public String getJoinTitle() {
        return "Trận Chiến Lớn";
    }

    @Override
    public String getJoinDesc() {
        return "Tham gia Trận Chiến Lớn giữa Hải Quân, Hải Tặc và Quân Cách Mạng?";
    }

    @Override
    public String canJoin(Player p) {
        if (p == null) return "Người chơi không hợp lệ!";
        return null;
    }

    @Override
    public void create() {
        // Trận Chiến Lớn diễn ra trên các map sự kiện công cộng (272..275)
        this.time = System.currentTimeMillis() + 600_000L;
    }

    @Override
    public void join(Player p) throws IOException {
        if (p == null) return;
        
        // 1. Kiểm tra phe
        if (p.huongnghiep == 0) {
            zinterfaces.menus.SubMenuWorldWar.openSelectFactionMenu(p);
            p.getService().send_box_ThongBao_OK("Hãy chọn 1 trong 3 phe (Hải Quân, Hải Tặc, Quân Cách Mạng) trước khi vào sảnh chiến!");
            return;
        }

        if (status == STATUS_CLOSE && !runnning) {
            startRegister();
        }

        // 2. Xác định map mục tiêu theo level
        int targetMapId;
        if (p.level < 50) {
            targetMapId = 272;
        } else if (p.level < 70) {
            targetMapId = 273;
        } else if (p.level < 90) {
            targetMapId = 274;
        } else {
            targetMapId = 275;
        }

        Zone[] zones = Zone.getMapByID(targetMapId);
        if (zones != null && zones.length > 0 && zones[0] != null) {
            Vgo vgo = new Vgo();
            vgo.map_go = new Zone[]{zones[0]};
            vgo.xnew = (short) (vgo.map_go[0].template.maxW / 2);
            vgo.ynew = (short) (vgo.map_go[0].template.maxH / 2);
            p.goto_map(vgo);
            setType(p);
            p.ensureFactionSkill();
            p.send_skill();
            dispatchBots();
            if (status == STATUS_REGISTER) {
                p.getService().send_time_cool_down(registerStartTime + 30_000L, "Chờ bắt đầu", 0);
            } else if (status == STATUS_WAR) {
                p.getService().send_time_cool_down(warStartTime + 600_000L, "Thời gian", 0);
            }
            p.getService().send_box_ThongBao_OK("Bạn đã tiến vào sảnh chiến Trận Chiến Lớn!");
        }
    }

    private long lastUpdateTimer = 0;

    @Override
    public void update(Zone zone) throws IOException {
        if (zone == null || zone.players == null) return;
        if (zone.template != null && zone.template.id >= 272 && zone.template.id <= 275) {
            // 1. Tự động kiểm tra chuyển đổi trạng thái vòng đấu (Chờ đăng ký -> Khai hỏa -> Kết thúc -> Chờ đăng ký mới)
            checkAutoStart();

            // 2. Cập nhật cờ PK cho tất cả người chơi trong map theo phe đã chọn
            for (int i = 0; i < zone.players.size(); i++) {
                Player p0 = zone.players.get(i);
                if (p0 != null) {
                    setType(p0);
                }
            }

            // 3. Gửi thời gian chờ / thời gian chiến đấu theo trạng thái map chuẩn
            long now = System.currentTimeMillis();
            if (now - lastUpdateTimer >= 2000) {
                lastUpdateTimer = now;
                if (status == STATUS_REGISTER) {
                    long timeEnd = registerStartTime + 30_000L;
                    String title = "Chờ bắt đầu";
                    for (int i = 0; i < zone.players.size(); i++) {
                        Player p0 = zone.players.get(i);
                        if (p0 != null && p0.getService() != null) {
                            p0.getService().send_time_cool_down(timeEnd, title, 0);
                        }
                    }
                } else if (status == STATUS_WAR) {
                    long timeEnd = warStartTime + 600_000L;
                    String title = "Thời gian";
                    for (int i = 0; i < zone.players.size(); i++) {
                        Player p0 = zone.players.get(i);
                        if (p0 != null && p0.getService() != null) {
                            p0.getService().send_time_cool_down(timeEnd, title, 0);
                        }
                    }
                } else if (status == STATUS_CLOSE) {
                    long timeEnd = closeTime + 10_000L;
                    String title = "Trận mới sau";
                    for (int i = 0; i < zone.players.size(); i++) {
                        Player p0 = zone.players.get(i);
                        if (p0 != null && p0.getService() != null) {
                            p0.getService().send_time_cool_down(timeEnd, title, 0);
                        }
                    }
                }
            }
        }
    }

    // ======================================================
    //  STATIC GETTERS & HELPERS
    // ======================================================

    public static boolean isWarRunning() {
        return status == STATUS_WAR && runnning;
    }

    public static boolean isRegistrationOpen() {
        return status == STATUS_REGISTER;
    }

    public static String getFactionName(byte huongnghiep) {
        switch (huongnghiep) {
            case 1: return "Hải Quân";
            case 2: return "Hải Tặc";
            case 3: return "Quân Cách Mạng";
            default: return "Chưa chọn";
        }
    }

    public static int getNavyPoints() {
        return getTotalPoint(mem_haiquan);
    }

    public static int getPiratePoints() {
        return getTotalPoint(mem_haitac);
    }

    public static int getRevPoints() {
        return getTotalPoint(mem_quancachmang);
    }

    public static boolean isNavy(String name) {
        return mem_haiquan.containsKey(name);
    }

    public static boolean isPirate(String name) {
        return mem_haitac.containsKey(name);
    }

    public static boolean isRevolutionary(String name) {
        return mem_quancachmang.containsKey(name);
    }

    public static void dispatchBots() {
        try {
            bot.botplayer.BotPlayerManager.dispatchBotsToWorldWar();
        } catch (Exception ignored) {}
    }

    public static void checkAndRestoreState(int hour, int min) {
        // Tương thích với ServerEventManager
    }

    // ======================================================
    //  WAR LIFECYCLE & ROUND MANAGEMENT
    // ======================================================

    public static void startRegister() {
        startRegister(ZUtil.generateMD5Token("TranChienLon_" + System.currentTimeMillis()));
    }

    public static void startRegister(String sessionToken) {
        try {
            status = STATUS_REGISTER;
            runnning = false;
            currentSessionToken = (sessionToken != null && !sessionToken.isEmpty()) ? sessionToken : ZUtil.generateMD5Token("TranChienLon_" + System.currentTimeMillis());
            registerStartTime = System.currentTimeMillis();
            warStartTime = 0;
            closeTime = 0;
            mem_haiquan.clear();
            mem_haitac.clear();
            mem_quancachmang.clear();
            top3.clear();
            wins.clear();
            saveData();
            Manager.gI().chatKTG(0, "Đại Chiến Thế Giới (Trận Chiến Lớn) đã mở cổng đăng ký! Hãy chọn phe tham chiến!", 5);
        } catch (Exception ex) {
            Logger.getLogger(TranChienLon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void checkAutoStart() {
        long now = System.currentTimeMillis();
        if (status == STATUS_REGISTER) {
            long elapsed = now - registerStartTime;
            if (elapsed >= 30_000L || registerStartTime == 0) {
                startWar();
            }
        } else if (status == STATUS_WAR) {
            long elapsed = now - warStartTime;
            if (warStartTime > 0 && elapsed >= 600_000L) {
                close();
            }
        } else if (status == STATUS_CLOSE) {
            long elapsed = now - closeTime;
            if (closeTime > 0 && elapsed >= 10_000L) {
                startRegister();
            }
        }
    }

    public static void startWar() {
        try {
            status = STATUS_WAR;
            runnning = true;
            warStartTime = System.currentTimeMillis();
            saveData();

            for (map.Map mapall : map.MapManager.getInstance().getMaps()) {
                for (map.Zone map : mapall.zones) {
                    for (int i = 0; i < map.players.size(); i++) {
                        Player p0 = map.players.get(i);
                        if (p0 != null && p0.map != null && p0.map.map_little_garden == null) {
                            setType(p0);
                            if (p0.conn != null) {
                                p0.send_skill();
                            }
                        }
                    }
                }
            }

            dispatchBots();
            Manager.gI().chatKTG(0, "Trận Chiến Lớn chính thức KHAI HỎA! Các lực lượng Hải Quân, Hải Tặc và Quân Cách Mạng hãy xông lên!", 5);
        } catch (IOException ex) {
            Logger.getLogger(TranChienLon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void start() {
        startWar();
    }

    public static void close() {
        try {
            status = STATUS_CLOSE;
            runnning = false;
            closeTime = System.currentTimeMillis();
            wins.clear();

            int totalPirate = getTotalPoint(mem_haitac);
            int totalNavy = getTotalPoint(mem_haiquan);
            int totalRev = getTotalPoint(mem_quancachmang);
            String winningFaction = "";
            if (totalPirate > totalNavy && totalPirate > totalRev) {
                wins.putAll(mem_haitac);
                winningFaction = "phe Hải Tặc";
            } else if (totalNavy > totalPirate && totalNavy > totalRev) {
                wins.putAll(mem_haiquan);
                winningFaction = "phe Hải Quân";
            } else if (totalRev > totalPirate && totalRev > totalNavy) {
                wins.putAll(mem_quancachmang);
                winningFaction = "phe Quân Cách Mạng";
            } else {
                wins.putAll(mem_haiquan);
                wins.putAll(mem_haitac);
                wins.putAll(mem_quancachmang);
                winningFaction = "các phe hòa nhau";
            }

            getTop3();
            StringBuilder names = new StringBuilder();
            for (Map.Entry<String, Integer> entry : top3.entrySet()) {
                names.append(entry.getKey()).append(", ");
            }
            Manager.gI().chatKTG(0, "Trận Chiến Lớn đã kết thúc, " + winningFaction + " giành chiến thắng! Top 1-3: " + names, 5);

            // 1. Trao quà Top 1..3 Trận Chiến Lớn qua Hộp Thư
            int rankIdx = 0;
            for (Map.Entry<String, Integer> entry : top3.entrySet()) {
                String topPlayerName = entry.getKey();
                if (topPlayerName == null || topPlayerName.isEmpty()) continue;
                Player onlineP = Zone.get_player_by_name_allmap(topPlayerName);
                if (onlineP != null && (onlineP.isBot || onlineP.conn == null)) continue;
                int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(topPlayerName);
                if (ids[0] > 0) {
                    List<GiftBox> topRewards = rank.TopWorldWar.gI().getRewards(rankIdx);
                    core.MailService.sendMailOffline(ids[0], ids[1], topPlayerName, "Trận Chiến Lớn",
                            "Quà Top " + (rankIdx + 1) + " Trận Chiến Lớn",
                            "Chúc mừng bạn đạt Top " + (rankIdx + 1) + " Trận Chiến Lớn với " + entry.getValue() + " điểm!",
                            core.MailService.MAIL_TYPE_GIFT, false, topRewards, 14L * 24 * 3600 * 1000);
                }
                rankIdx++;
                if (rankIdx >= 3) break;
            }

            // 2. Trao quà Chiến Thắng Phe vào Hộp Thư
            List<GiftBox> winFactionGifts = new ArrayList<>();
            winFactionGifts.add(new GiftBox(4, 0, 2_000_000)); // 2m Beri
            winFactionGifts.add(new GiftBox(4, 1, 200));       // 200 Ruby
            winFactionGifts.add(new GiftBox(7, 1, 10));        // 10 Đá Cường Hóa

            for (String winnerName : wins.keySet()) {
                if (winnerName == null || winnerName.isEmpty()) continue;
                Player onlineP = Zone.get_player_by_name_allmap(winnerName);
                if (onlineP != null && (onlineP.isBot || onlineP.conn == null)) continue;
                int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(winnerName);
                if (ids[0] > 0) {
                    core.MailService.sendMailOffline(ids[0], ids[1], winnerName, "Trận Chiến Lớn",
                            "Thưởng Chiến Thắng " + winningFaction,
                            "Chúc mừng bạn và " + winningFaction + " đã giành chiến thắng Trận Chiến Lớn! Phần thưởng chiến thắng đính kèm bên dưới.",
                            core.MailService.MAIL_TYPE_GIFT, false, winFactionGifts, 14L * 24 * 3600 * 1000);
                }
            }

            // 3. Trao quà Tham Gia cho các phe còn lại
            List<GiftBox> partGifts = new ArrayList<>();
            partGifts.add(new GiftBox(4, 0, 500_000)); // 500k Beri
            partGifts.add(new GiftBox(4, 1, 50));      // 50 Ruby
            partGifts.add(new GiftBox(7, 1, 3));       // 3 Đá Cường Hóa

            Set<String> allParticipants = new HashSet<>();
            allParticipants.addAll(mem_haiquan.keySet());
            allParticipants.addAll(mem_haitac.keySet());
            allParticipants.addAll(mem_quancachmang.keySet());
            allParticipants.removeAll(wins.keySet()); // Chỉ lấy những người không thuộc phe thắng

            for (String loserName : allParticipants) {
                if (loserName == null || loserName.isEmpty()) continue;
                Player onlineP = Zone.get_player_by_name_allmap(loserName);
                if (onlineP != null && (onlineP.isBot || onlineP.conn == null)) continue;
                int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(loserName);
                if (ids[0] > 0) {
                    core.MailService.sendMailOffline(ids[0], ids[1], loserName, "Trận Chiến Lớn",
                            "Thưởng Tham Gia Trận Chiến Lớn",
                            "Cảm ơn bạn đã tham gia Trận Chiến Lớn! Phần thưởng khích lệ đính kèm bên dưới.",
                            core.MailService.MAIL_TYPE_GIFT, false, partGifts, 14L * 24 * 3600 * 1000);
                }
            }

            for (map.Map mapall : map.MapManager.getInstance().getMaps()) {
                for (map.Zone map : mapall.zones) {
                    for (int i = 0; i < map.players.size(); i++) {
                        Player p0 = map.players.get(i);
                        if (p0 != null && p0.map != null && p0.map.map_little_garden == null) {
                            if (p0.type_pk == TYPE_PK_HAI_TAC || p0.type_pk == TYPE_PK_HAI_QUAN || p0.type_pk == TYPE_PK_QUAN_CACH_MANG) {
                                p0.type_pk = -1;
                                for (int k = 0; k < p0.map.players.size(); k++) {
                                    Player p_other = p0.map.players.get(k);
                                    if (p_other != null && p_other.getService() != null) {
                                        p_other.getService().update_PK(p0, false);
                                    }
                                }
                            }
                            if (p0.conn != null) {
                                p0.send_skill();
                            }
                        }
                    }
                }
            }

            mem_haiquan.clear();
            mem_haitac.clear();
            mem_quancachmang.clear();
            top3.clear();
            saveData();
        } catch (IOException ex) {
            Logger.getLogger(TranChienLon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setType(Player p) throws IOException {
        if (p == null || p.map == null || p.map.template == null) return;

        if (p.isSpectator) {
            p.type_pk = -1;
            if (p.getService() != null) {
                p.getService().update_PK(p, false);
            }
            return;
        }
        if (mapTranChienLon(p.map.template.id) || !runnning || status != STATUS_WAR) {
            if (p.type_pk == TYPE_PK_HAI_TAC || p.type_pk == TYPE_PK_HAI_QUAN || p.type_pk == TYPE_PK_QUAN_CACH_MANG) {
                p.type_pk = -1;
                if (p.getService() != null) {
                    p.getService().update_PK(p, false);
                }
                for (int i = 0; i < p.map.players.size(); i++) {
                    Player p0 = p.map.players.get(i);
                    if (p0 != null && p0.getService() != null) {
                        p0.getService().update_PK(p, false);
                    }
                }
            }
            return;
        }

        if (p.isBot && p.huongnghiep == 0) {
            int hash = Math.abs(p.name != null ? p.name.hashCode() : p.IDPlayer);
            p.huongnghiep = (byte) ((hash % 3) + 1);
        }
        if (p.huongnghiep == 2) {
            if (!mem_haitac.containsKey(p.name)) {
                mem_haitac.put(p.name, 0);
            }
            p.type_pk = TYPE_PK_HAI_TAC;
        } else if (p.huongnghiep == 1) {
            if (!mem_haiquan.containsKey(p.name)) {
                mem_haiquan.put(p.name, 0);
            }
            p.type_pk = TYPE_PK_HAI_QUAN;
        } else if (p.huongnghiep == 3) {
            if (!mem_quancachmang.containsKey(p.name)) {
                mem_quancachmang.put(p.name, 0);
            }
            p.type_pk = TYPE_PK_QUAN_CACH_MANG;
        } else {
            return;
        }
        for (int i = 0; i < p.map.players.size(); i++) {
            Player p0 = p.map.players.get(i);
            if (p0 != null && p0.getService() != null) {
                p0.getService().update_PK(p, false);
            }
        }
    }

    public static boolean mapTranChienLon(int id) {
        return id < 272 || id > 275;
    }

    public static int getTotalPoint(ConcurrentHashMap<String, Integer> map) {
        int total = 0;
        for (int value : map.values()) {
            total += value;
        }
        return total;
    }

    public static void upPoint(Player p_atk, Player p_target) {
        try {
            int victimStreak = p_target.killStreak;
            p_target.killStreak = 0;

            p_atk.killStreak++;
            int killerStreak = p_atk.killStreak;

            String msg = "";
            int color = 0;

            if (killerStreak == 2) {
                msg = p_atk.name + " đã đạt Song Sát (Double Kill)!";
            } else if (killerStreak == 3) {
                msg = p_atk.name + " đã đạt Tam Sát (Triple Kill)!";
                color = 5;
            } else if (killerStreak == 4) {
                msg = p_atk.name + " đã đạt Tứ Sát (Quadra Kill)!";
                color = 5;
            } else if (killerStreak == 5) {
                msg = p_atk.name + " đã đạt Ngũ Sát (Penta Kill)!";
                color = 5;
            } else if (killerStreak == 6) {
                msg = p_atk.name + " đã đạt chuỗi hạ liên tục Chiến Thần (Unstoppable)!";
                color = 5;
            } else if (killerStreak == 7) {
                msg = p_atk.name + " đã đạt chuỗi hạ liên tục Thần Thánh (Godlike)!";
                color = 5;
            } else if (killerStreak >= 8) {
                msg = p_atk.name + " đã đạt chuỗi hạ liên tục Huyền Thoại (Legendary)!";
                color = 5;
            }

            Zone currentZone = p_atk.map;
            if (currentZone != null) {
                if (victimStreak >= 3) {
                    String shutdownMsg = p_atk.name + " đã CHẤM DỨT chuỗi hạ liên tục của " + p_target.name + " (Shut Down)!";
                    broadcastToZone(currentZone, shutdownMsg, 5);
                }
                if (!msg.isEmpty()) {
                    broadcastToZone(currentZone, msg, color);
                }
            }
        } catch (Exception ignored) {}

        p_atk.killWW++;
        p_target.deadWW++;
        try {
            p_atk.updateExpFactionSkill(3000);
        } catch (Exception ignored) {}

        if (p_atk.huongnghiep == 2) {
            int point = mem_haitac.getOrDefault(p_atk.name, 0);
            point += 1;
            mem_haitac.put(p_atk.name, point);
            p_atk.point_ww = (byte) Math.min(127, point);
        } else if (p_atk.huongnghiep == 1) {
            int point = mem_haiquan.getOrDefault(p_atk.name, 0);
            point += 1;
            mem_haiquan.put(p_atk.name, point);
            p_atk.point_ww = (byte) Math.min(127, point);
        } else if (p_atk.huongnghiep == 3) {
            int point = mem_quancachmang.getOrDefault(p_atk.name, 0);
            point += 1;
            mem_quancachmang.put(p_atk.name, point);
            p_atk.point_ww = (byte) Math.min(127, point);
        }

        if (p_target.huongnghiep == 2) {
            int point = mem_haitac.getOrDefault(p_target.name, 0);
            if (point > 0) point -= 1;
            mem_haitac.put(p_target.name, point);
            p_target.point_ww = (byte) Math.max(0, point);
        } else if (p_target.huongnghiep == 1) {
            int point = mem_haiquan.getOrDefault(p_target.name, 0);
            if (point > 0) point -= 1;
            mem_haiquan.put(p_target.name, point);
            p_target.point_ww = (byte) Math.max(0, point);
        } else if (p_target.huongnghiep == 3) {
            int point = mem_quancachmang.getOrDefault(p_target.name, 0);
            if (point > 0) point -= 1;
            mem_quancachmang.put(p_target.name, point);
            p_target.point_ww = (byte) Math.max(0, point);
        }

        if (p_atk.map != null && p_atk.map.getService() != null) {
            p_atk.map.getService().send_update_point_ww(p_atk);
        }
        if (p_target.map != null && p_target.map.getService() != null) {
            p_target.map.getService().send_update_point_ww(p_target);
        }
    }

    private static void broadcastToZone(Zone zone, String text, int color) {
        try {
            Message m = new Message(-31);
            m.writer().writeByte(0);
            m.writer().writeUTF(text);
            m.writer().writeByte(color);
            m.writer().writeShort(-1);
            for (int i = 0; i < zone.players.size(); i++) {
                Player p0 = zone.players.get(i);
                if (p0 != null && p0.conn != null) {
                    p0.addmsg(m);
                }
            }
            m.cleanup();
        } catch (Exception ignored) {}
    }

    public static synchronized void logWW(String text) {
        try {
            java.nio.file.Files.write(
                java.nio.file.Paths.get("C:/DepLor/HTTH/Team/HaiTacTiHonServer/diagnostic_ww.txt"),
                (text + "\n").getBytes("UTF-8"),
                java.nio.file.StandardOpenOption.CREATE,
                java.nio.file.StandardOpenOption.APPEND
            );
        } catch (Exception ignored) {}
    }

    public static HashMap<String, Integer> getRank() {
        HashMap<String, Integer> full = new LinkedHashMap<>();
        full.putAll(mem_haiquan);
        full.putAll(mem_haitac);
        full.putAll(mem_quancachmang);

        List<Map.Entry<String, Integer>> list = new ArrayList<>(full.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        HashMap<String, Integer> rank = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            if (entry.getValue() <= 0) {
                continue;
            }
            rank.put(entry.getKey(), entry.getValue());
        }
        return rank;
    }

    public static void getTop3() {
        HashMap<String, Integer> top = getRank();
        int count = 0;
        for (Map.Entry<String, Integer> entry : top.entrySet()) {
            if (count < 3 && entry.getValue() > 0) {
                count += 1;
                top3.put(entry.getKey(), count);
            }
        }
    }

    public static String findTop() {
        StringBuilder str = new StringBuilder();
        int count = 0;
        HashMap<String, Integer> top = getRank();
        for (Map.Entry<String, Integer> entry : top.entrySet()) {
            count += 1;
            Player p = map.Zone.get_player_by_name_allmap(entry.getKey());
            if (p != null) {
                str.append("Top ").append(count).append(": ").append(entry.getKey()).append(" có ").append(entry.getValue()).append(" điểm, tại ").append(p.map.template.name).append("\n");
            } else {
                str.append("Top ").append(count).append(": ").append(entry.getKey()).append(" có ").append(entry.getValue()).append(" điểm, đang offline\n");
            }
            if (count >= 3) {
                break;
            }
        }
        return str.toString();
    }

    public static void loadData() {
        historys.WorldWarHistory.loadData(mem_haiquan, mem_haitac, mem_quancachmang, wins, top3);
    }

    public static void saveData() {
        historys.WorldWarHistory.saveData(mem_haiquan, mem_haitac, mem_quancachmang, wins, top3);
    }

    public static class DataContainer {
        private ConcurrentHashMap<String, Integer> mem_haiquan;
        private ConcurrentHashMap<String, Integer> mem_haitac;
        private ConcurrentHashMap<String, Integer> mem_quancachmang;
        private HashMap<String, Integer> wins;
        private HashMap<String, Integer> top3;

        public DataContainer(ConcurrentHashMap<String, Integer> mem_haiquan,
                             ConcurrentHashMap<String, Integer> mem_haitac,
                             ConcurrentHashMap<String, Integer> mem_quancachmang,
                             HashMap<String, Integer> wins,
                             HashMap<String, Integer> top3) {
            this.mem_haiquan = mem_haiquan;
            this.mem_haitac = mem_haitac;
            this.mem_quancachmang = mem_quancachmang;
            this.wins = wins;
            this.top3 = top3;
        }

        public DataContainer() {
            this.mem_haiquan = new ConcurrentHashMap<>();
            this.mem_haitac = new ConcurrentHashMap<>();
            this.mem_quancachmang = new ConcurrentHashMap<>();
            this.wins = new HashMap<>();
            this.top3 = new HashMap<>();
        }

        public ConcurrentHashMap<String, Integer> getMem_haiquan() { return mem_haiquan; }
        public void setMem_haiquan(ConcurrentHashMap<String, Integer> mem_haiquan) { this.mem_haiquan = mem_haiquan; }

        public ConcurrentHashMap<String, Integer> getMem_haitac() { return mem_haitac; }
        public void setMem_haitac(ConcurrentHashMap<String, Integer> mem_haitac) { this.mem_haitac = mem_haitac; }

        public ConcurrentHashMap<String, Integer> getMem_quancachmang() { return mem_quancachmang; }
        public void setMem_quancachmang(ConcurrentHashMap<String, Integer> mem_quancachmang) { this.mem_quancachmang = mem_quancachmang; }

        public HashMap<String, Integer> getWins() { return wins; }
        public void setWins(HashMap<String, Integer> wins) { this.wins = wins; }

        public HashMap<String, Integer> getTop3() { return top3; }
        public void setTop3(HashMap<String, Integer> top3) { this.top3 = top3; }
    }
}
