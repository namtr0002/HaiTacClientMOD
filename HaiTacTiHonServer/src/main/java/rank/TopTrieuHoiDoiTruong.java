package rank;

import zabstracts.AbsRanked;
import template.InfoMemList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopTrieuHoiDoiTruong — BXH Triệu Hồi Đội Trưởng Sự Kiện Đấu Trường Rực Lửa (Event ID 13).
 * typeBXH = 1301.
 */
public class TopTrieuHoiDoiTruong extends AbsRanked {

    private static TopTrieuHoiDoiTruong instance;

    public static TopTrieuHoiDoiTruong gI() {
        if (instance == null) instance = new TopTrieuHoiDoiTruong();
        return instance;
    }

    public static final List<InfoMemList> CACHE    = new ArrayList<>();
    public static final int EVENT_SUBTYPE          = 1301;
    public static final int MAX_ITEMS              = 100;

    public TopTrieuHoiDoiTruong() {
        super("Top Làm Vật Phẩm", 7, MAX_ITEMS);
        this.key = "TOP_LAM_VAT_PHAM_WORLDCUP";
    }

    @Override
    public int getEventId() { return 13; }

    @Override
    public int getEventSubType() {
        return EVENT_SUBTYPE;
    }

    @Override
    public boolean isActive() {
        return event.EventManager.isActive(13);
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
        long score = getEventScore(rs.getString("event"), 13, "top_trieu_hoi", 20);
        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = String.format("Làm: %s", core.ZUtil.number_format(e.thongthao));
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
