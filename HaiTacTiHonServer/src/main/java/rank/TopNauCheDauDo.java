package rank;

import zabstracts.AbsRanked;
import template.InfoMemList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopNauCheDauDo — BXH Nấu Chè Đậu Đỏ Sự Kiện Thất Tịch (Event ID 19).
 * typeBXH = 1901.
 */
public class TopNauCheDauDo extends AbsRanked {

    private static TopNauCheDauDo instance;

    public static TopNauCheDauDo gI() {
        if (instance == null) instance = new TopNauCheDauDo();
        return instance;
    }

    public static final List<InfoMemList> CACHE    = new ArrayList<>();
    public static final int EVENT_SUBTYPE          = 1901;
    public static final int MAX_ITEMS              = 100;

    public TopNauCheDauDo() {
        super("Top Nấu Chè Đậu Đỏ", 7, MAX_ITEMS);
        this.key = "TOP_NAU_CHE_DAU_DO";
    }

    @Override
    public int getEventId() { return 19; }

    @Override
    public int getEventSubType() {
        return EVENT_SUBTYPE;
    }

    @Override
    public boolean isActive() {
        return event.EventManager.isActive(19);
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
        long score = getEventScore(rs.getString("event"), 19, "top_che_dau_do", 20);
        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = String.format("Nấu chè đậu đỏ: %s", core.ZUtil.number_format(e.thongthao));
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
