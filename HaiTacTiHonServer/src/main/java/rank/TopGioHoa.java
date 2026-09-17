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
 * TopGioHoa — BXH Giỏ Hoa (point_event1, top 10, min > 100).
 * typeBXH = 11, active khi event == 8.
 */
public class TopGioHoa extends AbsRanked {

    private static TopGioHoa instance;

    public static TopGioHoa gI() {
        if (instance == null) instance = new TopGioHoa();
        return instance;
    }

    public static final List<InfoMemList> CACHE    = new ArrayList<>();
    public static final int EVENT_SUBTYPE          = 11;
    public static final int MAX_ITEMS              = 10;

    private TopGioHoa() { super("Top Giỏ Hoa", 7, MAX_ITEMS); this.key = "TOP_GIO_HOA"; }

    @Override
    public int getEventId() { return 10; }

    @Override
    public int getEventSubType() { return EVENT_SUBTYPE; }
    @Override public boolean isActive()     { return event.EventManager.isActive(10) || event.EventManager.isActive(11); }

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
        long score = getEventScore(rs.getString("event"), 10, "top_gio_hoa", 20);
        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = String.format("Giỏ hoa: %s", core.ZUtil.number_format(e.thongthao));
        return e;
    }

    @Override
    public List<InfoMemList> getCache() { return CACHE; }

    // ======================================================
    //  REWARD — phần thưởng theo rank
    // ======================================================

    /**
     * Phần thưởng Giỏ Hoa (top 10):
     *   Top 1 : 500M Beri + 100,000 Ruby
     *   Top 2 : 200M Beri + 50,000 Ruby
     *   Top 3 : 100M Beri + 20,000 Ruby
     *   Top 4-10: 50M Beri + 10,000 Ruby
     */
    @Override
    public List<template.GiftBox> getRewards(int rank) {
        if (rank < 0 || rank >= MAX_ITEMS) return Collections.emptyList();
        return standardBeriRubyRewards(rank);
    }
}
