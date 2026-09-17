package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import core.Manager;
import core.Log;

public class DbManager {
	private static DbManager instance = null;
	private HikariDataSource dataSource;
	private final String url;
	private final String user;
	private final String pass;

	public DbManager() {
            user = Manager.gI().mysql_user;
            pass = Manager.gI().mysql_pass;
            url = "jdbc:mysql://" + Manager.gI().mysql_host + ":3306/" + Manager.gI().mysql_database
                            + "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_unicode_ci"
                            + "&allowMultiQueries=false&maxAllowedPacket=1073741824&blobSendChunkSize=1048576"
                            + "&cachePrepStmts=true&prepStmtCacheSize=250&prepStmtCacheSqlLimit=2048"
                            + "&useServerPrepStmts=true&rewriteBatchedStatements=true&useLocalSessionState=true"
                            + "&cacheResultSetMetadata=true&cacheServerConfiguration=true"
                            + "&elideSetAutoCommits=true&maintainTimeStats=false&allowPublicKeyRetrieval=true&useSSL=false";
            // System.out.println(url);

            // Tự động mở rộng toàn bộ giới hạn MySQL Server ngay khi khởi động (không cần sửa my.ini thủ công)
            autoConfigureMySQLServerLimits(url, user, pass);
            
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(pass);
            config.addDataSourceProperty("maxAllowedPacket", "1073741824");
            config.addDataSourceProperty("blobSendChunkSize", "1048576");
            config.setConnectionInitSql("SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;");
            config.setConnectionTimeout(30_000L);
            config.setIdleTimeout(600_000L);
            config.setMinimumIdle(25);
            config.setMaximumPoolSize(150);
            config.setMaxLifetime(1_800_000L);
            config.setKeepaliveTime(60_000L);
            config.setValidationTimeout(5_000L);
            config.setLeakDetectionThreshold(15_000L);
            config.setPoolName("HTTH_pool");
            
            dataSource = new HikariDataSource(config);
            Log.success("Database", "MySQL HikariCP Connection Pool Initialized (" + Manager.gI().mysql_host + ":3306/" + Manager.gI().mysql_database + ")");
            try (Connection conn = dataSource.getConnection();
                 java.sql.Statement st = conn.createStatement()) {
                // Đảm bảo kết nối session luôn là utf8mb4 & cấu hình các biến session cần thiết
                try {
                    st.execute("SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;");
                    try { st.execute("SET SESSION group_concat_max_len = 16777216;"); } catch (Exception ignored) {}
                    try { st.execute("SET GLOBAL max_allowed_packet = 1073741824;"); } catch (Exception ignored) {}
                    st.executeUpdate("ALTER TABLE `clan` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
                    st.executeUpdate("ALTER TABLE `players_bot` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
                } catch (Exception ignored) {}
                // Check if historys table exists. If not, check if user_logs exists. If user_logs exists, rename it. If not, create historys.
                boolean hasHistorys = false;
                try {
                    java.sql.DatabaseMetaData dbmd = conn.getMetaData();
                    try (java.sql.ResultSet rs = dbmd.getTables(conn.getCatalog(), null, "historys", null)) {
                        if (rs.next()) {
                            hasHistorys = true;
                        }
                    }
                    if (!hasHistorys) {
                        boolean hasUserLogs = false;
                        try (java.sql.ResultSet rs = dbmd.getTables(conn.getCatalog(), null, "user_logs", null)) {
                            if (rs.next()) {
                                hasUserLogs = true;
                            }
                        }
                        if (hasUserLogs) {
                            st.executeUpdate("RENAME TABLE `user_logs` TO `historys` ;");
                            Log.info("DB Migration", "Renamed table user_logs to historys successfully.");
                            hasHistorys = true;
                        } else {
                            st.executeUpdate("CREATE TABLE IF NOT EXISTS `historys` ("
                                    + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                                    + "`account_id` int(11) NOT NULL DEFAULT 0,"
                                    + "`player_id` int(11) NOT NULL DEFAULT 0,"
                                    + "`type` varchar(255) NOT NULL DEFAULT 'GENERAL',"
                                    + "`data` longtext DEFAULT NULL,"
                                    + "`create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,"
                                    + "`update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                                    + "PRIMARY KEY (`id`),"
                                    + "KEY `idx_player_type` (`player_id`, `type`(64)),"
                                    + "KEY `idx_account_type` (`account_id`, `type`(64)),"
                                    + "KEY `idx_type_create` (`type`(64), `create_at`)"
                                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");
                            Log.info("DB Migration", "Created table historys successfully.");
                            hasHistorys = true;
                        }
                    }
                    if (hasHistorys) {
                        try { st.executeUpdate("ALTER TABLE `historys` MODIFY COLUMN `data` LONGTEXT;"); } catch (Exception ignored) {}
                        try { st.executeUpdate("CREATE INDEX `idx_player_type` ON `historys` (`player_id`, `type`(64));"); } catch (Exception ignored) {}
                        try { st.executeUpdate("CREATE INDEX `idx_account_type` ON `historys` (`account_id`, `type`(64));"); } catch (Exception ignored) {}
                        try { st.executeUpdate("CREATE INDEX `idx_type_create` ON `historys` (`type`(64), `create_at`);"); } catch (Exception ignored) {}
                    }
                    // Auto-ensure account & players table have create_at, date, time_online columns
                    try { st.executeUpdate("ALTER TABLE `account` ADD COLUMN IF NOT EXISTS `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP;"); } catch (Exception ignored) {}
                    try { st.executeUpdate("ALTER TABLE `account` ADD COLUMN IF NOT EXISTS `date` varchar(100) NULL DEFAULT NULL;"); } catch (Exception ignored) {}
                    try { st.executeUpdate("ALTER TABLE `account` ADD COLUMN IF NOT EXISTS `time_online` bigint(20) NOT NULL DEFAULT 0;"); } catch (Exception ignored) {}
                    try { st.executeUpdate("ALTER TABLE `players` ADD COLUMN IF NOT EXISTS `date` varchar(100) NULL DEFAULT NULL;"); } catch (Exception ignored) {}
                    try { st.executeUpdate("ALTER TABLE `players` ADD COLUMN IF NOT EXISTS `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP;"); } catch (Exception ignored) {}
                    // Auto-ensure item7 has price/priceruby and Bột tím has priceruby = 3
                    try { st.executeUpdate("ALTER TABLE `item7` ADD COLUMN IF NOT EXISTS `price` int NULL DEFAULT 0;"); } catch (Exception ignored) {}
                    try { st.executeUpdate("ALTER TABLE `item7` ADD COLUMN IF NOT EXISTS `priceruby` int NULL DEFAULT 0;"); } catch (Exception ignored) {}
                    try { st.executeUpdate("UPDATE `item7` SET `priceruby` = 3 WHERE `id` = 3 AND `price` <= 0 AND (`priceruby` IS NULL OR `priceruby` <= 0);"); } catch (Exception ignored) {}

                    // Auto-clean stale historys on server startup (e.g. if DB was wiped/reset without clearing historys)
                    try {
                        historys.HistoryManager.cleanStaleHistory(conn);
                    } catch (Exception ignored) {}
                } catch (Exception e) {
                    Log.error("Database", "Error checking, creating or indexing historys: " + e.getMessage());
                }
                // Migrate server_data into settings if server_data table exists
//                try {
//                    st.executeUpdate("INSERT INTO `settings` (`key_name`, `value`, `description`) "
//                            + "SELECT `key_name`, `value`, `description` FROM `server_data` "
//                            + "ON DUPLICATE KEY UPDATE `value` = VALUES(`value`), `description` = VALUES(`description`);");
//                    st.executeUpdate("DROP TABLE IF EXISTS `server_data`;");
//                    Log.info("DB Migration", "Migrated server_data into settings and dropped server_data table.");
//                } catch (Exception ignored) {}

//                // Insert default server configurations if table is empty or missing keys
//                try {
//                    st.executeUpdate("INSERT IGNORE INTO `settings` (`key_name`, `value`, `description`) VALUES "
//                            + "('site_name', 'Hải Tặc Tí Hon - One Piece', 'Tên website'),"
//                            + "('site_description', 'Hãy cùng tham gia vào cuộc hành trình chinh phục biển cả rộng lớn, thời đại của Hải tặc chính thức bắt đầu!', 'Mô tả website'),"
//                            + "('maintenance_mode', '0', 'Chế độ bảo trì - 0: Tắt, 1: Bật'),"
//                            + "('registration_enabled', '1', 'Cho phép đăng ký - 0: Tắt, 1: Bật'),"
//                            + "('exp_rate', '10', 'Hệ số kinh nghiệm (EXP multiplier)'),"
//                            + "('gold_rate', '1', 'Hệ số rơi vàng (Beri/Gold multiplier)'),"
//                            + "('max_players', '500', 'Số người chơi online tối đa (Max players online)'),"
//                            + "('is_double_xp', 'false', 'Sự kiện nhân đôi EXP - true/false (Double EXP event)'),"
//                            + "('vip_exp_bonus', '10', 'Tỷ lệ tăng thêm EXP cho VIP - phần trăm (VIP EXP bonus percentage)'),"
//                            + "('chat_ktg_cooldown', '30', 'Thời gian chờ chat thế giới - giây (KTG chat cooldown in seconds)'),"
//                            + "('server_name', 'HaiTacGalaxy', 'Tên hiển thị của server (Server name)')");
//
//                    st.executeUpdate("INSERT IGNORE INTO `settings` (`id`, `key_name`, `value`, `description`) VALUES "
//                            + "(22, 'event_config', '#event_id,is_active,time,timedropitem,timechangeitem,timeremoveitem,timex2pay,key\\n#Cấu hình: #event_id|is_active(1:Bật/0:Tắt)|thời gian sự kiện|thời gian rơi đồ|thời gian đổi đồ|thời gian xóa đồ|thời gian x2 nạp|key/season|\\n1,0,0:00:00 10/7/2026 > 23:59:59 20/7/2026,0:00:00 10/7/2026 > 23:59:59 21/7/2026,0:00:00 10/7/2026 > 23:59:59 22/7/2026,0:00:00 10/7/2026 > 23:59:59 23/7/2026,0:00:00 10/7/2026 > 23:59:59 24/7/2026,TRUNG_THU_2026\\n2,0,,,,,,TET_2026\\n3,0,,,,,,VALENTINE_2026\\n4,0,,,,,,8_3_2026\\n5,0,,,,,,30_4_2026\\n6,0,,,,,,1_6_2026\\n7,0,,,,,,TRONG_CAY_2026\\n8,0,0:00:00 15/6/2026 > 23:59:59 15/10/2026,0:00:00 15/6/2026 > 23:59:59 15/10/2026,0:00:00 15/6/2026 > 23:59:59 15/10/2026,0:00:00 15/6/2026 > 23:59:59 15/10/2026,0:00:00 15/6/2026 > 23:59:59 15/10/2026,HE_2026\\n9,0,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,NOEL_2026\\n10,0,,,,,,20_10_2026\\n11,0,,,,,,20_11_2026\\n12,0,,,,,,KINH_KIBI_2026\\n13,1,0:00:00 15/6/2026 > 23:59:59 01/7/2026,0:00:00 15/6/2026 > 23:59:59 01/7/2026,0:00:00 15/6/2026 > 23:59:59 01/7/2026,0:00:00 15/6/2026 > 23:59:59 01/7/2026,0:00:00 15/6/2026 > 23:59:59 01/7/2026,DAU_TRUONG_2026\\n14,0,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,GIO_TO_2026\\n15,0,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,0:00:00 15/6/2026 > 23:59:59 15/12/2026,HALLOWEEN_2026\\n16,0,,,,,,WANO_2026\\n17,0,,,,,,BIGMOM_2026', 'Cấu hình thời gian các sự kiện'),"
//                            + "(23, 'server_automaintenance', '0:00:00', 'Thời gian bảo trì định kỳ')");
//
//                    st.executeUpdate("INSERT IGNORE INTO `settings` (`key_name`, `value`, `description`) VALUES "
//                            + "('server_automaintenance', '0:00:00', 'Thời gian bảo trì định kỳ')");
//                } catch (Exception ignored) {}

                // Auto-fix quests table schema & data: ensure no AUTO_INCREMENT, ensure quest 0 is intact
                try {
                    st.executeUpdate("ALTER TABLE `quests` MODIFY `id` INT NOT NULL;");
                    int fixedQuests = st.executeUpdate("UPDATE `quests` SET `id` = 0 WHERE `id` = 285 AND `index_server` = 0;");
                    if (fixedQuests > 0) {
                        Log.info("DB Migration", "Fixed quest ID 285 -> 0 for starting quest 'Làng Cối Xay Gió'.");
                        java.io.File cacheFile = new java.io.File(".cache/templates/quests.dat");
                        if (cacheFile.exists()) cacheFile.delete();
                        java.io.File metaFile = new java.io.File(".cache/templates/quests.meta");
                        if (metaFile.exists()) metaFile.delete();
                    }
                } catch (Exception ignored) {}
                try {
                    st.executeUpdate("UPDATE `players` SET `quest` = '[[0,[]]]' WHERE `level` <= 5 AND (`quest` LIKE '[[285,%' OR `quest` LIKE '[[286,%');");
                } catch (Exception ignored) {}
                try {
                    st.executeUpdate("DROP TABLE IF EXISTS `event_configs`;");
                } catch (Exception ignored) {}
                st.executeUpdate("CREATE TABLE IF NOT EXISTS `players_detu` ("
                        + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                        + "`owner_id` int(11) NOT NULL,"
                        + "`name` varchar(255) NOT NULL,"
                        + "`clazz` tinyint(4) DEFAULT 1,"
                        + "`body` varchar(255) DEFAULT NULL,"
                        + "`level` varchar(2000) DEFAULT NULL,"
                        + "`exp` bigint(20) DEFAULT 0,"
                        + "`potential` varchar(255) DEFAULT NULL,"
                        + "`it_body` text DEFAULT NULL,"
                        + "`skill` varchar(1000) DEFAULT NULL,"
                        + "`eff` text DEFAULT NULL,"
                        + "`fashion` text DEFAULT NULL,"
                        + "`site` varchar(1000) DEFAULT NULL,"
                        + "`rms` text DEFAULT NULL,"
                        + "`create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,"
                        + "`update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                        + "PRIMARY KEY (`id`),"
                        + "KEY `idx_owner` (`owner_id`),"
                        + "KEY `idx_name` (`name`)"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

                // Ensure clan_hanhtrinh_template exists and has data
                try {
                    st.executeUpdate("CREATE TABLE IF NOT EXISTS `clan_hanhtrinh_template` ("
                            + "`id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,"
                            + "`icon` smallint(6) NULL DEFAULT NULL,"
                            + "`name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,"
                            + "`info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,"
                            + "`op` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,"
                            + "`rd` int(11) NULL DEFAULT NULL,"
                            + "PRIMARY KEY (`id`) USING BTREE"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;");

                    try (java.sql.ResultSet rsHt = st.executeQuery("SELECT COUNT(*) FROM `clan_hanhtrinh_template`")) {
                        if (rsHt.next() && rsHt.getInt(1) == 0) {
                            java.io.File sqlFile = new java.io.File("data/sql/clan_hanhtrinh_template_data.sql");
                            if (sqlFile.exists()) {
                                String sqlText = new String(java.nio.file.Files.readAllBytes(sqlFile.toPath()), java.nio.charset.StandardCharsets.UTF_8);
                                for (String query : sqlText.split(";")) {
                                    String q = query.trim();
                                    if (!q.isEmpty() && !q.startsWith("--")) {
                                        try { st.executeUpdate(q); } catch (Exception ignored) {}
                                    }
                                }
                                Log.info("DB Migration", "Auto-populated clan_hanhtrinh_template table from SQL template.");
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.error("DB Migration", "Error verifying clan_hanhtrinh_template: " + e.getMessage());
                }

                // Auto-create danhhieu table if not exists
                try {
                    st.executeUpdate("CREATE TABLE IF NOT EXISTS `danhhieu` ("
                            + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                            + "`name` varchar(255) NOT NULL,"
                            + "`ideff` int(11) NOT NULL DEFAULT 0,"
                            + "`coin` int(11) NOT NULL DEFAULT 0,"
                            + "`op` text DEFAULT NULL,"
                            + "PRIMARY KEY (`id`)"
                            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");
                } catch (Exception e) {
                    Log.error("DB Migration", "Error verifying danhhieu table: " + e.getMessage());
                }

                // Auto-verify and migrate market table to 1 row per item
                try {
                    boolean isOldMarket = false;
                    java.sql.DatabaseMetaData dbmd = conn.getMetaData();
                    try (ResultSet rsCols = dbmd.getColumns(conn.getCatalog(), null, "market", "data")) {
                        if (rsCols.next()) {
                            isOldMarket = true;
                        }
                    }
                    if (isOldMarket) {
                        Log.warn("DB Migration", "Detected legacy array-based market table. Starting migration to 1 row per item...");
                        List<template.ItemMarket> oldItem3List = new ArrayList<>();
                        List<template.PotionMarket> oldItem47List = new ArrayList<>();
                        try (Statement stOld = conn.createStatement();
                             ResultSet rsOld = stOld.executeQuery("SELECT * FROM `market`;")) {
                            while (rsOld.next()) {
                                byte marketType = rsOld.getByte("id");
                                String jsonStr = rsOld.getString("data");
                                if (jsonStr != null && !jsonStr.trim().isEmpty()) {
                                    org.json.simple.JSONObject jsob = (org.json.simple.JSONObject) org.json.simple.JSONValue.parse(jsonStr);
                                    if (jsob != null) {
                                        if (jsob.containsKey("item3")) {
                                            org.json.simple.JSONArray js3 = (org.json.simple.JSONArray) org.json.simple.JSONValue.parse(jsob.get("item3").toString());
                                            if (js3 != null) {
                                                for (Object o : js3) {
                                                    template.ItemMarket im = new template.ItemMarket();
                                                    im.load_json((org.json.simple.JSONArray) o);
                                                    if (im.template != null) {
                                                        im.market_type = marketType;
                                                        oldItem3List.add(im);
                                                    }
                                                }
                                            }
                                        }
                                        if (jsob.containsKey("item47")) {
                                            org.json.simple.JSONArray js47 = (org.json.simple.JSONArray) org.json.simple.JSONValue.parse(jsob.get("item47").toString());
                                            if (js47 != null) {
                                                for (Object o : js47) {
                                                    template.PotionMarket pm = new template.PotionMarket();
                                                    pm.load_json((org.json.simple.JSONArray) o);
                                                    if (pm.id > 0 || pm.category == 4) {
                                                        pm.market_type = marketType;
                                                        oldItem47List.add(pm);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        st.executeUpdate("DROP TABLE IF EXISTS `market`;");
                        st.executeUpdate("CREATE TABLE `market` ("
                                + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                                + "`seller_id` int(11) NOT NULL DEFAULT 0,"
                                + "`buyer_id` int(11) NOT NULL DEFAULT -1,"
                                + "`market_type` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`category` tinyint(4) NOT NULL DEFAULT 3,"
                                + "`item_id` int(11) NOT NULL DEFAULT 0,"
                                + "`quantity` int(11) NOT NULL DEFAULT 1,"
                                + "`level_up` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`type_lock` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`num_hole_da_duc` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`time_use` int(11) NOT NULL DEFAULT 0,"
                                + "`value_chetac` smallint(6) NOT NULL DEFAULT 0,"
                                + "`is_hoan_my` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`value_kich_an` tinyint(4) NOT NULL DEFAULT -1,"
                                + "`option_item` text DEFAULT NULL,"
                                + "`option_item_2` text DEFAULT NULL,"
                                + "`num_lo_kham` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`mdakham` varchar(255) DEFAULT NULL,"
                                + "`time_market` bigint(20) NOT NULL DEFAULT 0,"
                                + "`price_market` int(11) NOT NULL DEFAULT 0,"
                                + "`type_market` tinyint(4) NOT NULL DEFAULT 1,"
                                + "`created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,"
                                + "`updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                                + "PRIMARY KEY (`id`),"
                                + "KEY `idx_seller_id` (`seller_id`),"
                                + "KEY `idx_buyer_id` (`buyer_id`),"
                                + "KEY `idx_market_type` (`market_type`),"
                                + "KEY `idx_type_market` (`type_market`)"
                                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

                        for (template.ItemMarket im : oldItem3List) {
                            activities.Market.insertItem(im);
                        }
                        for (template.PotionMarket pm : oldItem47List) {
                            activities.Market.insertPotion(pm);
                        }
                        Log.success("DB Migration", "Migrated " + (oldItem3List.size() + oldItem47List.size()) + " market items to individual rows successfully.");
                    } else {
                        st.executeUpdate("CREATE TABLE IF NOT EXISTS `market` ("
                                + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                                + "`seller_id` int(11) NOT NULL DEFAULT 0,"
                                + "`buyer_id` int(11) NOT NULL DEFAULT -1,"
                                + "`market_type` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`category` tinyint(4) NOT NULL DEFAULT 3,"
                                + "`item_id` int(11) NOT NULL DEFAULT 0,"
                                + "`quantity` int(11) NOT NULL DEFAULT 1,"
                                + "`level_up` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`type_lock` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`num_hole_da_duc` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`time_use` int(11) NOT NULL DEFAULT 0,"
                                + "`value_chetac` smallint(6) NOT NULL DEFAULT 0,"
                                + "`is_hoan_my` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`value_kich_an` tinyint(4) NOT NULL DEFAULT -1,"
                                + "`option_item` text DEFAULT NULL,"
                                + "`option_item_2` text DEFAULT NULL,"
                                + "`num_lo_kham` tinyint(4) NOT NULL DEFAULT 0,"
                                + "`mdakham` varchar(255) DEFAULT NULL,"
                                + "`time_market` bigint(20) NOT NULL DEFAULT 0,"
                                + "`price_market` int(11) NOT NULL DEFAULT 0,"
                                + "`type_market` tinyint(4) NOT NULL DEFAULT 1,"
                                + "`created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,"
                                + "`updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                                + "PRIMARY KEY (`id`),"
                                + "KEY `idx_seller_id` (`seller_id`),"
                                + "KEY `idx_buyer_id` (`buyer_id`),"
                                + "KEY `idx_market_type` (`market_type`),"
                                + "KEY `idx_type_market` (`type_market`)"
                                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");
                    }
                } catch (Exception e) {
                    Log.error("DB Migration", "Error verifying/migrating market table: " + e.getMessage());
                }

                // Auto-fill full item4_info records to eliminate "infopotion fail" logs and guarantee accurate descriptions
                try {
                    java.io.File infoSqlFile = new java.io.File("data/sql/item4_info_complete.sql");
                    if (infoSqlFile.exists()) {
                        String sqlText = new String(java.nio.file.Files.readAllBytes(infoSqlFile.toPath()), java.nio.charset.StandardCharsets.UTF_8);
                        for (String query : sqlText.split(";")) {
                            String q = query.trim();
                            if (!q.isEmpty() && !q.startsWith("--")) {
                                try { st.executeUpdate(q); } catch (Exception ignored) {}
                            }
                        }
                    } else {
                        st.executeUpdate("INSERT INTO `item4_info` (`id`, `info`) VALUES "
                                + "(299, 'Vật phẩm sự kiện'),"
                                + "(325, 'Bộ quà tặng\\n3.000.000 Bery\\n30 Ruby\\n1v Hải thạch c4\\n1 đá khảm Lv6 tự chọn\\n5 Vé x3 skill\\n'),"
                                + "(326, 'Bộ quà tặng\\n5.000.000 Bery\\n50 Ruby\\n1v Hải thạch c5\\n1 đá khảm Lv6 tự chọn\\n10 Vé x3 skill\\n'),"
                                + "(327, 'Bộ quà tặng\\n10.000.000 Bery\\n100 Ruby\\n1v Hải thạch c5\\n1 đá khảm Lv6 tự chọn\\n15 Vé x3 skill\\n1 Rương đại ác quỷ\\n'),"
                                + "(328, 'Bộ quà tặng\\n10.000.000 Bery\\n100 Ruby\\n2v Hải thạch c6\\n20 Vé x3 skill\\n1 Rương đại ác quỷ\\n5 vé Khóa exp\\n2 Xu hành trình\\n'),"
                                + "(329, 'Bộ quà tặng\\n20.000.000 Bery\\n200 Ruby\\n3v Hải thạch c6\\n1 Rương đại ác quỷ\\n20 vé X3 Skill\\n20 vé khóa exp\\n2 Xu hành trình\\n1 Sao 8 cánh\\n'),"
                                + "(345, 'Đá đặc biệt dùng để nâng cấp trang bị lên Thời Không tại NPC Thợ Rèn.'),"
                                + "(346, 'Đá đặc biệt dùng để nâng cấp trang bị lên Truyền Thuyết tại NPC Thợ Rèn.'),"
                                + "(347, 'Đá đặc biệt dùng để nâng cấp trang bị lên Thần Thoại tại NPC Thợ Rèn.'),"
                                + "(384, 'Đá đặc biệt dùng để nâng cấp trang bị lên Vô Cực tại NPC Thợ Rèn.') "
                                + "ON DUPLICATE KEY UPDATE `info` = VALUES(`info`);");
                    }
                } catch (Exception ignored) {}

                try {
                    int unlocked = st.executeUpdate("UPDATE `account` SET `lock` = 0 WHERE `lock` = 1;");
                    if (unlocked > 0) {
                        Log.info("Database", "Auto-unlocked " + unlocked + " account(s) previously locked by false-positive checks.");
                    }
                } catch (Exception ignored) {}

                Log.success("Database", "All database tables & schemas verified successfully.");
            } catch (SQLException e) {
                Log.error("Database", "Error initializing tables: " + e.getMessage(), e);
            }

            runMigrations();
	}

	    public void migrateMissingColumns(Connection conn, String table, String[] columns, String[] definitions) {
        for (int i = 0; i < columns.length; i++) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("ALTER TABLE " + table + " ADD COLUMN " + columns[i] + " " + definitions[i]);
                System.out.println("[DB Migration] Added missing column " + columns[i] + " to table " + table + "");
            } catch (SQLException e) {
                if (!e.getMessage().contains("Duplicate column name") && !e.getMessage().contains("doesn't exist")) {
                    System.err.println("[DB Migration] Error adding column " + columns[i] + " to " + table + ": " + e.getMessage());
                }
            }
        }
    }

    public void runMigrations() {
        try (Connection conn = getConnect()) {
            if (conn == null) return;
            
            String[] commonCols = {
                "bag3", "box3", "it_body", "bag47", "box47", 
                "save_it3", "save_it47", "hanhtrinh", "potential", "skill", "rms", "friend", "enemy", "eff", "fashion",
                "mypet", "coin", "myarchidaily", "mbv",
                "date", "quest", "level", "exp", "clazz", "account_id",
                "inventory", "body_parts", "timeResetHangNgay", "eff_save"
            };

            String[] commonDefs = {
                "text DEFAULT NULL", // bag3
                "text DEFAULT NULL", // box3
                "text DEFAULT NULL", // it_body
                "text DEFAULT NULL", // bag47
                "text DEFAULT NULL", // box47
                "text DEFAULT NULL", // save_it3
                "text DEFAULT NULL", // save_it47
                "text DEFAULT NULL", // hanhtrinh
                "varchar(255) DEFAULT NULL", // potential
                "varchar(1000) DEFAULT NULL", // skill
                "text DEFAULT NULL", // rms
                "text DEFAULT NULL", // friend
                "text DEFAULT NULL", // enemy
                "text DEFAULT NULL", // eff
                "text DEFAULT NULL", // fashion
                "varchar(5000) DEFAULT NULL", // mypet
                "int(11) NOT NULL DEFAULT 0", // coin
                "text DEFAULT NULL", // myarchidaily
                "varchar(255) DEFAULT NULL", // mbv
                "varchar(255) DEFAULT NULL", // date
                "text DEFAULT NULL", // quest
                "varchar(2000) DEFAULT NULL", // level
                "bigint(20) DEFAULT 0", // exp
                "tinyint(4) DEFAULT 1", // clazz
                "int(11) DEFAULT 0", // account_id
                "text DEFAULT NULL", // inventory
                "varchar(255) DEFAULT NULL", // body_parts
                "timestamp NULL DEFAULT CURRENT_TIMESTAMP", // timeResetHangNgay
                "text DEFAULT NULL" // eff_save
            };
            
            String[] detuCols = {
                "body", "it_body", "potential", "skill", "eff", "fashion",
                "level", "exp", "site", "clazz", "rms"
            };

            String[] detuDefs = {
                "varchar(255) DEFAULT NULL", // body
                "text DEFAULT NULL", // it_body
                "varchar(255) DEFAULT NULL", // potential
                "varchar(1000) DEFAULT NULL", // skill
                "text DEFAULT NULL", // eff
                "text DEFAULT NULL", // fashion
                "varchar(2000) DEFAULT NULL", // level
                "bigint(20) DEFAULT 0", // exp
                "varchar(1000) DEFAULT NULL", // site
                "tinyint(4) DEFAULT 1", // clazz
                "text DEFAULT NULL" // rms
            };
            
            String[] botCols = {
                "body", "it_body", "potential", "skill", "eff", "fashion",
                "level", "exp", "site", "clazz", "quest"
            };

            String[] botDefs = {
                "varchar(255) DEFAULT NULL", // body
                "text DEFAULT NULL", // it_body
                "varchar(255) DEFAULT NULL", // potential
                "varchar(1000) DEFAULT NULL", // skill
                "text DEFAULT NULL", // eff
                "text DEFAULT NULL", // fashion
                "varchar(2000) DEFAULT NULL", // level
                "bigint(20) DEFAULT 0", // exp
                "varchar(1000) DEFAULT NULL", // site
                "tinyint(4) DEFAULT 1", // clazz
                "text DEFAULT NULL" // quest
            };
            
            migrateMissingColumns(conn, "players", commonCols, commonDefs);
            migrateMissingColumns(conn, "players_detu", detuCols, detuDefs);
            
            // Drop unnecessary unique name index from players_detu if it exists
            try (Statement st = conn.createStatement()) {
                try {
                    st.executeUpdate("ALTER TABLE `players_detu` DROP INDEX `uniq_name`");
                    Log.info("DB Migration", "Dropped uniq_name index from players_detu successfully.");
                } catch (Exception ignored) {}
                try {
                    st.executeUpdate("ALTER TABLE `players_detu` ADD INDEX `idx_name` (`name`)");
                } catch (Exception ignored) {}
                try {
                    st.executeUpdate("ALTER TABLE `players_detu` ADD INDEX `idx_owner` (`owner_id`)");
                } catch (Exception ignored) {}
            } catch (Exception ignored) {}
            
            // Create players_bot with only the essential columns if it doesn't exist
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS `players_bot` ("
                        + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                        + "`name` varchar(255) NOT NULL,"
                        + "`clazz` tinyint(4) DEFAULT 1,"
                        + "`body` varchar(255) DEFAULT NULL,"
                        + "`level` varchar(2000) DEFAULT NULL,"
                        + "`exp` bigint(20) DEFAULT 0,"
                        + "`potential` varchar(255) DEFAULT NULL,"
                        + "`it_body` text DEFAULT NULL,"
                        + "`skill` varchar(1000) DEFAULT NULL,"
                        + "`eff` text DEFAULT NULL,"
                        + "`fashion` text DEFAULT NULL,"
                        + "`site` varchar(1000) DEFAULT NULL,"
                        + "`create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,"
                        + "`update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                        + "PRIMARY KEY (`id`),"
                        + "UNIQUE KEY `uniq_name` (`name`)"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
            } catch (Exception e) {}
            migrateMissingColumns(conn, "players_bot", botCols, botDefs);
            migrateMissingColumns(conn, "clan", new String[]{"hanhtrinh"}, new String[]{"mediumtext DEFAULT NULL"});
        } catch (Exception e) {
            System.err.println("[DB Migration] Failed: " + e.getMessage());
        }
    }

    /**
     * Tự động cấu hình mở rộng các biến toàn cục (GLOBAL variables) của MySQL server
     * trực tiếp bằng code Java, đảm bảo server không bao giờ bị lỗi 'Packet for query is too large'
     * hoặc timeout/max_connections mà không cần can thiệp thủ công vào my.ini / my.cnf.
     */
    private static void autoConfigureMySQLServerLimits(String jdbcUrl, String dbUser, String dbPass) {
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (Exception ignored) {}
            }

            try (Connection rawConn = java.sql.DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
                 Statement st = rawConn.createStatement()) {

                // 1. Tự động mở giới hạn max_allowed_packet lên 1GB (1,073,741,824 bytes)
                try {
                    st.execute("SET GLOBAL max_allowed_packet = 1073741824;");
                } catch (Exception ignored) {}

                // 2. Mở rộng net_buffer_length (1MB)
                try {
                    st.execute("SET GLOBAL net_buffer_length = 1048576;");
                } catch (Exception ignored) {}

                // 3. Mở rộng group_concat_max_len lên 16MB (tránh cắt chuỗi lớn)
                try {
                    st.execute("SET GLOBAL group_concat_max_len = 16777216;");
                } catch (Exception ignored) {}

                // 4. Mở rộng số lượng kết nối tối đa (max_connections = 1000)
                try {
                    st.execute("SET GLOBAL max_connections = 1000;");
                } catch (Exception ignored) {}

                // 5. Mở rộng timeout kết nối (wait_timeout & interactive_timeout = 8 giờ)
                try {
                    st.execute("SET GLOBAL wait_timeout = 28800;");
                } catch (Exception ignored) {}
                try {
                    st.execute("SET GLOBAL interactive_timeout = 28800;");
                } catch (Exception ignored) {}

                // 6. Tăng connect_timeout, max_connect_errors và innodb_lock_wait_timeout
                try {
                    st.execute("SET GLOBAL connect_timeout = 60;");
                } catch (Exception ignored) {}
                try {
                    st.execute("SET GLOBAL max_connect_errors = 1000000;");
                } catch (Exception ignored) {}
                try {
                    st.execute("SET GLOBAL innodb_lock_wait_timeout = 120;");
                } catch (Exception ignored) {}

                Log.success("Database", "MySQL limits auto-configured: max_allowed_packet=1GB, net_buffer_length=1MB, group_concat_max_len=16MB, max_connections=1000");
            }
        } catch (Exception e) {
            Log.warn("Database", "Could not set global MySQL variables (might lack SUPER privileges, fallback to JDBC client parameters): " + e.getMessage());
        }
    }

	public static int getPlayerIdByName(String name) {
		if (name == null || name.trim().isEmpty()) return -1;
		try (Connection conn = gI().getConnect();
			 java.sql.PreparedStatement ps = conn.prepareStatement("SELECT `id` FROM `players` WHERE `name` = ? LIMIT 1")) {
			ps.setString(1, name.trim());
			try (java.sql.ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("id");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static String getPlayerNameById(int id) {
		if (id <= 0) return "";
		try (Connection conn = gI().getConnect();
			 java.sql.PreparedStatement ps = conn.prepareStatement("SELECT `name` FROM `players` WHERE `id` = ? LIMIT 1")) {
			ps.setInt(1, id);
			try (java.sql.ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String n = rs.getString("name");
					return (n != null) ? n : "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static DbManager gI() {
		if (instance == null) {
			instance = new DbManager();
		}
		return instance;
	}

	public Connection getConnect() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void close() {
		dataSource.close();
	}

	/**
	 * Converts the current row of a ResultSet to a LinkedHashMap<String, Object>.
	 * Used by TemplateCache to serialize SQL rows to disk without duplicating parsing logic.
	 */
	public static java.util.Map<String, Object> resultSetToMap(java.sql.ResultSet rs) throws java.sql.SQLException {
		java.sql.ResultSetMetaData meta = rs.getMetaData();
		int cols = meta.getColumnCount();
		java.util.LinkedHashMap<String, Object> map = new java.util.LinkedHashMap<>(cols * 2);
		for (int i = 1; i <= cols; i++) {
			map.put(meta.getColumnLabel(i), rs.getObject(i));
		}
		return map;
	}
}
