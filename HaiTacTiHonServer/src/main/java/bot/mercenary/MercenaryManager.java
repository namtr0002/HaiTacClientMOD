package bot.mercenary;

import ability.Ability;
import bot.mercenary.MercenaryTemplate.MercEntry;
import model.Player;
import database.DbManager;
import core.Log;
import core.ZUtil;
import network.SessionManager;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import map.Zone;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MercenaryManager — Quản lý Hợp đồng Lính Đánh Thuê chuẩn SQL + Cache.
 *
 * ==============================================================
 * QUY TẮC GIỚI HẠN & DỮ LIỆU:
 *   1. Giới hạn thuê: Tối đa 10 Hợp đồng lính đánh thuê / người chơi.
 *   2. Giới hạn xuất chiến: Tối đa 3 Lính được BẬT XUẤT CHIẾN (đi theo đánh quái).
 *   3. Lưu SQL `mercenary_contracts`: Giữ nguyên dữ liệu khi offline/server restart.
 *   4. Tự động kiểm tra thời gian hết hạn hợp đồng và dọn dẹp.
 * ==============================================================
 */
public class MercenaryManager {

    private static final MercenaryManager INSTANCE = new MercenaryManager();
    public static MercenaryManager gI() { return INSTANCE; }

    public static final int MAX_TOTAL_HIRED = 10;
    public static final int MAX_ACTIVE_BOTS = 1;

