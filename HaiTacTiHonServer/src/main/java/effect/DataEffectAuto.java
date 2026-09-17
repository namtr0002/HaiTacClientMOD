package effect;

import core.ZUtil;
import database.DbManager;
import network.Message;
import network.Session;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataEffectAuto {

    private static final java.util.concurrent.ConcurrentHashMap<Integer, byte[]> SQL_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    public static void clearCache() {
        SQL_CACHE.clear();
    }

    public static byte[] loadFromSQL(int id) {
        byte[] cached = SQL_CACHE.get(id);
        if (cached != null) {
            return cached.length > 0 ? cached : null;
        }

        try (Connection conn = DbManager.gI().getConnect()) {
            if (conn != null) {
                String query = "SELECT sprites, frames, squence FROM effect_auto_data WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String sprites = rs.getString("sprites");
                            String frames = rs.getString("frames");
                            String squence = rs.getString("squence");
                            if (sprites != null && frames != null && squence != null) {
                                byte[] bytes = serializeToBytes(id, sprites, frames, squence);
                                if (bytes != null && bytes.length > 0) {
                                    SQL_CACHE.put(id, bytes);
                                    return bytes;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Negative cache: prevent repeatedly querying MySQL for missing effect auto
        SQL_CACHE.put(id, new byte[0]);
        return null;
    }

    private static byte[] serializeToBytes(int id, String sprites, String frames, String squence) {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);

            // Write ID
            dos.writeShort(id);

            // Temporary stream for metadata to compute length
            ByteArrayOutputStream metadataBaos = new ByteArrayOutputStream();
            DataOutputStream metadataDos = new DataOutputStream(metadataBaos);

            // 1. parse sprites
            JSONObject spritesObj = (JSONObject) JSONValue.parse(sprites);
            if (spritesObj == null) return null;
            metadataDos.writeByte(spritesObj.size());
            for (int i = 0; i < spritesObj.size(); i++) {
                JSONObject child = (JSONObject) spritesObj.get(String.valueOf(i));
                if (child == null) {
                    child = (JSONObject) spritesObj.get(i);
                }
                if (child != null) {
                    metadataDos.writeByte(getInt(child, "ID", getInt(child, "id", i)));
                    metadataDos.writeByte(getInt(child, "x", 0));
                    metadataDos.writeByte(getInt(child, "y", 0));
                    metadataDos.writeByte(getInt(child, "w", 0));
                    metadataDos.writeByte(getInt(child, "h", 0));
                } else {
                    metadataDos.writeByte(i);
                    metadataDos.writeByte(0);
                    metadataDos.writeByte(0);
                    metadataDos.writeByte(0);
                    metadataDos.writeByte(0);
                }
            }

            // 2. parse frames
            JSONArray framesArr = (JSONArray) JSONValue.parse(frames);
            if (framesArr == null) return null;
            metadataDos.writeShort(framesArr.size());
            for (int i = 0; i < framesArr.size(); i++) {
                JSONObject frameObj = (JSONObject) framesArr.get(i);
                JSONArray mpart = (JSONArray) frameObj.get("mpart");
                metadataDos.writeByte(mpart.size());
                for (int j = 0; j < mpart.size(); j++) {
                    JSONObject part = (JSONObject) mpart.get(j);
                    metadataDos.writeShort(getInt(part, "x", 0));
                    metadataDos.writeShort(getInt(part, "y", 0));
                    metadataDos.writeByte(getInt(part, "idPartImage", getInt(part, "idPart", 0)));
                }
            }

            // 3. parse squence
            JSONArray seqArr = (JSONArray) JSONValue.parse(squence);
            if (seqArr == null) return null;
            metadataDos.writeShort(seqArr.size());
            for (int i = 0; i < seqArr.size(); i++) {
                Object val = seqArr.get(i);
                if (val instanceof Number) {
                    metadataDos.writeShort(((Number) val).shortValue());
                } else {
                    metadataDos.writeShort(Short.parseShort(String.valueOf(val)));
                }
            }

            // 4. extra bytes
            metadataDos.writeByte(0);
            metadataDos.writeByte(0);

            metadataDos.flush();
            byte[] metadataBytes = metadataBaos.toByteArray();

            // Write metadata length and bytes
            dos.writeShort(metadataBytes.length);
            dos.write(metadataBytes);

            dos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) dos.close();
                if (baos != null) baos.close();
            } catch (IOException e) {}
        }
        return null;
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

    public static void sendData(Session conn, int id) {
        if (conn == null) return;
        try {
            byte[] serialized = loadFromSQL(id);
            if (serialized != null) {
                // Loaded and serialized from SQL, now append the PNG image
                byte[] image = ZUtil.loadfile("data/EffectAuto/x" + conn.zoomlv + "/" + id + ".png");
                if (image == null) {
                    image = ZUtil.loadfile("data/EffectAuto/img/" + id + ".png");
                }
                if (image != null) {
                    Message m2 = new Message(-44);
                    m2.writer().write(serialized);
                    m2.writer().write(image);
                    conn.addmsg(m2);
                    m2.cleanup();
                    return;
                }
            }

            // Fallback: load pre-combined binary file
            byte[] binaryCombined = ZUtil.loadfile("data/EffectAuto/data/" + id);
            if (binaryCombined != null) {
                Message m2 = new Message(-44);
                m2.writer().write(binaryCombined);
                conn.addmsg(m2);
                m2.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
