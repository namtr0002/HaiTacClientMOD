package rank;

import zabstracts.AbsRanked;
import template.InfoMemList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopHopKimCuongDo — BXH Hộp Kim Cương Đỏ Sự Kiện 30 Tháng 4 (Event ID 6).
 * typeBXH = 601.
 */
public class TopHopKimCuongDo extends AbsRanked {

    private static TopHopKimCuongDo instance;

    public static TopHopKimCuongDo gI() {
        if (instance == null) instance = new TopHopKimCuongDo();
        return instance;
    }

    public static final List<InfoMemList> CACHE    = new ArrayList<>();
    public static final int EVENT_SUBTYPE          = 601;
    public static final int MAX_ITEMS              = 100;

    public TopHopKimCuongDo() {
        super("Top Hộp Kim Cương Đỏ", 7, MAX_ITEMS);
        this.key = "TOP_HOP_KIM_CUONG_DO";
    }

    @Override
    public int getEventId() { return 6; }

    @Override
    public int getEventSubType() {
        return EVENT_SUBTYPE;
    }

    @Override
    public boolean isActive() {
        return event.EventManager.isActive(6);
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
        long score = getEventScore(rs.getString("event"), 6, "top_kc_do", 20);
        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = String.format("Hộp KC Đỏ: %s", core.ZUtil.number_format(e.thongthao));
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
