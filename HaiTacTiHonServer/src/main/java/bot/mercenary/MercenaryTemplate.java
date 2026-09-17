package bot.mercenary;

import java.util.*;
import core.ZUtil;

/**
 * MercenaryTemplate -- Cau hinh mau Linh Danh Thue chuan One Piece.
 *
 * 3 PHE:
 *   1. Hai Quan (Navy) -- Phong thu tot, kiem soat, do ben cao
 *   2. Hai Tac (Pirates) -- Sat thuong khung, bao kich, toc do cao
 *   3. Quan Cach Mang (Revolutionary) -- Da nang, nhieu hieu ung, linh hoat
 *
 * 4 PHAM CAP (TIER):
 *   - TIER_FULL (4.0): Full 8 mon Hoan My + Kham da Thuong + Kich an Cap 5 (Lv 100-110, Skill 30)
 *   - TIER_NEAR_FULL (3.0): Full 6 mon Hoan My + Kham da (Lv 85-99, Skill 25-26)
 *   - TIER_HIGH (2.0): Trang bi cao cap, co mon Hoan My (Lv 55-84, Skill 20-23)
 *   - TIER_MID (1.0): Trang bi tieu chuan phu hop cap do (Lv 1-54, Skill 10-18)
 *
 * BANG GIA CAN BANG & THOI GIAN:
 *   - Beri: 10M - 250M Beri (Thoi gian 60 - 120 phut)
 *   - Ruby: 60 - 1.500 Ruby (Thoi gian 120 - 240 phut)
 *   - Extol: 6.000 - 150.000 Extol (Thoi gian 180 - 360 phut)
 */
public class MercenaryTemplate {

    public static final int TYPE_HAI_QUAN  = 1;
    public static final int TYPE_HAI_TAC   = 2;
    public static final int TYPE_CACH_MANG = 3;

    public static final String[] FACTION_NAME = {
        "", "Hai Quan", "Hai Tac", "Quan Cach Mang"
    };

    public static final double TIER_FULL      = 4.0; // Full Hoan My + Kham da + Kich an 5
    public static final double TIER_NEAR_FULL = 3.0; // Gan Full Hoan My + Kham da
    public static final double TIER_HIGH      = 2.0; // Cao cap
    public static final double TIER_MID       = 1.0; // Trung cap

    public static final int CURRENCY_EXTOL = 1; // Extol
    public static final int CURRENCY_RUBY  = 2; // Ruby
    public static final int CURRENCY_BERI  = 3; // Beri

    public static final String[] CURRENCY_NAME = {"", "Extol", "Ruby", "Beri"};

    // Danh sach Trai Ac Quy theo phan cap tien
    public static final String[] TAQ_EXTOL = {
        "Trai Nham Thach (Magma)", "Trai Bang Gia (Ice)",
        "Trai Rong Toi Thuong (Dragon)", "Trai Phuong Hoang (Phoenix)",
        "Trai Anh Sang (Glint)",   "Trai Bong Toi (Dark)",
        "Trai Chan Dong (Gura)",      "Trai Cao Su Thuc Tinh (Nika)"
    };

    public static final String[] TAQ_RUBY = {
        "Trai Lua (Flame)",  "Trai Khoi (Smoke)",
        "Trai Cat (Sand)",    "Trai Set (Rumble)",
        "Trai Mochi (Mochi)", "Trai Rao Chan (Barrier)"
    };

    public static final String[] TAQ_BERI = {
        "Trai Lo Xo (Spring)", "Trai Trong Luong (Weight)",
        "Trai Boc Pha (Bomb)",   "Trai Kilo (Kilo)"
    };

    public static double getMaxAllowedTier(int level) {
        if (level < 50) return 1.2;
        if (level < 70) return 2.0;
        if (level < 90) return 3.0;
        return 4.0;
    }

    /**
     * Mau Linh Danh Thue
     */
    public static class MercEntry {
        public final int id;
        public final String name;
        public final int faction;       // 1=Hai Quan, 2=Hai Tac, 3=Cach Mang
        public final byte clazz;        // 1=Luffy/Vo, 2=Zoro/Kiem, 3=Sanji/Cuoc, 4=Nami/Phap, 5=Usopp/Sung
        public final int minLv;
        public final int maxLv;
        public double tier;       // Double Seed (1.0 .. 4.0+)
        public int skillLv;       // Level skill (10 - 30)
        public boolean hasHoanMy;
        public boolean hasKhamDa;
        public boolean hasKichAn;

