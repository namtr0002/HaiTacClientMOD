import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

import model.Player;
import skill.Skill_Template;
import skill.Skill_info;

public class TestSkillDamageCalculation {

    @Test
    public void testExtractDamagePercent() {
        // 1. Trái Tình Yêu
        String mero1 = "Bắn ra trái tim hồng rực sáng khiến kẻ địch mê muội hóa đá, xuyên thấu phòng ngự và hút sinh lực đối phương. Kèm 40% tỷ lệ gây Hoa mắt trong 3.0s. 600% sát thương của chiêu thức Quả đấm tốc độ";
        assertEquals("Trái tim tử thần phải có 600% sát thương", 600, Skill_info.extractDamagePercent(mero1, 1550));

        String mero2 = "Tung hàng loạt mũi tên trái tim gây sát thương diện rộng lên 4 mục tiêu, bạo kích cực mạnh và làm suy yếu sức tấn công của kẻ địch. Kèm 45% tỷ lệ gây Giảm công trong 3.5s. 900% sát thương của chiêu thức Quả đấm tốc độ";
        assertEquals("Tâm tiễn ái tình phải có 900% sát thương", 900, Skill_info.extractDamagePercent(mero2, 2325));

        // 2. Trái Ánh Sáng
        String pika1 = "Tập trung chùm sáng nguyên tử bắn xuyên phá mục tiêu với vận tốc ánh sáng, gây sát thương chuẩn cực lớn. Kèm 40% tỷ lệ gây Hoa mắt trong 3.5s. 650% sát thương của chiêu thức Quả đấm tốc độ";
        assertEquals("Cực quang phong ấn phải có 650% sát thương", 650, Skill_info.extractDamagePercent(pika1, 1550));

        String pika2 = "Giải phóng hàng vạn tia sáng hủy diệt trút xuống 4 mục tiêu diện rộng, tạo ra những đòn chí mạng long trời lở đất. Kèm 50% tỷ lệ gây Điện giật trong 3.5s. 950% sát thương của chiêu thức Quả đấm tốc độ";
        assertEquals("Thiên quang bất diệt phải có 950% sát thương", 950, Skill_info.extractDamagePercent(pika2, 2325));

        // 3. Trái Nika
        String nika1 = "Hóa khổng lồ nắm đấm Thần Mặt Trời giáng thẳng xuống kẻ địch, nghiền nát mọi phòng ngự và hút sinh lực. Kèm 40% tỷ lệ gây Choáng trong 3.0s. 700% sát thương của chiêu thức Quả đấm tốc độ";
        assertEquals("Nắm đấm của Thần phải có 700% sát thương", 700, Skill_info.extractDamagePercent(nika1, 1550));

        String nika2 = "Cầm tia sét khổng lồ phóng như mũi lao thần thánh vào 4 mục tiêu, bạo kích hủy diệt và làm tê liệt kẻ thù. Kèm 50% tỷ lệ gây Điện giật trong 4.0s. 1000% sát thương của chiêu thức Quả đấm tốc độ";
        assertEquals("Số 4: Snakeman phải có 1000% sát thương", 1000, Skill_info.extractDamagePercent(nika2, 2325));

        // 4. Chim Ưng & Báo Đốm
        String ung = "Tung ra đòn đánh hình cơn lốc lướt qua đối thủ nếu ở dạng chim ưng sẽ công kích đối thủ từ trên không và gây thêm 15% sát thương. 550% sát thương của chiêu thức Quả đấm tốc độ";
        assertEquals("Cơn lốc - Ưng kích phải có 550% sát thương", 550, Skill_info.extractDamagePercent(ung, 3025));

        String bao = "Vận dụng sức mạnh chưa được khám phá tạo ra luồng sóng cực lớn đánh bật kẻ địch nếu ở dạng báo sẽ tăng 15% sát thương vào đối phương. 600% sát thương của chiêu thức Quả đấm tốc độ";
        assertEquals("Sóng âm - Xung kích phải có 600% sát thương", 600, Skill_info.extractDamagePercent(bao, 3600));

        // 5. Trái Lửa, Khói, Cát, Băng, Sét, Nham Thạch
        String lua1 = "Hàng loạt đòn đánh lửa cực mạnh từ trên trời giáng xuống như một trận bão. 650% sát thương của chiêu thức Quả đấm tốc độ";
        assertEquals("Nắm đấm lửa phải có 650% sát thương", 650, Skill_info.extractDamagePercent(lua1, 4225));

        String lua2 = "Giải phóng nguồn năng lượng lửa khổng lồ của bản thân quét sạch mọi thứ trên đường đi. 300% sát thương của chiêu thức Quả đấm tốc độ";
        assertEquals("Hỏa quyền phải có 300% sát thương", 300, Skill_info.extractDamagePercent(lua2, 1950));

        String diaChan1 = "Vận dụng sức mạnh của thiên nhiên tấn công đối phương từ các đợt khí bốc lên từ lòng đất. 300% sát thương của chiêu Quả đấm tốc độ";
        assertEquals("Vết nứt phải có 300% sát thương", 300, Skill_info.extractDamagePercent(diaChan1, 75));
    }

