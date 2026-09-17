package core;

import model.Player;
import model.Quest;
import ability.AbilityTemplateManager;
import model.Pet;
import mob.Mob;
import skill.Skill_info;
import skill.Skill_Template;
import boss.BossReward;
import effect.DataEffect;
import itemz.Rebuild_Item;
import clan.ClanMember;
import clan.ClanHanhTrinhIcon;
import clan.Clan;
import model.DauGia;
import boss.SuperBossManager;
import model.VongQuay;
import map.zones.Red_Line;
import itemz.UpgradeItem;
import map.zones.ChiemDao;
import map.zones.ThuLinhBienKhoi;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import activities.*;
import map.*;
import map.Zone;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import database.DbManager;
import network.Message;
import network.Session;
import template.*;

public class Manager {

    private static Manager instance;
    public static String[] NAME_ITEM_SELL_TEMP = new String[] { "Shop Trang Bị Võ Sĩ", "Shop Trang Bị Kiếm Khách",
            "Shop Trang Bị Đầu Bếp", "Shop Trang Bị Hoa Tiêu", "Shop Trang Bị Xạ Thủ" };
    public static List<ShopLimitItemTemplate> shoptichluy_entry = new ArrayList<>();
    public boolean debug;
    public String mysql_host = "localhost";
    public String mysql_database = "haitacz";
    public String mysql_user = "root";
    public String mysql_pass = "";
    public int server_port;
    public int exp;
    public boolean server_admin;
    public int cache_max_ram_entries = -1;
    public boolean cache_enable_ssd = true;
    public boolean auto_restart = true;
    public boolean auto_baotri_midnight = true;
    public volatile String server_automaintenance = "0:00:00";
    public volatile boolean running = true;
    private Thread timedDungeonThread;
    public final AtomicInteger index_mob = new AtomicInteger(0);
    private TaiXiu tx;

