import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Hóa Thần Security Core Engine cho J2ME (HuaThanh)
 * - Tối ưu hóa 100% cho CLDC 1.1 / MIDP 2.0 KVM.
 * - Giải mã chuỗi Hex / Byte XOR xoay vòng đa tầng siêu tốc.
 * - Khóa nội tại được xáo trộn động, chống phân tích tĩnh và hex-dump.
 * - Sẵn sàng để ProGuard rút gọn và rối tên toàn diện cùng toàn bộ dự án.
 */
public final class HuaThanh {

    // Khóa cơ sở được che giấu qua toán tử XOR tĩnh
    private static final int MASK = 0xA55A;
    private static final byte[] K = new byte[]{
        (byte)(0x48 ^ 0x11), (byte)(0x54 ^ 0x22), (byte)(0x54 ^ 0x33), (byte)(0x48 ^ 0x44),
        (byte)(0x5F ^ 0x55), (byte)(0x56 ^ 0x66), (byte)(0x49 ^ 0x77), (byte)(0x50 ^ 0x88),
        (byte)(0x32 ^ 0x99), (byte)(0x30 ^ 0xAA), (byte)(0x32 ^ 0xBB), (byte)(0x36 ^ 0xCC)
    };

    /**
     * Giải mã chuỗi từ chuỗi Hex mã hóa XOR xoay vòng.
     * Sử dụng giải thuật phân tích ký tự trực tiếp siêu nhẹ, không tạo object thừa, tương thích mọi KVM.
     */
    public static String h(String hex, int salt) {
        if (hex == null || hex.length() == 0) return "";
        try {
            int len = hex.length() / 2;
            byte[] out = new byte[len];
            for (int i = 0; i < len; i++) {
                char c1 = hex.charAt(i * 2);
                char c2 = hex.charAt(i * 2 + 1);
                int h1 = (c1 >= '0' && c1 <= '9') ? (c1 - '0') : ((c1 >= 'a' && c1 <= 'f') ? (c1 - 'a' + 10) : ((c1 >= 'A' && c1 <= 'F') ? (c1 - 'A' + 10) : 0));
                int h2 = (c2 >= '0' && c2 <= '9') ? (c2 - '0') : ((c2 >= 'a' && c2 <= 'f') ? (c2 - 'a' + 10) : ((c2 >= 'A' && c2 <= 'F') ? (c2 - 'A' + 10) : 0));
                byte b = (byte) ((h1 << 4) | h2);
                int key = (salt ^ (i * 37 + 0xA5)) & 0xFF;
                out[i] = (byte) (b ^ key);
            }
            return new String(out, "UTF-8");
        } catch (Exception e) {
            return hex;
        }
    }

    /**
     * Giải mã chuỗi từ mảng byte mã hóa XOR xoay vòng
     */
    public static String d(byte[] b, int salt) {
        if (b == null) return "";
        try {
            byte[] out = new byte[b.length];
            for (int i = 0; i < b.length; i++) {
                int key = (salt ^ (i * 31 + 0x5A)) & 0xFF;
                out[i] = (byte) (b[i] ^ key);
            }
            return new String(out, "UTF-8");
        } catch (Exception e) {
            return new String(b);
        }
    }

    /**
     * Nạp và giải mã tài nguyên nhị phân ngẫu nhiên từ file nhị phân trong JAR
     */
    public static byte[] loadEncryptedBinary(String resPath, int keySalt) {
        try {
            InputStream is = GameMidlet.getResourceAsStream(resPath);
            if (is == null) return null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int r;
            while ((r = is.read(buf)) != -1) {
                baos.write(buf, 0, r);
            }
            is.close();
            byte[] raw = baos.toByteArray();

            byte[] dec = new byte[raw.length];
            for (int i = 0; i < raw.length; i++) {
                int kIndex = (i + keySalt) % K.length;
                int baseKey = (K[kIndex] ^ ((kIndex + 1) * 0x11)) & 0xFF;
                int dynamicKey = (baseKey ^ (i * 17 + keySalt)) & 0xFF;
                dec[i] = (byte) (raw[i] ^ dynamicKey);
            }
            return dec;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Vị từ mờ đục bảo vệ tính toàn vẹn (Opaque Integrity Guard)
     */
    public static boolean checkIntegrity(int token) {
        int x = (token * 31) ^ MASK;
        return ((x * (x + 1)) & 1) == 0;
    }
}
