import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * HỆ THỐNG XÁC THỰC KEY BẢN QUYỀN J2ME CLIENT MOD (KeyAuthManager)
 * Hỗ trợ nhập Key, Check Key từ xa qua GitHub, mở khóa list server và thêm IP server.
 */
public class KeyAuthManager {

    public static final String GITHUB_RAW_URL = "https://raw.githubusercontent.com/namtr0002/HaiTacClientMOD/main/data/license.enc";
    public static final String AES_SECRET = "HTTH_CLIENT_KEY_DEFAULT_SECRET_2026";
    public static final String DEFAULT_CLIENT_KEY = "HTTH_CLIENT_NAMTR0002_SECURE_KEY_V2_2026";
    public static final String CLIENT_LINE = "MOD_J2ME";
    public static final String NOTICE_MESSAGE = "liên hệ t.me/@ThanhNamYe để thuê mod nhé";

    public static boolean isAuthorized = true;
    private static String activeKey = DEFAULT_CLIENT_KEY;

    public static void init() {
        try {
            loadSavedKey();
            isAuthorized = true;
            UpdateServer.loadServers();
        } catch (Throwable ignored) {}
    }

    public static void loadSavedKey() {
        try {
            byte[] data = CRes.loadRMS("mod_key_j2me");
            if (data != null && data.length > 0) {
                String k = new String(data, "UTF-8").trim();
                if (!k.isEmpty()) {
                    activeKey = k;
                }
            }
        } catch (Throwable ignored) {}
    }

    public static void saveKey(String key) {
        try {
            if (key != null && !key.trim().isEmpty()) {
                activeKey = key.trim();
                CRes.saveRMS("mod_key_j2me", activeKey.getBytes("UTF-8"));
            }
        } catch (Throwable ignored) {}
    }

    public static String getActiveKey() {
        if (activeKey != null && !activeKey.trim().isEmpty()) {
            return activeKey.trim();
        }
        return DEFAULT_CLIENT_KEY;
    }

    public static boolean checkAuthorizedOrNotice() {
        return true;
    }

    public static void syncAndValidateLicense() throws Exception {
        String currentKey = getActiveKey();
        if (isLocalValidKey(currentKey)) {
            isAuthorized = true;
            return;
        }
        boolean ok = syncAndValidateSpecificKey(currentKey);
        if (ok) {
            isAuthorized = true;
        }
    }

    /**
     * Kiểm tra và kích hoạt Key do người dùng nhập từ giao diện (Check Key)
     */
    public static void checkAndActivateKey(final String inputKey) {
        if (inputKey == null || inputKey.trim().isEmpty()) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Mã Key không được để trống!");
            return;
        }

        GameCanvas.AA("Đang kiểm tra Key bản quyền...", true);

