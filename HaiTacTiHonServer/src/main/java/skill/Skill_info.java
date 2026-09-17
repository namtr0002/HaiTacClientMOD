package skill;

import model.Player;

/**
 *
 * @author Truongbk
 */
public class Skill_info {
    // idEff 1: gay choang
    //
    // idEff 2: gay chay mau
    // idEff 3: gay giam cong
    // idEff 4: gay giam thu
    // idEff 5: gay hoa mat
    // idEff 6: gay dien giat
    // idEff 7: gay lua chay
    //
    // idEff 8: gay troi chan
    // idEff 9: gay hut nang luong
    // idEff 10: gay trung doc
    // idEff 11: gay bat tu
    // idEff 12: gay chi mang lien tuc
    // idEff 13: gay tru bat tu
    // idEff 14: gay tru bat tu (co dau)
    // idEff 15: gay hut suc manh
    // idEff 16: gay hoang loan
    //
    public static long[] EXP = new long[40];
    public static int[] EXP_DEVIL = new int[] { 100, 125, 167, 200, 250 };
    public long exp;
    public Skill_Template temp;
    public byte lvdevil;
    public byte devilpercent;
    static {
        // Cấp skill < 5: Giảm mốc EXP yêu cầu để nâng cấp nhanh hơn
        Skill_info.EXP[0] = 600;    // Cấp 1 -> 2 (cũ: 3400)
        Skill_info.EXP[1] = 1800;   // Cấp 2 -> 3 (cũ: 10000)
        Skill_info.EXP[2] = 5000;   // Cấp 3 -> 4 (cũ: 30000)
        Skill_info.EXP[3] = 15000;  // Cấp 4 -> 5 (cũ: 60000)
        // Cấp 5 trở lên: Giữ nguyên giá trị và công thức tính như cũ
        Skill_info.EXP[4] = 120000; // Cấp 5 -> 6 (cũ: 120000)
        for (int i = 5; i < Skill_info.EXP.length; i++) {
            Skill_info.EXP[i] = (Skill_info.EXP[i - 1] * 15) / 10;
        }
    }

    public int get_percent() {
        if (exp <= 0 || temp == null || temp.Lv_RQ <= 0) {
            return 0;
        }
        int lvIndex = temp.Lv_RQ - 1;
        if (lvIndex < 0) {
            return 0;
        }
        if (lvIndex >= Skill_info.EXP.length) {
            return 1000;
        }
        long exp_total = Skill_info.EXP[lvIndex];
        if (exp_total <= 0) {
            return 0;
        }
        long pct = (exp * 1000L) / exp_total;
        if (pct < 0) {
            return 0;
        }
        if (pct > 1000) {
            return 1000;
        }
        return (int) pct;
    }

    private static final java.util.regex.Pattern PATTERN_CHIEU = java.util.regex.Pattern.compile("(\\d+)\\s*%\\s*sát\\s*thương\\s*của\\s*chiêu", java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.UNICODE_CASE);
    private static final java.util.regex.Pattern PATTERN_SAT_THUONG = java.util.regex.Pattern.compile("(\\d+)\\s*%\\s*sát\\s*thương", java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.UNICODE_CASE);
    private static final java.util.regex.Pattern PATTERN_PERCENT = java.util.regex.Pattern.compile("(\\d+)\\s*%");

    public static int extractDamagePercent(String info, int defaultDamage) {
        if (info != null && !info.isEmpty()) {
            java.util.regex.Matcher m = PATTERN_CHIEU.matcher(info);
            if (m.find()) {
                try {
                    return Integer.parseInt(m.group(1));
                } catch (Exception ignored) {}
            }
            m = PATTERN_SAT_THUONG.matcher(info);
            if (m.find()) {
                try {
                    return Integer.parseInt(m.group(1));
                } catch (Exception ignored) {}
            }
            // Fallback: search for last percentage in string
            m = PATTERN_PERCENT.matcher(info);
            int lastPct = -1;
            while (m.find()) {
                try {
                    lastPct = Integer.parseInt(m.group(1));
                } catch (Exception ignored) {}
            }
            if (lastPct > 0) {
                return lastPct;
            }
        }
        return defaultDamage > 0 ? defaultDamage : 100;
    }

