import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * HỆ THỐNG XÁC THỰC KEY BẢN QUYỀN CLIENT MOD (KeyAuthManager)
 * Tự động đồng bộ và giải mã cấu hình bảo mật từ GitHub:
 * https://github.com/namtr0002/HaiTacClientMOD
 */
public class KeyAuthManager {

    public static final String GITHUB_RAW_URL = "https://raw.githubusercontent.com/namtr0002/HaiTacClientMOD/main/data/license.enc";
    public static final String AES_SECRET = "HTTH_CLIENT_KEY_DEFAULT_SECRET_2026";
    public static final String CLIENT_LINE = "MOD_LIBGDX";

    private static boolean masterEnabled = true;
    private static boolean lineEnabled = true;
    private static boolean lineRequireKey = true;

    private static String activeKey = "";
    private static boolean isKeyValid = false;
    private static String keyExpireStr = "";
    private static String remainingTimeStr = "";
    private static String clientNotice = "";
    private static boolean hasChecked = false;

    private static final String KEY_FILE_NAME = "mod_key.txt";

    /**
     * Khởi chạy luồng đồng bộ ngầm khi mở ứng dụng game
     */
    public static void init() {
        try {
            loadSavedKey();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        syncFromGitHub();
                    } catch (Throwable t) {
                        // Fail-soft: Mất kết nối GitHub không làm crash game
                    }
                }
            }).start();
        } catch (Throwable ignored) {}
    }

    /**
     * Kiểm tra xem hệ thống có đang BẬT kiểm tra key hay không
     */
    public static boolean isEnabled() {
        try {
            return masterEnabled && lineEnabled;
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean isRequireKey() {
        try {
            return isEnabled() && lineRequireKey;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Kiểm tra xem Client có đủ điều kiện vào game không
     */
    public static boolean canPlay() {
        try {
            // Nếu tắt kiểm tra key -> cho phép chơi ngay (Free)
            if (!isEnabled()) {
                return true;
            }
            // Nếu dòng này không bắt buộc nhập key (Free Mode)
            if (!lineRequireKey) {
                return true;
            }
            // Nếu cần key: phải có key hợp lệ
            return isKeyValid;
        } catch (Throwable t) {
            return true; // Fail-soft fallback
        }
    }

    public static String getActiveKey() {
        return activeKey != null ? activeKey : "";
    }

    public static boolean isKeyValid() {
        return isKeyValid;
    }

    public static String getKeyExpireStr() {
        return keyExpireStr != null ? keyExpireStr : "";
    }

    public static String getRemainingTimeStr() {
        return remainingTimeStr != null ? remainingTimeStr : "";
    }

    public static String getClientNotice() {
        return clientNotice != null ? clientNotice : "";
    }

    /**
     * Tải và giải mã license từ GitHub Raw
     */
    public static void syncFromGitHub() throws Exception {
        URL url = java.net.URI.create(GITHUB_RAW_URL + "?t=" + System.currentTimeMillis()).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(10000);

        if (conn.getResponseCode() != 200) {
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = conn.getInputStream();
        byte[] buf = new byte[1024];
        int n;
        while ((n = is.read(buf)) != -1) {
            baos.write(buf, 0, n);
        }
        is.close();

        String encryptedBase64 = new String(baos.toByteArray(), StandardCharsets.UTF_8).trim();
        if (encryptedBase64.isEmpty()) return;

        String decryptedJson = decryptAes(encryptedBase64, AES_SECRET);
        parseLicenseJson(decryptedJson);
        hasChecked = true;
    }

    private static String decryptAes(String base64Cipher, String passphrase) throws Exception {
        byte[] encryptedData = Base64.getDecoder().decode(base64Cipher.replaceAll("\\s+", ""));
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = Arrays.copyOf(sha.digest(passphrase.getBytes(StandardCharsets.UTF_8)), 16);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        byte[] iv = new byte[16];
        System.arraycopy(encryptedData, 0, iv, 0, 16);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] cipherBytes = new byte[encryptedData.length - 16];
        System.arraycopy(encryptedData, 16, cipherBytes, 0, cipherBytes.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return new String(cipher.doFinal(cipherBytes), StandardCharsets.UTF_8);
    }

    private static void parseLicenseJson(String json) {
        try {
            // Master enabled
            if (json.contains("\"master_enabled\": false")) {
                masterEnabled = false;
            } else if (json.contains("\"master_enabled\": true")) {
                masterEnabled = true;
            }

            // Line check
            int lineIdx = json.indexOf("\"line_id\": \"" + CLIENT_LINE + "\"");
            if (lineIdx != -1) {
                String lineSnippet = json.substring(lineIdx, Math.min(json.length(), lineIdx + 400));
                if (lineSnippet.contains("\"enabled\": false")) {
                    lineEnabled = false;
                } else if (lineSnippet.contains("\"enabled\": true")) {
                    lineEnabled = true;
                }
                if (lineSnippet.contains("\"require_key\": false")) {
                    lineRequireKey = false;
                } else if (lineSnippet.contains("\"require_key\": true")) {
                    lineRequireKey = true;
                }
            }

            // Kiểm tra key đã lưu
            isKeyValid = false;
            if (activeKey != null && !activeKey.trim().isEmpty()) {
                String keySearch = "\"key\": \"" + activeKey.trim() + "\"";
                int kIdx = json.indexOf(keySearch);
                if (kIdx != -1) {
                    String kSnippet = json.substring(kIdx, Math.min(json.length(), kIdx + 500));
                    boolean isActive = kSnippet.contains("\"status\": \"ACTIVE\"");
                    long expireAt = -1;
                    int expIdx = kSnippet.indexOf("\"expire_at\":");
                    if (expIdx != -1) {
                        int commaIdx = kSnippet.indexOf(",", expIdx);
                        if (commaIdx != -1) {
                            String numStr = kSnippet.substring(expIdx + 12, commaIdx).replaceAll("[^0-9-]", "");
                            try { expireAt = Long.parseLong(numStr); } catch (Exception ignored) {}
                        }
                    }

                    if (isActive) {
                        if (expireAt <= 0) {
                            // Vĩnh viễn
                            isKeyValid = true;
                            remainingTimeStr = "Vĩnh viễn";
                            keyExpireStr = "Vĩnh viễn";
                        } else if (System.currentTimeMillis() <= expireAt) {
                            // Còn hạn
                            isKeyValid = true;
                            long diff = expireAt - System.currentTimeMillis();
                            long days = diff / (24 * 60 * 60 * 1000);
                            long hours = (diff % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
                            remainingTimeStr = days + " ngày " + hours + "h";
                            keyExpireStr = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(expireAt));
                        } else {
                            // Hết hạn
                            isKeyValid = false;
                            remainingTimeStr = "ĐÃ HẾT HẠN";
                            keyExpireStr = "Hết hạn";
                        }
                    } else {
                        // Bị khóa
                        isKeyValid = false;
                        remainingTimeStr = "ĐÃ BỊ KHÓA";
                    }
                }
            }
        } catch (Throwable ignored) {}
    }

    public static boolean activateKey(String newKey) {
        try {
            if (newKey == null || newKey.trim().isEmpty()) return false;
            activeKey = newKey.trim();
            saveKey(activeKey);
            syncFromGitHub();
            return isKeyValid;
        } catch (Throwable t) {
            return false;
        }
    }

    public static void loadSavedKey() {
        try {
            File f = getStorageFile();
            if (f != null && f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                activeKey = br.readLine();
                br.close();
                if (activeKey != null) activeKey = activeKey.trim();
            }
        } catch (Throwable ignored) {}
    }

    public static void saveKey(String key) {
        try {
            File f = getStorageFile();
            if (f != null) {
                f.getParentFile().mkdirs();
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                bw.write(key != null ? key.trim() : "");
                bw.close();
            }
        } catch (Throwable ignored) {}
    }

    private static File getStorageFile() {
        try {
            File dir = new File("Data/rms");
            if (!dir.exists()) dir = new File("rms");
            if (!dir.exists()) dir = new File(".");
            return new File(dir, KEY_FILE_NAME);
        } catch (Throwable t) {
            return new File(KEY_FILE_NAME);
        }
    }

    public static String getClientToken() {
        try {
            return "SIG_" + CLIENT_LINE + "_" + System.currentTimeMillis();
        } catch (Throwable t) {
            return "SIG_FAILSAFE";
        }
    }

    /**
     * Hiển thị thông báo yêu cầu nhập Key hoặc gia hạn
     */
    public static void showKeyNoticeDialog() {
        try {
            String notice;
            if (activeKey == null || activeKey.trim().isEmpty()) {
                notice = "Phiên bản MOD Hải Tặc yêu cầu Key kích hoạt!\nVui lòng liên hệ Admin để nhận Key hoặc nhập Key trong Menu MOD.";
            } else if (!isKeyValid) {
                notice = "Key của bạn (" + activeKey + ") " + (remainingTimeStr.isEmpty() ? "không hợp lệ hoặc đã hết hạn!" : remainingTimeStr + "!") + "\nVui lòng liên hệ Admin để gia hạn.";
            } else {
                notice = "Key MOD: " + activeKey + "\nThời hạn: " + remainingTimeStr;
            }
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(notice);
        } catch (Throwable ignored) {}
    }
}
