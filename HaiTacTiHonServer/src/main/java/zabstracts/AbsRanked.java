package zabstracts;

import model.Player;
import database.DbManager;
import itemz.Item;
import network.Message;
import network.Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * iRanked — Abstract base class cho mọi bảng xếp hạng.
 *
 * +-----------------------------------------------------+
 * |  CÁCH TẠO TOP MỚI (chỉ cần ~40 dòng):              |
 * |                                                     |
 * |  public class TopXxx extends iRanked {              |
 * |      private static TopXxx instance;                |
 * |      public static TopXxx gI() {                    |
 * |          if (instance == null) instance = new TopXxx(); |
 * |          return instance;                           |
 * |      }                                              |
 * |      public static final List<InfoMemList>          |
 * |          CACHE = new ArrayList<>();                  |
 * |                                                     |
 * |      private TopXxx() {                             |
 * |          super("Tên Hiển Thị", TYPE_BYTE, MAX_TOP); |
 * |      }                                              |
 * |                                                     |
 * |      @Override public String getSql() {             |
 * |          return "SELECT ... FROM players ...";      |
 * |      }                                              |
 * |                                                     |
 * |      @Override                                      |
 * |      public InfoMemList buildEntry(ResultSet rs)    |
 * |              throws Exception {                     |
 * |          InfoMemList e = new InfoMemList();         |
 * |          e.id   = rs.getInt("id");                  |
 * |          e.name = rs.getString("name");             |
 * |          e.thongthao = rs.getLong("diem");          |
 * |          e.info = "Điểm: " + e.thongthao;          |
 * |          short[] app = readAppearance(rs);          |
 * |          e.head = app[0]; e.hair = app[1];          |
 * |          e.hat  = app[2];                           |
 * |          return e;                                  |
 * |      }                                              |
 * |                                                     |
 * |      @Override public List<InfoMemList> getCache()  |
 * |          { return CACHE; }                          |
 * |  }                                                  |
 * +-----------------------------------------------------+
 *
 * Framework tự lo: sort giảm dần, gán rank, phân trang,
 * gửi packet -30, đóng DB connection.
 *
 * Registry: auto-discovery qua core.Util.getClasses("rank"),
 * không cần đăng ký thủ công.
 */
public abstract class AbsRanked {

    // ======================================================
    //  REGISTRY — auto-discovery, không đăng ký thủ công
    // ======================================================

    /** type -> top thông thường (CaoThu, PVP, Wanted, Hang...) */
    private static final Map<Integer, AbsRanked> REGISTRY       = new LinkedHashMap<>();
    /** typeBXH index -> event top (type=7 sub-types) */
    private static final Map<Integer, AbsRanked> EVENT_REGISTRY = new LinkedHashMap<>();

