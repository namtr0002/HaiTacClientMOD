package activities;

import model.Player;
import model.DeTu;
import core.Manager;

import core.Log;
import core.ZUtil;
import zinterfaces.menus.AdminSystemMenu;
import map.Zone;
import map.zones.DauTruongTuDo;
import map.zones.WorldWar;
import map.zones.TranChienKhongLo;
import boss.BossTheGioi;
import map.zones.BaoVePhaoDai;
import bot.botplayer.BotPlayerManager;
import bot.botplayer.BotPlayerReal;
import skill.Skill_info;
import template.Item_wear;
import template.ItemTemplate3;
import template.Option;
import zinterfaces.iMenuDymanic;
import network.SessionManager;
import event.EventManager;
import event.SuKienHalloween;
import map.zones.TranChienLon;
import template.GiftBox;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import rank.TopWorldWar;
import rank.TopChiemDao;
import rank.TopDauTruongTuDo;
import rank.TopSuperBoss;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.YesNoDialog;

/**
 * TimedDungeonManager — Quản Lý Toàn Bộ Phó Bản Thời Gian & Công Cụ Admin Đỉnh Cao Qua Thẻ Hệ Thống (Item 872).
 */
public class TimedDungeonManager {

    private static final TimedDungeonManager instance = new TimedDungeonManager();

    public static TimedDungeonManager gI() {
        return instance;
    }

    public static class ScheduleConfig {
        public static final int TIMING_MODE_REAL_TIME = 0;
        public static final int TIMING_MODE_SERVER_CYCLE = 1;

        public static final int STATE_IDLE = 0;
        public static final int STATE_RUNNING = 1;

        public String id;
        public String name;
        public boolean isEnabled;
        public boolean isPrivateMap;
        public int timingMode = TIMING_MODE_SERVER_CYCLE; // 0 = Giờ thực, 1 = Chu kỳ từ lúc server init
        public int runDurationMinutes = 25; // Thời gian diễn ra mỗi đợt (phút)
        public int cooldownMinutes = 10;    // Thời gian chờ đợt sau (phút)
        public int[] daysOfWeek = null;     // null/empty = mở hàng ngày; {1, 3, 5, 7} = T2, T4, T6, CN; {2, 4, 6, 7} = T3, T5, T7, CN
        public int startHour;
        public int startMinute;
        public int endHour;
        public int endMinute;
        public String rewardInfo;

        // Trạng thái chu kỳ động & Token định danh
        public int state = STATE_IDLE;
        public long nextStateChangeTime = 0;
        public int roundIndex = 0;
        public String currentTokenId = "";
        // Các khung giờ mở bổ sung ngoài startHour/endHour mặc định
        // Format: {{startH, startM, endH, endM}, ...}
        public int[][] extraSlots = null;

        public ScheduleConfig(String id, String name, boolean isEnabled, boolean isPrivateMap, int timingMode, int runDurationMinutes, int cooldownMinutes, int startHour, int startMinute, int endHour, int endMinute, String rewardInfo) {
            this(id, name, isEnabled, isPrivateMap, timingMode, runDurationMinutes, cooldownMinutes, null, startHour, startMinute, endHour, endMinute, rewardInfo);
        }

        public ScheduleConfig(String id, String name, boolean isEnabled, boolean isPrivateMap, int timingMode, int runDurationMinutes, int cooldownMinutes, int[] daysOfWeek, int startHour, int startMinute, int endHour, int endMinute, String rewardInfo) {
            this.id = id;
            this.name = name;
            this.isEnabled = isEnabled;
            this.isPrivateMap = isPrivateMap;
            this.timingMode = timingMode;
            this.runDurationMinutes = runDurationMinutes;
            this.cooldownMinutes = cooldownMinutes;
            this.daysOfWeek = daysOfWeek;
            this.startHour = startHour;
            this.startMinute = startMinute;
            this.endHour = endHour;
            this.endMinute = endMinute;
            this.rewardInfo = rewardInfo;
            this.state = STATE_IDLE;
            this.nextStateChangeTime = System.currentTimeMillis() + 5000L; // Bắt đầu 5s sau khi init
        }

        public String getTimeRangeString() {
            if (timingMode == TIMING_MODE_SERVER_CYCLE) {
                String stateStr = (state == STATE_RUNNING) ? "Đang chạy" : "Đang chờ";
                long remainSec = Math.max(0, (nextStateChangeTime - System.currentTimeMillis()) / 1000);
                return "Chu kỳ: " + runDurationMinutes + "p chạy / " + cooldownMinutes + "p chờ (" + stateStr + " " + (remainSec / 60) + "m" + (remainSec % 60) + "s) [Đợt #" + roundIndex + "]";
            } else {
                StringBuilder sb = new StringBuilder();
                if (daysOfWeek == null || daysOfWeek.length == 0 || daysOfWeek.length >= 7) {
                    sb.append("Hàng ngày: ");
                } else {
                    sb.append("Thứ ");
                    for (int i = 0; i < daysOfWeek.length; i++) {
                        int d = daysOfWeek[i];
                        sb.append(d == 7 ? "CN" : (d + 1));
                        if (i < daysOfWeek.length - 1) sb.append(", ");
                    }
                    sb.append(": ");
                }
                sb.append(String.format("%02d:%02d -> %02d:%02d", startHour, startMinute, endHour, endMinute));
                if (extraSlots != null) {
                    for (int[] slot : extraSlots) {
                        if (slot != null && slot.length >= 4) {
                            sb.append(" & ").append(String.format("%02d:%02d -> %02d:%02d", slot[0], slot[1], slot[2], slot[3]));
                        }
                    }
                }
                String stateStr = (state == STATE_RUNNING) ? "Đang mở" : "Chưa mở";
                long remainSec = Math.max(0, (nextStateChangeTime - System.currentTimeMillis()) / 1000);
                if (remainSec > 0) {
                    if (state == STATE_RUNNING) {
                        sb.append(" (").append(stateStr).append(" - Còn ").append(remainSec / 60).append("p").append(remainSec % 60).append("s)");
                    } else {
                        sb.append(" (").append(stateStr).append(" - Mở sau ").append(remainSec / 3600).append("h").append((remainSec % 3600) / 60).append("m)");
                    }
                } else {
                    sb.append(" (").append(stateStr).append(")");
                }
                return sb.toString();
            }
        }
    }

    private final Map<String, ScheduleConfig> configs = new ConcurrentHashMap<>();
    private boolean globalMasterSwitch = true;
    private static final String SCHEDULE_CACHE_FILE = "./.cache/dungeon_schedules.json";
    private long lastCacheSaveTime = 0;

    public TimedDungeonManager() {
        applyStandardSchedules();
        loadCache();
    }

    private void initDefaultSchedules() {
        applyStandardSchedules();
    }

    /**
     * Tự động áp dụng cấu hình chuẩn:
     * - Toàn bộ phó bản chạy theo khung giờ thực cố định (không chạy xoay vòng liên tục 24/24).
     */
    public synchronized void applyStandardSchedules() {
        Log.info("TimedDungeonManager", ">>> Cấu hình hệ thống phó bản thời gian: Chạy theo lịch giờ thực chuẩn cố định (Không xoay vòng test)");

        // 1. Đấu Trường Tự Do: Hàng ngày, Trưa 12:30 - 13:30 & Tối 20:00 - 21:00
        addMultiSlot("DAU_TRUONG_TU_DO", "Đấu Trường Tự Do", false, null,
                new int[][]{{12, 30, 13, 30}, {20, 0, 21, 0}}, "Quà Top 1-10 + Quà tham gia");

        // 2. Boss Thế Giới: Hàng ngày, Trưa 12:00 - 12:30 & Tối 21:00 - 21:30
        addMultiSlot("BOSS_THE_GIOI", "Boss Thế Giới", false, null,
                new int[][]{{12, 0, 12, 30}, {21, 0, 21, 30}}, "Rớt Trái Ác Quỷ Thượng Cấp");

        // 3. Đại Chiến Thế Giới: Hàng ngày, Tối 19:00 - 20:00
        addMultiSlot("WORLD_WAR", "Đại Chiến Thế Giới", true, null,
                new int[][]{{19, 0, 20, 0}}, "10,000 Ruby + 500k Beri phe Thắng");

        // 4. Phó Bản PVP Băng: Hàng ngày, Trưa 11:30 - 13:30 & Tối 18:30 - 22:30
        addMultiSlot("PVP_BANG", "Phó Bản PVP Băng", true, null,
                new int[][]{{11, 30, 13, 30}, {18, 30, 22, 30}}, "2000 EXP Băng & Danh Hiệu Băng");

        // 5. Chiếm Đảo Bang Hội: Thứ 2, 4, 6, Chủ Nhật (1, 3, 5, 7), Tối 19:30 - 20:30
        addMultiSlot("CHIEM_DAO", "Chiếm Đảo Bang Hội", true, new int[]{1, 3, 5, 7},
                new int[][]{{19, 30, 20, 30}}, "500k Beri + 20 Đá Cường Hóa");

        // 6. Bảo Vệ Pháo Đài: Thứ 2, 4, 6, Chủ Nhật (1, 3, 5, 7), Tối 21:00 - 22:00
        addMultiSlot("BAO_VE_PHAO_DAI", "Bảo Vệ Pháo Đài", true, new int[]{1, 3, 5, 7},
                new int[][]{{21, 0, 22, 0}}, "Điểm Pháo Đài & Quà Băng");

        // 7. Trận Chiến Khổng Lồ: Thứ 3, 5, 7, Chủ Nhật (2, 4, 6, 7), Tối 21:00 - 22:00
        addMultiSlot("TRAN_CHIEN_KHONG_LO", "Trận Chiến Khổng Lồ", true, new int[]{2, 4, 6, 7},
                new int[][]{{21, 0, 22, 0}}, "Thưởng Ruby & EXP Băng");

        // 8. Thủ Lĩnh Biển Khơi: Thứ 3, 5, 7, Chủ Nhật (2, 4, 6, 7), Trưa 11:30 - 12:30 & Tối 18:30 - 19:30
        addMultiSlot("THU_LINH_BIEN_KHOI", "Thủ Lĩnh Biển Khơi", true, new int[]{2, 4, 6, 7},
                new int[][]{{11, 30, 12, 30}, {18, 30, 19, 30}}, "Huy Hiệu Hành Trình & Quà 4 Vùng Biển");
    }

