package network;

public class KeySecurityTest {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  VERIFYING CLIENT-SERVER SECURITY & KEY SYSTEM  ");
        System.out.println("=================================================");

        boolean allPassed = true;

        String defaultSecret = "HTTH_CLIENT_NAMTR0002_SECURE_KEY_V4_2026";
        String clientToken = "SIG_MOD_LIBGDX_" + System.currentTimeMillis();
        String clientKeyPrefix = "HTTH_KEY:" + defaultSecret;

        // 1. Kiểm tra Server KeyServerValidator với check_client_key = true
        System.out.println("\n[TEST 1] Testing Server KeyServerValidator with Key Check ENABLED...");
        KeyServerValidator.setServerCheckEnabled(true);
        KeyServerValidator.setLocalSecretKey(defaultSecret);

        // Kịch bản A: Phiên bản HaiTacZ gốc (không gửi key, không gửi token)
        boolean haitacZAllowed = KeyServerValidator.validateClient("127.0.0.1", "", "");
        if (haitacZAllowed) {
            System.err.println("FAIL: HaiTacZ gốc không được phép đăng nhập nhưng lại được cho qua!");
            allPassed = false;
        } else {
            System.out.println("PASS: HaiTacZ gốc bị chặn thành công!");
            System.out.println("      Thông báo trả về: \"" + KeyServerValidator.getLastRejectNotice() + "\"");
        }

        // Kịch bản B: Mod ngoài / Mod lậu (gửi token giả mạo hoặc key sai)
        boolean fakeModAllowed = KeyServerValidator.validateClient("127.0.0.1", "HTTH_KEY:FAKE_KEY_XYZ", "SIG_FAKE_MOD");
        if (fakeModAllowed) {
            System.err.println("FAIL: Mod ngoài key sai nhưng lại được cho qua!");
            allPassed = false;
        } else {
            System.out.println("PASS: Mod ngoài / key sai bị chặn thành công!");
        }

        // Kịch bản C: Client MOD chính thức (gửi token SIG_ và secret key V4)
        boolean officialModAllowed = KeyServerValidator.validateClient("127.0.0.1", clientKeyPrefix, clientToken);
        if (!officialModAllowed) {
            System.err.println("FAIL: Client MOD chính thức bị từ chối: " + KeyServerValidator.getLastRejectNotice());
            allPassed = false;
        } else {
            System.out.println("PASS: Client MOD chính thức kết nối thành công và được xác thực tự động với Key V4!");
        }

        // Kịch bản D: Client Unity MOD V4 mới (gửi token SIG_MOD_UNITY và key V4)
        String unityToken = "SIG_MOD_UNITY_" + System.currentTimeMillis();
        String unityKey = "HTTH_KEY:" + defaultSecret;
        boolean unityModAllowed = KeyServerValidator.validateClient("127.0.0.1", unityKey, unityToken);
        if (!unityModAllowed) {
            System.err.println("FAIL: Client Unity MOD V4 bị từ chối: " + KeyServerValidator.getLastRejectNotice());
            allPassed = false;
        } else {
            System.out.println("PASS: Client Unity MOD V4 kết nối thành công với Key V4 chính thức!");
        }

        // Kịch bản E: Client J2ME MOD V4 mới (gửi token SIG_MOD_J2ME và key V4)
        String j2meToken = "SIG_MOD_J2ME_" + System.currentTimeMillis();
        String j2meKey = "HTTH_KEY:" + defaultSecret;
        boolean j2meModAllowed = KeyServerValidator.validateClient("127.0.0.1", j2meKey, j2meToken);
        if (!j2meModAllowed) {
            System.err.println("FAIL: Client J2ME MOD V4 bị từ chối: " + KeyServerValidator.getLastRejectNotice());
            allPassed = false;
        } else {
            System.out.println("PASS: Client J2ME MOD V4 kết nối thành công với Key V4 chính thức!");
        }

        // Kịch bản F: Bản CŨ dùng key V1 (HTTH_CLIENT_NAMTR0002_SECURE_KEY_2026) -> PHẢI BỊ CHẶN!
        String oldKeyV1 = "HTTH_KEY:HTTH_CLIENT_NAMTR0002_SECURE_KEY_2026";
        boolean oldClientV1Allowed = KeyServerValidator.validateClient("127.0.0.1", oldKeyV1, unityToken);
        if (oldClientV1Allowed) {
            System.err.println("FAIL: Bản cũ V1 dùng key cũ nhưng vẫn vào được server!");
            allPassed = false;
        } else {
            System.out.println("PASS: Bản cũ V1 (dùng key cũ) đã bị máy chủ chặn thành công!");
        }

        // Kịch bản G: Bản CŨ dùng key V2 (HTTH_CLIENT_NAMTR0002_SECURE_KEY_V2_2026) -> PHẢI BỊ CHẶN!
        String oldKeyV2 = "HTTH_KEY:HTTH_CLIENT_NAMTR0002_SECURE_KEY_V2_2026";
        boolean oldClientV2Allowed = KeyServerValidator.validateClient("127.0.0.1", oldKeyV2, unityToken);
        if (oldClientV2Allowed) {
            System.err.println("FAIL: Bản cũ V2 dùng key V2 nhưng vẫn vào được server!");
            allPassed = false;
        } else {
            System.out.println("PASS: Bản cũ V2 (dùng key V2) đã bị máy chủ chặn thành công!");
        }

        // Kịch bản H: Bản CŨ dùng key V3 (HTTH_CLIENT_NAMTR0002_SECURE_KEY_V3_2026) -> PHẢI BỊ CHẶN!
        String oldKeyV3 = "HTTH_KEY:HTTH_CLIENT_NAMTR0002_SECURE_KEY_V3_2026";
        boolean oldClientV3Allowed = KeyServerValidator.validateClient("127.0.0.1", oldKeyV3, unityToken);
        if (oldClientV3Allowed) {
            System.err.println("FAIL: Bản cũ V3 dùng key V3 chưa cập nhật nhưng vẫn vào được server!");
            allPassed = false;
        } else {
            System.out.println("PASS: Bản cũ V3 (chưa cập nhật) đã bị máy chủ chặn thành công!");
            System.out.println("      Thông báo gửi tới bản cũ: \"" + KeyServerValidator.getLastRejectNotice() + "\"");
        }

        // 2. Kiểm tra khi TẮT kiểm tra Key trên Server (check_client_key = false)
        System.out.println("\n[TEST 2] Testing Server with Key Check DISABLED (check_client_key=false)...");
        KeyServerValidator.setServerCheckEnabled(false);
        boolean anyClientAllowed = KeyServerValidator.validateClient("127.0.0.1", "", "");
        if (!anyClientAllowed) {
            System.err.println("FAIL: Khi tắt check key, HaiTacZ vẫn bị chặn!");
            allPassed = false;
        } else {
            System.out.println("PASS: Khi tắt check key, tất cả client (kể cả HaiTacZ) đều kết nối được bình thường!");
        }

        System.out.println("\n=================================================");
        if (allPassed) {
            System.out.println("  >>> TAT CA CAC BAI KIEM THU DEU DAT CHUAN! <<<  ");
        } else {
            System.out.println("  >>> CO BAI KIEM THU THAT BAI! <<<  ");
        }
        System.out.println("=================================================");
        if (!allPassed) {
            System.exit(1);
        }
    }
}
