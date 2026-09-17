package rank;

import map.zones.TranChienLon;
import model.Player;
import zabstracts.AbsRanked;
import template.GiftBox;
import template.InfoMemList;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TopWorldWar — BXH Đại Chiến Thế Giới (Trận Chiến Lớn).
 * Tách riêng thành class độc lập trong package rank.
 */
public class TopWorldWar extends AbsRanked {

    private static TopWorldWar instance;

    public static TopWorldWar gI() {
        if (instance == null) instance = new TopWorldWar();
        return instance;
    }

    public static final List<InfoMemList> CACHE = new ArrayList<>();
    public static final int TYPE = 17;
    public static final int MAX_ITEMS = 10;

    private TopWorldWar() {
        super("Đại Chiến Thế Giới", TYPE, MAX_ITEMS);
        this.key = "TOP_WORLD_WAR";
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
        List<InfoMemList> list = new ArrayList<>();
        if (TranChienLon.top3 != null && !TranChienLon.top3.isEmpty()) {
            int rank = 0;
            for (Map.Entry<String, Integer> entry : TranChienLon.top3.entrySet()) {
                if (entry.getKey() == null) continue;
                InfoMemList e = new InfoMemList();
                e.name = entry.getKey();
                e.thongthao = entry.getValue();
                e.rank = (short) rank;
                e.info = "Điểm chiến trường: " + entry.getValue();
                list.add(e);
                rank++;
                if (rank >= MAX_ITEMS) break;
            }
        }
        CACHE.clear();
        CACHE.addAll(list);
    }

    @Override
    public List<InfoMemList> getCache() {
        if (!CACHE.isEmpty()) return CACHE;
        update();
        return CACHE;
    }

    @Override
    public void show(Player p, int page) throws IOException {
        super.show(p, page);
    }

    @Override
    public List<GiftBox> getRewards(int rank) {
        List<GiftBox> gifts = new ArrayList<>();
        int beri = 0, ruby = 0, boxVang = 0;
        switch (rank) {
            case 0:
                beri = 5_000_000; ruby = 500; boxVang = 3;
                break;
            case 1:
                beri = 3_000_000; ruby = 300; boxVang = 2;
                break;
            case 2:
                beri = 1_500_000; ruby = 150; boxVang = 1;
                break;
            default:
                if (rank < 10) {
                    beri = 500_000; ruby = 50;
                }
                break;
        }
        if (beri > 0) gifts.add(createGift(0, 4, beri));
        if (ruby > 0) gifts.add(createGift(1, 4, ruby));
        if (boxVang > 0) gifts.add(createGift(106, 4, boxVang));
        return gifts;
    }
}
