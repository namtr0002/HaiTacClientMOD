package com.haitac.manager.model;

import java.util.*;

/**
 * Model đại diện cho một Dòng phiên bản / Sản phẩm MOD
 */
public class ProductLine {

    private String lineId;
    private String lineName;
    private boolean enabled;
    private boolean requireKey;
    private List<String> freeIps;
    private String notice;

    public ProductLine() {
        this.lineId = "MOD_UNITY";
        this.lineName = "Bản MOD Unity (PC & Mobile)";
        this.enabled = true;
        this.requireKey = true;
        this.freeIps = new ArrayList<String>(Collections.singletonList("*"));
        this.notice = "";
    }

    public ProductLine(String lineId, String lineName, boolean enabled, boolean requireKey) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.enabled = enabled;
        this.requireKey = requireKey;
        this.freeIps = new ArrayList<String>(Collections.singletonList("*"));
        this.notice = "";
    }

    public String getFreeIpsStr() {
        if (freeIps == null || freeIps.isEmpty()) return "*";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < freeIps.size(); i++) {
            sb.append(freeIps.get(i));
            if (i < freeIps.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public void setFreeIpsFromStr(String ipsStr) {
        freeIps = new ArrayList<String>();
        if (ipsStr == null || ipsStr.trim().isEmpty() || "*".equals(ipsStr.trim())) {
            freeIps.add("*");
            return;
        }
        String[] parts = ipsStr.split("[,; ]+");
        for (String p : parts) {
            p = p.trim();
            if (!p.isEmpty()) {
                freeIps.add(p);
            }
        }
        if (freeIps.isEmpty()) freeIps.add("*");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("line_id", lineId);
        map.put("line_name", lineName);
        map.put("enabled", enabled);
        map.put("require_key", requireKey);
        map.put("free_ips", freeIps != null ? freeIps : Collections.singletonList("*"));
        map.put("notice", notice != null ? notice : "");
        return map;
    }

    @SuppressWarnings("unchecked")
    public static ProductLine fromMap(Map<String, Object> map) {
        ProductLine line = new ProductLine();
        if (map == null) return line;

        if (map.containsKey("line_id")) line.setLineId(String.valueOf(map.get("line_id")));
        if (map.containsKey("line_name")) line.setLineName(String.valueOf(map.get("line_name")));
        if (map.containsKey("enabled")) {
            Object v = map.get("enabled");
            if (v instanceof Boolean) line.setEnabled((Boolean) v);
            else line.setEnabled(Boolean.parseBoolean(String.valueOf(v)));
        }
        if (map.containsKey("require_key")) {
            Object v = map.get("require_key");
            if (v instanceof Boolean) line.setRequireKey((Boolean) v);
            else line.setRequireKey(Boolean.parseBoolean(String.valueOf(v)));
        }
        if (map.containsKey("free_ips")) {
            Object v = map.get("free_ips");
            if (v instanceof List) {
                List<String> list = new ArrayList<String>();
                for (Object o : (List<?>) v) {
                    if (o != null) list.add(o.toString());
                }
                line.setFreeIps(list);
            }
        }
        if (map.containsKey("notice")) line.setNotice(String.valueOf(map.get("notice")));
        return line;
    }

    @Override
    public String toString() {
        return lineName + " (" + lineId + ")";
    }

    public String getLineId() { return lineId; }
    public void setLineId(String lineId) { this.lineId = lineId; }

    public String getLineName() { return lineName; }
    public void setLineName(String lineName) { this.lineName = lineName; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public boolean isRequireKey() { return requireKey; }
    public void setRequireKey(boolean requireKey) { this.requireKey = requireKey; }

    public List<String> getFreeIps() { return freeIps; }
    public void setFreeIps(List<String> freeIps) { this.freeIps = freeIps; }

    public String getNotice() { return notice; }
    public void setNotice(String notice) { this.notice = notice; }
}