        // Gia & Thoi gian (phut)
        public final int priceExtol;
        public final int durationExtol;
        public final int priceRuby;
        public final int durationRuby;
        public final long priceBeri;
        public final int durationBeri;

        public MercEntry(int id, String name, int faction, byte clazz,
                         int minLv, int maxLv, double tier, int skillLv,
                         boolean hasHoanMy, boolean hasKhamDa, boolean hasKichAn,
                         int priceExtol, int durationExtol,
                         int priceRuby, int durationRuby,
                         long priceBeri, int durationBeri) {
            this.id = id;
            this.name = name;
            this.faction = faction;
            this.clazz = clazz;
            this.minLv = minLv;
            this.maxLv = maxLv;
            this.tier = tier;
            this.skillLv = skillLv;
            this.hasHoanMy = hasHoanMy;
            this.hasKhamDa = hasKhamDa;
            this.hasKichAn = hasKichAn;
            this.priceExtol = priceExtol;
            this.durationExtol = durationExtol;
            this.priceRuby = priceRuby;
            this.durationRuby = durationRuby;
            this.priceBeri = priceBeri;
            this.durationBeri = durationBeri;
        }

        public String getTierStars() {
            if (tier >= 3.5) return String.format("%.2f [THẦN THOẠI] Thần Trang & Đá Thần Thoại", tier);
            if (tier >= 2.5) return String.format("%.2f [HOÀN MỸ] Đồ Hoàn Mỹ & Đá Cấp 6", tier);
            if (tier >= 1.5) return String.format("%.2f [CAO CẤP] Đồ Tím/Cam & Đá Cấp 4-6", tier);
            return String.format("%.2f [TRUNG CẤP] Đồ Chuẩn Cấp", tier);
        }

        public String getRandomTAQ(int currency) {
            switch (currency) {
                case CURRENCY_EXTOL:
                    return TAQ_EXTOL[ZUtil.random(TAQ_EXTOL.length)];
                case CURRENCY_RUBY:
                    return TAQ_RUBY[ZUtil.random(TAQ_RUBY.length)];
                default:
                    return TAQ_BERI[ZUtil.random(TAQ_BERI.length)];
            }
        }

        /**
         * Dinh dang thong tin chi so & trang bi
         */
        public static double getMaxAllowedTier(int level) {
            if (level < 50) return 1.2;
            if (level < 70) return 2.0;
            if (level < 90) return 3.0;
            return 4.0;
        }

        /**
         * Dinh dang thong tin chi so & trang bi
         */
        public String getStatsInfo() {
            StringBuilder sb = new StringBuilder();
            sb.append("+========================+\n");
            sb.append("  ").append(name).append("\n");
            sb.append("+========================+\n");
            sb.append("- Phe: ").append(FACTION_NAME[faction]).append("\n");
            sb.append("- Cấp độ: Lv.").append(minLv).append(" - ").append(maxLv).append(" (Không vượt quá cấp Chủ Nhân)\n");
            sb.append("- Phẩm chất: ").append(getTierStars()).append("\n");
            sb.append("- Trang bị: ").append(tier >= 3.5 ? "Full 6 Món Thần Trang Thần Thoại" : (hasHoanMy ? "Full 6 Món Hoàn Mỹ" : "Trang bị Chuẩn Cấp")).append("\n");
            sb.append("- Cường hóa trang bị: +").append(tier >= 3.5 ? 16 : tier >= 2.5 ? 14 : tier >= 1.5 ? 10 : 6).append("\n");
            sb.append("- Độ Hoàn Mỹ: ").append(hasHoanMy || tier >= 2.0 ? "Có (Đồ Hoàn Mỹ)" : "Không").append("\n");
            sb.append("- Khảm đá: ").append(tier >= 3.5 ? "Full Đá Thần Thoại (+320 Công/HP, +100 Thủ, +150 Bạo/Xuyên)" : (tier >= 2.5 ? "Full Đá Cấp 6 (+220 Công)" : "Đá Cấp 1-3")).append("\n");
            sb.append("- Kích ẩn trang bị: ").append(hasKichAn || tier >= 2.5 ? "Kích Hoạt Ấn Đồng Bộ" : "Không").append("\n");
            sb.append("- Trái Ác Quỷ: ").append(tier >= 3.5 ? "Trái Bá Vương Thức Tỉnh (Cấp Quỷ 5)" : (tier >= 2.5 ? "Trái Thượng Cấp (Cấp Quỷ 3-4)" : "Trái Phổ Thông (Cấp Quỷ 1-2)")).append("\n");
            sb.append("- Sát thương bổ sung: +").append(String.format("%.1f", tier * 5.0)).append("%\n");
            sb.append("- Hưởng EXP chiến đấu: 25% EXP quái chia sẻ cho Chủ Thuê\n");
            sb.append("- Phạm vi bảo vệ: Hỗ trợ trong phạm vi 350px quanh Chủ Thuê\n");
            return sb.toString();
        }

