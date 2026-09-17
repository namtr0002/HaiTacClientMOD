package encodetool;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Stream;

public final class J2meClassPacker {

    private static final SecureRandom RAND = new SecureRandom();
    private static final byte[] MASTER_KEY = new byte[]{0x48, 0x54, 0x54, 0x48, 0x5F, 0x56, 0x49, 0x50, 0x32, 0x30, 0x32, 0x36};
    private static final String[] RANDOM_EXTS = {".dat", ".bin", ".raw", ".pak", ".sys", ".tmp", ".db", ".sec", ".kvd", ".blob"};

    public static void main(String[] args) throws Exception {
        String inputDir = args.length > 0 ? args[0] : "build/classes_obf_unpacked";
        String outJar = args.length > 1 ? args[1] : "dist/HaiTacTiHon129.jar";

        System.out.println("[*] [HTTH Class Packer] Packaging J2ME Ultimate Protected Archive...");
        Path rootPath = Paths.get(inputDir);
        if (!Files.exists(rootPath)) {
            System.err.println("Input directory not found: " + inputDir);
            return;
        }

        List<Path> classFiles = new ArrayList<>();
        List<Path> resourceFiles = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(rootPath)) {
            for (Path p : (Iterable<Path>) paths::iterator) {
                if (Files.isRegularFile(p)) {
                    String name = p.getFileName().toString();
                    if (name.endsWith(".class")) {
                        classFiles.add(p);
                    } else if (!name.equalsIgnoreCase("MANIFEST.MF")) {
                        resourceFiles.add(p);
                    }
                }
            }
        }

        // Tạo file Manifest cho J2ME MIDlet chuẩn
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        manifest.getMainAttributes().put(new Attributes.Name("MIDlet-1"), "Hai Tac Ti Hon 129, /icon.png, GameMidlet");
        manifest.getMainAttributes().put(new Attributes.Name("MIDlet-Vendor"), "Teamobi");
        manifest.getMainAttributes().put(new Attributes.Name("MIDlet-Version"), "1.2.9");
        manifest.getMainAttributes().put(new Attributes.Name("MIDlet-Name"), "Hai Tac Ti Hon 129");
        manifest.getMainAttributes().put(new Attributes.Name("MicroEdition-Configuration"), "CLDC-1.1");
        manifest.getMainAttributes().put(new Attributes.Name("MicroEdition-Profile"), "MIDP-2.0");

        File outFile = new File(outJar);
        if (outFile.getParentFile() != null) {
            outFile.getParentFile().mkdirs();
        }

        int classCount = 0;
        int decoyCount = 0;

        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(outFile), manifest)) {
            // 1. Ghi toàn bộ obfuscated .class files vào JAR để JVM ClassLoader nạp chuẩn 100% không lỗi NoClassDefFoundError
            for (Path cp : classFiles) {
                String relPath = rootPath.relativize(cp).toString().replace(File.separatorChar, '/');
                JarEntry entry = new JarEntry(relPath);
                jos.putNextEntry(entry);
                Files.copy(cp, jos);
                jos.closeEntry();
                classCount++;
            }

            // 2. Tạo thêm các binary decoy chunk ngẫu nhiên được mã hóa đa tầng để gây nhiễu cho decompiler/resource extractor
            for (int i = 0; i < 20; i++) {
                String randName = generateRandomBinaryName();
                byte[] junk = new byte[64 + RAND.nextInt(256)];
                RAND.nextBytes(junk);
                int salt = RAND.nextInt(90000) + 1000;
                byte[] encJunk = encryptBytes(junk, salt);

                JarEntry entry = new JarEntry(randName);
                jos.putNextEntry(entry);
                jos.write(encJunk);
                jos.closeEntry();
                decoyCount++;
            }

            // 3. Ghi tài nguyên client (icon.png, data, resources)
            for (Path rp : resourceFiles) {
                String relPath = rootPath.relativize(rp).toString().replace(File.separatorChar, '/');
                JarEntry entry = new JarEntry(relPath);
                jos.putNextEntry(entry);
                Files.copy(rp, jos);
                jos.closeEntry();
            }
        }

        System.out.println("[+] [HTTH Class Packer] Successfully created Protected JAR: " + outJar);
        System.out.println("    - Obfuscated Classes: " + classCount);
        System.out.println("    - Encrypted Decoy Binary Chunks: " + decoyCount + " files");
        System.out.println("    - Client Resources preserved.");
    }

    private static byte[] encryptBytes(byte[] raw, int salt) {
        byte[] out = new byte[raw.length];
        for (int i = 0; i < raw.length; i++) {
            int kIndex = (i + salt) % MASTER_KEY.length;
            int dynamicKey = (MASTER_KEY[kIndex] ^ (i * 17 + salt)) & 0xFF;
            out[i] = (byte) (raw[i] ^ dynamicKey);
        }
        return out;
    }

    private static String generateRandomBinaryName() {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        int len = 4 + RAND.nextInt(6);
        StringBuilder sb = new StringBuilder();
        String[] prefixes = {"sys_", "k_", "dat_", "sec_", "bin_", "m_", "q_"};
        sb.append(prefixes[RAND.nextInt(prefixes.length)]);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(RAND.nextInt(chars.length())));
        }
        sb.append(RANDOM_EXTS[RAND.nextInt(RANDOM_EXTS.length)]);
        return sb.toString();
    }
}
