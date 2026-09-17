package template;

public final class IconType {
    public static final byte ITEM_MAP         = 0;  // Thư mục: item_map
    public static final byte MONSTER          = 1;  // Thư mục: monster
    public static final byte POTION           = 2;  // Thư mục: potion
    public static final byte ITEM             = 3;  // Thư mục: items
    public static final byte SKILL            = 4;  // Thư mục: skill
    public static final byte SKILL_SMALL      = 5;  // Thư mục: skill_small
    public static final byte NPC              = 6;  // Thư mục: npc
    public static final byte QUEST_POTION     = 7;  // Thư mục: questitem
    public static final byte MATERIAL_POTION  = 8;  // Thư mục: material
    public static final byte CLAN             = 9;  // Thư mục: clan
    public static final byte BOAT             = 10; // Thư mục: boat
    public static final byte ITEM_OTHER       = 11; // Thư mục: dialog
    public static final byte CHAR_PART        = 12; // Thư mục: char_part
    public static final byte FASHION          = 13; // Thư mục: fashion
    public static final byte SKILL_COMBO      = 14; // Thư mục: skill_combo
    public static final byte CLAN_BIG         = 15; // Thư mục: clan_big
    public static final byte OTHER_NEW        = 16; // Thư mục: other_new
    public static final byte EFF_CLIENT       = 17; // Thư mục: eff
    public static final byte EFF_CLIENT_LOW   = 18; // Thư mục: efflow

    public static final int COUNT = 19;

    public static final String[] FOLDERS = {
        "item_map",
        "monster",
        "potion",
        "items",
        "skill",
        "skill_small",
        "npc",
        "questitem",
        "material",
        "clan",
        "boat",
        "dialog",
        "char_part",
        "fashion",
        "skill_combo",
        "clan_big",
        "other_new",
        "eff",
        "efflow"
    };

    public static final int[] LEGACY_OFFSETS = {
        0,      // ITEM_MAP
        1000,   // MONSTER
        2000,   // POTION
        3000,   // ITEM
        4000,   // SKILL
        4500,   // SKILL_SMALL
        5000,   // NPC
        6000,   // QUEST_POTION
        6500,   // MATERIAL_POTION
        7000,   // CLAN
        8000,   // BOAT
        9000,   // ITEM_OTHER
        10000,  // CHAR_PART (với hack 26000 overflow)
        20000,  // FASHION
        21000,  // SKILL_COMBO
        22000,  // CLAN_BIG
        23000,  // OTHER_NEW
        24000,  // EFF_CLIENT
        25000   // EFF_CLIENT_LOW
    };

    public static String getFolder(byte type) {
        if (type >= 0 && type < FOLDERS.length) {
            return FOLDERS[type];
        }
        return "other";
    }

    public static byte getFolderType(String folder) {
        if (folder != null) {
            for (byte i = 0; i < FOLDERS.length; i++) {
                if (FOLDERS[i].equalsIgnoreCase(folder)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int getLegacyOffset(byte type) {
        if (type >= 0 && type < LEGACY_OFFSETS.length) {
            return LEGACY_OFFSETS[type];
        }
        return 0;
    }

    /**
     * Tự động suy luận Type và ID gốc từ raw ID có offset cũ (dùng cho backward compatibility).
     */
    public static byte detectLegacyType(int rawId) {
        if (rawId >= 26000) return CHAR_PART;
        if (rawId >= 25000) return EFF_CLIENT_LOW;
        if (rawId >= 24000) return EFF_CLIENT;
        if (rawId >= 23000) return OTHER_NEW;
        if (rawId >= 22000) return CLAN_BIG;
        if (rawId >= 21000) return SKILL_COMBO;
        if (rawId >= 20000) return FASHION;
        if (rawId >= 10000) return CHAR_PART;
        if (rawId >= 9000)  return ITEM_OTHER;
        if (rawId >= 8000)  return BOAT;
        if (rawId >= 7000)  return CLAN;
        if (rawId >= 6500)  return MATERIAL_POTION;
        if (rawId >= 6000)  return QUEST_POTION;
        if (rawId >= 5000)  return NPC;
        if (rawId >= 4500)  return SKILL_SMALL;
        if (rawId >= 4000)  return SKILL;
        if (rawId >= 3000)  return ITEM;
        if (rawId >= 2000)  return POTION;
        if (rawId >= 1000)  return MONSTER;
        return ITEM_MAP;
    }

    public static int detectLegacyId(int rawId) {
        if (rawId >= 26000) {
            return 10000 + (rawId - 26000);
        }
        byte type = detectLegacyType(rawId);
        int offset = getLegacyOffset(type);
        return rawId - offset;
    }

    public static int pack(byte type, int id) {
        return ((type & 0xFF) << 24) | (id & 0x00FFFFFF);
    }

    public static byte getPackedType(int packed) {
        return (byte) ((packed >>> 24) & 0xFF);
    }

    public static int getPackedId(int packed) {
        return packed & 0x00FFFFFF;
    }

    public static boolean isPacked(int packed) {
        return (packed >>> 24) != 0;
    }

    public static String format(byte type, int id) {
        return getFolder(type) + "_" + id;
    }

    public static byte parseType(String strTypeId, byte defaultType) {
        if (strTypeId == null || strTypeId.isEmpty()) return defaultType;
        int sep = strTypeId.indexOf('_');
        if (sep == -1) sep = strTypeId.indexOf('/');
        if (sep != -1) {
            String prefix = strTypeId.substring(0, sep);
            byte t = getFolderType(prefix);
            if (t != -1) return t;
            try {
                return Byte.parseByte(prefix);
            } catch (Exception ignored) {}
        }
        return defaultType;
    }

    public static int parseId(String strTypeId, int defaultId) {
        if (strTypeId == null || strTypeId.isEmpty()) return defaultId;
        int sep = strTypeId.indexOf('_');
        if (sep == -1) sep = strTypeId.indexOf('/');
        String idPart = (sep != -1) ? strTypeId.substring(sep + 1) : strTypeId;
        int dot = idPart.indexOf('.');
        if (dot != -1) idPart = idPart.substring(0, dot);
        try {
            return Integer.parseInt(idPart);
        } catch (Exception ignored) {
            return defaultId;
        }
    }
}