        /**
         * Dinh dang thong tin ky nang & Trai Ac Quy
         */
        public String getSkillInfo() {
            StringBuilder sb = new StringBuilder();
            sb.append("+========================+\n");
            sb.append("  KỸ NĂNG & TRÁI ÁC QUỶ\n");
            sb.append("+========================+\n");
            sb.append("- Cấp Kỹ năng: Cấp ").append(skillLv).append(" / 30\n");
            sb.append("- Chiêu chủ động: Kỹ năng Class chính + Haki hỗ trợ\n");
            sb.append("- Skill Nội Tại: Passive hỗ trợ bạn đồng hành\n");
            sb.append("- Hồi chiêu đòn đánh: 1.300ms (Cân bằng người chơi)\n\n");
            sb.append("PHÂN CẤP TRÁI ÁC QUỶ THEO TIỀN THUÊ & PHẨM CHẤT:\n");
            sb.append("- Extol / Tier Thần Thoại: Trái Bá Vương Thức Tỉnh (Nika, Dragon, Phoenix, Magma, Gura, Dark...) - Cấp Quỷ 5\n");
            sb.append("- Ruby / Tier Cao Cấp:  Trái Thượng Cấp (Lửa, Khói, Cát, Sét, Mochi, Rào Chắn...) - Cấp Quỷ 3-4\n");
            sb.append("- Beri / Tier Phổ Thông:  Trái Phổ Thông (Lò Xo, Trọng Lượng, Bộc Phá, Kilo...) - Cấp Quỷ 1-2\n");
            return sb.toString();
        }
    }

    // ==================================================
    //  CHI PHÍ NÂNG CẤP LÍNH ĐÁNH THUÊ
    // ==================================================

    public static long getUpgradeLevelCostBeri(int currentLevel) {
        return (long) currentLevel * 1_000_000L;
    }
    public static int getUpgradeLevelCostRuby(int currentLevel) {
        return Math.max(10, currentLevel / 5);
    }
    public static int getUpgradeLevelCostExtol(int currentLevel) {
        return Math.max(500, currentLevel * 50);
    }

    public static long getUpgradeTierCostBeri(double targetTier) {
        return (long) (targetTier * 50_000_000L);
    }
    public static int getUpgradeTierCostRuby(double targetTier) {
        return (int) Math.round(targetTier * 200);
    }
    public static int getUpgradeTierCostExtol(double targetTier) {
        return (int) Math.round(targetTier * 25000);
    }

    // ==================================================
    //  DANH SACH TEMPLATE BOT CHUAN ONE PIECE
    // ==================================================

    public static final List<MercEntry> TEMPLATES = new ArrayList<>();

