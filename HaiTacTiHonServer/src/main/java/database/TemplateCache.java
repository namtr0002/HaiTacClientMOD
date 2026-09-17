package database;

import core.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * TemplateCache - SSD file cache for SQL template tables loaded at server startup.
 *
 * On first boot (or after cache invalidation), templates are loaded from MySQL and
 * serialized to GZIP+XOR encrypted .dat files under ./.cache/templates/.
 * On subsequent boots, the cache is validated by row count; if count matches, data is
 * loaded from disk instead of MySQL, reducing startup time significantly.
 *
 * Cache format:
 *   table.dat  - GZIP+XOR encrypted JSON array of rows (List of Map)
 *   table.meta - plain-text: rowCount|lastModifiedMs
 */
public class TemplateCache {

    private static final String CACHE_DIR = "./.cache/templates";
    private static final byte XOR_KEY = 0x5C;
    private static volatile boolean enabled = false;

    /**
     * Tables that must ALWAYS be loaded fresh from MySQL on every server restart.
     * These are gameplay-critical tables whose data must never be served from a
     * stale disk cache regardless of the cache-enable-ssd setting.
     */
    private static final Set<String> NEVER_CACHE_TABLES = new HashSet<>(Arrays.asList(
            "skill",        // Skill templates — game balance data
            "mobs",         // Mob templates
            "maps",         // Map templates
            "item3",        // Equipment templates
            "item4",        // Potion/consumable templates
            "item7",        // Material templates
            "item8",        // Special item templates
            "itemhair",     // Cosmetic templates
            "fashiontemplate", // Fashion templates
            "itemoption",   // Item option templates
            "parts"         // Part templates
    ));

    public static void setEnabled(boolean value) { enabled = value; }
    public static boolean isEnabled() { return enabled; }

    /**
     * Attempt to load cached rows for a given SQL table.
     * Returns null (forcing a MySQL reload) when:
     *   - disk cache is disabled, OR
     *   - the table is in the NEVER_CACHE_TABLES blacklist.
     */
    public static List<Map<String, Object>> load(String tableName, int liveCount) {
        if (!enabled) return null;
        if (NEVER_CACHE_TABLES.contains(tableName)) return null;
        try {
            File metaFile = metaFile(tableName);
            File dataFile = dataFile(tableName);
            if (!metaFile.exists() || !dataFile.exists()) return null;
            String[] meta = readMeta(metaFile);
            if (meta == null) return null;
            int cachedCount = Integer.parseInt(meta[0].trim());
            if (cachedCount != liveCount) {
                Log.info("TemplateCache", tableName + ": count mismatch ("
                        + cachedCount + " cached vs " + liveCount + " live) - reloading from MySQL");
                deleteFiles(tableName);
                return null;
            }
            List<Map<String, Object>> rows = loadData(dataFile);
            if (rows == null) { deleteFiles(tableName); return null; }
            return rows;
        } catch (Exception e) {
            Log.warn("TemplateCache", "Failed to load cache for " + tableName + ": " + e.getMessage());
            deleteFiles(tableName);
            return null;
        }
    }

    /**
     * Save rows to disk cache for a given SQL table.
     * Skips saving when the table is in the NEVER_CACHE_TABLES blacklist.
     */
    public static void save(String tableName, List<Map<String, Object>> rows) {
        if (!enabled || rows == null) return;
        if (NEVER_CACHE_TABLES.contains(tableName)) return;
        try {
            new File(CACHE_DIR).mkdirs();
            checkDiskQuota();
            saveData(dataFile(tableName), rows);
            writeMeta(metaFile(tableName), rows.size());
        } catch (Exception e) {
            Log.warn("TemplateCache", "Failed to write cache for " + tableName + ": " + e.getMessage());
            deleteFiles(tableName);
        }
    }


