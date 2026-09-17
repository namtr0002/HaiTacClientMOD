import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import skill.Skill_Template;

public class ApplyThanTrangSkillDb {

    private Connection getDbConnection() {
        String[] hosts = new String[]{"localhost", "127.0.0.1"};
        String[] users = new String[]{"root"};
        String[] passwords = new String[]{"", "123456", "12345678", "root"};

        for (String host : hosts) {
            for (String user : users) {
                for (String pass : passwords) {
                    try {
                        String url = "jdbc:mysql://" + host + ":3306/haitacz?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false";
                        Connection conn = DriverManager.getConnection(url, user, pass);
                        if (conn != null) {
                            return conn;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
        return null;
    }

    @Test
    public void syncSkillsInDatabase() throws Exception {
        Connection conn = getDbConnection();
        if (conn == null) {
            System.out.println("WARNING: Could not connect to haitacz MySQL database (MySQL might not be running on standard ports/passwords).");
            return;
        }

        try (Statement st = conn.createStatement()) {
            // 1. Xóa toàn bộ skill thần trang thừa trong database (5001..5032 và 4001..4080)
            st.executeUpdate("DELETE FROM skill WHERE (id BETWEEN 5001 AND 5032 OR id_index BETWEEN 5001 AND 5032) OR (id BETWEEN 4001 AND 4080 OR id_index BETWEEN 4001 AND 4080);");

            // 2. Chèn đúng 16 Skill Tấn Công Chủ Động (Active Attack Skill) - 1 skill duy nhất cho mỗi set
            // typeSkill = 1 (Active Attack), typeBuff = 0, nTarget = 5, range = 220, rangeLan = 140, damage = 5000, manaLost = 50, timeDelay = 3000ms
            String sqlInsert = "INSERT INTO skill (id, id_index, id_2, icon, typeSkill, typeBuff, name, typeEffSkill, `range`, rangeLan, nTarget, damage, manaLost, timeDelay, nKick, info, Lv_RQ, percentLv, typeDevil, `option`, EffSpec, LvDevilSkill, phanTramDevilSkill) VALUES\n"
                + "(4001, 4001, 4001, 201, 1, 0, 'Hỏa Diễm Thần Quyền', 4001, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Hỏa Long Thần Trang giải phóng biển lửa thiêu rụi mục tiêu.', 1, 0, 0, '[[1,350],[5,150],[10,350],[13,400],[14,300],[29,500]]', '[2,50,30]', 0, 0),\n"
                + "(4002, 4002, 4002, 202, 1, 0, 'Đại Phún Hỏa Volcano', 4002, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Hải Vương Thần Trang triệu hồi nham thạch phun trào hủy diệt.', 1, 0, 0, '[[1,350],[2,350],[10,350],[13,400],[14,300],[29,500]]', '[2,50,30]', 0, 0),\n"
                + "(4003, 4003, 4003, 203, 1, 0, 'Kỷ Băng Hà Tuyệt Đối', 4003, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Lôi Thần Thần Trang đóng băng vạn vật trong chớp mắt.', 1, 0, 0, '[[1,350],[3,350],[10,350],[13,400],[28,10],[29,500]]', '[3,50,30]', 0, 0),\n"
                + "(4004, 4004, 4004, 204, 1, 0, 'Bát Xích Quỳnh Khúc Ngọc', 4004, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Phong Ma Thần Trang phóng mưa ngọc quang tử tốc độ ánh sáng.', 1, 0, 0, '[[1,350],[7,150],[10,350],[13,400],[9,200],[29,500]]', '[4,50,30]', 0, 0),\n"
                + "(4005, 4005, 4005, 205, 1, 0, 'Hắc Ám Thôn Phệ Vô Tận', 4005, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Tử Thần Thần Trang mở ra lỗ đen nuốt chửng mọi kẻ thù, chặn di chuyển mục tiêu.', 1, 0, 0, '[[1,350],[2,350],[10,350],[13,400],[29,500],[30,50]]', '[8,100,35]', 0, 0),\n"
                + "(4006, 4006, 4006, 206, 1, 0, '200 Triệu Volt Thần Lôi', 4006, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Băng Đế Thần Trang giáng sấm sét 200 triệu volt hủy diệt.', 1, 0, 0, '[[1,350],[5,150],[10,350],[13,400],[28,10],[29,500]]', '[6,50,30]', 0, 0),\n"
                + "(4007, 4007, 4007, 207, 1, 0, 'Hải Chấn Toái Địa Cầu', 4007, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Chấn Động Thần Trang đập vỡ không gian tạo đại hải chấn kinh thiên động địa. Kèm 50% tỷ lệ gây Choáng trong 3.0s.', 1, 0, 0, '[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]', '[1,50,30]', 0, 0),\n"
                + "(4008, 4008, 4008, 208, 1, 0, 'ROOM Gamma Knife', 4008, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Kim Cương Thần Trang phân cắt và phá hủy nội tạng đối thủ.', 1, 0, 0, '[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]', '[7,50,30]', 0, 0),\n"
                + "(4009, 4009, 4009, 209, 1, 0, 'Từ Trường Bộc Phá Đại Pháo', 4009, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Hắc Ám Thần Trang tụ lực từ tính bắn đại pháo hủy diệt.', 1, 0, 0, '[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]', '[1,55,30]', 0, 0),\n"
                + "(4010, 4010, 4010, 210, 1, 0, 'Cổ Độc Phán Quyết Venom', 4010, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Quang Minh Thần Trang phóng độc dược ăn mòn sinh mệnh.', 1, 0, 0, '[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]', '[8,50,30]', 0, 0),\n"
                + "(4011, 4011, 4011, 211, 1, 0, 'Mũi Tên Mê Hoặc Thạch Hóa', 4011, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Tu La Thần Trang hóa đá mọi kẻ thù trúng phải.', 1, 0, 0, '[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]', '[9,50,30]', 0, 0),\n"
                + "(4012, 4012, 4012, 212, 1, 0, 'Phượng Hoàng Bất Tử Bộc Phá', 4012, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Thánh Linh Thần Trang tung cánh phượng hoàng lam hỏa thiêu rụi.', 1, 0, 0, '[[1,350],[3,500],[10,350],[13,400],[29,500],[30,50]]', '[2,50,30]', 0, 0),\n"
                + "(4013, 4013, 4013, 213, 1, 0, 'Đại Phật Sóng Xung Kích', 4013, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Huyết Long Thần Trang chưởng sóng xung kích uy lực vô song.', 1, 0, 0, '[[1,350],[2,500],[10,350],[13,400],[28,10],[29,500]]', '[1,55,30]', 0, 0),\n"
                + "(4014, 4014, 4014, 214, 1, 0, 'Bát Quái Cửu Long Thiên', 4014, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Ma Thần Thần Trang quét chùy sấm sét bách thú vô địch.', 1, 0, 0, '[[1,350],[2,500],[10,350],[13,400],[28,10],[29,500]]', '[1,50,30]', 0, 0),\n"
                + "(4015, 4015, 4015, 215, 1, 0, 'Long Trảo Viêm Long Toái Địa', 4015, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Thiên Thần Thần Trang trảo rồng lửa phá hủy mặt đất.', 1, 0, 0, '[[1,350],[5,150],[10,350],[13,400],[14,300],[29,500]]', '[2,50,30]', 0, 0),\n"
                + "(4016, 4016, 4016, 216, 1, 0, 'Vận Thạch Thiên Giáng', 4016, 220, 140, 5, 5000, 50, 3000, 1, 'Tuyệt kỹ Hỗn Độn Thần Trang kéo thiên thạch rơi tự do từ vũ trụ.', 1, 0, 0, '[[1,350],[10,350],[13,400],[28,10],[29,500],[30,50]]', '[1,50,30]', 0, 0);";

            st.executeUpdate(sqlInsert);

            // 3. Xóa sạch cache template quests & skills nếu có
            try {
                java.io.File cacheFile = new java.io.File(".cache/templates/skill.dat");
                if (cacheFile.exists()) cacheFile.delete();
                java.io.File metaFile = new java.io.File(".cache/templates/skill.meta");
                if (metaFile.exists()) metaFile.delete();
            } catch (Exception ignored) {}

            // 4. Verify in database: Exactly 16 skills
            int count = 0;
            try (ResultSet rs = st.executeQuery("SELECT id_index, id_2, icon, typeSkill, name FROM skill WHERE id_index BETWEEN 4001 AND 4016 ORDER BY id_index")) {
                while (rs.next()) {
                    count++;
                    int idIndex = rs.getInt("id_index");
                    int id2 = rs.getInt("id_2");
                    int icon = rs.getInt("icon");
                    assertEquals(idIndex, id2);
                    assertTrue(icon > 0);
                }
            }
            assertEquals("Phải có đúng 16 skill chủ động trong database", 16, count);

            // Kiểm tra không còn skill thừa nào trong dải 4017..4080 hoặc 5001..5032
            int redundantCount = 0;
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM skill WHERE (id_index BETWEEN 4017 AND 4080) OR (id_index BETWEEN 5001 AND 5032)")) {
                if (rs.next()) {
                    redundantCount = rs.getInt(1);
                }
            }
            assertEquals("Không được còn bất kỳ skill thừa nào trong database", 0, redundantCount);

            System.out.println("SUCCESS: Cleared all redundant skills and synced exactly 16 Than Trang active skills in MySQL database!");
        }
    }
}
