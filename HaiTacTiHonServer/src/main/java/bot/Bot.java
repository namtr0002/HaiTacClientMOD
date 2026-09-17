package bot;

import model.Player;
import ability.Ability;
import map.Zone;
import map.zones.WorldWar;
import map.zones.ChiemDao;
import core.ZUtil;
import network.Session;
import network.NoService;
import bot.botplayer.BotDb;
import database.DbManager;
import bot.mercenary.MercenaryBot;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.joda.time.DateTime;
import template.Item_wear;
import template.ItemBag47;
import template.ItemTemplate3;
import template.ItemFashion;
import template.ItemHair;
import template.ItemBoat;
import template.Option;
import template.EffTemplate;
import skill.Skill_info;
import skill.Skill_Template;
import itemz.Item;
import template.ItemFashionP;
import template.ItemFashionP2;
import model.Quest;
import template.QuestP;
import java.sql.*;

public class Bot extends Player {

    private static final String[] CHAT = new String[]{
        "bot siêu cấp vip pro", "tuổi gì đòi ăn a", "đứng lại đó, không được chạy!", "t đấm 3 má nhận k ra nhé",
        "Mày biết bố mày là ai không", "Đánh thắng tao đi rồi hãy nói chuyện", "1 mình Tao chấp hết", "Lêu Lêu mấy con gà", "Đánh thắng tao sẽ có quà đó nha", "Kho báu Hải tặc"
    };

    private IAttack attack;
    private IMove move;
    protected boolean disableBaseChat = false;
    public boolean isBalancedStats = false;

    public Bot(int id, String name) throws Exception {
        super(null, name);
        this.IDPlayer = id;
        this.isBot = true;
        this.type_pk = -1;
        this.typePirate = -1;
        this.setService(new NoService(this));
        this.index_map = (short) id;
    }

    public enum BotCategory {
        PVP,      // Bot PvP (Lôi đài, Đấu trường, Truy Nã) -> type_pk = 1
        BOSS,     // Boss Bot (Kho Báu, Boss Phó Bản) -> type_pk = -1 (không hiện PK)
        ESCORT,   // Bot Lính Đánh Thuê / Bảo vệ -> type_pk = -1
        PLAYER    // Bot Giả Lập Người Chơi -> type_pk tùy mode/map
    }

    public enum StatTier {
        NORMAL,   // 50% - 60% stats của người chơi thật
        VIP       // 95% - 105% stats (tối đa 110%) của người chơi thật
    }

    public StatTier statTier = StatTier.NORMAL;

    public BotCategory getBotCategory() {
        if (this instanceof BotPVP || this instanceof BotTruyNa) {
            return BotCategory.PVP;
        }
        if (this instanceof BotKhoBau || this instanceof event.eboss.DoiTruongHuyenThoai) {
            return BotCategory.BOSS;
        }
        if (this instanceof bot.mercenary.MercenaryBot) {
            return BotCategory.ESCORT;
        }
        if (this instanceof bot.botplayer.BotPlayerReal) {
            bot.botplayer.BotPlayerReal realBot = (bot.botplayer.BotPlayerReal) this;
            if (realBot.botType == bot.botplayer.BotPlayerReal.BotType.PVP_QUEUE || (this.map != null && this.map.map_vp != null)) {
                return BotCategory.PVP;
            }
        }
        return BotCategory.PLAYER;
    }

    public void updateBotTypePk() {
        if (this instanceof bot.mercenary.MercenaryBot) {
            ((bot.mercenary.MercenaryBot) this).updateBotTypePk();
            return;
        }
        if (this instanceof BotDauTruongTuDo) {
            return; // Giữ cờ PK cho bot Đấu Trường Tự Do
        }
        if (this.map != null && (this.map.map_vp != null || (this.map.template != null && (this.map.template.id == 58 || this.map.template.id == 120 || this.map.template.id == 122 || this.map.template.id == 123)))) {
            return; // Giữ nguyên cờ PK cho PvP lôi đài
        }
        if (this.map != null && this.map.template != null) {
            int mapId = this.map.template.id;
            // Chiếm Đảo (Phó bản 254..258 hoặc Map Chiến Trường 261..265)
            if ((mapId >= 254 && mapId <= 258) || (mapId >= 261 && mapId <= 265)) {
                clan.Clan topClan = ChiemDao.getClanTop(mapId);
                if (this.map.map_dungeon instanceof ChiemDao) {
                    ChiemDao cd = (ChiemDao) this.map.map_dungeon;
                    if (cd.occupyingClan != null) {
                        topClan = cd.occupyingClan;
                    }
                }
                if (this instanceof BotChiemDao) {
                    BotChiemDao bcd = (BotChiemDao) this;
                    if (bcd.team == 5 || (topClan != null && bcd.clan != null && (bcd.clan.id == topClan.id || (bcd.clan.name != null && bcd.clan.name.equalsIgnoreCase(topClan.name))))) {
                        this.type_pk = 5;
                        if (bcd.clan == null && topClan != null) bcd.clan = topClan;
                    } else {
                        this.type_pk = 4;
                    }
                } else {
                    boolean isDefender = (topClan != null && this.clan != null && (this.clan.id == topClan.id || (this.clan.name != null && this.clan.name.equalsIgnoreCase(topClan.name))));
                    this.type_pk = (byte) (isDefender ? 5 : 4);
                }
                return;
            }
            // Trận Chiến Khổng Lồ (Little Garden map 81)
            if (mapId == 81 || this.map.map_little_garden != null) {
                if (this instanceof BotTranChienKhongLo) {
                    this.type_pk = (byte) ((BotTranChienKhongLo) this).team;
                } else if (this.map.map_little_garden != null) {
                    boolean isClan1 = (this.clan != null && this.clan.equals(this.map.map_little_garden.clan1));
                    this.type_pk = (byte) (isClan1 ? 4 : 5);
                } else if (this.type_pk != 4 && this.type_pk != 5) {
                    this.type_pk = 5;
                }
                return;
            }
            // Thủ Lĩnh Biển Khơi (Map 178..184)
            if (mapId >= 178 && mapId <= 184) {
                return;
            }
            // PvP Băng (map 120, 259, 260...)
            if (mapId == 120 || mapId == 259 || mapId == 260 || this.map.pvpBangMapFight != null) {
                return;
            }
            // Bảo Vệ Pháo Đài (map 267..271)
            if (mapId >= 267 && mapId <= 271 || this.map.IsMapBaoVePhaoDai() || this.map.baoVePhaoDai != null) {
                return;
            }
            // Đấu Trường Tự Do (map 70..74)
            if (mapId >= 70 && mapId <= 74) {
                return;
            }
            // World War (map 272..275)
            if (mapId >= 272 && mapId <= 275) {
                return;
            }
        }
        // Giữ type_pk hợp lệ: Cờ màu (4..10), WorldWar (11..13), Clan PVP (14..15)
        if (this.type_pk >= 4 && this.type_pk <= 15) {
            return;
        }
        // Tắt toàn bộ cờ type_pk ảo (type_pk = 1 hoặc 0) cho tất cả Bot thường ngoài các hoạt động trên
        this.type_pk = -1;
    }