    private static void checkDiskQuota() {
        File dir = new File(CACHE_DIR);
        if (!dir.exists()) return;
        File[] files = dir.listFiles();
        if (files == null) return;
        long totalSize = 0;
        for (File f : files) { if (f.isFile()) totalSize += f.length(); }
        // 500 MB quota for static templates
        long maxQuota = 500L * 1024 * 1024;
        if (totalSize > maxQuota) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            for (File f : files) {
                if (f.isFile()) {
                    long size = f.length();
                    if (f.delete()) {
                        totalSize -= size;
                        if (totalSize <= maxQuota * 0.8) break;
                    }
                }
            }
        }
    }

    /** Invalidate disk cache for a specific table. Next boot re-fetches from MySQL. */
    public static void invalidate(String tableName) {
        deleteFiles(tableName);
        Log.info("TemplateCache", "Cache invalidated for: " + tableName);
    }

    /** Invalidate all template cache files. */
    public static void invalidateAll() {
        File dir = new File(CACHE_DIR);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) { for (File f : files) { f.delete(); } }
        }
        Log.info("TemplateCache", "All template caches invalidated.");
    }

    /**
     * Query SELECT COUNT(*) FROM table. Returns -1 on failure (cache miss).
     */
    public static int queryCount(String tableName) {
        try (java.sql.Connection conn = DbManager.gI().getConnect();
             java.sql.Statement st = conn.createStatement();
             java.sql.ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM `" + tableName + "`")) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            Log.warn("TemplateCache", "COUNT query failed for " + tableName + ": " + e.getMessage());
        }
        return -1;
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private static File dataFile(String t) {
        return new File(CACHE_DIR, t.replaceAll("[^a-zA-Z0-9_\\-]", "_") + ".dat");
    }

    private static File metaFile(String t) {
        return new File(CACHE_DIR, t.replaceAll("[^a-zA-Z0-9_\\-]", "_") + ".meta");
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> loadData(File file) throws IOException {
        byte[] raw = readAndDecrypt(file);
        Object parsed = JSONValue.parse(new String(raw, StandardCharsets.UTF_8));
        if (!(parsed instanceof JSONArray)) return null;
        JSONArray arr = (JSONArray) parsed;
        List<Map<String, Object>> result = new ArrayList<>(arr.size());
        for (Object obj : arr) {
            if (obj instanceof Map) result.add((Map<String, Object>) obj);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static void saveData(File file, List<Map<String, Object>> rows) throws IOException {
        JSONArray arr = new JSONArray();
        for (Map<String, Object> row : rows) {
            JSONObject o = new JSONObject();
            o.putAll(row);
            arr.add(o);
        }
        encryptAndWrite(file, arr.toJSONString().getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] readAndDecrypt(File file) throws IOException {
        int len = (int) file.length();
        byte[] bytes = new byte[len];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            int read = 0;
            while (read < len) {
                int r = bis.read(bytes, read, len - read);
                if (r == -1) break;
                read += r;
            }
        }
        for (int i = 0; i < bytes.length; i++) bytes[i] ^= XOR_KEY;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(len * 3);
        try (GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = gzis.read(buf)) != -1) baos.write(buf, 0, n);
        }
        return baos.toByteArray();
    }

    private static void encryptAndWrite(File file, byte[] raw) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(raw.length / 2);
        try (GZIPOutputStream gzos = new GZIPOutputStream(baos)) { gzos.write(raw); }
        byte[] c = baos.toByteArray();
        for (int i = 0; i < c.length; i++) c[i] ^= XOR_KEY;
        file.getParentFile().mkdirs();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            bos.write(c);
        }
    }

    private static void writeMeta(File f, int rowCount) throws IOException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(f), StandardCharsets.UTF_8))) {
            pw.print(rowCount + "|" + System.currentTimeMillis());
        }
    }

    private static String[] readMeta(File f) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line = br.readLine();
            return (line == null) ? null : line.split("\\|");
        }
    }

    private static void deleteFiles(String t) { dataFile(t).delete(); metaFile(t).delete(); }
}
