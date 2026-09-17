package map.zones;

import event.EventManager;

import model.Player;
import core.ZUtil;
import network.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import event.EventData;
import core.Manager;
import event.SuKienTrongCay;
import event.SuKienNoel;
import model.Wanted_Chest;

public class WantedFight extends MapPvp {

    public WantedFight(Player p1, Player p2, int mapId) {
        super(p1, p2, (byte) 2, mapId);
    }

    @Override
    protected boolean checkEntryConditions() {
        return player1 != null && player2 != null;
    }

    @Override
    protected void onFightStart() throws IOException {
        // Wanted fight default start
    }

    @Override
    protected void onMatchEnd(Player p_win, Player p_lose) throws IOException {
        if (p_win != null && !p_win.isBot) {
            activities.Pvp.pvp_notice(p_win, 3);
        }
        if (p_lose != null && !p_lose.isBot) {
            activities.Pvp.pvp_notice(p_lose, 4);
        }

        int losePoints = (p_lose != null) ? p_lose.get_wanted_point() : 0;
        long beri_win = (10_000L + (long) losePoints) / 100L;
        long beri_lose = (5_000L + (long) losePoints) / 100L;
        if (p_win != null) {
            p_win.update_wanted_point((int) beri_win);
            if (!p_win.isBot) {
                Wanted_Chest.receiv_ruong(p_win);
            }
        }
        if (p_lose != null) {
            p_lose.update_wanted_point((int) -beri_lose);
        }

        if (p_win != null && !p_win.isBot && event.EventManager.isActive(3) && p_win.eventData != null) {
            EventData temp_select = null;
            for (int i2 = 0; i2 < p_win.eventData.size(); i2++) {
                if (p_win.eventData.get(i2) != null && p_win.eventData.get(i2).eventID == 3) {
                    temp_select = p_win.eventData.get(i2);
                    break;
                }
            }
            if (temp_select != null && temp_select.data != null && temp_select.data.length > 2 && temp_select.data[2] < 10) {
                temp_select.data[2]++;
                if (p_win.item != null) {
                    p_win.item.add_item_bag47(4, 211, 1);
                    p_win.item.updateInventory(false);
                }
            }
        }

        if (p_win != null && !p_win.isBot) {
            event.EventManager.dispatchOnWanted(p_win);
        }
    }
}
