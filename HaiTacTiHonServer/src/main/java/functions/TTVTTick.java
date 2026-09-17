package functions;

import model.Player;
import zabstracts.AbsTableTickOption;
import java.io.IOException;
import java.util.List;

public class TTVTTick extends AbsTableTickOption {

    public TTVTTick(Player player) {
        super(player);
    }
    @Override
    public void onAccept(Player leader, List<Player> readyPlayers) throws IOException {
        leader.setyesNoDialog(new model.YesNoDialog(leader, 56, "Thông báo",
                "Phó bản thử thách vệ thần mỗi lần đi tốn 1 chìa khóa, xác nhận vào?",
                new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
        leader.getService().startYesNo();
    }
    
    @Override
    public void onDecline(Player p) throws IOException {
    }
}
