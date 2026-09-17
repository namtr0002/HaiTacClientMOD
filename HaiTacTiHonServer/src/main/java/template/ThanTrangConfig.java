package template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ThanTrangConfig - Quản lý cấu hình dữ liệu chuẩn cho 15 Bộ Thần Trang (90 món: ID 2604..2693)
 * Mỗi bộ sở hữu đặc tính và phong cách chiến đấu riêng biệt, chỉ số cân bằng, không ảo, không bug.
 */
public class ThanTrangConfig {

    public static class OptionDef {
        public final int optionId;
        public final int param;

        public OptionDef(int optionId, int param) {
            this.optionId = optionId;
            this.param = param;
        }
    }

    public static class ThanTrangOptionRange {
        public final int optionId;
        public final int minParam;
        public final int maxParam;

        public ThanTrangOptionRange(int optionId, int minParam, int maxParam) {
            this.optionId = optionId;
            this.minParam = minParam;
            this.maxParam = maxParam;
        }
    }

    public static final String[] SET_NAMES = new String[]{
        "Set 1 - Dung Nham (Akainu - Thiêu Đốt Bộc Phá)",
        "Set 2 - Hàn Băng (Aokiji - Khống Chế Băng Giá)",
        "Set 3 - Quang Tốc (Kizaru - Tốc Độ & Né Tránh)",
        "Set 4 - Hắc Ám (Blackbeard - Hấp Thụ & Triệt Miễn Thương)",
        "Set 5 - Sấm Sét (Enel - Lôi Thần & Điện Giật)",
        "Set 6 - Chấn Động (Whitebeard - Đại Hải Tặc Chấn Thiên)",
        "Set 7 - Phẫu Thuật (Law - ROOM & Từ Chối Tử Thần)",
        "Set 8 - Từ Tính (Kid - Phản Lực & Pháo Railgun)",
        "Set 9 - Rồng Độc (Magellan - Ăn Mòn % Máu & Phá Giáp)",
        "Set 10 - Tình Yêu (Hancock - Hóa Đá Mê Hoặc & Suy Yếu)",
        "Set 11 - Phượng Hoàng Lam Hỏa (Marco - Tái Sinh & Chuyển Hóa HP)",
        "Set 12 - Phật Quang (Sengoku - Kim Thân Bất Hoại & Miễn Thương)",
        "Set 13 - Bách Thú Kaido (Kaido - Thanh Long Cuồng Nộ & Sát Thương Cuối)",
        "Set 14 - Hổ Răng Kiếm (Who's Who - Sát Thủ Tiền Sử & Nanh Vuốt Bạo Kích)",
        "Set 15 - T-Rex Bạo Chúa (Queen - Titan Tiền Sử & Máu Khổng Lồ)"
    };

    public static int getSkillBySetId(int setId) {
        if (setId >= 1 && setId <= 15) {
            return 4001 + setId;
        }
        return -1;
    }

    public static int getSetIdBySkill(int skillId) {
        if (skillId >= 4002 && skillId <= 4016) {
            return skillId - 4001;
        }
        if (skillId == 4001) {
            return 1;
        }
        if (skillId >= 4017 && skillId <= 4080) {
            return (skillId - 4001) / 5 + 1;
        }
        return 0;
    }

    public static boolean isThanTrangSkill(int skillId) {
        return skillId >= 4001 && skillId <= 4080;
    }

    private static final Map<Integer, OptionDef[]> THAN_TRANG_MAP = new HashMap<>();
    private static final Map<Integer, Byte> TYPE_EQUIP_MAP = new HashMap<>();

