package core;

import database.DbManager;
import model.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * VipManager — Quản lý 20 Cấp VIP (VIP 0 -> 20) đồng bộ 100% với Web htdocs
 * Cấu hình mốc nạp và danh hiệu chuẩn theo C:/xampp/htdocs/config/functions.php
 */
public class VipManager {

    /**
     * 20 Mốc nạp chuẩn VNĐ (0 -> 20)
     */
    public static final long[] VIP_MILESTONES = {
        0L,             // VIP 0:  0 VNĐ
        20_000L,        // VIP 1:  20k VNĐ
        50_000L,        // VIP 2:  50k VNĐ
        100_000L,       // VIP 3:  100k VNĐ
        200_000L,       // VIP 4:  200k VNĐ
        350_000L,       // VIP 5:  350k VNĐ
        500_000L,       // VIP 6:  500k VNĐ
        800_000L,       // VIP 7:  800k VNĐ
        1_200_000L,     // VIP 8:  1.2M VNĐ
        1_800_000L,     // VIP 9:  1.8M VNĐ
        2_500_000L,     // VIP 10: 2.5M VNĐ
        3_500_000L,     // VIP 11: 3.5M VNĐ
        5_000_000L,     // VIP 12: 5.0M VNĐ
        7_500_000L,     // VIP 13: 7.5M VNĐ
        11_000_000L,    // VIP 14: 11.0M VNĐ
        16_000_000L,    // VIP 15: 16.0M VNĐ
        23_000_000L,    // VIP 16: 23.0M VNĐ
        32_000_000L,    // VIP 17: 32.0M VNĐ
        45_000_000L,    // VIP 18: 45.0M VNĐ
        65_000_000L,    // VIP 19: 65.0M VNĐ
        100_000_000L    // VIP 20: 100.0M VNĐ
    };

    /**
     * 21 Danh hiệu Hải tặc chuẩn theo Web
     */
    public static final String[] VIP_TITLES = {
        "Tân Thủ Hải Tặc (Novice)",
        "Thuyền Viên Tập Sự (Rookie)",
        "Thủy Thủ Đoàn (Crewmate)",
        "Hoa Tiêu Biển Khơi (Navigator)",
        "Xạ Thủ Thiện Xạ (Sniper)",
        "Đầu Bếp Hải Tặc (Chef)",
        "Kiếm Sĩ Siêu Phàm (Swordsman)",
        "Bác Sĩ Tài Ba (Doctor)",
        "Thợ Đóng Tàu Huyền Thoại (Shipwright)",
        "Thuyền Phó Dũng Mãnh (First Mate)",
        "Thuyền Trưởng Vĩ Đại (Captain)",
        "Siêu Tân Tinh (Supernova)",
        "Thất Vũ Hải (Warlord of Sea)",
        "Đô Đốc Hải Quân (Admiral)",
        "Thủy Sư Đô Đốc (Fleet Admiral)",
        "Tứ Hoàng Biển Cả (Yonko)",
        "Ngũ Lão Tinh (Five Elders)",
        "Long Tinh Thần Thánh (Celestial Dragon)",
        "Đấng Tối Cao Biển Cả (Imu Sama)",
        "Chúa Tể Tân Thế Giới (Lord of Sea)",
        "Đại Vua Hải Tặc Tối Thượng (Pirate King God)"
    };

    /**
     * Tính cấp VIP (0 đến 20) dựa theo tổng nạp VNĐ
     */
    public static int getVipByRecharge(long tongnap) {
        if (tongnap <= 0) return 0;
        for (int v = 20; v >= 1; v--) {
            if (tongnap >= VIP_MILESTONES[v]) {
                return v;
            }
        }
        return 0;
    }

    /**
     * Lấy Danh hiệu Hải tặc của cấp VIP
     */
    public static String getVipTitle(int vipLevel) {
        if (vipLevel < 0) vipLevel = 0;
        if (vipLevel >= VIP_TITLES.length) vipLevel = VIP_TITLES.length - 1;
        return VIP_TITLES[vipLevel];
    }

    /**
     * Tính % EXP Bonus dựa theo cấp VIP (VIP 1 = +5%, VIP 10 = +50%, VIP 20 = +100%)
     */
    public static int getVipExpBonusPercent(int vipLevel) {
        if (vipLevel <= 0) return 0;
        if (vipLevel > 20) vipLevel = 20;
        return vipLevel * 5;
    }

    /**
     * Tính % Drop Bonus dựa theo cấp VIP (VIP 1 = +2%, VIP 10 = +20%, VIP 20 = +50%)
     */
    public static int getVipDropBonusPercent(int vipLevel) {
        if (vipLevel <= 0) return 0;
        if (vipLevel <= 10) return vipLevel * 2;
        return 20 + (vipLevel - 10) * 3;
    }

    /**
     * Đồng bộ và cập nhật VIP cho Player từ account tongnap và account vip
     */
    public static int syncAndGetPlayerVip(Player p) {
        if (p == null) return 0;
        int storedVip = p.vip;
        int tongnap = p.getTongnap();
        int calculatedVip = getVipByRecharge(tongnap);
        int finalVip = Math.max(storedVip, calculatedVip);
        finalVip = Math.max(0, Math.min(20, finalVip));

        if (p.vip != finalVip) {
            p.setVip(finalVip);
        }
        return finalVip;
    }

    /**
     * Cập nhật VIP trong cơ sở dữ liệu cho account
     */
    public static void updateAccountVipInDb(int accountId, int newVip, long tongnap) {
        if (accountId <= 0) return;
        int calcVip = getVipByRecharge(tongnap);
        int finalVip = Math.max(newVip, calcVip);
        finalVip = Math.max(0, Math.min(20, finalVip));

        try (Connection conn = DbManager.gI().getConnect();
             PreparedStatement ps = conn.prepareStatement("UPDATE `account` SET `vip` = ? WHERE `id` = ?")) {
            ps.setInt(1, finalVip);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
