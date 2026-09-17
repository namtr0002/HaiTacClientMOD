package core;

import model.DauGia;
import map.zones.TranChienKhongLo;
import map.zones.WorldWar;
import bot.BotTruyNa;
import bot.BotKhoBau;
import boss.BossPica;
import boss.BossTheGioi;
import map.zones.ChiemDao;
import map.zones.ThuLinhBienKhoi;
import event.SuKienHalloween;
import event.eboss.BiNgoMa;
import event.SuKienNoel;
import event.eboss.SantaNoel;
import event.eboss.QuaiVatTuyetNoel;
import activities.*;
import map.zones.DauTruongTuDo;
import clan.Clan;
import model.Player;
import event.EventManager;
import event.SuKienGioTo;
import network.SessionManager;
import boss.SuperBossManager;
import map.Zone;
import mob.Mob;
import map.Vgo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import map.zones.MapTranChienKhongLo;
import java.util.ArrayList;
import java.util.List;
import map.zones.PvpBang;
import clan.ClanChat;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.text.SimpleDateFormat;

public class ServerEventManager {

    private Thread thread_cal_time;
    private Thread thread_save_data;
    private boolean running;
    private int lastProcessedDay = -1;
    private String lastTriggeredMaintenanceSlot = "";
    private long lastSettingsReloadTime = 0;

    public ServerEventManager() {
        this.running = false;
    }

    public static boolean isMaintenanceTime(int currentHour, int currentMin, int currentSec) {
        String conf = Manager.gI().server_automaintenance;
        if (conf == null || conf.trim().isEmpty()) return false;
        conf = conf.trim().toLowerCase();
        if (conf.equals("off") || conf.equals("false") || conf.equals("-1") || conf.equals("none")) {
            return false;
        }
        String[] timeSlots = conf.split("[,;\\n]+");
        for (String slot : timeSlots) {
            slot = slot.trim();
            if (slot.isEmpty()) continue;
            String[] parts = slot.split(":");
            if (parts.length >= 2) {
                try {
                    int targetHour = Integer.parseInt(parts[0].trim());
                    int targetMin = Integer.parseInt(parts[1].trim());
                    int targetSec = (parts.length >= 3) ? Integer.parseInt(parts[2].trim()) : 0;
                    if (currentHour == targetHour && currentMin == targetMin && currentSec == targetSec) {
                        return true;
                    }
                } catch (Exception ignored) {}
            }
        }
        return false;
    }

