package rank;

import zabstracts.AbsRanked;
import template.InfoMemList;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * TopHang — Bảng xếp hạng Hang Động (top point_hang_dong).
 */
public class TopHang extends AbsRanked {

    private static TopHang instance;

    public static TopHang gI() {
        if (instance == null) instance = new TopHang();
        return instance;
    }

    public static final List<InfoMemList> CACHE = new ArrayList<>();
    public static final int TYPE = 10;
    public static final int MAX_ITEMS = 5;

    private TopHang() {
        super("Top Hang Động", TYPE, MAX_ITEMS);
        this.key = "TOP_HANG";
    }

    @Override
    protected boolean shouldSort() {
        return true;
    }

    @Override
    public String getSql() {
        return "SELECT `id`, `name`, `body_parts`, `it_body`, `fashion`, `inventory` " +
               "FROM `players`";
    }

    @Override
    public InfoMemList buildEntry(ResultSet rs) throws Exception {
        String invStr = rs.getString("inventory");
        if (invStr == null || invStr.isEmpty()) return null;
        long score = 0;
        try {
            org.json.simple.JSONObject inv = (org.json.simple.JSONObject) org.json.simple.JSONValue.parse(invStr);
            if (inv != null) {
                if (inv.containsKey("point_hang_dong")) {
                    score = Long.parseLong(inv.get("point_hang_dong").toString());
                } else if (inv.containsKey("hang")) {
                    score = Long.parseLong(inv.get("hang").toString());
                }
            }
        } catch (Exception ignored) {}

        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = "Hạng: " + e.thongthao;
        return e;
    }

    @Override
    public List<InfoMemList> getCache() { return CACHE; }

    // ======================================================
    //  REWARD — phần thưởng theo rank
    // ======================================================

    /**
     * Phần thưởng Hang Động (top 5):
     *   Top 1 : 20M Beri + 20,000 Ruby + 20 Búa Đục Dial (id 457)
     *   Top 2 : 12M Beri + 12,000 Ruby + 12 Búa Đục Dial (id 457)
     *   Top 3 : 8M Beri + 8,000 Ruby + 8 Búa Đục Dial (id 457)
     *   Top 4-5: 5M Beri + 5,000 Ruby + 5 Búa Đục Dial (id 457)
     *   Ngoài top 5: không có quà.
     */
    @Override
    public java.util.List<template.GiftBox> getRewards(int rank) {
        if (rank < 0 || rank >= MAX_ITEMS) return java.util.Collections.emptyList();
        int beri, ruby, bua;
        switch (rank) {
            case 0: beri = 20_000_000; ruby = 20_000; bua = 20; break;
            case 1: beri = 12_000_000; ruby = 12_000; bua = 12; break;
            case 2: beri = 8_000_000; ruby = 8_000; bua = 8; break;
            default: beri = 5_000_000; ruby = 5_000; bua = 5; break;
        }
        java.util.List<template.GiftBox> gifts = new java.util.ArrayList<>();
        gifts.add(new template.GiftBox(4, 0, beri));
        gifts.add(new template.GiftBox(4, 1, ruby));
        gifts.add(new template.GiftBox(4, 457, bua));
        return gifts;
    }
}
