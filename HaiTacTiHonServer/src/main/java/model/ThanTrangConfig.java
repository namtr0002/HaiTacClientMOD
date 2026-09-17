package model;

import template.Item_wear;
import template.ThanTrangConfig.OptionDef;
import network.Service;
import map.MapService;
import skill.Skill_info;
import skill.Skill_Template;
import java.util.ArrayList;
import java.util.List;

public class ThanTrangConfig {

    public static final int TOTAL_SETS = 15;

    // Transformation Parts: [head, body, leg, hair, hat, weapon] (15 Sets: Akainu -> Queen)
    public static final int[][] TRANSFORM_PARTS = new int[][]{
        {-1, 312, 313, -1, -1, -1},        // Set 1: Dung Nham (Akainu)
        {319, 320, 321, -1, -1, -1},       // Set 2: Hàn Băng (Aokiji)
        {735, 736, 737, -1, -1, -1},       // Set 3: Quang Tốc (Kizaru)
        {847, 851, 850, 848, -1, -1},      // Set 4: Hắc Ám (Blackbeard)
        {494, 390, 391, -1, -1, -1},       // Set 5: Sấm Sét (Enel)
        {729, 719, 720, 757, -1, -1},      // Set 6: Chấn Động (Whitebeard)
        {1003, 1004, -1, -1, -1, -1},      // Set 7: Phẫu Thuật (Law)
        {1009, 1010, 1011, -1, -1, -1},    // Set 8: Từ Tính (Kid)
        {1109, 1110, 1111, -1, -1, -1},    // Set 9: Rồng Độc (Magellan)
        {694, 695, 696, -1, -1, -1},       // Set 10: Tình Yêu (Hancock)
        {875, 876, 877, -1, -1, -1},       // Set 11: Phượng Hoàng (Marco)
        {889, 890, 891, 892, -1, -1},      // Set 12: Phật Quang (Sengoku)
        {1062, 1063, 1064, 1067, -1, -1},  // Set 13: Bách Thú Kaido
        {984, 985, 986, -1, -1, -1},       // Set 14: Hổ Răng Kiếm (Who's Who)
        {971, 972, 973, 976, -1, -1}       // Set 15: T-Rex Bạo Chúa (Queen)
    };

    public static boolean isThanTrangItem(int itemId) {
        return template.ThanTrangConfig.isThanTrang(itemId);
    }

    public static int getSetIdByItem(int itemId) {
        int idx = template.ThanTrangConfig.getSetIndex(itemId);
        return (idx >= 0 && idx < TOTAL_SETS) ? (idx + 1) : 0;
    }

    public static boolean hasFullSet(Player p, int setId) {
        if (p == null || p.item == null || p.item.it_body == null) {
            return false;
        }
        if (setId < 1 || setId > TOTAL_SETS) {
            return false;
        }
        int targetSetIndex = setId - 1;

        // 1. Check slots 8..13 first (Dedicated Than Trang slots in it_body)
        boolean fullIn8to13 = true;
        for (int slot = 8; slot <= 13; slot++) {
            if (slot >= p.item.it_body.length) {
                fullIn8to13 = false;
                break;
            }
            Item_wear it = p.item.it_body[slot];
            if (it == null || it.template == null || template.ThanTrangConfig.getSetIndex(it.template.id) != targetSetIndex) {
                fullIn8to13 = false;
                break;
            }
        }
        if (fullIn8to13) return true;

        // 2. Check slots 0..5 fallback (Normal equipment slots)
        boolean fullIn0to5 = true;
        for (int slot = 0; slot < 6; slot++) {
            if (slot >= p.item.it_body.length) {
                fullIn0to5 = false;
                break;
            }
            Item_wear it = p.item.it_body[slot];
            if (it == null || it.template == null || template.ThanTrangConfig.getSetIndex(it.template.id) != targetSetIndex) {
                fullIn0to5 = false;
                break;
            }
        }
        if (fullIn0to5) return true;

        // 3. General distinct piece check: Check if player has all 6 pieces of this set anywhere in it_body
        boolean[] hasPiece = new boolean[6];
        int countPieces = 0;
        for (int slot = 0; slot < p.item.it_body.length; slot++) {
            Item_wear it = p.item.it_body[slot];
            if (it != null && it.template != null && template.ThanTrangConfig.getSetIndex(it.template.id) == targetSetIndex) {
                int piece = (it.template.id - 2604) % 6;
                if (piece >= 0 && piece < 6 && !hasPiece[piece]) {
                    hasPiece[piece] = true;
                    countPieces++;
                }
            }
        }
        return countPieces >= 6;
    }

    public static int getFullSetId(Player p) {
        if (p == null || p.item == null || p.item.it_body == null) {
            return 0;
        }
        for (int setId = 1; setId <= TOTAL_SETS; setId++) {
            if (hasFullSet(p, setId)) {
                return setId;
            }
        }
        return 0;
    }

