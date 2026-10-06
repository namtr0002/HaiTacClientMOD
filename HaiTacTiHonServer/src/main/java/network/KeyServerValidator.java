package network;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * HỆ THỐNG XÁC THỰC BẢN QUYỀN KEY & CHẶN MOD LẬU TRÊN SERVER
 * Tự động đồng bộ cấu hình mã hóa bảo mật từ GitHub:
 * https://github.com/namtr0002/HaiTacClientMOD
 */
public class KeyServerValidator {

    public static final String GITHUB_RAW_URL = "https://raw.githubusercontent.com/namtr0002/HaiTacClientMOD/main/data/license.enc";
    public static final String AES_SECRET = "HTTH_CLIENT_KEY_DEFAULT_SECRET_2026";
    public static final String DEFAULT_SECRET_KEY = "HTTH-HAITACZ-2026-PRO";

    private static boolean masterEnabled = true;
    private static boolean serverCheckEnabled = true;
    private static String localSecretKey = "HTTH-HAITACZ-2026-PRO";
    private static long lastSyncTime = 0;
    private static final long CACHE_EXPIRE_MS = 60 * 1000; // Đồng bộ mỗi 60s

    private static final Map<String, CachedKey> VALID_KEYS = new HashMap<String, CachedKey>();
    private static final Set<String> FREE_LINES = new HashSet<String>();
    private static final Map<String, String> LINE_NOTICES = new HashMap<String, String>();
    private static String lastRejectNotice = "Vui lòng cập nhật phiên bản mới tại haitacz.xyz/tai-game";

    public static class CachedKey {
        public String key;
        public String lineId;
        public String status;
        public long expireAt;
        public List<String> allowedIps = new ArrayList<String>();
    }

    public static boolean isKeyCheckEnabled() {
        return masterEnabled && serverCheckEnabled;
    }

    public static void setServerCheckEnabled(boolean enabled) {
        serverCheckEnabled = enabled;
    }

    public static void setLocalSecretKey(String key) {
        if (key != null && !key.trim().isEmpty()) {
            localSecretKey = key.trim();
        }
    }

    public static String getLocalSecretKey() {
        return localSecretKey;
    }

    public static String getLastRejectNotice() {
        return lastRejectNotice != null ? lastRejectNotice : "Vui lòng cập nhật phiên bản mới tại haitacz.xyz/tai-game";
    }

    private static String getRejectNoticeForToken(String clientToken) {
        if (clientToken != null) {
            for (Map.Entry<String, String> entry : LINE_NOTICES.entrySet()) {
                if (clientToken.contains(entry.getKey()) && !entry.getValue().isEmpty()) {
                    return entry.getValue();
                }
            }
        }
        return lastRejectNotice != null && !lastRejectNotice.isEmpty()
                ? lastRejectNotice : "Vui lòng cập nhật phiên bản mới tại haitacz.xyz/tai-game";
    }

