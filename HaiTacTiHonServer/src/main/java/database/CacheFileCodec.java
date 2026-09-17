package database;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * CacheFileCodec - Consolidates and optimizes file-based cache operations,
 * including GZIP/Deflate compression and XOR-based encryption.
 */
public class CacheFileCodec {

    public enum Compression {
        GZIP,
        DEFLATE,
        NONE
    }

    /**
     * Compresses, encrypts (XOR), and saves raw bytes to a file.
     * Uses BufferedOutputStream to maximize writing performance.
     */
    public static void save(File file, byte[] rawBytes, byte xorKey, Compression compression) throws IOException {
        byte[] processedBytes;
        if (compression == Compression.GZIP) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(rawBytes.length / 2);
            try (GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
                gzos.write(rawBytes);
            }
            processedBytes = baos.toByteArray();
        } else if (compression == Compression.DEFLATE) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(rawBytes.length / 2);
            try (DeflaterOutputStream dos = new DeflaterOutputStream(baos)) {
                dos.write(rawBytes);
            }
            processedBytes = baos.toByteArray();
        } else {
            processedBytes = rawBytes;
        }

        // In-place XOR encryption on the compressed byte array
        for (int i = 0; i < processedBytes.length; i++) {
            processedBytes[i] ^= xorKey;
        }

        // Ensure target parent directories exist
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        // Write directly to disk using buffered output stream
        try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(file))) {
            fos.write(processedBytes);
        }
    }

    /**
     * Reads a file, decrypts (XOR), and decompresses it to raw bytes.
     * Uses BufferedInputStream to optimize file reads.
     */
    public static byte[] load(File file, byte xorKey, Compression compression) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Cache file not found: " + file.getPath());
        }

        int fileLen = (int) file.length();
        byte[] encryptedBytes = new byte[fileLen];

        // Efficient read block
        try (InputStream fis = new BufferedInputStream(new FileInputStream(file))) {
            int read = 0;
            while (read < fileLen) {
                int r = fis.read(encryptedBytes, read, fileLen - read);
                if (r == -1) break;
                read += r;
            }
        }

        // In-place XOR decryption
        for (int i = 0; i < fileLen; i++) {
            encryptedBytes[i] ^= xorKey;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(encryptedBytes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(fileLen * 2);

        if (compression == Compression.GZIP) {
            try (GZIPInputStream gzis = new GZIPInputStream(bais)) {
                byte[] buffer = new byte[4096];
                int len;
                while ((len = gzis.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
            }
        } else if (compression == Compression.DEFLATE) {
            try (InflaterInputStream iis = new InflaterInputStream(bais)) {
                byte[] buffer = new byte[4096];
                int len;
                while ((len = iis.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
            }
        } else {
            return encryptedBytes;
        }

        return baos.toByteArray();
    }

    /**
     * Converts a Map to JSON, GZIP compresses, XOR encrypts, and saves it.
     */
    @SuppressWarnings("unchecked")
    public static void saveJsonMap(File file, Map<String, Object> data, byte xorKey) throws IOException {
        JSONObject obj = new JSONObject();
        obj.putAll(data);
        String jsonStr = obj.toJSONString();
        byte[] rawBytes = jsonStr.getBytes(StandardCharsets.UTF_8);
        save(file, rawBytes, xorKey, Compression.GZIP);
    }

    /**
     * Reads a cache file, decrypts, GZIP decompresses, and parses it back to a JSON Map.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadJsonMap(File file, byte xorKey) throws IOException {
        byte[] rawBytes = load(file, xorKey, Compression.GZIP);
        String jsonStr = new String(rawBytes, StandardCharsets.UTF_8);
        Object parsed = JSONValue.parse(jsonStr);
        if (parsed instanceof Map) {
            return (Map<String, Object>) parsed;
        }
        return null;
    }

    /**
     * Converts a long array to bytes, Deflate compresses, XOR encrypts, and saves it.
     */
    public static void saveLongArray(File file, long[] data, byte xorKey) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(data.length * 8);
        for (long val : data) {
            buf.putLong(val);
        }
        save(file, buf.array(), xorKey, Compression.DEFLATE);
    }

    /**
     * Reads a cache file, decrypts, Deflate decompresses, and decodes it back to a long array.
     */
    public static long[] loadLongArray(File file, int expectedLength, byte xorKey) throws IOException {
        byte[] rawBytes = load(file, xorKey, Compression.DEFLATE);
        ByteBuffer buf = ByteBuffer.wrap(rawBytes);
        long[] data = new long[expectedLength];
        for (int i = 0; i < expectedLength; i++) {
            data[i] = buf.getLong();
        }
        return data;
    }
}