    @Test
    public void testSkillTemplateGetInfo() {
        Skill_Template st = new Skill_Template(799, 5015, (short) 101, (byte) 1, (byte) 0, "Trái tim tử thần", (short) 408, (short) 120);
        st.getData((byte) 1, (short) 120, 1550, (short) 87, 16000, (byte) 1,
            "Bắn ra trái tim hồng rực sáng khiến kẻ địch mê muội hóa đá, xuyên thấu phòng ngự và hút sinh lực đối phương. Kèm 40% tỷ lệ gây Hoa mắt trong 3.0s. 600% sát thương của chiêu thức Quả đấm tốc độ",
            (byte) 1, (byte) 1);

        // Level 0 (chưa nâng cấp)
        String infoLv0 = st.getInfo((byte) 0, 1);
        assertTrue("Info Lv 0 phải giữ nguyên 40% hoa mắt", infoLv0.contains("40% tỷ lệ"));
        assertTrue("Info Lv 0 phải giữ nguyên 600% sát thương", infoLv0.contains("600% sát thương"));

        // Level 5 (cường hóa max cấp ác quỷ x2)
        String infoLv5 = st.getInfo((byte) 5, 1);
        assertTrue("Info Lv 5 vẫn giữ nguyên 40% hoa mắt không bị sửa nhầm", infoLv5.contains("40% tỷ lệ"));
        assertTrue("Info Lv 5 phải nâng cấp 600% thành 1200% sát thương", infoLv5.contains("1200% sát thương"));
    }

    @Test
    public void testGetDameCalculation() {
        Player p = new Player();
        p.skill_point = new ArrayList<>();

        // Skill 1 môn phái: Quả đấm tốc độ Lv 1 (dame = 25)
        Skill_Template st0 = new Skill_Template(0, 0, (short) 1, (byte) 1, (byte) 0, "Quả đấm tốc độ", (short) 1, (short) 100);
        st0.getData((byte) 1, (short) 100, 25, (short) 0, 0, (byte) 1, "Kỹ năng tấn công cơ bản", (byte) 1, (byte) 0);
        Skill_info sk0 = new Skill_info();
        sk0.temp = st0;
        p.skill_point.add(sk0);

        assertEquals("Đấm thường Lv 1 phải có dame = 25", 25, sk0.get_dame(p));

        // Skill Trái Ác Quỷ Tình Yêu (600% sát thương)
        Skill_Template stMero = new Skill_Template(799, 5015, (short) 101, (byte) 1, (byte) 0, "Trái tim tử thần", (short) 408, (short) 120);
        stMero.getData((byte) 1, (short) 120, 1550, (short) 87, 16000, (byte) 1,
            "Bắn ra trái tim hồng rực sáng khiến kẻ địch mê muội hóa đá, xuyên thấu phòng ngự và hút sinh lực đối phương. Kèm 40% tỷ lệ gây Hoa mắt trong 3.0s. 600% sát thương của chiêu thức Quả đấm tốc độ",
            (byte) 1, (byte) 1);
        Skill_info skMero = new Skill_info();
        skMero.temp = stMero;

        // Cấp quỷ 0: 600% của 25 = 150
        skMero.lvdevil = 0;
        assertEquals("Trái tim tử thần Cấp Quỷ 0 phải có dame = 150 (6.0x)", 150, skMero.get_dame(p));

        // Cấp quỷ 5 (x2): 1200% của 25 = 300
        skMero.lvdevil = 5;
        assertEquals("Trái tim tử thần Cấp Quỷ 5 phải có dame = 300 (12.0x)", 300, skMero.get_dame(p));

        // Giả sử đấm thường lên cấp 20 (dame = 295)
        st0.damage = 295;
        skMero.lvdevil = 0;
        assertEquals("Với đấm thường Lv 20 (295), Trái tim tử thần Cấp Quỷ 0 phải là 1770 (6.0x)", 1770, skMero.get_dame(p));
        skMero.lvdevil = 5;
        assertEquals("Với đấm thường Lv 20 (295), Trái tim tử thần Cấp Quỷ 5 phải là 3540 (12.0x)", 3540, skMero.get_dame(p));

        // Skill Tuyệt Kỹ Thần Trang (4001): 5000% sát thương
        Skill_Template stThanTrang = new Skill_Template(4001, 4001, (short) 201, (byte) 1, (byte) 0, "Hỏa Diệm Thần Quyền", (short) 4001, (short) 220);
        stThanTrang.getData((byte) 5, (short) 140, 5000, (short) 50, 3000, (byte) 1,
            "Tuyệt kỹ Hỏa Long Thần Trang giải phóng biển lửa thiêu rụi mục tiêu.", (byte) 1, (byte) 0);
        Skill_info skThanTrang = new Skill_info();
        skThanTrang.temp = stThanTrang;

        // Với đấm thường Lv 20 (295), 5000% = 50x = 14750
        assertEquals("Thần Trang với đấm thường 295 phải có dame = 14750 (50.0x)", 14750, skThanTrang.get_dame(p));
    }
}
