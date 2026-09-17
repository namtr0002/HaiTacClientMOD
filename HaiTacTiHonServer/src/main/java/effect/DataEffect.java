package effect;

import core.ZUtil;
import database.DbManager;
import map.Map;
import map.Zone;
import model.Player;
import network.Message;
import network.Session;
import org.apache.commons.io.output.ByteArrayOutputStream;
import template.PartFrame;
import template.SmallImg;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataEffect {
    public static final List<DataEffect> ENTRY = new ArrayList<>();
    private static final ConcurrentHashMap<Integer, DataEffect> MAP_SQL_ENTRY = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, byte[]> CACHED_EFFECT_DATA = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, byte[]> CACHED_EFFECT_IMAGE = new ConcurrentHashMap<>();

    private static final byte[] FALLBACK_EMPTY_PNG = new byte[] {
        (byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52,
        0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x08, 0x06, 0x00, 0x00, 0x00, 0x1F, 0x15, (byte)0xC4,
        (byte)0x89, 0x00, 0x00, 0x00, 0x0A, 0x49, 0x44, 0x41, 0x54, 0x78, (byte)0x9C, 0x63, 0x60, 0x00, 0x00, 0x00,
        0x02, 0x00, 0x01, (byte)0xE2, 0x21, (byte)0xBC, 0x33, 0x00, 0x00, 0x00, 0x00, 0x49, 0x45, 0x4E, 0x44,
        (byte)0xAE, 0x42, 0x60, (byte)0x82
    };

    public static void clearCache() {
        CACHED_EFFECT_DATA.clear();
        CACHED_EFFECT_IMAGE.clear();
    }

    public static class FrameData {
        public List<PartFrame> parts = new ArrayList<>();
    }

    public int id;
    public int nImg;
    public int dx, dy;
    public List<SmallImg> smallImg = new ArrayList<>();
    public List<PartFrame> partFrame = new ArrayList<>();
    public List<FrameData> frames = new ArrayList<>();
    public short[] seq = new short[0];
    public List<Integer> frameChar = new ArrayList<>();
    public List<Integer> splash = new ArrayList<>();

    public static byte[] encodeDataEffect(DataEffect t) {
        if (t == null) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);

            dos.writeByte(t.smallImg.size());
            for (int j = 0; j < t.smallImg.size(); j++) {
                SmallImg s = t.smallImg.get(j);
                if (s.x > 255 || s.y > 255 || s.w > 255 || s.h > 255 || s.id > 254) {
                    dos.writeByte(255);
                    dos.writeByte(s.id & 0xFF);
                    dos.writeShort(s.x);
                    dos.writeShort(s.y);
                    dos.writeShort(s.w);
                    dos.writeShort(s.h);
                } else {
                    dos.writeByte(s.id & 0xFF);
                    dos.writeByte(s.x & 0xFF);
                    dos.writeByte(s.y & 0xFF);
                    dos.writeByte(s.w & 0xFF);
                    dos.writeByte(s.h & 0xFF);
                }
            }

            if (t.frames != null && !t.frames.isEmpty()) {
                dos.writeShort(t.frames.size());
                for (int j = 0; j < t.frames.size(); j++) {
                    FrameData fd = t.frames.get(j);
                    dos.writeByte(fd.parts.size() & 0xFF);
                    for (int k = 0; k < fd.parts.size(); k++) {
                        PartFrame pf = fd.parts.get(k);
                        dos.writeShort(pf.dx + t.dx);
                        dos.writeShort(pf.dy + t.dy);
                        dos.writeByte(pf.id & 0xFF);
                        if (pf.rotate != 0) {
                            dos.writeByte(128);
                            dos.writeByte(pf.flip & 0xFF);
                            dos.writeShort(pf.rotate);
                        } else {
                            dos.writeByte(pf.flip & 0xFF);
                        }
                        dos.writeByte(pf.onTop & 0xFF);
                    }
                }
            } else {
                dos.writeShort(t.partFrame.size());
                for (int j = 0; j < t.partFrame.size(); j++) {
                    PartFrame pf = t.partFrame.get(j);
                    dos.writeByte(1);
                    dos.writeShort(pf.dx + t.dx);
                    dos.writeShort(pf.dy + t.dy);
                    dos.writeByte(pf.id & 0xFF);
                    if (pf.rotate != 0) {
                        dos.writeByte(128);
                        dos.writeByte(pf.flip & 0xFF);
                        dos.writeShort(pf.rotate);
                    } else {
                        dos.writeByte(pf.flip & 0xFF);
                    }
                    dos.writeByte(pf.onTop & 0xFF);
                }
            }

            dos.writeByte(t.seq.length);
            for (int j = 0; j < t.seq.length; j++) {
                dos.writeShort(t.seq[j]);
            }

            dos.writeByte(0);
            dos.writeByte(0);
            dos.writeByte(0);
            dos.writeByte(0);
            dos.writeByte(0);
            dos.writeByte(0);
            dos.writeByte(0);

            dos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (dos != null) dos.close();
                if (baos != null) baos.close();
            } catch (IOException ignored) {}
        }
    }

    public static byte[] getDataByID(int id) {
        byte[] cached = CACHED_EFFECT_DATA.get(id);
        if (cached != null) {
            return cached.length > 0 ? cached : null;
        }

        DataEffect t = MAP_SQL_ENTRY.get(id);
        if (t == null) {
            // Check in ENTRY list
            for (int i = 0; i < ENTRY.size(); i++) {
                DataEffect cur = ENTRY.get(i);
                if (cur != null && cur.id == id) {
                    t = cur;
                    MAP_SQL_ENTRY.put(id, t);
                    break;
                }
            }
        }

        if (t == null) {
            // Query MySQL on demand
            try (Connection conn = DbManager.gI().getConnect()) {
                if (conn != null) {
                    String query = "SELECT id, nImg, sprites, frames, squence, frame_char, splash FROM effect_data WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(query)) {
                        ps.setInt(1, id);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                t = parseDataEffect(rs);
                                ENTRY.add(t);
                                MAP_SQL_ENTRY.put(id, t);
                            }
                        }
                    }
                }
            } catch (Exception ignored) {}
        }

        if (t != null) {
            byte[] encoded = encodeDataEffect(t);
            if (encoded != null && encoded.length > 0) {
                CACHED_EFFECT_DATA.put(id, encoded);
                return encoded;
            }
        }

        // Negative cache: prevent hitting MySQL again for non-existent effect ID
        CACHED_EFFECT_DATA.put(id, new byte[0]);
        return null;
    }

    public static byte[] getEffectImage(int zoom, int id) {
        int z = (zoom > 0 && zoom <= 4) ? zoom : 4;
        String cacheKey = z + "_" + id;
        byte[] cached = CACHED_EFFECT_IMAGE.get(cacheKey);
        if (cached != null && cached.length > 0) {
            return cached;
        }

        String[] paths = {
            "data/Effect/x" + z + "/" + id + ".png"
        };

        for (String path : paths) {
            try {
                byte[] data = ZUtil.loadfile(path);
                if (data != null && data.length > 0) {
                    //CACHED_EFFECT_IMAGE.put(cacheKey, data);
                    return data;
                }
            } catch (Exception ignored) {}
        }

        return FALLBACK_EMPTY_PNG;
    }

    public static byte[] getEffectData(int zoom, int id) {
        // 1. Check in-memory binary cache
        byte[] cached = CACHED_EFFECT_DATA.get(id);
        if (cached != null) {
            return cached.length > 0 ? cached : null;
        }

        // 2. Search binary files on disk across all paths
        int z = (zoom > 0 && zoom <= 4) ? zoom : 4;
        String[] paths = {
            "data/Effect/data/" + id
        };
        for (String path : paths) {
            try {
                byte[] data = ZUtil.loadfile(path);
                if (data != null && data.length > 0) {
                    CACHED_EFFECT_DATA.put(id, data);
                    return data;
                }
            } catch (Exception ignored) {}
        }

        // 3. Fallback to SQL database
        byte[] sqlData = getDataByID(id);
        if (sqlData != null && sqlData.length > 0) {
            return sqlData;
        }

        return null;
    }

    public static void sendData(Session conn, int id) {
        if (conn == null) return;
        try {
            int zoom = (conn.zoomlv > 0 && conn.zoomlv <= 4) ? conn.zoomlv : 4;
            byte[] data1 = getEffectData(zoom, id);
            if (data1 == null || data1.length == 0) {
                // System.out.println("[DataEffect] Missing data for effect id=" + id + " zoom=" + zoom);
                return;
            }
            byte[] data2 = getEffectImage(zoom, id);
            if (data2 == null || data2.length == 0) {
                data2 = FALLBACK_EMPTY_PNG;
            }
            int numBytes = 1 + 2 + 2 + data1.length + data2.length;
            Message m2 = new Message(numBytes >= 32_000 ? 76 : 74);
            m2.writer().writeByte(0);
            m2.writer().writeShort(id);
            m2.writer().writeShort(data1.length);
            m2.writer().write(data1);
            m2.writer().write(data2);
            conn.addmsg(m2);
            m2.cleanup();
        } catch (Exception e) {
            System.out.println("[DataEffect] ERROR sending effect id=" + id + " -> " + e.getMessage());
        }
    }

    public static void sendData(Player p, int id) {
        if (p != null && p.conn != null) {
            sendData(p.conn, id);
        }
    }

    public static void sendDataToZone(Zone zone, int id) {
        if (zone == null || zone.players == null) return;
        for (int i = 0; i < zone.players.size(); i++) {
            Player p = zone.players.get(i);
            if (p != null && p.conn != null) {
                sendData(p.conn, id);
            }
        }
    }

    public static void sendDataToMap(Map map, int id) {
        if (map == null || map.zones == null) return;
        for (Zone z : map.zones) {
            sendDataToZone(z, id);
        }
    }

    private static DataEffect parseDataEffect(ResultSet rs) throws Exception {
        DataEffect t = new DataEffect();
        t.id = rs.getInt("id");
        t.nImg = rs.getInt("nImg");
        t.dx = 0;
        t.dy = 0;
        t.smallImg = parseSmallImg(rs.getString("sprites"));
        t.frames = parseFrames(rs.getString("frames"));
        t.partFrame = parsePartFrames(rs.getString("frames"));
        t.seq = parseShortArray(rs.getString("squence"));
        t.frameChar = parseIntList(rs.getString("frame_char"));
        t.splash = parseIntList(rs.getString("splash"));
        return t;
    }

    private static void preCacheFolder(String folderPath) {
        try {
            File dir = new File(folderPath);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.isFile()) {
                            try {
                                int id = Integer.parseInt(f.getName());
                                byte[] b = ZUtil.loadfile(f.getPath());
                                if (b != null && b.length > 0) {
                                    CACHED_EFFECT_DATA.put(id, b);
                                }
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    public static void load() {
        ENTRY.clear();
        MAP_SQL_ENTRY.clear();
        CACHED_EFFECT_DATA.clear();
        CACHED_EFFECT_IMAGE.clear();

        // 1. Load all effects from MySQL
        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn != null) {
                String query = "SELECT id, nImg, sprites, frames, squence, frame_char, splash FROM effect_data";
                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery(query)) {
                    while (rs.next()) {
                        try {
                            DataEffect t = parseDataEffect(rs);
                            ENTRY.add(t);
                            MAP_SQL_ENTRY.put(t.id, t);
                            byte[] enc = encodeDataEffect(t);
                            if (enc != null && enc.length > 0) {
                                CACHED_EFFECT_DATA.put(t.id, enc);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Pre-cache binary files from disk (data/Effect/data has highest priority)
        preCacheFolder("data/template/skill/x1/data");
        preCacheFolder("data/Effect/data");

        core.Log.step("DataEffect", ENTRY.size() + " SQL effects, " + CACHED_EFFECT_DATA.size() + " cached effect datas");
    }

    private static List<SmallImg> parseSmallImg(String json) {
        List<SmallImg> list = new ArrayList<>();

        if (json == null || json.trim().isEmpty()) {
            return list;
        }

        Object parsed = JSONValue.parse(json);
        if (!(parsed instanceof JSONArray)) {
            return list;
        }

        JSONArray arr = (JSONArray) parsed;
        for (int i = 0; i < arr.size(); i++) {
            Object obj = arr.get(i);
            if (!(obj instanceof JSONObject)) {
                continue;
            }

            JSONObject jo = (JSONObject) obj;
            SmallImg img = new SmallImg();

            img.id = getInt(jo, "idx", getInt(jo, "id", 0));
            img.x = getInt(jo, "x", 0);
            img.y = getInt(jo, "y", 0);
            img.w = getInt(jo, "w", 0);
            img.h = getInt(jo, "h", 0);

            list.add(img);
        }

        return list;
    }

    private static List<FrameData> parseFrames(String json) {
        List<FrameData> list = new ArrayList<>();

        if (json == null || json.trim().isEmpty()) {
            return list;
        }

        Object parsed = JSONValue.parse(json);
        if (!(parsed instanceof JSONArray)) {
            return list;
        }

        JSONArray frames = (JSONArray) parsed;
        for (int i = 0; i < frames.size(); i++) {
            Object frameObj = frames.get(i);
            if (!(frameObj instanceof JSONObject)) {
                continue;
            }

            JSONObject frameJson = (JSONObject) frameObj;
            Object partsObj = frameJson.get("parts");
            if (!(partsObj instanceof JSONArray)) {
                continue;
            }

            JSONArray parts = (JSONArray) partsObj;
            FrameData fd = new FrameData();
            for (int j = 0; j < parts.size(); j++) {
                Object partObj = parts.get(j);
                if (!(partObj instanceof JSONObject)) {
                    continue;
                }

                JSONObject partJson = (JSONObject) partObj;
                PartFrame pf = new PartFrame();

                pf.dx = getInt(partJson, "dx", 0);
                pf.dy = getInt(partJson, "dy", 0);
                pf.id = (byte) getInt(partJson, "img_id", getInt(partJson, "imgId", 0));
                pf.flip = (byte) getInt(partJson, "flip", 0);
                pf.onTop = (byte) getInt(partJson, "on_top", getInt(partJson, "onTop", 0));
                pf.rotate = (short) getInt(partJson, "rotate", getInt(partJson, "rot", 0));

                fd.parts.add(pf);
            }
            list.add(fd);
        }

        return list;
    }

    private static List<PartFrame> parsePartFrames(String json) {
        List<PartFrame> list = new ArrayList<>();

        if (json == null || json.trim().isEmpty()) {
            return list;
        }

        Object parsed = JSONValue.parse(json);
        if (!(parsed instanceof JSONArray)) {
            return list;
        }

        JSONArray frames = (JSONArray) parsed;
        for (int i = 0; i < frames.size(); i++) {
            Object frameObj = frames.get(i);
            if (!(frameObj instanceof JSONObject)) {
                continue;
            }

            JSONObject frameJson = (JSONObject) frameObj;
            Object partsObj = frameJson.get("parts");
            if (!(partsObj instanceof JSONArray)) {
                continue;
            }

            JSONArray parts = (JSONArray) partsObj;
            for (int j = 0; j < parts.size(); j++) {
                Object partObj = parts.get(j);
                if (!(partObj instanceof JSONObject)) {
                    continue;
                }

                JSONObject partJson = (JSONObject) partObj;
                PartFrame pf = new PartFrame();

                pf.dx = getInt(partJson, "dx", 0);
                pf.dy = getInt(partJson, "dy", 0);
                pf.id = (byte) getInt(partJson, "img_id", getInt(partJson, "imgId", 0));
                pf.flip = (byte) getInt(partJson, "flip", 0);
                pf.onTop = (byte) getInt(partJson, "on_top", getInt(partJson, "onTop", 0));
                pf.rotate = (short) getInt(partJson, "rotate", getInt(partJson, "rot", 0));

                list.add(pf);
            }
        }

        return list;
    }

    private static short[] parseShortArray(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new short[0];
        }

        Object parsed = JSONValue.parse(json);
        if (!(parsed instanceof JSONArray)) {
            return new short[0];
        }

        JSONArray arr = (JSONArray) parsed;
        short[] result = new short[arr.size()];

        for (int i = 0; i < arr.size(); i++) {
            Object val = arr.get(i);
            try {
                if (val instanceof Number) {
                    result[i] = ((Number) val).shortValue();
                } else if (val instanceof JSONObject) {
                    JSONObject obj = (JSONObject) val;
                    if (obj.containsKey("id")) {
                        result[i] = Short.parseShort(String.valueOf(obj.get("id")));
                    } else if (obj.containsKey("part_type")) {
                        result[i] = Short.parseShort(String.valueOf(obj.get("part_type")));
                    } else {
                        result[i] = 0;
                    }
                } else {
                    result[i] = Short.parseShort(String.valueOf(val));
                }
            } catch (Exception e) {
                result[i] = 0;
            }
        }

        return result;
    }

    private static List<Integer> parseIntList(String json) {
        List<Integer> list = new ArrayList<>();

        if (json == null || json.trim().isEmpty()) {
            return list;
        }

        Object parsed = JSONValue.parse(json);
        if (!(parsed instanceof JSONArray)) {
            return list;
        }

        JSONArray arr = (JSONArray) parsed;
        for (int i = 0; i < arr.size(); i++) {
            Object val = arr.get(i);
            try {
                if (val instanceof Number) {
                    list.add(((Number) val).intValue());
                } else if (val instanceof JSONObject) {
                    JSONObject obj = (JSONObject) val;
                    if (obj.containsKey("id")) {
                        list.add(Integer.parseInt(String.valueOf(obj.get("id"))));
                    } else if (obj.containsKey("part_type")) {
                        list.add(Integer.parseInt(String.valueOf(obj.get("part_type"))));
                    } else {
                        list.add(0);
                    }
                } else {
                    list.add(Integer.parseInt(String.valueOf(val)));
                }
            } catch (Exception e) {
                list.add(0);
            }
        }

        return list;
    }

    private static int getInt(JSONObject obj, String key, int def) {
        Object v = obj.get(key);
        if (v == null) {
            return def;
        }

        try {
            return Integer.parseInt(String.valueOf(v));
        } catch (Exception e) {
            return def;
        }
    }
}