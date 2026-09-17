package rank;

import event.EventManager;

import zabstracts.AbsRanked;
import core.Manager;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import template.InfoMemList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopDungBanh — BXH Dùng Bánh (event JSON typeEvent=3, index=4).
 * typeBXH = 3, active khi event == 3.
 */
public class TopDungBanh extends AbsRanked {

    private static TopDungBanh instance;

    public static TopDungBanh gI() {
        if (instance == null) instance = new TopDungBanh();
        return instance;
    }

    public static final List<InfoMemList> CACHE    = new ArrayList<>();
    public static final int EVENT_SUBTYPE          = 8;
    public static final int MAX_ITEMS              = 50;

    private TopDungBanh() { super("Top Dùng Bánh Trung Thu", 7, MAX_ITEMS);
        this.key = "TOP_DUNG_BANH"; }

    @Override
    public int getEventId() { return 8; }

    @Override
    public int getEventSubType() { return EVENT_SUBTYPE; }
    @Override public boolean isActive()     { return event.EventManager.isActive(8); }
    

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
        long score = getEventScore(rs.getString("event"), 8, "top_dung_banh", 22);
        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = String.format("Dựng bánh: %s", core.ZUtil.number_format(e.thongthao));
        return e;
    }

    @Override
    public List<InfoMemList> getCache() { return CACHE; }

    // ======================================================
    //  REWARD — phần thưởng theo rank
    // ======================================================

    /**
     * Phần thưởng Dùng Bánh (top 3):
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
