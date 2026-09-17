package rank;

import zabstracts.AbsRanked;
import core.Manager;
import template.InfoMemList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopDotPhao — BXH Đốt Pháo (point_event1).
 * typeBXH = 1, active khi event 9 / 2 / 1.
 */
public class TopDotPhao extends AbsRanked {

    private static TopDotPhao instance;

    public static TopDotPhao gI() {
        if (instance == null) instance = new TopDotPhao();
        return instance;
    }

    public static final List<InfoMemList> CACHE    = new ArrayList<>();
    public static final int EVENT_SUBTYPE          = 201;
    public static final int MAX_ITEMS              = 100;

    public TopDotPhao() { super("Top Đốt Pháo", 7, MAX_ITEMS); this.key = "TOP_DOT_PHAO"; }

    @Override
    public int getEventId() { return 2; }

    @Override
    public int getEventSubType() { return EVENT_SUBTYPE; }
    @Override public boolean isActive() {
        return event.EventManager.isActive(2);
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
        long score = getEventScore(rs.getString("event"), 2, "top_dot_phao", 20);
        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = String.format("Đốt pháo: %s", core.ZUtil.number_format(e.thongthao));
        return e;
    }

    @Override
    public List<InfoMemList> getCache() { return CACHE; }

    // ======================================================
    //  REWARD — phần thưởng theo rank (migrate từ TraoQuaTop)
    // ======================================================

    /**
     * Phần thưởng Đốt Pháo:
     *   Top 1 : item 427 (x1) + item 1006 (x20) + item 1019 (x50) + 1,000,000 coin
     *   Top 2 : item 1006 (x10) + item 1019 (x20) + 500,000 coin
     *   Top 3 : item 1006 (x3)  + item 1019 (x10) + 200,000 coin
     *   Rank khác: item 1006 (x1) + item 1019 (x5) + 100,000 coin
     *
     * Note: coin được trao riêng qua p.update_coin() trong code gọi sendReward().
     */
    @Override
    public List<template.GiftBox> getRewards(int rank) {
        if (rank < 0 || rank >= MAX_ITEMS) return Collections.emptyList();
        List<template.GiftBox> gifts = new ArrayList<>();
        template.GiftBox gb;
        switch (rank) {
            case 0:
                gb = createGift(427, 4, 1);     if (gb != null) gifts.add(gb); // Trái bóng tối
                gb = createGift(866, 4, 20);    if (gb != null) gifts.add(gb); // Vé quay VIP
                gb = createGift(801, 4, 50);    if (gb != null) gifts.add(gb); // Vé quay thường
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