    public static void processPendingServerCommands() {
        try (java.sql.Connection conn = database.DbManager.gI().getConnect()) {
            if (conn == null) return;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // 1. Xử lý các lệnh chờ từ bảng server_commands
            try (java.sql.PreparedStatement ps = conn.prepareStatement(
                    "SELECT `id`, `command`, `created_by` FROM `server_commands` WHERE `status` = 0 ORDER BY `id` ASC LIMIT 5");
                 java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int cmdId = rs.getInt("id");
                    String rawCmd = rs.getString("command");
                    String createdBy = rs.getString("created_by");
                    if (createdBy == null || createdBy.isEmpty()) createdBy = "admin";
                    if (rawCmd == null || rawCmd.trim().isEmpty()) continue;

                    // Đánh dấu status = 1 (Đang xử lý)
                    try (java.sql.PreparedStatement psRun = conn.prepareStatement(
                            "UPDATE `server_commands` SET `status` = 1 WHERE `id` = ?")) {
                        psRun.setInt(1, cmdId);
                        psRun.executeUpdate();
                    }

                    // Thực thi lệnh qua PanelManager GM Console Engine
                    String res = "";
                    int finalStatus = 2; // Thành công
                    try {
                        res = PanelManager.gI().executeAdminCommand(rawCmd.trim());
                        if (res != null && res.startsWith("[ERROR]")) {
                            finalStatus = 3;
                        }
                    } catch (Exception ex) {
                        res = "[EXCEPTION] " + ex.getMessage();
                        finalStatus = 3;
                    }

                    // Lưu phản hồi trở lại server_commands
                    try (java.sql.PreparedStatement psDone = conn.prepareStatement(
                            "UPDATE `server_commands` SET `status` = ?, `response` = ?, `executed_at` = NOW() WHERE `id` = ?")) {
                        psDone.setInt(1, finalStatus);
                        psDone.setString(2, res);
                        psDone.setInt(3, cmdId);
                        psDone.executeUpdate();
                    }

                    // Ghi nhận nhật ký lệnh và phản hồi hoàn chỉnh vào bảng historys
                    try {
                        JSONObject logObj = new JSONObject();
                        logObj.put("command_id", cmdId);
                        logObj.put("command", rawCmd);
                        logObj.put("status", finalStatus);
                        logObj.put("response", res);
                        logObj.put("created_by", createdBy);
                        logObj.put("executed_at", sdf.format(new java.util.Date()));
                        logObj.put("source", "WEB_IPC");
                        logObj.put("md5", ZUtil.generateMD5Token("CMD_" + cmdId + "_" + System.currentTimeMillis()));

                        boolean updatedHist = false;
                        try (java.sql.PreparedStatement psHistUp = conn.prepareStatement(
                                "UPDATE `historys` SET `type` = 'SERVER_COMMAND_HISTORY', `data` = ?, `update_at` = NOW() WHERE `type` = 'PENDING_SERVER_COMMAND' AND `data` LIKE ?")) {
                            psHistUp.setString(1, logObj.toJSONString());
                            psHistUp.setString(2, "%\"command_id\":" + cmdId + "%");
                            if (psHistUp.executeUpdate() > 0) {
                                updatedHist = true;
                            }
                        }
                        if (!updatedHist) {
                            try (java.sql.PreparedStatement psHistIn = conn.prepareStatement(
                                    "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`, `create_at`, `update_at`) VALUES (0, 0, 'SERVER_COMMAND_HISTORY', ?, NOW(), NOW())")) {
                                psHistIn.setString(1, logObj.toJSONString());
                                psHistIn.executeUpdate();
                            }
                        }
                    } catch (Exception exHist) {
                        Log.error("DBCommand", "Error recording command history to historys", exHist);
                    }

                    Log.info("DBCommand", "Processed Web IPC command #" + cmdId + " ('" + rawCmd + "'): " + res);
                }
            } catch (Exception ex1) {
                // Table server_commands temporary error or does not exist
            }

            // 2. Xử lý các lệnh gửi trực tiếp qua bảng historys (type = 'PENDING_SERVER_COMMAND')
            try (java.sql.PreparedStatement psH = conn.prepareStatement(
                    "SELECT `id`, `data` FROM `historys` WHERE `type` = 'PENDING_SERVER_COMMAND' ORDER BY `id` ASC LIMIT 5");
                 java.sql.ResultSet rsH = psH.executeQuery()) {
                while (rsH.next()) {
                    int hId = rsH.getInt("id");
                    String hData = rsH.getString("data");
                    if (hData == null || hData.trim().isEmpty()) continue;

                    String rawCmd = "";
                    String createdBy = "admin";
                    try {
                        JSONObject json = (JSONObject) JSONValue.parse(hData);
                        if (json != null) {
                            if (json.containsKey("command")) rawCmd = (String) json.get("command");
                            if (json.containsKey("created_by")) createdBy = (String) json.get("created_by");
                            // Nếu record này đã có command_id đã được nhánh 1 xử lý thì bỏ qua
                            if (json.containsKey("command_id") && ((Number) json.get("command_id")).intValue() > 0) {
                                continue;
                            }
                        }
                    } catch (Exception ignored) {}

                    if (rawCmd == null || rawCmd.trim().isEmpty()) {
                        rawCmd = hData.trim();
                    }
                    if (rawCmd.isEmpty()) continue;

                    // Thực thi lệnh qua PanelManager
                    String res = "";
                    int finalStatus = 2;
                    try {
                        res = PanelManager.gI().executeAdminCommand(rawCmd.trim());
                        if (res != null && res.startsWith("[ERROR]")) {
                            finalStatus = 3;
                        }
                    } catch (Exception ex) {
                        res = "[EXCEPTION] " + ex.getMessage();
                        finalStatus = 3;
                    }

                    JSONObject logObj = new JSONObject();
                    logObj.put("history_id", hId);
                    logObj.put("command", rawCmd);
                    logObj.put("status", finalStatus);
                    logObj.put("response", res);
                    logObj.put("created_by", createdBy);
                    logObj.put("executed_at", sdf.format(new java.util.Date()));
                    logObj.put("source", "HISTORYS_DB");
                    logObj.put("md5", ZUtil.generateMD5Token("HIST_CMD_" + hId + "_" + System.currentTimeMillis()));

                    try (java.sql.PreparedStatement psHistDone = conn.prepareStatement(
                            "UPDATE `historys` SET `type` = 'SERVER_COMMAND_HISTORY', `data` = ?, `update_at` = NOW() WHERE `id` = ?")) {
                        psHistDone.setString(1, logObj.toJSONString());
                        psHistDone.setInt(2, hId);
                        psHistDone.executeUpdate();
                    }
                    Log.info("DBCommand", "Processed historys command #" + hId + " ('" + rawCmd + "'): " + res);
                }
            } catch (Exception ex2) {
                // Table historys temporary lag
            }
        } catch (Exception ignored) {
            // Silently ignore if connection temporary lag
        }
    }

    public void close() {
        running = false;
        if (this.thread_cal_time != null) this.thread_cal_time.interrupt();
        if (this.thread_save_data != null) this.thread_save_data.interrupt();
    }

    public void init() {
        //
        this.running = true;
        this.thread_cal_time = new Thread(() -> {
            DateTime now;
            int hour, min, sec, millis, dayOfYear;
            DateTimeZone vnZone = DateTimeZone.forID("Asia/Ho_Chi_Minh");
            while (this.running) {
                try {
                    now = DateTime.now(vnZone);
                    dayOfYear = now.getDayOfYear();
                    hour = now.getHourOfDay();
                    min = now.getMinuteOfHour();
                    sec = now.getSecondOfMinute();
                    millis = now.getMillisOfSecond();

                    // Periodically process pending IPC commands from Web Admin database
                    processPendingServerCommands();

                    // Periodically reload settings from database every 10s
                    if (System.currentTimeMillis() - this.lastSettingsReloadTime >= 10_000L) {
                        this.lastSettingsReloadTime = System.currentTimeMillis();
                        try {
                            Manager.gI().loadSettingsFromDb();
                        } catch (Exception ignored) {}
                    }

                    if (this.lastProcessedDay == -1) {
                        this.lastProcessedDay = dayOfYear;
                    }

                    // 1. Midnight daily reset: triggers once per day when day changes (00:00:00)
                    if (dayOfYear != this.lastProcessedDay) {
                        this.lastProcessedDay = dayOfYear;
                        Log.info("ServerEvent", "Starting 0:00 midnight daily reset sequence (Day: " + dayOfYear + ")...");

                        try {
                            for (map.Map map_all : map.MapManager.getInstance().getMaps()) {
                                for (Zone map : map_all.zones) {
                                    List<Player> players = new ArrayList<>(map.players);
                                    for (Player p : players) {
                                        if (p != null && !p.isClosed) {
                                            try {
                                                p.change_new_date();
                                            } catch (Exception e) {
                                                Log.error("ServerEvent", "Error updating player new date: " + p.name, e);
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error resetting map players new date", e);
                        }

                        try {
                            List<Zone> mapplus = Zone.get_map_plus();
                            for (Zone z : mapplus) {
                                List<Player> players = new ArrayList<>(z.players);
                                for (Player p0 : players) {
                                    if (p0 != null && !p0.isClosed) {
                                        try {
                                            p0.change_new_date();
                                        } catch (Exception e) {
                                            Log.error("ServerEvent", "Error updating plus map player new date: " + p0.name, e);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error resetting mapplus players new date", e);
                        }

                        try {
                            Clan.reset_day();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error resetting Clan day", e);
                        }

                        try {
                            TranChienKhongLo.LIST.clear();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error clearing TranChienKhongLo", e);
                        }

                        try {
                            if (ZUtil.is_DayofWeek(1)) {
                                TopGift.calculateWeeklyGifts();
                                try (java.sql.Connection connW = database.DbManager.gI().getConnect();
                                     java.sql.Statement stW = connW.createStatement()) {
                                    stW.executeUpdate("UPDATE `account` SET `tongnap2` = 0");
                                } catch (Exception e) {
                                    Log.error("ServerEvent", "Error resetting weekly recharge tongnap2 in DB", e);
                                }
                                for (network.Session ss : network.SessionManager.CLIENT_ENTRYS) {
                                    if (ss != null) {
                                        ss.tongnap2 = 0;
                                        if (ss.p != null) {
                                            ss.p.tongnap2 = 0;
                                            ss.p.resetWeeklyData(false);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error calculating weekly top gifts", e);
                        }

                        new Thread(() -> {
                            try {
                                SaveData.process();
                            } catch (Exception e) {
                                Log.error("ServerEvent", "Error saving data on midnight", e);
                            }
                            try {
                                historys.zLog.cleanOldLogs();
                            } catch (Exception e) {
                                Log.error("ServerEvent", "Error cleaning old logs", e);
                            }
                            try {
                                MailService.cleanExpiredMails();
                            } catch (Exception e) {
                                Log.error("ServerEvent", "Error cleaning expired mails", e);
                            }
                        }, "Midnight-Daily-Save").start();
                    }

                    // 2. Scheduled automatic maintenance: triggers at configured time(s) from settings
                    if (!ServerManager.gI().isBaoTri && isMaintenanceTime(hour, min, sec)) {
                        String slotKey = dayOfYear + "_" + hour + ":" + min + ":" + sec;
                        if (!slotKey.equals(this.lastTriggeredMaintenanceSlot)) {
                            this.lastTriggeredMaintenanceSlot = slotKey;
                            Log.warn("AutoMaintenance", "Triggered scheduled maintenance at "
                                    + String.format("%02d:%02d:%02d", hour, min, sec)
                                    + " (Config: " + Manager.gI().server_automaintenance + ")");
                            new Thread(() -> {
                                try {
                                    historys.zLog.cleanOldLogs();
                                } catch (Exception e) {
                                    Log.error("AutoMaintenance", "Error cleaning old logs", e);
                                }
                                try {
                                    MailService.cleanExpiredMails();
                                } catch (Exception e) {
                                    Log.error("AutoMaintenance", "Error cleaning expired mails", e);
                                }
                                SaveData.BaoTri();
                            }, "Auto-Maintenance-Thread").start();
                        }
                    }
                    if (event.EventManager.isActive(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025) && SuKienHalloween.gI().isTimeBoss(hour)) {
                        try {
                            if (min == 0 && sec == 0) {
                                SuKienHalloween.gI().createBoss();
                            } else if (min == 59 && sec == 59) {
                                SuKienHalloween.gI().closeBoss();
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error updating SuKienHalloween", e);
                        }
                    }
                    if (SuKienNoel.gI().isNoel()) {
                        try {
                            SuKienNoel.gI().update(hour, min, sec);
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error updating SuKienNoel", e);
                        }
                    }
                    
                    if (min == 1 && sec == 0 && (hour == 18)) {
                        try {
                            ChiemDao.hpTruChinh = ChiemDao.hpTruChinhOrg;
                            ChiemDao.clanTop1 = null;
                            ChiemDao.clanTop2 = null;
                            ChiemDao.clanTop3 = null;
                            ChiemDao.clanTop4 = null;
                            ChiemDao.clanTop5 = null;
                            ChiemDao.time1 = -1;
                            ChiemDao.time2 = -1;
                            ChiemDao.time3 = -1;
                            ChiemDao.time4 = -1;
                            ChiemDao.time5 = -1;
                            ChiemDao.check1 = false;
                            ChiemDao.check2 = false;
                            ChiemDao.check3 = false;
                            ChiemDao.check4 = false;
                            ChiemDao.check5 = false;
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error resetting ChiemDao", e);
                        }
                    }
                    if (sec % 2 == 0) {
                        try {
                            PvpBang.update();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error updating PvpBang", e);
                        }
                    }
                    if (sec % 10 == 0) {
                        try {
                            PvpBang.sendChatNpc();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error in PvpBang.sendChatNpc", e);
                        }
                    }
                    
                    if (ThuLinhBienKhoi.isOpen()) {
                        try {
                            ThuLinhBienKhoi.update(min, sec);
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error updating ThuLinhBienKhoi", e);
                        }
                    }
                    if (min == 1 && sec == 0 && (hour % 2 == 0)) {
                        try {
                            if (ChiemDao.clanTop1 != null && ChiemDao.clanTop1.members != null) {
                                for (int i = 0; i < ChiemDao.clanTop1.members.size(); i++) {
                                    Player p0 = Zone.get_player_by_name_allmap(ChiemDao.clanTop1.members.get(i).name);
                                    if (p0 != null) {
                                        p0.update_vang(500_000);
                                        p0.updateMoney();
                                    } else {
                                        ChiemDao.clanTop1.saveGift2(ChiemDao.clanTop1.members.get(i).name, 4, 0, 500_000);
                                    }
                                }
                                if (!ChiemDao.clanTop1.members.isEmpty()) {
                                    ClanChat chat = new ClanChat();
                                    chat.idMem = ChiemDao.clanTop1.members.get(0).id;
                                    chat.name = ChiemDao.clanTop1.members.get(0).name;
                                    chat.str = "nhận 500.000 Beri phần thưởng chiếm đảo 2h/lần";
                                    chat.typeChat = -3;
                                    ChiemDao.clanTop1.add_chat(chat);
                                    ChiemDao.clanTop1.send_chat(chat, null);
                                }
                            }
                            if (ChiemDao.clanTop2 != null && ChiemDao.clanTop2.members != null) {
                                for (int i = 0; i < ChiemDao.clanTop2.members.size(); i++) {
                                    Player p0 = Zone.get_player_by_name_allmap(ChiemDao.clanTop2.members.get(i).name);
                                    if (p0 != null) {
                                        p0.update_vang(600_000);
                                        p0.updateMoney();
                                    } else {
                                        ChiemDao.clanTop2.saveGift2(ChiemDao.clanTop2.members.get(i).name, 4, 0, 600_000);
                                    }
                                }
                                if (!ChiemDao.clanTop2.members.isEmpty()) {
                                    ClanChat chat = new ClanChat();
                                    chat.idMem = ChiemDao.clanTop2.members.get(0).id;
                                    chat.name = ChiemDao.clanTop2.members.get(0).name;
                                    chat.str = "nhận 600.000 Beri phần thưởng chiếm đảo 2h/lần";
                                    chat.typeChat = -3;
                                    ChiemDao.clanTop2.add_chat(chat);
                                    ChiemDao.clanTop2.send_chat(chat, null);
                                }
                            }
                            if (ChiemDao.clanTop3 != null && ChiemDao.clanTop3.members != null) {
                                for (int i = 0; i < ChiemDao.clanTop3.members.size(); i++) {
                                    Player p0 = Zone.get_player_by_name_allmap(ChiemDao.clanTop3.members.get(i).name);
                                    if (p0 != null) {
                                        p0.update_vang(1_000_000);
                                        p0.updateMoney();
                                    } else {
                                        ChiemDao.clanTop3.saveGift2(ChiemDao.clanTop3.members.get(i).name, 4, 0, 1_000_000);
                                    }
                                }
                                if (!ChiemDao.clanTop3.members.isEmpty()) {
                                    ClanChat chat = new ClanChat();
                                    chat.idMem = ChiemDao.clanTop3.members.get(0).id;
                                    chat.name = ChiemDao.clanTop3.members.get(0).name;
                                    chat.str = "nhận 1.000.000 Beri phần thưởng chiếm đảo 2h/lần";
                                    chat.typeChat = -3;
                                    ChiemDao.clanTop3.add_chat(chat);
                                    ChiemDao.clanTop3.send_chat(chat, null);
                                }
                            }
                            if (ChiemDao.clanTop4 != null && ChiemDao.clanTop4.members != null) {
                                for (int i = 0; i < ChiemDao.clanTop4.members.size(); i++) {
                                    Player p0 = Zone.get_player_by_name_allmap(ChiemDao.clanTop4.members.get(i).name);
                                    if (p0 != null) {
                                        p0.update_ngoc(30);
                                        p0.updateMoney();
                                    } else {
                                        ChiemDao.clanTop4.saveGift2(ChiemDao.clanTop4.members.get(i).name, 4, 1, 30);
                                    }
                                }
                                if (!ChiemDao.clanTop4.members.isEmpty()) {
                                    ClanChat chat = new ClanChat();
                                    chat.idMem = ChiemDao.clanTop4.members.get(0).id;
                                    chat.name = ChiemDao.clanTop4.members.get(0).name;
                                    chat.str = "nhận 30 Ruby phần thưởng chiếm đảo 2h/lần";
                                    chat.typeChat = -3;
                                    ChiemDao.clanTop4.add_chat(chat);
                                    ChiemDao.clanTop4.send_chat(chat, null);
                                }
                            }
                            if (ChiemDao.clanTop5 != null && ChiemDao.clanTop5.members != null) {
                                for (int i = 0; i < ChiemDao.clanTop5.members.size(); i++) {
                                    Player p0 = Zone.get_player_by_name_allmap(ChiemDao.clanTop5.members.get(i).name);
                                    if (p0 != null) {
                                        p0.update_ngoc(50);
                                        p0.update_vang(900_000);
                                        p0.updateMoney();
                                    } else {
                                        ChiemDao.clanTop5.saveGift2(ChiemDao.clanTop5.members.get(i).name, 4, 0, 900_000);
                                        ChiemDao.clanTop5.saveGift2(ChiemDao.clanTop5.members.get(i).name, 4, 1, 50);
                                    }
                                }
                                if (!ChiemDao.clanTop5.members.isEmpty()) {
                                    ClanChat chat = new ClanChat();
                                    chat.idMem = ChiemDao.clanTop5.members.get(0).id;
                                    chat.name = ChiemDao.clanTop5.members.get(0).name;
                                    chat.str = "nhận 50 Ruby và 900.000 Beri phần thưởng chiếm đảo 2h/lần";
                                    chat.typeChat = -3;
                                    ChiemDao.clanTop5.add_chat(chat);
                                    ChiemDao.clanTop5.send_chat(chat, null);
                                }
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error distributing ChiemDao periodic reward", e);
                        }
                    }
                    try {
                        if (hour == 19 && min == 0 && sec == 0) {
                            WorldWar.startRegister();
                        }
                        if (hour == 19 && min == 15 && sec == 0) {
                            WorldWar.startWar();
                        }
                        if (hour == 21 && min == 0 && sec == 0) {
                            WorldWar.close();
                        }
                        if (sec == 0) {
                            WorldWar.checkAndRestoreState(hour, min);
                        }
                    } catch (Exception e) {
                        Log.error("ServerEvent", "Error updating WorldWar", e);
                    }
                    try {
                        if ((min == 0 || min == 30) && sec == 0) {
                            SuperBossManager.createSuperBoss();
                        }
                    } catch (Exception e) {
                        Log.error("ServerEvent", "Error updating SuperBossManager", e);
                    }
                    try {
                        if (ChiemDao.isOpen() && hour == 19 && min == 0 && sec == 1) {
                            Manager.gI().chatKTG(0, "Thời gian chiếm đảo bắt đầu", 5);
                        }
                        if ((ZUtil.is_DayofWeek(2) || ZUtil.is_DayofWeek(4) || ZUtil.is_DayofWeek(6)) && hour == 20 && min == 0 && sec == 1) {
                            String notice = "Thời gian chiếm đảo kết thúc";
                            if (ChiemDao.clanTop1 != null) {
                                notice += ("\n" + ChiemDao.clanTop1.name + " chiếm Đảo Tiền bạc sơ cấp");
                            }
                            if (ChiemDao.clanTop2 != null) {
                                notice += ("\n" + ChiemDao.clanTop2.name + " chiếm Đảo Châu báu sơ cấp");
                            }
                            if (ChiemDao.clanTop3 != null) {
                                notice += ("\n" + ChiemDao.clanTop3.name + " chiếm Đảo Danh vọng");
                            }
                            if (ChiemDao.clanTop4 != null) {
                                notice += ("\n" + ChiemDao.clanTop4.name + " chiếm Đảo Tiền bạc trung cấp");
                            }
                            if (ChiemDao.clanTop5 != null) {
                                notice += ("\n" + ChiemDao.clanTop5.name + " chiếm Đảo Châu báu trung cấp");
                            }
                            Manager.gI().chatKTG(0, notice, 5);
                        }
                    } catch (Exception e) {
                        Log.error("ServerEvent", "Error in ChiemDao notification", e);
                    }
                    try {
                        if (hour == 23 && min == 0 && sec == 0) {
                            BossPica.start();
                        }
                    } catch (Exception e) {
                        Log.error("ServerEvent", "Error starting BossPica", e);
                    }
                    // Reload config mỗi phút
                    if (sec == 0) {
                        try {
                            Manager.gI().loadSettingsFromDb();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error reloading Manager settings from DB", e);
                        }
                        try {
                            Manager.gI().autoDetectAndUpdatePhase();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error auto detecting server phase", e);
                        }
                        try {
                            EventManager.gI().reloadConfigs();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error reloading EventManager configs", e);
                        }

                        // Tự động kiểm tra và trao quà Đua Top Cao Thủ vào Hộp Thư
                        try {
                            boolean isSundayEvening = ZUtil.is_DayofWeek(7) && hour >= 18;
                            if (Manager.gI().hasTopCaoThuEnded() || (isSundayEvening && (Manager.gI().isBetaPhase() || Manager.gI().isTopRacingRunning()))) {
                                rank.TopCaoThu.gI().distributeAutoRewardsToMail();
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error auto-distributing TopCaoThu rewards", e);
                        }

                        // Tự động kiểm tra và trao quà Đua Top PvP vào Hộp Thư
                        try {
                            if (Manager.gI().hasTopPvpEnded()) {
                                rank.TopPVP.gI().distributeAutoRewardsToMail();
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error auto-distributing TopPVP rewards", e);
                        }

                        // Tự động kiểm tra và trao quà Đua Top Lệnh Truy Nã vào Hộp Thư
                        try {
                            if (Manager.gI().hasTopWantedEnded()) {
                                rank.TopWanted.gI().distributeAutoRewardsToMail();
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error auto-distributing TopWanted rewards", e);
                        }

                        // Tự động kiểm tra và trao quà Đua Top Sự Kiện vào Hộp Thư
                        try {
                            for (event.Event ev : EventManager.gI().getActiveEventsList()) {
                                if (ev.isEventEnded() && ev.bxhSubTypes != null && ev.bxhSubTypes.length > 0) {
                                    String evSeason = ev.getSeasonKey();
                                    String evLogKey = "TOP_DISTRIBUTED_EVENT_" + ev.id + "_" + evSeason;
                                    if (!historys.HistoryManager.hasSystemLog(evLogKey)) {
                                        for (int subType : ev.bxhSubTypes) {
                                            zabstracts.AbsRanked bxh = zabstracts.AbsRanked.getBySubType(subType);
                                            if (bxh != null) {
                                                bxh.update();
                                                bxh.sendRewardToAllTop();
                                            }
                                        }
                                        org.json.simple.JSONObject logData = new org.json.simple.JSONObject();
                                        logData.put("event_id", ev.id);
                                        logData.put("season", evSeason);
                                        logData.put("time", System.currentTimeMillis());
                                        historys.HistoryManager.saveSystemLog(evLogKey, logData.toJSONString());
                                        Manager.gI().chatKTG(0, "Sự kiện Đua Top [" + ev.name + "] đã kết thúc! Phần thưởng đã được gửi tự động vào Hộp Thư!", 5);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error auto-distributing Event Top rewards", e);
                        }
                    }
                    // Dispatch update tới tất cả active events
                    try {
                        EventManager.dispatchUpdate(hour, min, sec);
                    } catch (Exception e) {
                        Log.error("ServerEvent", "Error in EventManager.dispatchUpdate", e);
                    }
                    // Chiếm đảo: kiểm tra conquest notifications (per minute)
                    if (sec == 0) {
                        try {
                            if (!ChiemDao.check1 && ChiemDao.clanTop1 != null && ChiemDao.clanTop1.members != null && ChiemDao.time1 < System.currentTimeMillis()) {
                                Manager.gI().chatKTG(0, ChiemDao.clanTop1.name + " chiếm Đảo Tiền bạc sơ cấp", 5);
                                if (!ChiemDao.clanTop1.members.isEmpty()) {
                                    ClanChat chat = new ClanChat();
                                    chat.idMem = ChiemDao.clanTop1.members.get(0).id;
                                    chat.name = ChiemDao.clanTop1.members.get(0).name;
                                    chat.str = "chiếm thành công Đảo Tiền bạc sơ cấp";
                                    chat.typeChat = -3;
                                    ChiemDao.clanTop1.add_chat(chat);
                                    ChiemDao.clanTop1.send_chat(chat, null);
                                }
                                ChiemDao.check1 = true;
                            }
                            if (!ChiemDao.check2 && ChiemDao.clanTop2 != null && ChiemDao.clanTop2.members != null && ChiemDao.time2 < System.currentTimeMillis()) {
                                Manager.gI().chatKTG(0, ChiemDao.clanTop2.name + " chiếm Đảo Châu báu sơ cấp", 5);
                                if (!ChiemDao.clanTop2.members.isEmpty()) {
                                    ClanChat chat = new ClanChat();
                                    chat.idMem = ChiemDao.clanTop2.members.get(0).id;
                                    chat.name = ChiemDao.clanTop2.members.get(0).name;
                                    chat.str = "chiếm thành công Đảo Châu báu sơ cấp";
                                    chat.typeChat = -3;
                                    ChiemDao.clanTop2.add_chat(chat);
                                    ChiemDao.clanTop2.send_chat(chat, null);
                                }
                                ChiemDao.check2 = true;
                            }
                            if (!ChiemDao.check3 && ChiemDao.clanTop3 != null && ChiemDao.clanTop3.members != null && ChiemDao.time3 < System.currentTimeMillis()) {
                                Manager.gI().chatKTG(0, ChiemDao.clanTop3.name + " chiếm Đảo Danh vọng", 5);
                                if (!ChiemDao.clanTop3.members.isEmpty()) {
                                    ClanChat chat = new ClanChat();
                                    chat.idMem = ChiemDao.clanTop3.members.get(0).id;
                                    chat.name = ChiemDao.clanTop3.members.get(0).name;
                                    chat.str = "chiếm thành công Đảo Danh vọng";
                                    chat.typeChat = -3;
                                    ChiemDao.clanTop3.add_chat(chat);
                                    ChiemDao.clanTop3.send_chat(chat, null);
                                }
                                ChiemDao.check3 = true;
                            }
                            if (!ChiemDao.check4 && ChiemDao.clanTop4 != null && ChiemDao.clanTop4.members != null && ChiemDao.time4 < System.currentTimeMillis()) {
                                Manager.gI().chatKTG(0, ChiemDao.clanTop4.name + " chiếm Đảo Tiền bạc trung cấp", 5);
                                if (!ChiemDao.clanTop4.members.isEmpty()) {
                                    ClanChat chat = new ClanChat();
                                    chat.idMem = ChiemDao.clanTop4.members.get(0).id;
                                    chat.name = ChiemDao.clanTop4.members.get(0).name;
                                    chat.str = "chiếm thành công Đảo Tiền bạc trung cấp";
                                    chat.typeChat = -4;
                                    ChiemDao.clanTop4.add_chat(chat);
                                    ChiemDao.clanTop4.send_chat(chat, null);
                                }
                                ChiemDao.check4 = true;
                            }
                            if (!ChiemDao.check5 && ChiemDao.clanTop5 != null && ChiemDao.clanTop5.members != null && ChiemDao.time5 < System.currentTimeMillis()) {
                                Manager.gI().chatKTG(0, ChiemDao.clanTop5.name + " chiếm Đảo Châu báu trung cấp", 5);
                                if (!ChiemDao.clanTop5.members.isEmpty()) {
                                    ClanChat chat = new ClanChat();
                                    chat.idMem = ChiemDao.clanTop5.members.get(0).id;
                                    chat.name = ChiemDao.clanTop5.members.get(0).name;
                                    chat.str = "chiếm thành công Đảo Châu báu trung cấp";
                                    chat.typeChat = -5;
                                    ChiemDao.clanTop5.add_chat(chat);
                                    ChiemDao.clanTop5.send_chat(chat, null);
                                }
                                ChiemDao.check5 = true;
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error in ChiemDao periodic check", e);
                        }
                    }
                    if (true) { // update eff player every second
                        try {
                            for (Player p0 : SessionManager.PLAYERS_MAP.values()) {
                                if (p0 != null && !p0.isClosed) {
                                    try {
                                        p0.update_eff();
                                    } catch (Exception ignored) {}
                                }
                            }
                        } catch (Exception ignored) {}

                        try {
                            Manager.gI().TaiXiu().upTime();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error in TaiXiu.upTime", e);
                        }
                        try {
                            if (DauGia.ENTRY != null) {
                                DauGia.update();
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error in DauGia.update", e);
                        }
                        try {
                            clan.Clan.endBaoVePhaoDai();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error in Clan.endBaoVePhaoDai", e);
                        }
                        // DauTruongTuDo lifecycle & top rewards handled exclusively by activities.TimedDungeonManager
                        try {
                            BotKhoBau.updateSpawning();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error in BotKhoBau.updateSpawning", e);
                        }
                        if (sec % 10 == 0) {
                            try {
                                model.DeTu.updateWildSpawning();
                            } catch (Exception e) {
                                Log.error("ServerEvent", "Error in DeTu.updateWildSpawning", e);
                            }
                        }
                    }
                    // BossTheGioi & DauTruongTuDo lifecycle managed exclusively by activities.TimedDungeonManager
                    
                    if (sec % 5 == 0) { // clan operations every 5s
                        try {
                            SessionManager.update();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error in SessionManager.update", e);
                        }
                        try {
                            clan.Clan.findBaoVePhaoDai();
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error in Clan.findBaoVePhaoDai", e);
                        }
                        try {
                            if (TranChienKhongLo.isOpen()) {
                                TranChienKhongLo.processQueue();
                            }
                        } catch (Exception e) {
                            Log.error("ServerEvent", "Error in TranChienKhongLo.processQueue", e);
                        }
                    }
                    //
                    long nowMillis = System.currentTimeMillis();
                    long nextSecond = ((nowMillis / 1000L) + 1L) * 1000L;
                    long time_sleep = nextSecond - System.currentTimeMillis();
                    if (time_sleep > 0) {
                        Thread.sleep(time_sleep);
                    }
                } 
                catch (InterruptedException e) {
                } 
                catch (Exception e) {
                    Log.error("ServerEvent", "Exception in server time update loop", e);
                }
            }
        }, "xx4");
        this.thread_cal_time.start();
        //
        this.thread_save_data = new Thread(() -> {
            while (this.running) {
                try {
                    Thread.sleep(50_000L);
                    if(ServerManager.gI().isBaoTri) {
                        continue;
                    }
                    SaveData.process();
                } 
                catch (InterruptedException e) {
                    break;
                }
                catch (Exception e) {
                    if (this.running) {
                        e.printStackTrace();
                        System.err.println("err thread save data");
                    }
                }
            }
        });
        this.thread_save_data.start();
        BotKhoBau.initClass();
        BotTruyNa.initClass();
        BossPica.initialize();
    }
}
