package com.haitac.manager.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Dịch vụ mã hóa & giải mã AES-128 CBC an toàn
 * Tự động tạo IV ngẫu nhiên 16 bytes, gắn kèm vào đầu dữ liệu mã hóa.
 */
public class CryptoService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static SecretKeySpec deriveKey(String passphrase) throws Exception {
        if (passphrase == null || passphrase.trim().isEmpty()) {
            passphrase = "HTTH_CLIENT_KEY_DEFAULT_SECRET_2026";
        }
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha.digest(passphrase.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Arrays.copyOf(hash, 16);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static String encrypt(String plainText, String passphrase) throws Exception {
        if (plainText == null) return null;
        byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = encryptBytes(plainBytes, passphrase);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static byte[] encryptBytes(byte[] plainBytes, String passphrase) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        RANDOM.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        SecretKeySpec keySpec = deriveKey(passphrase);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] cipherBytes = cipher.doFinal(plainBytes);

        byte[] combined = new byte[iv.length + cipherBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(cipherBytes, 0, combined, iv.length, cipherBytes.length);

        return combined;
    }

    public static String decrypt(String base64Encrypted, String passphrase) throws Exception {
        if (base64Encrypted == null || base64Encrypted.trim().isEmpty()) return null;
        byte[] combined = Base64.getDecoder().decode(base64Encrypted.replaceAll("\\s+", ""));
        byte[] decryptedBytes = decryptBytes(combined, passphrase);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static byte[] decryptBytes(byte[] combined, String passphrase) throws Exception {
        if (combined == null || combined.length <= IV_SIZE) {
            throw new Exception("Dữ liệu mã hóa không hợp lệ hoặc quá ngắn (thiếu IV)");
        }

        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(combined, 0, iv, 0, IV_SIZE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] cipherBytes = new byte[combined.length - IV_SIZE];
        System.arraycopy(combined, IV_SIZE, cipherBytes, 0, cipherBytes.length);

        SecretKeySpec keySpec = deriveKey(passphrase);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        return cipher.doFinal(cipherBytes);
    }
}