    public static Manager gI() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }

    public int ZEVENT_ID;

    public void init() {
        index_mob.set(1);
        // Invalidate all template caches — ensures skill, mobs, maps and all critical
        // game-data tables are always reloaded fresh from MySQL on every server start.
        database.TemplateCache.invalidateAll();
        try {
            load_config();
        } catch (Exception e) {
            System.out.println("config load err!");
            e.printStackTrace();
            System.exit(0);
        }
        loadSettingsFromDb();
        load_database();
        loadSettingsFromDb();
        load_head_from_itemhair();
        start_service();
    }

    public String server_status = "OPEN";
    public volatile String server_open_time = "2026-09-06 14:00:00";
    public volatile String server_test_start = "2026-09-10 14:00:00";
    public volatile String server_test_end = "2026-09-14 12:00:00";
    public volatile String server_beta_start = "2026-09-15 14:00:00";
    public volatile String server_beta_end = "2026-09-21 18:00:00";
    public volatile String server_open_date = "2026-09-22 14:00:00";
    public volatile String top_caothu_reward_time = "18:00:00";
    public volatile boolean lock_coin_exchange_racing = true;

    public volatile String top_cao_thu_start = "2026-09-06 14:00:00";
    public volatile String top_cao_thu_end = "2026-09-13 23:59:59";
    public volatile int top_cao_thu_days = 7;
    public volatile String top_cao_thu_season = "OPEN_MUA_1";

    public volatile String top_pvp_start = "2026-09-06 14:00:00";
    public volatile String top_pvp_end = "2026-09-13 23:59:59";
    public volatile int top_pvp_days = 7;
    public volatile String top_pvp_season = "OPEN_MUA_1";

    public volatile String top_wanted_start = "2026-09-06 14:00:00";
    public volatile String top_wanted_end = "2026-09-13 23:59:59";
    public volatile int top_wanted_days = 7;
    public volatile String top_wanted_season = "OPEN_MUA_1";

    public volatile int trade_min_online_hours = 48;
    public volatile int server_open_status = 0;
    public volatile int gold_rate = 1;
    public volatile int max_players = 1000;
    public volatile boolean is_double_xp = false;
    public volatile int vip_exp_bonus = 10;
    public volatile int chat_ktg_cooldown = 30;

    public String getServerStatus() {
        if (this.server_status == null) return "OPEN";
        String s = this.server_status.trim();
        if (s.contains("\n")) s = s.split("\n")[0].trim();
        if (s.contains("\r")) s = s.split("\r")[0].trim();
        s = s.toUpperCase();
        if ("TEST".equals(s) || s.startsWith("TEST")) return "TEST";
        if ("BETA".equals(s) || s.startsWith("BETA")) return "BETA";
        return "OPEN";
    }

    public boolean isTestMode() {
        if (debug) return true;
        return "TEST".equals(getServerStatus());
    }

    public boolean isTestPhase() {
        if (debug) return true;
        return "TEST".equals(getServerStatus());
    }

    public boolean isBetaPhase() {
        return "BETA".equals(getServerStatus());
    }

    public boolean isOpenBetaMode() {
        return false;
    }

    public boolean isOpenMode() {
        return "OPEN".equals(getServerStatus());
    }

    /**
     * Kiểm tra xem hiện tại có đang trong mùa giải đua Top Cao Thủ hay không
     */
    public boolean isTopRacingRunning() {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long now = System.currentTimeMillis();
            if (this.top_cao_thu_start != null && this.top_cao_thu_end != null) {
                long tStart = sdf.parse(this.top_cao_thu_start).getTime();
                long tEnd = sdf.parse(this.top_cao_thu_end).getTime();
                return now >= tStart && now <= tEnd;
            }
        } catch (Exception ignored) {}
        return isBetaPhase();
    }

    /**
     * Tự động đồng bộ và bảo vệ trạng thái máy chủ (TEST, BETA, OPEN) do Admin thiết lập
     */
    public synchronized void autoDetectAndUpdatePhase() {
        // Tôn trọng 100% cấu hình trạng thái do Admin chỉ định (TEST, BETA, OPEN) trên Web/Database.
        // Không tự ý so sánh ngày giờ cũ để đè ngược lại CSDL, tránh xung đột cấu hình.
    }

    public boolean isClosedMode() {
        String s = getServerStatus();
        return s.equals("CLOSED") || s.equals("CLOSE") || s.startsWith("CLOSE");
    }

    // --- TOP CAO THỦ ---
    public long getTopCaoThuStartTime() {
        try {
            String timeStr = (top_cao_thu_start != null && !top_cao_thu_start.trim().isEmpty())
                    ? top_cao_thu_start.trim()
                    : server_open_time;
            java.util.Date d = event.Event.parseEventDate(timeStr, true);
            if (d != null) return d.getTime();
        } catch (Exception ignored) {}
        return 0L;
    }

    public long getTopCaoThuEndTime() {
        try {
            if (top_cao_thu_end != null && !top_cao_thu_end.trim().isEmpty()) {
                java.util.Date d = event.Event.parseEventDate(top_cao_thu_end.trim(), false);
                if (d != null) return d.getTime();
            }
            long start = getTopCaoThuStartTime();
            if (start > 0) {
                return start + ((long) top_cao_thu_days * 24L * 60L * 60L * 1000L);
            }
        } catch (Exception ignored) {}
        return 0L;
    }

    public boolean isTopCaoThuOpenWeek() {
        try {
            long now = System.currentTimeMillis();
            long start = getTopCaoThuStartTime();
            long end = getTopCaoThuEndTime();
            if (start > 0 && end > 0) {
                return now >= start && now <= end;
            }
            // Fallback: nếu chưa cấu hình chi tiết, lấy 7 ngày kể từ server_open_time
            if (server_open_time != null && !server_open_time.trim().isEmpty()) {
                java.util.Date d = event.Event.parseEventDate(server_open_time.trim(), true);
                if (d != null) {
                    long openTs = d.getTime();
                    long endTs = openTs + (7L * 24L * 60L * 60L * 1000L);
                    return now >= openTs && now <= endTs;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    public boolean isDuaTopCaoThu() {
        return isTopCaoThuOpenWeek();
    }

    public boolean hasTopCaoThuEnded() {
        try {
            long now = System.currentTimeMillis();
            long end = getTopCaoThuEndTime();
            if (end > 0) {
                return now > end;
            }
        } catch (Exception ignored) {}
        return false;
    }

    public String getTopCaoThuSeasonKey() {
        if (top_cao_thu_season != null && !top_cao_thu_season.trim().isEmpty()) {
            return top_cao_thu_season.trim();
        }
        return "OPEN_MUA_1";
    }

    // --- TOP PVP (THÁCH ĐẤU) ---
    public long getTopPvpStartTime() {
        try {
            String timeStr = (top_pvp_start != null && !top_pvp_start.trim().isEmpty())
                    ? top_pvp_start.trim()
                    : server_open_time;
            java.util.Date d = event.Event.parseEventDate(timeStr, true);
            if (d != null) return d.getTime();
        } catch (Exception ignored) {}
        return 0L;
    }

    public long getTopPvpEndTime() {
        try {
            if (top_pvp_end != null && !top_pvp_end.trim().isEmpty()) {
                java.util.Date d = event.Event.parseEventDate(top_pvp_end.trim(), false);
                if (d != null) return d.getTime();
            }
            long start = getTopPvpStartTime();
            if (start > 0) {
                return start + ((long) top_pvp_days * 24L * 60L * 60L * 1000L);
            }
        } catch (Exception ignored) {}
        return 0L;
    }

    public boolean isTopPvpRunning() {
        try {
            long now = System.currentTimeMillis();
            long start = getTopPvpStartTime();
            long end = getTopPvpEndTime();
            if (start > 0 && end > 0) {
                return now >= start && now <= end;
            }
            if (server_open_time != null && !server_open_time.trim().isEmpty()) {
                java.util.Date d = event.Event.parseEventDate(server_open_time.trim(), true);
                if (d != null) {
                    long openTs = d.getTime();
                    long endTs = openTs + (7L * 24L * 60L * 60L * 1000L);
                    return now >= openTs && now <= endTs;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    public boolean isDuaTopPvp() {
        return isTopPvpRunning();
    }

    public boolean hasTopPvpEnded() {
        try {
            long now = System.currentTimeMillis();
            long end = getTopPvpEndTime();
            if (end > 0) {
                return now > end;
            }
        } catch (Exception ignored) {}
        return false;
    }

    public String getTopPvpSeasonKey() {
        if (top_pvp_season != null && !top_pvp_season.trim().isEmpty()) {
            return top_pvp_season.trim();
        }
        return "OPEN_MUA_1";
    }

    // --- TOP WANTED (TRUY NÃ) ---
    public long getTopWantedStartTime() {
        try {
            String timeStr = (top_wanted_start != null && !top_wanted_start.trim().isEmpty())
                    ? top_wanted_start.trim()
                    : server_open_time;
            java.util.Date d = event.Event.parseEventDate(timeStr, true);
            if (d != null) return d.getTime();
        } catch (Exception ignored) {}
        return 0L;
    }

    public long getTopWantedEndTime() {
        try {
            if (top_wanted_end != null && !top_wanted_end.trim().isEmpty()) {
                java.util.Date d = event.Event.parseEventDate(top_wanted_end.trim(), false);
                if (d != null) return d.getTime();
            }
            long start = getTopWantedStartTime();
            if (start > 0) {
                return start + ((long) top_wanted_days * 24L * 60L * 60L * 1000L);
            }
        } catch (Exception ignored) {}
        return 0L;
    }

    public boolean isTopWantedRunning() {
        try {
            long now = System.currentTimeMillis();
            long start = getTopWantedStartTime();
            long end = getTopWantedEndTime();
            if (start > 0 && end > 0) {
                return now >= start && now <= end;
            }
            if (server_open_time != null && !server_open_time.trim().isEmpty()) {
                java.util.Date d = event.Event.parseEventDate(server_open_time.trim(), true);
                if (d != null) {
                    long openTs = d.getTime();
                    long endTs = openTs + (7L * 24L * 60L * 60L * 1000L);
                    return now >= openTs && now <= endTs;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    public boolean isDuaTopWanted() {
        return isTopWantedRunning();
    }

    public boolean hasTopWantedEnded() {
        try {
            long now = System.currentTimeMillis();
            long end = getTopWantedEndTime();
            if (end > 0) {
                return now > end;
            }
        } catch (Exception ignored) {}
        return false;
    }

    public String getTopWantedSeasonKey() {
        if (top_wanted_season != null && !top_wanted_season.trim().isEmpty()) {
            return top_wanted_season.trim();
        }
        return "OPEN_MUA_1";
    }

    public boolean isAnyTopOpenRunning() {
        return isTopCaoThuOpenWeek() || isTopPvpRunning() || isTopWantedRunning();
    }

    private void parseTopConfigMultiline(String val, java.util.function.Consumer<String[]> consumer) {
        if (val == null || val.trim().isEmpty()) return;
        String[] lines = val.split("\\r?\\n");
        String start = null;
        String end = null;
        String season = null;
        int idx = 0;
        for (String line : lines) {
            String clean = line.trim();
            if (clean.isEmpty() || clean.startsWith("#")) continue;
            if (idx == 0) start = clean;
            else if (idx == 1) end = clean;
            else if (idx == 2) season = clean;
            idx++;
        }
        consumer.accept(new String[]{start, end, season});
    }

    public void loadSettingsFromDb() {
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn == null) return;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT `id`, `key_name`, `value` FROM `settings`")) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String key = rs.getString("key_name");
                    String val = rs.getString("value");
                    if (key == null) continue;

                    if ("server_automaintenance".equalsIgnoreCase(key) || id == 23) {
                        this.server_automaintenance = (val != null && !val.trim().isEmpty()) ? val.trim() : "0:00:00";
                    } else if ("server_status".equalsIgnoreCase(key) || "status".equalsIgnoreCase(key) || id == 24) {
                        this.server_status = (val != null && !val.trim().isEmpty()) ? val.trim() : "OPEN";
                    } else if ("exp_rate".equalsIgnoreCase(key)) {
                        try {
                            this.exp = Integer.parseInt(val.trim());
                        } catch (Exception ignored) {}
                    } else if ("server_open_time".equalsIgnoreCase(key) || id == 1188) {
                        if (val != null && !val.trim().isEmpty()) {
                            this.server_open_time = val.trim();
                        }
                    } else if ("top_caothu_config".equalsIgnoreCase(key) || "top_cao_thu_config".equalsIgnoreCase(key) || id == 26) {
                        parseTopConfigMultiline(val, res -> {
                            if (res[0] != null && !res[0].isEmpty()) this.top_cao_thu_start = res[0];
                            if (res[1] != null && !res[1].isEmpty()) this.top_cao_thu_end = res[1];
                            if (res[2] != null && !res[2].isEmpty()) this.top_cao_thu_season = res[2];
                        });
                    } else if ("top_pvp_config".equalsIgnoreCase(key) || id == 27) {
                        parseTopConfigMultiline(val, res -> {
                            if (res[0] != null && !res[0].isEmpty()) this.top_pvp_start = res[0];
                            if (res[1] != null && !res[1].isEmpty()) this.top_pvp_end = res[1];
                            if (res[2] != null && !res[2].isEmpty()) this.top_pvp_season = res[2];
                        });
                    } else if ("top_wanted_config".equalsIgnoreCase(key) || "top_truyna_config".equalsIgnoreCase(key) || id == 28) {
                        parseTopConfigMultiline(val, res -> {
                            if (res[0] != null && !res[0].isEmpty()) this.top_wanted_start = res[0];
                            if (res[1] != null && !res[1].isEmpty()) this.top_wanted_end = res[1];
                            if (res[2] != null && !res[2].isEmpty()) this.top_wanted_season = res[2];
                        });
                    } else if ("top_cao_thu_start".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) {
                            this.top_cao_thu_start = val.trim();
                        }
                    } else if ("top_cao_thu_end".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) {
                            this.top_cao_thu_end = val.trim();
                        }
                    } else if ("top_cao_thu_season".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) {
                            this.top_cao_thu_season = val.trim();
                        }
                    } else if ("top_cao_thu_days".equalsIgnoreCase(key)) {
                        try {
                            this.top_cao_thu_days = Integer.parseInt(val.trim());
                        } catch (Exception ignored) {}
                    } else if ("top_pvp_start".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) {
                            this.top_pvp_start = val.trim();
                        }
                    } else if ("top_pvp_end".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) {
                            this.top_pvp_end = val.trim();
                        }
                    } else if ("top_pvp_season".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) {
                            this.top_pvp_season = val.trim();
                        }
                    } else if ("top_wanted_start".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) {
                            this.top_wanted_start = val.trim();
                        }
                    } else if ("top_wanted_end".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) {
                            this.top_wanted_end = val.trim();
                        }
                    } else if ("top_wanted_season".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) {
                            this.top_wanted_season = val.trim();
                        }
                    } else if ("trade_min_online_hours".equalsIgnoreCase(key) || "trade_online_hours".equalsIgnoreCase(key)) {
                        try {
                            this.trade_min_online_hours = Integer.parseInt(val.trim());
                        } catch (Exception ignored) {}
                    } else if ("gold_rate".equalsIgnoreCase(key) || id == 16) {
                        try {
                            this.gold_rate = Integer.parseInt(val.trim());
                        } catch (Exception ignored) {}
                    } else if ("max_players".equalsIgnoreCase(key) || id == 17) {
                        try {
                            this.max_players = Integer.parseInt(val.trim());
                        } catch (Exception ignored) {}
                    } else if ("is_double_xp".equalsIgnoreCase(key) || id == 18) {
                        try {
                            this.is_double_xp = "true".equalsIgnoreCase(val.trim()) || "1".equals(val.trim());
                        } catch (Exception ignored) {}
                    } else if ("vip_exp_bonus".equalsIgnoreCase(key) || id == 19) {
                        try {
                            this.vip_exp_bonus = Integer.parseInt(val.trim());
                        } catch (Exception ignored) {}
                    } else if ("chat_ktg_cooldown".equalsIgnoreCase(key) || id == 20) {
                        try {
                            this.chat_ktg_cooldown = Integer.parseInt(val.trim());
                        } catch (Exception ignored) {}
                    } else if ("server_open_status".equalsIgnoreCase(key) || id == 1186) {
                        try {
                            this.server_open_status = Integer.parseInt(val.trim());
                        } catch (Exception ignored) {}
                    } else if ("server_phase_config".equalsIgnoreCase(key)) {
                        try {
                            Object obj = JSONValue.parse(val);
                            if (obj instanceof JSONObject) {
                                JSONObject json = (JSONObject) obj;
                                String phStatus = null;
                                if (json.get("status") != null) phStatus = json.get("status").toString().trim();
                                else if (json.get("server_status") != null) phStatus = json.get("server_status").toString().trim();
                                if (phStatus != null && !phStatus.isEmpty()) {
                                    if (!"TEST".equalsIgnoreCase(this.server_status) || "TEST".equalsIgnoreCase(phStatus)) {
                                        this.server_status = phStatus;
                                    }
                                }

                                if (json.get("automaintenance") != null) this.server_automaintenance = json.get("automaintenance").toString().trim();
                                else if (json.get("server_automaintenance") != null) this.server_automaintenance = json.get("server_automaintenance").toString().trim();

                                if (json.get("server_open_time") != null) this.server_open_time = json.get("server_open_time").toString().trim();

                                if (json.get("test_start") != null) this.server_test_start = json.get("test_start").toString().trim();
                                else if (json.get("server_test_start") != null) this.server_test_start = json.get("server_test_start").toString().trim();

                                if (json.get("test_end") != null) this.server_test_end = json.get("test_end").toString().trim();
                                else if (json.get("server_test_end") != null) this.server_test_end = json.get("server_test_end").toString().trim();

                                if (json.get("beta_start") != null) this.server_beta_start = json.get("beta_start").toString().trim();
                                else if (json.get("server_beta_start") != null) this.server_beta_start = json.get("server_beta_start").toString().trim();

                                if (json.get("beta_end") != null) this.server_beta_end = json.get("beta_end").toString().trim();
                                else if (json.get("server_beta_end") != null) this.server_beta_end = json.get("server_beta_end").toString().trim();

                                if (json.get("open_date") != null) this.server_open_date = json.get("open_date").toString().trim();
                                else if (json.get("server_open_date") != null) this.server_open_date = json.get("server_open_date").toString().trim();

                                if (json.get("reward_time") != null) this.top_caothu_reward_time = json.get("reward_time").toString().trim();
                                else if (json.get("top_caothu_reward_time") != null) this.top_caothu_reward_time = json.get("top_caothu_reward_time").toString().trim();

                                if (json.get("lock_coin_exchange_racing") != null) {
                                    this.lock_coin_exchange_racing = "true".equalsIgnoreCase(json.get("lock_coin_exchange_racing").toString().trim())
                                            || "1".equals(json.get("lock_coin_exchange_racing").toString().trim());
                                }

                                Object tcObj = json.get("top_caothu");
                                if (tcObj instanceof JSONObject) {
                                    JSONObject tc = (JSONObject) tcObj;
                                    if (tc.get("start") != null) this.top_cao_thu_start = tc.get("start").toString().trim();
                                    if (tc.get("end") != null) this.top_cao_thu_end = tc.get("end").toString().trim();
                                    if (tc.get("season") != null) this.top_cao_thu_season = tc.get("season").toString().trim();
                                    if (tc.get("reward_time") != null) this.top_caothu_reward_time = tc.get("reward_time").toString().trim();
                                }
                                if (json.get("top_caothu_start") != null) this.top_cao_thu_start = json.get("top_caothu_start").toString().trim();
                                if (json.get("top_caothu_end") != null) this.top_cao_thu_end = json.get("top_caothu_end").toString().trim();
                                if (json.get("top_caothu_season") != null) this.top_cao_thu_season = json.get("top_caothu_season").toString().trim();

                                Object tpObj = json.get("top_pvp");
                                if (tpObj instanceof JSONObject) {
                                    JSONObject tp = (JSONObject) tpObj;
                                    if (tp.get("start") != null) this.top_pvp_start = tp.get("start").toString().trim();
                                    if (tp.get("end") != null) this.top_pvp_end = tp.get("end").toString().trim();
                                    if (tp.get("season") != null) this.top_pvp_season = tp.get("season").toString().trim();
                                }
                                if (json.get("top_pvp_start") != null) this.top_pvp_start = json.get("top_pvp_start").toString().trim();
                                if (json.get("top_pvp_end") != null) this.top_pvp_end = json.get("top_pvp_end").toString().trim();
                                if (json.get("top_pvp_season") != null) this.top_pvp_season = json.get("top_pvp_season").toString().trim();

                                Object twObj = json.get("top_wanted");
                                if (twObj instanceof JSONObject) {
                                    JSONObject tw = (JSONObject) twObj;
                                    if (tw.get("start") != null) this.top_wanted_start = tw.get("start").toString().trim();
                                    if (tw.get("end") != null) this.top_wanted_end = tw.get("end").toString().trim();
                                    if (tw.get("season") != null) this.top_wanted_season = tw.get("season").toString().trim();
                                }
                                if (json.get("top_wanted_start") != null) this.top_wanted_start = json.get("top_wanted_start").toString().trim();
                                if (json.get("top_wanted_end") != null) this.top_wanted_end = json.get("top_wanted_end").toString().trim();
                                if (json.get("top_wanted_season") != null) this.top_wanted_season = json.get("top_wanted_season").toString().trim();

                                if (json.get("trade_min_online_hours") != null) {
                                    try { this.trade_min_online_hours = Integer.parseInt(json.get("trade_min_online_hours").toString().trim()); } catch (Exception ignored) {}
                                }
                                if (json.get("exp_rate") != null) {
                                    try { this.exp = Integer.parseInt(json.get("exp_rate").toString().trim()); } catch (Exception ignored) {}
                                }
                            }
                        } catch (Exception ignored) {}
                    } else if ("server_test_start".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) this.server_test_start = val.trim();
                    } else if ("server_test_end".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) this.server_test_end = val.trim();
                    } else if ("server_beta_start".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) this.server_beta_start = val.trim();
                    } else if ("server_beta_end".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) this.server_beta_end = val.trim();
                    } else if ("server_open_date".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) this.server_open_date = val.trim();
                    } else if ("top_caothu_reward_time".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) this.top_caothu_reward_time = val.trim();
                    } else if ("lock_coin_exchange_racing".equalsIgnoreCase(key)) {
                        if (val != null && !val.trim().isEmpty()) {
                            this.lock_coin_exchange_racing = "true".equalsIgnoreCase(val.trim()) || "1".equals(val.trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.error("Manager", "Error loading settings from DB: " + e.getMessage());
        }
    }

    private void start_service() {
        this.running = true;
        for (map.Map mapall : map.MapManager.getInstance().getMaps()) {
            for (Zone map : mapall.zones) {
                if (map != null) {
                    map.setRunning(true);
                }
            }
        }
        // Initialize static service modules — touch each class to trigger static initializers
        tx = new TaiXiu();
        // Suppress unused-result: these access static fields to force class loading
        int _r = Rebuild_Item.ID_SELL.length
               + Red_Line.KEY0.length
               + UpgradeItem.DATA.size()
               + ItemBoat.ENTRYS.size()
               + ItemSell.ENTRYS.size()
               + VongQuay.ID_ITEM.length
               + Level.ENTRYS.length
               + Skill_info.EXP.length;
        AbilityTemplateManager.gI().init();
        database.CacheManager.gI().init();
        database.DungeonSessionCache.gI().loadAllDungeonsFromCache();
        activities.TimedDungeonManager.gI().loadCache();
        
        timedDungeonThread = new Thread(() -> {
            try {
                Thread.sleep(2000L); // Chờ 2s cho các map/zone load xong
                if (this.running) {
                    activities.TimedDungeonManager.gI().tick(); // Chạy ngay lập tức khi khởi động
                }
            } catch (Exception ignored) {}
            while (this.running) {
                try {
                    Thread.sleep(10_000L); // Tick mỗi 10s để kiểm tra đúng giờ thực
                    if (this.running) {
                        activities.TimedDungeonManager.gI().tick();
                    }
                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                    if (this.running) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Timed-Dungeon-Thread");
        timedDungeonThread.start();

        Log.success("Service", "Engine core services initialized (" + _r + " static entries loaded)");
    }

    private void stop_service() {
        this.running = false;
        try {
            activities.TimedDungeonManager.gI().saveCache();
            database.DungeonSessionCache.gI().saveAllDungeonsToCache();
        } catch (Exception ignored) {}
        if (timedDungeonThread != null) {
            timedDungeonThread.interrupt();
        }
        for (map.Map mapall : map.MapManager.getInstance().getMaps()) {
            for (Zone map : mapall.zones) {
                map.stop_map();
            }
        }
        if (tx != null) {
            tx.close();
        }
        for (int i = 0; i < Zone.get_map_plus().size(); i++) {
            Zone.get_map_plus().get(i).stop_map();
        }
        map.MapManager.getInstance().stop();
    }


    private void load_head_from_itemhair() {
        // Nạp chuẩn 16 mẫu khuôn mặt Thẩm Mỹ Viện (Type 108)
        Object[][] defaultHeads = new Object[][]{
            {0, "Mặt Trái xoan", (short) 0, 0, (short) 0},
            {3, "Mặt Ngầu", (short) 489, 50000, (short) 0},
            {1, "Mặt Bầu Bĩnh", (short) 487, 50000, (short) 0},
            {2, "Mặt Lạnh", (short) 488, 50000, (short) 0},
            {4, "Mũi dài", (short) 573, 0, (short) 50},
            {5, "Pell", (short) 574, 0, (short) 50},
            {6, "Buggy", (short) 575, 0, (short) 50},
            {7, "Franky", (short) 730, 0, (short) 70},
            {8, "Dragon", (short) 731, 0, (short) 70},
            {9, "Killer", (short) 732, 0, (short) 70},
            {11, "Morgan", (short) 733, 0, (short) 70},
            {12, "Zeff", (short) 734, 0, (short) 70},
            {13, "Râu trắng", (short) 729, 0, (short) 100},
            {14, "Người cá", (short) 768, 0, (short) 70},
            {16, "Lego", (short) 770, 0, (short) 100},
            {18, "Râu đen", (short) 880, 0, (short) 200}
        };
        for (Object[] h : defaultHeads) {
            short id = ((Number) h[0]).shortValue();
            if (ItemHair.get_item(id, 108) == null) {
                String name = (String) h[1];
                short icon = ((Number) h[2]).shortValue();
                int beri = ((Number) h[3]).intValue();
                short ruby = ((Number) h[4]).shortValue();
                ItemHair headItem = new ItemHair(id, icon, name, beri, ruby, 108, new ArrayList<>());
                ItemHair.putItem(headItem);
            }
        }

        // Nạp và bổ sung toàn bộ 67 mẫu Tiệm Tóc (Type 103) nếu CSDL thiếu
        Object[][] defaultHairs = new Object[][]{
            {1, "Tóc tảo biển", 24, 5000, 0, "[]"},
            {2, "Tóc bồi bàn", 28, 5000, 0, "[]"},
            {3, "Tóc đỏ son môi", 32, 5000, 0, "[]"},
            {4, "Tóc xoăn", 36, 5000, 0, "[]"},
            {5, "Tóc lù xù", 1, 5000, 0, "[]"},
            {6, "Tóc chẻ bụi đời", 41, 0, 10, "[]"},
            {7, "Tóc võ sĩ đạo", 45, 0, 10, "[]"},
            {8, "Tóc bạch kim", 49, 0, 10, "[]"},
            {9, "Tóc ngắn xinh xắn", 53, 0, 10, "[]"},
            {10, "Tóc quý tộc", 57, 0, 10, "[]"},
            {11, "Tóc lỳ lợm", 185, 50000, 0, "[]"},
            {12, "Tóc hiệp khách", 189, 50000, 0, "[]"},
            {13, "Tóc đường phố", 193, 50000, 0, "[]"},
            {14, "Tóc cam nhẹ nhàng", 197, 50000, 0, "[]"},
            {15, "Tóc thổ dân", 201, 50000, 0, "[]"},
            {16, "Tóc nhà vô địch", 205, 0, 100, "[]"},
            {17, "Tóc thủy thần", 209, 0, 50, "[]"},
            {18, "Tóc lãng tử", 213, 0, 50, "[]"},
            {19, "Tóc tiên cá", 217, 0, 50, "[]"},
            {20, "Tóc ngựa", 221, 0, 100, "[]"},
            {21, "Tóc Vivi", 484, 0, 100, "[]"},
            {22, "Tóc Tím", 485, 0, 100, "[]"},
            {23, "Tóc Vàng", 486, 0, 100, "[]"},
            {24, "Đầu trọc", -1, 0, 100, "[]"},
            {25, "Tóc băng", 626, 0, 150, "[]"},
            {26, "Tóc lửa", 627, 0, 150, "[]"},
            {27, "Tóc Pháp Sư", 628, 0, 150, "[]"},
            {28, "Tóc đấu sĩ", 629, 0, 150, "[]"},
            {32, "Tóc sét", 632, 0, 150, "[]"},
            {33, "Tóc thép", 634, 0, 150, "[]"},
            {34, "Tóc nước", 633, 0, 150, "[]"},
            {35, "Tóc tuyết", 635, 0, 150, "[]"},
            {36, "Tóc khỉ con", 714, 0, 500, "[]"},
            {37, "Tóc lưỡi liềm", 715, 0, 250, "[]"},
            {38, "Tóc ShowBiz", 716, 0, 500, "[]"},
            {39, "Tóc Công Chúa", 717, 0, 500, "[]"},
            {41, "Tóc ấn tượng", 771, 0, 250, "[]"},
            {42, "Tóc Lego", 775, 0, 250, "[]"},
            {43, "Tóc mốc lai", 776, 0, 250, "[]"},
            {44, "Tóc Kool", 777, 0, 500, "[]"},
            {45, "Tóc Siêu Sao", 772, 0, 500, "[]"},
            {48, "Tóc râu đen", 881, 0, 500, "[]"},
            {49, "Tóc RS Ichiji", 1008, 0, 300, "[]"},
            {50, "Tóc RS Niji", 1012, 0, 300, "[]"},
            {51, "Tóc RS Yonji", 1016, 0, 300, "[]"},
            {52, "Tóc RS Sanji", 1019, 0, 300, "[]"},
            {53, "Tóc RS Reiju", 1026, 0, 300, "[]"},
            {54, "Tóc RD Judge", 1023, 0, 300, "[]"},
            {55, "Cam điển trai", 1055, 0, 500, "[[17,30],[1,100]]"},
            {56, "Xanh soái ca", 1056, 0, 500, "[[10,50],[17,30]]"},
            {57, "Vàng mái buông", 1057, 0, 400, "[[17,30],[13,50]]"},
            {58, "Xanh dễ thương", 1058, 0, 400, "[[17,30],[1,100]]"},
            {59, "Đỏ thắt nơ", 1059, 0, 400, "[[10,50],[17,30]]"},
            {60, "Hồng Lolita", 1060, 0, 400, "[[17,30],[13,50]]"},
            {61, "Siêu tóc huyền thoại", 1061, 0, 800, "[]"},
            {62, "Xanh Trầm Tính", 1088, 0, 400, "[[17,10],[18,30]]"},
            {63, "Đen Cá Tính", 1089, 0, 400, "[[17,20],[14,50]]"},
            {64, "Vàng Lãnh Tử", 1090, 0, 400, "[[17,20],[10,50]]"},
            {65, "Nhu Mì", 1091, 0, 400, "[[17,20],[18,30]]"},
            {66, "Thảo Mai", 1092, 0, 400, "[[17,20],[14,50]]"},
            {67, "Chị đại học đường", 1093, 0, 400, "[[17,20],[10,50]]"}
        };
        for (Object[] h : defaultHairs) {
            short id = ((Number) h[0]).shortValue();
            if (ItemHair.get_item(id, 103) == null) {
                String name = (String) h[1];
                short icon = ((Number) h[2]).shortValue();
                int beri = ((Number) h[3]).intValue();
                short ruby = ((Number) h[4]).shortValue();
                ArrayList<Option> opList = new ArrayList<>();
                try {
                    org.json.simple.JSONArray js = (org.json.simple.JSONArray) org.json.simple.JSONValue.parse((String) h[5]);
                    if (js != null) {
                        for (int i = 0; i < js.size(); i++) {
                            org.json.simple.JSONArray js2 = (org.json.simple.JSONArray) org.json.simple.JSONValue.parse(js.get(i).toString());
                            opList.add(new Option(Short.parseShort(js2.get(0).toString()), Short.parseShort(js2.get(1).toString())));
                        }
                    }
                } catch (Exception ignored) {}
                ItemHair hairItem = new ItemHair(id, icon, name, beri, ruby, 103, opList);
                ItemHair.putItem(hairItem);
            }
        }
        Log.info("Template", "Loaded " + ItemHair.get_size_type(103) + " `itemhair` & " + ItemHair.get_size_type(108) + " `itemface`");
    }

    private void load_database() {
        Log.info("Templates", "Loading game data templates from database...");
        Connection conn = null;
        Statement ps = null;
        ResultSet rs = null;
        try {
            conn = DbManager.gI().getConnect();
            ps = conn.createStatement();
            // load mobs
            {
                MobTemplate.ENTRYS = new ArrayList<>();
                rs = ps.executeQuery("SELECT * FROM `mobs`;");
                while (rs.next()) {
                    MobTemplate temp = new MobTemplate();
                    temp.mob_id = rs.getShort("id");
                    temp.name = rs.getString("name");
                    if (temp.name == null) temp.name = "";
                    temp.level = rs.getShort("level");
                    temp.hp_max = rs.getInt("hp");
                    temp.hOne = rs.getShort("hOne");
                    temp.typemove = rs.getByte("typemove");
                    temp.ishuman = rs.getByte("ishuman");
                    temp.typemonster = rs.getByte("typemonster");
                    JSONArray js = (JSONArray) JSONValue.parse(rs.getString("idicon"));
                    if (temp.ishuman == 0) {
                        if (js.size() > 1) { temp.icon = Short.parseShort(js.get(1).toString()); }
                        else if (js.size() > 0) { temp.icon = Short.parseShort(js.get(0).toString()); }
                        else { temp.icon = 0; }
                    } else if (temp.ishuman == 1) {
                        temp.head = (js.size() > 1) ? Short.parseShort(js.get(1).toString()) : 0;
                        temp.hair = (js.size() > 2) ? Short.parseShort(js.get(2).toString()) : 0;
                        if (js.size() > 3) {
                            JSONArray js2 = (JSONArray) JSONValue.parse(js.get(3).toString());
                            temp.wearing = new short[js2.size()];
                            for (int i = 0; i < temp.wearing.length; i++) { temp.wearing[i] = Short.parseShort(js2.get(i).toString()); }
                        } else { temp.wearing = new short[0]; }
                    }
                    js.clear();
                    js = (JSONArray) JSONValue.parse(rs.getString("skill"));
                    temp.skill = new short[js.size()];
                    for (int i = 0; i < temp.skill.length; i++) { temp.skill[i] = Short.parseShort(js.get(i).toString()); }
                    MobTemplate.ENTRYS.add(temp);
                }
                rs.close();
                Log.step("Mobs", MobTemplate.ENTRYS.size() + " templates");
            }
            shoptichluy_entry = new store.LimitShop().initItems();
            Log.step("Shop Tich Luy", shoptichluy_entry.size() + " items");
            // load map
            String query = "SELECT * FROM `maps`;";
            rs = ps.executeQuery(query);
            List<MapTemplate> mapEntryList = new ArrayList<>();
            while (rs.next()) {
                short id_map = 0;
                try {
                    id_map = rs.getShort("id");
                    MapTemplate map_temp = new MapTemplate();
                    map_temp.id = id_map;
                    map_temp.name = rs.getString("name");
                    if (map_temp.name == null) map_temp.name = "";
                    map_temp.max_zone = rs.getByte("maxzone");
                    map_temp.max_player = rs.getByte("maxplayer");
                    // Mở rộng sức chứa Đấu Trường Tự Do (maps 70, 71, 72, 74) lên tối đa 100 người mỗi khu
                    if (id_map == 70 || id_map == 71 || id_map == 72 || id_map == 74) {
                        map_temp.max_player = (byte) 100;
                    }
                    JSONArray js_npc = (JSONArray) JSONValue.parse(rs.getString("npcs"));
                    List<Npc> npcList = new ArrayList<>();
                    if (js_npc != null) {
                        for (int i = 0; i < js_npc.size(); i++) {
                            JSONArray js_npc_temp = (JSONArray) JSONValue.parse(js_npc.get(i).toString());
                            if (js_npc_temp == null) continue;
                            Npc npc = new Npc();
                            npc.idmenu = Short.parseShort(js_npc_temp.get(0).toString());
                            npc.name = js_npc_temp.get(1).toString();
                            npc.namegt = js_npc_temp.get(2).toString();
                            npc.chat = js_npc_temp.get(3).toString();
                            npc.x = Short.parseShort(js_npc_temp.get(4).toString());
                            npc.y = Short.parseShort(js_npc_temp.get(5).toString());
                            npc.isPerson = Byte.parseByte(js_npc_temp.get(6).toString());
                            npc.typeIcon = Byte.parseByte(js_npc_temp.get(7).toString());
                            npc.wBlock = Byte.parseByte(js_npc_temp.get(8).toString());
                            npc.hBlock = Byte.parseByte(js_npc_temp.get(9).toString());
                            npc.b3 = Byte.parseByte(js_npc_temp.get(10).toString());
                            JSONArray js_npc_temp_2 = (JSONArray) JSONValue.parse(js_npc_temp.get(11).toString());
                            npc.dataFrame = new byte[js_npc_temp_2.size()];
                            for (int j = 0; j < npc.dataFrame.length; j++) {
                                npc.dataFrame[j] = Byte.parseByte(js_npc_temp_2.get(j).toString());
                            }
                            npc.head = Short.parseShort(js_npc_temp.get(12).toString());
                            npc.hair = Short.parseShort(js_npc_temp.get(13).toString());
                            if (npc.idmenu == -975) {
                                npc.b3 = 0;
                                npc.dataFrame = new byte[]{1, 2}; // IdIcon = 1 (loads 5001), 2 frames
                            }
                            JSONArray js_npc_temp_3 = (JSONArray) JSONValue.parse(js_npc_temp.get(14).toString());
                            npc.wearing = new short[js_npc_temp_3.size()];
                            for (int k = 0; k < npc.wearing.length; k++) {
                                npc.wearing[k] = Short.parseShort(js_npc_temp_3.get(k).toString());
                            }
                            npcList.add(npc);
                        }
                        js_npc.clear();
                    }
                    map_temp.npcs = new java.util.concurrent.CopyOnWriteArrayList<>(npcList);
                    js_npc = (JSONArray) JSONValue.parse(rs.getString("boat"));
                    List<BoatInMap> boatList = new ArrayList<>();
                    if (js_npc != null) {
                        for (int i = 0; i < js_npc.size(); i++) {
                            JSONArray js_temp = (JSONArray) js_npc.get(i);
                            if (js_temp == null) continue;
                            BoatInMap temp_boat = new BoatInMap();
                            temp_boat.x = Short.parseShort(js_temp.get(0).toString());
                            temp_boat.y = Short.parseShort(js_temp.get(1).toString());
                            boatList.add(temp_boat);
                        }
                        js_npc.clear();
                    }
                    map_temp.list_boat = new java.util.concurrent.CopyOnWriteArrayList<>(boatList);
                    List<Vgo> vgoList = new ArrayList<>();
                    js_npc = (JSONArray) JSONValue.parse(rs.getString("vgos"));
                    if (js_npc != null) {
                        for (int i = 0; i < js_npc.size(); i++) {
                            JSONArray js_0 = (JSONArray) js_npc.get(i);
                            if (js_0 == null) continue;
                            Vgo vgo_temp = new Vgo();
                            vgo_temp.id_map_go = Short.parseShort(js_0.get(0).toString());
                            vgo_temp.xold = Short.parseShort(js_0.get(1).toString());
                            vgo_temp.yold = Short.parseShort(js_0.get(2).toString());
                            vgo_temp.xnew = Short.parseShort(js_0.get(3).toString());
                            vgo_temp.ynew = Short.parseShort(js_0.get(4).toString());
                            if (vgo_temp.id_map_go != -1) {
                                // Ánh xạ chuẩn cho các map Bảo Vệ Pháo Đài (267: Làng Đỏ, 268: Đường Trên, 269: Đường Dưới, 270: Đường Giữa, 271: Làng Xanh)
                                if (map_temp.id >= 267 && map_temp.id <= 271) {
                                    if (vgo_temp.id_map_go == 203) vgo_temp.id_map_go = 267;
                                    else if (vgo_temp.id_map_go == 204) vgo_temp.id_map_go = 268;
                                    else if (vgo_temp.id_map_go == 205) vgo_temp.id_map_go = 269;
                                    else if (vgo_temp.id_map_go == 206) vgo_temp.id_map_go = 270;
                                    else if (vgo_temp.id_map_go == 207) vgo_temp.id_map_go = 271;
                                }
                                vgoList.add(vgo_temp);
                            }
                        }
                        js_npc.clear();
                    }
                    map_temp.vgos = new java.util.concurrent.CopyOnWriteArrayList<>(vgoList);
                    map_temp.type_view_p = rs.getByte("typeViewPlayer");
                    map_temp.b = rs.getByte("b");
                    map_temp.specMap = rs.getByte("specMap");
                    map_temp.type_view_p = rs.getByte("typeViewPlayer");
                    map_temp.b = rs.getByte("b");
                    map_temp.specMap = rs.getByte("specMap");

                    // Read separate columns w, h, tile_id if available
                    short mapW = 0;
                    short mapH = 0;
                    byte tileId = 0;
                    try { mapW = rs.getShort("w"); } catch (Exception ignored) {}
                    try { mapH = rs.getShort("h"); } catch (Exception ignored) {}
                    try { tileId = rs.getByte("tile_id"); } catch (Exception ignored) {}
                    map_temp.w = mapW;
                    map_temp.h = mapH;
                    map_temp.tile_id = tileId;

                    // Read direct background columns if available
                    try { map_temp.IDBack = rs.getByte("IDBack"); } catch (Exception ignored) {}
                    try { map_temp.HBack = rs.getShort("HBack"); } catch (Exception ignored) {}
                    try { map_temp.maxW = rs.getShort("maxW"); } catch (Exception ignored) {}
                    try { map_temp.maxH = rs.getShort("maxH"); } catch (Exception ignored) {}

                    // MapBack fallback (if direct columns not populated or legacy schema)
                    try {
                        String mapBackStr = rs.getString("MapBack");
                        if (mapBackStr != null && !mapBackStr.isEmpty() && !mapBackStr.equalsIgnoreCase("null")) {
                            JSONArray js_back = (JSONArray) JSONValue.parse(mapBackStr);
                            if (js_back != null && js_back.size() >= 4) {
                                map_temp.IDBack = Byte.parseByte(js_back.get(0).toString());
                                map_temp.HBack = Short.parseShort(js_back.get(1).toString());
                                short bW = Short.parseShort(js_back.get(2).toString());
                                short bH = Short.parseShort(js_back.get(3).toString());
                                if (map_temp.maxW <= 0) map_temp.maxW = bW;
                                if (map_temp.maxH <= 0) map_temp.maxH = bH;
                            }
                        }
                    } catch (Exception ignored) {}

                    // Load map data: first check database column `data`, then fallback to binary files
                    boolean loadedData = false;
                    try {
                        String dataStr = rs.getString("data");
                        if (dataStr != null && !dataStr.isEmpty() && !dataStr.equalsIgnoreCase("null")) {
                            JSONArray js_data = (JSONArray) JSONValue.parse(dataStr);
                            if (js_data != null && js_data.size() >= 2) {
                                map_temp.data = new byte[2][];
                                for (int i = 0; i < js_data.size(); i++) {
                                    JSONArray js_in = (JSONArray) js_data.get(i);
                                    map_temp.data[i] = new byte[js_in.size()];
                                    for (int j = 0; j < map_temp.data[i].length; j++) {
                                        map_temp.data[i][j] = Byte.parseByte(js_in.get(j).toString());
                                    }
                                }
                                if (map_temp.data[0] != null && map_temp.data[0].length >= 3) {
                                    int rawW = map_temp.data[0][0] & 0xFF;
                                    int rawH = map_temp.data[0][1] & 0xFF;
                                    if (rawW > 0 && rawH > 0) {
                                        map_temp.w = (short) rawW;
                                        map_temp.h = (short) rawH;
                                        map_temp.tile_id = map_temp.data[0][2];
                                        loadedData = true;
                                    }
                                }
                            }
                        }
                    } catch (Exception ignored) {}

                    // Fallback: load binary map files {id}_data and {id}_item if database column was empty
                    if (!loadedData) {
                        java.io.File dataFile = new java.io.File("data/map/data/" + id_map + "_data");
                        if (!dataFile.exists()) dataFile = new java.io.File("data/map/" + id_map + "_data");
                        java.io.File itemFile = new java.io.File("data/map/data/" + id_map + "_item");
                        if (!itemFile.exists()) itemFile = new java.io.File("data/map/" + id_map + "_item");

                        if (dataFile.exists()) {
                            try {
                                byte[] blockBytes = java.nio.file.Files.readAllBytes(dataFile.toPath());
                                byte[] itemBytes = itemFile.exists() ? java.nio.file.Files.readAllBytes(itemFile.toPath()) : new byte[]{0, 0, 0, 0};

                                if (blockBytes.length >= 3 && (blockBytes[0] & 0xFF) > 0 && (blockBytes[1] & 0xFF) > 0) {
                                    map_temp.data = new byte[2][];
                                    map_temp.data[0] = blockBytes;
                                    map_temp.data[1] = itemBytes;
                                    map_temp.w = (short) (blockBytes[0] & 0xFF);
                                    map_temp.h = (short) (blockBytes[1] & 0xFF);
                                    map_temp.tile_id = blockBytes[2];
                                    loadedData = true;
                                } else if (map_temp.w > 0 && map_temp.h > 0) {
                                    map_temp.data = new byte[2][];
                                    map_temp.data[0] = new byte[3 + blockBytes.length];
                                    map_temp.data[0][0] = (byte) map_temp.w;
                                    map_temp.data[0][1] = (byte) map_temp.h;
                                    map_temp.data[0][2] = map_temp.tile_id;
                                    System.arraycopy(blockBytes, 0, map_temp.data[0], 3, blockBytes.length);
                                    map_temp.data[1] = itemBytes;
                                    loadedData = true;
                                }
                            } catch (Exception e) {
                                System.err.println("[Manager] Error loading binary map " + id_map + ": " + e.getMessage());
                            }
                        }
                    }

                    if (map_temp.maxW <= 0 && map_temp.w > 0) map_temp.maxW = (short) (map_temp.w * 24);
                    if (map_temp.maxH <= 0 && map_temp.h > 0) map_temp.maxH = (short) (map_temp.h * 24);
                    if (map_temp.data != null && map_temp.data.length > 0 && map_temp.data[0] != null && map_temp.data[0].length >= 2) {
                        int tileW = map_temp.data[0][0] & 0xFF;
                        int tileH = map_temp.data[0][1] & 0xFF;
                        if (tileW > 0) {
                            map_temp.maxW = (short) Math.max((int) map_temp.maxW, tileW * 24);
                        }
                        if (tileH > 0) {
                            map_temp.maxH = (short) Math.max((int) map_temp.maxH, tileH * 24);
                        }
                    }
                    map_temp.id_eff_map = rs.getByte("id_eff_map");
                    map_temp.level = rs.getByte("level");
                    map_temp.typeChangeMap = rs.getByte("typeChangeMap");
                    JSONArray js_train = (JSONArray) JSONValue.parse(rs.getString("mPosMapTrain"));
                    if (js_train != null) {
                        map_temp.mPosMapTrain = new byte[js_train.size()][];
                        for (int i = 0; i < js_train.size(); i++) {
                            JSONArray js_in = (JSONArray) js_train.get(i);
                            map_temp.mPosMapTrain[i] = new byte[js_in.size()];
                            for (int j = 0; j < map_temp.mPosMapTrain[i].length; j++) {
                                map_temp.mPosMapTrain[i][j] = Byte.parseByte(js_in.get(j).toString());
                            }
                        }
                    } else {
                        map_temp.mPosMapTrain = new byte[0][];
                    }
                    map_temp.strTimeChange = rs.getString("strTimeChange");
                    if (map_temp.strTimeChange == null) map_temp.strTimeChange = "";
                    mapEntryList.add(map_temp);
                    //
                    String mob_json = rs.getString("mobs");
                    Zone[] m_temp = new Zone[map_temp.max_zone];
                    for (int i2 = 0; i2 < m_temp.length; i2++) {
                        m_temp[i2] = new Zone();
                        m_temp[i2].zone_id = (byte) i2;
                        m_temp[i2].template = map_temp;
                        if (mob_json != null && !mob_json.isEmpty() && !mob_json.equalsIgnoreCase("null")) {
                            JSONArray js = (JSONArray) JSONValue.parse(mob_json);
                            if (js != null) {
                                m_temp[i2].list_mob = new int[js.size()];
                                for (int i = 0; i < js.size(); i++) {
                                    JSONArray js2 = (JSONArray) JSONValue.parse(js.get(i).toString());
                                    if (js2 != null && js2.size() >= 3) {
                                        int mobTemplateId = Integer.parseInt(js2.get(0).toString());
                                        MobTemplate template = MobTemplate.get_mob_template(mobTemplateId);
                                        short mobX = Short.parseShort(js2.get(1).toString());
                                        short mobY = Short.parseShort(js2.get(2).toString());
                                        int currentIndex = this.index_mob.getAndIncrement();
                                        Mob temp = mob.MobFactory.createMob(template, m_temp[i2], mobX, mobY, currentIndex);
                                        Mob.ENTRYS.put(currentIndex, temp);
                                        if (temp.map != null) {
                                            temp.map.mobs.put(currentIndex, temp);
                                        }
                                        m_temp[i2].list_mob[i] = currentIndex;

                                        if (temp.map.template.id == 179) {
                                            ThuLinhBienKhoi.mob = temp;
                                            temp.isdie = true;
                                        }
                                    }
                                }
                            } else {
                                m_temp[i2].list_mob = new int[0];
                            }
                        } else {
                            m_temp[i2].list_mob = new int[0];
                        }
                    }

                    map.Map newMap = new map.Map(map.MapManager.getInstance().getMaps().size(), map_temp, m_temp);
                    map.MapManager.getInstance().addMap(newMap);
                } catch (Exception e) {
                    System.err.println("[Manager] Error loading map ID " + id_map + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            rs.close();
            MapTemplate.ENTRYS = new java.util.concurrent.CopyOnWriteArrayList<>(mapEntryList);
            for (int i = 0; i < MapTemplate.ENTRYS.size(); i++) {
                for (int j = 0; j < MapTemplate.ENTRYS.get(i).vgos.size(); j++) {
                    Vgo vgo = MapTemplate.ENTRYS.get(i).vgos.get(j);
                    vgo.map_go = Zone.getMapByID(vgo.id_map_go);
                    if (vgo.map_go == null) {
                        vgo.map_go = Zone.getMapByID(1);
                    }
                }
            }
            Log.step("Map & Mobs", MapTemplate.ENTRYS.size() + " maps, " + (this.index_mob.get() - 1) + " mob instances");
            // load parts
            Part.ENTRY = new ArrayList<>();
            {
                rs = ps.executeQuery("SELECT * FROM `parts`;");
                while (rs.next()) {
                    byte type = rs.getByte("type");
                    JSONArray js = (JSONArray) JSONValue.parse(rs.getString("data"));
                    Part part = new Part(type);
                    part.id = rs.getShort("id");
                    if (part.pi != null && js != null) {
                        for (int i = 0; i < js.size() && i < part.pi.length; i++) {
                            JSONArray js_in = (JSONArray) js.get(i);
                            if (js_in == null || js_in.isEmpty()) continue;
                            part.pi[i] = new PartImg();
                            part.pi[i].id = Integer.parseInt(js_in.get(0).toString());
                            if (js_in.size() >= 3) {
                                part.pi[i].dx = Byte.parseByte(js_in.get(1).toString());
                                part.pi[i].dy = Byte.parseByte(js_in.get(2).toString());
                            } else if (js_in.size() == 2) {
                                part.pi[i].dx = 0;
                                part.pi[i].dy = Byte.parseByte(js_in.get(1).toString());
                            } else {
                                part.pi[i].dx = 0;
                                part.pi[i].dy = 0;
                            }
                        }
                    }
                    Part.ENTRY.add(part);
                }
                rs.close();
                Log.step("Parts", Part.ENTRY.size() + " templates");
            }
            // load dataEff
            DataEffect.load();
            // Load đấu giá từ settings (key: game_daugia) — không dùng bảng daugia nữa
            DauGia.loadFromSettings();
            // load item 3
            {
                ItemTemplate3.ENTRYS = new ArrayList<>();
                rs = ps.executeQuery("SELECT * FROM `item3`;");
                while (rs.next()) {
                    ItemTemplate3 temp = new ItemTemplate3();
                    temp.id = rs.getShort("id");
                    temp.name = rs.getString("name");
                    if (temp.name == null) temp.name = "";
                    temp.clazz = rs.getByte("clazz");
                    temp.typeEquip = rs.getByte("typeequip");
                    temp.icon = rs.getShort("icon");
                    temp.level = rs.getShort("level");
                    temp.color = rs.getByte("color");
                    temp.typelock = rs.getByte("typelock");
                    temp.numHoleDaDuc = rs.getByte("numHoleDaDuc");
                    try {
                        temp.valueChetac = rs.getShort("chetac");
                    } catch (Exception e) {
                        temp.valueChetac = (short) (100);
                    }
                    temp.isHoanMy = rs.getByte("ishoanmy");
                    temp.valueKichAn = -1;
                    try {
                        temp.beri = rs.getInt("beri");
                    } catch (Exception ignore) {}
                    JSONArray js = (JSONArray) JSONValue.parse(rs.getString("op_1"));
                    temp.option_item = new ArrayList<>();
                    for (int i = 0; i < js.size(); i++) {
                        JSONArray js2 = (JSONArray) JSONValue.parse(js.get(i).toString());
                        temp.option_item.add(new Option(Byte.parseByte(js2.get(0).toString()), Short.parseShort(js2.get(1).toString())));
                    }
                    temp.option_item_2 = new ArrayList<>();
                    js = (JSONArray) JSONValue.parse(rs.getString("op_2"));
                    for (int k = 0; k < js.size(); k++) {
                        JSONArray js2 = (JSONArray) JSONValue.parse(js.get(k).toString());
                        temp.option_item_2.add(new Option(Byte.parseByte(js2.get(0).toString()), Short.parseShort(js2.get(1).toString())));
                    }
                    temp.numLoKham = rs.getByte("numlokham");
                    js = (JSONArray) JSONValue.parse(rs.getString("mdakham"));
                    temp.mdakham = new short[js.size()];
                    for (int l = 0; l < temp.mdakham.length; l++) {
                        temp.mdakham[l] = Short.parseShort(js.get(l).toString());
                    }
                    temp.part = rs.getShort("part");
                    temp.initDefaultOptionsFromCode();
                    ItemTemplate3.ENTRYS.add(temp);
                }
                rs.close();
                // Đảm bảo 100% toàn bộ 90 món Thần Trang (15 set: 2604..2693) luôn có mặt trong ENTRYS
                for (int ttId = 2604; ttId <= 2693; ttId++) {
                    if (ItemTemplate3.get_it_by_id((short) ttId) == null) {
                        ItemTemplate3 ttTemp = new ItemTemplate3();
                        ttTemp.id = (short) ttId;
                        ttTemp.name = "Thần Trang " + ttId;
                        ttTemp.clazz = 0;
                        ttTemp.typeEquip = ThanTrangConfig.getTypeEquip(ttId);
                        ttTemp.icon = (short) (271 + (ttId - 2604));
                        ttTemp.level = 1;
                        ttTemp.color = 8;
                        ttTemp.typelock = 1;
                        ttTemp.numHoleDaDuc = 0;
                        ttTemp.valueChetac = 0;
                        ttTemp.isHoanMy = 0;
                        ttTemp.valueKichAn = -1;
                        ttTemp.option_item = new ArrayList<>();
                        ttTemp.option_item_2 = new ArrayList<>();
                        ttTemp.numLoKham = 0;
                        ttTemp.mdakham = new short[0];
                        ttTemp.part = -1;
                        ttTemp.initDefaultOptionsFromCode();
                        ItemTemplate3.ENTRYS.add(ttTemp);
                    }
                }
                Log.step("Item3", ItemTemplate3.ENTRYS.size() + " templates");
            }
            // load item temp 4 info
            ItemTemplate4_Info.ENTRY = new ArrayList<>();
            {
                rs = ps.executeQuery("SELECT * FROM `item4_info`;");
                while (rs.next()) {
                    ItemTemplate4_Info temp = new ItemTemplate4_Info();
                    temp.id = rs.getShort("id");
                    temp.info = rs.getString("info");
                    if (temp.info == null) {
                        temp.info = "";
                    } else if (temp.info.contains("</USER_REQUEST>") || temp.info.contains("<ADDITIONAL_METADATA>") || temp.info.contains("<USER_REQUEST>")) {
                        int idx = temp.info.indexOf("</USER_REQUEST>");
                        if (idx == -1) idx = temp.info.indexOf("<ADDITIONAL_METADATA>");
                        if (idx == -1) idx = temp.info.indexOf("<USER_REQUEST>");
                        if (idx != -1) temp.info = temp.info.substring(0, idx).trim();
                    }
                    ItemTemplate4_Info.ENTRY.add(temp);
                }
                rs.close();
            }
            // load item temp 4
            {
                ItemTemplate4.ENTRYS = new ArrayList<>();
                ItemTemplate4.hashMap = new HashMap<>();
                rs = ps.executeQuery("SELECT * FROM `item4`;");
                while (rs.next()) {
                    ItemTemplate4 temp = new ItemTemplate4();
                    temp.id = rs.getShort("id");
                    temp.name = rs.getString("name");
                    if (temp.name == null) temp.name = "";
                    temp.icon = rs.getShort("icon");
                    temp.indexInfoPotion = rs.getShort("indexInfoPotion");
                    try {
                        temp.beri = rs.getInt("price");
                    } catch (Exception ignore) {}
                    try {
                        temp.ruby = rs.getShort("priceruby");
                    } catch (Exception ignore) {}
                    temp.istrade = rs.getByte("istrade");
                    temp.type = rs.getByte("hpmpother");
                    temp.timedelay = rs.getShort("timedelay");
                    temp.value = rs.getShort("value");
                    temp.timeactive = rs.getShort("timeactive");
                    temp.nameuse = rs.getString("nameuse");
                    if (temp.nameuse == null) temp.nameuse = "";
                    if (temp.id == 876 || temp.id == 877) {
                        if (temp.type == 40) {
                            temp.type = -1;
                        }
                        if (temp.nameuse.isEmpty() || temp.nameuse.equalsIgnoreCase("null")) {
                            temp.nameuse = "Sử dụng";
                        }
                    }
                    ItemTemplate4.ENTRYS.add(temp);
                    ItemTemplate4.hashMap.put(temp.id, temp);
                }
                rs.close();
                if (!ItemTemplate4.hashMap.containsKey((short) 872)) {
                    ItemTemplate4 card872 = new ItemTemplate4();
                    card872.id = 872;
                    card872.name = "Thẻ Hệ Thống";
                    card872.icon = 401;
                    card872.indexInfoPotion = 0;
                    card872.beri = 0;
                    card872.ruby = 0;
                    card872.istrade = 0;
                    card872.type = -1;
                    card872.timedelay = 0;
                    card872.value = 0;
                    card872.timeactive = 0;
                    card872.nameuse = "Sử dụng";
                    ItemTemplate4.ENTRYS.add(card872);
                    ItemTemplate4.hashMap.put(card872.id, card872);
                }
                if (!ItemTemplate4.hashMap.containsKey((short) 911)) {
                    ItemTemplate4 chest911 = new ItemTemplate4();
                    chest911.id = 911;
                    chest911.name = "Rương Siêu Đại Ác Quỷ";
                    chest911.icon = 112;
                    chest911.indexInfoPotion = 0;
                    chest911.beri = 0;
                    chest911.ruby = 0;
                    chest911.istrade = 1;
                    chest911.type = 31;
                    chest911.timedelay = 0;
                    chest911.value = 0;
                    chest911.timeactive = 0;
                    chest911.nameuse = "Mở rương";
                    ItemTemplate4.ENTRYS.add(chest911);
                    ItemTemplate4.hashMap.put(chest911.id, chest911);
                }
                Log.step("Item4", ItemTemplate4.ENTRYS.size() + " templates");
            }
            // load item temp 7
            {
                ItemTemplate7.ENTRYS = new ArrayList<>();
                ItemTemplate7.hashMap = new HashMap<>();
                rs = ps.executeQuery("SELECT * FROM `item7`;");
                while (rs.next()) {
                    ItemTemplate7 temp = new ItemTemplate7();
                    temp.id = rs.getShort("id");
                    temp.name = rs.getString("name");
                    if (temp.name == null) temp.name = "";
                    temp.type = rs.getByte("type");
                    temp.icon = rs.getByte("icon");
                    try {
                        temp.price = rs.getInt("price");
                    } catch (Exception ignore) {}
                    try {
                        temp.priceruby = rs.getShort("priceruby");
                    } catch (Exception ignore) {}
                    if (temp.id == 3 && temp.price <= 0 && temp.priceruby <= 0) {
                        temp.priceruby = 3;
                    }
                    temp.istrade = rs.getByte("istrade");
                    ItemTemplate7.ENTRYS.add(temp);
                    ItemTemplate7.hashMap.put(temp.id, temp);
                }
                rs.close();
                Log.step("Item7", ItemTemplate7.ENTRYS.size() + " templates");
            }
            // load ip lock
            query = "SELECT * FROM `historys` WHERE `type` = 'IP_LOCK';";
            rs = ps.executeQuery(query);
            while (rs.next()) {
                String dataStr = rs.getString("data");
                if (dataStr != null) {
                    Object parsed = JSONValue.parse(dataStr);
                    if (parsed instanceof JSONObject) {
                        JSONObject js = (JSONObject) parsed;
                        if (js.containsKey("ip")) {
                            Session.IP_LOCK.add(js.get("ip").toString());
                        }
                    } else {
                        Session.IP_LOCK.add(dataStr);
                    }
                }
            }
            rs.close();
            Log.step("IP Lock List", Session.IP_LOCK.size() + " blocked IPs");
            // load skill temp (with SSD cache)
            {
                Skill_Template.ENTRYS = new ArrayList<>();
                rs = ps.executeQuery("SELECT * FROM `skill` ORDER BY `id_index`;");
                while (rs.next()) {
                    String skName = rs.getString("name");
                    if (skName == null) skName = "";
                    String skInfo = rs.getString("info");
                    if (skInfo == null) skInfo = "";
                    Skill_Template temp_add = new Skill_Template(rs.getShort("id_index"),
                            rs.getShort("id_2"), rs.getShort("icon"), rs.getByte("typeSkill"),
                            rs.getByte("typeBuff"), skName, rs.getShort("typeEffSkill"),
                            rs.getShort("range"));
                    temp_add.getData(rs.getByte("nTarget"), rs.getShort("rangeLan"),
                            rs.getInt("damage"), rs.getShort("manaLost"), rs.getInt("timeDelay"),
                            rs.getByte("nKick"), skInfo, rs.getByte("Lv_RQ"),
                            rs.getByte("typeDevil"));
                    temp_add.op = new ArrayList<>();
                    String optStr = rs.getString("option");
                    if (optStr != null && !optStr.isEmpty()) {
                        JSONArray js = (JSONArray) JSONValue.parse(optStr);
                        if (js != null) {
                            for (int j = 0; j < js.size(); j++) {
                                JSONArray js2 = (JSONArray) JSONValue.parse(js.get(j).toString());
                                if (js2 != null && js2.size() >= 2) {
                                    temp_add.op.add(new Option(Byte.parseByte(js2.get(0).toString()), Integer.parseInt(js2.get(1).toString())));
                                }
                            }
                        }
                    }
                    String effSpecStr = rs.getString("EffSpec");
                    if (effSpecStr != null && !effSpecStr.isEmpty()) {
                        JSONArray js = (JSONArray) JSONValue.parse(effSpecStr);
                        if (js != null && js.size() >= 3) {
                            temp_add.idEffSpec = Byte.parseByte(js.get(0).toString());
                            temp_add.perEffSpec = Short.parseShort(js.get(1).toString());
                            temp_add.timeEffSpec = Short.parseShort(js.get(2).toString());
                        }
                    }
                    if (temp_add.ID >= 4001 && temp_add.ID <= 4016) {
                        applyThanTrangSkillDefaults(temp_add);
                    }
                    Skill_Template.ENTRYS.add(temp_add);
                }
                rs.close();

                // Ensure 16 Than Trang active skills (4001..4016) exist with authentic tables
                String[] thanTrangNames = new String[]{
                    "Hỏa Diễm Thần Quyền",
                    "Đại Phún Hỏa Volcano",
                    "Kỷ Băng Hà Tuyệt Đối",
                    "Bát Xích Quỳnh Khúc Ngọc",
                    "Hắc Ám Thôn Phệ Vô Tận",
                    "200 Triệu Volt Thần Lôi",
                    "Hải Chấn Toái Địa Cầu",
                    "ROOM Gamma Knife",
                    "Từ Trường Bộc Phá Đại Pháo",
                    "Cổ Độc Phán Quyết Venom",
                    "Mũi Tên Mê Hoặc Thạch Hóa",
                    "Phượng Hoàng Bất Tử Bộc Phá",
                    "Đại Phật Sóng Xung Kích",
                    "Bát Quái Cửu Long Thiên",
                    "Long Trảo Viêm Long Toái Địa",
                    "Vận Thạch Thiên Giáng"
                };
                short[] ttIcons = new short[]{201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216};
                short[] ttDamages = new short[]{5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000};
                byte[] ttTargets = new byte[]{5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
                short[] ttRangeLans = new short[]{140, 140, 140, 140, 140, 140, 140, 140, 140, 140, 140, 140, 140, 140, 140, 140};
                String[] ttInfos = new String[]{
                    "Tuyệt kỹ Hỏa Long Thần Trang giải phóng biển lửa thiêu rụi mục tiêu.",
                    "Tuyệt kỹ Hải Vương Thần Trang triệu hồi nham thạch phun trào hủy diệt.",
                    "Tuyệt kỹ Lôi Thần Thần Trang đóng băng vạn vật trong chớp mắt.",
                    "Tuyệt kỹ Phong Ma Thần Trang phóng mưa ngọc quang tử tốc độ ánh sáng.",
                    "Tuyệt kỹ Tử Thần Thần Trang mở ra lỗ đen nuốt chửng mọi kẻ thù.",
                    "Tuyệt kỹ Băng Đế Thần Trang giáng sấm sét 200 triệu volt hủy diệt.",
                    "Tuyệt kỹ Chấn Động Thần Trang đập vỡ không gian tạo đại hải chấn kinh thiên động địa. Kèm 50% tỷ lệ gây Choáng trong 3.0s.",
                    "Tuyệt kỹ Kim Cương Thần Trang phân cắt và phá hủy nội tạng đối thủ.",
                    "Tuyệt kỹ Hắc Ám Thần Trang tụ lực từ tính bắn đại pháo hủy diệt.",
                    "Tuyệt kỹ Quang Minh Thần Trang phóng độc dược ăn mòn sinh mệnh.",
                    "Tuyệt kỹ Tu La Thần Trang hóa đá mọi kẻ thù trúng phải.",
                    "Tuyệt kỹ Thánh Linh Thần Trang tung cánh phượng hoàng lam hỏa thiêu rụi.",
                    "Tuyệt kỹ Huyết Long Thần Trang chưởng sóng xung kích uy lực vô song.",
                    "Tuyệt kỹ Ma Thần Thần Trang quét chùy sấm sét bách thú vô địch.",
                    "Tuyệt kỹ Thiên Thần Thần Trang trảo rồng lửa phá hủy mặt đất.",
                    "Tuyệt kỹ Hỗn Độn Thần Trang kéo thiên thạch rơi tự do từ vũ trụ."
                };
                for (int s = 1; s <= 16; s++) {
                    int skId = 4000 + s;
                    short curIcon = ttIcons[s - 1];
                    Skill_Template ttSk = Skill_Template.get_temp_by_id(skId);
                    if (ttSk == null) {
                        ttSk = new Skill_Template(
                            (short) skId, (short) skId, curIcon,
                            (byte) 1, (byte) 0, thanTrangNames[s - 1],
                            (short) skId, (short) 220
                        );
                        ttSk.getData(ttTargets[s - 1], ttRangeLans[s - 1], ttDamages[s - 1], (short) 50, 3000, (byte) 1,
                            ttInfos[s - 1],
                            (byte) 0, (byte) 0);
                        ttSk.op = new ArrayList<>();
                        applyThanTrangSkillDefaults(ttSk);
                        Skill_Template.ENTRYS.add(ttSk);
                    } else {
                        ttSk.name = thanTrangNames[s - 1];
                        ttSk.typeEffSkill = (short) skId;
                        ttSk.idIcon = curIcon;
                        ttSk.range = 220;
                        ttSk.rangeLan = ttRangeLans[s - 1];
                        ttSk.nTarget = ttTargets[s - 1];
                        ttSk.damage = ttDamages[s - 1];
                        ttSk.manaLost = 50;
                        ttSk.timeDelay = 3000;
                        ttSk.nKick = 1;
                        ttSk.info = ttInfos[s - 1];
                        if (ttSk.op == null || ttSk.op.isEmpty()) {
                            ttSk.op = new ArrayList<>();
                            applyThanTrangSkillDefaults(ttSk);
                        }
                    }
                }

                // Ensure 5 sect class buff skills (1010..1014 / 487..511) exist
                String[] classBuffNames = new String[]{
                    "Không thể cản phá",
                    "Ảo ảnh tam kiếm",
                    "Sức mạnh của tình yêu",
                    "Cơn thịnh nộ của thời tiết",
                    "Bomb hẹn giờ"
                };
                String[] classBuffDescs = new String[]{
                    "Tăng mạnh sức phòng thủ và miễn nhiễm sát thương trong thời gian ngắn.",
                    "Tăng tỷ lệ chí mạng và tốc độ tấn công trong thời gian ngắn.",
                    "Hồi phục sinh lực và tăng sức mạnh cho bản thân và đồng đội.",
                    "Tăng sức mạnh sấm sét và gây choáng các mục tiêu xung quanh.",
                    "Đặt bom hẹn giờ làm choáng và gây sát thương diện rộng."
                };
                for (int c = 1; c <= 5; c++) {
                    int baseIdx = 487 + (c - 1) * 5;
                    int buffSkillId = 1009 + c;
                    short icon = (short) (40 + c);

                    // 1. Unlearned template (Lv_RQ = -1)
                    boolean hasUnlearned = false;
                    for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                        Skill_Template st = Skill_Template.ENTRYS.get(i);
                        if (st.indexSkillInServer == baseIdx && st.Lv_RQ == -1) {
                            hasUnlearned = true;
                            break;
                        }
                    }
                    if (!hasUnlearned) {
                        Skill_Template unlearnSk = new Skill_Template(
                            (short) baseIdx, (short) buffSkillId, icon,
                            (byte) 2, (byte) 1, classBuffNames[c - 1],
                            (short) 46, (short) 120
                        );
                        unlearnSk.getData((byte) (c == 5 ? 5 : 1), (short) 120, 0, (short) 30, 15000, (byte) 1,
                            classBuffDescs[c - 1], (byte) -1, (byte) 0);
                        unlearnSk.op = new ArrayList<>();
                        unlearnSk.op.add(new Option((byte) 32, 150));
                        Skill_Template.ENTRYS.add(unlearnSk);
                    }

                    // 2. Learned templates (Lv_RQ = 0..4)
                    for (int lv = 0; lv < 5; lv++) {
                        int curIdx = baseIdx + lv;
                        boolean hasLearned = false;
                        for (int i = 0; i < Skill_Template.ENTRYS.size(); i++) {
                            Skill_Template st = Skill_Template.ENTRYS.get(i);
                            if (st.indexSkillInServer == curIdx && st.Lv_RQ >= 0) {
                                hasLearned = true;
                                break;
                            }
                        }
                        if (!hasLearned) {
                            Skill_Template learnedSk = new Skill_Template(
                                (short) curIdx, (short) buffSkillId, icon,
                                (byte) 2, (byte) 1, classBuffNames[c - 1] + (lv > 0 ? " +" + lv : ""),
                                (short) 46, (short) 120
                            );
                            learnedSk.getData((byte) (c == 5 ? 5 : 1), (short) 120, 0, (short) (30 + lv * 10), 15000, (byte) 1,
                                classBuffDescs[c - 1], (byte) (lv + 1), (byte) 0);
                            learnedSk.op = new ArrayList<>();
                            learnedSk.op.add(new Option((byte) 32, 150 + lv * 30));
                            if (c == 1) {
                                learnedSk.op.add(new Option((byte) 11, 100 + lv * 20)); // Bất tử
                            } else if (c == 2) {
                                learnedSk.op.add(new Option((byte) 12, 100 + lv * 20)); // Chí mạng liên tục
                            } else if (c == 3) {
                                learnedSk.op.add(new Option((byte) 13, 100 + lv * 20)); // Trừ bất tử / buff máu
                            } else if (c == 4) {
                                learnedSk.op.add(new Option((byte) 14, 100 + lv * 20)); // Bão tố
                            } else if (c == 5) {
                                learnedSk.op.add(new Option((byte) 1, 100 + lv * 20)); // Choáng
                            }
                            Skill_Template.ENTRYS.add(learnedSk);
                        }
                    }
                }

                Log.step("Skill", Skill_Template.ENTRYS.size() + " templates [from MySQL]");
            }
            // load item option temp
            {
                ItemOptionTemplate.ENTRYS = new ArrayList<>();
                rs = ps.executeQuery("SELECT * FROM `itemoption`;");
                while (rs.next()) {
                    ItemOptionTemplate temp = new ItemOptionTemplate();
                    temp.id = rs.getShort("id");
                    temp.name = rs.getString("name");
                    if (temp.name == null) temp.name = "";
                    temp.color = rs.getByte("color");
                    temp.percent = rs.getByte("percent");
                    ItemOptionTemplate.ENTRYS.add(temp);
                }
                rs.close();
                Log.step("Item Options", ItemOptionTemplate.ENTRYS.size() + " templates");
            }
            // load item fashion info
            {
                ItemFashion.ENTRYS = new ArrayList<>();
                rs = ps.executeQuery("SELECT * FROM `fashiontemplate`;");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    short icon = rs.getShort("icon");
                    String name = rs.getString("name");
                    if (name == null) name = "";
                    String info = rs.getString("info");
                    if (info == null) info = "";
                    JSONArray js = (JSONArray) JSONValue.parse(rs.getString("mwear"));
                    short[] wear = new short[js.size()];
                    for (int i = 0; i < wear.length; i++) { wear[i] = Short.parseShort(js.get(i).toString()); }
                    js = (JSONArray) JSONValue.parse(rs.getString("op"));
                    List<Option> op = new ArrayList<>();
                    for (int i = 0; i < js.size(); i++) {
                        JSONArray js2 = (JSONArray) JSONValue.parse(js.get(i).toString());
                        op.add(new Option(Byte.parseByte(js2.get(0).toString()), Integer.parseInt(js2.get(1).toString())));
                    }
                    ItemFashion.ENTRYS.add(new ItemFashion((short) id, icon, name, info, wear, op, rs.getInt("price"), rs.getInt("hsd")));
                }
                rs.close();
                Log.step("Fashion", ItemFashion.ENTRYS.size() + " templates");
            }
            // load boss
            SuperBossManager.ENTRYS = new ArrayList<>();
            java.util.Set<Integer> loadedMapIds = new java.util.HashSet<>();
            for (zabstracts.AbsBoss b : boss.BossManager.gI().getBosses()) {
                short[] loc = b.resolveSpawnLocation();
                if (loc == null || loc.length < 3) continue;
                short map_id = loc[zabstracts.AbsBoss.LOC_MAP_ID];
                short temp_x = loc[zabstracts.AbsBoss.LOC_X];
                short temp_y = loc[zabstracts.AbsBoss.LOC_Y];
                if (map_id < 0) continue;
                Zone[] map = Zone.getMapByID(map_id);
                if (map == null || map.length == 0) {
                    continue;
                }

                // Mỗi map id chỉ được 1 siêu trùm ở khu bất kì. Nếu 1 trong all khu có siêu trùm không init nữa.
                if (b.isSieuTrum()) {
                    if (loadedMapIds.contains((int) map_id) || Zone.hasActiveSuperBossInMap(map_id)) {
                        continue;
                    }
                    loadedMapIds.add((int) map_id);
                }

                // Chọn duy nhất 1 khu ngẫu nhiên trong map
                int zoneIdx = (map.length > 1) ? core.ZUtil.random(1, map.length - 1) : 0;
                Zone targetZone = map[zoneIdx];
                if (targetZone == null) continue;

                zabstracts.AbsBoss boss_temp = boss.BossManager.gI().createBoss(b.id);
                if (boss_temp == null) {
                    continue;
                }
                short[] coords = boss_temp.calculateCoordinatesInZone(targetZone, temp_x, temp_y);
                boss_temp.x = coords[0];
                boss_temp.y = coords[1];
                boss_temp.hp_max = b.getHpMax();
                boss_temp.hp = 0;
                boss_temp.level = b.getLevel();
                boss_temp.isdie = true;
                boss_temp.id_target = -1;
                int bossIndex = this.index_mob.getAndAdd(10);
                boss_temp.index = bossIndex;
                boss_temp.index_mob_save = bossIndex;
                boss_temp.boss_inf = boss_temp;
                boss_temp.isSieuTrum = boss_temp.isSieuTrum();
                boss_temp.map = targetZone;
                Mob.ENTRYS.put(boss_temp.index, boss_temp);
                targetZone.mobs.put(boss_temp.index, boss_temp);
                for (int j = 0; j < 10; j++) { // them 10slot cho 10 bac
                    Mob.ENTRYS.put((boss_temp.index + j), boss_temp);
                    targetZone.mobs.put((boss_temp.index + j), boss_temp);
                }
                // Setup skills and parameters
                boss_temp.skill = b.getSkills();
                boss_temp.time_atk = new long[boss_temp.skill.length];
                boss_temp.TopDame = new ArrayList<>();
                boss_temp.levelBoss = 1;
                boss_temp.buff = new ArrayList<>();
                SuperBossManager.ENTRYS.add(boss_temp);
            }
            Log.step("Super Bosses", SuperBossManager.ENTRYS.size() + " boss entities");

            // load boss reward
            query = "SELECT * FROM `historys` WHERE `type` = 'BOSS_REWARD';";
            rs = ps.executeQuery(query);
            while (rs.next()) {
                historys.BossRewardHistory.load(rs, SuperBossManager.top_boss_reward);
            }
            rs.close();
            // load hair
            {
                ItemHair.ENTRYS = new ArrayList<>();
                ItemHair.hashMap = new HashMap<>();
                rs = ps.executeQuery("SELECT * FROM `itemhair`;");
                while (rs.next()) {
                    try {
                        ItemHair itemHair = ItemHair.read_json_it_hair(rs);
                        ItemHair.putItem(itemHair);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                rs.close();
                Log.step("ItemHair", ItemHair.ENTRYS.size() + " templates");
            }
            // load item8
            {
                ItemTemplate8.ENTRYS = new ArrayList<>();
                try {
                    rs = ps.executeQuery("SELECT * FROM `item8`;");
                    while (rs.next()) {
                        ItemTemplate8 temp = new ItemTemplate8();
                        temp.id = rs.getShort("id");
                        temp.name = rs.getString("name");
                        if (temp.name == null) temp.name = "";
                        temp.icon = rs.getShort("icon");
                        temp.info = rs.getString("info");
                        if (temp.info == null) temp.info = "";
                        temp.beri = rs.getInt("price");
                        temp.ruby = rs.getShort("priceruby");
                        temp.istrade = rs.getByte("istrade");
                        temp.type = rs.getByte("hpmpother");
                        temp.timedelay = rs.getShort("timedelay");
                        temp.value = rs.getShort("value");
                        temp.timeactive = rs.getShort("timeactive");
                        temp.nameuse = rs.getString("nameuse");
                        if (temp.nameuse == null) temp.nameuse = "";
                        ItemTemplate8.ENTRYS.add(temp);
                    }
                    rs.close();
                } catch (Exception e) {
                    // table may not exist yet
                }
                if (ItemTemplate8.ENTRYS.isEmpty()) {
                    ItemTemplate8.initDefaultEntries();
                }
                Log.step("Item8", ItemTemplate8.ENTRYS.size() + " templates");
            }
            // load clan_icon
            {
                ClanIcon.ENTRY = new ArrayList<>();
                try {
                    rs = ps.executeQuery("SELECT * FROM `clan_icon`;");
                    while (rs.next()) {
                        ClanIcon clanIcon = new ClanIcon();
                        clanIcon.id = rs.getInt("id");
                        clanIcon.name = rs.getString("name");
                        if (clanIcon.name == null) clanIcon.name = "";
                        clanIcon.info = rs.getString("info");
                        if (clanIcon.info == null) clanIcon.info = "";
                        clanIcon.price = rs.getInt("price");
                        clanIcon.type = rs.getByte("type");
                        clanIcon.sell = rs.getBoolean("sell");
                        ClanIcon.ENTRY.add(clanIcon);
                    }
                    rs.close();
                } catch (Exception e) {
                    // table may not exist yet
                }
                if (ClanIcon.ENTRY.isEmpty()) {
                    ClanIcon.initDefaultEntries();
                }
                Log.step("ClanIcon", ClanIcon.ENTRY.size() + " icons");
            }
            // load sudo
            query = "SELECT * FROM `historys` WHERE `type` = 'SUDO';";
            rs = ps.executeQuery(query);
            while (rs.next()) {
                historys.SudoHistory.load(rs);
            }
            rs.close();
            // load sudo
            Clan.ENTRY = new ArrayList<>();
            Clan.BXH = new ArrayList<>();
            query = "SELECT * FROM `clan`;";
            Set<String> name_check = new HashSet<>();
            rs = ps.executeQuery(query);
            while (rs.next()) {
                Clan clan = new Clan();
                clan.id = rs.getShort("id");
                clan.name = rs.getString("name");
                if (clan.name == null) clan.name = "";
                JSONArray js = (JSONArray) JSONValue.parse(rs.getString("info"));
                clan.icon = Short.parseShort(js.get(0).toString());
                clan.level = Short.parseShort(js.get(1).toString());
                if (clan.level <= 0) clan.level = 1;
                clan.xp = Integer.parseInt(js.get(2).toString());
                clan.maxAttri = Short.parseShort(js.get(3).toString());
                clan.pointAttri = Short.parseShort(js.get(4).toString());
                clan.trungsinh = Byte.parseByte(js.get(5).toString());
                switch (clan.trungsinh) {
                    case 1: {
                        clan.maxAttri = 25;
                        break;
                    }
                    case 2: {
                        clan.maxAttri = 30;
                        break;
                    }
                    case 3: {
                        clan.maxAttri = 35;
                        break;
                    }
                    case 4: {
                        clan.maxAttri = 40;
                        break;
                    }
                    case 5: {
                        clan.maxAttri = 45;
                        break;
                    }
                    case 6: {
                        clan.maxAttri = 50;
                        break;
                    }
                    default: { // 0
                        clan.maxAttri = 20;
                        break;
                    }
                }
                clan.countAction = Integer.parseInt(js.get(6).toString());
                clan.ruby = Integer.parseInt(js.get(7).toString());
                clan.beri = Integer.parseInt(js.get(8).toString());
                clan.allowRequest = Byte.parseByte(js.get(9).toString());
                clan.opAttri = new short[] { 0, 0, 0, 0, 0 };
                JSONArray js2 = (JSONArray) js.get(10);
                for (int i = 0; i < clan.opAttri.length; i++) {
                    clan.opAttri[i] = Short.parseShort(js2.get(i).toString());
                }
                clan.thongbao = rs.getString("notice");
                if (clan.thongbao == null) clan.thongbao = "";
                js.clear();
                clan.chat = new ArrayList<>();
                clan.mem_request = new ArrayList<>();
                clan.members = new ArrayList<>();
                js = (JSONArray) JSONValue.parse(rs.getString("member"));
                for (int i = 0; i < js.size(); i++) {
                    JSONArray js_in = (JSONArray) js.get(i);
                    ClanMember mem = new ClanMember();
                    mem.id = (short) i;
                    mem.name = js_in.get(0).toString();
                    mem.level = Short.parseShort(js_in.get(1).toString());
                    if (mem.level <= 0) mem.level = 1;
                    mem.levelInclan = Byte.parseByte(js_in.get(2).toString());
                    mem.donate = Short.parseShort(js_in.get(3).toString());
                    mem.gopRuby = Short.parseShort(js_in.get(4).toString());
                    mem.numquest = Short.parseShort(js_in.get(5).toString());
                    mem.conghien = Integer.parseInt(js_in.get(6).toString());
                    mem.head = Short.parseShort(js_in.get(7).toString());
                    mem.hair = Short.parseShort(js_in.get(8).toString());
                    mem.hat = Short.parseShort(js_in.get(9).toString());
                    mem.clazz = Byte.parseByte(js_in.get(10).toString());
                    if (ZUtil.isNull(js_in, 11)) {
                        mem.timeJoinClan = 999;
                    } else {
                        mem.timeJoinClan = Long.parseLong(js_in.get(11).toString());
                    }
                    if (js_in.size() > 12 && !ZUtil.isNull(js_in, 12)) {
                        try {
                            mem.playerId = Integer.parseInt(js_in.get(12).toString());
                        } catch (Exception e) {
                            mem.playerId = database.DbManager.getPlayerIdByName(mem.name);
                        }
                    } else {
                        mem.playerId = database.DbManager.getPlayerIdByName(mem.name);
                    }
                    if (mem.playerId > 0) {
                        String currentName = database.DbManager.getPlayerNameById(mem.playerId);
                        if (currentName != null && !currentName.isEmpty()) {
                            mem.name = currentName;
                        }
                    }
                    //
                    boolean add = true;
                    int num_clazz = 0;
                    for (int j = 0; j < clan.members.size(); j++) {
                        if (clan.members.get(j).clazz == mem.clazz) {
                            num_clazz++;
                        }
                    }
                    if (num_clazz >= 4) {
                        System.out.println("err load clan >=4 " + clan.name + " " + mem.name);
                        add = false;
                    }
                    if (add && !name_check.contains(mem.name)) {
                        name_check.add(mem.name);
                    } else {
                        add = false;
                    }
                    if (add) {
                        clan.members.add(mem);
                    }
                }
                js.clear();
                clan.list_it = new ArrayList<>();
                js = (JSONArray) JSONValue.parse(rs.getString("item"));
                for (int i = 0; i < js.size(); i++) {
                    JSONArray js_in = (JSONArray) js.get(i);
                    ItemBag47 itemBag47 = new ItemBag47();
                    itemBag47.category = 8;
                    itemBag47.id = Short.parseShort(js_in.get(0).toString());
                    itemBag47.quant = Short.parseShort(js_in.get(1).toString());
                    clan.list_it.add(itemBag47);
                }
                clan.buff = new java.util.concurrent.CopyOnWriteArrayList<>();
                String buffStr = rs.getString("buff");
                if (buffStr != null && !buffStr.isBlank()) {
                    try {
                        JSONArray jsBuff = (JSONArray) JSONValue.parse(buffStr);
                        if (jsBuff != null) {
                            for (int i = 0; i < jsBuff.size(); i++) {
                                JSONArray js_in = (JSONArray) jsBuff.get(i);
                                if (js_in != null && js_in.size() >= 3) {
                                    clan.buff.add(new EffTemplate(
                                            Byte.parseByte(js_in.get(0).toString()),
                                            Integer.parseInt(js_in.get(1).toString()),
                                            Long.parseLong(js_in.get(2).toString())
                                    ));
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing clan buff data: " + e.getMessage());
                    }
                }
                clan.hanhtrinh = new ArrayList<>();
                try {
                    JSONArray js_ht = (JSONArray) JSONValue.parse(rs.getString("hanhtrinh"));
                    for (int i = 0; i < js_ht.size(); i++) {
                        clan.hanhtrinh.add(Integer.parseInt(js_ht.get(i).toString()));
                    }
                } catch (Exception e) {
                    clan.hanhtrinh.add(0);
                }
                Clan.add_new_clan(clan);
            }
            rs.close();
            Log.step("Clans", Clan.ENTRY.size() + " clans");

            try {
                query = "SELECT `data` FROM `historys` WHERE `type` = 'CHIEMDAO';";
                rs = ps.executeQuery(query);
                while (rs.next()) {
                    org.json.simple.JSONObject obj = (org.json.simple.JSONObject) JSONValue.parse(rs.getString("data"));
                    int id = Integer.parseInt(obj.get("id").toString());
                    int clan_id = Integer.parseInt(obj.get("clan_id").toString());
                    if (id == 1) {
                        ChiemDao.clanTop1 = Clan.get_clan_by_id(clan_id);
                    } else if (id == 2) {
                        ChiemDao.clanTop2 = Clan.get_clan_by_id(clan_id);
                    } else if (id == 3) {
                        ChiemDao.clanTop3 = Clan.get_clan_by_id(clan_id);
                    } else if (id == 4) {
                        ChiemDao.clanTop4 = Clan.get_clan_by_id(clan_id);
                    } else if (id == 5) {
                        ChiemDao.clanTop5 = Clan.get_clan_by_id(clan_id);
                    }
                }
                rs.close();
                // load chiemdao
            } catch (SQLException e) {
                System.err.println("Warning: Failed to load chiemdao from historys. Skipping.");
            }

            // load clan_hanhtrinh_template
            {
                ClanHanhTrinhIcon.ENTRY = new ArrayList<>();
                rs = ps.executeQuery("SELECT * FROM `clan_hanhtrinh_template`;");
                while (rs.next()) {
                    ClanHanhTrinhIcon temp = new ClanHanhTrinhIcon();
                    temp.id = rs.getInt("id");
                    temp.icon = rs.getShort("icon");
                    temp.name = rs.getString("name");
                    if (temp.name == null) temp.name = "";
                    temp.info = rs.getString("info");
                    if (temp.info == null) temp.info = "";
                    temp.op = new ArrayList<>();
                    JSONArray js = (JSONArray) JSONValue.parse(rs.getString("op"));
                    for (int i = 0; i < js.size(); i++) {
                        JSONArray js_in = (JSONArray) js.get(i);
                        temp.op.add(new Option(Integer.parseInt(js_in.get(0).toString()), Integer.parseInt(js_in.get(1).toString())));
                    }
                    ClanHanhTrinhIcon.ENTRY.add(temp);
                    temp.rd = rs.getInt("rd");
                }
                rs.close();
                Log.step("Clan Hanh Trinh", ClanHanhTrinhIcon.ENTRY.size() + "");
                if (Clan.ENTRY != null && ClanHanhTrinhIcon.ENTRY != null) {
                    for (Clan c : Clan.ENTRY) {
                        if (c != null && c.hanhtrinhIcon == null && c.icon > 0) {
                            for (ClanHanhTrinhIcon hti : ClanHanhTrinhIcon.ENTRY) {
                                if (hti != null && (hti.icon == c.icon || hti.id == c.icon)) {
                                    c.hanhtrinhIcon = hti;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            // load quest
            {
                Quest.ENTRY = new ArrayList<>();
                rs = ps.executeQuery("SELECT * FROM `quests` ORDER BY `id` ASC;");
                while (rs.next()) {
                    try {
                        Quest.add(rs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                rs.close();
                Quest.add_finish_quest();
                Log.step("Quests", Quest.ENTRY.size() + " quests");
            }

            // load pet template
            try {
                Pet.ENTRY = new ArrayList<>();
                rs = ps.executeQuery("SELECT * FROM `pet_template`;");
                while (rs.next()) {
                    Pet tempPet = new Pet();
                    tempPet.id = rs.getShort("id");
                    tempPet.name = rs.getString("name");
                    if (tempPet.name == null) tempPet.name = "";
                    tempPet.type = rs.getByte("type");
                    tempPet.icon = rs.getShort("icon");
                    tempPet.frame = rs.getShort("frame");
                    tempPet.op = new ArrayList<>();
                    JSONArray js = (JSONArray) JSONValue.parse(rs.getString("op"));
                    for (int i = 0; i < js.size(); i++) {
                        JSONArray js_in = (JSONArray) js.get(i);
                        tempPet.op.add(new Option(Byte.parseByte(js_in.get(0).toString()), Integer.parseInt(js_in.get(1).toString())));
                    }
                    Pet.ENTRY.add(tempPet);
                }
                rs.close();
                Log.step("Pets", Pet.ENTRY.size()+"");
            } catch (SQLException e) {
                System.err.println("Warning: Table `pet_template` does not exist or failed to load. Skipping.");
            }

            try {
                activities.DanhHieu.load(conn);
                Log.step("DanhHieu", activities.DanhHieu.ENY.size() + "");
            } catch (Exception e) {
                Log.error("Manager", "Error loading DanhHieu: " + e.getMessage());
            }

            try {
                activities.Market.load(conn);
            } catch (Exception e) {
                Log.error("Manager", "Error loading Market: " + e.getMessage());
            }

            // load store_data
            try {
                zabstracts.AbsShop.storeItems.clear();
                rs = ps.executeQuery("SELECT * FROM `store_data`;");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int storeId = rs.getInt("store_id");
                    int itemId = rs.getInt("item_id");
                    int itemCategory = rs.getInt("item_category");
                    int priceCoin = 0;
                    try { priceCoin = rs.getInt("price_coin"); } catch (Exception ignore) {}
                    int priceRuby = 0;
                    try { priceRuby = rs.getInt("price_ruby"); } catch (Exception ignore) {}
                    int reqLevel = 0;
                    try { reqLevel = rs.getInt("req_level"); } catch (Exception ignore) {}
                    int eventId = 0;
                    try { eventId = rs.getInt("event_id"); } catch (Exception ignore) {}
                    int type2 = -1;
                    try { type2 = rs.getInt("type2"); } catch (Exception ignore) {}

                    store.StoreItem storeItem = new store.StoreItem(
                        id, storeId, itemId, itemCategory, priceCoin, priceRuby, reqLevel, eventId, type2
                    );
                    zabstracts.AbsShop.storeItems.add(storeItem);

                    if (storeItem.itemCategory == 3) {
                        ItemTemplate3 it3 = ItemTemplate3.get_it_by_id(storeItem.itemId);
                        if (it3 != null) {
                            it3.beri = storeItem.priceCoin;
                            it3.ruby = storeItem.priceRuby;
                        }
                        if (storeItem.type2 > -1) {
                            ItemTemplate3 it3_type2 = ItemTemplate3.get_it_by_id(storeItem.type2);
                            if (it3_type2 != null) {
                                it3_type2.beri = storeItem.priceCoin;
                                it3_type2.ruby = storeItem.priceRuby;
                            }
                        }
                    } else if (storeItem.itemCategory == 4) {
                        ItemTemplate4 it4 = ItemTemplate4.get_it_by_id(storeItem.itemId);
                        if (it4 != null) {
                            it4.beri = storeItem.priceCoin;
                            it4.ruby = (short) storeItem.priceRuby;
                        }
                        if (storeItem.type2 > -1) {
                            ItemTemplate4 it4_type2 = ItemTemplate4.get_it_by_id(storeItem.type2);
                            if (it4_type2 != null) {
                                it4_type2.beri = storeItem.priceCoin;
                                it4_type2.ruby = (short) storeItem.priceRuby;
                            }
                        }
                    } else if (storeItem.itemCategory == 7) {
                        ItemTemplate7 it7 = ItemTemplate7.get_it_by_id(storeItem.itemId);
                        if (it7 != null) {
                            it7.price = storeItem.priceCoin;
                            it7.priceruby = (short) storeItem.priceRuby;
                        }
                        if (storeItem.type2 > -1) {
                            ItemTemplate7 it7_type2 = ItemTemplate7.get_it_by_id(storeItem.type2);
                            if (it7_type2 != null) {
                                it7_type2.price = storeItem.priceCoin;
                                it7_type2.priceruby = (short) storeItem.priceRuby;
                            }
                        }
                    } else if (storeItem.itemCategory == 8) {
                        ItemTemplate8 it8 = ItemTemplate8.get_it_by_id(storeItem.itemId);
                        if (it8 != null) {
                            it8.beri = storeItem.priceCoin;
                            it8.ruby = (short) storeItem.priceRuby;
                        }
                        if (storeItem.type2 > -1) {
                            ItemTemplate8 it8_type2 = ItemTemplate8.get_it_by_id(storeItem.type2);
                            if (it8_type2 != null) {
                                it8_type2.beri = storeItem.priceCoin;
                                it8_type2.ruby = (short) storeItem.priceRuby;
                            }
                        }
                    }
                }
                rs.close();
                template.ItemSell.init();
                network.Session.clearStaticDataCache();
                Log.stepLast("Store Data", zabstracts.AbsShop.storeItems.size() + " items");
                //Log.success("Templates", "All game templates loaded successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Warning: Table `store_data` does not exist or failed to load. Skipping.");
            }
            try {
                map.zones.WorldWar.loadData();
            } catch (Exception e) {
                System.err.println("Warning: Failed to load WorldWar data at server startup.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void load_config() throws IOException {
        java.util.Properties properties = new java.util.Properties();
        try (java.io.FileInputStream fis = new java.io.FileInputStream("config.properties")) {
            properties.load(fis);
            System.out.println("Loaded config.properties successfully.");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Config file config.properties not found!");
            System.exit(0);
        }

        this.server_port = Integer.parseInt(properties.getProperty("port", "2239"));
        this.debug = Boolean.parseBoolean(properties.getProperty("debug", "false"));
        this.mysql_host = properties.getProperty("mysql-host", "127.0.0.1");
        this.mysql_user = properties.getProperty("mysql-user", "root");
        this.mysql_pass = properties.getProperty("mysql-password", "12345678");
        this.mysql_database = properties.getProperty("mysql-database", "database");
        this.exp = Integer.parseInt(properties.getProperty("exp", "1"));
        this.ZEVENT_ID = 13;
        this.server_admin = Boolean.parseBoolean(properties.getProperty("serveradmin", "false"));
        this.cache_max_ram_entries = Integer.parseInt(properties.getProperty("cache-max-ram-entries", "-1"));
        this.cache_enable_ssd = Boolean.parseBoolean(properties.getProperty("cache-enable-ssd", "true"));
        database.TemplateCache.setEnabled(this.cache_enable_ssd);
        this.auto_restart = Boolean.parseBoolean(properties.getProperty("auto-restart", "true"));
        this.auto_baotri_midnight = Boolean.parseBoolean(properties.getProperty("auto-baotri-midnight", "true"));
        String propStatus = properties.getProperty("server_status", properties.getProperty("status", null));
        if (propStatus != null && !propStatus.trim().isEmpty()) {
            this.server_status = propStatus.trim();
        }
        try {
            this.trade_min_online_hours = Integer.parseInt(properties.getProperty("trade-min-online-hours", properties.getProperty("trade_min_online_hours", "48")));
        } catch (Exception ignored) {}

        // Cấu hình kiểm tra key bản quyền phiên bản MOD & Chặn bản gốc HaiTacZ
        boolean checkClientKey = Boolean.parseBoolean(properties.getProperty("check_client_key", properties.getProperty("check-client-key", "true")));
        String clientSecretKey = properties.getProperty("client_secret_key", properties.getProperty("client-secret-key", "HTTH_CLIENT_NAMTR0002_SECURE_KEY_V4_2026"));
        network.KeyServerValidator.setServerCheckEnabled(checkClientKey);
        network.KeyServerValidator.setLocalSecretKey(clientSecretKey);
    }

    public void close() {
        stop_service();
    }

    public void chatKTG(Player p, String text) throws IOException {
        activities.Chat.requestWorldChat(p, text);
    }

    public void chatKTG(int type, String text, int color) {
        try {
            Message m = new Message(-31);
            m.writer().writeByte(type);
            m.writer().writeUTF(text);
            m.writer().writeByte(color);
            m.writer().writeShort(-1);
            for (map.Map mapall : map.MapManager.getInstance().getMaps()) {
                for (Zone map : mapall.zones) {
                    for (int i = 0; i < map.players.size(); i++) {
                        Player p0 = map.players.get(i);
                        if (p0.conn != null) {
                            p0.addmsg(m);
                        }
                    }
                }
            }
            List<Zone> mapplus = Zone.get_map_plus();
            for (int i = 0; i < mapplus.size(); i++) {
                for (int i12 = 0; i12 < mapplus.get(i).players.size(); i12++) {
                    Player p0 = mapplus.get(i).players.get(i12);
                    if (p0.conn != null) {
                        p0.addmsg(m);
                    }
                }
            }
            m.cleanup();
        } catch (IOException e) {

        }
    }

    public void Notify(Player p, int type, String text, int color) {
        try {
            Message m = new Message(-31);
            m.writer().writeByte(type);
            m.writer().writeUTF(text);
            m.writer().writeByte(color);
            m.writer().writeShort(-1);
            p.msgs.add(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }

    public TaiXiu TaiXiu() {
        return tx;
    }

    public static List<ShopLimitItemTemplate> loadShopItems(java.sql.Statement st, String tableName) throws SQLException {
        List<ShopLimitItemTemplate> list = new ArrayList<>();
        try (ResultSet rs = st.executeQuery("SELECT * FROM `" + tableName + "` ORDER BY `type`;")) {
            while (rs.next()) {
                ShopLimitItemTemplate temp = new ShopLimitItemTemplate();
                temp.id = rs.getShort("id");
                temp.type = rs.getByte("type");
                temp.point = rs.getInt("point");
                temp.info = rs.getString("info");
                temp.limit = rs.getInt("limit");
                temp.limit_data = new java.util.HashMap<>();
                list.add(temp);
            }
        }
        return list;
    }

    public static void applyThanTrangSkillDefaults(Skill_Template sk) {
        if (sk == null) return;
        if (sk.op == null) sk.op = new ArrayList<>();
        if (sk.op.isEmpty()) {
            switch (sk.ID) {
                case 4001: // Hỏa Diễm Thần Quyền X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(5, 150));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(14, 300));
                    sk.op.add(new Option(29, 500));
                    break;
                case 4002: // Đại Phún Hỏa Volcano V
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(2, 350));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(14, 300));
                    sk.op.add(new Option(29, 500));
                    break;
                case 4003: // Kỷ Băng Hà Tuyết Đối X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(3, 350));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(28, 10));
                    sk.op.add(new Option(29, 500));
                    break;
                case 4004: // Bát Xích Quỳnh Khúc Ngọc X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(7, 150));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(9, 200));
                    sk.op.add(new Option(29, 500));
                    break;
                case 4005: // Hắc Ám Thôn Phệ Vô Tận X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(2, 350));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(29, 500));
                    sk.op.add(new Option(30, 50));
                    break;
                case 4006: // 200 Triệu Volt Thần Lôi V
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(5, 150));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(28, 10));
                    sk.op.add(new Option(29, 500));
                    break;
                case 4007: // Hải Chấn Toái Địa Cầu V
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(28, 10));
                    sk.op.add(new Option(29, 500));
                    sk.op.add(new Option(30, 50));
                    break;
                case 4008: // ROOM Gamma Knife V
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(28, 10));
                    sk.op.add(new Option(29, 500));
                    sk.op.add(new Option(30, 50));
                    break;
                case 4009: // Từ Trường Bộc Phá Đại Pháo X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(28, 10));
                    sk.op.add(new Option(29, 500));
                    sk.op.add(new Option(30, 50));
                    break;
                case 4010: // Cổ Độc Phán Quyết Venom X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(28, 10));
                    sk.op.add(new Option(29, 500));
                    sk.op.add(new Option(30, 50));
                    break;
                case 4011: // Mũi Tên Mê Hoặc Thạch Hóa X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(28, 10));
                    sk.op.add(new Option(29, 500));
                    sk.op.add(new Option(30, 50));
                    break;
                case 4012: // Phượng Hoàng Bất Tử Bộc Phá X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(3, 500));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(29, 500));
                    sk.op.add(new Option(30, 50));
                    break;
                case 4013: // Đại Phật Sóng Xung Kích X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(2, 500));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(28, 10));
                    sk.op.add(new Option(29, 500));
                    break;
                case 4014: // Bát Quái Cửu Long Thiên X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(2, 500));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(28, 10));
                    sk.op.add(new Option(29, 500));
                    break;
                case 4015: // Long Trảo Viêm Long Toái Địa X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(5, 150));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(14, 300));
                    sk.op.add(new Option(29, 500));
                    break;
                case 4016: // Vận Thạch Thiên Giáng X
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(28, 10));
                    sk.op.add(new Option(29, 500));
                    sk.op.add(new Option(30, 50));
                    break;
                default:
                    sk.op.add(new Option(1, 350));
                    sk.op.add(new Option(10, 350));
                    sk.op.add(new Option(13, 400));
                    sk.op.add(new Option(29, 500));
                    break;
            }
        }
        if (sk.idEffSpec == 0) {
            switch (sk.ID) {
                case 4001:
                    sk.idEffSpec = 2; // Thiêu đốt (Burn)
                    sk.perEffSpec = 45;
                    sk.timeEffSpec = 30;
                    break;
                case 4002:
                    sk.idEffSpec = 2; // Thiêu đốt Nham Thạch (Lava Burn)
                    sk.perEffSpec = 60;
                    sk.timeEffSpec = 35;
                    break;
                case 4003:
                    sk.idEffSpec = 3; // Đóng băng (Freeze)
                    sk.perEffSpec = 55;
                    sk.timeEffSpec = 30;
                    break;
                case 4004:
                    sk.idEffSpec = 4; // Mù lòa (Blind)
                    sk.perEffSpec = 55;
                    sk.timeEffSpec = 30;
                    break;
                case 4005:
                    sk.idEffSpec = 8; // Trói chân (Root / Chặn di chuyển)
                    sk.perEffSpec = 100;
                    sk.timeEffSpec = 35;
                    break;
                case 4006:
                    sk.idEffSpec = 6; // Tê liệt (Shock)
                    sk.perEffSpec = 60;
                    sk.timeEffSpec = 30;
                    break;
                case 4007: // Hải Chấn Toái Địa Cầu (Choáng Trấn Thiên)
                    sk.idEffSpec = 1;
                    sk.perEffSpec = 65;
                    sk.timeEffSpec = 35;
                    break;
                case 4008:
                    sk.idEffSpec = 7; // Nội thương (Bleed)
                    sk.perEffSpec = 55;
                    sk.timeEffSpec = 35;
                    break;
                case 4009:
                    sk.idEffSpec = 1; // Choáng Từ Tính (Magnetic Stun)
                    sk.perEffSpec = 55;
                    sk.timeEffSpec = 30;
                    break;
                case 4010:
                    sk.idEffSpec = 8; // Trúng độc (Poison)
                    sk.perEffSpec = 60;
                    sk.timeEffSpec = 40;
                    break;
                case 4011:
                    sk.idEffSpec = 9; // Hóa đá (Petrify)
                    sk.perEffSpec = 55;
                    sk.timeEffSpec = 35;
                    break;
                case 4012:
                    sk.idEffSpec = 2; // Lam Hỏa Thiêu Đốt (Blue Fire)
                    sk.perEffSpec = 50;
                    sk.timeEffSpec = 30;
                    break;
                case 4013:
                    sk.idEffSpec = 1; // Sóng Xung Kích Choáng (Shockwave Stun)
                    sk.perEffSpec = 55;
                    sk.timeEffSpec = 30;
                    break;
                case 4014:
                    sk.idEffSpec = 1; // Long Lôi Choáng (Dragon Stun)
                    sk.perEffSpec = 60;
                    sk.timeEffSpec = 35;
                    break;
                case 4015:
                    sk.idEffSpec = 2; // Long Trảo Hỏa Diễm (Dragon Fire Burn)
                    sk.perEffSpec = 55;
                    sk.timeEffSpec = 30;
                    break;
                case 4016:
                    sk.idEffSpec = 1; // Trọng Lực Đè Nặng (Gravity Stun)
                    sk.perEffSpec = 60;
                    sk.timeEffSpec = 35;
                    break;
                default:
                    sk.idEffSpec = 1;
                    sk.perEffSpec = 50;
                    sk.timeEffSpec = 30;
                    break;
            }
        }
    }

    public String get_name_clazz(byte clazz) {
        switch (clazz) {
            case 0: return "Võ sĩ";
            case 1: return "Kiếm khách";
            case 2: return "Đầu bếp";
            case 3: return "Hoa tiêu";
            case 4: return "Xạ thủ";
            default: return "Chưa chọn";
        }
    }
}
