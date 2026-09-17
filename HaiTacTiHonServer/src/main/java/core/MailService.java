package core;

import database.DbManager;
import model.Player;
import network.Message;
import network.Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.GiftBox;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MailService — Quản lý toàn bộ Hộp Thư & Quà Đính Kèm (Top, Phó bản, Sự kiện, Hệ thống).
 * Lưu trữ bền vững qua bảng `historys` (type = 'MAIL_BOX'), theo dõi trạng thái đã đọc, đã nhận quà.
 */
public class MailService {

    public static final String TYPE_DB_MAIL = "MAIL_BOX";

    public static final byte MAIL_TYPE_PRIVATE = 0;  // Trò chuyện riêng
    public static final byte MAIL_TYPE_SYSTEM  = 1;  // Thư hệ thống / thông báo
    public static final byte MAIL_TYPE_GIFT    = 2;  // Thư đính kèm quà (Top, Phó bản, Sự kiện)
    public static final byte MAIL_TYPE_CLAN    = 3;  // Thư bang hội
    public static final byte MAIL_TYPE_PARTY   = 4;  // Thư nhóm

    public static class MailEntry {
        public int id;
        public int accountId;
        public int playerId;
        public String playerName = "";
        public String sender = "Hệ Thống";
        public String title = "Thư";
        public String content = "";
        public byte typeMail = MAIL_TYPE_GIFT;
        public boolean canReply = false;
        public boolean isRead = false;
        public boolean isClaimed = false;
        public long createdAt = System.currentTimeMillis();
        public long expireAt = System.currentTimeMillis() + 14L * 24 * 3600 * 1000; // 14 ngày mặc định
        public List<GiftBox> gifts = new ArrayList<>();

        public JSONObject toJson() {
            JSONObject js = new JSONObject();
            js.put("account_id", accountId);
            js.put("player_id", playerId);
            js.put("player_name", playerName);
            js.put("sender", sender);
            js.put("title", title);
            js.put("content", content);
            js.put("type_mail", (int) typeMail);
            js.put("can_reply", canReply);
            js.put("is_read", isRead);
            js.put("is_claimed", isClaimed);
            js.put("created_at", createdAt);
            js.put("expire_at", expireAt);

            JSONArray giftArr = new JSONArray();
            if (gifts != null) {
                for (GiftBox gb : gifts) {
                    if (gb == null) continue;
                    JSONObject gJs = new JSONObject();
                    gJs.put("id", gb.id);
                    gJs.put("type", (int) gb.type);
                    gJs.put("name", gb.name != null ? gb.name : "");
                    gJs.put("icon", (int) gb.icon);
                    gJs.put("num", gb.num);
                    gJs.put("color", (int) gb.color);
                    if (gb.options != null && !gb.options.isEmpty()) {
                        JSONArray optArr = new JSONArray();
                        for (template.Option opt : gb.options) {
                            if (opt == null) continue;
                            JSONObject oJs = new JSONObject();
                            oJs.put("id", (int) opt.id);
                            oJs.put("param", opt.param);
                            optArr.add(oJs);
                        }
                        gJs.put("options", optArr);
                    }
                    giftArr.add(gJs);
                }
            }
            js.put("gifts", giftArr);
            return js;
        }