    static {
        // =========================================================================
        // SET 1: DUNG NHAM - AKAINU (2604..2609)
        // Đặc tính: Thiêu Đốt Bộc Phá & Sát Thương Chuẩn cực đại, triệt tiêu miễn thương
        // =========================================================================
        // 2604: Găng Dung Nham Cực Hạn (typeequip 8)
        register(2604, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+8.5%)
            new OptionDef(0, 160),   // Tấn công
            new OptionDef(57, 30),   // Sát thương chuẩn % (+3.0%)
            new OptionDef(64, 20),   // Bộc phá Ác Quỷ % (+2.0%)
            new OptionDef(13, 22),   // Xuyên giáp % (+2.2%)
            new OptionDef(10, 20)    // Chí mạng % (+2.0%)
        });
        // 2605: Mũ Nguyên Soái Hỏa Nham (typeequip 9)
        register(2605, (byte) 9, new OptionDef[]{
            new OptionDef(3, 55),    // Phòng thủ
            new OptionDef(15, 150),  // HP +
            new OptionDef(17, 45),   // Tăng HP % (+4.5%)
            new OptionDef(56, 25),   // Máu cuối % (+2.5%)
            new OptionDef(27, 25),   // Kháng phép % (+2.5%)
            new OptionDef(5, 6)      // T/n sức mạnh
        });
        // 2606: Dây Chuyền Lõi Núi Lửa (typeequip 10)
        register(2606, (byte) 10, new OptionDef[]{
            new OptionDef(15, 180),  // HP +
            new OptionDef(17, 50),   // Tăng HP % (+5.0%)
            new OptionDef(63, 20),   // Giảm miễn thương % (+2.0%)
            new OptionDef(57, 22),   // Sát thương chuẩn % (+2.2%)
            new OptionDef(46, 20),   // Sát thương cuối % (+2.0%)
            new OptionDef(21, 10)    // Hút HP
        });
        // 2607: Giáp Dung Nham Bất Hoại (typeequip 11)
        register(2607, (byte) 11, new OptionDef[]{
            new OptionDef(3, 70),    // Phòng thủ
            new OptionDef(4, 40),    // Tăng P.Thủ % (+4.0%)
            new OptionDef(15, 180),  // HP +
            new OptionDef(26, 30),   // Kháng vật lý % (+3.0%)
            new OptionDef(56, 25),   // Máu cuối % (+2.5%)
            new OptionDef(14, 18)    // Phản đòn % (+1.8%)
        });
        // 2608: Nhẫn Hỏa Diệm Magma (typeequip 12)
        register(2608, (byte) 12, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+5.5%)
            new OptionDef(10, 22),   // Chí mạng % (+2.2%)
            new OptionDef(11, 50),   // S.t chí mạng % (+5.0%)
            new OptionDef(63, 18),   // Giảm miễn thương % (+1.8%)
            new OptionDef(57, 20),   // Sát thương chuẩn % (+2.0%)
            new OptionDef(64, 15)    // Bộc phá Ác Quỷ % (+1.5%)
        });
        // 2609: Ủng Dung Nham Thiêu Đốt (typeequip 13)
        register(2609, (byte) 13, new OptionDef[]{
            new OptionDef(3, 55),    // Phòng thủ
            new OptionDef(12, 20),   // Né tránh % (+2.0%)
            new OptionDef(14, 20),   // Phản đòn % (+2.0%)
            new OptionDef(76, 20),   // Tăng % chính xác (+2.0%)
            new OptionDef(56, 25),   // Máu cuối % (+2.5%)
            new OptionDef(25, 15)    // Tốc độ hồi chiêu % (+1.5%)
        });

        // =========================================================================
        // SET 3: HÀN BĂNG - AOKIJI (2610..2615)
        // Đặc tính: Khống Chế Băng Giá, Đóng Băng Choáng, Triệt Tiêu Chí Mạng Đối Thủ
        // =========================================================================
        // 2610: Kiếm Băng Cực Hàn (typeequip 8)
        register(2610, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+8.0%)
            new OptionDef(2, 50),   // Phép thuật % (+15.0%)
            new OptionDef(75, 22),   // Tăng % choáng (+2.2%)
            new OptionDef(46, 20),   // Sát thương cuối % (+2.0%)
            new OptionDef(10, 20),   // Chí mạng % (+2.0%)
            new OptionDef(11, 50)    // S.t chí mạng % (+5.0%)
        });
        // 2611: Mũ Băng Tuyết Vĩnh Cửu (typeequip 9)
        register(2611, (byte) 9, new OptionDef[]{
            new OptionDef(3, 60),    // Phòng thủ
            new OptionDef(15, 160),  // HP +
            new OptionDef(49, 22),   // Giảm chí mạng đ/t % (+2.2%)
            new OptionDef(27, 32),   // Kháng phép % (+3.2%)
            new OptionDef(56, 25),   // Máu cuối % (+2.5%)
            new OptionDef(8, 6)      // T/n tinh thần
        });
        // 2612: Dây Chuyền Trái Tim Hàn Băng (typeequip 10)
        register(2612, (byte) 10, new OptionDef[]{
            new OptionDef(15, 170),  // HP +
            new OptionDef(16, 110),  // MP +
            new OptionDef(53, 20),   // Miễn thương % (+2.0%)
            new OptionDef(71, 22),   // Kháng hiệu ứng % (+2.2%)
            new OptionDef(25, 18),   // Tốc độ hồi chiêu % (+1.8%)
            new OptionDef(22, 10)    // Hút MP
        });
        // 2613: Giáp Tinh Thể Hàn Băng (typeequip 11)
        register(2613, (byte) 11, new OptionDef[]{
            new OptionDef(3, 75),    // Phòng thủ
            new OptionDef(4, 45),    // Tăng P.Thủ % (+4.5%)
            new OptionDef(15, 190),  // HP +
            new OptionDef(27, 35),   // Kháng phép % (+3.5%)
            new OptionDef(26, 25),   // Kháng vật lý % (+2.5%)
            new OptionDef(53, 15)    // Miễn thương % (+1.5%)
        });
        // 2614: Nhẫn Hoa Tuyết Lam (typeequip 12)
        register(2614, (byte) 12, new OptionDef[]{
            new OptionDef(2, 50),   // Phép thuật % (+12.0%)
            new OptionDef(10, 20),   // Chí mạng % (+2.0%)
            new OptionDef(75, 18),   // Tăng % choáng (+1.8%)
            new OptionDef(49, 20),   // Giảm chí mạng đ/t % (+2.0%)
            new OptionDef(69, 25),   // Giảm ST chí mạng đ/t % (+2.5%)
            new OptionDef(18, 35)    // Tăng MP % (+3.5%)
        });
        // 2615: Giày Băng Trượt Tuyết Hàn Băng (typeequip 13)
        register(2615, (byte) 13, new OptionDef[]{
            new OptionDef(3, 58),    // Phòng thủ
            new OptionDef(12, 24),   // Né tránh % (+2.4%)
            new OptionDef(76, 20),   // Tăng % chính xác (+2.0%)
            new OptionDef(75, 15),   // Tăng % choáng (+1.5%)
            new OptionDef(56, 25),   // Máu cuối % (+2.5%)
            new OptionDef(25, 16)    // Tốc độ hồi chiêu % (+1.6%)
        });

        // =========================================================================
        // SET 4: QUANG TỐC - KIZARU (2616..2621)
        // Đặc tính: Tốc Độ Ánh Sáng, Né Tránh Cực Cao & Chính Xác Tuyệt Đối
        // =========================================================================
        // 2616: Thánh Kiếm Quang Tốc Murakumokiri (typeequip 8)
        register(2616, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+9.0%)
            new OptionDef(0, 170),   // Tấn công
            new OptionDef(10, 25),   // Chí mạng % (+2.5%)
            new OptionDef(11, 50),   // S.t chí mạng % (+6.0%)
            new OptionDef(76, 25),   // Tăng % chính xác (+2.5%)
            new OptionDef(46, 22)    // Sát thương cuối % (+2.2%)
        });
        // 2617: Mũ Quang Tử Tốc Độ (typeequip 9)
        register(2617, (byte) 9, new OptionDef[]{
            new OptionDef(3, 52),    // Phòng thủ
            new OptionDef(15, 140),  // HP +
            new OptionDef(12, 25),   // Né tránh % (+2.5%)
            new OptionDef(76, 20),   // Tăng % chính xác (+2.0%)
            new OptionDef(9, 7),     // T/n nhanh nhẹn
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2618: Dây Chuyền Hạt Quang Tử (typeequip 10)
        register(2618, (byte) 10, new OptionDef[]{
            new OptionDef(15, 150),  // HP +
            new OptionDef(25, 22),   // Tốc độ hồi chiêu % (+2.2%)
            new OptionDef(12, 20),   // Né tránh % (+2.0%)
            new OptionDef(76, 20),   // Tăng % chính xác (+2.0%)
            new OptionDef(53, 18),   // Miễn thương % (+1.8%)
            new OptionDef(17, 40)    // Tăng HP % (+4.0%)
        });
        // 2619: Giáp Hoàng Kim Quang Học (typeequip 11)
        register(2619, (byte) 11, new OptionDef[]{
            new OptionDef(3, 68),    // Phòng thủ
            new OptionDef(4, 38),    // Tăng P.Thủ % (+3.8%)
            new OptionDef(15, 170),  // HP +
            new OptionDef(12, 22),   // Né tránh % (+2.2%)
            new OptionDef(26, 25),   // Kháng vật lý % (+2.5%)
            new OptionDef(27, 25)    // Kháng phép % (+2.5%)
        });
        // 2620: Nhẫn Lăng Kính Bát Quái (typeequip 12)
        register(2620, (byte) 12, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+6.0%)
            new OptionDef(10, 24),   // Chí mạng % (+2.4%)
            new OptionDef(11, 50),   // S.t chí mạng % (+5.5%)
            new OptionDef(25, 18),   // Tốc độ hồi chiêu % (+1.8%)
            new OptionDef(76, 22),   // Tăng % chính xác (+2.2%)
            new OptionDef(63, 18)    // Giảm miễn thương % (+1.8%)
        });
        // 2621: Ủng Tốc Độ Tia Chớp (typeequip 13)
        register(2621, (byte) 13, new OptionDef[]{
            new OptionDef(3, 50),    // Phòng thủ
            new OptionDef(12, 30),   // Né tránh % (+3.0%)
            new OptionDef(76, 25),   // Tăng % chính xác (+2.5%)
            new OptionDef(25, 18),   // Tốc độ hồi chiêu % (+1.8%)
            new OptionDef(9, 7),     // T/n nhanh nhẹn
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });

        // =========================================================================
        // SET 5: HẮC ÁM - BLACKBEARD (2622..2627)
        // Đặc tính: Hố Đen Hấp Thụ & Triệt Tiêu Miễn Thương, Hút Sinh Lực
        // =========================================================================
        // 2622: Móng Vuốt Hắc Ám Hư Không (typeequip 8)
        register(2622, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+8.5%)
            new OptionDef(0, 175),   // Tấn công
            new OptionDef(63, 25),   // Giảm miễn thương % (+2.5%)
            new OptionDef(57, 28),   // Sát thương chuẩn % (+2.8%)
            new OptionDef(13, 24),   // Xuyên giáp % (+2.4%)
            new OptionDef(21, 15)    // Hút HP
        });
        // 2623: Mũ Thuyền Trưởng Vực Thẳm (typeequip 9)
        register(2623, (byte) 9, new OptionDef[]{
            new OptionDef(3, 60),    // Phòng thủ
            new OptionDef(15, 170),  // HP +
            new OptionDef(63, 16),   // Giảm miễn thương % (+1.6%)
            new OptionDef(58, 18),   // Hấp thụ % (+1.8%)
            new OptionDef(26, 25),   // Kháng vật lý % (+2.5%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2624: Dây Chuyền Hạt Nhân Hắc Ám (typeequip 10)
        register(2624, (byte) 10, new OptionDef[]{
            new OptionDef(15, 180),  // HP +
            new OptionDef(16, 120),  // MP +
            new OptionDef(58, 20),   // Hấp thụ % (+2.0%)
            new OptionDef(71, 22),   // Kháng hiệu ứng % (+2.2%)
            new OptionDef(21, 12),   // Hút HP
            new OptionDef(22, 12)    // Hút MP
        });
        // 2625: Áo Choàng Lỗ Đen Hắc Ám (typeequip 11)
        register(2625, (byte) 11, new OptionDef[]{
            new OptionDef(3, 74),    // Phòng thủ
            new OptionDef(4, 42),    // Tăng P.Thủ % (+4.2%)
            new OptionDef(15, 200),  // HP +
            new OptionDef(58, 22),   // Hấp thụ % (+2.2%)
            new OptionDef(26, 28),   // Kháng vật lý % (+2.8%)
            new OptionDef(27, 28)    // Kháng phép % (+2.8%)
        });
        // 2626: Nhẫn Xoáy Hư Vô (typeequip 12)
        register(2626, (byte) 12, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+5.8%)
            new OptionDef(63, 22),   // Giảm miễn thương % (+2.2%)
            new OptionDef(50, 20),   // Giảm xuyên giáp đ/t % (+2.0%)
            new OptionDef(59, 18),   // Hút % máu (+1.8%)
            new OptionDef(10, 20),   // Chí mạng % (+2.0%)
            new OptionDef(22, 12)    // Hút MP
        });
        // 2627: Ủng Bóng Đêm Vô Tận (typeequip 13)
        register(2627, (byte) 13, new OptionDef[]{
            new OptionDef(3, 54),    // Phòng thủ
            new OptionDef(14, 20),   // Phản đòn % (+2.0%)
            new OptionDef(58, 16),   // Hấp thụ % (+1.6%)
            new OptionDef(19, 18),   // Tự hồi HP
            new OptionDef(20, 18),   // Tự hồi MP
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });

        // =========================================================================
        // SET 6: SẤM SÉT - ENEL (2628..2633)
        // Đặc tính: Lôi Thần 200 Triệu Volt, Gây Điện Giật & Sốc Choáng Bạo Kích
        // =========================================================================
        // 2628: Trượng Sấm Sét 200 Triệu Volt (typeequip 8)
        register(2628, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+8.0%)
            new OptionDef(2, 50),   // Phép thuật % (+18.0%)
            new OptionDef(80, 25),   // +% gây Điện giật trong 4s (+2.5%)
            new OptionDef(75, 22),   // Tăng % choáng (+2.2%)
            new OptionDef(11, 50),   // S.t chí mạng % (+5.5%)
            new OptionDef(10, 22)    // Chí mạng % (+2.2%)
        });
        // 2629: Mũ Thần Sấm Trống Lôi Đài (typeequip 9)
        register(2629, (byte) 9, new OptionDef[]{
            new OptionDef(3, 54),    // Phòng thủ
            new OptionDef(15, 150),  // HP +
            new OptionDef(80, 18),   // +% gây Điện giật (+1.8%)
            new OptionDef(27, 30),   // Kháng phép % (+3.0%)
            new OptionDef(75, 16),   // Tăng % choáng (+1.6%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2630: Dây Chuyền Trống Thần Lôi (typeequip 10)
        register(2630, (byte) 10, new OptionDef[]{
            new OptionDef(15, 160),  // HP +
            new OptionDef(16, 120),  // MP +
            new OptionDef(80, 20),   // +% gây Điện giật (+2.0%)
            new OptionDef(25, 20),   // Tốc độ hồi chiêu % (+2.0%)
            new OptionDef(71, 20),   // Kháng hiệu ứng % (+2.0%)
            new OptionDef(53, 16)    // Miễn thương % (+1.6%)
        });
        // 2631: Giáp Điện Từ Hoàng Kim (typeequip 11)
        register(2631, (byte) 11, new OptionDef[]{
            new OptionDef(3, 70),    // Phòng thủ
            new OptionDef(4, 40),    // Tăng P.Thủ % (+4.0%)
            new OptionDef(15, 180),  // HP +
            new OptionDef(80, 18),   // +% gây Điện giật (+1.8%)
            new OptionDef(26, 26),   // Kháng vật lý % (+2.6%)
            new OptionDef(27, 30)    // Kháng phép % (+3.0%)
        });
        // 2632: Nhẫn Lôi Vân Tụ Khí (typeequip 12)
        register(2632, (byte) 12, new OptionDef[]{
            new OptionDef(2, 50),   // Phép thuật % (+13.0%)
            new OptionDef(10, 24),   // Chí mạng % (+2.4%)
            new OptionDef(11, 50),   // S.t chí mạng % (+5.5%)
            new OptionDef(80, 18),   // +% gây Điện giật (+1.8%)
            new OptionDef(75, 16),   // Tăng % choáng (+1.6%)
            new OptionDef(57, 20)    // Sát thương chuẩn % (+2.0%)
        });
        // 2633: Ủng Tia Chớp Sấm Sét (typeequip 13)
        register(2633, (byte) 13, new OptionDef[]{
            new OptionDef(3, 50),    // Phòng thủ
            new OptionDef(12, 25),   // Né tránh % (+2.5%)
            new OptionDef(76, 22),   // Tăng % chính xác (+2.2%)
            new OptionDef(80, 16),   // +% gây Điện giật (+1.6%)
            new OptionDef(25, 16),   // Tốc độ hồi chiêu % (+1.6%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });

        // =========================================================================
        // SET 7: CHẤN ĐỘNG - WHITEBEARD (2634..2639)
        // Đặc tính: Đại Hải Tặc Chấn Thiên & Sát Thương Cuối Hủy Diệt, Xuyên Phá Giáp
        // =========================================================================
        // 2634: Đại Đao Rạn Nứt Không Gian (typeequip 8)
        register(2634, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+9.5%)
            new OptionDef(0, 200),   // Tấn công
            new OptionDef(46, 28),   // Sát thương cuối % (+2.8%)
            new OptionDef(13, 26),   // Xuyên giáp % (+2.6%)
            new OptionDef(57, 30),   // Sát thương chuẩn % (+3.0%)
            new OptionDef(10, 20)    // Chí mạng % (+2.0%)
        });
        // 2635: Mũ Chiến Binh Chấn Động (typeequip 9)
        register(2635, (byte) 9, new OptionDef[]{
            new OptionDef(3, 65),    // Phòng thủ
            new OptionDef(15, 180),  // HP +
            new OptionDef(5, 8),     // T/n sức mạnh
            new OptionDef(17, 50),   // Tăng HP % (+5.5%)
            new OptionDef(26, 30),   // Kháng vật lý % (+3.0%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2636: Dây Chuyền Rạn Nứt Địa Cầu (typeequip 10)
        register(2636, (byte) 10, new OptionDef[]{
            new OptionDef(15, 200),  // HP +
            new OptionDef(46, 20),   // Sát thương cuối % (+2.0%)
            new OptionDef(57, 22),   // Sát thương chuẩn % (+2.2%)
            new OptionDef(13, 20),   // Xuyên giáp % (+2.0%)
            new OptionDef(53, 18),   // Miễn thương % (+1.8%)
            new OptionDef(71, 20)    // Kháng hiệu ứng % (+2.0%)
        });
        // 2637: Đại Bào Chấn Thiên Bát Ngát (typeequip 11)
        register(2637, (byte) 11, new OptionDef[]{
            new OptionDef(3, 80),    // Phòng thủ
            new OptionDef(4, 48),    // Tăng P.Thủ % (+4.8%)
            new OptionDef(15, 220),  // HP +
            new OptionDef(26, 34),   // Kháng vật lý % (+3.4%)
            new OptionDef(27, 26),   // Kháng phép % (+2.6%)
            new OptionDef(53, 16)    // Miễn thương % (+1.6%)
        });
        // 2638: Nhẫn Sóng Xung Kích (typeequip 12)
        register(2638, (byte) 12, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+6.5%)
            new OptionDef(13, 22),   // Xuyên giáp % (+2.2%)
            new OptionDef(46, 18),   // Sát thương cuối % (+1.8%)
            new OptionDef(57, 22),   // Sát thương chuẩn % (+2.2%)
            new OptionDef(63, 18),   // Giảm miễn thương % (+1.8%)
            new OptionDef(11, 45)    // S.t chí mạng % (+4.5%)
        });
        // 2639: Ủng Địa Chấn Rung Chuyển (typeequip 13)
        register(2639, (byte) 13, new OptionDef[]{
            new OptionDef(3, 60),    // Phòng thủ
            new OptionDef(14, 24),   // Phản đòn % (+2.4%)
            new OptionDef(5, 6),     // T/n sức mạnh
            new OptionDef(76, 20),   // Tăng % chính xác (+2.0%)
            new OptionDef(19, 18),   // Tự hồi HP
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });

        // =========================================================================
        // SET 8: PHẪU THUẬT - LAW (2640..2645)
        // Đặc tính: Bác Sĩ Tử Thần & Không Gian ROOM, Từ Chối Tử Thần, Giảm Hồi Chiêu
        // =========================================================================
        // 2640: Quỷ Kiếm Kikoku Phẫu Thuật (typeequip 8)
        register(2640, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+8.5%)
            new OptionDef(2, 50),   // Phép thuật % (+16.0%)
            new OptionDef(25, 24),   // Tốc độ hồi chiêu % (+2.4%)
            new OptionDef(10, 25),   // Chí mạng % (+2.5%)
            new OptionDef(11, 50),   // S.t chí mạng % (+5.5%)
            new OptionDef(13, 22)    // Xuyên giáp % (+2.2%)
        });
        // 2641: Mũ Lông Bác Sĩ Tử Thần (typeequip 9)
        register(2641, (byte) 9, new OptionDef[]{
            new OptionDef(3, 55),    // Phòng thủ
            new OptionDef(15, 150),  // HP +
            new OptionDef(8, 7),     // T/n tinh thần
            new OptionDef(27, 30),   // Kháng phép % (+3.0%)
            new OptionDef(71, 22),   // Kháng hiệu ứng % (+2.2%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2642: Dây Chuyền Trái Tim Bất Tử (typeequip 10)
        register(2642, (byte) 10, new OptionDef[]{
            new OptionDef(15, 180),  // HP +
            new OptionDef(55, 16),   // Từ chối tử thần % (+1.6%)
            new OptionDef(25, 22),   // Tốc độ hồi chiêu % (+2.2%)
            new OptionDef(53, 20),   // Miễn thương % (+2.0%)
            new OptionDef(71, 22),   // Kháng hiệu ứng % (+2.2%)
            new OptionDef(19, 20)    // Tự hồi HP
        });
        // 2643: Áo Choàng Không Gian ROOM (typeequip 11)
        register(2643, (byte) 11, new OptionDef[]{
            new OptionDef(3, 70),    // Phòng thủ
            new OptionDef(4, 40),    // Tăng P.Thủ % (+4.0%)
            new OptionDef(15, 190),  // HP +
            new OptionDef(12, 22),   // Né tránh % (+2.2%)
            new OptionDef(27, 30),   // Kháng phép % (+3.0%)
            new OptionDef(26, 26)    // Kháng vật lý % (+2.6%)
        });
        // 2644: Nhẫn Phẫu Thuật Không Gian (typeequip 12)
        register(2644, (byte) 12, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+5.5%)
            new OptionDef(2, 50),   // Phép thuật % (+12.0%)
            new OptionDef(25, 20),   // Tốc độ hồi chiêu % (+2.0%)
            new OptionDef(10, 22),   // Chí mạng % (+2.2%)
            new OptionDef(57, 20),   // Sát thương chuẩn % (+2.0%)
            new OptionDef(63, 18)    // Giảm miễn thương % (+1.8%)
        });
        // 2645: Ủng Dịch Chuyển Tức Thời (typeequip 13)
        register(2645, (byte) 13, new OptionDef[]{
            new OptionDef(3, 52),    // Phòng thủ
            new OptionDef(12, 28),   // Né tránh % (+2.8%)
            new OptionDef(25, 20),   // Tốc độ hồi chiêu % (+2.0%)
            new OptionDef(76, 22),   // Tăng % chính xác (+2.2%)
            new OptionDef(19, 18),   // Tự hồi HP
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });

        // =========================================================================
        // SET 9: TỪ TÍNH - KID (2646..2651)
        // Đặc tính: Phản Lực Từ Trường & Pháo Điện Từ Railgun, Giáp Kiên Cố, Phản Đòn
        // =========================================================================
        // 2646: Cánh Tay Phế Liệu Từ Tính (typeequip 8)
        register(2646, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+9.0%)
            new OptionDef(0, 180),   // Tấn công
            new OptionDef(14, 25),   // Phản đòn % (+2.5%)
            new OptionDef(13, 25),   // Xuyên giáp % (+2.5%)
            new OptionDef(46, 22),   // Sát thương cuối % (+2.2%)
            new OptionDef(57, 25)    // Sát thương chuẩn % (+2.5%)
        });
        // 2647: Mũ Kim Loại Cơ Khí Punk (typeequip 9)
        register(2647, (byte) 9, new OptionDef[]{
            new OptionDef(3, 65),    // Phòng thủ
            new OptionDef(15, 170),  // HP +
            new OptionDef(6, 7),     // T/n phòng thủ
            new OptionDef(14, 18),   // Phản đòn % (+1.8%)
            new OptionDef(26, 30),   // Kháng vật lý % (+3.0%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2648: Dây Chuyền Lõi Gia Tốc Railgun (typeequip 10)
        register(2648, (byte) 10, new OptionDef[]{
            new OptionDef(15, 180),  // HP +
            new OptionDef(14, 20),   // Phản đòn % (+2.0%)
            new OptionDef(13, 20),   // Xuyên giáp % (+2.0%)
            new OptionDef(57, 20),   // Sát thương chuẩn % (+2.0%)
            new OptionDef(53, 18),   // Miễn thương % (+1.8%)
            new OptionDef(71, 18)    // Kháng hiệu ứng % (+1.8%)
        });
        // 2649: Giáp Sắt Thép Điện Từ (typeequip 11)
        register(2649, (byte) 11, new OptionDef[]{
            new OptionDef(3, 78),    // Phòng thủ
            new OptionDef(4, 45),    // Tăng P.Thủ % (+4.5%)
            new OptionDef(15, 210),  // HP +
            new OptionDef(14, 22),   // Phản đòn % (+2.2%)
            new OptionDef(26, 32),   // Kháng vật lý % (+3.2%)
            new OptionDef(27, 25)    // Kháng phép % (+2.5%)
        });
        // 2650: Nhẫn Hai Cực Nam Châm (typeequip 12)
        register(2650, (byte) 12, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+6.0%)
            new OptionDef(14, 18),   // Phản đòn % (+1.8%)
            new OptionDef(13, 20),   // Xuyên giáp % (+2.0%)
            new OptionDef(57, 20),   // Sát thương chuẩn % (+2.0%)
            new OptionDef(63, 18),   // Giảm miễn thương % (+1.8%)
            new OptionDef(10, 20)    // Chí mạng % (+2.0%)
        });
        // 2651: Ủng Phản Lực Từ Trường (typeequip 13)
        register(2651, (byte) 13, new OptionDef[]{
            new OptionDef(3, 58),    // Phòng thủ
            new OptionDef(14, 22),   // Phản đòn % (+2.2%)
            new OptionDef(13, 18),   // Xuyên giáp % (+1.8%)
            new OptionDef(12, 20),   // Né tránh % (+2.0%)
            new OptionDef(76, 18),   // Tăng % chính xác (+1.8%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });

        // =========================================================================
        // SET 10: RỒNG ĐỘC - MAGELLAN (2652..2657)
        // Đặc tính: Độc Dịch Ăn Mòn % Máu Đối Thủ, Giảm P.Thủ Cuối, Hấp Thụ Độc
        // =========================================================================
        // 2652: Trượng Rồng Độc Hydra (typeequip 8)
        register(2652, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+7.5%)
            new OptionDef(2, 50),   // Phép thuật % (+18.0%)
            new OptionDef(48, 22),   // Sát thương % máu đ/t (+2.2%)
            new OptionDef(70, 22),   // Giảm P.Thủ cuối % (+2.2%)
            new OptionDef(57, 25),   // Sát thương chuẩn % (+2.5%)
            new OptionDef(46, 20)    // Sát thương cuối % (+2.0%)
        });
        // 2653: Mũ Sừng Quản Ngục Impel Down (typeequip 9)
        register(2653, (byte) 9, new OptionDef[]{
            new OptionDef(3, 60),    // Phòng thủ
            new OptionDef(15, 170),  // HP +
            new OptionDef(48, 15),   // Sát thương % máu đ/t (+1.5%)
            new OptionDef(27, 30),   // Kháng phép % (+3.0%)
            new OptionDef(71, 22),   // Kháng hiệu ứng % (+2.2%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2654: Dây Chuyền Đầu Lâu Bình Axit (typeequip 10)
        register(2654, (byte) 10, new OptionDef[]{
            new OptionDef(15, 180),  // HP +
            new OptionDef(48, 16),   // Sát thương % máu đ/t (+1.6%)
            new OptionDef(70, 18),   // Giảm P.Thủ cuối % (+1.8%)
            new OptionDef(58, 18),   // Hấp thụ % (+1.8%)
            new OptionDef(53, 18),   // Miễn thương % (+1.8%)
            new OptionDef(71, 22)    // Kháng hiệu ứng % (+2.2%)
        });
        // 2655: Áo Giáp Ăn Mòn Huyết Độc (typeequip 11)
        register(2655, (byte) 11, new OptionDef[]{
            new OptionDef(3, 75),    // Phòng thủ
            new OptionDef(4, 44),    // Tăng P.Thủ % (+4.4%)
            new OptionDef(15, 200),  // HP +
            new OptionDef(58, 20),   // Hấp thụ % (+2.0%)
            new OptionDef(26, 28),   // Kháng vật lý % (+2.8%)
            new OptionDef(27, 32)    // Kháng phép % (+3.2%)
        });
        // 2656: Nhẫn Nanh Rồng Độc (typeequip 12)
        register(2656, (byte) 12, new OptionDef[]{
            new OptionDef(2, 50),   // Phép thuật % (+13.0%)
            new OptionDef(48, 18),   // Sát thương % máu đ/t (+1.8%)
            new OptionDef(70, 20),   // Giảm P.Thủ cuối % (+2.0%)
            new OptionDef(58, 16),   // Hấp thụ % (+1.6%)
            new OptionDef(63, 18),   // Giảm miễn thương % (+1.8%)
            new OptionDef(10, 20)    // Chí mạng % (+2.0%)
        });
        // 2657: Ủng Độc Dịch Tử Thần (typeequip 13)
        register(2657, (byte) 13, new OptionDef[]{
            new OptionDef(3, 56),    // Phòng thủ
            new OptionDef(14, 20),   // Phản đòn % (+2.0%)
            new OptionDef(58, 15),   // Hấp thụ % (+1.5%)
            new OptionDef(19, 18),   // Tự hồi HP
            new OptionDef(76, 18),   // Tăng % chính xác (+1.8%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });

        // =========================================================================
        // SET 11: TÌNH YÊU - HANCOCK (2658..2663)
        // Đặc tính: Mê Hoặc Hóa Đá Choáng, Giảm Sức Mạnh Đối Thủ, Hồi Phục & Né Tránh
        // =========================================================================
        // 2658: Cung Tên Tình Yêu Mero Mero (typeequip 8)
        register(2658, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+8.0%)
            new OptionDef(2, 50),   // Phép thuật % (+17.0%)
            new OptionDef(75, 22),   // Tăng % choáng (+2.2%)
            new OptionDef(60, 15),   // Giảm sức mạnh (15)
            new OptionDef(10, 22),   // Chí mạng % (+2.2%)
            new OptionDef(11, 50)    // S.t chí mạng % (+5.5%)
        });
        // 2659: Vương Miện Nữ Hoàng Xà Tộc (typeequip 9)
        register(2659, (byte) 9, new OptionDef[]{
            new OptionDef(3, 55),    // Phòng thủ
            new OptionDef(15, 150),  // HP +
            new OptionDef(12, 22),   // Né tránh % (+2.2%)
            new OptionDef(75, 15),   // Tăng % choáng (+1.5%)
            new OptionDef(27, 30),   // Kháng phép % (+3.0%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2660: Chuỗi Ngọc Rắn Song Đầu (typeequip 10)
        register(2660, (byte) 10, new OptionDef[]{
            new OptionDef(15, 160),  // HP +
            new OptionDef(79, 4),    // Hồi máu cuối mỗi 10s % (+0.4%)
            new OptionDef(60, 12),   // Giảm sức mạnh (12)
            new OptionDef(53, 20),   // Miễn thương % (+2.0%)
            new OptionDef(75, 15),   // Tăng % choáng (+1.5%)
            new OptionDef(71, 18)    // Kháng hiệu ứng % (+1.8%)
        });
        // 2661: Xiêm Y Nữ Thần Xà Cốt (typeequip 11)
        register(2661, (byte) 11, new OptionDef[]{
            new OptionDef(3, 70),    // Phòng thủ
            new OptionDef(4, 40),    // Tăng P.Thủ % (+4.0%)
            new OptionDef(15, 180),  // HP +
            new OptionDef(12, 24),   // Né tránh % (+2.4%)
            new OptionDef(27, 30),   // Kháng phép % (+3.0%)
            new OptionDef(26, 25)    // Kháng vật lý % (+2.5%)
        });
        // 2662: Nhẫn Trái Tim Kim Cương Mero (typeequip 12)
        register(2662, (byte) 12, new OptionDef[]{
            new OptionDef(2, 50),   // Phép thuật % (+12.0%)
            new OptionDef(75, 16),   // Tăng % choáng (+1.6%)
            new OptionDef(79, 4),    // Hồi máu cuối mỗi 10s % (+0.4%)
            new OptionDef(60, 12),   // Giảm sức mạnh (12)
            new OptionDef(63, 18),   // Giảm miễn thương % (+1.8%)
            new OptionDef(10, 20)    // Chí mạng % (+2.0%)
        });
        // 2663: Giày Cao Gót Mê Hồn (typeequip 13)
        register(2663, (byte) 13, new OptionDef[]{
            new OptionDef(3, 52),    // Phòng thủ
            new OptionDef(12, 26),   // Né tránh % (+2.6%)
            new OptionDef(76, 20),   // Tăng % chính xác (+2.0%)
            new OptionDef(79, 4),    // Hồi máu cuối mỗi 10s % (+0.4%)
            new OptionDef(19, 18),   // Tự hồi HP
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });

        // =========================================================================
        // SET 12: PHƯỢNG HOÀNG LAM HỎA - MARCO (2664..2669)
        // Đặc tính: Bất Tử Tái Sinh, Hồi Máu Mỗi 10s, Chuyển Hóa Sát Thương Thành HP
        // =========================================================================
        // 2664: Móng Vuốt Lam Hỏa Tái Sinh (typeequip 8)
        register(2664, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+8.5%)
            new OptionDef(2, 50),   // Phép thuật % (+16.0%)
            new OptionDef(79, 6),    // Hồi máu cuối mỗi 10s % (+0.6%)
            new OptionDef(73, 18),   // C.Hóa ST thành Hp % (+1.8%)
            new OptionDef(10, 20),   // Chí mạng % (+2.0%)
            new OptionDef(57, 25)    // Sát thương chuẩn % (+2.5%)
        });
        // 2665: Mũ Lông Vũ Phượng Hoàng Bất Tử (typeequip 9)
        register(2665, (byte) 9, new OptionDef[]{
            new OptionDef(3, 60),    // Phòng thủ
            new OptionDef(15, 170),  // HP +
            new OptionDef(79, 4),    // Hồi máu cuối mỗi 10s % (+0.4%)
            new OptionDef(19, 20),   // Tự hồi HP
            new OptionDef(27, 30),   // Kháng phép % (+3.0%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2666: Dây Chuyền Giọt Lệ Phượng Hoàng (typeequip 10)
        register(2666, (byte) 10, new OptionDef[]{
            new OptionDef(15, 190),  // HP +
            new OptionDef(79, 6),    // Hồi máu cuối mỗi 10s % (+0.6%)
            new OptionDef(55, 18),   // Từ chối tử thần % (+1.8%)
            new OptionDef(73, 16),   // C.Hóa ST thành Hp % (+1.6%)
            new OptionDef(53, 20),   // Miễn thương % (+2.0%)
            new OptionDef(71, 20)    // Kháng hiệu ứng % (+2.0%)
        });
        // 2667: Giáp Cánh Lửa Tái Sinh (typeequip 11)
        register(2667, (byte) 11, new OptionDef[]{
            new OptionDef(3, 75),    // Phòng thủ
            new OptionDef(4, 44),    // Tăng P.Thủ % (+4.4%)
            new OptionDef(15, 210),  // HP +
            new OptionDef(79, 5),    // Hồi máu cuối mỗi 10s % (+0.5%)
            new OptionDef(26, 28),   // Kháng vật lý % (+2.8%)
            new OptionDef(27, 30)    // Kháng phép % (+3.0%)
        });
        // 2668: Nhẫn Ngọn Lửa Bất Tử (typeequip 12)
        register(2668, (byte) 12, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+5.5%)
            new OptionDef(79, 4),    // Hồi máu cuối mỗi 10s % (+0.4%)
            new OptionDef(73, 15),   // C.Hóa ST thành Hp % (+1.5%)
            new OptionDef(57, 20),   // Sát thương chuẩn % (+2.0%)
            new OptionDef(19, 18),   // Tự hồi HP
            new OptionDef(63, 18)    // Giảm miễn thương % (+1.8%)
        });
        // 2669: Ủng Móng Vuốt Lam Hỏa (typeequip 13)
        register(2669, (byte) 13, new OptionDef[]{
            new OptionDef(3, 55),    // Phòng thủ
            new OptionDef(12, 22),   // Né tránh % (+2.2%)
            new OptionDef(79, 4),    // Hồi máu cuối mỗi 10s % (+0.4%)
            new OptionDef(19, 20),   // Tự hồi HP
            new OptionDef(76, 18),   // Tăng % chính xác (+1.8%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });

        // =========================================================================
        // SET 13: PHẬT QUANG - SENGOKU (2670..2675)
        // Đặc tính: Kim Thân Bất Hoại, Miễn Thương Cực Đại, Giảm 90% Damage Tỷ Lệ
        // =========================================================================
        // 2670: Kim Cương Chưởng Phật Quang (typeequip 8)
        register(2670, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+9.0%)
            new OptionDef(0, 180),   // Tấn công
            new OptionDef(53, 20),   // Miễn thương % (+2.0%)
            new OptionDef(46, 22),   // Sát thương cuối % (+2.2%)
            new OptionDef(57, 25),   // Sát thương chuẩn % (+2.5%)
            new OptionDef(3, 50)     // Phòng thủ
        });
        // 2671: Mũ Sen Vàng Tháp Phật (typeequip 9)
        register(2671, (byte) 9, new OptionDef[]{
            new OptionDef(3, 70),    // Phòng thủ
            new OptionDef(15, 180),  // HP +
            new OptionDef(53, 18),   // Miễn thương % (+1.8%)
            new OptionDef(26, 32),   // Kháng vật lý % (+3.2%)
            new OptionDef(27, 32),   // Kháng phép % (+3.2%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2672: Tràng Hạt Hoàng Kim Phật Quang (typeequip 10)
        register(2672, (byte) 10, new OptionDef[]{
            new OptionDef(15, 200),  // HP +
            new OptionDef(53, 24),   // Miễn thương % (+2.4%)
            new OptionDef(54, 15),   // Giảm 90% Damage. Tỷ lệ % (+1.5%)
            new OptionDef(71, 24),   // Kháng hiệu ứng % (+2.4%)
            new OptionDef(17, 50),   // Tăng HP % (+5.0%)
            new OptionDef(18, 35)    // Tăng MP % (+3.5%)
        });
        // 2673: Kim Thân Phật Tổ Chấn Động (typeequip 11)
        register(2673, (byte) 11, new OptionDef[]{
            new OptionDef(3, 85),    // Phòng thủ
            new OptionDef(4, 50),    // Tăng P.Thủ % (+5.0%)
            new OptionDef(15, 230),  // HP +
            new OptionDef(53, 22),   // Miễn thương % (+2.2%)
            new OptionDef(26, 35),   // Kháng vật lý % (+3.5%)
            new OptionDef(27, 35)    // Kháng phép % (+3.5%)
        });
        // 2674: Nhẫn Bánh Xe Pháp Luân (typeequip 12)
        register(2674, (byte) 12, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+6.0%)
            new OptionDef(53, 18),   // Miễn thương % (+1.8%)
            new OptionDef(57, 20),   // Sát thương chuẩn % (+2.0%)
            new OptionDef(63, 18),   // Giảm miễn thương % (+1.8%)
            new OptionDef(10, 20),   // Chí mạng % (+2.0%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2675: Ủng Hoàng Kim Phật Pháp (typeequip 13)
        register(2675, (byte) 13, new OptionDef[]{
            new OptionDef(3, 62),    // Phòng thủ
            new OptionDef(14, 24),   // Phản đòn % (+2.4%)
            new OptionDef(53, 16),   // Miễn thương % (+1.6%)
            new OptionDef(76, 18),   // Tăng % chính xác (+1.8%)
            new OptionDef(19, 18),   // Tự hồi HP
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });

        // =========================================================================
        // SET 14: BÁCH THÚ KAIDO (2676..2681)
        // Đặc tính: Sinh Vật Mạnh Nhất Thế Giới & Thanh Long Cuồng Nộ, Sát Thương Cuối
        // =========================================================================
        // 2676: Chùy Gai Hassaikai Bát Quái (typeequip 8)
        register(2676, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+9.5%)
            new OptionDef(0, 210),   // Tấn công
            new OptionDef(46, 28),   // Sát thương cuối % (+2.8%)
            new OptionDef(57, 32),   // Sát thương chuẩn % (+3.2%)
            new OptionDef(13, 26),   // Xuyên giáp % (+2.6%)
            new OptionDef(11, 50)    // S.t chí mạng % (+5.5%)
        });
        // 2677: Mũ Sừng Rồng Bách Thú Haki (typeequip 9)
        register(2677, (byte) 9, new OptionDef[]{
            new OptionDef(3, 68),    // Phòng thủ
            new OptionDef(15, 190),  // HP +
            new OptionDef(5, 8),     // T/n sức mạnh
            new OptionDef(7, 7),     // T/n thể lực
            new OptionDef(26, 30),   // Kháng vật lý % (+3.0%)
            new OptionDef(56, 30)    // Máu cuối % (+3.0%)
        });
        // 2678: Dây Chuyền Long Hỏa Châu (typeequip 10)
        register(2678, (byte) 10, new OptionDef[]{
            new OptionDef(15, 210),  // HP +
            new OptionDef(46, 20),   // Sát thương cuối % (+2.0%)
            new OptionDef(56, 30),   // Máu cuối % (+3.0%)
            new OptionDef(57, 22),   // Sát thương chuẩn % (+2.2%)
            new OptionDef(53, 18),   // Miễn thương % (+1.8%)
            new OptionDef(71, 18)    // Kháng hiệu ứng % (+1.8%)
        });
        // 2679: Giáp Vảy Thanh Long Cuồng Nộ (typeequip 11)
        register(2679, (byte) 11, new OptionDef[]{
            new OptionDef(3, 82),    // Phòng thủ
            new OptionDef(4, 48),    // Tăng P.Thủ % (+4.8%)
            new OptionDef(15, 240),  // HP +
            new OptionDef(56, 30),   // Máu cuối % (+3.0%)
            new OptionDef(26, 34),   // Kháng vật lý % (+3.4%)
            new OptionDef(27, 28)    // Kháng phép % (+2.8%)
        });
        // 2680: Nhẫn Móng Vuốt Long Hỏa (typeequip 12)
        register(2680, (byte) 12, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+6.5%)
            new OptionDef(13, 22),   // Xuyên giáp % (+2.2%)
            new OptionDef(46, 18),   // Sát thương cuối % (+1.8%)
            new OptionDef(57, 22),   // Sát thương chuẩn % (+2.2%)
            new OptionDef(63, 20),   // Giảm miễn thương % (+2.0%)
            new OptionDef(10, 20)    // Chí mạng % (+2.0%)
        });
        // 2681: Ủng Sấm Sét Bát Quái Chân Long (typeequip 13)
        register(2681, (byte) 13, new OptionDef[]{
            new OptionDef(3, 60),    // Phòng thủ
            new OptionDef(14, 24),   // Phản đòn % (+2.4%)
            new OptionDef(5, 7),     // T/n sức mạnh
            new OptionDef(76, 18),   // Tăng % chính xác (+1.8%)
            new OptionDef(19, 18),   // Tự hồi HP
            new OptionDef(56, 30)    // Máu cuối % (+3.0%)
        });

        // =========================================================================
        // SET 15: HỔ RĂNG KIẾM - WHO'S WHO (2682..2687)
        // Đặc tính: Sát Thủ Tiền Sử, Chí Mạng & Sát Thương Chí Mạng Cực Cao, Chính Xác
        // =========================================================================
        // 2682: Song Dao Nanh Kiếm Cổ Đại (typeequip 8)
        register(2682, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+9.0%)
            new OptionDef(0, 170),   // Tấn công
            new OptionDef(10, 28),   // Chí mạng % (+2.8%)
            new OptionDef(11, 50),   // S.t chí mạng % (+6.5%)
            new OptionDef(13, 24),   // Xuyên giáp % (+2.4%)
            new OptionDef(76, 24)    // Tăng % chính xác (+2.4%)
        });
        // 2683: Mặt Nạ Đầu Lâu Hổ Răng Kiếm (typeequip 9)
        register(2683, (byte) 9, new OptionDef[]{
            new OptionDef(3, 54),    // Phòng thủ
            new OptionDef(15, 150),  // HP +
            new OptionDef(10, 20),   // Chí mạng % (+2.0%)
            new OptionDef(9, 7),     // T/n nhanh nhẹn
            new OptionDef(76, 20),   // Tăng % chính xác (+2.0%)
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });
        // 2684: Dây Chuyền Nanh Hổ Phù Điêu (typeequip 10)
        register(2684, (byte) 10, new OptionDef[]{
            new OptionDef(15, 160),  // HP +
            new OptionDef(10, 20),   // Chí mạng % (+2.0%)
            new OptionDef(11, 50),   // S.t chí mạng % (+5.0%)
            new OptionDef(52, 22),   // Giảm phản đòn đ/t % (+2.2%)
            new OptionDef(53, 16),   // Miễn thương % (+1.6%)
            new OptionDef(76, 20)    // Tăng % chính xác (+2.0%)
        });
        // 2685: Giáp Da Thú Săn Mồi Tiền Sử (typeequip 11)
        register(2685, (byte) 11, new OptionDef[]{
            new OptionDef(3, 70),    // Phòng thủ
            new OptionDef(4, 40),    // Tăng P.Thủ % (+4.0%)
            new OptionDef(15, 180),  // HP +
            new OptionDef(12, 24),   // Né tránh % (+2.4%)
            new OptionDef(26, 26),   // Kháng vật lý % (+2.6%)
            new OptionDef(27, 24)    // Kháng phép % (+2.4%)
        });
        // 2686: Nhẫn Hổ Phách Nanh Vuốt (typeequip 12)
        register(2686, (byte) 12, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+6.0%)
            new OptionDef(10, 25),   // Chí mạng % (+2.5%)
            new OptionDef(11, 50),   // S.t chí mạng % (+5.5%)
            new OptionDef(13, 20),   // Xuyên giáp % (+2.0%)
            new OptionDef(52, 20),   // Giảm phản đòn đ/t % (+2.0%)
            new OptionDef(63, 18)    // Giảm miễn thương % (+1.8%)
        });
        // 2687: Ủng Nanh Vuốt Báo Đốm Cổ Đại (typeequip 13)
        register(2687, (byte) 13, new OptionDef[]{
            new OptionDef(3, 50),    // Phòng thủ
            new OptionDef(12, 28),   // Né tránh % (+2.8%)
            new OptionDef(76, 26),   // Tăng % chính xác (+2.6%)
            new OptionDef(10, 18),   // Chí mạng % (+1.8%)
            new OptionDef(9, 7),     // T/n nhanh nhẹn
            new OptionDef(56, 25)    // Máu cuối % (+2.5%)
        });

        // =========================================================================
        // SET 16: T-REX BẠO CHÚA - QUEEN (2688..2693)
        // Đặc tính: Titan Tiền Sử Bạo Chúa, Máu Khổng Lồ, Nghiền Nát Phòng Ngự
        // =========================================================================
        // 2688: Rìu Chiến Khủng Long Cơ Khí (typeequip 8)
        register(2688, (byte) 8, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+9.2%)
            new OptionDef(0, 190),   // Tấn công
            new OptionDef(46, 25),   // Sát thương cuối % (+2.5%)
            new OptionDef(57, 28),   // Sát thương chuẩn % (+2.8%)
            new OptionDef(70, 25),   // Giảm P.Thủ cuối % (+2.5%)
            new OptionDef(13, 24)    // Xuyên giáp % (+2.4%)
        });
        // 2689: Đầu Lâu Bạo Chúa T-Rex Tối Thượng (typeequip 9)
        register(2689, (byte) 9, new OptionDef[]{
            new OptionDef(3, 68),    // Phòng thủ
            new OptionDef(15, 190),  // HP +
            new OptionDef(7, 8),     // T/n thể lực
            new OptionDef(5, 7),     // T/n sức mạnh
            new OptionDef(26, 30),   // Kháng vật lý % (+3.0%)
            new OptionDef(56, 30)    // Máu cuối % (+3.0%)
        });
        // 2690: Dây Chuyền Trứng Khủng Long Hóa Thạch (typeequip 10)
        register(2690, (byte) 10, new OptionDef[]{
            new OptionDef(15, 210),  // HP +
            new OptionDef(56, 30),   // Máu cuối % (+3.0%)
            new OptionDef(46, 18),   // Sát thương cuối % (+1.8%)
            new OptionDef(70, 20),   // Giảm P.Thủ cuối % (+2.0%)
            new OptionDef(53, 18),   // Miễn thương % (+1.8%)
            new OptionDef(71, 18)    // Kháng hiệu ứng % (+1.8%)
        });
        // 2691: Giáp Titan Khủng Long Cổ Đại (typeequip 11)
        register(2691, (byte) 11, new OptionDef[]{
            new OptionDef(3, 80),    // Phòng thủ
            new OptionDef(4, 46),    // Tăng P.Thủ % (+4.6%)
            new OptionDef(15, 230),  // HP +
            new OptionDef(56, 30),   // Máu cuối % (+3.0%)
            new OptionDef(26, 32),   // Kháng vật lý % (+3.2%)
            new OptionDef(14, 20)    // Phản đòn % (+2.0%)
        });
        // 2692: Nhẫn Mắt Quỷ Khủng Long Bạo Chúa (typeequip 12)
        register(2692, (byte) 12, new OptionDef[]{
            new OptionDef(1, 50),    // Tăng tấn công % (+6.2%)
            new OptionDef(57, 22),   // Sát thương chuẩn % (+2.2%)
            new OptionDef(70, 18),   // Giảm P.Thủ cuối % (+1.8%)
            new OptionDef(63, 20),   // Giảm miễn thương % (+2.0%)
            new OptionDef(10, 20),   // Chí mạng % (+2.0%)
            new OptionDef(46, 16)    // Sát thương cuối % (+1.6%)
        });
        // 2693: Ủng Móng Vuốt Nghiền Nát Địa Cầu (typeequip 13)
        register(2693, (byte) 13, new OptionDef[]{
            new OptionDef(3, 60),    // Phòng thủ
            new OptionDef(14, 24),   // Phản đòn % (+2.4%)
            new OptionDef(7, 7),     // T/n thể lực
            new OptionDef(76, 18),   // Tăng % chính xác (+1.8%)
            new OptionDef(19, 18),   // Tự hồi HP
            new OptionDef(56, 30)    // Máu cuối % (+3.0%)
        });
    }

    private static void register(int id, byte typeEquip, OptionDef[] options) {
        THAN_TRANG_MAP.put(id, options);
        TYPE_EQUIP_MAP.put(id, typeEquip);
    }

    public static boolean isThanTrang(int templateId) {
        return THAN_TRANG_MAP.containsKey(templateId);
    }

    public static byte getTypeEquip(int templateId) {
        Byte b = TYPE_EQUIP_MAP.get(templateId);
        if (b != null) return b;
        if (templateId >= 2604 && templateId <= 2693) {
            int piece = (templateId - 2604) % 6;
            return (byte) (8 + piece);
        }
        return 8;
    }

    public static int getSetIndex(int templateId) {
        if (templateId >= 2604 && templateId <= 2693) {
            return (templateId - 2604) / 6; // Set 1..15 (index 0..14)
        }
        return -1;
    }

    public static String getSetName(int setIndex) {
        if (setIndex >= 0 && setIndex < SET_NAMES.length) {
            return SET_NAMES[setIndex];
        }
        return "Thần Trang Cực Phẩm";
    }

    public static boolean isPercentOption(int optionId) {
        if (ItemOptionTemplate.ENTRYS != null && optionId >= 0 && optionId < ItemOptionTemplate.ENTRYS.size()) {
            ItemOptionTemplate tpl = ItemOptionTemplate.ENTRYS.get(optionId);
            if (tpl != null && tpl.percent == 1) return true;
        }
        switch (optionId) {
            case 1: case 2: case 4: case 10: case 11: case 12: case 13: case 14:
            case 17: case 18: case 23: case 25: case 26: case 27: case 46: case 48:
            case 49: case 50: case 52: case 53: case 54: case 55: case 56: case 57:
            case 58: case 59: case 63: case 64: case 67: case 68: case 69: case 70:
            case 71: case 72: case 73: case 75: case 76: case 79: case 80:
                return true;
            default:
                return false;
        }
    }

    public static List<Option> getThanTrangOptions(int templateId) {
        OptionDef[] defs = THAN_TRANG_MAP.get(templateId);
        if (defs == null) return Collections.emptyList();
        List<Option> list = new ArrayList<>();
        for (OptionDef def : defs) {
            int p = def.param;
            if (isPercentOption(def.optionId) && p > 50) {
                p = 50; // Cap tỉ lệ % tối đa 5%
            }
            list.add(new Option(def.optionId, p));
        }
        return list;
    }

    public static List<ThanTrangOptionRange> getThanTrangOptionRanges(int templateId) {
        OptionDef[] defs = THAN_TRANG_MAP.get(templateId);
        if (defs == null) return Collections.emptyList();
        List<ThanTrangOptionRange> ranges = new ArrayList<>();
        for (OptionDef def : defs) {
            int min, max;
            int base = def.param;
            if (isPercentOption(def.optionId)) {
                if (base > 50) base = 50;
                min = Math.max(1, Math.min(base, (base * 75) / 100));
                max = Math.max(base, Math.min(55, (base * 125) / 100));
            } else if (def.optionId >= 5 && def.optionId <= 9) { // Điểm tiềm năng: base 5..6, max 7..8
                min = Math.max(2, base - 2);
                max = base + 2;
            } else if (base <= 10) { // Giá trị nhỏ khác
                min = Math.max(1, base - 2);
                max = base + 2;
            } else { // Chỉ số flat (Tấn công, HP, P.Thủ)
                min = Math.max(1, (base * 85) / 100);
                max = Math.max(min + 1, (base * 115) / 100);
            }
            ranges.add(new ThanTrangOptionRange(def.optionId, min, max));
        }
        return ranges;
    }

    public static OptionDef[] getOptionDefs(int templateId) {
        return THAN_TRANG_MAP.get(templateId);
    }

    /**
     * Tạo Thần Trang rơi từ Siêu Trùm (90 ID chuẩn từ 2604 đến 2693, tương đương 15 bộ).
     * Thần trang cùi nhất (bộ cùi và phẩm cùi) có tỉ lệ xuất hiện thấp hơn.
     */
    public static Item_wear createRandomSuperBossDrop() {
        List<Integer> pool = new ArrayList<>();
        // 90 ID Thần Trang: 2604..2693
        // Giảm tỉ lệ xuất hiện của các bộ cùi nhất (Set 1 Dung Nham, Set 2 Hàn Băng: weight = 3)
        // Các bộ trung bình (Set 3..8: weight = 6), các bộ cực phẩm (Set 9..15: weight = 9)
        for (int i = 2604; i <= 2693; i++) {
            if (!THAN_TRANG_MAP.containsKey(i)) continue;
            int setIdx = (i - 2604) / 6;
            int weight = (setIdx <= 1) ? 3 : (setIdx <= 7 ? 6 : 9);
            for (int w = 0; w < weight; w++) {
                pool.add(i);
            }
        }
        if (pool.isEmpty()) {
            for (int i = 2604; i <= 2693; i++) pool.add(i);
        }
        int chosenId = pool.get(core.ZUtil.random(pool.size()));
        
        // Random phẩm màu: Giảm mạnh tỉ lệ phẩm cùi nhất (Xanh - 1) từ 50% xuống chỉ còn 15%
        // Tỉ lệ: Xanh 15%, Vàng 50%, Tím 25%, Cam 10%
        int randColorRoll = core.ZUtil.random(100);
        byte color;
        if (randColorRoll < 15) {
            color = 1; // Xanh (Cùi nhất - giảm xuất hiện)
        } else if (randColorRoll < 65) {
            color = 2; // Vàng (50%)
        } else if (randColorRoll < 90) {
            color = 3; // Tím (25%)
        } else {
            color = 4; // Cam (10%)
        }

        Item_wear it = new Item_wear();
        it.setup_template_by_id(chosenId);
        it.color = color;
        it.levelUp = 0;
        it.typelock = 0; // Mặc định không khóa để có thể giao dịch & treo chợ
        it.numLoKham = 0;
        it.numHoleDaDuc = 0;
        it.mdakham = new short[0];
        it.isHoanMy = 0;
        it.valueKichAn = -1;
        it.valueChetac = 0;

        // Chỉ số cân bằng scale theo phẩm màu
        double multiplier = switch (color) {
            case 1 -> 0.85; // Xanh
            case 2 -> 1.00; // Vàng
            case 3 -> 1.15; // Tím
            case 4 -> 1.30; // Cam
            default -> 1.00;
        };

        List<Option> baseOps = getThanTrangOptions(chosenId);
        it.option_item = new ArrayList<>();
        for (Option op : baseOps) {
            int val = (int) Math.round(op.getParam() * multiplier);
            if (isPercentOption(op.id) && val > 50) {
                val = 50;
            }
            if (val < 1) val = 1;
            it.option_item.add(new Option(op.id, val));
        }

        return it;
    }
}
