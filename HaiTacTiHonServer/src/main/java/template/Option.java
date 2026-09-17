package template;

public class Option {
    public static int[] PAR_PER_LEVELUP = new int[] {100, 110, 120, 130, 140, 150, 170, 190, 210,
            230, 250, 270, 290, 310, 330, 350, 380, 420, 470, 530, 600};
    public static int[] PAR_PER_LEVELUP_THAN_TRANG = new int[] {100, 107, 114, 121, 128, 135, 142, 150, 158, 166, 175,
            182, 190, 197, 205, 212, 220, 227, 235, 242, 250};
    public static int[] PAR_PER_DIAL = new int[] {100, 160, 180, 210, 250, 300};
    public short id;
    public int param;

    public Option(int id, int param) {
        this.id = (short) id;
        this.param = param;
    }

    public int getParam() {
        return param;
    }

    public int getParam(int type, int tier, int isHoanMy) {
        int result = param;
        if (tier < 0) {
            tier = 0;
        }
        switch (type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                if (tier > 15) {
                    tier = 15;
                }
                if (this.id >= 5 && this.id <= 9) {
                    // Option 5..9: Tiềm năng (Sức mạnh, Phòng thủ, Thể lực, Tinh thần, Nhanh nhẹn)
                    result = param + (tier / 4);
                } else if (this.id == 10 || this.id == 12 || this.id == 14) {
                    // Đồ thường (type 0..5): các option tỷ lệ % chiến đấu (Chí mạng 10, Né tránh 12, Phản đòn 14)
                    // Tăng trưởng mượt mà theo cấp cường hóa (+5% mỗi cấp, ở +20 Hoàn mỹ đạt 2.2x base)
                    long mult = (long) result * (100L + tier * 5L) * ((isHoanMy == 1) ? 110L : 100L);
                    result = (int) (mult / 10_000L);
                } else if (this.id < 28 || this.id >= 46) {
                    int safeTier = (tier < PAR_PER_LEVELUP.length) ? tier : PAR_PER_LEVELUP.length - 1;
                    long mult = (long) result * PAR_PER_LEVELUP[safeTier] * ((isHoanMy == 1) ? 110L : 100L);
                    result = (int) (mult / 10_000L);
                }
                break;
            }
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15: {
                // Thần Trang (typeequip 8..15):
                // 1. Tiềm năng (Option 5..9): tăng param + (tier / 2) -> ở +20 tăng thêm +10 điểm tiềm năng
                //    (gấp đôi đồ thường, mạnh mẽ xứng tầm Thần Trang và hoàn toàn kiểm soát được chỉ số).
                // 2. Dòng % và flat: Hệ số cường hóa riêng từ 100% (+0) lên tối đa 250% (+20).
                if (this.id >= 5 && this.id <= 9) {
                    result = param + (tier / 2);
                } else if (this.id < 28 || this.id >= 46) {
                    int safeTier = (tier < PAR_PER_LEVELUP_THAN_TRANG.length) ? tier : PAR_PER_LEVELUP_THAN_TRANG.length - 1;
                    long mult = (long) result * PAR_PER_LEVELUP_THAN_TRANG[safeTier] * ((isHoanMy == 1) ? 110L : 100L);
                    result = (int) (mult / 10_000L);
                }
                break;
            }
            case 6: { // heart (Trang bị Tim)
                if (this.id == 56) {
                    // Option 56: Máu cuối % (Chỉ số tăng HP cốt lõi của Tim) - chuẩn gốc: param + 50 * tier
                    // Cấp 0 = 100 (+10.0% HP), Cấp 50 = 2600 (+260% HP), Cấp 100 = 5100 (+510% HP), Cấp 110 = 5600 (+560% HP)
                    result = param + 50 * tier;
                } else if (this.id == 79) {
                    // Option 79: Hồi máu cuối mỗi 10s % (Mở khóa tại cấp 100 với base param = 2)
                    // Nerf chuẩn chống bất tử: Cấp 100 = 2%, cấp 110 = 12% HP hồi mỗi 10s
                    result = param + (tier >= 100 ? (tier - 100) : 0);
                } else if (this.id >= 5 && this.id <= 9) {
                    // Option Tiềm năng ở Tim: Tăng nhẹ theo cấp (tối đa +5 điểm ở cấp 100), chống lỗi cộng hàng nghìn điểm
                    result = param + (tier / 20);
                } else if (this.id == 1 || this.id == 2 || this.id == 4 || this.id == 10 || this.id == 11
                        || this.id == 12 || this.id == 13 || this.id == 14 || this.id == 17 || this.id == 18
                        || this.id == 25 || this.id == 26 || this.id == 27 || (this.id >= 46 && this.id <= 80)) {
                    // Các option mang tính % phụ khác: tăng nhẹ theo cấp tim
                    result = param + (tier / 2);
                } else {
                    // Option thuần chỉ số flat (ATK, DEF, HP flat...)
                    result = param + 50 * tier;
                }
                break;
            }
            case 7: { // dial
                int safeTier = (tier < PAR_PER_DIAL.length) ? tier : PAR_PER_DIAL.length - 1;
                result = (result * PAR_PER_DIAL[safeTier]) / 100;
                break;
            }   
//            case 8: { // heart
//                if (this.id < 28) {
//                    result = param + 250 * tier;
//                }
//                break;
//            }
        }
        return result;
    }

    public void setParam(int param) {
        this.param = param;
    }
}
