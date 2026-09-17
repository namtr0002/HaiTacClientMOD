package functions;

import model.Player;
import zabstracts.AbsTableTickOption;
import java.io.IOException;
import java.util.List;

public class VuonCamTick extends AbsTableTickOption {

    public VuonCamTick(Player player) {
        super(player);
    }
    @Override
    public void onAccept(Player leader, List<Player> readyPlayers) throws IOException {
        leader.setyesNoDialog(new model.YesNoDialog(leader, 63, "Thông báo",
                "Phó bản vườn cam Namie mỗi lần đi tốn 2 chìa khóa.",
                new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
        leader.getService().startYesNo();
    }
    
    @Override
    public void onDecline(Player p) throws IOException {
    }
}
