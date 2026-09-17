
import java.util.Enumeration;

public final class ObjectData {

    public static MyHashTable HashImageItemMap;
    public static MyHashTable HashImageMonster;
    public static MyHashTable hashImageItem;
    public static MyHashTable hashImagePotion;
    public static MyHashTable hashImageSkill;
    public static MyHashTable hashImageSkillSmall;
    public static MyHashTable hashImageNPC;
    public static MyHashTable hashImageQuestPotion;
    public static MyHashTable hashImageMaterialPotion;
    public static MyHashTable hashImageIconClan;
    public static MyHashTable hashImageIconClanBig;
    public static MyHashTable hashImageBoat;
    public static MyHashTable hashImageItemOther;
    public static MyHashTable HashImageCharPart;
    public static MyHashTable HashImageFashion;
    public static MyHashTable HashImageOtherNew;
    public static MyHashTable HashImageEffClient;
    public static MyHashTable HashImageEffClientLow;

    static {
        new mVector("ObjectData.vecSaveImage");
        HashImageItemMap = new MyHashTable(IconType.ITEM_MAP, "/server/item_map/");
        HashImageMonster = new MyHashTable(IconType.MONSTER, "/server/monster/");
        hashImageItem = new MyHashTable(IconType.ITEM, "/server/items/");
        hashImagePotion = new MyHashTable(IconType.POTION, "/server/potion/");
        hashImageSkill = new MyHashTable(IconType.SKILL, "/server/skill/");
        hashImageSkillSmall = new MyHashTable(IconType.SKILL_SMALL, "/server/skill_small/");
        hashImageNPC = new MyHashTable(IconType.NPC, "/server/npc/");
        hashImageQuestPotion = new MyHashTable(IconType.QUEST_POTION, "/server/questitem/");
        hashImageMaterialPotion = new MyHashTable(IconType.MATERIAL_POTION, "/server/material/");
        hashImageIconClan = new MyHashTable(IconType.CLAN, "/server/Clan/");
        hashImageIconClanBig = new MyHashTable(IconType.CLAN_BIG, "/server/ClanBig/");
        hashImageBoat = new MyHashTable(IconType.BOAT, "");
        hashImageItemOther = new MyHashTable(IconType.ITEM_OTHER, "/server/dialog/");
        HashImageCharPart = new MyHashTable(IconType.CHAR_PART, "/server/char_part/Small");
        HashImageFashion = new MyHashTable(IconType.FASHION, "/server/itemFashion/");
        HashImageOtherNew = new MyHashTable(IconType.OTHER_NEW, "/server/hinhtonghop/");
        HashImageEffClient = new MyHashTable(IconType.EFF_CLIENT, "/eff/");
        HashImageEffClientLow = new MyHashTable(IconType.EFF_CLIENT_LOW, "/efflow/");
    }

    public static MyHashTable getTable(byte type) {
        switch (type) {
            case IconType.ITEM_MAP: return HashImageItemMap;
            case IconType.MONSTER: return HashImageMonster;
            case IconType.POTION: return hashImagePotion;
            case IconType.ITEM: return hashImageItem;
            case IconType.SKILL: return hashImageSkill;
            case IconType.SKILL_SMALL: return hashImageSkillSmall;
            case IconType.NPC: return hashImageNPC;
            case IconType.QUEST_POTION: return hashImageQuestPotion;
            case IconType.MATERIAL_POTION: return hashImageMaterialPotion;
            case IconType.CLAN: return hashImageIconClan;
            case IconType.BOAT: return hashImageBoat;
            case IconType.ITEM_OTHER: return hashImageItemOther;
            case IconType.CHAR_PART: return HashImageCharPart;
            case IconType.FASHION: return HashImageFashion;
            case IconType.SKILL_COMBO: return hashImageSkill;
            case IconType.CLAN_BIG: return hashImageIconClanBig;
            case IconType.OTHER_NEW: return HashImageOtherNew;
            case IconType.EFF_CLIENT: return HashImageEffClient;
            case IconType.EFF_CLIENT_LOW: return HashImageEffClientLow;
            default: return null;
        }
    }

    private static final String[] KEY_CACHE = new String[4096];

    public static String getKey(short id) {
        if (id >= 0 && id < 4096) {
            String s = KEY_CACHE[id];
            if (s == null) {
                s = String.valueOf((int) id);
                KEY_CACHE[id] = s;
            }
            return s;
        }
        return String.valueOf((int) id);
    }

    public static MainImage getImage(byte type, int id) {
        return getImageByType(type, id);
    }