        public static MailEntry fromJson(int rowId, String jsonStr) {
            if (jsonStr == null || jsonStr.isEmpty()) return null;
            try {
                Object obj = JSONValue.parse(jsonStr);
                if (!(obj instanceof JSONObject)) return null;
                JSONObject js = (JSONObject) obj;

                MailEntry entry = new MailEntry();
                entry.id = rowId;
                entry.accountId = js.containsKey("account_id") ? ((Number) js.get("account_id")).intValue() : 0;
                entry.playerId = js.containsKey("player_id") ? ((Number) js.get("player_id")).intValue() : 0;
                entry.playerName = (String) js.getOrDefault("player_name", "");
                entry.sender = (String) js.getOrDefault("sender", "Hệ Thống");
                entry.title = (String) js.getOrDefault("title", "Thư");
                entry.content = (String) js.getOrDefault("content", "");
                entry.typeMail = js.containsKey("type_mail") ? ((Number) js.get("type_mail")).byteValue() : MAIL_TYPE_GIFT;
                entry.canReply = Boolean.TRUE.equals(js.get("can_reply"));
                entry.isRead = Boolean.TRUE.equals(js.get("is_read"));
                entry.isClaimed = Boolean.TRUE.equals(js.get("is_claimed"));
                entry.createdAt = js.containsKey("created_at") ? ((Number) js.get("created_at")).longValue() : System.currentTimeMillis();
                entry.expireAt = js.containsKey("expire_at") ? ((Number) js.get("expire_at")).longValue() : 0L;

                if (js.containsKey("gifts") && js.get("gifts") instanceof JSONArray) {
                    JSONArray giftArr = (JSONArray) js.get("gifts");
                    for (Object gObj : giftArr) {
                        if (gObj instanceof JSONObject) {
                            JSONObject gJs = (JSONObject) gObj;
                            GiftBox gb = new GiftBox();
                            gb.id = gJs.containsKey("id") ? ((Number) gJs.get("id")).shortValue() : 0;
                            gb.type = gJs.containsKey("type") ? ((Number) gJs.get("type")).byteValue() : 4;
                            gb.name = (String) gJs.getOrDefault("name", "");
                            gb.icon = gJs.containsKey("icon") ? ((Number) gJs.get("icon")).shortValue() : 0;
                            gb.num = gJs.containsKey("num") ? ((Number) gJs.get("num")).intValue() : 1;
                            gb.color = gJs.containsKey("color") ? ((Number) gJs.get("color")).byteValue() : 0;
                            if (gJs.containsKey("options") && gJs.get("options") instanceof JSONArray) {
                                JSONArray optArr = (JSONArray) gJs.get("options");
                                for (Object oObj : optArr) {
                                    if (oObj instanceof JSONObject) {
                                        JSONObject oJs = (JSONObject) oObj;
                                        short oId = oJs.containsKey("id") ? ((Number) oJs.get("id")).shortValue() : 0;
                                        int oParam = oJs.containsKey("param") ? ((Number) oJs.get("param")).intValue() : 0;
                                        gb.options.add(new template.Option(oId, oParam));
                                    }
                                }
                            }
                            entry.gifts.add(gb);
                        }
                    }
                }
                return entry;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String cleanClientText(String text) {
        if (text == null) return "";
        return text
            .replace("\u2022", "- ")  // • -> - 
            .replace("\u2013", "-")   // – -> -
            .replace("\u2014", "--")  // — -> --
            .replace("\u2018", "'")   // ‘ -> '
            .replace("\u2019", "'")   // ’ -> '
            .replace("\u201C", "\"")  // “ -> "
            .replace("\u201D", "\"")  // ” -> "
            .replace("\u2026", "...") // … -> ...
            .replace("\u2605", "*")   //  -> *
            .replace("\u2606", "*")   //  -> *
            .replaceAll("[\uD800-\uDBFF][\uDC00-\uDFFF]", "") // remove surrogate pairs (emojis)
            .replaceAll("[\\p{So}\\p{Cn}]", "") // remove other symbols
            .trim();
    }

    /**
     * Gửi thư cho Player (kèm quà nếu có)
     */
    public static MailEntry sendMail(Player p, String sender, String title, String content,
                                      byte typeMail, boolean canReply, List<GiftBox> gifts, long durationMs) {
        if (p == null) return null;
        int accountId = p.conn != null ? p.conn.idUser : 0;
        return sendMailOffline(p.IDPlayer, accountId, p.name, sender, title, content, typeMail, canReply, gifts, durationMs);
    }

    public static void populateGiftMetadata(GiftBox gb) {
        if (gb == null) return;
        if (gb.name != null) gb.name = cleanClientText(gb.name);
        if (gb.type == 4) {
            if (gb.id == 0) {
                if (gb.name == null || gb.name.isEmpty()) gb.name = "Beri";
                gb.icon = 0;
            } else if (gb.id == 1) {
                if (gb.name == null || gb.name.isEmpty()) gb.name = "Ruby";
                gb.icon = 1;
            } else if (gb.id == 2 || gb.id == 908) {
                if (gb.name == null || gb.name.isEmpty()) gb.name = "Extol";
                gb.icon = (gb.id == 908) ? (short) 908 : (short) 2;
            } else if (gb.id == 6) {
                if (gb.name == null || gb.name.isEmpty()) gb.name = "Vé";
                gb.icon = 6;
            } else if (gb.id == 333) {
                if (gb.name == null || gb.name.isEmpty()) gb.name = "XP Skill";
                gb.icon = 286;
            } else {
                ItemTemplate4 t4 = ItemTemplate4.get_it_by_id(gb.id);
                if (t4 != null) {
                    if (gb.name == null || gb.name.isEmpty()) gb.name = cleanClientText(t4.name);
                    if (gb.icon == 0) gb.icon = t4.icon;
                }
            }
        } else if (gb.type == 7) {
            ItemTemplate7 t7 = ItemTemplate7.get_it_by_id(gb.id);
            if (t7 != null) {
                if (gb.name == null || gb.name.isEmpty()) gb.name = cleanClientText(t7.name);
                if (gb.icon == 0) gb.icon = (short) t7.icon;
            }
        } else if (gb.type == 3) {
            ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(gb.id);
            if (t3 != null) {
                if (gb.name == null || gb.name.isEmpty()) gb.name = cleanClientText(t3.name);
                if (gb.icon == 0) gb.icon = t3.icon;
                if (gb.color == 0) gb.color = t3.color;
            }
        } else if (gb.type == 99) {
            if (gb.name == null || gb.name.isEmpty()) gb.name = "Kinh Nghiệm";
            if (gb.icon == 0) gb.icon = 60;
        } else if (gb.type == 105) {
            template.ItemFashion itf = template.ItemFashion.get_item(gb.id);
            if (itf != null) {
                if (gb.name == null || gb.name.isEmpty()) gb.name = cleanClientText(itf.name);
                if (gb.icon == 0) gb.icon = (short) itf.idIcon;
            }
        } else if (gb.type == 110) {
            ItemTemplate4 t4 = ItemTemplate4.get_it_by_id(gb.id);
            if (t4 != null) {
                if (gb.name == null || gb.name.isEmpty()) gb.name = cleanClientText(t4.name);
                if (gb.icon == 0) gb.icon = t4.icon;
            }
        }
        if (gb.name == null || gb.name.isEmpty()) {
            gb.name = "Vật phẩm";
        }
    }

    public static final long DEFAULT_EXPIRE_AFTER_CLAIM_OR_EMPTY = 7L * 24 * 3600 * 1000L; // 7 ngày tự xóa

    public static boolean isTestMail(String title, String content) {
        String t = (title != null ? title.toLowerCase().trim() : "");
        String c = (content != null ? content.toLowerCase().trim() : "");
        return t.startsWith("quà test") || t.startsWith("qua test")
            || t.contains("quà test") || t.contains("qua test")
            || c.contains("gói hỗ trợ test") || c.contains("goi ho tro test");
    }

    public static boolean isTestMail(MailEntry mail) {
        if (mail == null) return false;
        return isTestMail(mail.title, mail.content);
    }

    public static boolean isCurrentTestTemplateTitle(String title) {
        if (title == null) return false;
        String t = title.trim();
        for (MailEntry tpl : createAllTestMailTemplates(0, 0, "")) {
            if (tpl.title.equalsIgnoreCase(t)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isObsoleteTestMail(MailEntry mail) {
        if (mail == null) return false;
        if (!isTestMail(mail)) return false;
        return !isCurrentTestTemplateTitle(mail.title);
    }

    public static boolean isMailActive(MailEntry entry, long now) {
        if (entry == null) return false;
        // Thư quà test đã nhận: Tự động xóa ngay, không coi là active
        if (entry.isClaimed && isTestMail(entry)) {
            return false;
        }
        // Thư quà test phiên bản cũ không còn dùng: Tự động xóa
        if (isObsoleteTestMail(entry)) {
            return false;
        }
        // Thư chưa nhận quà: KHÔNG BAO GIỜ BỊ XÓA HOẶC HẾT HẠN
        if (!entry.isClaimed && entry.gifts != null && !entry.gifts.isEmpty()) {
            return true;
        }
        // Thư không có quà hoặc đã nhận quà: kiểm tra hạn 7 ngày
        if (entry.expireAt == 0) {
            return true;
        }
        return entry.expireAt > now;
    }

    public static boolean isChiemDaoMail(String title, String content) {
        String t = (title != null ? title.toLowerCase() : "");
        String c = (content != null ? content.toLowerCase() : "");
        return t.contains("chiếm đảo") || t.contains("chiem dao") || t.contains("chiem_dao")
            || c.contains("chiếm đảo") || c.contains("chiem dao") || c.contains("chiếm được đảo");
    }

    public static boolean isChiemDaoMail(MailEntry mail) {
        if (mail == null) return false;
        return isChiemDaoMail(mail.title, mail.content);
    }

    public static boolean isBagOverflowMail(String title, String content) {
        String t = (title != null ? title.toLowerCase().trim() : "");
        String c = (content != null ? content.toLowerCase().trim() : "");
        String full = t + " " + c;
        return full.contains("hành trang") || full.contains("hanh trang")
                || full.contains("chỗ trống") || full.contains("cho trong")
                || full.contains("đầy rương") || full.contains("day ruong")
                || full.contains("quà đính kèm") || full.contains("qua dinh kem");
    }

    public static boolean isBagOverflowMail(MailEntry mail) {
        if (mail == null) return false;
        return isBagOverflowMail(mail.title, mail.content);
    }

    /**
     * Kiểm tra danh sách quà có phải là tiền tệ, EXP hoặc vật phẩm xếp chồng (Category 4 & 7 không có options riêng biệt).
     */
    public static boolean isPureResourceMail(List<GiftBox> gifts) {
        if (gifts == null || gifts.isEmpty()) return false;
        for (GiftBox gb : gifts) {
            if (gb == null || gb.num <= 0) continue;
            // 1. EXP nhân vật (type 99)
            if (gb.type == 99) continue;
            // 2. Tiền tệ, điểm tích lũy và vật phẩm xếp chồng category 4 (potions, chests, đá, vé, nguyên liệu)
            if (gb.type == 4) {
                if (gb.options == null || gb.options.isEmpty()) {
                    continue;
                }
                return false;
            }
            // 3. Vật phẩm xếp chồng category 7 (nguyên liệu, đá khảm)
            if (gb.type == 7) {
                if (gb.options == null || gb.options.isEmpty()) {
                    continue;
                }
                return false;
            }
            // Trang bị (3) có options/chỉ số riêng
            return false;
        }
        return true;
    }

    /**
     * Kiểm tra thư có được phép gộp tự động hay không:
     * - Thư Chiếm Đảo: luôn được gộp.
     * - Thư Hành Trang Đầy: luôn được gộp (gom tất cả đồ gửi về thư do đầy túi).
     * - Các thư khác: gộp nếu là tài nguyên, EXP hoặc vật phẩm xếp chồng.
     */
    public static boolean isMergeableMail(MailEntry mail) {
        if (mail == null || mail.gifts == null || mail.gifts.isEmpty() || mail.isClaimed || mail.canReply) {
            return false;
        }
        if (isTestMail(mail)) {
            return false;
        }
        if (isChiemDaoMail(mail) || isBagOverflowMail(mail)) {
            return true;
        }
        return isPureResourceMail(mail.gifts);
    }

    /**
     * Xác định CategoryKey nhóm các thư quà cùng nguồn hoạt động để tự động gộp & cộng dồn.
     */
    public static String getMailCategoryKey(String title, String content) {
        String t = (title != null ? title.toLowerCase().trim() : "");
        String c = (content != null ? content.toLowerCase().trim() : "");
        String full = t + " " + c;

        // Bỏ qua các thư test riêng biệt
        if (isTestMail(title, content)) {
            return null;
        }

        // 1. Chiếm Đảo
        if (full.contains("chiếm đảo") || full.contains("chiem dao") || full.contains("chiem_dao") || full.contains("chiếm được đảo")) {
            return "CAT_CHIEM_DAO";
        }
        // 2. Siêu Trùm / Super Boss
        if (full.contains("siêu trùm") || full.contains("sieu trum") || full.contains("super boss") || full.contains("super_boss") || full.contains("săn boss")) {
            return "CAT_SUPER_BOSS";
        }
        // 3. Đấu Trường Tự Do / Đấu Trường
        if (full.contains("đấu trường tự do") || full.contains("dau truong tu do") || full.contains("đấu trường rực lửa") || full.contains("dự đoán trận đấu") || full.contains("đấu trường") || full.contains("dau truong")) {
            return "CAT_DAU_TRUONG";
        }
        // 4. Bảo Vệ Pháo Đài
        if (full.contains("pháo đài") || full.contains("phao dai") || full.contains("phao_dai")) {
            return "CAT_BAO_VE_PHAO_DAI";
        }
        // 5. Trận Chiến Khổng Lồ / Phó Bản Khổng Lồ
        if (full.contains("khổng lồ") || full.contains("khong lo") || full.contains("khong_lo")) {
            return "CAT_TRAN_CHIEN_KHONG_LO";
        }
        // 6. Thủ Lĩnh Biển Khơi
        if (full.contains("thủ lĩnh") || full.contains("thu linh") || full.contains("thu_linh")) {
            return "CAT_THU_LINH_BIEN_KHOI";
        }
        // 7. Đại Chiến Thế Giới / Trận Chiến Lớn
        if (full.contains("trận chiến lớn") || full.contains("tran chien lon") || full.contains("đại chiến thế giới") || full.contains("dai chien the gioi") || full.contains("world war") || full.contains("world_war")) {
            return "CAT_WORLD_WAR";
        }
        // 8. PVP Băng / Bang Hội PVP
        if (full.contains("pvp băng") || full.contains("pvp bang") || full.contains("pvp_bang")) {
            return "CAT_PVP_BANG";
        }
        // 9. Phó Bản Ải Đơn
        if (full.contains("ải đơn") || full.contains("ai don") || full.contains("ai_don")) {
            return "CAT_MAP_AI_DON";
        }
        // 10. Boss Thế Giới / Boss Pica
        if (full.contains("boss thế giới") || full.contains("boss the gioi") || full.contains("boss_the_gioi") || full.contains("boss pica") || full.contains("pica")) {
            return "CAT_BOSS_WORLD";
        }
        // 11. Đua Top / Bảng Xếp Hạng
        if (full.contains("đua top") || full.contains("dua top") || full.contains("quà top") || full.contains("qua top") || full.contains("thưởng top") || full.contains("thuong top") || full.contains("hạng ") || full.contains("hang ") || full.contains("bảng xếp hạng")) {
            String baseTitle = t.replaceAll("\\(.*?\\)", "").trim();
            return "CAT_TOP_" + baseTitle;
        }
        // 12. Hành trang đầy / Overflow
        if (full.contains("hành trang") || full.contains("chỗ trống") || full.contains("đầy rương")) {
            return "CAT_BAG_OVERFLOW";
        }
        // 13. Sự Kiện
        if (full.contains("sự kiện") || full.contains("su kien") || full.contains("trồng cây") || full.contains("lộc chia vui") || full.contains("thả lồng đèn") || full.contains("pháo hoa")) {
            String baseTitle = t.replaceAll("\\(.*?\\)", "").trim();
            return "CAT_EVENT_" + baseTitle;
        }

        // Mặc định: gom theo Tiêu đề đã chuẩn hóa
        if (!t.isEmpty()) {
            return "TITLE_" + t;
        }
        return null;
    }

    public static String getStandardizedTitle(String originalTitle, String categoryKey) {
        if (categoryKey == null) return (originalTitle != null && !originalTitle.isEmpty()) ? originalTitle : "Thư";
        if (categoryKey.equals("CAT_CHIEM_DAO")) return "Thưởng Chiếm Đảo Bang Hội";
        if (categoryKey.equals("CAT_SUPER_BOSS")) return "Thưởng Siêu Trùm (Tích Lũy)";
        if (categoryKey.equals("CAT_DAU_TRUONG")) return "Thưởng Đấu Trường (Tích Lũy)";
        if (categoryKey.equals("CAT_BAO_VE_PHAO_DAI")) return "Thưởng Bảo Vệ Pháo Đài (Tích Lũy)";
        if (categoryKey.equals("CAT_TRAN_CHIEN_KHONG_LO")) return "Thưởng Phó Bản Khổng Lồ (Tích Lũy)";
        if (categoryKey.equals("CAT_THU_LINH_BIEN_KHOI")) return "Thưởng Thủ Lĩnh Biển Khơi (Tích Lũy)";
        if (categoryKey.equals("CAT_WORLD_WAR")) return "Thưởng Đại Chiến Thế Giới (Tích Lũy)";
        if (categoryKey.equals("CAT_PVP_BANG")) return "Thưởng PVP Băng (Tích Lũy)";
        if (categoryKey.equals("CAT_MAP_AI_DON")) return "Thưởng Ải Đơn (Tích Lũy)";
        if (categoryKey.equals("CAT_BOSS_WORLD")) return "Thưởng Săn Boss (Tích Lũy)";
        if (categoryKey.equals("CAT_BAG_OVERFLOW")) return "Quà Đính Kèm (Hành Trang Đầy)";
        if (originalTitle != null && !originalTitle.isEmpty()) {
            return originalTitle;
        }
        return "Phần Thưởng Tích Lũy";
    }

    public static String getStandardizedContent(String originalContent, String categoryKey) {
        if (categoryKey == null) return (originalContent != null && !originalContent.isEmpty()) ? originalContent : "";
        if (categoryKey.equals("CAT_CHIEM_DAO")) return "Tổng kết phần thưởng tích lũy từ hoạt động Chiếm Đảo (Beri, Ruby, nguyên liệu). Chi tiết phần thưởng đính kèm bên dưới:";
        if (categoryKey.equals("CAT_SUPER_BOSS")) return "Tổng kết phần thưởng tiền tệ và điểm thưởng từ hoạt động Săn Siêu Trùm đính kèm bên dưới:";
        if (categoryKey.equals("CAT_DAU_TRUONG")) return "Tổng kết phần thưởng tiền tệ và điểm tích lũy từ các trận thi đấu Đấu Trường đính kèm bên dưới:";
        if (categoryKey.equals("CAT_BAO_VE_PHAO_DAI")) return "Tổng kết phần thưởng tiền tệ từ hoạt động Bảo Vệ Pháo Đài đính kèm bên dưới:";
        if (categoryKey.equals("CAT_TRAN_CHIEN_KHONG_LO")) return "Tổng kết phần thưởng tiền tệ từ hoạt động Trận Chiến Khổng Lồ đính kèm bên dưới:";
        if (categoryKey.equals("CAT_THU_LINH_BIEN_KHOI")) return "Tổng kết phần thưởng tiền tệ và điểm thưởng từ hoạt động Thủ Lĩnh Biển Khơi đính kèm bên dưới:";
        if (categoryKey.equals("CAT_WORLD_WAR")) return "Tổng kết phần thưởng tiền tệ từ hoạt động Đại Chiến Thế Giới đính kèm bên dưới:";
        if (categoryKey.equals("CAT_PVP_BANG")) return "Tổng kết phần thưởng tiền tệ từ hoạt động PVP Băng đính kèm bên dưới:";
        if (categoryKey.equals("CAT_MAP_AI_DON")) return "Tổng kết phần thưởng tiền tệ và EXP từ Phó Bản Ải Đơn đính kèm bên dưới:";
        if (categoryKey.equals("CAT_BOSS_WORLD")) return "Tổng kết phần thưởng tiền tệ và EXP từ hoạt động Săn Boss đính kèm bên dưới:";
        if (categoryKey.equals("CAT_BAG_OVERFLOW")) return "Phần thưởng tích lũy chưa nhận do hành trang đầy trước đó đính kèm bên dưới:";
        if (originalContent != null && !originalContent.isEmpty()) {
            return originalContent;
        }
        return "Tổng kết phần thưởng tích lũy đính kèm bên dưới:";
    }

    public static void mergeGifts(List<GiftBox> target, List<GiftBox> source) {
        if (source == null || source.isEmpty() || target == null) return;
        for (GiftBox src : source) {
            if (src == null || src.num <= 0) continue;
            populateGiftMetadata(src);
            boolean merged = false;
            for (GiftBox dst : target) {
                if (dst != null && dst.type == src.type && dst.id == src.id && (dst.options == null || dst.options.isEmpty()) && (src.options == null || src.options.isEmpty())) {
                    long total = (long) dst.num + src.num;
                    dst.num = (int) Math.min(Integer.MAX_VALUE, total);
                    if (dst.name == null || dst.name.isEmpty()) dst.name = src.name;
                    if (dst.icon == 0) dst.icon = src.icon;
                    if (dst.color == 0) dst.color = src.color;
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                GiftBox copy = new GiftBox();
                copy.id = src.id;
                copy.type = src.type;
                copy.num = src.num;
                copy.name = src.name;
                copy.icon = src.icon;
                copy.color = src.color;
                copy.options = (src.options != null) ? new ArrayList<>(src.options) : new ArrayList<>();
                populateGiftMetadata(copy);
                target.add(copy);
            }
        }
    }

    /**
     * Tự động quét và gom các thư quà tặng nhỏ lẻ thuần tiền tệ / EXP / Chiếm Đảo
     * cùng nhóm hoạt động thành 1 thư duy nhất cho mỗi nhóm, tự động cộng dồn số lượng quà.
     */
    public static synchronized void consolidateRewardMails(int playerId) {
        long now = System.currentTimeMillis();
        List<MailEntry> unclaimedMails = new ArrayList<>();
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `data` FROM `historys` WHERE `player_id` = ? AND `type` = ? ORDER BY `id` DESC")) {
            ps.setInt(1, playerId);
            ps.setString(2, TYPE_DB_MAIL);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int rowId = rs.getInt("id");
                    String dataStr = rs.getString("data");
                    MailEntry entry = MailEntry.fromJson(rowId, dataStr);
                    if (entry != null && isMailActive(entry, now) && isMergeableMail(entry)) {
                        unclaimedMails.add(entry);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (unclaimedMails.isEmpty()) {
            return;
        }

        // Nhóm các thư theo CategoryKey
        java.util.Map<String, List<MailEntry>> groupMap = new java.util.LinkedHashMap<>();
        for (MailEntry entry : unclaimedMails) {
            String catKey = getMailCategoryKey(entry.title, entry.content);
            if (catKey != null) {
                groupMap.computeIfAbsent(catKey, k -> new ArrayList<>()).add(entry);
            }
        }

        for (java.util.Map.Entry<String, List<MailEntry>> e : groupMap.entrySet()) {
            String catKey = e.getKey();
            List<MailEntry> list = e.getValue();
            if (list == null || list.size() <= 1) continue;

            MailEntry primary = list.get(0);
            List<Integer> idsToDelete = new ArrayList<>();
            for (int i = 1; i < list.size(); i++) {
                MailEntry duplicate = list.get(i);
                mergeGifts(primary.gifts, duplicate.gifts);
                idsToDelete.add(duplicate.id);
            }

            primary.title = getStandardizedTitle(primary.title, catKey);
            primary.content = getStandardizedContent(primary.content, catKey);
            primary.isRead = false;
            primary.expireAt = 0L;
            primary.createdAt = System.currentTimeMillis();

            try (Connection conn = DbManager.gI().getConnect()) {
                try (PreparedStatement psUp = conn.prepareStatement("UPDATE `historys` SET `data` = ? WHERE `id` = ? AND `type` = ?")) {
                    psUp.setString(1, primary.toJson().toJSONString());
                    psUp.setInt(2, primary.id);
                    psUp.setString(3, TYPE_DB_MAIL);
                    psUp.executeUpdate();
                }
                try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM `historys` WHERE `id` = ? AND `type` = ?")) {
                    for (int delId : idsToDelete) {
                        psDel.setInt(1, delId);
                        psDel.setString(2, TYPE_DB_MAIL);
                        psDel.addBatch();
                    }
                    psDel.executeBatch();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            Player onlinePlayer = (primary.playerName != null && !primary.playerName.isEmpty())
                    ? map.Zone.get_player_by_name_allmap(primary.playerName)
                    : map.Zone.get_player_by_id_allmap(playerId);
            if (onlinePlayer != null && onlinePlayer.conn != null) {
                for (int delId : idsToDelete) {
                    sendMailDeletedToClient(onlinePlayer, delId);
                }
                try {
                    sendSingleMailToClient(onlinePlayer, primary);
                } catch (Exception ignored) {}
            }
        }
    }

    /**
     * Tự động quét và gộp thư quà Chiếm Đảo chưa nhận của người chơi thành 1 thư duy nhất, cộng dồn số lượng quà.
     */
    public static synchronized MailEntry consolidateChiemDaoMails(int playerId) {
        consolidateRewardMails(playerId);
        long now = System.currentTimeMillis();
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `data` FROM `historys` WHERE `player_id` = ? AND `type` = ? ORDER BY `id` DESC")) {
            ps.setInt(1, playerId);
            ps.setString(2, TYPE_DB_MAIL);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int rowId = rs.getInt("id");
                    String dataStr = rs.getString("data");
                    MailEntry entry = MailEntry.fromJson(rowId, dataStr);
                    if (entry != null && !entry.isClaimed && isChiemDaoMail(entry) && isMailActive(entry, now)) {
                        return entry;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MailEntry sendChiemDaoMail(Player p, String clanName, List<GiftBox> gifts) {
        if (p == null) return null;
        int accountId = (p.conn != null) ? p.conn.idUser : 0;
        return sendChiemDaoMailOffline(p.IDPlayer, accountId, p.name, clanName, gifts);
    }

    public static MailEntry sendChiemDaoMailOffline(int playerId, int accountId, String playerName, String clanName, List<GiftBox> gifts) {
        String title = "Thưởng Chiếm Đảo Bang Hội";
        String content = (clanName != null && !clanName.isEmpty())
                ? "Chúc mừng bang [" + clanName + "] đã xuất sắc tham gia Chiếm Đảo! Tổng kết phần thưởng đính kèm bên dưới:"
                : "Chúc mừng bạn và bang hội đã tham gia Chiếm Đảo! Tổng kết phần thưởng đính kèm bên dưới:";
        return sendMailOffline(playerId, accountId, playerName, "Bang Hội", title, content, MAIL_TYPE_GIFT, false, gifts, 0L);
    }

    /**
     * Gửi thư cho Player kể cả khi offline:
     * - Nếu là thư Chiếm Đảo hoặc thư nhỏ lẻ thuần tiền tệ & EXP -> Tự động tìm thư cùng loại chưa nhận để gộp & cộng dồn.
     * - Nếu thư chứa trang bị, rương, item hành trang -> Tạo thư riêng độc lập, không gộp bừa bãi.
     */
    public static MailEntry sendMailOffline(int playerId, int accountId, String playerName,
                                            String sender, String title, String content,
                                            byte typeMail, boolean canReply, List<GiftBox> gifts, long durationMs) {
        boolean mergeable = (gifts != null && !gifts.isEmpty() && !canReply)
                && (isChiemDaoMail(title, content) || isBagOverflowMail(title, content) || isPureResourceMail(gifts));
        String catKey = mergeable ? getMailCategoryKey(title, content) : null;
        if (catKey != null) {
            synchronized (MailService.class) {
                consolidateRewardMails(playerId);
                long now = System.currentTimeMillis();
                MailEntry existing = null;
                try (Connection conn = DbManager.gI().getConnect();
                     PreparedStatement ps = conn.prepareStatement(
                         "SELECT `id`, `data` FROM `historys` WHERE `player_id` = ? AND `type` = ? ORDER BY `id` DESC")) {
                    ps.setInt(1, playerId);
                    ps.setString(2, TYPE_DB_MAIL);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int rowId = rs.getInt("id");
                            String dataStr = rs.getString("data");
                            MailEntry entry = MailEntry.fromJson(rowId, dataStr);
                            if (entry != null && isMailActive(entry, now) && isMergeableMail(entry)) {
                                String entryCat = getMailCategoryKey(entry.title, entry.content);
                                if (catKey.equals(entryCat)) {
                                    existing = entry;
                                    break;
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (existing != null) {
                    mergeGifts(existing.gifts, gifts);
                    existing.title = getStandardizedTitle(title, catKey);
                    existing.content = getStandardizedContent(content, catKey);
                    existing.isRead = false;
                    existing.expireAt = 0L;
                    existing.createdAt = System.currentTimeMillis();

                    try (Connection conn = DbManager.gI().getConnect();
                         PreparedStatement ps = conn.prepareStatement("UPDATE `historys` SET `data` = ? WHERE `id` = ? AND `type` = ?")) {
                        ps.setString(1, existing.toJson().toJSONString());
                        ps.setInt(2, existing.id);
                        ps.setString(3, TYPE_DB_MAIL);
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Player onlinePlayer = (playerName != null && !playerName.isEmpty())
                            ? map.Zone.get_player_by_name_allmap(playerName)
                            : ((existing.playerName != null && !existing.playerName.isEmpty())
                                ? map.Zone.get_player_by_name_allmap(existing.playerName)
                                : map.Zone.get_player_by_id_allmap(playerId));
                    if (onlinePlayer != null && onlinePlayer.conn != null) {
                        try {
                            sendSingleMailToClient(onlinePlayer, existing);
                        } catch (Exception ignored) {}
                    }
                    return existing;
                }
            }
        }

        MailEntry entry = new MailEntry();
        entry.accountId = accountId;
        entry.playerId = playerId;
        entry.playerName = cleanClientText(playerName != null ? playerName : "");
        entry.sender = cleanClientText(sender != null ? sender : "Hệ Thống");
        entry.title = cleanClientText(title != null ? title : "Thư");
        entry.content = cleanClientText(content != null ? content : "");
        entry.typeMail = typeMail;
        entry.canReply = canReply;
        entry.isRead = false;
        entry.isClaimed = false;
        entry.createdAt = System.currentTimeMillis();

        if (gifts != null) {
            gifts = template.GiftBox.consolidateGifts(gifts);
            for (GiftBox gb : gifts) {
                if (gb != null) {
                    populateGiftMetadata(gb);
                    entry.gifts.add(gb);
                }
            }
        }

        // Quy tắc: Nếu thư có quà -> expireAt = 0 (vĩnh viễn cho đến khi nhận quà).
        // Nếu thư không có quà -> expireAt = 7 ngày kể từ lúc gửi (hoặc durationMs nếu chỉ định cụ thể > 0).
        if (entry.gifts != null && !entry.gifts.isEmpty()) {
            entry.expireAt = 0L; // Chưa nhận quà thì không bao giờ hết hạn
        } else {
            entry.expireAt = System.currentTimeMillis() + (durationMs > 0 ? durationMs : DEFAULT_EXPIRE_AFTER_CLAIM_OR_EMPTY);
        }

        String pDate = historys.HistoryManager.getPlayerCreatedAt(playerId);
        String aDate = historys.HistoryManager.getAccountCreatedAt(accountId);
        String user = historys.HistoryManager.getUsernameByPlayerId(playerId);
        String pName = historys.HistoryManager.getPlayerNameById(playerId);
        String pMd5 = historys.HistoryManager.getPlayerMd5(aDate, user, accountId, pDate, pName, playerId);

        JSONObject js = entry.toJson();
        js.put("p_md5", pMd5);
        js.put("p_date", pDate);
        js.put("a_date", aDate);

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO `historys` (`account_id`, `player_id`, `type`, `data`) VALUES (?, ?, ?, ?)",
                 PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, accountId);
            ps.setInt(2, playerId);
            ps.setString(3, TYPE_DB_MAIL);
            ps.setString(4, js.toJSONString());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entry.id = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Nếu người chơi đang online, đẩy gói tin thông báo thư mới vào ChatTabScreen
        Player onlinePlayer = map.Zone.get_player_by_name_allmap(playerName);
        if (onlinePlayer == null && playerId > 0) {
            onlinePlayer = map.Zone.get_player_by_id_allmap(playerId);
        }
        if (onlinePlayer != null && onlinePlayer.conn != null) {
            try {
                sendSingleMailToClient(onlinePlayer, entry);
            } catch (Exception ignored) {}
        }

        return entry;
    }

    /**
     * Lấy danh sách thư còn hiệu lực của Player
     */
    public static List<MailEntry> getActiveMails(int playerId) {
        consolidateRewardMails(playerId);
        List<MailEntry> list = new ArrayList<>();
        long now = System.currentTimeMillis();
        List<Integer> idsToDelete = new ArrayList<>();
        java.util.Set<String> seenUnclaimedTestTitles = new java.util.HashSet<>();

        String playerDateStr = historys.HistoryManager.getPlayerCreatedAt(playerId);
        model.Player onlineP = map.Zone.get_player_by_id_allmap(playerId);

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `data`, `create_at` FROM `historys` WHERE `player_id` = ? AND `type` = ? ORDER BY `id` DESC LIMIT 200")) {
            ps.setInt(1, playerId);
            ps.setString(2, TYPE_DB_MAIL);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int rowId = rs.getInt("id");
                    String dataStr = rs.getString("data");
                    java.sql.Timestamp cAt = rs.getTimestamp("create_at");

                    if (onlineP != null) {
                        if (!historys.HistoryManager.validatePlayerRecord(onlineP, dataStr, cAt)) {
                            idsToDelete.add(rowId);
                            continue;
                        }
                    } else if (playerDateStr != null && !playerDateStr.isEmpty() && cAt != null) {
                        try {
                            org.joda.time.DateTime pdt = org.joda.time.DateTime.parse(playerDateStr);
                            if (cAt.getTime() < (pdt.getMillis() - 5000L)) {
                                idsToDelete.add(rowId);
                                continue;
                            }
                        } catch (Exception ignored) {}
                    }

                    MailEntry entry = MailEntry.fromJson(rowId, dataStr);
                    if (entry != null) {
                        // 0. Tự động xóa sạch mọi thư Quà Test khỏi Hộp Thư
                        if (isTestMail(entry)) {
                            idsToDelete.add(rowId);
                            continue;
                        }
                        // 1. Thư test đã nhận -> Tự động xóa ngay lập tức khỏi DB
                        if (entry.isClaimed && isTestMail(entry)) {
                            idsToDelete.add(rowId);
                            continue;
                        }
                        // 2. Thư test phiên bản cũ không còn trong danh mục mẫu -> Tự động xóa ngay lập tức khỏi DB
                        if (isObsoleteTestMail(entry)) {
                            idsToDelete.add(rowId);
                            continue;
                        }
                        // 3. Thư test chưa nhận: nếu bị duplicate (cùng tiêu đề) -> chỉ giữ 1 bản mới nhất, xóa các bản cũ
                        if (!entry.isClaimed && isTestMail(entry)) {
                            String normTitle = entry.title != null ? entry.title.trim().toLowerCase() : "";
                            if (seenUnclaimedTestTitles.contains(normTitle)) {
                                idsToDelete.add(rowId);
                                continue;
                            }
                            seenUnclaimedTestTitles.add(normTitle);
                        }
                        // 4. Kiểm tra còn hiệu lực
                        if (isMailActive(entry, now)) {
                            list.add(entry);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Xóa ngay các thư test đã nhận / obsolete / duplicate
        if (!idsToDelete.isEmpty()) {
            try (Connection conn = DbManager.gI().getConnect();
                 PreparedStatement psDel = conn.prepareStatement("DELETE FROM `historys` WHERE `id` = ? AND `type` = ?")) {
                for (int id : idsToDelete) {
                    psDel.setInt(1, id);
                    psDel.setString(2, TYPE_DB_MAIL);
                    psDel.addBatch();
                }
                psDel.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * Lấy thông tin 1 thư cụ thể
     */
    public static MailEntry getMailById(int mailId) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT `id`, `data` FROM `historys` WHERE `id` = ? AND `type` = ?")) {
            ps.setInt(1, mailId);
            ps.setString(2, TYPE_DB_MAIL);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int rowId = rs.getInt("id");
                    String dataStr = rs.getString("data");
                    return MailEntry.fromJson(rowId, dataStr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Nhận quà từ thư:
     * - Kiểm tra ô trống hành trang.
     * - Sau khi nhận quà, đánh dấu isClaimed = true và cập nhật expireAt = 7 ngày kể từ lúc nhận quà.
     */
    public static synchronized boolean claimMailGift(Player p, int mailId) {
        if (p == null || p.isClosed) return false;
        MailEntry entry = getMailById(mailId);
        if (entry == null) {
            p.getService().send_box_ThongBao_OK("Thư không tồn tại hoặc đã hết hạn!");
            return false;
        }

        if (entry.playerId != p.IDPlayer) {
            p.getService().send_box_ThongBao_OK("Thư không thuộc về nhân vật này!");
            return false;
        }

        if (entry.isClaimed) {
            p.getService().send_box_ThongBao_OK("Quà của bức thư này đã được nhận từ trước!");
            return false;
        }

        if (entry.gifts == null || entry.gifts.isEmpty()) {
            p.getService().send_box_ThongBao_OK("Bức thư này không có quà đính kèm.");
            return false;
        }

        if (!RewardService.hasEnoughBagSpace(p, entry.gifts)) {
            p.getService().send_box_ThongBao_OK("Hành trang của bạn không đủ chỗ trống để nhận quà thư này!\nVui lòng dọn dẹp hành trang và thử lại.");
            return false;
        }

        // Cập nhật claimed = true và expireAt = 7 ngày kể từ lúc nhận trong DB trước để chống duplicate exploit
        entry.isClaimed = true;
        entry.isRead = true;
        entry.expireAt = System.currentTimeMillis() + DEFAULT_EXPIRE_AFTER_CLAIM_OR_EMPTY;
        JSONObject updatedJson = entry.toJson();
        boolean dbOk = false;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("UPDATE `historys` SET `data` = ? WHERE `id` = ? AND `type` = ? AND (`data` NOT LIKE '%\"is_claimed\":true%')")) {
            ps.setString(1, updatedJson.toJSONString());
            ps.setInt(2, mailId);
            ps.setString(3, TYPE_DB_MAIL);
            dbOk = (ps.executeUpdate() > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!dbOk) {
            p.getService().send_box_ThongBao_OK("Lỗi hệ thống khi nhận quà, vui lòng thử lại sau.");
            return false;
        }

        try {
            if (entry.gifts != null) {
                for (GiftBox gb : entry.gifts) {
                    populateGiftMetadata(gb);
                }
            }

            Service.send_gift(p, 1, entry.title, "Nhận quà Hộp Thư thành công!", entry.gifts, true);

            if (isTestMail(entry)) {
                // TỰ ĐỘNG XÓA THƯ QUÀ TEST VỪA NHẬN ĐỂ HỘP THƯ LUÔN GỌN GÀNG (TẮT TẠO LẠI)
                deleteMailDirect(p, mailId);
                sendMailDeletedToClient(p, mailId);
            } else {
                // Thư thường: gửi trạng thái đã nhận về Client
                sendSingleMailToClient(p, entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Đánh dấu đã đọc thư và đảm bảo thư không có quà được đếm ngược 7 ngày
     */
    public static void markMailRead(Player p, int mailId) {
        if (p == null) return;
        MailEntry entry = getMailById(mailId);
        if (entry != null && entry.playerId == p.IDPlayer) {
            boolean changed = false;
            if (!entry.isRead) {
                entry.isRead = true;
                changed = true;
            }
            // Nếu thư không có quà hoặc đã nhận quà mà chưa có hạn 7 ngày, set hạn 7 ngày
            if ((entry.isClaimed || entry.gifts == null || entry.gifts.isEmpty()) && entry.expireAt == 0) {
                entry.expireAt = System.currentTimeMillis() + DEFAULT_EXPIRE_AFTER_CLAIM_OR_EMPTY;
                changed = true;
            }
            if (changed) {
                try (Connection conn = DbManager.gI().getConnect();
                     PreparedStatement ps = conn.prepareStatement("UPDATE `historys` SET `data` = ? WHERE `id` = ? AND `type` = ?")) {
                    ps.setString(1, entry.toJson().toJSONString());
                    ps.setInt(2, mailId);
                    ps.setString(3, TYPE_DB_MAIL);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Dọn dẹp các thư đã nhận quà hoặc không có quà quá 7 ngày.
     * Tự động xóa vĩnh viễn toàn bộ thư quà test đã nhận, thư quà test cũ/obsolete, và thư quà test duplicate.
     * TUYỆT ĐỐI KHÔNG XÓA thư thường chưa nhận quà.
     */
    public static int cleanExpiredMails() {
        long now = System.currentTimeMillis();
        int deletedCount = 0;
        List<Integer> idsToDelete = new ArrayList<>();
        java.util.Map<Integer, java.util.Set<String>> seenUnclaimedPerPlayer = new java.util.HashMap<>();

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("SELECT `id`, `player_id`, `data` FROM `historys` WHERE `type` = ?")) {
            ps.setString(1, TYPE_DB_MAIL);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int rowId = rs.getInt("id");
                    int pId = rs.getInt("player_id");
                    String dataStr = rs.getString("data");
                    MailEntry entry = MailEntry.fromJson(rowId, dataStr);
                    if (entry != null) {
                        // 0. Tự động xóa sạch mọi thư Quà Test khỏi DB
                        if (isTestMail(entry)) {
                            idsToDelete.add(rowId);
                            continue;
                        }
                        // 1. Thư quà test đã nhận -> Tự động xóa ngay lập tức khỏi DB
                        if (entry.isClaimed && isTestMail(entry)) {
                            idsToDelete.add(rowId);
                            continue;
                        }
                        // 2. Thư quà test phiên bản cũ không còn dùng -> Tự động xóa ngay lập tức
                        if (isObsoleteTestMail(entry)) {
                            idsToDelete.add(rowId);
                            continue;
                        }
                        // 3. Thư quà test chưa nhận bị trùng lặp -> Giữ 1 bản mới nhất, xóa các bản cũ
                        if (!entry.isClaimed && isTestMail(entry)) {
                            java.util.Set<String> titles = seenUnclaimedPerPlayer.computeIfAbsent(pId, k -> new java.util.HashSet<>());
                            String normTitle = entry.title != null ? entry.title.trim().toLowerCase() : "";
                            if (titles.contains(normTitle)) {
                                idsToDelete.add(rowId);
                                continue;
                            }
                            titles.add(normTitle);
                        }
                        // 4. Thư có quà chưa nhận -> BỎ QUA KHÔNG BAO GIỜ XÓA
                        if (!entry.isClaimed && entry.gifts != null && !entry.gifts.isEmpty()) {
                            continue;
                        }
                        // 5. Thư đã nhận hoặc không có quà: nếu đã hết hạn (expireAt <= now và expireAt > 0)
                        if (entry.expireAt > 0 && entry.expireAt <= now) {
                            idsToDelete.add(rowId);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!idsToDelete.isEmpty()) {
            try (Connection conn = DbManager.gI().getConnect()) {
                conn.setAutoCommit(false);
                try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM `historys` WHERE `id` = ? AND `type` = ?")) {
                    for (int id : idsToDelete) {
                        psDel.setInt(1, id);
                        psDel.setString(2, TYPE_DB_MAIL);
                        psDel.addBatch();
                    }
                    int[] results = psDel.executeBatch();
                    conn.commit();
                    deletedCount = results.length;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (deletedCount > 0) {
            Log.info("MailService", "Đã dọn dẹp " + deletedCount + " thư hết hạn / thư quà test cũ đã nhận.");
        }
        return deletedCount;
    }

    /**
     * Xóa thư khỏi DB kèm cơ chế xác nhận YesNo thông minh:
     * - Nếu đã nhận quà hoặc không có quà: Xóa ngay lập tức.
     * - Nếu chưa nhận quà: Hiện menu 3 nút [Nhận quà], [Xóa bỏ], [Đóng].
     *   Nếu bấm [Xóa bỏ], hiện tiếp menu 2 nút [Chắc chắn], [Hủy].
     */
    public static void deleteMail(Player p, int mailId) {
        if (p == null) return;
        MailEntry entry = getMailById(mailId);
        if (entry == null) {
            sendMailDeletedToClient(p, mailId);
            return;
        }

        if (entry.playerId != p.IDPlayer) {
            p.getService().send_box_ThongBao_OK("Thư không thuộc về nhân vật này!");
            return;
        }

        // Nếu thư đã nhận quà HOẶC không có quà đính kèm:
        if (entry.isClaimed || entry.gifts == null || entry.gifts.isEmpty()) {
            deleteMailDirect(p, mailId);
            sendMailDeletedToClient(p, mailId);
            p.getService().send_box_ThongBao_OK("Đã xóa thư thành công.");
            return;
        }

        // Nếu thư CHƯA nhận quà và có quà đính kèm:
        List<model.Menu> menus = new ArrayList<>();
        menus.add(new model.Menu("Nhận quà", (short) 136, () -> {
            claimMailGift(p, mailId);
        }));
        menus.add(new model.Menu("Xóa bỏ", (short) 133, () -> {
            List<model.Menu> confirmMenus = new ArrayList<>();
            confirmMenus.add(new model.Menu("Chắc chắn", (short) 133, () -> {
                deleteMailDirect(p, mailId);
                sendMailDeletedToClient(p, mailId);
                p.getService().send_box_ThongBao_OK("Đã xóa thư và hủy quà đính kèm.");
            }));
            confirmMenus.add(new model.Menu("Hủy", (short) 134, () -> {}));
            p.getService().openDynamicMenu(9922, "Bạn có CHẮC CHẮN muốn xóa bức thư này và BỎ TOÀN BỘ quà đính kèm không?", confirmMenus);
        }));
        menus.add(new model.Menu("Đóng", (short) 134, () -> {}));

        p.getService().openDynamicMenu(9921, "Thư [" + entry.title + "] vẫn còn quà chưa nhận!\nBạn muốn làm gì?", menus);
    }

    public static boolean deleteMailDirect(Player p, int mailId) {
        if (p == null) return false;
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM `historys` WHERE `id` = ? AND `player_id` = ? AND `type` = ?")) {
            ps.setInt(1, mailId);
            ps.setInt(2, p.IDPlayer);
            ps.setString(3, TYPE_DB_MAIL);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteMail(int mailId) {
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM `historys` WHERE `id` = ? AND `type` = ?")) {
            ps.setInt(1, mailId);
            ps.setString(2, TYPE_DB_MAIL);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void sendMailDeletedToClient(Player p, int mailId) {
        if (p == null || p.conn == null) return;
        try {
            Message m = new Message(-110);
            m.writer().writeByte(2); // Action 2: Delete mail
            m.writer().writeInt(mailId);
            p.addmsg(m);
            m.cleanup();
        } catch (Exception ignored) {}
    }

    /**
     * Kiểm tra xem người chơi đã từng nhận Quà Tân Thủ Hải Tặc trong lịch sử chưa
     */
    public static boolean hasReceivedNewbieGift(int playerId) {
        String sql = "SELECT `id` FROM `historys` WHERE `player_id` = ? AND `type` = ? AND `data` LIKE '%Quà Tân Thủ Hải Tặc%' LIMIT 1";
        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.setString(2, TYPE_DB_MAIL);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tạo danh sách Quà Tân Thủ cho chế độ OPEN:
     * 10M Beri, 100k Ruby, 10k Extol (id 908), 1 Rương đại ác quỷ, 10 Rương ác quỷ, 20 XP Skill, 80 Kinh nghiệm X2, 102 XP x2 Phó bảng
     */
    public static List<GiftBox> createOpenNewbieGifts() {
        List<GiftBox> gifts = new ArrayList<>();
        GiftBox g1 = new GiftBox(); g1.id = 0; g1.type = 4; g1.num = 10_000_000; g1.name = "Beri"; g1.icon = 0; gifts.add(g1);
        GiftBox g2 = new GiftBox(); g2.id = 1; g2.type = 4; g2.num = 100_000; g2.name = "Ruby"; g2.icon = 1; gifts.add(g2);
        GiftBox g3 = new GiftBox(); g3.id = 908; g3.type = 4; g3.num = 10_000; g3.name = "Extol"; g3.icon = 908; gifts.add(g3);
        GiftBox g4 = new GiftBox(); g4.id = 158; g4.type = 4; g4.num = 1; g4.name = "Rương đại ác quỷ"; g4.icon = 112; gifts.add(g4);
        GiftBox g5 = new GiftBox(); g5.id = 29; g5.type = 4; g5.num = 10; g5.name = "Rương ác quỷ"; g5.icon = 9; gifts.add(g5);
        GiftBox g6 = new GiftBox(); g6.id = 333; g6.type = 4; g6.num = 20; g6.name = "XP Skill"; g6.icon = 286; gifts.add(g6);
        GiftBox g7 = new GiftBox(); g7.id = 80; g7.type = 4; g7.num = 80; g7.name = "Kinh nghiệm X2"; g7.icon = 60; gifts.add(g7);
        GiftBox g8 = new GiftBox(); g8.id = 102; g8.type = 4; g8.num = 102; g8.name = "XP x2 Phó bảng"; g8.icon = 96; gifts.add(g8);
        return gifts;
    }

    /**
     * Tạo danh sách các gói Hòm Thư Quà Test vô hạn riêng biệt (số lượng x9999) cho chế độ TEST
     */
    public static List<MailEntry> createAllTestMailTemplates(int playerId, int userId, String playerName) {
        List<MailEntry> list = new ArrayList<>();

        // 1. Tiền Tệ Vô Hạn
        List<GiftBox> m1 = new ArrayList<>();
        GiftBox b1 = new GiftBox(); b1.id = 0; b1.type = 4; b1.num = 2_000_000_000; b1.name = "Beri"; b1.icon = 0; m1.add(b1);
        GiftBox b2 = new GiftBox(); b2.id = 1; b2.type = 4; b2.num = 2_000_000; b2.name = "Ruby"; b2.icon = 1; m1.add(b2);
        GiftBox b3 = new GiftBox(); b3.id = 908; b3.type = 4; b3.num = 99_999_999; b3.name = "Extol"; b3.icon = 908; m1.add(b3);

        // 2. EXP & Hỗ Trợ Đấu Trường / Phó Bản
        List<GiftBox> m2 = new ArrayList<>();
        GiftBox e1 = new GiftBox(); e1.id = 333; e1.type = 4; e1.num = 9999; e1.name = "XP Skill"; e1.icon = 286; m2.add(e1);
        GiftBox e2 = new GiftBox(); e2.id = 80; e2.type = 4; e2.num = 9999; e2.name = "Kinh nghiệm X2"; e2.icon = 60; m2.add(e2);
        GiftBox e3 = new GiftBox(); e3.id = 102; e3.type = 4; e3.num = 9999; e3.name = "XP x2 Phó bảng"; e3.icon = 96; m2.add(e3);
        GiftBox e4 = new GiftBox(); e4.id = 101; e4.type = 4; e4.num = 9999; e4.name = "Thẻ XP Đấu Trường"; e4.icon = 95; m2.add(e4);
        GiftBox e5 = new GiftBox(); e5.id = 35; e5.type = 4; e5.num = 9999; e5.name = "Vé Đấu Trường"; e5.icon = 13; m2.add(e5);
        GiftBox e6 = new GiftBox(); e6.id = 40; e6.type = 4; e6.num = 9999; e6.name = "Chìa Khóa Phó Bản"; e6.icon = 18; m2.add(e6);
        GiftBox e7 = new GiftBox(); e7.id = 89; e7.type = 4; e7.num = 9999; e7.name = "Vé Hồi Sinh"; e7.icon = 67; m2.add(e7);

        // 3. Đan Dược & Dược Phẩm Bồi Dưỡng
        List<GiftBox> m3 = new ArrayList<>();
        GiftBox d1 = new GiftBox(); d1.id = 413; d1.type = 4; d1.num = 9999; d1.name = "Tiến cấp đơn"; d1.icon = 366; m3.add(d1);
        GiftBox d2 = new GiftBox(); d2.id = 414; d2.type = 4; d2.num = 9999; d2.name = "Kỹ năng đơn"; d2.icon = 367; m3.add(d2);
        GiftBox d3 = new GiftBox(); d3.id = 415; d3.type = 4; d3.num = 9999; d3.name = "Đại bổ đơn"; d3.icon = 368; m3.add(d3);
        GiftBox d4 = new GiftBox(); d4.id = 83; d4.type = 4; d4.num = 9999; d4.name = "Lọ hồi sức 100%"; d4.icon = 63; m3.add(d4);
        GiftBox d5 = new GiftBox(); d5.id = 177; d5.type = 4; d5.num = 9999; d5.name = "Dược phẩm cao cấp"; d5.icon = 128; m3.add(d5);
        GiftBox d6 = new GiftBox(); d6.id = 175; d6.type = 4; d6.num = 9999; d6.name = "Nước tăng lực"; d6.icon = 126; m3.add(d6);
        GiftBox d7 = new GiftBox(); d7.id = 334; d7.type = 4; d7.num = 9999; d7.name = "Rumble trắng"; d7.icon = 287; m3.add(d7);
        GiftBox d8 = new GiftBox(); d8.id = 335; d8.type = 4; d8.num = 9999; d8.name = "Rumble lục"; d8.icon = 288; m3.add(d8);
        GiftBox d9 = new GiftBox(); d9.id = 336; d9.type = 4; d9.num = 9999; d9.name = "Rumble tím"; d9.icon = 289; m3.add(d9);
        GiftBox d10 = new GiftBox(); d10.id = 337; d10.type = 4; d10.num = 9999; d10.name = "Rumble cam"; d10.icon = 290; m3.add(d10);
        GiftBox d11 = new GiftBox(); d11.id = 338; d11.type = 4; d11.num = 9999; d11.name = "Rumble đỏ"; d11.icon = 291; m3.add(d11);

        // 4. Trái Ác Quỷ Thần Thoại & Cao Cấp
        List<GiftBox> m4 = new ArrayList<>();
        GiftBox f1 = new GiftBox(); f1.id = 873; f1.type = 4; f1.num = 9999; f1.name = "Trái Nika"; m4.add(f1);
        GiftBox f2 = new GiftBox(); f2.id = 240; f2.type = 4; f2.num = 9999; f2.name = "Trái Chấn Thiên"; m4.add(f2);
        GiftBox f3 = new GiftBox(); f3.id = 160; f3.type = 4; f3.num = 9999; f3.name = "Trái Sét"; m4.add(f3);
        GiftBox f4 = new GiftBox(); f4.id = 427; f4.type = 4; f4.num = 9999; f4.name = "Trái Bóng Tối"; m4.add(f4);
        GiftBox f5 = new GiftBox(); f5.id = 161; f5.type = 4; f5.num = 9999; f5.name = "Trái Nham Thạch"; m4.add(f5);
        GiftBox f6 = new GiftBox(); f6.id = 869; f6.type = 4; f6.num = 9999; f6.name = "Trái Ánh Sáng"; m4.add(f6);
        GiftBox f7 = new GiftBox(); f7.id = 870; f7.type = 4; f7.num = 9999; f7.name = "Trái Tình Yêu"; m4.add(f7);

        // 5. Trái Ác Quỷ Thượng Cấp & Biến Dị
        List<GiftBox> m5 = new ArrayList<>();
        GiftBox s1 = new GiftBox(); s1.id = 32; s1.type = 4; s1.num = 9999; s1.name = "Trái Lửa"; m5.add(s1);
        GiftBox s2 = new GiftBox(); s2.id = 93; s2.type = 4; s2.num = 9999; s2.name = "Trái Cát"; m5.add(s2);
        GiftBox s3 = new GiftBox(); s3.id = 88; s3.type = 4; s3.num = 9999; s3.name = "Trái Khói"; m5.add(s3);
        GiftBox s4 = new GiftBox(); s4.id = 33; s4.type = 4; s4.num = 9999; s4.name = "Trái Cao Su"; m5.add(s4);
        GiftBox s5 = new GiftBox(); s5.id = 219; s5.type = 4; s5.num = 9999; s5.name = "Trái Báo Đốm"; m5.add(s5);
        GiftBox s6 = new GiftBox(); s6.id = 220; s6.type = 4; s6.num = 9999; s6.name = "Trái Chim Ưng"; m5.add(s6);

        // 6. Trái Ác Quỷ Paramecia & Zoan
        List<GiftBox> m6 = new ArrayList<>();
        GiftBox p1 = new GiftBox(); p1.id = 316; p1.type = 4; p1.num = 9999; p1.name = "Trái Sáp"; m6.add(p1);
        GiftBox p2 = new GiftBox(); p2.id = 317; p2.type = 4; p2.num = 9999; p2.name = "Trái Dao"; m6.add(p2);
        GiftBox p3 = new GiftBox(); p3.id = 318; p3.type = 4; p3.num = 9999; p3.name = "Trái Kilo"; m6.add(p3);
        GiftBox p4 = new GiftBox(); p4.id = 90; p4.type = 4; p4.num = 9999; p4.name = "Trái Bò Tót"; m6.add(p4);
        GiftBox p5 = new GiftBox(); p5.id = 91; p5.type = 4; p5.num = 9999; p5.name = "Trái Vượn"; m6.add(p5);
        GiftBox p6 = new GiftBox(); p6.id = 92; p6.type = 4; p6.num = 9999; p6.name = "Trái Bọng"; m6.add(p6);
        GiftBox p7 = new GiftBox(); p7.id = 34; p7.type = 4; p7.num = 9999; p7.name = "Trái Tuần Lộc"; m6.add(p7);

        // 7. Rương Ác Quỷ & Rương Chọn
        List<GiftBox> m7 = new ArrayList<>();
        GiftBox c1 = new GiftBox(); c1.id = 158; c1.type = 4; c1.num = 9999; c1.name = "Rương đại ác quỷ"; c1.icon = 112; m7.add(c1);
        GiftBox c2 = new GiftBox(); c2.id = 29; c2.type = 4; c2.num = 9999; c2.name = "Rương ác quỷ"; c2.icon = 9; m7.add(c2);
        GiftBox c3 = new GiftBox(); c3.id = 690; c3.type = 4; c3.num = 9999; c3.name = "Rương Trái Ác Quỷ Tự Chọn"; m7.add(c3);

        // 8. Đá Khảm Cấp 5 & Cấp 6 (Chỉ giữ Cấp 5, Cấp 6 và Thần Thoại, bỏ vô cực & siêu cấp)
        List<GiftBox> m8 = new ArrayList<>();
        // Đá Cấp 5
        GiftBox k5_1 = new GiftBox(); k5_1.id = 48; k5_1.type = 4; k5_1.num = 9999; k5_1.name = "Cẩm thạch cấp 5"; m8.add(k5_1);
        GiftBox k5_2 = new GiftBox(); k5_2.id = 54; k5_2.type = 4; k5_2.num = 9999; k5_2.name = "Đá Topaz cấp 5"; m8.add(k5_2);
        GiftBox k5_3 = new GiftBox(); k5_3.id = 60; k5_3.type = 4; k5_3.num = 9999; k5_3.name = "Tinh thể ruby cấp 5"; m8.add(k5_3);
        GiftBox k5_4 = new GiftBox(); k5_4.id = 66; k5_4.type = 4; k5_4.num = 9999; k5_4.name = "Ngọc lục bảo cấp 5"; m8.add(k5_4);
        GiftBox k5_5 = new GiftBox(); k5_5.id = 72; k5_5.type = 4; k5_5.num = 9999; k5_5.name = "Đá Saphia cấp 5"; m8.add(k5_5);
        GiftBox k5_6 = new GiftBox(); k5_6.id = 78; k5_6.type = 4; k5_6.num = 9999; k5_6.name = "Thạch anh tím cấp 5"; m8.add(k5_6);
        GiftBox k5_7 = new GiftBox(); k5_7.id = 366; k5_7.type = 4; k5_7.num = 9999; k5_7.name = "Hổ phách cấp 5"; m8.add(k5_7);
        // Đá Cấp 6
        GiftBox k6_1 = new GiftBox(); k6_1.id = 49; k6_1.type = 4; k6_1.num = 9999; k6_1.name = "Cẩm thạch cấp 6"; m8.add(k6_1);
        GiftBox k6_2 = new GiftBox(); k6_2.id = 55; k6_2.type = 4; k6_2.num = 9999; k6_2.name = "Đá Topaz cấp 6"; m8.add(k6_2);
        GiftBox k6_3 = new GiftBox(); k6_3.id = 61; k6_3.type = 4; k6_3.num = 9999; k6_3.name = "Tinh thể ruby cấp 6"; m8.add(k6_3);
        GiftBox k6_4 = new GiftBox(); k6_4.id = 67; k6_4.type = 4; k6_4.num = 9999; k6_4.name = "Ngọc lục bảo cấp 6"; m8.add(k6_4);
        GiftBox k6_5 = new GiftBox(); k6_5.id = 73; k6_5.type = 4; k6_5.num = 9999; k6_5.name = "Đá Saphia cấp 6"; m8.add(k6_5);
        GiftBox k6_6 = new GiftBox(); k6_6.id = 79; k6_6.type = 4; k6_6.num = 9999; k6_6.name = "Thạch anh tím cấp 6"; m8.add(k6_6);
        GiftBox k6_7 = new GiftBox(); k6_7.id = 367; k6_7.type = 4; k6_7.num = 9999; k6_7.name = "Hổ phách cấp 6"; m8.add(k6_7);
        GiftBox k_ns = new GiftBox(); k_ns.id = 0; k_ns.type = 7; k_ns.num = 9999; k_ns.name = "Đá ngũ sắc"; m8.add(k_ns);

        // 8.1. Đá Hải Thạch Cấp 1 Đến Cấp 6 (221..226)
        List<GiftBox> mHaiThach = new ArrayList<>();
        GiftBox ht1 = new GiftBox(); ht1.id = 221; ht1.type = 4; ht1.num = 9999; ht1.name = "Đá Hải Thạch cấp 1"; mHaiThach.add(ht1);
        GiftBox ht2 = new GiftBox(); ht2.id = 222; ht2.type = 4; ht2.num = 9999; ht2.name = "Đá Hải Thạch cấp 2"; mHaiThach.add(ht2);
        GiftBox ht3 = new GiftBox(); ht3.id = 223; ht3.type = 4; ht3.num = 9999; ht3.name = "Đá Hải Thạch cấp 3"; mHaiThach.add(ht3);
        GiftBox ht4 = new GiftBox(); ht4.id = 224; ht4.type = 4; ht4.num = 9999; ht4.name = "Đá Hải Thạch cấp 4"; mHaiThach.add(ht4);
        GiftBox ht5 = new GiftBox(); ht5.id = 225; ht5.type = 4; ht5.num = 9999; ht5.name = "Đá Hải Thạch cấp 5"; mHaiThach.add(ht5);
        GiftBox ht6 = new GiftBox(); ht6.id = 226; ht6.type = 4; ht6.num = 9999; ht6.name = "Đá Hải Thạch cấp 6"; mHaiThach.add(ht6);

        // 9. Thần Thoại - Cẩm Thạch (647..651)
        List<GiftBox> mythicCamThach = new ArrayList<>();
        GiftBox ct1 = new GiftBox(); ct1.id = 647; ct1.type = 4; ct1.num = 9999; ct1.name = "Cẩm thạch - Topaz thần thoại"; mythicCamThach.add(ct1);
        GiftBox ct2 = new GiftBox(); ct2.id = 648; ct2.type = 4; ct2.num = 9999; ct2.name = "Cẩm thạch - Ruby thần thoại"; mythicCamThach.add(ct2);
        GiftBox ct3 = new GiftBox(); ct3.id = 649; ct3.type = 4; ct3.num = 9999; ct3.name = "Cẩm thạch - Ngọc thần thoại"; mythicCamThach.add(ct3);
        GiftBox ct4 = new GiftBox(); ct4.id = 650; ct4.type = 4; ct4.num = 9999; ct4.name = "Cẩm thạch - Saphia thần thoại"; mythicCamThach.add(ct4);
        GiftBox ct5 = new GiftBox(); ct5.id = 651; ct5.type = 4; ct5.num = 9999; ct5.name = "Cẩm thạch - Thạch anh thần thoại"; mythicCamThach.add(ct5);

        // 10. Thần Thoại - Topaz (652..656)
        List<GiftBox> mythicTopaz = new ArrayList<>();
        GiftBox tp1 = new GiftBox(); tp1.id = 652; tp1.type = 4; tp1.num = 9999; tp1.name = "Topaz - Cẩm thạch thần thoại"; mythicTopaz.add(tp1);
        GiftBox tp2 = new GiftBox(); tp2.id = 653; tp2.type = 4; tp2.num = 9999; tp2.name = "Topaz - Ruby thần thoại"; mythicTopaz.add(tp2);
        GiftBox tp3 = new GiftBox(); tp3.id = 654; tp3.type = 4; tp3.num = 9999; tp3.name = "Topaz - Ngọc thần thoại"; mythicTopaz.add(tp3);
        GiftBox tp4 = new GiftBox(); tp4.id = 655; tp4.type = 4; tp4.num = 9999; tp4.name = "Topaz - Saphia thần thoại"; mythicTopaz.add(tp4);
        GiftBox tp5 = new GiftBox(); tp5.id = 656; tp5.type = 4; tp5.num = 9999; tp5.name = "Topaz - Thạch Anh thần thoại"; mythicTopaz.add(tp5);

        // 11. Thần Thoại - Ruby (657..661)
        List<GiftBox> mythicRuby = new ArrayList<>();
        GiftBox rb1 = new GiftBox(); rb1.id = 657; rb1.type = 4; rb1.num = 9999; rb1.name = "Ruby - Cẩm thạch thần thoại"; mythicRuby.add(rb1);
        GiftBox rb2 = new GiftBox(); rb2.id = 658; rb2.type = 4; rb2.num = 9999; rb2.name = "Ruby - Topaz thần thoại"; mythicRuby.add(rb2);
        GiftBox rb3 = new GiftBox(); rb3.id = 659; rb3.type = 4; rb3.num = 9999; rb3.name = "Ruby - Ngọc thần thoại"; mythicRuby.add(rb3);
        GiftBox rb4 = new GiftBox(); rb4.id = 660; rb4.type = 4; rb4.num = 9999; rb4.name = "Ruby - Saphia thần thoại"; mythicRuby.add(rb4);
        GiftBox rb5 = new GiftBox(); rb5.id = 661; rb5.type = 4; rb5.num = 9999; rb5.name = "Ruby - Thạch anh thần thoại"; mythicRuby.add(rb5);

        // 12. Thần Thoại - Ngọc Lục Bảo (662..666)
        List<GiftBox> mythicNgoc = new ArrayList<>();
        GiftBox ng1 = new GiftBox(); ng1.id = 662; ng1.type = 4; ng1.num = 9999; ng1.name = "Ngọc - Cẩm thạch thần thoại"; mythicNgoc.add(ng1);
        GiftBox ng2 = new GiftBox(); ng2.id = 663; ng2.type = 4; ng2.num = 9999; ng2.name = "Ngọc - Topaz thần thoại"; mythicNgoc.add(ng2);
        GiftBox ng3 = new GiftBox(); ng3.id = 664; ng3.type = 4; ng3.num = 9999; ng3.name = "Ngọc - Ruby thần thoại"; mythicNgoc.add(ng3);
        GiftBox ng4 = new GiftBox(); ng4.id = 665; ng4.type = 4; ng4.num = 9999; ng4.name = "Ngọc - Saphia thần thoại"; mythicNgoc.add(ng4);
        GiftBox ng5 = new GiftBox(); ng5.id = 666; ng5.type = 4; ng5.num = 9999; ng5.name = "Ngọc - Thạch anh thần thoại"; mythicNgoc.add(ng5);

        // 13. Thần Thoại - Saphia (667..671)
        List<GiftBox> mythicSaphia = new ArrayList<>();
        GiftBox sp1 = new GiftBox(); sp1.id = 667; sp1.type = 4; sp1.num = 9999; sp1.name = "Saphia - Cẩm thạch thần thoại"; mythicSaphia.add(sp1);
        GiftBox sp2 = new GiftBox(); sp2.id = 668; sp2.type = 4; sp2.num = 9999; sp2.name = "Saphia - Topaz thần thoại"; mythicSaphia.add(sp2);
        GiftBox sp3 = new GiftBox(); sp3.id = 669; sp3.type = 4; sp3.num = 9999; sp3.name = "Saphia - Ruby thần thoại"; mythicSaphia.add(sp3);
        GiftBox sp4 = new GiftBox(); sp4.id = 670; sp4.type = 4; sp4.num = 9999; sp4.name = "Saphia - Ngọc thần thoại"; mythicSaphia.add(sp4);
        GiftBox sp5 = new GiftBox(); sp5.id = 671; sp5.type = 4; sp5.num = 9999; sp5.name = "Saphia - Thạch anh thần thoại"; mythicSaphia.add(sp5);

        // 14. Thần Thoại - Thạch Anh (672..676)
        List<GiftBox> mythicThachAnh = new ArrayList<>();
        GiftBox ta1 = new GiftBox(); ta1.id = 672; ta1.type = 4; ta1.num = 9999; ta1.name = "Thạch anh - Cẩm thạch thần thoại"; mythicThachAnh.add(ta1);
        GiftBox ta2 = new GiftBox(); ta2.id = 673; ta2.type = 4; ta2.num = 9999; ta2.name = "Thạch anh - Topaz thần thoại"; mythicThachAnh.add(ta2);
        GiftBox ta3 = new GiftBox(); ta3.id = 674; ta3.type = 4; ta3.num = 9999; ta3.name = "Thạch anh - Ruby thần thoại"; mythicThachAnh.add(ta3);
        GiftBox ta4 = new GiftBox(); ta4.id = 675; ta4.type = 4; ta4.num = 9999; ta4.name = "Thạch anh - Ngọc thần thoại"; mythicThachAnh.add(ta4);
        GiftBox ta5 = new GiftBox(); ta5.id = 676; ta5.type = 4; ta5.num = 9999; ta5.name = "Thạch anh - Saphia thần thoại"; mythicThachAnh.add(ta5);

        // 15. Thần Thoại - Hổ Phách (677..682)
        List<GiftBox> mythicHoPhach = new ArrayList<>();
        GiftBox hp1 = new GiftBox(); hp1.id = 677; hp1.type = 4; hp1.num = 9999; hp1.name = "Hổ phách - Cẩm thạch thần thoại"; mythicHoPhach.add(hp1);
        GiftBox hp2 = new GiftBox(); hp2.id = 678; hp2.type = 4; hp2.num = 9999; hp2.name = "Hổ phách - Topaz thần thoại"; mythicHoPhach.add(hp2);
        GiftBox hp3 = new GiftBox(); hp3.id = 679; hp3.type = 4; hp3.num = 9999; hp3.name = "Hổ phách - Ruby thần thoại"; mythicHoPhach.add(hp3);
        GiftBox hp4 = new GiftBox(); hp4.id = 680; hp4.type = 4; hp4.num = 9999; hp4.name = "Hổ phách - Ngọc thần thoại"; mythicHoPhach.add(hp4);
        GiftBox hp5 = new GiftBox(); hp5.id = 681; hp5.type = 4; hp5.num = 9999; hp5.name = "Hổ phách - Saphia thần thoại"; mythicHoPhach.add(hp5);
        GiftBox hp6 = new GiftBox(); hp6.id = 682; hp6.type = 4; hp6.num = 9999; hp6.name = "Hổ phách - Thạch anh thần thoại"; mythicHoPhach.add(hp6);

        // 16. Nguyên Liệu Cường Hóa (Chỉ giữ nguyên liệu cao cấp & cần thiết)
        List<GiftBox> m10 = new ArrayList<>();
        GiftBox n1 = new GiftBox(); n1.id = 4; n1.type = 7; n1.num = 9999; n1.name = "Bột vàng"; m10.add(n1);
        GiftBox n2 = new GiftBox(); n2.id = 18; n2.type = 7; n2.num = 9999; n2.name = "Bột Siêu Cấp"; m10.add(n2);
        GiftBox n3 = new GiftBox(); n3.id = 19; n3.type = 7; n3.num = 9999; n3.name = "Tinh Thể Vàng"; m10.add(n3);
        GiftBox n4 = new GiftBox(); n4.id = 5; n4.type = 7; n4.num = 9999; n4.name = "Ngôi sao may mắn"; m10.add(n4);
        GiftBox n5 = new GiftBox(); n5.id = 7; n5.type = 7; n5.num = 9999; n5.name = "Tinh Tú may mắn"; m10.add(n5);
        GiftBox n6 = new GiftBox(); n6.id = 12; n6.type = 7; n6.num = 9999; n6.name = "Bùa cường hóa"; m10.add(n6);
        GiftBox n7 = new GiftBox(); n7.id = 11; n7.type = 7; n7.num = 9999; n7.name = "Thiên thạch may mắn"; m10.add(n7);
        GiftBox n8 = new GiftBox(); n8.id = 17; n8.type = 7; n8.num = 9999; n8.name = "Bảo thạch may mắn"; m10.add(n8);
        GiftBox n9 = new GiftBox(); n9.id = 14; n9.type = 7; n9.num = 9999; n9.name = "Sao 5 cánh"; m10.add(n9);
        GiftBox n10 = new GiftBox(); n10.id = 15; n10.type = 7; n10.num = 9999; n10.name = "Sao 6 cánh"; m10.add(n10);
        GiftBox n11 = new GiftBox(); n11.id = 16; n11.type = 7; n11.num = 9999; n11.name = "Sao 8 cánh"; m10.add(n11);
        GiftBox n12 = new GiftBox(); n12.id = 13; n12.type = 7; n12.num = 9999; n12.name = "Lông vũ"; m10.add(n12);
        GiftBox n13 = new GiftBox(); n13.id = 6; n13.type = 7; n13.num = 9999; n13.name = "Mai rùa"; m10.add(n13);
        GiftBox n14 = new GiftBox(); n14.id = 10; n14.type = 7; n14.num = 9999; n14.name = "Khiên"; m10.add(n14);
        GiftBox n15 = new GiftBox(); n15.id = 0; n15.type = 7; n15.num = 9999; n15.name = "Đá ngũ sắc"; m10.add(n15);
        GiftBox n16 = new GiftBox(); n16.id = 323; n16.type = 4; n16.num = 9999; n16.name = "Bùa siêu cấp"; m10.add(n16);

        // 17. Rương Trang Bị Cam (MAX +15)
        List<GiftBox> m11 = new ArrayList<>();
        GiftBox r1 = new GiftBox(); r1.id = 829; r1.type = 4; r1.num = 9999; r1.name = "Rương Cam +15 Cùng Hệ"; m11.add(r1);
        GiftBox r4 = new GiftBox(); r4.id = 157; r4.type = 4; r4.num = 9999; r4.name = "Rương Anh Hùng Lv100"; m11.add(r4);
        GiftBox r5 = new GiftBox(); r5.id = 28; r5.type = 4; r5.num = 9999; r5.name = "Rương Huyền Bí Lv100"; m11.add(r5);
        GiftBox r6 = new GiftBox(); r6.id = 519; r6.type = 4; r6.num = 9999; r6.name = "Rương hành trình"; m11.add(r6);

        // 18. Quà Test: Hệ Thống DIAL Toàn Diện (Tự Mở & Tự Làm)
        List<GiftBox> m12 = new ArrayList<>();
        GiftBox dial1 = new GiftBox(); dial1.id = 451; dial1.type = 4; dial1.num = 9999; dial1.name = "Trang Giấy"; m12.add(dial1);
        GiftBox dial2 = new GiftBox(); dial2.id = 454; dial2.type = 4; dial2.num = 9999; dial2.name = "Mảnh vỏ ốc"; m12.add(dial2);
        GiftBox dial3 = new GiftBox(); dial3.id = 452; dial3.type = 4; dial3.num = 9999; dial3.name = "Sách công thức"; m12.add(dial3);
        GiftBox dial4 = new GiftBox(); dial4.id = 453; dial4.type = 4; dial4.num = 9999; dial4.name = "Vỏ ốc"; m12.add(dial4);
        GiftBox dial5 = new GiftBox(); dial5.id = 455; dial5.type = 4; dial5.num = 9999; dial5.name = "Rương Dial"; m12.add(dial5);
        GiftBox dial6 = new GiftBox(); dial6.id = 823; dial6.type = 4; dial6.num = 9999; dial6.name = "Rương dial truyền thuyết"; m12.add(dial6);
        GiftBox dial7 = new GiftBox(); dial7.id = 457; dial7.type = 4; dial7.num = 9999; dial7.name = "Búa đục DIAL"; m12.add(dial7);
        GiftBox dial8 = new GiftBox(); dial8.id = 908; dial8.type = 4; dial8.num = 9999999; dial8.name = "Extol"; m12.add(dial8);
        GiftBox dial10 = new GiftBox(); dial10.id = 4; dial10.type = 7; dial10.num = 9999; dial10.name = "Bột vàng"; m12.add(dial10);
        GiftBox dial11 = new GiftBox(); dial11.id = 13; dial11.type = 7; dial11.num = 9999; dial11.name = "Lông vũ"; m12.add(dial11);
        GiftBox dial12 = new GiftBox(); dial12.id = 6; dial12.type = 7; dial12.num = 9999; dial12.name = "Mai rùa"; m12.add(dial12);
        GiftBox dial13 = new GiftBox(); dial13.id = 10; dial13.type = 7; dial13.num = 9999; dial13.name = "Khiên"; m12.add(dial13);
        GiftBox dial14 = new GiftBox(); dial14.id = 11; dial14.type = 7; dial14.num = 9999; dial14.name = "Thiên thạch may mắn"; m12.add(dial14);
        GiftBox dial15 = new GiftBox(); dial15.id = 18; dial15.type = 7; dial15.num = 9999; dial15.name = "Bột Siêu Cấp"; m12.add(dial15);
        GiftBox dial16 = new GiftBox(); dial16.id = 19; dial16.type = 7; dial16.num = 9999; dial16.name = "Tinh Thể Vàng"; m12.add(dial16);
        GiftBox dial17 = new GiftBox(); dial17.id = 17; dial17.type = 7; dial17.num = 9999; dial17.name = "Bảo thạch may mắn"; m12.add(dial17);
        GiftBox dial18 = new GiftBox(); dial18.id = 0; dial18.type = 7; dial18.num = 9999; dial18.name = "Đá ngũ sắc"; m12.add(dial18);
        GiftBox dial21 = new GiftBox(); dial21.id = 12004; dial21.type = 3; dial21.num = 1; dial21.name = "Dial Truyền thuyết"; m12.add(dial21);
        GiftBox dial22 = new GiftBox(); dial22.id = 12003; dial22.type = 3; dial22.num = 1; dial22.name = "Dial Thần Thoại"; m12.add(dial22);

        // 19. Quà Test: Rương Hành Trình (Item4 519 x 9999)
        List<GiftBox> mHanhTrinh = new ArrayList<>();
        GiftBox htBox = new GiftBox(); htBox.id = 519; htBox.type = 4; htBox.num = 9999; htBox.name = "Rương hành trình"; mHanhTrinh.add(htBox);

        // 20. Quà Test: Sách Haki Hoàn Chỉnh (643, 753, 754)
        List<GiftBox> mHakiSach = new ArrayList<>();
        GiftBox hk1 = new GiftBox(); hk1.id = 643; hk1.type = 4; hk1.num = 9999; hk1.name = "Sách Haki Quan Sát"; mHakiSach.add(hk1);
        GiftBox hk2 = new GiftBox(); hk2.id = 753; hk2.type = 4; hk2.num = 9999; hk2.name = "Sách Haki Vũ Trang"; mHakiSach.add(hk2);
        GiftBox hk3 = new GiftBox(); hk3.id = 754; hk3.type = 4; hk3.num = 9999; hk3.name = "Sách Haki Bá Vương"; mHakiSach.add(hk3);

        // 21. Quà Test: Mảnh Sách Haki (639..642, 755..762)
        List<GiftBox> mHakiManh = new ArrayList<>();
        GiftBox hm1 = new GiftBox(); hm1.id = 639; hm1.type = 4; hm1.num = 9999; hm1.name = "Mảnh Sách Haki Quan Sát 1"; mHakiManh.add(hm1);
        GiftBox hm2 = new GiftBox(); hm2.id = 640; hm2.type = 4; hm2.num = 9999; hm2.name = "Mảnh Sách Haki Quan Sát 2"; mHakiManh.add(hm2);
        GiftBox hm3 = new GiftBox(); hm3.id = 641; hm3.type = 4; hm3.num = 9999; hm3.name = "Mảnh Sách Haki Quan Sát 3"; mHakiManh.add(hm3);
        GiftBox hm4 = new GiftBox(); hm4.id = 642; hm4.type = 4; hm4.num = 9999; hm4.name = "Mảnh Sách Haki Quan Sát 4"; mHakiManh.add(hm4);
        GiftBox hm5 = new GiftBox(); hm5.id = 755; hm5.type = 4; hm5.num = 9999; hm5.name = "Mảnh Sách Haki Vũ Trang 1"; mHakiManh.add(hm5);
        GiftBox hm6 = new GiftBox(); hm6.id = 756; hm6.type = 4; hm6.num = 9999; hm6.name = "Mảnh Sách Haki Vũ Trang 2"; mHakiManh.add(hm6);
        GiftBox hm7 = new GiftBox(); hm7.id = 757; hm7.type = 4; hm7.num = 9999; hm7.name = "Mảnh Sách Haki Vũ Trang 3"; mHakiManh.add(hm7);
        GiftBox hm8 = new GiftBox(); hm8.id = 758; hm8.type = 4; hm8.num = 9999; hm8.name = "Mảnh Sách Haki Vũ Trang 4"; mHakiManh.add(hm8);
        GiftBox hm9 = new GiftBox(); hm9.id = 759; hm9.type = 4; hm9.num = 9999; hm9.name = "Mảnh Sách Haki Bá Vương 1"; mHakiManh.add(hm9);
        GiftBox hm10 = new GiftBox(); hm10.id = 760; hm10.type = 4; hm10.num = 9999; hm10.name = "Mảnh Sách Haki Bá Vương 2"; mHakiManh.add(hm10);
        GiftBox hm11 = new GiftBox(); hm11.id = 761; hm11.type = 4; hm11.num = 9999; hm11.name = "Mảnh Sách Haki Bá Vương 3"; mHakiManh.add(hm11);
        GiftBox hm12 = new GiftBox(); hm12.id = 762; hm12.type = 4; hm12.num = 9999; hm12.name = "Mảnh Sách Haki Bá Vương 4"; mHakiManh.add(hm12);

        MailEntry me1 = new MailEntry(); me1.title = "Quà Test: Tiền Tệ Vô Hạn"; me1.content = "Gói hỗ trợ Test vô hạn: Beri, Ruby, Extol. Nhận xong thư sẽ tự động gửi lại!"; me1.gifts = m1; list.add(me1);
        MailEntry me2 = new MailEntry(); me2.title = "Quà Test: EXP & Hỗ Trợ Đấu Trường"; me2.content = "Gói hỗ trợ Test vô hạn: XP Skill, X2 EXP, Vé & Thẻ Đấu Trường, Vé Hồi Sinh. Nhận xong thư sẽ tự động gửi lại!"; me2.gifts = m2; list.add(me2);
        MailEntry me3 = new MailEntry(); me3.title = "Quà Test: Đan Dược & Bồi Dưỡng"; me3.content = "Gói hỗ trợ Test vô hạn: Tiến cấp đơn, Kỹ năng đơn, Đại bổ đơn, Dược phẩm, Rumble full loại. Nhận xong thư sẽ tự động gửi lại!"; me3.gifts = m3; list.add(me3);
        MailEntry me4 = new MailEntry(); me4.title = "Quà Test: Trái Ác Quỷ Thần Thoại"; me4.content = "Gói hỗ trợ Test vô hạn: Full Trái Ác Quỷ Cao Cấp (Nika, Chấn Thiên, Sét, Nham Thạch, Bóng Tối, Ánh Sáng, Tình Yêu). Nhận xong thư sẽ tự động gửi lại!"; me4.gifts = m4; list.add(me4);
        MailEntry me5 = new MailEntry(); me5.title = "Quà Test: Trái Ác Quỷ Thượng Cấp"; me5.content = "Gói hỗ trợ Test vô hạn: Trái Lửa, Cát, Khói, Cao Su, Báo Đốm, Chim Ưng. Nhận xong thư sẽ tự động gửi lại!"; me5.gifts = m5; list.add(me5);
        MailEntry me6 = new MailEntry(); me6.title = "Quà Test: Trái Ác Quỷ Paramecia & Zoan"; me6.content = "Gói hỗ trợ Test vô hạn: Trái Sáp, Dao, Kilo, Bò Tót, Vượn, Bọng, Tuần Lộc. Nhận xong thư sẽ tự động gửi lại!"; me6.gifts = m6; list.add(me6);
        MailEntry me7 = new MailEntry(); me7.title = "Quà Test: Rương Ác Quỷ & Rương Chọn"; me7.content = "Gói hỗ trợ Test vô hạn: Rương Đại Ác Quỷ, Rương Ác Quỷ, Rương Trái Ác Quỷ Tự Chọn. Nhận xong thư sẽ tự động gửi lại!"; me7.gifts = m7; list.add(me7);
        MailEntry me8 = new MailEntry(); me8.title = "Quà Test: Đá Khảm Cấp 5 & Cấp 6"; me8.content = "Gói hỗ trợ Test vô hạn: Toàn bộ Đá khảm Cấp 5, Cấp 6 & Đá ngũ sắc. Nhận xong thư sẽ tự động gửi lại!"; me8.gifts = m8; list.add(me8);
        MailEntry me8_1 = new MailEntry(); me8_1.title = "Quà Test: Đá Hải Thạch"; me8_1.content = "Gói hỗ trợ Test vô hạn: Toàn bộ Đá Hải Thạch từ Cấp 1 đến Cấp 6 (221 - 226). Nhận xong thư sẽ tự động gửi lại!"; me8_1.gifts = mHaiThach; list.add(me8_1);
        MailEntry me9_1 = new MailEntry(); me9_1.title = "Quà Test: Đá Thần Thoại - Cẩm Thạch"; me9_1.content = "Gói hỗ trợ Test vô hạn: Toàn bộ Đá Thần Thoại dòng Cẩm Thạch. Nhận xong thư sẽ tự động gửi lại!"; me9_1.gifts = mythicCamThach; list.add(me9_1);
        MailEntry me9_2 = new MailEntry(); me9_2.title = "Quà Test: Đá Thần Thoại - Topaz"; me9_2.content = "Gói hỗ trợ Test vô hạn: Toàn bộ Đá Thần Thoại dòng Topaz. Nhận xong thư sẽ tự động gửi lại!"; me9_2.gifts = mythicTopaz; list.add(me9_2);
        MailEntry me9_3 = new MailEntry(); me9_3.title = "Quà Test: Đá Thần Thoại - Ruby"; me9_3.content = "Gói hỗ trợ Test vô hạn: Toàn bộ Đá Thần Thoại dòng Ruby. Nhận xong thư sẽ tự động gửi lại!"; me9_3.gifts = mythicRuby; list.add(me9_3);
        MailEntry me9_4 = new MailEntry(); me9_4.title = "Quà Test: Đá Thần Thoại - Ngọc Lục Bảo"; me9_4.content = "Gói hỗ trợ Test vô hạn: Toàn bộ Đá Thần Thoại dòng Ngọc Lục Bảo. Nhận xong thư sẽ tự động gửi lại!"; me9_4.gifts = mythicNgoc; list.add(me9_4);
        MailEntry me9_5 = new MailEntry(); me9_5.title = "Quà Test: Đá Thần Thoại - Saphia"; me9_5.content = "Gói hỗ trợ Test vô hạn: Toàn bộ Đá Thần Thoại dòng Saphia. Nhận xong thư sẽ tự động gửi lại!"; me9_5.gifts = mythicSaphia; list.add(me9_5);
        MailEntry me9_6 = new MailEntry(); me9_6.title = "Quà Test: Đá Thần Thoại - Thạch Anh"; me9_6.content = "Gói hỗ trợ Test vô hạn: Toàn bộ Đá Thần Thoại dòng Thạch Anh. Nhận xong thư sẽ tự động gửi lại!"; me9_6.gifts = mythicThachAnh; list.add(me9_6);
        MailEntry me9_7 = new MailEntry(); me9_7.title = "Quà Test: Đá Thần Thoại - Hổ Phách"; me9_7.content = "Gói hỗ trợ Test vô hạn: Toàn bộ Đá Thần Thoại dòng Hổ Phách. Nhận xong thư sẽ tự động gửi lại!"; me9_7.gifts = mythicHoPhach; list.add(me9_7);
        MailEntry me10 = new MailEntry(); me10.title = "Quà Test: Nguyên Liệu Cường Hóa"; me10.content = "Gói hỗ trợ Test vô hạn: Bột vàng, Bột siêu cấp, Tinh thể vàng, Bảo thạch, Ngôi sao, Thiên thạch, Sao 5/6/8 cánh, Bùa siêu cấp, Mai rùa, Khiên, Lông vũ. Nhận xong thư sẽ tự động gửi lại!"; me10.gifts = m10; list.add(me10);
        MailEntry me11 = new MailEntry(); me11.title = "Quà Test: Rương Trang Bị Cam (MAX +15)"; me11.content = "Gói hỗ trợ Test vô hạn: Rương Cam +15 Cùng Hệ, Rương Huyền Bí, Rương Anh Hùng, Rương Hành Trình. Nhận xong thư sẽ tự động gửi lại!"; me11.gifts = m11; list.add(me11);
        MailEntry me12 = new MailEntry(); me12.title = "Quà Test: Hệ Thống DIAL Toàn Diện"; me12.content = "Gói hỗ trợ Test vô hạn hệ thống DIAL (Tự làm, Tự mở, Đục lỗ, Cường hóa, Khảm ngọc): Trang giấy, Mảnh vỏ ốc, Sách CT, Vỏ ốc, Rương Dial, Búa đục, Bột vàng, Lông vũ, Mai rùa, Khiên, Thiên thạch, Extol, Dial mẫu. Nhận xong thư sẽ tự động gửi lại!"; me12.gifts = m12; list.add(me12);
        MailEntry me13 = new MailEntry(); me13.title = "Quà Test: Rương Hành Trình"; me13.content = "Gói hỗ trợ Test vô hạn: Rương hành trình (Item4 519 x 9999). Mở tại các làng để nhận Đá Hành Trình. Nhận xong thư sẽ tự động gửi lại!"; me13.gifts = mHanhTrinh; list.add(me13);
        MailEntry me14 = new MailEntry(); me14.title = "Quà Test: Sách Haki Hoàn Chỉnh"; me14.content = "Gói hỗ trợ Test vô hạn: Sách Haki Quan Sát, Sách Haki Vũ Trang, Sách Haki Bá Vương (x9999). Nhận xong thư sẽ tự động gửi lại!"; me14.gifts = mHakiSach; list.add(me14);
        MailEntry me15 = new MailEntry(); me15.title = "Quà Test: Mảnh Sách Haki"; me15.content = "Gói hỗ trợ Test vô hạn: Toàn bộ Mảnh Sách Haki Quan Sát (1..4), Haki Vũ Trang (1..4), Haki Bá Vương (1..4) (x9999). Nhận xong thư sẽ tự động gửi lại!"; me15.gifts = mHakiManh; list.add(me15);

        // === QUÀ TEST THẦN TRANG SET 16 ĐẾN SET 30 (BUFF FULL CẤP MAX +20) ===
        String[] setNames = new String[]{
            "Set 16 - Nham Thạch Magma",
            "Set 17 - Băng Giá Hàn Băng",
            "Set 18 - Ánh Sáng Quang Tốc",
            "Set 19 - Bóng Tối Hắc Ám",
            "Set 20 - Lôi Thần Gomu",
            "Set 21 - Chấn Động Hủy Diệt",
            "Set 22 - Phẫu Thuật ROOM",
            "Set 23 - Từ Tính Trọng Lực",
            "Set 24 - Trùng Độc Ngục Giam",
            "Set 25 - Hóa Đá Mê Hoặc",
            "Set 26 - Hỏa Phượng Hoàng",
            "Set 27 - Đại Phật Vàng",
            "Set 28 - Ngưu Ma Bách Thú",
            "Set 29 - Mãnh Hổ Răng Kiếm",
            "Set 30 - Bạo Chúa Khủng Long"
        };

        for (int s = 0; s < 15; s++) {
            int startItemId = 2604 + (s * 6);
            List<GiftBox> ttGifts = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                int itemId = startItemId + i;
                ItemTemplate3 tpl3 = ItemTemplate3.get_it_by_id(itemId);
                GiftBox gb = new GiftBox();
                gb.id = (short) itemId;
                gb.type = 3;
                gb.num = 1;
                gb.name = (tpl3 != null && tpl3.name != null) ? tpl3.name : ("Thần Trang " + itemId);
                gb.icon = (tpl3 != null) ? tpl3.icon : (short) (3271 + (s * 6) + i);
                gb.color = 8;
                ttGifts.add(gb);
            }
            MailEntry ttMail = new MailEntry();
            ttMail.title = "Quà Test: Thần Trang " + setNames[s] + " (MAX +20)";
            ttMail.content = "Gói hỗ trợ Test Full Bộ 6 Món Thần Trang " + setNames[s] + " (Vũ khí, Mũ, Dây chuyền, Áo, Nhẫn, Giày) Cường Hóa Max +20 (Không Lỗ, Không Khảm Đá, Không Hoàn Mỹ, Không Kích Ẩn). Nhận xong thư sẽ tự động gửi lại.";
            ttMail.gifts = ttGifts;
            list.add(ttMail);
        }

        return list;
    }

    /**
     * Gửi toàn bộ danh sách Hộp Thư cho Player khi Login (Tự động gửi quà test vào thư) bất đồng bộ
     */
    public static void sendAllMailsOnLogin(Player p) {
        if (p == null || p.conn == null) return;
        network.GlobalThreadManager.getPlayerExecutor().execute(() -> {
            try {
                if (p.isClosed || p.conn == null) return;
                List<MailEntry> activeMails = getActiveMails(p.IDPlayer);

                // Xóa các thư quà test cũ đã lỗi thời (ví dụ: thư chứa đá vô cực cũ, thư thần trang cũ, thư set đỏ cũ)
                activeMails.removeIf(m -> {
                    if (m.title != null && (m.title.contains("Đá Khảm Cấp 6 & Vô Cực") || m.title.contains("Đá Vô Cực") || m.title.contains("Set Đỏ")
                            || (m.title.contains("Quà Test: Thần Trang") && !m.title.contains("MAX +20")))) {
                        deleteMailDirect(p, m.id);
                        return true;
                    }
                    return false;
                });

                // Tắt hoàn toàn quà test - Tự động dọn sạch mọi thư Quà Test nếu có trong hộp thư
                activeMails.removeIf(m -> {
                    if (m.title != null && isTestMail(m)) {
                        deleteMailDirect(p, m.id);
                        return true;
                    }
                    return false;
                });

                for (MailEntry entry : activeMails) {
                    try {
                        if (p.isClosed || p.conn == null) break;
                        sendSingleMailToClient(p, entry);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Gửi 1 thư tới Client (Opcode 18 / ChatTab với giao thức mở rộng)
     */
    public static void sendSingleMailToClient(Player p, MailEntry mail) throws IOException {
        if (p == null || p.conn == null || mail == null) return;

        Message m = new Message(18);
        m.writer().writeUTF(cleanClientText(mail.sender)); // Tab name
        m.writer().writeUTF(cleanClientText(mail.content)); // Nội dung chính
        
        // Mở rộng giao thức Mail:
        // Byte maskNotReply / canReply: 1 = Not reply (mask on), 0 = Can reply
        m.writer().writeByte(mail.canReply ? 0 : 1);
        m.writer().writeInt(mail.id);
        m.writer().writeUTF(cleanClientText(mail.title));
        m.writer().writeByte(mail.isClaimed ? 1 : 0);
        m.writer().writeByte(mail.typeMail);
        
        // Gửi danh sách quà đính kèm
        int giftCount = mail.gifts != null ? mail.gifts.size() : 0;
        m.writer().writeByte(giftCount);
        if (giftCount > 0) {
            for (GiftBox gb : mail.gifts) {
                if (gb.icon == 0 && gb.id > 2) {
                    if (gb.type == 4) {
                        ItemTemplate4 t4 = ItemTemplate4.get_it_by_id(gb.id);
                        if (t4 != null) {
                            if (gb.name == null || gb.name.isEmpty()) gb.name = cleanClientText(t4.name);
                            gb.icon = t4.icon;
                        }
                    } else if (gb.type == 7) {
                        ItemTemplate7 t7 = ItemTemplate7.get_it_by_id(gb.id);
                        if (t7 != null) {
                            if (gb.name == null || gb.name.isEmpty()) gb.name = cleanClientText(t7.name);
                            gb.icon = (short) t7.icon;
                        }
                    } else if (gb.type == 3) {
                        ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(gb.id);
                        if (t3 != null) {
                            if (gb.name == null || gb.name.isEmpty()) gb.name = cleanClientText(t3.name);
                            gb.icon = t3.icon;
                        }
                    }
                }
                m.writer().writeByte(gb.type);
                m.writer().writeShort(gb.id);
                m.writer().writeUTF(cleanClientText(gb.name != null ? gb.name : ""));
                m.writer().writeShort(gb.icon);
                m.writer().writeInt(gb.num);
                m.writer().writeByte(gb.color);
            }
        }

        p.addmsg(m);
        m.cleanup();
    }

    /**
     * Gửi trực tiếp toàn bộ hơn 30 gói Quà Test Vô Hạn vào Hộp Thư người chơi ngay lập tức
     */
    public static void sendTestMailsDirect(Player p) {
        if (p == null || p.conn == null) return;
        network.GlobalThreadManager.getPlayerExecutor().execute(() -> {
            try {
                if (p.isClosed || p.conn == null) return;
                List<MailEntry> activeMails = getActiveMails(p.IDPlayer);
                List<MailEntry> testTemplates = createAllTestMailTemplates(p.IDPlayer, p.conn.idUser, p.name);
                int countAdded = 0;
                for (MailEntry tpl : testTemplates) {
                    boolean existsUnclaimed = false;
                    for (MailEntry existing : activeMails) {
                        if (!existing.isClaimed && existing.title != null && existing.title.equalsIgnoreCase(tpl.title)) {
                            existsUnclaimed = true;
                            break;
                        }
                    }
                    if (!existsUnclaimed) {
                        MailEntry created = sendMailOffline(p.IDPlayer, p.conn.idUser, p.name, "Hệ Thống",
                                tpl.title, tpl.content, MAIL_TYPE_GIFT, false, tpl.gifts, 0L);
                        if (created != null) {
                            activeMails.add(created);
                            try {
                                sendSingleMailToClient(p, created);
                            } catch (Exception ignored) {}
                            countAdded++;
                        }
                    }
                }
                if (countAdded > 0) {
                    p.getService().send_box_ThongBao_OK("Đã gửi trọn bộ " + countAdded + " gói Quà Test Vô Hạn vào Hộp Thư! Mở hòm thư để nhận.");
                } else {
                    p.getService().send_box_ThongBao_OK("Hộp Thư của bạn đã có sẵn toàn bộ các gói Quà Test Vô Hạn!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