    static {
        // -- Phe 1: HAI QUAN (Navy) --
        TEMPLATES.add(new MercEntry(101, "Akainu Sakazuki", TYPE_HAI_QUAN, (byte)1, 100, 110, TIER_FULL, 30, true, true, true, 150000, 240, 1200, 180, 250_000_000L, 90));
        TEMPLATES.add(new MercEntry(102, "Aokiji Kuzan", TYPE_HAI_QUAN, (byte)3, 85, 99, TIER_NEAR_FULL, 26, true, true, true, 60000, 180, 550, 120, 110_000_000L, 75));
        TEMPLATES.add(new MercEntry(103, "Kizaru Borsalino", TYPE_HAI_QUAN, (byte)3, 85, 99, TIER_HIGH, 24, true, true, false, 45000, 180, 400, 120, 85_000_000L, 60));
        TEMPLATES.add(new MercEntry(104, "Garp Anh Hung", TYPE_HAI_QUAN, (byte)1, 100, 110, TIER_FULL, 30, true, true, true, 140000, 240, 1100, 180, 230_000_000L, 90));
        TEMPLATES.add(new MercEntry(105, "Fujitora Issho", TYPE_HAI_QUAN, (byte)2, 85, 99, TIER_HIGH, 22, true, true, false, 40000, 180, 350, 120, 75_000_000L, 60));
        TEMPLATES.add(new MercEntry(106, "Smoker Khoi Trang", TYPE_HAI_QUAN, (byte)5, 70, 84, TIER_HIGH, 20, false, true, false, 30000, 180, 250, 120, 50_000_000L, 60));
        TEMPLATES.add(new MercEntry(107, "Tashigi Kiem Si", TYPE_HAI_QUAN, (byte)2, 55, 69, TIER_MID, 18, false, false, false, 20000, 150, 180, 90, 35_000_000L, 60));
        TEMPLATES.add(new MercEntry(108, "Coby Anh Hung", TYPE_HAI_QUAN, (byte)1, 40, 54, TIER_MID, 16, false, false, false, 15000, 150, 130, 90, 25_000_000L, 60));
        TEMPLATES.add(new MercEntry(109, "Helmeppo Song Dao", TYPE_HAI_QUAN, (byte)2, 25, 39, TIER_MID, 14, false, false, false, 10000, 120, 90, 90, 18_000_000L, 60));
        TEMPLATES.add(new MercEntry(110, "Morgan Tay Riu", TYPE_HAI_QUAN, (byte)1, 10, 24, TIER_MID, 12, false, false, false, 8000, 120, 75, 90, 14_000_000L, 60));
        TEMPLATES.add(new MercEntry(111, "Hai Quan Tap Su", TYPE_HAI_QUAN, (byte)5, 1, 15, TIER_MID, 10, false, false, false, 6000, 120, 60, 60, 10_000_000L, 60));

        // -- Phe 2: HAI TAC (Pirates) --
        TEMPLATES.add(new MercEntry(201, "Luffy Nika", TYPE_HAI_TAC, (byte)1, 100, 110, TIER_FULL, 30, true, true, true, 160000, 240, 1300, 180, 260_000_000L, 90));
        TEMPLATES.add(new MercEntry(202, "Roronoa Zoro", TYPE_HAI_TAC, (byte)2, 85, 99, TIER_NEAR_FULL, 26, true, true, true, 70000, 180, 600, 120, 120_000_000L, 75));
        TEMPLATES.add(new MercEntry(203, "Vinsmoke Sanji", TYPE_HAI_TAC, (byte)3, 70, 84, TIER_HIGH, 23, true, true, false, 35000, 180, 300, 120, 60_000_000L, 60));
        TEMPLATES.add(new MercEntry(204, "Trafalgar Law", TYPE_HAI_TAC, (byte)2, 85, 99, TIER_NEAR_FULL, 25, true, true, true, 65000, 180, 550, 120, 115_000_000L, 75));
        TEMPLATES.add(new MercEntry(205, "Eustass Kid", TYPE_HAI_TAC, (byte)1, 85, 99, TIER_HIGH, 24, true, true, false, 45000, 180, 400, 120, 85_000_000L, 60));
        TEMPLATES.add(new MercEntry(206, "Shanks Toc Do", TYPE_HAI_TAC, (byte)2, 100, 110, TIER_FULL, 30, true, true, true, 150000, 240, 1200, 180, 240_000_000L, 90));
        TEMPLATES.add(new MercEntry(207, "Dracule Mihawk", TYPE_HAI_TAC, (byte)2, 100, 110, TIER_FULL, 30, true, true, true, 150000, 240, 1200, 180, 240_000_000L, 90));
        TEMPLATES.add(new MercEntry(208, "Portgas D. Ace", TYPE_HAI_TAC, (byte)5, 70, 84, TIER_HIGH, 22, true, true, false, 32000, 180, 280, 120, 55_000_000L, 60));
        TEMPLATES.add(new MercEntry(209, "Charlotte Katakuri", TYPE_HAI_TAC, (byte)2, 85, 99, TIER_NEAR_FULL, 25, true, true, true, 60000, 180, 520, 120, 105_000_000L, 75));
        TEMPLATES.add(new MercEntry(210, "Nami Hoa Tieu", TYPE_HAI_TAC, (byte)4, 55, 69, TIER_HIGH, 20, false, true, false, 25000, 150, 200, 90, 40_000_000L, 60));
        TEMPLATES.add(new MercEntry(211, "God Usopp", TYPE_HAI_TAC, (byte)5, 40, 54, TIER_MID, 18, false, false, false, 15000, 150, 130, 90, 25_000_000L, 60));
        TEMPLATES.add(new MercEntry(212, "Tony Chopper", TYPE_HAI_TAC, (byte)1, 25, 39, TIER_MID, 15, false, false, false, 10000, 120, 90, 90, 18_000_000L, 60));
        TEMPLATES.add(new MercEntry(213, "Tan Binh Hai Tac", TYPE_HAI_TAC, (byte)3, 1, 15, TIER_MID, 10, false, false, false, 6000, 120, 60, 60, 10_000_000L, 60));

        // -- Phe 3: QUAN CACH MANG (Revolutionary) --
        TEMPLATES.add(new MercEntry(301, "Monkey D. Dragon", TYPE_CACH_MANG, (byte)1, 100, 110, TIER_FULL, 30, true, true, true, 160000, 240, 1300, 180, 260_000_000L, 90));
        TEMPLATES.add(new MercEntry(302, "Sabo Hoa Quyen", TYPE_CACH_MANG, (byte)3, 100, 110, TIER_FULL, 30, true, true, true, 140000, 240, 1100, 180, 230_000_000L, 90));
        TEMPLATES.add(new MercEntry(303, "Emporio Ivankov", TYPE_CACH_MANG, (byte)1, 85, 99, TIER_NEAR_FULL, 25, true, true, true, 55000, 180, 480, 120, 95_000_000L, 75));
        TEMPLATES.add(new MercEntry(304, "Bartholomew Kuma", TYPE_CACH_MANG, (byte)1, 85, 99, TIER_NEAR_FULL, 25, true, true, true, 60000, 180, 520, 120, 105_000_000L, 75));
        TEMPLATES.add(new MercEntry(305, "Karasu Dan Qua", TYPE_CACH_MANG, (byte)5, 70, 84, TIER_HIGH, 22, true, true, false, 32000, 180, 280, 120, 55_000_000L, 60));
        TEMPLATES.add(new MercEntry(306, "Koala Cach Mang", TYPE_CACH_MANG, (byte)4, 55, 69, TIER_HIGH, 20, false, true, false, 25000, 150, 200, 90, 42_000_000L, 60));
        TEMPLATES.add(new MercEntry(307, "Hack Ngu Nhan", TYPE_CACH_MANG, (byte)3, 55, 69, TIER_HIGH, 20, false, true, false, 22000, 150, 190, 90, 38_000_000L, 60));
        TEMPLATES.add(new MercEntry(308, "Inazuma Keo Vang", TYPE_CACH_MANG, (byte)2, 40, 54, TIER_MID, 18, false, false, false, 15000, 150, 130, 90, 25_000_000L, 60));
        TEMPLATES.add(new MercEntry(309, "Morley Khong Lo", TYPE_CACH_MANG, (byte)1, 40, 54, TIER_MID, 16, false, false, false, 13000, 150, 120, 90, 22_000_000L, 60));
        TEMPLATES.add(new MercEntry(310, "Belo Betty", TYPE_CACH_MANG, (byte)4, 25, 39, TIER_MID, 14, false, false, false, 10000, 120, 90, 90, 18_000_000L, 60));
        TEMPLATES.add(new MercEntry(311, "Lindbergh Xa Thu", TYPE_CACH_MANG, (byte)5, 10, 24, TIER_MID, 12, false, false, false, 8000, 120, 75, 90, 14_000_000L, 60));
        TEMPLATES.add(new MercEntry(312, "Tan Binh Cach Mang", TYPE_CACH_MANG, (byte)1, 1, 15, TIER_MID, 10, false, false, false, 6000, 120, 60, 60, 10_000_000L, 60));
    }