    public static MainImage getImageByType(byte type, int id) {
        MyHashTable table = getTable(type);
        if (table == null) return null;
        return getImage(table, id);
    }

    public static MainImage getImage(MyHashTable table, int id) {
        if (id < 0 || table == null) return null;
        String key = (id < 4096) ? getKey((short) id) : String.valueOf(id);
        MainImage imgObj = (MainImage) table.get(key);
        if (imgObj == null) {
            imgObj = new MainImage();
            table.put(key, imgObj);
            imgObj.img = getFromRms(id, table.typeKey, table);
            imgObj.AA();
        }

        imgObj.AE = GameCanvas.timeNow / 1000L;
        if (imgObj.img == null) {
            ++imgObj.AF;
            if (imgObj.AF == 1 || imgObj.AF >= 200) {
                if (table.typeKey >= 0) {
                    GlobalService.getInstance().load_image(table.typeKey, id);
                }
                if (imgObj.AF >= 200) {
                    imgObj.AF = 1;
                }
            }
        }
        return imgObj;
    }

    public static MainImage getImage(int packedTypeId) {
        if (IconType.isPacked(packedTypeId)) {
            byte type = IconType.getPackedType(packedTypeId);
            int id = IconType.getPackedId(packedTypeId);
            return getImageByType(type, id);
        }
        return getImageByType(IconType.ITEM, packedTypeId);
    }

    public static MainImage getImage(String strTypeId) {
        if (strTypeId == null || strTypeId.length() == 0) return null;
        byte type = IconType.parseType(strTypeId, IconType.ITEM);
        int id = IconType.parseId(strTypeId, -1);
        if (id < 0) return null;
        return getImageByType(type, id);
    }

    public static MainImage getImageOther(short var0, short var1) {
        return getImageByType(IconType.OTHER_NEW, var0 + var1);
    }

    public static MainImage getImageAll(short var0, MyHashTable var1, short var2) {
        return getImageAll((int) var0, var1, var2);
    }

    public static MainImage getImageAll(int id, MyHashTable table, short legacyOffset) {
        if (id < 0) return null;
        if (table != null) {
            return getImage(table, id);
        }
        return null;
    }

    private static mImage getFromRms(int id, int legacyOffset, MyHashTable table) {
        if (id < 0) return null;
        byte type = (table != null && table.typeKey >= 0) ? table.typeKey : (byte) legacyOffset;
        byte[] data = null;
        try {
            data = CRes.loadRMS("SUB_img_" + type + "_" + id);
        } catch (Exception ignored) {}

        if (data == null) {
            GlobalService.getInstance().load_image(type, id);
            return null;
        }

        try {
            return mImage.AA(data);
        } catch (Exception var3) {
            GlobalService.getInstance().load_image(type, id);
            return null;
        }
    }

    public static boolean setIdOK() {
        return false;
    }

    public static void setToRms(byte[] mimg, byte type, int id) {
        try {
            CRes.saveRMS("SUB_img_" + type + "_" + id, mimg);
        } catch (Exception ignored) {}
    }

    public static void setToRms(byte[] mimg, short id) {
        try {
            CRes.saveRMS("SUB_image" + id, mimg);
        } catch (Exception var2) {
        }
    }

    public static void saveImageToRmsAndroid(byte[] mimg, String name) {
        try {
            CRes.saveRMS("Main_Image_" + name, mimg);
        } catch (Exception var2) {
        }
    }

    public static void checkDelHash(MyHashTable var0, int var1, boolean var2) {
        if (!var2 && (var0 == HashImageEffClient || var0 == HashImageEffClientLow)) {
            return;
        }
        mVector var5 = new mVector();
        Enumeration var6 = var0.GetEnumerator();

        while (var6.hasMoreElements()) {
            String var3 = (String) var6.nextElement();
            MainImage var4 = (MainImage) var0.get(var3);
            if (GameCanvas.timeNow / 1000L - var4.AE > 120L) {
                var5.addElement(var3);
            }
        }

        for (int var7 = 0; var7 < var5.size(); ++var7) {
            var0.remove(var5.elementAt(var7));
        }

    }

    public static void checkDelHash_Data(MyHashTable var0, int var1, boolean var2) {
        mVector var5 = new mVector();
        Enumeration var6 = var0.GetEnumerator();

        while (var6.hasMoreElements()) {
            String var3 = (String) var6.nextElement();
            EffectData var4 = (EffectData) var0.get(var3);
            if (GameCanvas.timeNow / 1000L - var4.count > 120L) {
                var5.addElement(var3);
            }
        }

        for (int var7 = 0; var7 < var5.size(); ++var7) {
            var0.remove(var5.elementAt(var7));
        }

    }
}
