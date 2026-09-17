package com.haitac.manager.model;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Model biểu diễn một License Key cho từng Client
 */
public class KeyItem {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_LOCKED = "LOCKED";
    public static final String STATUS_EXPIRED = "EXPIRED";

    private String key;
    private String lineId;
    private String customer;
    private String note;
    private String status; // ACTIVE, LOCKED
    private long createdAt;
    private long expireAt; // -1 = Lifetime (Vĩnh viễn)
    private List<String> allowedIps;
    private String hwid;
    private List<String> servers; // Danh sách server riêng cho client này (nếu có)

    public KeyItem() {
        this.key = "";
        this.lineId = "MOD_UNITY";
        this.customer = "";
        this.note = "";
        this.status = STATUS_ACTIVE;
        this.createdAt = System.currentTimeMillis();
        this.expireAt = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000); // Mặc định 30 ngày
        this.allowedIps = new ArrayList<String>(Collections.singletonList("*"));
        this.hwid = "";
        this.servers = new ArrayList<String>();
    }

    public boolean isLifetime() {
        return expireAt <= 0;
    }

    public boolean isExpired() {
        if (isLifetime()) return false;
        return System.currentTimeMillis() > expireAt;
    }

    public String getEffectiveStatus() {
        if (STATUS_LOCKED.equalsIgnoreCase(status)) {
            return STATUS_LOCKED;
        }
        if (isExpired()) {
            return STATUS_EXPIRED;
        }
        return STATUS_ACTIVE;
    }

    public String getRemainingTimeStr() {
        if (STATUS_LOCKED.equalsIgnoreCase(status)) {
            return "ĐÃ KHÓA";
        }
        if (isLifetime()) {
            return "VĨNH VIỄN";
        }
        long diff = expireAt - System.currentTimeMillis();
        if (diff <= 0) {
            return "Đã hết hạn";
        }
        long days = diff / (24L * 60 * 60 * 1000);
        long hours = (diff % (24L * 60 * 60 * 1000)) / (60L * 60 * 1000);
        if (days > 0) {
            return days + " ngày " + hours + " giờ";
        }
        long minutes = (diff % (60L * 60 * 1000)) / (60L * 1000);
        return hours + " giờ " + minutes + " phút";
    }

    public String getExpireDateStr() {
        if (isLifetime()) return "Vĩnh viễn";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(expireAt));
    }

    public String getCreatedDateStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(createdAt));
    }

    public String getAllowedIpsStr() {
        if (allowedIps == null || allowedIps.isEmpty()) return "*";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allowedIps.size(); i++) {
            sb.append(allowedIps.get(i));
            if (i < allowedIps.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public void setAllowedIpsFromStr(String ipsStr) {
        allowedIps = new ArrayList<String>();
        if (ipsStr == null || ipsStr.trim().isEmpty() || "*".equals(ipsStr.trim())) {
            allowedIps.add("*");
            return;
        }
        String[] parts = ipsStr.split("[,; ]+");
        for (String p : parts) {
            p = p.trim();
            if (!p.isEmpty()) {
                allowedIps.add(p);
            }
        }
        if (allowedIps.isEmpty()) allowedIps.add("*");
    }

    public String getServersStr() {
        if (servers == null || servers.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < servers.size(); i++) {
            sb.append(servers.get(i));
            if (i < servers.size() - 1) sb.append("\n");
        }
        return sb.toString();
    }

    public void setServersFromStr(String str) {
        servers = new ArrayList<String>();
        if (str == null || str.trim().isEmpty()) return;
        String[] lines = str.split("\r?\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                servers.add(line);
            }
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("key", key != null ? key : "");
        map.put("line_id", lineId != null ? lineId : "MOD_UNITY");
        map.put("customer", customer != null ? customer : "");
        map.put("note", note != null ? note : "");
        map.put("status", status != null ? status : STATUS_ACTIVE);
        map.put("created_at", createdAt);
        map.put("expire_at", expireAt);
        map.put("allowed_ips", allowedIps != null ? allowedIps : Collections.singletonList("*"));
        map.put("hwid", hwid != null ? hwid : "");
        if (servers != null && !servers.isEmpty()) {
            map.put("servers", servers);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static KeyItem fromMap(Map<String, Object> map) {
        KeyItem item = new KeyItem();
        if (map == null) return item;

        if (map.containsKey("key")) item.setKey(String.valueOf(map.get("key")));
        if (map.containsKey("line_id")) item.setLineId(String.valueOf(map.get("line_id")));
        if (map.containsKey("customer")) item.setCustomer(String.valueOf(map.get("customer")));
        if (map.containsKey("note")) item.setNote(String.valueOf(map.get("note")));
        if (map.containsKey("status")) item.setStatus(String.valueOf(map.get("status")));
        if (map.containsKey("created_at")) {
            Object v = map.get("created_at");
            if (v instanceof Number) item.setCreatedAt(((Number) v).longValue());
        }
        if (map.containsKey("expire_at")) {
            Object v = map.get("expire_at");
            if (v instanceof Number) item.setExpireAt(((Number) v).longValue());
        }
        if (map.containsKey("allowed_ips")) {
            Object v = map.get("allowed_ips");
            if (v instanceof List) {
                List<String> list = new ArrayList<String>();
                for (Object o : (List<?>) v) {
                    if (o != null) list.add(o.toString());
                }
                item.setAllowedIps(list);
            }
        }
        if (map.containsKey("hwid")) item.setHwid(String.valueOf(map.get("hwid")));
        if (map.containsKey("servers")) {
            Object v = map.get("servers");
            if (v instanceof List) {
                List<String> list = new ArrayList<String>();
                for (Object o : (List<?>) v) {
                    if (o != null) list.add(o.toString());
                }
                item.setServers(list);
            }
        }
        return item;
    }

    // Getters and Setters
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getLineId() { return lineId; }
    public void setLineId(String lineId) { this.lineId = lineId; }

    public String getCustomer() { return customer; }
    public void setCustomer(String customer) { this.customer = customer; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getExpireAt() { return expireAt; }
    public void setExpireAt(long expireAt) { this.expireAt = expireAt; }

    public List<String> getAllowedIps() { return allowedIps; }
    public void setAllowedIps(List<String> allowedIps) { this.allowedIps = allowedIps; }

    public String getHwid() { return hwid; }
    public void setHwid(String hwid) { this.hwid = hwid; }

    public List<String> getServers() { return servers; }
    public void setServers(List<String> servers) { this.servers = servers; }
}
