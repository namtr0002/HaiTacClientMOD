package historys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import model.Player;
import database.DbManager;
import template.Log_template;
import core.ZUtil;

public class zLog implements Runnable {

    private static zLog instance;
    private final BlockingQueue<Log_template> list;
    private final Thread mythread;
    private boolean running;

    public zLog() {
        list = new LinkedBlockingQueue<>();
        mythread = new Thread(this);
    }

    public static zLog gI() {
        if (instance == null) {
            instance = new zLog();
        }
        return instance;
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                Log_template temp = list.take();
                if (temp != null) {
                    try {
                        this.save_log_db(temp);
                    } catch (Exception e) {
                        System.err.println("save log db err at " + temp.name + ": " + e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("exception at save log db");
            }
        }
    }

    private void save_log_db(Log_template temp) {
        try (
            Connection conn = DbManager.gI().getConnect();
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, ?, ?, ?)"
            )
        ) {
            ps.setInt(1, temp.accountId);
            ps.setInt(2, temp.playerId);
            ps.setString(3, temp.type != null ? temp.type : "GENERAL");
            
            String logContent = temp.data;
            if (temp.accountId == 0 && temp.playerId == 0 && temp.name != null && !temp.name.isEmpty()) {
                logContent = "[" + temp.name + "] " + logContent;
            }
            
            ps.setString(4, logContent);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting log to DB: " + e.getMessage());
        }
    }

    public void start_log() {
        this.running = true;
        this.mythread.start();
    }

    public void close_log() {
        int dem = 0;
        while (dem < 10 && list.size() > 0) {
            dem++;
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Flush all pending item pickup batches on server close
        try {
            ItemPickupHistory.flushAll();
        } catch (Exception ignored) {}

        this.running = false;
        this.mythread.interrupt();
    }

    public void add_log(Player p, String txt) {
        if (p != null && !p.isBot) {
            String lower = txt.toLowerCase();
            // Nếu là log nhặt đồ, đưa vào ItemPickupHistory để gom 100 cái/row
            if (lower.contains("nhặt")) {
                ItemPickupHistory.logPickupText(p, txt);
                return;
            }

            String type = "GENERAL";
            if (lower.contains("vong_quay_oc_sen") || lower.contains("vòng quay ốc sên") || lower.contains("ốc sên")) {
                type = "VONG_QUAY_OC_SEN";
            } else if (lower.contains("market") || lower.contains("chợ") || lower.contains("bán đồ") || lower.contains("mua đồ")) {
                type = "MARKET";
            } else if (lower.contains("giao dịch") || lower.contains("trade")) {
                type = "TRADE";
            } else if (lower.contains("học skill") || lower.contains("kỹ năng") || lower.contains("skill")) {
                type = "SKILL";
            } else if (lower.contains("extol") || lower.contains("đổi extol")) {
                type = "EXTOL";
            } else if (lower.contains("login") || lower.contains("logout") || lower.contains("đăng nhập") || lower.contains("đăng xuất")) {
                type = "AUTH";
            } else if (lower.contains("skin") || lower.contains("nâng cấp skin")) {
                type = "SKIN";
            } else if (lower.contains("hải quân") || lower.contains("hải tặc") || lower.contains("phe")) {
                type = "HUONG_NGHIEP";
            }
            
            add_log(p, type, txt);
        }
    }

    public void add_log(Player p, String type, String txt) {
        if (p != null && !p.isBot) {
            if (type != null && (type.equals("ITEM_PICKUP") || type.contains("PICKUP"))) {
                ItemPickupHistory.logPickupText(p, txt);
                return;
            }

            int accountId = (p.conn != null) ? p.conn.idUser : 0;
            int playerId = p.IDPlayer;
            
            if (type != null && type.startsWith("CLAN") && p.clan != null && p.clan.members != null && !p.clan.members.isEmpty()) {
                clan.ClanMember leader = p.clan.members.get(0);
                if (leader != null) {
                    playerId = leader.id;
                }
            }
            
            String time = "[" + ZUtil.str_time_now(1) + "]  ";
            String save = time + txt;
            if (p.conn != null) {
                save += (" ip: " + p.conn.ip);
            } else {
                save += (" ip: null");
            }
            if (save.contains("Logout")) {
                save += "\n";
            }
            this.list.add(new Log_template(accountId, playerId, type, p.name, save));
        }
    }
    
    public void add_log(String name, String txt) {
        String type = "SYSTEM";
        if (name.equalsIgnoreCase("BUG")) {
            type = "BUG";
        }
        add_log(name, type, txt);
    }

    public void add_log(String name, String type, String txt) {
        String time = "[" + ZUtil.str_time_now(1) + "]  ";
        String save = time + txt;
        this.list.add(new Log_template(0, 0, type, name, save));
    }

    public void add_log_by_name(String playerName, String type, String txt) {
        int playerId = 0;
        int accountId = 0;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT id, account_id FROM players WHERE name = ?"
             )
        ) {
            ps.setString(1, playerName);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    playerId = rs.getInt("id");
                    accountId = rs.getInt("account_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (type != null && type.startsWith("CLAN")) {
            Player onlinePlayer = map.Zone.get_player_by_name_allmap(playerName);
            clan.Clan playerClan = null;
            if (onlinePlayer != null) {
                playerClan = onlinePlayer.clan;
            } else {
                for (clan.Clan c : clan.Clan.ENTRY) {
                    if (c.members != null) {
                        for (clan.ClanMember mem : c.members) {
                            if (mem != null && mem.name.equals(playerName)) {
                                playerClan = c;
                                break;
                            }
                        }
                    }
                    if (playerClan != null) break;
                }
            }
            if (playerClan != null && playerClan.members != null && !playerClan.members.isEmpty()) {
                clan.ClanMember leader = playerClan.members.get(0);
                if (leader != null) {
                    playerId = leader.id;
                    try (Connection conn = DbManager.gI().getConnect();
                         PreparedStatement ps = conn.prepareStatement("SELECT account_id FROM players WHERE id = ?")
                    ) {
                        ps.setInt(1, playerId);
                        try (java.sql.ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                accountId = rs.getInt("account_id");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        String time = "[" + ZUtil.str_time_now(1) + "]  ";
        String save = time + txt;
        this.list.add(new Log_template(accountId, playerId, type, playerName, save));
    }

    /**
     * Dọn dẹp bảng historys theo chính sách retention tùy từng loại dữ liệu.
     *
     * NHÓM A – Runtime state (KHÔNG xóa tự động):
     *   BOSS_REWARD, CHIEMDAO, WORLDWAR, VONGQUAYOCSEN, SUDO   – delete-all + re-insert mỗi lần save
     *   IP_LOCK  – danh sách IP bị chặn, load lên RAM khi khởi động
     *   MAIL_BOX – hộp thư người chơi, MailService.cleanExpiredMails() tự quản lý TTL riêng
     *
     * NHÓM B – Dữ liệu sự kiện theo mùa (90 ngày, dùng update_at):
     *   EVENT_DATA_*, NAMI_PASS_*, ARCHI_PRIVATE_PASS_*, TICH_LUY_*, TICH_TIEU_RUBY*
     *   Dùng update_at vì saveData() luôn DELETE + INSERT mới  update_at = create_at
     *
     * NHÓM C – Chức năng chống trùng / tra cứu (phải giữ đủ lâu):
     *   NAP_THE / RECHARGE_HISTORY : VĨNH VIỄN (KHÔNG BAO GIỜ XÓA - để theo dõi và đối soát tích nạp)
     *   DUNGEON_TOP_REWARD : chỉ xóa khi claimed=true VÀ cũ hơn 7 ngày
     *   TOP_REWARD         : 14 ngày (chống phát quà top tuần trùng; week key theo số tuần năm)
     *   SHOP_LIMIT_BUY     :   7 ngày (đủ 1 chu kỳ tuần; query chỉ đếm từ đầu tuần)
     *   CLAN_TIME_GIFT     :   2 ngày (cooldown 8h – giữ buffer gấp 6 lần để phục hồi sau restart)
     *   DAU_GIA            :  30 ngày
     *   ARCHI_DAILY_*      :  30 ngày
     *
     * NHÓM D – Log debug/audit (ngắn hạn):
     *   AUTH, SYSTEM, GENERAL             :  7 ngày
     *   ITEM_PICKUP                        :  7 ngày  (batch 100 item/row, chỉ cần cho audit gần)
     *   TRADE, MARKET, VONG_QUAY_OC_SEN, SKILL, CLAN_* :  14 ngày
     *   EXTOL, SKIN, HUONG_NGHIEP, DIEM_DANH, BUG      :  30 ngày
     */
    public static void cleanOldLogs() {
        int totalDeleted = 0;

        // --- NHÓM A: Dọn dẹp bản ghi mồ côi / data cũ của nhân vật/tài khoản đã bị reset/xóa ---
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn != null) {
                totalDeleted += HistoryManager.cleanStaleHistory(conn);
            }
        } catch (Exception ignored) {}

        // --- NHÓM B: Dữ liệu sự kiện theo mùa (90 ngày, dùng update_at) ---
        totalDeleted += runDelete(
            "DELETE FROM `historys` WHERE `update_at` < DATE_SUB(NOW(), INTERVAL 90 DAY)"
            + " AND (`type` LIKE 'EVENT_DATA%'"
            + "   OR `type` LIKE 'NAMI_PASS%'"
            + "   OR `type` LIKE 'ARCHI_PRIVATE_PASS%'"
            + "   OR `type` LIKE 'TICH_LUY%'"
            + "   OR `type` LIKE 'TICH_TIEU_RUBY%')",
            "EVENT/SEASON data (90d)"
        );

        // --- NHÓM C1: DUNGEON_TOP_REWARD – chỉ xóa khi claimed=true + > 7 ngày ---
        // QUAN TRỌNG: claimed=false là quà chưa nhận – tuyệt đối không xóa
        totalDeleted += runDelete(
            "DELETE FROM `historys` WHERE `type` = 'DUNGEON_TOP_REWARD'"
            + " AND `data` LIKE '%\"claimed\":true%'"
            + " AND `update_at` < DATE_SUB(NOW(), INTERVAL 7 DAY)",
            "DUNGEON_TOP_REWARD claimed (7d)"
        );

        // --- NHÓM C2: TOP_REWARD (quà top tuần Wanted/PVP) – 14 ngày ---
        // hasReceivedReward() kiểm tra theo "week:Week-N-Year-Y"  14 ngày đủ 2 chu kỳ tuần
        // TopGift.getActiveGifts() lọc theo claimed=false + expire_at > now  chỉ xóa cũ
        totalDeleted += runDelete(
            "DELETE FROM `historys` WHERE `type` = 'TOP_REWARD'"
            + " AND `create_at` < DATE_SUB(NOW(), INTERVAL 14 DAY)",
            "TOP_REWARD (14d)"
        );


        // --- NHÓM C4: SHOP_LIMIT_BUY – 7 ngày ---
        // getPurchasedCount() chỉ đếm từ start_of_week  record cũ hơn 7 ngày không bao giờ được đếm
        totalDeleted += runDelete(
            "DELETE FROM `historys` WHERE `type` = 'SHOP_LIMIT_BUY'"
            + " AND `create_at` < DATE_SUB(NOW(), INTERVAL 7 DAY)",
            "SHOP_LIMIT_BUY (7d)"
        );

        // --- NHÓM C5: CLAN_TIME_GIFT – 2 ngày ---
        // Cooldown = 8 tiếng. Load từ DB vào HM_TIME_GIFT khi server restart.
        // Giữ 2 ngày = buffer x6 so với cooldown 8h  an toàn tuyệt đối sau restart
        totalDeleted += runDelete(
            "DELETE FROM `historys` WHERE `type` = 'CLAN_TIME_GIFT'"
            + " AND `create_at` < DATE_SUB(NOW(), INTERVAL 2 DAY)",
            "CLAN_TIME_GIFT (2d)"
        );

        // --- NHÓM C6: DAU_GIA – 30 ngày ---
        totalDeleted += runDelete(
            "DELETE FROM `historys` WHERE `type` = 'DAU_GIA'"
            + " AND `create_at` < DATE_SUB(NOW(), INTERVAL 30 DAY)",
            "DAU_GIA (30d)"
        );

        // --- NHÓM C7: ARCHI_DAILY_* – 30 ngày ---
        totalDeleted += runDelete(
            "DELETE FROM `historys` WHERE `type` LIKE 'ARCHI_DAILY_%'"
            + " AND `create_at` < DATE_SUB(NOW(), INTERVAL 30 DAY)",
            "ARCHI_DAILY (30d)"
        );

        // --- NHÓM D: Log ngắn hạn 7 ngày ---
        totalDeleted += runDelete(
            "DELETE FROM `historys`"
            + " WHERE `type` IN ('ITEM_PICKUP', 'AUTH', 'SYSTEM', 'GENERAL')"
            + " AND `create_at` < DATE_SUB(NOW(), INTERVAL 7 DAY)",
            "short logs (7d)"
        );

        // --- NHÓM D: Log 14 ngày ---
        totalDeleted += runDelete(
            "DELETE FROM `historys`"
            + " WHERE `type` IN ('TRADE', 'MARKET', 'VONG_QUAY_OC_SEN', 'SKILL')"
            + " AND `create_at` < DATE_SUB(NOW(), INTERVAL 14 DAY)",
            "mid logs (14d)"
        );

        // --- NHÓM D: CLAN_* logs 14 ngày (CLAN_TIME_GIFT đã xóa riêng ở trên) ---
        totalDeleted += runDelete(
            "DELETE FROM `historys`"
            + " WHERE `type` LIKE 'CLAN_%'"
            + " AND `type` != 'CLAN_TIME_GIFT'"
            + " AND `create_at` < DATE_SUB(NOW(), INTERVAL 14 DAY)",
            "CLAN logs (14d)"
        );

        // --- NHÓM D: Log audit 30 ngày ---
        totalDeleted += runDelete(
            "DELETE FROM `historys`"
            + " WHERE `type` IN ('EXTOL', 'SKIN', 'HUONG_NGHIEP', 'DIEM_DANH', 'BUG')"
            + " AND `create_at` < DATE_SUB(NOW(), INTERVAL 30 DAY)",
            "audit logs (30d)"
        );

        System.out.println("[zLog] cleanOldLogs completed — total deleted: " + totalDeleted + " rows.");
    }

    /**
     * Thực thi một câu DELETE và trả về số dòng đã xóa. Log lỗi nếu có.
     */
    private static int runDelete(String sql, String label) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                System.out.println("[zLog] Cleaned [" + label + "]: " + deleted + " rows.");
            }
            return deleted;
        } catch (SQLException e) {
            System.err.println("[zLog] Error cleaning [" + label + "]: " + e.getMessage());
            return 0;
        }
    }
}