    /**
     * Lưu trạng thái tiến trình và mốc thời gian chu kỳ của toàn bộ phó bản vào .cache/dungeon_schedules.json
     */
    public synchronized void saveCache() {
        try {
            File dir = new File("./.cache");
            if (!dir.exists()) dir.mkdirs();
            File file = new File(SCHEDULE_CACHE_FILE);

            JSONObject root = new JSONObject();
            root.put("lastSavedTime", System.currentTimeMillis());
            root.put("globalMasterSwitch", globalMasterSwitch);
            root.put("serverPhase", Manager.gI().getServerStatus());

            JSONArray arr = new JSONArray();
            for (ScheduleConfig cfg : configs.values()) {
                if (cfg == null) continue;
                JSONObject item = new JSONObject();
                item.put("id", cfg.id);
                item.put("state", cfg.state);
                item.put("nextStateChangeTime", cfg.nextStateChangeTime);
                item.put("roundIndex", cfg.roundIndex);
                item.put("currentTokenId", cfg.currentTokenId != null ? cfg.currentTokenId : "");
                item.put("timingMode", cfg.timingMode);
                item.put("runDurationMinutes", cfg.runDurationMinutes);
                item.put("cooldownMinutes", cfg.cooldownMinutes);
                if (cfg.daysOfWeek != null) {
                    JSONArray dArr = new JSONArray();
                    for (int d : cfg.daysOfWeek) dArr.add(d);
                    item.put("daysOfWeek", dArr);
                }
                arr.add(item);
            }
            root.put("schedules", arr);

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                writer.write(root.toJSONString());
            }
            lastCacheSaveTime = System.currentTimeMillis();
        } catch (Exception e) {
            Log.error("TimedDungeonManager", "Failed to save schedule cache: " + e.getMessage());
        }
    }

    /**
     * Nạp và đồng bộ hóa chuẩn xác mốc thời gian thực tế đã trôi qua từ cache SSD.
     * Đảm bảo khi thoát ra vào lại hoặc bảo trì server, thời gian đếm ngược chính xác.
     */
    public synchronized void loadCache() {
        try {
            File file = new File(SCHEDULE_CACHE_FILE);
            if (!file.exists()) {
                Log.info("TimedDungeonManager", "No schedule cache file found, starting with initial timers.");
                return;
            }

            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                Object obj = JSONValue.parse(reader);
                if (!(obj instanceof JSONObject)) return;

                JSONObject root = (JSONObject) obj;
                if (root.containsKey("globalMasterSwitch")) {
                    this.globalMasterSwitch = Boolean.parseBoolean(root.get("globalMasterSwitch").toString());
                }

                String savedPhase = root.containsKey("serverPhase") ? root.get("serverPhase").toString().trim() : "";
                String currentPhase = Manager.gI().getServerStatus();
                boolean phaseChanged = (!savedPhase.isEmpty() && !savedPhase.equalsIgnoreCase(currentPhase));

                if (phaseChanged) {
                    Log.info("TimedDungeonManager", "Server phase changed from [" + savedPhase + "] to [" + currentPhase + "]. Automatically reapplying standard schedules.");
                    applyStandardSchedules();
                    saveCache();
                    return;
                }

                JSONArray arr = (JSONArray) root.get("schedules");
                if (arr == null) return;

                long now = System.currentTimeMillis();
                int restoredCount = 0;

                for (Object o : arr) {
                    if (!(o instanceof JSONObject)) continue;
                    JSONObject item = (JSONObject) o;
                    String id = (String) item.get("id");
                    if (id == null) continue;

                    ScheduleConfig cfg = configs.get(id);
                    if (cfg == null) continue;

                    int savedState = item.containsKey("state") ? Integer.parseInt(item.get("state").toString()) : ScheduleConfig.STATE_IDLE;
                    long savedNext = item.containsKey("nextStateChangeTime") ? Long.parseLong(item.get("nextStateChangeTime").toString()) : 0L;
                    int savedRound = item.containsKey("roundIndex") ? Integer.parseInt(item.get("roundIndex").toString()) : 0;
                    String savedToken = (String) item.get("currentTokenId");

                    if (cfg.timingMode == ScheduleConfig.TIMING_MODE_SERVER_CYCLE) {
                        long runDurationMs = (long) cfg.runDurationMinutes * 60_000L;
                        long cooldownMs = (long) cfg.cooldownMinutes * 60_000L;

                        if (now < savedNext && savedNext > 0) {
                            cfg.state = savedState;
                            cfg.nextStateChangeTime = savedNext;
                            cfg.roundIndex = savedRound;
                            cfg.currentTokenId = (savedToken != null && !savedToken.isEmpty()) ? savedToken : ZUtil.generateMD5Token(cfg.id + "_" + cfg.roundIndex + "_" + savedNext);
                        } else if (savedNext > 0) {
                            int currentState = savedState;
                            int currentRound = savedRound;
                            long timeCursor = savedNext;

                            while (timeCursor <= now) {
                                if (currentState == ScheduleConfig.STATE_RUNNING) {
                                    currentState = ScheduleConfig.STATE_IDLE;
                                    timeCursor += cooldownMs;
                                } else {
                                    currentState = ScheduleConfig.STATE_RUNNING;
                                    currentRound++;
                                    timeCursor += runDurationMs;
                                }
                            }

                            cfg.state = currentState;
                            cfg.roundIndex = currentRound;
                            cfg.nextStateChangeTime = timeCursor;
                            cfg.currentTokenId = ZUtil.generateMD5Token(cfg.id + "_" + cfg.roundIndex + "_" + timeCursor);
                        }
                        restoredCount++;
                    }
                }
            }
        } catch (Exception e) {
            Log.error("TimedDungeonManager", "Error loading schedule cache: " + e.getMessage());
        }
    }

    /**
     * Tạo ScheduleConfig thực cho một phó bản có thể có nhiều khung giờ trong ngày.
     * slots: {{startH, startM, endH, endM}, ...}
     */
    private void addMultiSlot(String id, String name, boolean isPrivate, int[] daysOfWeek, int[][] slots, String reward) {
        if (slots == null || slots.length == 0) return;
        int[] s0 = slots[0];
        ScheduleConfig cfg = new ScheduleConfig(
                id, name, true, isPrivate,
                ScheduleConfig.TIMING_MODE_REAL_TIME,
                0, 0, daysOfWeek,
                s0[0], s0[1], s0[2], s0[3],
                reward
        );
        if (slots.length > 1) {
            cfg.extraSlots = new int[slots.length - 1][];
            for (int i = 1; i < slots.length; i++) {
                cfg.extraSlots[i - 1] = slots[i];
            }
        }
        configs.put(id, cfg);
    }

    public Map<String, ScheduleConfig> getConfigs() {
        return configs;
    }

    public boolean isGlobalMasterSwitch() {
        return globalMasterSwitch;
    }

    public void setGlobalMasterSwitch(boolean value) {
        this.globalMasterSwitch = value;
    }

    public boolean isWithinRealTimeRange(ScheduleConfig config) {
        if (!globalMasterSwitch || config == null || !config.isEnabled) {
            return false;
        }

        Calendar cal = Calendar.getInstance();
        int curHour = cal.get(Calendar.HOUR_OF_DAY);
        int curMin = cal.get(Calendar.MINUTE);
        int curSec = cal.get(Calendar.SECOND);
        int nowDay = org.joda.time.DateTime.now().getDayOfWeek(); // 1=Mon .. 7=Sun
        int nowTotalMin = curHour * 60 + curMin;

        // 1. Kiểm tra ngày trong tuần
        boolean dayMatch = true;
        if (config.daysOfWeek != null && config.daysOfWeek.length > 0) {
            dayMatch = false;
            for (int d : config.daysOfWeek) {
                if (d == nowDay) {
                    dayMatch = true;
                    break;
                }
            }
        }

        // Tạo danh sách tất cả các slots: {{sH, sM, eH, eM}, ...}
        List<int[]> allSlots = new ArrayList<>();
        allSlots.add(new int[]{config.startHour, config.startMinute, config.endHour, config.endMinute});
        if (config.extraSlots != null) {
            for (int[] es : config.extraSlots) {
                if (es != null && es.length >= 4) {
                    allSlots.add(es);
                }
            }
        }

        if (dayMatch) {
            for (int[] slot : allSlots) {
                int sH = slot[0], sM = slot[1], eH = slot[2], eM = slot[3];
                int startTotal = sH * 60 + sM;
                int endTotal = eH * 60 + eM;
                if (nowTotalMin >= startTotal && nowTotalMin < endTotal) {
                    // Đang trong khung giờ mở!
                    long endMillis = cal.getTimeInMillis() + (long) (endTotal - nowTotalMin) * 60_000L - (curSec * 1000L);
                    config.nextStateChangeTime = endMillis;
                    return true;
                }
            }
        }

        // Nếu không trong slot mở, tính thời gian mở tiếp theo
        long nextOpenMillis = calculateNextOpenTime(config, cal, nowDay, nowTotalMin, curSec, allSlots);
        if (nextOpenMillis > 0) {
            config.nextStateChangeTime = nextOpenMillis;
        }
        return false;
    }

    private long calculateNextOpenTime(ScheduleConfig config, Calendar cal, int nowDay, int nowTotalMin, int curSec, List<int[]> allSlots) {
        boolean todayAllowed = (config.daysOfWeek == null || config.daysOfWeek.length == 0);
        if (!todayAllowed && config.daysOfWeek != null) {
            for (int d : config.daysOfWeek) {
                if (d == nowDay) { todayAllowed = true; break; }
            }
        }

        if (todayAllowed) {
            int minLaterStart = Integer.MAX_VALUE;
            for (int[] slot : allSlots) {
                int sTotal = slot[0] * 60 + slot[1];
                if (sTotal > nowTotalMin && sTotal < minLaterStart) {
                    minLaterStart = sTotal;
                }
            }
            if (minLaterStart != Integer.MAX_VALUE) {
                return cal.getTimeInMillis() + (long) (minLaterStart - nowTotalMin) * 60_000L - (curSec * 1000L);
            }
        }

        // Tìm ngày mở tiếp theo trong vòng 7 ngày tới
        for (int dayOffset = 1; dayOffset <= 7; dayOffset++) {
            int targetDay = ((nowDay - 1 + dayOffset) % 7) + 1;
            boolean targetAllowed = (config.daysOfWeek == null || config.daysOfWeek.length == 0);
            if (!targetAllowed && config.daysOfWeek != null) {
                for (int d : config.daysOfWeek) {
                    if (d == targetDay) { targetAllowed = true; break; }
                }
            }
            if (targetAllowed && !allSlots.isEmpty()) {
                int earliestStart = Integer.MAX_VALUE;
                for (int[] slot : allSlots) {
                    int sTotal = slot[0] * 60 + slot[1];
                    if (sTotal < earliestStart) earliestStart = sTotal;
                }
                long dayMillis = (long) dayOffset * 24L * 3600_000L;
                long startOfDayMillis = cal.getTimeInMillis() - (long) (nowTotalMin * 60_000L + curSec * 1000L);
                return startOfDayMillis + dayMillis + (long) earliestStart * 60_000L;
            }
        }
        return 0L;
    }

    public boolean isWithinTimeRange(ScheduleConfig config) {
        if (!globalMasterSwitch || config == null || !config.isEnabled) {
            return false;
        }
        if (config.timingMode == ScheduleConfig.TIMING_MODE_SERVER_CYCLE) {
            return config.state == ScheduleConfig.STATE_RUNNING;
        }
        return isWithinRealTimeRange(config);
    }

    /**
     * API kiểm tra phó bản thời gian có đang mở cửa không (dành cho toàn server).
     */
    public boolean isDungeonOpen(String id) {
        ScheduleConfig cfg = configs.get(id);
        if (cfg == null || !globalMasterSwitch || !cfg.isEnabled) return false;
        return isWithinTimeRange(cfg);
    }

    /**
     * API lấy thông báo chi tiết về trạng thái mở cửa / thời gian đếm ngược còn lại.
     */
    public String getDungeonStatusMessage(String id) {
        ScheduleConfig cfg = configs.get(id);
        if (cfg == null) return "Không tìm thấy cấu hình phó bản!";
        if (!globalMasterSwitch) return "Toàn bộ hệ thống phó bản đang tạm bảo trì bởi BQT!";
        if (!cfg.isEnabled) return "Phó bản [" + cfg.name + "] hiện đang tạm đóng!";

        if (cfg.timingMode == ScheduleConfig.TIMING_MODE_SERVER_CYCLE) {
            long remainSec = Math.max(0, (cfg.nextStateChangeTime - System.currentTimeMillis()) / 1000);
            if (cfg.state == ScheduleConfig.STATE_RUNNING) {
                return "Phó bản [" + cfg.name + "] đang mở cửa Đợt #" + cfg.roundIndex + "!\n"
                        + "- Thời gian đợt này còn: " + (remainSec / 60) + " phút " + (remainSec % 60) + " giây.\n"
                        + "- Chu kỳ: " + cfg.runDurationMinutes + " phút chạy / " + cfg.cooldownMinutes + " phút chờ.";
            } else {
                return "Phó bản [" + cfg.name + "] đang trong thời gian chờ giữa các đợt!\n"
                        + "- Đợt tiếp theo (#" + (cfg.roundIndex + 1) + ") sẽ mở sau: " + (remainSec / 60) + " phút " + (remainSec % 60) + " giây.\n"
                        + "- Chu kỳ: " + cfg.runDurationMinutes + " phút chạy / " + cfg.cooldownMinutes + " phút chờ.";
            }
        } else {
            if (isWithinRealTimeRange(cfg)) {
                long remainSec = Math.max(0, (cfg.nextStateChangeTime - System.currentTimeMillis()) / 1000);
                return "Phó bản [" + cfg.name + "] đang trong thời gian mở cửa!\n"
                        + "- Thời gian còn lại của đợt: " + (remainSec / 60) + " phút " + (remainSec % 60) + " giây.\n"
                        + "- Lịch mở: " + cfg.getTimeRangeString();
            } else {
                long waitSec = Math.max(0, (cfg.nextStateChangeTime - System.currentTimeMillis()) / 1000);
                String waitStr = (waitSec > 0)
                        ? ("\n- Đợt mở kế tiếp sau: " + (waitSec / 3600) + " giờ " + ((waitSec % 3600) / 60) + " phút.")
                        : "";
                return "Phó bản [" + cfg.name + "] chưa đến giờ mở cửa!\n"
                        + "- Lịch mở: " + cfg.getTimeRangeString() + waitStr;
            }
        }
    }

    /**
     * Kiểm tra khung giờ mở phó bản bang hội (Hỗ trợ tương thích ngược).
     */
    public static boolean isClanDungeonTime() {
        if (ZUtil.is_DayofWeek(7)) {
            return true;
        }
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int totalMin = hour * 60 + min;

        // Mốc trưa: 11:30 - 13:30
        if (totalMin >= 11 * 60 + 30 && totalMin <= 13 * 60 + 30) {
            return true;
        }
        // Mốc tối: 18:30 - 22:30
        if (totalMin >= 18 * 60 + 30 && totalMin <= 22 * 60 + 30) {
            return true;
        }
        return false;
    }

    public void tick() {
        if (!globalMasterSwitch) return;
        try {
            long now = System.currentTimeMillis();
            boolean stateChanged = false;

            for (ScheduleConfig cfg : configs.values()) {
                if (cfg == null || !cfg.isEnabled) continue;

                if (cfg.timingMode == ScheduleConfig.TIMING_MODE_SERVER_CYCLE) {
                    // Chế độ Chu kỳ xoay vòng liên tục (TEST MODE)
                    if (cfg.state == ScheduleConfig.STATE_IDLE) {
                        if (now >= cfg.nextStateChangeTime || cfg.nextStateChangeTime == 0) {
                            // Chuyển sang RUNNING: Khởi tạo đợt mới
                            cfg.state = ScheduleConfig.STATE_RUNNING;
                            cfg.roundIndex++;
                            cfg.currentTokenId = ZUtil.generateMD5Token(cfg.id + "_" + cfg.roundIndex + "_" + now);
                            cfg.nextStateChangeTime = now + (long) cfg.runDurationMinutes * 60_000L;
                            onStartDungeonRound(cfg);
                            stateChanged = true;
                        }
                    } else if (cfg.state == ScheduleConfig.STATE_RUNNING) {
                        if (now >= cfg.nextStateChangeTime) {
                            // Chuyển sang IDLE: Kết thúc đợt và bắt đầu cooldown chờ đợt sau
                            cfg.state = ScheduleConfig.STATE_IDLE;
                            cfg.nextStateChangeTime = now + (long) cfg.cooldownMinutes * 60_000L;
                            onEndDungeonRound(cfg);
                            stateChanged = true;
                        }
                    }
                } else {
                    // Chế độ Giờ thực cố định (BETA / OPEN MODE)
                    boolean withinRealTime = isWithinRealTimeRange(cfg);
                    if (withinRealTime && cfg.state == ScheduleConfig.STATE_IDLE) {
                        cfg.state = ScheduleConfig.STATE_RUNNING;
                        cfg.roundIndex++;
                        cfg.currentTokenId = ZUtil.generateMD5Token(cfg.id + "_" + cfg.roundIndex + "_" + now);
                        onStartDungeonRound(cfg);
                        stateChanged = true;
                    } else if (!withinRealTime && cfg.state == ScheduleConfig.STATE_RUNNING) {
                        cfg.state = ScheduleConfig.STATE_IDLE;
                        onEndDungeonRound(cfg);
                        stateChanged = true;
                    }
                }
            }

            if (stateChanged || (now - lastCacheSaveTime) > 30_000L) {
                saveCache();
            }
        } catch (Exception e) {
            Log.error("TimedDungeonManager", "Tick error: " + e.getMessage());
        }
    }

    private void onStartDungeonRound(ScheduleConfig cfg) {
        try {
            // Log.info("TimedDungeonManager", ">>> [START SEASON #" + cfg.roundIndex + "] " + cfg.name + " | MD5 Token: " + cfg.currentTokenId);
            switch (cfg.id) {
                case "DAU_TRUONG_TU_DO":
                    DauTruongTuDo.Open(cfg.currentTokenId);
                    break;
                case "TRAN_CHIEN_KHONG_LO":
                    TranChienKhongLo.startSession(cfg.currentTokenId, cfg.runDurationMinutes);
                    break;
                case "WORLD_WAR":
                    WorldWar.startRegister(cfg.currentTokenId);
                    break;
                case "BOSS_THE_GIOI":
                    if (!BossTheGioi.is_spawned) {
                        BossTheGioi.spawn_boss();
                        Manager.gI().chatKTG(0, "Boss Thế Giới đã xuất hiện tàn sát!", 5);
                    }
                    break;
                case "BAO_VE_PHAO_DAI":
                    BaoVePhaoDai.is_open = true;
                    Manager.gI().chatKTG(0, "Sự kiện Bảo Vệ Pháo Đài đã mở đợt mới #" + cfg.roundIndex + "!", 5);
                    break;
                case "THU_LINH_BIEN_KHOI":
                    bot.BotThuLinhBienKhoi.dispatchBots();
                    Manager.gI().chatKTG(0, "Thủ Lĩnh Biển Khơi đã mở đợt mới #" + cfg.roundIndex + "!", 5);
                    break;
                case "CHIEM_DAO":
                    for (int mId = 261; mId <= 265; mId++) {
                        Zone[] zones = Zone.getMapByID(mId);
                        if (zones != null) {
                            for (Zone z : zones) {
                                if (z != null && z.list_mob != null) {
                                    for (int idx : z.list_mob) {
                                        mob.Mob m = z.getMob(idx);
                                        if (m != null) {
                                            m.isdie = false;
                                            m.hp = m.hp_max;
                                            m.id_target = -1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    bot.BotChiemDao.dispatchAllIslands();
                    Manager.gI().chatKTG(0, "Hoạt động Chiếm Đảo đã mở đợt mới #" + cfg.roundIndex + "!", 5);
                    break;
                case "PVP_BANG":
                    bot.BotLobbyManager.initLobbyBots();
                    Manager.gI().chatKTG(0, "Hoạt động Phó Bản PVP Băng đã mở đợt mới #" + cfg.roundIndex + "!", 5);
                    break;
            }
        } catch (Exception e) {
            Log.error("TimedDungeonManager", "onStartDungeonRound error (" + cfg.id + "): " + e.getMessage());
        }
    }

    private void onEndDungeonRound(ScheduleConfig cfg) {
        try {
            // Log.info("TimedDungeonManager", "[KẾT THÚC ĐỢT #" + cfg.roundIndex + "] " + cfg.name);
            switch (cfg.id) {
                case "DAU_TRUONG_TU_DO":
                    if (DauTruongTuDo.IsOpen()) {
                        DauTruongTuDo.CreateBxh(); // tổng kết bảng xếp hạng trước
                        saveTopRewards_DauTruong(cfg);
                        DauTruongTuDo.Close();
                    }
                    break;
                case "TRAN_CHIEN_KHONG_LO":
                    TranChienKhongLo.endSession();
                    break;
                case "WORLD_WAR":
                    if (WorldWar.status != WorldWar.STATUS_CLOSE) {
                        saveTopRewards_WorldWar(cfg);
                        WorldWar.close();
                    }
                    break;
                case "BOSS_THE_GIOI":
                    // Boss Thế Giới tự quản lý death/reward
                    break;
                case "BAO_VE_PHAO_DAI":
                    BaoVePhaoDai.is_open = false;
                    Manager.gI().chatKTG(0, "Bảo Vệ Pháo Đài kết thúc đợt #" + cfg.roundIndex + "! Top nhận quà tại NPC.", 5);
                    break;
                case "THU_LINH_BIEN_KHOI":
                    bot.BotThuLinhBienKhoi.clearBots();
                    bot.BotLobbyManager.initLobbyBots();
                    Manager.gI().chatKTG(0, "Thủ Lĩnh Biển Khơi kết thúc đợt #" + cfg.roundIndex + "!", 5);
                    break;
                case "CHIEM_DAO":
                    saveTopRewards_ChiemDao(cfg);
                    bot.BotChiemDao.clearIslandBots();
                    bot.BotChiemDao.dispatchVillageBots();
                    Manager.gI().chatKTG(0, "Chiếm Đảo kết thúc đợt #" + cfg.roundIndex + "! Phần thưởng đã được gửi tới các Bang chiếm đảo.", 5);
                    break;
                case "PVP_BANG":
                    bot.BotLobbyManager.initLobbyBots();
                    Manager.gI().chatKTG(0, "PVP Băng kết thúc đợt #" + cfg.roundIndex + "!", 5);
                    break;
            }
        } catch (Exception e) {
            Log.error("TimedDungeonManager", "onEndDungeonRound error (" + cfg.id + "): " + e.getMessage());
        }
    }

    private void saveTopRewards_WorldWar(ScheduleConfig cfg) {
        try {
            if (TranChienLon.top3 != null && !TranChienLon.top3.isEmpty()) {
                int rIdx = 0;
                for (Map.Entry<String, Integer> entry : TranChienLon.top3.entrySet()) {
                    if (entry.getKey() == null) continue;
                    List<GiftBox> gifts = TopWorldWar.gI().getRewards(rIdx);
                    int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(entry.getKey());
                    if (ids[0] > 0) {
                        historys.DungeonRewardHistory.saveReward(
                            ids[0], ids[1],
                            cfg.id, cfg.currentTokenId,
                            rIdx, gifts
                        );
                    }
                    rIdx++;
                    if (rIdx >= 3) break;
                }
            }
        } catch (Exception e) {
            Log.error("TimedDungeonManager", "saveTopRewards_WorldWar error: " + e.getMessage());
        }
    }

    private void saveTopRewards_ChiemDao(ScheduleConfig cfg) {
        try {
            int[] islandIds = {25, 33, 49, 69, 83};
            for (int i = 0; i < islandIds.length; i++) {
                clan.Clan c = map.zones.ChiemDao.getClanTop(islandIds[i]);
                if (c != null && c.members != null && !c.members.isEmpty()) {
                    List<template.GiftBox> gifts = TopChiemDao.gI().getRewards(i);
                    // Trao quà cho Bang chủ (thuyền trưởng)
                    String leaderName = c.members.get(0).name;
                    int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(leaderName);
                    if (ids[0] > 0) {
                        historys.DungeonRewardHistory.saveReward(
                            ids[0], ids[1],
                            cfg.id, cfg.currentTokenId + "_island_" + islandIds[i],
                            i, gifts
                        );
                    }
                }
            }
        } catch (Exception e) {
            Log.error("TimedDungeonManager", "saveTopRewards_ChiemDao error: " + e.getMessage());
        }
    }

    /**
     * Tính top Đấu Trường Tự Do, build quà theo rank, lưu vào DB để player claim.
     */
    private void saveTopRewards_DauTruong(ScheduleConfig cfg) {
        try {
            java.util.List<template.InfoMemList> bxh = DauTruongTuDo.bxh;
            if (bxh == null || bxh.isEmpty()) return;

            int topCount = Math.min(bxh.size(), 10);
            for (int rIdx = 0; rIdx < topCount; rIdx++) {
                template.InfoMemList entry = bxh.get(rIdx);
                if (entry == null || entry.name == null) continue;

                java.util.List<template.GiftBox> gifts = TopDauTruongTuDo.gI().getRewards(rIdx);
                if (gifts.isEmpty()) continue;

                int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(entry.name);
                if (ids[0] == 0) continue; // player không tìm thấy trong DB

                historys.DungeonRewardHistory.saveReward(
                    ids[0], ids[1],
                    cfg.id, cfg.currentTokenId,
                    rIdx, gifts
                );
            }
            Manager.gI().chatKTG(0, "Đấu Trường Tự Do đợt #" + cfg.roundIndex + " kết thúc! Top " + topCount + " nhận quà tại NPC.", 5);
        } catch (Exception e) {
            Log.error("TimedDungeonManager", "saveTopRewards_DauTruong error: " + e.getMessage());
        }
    }

    private java.util.List<template.GiftBox> buildDauTruongGifts(int rank) {
        java.util.List<template.GiftBox> gifts = new java.util.ArrayList<>();
        int beri = 0, ruby = 0, ticket = 0;
        switch (rank) {
            case 0: beri = 2_000_000; ruby = 150; ticket = 3; break;
            case 1: beri = 1_000_000; ruby = 100; ticket = 2; break;
            case 2: beri = 500_000; ruby = 50; ticket = 1; break;
            case 3: case 4: case 5: beri = 300_000; ruby = 30; ticket = 1; break;
            default: beri = 150_000; ruby = 15; break;
        }
        addGift(gifts, 0, 4, beri);
        addGift(gifts, 1, 4, ruby);
        if (ticket > 0) addGift(gifts, 30, 4, ticket);
        return gifts;
    }

    private void addGift(java.util.List<template.GiftBox> list, int itemId, int type, int num) {
        if (num <= 0) return;
        template.ItemTemplate4 t = template.ItemTemplate4.get_it_by_id(itemId);
        if (t == null) return;
        template.GiftBox g = new template.GiftBox();
        g.id = t.id; g.type = (byte) type;
        g.name = t.name; g.icon = t.icon;
        g.num = num; g.color = 0;
        list.add(g);
    }



    public boolean isAdmin(Player p) {
        if (p == null) return false;
        return p.admin == 1 || "admin".equalsIgnoreCase(p.name) || "ad".equalsIgnoreCase(p.name);
    }

    public void openAdminSystemMenu(Player p) throws IOException {
        AdminSystemMenu.openMain(p);
    }

    public void handleAdminSystemMenu(Player p, int index) throws IOException {
        if (!isAdmin(p)) return;

        switch (index) {
            case 0: buffAdminFullStats(p); break;
            case 1: AdminSystemMenu.openBuffDevilFruitMenuStatic(p); break;
            case 2: openSwitchClazzMenu(p); break;
            case 3: openAdjustLevelDialog(p); break;
            case 4: openAddItemCategoryMenu(p); break;
            case 5: AdminSystemMenu.openCleanThanTrangMenu(p); break;
            case 6: openTeleportMenu(p); break;
            case 7: openBotManagementMenu(p); break;
            case 8: openDiscipleManagementMenu(p); break;
            case 9: openTimedDungeonManagementMenu(p); break;
            case 10: openBossEventMenu(p); break;
            case 11: openPlayerManagementMenu(p); break;
            case 12: break;
        }
    }

    public void openAddItemCategoryMenu(Player p) throws IOException {
        p.getService().openDynamicMenu(87270,
            "Quản Lý Tài Nguyên & Item",
            new String[]{
                "Thêm Item Tự Chọn (Nhập Category, ID, Số Lượng)",
                "Thêm Vàng (Beri)",
                "Thêm Ruby",
                "Thêm Extol (Trái Bá Vương)",
                "Nhận Full Trái Ác Quỷ Thượng Cấp vào Hành Trang",
                "Quay lại"
            },
            new short[]{133, 110, 110, 133, 155, 134}
        );
    }

    public void handleAddItemCategoryMenu(Player p, int index) throws IOException {
        if (!isAdmin(p)) return;
        switch (index) {
            case 0: openAddItemDialog(p); break;
            case 1: {
                p.sendInput("Nhập số lượng Vàng (Beri) muốn cộng:", new String[]{"Số Vàng"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0].trim())) {
                        long val = Long.parseLong(inputs[0].trim());
                        p.update_vang(val);
                        p.updateMoney();
                        try { p.getService().send_box_ThongBao_OK("Đã cộng " + val + " Beri!"); } catch (Exception ignored) {}
                    }
                });
                break;
            }
            case 2: {
                p.sendInput("Nhập số lượng Ruby muốn cộng:", new String[]{"Số Ruby"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0].trim())) {
                        long val = Long.parseLong(inputs[0].trim());
                        p.update_ruby(val);
                        p.updateMoney();
                        try { p.getService().send_box_ThongBao_OK("Đã cộng " + val + " Ruby!"); } catch (Exception ignored) {}
                    }
                });
                break;
            }
            case 3: {
                p.sendInput("Nhập số lượng Extol muốn cộng:", new String[]{"Số Extol"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && ZUtil.isnumber(inputs[0].trim())) {
                        long val = Long.parseLong(inputs[0].trim());
                        p.updateVnd(val);
                        p.updateMoney();
                        try { p.getService().send_box_ThongBao_OK("Đã cộng " + val + " Extol!"); } catch (Exception ignored) {}
                    }
                });
                break;
            }
            case 4: {
                int[] dfIds = new int[]{207, 208, 209, 210, 211, 212, 213, 214};
                for (int dfId : dfIds) {
                    p.item.add_item_bag47(4, (short) dfId, 10);
                }
                p.item.updateInventory(false);
                p.getService().send_box_ThongBao_OK("Đã thêm Bộ Trái Ác Quỷ Thượng Cấp vào Hành Trang!");
                break;
            }
            case 5: openAdminSystemMenu(p); break;
        }
    }

    public void openTeleportMenu(Player p) throws IOException {
        p.getService().openDynamicMenu(87240,
            "Fast Teleport & Người Chơi",
            new String[]{
                "Làng Cối Xay Gió (Map 1)",
                "Thị Trấn Vỏ Sò (Map 9)",
                "Làng Khởi Đầu (Map 49)",
                "Mỏm Sinh Đôi (Map 66)",
                "Đấu Trường Tự Do (Map 69)",
                "Map Boss Thế Giới (Map 107)",
                "Dịch chuyển ĐẾN vị trí Người Chơi",
                "KÉO Người Chơi đến vị trí Admin",
                "Quay lại"
            },
            new short[]{116, 116, 116, 116, 136, 136, 110, 110, 134}
        );
    }

    public void handleTeleportMenu(Player p, int index) throws IOException {
        if (!isAdmin(p)) return;
        switch (index) {
            case 0: teleportPlayerToMap(p, 1); break;
            case 1: teleportPlayerToMap(p, 9); break;
            case 2: teleportPlayerToMap(p, 49); break;
            case 3: teleportPlayerToMap(p, 66); break;
            case 4: teleportPlayerToMap(p, 69); break;
            case 5: teleportPlayerToMap(p, 107); break;
            case 6: {
                p.sendInput("Nhập tên người chơi muốn đến:", new String[]{"Tên Player"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        teleportPlayerToPlayer(p, inputs[0].trim());
                    }
                });
                break;
            }
            case 7: {
                p.sendInput("Nhập tên người chơi muốn kéo đến Admin:", new String[]{"Tên Player"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        pullPlayerToAdmin(p, inputs[0].trim());
                    }
                });
                break;
            }
            case 8: openAdminSystemMenu(p); break;
        }
    }

    public static void teleportPlayerToMap(Player p, int mapId) {
        try {
            Zone[] zones = Zone.getMapByID(mapId);
            if (zones != null && zones.length > 0 && zones[0] != null) {
                Zone targetZone = zones[0];
                if (p.map != null) {
                    p.map.leave_map(p, 2);
                }
                p.map = targetZone;
                if (targetZone.template.vgos != null && !targetZone.template.vgos.isEmpty()) {
                    p.x = targetZone.template.vgos.get(0).xnew;
                    p.y = targetZone.template.vgos.get(0).ynew;
                } else {
                    p.x = 200;
                    p.y = 200;
                }
                p.xold = p.x;
                p.yold = p.y;
                p.lastValidX = p.x;
                p.lastValidY = p.y;
                p.map.goto_map(p);
                p.getService().update_PK(p, true);
                p.getService().pet(p, true);
                p.getService().send_box_ThongBao_OK("Đã dịch chuyển đến map " + targetZone.template.name + " (ID: " + mapId + ")!");
            } else {
                p.getService().send_box_ThongBao_OK("Không tìm thấy Map ID: " + mapId);
            }
        } catch (Exception e) {
            try { p.getService().send_box_ThongBao_OK("Lỗi dịch chuyển: " + e.getMessage()); } catch (Exception ignored) {}
        }
    }

    public static void teleportPlayerToPlayer(Player admin, String targetName) {
        try {
            Player target = Zone.get_player_by_name_allmap(targetName);
            if (target == null || target.map == null) {
                admin.getService().send_box_ThongBao_OK(" Người chơi " + targetName + " không online hoặc không khả dụng!");
                return;
            }
            if (admin.map != null) {
                admin.map.leave_map(admin, 2);
            }
            admin.map = target.map;
            admin.x = target.x;
            admin.y = target.y;
            admin.xold = admin.x;
            admin.yold = admin.y;
            admin.lastValidX = admin.x;
            admin.lastValidY = admin.y;
            admin.map.goto_map(admin);
            admin.getService().update_PK(admin, true);
            admin.getService().pet(admin, true);
            admin.getService().send_box_ThongBao_OK("Đã bay đến vị trí của " + target.name + " tại map " + target.map.template.name + "!");
        } catch (Exception e) {
            try { admin.getService().send_box_ThongBao_OK("Lỗi dịch chuyển: " + e.getMessage()); } catch (Exception ignored) {}
        }
    }

    public static void pullPlayerToAdmin(Player admin, String targetName) {
        try {
            Player target = Zone.get_player_by_name_allmap(targetName);
            if (target == null || target.map == null) {
                admin.getService().send_box_ThongBao_OK("Người chơi " + targetName + " không online hoặc không khả dụng!");
                return;
            }
            if (target.map != null) {
                target.map.leave_map(target, 2);
            }
            target.map = admin.map;
            target.x = admin.x;
            target.y = admin.y;
            target.xold = target.x;
            target.yold = target.y;
            target.lastValidX = target.x;
            target.lastValidY = target.y;
            target.map.goto_map(target);
            target.getService().update_PK(target, true);
            target.getService().pet(target, true);
            try { target.getService().send_box_ThongBao_OK("Bạn đã được Admin " + admin.name + " triệu hồi!"); } catch (Exception ignored) {}
            admin.getService().send_box_ThongBao_OK("Đã kéo " + target.name + " đến vị trí của bạn!");
        } catch (Exception e) {
            try { admin.getService().send_box_ThongBao_OK("Lỗi kéo người chơi: " + e.getMessage()); } catch (Exception ignored) {}
        }
    }

    public static int pullAllOnlinePlayersToAdmin(Player admin) {
        if (admin == null || admin.map == null) return 0;
        List<Player> realPlayers = AdminSystemMenu.getRealOnlinePlayers();
        realPlayers.removeIf(pl -> pl == null || pl.IDPlayer == admin.IDPlayer || pl.conn == null || !pl.conn.connected);
        if (realPlayers.isEmpty()) {
            if (admin.getService() != null) {
                admin.getService().send_box_ThongBao_OK("Hiện không có người chơi online nào khác!");
            }
            return 0;
        }

        Zone targetZone = admin.map;
        int mapW = (targetZone.template != null && targetZone.template.maxW > 0) ? targetZone.template.maxW : 1200;
        List<map.Vgo> vgos = (targetZone.template != null && targetZone.template.vgos != null) ? targetZone.template.vgos : Collections.emptyList();

        List<Integer> safeXList = new ArrayList<>();
        int minX = 120;
        int maxX = Math.max(minX + 100, mapW - 120);
        int step = 60;

        for (int testX = minX; testX <= maxX; testX += step) {
            boolean isNearVgo = false;
            for (map.Vgo v : vgos) {
                if (v != null && Math.abs(testX - v.xold) < 140) {
                    isNearVgo = true;
                    break;
                }
            }
            if (!isNearVgo) {
                safeXList.add(testX);
            }
        }

        if (safeXList.isEmpty()) {
            safeXList.add((int) admin.x);
        }

        int pulledCount = 0;
        int numPlayers = realPlayers.size();

        for (int i = 0; i < numPlayers; i++) {
            Player pl = realPlayers.get(i);
            try {
                int slotIndex = (i * safeXList.size()) / numPlayers;
                if (slotIndex >= safeXList.size()) slotIndex = safeXList.size() - 1;
                short destX = safeXList.get(slotIndex).shortValue();

                short destY = admin.y;
                if (numPlayers > safeXList.size()) {
                    int row = (i / safeXList.size()) % 3;
                    if (row == 1) destY = (short) Math.max(50, admin.y - 15);
                    else if (row == 2) destY = (short) (admin.y + 15);
                }

                if (pl.map != null) {
                    pl.map.leave_map(pl, 2);
                }
                pl.map = targetZone;
                pl.x = destX;
                pl.y = destY;
                pl.xold = destX;
                pl.yold = destY;
                pl.lastValidX = destX;
                pl.lastValidY = destY;
                pl.spawnX = destX;
                pl.spawnY = destY;
                pl.hasMovedFromSpawn = false;
                pl.map.goto_map(pl);
                if (pl.getService() != null) {
                    pl.getService().update_PK(pl, true);
                    pl.getService().pet(pl, true);
                    try {
                        pl.getService().send_box_ThongBao_OK("Bạn đã được Admin triệu tập về " + targetZone.template.name + "!");
                    } catch (Exception ignored) {}
                }
                pulledCount++;
            } catch (Exception e) {
                Log.error("pullAllOnlinePlayersToAdmin", "Error pulling " + pl.name + ": " + e.getMessage());
            }
        }

        if (admin.getService() != null) {
            admin.getService().send_box_ThongBao_OK(
                "Đã kéo thành công " + pulledCount + " người chơi online về " + targetZone.template.name + "!\n" +
                "- Tọa độ phân bổ đều trên toàn bộ bản đồ\n" +
                "- Không đứng sát nhau và cách xa cổng chuyển map\n" +
                "- Khu vực: Khu " + (targetZone.zone_id + 1)
            );
        }
        return pulledCount;
    }

    public void openBossEventMenu(Player p) throws IOException {
        p.getService().openDynamicMenu(87250,
            "Quản Lý Boss & Event Server",
            new String[]{
                "Spawn Boss Thế Giới Ngay Lập Tức",
                "Mở Ngay Đấu Trường Tự Do",
                "Đóng & Phát Quà Đấu Trường Tự Do",
                "Mở Cổng Đăng Ký Đại Chiến Thế Giới",
                "Mở Sự Kiện Bảo Vệ Pháo Đài",
                "Bật/Tắt Event Halloween",
                "Bật/Tắt Event Noel",
                "Bật/Tắt Event Trồng Cây",
                "Quay lại"
            },
            new short[]{136, 136, 118, 146, 110, 155, 155, 155, 134}
        );
    }

    public void handleBossEventMenu(Player p, int index) throws IOException {
        if (!isAdmin(p)) return;
        switch (index) {
            case 0: {
                BossTheGioi.spawn_boss();
                p.getService().send_box_ThongBao_OK("Đã triệu hồi Boss Thế Giới!");
                break;
            }
            case 1: {
                DauTruongTuDo.Open();
                p.getService().send_box_ThongBao_OK("Đã mở thủ công Đấu Trường Tự Do!");
                break;
            }
            case 2: {
                DauTruongTuDo.Close();
                p.getService().send_box_ThongBao_OK("Đã đóng Đấu Trường Tự Do & Phát Quà Top!");
                break;
            }
            case 3: {
                WorldWar.status = WorldWar.STATUS_REGISTER;
                try { Manager.gI().chatKTG(0, "Đại Chiến Thế Giới đã chính thức mở cổng đăng ký từ Admin!", 5); } catch (Exception ignored) {}
                p.getService().send_box_ThongBao_OK("Đã mở đăng ký Đại Chiến Thế Giới!");
                break;
            }
            case 4: {
                BaoVePhaoDai.is_open = true;
                try { Manager.gI().chatKTG(0, "Sự kiện Bảo Vệ Pháo Đài đã kích hoạt từ Admin!", 5); } catch (Exception ignored) {}
                p.getService().send_box_ThongBao_OK("Đã mở Bảo Vệ Pháo Đài!");
                break;
            }
            case 5: {
                boolean current = EventManager.isActive(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025);
                EventManager.setEventActive(SuKienHalloween.ID_SUKIEN_HALLOWEEN_2025, !current);
                p.getService().send_box_ThongBao_OK("Event Halloween -> " + (!current ? "Đã Bật" : "Đã Tắt"));
                break;
            }
            case 6: {
                boolean current = EventManager.isActive(1);
                EventManager.setEventActive(1, !current);
                p.getService().send_box_ThongBao_OK("Event Noel -> " + (!current ? "Đã Bật" : "Đã Tắt"));
                break;
            }
            case 7: {
                boolean current = EventManager.isActive(3);
                EventManager.setEventActive(3, !current);
                p.getService().send_box_ThongBao_OK("Event Trồng Cây -> " + (!current ? "Đã Bật" : "Đã Tắt"));
                break;
            }
            case 8: openAdminSystemMenu(p); break;
        }
    }

    public void openPlayerManagementMenu(Player p) throws IOException {
        p.getService().openDynamicMenu(87260,
            "Quản Lý Người Chơi & KTG",
            new String[]{
                "Thông Báo KTG Toàn Server",
                "Phục Hồi 100% HP/MP Cho TOÀN BỘ Server",
                "Tặng 500k Beri + 1k Ruby Cho TOÀN BỘ Server",
                "Kick 1 Người Chơi Khỏi Server",
                "Quay lại"
            },
            new short[]{134, 133, 110, 118, 134}
        );
    }

    public void handlePlayerManagementMenu(Player p, int index) throws IOException {
        if (!isAdmin(p)) return;
        switch (index) {
            case 0: {
                p.sendInput("Nội dung thông báo toàn server:", new String[]{"Nhập thông báo"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        try {
                            Manager.gI().chatKTG(0, "[ADMIN " + p.name + "]: " + inputs[0], 5);
                        } catch (Exception ignored) {}
                    }
                });
                break;
            }
            case 1: {
                int count = 0;
                for (Player p0 : SessionManager.PLAYERS_MAP.values()) {
                    if (p0 != null && p0.ability != null) {
                        p0.hp = p0.ability.get_hp_max(true);
                        p0.mp = p0.ability.get_mp_max(true);
                        count++;
                    }
                }
                p.getService().send_box_ThongBao_OK("Đã phục hồi HP/MP cho " + count + " người chơi online!");
                break;
            }
            case 2: {
                int count = 0;
                for (Player p0 : SessionManager.PLAYERS_MAP.values()) {
                    if (p0 != null) {
                        p0.update_vang(500_000);
                        p0.update_ruby(1_000);
                        p0.updateMoney();
                        try { p0.getService().send_box_ThongBao_OK("Bạn nhận được 500,000 Beri + 1,000 Ruby từ Admin!"); } catch (Exception ignored) {}
                        count++;
                    }
                }
                p.getService().send_box_ThongBao_OK("Đã phát phần thưởng cho " + count + " người chơi online!");
                break;
            }
            case 3: {
                p.sendInput("Nhập tên người chơi cần Kick:", new String[]{"Tên Player"}, (inputs) -> {
                    if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                        Player target = Zone.get_player_by_name_allmap(inputs[0].trim());
                        if (target != null && target.conn != null) {
                            try {
                                target.conn.close();
                                p.getService().send_box_ThongBao_OK("Đã ngắt kết nối " + target.name + "!");
                            } catch (Exception e) {
                                try { p.getService().send_box_ThongBao_OK("Lỗi kick player: " + e.getMessage()); } catch (Exception ignored) {}
                            }
                        } else {
                            p.getService().send_box_ThongBao_OK("Người chơi không online!");
                        }
                    }
                });
                break;
            }
            case 4: openAdminSystemMenu(p); break;
        }
    }

    public void openSwitchClazzMenu(Player p) throws IOException {
        new zinterfaces.menus.AdminSystemMenu().openSwitchClazz(p);
    }

    public void handleSwitchClazz(Player p, int choice) throws IOException {
        if (choice == 5) {
            openAdminSystemMenu(p);
            return;
        }
        if (choice < 0 || choice >= 5) return;
        byte newClazz = (byte) (choice + 1);
        if (p.clazz == newClazz) {
            p.getService().send_box_ThongBao_OK("Bạn đang ở " + zinterfaces.menus.AdminSystemMenu.getClassName(newClazz) + " rồi!");
            return;
        }

        p.tempTargetClazz = newClazz;
        new zinterfaces.menus.AdminSystemMenu().openSwitchClazzTypeMenu(p, newClazz);
    }

    public static void convertEquipClass(Player p, byte newClazz) {
        zinterfaces.menus.AdminSystemMenu.changeClassConvert(p, newClazz);
    }

    public void openAdjustLevelDialog(Player p) throws IOException {
        p.sendInput("Nhập số Level muốn thay đổi (Ví dụ: 10 để tăng 10 lv, -5 để trừ 5 lv, hoặc 150 để đặt max):", new String[]{"Số Level (+/-)"}, (inputs) -> {
            try {
                if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                    int val = Integer.parseInt(inputs[0].trim());
                    if (val > 0 && val <= 150 && inputs[0].trim().length() <= 3 && !inputs[0].trim().startsWith("+")) {
                        p.level = (short) Math.min(150, val);
                    } else {
                        p.level = (short) Math.max(1, Math.min(150, p.level + val));
                    }
                    p.hp = p.ability.get_hp_max(true);
                    p.mp = p.ability.get_mp_max(true);
                    p.update_info_to_all();
                    p.getService().send_box_ThongBao_OK("Cấp độ hiện tại của bạn: Level " + p.level);
                }
            } catch (Exception e) {
                try { p.getService().send_box_ThongBao_OK("Số level không hợp lệ!"); } catch (Exception ignored) {}
            }
        });
    }

    public void openAddItemDialog(Player p) throws IOException {
        p.sendInput("Nhập: Loại_Item ID_Item Số_Lượng (Ví dụ: 4 207 100 cho Item Bag47, hoặc 3 100 1 cho Item Trang bị):", new String[]{"Category ID Quantity"}, (inputs) -> {
            try {
                if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                    String[] parts = inputs[0].trim().split("\\s+");
                    if (parts.length >= 3) {
                        byte cat = Byte.parseByte(parts[0]);
                        short itemId = Short.parseShort(parts[1]);
                        int quant = Integer.parseInt(parts[2]);
                        if (cat == 4 || cat == 7) {
                            p.item.add_item_bag47(cat, itemId, quant);
                        } else if (cat == 3) {
                            ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(itemId);
                            if (t3 != null) {
                                Item_wear newWear = new Item_wear();
                                newWear.setup_template_by_id(t3);
                                p.item.add_item_bag3(newWear);
                            }
                        }
                        p.item.updateInventory(false);
                        p.getService().send_box_ThongBao_OK("Đã thêm " + quant + " vật phẩm (Category " + cat + ", ID " + itemId + ") vào hành trang!");
                    } else {
                        p.getService().send_box_ThongBao_OK("Nhập đủ 3 tham số: Category ID Quantity!");
                    }
                }
            } catch (Exception e) {
                try { p.getService().send_box_ThongBao_OK("Thông số item không hợp lệ!"); } catch (Exception ignored) {}
            }
        });
    }

    public void openDiscipleManagementMenu(Player p) throws IOException {
        if (p.detu == null && (p.nameDe == null || p.nameDe.isBlank())) {
            if (bot.mercenary.MercenaryManager.gI().hasAnyContracts(p)) {
                zinterfaces.menus.dymanics.DymanicMercenary.sendMercenaryMenu(p);
                return;
            }
            p.getService().send_box_ThongBao_OK("Bạn chưa có Đệ Tử hoặc Lính Đánh Thuê!");
            return;
        }

        DeTu dt = p.detu;
        String dtName = (dt != null) ? dt.name : p.nameDe;
        String curStatus = (dt != null && dt.detuStatus >= 0 && dt.detuStatus < DeTu.STATUS_NAMES.length) ? DeTu.STATUS_NAMES[dt.detuStatus] : "Chưa triệu hồi";
        int lv = (dt != null) ? dt.level : 1;

        p.getService().openDynamicMenu(zinterfaces.menus.AdminSystemMenu.MENU_DISCIPLE_STATUS,
            "Đệ Tử: " + dtName + " [Lv." + lv + " | " + curStatus + "]",
            new String[]{
                "Trạng thái: Đi theo",
                "Trạng thái: Bảo vệ",
                "Trạng thái: Tấn công",
                "Trạng thái: Về nhà (Ẩn đệ tử)",
                "Trạng thái: Hợp thể (Nhập thể)",
                "Xem thông tin Đệ Tử",
                "Xem danh sách Kỹ năng & Trái Ác Quỷ",
                "Nâng điểm tiềm năng",
                "Xóa đệ tử",
                "Quay lại"
            },
            new short[]{133, 110, 110, 118, 133, 110, 155, 110, 114, 134}
        );
    }

    public void handleDiscipleManagementMenu(Player p, int choice) throws IOException {
        if (p.detu == null && (p.nameDe == null || p.nameDe.isBlank())) {
            p.getService().send_box_ThongBao_OK("Bạn chưa có Đệ Tử!");
            return;
        }
        DeTu dt = p.detu;

        switch (choice) {
            case 0:
                if (dt != null) dt.setStatus(DeTu.STATUS_FOLLOW);
                break;
            case 1:
                if (dt != null) dt.setStatus(DeTu.STATUS_PROTECT);
                break;
            case 2:
                if (dt != null) dt.setStatus(DeTu.STATUS_ATTACK);
                break;
            case 3:
                if (dt != null) dt.setStatus(DeTu.STATUS_HOME);
                break;
            case 4:
                if (dt != null) dt.setStatus(DeTu.STATUS_FUSION);
                break;
            case 5:
                DeTu.showDeTuInfo(p);
                break;
            case 6:
                DeTu.showDeTuSkillMenu(p);
                break;
            case 7:
                iMenuDymanic.buildAndSend(p, 942, "Đệ tử",
                        new String[]{"Thông tin", "Sức mạnh", "Phòng thủ", "Thể lực", "Tinh Thần", "Nhanh nhẹn"},
                        null);
                break;
            case 8:
                DeTu.confirmDeleteDeTu(p);
                break;
            case 9:
                zinterfaces.menus.AdminSystemMenu.openItem872Menu(p);
                break;
        }
    }

    public void showDiscipleSkillMenu(Player p, Player target) throws IOException {
        if (target instanceof DeTu) {
            DeTu.showDeTuSkillMenu(p);
            return;
        }
        if (target == null || target.skill_point == null || target.skill_point.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Chưa có kỹ năng nào.");
            return;
        }

        List<String> options = new ArrayList<>();
        List<Integer> icons = new ArrayList<>();
        p.tempSkillList = new ArrayList<>(target.skill_point);

        for (Skill_info sk : target.skill_point) {
            if (sk == null || sk.temp == null) continue;

            String prefix = "";
            int lv = (sk.temp.Lv_RQ > 0) ? sk.temp.Lv_RQ : 1;

            boolean isDevil = (sk.temp.ID > 2000 
                    || (sk.temp.indexSkillInServer >= 475 && sk.temp.indexSkillInServer <= 486) 
                    || (sk.temp.indexSkillInServer >= 512 && sk.temp.indexSkillInServer <= 551) 
                    || (sk.temp.indexSkillInServer >= 656 && sk.temp.indexSkillInServer <= 659)
                    || (sk.temp.indexSkillInServer >= 791 && sk.temp.indexSkillInServer <= 802)
                    || sk.temp.typeDevil > 0)
                    && !(sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511)
                    && !(sk.temp.ID >= 1010 && sk.temp.ID <= 1014);

            if (isDevil) {
                prefix = "[Ác Quỷ] ";
                lv = sk.lvdevil > 0 ? sk.lvdevil : (sk.temp.Lv_RQ > 0 ? sk.temp.Lv_RQ : 1);
            } else if (sk.temp.indexSkillInServer >= 672 && sk.temp.indexSkillInServer <= 690) {
                prefix = "[Haki] ";
            } else if ((sk.temp.indexSkillInServer >= 487 && sk.temp.indexSkillInServer <= 511) || (sk.temp.ID >= 1010 && sk.temp.ID <= 1014)) {
                prefix = "[Buff Phái] ";
            } else if (sk.temp.typeSkill == 2 || sk.temp.typeSkill == 3) {
                prefix = "[Nội tại] ";
            } else {
                prefix = "[Chủ động] ";
            }

            options.add(prefix + sk.temp.name + " (Lv." + lv + ")");
            icons.add((int) sk.temp.idIcon);
        }

        p.getService().send_dynamic_menu_type4(941, 0, "Kỹ năng: " + target.name, options, icons);
    }

    public void openBotManagementMenu(Player p) throws IOException {
        int botCount = BotPlayerManager.activeBots.size();

        p.getService().openDynamicMenu(87210,
            "Quản Lý Bot System (" + botCount + "/" + BotPlayerManager.MAX_ACTIVE_BOTS + " Bot Online)",
            new String[]{
                "Xem danh sách Bot Online (" + botCount + ")",
                "Ép TOÀN BỘ Bot đi PHÓ BẢN ngay lập tức",
                "Ép TOÀN BỘ Bot đi ĐÁNH BOSS ngay lập tức",
                "Ép TOÀN BỘ Bot đi ĐẠI CHIẾN THẾ GIỚI",
                "Ép TOÀN BỘ Bot đi TRUY NÃ / PVP",
                "Trả TOÀN BỘ Bot về trạng thái TỰ ĐỘNG (FARM)",
                "Gọi thêm 5 Bot offline vào game",
                "Gọi TOÀN BỘ Bot offline vào game",
                "Kick / Đăng xuất TOÀN BỘ Bot",
                "Quay lại"
            },
            new short[]{110, 155, 136, 146, 137, 133, 110, 110, 118, 134}
        );
    }

    public void handleBotManagementMenu(Player p, int index) throws IOException {
        int count = 0;
        switch (index) {
            case 0: showBotListDetail(p); break;
            case 1: {
                for (BotPlayerReal bot : BotPlayerManager.activeBots.values()) {
                    if (bot != null && !bot.isdie) {
                        bot.state = "DUNGEON";
                        bot.targetMapId = -1;
                        bot.currentPath = null;
                        count++;
                    }
                }
                p.getService().send_box_ThongBao_OK("Đã ép " + count + " Bot chuyển sang đi PHÓ BẢN ngay lập tức!");
                break;
            }
            case 2: {
                for (BotPlayerReal bot : BotPlayerManager.activeBots.values()) {
                    if (bot != null && !bot.isdie) {
                        bot.state = "BOSS";
                        count++;
                    }
                }
                p.getService().send_box_ThongBao_OK("Đã ép " + count + " Bot chuyển sang SĂN BOSS!");
                break;
            }
            case 3: {
                for (BotPlayerReal bot : BotPlayerManager.activeBots.values()) {
                    if (bot != null && !bot.isdie) {
                        bot.state = "WORLD_WAR";
                        count++;
                    }
                }
                p.getService().send_box_ThongBao_OK("Đã ép " + count + " Bot tham gia ĐẠI CHIẾN THẾ GIỚI!");
                break;
            }
            case 4: {
                for (BotPlayerReal bot : BotPlayerManager.activeBots.values()) {
                    if (bot != null && !bot.isdie) {
                        bot.state = "WANTED";
                        bot.targetMapId = 119;
                        bot.currentPath = null;
                        count++;
                    }
                }
                p.getService().send_box_ThongBao_OK("Đã ép " + count + " Bot chuyển sang LỆNH TRUY NÃ / PVP!");
                break;
            }
            case 5: {
                for (BotPlayerReal bot : BotPlayerManager.activeBots.values()) {
                    if (bot != null) {
                        bot.state = "FARM";
                        bot.currentPath = null;
                        count++;
                    }
                }
                p.getService().send_box_ThongBao_OK("Đã trả " + count + " Bot về chế độ cày tự động!");
                break;
            }
            case 6: {
                for (int i = 0; i < 5; i++) BotPlayerManager.loginRandomBot();
                p.getService().send_box_ThongBao_OK("Đã gọi thêm Bot offline vào game!");
                break;
            }
            case 7: {
                for (int i = 0; i < BotPlayerManager.MAX_ACTIVE_BOTS; i++) BotPlayerManager.loginRandomBot();
                p.getService().send_box_ThongBao_OK("Đã gọi toàn bộ Bot offline tham gia vào game!");
                break;
            }
            case 8: {
                int total = BotPlayerManager.activeBots.size();
                for (BotPlayerReal bot : new ArrayList<>(BotPlayerManager.activeBots.values())) {
                    if (bot != null) {
                        bot.leave();
                        BotPlayerManager.activeBots.remove(bot.name);
                    }
                }
                p.getService().send_box_ThongBao_OK("Đã đăng xuất " + total + " Bot khỏi hệ thống!");
                break;
            }
            case 9: openAdminSystemMenu(p); break;
        }
    }

    public void showBotListDetail(Player p) throws IOException {
        List<BotPlayerReal> bots = new ArrayList<>(BotPlayerManager.activeBots.values());
        if (bots.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Hiện chưa có Bot nào online!");
            return;
        }

        String[] names = new String[Math.min(25, bots.size())];
        for (int i = 0; i < names.length; i++) {
            BotPlayerReal bot = bots.get(i);
            String mapName = (bot.map != null && bot.map.template != null) ? bot.map.template.name : "Map " + bot.targetMapId;
            names[i] = "" + bot.name + " (Lv." + bot.level + " | " + bot.state + " | " + mapName + ")";
        }

        p.getService().openDynamicMenu(87211,
            "Danh Sách Bot Online (" + bots.size() + " Total)", names, null);
    }

    public void handleBotSelectDetail(Player p, int botIndex) throws IOException {
        List<BotPlayerReal> bots = new ArrayList<>(BotPlayerManager.activeBots.values());
        if (botIndex < 0 || botIndex >= bots.size()) return;

        BotPlayerReal bot = bots.get(botIndex);

        p.setyesNoDialog(new YesNoDialog(p, 87212, "Điều Khiển Bot: " + bot.name,
            "Chọn thao tác quản lý Bot " + bot.name + ":",
            new String[]{
                "Xem Thông Tin",
                "Xem Danh Sách Kỹ Năng Bot",
                "Ép đi Phó Bản",
                "Ép đi Đánh Boss",
                "Trả về Farm",
                "Kick Bot này",
                "Hủy"
            },
            new byte[]{-1, -1, -1, -1, -1, -1, -1}, (byte choice) -> {
                try {
                    if (choice == 0) {
                        p.getService().send_view_other_player(bot);
                    } else if (choice == 1) {
                        showDiscipleSkillMenu(p, bot);
                    } else if (choice == 2) {
                        bot.state = "DUNGEON";
                        bot.targetMapId = -1;
                        bot.currentPath = null;
                        p.getService().send_box_ThongBao_OK("Đã ép Bot " + bot.name + " đi Phó Bản!");
                    } else if (choice == 3) {
                        bot.state = "BOSS";
                        p.getService().send_box_ThongBao_OK("Đã ép Bot " + bot.name + " đi Đánh Boss!");
                    } else if (choice == 4) {
                        bot.state = "FARM";
                        p.getService().send_box_ThongBao_OK("Đã trả Bot " + bot.name + " về chế độ Farm!");
                    } else if (choice == 5) {
                        bot.leave();
                        BotPlayerManager.activeBots.remove(bot.name);
                        p.getService().send_box_ThongBao_OK("Đã kick Bot " + bot.name + " khỏi server!");
                    }
                } catch (Exception ignored) {}
            }
        ));
        p.getService().startYesNo();
    }

    public void openTimedDungeonManagementMenu(Player p) throws IOException {
        List<model.Menu> menus = new ArrayList<>();

        // 1. Nút Master Switch: Đang Bật -> hiển thị Tắt, Đang Tắt -> hiển thị Bật
        String masterStatus = globalMasterSwitch ? " [MASTER: ĐANG BẬT] Bấm để TẮT TẤT CẢ" : " [MASTER: ĐANG TẮT] Bấm để BẬT TẤT CẢ";
        short masterIcon = globalMasterSwitch ? (short) 133 : (short) 118;
        menus.add(new model.Menu(masterStatus, masterIcon, () -> {
            try {
                globalMasterSwitch = !globalMasterSwitch;
                saveCache();
                p.getService().send_box_ThongBao_OK(globalMasterSwitch ? "Đã BẬT Master toàn bộ Phó bản!" : "Đã TẮT Master toàn bộ Phó bản!");
                openTimedDungeonManagementMenu(p);
            } catch (Exception e) {
                Log.error("TimedDungeonManager", "Master toggle error: " + e.getMessage());
            }
        }));

        // Nút Áp Dụng Cấu Hình Chuẩn theo Server Mode
        String modeName = Manager.gI().isTestMode() ? "TEST (Xoay Vòng 24/24)" : "BETA / OPEN (Giờ Thực Cố Định)";
        menus.add(new model.Menu("Áp Dụng Cấu Hình Chuẩn [" + modeName + "]", (short) 136, () -> {
            try {
                applyStandardSchedules();
                saveCache();
                p.getService().send_box_ThongBao_OK("Đã khôi phục toàn bộ phó bản về cấu hình chuẩn cho chế độ [" + modeName + "] thành công!");
                openTimedDungeonManagementMenu(p);
            } catch (Exception e) {
                Log.error("TimedDungeonManager", "Reset standard error: " + e.getMessage());
            }
        }));

        // 2. Mỗi phó bản 1 nút hiển thị trạng thái hiện tại (Đang bật -> hiện tắt / Đang tắt -> hiện bật, trạng thái đang chạy / đang chờ)
        List<ScheduleConfig> list = new ArrayList<>(configs.values());
        for (ScheduleConfig cfg : list) {
            if (cfg == null) continue;

            String statusTag;
            short icon;
            if (!globalMasterSwitch) {
                statusTag = " [MASTER TẮT]";
                icon = 118;
            } else if (!cfg.isEnabled) {
                statusTag = " [ĐANG TẮT]";
                icon = 118;
            } else {
                if (cfg.state == ScheduleConfig.STATE_RUNNING) {
                    long remainSec = Math.max(0, (cfg.nextStateChangeTime - System.currentTimeMillis()) / 1000);
                    statusTag = " [BẬT - Đang Chạy " + (remainSec / 60) + "p" + (remainSec % 60) + "s]";
                    icon = 133;
                } else {
                    long remainSec = Math.max(0, (cfg.nextStateChangeTime - System.currentTimeMillis()) / 1000);
                    statusTag = " [BẬT - Đang Chờ " + (remainSec / 60) + "p" + (remainSec % 60) + "s]";
                    icon = 110;
                }
            }

            String btnTitle = statusTag + " " + cfg.name + " (Đợt #" + cfg.roundIndex + ")";
            menus.add(new model.Menu(btnTitle, icon, () -> {
                try {
                    openTimedDungeonDetailMenuDynamic(p, cfg);
                } catch (Exception e) {
                    Log.error("TimedDungeonManager", "Open dungeon detail error: " + e.getMessage());
                }
            }));
        }

        // 3. Nút quay lại Menu Admin
        menus.add(new model.Menu("Quay Lại Menu Admin", (short) 134, () -> {
            try {
                AdminSystemMenu.openMain(p);
            } catch (Exception e) {
                Log.error("TimedDungeonManager", "Return to main error: " + e.getMessage());
            }
        }));

        p.getService().openDynamicMenu(87201, "Cấu Hình Bật / Tắt ALL Phó Bản Thời Gian", menus);
    }

    public void openTimedDungeonDetailMenuDynamic(Player p, ScheduleConfig cfg) throws IOException {
        if (p == null || cfg == null) return;
        List<model.Menu> menus = new ArrayList<>();

        // Nút 1: Bật / Tắt phó bản này (Đang bật -> hiện Tắt / Đang tắt -> hiện Bật)
        String toggleText = cfg.isEnabled ? "TẮT Phó Bản Này (Đang Bật)" : "BẬT Phó Bản Này (Đang Tắt)";
        short toggleIcon = cfg.isEnabled ? (short) 118 : (short) 133;
        menus.add(new model.Menu(toggleText, toggleIcon, () -> {
            try {
                cfg.isEnabled = !cfg.isEnabled;
                saveCache();
                p.getService().send_box_ThongBao_OK(cfg.name + " -> " + (cfg.isEnabled ? "Đã BẬT" : "Đã TẮT"));
                openTimedDungeonDetailMenuDynamic(p, cfg);
            } catch (Exception e) {
                Log.error("TimedDungeonManager", "Toggle error: " + e.getMessage());
            }
        }));

        // Nút 2: Khởi động ngay đợt mới (Chạy ngay lập tức)
        menus.add(new model.Menu("KHỞI ĐỘNG NGAY ĐỢT MỚI (Bắt Đầu Chạy Luôn)", (short) 136, () -> {
            try {
                cfg.state = ScheduleConfig.STATE_RUNNING;
                cfg.roundIndex++;
                cfg.nextStateChangeTime = System.currentTimeMillis() + (long) cfg.runDurationMinutes * 60_000L;
                cfg.currentTokenId = ZUtil.generateMD5Token(cfg.id + "_" + cfg.roundIndex + "_" + cfg.nextStateChangeTime);
                cfg.isEnabled = true;
                onStartDungeonRound(cfg);
                saveCache();
                try {
                    Manager.gI().chatKTG(0, "Phó bản [" + cfg.name + "] Đợt #" + cfg.roundIndex + " đã được Admin kích hoạt bắt đầu ngay lập tức!", 5);
                } catch (Exception ignored) {}
                p.getService().send_box_ThongBao_OK("Đã khởi động ngay Đợt #" + cfg.roundIndex + " cho " + cfg.name + "!\nThời gian chạy: " + cfg.runDurationMinutes + " phút.\nToken MD5: " + cfg.currentTokenId);
                openTimedDungeonDetailMenuDynamic(p, cfg);
            } catch (Exception e) {
                Log.error("TimedDungeonManager", "Start round error: " + e.getMessage());
            }
        }));

        // Nút 3: Đổi chế độ timing
        String modeText = (cfg.timingMode == ScheduleConfig.TIMING_MODE_SERVER_CYCLE)
                ? "Chế Độ: [CHU KỲ SERVER] (Bấm đổi sang Giờ Thực)"
                : "Chế Độ: [GIỜ THỰC CỐ ĐỊNH] (Bấm đổi sang Chu Kỳ)";
        menus.add(new model.Menu(modeText, (short) 110, () -> {
            try {
                cfg.timingMode = (cfg.timingMode == ScheduleConfig.TIMING_MODE_SERVER_CYCLE)
                        ? ScheduleConfig.TIMING_MODE_REAL_TIME
                        : ScheduleConfig.TIMING_MODE_SERVER_CYCLE;
                saveCache();
                p.getService().send_box_ThongBao_OK(
                    "Đã chuyển chế độ cho " + cfg.name + " sang:\n" +
                    (cfg.timingMode == ScheduleConfig.TIMING_MODE_SERVER_CYCLE ? "Chu kỳ Xoay Vòng từ lúc mở Server" : "Giờ thực cố định trong ngày")
                );
                openTimedDungeonDetailMenuDynamic(p, cfg);
            } catch (Exception e) {
                Log.error("TimedDungeonManager", "Mode toggle error: " + e.getMessage());
            }
        }));

        // Nút 4: Cài đặt chu kỳ chạy & chờ
        menus.add(new model.Menu("Cài Đặt Chu Kỳ (Số phút Chạy & Chờ)", (short) 155, () -> {
            try {
                p.sendInput("Nhập số phút Chạy và số phút Chờ đợt sau (Ví dụ: 30 15):", new String[]{"Phút_Chạy Phút_Chờ"}, (inputs) -> {
                    try {
                        if (inputs != null && inputs.length > 0 && !inputs[0].isBlank()) {
                            String[] parts = inputs[0].trim().split("\\s+");
                            if (parts.length >= 2) {
                                cfg.runDurationMinutes = Math.max(1, Integer.parseInt(parts[0]));
                                cfg.cooldownMinutes = Math.max(1, Integer.parseInt(parts[1]));
                                cfg.timingMode = ScheduleConfig.TIMING_MODE_SERVER_CYCLE;
                                saveCache();
                                p.getService().send_box_ThongBao_OK("Đã cập nhật chu kỳ cho " + cfg.name + ":\n- Chạy: " + cfg.runDurationMinutes + " phút\n- Chờ: " + cfg.cooldownMinutes + " phút");
                                openTimedDungeonDetailMenuDynamic(p, cfg);
                            }
                        }
                    } catch (Exception ex) {
                        try { p.getService().send_box_ThongBao_OK("Lỗi định dạng: " + ex.getMessage()); } catch (Exception ignored) {}
                    }
                });
            } catch (Exception e) {
                Log.error("TimedDungeonManager", "Set cycle input error: " + e.getMessage());
            }
        }));

        // Nút 5: Dịch chuyển đến Map phó bản này
        menus.add(new model.Menu("Dịch Chuyển Đến Map Phó Bản Này", (short) 116, () -> {
            try {
                int targetMapId = switch (cfg.id) {
                    case "DAU_TRUONG_TU_DO" -> 69;
                    case "TRAN_CHIEN_KHONG_LO" -> 107;
                    case "WORLD_WAR" -> 113;
                    case "BOSS_THE_GIOI" -> 107;
                    case "BAO_VE_PHAO_DAI" -> 88;
                    case "THU_LINH_BIEN_KHOI" -> 72;
                    case "CHIEM_DAO" -> 72;
                    case "PVP_BANG" -> 69;
                    default -> 1;
                };
                teleportPlayerToMap(p, targetMapId);
            } catch (Exception e) {
                Log.error("TimedDungeonManager", "Teleport error: " + e.getMessage());
            }
        }));

        // Nút 6: Xem chi tiết & Token MD5
        menus.add(new model.Menu("Xem Chi Tiết Trạng Thái & Token MD5", (short) 110, () -> {
            try {
                long remainSec = Math.max(0, (cfg.nextStateChangeTime - System.currentTimeMillis()) / 1000);
                String stateStr = (cfg.state == ScheduleConfig.STATE_RUNNING) ? "ĐANG CHẠY" : "ĐANG CHỜ COOLDOWN";
                String detail = "=== CHI TIẾT PHÓ BẢN: " + cfg.name.toUpperCase() + " ===\n" +
                    "• Trạng thái: " + (cfg.isEnabled ? "ĐANG BẬT" : "ĐANG TẮT") + "\n" +
                    "• Tiến trình hiện tại: " + stateStr + " (Còn " + (remainSec / 60) + "m " + (remainSec % 60) + "s)\n" +
                    "• Đợt hiện tại: Đợt #" + cfg.roundIndex + "\n" +
                    "• Chế độ thời gian: " + (cfg.timingMode == 1 ? "Chu kỳ xoay vòng server" : "Giờ thực cố định") + "\n" +
                    "• Cấu hình chu kỳ: " + cfg.runDurationMinutes + " phút chạy / " + cfg.cooldownMinutes + " phút chờ\n" +
                    "• Token MD5 đợt này: " + (cfg.currentTokenId != null ? cfg.currentTokenId : "Chưa có") + "\n" +
                    "• Thưởng đợt: " + cfg.rewardInfo;
                p.getService().send_box_ThongBao_OK(detail);
            } catch (Exception e) {
                Log.error("TimedDungeonManager", "View token error: " + e.getMessage());
            }
        }));

        // Nút 7: Quay lại danh sách
        menus.add(new model.Menu("Quay Lại Danh Sách", (short) 134, () -> {
            try {
                openTimedDungeonManagementMenu(p);
            } catch (Exception e) {
                Log.error("TimedDungeonManager", "Return to list error: " + e.getMessage());
            }
        }));

        String title = cfg.name + " (" + (cfg.isEnabled ? "Đang Bật" : "Đang Tắt") + ")";
        p.getService().openDynamicMenu(87202, title, menus);
    }

    public void handleTimedDungeonManagementMenu(Player p, int index) throws IOException {
        openTimedDungeonManagementMenu(p);
    }

    public void handleTimedDungeonDetailAction(Player p, int action) throws IOException {
        openTimedDungeonManagementMenu(p);
    }

    public void buffAdminFullStats(Player p) {
        try {
            p.updateVnd(2_000_000_000);
            p.update_ruby(2_000_000_000);
            p.update_vang(2_000_000_000);
            p.level = 150;
            p.hp = p.ability.get_hp_max(true);
            p.mp = p.ability.get_mp_max(true);
            p.updateMoney();

            p.getService().send_box_ThongBao_OK(
                "BẮT ĐẦU BUFF ADMIN FULL TÀI NGUYÊN\n" +
                "-2 Tỷ Extol\n" +
                "-2 Tỷ Ruby\n" +
                "-2 Tỷ Beri\n" +
                "-Level Max: Lv.150\n" +
                "-Phục Hồi 100% HP / MP Max!"
            );
        } catch (Exception e) {
            Log.error("TimedDungeonManager", "buffAdminFullStats error: " + e.getMessage());
        }
    }

    public void forceStartDungeon(String id) {
        ScheduleConfig cfg = configs.get(id);
        if (cfg != null) {
            long now = System.currentTimeMillis();
            cfg.state = ScheduleConfig.STATE_RUNNING;
            cfg.roundIndex++;
            cfg.currentTokenId = ZUtil.generateMD5Token(cfg.id + "_" + cfg.roundIndex + "_" + now);
            cfg.nextStateChangeTime = now + (long) cfg.runDurationMinutes * 60_000L;
            onStartDungeonRound(cfg);
            saveCache();
        }
    }

    public void forceEndDungeon(String id) {
        ScheduleConfig cfg = configs.get(id);
        if (cfg != null) {
            long now = System.currentTimeMillis();
            cfg.state = ScheduleConfig.STATE_IDLE;
            cfg.nextStateChangeTime = now + (long) cfg.cooldownMinutes * 60_000L;
            onEndDungeonRound(cfg);
            saveCache();
        }
    }

    public void toggleDungeon(String id, boolean enable) {
        ScheduleConfig cfg = configs.get(id);
        if (cfg != null) {
            cfg.isEnabled = enable;
            saveCache();
        }
    }

    public void setCycleDuration(String id, int runMin, int cdMin) {
        ScheduleConfig cfg = configs.get(id);
        if (cfg != null) {
            cfg.runDurationMinutes = runMin;
            cfg.cooldownMinutes = cdMin;
            saveCache();
        }
    }

    public void setMasterSwitch(boolean enable) {
        setGlobalMasterSwitch(enable);
        saveCache();
    }
}
