package functions;

import model.Player;
import zabstracts.AbsTableTickOption;
import map.zones.PvpBang;
import java.io.IOException;
import java.util.List;

public class PvpBangTick extends AbsTableTickOption {

    public PvpBangTick(Player player) {
        super(player);
    }
    @Override
    public void onAccept(Player leader, List<Player> readyPlayers) throws IOException {
        if (readyPlayers == null || readyPlayers.isEmpty()) {
            leader.getService().send_box_ThongBao_OK("Không có thành viên nào được tick sẵn sàng!");
            return;
        }
        PvpBang dungeon = PvpBang.createDungeon(leader.clan);
        dungeon.join(readyPlayers);
    }
    
    @Override
    public void onDecline(Player p) throws IOException {
    }
}