    public int get_dame(Player p) {
        if (this.temp == null) {
            return 100;
        }
        int result = this.temp.damage;
        int baseAtk = 100;
        if (p != null && p.skill_point != null && !p.skill_point.isEmpty()) {
            Skill_info sk0 = p.skill_point.get(0);
            if (sk0 != null && sk0.temp != null && sk0.temp.damage > 0) {
                baseAtk = sk0.temp.damage;
            }
        }

        // 1. Kỹ năng Trái Ác Quỷ Tấn Công (typeDevil == 1 && typeSkill == 1)
        if (this.temp.typeDevil == 1 && this.temp.typeSkill == 1) {
            int pct = extractDamagePercent(this.temp.info, this.temp.damage);
            if (this.lvdevil > 0) {
                switch (this.lvdevil) {
                    case 1: pct = (pct * 11) / 10; break;
                    case 2: pct = (pct * 125) / 100; break;
                    case 3: pct = (pct * 145) / 100; break;
                    case 4: pct = (pct * 17) / 10; break;
                    case 5: pct *= 2; break;
                }
            }
            result = (baseAtk * pct) / 100;
        }
        // 2. Kỹ năng Tuyệt Kỹ Thần Trang (4001..4080)
        else if (this.temp.ID >= 4001 && this.temp.ID <= 4080) {
            int pct = this.temp.damage > 0 ? this.temp.damage : 5000;
            result = (baseAtk * pct) / 100;
        }
        // 3. Kỹ năng Môn Phái (ID == 0, 1, 2) khi được cường hóa cấp ác quỷ
        else if (this.temp.ID == 0 || this.temp.ID == 1 || this.temp.ID == 2) {
            if (this.lvdevil > 0) {
                switch (this.lvdevil) {
                    case 1: result = (result * 11) / 10; break;
                    case 2: result = (result * 125) / 100; break;
                    case 3: result = (result * 145) / 100; break;
                    case 4: result = (result * 17) / 10; break;
                    case 5: result *= 2; break;
                }
            }
        }
        return Math.max(1, result);
    }

    public void update_exp_devil(int exp_devil) {
        int exp = (this.devilpercent * Skill_info.EXP_DEVIL[this.lvdevil]) / 100;
        exp += exp_devil;
        while (this.lvdevil < 5 && exp >= Skill_info.EXP_DEVIL[this.lvdevil]) {
            exp -= Skill_info.EXP_DEVIL[this.lvdevil];
            this.lvdevil++;
        }
        if (this.lvdevil == 5) {
            this.devilpercent = 0;
        } else {
            this.devilpercent = (byte) ((exp * 100) / Skill_info.EXP_DEVIL[this.lvdevil]);
        }
    }

    public short[] get_eff_skills() {
        short singleId = -1;
        if (temp.ID >= 2000 && lvdevil == 5) {
            switch (temp.indexSkillInServer) {
                case 658: singleId = 402; break;
                case 659: singleId = 403; break;
                case 475: singleId = 228; break;
                case 476: singleId = 229; break;
                case 477: singleId = 227; break;
                case 485: singleId = 232; break;
                case 486: singleId = 234; break;
                case 520: singleId = 236; break;
                case 521: singleId = 235; break;
                case 522: singleId = 230; break;
                case 523: singleId = 231; break;
                case 526: singleId = 237; break;
                case 527: singleId = 238; break;
                case 530: singleId = 239; break;
                case 531: singleId = 240; break;
                case 535: singleId = 241; break;
                case 538: singleId = 242; break;
                case 541: singleId = 244; break;
                case 542: singleId = 243; break;
                case 543: singleId = 251; break;
                case 544: singleId = 252; break;
                case 546: singleId = 254; break;
                case 547: singleId = 253; break;
                case 549: singleId = 255; break;
            }
        }
        if (singleId == -1 && lvdevil == 5) {
            switch (temp.getTypeEffSkill()) {
                // Trái Ánh Sáng: 404 -> 406, 405 -> 407
                case 404: singleId = 406; break;
                case 405: singleId = 407; break;
                // Trái Tình Yêu: 408 -> 410, 409 -> 411
                case 408: singleId = 410; break;
                case 409: singleId = 411; break;
                // Trái Bóng Tối: 400 -> 402, 401 -> 403
                case 400: singleId = 402; break;
                case 401: singleId = 403; break;
                // Trái Nika: 3100 -> 3101, 3102 -> 3102, 3103 -> 3103
                case 3100: singleId = 3101; break;
                case 3102: singleId = 3102; break;
                case 3103: singleId = 3103; break;
                // Trái Lửa (Ace): 2 -> 228, 3 -> 229
                case 2: singleId = 228; break;
                case 3: singleId = 229; break;
                // Trái Băng (Aokiji): 4 -> 230, 5 -> 231
                case 4: singleId = 230; break;
                case 5: singleId = 231; break;
                // Trái Khói (Smoker): 6 -> 232, 10 -> 234
                case 6: singleId = 232; break;
                case 10: singleId = 234; break;
                // Trái Cát (Crocodile): 25 -> 235, 26 -> 236
                case 25: singleId = 235; break;
                case 26: singleId = 236; break;
                // Trái Sấm (Enel): 169 -> 237, 170 -> 238
                case 169: singleId = 237; break;
                case 170: singleId = 238; break;
                // Trái Nham Thạch (Akainu): 171 -> 239, 172 -> 240
                case 171: singleId = 239; break;
                case 172: singleId = 240; break;
                // Trái Cao Su (Luffy): 164 -> 227
                case 164: singleId = 227; break;
                // Các trái khác:
                case 245: singleId = 251; break;
                case 246: singleId = 253; break;
                case 247: singleId = 254; break;
                case 248: singleId = 255; break;
                case 249: singleId = 252; break;
            }
        }
        if (singleId != -1) return new short[]{singleId};
        return new short[]{temp != null ? temp.getTypeEffSkill() : (short) 21};
    }
}
