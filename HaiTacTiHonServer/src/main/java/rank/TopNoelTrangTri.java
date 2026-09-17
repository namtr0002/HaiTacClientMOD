package rank;

import zabstracts.AbsRanked;
import event.SuKienNoel;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import template.InfoMemList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopNoelTrangTri — BXH Trang Trí Noel (event JSON id=12346, INDEX_DECOR).
 * typeBXH = 10, active khi Noel event đang chạy.
 */
public class TopNoelTrangTri extends AbsRanked {

    private static TopNoelTrangTri instance;

    public static TopNoelTrangTri gI() {
        if (instance == null) instance = new TopNoelTrangTri();
        return instance;
    }

    public static final List<InfoMemList> CACHE    = new ArrayList<>();
    public static final int EVENT_SUBTYPE          = 10;
    public static final int MAX_ITEMS              = 50;

    private TopNoelTrangTri() { super("Top trang trí", 7, MAX_ITEMS);
        this.key = "TOP_NOEL_TRANG_TRI"; }

    @Override
    public int getEventId() { return 12; }

    @Override
    public int getEventSubType() { return EVENT_SUBTYPE; }
    @Override public boolean isActive()     { return event.EventManager.isActive(12); }
    

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
        long score = getEventScore(rs.getString("event"), 12, "top_trang_tri", 20);
        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = String.format("Trang trí: %s", core.ZUtil.number_format(e.thongthao));
        return e;
    }

    @Override
    public List<InfoMemList> getCache() { return CACHE; }

    // ======================================================
    //  REWARD — phần thưởng theo rank
    // ======================================================

    /**
     * Phần thưởng Trang Trí Noel:
     *   Top 1 : 500M Beri + 100,000 Ruby
     *   Top 2 : 200M Beri + 50,000 Ruby
     *   Top 3 : 100M Beri + 20,000 Ruby
     *   Top khác: không có quà.
     */
    @Override
    public List<template.GiftBox> getRewards(int rank) {
        if (rank < 0 || rank > 2) return Collections.emptyList();
        return standardBeriRubyRewards(rank);
    }
}
