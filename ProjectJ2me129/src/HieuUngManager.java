public class HieuUngManager {
    public static mVector vecHieuUng = new mVector();
    public static MyHashTable disabledEffectIds = new MyHashTable();
    private static boolean isLoaded = false;
    private static final String RMS_NAME = "RMS_EFF_SETTINGS_V1";

    public static void init() {
        if (!isLoaded) {
            loadFromRMS();
            isLoaded = true;
        }
    }

    public static boolean isEffectDisabled(int id) {
        init();
        return disabledEffectIds.containsKey(String.valueOf(id));
    }

    public static boolean isDataSkillEffDisabled(short idEff) {
        if (idEff <= 0) return false;
        init();
        // Check if title effect is disabled
        if (isEffectDisabled(6)) {
            if (Player.vecDanhHieu != null) {
                for (int i = 0; i < Player.vecDanhHieu.size(); i++) {
                    DanhHieuInfo dh = (DanhHieuInfo)Player.vecDanhHieu.elementAt(i);
                    if (dh != null && dh.state == 2 && dh.idEff == idEff) {
                        return true;
                    }
                }
            }
        }
        return isEffectDisabled((int)idEff);
    }

    public static boolean isSetUpgradeDisabled() {
        init();
        return isEffectDisabled(1) || isEffectDisabled(2) || isEffectDisabled(3) || isEffectDisabled(4) || isEffectDisabled(5);
    }

    public static boolean isFashionEffectDisabled() {
        init();
        return isEffectDisabled(7);
    }

    public static boolean isMasteryDisabled() {
        if (!AThMadaraMOD.isShowMasteryEffect) return true;
        init();
        return isEffectDisabled(8);
    }

    public static void setEffectState(int id, boolean isEnabled) {
        init();
        if (isEnabled) {
            disabledEffectIds.remove(String.valueOf(id));
        } else {
            disabledEffectIds.put(String.valueOf(id), 1);
        }
        saveToRMS();

        // Update in vecHieuUng
        if (vecHieuUng != null) {
            for (int i = 0; i < vecHieuUng.size(); i++) {
                HieuUngInfo hu = (HieuUngInfo)vecHieuUng.elementAt(i);
                if (hu != null && hu.id == id) {
                    hu.state = (byte)(isEnabled ? 1 : 0);
                    hu.actionButtons.removeAllElements();
                    if (hu.state == 1) {
                        hu.actionButtons.addElement(new TitleActionBtn((byte)0, "Tắt Hiệu Ứng", (byte)2));
                    } else {
                        hu.actionButtons.addElement(new TitleActionBtn((byte)1, "Bật Hiệu Ứng", (byte)1));
                    }
                    break;
                }
            }
        }
    }

    public static void toggleHieuUng(int id) {
        boolean currentlyDisabled = isEffectDisabled(id);
        setEffectState(id, currentlyDisabled);
    }

    public static void scanActiveEffects() {
        init();
        mVector list = new mVector();

        // 1. Hào quang Set +11..+15
        int maxUpgrade = 0;
        if (Player.vecInventory != null) {
            for (int i = 0; i < Player.vecInventory.size(); i++) {
                MainItem item = (MainItem)Player.vecInventory.elementAt(i);
                if (item != null && item.typeObject == 3 && item.LvUpgrade > maxUpgrade) {
                    maxUpgrade = item.LvUpgrade;
                }
            }
        }
        if (GameScreen.player != null && GameScreen.player.levelPerfect > 0 && maxUpgrade < 11) {
            maxUpgrade = 11 + (GameScreen.player.levelPerfect - 1);
        }

        if (maxUpgrade >= 11) {
            int setLv = (maxUpgrade > 15) ? 15 : maxUpgrade;
            int effId = setLv - 10; // 1..5
            byte state = isEffectDisabled(effId) ? (byte)0 : (byte)1;
            HieuUngInfo hu = new HieuUngInfo(effId, "Hào Quang Set +" + setLv, "[Trang bị]", (short)-effId, (byte)effId, state, "Hào quang tỏa sáng quanh nhân vật khi trang bị set đồ cường hóa +" + setLv + ".");
            if (state == 1) hu.actionButtons.addElement(new TitleActionBtn((byte)0, "Tắt Hiệu Ứng", (byte)2));
            else hu.actionButtons.addElement(new TitleActionBtn((byte)1, "Bật Hiệu Ứng", (byte)1));
            list.addElement(hu);
        } else {
            // Mặc định cho phép cấu hình Set +11 nếu chưa đạt mốc
            byte state = isEffectDisabled(1) ? (byte)0 : (byte)1;
            HieuUngInfo hu = new HieuUngInfo(1, "Hào Quang Set +11", "[Trang bị]", (short)-1, (byte)1, state, "Hào quang tỏa sáng quanh nhân vật khi trang bị set đồ cường hóa +11 trở lên.");
            if (state == 1) hu.actionButtons.addElement(new TitleActionBtn((byte)0, "Tắt Hiệu Ứng", (byte)2));
            else hu.actionButtons.addElement(new TitleActionBtn((byte)1, "Bật Hiệu Ứng", (byte)1));
            list.addElement(hu);
        }

        // 2. Hào quang Danh Hiệu
        short activeTitleEff = -1;
        String activeTitleName = "";
        if (Player.vecDanhHieu != null) {
            for (int i = 0; i < Player.vecDanhHieu.size(); i++) {
                DanhHieuInfo dh = (DanhHieuInfo)Player.vecDanhHieu.elementAt(i);
                if (dh != null && dh.state == 2 && dh.idEff > 0) {
                    activeTitleEff = dh.idEff;
                    activeTitleName = dh.name;
                    break;
                }
            }
        }
        byte titleState = isEffectDisabled(6) ? (byte)0 : (byte)1;
        String titleDesc = (activeTitleEff > 0) ? ("Hiệu ứng ánh sáng từ danh hiệu: [" + activeTitleName + "]") : "Hiệu ứng ánh sáng vinh quang từ Danh Hiệu đang kích hoạt của nhân vật.";
        HieuUngInfo huTitle = new HieuUngInfo(6, "Hào Quang Danh Hiệu", "[Danh hiệu]", activeTitleEff, (byte)6, titleState, titleDesc);
        if (titleState == 1) huTitle.actionButtons.addElement(new TitleActionBtn((byte)0, "Tắt Hiệu Ứng", (byte)2));
        else huTitle.actionButtons.addElement(new TitleActionBtn((byte)1, "Bật Hiệu Ứng", (byte)1));
        list.addElement(huTitle);

        // 3. Hào quang Thần Trang
        byte fasState = isEffectDisabled(7) ? (byte)0 : (byte)1;
        HieuUngInfo huFas = new HieuUngInfo(7, "Hào Quang Thần Trang", "[Thần trang]", (short)-10, (byte)7, fasState, "Hiệu ứng lộng lẫy độc quyền tỏa ra từ bộ Thần Trang đang mặc trên người.");
        if (fasState == 1) huFas.actionButtons.addElement(new TitleActionBtn((byte)0, "Tắt Hiệu Ứng", (byte)2));
        else huFas.actionButtons.addElement(new TitleActionBtn((byte)1, "Bật Hiệu Ứng", (byte)1));
        list.addElement(huFas);

        // 4. Vòng Sao Tinh Thông
        byte masteryState = isEffectDisabled(8) ? (byte)0 : (byte)1;
        HieuUngInfo huMastery = new HieuUngInfo(8, "Vòng Sao Tinh Thông", "[Thành tích]", (short)-11, (byte)8, masteryState, "Vòng sao hiệu ứng cấp độ tinh thông bậc thầy hiển thị trên đầu nhân vật.");
        if (masteryState == 1) huMastery.actionButtons.addElement(new TitleActionBtn((byte)0, "Tắt Hiệu Ứng", (byte)2));
        else huMastery.actionButtons.addElement(new TitleActionBtn((byte)1, "Bật Hiệu Ứng", (byte)1));
        list.addElement(huMastery);

        vecHieuUng = list;
    }

    public static void saveToRMS() {
        try {
            if (disabledEffectIds == null || disabledEffectIds.size() == 0) {
                CRes.saveRMS(RMS_NAME, new byte[0]);
                return;
            }
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.DataOutputStream dos = new java.io.DataOutputStream(baos);
            dos.writeShort(disabledEffectIds.size());
            java.util.Enumeration keys = disabledEffectIds.keys();
            while (keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                dos.writeInt(Integer.parseInt(key));
            }
            CRes.saveRMS(RMS_NAME, baos.toByteArray());
        } catch (Exception ignored) {}
    }

    public static void loadFromRMS() {
        disabledEffectIds = new MyHashTable();
        try {
            byte[] data = CRes.loadRMS(RMS_NAME);
            if (data != null && data.length >= 2) {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(data);
                java.io.DataInputStream dis = new java.io.DataInputStream(bais);
                short count = dis.readShort();
                for (int i = 0; i < count; i++) {
                    int id = dis.readInt();
                    disabledEffectIds.put(String.valueOf(id), 1);
                }
            }
        } catch (Exception ignored) {}
    }
}
