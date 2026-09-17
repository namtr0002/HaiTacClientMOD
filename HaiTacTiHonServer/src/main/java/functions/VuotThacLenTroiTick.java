package functions;

import model.Player;
import zabstracts.AbsTableTickOption;
import map.Zone;
import java.io.IOException;
import java.util.List;

public class VuotThacLenTroiTick extends AbsTableTickOption {

    public VuotThacLenTroiTick(Player player) {
        super(player);
    }
    @Override
    public void onAccept(Player leader, List<Player> readyPlayers) throws IOException {
        Zone[] map_go = Zone.getMapByID(109);
        if (map_go != null && map_go.length > 0) {
            if (leader.party != null) {
                leader.party.activeKeyIndex = 0;
            }
            for (Player p0 : readyPlayers) {
                p0.time_key_red_line = -1;
                p0.key_red_line.clear();
                p0.map.leave_map(p0, 2);
                p0.map = map_go[0];
                p0.x = 50;
                p0.y = 670;
                p0.xold = p0.x;
                p0.yold = p0.y;
                p0.map.goto_map(p0);
                p0.map.update_boat(p0, p0, true);
            }
        }
    }
    
    @Override
    public void onDecline(Player p) throws IOException {
    }
}
