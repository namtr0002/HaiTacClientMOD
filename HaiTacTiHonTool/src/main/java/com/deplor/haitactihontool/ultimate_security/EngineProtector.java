package com.deplor.haitactihontool.ultimate_security;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EngineProtector extends ClassLoader {
   private static final String ALGO = "AES";
   private static final byte[] KEY_VAL = "1a2b3c4d5e6f7g8h".getBytes();
   private final Map<String, byte[]> classBuffer = new HashMap<>();

   public static void launch(String mainClass, String[] args) throws Exception {
      EngineProtector loader = new EngineProtector();
      Thread.currentThread().setContextClassLoader(loader);
      Class<?> clazz = loader.loadClass(mainClass);
      clazz.getMethod("main", String[].class).invoke(null, args);
   }

   public EngineProtector() throws Exception {
      super(EngineProtector.class.getClassLoader());
      this.loadCore();
   }

   private void loadCore() throws Exception {
      InputStream is1 = this.getClass().getResourceAsStream("/sys.dat");
      InputStream is2 = this.getClass().getResourceAsStream("/core.bin");
      InputStream is3 = this.getClass().getResourceAsStream("/cache.tmp");
      if (is1 != null && is2 != null && is3 != null) {
         byte[] p1 = is1.readAllBytes();
         byte[] p2 = is2.readAllBytes();
         byte[] p3 = is3.readAllBytes();
         byte[] encrypted = new byte[p1.length + p2.length + p3.length];
         System.arraycopy(p1, 0, encrypted, 0, p1.length);
         System.arraycopy(p2, 0, encrypted, p1.length, p2.length);
         System.arraycopy(p3, 0, encrypted, p1.length + p2.length, p3.length);
         if (encrypted.length < 100) {
            throw new Exception("Tampering detected: Payload corrupted.");
         } else {
            byte[] decrypted = this.decrypt(encrypted);

            ZipEntry entry;
            try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(decrypted))) {
               for (; (entry = zis.getNextEntry()) != null; zis.closeEntry()) {
                  if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                     String className = entry.getName().replace(".class", "").replace("/", ".");
                     byte[] buffer = zis.readAllBytes();
                     this.classBuffer.put(className, buffer);
                  }
               }
            }
         }
      } else {
         throw new Exception("Engine payload fragments not found!");
      }
   }

   private byte[] decrypt(byte[] data) throws Exception {
      Key key = new SecretKeySpec(KEY_VAL, "AES");
      Cipher c = Cipher.getInstance("AES");
      c.init(2, key);
      return c.doFinal(data);
   }

   @Override
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      byte[] data = this.classBuffer.get(name);
      return data != null ? this.defineClass(name, data, 0, data.length) : super.findClass(name);
   }
}
