
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
        HashImageItemMap = new MyHashTable("/server/item_map/");
        HashImageMonster = new MyHashTable("/server/monster/");
        hashImageItem = new MyHashTable("/server/items/");
        hashImagePotion = new MyHashTable("/server/potion/");
        hashImageSkill = new MyHashTable("/server/skill/");
        hashImageSkillSmall = new MyHashTable("/server/skill_small/");
        hashImageNPC = new MyHashTable("/server/npc/");
        hashImageQuestPotion = new MyHashTable("/server/questitem/");
        hashImageMaterialPotion = new MyHashTable("/server/material/");
        hashImageIconClan = new MyHashTable("/server/Clan/");
        hashImageIconClanBig = new MyHashTable("/server/ClanBig/");
        hashImageBoat = new MyHashTable();
        hashImageItemOther = new MyHashTable("/server/dialog/");
        HashImageCharPart = new MyHashTable("/server/char_part/Small");
        HashImageFashion = new MyHashTable("/server/itemFashion/");
        HashImageOtherNew = new MyHashTable("/server/hinhtonghop/");
        HashImageEffClient = new MyHashTable("/eff/");
        HashImageEffClientLow = new MyHashTable("/efflow/");
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

    public static MainImage getImageOther(short var0, short var1) {
        return getImageAll((short) (var0 + var1), HashImageOtherNew, (short) 23000);
    }

    public static MainImage getImageAll(short var0, MyHashTable var1, short var2) {
        if (var0 == -1) {
            return null;
        } else {
            String var4 = getKey(var0);
            MainImage var5;
            if ((var5 = (MainImage) var1.get(var4)) == null) {
                var5 = new MainImage();
                var1.put(var4, var5);
                var5.img = getFromRms(var0, var2, var1);
                var5.AA();
            }

            var5.AE = GameCanvas.timeNow / 1000L;
            if (var5.img == null) {
                ++var5.AF;
                if (var5.AF == 1 || var5.AF >= 200) {
                    GlobalService.getInstance().load_image(var0, var2);
                    if (var5.AF >= 200) {
                        var5.AF = 1;
                    }
                }
            }

            return var5;
        }
    }

    private static mImage getFromRms(short var0, int var1, MyHashTable var2) {
        if (var1 == 10000 && var0 > 10000) {
            var1 = 26000;
            var0 = (short) (var0 - 10000);
        }

        byte[] data = null;
        if (var0 >= 0) {
            try {
                data = CRes.loadRMS("SUB_image" + (var0 + var1));
            } catch (Exception ignored) {}
        }

        if (data == null) {
            if (var0 >= 0) {
                GlobalService.getInstance().load_image(var0, (short) var1);
            }
            return null;
        } else {
            try {
                return mImage.AA(data);
            } catch (Exception var3) {
                if (var0 >= 0) {
                    GlobalService.getInstance().load_image(var0, (short) var1);
                }
                return null;
            }
        }
    }

    public static boolean setIdOK() {
        return false;
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