    public static final int[] ALL_FRUIT_IDS = {
        32,  // Trai Nham Thach / Lua (Magma)
        33,  // Trai Bang Gia (Ice)
        34,  // Trai Khoi (Smoke)
        88,  // Trai Lua (Flame)
        90,  // Trai Nika Thuc Tinh (Nika)
        91,  // Trai Set (Rumble)
        92,  // Trai Cat (Sand)
        93,  // Trai Chan Dong (Gura)
        160, // Trai Phuong Hoang (Phoenix)
        161, // Trai Rong (Dragon)
        219, // Trai Boc Pha (Bomb)
        220, // Trai Lo Xo (Spring)
        240, // Trai Rao Chan (Barrier)
        316, // Trai Mochi (Mochi)
        317, // Trai Yamato
        318, // Trai Trong Luong (Weight)
        427  // Trai Bong Toi (Dark)
    };

    public static int getFruitIdForEntry(MercEntry entry) {
        if (entry == null) return 88;
        return getFruitIdForEntry(entry.id, entry.name);
    }

    public static int getFruitIdForEntry(int entryId) {
        return getFruitIdForEntry(entryId, "");
    }

    public static int getFruitIdForEntry(int entryId, String name) {
        switch (entryId) {
            case 101: return 32;  // Akainu: Magma
            case 102: return 33;  // Aokiji: Ice
            case 103: return 34;  // Kizaru / Light / Smoke
            case 104: return 161; // Garp: Dragon
            case 105: return 318; // Fujitora: Weight / Gravity
            case 106: return 34;  // Smoker: Smoke
            case 107: return 33;  // Tashigi: Ice
            case 108: return 91;  // Coby: Rumble
            case 109: return 160; // Helmeppo: Phoenix
            case 110: return 240; // Morgan: Barrier
            case 111: return 219; // Hai Quan Tap Su: Bomb

            case 201: return 90;  // Luffy: Nika
            case 202: return 160; // Zoro: Phoenix
            case 203: return 88;  // Sanji: Flame
            case 204: return 33;  // Law: Ice / Ope
            case 205: return 32;  // Kid: Magma / Magnet
            case 206: return 161; // Shanks: Dragon
            case 207: return 33;  // Mihawk: Ice
            case 208: return 88;  // Ace: Flame
            case 209: return 316; // Katakuri: Mochi
            case 210: return 91;  // Nami: Rumble
            case 211: return 92;  // Usopp: Sand
            case 212: return 220; // Chopper: Spring
            case 213: return 220; // Tan Binh Hai Tac: Spring

            case 301: return 161; // Dragon: Dragon
            case 302: return 88;  // Sabo: Flame
            case 303: return 91;  // Ivankov: Rumble / Hormone
            case 304: return 93;  // Kuma: Gura / Paw
            case 305: return 427; // Karasu: Dark / Crow
            case 306: return 160; // Koala: Phoenix
            case 307: return 32;  // Hack: Magma
            case 308: return 33;  // Inazuma: Ice / Scissor
            case 309: return 318; // Morley: Weight
            case 310: return 240; // Belo Betty: Barrier
            case 311: return 219; // Lindbergh: Bomb
            case 312: return 219; // Tan Binh Cach Mang: Bomb
        }

        if (name != null && !name.isEmpty()) {
            String lower = name.toLowerCase();
            if (lower.contains("nika") || lower.contains("thuc tinh") || lower.contains("luffy")) return 90;
            if (lower.contains("sabo") || lower.contains("ace") || lower.contains("lua") || lower.contains("flame") || lower.contains("hoa")) return 88;
            if (lower.contains("usopp") || lower.contains("xa thu") || lower.contains("cat") || lower.contains("sand")) return 92;
            if (lower.contains("nami") || lower.contains("set") || lower.contains("rumble") || lower.contains("coby") || lower.contains("ivankov")) return 91;
            if (lower.contains("zoro") || lower.contains("phuong hoang") || lower.contains("koala") || lower.contains("helmeppo")) return 160;
            if (lower.contains("smoker") || lower.contains("khoi") || lower.contains("kizaru")) return 34;
            if (lower.contains("akainu") || lower.contains("sakazuki") || lower.contains("magma") || lower.contains("nham thach") || lower.contains("kid") || lower.contains("hack")) return 32;
            if (lower.contains("aokiji") || lower.contains("bang") || lower.contains("tashigi") || lower.contains("law") || lower.contains("inazuma") || lower.contains("mihawk")) return 33;
            if (lower.contains("kuma") || lower.contains("chan dong") || lower.contains("gura")) return 93;
            if (lower.contains("rao chan") || lower.contains("barrier") || lower.contains("belo") || lower.contains("morgan")) return 240;
            if (lower.contains("boc pha") || lower.contains("bomb") || lower.contains("lindbergh")) return 219;
            if (lower.contains("lo xo") || lower.contains("spring") || lower.contains("chopper")) return 220;
            if (lower.contains("katakuri") || lower.contains("mochi")) return 316;
            if (lower.contains("yamato")) return 317;
            if (lower.contains("kaido") || lower.contains("dragon") || lower.contains("garp") || lower.contains("shanks") || lower.contains("rong")) return 161;
            if (lower.contains("karasu") || lower.contains("dark") || lower.contains("bong toi")) return 427;
            if (lower.contains("fujitora") || lower.contains("morley") || lower.contains("trong luong") || lower.contains("weight")) return 318;
        }

        int hash = Math.abs((name != null && !name.isEmpty() ? name.hashCode() : entryId));
        return ALL_FRUIT_IDS[hash % ALL_FRUIT_IDS.length];
    }

