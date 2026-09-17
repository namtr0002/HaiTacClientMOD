package com.deplor.haitactihontool.ultimate_security;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.zip.CRC32;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class KeyAuth {
   private static final String GITHUB_KEY_URL = "https://raw.githubusercontent.com/namtr0002/HTTH/refs/heads/main/KEY.txt";
   private static final byte[] SALT_PRIMARY_V5 = "HTTH_V5_QUANTUM_PRIME_2026_MASTER_SECRET_KEY!@#$".getBytes(StandardCharsets.UTF_8);
   private static final byte[] SALT_SECONDARY_V5;
   private static final byte[] SBOX_V5 = new byte[256];
   private static final byte[] SBOX_INV_V5 = new byte[256];
   private static final byte[] SALT_PRIMARY_V4 = "HTTH_V4_NEXUS_PRIME_KEY_2026!@#$".getBytes(StandardCharsets.UTF_8);
   private static final byte[] SALT_SECONDARY_V4;
   private static final byte[] SBOX_V4 = new byte[256];
   private static final byte[] SBOX_INV_V4 = new byte[256];
   private static String sessionInputKey;
   private static String sessionEncKey;
   private static boolean isAuthorized;

   public static String encrypt(String input) {
      return encryptV5(input);
   }

   public static String encryptV5(String input) {
      if (input != null && !input.trim().isEmpty()) {
         try {
            byte[] b = input.trim().getBytes(StandardCharsets.UTF_8);
            byte[] out = new byte[b.length];
            int sp = SALT_PRIMARY_V5.length;
            int ss = SALT_SECONDARY_V5.length;

            for (int i = 0; i < b.length; i++) {
               int val = b[i] & 255;
               val ^= SALT_PRIMARY_V5[i % sp] & 255;
               int shift = (i * 3 + 5) % 7 + 1;
               val = (val << shift | val >>> 8 - shift) & 0xFF;
               val = SBOX_V5[val] & 255;
               val ^= SALT_SECONDARY_V5[ss - 1 - i * 2 % ss] & 255;
               val = ~val & 0xFF;
               out[i] = (byte)val;
            }

            CRC32 crc = new CRC32();
            crc.update(out);
            long crcVal = crc.getValue();
            byte[] tag = new byte[]{(byte)(crcVal >> 24), (byte)(crcVal >> 16), (byte)(crcVal >> 8), (byte)crcVal};
            byte[] full = new byte[out.length + tag.length];
            System.arraycopy(out, 0, full, 0, out.length);
            System.arraycopy(tag, 0, full, out.length, tag.length);
            return "V5$" + Base64.getUrlEncoder().withoutPadding().encodeToString(full);
         } catch (Exception var10) {
            return input;
         }
      } else {
         return "";
      }
   }

   public static String decrypt(String token) {
      if (token != null && !token.trim().isEmpty()) {
         String t = token.trim();
         if (t.startsWith("V5$")) {
            return decryptV5(t);
         } else {
            String decV5 = decryptV5(t);
            return decV5 != null && !decV5.equals(t) ? decV5 : decryptV4(t);
         }
      } else {
         return "";
      }
   }

   public static String decryptV5(String token) {
      if (token != null && !token.trim().isEmpty()) {
         try {
            String rawToken = token.trim();
            if (rawToken.startsWith("V5$")) {
               rawToken = rawToken.substring(3);
            }

            int pad = (4 - rawToken.length() % 4) % 4;
            String padded = rawToken + "====".substring(0, pad);
            byte[] full = Base64.getUrlDecoder().decode(padded);
            if (full.length < 4) {
               return token;
            } else {
               int payloadLen = full.length - 4;
               byte[] raw = new byte[payloadLen];
               System.arraycopy(full, 0, raw, 0, payloadLen);
               CRC32 crc = new CRC32();
               crc.update(raw);
               long expectedCrc = crc.getValue();
               long actualCrc = (full[payloadLen] & 255L) << 24
                  | (full[payloadLen + 1] & 255L) << 16
                  | (full[payloadLen + 2] & 255L) << 8
                  | full[payloadLen + 3] & 255L;
               if (expectedCrc != actualCrc) {
               }

               byte[] out = new byte[raw.length];
               int sp = SALT_PRIMARY_V5.length;
               int ss = SALT_SECONDARY_V5.length;

               for (int i = 0; i < raw.length; i++) {
                  int val = raw[i] & 255;
                  val = ~val & 0xFF;
                  val ^= SALT_SECONDARY_V5[ss - 1 - i * 2 % ss] & 255;
                  val = SBOX_INV_V5[val] & 255;
                  int shift = (i * 3 + 5) % 7 + 1;
                  val = (val >>> shift | val << 8 - shift) & 0xFF;
                  val ^= SALT_PRIMARY_V5[i % sp] & 255;
                  out[i] = (byte)val;
               }

               return new String(out, StandardCharsets.UTF_8);
            }
         } catch (Exception var18) {
            return token;
         }
      } else {
         return "";
      }
   }

   public static String encryptV4(String input) {
      if (input != null && !input.isEmpty()) {
         try {
            byte[] b = input.getBytes(StandardCharsets.UTF_8);
            byte[] out = new byte[b.length];
            int sp = SALT_PRIMARY_V4.length;
            int ss = SALT_SECONDARY_V4.length;

            for (int i = 0; i < b.length; i++) {
               int val = b[i] & 255;
               val ^= SALT_PRIMARY_V4[i % sp] & 255;
               int shift = i % 7 + 1;
               val = (val << shift | val >>> 8 - shift) & 0xFF;
               val = SBOX_V4[val] & 255;
               val ^= SALT_SECONDARY_V4[ss - 1 - i % ss] & 255;
               val = ~val & 0xFF;
               out[i] = (byte)val;
            }

            return Base64.getUrlEncoder().withoutPadding().encodeToString(out);
         } catch (Exception var8) {
            return input;
         }
      } else {
         return "";
      }
   }

   public static String decryptV4(String token) {
      if (token != null && !token.isEmpty()) {
         try {
            int pad = (4 - token.length() % 4) % 4;
            String padded = token + "====".substring(0, pad);
            byte[] raw = Base64.getUrlDecoder().decode(padded);
            byte[] out = new byte[raw.length];
            int sp = SALT_PRIMARY_V4.length;
            int ss = SALT_SECONDARY_V4.length;

            for (int i = 0; i < raw.length; i++) {
               int val = raw[i] & 255;
               val = ~val & 0xFF;
               val ^= SALT_SECONDARY_V4[ss - 1 - i % ss] & 255;
               val = SBOX_INV_V4[val] & 255;
               int shift = i % 7 + 1;
               val = (val >>> shift | val << 8 - shift) & 0xFF;
               val ^= SALT_PRIMARY_V4[i % sp] & 255;
               out[i] = (byte)val;
            }

            return new String(out, StandardCharsets.UTF_8);
         } catch (Exception var10) {
            return token;
         }
      } else {
         return "";
      }
   }

   public static String getHWID() {
      try {
         String raw = System.getenv("COMPUTERNAME")
            + "|"
            + System.getenv("PROCESSOR_IDENTIFIER")
            + "|"
            + System.getenv("PROCESSOR_LEVEL")
            + "|"
            + System.getenv("NUMBER_OF_PROCESSORS");
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));
         StringBuilder sb = new StringBuilder();

         for (byte byt : hash) {
            sb.append(String.format("%02x", byt));
         }

         return sb.toString().substring(0, 12).toUpperCase();
      } catch (Exception var8) {
         return "UNKNOWN_HWID";
      }
   }

   public static KeyAuth.KeyEntry parseKeyLine(String line) {
      if (line != null && !line.trim().isEmpty() && !line.trim().startsWith("#")) {
         String[] parts = line.trim().split("\\|", -1);
         if (parts.length == 0) {
            return null;
         } else {
            KeyAuth.KeyEntry entry = new KeyAuth.KeyEntry();
            entry.storedKeyToken = parts[0].trim();
            if (parts.length > 1) {
               entry.storedTypeToken = parts[1].trim();
            }

            if (parts.length > 2) {
               entry.storedUidToken = parts[2].trim();
            }

            if (parts.length == 4) {
               String f3 = parts[3].trim();
               if (isLikelyDate(f3)) {
                  entry.endTime = f3;
               } else {
                  entry.note = f3;
               }
            } else if (parts.length == 5) {
               String f3 = parts[3].trim();
               String f4 = parts[4].trim();
               if (isLikelyDate(f3) && isLikelyDate(f4)) {
                  entry.startTime = f3;
                  entry.endTime = f4;
               } else if (isLikelyDate(f3)) {
                  entry.endTime = f3;
                  entry.note = f4;
               } else if (isLikelyDate(f4)) {
                  entry.startTime = f3;
                  entry.endTime = f4;
               } else {
                  entry.note = f3 + " " + f4;
               }
            } else if (parts.length >= 6) {
               entry.startTime = parts[3].trim();
               entry.endTime = parts[4].trim();
               StringBuilder sb = new StringBuilder();

               for (int i = 5; i < parts.length; i++) {
                  if (sb.length() > 0) {
                     sb.append("|");
                  }

                  sb.append(parts[i].trim());
               }

               entry.note = sb.toString();
            }

            return entry;
         }
      } else {
         return null;
      }
   }

   private static boolean isLikelyDate(String str) {
      if (str != null && !str.trim().isEmpty()) {
         String s = str.trim().toLowerCase();
         return !s.equals("unlimited") && !s.equals("forever") && !s.equals("never") && !s.equals("vinhvien") && !s.contains("vĩnh viễn")
            ? s.matches(".*\\d{2,4}[-\\/\\:\\s]\\d{1,2}.*") || s.matches("\\d{4}-\\d{2}-\\d{2}.*") || s.matches("\\d{1,2}/\\d{1,2}/\\d{4}.*")
            : true;
      } else {
         return false;
      }
   }

   public static String resolveType(String storedTypeToken) {
      if (storedTypeToken != null && !storedTypeToken.trim().isEmpty()) {
         String clean = storedTypeToken.trim();
         if (clean.startsWith("V5$")) {
            String dec = decryptV5(clean);
            if (isReadableType(dec)) {
               return dec.toLowerCase().trim();
            }
         }

         String dec = decrypt(clean);
         return isReadableType(dec) ? dec.toLowerCase().trim() : clean.toLowerCase().trim();
      } else {
         return "map";
      }
   }

   private static boolean isReadableType(String str) {
      if (str != null && !str.trim().isEmpty()) {
         String s = str.trim();

         for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < ' ' || c > '~') {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean isKeyMatched(String storedKeyToken, String cleanInputKey) {
      if (storedKeyToken != null && cleanInputKey != null) {
         String sKey = storedKeyToken.trim();
         String iKey = cleanInputKey.trim();
         if (sKey.equalsIgnoreCase(iKey)) {
            return true;
         } else {
            String encV5Key = encryptV5(iKey);
            String encV4Key = encryptV4(iKey);
            if (!sKey.equalsIgnoreCase(encV5Key) && !sKey.equalsIgnoreCase(encV4Key)) {
               String decV5 = decryptV5(sKey);
               String decV4 = decryptV4(sKey);
               return decV5.equalsIgnoreCase(iKey) || decV4.equalsIgnoreCase(iKey);
            } else {
               return true;
            }
         }
      } else {
         return false;
      }
   }

   public static boolean isHwidMatched(String storedUidToken) {
      if (storedUidToken != null && !storedUidToken.trim().isEmpty()) {
         String sUid = storedUidToken.trim();
         String currentHWID = getHWID();
         if (sUid.equalsIgnoreCase("ALL") || sUid.equalsIgnoreCase("GLOBAL") || sUid.equalsIgnoreCase("*")) {
            return true;
         } else if (sUid.equalsIgnoreCase(currentHWID)) {
            return true;
         } else {
            String encV5HWID = encryptV5(currentHWID);
            String encV4HWID = encryptV4(currentHWID);
            if (!sUid.equalsIgnoreCase(encV5HWID) && !sUid.equalsIgnoreCase(encV4HWID)) {
               String decUidV5 = decryptV5(sUid);
               String decUidV4 = decryptV4(sUid);
               return decUidV5.equalsIgnoreCase("ALL")
                     || decUidV4.equalsIgnoreCase("ALL")
                     || decUidV5.equalsIgnoreCase("GLOBAL")
                     || decUidV4.equalsIgnoreCase("GLOBAL")
                     || decUidV5.equalsIgnoreCase("*")
                     || decUidV4.equalsIgnoreCase("*")
                  ? true
                  : decUidV5.equalsIgnoreCase(currentHWID) || decUidV4.equalsIgnoreCase(currentHWID);
            } else {
               return true;
            }
         }
      } else {
         return true;
      }
   }

   public static Date parseToDate(String dateStr) {
      if (dateStr != null && !dateStr.trim().isEmpty()) {
         String s = dateStr.trim();
         if (!s.equalsIgnoreCase("unlimited")
            && !s.equalsIgnoreCase("forever")
            && !s.equalsIgnoreCase("never")
            && !s.equalsIgnoreCase("vinhvien")
            && !s.contains("vĩnh viễn")) {
            String[] patterns = new String[]{
               "yyyy-MM-dd HH:mm:ss",
               "yyyy-MM-dd HH:mm",
               "yyyy-MM-dd",
               "dd/MM/yyyy HH:mm:ss",
               "dd/MM/yyyy HH:mm",
               "dd/MM/yyyy",
               "HH:mm dd/MM/yyyy",
               "HH:mm:ss dd/MM/yyyy"
            };

            for (String pattern : patterns) {
               try {
                  SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                  sdf.setLenient(false);
                  Date date = sdf.parse(s);
                  if (!pattern.equals("yyyy-MM-dd") && !pattern.equals("dd/MM/yyyy")) {
                     return date;
                  }

                  return new Date(date.getTime() + 86400000L - 1000L);
               } catch (Exception var9) {
               }
            }

            return null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static boolean isDateExpired(String dateStr) {
      Date expDate = parseToDate(dateStr);
      return expDate == null ? false : new Date().after(expDate);
   }

   public static boolean checkKey(String inputKey) {
      if (inputKey != null && !inputKey.trim().isEmpty()) {
         String cleanInputKey = inputKey.trim();
         sessionInputKey = cleanInputKey;

         try {
            String fetchUrl = "https://raw.githubusercontent.com/namtr0002/HTTH/refs/heads/main/KEY.txt?t=" + System.currentTimeMillis();
            URL url = new URL(fetchUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Expires", "0");
            if (conn.getResponseCode() != 200) {
               System.err.println("[KeyAuth] Connection error HTTP " + conn.getResponseCode());
               return false;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = in.readLine()) != null) {
               KeyAuth.KeyEntry entry = parseKeyLine(line);
               if (entry != null && isKeyMatched(entry.storedKeyToken, cleanInputKey)) {
                  if (isHwidMatched(entry.storedUidToken)) {
                     if (!entry.endTime.isEmpty() && isDateExpired(entry.endTime)) {
                        System.err.println("[KeyAuth] Key " + cleanInputKey + " is EXPIRED on " + entry.endTime);
                        SwingUtilities.invokeLater(
                           () -> JOptionPane.showMessageDialog(
                              null, "Mã Key của bạn đã hết hạn sử dụng ngày " + entry.endTime + "!\nVui lòng liên hệ Admin để gia hạn.", "Mã Key Hết Hạn", 2
                           )
                        );
                        in.close();
                        return false;
                     }

                     sessionEncKey = entry.storedKeyToken;
                     isAuthorized = true;
                     String resolvedType = resolveType(entry.storedTypeToken);
                     SessionInfo.activate(resolvedType, entry.startTime, entry.endTime, entry.note);
                     startHeartbeat();
                     in.close();
                     System.out
                        .println("[KeyAuth] Key successfully authorized for " + cleanInputKey + " [Type=" + resolvedType + ", Expiry=" + entry.endTime + "]");
                     return true;
                  }

                  System.err.println("[KeyAuth] Key matched but HWID mismatch! Machine=" + getHWID() + " vs Stored=" + entry.storedUidToken);
               }
            }

            in.close();
         } catch (Exception var9) {
            System.err.println("[KeyAuth] Auth Exception: " + var9.getMessage());
            var9.printStackTrace();
         }

         return false;
      } else {
         return false;
      }
   }

   private static void startHeartbeat() {
      Thread t = new Thread(() -> {
         while (isAuthorized) {
            try {
               Thread.sleep(60000L);
               validateSession();
            } catch (InterruptedException var1) {
               break;
            }
         }
      });
      t.setName("HTTH-V5-Heartbeat");
      t.setDaemon(true);
      t.start();
   }

   public static boolean validateSession() {
      if (isAuthorized && sessionInputKey != null && !sessionInputKey.isEmpty()) {
         try {
            String fetchUrl = "https://raw.githubusercontent.com/namtr0002/HTTH/refs/heads/main/KEY.txt?t=" + System.currentTimeMillis();
            URL url = new URL(fetchUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Expires", "0");
            if (conn.getResponseCode() != 200) {
               return true;
            } else {
               BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
               KeyAuth.KeyEntry matchedEntry = null;

               String line;
               while ((line = in.readLine()) != null) {
                  KeyAuth.KeyEntry entry = parseKeyLine(line);
                  if (entry != null && isKeyMatched(entry.storedKeyToken, sessionInputKey)) {
                     matchedEntry = entry;
                     break;
                  }
               }

               in.close();
               if (matchedEntry == null) {
                  isAuthorized = false;
                  SwingUtilities.invokeLater(
                     () -> {
                        JOptionPane.showMessageDialog(
                           null, "Mã Key của bạn đã bị hủy hoặc xóa khỏi hệ thống từ xa.\nHệ thống tự động thoát...", "Phiên Đăng Nhập Đã Hủy", 0
                        );
                        System.exit(0);
                     }
                  );
                  return false;
               } else if (!matchedEntry.endTime.isEmpty() && isDateExpired(matchedEntry.endTime)) {
                  isAuthorized = false;
                  String expTime = matchedEntry.endTime;
                  SwingUtilities.invokeLater(
                     () -> {
                        JOptionPane.showMessageDialog(
                           null, "Mã Key của bạn đã hết hạn sử dụng (" + expTime + ").\nHệ thống tự động thoát...", "Mã Key Hết Hạn", 2
                        );
                        System.exit(0);
                     }
                  );
                  return false;
               } else {
                  String resolvedType = resolveType(matchedEntry.storedTypeToken);
                  SessionInfo.activate(resolvedType, matchedEntry.startTime, matchedEntry.endTime, matchedEntry.note);
                  return true;
               }
            }
         } catch (Exception var7) {
            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean isAuthorized() {
      return isAuthorized;
   }

   static {
      try {
         MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
         SALT_SECONDARY_V5 = sha512.digest("HTTH_NEXUS_ULTIMATE_SBOX_SEED_V5".getBytes(StandardCharsets.UTF_8));

         for (int i = 0; i < 256; i++) {
            SBOX_V5[i] = (byte)i;
         }

         int j = 0;

         for (int i = 0; i < 256; i++) {
            j = j + (SBOX_V5[i] & 255) + (SALT_SECONDARY_V5[i % SALT_SECONDARY_V5.length] & 255) & 0xFF;
            byte tmp = SBOX_V5[i];
            SBOX_V5[i] = SBOX_V5[j];
            SBOX_V5[j] = tmp;
         }

         for (int i = 0; i < 256; i++) {
            SBOX_INV_V5[SBOX_V5[i] & 255] = (byte)i;
         }

         MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
         SALT_SECONDARY_V4 = sha256.digest("HTTH_NEXUS_2026_SBOX_SEED".getBytes(StandardCharsets.UTF_8));

         for (int i = 0; i < 256; i++) {
            SBOX_V4[i] = (byte)i;
         }

         j = 0;

         for (int i = 0; i < 256; i++) {
            j = j + (SBOX_V4[i] & 255) + (SALT_SECONDARY_V4[i % SALT_SECONDARY_V4.length] & 255) & 0xFF;
            byte tmp = SBOX_V4[i];
            SBOX_V4[i] = SBOX_V4[j];
            SBOX_V4[j] = tmp;
         }

         for (int i = 0; i < 256; i++) {
            SBOX_INV_V4[SBOX_V4[i] & 255] = (byte)i;
         }
      } catch (Exception var5) {
         throw new RuntimeException("HTTH Security Initialization Failed", var5);
      }

      sessionInputKey = "";
      sessionEncKey = "";
      isAuthorized = false;
   }

   public static class KeyEntry {
      public String storedKeyToken = "";
      public String storedTypeToken = "map";
      public String storedUidToken = "ALL";
      public String startTime = "";
      public String endTime = "";
      public String note = "";
   }
}