    static {
        try {
            initRegistry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Scan package "rank", tìm tất cả subclass concrete của iRanked,
     * tự động đăng ký vào registry. Không cần gọi tay.
     */
    public static void initRegistry() {
        REGISTRY.clear();
        EVENT_REGISTRY.clear();

        // Manual registration fallback to prevent classpath scanning issues
        try {
            AbsRanked[] insts = new AbsRanked[] {
                // Event 1
                rank.TopBatPokemon.gI(),
                rank.TopVeTuoiTho.gI(),
                // Event 2
                rank.TopDotPhao.gI(),
                rank.TopBanhChung.gI(),
                // Event 3
                rank.TopGioTo.gI(),
                rank.TopGioToBoss.gI(),
                // Event 4
                rank.TopMoRuongKinhKibi.gI(),
                rank.TopGhepKinhKibi.gI(),
                // Event 5
                rank.TopBanhKeoThieuNhi.gI(),
                rank.TopBongBongThieuNhi.gI(),
                // Event 6
                rank.TopHopKimCuongDo.gI(),
                rank.TopHopKimCuongTim.gI(),
                // Event 7
                rank.TopHalloweenHQMQ.gI(),
                rank.TopHalloweenNap.gI(),
                // Event 8
                rank.TopLongDen.gI(),
                rank.TopDenKeoQuan.gI(),
                rank.TopDungBanh.gI(),
                // Event 9
                rank.TopTrongCay.gI(),
                rank.TopGomTraiCay.gI(),
                // Event 10
                rank.TopGioHoa.gI(),
                rank.TopBoHoa.gI(),
                // Event 11
                rank.TopGoiHopQua2010.gI(),
                rank.TopHopQuaDacBiet2010.gI(),
                // Event 12
                rank.TopNoelTrangTri.gI(),
                // Event 13
                rank.TopTrieuHoiDoiTruong.gI(),
                rank.TopDuDoanTranDau.gI(),
                // Event 14
                rank.TopBoSenTrang.gI(),
                rank.TopBoSenHong.gI(),
                // Event 15
                rank.TopGoiSocola.gI(),
                rank.TopAnSocola.gI(),
                // Event 16
                rank.TopBigMom.gI(),
                // Event 17
                rank.TopHoaDangWano.gI(),
                rank.TopTieuDietKaido.gI(),
                // Event 18
                rank.TopHoaHongTrang.gI(),
                rank.TopMoBoHoaVuLan.gI(),
                // Event 19
                rank.TopNauCheDauDo.gI(),
                rank.TopCauDuyenThatTich.gI(),

                // Non-event top
                rank.TopCaoThu.gI(),
                rank.TopClan.gI(),
                rank.TopGhepHuyHieu.gI(),
                rank.TopHang.gI(),
                rank.TopPVP.gI(),
                rank.TopWanted.gI(),
                rank.TopDauTruongTuDo.gI(),
                rank.TopChiemDao.gI(),
                rank.TopWorldWar.gI(),
                rank.TopSuperBoss.gI()
            };
            for (AbsRanked inst : insts) {
                if (inst.getEventSubType() >= 0) {
                    EVENT_REGISTRY.put(inst.getEventSubType(), inst);
                } else {
                    REGISTRY.put(inst.getType(), inst);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Class<?>> classes = core.ZUtil.getClasses("rank");
        for (Class<?> clazz : classes) {
            if (!AbsRanked.class.isAssignableFrom(clazz)) continue;
            if (clazz.isInterface() || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) continue;
            try {
                java.lang.reflect.Constructor<?> ctor = clazz.getDeclaredConstructor();
                ctor.setAccessible(true);
                AbsRanked inst = (AbsRanked) ctor.newInstance();
                if (inst.getEventSubType() >= 0) {
                    EVENT_REGISTRY.put(inst.getEventSubType(), inst);
                } else {
                    REGISTRY.put(inst.getType(), inst);
                }
            } catch (NoSuchMethodException ignored) {
                // Bỏ qua class không có no-arg constructor
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Lấy top theo type packet (4=CaoThu, 9=Wanted, 10=Hang...) */
    public static AbsRanked get(int type) {
        return REGISTRY.get(type);
    }

    /** Lấy event top theo typeBXH index (1=DotPhao, 2=GhepHH...) */
    public static AbsRanked getBySubType(int subType) {
        return EVENT_REGISTRY.get(subType);
    }

    /** Tất cả top thông thường — dùng để update tất cả */
    public static Collection<AbsRanked> all() {
        return REGISTRY.values();
    }

    /** Tất cả event top — dùng để update theo điều kiện isActive() */
    public static Collection<AbsRanked> allEventSubTypes() {
        return EVENT_REGISTRY.values();
    }

    // ======================================================
    //  META — subclass set qua constructor
    // ======================================================

    /** Tiêu đề hiển thị trên client */
    protected final String title;

    /**
     * Type byte gửi trong packet -30.
     * Top thông thường: khớp với case trong Ranked.send().
     * Event top (typeBXH): tất cả ghi type=7 vào packet, dùng getEventSubType() để phân biệt.
     */
    protected final int type;

    /** Số lượng tối đa trong bảng (mặc định 50) */
    protected final int maxItems;
    protected String key;

    public int getEventId() {
        return -1;
    }

    public String getSeasonKey() {
        String baseKey = (key != null && !key.trim().isEmpty()) ? key : ("Ranked_" + (getEventSubType() >= 0 ? "Event_" + getEventSubType() : type));
        if (getEventId() > 0) {
            event.Event ev = event.EventManager.gI().getEvent(getEventId());
            if (ev != null) {
                String sKey = ev.getSeasonKey();
                if (sKey != null && !sKey.trim().isEmpty()) {
                    return historys.HistoryManager.formatKey(baseKey, sKey);
                }
            }
        }
        return baseKey;
    }

    public String getEventSeasonKey() {
        if (getEventId() > 0) {
            event.Event ev = event.EventManager.gI().getEvent(getEventId());
            if (ev != null) {
                return ev.getSeasonKey();
            }
        }
        return "";
    }

    public static String buildEventSql(int eventId) {
        event.Event ev = event.EventManager.gI().getEvent(eventId);
        String seasonKey = (ev != null) ? ev.getSeasonKey() : "";
        String typeCondition;
        if (seasonKey != null && !seasonKey.trim().isEmpty()) {
            String formattedKey = historys.HistoryManager.formatKey("EVENT_DATA", seasonKey);
            typeCondition = "h.`type` = '" + formattedKey + "'";
        } else {
            typeCondition = "h.`type` = 'EVENT_DATA'";
        }
        return "SELECT p.`id`, p.`name`, p.`body_parts`, p.`it_body`, p.`fashion`, p.`inventory`, h.`data` AS `event` " +
               "FROM `players` p " +
               "INNER JOIN `historys` h ON p.`id` = h.`player_id` AND " + typeCondition + " " +
               "GROUP BY p.`id`";
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    // ======================================================
    //  CONSTRUCTOR
    // ======================================================

    /**
     * @param title    Tên top hiển thị (VD: "Cao Thủ", "PVP")
     * @param type     Type byte gửi packet (VD: 4, 7, 9...)
     * @param maxItems Giới hạn bảng xếp hạng
     */
    protected AbsRanked(String title, int type, int maxItems) {
        this.title    = title;
        this.type     = type;
        this.maxItems = maxItems;
    }

    /** Tạo top với maxItems mặc định = 50 */
    protected AbsRanked(String title, int type) {
        this(title, type, 50);
    }

    // ======================================================
    //  ABSTRACT — subclass BẮT BUỘC override
    // ======================================================

    /**
     * SQL query để lấy dữ liệu xếp hạng từ DB.
     * Framework tự sort nếu shouldSort()=true.
     * Nếu SQL đã ORDER BY + LIMIT thì override shouldSort() trả false.
     *
     * Columns bắt buộc cho readAppearance(): fashion, body, it_body, site
     */
    public abstract String getSql();

    /**
     * Parse 1 dòng ResultSet -> InfoMemList.
     * Throw exception để framework bỏ qua dòng lỗi tự động.
     * Không cần gán rank (framework tự gán).
     */
    public abstract InfoMemList buildEntry(ResultSet rs) throws Exception;

    /**
     * Reference tới List<InfoMemList> static của subclass.
     * Mỗi subclass PHẢI có 1 field static riêng.
     *
     * VD: public static final List<InfoMemList> CACHE = new ArrayList<>();
     *     @Override public List<InfoMemList> getCache() { return CACHE; }
     */
    public abstract List<InfoMemList> getCache();

    // ======================================================
    //  OPTIONAL OVERRIDE — subclass có thể tuỳ chỉnh
    // ======================================================

    /**
     * Có sort theo thongthao giảm dần sau khi fetch không?
     * Override trả về false nếu SQL đã tự ORDER BY + LIMIT.
     */
    protected boolean shouldSort() { return false; }

    /**
     * Event sub-type index (p.typeBXH) — chỉ dùng cho event tops trong type=7.
     * Top thông thường: trả -1 (mặc định, đăng ký vào REGISTRY).
     * Event top: trả typeBXH tương ứng (đăng ký vào EVENT_REGISTRY).
     *
     * VD: TopDotPhao trả 1, TopGhepHuyHieu trả 2...
     */
    public int getEventSubType() { return -1; }

    /**
     * Top này có đang active (nên update) không?
     * Top thường: luôn true.
     * Event top: override để check sự kiện đang chạy.
     *
     * VD: return Manager.gI().event == 9;
     */
    public boolean isActive() { return true; }

    // ======================================================
    //  TEMPLATE METHOD: update() — chạy SQL, build cache
    // ======================================================

    /**
     * Chạy SQL, build cache. Gọi định kỳ (VD: mỗi 5 phút).
     * An toàn: nếu DB lỗi, cache cũ được giữ nguyên.
     * Nếu dòng nào buildEntry() throw exception, dòng đó bị bỏ qua.
     */
    public void update() {
        String sql = getSql();
        if (sql == null || sql.trim().isEmpty()) {
            return;
        }
        List<InfoMemList> temp = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps  = null;
        ResultSet rs          = null;
        try {
            con = DbManager.gI().getConnect();
            if (con == null) {
                return;
            }
            ps  = con.prepareStatement(sql);
            rs  = ps.executeQuery();
            while (rs.next()) {
                try {
                    InfoMemList entry = buildEntry(rs);
                    if (entry != null) temp.add(entry);
                } catch (Exception rowErr) {
                    // Bỏ qua dòng lỗi, không dừng toàn bộ update
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return; // Giữ cache cũ, không clear
        } finally {
            closeQuietly(rs, ps, con);
        }

        applyToCache(temp);
    }

    /**
     * Sort (nếu cần) + gán rank + đẩy vào cache.
     * Tự động loại bỏ bản ghi trùng lặp (deduplicate) theo player id, giữ bản ghi có điểm cao nhất.
     */
    protected void applyToCache(List<InfoMemList> temp) {
        if (temp == null || temp.isEmpty()) {
            getCache().clear();
            return;
        }

        // Loại trùng theo id người chơi, giữ điểm cao nhất
        Map<Integer, InfoMemList> map = new LinkedHashMap<>();
        for (InfoMemList entry : temp) {
            if (entry == null || entry.name == null || entry.name.isEmpty()) continue;
            InfoMemList existing = map.get(entry.id);
            if (existing == null || entry.thongthao > existing.thongthao) {
                map.put(entry.id, entry);
            }
        }

        List<InfoMemList> deduped = new ArrayList<>(map.values());

        if (shouldSort()) {
            deduped.sort((a, b) -> Long.compare(b.thongthao, a.thongthao));
        }
        int limit = Math.min(deduped.size(), maxItems);
        List<InfoMemList> result = new ArrayList<>(limit);
        for (int i = 0; i < limit; i++) {
            deduped.get(i).rank = (short) i;
            result.add(deduped.get(i));
        }
        List<InfoMemList> cache = getCache();
        cache.clear();
        cache.addAll(result);
    }

    // ======================================================
    //  TEMPLATE METHOD: show() — gửi packet cho player
    // ======================================================

    /**
     * Gửi bảng xếp hạng cho player với phân trang 10 bản/trang.
     * Packet format: writeByte(type), writeUTF(title),
     *                writeByte(page), writeByte(count), [entries...]
     *
     * Event top ghi type=7 vào packet (không phải getEventSubType()).
     */
    public void show(Player p, int page) throws IOException {
        if (p == null) return;
        if (page < 0) page = 0;

        // Tự động đồng bộ typeBXH của Player ngay khi show bất kỳ Event Top nào
        if (getEventSubType() >= 0) {
            p.typeBXH = getEventSubType();
        }

        List<InfoMemList> cache = getCache();
        if (cache == null || cache.isEmpty()) {
            update();
            cache = getCache();
        }
        int bound1 = 0, bound2 = 0;
        if (cache != null && !cache.isEmpty()) {
            if (cache.size() > 10) {
                if ((page + 1) * 10 > cache.size()) {
                    bound1 = 10 * page;
                    bound2 = cache.size();
                    while (bound1 >= bound2 && page > 0) {
                        bound1 -= 10;
                        page--;
                    }
                } else {
                    bound1 = 10 * page;
                    bound2 = bound1 + 10;
                }
            } else {
                bound1 = 0;
                bound2 = cache.size();
                page   = 0;
            }
        } else {
            page = 0;
        }

        // Event top (typeBXH) luôn ghi type=7 vào packet
        int packetType = (getEventSubType() >= 0) ? 7 : type;

        Message m = new Message(-30);
        m.writer().writeByte(packetType);
        m.writer().writeUTF(getTitle(p));
        m.writer().writeByte(page);
        m.writer().writeByte(bound2 - bound1);
        for (int i = bound1; i < bound2; i++) {
            InfoMemList.WriteInfoMemList(m.writer(), cache.get(i));
        }
        p.addmsg(m);
        m.cleanup();
    }

    // ======================================================
    //  HELPER: rank lookup
    // ======================================================

    /** Lấy vị trí (0-based) của player trong toàn bộ bảng, -1 nếu không có. */
    public int getRank(String playerName) {
        return getRank(-1, playerName, maxItems);
    }

    public int getRank(Player p) {
        if (p == null) return -1;
        return getRank(p.IDPlayer, p.name, maxItems);
    }

    public final int getRank(int playerId, String playerName, int topN) {
        List<InfoMemList> cache = getCache();
        int limit = Math.min(cache.size(), topN);
        for (int i = 0; i < limit; i++) {
            InfoMemList entry = cache.get(i);
            if (entry != null) {
                if (playerId > 0 && entry.id == playerId) return i;
                if (playerName != null && entry.name != null && entry.name.equalsIgnoreCase(playerName)) return i;
            }
        }
        return -1;
    }

    public final int getRank(Player p, int topN) {
        if (p == null) return -1;
        return getRank(p.IDPlayer, p.name, topN);
    }

    /**
     * Lấy vị trí (0-based) trong top N đầu, -1 nếu không có.
     * VD: getRank(name, 10) -> kiểm tra top 10.
     */
    public final int getRank(String playerName, int topN) {
        return getRank(-1, playerName, topN);
    }

    public final int getRankTier(int playerId, String playerName, int topN) {
        int rank = getRank(playerId, playerName, topN);
        if (rank < 0) return -1;
        return Math.min(rank, 3);
    }

    public final int getRankTier(Player p, int topN) {
        if (p == null) return -1;
        return getRankTier(p.IDPlayer, p.name, topN);
    }

    /**
     * Nhóm thứ hạng: 0=TOP1, 1=TOP2, 2=TOP3, 3=TOP4-N, -1=ngoài top.
     * Dùng cho tính thành tích / danh hiệu.
     */
    public final int getRankTier(String playerName, int topN) {
        return getRankTier(-1, playerName, topN);
    }

    // ======================================================
    //  STATIC HELPER: readAppearance — dùng trong buildEntry()
    // ======================================================

    /**
     * Đọc head, hair, hat từ các column JSON: fashion, body, it_body, site.
     * Tái sử dụng ở mọi subclass, không cần copy-paste.
     *
     * @return short[3] = {head, hair, hat}
     * @throws Exception nếu JSON parse lỗi (buildEntry sẽ bỏ qua dòng này)
     */
    public static short[] readFullAppearance(ResultSet rs) throws Exception {
        short head_ = -999, hair_ = -999;
        short[] fashionWearing = null;

        // -- Đọc fashion --
        String rawFashion = rs.getString("fashion");
        Object parsedFashion = JSONValue.parse(rawFashion);
        
        List<ItemFashionP> itfashionP = new ArrayList<>();
        List<ItemFashionP2> fashion = new ArrayList<>();
        
        if (parsedFashion instanceof JSONObject) {
            JSONObject fo = (JSONObject) parsedFashion;
            JSONArray fpArr = (JSONArray) fo.get("fp");
            if (fpArr != null) {
                for (Object o : fpArr) {
                    JSONObject eo = (JSONObject) o;
                    itfashionP.add(new ItemFashionP(
                        jShort(eo, "id", (short)0), jShort(eo, "icon", (short)0),
                        jByte(eo, "cat", (byte)0), jBool(eo, "use", false)));
                }
            }
            JSONArray f2Arr = (JSONArray) fo.get("f2");
            if (f2Arr != null) {
                for (Object o : f2Arr) {
                    JSONObject eo = (JSONObject) o;
                    ItemFashionP2 f2 = new ItemFashionP2();
                    f2.id = jShort(eo, "id", (short)0); f2.is_use = jBool(eo, "use", false);
                    f2.level = jByte(eo, "lv", (byte)0); f2.expires = jLong(eo, "exp", -1L);
                    fashion.add(f2);
                }
            }
        } else if (parsedFashion instanceof JSONArray) {
            JSONArray fa = (JSONArray) parsedFashion;
            if (fa.size() > 0) {
                JSONArray fp = (JSONArray) JSONValue.parse(fa.get(0).toString());
                for (int i = 0; i < fp.size(); i++) {
                    JSONArray t = (JSONArray) JSONValue.parse(fp.get(i).toString());
                    itfashionP.add(new ItemFashionP(
                        Short.parseShort(t.get(1).toString()), Short.parseShort(t.get(2).toString()),
                        Byte.parseByte(t.get(0).toString()), Byte.parseByte(t.get(3).toString()) == 1));
                }
            }
            if (fa.size() > 1) {
                JSONArray f2arr = (JSONArray) JSONValue.parse(fa.get(1).toString());
                for (int i = 0; i < f2arr.size(); i++) {
                    JSONArray t = (JSONArray) JSONValue.parse(f2arr.get(i).toString());
                    ItemFashionP2 f2 = new ItemFashionP2();
                    f2.id = Short.parseShort(t.get(0).toString()); f2.is_use = Byte.parseByte(t.get(1).toString()) == 1;
                    f2.level = Byte.parseByte(t.get(2).toString());
                    f2.expires = t.size() >= 4 ? Long.parseLong(t.get(3).toString()) : -1L;
                    fashion.add(f2);
                }
            }
        }

        // Tìm fashion set đang mặc
        for (ItemFashionP2 f2 : fashion) {
            if (f2.is_use) {
                ItemFashion temp = ItemFashion.get_item(f2.id);
                if (temp != null) {
                    fashionWearing = temp.mWearing;
                    break;
                }
            }
        }

        // Đọc site
        String rawSite = null;
        try {
            String invStr = rs.getString("inventory");
            if (invStr != null) {
                JSONObject invObj = (JSONObject) JSONValue.parse(invStr);
                if (invObj != null && invObj.containsKey("site")) {
                    Object siteObj = invObj.get("site");
                    if (siteObj instanceof String) rawSite = (String) siteObj;
                    else if (siteObj != null) rawSite = ((org.json.simple.JSONAware) siteObj).toJSONString();
                }
            }
        } catch (SQLException e) {
            try { rawSite = rs.getString("site"); } catch (SQLException ignored) {}
        }
        if (rawSite == null) rawSite = "{}";
        Object parsedSite = JSONValue.parse(rawSite);
        boolean hfHair = false;
        boolean hfHead = false;
        boolean showHat = true;
        boolean showWeapon = true;
        if (parsedSite instanceof JSONObject) {
            JSONObject so = (JSONObject) parsedSite;
            hfHair = jBool(so, "hfhair", false);
            hfHead = jBool(so, "hfhead", false);
            showHat = jBool(so, "hat", true);
            showWeapon = jBool(so, "weapon", true);
        } else if (parsedSite instanceof JSONArray) {
            JSONArray jsSite = (JSONArray) parsedSite;
            if (jsSite.size() > 6) showHat = Byte.parseByte(jsSite.get(6).toString()) == 1;
            if (jsSite.size() > 7) showWeapon = Byte.parseByte(jsSite.get(7).toString()) == 1;
        }

        // Fashion set override head/hair (index 6 = head icon, index 7 = hair icon)
        if (fashionWearing != null) {
            if (!hfHead && fashionWearing.length > 6 && fashionWearing[6] > 0) {
                head_ = fashionWearing[6];
            }
            if (!hfHair) {
                if (fashionWearing.length > 7 && fashionWearing[7] > 0) {
                    hair_ = fashionWearing[7];
                } else if (!hfHead && fashionWearing.length > 6 && fashionWearing[6] > 0) {
                    hair_ = -2;
                } else if (showHat && fashionWearing.length > 1 && fashionWearing[1] > 0 && fashionWearing.length > 7 && fashionWearing[7] == -2) {
                    hair_ = -2;
                }
            }
        }

        if (hair_ == -999) {
            for (ItemFashionP fp : itfashionP) {
                if (fp.category == 103 && fp.is_use) {
                    hair_ = fp.icon;
                    break;
                }
            }
        }
        if (head_ == -999) {
            for (ItemFashionP fp : itfashionP) {
                if (fp.category == 108 && fp.is_use) {
                    head_ = fp.icon;
                    break;
                }
            }
        }

        // -- Body & Clazz --
        byte clazz = 1;
        try {
            clazz = rs.getByte("clazz");
            if (clazz < 1 || clazz > 5) clazz = 1;
        } catch (Exception ignored) {}

        String rawBody;
        try {
            rawBody = rs.getString("body_parts");
        } catch (SQLException e) {
            rawBody = rs.getString("body");
        }
        Object parsedBody = JSONValue.parse(rawBody);
        short head = -1, hair = -1, bodyPart = -1, legPart = -1, weaponPart = -1;
        if (parsedBody instanceof JSONObject) {
            JSONObject bo = (JSONObject) parsedBody;
            head = jShort(bo, "head", (short) -1);
            hair = jShort(bo, "hair", (short) -1);
            bodyPart = jShort(bo, "body", (short) -1);
            legPart = jShort(bo, "leg", (short) -1);
            weaponPart = jShort(bo, "weapon", (short) -1);
            if (bo.containsKey("clazz")) {
                clazz = jByte(bo, "clazz", clazz);
                if (clazz < 1 || clazz > 5) clazz = 1;
            }
        } else if (parsedBody instanceof JSONArray) {
            JSONArray jsBody = (JSONArray) parsedBody;
            if (jsBody.size() > 0) head = Short.parseShort(jsBody.get(0).toString());
            if (jsBody.size() > 1) hair = Short.parseShort(jsBody.get(1).toString());
            if (jsBody.size() > 2) bodyPart = Short.parseShort(jsBody.get(2).toString());
            if (jsBody.size() > 3) legPart = Short.parseShort(jsBody.get(3).toString());
            if (jsBody.size() > 5) weaponPart = Short.parseShort(jsBody.get(5).toString());
        }

        if (head_ != -999) {
            head = head_;
        } else {
            if (head < 0 || (head >= 1 && head <= 4)) {
                head = 0;
            }
        }

        if (hair_ != -999) {
            hair = hair_;
        } else if (hair <= 0) {
            switch (clazz) {
                case 1: hair = 1; break;
                case 2: hair = 24; break;
                case 3: hair = 28; break;
                case 4: hair = 32; break;
                case 5: hair = 36; break;
                default: hair = 1; break;
            }
        }

        // -- it_body --
        Item_wear[] it = new Item_wear[itemz.Item.MAX_BODY];
        try {
            String itBodyStr = rs.getString("it_body");
            if (itBodyStr != null) {
                JSONArray jsItBody = (JSONArray) JSONValue.parse(itBodyStr);
                if (jsItBody != null) {
                    for (int i = 0; i < jsItBody.size(); i++) {
                        Object itemElem = jsItBody.get(i);
                        if (itemElem == null) continue;
                        String itemStr = (itemElem instanceof String) ? (String) itemElem : itemElem.toString();
                        Item_wear w = new Item_wear();
                        Item.readUpdateItem(itemStr, w);
                        if (w.template != null) {
                            int slot = (w.template.typeEquip >= 0 && w.template.typeEquip < it.length) ? w.template.typeEquip : w.index;
                            if (slot >= 0 && slot < it.length) {
                                it[slot] = w;
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        // -- Hat --
        short hat = -1;
        if (showHat) {
            if (fashionWearing != null && fashionWearing.length > 1 && fashionWearing[1] >= 0) {
                hat = fashionWearing[1];
            } else if (it[1] != null) {
                ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(it[1].template.id);
                if (t3 != null) hat = t3.part;
            }
        }

        // -- Body --
        short body = -1;
        if (fashionWearing != null && fashionWearing.length > 3 && fashionWearing[3] >= 0) {
            body = fashionWearing[3];
        } else if (it[3] != null) {
            ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(it[3].template.id);
            if (t3 != null) body = t3.part;
        }
        if (body < 0) {
            if (bodyPart >= 0) {
                body = bodyPart;
            } else {
                switch (clazz) {
                    case 1: body = 3; break;
                    case 2: body = 26; break;
                    case 3: body = 30; break;
                    case 4: body = 34; break;
                    case 5: body = 38; break;
                    default: body = 3; break;
                }
            }
        }

        // -- Leg --
        short leg = -1;
        if (fashionWearing != null && fashionWearing.length > 5 && fashionWearing[5] >= 0) {
            leg = fashionWearing[5];
        } else if (it[5] != null) {
            ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(it[5].template.id);
            if (t3 != null) leg = t3.part;
        }
        if (leg < 0) {
            if (legPart >= 0) {
                leg = legPart;
            } else {
                switch (clazz) {
                    case 1: leg = 4; break;
                    case 2: leg = 27; break;
                    case 3: leg = 31; break;
                    case 4: leg = 35; break;
                    case 5: leg = 39; break;
                    default: leg = 4; break;
                }
            }
        }

        // -- Weapon --
        short weapon = -1;
        if (showWeapon) {
            if (fashionWearing != null && fashionWearing.length > 0 && fashionWearing[0] >= 0) {
                weapon = fashionWearing[0];
            } else if (it[0] != null) {
                ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(it[0].template.id);
                if (t3 != null) weapon = t3.part;
            }
            if (weapon < 0) {
                if (weaponPart >= 0) {
                    weapon = weaponPart;
                } else {
                    switch (clazz) {
                        case 1: weapon = -1; break;
                        case 2: weapon = 5; break;
                        case 3: weapon = 180; break;
                        case 4: weapon = 6; break;
                        case 5: weapon = 7; break;
                        default: weapon = -1; break;
                    }
                }
            }
        }

        return new short[]{head, hair, hat, body, leg, weapon};
    }

    public static short[] readAppearance(ResultSet rs) throws Exception {
        short[] full = readFullAppearance(rs);
        return new short[]{full[0], full[1], full[2]};
    }

    private static long jLong(JSONObject o, String key, long def) {
        try { Object v = o.get(key); return v == null ? def : Long.parseLong(v.toString()); } catch (Exception e) { return def; }
    }
    private static short jShort(JSONObject o, String key, short def) {
        try { Object v = o.get(key); return v == null ? def : Short.parseShort(v.toString()); } catch (Exception e) { return def; }
    }
    private static byte jByte(JSONObject o, String key, byte def) {
        try { Object v = o.get(key); return v == null ? def : Byte.parseByte(v.toString()); } catch (Exception e) { return def; }
    }
    public static boolean jBool(JSONObject o, String key, boolean def) {
        try {
            Object v = o.get(key);
            if (v == null) return def;
            if (v instanceof Boolean) return (Boolean) v;
            if (v instanceof Number) return ((Number) v).intValue() == 1;
            String str = v.toString().trim();
            if ("1".equals(str) || "true".equalsIgnoreCase(str)) return true;
            if ("0".equals(str) || "false".equalsIgnoreCase(str)) return false;
            return def;
        } catch (Exception e) {
            return def;
        }
    }

    protected static long getEventScore(String eventJson, int eventId, int dataIndex) {
        return getEventScore(eventJson, eventId, null, dataIndex, null);
    }

    protected static long getEventScore(String eventJson, int eventId, String pointKey, int dataIndex) {
        event.Event ev = event.EventManager.gI().getEvent(eventId);
        String expectedSeasonKey = (ev != null) ? ev.getSeasonKey() : null;
        return getEventScore(eventJson, eventId, pointKey, dataIndex, expectedSeasonKey);
    }

    protected static long getEventScore(String eventJson, int eventId, int dataIndex, String seasonKey) {
        return getEventScore(eventJson, eventId, null, dataIndex, seasonKey);
    }

    protected static long getEventScore(String eventJson, int eventId, String pointKey, int dataIndex, String seasonKey) {
        if (eventJson == null || eventJson.isEmpty() || eventJson.equals("[]")) return 0;
        try {
            Object parsed = JSONValue.parse(eventJson);
            if (parsed == null) return 0;

            JSONArray js12 = null;
            if (parsed instanceof JSONArray) {
                js12 = (JSONArray) parsed;
            } else if (parsed instanceof JSONObject) {
                js12 = new JSONArray();
                js12.add(parsed);
            }
            if (js12 == null || js12.isEmpty()) return 0;

            for (int i = 0; i < js12.size(); i++) {
                Object elem = js12.get(i);
                if (elem instanceof JSONObject) {
                    JSONObject eo = (JSONObject) elem;
                    if (eo.get("ev") != null && Integer.parseInt(eo.get("ev").toString()) == eventId) {
                        // Kiểm tra season key nếu cả 2 bên đều có cấu hình
                        if (seasonKey != null && !seasonKey.trim().isEmpty() && eo.get("k") != null) {
                            String itemKey = eo.get("k").toString().trim();
                            if (!itemKey.isEmpty() && !seasonKey.equals(itemKey)) {
                                continue;
                            }
                        }

                        long scoreFromMap = 0;
                        // 1. Đọc từ points JSONObject ("pt")
                        if (eo.containsKey("pt")) {
                            Object ptObj = eo.get("pt");
                            if (ptObj instanceof JSONObject) {
                                JSONObject pt = (JSONObject) ptObj;
                                if (pointKey != null && pt.containsKey(pointKey)) {
                                    try { scoreFromMap = Long.parseLong(pt.get(pointKey).toString()); } catch (Exception ignored) {}
                                }
                                if (scoreFromMap <= 0 && dataIndex >= 0) {
                                    String autoKey = (dataIndex == 20) ? event.Event.getEventPointKey1(eventId)
                                                   : (dataIndex == 21) ? event.Event.getEventPointKey2(eventId)
                                                   : null;
                                    if (autoKey != null && pt.containsKey(autoKey)) {
                                        try { scoreFromMap = Long.parseLong(pt.get(autoKey).toString()); } catch (Exception ignored) {}
                                    }
                                    if (scoreFromMap <= 0) {
                                        String[] fallbackKeys = new String[]{
                                            "point_event" + (dataIndex == 20 ? 1 : (dataIndex == 21 ? 2 : dataIndex)),
                                            "top_goi_hop_qua", "top_hop_qua_db",
                                            "point_" + dataIndex, "score_" + dataIndex, "p" + dataIndex
                                        };
                                        for (String fk : fallbackKeys) {
                                            if (pt.containsKey(fk)) {
                                                try {
                                                    long val = Long.parseLong(pt.get(fk).toString());
                                                    if (val > scoreFromMap) scoreFromMap = val;
                                                } catch (Exception ignored) {}
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 2. Đọc từ data JSONArray ("d")
                        long scoreFromArray = 0;
                        JSONArray da = (JSONArray) eo.get("d");
                        if (da != null && dataIndex >= 0 && da.size() > dataIndex) {
                            try {
                                scoreFromArray = Long.parseLong(da.get(dataIndex).toString());
                            } catch (Exception ignored) {}
                        }

                        return Math.max(scoreFromMap, scoreFromArray);
                    }
                } else if (elem instanceof JSONArray) {
                    JSONArray js_in = (JSONArray) elem;
                    if (!js_in.isEmpty() && Integer.parseInt(js_in.get(0).toString()) == eventId) {
                        if (js_in.size() > 1 && js_in.get(1) instanceof JSONArray) {
                            JSONArray js_in2 = (JSONArray) js_in.get(1);
                            if (dataIndex >= 0 && js_in2.size() > dataIndex) {
                                return Long.parseLong(js_in2.get(dataIndex).toString());
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return 0;
    }

    // ======================================================
    //  STATIC HELPER: DB cleanup
    // ======================================================

    /** Đóng DB resources, không ném exception. */
    protected static void closeQuietly(ResultSet rs, PreparedStatement ps, Connection con) {
        try { if (rs  != null) rs.close();  } catch (SQLException ignored) {}
        try { if (ps  != null) ps.close();  } catch (SQLException ignored) {}
        try { if (con != null) con.close(); } catch (SQLException ignored) {}
    }

    // ======================================================
    //  REWARD FRAMEWORK
    // ======================================================

    /**
     * Danh sách phần thưởng cho vị trí rank (0-based) trong bảng xếp hạng này.
     *
     * Subclass override để khai báo phần thưởng ngay trong class:
     *   - rank=0 -> Top 1, rank=1 -> Top 2, ...
     *   - Trả empty list -> không có quà cho vị trí đó (hoặc top không trao quà).
     *
     * Mặc định: trả empty list (không top nào tự trao quà nếu không override).
     *
     * Ví dụ override:
     * <pre>
     *   {@literal @}Override
     *   public List<GiftBox> getRewards(int rank) {
     *       List<GiftBox> gifts = new ArrayList<>();
     *       switch (rank) {
     *           case 0: gifts.add(createGift(0, 4, 500_000_000)); break;
     *           case 1: gifts.add(createGift(0, 4, 200_000_000)); break;
     *       }
     *       return gifts;
     *   }
     * </pre>
     *
     * @param rank Vị trí 0-based trong bảng xếp hạng (0 = Top 1)
     * @return Danh sách phần thưởng, hoặc empty nếu không có
     */
    public List<GiftBox> getRewards(int rank) {
        return Collections.emptyList();
    }

    /**
     * Lấy danh sách phần thưởng cho vị trí rank (0-based) của Player cụ thể.
     * Mặc định: gọi getRewards(rank).
     */
    public List<GiftBox> getRewards(Player p, int rank) {
        return getRewards(rank);
    }

    /**
     * Gửi phần thưởng BXH cho player theo vị trí rank.
     * Gọi getRewards(rank) để lấy danh sách quà, nếu rỗng thì không làm gì.
     * Tự động log "TOP_REWARD" vào historys.
     *
     * @param p    Player nhận quà (phải != null)
     * @param rank Vị trí 0-based (0 = Top 1)
     */
    public void sendReward(Player p, int rank) throws IOException {
        if (p == null) return;
        List<GiftBox> gifts = getRewards(rank);
        if (gifts == null || gifts.isEmpty()) return;
        String label = getTitle(p) + " Top " + (rank + 1);
        core.RewardService.sendGiftOrMail(p, 1, "Quà " + getTitle(p), label, gifts, true);
        historys.zLog.gI().add_log(p, "TOP_REWARD",
                String.format("top:%s | rank:%d", getTitle(p), rank + 1));
    }

    public void sendRewardToAllTop() {
        List<InfoMemList> cache = getCache();
        if (cache == null || cache.isEmpty()) return;
        int max = Math.min(cache.size(), getMaxItems());
        for (int rank = 0; rank < max; rank++) {
            InfoMemList entry = cache.get(rank);
            if (entry == null || entry.name == null || entry.name.isEmpty()) continue;
            List<GiftBox> gifts = getRewards(rank);
            if (gifts == null || gifts.isEmpty()) continue;
            int[] ids = historys.DungeonRewardHistory.getPlayerAndAccountId(entry.name);
            if (ids[0] > 0) {
                core.MailService.sendMailOffline(ids[0], ids[1], entry.name, "Hệ Thống",
                    "Quà " + getTitle() + " (Hạng " + (rank + 1) + ")",
                    "Chúc mừng bạn đã xuất sắc đạt Hạng " + (rank + 1) + " " + getTitle() + "! Phần thưởng đính kèm bên dưới.",
                    core.MailService.MAIL_TYPE_GIFT, false, gifts, 0L);
            }
        }
    }

    /**
     * Tạo nhanh 1 GiftBox từ item id.
     * type=3 -> ItemTemplate3 (Trang bị).
     * type=4 -> ItemTemplate4 (Beri, ruby, vật phẩm).
     * type=7 -> ItemTemplate7 (Vật phẩm đặc biệt, đá, bột).
     * type=8 -> ItemTemplate8 (Vật phẩm bang hội).
     * type=105 -> ItemFashion (Thời trang).
     *
     * @param itemId ID item
     * @param type   Loại item (3, 4, 7, 8, 105, ...)
     * @param num    Số lượng
     * @return GiftBox đã set đầy đủ, hoặc null nếu template không tồn tại
     */
    protected static GiftBox createGift(int itemId, int type, int num) {
        GiftBox gb = new GiftBox();
        gb.id   = (short) itemId;
        gb.type = (byte) type;
        gb.num  = num;
        gb.color = 0;

        if (type == 7) {
            ItemTemplate7 t7 = ItemTemplate7.get_it_by_id(itemId);
            if (t7 == null) return null;
            gb.name = t7.name;
            gb.icon = t7.icon;
        } else if (type == 3) {
            ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(itemId);
            if (t3 == null) return null;
            gb.name = t3.name;
            gb.icon = t3.icon;
            gb.color = t3.color;
        } else if (type == 8) {
            ItemTemplate8 t8 = ItemTemplate8.get_it_by_id(itemId);
            if (t8 == null) return null;
            gb.name = t8.name;
            gb.icon = t8.icon;
        } else if (type == 105) {
            ItemFashion itf = ItemFashion.get_item(itemId);
            if (itf != null) {
                gb.name = itf.name;
                gb.icon = itf.idIcon;
            } else {
                ItemTemplate4 t4 = ItemTemplate4.get_it_by_id(itemId);
                if (t4 == null) return null;
                gb.name = t4.name;
                gb.icon = t4.icon;
            }
        } else {
            ItemTemplate4 t4 = ItemTemplate4.get_it_by_id(itemId);
            if (t4 == null) return null;
            gb.name = t4.name;
            gb.icon = t4.icon;
        }
        return gb;
    }

    /**
     * Helper: tạo danh sách phần thưởng Beri + Ruby theo vị trí rank chuẩn.
     * Dùng cho các top thông thường (CaoThu, PVP, Wanted, Hang).
     *
     * rank=0: 5,000,000 Beri + 500 Ruby
     * rank=1: 3,000,000 Beri + 300 Ruby
     * rank=2: 1,500,000 Beri + 150 Ruby
     * rank=3-9: 500,000 Beri + 50 Ruby
     * Ngoài top 10: trả empty list.
     *
     * @param rank Vị trí 0-based
     * @return List phần thưởng, hoặc empty nếu ngoài top 10
     */
    protected static List<GiftBox> standardBeriRubyRewards(int rank) {
        if (rank < 0 || rank >= 10) return Collections.emptyList();
        int beri, ruby;
        switch (rank) {
            case 0: beri = 5_000_000; ruby = 500; break;
            case 1: beri = 3_000_000; ruby = 300; break;
            case 2: beri = 1_500_000; ruby = 150; break;
            default: beri = 500_000; ruby =  50; break;
        }
        List<GiftBox> gifts = new ArrayList<>();
        GiftBox gbBeri = createGift(0, 4, beri);
        GiftBox gbRuby = createGift(1, 4, ruby);
        if (gbBeri != null) gifts.add(gbBeri);
        if (gbRuby != null) gifts.add(gbRuby);
        return gifts;
    }

    // ======================================================
    //  GETTERS
    // ======================================================

    public String getTitle(Player p) {
        return getTitle();
    }

    public String getTitle() {
        return title;
    }
    public int    getType()     { return type; }
    public int    getMaxItems() { return maxItems; }
}
