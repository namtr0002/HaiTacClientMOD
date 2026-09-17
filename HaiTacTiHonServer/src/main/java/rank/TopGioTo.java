package rank;

import event.EventManager;

import zabstracts.AbsRanked;
import core.Manager;
import template.InfoMemList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopGioTo — BXH Sử Dụng Mâm (point_event1, top 10, min > 100).
 * typeBXH = 13, active khi event == 11.
 */
public class TopGioTo extends AbsRanked {

    private static TopGioTo instance;

    public static TopGioTo gI() {
        if (instance == null) instance = new TopGioTo();
        return instance;
    }

    public static final List<InfoMemList> CACHE    = new ArrayList<>();
    public static final int EVENT_SUBTYPE          = 3;
    public static final int MAX_ITEMS              = 10;

    private TopGioTo() { super("Top Sử Dụng Mâm", 7, MAX_ITEMS);
        this.key = "TOP_GIO_TO"; }

    @Override
    public int getEventId() { return 3; }

    @Override
    public int getEventSubType() { return EVENT_SUBTYPE; }
    @Override public boolean isActive()     { return event.EventManager.isActive(3); }

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
        long score = getEventScore(rs.getString("event"), 3, "top_gio_to", 20);
        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = String.format("Dâng mâm: %s", core.ZUtil.number_format(e.thongthao));
        return e;
    }

    @Override
    public List<InfoMemList> getCache() {
        return CACHE;
    }

    // ======================================================
    //  REWARD — phần thưởng theo rank
    // ======================================================

    /**
     * Phần thưởng Sử Dụng Mâm (top 10):
     *   Top 1 : 500M Beri + 100,000 Ruby
     *   Top 2 : 200M Beri + 50,000 Ruby
     *   Top 3 : 100M Beri + 20,000 Ruby
     *   Top 4-10: không có quà.
     */
    @Override
    public List<template.GiftBox> getRewards(int rank) {
        if (rank < 0 || rank > 2) return Collections.emptyList();
        return standardBeriRubyRewards(rank);
    }
}