    public static byte getClazzForCharName(String name) {
        if (name == null || name.isEmpty()) return (byte) ZUtil.random(1, 5);
        String lower = name.toLowerCase();
        if (lower.contains("zoro") || lower.contains("mihawk") || lower.contains("law") || lower.contains("tashigi")
                || lower.contains("helmeppo") || lower.contains("fujitora") || lower.contains("inazuma")
                || lower.contains("katakuri") || lower.contains("shanks") || lower.contains("kiem")) {
            return 2; // Kiem si
        }
        if (lower.contains("sanji") || lower.contains("kizaru") || lower.contains("aokiji") || lower.contains("sabo")
                || lower.contains("hack") || lower.contains("cuoc")) {
            return 3; // Cuoc thu
        }
        if (lower.contains("nami") || lower.contains("koala") || lower.contains("belo") || lower.contains("phap")) {
            return 4; // Phap su
        }
        if (lower.contains("usopp") || lower.contains("smoker") || lower.contains("ace") || lower.contains("karasu")
                || lower.contains("lindbergh") || lower.contains("sung") || lower.contains("xa thu")) {
            return 5; // Xa thu
        }
        return 1; // Vo si / Dam mac dinh
    }

    public static List<MercEntry> generateRandomCandidates(int faction) {
        return generateRandomCandidates(null, faction);
    }

