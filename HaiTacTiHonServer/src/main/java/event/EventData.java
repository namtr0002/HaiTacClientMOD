package event;

import java.util.HashMap;
import java.util.Map;

public class EventData {
    public int eventID;
    public String key;
    public String info;
    public int[] data;

    // Map lưu điểm sự kiện theo key để dễ quản lý, thay vì dùng mảng index
    public Map<String, Integer> points = new HashMap<>();

    public int getPoint(String pointKey) {
        if (points == null) points = new HashMap<>();
        if (pointKey == null) return 0;
        return points.getOrDefault(pointKey.trim(), 0);
    }

    public int getPoint(String pointKey, int defaultVal) {
        if (points == null) points = new HashMap<>();
        if (pointKey == null) return defaultVal;
        return points.getOrDefault(pointKey.trim(), defaultVal);
    }

    public boolean hasPoint(String pointKey) {
        if (points == null || pointKey == null) return false;
        return points.containsKey(pointKey.trim());
    }

    public void addPoint(String pointKey, int value) {
        if (points == null) points = new HashMap<>();
        if (pointKey == null) return;
        String k = pointKey.trim();
        points.put(k, getPoint(k) + value);
    }

    public void setPoint(String pointKey, int value) {
        if (points == null) points = new HashMap<>();
        if (pointKey == null) return;
        points.put(pointKey.trim(), value);
    }

    public int getData(String key) {
        return getPoint(key);
    }

    public int getData(String key, int defaultVal) {
        return getPoint(key, defaultVal);
    }

    public void setData(String key, int value) {
        setPoint(key, value);
    }

    public void addData(String key, int value) {
        addPoint(key, value);
    }

    public int getLimit(String key) {
        return getPoint(key);
    }

    public int getLimit(String key, int defaultVal) {
        return getPoint(key, defaultVal);
    }

    public void setLimit(String key, int value) {
        setPoint(key, value);
    }

    public void addLimit(String key, int value) {
        addPoint(key, value);
    }

    /**
     * Reset toàn bộ các biến đếm hoạt động hàng ngày (những key chứa "daily", "_day" hoặc "d_")
     */
    public void resetDaily() {
        if (points != null) {
            java.util.List<String> dailyKeys = new java.util.ArrayList<>();
            for (String k : points.keySet()) {
                if (k != null) {
                    String lk = k.toLowerCase();
                    if (lk.contains("daily") || lk.contains("_day") || lk.startsWith("d_") || lk.contains("limit_day") || lk.contains("drop_limit")) {
                        dailyKeys.add(k);
                    }
                }
            }
            for (String k : dailyKeys) {
                points.put(k, 0);
            }
        }
    }
}
