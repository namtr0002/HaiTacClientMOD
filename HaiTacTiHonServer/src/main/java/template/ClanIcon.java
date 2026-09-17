package template;

import clan.Clan;
import java.util.ArrayList;
import java.util.List;

public class ClanIcon {

    public static List<ClanIcon> ENTRY = new ArrayList<>();

    public int id;
    public String name = "";
    public String info = "";
    public int price;
    public byte type;
    public boolean sell = true;

    public ClanIcon() {
    }

    public ClanIcon(int id, String name, String info, int price, byte type, boolean sell) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.price = price;
        this.type = type;
        this.sell = sell;
    }

    public static ClanIcon GetById(short id) {
        if (ENTRY == null || ENTRY.isEmpty()) {
            initDefaultEntries();
        }
        for (int i = 0; i < ENTRY.size(); i++) {
            ClanIcon get = ENTRY.get(i);
            if (get != null && get.id == id && get.sell) {
                return get;
            }
        }
        return null;
    }

    public static ClanIcon getIconById(int id) {
        if (ENTRY == null || ENTRY.isEmpty()) {
            initDefaultEntries();
        }
        for (int i = 0; i < ENTRY.size(); i++) {
            ClanIcon get = ENTRY.get(i);
            if (get != null && get.id == id) {
                return get;
            }
        }
        return null;
    }

    public static List<ClanIcon> getIconThuong() {
        if (ENTRY == null || ENTRY.isEmpty()) {
            initDefaultEntries();
        }
        List<ClanIcon> result = new ArrayList<>();
        for (int i = 0; i < ENTRY.size(); i++) {
            ClanIcon get = ENTRY.get(i);
            if (get != null && get.sell && get.type == 0) {
                result.add(get);
            }
        }
        return result;
    }

    public static List<ClanIcon> getIconVip() {
        if (ENTRY == null || ENTRY.isEmpty()) {
            initDefaultEntries();
        }
        List<ClanIcon> result = new ArrayList<>();
        for (int i = 0; i < ENTRY.size(); i++) {
            ClanIcon get = ENTRY.get(i);
            if (get != null && get.sell && get.type == 1) {
                result.add(get);
            }
        }
        return result;
    }

    public static void initDefaultEntries() {
        if (ENTRY == null) {
            ENTRY = new ArrayList<>();
        }
        if (!ENTRY.isEmpty()) {
            return;
        }

        // ==========================================
        // 1. BIỂU TƯỢNG THƯỜNG (TYPE = 0)
        // ==========================================
        // Cờ mặc định tân thủ (0 - 9)
        ENTRY.add(new ClanIcon(0, "Cờ Mặc Định 1", "Cờ hải tặc đầu lâu cơ bản", 0, (byte) 0, true));
        ENTRY.add(new ClanIcon(1, "Cờ Mặc Định 2", "Cờ hải tặc xương chéo", 0, (byte) 0, true));
        ENTRY.add(new ClanIcon(2, "Cờ Mặc Định 3", "Cờ sọc đỏ chiến hạm", 0, (byte) 0, true));
        ENTRY.add(new ClanIcon(3, "Cờ Mặc Định 4", "Cờ hải quân biển xanh", 0, (byte) 0, true));
        ENTRY.add(new ClanIcon(4, "Cờ Mặc Định 5", "Cờ hoàng kim hải tặc", 0, (byte) 0, true));
        ENTRY.add(new ClanIcon(5, "Cờ Mặc Định 6", "Cờ tím bí ẩn đại dương", 0, (byte) 0, true));
        ENTRY.add(new ClanIcon(6, "Cờ Mặc Định 7", "Cờ kiếm sĩ bến cảng", 0, (byte) 0, true));
        ENTRY.add(new ClanIcon(7, "Cờ Mặc Định 8", "Cờ hoa tiêu hải trình", 0, (byte) 0, true));
        ENTRY.add(new ClanIcon(8, "Cờ Mặc Định 9", "Cờ pháo thủ hạm đội", 0, (byte) 0, true));
        ENTRY.add(new ClanIcon(9, "Cờ Mặc Định 10", "Cờ bóng đêm hải tặc", 0, (byte) 0, true));

        // Biểu tượng thường kinh điển
        ENTRY.add(new ClanIcon(10, "Huy Hiệu Xương Cá Tầm", "Được làm từ Xương Cá Tầm, loại này chỉ xuất hiện 2 lần trong năm ở Đảo Vỏ Sò", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(30, "Huy Hiệu Mũ Rơm Cổ Điển", "Biểu tượng băng hải tặc Mũ Rơm", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(35, "Huy Hiệu Brock Vui Vẻ", "Biểu tượng cho sự bất tử và âm nhạc", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(39, "Huy Hiệu Mặt Trời Ngư Tộc", "Biểu tượng băng hải tặc Mặt Trời", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(44, "Huy Hiệu Zoro Kiếm Sĩ", "Biểu tượng của đệ nhất kiếm sĩ tương lai", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(51, "Huy Hiệu Râu Trắng", "Biểu tượng băng hải tặc Râu Trắng hùng mạnh", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(58, "Huy Hiệu Usopp Xạ Thủ", "Biểu tượng của vua bắn tỉa Sogeking", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(62, "Huy Hiệu Chim Sâu", "Biểu tượng theo loài chim kiên cường vượt bão", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(71, "Huy Hiệu Hoàng Tử", "Huy hiệu quý tộc Hoàng Gia", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(94, "Huy Hiệu Tàu Sunny", "Vượt qua muôn ngàn đại dương và làm chủ bầu trời", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(118, "Huy Hiệu Chopper", "Thú cưng dễ thương trị giá 500 Beli", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(160, "Huy Hiệu Pikachu", "Chuột sấm sét Pika Pika Chuuuuu", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(191, "Huy Hiệu Chó Bulldog", "Biểu tượng chú chó dũng cảm kiên cường", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(243, "Huy Hiệu Angry Bird", "Cơn thịnh nộ của loài chim đỏ", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(275, "Huy Hiệu Mihawk Hắc Kiếm", "Biểu tượng của Đệ Nhất Kiếm Sĩ Thế Giới", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(302, "Huy Hiệu Franky Người Máy", "Người máy biến hình siêu đẳng, Superrrrrr!", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(385, "Huy Hiệu Doflamingo", "Thuyền trưởng băng hải tặc Donquixote", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(388, "Huy Hiệu Crocodile", "Thủ lĩnh Baroque Works Cá Sấu Sa Mạc", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(390, "Huy Hiệu Kuzan Băng Giá", "Kỷ băng hà tuyệt đối của biển cả", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(395, "Huy Hiệu Kaido Bách Thú", "1 vs 1 đơn đả độc đấu bất khả chiến bại", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(513, "Huy Hiệu Jinbe Hiệp Sĩ", "Muốn thử Karate người cá thì cứ đến gặp ta", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(525, "Huy Hiệu Ghost Bóng Ma", "Bóng ma trôi dạt nơi biển sâu bí ẩn", 1000, (byte) 0, true));
        ENTRY.add(new ClanIcon(530, "Huy Hiệu Zombie Xác Sống", "Xác sống bất tử luôn ở quanh ta", 1000, (byte) 0, true));

        // Bộ huy hiệu hải trình căn bản (335 - 349)
        ENTRY.add(new ClanIcon(335, "Huy Hiệu Neo Biển", "Neo đậu kiên định giữa muôn trùng sóng gió", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(336, "Huy Hiệu La Bàn Gió", "Chỉ lối cho các hải trình vượt bão tố đại dương", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(337, "Huy Hiệu Bánh Lái Tàu", "Khẳng định quyền dẫn dắt chiến hạm rẽ sóng", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(338, "Huy Hiệu Ngọn Hải Đăng", "Ánh sáng xua tan màn đêm tăm tối nơi hải phận", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(339, "Huy Hiệu Sóng Biển", "Sức mạnh dâng trào như thủy triều cuồn cuộn", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(340, "Huy Hiệu Kính Viễn Vọng", "Tầm nhìn xa nghìn dặm của hoa tiêu tài ba", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(341, "Huy Hiệu Cánh Buồm Trắng", "Đón gió biển khơi vượt qua ngàn dặm trùng dương", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(342, "Huy Hiệu Mỏ Neo Vàng", "Vật phẩm biểu trưng cho phú quý và sự vững chãi", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(343, "Huy Hiệu Bản Đồ Cổ", "Lộ trình hải đồ dẫn lối đến các kho báu bí mật", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(344, "Huy Hiệu Đại Bác Hạm Đội", "Hỏa lực pháo đài công kích kiên cố trên biển cả", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(345, "Huy Hiệu Thủy Thủ Đoàn", "Tinh thần đồng đội keo sơn gắn bó trên cùng chiến hạm", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(346, "Huy Hiệu Song Kiếm Cảng", "Kiếm pháp phòng vệ bến đỗ trước cướp biển", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(347, "Huy Hiệu Chiến Binh Biển", "Bất khuất kiên cường trước phong ba bão táp", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(348, "Huy Hiệu Rương Kho Báu", "Thành quả sau những chuyến thám hiểm hiểm nguy", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(349, "Huy Hiệu Khiên San Hô", "Lớp bảo hộ kết tinh từ san hô ngàn năm dưới đáy biển", 500, (byte) 0, true));

        // Bộ huy hiệu đảo Grand Line (500 - 509)
        ENTRY.add(new ClanIcon(500, "Huy Hiệu Làng Foosha", "Ngôi làng yên bình nơi cậu bé Mũ Rơm bắt đầu ước mơ", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(501, "Huy Hiệu Chuông Vàng Shandora", "Tháp chuông vàng cổ đại vang vọng lời hứa ngàn năm", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(502, "Huy Hiệu Chiến Hạm Gỗ Adam", "Chiến hạm gỗ Adam vượt qua muôn vàn hải trình bão táp", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(503, "Huy Hiệu Ngư Lôi Cổ", "Vũ khí thủy chiến ngầm từ thời đại hoàng kim", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(504, "Huy Hiệu Sa Mạc Nanohana", "Ý chí quật cường của vương quốc sa mạc Alabasta", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(505, "Huy Hiệu Đảo Trên Trời", "Vương quốc mây trắng bồng bềnh Skypiea", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(506, "Huy Hiệu Kinh Đô Nước Water Seven", "Kinh đô thợ đóng tàu Water Seven huyền thoại", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(507, "Huy Hiệu Đảo Tư Pháp", "Hòn đảo không bao giờ có bóng đêm Enies Lobby", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(508, "Huy Hiệu Thuyền Ma Thriller Bark", "Con tàu ma khổng lồ Thriller Bark trong sương mù", 500, (byte) 0, true));
        ENTRY.add(new ClanIcon(509, "Huy Hiệu Quần Đảo Bọt Biển Sabaody", "Nơi dừng chân ngập tràn bong bóng Sabaody", 500, (byte) 0, true));

        // ==========================================
        // 2. BIỂU TƯỢNG CAO CẤP (VIP / ĐỘNG - TYPE = 1)
        // ==========================================
        // Biểu tượng VIP kinh điển
        ENTRY.add(new ClanIcon(83, "Huy Hiệu Cờ Đỏ Sao Vàng", "Biểu tượng tự hào Tổ quốc Việt Nam hào hùng", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(89, "Huy Hiệu Barcelona", "CLB Bóng Đá Barcelona - Mes que un club", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(96, "Huy Hiệu Manchester United", "CLB Manchester United - Quỷ Đỏ thành Manchester", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(158, "Huy Hiệu Number One", "Biểu tượng Vô Địch - Vị thế số 1 biển cả", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(161, "Huy Hiệu Love Trái Tim", "Biểu tượng tình yêu vĩnh cửu và gắn kết bang hội", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(517, "Huy Hiệu Phi Hành Gia Among Us", "Kẻ mạo danh tinh quái du hành thiên hà", 3500, (byte) 1, true));

        // Biểu tượng Tứ Hoàng & Thế Lực Lớn
        ENTRY.add(new ClanIcon(370, "Huy Hiệu Hải Tặc Mặt Trời Tự Do", "Ý chí tự do quật khởi của Fisher Tiger và người cá", 2000, (byte) 1, true));
        ENTRY.add(new ClanIcon(371, "Huy Hiệu Donquixote Family", "Gia tộc thế giới ngầm quyền lực tối thượng Dressrosa", 2500, (byte) 1, true));
        ENTRY.add(new ClanIcon(372, "Huy Hiệu Baroque Works Sát Thủ", "Tổ chức ngầm sa mạc Alabasta khét tiếng", 2000, (byte) 1, true));
        ENTRY.add(new ClanIcon(373, "Huy Hiệu Băng Heart Tử Thần", "Băng hải tặc của Bác Sĩ Tử Thần Trafalgar Law", 2500, (byte) 1, true));
        ENTRY.add(new ClanIcon(374, "Huy Hiệu Băng Kid Từ Tính", "Cơn thịnh nộ từ tính của Eustass Captain Kid", 2500, (byte) 1, true));
        ENTRY.add(new ClanIcon(375, "Huy Hiệu Băng Bách Thú Ma Quỷ", "Đội quân dã thú bạo tàn thống trị Wano Quốc", 2500, (byte) 1, true));
        ENTRY.add(new ClanIcon(376, "Huy Hiệu Băng Big Mom Totto Land", "Gia tộc nữ hoàng bánh ngọt quyền uy Totto Land", 2500, (byte) 1, true));
        ENTRY.add(new ClanIcon(377, "Huy Hiệu Băng Tóc Đỏ Bá Khí", "Bá khí cái thế của Tứ Hoàng Shanks Tóc Đỏ", 3000, (byte) 1, true));
        ENTRY.add(new ClanIcon(378, "Huy Hiệu Băng Râu Trắng Hoàng Kim", "Gia đình vĩ đại nhất đại dương của Bố Già Edward Newgate", 3000, (byte) 1, true));
        ENTRY.add(new ClanIcon(379, "Huy Hiệu Băng Râu Đen Hắc Ám", "Bóng tối nuốt chửng vạn vật của Marshall D. Teach", 3000, (byte) 1, true));
        ENTRY.add(new ClanIcon(380, "Huy Hiệu Mũ Rơm Tân Thế Giới", "Lời thề tái hợp sau 2 năm rèn luyện gian khổ", 2500, (byte) 1, true));
        ENTRY.add(new ClanIcon(381, "Huy Hiệu Cross Guild Săn Hải Quân", "Tổ chức săn tiền thưởng hải quân của Tân Tứ Hoàng Buggy", 2500, (byte) 1, true));
        ENTRY.add(new ClanIcon(382, "Huy Hiệu Quân Cách Mạng Giải Phóng", "Tổ chức giải phóng thế giới của Monkey D. Dragon", 3000, (byte) 1, true));
        ENTRY.add(new ClanIcon(383, "Huy Hiệu Công Lý Tuyệt Đối Hải Quân", "Biểu tượng công lý tuyệt đối của Tổng Bộ Hải Quân Marine", 2500, (byte) 1, true));
        ENTRY.add(new ClanIcon(384, "Huy Hiệu Mật Vụ CP0 Hoàng Gia", "Tổ chức tình báo bí mật tối cao của Thiên Long Nhân", 2500, (byte) 1, true));
        ENTRY.add(new ClanIcon(386, "Huy Hiệu Chúa Tể Bóng Ma Gecko Moria", "Chúa tể bóng ma đoạt lấy bóng hồn kẻ bại trận", 2000, (byte) 1, true));
        ENTRY.add(new ClanIcon(387, "Huy Hiệu Bạo Chúa Kuma Bàn Chân", "Hiệp sĩ cách mạng với áp suất không khí hủy diệt", 2500, (byte) 1, true));
        ENTRY.add(new ClanIcon(389, "Huy Hiệu Nữ Hoàng Kuja Boa Hancock", "Nữ vương Kuja hóa đá mọi trái tim si mê", 3000, (byte) 1, true));
        ENTRY.add(new ClanIcon(391, "Huy Hiệu Đô Đốc Dung Nham Akainu", "Đô Đốc Dung Nham trừng phạt công lý triệt để", 3000, (byte) 1, true));
        ENTRY.add(new ClanIcon(392, "Huy Hiệu Đô Đốc Ánh Sáng Kizaru", "Tốc độ ánh sáng kinh hoàng tia chớp vàng", 3000, (byte) 1, true));
        ENTRY.add(new ClanIcon(393, "Huy Hiệu Đô Đốc Trọng Lực Fujitora", "Đô Đốc Hổ Tím điều khiển trọng lực triệu hồi thiên thạch", 3000, (byte) 1, true));
        ENTRY.add(new ClanIcon(394, "Huy Hiệu Đô Đốc Rừng Rậm Ryokugyu", "Đô Đốc Bò Xanh tái sinh sức sống tự nhiên vô tận", 3000, (byte) 1, true));
        ENTRY.add(new ClanIcon(405, "Huy Hiệu Sấm Sét Lôi Thần Enel", "Vị thần tối cao đảo trời phóng triệu vôn sấm sét", 2500, (byte) 1, true));
        ENTRY.add(new ClanIcon(412, "Huy Hiệu Thần Thú Bạch Hổ", "Linh thú hổ trắng trấn thủ phương Tây dũng mãnh", 2500, (byte) 1, true));

        // ==========================================
        // 3. BIỂU TƯỢNG ĐỘNG (ANIMATED SPRITE STRIPS)
        // ==========================================
        ENTRY.add(new ClanIcon(316, "[Động] Hào Quang Hỏa Ma Thuật", "Ngọn lửa ma thuật tím đỏ bốc cháy 5 tầng cuồn cuộn", 4000, (byte) 1, true));
        ENTRY.add(new ClanIcon(317, "[Động] Thái Dương Rực Lửa 9 Tầng", "Biểu tượng Mặt Trời rực cháy 9 tầng hào quang rực rỡ", 4500, (byte) 1, true));
        ENTRY.add(new ClanIcon(318, "[Động] Mắt Sharingan Luân Hồi", "Con mắt ảo thuật thần bí xoay chuyển 9 tầng ảo ảnh", 4500, (byte) 1, true));
        ENTRY.add(new ClanIcon(319, "[Động] Brock Nhạc Công Tử Thần", "Khúc ca linh hồn bất tử 12 tầng vũ điệu cõi âm", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(320, "[Động] Mũ Rơm Hào Quang Vàng", "Ý chí D tỏa sáng hoàng kim 5 tầng chói lọi biển cả", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(321, "[Động] Huyết Chiến Hải Tặc", "Khát khao chinh phục đẫm máu 4 tầng hắc hỏa bốc cháy", 4500, (byte) 1, true));
        ENTRY.add(new ClanIcon(322, "[Động] Vergo Haki Tím Bá Khí", "Khí tức Haki Vũ Trang tím 9 tầng bao phủ toàn thân", 4500, (byte) 1, true));
        ENTRY.add(new ClanIcon(323, "[Động] Tàn Hộ Hắc Ám Cấm Thuật", "Cấm thuật bóng tối 8 tầng áp chế tinh thần kẻ thù", 4000, (byte) 1, true));
        ENTRY.add(new ClanIcon(324, "[Động] Bóng Ma U Linh Xanh", "Ngọn lửa linh hồn xanh 7 tầng trôi dạt hư vô", 4000, (byte) 1, true));
        ENTRY.add(new ClanIcon(325, "[Động] Bí Ngô Ma Quái Halloween", "Bí ngô quỷ dạ hành 8 tầng đêm hội bóng đêm", 4000, (byte) 1, true));
        ENTRY.add(new ClanIcon(326, "[Động] Phù Thủy Đêm Trăng", "Bóng hình phù thủy bay lượn dưới ánh trăng", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(327, "[Động] Cánh Cụt Lạnh Lùng", "Chim cánh cụt băng giá cực bắc cực đáng yêu", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(328, "[Động] Mèo Tuxedo Quý Phái", "Mèo đen quý tộc lấp lánh ánh mắt thần kỳ", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(329, "[Động] Lôi Thần Điện Quang", "Sấm chớp cuồng nộ giáng trần 8 luồng sét hủy diệt", 4500, (byte) 1, true));
        ENTRY.add(new ClanIcon(330, "[Động] Đầu Lâu Hỏa Diệm Bất Diệt", "Đầu lâu rực lửa tử thần 4 tầng ma hỏa bất diệt", 4000, (byte) 1, true));
        ENTRY.add(new ClanIcon(331, "[Động] Ngôi Mộ Bất Tử Cõi Chết", "Sự trỗi dậy từ cõi chết nghìn năm 8 tầng quỷ khí", 4000, (byte) 1, true));
        ENTRY.add(new ClanIcon(332, "[Động] Dạ Hành Hắc Dơi Chúa Tể", "Chúa tể bóng đêm săn mồi trong bóng tối", 4000, (byte) 1, true));
        ENTRY.add(new ClanIcon(333, "[Động] Lêu Lêu Tinh Quái Trêu Ngươi", "Trêu ngươi đối thủ khiến kẻ thù ức chế", 4500, (byte) 1, true));
        ENTRY.add(new ClanIcon(334, "[Động] Khỉ Đột Cuồng Nộ", "Sức mạnh dã man của chúa tể rừng rậm 8 tầng cuồng nộ", 4500, (byte) 1, true));
        ENTRY.add(new ClanIcon(360, "[Động] Ma Pháp Trận Ngũ Tinh Tím", "Ma trận triệu hồi 7 tầng phép thuật tím huyền ảo", 4500, (byte) 1, true));
        ENTRY.add(new ClanIcon(366, "[Động] Vòng Xoáy Vũ Trụ Galaxia", "Sức mạnh lỗ đen vũ trụ 12 chiều không gian", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(368, "[Động] Hắc Ám Long Tuyền Thần Long", "Rồng đen cuộn trào 10 tầng hắc hỏa uy áp", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(400, "[Động] Mihawk Đầu Lâu Tử Thần", "Nhát chém đệ nhất kiếm sĩ tử thần 7 tầng nhát kiếm", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(415, "[Động] Ngón Tay Bá Đạo Number 1", "Uy lực chỉ tay 9 tầng áp đảo thiên hạ", 4500, (byte) 1, true));
        ENTRY.add(new ClanIcon(416, "[Động] Huy Hiệu Ma U Ám Dạ Hành", "Bóng ma dạ hành 8 tầng thoắt ẩn thoắt hiện", 4000, (byte) 1, true));
        ENTRY.add(new ClanIcon(417, "[Động] Bá Tước Dracula Huyết Nguyệt", "Hút máu và bóng tối của bá tước Dracula 8 tầng", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(418, "[Động] Quỷ Vương Bất Diệt Sấm Xanh", "Uy áp tối thượng 11 tầng sấm xanh cuồng nộ", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(419, "[Động] Hỏa Khuyển Ngố Ngáo Siêu Quậy", "Thần khuyển lửa cuồng bạo 9 tầng siêu quậy", 4500, (byte) 1, true));

        // Bộ 10 Chibi Hoàng Gia Động (420 - 429)
        ENTRY.add(new ClanIcon(420, "[Động] Chibi Hoàng Gia Vương Miện", "Thủ lĩnh nhí mang vương miện hoàng gia lấp lánh", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(421, "[Động] Chibi Kiếm Sĩ Dũng Cảm", "Kiếm sĩ nhí dũng mãnh vung kiếm rèn luyện", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(422, "[Động] Chibi Hoa Tiêu Xinh Đẹp", "Hoa tiêu nhí đáng yêu chỉ lối đại dương", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(423, "[Động] Chibi Thủy Thủ Nhí Nhảnh", "Thủy thủ nhí nhảnh bơi lội biển xanh", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(424, "[Động] Chibi Chiến Binh Gai Góc", "Chiến binh gai góc bất khuất", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(425, "[Động] Chibi Bác Sĩ Dễ Thương", "Bác sĩ tí hon chăm sóc sức khỏe đồng đội", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(426, "[Động] Chibi Đầu Bếp Siêu Phàm", "Đầu bếp nấu món ngon tiếp thêm năng lượng", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(427, "[Động] Chibi Thợ Đóng Tàu Tài Ba", "Thợ đóng tàu gia cố chiến hạm vượt sóng", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(428, "[Động] Chibi Xạ Thủ Bách Trúng", "Xạ thủ nhí bách phát bách trúng", 3500, (byte) 1, true));
        ENTRY.add(new ClanIcon(429, "[Động] Chibi Nhạc Sĩ Linh Hồn", "Nhạc sĩ gảy đàn khúc ca hải tặc rộn ràng", 3500, (byte) 1, true));

        // ==========================================
        // 4. DANH HIỆU THẦN THOẠI & CAO CẤP BỔ SUNG
        // ==========================================
        ENTRY.add(new ClanIcon(352, "Song Long Hộ Mệnh", "Biểu tượng song long bảo hộ vận mệnh toàn băng", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(402, "Hỏa Long Bão Táp", "Hỏa long cuồng nộ thiêu rụi mọi chướng ngại", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(531, "Thức Tỉnh Trái Ác Quỷ", "Khai mở sức mạnh thức tỉnh tối thượng", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(532, "[Top 1] Vương Miện Quán Quân Biển", "Băng hải tặc vĩ đại nhất đại dương", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(533, "Cúp Bạc Hải Chiến", "Vinh danh chiến tích lẫy lừng trên đại dương", 4500, (byte) 1, true));
        ENTRY.add(new ClanIcon(534, "Huân Chương Danh Dự", "Biểu tượng lòng quả cảm và danh dự hải tặc", 4000, (byte) 1, true));
        ENTRY.add(new ClanIcon(539, "[Chiếm Đảo] Chiến Thắng Chiếm Đảo Pháo Đài", "Thống trị pháo đài lãnh địa biển", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(542, "[Thần Thoại] Vua Hải Tặc Roger", "Chinh phục hoàn toàn kho báu One Piece", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(543, "Huy Hiệu Vua Bóng Tối Rayleigh", "Bá khí vô song của cánh tay phải Vua Hải Tặc", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(544, "Huy Hiệu Anh Hùng Garp", "Nắm đấm công lý chấn động thời đại hải tặc", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(546, "Huy Hiệu Kozuki Oden", "Ý chí ngút trời của samurai huyền thoại xứ Wano", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(547, "[Thần Thoại] Thần Mặt Trời Nika Gear 5", "Hiện thân của Thần Tự Do Nika chiến binh", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(548, "Huyền Thoại Rocks D. Xebec", "Biểu tượng bóng đêm kinh hoàng nhất lịch sử hải tặc", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(549, "[Thần Thoại] Joy Boy Cổ Đại", "Lời hứa thế kỷ trống 800 năm", 5000, (byte) 1, true));
        ENTRY.add(new ClanIcon(552, "[Độc Quyền] Ngai Vàng Biển Cả", "Thống lĩnh toàn bộ các hòn đảo Grand Line", 5000, (byte) 1, true));
    }
}
