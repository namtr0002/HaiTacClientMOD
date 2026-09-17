package rank;

import zabstracts.AbsRanked;
import core.Manager;
import template.InfoMemList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopGhepHuyHieu — BXH Ghép Huy Hiệu (point_event2).
 * typeBXH = 2, active khi event 9 / 2 / 1.
 */
public class TopGhepHuyHieu extends AbsRanked {

    private static TopGhepHuyHieu instance;

    public static TopGhepHuyHieu gI() {
        if (instance == null) instance = new TopGhepHuyHieu();
        return instance;
    }

    public static final List<InfoMemList> CACHE    = new ArrayList<>();
    public static final int EVENT_SUBTYPE          = 2;
    public static final int MAX_ITEMS              = 100;

    public TopGhepHuyHieu() { super("Top Ghép Huy Hiệu", 7, MAX_ITEMS);
        this.key = "TOP_GHEP_HUY_HIEU"; }

    @Override public int getEventSubType() { return EVENT_SUBTYPE; }
    @Override public boolean isActive() {
        return event.EventManager.isActive(1) || event.EventManager.isActive(2) || event.EventManager.isActive(4) ||
               event.EventManager.isActive(5) || event.EventManager.isActive(6) || event.EventManager.isActive(8) ||
               event.EventManager.isActive(9) || event.EventManager.isActive(11) || event.EventManager.isActive(13) ||
               event.EventManager.isActive(14) || event.EventManager.isActive(15) || event.EventManager.isActive(17) ||
               event.EventManager.isActive(18) || event.EventManager.isActive(19);
    }

    @Override
    protected boolean shouldSort() {
        return true;
    }

    @Override
    public String getSql() {
        return "SELECT p.`id`, p.`name`, p.`body_parts`, p.`it_body`, p.`fashion`, p.`inventory`, h.`data` AS `event` " +
               "FROM `players` p " +
               "LEFT JOIN `historys` h ON p.`id` = h.`player_id` AND (h.`type` = 'EVENT_DATA' OR h.`type` LIKE 'EVENT_DATA%')";
    }

    @Override
    public InfoMemList buildEntry(ResultSet rs) throws Exception {
        int activeId = event.EventManager.activeId();
        long score = getEventScore(rs.getString("event"), activeId, "top_ghep_huy_hieu", 21);
        if (score <= 0) return null;

        InfoMemList e    = new InfoMemList();
        e.id             = rs.getInt("id");
        e.name           = rs.getString("name");
        e.thongthao      = score;
        short[] app      = readAppearance(rs);
        e.head = app[0]; e.hair = app[1]; e.hat = app[2];
        e.info           = String.format("Điểm: %s", core.ZUtil.number_format(e.thongthao));
        return e;
    }

    @Override public List<InfoMemList> getCache() { return CACHE; }

    // ======================================================
    //  REWARD — phần thưởng theo rank (migrate từ TraoQuaTop)
    // ======================================================

    /**
     * Phần thưởng Ghép Huy Hiệu:
     *   Top 1 : item 427 (x9999) + item 866 (x9999) + item 191 (x9999) + 1,000,000 gcoin
     *   Top 2 : item 1006 (x10) + item 1019 (x20) + 500,000 coin
     *   Top 3 : item 1006 (x3) + item 1019 (x10) + 200,000 coin
     *   Rank khác: item 1006 (x1) + item 1019 (x5) + 100,000 coin
     *
     * Note: gcoin và coin được trao riêng qua sendReward() bằng update_gcoin/update_coin.
     * GiftBox ở đây chỉ là items, tiền xuất hiện qua p.update_coin() trong code gọi.
     */
    @Override
    public List<template.GiftBox> getRewards(int rank) {
        if (rank < 0 || rank >= MAX_ITEMS) return Collections.emptyList();
        List<template.GiftBox> gifts = new ArrayList<>();
        template.GiftBox gb;
        switch (rank) {
            case 0:
                gb = createGift(427, 4, 9999);  if (gb != null) gifts.add(gb); // Trái bóng tối
                gb = createGift(866, 4, 9999);  if (gb != null) gifts.add(gb); // Vé quay VIP
                gb = createGift(191, 4, 9999);  if (gb != null) gifts.add(gb); // Rương hành trình / Đổi trang bị
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
