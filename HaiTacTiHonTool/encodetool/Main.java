package encodetool;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class Main {
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final byte[] KEY = sha256("HTTH-ULTIMATE-KEY-v1");

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java encodetool.Main <input_file> <output_file>");
            System.exit(1);
        }

        Path input = Paths.get(args[0]);
        Path output = Paths.get(args[1]);

        try {
            if (!Files.exists(input)) {
                System.out.println("Input file not found: " + input);
                System.exit(1);
            }

            byte[] plain = Files.readAllBytes(input);

            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, "AES"), new IvParameterSpec(iv));
            byte[] enc = cipher.doFinal(plain);

            byte[] packed = new byte[iv.length + enc.length];
            System.arraycopy(iv, 0, packed, 0, iv.length);
            System.arraycopy(enc, 0, packed, iv.length, enc.length);

            if (output.getParent() != null) {
                Files.createDirectories(output.getParent());
            }
            Files.write(output, packed);

            System.out.println("Packed " + plain.length + " bytes -> " + output);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static byte[] sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(s.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}