    @Override
    public boolean setup() {
        try {
            return setup("players_bot");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean setup(String tableName) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DbManager.gI().getConnect();
            ps = connection.prepareStatement("SELECT * FROM `" + tableName + "` WHERE `name` = ? LIMIT 1;");
            ps.setString(1, this.name);
            rs = ps.executeQuery();
            if (rs.next()) {
                loadFromResultSet(rs);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static String getColStringSafe(ResultSet rs, String... colNames) {
        for (String col : colNames) {
            try {
                String val = rs.getString(col);
                if (val != null) return val;
            } catch (Exception ignored) {}
        }
        return null;
    }

    private void loadFromResultSet(ResultSet rs) throws Exception {
        IDPlayer = rs.getInt("id");
        index_map = (short) IDPlayer;
        clazz = rs.getByte("clazz");
        
        // 1. level
        {
            String raw = getColStringSafe(rs, "level");
            JSONObject o = parseObj(raw);
            if (o != null) {
                level = jShort(o, "lv", (short) 1);
                exp = jLong(o, "exp", 0L);
                thongthao = jShort(o, "tt", (short) 0);
            } else {
                JSONArray a = parseArr(raw);
                if (a != null && a.size() >= 3) {
                    level = Short.parseShort(a.get(0).toString());
                    exp = Long.parseLong(a.get(1).toString());
                    thongthao = Short.parseShort(a.get(2).toString());
                }
            }
        }

        // 2. body
        {
            String raw = getColStringSafe(rs, "body", "body_parts");
            JSONObject o = parseObj(raw);
            if (o != null) {
                head = jShort(o, "head", (short) -1);
                hair = jShort(o, "hair", (short) -1);
                part_body = jShort(o, "body", (short) -1);
                part_leg = jShort(o, "leg", (short) -1);
                part_ring = jShort(o, "ring", (short) -1);
                part_weapon = jShort(o, "weapon", (short) -1);
            } else {
                JSONArray a = parseArr(raw);
                if (a != null && a.size() >= 2) {
                    head = Short.parseShort(a.get(0).toString());
                    hair = Short.parseShort(a.get(1).toString());
                    part_body = a.size() > 2 ? Short.parseShort(a.get(2).toString()) : -1;
                    part_leg = a.size() > 3 ? Short.parseShort(a.get(3).toString()) : -1;
                    part_ring = a.size() > 4 ? Short.parseShort(a.get(4).toString()) : -1;
                    part_weapon = a.size() > 5 ? Short.parseShort(a.get(5).toString()) : -1;
                }
            }
        }

        // 3. potential
        {
            String raw = getColStringSafe(rs, "potential");
            JSONObject o = parseObj(raw);
            list_op_thongthao = new ArrayList<>();
            if (o != null) {
                pointAttribute = jLong(o, "attr", 0L);
                point1 = jLong(o, "p1", 0L);
                point2 = jLong(o, "p2", 0L);
                point3 = jLong(o, "p3", 0L);
                point4 = jLong(o, "p4", 0L);
                point5 = jLong(o, "p5", 0L);
                pointAttributeThongThao = jLong(o, "tt", 0L);
                JSONArray ops = (JSONArray) o.get("op_tt");
                if (ops != null) {
                    for (int i = 0; i < ops.size(); i++) {
                        Object elem = ops.get(i);
                        if (elem instanceof JSONArray) {
                            JSONArray js_in_1 = (JSONArray) elem;
                            if (js_in_1.size() >= 2) {
                                list_op_thongthao.add(new Option(Byte.parseByte(js_in_1.get(0).toString()), Integer.parseInt(js_in_1.get(1).toString())));
                            }
                        } else if (elem instanceof JSONObject) {
                            JSONObject jso = (JSONObject) elem;
                            list_op_thongthao.add(new Option((byte) jInt(jso, "id", 0), jInt(jso, "param", 0)));
                        }
                    }
                }
            } else {
                JSONArray a = parseArr(raw);
                if (a != null && a.size() >= 7) {
                    pointAttribute = Long.parseLong(a.get(0).toString());
                    point1 = Long.parseLong(a.get(1).toString());
                    point2 = Long.parseLong(a.get(2).toString());
                    point3 = Long.parseLong(a.get(3).toString());
                    point4 = Long.parseLong(a.get(4).toString());
                    point5 = Long.parseLong(a.get(5).toString());
                    pointAttributeThongThao = Long.parseLong(a.get(6).toString());
                    if (a.size() > 7) {
                        JSONArray js_in = (JSONArray) a.get(7);
                        if (js_in != null) {
                            for (int i = 0; i < js_in.size(); i++) {
                                JSONArray js_in_1 = (JSONArray) js_in.get(i);
                                list_op_thongthao.add(new Option(Byte.parseByte(js_in_1.get(0).toString()), Integer.parseInt(js_in_1.get(1).toString())));
                            }
                        }
                    }
                }
            }
        }

        this.tieuRuby = 0;
        this.wanted_point = 0;

        // 4. it_body
        this.item = new Item(this);
        this.item.it_body = new Item_wear[itemz.Item.MAX_BODY];
        {
            String raw = getColStringSafe(rs, "it_body");
            JSONArray a = parseArr(raw);
            if (a != null) {
                for (int i = 0; i < a.size(); i++) {
                    Object elem = a.get(i);
                    Item_wear w = new Item_wear();
                    Item.readUpdateItem(elem instanceof String ? (String) elem : ((org.json.simple.JSONAware) elem).toJSONString(), w);
                    if (w.template != null && ItemTemplate3.get_it_by_id(w.template.id) != null) {
                        byte correctSlot = (w.template.typeEquip >= 0 && w.template.typeEquip < this.item.it_body.length)
                                ? w.template.typeEquip : (byte) w.index;
                        if (correctSlot >= 0 && correctSlot < this.item.it_body.length) {
                            w.index = correctSlot;
                            this.item.it_body[correctSlot] = w;
                            if (correctSlot == 6) {
                                this.item.it_heart = w;
                                this.item.it_heart.typelock = 1;
                            }
                        }
                    }
                }
            }
        }

        // Tự động phân bổ tiềm năng nếu bot level > 1 và chưa từng phân bổ điểm
        if (this.point1 <= 1 && this.point2 <= 1 && this.point3 <= 1 && this.point4 <= 1 && this.point5 <= 1 && this.level > 1) {
            this.autoAllocatePotentialPoints(this.clazz);
        }

        // Fallback: Chỉ bổ sung trang bị khởi đầu nếu bot hoàn toàn không có trang bị cơ bản nào (vũ khí, áo, quần đều trống)
        if (this.item.it_body[0] == null && this.item.it_body[3] == null && this.item.it_body[5] == null) {
            int botTier = this.level >= 90 ? 4 : (this.level >= 60 ? 3 : (this.level >= 30 ? 2 : 1));
            boolean buff = botTier >= 2;
            this.setupBotEquip(this.clazz, this.level, botTier, buff, buff, buff);
        }

        // Initialize empty bags for Bot
        this.item.bag3 = new Item_wear[this.item.max_bag];
        this.item.bag47 = new ArrayList<>();


        // 5. skill
        this.skill_point = new ArrayList<>();
        {
            String raw = getColStringSafe(rs, "skill");
            JSONArray a = parseArr(raw);
            if (a != null) {
                for (int i = 0; i < a.size(); i++) {
                    Object elem = a.get(i);
                    JSONObject o = elem instanceof JSONObject ? (JSONObject) elem : null;
                    if (o != null) {
                        Skill_info sk = new Skill_info();
                        sk.exp = jLong(o, "exp", 0L);
                        sk.temp = Skill_Template.get_temp(jShort(o, "id", (short)0), sk.exp);
                        sk.lvdevil = (byte) jInt(o, "lv", 0);
                        sk.devilpercent = (byte) jInt(o, "pct", 0);
                        if (sk.temp != null) this.skill_point.add(sk);
                    } else if (elem instanceof JSONArray) {
                        JSONArray js2 = (JSONArray) elem;
                        if (js2.size() >= 2) {
                            Skill_info sk = new Skill_info();
                            sk.exp = Long.parseLong(js2.get(1).toString());
                            sk.temp = Skill_Template.get_temp(Short.parseShort(js2.get(0).toString()), sk.exp);
                            if (js2.size() > 2) sk.lvdevil = Byte.parseByte(js2.get(2).toString());
                            if (js2.size() > 3) sk.devilpercent = Byte.parseByte(js2.get(3).toString());
                            if (sk.temp != null) this.skill_point.add(sk);
                        }
                    }
                }
            }
        }
        if (this.skill_point.isEmpty()) {
            this.setupBotSkills(this.clazz, (short) 15, 1);
        }

        // 6. eff
        this.list_eff = new CopyOnWriteArrayList<>();
        {
            String raw = getColStringSafe(rs, "eff");
            JSONArray a = parseArr(raw);
            if (a != null) {
                for (int i = 0; i < a.size(); i++) {
                    Object elem = a.get(i);
                    JSONObject o = elem instanceof JSONObject ? (JSONObject) elem : null;
                    if (o != null) {
                        list_eff.add(new EffTemplate(
                                (byte) jInt(o, "id", 0),
                                jInt(o, "param", 0),
                                System.currentTimeMillis() + jLong(o, "time", 0L)
                        ));
                    } else if (elem instanceof JSONArray) {
                        JSONArray js2 = (JSONArray) elem;
                        if (js2.size() >= 3) {
                            list_eff.add(new EffTemplate(
                                    Byte.parseByte(js2.get(0).toString()),
                                    Integer.parseInt(js2.get(1).toString()),
                                    System.currentTimeMillis() + Long.parseLong(js2.get(2).toString())
                            ));
                        }
                    }
                }
            }
        }

        // 7. fashion
        this.itfashionP = new ArrayList<>();
        this.fashion = new ArrayList<>();
        this.itemboat = new ArrayList<>();
        {
            String raw = getColStringSafe(rs, "fashion");
            JSONObject fo = parseObj(raw);
            if (fo != null) {
                JSONArray fpArr = jArr(fo, "fp");
                if (fpArr != null) {
                    for (Object o : fpArr) {
                        if (o instanceof JSONObject) {
                            JSONObject eo = (JSONObject) o;
                            short fid = jShort(eo, "id", (short)0);
                            short ficon = jShort(eo, "icon", (short)0);
                            byte fcat = jByte(eo, "cat", (byte)0);
                            boolean fuse = jBool(eo, "use", false);
                            if (fcat == 103 || fcat == 108) {
                                if (ItemHair.get_item(fid, fcat) == null) continue;
                            }
                            this.itfashionP.add(new template.ItemFashionP(fid, ficon, fcat, fuse));
                        }
                    }
                }
                JSONArray f2Arr = jArr(fo, "f2");
                if (f2Arr != null) {
                    for (Object o : f2Arr) {
                        if (o instanceof JSONObject) {
                            JSONObject eo = (JSONObject) o;
                            short fid = jShort(eo, "id", (short)0);
                            if (ItemFashion.get_item(fid) == null) continue;
                            ItemFashionP2 f2 = new ItemFashionP2();
                            f2.id = fid;
                            f2.is_use = jBool(eo, "use", false);
                            f2.level = jByte(eo, "lv", (byte)0);
                            f2.expires = jLong(eo, "exp", -1L);
                            this.fashion.add(f2);
                        }
                    }
                }
                JSONArray boatArr = jArr(fo, "boat");
                if (boatArr != null) {
                    for (Object o : boatArr) {
                        if (o instanceof JSONObject) {
                            JSONObject eo = (JSONObject) o;
                            byte bid = jByte(eo, "id", (byte)0);
                            if (ItemBoat.get_item(bid) == null) continue;
                            template.ItemBoatP b = new template.ItemBoatP();
                            b.id = bid;
                            b.is_use = jBool(eo, "use", false);
                            this.itemboat.add(b);
                        }
                    }
                }
            } else {
                JSONArray a = parseArr(raw);
                if (a != null) {
                    if (a.size() > 0 && a.get(0) instanceof JSONArray) {
                        JSONArray fp = (JSONArray) a.get(0);
                        for (int i = 0; i < fp.size(); i++) {
                            JSONArray t = (JSONArray) fp.get(i);
                            short fid = Short.parseShort(t.get(1).toString());
                            short ficon = Short.parseShort(t.get(2).toString());
                            byte fcat = Byte.parseByte(t.get(0).toString());
                            boolean fuse = Byte.parseByte(t.get(3).toString()) == 1;
                            if (fcat == 103 || fcat == 108) {
                                if (ItemHair.get_item(fid, fcat) == null) continue;
                            }
                            this.itfashionP.add(new template.ItemFashionP(fid, ficon, fcat, fuse));
                        }
                        if (a.size() > 1 && a.get(1) instanceof JSONArray) {
                            JSONArray f2arr = (JSONArray) a.get(1);
                            for (int i = 0; i < f2arr.size(); i++) {
                                Object elem = f2arr.get(i);
                                if (elem instanceof JSONObject) {
                                    JSONObject o = (JSONObject) elem;
                                    short fid = jShort(o, "id", (short) 0);
                                    if (ItemFashion.get_item(fid) == null) continue;
                                    ItemFashionP2 nn = new ItemFashionP2();
                                    nn.id = fid;
                                    nn.is_use = jBool(o, "use", false);
                                    nn.level = (byte) jInt(o, "lv", 0);
                                    this.fashion.add(nn);
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < a.size(); i++) {
                            Object elem = a.get(i);
                            JSONObject o = elem instanceof JSONObject ? (JSONObject) elem : null;
                            if (o != null) {
                                short fid = jShort(o, "id", (short) 0);
                                if (ItemFashion.get_item(fid) == null) continue;
                                ItemFashionP2 nn = new ItemFashionP2();
                                nn.id = fid;
                                nn.is_use = jBool(o, "use", false);
                                nn.level = (byte) jInt(o, "lv", 0);
                                fashion.add(nn);
                            } else if (elem instanceof JSONArray) {
                                JSONArray js2 = (JSONArray) elem;
                                if (js2.size() >= 3) {
                                    try {
                                        short fid = Short.parseShort(js2.get(0).toString());
                                        if (ItemFashion.get_item(fid) == null) continue;
                                        ItemFashionP2 nn = new ItemFashionP2();
                                        nn.id = fid;
                                        nn.is_use = Byte.parseByte(js2.get(1).toString()) == 1;
                                        nn.level = Byte.parseByte(js2.get(2).toString());
                                        fashion.add(nn);
                                    } catch (Exception ignored) {}
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.itfashionP.isEmpty() && this.fashion.isEmpty()) {
            this.setDefaultFashion(this.clazz);
        }

        // 7.5. quest
        this.list_quest = new ArrayList<>();
        try {
            String raw = getColStringSafe(rs, "quest");
            if (raw != null && !raw.trim().isEmpty()) {
                JSONArray arr = parseArr(raw);
                if (arr != null) {
                    for (int i = 0; i < arr.size(); i++) {
                        JSONArray qEntry = (JSONArray) arr.get(i);
                        QuestP temp = new QuestP();
                        temp.template = Quest.get_quest(Short.parseShort(qEntry.get(0).toString()));
                        JSONArray rows = (JSONArray) qEntry.get(1);
                        temp.data = new short[rows.size()][];
                        for (int j = 0; j < rows.size(); j++) {
                            JSONArray cells = (JSONArray) rows.get(j);
                            temp.data[j] = new short[cells.size()];
                            for (int k = 0; k < cells.size(); k++) {
                                temp.data[j][k] = Short.parseShort(cells.get(k).toString());
                            }
                        }
                        this.list_quest.add(temp);
                    }
                }
            }
        } catch (Exception ignored) {}

        // Fallback: If no main quest is loaded, initialize with the starting quest (Quest ID 0 or 1)
        if (this.list_quest.isEmpty()) {
            try {
                Quest startQuest = Quest.get_quest(0);
                if (startQuest == null || startQuest.equals(Quest.QUEST_FINISH)) {
                    startQuest = Quest.get_quest(1);
                }
                if (startQuest != null && !startQuest.equals(Quest.QUEST_FINISH)) {
                    QuestP qp = new QuestP();
                    qp.template = startQuest;
                    qp.data = new short[qp.template.data_quest != null ? qp.template.data_quest.length : 0][];
                    for (int i = 0; i < qp.data.length; i++) {
                        qp.data[i] = new short[qp.template.data_quest[i].length];
                        for (int j = 0; j < qp.data[i].length; j++) {
                            qp.data[i][j] = qp.template.data_quest[i][j];
                        }
                    }
                    this.list_quest.add(qp);
                }
            } catch (Exception ignored) {}
        }

        // 8. site
        {
            String raw = getColStringSafe(rs, "site");
            JSONObject o = parseObj(raw);
            if (o != null) {
                int mapId = jInt(o, "map", 1);
                int zoneId = jInt(o, "zone", 0);
                try {
                    Zone[] maps = Zone.getMapByID(mapId);
                    int zone_goto = zoneId < maps.length ? zoneId : 0;
                    this.map = maps[zone_goto];
                } catch (Exception e) {
                    this.map = Zone.getMapByID(1)[0];
                }
                this.hp = jInt(o, "hp", -1);
                this.mp = jInt(o, "mp", -1);
                x = jShort(o, "x", (short) 300);
                y = jShort(o, "y", (short) 300);
                is_show_hat = jBool(o, "hat", true);
                pointPk = jInt(o, "pk", 0);
                is_show_weapon = jBool(o, "wpn", true);
                is_hide_fashion_hair = jBool(o, "hfhair", false);
                is_hide_fashion_head = jBool(o, "hfhead", false);
            } else {
                JSONArray a = parseArr(raw);
                if (a != null && a.size() >= 8) {
                    try {
                        Zone[] maps = Zone.getMapByID(Integer.parseInt(a.get(0).toString()));
                        byte zone_id = Byte.parseByte(a.get(1).toString());
                        int zone_goto = zone_id < maps.length ? zone_id : 0;
                        this.map = maps[zone_goto];
                    } catch (Exception e) {
                        this.map = Zone.getMapByID(1)[0];
                    }
                    this.hp = Integer.parseInt(a.get(2).toString());
                    this.mp = Integer.parseInt(a.get(3).toString());
                    x = Short.parseShort(a.get(4).toString());
                    y = Short.parseShort(a.get(5).toString());
                    is_show_hat = Byte.parseByte(a.get(6).toString()) == 1;
                    pointPk = Integer.parseInt(a.get(7).toString());
                } else {
                    this.map = Zone.getMapByID(1)[0];
                }
            }
        }

        this.ability = new Ability(this);
        this.type_pk = -1;
        this.typePirate = -1;
        setAbility();
        updateParts();
    }

    @Override
    public void updateParts() {
        super.updateParts();
    }

    @Override
    public void wear_item(Item_wear it) throws java.io.IOException {
        super.wear_item(it);
        updateParts();
    }

    public synchronized void saveBotToDb(Connection conn) throws SQLException {
        updateParts();
        boolean isUpdate = this.IDPlayer > 0;
        String sql = isUpdate 
            ? "UPDATE `players_bot` SET `clazz` = ?, `body` = ?, `level` = ?, `exp` = ?, `potential` = ?, `it_body` = ?, `skill` = ?, `eff` = ?, `fashion` = ?, `site` = ?, `quest` = ? WHERE `id` = ?"
            : "INSERT INTO `players_bot` (`clazz`, `body`, `level`, `exp`, `potential`, `it_body`, `skill`, `eff`, `fashion`, `site`, `quest`, `name`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setByte(1, this.clazz);

            
            // 2. body
            {
                JSONObject o = new JSONObject();
                o.put("head", this.head);
                o.put("hair", this.hair);
                
                short part_weapon = -1;
                if (this.item != null && this.item.it_body != null && this.item.it_body[0] != null && this.item.it_body[0].template != null) {
                    template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(this.item.it_body[0].template.id);
                    if (temp != null) {
                        part_weapon = temp.part;
                    }
                }
                this.part_weapon = part_weapon;
                o.put("weapon", part_weapon);
                
                short part_body = -1;
                if (this.item != null && this.item.it_body != null && this.item.it_body[3] != null && this.item.it_body[3].template != null) {
                    template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(this.item.it_body[3].template.id);
                    if (temp != null) {
                        part_body = temp.part;
                    }
                }
                this.part_body = part_body;
                o.put("body", part_body);
                
                short part_leg = -1;
                if (this.item != null && this.item.it_body != null && this.item.it_body[5] != null && this.item.it_body[5].template != null) {
                    template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(this.item.it_body[5].template.id);
                    if (temp != null) {
                        part_leg = temp.part;
                    }
                }
                this.part_leg = part_leg;
                o.put("leg", part_leg);
                
                short part_ring = -1;
                if (this.item != null && this.item.it_body != null && this.item.it_body[4] != null && this.item.it_body[4].template != null) {
                    template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(this.item.it_body[4].template.id);
                    if (temp != null) {
                        part_ring = temp.part;
                    }
                }
                this.part_ring = part_ring;
                o.put("ring", part_ring);
                
                ps.setString(2, o.toJSONString());
            }
            
            // 3. level
            {
                JSONObject o = new JSONObject();
                o.put("lv", this.level);
                o.put("exp", this.exp);
                o.put("tt", this.thongthao);
                ps.setString(3, o.toJSONString());
            }
            
            ps.setLong(4, this.exp);
            
            // 5. potential
            {
                JSONObject o = new JSONObject();
                o.put("attr", this.pointAttribute);
                o.put("p1", this.point1);
                o.put("p2", this.point2);
                o.put("p3", this.point3);
                o.put("p4", this.point4);
                o.put("p5", this.point5);
                o.put("tt", this.pointAttributeThongThao);
                JSONArray ops = new JSONArray();
                for (int i = 0; i < this.list_op_thongthao.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", this.list_op_thongthao.get(i).id);
                    jo.put("param", this.list_op_thongthao.get(i).getParam());
                    ops.add(jo);
                }
                o.put("op_tt", ops);
                ps.setString(5, o.toJSONString());
            }
            
            // 6. it_body
            {
                JSONArray js = new JSONArray();
                if (this.item != null && this.item.it_body != null) {
                    if (this.item.it_heart != null && this.item.it_heart.template != null && ItemTemplate3.get_it_by_id(this.item.it_heart.template.id) != null) {
                        this.item.it_heart.index = 6;
                        this.item.it_body[6] = this.item.it_heart;
                    } else if (this.item.it_body.length > 6 && this.item.it_body[6] != null && (this.item.it_body[6].template != null && (this.item.it_body[6].template.typeEquip == 6 || this.item.it_body[6].template.id == 11000))) {
                        this.item.it_heart = this.item.it_body[6];
                        this.item.it_heart.index = 6;
                    }
                    for (int i = 0; i < this.item.it_body.length; i++) {
                        Item_wear it = (i == 6 && this.item.it_heart != null) ? this.item.it_heart : this.item.it_body[i];
                        if (it != null && it.template != null && ItemTemplate3.get_it_by_id(it.template.id) != null) {
                            it.index = (short) i;
                            JSONObject js_temp = Item.it_data_to_json(it);
                            if (js_temp != null && !js_temp.isEmpty()) js.add(js_temp);
                        }
                    }
                }
                ps.setString(6, js.toJSONString());
            }
            
            // 7. skill
            {
                JSONArray js = new JSONArray();
                for (int i = 0; i < this.skill_point.size(); i++) {
                    JSONObject o = new JSONObject();
                    o.put("id", this.skill_point.get(i).temp.indexSkillInServer);
                    o.put("exp", this.skill_point.get(i).exp);
                    o.put("lv", this.skill_point.get(i).lvdevil);
                    o.put("pct", this.skill_point.get(i).devilpercent);
                    js.add(o);
                }
                ps.setString(7, js.toJSONString());
            }
            
            // 8. eff
            {
                JSONArray js = new JSONArray();
                if (this.list_eff != null) {
                    for (EffTemplate eff_temp : this.list_eff) {
                        if (eff_temp != null && EffTemplate.check_eff_can_save(eff_temp.id)) {
                            JSONObject o = new JSONObject();
                            o.put("id", eff_temp.id);
                            o.put("param", eff_temp.param);
                            o.put("time", eff_temp.time - System.currentTimeMillis());
                            js.add(o);
                        }
                    }
                }
                ps.setString(8, js.toJSONString());
            }
            
            // 9. fashion
            {
                JSONArray js = new JSONArray();
                for (int i = 0; i < this.fashion.size(); i++) {
                    ItemFashionP2 f2 = this.fashion.get(i);
                    if (f2 != null && ItemFashion.get_item(f2.id) != null) {
                        JSONObject o = new JSONObject();
                        o.put("id", f2.id);
                        o.put("use", f2.is_use);
                        o.put("lv", f2.level);
                        js.add(o);
                    }
                }
                ps.setString(9, js.toJSONString());
            }
            
            // 10. site
            {
                JSONObject o = new JSONObject();
                o.put("map", this.map != null ? this.map.template.id : 1);
                o.put("zone", this.map != null ? this.map.zone_id : 0);
                o.put("hp", this.hp);
                o.put("mp", this.mp);
                o.put("x", this.x);
                o.put("y", this.y);
                o.put("hat", this.is_show_hat);
                o.put("pk", this.pointPk);
                ps.setString(10, o.toJSONString());
            }
            
            // 10.5. quest (Make sure bot always has a quest before saving)
            if (this.list_quest == null || this.list_quest.isEmpty()) {
                this.list_quest = new ArrayList<>();
                try {
                    Quest firstQuest = Quest.get_quest(1);
                    if (firstQuest != null && !firstQuest.equals(Quest.QUEST_FINISH)) {
                        QuestP qp = new QuestP();
                        qp.template = firstQuest;
                        qp.data = new short[qp.template.data_quest.length][];
                        for (int i = 0; i < qp.data.length; i++) {
                            qp.data[i] = new short[qp.template.data_quest[i].length];
                            for (int j = 0; j < qp.data[i].length; j++) {
                                qp.data[i][j] = qp.template.data_quest[i][j];
                            }
                        }
                        this.list_quest.add(qp);
                    }
                } catch (Exception ignored) {}
            }
            {
                JSONArray a = new JSONArray();
                for (QuestP qp : this.list_quest) {
                    JSONArray entry = new JSONArray();
                    entry.add(qp.template.id);
                    JSONArray rows = new JSONArray();
                    for (short[] row : qp.data) {
                        JSONArray cells = new JSONArray();
                        for (short v : row) cells.add(v);
                        rows.add(cells);
                    }
                    entry.add(rows);
                    a.add(entry);
                }
                ps.setString(11, a.toJSONString());
            }
            
            if (isUpdate) {
                ps.setInt(12, this.IDPlayer);
            } else {
                ps.setString(12, this.name);
            }
            
            ps.executeUpdate();
            if (!isUpdate) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.IDPlayer = generatedKeys.getInt(1);
                    }
                }
            }
        }
    }

    public synchronized int saveBot(boolean print) {
        return super.flush(this, print, "players_bot");
    }

    @Override
    public synchronized int flush(Player p, boolean print) {
        return super.flush(p, print, "players_bot");
    }

    @Override
    public synchronized int flush(Player p, boolean print, String tableName) {
        return super.flush(p, print, tableName);
    }

    public void init() {
        // Subclasses should override this to configure default attack/move strategies
    }

    public void join(map.Zone zone, short x, short y) {
        this.map = zone;
        this.x = x;
        this.y = y;
        this.xold = x;
        this.yold = y;
        this.lastValidX = x;
        this.lastValidY = y;

        if (zone != null && zone.template != null) {
            if (x < 30) this.x = 30;
            if (zone.template.maxW > 0 && x > zone.template.maxW - 30) this.x = (short) (zone.template.maxW - 30);
            if (y < 30) this.y = 30;
            if (zone.template.maxH > 0 && y > zone.template.maxH - 30) this.y = (short) (zone.template.maxH - 30);
            this.xold = this.x;
            this.yold = this.y;
            this.lastValidX = this.x;
            this.lastValidY = this.y;
        }

        if (this.ability == null) {
            this.ability = new ability.Ability(this);
        }
        setAbility();
        updateParts();
        
        long hpMax = this.ability != null ? this.ability.get_hp_max(true) : 1000;
        long mpMax = this.ability != null ? this.ability.get_mp_max(true) : 1000;
        if (this.hp <= 0) this.hp = (int) Math.max(500, hpMax);
        if (this.mp <= 0) this.mp = (int) Math.max(500, mpMax);
        this.isdie = false;

        if (zone != null) {
            zone.enter_map(this);
            
            // Broadcast CMD 1 (spawn character) và gửi thông tin chi tiết (CMD -5) của bot
            try {
                network.Message m_spawn = new network.Message(1);
                m_spawn.writer().writeByte(0);
                m_spawn.writer().writeShort(this.index_map);
                m_spawn.writer().writeShort(this.x);
                m_spawn.writer().writeShort(this.y);
                
                for (int i = 0; i < zone.players.size(); i++) {
                    model.Player p0 = zone.players.get(i);
                    if (p0 == null || p0.isBot || p0.conn == null) continue;
                    try {
                        p0.addmsg(m_spawn);
                        zone.send_char_in4_inmap(p0, this.index_map);
                        if (p0.getService() != null) {
                            p0.getService().update_PK(this, false);
                            p0.getService().charWearing(this, false);
                            p0.getService().Weapon_fashion(this, false);
                        }
                    } catch (Exception ignored) {}
                }
                m_spawn.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void leave() {
        if (this.party != null) {
            try {
                this.party.remove_mem(this);
            } catch (Exception ignored) {}
            this.party = null;
        }
        if (this.map != null) {
            this.map.remove_obj(this.index_map, 0);
            this.map.leave_map(this, 0);
            this.map = null;
        }
    }

    public IAttack getAttack() {
        return attack;
    }

    public void setAttack(IAttack attack) {
        this.attack = attack;
    }

    public IMove getMove() {
        return move;
    }

    public void setMove(IMove move) {
        this.move = move;
    }

    /**
     * Kích hoạt cơ chế phản đòn (Revenge / Retaliation priority) khi Bot bị người chơi tấn công.
     */
    public void onAttackedBy(Player attacker) {
        if (attacker != null && !attacker.equals(this) && !attacker.isdie) {
            if (this.canAttackTargetPlayer(attacker)) {
                this.lastAttacker = attacker;
                this.lastAttackedTime = System.currentTimeMillis();
                if (this.attack instanceof AttackAround) {
                    ((AttackAround) this.attack).setPriorityTarget(attacker);
                }
            }
        }
    }

    public long respawnTime = 0;

    protected void handleGenericBotDeath() {
        try {
            this.lastAttacker = null;
            this.targetFight = null;
            this.pvp_target = null;
            if (this.ability == null) {
                this.ability = new ability.Ability(this);
            }
            int hpMax = this.ability != null ? this.ability.get_hp_max(true) : 1000;
            int mpMax = this.ability != null ? this.ability.get_mp_max(true) : 1000;

            int currentMapId = (this.map != null && this.map.template != null) ? this.map.template.id : (this.id_map_save > 0 ? this.id_map_save : 1);
            boolean isPvpMap = this.map != null && (this.map.map_vp != null || WorldWar.mapTranChienLon(currentMapId) || this.map.map_little_garden != null || this.map.pvpBang != null);

            // 30% hồi sinh tại chỗ (hoặc luôn hồi sinh tại chỗ nếu trong map PvP)
            if (isPvpMap || ZUtil.random(100) < 30) {
                this.isdie = false;
                this.hp = Math.max(500, hpMax);
                this.mp = Math.max(500, mpMax);
                this.time_can_mob_atk = System.currentTimeMillis() + 1500L;
                if (this.map != null) {
                    try {
                        this.map.update_hp_mp_eff(this, null, 1, this.hp);
                        this.getService().use_potion(0, this.hp);
                    } catch (Exception ignored) {}
                }
                return;
            }

            // 70% quay về làng
            int villageMapId = Zone.getVillageMapId(currentMapId);
            if (villageMapId <= 0) villageMapId = (this.id_map_save > 0) ? this.id_map_save : 1;
            this.id_map_save = villageMapId;

            this.leave();
            this.isdie = false;
            this.hp = Math.max(500, hpMax);
            this.mp = Math.max(500, mpMax);
            this.time_can_mob_atk = System.currentTimeMillis() + 1500L;

            Zone[] zones = Zone.getMapByID(villageMapId);
            if (zones != null && zones.length > 0 && zones[0] != null) {
                short[] pt = bot.SmartMovement.getRandomGroundPoint(zones[0]);
                this.join(zones[0], pt[0], pt[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        super.update();
        try {
            if (this instanceof bot.mercenary.MercenaryBot) {
                return; // MercenaryBot có vòng đời, AI di chuyển và chiến đấu riêng tại MercenaryManager & mercTick()
            }
            if (this.map != null && (this.map.template.id >= 272 && this.map.template.id <= 275) && this.timeLogBot < System.currentTimeMillis()) {
                this.timeLogBot = System.currentTimeMillis() + 5000L;
                String logText = "Bot update: " + this.name + ", type_pk=" + this.type_pk + ", isdie=" + this.isdie 
                        + ", map_vp=" + (this.map.map_vp != null) + ", status=" + WorldWar.status + ", running=" + WorldWar.runnning 
                        + ", isWaiting=" + Zone.isWaitingOrUnstartedMatch(this.map) + ", x=" + this.x + ", y=" + this.y;
                //System.out.println(logText);
                //WorldWar.logWW(logText);
            }

            // === PHÒNG CHỜ / CHƯA BẮT ĐẦU: Đứng yên, không làm gì, giữ Bot luôn SỐNG 100% ===
            if (Zone.isWaitingOrUnstartedMatch(this.map)) {
                this.isdie = false;
                if (this.hp <= 0) {
                    long hpMax = this.ability != null ? this.ability.get_hp_max(true) : 1000;
                    long mpMax = this.ability != null ? this.ability.get_mp_max(true) : 1000;
                    this.hp = (int) Math.max(500, hpMax);
                    this.mp = (int) Math.max(500, mpMax);
                }
                if (this.map == null || this.map.map_vp == null) {
                    if (this.type_pk != -1) {
                        this.type_pk = -1;
                    }
                }
                return; // Không chat, không attack, không move
            }

            if (this.map != null && (this.map.template.id >= 272 && this.map.template.id <= 275)
                    && WorldWar.runnning && WorldWar.status == WorldWar.STATUS_WAR) {
                if (this.type_pk != 11 && this.type_pk != 12 && this.type_pk != 13) {
                    WorldWar.setType(this);
                }
            }

            if (this.isdie) {
                if (this instanceof bot.botplayer.BotPlayerReal) {
                    return; // BotPlayerReal được điều khiển bởi BotBrain
                }
                if (this.respawnTime == 0) {
                    this.respawnTime = System.currentTimeMillis() + 6000L + ZUtil.random(3000);
                }
                if (System.currentTimeMillis() >= this.respawnTime) {
                    this.respawnTime = 0;
                    handleGenericBotDeath();
                }
                return;
            } else {
                this.respawnTime = 0;
            }

            // === ĐANG CHIẾN ĐẤU: Hoạt động bình thường ===
            if (!disableBaseChat && this.timeChatBot < System.currentTimeMillis()) {
                this.timeChatBot = System.currentTimeMillis() + 5500;
                if (this.map != null) {
                    this.map.send_chat_popup(0, this.index_map, CHAT[ZUtil.random(CHAT.length)]);
                }
            }
            if (this.attack != null) {
                this.attack.attack(this);
            }
            if (this.move != null) {
                this.move.move(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
