package bot.botplayer;

import database.DbManager;
import org.joda.time.DateTime;
import java.sql.*;
import itemz.Item;
import template.Item_wear;
import skill.Skill_info;
import model.DeTu;
import model.Quest;
import template.QuestP;

/**
 * BotDb — Facade đọc/ghi bảng players_bot (riêng biệt với players).
 */
public class BotDb {

    // ========================= TABLE INIT =========================

    /**
     * Data migrations for Bot (gọi 1 lần lúc server khởi động từ DbManager).
     * Bảng players_bot đã được tạo trong DbManager.
     */
    public static void initBotTable() {
        Connection conn = null;
        Statement st = null;
        try {
            conn = DbManager.gI().getConnect();
            st = conn.createStatement();
            // Run database migrations to fix bot fashion
            try {
                st.executeUpdate("ALTER TABLE `players_bot` MODIFY `name` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL;");
                st.executeUpdate("UPDATE `players_bot` SET `fashion` = '[[[103,1,3,1],[108,0,2,1]],[],[[1,1],[5,1],[3,1],[7,1]]]' WHERE `clazz` = 2 AND `fashion` LIKE '%103,1,24%';");
                st.executeUpdate("UPDATE `players_bot` SET `fashion` = '[[[103,2,5,1],[108,0,4,1]],[],[[1,1],[5,1],[3,1],[7,1]]]' WHERE `clazz` = 3 AND `fashion` LIKE '%103,2,28%';");
                st.executeUpdate("UPDATE `players_bot` SET `fashion` = '[[[103,3,7,1],[108,0,6,1]],[],[[1,1],[5,1],[3,1],[7,1]]]' WHERE `clazz` = 4 AND `fashion` LIKE '%103,3,32%';");
                st.executeUpdate("UPDATE `players_bot` SET `fashion` = '[[[103,4,9,1],[108,0,8,1]],[],[[1,1],[5,1],[3,1],[7,1]]]' WHERE `clazz` = 5 AND `fashion` LIKE '%103,4,36%';");
                
                // Giữ nguyên toàn bộ tài khoản bot trong bảng players_bot để tiếp tục tích lũy tiến độ
                core.Log.info("BotDb", "Completed database migration for bot fashion & name collations.");
            } catch (Exception ex) {
                core.Log.error("BotDb", "Migration error: " + ex.getMessage());
            }
        } catch (Exception e) {
            System.err.println("[BotDb] initBotTable error: " + e.getMessage());
        } finally {
            closeQuietly(null, st, conn);
        }
    }

    // ========================= REGISTER BOT =========================

