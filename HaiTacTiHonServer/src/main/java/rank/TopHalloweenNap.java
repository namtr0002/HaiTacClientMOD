package rank;

import event.EventManager;

import zabstracts.AbsRanked;
import event.SuKienHalloween;
import template.InfoMemList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopHalloweenNap — BXH Nạp Halloween (cột napHlw).
 * typeBXH = 7, active khi Halloween event đang chạy.
 */
public class TopHalloweenNap extends AbsRanked {

    private static TopHalloweenNap instance;

    public static TopHalloweenNap gI() {
        if (instance == null) instance = new TopHalloweenNap();
        return instance;
    }

    public static final List<InfoMemList> CACHE    = new ArrayList<>();
    public static final int EVENT_SUBTYPE          = 7;
    public static final int MAX_ITEMS              = 50;

    private TopHalloweenNap() { super("Top nạp Halloween", 7, MAX_ITEMS);
        this.key = "TOP_HALLOWEEN_NAP"; }

    @Override
    public int getEventId() { return 7; }

    @Override
    public int getEventSubType() { return EVENT_SUBTYPE; }
    @Override public boolean isActive() {
        return event.EventManager.isActive(7);
    }
    

    private boolean hasEvent(String eventJson, int eventId) {
        if (eventJson == null || eventJson.isEmpty() || eventJson.equals("[]")) return false;
        try {
            org.json.simple.JSONArray js12 = (org.json.simple.JSONArray) org.json.simple.JSONValue.parse(eventJson);
            if (js12 == null) return false;
            for (int i = 0; i < js12.size(); i++) {
                Object elem = js12.get(i);
                if (elem instanceof org.json.simple.JSONObject) {
                    org.json.simple.JSONObject eo = (org.json.simple.JSONObject) elem;
                    if (eo.get("ev") != null && Integer.parseInt(eo.get("ev").toString()) == eventId) {
                        return true;
                    }
                } else if (elem instanceof org.json.simple.JSONArray) {
                    org.json.simple.JSONArray js_in = (org.json.simple.JSONArray) elem;
                    if (!js_in.isEmpty() && Integer.parseInt(js_in.get(0).toString()) == eventId) {
                        return true;
                    }
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    @Override
    protected boolean shouldSort() {
        return true;
    }

    @Override
    public String getSql() {
        return buildEventSql(getEventId());
    }

    @Override
    public InfoMemList buildEntry(ResultSet rs) throws Exception {
        long score = getEventScore(rs.getString("event"), 7, "top_nap", 13);
        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = String.format("Nạp sự kiện: %s", core.ZUtil.number_format(e.thongthao));
        return e;
    }

    @Override
    public List<InfoMemList> getCache() { return CACHE; }

    // ======================================================
    //  REWARD — phần thưởng theo rank (migrate từ TraoQuaTop "TOP NẠP")
    // ======================================================

    /**
     * Phần thưởng Nạp Halloween:
     *   Top 1 : item 427 (x9999)
     *   Top 2 : item 1006 (x10) + item 1019 (x20) + 1,000,000 coin
     *   Top 3 : item 1006 (x3)  + item 1019 (x10) + 500,000 coin
     *   Rank khác: item 1006 (x1) + item 1019 (x5) + 200,000 coin
     */
    @Override
    public List<template.GiftBox> getRewards(int rank) {
        if (rank < 0 || rank >= MAX_ITEMS) return Collections.emptyList();
        List<template.GiftBox> gifts = new ArrayList<>();
        template.GiftBox gb;
        switch (rank) {
            case 0:
                gb = createGift(427, 4, 9999);  if (gb != null) gifts.add(gb); // Trái bóng tối
                break;
            case 1:
                gb = createGift(866, 4, 10);    if (gb != null) gifts.add(gb); // Vé quay VIP
                gb = createGift(801, 4, 20);    if (gb != null) gifts.add(gb); // Vé quay thường
                break;
            case 2:
                gb = createGift(866, 4, 3);     if (gb != null) gifts.add(gb); // Vé quay VIP
                gb = createGift(801, 4, 10);    if (gb != null) gifts.add(gb); // Vé quay thường
                break;
            default:
                gb = createGift(866, 4, 1);     if (gb != null) gifts.add(gb); // Vé quay VIP
                gb = createGift(801, 4, 5);     if (gb != null) gifts.add(gb); // Vé quay thường
                break;
        }
        return gifts;
    }
}
