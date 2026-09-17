package map.zones;

import event.EventManager;

import model.Player;
import core.ZUtil;
import network.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import achievement.ArchiDaily;
import event.EventData;
import core.Manager;
import event.SuKienTrongCay;
import event.SuKienNoel;

public class SieuHangFight extends MapPvp {

    public SieuHangFight(Player p1, Player p2, int mapId) {
        super(p1, p2, (byte) 0, mapId);
    }

    @Override
    protected boolean checkEntryConditions() {
        return player1 != null && player2 != null;
    }

    @Override
    protected void onFightStart() throws IOException {
        // Tickets were checked and deducted before starting
    }

    @Override
    protected void onMatchEnd(Player p_win, Player p_lose) throws IOException {
        if (p_win != null && !p_win.isBot) {
            activities.Pvp.pvp_notice(p_win, 3);
            p_win.pvp_win++;
            ArchiDaily myArchiDaily = p_win.getArchiDaily(9);
            if (myArchiDaily != null) {
                myArchiDaily.currentNum++;
            }
            p_win.updateArchiDaily(13);
            event.EventManager.dispatchOnPvP(p_win);
        }
        if (p_lose != null && !p_lose.isBot) {
            activities.Pvp.pvp_notice(p_lose, 4);
            p_lose.pvp_lose++;
            ArchiDaily myArchiDaily2 = p_lose.getArchiDaily(9);
            if (myArchiDaily2 != null) {
                myArchiDaily2.currentNum--;
            }
        }

        if (p_win != null && p_lose != null) {
            int chenhLech = p_lose.get_pvpPoint() - p_win.get_pvpPoint();
            if (chenhLech > 15) {
                chenhLech = 15;
            } else if (chenhLech < -15) {
                chenhLech = -15;
            }
            chenhLech += 30;
            int diemwin = chenhLech;
            p_win.update_pvpPoint(diemwin);
            p_lose.update_pvpPoint(-chenhLech);
        }

    }

    @Override
    protected void onMatchTie(Player p1, Player p2) throws IOException {
        if (p1 != null && p2 != null && !p1.equals(p2)) {
            if (this.num_win_p1 > this.num_win_p2) {
                p1.pvp_win++;
                p1.update_pvpPoint(15);
                p2.pvp_lose++;
                p2.update_pvpPoint(-15);
            } else if (this.num_win_p1 < this.num_win_p2) {
                p1.pvp_lose++;
                p1.update_pvpPoint(-15);
                p2.pvp_win++;
                p2.update_pvpPoint(15);
            }
        }
    }
}
