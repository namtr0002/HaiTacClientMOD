package rank;

import zabstracts.AbsRanked;
import template.InfoMemList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopBoSenHong — BXH Bó Sen Hồng Sự Kiện 20 Tháng 11 (Event ID 14).
 * typeBXH = 1402.
 */
public class TopBoSenHong extends AbsRanked {

    private static TopBoSenHong instance;

    public static TopBoSenHong gI() {
        if (instance == null) instance = new TopBoSenHong();
        return instance;
    }

    public static final List<InfoMemList> CACHE    = new ArrayList<>();
    public static final int EVENT_SUBTYPE          = 1402;
    public static final int MAX_ITEMS              = 100;

    public TopBoSenHong() {
        super("Top Bó Sen Hồng", 7, MAX_ITEMS);
        this.key = "TOP_BO_SEN_HONG";
    }

    @Override
    public int getEventId() { return 14; }

    @Override
    public int getEventSubType() {
        return EVENT_SUBTYPE;
    }

    @Override
    public boolean isActive() {
        return event.EventManager.isActive(14);
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
        long score = getEventScore(rs.getString("event"), 14, "top_sen_hong", 21);
        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = String.format("Bó sen hồng: %s", core.ZUtil.number_format(e.thongthao));
        return e;
    }

    @Override
    public List<InfoMemList> getCache() {
        return CACHE;
    }

    @Override
    public List<template.GiftBox> getRewards(int rank) {
        if (rank < 0 || rank >= MAX_ITEMS) return Collections.emptyList();
        List<template.GiftBox> gifts = new ArrayList<>();
        template.GiftBox gb;
        switch (rank) {
            case 0:
                gb = createGift(158, 4, 2);     if (gb != null) gifts.add(gb);
                gb = createGift(866, 4, 20);    if (gb != null) gifts.add(gb);
                break;
            case 1:
                gb = createGift(158, 4, 1);     if (gb != null) gifts.add(gb);
                gb = createGift(866, 4, 10);    if (gb != null) gifts.add(gb);
                break;
            case 2:
                gb = createGift(158, 4, 1);     if (gb != null) gifts.add(gb);
                gb = createGift(866, 4, 5);     if (gb != null) gifts.add(gb);
                break;
            default:
                gb = createGift(866, 4, 2);     if (gb != null) gifts.add(gb);
                break;
        }
        return gifts;
    }
}
