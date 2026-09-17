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
 * HỆ THỐNG XÁC THỰC KEY BẢN QUYỀN J2ME CLIENT MOD (KeyAuthManager)
 */
public class KeyAuthManager {

    public static final String GITHUB_RAW_URL = "https://raw.githubusercontent.com/namtr0002/HaiTacClientMOD/main/data/license.enc";
    public static final String AES_SECRET = "HTTH_CLIENT_KEY_DEFAULT_SECRET_2026";
    public static final String DEFAULT_CLIENT_KEY = "HTTH_CLIENT_NAMTR0002_SECURE_KEY_V4_2026";
    public static final String CLIENT_LINE = "MOD_J2ME";

    private static boolean masterEnabled = true;
    private static boolean lineEnabled = true;
    private static boolean lineRequireKey = false; // Mặc định J2ME là Free mode

    private static String activeKey = "";
    private static boolean isKeyValid = true;

    public static void init() {
        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        syncFromGitHub();
                    } catch (Throwable ignored) {}
                }
            }).start();
        } catch (Throwable ignored) {}
    }

    public static boolean isEnabled() {
        return true;
    }

    public static boolean canPlay() {
        return true;
    }

    public static String getActiveKey() {
        if (activeKey != null && !activeKey.trim().isEmpty()) {
            return activeKey.trim();
        }
        return DEFAULT_CLIENT_KEY;
    }

    public static void syncFromGitHub() throws Exception {
        URL url = java.net.URI.create(GITHUB_RAW_URL + "?t=" + System.currentTimeMillis()).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(10000);

        if (conn.getResponseCode() != 200) return;

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

        if (json.contains("\"master_enabled\": false")) {
            masterEnabled = false;
        } else if (json.contains("\"master_enabled\": true")) {
            masterEnabled = true;
        }

        int lineIdx = json.indexOf("\"line_id\": \"" + CLIENT_LINE + "\"");
        if (lineIdx != -1) {
            String lineSnippet = json.substring(lineIdx, Math.min(json.length(), lineIdx + 400));
            if (lineSnippet.contains("\"enabled\": false")) lineEnabled = false;
            else if (lineSnippet.contains("\"enabled\": true")) lineEnabled = true;

            if (lineSnippet.contains("\"require_key\": false")) lineRequireKey = false;
            else if (lineSnippet.contains("\"require_key\": true")) lineRequireKey = true;
        }
    }

    public static String getClientToken() {
        try {
            return "SIG_" + CLIENT_LINE + "_" + System.currentTimeMillis();
        } catch (Throwable t) {
            return "SIG_FAILSAFE";
        }
    }
}