    /**
     * Lấy tổng số lượng bot hiện có trong bảng players_bot.
     */
    public static int getRegisteredBotCount() {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DbManager.gI().getConnect();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) FROM `players_bot`");
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println("[BotDb] getRegisteredBotCount error: " + e.getMessage());
        } finally {
            closeQuietly(rs, st, conn);
        }
        return 0;
    }

    /**
     * Tạo tài khoản + nhân vật bot trong players_bot nếu chưa tồn tại.
     * Chỉ tạo khi tổng số bot trong DB chưa đạt mức trần MAX_TOTAL_BOTS_IN_DB (25).
     * accountId = 0 (bot không có account thật, nhưng giữ trường để tương thích).
     */
    public static void registerBot(String name, String password, int clazz) {
        if (getIds(name) != null) {
            return; // Already registered!
        }
        if (getRegisteredBotCount() >= BotPlayerManager.MAX_TOTAL_BOTS_IN_DB) {
            return; // Đã đạt số lượng bot tối đa, không tạo thêm để tránh nặng DB
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DbManager.gI().getConnect();

            // 1. Insert minimal row
            String query = "INSERT INTO `players_bot` (`name`, `clazz`) VALUES (?, ?)";
            ps = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setInt(2, clazz);
            ps.executeUpdate();

            // Get generated ID
            int generatedId = 0;
            try (ResultSet rsKey = ps.getGeneratedKeys()) {
                if (rsKey.next()) {
                    generatedId = rsKey.getInt(1);
                }
            }

            // 2. Instantiate a temporary Bot, populate it, and save it!
            bot.Bot tempBot = new bot.Bot(generatedId, name);
            tempBot.IDPlayer = generatedId;
            tempBot.clazz = (byte) clazz;

            // Set default hair, head based on clazz
            short head = 0, hair = 1;
            switch (clazz) {
                case 1: hair = 1; break;
                case 2: hair = 24; break;
                case 3: hair = 28; break;
                case 4: hair = 32; break;
                case 5: hair = 36; break;
                default: hair = 1; tempBot.clazz = 1; break;
            }
            tempBot.head = head;
            tempBot.hair = hair;
            tempBot.is_show_hat = true;
            tempBot.part_body = -1;
            tempBot.part_leg = -1;
            tempBot.part_ring = -1;
            tempBot.part_weapon = -1;

            tempBot.level = 1;
            tempBot.exp = 0;
            tempBot.thongthao = 0;

            tempBot.pointAttribute = 5;
            tempBot.point1 = 1;
            tempBot.point2 = 1;
            tempBot.point3 = 1;
            tempBot.point4 = 1;
            tempBot.point5 = 1;
            tempBot.pointAttributeThongThao = 0;
            tempBot.list_op_thongthao = new java.util.ArrayList<>();

            tempBot.item = new Item(tempBot);
            tempBot.item.it_body = new Item_wear[itemz.Item.MAX_BODY];
            tempBot.item.bag3 = new Item_wear[tempBot.item.max_bag];
            tempBot.item.bag47 = new java.util.ArrayList<>();

            java.util.List<Item_wear> equips = DeTu.getDefaultEquip(clazz);
            for (Item_wear w : equips) {
                if (w != null && w.index >= 0 && w.index < tempBot.item.it_body.length) {
                    tempBot.item.it_body[w.index] = w;
                    if (w.index == 6) {
                        tempBot.item.it_heart = w;
                    }
                }
            }

            tempBot.skill_point = DeTu.getDefaultSkills(clazz);
            tempBot.setDefaultFashion(clazz);
            tempBot.updateParts();
            tempBot.list_eff = new java.util.concurrent.CopyOnWriteArrayList<>();

            // Initial Starting Quest (Quest 0 / 1) matching new player character creation
            tempBot.list_quest = new java.util.ArrayList<>();
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
                    tempBot.list_quest.add(qp);
                }
            } catch (Exception ignored) {}

            tempBot.map = map.Zone.getMapByID(0)[0];
            tempBot.hp = -1;
            tempBot.mp = -1;
            tempBot.x = 300;
            tempBot.y = 300;
            tempBot.is_show_hat = true;
            tempBot.pointPk = 0;
            tempBot.ability = new ability.Ability(tempBot);
            tempBot.hp = tempBot.ability.get_hp_max(true);
            tempBot.mp = tempBot.ability.get_mp_max(true);

            // Save it to DB using canonical flushBotToDb!
            tempBot.flushBotToDb(tempBot, conn, false);
            BotPlayerManager.clearRegisteredBotNamesCache();

            //System.out.println("[BotDb] Registered bot: " + name + " class=" + clazz);
        } catch (Exception e) {
            System.err.println("[BotDb] registerBot error for " + name + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeQuietly(rs, ps, conn);
        }
    }

    // ========================= GET IDs =========================

    /**
     * Trả về [id, 0] của bot trong bảng players_bot.
     * account_id luôn là 0 với bot (không có account thật).
     * Trả về null nếu không tìm thấy.
     */
    public static int[] getIds(String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DbManager.gI().getConnect();
            ps = conn.prepareStatement("SELECT `id` FROM `players_bot` WHERE `name` = ? LIMIT 1;");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new int[]{ rs.getInt("id"), 0 };
            }
        } catch (Exception e) {
            System.err.println("[BotDb] getIds error for " + name + ": " + e.getMessage());
        } finally {
            closeQuietly(rs, ps, conn);
        }
        return null;
    }

    // ========================= UTIL =========================

    private static void closeQuietly(AutoCloseable... closeables) {
        for (AutoCloseable c : closeables) {
            if (c != null) { try { c.close(); } catch (Exception ignored) {} }
        }
    }
}
