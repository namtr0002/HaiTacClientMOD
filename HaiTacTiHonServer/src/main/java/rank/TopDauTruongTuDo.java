package rank;

import model.Player;
import zabstracts.AbsRanked;
import template.GiftBox;
import template.InfoMemList;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopDauTruongTuDo — BXH Đấu Trường Tự Do.
 * Tách riêng thành class độc lập trong package rank.
 */
public class TopDauTruongTuDo extends AbsRanked {

    private static TopDauTruongTuDo instance;

    public static TopDauTruongTuDo gI() {
        if (instance == null) instance = new TopDauTruongTuDo();
        return instance;
    }

    public static final List<InfoMemList> CACHE = new ArrayList<>();
    public static final int TYPE = 15;
    public static final int MAX_ITEMS = 20;

    private TopDauTruongTuDo() {
        super("BXH Đấu Trường", TYPE, MAX_ITEMS);
        this.key = "TOP_DAU_TRUONG_TU_DO";
    }

    @Override
    public String getSql() {
        return "";
    }

    @Override
    public InfoMemList buildEntry(ResultSet rs) throws Exception {
        return null;
    }

    @Override
    public void update() {
        if (map.zones.DauTruongTuDo.bxh != null) {
            CACHE.clear();
            CACHE.addAll(map.zones.DauTruongTuDo.bxh);
        }
    }

    @Override
    public List<InfoMemList> getCache() {
        if (map.zones.DauTruongTuDo.bxh != null && !map.zones.DauTruongTuDo.bxh.isEmpty()) {
            return map.zones.DauTruongTuDo.bxh;
        }
        return CACHE;
    }

    @Override
    public void show(Player p, int page) throws IOException {
        super.show(p, page);
    }

    @Override
    public List<GiftBox> getRewards(int rank) {
        List<GiftBox> gifts = new ArrayList<>();
        int beri = 0, ruby = 0, ticket = 0, boxId = 0, boxCount = 0;
        switch (rank) {
            case 0:
                beri = 2_000_000; ruby = 150; ticket = 3; boxId = 106; boxCount = 2;
                break;
            case 1:
                beri = 1_000_000; ruby = 100; ticket = 2; boxId = 106; boxCount = 1;
                break;
            case 2:
                beri = 500_000; ruby = 50; ticket = 1; boxId = 19; boxCount = 1;
                break;
            case 3: case 4: case 5:
                beri = 300_000; ruby = 30; ticket = 1;
                break;
            default:
                if (rank < 10) {
                    beri = 150_000; ruby = 15;
                }
                break;
        }
        if (beri > 0) gifts.add(createGift(0, 4, beri));
        if (ruby > 0) gifts.add(createGift(1, 4, ruby));
        if (ticket > 0) gifts.add(createGift(30, 4, ticket));
        if (boxId > 0 && boxCount > 0) gifts.add(createGift(boxId, 4, boxCount));
        return gifts;
    }
}