    /**
     * Xác thực Client khi đăng nhập vào Server:
     * Trả về TRUE nếu hợp lệ, FALSE nếu phiên bản mod lậu hoặc key sai/hết hạn.
     */
    public static boolean validateClient(String clientIp, String clientKey, String clientToken) {
        // Nếu Server hoặc Master tắt kiểm tra, cho phép tất cả kết nối bình thường (kể cả HaiTacZ)
        if (!isKeyCheckEnabled()) {
            return true;
        }

        // 1. Kiểm tra Token client mod
        // Client HaiTacZ gốc hoặc mod ngoài không có token hoặc token rỗng -> CHẶN NGAY!
        if (clientToken == null || clientToken.trim().isEmpty() || !clientToken.startsWith("SIG_")) {
            lastRejectNotice = getRejectNoticeForToken(clientToken);
            return false;
        }

        // 2. Tách mã key nếu có prefix
        if (clientKey != null && clientKey.startsWith("HTTH_KEY:")) {
            clientKey = clientKey.substring("HTTH_KEY:".length()).trim();
        }

        // 3. Khóa bí mật nội bộ cấu hình tại server / mặc định -> Hợp lệ ngay lập tức
        if (clientKey != null && !clientKey.isEmpty()) {
            if (localSecretKey != null && clientKey.equalsIgnoreCase(localSecretKey.trim())) {
                return true;
            }
            if (clientKey.equalsIgnoreCase("HTTH-HAITACZ-2026-PRO")
                    || clientKey.equalsIgnoreCase("HTTH-UNI-HAITACZ-2026-PRO")
                    || clientKey.equalsIgnoreCase("HTTH-J2ME-HAITACZ-2026-PRO")
                    || clientKey.equalsIgnoreCase("HTTH-HAITACZ-129-PRO")
                    || clientKey.equalsIgnoreCase("HTTH-UNI-HAITACZ-129-PRO")
                    || clientKey.equalsIgnoreCase("HTTH-J2ME-HAITACZ-129-PRO")
                    || clientKey.equalsIgnoreCase("HAITACZ_KEY_2026_SECURE")
                    || clientKey.equalsIgnoreCase("HTTH-UNI-HAITACZ-2026-X9K")
                    || clientKey.equalsIgnoreCase("HTTH-J2ME-HAITACZ-2026-K9S")
                    || clientKey.equalsIgnoreCase("HaiTacZ")
                    || clientKey.equalsIgnoreCase("HaiTacZ2026")
                    || clientKey.equalsIgnoreCase("HTTH_CLIENT_NAMTR0002_SECURE_KEY_V2_2026")
                    || clientKey.equalsIgnoreCase("HTTH_CLIENT_NAMTR0002_SECURE_KEY_V3_2026")
                    || clientKey.equalsIgnoreCase("HTTH_CLIENT_NAMTR0002_SECURE_KEY_V4_2026")
                    || clientKey.equalsIgnoreCase("KhanhDoan5M")
                    || clientKey.equalsIgnoreCase("KPAH5G_Khanh")
                    || clientKey.equalsIgnoreCase(DEFAULT_SECRET_KEY)) {
                return true;
            }
        }

        // 4. Kiểm tra cấu hình động từ GitHub
        ensureSync();

        // Kiểm tra xem dòng client có đang ở chế độ Free hay không
        for (String freeLine : FREE_LINES) {
            if (clientToken.contains(freeLine)) {
                return true; // Dòng này miễn phí
            }
        }

        // 5. Kiểm tra Key có tồn tại và hợp lệ trong danh sách từ GitHub không
        if (clientKey == null || clientKey.trim().isEmpty()) {
            lastRejectNotice = getRejectNoticeForToken(clientToken);
            return false;
        }

        CachedKey ck;
        synchronized (VALID_KEYS) {
            ck = VALID_KEYS.get(clientKey.trim().toUpperCase());
        }

        if (ck == null) {
            lastRejectNotice = getRejectNoticeForToken(clientToken);
            return false;
        }

        if (!"ACTIVE".equalsIgnoreCase(ck.status)) {
            lastRejectNotice = getRejectNoticeForToken(clientToken);
            return false;
        }

        if (ck.expireAt > 0 && System.currentTimeMillis() > ck.expireAt) {
            lastRejectNotice = getRejectNoticeForToken(clientToken);
            return false;
        }

        // 5. Kiểm tra IP cho phép
        if (ck.allowedIps != null && !ck.allowedIps.isEmpty() && !ck.allowedIps.contains("*")) {
            if (clientIp != null && !ck.allowedIps.contains(clientIp.trim())) {
                lastRejectNotice = getRejectNoticeForToken(clientToken);
                return false;
            }
        }

        return true;
    }

