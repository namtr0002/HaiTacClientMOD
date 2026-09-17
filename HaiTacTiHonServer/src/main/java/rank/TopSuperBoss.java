package rank;

import boss.SuperBossManager;
import core.ZUtil;
import model.Player;
import zabstracts.AbsRanked;
import template.GiftBox;
import template.InfoMemList;
import template.Top_Dame;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TopSuperBoss — BXH Sát Thương Siêu Boss.
 * Tách riêng thành class độc lập trong package rank.
 */
public class TopSuperBoss extends AbsRanked {

    private static TopSuperBoss instance;

    public static TopSuperBoss gI() {
        if (instance == null) instance = new TopSuperBoss();
        return instance;
    }

    public static final List<InfoMemList> CACHE = new ArrayList<>();
    public static final int TYPE = 18;
    public static final int MAX_ITEMS = 10;

    private TopSuperBoss() {
        super("Top Sát Thương Boss", TYPE, MAX_ITEMS);
        this.key = "TOP_SUPER_BOSS";
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
        if (SuperBossManager.top_dame_gift != null && !SuperBossManager.top_dame_gift.isEmpty()) {
            for (int i = 0; i < SuperBossManager.top_dame_gift.size() && i < MAX_ITEMS; i++) {
                String entry = SuperBossManager.top_dame_gift.get(i);
                if (entry != null && !entry.isEmpty()) {
                    String[] parts = entry.split(" - ");
                    InfoMemList e = new InfoMemList();
                    e.rank = (short) i;
                    e.name = parts[0];
                    if (parts.length > 1) {
                        e.info = "Sát thương: " + parts[1];
                    } else {
                        e.info = "Top " + (i + 1);
                    }
                    list.add(e);
                }
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
        return SuperBossManager.buildSuperBossTopGifts(rank);
    }
}
