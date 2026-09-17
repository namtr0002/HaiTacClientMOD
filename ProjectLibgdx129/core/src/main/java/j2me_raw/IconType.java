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
        "item_map", "monster", "potion", "items", "skill", "skill_small",
        "npc", "questitem", "material", "clan", "boat", "dialog",
        "char_part", "fashion", "skill_combo", "clan_big", "other_new", "eff", "efflow"
    };

    public static String getFolder(byte type) {
        if (type >= 0 && type < FOLDERS.length) return FOLDERS[type];
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
        if (strTypeId == null || strTypeId.length() == 0) return defaultType;
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
        if (strTypeId == null || strTypeId.length() == 0) return defaultId;
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