    private static synchronized void ensureSync() {
        if (System.currentTimeMillis() - lastSyncTime < CACHE_EXPIRE_MS && !VALID_KEYS.isEmpty()) {
            return;
        }
        lastSyncTime = System.currentTimeMillis();

        new Thread(new Runnable() {
            public void run() {
                try {
                    syncFromGitHub();
                } catch (Throwable t) {
                    System.err.println("[KeyServerValidator] Lỗi đồng bộ key từ GitHub: " + t.getMessage());
                }
            }
        }).start();
    }

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
        byte[] rawBytes = baos.toByteArray();
        if (rawBytes.length == 0) return;

        String decryptedJson = decryptAes(rawBytes, AES_SECRET);
        JSONObject root = (JSONObject) JSONValue.parse(decryptedJson);
        if (root == null) return;

        if (root.containsKey("master_enabled")) {
            masterEnabled = Boolean.TRUE.equals(root.get("master_enabled"));
        }
        if (root.containsKey("server_check_enabled")) {
            serverCheckEnabled = Boolean.TRUE.equals(root.get("server_check_enabled"));
        }
        if (root.containsKey("global_notice")) {
            String gn = String.valueOf(root.get("global_notice")).trim();
            if (!gn.isEmpty()) {
                lastRejectNotice = gn;
            }
        }

        // Cập nhật các dòng Free và thông báo theo dòng
        FREE_LINES.clear();
        LINE_NOTICES.clear();
        if (root.containsKey("lines") && root.get("lines") instanceof JSONArray) {
            JSONArray lines = (JSONArray) root.get("lines");
            for (Object obj : lines) {
                if (obj instanceof JSONObject) {
                    JSONObject line = (JSONObject) obj;
                    String lId = String.valueOf(line.get("line_id"));
                    boolean lReqKey = Boolean.TRUE.equals(line.get("require_key"));
                    boolean lEnabled = Boolean.TRUE.equals(line.get("enabled"));
                    if (!lReqKey || !lEnabled) {
                        FREE_LINES.add(lId);
                    }
                    if (line.containsKey("notice")) {
                        String noticeStr = String.valueOf(line.get("notice")).trim();
                        if (!noticeStr.isEmpty()) {
                            LINE_NOTICES.put(lId, noticeStr);
                        }
                    }
                }
            }
        }

        // Cập nhật danh sách key
        synchronized (VALID_KEYS) {
            VALID_KEYS.clear();
            if (root.containsKey("keys") && root.get("keys") instanceof JSONArray) {
                JSONArray keys = (JSONArray) root.get("keys");
                for (Object obj : keys) {
                    if (obj instanceof JSONObject) {
                        JSONObject ko = (JSONObject) obj;
                        CachedKey ck = new CachedKey();
                        ck.key = String.valueOf(ko.get("key"));
                        ck.lineId = String.valueOf(ko.get("line_id"));
                        ck.status = String.valueOf(ko.get("status"));
                        if (ko.get("expire_at") instanceof Number) {
                            ck.expireAt = ((Number) ko.get("expire_at")).longValue();
                        }
                        if (ko.get("allowed_ips") instanceof JSONArray) {
                            JSONArray ips = (JSONArray) ko.get("allowed_ips");
                            for (Object ip : ips) {
                                if (ip != null) ck.allowedIps.add(ip.toString());
                            }
                        }
                        VALID_KEYS.put(ck.key.toUpperCase(), ck);
                    }
                }
            }
        }
        System.out.println("[KeyServerValidator] Đã đồng bộ " + VALID_KEYS.size() + " key từ GitHub (master_enabled=" + masterEnabled + ")");
    }

    private static String decryptAes(byte[] rawBytes, String passphrase) throws Exception {
        byte[] encryptedData;
        try {
            String base64Cipher = new String(rawBytes, StandardCharsets.UTF_8).trim();
            encryptedData = Base64.getDecoder().decode(base64Cipher.replaceAll("\\s+", ""));
        } catch (Exception e) {
            encryptedData = rawBytes;
        }

        if (encryptedData == null || encryptedData.length <= 16) {
            throw new Exception("Dữ liệu mã hóa không hợp lệ");
        }

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
}