    public static int getSkillBySetId(int setId) {
        if (setId >= 1 && setId <= TOTAL_SETS) {
            return 4001 + setId;
        }
        return -1;
    }

    public static int getActiveSkill1(int setId) {
        return getSkillBySetId(setId);
    }

    public static int getActiveSkill2(int setId) {
        return getSkillBySetId(setId);
    }

    public static int getTransformSkill(int setId) {
        if (setId >= 1 && setId <= TOTAL_SETS) {
            return 4000 + (setId - 1) * 5 + 3;
        }
        return -1;
    }

    public static int getStatBuffSkill(int setId) {
        if (setId >= 1 && setId <= TOTAL_SETS) {
            return 4000 + (setId - 1) * 5 + 4;
        }
        return -1;
    }

    public static int getPassiveSkill(int setId) {
        if (setId >= 1 && setId <= TOTAL_SETS) {
            return 4000 + (setId - 1) * 5 + 5;
        }
        return -1;
    }

    public static boolean isThanTrangSkill(int skillId) {
        return skillId >= 4001 && skillId <= 4080;
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

    public static int getTransformBuffId(int setId) {
        if (setId >= 1 && setId <= TOTAL_SETS) {
            return 3000 + setId; // 3001..3015
        }
        return -1;
    }

    public static int getStatBuffId(int setId) {
        if (setId >= 1 && setId <= TOTAL_SETS) {
            return 3015 + setId; // 3016..3030
        }
        return -1;
    }

    public static int[] getTransformParts(int setId) {
        if (setId >= 1 && setId <= TOTAL_SETS) {
            return TRANSFORM_PARTS[setId - 1];
        }
        return null;
    }

    public static void applyPassiveBonus(Player p, int[] params, boolean have_eff) {
        if (p == null || params == null) return;
        int setId = getFullSetId(p);
        if (setId == 0) return;

        switch (setId) {
            case 1: // Set 1: Dung Nham (Akainu)
                params[1] += 700;   // +70% Công
                params[15] += 8000; // +8000 HP
                params[57] += 120;  // +12% ST chuẩn
                params[64] += 100;  // +10% bộc phá
                params[13] += 100;  // +10% xuyên giáp
                break;
            case 2: // Set 2: Hàn Băng (Aokiji)
                params[2] += 700;   // +70% Phép
                params[15] += 8000; // +8000 HP
                params[75] += 120;  // +12% choáng
                params[27] += 150;  // +15% kháng phép
                params[49] += 100;  // +10% giảm chí mạng đ/t
                break;
            case 3: // Set 3: Quang Tốc (Kizaru)
                params[1] += 700;   // +70% Công
                params[12] += 120;  // +12% né tránh
                params[76] += 150;  // +15% chính xác
                params[25] += 100;  // +10% hồi chiêu
                break;
            case 4: // Set 4: Hắc Ám (Blackbeard)
                params[1] += 700;   // +70% Công
                params[63] += 120;  // +12% giảm miễn thương
                params[58] += 120;  // +12% hấp thụ
                params[21] += 100;  // +100 hút HP
                break;
            case 5: // Set 5: Sấm Sét (Enel)
                params[2] += 700;   // +70% Phép
                params[15] += 8000; // +8000 HP
                params[80] += 150;  // +15% điện giật
                params[75] += 120;  // +12% choáng
                break;
            case 6: // Set 6: Chấn Động (Whitebeard)
                params[1] += 800;   // +80% Công
                params[15] += 10000;// +10000 HP
                params[46] += 150;  // +15% ST cuối
                params[13] += 150;  // +15% xuyên giáp
                break;
            case 7: // Set 7: Phẫu Thuật (Law)
                params[1] += 550;   // +55% Công
                params[2] += 550;   // +55% Phép
                params[25] += 150;  // +15% hồi chiêu
                params[55] += 120;  // +12% từ chối tử thần
                break;
            case 8: // Set 8: Từ Tính (Kid)
                params[1] += 700;   // +70% Công
                params[15] += 9000; // +9000 HP
                params[14] += 120;  // +12% phản đòn
                params[13] += 150;  // +15% xuyên giáp
                break;
            case 9: // Set 9: Rồng Độc (Magellan)
                params[2] += 700;   // +70% Phép
                params[15] += 9000; // +9000 HP
                params[48] += 120;  // +12% ST theo % máu
                params[70] += 120;  // +12% giảm thủ cuối
                break;
            case 10: // Set 10: Tình Yêu (Hancock)
                params[2] += 700;   // +70% Phép
                params[75] += 150;  // +15% choáng
                params[12] += 120;  // +12% né tránh
                params[60] += 100;  // -100 sức mạnh đ/t
                break;
            case 11: // Set 11: Phượng Hoàng (Marco)
                params[1] += 700;   // +70% Công
                params[15] += 9000; // +9000 HP
                params[79] += 25;   // +2.5% hồi máu mỗi 10s
                params[73] += 120;  // +12% chuyển hóa ST -> HP
                break;
            case 12: // Set 12: Phật Quang (Sengoku)
                params[1] += 700;   // +70% Công
                params[15] += 11000;// +11000 HP
                params[53] += 150;  // +15% miễn thương
                params[54] += 120;  // +12% giảm 90% ST
                params[3] += 250;   // +250 phòng thủ
                break;
            case 13: // Set 13: Bách Thú Kaido
                params[1] += 800;   // +80% Công
                params[15] += 12000;// +12000 HP
                params[57] += 150;  // +15% ST chuẩn
                params[46] += 150;  // +15% ST cuối
                params[10] += 100;  // +10% bạo kích
                break;
            case 14: // Set 14: Hổ Răng Kiếm (Who's Who)
                params[1] += 700;   // +70% Công
                params[15] += 9000; // +9000 HP
                params[10] += 100;  // +10% chí mạng
                params[11] += 200;  // +20% ST chí mạng
                params[76] += 150;  // +15% chính xác
                break;
            case 15: // Set 15: T-Rex Bạo Chúa (Queen)
                params[1] += 700;   // +70% Công
                params[15] += 12000;// +12000 HP
                params[70] += 150;  // +15% giảm thủ cuối
                params[57] += 150;  // +15% ST chuẩn
                params[3] += 250;   // +250 phòng thủ
                break;
        }
    }

    public static void broadcastCharWearing(Player p) {
        if (p == null) return;
        try {
            if (p.getService() != null) {
                p.getService().charWearing(p, false);
            }
            if (p.map != null && p.map.players != null) {
                for (int i = 0; i < p.map.players.size(); i++) {
                    Player p0 = p.map.players.get(i);
                    if (p0 != null && p0.conn != null && !p0.equals(p) && p0.getService() != null) {
                        p0.getService().charWearing(p, false);
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    public static void onPlayerUnequipOrCancel(Player p) {
        if (p == null) return;
        boolean needUpdate = false;
        
        if (p.isTransformThanTrang) {
            p.isTransformThanTrang = false;
            p.transformThanTrangSetId = 0;
            needUpdate = true;
        }

        if (p.list_eff != null) {
            for (int setId = 1; setId <= TOTAL_SETS; setId++) {
                final int tId = getTransformBuffId(setId);
                final int sId = getStatBuffId(setId);
                p.list_eff.removeIf(eff -> eff != null && (eff.id == tId || eff.id == sId));
            }
        }

        if (needUpdate) {
            try {
                p.update_info_to_all();
                broadcastCharWearing(p);
            } catch (Exception ignored) {}
        }
    }

    public static boolean activateTransformation(Player p) {
        if (p == null) return false;
        int setId = getFullSetId(p);
        if (setId == 0) return false;

        long now = System.currentTimeMillis();
        if (p.lastTransformThanTrangTime + 60000L > now) {
            if (p.getService() != null) {
                try {
                    p.getService().send_box_ThongBao_OK("Ky nang Bien hinh dang hoi! (" + ((p.lastTransformThanTrangTime + 60000L - now) / 1000) + "s)");
                } catch (Exception ignored) {}
            }
            return false;
        }

        p.isTransformThanTrang = true;
        p.transformThanTrangSetId = setId;
        p.lastTransformThanTrangTime = now;
        p.transformThanTrangEndTime = now + 30000L;

        int statBuffId = getStatBuffId(setId);
        if (statBuffId > 0) {
            p.add_new_eff(statBuffId, 1, 30000L);
        }

        try {
            p.update_info_to_all();
            broadcastCharWearing(p);
            if (p.getService() != null) {
                int effId = 150 + (setId <= 10 ? setId : (setId % 10 + 1));
                p.getService().addEffect(p.index_map, (short) effId, 5000, (byte) 0, (byte) 1);
            }
        } catch (Exception ignored) {}
        return true;
    }

    public static void checkTransformExpire(Player p) {
        if (p == null || !p.isTransformThanTrang) return;
        long now = System.currentTimeMillis();
        if (now >= p.transformThanTrangEndTime || getFullSetId(p) == 0) {
            p.isTransformThanTrang = false;
            p.transformThanTrangSetId = 0;
            try {
                p.update_info_to_all();
                broadcastCharWearing(p);
                if (p.getService() != null) {
                    p.getService().addEffect(p.index_map, (short) 166, 3000, (byte) 0, (byte) 1);
                }
            } catch (Exception ignored) {}
        }
    }
}