    private MercenaryManager() {
        // Luồng riêng tick AI di chuyển/đánh quái mỗi 400ms
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(400L);
                    tick();
                } catch (Exception ignored) {}
            }
        }, "Mercenary-Tick-Thread").start();
    }

    // ==================================================
    //  BẢN GHI HỢP ĐỒNG
    // ==================================================

    public static class HiredRecord {
        public long dbId;
        public final int hirerPlayerId;
        public final MercEntry entry;
        public MercenaryBot bot;
        public final long hiredAt;
        public final long expireAt;
        public final int currency;
        public boolean isActive;

        public HiredRecord(long dbId, int hirerPlayerId, MercEntry entry, MercenaryBot bot,
                           long hiredAt, long expireAt, int currency, boolean isActive) {
            this.dbId = dbId;
            this.hirerPlayerId = hirerPlayerId;
            this.entry = entry;
            this.bot = bot;
            this.hiredAt = hiredAt;
            this.expireAt = expireAt;
            this.currency = currency;
            this.isActive = isActive;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() >= expireAt;
        }

        public long getRemainingMinutes() {
            long rem = expireAt - System.currentTimeMillis();
            return rem <= 0 ? 0 : rem / 60_000L;
        }
    }

    // Map: playerId -> List<HiredRecord>
    private final Map<Integer, List<HiredRecord>> playerContractsMap = new ConcurrentHashMap<>();

    // Map: playerId -> (faction -> List<MercEntry>)
    private final Map<Integer, Map<Integer, List<MercEntry>>> playerRefreshedPools = new ConcurrentHashMap<>();

    // Map: playerId -> mercStatus
    private final Map<Integer, Integer> playerMercStatusMap = new ConcurrentHashMap<>();

    public MercenaryBot findBotByName(String name) {
        if (name == null) return null;
        for (List<HiredRecord> records : playerContractsMap.values()) {
            if (records != null) {
                for (HiredRecord rec : records) {
                    if (rec != null && rec.bot != null && rec.bot.name != null && rec.bot.name.equalsIgnoreCase(name)) {
                        return rec.bot;
                    }
                }
            }
        }
        return null;
    }

    public int getPlayerMercStatus(Player p) {
        if (p == null) return MercenaryBot.STATUS_ATTACK;
        return playerMercStatusMap.getOrDefault(p.IDPlayer, MercenaryBot.STATUS_ATTACK);
    }

    public void setPlayerMercStatus(Player p, int status) {
        if (p == null) return;
        playerMercStatusMap.put(p.IDPlayer, status);

        if (status == MercenaryBot.STATUS_HOME) {
            recallAllBots(p);
            return;
        }

        List<HiredRecord> contracts = getPlayerContracts(p);
        if (contracts.isEmpty()) {
            try {
                p.getService().send_box_ThongBao_OK("Bạn chưa có Hợp Đồng Lính Đánh Thuê nào!");
            } catch (Exception ignored) {}
            return;
        }

        summonAllActiveBots(p);

        try {
            p.getService().send_box_ThongBao_OK("Lính đánh thuê đã đổi sang trạng thái: " + MercenaryBot.STATUS_NAMES[status] + "!");
        } catch (Exception ignored) {}
    }

    public List<MercEntry> getRefreshedList(Player p, int faction) {
        if (p == null) return MercenaryTemplate.getByFaction(faction);
        Map<Integer, List<MercEntry>> map = playerRefreshedPools.computeIfAbsent(p.IDPlayer, k -> new ConcurrentHashMap<>());
        List<MercEntry> list = map.get(faction);
        if (list == null || list.isEmpty()) {
            list = refreshFactionList(p, faction);
        }
        return list;
    }

    public List<MercEntry> refreshFactionList(Player p, int faction) {
        if (p == null) return MercenaryTemplate.getByFaction(faction);
        List<MercEntry> newList = MercenaryTemplate.generateRandomCandidates(p, faction);
        Map<Integer, List<MercEntry>> map = playerRefreshedPools.computeIfAbsent(p.IDPlayer, k -> new ConcurrentHashMap<>());
        map.put(faction, newList);
        return newList;
    }

    public MercEntry getRefreshedEntry(Player p, int faction, int botId) {
        List<MercEntry> list = getRefreshedList(p, faction);
        if (list != null) {
            for (MercEntry e : list) {
                if (e != null && e.id == botId) return e;
            }
        }
        return MercenaryTemplate.getById(botId);
    }

    /**
     * Lấy danh sách các Bot Lính Đánh Thuê đang bật xuất chiến của người chơi.
     */
    public List<MercenaryBot> getActiveBots(Player owner) {
        List<MercenaryBot> result = new ArrayList<>();
        if (owner == null || owner.isBot) return result;
        List<HiredRecord> list = playerContractsMap.get(owner.IDPlayer);
        if (list != null) {
            for (HiredRecord rec : list) {
                if (rec != null && rec.isActive && !rec.isExpired() && rec.bot != null) {
                    result.add(rec.bot);
                }
            }
        }
        return result;
    }

    /**
     * Gửi giao diện xem thông tin / trang bị lính đánh thuê chuẩn giao diện Player (-42).
     */
    public void sendMercenaryProfile(Player viewer, MercEntry entry, HiredRecord rec) {
        if (viewer == null) return;
        try {
            Player target = null;
            if (rec != null && rec.bot != null) {
                target = rec.bot;
            } else if (entry != null) {
                target = MercenaryBot.createPreview(viewer, entry);
            }
            if (target != null) {
                viewer.getService().send_view_other_player(target);
            }
        } catch (Exception e) {
            Log.error("MercenaryManager", "sendMercenaryProfile error: " + e.getMessage());
        }
    }

    // ==================================================
    //  LOAD CONTRACTS KHI NGƯỜI CHƠI VÀO GAME
    // ==================================================

    public void loadPlayerContracts(Player p) {
        if (p == null || p.isBot) return;
        List<HiredRecord> oldList = playerContractsMap.get(p.IDPlayer);
        if (oldList != null) {
            for (HiredRecord r : oldList) {
                if (r != null && r.bot != null) {
                    r.bot.despawn();
                    r.bot = null;
                }
            }
        }
        for (Player bot : new ArrayList<>(SessionManager.PLAYERS_MAP.values())) {
            if (bot instanceof MercenaryBot && ((MercenaryBot) bot).ownerPlayerId == p.IDPlayer) {
                ((MercenaryBot) bot).despawn();
            }
        }
        List<HiredRecord> list = new ArrayList<>();
        long now = System.currentTimeMillis();
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM `mercenary_contracts` WHERE `player_id` = ? AND `expire_at` > ?")) {
            ps.setInt(1, p.IDPlayer);
            ps.setLong(2, now);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long dbId      = rs.getLong("id");
                    int templateId = rs.getInt("bot_template_id");
                    String botName = rs.getString("bot_name");
                    int faction    = rs.getInt("faction");
                    double tier    = rs.getDouble("tier");
                    int level      = rs.getInt("level");
                    int currency   = rs.getInt("currency");
                    boolean active = rs.getBoolean("is_active");
                    long hiredAt   = rs.getLong("hired_at");
                    long expireAt  = rs.getLong("expire_at");
                    byte clazz = 1;
                    try {
                        clazz = rs.getByte("clazz");
                    } catch (Exception ignored) {}
                    if (clazz <= 0) {
                        clazz = (byte) (Math.abs((botName != null ? botName.hashCode() : templateId)) % 5 + 1);
                    }
                    MercEntry entry = MercenaryTemplate.getById(templateId);
                    if (entry == null) {
                        String dataStr = null;
                        try {
                            dataStr = rs.getString("data");
                        } catch (Exception ignored) {}
                        int maxLv = Math.min(110, level + 10);
                        int skillLv = (tier >= 3.5) ? 30 : (tier >= 2.5 ? 25 : 22);
                        boolean hasHoanMy = (tier >= MercenaryTemplate.TIER_HIGH);
                        boolean hasKhamDa = true;
                        boolean hasKichAn = (tier >= MercenaryTemplate.TIER_NEAR_FULL);
                        int pExtol = (tier >= 3.5) ? 30000 : (tier >= 2.5 ? 20000 : 15000);
                        int dExtol = (tier >= 3.5) ? 240 : 180;
                        int pRuby  = (tier >= 3.5) ? 600 : (tier >= 2.5 ? 400 : 250);
                        int dRuby  = (tier >= 3.5) ? 180 : 120;
                        long pBeri = (tier >= 3.5) ? 40_000_000L : (tier >= 2.5 ? 20_000_000L : 10_000_000L);
                        int dBeri  = (tier >= 3.5) ? 90 : (tier >= 2.5 ? 60 : 45);
                        if (dataStr != null && !dataStr.isEmpty()) {
                            try {
                                Object obj = JSONValue.parse(dataStr);
                                if (obj instanceof JSONObject) {
                                    JSONObject json = (JSONObject) obj;
                                    if (json.containsKey("maxLv")) maxLv = ((Number) json.get("maxLv")).intValue();
                                    if (json.containsKey("skillLv")) skillLv = ((Number) json.get("skillLv")).intValue();
                                     if (json.containsKey("hoanMy")) hasHoanMy = zabstracts.AbsRanked.jBool(json, "hoanMy", false);
                                     if (json.containsKey("khamDa")) hasKhamDa = zabstracts.AbsRanked.jBool(json, "khamDa", false);
                                     if (json.containsKey("kichAn")) hasKichAn = zabstracts.AbsRanked.jBool(json, "kichAn", false);
                                    if (json.containsKey("pExtol")) pExtol = ((Number) json.get("pExtol")).intValue();
                                    if (json.containsKey("dExtol")) dExtol = ((Number) json.get("dExtol")).intValue();
                                    if (json.containsKey("pRuby")) pRuby = ((Number) json.get("pRuby")).intValue();
                                    if (json.containsKey("dRuby")) dRuby = ((Number) json.get("dRuby")).intValue();
                                    if (json.containsKey("pBeri")) pBeri = ((Number) json.get("pBeri")).longValue();
                                    if (json.containsKey("dBeri")) dBeri = ((Number) json.get("dBeri")).intValue();
                                }
                            } catch (Exception ignored) {}
                        }

                        entry = new MercEntry(
                            templateId, botName, faction, clazz,
                            level, maxLv, tier, skillLv,
                            hasHoanMy, hasKhamDa, hasKichAn,
                            pExtol, dExtol, pRuby, dRuby, pBeri, dBeri
                        );
                    }

                    MercenaryBot bot = null;
                    if (active && countActive(list) < MAX_ACTIVE_BOTS) {
                        bot = MercenaryBot.create(p, entry, currency);
                    } else {
                        active = false;
                    }

                    HiredRecord rec = new HiredRecord(dbId, p.IDPlayer, entry, bot, hiredAt, expireAt, currency, active);
                    list.add(rec);
                }
            }
            try (PreparedStatement delPs = conn.prepareStatement(
                "DELETE FROM `mercenary_contracts` WHERE `player_id` = ? AND `expire_at` <= ?")) {
                delPs.setInt(1, p.IDPlayer);
                delPs.setLong(2, now);
                delPs.executeUpdate();
            }

        } catch (Exception e) {
            Log.error("MercenaryManager", "loadPlayerContracts error: " + e.getMessage());
        }

        playerContractsMap.put(p.IDPlayer, list);
        if (!list.isEmpty()) {
            // Log.info("MercenaryManager", "Loaded " + list.size() + " contracts for player " + p.name);
        }
    }

    public void onPlayerLogout(Player p) {
        if (p == null) return;
        List<HiredRecord> oldList = playerContractsMap.remove(p.IDPlayer);
        if (oldList != null) {
            for (HiredRecord r : oldList) {
                if (r != null && r.bot != null) {
                    r.bot.despawn();
                    r.bot = null;
                }
            }
        }
    }

    public List<HiredRecord> getPlayerContracts(Player p) {
        if (p == null) return Collections.emptyList();
        List<HiredRecord> list = playerContractsMap.get(p.IDPlayer);
        if (list == null) {
            loadPlayerContracts(p);
            list = playerContractsMap.get(p.IDPlayer);
        } else {
            for (HiredRecord r : list) {
                if (r != null && r.bot != null) {
                    r.bot.syncOwnerIdentity(p);
                }
            }
        }
        return list != null ? list : Collections.emptyList();
    }

    public int countActive(List<HiredRecord> list) {
        int count = 0;
        for (HiredRecord r : list) {
            if (r.isActive && !r.isExpired()) count++;
        }
        return count;
    }

    // ==================================================
    //  THUÊ LÍNH ĐÁNH THUÊ MỚI (LƯU DB & CACHE)
    // ==================================================

    public boolean hireMercenary(Player p, MercEntry entry, int currency) {
        if (p != null && p.IDPlayer != 0) {
            network.SessionManager.PLAYERS_MAP.put(p.IDPlayer, p);
        }

        // Chặn tân thủ đầu game thuê lính làm phá vỡ cân bằng
        if (p == null || p.level < 40) {
            try { p.getService().send_box_ThongBao_OK("Bạn cần đạt cấp độ 40 trở lên để có thể thuê Lính Đánh Thuê!"); }
            catch (Exception ignored) {}
            return false;
        }

        if (entry != null && p.level < entry.minLv) {
            try { p.getService().send_box_ThongBao_OK("Cấp độ của bạn (Lv." + p.level + ") chưa đủ để thuê " + entry.name + "!\nYêu cầu tối thiểu Lv." + entry.minLv + "."); }
            catch (Exception ignored) {}
            return false;
        }

        List<HiredRecord> list = getPlayerContracts(p);

        if (list.size() >= MAX_TOTAL_HIRED) {
            try { p.getService().send_box_ThongBao_OK("Bạn đã thuê tối đa 10 lính đánh thuê! Hãy sa thải bớt lính trước khi thuê thêm."); }
            catch (Exception ignored) {}
            return false;
        }

        if (!deductCurrency(p, entry, currency)) {
            return false;
        }

        int durationMinutes;
        switch (currency) {
            case MercenaryTemplate.CURRENCY_EXTOL: durationMinutes = entry.durationExtol; break;
            case MercenaryTemplate.CURRENCY_RUBY:  durationMinutes = entry.durationRuby;  break;
            default:                               durationMinutes = entry.durationBeri;  break;
        }
        long now = System.currentTimeMillis();
        long expireAt = now + (long) durationMinutes * 60_000L;

        boolean isActive = (countActive(list) < MAX_ACTIVE_BOTS);
        MercenaryBot bot = null;
        if (isActive) {
            bot = MercenaryBot.create(p, entry, currency);
        }

        long dbId = saveContractToDb(p.IDPlayer, entry, currency, isActive, now, expireAt);
        if (dbId <= 0) {
            refundCurrency(p, entry, currency);
            try { p.getService().send_box_ThongBao_OK("Lỗi hệ thống lưu dữ liệu! Đã hoàn lại tiền."); }
            catch (Exception ignored) {}
            return false;
        }

        HiredRecord rec = new HiredRecord(dbId, p.IDPlayer, entry, bot, now, expireAt, currency, isActive);
        list.add(rec);

        // Flush player data immediately to persist currency deduction in DB and Cache
        try {
            p.flush(p, false);
        } catch (Exception ignored) {}

        String currencyName = MercenaryTemplate.CURRENCY_NAME[currency];
        try {
            p.getService().send_box_ThongBao_OK(
                "Đã thuê thành công!\n" +
                "Lính: " + entry.name + "\n" +
                "Trạng thái: " + (isActive ? "Xuất chiến ngay" : "Lưu trong danh sách (Nghỉ ngơi)") + "\n" +
                "Thời gian: " + durationMinutes + " phút\n" +
                "Hợp đồng đã được lưu an toàn vào hệ thống!"
            );
        } catch (Exception ignored) {}

        // Log.info("MercenaryManager", "Player " + p.name + " hired " + entry.name +
        //     " (active=" + isActive + ") using " + currencyName);
        return true;
    }

    private long saveContractToDb(int playerId, MercEntry entry, int currency, boolean isActive, long hiredAt, long expireAt) {
        String sql = "INSERT INTO `mercenary_contracts` (`player_id`, `bot_template_id`, `bot_name`, `faction`, `clazz`, `tier`, `level`, `currency`, `is_active`, `hired_at`, `expire_at`, `data`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, playerId);
            ps.setInt(2, entry.id);
            ps.setString(3, entry.name);
            ps.setInt(4, entry.faction);
            ps.setByte(5, entry.clazz);
            ps.setDouble(6, entry.tier);
            ps.setInt(7, entry.minLv);
            ps.setInt(8, currency);
            ps.setBoolean(9, isActive);
            ps.setLong(10, hiredAt);
            ps.setLong(11, expireAt);

            JSONObject json = new JSONObject();
            json.put("maxLv", entry.maxLv);
            json.put("skillLv", entry.skillLv);
            json.put("hoanMy", entry.hasHoanMy);
            json.put("khamDa", entry.hasKhamDa);
            json.put("kichAn", entry.hasKichAn);
            json.put("pExtol", entry.priceExtol);
            json.put("dExtol", entry.durationExtol);
            json.put("pRuby", entry.priceRuby);
            json.put("dRuby", entry.durationRuby);
            json.put("pBeri", entry.priceBeri);
            json.put("dBeri", entry.durationBeri);
            ps.setString(12, json.toJSONString());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (Exception e) {
            Log.error("MercenaryManager", "saveContractToDb error: " + e.getMessage());
        }
        return -1;
    }

    // ==================================================
    //  BẬT / TẮT XUẤT CHIẾN (TOGGLE ACTIVE)
    // ==================================================

    public void toggleActiveStatus(Player p, HiredRecord rec) {
        if (rec == null || rec.isExpired()) return;

        List<HiredRecord> list = getPlayerContracts(p);

        if (rec.isActive) {
            // Đang xuất chiến -> Chuyển sang Nghỉ ngơi
            rec.isActive = false;
            if (rec.bot != null) {
                rec.bot.despawn();
                rec.bot = null;
            }
            updateActiveDb(rec.dbId, false);
            try { p.getService().send_box_ThongBao_OK("Đã rút " + rec.entry.name + " về trạng thái nghỉ ngơi."); }
            catch (Exception ignored) {}

        } else {
            // Đang nghỉ ngơi -> Chuyển sang Xuất chiến
            if (p.map != null && isPvpMap(p.map)) {
                try { p.getService().send_box_ThongBao_OK("Không thể triệu hồi lính đánh thuê trong khu vực PVP / Phòng chờ!"); }
                catch (Exception ignored) {}
                return;
            }
            if (countActive(list) >= MAX_ACTIVE_BOTS) {
                try { p.getService().send_box_ThongBao_OK("Bạn chỉ có thể cho tối đa " + MAX_ACTIVE_BOTS + " lính xuất chiến cùng lúc! Hãy cho lính hiện tại nghỉ ngơi trước."); }
                catch (Exception ignored) {}
                return;
            }

            if (rec.bot != null) {
                rec.bot.despawn();
                rec.bot = null;
            }

            rec.isActive = true;
            rec.bot = MercenaryBot.create(p, rec.entry, rec.currency);
            if (rec.bot != null) {
                rec.bot.mercStatus = getPlayerMercStatus(p);
            }
            updateActiveDb(rec.dbId, true);
            try { p.getService().send_box_ThongBao_OK("Đã gọi " + rec.entry.name + " xuất chiến cùng bạn!"); }
            catch (Exception ignored) {}
        }
    }

    /**
     * Rút toàn bộ lính đánh thuê về (trạng thái ẩn/nghỉ ngơi) và bắt buộc ở trạng thái về.
     * Cập nhật isActive = false vào DB và cache, despawn bot khỏi map, không cho spawn lại.
     */
    public void recallAllBots(Player p) {
        if (p == null) return;
        playerMercStatusMap.put(p.IDPlayer, MercenaryBot.STATUS_HOME);
        List<HiredRecord> contracts = getPlayerContracts(p);
        int hiddenCount = 0;
        for (HiredRecord r : contracts) {
            if (r != null) {
                r.isActive = false;
                updateActiveDb(r.dbId, false);
                if (r.bot != null) {
                    r.bot.mercStatus = MercenaryBot.STATUS_HOME;
                    r.bot.isChangingMap = true;
                    try {
                        r.bot.despawn();
                    } catch (Throwable ignored) {}
                    r.bot = null;
                    hiddenCount++;
                }
            }
        }
        for (Player bot : new ArrayList<>(SessionManager.PLAYERS_MAP.values())) {
            if (bot instanceof MercenaryBot && ((MercenaryBot) bot).ownerPlayerId == p.IDPlayer) {
                ((MercenaryBot) bot).mercStatus = MercenaryBot.STATUS_HOME;
                try {
                    ((MercenaryBot) bot).despawn();
                } catch (Throwable ignored) {}
            }
        }
        try {
            p.getService().send_box_ThongBao_OK("Đã rút " + (hiddenCount > 0 ? hiddenCount : "toàn bộ") + " Lính Đánh Thuê về (nghỉ ngơi/ẩn khỏi map)!");
        } catch (Exception ignored) {}
    }

    /**
     * Triệu hồi / xuất chiến toàn bộ lính đánh thuê hợp lệ (tối đa MAX_ACTIVE_BOTS).
     */
    public void summonAllActiveBots(Player p) {
        if (p == null || p.map == null) return;

        if (isPvpMap(p.map)) {
            try {
                p.getService().send_box_ThongBao_OK("Lính đánh thuê tạm thời bị cấm xuất chiến trong bản đồ PVP!");
            } catch (Exception ignored) {}
            return;
        }

        List<HiredRecord> contracts = getPlayerContracts(p);
        if (contracts.isEmpty()) {
            try {
                p.getService().send_box_ThongBao_OK("Bạn chưa có Hợp Đồng Lính Đánh Thuê nào!\nHãy chọn Hải Quân, Hải Tặc hoặc Quân Cách Mạng để thuê lính xuất chiến!");
            } catch (Exception ignored) {}
            return;
        }

        int curStatus = getPlayerMercStatus(p);
        if (curStatus == MercenaryBot.STATUS_HOME) {
            curStatus = MercenaryBot.STATUS_ATTACK;
            playerMercStatusMap.put(p.IDPlayer, curStatus);
        }

        int activeCount = countActive(contracts);
        if (activeCount == 0) {
            int toActivate = Math.min(MAX_ACTIVE_BOTS, contracts.size());
            for (int i = 0; i < toActivate; i++) {
                HiredRecord r = contracts.get(i);
                if (!r.isExpired()) {
                    r.isActive = true;
                    updateActiveDb(r.dbId, true);
                }
            }
        }

        int spawnedCount = 0;
        for (HiredRecord r : contracts) {
            if (!r.isActive || r.isExpired()) continue;

            if (r.bot != null) r.bot.isChangingMap = true;

            try {
                if (r.bot == null) {
                    r.bot = MercenaryBot.create(p, r.entry, r.currency);
                    if (r.bot != null) {
                        r.bot.mercStatus = curStatus;
                    }
                } else if (r.bot.map == null) {
                    r.bot.syncOwnerIdentity(p);
                    r.bot.lastJoinTime = System.currentTimeMillis();
                    r.bot.join(p.map, (short) (p.x + r.bot.getOffsetX(p)), (short) (p.y + r.bot.getOffsetY(p)));
                    r.bot.mercStatus = curStatus;
                } else if (!r.bot.map.equals(p.map)) {
                    r.bot.leave();
                    r.bot.syncOwnerIdentity(p);
                    r.bot.lastJoinTime = System.currentTimeMillis();
                    r.bot.join(p.map, (short) (p.x + r.bot.getOffsetX(p)), (short) (p.y + r.bot.getOffsetY(p)));
                    r.bot.mercStatus = curStatus;
                } else {
                    r.bot.teleportToOwner(p);
                    r.bot.mercStatus = curStatus;
                }
                spawnedCount++;
            } catch (Throwable ignored) {
            } finally {
                if (r.bot != null) r.bot.isChangingMap = false;
            }
        }

        try {
            p.getService().send_box_ThongBao_OK("Đã xuất chiến " + spawnedCount + " Lính Đánh Thuê tập hợp bên cạnh bạn!");
        } catch (Exception ignored) {}
    }

    /**
     * Tạm thời rút toàn bộ lính đánh thuê ra khỏi map (ví dụ khi chủ nhân vào chế độ xem spectator).
     * Tuyệt đối không thay đổi isActive trong database.
     */
    public void recallBots(Player owner) {
        if (owner == null) return;
        List<HiredRecord> contracts = getPlayerContracts(owner);
        for (HiredRecord r : contracts) {
            if (r != null && r.bot != null) {
                r.bot.isChangingMap = true;
                try {
                    r.bot.leave();
                } catch (Throwable ignored) {}
                r.bot.isChangingMap = false;
            }
        }
    }

    public boolean hasSpawnedBots(Player p) {
        if (p == null) return false;
        List<HiredRecord> contracts = getPlayerContracts(p);
        for (HiredRecord r : contracts) {
            if (r.isActive && !r.isExpired() && r.bot != null && r.bot.map != null && !r.bot.isdie) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActiveContracts(Player p) {
        if (p == null) return false;
        List<HiredRecord> contracts = getPlayerContracts(p);
        for (HiredRecord r : contracts) {
            if (r.isActive && !r.isExpired()) return true;
        }
        return false;
    }

    public boolean hasAnyContracts(Player p) {
        if (p == null) return false;
        List<HiredRecord> contracts = getPlayerContracts(p);
        for (HiredRecord r : contracts) {
            if (!r.isExpired()) return true;
        }
        return false;
    }

    public boolean isPvpMap(Zone zone) {
        return MercenaryBot.isRestrictedPvpMap(zone);
    }

    public void toggleOrSummonBotsInstant(Player p) {
        if (p == null || p.map == null) return;
        if (hasSpawnedBots(p)) {
            recallAllBots(p);
        } else {
            summonAllActiveBots(p);
        }
    }

    public void onPlayerChangeMap(Player p, Zone newZone) {
        if (p == null || newZone == null) return;

        if (p.isSpectator) {
            // Khi đang ở chế độ xem trận đấu -> Tạm ẩn toàn bộ lính
            recallBots(p);
            return;
        }

        if (isPvpMap(newZone)) {
            // Vào map PVP -> Tạm ẩn toàn bộ lính
            List<HiredRecord> contracts = getPlayerContracts(p);
            boolean hadBot = false;
            for (HiredRecord r : contracts) {
                if (r.bot != null) {
                    hadBot = true;
                    break;
                }
            }
            recallBots(p);
            if (hadBot) {
                try {
                    p.getService().send_box_ThongBao_OK("Lính Đánh Thuê tạm thời ẩn khi bạn vào khu vực PVP!");
                } catch (Exception ignored) {}
            }
        } else {
            int playerMercStatus = getPlayerMercStatus(p);
            if (playerMercStatus == MercenaryBot.STATUS_HOME) {
                return;
            }
            // Sang map thường / phó bản / map PvP -> đồng bộ lính sang map mới
            List<HiredRecord> contracts = getPlayerContracts(p);
            for (HiredRecord r : contracts) {
                if (!r.isActive || r.isExpired()) continue;
                if (r.bot != null && (r.bot.isdie || r.bot.mercStatus == MercenaryBot.STATUS_HOME)) continue; // Nếu đang chết hoặc về nhà thì không di chuyển map

                // Lock tick trước để tránh race condition với mercTick()
                if (r.bot != null) r.bot.isChangingMap = true;

                try {
                    if (r.bot == null) {
                        // Bot chưa tồn tại -> tạo mới (create() tự join vào map owner)
                        r.bot = MercenaryBot.create(p, r.entry, r.currency);
                        if (r.bot != null) {
                            r.bot.mercStatus = getPlayerMercStatus(p);
                        }
                    } else if (r.bot.map == null) {
                        // Bot tồn tại nhưng chưa ở map nào -> join trực tiếp
                        r.bot.syncOwnerIdentity(p);
                        r.bot.lastJoinTime = System.currentTimeMillis();
                        r.bot.join(newZone, (short) (p.x + r.bot.getOffsetX(p)), (short) (p.y + r.bot.getOffsetY(p)));
                        r.bot.mercStatus = getPlayerMercStatus(p);
                    } else if (!r.bot.map.equals(newZone)) {
                        // Bot đang ở map khác -> leave sạch rồi join map mới
                        r.bot.leave();
                        r.bot.syncOwnerIdentity(p);
                        r.bot.lastJoinTime = System.currentTimeMillis();
                        r.bot.join(newZone, (short) (p.x + r.bot.getOffsetX(p)), (short) (p.y + r.bot.getOffsetY(p)));
                        r.bot.mercStatus = getPlayerMercStatus(p);
                    }
                    // Nếu bot.map == newZone: bot đã đúng chỗ, không làm gì thêm
                } catch (Throwable ignored) {
                } finally {
                    // Mở lock cho tick sau khi thao tác xong
                    if (r.bot != null) r.bot.isChangingMap = false;
                }
            }
        }
    }

    private void updateActiveDb(long dbId, boolean isActive) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE `mercenary_contracts` SET `is_active` = ? WHERE `id` = ?")) {
            ps.setBoolean(1, isActive);
            ps.setLong(2, dbId);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error("MercenaryManager", "updateActiveDb error: " + e.getMessage());
        }
    }

    // ==================================================
    //  SA THẢI LÍNH (HỦY HỢP ĐỒNG)
    // ==================================================

    public void dismissRecord(Player p, HiredRecord rec) {
        if (rec == null) return;

        List<HiredRecord> list = getPlayerContracts(p);
        list.remove(rec);

        if (rec.bot != null) {
            rec.bot.despawn();
            rec.bot = null;
        }

        deleteContractDb(rec.dbId);
        try {
            p.getService().send_box_ThongBao_OK("Đã sa thải " + rec.entry.name + " thành công!");
        } catch (Exception ignored) {}
    }

    public void dismissAll(Player p) {
        List<HiredRecord> list = getPlayerContracts(p);
        if (list.isEmpty()) {
            try { p.getService().send_box_ThongBao_OK("Bạn chưa thuê lính đánh thuê nào!"); }
            catch (Exception ignored) {}
            return;
        }

        for (HiredRecord rec : new ArrayList<>(list)) {
            if (rec.bot != null) {
                rec.bot.despawn();
                rec.bot = null;
            }
            deleteContractDb(rec.dbId);
        }
        list.clear();
        for (Player bot : new ArrayList<>(network.SessionManager.PLAYERS_MAP.values())) {
            if (bot instanceof MercenaryBot && ((MercenaryBot) bot).ownerPlayerId == p.IDPlayer) {
                ((MercenaryBot) bot).despawn();
            }
        }
        try { p.getService().send_box_ThongBao_OK("Đã sa thải toàn bộ lính đánh thuê!"); }
        catch (Exception ignored) {}
    }

    private void deleteContractDb(long dbId) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM `mercenary_contracts` WHERE `id` = ?")) {
            ps.setLong(1, dbId);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error("MercenaryManager", "deleteContractDb error: " + e.getMessage());
        }
    }

    // ==================================================
    //  TICK VÒNG LỜI (400MS)
    // ==================================================

    public void tick() {
        if (playerContractsMap.isEmpty()) return;

        for (Map.Entry<Integer, List<HiredRecord>> entry : playerContractsMap.entrySet()) {
            List<HiredRecord> list = entry.getValue();
            if (list == null || list.isEmpty()) continue;

            Iterator<HiredRecord> it = list.iterator();
            while (it.hasNext()) {
                HiredRecord rec = it.next();
                if (rec.isExpired()) {
                    if (rec.bot != null) {
                        rec.bot.despawn();
                    }
                    deleteContractDb(rec.dbId);
                    it.remove();

                    Player hirer = network.SessionManager.PLAYERS_MAP.get(rec.hirerPlayerId);
                    if (hirer != null && hirer.getService() != null) {
                        try {
                            hirer.getService().send_box_ThongBao_OK(
                                "Hợp đồng với " + rec.entry.name + " đã hết hạn!"
                            );
                        } catch (Exception ignored) {}
                    }
                } else if (rec.isActive) {
                    Player hirer = network.SessionManager.PLAYERS_MAP.get(rec.hirerPlayerId);
                    if (hirer != null && hirer.map != null && !hirer.isSpectator && hirer.conn != null) {
                        int hirerStatus = getPlayerMercStatus(hirer);
                        if (hirerStatus != MercenaryBot.STATUS_HOME && (rec.bot == null || rec.bot.mercStatus != MercenaryBot.STATUS_HOME)) {
                            if (rec.bot == null) {
                                if (!isPvpMap(hirer.map)) {
                                    rec.bot = MercenaryBot.create(hirer, rec.entry, rec.currency);
                                    if (rec.bot != null) {
                                        rec.bot.mercStatus = hirerStatus;
                                    }
                                }
                            } else if (!rec.bot.isdie && !rec.bot.isChangingMap) {
                                if (rec.bot.map == null || !rec.bot.map.equals(hirer.map)) {
                                    if (!isPvpMap(hirer.map)) {
                                        rec.bot.teleportToOwner(hirer);
                                    }
                                }
                            }
                        }
                    }
                    if (rec.bot != null) {
                        rec.bot.mercTick();
                    }
                }
            }
        }
    }

    // ==================================================
    //  CURRENCY HELPERS
    // ==================================================

    private boolean deductCurrency(Player p, MercEntry entry, int currency) {
        try {
            switch (currency) {
                case MercenaryTemplate.CURRENCY_EXTOL: {
                    if (p.get_vnd() < entry.priceExtol) {
                        p.getService().send_box_ThongBao_OK(
                            "Không đủ Extol! Cần " + ZUtil.number_format(entry.priceExtol) + " Extol.\n" +
                            "Bạn có: " + ZUtil.number_format(p.get_vnd()) + " Extol."
                        );
                        return false;
                    }
                    p.updateVnd(-entry.priceExtol);
                    p.updateMoney();
                    return true;
                }
                case MercenaryTemplate.CURRENCY_RUBY: {
                    if (p.get_ngoc() < entry.priceRuby) {
                        p.getService().send_box_ThongBao_OK(
                            "Không đủ Ruby! Cần " + ZUtil.number_format(entry.priceRuby) + " Ruby.\n" +
                            "Bạn có: " + ZUtil.number_format(p.get_ngoc()) + " Ruby."
                        );
                        return false;
                    }
                    p.update_ngoc(-entry.priceRuby);
                    p.updateMoney();
                    return true;
                }
                case MercenaryTemplate.CURRENCY_BERI:
                default: {
                    if (p.get_vang() < entry.priceBeri) {
                        p.getService().send_box_ThongBao_OK("Không đủ Beri! Cần " + ZUtil.number_format(entry.priceBeri) + " Beri.\n" +
                            "Bạn có: " + ZUtil.number_format(p.get_vang()) + " Beri."
                        );
                        return false;
                    }
                    p.update_vang(-entry.priceBeri);
                    p.updateMoney();
                    return true;
                }
            }
        } catch (Exception e) {
            Log.error("MercenaryManager", "deductCurrency error: " + e.getMessage());
        }
        return false;
    }

    public void upgradeMercenaryLevel(Player p, HiredRecord rec) {
        if (p == null || rec == null || rec.isExpired()) return;
        int curLv = (rec.bot != null) ? rec.bot.level : rec.entry.maxLv;
        if (curLv >= 110) {
            try { p.getService().send_box_ThongBao_OK("Lính đánh thuê này đã đạt Cấp độ Tối Đa (Lv.110)!"); } catch (Exception ignored) {}
            return;
        }

        if (curLv >= p.level) {
            try { p.getService().send_box_ThongBao_OK("Cấp độ của lính đánh thuê không thể vượt quá cấp độ của bạn (Lv." + p.level + ")!\nHãy rèn luyện nâng cấp bản thân trước."); } catch (Exception ignored) {}
            return;
        }

        long costBeri = MercenaryTemplate.getUpgradeLevelCostBeri(curLv);
        if (p.get_vang() < costBeri) {
            try { p.getService().send_box_ThongBao_OK("Bạn không đủ Beri để nâng cấp! Cần: " + ZUtil.number_format(costBeri) + " Beri."); } catch (Exception ignored) {}
            return;
        }

        p.update_vang(-costBeri);
        try { p.updateMoney(); } catch (Exception ignored) {}
        try { p.flush(p, false); } catch (Exception ignored) {}
        int newLv = Math.min(p.level, Math.min(110, curLv + 5));
        if (rec.bot != null) {
            try {
                rec.bot.level = (short) newLv;
                long totalPoints = (long) rec.bot.level * 10;
                rec.bot.pointAttribute = 0;
                rec.bot.point1 = totalPoints / 5;
                rec.bot.point2 = totalPoints / 5;
                rec.bot.point3 = totalPoints / 5;
                rec.bot.point4 = totalPoints / 5;
                rec.bot.point5 = totalPoints - (rec.bot.point1 * 4);
                int fruitId = MercenaryTemplate.getFruitIdForEntry(rec.entry);
                int chosenThanTrangSet = Player.selectThanTrangSetIndex(rec.bot.clazz, fruitId, rec.bot.name);
                double maxTier = MercenaryTemplate.getMaxAllowedTier(newLv);
                double effectiveTier = Math.min(maxTier, rec.entry.tier);
                rec.bot.setupBotEquip(rec.bot.clazz, rec.bot.level, effectiveTier, rec.entry.hasHoanMy && effectiveTier >= 2.0, effectiveTier >= 1.5, rec.entry.hasKichAn && effectiveTier >= 2.5, chosenThanTrangSet);
                rec.bot.setupBotSkills(rec.bot.clazz, (short) Math.max(rec.entry.skillLv, newLv >= 80 ? 30 : 20), effectiveTier, fruitId);
                rec.bot.setupBalancedMercenaryStats();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            p.getService().send_box_ThongBao_OK("Nâng cấp " + rec.entry.name + " lên Cấp " + newLv + " thành công!\nTrừ " + ZUtil.number_format(costBeri) + " Beri.");
        } catch (Exception ignored) {}
    }

    public void upgradeMercenaryTier(Player p, HiredRecord rec) {
        if (p == null || rec == null || rec.isExpired()) return;
        double maxTier = MercenaryTemplate.getMaxAllowedTier(p.level);
        if (rec.entry.tier >= maxTier) {
            try { p.getService().send_box_ThongBao_OK("Cấp độ của bạn (Lv." + p.level + ") chỉ cho phép lính đạt tối đa Tier " + String.format("%.1f", maxTier) + "!\nHãy tăng cấp bản thân để mở khóa phẩm chất cao hơn."); } catch (Exception ignored) {}
            return;
        }

        double newTier = Math.min(maxTier, rec.entry.tier + 1.0);
        long costBeri = MercenaryTemplate.getUpgradeTierCostBeri(newTier);
        if (p.get_vang() < costBeri) {
            try { p.getService().send_box_ThongBao_OK("Bạn không đủ Beri để nâng bậc Tier! Cần: " + ZUtil.number_format(costBeri) + " Beri."); } catch (Exception ignored) {}
            return;
        }

        p.update_vang(-costBeri);
        try { p.updateMoney(); } catch (Exception ignored) {}
        try { p.flush(p, false); } catch (Exception ignored) {}
        rec.entry.tier = newTier;
        if (newTier >= 2.0) {
            rec.entry.hasHoanMy = true;
        }
        if (newTier >= 2.5) {
            rec.entry.hasKichAn = true;
        }
        if (rec.bot != null) {
            try {
                int fruitId = MercenaryTemplate.getFruitIdForEntry(rec.entry);
                int chosenThanTrangSet = Player.selectThanTrangSetIndex(rec.bot.clazz, fruitId, rec.bot.name);
                rec.bot.setupBotEquip(rec.bot.clazz, rec.bot.level, newTier, newTier >= 2.0, newTier >= 1.5, newTier >= 2.5, chosenThanTrangSet);
                rec.bot.setupBotSkills(rec.bot.clazz, (short) (newTier >= 3.5 ? 30 : (newTier >= 2.5 ? 24 : 18)), newTier, fruitId);
                rec.bot.setupBotAppearance(rec.bot.clazz, newTier);
                rec.bot.setupBalancedMercenaryStats();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            p.getService().send_box_ThongBao_OK("Nâng bậc " + rec.entry.name + " lên Tier " + String.format("%.1f", newTier) + " (" + (newTier >= 3.5 ? "THẦN THOẠI" : (newTier >= 2.5 ? "HOÀN MỸ" : "CAO CẤP")) + ") thành công!\nTrừ " + ZUtil.number_format(costBeri) + " Beri.");
        } catch (Exception ignored) {}
    }

    public void awakenMercenaryFruit(Player p, HiredRecord rec) {
        if (p == null || rec == null || rec.isExpired()) return;
        if (p.level < 80) {
            try { p.getService().send_box_ThongBao_OK("Bạn cần đạt cấp độ 80 trở lên để có thể Thức Tỉnh Trái Ác Quỷ cho lính!"); } catch (Exception ignored) {}
            return;
        }
        int costRuby = 250;
        if (p.get_ngoc() < costRuby) {
            try { p.getService().send_box_ThongBao_OK("Bạn không đủ Ruby để Thức Tỉnh Trái Ác Quỷ! Cần: " + costRuby + " Ruby."); } catch (Exception ignored) {}
            return;
        }

        p.update_ngoc(-costRuby);
        try { p.updateMoney(); } catch (Exception ignored) {}
        try { p.flush(p, false); } catch (Exception ignored) {}
        if (rec.bot != null) {
            try {
                int fruitId = MercenaryTemplate.getFruitIdForEntry(rec.entry);
                rec.bot.setupBotDevilFruit(fruitId, 5); // Cấp Quỷ 5 Thức Tỉnh
                rec.bot.setupBalancedMercenaryStats();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            p.getService().send_box_ThongBao_OK("Thức tỉnh Trái Ác Quỷ Tối Thượng (Cấp Quỷ 5) cho " + rec.entry.name + " thành công!\nTrừ " + costRuby + " Ruby.");
        } catch (Exception ignored) {}
    }

    private void refundCurrency(Player p, MercEntry entry, int currency) {
        try {
            switch (currency) {
                case MercenaryTemplate.CURRENCY_EXTOL: p.updateVnd(entry.priceExtol); p.updateMoney(); break;
                case MercenaryTemplate.CURRENCY_RUBY:  p.update_ngoc(entry.priceRuby); p.updateMoney(); break;
                default:                               p.update_vang(entry.priceBeri);  p.updateMoney(); break;
            }
        } catch (Exception e) {
            Log.error("MercenaryManager", "refundCurrency error: " + e.getMessage());
        }
    }
}
