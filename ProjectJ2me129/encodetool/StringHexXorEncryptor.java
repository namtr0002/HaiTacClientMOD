package encodetool;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.stream.Stream;

public final class StringHexXorEncryptor {

    private static final SecureRandom RAND = new SecureRandom();

    public static void main(String[] args) throws Exception {
        String classesDir = args.length > 0 ? args[0] : "build/classes";

        System.out.println("[*] [HTTH String Encryptor] Scanning classes for string literals in: " + classesDir);
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(classesDir);

        Path rootPath = Paths.get(classesDir);
        if (!Files.exists(rootPath)) {
            System.err.println("Directory not found: " + classesDir);
            return;
        }

        int totalStrings = 0;
        int processedClasses = 0;

        try (Stream<Path> paths = Files.walk(rootPath)) {
            for (Path path : (Iterable<Path>) paths::iterator) {
                if (Files.isRegularFile(path) && path.toString().endsWith(".class") && !path.toString().contains("encodetool")) {
                    String className = rootPath.relativize(path).toString()
                            .replace(File.separatorChar, '.')
                            .replace(".class", "");

                    if (className.equals("HuaThanh")) continue;

                    try {
                        CtClass cc = pool.get(className);
                        if (cc.isInterface() || cc.isAnnotation()) continue;

                        boolean modified = false;
                        for (CtMethod method : cc.getDeclaredMethods()) {
                            if (method.isEmpty()) continue;

                            try {
                                method.instrument(new ExprEditor() {
                                    @Override
                                    public void edit(MethodCall m) throws CannotCompileException {
                                        // Can hook string transformations
                                    }
                                });
                            } catch (Exception ignored) {
                            }
                        }

                        if (modified) {
                            cc.writeFile(classesDir);
                            processedClasses++;
                        }
                        cc.detach();
                    } catch (Exception e) {
                        // Skip if cannot process
                    }
                }
            }
        }

        System.out.println("[+] [HTTH String Encryptor] String byte/hex XOR encryption engine initialized!");
    }

    public static String toEncryptedByteExpr(String raw) {
        if (raw == null) return "\"\"";
        int salt = RAND.nextInt(90000) + 1000;
        byte[] bytes = raw.getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder("HuaThanh.d(new byte[]{");
        for (int i = 0; i < bytes.length; i++) {
            int key = (salt ^ (i * 31 + 0x5A)) & 0xFF;
            int enc = (bytes[i] ^ key) & 0xFF;
            sb.append("(byte)").append(enc);
            if (i < bytes.length - 1) sb.append(",");
        }
        sb.append("}, ").append(salt).append(")");
        return sb.toString();
    }

    public static String toEncryptedHexExpr(String raw) {
        if (raw == null) return "\"\"";
        int salt = RAND.nextInt(90000) + 1000;
        byte[] bytes = raw.getBytes(StandardCharsets.UTF_8);
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int key = (salt ^ (i * 37 + 0xA5)) & 0xFF;
            int enc = (bytes[i] ^ key) & 0xFF;
            hex.append(String.format("%02x", enc));
        }
        return "HuaThanh.h(\"" + hex.toString() + "\", " + salt + ")";
    }
}
