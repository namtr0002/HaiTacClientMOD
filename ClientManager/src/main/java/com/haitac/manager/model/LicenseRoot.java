package com.haitac.manager.model;

import java.util.*;

/**
 * Model gốc chứa toàn bộ dữ liệu phân quyền và danh sách key
 */
public class LicenseRoot {

    private String version;
    private boolean masterEnabled;
    private boolean serverCheckEnabled;
    private String globalNotice;
    private long updatedAt;
    private List<ProductLine> lines;
    private List<KeyItem> keys;

    public LicenseRoot() {
        this.version = "1.0";
        this.masterEnabled = true;
        this.serverCheckEnabled = true;
        this.globalNotice = "liên hệ t.me/@ThanhNamYe để thuê mod nhé";
        this.updatedAt = System.currentTimeMillis();
        this.lines = new ArrayList<ProductLine>();
        this.keys = new ArrayList<KeyItem>();

        lines.add(new ProductLine("MOD_UNITY", "Bản MOD Unity (PC & Mobile)", true, true));
        lines.add(new ProductLine("MOD_J2ME", "Bản MOD J2ME Java", true, true));
        lines.add(new ProductLine("MOD_LIBGDX", "Bản MOD LibGDX (PC & Mobile)", true, true));
    }

    public ProductLine findLineById(String lineId) {
        if (lines == null || lineId == null) return null;
        for (ProductLine pl : lines) {
            if (lineId.equalsIgnoreCase(pl.getLineId())) {
                return pl;
            }
        }
        return null;
    }

    public KeyItem findKey(String keyCode) {
        if (keys == null || keyCode == null) return null;
        for (KeyItem item : keys) {
            if (keyCode.equalsIgnoreCase(item.getKey())) {
                return item;
            }
        }
        return null;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("version", version);
        map.put("master_enabled", masterEnabled);
        map.put("server_check_enabled", serverCheckEnabled);
        map.put("global_notice", globalNotice != null ? globalNotice : "");
        map.put("updated_at", updatedAt);

        List<Map<String, Object>> lineMaps = new ArrayList<Map<String, Object>>();
        if (lines != null) {
            for (ProductLine pl : lines) {
                lineMaps.add(pl.toMap());
            }
        }
        map.put("lines", lineMaps);

        List<Map<String, Object>> keyMaps = new ArrayList<Map<String, Object>>();
        if (keys != null) {
            for (KeyItem k : keys) {
                keyMaps.add(k.toMap());
            }
        }
        map.put("keys", keyMaps);

        return map;
    }

    @SuppressWarnings("unchecked")
    public static LicenseRoot fromMap(Map<String, Object> map) {
        LicenseRoot root = new LicenseRoot();
        if (map == null) return root;

        if (map.containsKey("version")) root.setVersion(String.valueOf(map.get("version")));
        if (map.containsKey("master_enabled")) {
            Object v = map.get("master_enabled");
            if (v instanceof Boolean) root.setMasterEnabled((Boolean) v);
            else root.setMasterEnabled(Boolean.parseBoolean(String.valueOf(v)));
        }
        if (map.containsKey("server_check_enabled")) {
            Object v = map.get("server_check_enabled");
            if (v instanceof Boolean) root.setServerCheckEnabled((Boolean) v);
            else root.setServerCheckEnabled(Boolean.parseBoolean(String.valueOf(v)));
        }
        if (map.containsKey("global_notice")) root.setGlobalNotice(String.valueOf(map.get("global_notice")));
        if (map.containsKey("updated_at")) {
            Object v = map.get("updated_at");
            if (v instanceof Number) root.setUpdatedAt(((Number) v).longValue());
        }

        if (map.containsKey("lines")) {
            Object linesObj = map.get("lines");
            if (linesObj instanceof List) {
                List<ProductLine> list = new ArrayList<ProductLine>();
                for (Object item : (List<?>) linesObj) {
                    if (item instanceof Map) {
                        list.add(ProductLine.fromMap((Map<String, Object>) item));
                    }
                }
                root.setLines(list);
            }
        }

        if (map.containsKey("keys")) {
            Object keysObj = map.get("keys");
            if (keysObj instanceof List) {
                List<KeyItem> list = new ArrayList<KeyItem>();
                for (Object item : (List<?>) keysObj) {
                    if (item instanceof Map) {
                        list.add(KeyItem.fromMap((Map<String, Object>) item));
                    }
                }
                root.setKeys(list);
            }
        }

        return root;
    }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public boolean isMasterEnabled() { return masterEnabled; }
    public void setMasterEnabled(boolean masterEnabled) { this.masterEnabled = masterEnabled; }

    public boolean isServerCheckEnabled() { return serverCheckEnabled; }
    public void setServerCheckEnabled(boolean serverCheckEnabled) { this.serverCheckEnabled = serverCheckEnabled; }

    public String getGlobalNotice() { return globalNotice; }
    public void setGlobalNotice(String globalNotice) { this.globalNotice = globalNotice; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public List<ProductLine> getLines() { return lines; }
    public void setLines(List<ProductLine> lines) { this.lines = lines; }

    public List<KeyItem> getKeys() { return keys; }
    public void setKeys(List<KeyItem> keys) { this.keys = keys; }
}
