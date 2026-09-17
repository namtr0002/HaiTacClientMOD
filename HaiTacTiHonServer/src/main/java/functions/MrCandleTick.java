package functions;

import model.Player;
import zabstracts.AbsTableTickOption;
import java.io.IOException;
import java.util.List;

public class MrCandleTick extends AbsTableTickOption {

    public MrCandleTick(Player player) {
        super(player);
    }
    @Override
    public void onAccept(Player leader, List<Player> readyPlayers) throws IOException {
        leader.setyesNoDialog(new model.YesNoDialog(leader, 67, "Thông báo",
                "Để tham gia phó bản Bộ Đôi Mr Candle mỗi thành viên sẽ phải mất 2 chìa khoá phó bản, Bạn có muốn tham gia?",
                new String[]{"Đồng ý", "Huỷ"}, new byte[]{2, 1}));
        leader.getService().startYesNo();
    }
    
    @Override
    public void onDecline(Player p) throws IOException {
    }
}
