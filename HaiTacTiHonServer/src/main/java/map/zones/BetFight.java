package map.zones;

import model.Player;
import java.io.IOException;

public class BetFight extends MapPvp {

    public long betAmount;

    public BetFight(Player p1, Player p2, int mapId, long betAmount) {
        super(p1, p2, (byte) 3, mapId);
        this.betAmount = betAmount;
    }

    @Override
    protected boolean checkEntryConditions() {
        if (player1 == null || player2 == null) {
            return false;
        }
        if (player1.get_vang() < betAmount) {
            player1.getService().send_box_ThongBao_OK("Bạn không đủ beri để cược!");
            player2.getService().send_box_ThongBao_OK("Đối thủ không đủ beri để cược!");
            return false;
        }
        if (player2.get_vang() < betAmount) {
            player2.getService().send_box_ThongBao_OK("Bạn không đủ beri để cược!");
            player1.getService().send_box_ThongBao_OK("Đối thủ không đủ beri để cược!");
            return false;
        }
        return true;
    }

    @Override
    protected void onFightStart() throws IOException {
        if (player1 != null && !player1.isBot) {
            player1.update_vang(-betAmount);
            if (player1.getService() != null) {
                player1.getService().send_box_ThongBao_OK("Đã trừ " + betAmount + " beri đặt cược.");
            }
            player1.update_info_to_all();
        }
        if (player2 != null && !player2.isBot) {
            player2.update_vang(-betAmount);
            if (player2.getService() != null) {
                player2.getService().send_box_ThongBao_OK("Đã trừ " + betAmount + " beri đặt cược.");
            }
            player2.update_info_to_all();
        }
    }

    @Override
    protected void onMatchEnd(Player p_win, Player p_lose) throws IOException {
        if (p_win != null && !p_win.isBot) {
            activities.Pvp.pvp_notice(p_win, 3);
            p_win.updateArchiDaily(9);
            long winAmount = (long) (betAmount * 2 * 0.95); // 5% fee
            p_win.update_vang(winAmount);
            if (p_win.getService() != null) {
                p_win.getService().send_box_ThongBao_OK("Chúc mừng! Bạn đã thắng và nhận " + winAmount + " beri cược!");
            }
            p_win.update_info_to_all();
        }
        if (p_lose != null && !p_lose.isBot) {
            activities.Pvp.pvp_notice(p_lose, 4);
            if (p_lose.getService() != null) {
                p_lose.getService().send_box_ThongBao_OK("Bạn đã thua cuộc và mất " + betAmount + " beri cược.");
            }
            p_lose.update_info_to_all();
        }
    }
}
