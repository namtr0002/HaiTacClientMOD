import java.io.*;
import java.util.*;
//ok
public class Ageteff {
    private static final String BASE = "C:\\ThMadara\\HTTH\\res\\effect\\"+Ageticon.fakezoomlv+"\\";
    private static final int MAX_ID = 256;
    private static boolean AUTO_SAVE = true;
    private static boolean running = true;
    public static void run() {
        if (running) {
            System.out.println("Starting Ageteff auto-download and save...");
            for (short id = 1; id <= MAX_ID; id++) {
                try {
                    if (id >= MAX_ID) {
                        running = false;
                        return;
                    }
                    System.out.println("Requesting effect ID: " + id);
                    GlobalService.getInstance().getDataSkillEff((byte) 0, id);
                    Thread.sleep(50);
                } catch (Exception e) {
                    System.err.println("Error get effId=" + id + " -> " + e.getMessage());
                }
            }
        }
    }

    public static void run(short id) {
        System.out.println("Requesting single effect ID: " + id);
        GlobalService.getInstance().getDataSkillEff((byte) 0, id);
    }
    
    public static void setAutoSave(boolean enabled) {
        AUTO_SAVE = enabled;
        System.out.println("Auto save " + (enabled ? "enabled" : "disabled"));
    }

    public static void processEffectData(byte[] payload) {
        if (!AUTO_SAVE || payload == null || payload.length < 4) return;
        
        try {
            save(payload);
            System.out.println("Auto-saved effect data successfully");
        } catch (Exception e) {
            System.err.println("Auto-save failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void save(byte[] payload) {
        if (payload == null || payload.length < 4) return;
        
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(payload));
        try {
            short effId = dis.readShort();
            short len = dis.readShort();
            if (len <= 0 || effId <= 0) {
                System.out.println("Skip save invalid effect " + effId + " len=" + len);
                return;
            }
            byte[] raw = new byte[len];
            dis.readFully(raw);
            byte[] img = new byte[dis.available()];
            dis.readFully(img);
            
            createDirectories();
            
            saveDataFile(effId, raw);
            
            byte[] enc = encode(raw);
            saveEncFile(effId, enc);
            
            String ext = isPng(img) ? ".png" : ".img";
            saveImageFile(effId, img, ext);
            
            Hashtable parsed = parseRaw(raw);
            String sql = buildSql(effId, parsed, raw);
            if (sql.length() > 0) {
                saveSqlFile(effId, sql);
            }
            
            System.out.println("Saved effect " + effId + " -> data:" + raw.length + " bytes, img:" + img.length + " bytes" + ext);
            
        } catch (Exception e) {
            System.err.println("Save error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { dis.close(); } catch (Exception ignore) {}
        }
    }
    
    private static byte[] encode(byte[] raw) {
        byte[] enc = new byte[raw.length];
        byte key = (byte)0x5A;
        for (int i = 0; i < raw.length; i++) {
            enc[i] = (byte)(raw[i] ^ key);
        }
        return enc;
    }
    
    private static void saveEncFile(short effId, byte[] enc) throws IOException {
        FileOutputStream fos = new FileOutputStream(BASE + "data\\" + effId + ".enc");
        fos.write(enc);
        fos.close();
    }
    

    private static void createDirectories() {
        new File(BASE + "data").mkdirs();
        new File(BASE + "img").mkdirs();
        new File(BASE + "sql").mkdirs();
    }
    
    private static void saveDataFile(short effId, byte[] raw) throws IOException {
        FileOutputStream fos = new FileOutputStream(BASE + "data\\" + effId + ".data");
        fos.write(raw);
        fos.close();
    }
    
    private static void saveImageFile(short effId, byte[] img, String ext) throws IOException {
        FileOutputStream fos = new FileOutputStream(BASE + "img\\" + effId + ext);
        fos.write(img);
        fos.close();
    }
    
    private static void saveSqlFile(short effId, String sql) throws IOException {
        FileOutputStream fos = new FileOutputStream(BASE + "sql\\" + effId + ".sql");
        fos.write(sql.getBytes("UTF-8"));
        fos.close();
    }

    private static boolean isPng(byte[] img) {
        if (img.length < 8) return false;
        return img[0] == (byte)0x89 && img[1] == 'P' && img[2] == 'N' && img[3] == 'G' 
            && img[4] == 13 && img[5] == 10 && img[6] == 26 && img[7] == 10;
    }

    private static Hashtable parseRaw(byte[] raw) {
    if (raw.length == 0) return null;
    Hashtable result = new Hashtable();
    DataInputStream dis = new DataInputStream(new ByteArrayInputStream(raw));
    try {
        int numImages = dis.readUnsignedByte();
        Vector images = new Vector();
        
        for (int i = 0; i < numImages; i++) {
            Hashtable im = new Hashtable();
            im.put("idx", dis.readUnsignedByte());
            im.put("x", dis.readUnsignedByte());
            im.put("y", dis.readUnsignedByte());
            im.put("w", dis.readUnsignedByte());
            im.put("h", dis.readUnsignedByte());
            images.addElement(im);
        }
        
        int numFrames = dis.readUnsignedShort();
        Vector frames = new Vector();
        for (int f = 0; f < numFrames; f++) {
            Hashtable frame = new Hashtable();
            frame.put("frame_idx", f);

            Vector parts = new Vector();
            int numParts = dis.readUnsignedByte();
            
            for (int p = 0; p < numParts; p++) {
                Hashtable part = new Hashtable();
                part.put("dx", dis.readShort());
                part.put("dy", dis.readShort());
                part.put("img_id", dis.readUnsignedByte());
                part.put("flip", dis.readUnsignedByte());
                part.put("on_top", dis.readUnsignedByte());
                parts.addElement(part);
            }
            frame.put("parts", parts);
            frames.addElement(frame);
        }
        
        int seqLen = dis.readUnsignedByte();
        Vector sequences = new Vector();
        for (int s = 0; s < seqLen; s++) {
            Hashtable seq = new Hashtable();
            seq.put("frame_idx", dis.readShort());
            sequences.addElement(seq);
        }
        
        dis.readByte();
        Vector charFrames = new Vector();
        for (int t = 0; t < 3; t++) {
            int len = dis.readUnsignedByte();
            int pt = (t == 2) ? 3 : t;
            for (int i = 0; i < len; i++) {
                Hashtable cf = new Hashtable();
                cf.put("part_type", pt);
                cf.put("frame_idx", dis.readUnsignedByte());
                charFrames.addElement(cf);
            }
        }
        
        try {
            dis.readInt(); // có thể có extra data
        } catch (Exception ignore) {}
        
        Vector splash = new Vector();
        for (int i = 0; i < 3; i++) {
            Hashtable sp = new Hashtable();
            sp.put("index_splash", 0);
            splash.addElement(sp);
        }
        
        result.put("num_images", numImages);
        result.put("seq_length", seqLen);
        result.put("has_extended", charFrames.isEmpty() ? 0 : 1);
        result.put("images", images);
        result.put("frames", frames);
        result.put("sequences", sequences);
        result.put("char_frames", charFrames);
        result.put("splash", splash);
        
        return result;
    } catch (Exception e) {
        System.err.println("Parse error: " + e.getMessage());
        return null;
    } finally {
        try { dis.close(); } catch (Exception ignore) {}
    }
}


@SuppressWarnings("unchecked")
private static String buildSql(short effId, Hashtable parsed, byte[] raw) {
    if (parsed == null) return "";
    StringBuilder sb = new StringBuilder();
    sb.append("INSERT INTO effect_data (");
    sb.append("eff_id, num_images, seq_length, has_extended, ");
    sb.append("images, frames, sequences, char_frames, splash, ");
    sb.append("raw_data, raw_compressed, parse_ok, parse_error) VALUES (");

    sb.append(effId).append(", ");
    sb.append(parsed.get("num_images")).append(", ");
    sb.append(parsed.get("seq_length")).append(", ");
    sb.append(parsed.get("has_extended")).append(", ");

    // Serialize các field thành JSON chuẩn
    sb.append("'").append(escapeSql(json(parsed.get("images")))).append("',");
    sb.append("'").append(escapeSql(json(parsed.get("frames")))).append("',");
    sb.append("'").append(escapeSql(json(parsed.get("sequences")))).append("',");
    sb.append("'").append(escapeSql(json(parsed.get("char_frames")))).append("',");
    sb.append("'").append(escapeSql(json(parsed.get("splash")))).append("',");

    sb.append("X'").append(toHex(raw)).append("',");
    sb.append("NULL,1,NULL) "); // raw_compressed=NULL, parse_ok=1, parse_error=NULL

    sb.append("ON DUPLICATE KEY UPDATE ");
    sb.append("num_images=VALUES(num_images), ");
    sb.append("seq_length=VALUES(seq_length), ");
    sb.append("has_extended=VALUES(has_extended), ");
    sb.append("images=VALUES(images), ");
    sb.append("frames=VALUES(frames), ");
    sb.append("sequences=VALUES(sequences), ");
    sb.append("char_frames=VALUES(char_frames), ");
    sb.append("splash=VALUES(splash), ");
    sb.append("raw_data=VALUES(raw_data), ");
    sb.append("raw_compressed=VALUES(raw_compressed), ");
    sb.append("parse_ok=VALUES(parse_ok), ");
    sb.append("parse_error=VALUES(parse_error);");

    return sb.toString();
}

/** Escape để nhét JSON vào SQL */
private static String escapeSql(String s) {
    if (s == null) return "null";
    return s.replace("\\", "\\\\").replace("'", "\\'");
}

    /** Serializer JSON cho Vector + Hashtable */
    private static String json(Object obj) {
        if (obj == null) return "null";

        if (obj instanceof Vector) {
            Vector v = (Vector) obj;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < v.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(json(v.elementAt(i))); // đệ quy
            }
            sb.append("]");
            return sb.toString();
        } 
        else if (obj instanceof Hashtable) {
            Hashtable h = (Hashtable) obj;
            StringBuilder sb = new StringBuilder("{");
            Enumeration e = h.keys();
            boolean first = true;
            while (e.hasMoreElements()) {
                if (!first) sb.append(",");
                first = false;
                String k = (String) e.nextElement();
                sb.append("\"").append(k).append("\":");
                Object val = h.get(k);
                if (val instanceof Number || val instanceof Boolean) {
                    sb.append(val.toString());
                } else {
                    sb.append(json(val)); // đệ quy cho nested
                }
            }
            sb.append("}");
            return sb.toString();
        } 
        else if (obj instanceof String) {
            return "\"" + escapeJson((String) obj) + "\"";
        } 
        else {
            return obj.toString();
        }
    }
private static String escapeJson(String s) {
    return s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
}

/** Escape chuỗi JSON */
private static String escapeJsonString(String s) {
    if (s == null) return "";
    return s.replace("\\", "\\\\").replace("\"", "\\\"");
}

/** Chuyển byte[] sang hex */
private static String toHex(byte[] data) {
    StringBuilder sb = new StringBuilder();
    for (byte b : data) {
        sb.append(String.format("%02X", b));
    }
    return sb.toString();
}

    public static void saveEffectById(short effId) {
        System.out.println("Manual save request for effect ID: " + effId);
        run(effId);
    }
    
    public static void clearAllEffects() {
        try {
            File dataDir = new File(BASE + "data");
            File imgDir = new File(BASE + "img");
            File sqlDir = new File(BASE + "sql");
            
            deleteDirectory(dataDir);
            deleteDirectory(imgDir);
            deleteDirectory(sqlDir);
            
            System.out.println("Cleared all effect files");
        } catch (Exception e) {
            System.err.println("Clear failed: " + e.getMessage());
        }
    }
    
    private static void deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }
    
    public static void showStatus() {
        File dataDir = new File(BASE + "data");
        File imgDir = new File(BASE + "img");
        File sqlDir = new File(BASE + "sql");
        
        int dataCount = dataDir.exists() ? dataDir.list().length : 0;
        int imgCount = imgDir.exists() ? imgDir.list().length : 0;
        int sqlCount = sqlDir.exists() ? sqlDir.list().length : 0;
        
        System.out.println("=== Ageteff Status ===");
        System.out.println("Auto Save: " + (AUTO_SAVE ? "ON" : "OFF"));
        System.out.println("Data files: " + dataCount);
        System.out.println("Image files: " + imgCount);
        System.out.println("SQL files: " + sqlCount);
        System.out.println("Base path: " + BASE);
    }
}