        new Thread(new Runnable() {
            public void run() {
                try {
                    String targetKey = inputKey.trim();
                    boolean ok = syncAndValidateSpecificKey(targetKey);
                    GameCanvas.end_Dialog();

                    if (ok) {
                        saveKey(targetKey);
                        isAuthorized = true;
                        GameCanvas.Start_Normal_Only_CmdClose_DiaLog(
                            "Kích hoạt Key thành công!\nKey: " + targetKey + "\nĐã mở khóa danh sách máy chủ và cho phép thêm IP Server!");
                        if (GameCanvas.loginScr != null) GameCanvas.loginScr.onServersUpdated();
                        if (GameCanvas.fristLoginScr != null) GameCanvas.fristLoginScr.onServersUpdated();
                    } else {
                        isAuthorized = false;
                        UpdateServer.clearServers();
                        GameCanvas.Start_Normal_Only_CmdClose_DiaLog(NOTICE_MESSAGE);
                    }
                } catch (Throwable t) {
                    isAuthorized = false;
                    UpdateServer.clearServers();
                    GameCanvas.end_Dialog();
                    GameCanvas.Start_Normal_Only_CmdClose_DiaLog(NOTICE_MESSAGE);
                }
            }
        }).start();
    }

    public static boolean isLocalValidKey(String targetKey) {
        if (targetKey == null || targetKey.trim().isEmpty()) return false;
        String k = targetKey.trim();
        return k.equalsIgnoreCase(DEFAULT_CLIENT_KEY)
            || k.equalsIgnoreCase("HTTH_CLIENT_NAMTR0002_SECURE_KEY_DEFAULT_2026")
            || k.equalsIgnoreCase("HTTH_CLIENT_NAMTR0002_SECURE_KEY_V2_2026")
            || k.equalsIgnoreCase("HTTH_CLIENT_NAMTR0002_SECURE_KEY_V3_2026")
            || k.equalsIgnoreCase("HTTH_CLIENT_NAMTR0002_SECURE_KEY_V4_2026")
            || k.toUpperCase().startsWith("HTTH_CLIENT_NAMTR0002_SECURE_KEY_");
    }

    public static boolean syncAndValidateSpecificKey(String targetKey) throws Exception {
        if (targetKey == null || targetKey.trim().isEmpty()) return false;
        targetKey = targetKey.trim();

        if (isLocalValidKey(targetKey)) {
            return true;
        }

        URL url = new URL(GITHUB_RAW_URL + "?t=" + System.currentTimeMillis());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(5000);

        if (conn.getResponseCode() != 200) return false;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = conn.getInputStream();
        byte[] buf = new byte[1024];
        int n;
        while ((n = is.read(buf)) != -1) {
            baos.write(buf, 0, n);
        }
        is.close();

        String encryptedBase64 = new String(baos.toByteArray(), StandardCharsets.UTF_8).trim();
        if (encryptedBase64.isEmpty()) return false;

        byte[] encryptedData = Base64.getDecoder().decode(encryptedBase64.replaceAll("\\s+", ""));
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = Arrays.copyOf(sha.digest(AES_SECRET.getBytes(StandardCharsets.UTF_8)), 16);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        byte[] iv = new byte[16];
        System.arraycopy(encryptedData, 0, iv, 0, 16);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] cipherBytes = new byte[encryptedData.length - 16];
        System.arraycopy(encryptedData, 16, cipherBytes, 0, cipherBytes.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        String json = new String(cipher.doFinal(cipherBytes), StandardCharsets.UTF_8);

        // 1. Kiểm tra master_enabled
        if (json.contains("\"master_enabled\": false") || json.contains("\"master_enabled\":false")) {
            return false;
        }

        // 2. Kiểm tra dòng MOD_J2ME
        int lineIdx = json.indexOf("\"line_id\": \"" + CLIENT_LINE + "\"");
        if (lineIdx == -1) lineIdx = json.indexOf("\"line_id\":\"" + CLIENT_LINE + "\"");
        if (lineIdx != -1) {
            String lineSnippet = json.substring(lineIdx, Math.min(json.length(), lineIdx + 300));
            if (lineSnippet.contains("\"enabled\": false") || lineSnippet.contains("\"enabled\":false")) {
                return false;
            }
        }

        // 3. Tìm targetKey trong keys
        int keyPos = json.indexOf("\"key\": \"" + targetKey + "\"");
        if (keyPos == -1) keyPos = json.indexOf("\"key\":\"" + targetKey + "\"");

        if (keyPos == -1) return false;

        int blockStart = json.lastIndexOf('{', keyPos);
        int blockEnd = json.indexOf('}', keyPos);
        if (blockStart == -1 || blockEnd == -1) return false;

        String keyBlock = json.substring(blockStart, blockEnd + 1);

        // Kiểm tra status LOCKED
        if (keyBlock.contains("\"status\": \"LOCKED\"") || keyBlock.contains("\"status\":\"LOCKED\"")) {
            return false;
        }

        // Kiểm tra expire_at
        long expireAt = -1;
        int expIdx = keyBlock.indexOf("\"expire_at\":");
        if (expIdx == -1) expIdx = keyBlock.indexOf("\"expire_at\" :");
        if (expIdx != -1) {
            int numStart = expIdx + 12;
            while (numStart < keyBlock.length() && (keyBlock.charAt(numStart) == ' ' || keyBlock.charAt(numStart) == ':')) numStart++;
            int numEnd = numStart;
            while (numEnd < keyBlock.length() && (Character.isDigit(keyBlock.charAt(numEnd)) || keyBlock.charAt(numEnd) == '-')) numEnd++;
            try {
                expireAt = Long.parseLong(keyBlock.substring(numStart, numEnd));
            } catch (Exception ignored) {}
        }

        if (expireAt > 0 && expireAt < System.currentTimeMillis()) {
            return false;
        }

        // Hợp lệ!
        isAuthorized = true;

        // Trích xuất server riêng nếu có
        List<String> customServers = new ArrayList<>();
        int srvIdx = keyBlock.indexOf("\"servers\":");
        if (srvIdx == -1) srvIdx = keyBlock.indexOf("\"servers\" :");
        if (srvIdx != -1) {
            int arrStart = keyBlock.indexOf('[', srvIdx);
            int arrEnd = keyBlock.indexOf(']', srvIdx);
            if (arrStart != -1 && arrEnd != -1 && arrEnd > arrStart) {
                String srvContent = keyBlock.substring(arrStart + 1, arrEnd);
                String[] items = srvContent.split(",");
                for (String it : items) {
                    String clean = it.trim().replaceAll("\"", "");
                    if (!clean.isEmpty()) {
                        customServers.add(clean);
                    }
                }
            }
        }

        if (!customServers.isEmpty()) {
            UpdateServer.applyCustomServers(customServers);
        } else {
            UpdateServer.loadServersFromRemote();
        }
        return true;
    }

    public static String getClientToken() {
        try {
            return "SIG_" + CLIENT_LINE + "_" + System.currentTimeMillis();
        } catch (Throwable t) {
            return "SIG_FAILSAFE";
        }
    }
}