    public static List<MercEntry> generateRandomCandidates(model.Player p, int faction) {
        List<MercEntry> list = new ArrayList<>();
        int targetFaction = (faction >= 1 && faction <= 3) ? faction : 1;

        String[][] candidatePools = new String[][]{
            // Phe 1: Hai Quan
            {
                "Akainu Sakazuki", "Aokiji Kuzan", "Kizaru Borsalino",
                "Garp Anh Hung", "Fujitora Issho", "Sengoku Phat Vang",
                "Smoker Khoi Trang", "Tashigi Kiem Si", "Coby Anh Hung",
                "Helmeppo Song Dao", "Morgan Tay Riu", "Hai Quan Tap Su"
            },
            // Phe 2: Hai Tac
            {
                "Luffy Nika", "Roronoa Zoro", "Vinsmoke Sanji",
                "Trafalgar Law", "Eustass Kid", "Shanks Toc Do",
                "Dracule Mihawk", "Portgas D. Ace", "Charlotte Katakuri",
                "Nami Hoa Tieu", "God Usopp", "Tony Chopper"
            },
            // Phe 3: Quan Cach Mang
            {
                "Monkey D. Dragon", "Sabo Hoa Quyen", "Emporio Ivankov",
                "Bartholomew Kuma", "Karasu Dan Qua", "Koala Cach Mang",
                "Hack Ngu Nhan", "Inazuma Keo Vang", "Morley Khong Lo",
                "Belo Betty", "Lindbergh Xa Thu", "Tan Binh Cach Mang"
            }
        };

        String[] namesPool = candidatePools[targetFaction - 1];

        List<String> poolList = new ArrayList<>(Arrays.asList(namesPool));
        Collections.shuffle(poolList);

        int count = Math.min(8, poolList.size());
        int playerLv = (p != null && p.level > 0) ? p.level : 60;
        double maxAllowedTier = getMaxAllowedTier(playerLv);

        for (int i = 0; i < count; i++) {
            String botName = poolList.get(i);
            int entryId = targetFaction * 1000 + (i + 1) * 10 + ZUtil.random(1, 9);
            byte clazz = getClazzForCharName(botName);

            // Cân bằng Tier không vượt quá mốc cấp độ của người chơi
            int tierRoll = ZUtil.random(100);
            double baseTier = (tierRoll < 25) ? (3.5 + ZUtil.random(0, 50) / 100.0)
                            : (tierRoll < 60 ? (2.5 + ZUtil.random(0, 99) / 100.0) : (1.0 + ZUtil.random(0, 149) / 100.0));
            double tier = Math.min(maxAllowedTier, baseTier);

            // Cấp độ lính không bao giờ vượt quá cấp độ của Chủ Nhân
            int maxLv = Math.min(110, playerLv);
            int minLv = Math.max(1, maxLv - ZUtil.random(2, 8));
            int skillLv = (tier >= 3.5) ? 30 : (tier >= 2.5 ? 24 : (tier >= 1.8 ? 18 : 12));
            boolean hasHoanMy = (tier >= 2.0);
            boolean hasKhamDa = (tier >= 1.5);
            boolean hasKichAn = (tier >= 2.5);

            int priceExtol = (tier >= 3.5) ? 140000 : (tier >= 2.5 ? 65000 : (tier >= 1.8 ? 30000 : 12000));
            int durExtol   = (tier >= 3.5) ? 240 : (tier >= 2.5 ? 180 : 120);

            int priceRuby  = (tier >= 3.5) ? 1100 : (tier >= 2.5 ? 500 : (tier >= 1.8 ? 250 : 90));
            int durRuby    = (tier >= 3.5) ? 180 : (tier >= 2.5 ? 120 : 90);

            long priceBeri = (tier >= 3.5) ? 220_000_000L : (tier >= 2.5 ? 95_000_000L : (tier >= 1.8 ? 45_000_000L : 15_000_000L));
            int durBeri    = (tier >= 3.5) ? 90 : (tier >= 2.5 ? 75 : 60);

            MercEntry entry = new MercEntry(
                entryId, botName, targetFaction, clazz,
                minLv, maxLv, tier, skillLv,
                hasHoanMy, hasKhamDa, hasKichAn,
                priceExtol, durExtol,
                priceRuby, durRuby,
                priceBeri, durBeri
            );

            list.add(entry);
        }

        return list;
    }

    public static List<MercEntry> getByFaction(int faction) {
        List<MercEntry> list = new ArrayList<>();
        for (MercEntry e : TEMPLATES) {
            if (e.faction == faction) list.add(e);
        }
        return list;
    }

    public static MercEntry getById(int id) {
        for (MercEntry e : TEMPLATES) {
            if (e.id == id) return e;
        }
        return null;
    }

    public static String getFactionName(int faction) {
        if (faction >= 0 && faction < FACTION_NAME.length) {
            return FACTION_NAME[faction];
        }
        return "";
    }
}


