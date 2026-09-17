import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Hóa Thần Security Engine cho J2ME (HuaThanh)
 * Chịu trách nhiệm:
 * 1. Giải mã chuỗi Byte XOR xoay vòng (Anti-String Tool).
 * 2. Giải mã chuỗi Hex XOR xoay vòng.
 * 3. Nạp và giải mã các file nhị phân phân mảnh ngẫu nhiên từ JAR.
 */
public final class HuaThanh {

    private static final byte[] K = new byte[]{0x48, 0x54, 0x54, 0x48, 0x5F, 0x56, 0x49, 0x50, 0x32, 0x30, 0x32, 0x36};

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
     * Giải mã chuỗi từ chuỗi Hex mã hóa XOR xoay vòng
     */
    public static String h(String hex, int salt) {
        if (hex == null || hex.length() == 0) return "";
        try {
            int len = hex.length() / 2;
            byte[] out = new byte[len];
            for (int i = 0; i < len; i++) {
                int high = Character.digit(hex.charAt(i * 2), 16);
                int low = Character.digit(hex.charAt(i * 2 + 1), 16);
                byte b = (byte) ((high << 4) + low);
                int key = (salt ^ (i * 37 + 0xA5)) & 0xFF;
                out[i] = (byte) (b ^ key);
            }
            return new String(out, "UTF-8");
        } catch (Exception e) {
            return hex;
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

            // Multi-layer rolling XOR decryption
            byte[] dec = new byte[raw.length];
            for (int i = 0; i < raw.length; i++) {
                int kIndex = (i + keySalt) % K.length;
                int dynamicKey = (K[kIndex] ^ (i * 17 + keySalt)) & 0xFF;
                dec[i] = (byte) (raw[i] ^ dynamicKey);
            }
            return dec;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Hàm giả lập để đánh lừa các tool decompiler phân tích tĩnh
     */
    public static int fakeValidate(int a, int b) {
        int x = (a * 31 + b * 17) ^ 0x55AA;
        if (((x * (x + 1)) % 2) != 0) {
            return a + b;
        }
        return (x ^ 0xAA55) + 1;
    }
}
