package map.zones;

import model.Player;
import java.io.IOException;

public class FriendlyFight extends MapPvp {

    public FriendlyFight(Player p1, Player p2, int mapId) {
        super(p1, p2, (byte) 1, mapId);
    }

    @Override
    protected boolean checkEntryConditions() {
        return player1 != null && player2 != null;
    }

    @Override
    protected void onFightStart() throws IOException {
        // Friendly fight has no entry fee or requirements
    }

    @Override
    protected void onMatchEnd(Player p_win, Player p_lose) throws IOException {
        if (p_win != null && !p_win.isBot) {
            activities.Pvp.pvp_notice(p_win, 3);
            p_win.updateArchiDaily(9);
            if (p_win.getService() != null) {
                p_win.getService().send_box_ThongBao_OK("Bạn đã thắng trận đấu giao hữu!");
            }
        }
        if (p_lose != null && !p_lose.isBot) {
            activities.Pvp.pvp_notice(p_lose, 4);
            if (p_lose.getService() != null) {
                p_lose.getService().send_box_ThongBao_OK("Bạn đã thua trận đấu giao hữu!");
            }
        }
    }
}
