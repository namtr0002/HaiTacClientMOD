package functions;

import model.Player;
import zabstracts.AbsTableTickOption;
import java.io.IOException;
import java.util.List;
import model.YesNoDialog;

public class LienTangTick extends AbsTableTickOption {

    public LienTangTick(Player player) {
        super(player);
    }
    @Override
    public void onAccept(Player leader, List<Player> readyPlayers) throws IOException {
        leader.data_yesno = new int[]{62};
        leader.setyesNoDialog(new YesNoDialog(leader, 62, "Thông báo",
                "Để tham gia phó bản đấu Boss liên tầng mỗi thành viên sẽ phải mất 2 Chìa khóa phó bản, bạn có muốn tham gia?",
                new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}));
        leader.getService().startYesNo();
    }
    
    @Override
    public void onDecline(Player p) throws IOException {
    }
}
