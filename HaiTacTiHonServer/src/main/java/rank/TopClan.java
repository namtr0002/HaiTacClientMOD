package rank;

import clan.Clan;
import model.Player;
import zabstracts.AbsRanked;
import network.Message;
import template.InfoMemList;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * TopClan — Bảng xếp hạng Băng Hải Tặc.
 *
 * TYPE = 6 — packet type client nhận cho clan BXH.
 * Dedicated class riêng cho Top Clan.
 */
public class TopClan extends AbsRanked {

    private static TopClan instance;

    public static TopClan gI() {
        if (instance == null) instance = new TopClan();
        return instance;
    }

    public static final List<InfoMemList> CACHE = new ArrayList<>();
    public static final int TYPE      = 6;
    public static final int MAX_ITEMS = 50;

    private TopClan() {
        super("Băng Hải Tặc", TYPE, MAX_ITEMS);
        this.key = "TOP_CLAN";
    }

    @Override
    public String getSql() { return ""; }

    @Override
    public InfoMemList buildEntry(ResultSet rs) throws Exception { return null; }

    @Override
    public List<InfoMemList> getCache() { return CACHE; }

    @Override
    public final void update() {
        List<InfoMemList> temp = new ArrayList<>();
        List<Clan> clans = new ArrayList<>();
        if (Clan.BXH != null && !Clan.BXH.isEmpty()) {
            for (int i = 0; i < Clan.BXH.size(); i++) {
                Clan c = Clan.get_clan_by_name(Clan.BXH.get(i));
                if (c != null && c.id >= 0 && !clans.contains(c)) clans.add(c);
            }
        }
        if (clans.isEmpty() && Clan.ENTRY != null) {
            for (Clan c : Clan.ENTRY) {
                if (c != null && c.id >= 0 && c.name != null && !c.name.isEmpty() && !clans.contains(c)) {
                    clans.add(c);
                }
            }
            clans.sort((c1, c2) -> {
                long xp1 = c1.xp + ((long) c1.level * 10000L) + (2400000L * c1.trungsinh);
                long xp2 = c2.xp + ((long) c2.level * 10000L) + (2400000L * c2.trungsinh);
                return Long.compare(xp2, xp1);
            });
        }
        for (int i = 0; i < clans.size() && i < MAX_ITEMS; i++) {
            Clan clan = clans.get(i);
            InfoMemList e = new InfoMemList();
            e.rank       = (short) i;
            e.name       = clan.name;
            e.id         = clan.id;
            temp.add(e);
        }
        CACHE.clear();
        CACHE.addAll(temp);
    }

    @Override
    public void show(Player p, int page) throws IOException {
        if (page < 0) page = 0;

        List<Clan> validClans = new ArrayList<>();
        if (Clan.BXH != null && !Clan.BXH.isEmpty()) {
            for (int i = 0; i < Clan.BXH.size(); i++) {
                String clanName = Clan.BXH.get(i);
                Clan clan = Clan.get_clan_by_name(clanName);
                if (clan != null && clan.id >= 0 && !validClans.contains(clan)) {
                    validClans.add(clan);
                }
            }
        }
        if (validClans.isEmpty() && Clan.ENTRY != null) {
            for (Clan c : Clan.ENTRY) {
                if (c != null && c.id >= 0 && c.name != null && !c.name.isEmpty() && !validClans.contains(c)) {
                    validClans.add(c);
                }
            }
            validClans.sort((c1, c2) -> {
                long xp1 = c1.xp + ((long) c1.level * 10000L) + (2400000L * c1.trungsinh);
                long xp2 = c2.xp + ((long) c2.level * 10000L) + (2400000L * c2.trungsinh);
                return Long.compare(xp2, xp1);
            });
        }

        int bound1 = 0, bound2 = 0;
        int size = validClans.size();

        if (size > 10) {
            if ((page + 1) * 10 > size) {
                bound1 = 10 * page;
                bound2 = size;
                while (bound1 >= bound2 && page > 0) {
                    bound1 -= 10;
                    page--;
                }
            } else {
                bound1 = 10 * page;
                bound2 = bound1 + 10;
            }
        } else if (size > 0) {
            bound1 = 0;
            bound2 = size;
            page   = 0;
        } else {
            bound1 = 0;
            bound2 = 0;
            page   = 0;
        }

        int count = Math.max(0, bound2 - bound1);

        Message m = new Message(-30);
        m.writer().writeByte(TYPE);
        m.writer().writeUTF(title);
        m.writer().writeByte(page);
        m.writer().writeByte(count);

        for (int i = bound1; i < bound2; i++) {
            Clan clan = validClans.get(i);
            short cLevel = (short) Math.max(1, clan.level);
            int xpMax = Math.max(1, Clan.get_xp_max(cLevel, clan.trungsinh));
            float percent = (clan.xp * 100f) / xpMax;
            if (percent > 100f) percent = 100f;

            String info = String.format("TS: %s - Lv: %s + %.2f%%",
                    clan.trungsinh, cLevel, percent);

            m.writer().writeShort(clan.id);
            m.writer().writeUTF(clan.name);
            m.writer().writeUTF(info);
            m.writer().writeShort(clan.icon >= 0 ? clan.icon : 1); // clan icon
            m.writer().writeShort(i);  // rank index
        }

        p.addmsg(m);
        m.cleanup();
    }
}
