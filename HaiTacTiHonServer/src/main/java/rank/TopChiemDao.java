package rank;

import clan.Clan;
import map.zones.ChiemDao;
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
 * TopChiemDao — BXH Chiếm Đảo Bang Hội.
 * Tách riêng thành class độc lập trong package rank.
 */
public class TopChiemDao extends AbsRanked {

    private static TopChiemDao instance;

    public static TopChiemDao gI() {
        if (instance == null) instance = new TopChiemDao();
        return instance;
    }

    public static final List<InfoMemList> CACHE = new ArrayList<>();
    public static final int TYPE = 16;
    public static final int MAX_ITEMS = 5;

    private static final int[] ISLAND_IDS = {25, 33, 49, 69, 83};
    private static final String[] ISLAND_NAMES = {
        "Đảo Tiền bạc sơ cấp", "Đảo Châu báu sơ cấp", "Đảo Danh vọng", "Đảo Tiền bạc trung cấp", "Đảo Châu báu trung cấp"
    };

    private TopChiemDao() {
        super("Chiếm Đảo Bang Hội", TYPE, MAX_ITEMS);
        this.key = "TOP_CHIEM_DAO";
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
        for (int i = 0; i < ISLAND_IDS.length; i++) {
            Clan clan = ChiemDao.getClanTop(ISLAND_IDS[i]);
            InfoMemList e = new InfoMemList();
            e.id = ISLAND_IDS[i];
            e.rank = (short) i;
            if (clan != null) {
                e.name = "[" + clan.name + "]";
                e.info = ISLAND_NAMES[i] + " - Bang " + clan.name + " (Lv." + clan.level + ")";
            } else {
                e.name = "[Chưa có Bang]";
                e.info = ISLAND_NAMES[i] + " - Đang bỏ trống";
            }
            list.add(e);
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
        gifts.add(createGift(0, 4, 500_000));  // 500k Beri
        gifts.add(createGift(221, 4, 20));     // 20 Đá Hải Thạch
        return gifts;
    }
}
