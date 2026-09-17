package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DbResultRow {
    private Map<String, Object> data;
    private ResultSet rs;

    public DbResultRow(ResultSet rs) {
        this.rs = rs;
    }

    public DbResultRow(Map<String, Object> data) {
        this.data = data;
    }

    public boolean next() throws SQLException {
        if (rs != null) return rs.next();
        return data != null;
    }

    public String getString(String col) {
        try {
            if (rs != null) return rs.getString(col);
            Object val = data != null ? data.get(col) : null;
            return val == null ? null : val.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public int getInt(String col) {
        try {
            if (rs != null) return rs.getInt(col);
            Object val = data != null ? data.get(col) : null;
            if (val instanceof Number) return ((Number) val).intValue();
            return val == null ? 0 : Integer.parseInt(val.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public byte getByte(String col) {
        try {
            if (rs != null) return rs.getByte(col);
            Object val = data != null ? data.get(col) : null;
            if (val instanceof Number) return ((Number) val).byteValue();
            return val == null ? 0 : Byte.parseByte(val.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public short getShort(String col) {
        try {
            if (rs != null) return rs.getShort(col);
            Object val = data != null ? data.get(col) : null;
            if (val instanceof Number) return ((Number) val).shortValue();
            return val == null ? 0 : Short.parseShort(val.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public long getLong(String col) {
        try {
            if (rs != null) return rs.getLong(col);
            Object val = data != null ? data.get(col) : null;
            if (val instanceof Number) return ((Number) val).longValue();
            return val == null ? 0L : Long.parseLong(val.toString());
        } catch (Exception e) {
            return 0L;
        }
    }

    public double getDouble(String col) {
        try {
            if (rs != null) return rs.getDouble(col);
            Object val = data != null ? data.get(col) : null;
            if (val instanceof Number) return ((Number) val).doubleValue();
            return val == null ? 0.0 : Double.parseDouble(val.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    public float getFloat(String col) {
        try {
            if (rs != null) return rs.getFloat(col);
            Object val = data != null ? data.get(col) : null;
            if (val instanceof Number) return ((Number) val).floatValue();
            return val == null ? 0.0f : Float.parseFloat(val.toString());
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public java.sql.Timestamp getTimestamp(String col) {
        try {
            if (rs != null) return rs.getTimestamp(col);
            Object val = data != null ? data.get(col) : null;
            if (val instanceof java.sql.Timestamp) return (java.sql.Timestamp) val;
            if (val instanceof java.util.Date) return new java.sql.Timestamp(((java.util.Date) val).getTime());
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
